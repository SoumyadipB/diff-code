var serverpushhubproxy;

var ClientMethodList = [
    "updateInprogressTasks",
    "updateInprogressTaskPanelUI",
    "refreshWebUIAfterAutoScan",
    "updateMyWorkAuditDetailUserComment",
    "ReceiveMessage",
    "updateadhoctasks",
    "StartAdhocTimerOnWebUI",
    "updateForWorkOrderCompletion",
    "updateForPauseAllTasks",
    "setDecisionValue",
];

 function InitiateSignalRConnection() {    
    
    if (startServerPush) {

        if (typeof (connection) == "undefined" || connection.state == signalR.HubConnectionState.Disconnected) {
            createConnection();
        }
        else if (connection.state == signalR.HubConnectionState.Connected) {
            logMessage('SignalR is connected(already), connection ID=' + connection.connectionId);
            return true;
        }

    };
    
}


function UpdateFloatingWindowServeMethods(IsLogout, IsCalledForFWUpdate, autoSenseInputData) {

    if (IsLogout == true) {
        connection.invoke('LogoutFloatingWindow', signumGlobal);
        logMessage('Invoked Hub method : LogoutFloatingWindow');
    }
    else if (IsCalledForFWUpdate == true) {

        if (autoSenseInputData != undefined && autoSenseInputData != null && autoSenseInputData != "" && Object.keys(autoSenseInputData).length !== 0) {
            var autosenseStepDetails = JSON.stringify(autoSenseInputData);
            connection.invoke('AutoSenseEnabledWorkOrderDetails', autosenseStepDetails);
            logMessage('Invoked Hub method : AutoSenseEnabledWorkOrderDetails');
        }
        else {
            connection.invoke('UpdateFloatingWindow', signumGlobal);
            logMessage('Invoked Hub method : UpdateFloatingWindow');
        }
    }
}

function UpdateFloatingWindowServeMethodsforMassSuspend(IsLogout, IsCalledForFWUpdate, autoSenseInputDataList) {

    if (IsLogout == true) {
        connection.invoke('LogoutFloatingWindow', signumGlobal);
        logMessage('Invoked Hub method : LogoutFloatingWindow');
    }
    else if (IsCalledForFWUpdate == true) {

        if (autoSenseInputDataList.length > 0) {
            connection.invoke('AutoSenseEnabledWorkOrdersList', JSON.stringify(autoSenseInputDataList));
            logMessage('Invoked Hub method : AutoSenseEnabledWorkOrdersList');

        }
        else {

            connection.invoke('UpdateFloatingWindow', signumGlobal);
            logMessage('Invoked Hub method : UpdateFloatingWindow');
        }
    }
}

function UpdateAdhocTimerOnMultipleTabs(data, IsAdhocStarted) {
    var obj = new Object();
    obj.Data = data;
    obj.Signum = signumGlobal;
    obj.ConnectionId = connection.connectionId;// need to check with Rajni
    obj.IsStarted = IsAdhocStarted
    connection.invoke('UpdateAdhocTimerOnAllTabs', JSON.stringify(obj));
    logMessage('Invoked Hub method : UpdateAdhocTimerOnAllTabs');

}

function UpdateWebFromWebClient(stepDetailsData, wOID) {
    stepDetailsData.wOID = wOID;
    var obj = new Object();
    obj.StepDetailsData = stepDetailsData;
    obj.Signum = signumGlobal;
    obj.ConnectionId = connection.connectionId; //need to check with Rajni
    connection.invoke('UpdateWebFromWebClient', JSON.stringify(obj));
    logMessage('Invoked Hub method : UpdateWebFromWebClient');
}

function PauseAllWorkOrders() {
    let obj = new Object();
    obj.ConnectionId = connection.connectionId;
    obj.Signum = signumGlobal;
    connection.invoke('PauseAllWorkOrders', JSON.stringify(obj));
    logMessage('Invoked Hub method : PauseAllWorkOrders');
}

