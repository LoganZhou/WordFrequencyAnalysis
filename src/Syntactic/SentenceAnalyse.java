/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Syntactic;

import ExportToFile.ExportToTxt;
import ImportFile.ImportFile;
import edu.stanford.nlp.dcoref.CorefChain;
import edu.stanford.nlp.dcoref.CorefCoreAnnotations;
import edu.stanford.nlp.ling.CoreAnnotations;
import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.pipeline.Annotation;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import edu.stanford.nlp.semgraph.SemanticGraph;
import edu.stanford.nlp.semgraph.SemanticGraphCoreAnnotations;
import edu.stanford.nlp.trees.Tree;
import edu.stanford.nlp.trees.TreeCoreAnnotations;
import edu.stanford.nlp.util.CoreMap;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;

import java.util.List;
import java.util.Map;
import java.util.Properties;
import javax.swing.JTextArea;

/**
 * 
 * @author Dementors
 */
public class SentenceAnalyse {
    /**
     * 仅将分析结果输出到文本框中
     * @param content 待分析文档String
     * @param resultArea 显示输出框
     */
    public static void sentenceAnalyse(String content, JTextArea resultArea) {
        //String[] sentences = content.split(".");//将文章按句子拆分
        //String[] grammaTree = null;//存放分析完毕的语法树
        ArrayList<String> strList=new ArrayList();
        Properties props = new Properties();
        props.setProperty("annotators", "tokenize, ssplit, pos, lemma, ner, parse");
        StanfordCoreNLP pipeline = new StanfordCoreNLP(props);
 
        Annotation document = new Annotation(content);  
        pipeline.annotate(document);  
        strList=parserResult(document);  
        
        int i=1;//句子计数器
        Iterator it=strList.iterator();
        while(it.hasNext()){
            String grammaTree=(String)it.next();
            resultArea.append("第"+i+"句话的语法树：\n");
            resultArea.append(grammaTree+"\n");
            i++;
        }
    }

    /**
     * 将分析结果显示到文本框并输出到TXT文件（句法树暂不支持XLSX文件）
     * @param content 待分析文档String
     * @param resultArea 显示输出框
     * @param txtExporter txt文件输出器
     */
    public static void resultOutput(String content, JTextArea resultArea, ExportToTxt txtExporter){
        ArrayList<String> strList=new ArrayList();
        Properties props = new Properties();
        props.setProperty("annotators", "tokenize, ssplit, pos, lemma, ner, parse");
        StanfordCoreNLP pipeline = new StanfordCoreNLP(props);
 
        Annotation document = new Annotation(content);  
        pipeline.annotate(document);  
        strList=parserResult(document);  
        
        int i=1;//句子计数器
        Iterator it=strList.iterator();
        while(it.hasNext()){
            String grammaTree=(String)it.next();
            resultArea.append("第"+i+"句话的语法树：\n");
            resultArea.append(grammaTree+"\n");
            i++;
        }
        
        //写入到TXT中
        i=1;
        txtExporter.writeLine("################################\n");
        Iterator itt=strList.iterator();
        while(itt.hasNext()){
            String grammaTree=(String)itt.next();
            txtExporter.writeLine("第"+i+"句话的语法树：\n");
            txtExporter.writeLine(grammaTree+"\n");
            i++;
        }
    }
    
    public static ArrayList<String> parserResult(Annotation document) {
        ArrayList<String> strList=new ArrayList();
        List<CoreMap> sentences = document.get(CoreAnnotations.SentencesAnnotation.class);
        for (CoreMap sentence : sentences) {
            for (CoreLabel token : sentence.get(CoreAnnotations.TokensAnnotation.class)) {
                String word = token.get(CoreAnnotations.TextAnnotation.class);
                String pos = token.get(CoreAnnotations.PartOfSpeechAnnotation.class);
                String ne = token.get(CoreAnnotations.NamedEntityTagAnnotation.class);
            }
            Tree tree = sentence.get(TreeCoreAnnotations.TreeAnnotation.class);
            String tempStr=tree.toString();
            System.out.println(tempStr);
            strList.add(tempStr);
        }
        return strList;
    }
    /*public static void main(String[] args) throws IOException {
        File file = new File("testTxt.txt");
        String[] grammaTree = null;
        long start=System.nanoTime();
        long end=System.nanoTime();
        System.out.println(end-start);
    }*/
}
