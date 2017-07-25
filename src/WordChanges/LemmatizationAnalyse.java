package WordChanges;

import ExportToFile.ExportToExcel;
import ExportToFile.ExportToTxt;
import Frequency.WordElement;
import static Frequency.WordFrequencyAnalyse.EXPORT_TO_EXCEL;
import static Frequency.WordFrequencyAnalyse.EXPORT_TO_TXT;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.TreeMap;
import ImportFile.ImportFile;

import edu.stanford.nlp.ling.CoreAnnotations.LemmaAnnotation;
import edu.stanford.nlp.ling.CoreAnnotations.PartOfSpeechAnnotation;
import edu.stanford.nlp.ling.CoreAnnotations.SentencesAnnotation;
import edu.stanford.nlp.ling.CoreAnnotations.TextAnnotation;
import edu.stanford.nlp.ling.CoreAnnotations.TokensAnnotation;
import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.pipeline.Annotation;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import edu.stanford.nlp.util.CoreMap;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import javax.swing.JTextArea;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.util.CellRangeAddress;

/*
 * 词形还原分析模块
 * 本模块包含三个方法：
 * 1.文章词形还原
 * 2.文章分词分句
 * 3.词形变换统计
 * 4.词频变换统计（功能集成版）
 * 5.词频变换及词性标注<<推荐使用
 */
public class LemmatizationAnalyse {
    private WordElement[] wordElements;             //待转换成原形的WordElement[]
    private HashMap<String, PositionType> positionCount; //词性统计
    private ExportToTxt txtExporter;        //txt文件输出，必须要先被初始化
    private ExportToExcel excelExporter;    //excel文件输出，必须要先被初始化
    public final static int EXPORT_TO_TXT = 1;      //输出txt
    public final static int EXPORT_TO_EXCEL = 2;    //输出excel
    private JTextArea resultArea;
    
    class PositionType {
        private String type;
        private String explanation;
        private Integer count;
        
        /**
         * 词性对象构造函数，用于插入HashMap时初始化
         * @param type 词性缩写
         * @param explanation 词性解释 
         */
        public PositionType(String type, String explanation) {
            this.type = type;
            this.explanation = explanation;
            count = 0;
        }
        
        /**
         * 更新词性统计,计数加1
         */
        public void addCount() {
            this.count ++;
        }
        
        /**
         * 取得当前词性统计个数
         * @return 个数
         */
        public Integer getCount() {
            return count;
        }

        public String getType() {
            return type;
        }

        public String getExplanation() {
            return explanation;
        }
    }
    
    /**
     * 词性变换构造函数,仅输出到显示框
     * @param document 待转换文档String
     * @param resultArea 结果显示框
     */
    public LemmatizationAnalyse(String document, JTextArea resultArea) {
        this.wordElements = getPositionAndLemma(document);
        this.resultArea = resultArea;
        init();
    }
    
    /**
     * 词性变换构造函数,输出到txt文件
     * @param document 待转换文档String
     * @param resultArea 结果显示框
     * @param txtExporter txt文件输出器
     */
    public LemmatizationAnalyse(String document, JTextArea resultArea, ExportToTxt txtExporter) {
        this.wordElements = getPositionAndLemma(document);
        this.txtExporter = txtExporter;
        this.resultArea = resultArea;
        init();
    }

    /**
     * 词性变换构造函数,输出到excel文件
     * @param document 待转换文档String
     * @param resultArea 结果显示框
     * @param excelExporter excel文件输出器
     */
    public LemmatizationAnalyse(String document, JTextArea resultArea, ExportToExcel excelExporter) {
        this.wordElements = getPositionAndLemma(document);
        this.excelExporter = excelExporter;
        this.resultArea = resultArea;
        init();
    }
    
