/**
 * Created by zhouheng on 2017/7/4.
 */
package OutOfBound;

import ExportToFile.ExportToExcel;
import ExportToFile.ExportToTxt;
import Frequency.WordElement;
import java.sql.*;
import java.util.ArrayList;
import java.util.Iterator;
import javax.swing.JTextArea;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.util.CellRangeAddress;

/**
 * 超纲词分析模块
 * 带文件输出
 */
public class OutOfBoundAnalysis {
    private Connection connection;
    //private HashMap<String, Double> wordsMap;  //词频HashMap
    private ArrayList<WordElement> wordList;
    private int outOfCET4 = 0;          //CET4超纲数
    private int outOFCET6 = 0;          //CET6超纲数
    private ExportToTxt txtExporter;        //txt文件输出，必须要先被初始化
    private ExportToExcel excelExporter;    //excel文件输出，必须要先被初始化
    public final static int CET4 = 1;   //CET4检索
    public final static int CET6 = 2;   //CET6检索
    public final static int EXPORT_TO_TXT = 1;      //输出txt
    public final static int EXPORT_TO_EXCEL = 2;    //输出excel
    private JTextArea resultArea;
    private WordElement[] wordElements;
    
    /**
     * 不带结果输出文件的简单分析
     * @param wordList 待分析的文档list
     * @param resultArea 输出框
     * @deprecated 请使用WordElement[]数组
     */
//    public OutOfBoundAnalysis(ArrayList<WordElement> wordList, JTextArea resultArea) {
//        this.wordList = wordList;
//        this.resultArea = resultArea;
//        createConnection();
//    }
    
    /**
     * 带txt结果输出的复杂分析，结果输出到文件和输出框
     * @param wordList 待分析的文档list
     * @param resultArea 输出框
     * @param txtExporter 已初始化好的文件输出器
     * @deprecated 请使用WordElement[]数组
     */
//    public OutOfBoundAnalysis(ArrayList<WordElement> wordList, JTextArea resultArea, ExportToTxt txtExporter) {
//        this(wordList, resultArea);
//        this.txtExporter = txtExporter;
//    }
    
    /**
     * 带excel结果输出的复杂分析，结果输出到文件和输出框
     * @param wordList 待分析的文档list
     * @param excelExporter 输出框
     * @param resultArea 已初始化好的文件输出器
     * @deprecated 请使用WordElement[]数组
     */
//    public OutOfBoundAnalysis(ArrayList<WordElement> wordList, JTextArea resultArea, ExportToExcel excelExporter) {
//        this.wordList = wordList;
//        this.excelExporter = excelExporter;
//        this.resultArea = resultArea;
//        createConnection();
//    }
    
    /**
     * 不带结果输出文件的简单分析，仅输出到结果框
     * @param wordElements 待分析的WordElement[]数组
     * @param resultArea 输出框
     */
    public OutOfBoundAnalysis(WordElement[] wordElements, JTextArea resultArea) {
        this.wordElements = wordElements;
        this.resultArea = resultArea;
        createConnection();
    }

    /**
     * 带txt结果输出的复杂分析，结果输出到文件和输出框
     * @param wordElements 待分析的WordElement[]数组
     * @param resultArea 输出框
     * @param txtExporter 已初始化好的文件输出器
     */
    public OutOfBoundAnalysis(WordElement[] wordElements, JTextArea resultArea, ExportToTxt txtExporter) {
        this.txtExporter = txtExporter;
        this.resultArea = resultArea;
        this.wordElements = wordElements;
        createConnection();
    }
    
    /**
     * 带excel结果输出的复杂分析，结果输出到文件和输出框
     * @param wordElements 待分析的WordElement[]数组
     * @param resultArea 输出框
     * @param excelExporter 已初始化好的文件输出器
     */
    public OutOfBoundAnalysis(WordElement[] wordElements, JTextArea resultArea, ExportToExcel excelExporter) {
        this.excelExporter = excelExporter;
        this.resultArea = resultArea;
        this.wordElements = wordElements;
        createConnection();
    }
    
    
    
