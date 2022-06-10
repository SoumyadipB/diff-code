package com.ericsson.isf.util;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.web.multipart.MultipartFile;

import com.ericsson.isf.exception.ApplicationException;

public class FileUtil {

	private static final String ERROR_DESCRIPTION = "Error Message";
	public static Workbook getWorkBookFromFile(final MultipartFile file) throws IOException {
		String fileName = file.getOriginalFilename();
		if (fileName.endsWith(".xlsx")) {
			return new XSSFWorkbook(file.getInputStream());
		} else if (fileName.endsWith(".xls")) {
			return new HSSFWorkbook(file.getInputStream());
		} else {
			throw new ApplicationException(500,
					"Invalid File: '" + fileName + "', Only .xls and .xlsx files are allowed");
		}
	}

	public static int getNumberofRows(final Sheet sheet, final MultipartFile file) {
		int rowsCount = sheet.getPhysicalNumberOfRows();
		if (rowsCount <= 1) {
			throw new ApplicationException(500,
					"Empty Sheet: '" + sheet.getSheetName() + "' found in file: '" + file.getOriginalFilename() + "'");
		}
		return rowsCount;
	}

	public static boolean isColumnInSequence(final List<String> columnList, final Row currentRow) {
		String cellValue;
		int totalColumnNumber = currentRow.getLastCellNum();
		for (int colNo = 0; colNo < totalColumnNumber; colNo++) {
			cellValue = currentRow.getCell(colNo, currentRow.CREATE_NULL_AS_BLANK).toString().trim();
			if (StringUtils.isBlank(cellValue) || !columnList.get(colNo).equalsIgnoreCase(cellValue)) {
				return false;
			}
		}
		return true;
	}

	public static Row getRow(final Sheet sheet, final int rowNo, final MultipartFile file) {
		Row row = sheet.getRow(rowNo);
		if (row == null) {
			throw new ApplicationException(500, "Row:" + (rowNo+1) + " is found empty in sheet: '" + sheet.getSheetName()
					+ "' in file: '" + file.getOriginalFilename() + "'");
		}
		return row;
	}
	public static byte[] generateXlsFile(List<Map<String, Object>> result) throws IOException {
	    	Workbook workbook=new XSSFWorkbook();
	        Sheet sheet = workbook.createSheet();
	        ByteArrayOutputStream baos = new ByteArrayOutputStream();
	        //write header
	        Row row = sheet.createRow(0);
	        int col=0;
	        for(String key:result.get(0).keySet()){
	            Cell cell = row.createCell(col++);
	            cell.setCellValue(key);	
	        }
	        
	        
	        //write data
	        for(int i=1;i<=result.size();i++){
	        	col=0;
	        	row = sheet.createRow(i);
		        for(String key:result.get(i-1).keySet()){
		            Cell cell = row.createCell(col++);
		            cell.setCellValue((result.get(i-1).get(key)==null)?(""):(result.get(i-1).get(key).toString()));
		        }
	        }
	        
	        workbook.write(baos);
	       
	        return baos.toByteArray();
	}

	public static byte[] generateXlsFileCnedb(List<Map<String, Object>> result, String flag) throws IOException {
		Workbook workbook = new XSSFWorkbook();
		Sheet sheet = workbook.createSheet();
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		// write header
		Row row = sheet.createRow(0);
		int col = 0;
		makeCnedbExcelHeader(result, flag, workbook, row, col);

		// write data
		makeCnedbExcellCellValue(result, flag, sheet);

		workbook.write(baos);

		return baos.toByteArray();
	}

	private static void makeCnedbExcellCellValue(List<Map<String, Object>> result, String flag, Sheet sheet) {
		Row row;
		int col;
		for (int i = 1; i <= result.size(); i++) {
			col = 0;
			row = sheet.createRow(i);
			for (String key : result.get(i - 1).keySet()) {
				if(org.apache.commons.lang3.StringUtils.equalsIgnoreCase(key, ERROR_DESCRIPTION) && 
	        			org.apache.commons.lang3.StringUtils.equalsIgnoreCase(flag, AppConstants.SUCCESS)) {
	        	}else {
	        		Cell cell = row.createCell(col++);
		            cell.setCellValue((result.get(i-1).get(key)==null)?(""):(result.get(i-1).get(key).toString()));
	        	}
				
				
		}
	}
	}
	private static void makeCnedbExcelHeader(List<Map<String, Object>> result, String flag, Workbook workbook, Row row,
			int col) {
		for (String key : result.get(0).keySet()) {
			if(org.apache.commons.lang3.StringUtils.equalsIgnoreCase(key, ERROR_DESCRIPTION) && 
        			org.apache.commons.lang3.StringUtils.equalsIgnoreCase(flag, AppConstants.SUCCESS)) {
        		
        	}else {
        		if(org.apache.commons.lang3.StringUtils.equalsIgnoreCase(key, ERROR_DESCRIPTION)) {
        			XSSFCellStyle style = (XSSFCellStyle) workbook.createCellStyle();
    				Cell cell = row.createCell(col++);
    				cell.setCellValue(key);
    				style.setFillForegroundColor(IndexedColors.RED.getIndex());
    				style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
    				cell.setCellStyle(style);
        		}
        		else {
        			Cell cell = row.createCell(col++);
                    cell.setCellValue(key);
        		}
        	}
		}

		}
	
	
}