    /**
     * 初始化词性变换统计HashMap
     */
    private void init() {
        positionCount = new HashMap<String, PositionType>();
        positionCount.put("CC", new PositionType("CC","并列连词"));
        positionCount.put("CD", new PositionType("CD","基数"));
        positionCount.put("DT", new PositionType("DT","限定词"));
        positionCount.put("EX", new PositionType("EX","存在词"));
        positionCount.put("FW", new PositionType("FW","外来语"));
        positionCount.put("IN", new PositionType("IN","介词或从属连词"));
        positionCount.put("JJ", new PositionType("JJ","形容词"));
        positionCount.put("JJR", new PositionType("JJR","形容词比较级"));
        positionCount.put("JJS", new PositionType("JJS","形容词最高级"));
        positionCount.put("LS", new PositionType("LS","List item marker"));
        positionCount.put("MD", new PositionType("MD","情态动词"));
        positionCount.put("NN", new PositionType("NN","名词单数或集合名词"));
        positionCount.put("NNS", new PositionType("NNS","名词复数"));
        positionCount.put("NNP", new PositionType("NNP","专有名词单数"));
        positionCount.put("NNPS", new PositionType("NNPS","专有名词复数"));
        positionCount.put("PDT", new PositionType("PDT","前置限定词"));
        positionCount.put("POS", new PositionType("POS","Possessive ending"));
        positionCount.put("PRP", new PositionType("PRP","人称代词"));
        positionCount.put("PRP$", new PositionType("PRP$","所有格代词"));
        positionCount.put("RB", new PositionType("RB","副词"));
        positionCount.put("RBR", new PositionType("RBR","副词比较级"));
        positionCount.put("RBS", new PositionType("RBS","副词最高级"));
        positionCount.put("RP", new PositionType("RP","小品词"));
        positionCount.put("SYM", new PositionType("SYM","符号"));
        positionCount.put("TO", new PositionType("TO","to"));
        positionCount.put("UH", new PositionType("UH","叹词"));
        positionCount.put("VB", new PositionType("VB","动词原形"));
        positionCount.put("VBD", new PositionType("VBD","动词过去式"));
        positionCount.put("VBG", new PositionType("VBG","动词现在分词"));
        positionCount.put("VBN", new PositionType("VBN","动词过去分词"));
        positionCount.put("VBP", new PositionType("VBP","动词非第三人称单数"));
        positionCount.put("VBZ", new PositionType("VBZ","动词第三人称单数"));
        positionCount.put("WDT", new PositionType("WDT","疑问限定词"));
        positionCount.put("WP", new PositionType("WP","疑问代词"));
        positionCount.put("VWP$", new PositionType("VWP$","Possessive wh-pronoun"));
        positionCount.put("WRB", new PositionType("WRB","疑问副词"));
    }
    
    /**
     * 启动词形变换分析，并将结果输出到输出框
     */
    public void analysis() {
        resultArea.append("\n\n################################\n");
        resultArea.append("开始词性变换分析\n");
        resultArea.append(String.format("%1$-20s%2$-20s%3$s\n", "单词","词性","原形"));
        for (WordElement element : wordElements) {
            resultArea.append(String.format("%1$-20s%2$-20s%3$-20s\n", element.getWord(),
                element.getPosition(), element.getOriginal()));
            PositionType currentType = positionCount.get(element.getPosition());
            currentType.addCount();
        }
        resultArea.append("\n-----------------------------------\n");
        resultArea.append("词形变换统计结果\n");
        resultArea.append(String.format("%1$-10s%2$-50s%3$-10s\n", "词性类别","词性解释","数目"));
        Iterator iter = positionCount.entrySet().iterator();
        while(iter.hasNext()) {
            Map.Entry entry = (Map.Entry) iter.next();
            PositionType positionType = (PositionType)entry.getValue();
            if (positionType.getCount() != 0) {
                resultArea.append(String.format("%1$-10s%2$-50s%3$-10d\n", 
                        positionType.getType(),positionType.getExplanation(),
                        positionType.getCount()));
            }
        }
        resultArea.append("统计完毕！");
    }
    
