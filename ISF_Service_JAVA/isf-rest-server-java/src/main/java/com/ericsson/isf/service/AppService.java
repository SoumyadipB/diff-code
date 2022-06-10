package com.ericsson.isf.service;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;
import org.supercsv.cellprocessor.ParseInt;

import com.ericsson.isf.azure.keyVault.service.ClientSecretKeyValueProvider;
import com.ericsson.isf.config.ApplicationConfigurations;
import com.ericsson.isf.dao.AppUtilDAO;
import com.ericsson.isf.exception.ApplicationException;
import com.ericsson.isf.model.AzureAppModel;
import com.ericsson.isf.model.DbJobsModel;
import com.ericsson.isf.model.GenericMailModel;
import com.ericsson.isf.model.ProficiencyTypeModal;
import com.ericsson.isf.model.Response;
import com.ericsson.isf.model.ServiceStartStopModel;
import com.ericsson.isf.model.SignalR;
import com.ericsson.isf.model.SignalrModel;
import com.ericsson.isf.model.VaultModel;
import com.ericsson.isf.util.AppConstants;
import com.ericsson.isf.util.ConfigurationFilesConstants;
import com.microsoft.sqlserver.jdbc.SQLServerBulkCSVFileRecord;
import com.microsoft.sqlserver.jdbc.SQLServerBulkCopy;
import com.microsoft.sqlserver.jdbc.SQLServerBulkCopyOptions;
import com.microsoft.sqlserver.jdbc.SQLServerException;

@Service
public class AppService {

	private final Logger LOG = LoggerFactory.getLogger(AppService.class);

	@Autowired
	private ApplicationConfigurations configurations;

	@Autowired
	private JdbcConnectionFactory jdbcConnectionFactory;
	
	@Autowired
	private AzureService azureService;
	
	@Autowired
	private ClientSecretKeyValueProvider clientSecretKeyValueProvider;
	
	@Autowired
	private OutlookAndEmailService emailService;
	
	@Autowired
	private AppUtilDAO appUtilDAO;
	
	@Autowired
	private ValidationUtilityService validationUtilityService;

	public String getConfigUploadFilePath() {
		return configurations.getStringProperty(ConfigurationFilesConstants.FILE_PATH);
	}

	public String getConfigUploadFilePathForNodeTemplate() {
		return configurations.getStringProperty(ConfigurationFilesConstants.NODE_TEMPLATE_PATH);
	}

	public String getConfigUploadFilePathForWOCreationTemplate() {
		return configurations.getStringProperty(ConfigurationFilesConstants.WOCREATION_TEMPLATE_PATH);
	}

	public String getConfigUploadFilePathForSampleTemplate() {
		return configurations.getStringProperty(ConfigurationFilesConstants.SAMPLE_TEMPLATE_PATH);
	}

	public String getConfigHelpPath() {
		return configurations.getStringProperty(ConfigurationFilesConstants.FILE_PATH);
	}

	public String getServerIP() {
		return configurations.getStringProperty(ConfigurationFilesConstants.SERVER_IP);
	}

	public String getConfigServerPort() {
		return configurations.getStringProperty(ConfigurationFilesConstants.SERVER_PORT);
	}

	public String getConfigUserName() {
		return configurations.getStringProperty(ConfigurationFilesConstants.USER_NAME);
	}

	public String getConfigPassword() {
		return configurations.getStringProperty(ConfigurationFilesConstants.PASS);
	}

	public String getSecretKey(String secretName) {
		try {

			String keyVaultName = configurations.getStringProperty(ConfigurationFilesConstants.AZURE_KEYVAULT_NAME);

			VaultModel vaultModel = azureService.getActiveSecretInfoByVaultNameAndSecretType(keyVaultName,
					secretName);
			AzureAppModel azureAppModel = azureService.getInfoByAzureAppName(
					configurations.getStringProperty(ConfigurationFilesConstants.AZURE_APP_NAME));

			String secretValue = clientSecretKeyValueProvider.getSecretValue(azureAppModel, vaultModel);
			LOG.info("API Manager security key obtained");
			return secretValue;
		
		} catch (Exception e) {

			e.printStackTrace();
			LOG.error("error occured while getting apim Auth Key: " + e.getMessage());
			return null;
		}
	}

	@SuppressWarnings("resource")
	public boolean CsvBulkUploadNewGen(String filePath, String tableName, String fileName)
			throws IOException, SQLException {
		Connection con = getConnection();
		boolean isSuccess = false;
		Statement st = null;
		Statement stmt = null;

		try {

			String queryCopyTableStructure = "SELECT TOP 0 * INTO " + fileName + " FROM " + tableName;
			st = con.createStatement();

			int i = st.executeUpdate(queryCopyTableStructure);
			LOG.info("table created" + i);

			SQLServerBulkCopy bulkCopy = null;
			ResultSetMetaData rsMetadata;
			SQLServerBulkCSVFileRecord fileRecord = null;

			bulkCopy = new SQLServerBulkCopy(con);
			stmt = con.createStatement();
			ResultSet resultSet = null;

			bulkCopy.setDestinationTableName(
					configurations.getStringProperty(ConfigurationFilesConstants.DATASOURCE_DBCONNDBNAME) + ".dbo."
							+ fileName);
			fileRecord = new SQLServerBulkCSVFileRecord(filePath, null, AppConstants.CSV_CHAR, false);
			resultSet = stmt.executeQuery("SELECT TOP 0 * from " + fileName);
			rsMetadata = resultSet.getMetaData();
			for (int i1 = 1; i1 <= rsMetadata.getColumnCount(); i1++) {
				fileRecord.addColumnMetadata(i1, null, java.sql.Types.NVARCHAR, 1000, 0);
			}
			try {
				bulkCopy.writeToServer(fileRecord);
				isSuccess = true;
			} catch (Exception e) {
				e.printStackTrace();
				isSuccess = false;
				throw new ApplicationException(500,
						"Error while uploading the file.Please upload valid file");
				
			}

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			con.close();
			stmt.close();
			st.close();
		}
		return isSuccess;
	}

