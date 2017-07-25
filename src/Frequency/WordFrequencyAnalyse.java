package Frequency;

import ExportToFile.ExportToExcel;
import ExportToFile.ExportToTxt;
import WordChanges.LemmatizationAnalyse;
import java.util.ArrayList;
import java.util.HashMap;  
import java.util.Iterator;  
import java.util.Map;   
import java.util.Set;
import javax.swing.JTextArea;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.util.CellRangeAddress;

/*
 * 词频分析模块
 * 本模块仅包含一个方法――单词统计及频率计算
 */
public class WordFrequencyAnalyse {
    private WordElement[] contentWords;     //分词后的原型数组
    private ExportToTxt txtExporter;        //txt文件输出，必须要先被初始化
    private ExportToExcel excelExporter;    //excel文件输出，必须要先被初始化
    public final static int EXPORT_TO_TXT = 1;      //输出txt
    public final static int EXPORT_TO_EXCEL = 2;    //输出excel
    private JTextArea resultArea;
    private String document;                    //原始文件String

    /**
     * 词频分析多线程测试构造函数
     * @param document 文档String
     */
    public WordFrequencyAnalyse(String document) {
        this.document = document;
    }


    /**
     * 默认词频排序，仅输出到显示框
     * @param document 文档String
     * @param resultArea 结果输出框
     */
    public WordFrequencyAnalyse(String document, JTextArea resultArea) {
        this.resultArea = resultArea;
        this.document = document;
    }
    
    /**
     * 默认词频排序，并输出到txt文件
     * @param document 文档String
     * @param resultArea 结果输出框
     * @param txtExporter txt文件输出器
     */
    public WordFrequencyAnalyse(String document, JTextArea resultArea, ExportToTxt txtExporter) {
        this.txtExporter = txtExporter;
        this.resultArea = resultArea;
        this.document = document;
    }
    
    /**
     * 默认词频排序，并输出到excel文件
     * @param document 文档String
     * @param resultArea 结果输出框
     * @param excelExporter excel文件输出器
     */
    public WordFrequencyAnalyse(String document, JTextArea resultArea, ExportToExcel excelExporter) {
        this.excelExporter = excelExporter;
        this.resultArea = resultArea;
        this.document = document;
    }
    
    public WordElement[] analysisForMultiThreads() {
        countTextWords();
        return contentWords;
    }
    
    
    /**
     * 结果仅输出到结果框
     */
    public void analysis() {
        countTextWords();
        resultArea.append("################################\n");
        resultArea.append("词频统计（默认排序）\n");
        resultArea.append(String.format("%-40s\t\t%s\n", "单词","词频"));
        for (WordElement contentWord : contentWords) {
            resultArea.append(String.format("%-40s\t\t%.8f\n",contentWord.getWord(),contentWord.getWordFrequency()));
            //resultArea.append("单词：" + contentWord.getWord()+ "\t\t词频：" + contentWord.getWordFrequency() + "\n");
        }
        resultArea.append("词频统计（默认排序）分析完毕！\n");
    }
    
