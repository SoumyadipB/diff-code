package com.ericsson.isf.config;

import java.io.File;

import javax.servlet.ServletContext;
import javax.sql.DataSource;

import org.apache.commons.lang.StringUtils;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.type.DateTypeHandler;
import org.apache.ibatis.type.TypeHandlerRegistry;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.jdbc.datasource.LazyConnectionDataSourceProxy;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import com.ericsson.isf.service.ExternalInterfaceManagmentService;
import com.ericsson.isf.util.ConfigurationFilesConstants;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

@Configuration
@EnableTransactionManagement
public class DataSourceConfig {
	
	@Autowired
    private ApplicationConfigurations configurations;
	
	@Autowired
	private ServletContext servletContext;
	
//	@Value(value = "${environment.os}")
//	private String operatingSystem;
	
	private static final org.slf4j.Logger LOG = LoggerFactory.getLogger(DataSourceConfig.class);
	
	@Bean(destroyMethod = "close")
	public HikariDataSource hikariDataSource() {
		HikariConfig config = getHikariConfig();
		config.setDataSourceClassName(configurations.getStringProperty(ConfigurationFilesConstants.JDBC_DATASOURCECLASSNAME));
        return new HikariDataSource(config);
	}
	private HikariConfig getHikariConfig() {
		HikariConfig config = new HikariConfig();
		config.setMinimumIdle(configurations.getIntegerProperty(ConfigurationFilesConstants.JDBC_MINIMUMIDLE));
        config.setMaximumPoolSize(configurations.getIntegerProperty(ConfigurationFilesConstants.JDBC_MAXIMUMPOOLSIZE));
        config.setConnectionTimeout(configurations.getIntegerProperty(ConfigurationFilesConstants.JDBC_CONNECTIONTIMEOUT));
        config.setAutoCommit(configurations.getBooleanProperty(ConfigurationFilesConstants.JDBC_AUTOCOMMIT));


        // for SQL Server
        String env=getEnvironment();
        if(StringUtils.isNotBlank(env)) {
        String operatingSystem=System.getenv("CONFIG_OS");
        LOG.info("Reading CONFIG_OS from Server "+operatingSystem);
        if(StringUtils.equalsIgnoreCase(operatingSystem,"Windows")) {
        String dbConnIp=System.getenv("SQLCONNSTR"+"_"+env+"_"+"DBCONNECTIONIP");
        String dbPort=System.getenv("SQLCONNSTR"+"_"+env+"_"+"DBPORT");
        String dbName=System.getenv("SQLCONNSTR"+"_"+env+"_"+"DBNAME");
        String	dbConnectionString="jdbc:sqlserver://"+dbConnIp+ ":"+dbPort+";databaseName="+dbName;
        LOG.info("DB Connection String " +dbConnectionString);
        //String dbConnectionString = System.getenv(dbConnString);
		config.addDataSourceProperty("url",dbConnectionString);
		LOG.info("Connecting to Azure App Service Connection String Database " +dbConnectionString);
        }
        else {
        	connectWithLocalConfig(config);
        }
        }
        else {
        	connectWithLocalConfig(config);
        }
        config.addDataSourceProperty("user", configurations.getStringProperty(ConfigurationFilesConstants.DATASOURCE_DBCONNUSRNM));
		config.addDataSourceProperty("password", configurations.getStringProperty(ConfigurationFilesConstants.DATASOURCE_DBCONNPWD));
        return config;
	}
	private void connectWithLocalConfig(HikariConfig config) {
		config.addDataSourceProperty("url",
				"jdbc:sqlserver://" + configurations.getStringProperty(ConfigurationFilesConstants.DATASOURCE_DBCONNIP) + ":"
						+ ((configurations.getStringProperty(ConfigurationFilesConstants.DATASOURCE_DBPORT) != null)
								? configurations.getStringProperty(ConfigurationFilesConstants.DATASOURCE_DBPORT)
								: 1433)
						+ ";databaseName=" + configurations.getStringProperty(ConfigurationFilesConstants.DATASOURCE_DBCONNDBNAME));
		LOG.info("Connecting to Local Config Database ");
	}

	private String getEnvironment() {
		String splitArray[] = new File(servletContext.getRealPath("/")).getName().split("-java");
		if (splitArray.length == 1) {
			return StringUtils.EMPTY;
		} else {
			return splitArray[1].substring(1, splitArray[1].length());
		}
	}
	@Bean
	public DataSource dataSource() {
	    return new LazyConnectionDataSourceProxy(hikariDataSource());
	}

	@Bean
	public DataSourceTransactionManager transactionManager() {
		DataSourceTransactionManager dataSourceTransactionManager = new DataSourceTransactionManager(dataSource());
		return dataSourceTransactionManager;
	}

	@Bean
	public SqlSessionFactory sqlSessionFactory() throws Exception {
		SqlSessionFactoryBean sqlSessionFactory = new SqlSessionFactoryBean();
		sqlSessionFactory.setDataSource(dataSource());
		PathMatchingResourcePatternResolver resourcePatternResolver = new PathMatchingResourcePatternResolver();
		DefaultResourceLoader defaultResourceLoader = new DefaultResourceLoader();
		sqlSessionFactory.setConfigLocation(defaultResourceLoader.getResource("classpath:mybatis-configuration.xml"));
		sqlSessionFactory.setMapperLocations(resourcePatternResolver.getResources("classpath*:mapper/**/*.xml"));
		
		TypeHandlerRegistry typeHandlerRegistry = sqlSessionFactory.getObject().getConfiguration().getTypeHandlerRegistry();
		typeHandlerRegistry.register(java.sql.Timestamp.class, DateTypeHandler.class);
		typeHandlerRegistry.register(java.sql.Time.class, DateTypeHandler.class);
		typeHandlerRegistry.register(java.sql.Date.class, DateTypeHandler.class);
		
		return sqlSessionFactory.getObject();
	}

	@Bean(destroyMethod = "clearCache")
	public SqlSessionTemplate sqlSession(SqlSessionFactory sqlSessionFactory) {
		SqlSessionTemplate sessionTemplate = new SqlSessionTemplate(sqlSessionFactory);
		return sessionTemplate;
	}
}
