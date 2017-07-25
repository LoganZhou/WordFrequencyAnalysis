/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Program;

import ExportToFile.ExportToExcel;
import ExportToFile.ExportToTxt;
import ExtractWords.ExtractByBeginning;
import ExtractWords.ExtractByEndding;
import ExtractWords.ExtractByLength;
import Frequency.SortedResult;
import Frequency.WordElement;
import Frequency.WordFrequencyAnalyse;
import ImportFile.ImportFile;
import OutOfBound.OutOfBoundAnalysis;
import Syntactic.SentenceAnalyse;
import Dialog.ProgressDialog;
import WordChanges.LemmatizationAnalyse;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JButton;
import javax.swing.JTextArea;

/**
 *
 * @author zhouheng
 */
public class SingleFileAnalyser2 implements Runnable {

    public static final int EXPORT_TO_TXT = 1;
    public static final int EXPORT_TO_EXCEL = 2;
    private File targetFile;
    private boolean chooseFrequency;
    private boolean chooseFreqAscend;
    private boolean chooseFreqDescend;
    private boolean chooseAlphaAscend;
    private boolean chooseAlphaDescend;
    private boolean chooseChangeAnalysis;
    private boolean chooseOutOfBound;
    private boolean chooseExtractLength;
    private boolean chooseExtractBegin;
    private boolean chooseExtractEnd;
    private boolean chooseExport;
    private boolean chooseSyntactic;
    private int extractLength;
    private String extractBeginStr;
    private String extractEndStr;
    private JTextArea resultArea;
    private File saveFile;
    private ProgressDialog progressDialog;
    private JButton startBT;
    private int choosedFuncCounts;
    private int completedCounts;
    
    /**
     * 单文档分析线程构造器
     * @param targetFile 待分析文件
     * @param chooseFrequency 词频分析
     * @param chooseFreqAscend 词频升序
     * @param chooseFreqDescend 词频降序
     * @param chooseAlphaAscend 字母升序
     * @param chooseAlphaDescend 字母降序
     * @param chooseChangeAnalysis 词形变换
     * @param chooseOutOfBound 超纲分析
     * @param chooseExtractLength 按长度减速
     * @param chooseExtractBegin 按词头检索
     * @param chooseExtractEnd 按词尾检索
     * @param chooseExport 文件输出
     * @param chooseSyntactic 句法分析
     * @param extractLength 检索长度
     * @param extractBeginStr 检索词头
     * @param extractEndStr 检索词尾
     * @param resultArea 输出显示框
     * @param saveFile 保存文件
     * @param choosedFuncCounts 选择功能计数
     * @param startBT 开始按钮
     * @param progressDialog 进度提示框
     */
    public SingleFileAnalyser2(File targetFile, boolean chooseFrequency,
            boolean chooseFreqAscend, boolean chooseFreqDescend,
            boolean chooseAlphaAscend, boolean chooseAlphaDescend,
            boolean chooseChangeAnalysis, boolean chooseOutOfBound,
            boolean chooseExtractLength, boolean chooseExtractBegin,
            boolean chooseExtractEnd, boolean chooseExport, boolean chooseSyntactic,
            int extractLength, String extractBeginStr,
            String extractEndStr, JTextArea resultArea,
            File saveFile,
            int choosedFuncCounts, JButton startBT, ProgressDialog progressDialog) {
        this.targetFile = targetFile;
        this.chooseFrequency = chooseFrequency;
        this.chooseFreqAscend = chooseFreqAscend;
        this.chooseFreqDescend = chooseFreqDescend;
        this.chooseAlphaAscend = chooseAlphaAscend;
        this.chooseAlphaDescend = chooseAlphaDescend;
        this.chooseChangeAnalysis = chooseChangeAnalysis;
        this.chooseOutOfBound = chooseOutOfBound;
        this.chooseExtractLength = chooseExtractLength;
        this.chooseExtractBegin = chooseExtractBegin;
        this.chooseExtractEnd = chooseExtractEnd;
        this.chooseExport = chooseExport;
        this.chooseSyntactic = chooseSyntactic;
        this.extractLength = extractLength;
        this.extractBeginStr = extractBeginStr;
        this.extractEndStr = extractEndStr;
        this.resultArea = resultArea;
        this.saveFile = saveFile;
        this.progressDialog = progressDialog;
        this.startBT = startBT;
    }

