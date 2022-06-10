package com.ericsson.isf.util;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.Enumeration;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipInputStream;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.KeyManager;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import org.apache.commons.io.IOUtils;
import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPReply;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.conn.ConnectTimeoutException;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.multipart.MultipartFile;

import com.ericsson.isf.exception.ApplicationExceptionHandler;
import com.google.common.io.ByteStreams;
import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import com.jcraft.jsch.SftpException;

public class BotStoreUtil 
{
	private static final Logger LOG = LoggerFactory.getLogger(ApplicationExceptionHandler.class);
	
	public static String getRelativeFilePathUploadInput(int requestID) 
	{
		String filePath = "";
		filePath = requestID + "/" + "input";
		return filePath;
	}
	public static String getRelativeFilePathUploadOutput(int requestID) 
	{
		String filePath = "";
		filePath = requestID + "/" + "output";
		return filePath;
	}
	public static String getRelativeFilePathUploadLogic(int requestID) 
	{
		String filePath = "";
		filePath = requestID + "/" + "logic";
		return filePath;
	}
	public static String getRelativeFilePathUploadCode(int requestID) 
	{
		String filePath = "";
		filePath = requestID + "/" + "code";
		return filePath;
	}
	public static String getRelativeFilePathUploadExe(int requestID) 
	{
		String filePath = "";
		filePath = requestID + "/" + "exe";
		return filePath;
	}
	
	

	public static String getAbsFileNameInDir(String sourcePath)
    {
          String fName="";
          
          File sdir=new File(sourcePath);
          File[] files = sdir.listFiles();
          for (File file : files) 
          {
                if (!file.isDirectory()) 
                {
                       fName=file.getAbsolutePath();
              } 
          }
          return fName;
    }

	public static String getFileNameFromZip(MultipartFile zipFile) 
    {
    	String macroFileName= null;

        ZipEntry zEntry = null;
        try (ZipInputStream zipIs = new ZipInputStream(new BufferedInputStream(zipFile.getInputStream()))){
        	while((zEntry = zipIs.getNextEntry()) != null){
                macroFileName= zEntry.getName();
                
                macroFileName= macroFileName.replaceAll("/", "#");
                if(null!= macroFileName && macroFileName.contains("#"))
                {
                	macroFileName= macroFileName.split("#")[macroFileName.split("#").length-1];
                }
                

            }	
        }catch (Exception e) {
			e.getMessage();
		}
        
        return macroFileName;
    }	

