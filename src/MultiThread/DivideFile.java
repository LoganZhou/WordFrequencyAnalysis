/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package MultiThread;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;

/**
 *
 * @author Dementors
 */
public class DivideFile {
    /**
     * 
     * @param file 待拆分TXT文件
     * @return 拆分所得文件个数
     * @throws FileNotFoundException 文件未找到异常（FileNotFoundException）
     * @throws IOException 文件IO异常（IOException）
     */
    public static int divideFile(File file) throws FileNotFoundException, IOException{
        ArrayList<String> contentList=new ArrayList<String>();//存放读取的内容
        String rowContent;//读入每行的内容
        System.out.println("文件大小："+file.length());
        
        FileReader reader=new FileReader(file);
        BufferedReader br=new BufferedReader(reader);
        //System.out.println(br.readLine());
        
        int totalRow=0;//计算总行数
        while((rowContent=br.readLine())!=null){
            contentList.add(rowContent);
            totalRow++;
        }
        System.out.println("总行数："+totalRow);     
        
        int divideNum=totalRow/200;//需要拆分的文件数
        int remain=totalRow%200;//得出余数
        if(remain>0){divideNum+=1;}//有余数则要+1
        System.out.println("拆分个数："+divideNum);
        
        //创建目录
        /*if(DivideFile.createDir("temp")){
            for(int i=0;i<divideNum;i++){//创建拆分文件
                FileWriter writer=new FileWriter("/temp"+i+"_"+file.getName());
                writer.close();
            }
        }else{
            for(int i=0;i<divideNum;i++){//创建拆分文件
                FileWriter writer=new FileWriter("/temp"+i+"_"+file.getName());
                writer.close();
            }
        }*/
        for(int i=0;i<divideNum;i++){//创建拆分文件
            FileWriter writer=new FileWriter(i+"_"+file.getName());
            writer.close();
        }
        
        int count=0;//计数器
        int writtenFile;//确定该写入哪个文件
        Iterator it=contentList.iterator();
        while(it.hasNext()){
            String str=(String) it.next();
            writtenFile=count%divideNum;
            FileWriter writer=new FileWriter(writtenFile+"_"+file.getName(), true);
            writer.append(str+"\r\n");
            writer.close();
            count++;
        }
        return divideNum;
    }
    
    /**
     * 判断创建目录是否成功
     * @param dirName 目录名
     * @return 是否创建成功
     */
    public static boolean createDir(String dirName){
        File dir=new File(dirName);
        if(dir.exists()){
            System.out.println("目录已存在");
            return false;
        }else{
            dir.mkdir();
            return true;
        }
    }
    /*public static void main(String[] args) throws IOException{
        File file=new File("圣经.txt");
        long start=System.nanoTime();
        DivideFile.divideFile(file);
        long end=System.nanoTime();
        System.out.println("文档拆分时间："+(end-start)+"ns");
        /*
        BufferedInputStream mainBin = new BufferedInputStream(new FileInputStream(file)); 
        //BufferedInputStream subBin=new BufferedInputStream(new FileInputStream("0_飘英文版.txt"));
        
        int codeMain=(mainBin.read()<<8)+mainBin.read();
        //int codeSub=(subBin.read()<<8)+subBin.read();
        
        switch (codeMain) {    
              case 0xefbb:    
                  System.out.println("UTF-8");   
                  break;    
              case 0xfffe:    
                  System.out.println("Unicode"); 
                  break;    
              case 0xfeff:    
                  System.out.println("UTF-16BE");
                  break;    
              default:    
                  System.out.println("GBK");
        }
        
        /*switch (codeSub) {    
              case 0xefbb:    
                  System.out.println("UTF-8");   
                  break;    
              case 0xfffe:    
                  System.out.println("Unicode"); 
                  break;    
              case 0xfeff:    
                  System.out.println("UTF-16BE");
                  break;    
              default:    
                  System.out.println("GBK");
        }*/
    //}
}