function NotifyClientOnWorkOrderStatusChange(stepDetailsData, wOID, autoSenseInputData, isUpdateFW) {
    if (InitiateSignalRConnection() == true) {
        if (isUpdateFW == true) {
            UpdateFloatingWindowServeMethods(false, true, autoSenseInputData);
        }
        UpdateWebFromWebClient(stepDetailsData, wOID);

    }
}

function NotifyClientOnWorkOrderSuspended(autoSenseInputData, isUpdateFW) {
    if (InitiateSignalRConnection() == true) {
        if (isUpdateFW == true) {
            UpdateFloatingWindowServeMethods(false, true, autoSenseInputData);
        }
    }
}

function NotifyClientOnWorkOrderMassSuspended(autoSenseInputDataList, isUpdateFW) {
    if (InitiateSignalRConnection() == true) {
        if (isUpdateFW == true) {
            UpdateFloatingWindowServeMethodsforMassSuspend(false, true, autoSenseInputDataList);
        }
    }
}

function NotifyClientOnPauseAllWO(autoSenseInputDataList) {
    if (InitiateSignalRConnection() == true) {
        UpdateFloatingWindowServeMethodsforMassSuspend(false, true, autoSenseInputDataList);
        PauseAllWorkOrders();
    }
}

function ConsoleWriter(message, data) {
    if (message != undefined && message != null && data != undefined && data != null) {
        console.log(message);
        console.log(data);
    }
}

function PrepareAutoSenseInputDataForClient(data, InProgressTasks, steptype) {
    var autoSenseInputData = new Object;
    if (steptype === "Automatic") {
        for (var i in InProgressTasks) {
            if (InProgressTasks[i].WoId === data.WoId && InProgressTasks[i].FlowChartStepID === data.FlowChartStepId && InProgressTasks[i].FlowChartDefID === data.FlowChartDefID && InProgressTasks[i].ExecutionType === steptype) {
                if (InProgressTasks[i].NextStepExecutionType === "Manual" && (InProgressTasks[i].NextStepStartRule === true || InProgressTasks[i].NextStepStopRule === true)) {
                    autoSenseInputData.WoId = data.WoId;
                    autoSenseInputData.FlowChartDefID = data.FlowChartDefID;
                    autoSenseInputData.stepID = data.FlowChartStepId;
                    autoSenseInputData.source = "UI";
                    autoSenseInputData.signumID = signumGlobal;
                    autoSenseInputData.overrideAction = "AutomaticSTOPPED";
                    autoSenseInputData.action = "";
                    autoSenseInputData.taskID = InProgressTasks[i].TaskID;
                    autoSenseInputData.startRule = InProgressTasks[i].StartRule;
                    autoSenseInputData.stopRule = InProgressTasks[i].StopRule;
                }
            }
        }
        return autoSenseInputData;

    }
}

function startScanner(autosenseStepData) {
    if (InitiateSignalRConnection() == true) {
        if (autosenseStepData != undefined && autosenseStepData != null && autosenseStepData != "" && Object.keys(autosenseStepData).length !== 0) {
            if (autosenseStepData.length > 0) {
                try {
                    connection.invoke('StartAutoScannerProcessForWoDetails', JSON.stringify(autosenseStepData));
                    logMessage('Invoked Hub method : StartAutoScannerProcessForWoDetails');
                }
                catch (err) {
                    console.log('Invocation of startAutoScannerForWorkOrderDetails. Error: ' + err);
                }
            }

        }
    }
}

function NotifyHUBforAcknowledgement(methodName) {
    //below one not found
    serverpushhubproxy.server.logSignalRAcknowledgement(signumGlobal, $.connection.hub.id, methodName, "UI");
}