	public static boolean unzipFileFromZip(MultipartFile zipFile, String baseLocalPath, String srcFileName, String targetFileName)
    {
		boolean isFileUnzipped= false;
		ZipFile file =null;
		try {
			zipFile.transferTo(new File(baseLocalPath+File.separator+srcFileName));

			file = new ZipFile(new File(baseLocalPath+File.separator+srcFileName));
			
			final Enumeration<? extends ZipEntry> entries = file.entries();
		    while ( entries.hasMoreElements() )
		    {
		        final ZipEntry entry = entries.nextElement();
		        
		        //use entry input stream:
		        //if(entry.getName().contains(targetFileName))
		        //{
		        	InputStream stream = file.getInputStream(entry);
		        	if(stream!=null)
		        	{
		        		// write the inputStream to a FileOutputStream
			    		OutputStream outputStream =  new FileOutputStream(new File(baseLocalPath+File.separator+targetFileName));
			    		int read = 0;
			    		byte[] bytes = new byte[2024];

			    		while ((read = stream.read(bytes)) != -1) {
			    			outputStream.write(bytes, 0, read);
			    		}

			    		
			    		outputStream.close();
			    		stream.close();
		        	}
		        //}
		        isFileUnzipped= true;
		    }
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		finally {
			try {
			    file.close();
			} catch (Exception e2) {
				e2.printStackTrace();
			}
		}
		return isFileUnzipped;
    }


	

	
	// function get Stream from url
	// by making HTTP POST or GET method
	/**
	 * purpose	: It is used to make http call for downloading the BOT.
	 * 
	 * @param URL : API url
	 * @param Method : http method name- Get or Post
	 * @param params : Bot Id and botName as a parameter
	 * 
	 * @return byte[] : byte array of the BOT file.
	 * 
	 */
	public static byte[] makeHttpRequestBOTDownload(String url, String method, List<NameValuePair> params) throws Exception 
	{
		// Making HTTP request
		InputStream is = null;
		try
		{
			// check for request method
			if (method == "GET") 
			{
				 // request method is GET
				
				if(url.contains("http://"))
				{
					///////////////////////////This is for HTTP Class
					
					  CloseableHttpClient httpClient = HttpClients.createDefault(); String
					  paramString = URLEncodedUtils.format(params, "utf-8"); url += "?" +
					  paramString; HttpGet httpGet = new HttpGet(url);
					  
					  CloseableHttpResponse response = httpClient.execute(httpGet); org.apache.http.HttpEntity
					  httpEntity = response.getEntity(); is = ((org.apache.http.HttpEntity) httpEntity).getContent();
					 
				}
				else if(url.contains("https://"))
				{
					//////////////////////////This is for HTTPS Class
					SSLContext ctx = SSLContext.getInstance("TLS");
					ctx.init(new KeyManager[0], new TrustManager[] {new DefaultTrustManager()}, new SecureRandom());
					SSLContext.setDefault(ctx);
    
					//String sNewURL=url.replace("http", "https").replace("8080", "8443");
					URL Newurl = new URL(url);
					HttpsURLConnection conn = (HttpsURLConnection) Newurl.openConnection();
					
					conn.setHostnameVerifier(new HostnameVerifier() 
					{
		              public boolean verify(String arg0, SSLSession arg1) 
		              {
		                  return true;
		              }
					});
					
					int responseCode = conn.getResponseCode();
					if (responseCode == HttpURLConnection.HTTP_OK) 
					{
						is=conn.getInputStream();
					}
				}
			}
		} 
		catch (UnsupportedEncodingException e) 
		{
			throw new Exception("Unsupported encoding error.");
		} 
		catch (ClientProtocolException e) 
		{
			throw new Exception("Client protocol error.");
		}
		catch (SocketTimeoutException e) 
		{
			throw new Exception("Sorry, socket timeout.");
		} 
		catch (ConnectTimeoutException e) 
		{
			throw new Exception("Sorry, connection timeout.");
		} 
		catch (IOException e) 
		{
			throw new Exception("I/O error(May be server down).");
		}
		    
		byte[] bytess= null;
		try 
		{
			if(is!=null)
			{
				bytess= IOUtils.toByteArray(is);
				
			}
			
		} 
		catch (Exception e) 
		{
			throw new Exception(e.getMessage());
		}finally {
			try {
				is.close();
			}catch (Exception e) {
				LOG.info(e.getMessage());
			}
			
		}
		return bytess;
	}

	
	private static class DefaultTrustManager implements X509TrustManager {

        public void checkClientTrusted(X509Certificate[] arg0, String arg1) throws CertificateException {}

        public void checkServerTrusted(X509Certificate[] arg0, String arg1) throws CertificateException {}

        public X509Certificate[] getAcceptedIssuers() {
            return null;
        }
    }

	
	public static boolean uploadFileUsingFTP(String server, String port, String user, String pass, String uploadPath, String zipInputFilePath, String zipFileName) throws Exception
	{
		boolean status=false;
		
		FTPClient ftpClient = new FTPClient();
        try {

            ftpClient.connect(server, Integer.parseInt(port));
            showServerReply(ftpClient);
            
            int replyCode = ftpClient.getReplyCode();
            if (!FTPReply.isPositiveCompletion(replyCode))
            {
                System.out.println("Connect failed");
            }
            else
            {
            	 boolean success = ftpClient.login(user, pass);
                 showServerReply(ftpClient);
      
                 if (!success) 
                 {
                     System.out.println("Could not login to the server");
                    
                 }
                 else
                 {
                	 ftpClient.login(user, pass);
                     ftpClient.enterLocalPassiveMode();

                     ftpClient.setFileType(FTP.BINARY_FILE_TYPE);

                     // APPROACH #2: uploads second file using an OutputStream
                     File secondLocalFile = new File(zipInputFilePath);
                     String secondRemoteFile = uploadPath+zipFileName;
                     InputStream inputStream = new FileInputStream(secondLocalFile);

                     //System.out.println("Start uploading second file");
                     OutputStream outputStream = ftpClient.storeFileStream(secondRemoteFile);
                     byte[] bytesIn = new byte[4096];
                     int read = 0;

                     while ((read = inputStream.read(bytesIn)) != -1) {
                         outputStream.write(bytesIn, 0, read);
                     }
                     inputStream.close();
                     outputStream.close();

                     boolean completed = ftpClient.completePendingCommand();
                     if (completed) 
                     {
                         //System.out.println("The second file is uploaded successfully.");
                     	status=true;
                     }
                 }
            }
        } 
        catch(SocketException se) {
        	status=false;
        	System.out.println("The SocketException handled.");
            //se.printStackTrace();
            //throw se;
		}
        catch (IOException ex) 
        {
        	status=false;
//            System.out.println("Error: " + ex.getMessage());
            ex.printStackTrace();
            //throw ex;
        }
        finally 
        {
            try 
            {
                if (ftpClient.isConnected()) 
                {
                    ftpClient.logout();
                    ftpClient.disconnect();
                }
            } 
            catch (IOException ex) 
            {
            	status=false;
                ex.printStackTrace();
                //throw ex;
            }
        }
        
		return status;
	}

	public static void showServerReply(FTPClient ftpClient) 
	{
        String[] replies = ftpClient.getReplyStrings();
        if (replies != null && replies.length > 0) 
        {
            for (String aReply : replies) 
            {
                System.out.println("SERVER: " + aReply);
            }
        }
    }


	public static boolean UploadFileUsingSFTP(String server, String port, String user, String pass, String uploadPath, String zipInputFilePath, String zipFileName)
	{
		boolean status=false;
		
		String SFTPHOST = server;
        int SFTPPORT = Integer.parseInt(port);
        String SFTPUSER = user;
        String SFTPPASS = pass;

        Session session = null;
        Channel channel = null;
        ChannelSftp channelSftp = null;
        System.out.println("preparing the host information for sftp.");
        
		
        try {
        		JSch jsch = new JSch();
        		session = jsch.getSession(SFTPUSER, SFTPHOST, SFTPPORT);
        		session.setPassword(SFTPPASS);
        		java.util.Properties config = new java.util.Properties();
        		config.put("StrictHostKeyChecking", "no");
        		session.setConfig(config);
        		session.connect();
        		System.out.println("Host connected.");
        		channel = session.openChannel("sftp");
        		channel.connect();
        		System.out.println("sftp channel opened and connected.");
        		channelSftp = (ChannelSftp) channel;
        		channelSftp.cd(uploadPath);
        		File f = new File(zipFileName);
        		channelSftp.put(new FileInputStream(f), f.getName());
        } catch (JSchException e) 
        {
            e.printStackTrace();  
        } catch (SftpException e) 
        {
            e.printStackTrace();
        }
        catch (Exception ex)
        {
            System.out.println("Exception found while tranfer the response.");
        }
        finally
        {
            channelSftp.exit();
            System.out.println("sftp Channel exited.");
            channel.disconnect();
            System.out.println("Channel disconnected.");
            session.disconnect();
            System.out.println("Host Session disconnected.");
        }
		
		return status;
		
	}
	public static byte[] unzipFileFromZipinBLOB(MultipartFile zipFile)
    {
		byte[] isFileUnzipped = null;
		
           try(ZipInputStream zis = new ZipInputStream(zipFile.getInputStream()))
           {
        	  // File tempFile = null;
			//  zipFile.transferTo(tempFile);
        	 
            
            ZipEntry entry;
            while ((entry = zis.getNextEntry()) != null) 
            {
                if (entry.isDirectory()) {
                    continue;
                }

                
                if (!entry.isDirectory() && (entry.getName().endsWith(".jar")||entry.getName().endsWith(".exe"))) 
                {
                String name = entry.getName();
                       name = new File(name).getName();
                       //File out = new File("C:\\Users\\essrmma\\Desktop\\Rupinder\\1604\\", name);
                       //noinspection ResultOfMethodCallIgnored
                       //FileOutputStream fos = new FileOutputStream(out);
                      // ByteStreams.copy(zis, fos);
                       byte[] bytes = ByteStreams.toByteArray(zis);
                       isFileUnzipped=bytes;
                       
                }
            }
            
           }
           catch(Exception e)
           {
                  e.printStackTrace();
           }
           
		return isFileUnzipped;
		}


}
