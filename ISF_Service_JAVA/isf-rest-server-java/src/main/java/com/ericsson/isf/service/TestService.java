package com.ericsson.isf.service;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Random;
import java.util.Set;
import java.util.logging.Level;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.FilenameUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.ericsson.isf.config.ApplicationConfigurations;
import com.ericsson.isf.dao.ActivityMasterDAO;
import com.ericsson.isf.dao.FlowChartDAO;
import com.ericsson.isf.dao.TestDAO;
import com.ericsson.isf.dao.ToolsMasterDAO;
import com.ericsson.isf.dao.WOExecutionDAO;
import com.ericsson.isf.exception.ApplicationException;
import com.ericsson.isf.exception.ApplicationExceptionHandler;
import com.ericsson.isf.flowchart.util.FlowChartStepTemplateMigration;
import com.ericsson.isf.model.FlowChartDefModel;
import com.ericsson.isf.model.FlowChartDependencyModel;
import com.ericsson.isf.model.FlowChartStepInformationModel;
import com.ericsson.isf.model.FlowChartStepModel;
import com.ericsson.isf.model.TestProject;
import com.ericsson.isf.util.AppUtil;
import com.ericsson.isf.util.ConfigurationFilesConstants;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.internal.LinkedTreeMap;

import antlr.StringUtils;


@Service
public class TestService {

	
	private static final Logger LOG = LoggerFactory
			.getLogger(ApplicationExceptionHandler.class);
	@Autowired
	private TestDAO testDAO;

	@Autowired
	private FlowChartService flowChartService;

	@Autowired
	private FlowChartDAO flowChartDao;

	@Autowired
	private WOExecutionDAO wOExecutionDAO;

	@Autowired
	private ToolsMasterDAO toolsMasterDAO;
    
    @Autowired
    private ActivityMasterDAO activityMasterDAO;
    
    @Autowired
    private ApplicationConfigurations configurations;
    
    @Autowired
    private AppService appService;
    
	public List<TestProject> getProjects(String testParameter1,
			String testParameter2) {

		// return this.testDAO.getProjects();
		return null;
	}

	@Transactional("transactionManager")
	/* Closes connection automatically */
	public void updateProject(TestProject testProject) {

		throw new ApplicationException(500, "Error while updating Project");
		/* To throw an exception to UI */
	}

	public void updateFlowChartJSON(int projectID) {
		List<Integer> lstOFProjectID = testDAO.getProjectID();
		for (Integer p : lstOFProjectID) {
			List<Integer> lstSubactivity = testDAO.getSubActivityList(p);
			for (Integer sid : lstSubactivity) {
				List<FlowChartDefModel> flowJSON = testDAO
						.getFlowChartJSONForSID(p, sid);
				for (FlowChartDefModel json : flowJSON) {
					// prepareJSON(json, projectID, sid);
				}
			}
		}
	}

	public void updateFlowChartJSON() {
		List<Integer> lstOFProjectID = testDAO.getProjectID();
		for (Integer p : lstOFProjectID) {
			List<Integer> lstSubactivity = testDAO.getSubActivityList(p);
			for (Integer sid : lstSubactivity) {
				List<FlowChartDefModel> flowJSON = testDAO
						.getFlowChartJSONForSID(p, sid);
				for (FlowChartDefModel json : flowJSON) {
					// Boolean exists =
					// testDAO.checkIFDataExists(p,sid,json.getSubActivityFlowChartDefID(),json.getVersionNumber());
					// if(!exists){
					// prepareJSON(json, p, sid);
					// }
				}
			}
		}
	}

	public void updateFlowChartJSONTools() {
		FileWriter JSONupdate = null;
		try {
			JSONupdate = new FileWriter("c:/ProdQuery/UpdateJSONScript.txt");
			List<Integer> flowChartDefID = testDAO
					.getFlowChartDefIDToUpdateJSON();
			for (int subActFCDefID : flowChartDefID) {
				FlowChartDefModel flowModel = testDAO
						.getFlowChartJSONForStep(subActFCDefID);
				prepareJSON(flowModel, flowModel.getProjectID(),
						flowModel.getSubActivityID(), JSONupdate);

			}
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			if (JSONupdate != null) {
				try {
					JSONupdate.close();
				} catch (IOException ex) {
					java.util.logging.Logger.getLogger(
							TestService.class.getName()).log(Level.SEVERE,
							null, ex);
				}
			}
		}
	}

	@Transactional("transactionManager")
	private void prepareJSON(FlowChartDefModel flowChartDefModel,
			int projectID, int subActivityID, FileWriter JSONupdate) {
		try {
			if (!flowChartDefModel.getFlowChartJSON().equalsIgnoreCase(
					"{\"cells\":[]}")) {
				JSONObject obj = new JSONObject(
						flowChartDefModel.getFlowChartJSON());
				JSONArray cells = (JSONArray) obj.get("cells");
				for (int i = 0; i < cells.length(); i++) {
					JSONObject types = (JSONObject) cells.get(i);
					if ("basic.Rect".equalsIgnoreCase((String) types
							.get("type"))
							|| "erd.WeakEntity".equalsIgnoreCase((String) types
									.get("type"))
							|| "app.ericssonStep"
									.equalsIgnoreCase((String) types
											.get("type"))
							|| "app.ericssonWeakEntity"
									.equalsIgnoreCase((String) types
											.get("type"))) {
						List<FlowChartStepInformationModel> stepData = testDAO
								.getFlowChartStepDetails(flowChartDefModel
										.getSubActivityFlowChartDefID(),
										flowChartDefModel.getVersionNumber());
						if (!stepData.isEmpty()) {
							for (FlowChartStepInformationModel stepModel : stepData) {
								String stepID = (String) types.get("id");
								if (stepID.equals(stepModel.getStepID())) {
									String newToolName = "";
									if (stepModel.getToolID() != 2) {
										String toolName = testDAO
												.getToolName(stepModel
														.getToolID());
										String[] tool = flowChartService
												.splitStepNameByWord(toolName);
										for (String tool1 : tool) {
											newToolName += tool1;
										}
									} else {
										newToolName = "No Tool";
									}
									JSONObject attrs = (JSONObject) types
											.get("attrs");
									if (attrs.has("task")) {
										JSONObject task = (JSONObject) attrs
												.get("task");
										task.remove("toolID");
										task.remove("tool");
										task.remove("toolName");
										task.remove("avgEstdEffort");
										task.put("toolID",
												stepModel.getToolID());
										task.put("toolName", newToolName);
										task.put("avgEstdEffort",
												stepModel.getAvgEstdEffort());
									}
									JSONObject text = (JSONObject) attrs
											.get("text");
									String textName = text.getString("text");
									text.remove(textName);
									try {
										if (textName
												.contains("\n\nExecutionType")) {
											textName = textName
													.substring(
															0,
															textName.indexOf("ExecutionType:"));
											textName = textName + "ToolName:"
													+ newToolName;
										} else {
											textName = textName
													.substring(
															0,
															textName.indexOf("ToolName:"));
											textName = textName + "ToolName:"
													+ newToolName;
										}
									} catch (Exception ex) {
										ex.printStackTrace();
									}
									text.remove("text");
									text.put("text", textName);
								}
							}
						}
					}
				}
				flowChartDefModel.setFlowChartJSON(obj.toString());
				String updateQuery = "update transactionalData.TBL_SUBACTIVITY_FLOWCHART_DEF SET FlowChartJSON= "
						+ "'"
						+ flowChartDefModel.getFlowChartJSON()
						+ "'"
						+ " where SubActivityFlowChartDefID = "
						+ flowChartDefModel.getSubActivityFlowChartDefID()
						+ " and ProjectID= "
						+ "'"
						+ projectID
						+ "'"
						+ " and SubActivityID= " + subActivityID;
				JSONupdate.write(updateQuery + "\n");
				LOG.info("JSON Update Success :"
						+ flowChartDefModel.getSubActivityFlowChartDefID());
				//
				// testDAO.updateFlowChartJSON(projectID, subActivityID,
				// flowChartDefModel.getSubActivityFlowChartDefID(),LastModifiedDate
				// = GetDATE()
				// flowChartDefModel.getVersionNumber(),
				// flowChartDefModel.getFlowChartJSON());

				// testDAO.saveUpdateStatus(projectID, subActivityID,
				// flowChartDefModel.getSubActivityFlowChartDefID(),
				// flowChartDefModel.getVersionNumber(), "Success");
			}
		} catch (Exception ex) {
			LOG.info("JSON Update Failed :"
					+ flowChartDefModel.getSubActivityFlowChartDefID());
			ex.printStackTrace();
			// testDAO.saveUpdateStatus(projectID, subActivityID,
			// flowChartDefModel.getSubActivityFlowChartDefID(),
			// flowChartDefModel.getVersionNumber(), "Failed");
		}
	}

	// public void testJSON(String JSON) {
	// try {
	// JSONObject obj = new JSONObject(JSON);
	// JSONArray cells = (JSONArray) obj.get("cells");
	// for (int i = 0; i < cells.length(); i++) {
	// JSONObject types = (JSONObject) cells.get(i);
	// if ("basic.Rect".equalsIgnoreCase((String) types.get("type")) ||
	// "erd.WeakEntity".equalsIgnoreCase((String) types.get("type"))
	// || "app.ericssonStep".equalsIgnoreCase((String) types.get("type")) ||
	// "app.ericssonWeakEntity".equalsIgnoreCase((String) types.get("type"))) {
	// List<FlowChartStepInformationModel> stepData =
	// testDAO.getFlowChartStepDetails(10973, 11);
	// if (!stepData.isEmpty()) {
	// for (FlowChartStepInformationModel stepModel : stepData) {
	// String stepDetails = (String) types.get("id");
	// Integer stepID = Integer.parseInt(stepDetails);
	// if (stepID == stepModel.getStepID()) {
	// if (stepModel.getToolID() == 0) {
	// stepModel.setToolID(2);
	// }
	// String toolName = testDAO.getToolName(stepModel.getToolID());
	// if (toolName != null) {
	// String[] tool = flowChartService.splitStepNameByWord(toolName);
	// String newToolName = "";
	// for (String tool1 : tool) {
	// newToolName += tool1;
	// }
	// JSONObject attrs = (JSONObject) types.get("attrs");
	// if (attrs.has("task")) {
	// JSONObject task = (JSONObject) attrs.get("task");
	// task.remove("tool");
	// task.remove("toolName");
	// task.remove("avgEstdEffort");
	// task.put("tool", stepModel.getToolID());
	// task.put("toolName", toolName);
	// task.put("avgEstdEffort", stepModel.getAvgEstdEffort());
	// }
	// JSONObject text = (JSONObject) attrs.get("text");
	// String textName = text.getString("text");
	// text.remove(textName);
	// String newString = textName.replaceAll("\n\nExecutionType",
	// "\nToolName:");
	// newString = newString.replaceAll(":Automatic", newToolName);
	// newString = newString.replaceAll(":Automated", newToolName);
	// newString = newString.replaceAll(":Manual", newToolName);
	// int lastIndex = newString.lastIndexOf(":");
	// newString = newString.substring(0, lastIndex);
	// newString = newString + ":" + stepModel.getAvgEstdEffort();
	// text.put("text", newString);
	// }
	// }
	// }
	// }
	// }
	// }
	//
	// } catch (Exception ex) {
	//
	// ex.printStackTrace();
	//
	// }
	//
	// }

