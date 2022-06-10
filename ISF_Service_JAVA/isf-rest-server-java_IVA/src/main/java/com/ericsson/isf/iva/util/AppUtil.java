/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ericsson.isf.iva.util;

import java.io.StringReader;
import java.text.SimpleDateFormat;

import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.stream.JsonReader;

/**
 *
 * @author ekarath
 */
public class AppUtil {

	private static final Logger LOG = LoggerFactory.getLogger(AppUtil.class);

	/**
	 * This method converts json string to class object
	 * 
	 * @return T
	 */
	@SuppressWarnings("hiding")
	public static <T> T convertJsonToClassObject(String jsonString, Class<T> class1) {

		Gson gson = new Gson();
		JsonParser parser = new JsonParser();
		JsonObject object = (JsonObject) parser.parse(StringUtils.trim(jsonString));// response will be the json String
		return gson.fromJson(object, class1);
	}

	/**
	 * 
	 * @param jsonString
	 * @param class1
	 * @return T
	 */
	@SuppressWarnings("hiding")
	public static <T> T convertHtmlJsonToClassObject(String jsonString, Class<T> class1) {

		Gson gson = new GsonBuilder().disableHtmlEscaping().create();
		JsonParser parser = new JsonParser();
		JsonReader reader = new JsonReader(
				new StringReader(StringEscapeUtils.unescapeHtml(StringUtils.trim(jsonString))));
		reader.setLenient(true);
		JsonObject object = (JsonObject) parser.parse(reader);// response will be the json String
		return gson.fromJson(object, class1);
	}

	public static String convertClassObjectToJson(Object object) {

		Gson gson = new Gson();
		return gson.toJson(object);
	}

}