    /**
     * 启动词形变换分析，结果输出到文档
     * @param exportType 输出类型
     */
    public void analysis(int exportType) {
        analysis();
        
        int count = 0;
        if (exportType == EXPORT_TO_TXT) {
            txtExporter.writeLine("################################\n");
            txtExporter.writeLine("开始词性变换分析\n");
            txtExporter.writeLine(String.format("%1$-20s%2$-20s%3$s\n", "单词","词性","原形"));
            for (WordElement element : wordElements) {
                txtExporter.writeLine(String.format("%1$-20s%2$-20s%3$-20s\n", element.getWord(),
                element.getPosition(), element.getOriginal()));
                PositionType currentType = positionCount.get(element.getPosition());
                currentType.addCount();
            }
            txtExporter.writeLine("\n-----------------------------------\n");
            txtExporter.writeLine("词形变换统计结果\n");
            txtExporter.writeLine(String.format("%1$-10s%2$-50s%3$-10s\n", "词性类别","词性解释","数目"));
            
            Iterator iter = positionCount.entrySet().iterator();
            while (iter.hasNext()) {
                Map.Entry entry = (Map.Entry) iter.next();
                PositionType positionType = (PositionType) entry.getValue();
                if (positionType.getCount() != 0) {
                    txtExporter.writeLine(String.format("%1$-10s%2$-50s%3$-10d\n", 
                        positionType.getType(),positionType.getExplanation(),
                        positionType.getCount()));
                }
            }
            txtExporter.writeLine("统计完毕！");
            
        } else if (exportType == EXPORT_TO_EXCEL){
            //创建一个新表,记录词性变换分析
            Sheet newSheet = excelExporter.createNewSheet("词性变换分析");
            newSheet.addMergedRegion(new CellRangeAddress(0,0,0,2));//合并单元格，创建表名
            Row firstRow = newSheet.createRow(0);
            firstRow.createCell((short)0).setCellValue("词性变换分析表");
            Row secondRow = newSheet.createRow(1);
            secondRow.createCell(0).setCellValue("单词");
            secondRow.createCell(1).setCellValue("词性");
            secondRow.createCell(2).setCellValue("原形");
            int rowNum = 2;//行数从第三行开始
            
            for (WordElement contentWord : wordElements) {
                Row newRow = newSheet.createRow(rowNum);
                newRow.createCell(0).setCellValue(contentWord.getWord());
                newRow.createCell(1).setCellValue(contentWord.getPosition());
                newRow.createCell(2).setCellValue(contentWord.getOriginal());
                rowNum++;
            }
            
            //创建一个新表，记录文档词性变换统计结果
            Sheet statistics = excelExporter.createNewSheet("词形变换统计");
            statistics.addMergedRegion(new CellRangeAddress(0,0,0,2));//合并单元格，创建表名
            firstRow = statistics.createRow(0);
            firstRow.createCell((short)0).setCellValue("词形变换统计表");
            secondRow = statistics.createRow(1);
            secondRow.createCell(0).setCellValue("词性");
            secondRow.createCell(1).setCellValue("词性解释");
            secondRow.createCell(2).setCellValue("词数");
            rowNum = 2;//行数从第三行开始
            
            Iterator iter = positionCount.entrySet().iterator();
            while (iter.hasNext()) {
                Map.Entry entry = (Map.Entry) iter.next();
                PositionType positionType = (PositionType) entry.getValue();
                if (positionType.getCount() != 0) {
                    Row newRow = statistics.createRow(rowNum);
                    newRow.createCell(0).setCellValue(positionType.getType());
                    newRow.createCell(1).setCellValue(positionType.getExplanation());
                    newRow.createCell(2).setCellValue(positionType.getCount());
                    rowNum++;
                }
            }
        }
    }
    
    /**
        * 文章词形还原
        * 
        * @param content 待还原词形的文章
        * 
        * @return 已还原词形的文章
     */
    public static String lemmatize(String content) {
        String resultContent = "";
        Properties props = new Properties();
        props.put("annotators", "tokenize, ssplit, pos, lemma");
        StanfordCoreNLP nlp = new StanfordCoreNLP(props);
        Annotation doc = new Annotation(content);
        nlp.annotate(doc);
        List<CoreMap> sentences = doc.get(SentencesAnnotation.class);
        for (CoreMap sentence : sentences) {
            for (CoreLabel token : sentence.get(TokensAnnotation.class)) {
                String lema = token.get(LemmaAnnotation.class);
                resultContent += lema;
                resultContent += " ";
                //System.out.print(lema+" ");
            }
        }
        //System.out.println(resultContent);
        return resultContent;
    }

