package com.ericsson.isf.util;

import org.springframework.beans.factory.annotation.Value;

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
	
	// signalr Properties
	public static final String HUB_NAME = "isf.signalR.hub.name";
	//We are now taking HUB_URL value from database
	//public static final String HUB_URL = "isf.signalR.hub.url";
	public static final String HUB_METHOD_NAME = "isf.signalR.hub.method.name";
	public static final String SIGNALR_APPLICATION_URL = "isf.signalR.app.url";
	public static final String ADHOC_HUB_METHOD_NAME="isf.signalR.hub.adhoc.method.name";
	public static final String WO_COMPLETION_HUB_METHOD_NAME="isf.signalR.hub.wocompletion.method.name";

	// kafka properties
	public static final String KAFKA_APPLICATION_URL = "isf.kafka.send.grid.url";
	public static final String SEND_KAFKA_MAIL = "send.kafka.mail";
	
	//smtp mail properties
	public static final String SMTP_MAIL_URL = "mail.smtp.host";
	public static final String SMTP_MAIL_USER = "mail.user";
	public static final String SMTP_MAIL_PASSWORD = "mail.password";
	public static final String SMTP_MAIL_STARTTLS = "mail.smtp.starttls.enable";
	public static final String SMTP_MAIL_PORT = "mail.smtp.port";
	public static final String SMTP_MAIL_AUTH = "mail.smtp.auth";

	public static final String EVENT_PUB_EXT_URL = "isf.event.pubisher.ext.url";

	// Environment properties
	public static final String CURRENT_ENV = "isf.current.env";
	public static final String CURRENT_ENV_URL = "isf.current.env.url";
	
	// BotStore properties
	public static final String BOT_STORE_CONTAINER="bot.store.container";
	
	// Key-Vault properties
	public static final String AZURE_KEYVAULT_NAME = "azure.keyvault.name";
	
	// APIm-user properties
	public static final String API_MANAGER_SERVICE = "api.manager.service";
	public static final String API_MANAGER_SUBSCRIPTION_ID = "api.manager.subscription.id";
	public static final String API_MANAGER_RESOURCE_GROUP = "api.manager.resource.group";
	
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
	public static final String NAME = "name";

	// Hibernate JDBC Properties
	public static final String HIBERNATE_DIALECT = "hibernate.dialect";
	public static final String HIBERNATE_C3P0_MIN_SIZE = "hibernate.c3p0.min_size";
	public static final String HIBERNATE_C3P0_MAX_SIZE = "hibernate.c3p0.max_size";
	public static final String HIBERNATE_C3P0_TIMEOUT = "hibernate.c3p0.timeout";
	public static final String HIBERNATE_C3P0_MAX_STATEMENTS = "hibernate.c3p0.max_statements";
	public static final String HIBERNATE_QUERY_PLAN_CACHE_MAX_SIZE = "hibernate.query.plan_cache_max_size";
	public static final String HIBERNATE_QUERY_PLAN_PARAMETER_METADATA_MAX_SIZE = "hibernate.query.plan_parameter_metadata_max_size";
	public static final String HIBERNATE_C3P0_ACQUIRE_INCREMENT = "hibernate.c3p0.acquire_increment";
	public static final String TYPE = "jdbc";
	public static final String QUERY_TIMEOUT_TIME = "10";
	/* database.properties constants starts */
	
	// redis cache Properties
	public static final String REDIS_HOST_NAME = "isf.redis.host.name";
	public static final String REDIS_PORT = "isf.redis.port";
	public static final String REDIS_PASSWORD = "isf.redis.password";
	public static final String REDIS_USE_SSL = "isf.redis.usessl";
	public static final String REDIS_ISENABLE = "isf.redis.isEnable";
    public static final String JEDIS_POOL_MAX_IDLE = "isf.jedis.pool.max.Idle";
	public static final String JEDIS_POOL_MAX_TOTAL="isf.jedis.pool.max.total";
	public static final String MAX_WAIT_MILLIS = "isf.jedis.max.wait.millis";
	public static final String MIN_IDLE = "isf.jedis.min.Idle";
	public static final String BULK_MSS_LEAVE_QUERY_TIMEOUT = "bulk.mss.leave.query.timeout";
	
	// Erisite
