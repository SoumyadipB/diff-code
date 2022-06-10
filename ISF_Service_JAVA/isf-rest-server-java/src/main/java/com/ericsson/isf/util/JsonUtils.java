package com.ericsson.isf.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicLong;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.ericsson.isf.model.json.Activities;
import com.ericsson.isf.model.json.Activity;
import com.ericsson.isf.model.json.AppActivity;
import com.ericsson.isf.model.json.Condition;
import com.ericsson.isf.model.json.ConditionEnum;
import com.ericsson.isf.model.json.Connector;
import com.ericsson.isf.model.json.ConnectorNotification;
import com.ericsson.isf.model.json.EmailActivity;
import com.ericsson.isf.model.json.FileActivity;
import com.ericsson.isf.model.json.Http;
import com.ericsson.isf.model.json.JSONDataModel;
import com.ericsson.isf.model.json.KeyMatchValue;
import com.ericsson.isf.model.json.Match;
import com.ericsson.isf.model.json.Notification;
import com.ericsson.isf.model.json.OperatorMappingEnum;
import com.ericsson.isf.model.json.ParentRule;
import com.ericsson.isf.model.json.ParentRuleModel;
import com.ericsson.isf.model.json.Rule;
import com.ericsson.isf.model.json.Rule1;
import com.ericsson.isf.model.json.Scanner;
import com.ericsson.isf.model.json.Source;
import com.ericsson.isf.model.json.Target;
import com.ericsson.isf.model.json.Trigger;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@Component
public class JsonUtils {
	
	private static final String MARKET = "@market@";
	private static final String NODES = "@nodes@";
	private static final String FIELDS = "fields";
	private static final String TYPE_NAME = "typeName";
	private static final String TARGET = "target";
	private static final String PORT = "port";
	private static final String ID = "id";
	private static final String SOURCE = "source";
	private static final String STANDARD_LINK = "standard.Link";
	private static final String CELLS = "cells";
	private static final String TYPE = "type";
	
	public static final String SCANNER="Scanner";
	public static final String HTML_APP_SCAN="html.AppScan";
	public static final String APP_SCANNER="app_scanner";
	public static final String CUSTOMAPP="customApp";
	public static final String TRIGGERONCLOSE="triggerOnClose";
	public static final String FALSE_FCAPS="False";
	public static final String HTML_FILE_SCAN="html.FileScan";
	public static final String FS_SCANNER="fs_scanner";
	public static final String CUSTOM_PATH="customPath";
	public static final String LOOK_FOR_SUBFOLDER="lookForSubfolder";
	public static final String HTML_EMAIL_SCAN="html.EmailScan";
	public static final String EMAIL_SCANNER="email_scanner";
	public static final String LOOK_FOR_PAST_EMAIL="lookForPastEmail";
	public static final String TIME_FIELD="timeField";
	public static final String HEADER="header";
	public static final String MATCH_TYPE="matchType";
	public static final String MATCH_ATTR="matchattr";
	public static final String INPUT_BUFFER_KEYSTROKE="INPUT_BUFFER_KEYSTROKE";
	public static final String INPUT_BUFFER_KEYEVENT="INPUT_BUFFER_KEYEVENT";
	public static final String STATIC_DATA="staticData";
	public static final String DYNAMIC_FIELD="dynamicField";
	public static final String DEFAULT_VALUE_FOR_DYNAMIC="defaultValueForDynamic";
	public static final String PORTS="ports";
	public static final String ITEMS="items";
	public static final String OPERATOR="operator";
	public static final String HTML_APP_CONDITION="html.AppCondition";
	public static final String NOTIFICATION="Notification";
	public static final String ACTION_NAME0="actionName0";
	public static final String HTML_CONNECTOR_CONDITION="html.ConnectorCondition";
	public static final String SCANNERID="scannerID = {}";
	public static final String LOOKBACK="lookBack";
	public static final String LOOK_BACK_TIME="look_back_time";
	public static final String VALID_APPS="valid_apps";
	public static final String SCANNER_COMBINATOR="scanner_combinator";
	public static final String CONNECTOR_CONDITION_COMBINATOR="connectorConditionCombinator";
	public static final String SCANNER_CONDITION_COMBINATOR="scannerConditionCombinator";
	public static final String ISKEYSTROKE="isKeyStroke";
	public static final String ISKEYEVENT="isKeyEvent";
	public static final String CUSTOMIZE_OPERATOR="customizeOperator";
	public static final String MATCH_VALUE_S="match_value";
	public static final String KEY_STROKE="key_stroke";
	public static final String MATCH="match";
	public static final String ACTIVITIES="activities";
	public static final String LOOK_BACK="look_back";
	public static final String OBSERVER="observer";
	public static final String TRIGGER_ON_CLOSE="trigger_on_close";
	public static final String DYNAMICANDSTATIC="dynamicandstatic";
	public static final String STATICANDDYNAMIC="staticanddynamic";
	public static final String DYNAMIC="dynamic";
	public static final String MODIFY="Modify";
	
	private static final Logger LOG = LoggerFactory.getLogger(JsonUtils.class);