    /*
	 * 文章分词及分句（仅为分词处理，未变换词形）
	 * 仅实现对英文文章的分词和分句。
	 * 
	 * @param content 未进行分词分句的文章
	 * 
	 * @return 已进行分词分句的文章
     */
    public static String tokenize(String content) {
        String resultContent = "";
        Properties props = new Properties();
        props.put("annotators", "tokenize, ssplit");
        StanfordCoreNLP nlp = new StanfordCoreNLP(props);
        Annotation doc = new Annotation(content);
        nlp.annotate(doc);
        List<CoreMap> sentences = doc.get(SentencesAnnotation.class);
        for (CoreMap sentence : sentences) {
            for (CoreLabel token : sentence.get(TokensAnnotation.class)) {
                String word = token.get(TextAnnotation.class);
                resultContent += word;
                resultContent += " ";
                //System.out.print(word+" ");
            }
        }
        //System.out.println(resultContent);
        return resultContent;
    }

    /*
	 * 词形变换统计
	 * 对传入的词形变换前文章和词形变换后文章进行比较处理，
	 * 统计出存在词形变换的单词，并保存其变形形式和单词原形
	 * 
	 * @param rawContent 词形变换前的文章
	 * @param resultContent 词形变换后的文章
	 * 
	 * @return 变形形式+原形形式
     */
    public static TreeMap<String, String> lemmmaStatistics(String rawContent, String resultContent) {
        //TreeMap<String, Integer> countResult=new TreeMap<String, Integer>();
        //按空格切分单词并保存到字符串数组中
        String[] rawArray = rawContent.split(" ");
        String[] resultArray = resultContent.split(" ");

        //保存处理掉标点符号的单词
        ArrayList<String> rawList = new ArrayList<String>();
        ArrayList<String> resultList = new ArrayList<String>();

        //处理标点符号
        for (int i = 0; i < rawArray.length; i++) {
            if (rawArray[i].charAt(0) == '?' || rawArray[i].charAt(0) == '.' || rawArray[i].charAt(0) == ',' || rawArray[i].charAt(0) == '\'' || rawArray[i].charAt(0) == '\"' || rawArray[i].charAt(0) == '`') {
                continue;
            } else {
                rawList.add(rawArray[i]);
            }
        }
        for (int i = 0; i < resultArray.length; i++) {
            if (resultArray[i].charAt(0) == '?' || resultArray[i].charAt(0) == '.' || resultArray[i].charAt(0) == ',' || resultArray[i].charAt(0) == '\'' || resultArray[i].charAt(0) == '\"' || resultArray[i].charAt(0) == '`') {
                continue;
            } else {
                resultList.add(resultArray[i]);
            }
        }

        //统计变形的单词（K=单词变形，V=单词原形）
        TreeMap<String, String> lemmaMap = new TreeMap<String, String>();
        for (int i = 0; i < rawList.size(); i++) {
            if (rawList.get(i).equals(resultList.get(i))) {
                continue;
            } else {
                lemmaMap.put(rawList.get(i), resultList.get(i));
            }
        }

        //测试统计数目
        /*Iterator it=lemmaMap.entrySet().iterator();
		while (it.hasNext()) {   
		    Map.Entry entry = (Map.Entry) it.next();   
		    System.out.println(entry.getKey()+" "+entry.getValue());
		} */
        return lemmaMap;
        /*Iterator<String> it1=rawList.iterator();
		Iterator<String> it2=resultList.iterator();
		while(it1.hasNext()&&it2.hasNext()){
			System.out.println(it1.next()+"\t\t"+it2.next());
		}*/
 /*Iterator<String> it=rawList.iterator();
		while(it.hasNext()){
			System.out.println(it.next());
		}*/

 /*Iterator<String> itt=resultList.iterator();
		while(itt.hasNext()){
			System.out.println(itt.next());
		}*/
 /*for(int i=0;i<resultArray.length;i++){
			resultList.add(resultArray[i]);
		}*/
        //for(int i=0;i<resultArray.length;i++)
        //System.out.println(resultArray[i]);
        //System.out.println(rawArray.length+"  "+resultArray.length);
        //System.out.println(rawList.size()+"  "+resultList.size());
        //return countResult;
    }

    /*
	 * 词频变换统计（功能集成版）
	 * 
     */
    public static TreeMap<String, String> lemmmaStatistics(String content) {
        String rawContent = "", resultContent = "";
        rawContent = LemmatizationAnalyse.tokenize(content);
        resultContent = LemmatizationAnalyse.lemmatize(content);
        return LemmatizationAnalyse.lemmmaStatistics(rawContent, resultContent);
    }

