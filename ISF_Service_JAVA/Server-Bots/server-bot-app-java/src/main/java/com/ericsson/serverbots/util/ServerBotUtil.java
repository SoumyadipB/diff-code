package com.ericsson.serverbots.util;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.nio.file.Files;
import java.util.Date;

import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

public class ServerBotUtil
{
	String INPATH="";
	String OUTPATH="";
	String MarketName="";
	
	public static void main(String[] args) 
	{

//			String inp=args[0];
//			String op=args[1];
			
		String inp="C:\\ServerBot\\Bot1\\";
		String op="C:\\ServerBot\\BOT2\\";
		
		
		
		
			
		try 
		{
			new ServerBotUtil().doProcess(inp, op);
		}
		catch (Exception e) 
		{
			e.printStackTrace();
		}
	}

	
	public int doProcess(String InPath, String OutPath) throws Exception
	{
		int nCount=-1;
		
		INPATH=InPath;
		OUTPATH=OutPath;
		
		this.ChangePathStyle();
		
		try 
		{
			//Add Market Name in WO_Detail.txt from Market.txt
//			AddMarketInWOFile(INPATH);
//			String woString	=	getWorkOrderString(INPATH);
			String marketServerIP = getMasterVMServerIP();
			if (marketServerIP.equalsIgnoreCase("NA"))
			{
				System.out.println("ERROR:========================================================================");
				System.out.println(MarketName+ " Market in input is not defined in BOT");
            	nCount	=	-1;
			}
			else
			{
				
				
				
				methodToUploadFile( InPath,  OutPath);
//				TestmethodToUploadFile( InPath,  OutPath);
			
			}
			
		} 
		catch(Exception exception) 
		{
			exception.printStackTrace();
		}
		
		System.out.println("--> "+nCount + " Done");
		 return nCount;
	}
	
	
	
	
	
	void TestmethodToUploadFile(String INPATH, String OUTPATH) throws Exception
	{
		
		String woNo="491507";
		String woNo2="491519";
		String botId="571";
		String botId1="773";
		
		
		for(int i=0;i<1;i++)
		{
			
		
				String jsonObj="{"
						+ "signum:erpnisu,"
						+ "woNo:"+i+","
						+ "projectId:348,"
						+ "taskId:127904,"
						+ "botId:"+botId+","
						+ "inpath:abc,"
						+ "nodes:123,"
						+ "outpath:c"
						+  "}";
			
			
			
			
			
			CloseableHttpClient httpClient = HttpClients.createDefault();
			HttpPost uploadFile = new HttpPost("http://10.174.128.118:8080/server-bot-app-java/serverBotExecutor/loadTest");
			MultipartEntityBuilder builder = MultipartEntityBuilder.create();
			builder.addTextBody("botDetail", jsonObj, ContentType.TEXT_PLAIN);
//			File folder = new File(INPATH);
//			if(folder.isDirectory()) 
//			{
//				for(File file : folder.listFiles()) 
//				{
//					if(file.isFile())
//					{
//						builder.addBinaryBody(
//							    "file",
//							    new FileInputStream(file),
//							    ContentType.APPLICATION_OCTET_STREAM,
//							    file.getName()
//							);
//					}
//					
//				}
//			} 
//			else 
//			{
//				builder.addBinaryBody(
//					    "file",
//					    new FileInputStream(folder),
//					    ContentType.APPLICATION_OCTET_STREAM,
//					    folder.getName()
//					);
//			}
//			
			HttpEntity multipart = builder.build();
			uploadFile.setEntity(multipart);
			CloseableHttpResponse response = httpClient.execute(uploadFile);
			
			HttpEntity responseEntity = response.getEntity();
			String content = EntityUtils.toString(responseEntity);
	        System.out.println(content+"kkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkk");

		}
	}
	
	void methodToUploadFile(String INPATH, String OUTPATH) throws Exception
	{
		
		String woNo="491507";
		String woNo2="491519";
		String botId="571";
		String botId1="773";
		
		
		for(int i=0;i<1;i++)
		{
			
			if(i==0)
			{
				String jsonObj="{"
						+ "signum:erpnisu,"
						+ "woNo:"+woNo+","
						+ "projectId:348,"
						+ "taskId:127904,"
						+ "botId:"+botId1+","
						+ "inpath:abc,"
						+ "nodes:123,"
						+ "platform:server"
						+  "}";
			
			
			
			
			
			CloseableHttpClient httpClient = HttpClients.createDefault();
			HttpPost uploadFile = new HttpPost("http://10.174.128.213:8081/server-bot-app-java/serverBotExecutor/runBot");
			MultipartEntityBuilder builder = MultipartEntityBuilder.create();
			builder.addTextBody("botDetail", jsonObj, ContentType.TEXT_PLAIN);
			File folder = new File(OUTPATH);
			if(folder.isDirectory()) 
			{
				for(File file : folder.listFiles()) 
				{
					if(file.isFile())
					{
						builder.addBinaryBody(
							    "file",
							    new FileInputStream(file),
							    ContentType.APPLICATION_OCTET_STREAM,
							    file.getName()
							);
					}
					
				}
			} 
			else 
			{
				builder.addBinaryBody(
					    "file",
					    new FileInputStream(folder),
					    ContentType.APPLICATION_OCTET_STREAM,
					    folder.getName()
					);
			}
			
			HttpEntity multipart = builder.build();
			uploadFile.setEntity(multipart);
			CloseableHttpResponse response = httpClient.execute(uploadFile);
			
			HttpEntity responseEntity = response.getEntity();
			String content = EntityUtils.toString(responseEntity);
			System.out.println(content+"kkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkk");
			}
			else
			{
				String jsonObj="{"
						+ "signum:erpnisu,"
						+ "woNo:"+woNo2+","
						+ "projectId:348,"
						+ "taskId:127904,"
						+ "botId:"+botId+","
						+ "inpath:abc,"
						+ "nodes:123,"
						+ "platform:server"
						+  "}";
			
			
			
			
			
			CloseableHttpClient httpClient = HttpClients.createDefault();
			HttpPost uploadFile = new HttpPost("http://10.174.128.213:8081/server-bot-app-java/serverBotExecutor/runBot");
			MultipartEntityBuilder builder = MultipartEntityBuilder.create();
			builder.addTextBody("botDetail", jsonObj, ContentType.TEXT_PLAIN);
			File folder = new File(INPATH);
			if(folder.isDirectory()) 
			{
				for(File file : folder.listFiles()) 
				{
					if(file.isFile())
					{
						builder.addBinaryBody(
							    "file",
							    new FileInputStream(file),
							    ContentType.APPLICATION_OCTET_STREAM,
							    file.getName()
							);
					}
					
				}
			} 
			else 
			{
				builder.addBinaryBody(
					    "file",
					    new FileInputStream(folder),
					    ContentType.APPLICATION_OCTET_STREAM,
					    folder.getName()
					);
			}
			
			HttpEntity multipart = builder.build();
			uploadFile.setEntity(multipart);
			CloseableHttpResponse response = httpClient.execute(uploadFile);
			
			HttpEntity responseEntity = response.getEntity();
			String content = EntityUtils.toString(responseEntity);
	        System.out.println(content+"kkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkk");
			}
		}
	}
	
