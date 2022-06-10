using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;
using Microsoft.AspNet.SignalR;
using System.Data;
using Microsoft.AspNet.SignalR.Hubs;
using ISF_WEB.ServerPush.Model;
using Newtonsoft.Json;
using ISF_WEB.Util;
using ISF_WEB.Models;
using System.IO;
using System.Text;
using Microsoft.AspNet.Identity;
using ISF_WEB.ServerPush.SignalServer;
using Microsoft.Ajax.Utilities;

namespace ISF_WEB.ServerPush
{
	[HubName("serverPush")]
	public class ServerPushHub : Hub
	{
		//private readonly IsfWoData _isfData;
		//private readonly AutoSenseNotifications _autoSenseNotifications;


		//public ServerPushHub() :
		//	this(IsfWoData.Instance, AutoSenseNotifications.Instance)
		//{

		//}

		//public ServerPushHub(IsfWoData isfData, AutoSenseNotifications autoSenseNotifications)
		//{
		//	_isfData = isfData;
		//	_autoSenseNotifications = autoSenseNotifications;
		//}


		//public void makeConnection(string signumId, string connectionId)
		//{
		//	CreateLogs createLogs = CreateLogs.GetInstance(signumId);
		//	createLogs.WriteSignalRInfoLogs("Hub method makeConnection is called");
		//	createLogs.WriteSignalRInfoLogs(string.Format("UI connection id is {0}", connectionId));
		//	logSignalRAcknowledgement(signumId, connectionId, "makeConnection", ConstantKeywords.C_UI);
		//	_isfData.MakeConnection(signumId, connectionId);
		//}

		//public void updateFloatingWindow(string signum, string connectionId)
		//{
		//	CreateLogs createLogs = CreateLogs.GetInstance(signum);
		//	createLogs.WriteSignalRInfoLogs("Hub method updateFloatingWindow is called");
		//	_isfData.UpdateAllFloatingClientByConnectionId(signum, false);

		//}

		//public void refreshFloatingWindowOnSensingStart(string signum)
		//{
		//	_isfData.UpdateFloatingWindowOnRuleSesningStart(signum);
		//}

		//public void refreshFloatingWindowOnSensingStop(string signum)
		//{
		//	_isfData.UpdateFloatingWindowOnRuleSesningStop(signum);
		//}


		//public void logoutFloatingWindow(string signum, string connectionId)
		//{
		//	_isfData.UpdateAllFloatingClientByConnectionId(signum, true);
		//	removeAllConnections(signum);
		//}

		//public void removeConnection(string signumId, string connectionId)
		//{
		//	_isfData.RemoveConnection(signumId, connectionId);
		//}

		//public void removeAllConnections(string signumId)
		//{
		//	_isfData.RemoveAllConnection(signumId);
		//}

		//public void MakeDesktopConnection(string signumId, string connectionId)
		//{
		//	CreateLogs createLogs = CreateLogs.GetInstance(signumId);
		//	createLogs.WriteSignalRInfoLogs("Hub method updateFloatingWindow is called");
		//	createLogs.WriteSignalRInfoLogs(string.Format("Connection id of Floating Window is {0}", connectionId));
		//	logSignalRAcknowledgement(signumId, connectionId, "MakeDesktopConnection", ConstantKeywords.C_FloatingWindow);
		//	_isfData.MakeDesktopConnection(signumId, connectionId);

		//}

		//public void removeDesktopConnection(string signumId, string connectionId)
		//{
		//	_isfData.RemoveDesktopConnection(signumId, connectionId);
		//}

		//public void UpdateClient(List<WoAutoTaskInfo> listWoAutoRunningTaskStatus)
		//{
		//	_isfData.CheckInProgressTaskStatus(listWoAutoRunningTaskStatus);

		//}

		//public void UpdateAuditDetailUserCommentClient(List<AuditDetailUserCommentWoDetail> listAuditDetailUserCommentWoDetail)
		//{
		//	_isfData.CheckAuditDetailUserCommentConnection(listAuditDetailUserCommentWoDetail);
		//}

