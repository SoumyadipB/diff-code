package com.ericsson.isf.test.config;

import javax.sql.DataSource;

import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.type.DateTypeHandler;
import org.apache.ibatis.type.TypeHandlerRegistry;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.jdbc.datasource.LazyConnectionDataSourceProxy;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import com.ericsson.isf.config.ApplicationConfigurations;
import com.ericsson.isf.util.ConfigurationFilesConstants;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

@Configuration
@EnableTransactionManagement
public class DataSourceConfigTest {
	
	@Autowired
    private ApplicationConfigurations configurations;
	
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
		config.addDataSourceProperty("url",
				"jdbc:sqlserver://" + configurations.getStringProperty(ConfigurationFilesConstants.DATASOURCE_DBCONNIP) + ":"
						+ ((configurations.getStringProperty(ConfigurationFilesConstants.DATASOURCE_DBPORT) != null)
								? configurations.getStringProperty(ConfigurationFilesConstants.DATASOURCE_DBPORT)
								: 1433)
						+ ";databaseName=" + configurations.getStringProperty(ConfigurationFilesConstants.DATASOURCE_DBCONNDBNAME));
		config.addDataSourceProperty("user", configurations.getStringProperty(ConfigurationFilesConstants.DATASOURCE_DBCONNUSRNM));
		config.addDataSourceProperty("password", configurations.getStringProperty(ConfigurationFilesConstants.DATASOURCE_DBCONNPWD));
        
        return config;
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
