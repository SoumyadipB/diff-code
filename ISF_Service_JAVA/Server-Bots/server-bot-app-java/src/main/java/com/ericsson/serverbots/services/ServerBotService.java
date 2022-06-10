package com.ericsson.serverbots.services;

import java.io.BufferedOutputStream;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.time.LocalTime;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.ericsson.serverbots.models.BotDetail;
import com.ericsson.serverbots.models.RpaApiResponse;

import net.lingala.zip4j.core.ZipFile;
import net.lingala.zip4j.exception.ZipException;

@Service
@PropertySource(value = {"classpath:prop/app.properties"})
public class ServerBotService
{

	@Autowired
	private Environment environment;	


	public void testServerBotRun(BotDetail botDetail) throws Exception 
	{

		String newupPath=environment.getRequiredProperty("serverBots.files.directory");


		String wONo= botDetail.getWoNo();

		String upPath="";
		upPath=newupPath;

		upPath=upPath+wONo+File.separator;

		System.out.println(upPath+"       "+"this is the upload path1111");

		makeInputDirectory(upPath);

		System.out.println("pre-hello");
		for(int i=0;i<11;i++)
		{
			System.out.println("hello");

//			Thread.sleep(1000);
			
			for(int j=0; j<=1599900000L; j++)
			{
				//System.out.println(new Date().getTime());
			}
			
			TextFileWritingExample2(upPath);
		}

	}

	public void TextFileWritingExample2(String path) throws Exception
	{


		Date date = new Date();
		//This method returns the time in millis
		long timeMilli = date.getTime();


		String Path=path+timeMilli+".txt"+File.separator;

		System.out.println(Path+"path for text file ");

		FileWriter writer = new FileWriter(Path, true);
		BufferedWriter bufferedWriter = new BufferedWriter(writer);

		bufferedWriter.write(date.toString());
		bufferedWriter.newLine();

		bufferedWriter.close();

	}



	public RpaApiResponse serverBotRun(MultipartFile[] files, BotDetail botDetail)
	{
    	RpaApiResponse res= new RpaApiResponse();
		
		try{
			String newupPath=environment.getRequiredProperty("serverBots.files.directory");


			String wONo= botDetail.getWoNo();

			String upPath="";
			upPath=newupPath;

			upPath=upPath+wONo+"_"+botDetail.getBotId()+"_"+botDetail.getSignum()+File.separator;

			makeInputDirectory(upPath);
			methodTouploadFilesOnServer(files,botDetail,upPath);
			
			res.setApiSuccess(true);
        	res.setResponseMsg(" Successfully");
			
		}
		catch(Exception e)
		{
			res.setApiSuccess(false);
        	res.setResponseMsg("Faile  :( ");
		}

		return res;
		
	}


	private void methodTouploadFilesOnServer(MultipartFile[] files, BotDetail botDetail, String upPath) 
	{

		String message = "NO";
		//String uploadPath	=	"C:/Pathloss/";
		for(int i=0;i<files.length; i++){
			MultipartFile file	=	files[i];
			String name = file.getOriginalFilename();
			try {
				byte[] bytes = file.getBytes();
				File dir = new File(upPath);
				if (dir.exists())
					dir.delete();

				dir = new File(upPath);
				if (!dir.exists())
					dir.mkdirs();

				// Create the file on server
				File serverFile = new File(dir+"/"+name);
				BufferedOutputStream stream = new BufferedOutputStream(
						new FileOutputStream(serverFile));
				stream.write(bytes);
				stream.close();
				System.out.println("Server File Location="
						+ serverFile.getAbsolutePath());
				System.out.println(message + "You successfully uploaded file=" + name + "");
				message = "YES";

				
				System.out.println("Platform Name is "    +botDetail.getPlatform()+" kkkkkkkkkkkkkkkkkkkkkkkkkk");

				runRPAJar(botDetail.getSignum(),botDetail.getWoNo(),botDetail.getProjectId(),botDetail.getTaskId(),botDetail.getBotId(),upPath,botDetail.getNodes(),botDetail.getPlatform());

				//				
				//			File f=new File(upPath);
				//				deleteDir(f);
				//				

			} catch (Exception e) {
				System.out.println(e);
				message = "NO";
			}
		}

	}


