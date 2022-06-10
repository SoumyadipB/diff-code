/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ericsson.isf.service;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.ericsson.isf.config.ApplicationConfigurations;
import com.ericsson.isf.util.ConfigurationFilesConstants;
import com.mchange.v2.c3p0.DataSources;

/**
 *
 * @author ekalakr
 */
@Component
public class JdbcConnectionFactory {

	@Autowired
	private ApplicationConfigurations configurations;

	public Connection con = null;
	public DataSource unpooled = null;
	public DataSource pooled = null;

	public Connection getConnection() {
		if (con == null) {
			try {
				String port = configurations.getStringProperty(ConfigurationFilesConstants.DATASOURCE_DBPORT);
				String databaseUrl = "jdbc:sqlserver://"
						+ configurations.getStringProperty(ConfigurationFilesConstants.DATASOURCE_DBCONNIP) + ":"
						+ ((port != null) ? port : 1433) + ";databaseName="
						+ configurations.getStringProperty(ConfigurationFilesConstants.DATASOURCE_DBCONNDBNAME);
				Class.forName(configurations.getStringProperty(ConfigurationFilesConstants.JDBC_DATASOURCECLASSNAME));
				con = DriverManager.getConnection(databaseUrl,
						configurations.getStringProperty(ConfigurationFilesConstants.DATASOURCE_DBCONNUSRNM),
						configurations.getStringProperty(ConfigurationFilesConstants.DATASOURCE_DBCONNPWD));
			} catch (ClassNotFoundException ex) {
				Logger.getLogger(JdbcConnectionFactory.class.getName()).log(Level.SEVERE, null, ex);
			} catch (SQLException ex) {
				Logger.getLogger(JdbcConnectionFactory.class.getName()).log(Level.SEVERE, null, ex);
			}
		}
		return con;
	}

	public Connection getPooledConnection() {
		try {
			if (pooled == null) {
				String port = configurations.getStringProperty(ConfigurationFilesConstants.DATASOURCE_DBPORT);
				String databaseUrl = "jdbc:sqlserver://"
						+ configurations.getStringProperty(ConfigurationFilesConstants.DATASOURCE_DBCONNIP) + ":"
						+ ((port != null) ? port : 1433) + ";databaseName="
						+ configurations.getStringProperty(ConfigurationFilesConstants.DATASOURCE_DBCONNDBNAME);
				Class.forName(configurations.getStringProperty(ConfigurationFilesConstants.JDBC_DATASOURCECLASSNAME));
				unpooled = DataSources.unpooledDataSource(databaseUrl,
						configurations.getStringProperty(ConfigurationFilesConstants.DATASOURCE_DBCONNUSRNM),
						configurations.getStringProperty(ConfigurationFilesConstants.DATASOURCE_DBCONNPWD));
				pooled = DataSources.pooledDataSource(unpooled);
			}
			con = pooled.getConnection();

		} catch (ClassNotFoundException ex) {
			Logger.getLogger(JdbcConnectionFactory.class.getName()).log(Level.SEVERE, null, ex);
		} catch (SQLException ex) {
			Logger.getLogger(JdbcConnectionFactory.class.getName()).log(Level.SEVERE, null, ex);
		}
		return con;
	}

	public void attemptClose(Connection o) {
		try {
			if (o != null) {
				o.close();

			}
		} catch (SQLException ex) {
			Logger.getLogger(JdbcConnectionFactory.class.getName()).log(Level.SEVERE, null, ex);
		}
	}
}
