package Flows;


import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.time.Duration;

import javax.imageio.ImageIO;

import org.openqa.selenium.By;
import org.openqa.selenium.Point;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.ITestContext;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.github.romankh3.image.comparison.ImageComparison;
import com.github.romankh3.image.comparison.ImageComparisonUtil;
import com.github.romankh3.image.comparison.model.ImageComparisonResult;
import com.github.romankh3.image.comparison.model.ImageComparisonState;

import Utilities.BrowserConfig;
import Utilities.Global_Utilities;
import ru.yandex.qatools.ashot.AShot;
import ru.yandex.qatools.ashot.Screenshot;
import ru.yandex.qatools.ashot.shooting.ShootingStrategies;

public class Outputs_HTMLCompare extends Global_Utilities {
	
	
	
	public void getElementscreenshot(String Foldername, String name, WebElement element) throws IOException  
    {
			Screenshot screenshot = new AShot().shootingStrategy(ShootingStrategies.viewportPasting(1000)).takeScreenshot(driver.get());	
			BufferedImage img = screenshot.getImage();
//		
			// Get the location of element on the page
			Point point = element.getLocation();

			// Get width and height of the element
			int eleWidth = element.getSize().getWidth();
			int eleHeight = element.getSize().getHeight();
			
			BufferedImage img1 = img.getSubimage(point.getX(),point.getY(),eleWidth,eleHeight);
            String dir = System.getProperty("user.dir");
            String filename= name +".jpg";
            File ScreenshotFolder=  new File(dir+"\\Comparison\\HTML_Image_Compare\\"+Foldername+"\\"+name+".jpg");
            
            try
            {
            	if (ScreenshotFolder.exists())
            	{
            			ImageIO.write(img1, "jpg", ScreenshotFolder);
            			System.out.println(filename+" saved at "+ScreenshotFolder);
            		
            	}
            	else
            	{
            		ScreenshotFolder.mkdir();
            		
            		ImageIO.write(img1, "jpg", ScreenshotFolder);
            		System.out.println(filename+" saved at "+ScreenshotFolder);
            	}
            }
            catch (Exception e)
            {
            	System.out.println("getscreenshot Failed");
            	e.printStackTrace();
            }	
            
    }

	
	public void OpenURL(String URL)  
    {
		try
		{
		driver.get().get(URL);
		driver.get().manage().window().maximize();
		System.out.println("Opened URL: "+URL);
		}
		catch (Exception e)
		{
			System.out.println("--OpenURL Failed tring to open "+URL);
			e.printStackTrace();
		}
    }
	

