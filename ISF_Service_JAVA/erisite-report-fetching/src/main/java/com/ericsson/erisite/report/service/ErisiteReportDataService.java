package com.ericsson.erisite.report.service;

import java.io.IOException;
import java.util.Base64;
import java.util.Collections;

import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import com.ericsson.erisite.report.dao.ReportDao;
import com.ericsson.erisite.report.model.ActivityDataModel;
import com.ericsson.erisite.report.model.Response;
import com.ericsson.erisite.report.model.WorkPlanFullModel;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class ErisiteReportDataService {


	@Autowired
	private ReportDao reportDao;


	private static final Logger LOGGER = LoggerFactory.getLogger(ErisiteReportDataService.class);

	
	@Transactional
	public Response<Void> downloadJsonForReport(int projectID) {
		Response<Void> apiResponse= new Response<>();
		try {
			ResponseEntity<Object> response = null;

			RestTemplate restTemplate = new RestTemplate();
			HttpHeaders headers = new HttpHeaders();

			Base64.Encoder encoder = Base64.getEncoder();  

			String username="RxwUIfFWwCWufFhJjULywLbpfCka";
			String password="07lJbzfafWYps9npMWLbVvXxMbUa";

			String userpass = username + ':' + password;
			String encoded_u = encoder.encodeToString(userpass.getBytes());
			headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
			headers.setAccept(Collections.singletonList(MediaType.ALL));
			headers.add("Authorization", "Basic "+encoded_u);

			MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
			map.add("grant_type","client_credentials");

			String url = "https://emea-api.erisite.ericsson.net/token";

			HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(map, headers);
			response = restTemplate.exchange(url, HttpMethod.POST, request, Object.class);
			
			JSONObject obj = new JSONObject(response.getBody().toString().replaceAll("=",":"));
			String bearerToken	=	obj.get("access_token").toString();			

			downloadWorkPlanFullStructure(bearerToken,projectID);
			
			LOGGER.info("Data Inserted Successfully");
			apiResponse.addFormMessage("Data Inserted Successfully");

		} catch (Exception e) {
			e.printStackTrace();
			apiResponse.addFormError(e.getMessage());
		}
		return apiResponse;
	}
	private void downloadWorkPlanFullStructure(String bearerToken, int projectID) throws JsonParseException, JsonMappingException, IOException {

		ResponseEntity<String> response = null;
		RestTemplate restTemplate = new RestTemplate();
		HttpHeaders headers = new HttpHeaders();
		String url = "https://emea-api.erisite.ericsson.net/ErisiteEMEA/1.0.0/api/workplans/fullstructure";
		headers.add("Authorization", "Bearer "+bearerToken);
		headers.setAccept(Collections.singletonList(MediaType.ALL));
		UriComponentsBuilder uriBuilder = UriComponentsBuilder.fromHttpUrl(url)
				.queryParam("ProjectID", projectID);

		HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(headers);
		response = restTemplate.exchange(uriBuilder.toUriString(), HttpMethod.GET, request, String.class);

		JSONObject myObject = new JSONObject(response.getBody());

		JSONArray data=myObject.getJSONArray("data");
		
		this.reportDao.truncateData();

		for(Object ob:data) {
			ObjectMapper mapper= new ObjectMapper();
			WorkPlanFullModel workPlanData= mapper.readValue(ob.toString(), WorkPlanFullModel.class);
		
			LOGGER.info("WorkPlanID ========= >"+workPlanData.getWorkPlanID());
			for(ActivityDataModel act : workPlanData.getActivityDataModel()) {

				this.reportDao.insertErisiteData(act,workPlanData, workPlanData.getSiteDatamodel().getName(),
						workPlanData.getProjectDataModel());
				LOGGER.info("Activity ID ========= >"+act.getId());
				downloadActivityFullStructure(act.getId(), bearerToken);
			}
		}
	}

	private void downloadActivityFullStructure(int id, String bearerToken) throws JsonParseException, JsonMappingException, IOException {
		ResponseEntity<String> response = null;
		RestTemplate restTemplate = new RestTemplate();
		HttpHeaders headers = new HttpHeaders();
		String url = "https://emea-api.erisite.ericsson.net/ErisiteEMEA/1.0.0/api/activities/fullstructure";
		headers.add("Authorization", "Bearer "+bearerToken);
		headers.setAccept(Collections.singletonList(MediaType.ALL));
		UriComponentsBuilder uriBuilder = UriComponentsBuilder.fromHttpUrl(url)
				.queryParam("ActivityId", id);

		HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(headers);
		response = restTemplate.exchange(uriBuilder.toUriString(), HttpMethod.GET, request, String.class);

		JSONObject myObject = new JSONObject(response.getBody());

		JSONArray data=myObject.getJSONArray("data");

		for(Object ob:data) {
			ObjectMapper mapper= new ObjectMapper();
			ActivityDataModel activityDataModel= mapper.readValue(ob.toString(), ActivityDataModel.class);

			this.reportDao.insertErisiteAcivityData(activityDataModel);

		}
	}


}