//	public static final String ERISITE_SAPBO_BASEFOLDER = "erisite.sapbo.basefolder";
//	public static final String ERISITE_REVERSE_URL = "erisite.reverse.url";
//	public static final String ERISITE_HEADERS_CONSUMERINTERFACEID = "erisite.headers.consumerinterfaceid";
//	public static final String ERISITE_HEADERS_PROVIDERINTERFACEID = "erisite.headers.providerinterfaceid";
//	public static final String ERISITE_AUTHENTICATION_USERNAME = "erisite.authentication.username";
//	public static final String ERISITE_AUTHENTICATION_PASSWORD = "erisite.authentication.password";
	
	// Erisite WO Closure
	public static final String ERISITE_HEADERS_WOCLOSURE_RECIEVERID = "erisite.3.woclosure.receiverid";

	public static final String ERISITE_HEADERS_WOCLOSURE_INTERFACEID = "erisite.3.woclosure.interfaceid";
	public static final String ERISITE_HEADERS_WOCLOSURE_METHOD = "erisite.3.woclosure.method";
	public static final String ERISITE_HEADERS_WOCLOSURE_DOC_TYPE = "erisite.3.woclosure.documenttype";
	public static final String ERISITE_HEADERS_WOCLOSURE_DOC_NAME_SPACE = "erisite.3.woclosure.documentnamespace";
	public static final String ERISITE_HEADERS_WOCLOSURE_PARTY_ID = "erisite.3.woclosure.partyid";
	public static final String ERISITE_HEADERS_WOCLOSURE_PARTY_TYPE = "erisite.3.woclosure.partytype";
	public static final String ERISITE_HEADERS_WOCLOSURE_MESSAGE_FLOW = "erisite.3.woclosure.messageflow";
	public static final String ERISITE_HEADERS_WOCLOSURE_BUSINESS_ENTITY_TYPE = "erisite.3.woclosure.businessentitytype";
	public static final String ERISITE_HEADERS_WOCLOSURE_MESSAGE_SERVICE_NAME = "erisite.3.woclosure.message.service.name";
	public static final String ERISITE_HEADERS_WOCLOSURE_MESSAGE_REQUEST_TYPE = "erisite.3.woclosure.message.request.type";
	public static final String ERISITE_HEADERS_WOCLOSURE_MESSAGE_SERVICE_TYPE = "erisite.3.woclosure.message.service.type";
	public static final String ERISITE_HEADERS_WOCLOSURE_CONTENT_TYPE = "erisite.3.woclosure.content.type";

	// azure properties
	public static final String AZURE_FUNCTION_BASE_URL = "azure.function.base.url";
	
	// signalR properties
	public static final String TIME_SLEEP = "signalr.time.sleep";
	
	public static final String IS_ERISITE_UP = "erisite.up.status";
	
	public static final String SIGNALR_CLIENT_URL = "signalR.client.url";

	public static final String AZURE_REDIS_KEYVAULT_NAME = "azure.redis.keyvault.name";

	public static final String AZURE_REDIS_CONNECTION_STRING = "azure.redis.connection.key";

	public static final String VIEW_NETWORK_ELEMENT_LENTH = "view.network.element.length";
	
	//WorkOrder Mail
	public static final String WORKORDER_MAIL_ENABLED = "workOrder.mail.enabled";
	
	public static final String FEED_TIME_INTERVAL = "feed.time.interval";
	
	public static final String MQTT_APPLICATION_URL = "isf.mqtt.app.url";

	public static final String CNEDB_EXPIRY_TIMEL = "cnedb.expiry.time";
	
	public static final String COMMON_BLOB_CONTAINER="common.container.name";

	public static final String NETWORK_ELEMENT_MAIL_ENABLED = "network.element.mail.enabled";
	
	public static final String NETWORK_ELEMENT_UPLOAD_LENGTH = "network.element.upload.length";

	public static final String  ENV_NAME="current.env.name";
	
	public static final String PROJECT_QUEUE_DOWNLOAD_LENGTH = "project.queue.download.length";


	
	public static final String NETWORK_ELEMENT_DELETE_COUNT = "network.element.delete.count";
	
	public static final String SERVERBOT_EXECUTION_AMQP_ENABLED = "server.bot.execution.amqp.enabled";
	
	public static final String FLOWCHART_PRECONDITION_CLOSED_DATE = "flowchart.precondition.closed.date";

	public static final String FLOWCHART_PRECONDITION_COUNT = "flowchart.precondition.count";

	public static final String FLOWCHART_PAGE_SIZE = "flowchart.page.size";


	
	
	public static final String MAX_DR_ALLOWED = "delivery.responsible.limit";
	public static final String MAX_MANUAL_TASK_ALLOWED = "manual.step.exe.default.value";
	public static final String MAX_AUTOMATIC_TASK_ALLOWED = "automatic.step.exe.default.value";
	public static final String MAX_NE_ALLOWED_FOR_WO = "network.element.workorder.limit";
	


}