	public JSONDataModel parseJson(String jsonFromat1) {
		List<Source> listSource = new ArrayList<Source>();
		List<Scanner> listScanner = new ArrayList<Scanner>();
		List<Match> listMatch = new ArrayList<Match>();
		List<Condition> listCondition = new ArrayList<Condition>();
		JSONDataModel jsonDataModel = new JSONDataModel();
		Map<String, List<String>> mapChild = new HashMap<>();
		List<String> keyList = new ArrayList<>();
		try {

			JSONObject jObject = new JSONObject(jsonFromat1);
			JSONArray arr = jObject.getJSONArray(CELLS);
			List<String> targetId = new ArrayList<String>();
			// 1st iteration to capture the source and target id's
			for (int i = 0; i < arr.length(); i++) {
				if (arr.getJSONObject(i).has(TYPE)) {
					// 1. check for standard.Link & Getting id's first
					if ((String.valueOf(arr.getJSONObject(i).get(TYPE))).equalsIgnoreCase(STANDARD_LINK)) {

						Source source = new Source();
						source.setId((String) arr.getJSONObject(i).getJSONObject(SOURCE).get(ID));
						source.setPort((String) arr.getJSONObject(i).getJSONObject(SOURCE).get(PORT));

						// checking for target
						if (arr.getJSONObject(i).has(TARGET)) {
							Target target = new Target();
							target.setId((String) arr.getJSONObject(i).getJSONObject(TARGET).get(ID));
							target.setPort((String) arr.getJSONObject(i).getJSONObject(TARGET).get(PORT));
							source.setTarget(target);

						}
						listSource.add(source);
						jsonDataModel.setSources(listSource);

						if (mapChild.containsKey((String) arr.getJSONObject(i).getJSONObject(SOURCE).get(ID))) {
							if (arr.getJSONObject(i).has(TARGET)) {
								mapChild.get((String) arr.getJSONObject(i).getJSONObject(SOURCE).get(ID));
								targetId.add((String) arr.getJSONObject(i).getJSONObject(TARGET).get(ID));
								mapChild.put((String) arr.getJSONObject(i).getJSONObject(SOURCE).get(ID), targetId);

							}
						} else {
							List<String> targetId1 = new ArrayList<String>();
							targetId1.add((String) arr.getJSONObject(i).getJSONObject(TARGET).get(ID));
							mapChild.put((String) arr.getJSONObject(i).getJSONObject(SOURCE).get(ID), targetId1);
							keyList.add((String) arr.getJSONObject(i).getJSONObject(SOURCE).get(ID));
						}
						jsonDataModel.setChildMap(mapChild);
						jsonDataModel.setKeyList(keyList);
					}
					/*
					 * from Here JSON will parse the each template
					 */
					if (arr.getJSONObject(i).has(TYPE_NAME)) {
						// 1. check for Scanner
						if ((String.valueOf(arr.getJSONObject(i).get(TYPE_NAME))).equalsIgnoreCase(SCANNER)) {
							Scanner scanner = new Scanner();
							// scanner.setType(APP_SCANNER); // arr.getJSONObject(i).getString(TYPE)
							// should come from UI,
							scanner.setTypeName((String) arr.getJSONObject(i).getString(TYPE_NAME));
							// checking fields object if present then proceed
							if (arr.getJSONObject(i).has(FIELDS)) {
								/*
								 * condition to take field values Scanner wise [ex AppScanner, FileScanner &
								 * EmailScanner]
								 */
								if ((String.valueOf(arr.getJSONObject(i).get(TYPE)))
										.equalsIgnoreCase(HTML_APP_SCAN)) {
									scanner.setType(APP_SCANNER);
									// setting validapp in AppScanner
									if (arr.getJSONObject(i).getJSONObject(FIELDS).has("app")) {
										if (arr.getJSONObject(i).getJSONObject(FIELDS)
												.get("app") instanceof JSONArray) {
											JSONArray arValiapp = arr.getJSONObject(i).getJSONObject(FIELDS)
													.getJSONArray("app");
											String[] valid_app = new String[arValiapp.length()];
											for (int j = 0; j < arValiapp.length(); j++) {
												valid_app[j] = arValiapp.getString(j);
											}
											scanner.setValid_apps(valid_app);
										} else {
											scanner.setValid_apps(new String[] { arr.getJSONObject(i)
													.getJSONObject(FIELDS).get("app").toString() });
										}

									}
									if (arr.getJSONObject(i).getJSONObject(FIELDS).has(CUSTOMAPP)) {
										scanner.setCustomApp((String) arr.getJSONObject(i).getJSONObject(FIELDS)
												.getString(CUSTOMAPP));
									}
									if (arr.getJSONObject(i).getJSONObject(FIELDS).has(TRIGGERONCLOSE)) {
										if (arr.getJSONObject(i).getJSONObject(FIELDS)
												.getBoolean(TRIGGERONCLOSE) == true)
											scanner.setTriggerOnClose("True");
										else
											scanner.setTriggerOnClose(FALSE_FCAPS);
									}
								} else if ((String.valueOf(arr.getJSONObject(i).get(TYPE)))
										.equalsIgnoreCase(HTML_FILE_SCAN)) {
									scanner.setType(FS_SCANNER);
									scanner.setValid_apps(new String[] {
											arr.getJSONObject(i).getJSONObject(FIELDS).getString("app") });
									scanner.setFs_valid_app(
											arr.getJSONObject(i).getJSONObject(FIELDS).getString("app")); // New
									if (arr.getJSONObject(i).getJSONObject(FIELDS).has(CUSTOM_PATH)) {
										scanner.setCustomPath((String) arr.getJSONObject(i).getJSONObject(FIELDS)
												.getString(CUSTOM_PATH));
									}
									if (arr.getJSONObject(i).getJSONObject(FIELDS).has(LOOK_FOR_SUBFOLDER)) {
										scanner.setLookForSubfolder(arr.getJSONObject(i).getJSONObject(FIELDS)
												.getBoolean(LOOK_FOR_SUBFOLDER));
									}
								} else if ((String.valueOf(arr.getJSONObject(i).get(TYPE)))
										.equalsIgnoreCase(HTML_EMAIL_SCAN)) {
									scanner.setType(EMAIL_SCANNER);
									if (arr.getJSONObject(i).getJSONObject(FIELDS).has(LOOK_FOR_PAST_EMAIL)) {
										scanner.setEmail_valid_app("Outlook"); // New
										scanner.setValid_apps(new String[] { "Outlook" });
										String lookformpastemail = "";
										if (arr.getJSONObject(i).getJSONObject(FIELDS)
												.getBoolean(LOOK_FOR_PAST_EMAIL) == true) {
										}else {
											lookformpastemail = FALSE_FCAPS;
										scanner.setLookForPastEmail(lookformpastemail);
										}
									}
									if (arr.getJSONObject(i).getJSONObject(FIELDS).has(TIME_FIELD)) {
										scanner.setLook_back_time(
												arr.getJSONObject(i).getJSONObject(FIELDS).getString(TIME_FIELD));
									}
								}
							}
							// getting id
							scanner.setId((String) arr.getJSONObject(i).getString(ID));
							listScanner.add(scanner);
							jsonDataModel.setScanners(listScanner);
						}
						// 2. check for Match
						else if ((String.valueOf(arr.getJSONObject(i).get(TYPE_NAME))).equalsIgnoreCase("Match")) {
							Match match = new Match();
							match.setType(arr.getJSONObject(i).getString(TYPE));
							match.setTypeName(arr.getJSONObject(i).getString(TYPE_NAME));
							if (arr.getJSONObject(i).has(FIELDS)) {
								match.setHeader(arr.getJSONObject(i).getJSONObject(FIELDS).getString(HEADER));
								if (arr.getJSONObject(i).getJSONObject(FIELDS).has("message")) {
									match.setMessage(arr.getJSONObject(i).getJSONObject(FIELDS).getString("message"));
								}
								if (arr.getJSONObject(i).getJSONObject(FIELDS).has(TYPE)) {
									match.setAttributeType(
											arr.getJSONObject(i).getJSONObject(FIELDS).getString(TYPE));
								}
								match.setOperator(convertOperator(
										arr.getJSONObject(i).getJSONObject(FIELDS).getString(OPERATOR))); // converting
																												// operator
																												// val
																												// here
								match.setMatchType(arr.getJSONObject(i).getJSONObject(FIELDS).getString(MATCH_TYPE));
								if (arr.getJSONObject(i).getJSONObject(FIELDS).has(MATCH_ATTR)) {
									String matchattr = arr.getJSONObject(i).getJSONObject(FIELDS)
											.getString(MATCH_ATTR);
									match.setMatchattr(matchattr);
									if (matchattr.equalsIgnoreCase(INPUT_BUFFER_KEYSTROKE)) // check for KeyStroke
										match.setKeyStroke(true);
									if (matchattr.equalsIgnoreCase(INPUT_BUFFER_KEYEVENT)) // check for keyEvent
										match.setIskeyEvent(true);

								}
								if (arr.getJSONObject(i).getJSONObject(FIELDS).has(STATIC_DATA)) {
									match.setStaticData(
											arr.getJSONObject(i).getJSONObject(FIELDS).getString(STATIC_DATA));
								}
								if (arr.getJSONObject(i).getJSONObject(FIELDS).has(DYNAMIC_FIELD)) {
									match.setStaticData(
											arr.getJSONObject(i).getJSONObject(FIELDS).getString(DYNAMIC_FIELD));
									match.setDefaultValueForDynamic(arr.getJSONObject(i).getJSONObject(FIELDS)
											.getString(DEFAULT_VALUE_FOR_DYNAMIC));
								}

								// getting port
								if (arr.getJSONObject(i).has(PORTS)) {
									JSONArray items = arr.getJSONObject(i).getJSONObject(PORTS).getJSONArray(ITEMS);
									match.setPort((String) items.getJSONObject(0).get(ID));
								}
							}
							// getting id
							match.setId(arr.getJSONObject(i).getString(ID));
							listMatch.add(match);
							jsonDataModel.setMatches(listMatch);
						}
						// 3. check for Condition
						else if ((String.valueOf(arr.getJSONObject(i).get(TYPE)))
								.equalsIgnoreCase(HTML_APP_CONDITION)) {
							Condition condition = new Condition();
							condition.setType(arr.getJSONObject(i).getString(TYPE));
							condition.setTypeName(arr.getJSONObject(i).getString(TYPE_NAME));
							if (arr.getJSONObject(i).has(FIELDS)) {
								condition.setHeader(arr.getJSONObject(i).getJSONObject(FIELDS).getString(HEADER));
								// checking for AND/OR
								if (arr.getJSONObject(i).getJSONObject(FIELDS).has("mode")) {
									boolean conditionType = arr.getJSONObject(i).getJSONObject(FIELDS)
											.getBoolean("mode");
									if (conditionType == true)
										condition.setConditionType(false);
									else
										condition.setConditionType(true);
								}
								if (arr.getJSONObject(i).getJSONObject(FIELDS).has("and"))
									condition.setConditionType(
											arr.getJSONObject(i).getJSONObject(FIELDS).getBoolean("and"));
							}
							// getting id
							condition.setId(arr.getJSONObject(i).getString(ID));
							// getting port
							if (arr.getJSONObject(i).has(PORTS)) {
								JSONArray items = arr.getJSONObject(i).getJSONObject(PORTS).getJSONArray(ITEMS);
								condition.setPort((String) items.getJSONObject(0).get(ID));
							}
							listCondition.add(condition);
							jsonDataModel.setConditions(listCondition);
						}
						// 4. check for Notification
						else if ((String.valueOf(arr.getJSONObject(i).get(TYPE_NAME)))
								.equalsIgnoreCase(NOTIFICATION)) {
							ConnectorNotification notification = new ConnectorNotification();
							notification.setType(arr.getJSONObject(i).getString(TYPE));
							notification.setTypeName(arr.getJSONObject(i).getString(TYPE_NAME));
							if (arr.getJSONObject(i).has(FIELDS)) {
								notification
										.setSelectType(arr.getJSONObject(i).getJSONObject(FIELDS).getString("type0"));
								if (arr.getJSONObject(i).getJSONObject(FIELDS).has(ACTION_NAME0)) {
									String actionName = arr.getJSONObject(i).getJSONObject(FIELDS)
											.getString(ACTION_NAME0);
									if (actionName.equalsIgnoreCase("startTask"))
										notification.setActionName("start");
									else
										notification.setActionName("stop");

								}
							}
							// getting port
							if (arr.getJSONObject(i).has(PORTS)) {
								JSONArray items = arr.getJSONObject(i).getJSONObject(PORTS).getJSONArray(ITEMS);
								notification.setPort((String) items.getJSONObject(0).get(ID));
							}
							notification.setId(arr.getJSONObject(i).getString(ID));
							jsonDataModel.setNotification(notification);

						}
						// 5. check for Scanner Connector
						else if ((String.valueOf(arr.getJSONObject(i).get(TYPE)))
								.equalsIgnoreCase(HTML_CONNECTOR_CONDITION)) {
							Connector connector = new Connector();
							connector.setType(arr.getJSONObject(i).getString(TYPE));
							connector.setTypeName(arr.getJSONObject(i).getString(TYPE_NAME));
							connector.setId(arr.getJSONObject(i).getString(ID));
							if (arr.getJSONObject(i).has(FIELDS)) {
								connector.setHeader(arr.getJSONObject(i).getJSONObject(FIELDS).getString(HEADER));
								if (arr.getJSONObject(i).getJSONObject(FIELDS).has("mode")) {
									boolean andType = arr.getJSONObject(i).getJSONObject(FIELDS).getBoolean("mode");
									if (andType == false)
										connector.setAnd(true);
									else
										connector.setAnd(false);

								}
								if (arr.getJSONObject(i).getJSONObject(FIELDS).has("and"))
									connector.setAnd(arr.getJSONObject(i).getJSONObject(FIELDS).getBoolean("and"));
							}
							// getting port
							if (arr.getJSONObject(i).has(PORTS)) {
								JSONArray items = arr.getJSONObject(i).getJSONObject(PORTS).getJSONArray(ITEMS);
								connector.setPort((String) items.getJSONObject(0).get(ID));
							}
							jsonDataModel.setConnector(connector);
						}
					}
				}
			}

		} catch (Exception e) {
			 LOG.error("ERROR IN PARSING JSON:: {}",e.getMessage());
		}
		return jsonDataModel;
	}

