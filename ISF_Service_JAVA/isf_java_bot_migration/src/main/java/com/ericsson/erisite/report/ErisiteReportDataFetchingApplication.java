package com.ericsson.erisite.report;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

import com.ericsson.erisite.report.service.ErisiteReportDataService;

@SpringBootApplication
@ComponentScan(basePackages = { "com.ericsson.erisite.report" })
@MapperScan("com.ericsson.erisite.report.mapper")
public class ErisiteReportDataFetchingApplication implements CommandLineRunner {

	@Autowired
	private ErisiteReportDataService erisiteReportDataService;

	public static void main(String[] args) {

		SpringApplication.run(ErisiteReportDataFetchingApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {

		//botMigrationService.createBotConfFiles(Integer.parseInt(args[0]), Integer.parseInt(args[1]));
		//botMigrationService.createBotExecutionFiles(args[0], args[1]);
		//botMigrationService.createBotConfFiles();
		//botMigrationService.createServerBotInputs(args[0], args[1]);
//		botMigrationService.createServerBotInputs(args[0], args[1]);
		
		erisiteReportDataService.downloadJsonForReport();
	}

}