	@SuppressWarnings("resource")
	public boolean CsvBulkUploadNewGenWithSeperator(String filePath, String tableName, String fileName, String seperator)
			throws IOException, SQLException {
		Connection con = getConnection();
		boolean isSuccess = false;
		Statement st = null;
		Statement stmt = null;

		try {
			LOG.info("parameter for table creation is ---> {}", " filePath "+filePath+" tableName "+tableName+" fileName "+fileName+" seperator "+seperator);
			String queryCopyTableStructure = "SELECT TOP 0 * INTO " + fileName + " FROM " + tableName;
			LOG.info("Query is:::::::::::::::::::"+queryCopyTableStructure);
			st = con.createStatement();

			int i = st.executeUpdate(queryCopyTableStructure);
			LOG.info("table created {}" , i);

			SQLServerBulkCopy bulkCopy = null;
			ResultSetMetaData rsMetadata;
			SQLServerBulkCSVFileRecord fileRecord = null;

			bulkCopy = new SQLServerBulkCopy(con);
			stmt = con.createStatement();
			ResultSet resultSet = null;

			bulkCopy.setDestinationTableName(
					configurations.getStringProperty(ConfigurationFilesConstants.DATASOURCE_DBCONNDBNAME) + ".dbo."
							+ fileName);
			fileRecord = new SQLServerBulkCSVFileRecord(filePath, null, seperator, false);
			resultSet = stmt.executeQuery("SELECT TOP 0 * from " + fileName);
			rsMetadata = resultSet.getMetaData();
			for (int i1 = 1; i1 <= rsMetadata.getColumnCount(); i1++) {
				fileRecord.addColumnMetadata(i1, null, java.sql.Types.NVARCHAR, 1000, 0);
			}
			try {
				bulkCopy.writeToServer(fileRecord);
				isSuccess = true;
			} catch (Exception e) {
				e.printStackTrace();
				isSuccess = false;
			}

		} catch (SQLException e) {
			LOG.info("exception in creating temp table::::::::::::::::::"+e.getMessage());	
		
		} finally {
			con.close();
			stmt.close();
			st.close();
		}
		return isSuccess;
	}

	
	@SuppressWarnings("resource")
	public boolean CsvBulkUploadNewGenWorkFlowMig(String filePath, String tableName, String fileName)
			throws IOException, SQLException {
		Connection con = getConnection();
		boolean isSuccess = false;
		Statement st = null;
		Statement stmt = null;

		// get the property value and print it out
		String databaseUsername = configurations.getStringProperty(ConfigurationFilesConstants.DATASOURCE_DBCONNUSRNM);
		String databasePassword = configurations.getStringProperty(ConfigurationFilesConstants.DATASOURCE_DBCONNPWD);
		String databaseIp = configurations.getStringProperty(ConfigurationFilesConstants.DATASOURCE_DBCONNIP);
		String databaseName = configurations.getStringProperty(ConfigurationFilesConstants.DATASOURCE_DBCONNDBNAME);

		final String BCP_UTIL_CMD = "bcp ";
		LOG.info(BCP_UTIL_CMD + " [" + databaseName + "].[dbo]." + fileName + " in " + filePath + " -S " + databaseIp
				+ " -U " + databaseUsername + " -P " + databasePassword + " -t\\t -E /c");

		try {

			String queryCopyTableStructure = "SELECT TOP 0 * INTO " + fileName + " FROM " + tableName;
			st = con.createStatement();

			int i = st.executeUpdate(queryCopyTableStructure);
			LOG.info("table created" + i);

			SQLServerBulkCopy bulkCopy = null;
			ResultSetMetaData rsMetadata;
			SQLServerBulkCSVFileRecord fileRecord = null;

			bulkCopy = new SQLServerBulkCopy(con);
			stmt = con.createStatement();
			ResultSet resultSet = null;

			bulkCopy.setDestinationTableName(
					configurations.getStringProperty(ConfigurationFilesConstants.DATASOURCE_DBCONNDBNAME) + ".dbo."
							+ fileName);
			fileRecord = new SQLServerBulkCSVFileRecord(filePath, null, AppConstants.CSV_CHAR_PIPE, false);
			resultSet = stmt.executeQuery("SELECT TOP 0 * from " + fileName);
			rsMetadata = resultSet.getMetaData();
			for (int i1 = 1; i1 <= rsMetadata.getColumnCount(); i1++) {
				fileRecord.addColumnMetadata(i1, null, java.sql.Types.NVARCHAR, 100, 0);
			}
			try {
				bulkCopy.writeToServer(fileRecord);
				isSuccess = true;
			} catch (Exception e) {
				e.printStackTrace();
				isSuccess = false;
			}

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			con.close();
			stmt.close();
			st.close();
		}
		return isSuccess;
	}