    /**
     * 单词数据库查询结果
     */
    class SelectResult {
        private String word;
        private String explanation;
        private int resultType;
        public static final int NOT_OUT_OF_BOUND = 0;
        public static final int OUT_OF_CET4 = 1;
        public static final int OUT_OF_CET6 = 2;
        public SelectResult(String word, String explanation, int resultType) {
            this.word = word;
            this.explanation = explanation;
            this.resultType = resultType;
        }

        public String getWord() {
            return word;
        }

        public String getExplanation() {
            return explanation;
        }

        public int getResultType() {
            return resultType;
        }
        
    }
    
//    /**
//     * 测试构造,文件输出
//     */
//    private OutOfBoundAnalysis(JTextArea resultArea, ExportToExcel excelExporter) {
//        this.wordList = new ArrayList<WordElement> ();
//        this.resultArea = resultArea;
//        this.excelExporter = excelExporter;
//        createConnection();
//        wordList.add(new WordElement("a", 1.0, 1));
//        wordList.add(new WordElement("b", 1.0, 1));
//        wordList.add(new WordElement("c", 1.0, 1));
//        analysis(EXPORT_TO_EXCEL);
//    }

    /**
     * 创建数据库连接
     */
    private void createConnection() {
        connection = DBConnection.getConnection();
    }
    
    public void analysis() {
        resultArea.append("\n################################\n");
        resultArea.append("开始超纲词分析\n");
        resultArea.append(String.format("%1$-10s\t%2$-20s\t%3$s\n", "超纲类型","单词","释义"));
        for (WordElement element : wordElements) {
            SelectResult result = search(element.getOriginal());
            switch (result.getResultType()) {
                case 0://未超纲
                    break;
                case 1://CET4超纲
                    resultArea.append(String.format("%1$-10s\t%2$-20s\t%3$s\n","CET4超纲",element.getOriginal(),result.getExplanation()));
                    break;
                case 2://CET6超纲
                    resultArea.append(String.format("%1$-10s\t%2$-20s\t%3$s\n","CET6超纲",element.getOriginal(),result.getExplanation()));
                    break;
            }
        }
        resultArea.append("----------------------------\n");
        resultArea.append("CET4 超纲词数：" + outOfCET4 + "\n");
        resultArea.append("CET6 超纲词数：" + outOFCET6 + "\n");
        resultArea.append("超纲分析完毕\n");
        resultArea.paintImmediately(resultArea.getBounds());
    }
    