	public String getParsedRuleJsonFromRuleJson(String rappidJson,boolean isManualValidation) throws JsonProcessingException {

		JSONDataModel dataModel = getNotification(rappidJson);
		ConnectorNotification notification = dataModel.getNotification();
		String action = "";
		if (notification != null) {
			action = notification.getActionName();
		}
		Map<String, String> rootScannerList = getRootScannerList(rappidJson,isManualValidation);
		Map<String, Map<String, String>> cellAttributeInfo = getIdTypeAndVale(rappidJson,isManualValidation);
		Map<String, List<String>> ruleMap = new HashMap<>();

		for (Map.Entry<String, String> entry : rootScannerList.entrySet()) {
			String key = entry.getKey();
			String value = entry.getValue();
			if (key.isEmpty()) {
				List<String> activityList = getChildScanner(value, cellAttributeInfo, rappidJson);
				ruleMap.put(value, activityList);
			} else {
				List<String> activityList = getChildScanner(key, cellAttributeInfo, rappidJson);
				ruleMap.put(key, activityList);
			}
		}

		LOG.info("++++++++++++++++++++******+");
		LOG.info("ruleMap: {}",ruleMap);

		LOG.info("++++++++++++++++++********+++");

		ParentRuleModel parentRuleModel = new ParentRuleModel();

		LOG.info("RuleMap iteration starts=======================================");
		Map<String, List<Activity>> activityMap = new HashMap<String, List<Activity>>();
		for (Map.Entry<String, List<String>> entry : ruleMap.entrySet()) {

			String ruleParent = entry.getKey();
			parentRuleModel.setCombinator(getRootCombinator(cellAttributeInfo, parentRuleModel, ruleParent));

			LOG.info("key = {}" , ruleParent);
			Rule rule = new Rule();
			rule.setCombinator(getCombinator(cellAttributeInfo, parentRuleModel, ruleParent));
			List<Activity> activities = new LinkedList<Activity>();
			List<String> parsedIDs = new ArrayList<String>();
			for (String scannerID : entry.getValue()) {

				if (parsedIDs.contains(scannerID)) {
					continue;
				}
				parsedIDs.add(scannerID);
				LOG.info(SCANNERID , scannerID);
				Activity activity = getActivity(cellAttributeInfo, scannerID, rappidJson);

				List<String> listOfAllChildScanner = getAllChildScanner(scannerID, rappidJson, cellAttributeInfo);
				
				setChildsToActivity(listOfAllChildScanner, parsedIDs, cellAttributeInfo, rappidJson, activity, scannerID);
				
				LOG.info("RuleMap parent condition starts=======================================");

				setConditionForScannerActivity(rappidJson, cellAttributeInfo, scannerID, activity);

				LOG.info("RuleMap child condition ends=======================================");

				LOG.info(SCANNERID , scannerID);
				LOG.info("activity = {}" , activity);
				LOG.info("object to json=======================================");
				LOG.info(AppUtil.convertClassObjectToJson(activity));
				activities.add(activity);
			}
			activityMap.put(ruleParent, activities);
			rule.setActivities(activities);
			parentRuleModel.getRule().add(rule);
			LOG.info("ruleModel = {}" , rule);
		}
		LOG.info("activityMap = {}" , activityMap);
		LOG.info("parentRuleModel = {}" , parentRuleModel);

		LOG.info("parentRuleModel to json=======================================");
		LOG.info(AppUtil.convertClassObjectToJson(parentRuleModel));

		Map<String, ParentRuleModel> finalRuleMap = new HashMap<String, ParentRuleModel>();
		finalRuleMap.put("rule", parentRuleModel);

		LOG.info("finalRuleMap to json=======================================");
		String finalRuleMapString = AppUtil.convertClassObjectToJson(finalRuleMap);
		LOG.info(finalRuleMapString);

		LOG.info("RuleMap iteration ends=======================================");
		Trigger trigger = new Trigger(new Notification(new Http(action)));
		ObjectMapper mapper = new ObjectMapper();
		JSONObject newObject = new JSONObject(finalRuleMapString);
		LOG.info(mapper.writeValueAsString(trigger));
		newObject.put("trigger", new JSONObject(mapper.writeValueAsString(trigger)));

		finalRuleMapString = newObject.toString();
		LOG.info("trigger value string: {}" , finalRuleMapString);
		return finalRuleMapString;
	}


	private int setChildsToActivity(List<String> listOfAllChildScanner, List<String> parsedIDs,
			Map<String, Map<String, String>> cellAttributeInfo, String rappidJson, Activity activity, String scannerID) throws JsonProcessingException {
		
		if(listOfAllChildScanner.size()>0) {
			
			List<String> childScanners = getChildScanner(scannerID, cellAttributeInfo, rappidJson);
			if (CollectionUtils.isNotEmpty(childScanners)) {
				for (String childScannerID : childScanners) {

					if (parsedIDs.contains(childScannerID)) {
						continue;
					}
					parsedIDs.add(childScannerID);

					Activity child = getActivity(cellAttributeInfo, childScannerID, rappidJson);
					activity.setChild(child);

					listOfAllChildScanner.remove(childScannerID);
					
					if (listOfAllChildScanner.size() > 0) {
						setChildsToActivity(listOfAllChildScanner, parsedIDs, cellAttributeInfo, rappidJson, child, childScannerID);
					} else {
						break;
					}
				}
			}
			return 1;
		} else {
			return 0;
		}
		
	}

	private Activity getActivity(Map<String, Map<String, String>> cellAttributeInfo, String scannerID, 
			String rappidJson) throws JsonProcessingException {
		
			Activity activity = null;


			if (StringUtils.equalsIgnoreCase(cellAttributeInfo.get(scannerID).get(TYPE),
					HTML_EMAIL_SCAN)) {

				activity = new EmailActivity();
				((EmailActivity) activity).setLook_back(cellAttributeInfo.get(scannerID).get(LOOKBACK));
				((EmailActivity) activity).setLook_back_time(
						Integer.parseInt(cellAttributeInfo.get(scannerID).get(LOOK_BACK_TIME)));
			} else if (StringUtils.equalsIgnoreCase(cellAttributeInfo.get(scannerID).get(TYPE),
					HTML_FILE_SCAN)) {


				activity = new FileActivity();
				((FileActivity) activity).getObserver()
						.setPath(cellAttributeInfo.get(scannerID).get(CUSTOM_PATH));
				((FileActivity) activity).getObserver().setRecursive(cellAttributeInfo.get(scannerID).get(LOOK_FOR_SUBFOLDER));
				((FileActivity) activity).getObserver()
						.setReference(cellAttributeInfo.get(scannerID).get(VALID_APPS));
			} else {
				activity = new AppActivity();
				((AppActivity) activity).setTrigger_on_close(cellAttributeInfo.get(scannerID).get(TRIGGERONCLOSE));
			}

			activity.setEvent_module(cellAttributeInfo.get(scannerID).get("event_module"));
			activity.setScanner_combinator(cellAttributeInfo.get(scannerID).get(SCANNER_COMBINATOR));
			String[] validApps = cellAttributeInfo.get(scannerID).get(VALID_APPS).split(",");
			activity.setValid_apps(validApps);

			setConditionForScannerActivity(rappidJson, cellAttributeInfo, scannerID,
					activity);
			LOG.info(SCANNERID , scannerID);
			return activity;
	}

	private JSONDataModel getNotification(String rappidJson) {

		JSONDataModel jsonDataModel = new JSONDataModel();
		try {

			JSONObject jObject = new JSONObject(rappidJson);
			JSONArray arr = jObject.getJSONArray(CELLS);
			// 1st iteration to capture the source and target id's
			for (int i = 0; i < arr.length(); i++) {
				if (arr.getJSONObject(i).has(TYPE) && arr.getJSONObject(i).has(TYPE_NAME)) {
					if ((String.valueOf(arr.getJSONObject(i).get(TYPE_NAME))).equalsIgnoreCase(NOTIFICATION)) {
						ConnectorNotification notification = new ConnectorNotification();
						notification.setType(arr.getJSONObject(i).getString(TYPE));
						notification.setTypeName(arr.getJSONObject(i).getString(TYPE_NAME));
						if (arr.getJSONObject(i).has(FIELDS)) {
							notification.setSelectType(arr.getJSONObject(i).getJSONObject(FIELDS).getString("type0"));
							if (arr.getJSONObject(i).getJSONObject(FIELDS).has(ACTION_NAME0)) {
								String actionName = arr.getJSONObject(i).getJSONObject(FIELDS)
										.getString(ACTION_NAME0);
								if (actionName.equalsIgnoreCase("startTask"))
									notification.setActionName("start");
								else
									notification.setActionName("stop");

							}
						}
						// getting port
						if (arr.getJSONObject(i).has(PORTS)) {
							JSONArray items = arr.getJSONObject(i).getJSONObject(PORTS).getJSONArray(ITEMS);
							notification.setPort((String) items.getJSONObject(0).get(ID));
						}
						notification.setId(arr.getJSONObject(i).getString(ID));
						jsonDataModel.setNotification(notification);
					}
				}
			}
		} catch (Exception e) {
			LOG.error("ERROR IN PARSING JSON:: {}",e.getMessage());
			e.printStackTrace();
		}
		return jsonDataModel;

	}


	private String getRootCombinator(Map<String, Map<String, String>> cellAttributeInfo,
			ParentRuleModel parentRuleModel, String ruleParent) {

		String combinator = "and";
		if (StringUtils.equalsIgnoreCase(cellAttributeInfo.get(ruleParent).get(TYPE), HTML_CONNECTOR_CONDITION)) {

			combinator = cellAttributeInfo.get(ruleParent).get(CONNECTOR_CONDITION_COMBINATOR);
		}

		return combinator;
	}

	private String getCombinator(Map<String, Map<String, String>> cellAttributeInfo, ParentRuleModel parentRuleModel,
			String ruleParent) {

		String combinator = "";
		if (StringUtils.equalsIgnoreCase(cellAttributeInfo.get(ruleParent).get(TYPE),
				HTML_CONNECTOR_CONDITION)) {

			combinator = cellAttributeInfo.get(ruleParent).get(CONNECTOR_CONDITION_COMBINATOR);
		} else if(StringUtils.equalsIgnoreCase(cellAttributeInfo.get(ruleParent).get(TYPE_NAME), SCANNER)) {
			combinator = cellAttributeInfo.get(ruleParent).get(SCANNER_COMBINATOR);
		}

		return combinator;
	}

	private void setConditionForScannerActivity(String rappidJson, Map<String, Map<String, String>> cellAttributeInfo,
			String scannerID, Activity activity) throws JsonProcessingException {

		activity.setConditions(getConditionForScanner(rappidJson, cellAttributeInfo, scannerID, activity));
	}

