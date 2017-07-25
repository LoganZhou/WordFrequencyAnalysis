/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package MultiThread;
import ExportToFile.ExportToExcel;
import ExportToFile.ExportToTxt;
import Frequency.WordElement;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.FutureTask;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JTextArea;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.util.CellRangeAddress;

/**
 * 多线程管理器
 * @author Dementors
 */
public class MultiThreadsManager implements Runnable{
    private File originalFile;          //原始待分析文档
    private int dividedNum;             //拆分文档个数
    public static final int EXPORT_TO_TXT = 1;
    public static final int EXPORT_TO_EXCEL = 2;
    private int allWordsCOunt;          //整个文档的词数
    private ArrayList<File> fileList;   //拆分后文件的路径
    private HashMap<String,WordElement> resultMap;      //结果Map
    private final ExecutorService threadsPool;          //线程池
    private final JTextArea resultArea;                       //结果显示框
    private ArrayList<FutureTask<ThreadResult>> futureTasks;        //线程任务集合
    private ExportToTxt txtExporter;
    private ExportToExcel excelExporter;
    private int exportType;             //分析结果输出类型
    private boolean  chooseExport;      //受否选择输出文件

    /**
     * 大文档多线程词频分析，结果输出到显示框
     * @param targetFile 待分析文档
     * @param resultArea 结果输出框
     */
    public MultiThreadsManager(File targetFile, JTextArea resultArea) {
        threadsPool = Executors.newFixedThreadPool(4);
        this.originalFile = targetFile;
        this.fileList = new ArrayList<File>();
        this.resultMap = new HashMap<String,WordElement>();
        this.futureTasks = new ArrayList<FutureTask<ThreadResult>>();
        this.resultArea = resultArea;
        allWordsCOunt = 0;
        this.chooseExport = false;
    }
    
    /**
     * 大文档多线程词频分析，结果输出到txt
     * @param targetFile 待分析文档
     * @param resultArea 结果显示框
     * @param txtExporter txt文件输出器
     */
    public MultiThreadsManager(File targetFile, JTextArea resultArea, ExportToTxt txtExporter) {
        threadsPool = Executors.newFixedThreadPool(4);
        this.originalFile = targetFile;
        this.resultArea = resultArea;
        this.txtExporter = txtExporter;
        this.fileList = new ArrayList<File>();
        this.resultMap = new HashMap<String,WordElement>();
        this.futureTasks = new ArrayList<FutureTask<ThreadResult>>();
        allWordsCOunt = 0;
        chooseExport = true;
        exportType = EXPORT_TO_TXT;
    }

    /**
     * 大文档多线程词频分析，结果输出到excel
     * @param originalFile 待分析文档
     * @param resultArea 结果显示框
     * @param excelExporter excel文件输出器
     */
    public MultiThreadsManager(File originalFile, JTextArea resultArea, ExportToExcel excelExporter) {
        this.originalFile = originalFile;
        this.resultArea = resultArea;
        this.excelExporter = excelExporter;
        this.fileList = new ArrayList<File>();
        this.resultMap = new HashMap<String,WordElement>();
        this.futureTasks = new ArrayList<FutureTask<ThreadResult>>();
        threadsPool = Executors.newFixedThreadPool(4);
        allWordsCOunt = 0;
        chooseExport = true;
        exportType = EXPORT_TO_EXCEL;
    }
    
    
    
    private void divideFile() {
        try {
            dividedNum = DivideFile.divideFile(originalFile);
        } catch (IOException ex) {
            System.out.println("拆分错误");
            ex.printStackTrace();
        }
        for (int i=0;i<dividedNum;i++) {
            fileList.add(new File(i + "_" + originalFile.getName()));
        }
    } 
    
