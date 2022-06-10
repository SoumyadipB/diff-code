package com.ericsson.serverbots.controller;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Files;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.ericsson.serverbots.models.BotDetail;
import com.ericsson.serverbots.models.RpaApiResponse;
import com.ericsson.serverbots.services.ServerBotService;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import net.lingala.zip4j.core.ZipFile;
import net.lingala.zip4j.exception.ZipException;


@Controller
@RequestMapping("/serverBotExecutor")



public class ServerBotController {
	
	@Autowired ServerBotService serverBotService;

	@Value("${serverBots.files.directory}")
	private String uploadPath;


	@RequestMapping(value="/runBot",method=RequestMethod.POST)
	@ResponseBody
	public String runBot(@RequestPart("file") MultipartFile[] files, @RequestParam("botDetail") String botJson) 
	{
		String message="UploadDone";
		
		

		Gson gson = new Gson();
		JsonParser parser = new JsonParser();
		JsonObject object = (JsonObject) parser.parse(botJson);// response will be the json String
		BotDetail botDetail= gson.fromJson(object, BotDetail.class);

		
		
		
		RpaApiResponse response=serverBotService.serverBotRun(files, botDetail);
		
		
		message=response.toString();
		return message;
	}
	
	//////////////////////////////////////////////////////////////////////////////////////////////////
	
	
	@RequestMapping(value="/loadTest",method=RequestMethod.POST)
	@ResponseBody
	public String loadTest( @RequestParam("botDetail") String botJson) throws Exception 
	{
		Gson gson = new Gson();
		JsonParser parser = new JsonParser();
		JsonObject object = (JsonObject) parser.parse(botJson);// response will be the json String
		BotDetail botDetail= gson.fromJson(object, BotDetail.class);
		
		
		System.out.println("Loastest Method start");
		serverBotService.testServerBotRun(botDetail);
		
		
		String message = "NO";
		
		return message;
	}
	
	
	

	
	

	
	
	
	

	






}
