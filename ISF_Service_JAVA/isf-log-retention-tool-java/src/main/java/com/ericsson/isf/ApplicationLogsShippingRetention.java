package com.ericsson.isf;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import org.apache.commons.lang3.StringUtils;

import com.ericsson.isf.config.ApplicationConfig;
import com.ericsson.isf.constants.EnvironmentVariable;

public class ApplicationLogsShippingRetention {
	public static void main(String[] args) throws IOException {
		ApplicationConfig applicationConfig = new ApplicationConfig();

		if (args.length == 0) {
			throw new RuntimeException("Please provide the config file path.");
		}

		Properties properties = applicationConfig.getConfigFile(args[0]);
		if (properties.getProperty(EnvironmentVariable.LOGDIRECTORY.getPropertyKey()).isEmpty()) {
			throw new RuntimeException("Please provide log directory path.");
		}
		if (properties.getProperty(EnvironmentVariable.FILEPATTERN.getPropertyKey()).isEmpty()) {
			throw new RuntimeException("Please provide atleast one file pattern.");
		}
		String[] logDirectory = properties.getProperty(EnvironmentVariable.LOGDIRECTORY.getPropertyKey()).split(",");
		String[] filePattern = properties.getProperty(EnvironmentVariable.FILEPATTERN.getPropertyKey()).split(",");
		String operation = properties.getProperty(EnvironmentVariable.OPERATION.getPropertyKey()).trim();
		String targetDirectory = properties.getProperty(EnvironmentVariable.TARGETDIRECTORY.getPropertyKey()).trim();
		String convertedFileFormat = properties.getProperty(EnvironmentVariable.CONVERTEDFILEFORMAT.getPropertyKey())
				.trim();
		int retentionTimeInDays = Integer
				.parseInt(properties.getProperty(EnvironmentVariable.RETENTIONTIMEINDAYS.getPropertyKey()).trim());
		String catelinaFilePath = properties.getProperty(EnvironmentVariable.CATALINAFILEPATH.getPropertyKey()).trim();
		if (StringUtils.isNotEmpty(catelinaFilePath) && new File(catelinaFilePath.trim()).exists()) {
			File[] listFiles = new File(catelinaFilePath.trim()).listFiles(pathname -> {
				Pattern patternmatch = Pattern.compile(EnvironmentVariable.CATALINAFILE.getPropertyKey());
				Matcher matcher = patternmatch.matcher(pathname.getName());
				return matcher.find();
			});
			String result = listFiles.length>0?clearCatalinaFile(listFiles):"no any file exist under source directory "+catelinaFilePath;
			System.out.println(result);
		}
		if (operation.equalsIgnoreCase(EnvironmentVariable.MOVE.getPropertyKey())
				|| operation.equalsIgnoreCase(EnvironmentVariable.DELETE.getPropertyKey())) {
			Arrays.stream(logDirectory).forEach(directory -> Arrays.stream(filePattern).forEach(pattern -> {
				getDirectoryAndDeleteFiles(directory, pattern, operation, targetDirectory, retentionTimeInDays,
						convertedFileFormat);
			}));
		} else {
			System.out.println("you are entering wrong operation :" + operation);
		}

	}

	private static void getDirectoryAndDeleteFiles(String sourcePath, String pattern, String operation,
			String targetDirectory, int retentionTimeInDays, String convertedFileFormat) {
		LocalDateTime localDateTime = LocalDateTime.now().minusDays(retentionTimeInDays);
		File directory = new File(sourcePath.trim());
		if (directory.exists()) {
			File[] listFiles = directory.listFiles(pathname -> {
				Pattern patternmatch = Pattern.compile(pattern);
				Matcher matcher = patternmatch.matcher(pathname.getName());
				return matcher.find() && pathname.lastModified() < Timestamp.valueOf(localDateTime).getTime();
			});
			if (listFiles.length > 0) {

				if (operation.equalsIgnoreCase(EnvironmentVariable.MOVE.getPropertyKey()) && listFiles.length > 0) {
					String result = moveFileInZip(listFiles, targetDirectory.trim(), convertedFileFormat, pattern);
					System.out.println(result);

				} else if (operation.equalsIgnoreCase(EnvironmentVariable.DELETE.getPropertyKey())
						&& listFiles.length > 0) {
					for (File listFile : listFiles) {
						listFile.delete();
					}
					System.out.println("file deleted successfully.");

				}
			}
			/*
			 * else if(listFiles.length == 0) {
			 * System.out.println(pattern+" file not existed in directory "+sourcePath); }
			 */
		} else {
			System.out.println("Directory does not exists.");
		}

	}

	private static String clearCatalinaFile(File[] listFiles) {
		try {
			for (int i = 0; i < listFiles.length; i++) {
				File sourceFile = new File("" + listFiles[i]);
				PrintWriter writer = new PrintWriter(sourceFile);
				writer.print("");
				writer.close();
			}
			return "catelina file clear successfully";
		} catch (Exception e) {
			return "catelina file not cleared with exception " + e.getMessage();
		}

	}

	private static String moveFileInZip(File[] files, String targetDirectory, String convertedFileFormat,
			String pattern) {
		boolean status = false;
		long currentMilliseconds = System.currentTimeMillis();
		if (!pattern.isEmpty()) {
			pattern = pattern.replaceAll(EnvironmentVariable.REMOVESPECIALCHARACTER.getPropertyKey(), "");
		}
		String zipFile = targetDirectory + EnvironmentVariable.DOUBLESLASH.getPropertyKey() + currentMilliseconds
				+ pattern + EnvironmentVariable.CONSTANT.getPropertyKey() + "." + convertedFileFormat;
		File file = new File(targetDirectory);
		if (!file.exists())
			file.mkdirs();
		try {

			// create byte buffer
			byte[] buffer = new byte[1024];

			FileOutputStream fos = new FileOutputStream(zipFile);

			ZipOutputStream zos = new ZipOutputStream(fos);

			for (int i = 0; i < files.length; i++) {
				File sourceFile = new File("" + files[i]);

				FileInputStream fis = new FileInputStream(sourceFile);

				// begin writing a new ZIP entry, positions the stream to the start of the entry
				// data
				zos.putNextEntry(new ZipEntry(sourceFile.getName()));

				int length;

				while ((length = fis.read(buffer)) > 0) {
					zos.write(buffer, 0, length);
				}

				zos.closeEntry();

				// close the InputStream
				fis.close();
				sourceFile.deleteOnExit();
				status = true;

			}

			// close the ZipOutputStream
			zos.close();
			return status ? pattern + " File Moved Successfully." : "";
		} catch (IOException ioe) {
			return "Error creating zip file: " + ioe.getMessage();
		}

	}

}
