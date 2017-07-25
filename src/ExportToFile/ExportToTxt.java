/**
 * Created by zhouheng on 2017/7/4.
 */
package ExportToFile;

import Frequency.WordElement;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

/**
 * 分析结果导出TXT模块
 */
public class ExportToTxt{
    private File saveFile = null;               //保存文件
    private FileWriter fileWriter = null;
    private BufferedWriter bufferedWriter = null;

//    public static void main(String[] args) {
//        //测试
//        ExportToTxt exporter = new ExportToTxt();
//    }

//    private ExportToTxt() {
//        //测试
//        this.saveFile = new File("/Users/zhouheng/Code/test.txt");
//        ArrayList<Map.Entry<String, Double>> list = new ArrayList<Map.Entry<String, Double>>();
//        list.add(new AbstractMap.SimpleEntry<String, Double>("a",1.0));
//        list.add(new AbstractMap.SimpleEntry<String, Double>("be",2.0));
//        list.add(new AbstractMap.SimpleEntry<String, Double>("has",3.0));
//        list.add(new AbstractMap.SimpleEntry<String, Double>("bee",4.0));
//        list.add(new AbstractMap.SimpleEntry<String, Double>("hello",5.0));
//        init();
//        writeArrayList(list,"默认词频");
//        close();
//    }
    
    /**
     * 构造txt文件输出器
     * @param saveFile 写入文件
     */
    public ExportToTxt(File saveFile) {
        this.saveFile = saveFile;
    }

    /**
     * 初始化文件写入
     */
    public void init() {
        //新建文件
        if (!saveFile.exists()) {
            try {
                saveFile.createNewFile();
            } catch (IOException e) {
                System.out.println("新建文件出错！");
                e.printStackTrace();
            }
        }
        try {
            fileWriter = new FileWriter(saveFile);
            bufferedWriter = new BufferedWriter(fileWriter);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 结束写入
     */
    public void close() {
        try {
            bufferedWriter.close();
            System.out.println("Finished!");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    
    /**
     * 词频统计结果写入，适用功能包括：
     * 1. 默认词频
     * 2. 升序词频
     * 3. 降序词频
     * 4. 按字母升序词频
     * 5. 按字母降序词频
     * 
     * @param wordList 已排序的词频表
     * @param title 输出表名
     */
    public void writeArrayList(ArrayList<WordElement> wordList, String title) {
        try {
            bufferedWriter.write("\n\n#################### " + title + " ####################\n");
            Iterator iter = wordList.iterator();
            while (iter.hasNext()) {
                WordElement element = (WordElement)iter.next();
                bufferedWriter.write("单词：" + element.getWord()+ "\t\t词频：" + element.getWordFrequency() + "\n");
            }
            bufferedWriter.write("######################  分析完毕！ ######################\n");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    /**
     * 词形变换分析结果写入txt
     * 
     * @param elementArray 词频数组（WordElement[]）
     * @param title 表名
     */
    public void writeArray(WordElement[] elementArray, String title) {
        try {
            bufferedWriter.write("\n\n#################### " + title + " ####################\n");
            for (WordElement element : elementArray) {
                bufferedWriter.write("单词：" + element.getOriginal() + "\t\t词频：" + element.getWordFrequency() + "\n");
            }
            bufferedWriter.write("#####################  分析完毕！ ####################\n");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    /**
     * 按行写入，每次写入一行，适用于：
     * 1. 超纲词分析
     * 2. 单词检索（按长度、词头、词尾）
     * 3. 词形变换分析
     * 
     * @param oneLine 一行内容
     */
    public void writeLine(String oneLine) {
        try {
            bufferedWriter.write(oneLine);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
//    /**
//     * 迭代输出Map的内容
//     * @param list 带输出Map
//     */
//    private void iterateMap(ArrayList<Map.Entry<String, Double>> list) {
//        for(Map.Entry<String, Double>mapping:list){
//            java.text.DecimalFormat df=new java.text.DecimalFormat("#0.000");
//            String word = (String) mapping.getKey();
//            Double frequency = (Double) mapping.getValue();
//            String newLine = "单词：" + word + "\t" + "词频：" + df.format(frequency) + "\n";
//            try {
//                bufferedWriter.write(newLine);
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        }
//    }

//    public void frequencyOfWords(ArrayList<Map.Entry<String, Double>> list) {
//        try {
//            bufferedWriter.write("#################### 词频统计结果 ####################\n");
//            iterateMap(list);
//            bufferedWriter.write("####################################################\n");
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }
//
//    public void frequencyAscend(ArrayList<Map.Entry<String, Double>> list) {
//        try {
//            bufferedWriter.write("#################### 按词频升序统计结果 ####################\n");
//            iterateMap(list);
//            bufferedWriter.write("#########################################################\n");
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }
//
//    public void frequencyDescend(ArrayList<Map.Entry<String, Double>> list) {
//        try {
//            bufferedWriter.write("#################### 按词频降序统计结果 ####################\n");
//            iterateMap(list);
//            bufferedWriter.write("#########################################################\n");
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }
//
//    public void alphabeticalAscend(ArrayList<Map.Entry<String, Double>> list) {
//        try {
//            bufferedWriter.write("#################### 按字母升序统计结果 ####################\n");
//            iterateMap(list);
//            bufferedWriter.write("#########################################################\n");
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }
//
//    public void alphabeticalDescend(ArrayList<Map.Entry<String, Double>> list) {
//        try {
//            bufferedWriter.write("#################### 按字母升序统计结果 ####################");
//            iterateMap(list);
//            bufferedWriter.write("#########################################################");
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }
}
