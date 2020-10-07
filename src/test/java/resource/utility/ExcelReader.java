package resource.utility;

import com.aventstack.extentreports.ExtentTest;
import org.apache.poi.openxml4j.util.ZipSecureFile;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import resource.common.GlobalVariables;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

// Creating a class which we will be using for reading data from Excel sheet
public class ExcelReader extends Utility {

	// Excel file path
	public String path;
	// For reading data from Excel sheet
	public FileInputStream fis = null;
	// For writing data to Excel sheet
	public FileOutputStream fileOut = null;
	// For .xlsx workbook
	private XSSFWorkbook workbook = null;
	// For .xlsx sheet in a workbook
	private XSSFSheet sheet = null;
	// For row in a sheet
	private XSSFRow row = null;
	// For column in a sheet
	private XSSFCell cell = null;

	//Constructor for ExcelReader class
	public ExcelReader(String path, ExtentTest logTestForTestBase) throws IOException {
		try {
			//Setting path for excel file
			this.path = path;

			//Open the excel file.
			fis = new FileInputStream(path);

			//Creating a workbook object
			ZipSecureFile.setMinInflateRatio(0);
			workbook = new XSSFWorkbook(fis);

			//Creating a sheet object and reading from first sheet
			sheet = workbook.getSheetAt(0);

			//Closing the excel file object
			fis.close();
		} catch(Exception e){
			log4j.error("ExcelReader method - ERROR - " + e);
			logException(logTestForTestBase, "ExcelReader method - ERROR", e);
		}
	}

	public ExcelReader(String path) throws IOException {
		try {
			//Setting path for excel file
			this.path = path;

			//Open the excel file.
			fis = new FileInputStream(path);

			//Creating a workbook object
			ZipSecureFile.setMinInflateRatio(0);
			workbook = new XSSFWorkbook(fis);

			//Creating a sheet object and reading from first sheet
			sheet = workbook.getSheetAt(0);

			//Closing the excel file object
			fis.close();
		} catch(Exception e){
			log4j.error("ExcelReader method - ERROR - " + e);
		}
	}

	public int getRowCount(String sheetName) {
		//index can be 0-N number based on how many sheets we have in an excel sheet.
		int index = workbook.getSheetIndex(sheetName);
		if (index == -1)
			return 0;
		else {
			sheet = workbook.getSheetAt(index);
			int totalNumberOfRows = sheet.getLastRowNum() + 1;
			return totalNumberOfRows;
		}
	}


	public String getCellData(String sheetName, String colName, int rowNum) throws IOException {
		try {
			if (rowNum <= 0)
				return "";

			int index = workbook.getSheetIndex(sheetName);
			int colNum = -1;

			if(index == -1)
				return "";

			sheet = workbook.getSheetAt(index);
			row = sheet.getRow(0);

			for(int i = 0; i < row.getLastCellNum(); i++) {
				if (row.getCell(i).getStringCellValue().trim().equals(colName.trim()))
					colNum = i;
			}

			if (colNum == -1)
				return "";

			sheet = workbook.getSheetAt(index);
			row = sheet.getRow(rowNum - 1);

			if (row == null)
				return "";
			cell = row.getCell(colNum);

			if (cell == null)
				return "";

			if(cell.getCellType() == Cell.CELL_TYPE_BLANK)
				return "";
			else {
				cell.setCellType(Cell.CELL_TYPE_STRING);
				return cell.getStringCellValue();
			}
		} catch (Exception e) {
			e.printStackTrace();
			//add into log
			return "row " + rowNum + " or column " + colName + " does not exist in xls";
		}
	}


	public String getCellData(String sheetName, int colNum, int rowNum) throws IOException {
		try {
			if (rowNum <= 0)
				return "";

			int index = workbook.getSheetIndex(sheetName);

			if (index == -1)
				return "";

			sheet = workbook.getSheetAt(index);
			row = sheet.getRow(rowNum - 1);

			if (row == null)
				return "";
			cell = row.getCell(colNum);

			if (cell == null)
				return "";
			//Find out if data type is String, then, directly return the data
			if(cell.getCellType() == Cell.CELL_TYPE_BLANK)
				return "";
			else {
				cell.setCellType(Cell.CELL_TYPE_STRING);
				return cell.getStringCellValue();
			}
		} catch (Exception e) {
			e.printStackTrace();
			//add into log
			return "row " + rowNum + " or column " + colNum + " does not exist  in xls";
		}
	}