	private Map<String, Object> getConditionForScanner(String rappidJson,
			Map<String, Map<String, String>> cellAttributeInfo, String scannerID, Activity activity)
			throws JsonProcessingException {

		List<String> immediateChildListForScanner = getChildFromParentNode(rappidJson, scannerID);

		String conditions = ConditionEnum.CONDITION_STRING;
		List<String> conditionListForScannerMatch = new LinkedList<String>();
		conditions = conditions.replace("PARENT_PORT", cellAttributeInfo.get(scannerID).get(SCANNER_COMBINATOR));


		for (String immediatechildForScanner : immediateChildListForScanner) {

			String htmlMatchType = cellAttributeInfo.get(immediatechildForScanner).get(TYPE);
			if (StringUtils.equalsIgnoreCase(htmlMatchType, "html.AppMatch")
					|| StringUtils.equalsIgnoreCase(htmlMatchType, "html.EmailMatch")
					|| StringUtils.equalsIgnoreCase(htmlMatchType, "html.FileMatch")) {

				String conditionChildMatch = getConditionChildMatch(immediatechildForScanner, cellAttributeInfo, activity,
						scannerID, rappidJson, cellAttributeInfo.get(scannerID).get(SCANNER_COMBINATOR), immediateChildListForScanner,"");

				conditionListForScannerMatch.add(conditionChildMatch);
			} else if (StringUtils.equalsIgnoreCase(htmlMatchType, HTML_APP_CONDITION)
					|| StringUtils.equalsIgnoreCase(htmlMatchType, "html.EmailCondition")
					|| StringUtils.equalsIgnoreCase(htmlMatchType, "html.FileCondition")) {

				List<String> immediateChildListForScannerCondition = getChildFromParentNode(rappidJson,
						immediatechildForScanner);
				String conditionConnector = ConditionEnum.CONDITION_STRING;
				List<String> conditionListForConditionMatch = new LinkedList<String>();
				conditionConnector = conditionConnector.replace("PARENT_PORT",
						cellAttributeInfo.get(immediatechildForScanner).get(SCANNER_CONDITION_COMBINATOR));
				for (String immediateChildForScannerCondition : immediateChildListForScannerCondition) {

					String matchType = cellAttributeInfo.get(immediateChildForScannerCondition).get(TYPE);
					if (StringUtils.equalsIgnoreCase(matchType, "html.AppMatch")
							|| StringUtils.equalsIgnoreCase(matchType, "html.EmailMatch")
							|| StringUtils.equalsIgnoreCase(matchType, "html.FileMatch")) {

						String conditionChildMatch = getConditionChildMatch(immediateChildForScannerCondition,
								cellAttributeInfo, activity, scannerID, rappidJson,
								cellAttributeInfo.get(immediatechildForScanner).get(SCANNER_CONDITION_COMBINATOR),
								immediateChildListForScanner, immediatechildForScanner);

						conditionListForConditionMatch.add(conditionChildMatch);
					}
				}
				conditionConnector = conditionConnector.replace("CHILD_MATCH",
						conditionListForConditionMatch.toString());
				conditionListForScannerMatch.add(conditionConnector);
			}
		}

		conditions = conditions.replace("CHILD_MATCH", conditionListForScannerMatch.toString());
		if (conditions.contains("{\"and\":[]}")) {
			conditions = conditions.replace("{\"and\":[]}","{}");
		}
		return new JSONObject(conditions).toMap();
	}

	private String getConditionChildMatch(String matchChild, Map<String, Map<String, String>> cellAttributeInfo,
			Activity activity, String scannerID, String rappidJson, String parentCombinatorPort,
			List<String> immediateChildListForScanner, String conditionChildID) throws JsonProcessingException {

		String conditionChildMatch = "";

		if (Boolean.parseBoolean(cellAttributeInfo.get(matchChild).get(ISKEYSTROKE))
				|| Boolean.parseBoolean(cellAttributeInfo.get(matchChild).get(ISKEYEVENT))) {

			
			conditionChildMatch = getKeyMatchCondition(cellAttributeInfo, matchChild, activity, scannerID, rappidJson,
					parentCombinatorPort, immediateChildListForScanner, conditionChildID);
			
		} else if (StringUtils.equalsIgnoreCase(cellAttributeInfo.get(matchChild).get(MATCH_ATTR),
				"nested_level")) {

			conditionChildMatch = ConditionEnum.NESTED_LEVEL;
			conditionChildMatch = conditionChildMatch.replace("NESTED_LEVEL",
					cellAttributeInfo.get(matchChild).get(MATCH_ATTR));
			conditionChildMatch = conditionChildMatch.replace("OPERATOR_VALUE",
					cellAttributeInfo.get(matchChild).get(CUSTOMIZE_OPERATOR));
			conditionChildMatch = conditionChildMatch.replace("DEPTH_VALUE",
					cellAttributeInfo.get(matchChild).get(MATCH_VALUE_S));
			((FileActivity) activity).getObserver().setRecursive("True");

		} else {

			conditionChildMatch = ConditionEnum.CHILD_MATCH;
			conditionChildMatch = conditionChildMatch.replace(OPERATOR,
					cellAttributeInfo.get(matchChild).get(CUSTOMIZE_OPERATOR));
			conditionChildMatch = conditionChildMatch.replace("MATCH_VALUE",
					cellAttributeInfo.get(matchChild).get(MATCH_VALUE_S));
		}

		conditionChildMatch = conditionChildMatch.replace("MATCH_ATTR",
				cellAttributeInfo.get(matchChild).get(MATCH_ATTR));
		return conditionChildMatch;
	}

	private String getKeyMatchCondition(Map<String, Map<String, String>> cellAttributeInfo,
			String keyMatchChild, Activity activity, String scannerID, String rappidJson,
			String immediateParentPort, List<String> immediateChildMatchesForScanner, String conditionChildID) throws JsonProcessingException {

		String conditionChildMatch = ConditionEnum.KEY_MATCH;
		if (Boolean.parseBoolean(cellAttributeInfo.get(keyMatchChild).get(ISKEYSTROKE))) {
			conditionChildMatch = conditionChildMatch.replace("KEY_MATCH", KEY_STROKE);
		} else {
			conditionChildMatch = conditionChildMatch.replace("KEY_MATCH", "key_event");
		}
		KeyMatchValue keyMatchValue = new KeyMatchValue();

		keyMatchValue.setApplication_name(activity.getValid_apps());
		keyMatchValue.setText_search(cellAttributeInfo.get(keyMatchChild).get(MATCH_VALUE_S));
		keyMatchValue.getOperator()
				.setOperator_text_search(cellAttributeInfo.get(keyMatchChild).get(CUSTOMIZE_OPERATOR));
		
		
		if(immediateChildMatchesForScanner.contains(keyMatchChild)) {

			if(StringUtils.equalsIgnoreCase(immediateParentPort, "and")) {

				//keep scanner's all childs value
				List<String> scannerChildMatchsWithExcludedKey = getChildMatchWithExcludedKey(scannerID, rappidJson,
						cellAttributeInfo, new String[] { INPUT_BUFFER_KEYSTROKE, INPUT_BUFFER_KEYEVENT });
				setKeyMatchValue(rappidJson, cellAttributeInfo,keyMatchValue, scannerChildMatchsWithExcludedKey);
			} else {
				//avoid scanner's all childs values
			}
		} else {

			String scannerPort = getPortValue(scannerID, rappidJson);
			
			if(StringUtils.equalsIgnoreCase(immediateParentPort, "or")) {

				if(StringUtils.equalsIgnoreCase(scannerPort, "and")) {

					//keep scanner's all childs value
					List<String> scannerChildMatchsWithExcludedKey = getChildMatchWithExcludedKey(scannerID, rappidJson,
							cellAttributeInfo, new String[] { INPUT_BUFFER_KEYSTROKE, INPUT_BUFFER_KEYEVENT });
					setKeyMatchValue(rappidJson, cellAttributeInfo,keyMatchValue, scannerChildMatchsWithExcludedKey);
				} else {
					//avoid scanner's all childs values
				}
			} else {

				if(StringUtils.equalsIgnoreCase(scannerPort, "and")) {

					//keep scanner's all childs value along with siblings
					List<String> scannerAllChildMatchsWithExcludedKey = getChildAllMatchExcludeKey(scannerID, rappidJson, cellAttributeInfo,
							new String[] { INPUT_BUFFER_KEYSTROKE, INPUT_BUFFER_KEYEVENT });
					setKeyMatchValue(rappidJson, cellAttributeInfo,keyMatchValue, scannerAllChildMatchsWithExcludedKey);
				} else {
					//avoid scanner's all childs values and keep siblings
					List<String> scannerChildMatchsWithExcludedKey = getChildMatchWithExcludedKey(conditionChildID, rappidJson,
							cellAttributeInfo, new String[] { INPUT_BUFFER_KEYSTROKE, INPUT_BUFFER_KEYEVENT });
					setKeyMatchValue(rappidJson, cellAttributeInfo,keyMatchValue, scannerChildMatchsWithExcludedKey);
					
				}
			}
			
		}
		conditionChildMatch = conditionChildMatch.replace("KEY_VALUE_MATCH", AppUtil.convertClassObjectToJson(keyMatchValue));
		return conditionChildMatch;
	}

	private void setKeyMatchValue(String rappidJson,
			Map<String, Map<String, String>> cellAttributeInfo, KeyMatchValue keyMatchValue, List<String> onlyMatchChilds) {

		for (String onlyMatchChild : onlyMatchChilds) {

			if (StringUtils.equalsIgnoreCase(cellAttributeInfo.get(onlyMatchChild).get(MATCH_ATTR),
					"APPLICATION_TITLE")) {

				keyMatchValue.setApplication_title(cellAttributeInfo.get(onlyMatchChild).get(MATCH_VALUE_S));
				keyMatchValue.getOperator()
						.setOperator_application_title(cellAttributeInfo.get(onlyMatchChild).get(CUSTOMIZE_OPERATOR));

			} else if (StringUtils.containsIgnoreCase(cellAttributeInfo.get(onlyMatchChild).get(MATCH_ATTR),
					"sheet")) {

				keyMatchValue.setSheet_name(cellAttributeInfo.get(onlyMatchChild).get(MATCH_VALUE_S));
				keyMatchValue.getOperator()
						.setOperator_sheet_name(cellAttributeInfo.get(onlyMatchChild).get(CUSTOMIZE_OPERATOR));
			}

		}
		
	}

	public List<String> getChildListFromNode(String rappidJson, String nodeID) {

		CopyOnWriteArrayList intermediateParentIds = new CopyOnWriteArrayList();
		intermediateParentIds.add(nodeID);
		List<String> parentList = new ArrayList<>();
		parentList.add(nodeID);
		for (int i = 0; i < intermediateParentIds.size(); i++) {
			List<String> childList = getChildFromParentNode(rappidJson, (String) intermediateParentIds.get(i));
			intermediateParentIds.addAll(childList);

		}
		return intermediateParentIds;
	}

	public Map<String, String> getRootScannerList(String rappidJson,boolean isManualValidation) {

		JSONObject jObject = new JSONObject(rappidJson);
		JSONArray arr = jObject.getJSONArray(CELLS);
		Map<String, String> childParentMap = getChildMap(rappidJson);
		Map<String, Map<String, String>> cellAttributeInfo = getIdTypeAndVale(rappidJson,false);

		List<String> allScannerList = new ArrayList<>();
		Map<String, String> uniqueRule = new HashMap<String, String>();
		for (int i = 0; i < arr.length(); i++) {
			if (!(String.valueOf(arr.getJSONObject(i).get(TYPE))).equalsIgnoreCase(STANDARD_LINK)) {
				if (SCANNER.equalsIgnoreCase((String) arr.getJSONObject(i).get(TYPE_NAME))) {
					allScannerList.add((String) arr.getJSONObject(i).get(ID));
				}
			}
		}
		for (int i = 0; i < allScannerList.size(); i++) {
			String scannerParentId = childParentMap.get(allScannerList.get(i));
			if (scannerParentId.isEmpty()
					|| (cellAttributeInfo.get(scannerParentId).get(TYPE) != null && HTML_CONNECTOR_CONDITION
							.equalsIgnoreCase(cellAttributeInfo.get(scannerParentId).get(TYPE)))) {
				uniqueRule.put(scannerParentId, allScannerList.get(i));
			}

		}

		return uniqueRule;

	}