function RecievedNotificationFromHub(notificationDetails) {
    if (notificationDetails != null && notificationDetails != undefined && notificationDetails != "null") {
        var notificationData = JSON.parse(notificationDetails);
        logMessage('Recieved notification from Hub for method : ' + notificationData.MethodName);
        if (notificationData.MethodName != null && notificationData.MethodName != undefined && notificationData.MethodName != "") {
            if (ClientMethodList.includes(notificationData.MethodName) === true) {
                window[notificationData.MethodName](notificationData.Input);
            }
        }
    }
}


function updateInprogressTaskPanelUI(stepDetailsData) {
    console.log('updateInprogressTaskPanelUI called');
    ConsoleWriter("SignalR notification received : Updating In Progress panel and Flow chart on multiple tabs", stepDetailsData);
    requestInprogressTasks();

    if (stepDetailsData != null && stepDetailsData != undefined && stepDetailsData != "null") {
        stepDetailsData = JSON.parse(stepDetailsData);
        if (stepDetailsData != undefined && stepDetailsData != null) {
            if (document.getElementById('iframe_' + stepDetailsData.wOID) != null && document.getElementById('iframe_' + stepDetailsData.wOID) != undefined && document.getElementById('iframe_' + stepDetailsData.wOID) != "undefined") {
                if (stepDetailsData.status.includes(C_EXECUTION_TYPE_MANUALDISABLED)) {
                    let status = stepDetailsData.status.split('_')[1];
                    window.parent.changeDisabledManualStepColor(status, stepDetailsData.wOID);
                    if (status == C_BOOKING_STATUS_COMPLETED || status == C_BOOKING_STATUS_ONHOLD) {
                        updateFlowchart(stepDetailsData.wOID, status);
                    }
                }
                else {
                    document.getElementById('iframe_' + stepDetailsData.wOID).contentWindow.changeStepColor(stepDetailsData);

                }
            }
            
            handleWOActionButtons(stepDetailsData.wOID, false);
        }
    }
}


function refreshWebUIAfterAutoScan(stepDetailsData) {
    console.log('refreshWebUIAfterAutoScan called');
    ConsoleWriter("SignalR notification received : Updating In Progress panel and Flow chart After Auto Scan", stepDetailsData);
    requestInprogressTasks();

    if (stepDetailsData != null && stepDetailsData != undefined && stepDetailsData != "null") {
        stepDetailsData = JSON.parse(stepDetailsData);
        if (stepDetailsData != undefined && stepDetailsData != null) {
            if (document.getElementById('iframe_' + stepDetailsData.wOID) != null && document.getElementById('iframe_' + stepDetailsData.wOID) != undefined && document.getElementById('iframe_' + stepDetailsData.wOID) != "undefined") {
                document.getElementById('iframe_' + stepDetailsData.wOID).contentWindow.changeStepColor(stepDetailsData);
            }
        }
    }
};


function updateMyWorkAuditDetailUserComment(data) {

    if (data != undefined && data != null && data != "null") {
        console.log('updateMyWorkAuditDetailUserComment called');
        ConsoleWriter("SignalR notification received : updateMyWorkAuditDetailUserComment", data);
        data = JSON.parse(data);
        getCommentsSection("WORK_ORDER_" + data.UniquePageID, '', '', 'WO_LEVEL');
    }
};


function updateadhoctasks(data) {
    console.log('updateadhoctasks called');
    ConsoleWriter("SignalR notification received : Adhoc timer updated", data);

    if (data != undefined && data != null && data != "null") {
        data = JSON.parse(data);
        SendDataForUpdateNonWorkingHourBooking("ACTUAL", true);
    }

}

function StartAdhocTimerOnWebUI(data) {
    console.log('StartAdhocTimerOnWebUI called');
    if (data != null && data != undefined && data != "null") {
        ConsoleWriter("SignalR notification received : Adhoc timer refreshed", data);
        data = JSON.parse(data);
        StartNonWorkingRecordingPopUp(data);
    }
};

function updateForWorkOrderCompletion(data) {
    console.log('updateForWorkOrderCompletion called');
    ConsoleWriter("SignalR notification received : Work order completion", data);
    if (data != null && data != undefined && data != "null") {
        data = JSON.parse(data);
        if (data.IsWorkOrderCompleted == true) {
            location.reload();
        }
    }
};

