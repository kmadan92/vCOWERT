package Utilities;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.BindException;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.time.Duration;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

import org.apache.commons.collections4.map.HashedMap;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell ;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFHyperlink;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.asserts.SoftAssert;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;

//import jxl.Cell;
import jxl.Sheet;
import jxl.Workbook;
import jxl.read.biff.BiffException;



public class Global_Utilities {
	
	public static String pathInsideProject;
	public static ThreadLocal<String> threadDataSheetName  = new ThreadLocal<String>();
	public static ThreadLocal<WebDriver> driver = new ThreadLocal<WebDriver>();
	public static ThreadLocal<WebDriverWait> wait = new ThreadLocal<WebDriverWait>();
	public static String Key,Value,FunctionName, Environment;
	public static ThreadLocal<String> BrowserName  = new ThreadLocal<String>();
	public static ThreadLocal<String> URL  = new ThreadLocal<String>();
//	public static ThreadLocal<String> downloadFilepath  = new ThreadLocal<String>();
	public static SoftAssert softAssertion = new SoftAssert();
	public static String className,methodName;
	public static ThreadLocal<Class> thisClass = new ThreadLocal<Class>();
//	public static Screen s = new Screen();
//	public static Pattern p = new Pattern();
//	public static Region r;
	public static ThreadLocal<ArrayList<String>> tabs2  = new ThreadLocal<ArrayList<String>>();
	public static Process process;
	public static String autodir = System.getProperty("user.dir")+"\\resources\\AutoIT";
	public static String sikulidir = System.getProperty("user.dir")+"\\resources\\Sikuli";
	public static ThreadLocal<String> winHandleBefore =  new ThreadLocal<String>();
	public static ThreadLocal<String> FolderLocation  = new ThreadLocal<String>();
	public static ExtentReports extent = new ExtentReports();
	public static ThreadLocal<ExtentTest> logger  =  new ThreadLocal<ExtentTest>();
	public static int totalHTMLFiles=0;
	public static ThreadLocal<String> Username  = new ThreadLocal<String>();
	public static ThreadLocal<String> Password  = new ThreadLocal<String>();
	public static final String Browser_INCOGNITO = "Incognito Mode";
	public static final String Browser_NORMAL = "Normal Mode";
	public static final String Browser_DOWNLOAD_PREFERENCE = "Download Preference";
	public static Map<String, String> map = new LinkedHashMap<String, String>();
	
	public static Map<String, Object> data  = new HashMap<String, Object>();
	
	public static String Filename  = new String();
	public static String Testcase  = new String();
	
	public static String getPathCommon() throws URISyntaxException {
		pathInsideProject = new File("").getAbsolutePath();
		return pathInsideProject;
	}
	
	public static void setThreadDataSheetName(String DataSheetName) {
		threadDataSheetName.set(DataSheetName);
	}
	
	public static String getThreadDataSheetName() {
		return threadDataSheetName.get();
	}
	
	//function to get total row count
		public static int getRowCount(String sheetName) {
			int rowCount = 0;
			try {
				String FileName = getThreadDataSheetName();
				String path = getPathCommon();
				FileInputStream file = new FileInputStream(new File(path + "//Datafiles//" + FileName));

				@SuppressWarnings("resource")
				XSSFWorkbook workbook = new XSSFWorkbook(file);
				XSSFSheet sheet = workbook.getSheet(sheetName);
				rowCount = sheet.getLastRowNum() + 1;
			} catch (Exception E) {
				E.printStackTrace();
			}
			return rowCount;
		}

		//Function to get total Column count
	public static int getColCount(String sheetName) throws Exception {
			int colCount = 0;
			try {
				String FileName = getThreadDataSheetName();
				String path = getPathCommon();
				FileInputStream file = new FileInputStream(new File(path + "//Datasheets//" + FileName));

				@SuppressWarnings("resource")
				XSSFWorkbook workbook = new XSSFWorkbook(file);
				XSSFSheet sheet = workbook.getSheet(sheetName);
				colCount = sheet.getRow(0).getLastCellNum();
			} catch (Exception E) {
				E.printStackTrace();
			}
			return colCount;
		}
	