	private String getParentScanner(String childId, Map<String, Map<String, String>> cellAttributeInfo,
			String rappidJson) {
		String parentScannerId = getParentFromChildNode(rappidJson, childId);
		Map<String, String> getMapWithFieldAndValue = cellAttributeInfo.get(parentScannerId);
		if (getMapWithFieldAndValue == null) {

			System.out.println("parent scanner id is null");
			return parentScannerId;
		} else {
			Iterator iterator = getMapWithFieldAndValue.entrySet().iterator();
			String scannerId = "";
			while (iterator.hasNext()) {
				Map.Entry<String, String> mapElement = (Map.Entry<String, String>) iterator.next();
				if (StringUtils.equalsIgnoreCase(SCANNER, String.valueOf(mapElement.getValue()))) {
					scannerId = parentScannerId;
				}
			}
			if (StringUtils.isEmpty(scannerId)) {
				return getParentScanner(parentScannerId, cellAttributeInfo, rappidJson);
			} else {
				return scannerId;
			}
		}

	}

	@SuppressWarnings("unchecked")
	private List<String> getChildMatch(String parentId, String rappidJson,
			Map<String, Map<String, String>> cellAttributeInfo) {
		
		List<String> childList = getChildFromParentNode(rappidJson, parentId);
		List<String> listOfChildMatch = new ArrayList<String>();
		for (int i = 0; i < childList.size(); i++) {
			Map<String, String> map = cellAttributeInfo.get(childList.get(i));
			Iterator iterator = map.entrySet().iterator();
			while (iterator.hasNext()) {
				Map.Entry<String, String> entry = (Map.Entry<String, String>) iterator.next();
				if (StringUtils.equalsIgnoreCase(MATCH, entry.getValue())) {
					listOfChildMatch.add(childList.get(i));
				}
			}
		}
		return listOfChildMatch;

	}
	
	private List<String> getChildMatchWithExcludedKey(String parentId, String rappidJson,
			Map<String, Map<String, String>> cellAttributeInfo, String[] excludedKey) {
		
		List<String> childList = getChildFromParentNode(rappidJson, parentId);
		List<String> listOfChildMatch = new ArrayList<String>();

		for (int i = 0; i < childList.size(); i++) {
			Map<String, String> map = cellAttributeInfo.get(childList.get(i));
			String typeName = map.get(TYPE_NAME);
			String matchattr = map.get(MATCH_ATTR);
			if (StringUtils.equalsIgnoreCase(typeName, MATCH) && excludedKey.length > 0
					&& !java.util.Arrays.asList(excludedKey).contains(matchattr)) {
				listOfChildMatch.add(childList.get(i));
			}
			if (StringUtils.equalsIgnoreCase(typeName, MATCH) && excludedKey.length == 0) {
				listOfChildMatch.add(childList.get(i));

			}
		}
	
		return listOfChildMatch;

	}

	public static String convertOperator(String operatorValue) {
		String convertedVal = "";
		try {
			Map<String, String> operatorMap = new HashMap<String, String>();
			operatorMap.put("contains", "in");
			operatorMap.put("containscasesensitive", "in_i");
			operatorMap.put("equalscasesensitive", "===");
			operatorMap.put("equals", "==");
			operatorMap.put("notin", "not_in");
			operatorMap.put("notincasesensitive", "not_in_i");
			operatorMap.put("notequals", "!=");
			operatorMap.put("notequalscasesensitive", "!==");
			operatorMap.put("equalscaseinsensitive", "===");
			operatorMap.put("greaterthan", ">");
			operatorMap.put("containscaseinsensitive", "in_i");
			for (Map.Entry<String, String> set : operatorMap.entrySet()) {
				if (set.getKey().equals(operatorValue)) {
					convertedVal = set.getValue();
				}
			}
		} catch (Exception e) {
			// LOG.error("ERROR IN convertOperator METHOD: "+e.getMessage());
			e.printStackTrace();
		}
		return convertedVal;
	}

	public static String removeEmailScanKeyIfPreesent(String finalJsonStr) {
		String newJson = "";
		try {

			if (!finalJsonStr.contains(EMAIL_SCANNER) && !finalJsonStr.contains(FS_SCANNER)) {
				// if(finalJsonStr.contains("\"observer\":")) {
				// finalJsonStr=finalJsonStr.replace("\"observer\":", "\"observer\":\"\",");
				// }
				// if(finalJsonStr.contains("\"combinator\":\"rule\"")) {
				// finalJsonStr=finalJsonStr.replace("\"combinator\":\"rule\"",
				// "\"combinator\":\"\",\"rule\"");
				// }
				// if(finalJsonStr.contains(",\"conditions\":null")) {
				// finalJsonStr=finalJsonStr.replace(",\"conditions\":null",
				// ",\"conditions\":{}");
				// }
				JSONObject jobj = new JSONObject(finalJsonStr);
				JSONObject jruleObj = jobj.getJSONObject("rule");
				JSONArray jarr = jruleObj.getJSONArray("rule");
				for (int i = 0; i < jarr.length(); i++) {
					if (jarr.getJSONObject(i).has(ACTIVITIES)) {
						JSONArray activityArr = jarr.getJSONObject(i).getJSONArray(ACTIVITIES);
						for (int j = 0; j < activityArr.length(); j++) {
							if (activityArr.getJSONObject(j).has(LOOK_BACK)) {
								activityArr.getJSONObject(j).remove(LOOK_BACK);
							}
							if (activityArr.getJSONObject(j).has(LOOK_BACK_TIME)) {
								activityArr.getJSONObject(j).remove(LOOK_BACK_TIME);
							}
							if (activityArr.getJSONObject(j).has(OBSERVER)) {
								activityArr.getJSONObject(j).remove(OBSERVER);
							}
						}
					}
				}
				newJson = jobj.toString();

			} else if (finalJsonStr.contains(EMAIL_SCANNER) && !finalJsonStr.contains(FS_SCANNER)) {

				JSONObject jobj = new JSONObject(finalJsonStr);
				JSONObject jruleObj = jobj.getJSONObject("rule");
				JSONArray jarr = jruleObj.getJSONArray("rule");
				for (int i = 0; i < jarr.length(); i++) {
					if (jarr.getJSONObject(i).has(ACTIVITIES)) {
						JSONArray activityArr = jarr.getJSONObject(i).getJSONArray(ACTIVITIES);
						for (int j = 0; j < activityArr.length(); j++) {
							if (activityArr.getJSONObject(j).has(OBSERVER)) {
								activityArr.getJSONObject(j).remove(OBSERVER);
							}
							if (activityArr.getJSONObject(j).has(TRIGGER_ON_CLOSE)) {
								activityArr.getJSONObject(j).remove(TRIGGER_ON_CLOSE);
							}
						}
					}
				}
				newJson = jobj.toString();
			} else if (finalJsonStr.contains(APP_SCANNER) && finalJsonStr.contains(FS_SCANNER)) {
				if (finalJsonStr.contains("\"observer\":\"conditions\"")) {
					finalJsonStr = finalJsonStr.replace("\"observer\":\"conditions\"",
							"\"observer\":\"\",\"conditions\"");
				}
				JSONObject jobj = new JSONObject(finalJsonStr);
				JSONObject jruleObj = jobj.getJSONObject("rule");
				JSONArray jarr = jruleObj.getJSONArray("rule");
				for (int i = 0; i < jarr.length(); i++) {
					if (jarr.getJSONObject(i).toString().contains(APP_SCANNER)) {
						if (jarr.getJSONObject(i).has(ACTIVITIES)) {
							JSONArray activityArr = jarr.getJSONObject(i).getJSONArray(ACTIVITIES);
							for (int j = 0; j < activityArr.length(); j++) {
								if (activityArr.getJSONObject(j).has(LOOK_BACK)) {
									activityArr.getJSONObject(j).remove(LOOK_BACK);
								}
								if (activityArr.getJSONObject(j).has(LOOK_BACK_TIME)) {
									activityArr.getJSONObject(j).remove(LOOK_BACK_TIME);
								}
								if (activityArr.getJSONObject(j).has(OBSERVER)) {
									activityArr.getJSONObject(j).remove(OBSERVER);
								}
							}
						}
					}
					if (jarr.getJSONObject(i).toString().contains(FS_SCANNER)) {
						if (jarr.getJSONObject(i).has(ACTIVITIES)) {
							JSONArray activityArr = jarr.getJSONObject(i).getJSONArray(ACTIVITIES);
							for (int j = 0; j < activityArr.length(); j++) {
								if (activityArr.getJSONObject(j).has(LOOK_BACK)) {
									activityArr.getJSONObject(j).remove(LOOK_BACK);
								}
								if (activityArr.getJSONObject(j).has(LOOK_BACK_TIME)) {
									activityArr.getJSONObject(j).remove(LOOK_BACK_TIME);
								}
								if (activityArr.getJSONObject(j).has(TRIGGER_ON_CLOSE)) {
									activityArr.getJSONObject(j).remove(TRIGGER_ON_CLOSE);
								}
							}
						}
					}
				}
				newJson = jobj.toString();
			} else if (finalJsonStr.contains(FS_SCANNER) && !finalJsonStr.contains(APP_SCANNER)) {
				if (finalJsonStr.contains("\"path\":\"recursive\"")) {
					finalJsonStr = finalJsonStr.replace("\"path\":\"recursive\"", "\"path\":\"\",\"recursive\"");
				}
				JSONObject jobj = new JSONObject(finalJsonStr);
				JSONObject jruleObj = jobj.getJSONObject("rule");
				JSONArray jarr = jruleObj.getJSONArray("rule");
				for (int i = 0; i < jarr.length(); i++) {
					if (jarr.getJSONObject(i).has(ACTIVITIES)) {
						JSONArray activityArr = jarr.getJSONObject(i).getJSONArray(ACTIVITIES);
						for (int j = 0; j < activityArr.length(); j++) {
							if (activityArr.getJSONObject(j).has(LOOK_BACK)) {
								activityArr.getJSONObject(j).remove(LOOK_BACK);
								activityArr.getJSONObject(j).remove(LOOK_BACK_TIME);
							}
							if (activityArr.getJSONObject(j).has(TRIGGER_ON_CLOSE)) {
								activityArr.getJSONObject(j).remove(TRIGGER_ON_CLOSE);
							}
						}
					}
				}
				newJson = jobj.toString();
			} else {
				newJson = finalJsonStr;
			}
		} catch (Exception e) {
			// LOG.error("ERROR IN removeEmailScanKeyIfPreesent METHOD: "+e.getMessage());
			e.printStackTrace();
		}
		return newJson;
	}