function updateForPauseAllTasks() {
    console.log('updateForPauseAllTasks called');
    ConsoleWriter("SignalR notification received : updateForPauseAllTasks");
    location.reload();
};

function setDecisionValue(data) {
    if (data != undefined && data != null && data != "null") {
        data = JSON.parse(data);
        console.log('updateInprogressTaskPanelUI called');
        document.getElementById('iframe_' + data.woId).contentWindow.GetElementAttForSettingDecisionValue(data.flowChartStepId, data.decisionValue, data.woId);
    }
}

function logMessage(message) {
    console.log(message);
    window.appInsights.trackTrace({
        message: 'Web Client: ' + message
    })
}

//Function to start connection with signalr service
async function start() {
    try {
        await connection.start();
        console.assert(connection.state === signalR.HubConnectionState.Connected);
        logMessage(`SignalR Connected with connectionId :${connection.connectionId}`);
        
        await connection.invoke('TestSignalRDual', signumGlobal);
        await connection.invoke('MakeConnection', signumGlobal);
        logMessage('Invoked Hub method : MakeConnection');
        return true;

    } catch (err) {
        console.assert(connection.state === signalR.HubConnectionState.Disconnected);
        logMessage("Error in connecting:" + err);

        //Added this condition in case of server timeout issue
        if (connection.state == signalR.HubConnectionState.Disconnected) {
            //setTimeout(() => start(), 5000);
            reconnectOnFail(); 
        }
        return false;
    }
};

function onConnected(connection) {
    console.log('connection started');
    try {
        connection.invoke('TestSignalRDual', signumGlobal);
        connection.invoke('MakeConnection', signumGlobal);
    }
    catch (err) {
        console.assert(connection.state === signalR.HubConnectionState.Disconnected);
        logMessage(err);
        //setTimeout(connection.start(), 5000);
        reconnectOnFail();
    }
}

function onConnectionError(error) {
    if (error && error.message) {
        console.error(error.message);
        logMessage(error.message);
    }

    logMessage('Reconnecting signalR after losing connection');
    reconnectOnFail();
    //start();
}

function bindConnectionMessage(connection) {
    var messageCallback = function (message) {
        console.log("message is " + message);
    };
    connection.on('ReceiveMessage', messageCallback);
    connection.on('ReceiveNotification', RecievedNotificationFromHub);
    connection.onclose(onConnectionError);

    connection.onreconnecting(error => {
        console.assert(connection.state === signalR.HubConnectionState.Reconnecting);
        logMessage(`Connection lost due to error "${error}". Reconnecting.`);
    });

    connection.onreconnected(async connectionId => {
        console.assert(connection.state === signalR.HubConnectionState.Connected);
        logMessage(`Connection reestablished. Connected with connectionId "${connectionId}".`);
        await connection.invoke('TestSignalRDual', signumGlobal);
        await connection.invoke('MakeConnection', signumGlobal);
        logMessage('Invoked Hub method on reconnection: MakeConnection');
    });
}

/* generate token */
var accesstoken = "";
function getAccessToken() {


    $.isf.ajax({
        type: "POST",
        url: UiRootDir + '/Data/signalRTokenDataGet',
        //  data: dataToPost,
        async: false,
        contentType: "application/json; charset=utf-8", // specify the content type
        // dataType: 'JSON',
        success: function (response) {
            console.log(response);
            accesstoken = response;
            //  return response;
        },
        error: function (xhr, status, statusText) {
            console.log("error in Data/signalRTokenDataGet");
            return null;
        }
    });
}

function createConnection() {
    getAccessToken();
    var token = accesstoken;
    console.log("access token is " + token);
    connection = new signalR.HubConnectionBuilder()
        .withUrl(Azure_SignalR_Hub_URL, { accessTokenFactory: () => token })
        .withAutomaticReconnect([0, 5000, 10000, 30000])
        .configureLogging(signalR.LogLevel.Trace)
        .build();
    bindConnectionMessage(connection);
    
    start();
}

