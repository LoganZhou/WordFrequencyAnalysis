/**
 * Created by zhouheng on 2017/7/4.
 */
package ExtractWords;

import ExportToFile.ExportToExcel;
import ExportToFile.ExportToTxt;
import Frequency.WordElement;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.swing.JTextArea;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.util.CellRangeAddress;

/**
 * 单词检索模块
 * 带文件输出
 */
public class ExtractWords {
    //private HashMap<String, Double> wordsMap = new HashMap<String, Double>();     //文档词频HashMap
    private ArrayList<WordElement> wordList;
    public final static int EXPORT_TO_TXT = 1;      //输出txt
    public final static int EXPORT_TO_EXCEL = 2;    //输出excel
    private ExportToTxt txtExporter;        //txt文件输出，必须要先被初始化
    private ExportToExcel excelExporter;    //excel文件输出，必须要先被初始化
    private JTextArea resultArea;
    
    /**
     * 单词检索（输出txt）构造函数
     * @param resultArea 结果输出框
     * @param wordList 文档转换为原型的list
     * @param txtExporter txt文件输出器
     * @deprecated 
     */
    public ExtractWords(JTextArea resultArea, ArrayList<WordElement> wordList, ExportToTxt txtExporter) {
        this.resultArea = resultArea;
        this.wordList= wordList;
        this.txtExporter = txtExporter;
    }
    