	public void populateWFLinkDetails() {
		List<Integer> flowChartDefID = testDAO.getFlowChartDefID();
		for (int defID : flowChartDefID) {
			String json = testDAO.getFlowChartJSON(defID);
			parseJsonAndUpdateStepsForWFDefinition(json, defID);
		}
	}

	private void parseJsonAndUpdateStepsForWFDefinition(String wFJson,
			int subActFCDefID) {
		try {
			JSONObject jObject = new JSONObject(wFJson);
			JSONArray arr = jObject.getJSONArray("cells");
			for (int i = 0; i < arr.length(); i++) {
				String sourceID = null;
				String targetID = null;
				JSONObject types = (JSONObject) arr.get(i);
				if (arr.getJSONObject(i).has("router")
						&& "app.Link".equalsIgnoreCase((String) types
								.get("type"))) {
					try {
						sourceID = arr.getJSONObject(i).getJSONObject("source")
								.getString("id");
						targetID = arr.getJSONObject(i).getJSONObject("target")
								.getString("id");
						this.flowChartDao
								.insertFlowchartStepLinkDetails(subActFCDefID,
										sourceID.trim(), targetID.trim());

					} catch (Exception ex) {
						ex.printStackTrace();
					}

				}
			}
		} catch (Exception ex) {
			ex.printStackTrace();
			throw new ApplicationException(500,
					"Error while saving the workFlow:" + ex.getMessage());
		}
	}