    /**
     * 结果输出到显示框和文件
     * @param exportType 文件输出类型
     */
    public void analysis(int exportType) {
        countTextWords();
        analysis(); //先输出到显示框
        
        //int count = 0;
        if (exportType == EXPORT_TO_TXT) {
            txtExporter.writeLine("################################\n");
            txtExporter.writeLine("词频统计（默认排序）\n");
            for (WordElement contentWord : contentWords) {
                txtExporter.writeLine(String.format("%1$-20s\t%2$.8f\n",contentWord.getWord(),contentWord.getWordFrequency()));
                //txtExporter.writeLine("单词：" + contentWord.getWord()+ "\t词频：" + contentWord.getWordFrequency() + "\n");
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
            
            for (WordElement contentWord : contentWords) {
                Row newRow = newSheet.createRow(rowNum);
                newRow.createCell(0).setCellValue(contentWord.getWord());
                newRow.createCell(1).setCellValue(contentWord.getWordFrequency());
                rowNum++;
            }
        }
        
    }
    
    /**
     * 获取词频数组
     * @return 默认排序词频数组
     */
    public WordElement[] getContentWords() {
        countTextWords();       //排序前先得到词频数组
        return contentWords;
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
    private /*HashMap<String, Double>*/void countTextWords(){  
    	String lemmaContent=LemmatizationAnalyse.lemmatize(document);
    	String[] contentArray=lemmaContent.split(" ");
        //String stringArray[]=string.split("[^a-zA-Z]");  //正则表达式识别字母
        /*
         * 创建哈希图存放单词及其累计个数
         */
        final Map<String,Integer> map=new HashMap<String, Integer>();  //存放单词及统计数目
        //final Map<String,Double> resultMap=new HashMap<String,Double>();	//存放单词及计算得到的频率
        Integer count;  
        
        for(int i=0;i<contentArray.length;i++){  
            count=map.get(contentArray[i]);  
            if(count==null){  
                map.put(contentArray[i], 1);  
            }else{
                map.put(contentArray[i], count+1);
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
        int size=map.size();//获取数组的大小
        //System.out.println(size);
        WordElement[] wordElement=new WordElement[size];//存放单词信息（单词+词频+词数）
        for(int i=0;i<size;i++){
        	wordElement[i]=new WordElement();
        }
        
        Set set=map.keySet();  
        Integer sum=0,value;  
        String key;  
        //第一次遍历求出单词的总个数
        int i=0;//初始化计数器
        for(Iterator iter=set.iterator();iter.hasNext();){ 
            key=(String)iter.next();  
            value=map.get(key);  
            wordElement[i].setWord(key);
            wordElement[i].setWordCount(value);
            sum+=value;  
            i++;
        } 
        //第二次遍历求出频率，并保存到新的HashMap中
        i=0;//初始化计数器
        for(Iterator iter=set.iterator();iter.hasNext();){
        	key=(String)iter.next();
        	//resultMap.put(key, (double)map.get(key)/sum);
        	wordElement[i].setWordFrequency((double)wordElement[i].getWordCount()/sum);
        	i++;
        }
        this.contentWords = wordElement;
        //return wordElement;
    }
    
    public static ArrayList<WordElement> toArrayList(WordElement[] wordElement) {
        ArrayList<WordElement> list = new ArrayList<WordElement>();
        for (int i = 0; i < wordElement.length; i++) {
            list.add(wordElement[i]);
        }
        return list;
    }
    
    
    /*public static void main(String[] args) throws Exception{
    	String fileName="testTxt.txt";
    	String content=ImportFile.importFile(fileName);
    	HashMap<String, Double>map=new HashMap<String, Double>();
		map=WordFrequencyAnalyse.countTextWords(content);
		ArrayList<Map.Entry<String, Double>> list = new ArrayList<Map.Entry<String, Double>>();
		list=SortedResult.SortedByFreqAscend(map);
		for(Map.Entry<String, Double>mapping:list){
        	java.text.DecimalFormat df=new java.text.DecimalFormat("#0.000");  
        	System.out.println(mapping.getKey()+":"+df.format(mapping.getValue()));
        }
    }*/
    /*
     * 测试频率输出
     */
    /*public static void main(String[] args)throws Exception{
    	File file=new File("testTxt.txt");
    	String content=ImportFile.importFile(file);
    	WordElement[] wordElement=countTextWords(content);
    	System.out.println(wordElement.length);
    	for(int i=0; i<wordElement.length; i++){
    		java.text.DecimalFormat df=new java.text.DecimalFormat("#0.000");
    		System.out.println(wordElement[i].getWord()+":"+df.format(wordElement[i].getWordFrequency()));
    	}
    	double sum=0.0;
    	for(int i=0; i<wordElement.length; i++){
    		sum+=wordElement[i].getWordFrequency();
    	}
    	System.out.println();
    	System.out.println(sum);
    }*/
}
