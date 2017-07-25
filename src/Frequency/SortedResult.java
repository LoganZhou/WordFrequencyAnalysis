package Frequency;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;

/*
 * 排序模块
 * 	1.词频升序排列
 * 	2.词频降序排列
 * 	3.字母升序排列
 * 	4.字母降序排列
 */
public class SortedResult {
	/*
	 * 按词频升序排列
	 * @param wordElement 单词元素：存放有单词+词频+词数
	 * @return list 已按照词频升序排列好的ArrayList
	 */
	/*public static ArrayList<Entry<String, Double>> SortedByFreqAscend(HashMap<String, Double>map){
		ArrayList<Map.Entry<String, Double>> list = new ArrayList<Map.Entry<String, Double>>(map.entrySet());
		Collections.sort(list, new Comparator<Map.Entry<String, Double>>() {  
            public int compare(Entry<String, Double> o1, Entry<String, Double> o2) {  
                return o1.getValue().compareTo(o2.getValue()); 
            }  
        });
		return list;
	}*/
	public static ArrayList<WordElement> SortedByFreqAscend(WordElement[] wordElement){
		ArrayList<WordElement> list=new ArrayList<WordElement>();
		for(int i=0;i<wordElement.length;i++){
			list.add(wordElement[i]);
		}
		Collections.sort(list, new Comparator<WordElement>(){
			public int compare(WordElement we1,WordElement we2){
				return (we1.getWordCount()-we2.getWordCount());
			}
		});
		return list;
	}
	/*
	 * 按词频降序排列
	 * @param wordElement 单词元素：存放有单词+词频+词数
	 * @return list 已按照词频降序排列好的ArrayList
	 */
	/*public static ArrayList<Entry<String, Double>> SortedByFreqDescend(HashMap<String, Double>map){
		ArrayList<Map.Entry<String, Double>> list = new ArrayList<Map.Entry<String, Double>>(map.entrySet());
		Collections.sort(list, new Comparator<Map.Entry<String, Double>>() {  
            public int compare(Entry<String, Double> o1, Entry<String, Double> o2) {  
                return o2.getValue().compareTo(o1.getValue()); 
            }  
        });
		return list;
	}*/
	public static ArrayList<WordElement> SortedByFreqDescend(WordElement[] wordElement){
		ArrayList<WordElement> list=new ArrayList<WordElement>();
		for(int i=0;i<wordElement.length;i++){
			list.add(wordElement[i]);
		}
		Collections.sort(list, new Comparator<WordElement>(){
			public int compare(WordElement we1,WordElement we2){
				return (we2.getWordCount()-we1.getWordCount());
			}
		});
		return list;
	}
	
	/*
	 * 按字母升序排序
	 * @param wordElement 单词元素：存放有单词+词频+词数
	 * @return list 已按照字母升序排列好的ArrayList
	 */
	/*public static ArrayList<Entry<String, Double>> SortedByAlphaAscend(HashMap<String, Double>map){
		ArrayList<Map.Entry<String, Double>> list = new ArrayList<Map.Entry<String, Double>>(map.entrySet());
        Collections.sort(list, new Comparator<Map.Entry<String, Double>>() {  
            public int compare(Entry<String, Double> o1, Entry<String, Double> o2) {  
            	return o1.getKey().compareTo(o2.getKey());
            }  
        }); 
		return list;
	}*/
	public static ArrayList<WordElement> SortedByAlphaAscend(WordElement[] wordElement){
		ArrayList<WordElement> list=new ArrayList<WordElement>();
		for(int i=0;i<wordElement.length;i++){
			list.add(wordElement[i]);
		}
		Collections.sort(list, new Comparator<WordElement>(){
			public int compare(WordElement we1,WordElement we2){
				return (we1.getWord().compareTo(we2.getWord()));
			}
		});
		return list;
	}
	
	/*
	 * 按字母降序排列
	 * @param wordElement 单词元素：存放有单词+词频+词数
	 * @return list 已按照字母降序排列好的ArrayList
	 */
	/*public static ArrayList<Entry<String, Double>> SortedByAlphaDescend(HashMap<String, Double>map){
		ArrayList<Map.Entry<String, Double>> list = new ArrayList<Map.Entry<String, Double>>(map.entrySet());
        Collections.sort(list, new Comparator<Map.Entry<String, Double>>() {  
            public int compare(Entry<String, Double> o1, Entry<String, Double> o2) {  
            	return o2.getKey().compareTo(o1.getKey());
            }  
        }); 
		return list;
	}*/
	public static ArrayList<WordElement> SortedByAlphaDescend(WordElement[] wordElement){
		ArrayList<WordElement> list=new ArrayList<WordElement>();
		for(int i=0;i<wordElement.length;i++){
			list.add(wordElement[i]);
		}
		Collections.sort(list, new Comparator<WordElement>(){
			public int compare(WordElement we1,WordElement we2){
				return (we2.getWord().compareTo(we1.getWord()));
			}
		});
		return list;
	}
}
