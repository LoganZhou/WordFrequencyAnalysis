/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Program;

import ExportToFile.ExportToExcel;
import ExportToFile.ExportToTxt;
import Frequency.DoubleDocAnalyse;
import ImportFile.ImportFile;
import Dialog.ProgressDialog;
import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JButton;
import javax.swing.JTextArea;

/**
 *
 * @author zhouheng
 */
public class DoubleFileAnalyser2 implements Runnable {

    private File targetFileA;
    private File targetFileB;
    private boolean chooseCount;
    private boolean chooseSameCount;
    private boolean chooseExport;
    private JTextArea resultArea;
    private File saveFile;
    private ProgressDialog progressDialog;
    private int choosedFuncCounts;
    private int completedCounts;
    private JButton startBT;

    public DoubleFileAnalyser2(File targetFileA, File targetFileB,
            boolean chooseCount, boolean chooseSameCount,
            boolean chooseExport, JTextArea resultArea,
            File saveFile, ProgressDialog progressDialog,
            JButton startBT) {
        this.targetFileA = targetFileA;
        this.targetFileB = targetFileB;
        this.chooseCount = chooseCount;
        this.chooseSameCount = chooseSameCount;
        this.chooseExport = chooseExport;
        this.resultArea = resultArea;
        this.saveFile = saveFile;
        this.progressDialog = progressDialog;
        this.startBT = startBT;
        choosedFuncCounts = 0;
        completedCounts = 0;
        if (chooseCount) {
            choosedFuncCounts++;
        }
        if (chooseSameCount) {
            choosedFuncCounts++;
        }
    }

    @Override
    public void run() {
        String documentA = null;
        String ducumentB = null;

        try {
            //首先导入待分析文档A，将其转换为String
            documentA = ImportFile.importFile(targetFileA);
        } catch (IOException ex) {
            System.out.println("读取文件出错！");
            ex.printStackTrace();
        }

        try {
            //导入待分析文档B，将其转换为String
            ducumentB = ImportFile.importFile(targetFileB);
        } catch (IOException ex) {
            System.out.println("读取文件出错！");
            ex.printStackTrace();
        }

        if (chooseExport == true) {
            complexAnalysis(documentA, ducumentB);
        } else {
            //传入String文档
            simpleAnalysis(documentA, ducumentB);
        }
    }

    private void simpleAnalysis(String ducumentA, String ducumentB) {
        //功能匹配
        DoubleDocAnalyse doubleDocAnalyse = new DoubleDocAnalyse(ducumentA, ducumentB, resultArea);
        if (chooseCount) {      //词数统计
            System.out.println("开始双文档词数统计");
            doubleDocAnalyse.wordsCount();
            completedCounts++;
            updateProcess();
        }
        if (chooseSameCount) {  //相同词统计
            //先初始化
            System.out.println("开始双文档相同词分析");
            doubleDocAnalyse.wordElementToHashMap();
            doubleDocAnalyse.findSameWords();
            completedCounts++;
            updateProcess();
        }
        progressDialog.setProgressValue(100);
        startBT.setEnabled(true);
        System.out.println("Finished!");
    }
    
    
    private void complexAnalysis(String ducumentA, String ducumentB) {
        //输出为文件，需要创建一个文件输出器
        ExportToTxt txtExporter = null;
        ExportToExcel excelExporter = null;

        int exportType = 0;
        System.out.println("导出文件");
        //判断导出文件为txt还是xlsx       
        if (saveFile.getAbsolutePath().endsWith(".txt")) {
            //导出为txt
            txtExporter = new ExportToTxt(saveFile);
            txtExporter.init();
            exportType = 1;
        } else if (saveFile.getAbsolutePath().endsWith(".xlsx")) {
            //导出为xlsx
            excelExporter = new ExportToExcel(saveFile);
            excelExporter.init();
            exportType = 2;
        }

        //功能匹配
        DoubleDocAnalyse doubleDocAnalyse = new DoubleDocAnalyse(ducumentA, ducumentB, resultArea);
        if (chooseCount) {      //词数统计
            if (exportType == 1) {
                doubleDocAnalyse.setTxtExporter(txtExporter);
                doubleDocAnalyse.wordsCount(DoubleDocAnalyse.EXPORT_TO_TXT);
                completedCounts++;
                updateProcess();
            } else {
                doubleDocAnalyse.setExcelExporter(excelExporter);
                doubleDocAnalyse.wordsCount(DoubleDocAnalyse.EXPORT_TO_EXCEL);
                completedCounts++;
                updateProcess();
            }
        }
        if (chooseSameCount) {  //相同词统计
            doubleDocAnalyse.wordElementToHashMap();
            if (exportType == 1) {
                doubleDocAnalyse.setTxtExporter(txtExporter);
                doubleDocAnalyse.findSameWords(DoubleDocAnalyse.EXPORT_TO_TXT);
                completedCounts++;
                updateProcess();
            } else {
                doubleDocAnalyse.setExcelExporter(excelExporter);
                doubleDocAnalyse.findSameWords(DoubleDocAnalyse.EXPORT_TO_EXCEL);
                completedCounts++;
                updateProcess();
            }
        }
        if (txtExporter != null) {
            txtExporter.close();
        }
        if (excelExporter != null) {
            excelExporter.close();
        }
        progressDialog.setProgressValue(100);
        startBT.setEnabled(true);
    }

    /**
     * 根据当前完成的功能占所选功能的比例更新进度条
     */
    private void updateProcess() {
        double process = (double) completedCounts / (double) choosedFuncCounts;
        progressDialog.updateProgressValue(process);
    }

}
