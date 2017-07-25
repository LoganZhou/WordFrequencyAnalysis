package ImportFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.apache.poi.hwpf.extractor.WordExtractor;

/*
 * 文件导入模块
 * 本模块仅包含一个方法――文件导入
 */
public class ImportFile {
	/*
	 * 文件导入
	 * 读入txt、docx或pdf文件，
	 * 并将其转化为字符串。
	 * 
	 * @param file 待分析的文件
	 * 
	 * @return 从文件中提取的字符串
	 */
	public static String importFile(File file)throws IOException{
		String fileName=file.getName();//保存文件名
		String content="";//用于保存读取的内容
		int c;//用于保存单个字符
		
		if(fileName.endsWith(".txt")){
			/*
			 * 读取TXT文件并保存到String中
			 */
			//FileReader fr=new FileReader(fileName);  
			Reader reader=new InputStreamReader(new FileInputStream(file));
	        while((c=reader.read())!=-1){  
	            content+=(char)c;  
	        }
	        reader.close();
		}else if(fileName.endsWith(".docx")){
			/*
			 * 读取DOCX文件并保存到String中
			 */
			FileInputStream fis=new FileInputStream(file);
			org.apache.poi.hwpf.extractor.WordExtractor doc=new WordExtractor(fis);//提取DOCX中的内容
			content=doc.getText();//保存DOCX中的内容
			fis.close();
		}else if(fileName.endsWith(".pdf")){
			/*
			 * 读取PDF文件并保存到String中
			 */		
			try{
				PDDocument d=null;
				d=PDDocument.load(file);
				PDFTextStripper stripper=new PDFTextStripper();//用语提取PDF中的内容
				int page=d.getNumberOfPages();
				stripper.setSortByPosition(true);
				stripper.setStartPage(1);
				stripper.setEndPage(page);
				content=stripper.getText(d);//保存PDF中的内容	
				d.close();
			}catch(Exception e){
				e.printStackTrace();
			}
		}else{
			System.out.println("系统不支持该文件类型！");
		}
		String transContent=content.toLowerCase();//将文件中所有字母转化成小写字母
		return transContent;
	}
}
