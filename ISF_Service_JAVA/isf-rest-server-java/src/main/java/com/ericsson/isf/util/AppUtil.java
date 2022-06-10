/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ericsson.isf.util;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.StringReader;
import java.io.UnsupportedEncodingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Time;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import javax.swing.JOptionPane;

import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.net.util.Base64;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.FormulaEvaluator;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFFormulaEvaluator;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.MultiValueMap;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.stream.JsonReader;

/**
 *
 * @author ekarath
 */
public class AppUtil {
	private static final Logger LOG = LoggerFactory.getLogger(AppUtil.class);

	private static byte[] sharedvector = { 0x01, 0x02, 0x03, 0x05, 0x07, 0x0B, 0x0D, 0x11 };

	private static String semicolon = AppConstants.CSV_CHAR_DOUBLE_SEMICOLON;

	public static final SimpleDateFormat DB_DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd");

	public static Date getDateFromString(String dateInString) {
		try {
			return DB_DATE_FORMAT.parse(dateInString);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return null;

	}

	public static final double WORKING_HOURS_IN_A_DAY = 8;

	public static double getNoOfWeekdayHoursBetweenDates(Date startDate, Date endDate) {

		Calendar cal1 = Calendar.getInstance();
		Calendar cal2 = Calendar.getInstance();
		cal1.setTime(startDate);
		cal2.setTime(endDate);

		cal1.set(Calendar.HOUR_OF_DAY, 0);
		cal1.set(Calendar.MINUTE, 0);
		cal1.set(Calendar.SECOND, 0);
		cal1.set(Calendar.MILLISECOND, 0);

		cal2.set(Calendar.HOUR_OF_DAY, 23);
		cal2.set(Calendar.MINUTE, 59);
		cal2.set(Calendar.SECOND, 59);
		cal2.set(Calendar.MILLISECOND, 0);

		startDate = cal1.getTime();
		endDate = cal2.getTime();

		long diff = endDate.getTime() - startDate.getTime();
		long diff1 = (long) TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS) + 1;
		Calendar c = Calendar.getInstance();

		int totalDays = 0;
		c.setTime(startDate);
		for (int i = 0; i < diff1; i++) {
			int day = c.get(Calendar.DAY_OF_WEEK);
			if (!(day == Calendar.SUNDAY || day == Calendar.SATURDAY)) {
				totalDays += 1;
			}
			c.add(Calendar.DATE, 1); // number of days to add
		}
		return totalDays * WORKING_HOURS_IN_A_DAY;
	}

	public static int getNoOfWeekdayDaysBetweenDates(Date startDate, Date endDate) {

		Calendar cal1 = Calendar.getInstance();
		Calendar cal2 = Calendar.getInstance();
		cal1.setTime(startDate);
		cal2.setTime(endDate);

		cal1.set(Calendar.HOUR_OF_DAY, 0);
		cal1.set(Calendar.MINUTE, 0);
		cal1.set(Calendar.SECOND, 0);
		cal1.set(Calendar.MILLISECOND, 0);

		cal2.set(Calendar.HOUR_OF_DAY, 23);
		cal2.set(Calendar.MINUTE, 59);
		cal2.set(Calendar.SECOND, 59);
		cal2.set(Calendar.MILLISECOND, 0);

		startDate = cal1.getTime();
		endDate = cal2.getTime();

		long diff = endDate.getTime() - startDate.getTime();
		long diff1 = (long) TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS) + 1;
		Calendar c = Calendar.getInstance();

