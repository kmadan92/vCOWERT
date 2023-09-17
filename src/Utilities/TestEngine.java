package Utilities;

public class TestEngine{
	

	public static void main(String args[]) throws Exception
	{
		Global_Utilities handle = new Global_Utilities();
		handle.readExecutor("Executor", "Config", "Executor.xls");
	}

}