	@SuppressWarnings("resource")
	public boolean CsvBulkUploadNewGenWorkFlow(String filePath, String tableName, String fileName)
			throws IOException, SQLException {
		Connection con = getConnection();
		boolean isSuccess = false;
		Statement st = null;
		Statement stmt = null;

		// get the property value and print it out
		String databaseUsername = configurations.getStringProperty(ConfigurationFilesConstants.DATASOURCE_DBCONNUSRNM);
		String databasePassword = configurations.getStringProperty(ConfigurationFilesConstants.DATASOURCE_DBCONNPWD);
		String databaseIp = configurations.getStringProperty(ConfigurationFilesConstants.DATASOURCE_DBCONNIP);
		String databaseName = configurations.getStringProperty(ConfigurationFilesConstants.DATASOURCE_DBCONNDBNAME);

		final String BCP_UTIL_CMD = "bcp ";
		LOG.info(BCP_UTIL_CMD + " [" + databaseName + "].[dbo]." + fileName + " in " + filePath + " -S " + databaseIp
				+ " -U " + databaseUsername + " -P " + databasePassword + " -t\\t -E /c");

		try {

			String queryCopyTableStructure = "SELECT TOP 0 * INTO " + fileName + " FROM " + tableName;
			st = con.createStatement();

			int i = st.executeUpdate(queryCopyTableStructure);
			LOG.info("table created" + i);

			SQLServerBulkCopy bulkCopy = null;
			ResultSetMetaData rsMetadata;
			SQLServerBulkCSVFileRecord fileRecord = null;

			bulkCopy = new SQLServerBulkCopy(con);
			stmt = con.createStatement();
			ResultSet resultSet = null;

			bulkCopy.setDestinationTableName(
					configurations.getStringProperty(ConfigurationFilesConstants.DATASOURCE_DBCONNDBNAME) + ".dbo."
							+ fileName);
			fileRecord = new SQLServerBulkCSVFileRecord(filePath + ".csv", null, AppConstants.CSV_CHAR_PIPE, false);
			resultSet = stmt.executeQuery("SELECT TOP 0 * from " + fileName);
			rsMetadata = resultSet.getMetaData();
			for (int i1 = 1; i1 <= rsMetadata.getColumnCount(); i1++) {
				fileRecord.addColumnMetadata(i1, null, java.sql.Types.NVARCHAR, 100, 0);
			}
			try {
				bulkCopy.writeToServer(fileRecord);
				isSuccess = true;
			} catch (Exception e) {
				e.printStackTrace();
				isSuccess = false;
			}

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			con.close();
			stmt.close();
			st.close();
		}
		return isSuccess;
	}

