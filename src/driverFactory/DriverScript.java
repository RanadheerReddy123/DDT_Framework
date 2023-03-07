package driverFactory;

import java.io.File;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.testng.Reporter;
import org.testng.annotations.Test;

import com.relevantcodes.extentreports.ExtentReports;
import com.relevantcodes.extentreports.ExtentTest;
import com.relevantcodes.extentreports.LogStatus;

import commonFunctions.FunctionLibrary;
import config.AppUtil;
import utilities.ExcelFileUtil;

public class DriverScript extends AppUtil {
String inputpath="C:\\RR\\eclipse-workspace\\DDT_Framework\\DataTables\\LoginData.xlsx";
String outputpath ="C:\\RR\\eclipse-workspace\\DDT_Framework\\TestResults\\DataDrivenResults.xlsx";
ExtentReports report;
ExtentTest test;
@Test
public void Login()throws Throwable
{
	report = new ExtentReports("./Report/DataDriven.html");
	//create object for excel file util
	ExcelFileUtil xl = new ExcelFileUtil(inputpath);
	//count no of rows
	int rc =xl.rowCount("Login");
	Reporter.log("No of rows are::"+rc,true);
	for(int i=1;i<=rc;i++)
	{
		test=report.startTest("Validate Login");
		String user =xl.getCellData("Login", i, 0);
		String pass =xl.getCellData("Login", i, 1);
		//call login method
		boolean res =FunctionLibrary.verifyLogin(user, pass);
		if(res)
		{
			//write as login success into result cell
			xl.setCellData("Login", i, 2, "Login Success", outputpath);
			xl.setCellData("Login", i, 3, "Pass", outputpath);
			test.log(LogStatus.PASS, "Login Success");
		}
		else
		{
			File screen =((TakesScreenshot)driver).getScreenshotAs(OutputType.FILE);
			FileUtils.copyFile(screen, new File("./Screens/Iteration/"+i+"Loginpage.png"));
			//write as login Fail into result cell
			xl.setCellData("Login", i, 2, "Login Fail", outputpath);
			xl.setCellData("Login", i, 3, "Fail", outputpath);
			test.log(LogStatus.FAIL, "Login Fail");
		}
		report.endTest(test);
		report.flush();
	}
}
}
