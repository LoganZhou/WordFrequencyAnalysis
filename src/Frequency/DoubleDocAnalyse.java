/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Frequency;

import ExportToFile.ExportToExcel;
import ExportToFile.ExportToTxt;
import static Frequency.WordFrequencyAnalyse.EXPORT_TO_EXCEL;
import static Frequency.WordFrequencyAnalyse.EXPORT_TO_TXT;
import WordChanges.LemmatizationAnalyse;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import javax.swing.JTextArea;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.util.CellRangeAddress;

/**
 *
 * @author zhouheng
 */
public class DoubleDocAnalyse {

    private WordElement[] contentWordsA;     //分词后的原形数组A
    private WordElement[] contentWordsB;    //分词后的原形数组B
    private ExportToTxt txtExporter;        //txt文件输出，必须要先被初始化
    private ExportToExcel excelExporter;    //excel文件输出，必须要先被初始化
    public final static int EXPORT_TO_TXT = 1;      //输出txt
    public final static int EXPORT_TO_EXCEL = 2;    //输出excel
    private JTextArea resultArea;
    private String documentA;            //原始文件B
    private String documentB;           //原始文件B
    private int countA;                 //文档A的词数
    private int countB;                 //文档B的词数
    private int sameCount;              //两份文档中相同词数
    private HashMap<String, WordElement> docMapA;   //文档A的HashMap
    private HashMap<String, WordElement> docMapB;   //文档B的HashMap

    public DoubleDocAnalyse(String documentA, String documentB, JTextArea resultArea) {
        this.resultArea = resultArea;
        this.documentA = documentA;
        this.documentB = documentB;
        countA = 0;
        countB = 0;
        sameCount = 0;
    }

    public DoubleDocAnalyse(String documentA, String documentB, JTextArea resultArea, ExportToTxt txtExporter) {
        this.txtExporter = txtExporter;
        this.resultArea = resultArea;
        this.documentA = documentA;
        this.documentB = documentB;
        countA = 0;
        countB = 0;
        sameCount = 0;
    }

    public DoubleDocAnalyse(String documentA, String documentB, JTextArea resultArea, ExportToExcel excelExporter) {
        this.excelExporter = excelExporter;
        this.resultArea = resultArea;
        this.documentA = documentA;
        this.documentB = documentB;
        countA = 0;
        countB = 0;
        sameCount = 0;
    }

    public void setTxtExporter(ExportToTxt txtExporter) {
        this.txtExporter = txtExporter;
    }

    public void setExcelExporter(ExportToExcel excelExporter) {
        this.excelExporter = excelExporter;
    }
    
    
    /**
     * 将WordElement[]数组转换成HashMap
     */
    public void wordElementToHashMap() {
        countTextWords(true);  //A文档计数
        countTextWords(false); //B文档计数
        
        docMapA = new HashMap<String, WordElement>();
        docMapB = new HashMap<String, WordElement>();
        for (WordElement element : contentWordsA) {
            docMapA.put(element.getWord(), element);
        }
        for (WordElement element : contentWordsB) {
            docMapB.put(element.getWord(), element);
        }
    }
    
    /**
     * 查找相同词，并输出到显示框
     * 注意：使用前必须调用wordElementToHashMap（）进行预处理
     */
    public void findSameWords() {
        resultArea.append("\n\n################################\n");
        resultArea.append("相同词分析\n");
        resultArea.append("相同词\t\t文档A词频\t\t文档B词频\n");
        WordElement wordA = null;
        WordElement wordB = null;
        Iterator iter = docMapA.entrySet().iterator();
        while (iter.hasNext()) {
            Map.Entry entry = (Map.Entry) iter.next();
            String word = (String) entry.getKey();
            if (docMapB.containsKey(word)) {
                sameCount++;
                wordA = docMapA.get(word);
                wordB = docMapB.get(word);
                resultArea.append(word + "\t\t" + wordA.getWordFrequency()
                        + "\t\t" + wordB.getWordFrequency() + "\n");
            }
        }
        resultArea.append("--------------------------------------\n");
        resultArea.append("相同词数：" + sameCount + "\n");
        resultArea.append("分析完毕\n");
    }
    