	public void ftpFileUpload(String botStoreRootFolder, String requestId, String folderNameForFileType,
			MultipartFile file) {

		FTPClient client = new FTPClient();
		// FileInputStream fis = null;

		try {
			client.connect(configurations.getStringProperty(ConfigurationFilesConstants.SERVER_IP));
			client.login(configurations.getStringProperty(ConfigurationFilesConstants.USER_NAME),
					configurations.getStringProperty(ConfigurationFilesConstants.USER_PASS));

			client.changeWorkingDirectory("/" + botStoreRootFolder);
			//
			if (client
					.changeWorkingDirectory("/" + botStoreRootFolder + "/" + requestId + "/" + folderNameForFileType)) {
				//
				client.removeDirectory("/" + botStoreRootFolder + "/" + requestId + "/" + folderNameForFileType);
			}
			client.makeDirectory("/" + botStoreRootFolder + "/" + requestId + "/" + folderNameForFileType);
			client.changeWorkingDirectory("/" + botStoreRootFolder + "/" + requestId + "/" + folderNameForFileType);

			client.setFileType(FTP.BINARY_FILE_TYPE);

			// String filename = "a.zip";
			String filename = null;
			if (folderNameForFileType.equalsIgnoreCase("input"))
				filename = "input.zip";
			else if (folderNameForFileType.equalsIgnoreCase("output"))
				filename = "output.zip";
			else if (folderNameForFileType.equalsIgnoreCase("code"))
				filename = "code.zip";
			else if (folderNameForFileType.equalsIgnoreCase("logic"))
				filename = "logic.zip";
			else if (folderNameForFileType.equalsIgnoreCase("exe"))
				filename = "exe.zip";
			else
				filename = file.getOriginalFilename();

			// fis = new FileInputStream("C:/Users/ejangua.ERICSSON/Desktop/a.zip");

			boolean done = client.storeFile(filename, file.getInputStream());

			// fis.close();
			client.logout();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				client.disconnect();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

	}

	@SuppressWarnings("resource")
	public Map<String, Object> CsvBulkUploadNewGenWothoutTmpTable(String filePath, String fileName)
			throws IOException, SQLException {
		Connection con = getConnection();
		boolean isSuccess = false;
		boolean errorFlag = false;
		String msg = "";
		Statement stmt = null;
		Map<String, Object> finalData = new HashMap<String, Object>();
		try {
			SQLServerBulkCopy bulkCopy = null;
			ResultSetMetaData rsMetadata;
			SQLServerBulkCSVFileRecord fileRecord = null;

			bulkCopy = new SQLServerBulkCopy(con);
			stmt = con.createStatement();
			ResultSet resultSet = null;

			bulkCopy.setDestinationTableName(
					configurations.getStringProperty(ConfigurationFilesConstants.DATASOURCE_DBCONNDBNAME)
							+ ".[refData]." + fileName);
			fileRecord = new SQLServerBulkCSVFileRecord(filePath, null, AppConstants.CSV_CHAR_COMMA, false);
			resultSet = stmt.executeQuery("SELECT TOP 0 * from " + "[refData]." + fileName);
			rsMetadata = resultSet.getMetaData();
			for (int i1 = 1; i1 <= rsMetadata.getColumnCount(); i1++) {
				fileRecord.addColumnMetadata(i1, null, java.sql.Types.VARCHAR, 100, 0);
			}
			try {
				bulkCopy.writeToServer(fileRecord);
				isSuccess = true;
				msg = "Successfully Uploaded";
			} catch (Exception e) {
				e.printStackTrace();
				msg = "Problem with header/data in the uploaded file";
				isSuccess = false;
				errorFlag=true;
				finalData.put("Error", msg);
			}

		} catch (SQLException e) {
			msg = e.getMessage();
			errorFlag=true;
			finalData.put("Error", msg);
			e.printStackTrace();
		} finally {
			con.close();
			stmt.close();
		}
		
		finalData.put("msg", msg);
		finalData.put("isUploadError", isSuccess);
		finalData.put("ErrorFlag", errorFlag);
		return finalData;
	}

	public boolean ftpFileUploadForErisite(String baseFolder, byte[] bs, String dirName) {

		FTPClient client = new FTPClient();
		boolean done = false;
		FileOutputStream fileOuputStream = null;

		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS zzz");

		try {
			client.connect(configurations.getStringProperty(ConfigurationFilesConstants.SERVER_IP));
			client.login(configurations.getStringProperty(ConfigurationFilesConstants.USER_NAME),
					configurations.getStringProperty(ConfigurationFilesConstants.USER_PASS));

			client.changeWorkingDirectory("/" + baseFolder + "/");

			String csvFilePath = "/" + baseFolder + "/";

			if (!client.changeWorkingDirectory(csvFilePath)) {
				client.makeDirectory(csvFilePath);
			}
			client.changeWorkingDirectory(csvFilePath);
			FTPFile[] csvFiles = client.listFiles(csvFilePath);
			if (csvFiles.length > 0)
				client.deleteFile(csvFilePath);

			client.setFileType(FTP.BINARY_FILE_TYPE);

			Date dt = new Date();
			Date date = sdf.parse(sdf.format(dt));

			// getTime() returns the number of milliseconds since January 1, 1970, 00:00:00
			// GMT represented by this Date object.
			long epochTime = date.getTime();

			try {
				fileOuputStream = new FileOutputStream(
						System.getProperty("user.dir") + "SAP_BO_" + epochTime + "_" + dirName + ".csv");
				fileOuputStream.write(bs);
			} finally {
				fileOuputStream.close();
			}

			File file = new File(System.getProperty("user.dir") + "SAP_BO_" + epochTime + "_" + dirName + ".csv");

			String filename = file.getName();

			done = client.storeFile(filename, new FileInputStream(file));
			client.logout();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			try {
				client.disconnect();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return done;
	}

	public void ftpFileUploadForJar(String botStoreRootFolder, String requestId, String folderNameForFileType,
			File file) {

		FTPClient client = new FTPClient();

		try {
			client.connect(configurations.getStringProperty(ConfigurationFilesConstants.SERVER_IP));
			client.login(configurations.getStringProperty(ConfigurationFilesConstants.USER_NAME),
					configurations.getStringProperty(ConfigurationFilesConstants.USER_PASS));

			client.changeWorkingDirectory("/" + botStoreRootFolder + "/DeployedBots");
			String jarFilePath = "";
			if (file.getAbsolutePath().contains(".exe")) {
				jarFilePath = "/" + botStoreRootFolder + "/DeployedBots/" + requestId + ".exe";
			} else {
				jarFilePath = "/" + botStoreRootFolder + "/DeployedBots/" + requestId + ".jar";
			}
			FTPFile[] jarFiles = client.listFiles(jarFilePath);
			if (jarFiles.length > 0)
				client.deleteFile(jarFilePath);

			client.setFileType(FTP.BINARY_FILE_TYPE);

			String filename = file.getName();
			if (folderNameForFileType.equalsIgnoreCase("jar"))
				filename = file.getName();

			boolean done = client.storeFile(filename, new FileInputStream(file));
			client.logout();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				client.disconnect();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

	}

	public void ftpFiledownload(String remoteFilePath, String localSystemfile) {

		FTPClient client = new FTPClient();

		try {
			client.connect(configurations.getStringProperty(ConfigurationFilesConstants.SERVER_IP));
			client.login(configurations.getStringProperty(ConfigurationFilesConstants.USER_NAME),
					configurations.getStringProperty(ConfigurationFilesConstants.USER_PASS));

			client.setFileType(FTP.BINARY_FILE_TYPE);

			remoteFilePath = "/dir1/dir2/a.zip";
			File localfile = new File("C:/Users/ejangua.ERICSSON/Desktop/a.zip");
			OutputStream outputStream = new BufferedOutputStream(new FileOutputStream(localfile));
			boolean success = client.retrieveFile(remoteFilePath, outputStream);
			outputStream.close();

		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				client.disconnect();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public InputStream downloadFileAsStream(String remoteFilePath) {

		FTPClient client = new FTPClient();
		InputStream fis = null;

		try {
			client.connect(configurations.getStringProperty(ConfigurationFilesConstants.SERVER_IP));
			client.login(configurations.getStringProperty(ConfigurationFilesConstants.USER_NAME),
					configurations.getStringProperty(ConfigurationFilesConstants.USER_PASS));

			client.setFileType(FTP.BINARY_FILE_TYPE);
			File localfile = new File(remoteFilePath);
			fis = new FileInputStream(localfile);

		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				client.disconnect();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return fis;
	}


	private Connection getConnection() throws SQLException, IOException {
		Connection con = null;

		// get the property value and print it out
		String databaseDriver = configurations.getStringProperty(ConfigurationFilesConstants.JDBC_DATASOURCECLASSNAME);
		String databaseUsername = configurations.getStringProperty(ConfigurationFilesConstants.DATASOURCE_DBCONNUSRNM);
		String databasePassword = configurations.getStringProperty(ConfigurationFilesConstants.DATASOURCE_DBCONNPWD);
		String databaseIp = configurations.getStringProperty(ConfigurationFilesConstants.DATASOURCE_DBCONNIP);
		String databaseName = configurations.getStringProperty(ConfigurationFilesConstants.DATASOURCE_DBCONNDBNAME);
		String databasePort = configurations.getStringProperty(ConfigurationFilesConstants.DATASOURCE_DBPORT);

		String databaseUrl = "jdbc:sqlserver://" + databaseIp + ":" + databasePort + ";databaseName=" + databaseName;

		if (con == null) {
			try {
				Class.forName(databaseDriver);
				return DriverManager.getConnection(databaseUrl, databaseUsername, databasePassword);
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			}

		}
		return con;
	}

	public boolean CsvBulkUploadSAPBOReport(String filePath, String tableName, String fileName, String separator,
			String instance) throws IOException, SQLException {
		Connection con = getConnection();
		boolean isSuccess = false;
		Statement st = null;
		Statement stmt = null;

		try {

			String queryCopyTableStructure = "SELECT TOP 0 * INTO " + fileName + " FROM " + tableName;
			st = con.createStatement();
			int i = st.executeUpdate(queryCopyTableStructure);
			LOG.info("table created" + i);

			SQLServerBulkCopy bulkCopy = null;
			ResultSetMetaData rsMetadata;
			SQLServerBulkCSVFileRecord fileRecord = null;

			bulkCopy = new SQLServerBulkCopy(con);
			stmt = con.createStatement();
			ResultSet resultSet = null;

			bulkCopy.setDestinationTableName(
					configurations.getStringProperty(ConfigurationFilesConstants.DATASOURCE_DBCONNDBNAME) + ".dbo."
							+ fileName);
			fileRecord = new SQLServerBulkCSVFileRecord(filePath, null, separator, true);
			resultSet = stmt.executeQuery("SELECT TOP 0 * from " + fileName);
			rsMetadata = resultSet.getMetaData();
			int columnCount = rsMetadata.getColumnCount();
			for (int i1 = 1; i1 <= columnCount; i1++) {
				fileRecord.addColumnMetadata(i1, null, java.sql.Types.NVARCHAR, 1000, 0);
			}
			try {
				bulkCopy.writeToServer(fileRecord);
				isSuccess = true;
			} catch (Exception e) {
				e.printStackTrace();
				isSuccess = false;
			}

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			con.close();
			stmt.close();
			st.close();
		}
		return isSuccess;
	}

	public String getAccessProfileDays() {
		return configurations.getStringProperty(ConfigurationFilesConstants.ACCESSPROFILE_ACCESSDAYS);
	}

	public Map<String, Object> CsvBulkUploadNetworkUpload(String filePath, String tableName, String fileName)
			throws IOException, SQLException {
		Connection con = null;
		String msg = "";
		boolean isUploadError = false;

		Statement stmt = null;
		try {
			con = jdbcConnectionFactory.getPooledConnection();
			String queryCopyTableStructure = "SELECT TOP 0 * INTO " + fileName + " FROM " + tableName;
			stmt = con.createStatement();

			int i = stmt.executeUpdate(queryCopyTableStructure);
			LOG.info("table created" + i);
			String s;
			final String BCP_UTIL_CMD = "bcp ";
			Process p;
			BufferedReader stdInput;
			String ip = configurations.getStringProperty(ConfigurationFilesConstants.DATASOURCE_DBCONNIP);
			String userName = configurations.getStringProperty(ConfigurationFilesConstants.DATASOURCE_DBCONNUSRNM);
			String password = configurations.getStringProperty(ConfigurationFilesConstants.DATASOURCE_DBCONNPWD);
			String databaseName = configurations.getStringProperty(ConfigurationFilesConstants.DATASOURCE_DBCONNDBNAME);
			File file = new File("resources/FormatFileNetworkElement.xml");
			String absolutePath = file.getAbsolutePath();

			String BCP_ERROR_STRING = "BCP copy in failed";

			LOG.info(BCP_UTIL_CMD + " [" + databaseName + "].[dbo]." + fileName + " in " + filePath + " -f "
					+ absolutePath + " -S " + ip + " -U " + userName + " -P " + password + " -t\\t -E /c");

			LOG.info("BCP started started");
			p = Runtime.getRuntime().exec(BCP_UTIL_CMD + " [" + databaseName + "].[dbo]." + fileName + " in " + filePath
					+ " -S " + ip + " -U " + userName + " -P " + password + " -t\\t -E /c");
			stdInput = new BufferedReader(new InputStreamReader(p.getInputStream()), 8 * 1024);

			while ((s = stdInput.readLine()) != null) {
				if (s.startsWith("Error =") || s.contains(BCP_ERROR_STRING)) {

					isUploadError = true;
					break;
				}
			}

			if (isUploadError) {
				msg = "Problem with header/data in the uploaded file";
			} else {
				msg = "Successfully Uploaded";
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		finally {
			if (con != null) {
				con.close();
			}
			if (stmt != null) {
				stmt.close();
			}
		}
		Map<String, Object> finalData = new HashMap<String, Object>();
		finalData.put("msg", msg);
		finalData.put("isUploadError", isUploadError);
		return finalData;
	}

	public void dropTable(String fileName) throws SQLException {

		Connection con = null;
		con = jdbcConnectionFactory.getPooledConnection();
		Statement stmt = null;

		try {
			stmt = con.createStatement();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			stmt.executeUpdate("DROP TABLE " + fileName);
			if (con != null) {
				con.close();
			}
			if (stmt != null) {
				stmt.close();
			}
		}

	}

	public String bcpExportToCSV(String filePath, String query, String fileName) throws IOException, SQLException {

		String s;
		final String BCP_UTIL_CMD = "bcp ";
		Process p;
		BufferedReader stdInput;
		String ip = configurations.getStringProperty(ConfigurationFilesConstants.DATASOURCE_DBCONNIP);
		String userName = configurations.getStringProperty(ConfigurationFilesConstants.DATASOURCE_DBCONNUSRNM);
		String password = configurations.getStringProperty(ConfigurationFilesConstants.DATASOURCE_DBCONNPWD);
		String databaseName = configurations.getStringProperty(ConfigurationFilesConstants.DATASOURCE_DBCONNDBNAME);

		String BCP_ERROR_STRING = "failed";

		try {
			LOG.info(BCP_UTIL_CMD + "\"" + query + "\"" + " queryout " + filePath + "/" + fileName + " /c -t\\t  -d "
					+ databaseName + " -S " + ip + " -U " + userName + " -P " + password);

			LOG.info("BCP started");
			p = Runtime.getRuntime().exec(BCP_UTIL_CMD + "\"" + query + "\"" + " queryout " + filePath + "/" + fileName
					+ " /c -t\\t  -d " + databaseName + " -S " + ip + " -U " + userName + " -P " + password);

			LOG.info("BCP Export done");
			stdInput = new BufferedReader(new InputStreamReader(p.getInputStream()), 8 * 1024);
			boolean isUploadError = false;

			while ((s = stdInput.readLine()) != null) {
				if (s.contains("Error =") || s.contains(BCP_ERROR_STRING)) {
					LOG.info("BCP Upload filed, ERROR:" + s);
					isUploadError = true;
				}
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return filePath + "/" + fileName;

	}

	public void CsvBulkUpload(String filePath, String tableName, String fileName) throws IOException, SQLException {
		Connection con = null;

		Statement stmt = null;
		try {
			con = jdbcConnectionFactory.getPooledConnection();
			String queryCopyTableStructure = "SELECT TOP 0 * INTO " + fileName + " FROM " + tableName;
			stmt = con.createStatement();

			int i = stmt.executeUpdate(queryCopyTableStructure);
			LOG.info("table created" + i);
			String s;
			final String BCP_UTIL_CMD = "bcp ";
			Process p;
			BufferedReader stdInput;
			String ip = configurations.getStringProperty(ConfigurationFilesConstants.DATASOURCE_DBCONNIP);
			String userName = configurations.getStringProperty(ConfigurationFilesConstants.DATASOURCE_DBCONNUSRNM);
			String password = configurations.getStringProperty(ConfigurationFilesConstants.DATASOURCE_DBCONNPWD);
			String databaseName = configurations.getStringProperty(ConfigurationFilesConstants.DATASOURCE_DBCONNDBNAME);

			String BCP_ERROR_STRING = "BCP copy in failed";

			LOG.info(BCP_UTIL_CMD + " [" + databaseName + "].[dbo]." + fileName + " in " + filePath + " -S " + ip
					+ " -U " + userName + " -P " + password + " -t\\t -E /c");

			LOG.info("BCP started started");
			p = Runtime.getRuntime().exec(BCP_UTIL_CMD + " [" + databaseName + "].[dbo]." + " in " + filePath + " -S "
					+ ip + " -U " + userName + " -P " + password + " -t\\t -E /c");

			stdInput = new BufferedReader(new InputStreamReader(p.getInputStream()), 8 * 1024);
			boolean isUploadError = false;

			while ((s = stdInput.readLine()) != null) {
				if (s.startsWith("Error =") || s.contains(BCP_ERROR_STRING)) {

					isUploadError = true;
					break;
				}
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		finally {
			if (con != null) {
				con.close();
			}
			if (stmt != null) {
				stmt.close();
			}
		}
	}

	@Transactional("transactionManager")
	public Response<DbJobsModel> jdbcConnection(ServiceStartStopModel serviceStartStopModel) throws IOException, SQLException {
		String message="Success";
		Response<DbJobsModel> apiResponse = new Response<>();
        DbJobsModel dbJobModels=new DbJobsModel();
		try (Connection conn=getConnection();
				CallableStatement cs = conn.prepareCall("{call dbo.usp_executejob(?)}")) {
		    boolean resultSetAvailable = false;
		    int numberOfResultsProcessed = 0;
		    try {
		    	cs.setString(1, serviceStartStopModel.getProcedureName());
		    	cs.setQueryTimeout(60*serviceStartStopModel.getQueryTimeout());
		        resultSetAvailable = cs.execute();
		    } catch (SQLServerException sse) {
		    	message="Exception thrown on execute: %s%n%n"+ sse.getMessage();
		        numberOfResultsProcessed++;
		    }
		    int updateCount = -2;  // initialize to impossible(?) value
		    while (true) {
		        boolean exceptionOccurred = true; 
		        do {
		            try {
		                if (numberOfResultsProcessed > 0) {
		                    resultSetAvailable = cs.getMoreResults();
		                }
		                exceptionOccurred = false;
		                updateCount = cs.getUpdateCount();
		            } catch (SQLServerException sse) {
		            	message="Current result is an exception: %s%n%n"+ sse.getMessage();
		         
		            }
		            numberOfResultsProcessed++;
		        } while (exceptionOccurred);

		        if ((!resultSetAvailable) && (updateCount == -1)) {
		            break;  // we're done
		        }
		        
		        if (resultSetAvailable) {
		            
		            try (ResultSet resultSet = cs.getResultSet()) {
		                try {
		                    while (resultSet.next()) {
		                    	dbJobModels.setErrorCode(resultSet.getInt(1));
		                    	dbJobModels.setErrorMsg(resultSet.getString(2));
		                    }
		                    apiResponse.setResponseData(dbJobModels);
		                } catch (SQLServerException sse) {
		                	message="Exception while processing ResultSet: %s%n"+sse.getMessage();
		                	apiResponse.addFormError(message);
		                 }
		            }
				} 
		       
		    }
		    
		}
		return apiResponse;
	}
	
	@Transactional("transactionManager")
	public Response<DbJobsModel> jdbcConnectionStopJob(ServiceStartStopModel serviceStartStopModel) throws IOException, SQLException {
		String message="Success";
		Response<DbJobsModel> apiResponse = new Response<>();
        DbJobsModel dbJobModels=new DbJobsModel();
		try (Connection conn=getConnection();
				CallableStatement cs = conn.prepareCall("{call dbo.usp_stopjob(?)}")) {
		    boolean resultSetAvailable = false;
		    int numberOfResultsProcessed = 0;
		    try {
		    	cs.setString(1, serviceStartStopModel.getProcedureName());
		    	cs.setQueryTimeout(60*5);
		        resultSetAvailable = cs.execute();
		    } catch (SQLServerException sse) {
		    	message="Exception thrown on execute: %s%n%n"+ sse.getMessage();
		        numberOfResultsProcessed++;
		    }
		    int updateCount = -2;  // initialize to impossible(?) value
		    while (true) {
		        boolean exceptionOccurred = true; 
		        do {
		            try {
		                if (numberOfResultsProcessed > 0) {
		                    resultSetAvailable = cs.getMoreResults();
		                }
		                exceptionOccurred = false;
		                updateCount = cs.getUpdateCount();
		            } catch (SQLServerException sse) {
		            	message="Current result is an exception: %s%n%n"+ sse.getMessage();
		         
		            }
		            numberOfResultsProcessed++;
		        } while (exceptionOccurred);

		        if ((!resultSetAvailable) && (updateCount == -1)) {
		            break;  // we're done
		        }
		        
		        if (resultSetAvailable) {
		            
		            try (ResultSet resultSet = cs.getResultSet()) {
		                try {
		                    while (resultSet.next()) {
		                    	dbJobModels.setErrorCode(resultSet.getInt(1));
		                    	dbJobModels.setErrorMsg(resultSet.getString(2));
		                    }
		                    apiResponse.setResponseData(dbJobModels);
		                } catch (SQLServerException sse) {
		                	message="Exception while processing ResultSet: %s%n"+sse.getMessage();
		                	apiResponse.addFormError(message);
		                 }
		            }
				} 
		       
		    }
		    
		}
		return apiResponse;
	}

	
	public void sendMailforStorageKeyInvalidation(String bodyData) {

		try {
			Map<String, Object> placeHolder = enrichMailforStorageKeyInvalidation(bodyData);
			emailService.sendMail(AppConstants.GENERIC_TEMPLATE_WITHOUT_TABLE_BODY, placeHolder);

		} catch (Exception ex) {
			ex.printStackTrace();
		}

	}
	
	public Map<String, Object> enrichMailforStorageKeyInvalidation(String bodyData) {
		
		Map<String, Object> data = new HashMap<String, Object>();
		GenericMailModel genericMailModel = new GenericMailModel("Storage key 1 is found invalid",
				StringUtils.EMPTY, bodyData);
		data.put(AppConstants.GENERIC_MAIL_MODEL, genericMailModel);
		data.put(AppConstants.SUBJECT, "Storage key 1 is found invalid");
		data.put(AppConstants.CUSTOM_NOTIFICATIONS_TO,
				configurations.getStringProperty(ConfigurationFilesConstants.ISF_SUPPORT_MAIL));
		data.put(AppConstants.PAGE_LINK_STRING, StringUtils.EMPTY);
		return data;

	}
	
	 public SignalrModel returnSignalrConfiguration(Object payload, String validMethod) {
			SignalrModel signalRModel = new SignalrModel();
			signalRModel.setHubName(configurations.getStringProperty(ConfigurationFilesConstants.HUB_NAME));
			//signalRModel.setHubUrl(configurations.getStringProperty(ConfigurationFilesConstants.HUB_URL));
			//signalRModel.setHubUrl(validationUtilityService.getCurrentEnvironment("BaseUrl"));
			signalRModel.setHubUrl(System.getenv("CONFIG_BASE_URL"));
			signalRModel.setMethodName(validMethod);
			signalRModel.setExecutionType(AppConstants.SIGNALR_EXECUTION_TYPE);
			signalRModel.setPayload(payload);
			return signalRModel;
		}
	 
	 public void callSignalrApplicationToCallSignalRHub(SignalrModel signalRModel) {
			new Thread(new Runnable() {
				@Override
				public void run() {
					try {
						connectSignalrApplication(signalRModel);
					} catch (Exception e) {
						LOG.debug(String.format("Exception thrown  %s", e.getMessage()));
					}

				}
			}).start();

		}
	 
	 public void connectSignalrApplication(SignalrModel signalRModel) throws org.apache.http.ParseException, IOException {
		    
			RestTemplate restTemplate = new RestTemplate();
			String url = configurations.getStringProperty(ConfigurationFilesConstants.SIGNALR_APPLICATION_URL)
					+ AppConstants.CALL_SIGNALR_API;
			String res = StringUtils.EMPTY;
			LOG.info("url called============:" + url);
			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(MediaType.APPLICATION_JSON);
			HttpEntity<SignalrModel> request = new HttpEntity<>(signalRModel, headers);
			
			try {
				LOG.info("\nSignalR Request : " + request.getBody());
				res = restTemplate.postForObject(url, request, String.class);
				System.out.println("SignalR Response : " + res);
			} catch (RestClientException ex) {
				LOG.debug(String.format("Exception thrown  %s", ex.getMessage()));
				res = ex.getMessage();
			}
		}
	@SuppressWarnings("resource")
	public Map<String, Object> CsvBulkUploadForLeave(String filePath, String tableName, String fileName, String seperator)
			throws IOException, SQLException {
		Connection con = getConnection();
		boolean isSuccess = false;
		boolean errorFlag = false;
		String msg = "";
		Statement st = null;
		Statement stmt = null;

		Map<String, Object> finalData = new HashMap<String, Object>();
		try {

			String queryCopyTableStructure = "SELECT TOP 0 * INTO " + fileName + " FROM " + tableName;
			st = con.createStatement();

			int i = st.executeUpdate(queryCopyTableStructure);
			LOG.info("table created" + i);

			SQLServerBulkCopy bulkCopy = null;
			ResultSetMetaData rsMetadata;
			SQLServerBulkCSVFileRecord fileRecord = null;

			bulkCopy = new SQLServerBulkCopy(con);
			stmt = con.createStatement();
			ResultSet resultSet = null;

			bulkCopy.setDestinationTableName(
					configurations.getStringProperty(ConfigurationFilesConstants.DATASOURCE_DBCONNDBNAME) + ".dbo."
							+ fileName);

			SQLServerBulkCopyOptions bulk =	bulkCopy.getBulkCopyOptions();
			
			String timeOut = this.getBulkCopyTimeout();
			bulk.setBulkCopyTimeout(Integer.parseInt(timeOut));
			
			bulkCopy.setBulkCopyOptions(bulk);
			
			fileRecord = new SQLServerBulkCSVFileRecord(filePath, null, seperator, false);
			resultSet = stmt.executeQuery("SELECT TOP 0 * from " + fileName);
			rsMetadata = resultSet.getMetaData();
			for (int i1 = 1; i1 <= rsMetadata.getColumnCount(); i1++) {
				fileRecord.addColumnMetadata(i1, null, java.sql.Types.NVARCHAR, 1000, 0);
			}
			try {
				LOG.info("File upload started !!");
				bulkCopy.writeToServer(fileRecord);
				isSuccess = true;
				errorFlag=false;
				msg = "File uploaded successfully !!";
				LOG.info("File uploaded successfully !!");
			} catch (Exception e) {
				e.printStackTrace();
				msg = "Problem with header/data in the uploaded file";
				System.out.println("Error msg : " + e.getMessage());
				if(e.getMessage().contains("The query has timed out")) {
					msg = "The query has timed out issue due to large file";
				} else if (e.getMessage().contains("Source data does not match source schema")) {
					msg = "Source data does not match source schema error, due to invalid data in some column";					
				}
				isSuccess = false;
				errorFlag=true;
				finalData.put("error", msg);
				LOG.info("error: ", e.getMessage());
			}
			fileRecord.close();
		} catch (SQLException e) {
			msg = e.getMessage();
			errorFlag=true;
			isSuccess = false;
			finalData.put("error", msg);
			e.printStackTrace();
			LOG.info("error: ", msg);

		} finally {
			con.close();
			stmt.close();
			st.close();
		}
		finalData.put("msg", msg);
		finalData.put("isUploadSuccess", isSuccess);
		finalData.put("errorFlag", errorFlag);
		return finalData;
	}

	/**
	 * @author ekmbuma
	 * @implNote To get timeout for Bulk Mss leave upload from app.properties
	 * 
	 * @return String
	 */
	public String getBulkCopyTimeout() {
		
		return configurations.getStringProperty(ConfigurationFilesConstants.BULK_MSS_LEAVE_QUERY_TIMEOUT);
	}
	
	public ProficiencyTypeModal getProficiencyID(String proficiencyName) {
		return appUtilDAO.getProficiencyID(proficiencyName);
	}
	
	public int getCountryCustomerGroupIDByProjectID(int projectID) {
		return appUtilDAO.getCountryCustomerGroupIDByProjectID(projectID);
	}
}