	// @Transactional("transactionManager")
	public void populateWFStepDetails() {
		FileWriter update = null;
		FileWriter insert = null;
		try {
			update = new FileWriter("c:/ProdQuery/UpdateScript.txt");
			insert = new FileWriter("c:/ProdQuery/InsertScript.txt");
			List<Integer> flowChartDefID = testDAO.getFlowChartDefIDForStep();
			for (int subActFCDefID : flowChartDefID) {
				FlowChartDefModel flowModel = testDAO
						.getFlowChartJSONForStep(subActFCDefID);
				updateDecisionDetails(flowModel.getFlowChartJSON(),
						subActFCDefID, flowModel, update, insert);
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			if (update != null) {
				try {
					update.close();
				} catch (IOException ex) {
					java.util.logging.Logger.getLogger(
							TestService.class.getName()).log(Level.SEVERE,
							null, ex);
				}
			}
			if (insert != null) {
				try {
					insert.close();
				} catch (IOException ex) {
					java.util.logging.Logger.getLogger(
							TestService.class.getName()).log(Level.SEVERE,
							null, ex);
				}
			}
		}
	}

	// private void parseJSON(String wFJson, int subActFCDefID,
	// FlowChartDefModel flowModel) {
	// try {
	// JSONObject jObject = new JSONObject(wFJson);
	// JSONArray arr = jObject.getJSONArray("cells");
	// for (int i = 0; i < arr.length(); i++) {
	// String stepId = null;
	// String stepName = null;
	// String taskId = null;
	// String taskName = null;
	// String executionType = null;
	// String avgEstdEffort = null;
	// String toolId = null;
	// String reason = null;
	// String sourceID = null;
	// String targetID = null;
	// if (arr.getJSONObject(i).has("id")) {
	// stepId = arr.getJSONObject(i).getString("id");
	// }
	// if (arr.getJSONObject(i).has("attrs")) {
	// if (arr.getJSONObject(i).getJSONObject("attrs").has("text")) {
	// String step =
	// arr.getJSONObject(i).getJSONObject("attrs").getJSONObject("text").getString("text");
	// String part[] = step.split("\n\n");
	// stepName = part[0];
	// }
	// if (arr.getJSONObject(i).getJSONObject("attrs").has("task")) {
	// taskId =
	// arr.getJSONObject(i).getJSONObject("attrs").getJSONObject("task").getString("taskID");
	// taskName =
	// arr.getJSONObject(i).getJSONObject("attrs").getJSONObject("task").getString("taskName");
	// executionType =
	// arr.getJSONObject(i).getJSONObject("attrs").getJSONObject("task").getString("executionType");
	// avgEstdEffort =
	// String.valueOf(arr.getJSONObject(i).getJSONObject("attrs").getJSONObject("task").get("avgEstdEffort"));
	// if
	// (arr.getJSONObject(i).getJSONObject("attrs").getJSONObject("task").getString("toolID")
	// != null) {
	// toolId =
	// String.valueOf(arr.getJSONObject(i).getJSONObject("attrs").getJSONObject("task").get("toolID"));
	// }
	// reason =
	// arr.getJSONObject(i).getJSONObject("attrs").getJSONObject("task").getString("reason");
	// if (reason.equalsIgnoreCase("%REASON%")) {
	// reason = "NULL";
	// }
	// String masterTask = flowChartDao.getMasterTask(Integer.parseInt(taskId),
	// flowModel.getSubActivityID());
	// try {
	// if (toolId == null) {
	// int taskID = Integer.parseInt(taskId);
	// int tool = toolsMasterDAO.getToolsDetails(taskID,
	// flowModel.getSubActivityID());
	// if (tool != 0) {
	// if (toolId != null) {
	// toolId = String.valueOf(tool);
	// }
	// }
	// }
	// } catch (Exception e) {
	// LOG.info("Error while saving the workFlow:" + e.getMessage());
	// e.printStackTrace();
	// }
	// this.flowChartDao.updatePrjectConfigTaskDetailsVersion(flowModel.getProjectID(),
	// flowModel.getSubActivityID(), Integer.parseInt(taskId), executionType,
	// flowModel.getVersionNumber());
	// if (subActFCDefID != 0) {
	// boolean checkIFDataExists =
	// this.flowChartDao.checkIFStepDataExists(subActFCDefID,
	// Integer.parseInt(stepId), stepName.trim(), Integer.parseInt(taskId),
	// flowModel.getVersionNumber());
	// if (checkIFDataExists) {
	// // this.flowChartDao.updateFlowChartStepDetailsValue(subActFCDefID,
	// stepId.trim(), stepName.trim(), taskId.trim(), taskName.trim(),
	// executionType.trim(), avgEstdEffort.trim(), toolId.trim(), reason.trim(),
	// flowModel.getVersionNumber(), masterTask);
	// } else {
	// this.flowChartDao.deleteFlowchartStepDetails(subActFCDefID,
	// Integer.parseInt(stepId.trim()), Integer.parseInt(taskId),
	// flowModel.getVersionNumber());
	// // this.wOExecutionDAO.insertInFlowChartStepDetails(subActFCDefID,
	// stepId.trim(), stepName.trim(), taskId.trim(), taskName.trim(),
	// executionType.trim(), avgEstdEffort.trim(), toolId.trim(), reason.trim(),
	// flowModel.getVersionNumber(), masterTask);
	// }
	// }
	// }
	// }
	// if (arr.getJSONObject(i).has("router")) {
	// try {
	// sourceID = arr.getJSONObject(i).getJSONObject("source").getString("id");
	// targetID = arr.getJSONObject(i).getJSONObject("target").getString("id");
	// this.flowChartDao.insertFlowchartStepLinkDetails(subActFCDefID,
	// sourceID.trim(), targetID.trim());
	// } catch (Exception ex) {
	// throw new ApplicationException(500,
	// "This is not a valid Workflow,Steps are not connected");
	// }
	// }
	// }
	//
	// } catch (Exception ex) {
	// LOG.info("Error while saving the workFlow:" + ex.getMessage());
	// ex.printStackTrace();
	// throw new ApplicationException(500, "Error while saving the workFlow:" +
	// ex.getMessage());
	// }
	//
	// }

	private void updateDecisionDetails(String flowChartJSON, int subActFCDefID,
			FlowChartDefModel flowModel, FileWriter update, FileWriter insert) {

		try {

			JSONObject jObject = new JSONObject(flowChartJSON);
			JSONArray arr = jObject.getJSONArray("cells");

			for (int i = 0; i < arr.length(); i++) {
				String stepId = null;
				String stepName = null;
				String taskId = null;
				String taskName = null;
				String executionType = null;
				String avgEstdEffort = null;
				String toolId = null;
				String reason = null;
				String sourceID = null;
				String targetID = null;
				String masterTask = null;
				String rpaID = "0";
				JSONObject types = (JSONObject) arr.get(i);
				if (arr.getJSONObject(i).has("id")) {
					stepId = arr.getJSONObject(i).getString("id");
				}
				if (arr.getJSONObject(i).has("attrs")) {
					if (arr.getJSONObject(i).getJSONObject("attrs").has("text")) {
						String step = arr.getJSONObject(i)
								.getJSONObject("attrs").getJSONObject("text")
								.getString("text");
						String part[] = step.split("\n\n");
						stepName = part[0];
					}
					if (arr.getJSONObject(i).getJSONObject("attrs").has("task")) {
						taskId = arr.getJSONObject(i).getJSONObject("attrs")
								.getJSONObject("task").getString("taskID");
						taskName = arr.getJSONObject(i).getJSONObject("attrs")
								.getJSONObject("task").getString("taskName");
						executionType = arr.getJSONObject(i)
								.getJSONObject("attrs").getJSONObject("task")
								.getString("executionType");
						if (String.valueOf(arr.getJSONObject(i)
								.getJSONObject("attrs").getJSONObject("task")
								.get("toolID")) == null
								|| String.valueOf(
										arr.getJSONObject(i)
												.getJSONObject("attrs")
												.getJSONObject("task")
												.get("toolID")).equals("")
								|| String.valueOf(
										arr.getJSONObject(i)
												.getJSONObject("attrs")
												.getJSONObject("task")
												.get("toolID")).equals(
										"%TOOL_ID%")) {
							toolId = "2";
						} else {
							toolId = String.valueOf(arr.getJSONObject(i)
									.getJSONObject("attrs")
									.getJSONObject("task").get("toolID"));
						}
						reason = arr.getJSONObject(i).getJSONObject("attrs")
								.getJSONObject("task").getString("reason");
						if (reason.equalsIgnoreCase("%REASON%")) {
							reason = "NULL";
						}
						if (executionType.equalsIgnoreCase("Automatic")
								|| executionType.equalsIgnoreCase("Automated")) {
							if (String.valueOf(arr.getJSONObject(i)
									.getJSONObject("attrs")
									.getJSONObject("tool").get("RPAID")) == null
									|| String.valueOf(
											arr.getJSONObject(i)
													.getJSONObject("attrs")
													.getJSONObject("tool")
													.get("RPAID")).equals("")
									|| String.valueOf(
											arr.getJSONObject(i)
													.getJSONObject("attrs")
													.getJSONObject("tool")
													.get("RPAID")).equals(
											"%RPATOOL_ID%")) {
								rpaID = "0";
							} else {
								rpaID = String.valueOf(arr.getJSONObject(i)
										.getJSONObject("attrs")
										.getJSONObject("tool").get("RPAID"));
							}
						} else {
							rpaID = "0";
						}
						populateFlowChartStepDetails(subActFCDefID,
								stepId.trim(), stepName.trim(), taskId.trim(),
								taskName.trim(), executionType.trim(),
								toolId.trim(), reason.trim(),
								flowModel.getVersionNumber(), masterTask,
								(String) types.get("type"), rpaID, update,
								insert);
					} else {
						if ("erd.Relationship".equalsIgnoreCase((String) types
								.get("type"))
								|| "basic.Circle"
										.equalsIgnoreCase((String) types
												.get("type"))) {
							populateFlowChartStepDetails(subActFCDefID,
									stepId.trim(), stepName.trim(), "0",
									"NULL", "Manual", "0", "NULL",
									flowModel.getVersionNumber(), "NULL",
									(String) types.get("type"), rpaID, update,
									insert);
						}
					}
				}
			}
			LOG.info("Step Details Success :" + subActFCDefID);
			// testDAO.saveUpdateStatus(flowModel.getProjectID(),
			// flowModel.getSubActivityID(), subActFCDefID,
			// flowModel.getVersionNumber(), "Success");
		} catch (Exception ex) {
			LOG.info("Step Details Failed :" + subActFCDefID);
			// testDAO.saveUpdateStatus(flowModel.getProjectID(),
			// flowModel.getSubActivityID(), subActFCDefID,
			// flowModel.getVersionNumber(), "Failed");
			ex.printStackTrace();
		}
	}

	private void populateFlowChartStepDetails(int subActFCDefID, String stepID,
			String stepName, String taskID, String taskName,
			String executionType, String toolID, String reason,
			int versionNumber, String masterTask, String stepType,
			String rpaID, FileWriter update, FileWriter insert) {

		try {
			boolean checkIFDataExists = this.flowChartDao
					.checkIFStepDataExists(subActFCDefID, stepID,
							stepName.trim(), Integer.parseInt(taskID),
							versionNumber);
			if (checkIFDataExists) {
				String updateQuery = "";
				if ("0".equals(taskID)) {
					updateQuery = "update transactionalData.TBL_FLOWCHART_STEP_DETAILS SET StepType= "
							+ "'"
							+ stepType
							+ "'"
							+ ","
							+ "RPAID= "
							+ rpaID
							+ ","
							+ "ToolID = "
							+ toolID
							+ " where SubActivityFlowChartDefID = "
							+ subActFCDefID + " and StepID= " + stepID;
				} else {
					updateQuery = "update transactionalData.TBL_FLOWCHART_STEP_DETAILS SET StepType= "
							+ "'"
							+ stepType
							+ "'"
							+ ","
							+ "RPAID= "
							+ rpaID
							+ ","
							+ "ToolID = "
							+ toolID
							+ " where SubActivityFlowChartDefID = "
							+ subActFCDefID
							+ " and TaskID= "
							+ taskID
							+ " and StepID= " + stepID;
				}
				update.write(updateQuery + "\n");
				// this.testDAO.updateFlowChartStepDetailsValue(subActFCDefID,
				// stepID, stepName, taskID,
				// taskName,toolID,versionNumber,stepType,rpaID);
			} else {
				if (stepType.equalsIgnoreCase("erd.Relationship")
						|| stepType.equalsIgnoreCase("basic.Circle")) {
					String insertQuery = "Insert into transactionalData.TBL_FLOWCHART_STEP_DETAILS(SubActivityFlowChartDefID,StepID,StepName,TaskID,Task,ExecutionType,ToolID,TaskReason,Active,VersionNumber,MasterTask,StepType,RPAID) Values("
							+ subActFCDefID
							+ ","
							+ stepID
							+ ","
							+ "'"
							+ stepName
							+ "'"
							+ ","
							+ taskID
							+ ","
							+ "'"
							+ taskName
							+ "'"
							+ ","
							+ "'"
							+ executionType
							+ "'"
							+ ","
							+ toolID
							+ ","
							+ "'"
							+ reason
							+ "'"
							+ ","
							+ 1
							+ ","
							+ versionNumber
							+ ","
							+ "'"
							+ masterTask
							+ "'"
							+ ","
							+ "'"
							+ stepType
							+ "'"
							+ ","
							+ rpaID
							+ ")";
					insert.write(insertQuery + "\n");
					// this.testDAO.insertInFlowChartStepDetails(subActFCDefID,
					// stepID, stepName.trim(), taskID, taskName, executionType,
					// toolID, reason, versionNumber, masterTask,
					// stepType,rpaID);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	@SuppressWarnings("unchecked")
	public byte[] generateNewExcelFromJson(HttpServletResponse response, int subActivityFlowChartDefID) 
	{
		//List<Map<String, Object>> defData = this.flowChartDao.getAllFlowCharts(MA);
		 List<Map<String, Object>> data =this.flowChartDao.getAllFlowChartsBySubActivityFlowChartDefID(subActivityFlowChartDefID);
		 byte[] xls=null;
		 
			if (!data.isEmpty()) 
			{

				for (Map<String, Object> a : data) {
					String jsonString = (String) a.get("FlowChartJSON");

					LinkedTreeMap<String, List<LinkedTreeMap<String, Object>>> jsonData = new LinkedTreeMap<String, List<LinkedTreeMap<String, Object>>>();
					Gson gson = new Gson();
					List<LinkedTreeMap<String, Object>> allRouters = new ArrayList<LinkedTreeMap<String, Object>>();
					LinkedTreeMap<String, LinkedTreeMap<String, Object>> allSteps = new LinkedTreeMap<String, LinkedTreeMap<String, Object>>();
					List<String> sequencialStep = new ArrayList<String>();
					LinkedHashMap<Integer, List<String>> finalData = new LinkedHashMap<Integer, List<String>>();
					List<String> preDataList = new ArrayList<String>();
					Collections.addAll(preDataList, "StepId",
							"DisplayStepName", "GraphicalRepresentation",
							"ExecutionType", "Responsible",
							"DependentStepName", "IfDependentOnDecisionStep",
							"IsTaskOrSubTask", "TaskOrParentTaskMapped",
							"ToolName", "ToolID", "TaskName", "TaskID", "OutputUpload", "CascadeInput");
					int count = 1;

					finalData.put(count, preDataList);
					count = 2;
					preDataList = new ArrayList<String>();

					try {
						jsonData = (LinkedTreeMap<String, List<LinkedTreeMap<String, Object>>>) gson
								.fromJson(jsonString, jsonData.getClass());

						try {
							if (jsonData.get("cells") != null) {
								for (int i = 0; i < jsonData.get("cells")
										.size(); i++) {
									if (jsonData.get("cells").get(i)
											.containsKey("size")) {
										String step = (String) jsonData.get("cells").get(i).get("id");
										if (!step.contains("%STEP_ID%"))
										{
											allSteps.put(
													(String) jsonData.get("cells")
															.get(i).get("id"),
													jsonData.get("cells").get(i));
										}
										
									} else if (jsonData.get("cells").get(i)
											.containsKey("router")) {
										allRouters.add(jsonData.get("cells")
												.get(i));
									}
								}
							}
						} catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
							System.out.println("No JSON Found:"
									+ a.get("SubActivityFlowChartDefID"));
							continue;
						}

						for (String stepId : allSteps.keySet()) {
							boolean targetFound = true;
							for (int j = 0; j < allRouters.size(); j++) {
								Map<String, String> targetId = (Map<String, String>) allRouters
										.get(j).get("target");
								if (stepId.equals(targetId.get("id"))) {
									targetFound = false;
								}
							}
							if (targetFound) {
								sequencialStep.add(stepId);
							}
						}

						for (String stepId : allSteps.keySet()) {
							if (!sequencialStep.contains(stepId)) {
								sequencialStep.add(stepId);
							}

						}

						List<String> tmpList = new ArrayList<String>();
						List<String> tmpList1 = new ArrayList<String>();
						tmpList.add(sequencialStep.get(0));
						tmpList1.add(sequencialStep.get(0));

						Map<String, String> typeMap = new HashMap<String, String>();
						typeMap.put("basic.Rect", "Operation");
						typeMap.put("app.ericssonStep", "Operation");
						typeMap.put("basic.WeakEntity", "Operation");
						typeMap.put("erd.WeakEntity", "Operation");
						typeMap.put("app.ericssonWeakEntity", "Operation");
						typeMap.put("erd.RelationShip", "Decision");
						typeMap.put("erd.Relationship", "Decision");
						typeMap.put("ericsson.Decision", "Decision");
						typeMap.put("ericsson.EndStep", "End");
						typeMap.put("basic.Circle", "End");
						typeMap.put("ericsson.Manual", "Operation");
						typeMap.put("ericsson.Automatic", "Operation");

						Map<String, String> executionTypeMap = new HashMap<String, String>();
						executionTypeMap.put("basic.Rect", "Manual");
						executionTypeMap.put("app.ericssonStep", "Manual");
						executionTypeMap.put("basic.WeakEntity", "Automatic");
						executionTypeMap.put("erd.WeakEntity", "Automatic");
						executionTypeMap.put("app.ericssonWeakEntity", "Automatic");
						executionTypeMap.put("erd.RelationShip", "Manual");
						executionTypeMap.put("erd.Relationship", "Manual");
						executionTypeMap.put("ericsson.Decision", "Manual");
						executionTypeMap.put("basic.Circle", "Manual");
						executionTypeMap.put("ericsson.EndStep", "Manual");
						executionTypeMap.put("ericsson.Manual", "Manual");
						executionTypeMap.put("ericsson.StartStep", "End");
						executionTypeMap.put("ericsson.Automatic", "Automatic");


						int l = 0;
						for (String stepId : sequencialStep) {
							l++;
							Map<String, Map<String, String>> attrs = (Map<String, Map<String, String>>) allSteps
									.get(stepId).get("attrs");
							String type = (String) allSteps.get(stepId).get(
									"type");
							String text = "";
							String outputUpload="";
							String cascadeInput="";
						try {
							if (type.matches("ericsson.StartStep")
									|| type.matches("ericsson.EndStep")) {
								if (attrs.get("text") != null
										&& attrs.get("text").get("text") != null) {
									text = attrs.get("text").get("text");
								}
							} else if (type.matches("ericsson.Decision")) {
								if (attrs.get("label") != null
										&& attrs.get("label").get("text") != null) {
									text = attrs.get("label").get("text");
								}
								else {
									if (attrs.get("bodyText") != null
											&& attrs.get("bodyText").get("text") != null) {
										text = attrs.get("bodyText").get("text");
									}
								}
								
							}else if (type.matches("ericsson.Automatic")) {
								
								cascadeInput = (String) allSteps.get(stepId).get(
										"cascadeInput");
								outputUpload = (String) allSteps.get(stepId).get(
										"outputUpload");
								
								
								if (attrs.get("label") != null
										&& attrs.get("label").get("text") != null) {
									text = attrs.get("label").get("text");
								}else {
									if (attrs.get("bodyText") != null
											&& attrs.get("bodyText").get("text") != null) {
										text = attrs.get("bodyText").get("text");
									}
								}
							}
							else {
								
								if (attrs.get("label") != null
										&& attrs.get("label").get("text") != null) {
									text = attrs.get("label").get("text");
								}else {
									if (attrs.get("bodyText") != null
											&& attrs.get("bodyText").get("text") != null) {
										text = attrs.get("bodyText").get("text");
									}
								}
							}
						} catch (Exception e1) {
							// TODO Auto-generated catch block
							System.out.println("Unable to Parse  4 :"
									+ a.get("SubActivityFlowChartDefID"));
							e1.printStackTrace();
						}

							String toolName = "";
							String taskName = "";
							String displayStepName = "";
							String graphicalRepresentation = "";
							String executionType = "Manual";
							String taskID = "";
							String toolID = "";
							
							try {
								if (attrs.get("task") != null) {
								//	executionType = attrs.get("task").get("executionType");
									if (attrs.get("task").get("taskID") != null) {
										taskID = attrs.get("task").get("taskID");
										Map<String, Object> taskData = this.flowChartDao.getTaskNameByID(taskID);
										if (taskData != null) {
											taskName = (String) taskData.get("Task");
										}

									}
									if (attrs.get("task").get("toolID") != null) {
										toolID = attrs.get("task").get("toolID");
										if (toolID.equals("0")) {
											toolID = "2";
										}
										Map<String, Object> toolData = this.flowChartDao.getToolNameByID(toolID);
										if (toolData != null) {
											toolName = (String) toolData.get("Tool");
										}

									}
								}
							} catch (Exception e1) {
								// TODO Auto-generated catch block
								e1.printStackTrace();
							}

							String responsible = "GLOBAL";
							String isTaskOrSubTask = "subtask";
							String taskOrParentTaskMapped = "";
							if (attrs.get("task") != null) {
								taskOrParentTaskMapped = attrs.get("task").get("taskName");
							}

							text = text.replace("\n", "").replace("\r", "");
							String pattern = "";
							text = text.replaceAll("Tool Name", "ToolName");
							if (text.contains("ToolName") && text.contains("AvgEstdEffort")) {
								pattern = "^(.*?)ToolName\\:(.*?)AvgEstdEffort.*?$";
								Pattern r = Pattern.compile(pattern);

								// Now create matcher object.
								Matcher m = r.matcher(text);
								if (m.find()) {
									displayStepName = m.group(1);
									// toolName = m.group(2);
								}
							} else if (text.contains("ToolName")) {
								pattern = "^(.*?)ToolName:(.*?)$";
								Pattern r = Pattern.compile(pattern);

								// Now create matcher object.
								Matcher m = r.matcher(text);
								if (m.find()) {
									displayStepName = m.group(1);
									// toolName = m.group(2);
								}
							} else {
								pattern = "^(.*?)$";
								Pattern r = Pattern.compile(pattern);

								Matcher m = r.matcher(text);
								if (m.find()) {
									displayStepName = m.group(0);
								}
							}

							if (l > 1) {
								for (int j = 0; j < allRouters.size(); j++) {

									Map<String, String> targetId = (Map<String, String>) allRouters.get(j).get("target");
									if (stepId.equals(targetId.get("id"))) {
										count++;
										Map<String, String> sourceId = (Map<String, String>) allRouters.get(j).get("source");
										List<Map<String, Map<String, Map<String, String>>>> labelInfo = null;

										String IfDependentOnDecisionStep = "";
										try {
											if (allRouters.get(j).get("labels") != null) {
												labelInfo = (List<Map<String, Map<String, Map<String, String>>>>) allRouters
														.get(j).get("labels");
												if (!labelInfo.isEmpty()) {
													IfDependentOnDecisionStep = labelInfo
															.get(0)
															.get("attrs")
															.get("text")
															.get("text");
												}

											}
										} catch (Exception e) {
											// TODO Auto-generated catch block
											e.printStackTrace();
											System.out
													.println("unable to parse 2 : "
															+ a.get("SubActivityFlowChartDefID"));
										}

										String dependentStepName = "";

										String depedentStepId = sourceId.get("id");
										Map<String, Map<String, String>> attrs1 = null;
										if (allSteps.get(depedentStepId) != null) {
											attrs1 = (Map<String, Map<String, String>>) allSteps.get(depedentStepId).get("attrs");
											String type1 = (String) allSteps.get(depedentStepId).get("type");
											if (type1.matches("ericsson.StartStep")
													|| type1.matches("ericsson.EndStep")) {
												if (attrs1.get("text") != null
														&& attrs1.get("text").get("text") != null) {
													dependentStepName = attrs1.get("text").get("text");
												}
											} else if (type1.matches("ericsson.Decision")) {
												if (attrs1.get("label") != null
														&& attrs1.get("label").get("text") != null) {
													dependentStepName = attrs1.get("label").get("text");
												}
												
											}else {
												if ((String) allSteps.get(depedentStepId).get("name") != null) {
													dependentStepName = (String) allSteps.get(depedentStepId).get("name");
												}
											}
										}

										if (type.equals("ericsson.Decision") || type.equals("ericsson.EndStep")){
											isTaskOrSubTask = "";
										}
										graphicalRepresentation = typeMap.get(type);
										executionType = executionTypeMap.get(type);
										Collections.addAll(preDataList, stepId,
												displayStepName,
												graphicalRepresentation,
												executionType, responsible,
												dependentStepName,
												IfDependentOnDecisionStep,
												isTaskOrSubTask,
												taskOrParentTaskMapped,
												toolName, toolID, taskName,
												taskID, outputUpload, cascadeInput);
										finalData.put(count, preDataList);
										preDataList = new ArrayList<String>();
									}
								}
							} else {
								if (type.equals("ericsson.StartStep")) {
									graphicalRepresentation = "Start";
								} else {
									graphicalRepresentation = typeMap.get(type);
								}
								Collections.addAll(preDataList, stepId,
										displayStepName,
										graphicalRepresentation, "Manual",
										responsible, "", "", "", "", "", "",
										"", "");
								finalData.put(2, preDataList);
								preDataList = new ArrayList<String>();
							}
						}
					} catch (JsonSyntaxException e) {
						// TODO Auto-generated catch block
						System.out.println(a.get("SubActivityFlowChartDefID"));
					}

					// Create a blank sheet
					XSSFWorkbook workbook = new XSSFWorkbook();
					XSSFSheet sheet = workbook.createSheet("data");

					Set<Integer> keyset = finalData.keySet();
					int rownum = 0;
					for (Integer key : keyset) {
						// this creates a new row in the sheet
						Row row = sheet.createRow(rownum++);
						List<String> objArr = finalData.get(key);
						int cellnum = 0;
						for (Object obj : objArr) {
							// this line creates a cell in the next column of
							// that
							// row
							Cell cell = row.createCell(cellnum++);
							if (obj instanceof String)
								cell.setCellValue((String) obj);
							else if (obj instanceof Integer)
								cell.setCellValue((Integer) obj);
						}
					}
					try 
					{
						 ByteArrayOutputStream baos = new ByteArrayOutputStream();
						 
					   	//response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
						//response.setHeader("Content-Disposition","attachment;filename=" + a.get("SubActivityFlowChartDefID")+ ".xlsx");
						// this Writes the workbook gfgcontribute
						//OutputStream out = response.getOutputStream();
						//workbook.write(out);

						 workbook.write(baos);
						xls = baos.toByteArray();

					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
			
			return xls;
	}
	

	@SuppressWarnings("unchecked")
	public void generateExcelFromJson(HttpServletResponse response, int subActivityFlowChartDefID) {
		//List<Map<String, Object>> defData = this.flowChartDao.getAllFlowCharts(MA);
		 List<Map<String, Object>> data =this.flowChartDao.getAllFlowChartsBySubActivityFlowChartDefID(subActivityFlowChartDefID);

			if (!data.isEmpty()) {

				for (Map<String, Object> a : data) {
					String jsonString = (String) a.get("FlowChartJSON");

					LinkedTreeMap<String, List<LinkedTreeMap<String, Object>>> jsonData = new LinkedTreeMap<String, List<LinkedTreeMap<String, Object>>>();
					Gson gson = new Gson();
					List<LinkedTreeMap<String, Object>> allRouters = new ArrayList<LinkedTreeMap<String, Object>>();
					LinkedTreeMap<String, LinkedTreeMap<String, Object>> allSteps = new LinkedTreeMap<String, LinkedTreeMap<String, Object>>();
					List<String> sequencialStep = new ArrayList<String>();
					LinkedHashMap<Integer, List<String>> finalData = new LinkedHashMap<Integer, List<String>>();
					List<String> preDataList = new ArrayList<String>();
					Collections.addAll(preDataList, "StepId",
							"DisplayStepName", "GraphicalRepresentation",
							"ExecutionType", "Responsible",
							"DependentStepName", "IfDependentOnDecisionStep",
							"IsTaskOrSubTask", "TaskOrParentTaskMapped",
							"ToolName", "ToolID", "TaskName", "TaskID");
					int count = 1;

					finalData.put(count, preDataList);
					count = 2;
					preDataList = new ArrayList<String>();

					try {
						jsonData = (LinkedTreeMap<String, List<LinkedTreeMap<String, Object>>>) gson
								.fromJson(jsonString, jsonData.getClass());

						try {
							if (jsonData.get("cells") != null) {
								for (int i = 0; i < jsonData.get("cells")
										.size(); i++) {
									if (jsonData.get("cells").get(i)
											.containsKey("size")) {
										allSteps.put(
												(String) jsonData.get("cells")
														.get(i).get("id"),
												jsonData.get("cells").get(i));
									} else if (jsonData.get("cells").get(i)
											.containsKey("router")) {
										allRouters.add(jsonData.get("cells")
												.get(i));
									}
								}
							}
						} catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
							System.out.println("No JSON Found:"
									+ a.get("SubActivityFlowChartDefID"));
							continue;
						}

						for (String stepId : allSteps.keySet()) {
							boolean targetFound = true;
							for (int j = 0; j < allRouters.size(); j++) {
								Map<String, String> targetId = (Map<String, String>) allRouters
										.get(j).get("target");
								if (stepId.equals(targetId.get("id"))) {
									targetFound = false;
								}
							}
							if (targetFound) {
								sequencialStep.add(stepId);
							}
						}

						for (String stepId : allSteps.keySet()) {
							if (!sequencialStep.contains(stepId)) {
								sequencialStep.add(stepId);
							}

						}

						List<String> tmpList = new ArrayList<String>();
						List<String> tmpList1 = new ArrayList<String>();
						tmpList.add(sequencialStep.get(0));
						tmpList1.add(sequencialStep.get(0));

						Map<String, String> typeMap = new HashMap<String, String>();
						typeMap.put("basic.Rect", "Operation");
						typeMap.put("app.ericssonStep", "Operation");
						typeMap.put("basic.WeakEntity", "Operation");
						typeMap.put("erd.WeakEntity", "Operation");
						typeMap.put("app.ericssonWeakEntity", "Operation");
						typeMap.put("erd.RelationShip", "Decision");
						typeMap.put("erd.Relationship", "Decision");
						typeMap.put("basic.Circle", "End");
						

						Map<String, String> executionTypeMap = new HashMap<String, String>();
						executionTypeMap.put("basic.Rect", "Manual");
						executionTypeMap.put("app.ericssonStep", "Manual");
						executionTypeMap.put("basic.WeakEntity", "Automatic");
						executionTypeMap.put("erd.WeakEntity", "Automatic");
						executionTypeMap.put("app.ericssonWeakEntity", "Automatic");
						executionTypeMap.put("erd.RelationShip", "Manual");
						executionTypeMap.put("erd.Relationship", "Manual");
						executionTypeMap.put("basic.Circle", "Manual");


						int l = 0;
						for (String stepId : sequencialStep) {
							l++;
							Map<String, Map<String, String>> attrs = (Map<String, Map<String, String>>) allSteps
									.get(stepId).get("attrs");
							String type = (String) allSteps.get(stepId).get(
									"type");
							String text = "";
							try {
								if (attrs.get("text") != null
										&& attrs.get("text").get("text") != null) {
									text = attrs.get("text").get("text");
								} else {
									text = "";

								}
							} catch (Exception e1) {
								// TODO Auto-generated catch block
								System.out.println("Unable to Parse  4 :"
										+ a.get("SubActivityFlowChartDefID"));
								e1.printStackTrace();
							}

							String toolName = "";
							String taskName = "";
							String displayStepName = "";
							String graphicalRepresentation = "";
							String executionType = "Manual";
							String taskID = "";
							String toolID = "";
							try {
								if (attrs.get("task") != null) {
								//	executionType = attrs.get("task").get("executionType");
									if (attrs.get("task").get("taskID") != null) {
										taskID = attrs.get("task")
												.get("taskID");
										Map<String, Object> taskData = this.flowChartDao
												.getTaskNameByID(taskID);
										if (taskData != null) {
											taskName = (String) taskData
													.get("Task");
										}

									}
									if (attrs.get("task").get("toolID") != null) {
										toolID = attrs.get("task")
												.get("toolID");
										if (toolID.equals("0")) {
											toolID = "2";
										}
										Map<String, Object> toolData = this.flowChartDao
												.getToolNameByID(toolID);
										if (toolData != null) {
											toolName = (String) toolData
													.get("Tool");
										}

									}
								}
							} catch (Exception e1) {
								// TODO Auto-generated catch block
								e1.printStackTrace();
							}

							String responsible = "GLOBAL";
							String isTaskOrSubTask = "subtask";
							String taskOrParentTaskMapped = "";
							if (attrs.get("task") != null) {
								taskOrParentTaskMapped = attrs.get("task").get(
										"taskName");
							}

							text = text.replace("\n", "").replace("\r", "");
							String pattern = "";
							text = text.replaceAll("Tool Name", "ToolName");
							if (text.contains("ToolName")
									&& text.contains("AvgEstdEffort")) {
								pattern = "^(.*?)ToolName\\:(.*?)AvgEstdEffort.*?$";
								Pattern r = Pattern.compile(pattern);

								// Now create matcher object.
								Matcher m = r.matcher(text);
								if (m.find()) {
									displayStepName = m.group(1);
									// toolName = m.group(2);
								}
							} else if (text.contains("ToolName")) {
								pattern = "^(.*?)ToolName:(.*?)$";
								Pattern r = Pattern.compile(pattern);

								// Now create matcher object.
								Matcher m = r.matcher(text);
								if (m.find()) {
									displayStepName = m.group(1);
									// toolName = m.group(2);
								}
							} else {
								pattern = "^(.*?)$";
								Pattern r = Pattern.compile(pattern);

								Matcher m = r.matcher(text);
								if (m.find()) {
									displayStepName = m.group(0);
								}
							}

							if (l > 1) {
								for (int j = 0; j < allRouters.size(); j++) {

									Map<String, String> targetId = (Map<String, String>) allRouters
											.get(j).get("target");
									if (stepId.equals(targetId.get("id"))) {
										count++;
										Map<String, String> sourceId = (Map<String, String>) allRouters
												.get(j).get("source");
										List<Map<String, Map<String, Map<String, String>>>> labelInfo = null;

										String IfDependentOnDecisionStep = "";
										try {
											if (allRouters.get(j).get("labels") != null) {
												labelInfo = (List<Map<String, Map<String, Map<String, String>>>>) allRouters
														.get(j).get("labels");
												if (!labelInfo.isEmpty()) {
													IfDependentOnDecisionStep = labelInfo
															.get(0)
															.get("attrs")
															.get("text")
															.get("text");
												}

											}
										} catch (Exception e) {
											// TODO Auto-generated catch block
											e.printStackTrace();
											System.out
													.println("unable to parse 2 : "
															+ a.get("SubActivityFlowChartDefID"));
										}

										String dependentStepName = "";

										String depedentStepId = sourceId
												.get("id");
										Map<String, Map<String, String>> attrs1 = null;
										if (allSteps.get(depedentStepId) != null) {
											attrs1 = (Map<String, Map<String, String>>) allSteps
													.get(depedentStepId).get(
															"attrs");
											String text1 = "";
											if (attrs1.get("text") != null
													&& attrs1.get("text").get(
															"text") != null) {
												text1 = attrs1.get("text").get(
														"text");
											}
											text1 = text1.replace("\n", "")
													.replace("\r", "");
											String pattern1 = "";
											text1 = text1.replaceAll(
													"Tool Name", "ToolName");
											if (text1.contains("ToolName")
													&& text1.contains("AvgEstdEffort")) {
												pattern1 = "^(.*?)ToolName\\:(.*?)AvgEstdEffort.*?$";
												Pattern r = Pattern
														.compile(pattern1);

												// Now create matcher object.
												Matcher m = r.matcher(text1);
												if (m.find()) {
													dependentStepName = m
															.group(1);
												}
											} else if (text1
													.contains("ToolName")) {
												pattern1 = "^(.*?)ToolName:(.*?)$";
												Pattern r = Pattern
														.compile(pattern1);

												// Now create matcher object.
												Matcher m = r.matcher(text1);
												if (m.find()) {
													dependentStepName = m
															.group(1);
												}
											} else {
												pattern1 = "^(.*?)$";
												Pattern r = Pattern
														.compile(pattern1);

												Matcher m = r.matcher(text1);
												if (m.find()) {
													dependentStepName = m
															.group(0);
												}
											}
										}

										graphicalRepresentation = typeMap.get(type);
										executionType = executionTypeMap.get(type);
										Collections.addAll(preDataList, stepId,
												displayStepName,
												graphicalRepresentation,
												executionType, responsible,
												dependentStepName,
												IfDependentOnDecisionStep,
												isTaskOrSubTask,
												taskOrParentTaskMapped,
												toolName, toolID, taskName,
												taskID);
										finalData.put(count, preDataList);
										preDataList = new ArrayList<String>();
									}
								}
							} else {
								if (type.equals("basic.Circle")) {
									graphicalRepresentation = "Start";
								} else {
									graphicalRepresentation = typeMap.get(type);
								}
								Collections.addAll(preDataList, stepId,
										displayStepName,
										graphicalRepresentation, "Manual",
										responsible, "", "", "", "", "", "",
										"", "");
								finalData.put(2, preDataList);
								preDataList = new ArrayList<String>();
							}
						}
					} catch (JsonSyntaxException e) {
						// TODO Auto-generated catch block
						System.out.println(a.get("SubActivityFlowChartDefID"));
					}

					// Create a blank sheet
					XSSFWorkbook workbook = new XSSFWorkbook();
					XSSFSheet sheet = workbook.createSheet("data");

					Set<Integer> keyset = finalData.keySet();
					int rownum = 0;
					for (Integer key : keyset) {
						// this creates a new row in the sheet
						Row row = sheet.createRow(rownum++);
						List<String> objArr = finalData.get(key);
						int cellnum = 0;
						for (Object obj : objArr) {
							// this line creates a cell in the next column of
							// that
							// row
							Cell cell = row.createCell(cellnum++);
							if (obj instanceof String)
								cell.setCellValue((String) obj);
							else if (obj instanceof Integer)
								cell.setCellValue((Integer) obj);
						}
					}
					try {
					   	response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
						response.setHeader("Content-Disposition","attachment;filename=" + a.get("SubActivityFlowChartDefID")+ ".xlsx");
						// this Writes the workbook gfgcontribute
						OutputStream out = response.getOutputStream();
						workbook.write(out);

						out.flush();
						out.close();

					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
	}

	@SuppressWarnings("unchecked")
	public void generateExcelFromJsonAll(String MA) {
		List<Map<String, Object>> defData = this.flowChartDao
				.getAllFlowCharts(MA);
		// List<Map<String, Object>> data =
		// this.flowChartDao.getAllFlowChartsBySubActivityFlowChartDefID(subActivityFlowChartDefID);

		for (int k = 0; k < defData.size(); k++) {
			defData.get(k).get("SubActivityFlowChartDefID");
			List<Map<String, Object>> data = this.flowChartDao
					.getAllFlowChartsBySubActivityFlowChartDefID((int) defData
							.get(k).get("SubActivityFlowChartDefID"));
			if (!data.isEmpty()) {

				for (Map<String, Object> a : data) {
					String jsonString = (String) a.get("FlowChartJSON");

					LinkedTreeMap<String, List<LinkedTreeMap<String, Object>>> jsonData = new LinkedTreeMap<String, List<LinkedTreeMap<String, Object>>>();
					Gson gson = new Gson();
					List<LinkedTreeMap<String, Object>> allRouters = new ArrayList<LinkedTreeMap<String, Object>>();
					LinkedTreeMap<String, LinkedTreeMap<String, Object>> allSteps = new LinkedTreeMap<String, LinkedTreeMap<String, Object>>();
					List<String> sequencialStep = new ArrayList<String>();
					LinkedHashMap<Integer, List<String>> finalData = new LinkedHashMap<Integer, List<String>>();
					List<String> preDataList = new ArrayList<String>();
					Collections.addAll(preDataList, "StepId",
							"DisplayStepName", "GraphicalRepresentation",
							"ExecutionType", "Responsible",
							"DependentStepName", "IfDependentOnDecisionStep",
							"IsTaskOrSubTask", "TaskOrParentTaskMapped",
							"ToolName", "ToolID", "TaskName", "TaskID");
					int count = 1;

					finalData.put(count, preDataList);
					count = 2;
					preDataList = new ArrayList<String>();

					try {
						jsonData = (LinkedTreeMap<String, List<LinkedTreeMap<String, Object>>>) gson
								.fromJson(jsonString, jsonData.getClass());

						try {
							if (jsonData.get("cells") != null) {
								for (int i = 0; i < jsonData.get("cells")
										.size(); i++) {
									if (jsonData.get("cells").get(i)
											.containsKey("size")) {
										allSteps.put(
												(String) jsonData.get("cells")
														.get(i).get("id"),
												jsonData.get("cells").get(i));
									} else if (jsonData.get("cells").get(i)
											.containsKey("router")) {
										allRouters.add(jsonData.get("cells")
												.get(i));
									}
								}
							}
						} catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
							System.out.println("No JSON Found:"
									+ a.get("SubActivityFlowChartDefID"));
							continue;
						}

						for (String stepId : allSteps.keySet()) {
							boolean targetFound = true;
							for (int j = 0; j < allRouters.size(); j++) {
								Map<String, String> targetId = (Map<String, String>) allRouters
										.get(j).get("target");
								if (stepId.equals(targetId.get("id"))) {
									targetFound = false;
								}
							}
							if (targetFound) {
								sequencialStep.add(stepId);
							}
						}

						for (String stepId : allSteps.keySet()) {
							if (!sequencialStep.contains(stepId)) {
								sequencialStep.add(stepId);
							}

						}

						List<String> tmpList = new ArrayList<String>();
						List<String> tmpList1 = new ArrayList<String>();
						tmpList.add(sequencialStep.get(0));
						tmpList1.add(sequencialStep.get(0));

						Map<String, String> typeMap = new HashMap<String, String>();
						typeMap.put("basic.Rect", "Operation");
						typeMap.put("app.ericssonStep", "Operation");
						typeMap.put("basic.WeakEntity", "Operation");
						typeMap.put("erd.WeakEntity", "Operation");
						typeMap.put("app.ericssonWeakEntity", "Operation");
						typeMap.put("erd.RelationShip", "Decision");
						typeMap.put("erd.Relationship", "Decision");
						typeMap.put("basic.Circle", "End");
						

						Map<String, String> executionTypeMap = new HashMap<String, String>();
						executionTypeMap.put("basic.Rect", "Manual");
						executionTypeMap.put("app.ericssonStep", "Manual");
						executionTypeMap.put("basic.WeakEntity", "Automatic");
						executionTypeMap.put("erd.WeakEntity", "Automatic");
						executionTypeMap.put("app.ericssonWeakEntity", "Automatic");
						executionTypeMap.put("erd.RelationShip", "Manual");
						executionTypeMap.put("erd.Relationship", "Manual");
						executionTypeMap.put("basic.Circle", "Manual");


						int l = 0;
						for (String stepId : sequencialStep) {
							l++;
							Map<String, Map<String, String>> attrs = (Map<String, Map<String, String>>) allSteps
									.get(stepId).get("attrs");
							String type = (String) allSteps.get(stepId).get(
									"type");
							String text = "";
							try {
								if (attrs.get("text") != null
										&& attrs.get("text").get("text") != null) {
									text = attrs.get("text").get("text");
								} else {
									text = "";

								}
							} catch (Exception e1) {
								// TODO Auto-generated catch block
								System.out.println("Unable to Parse  4 :"
										+ a.get("SubActivityFlowChartDefID"));
								e1.printStackTrace();
							}

							String toolName = "";
							String taskName = "";
							String displayStepName = "";
							String graphicalRepresentation = "";
							String executionType = "Manual";
							String taskID = "";
							String toolID = "";
							try {
								if (attrs.get("task") != null) {
								//	executionType = attrs.get("task").get("executionType");
									if (attrs.get("task").get("taskID") != null) {
										taskID = attrs.get("task")
												.get("taskID");
										Map<String, Object> taskData = this.flowChartDao
												.getTaskNameByID(taskID);
										if (taskData != null) {
											taskName = (String) taskData
													.get("Task");
										}

									}
									if (attrs.get("task").get("toolID") != null) {
										toolID = attrs.get("task")
												.get("toolID");
										if (toolID.equals("0")) {
											toolID = "2";
										}
										Map<String, Object> toolData = this.flowChartDao
												.getToolNameByID(toolID);
										if (toolData != null) {
											toolName = (String) toolData
													.get("Tool");
										}

									}
								}
							} catch (Exception e1) {
								// TODO Auto-generated catch block
								e1.printStackTrace();
							}

							String responsible = "GLOBAL";
							String isTaskOrSubTask = "subtask";
							String taskOrParentTaskMapped = "";
							if (attrs.get("task") != null) {
								taskOrParentTaskMapped = attrs.get("task").get(
										"taskName");
							}

							text = text.replace("\n", "").replace("\r", "");
							String pattern = "";
							text = text.replaceAll("Tool Name", "ToolName");
							if (text.contains("ToolName")
									&& text.contains("AvgEstdEffort")) {
								pattern = "^(.*?)ToolName\\:(.*?)AvgEstdEffort.*?$";
								Pattern r = Pattern.compile(pattern);

								// Now create matcher object.
								Matcher m = r.matcher(text);
								if (m.find()) {
									displayStepName = m.group(1);
									// toolName = m.group(2);
								}
							} else if (text.contains("ToolName")) {
								pattern = "^(.*?)ToolName:(.*?)$";
								Pattern r = Pattern.compile(pattern);

								// Now create matcher object.
								Matcher m = r.matcher(text);
								if (m.find()) {
									displayStepName = m.group(1);
									// toolName = m.group(2);
								}
							} else {
								pattern = "^(.*?)$";
								Pattern r = Pattern.compile(pattern);

								Matcher m = r.matcher(text);
								if (m.find()) {
									displayStepName = m.group(0);
								}
							}

							if (l > 1) {
								for (int j = 0; j < allRouters.size(); j++) {

									Map<String, String> targetId = (Map<String, String>) allRouters
											.get(j).get("target");
									if (stepId.equals(targetId.get("id"))) {
										count++;
										Map<String, String> sourceId = (Map<String, String>) allRouters
												.get(j).get("source");
										List<Map<String, Map<String, Map<String, String>>>> labelInfo = null;

										String IfDependentOnDecisionStep = "";
										try {
											if (allRouters.get(j).get("labels") != null) {
												labelInfo = (List<Map<String, Map<String, Map<String, String>>>>) allRouters
														.get(j).get("labels");
												if (!labelInfo.isEmpty()) {
													IfDependentOnDecisionStep = labelInfo
															.get(0)
															.get("attrs")
															.get("text")
															.get("text");
												}

											}
										} catch (Exception e) {
											// TODO Auto-generated catch block
											e.printStackTrace();
											System.out
													.println("unable to parse 2 : "
															+ a.get("SubActivityFlowChartDefID"));
										}

										String dependentStepName = "";

										String depedentStepId = sourceId
												.get("id");
										Map<String, Map<String, String>> attrs1 = null;
										if (allSteps.get(depedentStepId) != null) {
											attrs1 = (Map<String, Map<String, String>>) allSteps
													.get(depedentStepId).get(
															"attrs");
											String text1 = "";
											if (attrs1.get("text") != null
													&& attrs1.get("text").get(
															"text") != null) {
												text1 = attrs1.get("text").get(
														"text");
											}
											text1 = text1.replace("\n", "")
													.replace("\r", "");
											String pattern1 = "";
											text1 = text1.replaceAll(
													"Tool Name", "ToolName");
											if (text1.contains("ToolName")
													&& text1.contains("AvgEstdEffort")) {
												pattern1 = "^(.*?)ToolName\\:(.*?)AvgEstdEffort.*?$";
												Pattern r = Pattern
														.compile(pattern1);

												// Now create matcher object.
												Matcher m = r.matcher(text1);
												if (m.find()) {
													dependentStepName = m
															.group(1);
												}
											} else if (text1
													.contains("ToolName")) {
												pattern1 = "^(.*?)ToolName:(.*?)$";
												Pattern r = Pattern
														.compile(pattern1);

												// Now create matcher object.
												Matcher m = r.matcher(text1);
												if (m.find()) {
													dependentStepName = m
															.group(1);
												}
											} else {
												pattern1 = "^(.*?)$";
												Pattern r = Pattern
														.compile(pattern1);

												Matcher m = r.matcher(text1);
												if (m.find()) {
													dependentStepName = m
															.group(0);
												}
											}
										}

										graphicalRepresentation = typeMap.get(type);
										executionType = executionTypeMap.get(type);
										Collections.addAll(preDataList, stepId,
												displayStepName,
												graphicalRepresentation,
												executionType, responsible,
												dependentStepName,
												IfDependentOnDecisionStep,
												isTaskOrSubTask,
												taskOrParentTaskMapped,
												toolName, toolID, taskName,
												taskID);
										finalData.put(count, preDataList);
										preDataList = new ArrayList<String>();
									}
								}
							} else {
								if (type.equals("basic.Circle")) {
									graphicalRepresentation = "Start";
								} else {
									graphicalRepresentation = typeMap.get(type);
								}
								Collections.addAll(preDataList, stepId,
										displayStepName,
										graphicalRepresentation, "Manual",
										responsible, "", "", "", "", "", "",
										"", "");
								finalData.put(2, preDataList);
								preDataList = new ArrayList<String>();
							}
						}
					} catch (JsonSyntaxException e) {
						// TODO Auto-generated catch block
						System.out.println(a.get("SubActivityFlowChartDefID"));
					}

					// Create a blank sheet
					XSSFWorkbook workbook = new XSSFWorkbook();
					XSSFSheet sheet = workbook.createSheet("data");

					Set<Integer> keyset = finalData.keySet();
					int rownum = 0;
					for (Integer key : keyset) {
						// this creates a new row in the sheet
						Row row = sheet.createRow(rownum++);
						List<String> objArr = finalData.get(key);
						int cellnum = 0;
						for (Object obj : objArr) {
							// this line creates a cell in the next column of
							// that
							// row
							Cell cell = row.createCell(cellnum++);
							if (obj instanceof String)
								cell.setCellValue((String) obj);
							else if (obj instanceof Integer)
								cell.setCellValue((Integer) obj);
						}
					}
					try {
						// this Writes the workbook gfgcontribute
						FileOutputStream out = new FileOutputStream(new File(
								configurations.getStringProperty(ConfigurationFilesConstants.FILE_PATH) + "workflow/"
										+ a.get("SubActivityFlowChartDefID")
										+ ".xlsx"));
						workbook.write(out);

						out.flush();
						out.close();

					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
		}

	}
	

	public String generateNewJSONFromFile() {
		String flowChartDefID = "0";
		try {
			File[] lstFile = new File(configurations.getStringProperty(ConfigurationFilesConstants.FILE_PATH) + "workflow/").listFiles();
			for (File flowChartFile : lstFile) {
				String fileName = flowChartFile.getName();

				flowChartDefID = FilenameUtils.removeExtension(fileName);
				FileOutputStream fos = new FileOutputStream(configurations.getStringProperty(ConfigurationFilesConstants.FILE_PATH_NEW)
						+ flowChartDefID + ".csv");
				InputStream inputStream = null;
				inputStream = new FileInputStream(flowChartFile);
				Workbook wb = WorkbookFactory.create(inputStream);
				int numberOfSheet = wb.getNumberOfSheets();
				StringBuffer data = new StringBuffer();
				for (int i = 0; i < numberOfSheet; i++) {
					Sheet sheet = wb.getSheetAt(i);
					Iterator<Row> rowIterator = sheet.iterator();
					Row row;
					while (rowIterator.hasNext()) {
						row = rowIterator.next();
						List<Cell> cells = new ArrayList<Cell>();
						// For each row, iterate through each columns
						Iterator<Cell> cellIterator = row.cellIterator();
						int lastColumn = Math.max(row.getLastCellNum(), 13);
						for (int cn = 0; cn < lastColumn; cn++) {
							Cell c = row.getCell(cn, Row.RETURN_BLANK_AS_NULL);
							cells.add(c);
						}

						for (Cell cell : cells) {
							if (cell != null) {
								switch (cell.getCellType()) {
								case Cell.CELL_TYPE_BOOLEAN:
									data.append(cell.getBooleanCellValue()
											+ "|");

									break;
								case Cell.CELL_TYPE_NUMERIC:
									data.append(cell.getNumericCellValue()
											+ "|");

									break;
								case Cell.CELL_TYPE_STRING:

									data.append(cell.getStringCellValue()
											.replace("|", "").replace("\r", "")
											.replace("\n", "")
											.replace("\r\n", "")
											+ "|");
									break;

								case Cell.CELL_TYPE_BLANK:
									data.append("|");
									break;

								case Cell.CELL_TYPE_FORMULA:
									try {
										data.append(cell.getNumericCellValue()
												+ "|");
									} catch (Exception e) {
										// TODO Auto-generated catch block
										e.printStackTrace();
									}
									break;

								default:
									data.append("|");

								}
							} else {
								data.append("|");
							}
						}

						data.deleteCharAt(data.length() - 1);
						data.append("\r\n");
					}
				}
				String mainData = data.toString();
				fos.write(mainData.getBytes());
				fos.close();
				fileName = FilenameUtils.removeExtension(fileName);
				appService.CsvBulkUploadNewGenWorkFlowMig(configurations.getStringProperty(ConfigurationFilesConstants.FILE_PATH_NEW)
						+ flowChartDefID + ".csv", "FlowChartCreationTmp_MIGRATION", "UPLOAD_"+fileName);
				try {
			           uploadExcel("UPLOAD_"+fileName,flowChartDefID,flowChartFile);
//			        FileOutputStream outStream = new FileOutputStream(new File(path + "workflowSuccess/"+fileName));
//			        outStream.flush();
//			        outStream.close();
			        } catch (Exception e) {
			            throw e;
			        } finally {
			           appService.dropTable("UPLOAD_"+fileName);
			        }
			}
		} catch (Exception e) {
			if(flowChartDefID!="0") {
			    testDAO.insertStatus("Failure", Integer.parseInt(flowChartDefID));
			}
			LOG.info("Failed FlowchartDeID:" + flowChartDefID);
			e.printStackTrace();
		}
		return "Migration Completed";
	}


	private void uploadExcel(String fileName, String flowChartDefID,File flowChartFile){
	        try {
	           this.testDAO.uploadExcel(fileName,Integer.parseInt(flowChartDefID));
	                List<FlowChartStepModel> flowChartStepModel = this.testDAO.getFlowChartStepDetails(Integer.parseInt(flowChartDefID));
	                List<FlowChartStepModel> stepJSON = generateJSON(flowChartStepModel,Integer.parseInt(flowChartDefID));
	                if(stepJSON!=null) {
		                List<FlowChartDependencyModel> dependencyModel = this.testDAO.getDependencyStep(Integer.parseInt(flowChartDefID));
		                List<FlowChartDependencyModel> linkJSON = generateLink(dependencyModel);
		                String jsonData = createFinalJSON(stepJSON,linkJSON);
		                testDAO.updateFlowChartJsonDATA(Integer.parseInt(flowChartDefID),jsonData);
//		                System.out.println("Final JSON:"+jsonData);
		                testDAO.insertStatus("Success",Integer.parseInt(flowChartDefID));
		                deleteMigrationTableData(Integer.parseInt(flowChartDefID));
		                try {
//		               	    Files.write(Paths.get(path+"worflowBackup/"),fileName.getBytes());
		                	flowChartFile.renameTo(new File(configurations.getStringProperty(ConfigurationFilesConstants.FILE_PATH)+"worflowBackup/"+flowChartFile.getName()));
		                	flowChartFile.delete();
		                }catch(Exception ex) {
		                	ex.printStackTrace();
		                }
	                }
	        } catch (Exception ex) {
	        	testDAO.insertStatus("Failure", Integer.parseInt(flowChartDefID));
	        	deleteMigrationTableData(Integer.parseInt(flowChartDefID));
	           ex.printStackTrace();
	        }
	}

	 private void deleteMigrationTableData(int flowChartDefID) {
		 
		this.testDAO.deleteMigrationData(flowChartDefID);
		
		this.testDAO.deleteMigrationStepData(flowChartDefID);
		
	}

	private List<FlowChartStepModel> generateJSON(List<FlowChartStepModel> flowChartStepModel,int flowChartDefID){
        int x = 100;
        int y = 50;
        String json = "";
        String newJSON;
        List<FlowChartStepModel> stepJSON = new ArrayList<>();
        try {
            for (FlowChartStepModel flowModel : flowChartStepModel) {
                if (flowModel.getGraphicalRepresentation().equalsIgnoreCase("start")) {
                    json = FlowChartStepTemplateMigration.START_STEP_JSON;
                    newJSON = updateJSON(json, flowModel,x, y, new HashMap<>(),new HashMap<>());
                    flowModel.setStepJSON(newJSON);
                } else if (flowModel.getGraphicalRepresentation().equalsIgnoreCase("operation")) {
                	int subActivityID = testDAO.getFlowChartSubActivityID(flowChartDefID);
                    Map<String, Object> taskModel = this.testDAO.getTaskDetailForSID(subActivityID, flowModel.getTaskID());
                    Map<String,String> toolData = this.testDAO.getToolDetailForWF(flowModel.getToolID());
                    if (!taskModel.isEmpty() && taskModel.size()>0) {
                        if ((flowModel.getExecutionType().equalsIgnoreCase("Automated")) || (flowModel.getExecutionType().equalsIgnoreCase("Automatic"))) {
                            json = FlowChartStepTemplateMigration.AUTOMATED_JSON_WITH_TASK;
                        } else {
                            json = FlowChartStepTemplateMigration.OPERATION_STEP_JSON_WITH_TASK;
                        }
                        newJSON = updateJSON(json,flowModel,x, y,taskModel,toolData);
                        flowModel.setStepJSON(newJSON);
                    }
                } else if ((flowModel.getGraphicalRepresentation().equalsIgnoreCase("decision")) || (flowModel.getGraphicalRepresentation().equalsIgnoreCase("condition"))) {
                    json = FlowChartStepTemplateMigration.DECISION_STEP_JSON;
                    newJSON = updateJSON(json, flowModel, x, y, new HashMap<>(),new HashMap<>());
                    flowModel.setStepJSON(newJSON);
                }else if(flowModel.getGraphicalRepresentation().equalsIgnoreCase("end") || flowModel.getGraphicalRepresentation().equalsIgnoreCase("stop")) {
                	 json = FlowChartStepTemplateMigration.STOP_STEP_JSON;
                     newJSON = updateJSON(json, flowModel, x, y, new HashMap<>(),new HashMap<>());
                     flowModel.setStepJSON(newJSON);
                }
                x = x + 50;
                y = y + 50;
                stepJSON.add(flowModel);
            }
        } catch (Exception ex) {
        	testDAO.insertStatus("Failure", flowChartDefID);
            ex.printStackTrace();
            return null;
            }
        return stepJSON;
    }
    
    private String updateJSON(String json,FlowChartStepModel flowModel ,int x, int y, Map<String, Object> taskModel,Map<String,String> toolData){
        try {
        	String width ="190";
        	String height = "100";
        	if((flowModel.getStepName().length() > 50) && (flowModel.getStepName().length()<80)) {
        		width = "220";
        		height="140";
        	}if(flowModel.getStepName().length() > 80) {
        		width = "250";
        		height="180";
        	}
            String[] step = splitStepNameByWord(flowModel.getStepName());
            String newStep = "";
            String newTool = "";
            for (int i = 0; i < step.length; i++) {
                newStep += step[i] + "\\\\n";
            }
            if (!taskModel.isEmpty() && taskModel.size()>0) {
                String taskID = String.valueOf(taskModel.get("TaskID"));
                String taskName = String.valueOf(taskModel.get("Task"));
                String toolID = String.valueOf(toolData.get("ToolID"));
                String toolName = String.valueOf(toolData.get("ToolName"));
                String[] tools = splitStepNameByWord(toolName);
                for (String tool1 : tools) {
                    newTool += tool1 + "\\\\n";
                }
                json = json.replaceAll("%STEP_NAME%", newStep + "\\\\nTool Name:" + newTool);
                json = json.replaceAll("%STEP_VALUE%", flowModel.getStepName());
                json = json.replaceAll("%POSITION_X%", String.valueOf(x));
                json = json.replaceAll("%POSITION_Y%", String.valueOf(y));
                json = json.replaceAll("%STEP_ID%", String.valueOf(flowModel.getSubActivityFlowChartStepID()));
                json = json.replaceAll("%TASK_ID%", String.valueOf(taskID));
                json = json.replaceAll("%TASK_NAME%", taskName);
                json = json.replaceAll("%EXECUTION_TYPE%", flowModel.getExecutionType());
                json = json.replaceAll("%TOOL_ID%", String.valueOf(toolID));
                json = json.replaceAll("%TOOL_NAME%", String.valueOf(toolName));
                json = json.replaceAll("%RPATOOL_ID%", String.valueOf(0));
                json = json.replaceAll("%ACTION%", String.valueOf(taskID)+"@"+taskName);
				json = json.replaceAll("%TASK_DATA%", String.valueOf(taskID)+"@"+taskName); 
				json = json.replaceAll("%TOOL_DATA%", String.valueOf(toolID)+"@"+String.valueOf(toolName));
                json = json.replaceAll("%RPA_DATA%", String.valueOf(0)+"@"+String.valueOf(0));
                json = json.replaceAll("%RESP%", flowModel.getResponsible());
                json = json.replaceAll("%WIDTH%", width);
                json = json.replaceAll("%HEIGHT%", height);
            } else {
                json = json.replaceAll("%STEP_NAME%", newStep);
                json = json.replaceAll("%POSITION_X%", String.valueOf(x));
                json = json.replaceAll("%POSITION_Y%", String.valueOf(y));
                json = json.replaceAll("%STEP_ID%", String.valueOf(flowModel.getSubActivityFlowChartStepID()));
                json = json.replaceAll("%WIDTH%", width);
                json = json.replaceAll("%HEIGHT%", height);
            }
        } catch (Exception ex) {
            LOG.info("Error While Creating WorkFlow JSON:Tool/Task Not Mapped" + ex.getMessage());
            ex.printStackTrace();
        }
        return json;
    }
    

    private List<FlowChartDependencyModel> generateLink(List<FlowChartDependencyModel> dependencyModel) {
         String jsonLink = "";
         List<FlowChartDependencyModel> linkModel = new ArrayList<>();
         for(FlowChartDependencyModel dependency : dependencyModel) {
        	 Random rand = new Random(); 
        	 int linkID = rand.nextInt(10000);
             if ("".equals(dependency.getLinkText()) || dependency.getLinkText() == null) {
                 jsonLink = FlowChartStepTemplateMigration.APP_LINK_JSON;
                 jsonLink = jsonLink.replaceAll("%SRC_STEP_ID%", "" + dependency.getSrcFlowChartStepID());
                 jsonLink = jsonLink.replaceAll("%TAR_STEP_ID%", "" + dependency.getDestFlowChartStepID());
                 jsonLink = jsonLink.replaceAll("%LINK_ID%", "l_" + linkID);
             } else {
                 jsonLink = FlowChartStepTemplateMigration.APP_LINK_JSON_WITH_LABLE;
                 jsonLink = jsonLink.replaceAll("%SRC_STEP_ID%", "" + dependency.getSrcFlowChartStepID());
                 jsonLink = jsonLink.replaceAll("%TAR_STEP_ID%", "" + dependency.getDestFlowChartStepID());
                 jsonLink = jsonLink.replaceAll("%LINK_ID%", "l_" + linkID);
                 jsonLink = jsonLink.replaceAll("%LINK_TEXT%", dependency.getLinkText());
             }
             dependency.setLinkJson(jsonLink);
             linkModel.add(dependency);
        }
        return linkModel;
    }
    
    public String createFinalJSON(List<FlowChartStepModel> lstFlowChartStepModel,List<FlowChartDependencyModel> lstFlowChartDepModel) {
	        String finalJSON =  "{\n" +
				                "    \"cells\": [ \n";
	        finalJSON = lstFlowChartStepModel.stream().map((stepModel) -> stepModel.getStepJSON() + ",").reduce(finalJSON, String::concat);
	        finalJSON = lstFlowChartDepModel.stream().map((stepModel) -> stepModel.getLinkJson() + ",").reduce(finalJSON, String::concat);
	        finalJSON = finalJSON.substring(0, finalJSON.length() - 1);
	        finalJSON +="  ]\n" +
				          "}";
        return finalJSON;
    }
    
    public String[] splitStepNameByWord(String stepName){
        String[] stepValue = new String[10];
        int spaceCount = 0;
        int lastIndex = 0;
        String[] stringSplitted = new String[100];
        int stringLength = 0;
        for (int i = 0; i < stepName.length(); i++) {
            if (stepName.charAt(i) == ' ') {  
                spaceCount++;
            }
            if (spaceCount == 4) {  
                stringSplitted[stringLength++] = stepName.substring(lastIndex, i);
                lastIndex = i;
                spaceCount = 0;

            }
        }
        stringSplitted[stringLength++] = stepName.substring(lastIndex, stepName.length()+1 - 1);
        for (int i = 0; i < stringSplitted.length; i++) {
            if (stringSplitted[i] != null && stringSplitted.length > 0) {
                stepValue[i] =  stringSplitted[i];
            }
        }
        String[] newStepName = Arrays.stream(stepValue).filter(Objects::nonNull).toArray(String[]::new);
          return newStepName;
    }

	public String viewWorkFlow(int flowChartDefID) {
				return this.testDAO.viewWorkFlow(flowChartDefID);
	}
	

	public Map<String, Object> getWorkFlowInformation(int flowChartDefID) {
		
		return testDAO.getWorkFlowInformation(flowChartDefID);
	}



	public void generateNewJSONFromFile(MultipartFile file, String flowChartDefID) {
		try {
			upload(file, flowChartDefID);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	private void upload(MultipartFile file,String flowChartDefID)  throws Exception{
        String fileName = file.getName();
        fileName = AppUtil.getFileNameWithTimestamp(fileName);
        LOG.info("File Upload Start");
        this.uploadFile(file, flowChartDefID, fileName);
        String filePath = appService.getConfigUploadFilePath() + "/" + flowChartDefID + "/" + fileName;
        String csvFilePath = appService.getConfigUploadFilePath() + "/" + flowChartDefID + "/" + AppUtil.getFileNameWithTimestamp("flowchart") ;
        AppUtil.convertExcelToCSV(filePath, csvFilePath);
        appService.CsvBulkUploadNewGenWorkFlow(csvFilePath, "FlowChartCreationTmp_MIGRATION", fileName);
        try {
        	uploadExcelTest(fileName,flowChartDefID);
        } catch (Exception e) {
            throw e;
        } finally {
           appService.dropTable(fileName);
        }
}
	
	public void uploadFile(MultipartFile file, String relativeFilePath, String fileName) throws Exception {
        String directory = appService.getConfigUploadFilePath() + "/"  + relativeFilePath;
        File df = new File(directory);
        if (!df.exists()) {
            df.mkdirs();
        }

        if (file != null && !file.isEmpty()) {
//        if (file != null) {
            try {
                byte[] bytes = file.getBytes();
//                byte[] bytes = new byte[(int) file.length()];
                BufferedOutputStream stream = new BufferedOutputStream(new FileOutputStream(new File(appService.getConfigUploadFilePath() + "/" + relativeFilePath + "/" + fileName)));
                stream.write(bytes);
                stream.close();
            } catch (Exception e) {
                throw e;
            }
        }
    }
	
	private void uploadExcelTest(String fileName, String flowChartDefID){
        try {
           this.testDAO.uploadExcel(fileName,Integer.parseInt(flowChartDefID));
                List<FlowChartStepModel> flowChartStepModel = this.testDAO.getFlowChartStepDetails(Integer.parseInt(flowChartDefID));
                List<FlowChartStepModel> stepJSON = generateJSON(flowChartStepModel,Integer.parseInt(flowChartDefID));
                if(stepJSON!=null) {
	                List<FlowChartDependencyModel> dependencyModel = this.testDAO.getDependencyStep(Integer.parseInt(flowChartDefID));
	                List<FlowChartDependencyModel> linkJSON = generateLink(dependencyModel);
	                String jsonData = createFinalJSON(stepJSON,linkJSON);
	                testDAO.updateFlowChartJsonDATA(Integer.parseInt(flowChartDefID),jsonData);
//	                System.out.println("Final JSON:"+jsonData);
	                testDAO.insertStatus("Success",Integer.parseInt(flowChartDefID));
	                deleteMigrationTableData(Integer.parseInt(flowChartDefID));
                }
        } catch (Exception ex) {
        	testDAO.insertStatus("Failure", Integer.parseInt(flowChartDefID));
        	deleteMigrationTableData(Integer.parseInt(flowChartDefID));
           ex.printStackTrace();
        }
}

}