function reconnectOnFail() {
   
    var delay = getRandom(5000, 10000);
    console.log('reconnectOnFail:' + delay);
    setTimeout( function () {
         start();
    }, delay);
}

function getRandom(min, max) {
    return Math.floor(Math.random() * (max - min + 1) + min);
}
function updateInprogressTasks(data) {
    let nxtStepType, nxtTaskID, nxtExecType, nxtOutputUpload, nxtCascadeInput, nxtRunOnServer, nxtRPAID, nxtStepID, versionNo, nxtBotType, nxtStepName;
    let currTaskId, proj_ID;
    console.log('updateInprogressTasks called');
    data = JSON.parse(data);
    ConsoleWriter("SignalR notification received: Updating In Progress panel and flow chart", data);
    let steptype = data.ExecutionType;
    //   var InProgressTasks = JSON.parse(localStorage.getItem("AutoSenseData"));
    var InProgressTasks = GetSavedAutoSenseData();
    let autoSenseInputData = PrepareAutoSenseInputDataForClient(data, InProgressTasks, steptype);
    var pageUrl = $(location).attr("href");
    if (pageUrl.substring(pageUrl.lastIndexOf("/") + 1) === "WorkorderAndTask#" || 1 === 1) {

        requestInprogressTasks();        
        if (data.Reason !== null && data.Reason.includes(":")) {
            var reasonStatus = data.Reason.substr(0, data.Reason.indexOf(':')).trim();
            var failureReason = data.Reason.substr(data.Reason.lastIndexOf(':') + 1, data.Reason.length)
            if (reasonStatus.toUpperCase() === 'FAIL') {
                // Refresh Floating Window on Failure of execution of an Automatic step
                //  serverpushhubproxy.server.updateFloatingWindow(signumGlobal, $.connection.hub.id);

               // connection.invoke('UpdateFloatingWindow', signumGlobal);
                //logMessage('Invoked Hub method : UpdateFloatingWindow');

                failureReason = 'Task Execution has been failed for Work order Id - ' + data.WoId + ' -  ' + failureReason;
                pwIsf.alert({ msg: failureReason, type: 'error' });
            }
        }

        console.log('status: ' + data.Status + ' execution type: ' + steptype);
        let stepDetailsData = new Object();

        if (data.Status === 'COMPLETED') {
            if (data.IconsOnNextStep === true) {
                if (Object.keys(autoSenseInputData).length !== 0) {
                    var autosenseStepDetails = JSON.stringify(
                        );
                    // serverpushhubproxy.server.autoSenseEnabledWorkOrderDetails(autosenseStepDetails);
                   // connection.invoke('AutoSenseEnabledWorkOrderDetails', autosenseStepDetails);
                   // logMessage('Invoked Hub method : AutoSenseEnabledWorkOrderDetails');
                }
                else {
                    // Refresh Floating Window on completion of execution of an Automatic step
                    // serverpushhubproxy.server.updateFloatingWindow(signumGlobal, $.connection.hub.id);
                    //connection.invoke('UpdateFloatingWindow', signumGlobal);
                   // logMessage('Invoked Hub method : UpdateFloatingWindow');
                }



                //serverpushhubproxy.server.updateFloatingWindow(signumGlobal, $.connection.hub.id);

                stepDetailsData.status = "COMPLETED";

                //Getting nextStep Details
                $.isf.ajax({
                    type: "GET",
                    async: false,
                    url: service_java_URL + "rpaController/getNextStepData/" + data.FlowChartStepId + "/" + data.FlowChartDefID,
                    success: function (response) {

                        nxtStepType = response[0].nextStepType;
                        nxtCascadeInput = response[0].CascadeInput;
                        currTaskId = response[0].currentStepTaskId;
                        versionNo = response[0].VersionNumber;
                        nxtStepID = response[0].NextStepID;
                        nxtTaskID = response[0].NextTaskID;
                        nxtBotType = response[0].botType;
                        nxtStepName = response[0].NextStepName;
                        nxtRunOnServer = response[0].isRunOnServer;
                        proj_ID = response[0].ProjectID;
                        nxtOutputUpload = response[0].OutputUpload;
                        nxtRPAID = response[0].BOTID;
                        nxtExecType = response[0].NextExecutionType;
                    },
                    error: function (xhr, status, statusText) {
                        pwIsf.alert({ msg: 'Error while getting next step data', type: "error" });
                    },
                });

            }


            //Getting nextStep Details End
            //IF Next Step is CASCADESTEP START
            if (nxtStepType == "ericsson.Automatic" && nxtCascadeInput.toLowerCase() == "yes") {

                let stepDetails = {};
                let stepDetailsToStart = {};
                stepDetails.wOID = data.WoId;
                stepDetails.stepID = nxtStepID;
                stepDetails.taskID = nxtTaskID;
                stepDetails.subActivityFlowChartDefID = data.FlowChartDefID;
                stepDetails.signumID = signumGlobal;
                stepDetails.decisionValue = '';
                stepDetails.executionType = nxtExecType;
                stepDetails.botPlatform = "server";
                stepDetails.stepName = nxtStepName;
                stepDetails.type = nxtBotType;
                stepDetails.isCallingFromFlowchart = false;
                stepDetails.outputUpload = nxtOutputUpload;
                stepDetails.outputLink = data.OutputLink;
                stepDetails.bookingType = "QUEUED";
                stepDetails.projectId = proj_ID;
                stepDetails.WFVersion = versionNo;
                stepDetails.botId = nxtRPAID;
                stepDetails.inPath = "abc";
                stepDetails.prevStepTaskID = currTaskId;
                stepDetailsToStart.serverBotJsonStr = stepDetails;
                stepDetailsToStart.inputZip = '';
                stepDetailsToStart.isRunOnServer = nxtRunOnServer;
                stepDetailsToStart.flowChartType = data.flowChartType;

                startAnyStep(stepDetailsToStart, $('#botConfigModal'), false, false, true);

                //playServerStartStep(outputFile, execDetails, afterLoadingFlowchart);
                //    },
                //    error: function (xhr, status, statusText) {

                //    },
                //});
            }
            else {
                if (steptype == "Manual") {
                    stepDetailsData.status = data.Status;
                    stepDetailsData.bookingType = "BOOKING";
                }
                else if (steptype == "Automatic" && (data.Reason.toLowerCase() != "success")) {
                    stepDetailsData.status = "";
                }
                else {
                    stepDetailsData.status = data.Status;
                }

            }
            //IF Next Step is CASCADESTEP START END
        } else {
            // if next step icons flag is false
            if (steptype == "Manual") {
                stepDetailsData.status = data.Status;
                stepDetailsData.bookingType = "BOOKING";
            }
            else if (steptype == "Automatic" && data.Reason != null && (data.Reason.toLowerCase() != "success")) {
                stepDetailsData.status = data.Status;
            }
            else if (steptype == "Automatic" && data.Status === "STARTED") {
                stepDetailsData.status = data.Status;
                stepDetailsData.bookingType = "BOOKING";
            }
            else {
                stepDetailsData.status = data.Status;
            }
        }
        stepDetailsData.iconsOnNextStep = data.IconsOnNextStep;
        stepDetailsData.hour = data.Hour;
        stepDetailsData.stepID = data.FlowChartStepId;
        stepDetailsData.outputLink = data.OutputLink;
        stepDetailsData.flowChartType = data.flowChartType;
        if (document.getElementById('iframe_' + data.WoId) != null && document.getElementById('iframe_' + data.WoId) != undefined && document.getElementById('iframe_' + data.WoId) != "undefined") {
            if (steptype == C_EXECUTION_TYPE_MANUALDISABLED) {
                window.parent.changeDisabledManualStepColor(data.Status, data.WoId);
                if (data.Status == C_BOOKING_STATUS_COMPLETED || data.Status == C_BOOKING_STATUS_ONHOLD) {
                    updateFlowchart(data.WoId, data.Status);
                }
            }
            else {
                document.getElementById('iframe_' + data.WoId).contentWindow.changeStepColor(stepDetailsData);

            }           
            //document.getElementById('iframe_' + data.WoId).contentWindow.changeDisabledManualStepColor(data.Status);
        }
        handleWOActionButtons(data.WoId, false);


    }//Status Completed Scenario End 
    else if (data.Status == "FORCE_STOP") {
        let stepDetailsData = new Object();
        stepDetailsData.status = "FORCE_STOP";
        stepDetailsData.iconsOnNextStep = data.IconsOnNextStep;
        stepDetailsData.hour = data.Hour;
        stepDetailsData.stepID = data.FlowChartStepId;
        stepDetailsData.outputLink = data.OutputLink;
        stepDetailsData.flowChartType = data.flowChartType;
        if (document.getElementById('iframe_' + data.WoId) != null && document.getElementById('iframe_' + data.WoId) != undefined && document.getElementById('iframe_' + data.WoId) != "undefined") {
            document.getElementById('iframe_' + data.WoId).contentWindow.changeStepColor(stepDetailsData);
        }
    }
    else if (data.Status == "STARTED") {
        let stepDetailsData = new Object();
        stepDetailsData.status = "STARTED"
        stepDetailsData.iconsOnNextStep = data.IconsOnNextStep;
        stepDetailsData.hour = data.Hour;
        stepDetailsData.stepID = data.FlowChartStepId;
        stepDetailsData.outputLink = data.OutputLink;
        stepDetailsData.flowChartType = data.flowChartType;
        if (document.getElementById('iframe_' + data.WoId) != null && document.getElementById('iframe_' + data.WoId) != undefined && document.getElementById('iframe_' + data.WoId) != "undefined") {
            document.getElementById('iframe_' + data.WoId).contentWindow.changeStepColor(stepDetailsData);
        }
    }
    else if (data.Status == "SKIPPED") {

        let stepDetailsData = new Object();
        stepDetailsData.status = "SKIPPED"
        stepDetailsData.iconsOnNextStep = data.IconsOnNextStep;
        stepDetailsData.hour = data.Hour;
        stepDetailsData.stepID = data.FlowChartStepId;
        stepDetailsData.flowChartType = data.flowChartType;
        //stepDetailsData.outputLink = data.OutputLink;
        if (document.getElementById('iframe_' + data.WoId) != null && document.getElementById('iframe_' + data.WoId) != undefined && document.getElementById('iframe_' + data.WoId) != "undefined") {
            document.getElementById('iframe_' + data.WoId).contentWindow.changeStepColor(stepDetailsData);
        }
    }
    else if (data.Status == "ONHOLD") {
        let stepDetailsData = new Object();
        stepDetailsData.status = "ONHOLD"
        stepDetailsData.iconsOnNextStep = data.IconsOnNextStep;
        stepDetailsData.hour = data.Hour;
        stepDetailsData.stepID = data.FlowChartStepId;
        stepDetailsData.flowChartType = data.flowChartType;
        if (document.getElementById('iframe_' + data.WoId) != null && document.getElementById('iframe_' + data.WoId) != undefined && document.getElementById('iframe_' + data.WoId) != "undefined") {
            document.getElementById('iframe_' + data.WoId).contentWindow.changeStepColor(stepDetailsData);
            if (steptype == C_EXECUTION_TYPE_MANUALDISABLED) {
                window.parent.changeDisabledManualStepColor(data.Status, data.WoId);
			}
        }
    }

    // Notify HUB that this method has been notified by HUB
    //NotifyHUBforAcknowledgement("updateInprogressTasks");
}

