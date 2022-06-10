package com.ericsson.isf.test.config;

import java.util.Properties;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.orm.hibernate5.HibernateTransactionManager;
import org.springframework.orm.hibernate5.LocalSessionFactoryBuilder;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import com.ericsson.isf.config.ApplicationConfigurations;
import com.ericsson.isf.util.ConfigurationFilesConstants;

/**
*
* @author esaabeh
*/

@Configuration
@EnableTransactionManagement
public class HibernateConfigurationTest {
	
	@Autowired
    private ApplicationConfigurations configurations;
	
	@Bean(name="ds")
	public DriverManagerDataSource dS(){

		DriverManagerDataSource ds = new DriverManagerDataSource();
		ds.setDriverClassName(configurations.getStringProperty(ConfigurationFilesConstants.JDBC_DRIVERCLASSNAME));
		ds.setUrl("jdbc:sqlserver://"+configurations.getStringProperty(ConfigurationFilesConstants.DATASOURCE_DBCONNIP)+
				":"+configurations.getStringProperty(ConfigurationFilesConstants.DATASOURCE_DBPORT)+";databaseName="
				+configurations.getStringProperty(ConfigurationFilesConstants.DATASOURCE_DBCONNDBNAME));
		ds.setUsername(configurations.getStringProperty(ConfigurationFilesConstants.DATASOURCE_DBCONNUSRNM));
		ds.setPassword(configurations.getStringProperty(ConfigurationFilesConstants.DATASOURCE_DBCONNPWD));

		//Connection pooling changes for datasources:
		Properties propCon= new Properties();
		propCon.put("maximumPoolSize", configurations.getStringProperty(ConfigurationFilesConstants.JDBC_MAXIMUMPOOLSIZE));
		propCon.put("minimumPoolSize", configurations.getStringProperty(ConfigurationFilesConstants.JDBC_MINIMUMPOOLSIZE));
		propCon.put("minimumIdle", configurations.getStringProperty(ConfigurationFilesConstants.JDBC_MINIMUMIDLE));
		ds.setConnectionProperties(propCon);
		
		return ds;
	}


	private Properties getHibernateProperties() {
        Properties prop = new Properties();
        
        prop.put(ConfigurationFilesConstants.HIBERNATE_FORMAT_SQL, configurations.getStringProperty(ConfigurationFilesConstants.HIBERNATE_FORMAT_SQL));
        prop.put(ConfigurationFilesConstants.HIBERNATE_SHOW_SQL, configurations.getStringProperty(ConfigurationFilesConstants.HIBERNATE_SHOW_SQL));
        prop.put(ConfigurationFilesConstants.HIBERNATE_DIALECT, configurations.getStringProperty(ConfigurationFilesConstants.HIBERNATE_DIALECT));
        
        prop.put(ConfigurationFilesConstants.HIBERNATE_C3P0_MIN_SIZE, configurations.getStringProperty(ConfigurationFilesConstants.HIBERNATE_C3P0_MIN_SIZE));
        prop.put(ConfigurationFilesConstants.HIBERNATE_C3P0_MAX_SIZE, configurations.getStringProperty(ConfigurationFilesConstants.HIBERNATE_C3P0_MAX_SIZE));
        prop.put(ConfigurationFilesConstants.HIBERNATE_C3P0_ACQUIRE_INCREMENT, configurations.getStringProperty(ConfigurationFilesConstants.HIBERNATE_C3P0_ACQUIRE_INCREMENT));
        prop.put(ConfigurationFilesConstants.HIBERNATE_C3P0_TIMEOUT, configurations.getStringProperty(ConfigurationFilesConstants.HIBERNATE_C3P0_TIMEOUT));
        prop.put(ConfigurationFilesConstants.HIBERNATE_C3P0_MAX_STATEMENTS, configurations.getStringProperty(ConfigurationFilesConstants.HIBERNATE_C3P0_MAX_STATEMENTS));
        prop.put(ConfigurationFilesConstants.HIBERNATE_QUERY_PLAN_CACHE_MAX_SIZE, configurations.getStringProperty(ConfigurationFilesConstants.HIBERNATE_QUERY_PLAN_CACHE_MAX_SIZE));
        prop.put(ConfigurationFilesConstants.HIBERNATE_QUERY_PLAN_PARAMETER_METADATA_MAX_SIZE, configurations.getStringProperty(ConfigurationFilesConstants.HIBERNATE_QUERY_PLAN_PARAMETER_METADATA_MAX_SIZE));
        
        return prop;
    }

	@Bean
    public SessionFactory sessionFactory() {
        LocalSessionFactoryBuilder builder = new LocalSessionFactoryBuilder(dS());
        builder
        	.scanPackages("com.ericsson.isf.*")
            .addProperties(getHibernateProperties());

        return builder.buildSessionFactory();
	}

	//Create a transaction manager
	@Bean
    public HibernateTransactionManager txManager() {
		return new HibernateTransactionManager(sessionFactory());
	}

}
