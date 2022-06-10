/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ericsson.isf.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;

import com.ericsson.isf.config.ApplicationConfigurations;
import com.mchange.v2.c3p0.DataSources;


/**
 *
 * @author ekalakr
 */
public class JdbcConnectionFactory {
	
	@Autowired
    private static ApplicationConfigurations configurations;

	public static String databaseDriver;
    public static String databaseUrl;
    public static String databaseUsername;
    public static String databasePassword;
    public static String databaseIp;
    public static String databaseName;
    public static String port;
    public static Connection con = null;
    public static DataSource unpooled = null;
    public static DataSource pooled = null;

    static {
        try {            
            // load a properties file 

            // get the property value and print it out
            String databaseDriver = configurations.getStringProperty(ConfigurationFilesConstants.JDBC_DATASOURCECLASSNAME);
            String databaseUsername = configurations.getStringProperty(ConfigurationFilesConstants.DATASOURCE_DBCONNUSRNM);
            String databasePassword = configurations.getStringProperty(ConfigurationFilesConstants.DATASOURCE_DBCONNPWD);
            String databaseIp = configurations.getStringProperty(ConfigurationFilesConstants.DATASOURCE_DBCONNIP);
            String databaseName = configurations.getStringProperty(ConfigurationFilesConstants.DATASOURCE_DBCONNDBNAME);
            port = configurations.getStringProperty(ConfigurationFilesConstants.DATASOURCE_DBPORT);
            
            databaseUrl = "jdbc:sqlserver://"+databaseIp+":"+((port!=null)?port:1433) +";databaseName="+databaseName;
            

        } catch (Exception ex) {
            Logger.getLogger(JdbcConnectionFactory.class.getName()).log(Level.SEVERE, null, ex);
        } 
    }
    private JdbcConnectionFactory() {
    }

    public static Connection getConnection() {
        if (con == null) {
            try {
                Class.forName(databaseDriver);
                con = DriverManager.getConnection(databaseUrl, databaseUsername, databasePassword);
            } catch (ClassNotFoundException ex) {
                Logger.getLogger(JdbcConnectionFactory.class.getName()).log(Level.SEVERE, null, ex);
            } catch (SQLException ex) {
                Logger.getLogger(JdbcConnectionFactory.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return con;
    }

    public static Connection getPooledConnection() {
        try {
            if (pooled == null) {
                Class.forName(databaseDriver);
                unpooled = DataSources.unpooledDataSource(databaseUrl, databaseUsername, databasePassword);
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
    public static void attemptClose(Connection o) {
        try {
            if (o != null) {
                o.close();
                
            }
        } catch (SQLException ex) {
            Logger.getLogger(JdbcConnectionFactory.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
