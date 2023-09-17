package test.java;

import org.testng.annotations.Test;

import Utilities.Global_Utilities;

public class MavenTest {
	
  @Test
  public void Test() throws Exception {
	  
	  Global_Utilities handle = new Global_Utilities();
	  handle.readExecutor("Executor", "Config", "Executor.xls");
  }
  
  
}