    /*
	 * 词频变换及词性标注
     */
    public static WordElement[] getPositionAndLemma(String content) {
        //long startTime = System.currentTimeMillis();        //计算耗时
        
        Properties props = new Properties();
        props.put("annotators", "tokenize, ssplit, pos, lemma");
        StanfordCoreNLP pipeline = new StanfordCoreNLP(props);  //step1: 4.017s
        
//        long step1 = System.currentTimeMillis();
//        System.out.println("step1." + (float)(step1-startTime)/1000);
        
        Annotation document = new Annotation(content);          //step2: 0.002s
        
//        long step2 = System.currentTimeMillis();
//        System.out.println("step2." + (float)(step2-step1)/1000);
        
        pipeline.annotate(document);

        List<CoreMap> sentences = document.get(SentencesAnnotation.class);//step3: 0.946s
        
//        long step3 = System.currentTimeMillis();
//        System.out.println("step3." + (float)(step3-step2)/1000);
        
        //System.out.println(sentences.size());
        int size = 0; //初始化单词元素数组规格

        //int count=0;
        //求出单词元素数组的规格
        for (CoreMap sentence : sentences) {
            for (CoreLabel token : sentence.get(TokensAnnotation.class)) {
                String word = token.get(TextAnnotation.class);
                if (word.charAt(0)>=97 && word.charAt(0)<=122) {
                    size++;
                }
            }
        }
        
        //step4: 0.002s
//        long step4 = System.currentTimeMillis();
//        System.out.println("step4." + (float)(step4-step3)/1000);
        
        WordElement[] wordElement = new WordElement[size];//声明单词元素数组
        for (int j = 0; j < size; j++) {
            wordElement[j] = new WordElement();
        }

        int i = 0;//初始化数组游标	
        //将单词+词性+原形导入数组
        for (CoreMap sentence : sentences) {
            for (CoreLabel token : sentence.get(TokensAnnotation.class)) {
                String word = token.get(TextAnnotation.class);
                String position = token.get(PartOfSpeechAnnotation.class);
                String original = token.get(LemmaAnnotation.class);
                if (word.charAt(0)>=97 && word.charAt(0)<=122) {
                    wordElement[i].setWord(word);
                    wordElement[i].setPosition(position);
                    wordElement[i].setOriginal(original);
                    i++;
                }
                //count++;
                //System.out.println(word+" "+position+" "+original);
            }
        }
        //System.out.println(size);
        
        //step5: 0.004s
//        long step5 = System.currentTimeMillis();
//        System.out.println("step5." + (float)(step5-step4)/1000);
        
        return wordElement;
    }
    
    public static ArrayList<WordElement> toArrayList(WordElement[] wordElement) {
        ArrayList<WordElement> list = new ArrayList<WordElement>();
        for (int i = 0; i < wordElement.length; i++) {
            list.add(wordElement[i]);
        }
        return list;
    }
    
    public static ArrayList<WordElement> removeRepeated(ArrayList<WordElement> list) {
        ArrayList<WordElement> resultList = new ArrayList<WordElement>();//存放结果数组
        TreeMap<String, WordElement> tMap = new TreeMap<String, WordElement>();
        Iterator it = list.iterator();
        while (it.hasNext()) {
            WordElement element = (WordElement) it.next();
            tMap.put(element.getOriginal(), element);
        }

        it = tMap.keySet().iterator();
        while (it.hasNext()) {
            WordElement element = tMap.get(it.next());
            resultList.add(element);
        }
        /*for(WordElement element: set){
            resultList.add(element);
        }*/
        return resultList;
    }
    
    /**
     * 去除重复单词
     * @param wordElement 文档WordElement[]数组
     * @return 去除重复词后的WordElement[]数组
     */
    public static WordElement[] removeRepeated(WordElement[] wordElement){
        ArrayList<WordElement> list=LemmatizationAnalyse.removeRepeated(LemmatizationAnalyse.toArrayList(wordElement));
        int size=list.size();
        WordElement[] resultElement=new WordElement[size];//存放转化完成的元素数组
        
        int i=0;//计数器
        Iterator it=list.iterator();
        while(it.hasNext()){
            WordElement element=(WordElement)it.next();
            resultElement[i]=element;
            i++;
        }
        return resultElement;
    }
}
