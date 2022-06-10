package com.ericsson.isf.util;

/**
 * This class contains all keys of properties file as constants.
 * 
 * @author eakinhm
 *
 */
public class ConfigurationFilesConstants {

	/* app.properties constants starts */
	// SendGrid
	public static final String MAIL_SENDGRID_KEY = "mail.sendGrid.key";

	// file base folder
	public static final String FILE_PATH = "file.path";
	public static final String FILE_PATH_NEW = "file.path.new";
	public static final String SAMPLE_TEMPLATE_PATH = "sample.template.path";
	public static final String NODE_TEMPLATE_PATH = "node.template.path";
	public static final String WOCREATION_TEMPLATE_PATH = "woCreation.template.path";

	// feedback
	public static final String FEEDBACK_UPLOAD_BASEFOLDER = "feedback.upload.basefolder";

	// FTP
	public static final String SERVER_IP = "server.ip";
	public static final String SERVER_PORT = "server.port";
	public static final String USER_NAME = "user.name";
	public static final String USER_PASS = "user.pass";
	public static final String PASS = "pass";

	// BotStore
	public static final String BOTSTORE_UPLOAD_PATH = "botstore.upload.path";
	public static final String BOTSTORE_SERVERBOTS_SERVER_URL = "botstore.serverbots.server.url";
	public static final String BOTSTORE_RPA_BOT_DOWNLOAD_BASEPATH = "botstore.rpa.bot.download.basepath";
	public static final String BOTSTORE_RPA_BOT_BOTOUTPUT_UPLOAD_BASEPATH = "botstore.rpa.bot.botoutput.upload.basepath";
	public static final String BOTSTORE_UPLOAD_BASEFOLDER = "botstore.upload.basefolder";
	public static final String BOTSTORE_RPA_BOT_DOWNLOAD_BOTPATH = "botstore.rpa.bot.download.botpath";
	public static final String BOTSTORE_RPA_BOT_DOWNLOAD_FTPURL = "botstore.rpa.bot.download.ftpurl";
	public static final String BOTSTORE_RPA_UPLOAD_OUTPUT_IP = "botstore.rpa.upload.output.ip";
	public static final String BOTSTORE_RPA_UPLOAD_OUTPUT_PORT = "botstore.rpa.upload.output.port";
	public static final String BOTSTORE_RPA_UPLOAD_OUTPUT_USER = "botstore.rpa.upload.output.user";
	public static final String BOTSTORE_RPA_UPLOAD_OUTPUT_PWD = "botstore.rpa.upload.output.pwd";
	public static final String BOTSTORE_RPA_UPLOAD_OUTPUT_PATH = "botstore.rpa.upload.output.path";

	// Erisite
	public static final String ERISITE_SAPBO_BASEFOLDER = "erisite.sapbo.basefolder";
	public static final String ERISITE_REVERSE_URL = "erisite.reverse.url";
	public static final String ERISITE_HEADERS_CONSUMERINTERFACEID = "erisite.headers.consumerinterfaceid";
	public static final String ERISITE_HEADERS_PROVIDERINTERFACEID = "erisite.headers.providerinterfaceid";
	public static final String ERISITE_AUTHENTICATION_USERNAME = "erisite.authentication.username";
	public static final String ERISITE_AUTHENTICATION_PASSWORD = "erisite.authentication.password";

	// security
	public static final String APP_SECURITY_ENABLED = "app.security.enabled";

	// auditing
	public static final String AUDITING_ENABLED = "auditing.enabled";
	public static final String AUDITING_THREADCOUNT = "auditing.threadCount";

	// Email
	public static final String EMAIL_SCHEDULER_ENABLED = "email.scheduler.enabled";
	public static final String EMAIL_PASS = "email.pass";
	public static final String EMAIL_MAILBOX = "email.mailbox";
	public static final String EMAIL_SCHEDULER_TIMERINTERVAL = "email.scheduler.timerInterval";
	public static final String EMAIL_SCHEDULER_USERNAME = "email.scheduler.userName";

	// Env
	public static final String DEPLOYED_ENVIRONMENT = "deployed.environment";