    /**
     * 单词检索（输出excel）构造函数
     * @param resultArea 文本输出框
     * @param wordList 文档ArrayList
     * @param excelExporter excel输出器
     * @deprecated
     */
    public ExtractWords (JTextArea resultArea, ArrayList<WordElement> wordList, ExportToExcel excelExporter) {
        this.resultArea = resultArea;
        this.wordList = wordList;
        this.excelExporter = excelExporter;
    }
    
    
    /**
     * 按长度检索并输出到文件
     * @param length 长度
     * @param exportType 输出类型
     * @deprecated
     */
    public void extractByLength(int length, int exportType) {
        int count = 0;
        if (exportType == EXPORT_TO_TXT) {
            Iterator iter = wordList.iterator();
            //Iterator iter = wordsMap.entrySet().iterator();
            //System.out.println("按长度检索单词（长度：" + length + "）");
            txtExporter.writeLine("\n################################\n");
            txtExporter.writeLine("按长度检索单词（长度：" + length + "）" + "\n");
            while (iter.hasNext()) {
                //遍历文档词频HashMap
//                Map.Entry entry = (Map.Entry) iter.next();
//                String word = (String) entry.getKey();
//                Double frequency = (Double) entry.getValue();
                WordElement element = (WordElement)iter.next();
                if (element.getWord().length() == length) {
                    //System.out.println("单词：" + word + "\t词频：" + frequency);
                    txtExporter.writeLine("单词：" + element.getWord() + "\t\t词频：" 
                            + element.getWordFrequency() + "\t\t词数：" + element.getWordCount() + "\n");
                    count++;
                }
            }
            //System.out.println("检索完毕！");
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
                //遍历文档词频HashMap
                //Map.Entry entry = (Map.Entry) iter.next();
                //String word = (String) entry.getKey();
                //Double frequency = (Double) entry.getValue();
                WordElement element = (WordElement)iter.next();
                if (element.getWord().length() == length) {
                    Row newRow = newSheet.createRow(rowNum);
                    newRow.createCell(0).setCellValue(element.getWord());
                    newRow.createCell(1).setCellValue(element.getWordFrequency());
                    newRow.createCell(1).setCellValue(element.getWordCount());
                    count++;
                    rowNum++;
                }
            }
            newSheet.addMergedRegion(new CellRangeAddress(rowNum,rowNum,0,2));//合并单元格，创建表名
            Row lastRow = newSheet.createRow(rowNum);
            lastRow.createCell(0).setCellValue("总计：" + count);
        }
    }
    
    /**
     * 按词头检索单词，并输出到文件
     * @param beginStr 词头字符串
     * @param exportType 输出类型
     * @deprecated
     */
    public void extractByBeginning(String beginStr, int exportType) {
        int count = 0;
        if (exportType == EXPORT_TO_TXT) {
            Iterator iter = wordList.iterator();
            //Iterator iter = wordsMap.entrySet().iterator();
            //System.out.println("按长度检索单词（长度：" + length + "）");
            txtExporter.writeLine("\n################################\n");
            txtExporter.writeLine("按词头检索单词（词头：" + beginStr + "）" + "\n");
            String pattern = "(^" + beginStr + "\\w+)";  //正则表达式
            Pattern r = Pattern.compile(pattern);       //创建Pattern对象
            while (iter.hasNext()) {
                //遍历文档词频HashMap
                WordElement element = (WordElement)iter.next();
                Matcher m = r.matcher(element.getWord());
                if (m.find()) {
                    //System.out.println("单词：" + word + "\t词频：" + frequency);
                    txtExporter.writeLine("单词：" + element.getWord() + "\t\t词频：" 
                            + element.getWordFrequency() + "\t\t词数：" + element.getWordCount() + "\n");
                    count++;
                }
            }
            //System.out.println("检索完毕！");
            txtExporter.writeLine("总计：" + count + "\n");
            txtExporter.writeLine("检索完毕！\n");
        } else if (exportType == EXPORT_TO_EXCEL){
            //创建一个新表
            Sheet newSheet = excelExporter.createNewSheet("词头检索");
            newSheet.addMergedRegion(new CellRangeAddress(0,0,0,2));//合并单元格，创建表名
            Row firstRow = newSheet.createRow(0);
            firstRow.createCell((short)0).setCellValue("按词头检索单词（词头：" + beginStr + "）" + "\n");
            Row secondRow = newSheet.createRow(1);
            secondRow.createCell(0).setCellValue("单词");
            secondRow.createCell(1).setCellValue("词频");
            secondRow.createCell(2).setCellValue("词数");
            int rowNum = 2;//行数从第三行开始
            
            Iterator iter = wordList.iterator();
            String pattern = "(^" + beginStr + "\\w+)";  //正则表达式
            Pattern r = Pattern.compile(pattern);       //创建Pattern对象
            while (iter.hasNext()) {
                //遍历文档词频HashMap
                //Map.Entry entry = (Map.Entry) iter.next();
                //String word = (String) entry.getKey();
                //Double frequency = (Double) entry.getValue();
                WordElement element = (WordElement)iter.next();
                Matcher m = r.matcher(element.getWord());
                if (m.find()) {
                    Row newRow = newSheet.createRow(rowNum);
                    newRow.createCell(0).setCellValue(element.getWord());
                    newRow.createCell(1).setCellValue(element.getWordFrequency());
                    newRow.createCell(1).setCellValue(element.getWordCount());
                    count++;
                    rowNum++;
                }
            }
            newSheet.addMergedRegion(new CellRangeAddress(rowNum,rowNum,0,2));//合并单元格，创建表名
            Row lastRow = newSheet.createRow(rowNum);
            lastRow.createCell(0).setCellValue("总计：" + count);
        }
    }
    
    /**
     * 按词尾检索，并输出文件
     * @param endStr 词尾
     * @param exportType 输出类型 
     * @deprecated
     */
    public void extractByEndding(String endStr, int exportType) {
        int count = 0;
        if (exportType == EXPORT_TO_TXT) {
            Iterator iter = wordList.iterator();
            //Iterator iter = wordsMap.entrySet().iterator();
            //System.out.println("按长度检索单词（长度：" + length + "）");
            txtExporter.writeLine("\n################################\n");
            txtExporter.writeLine("按词尾检索单词（词头：" + endStr + "）" + "\n");
            String pattern = "(\\w+" + endStr + "$)";  //正则表达式
            Pattern r = Pattern.compile(pattern);       //创建Pattern对象
            while (iter.hasNext()) {
                //遍历文档词频HashMap
                WordElement element = (WordElement)iter.next();
                Matcher m = r.matcher(element.getWord());
                if (m.find()) {
                    //System.out.println("单词：" + word + "\t词频：" + frequency);
                    txtExporter.writeLine("单词：" + element.getWord() + "\t\t词频：" 
                            + element.getWordFrequency() + "\t\t词数：" + element.getWordCount() + "\n");
                    count++;
                }
            }
            //System.out.println("检索完毕！");
            txtExporter.writeLine("总计：" + count + "\n");
            txtExporter.writeLine("检索完毕！\n");
        } else if (exportType == EXPORT_TO_EXCEL){
            //创建一个新表
            Sheet newSheet = excelExporter.createNewSheet("词尾检索");
            newSheet.addMergedRegion(new CellRangeAddress(0,0,0,2));//合并单元格，创建表名
            Row firstRow = newSheet.createRow(0);
            firstRow.createCell((short)0).setCellValue("按词尾检索单词（词头：" + endStr + "）" + "\n");
            Row secondRow = newSheet.createRow(1);
            secondRow.createCell(0).setCellValue("单词");
            secondRow.createCell(1).setCellValue("词频");
            secondRow.createCell(2).setCellValue("词数");
            int rowNum = 2;//行数从第三行开始
            
            Iterator iter = wordList.iterator();
            String pattern = "(\\w+" + endStr + "$)";  //正则表达式
            Pattern r = Pattern.compile(pattern);       //创建Pattern对象
            while (iter.hasNext()) {
                //遍历文档词频HashMap
                //Map.Entry entry = (Map.Entry) iter.next();
                //String word = (String) entry.getKey();
                //Double frequency = (Double) entry.getValue();
                WordElement element = (WordElement)iter.next();
                Matcher m = r.matcher(element.getWord());
                if (m.find()) {
                    Row newRow = newSheet.createRow(rowNum);
                    newRow.createCell(0).setCellValue(element.getWord());
                    newRow.createCell(1).setCellValue(element.getWordFrequency());
                    newRow.createCell(1).setCellValue(element.getWordCount());
                    count++;
                    rowNum++;
                }
            }
            newSheet.addMergedRegion(new CellRangeAddress(rowNum,rowNum,0,2));//合并单元格，创建表名
            Row lastRow = newSheet.createRow(rowNum);
            lastRow.createCell(0).setCellValue("总计：" + count);
        }
    }
    
    /**
     * 按长度检索
     * @param length 长度
     * @deprecated
     */
    public void extractByLength(int length) {
        Iterator iter = wordList.iterator();
        //System.out.println("按长度检索单词（长度：" + length + "）");
        resultArea.append("\n################################\n");
        resultArea.append("按长度检索单词（长度：" + length + "）" + "\n");
        while (iter.hasNext()) {
            //遍历文档词频HashMap
            WordElement element = (WordElement)iter.next();
//            Map.Entry entry = (Map.Entry)iter.next();
//            String word = (String)entry.getKey();
//            Double frequency = (Double)entry.getValue();
            if (element.getWord().length() ==  length) {
                //System.out.println("单词：" + word + "\t词频：" + frequency);
                resultArea.append("单词：" + element.getWord() + "\t\t词频：" 
                            + element.getWordFrequency() + "\t\t词数：" + element.getWordCount() + "\n");
            }
        }
        //System.out.println("检索完毕！");
        resultArea.append("检索完毕！\n");
        resultArea.paintImmediately(resultArea.getBounds());
    }

    /**
     * 按词头检索
     * @param beginStr 词头字符串
     * @deprecated
     */
    public void extractByBeginning(String beginStr) {
        Iterator iter = wordList.iterator();
        resultArea.append("\n################################\n");
        //System.out.println("按词头检索单词（词头：" + beginStr + "）");
        resultArea.append("按词头检索单词（词头：" + beginStr + "）" + "\n");
        String pattern = "(^" + beginStr + "\\w+)";  //正则表达式
        Pattern r = Pattern.compile(pattern);       //创建Pattern对象

        while (iter.hasNext()) {
            //遍历文档词频HashMap
            WordElement element = (WordElement)iter.next();
//            Map.Entry entry = (Map.Entry)iter.next();
//            String word = (String)entry.getKey();
//            Double frequency = (Double)entry.getValue();
            Matcher m = r.matcher(element.getWord());
            if (m.find()) {
                //System.out.println("单词：" + word + "\t词频：" + frequency);
                resultArea.append("单词：" + element.getWord() + "\t\t词频：" 
                            + element.getWordFrequency() + "\t\t词数：" + element.getWordCount() + "\n");
            }
        }
        //System.out.println("检索完毕！");
        resultArea.append("检索完毕！\n");
        resultArea.paintImmediately(resultArea.getBounds());
    }

    /**
     * 按词尾检索
     * @param endStr 词尾字符串
     * @deprecated
     */
    public void extractByEndding(String endStr) {
        Iterator iter = wordList.iterator();
        //System.out.println("按词尾检索单词（词尾：" + endStr + "）");
        resultArea.append("\n################################\n");
        resultArea.append("按词尾检索单词（词尾：" + endStr + "）" + "\n");
        String pattern = "(\\w+" + endStr + "$)";  //正则表达式
        Pattern r = Pattern.compile(pattern);       //创建Pattern对象

        while (iter.hasNext()) {
            //遍历文档词频HashMap
            WordElement element = (WordElement)iter.next();
//            Map.Entry entry = (Map.Entry)iter.next();
//            String word = (String)entry.getKey();
//            Double frequency = (Double)entry.getValue();
            Matcher m = r.matcher(element.getWord());
            if (m.find()) {
                //System.out.println("单词：" + word + "\t词频：" + frequency);
                resultArea.append("单词：" + element.getWord() + "\t\t词频：" 
                            + element.getWordFrequency() + "\t\t词数：" + element.getWordCount() + "\n");
            }
        }
        //System.out.println("检索完毕！");
        resultArea.append("检索完毕！\n");
        resultArea.paintImmediately(resultArea.getBounds());
    }
}