	public static ParentRule createRule(Scanner scanner, String combinator, String sourcePort, List<Rule1> ruleAr) {
		// List<Rule> ruleAr=new ArrayList<Rule>();
		ParentRule parentRule = new ParentRule();
		parentRule.setCombinator(combinator);
		Rule1 rule = new Rule1();
		rule.setCombinator(combinator);
		ruleAr.add(rule);
		Activities activities = new Activities();
		activities.setEvent_module(scanner.getType());
		activities.setScanner_combinator(combinator);
		String customeApp = "";
		if (scanner.getCustomApp() == null) {
			customeApp = "";
		} else {
			customeApp = scanner.getCustomApp();
		}
		// for AppScanner
		if (scanner.getType().equalsIgnoreCase(APP_SCANNER)) {
			if (!customeApp.equals(""))
				activities.setValid_apps(new String[] { scanner.getCustomApp() }); // new String[]{validApps,
																					// scanner.getCustomApp()}
			else
				activities.setValid_apps(scanner.getValid_apps());

		}
		activities.setTrigger_on_close(scanner.getTriggerOnClose()); // present in AppScan

		Map<String, Object> map = new HashMap<String, Object>();
		List<Activities> activitiesArr = new ArrayList<Activities>();
		activitiesArr.add(activities);
		rule.setActivities(activitiesArr);
		parentRule.setRule(ruleAr);
		return parentRule;
	}

	public List<String> getChildFromParentNode(String rappidJson, String parentId) {

		JSONObject jObject = new JSONObject(rappidJson);
		JSONArray arr = jObject.getJSONArray(CELLS);
		List<String> childList = new ArrayList<>();
		for (int i = 0; i < arr.length(); i++) {
			if ((String.valueOf(arr.getJSONObject(i).get(TYPE))).equalsIgnoreCase(STANDARD_LINK)) {
				if (arr.getJSONObject(i).getJSONObject(SOURCE).get(ID).equals(parentId)) {
					childList.add((String) arr.getJSONObject(i).getJSONObject(TARGET).get(ID));
				}
			}
		}
		return childList;
	}

	public String getParentFromChildNode(String rappidJson, String childID) {

		JSONObject jObject = new JSONObject(rappidJson);
		JSONArray arr = jObject.getJSONArray(CELLS);
		String parent = "";
		for (int i = 0; i < arr.length(); i++) {
			if ((String.valueOf(arr.getJSONObject(i).get(TYPE))).equalsIgnoreCase(STANDARD_LINK)) {
				if (arr.getJSONObject(i).getJSONObject(TARGET).get(ID).equals(childID)) {
					parent = (String) arr.getJSONObject(i).getJSONObject(SOURCE).get(ID);
				}
			}
		}
		return parent;
	}

	public List<String> getAllNodes(String rappidJson) {

		JSONObject jObject = new JSONObject(rappidJson);
		JSONArray arr = jObject.getJSONArray(CELLS);
		List<String> nodeList = new ArrayList<>();
		for (int i = 0; i < arr.length(); i++) {
			if (!(String.valueOf(arr.getJSONObject(i).get(TYPE))).equalsIgnoreCase(STANDARD_LINK)) {
				nodeList.add((String) arr.getJSONObject(i).get(ID));
			}
		}
		return nodeList;
	}

	public Map<String, String> getChildMap(String rappidJson) {
		List<String> getAllNodes = getAllNodes(rappidJson);
		String parentId = "";
		Map<String, String> childParentMap = new LinkedHashMap<String, String>();
		for (int i = 0; i < getAllNodes.size(); i++) {
			String parent = getParentFromChildNode(rappidJson, getAllNodes.get(i));

			childParentMap.put(getAllNodes.get(i), parent);
		}
		return childParentMap;
	}

	public Map<String, List<String>> getParentMap(String rappidJson) {

		List<String> getAllNodes = getAllNodes(rappidJson);
		String parentId = getRootNode(rappidJson);
		Map<String, List<String>> parentChildMap = new LinkedHashMap<String, List<String>>();

		CopyOnWriteArrayList intermediateParentIds = new CopyOnWriteArrayList();
		intermediateParentIds.add(parentId);
		List<String> parentList = new ArrayList<>();
		parentList.add(parentId);
		parentChildMap.put("0", parentList);
		for (int i = 0; i < intermediateParentIds.size(); i++) {
			List<String> childList = getChildFromParentNode(rappidJson, (String) intermediateParentIds.get(i));
			intermediateParentIds.addAll(childList);
			parentChildMap.put(String.valueOf(intermediateParentIds.get(i)), childList);
		}
		return parentChildMap;
	}

	public String getRootNode(String rappidJson) {

		List<String> getAllNodes = getAllNodes(rappidJson);
		String parentId = "";
		for (int i = 0; i < getAllNodes.size(); i++) {
			String parent = getParentFromChildNode(rappidJson, getAllNodes.get(i));
			if (parent.isEmpty()) {
				parentId = getAllNodes.get(i);
			}
		}
		return parentId;
	}

	public String concatString(String oldString, String newString) {
		StringBuilder stringBuilder = new StringBuilder(oldString);
		if (!StringUtils.isEmpty(oldString)) {
			stringBuilder.append(",");
		}
		stringBuilder.append(newString);
		return stringBuilder.toString();
	}