    @Override
    public void run() {
        String document = null;
        //初始化进度条

        try {
            //首先导入待分析文档，将其转换为String
            document = ImportFile.importFile(targetFile);
        } catch (IOException ex) {
            System.out.println("读取文件出错！");
            ex.printStackTrace();
        }

        if (chooseExport == true) {
            complexAnalysis(document);
        } else {
            //传入String文档
            simpleAnalysis(document);
        }
    }

    /**
     * 将词频List输出到显示框
     * @param resultList 词频List
     * @param functionName 功能名称，用来显示表头
     */
    private void showResultList(ArrayList<WordElement> resultList, String functionName) {
        resultArea.append("\n################################\n");
        resultArea.append("开始" + functionName + "\n");
        Iterator iter = resultList.iterator();
        while (iter.hasNext()) {
            WordElement element = (WordElement) iter.next();
            resultArea.append(String.format("%1$-20s\t%2$.8f\n", element.getWord(), element.getWordFrequency()));
            //resultArea.append("单词：" + element.getWord() + "\t\t词频：" + element.getWordFrequency() + "\n");
        }
        resultArea.append("分析完毕！\n");
    }

    /**
     * 根据当前完成的功能占所选功能的比例更新进度条
     */
    private void updateProcess() {
        double process = (double) completedCounts / (double) choosedFuncCounts;
        progressDialog.updateProgressValue(process);
    }

    /**
     * 结果不输出文件的简单分析，仅输出到结果框
     */
    private void simpleAnalysis(String document) {
        //功能匹配
        //首先进行词形变换，将所有单词转换成原形
        WordElement[] originalArray = LemmatizationAnalyse.getPositionAndLemma(document);
        originalArray = LemmatizationAnalyse.removeRepeated(originalArray);

        //基础词频序列为降序排列
        WordFrequencyAnalyse frequencyAnalyse = new WordFrequencyAnalyse(document, resultArea);
        WordElement[] originalList = frequencyAnalyse.getContentWords();
        ArrayList<WordElement> descendList = SortedResult.SortedByFreqDescend(originalList);

        progressDialog.setRunningStatus(false);

        if (chooseFrequency) {      //词频统计    
            frequencyAnalyse.analysis();
            completedCounts++;
            updateProcess();
        }
        if (chooseFreqAscend) {     //词频升序
            ArrayList<WordElement> ascendList = SortedResult.SortedByFreqAscend(originalList);
            showResultList(ascendList, "词频升序");
            completedCounts++;
            updateProcess();
        }

        if (chooseFreqDescend) {     //词频降序
            showResultList(descendList, "词频降序");
            completedCounts++;
            updateProcess();
        }

        if (chooseAlphaAscend) {    //按字母升序
            ArrayList<WordElement> alphaAscendList = SortedResult.SortedByAlphaAscend(originalList);
            showResultList(alphaAscendList, "按字母升序");
            completedCounts++;
            updateProcess();
        }

        if (chooseAlphaDescend) {    //按字母降序
            ArrayList<WordElement> alphaDescendList = SortedResult.SortedByAlphaDescend(originalList);
            showResultList(alphaDescendList, "按字母降序");
            completedCounts++;
            updateProcess();
        }

        if (chooseChangeAnalysis) { //词形变换分析
            LemmatizationAnalyse lemmatizationAnalyse = new LemmatizationAnalyse(document, resultArea);
            lemmatizationAnalyse.analysis();
            completedCounts++;
            updateProcess();
        }

        if (chooseOutOfBound) {     //超纲词分析
            System.out.println("超纲词分析");
            OutOfBoundAnalysis outOfBoundAnalysis = new OutOfBoundAnalysis(originalArray, resultArea);
            outOfBoundAnalysis.analysis();
            completedCounts++;
            updateProcess();
        }
        if (chooseExtractLength) {  //按长度检索单词
            System.out.println("按长度检索单词");
            ExtractByLength extractByLength = new ExtractByLength(descendList, extractLength, resultArea);
            extractByLength.analysis();
            completedCounts++;
            updateProcess();
        }
        if (chooseExtractBegin) {   //按词头检索单词
            System.out.println("按词头检索单词");
            ExtractByBeginning extractByBeginning = new ExtractByBeginning(descendList, resultArea, extractBeginStr);
            extractByBeginning.analysis();
            completedCounts++;
            updateProcess();
        }
        if (chooseExtractEnd) {     //按词尾检索单词
            System.out.println("按词尾检索单词");
            ExtractByEndding extractByEndding = new ExtractByEndding(descendList, resultArea, extractEndStr);
            extractByEndding.analysis();
            completedCounts++;
            updateProcess();
        }

        if (chooseSyntactic) {       //句法分析
            System.out.println("句法分析");
            SentenceAnalyse.sentenceAnalyse(document, resultArea);
            completedCounts++;
            updateProcess();
        }

        progressDialog.setProgressValue(100);
        startBT.setEnabled(true);
    }