	private void AddMarketInWOFile(String inputFolder)
	{
		
		try
		{
			//Read Market.txt File
			String inputFile	=	inputFolder+File.separator+"Market.txt";
			FileReader reader	=	new FileReader(inputFile);
			BufferedReader br	=	new BufferedReader(reader);
			String line;
			while((line=br.readLine()) != null) 
			{
				MarketName	=	line;
				break;
			}
			reader.close();
			br.close();
			
			
			//Insert Market Name into WO_Detail.txt
			if(MarketName != null && !MarketName.isEmpty())
			{
				inputFile	=	inputFolder+File.separator+"WO_Details.txt";
				BufferedWriter bw = null;
				FileWriter fw = null;
				File file = new File(inputFile);

				// if file exists, then Append it
				if (file.exists()) 
				{
					fw = new FileWriter(file.getAbsoluteFile(), true);
					bw = new BufferedWriter(fw);

					bw.write("\r\nMARKETNAME="+MarketName);
					
					if (bw != null)
						bw.close();

					if (fw != null)
						fw.close();
				}
			}
			
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}
	
	private String getWorkOrderString(String inputFolder) 
	{
		String woString	=	null;
		try 
		{
			String inputFile	=	inputFolder+File.separator+"WO_Details.txt";
			FileReader reader	=	new FileReader(inputFile);
			BufferedReader br	=	new BufferedReader(reader);
			String line;
			while((line=br.readLine()) != null) 
			{
				if(line.toLowerCase().startsWith("wono")) 
				{
					woString	=	line;
					break;
				}
			}
			reader.close();
			br.close();
		} 
		catch(Exception exception) 
		{
			System.out.println("getWorkOrderId "+exception);
		}
		return woString;
	}
	
	private void ChangePathStyle()
	{
		String LastChar=INPATH.substring(INPATH.length()-1, INPATH.length());
		if(LastChar.equals("/")==false)
		{
			INPATH=INPATH+"/";
		}
		
		LastChar=OUTPATH.substring(OUTPATH.length()-1, OUTPATH.length());
		if(LastChar.equals("/")==false)
		{
			OUTPATH=OUTPATH+"/";
		}
		
		INPATH=INPATH.replace("/", "#");
		INPATH=INPATH.replace("//", "#");
		INPATH=INPATH.replace("\\", "#");
		
		
		OUTPATH=OUTPATH.replace("/", "#");
		OUTPATH=OUTPATH.replace("//", "#");
		OUTPATH=OUTPATH.replace("\\", "#");
		
		INPATH=INPATH.replace("##", "#");
		OUTPATH=OUTPATH.replace("##", "#");
		
		INPATH=INPATH.replace("#", "\\");
		OUTPATH=OUTPATH.replace("#", "\\");
	}
	
	private String getMasterVMServerIP()
	{
		String ServerIP="";
//		
//		if(MarketName.equalsIgnoreCase("NE"))
//		{
//			ServerIP="10.174.128.213";
//		}
//		else if(MarketName.equalsIgnoreCase("NCAL"))
//		{
//			ServerIP="10.174.128.213";
//		}
//		else if(MarketName.equalsIgnoreCase("NTX"))
//		{
//			ServerIP="10.174.128.213";
//		}
//		else if(MarketName.equalsIgnoreCase("TNKY"))
//		{
//			ServerIP="10.174.128.213";
//		}
//		else if(MarketName.equalsIgnoreCase("GA"))
//		{
//			ServerIP="10.174.128.213";
//		}
//		else if(MarketName.equalsIgnoreCase("NCSC"))
//		{
//			ServerIP="10.174.128.213";
//		}
//		else if(MarketName.equalsIgnoreCase("PNKWY"))//Penguin
//		{
//			ServerIP="10.174.128.228";
//		}
//		else if(MarketName.equalsIgnoreCase("OHWPA"))//Penguin
//		{
//			ServerIP="10.174.128.228";
//		}
//		else if(MarketName.equalsIgnoreCase("ALMSLA"))//Penguin
//		{
//			ServerIP="10.174.128.228";
//		}
//		else
//		{
//			ServerIP="NA";
//		}
		
		return ServerIP;
	}
	

}