    /**
     * 双文档相同词分析，结果输出到文件
     * 注意：使用前必须调用wordElementToHashMap（）进行预处理
     * @param exportType 输出类型
     */
    public void findSameWords(int exportType) {
        findSameWords();

        //int count = 0;
        if (exportType == EXPORT_TO_TXT) {
            txtExporter.writeLine("\n\n################################\n");
            txtExporter.writeLine("相同词分析\n");
            txtExporter.writeLine("相同词\t\t文档A词频\t\t文档B词频\n");
            WordElement wordA = null;
            WordElement wordB = null;
            Iterator iter = docMapA.entrySet().iterator();
            while (iter.hasNext()) {
                Map.Entry entry = (Map.Entry) iter.next();
                String word = (String) entry.getKey();
                if (docMapB.containsKey(word)) {
                    //sameCount++;  只需要在输出框遍历
                    wordA = docMapA.get(word);
                    wordB = docMapB.get(word);
                    txtExporter.writeLine(word + "\t\t" + wordA.getWordFrequency()
                            + "\t\t" + wordB.getWordFrequency() + "\n");
                }
            }
            txtExporter.writeLine("--------------------------------------\n");
            txtExporter.writeLine("相同词数：" + sameCount + "\n");
            txtExporter.writeLine("分析完毕\n");
        } else if (exportType == EXPORT_TO_EXCEL) {
            //创建一个新表
            WordElement wordA = null;
            WordElement wordB = null;
            
            Sheet newSheet = excelExporter.createNewSheet("相同词分析");
            newSheet.addMergedRegion(new CellRangeAddress(0, 0, 0, 2));//合并单元格，创建表名
            Row firstRow = newSheet.createRow(0);
            firstRow.createCell((short) 0).setCellValue("双文档相同词分析");
            Row secondRow = newSheet.createRow(1);
            secondRow.createCell(0).setCellValue("单词");
            secondRow.createCell(1).setCellValue("文档A词频");
            secondRow.createCell(2).setCellValue("文档B词频");
            int rowNum = 2;//行数从第三行开始
            
            Row newRow = null;
            Iterator iter = docMapA.entrySet().iterator();
            while (iter.hasNext()) {
                Map.Entry entry = (Map.Entry) iter.next();
                String word = (String) entry.getKey();
                if (docMapB.containsKey(word)) {
                    //sameCount++;   只需要在输出框遍历
                    wordA = docMapA.get(word);
                    wordB = docMapB.get(word);
                    newRow = newSheet.createRow(rowNum);
                    newRow.createCell(0).setCellValue(word);
                    newRow.createCell(1).setCellValue(wordA.getWordFrequency());
                    newRow.createCell(2).setCellValue(wordB.getWordFrequency());
                    rowNum++;
                }
            }
            
            newSheet.addMergedRegion(new CellRangeAddress(rowNum, rowNum, 0, 2));//合并单元格，创建表名
            Row lastRpw = newSheet.createRow(rowNum);
            lastRpw.createCell((short)0).setCellValue("相同词数：" + sameCount);
        }
    }

    /**
     * 遍历两份文档的词数，更新count
     */
    private void iterateWordElements() {
        for (WordElement element : contentWordsA) {
            countA += element.getWordCount();
        }
        for (WordElement element : contentWordsB) {
            countB += element.getWordCount();
        }
    }

    /**
     * 双文法词数统计,结果仅输出到显示框
     */
    public void wordsCount() {
        countTextWords(true);  //A文档计数
        countTextWords(false); //B文档计数

        //遍历计算
        iterateWordElements();
        resultArea.append("\n\n################################\n");
        resultArea.append("文档词数统计结果\n");
        resultArea.append("文档A词数：" + countA + "\n");
        resultArea.append("文档B词数：" + countB + "\n");
        resultArea.append("文档词数统计完毕！\n");
    }
    