	public static String createKeyMatch(String matchAttr, String strKeyStroke, boolean isIskeyEvent) {
		String str = "";
		try {
			String var = "{\"var\":\"" + matchAttr + "\"}";
			if (strKeyStroke.endsWith("]}")) {
				str = strKeyStroke.replace("]}", "," + var + "]}");
				if (str.contains(INPUT_BUFFER_KEYSTROKE)) {
					str = str.replace(INPUT_BUFFER_KEYSTROKE, "INPUT_BUFFER");
				} else if (str.contains(INPUT_BUFFER_KEYEVENT)) {
					str = str.replace(INPUT_BUFFER_KEYEVENT, "INPUT_BUFFER");
					if (str.contains(KEY_STROKE))
						str = str.replace(KEY_STROKE, "key_event");
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return str;
	}

	private List<String> getChildScanner(String parentId, Map<String, Map<String, String>> cellAttributeInfo,
			String rappidJson) {
		List<String> listOfChildId = getChildFromParentNode(rappidJson, parentId);
		List<String> listOfChildScanner = new ArrayList<String>();

		// if scanner is root parent and dont have child scanner
		// then set root scanner value
		if (!StringUtils.equalsIgnoreCase(cellAttributeInfo.get(parentId).get(TYPE), HTML_CONNECTOR_CONDITION)) {

			listOfChildScanner.add(parentId);
		}
		for (int i = 0; i < listOfChildId.size(); i++) {
			Map<String, String> map = cellAttributeInfo.get(listOfChildId.get(i));
			Iterator iterator = map.entrySet().iterator();
			while (iterator.hasNext()) {
				Map.Entry<String, String> entry = (Map.Entry<String, String>) iterator.next();
				if (StringUtils.equalsIgnoreCase(SCANNER, entry.getValue())) {
					listOfChildScanner.add(listOfChildId.get(i));
				}
			}
		}

		return listOfChildScanner;

	}

	public Map<String, Map<String, String>> getIdTypeAndVale(String rappidJson, boolean isManualValidation) {

		Map<String, Map<String, String>> map = new HashMap<String, Map<String, String>>();

		JSONObject jObject = new JSONObject(rappidJson);
		JSONArray arr = jObject.getJSONArray(CELLS);
		for (int i = 0; i < arr.length(); i++) {

			JSONObject jsonObject = arr.getJSONObject(i);
			
			if (!(String.valueOf(jsonObject.get(TYPE))).equalsIgnoreCase(STANDARD_LINK)) {
				Map<String, String> mapjsonfields = new HashMap<String, String>();

				mapjsonfields.put(ID, (String.valueOf(jsonObject.get(ID))));
				mapjsonfields.put(TYPE_NAME, (String.valueOf(jsonObject.get(TYPE_NAME))));
				mapjsonfields.put(TYPE, (String.valueOf(jsonObject.get(TYPE))));

				if ("Match".equalsIgnoreCase((String.valueOf(jsonObject.get(TYPE_NAME))))) {

					if (jsonObject.has(FIELDS)) {

						if (jsonObject.getJSONObject(FIELDS).has(MATCH_TYPE)) {

							mapjsonfields.put(MATCH_VALUE_S, getMatchValue(arr, i, isManualValidation));
						}

						if (jsonObject.getJSONObject(FIELDS).has(DEFAULT_VALUE_FOR_DYNAMIC)) {
							mapjsonfields.put(DEFAULT_VALUE_FOR_DYNAMIC, (String
									.valueOf(jsonObject.getJSONObject(FIELDS).getString(DEFAULT_VALUE_FOR_DYNAMIC))));
						}

						if (jsonObject.getJSONObject(FIELDS).has(DYNAMIC_FIELD)) {
							mapjsonfields.put(DYNAMIC_FIELD,
									(String.valueOf(jsonObject.getJSONObject(FIELDS).getString(DYNAMIC_FIELD))));
						}

						if (jsonObject.getJSONObject(FIELDS).has(MATCH_TYPE)) {
							mapjsonfields.put(MATCH_TYPE,
									(String.valueOf(jsonObject.getJSONObject(FIELDS).getString(MATCH_TYPE))));
						}

						if (jsonObject.getJSONObject(FIELDS).has(MATCH_ATTR)) {

							mapjsonfields.put(MATCH_ATTR,
									String.valueOf(jsonObject.getJSONObject(FIELDS).getString(MATCH_ATTR)));
							if (INPUT_BUFFER_KEYSTROKE
									.equalsIgnoreCase(jsonObject.getJSONObject(FIELDS).getString(MATCH_ATTR))) {
								mapjsonfields.put(ISKEYSTROKE, "true");
							} else {
								mapjsonfields.put(ISKEYSTROKE, FALSE_FCAPS);
							}
							if (INPUT_BUFFER_KEYEVENT
									.equalsIgnoreCase(jsonObject.getJSONObject(FIELDS).getString(MATCH_ATTR))) {
								mapjsonfields.put(ISKEYEVENT, "true");
							} else {
								mapjsonfields.put(ISKEYEVENT, FALSE_FCAPS);
							}
						}
						if (StringUtils.equalsIgnoreCase(STATICANDDYNAMIC,
								(String.valueOf(jsonObject.getJSONObject(FIELDS).getString(MATCH_TYPE))))
								|| StringUtils.equalsIgnoreCase(DYNAMICANDSTATIC,
										(String.valueOf(jsonObject.getJSONObject(FIELDS).getString(MATCH_TYPE))))
								|| StringUtils.equalsIgnoreCase(DYNAMIC,
										(String.valueOf(jsonObject.getJSONObject(FIELDS).getString(MATCH_TYPE))))) {

							mapjsonfields.put(CUSTOMIZE_OPERATOR, OperatorMappingEnum.getOperatorByDynamicValue(
									(String.valueOf(jsonObject.getJSONObject(FIELDS).get(OPERATOR))),
									(String.valueOf(jsonObject.getJSONObject(FIELDS).getString(DYNAMIC_FIELD)))));

						} else {

							mapjsonfields.put(CUSTOMIZE_OPERATOR, OperatorMappingEnum.getOperatorByDynamicValue(
									String.valueOf(jsonObject.getJSONObject(FIELDS).get(OPERATOR)), ""));

						}
					}
				} else if (HTML_CONNECTOR_CONDITION.equalsIgnoreCase((String.valueOf(jsonObject.get(TYPE))))) {
					if (jsonObject.has(FIELDS) && jsonObject.getJSONObject(FIELDS).has("mode")) {
						boolean andType = jsonObject.getJSONObject(FIELDS).getBoolean("mode");
						if (andType) {
							mapjsonfields.put(CONNECTOR_CONDITION_COMBINATOR, "or");

						} else {
							mapjsonfields.put(CONNECTOR_CONDITION_COMBINATOR, "and");
						}

					}

				} else if (SCANNER.equalsIgnoreCase((String.valueOf(jsonObject.get(TYPE_NAME))))) {

					mapjsonfields.put("event_module",
							getEventModuleConvertedValue((String.valueOf(jsonObject.get(TYPE)))));

					if (HTML_APP_SCAN.equalsIgnoreCase((String.valueOf(jsonObject.get(TYPE))))) {
						if (jsonObject.has(FIELDS) && jsonObject.getJSONObject(FIELDS).has("app")) {
							JSONArray apps ;
							if (jsonObject.getJSONObject(FIELDS).get("app") instanceof JSONArray) {
								apps = (JSONArray) jsonObject.getJSONObject(FIELDS).get("app");
							} else {
								apps = new JSONArray();
								apps.put(jsonObject.getJSONObject(FIELDS).get("app"));

							}

							if (jsonObject.has(FIELDS) && jsonObject.getJSONObject(FIELDS).has(CUSTOMAPP)) {
								apps.put(jsonObject.getJSONObject(FIELDS).getString(CUSTOMAPP));
							}
							StringBuilder validApps = new StringBuilder();
							for (int j = 0; j < apps.length(); j++) {

								String appName = String.valueOf(apps.get(j));
								if (StringUtils.equalsIgnoreCase(appName, "-1")) {
									continue;
								}
								validApps.append(appName).append(",");
							}
							mapjsonfields.put(VALID_APPS, validApps.toString());
						}

					} else if (HTML_FILE_SCAN.equalsIgnoreCase((String.valueOf(jsonObject.get(TYPE))))) {

						if (jsonObject.has(FIELDS)) {

							if (jsonObject.getJSONObject(FIELDS).has("app")) {
								mapjsonfields.put(VALID_APPS,
										(String.valueOf(jsonObject.getJSONObject(FIELDS).get("app"))));
							}

							if (jsonObject.getJSONObject(FIELDS).has(LOOK_FOR_SUBFOLDER)) {

								String lookForSubfolder = "";
								if (Boolean.parseBoolean(
										(String.valueOf(jsonObject.getJSONObject(FIELDS).get(LOOK_FOR_SUBFOLDER))))) {
									lookForSubfolder = "True";
								} else {
									lookForSubfolder = FALSE_FCAPS;
								}
								mapjsonfields.put(LOOK_FOR_SUBFOLDER, lookForSubfolder);
							} else {
								mapjsonfields.put(LOOK_FOR_SUBFOLDER, FALSE_FCAPS);
							}
							if (jsonObject.getJSONObject(FIELDS).has(CUSTOM_PATH)) {
								mapjsonfields.put(CUSTOM_PATH,
										(String.valueOf(jsonObject.getJSONObject(FIELDS).get(CUSTOM_PATH))));
							}
						}

					} else if (HTML_EMAIL_SCAN.equalsIgnoreCase((String.valueOf(jsonObject.get(TYPE))))) {

						if (jsonObject.has(FIELDS)) {

							if (jsonObject.getJSONObject(FIELDS).has(TIME_FIELD)) {
								mapjsonfields.put(LOOK_BACK_TIME,
										(String.valueOf(jsonObject.getJSONObject(FIELDS).get(TIME_FIELD))));
							}

							if (jsonObject.getJSONObject(FIELDS).has(LOOK_FOR_PAST_EMAIL)) {
								String lookForPastEmail = "";
								if (Boolean.parseBoolean(
										(String.valueOf(jsonObject.getJSONObject(FIELDS).get(LOOK_FOR_PAST_EMAIL))))) {
									lookForPastEmail = "True";
								} else {
									lookForPastEmail = FALSE_FCAPS;
								}
								mapjsonfields.put(LOOKBACK, lookForPastEmail);
							} else {
								mapjsonfields.put(LOOKBACK, FALSE_FCAPS);
							}

							mapjsonfields.put(VALID_APPS, "Outlook,Message (HTML)");

						}

					}

					if (jsonObject.has(FIELDS)) {

						if (jsonObject.getJSONObject(FIELDS).has(TRIGGERONCLOSE)) {

							String triggerOnClose = "";
							if (Boolean.parseBoolean(
									(String.valueOf(jsonObject.getJSONObject(FIELDS).get(TRIGGERONCLOSE))))) {
								triggerOnClose = "True";
							} else {
								triggerOnClose = FALSE_FCAPS;
							}
							mapjsonfields.put(TRIGGERONCLOSE, triggerOnClose);
						} else {
							mapjsonfields.put(TRIGGERONCLOSE, FALSE_FCAPS);
						}

					}

					String port = getPortValue(jsonObject.getString(ID), rappidJson);
					mapjsonfields.put(SCANNER_COMBINATOR, port);
				} else if (HTML_APP_CONDITION.equalsIgnoreCase((String.valueOf(jsonObject.get(TYPE))))) {
					if (jsonObject.has(FIELDS) && jsonObject.getJSONObject(FIELDS).has("mode")) {
						boolean conditionType = jsonObject.getJSONObject(FIELDS).getBoolean("mode");
						if (conditionType) {
							mapjsonfields.put(SCANNER_CONDITION_COMBINATOR, "or");
						} else {
							mapjsonfields.put(SCANNER_CONDITION_COMBINATOR, "and");
						}

					}

				} else if ("html.FileCondition".equalsIgnoreCase((String.valueOf(jsonObject.get(TYPE))))) {
					if (jsonObject.has(FIELDS) && jsonObject.getJSONObject(FIELDS).has("mode")) {
						boolean conditionType = jsonObject.getJSONObject(FIELDS).getBoolean("mode");
						if (conditionType) {
							mapjsonfields.put(SCANNER_CONDITION_COMBINATOR, "or");
						} else {
							mapjsonfields.put(SCANNER_CONDITION_COMBINATOR, "and");
						}

					}

				} else if ("html.EmailCondition".equalsIgnoreCase((String.valueOf(jsonObject.get(TYPE))))) {
					if (jsonObject.has(FIELDS) && jsonObject.getJSONObject(FIELDS).has("mode")) {
						boolean conditionType = jsonObject.getJSONObject(FIELDS).getBoolean("mode");
						if (conditionType) {
							mapjsonfields.put(SCANNER_CONDITION_COMBINATOR, "or");
						} else {
							mapjsonfields.put(SCANNER_CONDITION_COMBINATOR, "and");
						}

					}

				} else if (NOTIFICATION.equalsIgnoreCase((String.valueOf(jsonObject.get(TYPE_NAME))))) {
					if (jsonObject.has(FIELDS) && jsonObject.getJSONObject(FIELDS).has(ACTION_NAME0)) {
						mapjsonfields.put("action",
								(String.valueOf(jsonObject.getJSONObject(FIELDS).getString(ACTION_NAME0))));
					}
				}

				map.put((String) jsonObject.get(ID), mapjsonfields);

			}

		}
		return map;
	}

	private String getMatchValue(JSONArray arr, int i, boolean isManualValidation) {

		JSONObject jsonObject = arr.getJSONObject(i);
		String matchType = String.valueOf(jsonObject.getJSONObject(FIELDS).getString(MATCH_TYPE));

		if (isManualValidation) {

			String dynamicValue = StringUtils.EMPTY;
			if (jsonObject.getJSONObject(FIELDS).has(DYNAMIC_FIELD)) {

				dynamicValue = String.valueOf(jsonObject.getJSONObject(FIELDS).getString(DYNAMIC_FIELD));
			}
			if (StringUtils.equalsIgnoreCase(dynamicValue, NODES)
					|| StringUtils.equalsIgnoreCase(dynamicValue, MARKET)) {

				String dynamicMatchValue = "['MATCH_VALUE']";
				if (jsonObject.getJSONObject(FIELDS).has(DYNAMIC_FIELD)) {

					if (jsonObject.getJSONObject(FIELDS).has(DEFAULT_VALUE_FOR_DYNAMIC)) {

						dynamicMatchValue = dynamicMatchValue.replace("MATCH_VALUE",
								(String.valueOf(jsonObject.getJSONObject(FIELDS).getString(DEFAULT_VALUE_FOR_DYNAMIC))));
						if (dynamicMatchValue.indexOf("'s") != 1) {

							dynamicMatchValue = dynamicMatchValue.replace("'s", "\\\\'s");
						}
						return dynamicMatchValue;
					}
				}
			} else {

				if (jsonObject.getJSONObject(FIELDS).has(DYNAMIC_FIELD)
						&& StringUtils.equalsIgnoreCase(DYNAMIC, matchType)) {
					return (String.valueOf(jsonObject.getJSONObject(FIELDS).getString(DEFAULT_VALUE_FOR_DYNAMIC)));
				}
				if (jsonObject.getJSONObject(FIELDS).has(STATIC_DATA)
						&& StringUtils.equalsIgnoreCase("static", matchType)) {

					String matchValue = String.valueOf(jsonObject.getJSONObject(FIELDS).getString(STATIC_DATA));

					if (jsonObject.getJSONObject(FIELDS).has(MATCH_ATTR)) {

						String matchAttr = String.valueOf(jsonObject.getJSONObject(FIELDS).getString(MATCH_ATTR));

						if (StringUtils.equalsIgnoreCase("FOLDER_OPERATION", matchAttr)
								|| StringUtils.equalsIgnoreCase("FILE_OPERATION", matchAttr)) {

							if (StringUtils.equalsIgnoreCase("createAndModify", matchValue)
									|| StringUtils.equalsIgnoreCase(MODIFY, matchValue)) {

								matchValue = MODIFY;
							} else if (StringUtils.equalsIgnoreCase("delete", matchValue)) {
								matchValue = "Delete";
							}
						}

					}
					return matchValue;
				}
				if (jsonObject.getJSONObject(FIELDS).has(STATIC_DATA)
						&& jsonObject.getJSONObject(FIELDS).has(DYNAMIC_FIELD)
						&& StringUtils.equalsIgnoreCase(STATICANDDYNAMIC, matchType)) {

					return String.valueOf(jsonObject.getJSONObject(FIELDS).getString(STATIC_DATA)).concat(" ").concat(
							(String.valueOf(jsonObject.getJSONObject(FIELDS).getString(DEFAULT_VALUE_FOR_DYNAMIC))));
				}
				if (jsonObject.getJSONObject(FIELDS).has(DYNAMIC_FIELD)
						&& jsonObject.getJSONObject(FIELDS).has(STATIC_DATA)
						&& StringUtils.equalsIgnoreCase(DYNAMICANDSTATIC, matchType)) {
					return String.valueOf(jsonObject.getJSONObject(FIELDS).getString(DEFAULT_VALUE_FOR_DYNAMIC))
							.concat(" ")
							.concat((String.valueOf(jsonObject.getJSONObject(FIELDS).getString(STATIC_DATA))));
				}
			}

		} else {

			String dynamicValue = StringUtils.EMPTY;
			if (jsonObject.getJSONObject(FIELDS).has(DYNAMIC_FIELD)) {

				dynamicValue = String.valueOf(jsonObject.getJSONObject(FIELDS).getString(DYNAMIC_FIELD));
			}
			if (StringUtils.equalsIgnoreCase(dynamicValue, NODES)
					|| StringUtils.equalsIgnoreCase(dynamicValue, MARKET)) {

				if (StringUtils.equalsIgnoreCase(DYNAMIC, matchType)) {
					return String.valueOf(jsonObject.getJSONObject(FIELDS).getString(DYNAMIC_FIELD));
				}

				if (jsonObject.getJSONObject(FIELDS).has(STATIC_DATA)
						&& jsonObject.getJSONObject(FIELDS).has(DYNAMIC_FIELD)
						&& StringUtils.equalsIgnoreCase(STATICANDDYNAMIC, matchType)) {
					return String.valueOf(jsonObject.getJSONObject(FIELDS).getString(DYNAMIC_FIELD));
				}
				if (jsonObject.getJSONObject(FIELDS).has(DYNAMIC_FIELD)
						&& jsonObject.getJSONObject(FIELDS).has(STATIC_DATA)
						&& StringUtils.equalsIgnoreCase(DYNAMICANDSTATIC, matchType)) {
					return String.valueOf(jsonObject.getJSONObject(FIELDS).getString(DYNAMIC_FIELD));
				}

			} else {

				if (jsonObject.getJSONObject(FIELDS).has(DYNAMIC_FIELD)
						&& StringUtils.equalsIgnoreCase(DYNAMIC, matchType)) {
					return String.valueOf(jsonObject.getJSONObject(FIELDS).getString(DYNAMIC_FIELD));
				}
				if (jsonObject.getJSONObject(FIELDS).has(STATIC_DATA)
						&& StringUtils.equalsIgnoreCase("static", matchType)) {

					String matchValue = String.valueOf(jsonObject.getJSONObject(FIELDS).getString(STATIC_DATA));

					if (jsonObject.getJSONObject(FIELDS).has(MATCH_ATTR)) {

						String matchAttr = String.valueOf(jsonObject.getJSONObject(FIELDS).getString(MATCH_ATTR));

						if (StringUtils.equalsIgnoreCase("FOLDER_OPERATION", matchAttr)
								|| StringUtils.equalsIgnoreCase("FILE_OPERATION", matchAttr)) {

							if (StringUtils.equalsIgnoreCase("createAndModify", matchValue)
									|| StringUtils.equalsIgnoreCase(MODIFY, matchValue)) {

								matchValue = MODIFY;
							} else if (StringUtils.equalsIgnoreCase("delete", matchValue)) {
								matchValue = "Delete";
							}
						}

					}
					return matchValue;
				}

				if (jsonObject.getJSONObject(FIELDS).has(STATIC_DATA)
						&& jsonObject.getJSONObject(FIELDS).has(DYNAMIC_FIELD)
						&& StringUtils.equalsIgnoreCase(STATICANDDYNAMIC, matchType)) {
					return String.valueOf(jsonObject.getJSONObject(FIELDS).getString(STATIC_DATA)).concat(" ")
							.concat((String.valueOf(jsonObject.getJSONObject(FIELDS).getString(DYNAMIC_FIELD))));
				}
				if (jsonObject.getJSONObject(FIELDS).has(DYNAMIC_FIELD)
						&& jsonObject.getJSONObject(FIELDS).has(STATIC_DATA)
						&& StringUtils.equalsIgnoreCase(DYNAMICANDSTATIC, matchType)) {
					return String.valueOf(jsonObject.getJSONObject(FIELDS).getString(DYNAMIC_FIELD)).concat(" ")
							.concat((String.valueOf(jsonObject.getJSONObject(FIELDS).getString(STATIC_DATA))));
				}
			}
		}
		////////////
		return StringUtils.EMPTY;
	}


	public String getPortValue(String sourceId, String rappidJson) {
		String portValue = "and";
		JSONObject jObject = new JSONObject(rappidJson);
		JSONArray arr = jObject.getJSONArray(CELLS);
		for (int i = 0; i < arr.length(); i++) {
			if (arr.getJSONObject(i).has(TYPE)
					&& (String.valueOf(arr.getJSONObject(i).get(TYPE))).equalsIgnoreCase(STANDARD_LINK)
					&& arr.getJSONObject(i).has(SOURCE) && arr.getJSONObject(i).getJSONObject(SOURCE).has(PORT)
					&& StringUtils.equalsIgnoreCase(sourceId,
							(String) arr.getJSONObject(i).getJSONObject(SOURCE).get(ID))
					&& (((String) arr.getJSONObject(i).getJSONObject(SOURCE).get(PORT)).contains("and")
							|| ((String) arr.getJSONObject(i).getJSONObject(SOURCE).get(PORT)).contains("or"))) {
				portValue = ((String) arr.getJSONObject(i).getJSONObject(SOURCE).get(PORT));
				break;

			}
		}
		return portValue;
	}

	private String getEventModuleConvertedValue(String eventModuleValue) {
		String eventModuleConvertedValue = "";
		switch (eventModuleValue) {
		case HTML_APP_SCAN:
			eventModuleConvertedValue = APP_SCANNER;
			break;

		case HTML_FILE_SCAN:
			eventModuleConvertedValue = FS_SCANNER;
			break;

		case HTML_EMAIL_SCAN:
			eventModuleConvertedValue = EMAIL_SCANNER;
			break;

		default:
			eventModuleConvertedValue = "";
			break;
		}
		return eventModuleConvertedValue;
	}

	// This method is getting all match childs till leaf node
	// also excluding chids with given key 
	private List<String> getChildAllMatchExcludeKey(String parentId, String rappidJson,
			Map<String, Map<String, String>> cellAttributeInfo, String[] excludedKey) {

		List<String> childList = getChildListFromNode(rappidJson, parentId);
		List<String> listOfChildMatch = new ArrayList<String>();
		for (int i = 0; i < childList.size(); i++) {
			Map<String, String> map = cellAttributeInfo.get(childList.get(i));
			String typeName = map.get(TYPE_NAME);
			String matchattr = map.get(MATCH_ATTR);
			if (StringUtils.equalsIgnoreCase(typeName, MATCH) && excludedKey.length > 0
					&& !java.util.Arrays.asList(excludedKey).contains(matchattr)) {
				listOfChildMatch.add(childList.get(i));
			}
			if (StringUtils.equalsIgnoreCase(typeName, MATCH) && excludedKey.length == 0) {
				listOfChildMatch.add(childList.get(i));

			}
		}
		return listOfChildMatch;

	}
	
	private List<String> getAllChildScanner(String parentId, String rappidJson,
			Map<String, Map<String, String>> cellAttributeInfo) {

		List<String> childList = getChildListFromNode(rappidJson, parentId);
		List<String> listOfScanner = new ArrayList<String>();

		for (int i = 0; i < childList.size(); i++) {
			Map<String, String> map = cellAttributeInfo.get(childList.get(i));
			String typeName = map.get(TYPE_NAME);
			if (StringUtils.equalsIgnoreCase(typeName, SCANNER)) {
				listOfScanner.add(childList.get(i));
			}
		}
		return listOfScanner;

	}
	
	public String getManualValidateJsonFromParsedRuleJson(String parsedRuleJson, String signum) throws JsonProcessingException {
		JSONObject jObject = new JSONObject(parsedRuleJson);
		long uniqueId = getUniqueId();
		jObject.put("work_order_id", uniqueId);
		jObject.put("task_id", uniqueId);
		jObject.put("step_id", uniqueId);
		jObject.put("signum_id", signum);
		jObject.put("override_action", "");
		jObject.put("sub_activity_flowchart_id", uniqueId);
		return jObject.toString();
	}

	private long getUniqueId() {
		AtomicLong LAST_TIME_MS = new AtomicLong();
		long now = System.currentTimeMillis();
		while (true) {
			long lastTime = LAST_TIME_MS.get();
			if (lastTime >= now)
				now = lastTime + 1;
			if (LAST_TIME_MS.compareAndSet(lastTime, now))
				return now;
		}
	}
}
