/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package MultiThread;

import ExportToFile.ExportToTxt;
import Frequency.WordElement;
import Frequency.WordFrequencyAnalyse;
import ImportFile.ImportFile;
import WordChanges.LemmatizationAnalyse;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.Callable;
import java.util.concurrent.FutureTask;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Dementors
 */
public class AnalyseThread implements Callable<ThreadResult>{
    private final File file;
    private WordElement[] wordElements;
    private int wordsCount;
    
    public AnalyseThread(File file){
        this.file=file;
        wordsCount = 0;
    }
   
    @Override
    public ThreadResult call() throws Exception {
        String document = null;
        try {
            document = ImportFile.importFile(file);
        } catch (IOException ex) {
            Logger.getLogger(AnalyseThread.class.getName()).log(Level.SEVERE, null, ex);
        }
        WordFrequencyAnalyse frequencyAnalyse = new WordFrequencyAnalyse(document);
        wordElements = frequencyAnalyse.analysisForMultiThreads();
        //wordElements = LemmatizationAnalyse.removeRepeated(wordElements);
        //返回结果
        HashMap<String,WordElement> map = new HashMap<String,WordElement>();
        for (WordElement element : wordElements) {
            wordsCount += element.getWordCount();
            map.put(element.getWord(), element);
        }
        return new ThreadResult(wordsCount, map);
    }
}
