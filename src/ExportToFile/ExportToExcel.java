/**
 * Created by zhouheng on 2017/7/5.
 */
package ExportToFile;

import Frequency.WordElement;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.*;
import org.apache.poi.ss.util.CellRangeAddress;

/**
 * 分析结果输出Excel
 */
public class ExportToExcel{
    private File saveFile = null;               //保存文件
    private Workbook workbook = null;           //WorkBook
    private FileOutputStream fileOut = null;    //文件输出流

    public ExportToExcel(File saveFile) {
        this.saveFile = saveFile;
        workbook = new XSSFWorkbook();
    }

    public void init() {
        try {
            fileOut = new FileOutputStream(saveFile);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void close() {
        try {
            workbook.write(fileOut);
            fileOut.close();
            System.out.println("Finished!");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    /**
     * 词形变换分析结果写入excel，在Excel中新建一个Sheet，并写入一个WordElement数组
     * @param elementArray 待输入的词频数组
     * @param sheetTitle Sheet表名
     * @param tableTitle 表名
     */
    public void writeArray(WordElement[] elementArray, String sheetTitle, String tableTitle) {
        Sheet newSheet = workbook.createSheet(sheetTitle);
        newSheet.addMergedRegion(new CellRangeAddress(0,0,0,1));//合并单元格，创建表名
        Row firstRow = newSheet.createRow(0);
        firstRow.createCell((short)0).setCellValue(tableTitle);
        Row secondRow = newSheet.createRow(1);
        secondRow.createCell(0).setCellValue("单词");
        secondRow.createCell(1).setCellValue("词频");
        int rowNum = 2;//行数从第三行开始
        
        for (WordElement element : elementArray) {
            Row newRow = newSheet.createRow(rowNum);
            newRow.createCell(0).setCellValue(element.getWord());
            newRow.createCell(1).setCellValue(element.getWordFrequency());
            rowNum++;
        }
    }
    
    
    /**
     * 在Excel中新建一个Sheet，并写入一个ArrayList
     * @param wordList 待输出List
     * @param sheetTitle Sheet名
     * @param tableTitle 表名
     */
    public void writeArrayList(ArrayList<WordElement> wordList, String sheetTitle, String tableTitle) {
        Sheet newSheet = workbook.createSheet(sheetTitle);
        newSheet.addMergedRegion(new CellRangeAddress(0,0,0,1));//合并单元格，创建表名
        Row firstRow = newSheet.createRow(0);
        firstRow.createCell((short)0).setCellValue(tableTitle);
        Row secondRow = newSheet.createRow(1);
        secondRow.createCell(0).setCellValue("单词");
        secondRow.createCell(1).setCellValue("词频");
        int rowNum = 2;//行数从第三行开始
        
        Iterator iter = wordList.iterator();
        while (iter.hasNext()) {
            WordElement element = (WordElement)iter.next();
            Row newRow = newSheet.createRow(rowNum);
            newRow.createCell(0).setCellValue(element.getWord());
            newRow.createCell(1).setCellValue(element.getWordFrequency());
            rowNum++;
        }
    }
    
    /**
     * 创建一个空的Sheet，并将其返回，供需要逐行写入的功能调用
     * @param sheetTitle Sheet表名
     * @return 空的Sheet
     */
    public Sheet createNewSheet(String sheetTitle) {
        return workbook.createSheet(sheetTitle);
    }
    
//    public void frequencyOfWords(ArrayList<Map.Entry<String, Double>> list) {
//        Sheet sheet1 = workbook.createSheet("词频统计表");
//        createNewSheet(list,sheet1);
//    }
//
//    public void frequencyAscend(ArrayList<Map.Entry<String, Double>> list) {
//        Sheet sheet1 = workbook.createSheet("词频升序统计表");
//        createNewSheet(list,sheet1);
//    }
//
//    public void frequencyDescend(ArrayList<Map.Entry<String, Double>> list) {
//        Sheet sheet1 = workbook.createSheet("字母降序统计表");
//        createNewSheet(list,sheet1);
//    }
//
//    public void alphabeticalAscend(ArrayList<Map.Entry<String, Double>> list) {
//        Sheet sheet1 = workbook.createSheet("字母升序统计表");
//        createNewSheet(list,sheet1);
//    }
//
//    public void alphabeticalDescend(ArrayList<Map.Entry<String, Double>> list) {
//        Sheet sheet1 = workbook.createSheet("字母降序统计表");
//        createNewSheet(list,sheet1);
//    }
}