	static void deleteDir(File file) {
		File[] contents = file.listFiles();
		if (contents != null) {
			for (File f : contents) {
				if (! Files.isSymbolicLink(f.toPath())) {
					deleteDir(f);
				}
			}
		}
		file.delete();
	}

	//////////////////////////

	private void makeInputDirectory(String uploadPath)
	{

		File f=new File(uploadPath);

		//		if(f.exists())
		//		{
		//			f.delete();
		//		}

		if(!f.exists())
		{
			f.mkdirs();
		}
		
		System.out.println("makeInputDirectory() ends...");

	}
	///////////////////////////////

	public int runRPAJar(String SIGNUM,String WONO, String PROJECTID, String TASKID, String BOTID, String INPATH, String NODES,String platform) throws Exception 
	{
		int nCount = 1;

		try
		{ 
			Unzip_Files(INPATH);



//			String PYTHONFILEPATH= "C:\\SOFTHUMAN_DEVELOPMENT\\shrpa.jar ";  
//			Runtime.getRuntime().exec(" java -jar "+ PYTHONFILEPATH +SIGNUM+" "+WONO+" "+PROJECTID+" "+TASKID+" "+BOTID+" "+INPATH+" "+NODES);
			
			
			Runtime.getRuntime().exec("cmd /c start cmd.exe /K \"cd C:\\SOFTHUMAN_DEVELOPMENT\\ && java -cp rpa.jar sadagi.view.WelcomeSHRPAMain "+SIGNUM+" "+WONO+" "+PROJECTID+" "+TASKID+" "+BOTID+" "+INPATH+" "+NODES+" "+platform);
			
			/*
			BufferedReader in = new BufferedReader(new InputStreamReader(p.getInputStream()));

			String line; 

			while((line = in.readLine()) != null)
			{
				System.out.println("value is : "+line);	
			} 


			 */			
			//			runThroughCMD(SIGNUM,WONO,PROJECTID,TASKID,BOTID,INPATH,NODES);


		} 

		catch (Exception e) {
			e.printStackTrace();
			return -1;
		}

		return nCount;
	}

	////////////////////////////////////////////

	private static void runThroughCMD(String SIGNUM,String WONO,String PROJECTID,String TASKID,String BOTID,String INPATH,String NODES) throws Exception
	{

		Runtime.getRuntime().exec("cmd /c start cmd.exe /K \"cd C:\\SOFTHUMAN_DEVELOPMENT\\ && java -jar shrpa.jar "+SIGNUM+" "+WONO+" "+PROJECTID+" "+TASKID+" "+BOTID+" "+INPATH+" "+NODES); 

	}

	///////////////////////////////////////

	private static void Unzip_Files(String uploadPath) throws Exception {
		// TODO Auto-generated method stub

		File newInputFolder = new File(uploadPath);
		File[] newInputFile = newInputFolder.listFiles();

		for (int i = 0; i < newInputFile.length; i++) 
		{
			if(newInputFile[i].getName().toUpperCase().contains("ZIP".toUpperCase()))

			{
				unZipFile(newInputFile[i].getAbsolutePath());

			}
		}

	}

	/////////////////////////////////////////////


	public static boolean unZipFile(String source)
	{ 
		boolean status = false;
		String[] Arr = source.split("\\\\");
		String forlderName = Arr[Arr.length-1].split("\\.",2)[0];

		StringBuffer stringBuffer = new StringBuffer();
		for(int i =0; i < Arr.length-1; i++)
		{
			if(!Arr[i].equalsIgnoreCase(""))
				stringBuffer.append(Arr[i]+"\\");
		}
		String destinationDir = stringBuffer.toString();
		try
		{
			ZipFile zipFile = new ZipFile(source);
			zipFile.extractAll(destinationDir);
			new File(source).delete();
			File[] files = new File(destinationDir+""+forlderName).listFiles();;
			if(files != null)
			{
				for(int i= 0; i < files.length; i++)
				{
					files[i].renameTo(new File(destinationDir, files[i].getName()));
				}
			}

			if(new File(destinationDir+"/"+forlderName).delete())
			{
				status = true;
			}
		}

		catch (ZipException e) {

			status = false;
		}
		return status;
	}

}