    /**
     * 双文法词数统计，并将结果输出到文件
     * @param exportType 结果输出类型
     */
    public void wordsCount(int exportType) {
        DoubleDocAnalyse.this.wordsCount();

        //由于analysis（）已经完成计数，所以直接输出
        if (exportType == EXPORT_TO_TXT) {
            txtExporter.writeLine("\n\n################################\n");
            txtExporter.writeLine("文档词数统计结果\n");
            txtExporter.writeLine("文档A词数：" + countA + "\n");
            txtExporter.writeLine("文档B词数：" + countB + "\n");
            txtExporter.writeLine("文档词数统计完毕！\n");
        } else if (exportType == EXPORT_TO_EXCEL) {
            //创建一个新表
            Sheet newSheet = excelExporter.createNewSheet("双文档词数");
            newSheet.addMergedRegion(new CellRangeAddress(0, 0, 0, 1));//合并单元格，创建表名
            Row newRow = newSheet.createRow(0);
            newRow.createCell((short) 0).setCellValue("双文档词数统计");
            newRow = newSheet.createRow(1);
            newRow.createCell(0).setCellValue("文档");
            newRow.createCell(1).setCellValue("词数");

            newRow = newSheet.createRow(2);
            newRow.createCell(0).setCellValue("文档A");
            newRow.createCell(1).setCellValue(countA);

            newRow = newSheet.createRow(3);
            newRow.createCell(0).setCellValue("文档B");
            newRow.createCell(1).setCellValue(countB);
        }
    }

    /*
     * 单词统计及频率计算
     * 本方法将所传入的字符串经过分词、分句、词形还原等处理，得到所有单词的原形，
     * 统计原形字符串中出现的所有单词及其对应的频率。
     * 
     * @param string（待分析文件的字符串）
     * @param functionType（功能类型――按字母升降序或按频率升降序）
     * 
     * @return HashMap（存放单词内容和单词频率） 
     */
    private /*HashMap<String, Double>*/ void countTextWords(boolean isDocumentA) {
        String lemmaContent = null;
        if (isDocumentA) {
            lemmaContent = LemmatizationAnalyse.lemmatize(documentA);
        } else {
            lemmaContent = LemmatizationAnalyse.lemmatize(documentB);
        }
        String[] contentArray = lemmaContent.split(" ");
        //String stringArray[]=string.split("[^a-zA-Z]");  //正则表达式识别字母
        /*
         * 创建哈希图存放单词及其累计个数
         */
        final Map<String, Integer> map = new HashMap<String, Integer>();  //存放单词及统计数目
        //final Map<String,Double> resultMap=new HashMap<String,Double>();	//存放单词及计算得到的频率
        Integer count;

        for (int i = 0; i < contentArray.length; i++) {
            count = map.get(contentArray[i]);
            if (count == null) {
                map.put(contentArray[i], 1);
            } else {
                map.put(contentArray[i], count + 1);
            }
        }
        map.remove("\'\'");
        map.remove("'s");
        map.remove(",");
        map.remove(".");
        map.remove("?");
        map.remove("!");
        map.remove("``");

        /*
         * 初始化单词元素数组
         */
        int size = map.size();//获取数组的大小
        //System.out.println(size);
        WordElement[] wordElement = new WordElement[size];//存放单词信息（单词+词频+词数）
        for (int i = 0; i < size; i++) {
            wordElement[i] = new WordElement();
        }

        Set set = map.keySet();
        Integer sum = 0, value;
        String key;
        //第一次遍历求出单词的总个数
        int i = 0;//初始化计数器
        for (Iterator iter = set.iterator(); iter.hasNext();) {
            key = (String) iter.next();
            value = map.get(key);
            wordElement[i].setWord(key);
            wordElement[i].setWordCount(value);
            sum += value;
            i++;
        }
        //第二次遍历求出频率，并保存到新的HashMap中
        i = 0;//初始化计数器
        for (Iterator iter = set.iterator(); iter.hasNext();) {
            key = (String) iter.next();
            //resultMap.put(key, (double)map.get(key)/sum);
            wordElement[i].setWordFrequency((double) wordElement[i].getWordCount() / sum);
            i++;
        }
        if (isDocumentA) {
            contentWordsA = wordElement;
        } else {
            contentWordsB = wordElement;
        }
        //this.contentWords = wordElement;
        //return wordElement;
    }
}