	public void ImageComparator(String FolderTest, String FolderProd, String ResultFolder, String sheet) throws IOException {
		
		
		System.out.println("--Running comparison on "+loopCounter+" files--");
		
		
		for(int i=1;i<=loopCounter;i++)
		{
			String Test = FolderTest+"\\"+getParameterFromInputSheet(sheet,"GUID",i,0)+".jpg";
			String Prod = FolderProd+"\\"+getParameterFromInputSheet(sheet,"GUID",i,0)+".jpg";
			
					// load the images to be compared
	               BufferedImage ImageTest = ImageComparisonUtil.readImageFromResources(Test);
	               BufferedImage ImageProd = ImageComparisonUtil.readImageFromResources(Prod);
	       
	               // where to save the result (leave null if you want to see the result in the UI)
	               File resultDestination = new File( ResultFolder );
	               
	               //Compare image size
	               if(ImageProd.getWidth() != ImageTest.getWidth() || ImageProd.getHeight() != ImageTest.getHeight())
	         
	               {   
	            	   System.out.println("Size Mismatch for: "+getParameterFromInputSheet(sheet,"GUID",i,0));
		               System.out.println("--Size of Image on Test--");
		               System.out.println(ImageTest.getWidth()+","+ImageTest.getHeight());
		               System.out.println("--Size of Image on Reference--");
		               System.out.println(ImageProd.getWidth()+","+ImageProd.getHeight());
		               
		               System.out.println("---Resizing Images---");
	            	   //Make size of both images same
	            	   ImageProd = ImageComparisonUtil.resize(ImageProd, ImageTest.getWidth(), ImageTest.getHeight());
		               
		               System.out.println("--After Image Resize--");
		               System.out.println("--Size of Image on Test--");
		               System.out.println(ImageTest.getWidth()+","+ImageTest.getHeight());
		               System.out.println("--Size of Image on Reference--");
		               System.out.println(ImageProd.getWidth()+","+ImageProd.getHeight());
		               
	            	   
	               }
	               
	              
	               
	               //Create ImageComparison object for it.
	               ImageComparison imageComparison = new ImageComparison( ImageTest, ImageProd );
	      
	               //Threshold - it's the max distance between non-equal pixels. By default it's 5.
	               imageComparison.setThreshold(15);
	               imageComparison.getThreshold();
	       
	               //RectangleListWidth - Width of the line that is drawn in the rectangle. By default it's 1.
	               imageComparison.setRectangleLineWidth(2);
	               imageComparison.getRectangleLineWidth();
	       
	               //DifferenceRectangleFilling - Fill the inside the difference rectangles with a transparent fill. By default it's false and 20.0% opacity.
	               imageComparison.setDifferenceRectangleFilling(true, 25.0);
	               imageComparison.isFillDifferenceRectangles();
	               imageComparison.getPercentOpacityDifferenceRectangles();
	       
	               //ExcludedRectangleFilling - Fill the inside the excluded rectangles with a transparent fill. By default it's false and 20.0% opacity.
	               imageComparison.setExcludedRectangleFilling(true, 25.0);
	               imageComparison.isFillExcludedRectangles();
	               imageComparison.getPercentOpacityExcludedRectangles();
	       
	               /*
	               //Destination. Before comparing also can be added destination file for result image.
	               imageComparison.setDestination(resultDestination);
	               imageComparison.getDestination();
	       
	               //MaximalRectangleCount - It means that would get first x biggest rectangles for drawing.
	               // by default all the rectangles would be drawn.
	               imageComparison.setMaximalRectangleCount(10);
	               imageComparison.getMaximalRectangleCount();
	       
	               //MinimalRectangleSize - The number of the minimal rectangle size. Count as (width x height).
	               // by default it's 1.
	               imageComparison.setMinimalRectangleSize(100);
	               imageComparison.getMinimalRectangleSize();
	                */
	               //Change the level of the pixel tolerance:
	               imageComparison.setPixelToleranceLevel(0.2);
	               imageComparison.getPixelToleranceLevel();
	       
	               //After configuring the ImageComparison object, can be executed compare() method:
	               ImageComparisonResult imageComparisonResult = imageComparison.compareImages();
	       
	               //Can be found ComparisonState.
	               ImageComparisonState imageComparisonState = imageComparisonResult.getImageComparisonState();
	               System.out.println(imageComparisonState+" for "+getParameterFromInputSheet(sheet,"GUID",i,0) );
	              
	               if(!(imageComparisonState.toString().equalsIgnoreCase("MATCH")))
	               {
	               
	               //And Result Image
	               BufferedImage resultImage = imageComparisonResult.getResult();
	       
				//Image can be saved after comparison, using ImageComparisonUtil.
	               ImageComparisonUtil.saveImage(new File(resultDestination+"\\"+getParameterFromInputSheet(sheet,"GUID",i,0)+".jpg"), resultImage);
//	               ImageIO.write(resultImage, "jpg", new File(resultDestination+"\\"+getParameterFromInputSheet("HTML","GUID",i,0)+".jpg"));
	               }     
	    }
	}
	
	
	@BeforeMethod
	public void InitializeTest(ITestContext context) throws MalformedURLException
	{
		setThreadDataSheetName("Environment.xls");
		BrowserName.set(getBrowserFromEnvConfig("Environment", "Browser", 2, 0)); 
		driver.set(BrowserConfig.getBrowser(Browser_INCOGNITO));
		wait.set(new WebDriverWait(driver.get(), Duration.ofSeconds(60)));
		className = this.getClass().getName();
		
	}
	
	@Test(priority=0)
	public void OpenURLandTakeScreenshotOnTest() throws InterruptedException, IOException
	{
		System.out.println("--OpenURLandTakeScreenshotOnTest test Started--");
		setThreadDataSheetName("HTMLCompare.xlsx");
		
		for(int i=1;i<=loopCounter;i++)
		{
			String guid = getParameterFromInputSheet("HTML","GUID",i,0);
			OpenURL(getParameterFromInputSheet("HTML","URLTEST",i,0));
			Thread.sleep(3500);
			WebElement ele = driver.get().findElement(By.xpath("//div[contains(@class,'rhs-center')]"));
			getElementscreenshot("Test", guid, ele);
			
		}
		driver.get().close();
		
		System.out.println("--OpenURLandTakeScreenshotOnTest test Passed--");
	}
	
	@Test(priority=0)
	public void OpenURLandTakeScreenshotOnProd() throws InterruptedException, IOException
	{
		System.out.println("--OpenURLandTakeScreenshotOnProd test Started--");
		
		setThreadDataSheetName("HTMLCompare.xlsx");
		
		for(int i=1;i<=loopCounter;i++)
		{
			String guid = getParameterFromInputSheet("HTML","GUID",i,0);
			OpenURL(getParameterFromInputSheet("HTML","URLPROD",i,0));
			Thread.sleep(3500);
			WebElement ele = wait.get().until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[contains(@class,'rhs-center')]")));
			getElementscreenshot("Prod", guid, ele);
			
		}
		
		driver.get().close();
		
		System.out.println("--OpenURLandTakeScreenshotOnProd test Passed--");
	}
	