		//public void UpdateDesktopClient(string changeType)
		//{

		//	_isfData.UpdateAllDesktopClient(changeType);
		//}

		//public string TestSignalR(WoAutoTaskInfo task)
		//{
		//	//WoAutoTaskInfo obj = new WoAutoTaskInfo();
		//	//obj.Signum = "ekarmuj";
		//	//obj.WoId = 12829072;
		//	//obj.BookingId = 125092983;
		//	//obj.FlowChartStepId = "6a4ffb4f-5d5a-4b9d-9a06-829b97f7b7d1";
		//	//obj.Hour = "13.2665 minutes";
		//	//obj.Status = "COMPLETED";
		//	//obj.Reason = "SUCCESS";
		//	//obj.OutputLink = "12829072_ekarmuj_869_09Jan2020141823.zip";
		//	//obj.IconsOnNextStep = true;
		//	//obj.FlowChartDefID = 179968;
		//	//obj.StartDate = null;
		//	//obj.EndDate = null;

		//	var tasks = new List<WoAutoTaskInfo>();
		//	tasks.Add(task);

		//	UpdateClient(tasks);
		//	return string.Format("Message recieved- {0}", tasks);

		//}

		//public string TestSignalRDual(string msg)
		//{
		//	return string.Format("Message recieved- {0}", msg);

		//}

		//public string UpdateDeliveryExecutionClientWithJsonData(string jsonWorkOrderRunningTaskData)
		//{
		//	WoAutoTaskInfo taskInfo = new WoAutoTaskInfo();

		//	if (JsonHelper.Instance.IsValidJson(jsonWorkOrderRunningTaskData) == true)
		//	{
		//		taskInfo = JsonConvert.DeserializeObject<WoAutoTaskInfo>(jsonWorkOrderRunningTaskData);
		//		List<WoAutoTaskInfo> listWoAutoRunningTaskStatus = new List<WoAutoTaskInfo>();
		//		listWoAutoRunningTaskStatus.Add(taskInfo);
		//		_isfData.CheckInProgressTaskStatus(listWoAutoRunningTaskStatus);
		//		logSignalRAcknowledgement(taskInfo.Signum, string.Empty, "UpdateDeliveryExecutionClientWithJsonData", ConstantKeywords.C_EventPublisher);

		//		return ConstantKeywords.C_SUCCESS;
		//	}

		//	return ConstantKeywords.C_FAIL;

		//}

		//public string UpdateAdhocTaskClientWithJsonData(string jsonAdhocRunningTaskData)
		//{
		//	AdhocTaskStatus adhocTaskInfo = new AdhocTaskStatus();

		//	if (JsonHelper.Instance.IsValidJson(jsonAdhocRunningTaskData) == true)
		//	{
		//		adhocTaskInfo = JsonConvert.DeserializeObject<AdhocTaskStatus>(jsonAdhocRunningTaskData);
		//		List<AdhocTaskStatus> listAdhocTaskStatus = new List<AdhocTaskStatus>();
		//		listAdhocTaskStatus.Add(adhocTaskInfo);
		//		_isfData.CheckAdhocTaskStatus(listAdhocTaskStatus);
		//		logSignalRAcknowledgement(adhocTaskInfo.SignumId, string.Empty, "UpdateAdhocTaskClientWithJsonData", ConstantKeywords.C_EventPublisher);

		//		return ConstantKeywords.C_SUCCESS;
		//	}

		//	return ConstantKeywords.C_FAIL;
		//}

		//public void updateWebFromWebClient(string signum, string connectionId)
		//{
		//	if (!string.IsNullOrEmpty(signum) && !string.IsNullOrEmpty(connectionId))
		//	{
		//		CreateLogs createLogs = CreateLogs.GetInstance(signum);
		//		createLogs.WriteInformationLogs("method UpdateWebUI is called.");
		//		_isfData.UpdateWebUI(signum, connectionId);
		//		createLogs.WriteInformationLogs("method UpdateWebUI : execution completed.");
		//	}
		//}

