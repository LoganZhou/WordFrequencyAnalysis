/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package MultiThread;

import Frequency.WordElement;
import java.util.HashMap;

/**
 * 多线程词数统计结果
 * @author zhouheng
 */
public class ThreadResult {
    private int wordsCount;                 //拆分后文章词数
    HashMap<String,WordElement> wordsMap;   //分析后的Map

    public ThreadResult(int wordsCount, HashMap<String, WordElement> wordsMap) {
        this.wordsCount = wordsCount;
        this.wordsMap = wordsMap;
    }

    public int getWordsCount() {
        return wordsCount;
    }

    public HashMap<String, WordElement> getWordsMap() {
        return wordsMap;
    }
    
    
}