	@Test(priority=0)
	public void MD_OpenURLandTakeScreenshotOnTest() throws InterruptedException, IOException
	{
		System.out.println("--MD_OpenURLandTakeScreenshotOnTest test Started--");
		setThreadDataSheetName("HTMLCompare.xlsx");
		
		for(int i=1;i<=loopCounter;i++)
		{
			String guid = getParameterFromInputSheet("MD","GUID",i,0);
			OpenURL(getParameterFromInputSheet("MD","URLTEST",i,0));
			Thread.sleep(3500);
			WebElement ele = driver.get().findElement(By.xpath("//div[contains(@class,'rhs-center')]"));
			getElementscreenshot("Test", guid, ele);
			
		}
		driver.get().close();
		
		System.out.println("--MD_OpenURLandTakeScreenshotOnTest test Passed--");
	}
	
	@Test(priority=0)
	public void MD_OpenURLandTakeScreenshotOnProd() throws InterruptedException, IOException
	{
		System.out.println("--MD_OpenURLandTakeScreenshotOnProd test Started--");
		
		setThreadDataSheetName("HTMLCompare.xlsx");
		
		for(int i=1;i<=loopCounter;i++)
		{
			String guid = getParameterFromInputSheet("MD","GUID",i,0);
			OpenURL(getParameterFromInputSheet("MD","URLPROD",i,0));
			Thread.sleep(3500);
			WebElement ele = wait.get().until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[contains(@class,'rhs-center')]")));
			getElementscreenshot("Prod", guid, ele);
			
		}
		
		driver.get().close();
		
		System.out.println("--MD_OpenURLandTakeScreenshotOnProd test Passed--");
	}
	
	@Test(priority=0)
	public void RNA_OpenURLandTakeScreenshotOnTest() throws InterruptedException, IOException
	{
		System.out.println("--RNA_OpenURLandTakeScreenshotOnTest test Started--");
		setThreadDataSheetName("HTMLCompare.xlsx");
		
		for(int i=1;i<=loopCounter;i++)
		{
			String guid = getParameterFromInputSheet("RNA","GUID",i,0);
			OpenURL(getParameterFromInputSheet("RNA","URLTEST",i,0));
			Thread.sleep(3500);
			WebElement ele = driver.get().findElement(By.xpath("//div[contains(@class,'rhs-center')]"));
			getElementscreenshot("Test", guid, ele);
			
		}
		driver.get().close();
		
		System.out.println("--RNA_OpenURLandTakeScreenshotOnTest test Passed--");
	}
	
	@Test(priority=0)
	public void RNA_OpenURLandTakeScreenshotOnProd() throws InterruptedException, IOException
	{
		System.out.println("--RNA_OpenURLandTakeScreenshotOnProd test Started--");
		
		setThreadDataSheetName("HTMLCompare.xlsx");
		
		for(int i=1;i<=loopCounter;i++)
		{
			String guid = getParameterFromInputSheet("RNA","GUID",i,0);
			OpenURL(getParameterFromInputSheet("RNA","URLPROD",i,0));
			Thread.sleep(3500);
			WebElement ele = wait.get().until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[contains(@class,'rhs-center')]")));
			getElementscreenshot("Prod", guid, ele);
			
		}
		
		driver.get().close();
		
		System.out.println("--RNA_OpenURLandTakeScreenshotOnProd test Passed--");
	}
	
	@Test(priority=1)
	public void CompareImages(ITestContext context) throws InterruptedException, IOException
	{
		System.out.println("--CompareImages test Started--");
		
		setThreadDataSheetName("HTMLCompare.xlsx");
		
		String suiteName = context.getCurrentXmlTest().getSuite().getName();
		System.out.println("Suite Name is: "+suiteName);
		
		if(suiteName.contains("DITA"))
		{
		System.out.println("--Started Comparing DITA Topics--");
		ImageComparator(System.getProperty("user.dir")+"\\Comparison\\HTML_Image_Compare\\Test", System.getProperty("user.dir")+"\\Comparison\\HTML_Image_Compare\\Prod", System.getProperty("user.dir")+"\\Comparison\\HTML_Image_Compare\\Results", "HTML");
		}
		else if(suiteName.contains("MD"))
		{
			System.out.println("--Started Comparing MD Topics--");
			ImageComparator(System.getProperty("user.dir")+"\\Comparison\\HTML_Image_Compare\\Test", System.getProperty("user.dir")+"\\Comparison\\HTML_Image_Compare\\Prod", System.getProperty("user.dir")+"\\Comparison\\HTML_Image_Compare\\Results", "MD");	
		}
		else if(suiteName.contains("RNA"))
		{
			System.out.println("--Started Comparing RN--");
			ImageComparator(System.getProperty("user.dir")+"\\Comparison\\HTML_Image_Compare\\Test", System.getProperty("user.dir")+"\\Comparison\\HTML_Image_Compare\\Prod", System.getProperty("user.dir")+"\\Comparison\\HTML_Image_Compare\\Results", "RNA");	
		}
		
		driver.get().close();
		
		System.out.println("--CompareImages test Passed--");
	}
}