    private void launchThreads() {
        Iterator iter = fileList.iterator();
        while (iter.hasNext()) {
            File subFile = (File)iter.next();
            AnalyseThread thread = new AnalyseThread(subFile);
            FutureTask<ThreadResult> futureTask = new FutureTask<ThreadResult>(thread);
            futureTasks.add(futureTask);
            threadsPool.submit(futureTask);
        }
        ThreadResult subResult;
        
        //遍历所有线程结果
        for (FutureTask<ThreadResult> task : futureTasks) {
            try {
                subResult = task.get();
                allWordsCOunt += subResult.getWordsCount();      //累加拆分文档词数
                HashMap<String, WordElement>subMap = subResult.getWordsMap();
                
                //遍历结果，合并到总结果中
                Iterator subIterator = subMap.entrySet().iterator();
                while(subIterator.hasNext()) {
                    Map.Entry entry = (Map.Entry) subIterator.next();          
                    String word = (String)entry.getKey();
                    WordElement element =(WordElement) entry.getValue();
                    if(resultMap.containsKey(word)) {       //如果总结果中存在改词，则更新其计数
                        resultMap.get(word).addWordCount(element.getWordCount());
                    } else {
                        resultMap.put(word, element);
                    }
                }
            } catch (InterruptedException ex) {
                Logger.getLogger(MultiThreadsManager.class.getName()).log(Level.SEVERE, null, ex);
            } catch (ExecutionException ex) {
                Logger.getLogger(MultiThreadsManager.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        threadsPool.shutdown();
        System.out.println("Multi:Finished!");
    }
    
    /**
     * 计算词频
     */
    private void calculateFrequency() {
        Iterator subIterator = resultMap.entrySet().iterator();
        while (subIterator.hasNext()) {
            Map.Entry entry = (Map.Entry) subIterator.next();
            WordElement element = (WordElement) entry.getValue();
            double frequency = (double)element.getWordCount()/(double)allWordsCOunt;
            element.setWordFrequency(frequency);
        }
    }
    
    public void showToTextArea() {
        resultArea.append("################################\n");
        resultArea.append("词频统计（默认排序）\n");
        resultArea.append(String.format("%-40s\t\t%s\n", "单词","词频"));
        
        Iterator iter = resultMap.entrySet().iterator();
        while(iter.hasNext()) {
            Map.Entry entry = (Map.Entry)iter.next();
            WordElement element = (WordElement)entry.getValue();
            resultArea.append(String.format("%-40s\t\t%.8f\n",element.getWord(),element.getWordFrequency()));
        }   
       
        resultArea.append("词频统计（默认排序）分析完毕！\n");
    }
    
    public void exportToFile(int exportType) {
        showToTextArea();       //输出到显示框
        
        if (exportType == EXPORT_TO_TXT) {
            txtExporter.writeLine("################################\n");
            txtExporter.writeLine("词频统计（默认排序）\n");
            txtExporter.writeLine(String.format("%-40s\t\t%s\n", "单词","词频"));
            //遍历
            Iterator iter = resultMap.entrySet().iterator();
            while (iter.hasNext()) {
                Map.Entry entry = (Map.Entry) iter.next();
                WordElement element = (WordElement) entry.getValue();
                txtExporter.writeLine(String.format("%-40s\t\t%.8f\n", element.getWord(), element.getWordFrequency()));
            }
            
            txtExporter.writeLine("词频统计（默认排序）分析完毕！\n");
        } else if (exportType == EXPORT_TO_EXCEL){
            //创建一个新表
            Sheet newSheet = excelExporter.createNewSheet("词频统计");
            newSheet.addMergedRegion(new CellRangeAddress(0,0,0,1));//合并单元格，创建表名
            Row firstRow = newSheet.createRow(0);
            firstRow.createCell((short)0).setCellValue("词频统计（默认排序）");
            Row secondRow = newSheet.createRow(1);
            secondRow.createCell(0).setCellValue("单词");
            secondRow.createCell(1).setCellValue("词频");
            int rowNum = 2;//行数从第三行开始
            
            Iterator iter = resultMap.entrySet().iterator();
            while (iter.hasNext()) {
                Map.Entry entry = (Map.Entry) iter.next();
                WordElement element = (WordElement) entry.getValue();
                
                Row newRow = newSheet.createRow(rowNum);
                newRow.createCell(0).setCellValue(element.getWord());
                newRow.createCell(1).setCellValue(element.getWordFrequency());
                rowNum++;
            }
        }
    }
    
    /**
     * 
     */
    private void deleteTempFile() {
        Iterator iter = fileList.iterator();
        while(iter.hasNext()) {
            File tempFile = (File)iter.next();
            if(tempFile.exists()) {
                tempFile.delete();
            }
        }
    }
    
    
    @Override
    public void run() {
        divideFile();           //拆分文档
        launchThreads();        //分发任务
        calculateFrequency();   //子线程分析完毕，汇总计算词频
        if(chooseExport) {      //输出到文件
            exportToFile(exportType);
        } else {                //仅输出到显示框
            showToTextArea();
        }
        if(txtExporter!=null) {
            txtExporter.close();
        }
        if(excelExporter!=null) {
            excelExporter.close();
        }
        deleteTempFile();       //清理临时文件
    }
}