	public boolean setCellData(String sheetName, String colName, int rowNum, String data) throws IOException {
		try {
			if (rowNum <= 0)
				return false;

			int index = workbook.getSheetIndex(sheetName);
			int colNum = -1;

			if (index == -1)
				return false;

			sheet = workbook.getSheetAt(index);
			row = sheet.getRow(0);

			for(int i = 0; i < row.getLastCellNum(); i++) {
				if (row.getCell(i).getStringCellValue().trim().equals(colName))
					colNum = i;
			}

			if (colNum == -1)
				return false;

			sheet.autoSizeColumn(colNum);
			row = sheet.getRow(rowNum - 1);

			if (row == null)
				row = sheet.createRow(rowNum - 1);

			cell = row.getCell(colNum);

			if (cell == null)
				cell = row.createCell(colNum);

			cell.setCellValue(data);

			fileOut = new FileOutputStream(path);
			workbook.write(fileOut);
			fileOut.close();
		} catch (Exception e) {
			e.printStackTrace();
			//add to log
			return false;
		}
		return true;
	}


	public boolean setCellData(String sheetName, int colNum, int rowNum, String data) throws IOException {
		try {
			if (rowNum <= 0 || colNum <0)
				return false;

			int index = workbook.getSheetIndex(sheetName);
			if (index == -1)
				return false;

			sheet = workbook.getSheetAt(index);
			row = sheet.getRow(0);
			sheet.autoSizeColumn(colNum);
			row = sheet.getRow(rowNum - 1);

			if (row == null)
				row = sheet.createRow(rowNum - 1);

			cell = row.getCell(colNum);
			if (cell == null)
				cell = row.createCell(colNum);

			cell.setCellValue(data);
			fileOut = new FileOutputStream(path);
			workbook.write(fileOut);
			fileOut.close();
		} catch (Exception e) {
			e.printStackTrace();
			//add to log
			return false;
		}
		return true;
	}


	public boolean addSheet(String sheetName) throws IOException {
		FileOutputStream fileOut;
		try {
			int index = workbook.getSheetIndex(sheetName);
			if(index == -1) {
				workbook.createSheet(sheetName);
				fileOut = new FileOutputStream(path);
				workbook.write(fileOut);
				fileOut.close();
			}
		} catch (Exception e) {
			e.printStackTrace();
			//add to log
			return false;
		}
		return true;
	}


