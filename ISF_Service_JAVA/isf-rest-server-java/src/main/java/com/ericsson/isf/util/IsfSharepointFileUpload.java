package com.ericsson.isf.util;



import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

import javax.net.ssl.X509TrustManager;

import org.apache.commons.lang.StringUtils;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.FileEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ericsson.isf.model.SharePointModel;
import com.ericsson.isf.service.OutlookAndEmailService;
import com.nimbusds.oauth2.sdk.http.HTTPResponse;

@Service
public class IsfSharepointFileUpload {
	private static final String ACCESS_GRANTED = "Access Granted!!";
	public static String accessTokenInt = "";
	public static String accessTokenScr = "";
	
	public static String clientID = ""; 
	public static String clientSecret = ""; 

	static File filePath = null;

	static PrintStream fOut = System.out;

	private static final Logger LOG= LoggerFactory.getLogger(IsfSharepointFileUpload.class);
	
	public HttpResponse uploadFile(SharePointModel sharePointModel,  File fileSharePoint) throws IOException {
		
		//boolean status=false;
		clientID=sharePointModel.getClientId();
		clientSecret=sharePointModel.getSecretKey();
		
		
		String folderUrlscr = sharePointModel.getFolderName().replaceAll("\\s", "%20");
				
		String fileName= AppUtil.getFileNameWithDateAndTime(sharePointModel.getFileNamePattern()+"_Report");
		fileName=fileName.replaceAll("\\s", "%20");
		
		HttpResponse res= executeMultiPartScriptingRequest(
				sharePointModel.getBaseURL()+"/_api/web/GetFolderByServerRelativeUrl(\'"
						+ folderUrlscr +"\')/Files/add(url='" + fileName+".csv"+ "',overwrite=true)",
						fileSharePoint);
		
//		if(res.getStatusLine().getStatusCode()==HTTPResponse.SC_OK) {
//			status= true;
//		}else {
//			status=false;
//		}
		return res;
	}