		//public void updateWebFromWebClient(dynamic stepDetailsData, string signum, string connectionId)
		//{
		//	if (!string.IsNullOrEmpty(signum) && !string.IsNullOrEmpty(connectionId))
		//	{
		//		CreateLogs createLogs = CreateLogs.GetInstance(signum);
		//		createLogs.WriteInformationLogs("method UpdateWebUI is called.");
		//		_isfData.UpdateWebUI(stepDetailsData, signum, connectionId);
		//		createLogs.WriteInformationLogs("method UpdateWebUI : execution completed.");
		//		logSignalRAcknowledgement(signum, connectionId, "updateWebFromWebClient", ConstantKeywords.C_UI);
		//	}
		//}

		//private void UpdateWebUIAfterAutoScanner(StepDetailsData stepDetailsData, string signum)
		//{
		//	if (!string.IsNullOrEmpty(signum))
		//	{
		//		CreateLogs createLogs = CreateLogs.GetInstance(signum);
		//		createLogs.WriteInformationLogs("method UpdateWebUIAfterAutoScanner is called.");
		//		_isfData.UpdateWebUIAfterAutoScan(stepDetailsData, signum);
		//		createLogs.WriteInformationLogs("method UpdateWebUIAfterAutoScanner : execution completed.");
		//	}
		//}

		//public string UpdateWorkOrderCompletionToClient(string jsonWorkOrderCompletionData)
		//{
		//	WorkOrderCompletionData workOrderCompletionData = new WorkOrderCompletionData();

		//	if (JsonHelper.Instance.IsValidJson(jsonWorkOrderCompletionData) == true)
		//	{
		//		workOrderCompletionData = JsonConvert.DeserializeObject<WorkOrderCompletionData>(jsonWorkOrderCompletionData);
		//		_isfData.UpdateClientByConnectionIdForWorkOrderCompletion(workOrderCompletionData);
		//		logSignalRAcknowledgement(workOrderCompletionData.Signum, string.Empty, "UpdateWorkOrderCompletionToClient", ConstantKeywords.C_FloatingWindow);
		//	}
		//	else
		//	{
		//		return ConstantKeywords.C_FAIL_MSG;
		//	}
		//	return ConstantKeywords.C_SUCCESS;
		//}

		//public void updateAdhocTimerOnAllTabs(dynamic data, string signum, string connectionId, bool IsStarted)
		//{
		//	if (!string.IsNullOrEmpty(signum) && !string.IsNullOrEmpty(connectionId))
		//	{
		//		CreateLogs createLogs = CreateLogs.GetInstance(signum);
		//		createLogs.WriteInformationLogs("method UpdateAdhocTimerOnAllTabs is called.");
		//		_isfData.UpdateAdhocTimerOnMultipleTabs(data, signum, connectionId, IsStarted);
		//		createLogs.WriteInformationLogs("method UpdateAdhocTimerOnAllTabs : execution completed.");
		//		logSignalRAcknowledgement(signum, connectionId, "updateAdhocTimerOnAllTabs", ConstantKeywords.C_UI);
		//	}
		//}

		//public void pauseAllWorkOrders(string signum, string connectionId)
		//{
		//	if (!string.IsNullOrEmpty(signum) && !string.IsNullOrEmpty(connectionId))
		//	{
		//		CreateLogs createLogs = CreateLogs.GetInstance(signum);
		//		createLogs.WriteInformationLogs("method PauseAllWorkOrders is called.");
		//		_isfData.PauseAllWorkOrders(signum, connectionId);
		//		createLogs.WriteInformationLogs("method PauseAllWorkOrders : execution completed.");
		//		logSignalRAcknowledgement(signum, connectionId, "pauseAllWorkOrders", ConstantKeywords.C_UI);
		//	}
		//}

		//// Hub method to notify wo details for Auto Sense enabled task