	public boolean removeSheet(String sheetName) throws IOException {
		int index = workbook.getSheetIndex(sheetName);
		if (index == -1)
			return false;

		FileOutputStream fileOut;
		try {
			workbook.removeSheetAt(index);
			fileOut = new FileOutputStream(path);
			workbook.write(fileOut);
			fileOut.close();
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}


	public boolean addColumn(String sheetName, String colName) throws IOException {
		try {
			fis = new FileInputStream(path);
			ZipSecureFile.setMinInflateRatio(0);
			workbook = new XSSFWorkbook(fis);
			int index = workbook.getSheetIndex(sheetName);
			if (index == -1)
				return false;

			sheet = workbook.getSheetAt(index);
			row = sheet.getRow(0);

			if (row == null)
				row = sheet.createRow(0);

			if (row.getLastCellNum() == -1)
				cell = row.createCell(0);
			else
				cell = row.createCell(row.getLastCellNum());

			cell.setCellValue(colName);
			fileOut = new FileOutputStream(path);
			workbook.write(fileOut);
			fileOut.close();
		} catch (Exception e) {
			e.printStackTrace();
			//add to log
			return false;
		}
		return true;
	}


	public boolean removeColumn(String sheetName, int colNum) throws IOException {
		try {
			int index = workbook.getSheetIndex(sheetName);
			if (index == -1)
				return false;

			sheet = workbook.getSheet(sheetName);

			for (int i = 0; i < getRowCount(sheetName); i++) {
				row = sheet.getRow(i);
				if (row != null) {
					cell = row.getCell(colNum);
					if (cell != null) {
						row.removeCell(cell);
					}
				}
			}
			fileOut = new FileOutputStream(path);
			workbook.write(fileOut);
			fileOut.close();
		} catch (Exception e) {
			e.printStackTrace();
			//add to log
			return false;
		}
		return true;
	}


	public boolean isSheetExist(String sheetName) {
		int index = workbook.getSheetIndex(sheetName);
		if (index == -1) {
			index = workbook.getSheetIndex(sheetName.toUpperCase());
			return index != -1;
		} else
			return true;
	}

	/*
	 * This method gets number of columns in a sheet
	 * Input: Sheet Name
	 * Output: Number of columns
	 */
	public int getColumnCount(String sheetName) {
		if (!isSheetExist(sheetName))
			return -1;

		sheet = workbook.getSheet(sheetName);
		row = sheet.getRow(0);

		if (row == null)
			return -1;

		return row.getLastCellNum();
	}

	/*
	 * This method finds text in a column. It assumes that first column is a header.
	 * Input: Sheet Name, Column Name, Data to find in String format
	 * Output: returns -1 if it cannot find otherwise return row number.
	 */
	public int getCellRowNum(String sheetName, String colName, String cellValue) throws IOException {
		//Starting from row number 2 as assuming that we have a header at row#1
		for (int row = 2; row <= getRowCount(sheetName); row++) {
			if (getCellData(sheetName, colName, row).equalsIgnoreCase(cellValue)) {
				return row;
			}
		}
		return -1;
	}

	public static void setWarrantyInfoToExcelFile(String inputWarrantyOrder, String merchant, String headerName, ExtentTest logTest) throws IOException {

		try {

			log4j.debug("Set Warranty Order ID to excel file...start");

			/* Handle for Precondition tests of IDC*/
        	/* Get the test case ID only to compare*/
			String testName = GlobalVariables.TEST_NAME.split("_")[0];
			String excelSheet = GlobalVariables.TEST_DATA_SHEET;

			int testStartRowNum = 0;

			logInfo(logTest, "Set warranty order into excel file");
			// Find the row number from where test starts
			for (int rowNumber = 1; rowNumber <= excelReader.getRowCount(excelSheet); rowNumber++) {
				if (excelReader.getCellData(excelSheet, 0, rowNumber).contains(testName)) {
					testStartRowNum = rowNumber;
					break;
				}
			}

			// Column will start in the next row of test case
			int colStartRowNum = testStartRowNum + 1;

			//-----Find the column that contains header name----
			int totalCols = 0;
			while (!excelReader.getCellData(excelSheet, totalCols, colStartRowNum).equals("")) {
				totalCols++;
				if (excelReader.getCellData(excelSheet, totalCols, colStartRowNum).equals(headerName)) break;
			}
			int colWOHeader = totalCols;
			//--------------------------------------------------

			//------Find the column that contains merchant name----
			totalCols = 0;
			while (!excelReader.getCellData(excelSheet, totalCols, colStartRowNum).equals("")) {
				totalCols++;
				if (excelReader.getCellData(excelSheet, totalCols, colStartRowNum).equals("Retailer")) break;
			}
			int colMerchantHeader = totalCols;
			//--------------------------------------------------

			// Find out how many rows of data we have?
			// Data starts from 2nd row from the test1
			int dataStartRowNum = testStartRowNum + 2;
			int totalRows = 0;
			// Find all the rows till we get empty ("")
			while (!excelReader.getCellData(excelSheet, 0, dataStartRowNum + totalRows).equals("")) {
				totalRows++;
			}

			for (int rowNumber = dataStartRowNum; rowNumber < (dataStartRowNum + totalRows); rowNumber++) {
				if (excelReader.getCellData(excelSheet, colMerchantHeader, rowNumber).equals(merchant)) {
					excelReader.setCellData(excelSheet, colWOHeader, rowNumber, inputWarrantyOrder);
					break;
				}
			}

			log4j.debug("Set Warranty Order ID to excel file...end");
		}
		catch(Exception e){
			log4j.error("setWarrantyInfoToExcelFile - ERROR - " + e);
			logException(logTest, "setWarrantyInfoToExcelFile - ERROR", e);
		}
	}

	public static void setWarrantyIDToExcelFile(String inputWarrantyID, String testCaseID, String headerName, ExtentTest logTest) throws IOException {

		try {

			log4j.debug("Set Warranty ID to excel file basing testcase ID...start");

			String testName = testCaseID;
			String excelSheet = GlobalVariables.TEST_DATA_SHEET;

			int testStartRowNum = 0;

			logInfo(logTest, "Set Warranty ID to excel file basing testcase ID");
			// Find the row number from where test starts
			for (int rowNumber = 1; rowNumber <= excelReader.getRowCount(excelSheet); rowNumber++) {
				if (excelReader.getCellData(excelSheet, 0, rowNumber).equals(testName)) {
					testStartRowNum = rowNumber;
					break;
				}
			}

			// Column will start in the next row of test case
			int colStartRowNum = testStartRowNum + 1;

			//-----Find the column that contains header name----
			int totalCols = 0;
			while (!excelReader.getCellData(excelSheet, totalCols, colStartRowNum).equals("")) {
				totalCols++;
				if (excelReader.getCellData(excelSheet, totalCols, colStartRowNum).equals(headerName)) break;
			}

			// Find out how many rows of data we have?
			// Data starts from 2nd row from the test1
			int dataStartRowNum = testStartRowNum + 2;

			excelReader.setCellData(excelSheet, totalCols, dataStartRowNum, inputWarrantyID);

			log4j.debug("Set Warranty ID to excel file basing testcase ID...end");
		}
		catch(Exception e){
			log4j.error("setWarrantyIDToExcelFile - ERROR - " + e);
			logException(logTest, "setWarrantyIDToExcelFile - ERROR", e);
		}
	}

	public void deleteAllWarrantyOrderOnExcelFile(ExtentTest logTest) throws IOException {

		try {

			log4j.debug("Delete all values for WarrantyOrder column on excel file...starts");

			String testName = GlobalVariables.TEST_NAME;
			String headerName = "WarrantyOrder";
			String excelSheet = GlobalVariables.TEST_DATA_SHEET;

			int testStartRowNum = 0;

			// Find the row number from where test starts
			for (int rowNumber = 1; rowNumber <= excelReader.getRowCount(excelSheet); rowNumber++) {
				if (excelReader.getCellData(excelSheet, 0, rowNumber).equals(testName)) {
					testStartRowNum = rowNumber;
					break;
				}
			}

			// Column will start in the next row of test case
			int colStartRowNum = testStartRowNum + 1;
			int totalCols = 0;
			// Find the column that contains "WarrantyOrder" header
			while (!excelReader.getCellData(excelSheet, totalCols, colStartRowNum).equals("")) {
				totalCols++;
				if (excelReader.getCellData(excelSheet, totalCols, colStartRowNum).equals(headerName)) break;
			}

			int colWOHeader = totalCols;

			// Find out how many rows of data we have?
			// Data starts from 2nd row from the test1
			int dataStartRowNum = testStartRowNum + 2;
			int totalRows = 0;
			// Find all the rows till we get empty ("")
			while (!excelReader.getCellData(excelSheet, 0, dataStartRowNum + totalRows).equals("")) {
				totalRows++;
			}

			//Set empty value for all row of column 'WarrantyOrder'
			for (int rowNumber = dataStartRowNum; rowNumber < (dataStartRowNum + totalRows); rowNumber++) {
				excelReader.setCellData(excelSheet, colWOHeader, rowNumber, "");
			}

			log4j.debug("Delete all values for WarrantyOrder column on excel file...ends");
		}
		catch(Exception e){
			log4j.error("Delete all values for WarrantyOrder column on excel file - ERROR - " + e);
			logException(logTest, "Delete all values for WarrantyOrder column on excel file - ERROR", e);
		}
	}


	public static void setTestDataToExcelFile(String testcaseID, String testData, String merchant, String headerName, ExtentTest logTest) throws IOException {

		try {
			if(!testData.equals("")) {
				log4j.debug("Set Warranty Order ID to excel file...start");

				String testName = testcaseID;
				String excelSheet = GlobalVariables.TEST_DATA_SHEET;

				int testStartRowNum = 0;

				logInfo(logTest, "Set warranty order into excel file");
				// Find the row number from where test starts
				for (int rowNumber = 1; rowNumber <= excelReader.getRowCount(excelSheet); rowNumber++) {
					if (excelReader.getCellData(excelSheet, 0, rowNumber).equals(testName)) {
						testStartRowNum = rowNumber;
						break;
					}
				}

				// Column will start in the next row of test case
				int colStartRowNum = testStartRowNum + 1;

				//-----Find the column that contains header name----
				int totalCols = 0;
				while (!excelReader.getCellData(excelSheet, totalCols, colStartRowNum).equals("")) {
					totalCols++;
					if (excelReader.getCellData(excelSheet, totalCols, colStartRowNum).equals(headerName)) break;
				}
				int colWOHeader = totalCols;
				//--------------------------------------------------

				//------Find the column that contains merchant name----
				totalCols = 0;
				while (!excelReader.getCellData(excelSheet, totalCols, colStartRowNum).equals("")) {
					totalCols++;
					if (excelReader.getCellData(excelSheet, totalCols, colStartRowNum).equals("Retailer")) break;
				}
				int colMerchantHeader = totalCols;
				//--------------------------------------------------

				// Find out how many rows of data we have?
				// Data starts from 2nd row from the test1
				int dataStartRowNum = testStartRowNum + 2;
				int totalRows = 0;
				// Find all the rows till we get empty ("")
				while (!excelReader.getCellData(excelSheet, 0, dataStartRowNum + totalRows).equals("")) {
					totalRows++;
				}

				for (int rowNumber = dataStartRowNum; rowNumber < (dataStartRowNum + totalRows); rowNumber++) {
					if (excelReader.getCellData(excelSheet, colMerchantHeader, rowNumber).equals(merchant)) {
						excelReader.setCellData(excelSheet, colWOHeader, rowNumber, testData);
						break;
					}
				}

				log4j.debug("Set Warranty Order ID to excel file...end");
			}
		}
		catch(Exception e){
			log4j.error("setWarrantyInfoToExcelFile - ERROR - " + e);
			logException(logTest, "setWarrantyInfoToExcelFile - ERROR", e);
		}
	}
}