	// sapBo
	public static final String SAPBO_SCHEDULER_ENABLED = "sapBo.scheduler.enabled";
	public static final String SAPBO_SCHEDULER_TIMERINTERVAL = "sapbo.scheduler.timerInterval";
	public static final String SAPBO_AMERICAS5 = "sapBo.americas5";
	public static final String SAPBO_AMERICAS11 = "sapBo.americas11";
	public static final String SAPBO_APAC = "sapBo.apac";
	public static final String SAPBO_EMEA = "sapBo.emea";
	public static final String SAPBO_MAILER_ENABLED = "sapBo.mailer.enabled";

	// Logout Time Wait Interval
	public static final String LOGOUT_TIME_WAIT = "logout.time.wait";

	// accessprofile
	public static final String ACCESSPROFILE_ACCESSDAYS = "accessprofile.accessdays";
	
	// ldap properties
	public static final String INITIAL_CONTEXT_FACTORY = "initial.context.factory";
	public static final String LDAP_PROVIDER_URL = "ldap.provider.url";
	public static final String SECURITY_PRINCIPAL = "security.principal";
	public static final String SECURITY_CREDENTIALS = "security.credentials";
	
	//Rest Graph Properties
	public static final String AZURE_APP_NAME = "azure.app.name";
	public static final String ISF_SUPPORT_MAIL = "isf.support.mail";
	public static final String REST_GRAPH_EMAIL_SCHEDULER_ENABLED ="rest.graph.email.scheduler.enabled";
	
	//Bulk WO Creation Support Email
	public static final String BULK_WO_CREATION_SUPPORT_EMAIL_ID ="bulk.wo.creation.support.email.id";
	
	/* app.properties constants ends */

	/* db.properties constants starts */
	// JDBC
	public static final String JDBC_DATASOURCECLASSNAME = "jdbc.dataSourceClassName";
	public static final String JDBC_MINIMUMIDLE = "jdbc.minimumIdle";
	public static final String JDBC_MAXIMUMPOOLSIZE = "jdbc.maximumPoolSize";
	public static final String JDBC_CONNECTIONTIMEOUT = "jdbc.connectionTimeout";
	public static final String JDBC_AUTOCOMMIT = "jdbc.autocommit";

	// datasource
	public static final String DATASOURCE_DBCONNIP = "datasource.dbConnIp";
	public static final String DATASOURCE_DBPORT = "datasource.dbPort";
	public static final String DATASOURCE_DBCONNDBNAME = "datasource.dbConnDbName";
	public static final String DATASOURCE_DBCONNUSRNM = "datasource.dbConnUsrNm";
	public static final String DATASOURCE_DBCONNPWD = "datasource.dbConnPwd";

	/* db.properties constants ends */

	/* database.properties constants starts */
	// Data Source JDBC Properties
	public static final String JDBC_DRIVERCLASSNAME = "jdbc.driverClassName";
	public static final String JDBC_MINIMUMPOOLSIZE = "jdbc.minimumPoolSize";

	// Hibernate Configuration Properties
	public static final String HIBERNATE_SHOW_SQL = "hibernate.show_sql";
	public static final String HIBERNATE_FORMAT_SQL = "hibernate.format_sql";

	// Hibernate JDBC Properties
	public static final String HIBERNATE_DIALECT = "hibernate.dialect";
	public static final String HIBERNATE_C3P0_MIN_SIZE = "hibernate.c3p0.min_size";
	public static final String HIBERNATE_C3P0_MAX_SIZE = "hibernate.c3p0.max_size";
	public static final String HIBERNATE_C3P0_TIMEOUT = "hibernate.c3p0.timeout";
	public static final String HIBERNATE_C3P0_MAX_STATEMENTS = "hibernate.c3p0.max_statements";
	public static final String HIBERNATE_C3P0_ACQUIRE_INCREMENT = "hibernate.c3p0.acquire_increment";
	public static final String TYPE = "jdbc";
	public static final String QUERY_TIMEOUT_TIME = "10";
	/* database.properties constants starts */
}