		//public void autoSenseEnabledWorkOrderDetails(string autoSenseWoDetail)
		//{
		//	AutoSenseWorkOrderData workOrderData = new AutoSenseWorkOrderData();
		//	if (JsonHelper.Instance.IsValidJson(autoSenseWoDetail) == true)
		//	{
		//		workOrderData = JsonConvert.DeserializeObject<AutoSenseWorkOrderData>(autoSenseWoDetail);
		//		CreateLogs CreateLogsObj = CreateLogs.GetInstance(workOrderData.signumID);
		//		CreateLogsObj.WriteSignalRInfoLogs("Hub method autoSenseEnabledWorkOrderDetails is called.");
		//		_isfData.AutoSenseWoDetailForFloatingWindowClient(workOrderData);
		//		logSignalRAcknowledgement(workOrderData.signumID, string.Empty, "autoSenseEnabledWorkOrderDetails", ConstantKeywords.C_UI);
		//	}


		//}

		//private void NotifyFloatingWindow(List<WoAutoTaskInfo> TaskDetails)
		//{
		//	if (TaskDetails != null && TaskDetails.Count > 0)
		//	{
		//		CreateLogs CreateLogsObj = CreateLogs.GetInstance(TaskDetails[0].Signum);
		//		CreateLogsObj.WriteSignalRInfoLogs("Hub Method NotifyFloatingWindow is called");

		//		AutoSenseWorkOrderData workOrderData = new AutoSenseWorkOrderData();
		//		workOrderData.flowchartDefID = TaskDetails[0].FlowChartDefID.ToString();
		//		workOrderData.signumID = TaskDetails[0].Signum;
		//		workOrderData.stepID = TaskDetails[0].FlowChartStepId;
		//		workOrderData.woID = TaskDetails[0].WoId.ToString();
		//		workOrderData.action = TaskDetails[0].actionPerfomed;
		//		workOrderData.source = ConstantKeywords.C_Scanner;
		//		workOrderData.overrideAction = TaskDetails[0].overrideAction;
		//		workOrderData.startRule = TaskDetails[0].startRule == null ? null : TaskDetails[0].startRule.ToString();
		//		workOrderData.stopRule = TaskDetails[0].stopRule == null ? null : TaskDetails[0].stopRule.ToString();
		//		workOrderData.taskID = TaskDetails[0].taskId.ToString();

		//		var autoSenseWoDetail = JsonConvert.SerializeObject(workOrderData);

		//		CreateLogsObj.WriteSignalRInfoLogs(string.Format("work order data for autoSenseEnabledWorkOrderDetails {0}", autoSenseWoDetail));
		//		autoSenseEnabledWorkOrderDetails(autoSenseWoDetail);
		//	}

		//}

		//public string UpdateWorkOrderStatusOnWebAndFW(string jsonWorkOrderRunningTaskData)
		//{
		//	try
		//	{

		//		WoAutoTaskInfo taskInfo = new WoAutoTaskInfo();

		//		if (JsonHelper.Instance.IsValidJson(jsonWorkOrderRunningTaskData) == true)
		//		{
		//			taskInfo = JsonConvert.DeserializeObject<WoAutoTaskInfo>(jsonWorkOrderRunningTaskData);
		//			List<WoAutoTaskInfo> listWoAutoRunningTaskStatus = new List<WoAutoTaskInfo>();
		//			CreateLogs CreateLogsObj = CreateLogs.GetInstance(taskInfo.Signum);
		//			CreateLogsObj.WriteInformationLogs("Method UpdateWorkOrderStatusOnWebAndFW is called");
		//			logSignalRAcknowledgement(taskInfo.Signum, string.Empty, "UpdateWorkOrderStatusOnWebAndFW", ConstantKeywords.C_EventPublisher);

		//			if (taskInfo != null)
		//			{
		//				StepDetailsData stepDetail = new StepDetailsData();
		//				stepDetail.flowChartType = taskInfo.flowChartType;
		//				stepDetail.hour = taskInfo.Hour;
		//				stepDetail.status = taskInfo.Status;
		//				stepDetail.stepID = taskInfo.FlowChartStepId;
		//				stepDetail.wOID = taskInfo.WoId.ToString();
		//				if (taskInfo.Status.Equals(BookingStatus.STARTED.ToString(), StringComparison.OrdinalIgnoreCase))
		//				{
		//					stepDetail.bookingType = ConstantKeywords.C_BookingType;
		//				}
		//				UpdateWebUIAfterAutoScanner(stepDetail, taskInfo.Signum);

