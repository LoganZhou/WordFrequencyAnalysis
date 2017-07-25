package Frequency;

/*
 * 单词元素，装入有序数组时的元素
 * 
 * 主要方法：
 * 1.带参数构造器（词性和单词原形为不可构造）
 * 2.无参数构造器
 * 3.获取单词
 * 4.获取词性
 * 5.获取单词原形
 * 6.获取词频
 * 7.获取词数
 * 8.设置单词
 * 9.设置词性
 * 10.设置单词原形
 * 11.设置词频
 * 12.设置词数
 */
public class WordElement {
    private String word;            //单词
    private String position;		//词性
    private String original;		//单词原形
    private double wordFrequency;   //词频
    private int wordCount;          //词数
    
    /*
     * 单词元素构造器
     * 
     * @param word 单词
     * @param wordFrequency 词频
     * @param wordCount 词数
     */
    public WordElement(String word, double wordFrequency, int wordCount) {
        this.word = word;
        this.wordFrequency = wordFrequency;
        this.wordCount = wordCount;
        this.position="";
        this.original="";
    }
    /*
     * 单词元素构造器
     * 默认值为0
     */
    public WordElement(){
    	this.word = "";
    	this.position="";
        this.original="";
        this.wordFrequency = 0;
        this.wordCount = 0;
    }
    /*
     * 获取单词
     * 
     * @return 单词
     */
    public String getWord() {
        return word;
    }
    
    /*
     * 获取词性
     * 
     * @return 词性
     */
    public String getPosition(){
    	return position;
    }
    
    /*
     * 获取原形
     * 
     * @return 原形
     */
    public String getOriginal(){
    	return original;
    }
    
    /*
     * 获取词频
     * 
     * @return 词频
     */
    public double getWordFrequency() {
        return wordFrequency;
    }
    
    /*
     * 获取词数
     * 
     * @return 词数
     */
    public int getWordCount() {
        return wordCount;
    }
    
    /*
     * 设置单词
     * 
     * @param word 单词
     */
    public void setWord(String word){
    	this.word=word;
    }
    
    /*
     * 设置词性
     * 
     * @param position 词性
     */
    public void setPosition(String position){
    	this.position=position;
    }
    
    /*
     * 设置单词原形
     * 
     * @param original 单词原形
     */
    public void setOriginal(String original){
    	this.original=original;
    }
    
    /*
     * 设置词频
     * 
     * @param wordFrequency 词频
     */
    public void setWordFrequency(double wordFrequency){
    	this.wordFrequency=wordFrequency;
    }
    
    /*
     * 设置词数
     * 
     * @param wordCount 词数
     */
    public void setWordCount(int wordCount){
    	this.wordCount=wordCount;
    }
    
    public void addWordCount(int value) {
        this.wordCount += value;
    }
}
