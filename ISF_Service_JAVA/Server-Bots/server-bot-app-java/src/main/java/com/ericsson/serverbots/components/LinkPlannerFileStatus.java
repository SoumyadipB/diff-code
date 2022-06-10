package com.ericsson.serverbots.components;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class LinkPlannerFileStatus {
	@Value("${planner.file.path}")
	private String plannerFile;
	@Value("${result.file.path}")
	private String resultFile;
	@Value("${final.output.filePath}")
	private String finalOutputFilePath;
	
	private static final Logger log = Logger.getLogger(LinkPlannerFileStatus.class);
	
	public boolean checkifFilePlanned() {
		BufferedReader inputStream = null;
		boolean ifPlanned	=	false;
		try {
			log.info("*******planning status*******");
			String filePath	=	plannerFile;
			File file	=	new File(filePath);
			if (file.isFile() && file.getName().endsWith(".csv")) {
				inputStream = new BufferedReader(new FileReader(file));
                String line;

                while ((line = inputStream.readLine()) != null) {
                	if(line.contains("Site name S1,Site name S2,Latitude S1")) {
                		continue;
                	}
                	String[] linearr	=	line.split(",");
                	if(!checkValidRow(linearr)) {
                		continue;
                	}
                	
                	int arrlength	=	linearr.length;
                	String linkPlanningStatus	=	linearr[arrlength-1];
                	log.info("S1 : "+linearr[0]+" S2 : "+linearr[1]+" status : "+linkPlanningStatus);
                	if(!(linkPlanningStatus.trim().toLowerCase().equals("planned") || linkPlanningStatus.trim().toLowerCase().equals("unplanned"))) {
                		ifPlanned	=	false;
                		break;
                	} else {
                		if(ifResultFileGenerated())
                			ifPlanned	=	true;
                		else
                			ifPlanned	=	false;
                	}
                    System.out.println(line);
                }
			}
		} catch(Exception exception) {
			log.error(exception);
		} finally {
			if (inputStream != null) {
                try {
					inputStream.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
            }
		}
		return ifPlanned;
	}

	public String checkPenguinServiceStatus() {
		BufferedReader inputStream = null;
		boolean ifPlanned	=	false;
		String botSuccessFailMsg=	null;

		try {
			log.info("*******checkPenguinServiceStatus() starts*******");
			String filePath	=	finalOutputFilePath;
			
			System.out.println("Final Log file path-->"+filePath);

			File file	=	new File(filePath);
			//if (file.exists() && file.isFile() && file.getName().endsWith(".log")) {
			if (file.exists() && file.isFile()) {
				inputStream = new BufferedReader(new FileReader(file));
                String line;

                System.out.println("Printing line..");
                while ((line = inputStream.readLine()) != null) {
                    System.out.println("line--->"+line);
                	botSuccessFailMsg= line;
                	break;
                }
			}
		} catch(Exception exception) {
			log.error(exception);
		} finally {
			if (inputStream != null) {
                try {
					inputStream.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
            }
		}
		return botSuccessFailMsg;
	}

	private boolean ifResultFileGenerated() {
		boolean ifFileGenerated	=	false;
		try {
			String fileName	=	resultFile;
			File file	=	new File(fileName);
			if(file.isFile()) {
				ifFileGenerated	=	true;
			}
		} catch(Exception exception) {
			log.error("ifResultFileGenerated : "+exception);
			ifFileGenerated	=	false;
		}
		return ifFileGenerated;
	}
	
	private boolean checkValidRow(String[] linearr) {
		boolean ifValid	=	false;
		try {
			if(!linearr[0].trim().equals("") && !linearr[1].trim().equals("") && !linearr[2].trim().equals("")
					 && !linearr[3].trim().equals("") && !linearr[4].trim().equals("") && !linearr[5].trim().equals("")
					 && !linearr[6].trim().equals("") && !linearr[7].trim().equals("") && !linearr[8].trim().equals("")) {
				ifValid	=	true;
			}
		} catch(Exception exception) {
			log.error("checkValidRow "+exception);
		}
		return ifValid;
	}

}