	 public static String getParameterFromInputSheet(String sheetName, String parameter, int rowNum, int headerrow) {
		String value = null;
		
		String FileName = getThreadDataSheetName();
		try {
			String path = getPathCommon();
			ThreadLocal<FileInputStream> file = new ThreadLocal<FileInputStream>();
			file.set( new FileInputStream(new File(path + "\\Datafiles\\" + FileName)));

			ThreadLocal<XSSFWorkbook> workbook = new ThreadLocal<XSSFWorkbook>();
			workbook.set(new XSSFWorkbook(file.get()));
			ThreadLocal<XSSFSheet> sheet = new ThreadLocal<XSSFSheet>();
					sheet.set(workbook.get().getSheet(sheetName));
					
			int paramCol = -1;
			Iterator<Cell> cellIterator = sheet.get().getRow(headerrow).cellIterator();
			while (cellIterator.hasNext()) {
				Cell cell = (Cell) cellIterator.next();
				try {
					if (cell.getStringCellValue().equals(parameter))
						paramCol = cell.getColumnIndex();
				} catch (Exception e) {
				}
			}
			try {
				value = sheet.get().getRow(rowNum).getCell(paramCol).getStringCellValue();
			} catch (Exception e) {
			}
			file.get().close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			System.out.println("Please verify the Data sheet, and the path where it is saved are correct");
		} catch (IOException e) {
			e.printStackTrace();
		} catch (URISyntaxException e1) {
			e1.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return value;
	}
	
	public static void SetParameterFromInputSheet(String sheetName, String parameter, int rowNum, int headerrow,
			String Value) {
		{
			try {
				String FileName = getThreadDataSheetName();

				String path = getPathCommon();
				ThreadLocal<FileInputStream> file = new ThreadLocal<FileInputStream>();
				file.set( new FileInputStream(new File(path + "\\Datafiles\\" + FileName)));

				ThreadLocal<XSSFWorkbook> workbook = new ThreadLocal<XSSFWorkbook>();
				workbook.set(new XSSFWorkbook(file.get()));
				ThreadLocal<XSSFSheet> sheet = new ThreadLocal<XSSFSheet>();
				sheet.set(workbook.get().getSheet(sheetName));

				int paramCol = -1;
				Iterator<Cell> cellIterator = sheet.get().getRow(headerrow).cellIterator();
				while (cellIterator.hasNext()) {
					Cell cell = (Cell) cellIterator.next();
					try {
						if (cell.getStringCellValue().equals(parameter))
							paramCol = cell.getColumnIndex();
					} catch (Exception e) {
					}
				}
				try {
					ThreadLocal<XSSFRow> row1 = new ThreadLocal<XSSFRow>();
					row1.set(sheet.get().getRow(rowNum));
					ThreadLocal<XSSFCell> cellA1 = new ThreadLocal<XSSFCell>();
					cellA1.set(row1.get().createCell(paramCol));
					cellA1.get().setCellValue(Value);
				} catch (Exception e) {
				}
				FileOutputStream out = new FileOutputStream(new File(path + "//Datafiles//" + FileName));

				workbook.get().write(out);
				out.close();
			} catch (FileNotFoundException e) {
				e.printStackTrace();
				System.out.println("Please verify the Data sheet, and the path where it is saved are correct");
			} catch (IOException e) {
				e.printStackTrace();
			} catch (URISyntaxException e1) {
				e1.printStackTrace();
			}
		}
	}
	
	public static double getParameterFromFile(String sheetName, String FolderName, String parameter, int rowNum, int headerrow) {
		double value = 0;
		
		String FileName = getThreadDataSheetName();
		try {
			String path = getPathCommon();
			ThreadLocal<FileInputStream> file = new ThreadLocal<FileInputStream>();
			file.set( new FileInputStream(new File(path + "//"+FolderName+"//" + FileName)));

			ThreadLocal<XSSFWorkbook> workbook = new ThreadLocal<XSSFWorkbook>();
			workbook.set(new XSSFWorkbook(file.get()));
			ThreadLocal<XSSFSheet> sheet = new ThreadLocal<XSSFSheet>();
			sheet.set(workbook.get().getSheet(sheetName));
			
			
			int paramCol = -1;
			Iterator<Cell> cellIterator = sheet.get().getRow(headerrow).cellIterator();
			while (cellIterator.hasNext()) {
				Cell cell = (Cell) cellIterator.next();
				try {
					if (cell.getStringCellValue().equals(parameter))
						paramCol = cell.getColumnIndex();
				} catch (Exception e) {
				}
			}
			try {
				value = sheet.get().getRow(rowNum).getCell(paramCol).getNumericCellValue();
			} catch (Exception e) {
			}
			file.get().close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			System.out.println("Please verify the Data sheet, and the path where it is saved are correct");
		} catch (IOException e) {
			e.printStackTrace();
		} catch (URISyntaxException e1) {
			e1.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return value;
	}
	
	public static XSSFHyperlink getHyperlinkFromCell(String sheetName, String parameter, String path, int rowNum, int headerrow, int Pathrow, int Pathreference) {
		XSSFHyperlink value = null;
		
		
		try {
			String FileName = getThreadDataSheetName();
			
			ThreadLocal<FileInputStream> file = new ThreadLocal<FileInputStream>();
			file.set( new FileInputStream(new File(path+"\\" + FileName)));

			ThreadLocal<XSSFWorkbook> workbook = new ThreadLocal<XSSFWorkbook>();
			workbook.set(new XSSFWorkbook(file.get()));
			ThreadLocal<XSSFSheet> sheet = new ThreadLocal<XSSFSheet>();
			sheet.set(workbook.get().getSheet(sheetName));
			
			
			int paramCol = -1;
			Iterator<Cell> cellIterator = sheet.get().getRow(headerrow).cellIterator();
			while (cellIterator.hasNext()) {
				Cell cell = (Cell) cellIterator.next();
				try {
					if (cell.getStringCellValue().equals(parameter))
						paramCol = cell.getColumnIndex();
				} catch (Exception e) {
				}
			}
			try {
				value = sheet.get().getRow(rowNum).getCell(paramCol).getHyperlink();
			} catch (Exception e) {
			}
			file.get().close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			System.out.println("Please verify the Data sheet, and the path where it is saved are correct");
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return value;
	}
	
	
	
	@SuppressWarnings("resource")
	 public static String getBrowserFromEnvConfig(String sheetName, String parameter, int rowNum, int headerrow) {
		String value = null;
		
		String FileName = getThreadDataSheetName();
		try {
			String path = getPathCommon();
			ThreadLocal<FileInputStream> file = new ThreadLocal<FileInputStream>();
			file.set( new FileInputStream(new File(path + "//Config//" + FileName)));

			ThreadLocal<HSSFWorkbook> workbook = new ThreadLocal<HSSFWorkbook>();
			workbook.set(new HSSFWorkbook(file.get()));
			ThreadLocal<HSSFSheet> sheet = new ThreadLocal<HSSFSheet>();
			sheet.set(workbook.get().getSheet(sheetName));
		
			int paramCol = -1;
			Iterator<Cell> cellIterator = sheet.get().getRow(headerrow).cellIterator();
			while (cellIterator.hasNext()) {
				Cell cell = (Cell) cellIterator.next();
				try {
					if (cell.getStringCellValue().equals(parameter))
						paramCol = cell.getColumnIndex();
				} catch (Exception e) {
				}
			}
			try {
				value = sheet.get().getRow(rowNum).getCell(paramCol).getStringCellValue();
			} catch (Exception e) {
			}
			file.get().close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			System.out.println("Please verify the Data sheet, and the path where it is saved are correct");
		} catch (IOException e) {
			e.printStackTrace();
		} catch (URISyntaxException e1) {
			e1.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return value;
	}
	
	 public Map<String, String> readExecutor(String sheetName, String pathName, String fileName) throws Exception 
		{
			int R,C;
			Map<String,String> sheetData=new HashMap<String,String>();
			final String dir = System.getProperty("user.dir");
			File inputFile=new File(dir+"//" + pathName + "//"+ fileName);
			Workbook wb;
			try
			{
			wb=Workbook.getWorkbook(inputFile);

			Sheet sheet=wb.getSheet(sheetName);
			
			C=sheet.getColumns();
			R=sheet.getRows();
			sheetData.put("DataCount",Integer.toString(R));
			
		for(int j=1;j<R;j++)
		{
		 for(int i =0; i<C;i++)
		 {
			//(Column,Row)
				jxl.Cell cell=sheet.getCell(i,0);
				jxl.Cell cell_2=sheet.getCell(i,j);
				
				Key=cell.getContents();
				Value=cell_2.getContents();
				
				sheetData.put(Key, Value);
							
				
				if (Key.trim().equals("Execute"))
				{
					if (Value.trim().equals("Yes"))
					{
						BrowserName.set(sheetData.get("Browser"));
						className = sheetData.get("Class");
						FunctionName = sheetData.get("Function");
						
						System.out.println("---------------Scenario "+FunctionName+" started---------------");
						
						driver.set(BrowserConfig.getBrowser(BrowserName.get()));
						wait.set(new WebDriverWait(driver.get(), Duration.ofMinutes(60)));	
						
						
						Class<?> cls = Class.forName("Flows."+className);
						Object obj = cls.newInstance();
						java.lang.reflect.Method Meth =  obj.getClass().getMethod(FunctionName);
						Meth.invoke(obj);
						
					}
					
				}
				
				cell=null;
				cell_2=null;
		 }
		}
		
		 inputFile=null;
		 wb=null;
		 sheet=null;
			}
			catch(BindException e)
			{
				e.printStackTrace();
			}
			catch(IOException e)
			{
				e.printStackTrace();
			}
			
		 
			return sheetData;
		 
		}
	 
	 
	 @SuppressWarnings("rawtypes")
	public static Map readConfig() throws BiffException
		{
			String dir = System.getProperty("user.dir");
			Map<String,String> configData=new HashMap<String,String>();
			File inputFile=new File(dir+"\\" + "Config\\" + "Environment.xls");
			Workbook wb;
			String sheetName= "Environment";
			int C=0, R=0;
			String hKey=null, hValue=null;
			
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

					configData.put(hKey, hValue);
										
					if (hKey.trim().equals("ExecutionFlag"))
					{
						if (hValue.equals("Yes"))
						{
							Environment = configData.get("Environment");
						    URL.set(configData.get("URL"));
								   						
						}
											
					}
									
				}	
			        							
				}
				
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
			return configData;
		}

}