		Date tmpDate = startDate;
		int totalDays = 0;
		for (int i = 0; i < diff1; i++) {
			c.setTime(tmpDate);

			int day = c.get(Calendar.DAY_OF_WEEK);
			if (day == 1 || day == 7) {

			} else {
				totalDays += 1;
			}
			c.add(Calendar.DATE, 1); // number of days to add
			tmpDate = c.getTime();

		}
		return totalDays;
	}

	public static double getTotalDaysBetweenDates(Date startDate, Date endDate) {

		Calendar cal1 = Calendar.getInstance();
		Calendar cal2 = Calendar.getInstance();
		cal1.setTime(startDate);
		cal2.setTime(endDate);

		cal1.set(Calendar.HOUR_OF_DAY, 0);
		cal1.set(Calendar.MINUTE, 0);
		cal1.set(Calendar.SECOND, 0);
		cal1.set(Calendar.MILLISECOND, 0);

		cal2.set(Calendar.HOUR_OF_DAY, 23);
		cal2.set(Calendar.MINUTE, 59);
		cal2.set(Calendar.SECOND, 59);
		cal2.set(Calendar.MILLISECOND, 0);

		startDate = cal1.getTime();
		endDate = cal2.getTime();

		long diff = endDate.getTime() - startDate.getTime();
		long diff1 = (long) TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS) + 1;

		LOG.info("Total diff in days diff :" + diff1);
		Calendar c = Calendar.getInstance();

		Date tmpDate = startDate;
		int totalDays = 0;
		for (int i = 0; i < diff1; i++) {
			c.setTime(tmpDate);

			int day = c.get(Calendar.DAY_OF_WEEK);
			totalDays += 1;
			c.add(Calendar.DATE, 1); // number of days to add
			tmpDate = c.getTime();
		}
		LOG.info("Total diff in days :" + totalDays);
		return totalDays;
	}

	public static String getFileNameWithTimestamp(String fileName) {
		String fileNameWithoutExtension;
		String extension;
		String newFileName;
		if (fileName.lastIndexOf(".") == -1) {
			fileNameWithoutExtension = fileName;
			extension = "";
		} else {
			fileNameWithoutExtension = fileName.substring(0, fileName.lastIndexOf("."));
			extension = fileName.substring(fileName.lastIndexOf(".") + 1);
		}

		if (extension == null || "".equalsIgnoreCase(extension)) {
			newFileName = fileNameWithoutExtension + "_" + new Date().getTime();
		} else {
			newFileName = fileNameWithoutExtension + "_" + new Date().getTime() + "." + extension;
		}

		return newFileName;
	}

	public static String getRelativeFilePath(String domain, String technology, String vendor, String projectID) {
		String filePath = "";
		filePath = projectID + "/" + domain + "/" + technology + "/" + vendor;
		return filePath;
	}

	public static String getRelativeFilePathForFlowChart(int projectID, int subActivityID) {
		String filePath = "";
		filePath = projectID + "/" + subActivityID;
		return filePath;
	}

	public static String getRelativeFilePathForWOCreation(int projectID, String createdBy) {
		String filePath = "";
		filePath = projectID + "/" + createdBy;
		return filePath;
	}

	public static String EncryptText(String RawText) {
		String EncText = "";
		byte[] keyArray = new byte[24];
		byte[] temporaryKey;
		String key = "espa";
		byte[] toEncryptArray = null;

		try {

			toEncryptArray = RawText.getBytes("UTF-8");
			MessageDigest m = MessageDigest.getInstance("MD5");
			temporaryKey = m.digest(key.getBytes("UTF-8"));

			if (temporaryKey.length < 24) // DESede require 24 byte length key
			{
				int index = 0;
				for (int i = temporaryKey.length; i < 24; i++) {
					keyArray[i] = temporaryKey[index];
				}
			}

			Cipher c = Cipher.getInstance("DESede/CBC/PKCS5Padding");
			c.init(Cipher.ENCRYPT_MODE, new SecretKeySpec(keyArray, "DESede"), new IvParameterSpec(sharedvector));
			byte[] encrypted = c.doFinal(toEncryptArray);
			EncText = Base64.encodeBase64String(encrypted);

		} catch (NoSuchAlgorithmException | UnsupportedEncodingException | NoSuchPaddingException | InvalidKeyException
				| InvalidAlgorithmParameterException | IllegalBlockSizeException | BadPaddingException NoEx) {
			JOptionPane.showMessageDialog(null, NoEx);
		}

		return EncText;
	}

	public static String DecryptText(String EncText) {

		String RawText = "";
		byte[] keyArray = new byte[24];
		byte[] temporaryKey;
		String key = "espa";
		byte[] toEncryptArray = null;

		try {
			MessageDigest m = MessageDigest.getInstance("MD5");
			temporaryKey = m.digest(key.getBytes("UTF-8"));

			if (temporaryKey.length < 24) // DESede require 24 byte length key
			{
				int index = 0;
				for (int i = temporaryKey.length; i < 24; i++) {
					keyArray[i] = temporaryKey[index];
				}
			}

			Cipher c = Cipher.getInstance("DESede/CBC/PKCS5Padding");
			c.init(Cipher.DECRYPT_MODE, new SecretKeySpec(keyArray, "DESede"), new IvParameterSpec(sharedvector));
			byte[] decrypted = c.doFinal(Base64.decodeBase64(EncText));

			RawText = new String(decrypted, "UTF-8");
		} catch (NoSuchAlgorithmException | UnsupportedEncodingException | NoSuchPaddingException | InvalidKeyException
				| InvalidAlgorithmParameterException | IllegalBlockSizeException | BadPaddingException NoEx) {
			// JOptionPane.showMessageDialog(null, NoEx);
			return "Error";
		}

		return RawText;
	}

	public static String signumGenerator(String firstName, String lastName, String prefix, String charcter, int i) {
		int firstRanLen = firstName.length();
		int lastRanLen = lastName.length();
		String signum = prefix;

		if (firstName.length() < 3 && lastName.length() < 3) {
			while (firstRanLen < 3) {
				firstName += charcter;
				firstRanLen++;
			}

			while (lastRanLen < 3) {
				lastName += charcter;
				lastRanLen++;
			}
			signum += firstName + lastName;

		} else if (firstName.length() < 3) {
			while (firstRanLen < 3) {
				firstName += charcter;
				firstRanLen++;
			}
			signum += firstName + lastName.substring(0, 3);
		} else if (lastName.length() < 3) {
			while (lastRanLen < 3) {
				lastName += charcter;
				lastRanLen++;
			}
			signum += firstName.substring(0, 3) + lastName;
		} else {
			signum += firstName.substring(0, 3) + lastName.substring(0, 3);
			if (i >= 1) {
				signum = signum.substring(0, signum.length() - 1);
				signum += charcter;
			}

		}

		return signum;
	}

	public static String generateMD5(String string) throws NoSuchAlgorithmException {
		MessageDigest md = MessageDigest.getInstance("MD5");
		md.update(string.getBytes());
		byte byteData[] = md.digest();

		// convert the byte to hex format method 1
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < byteData.length; i++) {
			sb.append(Integer.toString((byteData[i] & 0xff) + 0x100, 16).substring(1));
		}
		return sb.toString();

	}

	public static void convertExcelToCSV(String ExcelfileName, String csvFilePath) throws IOException {
		XSSFWorkbook wBook = new XSSFWorkbook(new FileInputStream(ExcelfileName));
		for (int i = 0; i < wBook.getNumberOfSheets(); i++) {
			// Get first sheet from the workbook

			XSSFSheet sheet = wBook.getSheetAt(i);

			FileOutputStream fos = new FileOutputStream(csvFilePath + ".csv");
			StringBuffer data = new StringBuffer();
			// Get the workbook object for XLSX file

			Row row;
			// Iterate through each rows from first sheet
			Iterator<Row> rowIterator = sheet.iterator();

			while (rowIterator.hasNext()) {
				row = rowIterator.next();
				List<Cell> cells = new ArrayList<Cell>();
				// For each row, iterate through each columns
				Iterator<Cell> cellIterator = row.cellIterator();
				int lastColumn = Math.max(row.getLastCellNum(),11);
				for (int cn = 0; cn < lastColumn; cn++) {
					Cell c = row.getCell(cn, Row.RETURN_BLANK_AS_NULL);
					cells.add(c);
				}

				for (Cell cell : cells) {
					if (cell != null) {
						switch (cell.getCellType()) {
						case Cell.CELL_TYPE_BOOLEAN:
							data.append(cell.getBooleanCellValue() + "|");

							break;
						case Cell.CELL_TYPE_NUMERIC:
							data.append(cell.getNumericCellValue() + "|");

							break;
						case Cell.CELL_TYPE_STRING:

							data.append(cell.getStringCellValue().replace("|", "").replace("\r", "").replace("\n", "")
									.replace("\r\n", "") + "|");
							break;

						case Cell.CELL_TYPE_BLANK:
							data.append("|");
							break;

						case Cell.CELL_TYPE_FORMULA:
							try {
								data.append(cell.getNumericCellValue() + "|");
							} catch (Exception e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
							break;

						default:
							data.append("|");

						}
					} else {
						data.append("|");
					}
				}

				data.deleteCharAt(data.length() - 1);
				data.append("\r\n");
			}
			String mainData = data.toString();

			mainData = mainData.replace("$", "");
			mainData = mainData.replace("^", "");
			mainData = mainData.replace("@", "");
			mainData = mainData.replace("#", "");
			mainData = mainData.replace("%", "");
			// mainData = mainData.replace("&", "");
			mainData = mainData.replace("~", "");
			mainData = mainData.replace("*", "");
			// mainData = mainData.replace("'s", "");

			fos.write(mainData.getBytes());
			fos.close();
		}

	}

		public static String EncryptText(String RawText, String key) {
		String EncText = "";
		byte[] keyArray = new byte[24];
		byte[] temporaryKey;
		byte[] toEncryptArray = null;

		try {

			toEncryptArray = RawText.getBytes("UTF-8");
			MessageDigest m = MessageDigest.getInstance("MD5");
			temporaryKey = m.digest(key.getBytes("UTF-8"));

			if (temporaryKey.length < 24) // DESede require 24 byte length key
			{
				int index = 0;
				for (int i = temporaryKey.length; i < 24; i++) {
					keyArray[i] = temporaryKey[index];
				}
			}

			Cipher c = Cipher.getInstance("DESede/CBC/PKCS5Padding");
			c.init(Cipher.ENCRYPT_MODE, new SecretKeySpec(keyArray, "DESede"), new IvParameterSpec(sharedvector));
			byte[] encrypted = c.doFinal(toEncryptArray);
			EncText = Base64.encodeBase64String(encrypted);

		} catch (NoSuchAlgorithmException | UnsupportedEncodingException | NoSuchPaddingException | InvalidKeyException
				| InvalidAlgorithmParameterException | IllegalBlockSizeException | BadPaddingException NoEx) {
			JOptionPane.showMessageDialog(null, NoEx);
		}

		return EncText;
	}

	public static String DecryptText(String EncText, String key) {

		String RawText = "";
		byte[] keyArray = new byte[24];
		byte[] temporaryKey;
		byte[] toEncryptArray = null;

		try {
			MessageDigest m = MessageDigest.getInstance("MD5");
			temporaryKey = m.digest(key.getBytes("UTF-8"));

			if (temporaryKey.length < 24) // DESede require 24 byte length key
			{
				int index = 0;
				for (int i = temporaryKey.length; i < 24; i++) {
					keyArray[i] = temporaryKey[index];
				}
			}

			Cipher c = Cipher.getInstance("DESede/CBC/PKCS5Padding");
			c.init(Cipher.DECRYPT_MODE, new SecretKeySpec(keyArray, "DESede"), new IvParameterSpec(sharedvector));
			byte[] decrypted = c.doFinal(Base64.decodeBase64(EncText));

			RawText = new String(decrypted, "UTF-8");
		} catch (NoSuchAlgorithmException | UnsupportedEncodingException | NoSuchPaddingException | InvalidKeyException
				| InvalidAlgorithmParameterException | IllegalBlockSizeException | BadPaddingException NoEx) {
			// JOptionPane.showMessageDialog(null, NoEx);
			return "Error";
		}

		return RawText;
	}

	static SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");

	public static List<String> getDaysBetweenDates(Date startdate, Date enddate) {
		List<String> dates = new ArrayList<String>();
		Calendar calendar = new GregorianCalendar();
		calendar.setTime(startdate);
		Date result = null;
		while (calendar.getTime().before(enddate)) {
			result = calendar.getTime();
			dates.add(formatter.format(result));
			calendar.add(Calendar.DATE, 1);
		}
		result = calendar.getTime();
		if (result.compareTo(enddate) <= 0)
			dates.add(formatter.format(result));
		return dates;
	}

	public static LocalTime addTime(Time sqlTime, String timeOffset) throws Exception {
		char plusOrMinus = timeOffset.charAt(0);
		String hourMinutes = timeOffset.substring(1, timeOffset.length());
		String hour = hourMinutes.split(":")[0];
		String minutes = hourMinutes.split(":")[1];

		LocalTime localtime = sqlTime.toLocalTime();
		if (plusOrMinus == '+') {
			localtime = localtime.plusHours(Long.parseLong(hour));
			localtime = localtime.plusMinutes(Long.parseLong(minutes));
		} else if (plusOrMinus == '-') {
			localtime = localtime.plusHours(-Long.parseLong(hour));
			localtime = localtime.plusMinutes(-Long.parseLong(minutes));
		}

		System.out.println("localtime-->" + localtime);

		return localtime;
	}

	public static final int trimLeftByteArray(byte[] bytes, int pos) {
		if (bytes == null) {
			return pos;
		}
		while ((pos < bytes.length)
				&& ((bytes[pos] == ' ') || (bytes[pos] == ',') || (bytes[pos] == '\n') || (bytes[pos] == '\r'))) {
			pos++;
		}
		return pos;
	}

	public static final int trimRightByteArray(byte[] bytes, int pos) {
		if (bytes == null) {
			return pos;
		}
		while ((pos >= 0)
				&& ((bytes[pos] == ' ') || (bytes[pos] == ',') || (bytes[pos] == '\n') || (bytes[pos] == '\r'))) {
			pos--;
		}
		return pos;
	}

	public static byte[] removeLeadingBlankLine(byte[] byteArray) {
		int start = trimLeftByteArray(byteArray, 0);
		int end = trimRightByteArray(byteArray, byteArray.length - 1);
		int length = end - start + 1;
		if (length != 0) {
			byte[] newByteArray = new byte[end - start + 1];
			System.arraycopy(byteArray, start, newByteArray, 0, length);
			return newByteArray;
		} else {
			return new byte[0];
		}
	}

	public static void main(String[] args) {
		/*
		 * String b = EncryptText("password", "isf@123"); String a = DecryptText(b,
		 * "isf@123"); System.out.println(a); System.out.println(b);
		 */
		/*
		 * String DOMAIN_ID_CANNOT_BE_0 = "Invalid input.... %s cannot be 0 !!!"; String
		 * str = String.format(DOMAIN_ID_CANNOT_BE_0,"domainid"); String str1
		 * =String.format(DOMAIN_ID_CANNOT_BE_0,"tech"); System.out.println(str);
		 * System.out.println(str1);
		 */

		//String str = "\\\\eamcs.ericsson.se\\\\group15\\BRTIM\\WCDMA-Rollout\\19.Weekly_KPI_Bolinha\\Weekly_Offender_New_Scope";
		System.out.println(DecryptText("BqGZKPCzqXICkrH9VxjwAIXS684VkgT0UEG4a2ojU/R/M/SoxLPcqw==", "tser"));

	}

	/**
	 * This method converts json string to class object
	 * 
	 * @return T
	 */
	@SuppressWarnings("hiding")
	public static <T> T convertJsonToClassObject(String jsonString, Class<T> class1) {

		Gson gson = new Gson();
		JsonParser parser = new JsonParser();
		JsonObject object = (JsonObject) parser.parse(StringUtils.trim(jsonString));// response will be the json String
		return gson.fromJson(object, class1);
	}

	/**
	 * 
	 * @param jsonString
	 * @param class1
	 * @return T
	 */
	@SuppressWarnings("hiding")
	public static <T> T convertHtmlJsonToClassObject(String jsonString, Class<T> class1) {

		Gson gson = new GsonBuilder().disableHtmlEscaping().create();
		JsonParser parser = new JsonParser();
		JsonReader reader = new JsonReader(
				new StringReader(StringEscapeUtils.unescapeHtml(StringUtils.trim(jsonString))));
		reader.setLenient(true);
		JsonObject object = (JsonObject) parser.parse(reader);// response will be the json String
		return gson.fromJson(object, class1);
	}

	public static String convertClassObjectToJson(Object object) throws JsonProcessingException {
		 ObjectMapper Obj = new ObjectMapper(); 
		return Obj.writeValueAsString(object); 
	}

	/**
	 * Convert a list into comma separated string e.g > [1,2,3] -> ('1','2','3')
	 * 
	 * @param dataList
	 * @return
	 */
	public static String convertListToCommaSeparatedString(List<String> dataList) {

		String resultCommaSeparated = StringUtils.EMPTY;
		List<String> list = new ArrayList<String>();

		for (String data : dataList) {
			data = data.replaceAll("'", "''");
			list.add(data);
		}

		resultCommaSeparated = String.join("','", list);
		resultCommaSeparated = "'" + resultCommaSeparated + "'";

		return resultCommaSeparated;
	}
	
	/** Purpose:This method used to validate url starting with https or ftp.
	 * 
	 * @param url
	 * @return boolean
	 */
	
	public static boolean validateUrl(String url) {
		Pattern pattern =null;
		boolean isValid=false;
		if (StringUtils.isEmpty(url)) {
			return Boolean.FALSE;
		}
		boolean resultOfUrlValidation = false;
		try {
			
			if(url.startsWith("https")) {
				 isValid=isUrlWithDomain(url);
				 pattern = Pattern.compile(AppConstants.HTTP_URL_VALIDATION_REGEX);
			}else if(url.startsWith("ftp")){
				 pattern = Pattern.compile(AppConstants.FTP_URL_VALIDATION_REGEX);
			}
			else {
				return resultOfUrlValidation;
			}
			
			Matcher matcher = pattern.matcher(url);
			if (matcher.matches()) {
				resultOfUrlValidation = Boolean.TRUE;
			}
		} catch (RuntimeException e) {
			return Boolean.FALSE;
		}
		
		return resultOfUrlValidation || isValid;
	}

	private static boolean isUrlWithDomain(String url) {
		Pattern pattern = Pattern.compile(AppConstants.HTTP_SECOND_URL_VALIDATION_REGEX);
		Matcher matcher = pattern.matcher(url);
		if (matcher.matches()) {
			return Boolean.TRUE;
		}else {
			return Boolean.FALSE;
		}
		
	}

	/**
	 * Purpose:This method used to validate transactional url i.e ;.
	 * @param url
	 * @return
	 */
	
	public static boolean validateTransactionalUrl(String url) {
		if (StringUtils.isEmpty(url)) {
			return Boolean.FALSE;
		} else if ((url.startsWith("https")||url.startsWith("ftp")) ) {
			return validateUrl(url);
		}
		else if (url.replace("\\", "\\\\").startsWith("\\")) {
			return validateSharedFolderUrl(url);
		} else {
			return Boolean.FALSE;
		}

	}

	public static boolean validateSharedFolderUrl(String url) {
		boolean resultOfUrlValidation = false;
	if (StringUtils.isEmpty(url)) {
		return Boolean.FALSE;
	}

	try {
		Pattern pattern = Pattern.compile(AppConstants.SHARED_FOLDER_REGEX);
		Matcher matcher = pattern.matcher(url);
		if (matcher.matches()) {
			resultOfUrlValidation = Boolean.TRUE;
		}
	} catch (RuntimeException e) {
		return Boolean.FALSE;
	}
	return resultOfUrlValidation;
}
	 public static String substringNthOccurrence(String string, char c, int n) {
	        if (n <= 0) {
	            return "";
	        }

	        int index = 0;
	        while (n-- > 0 && index != -1) {
	            index = string.indexOf(c, index + 1);   
	        }
	        return index > -1 ? string.substring(0, index) : string;
	    }
	 public static String substringNthOccurrenceSharedFolder(String string, char c, int n) {
	        if (n <= 0) {
	            return "";
	        }

	        int index = 0;
	        while (n-- > 0 && index != -1) {
	            index = string.indexOf(c, index + 1);   
	        }
	        return index > -1 ? string.substring(0, index-1) : string;
	    }
	 public static Map<String,Object> validateTransactionalUrlData(String url) {
		 Map<String,Object> map=new HashMap<>();
			if (StringUtils.isEmpty(url)) {
				map.put(AppConstants.RESULT, Boolean.FALSE);
				map.put("message", "URL should not be empty.");
			} else if ((url.startsWith("https://")||url.startsWith("ftp")) ) {
				boolean status=validateUrl(url);
				map.put(AppConstants.RESULT, status);
				if(status) {
					map.put("message", url+" is  a valid url.");
				}else {
					map.put("message", url+" is not a valid url.");
				}
				
			}
			else if (url.replace("\\", "\\\\").startsWith("\\")) {
				boolean status=validateSharedFolderUrl(url);
				map.put(AppConstants.RESULT, status);
				if(status) {
					map.put("message", url+" is  a valid url.");
				}else {
					map.put("message", url+" is not a valid url.");
				}
				
			} else {
				map.put(AppConstants.RESULT,Boolean.FALSE);
				map.put("message", url+" should start with https or ftp or \\\\");
			}
			return map;

		}
		public static void convertMSSLeaveExcelToCSV(String ExcelfileName, String csvFilePath) throws IOException {

			XSSFWorkbook wBook = new XSSFWorkbook(new FileInputStream(ExcelfileName));
			for (int i = 0; i < wBook.getNumberOfSheets(); i++) {
				// Get first sheet from the workbook

				XSSFSheet sheet = wBook.getSheetAt(i);

				DataFormatter objDefaultFormat = new DataFormatter();
				FormulaEvaluator objFormulaEvaluator = new XSSFFormulaEvaluator((XSSFWorkbook) wBook);
				
				FileOutputStream fos = new FileOutputStream(csvFilePath);
				StringBuffer data = new StringBuffer();
				// Get the workbook object for XLSX file

				Row row;
				// Iterate through each rows from first sheet
				Iterator<Row> rowIterator = sheet.iterator();
				int rowNumber = 0;
				while (rowIterator.hasNext()) {
					rowNumber++;
					row = rowIterator.next();
					List<Cell> cells = new ArrayList<>();
					// For each row, iterate through each columns
					Iterator<Cell> cellIterator = row.cellIterator();
					int lastColumn = Math.max(row.getLastCellNum(), 9);
					for (int cn = 0; cn < lastColumn; cn++) {
						if(cn == 10 && rowNumber != 1) {
							cells.add(null);
						} else {
							Cell c = row.getCell(cn, Row.RETURN_BLANK_AS_NULL);
							cells.add(c);							
						}
					}

					for (Cell cell : cells) {
						if (cell != null) {
							switch (cell.getCellType()) {
							case Cell.CELL_TYPE_BOOLEAN:
								data.append(cell.getBooleanCellValue() + semicolon);

								break;
							case Cell.CELL_TYPE_NUMERIC:
								data.append(objDefaultFormat.formatCellValue(cell,objFormulaEvaluator) + semicolon);

								break;
							case Cell.CELL_TYPE_STRING:

								data.append(cell.getStringCellValue() + semicolon);
								break;

							case Cell.CELL_TYPE_BLANK:
								data.append(semicolon);
								break;

							case Cell.CELL_TYPE_FORMULA:
								try {
									data.append(cell.getNumericCellValue() + semicolon);
								} catch (Exception e) {
									e.printStackTrace();
								}
								break;

							default:
								data.append(semicolon);

							}
						} else {
							data.append(semicolon);
						}
					}

					data.deleteCharAt(data.length() - 1);
					data.append("\r\n");
				}
				String mainData = data.toString();

				mainData = mainData.replace("$", "");
				mainData = mainData.replace("^", "");
				mainData = mainData.replace("@", "");
				mainData = mainData.replace("#", "");
				mainData = mainData.replace("%", "");
				mainData = mainData.replace("~", "");
				mainData = mainData.replace("*", "");
				mainData = mainData.replace("'s", "");

				fos.write(mainData.getBytes());
				fos.close();
			}

		}


	public static String getFileNameWithDateAndTime(String fileName) {
		String fileNameWithoutExtension;
		String extension;
		String newFileName;
		if (fileName.lastIndexOf(".") == -1) {
			fileNameWithoutExtension = fileName;
			extension = "";
		} else {
			fileNameWithoutExtension = fileName.substring(0, fileName.lastIndexOf("."));
			extension = fileName.substring(fileName.lastIndexOf(".") + 1);
		}

		if (extension == null || "".equalsIgnoreCase(extension)) {
			newFileName = fileNameWithoutExtension + "_" +new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date()) ;
		} else {
			newFileName = fileNameWithoutExtension + "_" + new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date()) + "." + extension;
		}

		return newFileName;

	}

	public static String convertClassObjectToJsonServerBOT(MultiValueMap<String, Object> parts) {
		Gson gson = new Gson();
        return gson.toJson(parts);
	}
	
	public static  Date addHoursToJavaUtilDate(Date date, int hours) {
	    Calendar calendar = Calendar.getInstance();
	    calendar.setTime(date);
	    calendar.add(Calendar.HOUR_OF_DAY, hours);
	    return calendar.getTime();
	}
	
	public static void convertCnedbExcelToCSV(String excelfileName, String csvFilePath) throws IOException {

		XSSFWorkbook wBook = new XSSFWorkbook(new FileInputStream(excelfileName));
		for (int i = 0; i < wBook.getNumberOfSheets(); i++) {
			// Get first sheet from the workbook

			XSSFSheet sheet = wBook.getSheetAt(i);

			DataFormatter objDefaultFormat = new DataFormatter();
			FormulaEvaluator objFormulaEvaluator = new XSSFFormulaEvaluator(wBook);
			try(FileOutputStream fos = new FileOutputStream(csvFilePath)){
				StringBuilder data = new StringBuilder();
				// Get the workbook object for XLSX file

				// Iterate through each rows from first sheet
				Iterator<Row> rowIterator = sheet.iterator();

				iterateRowForExcelToCsv(objDefaultFormat, objFormulaEvaluator, data, rowIterator);
				String mainData = data.toString();

				mainData = mainData.replace("$", "");
				mainData = mainData.replace("^", "");
				mainData = mainData.replace("@", "");
				mainData = mainData.replace("#", "");
				mainData = mainData.replace("%", "");
				mainData = mainData.replace("~", "");
				mainData = mainData.replace("*", "");
				mainData = mainData.replace("'s", "");

				fos.write(mainData.getBytes());
			}
			
		}

	}

	private static void iterateRowForExcelToCsv(DataFormatter objDefaultFormat, FormulaEvaluator objFormulaEvaluator,
			StringBuilder data, Iterator<Row> rowIterator) {
		Row row;
		while (rowIterator.hasNext()) {
			row = rowIterator.next();
			boolean checkEmptyRow=isRowEmpty(row);
			if(!checkEmptyRow) {
				List<Cell> cells = new ArrayList<>();
				// For each row, iterate through each columns
				
				int lastColumn = Math.max(row.getLastCellNum(), 9);
				for (int cn = 0; cn < lastColumn; cn++) {
					Cell c = row.getCell(cn, Row.RETURN_BLANK_AS_NULL);
					cells.add(c);
				}

				checkCellType(objDefaultFormat, objFormulaEvaluator, data, cells);

				data.deleteCharAt(data.length() - 1);
				
				data.append("\r\n");
			}
			
		}
	}

	private static void checkCellType(DataFormatter objDefaultFormat, FormulaEvaluator objFormulaEvaluator,
			StringBuilder data, List<Cell> cells) {
		for (Cell cell : cells) {
			if (cell != null) {
				switch (cell.getCellType()) {
				case Cell.CELL_TYPE_BOOLEAN:
					data.append(cell.getBooleanCellValue() + semicolon);

					break;
				case Cell.CELL_TYPE_NUMERIC:
					data.append(objDefaultFormat.formatCellValue(cell,objFormulaEvaluator) + semicolon);

					break;
				case Cell.CELL_TYPE_STRING:

					data.append(cell.getStringCellValue() + semicolon);
					break;

				case Cell.CELL_TYPE_BLANK:
					data.append(semicolon);
					break;

				case Cell.CELL_TYPE_FORMULA:
					try {
						data.append(cell.getNumericCellValue() + semicolon);
					} catch (Exception e) {
						e.printStackTrace();
					}
					break;

				default:
					data.append(semicolon);

				}
			} else {
				data.append(semicolon);
			}
		}
	}

	private static boolean isRowEmpty(Row row) {
		for (int c = row.getFirstCellNum(); c < row.getLastCellNum(); c++) {
	        Cell cell = row.getCell(c);
	        if (cell != null && cell.getCellType() != Cell.CELL_TYPE_BLANK)
	            return false;
	    }
	    return true;
	}
}

