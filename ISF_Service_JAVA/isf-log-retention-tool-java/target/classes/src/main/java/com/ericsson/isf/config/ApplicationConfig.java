package com.ericsson.isf.config;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import com.ericsson.isf.constants.EnvironmentVariable;

public class ApplicationConfig {
	public Properties getConfigFile(String args) throws IOException {
		Properties properties = new Properties();
		try (InputStream resourceStream = new FileInputStream(
				args.trim())) {
			try {
				properties.load(resourceStream);

			} catch (Exception e) {
				System.out.println("File not found.");
			}
		}

		return properties;
	}
}
