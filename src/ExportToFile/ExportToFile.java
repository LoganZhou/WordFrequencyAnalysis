/**
 * Created by zhouheng on 2017/7/5.
 */
package ExportToFile;

import java.util.ArrayList;
import java.util.Map;


/**
 * 结果文件导出接口
 */
public interface ExportToFile {
    
    void writeArrayList(ArrayList<Map.Entry<String, Double>> list, String title);
    void writeLine();
//    /**
//     * 统计词频(无序)
//     * @param list 待输出List
//     */
//    void frequencyOfWords(ArrayList<Map.Entry<String, Double>> list);
//
//    /**
//     * 结果按词频升序输出
//     * @param list 按词频升序List
//     */
//    void frequencyAscend(ArrayList<Map.Entry<String, Double>> list);
//
//    /**
//     * 结果按词频降序输出
//     * @param list 按词频降序List
//     */
//    void frequencyDescend(ArrayList<Map.Entry<String, Double>> list);
//
//    /**
//     * 结果按字母升序输出
//     * @param list 按字母升序List
//     */
//    void alphabeticalAscend(ArrayList<Map.Entry<String, Double>> list);
//
//    /**
//     * 结果按字母降序输出
//     * @param list 按字母降序List
//     */
//    void alphabeticalDescend(ArrayList<Map.Entry<String, Double>> list);
//    
//    /**
//     * 超纲词分析结果写入，每次写入一行内容
//     * @param oneLineString 一行内容
//     */
//    void outOfBound(String oneLineString);
//    
//    /**
//     * 单词检索（包括按长度、词头、词尾）结果写入，每次写入一行
//     * @param oneLineString 一行内容
//     */
//    void extractWord(String oneLineString);
//    
//    /**
//     * 词形变换分析结果写入，每次写入一行
//     * @param oneLineString 一行内容
//     */
//    void wordChanges(String oneLineString);
}