	// File Upload functionality
	@SuppressWarnings("deprecation")
	private static HttpResponse executeRequest(HttpPost httpPost) {
		HttpResponse response=null;
		try {
			@SuppressWarnings("resource")
			HttpClient client = new DefaultHttpClient();
			response = client.execute(httpPost);
			System.out.println("Response Code:  " + response.getStatusLine().getStatusCode());
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IllegalStateException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return response;
	}

	public static HttpResponse executeMultiPartScriptingRequest(String urlString, File file) throws IOException {
		HttpPost postRequest = new HttpPost(urlString);
		postRequest = addHeaderScripting(postRequest);
		try {
			postRequest.setEntity(new FileEntity(file));
		} catch (Exception ex) {
			ex.printStackTrace();
			fOut.println("file ------->"+file);
		}
		return executeRequest(postRequest); 
	}

	private static HttpPost addHeaderScripting(HttpPost httpPost) throws IOException {
		accessTokenScr = getSharepointTokenScripting();
		httpPost.addHeader("Accept", "application/json;odata=verbose");
		httpPost.setHeader("Authorization", "Bearer " + accessTokenScr);
		System.out.println("httpPost........" + httpPost);
		return httpPost;
	}

	private static class DefaultTrustManager implements X509TrustManager {

		@Override
		public void checkClientTrusted(X509Certificate[] arg0, String arg1) throws CertificateException {
		}

		@Override
		public void checkServerTrusted(X509Certificate[] arg0, String arg1) throws CertificateException {
		}

		@Override
		public X509Certificate[] getAcceptedIssuers() {
			return null;
		}
	}

	public static String getSharepointTokenScripting() throws IOException {
		/**
		 * This function helps to get SharePoint Access Token. SharePoint Access
		 * Token is required to authenticate SharePoint REST service while
		 * performing Read/Write events. SharePoint REST-URL to get access token
		 * is as: https://accounts.accesscontrol.windows.net/
		 * <tenantID>/tokens/OAuth/2
		 * 
		 * Input required related to SharePoint are as: 1. shp_clientId 2.
		 * shp_tenantId 3. shp_clientSecret
		 */

		String accessToken = "";
		try {

			// AccessToken url : app.properties

			String wsURL = "https://accounts.accesscontrol.windows.net/92e84ceb-fbfd-47ab-be52-080c6b87953f/tokens/OAuth/2";

			URL url = new URL(wsURL);
			URLConnection connection = url.openConnection();
			HttpURLConnection httpConn = (HttpURLConnection) connection;

			// Set header
			httpConn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
			httpConn.setDoOutput(true);
			httpConn.setDoInput(true);
			httpConn.setRequestMethod("POST");

			// Prepare RequestData

			String jsonParam = "grant_type=client_credentials"
					+ "&client_id="+clientID+"@92e84ceb-fbfd-47ab-be52-080c6b87953f"
					+ "&client_secret="+clientSecret
					+ "&resource=00000003-0000-0ff1-ce00-000000000000/ericsson.sharepoint.com@92e84ceb-fbfd-47ab-be52-080c6b87953f";

			// Send Request
			DataOutputStream wr = new DataOutputStream(httpConn.getOutputStream());
			wr.writeBytes(jsonParam);
			wr.flush();
			wr.close();

			// Read the response.
			InputStreamReader isr = null;
			if (httpConn.getResponseCode() == 200) {
				isr = new InputStreamReader(httpConn.getInputStream());
			} else {
				isr = new InputStreamReader(httpConn.getErrorStream());
			}

			BufferedReader in = new BufferedReader(isr);
			String responseString = "";
			String outputString = "";

			// Write response to a String.
			while ((responseString = in.readLine()) != null) {
				outputString = outputString + responseString;
			}
			
			if (outputString.indexOf("access_token\":\"") > -1) {
				int i1 = outputString.indexOf("access_token\":\"");
				String str1 = outputString.substring(i1 + 15);
				int i2 = str1.indexOf("\"}");
				String str2 = str1.substring(0, i2);
				accessToken = str2;
				// System.out.println("accessToken.........." + accessToken);
			}
		} catch (Exception e) {
			accessToken = "Error: " + e.getMessage();
		}
		return accessToken;
	}

	public String getSharepointAccess(String sharePointclientID, String secretKey, String siteURL) throws IOException {

		String msg = "";

		// For create a File on SharePoint

		HttpURLConnection create = createFileonSharePoint(sharePointclientID, secretKey, siteURL);
		if (create.getResponseCode() == 200) {
			
			// After created, delete File from sharePoint location
			HttpURLConnection delete = deleteFileFromSharePoint(sharePointclientID, secretKey, siteURL);
			if (delete.getResponseCode() == 200) {
				msg = ACCESS_GRANTED;
			} else {
				msg = "Response Code : " + create.getResponseCode();

			}
		} else {
			msg = "Response Code : " + create.getResponseCode();

		}

		return msg;

	}

	private HttpURLConnection createFileonSharePoint(String sharePointclientID, String secretKey, String siteURL)
			throws IOException {

		String wsURL = siteURL
				+ "/_api/web/GetFolderByServerRelativeUrl('Shared%20Documents')/Files/add(url='Test.txt',overwrite=true)";
		URL url = new URL(wsURL);
		URLConnection connection = url.openConnection();

		HttpURLConnection httpConn = (HttpURLConnection) connection;
		try {
			clientID = sharePointclientID;
			clientSecret = secretKey;

			// AccessToken url : app.properties

			accessTokenScr = getSharepointTokenScripting();
			// Set header
			httpConn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");

			httpConn.setRequestProperty("Authorization", "Bearer " + accessTokenScr);
			httpConn.setDoOutput(true);
			httpConn.setDoInput(true);
			httpConn.setRequestMethod("POST");

			String request = "Create a File for Testing !!!";

			DataOutputStream wr = new DataOutputStream(httpConn.getOutputStream());
			wr.writeBytes(request);
			wr.flush();
			wr.close();

			String responseStr = "";
			if (httpConn.getResponseCode() == 200) {
				responseStr = "File has been written successfully. ResponseCode: " + httpConn.getResponseCode();
			} else {
				responseStr += "Error while writing file, ResponseCode: " + httpConn.getResponseCode() + " "
						+ httpConn.getResponseMessage();
			}
			LOG.info(responseStr);
		} catch (Exception e) {

		}
		return httpConn;

	}

	private HttpURLConnection deleteFileFromSharePoint(String sharePointclientID, String secretKey, String siteURL)
			throws IOException {

		String[] splitSiteURL = siteURL.split("/");
		int l = splitSiteURL.length;
		String name = splitSiteURL[l - 1];
		LOG.info(name);

		String wsURL = siteURL + "/_api/web/GetFileByServerRelativeUrl('/sites/" + name
				+ "/Shared%20Documents/Test.txt')";

		URL url = new URL(wsURL);
		URLConnection connection = url.openConnection();

		HttpURLConnection httpConn = (HttpURLConnection) connection;
		try {
			clientID = sharePointclientID;
			clientSecret = secretKey;

			// AccessToken url : app.properties

			accessTokenScr = getSharepointTokenScripting();
			// Set header
			httpConn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");

			httpConn.setRequestProperty("Authorization", "Bearer " + accessTokenScr);
			httpConn.setDoOutput(true);
			httpConn.setDoInput(true);
			httpConn.setRequestMethod("DELETE");

			String responseStr = "";
			if (httpConn.getResponseCode() == 200) {
				responseStr = "File has been deleted successfully. ResponseCode: " + httpConn.getResponseCode();
			} else {
				responseStr += "Error while deleting file, ResponseCode: " + httpConn.getResponseCode() + " "
						+ httpConn.getResponseMessage();
			}

			LOG.info(responseStr);
		} catch (Exception e) {

		}
		return httpConn;

	}


	
}
