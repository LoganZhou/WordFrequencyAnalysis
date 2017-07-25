/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ExtractWords;

import ExportToFile.ExportToExcel;
import ExportToFile.ExportToTxt;
import Frequency.WordElement;
import java.util.ArrayList;
import java.util.Iterator;
import javax.swing.JTextArea;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.util.CellRangeAddress;

/**
 * 按长度检索单词
 * @author zhouheng
 */
public class ExtractByLength {
    private ArrayList<WordElement> wordList;
    public final static int EXPORT_TO_TXT = 1;      //输出txt
    public final static int EXPORT_TO_EXCEL = 2;    //输出excel
    private ExportToTxt txtExporter;        //txt文件输出，必须要先被初始化
    private ExportToExcel excelExporter;    //excel文件输出，必须要先被初始化
    private JTextArea resultArea;           //输出框
    private int length;                     //长度
    /**
     * 按长度检索构造函数，不输出文件
     * @param wordList 文档list
     * @param length 检索长度
     * @param resultArea 结果输出框
     */
    public ExtractByLength(ArrayList<WordElement> wordList, int length, JTextArea resultArea) {
        this.wordList = wordList;
        this.resultArea = resultArea;
        this.length = length;
    }

    /**
     * 按长度检索构造函数，输出txt文件
     * @param wordList 文档list
     * @param length 检索长度
     * @param txtExporter txt文件输出器
     * @param resultArea 结果输出框
     */
    public ExtractByLength(ArrayList<WordElement> wordList, int length, ExportToTxt txtExporter, JTextArea resultArea) {
        this.wordList = wordList;
        this.txtExporter = txtExporter;
        this.resultArea = resultArea;
        this.length = length;
    }
    
    /**
     * 按长度检索构造函数，输出excel
     * @param wordList 文档list
     * @param length 检索长度
     * @param excelExporter excel文件输出器
     * @param resultArea 结果输出框
     */
    public ExtractByLength(ArrayList<WordElement> wordList, int length, ExportToExcel excelExporter, JTextArea resultArea) {
        this.wordList = wordList;
        this.excelExporter = excelExporter;
        this.resultArea = resultArea;
        this.length = length;
    }
    
    /**
     * 输出到结果显示框
     */
    public void analysis() {
        Iterator iter = wordList.iterator();
        resultArea.append("\n################################\n");
        resultArea.append("按长度检索单词（长度：" + length + "）" + "\n");
        resultArea.append(String.format("%1$-10s%2$-20s%3$s\n","单词","词频","词数"));
        while (iter.hasNext()) {
            //遍历文档词频list
            WordElement element = (WordElement)iter.next();
            if (element.getWord().length() ==  length) {
                resultArea.append(String.format("%1$-10s%2$-20.8f%3$d\n",
                        element.getWord(),element.getWordFrequency(),element.getWordCount()));
            }
        }
        resultArea.append("检索完毕！\n");
        resultArea.paintImmediately(resultArea.getBounds());
    }
    
    /**
     * 输出到文件
     * @param exportType 输出文件类型
     */
    public void analysis(int exportType) {
        //先输出到结果显示框
        analysis();
        
        int count = 0;
        if (exportType == EXPORT_TO_TXT) {
            Iterator iter = wordList.iterator();
            txtExporter.writeLine("\n################################\n");
            txtExporter.writeLine("按长度检索单词（长度：" + length + "）" + "\n");
            txtExporter.writeLine(String.format("%1$-10s%2$-20s%3$s\n","单词","词频","词数"));
            while (iter.hasNext()) {
                //遍历文档list
                WordElement element = (WordElement)iter.next();
                if (element.getWord().length() == length) {
                    //System.out.println("单词：" + word + "\t词频：" + frequency);
                    txtExporter.writeLine(String.format("%1$-10s%2$-20.8f%3$d\n",
                        element.getWord(),element.getWordFrequency(),element.getWordCount()));
                    count++;
                }
            }
            txtExporter.writeLine("总计：" + count + "\n");
            txtExporter.writeLine("检索完毕！\n");
        } else if (exportType == EXPORT_TO_EXCEL){
            //创建一个新表
            Sheet newSheet = excelExporter.createNewSheet("长度检索");
            newSheet.addMergedRegion(new CellRangeAddress(0,0,0,2));//合并单元格，创建表名
            Row firstRow = newSheet.createRow(0);
            firstRow.createCell((short)0).setCellValue("按长度检索单词（长度：" + length + "）" + "\n");
            Row secondRow = newSheet.createRow(1);
            secondRow.createCell(0).setCellValue("单词");
            secondRow.createCell(1).setCellValue("词频");
            secondRow.createCell(2).setCellValue("词数");
            int rowNum = 2;//行数从第三行开始
            
            Iterator iter = wordList.iterator();
            while (iter.hasNext()) {
                //遍历文档list
                WordElement element = (WordElement)iter.next();
                if (element.getWord().length() == length) {
                    Row newRow = newSheet.createRow(rowNum);
                    newRow.createCell(0).setCellValue(element.getWord());
                    newRow.createCell(1).setCellValue(element.getWordFrequency());
                    newRow.createCell(2).setCellValue(element.getWordCount());
                    count++;
                    rowNum++;
                }
            }
            newSheet.addMergedRegion(new CellRangeAddress(rowNum,rowNum,0,2));//合并单元格，创建表名
            Row lastRow = newSheet.createRow(rowNum);
            lastRow.createCell(0).setCellValue("总计：" + count);
        }
    }
}