    public void analysis(int exportType) {
        analysis();
        
        if (exportType == EXPORT_TO_TXT) {
            txtExporter.writeLine("\n################################\n");
            txtExporter.writeLine("开始超纲词分析\n");
            txtExporter.writeLine(String.format("%1$-10s\t%2$-20s\t%3$s\n", "超纲类型","单词","释义"));
            //System.out.println("开始超纲词分析");
            
            for (WordElement element : wordElements) {
                SelectResult result = search(element.getOriginal());
                switch (result.getResultType()) {
                    case 0://未超纲
                        break;
                    case 1://CET4超纲
                        txtExporter.writeLine(String.format("%1$-10s\t%2$-20s\t%3$s\n","CET4超纲",element.getOriginal(),result.getExplanation()));
                        break;
                    case 2://CET6超纲
                        txtExporter.writeLine(String.format("%1$-10s\t%2$-20s\t%3$s\n","CET6超纲",element.getOriginal(),result.getExplanation()));
                        break;
                }
            }
            txtExporter.writeLine("----------------------------\n");
            txtExporter.writeLine("CET4 超纲词数：" + outOfCET4 + "\n");
            txtExporter.writeLine("CET6 超纲词数：" + outOFCET6 + "\n");
            txtExporter.writeLine("超纲分析完毕\n");
        } else if (exportType == EXPORT_TO_EXCEL) {
            //创建一个新表
            Sheet newSheet = excelExporter.createNewSheet("超纲词分析");
            newSheet.addMergedRegion(new CellRangeAddress(0, 0, 0, 2));//合并单元格，创建表名
            Row firstRow = newSheet.createRow(0);
            firstRow.createCell((short) 0).setCellValue("超纲词分析");
            Row secondRow = newSheet.createRow(1);
            secondRow.createCell(0).setCellValue("单词");
            secondRow.createCell(1).setCellValue("释义");
            secondRow.createCell(2).setCellValue("超纲类型");

            int rowNum = 2;//行数从第三行开始
            
            for (WordElement element : wordElements) {
                SelectResult result = search(element.getOriginal());
                Row newRow = newSheet.createRow(rowNum);
                switch (result.getResultType()) {
                    case 0://未超纲
                        break;
                    case 1://CET4超纲
                        newRow.createCell(0).setCellValue(result.getWord());
                        newRow.createCell(1).setCellValue(result.getExplanation());
                        newRow.createCell(2).setCellValue("CET4");
                        rowNum++;
                        break;
                    case 2://CET6超纲
                        newRow.createCell(0).setCellValue(result.getWord());
                        newRow.createCell(1).setCellValue(result.getExplanation());
                        newRow.createCell(2).setCellValue("CET6");
                        rowNum++;
                        break;
                }
            }
            
            newSheet.addMergedRegion(new CellRangeAddress(rowNum, rowNum, 0, 2));//合并单元格，创建表名
            Row lastRow = newSheet.createRow(rowNum);
            lastRow.createCell(0).setCellValue("超纲总计：CET4=" + outOfCET4 + ";CET6=" + outOFCET6);
        }
    }

//    /**
//     * 超纲词分析，并输出到文件
//     * @param exportType 
//     */
//    public void analysis(int exportType) {
//        //先输出到结果显示框
//        analysis();
//        
//        if (exportType == EXPORT_TO_TXT) {
//            txtExporter.writeLine("\n################################\n");
//            txtExporter.writeLine("开始超纲词分析\n");
//            //System.out.println("开始超纲词分析");
//            Iterator iter = wordList.iterator();
//            while (iter.hasNext()) {
////            Map.Entry entry = (Map.Entry)iter.next();
////            String word = (String)entry.getKey();
////            Double frequency = (Double)entry.getValue();
//                WordElement element = (WordElement) iter.next();
//                SelectResult result = search(element.getOriginal());
//                switch(result.getResultType()) {
//                    case 0://未超纲
//                        break;
//                    case 1://CET4超纲
//                        txtExporter.writeLine("CET4超纲：" + element.getOriginal() + "\t\t释义：" + result.getExplanation() + "\n");
//                        break;
//                    case 2://CET6超纲
//                        txtExporter.writeLine("CET6超纲：" + element.getOriginal() + "\t\t释义：" + result.getExplanation() + "\n");
//                        break;
//                }
//            }
//            txtExporter.writeLine("----------------------------\n");
//            txtExporter.writeLine("CET4 超纲词数：" + outOfCET4 + "\n");
//            txtExporter.writeLine("CET6 超纲词数：" + outOFCET6 + "\n");
//            txtExporter.writeLine("超纲分析完毕\n");
//        } else if (exportType == EXPORT_TO_EXCEL) {
//            //创建一个新表
//            Sheet newSheet = excelExporter.createNewSheet("超纲词分析");
//            newSheet.addMergedRegion(new CellRangeAddress(0,0,0,2));//合并单元格，创建表名
//            Row firstRow = newSheet.createRow(0);
//            firstRow.createCell((short)0).setCellValue("超纲词分析");
//            Row secondRow = newSheet.createRow(1);
//            secondRow.createCell(0).setCellValue("单词");
//            secondRow.createCell(1).setCellValue("释义");
//            secondRow.createCell(2).setCellValue("超纲类型");
////            secondRow.createCell(1).setCellValue("词频");
////            secondRow.createCell(2).setCellValue("词数");
//            int rowNum = 2;//行数从第三行开始
//            
//            Iterator iter = wordList.iterator();
//            while (iter.hasNext()) {
//                //遍历文档词频HashMap
//                WordElement element = (WordElement)iter.next();
//                SelectResult result = search(element.getWord());
//                Row newRow = newSheet.createRow(rowNum);
//                switch(result.getResultType()) {
//                    case 0://未超纲
//                        break;
//                    case 1://CET4超纲
//                        newRow.createCell(0).setCellValue(result.getWord());
//                        newRow.createCell(1).setCellValue(result.getExplanation());
//                        newRow.createCell(2).setCellValue("CET4");
//                        rowNum++;
//                        break;
//                    case 2://CET6超纲
//                        newRow.createCell(0).setCellValue(result.getWord());
//                        newRow.createCell(1).setCellValue(result.getExplanation());
//                        newRow.createCell(2).setCellValue("CET6");
//                        rowNum++;
//                        break;
//                }
//            }
//            newSheet.addMergedRegion(new CellRangeAddress(rowNum,rowNum,0,2));//合并单元格，创建表名
//            Row lastRow = newSheet.createRow(rowNum);
//            lastRow.createCell(0).setCellValue("超纲总计：CET4=" + outOfCET4 + ";CET6=" + outOFCET6);
//        }
//    }
//    
//    public void analysis() {
//        resultArea.append("\n################################\n");
//        resultArea.append("开始超纲词分析\n");
//        //System.out.println("开始超纲词分析");
//        Iterator iter = wordList.iterator();
//        while (iter.hasNext()) {
//            WordElement element = (WordElement)iter.next();
//            SelectResult result = search(element.getOriginal());
//            switch(result.getResultType()) {
//                    case 0://未超纲
//                        break;
//                    case 1://CET4超纲
//                        resultArea.append("CET4超纲：" + element.getWord() + "\t\t释义：" + result.getExplanation() + "\n");
//                        break;
//                    case 2://CET6超纲
//                        resultArea.append("CET6超纲：" + element.getWord() + "\t\t释义：" + result.getExplanation() + "\n");
//                        break;
//                }
//        }
//        resultArea.append("----------------------------\n");
//        resultArea.append("CET4 超纲词数：" + outOfCET4 + "\n");
//        resultArea.append("CET6 超纲词数：" + outOFCET6 + "\n");
//        resultArea.append("超纲分析完毕\n");
//        resultArea.paintImmediately(resultArea.getBounds());
//    }