		//				listWoAutoRunningTaskStatus.Add(taskInfo);
		//				NotifyFloatingWindow(listWoAutoRunningTaskStatus);

		//				return ConstantKeywords.C_SUCCESS;
		//			}

		//		}
		//	}
		//	catch (Exception ex)
		//	{
		//		CreateLogs CreateLogsObj = CreateLogs.GetInstance(string.Empty);
		//		CreateLogsObj.WriteExceptionLogs(ex);
		//	}
		//	return ConstantKeywords.C_FAIL;
		//}

		//public void startAutoScannerProcessForWoDetails(List<AutoSenseWorkOrderData> autoSenseWoDetailList)
		//{
		//	CreateLogs createLogs = null;
		//	try
		//	{

		//		if (autoSenseWoDetailList != null && autoSenseWoDetailList?.Count > 0)
		//		{
		//			createLogs = CreateLogs.GetInstance(autoSenseWoDetailList?[0].signumID);
		//			createLogs.WriteSignalRInfoLogs(string.Format("Hub method startAutoScannerProcessForWoDetails is called withautoSenseWoDetailList:{0}", autoSenseWoDetailList));
		//			_isfData.StartAutoScannerProcessForFloatingWindowClient(autoSenseWoDetailList);
		//			logSignalRAcknowledgement(autoSenseWoDetailList?[0].signumID, string.Empty, "startAutoScannerProcessForWoDetails", ConstantKeywords.C_UI);

		//		}

		//	}
		//	catch (Exception ex)
		//	{
		//		createLogs = CreateLogs.GetInstance(string.Empty);
		//		createLogs.WriteExceptionLogs(ex);
		//	}
		//}

		//// Hub method to notify wo details list for Auto Sense enabled task for mass transfer
		//public void autoSenseEnabledWorkOrdersList(List<AutoSenseWorkOrderData> autoSenseWoDetailList)
		//{
		//	CreateLogs createLogsObj = null;
		//	try
		//	{
		//		if (autoSenseWoDetailList != null && autoSenseWoDetailList?.Count > 0)
		//		{
		//			createLogsObj = CreateLogs.GetInstance(autoSenseWoDetailList?[0].signumID);
		//			createLogsObj.WriteSignalRInfoLogs(string.Format("Hub method autoSenseEnabledWorkOrdersList is called. with autoSenseWoDetailList:{0}", autoSenseWoDetailList));
		//			_isfData.AutoSenseWoDetailListForFloatingWindowClient(autoSenseWoDetailList);
		//			logSignalRAcknowledgement(autoSenseWoDetailList?[0].signumID, string.Empty, "autoSenseEnabledWorkOrdersList", ConstantKeywords.C_UI);


		//		}
		//		else
		//		{
		//			logSignalRAcknowledgement(string.Empty, string.Empty, "autoSenseEnabledWorkOrdersList", ConstantKeywords.C_UI);

		//		}

		//	}
		//	catch (Exception ex)
		//	{
		//		createLogsObj = CreateLogs.GetInstance(string.Empty);
		//		createLogsObj.WriteExceptionLogs(ex);
		//	}
		//}

		//public void logSignalRAcknowledgement(string signum, string connectionId, string methodName, string client)
		//{
		//	CreateLogs createLogs = CreateLogs.GetInstance(signum);
		//	createLogs.WriteSignalRMessageTransactionLogs(true, signum, methodName, connectionId, client);
		//}

		//public void setDecisionValue(string decisionValueDetails)
		//{
		//	string signum = _autoSenseNotifications.SetDecisionValue(decisionValueDetails);
		//	logSignalRAcknowledgement(signum, string.Empty, "setDecisionValue", ConstantKeywords.C_EventPublisher);

		//}
	}
}