    /**
     * 带结果输出文档的复杂分析，结果输出到指定路径文件
     */
    private void complexAnalysis(String document) {
        //输出为文件，需要创建一个文件输出器
        ExportToTxt txtExporter = null;
        ExportToExcel excelExporter = null;
        WordFrequencyAnalyse frequencyAnalyse = null;
        int exportType = 0;
        System.out.println("导出文件");
        //判断导出文件为txt还是xlsx       
        if (saveFile.getAbsolutePath().endsWith(".txt")) {
            //导出为txt
            txtExporter = new ExportToTxt(saveFile);
            txtExporter.init();
            exportType = 1;
            frequencyAnalyse = new WordFrequencyAnalyse(document, resultArea, txtExporter);
            completedCounts++;
            updateProcess();
        } else if (saveFile.getAbsolutePath().endsWith(".xlsx")) {
            //导出为xlsx
            excelExporter = new ExportToExcel(saveFile);
            excelExporter.init();
            exportType = 2;
            frequencyAnalyse = new WordFrequencyAnalyse(document, resultArea, excelExporter);
            completedCounts++;
            updateProcess();
        }

        //首先进行词形变换，将所有单词转换成原形，超纲词查询
        WordElement[] originalArray = LemmatizationAnalyse.getPositionAndLemma(document);
        originalArray = LemmatizationAnalyse.removeRepeated(originalArray);

        //基础词频序列为降序排列
        frequencyAnalyse = new WordFrequencyAnalyse(document, resultArea);
        WordElement[] originalList = frequencyAnalyse.getContentWords();
        ArrayList<WordElement> descendList = SortedResult.SortedByFreqDescend(originalList);

        //功能匹配
        if (chooseFrequency) {      //词频统计输出到文档
            System.out.println("词频统计输出文档");
            if (exportType == 1) {
                frequencyAnalyse.analysis(EXPORT_TO_TXT);
                completedCounts++;
                updateProcess();
            } else {
                frequencyAnalyse.analysis(EXPORT_TO_EXCEL);
                completedCounts++;
                updateProcess();
            }
        }
        if (chooseFreqAscend) {     //词频升序输出到文档
            System.out.println("词频升序输出文档");
            ArrayList<WordElement> ascendList = SortedResult.SortedByFreqAscend(originalList);
            showResultList(ascendList, "词频升序");
            if (exportType == 1) {
                txtExporter.writeArrayList(ascendList, "词频升序");
                completedCounts++;
                updateProcess();
            } else {
                excelExporter.writeArrayList(ascendList, "词频升序", "词频升序表");
                completedCounts++;
                updateProcess();
            }
        }
        if (chooseFreqDescend) {     //词频降序输出到文档
            System.out.println("词频降序输出文档");
            showResultList(descendList, "词频降序");
            if (exportType == 1) {
                txtExporter.writeArrayList(descendList, "词频降序");
                completedCounts++;
                updateProcess();
            } else {
                excelExporter.writeArrayList(descendList, "词频降序", "词频降序表");
                completedCounts++;
                updateProcess();
            }
        }
        if (chooseAlphaAscend) {    //按字母升序输出到文档
            System.out.println("按字母升序输出文档");
            ArrayList<WordElement> alphaAscendList = SortedResult.SortedByAlphaAscend(originalList);
            showResultList(descendList, "按字母升序");
            if (exportType == 1) {
                txtExporter.writeArrayList(alphaAscendList, "按字母升序");
                completedCounts++;
                updateProcess();
            } else {
                excelExporter.writeArrayList(alphaAscendList, "字母升序", "字母升序词频");
                completedCounts++;
                updateProcess();
            }
        }
        if (chooseAlphaDescend) {    //按字母降序输出到文档
            System.out.println("按字母降序输出文档");
            ArrayList<WordElement> alphaDescendList = SortedResult.SortedByAlphaDescend(originalList);
            showResultList(descendList, "按字母降序");
            if (exportType == 1) {
                txtExporter.writeArrayList(alphaDescendList, "按字母降序");
                completedCounts++;
                updateProcess();
            } else {
                excelExporter.writeArrayList(alphaDescendList, "字母降序", "字母降序词频");
                completedCounts++;
                updateProcess();
            }
        }
        if (chooseChangeAnalysis) { //词形变换分析输出到文档
            System.out.println("词形变换分析输出文档");
            if (exportType == 1) {
                LemmatizationAnalyse lemmatizationAnalyse = new LemmatizationAnalyse(document, resultArea, txtExporter);
                lemmatizationAnalyse.analysis(EXPORT_TO_TXT);
                completedCounts++;
                updateProcess();
            } else {
                LemmatizationAnalyse lemmatizationAnalyse = new LemmatizationAnalyse(document, resultArea, excelExporter);
                lemmatizationAnalyse.analysis(EXPORT_TO_EXCEL);
                completedCounts++;
                updateProcess();
            }
        }
        if (chooseOutOfBound) {     //超纲词分析输出到文档
            System.out.println("超纲词分析输出文档");
            if (exportType == 1) {
                OutOfBoundAnalysis outOfBoundAnalysis = new OutOfBoundAnalysis(originalArray, resultArea, txtExporter);
                outOfBoundAnalysis.analysis(OutOfBoundAnalysis.EXPORT_TO_TXT);
                completedCounts++;
                updateProcess();
            } else {
                OutOfBoundAnalysis outOfBoundAnalysis = new OutOfBoundAnalysis(originalArray, resultArea, excelExporter);
                outOfBoundAnalysis.analysis(OutOfBoundAnalysis.EXPORT_TO_EXCEL);
                completedCounts++;
                updateProcess();
            }
        }
        if (chooseExtractLength) {  //按长度检索单词输出到文档
            System.out.println("按长度检索单词输出文档");
            if (exportType == 1) { //输出txt
                ExtractByLength extractByLength = new ExtractByLength(descendList, extractLength, txtExporter, resultArea);
                extractByLength.analysis(ExtractByLength.EXPORT_TO_TXT);
                completedCounts++;
                updateProcess();
            } else {    //输出excel
                ExtractByLength extractByLength = new ExtractByLength(descendList, extractLength, excelExporter, resultArea);
                extractByLength.analysis(ExtractByLength.EXPORT_TO_EXCEL);
                completedCounts++;
                updateProcess();
            }
        }
        if (chooseExtractBegin) {   //按词头检索单词输出到文档
            System.out.println("按词头检索单词输出文档");
            if (exportType == 1) { //输出txt
                ExtractByBeginning extractByBeginning = new ExtractByBeginning(descendList, txtExporter, resultArea, extractBeginStr);
                extractByBeginning.analysis(ExtractByBeginning.EXPORT_TO_TXT);
                completedCounts++;
                updateProcess();
            } else {    //输出excel
                ExtractByBeginning extractByBeginning = new ExtractByBeginning(descendList, excelExporter, resultArea, extractBeginStr);
                extractByBeginning.analysis(ExtractByBeginning.EXPORT_TO_EXCEL);
                completedCounts++;
                updateProcess();
            }
        }
        if (chooseExtractEnd) {     //按词尾检索单词输出到文档
            System.out.println("按词尾检索单词输出文档");
            if (exportType == 1) { //输出txt
                ExtractByEndding extractByEndding = new ExtractByEndding(descendList, txtExporter, resultArea, extractEndStr);
                extractByEndding.analysis(ExtractByEndding.EXPORT_TO_TXT);
                completedCounts++;
                updateProcess();
            } else {    //输出excel
                ExtractByEndding extractByEndding = new ExtractByEndding(descendList, excelExporter, resultArea, extractEndStr);
                extractByEndding.analysis(ExtractByEndding.EXPORT_TO_EXCEL);
                completedCounts++;
                updateProcess();
            }
        }

        if (chooseSyntactic) {       //句法分析
            SentenceAnalyse.resultOutput(document, resultArea, txtExporter);
            completedCounts++;
            updateProcess();
        }

        //关闭输出器
        if (txtExporter != null) {
            txtExporter.close();
        }
        if (excelExporter != null) {
            excelExporter.close();
        }

        progressDialog.setProgressValue(100);
        startBT.setEnabled(true);
    }

}