    /**
     * 超纲检查
     * @param vocabulary 待检查词汇
     * @return 单词查询结果
     *                  
     */
    private SelectResult search(String vocabulary) {
        PreparedStatement pstmt = null;
        ResultSet result = null;
        String explanation = null;
        String statementStr = "select Word, Explanation from CET4 where Word = ?;";
        int flag = -1;
        try {
            pstmt = (PreparedStatement) connection.prepareStatement(statementStr);
            pstmt.setString(1,vocabulary);
            result = pstmt.executeQuery();
            if (result.next()) {
                /*如果结果非空，则未超纲*/
                flag = 0;//未超纲
            } else {
                /*CET4超纲*/
                //继续检索CET6
                statementStr = "select Word, Explanation from CET6 where Word = ?;";
                pstmt = (PreparedStatement) connection.prepareStatement(statementStr);
                pstmt.setString(1,vocabulary);
                result = pstmt.executeQuery();
                if (result.next()) {
                    /*如果结果非空，CET6检索成功*/
                    flag = 1;
                    explanation = result.getString(2);
                } else {
                    /*CET6超纲*/
                    flag = 2;
                    //查询通用词库
                    statementStr = "select Explanation from UniversalDict where Word = ?;";
                    pstmt = (PreparedStatement) connection.prepareStatement(statementStr);
                    pstmt.setString(1,vocabulary);
                    result = pstmt.executeQuery();
                    if (result.next()) {
                        explanation = result.getString(1);
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        if(flag == 1) {
            //System.out.println("CET4超纲：" + vocabulary + "\t释义：" + explanation);
            //resultArea.append("CET4超纲：" + vocabulary + "\t释义：" + explanation + "\n");
            outOfCET4++;
            return new SelectResult(vocabulary,explanation,SelectResult.OUT_OF_CET4);
        } else if(flag == 2){
            //System.out.println("CET6 超纲词：" + vocabulary + "\t释义：" + explanation);
            //resultArea.append("CET6超纲：" + vocabulary + "\t释义：" + explanation + "\n");
            outOFCET6++;
            return new SelectResult(vocabulary,explanation,SelectResult.OUT_OF_CET6);
        } else {
            return new SelectResult(vocabulary,explanation,SelectResult.NOT_OUT_OF_BOUND);
        }
    }

    /**
     * 获取文档CET4超纲词数
     * @return CET4超纲词数
     */
    public int getOutOfCET4() {
        return outOfCET4;
    }

    /**
     * 获取文档CET6超纲词数
     * @return CET4超纲词数
     */
    public int getOutOFCET6() {
        return outOFCET6;
    }
}
