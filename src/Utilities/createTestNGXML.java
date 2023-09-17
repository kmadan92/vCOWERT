package Utilities;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.BindException;
import java.util.HashMap;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.testng.xml.XmlSuite;
import org.testng.xml.XmlTest;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import jxl.Sheet;
import jxl.Workbook;
import jxl.read.biff.BiffException;

public class createTestNGXML {
	
	/**
	 * @param args
	 * @throws ParserConfigurationException
	 * @throws BiffException
	 * @throws TransformerException
	 */
	
	public static String tempClass= null;
	
	public static void main(String args[]) throws ParserConfigurationException, BiffException, TransformerException
	{
		
		String dir = System.getProperty("user.dir");
		File inputFile=new File(dir+"\\" + "Config\\" + "Executor.xls");
		Map<String,String> sheetData=new HashMap<String,String>();
		Map<Integer,String> ClassMap=new HashMap<Integer,String>();
		Map<Integer,String> FunctionMap=new HashMap<Integer,String>();
		int z=1;
		
		Workbook wb;
		String sheetName= "Executor";
		int C=0, R=0;
		String hKey=null, hValue=null;
		
		DocumentBuilderFactory documentbuilderfactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder documentbuilder = documentbuilderfactory.newDocumentBuilder();
		Document document = documentbuilder.newDocument();
		
		Element rootElementListener = document.createElement("listeners");
		Element childElementListener = document.createElement("listener");
		childElementListener.setAttribute("class-name", "Utilities.ExtentListener");
		rootElementListener.appendChild(childElementListener);
		
		Element rootElementSuite = document.createElement("suite");
		rootElementSuite.setAttribute("name", "RegressionSuite");
		rootElementSuite.appendChild(rootElementListener);
		Element rootElementTest = document.createElement("test");
		rootElementTest.setAttribute("name", "RegressionTest");
//		rootElementTest.setAttribute("thread-count", "1");
		Element rootElementClass = document.createElement("classes");
		Element childElementClass = null ;
		Element rootElementGroups = null;
		Element rootInclude = null;
		
		try
		{
			wb=Workbook.getWorkbook(inputFile);
			Sheet sheet=wb.getSheet(sheetName);
			C=sheet.getColumns();
			R=sheet.getRows();
			
			jxl.Cell cell;
			jxl.Cell  cell_2;
			
			for (int j=1; j<R; j++)
			{
			for(int i=0;i<C;i++)
			{
				//(Column,Row)
			 cell=sheet.getCell(i,0);
			 cell_2=sheet.getCell(i,j);
				hKey=cell.getContents();
				hValue=cell_2.getContents();
				
				sheetData.put(hKey, hValue);
									
				if (hKey.trim().equals("Execute"))
				{
					if (hValue.equals("Yes"))
					{
						
						ClassMap.put(z, "Flows."+sheetData.get("Class"));
						FunctionMap.put(z, sheetData.get("Function"));
						z++;
						
					}
				}													
			}	
		        							
			}
			
			int ClassSize = ClassMap.size();
			
			System.out.println(ClassMap);
			System.out.println(FunctionMap);
			
			
			
			for (int m = 0;m<ClassSize;m++)
			{
				if(tempClass == null)
				{
					childElementClass = document.createElement("class");
					childElementClass.setAttribute("name", ClassMap.get(m+1));
					
					rootElementGroups = document.createElement("methods");
					
					rootInclude =  document.createElement("include");
					rootInclude.setAttribute("name", FunctionMap.get(m+1));
					
					tempClass = childElementClass.getAttribute("name");
				}
				
				else if(tempClass.equalsIgnoreCase(ClassMap.get(m+1)))
				{
					rootInclude =  document.createElement("include");
					rootInclude.setAttribute("name", FunctionMap.get(m+1));
				}
				
				else
				{
					childElementClass = document.createElement("class");
					childElementClass.setAttribute("name", ClassMap.get(m+1));
					
					rootElementGroups = document.createElement("methods");
					
					rootInclude =  document.createElement("include");
					rootInclude.setAttribute("name", FunctionMap.get(m+1));
					
					tempClass = childElementClass.getAttribute("name");
				}

				
				rootElementSuite.appendChild(rootElementTest);
				rootElementTest.appendChild(rootElementClass);
				rootElementClass.appendChild(childElementClass);
				childElementClass.appendChild(rootElementGroups);
				rootElementGroups.appendChild(rootInclude);
				
			
			}
			
			document.appendChild(rootElementSuite);
			
			FileWriter fstream = new FileWriter(dir+"\\testng.xml");
			BufferedWriter out = new BufferedWriter(fstream);
			
			TransformerFactory transformerFactory = TransformerFactory.newInstance();
			Transformer transformer = transformerFactory.newTransformer();
			DOMSource source = new DOMSource(document);
			
			StreamResult result = new StreamResult(fstream);
			transformer.transform(source, result);
			
			out.close();
			
			
			cell=null;
			cell_2=null;		
			inputFile=null;
			wb=null;
			sheet=null;
				
		} 
	
		catch (BindException e) 
		{
			e.printStackTrace();
		} 
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}

}
