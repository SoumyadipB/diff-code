var refreshFlag = 0;
var subActivityFlowChartDefID;
playNextClicked = false;

const C__SERVER_BOT_INPUT_FILE_URL_ID = '#serverBotInputUrl';
const C_VALIDATION_SUCCESS = 'validated';
const C_FILE_VALIDATION_FAIL_MSG = 'validation failed';
const C_SERVER_BOT_EMPTY_INPUT_URL_MSG = 'Please provide Input file URL';
const C_ZIP_FORMAT = 'zip';
const C_SERVER_BOT_ZIP_VALIDATION_MSG = "Input URL should always have .zip extension";

//Get Input URL
function getInputFilesFlowChart(woidInput) {
    let htmlInput = '';
    $.isf.ajax({
        url: `${service_java_URL}woManagement/getWOInputFile?woid=${woidInput}`,
        async: false,
        success: function (data) {
            if (data.isValidationFailed == false) {
                if (data.responseData.length != 0) {
                    
                    $.each(data.responseData, function (i, d) {
                        if (d.inputName == null) {
                            d.inputName = 'SRID_InputURL'
                        }
                        htmlInput += '<li><a href="' + d.inputUrl + '" target="_blank"><i class="fa fa-file-text"></i> ' + d.inputName + '</a></li>';
                    });
                }
                else {
                    htmlInput += '<li><a href="#">No Input URL</a></li>';
                }
            }
            else {
                pwIsf.alert({ msg: data.formErrors[0], type: 'error' });
            }
        },
        error: function (xhr, status, statusText) {
            pwIsf.alert({ msg: data.formErrors[0], type: 'error' });
        }
    })
    return htmlInput;
}

//Get Output URL
function getOutputFilesFlowChart(woidInput) {
    let htmlInput = '';
    $.isf.ajax({
        url: `${service_java_URL}woManagement/getWOOutputFile?woid=${woidInput}`,
        async: false,
        success: function (data) {
            if (data.responseData.length != 0 && data.isValidationFailed == false) {
                $.each(data.responseData, function (i, d) {
                    htmlInput += '<li><a href="' + d.outputUrl + '" target="_blank"><i class="fa fa-file-text"></i> ' + d.outputName + '</a></li>';
                });
            }
            else
                htmlInput += '<li><a href="#">No Output URL</a></li>';
        }
    })
    return htmlInput;
}

function getWODetailsForWF(woID) {
    uniquePageID = "WORK_ORDER_" + woID;
    $("#commentsContainerDT").empty();
    $("#commentsContainerDT").append('<div id="comments-container' + uniquePageID + '"></div>');
    $("#selectContext").append('<select class="select2able select2-offscreen" id="selectStepsWO' + woID + '" onchange="onChangeContext(this);"></select>');
    $("#searchCommentsDiv").append('<input onkeydown="searchCommentsOnEnter(event,this,\'' + uniquePageID + '\')" name="q" placeholder="Type and Press Enter to Search..." class="searchText" id="searchInComments' + woID + '"></input>');

    var role = JSON.parse(ActiveProfileSession).role;

    getCommentsSection(uniquePageID);

    if (woID == null || woID.length == 0) {
        pwIsf.alert({ msg: 'Something went wrong!!', type: 'warning' });

    } else {
        $.isf.ajax({
            url: `${service_java_URL}woExecution/getWorkOrderViewDetailsByWOID/${woID}`,
            async: false,
            success: function (data) {

                ConvertTimeZones(data, getWorkOrderViewDetailsByWOID_tzColumns);

                var workOrderInfo = "";
                subActivityFlowChartDefID = data[0].flowchartdefid;
                $.isf.ajax({
                    url: `${service_java_URL}woDeliveryAcceptance/getStatusReasons/${woID}`,
                    async: false,
                    success: function (data) {
                        var userDeliveryStatus = data.userDeliveryStatus;
                        if (userDeliveryStatus == null || userDeliveryStatus == "null")
                            userDeliveryStatus = "";

                        var userReason = data.userReason;
                        if (userReason == null || userReason == "null")
                            userReason = "";

                        var userComments = data.userComments;
                        if (userComments == null || userComments == "null")
                            userComments = "";

                        var deliveryStatus = data.deliveryStatus;
                        if (deliveryStatus == null || deliveryStatus == "null")
                            deliveryStatus = "";

                        var deliveryRatings = data.deliveryRatings;
                        if (deliveryRatings == null || deliveryRatings == "null")
                            deliveryRatings = "";

                        var deliveryComment = data.deliveryComment;
                        if (deliveryComment == null || deliveryComment == "null")
                            deliveryComment = "";

                        var deliveryReason = data.deliveryReason
                        if (deliveryReason == null || deliveryReason == "null")
                            deliveryReason = "";

                        if (data.isParent) {
                            workOrderInfo += '<div class=\'container\' style=\'font-size:14px;\'><p><strong>WO Status - REOPENED</strong></p><p>Parent WO Details-</p><table class=\'table table-bordered\'><tbody>';
                        }
                        else {
                            workOrderInfo += '<div class=\'container\' style=\'font-size:14px;\'><table class=\'table table-bordered\'><tbody>';
                        }
                        workOrderInfo += '<tr><td><strong>WO Status</strong></td><td>' + data.woStatus + '</td></tr>';
                        workOrderInfo += '<tr><td><strong>User Delivery Status</strong></td><td>' + userDeliveryStatus + '</td></tr>';

                        workOrderInfo += '<tr><td><strong>User Reason</strong></td><td style=\'color:red\'>' + userReason + '</td></tr>';
                        workOrderInfo += '<tr><td><strong>User Comment</strong></td><td style=\'color:red\'>' + userComments + '</td></tr>';

                        workOrderInfo += '<tr><td><strong>Delivery Status</strong></td><td>' + deliveryStatus + '</td></tr>';
                        if (data.deliveryStatus == 'Accepted') {
                            workOrderInfo += '<tr><td><strong>Delivery Ratings</strong></td><td>' + deliveryRatings + '</td></tr>';
                            workOrderInfo += '<tr><td><strong>Delivery Comment</strong></td><td>' + deliveryComment + '</td></tr>';
                        }
                        else {
                            workOrderInfo += '<tr><td><strong>Delivery Reason</strong></td><td style=\'color:red\'>' + deliveryReason + '</td></tr>';
                            workOrderInfo += '<tr><td><strong>Delivery Comment</strong></td><td style=\'color:red\'>' + deliveryComment + '</td></tr>';
                        }
                        workOrderInfo += '</tbody></table></div>';
                    },
                    error: function (xhr, status, statusText) {
                        var err = JSON.parse(xhr.responseText);
                    }
                });
                var contentWF = '<div class="row"><div class="col-md-8"><h4> Work Order - ' + data[0].wOID + '  ' + data[0].wOName + ' ' + '( ' + '<strong>Project ID</strong> : ' + data[0].projectID + '<strong>, Work Flow Name</strong> : ' + data[0].wFName + ' )';
                if (workOrderInfo.length != 0)
                    contentWF += '&nbsp;&nbsp;<a href= "#" data-toggle="popover" data-html="true" data-trigger="hover" data-placement="bottom" title="Work Order Info" data-content="' + workOrderInfo + '"><span class="fa fa-info-circle" style="color: blue;cursor:pointer" ></span></a></h4></div>';

                contentWF += '<div class="col-md-2">'
                    + '<div class="filesDiv" style="width:80%">'
                    + '<a href="#" class="btn btn-xs btn-success" role="button" style="text-decoration:none;width:100%" title="Input URLs">Input URLs'
                    + '</a>'
                    + '<ul class="dropdown-menu" style="top: 99%;">'
                    + getInputFilesFlowChart(woID)
                    + '</ul>'
                    + '</div>'
                    + '</div>'

                contentWF += '<div class="col-md-2">'
                    + '<div class="filesDiv" style="width:80%">'
                    + '<a href="#" class="btn btn-xs btn-primary" role="button" style="text-decoration:none;width:100%" title="Output URLs">Output URLs'
                    + '</a>'
                    + '<ul class="dropdown-menu" style="top: 99%;">'
                    + getOutputFilesFlowChart(woID)
                    + '</ul>'
                    + '</div>'
                    + '</div>'

                contentWF += '</div><div class="row">' +
                    '<div class="col-md-3"><strong>Network Element Type: </strong>' + data[0].nodeType + '</div>' +
                    '<div class="col-md-3"><strong>Network Element Count: </strong>' + data[0].nodeCount + '</div>' +
                    '<div class="col-md-3 divnowrap"><strong>Network Element Name/ID: </strong>' + data[0].nodeNames + '</div>' +
                    '<div class="col-md-3"><strong>Started On: </strong>' + data[0].actualStartDate + '</div>' +
                    '</div>' +
                    '<div class="row">' +
                    '<div class="col-md-3"><strong>Closed On: </strong>' + data[0].actualEndDate + '</div>' +
                    '<div class="col-md-3"><strong>Digital Efforts: </strong>' + data[0].automatedHours + '</div>' +
                    '<div class="col-md-3"><strong>Manual Efforts: </strong>' + data[0].manualHours + '</div > ' +
                    '<div class="col-md-3"><strong>Total Efforts: </strong>' + data[0].totalHours + '</div>' +
                    '</div><hr />';
                contentWF += '<div class="row">' +
                    '<div class="col-md-2">Started<span style="margin-left: 5px ;padding: 3px 10px 3px 10px;background-color:#6e88ff;"></span></div>' +
                    '<div class="col-md-2">Completed<span style=" margin-left: 5px ;padding: 3px 10px 3px 10px;background-color:#00983b;"></span></div>' +
                    '<div class="col-md-2">On Hold<span style="margin-left: 5px ;padding: 3px 10px 3px 10px;background-color:#E5E500 ;"></span></div>' +
                    '<div class="col-md-2">Skipped<span style=" margin-left: 5px ;padding: 3px 10px 3px 10px;background-color:#FFAB73 ;"></span></div>' +
                    '<div class="col-md-2">Manual<span style=" margin-left: 5px ;padding: 3px 17px 3px 17px;background-color:#dcd7d7;border: 1px solid #31d0c6 ;"></span></div>' +
                    '<div class="col-md-2" style="width:155px">Automatic<span style="margin-left: 5px;padding: 1px 4px 4px 4px;background-color:#ffffff;border: 1px solid red;"><span style=" margin-left: 0px ;padding: 1px 15px 1px 15px;background-color:#31d0c6;border: 1px solid red"></span></span></div>' +
                    '<a href="#" onclick="location.reload();"><span class="fa fa-refresh" style="color:blue;" data-placement="bottom" data-toggle="tooltip" title="Refresh"></span></a>' +
                    '</div>';
                $('#wOdetailForWF').append(contentWF);
                $('[data-toggle="popover"]').popover({
                    container: 'body'
                });
                $("#expertise").select2();
                $('.select2-container').css('width', '195px');
                getStepsWO(subActivityFlowChartDefID, woID, experienced = '21');
            },
            error: function (xhr, status, statusText) {
                var err = JSON.parse(xhr.responseText);
                pwIsf.alert({ msg: err.errorMessage, type: 'error' });
            }
        });
    }
}

function openpdf(data) {

    var parent = $('#objpdf').parent();
    var dataURL = data + '#toolbar=0';
    var newElement = '<object id="objpdf" width="100%" height="100%" type="application/pdf" data="' + dataURL + '">';
    $('#objpdf').remove();
    parent.append(newElement);
    $('#pdfDiv').modal('show');
}

function afterLoadingFlowchart() {
    window.pwIsf.removeLayer();

}

function validateFile(input) {
    let fileFromPlay;
    let regex = new RegExp("(.*?)\.(rar|zip)$");
    if (!window.FileReader) {

        pwIsf.alert({ msg: "The file API isn't supported on this browser yet.", type: 'warning' });
        return falses;
    }

    if (!input.files) {
        pwIsf.alert({ msg: "This browser doesn't seem to support the 'files' property of file inputs.", type: 'warning' });
        return false;
    }
    else if (!input.files[0]) {

        pwIsf.alert({ msg: "Please select a file before clicking 'Load'", type: 'warning' });
        return false;
    }
    else if (!(regex.test(input.value.toLowerCase()))) {
        input.value = '';
        pwIsf.alert({ msg: "Please select only zip files", type: 'warning' });
        return false;
    }
    else {
        let fileInMb = '';
        fileFromPlay = input.files[0];
        if (fileFromPlay.size > 524288000) {
            pwIsf.alert({ msg: "File " + fileFromPlay.name + " is more then 500 MB.", type: 'warning' });
            return false;
        }
        else {
            return true;
        }
    }
}

//Display Flow chart.
function viewFlowChartWorkOrder(version, woid, prjId, subActId) {

    $.isf.ajax({
        url: service_java_URL + "flowchartController/viewFlowChartForSubActivity/" + prjId + "/" + subActId + "/0/" + version,
        success: function (data) {
            if ('Success' in data) {
                if ((data.Success.FlowChartJSON == null) || (data.Success.FlowChartJSON == '')) {
                    data.Success.FlowChartJSON = '{ "cells": [{ "size": { "width": 200, "height": 90 }, "angle": 0, "z": 1, "position": { "x": 225, "y": 230 }, "type": "basic.Rect", "attrs": { "rect": { "rx": 2, "ry": 2, "width": 50, "stroke-dasharray": "0", "stroke-width": 2, "fill": "#dcd7d7", "stroke": "#31d0c6", "height": 30 }, "text": { "font-weight": "Bold", "font-size": 11, "font-family": "Roboto Condensed", "text": "No FlowChart Available", "stroke-width": 0, "fill": "#222138" }, ".": { "data-tooltip-position-selector": ".joint-stencil", "data-tooltip-position": "left" } } }] }'
                }
                var iframeSrc = window.location.href.split("WorkorderAndTask")[0] + 'FlowChartExecution?mode=execute&version=' + $('#flowchartVersion_' + woid).val() + '&woID=' + woid + '&subActID=' + subActId + '&prjID=' + projId;
                var appContainer = $('#iframe_' + woid)[0].contentWindow.app;
                versionNo = window.parent.$("#flowchartVersion_" + woid).val();
                appContainer.graph.fromJSON(JSON.parse(data.Success.FlowChartJSON));
                var root = appContainer.graph.getSources()[0];
                var firstStep = appContainer.graph.getNeighbors(root, { outbound: true })[0];
                firstStep.set('showIcons', true);
                makePopup();
            }
        },
        error: function (xhr, status, statusText) {
            var err = JSON.parse(xhr.responseText);
            console.log(err.errorMessage);

        }
    });
}

function getVersionDropdown(element, projId, subActId) {
    $.isf.ajax({
        url: service_java_URL + "woExecution/getUserWFProficency/" + signumGlobal + "/" + projId + "/" + subActId,
        async: false,
        success: function (data) {
            if (data[0].Error == undefined) {
                $("#" + element).html('');
                $("#" + element).append('<option value="" disabled selected>--Select--</option>');
                for (const indexValue of data) {
                    $("#" + element).append('<option value="' + indexValue.VersionNumber + '">' + indexValue.WorkFlowName + '</option>');
                }
            }
            else {
                $("#" + element).select2('destroy');
			}  
            $("#" + element).hide();
        }
    });
}

function changeWFVersion(woid, prjId, subActId) {

    isVersionChanged = true;
    viewFlowChartWorkOrder($('#flowchartVersion_' + woid).val(), woid, prjId, subActId)


}

function getBotConfigJson(stepID) {
    if (app.graph != undefined) {
        let step = app.graph.getCell(stepID);
        return {
            botConfig: step.attributes.botConfigJson,
            botType: step.attributes.botType,
            botNestId: step.attributes.botNestId,
            outputUpload: step.outputUpload
        };
    }
    else {
        return {
            botConfig: 'checking',
            botType: '',
            botNestId: '',
            outputUpload: ''
        }
    }
}

function InvokeStartFlowChartTask(flowChartType, woID, prjID, taskID, subActivityFlowChartDefID, type, stepID, bookingStatus, booking_Id, botConfigData, botType, fileID, executionType, version, stepName, outputUpload, nextRunOnServerPassed, isInputRequiredPassed, callback, isCallingFromFlowchart = true) {

    let StartTaskInput = PrepareStartFlowChartTaskInput(flowChartType, woID, prjID, taskID, subActivityFlowChartDefID, type, stepID, bookingStatus, booking_Id,
        botConfigData, botType, fileID, executionType, version, stepName, outputUpload, nextRunOnServerPassed, isInputRequiredPassed, callback, isCallingFromFlowchart = true);
    startFlowChartTask(StartTaskInput);
}

function InvokeStopFlowChartTask(flowChartType, woID, subActivityFlowChartDefID, stepID, taskID, isCallingFromFlowchart, executionType, booking_id, booking_type, reason, callback, asyncType = true, isForceStop, isPlayNext, isCalledFromSkip, nextStepType='') {

    if (isForceStop == null || isForceStop == undefined || isForceStop == '') {
        isForceStop = false;
    }
    if (isPlayNext == null || isPlayNext == undefined || isPlayNext == '') {
        isPlayNext = false;
    }
    if (isCalledFromSkip == null || isCalledFromSkip == undefined || isCalledFromSkip == '') {
        isCalledFromSkip = false;
    }


    let StopTaskInput = PrepareStopFlowChartTaskInput(flowChartType, woID, subActivityFlowChartDefID, stepID, taskID, isCallingFromFlowchart,executionType, booking_id, booking_type, reason, callback, asyncType = true, isForceStop, isPlayNext, isCalledFromSkip, nextStepType);
    stopFlowChartTask(StopTaskInput);
}

function PrepareStartFlowChartTaskInput(flowChartType, woID, prjID, taskID, subActivityFlowChartDefID, type, stepID, bookingStatus, booking_Id, botConfigData, botType, fileID, executionType, version, stepName, outputUpload, nextRunOnServerPassed, isInputRequiredPassed, callback, isCallingFromFlowchart = true, isPlayNext, IscalledFromDecisionButton) {
    if (isPlayNext == null || isPlayNext == "" || isPlayNext == undefined) {
        isPlayNext = false;
    }
    var StartFlowChartTaskParams = {};
    StartFlowChartTaskParams.flowChartType = flowChartType;
    StartFlowChartTaskParams.woID = woID;
    StartFlowChartTaskParams.prjID = prjID;
    StartFlowChartTaskParams.taskID = taskID;
    StartFlowChartTaskParams.subActivityFlowChartDefID = subActivityFlowChartDefID;
    StartFlowChartTaskParams.type = type;
    StartFlowChartTaskParams.stepID = stepID;
    StartFlowChartTaskParams.bookingStatus = bookingStatus;
    StartFlowChartTaskParams.booking_Id = booking_Id;
    StartFlowChartTaskParams.botConfigData = botConfigData;
    StartFlowChartTaskParams.botType = botType;
    StartFlowChartTaskParams.fileID = fileID;
    StartFlowChartTaskParams.executionType = executionType;
    StartFlowChartTaskParams.version = version;
    StartFlowChartTaskParams.stepName = stepName;
    StartFlowChartTaskParams.outputUpload = outputUpload;
    StartFlowChartTaskParams.nextRunOnServerPassed = nextRunOnServerPassed;
    StartFlowChartTaskParams.isInputRequiredPassed = isInputRequiredPassed;
    StartFlowChartTaskParams.callback = callback;
    StartFlowChartTaskParams.isCallingFromFlowchart = isCallingFromFlowchart;
    StartFlowChartTaskParams.isPlayNext = isPlayNext;
    StartFlowChartTaskParams.IscalledFromDecisionButton = IscalledFromDecisionButton;
    return StartFlowChartTaskParams;
}

function PrepareStopFlowChartTaskInput(flowChartType, woID, subActivityFlowChartDefID, stepID, taskID, isCallingFromFlowchart, executionType, booking_id, booking_type, reason, callback, asyncType = true, isForceStop, isPlayNext, isCalledFromSkip, nextStepType='') {
    if (isForceStop == null || isForceStop == "" || isForceStop == undefined) {
        isForceStop = false;
    }
    if (isPlayNext == null || isPlayNext == "" || isPlayNext == undefined) {
        isPlayNext = false;
    }
    if (isCalledFromSkip == null || isCalledFromSkip == "" || isCalledFromSkip == undefined) {
        isCalledFromSkip = false;
    }
    var StopFlowChartTaskParams = {};
    StopFlowChartTaskParams.flowChartType = flowChartType;
    StopFlowChartTaskParams.woID = woID;
    StopFlowChartTaskParams.subActivityFlowChartDefID = subActivityFlowChartDefID;
    StopFlowChartTaskParams.stepID = stepID;
    StopFlowChartTaskParams.taskID = taskID;
    StopFlowChartTaskParams.isCallingFromFlowchart = isCallingFromFlowchart;
    StopFlowChartTaskParams.executionType = executionType;
    StopFlowChartTaskParams.booking_id = booking_id;
    StopFlowChartTaskParams.booking_type = booking_type;
    StopFlowChartTaskParams.reason = reason;
    StopFlowChartTaskParams.callback = callback;
    StopFlowChartTaskParams.asyncType = asyncType;
    StopFlowChartTaskParams.isForceStop = isForceStop;
    StopFlowChartTaskParams.isPlayNext = isPlayNext;
    StopFlowChartTaskParams.isCalledFromSkip = isCalledFromSkip;
    StopFlowChartTaskParams.nextStepType = nextStepType;

    return StopFlowChartTaskParams;
}

//Play Next Step - Disabled Manual
function PlayNextDisabledManualStep(flowChartType, woID, prjID, version, isCallingFromFlowchart) {
   initiateDisabledStep(flowChartType, woID, prjID, version, isCallingFromFlowchart);
    
}

//Play Next Step 
function playNextStep(flowChartType, isInputRequiredNext, botType, nextRPAID, woID, prjID, version, stepID, nextTaskID, subActivityFlowChartDefID, nextBookingID, nextStepID, nextBookingStatus, nextExecutionType, currentTaskID, currentBookingID, currentExecutionType, nextStepName, outputUpload, nextRunOnServerPassed, isCallingFromFlowchart = true, paramsObj, IscalledFromDecisionButton) {
    let StartTaskInput;
    let StopTaskInput;
    let defaultOptions = {
        stopStepRequired: true
    };
    let actualOptions = $.extend({}, defaultOptions, paramsObj || {});

    let fileID = null;
    let nextBotConfigJson = null;
    let botNestId = '';

    //model for disbaled step
    let stopDisabledStepDetails = new Object();
    if (currentExecutionType == C_EXECUTION_TYPE_MANUALDISABLED) {
        stopDisabledStepDetails.flowChartType = flowChartType;
        stopDisabledStepDetails.woID = woID;
        stopDisabledStepDetails.subActivityFlowChartDefID = subActivityFlowChartDefID;
        stopDisabledStepDetails.stepID = stepID;
        stopDisabledStepDetails.taskID = currentTaskID;
        stopDisabledStepDetails.executionType = currentExecutionType;
        stopDisabledStepDetails.bookingID = currentBookingID;
        stopDisabledStepDetails.bookingType = $("#hiddenVal_" + woID).data('bookingtype');
    }
    
    //Bot Nest Bot Only
    if (nextExecutionType == "Automatic" && botType.toLowerCase().includes('nest')) {
        rpaID = nextRPAID;
        //Play next From Workflow
        if (isCallingFromFlowchart) {
            nextBotConfigJson = app.graph.getCell(nextStepID).attributes.botConfigJson;
            botType = app.graph.getCell(nextStepID).attributes.botType;
            botNestId = app.graph.getCell(nextStepID).attributes.botNestId;
            openConfigOnPlayNext(flowChartType, nextRunOnServerPassed, isInputRequiredNext, nextRPAID, woID, prjID, urlParams.get("subActID"), version, stepID, nextTaskID, subActivityFlowChartDefID, nextBookingID, nextStepID, nextBookingStatus, nextExecutionType, currentTaskID, currentBookingID, currentExecutionType, nextStepName, nextBotConfigJson, botType, botNestId, isCallingFromFlowchart, window, paramsObj, stopDisabledStepDetails, outputUpload);
        }
        //Play next from In Progress Div
        else {
            if ($('#iframe_' + woID)[0] != undefined) {
                nextBotConfigJson = document.getElementById('iframe_' + woID).contentWindow.getBotConfigJson(nextStepID);
                openConfigOnPlayNext(flowChartType, nextRunOnServerPassed, isInputRequiredNext, nextRPAID, woID, prjID, subActivityID, version, stepID, nextTaskID, subActivityFlowChartDefID, nextBookingID, nextStepID, nextBookingStatus, nextExecutionType, currentTaskID, currentBookingID, currentExecutionType, nextStepName, nextBotConfigJson.botConfig, nextBotConfigJson.botType, nextBotConfigJson.botNestId, isCallingFromFlowchart, $('#iframe_' + woID)[0], paramsObj, stopDisabledStepDetails, outputUpload);
            }
            else {
                var workorderpanel = $(".work_order_dBox_container").find('span').filter(function () { return ($(this).text().indexOf(woID) > -1) });
                var viewFCBtn = workorderpanel.closest('.panel-heading').find('.on-wo-viewFlowchart');
                $(viewFCBtn).click();
                pwIsf.alert({ msg: "This step has configuration. Play this step from flowchart!", type: "info" });

            }
        }
    }
    //Automatic - Local/Server/Bot-Config
    else if (nextExecutionType == "Automatic") {
        rpaID = nextRPAID;
        if (isCallingFromFlowchart) {
            nextBotConfigJson = app.graph.getCell(nextStepID).attributes.botConfigJson;
            //Next Step has Bot Config
            if (nextBotConfigJson != undefined && nextBotConfigJson[0] != undefined && nextBotConfigJson[0] != 0 && nextBotConfigJson[0] != "") {
                openConfigOnPlayNext(flowChartType, nextRunOnServerPassed, isInputRequiredNext, nextRPAID, woID, prjID, urlParams.get("subActID"), version, stepID, nextTaskID, subActivityFlowChartDefID, nextBookingID, nextStepID, nextBookingStatus, nextExecutionType, currentTaskID, currentBookingID, currentExecutionType, nextStepName, nextBotConfigJson, botType, botNestId, isCallingFromFlowchart, window, paramsObj, stopDisabledStepDetails, outputUpload);
            }
            else {

                if (actualOptions.stopStepRequired) {  //playnext button next to decision.
                    if (currentExecutionType == C_EXECUTION_TYPE_MANUALDISABLED) {
                        stopDisabledStep(stopDisabledStepDetails, '', woID, false, false);
                    }
                    else {
                        InvokeStopFlowChartTask(flowChartType, woID, subActivityFlowChartDefID, stepID, currentTaskID, isCallingFromFlowchart, currentExecutionType, currentBookingID, 'BOOKING', '', afterLoadingFlowchart, '', false, true, false);
                    }
                }
                if (nextRunOnServerPassed == '1') {
                    let configDetails = JSON.stringify({ flowChartType: flowChartType, isInputRequired: isInputRequiredNext, isRunOnServer: nextRunOnServerPassed, configForBot: nextRPAID, id: nextStepID, bookingID: nextBookingID, bookingStatus: nextBookingStatus, executionType: nextExecutionType, taskID: nextTaskID, taskName: "", stepName: nextStepName, stepID: nextStepID, botType: botType, version: version, projID: prjID, subActivityDefID: subActivityFlowChartDefID, woID: woID, isCallingFromFlowchart: true, outputUpload: outputUpload });
                    $('#runOnServerAutomatic').attr('data-details', configDetails);
                    $('#runOnLocalAutomatic').attr('data-details', configDetails);
                    $('#modalSelectStartOption').modal('show');
                }
                else
                    StartTaskInput = PrepareStartFlowChartTaskInput(flowChartType, woID, prjID, nextTaskID, subActivityFlowChartDefID, nextExecutionType, nextStepID, nextBookingStatus, nextBookingID, nextBotConfigJson, botType, fileID, nextExecutionType, version, nextStepName, outputUpload, nextRunOnServerPassed, isInputRequiredNext, afterLoadingFlowchart, isCallingFromFlowchart, true, IscalledFromDecisionButton);
                startFlowChartTask(StartTaskInput);
            }
        }


        else if ($('#iframe_' + woID)[0] != undefined) {
            nextBotConfigJson = document.getElementById('iframe_' + woID).contentWindow.getBotConfigJson(nextStepID);

            if (nextBotConfigJson.botConfig != undefined && nextBotConfigJson.botConfig != 0 && nextBotConfigJson.botConfig != "") {
                if (currentExecutionType == C_EXECUTION_TYPE_MANUALDISABLED) {
                    stopDisabledStep(stopDisabledStepDetails, '', woID, false, false);
                }
                else {
                    //stop Manual Step 
                    StopTaskInput = PrepareStopFlowChartTaskInput(flowChartType, woID, subActivityFlowChartDefID, stepID, currentTaskID, isCallingFromFlowchart, currentExecutionType, currentBookingID, 'BOOKING', '', afterLoadingFlowchart, false, false, true, false);
                    stopFlowChartTask(StopTaskInput);                   
                }
                pwIsf.alert({ msg: 'This step might have configuration. Play this step from flowchart!', type: 'info' });


            } else {
                if (actualOptions.stopStepRequired) {
                    if (currentExecutionType == C_EXECUTION_TYPE_MANUALDISABLED) {
                        stopDisabledStep(stopDisabledStepDetails, '', woID, false, false);
                    }
                    else {
                        StopTaskInput = PrepareStopFlowChartTaskInput(flowChartType, woID, subActivityFlowChartDefID, stepID, currentTaskID, isCallingFromFlowchart, currentExecutionType, currentBookingID, 'BOOKING', '', afterLoadingFlowchart, false, false, true, false);
                        stopFlowChartTask(StopTaskInput);
                    }
                }
                if (nextRunOnServerPassed == '1') {
                    let configDetails = JSON.stringify({ flowChartType: flowChartType, isInputRequired: isInputRequiredNext, isRunOnServer: nextRunOnServerPassed, configForBot: nextRPAID, id: nextStepID, bookingID: nextBookingID, bookingStatus: nextBookingStatus, executionType: nextExecutionType, taskID: nextTaskID, taskName: "", stepName: nextStepName, stepID: nextStepID, botType: botType, version: version, projID: prjID, subActivityDefID: subActivityFlowChartDefID, woID: woID, isCallingFromFlowchart: false, outputUpload: outputUpload });
                    $('#runOnServerAutomatic').attr('data-details', configDetails);
                    $('#runOnLocalAutomatic').attr('data-details', configDetails);
                    $('#modalSelectStartOption').modal('show');
                }
                else {
                    StartTaskInput = PrepareStartFlowChartTaskInput(flowChartType, woID, prjID, nextTaskID, subActivityFlowChartDefID, nextExecutionType, nextStepID, nextBookingStatus, nextBookingID, nextBotConfigJson, botType, fileID, nextExecutionType, version, nextStepName, outputUpload, nextRunOnServerPassed, isInputRequiredNext, afterLoadingFlowchart, isCallingFromFlowchart, true);
                    startFlowChartTask(StartTaskInput);
                }
            }
        }
        else {

            let workorderpanel = $(".work_order_dBox_container").find('span').filter(function () { return ($(this).text().indexOf(woID) > -1) });
            let viewFCBtn = workorderpanel.closest('.panel-heading').find('.on-wo-viewFlowchart');
            $(viewFCBtn).click();
            pwIsf.alert({ msg: 'This step might have configuration. Play this step from flowchart!', type: 'info' });
        }
    } //Manual
    else {
        if (actualOptions.stopStepRequired) {
            if (currentExecutionType == C_EXECUTION_TYPE_MANUALDISABLED) {
                stopDisabledStep(stopDisabledStepDetails, '', woID, false, false);
            }
            else {
                StopTaskInput = PrepareStopFlowChartTaskInput(flowChartType, woID, subActivityFlowChartDefID, stepID, currentTaskID, isCallingFromFlowchart, currentExecutionType, currentBookingID, 'BOOKING', '', afterLoadingFlowchart, false, false, true, false);
                stopFlowChartTask(StopTaskInput);
            }
        }
        if (nextExecutionType == C_EXECUTION_TYPE_MANUAL ) {
            StartTaskInput = PrepareStartFlowChartTaskInput(flowChartType, woID, prjID, nextTaskID, subActivityFlowChartDefID, nextExecutionType, nextStepID, nextBookingStatus, nextBookingID, nextBotConfigJson, botType, fileID, nextExecutionType, version, nextStepName, outputUpload, nextRunOnServerPassed, isInputRequiredNext, afterLoadingFlowchart, isCallingFromFlowchart, true, IscalledFromDecisionButton);
            startFlowChartTask(StartTaskInput);
        }
        else {
            initiateDisabledStep(flowChartType, woID, prjID, version, isCallingFromFlowchart);
        }
    }
}

//Open config on play next
function openConfigOnPlayNext(flowChartType, nextRunOnServer, isInputRequiredNext, nextRPAID, woID, prjID, subActivityID, version, stepID, nextTaskID, subActivityFlowChartDefID, nextBookingID, nextStepID, nextBookingStatus, nextExecutionType, currentTaskID, currentBookingID, currentExecutionType, nextStepName, nextBotConfigJson, botType, botNestId, isCallingFromFlowchart, object, paramsObj, stopDisabledStepDetails, outputUpload) {

    let defaultOptions = {
        stopStepRequired: true
    };

    let actualOptions = $.extend({}, defaultOptions, paramsObj || {});
    let fileID = null;

    let currentWindow = '';

    $(object).is('iframe') ? currentWindow = object.contentWindow : currentWindow = window;
    //Bot Config Bot
    if (nextBotConfigJson != "" && nextBotConfigJson != undefined) {
        botConfigJson = JSON.stringify(nextBotConfigJson);
        currentWindow.createFormFromFormBuilderJson('botConfigBody', botConfigJson);
    }
    else {
        botConfigJson = nextBotConfigJson;
    }
    let configDetails = JSON.stringify({ flowChartType: flowChartType, isInputRequired: isInputRequiredNext, isRunOnServer: nextRunOnServer, configForBot: nextRPAID, botConfigJson: nextBotConfigJson, botType: botType, bookingID: nextBookingID, id: nextStepID, bookingStatus: nextBookingStatus, executionType: nextExecutionType, taskID: nextTaskID, taskName: "", stepName: nextStepName, stepID: nextStepID, version: currentWindow.version, projID: prjID, subActivityDefID: subActivityFlowChartDefID, subActId: subActivityID, woID: woID, isCallingFromFlowchart: isCallingFromFlowchart, outputUpload: outputUpload });
    currentWindow.$('#currentCelldata').data('details', configDetails);
    currentWindow.$('#currentCelldata').data('botOldJson', botConfigJson);
    currentWindow.$('#currentCelldata').data('botType', botType);
    currentWindow.$('#currentCelldata').data('botNestId', botNestId);
    currentWindow.$('#cellDataAutomatic').data('details', configDetails);

    $('#runOnServerAutomatic').attr('data-details', configDetails);
    $('#runOnLocalAutomatic').attr('data-details', configDetails);
    //Bot Nest Only
    if (botType.toLowerCase().includes('nest')) {
        if (actualOptions.stopStepRequired) {
            if (currentExecutionType == C_EXECUTION_TYPE_MANUALDISABLED) {
                stopDisabledStep(stopDisabledStepDetails, '', woID, false, false);
            }
            else {
                InvokeStopFlowChartTask(flowChartType, woID, subActivityFlowChartDefID, stepID, currentTaskID, isCallingFromFlowchart, currentExecutionType, '', '', '', afterLoadingFlowchart, false, false, true, false);
            }
        }
        if (isInputRequiredNext) {
            currentWindow.$('#botConfigModal').modal('show');
            currentWindow.$('#fileInputDiv').show();
            $('#fileinputOnServerPlay').attr('required', true);
            getFileInputNames(nextRPAID, currentWindow);

            serverBotForNest(currentWindow, true);
            $('#btnUploadFile').prop('disabled', false);
        }
        else {
            currentWindow.$('#botConfigModal').modal('show');
            serverBotForNest(currentWindow, true);
        }
    }

    else {
        currentWindow.$('#botConfigModal').modal('show');
        $('#fileInputDiv').hide();
        $('#fileinputOnServerPlay').attr('required', false);
        $('#expectedFileNames').hide();
        $('#saveBotConfigBtn').prop('disabled', false);

        if (actualOptions.stopStepRequired) {
            if (currentExecutionType == C_EXECUTION_TYPE_MANUALDISABLED) {
                stopDisabledStep(stopDisabledStepDetails, '', woID, false, false);
            }
            else {
                InvokeStopFlowChartTask(flowChartType, woID, subActivityFlowChartDefID, stepID, currentTaskID, isCallingFromFlowchart, currentExecutionType, '', '', '', afterLoadingFlowchart, false, false, true, false);
            }
        }
    }
}

$(document).on('show.bs.modal', '#modalBotNestStreaming', function () {
    let bookingid = $('#botdetails').data('bookingid');
    let botNestID = $('#botdetails').data('botid');
    let isfBotId = $('#botdetails').data('isfbotid');
    myInterval = setInterval(function () {
        getBotLogs(bookingid, isfBotId);
    }, 10000)
});

function arraysEqual(_arr1, _arr2) {

    if (!Array.isArray(_arr1) || !Array.isArray(_arr2) || _arr1.length !== _arr2.length)
        return false;

    var arr1 = _arr1.concat().sort();
    var arr2 = _arr2.concat().sort();

    for (var i = 0; i < arr1.length; i++) {

        if (arr1[i] !== arr2[i])
            return false;
    }

    return true;
}

$(document).on('hidden.bs.modal', '#modalBotNestStreaming', function (e) {
    e.preventDefault(); clearInterval(myInterval)
});

//validate bot files. 
function validateBotFiles(input, expectedfile, fileList) {
    // $(btn).attr('')
    var file;
    let regex = new RegExp("(.*?)\.(zip|xls|xlsx|pptx|yml)$");
    let returnValue = true;

    // (Can't use `typeof FileReader === "function"` because apparently
    // it comes back as "object" on some browsers. So just see if it's there
    // at all.)
    if (!window.FileReader) {

        pwIsf.alert({ msg: "The file API isn't supported on this browser yet.", type: 'warning' });
        return;
    }
    if (input.files.length != 0) {
        let isSameArray = arraysEqual(expectedfile, fileList);
        if (!isSameArray) {
            pwIsf.alert({ msg: "File name should be same as expected files and number of files uploaded should not be more then 3.", type: 'warning' });
            returnValue = false;
            return returnValue;
        }
    }
    if (input.files.length != 0) {
        let checkext = true;
        for (let i = 0; i < input.files.length; i++) {
            checkext = (regex.test(input.files[i].name.toLowerCase()))
            if (!checkext) {
                pwIsf.alert({ msg: "Please select only zip,xls,xlsx,pptx,yml extention files", type: 'warning' });
                returnValue = false;
                return returnValue;
            }
        }  
    }

    if (!input.files) {
        pwIsf.alert({ msg: "This browser doesn't seem to support the 'files' property of file inputs.", type: 'warning' });
        returnValue = false;
        return returnValue;
    }
    else if (!input.files[0]) {

        pwIsf.alert({ msg: "Please select a file before clicking 'Load'", type: 'warning' });
        returnValue = false;
        return returnValue;
    }
    else if (input.files.length > 3) {
        pwIsf.alert({ msg: "Please select maximum 3 files", type: 'warning' });
        returnValue = false;
        return returnValue;
    }
    else {
        let totalSize = 0;
        for (let i = 0; i < input.files.length; i++) {
            totalSize += input.files[i].size;
        }

        let valid = totalSize <= 524288000;
        if (!valid) {

            pwIsf.alert({ msg: "File/Files is/are more then 500 MB.", type: 'warning' });
            returnValue = false;
            return returnValue;
        }
    }
    return returnValue;
}

$(document).on('click', '#btnUploadFile', function () {

    let currentCellData = JSON.parse($('#currentCelldata').data('details'));
    let stepID = currentCellData.stepID;
    //2VN
    let bookingID = app.graph.getCell(stepID).attributes.isfRefId.toString();
    //let bookingID = app.graph.getCell(stepID).attributes.attrs.task.bookingID.toString();
    let configForBot = currentCellData.configForBot;
    let botNestId = $('#currentCelldata').data('botNestId');
    let fileDataFromPlay = new FormData();
    let fileID = [];
    let input = document.getElementById('fileinputOnServerPlay');
    let fileList = [];
    for (let a = 0; a < input.files.length; a++) {
        fileList.push(input.files[a].name);
    }
    let filevalue = input.value;
    var pattern = /\\/;
    filearr = filevalue.split(pattern);
    let filename = filearr[2];

    let expectedfile = $('.fileNames').text();
    expectedfile = expectedfile.split(',');

    let isValid = validateBotFiles(input, fileList, expectedfile);

    if (isValid) {
        for (let a = 0; a < input.files.length; a++) {
            fileDataFromPlay.append('file', input.files[a]);
        }

        fileDataFromPlay.append('isfBotId', configForBot);// 12000101botNestId
        fileDataFromPlay.append('bookingId', bookingID); //akjffijk  
        // }
        pwIsf.addLayer({ text: "Please wait ..." });
        $.isf.ajax({
            url: botNestExternal_URL + "uploadUserInput",
            enctype: 'multipart/form-data',
            type: "POST",
            data: fileDataFromPlay,
            processData: false,
            contentType: false,
            cache: false,
            timeout: 600000,
            success: function (data) {
                // console.log('success');
                if (data.includes('fileIds')) {
                    if (JSON.parse(data).fileIds.length == 0) { fileID = [] }
                    else {
                        for (let a = 0; a < JSON.parse(data).fileIds.length; a++) {
                            fileID.push(JSON.parse(data).fileIds[a]);
                        }
                    }
                    $('#currentCelldata').data('fileID', fileID);
                    $('#btnUploadFile').prop('disabled', true);
                    pwIsf.alert({ msg: "File Uploaded", type: 'info' });
                }
                else {
                    pwIsf.alert({ msg: data, type: info });
                    $('#currentCelldata').data('fileID', null);
                }
            },
            error: function () {
                console.log('error');
            },
            complete: function () {
                pwIsf.removeLayer();
            }
        });
        $('#saveBotConfigBtn').prop('disabled', false);
    }
    else {
        $('#saveBotConfigBtn').prop('disabled', true);

    }
});

//Get Bot Video Streaming URL
function getVideoStreamingUrl(booking_Id) {
    let botVideoURLDetails = new FormData();
    botVideoURLDetails.append("bookingId", booking_Id);//booking_Id;1wkjc9k

    $.isf.ajax({
        type: "POST",
        enctype: 'multipart/form-data',
        url: botNestExternal_URL + "getVideoStreamURL",
        processData: false,
        contentType: false,
        cache: false,
        timeout: 600000,
        data: botVideoURLDetails,
        success: function (data) {
            console.log(data);
            $($('#botVideo').find('source')[0]).attr('src', data);
            //play video using HLS.
            if (Hls.isSupported()) {
                var video = document.getElementById('botVideo');
                var hls = new Hls();
                hls.loadSource(data);
                hls.attachMedia(video);
                hls.on(Hls.Events.MANIFEST_PARSED, function () {
                    video.play();
                });
            }
        },
        error: function () {
            console.log('error');
        }
    });
}

//Get BOT LOGs
function getBotLogs(booking_Id, isfBotId) {
    let botStatusDetails = new FormData();
    botStatusDetails.append("isfBotId", isfBotId);
    botStatusDetails.append("bookingId", booking_Id);//booking_Id;1wkjc9k

    $.isf.ajax({
        type: "POST",
        enctype: 'multipart/form-data',
        url: botNestExternal_URL + "getBotStatus",
        processData: false,
        contentType: false,
        cache: false,
        timeout: 600000,
        data: botStatusDetails,
        success: function (data) {
            console.log(data);
            let status = data;
            let count = 1;
            let checkLastStatus = $('#botLogStatus ul').find('li :last').text();
            if (checkLastStatus != status) {
                $("#botLogStatus ul").append('<li><span class="tab">' + data + '</span></li>');

                if (status.toLowerCase() == "resource allocated" || status.toLowerCase() == "vm launched") {
                    //if (count == 1) {
                    $(".video-js").hide();
                    $('#labelForVideo').remove();
                    $("#botStream").append('<b id="labelForVideo">Video Streaming will start in a while...</b>');
                    // }
                    // count++;
                }
                else if (status.includes('Completed')) {
                    clearInterval(myInterval);
                    $('#labelForVideo').remove();
                    //var video = document.getElementById('botVideo');
                    // video.stop();
                    $(".video-js").hide();

                    $("#botStream").append('<b id="labelForVideo">BOT Execution Complete.</b>');
                }
                else if (status.toLowerCase() == "video stream started" || status.toLowerCase() == "bot launched" || status.toLowerCase() == "bot execution in progress" || status.toLowerCase() == "bot execution concluded" || status.toLowerCase() == "bot execution completed" || status.includes("BOT Execution In Progress")) {
                    $(".video-js").show();
                    $('#labelForVideo').remove();
                    getVideoStreamingUrl(booking_Id);
                }
            }


        },
        error: function () {
            console.log('error');
        }
    });
}

function saveBotConfig() {
    let StartTaskInput;
    let getFormJson = '';
    actualJson = '';

    let currentCellData = JSON.parse($('#currentCelldata').data('details'));
    let formJson = $('#currentCelldata').data('botOldJson');
    let botType = $('#currentCelldata').data('botType');
    let botNestId = $('#currentCelldata').data('botNestId');
    if (formJson != "") {
        getFormJson = updateFormBuilderJsonWithExistingFormData(JSON.parse(formJson), 'botConfigForm');
        actualJson = common_getActualJsonFromFormBuilderJson(getFormJson);
    }

    let woID = currentCellData.woID;
    let prj_ID = currentCellData.projID;
    let taskID = currentCellData.taskID;
    let subActivityDefID = currentCellData.subActivityDefID;
    let execType = currentCellData.executionType;
    let stepID = currentCellData.stepID;
    let bookingStatus = currentCellData.bookingStatus;
    let version = currentCellData.version;
    let stepName = currentCellData.stepName;
    let fileID = $('#currentCelldata').data('fileID');
    let booking_Id = currentCellData.bookingID;
    let outputUpload = currentCellData.outputUpload;
    let runOnServer = currentCellData.isRunOnServer;
    let isInputRequired = currentCellData.isInputRequired;
    let flowChartType = '';

    if (botType.toLowerCase().includes('nest')) {
        startBotNestTask(botType, currentCellData, formJson, botNestId, fileID, afterLoadingFlowchart)
    }
    else if (runOnServer == '0') {
        StartTaskInput = PrepareStartFlowChartTaskInput(flowChartType, woID, prj_ID, taskID, subActivityDefID, execType, stepID, bookingStatus, booking_Id, JSON.stringify(actualJson), botType, fileID, execType, version, stepName, outputUpload, runOnServer, isInputRequired, afterLoadingFlowchart);
        startFlowChartTask(StartTaskInput);

    }
    else {
        $('#modalSelectStartOption').modal('show');

    }

}

//stop bot nest step
function stopBotNestTask(bookingId, stepID, woID, flowChartType, isCallingFromFlowchart, callback) {
    let stopBotData = new FormData();
    stopBotData.append('bookingId', bookingId);
    let stepDetailsData = new Object();
    window.parent.pwIsf.addLayer({ text: "Please wait ..." });

    $.isf.ajax({
        type: "POST",
        async: false,
        enctype: 'multipart/form-data',
        processData: false,
        contentType: false,
        cache: false,
        timeout: 600000,
        url: botNestExternal_URL + "abortBot",
        data: (stopBotData),
        success: function (data) {
            if (data == 'success') {
                pwIsf.alert({ msg: "Bot aborted.", type: "info" });
                stepDetailsData.flowChartType = flowChartType;

                if (isCallingFromFlowchart) {

                    stepDetailsData.status = "COMPLETED";
                    stepDetailsData.hour = data.hour;
                    stepDetailsData.stepID = stepID;
                    changeStepColor(stepDetailsData)
                    window.parent.requestInprogressTasks();
                    window.parent.requestWorkorder();
                    window.parent.getWOpercentageAndPerTextFromAPI({ woId: woID });
                }
                else {

                    if ($('#iframe_' + woID)[0] != undefined) {
                        //  $('#iframe_' + woID)[0].contentWindow.location.reload(true);

                        stepDetailsData.status = "COMPLETED";
                        stepDetailsData.hour = data.hour;
                        stepDetailsData.stepID = stepID;

                        document.getElementById('iframe_' + woID).contentWindow.changeStepColor(stepDetailsData);                        
                    }
                    requestInprogressTasks();
                    requestWorkorder();
                    getWOpercentageAndPerTextFromAPI({ woId: woID });
                }
                handleWOActionButtons(woID, isCallingFromFlowchart);
            }
            else { pwIsf.alert({ msg: "Abort Bot Fail!", type: "info" }); }

        },
        error: function () {
            pwIsf.alert({ msg: 'Error while aborting BOT', type: "error" });
        },
        complete: function () {
            pwIsf.removeLayer();
        }
    });
}

// start task
function startAnyStep(stepDetailsToStart, modalID, isPlayNext, IscalledFromDecisionButton, IsSignalRNotification) {
    let wOID = stepDetailsToStart.serverBotJsonStr.wOID;
    let executionType = stepDetailsToStart.serverBotJsonStr.executionType;
    let projectId = stepDetailsToStart.serverBotJsonStr.projectId;
    let taskID = stepDetailsToStart.serverBotJsonStr.taskID;
    let WFversion = stepDetailsToStart.serverBotJsonStr.WFVersion;
    let stepID = stepDetailsToStart.serverBotJsonStr.stepID;
    let botPlatform = stepDetailsToStart.serverBotJsonStr.botPlatform;
    let subActivityFlowChartDefID = stepDetailsToStart.serverBotJsonStr.subActivityFlowChartDefID;
    let botId = stepDetailsToStart.serverBotJsonStr.botId;
    let formJson = stepDetailsToStart.serverBotJsonStr.formJson;
    let type = stepDetailsToStart.serverBotJsonStr.type;
    let inPath = stepDetailsToStart.serverBotJsonStr.inPath;
    let signumID = stepDetailsToStart.serverBotJsonStr.signumID;
    let nodes = stepDetailsToStart.serverBotJsonStr.nodes;
    let decisionValue = stepDetailsToStart.serverBotJsonStr.decisionValue;
    let bookingType = stepDetailsToStart.serverBotJsonStr.bookingType;
    let botConfigJson = stepDetailsToStart.botConfigJson;
    let inputFile = stepDetailsToStart.inputZip;
    let stepName = stepDetailsToStart.serverBotJsonStr.stepName;
    let isCallingFromFlowchart = stepDetailsToStart.serverBotJsonStr.isCallingFromFlowchart;
    let flowChartType = stepDetailsToStart.flowChartType;
    let refferer = "ui";
    let cascadeInput;
    let outputUpload = stepDetailsToStart.serverBotJsonStr.outputUpload;
    let isRunOnServer = stepDetailsToStart.isRunOnServer;
    let isInputRequired = stepDetailsToStart.isInputRequired;
    let proficiencyID = '';
    let serverBotInputFileUrl = stepDetailsToStart.serverBotInputUrl;
    let serverBotOutputUrl = stepDetailsToStart.serverBotOutputUrl;
    if (isCallingFromFlowchart) {
        proficiencyID = urlParams.get("proficiencyId");
    }
    else {
        proficiencyID = $(`#viewBtn_${wOID}`).data('details').proficiencyId;
    }    

    //if (isRunOnServer === '1') { outputUpload = "Yes"; }
    //else { outputUpload = stepDetailsToStart.serverBotJsonStr.outputUpload; }

    if (isPlayNext == null || isPlayNext === "" || isPlayNext === undefined) {
        isPlayNext = false;
    }

    if (serverBotInputFileUrl == null || serverBotInputFileUrl === "") {
        serverBotInputFileUrl = null;
    }

    stepDetailsToStart.serverBotJsonStr.refferer = refferer;
    stepDetailsToStart.serverBotJsonStr.externalSourceName = refferer;
    stepDetailsToStart.serverBotJsonStr.outputUpload = outputUpload;
    stepDetailsToStart.serverBotJsonStr.proficiencyID = proficiencyID;
    stepDetailsToStart.serverBotJsonStr.serverBotInputUrl = serverBotInputFileUrl;
    stepDetailsToStart.serverBotJsonStr.serverBotOutputUrl = serverBotOutputUrl;

    let startStepData = new FormData();
    startStepData.append('botConfigJson', botConfigJson);
    startStepData.append('inputZip', inputFile);
    startStepData.append('serverBotModel', JSON.stringify(stepDetailsToStart.serverBotJsonStr));

    let startTaskApiUrl = `${service_java_URL}woExecution/startTask`;

    window.parent.pwIsf.addLayer({ text: C_PLEASE_WAIT });
    $.isf.ajax({
        type: 'POST',
        enctype: 'multipart/form-data',
        url: startTaskApiUrl,
        processData: false,
        contentType: false,
        cache: false,
        timeout: 600000,
        data: startStepData,
        success: function (data) {
            if (data.isApiSuccess) {
                let stepDetailsData = new Object();
                if (modalID.css('display') === 'block' && type.toLowerCase() !== "botnest")
                { modalID.modal('toggle'); }

                let queue_id = data.bookingID;
                setRunningStep(stepID, stepName);

                stepDetailsData.flowChartType = flowChartType;
                var playNextStatus = "";
                if (isPlayNext) {
                    playNextStatus = "PLAYNEXT";
                }
                else {
                    playNextStatus = "STARTED";
                }
                if (isCallingFromFlowchart) {
                    stepDetailsData.status = "STARTED";
                    if (type != undefined && type != "") {
                        app.graph.getCell(stepID).attributes.isfRefId = queue_id.toString();
                    }

                    (type != "" && type != undefined && type.toLowerCase().includes('nest')) ? '' : stepDetailsData.hour = data.hour;
                    stepDetailsData.stepID = stepID;
                    stepDetailsData.bookingType = bookingType;

                    changeStepColor(stepDetailsData)
                    window.parent.requestInprogressTasks();

                    if (isPlayNext === true && !(IscalledFromDecisionButton == undefined || IscalledFromDecisionButton == null || IscalledFromDecisionButton == false))
                    {
                        // To Notify Floating Window and All opened tabs
                        var InProgressTasks = GetSavedAutoSenseData();
                        playNextStatus = "DECISION";
                        let autoSenseInputData = PrepareAutoSenseInputData(wOID, subActivityFlowChartDefID, stepID, playNextStatus, InProgressTasks);
                        window.parent.NotifyClientOnWorkOrderStatusChange(stepDetailsData, wOID, autoSenseInputData, true);
                    }
                    else if (isPlayNext === true)
                    {
                        window.parent.NotifyClientOnWorkOrderStatusChange(stepDetailsData, wOID, null, true);
                    }
                    else {
                        // To Notify Floating Window and All opened tabs
                        var InProgressTasks =GetSavedAutoSenseData();
                        let autoSenseInputData = PrepareAutoSenseInputData(wOID, subActivityFlowChartDefID, stepID, playNextStatus, InProgressTasks);
                        window.parent.NotifyClientOnWorkOrderStatusChange(stepDetailsData, wOID, autoSenseInputData, true);

                    }

                    window.parent.requestWorkorder();
                    disableAssessedExperiencedToggle(wOID);
                    window.parent.getWOpercentageAndPerTextFromAPI({ woId: wOID });
                    if (playNextClicked && executionType === "Automatic" && isRunOnServer == 1) {
                        console.log('');
                    }
                    else if (executionType === "Automatic" && botPlatform === "local") {
                        openISFDesktop(`isfalert:exec_${rpaID}_${wOID}_${taskID}_${queue_id}`);
                    }
                }
                else {
                    if ($('#iframe_' + wOID)[0] != undefined) {

                        if (type != undefined) {
                            document.getElementById('iframe_' + wOID).contentWindow.app.graph.getCell(stepID).attributes.isfRefId = queue_id.toString();
                        }

                        stepDetailsData.status = "STARTED";
                        stepDetailsData.hour = data.hour;
                        stepDetailsData.stepID = stepID;
                        stepDetailsData.bookingType = bookingType;


                        document.getElementById('iframe_' + wOID).contentWindow.changeStepColor(stepDetailsData);
                    }

                    window.parent.requestInprogressTasks();

                    if (isPlayNext == true && !(IscalledFromDecisionButton == undefined || IscalledFromDecisionButton == null || IscalledFromDecisionButton == false)) {
                        // To Notify Floating Window and All opened tabs
                        var InProgressTasks = GetSavedAutoSenseData();
                        let autoSenseInputData = PrepareAutoSenseInputData(wOID, subActivityFlowChartDefID, stepID, playNextStatus, InProgressTasks);
                        window.parent.NotifyClientOnWorkOrderStatusChange(stepDetailsData, wOID, autoSenseInputData, true);
                    }
                    else if (isPlayNext) {
                        window.parent.NotifyClientOnWorkOrderStatusChange(stepDetailsData, wOID, null, true);
                    }

                    else {
                        // To Notify Floating Window and All opened tabs
                        var InProgressTasks = GetSavedAutoSenseData();
                        let autoSenseInputData = PrepareAutoSenseInputData(wOID, subActivityFlowChartDefID, stepID, playNextStatus, InProgressTasks);
                        window.parent.NotifyClientOnWorkOrderStatusChange(stepDetailsData, wOID, autoSenseInputData, true);
                    }
                    window.parent.requestWorkorder();
                    disableAssessedExperiencedToggle(wOID, false);
                    window.parent.getWOpercentageAndPerTextFromAPI({ woId: wOID });
                    if (executionType == "Automatic" && botPlatform == "local") {
                        openISFDesktop('isfalert:exec_' + rpaID + '_' + wOID + '_' + taskID + '_' + queue_id);
                    }
                }
                handleWOActionButtons(wOID, isCallingFromFlowchart);
            }
            else {
                if (IsSignalRNotification === undefined || IsSignalRNotification === false || IsSignalRNotification === null)
                pwIsf.alert({ msg: data.responseMsg, type: 'error' });
                else
                    window.parent.requestInprogressTasks();
            }
            window.parent.pwIsf.removeLayer();
        },
        error: function (xhr, status, statusText) {
            var errorMessage = JSON.parse(xhr.responseText).errorMessage;
            if (errorMessage == undefined || errorMessage == '') {
                errorMessage = "Some issue occured. Please try again!"
            }
            pwIsf.alert({ msg: errorMessage, type: 'error' });
            pwIsf.removeLayer();
            window.parent.pwIsf.removeLayer();
        }
    });
}

function autoResizeForIcon(rappidObj, element) {  // Make autosize of step boxes according to text

    let view = rappidObj.paper.findViewByModel(element);
    let rect, g; 
    let rect_bbox;
    //let maxXIndex = app.graph.maxZIndex();

    if (element.attributes.type == C_STEP_TYPE_MANUAL || element.attributes.type === C_STEP_TYPE_AUTOMATIC) {
        rect = view.$('rect')[2];
        rect_bbox = V(rect).bbox(true);
        if (rect_bbox.width <= 200) {
            element.resize(226, rect_bbox.height + 100);
        }
       
    }

}
//start bot nest step.
function serverBotForNest(currentWindow, isPlayNext) {
    let actualJson = '';
    let currentCellData = JSON.parse(currentWindow.$('#currentCelldata').data('details'));
    let wOID = currentCellData.woID;
    let status = currentCellData.bookingStatus;
    let execType = currentCellData.executionType;
    let prjID = currentCellData.projID;
    let taskID = currentCellData.taskID;
    let version = currentCellData.version;
    let isCallingFromFlowchart = currentCellData.isCallingFromFlowchart;
    let subActFlowChartDefID = currentCellData.subActivityDefID;
    let stepID = currentCellData.stepID;
    let botID = currentCellData.configForBot;
    let formJson = currentWindow.$('#currentCelldata').data('botOldJson');
    let botType = currentWindow.$('#currentCelldata').data('botType');
    let botNestId = currentWindow.$('#currentCelldata').data('botNestId');
    let stepName = currentCellData.stepName;
    let isInputRequired = currentCellData.isInputRequired;
    let flowChartType = currentCellData.flowChartType;

    if (formJson != "") {
        getFormJson = updateFormBuilderJsonWithExistingFormData(JSON.parse(formJson), 'botConfigForm');
        actualJson = common_getActualJsonFromFormBuilderJson(getFormJson);
    }
    if (isPlayNext === null || isPlayNext === undefined || isPlayNext == '') {
        isPlayNext = false;
    }
    if (botID != 0) {
        var executionPlanObj = new Object();
        executionPlanObj.signumID = signumGlobal;
        executionPlanObj.isApproved = 0;
        executionPlanObj.executionType = execType;
        executionPlanObj.woid = wOID;
        executionPlanObj.taskid = taskID;
        executionPlanObj.projectID = prjID;
        executionPlanObj.versionNO = version;
        executionPlanObj.subActivityFlowChartDefID = subActFlowChartDefID;
        executionPlanObj.stepID = stepID;
        //var JsonObj = JSON.parse(executionPlanObj);

        $.isf.ajax({
            url: service_java_URL + "woExecution/checkParallelWorkOrderDetails/", 
            type: "POST",
            data: JSON.stringify(executionPlanObj),
            contentType: "application/json",
            success: function (data) {
                if (data.isValidationFailed == false) {
                    if (data.responseData.executeFlag == true) {
                        $.isf.ajax({
                            type: "GET",
                            async: false,
                            url: service_java_URL + "woExecution/checkIFPreviousStepCompleted/" + wOID + "/" + taskID + "/" + subActFlowChartDefID + "/" + stepID,
                            success: function (data) {
                                if (data.responseData.allowed) {
                                    var botjson = new Object();
                                    botjson.signumID = signumGlobal;
                                    botjson.wOID = wOID;
                                    botjson.projectId = prjID;
                                    botjson.taskID = taskID;
                                    botjson.botId = botID;
                                    botjson.inPath = "abc";
                                    botjson.stepName = stepName;
                                    botjson.isCallingFromFlowchart = isCallingFromFlowchart;
                                    // botjson.isInputRequired = isInputRequired;
                                    $.isf.ajax({
                                        url: service_java_URL + "woExecution/getWorkOrderViewDetailsByWOID/" + wOID,
                                        async: false,
                                        success: function (data) {
                                            botjson.nodes = data[0].nodeNames;
                                        },
                                        error: function (xhr, status, statusText) {

                                        }
                                    });
                                    botjson.botPlatform = "server";
                                    botjson.stepID = stepID;
                                    botjson.subActivityFlowChartDefID = subActFlowChartDefID;
                                    botjson.executionType = execType;
                                    botjson.type = botType;
                                    botjson.bookingType = "QUEUED";

                                    if (environment != "Prod") {
                                        botjson.forTesting = "YES";
                                    }
                                    else {
                                        botjson.forTesting = "NO";
                                    }

                                    let stepDetailsData = new Object();
                                    stepDetailsData.inputZip = '';
                                    stepDetailsData.botConfigJson = actualJson;
                                    stepDetailsData.serverBotJsonStr = botjson;
                                    stepDetailsData.isInputRequired = isInputRequired;
                                    stepDetailsData.flowChartType = flowChartType;
                                    startAnyStep(stepDetailsData, $('#botConfigModal'), isPlayNext);
                                }
                                else {
                                    $('#botConfigModal').modal('hide');
                                    pwIsf.alert({ msg: " Please complete previous task/step. ", type: 'error' });
                                    pwIsf.removeLayer();
                                }
                            },
                            error: function (xhr, status, statusText) {
                                var err = JSON.parse(xhr.responseText);
                                pwIsf.alert({ msg: err.errorMessage, type: 'error' });
                                pwIsf.removeLayer();
                                $('#botConfigModal').modal('hide');
                            },
                            complete: function () {
                            }
                        });
                    }
                }
                else {

                    pwIsf.removeLayer();
                    $('#botConfigModal').modal('hide');
                    pwIsf.alert({ msg: data.formErrors[0], type: 'warning' });
                }
            },
            error: function (data) {
                var err = JSON.parse(xhr.responseText);
                pwIsf.removeLayer();
                $('#botConfigModal').modal('hide');
                pwIsf.alert({ msg: "error", type: 'error' });
            },
        });
    }
}

function validationForBotNest() {
    var valid;
    let validateBotIDData = new FormData();
    validateBotIDData.append('botId', 0);//rpaId;1728
    validateBotIDData.append('bookingId', 0);//booking_Id 

    $.isf.ajax({
        type: "POST",
        async: false,
        enctype: 'multipart/form-data',
        statusCode: {
            404: function () {
                valid = false;
            },
            500: function () {
                valid = true;
            }

        },
        processData: false,
        contentType: false,
        cache: false,
        timeout: 600000,
        url: botNestExternal_URL + "validateBotId",
        data: (validateBotIDData),
        success: function (data) {
            valid = true;
        },
        error: function (jqXHR, textStatus, errorThrown) {
            valid = false;

        }

    });
    return valid;
}

function startBotNestTask(botType, currentCellData, actualJson, botNestId, fileID, callback) {
    let woID = currentCellData.woID;
    let status = currentCellData.bookingStatus;
    let execType = currentCellData.executionType;
    let prjID = currentCellData.projID;
    let taskID = currentCellData.taskID;
    let version = currentCellData.version;
    let subActFlowChartDefID = currentCellData.subActivityDefID;
    let stepID = currentCellData.stepID;
    let botID = currentCellData.configForBot;

    window.parent.pwIsf.addLayer({ text: "Please wait ..." });

    if (botID != 0) {
        let queue_id = app.graph.getCell(stepID).attributes.isfRefId.toString();
        let validateBotIDData = new FormData();
        validateBotIDData.append('botId', botID);//rpaId;1728
        validateBotIDData.append('bookingId', queue_id);//booking_Id 

        $.isf.ajax({
            type: "POST",
            async: false,
            enctype: 'multipart/form-data',
            processData: false,
            contentType: false,
            cache: false,
            statusCode: {
                404: function () {
                    pwIsf.alert({ msg: "BOTNest bots cannot be run on internet!", type: 'error' });
                }
            },
            timeout: 600000,
            url: botNestExternal_URL + "validateBotId",
            data: (validateBotIDData),
            success: function (data) {

                botInstanceId = data.botInstanceId;
                if (data.status == "valid") {
                    botNestID = data.botId;
                    let botDetails = new FormData();
                    botDetails.append('isfBotId', botID);  //botNestID;12000190 
                    botDetails.append('bookingId', queue_id);   //booking_Id 1wkjc9k
                    botDetails.append('fileIds', fileID);
                    botDetails.append('botConfig', actualJson);
                    botDetails.append('isfURL', service_java_URL);
                    let botInstanceId = '';

                    $.isf.ajax({
                        type: "POST",
                        async: false,
                        enctype: 'multipart/form-data',
                        processData: false,
                        contentType: false,
                        cache: false,
                        timeout: 600000,
                        url: botNestExternal_URL + "launchBotNest",
                        data: (botDetails),
                        success: function (data) {
                            botInstanceId = data.botInstanceId;
                            $("#botConfigModal").modal('hide');
                        },
                        error: function () {
                            pwIsf.removeLayer();
                        }
                    });

                }
                else {
                    pwIsf.alert({ msg: "BookingId not unique.", type: 'error' });
                    pwIsf.removeLayer();
                }
            },
            error: function () {
                console.log("error");
                pwIsf.removeLayer();
            }
        });

        // window.parent.$('#togBtnForFlowChartViewMode_' + woID).prop('disabled', 'disabled'); //disabled toggal button of assessed/experienced
        disableAssessedExperiencedToggle(woID);
        pwIsf.removeLayer();
    }
}

//download bot file.
function downloadFiles(isfBotId, bookingID) {

    let xmlhttp = new XMLHttpRequest();   // new HttpRequest instance
    //url needs to change here
    xmlhttp.open("POST", botNestExternal_URL + "downloadReport", true);
    xmlhttp.setRequestHeader("Content-Type", "application/json;charset=UTF-8");
    xmlhttp.send(JSON.stringify({ 'bookingId': bookingID, "isfBotId": isfBotId }));
    xmlhttp.responseType = "arraybuffer";

    xmlhttp.onload = function () {
        //var uri_doc = xmlhttp.responseText;

        let blob = xmlhttp.response;
        let contentType = xmlhttp.getResponseHeader("Content-Type");
        let filename = xmlhttp.getResponseHeader("filename");

        //console.log(xmlhttp.getAllResponseHeaders());

        if (filename == 'None') {
            pwIsf.alert({ msg: "No file availabe to download", type: "error" });
        }
        else {

            var newBlob = new Blob([blob], { type: contentType });

            // console.log(xmlhttp);
            let link = document.createElement('a');
            link.href = window.URL.createObjectURL(newBlob);
            link.download = filename;
            document.body.appendChild(link);
            link.click();
            document.body.removeChild(link);
        }
    }

}

//Start disabled step
function startDisabledStep(stepDetailsToStart, isCallingFromFlowChart) {
    let refferer = 'ui';
    let wOID = stepDetailsToStart.serverBotJsonStr.wOID;
    let instanceId = isCallingFromFlowChart ? urlParams.get("proficiencyId").toString().slice(0, -1) : document.getElementById("iframe_" + wOID).contentWindow.urlParams.get("proficiencyId").toString().slice(0, -1);
    let proficiencyID = isCallingFromFlowChart ? window.parent.$('#togBtnForFlowChartViewMode_' + wOID).prop("checked") ? instanceId + '2' : instanceId + '1' : $('#togBtnForFlowChartViewMode_' + wOID).prop("checked") ? instanceId + '2' : instanceId + '1';
    proficiencyID = parseInt(proficiencyID);

    stepDetailsToStart.serverBotJsonStr.refferer = refferer;
    stepDetailsToStart.serverBotJsonStr.externalSourceName = refferer;
    stepDetailsToStart.serverBotJsonStr.proficiencyID = proficiencyID;


    let startStepData = new FormData();
    startStepData.append('botConfigJson', undefined);
    startStepData.append('inputZip', stepDetailsToStart.inputZip);
    startStepData.append('serverBotModel', JSON.stringify(stepDetailsToStart.serverBotJsonStr));

    const serverBotPlatform = 'server';
    let startTaskApiUrl = '';
    if (stepDetailsToStart.serverBotJsonStr.botPlatform.toLowerCase() === serverBotPlatform && stepDetailsToStart.inputZip !== null && stepDetailsToStart.inputZip !== '') {
        startTaskApiUrl = `${service_java_URL_VM}woExecution/startTask`;
    }
    else {
        startTaskApiUrl = `${service_java_URL}woExecution/startTask`;
    }

    $.isf.ajax({
        type: 'POST',
        enctype: 'multipart/form-data',
        url: startTaskApiUrl,

        //async: false,
        processData: false,
        contentType: false,
        cache: false,
        timeout: 600000,
        data: startStepData,
        success: function (data) {
            pwIsf.removeLayer();
            if (data.isApiSuccess) {

                let disabledStepDetails = isCallingFromFlowChart ? JSON.parse(window.parent.$('#woButtons_' + wOID + ' .disabledStepObj').val()) : JSON.parse($('#woButtons_' + wOID + ' .disabledStepObj').val());
                disabledStepDetails.status = C_BOOKING_STATUS_STARTED;
                disabledStepDetails = JSON.stringify(disabledStepDetails);

                setRunningDisabledStep(wOID);

                let stepDetailsData = new Object();

                stepDetailsData.flowChartType = stepDetailsToStart.flowChartType;
                stepDetailsData.status = C_EXECUTION_TYPE_MANUALDISABLED + '_' + C_BOOKING_STATUS_STARTED;
                stepDetailsData.stepID = stepDetailsToStart.serverBotJsonStr.stepID;
                stepDetailsData.bookingType = stepDetailsToStart.serverBotJsonStr.bookingType;

                if (isCallingFromFlowChart) {
                    window.parent.requestInprogressTasks();
                    window.parent.requestWorkorder();

                    //Notify other tabs and floating window
                    window.parent.NotifyClientOnWorkOrderStatusChange(stepDetailsData, wOID, null, true);

                    window.parent.$('#woButtons_' + wOID + ' .disabledStepObj').val(disabledStepDetails);
                    window.parent.changeDisabledManualStepColor(C_BOOKING_STATUS_STARTED, wOID);
                }
                else {
                    requestInprogressTasks();
                    requestWorkorder();

                    //Notify other tabs and floating window
                    NotifyClientOnWorkOrderStatusChange(stepDetailsData, wOID, null, true);

                    $('#woButtons_' + wOID + ' .disabledStepObj').val(disabledStepDetails);
                    changeDisabledManualStepColor(C_BOOKING_STATUS_STARTED, wOID);
                }
                

                
                disableAssessedExperiencedToggle(wOID, isCallingFromFlowChart);                
                handleWOActionButtons(wOID, isCallingFromFlowChart);
            }
            else {
                
            }
        },
        error: function (xhr, status, statusText) {
            pwIsf.removeLayer();
        }
    });
}

//Call checkParallelWorkOrderDetails
function executeDisabledStep(StartTaskInput, isCallingFromFlowChart) {
   
    if (StartTaskInput.woID == null || StartTaskInput.woID.length == 0 || StartTaskInput.taskID == null || StartTaskInput.taskID.length == 0 || signumGlobal == null || signumGlobal.length == 0 || StartTaskInput.subActivityFlowChartDefID == null || StartTaskInput.subActivityFlowChartDefID.length == 0 || StartTaskInput.executionType == null || StartTaskInput.executionType.length == 0) {
        pwIsf.alert({ msg: 'Something went wrong!!', type: 'error' });

    }
    else {
        pwIsf.addLayer({ text: "Please wait ..." });
        if ((StartTaskInput.executionType == "Automatic" && rpaID != 0) || (StartTaskInput.executionType == "Manual") || (StartTaskInput.executionType == "ManualDisabled")) {

            var executionPlanObj = new Object();
            executionPlanObj.signumID = signumGlobal;
            executionPlanObj.isApproved = 0;
            executionPlanObj.executionType = StartTaskInput.type;
            executionPlanObj.woid = StartTaskInput.woID;
            executionPlanObj.taskid = StartTaskInput.taskID;
            executionPlanObj.projectID = StartTaskInput.prjID;
            executionPlanObj.versionNO = StartTaskInput.version;
            executionPlanObj.subActivityFlowChartDefID = StartTaskInput.subActivityFlowChartDefID;
            executionPlanObj.stepID = StartTaskInput.stepID;
            //var JsonObj = JSON.parse(executionPlanObj);

            $.isf.ajax({
                url: service_java_URL + "woExecution/checkParallelWorkOrderDetails/",
                type: "POST",
                data: JSON.stringify(executionPlanObj),
                contentType: "application/json",
                success: function (data) {

                    if (data.isValidationFailed == false) {
                        if (data.responseData.executeFlag == true) {

                            let stepDetails = new Object();
                            let stepDetailsToStart = new Object();
                            stepDetails.wOID = StartTaskInput.woID;
                            stepDetails.stepID = StartTaskInput.stepID;
                            stepDetails.taskID = StartTaskInput.taskID;
                            stepDetails.subActivityFlowChartDefID = StartTaskInput.subActivityFlowChartDefID;
                            stepDetails.signumID = signumGlobal;
                            stepDetails.decisionValue = '';
                            stepDetails.executionType = StartTaskInput.type;
                            stepDetails.botPlatform = "null";
                            stepDetails.stepName = "";
                            stepDetails.type = StartTaskInput.botType;
                            stepDetails.isCallingFromFlowchart = "";
                            stepDetails.outputUpload = StartTaskInput.outputUpload;
                            stepDetails.bookingType = "BOOKING";

                            stepDetailsToStart.serverBotJsonStr = stepDetails;
                            stepDetailsToStart.inputZip = '';
                            stepDetailsToStart.isRunOnServer = "";
                            stepDetailsToStart.isInputRequired = "";
                            stepDetailsToStart.flowChartType = stepDetails.flowChartType;
                           
                            startDisabledStep(stepDetailsToStart, isCallingFromFlowChart);

                        }
                    }
                    else {
                        pwIsf.removeLayer();
                        pwIsf.alert({ msg: data.formErrors[0], type: 'warning' });
                    }
                },
                error: function (xhr, status, statusText) {
                    var err = JSON.parse(xhr.responseText);
                    pwIsf.removeLayer();
                    pwIsf.alert({ msg: "error", type: 'error' });

                },
                complete: function () {
                }
            });
        }
        else {
            pwIsf.alert({ msg: "Incorrect RPAID configured in Automatic Step Or Execution type is incorrectly mapped.", type: 'error' });
        }


    }
    
}

//start step execution 
function startFlowChartTask(StartTaskInput) {
    if (StartTaskInput.woID == null || StartTaskInput.woID.length == 0 || StartTaskInput.taskID == null || StartTaskInput.taskID.length == 0 || signumGlobal == null || signumGlobal.length == 0 || StartTaskInput.subActivityFlowChartDefID == null || StartTaskInput.subActivityFlowChartDefID.length == 0 || StartTaskInput.executionType == null || StartTaskInput.executionType.length == 0) {
        pwIsf.alert({ msg: 'Something went wrong!!', type: 'error' });

    }
    else {
        var runningTask = "";
        window.parent.pwIsf.addLayer({ text: "Please wait ..." });
        if ((StartTaskInput.executionType == "Automatic" && rpaID != 0) || (StartTaskInput.executionType == "Manual") || (StartTaskInput.executionType == "ManualDisabled")) {

            var executionPlanObj = new Object();
            executionPlanObj.signumID = signumGlobal;
            executionPlanObj.isApproved = 0;
            executionPlanObj.executionType = StartTaskInput.type;
            executionPlanObj.woid = StartTaskInput.woID;
            executionPlanObj.taskid = StartTaskInput.taskID;
            executionPlanObj.projectID = StartTaskInput.prjID;
            executionPlanObj.versionNO = StartTaskInput.version;
            executionPlanObj.subActivityFlowChartDefID = StartTaskInput.subActivityFlowChartDefID;
            executionPlanObj.stepID = StartTaskInput.stepID;
            //var JsonObj = JSON.parse(executionPlanObj);

            $.isf.ajax({
                url: service_java_URL + "woExecution/checkParallelWorkOrderDetails/",
                type: "POST",
                data: JSON.stringify(executionPlanObj),
                contentType: "application/json",
                success: function (data) {

                    if (data.isValidationFailed == false) {
                        if (data.responseData.executeFlag == true) {
                            setTimeout(function () {
                                //if (StartTaskInput.next)
                                taskFlowChartStartAPICall(StartTaskInput.flowChartType, StartTaskInput.woID, StartTaskInput.taskID, StartTaskInput.subActivityFlowChartDefID, StartTaskInput.stepID, StartTaskInput.stepName, StartTaskInput.bookingStatus, StartTaskInput.booking_Id, StartTaskInput.botConfigData, StartTaskInput.botType, StartTaskInput.fileID, StartTaskInput.executionType, StartTaskInput.outputUpload, StartTaskInput.nextRunOnServerPassed, StartTaskInput.isInputRequiredPassed, StartTaskInput.isCallingFromFlowchart, StartTaskInput.isPlayNext, StartTaskInput.IscalledFromDecisionButton);
                            },0);
                        }
                    }
                    else {
                        pwIsf.removeLayer();
                        pwIsf.alert({ msg: data.formErrors[0], type: 'warning' });
                    }
                },
                error: function (xhr, status, statusText) {
                    var err = JSON.parse(xhr.responseText);
                    pwIsf.removeLayer();
                    pwIsf.alert({ msg: "error", type: 'error' });

                },
                complete: function () {
                }
            });
        }
        else {
            pwIsf.alert({ msg: "Incorrect RPAID configured in Automatic Step Or Execution type is incorrectly mapped.", type: 'error' });
            pwIsf.removeLayer();
        }


    }

}

function taskFlowChartStartAPICall(flowChartType, woID, taskID, subActivityFlowChartDefID, stepID, stepName, bookingStatus, booking_Id, botConfigData, botType, fileID, executionType, outputUpload, nextRunOnServerPassed, isInputRequiredPassed, isCallingFromFlowchart = true, isPlayNext, IscalledFromDecisionButton) {
    let botNestID = '';
    let botPlatform = 'null';
    $.isf.ajax({
        type: "GET",
        async: false,
        url: service_java_URL + "woExecution/checkIFPreviousStepCompleted/" + woID + "/" + taskID + "/" + subActivityFlowChartDefID + "/" + stepID,
        success: function (data) {
            let startRequest = {}; let stepDetails = new Object(); let stepDetailsToStart = new Object();
            if (data.responseData.allowed) {
                if (executionType == "Automatic") {
                    botPlatform = 'local';
                    if (botConfigData === null || botConfigData === undefined || botConfigData === '') {
                        stepDetailsToStart.botConfigJson = '';
                    }
                    else {
                        stepDetailsToStart.botConfigJson = botConfigData;
                    }
                }
                stepDetails.wOID = woID;
                stepDetails.stepID = stepID;
                stepDetails.taskID = taskID;
                stepDetails.subActivityFlowChartDefID = subActivityFlowChartDefID;
                stepDetails.signumID = signumGlobal;
                stepDetails.decisionValue = '';
                stepDetails.executionType = executionType;
                stepDetails.botPlatform = botPlatform;
                stepDetails.stepName = stepName;
                stepDetails.type = botType;
                stepDetails.isCallingFromFlowchart = isCallingFromFlowchart;
                stepDetails.outputUpload = outputUpload;

                (executionType == "Manual") ? stepDetails.bookingType = "BOOKING" : stepDetails.bookingType = "QUEUED";

                stepDetailsToStart.serverBotJsonStr = stepDetails;
                stepDetailsToStart.inputZip = '';
                stepDetailsToStart.isRunOnServer = nextRunOnServerPassed;
                stepDetailsToStart.isInputRequired = isInputRequiredPassed;
                stepDetailsToStart.flowChartType = flowChartType;

                startAnyStep(stepDetailsToStart, $('#botConfigModal'), isPlayNext, IscalledFromDecisionButton);

            }
            else {
                pwIsf.alert({ msg: " Please complete previous task/step. ", type: 'error' });
                pwIsf.removeLayer();
            }
        },
        error: function (xhr, status, statusText) {
            var err = JSON.parse(xhr.responseText);
            pwIsf.removeLayer();
            pwIsf.alert({ msg: err.errorMessage, type: 'error' });

        },

    });
}

//Decision step play 
function stepDetails(woID, stepID, subActivityFlowChartDefID, executionType, eleAttr) {

    executionType = (executionType == 'undefined') ? '' : executionType;
    let decidsionVal = eleAttr.labels[0].attrs.text.text;
    $.isf.ajax({
        type: "GET",
        async:false,
        url: `${service_java_URL}woExecution/checkIFPreviousStepCompleted/${woID}/0/${subActivityFlowChartDefID}/${stepID}`,
        success: function (data) {
            let stepDetails = new Object;
            stepDetails.woId = woID;
            stepDetails.taskID = 0;
            stepDetails.flowChartStepId = stepID;
            stepDetails.flowChartDefID = subActivityFlowChartDefID;
            stepDetails.signumId = signumGlobal;
            stepDetails.decisionValue = decidsionVal;
            stepDetails.executionType = "Manual";
            if (data.responseData.allowed) {
                $.isf.ajax({
                    type: "POST",
                    async: false,
                    contentType: "application/json; charset=utf-8",
                    data: JSON.stringify(stepDetails),
                    url: `${service_java_URL}woExecution/addStepDetailsForFlowChart`,
                    success: function (dataForStepDetails) {
                        if (dataForStepDetails.isValidationFailed == false) {
                            SetDecisionValue(stepID, decidsionVal, eleAttr, woID);
                        }
                        else {

                            pwIsf.alert({ msg: dataForStepDetails.formErrors, type: 'error' });
                        }
                       
                    },
                    error: function (xhr, status, statusText) {
                        
                        pwIsf.alert({ msg: xhr.formErrors, type: 'error' });
                    },
                });
            }
            else {
                pwIsf.alert({ msg: "Please complete previous task to start this.", type: 'warning' });
                //    alert("Please complete previous task to start this.");
            }
        },
        error: function (xhr, status, statusText) {
            var err = JSON.parse(xhr.responseText);
            pwIsf.alert({ msg: err.errorMessage, type: 'error' });
            pwIsf.removeLayer();
        }
    });
}

function SetDecisionValue(stepID, decidsionVal, eleAttr, woID) {
    var currentCell = '';
    var cellArray = [];
    app.graph.getCell(stepID).attr('label/text', app.graph.getCell(stepID).attr('label/text').split('\n')[0]);
    app.graph.getCell(stepID).attr('label/text', app.graph.getCell(stepID).attr("label/text") + "\n Selected:" + decidsionVal);
    let targetElement = app.graph.getCell(eleAttr).getTargetElement();

    let isNextDisabled = targetElement.attributes.executionType == C_EXECUTION_TYPE_MANUAL &&
        !targetElement.attributes.viewMode.includes(C_PROFICIENCY_EXPERIENCED);

    let proficiencyLevel = urlParams.get('proficiencyLevel');

    if (proficiencyLevel == 2 && isNextDisabled) {
        let disabledStepDetails = JSON.parse(window.parent.$('#woButtons_' + woID + ' .disabledStepObj').val());
        let targetId = disabledStepDetails.flowChartStepId;
        app.graph.getCell(eleAttr).set('hiddenselected', targetId);
    }
    else {
        app.graph.getCell(eleAttr).set('hiddenselected', targetElement.attributes.id);
    }
    cellArray = app.graph.toJSON().cells;
    let iconContent = new Object();
    iconContent.shape = eleAttr.type;
    iconContent.stepID = eleAttr.id;
    iconContent.currentCell = eleAttr;
    iconContent.currentJson = cellArray;
    iconContent.iconsOnNextStep = true;
    makeContentIcon(iconContent);
    cellArray = { "cells": cellArray };
    app.graph.fromJSON(cellArray);
}

//CHANGE STEP COLOR ON ANY ACTION
function changeStepColor(stepDetailsData) {

    let iconContent = new Object();
    //let reason = stepDetailsData.Reason;
    let status = stepDetailsData.status;
    let stepID = stepDetailsData.stepID;
    let hour = stepDetailsData.hour;
    // let botNestID = stepDetailsData.botNestID;
    let outputLink = stepDetailsData.outputLink;
    let bookingType = stepDetailsData.bookingType;
    let iconsOnNextStep = stepDetailsData.iconsOnNextStep;
    let flowChartType = stepDetailsData.flowChartType;
    let sadCount = '';
    //alert('call changeStepColor');
    var currentRunningPosition = '';
    var myObj;
    var activeCell = '';
    var shapeJson = app.graph.toJSON().cells;
    for (var shape = 0; shape < shapeJson.length; shape++) {
        if (shapeJson[shape].id == stepID) {
            activeCell = shapeJson[shape];
            currentShape = shapeJson[shape].type;
            currentTaskID = shapeJson[shape].attrs.task.taskID;
            currentTaskName = shapeJson[shape].attrs.task.taskName;
            bookingID = shapeJson[shape].attrs.task.bookingID;

            //let hour = shapeJson[shape].attrs.task.Hours;
            bookingStatus = status;
            var temp = [];
            stepid = shapeJson[shape].id;
            executionType = shapeJson[shape].attrs.task.executionType;
            sadCount = shapeJson[shape].sadCount;

            if (shapeJson[shape].attrs.bodyText.text.match(/(^|\W)Effort($|\W)/)) {
                temp = [];
                temp = shapeJson[shape].attrs.bodyText.text.split('\n\n');
                temp[2] = "Effort:" + hour;
                shapeJson[shape].attrs.bodyText.text = temp[0] + '\n\n' + temp[1] + '\n\n' + temp[2];
            }
            else {
                temp = [];
                temp = shapeJson[shape].attrs.bodyText.text.split('\n\n');
                //var effort = "Effort:" + hour;
                shapeJson[shape].attrs.bodyText.text = temp[0] + '\n\n' + temp[1] + '\n\n' + 'Effort:' + hour;
            }

            if (status === C_BOOKING_STATUS_STARTED) {

                shapeJson[shape].attrs.body.fill = "#7C68FC";
                shapeJson[shape].attrs.task.status = status;
                shapeJson[shape].attrs.bodyText.fill = "white";
                shapeJson[shape].bookingType = bookingType;
                shapeJson[shape].attrs.task.Hours = hour;
                // shapeJson[shape].botNestId = botNestID;

                myObj = { "cells": shapeJson };
                currentRunningPosition = shapeJson[shape].position;
            }
            else if (status === C_BOOKING_STATUS_COMPLETED) {

                if (executionType === C_EXECUTION_TYPE_AUTOMATIC) {

                    shapeJson[shape].attrs.body.fill = "#008000";
                    shapeJson[shape].attrs.bodyText.fill = "white";
                    shapeJson[shape].attrs.task.outputLink = outputLink;
                    //shapeJson[shape].attrs.task.reason = outputLink;
                    urlpath = shapeJson[shape].attrs.task.outputLink;
                    //if (outputLink !== undefined) {
                    //    urlpath = outputLink;
                    //} 
                    if ((urlpath != null) || (urlpath != undefined)) {
                        urlpath = urlpath.split("=")[1];
                    }
                }
                else {
                    shapeJson[shape].attrs.body.fill = "#008000";
                    shapeJson[shape].attrs.bodyText.fill = "white";

                }
                shapeJson[shape].attrs.task.Hours = hour;
                shapeJson[shape].attrs.task.status = status;
                myObj = { "cells": shapeJson }
                currentRunningPosition = shapeJson[shape].position;

            }
            else if (status === C_BOOKING_STATUS_SKIPPED) {

                // if (executionType == "Automatic") { //shapeJson[shape].attrs[".outer"].fill = "#FFAB73"; 
                shapeJson[shape].attrs.body.fill = "#FFAB73";
                shapeJson[shape].attrs.bodyText.fill = "black";
                shapeJson[shape].attrs.task.status = status;
                shapeJson[shape].attrs.task.Hours = hour;
                myObj = { "cells": shapeJson }
                currentRunningPosition = shapeJson[shape].position;

            }
            else if (status === C_BOOKING_STATUS_DEFAULT || status == "") {

                // if (executionType == "Automatic") { //shapeJson[shape].attrs[".outer"].fill = "#FFAB73"; 
                shapeJson[shape].attrs.body.fill = "#FFFFFF";
                shapeJson[shape].attrs.bodyText.fill = "black";
                shapeJson[shape].attrs.task.status = status;
                shapeJson[shape].attrs.task.Hours = hour;
                myObj = { "cells": shapeJson }
                currentRunningPosition = shapeJson[shape].position;

            }
            else {
                shapeJson[shape].attrs.bodyText.fill = "black";
                shapeJson[shape].attrs.body.fill = "#E5E500";
                shapeJson[shape].attrs.task.status = status;
                shapeJson[shape].attrs.task.Hours = hour;
                myObj = { "cells": shapeJson };

                currentRunningPosition = shapeJson[shape].position;

            }
            break;
        }

    }

    nextStepDetails(stepID);

    iconContent.taskName = currentTaskName;
    iconContent.shape = currentShape;
    iconContent.taskID = currentTaskID;
    iconContent.bookingID = bookingID;
    iconContent.status = bookingStatus;
    iconContent.stepID = stepid;
    iconContent.execType = executionType;
    iconContent.currentCell = activeCell;
    iconContent.currentJson = shapeJson;
    iconContent.iconsOnNextStep = iconsOnNextStep;
    iconContent.flowChartType = flowChartType;
    iconContent.sadCount = sadCount;
    if (iconContent.currentCell.startRule) {
        iconContent.startRule = iconContent.currentCell.startRule;
    }
    else {
        iconContent.startRule = false;
	}
    if (iconContent.currentCell.stopRule) {
        iconContent.stopRule = iconContent.currentCell.stopRule;
    } else {
        iconContent.stopRule = false;
	}
    iconContent.iscallingFromSteps = true;
    makeContentIcon(iconContent);
    app.graph.fromJSON(myObj);

    let getAllCells = app.graph.getCells();//.filter(x => x.attributes.type === "ericsson.Automatic" || x.attributes.type === "ericsson.Manual");

    let currentResizingStep = getAllCells.filter(function (el) { return el.attributes.id == stepID });

    if (status === C_BOOKING_STATUS_SKIPPED || status === C_BOOKING_STATUS_ONHOLD || status === C_BOOKING_STATUS_COMPLETED) {
        let outboundlink = app.graph.getConnectedLinks(currentResizingStep[0], { outbound: true });
        let woid = urlParams.get("woID");
        var nextStep = getNextStep(outboundlink[0], stepID, getAllCells, woid);
        if (nextStep) {
            if (nextStep.attributes.showIcons === true) { 
            }
        }
    }


    app.paperScroller.scroll(currentRunningPosition.x, currentRunningPosition.y);

    pwIsf.removeLayer();


}

//CHANGE DISABLE STEP COLOR
function changeDisabledManualStepColor(taskStatus, woID) {
    if (woID == undefined || woID == 'undefined' || woID == 'null' || woID == null) {
        if (taskStatus == C_BOOKING_STATUS_ONHOLD) {
            $('div.experiencedWOButtons').css('background-color', '#E5E500');
        } else {
            $('div.experiencedWOButtons').css('background-color', '#e4e6e9');
        }
    } else {
        if (taskStatus == C_BOOKING_STATUS_ONHOLD) {
            $('#woButtons_' + woID).css('background-color', '#E5E500');
        } else {
            $('#woButtons_' + woID).css('background-color', '#e4e6e9');
        }
	}
    

}
//stop step execution
function stopFlowChartTask(stopTaskInput) {

    if ($('#modalStopWF').hasClass('in')) {
        $('#modalStopWF').modal('hide');
    }
    var currentStatus = "";
    if (stopTaskInput.isPlayNext == true) {
        currentStatus = "PLAYNEXT";
    }
    else {
        currentStatus = "STOPPED";
    }


    if (stopTaskInput.woID == null || stopTaskInput.woID.length == 0 || stopTaskInput.taskID == null || stopTaskInput.taskID.length == 0 || signumGlobal == null || signumGlobal.length == 0) {
        pwIsf.alert({ msg: 'Something went wrong!!', type: 'error' });
    }
    else {

        let stopStepDetails = new Object();
        stopStepDetails.wOID = stopTaskInput.woID;
        stopStepDetails.taskID = stopTaskInput.taskID;
        stopStepDetails.stepID = stopTaskInput.stepID;
        stopStepDetails.subActivityFlowChartDefID = stopTaskInput.subActivityFlowChartDefID;
        stopStepDetails.signumID = signumGlobal;
        stopStepDetails.executionType = stopTaskInput.executionType;
        stopStepDetails.bookingID = stopTaskInput.booking_id;
        stopStepDetails.type = stopTaskInput.booking_type;
        stopStepDetails.reason = stopTaskInput.reason;
        stopStepDetails.decisionValue = '';
        stopStepDetails.refferer = 'ui';
        stopStepDetails.externalSourceName = 'ui';

        window.parent.pwIsf.addLayer({ text: "Please wait ..." });
        // let service_java_URL = "http://169.144.20.166:8080/isf-rest-server-java/";
        $.isf.ajax({
            async: stopTaskInput.asyncType,
            type: "POST",
            cors: true,
            contentType: 'application/json',
            data: JSON.stringify(stopStepDetails),
            url: service_java_URL + "woExecution/stopWorkOrderTask",
            //+ woID + "/" + taskID + "/" + stepID + "/" + subActivityFlowChartDefID + "/" + signumGlobal + "/ ''/" + executionType,
            success: function (data) {

                if (data.isSuccess) {
                    bookingStatus = C_BOOKING_STATUS_COMPLETED;
                    let stepDetailsData = new Object();

                    stepDetailsData.flowChartType = stopTaskInput.flowChartType;
                    //isCallingFromFlowchart - flag to know if stop step was called from flowchart or from inprogess timer strip. (true- not from strip.) 
                    if (stopTaskInput.isCallingFromFlowchart) {    
                        //if automatic step is not successfull 
                        if (data.playNext == false && stopTaskInput.executionType === C_EXECUTION_TYPE_AUTOMATIC) {
                            stepDetailsData.status = C_BOOKING_STATUS_DEFAULT;
                            stepDetailsData.hour = data.hour;
                            stepDetailsData.stepID = stopTaskInput.stepID;
                            stepDetailsData.iconsOnNextStep = data.playNext;


                        }
                        else { // if manual/auto step is successfully complete
                            //stopTaskInput.status = bookingStatus;
                            stepDetailsData.status = bookingStatus;
                            stepDetailsData.hour = data.hour;
                            stepDetailsData.stepID = stopTaskInput.stepID;
                            stepDetailsData.iconsOnNextStep = data.playNext;
                        }
                        changeStepColor(stepDetailsData)                        


                        // To Notify Floating Window and All opened tabs
                        if (stopTaskInput.isForceStop == true) {
                            window.parent.NotifyClientOnWorkOrderStatusChange(stepDetailsData, stopTaskInput.woID, null, true);

                        }

                        else if (stopTaskInput.isCalledFromSkip === true) {
                            window.parent.NotifyClientOnWorkOrderStatusChange(stepDetailsData, stopTaskInput.woID, null, false);

                        }
                        else {
                            var InProgressTasks = GetSavedAutoSenseData();
                            let autoSenseInputData = PrepareAutoSenseInputData(stopTaskInput.woID, stopTaskInput.subActivityFlowChartDefID, stopTaskInput.stepID, currentStatus, InProgressTasks);
                            window.parent.NotifyClientOnWorkOrderStatusChange(stepDetailsData, stopTaskInput.woID, autoSenseInputData, true);

                        }


                        window.parent.requestInprogressTasks();
                        window.parent.requestWorkorder();
                        window.parent.getWOpercentageAndPerTextFromAPI({ woId: stopTaskInput.woID });
                        if (stopTaskInput.nextStepType === C_STEP_TYPE_END) {
                            var workorderpanel = window.parent.$(".work_order_dBox_container").find('span').filter(function () { return ($(this).text().indexOf(stopTaskInput.woID) > -1) });
                            var markCompletedBtn = workorderpanel.closest('.panel-heading').find('div.pull-right.infoDropdown').find('a.view-completed[data-woid="' + stopTaskInput.woID + '"]');
                            window.parent.$(markCompletedBtn).click();
                        }
                    }
                    else {

                        if ($('#iframe_' + stopTaskInput.woID)[0] != undefined) {

                            if (data.playNext == false && stopTaskInput.executionType === C_EXECUTION_TYPE_AUTOMATIC) {
                                stepDetailsData.status = C_BOOKING_STATUS_DEFAULT;
                                stepDetailsData.hour = data.hour;
                                stepDetailsData.stepID = stopTaskInput.stepID;
                                stepDetailsData.iconsOnNextStep = data.playNext;
                            }
                            else {
                                stepDetailsData.status = bookingStatus;
                                stepDetailsData.hour = data.hour;
                                stepDetailsData.stepID = stopTaskInput.stepID;
                                stepDetailsData.iconsOnNextStep = data.playNext;
                            }

                            document.getElementById('iframe_' + stopTaskInput.woID).contentWindow.changeStepColor(stepDetailsData);
                        }



                        // To Notify Floating Window and All opened tabs
                        if (stopTaskInput.isForceStop == true) {
                            window.parent.NotifyClientOnWorkOrderStatusChange(stepDetailsData, stopTaskInput.woID, null, true);

                        }
                        else if (stopTaskInput.isCalledFromSkip === true) {
                            window.parent.NotifyClientOnWorkOrderStatusChange(stepDetailsData, stopTaskInput.woID, null, false);

                        }

                        else {
                          //  var InProgressTasks = JSON.parse(localStorage.getItem("AutoSenseData"));
                            var InProgressTasks = GetSavedAutoSenseData();
                            let autoSenseInputData = PrepareAutoSenseInputData(stopTaskInput.woID, stopTaskInput.subActivityFlowChartDefID, stopTaskInput.stepID, currentStatus, InProgressTasks);
                            window.parent.NotifyClientOnWorkOrderStatusChange(stepDetailsData, stopTaskInput.woID, autoSenseInputData, true);

                        }
                        // reloadFlowchartWindow(stopTaskInput.woID);
                        requestInprogressTasks();
                        requestWorkorder();
                        getWOpercentageAndPerTextFromAPI({ woId: stopTaskInput.woID });
                    }
                    handleWOActionButtons(stopTaskInput.woID, stopTaskInput.isCallingFromFlowchart);

                }
                else {
                    if (data.msg) {
                        location.reload();
                    }
                    //pwIsf.alert({ msg: data.Error+" please refresh the window", type: 'error' });
                }
            },
            complete: function () {
                stopTaskInput.callback();
            },
            error: function (xhr, status, statusText) {
                var errorMessage = JSON.parse(xhr.responseText).errorMessage;
                if (errorMessage == undefined || errorMessage == '') {
                    errorMessage = "Some issue occured. Please try again!"
                }
                pwIsf.alert({ msg: errorMessage, type: 'error' });
                //  pwIsf.removeLayer();
            }
        });
    }

}

//pause all woids.
function updateFlowChartTaskStatusForAll(arrPauseAll, status, reason) {

    if ((status === C_BOOKING_STATUS_SKIPPED || status === C_BOOKING_STATUS_ONHOLD) && (reason == null || reason.length == 0)) {
        pwIsf.alert({ msg: 'Please give a reason...', type: 'error' });
    }
    for (var i = 0; i < reason.length; i++) {
        if (reason.charAt(i) == "/") {
            // console.log(taskName.charAt(i));
            reason = reason.replace(reason.charAt(i), "@");
        }
    }
    $.isf.ajax({
        type: "POST",
        contentType: 'application/json',
        data: JSON.stringify(arrPauseAll),
        url: `${service_java_URL}woExecution/pauseAllWOTasks/${reason}/${signumGlobal}`,
        success: function (data) {
            if ('Success' in data) {
                pwIsf.alert({ msg: data.Success, type: 'info' });
                $("#modalPauseAll").modal("toggle");
                $('.flow_chart_main_panel .panel-body > .flow_chart_single_box').remove();
                $('.flowChartInfoMsg').show();
                clearTimeouts();
                let autoSenseInputDataArr = [];
                let tempArr = arrPauseAll.filter(x => x.workOrderAutoSenseEnabled === true);
                $.each(tempArr, function (i, k) {
                    autoSenseInputDataArr.push(getAutoSenseObject(tempArr[i].woID, tempArr[i].flowChartDefID, '0', C_SOURCEUI, signumGlobal, C_OVERRIDE_ACTION_SUSPEND, '', '0', null, null));
                });
                requestInprogressTasks();

                // reload all opened tabs and update floating window
                NotifyClientOnPauseAllWO(autoSenseInputDataArr);

                requestWorkorder();
               
            }
        },
        error: function (xhr, status, statusText) {
            pwIsf.alert({ msg: 'error', type: 'error' });
            $("#modalPauseAll").modal("toggle");
        }

    });
}

//pause one woid.
function updateFlowChartTaskStatus(flowChartType, woID, subActivityFlowChartDefID, stepID, taskID, bookingID, status, reason, executionType, isCallingFromFlowchart) {
    //$(".joint-popup").hide();
    var nextStartRule = null;
    var nextStopRule = null;
    var nextExecutionType = null;
    var nextStepType = null;
    var nextStepId = null;
  
 
    if (status === C_BOOKING_STATUS_SKIPPED && isCallingFromFlowchart === true) {
        let getAllCells = app.graph.getCells();
        let getAllFilteredCells = app.graph.getCells().filter(x => x.attributes.type === C_STEP_TYPE_AUTOMATIC || x.attributes.type === C_STEP_TYPE_MANUAL);
        let currentStep = getAllFilteredCells.filter(function (el) { return el.attributes.id == stepID });
        let currentCell = app.graph.getCell(stepID);
        var outboundlink = app.graph.getConnectedLinks(currentCell, { outbound: true });
        var nextStep = getNextStep(outboundlink[0], stepID, getAllCells, woID);
        var currentStartRule = currentStep[0].attributes.startRule;
        var currentStopRule = currentStep[0].attributes.stopRule; 
        if (nextStep) {
            nextStepType = nextStep.attributes.type;
            nextStepId = nextStep.attributes.id;
            if (nextStep.attributes.type === C_STEP_TYPE_AUTOMATIC || nextStep.attributes.type === C_STEP_TYPE_MANUAL) {
                nextStartRule = nextStep.attributes.startRule;
                nextStopRule = nextStep.attributes.stopRule;
                nextExecutionType = nextStep.attributes.executionType;
            }
        }

    }
   
    var refferer = 'ui';
    if (woID == null || woID.length == 0 || taskID == null || taskID.length == 0 || status == null || status.length == 0 || signumGlobal == null || signumGlobal.length == 0) {
        pwIsf.alert({ msg: 'Something went wrong!!', type: 'error' });
    }
    else {
        if (bookingID == "%BOOKING_ID%" || bookingID == "") {
            bookingID = 0;
        }
        if (reason != null) {
            for (var i = 0; i < reason.length; i++) {
                if (reason.charAt(i) == "/") {
                    // console.log(taskName.charAt(i));
                    reason = reason.replace(reason.charAt(i), "@");
                }
            }
        }

        let apiurl ='';
        if (isCallingFromFlowchart === true && status === C_BOOKING_STATUS_SKIPPED) {
          
            let proficiencyID = urlParams.get("proficiencyId");

            apiurl = "woExecution/updateBookingDetailsStatus/" + woID + "/" + signumGlobal + "/" + taskID + '/' + bookingID + '/' + status + '/' + reason + '/' + stepID + '/' + subActivityFlowChartDefID + '/' + refferer + '?proficiencyId='+proficiencyID;

        }
        else {
            apiurl = "woExecution/updateBookingDetailsStatus/" + woID + "/" + signumGlobal + "/" + taskID + '/' + bookingID + '/' + status + '/' + reason + '/' + stepID + '/' + subActivityFlowChartDefID + '/' + refferer;

        }

      
        pwIsf.addLayer({ text: "Please wait ..." });
        $.isf.ajax({
            type: "POST",
            url: service_java_URL + apiurl,
            success: function (data) {
                if ('Success' in data) {
                    let stepDetails = new Object;
                    stepDetails.woId = woID;
                    stepDetails.taskID = taskID;
                    stepDetails.flowChartStepId = stepID;
                    stepDetails.flowChartDefID = subActivityFlowChartDefID;
                    stepDetails.signumId = signumGlobal;
                    stepDetails.decisionValue = '';
                    stepDetails.executionType = executionType;

                    let stepDetailsData = new Object();
                    //let flowChartType = window.parent.$('.feedback_' + woID).data().feedbackdetails.flowChartType;
                    stepDetailsData.flowChartType = flowChartType;

                    if (isCallingFromFlowchart) {
                        $("#modalSkippedWF").modal("hide");
                        $("#commentSkippedWF").val('');
                        $("#commentSkippedWF").select2();
                        //getFlowChart(woID);

                        stepDetailsData.status = status;
                        stepDetailsData.hour = data.hour;
                        stepDetailsData.stepID = stepID;
                        stepDetailsData.iconsOnNextStep = true;
                        changeStepColor(stepDetailsData);
                        // To Notify Floating Window and All opened tabs
                        if (status === C_BOOKING_STATUS_SKIPPED) {
                            let autoSenseDataForDirectSkip = PrepareAutoSenseInputDataForSkip(woID, subActivityFlowChartDefID, stepID, taskID, status, executionType, nextStartRule, nextStopRule, currentStartRule, currentStopRule, nextExecutionType, nextStepType, nextStepId);
                            if (Object.keys(autoSenseDataForDirectSkip).length === 0 && bookingID == 0) {
                                window.parent.NotifyClientOnWorkOrderStatusChange(stepDetailsData, woID, null, false);

                            }
                            else {
                                window.parent.NotifyClientOnWorkOrderStatusChange(stepDetailsData, woID, autoSenseDataForDirectSkip, true);

                            }



                        }
                        else {
                            var InProgressTasks = GetSavedAutoSenseData();
                            let autoSenseInputData = PrepareAutoSenseInputData(woID, subActivityFlowChartDefID, stepID, status, InProgressTasks);
                            window.parent.NotifyClientOnWorkOrderStatusChange(stepDetailsData, woID, autoSenseInputData, true);


                        }
                        window.parent.requestInprogressTasks();
                        window.parent.requestWorkorder();
                        window.parent.getWOpercentageAndPerTextFromAPI({ woId: woID });
                        disableAssessedExperiencedToggle(woID);//Toggle assessed and qual
                    }
                    else {
                        if (status == C_BOOKING_STATUS_SKIPPED) { $("#modalSkipped").modal("hide"); }
                        else if (executionType === C_EXECUTION_TYPE_MANUALDISABLED && status === C_BOOKING_STATUS_ONHOLD) {
                            $("#modalDisabledStepPause").modal("hide");
                        }
                        else { $("#modalTaskHold").modal("hide"); }

                        if ($('#iframe_' + woID)[0] != undefined) {

                            stepDetailsData.status = status;
                            stepDetailsData.hour = data.hour;
                            stepDetailsData.stepID = stepID;
                            document.getElementById('iframe_' + woID).contentWindow.changeStepColor(stepDetailsData)
                        }


                        // To Notify Floating Window and All opened tabs

                        var InProgressTasks =GetSavedAutoSenseData();
                        let autoSenseInputData = PrepareAutoSenseInputData(woID, subActivityFlowChartDefID, stepID, status, InProgressTasks);
                        window.parent.NotifyClientOnWorkOrderStatusChange(stepDetailsData, woID, autoSenseInputData, true);
                        requestInprogressTasks();
                        requestWorkorder();
                        getWOpercentageAndPerTextFromAPI({ woId: woID });
                        disableAssessedExperiencedToggle(woID, false);


                    }
                    //  pwIsf.removeLayer();
                    handleWOActionButtons(woID, isCallingFromFlowchart);
                }
                else {
                    pwIsf.removeLayer();
                    pwIsf.alert({ msg: data.Error, type: 'error' });
                }
            },
            error: function (xhr, status, statustext) {
                pwIsf.removeLayer();
                var errorMessage = JSON.parse(xhr.responseText).errorMessage;
                if (errorMessage == undefined || errorMessage == '') {
                    errorMessage = "Some issue occured. Please try again!"
                }
                pwIsf.alert({ msg: errorMessage, type: 'error' });
            },
            complete: function () {
				pwIsf.removeLayer();
            }

        });
    }
}

function makeIconInElements(json2, lastModifiedStep, flowChartType) {

    let changedJson = ''; let link = [];
    let currentRunningPosition;
    let iconContent = new Object();
    let outBoundLinks = [];
    if (json2 != undefined) {
        changedJson = json2;

        changedJson.cells.forEach(function (cell) {
            iconContent.workOrderAutoSenseEnabled = isWorkOrderAutoSenseEnabled;
            iconContent.workFlowAutoSenseEnabled = isWorkFlowAutoSenseEnabled;
            if (cell.type === C_STEP_TYPE_MANUAL) {

                iconContent.taskName = cell.attrs.task.taskName;
                iconContent.shape = cell.type;
                iconContent.taskID = cell.attrs.task.taskID;
                iconContent.bookingID = cell.attrs.task.bookingID;
                iconContent.status = cell.attrs.task.status;
                iconContent.stepID = cell.id;
                iconContent.execType = cell.attrs.task.executionType;
                iconContent.currentCell = cell;
                iconContent.currentJson = changedJson.cells
                iconContent.flowChartType = flowChartType
                iconContent.sadCount = cell.sadCount;
                iconContent.viewMode = cell.viewMode;


                if (cell.startRule)
                    iconContent.startRule = cell.startRule;
                else
                    iconContent.startRule = false;

                if (cell.stopRule)
                    iconContent.stopRule = cell.stopRule;
                else
                    iconContent.stopRule = false;


                makeContentIcon(iconContent);

            }

            if (cell.type === C_STEP_TYPE_AUTOMATIC) {

                iconContent.taskName = cell.attrs.task.taskName;
                iconContent.shape = cell.type;
                iconContent.taskID = cell.attrs.task.taskID;
                iconContent.bookingID = cell.attrs.task.bookingID;
                iconContent.status = cell.attrs.task.status;
                iconContent.stepID = cell.id;
                iconContent.execType = cell.attrs.task.executionType;
                iconContent.currentCell = cell;
                iconContent.currentJson = changedJson.cells;
                iconContent.flowChartType = flowChartType;
                iconContent.sadCount = cell.sadCount;

                //if step is completed successfully show icon on nextstep.
                if (cell.attrs.task.status === C_BOOKING_STATUS_COMPLETED && cell.attrs.task.reason.toLowerCase() === C_SUCCESS) {
                    //outboundlink = app.graph.getConnectedLinks(cell, { outbound: true });
                    //let nextStep = getNextStep(outboundlink[0], cell.id, arrAllShapes);
                    iconContent.iconsOnNextStep = true;
                }
                else { iconContent.iconsOnNextStep = false; }

                makeContentIcon(iconContent);
            }

            if (cell.type === C_STEP_TYPE_DECISION) {

                outBoundLinks = app.graph.getConnectedLinks(cell, { outbound: true })
                for (let k in outBoundLinks) {
                    link = changedJson.cells.filter(function (el) { return el.id == outBoundLinks[k].get('id') });

                    iconContent.shape = link[0].type;
                    iconContent.stepID = link[0].id;
                    iconContent.currentCell = link[0];
                    iconContent.currentJson = changedJson.cells
     //               if (cell.attrs.label.text == "auto step 2\nSelected:label3") {
     //                   console.log(cell.attrs.label.text);
					//}
                    makeContentIcon(iconContent);
                }

            }

            if (cell.type === C_STEP_TYPE_APPLINK && app.graph.getCell(cell.id).getSourceElement().get('type') === C_STEP_TYPE_DECISION) {

                iconContent.shape = cell.type;
                iconContent.stepID = cell.id;
                iconContent.currentCell = cell;
                iconContent.currentJson = changedJson.cells;
                makeContentIcon(iconContent);

            }

        });
        app.graph.fromJSON(changedJson);

        //Resizing the cell if icons are outside the box.
        changedJson.cells.forEach(function (cell) {
            let getAllCells = app.graph.getCells();
            let currentResizingStep = getAllCells.filter(function (el) { return el.attributes.id == cell.id; });
            autoResizeForIcon(app, currentResizingStep[0]);

        });
        //app.layoutDirectedGraph();
        if (lastModifiedStep.attrs.task.status != C_BOOKING_STATUS_DEFAULT) {
            currentRunningPosition = lastModifiedStep.position;
            app.paperScroller.scroll(currentRunningPosition.x, currentRunningPosition.y);
        }
    }
}

//Get next step for Assessed WF
function getNextStepForAssessed(currCell, arrAllShapes) {
    let cell = app.graph.getCell(currCell.id);
    let srcEle = cell.getSourceElement();
    let targetEle = cell.getTargetElement();
    return arrAllShapes.filter(function (el) { return el.id == targetEle.id })[0];
}

//show icons on next Step.
function getNextStep(currCell, stepId, arrAllShapes, woid) {
    let nextStep;
    let proficiencyLevel = urlParams.get('proficiencyLevel');
    if (proficiencyLevel == 1) {
        nextStep = getNextStepForAssessed(currCell, arrAllShapes); 
    }
    else {
        let actualNextStep = getNextStepForAssessed(currCell, arrAllShapes);
        let isDisabledStep = actualNextStep.attributes ? actualNextStep.attributes.executionType == C_EXECUTION_TYPE_MANUAL && !actualNextStep.attributes.viewMode.includes(C_PROFICIENCY_EXPERIENCED)
            : actualNextStep.executionType == C_EXECUTION_TYPE_MANUAL && !actualNextStep.viewMode.includes(C_PROFICIENCY_EXPERIENCED);
        let disabledStepDetails = JSON.parse(window.parent.$('#woButtons_' + woid + ' .disabledStepObj').val());
        let disabledStepStatus = disabledStepDetails.status;
        let inprogressTaskObj = window.parent.$('#hiddenVal_' + woid)
        let disabledStarted = inprogressTaskObj.length != 0 ? (inprogressTaskObj.data('exectype') == C_EXECUTION_TYPE_MANUALDISABLED ? true : false) : false;
        let isDisabledStepCompleted = disabledStepStatus == null || disabledStepStatus == C_BOOKING_STATUS_STARTED ? false : true;

        //Check if next disabled step is executed once or not
        if (isDisabledStep) {
            if (isDisabledStepCompleted) {
                nextStep = getNextStepForExperienced(currCell, arrAllShapes);
            }
            else {
                return false;
            }
            
        }
        else {
            nextStep = getNextStepForExperienced(currCell, arrAllShapes);
        }
    }

    return nextStep;
}

function getPrevStep(currCell, currStepID, arrAllShapes) {

    let cell = app.graph.getCell(currCell.id);
    let srcEle = cell.getSourceElement();
    return arrAllShapes.filter(function (el) { return el.id == srcEle.id })[0];
}

function setNextStepDetails(cell, proficiencyLevel) {
    nextStepType = cell.type;
    targetID = cell.id;
    let woid = urlParams.get('woID');
    let isDisabled = proficiencyLevel == 2 ? nextStepType == C_STEP_TYPE_MANUAL && !cell.viewMode.includes(C_PROFICIENCY_EXPERIENCED) : false;

    if (isDisabled) {
        let disabledStepDetails = JSON.parse(window.parent.$('#woButtons_' + woid + ' .disabledStepObj').val());

        nextStepFlag = true;
        nextTaskID = disabledStepDetails.taskID;
        nextExecType = disabledStepDetails.executionType;
        nextBookingStatus = disabledStepDetails.status;
        nextBookingID = disabledStepDetails.bookingId;
        nextRPAID = cell.attrs.tool.RPAID;
        nextStepName = disabledStepDetails.stepName;
        nextBotType = cell.botType;
        nextRunOnServer = cell.attrs.tool.isRunOnServer;
        isInputRequiredNext = cell.attrs.tool.isInputRequired;
        nextOutputUpload = cell.outputUpload;
        nextStepID = cell.id;
        if (nextRPAID == "") { nextRPAID = 0; }
        nextBotConfigJson = cell.botConfigJson;
        if (nextBotConfigJson == undefined) { nextBotConfigJson = null; }
        $("#runOnServer").data('runOnServer', nextRunOnServer);


    }
    else if (nextStepType == 'ericsson.Manual' || nextStepType == 'ericsson.Automatic') {



        nextStepFlag = true;
        nextTaskID = cell.attrs.task.taskID;
        nextExecType = cell.attrs.task.executionType;
        nextBookingStatus = cell.attrs.task.status;
        nextBookingID = cell.attrs.task.bookingID;
        nextRPAID = cell.attrs.tool.RPAID;
        nextStepName = cell.name;
        nextBotType = cell.botType;
        nextRunOnServer = cell.attrs.tool.isRunOnServer;
        isInputRequiredNext = cell.attrs.tool.isInputRequired;
        nextOutputUpload = cell.outputUpload;
        nextStepID = cell.id;
        if (nextRPAID == "") { nextRPAID = 0; }
        nextBotConfigJson = cell.botConfigJson;
        if (nextBotConfigJson == undefined) { nextBotConfigJson = null; }
        $("#runOnServer").data('runOnServer', nextRunOnServer);
    }
    else if (nextStepType == 'ericsson.Decision') {
        nextStepFlag = true;
        nextStepID = targetID;
        if (cell.attrs.task != undefined) {
            nextTaskID = cell.attrs.task.taskID;
            nextExecType = cell.attrs.task.executionType;
            nextBookingStatus = cell.attrs.task.status;
            nextBookingID = cell.attrs.task.bookingID;
            nextRPAID = 0;
            nextBotConfigJson = null;
        }
        else {
            nextTaskID = "";
            nextExecType = null;
            nextBookingStatus = "";
            nextBookingID = "";
            nextRPAID = 0;
            nextBotConfigJson = null;
        }

    }
    else
        nextStepFlag = false;
}

//nextStep Execution from Play Next Button
function nextStepDetails(nxtStepID) {
    let proficiencyLevel = urlParams.get('proficiencyLevel');
    let nextStep;

    //check if WF proficiency is Assessed or Experienced
    //if (proficiencyLevel == 1) {
        for (var i = 0; i < arrLink.length; i++) {
            if (arrLink[i].source.id == nxtStepID) {
                targetID = arrLink[i].target.id;
                for (var j = 0; j < arrAllShapes.length; j++) {
                    if (targetID == arrAllShapes[j].id) {
                        nextStep = arrAllShapes[j];                        
                    }
                }
            }
        }
    setNextStepDetails(nextStep, proficiencyLevel);
}

function showTooltip(evt, text) {
    let tooltip = document.getElementById("tooltipText");
    tooltip.innerHTML = text;

    if (window.parent.$('#changeColumns').find('.fa').hasClass('fa-th-large')) {
        tooltip.style.left = evt.pageX + 10 + 'px';
        tooltip.style.top = evt.pageY + 10 + 'px';
    } else {
        tooltip.style.left = evt.pageX - 200 + 'px';
        tooltip.style.top = evt.pageY + 10 + 'px';
    }
    tooltip.style.display = "block";
}
function hideTooltip() {
    var tooltip = document.getElementById("tooltipText");
    tooltip.style.display = "none";
}

function addAsIconToStepOnUserAction(thatStep, thatIcon) {
    if (thatStep.executionType === C_EXECUTION_TYPE_MANUAL && isWorkFlowAutoSenseEnabled === true && urlParams.get('proficiencyLevel') == 1) {
        if (isWorkOrderAutoSenseEnabled === true) {

            if (thatStep.startRule == true && thatStep.stopRule == true) {
                thatIcon.fill = C_AS_STEP_ICONS_COLOR_ARRAY_OGRG[0];//'#ff9900';
                thatIcon.tooltip = C_AS_BOTH_RULE_TOOL_TIP_TEXT;
                if (thatStep.icons.length > 0) {
                    let autosenseArray = thatStep.icons.filter(x => x.name === C_AUTOSENSE.toLowerCase());
                    if (autosenseArray == undefined || autosenseArray.length == 0 || autosenseArray == null) {
                        thatStep.icons.unshift(thatIcon);
                    }
                } else {
                    thatStep.icons = [thatIcon];

                }

            }
            if (thatStep.startRule == true && thatStep.stopRule == false) {
                thatIcon.fill = C_AS_STEP_ICONS_COLOR_ARRAY_OGRG[1];//'#008000';
                thatIcon.tooltip = C_AS_START_RULE_TOOL_TIP_TEXT;
                if (thatStep.icons.length > 0) {
                    let autosenseArray = thatStep.icons.filter(x => x.name === C_AUTOSENSE.toLowerCase());
                    if (autosenseArray == undefined || autosenseArray.length == 0 || autosenseArray == null) {
                        thatStep.icons.unshift(thatIcon);
                    }
                } else {
                    thatStep.icons = [thatIcon];

                }
            }
            if (thatStep.startRule == false && thatStep.stopRule == true) {
                thatIcon.fill = C_AS_STEP_ICONS_COLOR_ARRAY_OGRG[2];//'#FF0000';
                thatIcon.tooltip = C_AS_STOP_RULE_TOOL_TIP_TEXT;
                if (thatStep.icons.length > 0) {
                    let autosenseArray = thatStep.icons.filter(x => x.name === C_AUTOSENSE.toLowerCase());
                    if (autosenseArray == undefined || autosenseArray.length == 0 || autosenseArray == null) {
                        thatStep.icons.unshift(thatIcon);
                    }
                } else {
                    thatStep.icons = [thatIcon];

                }

            }

        }
        if (isWorkOrderAutoSenseEnabled === false) {
            thatIcon.fill = C_AS_STEP_ICONS_COLOR_ARRAY_OGRG[3];//#a6a6a6
            if (thatStep.startRule == true && thatStep.stopRule == true) {
                thatIcon.tooltip = C_AS_BOTH_RULE_TOOL_TIP_TEXT;
                if (thatStep.icons.length > 0) {
                    let autosenseArray = thatStep.icons.filter(x => x.name === C_AUTOSENSE.toLowerCase());
                    if (autosenseArray == undefined || autosenseArray.length == 0 || autosenseArray == null) {
                        thatStep.icons.unshift(thatIcon);
                    }
                } else {
                    thatStep.icons = [thatIcon];

                }
            }
            if (thatStep.startRule == true && thatStep.stopRule == false) {
                thatIcon.tooltip = C_AS_START_RULE_TOOL_TIP_TEXT;
                if (thatStep.icons.length > 0) {
                    let autosenseArray = thatStep.icons.filter(x => x.name === C_AUTOSENSE.toLowerCase());
                    if (autosenseArray == undefined || autosenseArray.length == 0 || autosenseArray == null) {
                        thatStep.icons.unshift(thatIcon);
                    }
                } else {
                    thatStep.icons = [thatIcon];

                }
            }
            if (thatStep.startRule == false && thatStep.stopRule == true) {
                thatIcon.tooltip = C_AS_STOP_RULE_TOOL_TIP_TEXT;
                if (thatStep.icons.length > 0) {
                    let autosenseArray = thatStep.icons.filter(x => x.name === C_AUTOSENSE.toLowerCase());
                    if (autosenseArray == undefined || autosenseArray.length == 0 || autosenseArray == null) {
                        thatStep.icons.unshift(thatIcon);
                    }
                } else {
                    thatStep.icons = [thatIcon];

                }
            }

        }


    }
}

function addAutoSenseIndicator(iconContent, asIcon) {
    let currentShape = iconContent.shape;
    let cell = iconContent.currentCell;
    let thatIcon = Object.assign({}, asIcon);
    if (currentShape === C_STEP_TYPE_MANUAL && isWorkFlowAutoSenseEnabled === true) {
        if (isWorkOrderAutoSenseEnabled === true) {

            if (iconContent.startRule === true && iconContent.stopRule === true) {
                thatIcon.fill = C_AS_STEP_ICONS_COLOR_ARRAY_OGRG[0];//'#ff9900';
                thatIcon.tooltip = C_AS_BOTH_RULE_TOOL_TIP_TEXT;
                addAutoSenseIcon(cell, thatIcon);
            }
            if (iconContent.startRule === true && iconContent.stopRule === false) {
                thatIcon.fill = C_AS_STEP_ICONS_COLOR_ARRAY_OGRG[1]; //'#008000';
                thatIcon.tooltip = C_AS_START_RULE_TOOL_TIP_TEXT;
                addAutoSenseIcon(cell, thatIcon);
            }
            if (iconContent.startRule === false && iconContent.stopRule === true) {
                thatIcon.fill = C_AS_STEP_ICONS_COLOR_ARRAY_OGRG[2];//'#FF0000';
                thatIcon.tooltip = C_AS_STOP_RULE_TOOL_TIP_TEXT;
                addAutoSenseIcon(cell, thatIcon);

            }

        }
        if (isWorkOrderAutoSenseEnabled === false) {
            thatIcon.fill = C_AS_STEP_ICONS_COLOR_ARRAY_OGRG[3]; //'#a6a6a6';
            if (iconContent.startRule === true && iconContent.stopRule === true) {
                thatIcon.tooltip = C_AS_BOTH_RULE_TOOL_TIP_TEXT;
                addAutoSenseIcon(cell, thatIcon);
            }
            if (iconContent.startRule === true && iconContent.stopRule === false) {
                thatIcon.tooltip = C_AS_START_RULE_TOOL_TIP_TEXT;
                addAutoSenseIcon(cell, thatIcon);
            }
            if (iconContent.startRule === false && iconContent.stopRule === true) {
                thatIcon.tooltip = C_AS_STOP_RULE_TOOL_TIP_TEXT;
                addAutoSenseIcon(cell, thatIcon);
            }

        }

        //}
    }

}

function makeContentIcon(iconContent, srcDecisionId = '') {
    let currentTaskName = iconContent.taskName;
    let currentShape = iconContent.shape;
    let currentTaskID = iconContent.taskID;
    let bookingID = iconContent.bookingID;
    let bookingStatus = iconContent.status;
    let stepID = iconContent.stepID;
    let executionType = iconContent.execType;
    let cell = iconContent.currentCell;
    let arrAllShapes = iconContent.currentJson;
    let iconsOnNextStep = iconContent.iconsOnNextStep;
    let flowChartType = iconContent.flowChartType;
    let sadCount = iconContent.sadCount;
    let isWorkOrderAutoSenseEnabled = iconContent.workOrderAutoSenseEnabled;
    let isWorkFlowAutoSenseEnabled = iconContent.workFlowAutoSenseEnabled;
    let stepInstructionExist = iconContent.currentCell.isStepInstructionExist;
    let outputUpload = iconContent.currentCell.outputUpload;
    let viewMode = iconContent.viewMode;
    let autoSenseIcon = { src: '\uf0a3', fill: '#a6a6a6', tooltip: C_AS_DEFAULT_TOOL_TIP_TEXT, className: 'autosenseClass bottom-right', name: 'autosense', event: 'manualAutoSenseClick' };
    let cascadeInputIcon = { src: '\uf1f9', fill: 'green', tooltip: 'Cascaded Bot', className: '', name: '' };
    let makeCompleteIcon = { src: '\uf058', fill: 'white', tooltip: 'Mark as Completed', className: 'bottom-right', event: 'markAsCompleted', name: '' };

    let colorOfSmiley = 'white';
    if (sadCount == 1) { colorOfSmiley = 'red'; }
    let smileyIcon = { src: '\uf119', fill: colorOfSmiley, className: 'top-left', tooltip: 'Unsatisfied?', event: 'makeAsSad', 'textanchor': 'end', 'name': 'smiley' };

    let feedbackIcon = { src: '\uf075', fill: 'blue', className: 'top-left', tooltip: 'Leave a feedback', event: 'feedbackButtonClick', 'textanchor': 'end', 'name': 'feedback' }
    let outboundlink = []; let rpaFileDataParam; let botNestStartDataParam; let serverStartIcon;
    let inboundlink = [];
    let infoIcon = '';
    let woId = urlParams.get("woID");
    let woProficiency = window.parent.$('#togBtnForFlowChartViewMode_' + woId).is(":checked");

    if (currentShape === C_STEP_TYPE_AUTOMATIC || currentShape == 'ericsson.Manual' || currentShape == 'app.ericssonWeakEntity' || currentShape == 'app.ericssonStep') {

        let botDataParam = JSON.stringify({ currentShape: currentShape, executionType: executionType, avgEstdEffort: 0, taskId: currentTaskID, taskName: "", rpaId: "0", stepID: stepID, version: versionNo, projId: urlParams.get("prjID"), subActivityDefID: subActivityFlowChartDefID, subActId: urlParams.get("subActID") });
        let botStoreIcon = { src: '\uf032', tooltip: 'BOT Store', fill: 'black', className: 'view-bot-store bottom-right', details: botDataParam, name: '' };

        let startIcon = { src: '\uf04b', fill: 'blue', tooltip: 'Start', className: 'start bottom-left', event: 'startbuttonclick', name: '' };

        if (cell.botType.toLowerCase().includes('nest')) {
            let botNestStartDataParam = JSON.stringify({ flowChartType: flowChartType, isInputRequired: cell.attrs.tool.isInputRequired, isRunOnServer: cell.attrs.tool.isRunOnServer, woID: urlParams.get("woID"), configForBot: cell.configForBot, botConfigJson: cell.botConfigJson, botType: cell.botType, botNestId: cell.botNestId, projID: urlParams.get("prjID"), subActId: urlParams.get("subActID"), version: versionNo, taskID: currentTaskID, bookingStatus: bookingStatus, bookingId: bookingID, stepID: stepID, stepName: cell.name, subActivityDefID: subActivityFlowChartDefID, executionType: executionType, rpaID: rpaID, isCallingFromFlowchart: true });
            serverStartIcon = { src: '\uf109', fill: 'blue', tooltip: 'BotNest Start', className: 'serverstart bottom-right', details: botNestStartDataParam, name: '' };

        }
        else {
            let rpaFileDataParam = JSON.stringify({ flowChartType: flowChartType, isInputRequired: cell.attrs.tool.isInputRequired, isRunOnServer: cell.attrs.tool.isRunOnServer, woID: urlParams.get("woID"), projID: urlParams.get("prjID"), subActId: urlParams.get("subActID"), version: versionNo, botType: cell.botType, taskID: currentTaskID, bookingStatus: bookingStatus, stepID: stepID, stepName: cell.name, subActivityDefID: subActivityFlowChartDefID, executionType: executionType, rpaID: rpaID,outputUpload:'', isCallingFromFlowchart: true });
            serverStartIcon = { src: '\uf109', fill: 'blue', tooltip: 'Server Start', className: 'serverstart bottom-right', details: rpaFileDataParam, name: '' };

        }

        let stopIcon = { src: '\uf04d', fill: 'red', tooltip: 'Stop', className: 'stop bottom-left', event: 'stopbuttonclick', name: '' };
        if (cell.botType.toLowerCase().includes('nest')) {
            infoIcon = { src: '\uf129', fill: 'red', tooltip: currentTaskName + '\nBooking ID:' + cell.isfRefId, className: 'info bottom-right', name: '' };
        }
        else {
            infoIcon = { src: '\uf129', fill: 'red', tooltip: currentTaskName + '\n Booking ID:' + bookingID, className: 'info bottom-right', name: '' };
        }

        let skipDataParam = JSON.stringify({ flowChartType: flowChartType, statuswf: 'SKIPPED', modalname: "Skipped", stepID: stepID, executionType: executionType, taskname: currentTaskName, woID: urlParams.get("woID"), taskID: currentTaskID, bookingID: bookingID, subActivityDefID: subActivityFlowChartDefID, isWorkOrderAutoSenseEnabled: isWorkOrderAutoSenseEnabled, isWorkFlowAutoSenseEnabled: isWorkFlowAutoSenseEnabled });
        let skipIcon = { src: '\uf04e', fill: 'black', tooltip: 'Skip', className: 'view-skip-wf bottom-left', details: skipDataParam, name: '' };

        let skippedIcon = { src: '\uf05e', fill: 'black', tooltip: 'Skipped', name: '', className: 'bottom-right' };

        let holdDataParam = JSON.stringify({ flowChartType: flowChartType, statuswf: 'ONHOLD', modalname: "Hold", stepID: stepID, executionType: executionType, taskname: currentTaskName, woID: urlParams.get("woID"), taskID: currentTaskID, bookingID: bookingID, subActivityDefID: subActivityFlowChartDefID });
        let holdIcon = { src: '\uf04c', fill: 'black', tooltip: 'Pause', className: 'view-skip-wf bottom-left', details: holdDataParam, name: '' };

        let downloadIcon = { src: '\uf019', fill: 'red', tooltip: 'Download File', event: 'downloadbuttonclick', name: '', className: 'bottom-left' };
        let completeIcon = { src: '\uf00c', fill: 'green', tooltip: 'Completed', name: '', className: 'bottom-right' };
        let playNextStepIcon = { src: '\uf051', fill: 'blue', tooltip: 'Play Next', className: 'playnext bottom-left', event: 'playNextStep', name: '' };
        let playNextRelIcon = { src: '\uf051', fill: 'blue', tooltip: 'Play Next', className: 'playnext bottom-left', event: 'playNextRel', name: '' };


        let downloadDocIcon = { src: '\uf15c', fill: 'green', tooltip: 'Work Instructions', event: 'downloadworkinstructionclick', toggle: 'dropdown', details: holdDataParam, name: '', className: 'work-instruction dropdown-toggle top-right' }

        let liveStreamingIcon = { src: '\uf09e', fill: 'green', tooltip: 'View LiveStreaming', event: 'viewBOTLiveStreaming', name: '', className: 'bottom-right' }
        let downloadBOTNestIcon = { src: '\uf019', fill: 'green', tooltip: 'Download BOT NEST File', event: 'downloadBotNestFileClick', name: '', className: 'bottom-right' }

        if (woProficiency && cell.executionType == C_EXECUTION_TYPE_MANUAL && !cell.viewMode.includes(C_PROFICIENCY_EXPERIENCED)) {
            cell.icons = [botStoreIcon];
            if (cell.workInstruction || stepInstructionExist) { cell.icons.push(downloadDocIcon); }
            if (flowChartType.toLowerCase() != 'custom') { cell.icons.push(feedbackIcon, smileyIcon); }
            cell.showIcons = true;
        }
        else {
            if (((bookingStatus === C_BOOKING_STATUS_DEFAULT) || (bookingStatus == ""))) {


                if (executionType === C_EXECUTION_TYPE_MANUAL) {
                    if (cell.showIcons != undefined) {
                        cell.icons = [botStoreIcon, infoIcon, skipIcon, startIcon];
                        if (cell.workInstruction || stepInstructionExist) { cell.icons.push(downloadDocIcon); }
                        if (flowChartType.toLowerCase() != 'custom') { cell.icons.push(feedbackIcon, smileyIcon); }
                    }

                }
                else {
                    cell.icons = [infoIcon, skipIcon];
                    if ((cell.attrs.tool.isRunOnServer == '1') && (cell.botType.toLowerCase().includes('nest'))) { cell.icons.push(serverStartIcon); }
                    else if ((cell.attrs.tool.isRunOnServer == '1')) { cell.icons.push(serverStartIcon); cell.icons.push(startIcon); }
                    //else 
                    else if ((cell.attrs.tool.isRunOnServer != '1') || (cell.attrs.tool.isRunOnServer == undefined) || (cell.attrs.tool.isRunOnServer == null)) { cell.icons.push(startIcon); }

                    // if (cell.botType.toLowerCase().includes('nest')) { cell.icons.push(botNestStartIcon); }
                    if (cell.workInstruction || stepInstructionExist) { cell.icons.push(downloadDocIcon); }
                    if (cell.cascadeInput != null || cell.cascadeInput != undefined) {
                        if (cell.cascadeInput.toLowerCase() == "yes") { cell.icons.push(cascadeInputIcon); }
                    }

                    if (flowChartType.toLowerCase() != 'custom') { cell.icons.push(feedbackIcon, smileyIcon); }
                    //if automatic step execution  failed show icon on stop step. 
                    outboundlink = app.graph.getConnectedLinks(cell, { outbound: true });
                    var nextStep = getNextStep(outboundlink[0], cell.id, arrAllShapes, woId);
                    if (nextStep) {
                        if (nextStep.type == "ericsson.EndStep" && iconContent.iconsOnNextStep == false) {
                            nextStep.icons = [makeCompleteIcon];
                            //nextStep.showIcons = true;
                        }
                    }
                }

            }

            if ((bookingStatus === C_BOOKING_STATUS_STARTED)) {

                //Defining nextStepFlag to sync with previous functionality. Here nextStepFlag is for next enabled step
                let nextStepFlag = false;

                outboundlink = app.graph.getConnectedLinks(cell, { outbound: true });
                var nextStep = getNextStep(outboundlink[0], cell.id, arrAllShapes, woId);
                var nextStepType = nextStep.type;

                if (nextStepType == 'ericsson.Manual' || nextStepType == 'ericsson.Automatic')
                    nextStepFlag = true;
                else if (nextStepType == 'ericsson.Decision') 
                    nextStepFlag = true;
		        else
                    nextStepFlag = false;

                if (nextStepFlag) {
                    if (executionType === C_EXECUTION_TYPE_AUTOMATIC) {
                        if (nextStepType === C_STEP_TYPE_DECISION) {
                            if (cell.botType.toLowerCase().includes('nest')) { cell.icons = [liveStreamingIcon, infoIcon, skipIcon, stopIcon]; }
                            else { cell.icons = [infoIcon, skipIcon, stopIcon]; }

                            if (cell.workInstruction || stepInstructionExist) { cell.icons.push(downloadDocIcon); }
                            //if (cell.cascadeInput.toLowerCase() == "yes") { cell.icons.push(cascadeInputIcon); }

                            if (flowChartType.toLowerCase() != 'custom') { cell.icons.push(feedbackIcon, smileyIcon); }

                            cell.showIcons = true; //now added
                        }
                        else {
                            if (cell.botType.toLowerCase().includes('nest')) {
                                cell.icons = [liveStreamingIcon, infoIcon, skipIcon, stopIcon]; //playNextStepIcon
                                //if (flowChartType.toLowerCase() != 'custom') { cell.icons.push(feedbackIcon, smileyIcon); }
                            }

                            else { cell.icons = [infoIcon, skipIcon, stopIcon]; }//playNextStepIcon

                            if (flowChartType.toLowerCase() != 'custom') { cell.icons.push(feedbackIcon, smileyIcon); }

                            if (cell.workInstruction || stepInstructionExist) { cell.icons.push(downloadDocIcon); }
                            if (cell.cascadeInput != null || cell.cascadeInput != undefined) {
                                if (cell.cascadeInput.toLowerCase() == "yes") { cell.icons.push(cascadeInputIcon); }
                            }
                            cell.showIcons = true;

                        }
                    }
                    else {
                        if (nextStepType === C_STEP_TYPE_DECISION) {
                            cell.icons = [botStoreIcon, infoIcon, holdIcon, skipIcon, stopIcon, playNextStepIcon];
                            if (cell.workInstruction || stepInstructionExist) { cell.icons.push(downloadDocIcon); }

                            if (flowChartType.toLowerCase() != 'custom') { cell.icons.push(feedbackIcon, smileyIcon); }

                            cell.showIcons = true; // now added

                        }
                        else {
                            cell.icons = [botStoreIcon, infoIcon, holdIcon, skipIcon, stopIcon, playNextStepIcon];
                            if (cell.workInstruction || stepInstructionExist) { cell.icons.push(downloadDocIcon); }
                            if (flowChartType.toLowerCase() != 'custom') { cell.icons.push(feedbackIcon, smileyIcon); }

                            cell.showIcons = true;
                            if (nextStep) {
                                if ((nextStep.attrs.task.status === C_BOOKING_STATUS_DEFAULT) || (nextStep.attrs.task.status == "") || nextStep.type != C_STEP_TYPE_DECISION) { }
                                if (nextStep.type == "ericsson.EndStep") {
                                    nextStep.showIcons = false;

                                }
                                else {
                                    //if(nextStep.type=="ericsson.Automatic" && nextStep.attrs.tool.isRunOnServer=='1'){ cell.icons.push(serverStartIcon);}
                                    if (!nextStep.showIcons)//very first time false. Next Time no need to false
                                     nextStep.showIcons = false;

                                }
                            }
                        }
                    }

                }
                else {
                    if (executionType === C_EXECUTION_TYPE_AUTOMATIC) {
                        if (cell.botType.toLowerCase().includes('nest')) {
                            cell.icons = [liveStreamingIcon, stopIcon, skipIcon, infoIcon];
                            if (flowChartType.toLowerCase() != 'custom') { cell.icons.push(feedbackIcon, smileyIcon); }
                        }
                        else {
                            cell.icons = [stopIcon, skipIcon, infoIcon];
                            if (flowChartType.toLowerCase() != 'custom') { cell.icons.push(feedbackIcon, smileyIcon); }
                        }
                        if (cell.workInstruction || stepInstructionExist) { cell.icons.push(downloadDocIcon); }
                        if (cell.cascadeInput != null || cell.cascadeInput != undefined) {
                            if (cell.cascadeInput.toLowerCase() == "yes") { cell.icons.push(cascadeInputIcon); }
                        }
                        cell.showIcons = true;

                        if (nextStep) {

                            if (nextStep.type == "ericsson.EndStep") {
                                nextStep.showIcons = false;
                            }
                            else {
                                nextStep.showIcons = true;
                            }
                        }
                    }
                    else {
                        cell.icons = [infoIcon, holdIcon, skipIcon, stopIcon, playNextStepIcon];
                        if (cell.workInstruction || stepInstructionExist) { cell.icons.push(downloadDocIcon); }

                        if (flowChartType.toLowerCase() != 'custom') { cell.icons.push(feedbackIcon, smileyIcon); }

                        cell.showIcons = true;

                        if (nextStep) {

                            nextStep.showIcons = true;
                            if (nextStep.type == "ericsson.EndStep") {
                                nextStep.showIcons = false;

                            }

                        }
                    }
                }

            }

            if ((bookingStatus === C_BOOKING_STATUS_ONHOLD)) {

                if (executionType === C_EXECUTION_TYPE_AUTOMATIC) {
                    cell.icons = [infoIcon, skipIcon];

                    if ((cell.attrs.tool.isRunOnServer == '1') && (cell.botType.toLowerCase().includes('nest'))) { cell.icons.push(serverStartIcon); }
                    else if ((cell.attrs.tool.isRunOnServer == '1')) { cell.icons.push(serverStartIcon); cell.icons.push(startIcon); }
                    else if ((cell.attrs.tool.isRunOnServer == '0') || (cell.attrs.tool.isRunOnServer == undefined) || (cell.attrs.tool.isRunOnServer == null)) { cell.icons.push(startIcon); }
                    if (cell.cascadeInput != null || cell.cascadeInput != undefined) {
                        if (cell.cascadeInput.toLowerCase() == "yes") { cell.icons.push(cascadeInputIcon); }
                    }
                    if (flowChartType.toLowerCase() != 'custom') { cell.icons.push(feedbackIcon, smileyIcon); }

                    // else if (cell.cascadeInput.toLowerCase() == "yes") { cell.icons.push(cascadeInputIcon); }
                }
                else {
                    cell.icons = [botStoreIcon, infoIcon, skipIcon, startIcon];
                    if (cell.workInstruction || stepInstructionExist) { cell.icons.push(downloadDocIcon); }

                    if (flowChartType.toLowerCase() != 'custom') { cell.icons.push(feedbackIcon, smileyIcon); }
                }
                cell.showIcons = true;
                outboundlink = app.graph.getConnectedLinks(cell, { outbound: true });
                var nextStep = getNextStep(outboundlink[0], cell.id, arrAllShapes, woId);

                if (nextStep) {
                    // if(nextStep.type=="ericsson.Automatic" && nextStep.attrs.tool.isRunOnServer=='1'){ cell.icons.push(serverStartIcon);}
                    if (nextStep.type === C_STEP_TYPE_DECISION) {
                        let link = [];
                        outboundlink = app.graph.getConnectedLinks(nextStep, { outbound: true });
                        for (let m in outboundlink) {
                            link = arrAllShapes.filter(function (el) { return el.id == outboundlink[m].get('id') });
                            link[0].showIcons = true;
                        }
                    }
                    else if (nextStep.type == "ericsson.EndStep") {

                        nextStep.icons = [makeCompleteIcon];
                        nextStep.showIcons = true;
                    }
                    else {

                        if (nextStep.attrs.task.status === C_BOOKING_STATUS_DEFAULT && nextStep.executionType === C_EXECUTION_TYPE_MANUAL) {
                            let botDataParamForNextStep = JSON.stringify({ currentShape: currentShape, executionType: executionType, avgEstdEffort: 0, taskId: nextStep.attrs.task.taskID, taskName: nextStep.attrs.task.taskName, rpaId: "0", stepID: nextStep.id, version: versionNo, projId: urlParams.get("prjID"), subActivityDefID: subActivityFlowChartDefID, subActId: urlParams.get("subActID") });
                            let skipDataParamForNextStep = JSON.stringify({ flowChartType: flowChartType, statuswf: C_BOOKING_STATUS_SKIPPED, modalname: "Skipped", stepID: nextStep.id, executionType: nextStep.executionType, taskname: nextStep.attrs.task.taskName, woID: urlParams.get("woID"), taskID: nextStep.attrs.task.taskID, bookingID: nextStep.attrs.task.bookingID, subActivityDefID: subActivityFlowChartDefID });                           

                            let copySkipIcon = Object.assign({}, skipIcon);
                            let copybotStoreIcon = Object.assign({}, botStoreIcon);
                            let copysmileyIcon = Object.assign({}, smileyIcon);
                            let copyfeedbackIcon = Object.assign({}, feedbackIcon);
                            copySkipIcon.details = skipDataParamForNextStep;
                            copybotStoreIcon.details = botDataParamForNextStep;
                            colorOfSmiley = 'white';
                            if (nextStep.sadCount == 1) { colorOfSmiley = 'red'; }
                            copysmileyIcon.fill = colorOfSmiley;
                            nextStep.icons = [copybotStoreIcon, infoIcon, copySkipIcon, startIcon];
                            if (nextStep.workInstruction || nextStep.isStepInstructionExist) { nextStep.icons.push(downloadDocIcon); }
                            if (flowChartType.toLowerCase() != 'custom') { nextStep.icons.push(copyfeedbackIcon, copysmileyIcon); }
                            let thatAsIcon = Object.assign({}, autoSenseIcon);

                            addAsIconToStepOnUserAction(nextStep, thatAsIcon);

                        }

                        nextStep.showIcons = true;
                    }
                }
                else if (nextStep == false) { //Means next step is disabled and not started yet
                    let disabledStepDetails = JSON.parse(window.parent.$('#woButtons_' + woId + ' .disabledStepObj').val());
                    disabledStepDetails.enableStart = true;
                    disabledStepDetails = JSON.stringify(disabledStepDetails);

                    //update status to enable start button when handleWOActions function is called
                    window.parent.$('#woButtons_' + woId + ' .disabledStepObj').val(disabledStepDetails);

                    window.parent.$('#woButtons_' + woId + ' .startExperiencedWO').removeClass('disabledWOButton');
                }
            }

            if ((bookingStatus == C_BOOKING_STATUS_COMPLETED)) {
                if (executionType === C_EXECUTION_TYPE_MANUAL) {
                    cell.icons = [botStoreIcon, completeIcon, infoIcon, startIcon];

                    if (cell.workInstruction || stepInstructionExist) { cell.icons.push(downloadDocIcon); }

                    if (flowChartType.toLowerCase() != 'custom') { cell.icons.push(feedbackIcon, smileyIcon); }

                    cell.showIcons = true;
                    outboundlink = app.graph.getConnectedLinks(cell, { outbound: true });
                    var nextStep = getNextStep(outboundlink[0], cell.id, arrAllShapes, woId);
                    if (nextStep) {
                        if (nextStep.type === C_STEP_TYPE_DECISION) {
                            let link = [];
                            outboundlink = app.graph.getConnectedLinks(nextStep, { outbound: true });
                            for (let m in outboundlink) {
                                link = arrAllShapes.filter(function (el) { return el.id == outboundlink[m].get('id') });
                                link[0].showIcons = true;
                            }
                        }
                        else if (nextStep.type === "ericsson.EndStep") {

                            nextStep.icons = [makeCompleteIcon];
                            nextStep.showIcons = true;
                        }
                        else {
                            if (nextStep.attrs.task.status === C_BOOKING_STATUS_DEFAULT && nextStep.executionType === C_EXECUTION_TYPE_MANUAL) {
                                let botDataParamForNextStep = JSON.stringify({ currentShape: currentShape, executionType: executionType, avgEstdEffort: 0, taskId: nextStep.attrs.task.taskID, taskName: nextStep.attrs.task.taskName, rpaId: "0", stepID: nextStep.id, version: versionNo, projId: urlParams.get("prjID"), subActivityDefID: subActivityFlowChartDefID, subActId: urlParams.get("subActID") });
                                let skipDataParamForNextStep = JSON.stringify({ flowChartType: flowChartType, statuswf: C_BOOKING_STATUS_SKIPPED, modalname: "Skipped", stepID: nextStep.id, executionType: nextStep.executionType, taskname: nextStep.attrs.task.taskName, woID: urlParams.get("woID"), taskID: nextStep.attrs.task.taskID, bookingID: nextStep.attrs.task.bookingID, subActivityDefID: subActivityFlowChartDefID }); 

                                let copySkipIcon = Object.assign({}, skipIcon);
                                let copybotStoreIcon = Object.assign({}, botStoreIcon);
                                let copysmileyIcon = Object.assign({}, smileyIcon);
                                let copyfeedbackIcon = Object.assign({}, feedbackIcon);
                                copySkipIcon.details = skipDataParamForNextStep;
                                copybotStoreIcon.details = botDataParamForNextStep;
                                colorOfSmiley = 'white';
                                if (nextStep.sadCount == 1) { colorOfSmiley = 'red'; }
                                copysmileyIcon.fill = colorOfSmiley;
                                nextStep.icons = [copybotStoreIcon, infoIcon, copySkipIcon, startIcon];
                                if (nextStep.workInstruction || nextStep.isStepInstructionExist) { nextStep.icons.push(downloadDocIcon); }
                                if (flowChartType.toLowerCase() != 'custom') { nextStep.icons.push(copyfeedbackIcon, copysmileyIcon); }
                                let thatAsIcon = Object.assign({}, autoSenseIcon);
                                nextStep.shape = iconContent.shape;
                                nextStep.currentCell = iconContent.currentCell;
                                addAsIconToStepOnUserAction(nextStep, thatAsIcon);

                            }

                            nextStep.showIcons = true;

                        }

                    }
                    else if (nextStep == false) { //Means next step is disabled and not started yet
                        let disabledStepDetails = JSON.parse(window.parent.$('#woButtons_' + woId + ' .disabledStepObj').val());
                        disabledStepDetails.enableStart = true;
                        disabledStepDetails = JSON.stringify(disabledStepDetails);

                        //update status to enable start button when handleWOActions function is called
                        window.parent.$('#woButtons_' + woId + ' .disabledStepObj').val(disabledStepDetails);

                        window.parent.$('#woButtons_' + woId + ' .startExperiencedWO').removeClass('disabledWOButton');
                    }
                    //    }
                }
                else if ((executionType === C_EXECUTION_TYPE_AUTOMATIC) && (cell.attrs.task.outputLink != undefined && cell.attrs.task.outputLink != "null")) {
                    urlpath = cell.attrs.task.outputLink;
                    cell.icons = [completeIcon, infoIcon];

                    
                    if ((cell.attrs.tool.isRunOnServer == '1') && (cell.botType.toLowerCase().includes('nest'))) {
                        cell.icons.push(serverStartIcon);
                        cell.icons.push(downloadBOTNestIcon);
                        cell.icons.push(downloadIcon);
                    }
                    else if ((cell.attrs.tool.isRunOnServer == '1')) {
                        cell.icons.push(serverStartIcon);
                        cell.icons.push(startIcon);

                        if (urlpath != '') {
                            cell.icons.push(downloadIcon);
                            cell.outputUpload = cell.attrs.tool.outputUpload;
                        }
                        else { cell.outputUpload = cell.attrs.tool.outputUpload; }
                    }
                    else if ((cell.attrs.tool.isRunOnServer == '0') || (cell.attrs.tool.isRunOnServer == undefined) || (cell.attrs.tool.isRunOnServer == null)) {
                        cell.icons.push(startIcon);
                        if (urlpath != '') {
                            cell.outputUpload = cell.attrs.tool.outputUpload;
                        }
                        else { cell.outputUpload = cell.attrs.tool.outputUpload; }
                    }
                    if ((cell.attrs.tool.isRunOnServer == '0') && ((cell.outputUpload == "Yes") || (cell.outputUpload == "YES"))) { cell.icons.push(downloadIcon); }

                    if (cell.workInstruction || stepInstructionExist) { cell.icons.push(downloadDocIcon); }
                    if (cell.cascadeInput != null || cell.cascadeInput != undefined) {
                        if (cell.cascadeInput.toLowerCase() == "yes") { cell.icons.push(cascadeInputIcon); }
                    }
                    //if Step is not successfully completed. 
                    if (flowChartType.toLowerCase() != 'custom') { cell.icons.push(feedbackIcon, smileyIcon); }
                    cell.showIcons = true;
                    outboundlink = app.graph.getConnectedLinks(cell, { outbound: true });
                    let nextStep = getNextStep(outboundlink[0], cell.id, arrAllShapes, woId);

                    if (iconsOnNextStep) {
                        if (nextStep) {
                            if (nextStep.type == "ericsson.Decision") {
                                let link = [];
                                outboundlink = app.graph.getConnectedLinks(nextStep, { outbound: true });
                                for (let m in outboundlink) {
                                    link = arrAllShapes.filter(function (el) { return el.id == outboundlink[m].get('id') });
                                    link[0].showIcons = true;
                                }
                            }
                            else if (nextStep.type == "ericsson.EndStep") {
                                nextStep.icons = [makeCompleteIcon];
                                nextStep.showIcons = true;
                            }
                            else {
                                if (nextStep.attrs.task.status === C_BOOKING_STATUS_DEFAULT && nextStep.executionType === C_EXECUTION_TYPE_MANUAL) {
                                    let botDataParamForNextStep = JSON.stringify({ currentShape: currentShape, executionType: nextStep.executionType, avgEstdEffort: 0, taskId: nextStep.attrs.task.taskID, taskName: nextStep.attrs.task.taskName, rpaId: "0", stepID: nextStep.id, version: versionNo, projId: urlParams.get("prjID"), subActivityDefID: subActivityFlowChartDefID, subActId: urlParams.get("subActID") });
                                    let skipDataParamForNextStep = JSON.stringify({ flowChartType: flowChartType, statuswf: C_BOOKING_STATUS_SKIPPED, modalname: "Skipped", stepID: nextStep.id, executionType: nextStep.executionType, taskname: nextStep.attrs.task.taskName, woID: urlParams.get("woID"), taskID: nextStep.attrs.task.taskID, bookingID: nextStep.attrs.task.bookingID, subActivityDefID: subActivityFlowChartDefID });

                                    let copySkipIcon = Object.assign({}, skipIcon);
                                    let copybotStoreIcon = Object.assign({}, botStoreIcon);
                                    let copysmileyIcon = Object.assign({}, smileyIcon);
                                    let copyfeedbackIcon = Object.assign({}, feedbackIcon);

                                    copySkipIcon.details = skipDataParamForNextStep;
                                    copybotStoreIcon.details = botDataParamForNextStep;
                                    let defaultColorOfSmiley = 'white';
                                    if (nextStep.sadCount == 1) { defaultColorOfSmiley = 'red'; }
                                    copysmileyIcon.fill = defaultColorOfSmiley;

                                    nextStep.icons = [copybotStoreIcon, infoIcon, copySkipIcon, startIcon];
                                    if (nextStep.workInstruction || nextStep.isStepInstructionExist) { nextStep.icons.push(downloadDocIcon); }
                                    if (flowChartType.toLowerCase() != 'custom') { nextStep.icons.push(copyfeedbackIcon, copysmileyIcon); }
                                    let thatAsIcon = Object.assign({}, autoSenseIcon);
                                    nextStep.shape = iconContent.shape;
                                    nextStep.currentCell = iconContent.currentCell;
                                    addAsIconToStepOnUserAction(nextStep, thatAsIcon);

                                } else if (nextStep.attrs.task.status === C_BOOKING_STATUS_DEFAULT && nextStep.executionType === C_EXECUTION_TYPE_AUTOMATIC) {
                                    let copyServerStartIcon = Object.assign({}, serverStartIcon);
                                    if (nextStep.botType.toLowerCase().includes('nest')) {
                                        copyServerStartIcon.details = JSON.stringify({ flowChartType: flowChartType, isInputRequired: nextStep.attrs.tool.isInputRequired, isRunOnServer: nextStep.attrs.tool.isRunOnServer, woID: urlParams.get("woID"), configForBot: nextStep.configForBot, botConfigJson: nextStep.botConfigJson, botType: nextStep.botType, botNestId: nextStep.botNestId, projID: urlParams.get("prjID"), subActId: urlParams.get("subActID"), version: versionNo, taskID: nextStep.attrs.task.taskID, bookingStatus: nextStep.bookingType, bookingId: nextStep.attrs.task.bookingID, stepID: nextStep.id, stepName: nextStep.name, subActivityDefID: subActivityFlowChartDefID, executionType: nextStep.executionType, rpaID: nextStep.rpaid, isCallingFromFlowchart: true });
                                    }
                                    else {
                                        copyServerStartIcon.details = JSON.stringify({ flowChartType: flowChartType, isInputRequired: nextStep.attrs.tool.isInputRequired, isRunOnServer: nextStep.attrs.tool.isRunOnServer, woID: urlParams.get("woID"), projID: urlParams.get("prjID"), subActId: urlParams.get("subActID"), version: versionNo, botType: nextStep.botType, taskID: nextStep.attrs.task.taskID, bookingStatus: nextStep.bookingType, stepID: nextStep.id, stepName: nextStep.name, subActivityDefID: subActivityFlowChartDefID, executionType: nextStep.executionType, rpaID: nextStep.rpaid, outputUpload: outputUpload, isCallingFromFlowchart: true });

                                    }
                                    let botDataParamForNextStep = JSON.stringify({ currentShape: currentShape, executionType: nextStep.executionType, avgEstdEffort: 0, taskId: nextStep.attrs.task.taskID, taskName: nextStep.attrs.task.taskName, rpaId: "0", stepID: nextStep.id, version: versionNo, projId: urlParams.get("prjID"), subActivityDefID: subActivityFlowChartDefID, subActId: urlParams.get("subActID") });
                                    let skipDataParamForNextStep = JSON.stringify({ flowChartType: flowChartType, statuswf: C_BOOKING_STATUS_SKIPPED, modalname: "Skipped", stepID: nextStep.id, executionType: nextStep.executionType, taskname: nextStep.attrs.task.taskName, woID: urlParams.get("woID"), taskID: nextStep.attrs.task.taskID, bookingID: nextStep.attrs.task.bookingID, subActivityDefID: subActivityFlowChartDefID });


                                    let copySkipIcon = Object.assign({}, skipIcon);
                                    let copybotStoreIcon = Object.assign({}, botStoreIcon);
                                    let copysmileyIcon = Object.assign({}, smileyIcon);
                                    let copyfeedbackIcon = Object.assign({}, feedbackIcon);

                                    copySkipIcon.details = skipDataParamForNextStep;
                                    copybotStoreIcon.details = botDataParamForNextStep;
                                    let defaultColorOfSmiley = 'white';
                                    if (nextStep.sadCount == 1) { defaultColorOfSmiley = 'red'; }
                                    copysmileyIcon.fill = defaultColorOfSmiley;

                                    nextStep.icons = [infoIcon, copySkipIcon];
                                    if ((nextStep.attrs.tool.isRunOnServer == '1') && (nextStep.botType.toLowerCase().includes('nest'))) { nextStep.icons.push(copyServerStartIcon); }
                                    else if ((nextStep.attrs.tool.isRunOnServer == '1')) { nextStep.icons.push(copyServerStartIcon); nextStep.icons.push(startIcon); }
                                    //else 
                                    else if ((nextStep.attrs.tool.isRunOnServer == '0')) { nextStep.icons.push(startIcon); }
                                    if (nextStep.workInstruction || nextStep.isStepInstructionExist) { nextStep.icons.push(downloadDocIcon); }
                                    if (nextStep.cascadeInput != null || nextStep.cascadeInput != undefined) {
                                        if (nextStep.cascadeInput.toLowerCase() == "yes") { nextStep.icons.push(cascadeInputIcon); }
                                    }

                                    if (flowChartType.toLowerCase() != 'custom') { nextStep.icons.push(feedbackIcon, copysmileyIcon); }

                                }
                                nextStep.showIcons = true;
                            }
                        }
                        else if (nextStep == false) { //Means next step is disabled and not started yet
                            let disabledStepDetails = JSON.parse(window.parent.$('#woButtons_' + woId + ' .disabledStepObj').val());
                            disabledStepDetails.enableStart = true;
                            disabledStepDetails = JSON.stringify(disabledStepDetails);

                            //update status to enable start button when handleWOActions function is called
                            window.parent.$('#woButtons_' + woId + ' .disabledStepObj').val(disabledStepDetails);

                            window.parent.$('#woButtons_' + woId + ' .startExperiencedWO').removeClass('disabledWOButton');
                        }
                    }
                }
                else {
                    cell.icons = [completeIcon, infoIcon];


                    if ((cell.attrs.tool.isRunOnServer == '1') && (cell.botType.toLowerCase().includes('nest'))) { cell.icons.push(serverStartIcon); cell.icons.push(downloadBOTNestIcon); }
                    else if ((cell.attrs.tool.isRunOnServer == '1')) { cell.icons.push(serverStartIcon); cell.icons.push(startIcon); }
                    else if ((cell.attrs.tool.isRunOnServer == '0')) { cell.icons.push(startIcon); }

                    if (cell.workInstruction || stepInstructionExist) { cell.icons.push(downloadDocIcon); }
                    if (cell.cascadeInput != null || cell.cascadeInput != undefined) {
                        if (cell.cascadeInput.toLowerCase() == "yes") { cell.icons.push(cascadeInputIcon); }
                    }
                    if (flowChartType.toLowerCase() != 'custom') { cell.icons.push(feedbackIcon, smileyIcon); }

                    cell.showIcons = true;
                    outboundlink = app.graph.getConnectedLinks(cell, { outbound: true });
                    var nextStep = getNextStep(outboundlink[0], cell.id, arrAllShapes, woId);
                    if (iconsOnNextStep) {
                        if (nextStep) {
                            if (nextStep.type == "ericsson.Decision") {
                                let link = [];
                                outboundlink = app.graph.getConnectedLinks(nextStep, { outbound: true });
                                for (let m in outboundlink) {
                                    link = arrAllShapes.filter(function (el) { return el.id == outboundlink[m].get('id') });
                                    link[0].showIcons = true;
                                }
                            }
                            else if (nextStep.type == "ericsson.EndStep") {
                                nextStep.icons = [makeCompleteIcon];
                                nextStep.showIcons = true;
                            }
                            else {
                                if (nextStep.attrs.task.status === C_BOOKING_STATUS_DEFAULT && nextStep.executionType === C_EXECUTION_TYPE_MANUAL) {
                                    let botDataParamForNextStep = JSON.stringify({ currentShape: currentShape, executionType: nextStep.executionType, avgEstdEffort: 0, taskId: nextStep.attrs.task.taskID, taskName: nextStep.attrs.task.taskName, rpaId: "0", stepID: nextStep.id, version: versionNo, projId: urlParams.get("prjID"), subActivityDefID: subActivityFlowChartDefID, subActId: urlParams.get("subActID") });
                                    let skipDataParamForNextStep = JSON.stringify({ flowChartType: flowChartType, statuswf: C_BOOKING_STATUS_SKIPPED, modalname: "Skipped", stepID: nextStep.id, executionType: nextStep.executionType, taskname: nextStep.attrs.task.taskName, woID: urlParams.get("woID"), taskID: nextStep.attrs.task.taskID, bookingID: nextStep.attrs.task.bookingID, subActivityDefID: subActivityFlowChartDefID });

                                    let copySkipIcon = Object.assign({}, skipIcon);
                                    let copybotStoreIcon = Object.assign({}, botStoreIcon);
                                    let copysmileyIcon = Object.assign({}, smileyIcon);
                                    let copyfeedbackIcon = Object.assign({}, feedbackIcon);

                                    copySkipIcon.details = skipDataParamForNextStep;
                                    copybotStoreIcon.details = botDataParamForNextStep;
                                    let defaultColorOfSmiley = 'white';
                                    if (nextStep.sadCount == 1) { defaultColorOfSmiley = 'red'; }
                                    copysmileyIcon.fill = defaultColorOfSmiley;

                                    nextStep.icons = [copybotStoreIcon, infoIcon, copySkipIcon, startIcon];
                                    if (nextStep.workInstruction || nextStep.isStepInstructionExist) { nextStep.icons.push(downloadDocIcon); }
                                    if (flowChartType.toLowerCase() != 'custom') { nextStep.icons.push(copyfeedbackIcon, copysmileyIcon); }
                                    let thatAsIcon = Object.assign({}, autoSenseIcon);
                                    nextStep.shape = iconContent.shape;
                                    nextStep.currentCell = iconContent.currentCell;
                                    addAsIconToStepOnUserAction(nextStep, thatAsIcon);

                                }
                                else if (nextStep.attrs.task.status === C_BOOKING_STATUS_DEFAULT && nextStep.executionType === C_EXECUTION_TYPE_AUTOMATIC) {
                                    let copyServerStartIcon = Object.assign({}, serverStartIcon);
                                    if (nextStep.botType.toLowerCase().includes('nest')) {
                                        copyServerStartIcon.details = JSON.stringify({ flowChartType: flowChartType, isInputRequired: nextStep.attrs.tool.isInputRequired, isRunOnServer: nextStep.attrs.tool.isRunOnServer, woID: urlParams.get("woID"), configForBot: nextStep.configForBot, botConfigJson: nextStep.botConfigJson, botType: nextStep.botType, botNestId: nextStep.botNestId, projID: urlParams.get("prjID"), subActId: urlParams.get("subActID"), version: versionNo, taskID: nextStep.attrs.task.taskID, bookingStatus: nextStep.bookingType, bookingId: nextStep.attrs.task.bookingID, stepID: nextStep.id, stepName: nextStep.name, subActivityDefID: subActivityFlowChartDefID, executionType: nextStep.executionType, rpaID: nextStep.rpaid, isCallingFromFlowchart: true });
                                    }
                                    else {
                                        copyServerStartIcon.details = JSON.stringify({ flowChartType: flowChartType, isInputRequired: nextStep.attrs.tool.isInputRequired, isRunOnServer: nextStep.attrs.tool.isRunOnServer, woID: urlParams.get("woID"), projID: urlParams.get("prjID"), subActId: urlParams.get("subActID"), version: versionNo, botType: nextStep.botType, taskID: nextStep.attrs.task.taskID, bookingStatus: nextStep.bookingType, stepID: nextStep.id, stepName: nextStep.name, subActivityDefID: subActivityFlowChartDefID, executionType: nextStep.executionType, rpaID: nextStep.rpaid, outputUpload: outputUpload, isCallingFromFlowchart: true });

                                    }
                                    let botDataParamForNextStep = JSON.stringify({ currentShape: currentShape, executionType: nextStep.executionType, avgEstdEffort: 0, taskId: nextStep.attrs.task.taskID, taskName: nextStep.attrs.task.taskName, rpaId: "0", stepID: nextStep.id, version: versionNo, projId: urlParams.get("prjID"), subActivityDefID: subActivityFlowChartDefID, subActId: urlParams.get("subActID") });
                                    let skipDataParamForNextStep = JSON.stringify({ flowChartType: flowChartType, statuswf: C_BOOKING_STATUS_SKIPPED, modalname: "Skipped", stepID: nextStep.id, executionType: nextStep.executionType, taskname: nextStep.attrs.task.taskName, woID: urlParams.get("woID"), taskID: nextStep.attrs.task.taskID, bookingID: nextStep.attrs.task.bookingID, subActivityDefID: subActivityFlowChartDefID });


                                    let copySkipIcon = Object.assign({}, skipIcon);
                                    let copybotStoreIcon = Object.assign({}, botStoreIcon);
                                    let copysmileyIcon = Object.assign({}, smileyIcon);
                                    let copyfeedbackIcon = Object.assign({}, feedbackIcon);

                                    copySkipIcon.details = skipDataParamForNextStep;
                                    copybotStoreIcon.details = botDataParamForNextStep;
                                    let defaultColorOfSmiley = 'white';
                                    if (nextStep.sadCount == 1) { defaultColorOfSmiley = 'red'; }
                                    copysmileyIcon.fill = defaultColorOfSmiley;

                                    nextStep.icons = [infoIcon, copySkipIcon];
                                    if ((nextStep.attrs.tool.isRunOnServer == '1') && (nextStep.botType.toLowerCase().includes('nest'))) { nextStep.icons.push(copyServerStartIcon); }
                                    else if ((nextStep.attrs.tool.isRunOnServer == '1')) { nextStep.icons.push(copyServerStartIcon); nextStep.icons.push(startIcon); }
                                    else if ((nextStep.attrs.tool.isRunOnServer == '0')) { nextStep.icons.push(startIcon); }
                                    if (nextStep.workInstruction || nextStep.isStepInstructionExist) { nextStep.icons.push(downloadDocIcon); }
                                    if (nextStep.cascadeInput != null || nextStep.cascadeInput != undefined) {
                                        if (nextStep.cascadeInput.toLowerCase() == "yes") { nextStep.icons.push(cascadeInputIcon); }
                                    }

                                    if (flowChartType.toLowerCase() != 'custom') { nextStep.icons.push(feedbackIcon, copysmileyIcon); }

                                }
                                nextStep.showIcons = true;

                            }
                        }
                        else if (nextStep == false) { //Means next step is disabled and not started yet
                            let disabledStepDetails = JSON.parse(window.parent.$('#woButtons_' + woId + ' .disabledStepObj').val());
                            disabledStepDetails.enableStart = true;
                            disabledStepDetails = JSON.stringify(disabledStepDetails);

                            //update status to enable start button when handleWOActions function is called
                            window.parent.$('#woButtons_' + woId + ' .disabledStepObj').val(disabledStepDetails);

                            window.parent.$('#woButtons_' + woId + ' .startExperiencedWO').removeClass('disabledWOButton');
                        }
                    }
                }
            }

            if ((bookingStatus === C_BOOKING_STATUS_SKIPPED)) {
                cell.icons = [infoIcon, skippedIcon];
                if (executionType == "Automatic") {
                    if ((cell.attrs.tool.isRunOnServer == '1') && (cell.botType.toLowerCase().includes('nest'))) { cell.icons.push(serverStartIcon); }
                    else if ((cell.attrs.tool.isRunOnServer == '1')) { cell.icons.push(serverStartIcon); cell.icons.push(startIcon); }
                    else if ((cell.attrs.tool.isRunOnServer == '0')) { cell.icons.push(startIcon); }

                }
                else {
                    cell.icons.push(startIcon);
                    cell.icons.push(botStoreIcon);
                }

                if (cell.workInstruction || stepInstructionExist) { cell.icons.push(downloadDocIcon); }

                if (flowChartType.toLowerCase() != 'custom') { cell.icons.push(feedbackIcon, smileyIcon); }

                cell.showIcons = true;
                outboundlink = app.graph.getConnectedLinks(cell, { outbound: true });
                var nextStep = getNextStep(outboundlink[0], cell.id, arrAllShapes, woId);
                if (nextStep) {
                    if (nextStep.type == "ericsson.Decision") {
                        let link = [];
                        outboundlink = app.graph.getConnectedLinks(nextStep, { outbound: true });
                        for (let m in outboundlink) {
                            link = arrAllShapes.filter(function (el) { return el.id == outboundlink[m].get('id') });
                            link[0].showIcons = true;
                        }
                    }
                    else if (nextStep.type == "ericsson.EndStep") {
                        nextStep.icons = [makeCompleteIcon];
                        nextStep.showIcons = true;
                    }
                    else {
                        if (nextStep.attrs.task.status === C_BOOKING_STATUS_DEFAULT && nextStep.executionType === C_EXECUTION_TYPE_MANUAL) {
                            let botDataParamForNextStep = JSON.stringify({ currentShape: currentShape, executionType: executionType, avgEstdEffort: 0, taskId: nextStep.attrs.task.taskID, taskName: nextStep.attrs.task.taskName, rpaId: "0", stepID: nextStep.id, version: versionNo, projId: urlParams.get("prjID"), subActivityDefID: subActivityFlowChartDefID, subActId: urlParams.get("subActID") });
                            let skipDataParamForNextStep = JSON.stringify({ flowChartType: flowChartType, statuswf: C_BOOKING_STATUS_SKIPPED, modalname: "Skipped", stepID: nextStep.id, executionType: nextStep.executionType, taskname: nextStep.attrs.task.taskName, woID: urlParams.get("woID"), taskID: nextStep.attrs.task.taskID, bookingID: nextStep.attrs.task.bookingID, subActivityDefID: subActivityFlowChartDefID });
                            let copySkipIcon = Object.assign({}, skipIcon);
                            let copybotStoreIcon = Object.assign({}, botStoreIcon);
                            let copysmileyIcon = Object.assign({}, smileyIcon);
                            let copyfeedbackIcon = Object.assign({}, feedbackIcon);
                            copySkipIcon.details = skipDataParamForNextStep;
                            copybotStoreIcon.details = botDataParamForNextStep;
                            colorOfSmiley = 'white';
                            if (nextStep.sadCount == 1) { colorOfSmiley = 'red'; }
                            copysmileyIcon.fill = colorOfSmiley;
                            nextStep.icons = [copybotStoreIcon, infoIcon, copySkipIcon, startIcon];
                            if (nextStep.workInstruction || nextStep.isStepInstructionExist) { nextStep.icons.push(downloadDocIcon); }
                            if (flowChartType.toLowerCase() != 'custom') { nextStep.icons.push(copyfeedbackIcon, copysmileyIcon); }
                            let thatAsIcon = Object.assign({}, autoSenseIcon);
                            nextStep.shape = iconContent.shape;
                            nextStep.currentCell = iconContent.currentCell;

                            addAsIconToStepOnUserAction(nextStep, thatAsIcon);

                        }
                        nextStep.showIcons = true;
                    }
                }
                else if (nextStep == false) { //Means next step is disabled and not started yet
                    let disabledStepDetails = JSON.parse(window.parent.$('#woButtons_' + woId + ' .disabledStepObj').val());
                    disabledStepDetails.enableStart = true;
                    disabledStepDetails = JSON.stringify(disabledStepDetails);

                    //update status to enable start button when handleWOActions function is called
                    window.parent.$('#woButtons_' + woId + ' .disabledStepObj').val(disabledStepDetails);

                    window.parent.$('#woButtons_' + woId + ' .startExperiencedWO').removeClass('disabledWOButton');
                }
            }


            addAutoSenseIndicator(iconContent, autoSenseIcon);
        }
        

        }
        else if (currentShape === C_STEP_TYPE_DECISION) {
            outboundlink = app.graph.getConnectedLinks(cell, { outbound: true });
            let link;
            for (let o in outboundlink) {
                link = arrAllShapes.filter(function (el) { return el.id == outboundlink[o].get('id') });

            }    
        }

        else if (currentShape === C_STEP_TYPE_APPLINK) {
            let startLinkIcon = { src: '\uf051', fill: 'blue', tooltip: 'Play Next', className: '', event: 'clickYesOnDecisionBox' }; // icon change 

            if (cell.showIcons) {

                cell.icons = [startLinkIcon];
                let isDecisionLabelSelected = checkIfDecisionSelected(cell.id);
                let actualNextStep = getNextStepForAssessed(cell, arrAllShapes);
                let isNextDisabledStep = actualNextStep.attributes ? actualNextStep.attributes.executionType == C_EXECUTION_TYPE_MANUAL && !actualNextStep.attributes.viewMode.includes(C_PROFICIENCY_EXPERIENCED)
                    : actualNextStep.executionType == C_EXECUTION_TYPE_MANUAL && !actualNextStep.viewMode.includes(C_PROFICIENCY_EXPERIENCED);
                var nextStep = getNextStep(cell, cell.id, arrAllShapes, woId);
                if (nextStep) {
                    if (nextStep.type == "ericsson.Decision") {
                        let link = [];
                        outboundlink = app.graph.getConnectedLinks(nextStep, { outbound: true });
                        for (let m in outboundlink) {
                            link = arrAllShapes.filter(function (el) { return el.id == outboundlink[m].get('id') });

                            if ((woProficiency && isNextDisabledStep && isDecisionLabelSelected) ||
                                (iconsOnNextStep != undefined && iconsOnNextStep == true)) { link[0].showIcons = true; }



                        }
                    }
                    else if (nextStep.type == "ericsson.EndStep") {
                        nextStep.icons = [makeCompleteIcon];
                        nextStep.showIcons = true;
                    }
                    else {
                        
                        if (woProficiency && isNextDisabledStep && isDecisionLabelSelected) {
                            nextStep.showIcons = true;
                          
                        }
                        else if ((cell.hiddenselected) && (cell.hiddenselected == nextStep.id)) {
                            nextStep.showIcons = true;
                        }

                    }
                }
                else if (nextStep == false) { //Means next step is disabled and not started yet

                    let disabledStepDetails = JSON.parse(window.parent.$('#woButtons_' + woId + ' .disabledStepObj').val());
                    let targetId = disabledStepDetails.flowChartStepId;

                    if ((cell.hiddenselected) && (cell.hiddenselected == targetId)) {

                        disabledStepDetails.enableStart = true;
                        disabledStepDetails = JSON.stringify(disabledStepDetails);

                        //update status to enable start button when handleWOActions function is called
                        window.parent.$('#woButtons_' + woId + ' .disabledStepObj').val(disabledStepDetails);

                        window.parent.$('#woButtons_' + woId + ' .startExperiencedWO').removeClass('disabledWOButton');
                    }                   
                }
            }
            else {
                cell.icons = [startLinkIcon];
                const isLabelSelected = checkIfDecisionSelected(cell.id);
                const nextStepDetail = getNextStep(cell, cell.id, arrAllShapes, woId);  
                if (nextStepDetail.type === C_STEP_TYPE_DECISION && isLabelSelected ) {
                        displayPlayIconsOnOutboundLink(nextStepDetail, arrAllShapes);
                    }
                if (isLabelSelected){
                        prevCellDecissionAndImmediateNextStepDisabledManual(cell.id, arrAllShapes);

                    }

                
               
            }
        }
}

function addAutoSenseIcon(cell, autoSenseIcon) {
    if (cell.icons.length > 0) {
        let autosenseArray = cell.icons.filter(x => x.name === C_AUTOSENSE.toLowerCase());
        if (autosenseArray == undefined || autosenseArray.length == 0 || autosenseArray == null) {
            cell.icons.unshift(autoSenseIcon);
        }
    } else {
        cell.icons = [autoSenseIcon];
    }
    cell.showIcons = true;


}

function markAsCompleted(element) {
    let woID = urlParams.get("woID");
    window.parent.validateAndCompleteWorkOrder(woID);
}

//BOT STORE CLICK FUNCTION
$(document).on('click', '.view-bot-store', function () {
    let getDetails = JSON.parse($(this).attr('data-details'));
    let currentShape = '', executionType = getDetails.executionType, avgEstdEffort = 0, taskId = getDetails.taskId, taskName = '', rpaId = 0, stepID = getDetails.stepID, version = getDetails.version, tool_ID = '', tool_Name = '', subActDefId = getDetails.subActivityDefID, subActId = getDetails.subActId;

    let extraParams = { subActDefId: subActDefId, projId: getDetails.projId, subActId: subActId };
    funcRpa1(currentShape, executionType, avgEstdEffort, taskId, taskName, rpaId, stepID, version, tool_ID, tool_Name, extraParams);

});

//Browse Rpa Input Zip File 
$(document).on("click", ".serverstart", function () {
    //Server Bots
    $('#botConfigModal').modal('hide');
    let type_bot = JSON.parse($(this).attr('data-details')).botType;
    let flowChartType = JSON.parse($(this).attr('data-details')).flowChartType;

    //Not Bot Nest
    if (!type_bot.toLowerCase().includes('nest')) {
        let getDetails = $(this).attr('data-details');
        let stepID = JSON.parse(getDetails).stepID;
        let subActivityFlowChartDefId = JSON.parse(getDetails).subActivityDefID;

        let outputUpload = getOutputUploadFromAPIUsingStepId(stepID, subActivityFlowChartDefId);
        let receivedDetails = JSON.parse(getDetails);
        receivedDetails.outputUpload = outputUpload;

        receivedDetails = JSON.stringify(receivedDetails);

        $('#botjsondetails').val(receivedDetails);
        let execDetails = JSON.parse($('#botjsondetails').val());
        

        let isInputRequired = JSON.parse(getDetails).isInputRequired;
        if (isInputRequired) {
            $('#modalBrowseFileHeader').find('p').remove().end();
            $('#modalBrowseFileHeader').append('<p> Input URL for RPAID: ' + rpaID + '</p>')
            $('#serverBotInputUrl').val('');
            $('#modalBrowseFile').modal('show');
            let marketAreaConfiguredWithSharepoint = checkSharepointConfiguredWithMA(JSON.parse(getDetails).projID);
            if (marketAreaConfiguredWithSharepoint == null) {
                $('#runBotOnServer').prop("disabled", true);
            }
            else {
                // It will work as functionality works
            }
        }
        else {
            if (outputUpload === 'YES') {
                $('#modalBrowseFileHeaderForOutput').find('p').remove().end();
                $('#modalBrowseFileHeaderForOutput').append(`<p> Output URL for RPAID: ${rpaID} </p>`)
                $('#serverBotOutputUrl').val('');
                $('#modalOutputFileForServerBot').modal('show');
                let marketAreaConfiguredWithSharepoint = checkSharepointConfiguredWithMA(JSON.parse(getDetails).projID);
                if (marketAreaConfiguredWithSharepoint == null) {
                    $('#runNoInputBotOnServer').prop("disabled", true);
                }
            }
            else {
                playServerStartStep('', execDetails, afterLoadingFlowchart, null, null);
            }
        }
    }
    //BOTNEST
    else if (type_bot.toLowerCase().includes('nest')) {
        let isOnInternet = validationForBotNest();
        if (isOnInternet == true) {
            $('#fileinputOnServerPlay').val('');
            let getDetails = $(this).attr('data-details');
            getDetails = JSON.parse(getDetails);
            let botConfigJson = null; let botType = ""; let botNestId = ''; let configForBot = "";

            botConfigJson = getDetails.botConfigJson;
            if (botConfigJson != "" && botConfigJson != undefined) {
                botConfigJson = JSON.stringify(botConfigJson);
                createFormFromFormBuilderJson('botConfigBody', botConfigJson);
            }
            botType = getDetails.botType;
            botNestId = getDetails.botNestId.toString();

            configDetails = JSON.stringify({ flowChartType: flowChartType, isInputRequired: getDetails.isInputRequired, isRunOnServer: getDetails.isRunOnServer, configForBot: getDetails.configForBot, bookingID: getDetails.bookingId, bookingStatus: getDetails.bookingStatus, executionType: getDetails.executionType, taskID: getDetails.taskID, taskName: "", stepName: getDetails.stepName, stepID: getDetails.stepID, version: getDetails.version, projID: getDetails.projID, subActivityDefID: getDetails.subActivityDefID, subActId: getDetails.subActId, woID: getDetails.woID, isCallingFromFlowchart: true });

            configDetails = JSON.stringify({ flowChartType: flowChartType, isInputRequired: getDetails.isInputRequired, isRunOnServer: getDetails.isRunOnServer, configForBot: getDetails.configForBot, bookingID: getDetails.bookingId, bookingStatus: getDetails.bookingStatus, executionType: getDetails.executionType, taskID: getDetails.taskID, taskName: "", stepName: getDetails.stepName, stepID: getDetails.stepID, version: getDetails.version, projID: getDetails.projID, subActivityDefID: getDetails.subActivityDefID, subActId: getDetails.subActId, woID: getDetails.woID, isCallingFromFlowchart: true });

            if (botType.toLowerCase().includes('nest')) {
                $('#fileInputDiv').show();
                $('#fileinputOnServerPlay').attr('required', true);
                $('#currentCelldata').data('botType', botType);
                $('#currentCelldata').data('botNestId', botNestId);
                getFileInputNames(getDetails.configForBot, window);

            }
            $('#cellDataAutomatic').data('details', configDetails);
            $('#currentCelldata').data('details', configDetails);
            $('#currentCelldata').data('botOldJson', botConfigJson);
            $("#btnUploadFile").prop("disabled", false);
            $('#botConfigModal').modal('show');

            serverBotForNest(window);

        }
        else {
            pwIsf.alert({ msg: "BOTNest bots cannot be run on internet!", type: 'info' });
        }
    }
    $('#modalSelectStartOption').modal('hide');
});

//SKIP OR OHHOLD CLICK ICON
$(document).on("click", ".view-skip-wf", function () {

    let getDetails = JSON.parse($(this).attr('data-details'));
    let woId = getDetails.woID;
    var bookId = getDetails.bookingID;
    var taskId = getDetails.taskID;
    var taskName = getDetails.taskname;
    var status = getDetails.statuswf;
    var modalname = getDetails.modalname;
    let stepID = getDetails.stepID;
    let executionType = getDetails.executionType;
    let subActivityFlowChartDefID = getDetails.subActivityDefID;
    let flowChartType = getDetails.flowChartType;

    let bookingType = '';
    $('#skippedHeaderWF').find('p').remove().end();
    $('#skippedHeaderWF').append('<p>' + modalname + ': ' + taskName + '</p>')
    $(".modal-body #woIdWF").val(woId);
    $(".modal-body #taskIdWF").val(taskId);
    $(".modal-body #bookIdWF").val(bookId);
    $(".modal-body #statusWF").val(status);
    $(".modal-body #stepID").val(stepID);
    $(".modal-body #flowChartType").val(flowChartType);


    //populated reason dropdown


    if (status === C_BOOKING_STATUS_SKIPPED && executionType === C_EXECUTION_TYPE_AUTOMATIC) {

        let bookingDetails = getBookingDetailsByBookingId(woId, taskId, stepID);
        bookingType = bookingDetails.type;
        bookId = bookingDetails.bookingID;
        let currentStatus = bookingDetails.status;

        // skipped, bookingtype = "queued"
        if (bookingType != undefined && bookingType.toLowerCase() == "queued" && currentStatus != C_BOOKING_STATUS_COMPLETED) {
            GetReasonForDenail('Automatic_Forced_stop', $('#commentSkippedWF'));
            $('#modalSkippedWF').modal('show');
        }
        //Stopped and then skipped, bookingtype = "queued"
        else if (bookingType != undefined && bookingType.toLowerCase() == "queued" && currentStatus === C_BOOKING_STATUS_COMPLETED) {
            updateFlowChartTaskStatus(flowChartType, woId, subActivityFlowChartDefID, stepID, taskId, bookId, status, null, executionType, true);
        }
        // skipped, bookingtype = "booking"
        else if (bookingType != undefined && bookingType.toLowerCase() == "booking" && currentStatus != C_BOOKING_STATUS_COMPLETED) {
            GetReasonForDenail('Automatic_Forced_stop', $('#commentStoppedWF'));

            pwIsf.confirm({
                title: 'Stop BOT Execution?', type: 'warning', msg:
                    'Bot is already executing. Inorder to skip the step, stop execution first.',
                'buttons': {
                    'Force Stop': {
                        'class': 'btn btn-danger',
                        'action': function () {
                            $('.stopAuto #woIdWF').val(urlParams.get('woID'));
                            $('.stopAuto #taskIdWF').val(taskId);
                            $('.stopAuto #stepID').val(stepID);
                            $('.stopAuto #subActDefId').val(subActivityFlowChartDefID);
                            $('.stopAuto #bookIdWF').val(bookId);
                            $('.stopAuto #bookTypeWF').val('FORCE_STOP');
                            $(".stopAuto #calledFromSkip").val('true');
                            $(".stopAuto #flowChartType").val(flowChartType);
                            $('#modalStopWF').modal('show');
                        }
                    },
                    'Cancel': {
                        'class': 'btn btn-success',
                        'action': function () {
                            $('#modalSkippedWF').modal('hide');
                        }
                    },
                }
            });

        }
        else if (bookingType != undefined && bookingType.toLowerCase() == "booking" && currentStatus === C_BOOKING_STATUS_COMPLETED) {
            //Stopped and then skipped, bookingtype = "booking".
            updateFlowChartTaskStatus(flowChartType, woId, subActivityFlowChartDefID, stepID, taskId, bookId, status, null, executionType, true);
        }
        else {
            GetReasonForDenail('Automatic_Forced_stop', $('#commentSkippedWF'));
            $('#modalSkippedWF').modal('show');
        }

    }
    else {

        GetReasonForDenail('DeliveryExecution', $('#commentSkippedWF'));
        $('#modalSkippedWF').modal('show');
    }
});

function forceStopOnSkip(flowChartType, woID, subActDefID, stepID, taskID, isCallingFromFlowchart, executionType, bookingID, bookingType, comments, isCalledFromSkip, afterLoadingFlowchart) {
    if (isCalledFromSkip == "true") {
        InvokeStopFlowChartTask(flowChartType, woID, subActDefID, stepID, taskID, isCallingFromFlowchart, executionType, bookingID, bookingType, comments, afterLoadingFlowchart, '', false, false, true);
        updateFlowChartTaskStatus(flowChartType, woID, subActDefID, stepID, taskID, bookingID, C_BOOKING_STATUS_SKIPPED, null, executionType, isCallingFromFlowchart);
    }
    else {
        InvokeStopFlowChartTask(flowChartType, woID, subActDefID, stepID, taskID, isCallingFromFlowchart, executionType, bookingID, bookingType, comments, afterLoadingFlowchart, '', true, false, false);
    }
    
}

$(document).on("click", "#runOnLocalAutomatic", function () {
    playNextClicked = false;
    $('#botConfigModal').modal('hide');
    var config = JSON.parse($(this).attr('data-details'));
    var woID = config.woID;
    var projID = config.projID;
    var taskID = config.taskID;
    var subActivityDefID = config.subActivityDefID;
    var executionType = config.executionType;
    var id = config.id;
    var bookingStatus = config.bookingStatus;
    var bookingID = config.bookingID;
    var botConfigJson = config.botConfigJson;
    var botType = config.botType;
    var fileID = config.fileID;
    var version = config.version;
    var stepName = config.stepName;
    var afterLoadingFlowchart = config.afterLoadingFlowchart;
    var outputUpload = config.outputUpload;
    var isCallingFromFlowchart = config.isCallingFromFlowchart;
    var isRunOnServer = config.isRunOnServer;
    var isInputRequired = config.isInputRequired;
    let flowChartType = config.flowChartType;
    let StartTaskInput;
    //Non Bot Config
    if (botConfigJson == undefined || botConfigJson == "" || botConfigJson == "\"\"") {
        StartTaskInput = PrepareStartFlowChartTaskInput(flowChartType, woID, projID, taskID, subActivityDefID, executionType, id, bookingStatus, bookingID, botConfigJson, botType, fileID, executionType, version, stepName, outputUpload, isRunOnServer, isInputRequired, afterLoadingFlowchart, isCallingFromFlowchart);
        startFlowChartTask(StartTaskInput);
    }
    //Bot Config
    else {
        StartTaskInput = PrepareStartFlowChartTaskInput(flowChartType, woID, projID, taskID, subActivityDefID, executionType, id, bookingStatus, bookingID, JSON.stringify(actualJson), botType, fileID, executionType, version, stepName, outputUpload, isRunOnServer, isInputRequired, afterLoadingFlowchart);
        startFlowChartTask(StartTaskInput);
    }
    $('#modalSelectStartOption').modal('hide');
});

function GetReasonForDenail(reasonType, selectID) {
    $.isf.ajax({
        async: false,
        url: service_java_URL + "woExecution/getWOFailureReasons/" + reasonType,
        success: function (data) {
            selectID.html('');
            if (data.isValidationFailed == false) {
                $.each(data.responseData, function (i, d) {
                    selectID.append('<option value="' + d.failureReason + '">' + d.failureReason + '</option>');
                })
            }
            else {
                pwIsf.alert({ msg: data.formErrors[0], type: 'error' });
            }

        },
        error: function (xhr, status, statusText) {
            console.log('An error occurred on getProjectName: ' + xhr.error);
        },
        complete: function (xhr, statusText) {
            $('#commentSkippedWF').select2();
        }
    });
}

function submitRpaFile() {
    let inputFileUrl = $(C__SERVER_BOT_INPUT_FILE_URL_ID).val();
    let execDetails = JSON.parse($('#botjsondetails').val());
    let inputFileValidated = checkInputFileValidationForServerBot(inputFileUrl);
    if (inputFileValidated) {
        let urlValidationFromAPI = validateInputUrlForServerBot(inputFileUrl,execDetails.projID);
        if (urlValidationFromAPI) {
             playServerStartStep(null, execDetails, afterLoadingFlowchart, inputFileUrl,null);
        }
    }
    else {
        console.log(C_FILE_VALIDATION_FAIL_MSG);
    }
}

function submitOutputFile() {
    //let execDetails;
    let outputFileUrl = $('#serverBotOutputUrl').val();
    //let getDetails = $('#runOnServerAutomatic').attr('data-details');
    //if (getDetails == null || getDetails === '') {
    //    execDetails = JSON.parse($('#botjsondetails').val());
    //} else {
    //    $('#botjsondetails').val(getDetails);
    //    execDetails = JSON.parse($('#botjsondetails').val());
    //} 

    let execDetails = JSON.parse($('#botjsondetails').val());
    if (outputFileUrl == null || outputFileUrl === "") {
        pwIsf.alert({ msg: 'Please provide output file url', type: C_WARNING });
    } else {
        let urlValidationFromAPI = validateInputUrlForServerBot(outputFileUrl, execDetails.projID);
        if (urlValidationFromAPI) {
            playServerStartStep(null, execDetails, afterLoadingFlowchart, null, outputFileUrl);
        }
    }
}

// start a step
function playServerStartStep(file, execDetails, callback, inputFileUrl, serverBotOutputUrl) {
    let getDetails = execDetails;
    let woID = getDetails.woID;
    let execType = getDetails.executionType;
    let prjID = getDetails.projID;
    let taskID = getDetails.taskID;
    let version = getDetails.version;
    let subActFlowChartDefID = getDetails.subActivityDefID;
    let stepID = getDetails.stepID;
    let stepName = getDetails.stepName;
    let botType = getDetails.botType;
    let isRunOnServer = getDetails.isRunOnServer;
    let isCallingFromFlowchart = getDetails.isCallingFromFlowchart;
    let isInputRequired = getDetails.isInputRequired;
    let flowChartType = getDetails.flowChartType;
    let outputUpload = getDetails.outputUpload;

    window.parent.pwIsf.addLayer({ text: C_PLEASE_WAIT });
    if (rpaID != 0) {
        var executionPlanObj = new Object();
        executionPlanObj.signumID = signumGlobal;
        executionPlanObj.isApproved = 0;
        executionPlanObj.executionType = execType;
        executionPlanObj.woid = woID;
        executionPlanObj.taskid = taskID;
        executionPlanObj.projectID = prjID;
        executionPlanObj.versionNO = version;
        executionPlanObj.subActivityFlowChartDefID = subActivityFlowChartDefID;
        executionPlanObj.stepID = stepID;

        $.isf.ajax({
            url: `${service_java_URL}woExecution/checkParallelWorkOrderDetails/`,
            type: "POST",
            data: JSON.stringify(executionPlanObj),
            contentType: C_CONTENT_TYPE_APPLICATION_JSON,
            success: function (data) {
                if (!data.isValidationFailed) {
                    if (data.responseData.executeFlag) {
                        $.isf.ajax({
                            type: "GET",
                            async: false,
                            url: `${service_java_URL}woExecution/checkIFPreviousStepCompleted/${woID}/${taskID}/${subActFlowChartDefID}/${stepID}`,
                            success: function (data) {
                                if (data.responseData.allowed) {
                                    let botjson = new Object();
                                    botjson.signumID = signumGlobal;
                                    botjson.wOID = woID;
                                    botjson.projectId = prjID;
                                    botjson.taskID = taskID;
                                    botjson.botId = rpaID;
                                    botjson.inPath = "abc";
                                    botjson.nodes = getWorkOrderViewDetailsByWOIDAPICall(woID);
                                    botjson.botPlatform = "server";
                                    botjson.stepID = stepID;
                                    botjson.subActivityFlowChartDefID = subActFlowChartDefID;
                                    botjson.executionType = execType;
                                    botjson.bookingType = "QUEUED";
                                    botjson.type = botType;
                                    botjson.isCallingFromFlowchart = isCallingFromFlowchart;
                                    botjson.stepName = stepName;
                                    botjson.outputUpload = outputUpload;
                                    if (environment !== "Prod") {
                                        botjson.forTesting = "YES";
                                    }
                                    else {
                                        botjson.forTesting = "NO";
                                    }

                                    let botConfig = '';
                                    let fileData = new FormData();
                                    fileData.append('inputZip', file);
                                    fileData.append('serverBotJsonStr', JSON.stringify(botjson));
                                    fileData.append('botConfigJson', botConfig);

                                    let stepDetailsData = new Object();
                                    stepDetailsData.inputZip = file;
                                    stepDetailsData.botConfigJson = botConfig;
                                    stepDetailsData.serverBotJsonStr = botjson;
                                    stepDetailsData.isRunOnServer = isRunOnServer;
                                    stepDetailsData.isInputRequired = isInputRequired;
                                    stepDetailsData.flowChartType = flowChartType;
                                    stepDetailsData.serverBotInputUrl = inputFileUrl;
                                    stepDetailsData.serverBotOutputUrl = serverBotOutputUrl;                                    
                                    let modalId;
                                    if (serverBotOutputUrl !== null) {
                                        modalId = "#modalOutputFileForServerBot";
                                    }
                                    else {
                                        modalId = "#modalBrowseFile";
                                    }
                                    startAnyStep(stepDetailsData, $(modalId));
                                }
                                else {
                                    window.parent.pwIsf.removeLayer();
                                    pwIsf.alert({ msg: data.message, type: 'error' });
                                }
                            },
                            error: function (xhr, status, statusText) {
                                commonErrorBlock(xhr);
                            },
                        });
                    }
                }
                else {
                    pwIsf.alert({ msg: data.formErrors[0], type: 'error' });
                    window.parent.pwIsf.removeLayer();
                }
            },
            error: function (xhr, status, statusText) {
                commonErrorBlock(xhr);
            }
        });
    }
}

// get NE details for WO View
function getWorkOrderViewDetailsByWOIDAPICall(woID) {
    let botJsonNodes;
    $.isf.ajax({
        url: `${service_java_URL}woExecution/getWorkOrderViewDetailsByWOID/${woID}`,
        async: false,
        success: function (data) {
            if (data[0].nodeNames == null || data[0].nodeNames == undefined || data[0].nodeNames == "") {
                botJsonNodes = 0;
            }
            else {
                botJsonNodes = data[0].nodeNames;
            }
        },
        error: function (xhr, status, statusText) {
            console.log(C_ERROR_MSG);
            botJsonNodes = "";
        }
    });

    return botJsonNodes;
}

function getFileInputNames(configForBot, currentWindow) {

    let listOfFileNames = new FormData();

    listOfFileNames.append('isfBotId', configForBot);

    //list file names
    $.isf.ajax({
        url: `${botNestExternal_URL}getInputFileNames`,
        enctype: 'multipart/form-data',
        type: "POST",
        data: listOfFileNames,
        processData: false,
        contentType: false,
        //async:false,
        cache: false,
        crossDomain: true,
        timeout: 600000,
        success: function (data) {
            if (data.includes('fileNames')) {
                pwIsf.alert({ msg: "Please use the filenames listed in popup to upload respective files.", type: "success" });
                let filenamelists = JSON.parse(data).fileNames;
                let filenamelisttext = filenamelists[0];
                for (let i = 1; i < filenamelists.length; i++) {
                    filenamelisttext = filenamelisttext + ',' + filenamelists[i];

                }
                currentWindow.$('.fileNames').text(filenamelisttext);
                currentWindow.$('#currentCelldata').data('fileNames', JSON.parse(data).fileNames);


            }

        },
        error: function () {
            console.log('error');

        },
        complete: function () {
            pwIsf.removeLayer();

        }
    });

}

function getBookingDetailsByBookingId(wOID, taskID, stepID) {
    let bookingDetails = '';
    let bookingObject = new Object();
    bookingObject.wOID = wOID;
    bookingObject.taskID = taskID;
    bookingObject.stepID = stepID

    $.isf.ajax({
        type: 'POST',
        async: false,
        contentType: 'application/json',
        data: JSON.stringify(bookingObject),
        url: service_java_URL + 'woExecution/stopWOMaxBookingIDNew',
        success: function (data) {
            if (data.isValidationFailed === false && data.responseData !== null) {
                if (data.responseData.type) {
                    bookingType = data.responseData.type;
                }
                bookingDetails = data.responseData;
			}
            
        },
        error: function (data) {
            pwIsf.removeLayer();
            $('#botConfigModal').modal('hide');
            pwIsf.alert({ msg: "error", type: 'error' });
        }
    });
    return bookingDetails;
}

function getFlowChart(woID,proficiencyID='0') {
    pwIsf.addLayer({ text: "Please wait ..." });
    $.isf.ajax({
        async: false,
        type: "GET",
        url: `${service_java_URL}woExecution/getWOWorkFlow/${woID}/${proficiencyID}`,
        dataType: "json",
        crossdomain: true,
        success: function (data) {
            data = data.responseData;
            if ('Success' in data) {
                var parsedJSON = JSON.parse(data.Success.flowChartJSON);
                app.graph.fromJSON(parsedJSON);
                resizeCell(app.paper.model);
                makePopup();
            }
        },
        complete: function () {
            pwIsf.removeLayer();
        }
    })
}

function openISFDesktop(url) {
    window.open(url, '_self');
}

function flowChartViewOpenInNewWindow(url) {
    var width = window.innerWidth * 0.85;
    // define the height in
    var height = width * window.innerHeight / window.innerWidth;
    // Ratio the hight to the width as the user screen ratio
    window.open(url, 'newwindow');

}

function disableAssessedExperiencedToggle(wOID, fromIframe = true) {
    if (fromIframe) {
        window.parent.$('#togBtnForFlowChartViewMode_' + wOID).prop('disabled', 'disabled'); //disabled toggal button of assessed/experienced
        $(window.parent.$('#togBtnForFlowChartViewMode_' + wOID).next()[0]).addClass('disableAssessedBg') // make toggle button Gray
    }
    else {
        $('#togBtnForFlowChartViewMode_' + wOID).prop('disabled', 'disabled'); //disabled toggal button of assessed/experienced
        $($('#togBtnForFlowChartViewMode_' + wOID).next()[0]).addClass('disableAssessedBg')
    }
}

function addAutoSenseBedgeOnWF(backColor) {
    let validationArea = $('<div class="validateArea" ></div>');
    let backCssColor = C_AS_ENABLED_BEDGE_CSS;
    let textOfAs = C_AS_ENABLED_TEXT;
    if (backColor === C_AS_BEDGE_COLOR_ARRAY_GO[0]) {
        textOfAs = C_AS_DISABLED_TEXT;
        backCssColor = C_AS_DISABLED_BEDGE_CSS
    }
    let autoSenseOnPaperBtn = $('<button class="validateWfBtn" >' + textOfAs + '</button>');
    autoSenseOnPaperBtn.addClass(backCssColor);
    $(autoSenseOnPaperBtn).attr('disabled', true);
    validationArea.append(autoSenseOnPaperBtn);
    $('#app').find('.app-header').append(validationArea);
    //workflow for Preview. FlowChartWorkOrderAdd

}

function notifyToHub(data) {
    window.parent.startScanner(data);
}

function getFirstManualStepWithStartRule(arrCell, woID, defID) {
    let startStep;
    let firstLink;
    let firstAfterStartStep;
    let arrAutoSenseData = [];
    startStep = arrCell.cells.find(x => x.type === C_STEP_TYPE_START);
    firstLink = arrCell.cells.filter(x => x.type === C_STEP_TYPE_APPLINK).find(y => y.source.id === startStep.id);
    firstAfterStartStep = arrCell.cells.find(x => x.id === firstLink.target.id);
    //for first manual step.
    if (firstAfterStartStep.type === C_STEP_TYPE_MANUAL && (firstAfterStartStep.startRule === true || firstAfterStartStep.stopRule === true)) {
        let autoSenseData = getAutoSenseObject(woID, defID, firstAfterStartStep.id, C_SOURCEUI, signumGlobal, '', '', firstAfterStartStep.attrs.task.taskID, firstAfterStartStep.startRule, firstAfterStartStep.stopRule);
        arrAutoSenseData.push(autoSenseData);
    }
    else if (firstAfterStartStep.type === C_STEP_TYPE_DECISION) {
        let arrAllLink = arrCell.cells.filter(x => x.type === C_STEP_TYPE_APPLINK).filter(y => y.source.id == firstAfterStartStep.id);

        if (arrAllLink.length > 0) {

            if (validateAllNextStepAreManualWHStartRule(arrAllLink, arrCell) === true) {
                let tempManageArray = [];
                $.each(arrAllLink, function (i, d) {
                    let curCell = arrCell.cells.find(x => x.id == arrAllLink[i].target.id);
                    tempManageArray.push(curCell);

                });
                $.each(tempManageArray, function (j, k) {
                    let autoSenseData = getAutoSenseObject(woID, defID, tempManageArray[j].id, C_SOURCEUI, signumGlobal, C_DECISION.toLowerCase(), '', tempManageArray[j].attrs.task.taskID, tempManageArray[j].startRule, tempManageArray[j].stopRule);
                    arrAutoSenseData.push(autoSenseData);
                });

            }
        }
    }
    return arrAutoSenseData;
}

function validateAllNextStepAreManualWHStartRule(arrCell, allCells) {

    var valid = false;
    $.each(arrCell, function (i, d) {
        let curCell = allCells.cells.find(x => x.id === arrCell[i].target.id);
        if (curCell) {
            if (curCell.type === C_STEP_TYPE_MANUAL && curCell.startRule === true) {
                valid = true;
            } else {
                return false;
            }
        }

    });
    return valid;

}

function saveAutoSenseDetails(action, arrShape, flowChartDefID, isExperiencedChecked, woID) {

    let jsonCellArray = [];
    let userAction = action;
    let toggleValue = $('#togBtnForFlowChartViewMode_' + woID).prop("checked");
    var userInput = { 'WOID': woID, 'USERACTION': userAction, 'EXPERIENCED': isExperiencedChecked };
    localStorage.setItem(C_LS_KEY_ASUSERINPUT + woID, JSON.stringify(userInput));
    pwIsf.addLayer({ text: C_LOADDER_MSG });
        jsonCellArray = arrShape;
        let arrayofStepsWithAutoSenseRule = arrShape.cells.filter(req => req.type === C_STEP_TYPE_MANUAL).find(req => req.startRule == true || req.stopRule == true);
        if (arrayofStepsWithAutoSenseRule == undefined || arrayofStepsWithAutoSenseRule == null) {
            action = false;
        }
        $.isf.ajax({
            type: "POST",
            async: false,
            processData: false,
            contentType: 'application/json',
            cache: false,
            url: service_java_URL + "autoSense/updateWorkOrderAutoSenseStatus?woID=" + woID + "&flowchartDefID=" + flowChartDefID + "&autoSenseFlag=" + action,
            success: function (data) {

                isWorkOrderAutoSenseEnabled = action;
                $('#workOrderAutoSenseEnabled').val(action);
                if (data.isValidationFailed == false) {

                    let woIDDetails = JSON.parse(window.parent.$('#viewBtn_' + woID).attr('data-details'));
                    woIDDetails.workOrderAutoSenseEnabled = action;
                    window.parent.$('#viewBtn_' + woID).attr('data-details', JSON.stringify(woIDDetails));

                    //Also updating the data for the element using .data() method as we're accessing values using .data() on click of eye button
                    let woDetails = window.parent.$('#viewBtn_' + woID).data('details');
                    woDetails.workOrderAutoSenseEnabled = action;
                    window.parent.$('#viewBtn_' + woID).data('details', woDetails);


                }

            },
            error: function () {
                pwIsf.alert({ msg: "error", type: "error" });
                isWorkOrderAutoSenseEnabled = false;
            },
            complete: function () {
                let autoSenseData = getFirstManualStepWithStartRule(jsonCellArray, woID, flowChartDefID);
                if (autoSenseData.length > 0 && autoSenseData != undefined) {
                    if (action === true) {
                        notifyToHub(autoSenseData);
                    }
                }
                reloadFlowchartWindow(woID, isExperiencedChecked);

            }
        });

    $(".inspector-container").hide();
    $('#autoSensePopupId').modal('hide');
}

function reloadFlowchartWindow(WoId, proficiencyId) {

    let iframeId = "#iframe_" + WoId;
    let experiencedParam = '';
    let instanceId = proficiencyId.toString().slice(0, -1);
    experiencedParam = ($('#togBtnForFlowChartViewMode_' + WoId).prop("checked")) ? '&proficiencyLevel=2&proficiencyId=' + instanceId + '2' : '&proficiencyLevel=1&proficiencyId=' + instanceId + '1';
    let iframeSrcTemp = '';
    let iframeSrc = window.location.href;// frmSource;
    if (iframeSrc.search('isExperiencedChecked') != -1) {
        iframeSrcTemp = iframeSrc.split('&isExperiencedChecked');
        iframeSrc = iframeSrcTemp[0] + experiencedParam;
    }
    else {
        iframeSrc = iframeSrc + experiencedParam;
    }
    let isAlreadyOnAssessed = '&isAlreadyOnAssessed=' + true;
    iframeSrc += isAlreadyOnAssessed;
    window.parent.pwIsf.addLayer({ text: C_LOADDER_MSG });
    window.parent.$(iframeId).on('load', function () {
        window.parent.pwIsf.removeLayer();
    });
    window.parent.$(iframeId).attr('src', iframeSrc);
}

function PrepareAutoSenseInputData(wOID, subActivityFlowChartDefID, stepID, status, InProgressTasks) {

    var autoSenseInputData = new Object;
    for (var i in InProgressTasks) {
        if (InProgressTasks[i].WoId == wOID && InProgressTasks[i].FlowChartStepID == stepID && InProgressTasks[i].FlowChartDefID == subActivityFlowChartDefID && InProgressTasks[i].WorkFlowAutoSenseEnabled === true && InProgressTasks[i].WorkOrderAutoSenseEnabled === true) {
            if ((InProgressTasks[i].ExecutionType === C_EXECUTION_TYPE_MANUAL && (InProgressTasks[i].StartRule === true || InProgressTasks[i].StopRule === true)) ||
                (status !== C_BOOKING_STATUS_STARTED && (InProgressTasks[i].ExecutionType === C_EXECUTION_TYPE_MANUAL || InProgressTasks[i].ExecutionType === C_EXECUTION_TYPE_AUTOMATIC) && InProgressTasks[i].NextStepExecutionType === C_EXECUTION_TYPE_MANUAL && (InProgressTasks[i].NextStepStartRule === true || InProgressTasks[i].NextStepStopRule === true)) ||
                ((InProgressTasks[i].ExecutionType === C_EXECUTION_TYPE_MANUAL || InProgressTasks[i].ExecutionType === C_EXECUTION_TYPE_AUTOMATIC) && InProgressTasks[i].NextStepType === C_STEP_TYPE_DECISION)) {
                if (InProgressTasks[i].ExecutionType === C_EXECUTION_TYPE_MANUAL || InProgressTasks[i].ExecutionType === C_EXECUTION_TYPE_AUTOMATIC) {

                    if (InProgressTasks[i].StartRule === true || InProgressTasks[i].StopRule === true) {
                        status = InProgressTasks[i].ExecutionType + C_AUTOSENSE + status;
                    }
                    else {
                        status = InProgressTasks[i].ExecutionType + status;
                    }

                }
                autoSenseInputData.WoId = wOID;
                autoSenseInputData.FlowChartDefID = subActivityFlowChartDefID;
                autoSenseInputData.stepID = stepID;
                autoSenseInputData.source = C_SOURCEUI;
                autoSenseInputData.signumID = signumGlobal;
                autoSenseInputData.overrideAction = status;
                autoSenseInputData.action = "";
                autoSenseInputData.taskID = InProgressTasks[i].TaskID;
                autoSenseInputData.startRule = InProgressTasks[i].StartRule;
                autoSenseInputData.stopRule = InProgressTasks[i].StopRule;
            }
        }
    }
    return autoSenseInputData;
}

function PrepareAutoSenseInputDataForSkip(woID, subActivityFlowChartDefID, stepID, taskID, status, executionType, nextStartRule, nextStopRule, currentStartRule, currentStopRule, nextExecutionType, nextStepType,nextStepId) {
    var autoSenseInputDataForSkip = new Object;

    if (isWorkOrderAutoSenseEnabled === true && isWorkFlowAutoSenseEnabled === true) {
        if ((executionType === C_EXECUTION_TYPE_MANUAL && (currentStartRule === true || currentStopRule === true)) ||
            ((executionType === C_EXECUTION_TYPE_MANUAL || executionType === C_EXECUTION_TYPE_AUTOMATIC) && nextExecutionType === C_EXECUTION_TYPE_MANUAL && (nextStartRule === true || nextStopRule === true)) ||
            ((executionType === C_EXECUTION_TYPE_MANUAL || executionType === C_EXECUTION_TYPE_AUTOMATIC) && nextStepType ===C_STEP_TYPE_DECISION)) {
            if (executionType === C_EXECUTION_TYPE_MANUAL || executionType === C_EXECUTION_TYPE_AUTOMATIC) {             
                if (currentStartRule === true || currentStopRule === true) {
                    if (nextStepType === C_STEP_TYPE_DECISION) {

                        status = executionType + C_AUTOSENSE_DECISION + status;
                        stepID = stepID + C_NEXT + nextStepId;

                    }
                    else {
                        status = executionType + C_AUTOSENSE + status;

                    }
                        
                    }
                else {

                    if (nextStepType === C_STEP_TYPE_DECISION) {

                        status = executionType + C_DECISION + status;
                        stepID = stepID + C_NEXT + nextStepId;

                    }
                    else {
                        status = executionType + status;

                    }
                       

                    }
                    
                }
          

            autoSenseInputDataForSkip.WoId = woID;
            autoSenseInputDataForSkip.FlowChartDefID = subActivityFlowChartDefID;
            autoSenseInputDataForSkip.stepID = stepID;
            autoSenseInputDataForSkip.source = C_SOURCEUI;
            autoSenseInputDataForSkip.signumID = signumGlobal;
            autoSenseInputDataForSkip.overrideAction = status;
            autoSenseInputDataForSkip.action = "";
            autoSenseInputDataForSkip.taskID = taskID;
            autoSenseInputDataForSkip.startRule = currentStartRule;
            autoSenseInputDataForSkip.stopRule = currentStopRule;

        }

    }
    return autoSenseInputDataForSkip;
}

function getBackColorForAutoSenseButton() {
    let color;
    if (isWorkOrderAutoSenseEnabled) {
        color = C_AS_BEDGE_COLOR_ARRAY_GO[1];
    } else {
        color = C_AS_BEDGE_COLOR_ARRAY_GO[0];
    }
    return color;
}

// To notify FW in case we click on play next buton in between two decisions
function notifyFloatingWindowOnButtonClickNextDecision(wOID, subActivityFlowChartDefID, stepID) {
    var autoSenseInputData = new Object();
    autoSenseInputData.WoId = wOID;
    autoSenseInputData.FlowChartDefID = subActivityFlowChartDefID;
    autoSenseInputData.stepID = stepID;
    autoSenseInputData.source = C_SOURCEUI;
    autoSenseInputData.signumID = signumGlobal;
    autoSenseInputData.overrideAction = C_OVERRIDE_ACTION_DECISION_BC;
    autoSenseInputData.action = "";
    autoSenseInputData.taskID = "0";
    autoSenseInputData.startRule = "";
    autoSenseInputData.stopRule = "";

    window.parent.NotifyClientOnWorkOrderStatusChange(null, wOID, autoSenseInputData, true);

}

function GetElementAttForSettingDecisionValue(stepId, decisionValue, woID) {
    var eleAttr;
    var cellArray = app.graph.toJSON().cells;
    for (var item in cellArray) {
        if (cellArray[item].labels != undefined && cellArray[item].labels[0] != undefined && cellArray[item].labels[0].attrs.text.text === decisionValue) {
            eleAttr = cellArray[item];
        }
    }

    SetDecisionValue(stepId, decisionValue, eleAttr, woID);
}

function GetSavedAutoSenseData() {
    var result;
    $.isf.ajax({
        type: "GET",
        url: `${UiRootDir}/Data/GetAutoSenseData`,
        async: false,
        contentType: "application/json; charset=utf-8", // specify the content type
        dataType: 'JSON',
        success: function (response) {
            console.log("GetSavedAutoSenseData is executed successfully");
            result= response;
        },
        error: function (xhr, status, statusText) {
            console.log("error while saving autosense data");
        }
    });

    return result;
}

function handleWOActionButtons(woid, isFlowChartCall) {
    let proficiencyLevel = isFlowChartCall ? urlParams.get("proficiencyLevel") : $('#togBtnForFlowChartViewMode_' + woid).is(":checked") ? 2 : 1;
    let status = isFlowChartCall ? window.parent.$('#viewBtn_' + woid).data('details').status : $('#viewBtn_' + woid).data('details').status;
    let startSelectorObj = isFlowChartCall ? window.parent.$('#woButtons_' + woid + ' .startExperiencedWO') : $('#woButtons_' + woid + ' .startExperiencedWO');
    let pauseSelectorObj = isFlowChartCall ? window.parent.$('#woButtons_' + woid + ' .pauseExperiencedWO') : $('#woButtons_' + woid + ' .pauseExperiencedWO');
    let nextSelectorObj = isFlowChartCall ? window.parent.$('#woButtons_' + woid + ' .nextExperiencedStep') : $('#woButtons_' + woid + ' .nextExperiencedStep');
    let stopSelectorObj = isFlowChartCall ? window.parent.$('#woButtons_' + woid + ' .stopExperiencedWO') : $('#woButtons_' + woid + ' .stopExperiencedWO');
    let flowChartViewSelectorObj = isFlowChartCall ? window.parent.$('#flow_chart_single_box_' + woid) : $('#flow_chart_single_box_' + woid);
    let isWOFresh = isFlowChartCall ? window.parent.$('#viewBtn_' + woid).data('details').isWOFresh : $('#viewBtn_' + woid).data('details').isWOFresh;

    let isWOPartial;

    //Check if WO is partial
    if (status.toLowerCase() == C_WO_ASSIGNED_STATUS.toLowerCase() ||
        status.toLowerCase() == C_WO_REOPENED_STATUS.toLowerCase() ||
        (status.toLowerCase() == C_WO_ONHOLD_STATUS.toLowerCase() && isWOFresh)) {
        isWOPartial = false;
    }
    else if (status.toLowerCase() == C_WO_INPROGRESS_STATUS.toLowerCase()) {
        isWOPartial = true;
    }

    //Disable all WO Buttons first
    startSelectorObj.addClass('disabledWOButton');
    pauseSelectorObj.addClass('disabledWOButton');
    nextSelectorObj.addClass('disabledWOButton');
    stopSelectorObj.addClass('disabledWOButton');

    //Experienced Mode and Flow Chart is opened
    if (proficiencyLevel == 2 && flowChartViewSelectorObj.length != 0) {

        let graphCells = isFlowChartCall ? app.graph.toJSON().cells : document.getElementById('iframe_' + woid).contentWindow.app.graph.toJSON().cells;
        let endStep = graphCells.filter(function (el) { return el.type == C_STEP_TYPE_END });
        let isEndStepWithMarkAsComplete = endStep[0].icons && endStep[0].icons.length != 0 && endStep[0].icons.some(e => e.event == "markAsCompleted") && (endStep[0].showIcons ? endStep[0].showIcons : false);
        let disabledStepDetails = isFlowChartCall ? JSON.parse(window.parent.$('#woButtons_' + woid + ' .disabledStepObj').val()) : JSON.parse($('#woButtons_' + woid + ' .disabledStepObj').val());
        let disabledStepStatus = disabledStepDetails.status;
        let enableStart = disabledStepDetails.enableStart;

        //Fresh WO Case
        if (!isWOPartial) {

            let startStep = graphCells.filter(function (el) { return el.type == C_STEP_TYPE_START });
            let firstStep = getNextStepCell(startStep[0].id, graphCells);

            //Show start button if first step is disabled
            if (firstStep[0].executionType == C_EXECUTION_TYPE_MANUAL && !firstStep[0].viewMode.includes(C_PROFICIENCY_EXPERIENCED)) {
                startSelectorObj.removeClass('disabledWOButton');
            }

            //Show stop button if end step has mark as complete icon
            if (isEndStepWithMarkAsComplete) {
                stopSelectorObj.removeClass('disabledWOButton');
            }

        }
        else {
            //Selectors for inprogress task box
            let valSelectorObj = isFlowChartCall ? window.parent.$('.dBox-content #hiddenVal_' + woid) : $('.dBox-content #hiddenVal_' + woid);
            let buttonSelectorObj = isFlowChartCall ? window.parent.$('#inprogressTask_' + woid + ' .startNextTaskBtn') : $('#inprogressTask_' + woid + ' .startNextTaskBtn');

            //If a task is in progress and it's not automatic then enable pause/playnext
            if (valSelectorObj.length != 0) {
                if (valSelectorObj.data('exectype') != C_EXECUTION_TYPE_AUTOMATIC) {
                    pauseSelectorObj.removeClass('disabledWOButton');

                    //Check if playnext is visible in inprogress tile
                    if (buttonSelectorObj.length != 0) {
                        nextSelectorObj.removeClass('disabledWOButton');
                    }

                    //Show stop button if end step has mark as complete icon
                    //if (isEndStepWithMarkAsComplete) {

                        stopSelectorObj.removeClass('disabledWOButton');
                    //}
                }                
            }
            else {  
                //Show stop button if end step has mark as complete icon

                let isDisabledStepPresent = graphCells.some(el => el.executionType == C_EXECUTION_TYPE_MANUAL && !el.viewMode.includes(C_PROFICIENCY_EXPERIENCED));

                //Disable start button if no disabled step is present
                if (isDisabledStepPresent) {

                    if (disabledStepStatus != null || enableStart) {
                        startSelectorObj.removeClass('disabledWOButton');
                    }
                }
            }
        }
    }
}

function getNextStepCell(cellId, graphCells) {
    
    let link = graphCells.filter(function (el) {
        return el.type == 'app.Link' && el.source.id == cellId;
    });
    let nextCell = graphCells.filter(function (el) {
        return el.id == link[0].target.id;
    })
    return nextCell;
}

//Check if decision is selected
function checkIfDecisionSelected(cellId) {

    let decisionSelected = false;

    let linkCell = app.graph.getCell(cellId);
    let decisionText = linkCell.getSourceElement().attributes.attrs.label.text;
    let labelText = linkCell.label(0).attrs.text.text;

    if (decisionText.includes('Selected') && decisionText.split('Selected:')[1] == labelText) {
        decisionSelected = true;
    }

    return decisionSelected;
    
}

//Get app link just after decision for this branch
function getDecisionLink(cellId) {

    let link = arrLink.filter(function (el) {
        return el.target.id == cellId;
    })[0];

    let linkCell = app.graph.getCell(link.id);
    let srcEle = linkCell.getSourceElement();

    if (srcEle.attributes.type != C_STEP_TYPE_DECISION) {
        linkCell = getDecisionLink(srcEle.id);
    }

    return linkCell;

}

//Get the previous cell
function getPreviousCell(cellId) {
    let link = arrLink.filter(function (el) {
        return el.target.id == cellId;
    })[0];

    let previousCell = arrAllShapes.filter(function (el) {
        return el.id == link.source.id;
    })[0];

    return previousCell;
}

//Get the next enabled cell
function getNextEnabledCell(cellId) {    

    let link = arrLink.filter(function (el) {
        return el.source.id == cellId;
    })[0];

    let nextCell = arrAllShapes.filter(function (el) {
        return el.id == link.target.id;
    })[0];

    if (nextCell.executionType == C_EXECUTION_TYPE_MANUAL && !nextCell.viewMode.includes(C_PROFICIENCY_EXPERIENCED)) {
        nextCell = getNextEnabledCell(nextCell.id);
    }
    return nextCell;

}

//Get next step for Experienced WF. 'currCell' here is of type app.Link 
function getNextStepForExperienced(currCell, arrAllShapes) {
    
    let cell = app.graph.getCell(currCell.id);
    let targetEle = cell.getTargetElement();

    let nextCell = arrAllShapes.filter(function (el) { return el.id == targetEle.id })[0];

    //Condition to check if step is disabled
    let isDisabledStep = nextCell.attributes ? nextCell.attributes.executionType == C_EXECUTION_TYPE_MANUAL && !nextCell.attributes.viewMode.includes(C_PROFICIENCY_EXPERIENCED)
        : nextCell.executionType == C_EXECUTION_TYPE_MANUAL && !nextCell.viewMode.includes(C_PROFICIENCY_EXPERIENCED);

    //recursive call until enabled step is found
    if (isDisabledStep) {
        let newCell = app.graph.getCell(nextCell.id);
        let outboundlink = app.graph.getConnectedLinks(newCell, { outbound: true })[0];
        nextCell = getNextStepForExperienced(outboundlink, arrAllShapes);
    }

    return nextCell;
}

//To Pause Disabled Step
function PauseDisabledStep(stopDisabledStepDetails, reason)
{
    updateFlowChartTaskStatus(stopDisabledStepDetails.flowChartType, stopDisabledStepDetails.woID, stopDisabledStepDetails.subActivityFlowChartDefID, stopDisabledStepDetails.stepID, stopDisabledStepDetails.taskID, stopDisabledStepDetails.bookingID, "ONHOLD", reason, stopDisabledStepDetails.executionType, false)
}

//Update DC after disabled step stop/pause
function updateFlowchart(woId, status) {
    let disabledStepName = "WORK_ORDER_" + woId; 
    let disabledStepDetails = JSON.parse($('#woButtons_' + woId + ' .disabledStepObj').val());

    disabledStepDetails.status = status;
    $('#woButtons_' + woId + ' .disabledStepObj').val(JSON.stringify(disabledStepDetails));

    filterCommentsByStep(disabledStepName);

    let flowChart = document.getElementById('iframe_' + woId).contentWindow;
    let flowChartType = (flowChart.urlParams.get('flowchartType')) ? flowChart.urlParams.get('flowchartType') : '';
    let max = flowChart.max;
    let appObj = flowChart.app;
    let root = appObj.graph.getSources();
    if (root[0].attributes.type == 'ericsson.StartStep') { root = root[0] } else { root = root[1]; }
    let firstStep = flowChart.getFirstEnabledStep(appObj, root);

    //Show icons the first step after first disabled steps
    if (firstStep.attributes.type == "ericsson.Decision") {
        let link = [];
        let outboundlink = appObj.graph.getConnectedLinks(firstStep, { outbound: true });
        for (let m in outboundlink) { 
            outboundlink[m].set('showIcons', true);
        }
    }
    else if (firstStep.attributes.type == C_STEP_TYPE_END) {
        let makeCompleteIcon = { src: '\uf058', fill: 'white', tooltip: 'Mark as Completed', className: 'bottom-right', event: 'markAsCompleted', name: '' };
        firstStep.attributes.icons = [makeCompleteIcon];
        firstStep.attributes.showIcons = true;
    }
    else { firstStep.set('showIcons', true); }

    let fcJson = flowChart.app.graph.toJSON();
    flowChart.makeIconInElements(fcJson, max, flowChartType);
}

//To stop disabled step
function stopDisabledStep(stopDisabledStepDetails, reason, woId, isMarkCompleteNeeded = false,asyncType = true) {

    //If disabled task in progress
    if (stopDisabledStepDetails != null) {
        let stopTaskInput = PrepareStopFlowChartTaskInput(stopDisabledStepDetails.flowChartType, stopDisabledStepDetails.woID,
            stopDisabledStepDetails.subActivityFlowChartDefID, stopDisabledStepDetails.stepID, stopDisabledStepDetails.taskID,
            false, stopDisabledStepDetails.executionType, stopDisabledStepDetails.bookingID,
            stopDisabledStepDetails.bookingType, reason, afterLoadingFlowchart, asyncType);

        if (stopTaskInput.woID == null || stopTaskInput.woID.length == 0 || stopTaskInput.taskID == null || stopTaskInput.taskID.length == 0 || signumGlobal == null || signumGlobal.length == 0) {
            pwIsf.alert({ msg: 'Something went wrong!!', type: 'error' });
        }
        else {            
            let stopStepDetails = new Object();
            stopStepDetails.wOID = stopTaskInput.woID;
            stopStepDetails.taskID = stopTaskInput.taskID;
            stopStepDetails.stepID = stopTaskInput.stepID;
            stopStepDetails.subActivityFlowChartDefID = stopTaskInput.subActivityFlowChartDefID;
            stopStepDetails.signumID = signumGlobal;
            stopStepDetails.executionType = stopTaskInput.executionType;
            stopStepDetails.bookingID = stopTaskInput.booking_id;
            stopStepDetails.type = stopTaskInput.booking_type;
            stopStepDetails.reason = stopTaskInput.reason;
            stopStepDetails.decisionValue = '';
            stopStepDetails.refferer = 'ui';
            stopStepDetails.externalSourceName = 'ui';

            window.parent.pwIsf.addLayer({ text: "Please wait ..." });
            $.isf.ajax({
                async: stopTaskInput.asyncType,
                type: "POST",
                cors: true,
                contentType: 'application/json',
                data: JSON.stringify(stopStepDetails),
                url: `${service_java_URL}woExecution/stopWorkOrderTask`,
                success: function (data) {

                    if (data.isSuccess) {
                        $('#modalDisabledStepPause').modal('hide');

                        if ($('#iframe_' + woId)[0] != undefined) {                            
                            updateFlowchart(woId, C_BOOKING_STATUS_COMPLETED);
                        }

                        let stepDetailsData = new Object();

                        stepDetailsData.flowChartType = stopTaskInput.flowChartType;
                        stepDetailsData.status = C_EXECUTION_TYPE_MANUALDISABLED + '_' + C_BOOKING_STATUS_COMPLETED;
                        stepDetailsData.stepID = stopTaskInput.stepID;
                        stepDetailsData.bookingType = stopTaskInput.booking_type;

                        requestInprogressTasks();

                        //Notify other tabs and floating window
                        NotifyClientOnWorkOrderStatusChange(stepDetailsData, woId, null, true);

                        requestWorkorder();                        
                        handleWOActionButtons(stopTaskInput.woID, false);

                        if (isMarkCompleteNeeded) {
                            completeWorkOrder(woId);
                        }

                    }
                    else {
                        if (data.msg) {
                            location.reload();
                        }
                    }
                },
                complete: function () {
                    stopTaskInput.callback();
                },
                error: function (xhr, status, statusText) {
                    var errorMessage = JSON.parse(xhr.responseText).errorMessage;
                    if (errorMessage == undefined || errorMessage == '') {
                        errorMessage = "Some issue occured. Please try again!"
                    }
                    pwIsf.alert({ msg: errorMessage, type: 'error' });
                }
            });
        }
    }
    else if (isMarkCompleteNeeded) {
        completeWorkOrder(woId);
    }
}

//To pause disabled step
function pauseAllDisabledStep(stopDisabledStepDetails, reason, woId, isCallingFromFlowchart ,isMarkCompleteNeeded = false, asyncType = true) {

    //If disabled task in progress
    if (stopDisabledStepDetails != null) {
        let stopTaskInput = PrepareStopFlowChartTaskInput(stopDisabledStepDetails.flowChartType, stopDisabledStepDetails.woID,
            stopDisabledStepDetails.subActivityFlowChartDefID, stopDisabledStepDetails.stepID, stopDisabledStepDetails.taskID,
            false, stopDisabledStepDetails.executionType, stopDisabledStepDetails.bookingID,
            stopDisabledStepDetails.bookingType, reason, afterLoadingFlowchart, asyncType);

        if (stopTaskInput.woID == null || stopTaskInput.woID.length == 0 || stopTaskInput.taskID == null || stopTaskInput.taskID.length == 0 || signumGlobal == null || signumGlobal.length == 0) {
            pwIsf.alert({ msg: 'Something went wrong!!', type: 'error' });
        }
        else {
            let status = 'ONHOLD';
            if (stopDisabledStepDetails.bookingID == "%BOOKING_ID%" || stopDisabledStepDetails.bookingID == "") {
                stopDisabledStepDetails.bookingID = 0;
            }
            if (reason != null) {
                for (var i = 0; i < reason.length; i++) {
                    if (reason.charAt(i) == "/") {
                        reason = reason.replace(reason.charAt(i), "@");
                    }
                }
            }
            let refferer = 'ui';
            let apiurl = '';
            if (isCallingFromFlowchart === "true" && status === C_BOOKING_STATUS_SKIPPED) {

                let proficiencyID = urlParams.get("proficiencyId");

                apiurl = "woExecution/updateBookingDetailsStatus/" + stopTaskInput.woID + "/" + signumGlobal + "/" + stopTaskInput.taskID + '/' + stopDisabledStepDetails.bookingID + '/' + status + '/' + reason + '/' + stopTaskInput.stepID + '/' + stopTaskInput.subActivityFlowChartDefID + '/' + refferer + '?proficiencyId=' + proficiencyID;

            }
            else {
                apiurl = "woExecution/updateBookingDetailsStatus/" + stopTaskInput.woID + "/" + signumGlobal + "/" + stopTaskInput.taskID + '/' + stopDisabledStepDetails.bookingID + '/' + status + '/' + reason + '/' + stopTaskInput.stepID + '/' + stopTaskInput.subActivityFlowChartDefID + '/' + refferer;

            }
           pwIsf.addLayer({ text: "Please wait ..." });
            $.isf.ajax({
                async: stopTaskInput.asyncType,
                type: "POST",
                cors: true,
                url: service_java_URL + apiurl,
                success: function (data) {

                    if ('Success' in data) {
                        $('#modalDisabledStepPause').modal('hide');
                        let disabledStepName = "WORK_ORDER_" + woId;
                        let stepDetailsData = new Object();

                        stepDetailsData.flowChartType = stopTaskInput.flowChartType;
                        stepDetailsData.status = C_EXECUTION_TYPE_MANUALDISABLED + '_' + status;
                        stepDetailsData.stepID = stopTaskInput.stepID;
                        stepDetailsData.bookingType = stopTaskInput.booking_type;

                        changeDisabledManualStepColor(C_BOOKING_STATUS_ONHOLD, woId);
                        //update status ONHOLD
                        if (document.getElementById("iframe_" + woId)) {
                            updateFlowchart(woId, status);

                            
						}
                        requestInprogressTasks();

                        //Notify other tabs and floating window
                        NotifyClientOnWorkOrderStatusChange(stepDetailsData, woId, null, true);

                        requestWorkorder();

                        handleWOActionButtons(stopTaskInput.woID, false);

                    }
                    else {
                        if (data.msg) {
                            location.reload();
                        }
                    }
                },
                complete: function () {
                    stopTaskInput.callback();
                },
                error: function (xhr, status, statusText) {
                    var errorMessage = JSON.parse(xhr.responseText).errorMessage;
                    if (errorMessage == undefined || errorMessage == '') {
                        errorMessage = "Some issue occured. Please try again!"
                    }
                    pwIsf.alert({ msg: errorMessage, type: 'error' });
                }
            });
        }
    }
    else if (isMarkCompleteNeeded) {
        completeWorkOrder(woId);
    }
}

function stopDisabledStepFromFlowChart() {
    let proficiencyLevel = urlParams.get('proficiencyLevel');
    let woId = urlParams.get("woID");
    let valSelectorObj = window.parent.$('.dBox-content #hiddenVal_' + woId);
    let buttonSelectorObj = window.parent.$('#inprogressTask_' + woId + ' .stopTaskBtn');

    if (proficiencyLevel == 2 && valSelectorObj.length != 0 && valSelectorObj.data('exectype') == C_EXECUTION_TYPE_MANUALDISABLED) {

        let subActivityFlowChartDefID = valSelectorObj.data('defid');
        let stepID = valSelectorObj.data('stepid');
        let taskID = valSelectorObj.data('taskid');
        let bookingID = valSelectorObj.data('bookingid');
        let executionType = valSelectorObj.data('exectype');
        let bookingType = buttonSelectorObj.data('bookingtype');
        let flowChartType = buttonSelectorObj.data('flowcharttype');

        let stopDisabledStepDetails = new Object();
        stopDisabledStepDetails.flowChartType = flowChartType;
        stopDisabledStepDetails.woID = woId;
        stopDisabledStepDetails.subActivityFlowChartDefID = subActivityFlowChartDefID;
        stopDisabledStepDetails.stepID = stepID;
        stopDisabledStepDetails.taskID = taskID;
        stopDisabledStepDetails.executionType = executionType;
        stopDisabledStepDetails.bookingID = bookingID;
        stopDisabledStepDetails.bookingType = bookingType;

        window.parent.stopDisabledStep(stopDisabledStepDetails, '', woId, false, false);

    }
}

function getGraphCells() {
    var shapeJson = app.graph.toJSON().cells;
    let currentRunningPosition = null;
    for (var shape = 0; shape < shapeJson.length; shape++) {
        if (shapeJson[shape].type === C_STEP_TYPE_DECISION) {
            currentRunningPosition = shapeJson[shape].position;
            break;
        }
    }
    app.paperScroller.scroll(currentRunningPosition.x, currentRunningPosition.y);
}

function CheckIfPreviousStepCompleted(woID, taskID, subActivityFlowChartDefID, stepID) {
    let shouldProceed = false;
    $.isf.ajax({
        type: "GET",
        async: false,
        url: `${service_java_URL}woExecution/checkIFPreviousStepCompleted/${woID}/${taskID}/${subActivityFlowChartDefID}/${stepID}`,
        success: function (data) {
            if (data.responseData.allowed) {
                shouldProceed = true;
            }
            else {
                pwIsf.alert({ msg: " Please complete previous task/step. ", type: 'error' });
                pwIsf.removeLayer();
            }
        },
        error: function (xhr, status, statusText) {
            var err = JSON.parse(xhr.responseText);
            pwIsf.removeLayer();
            pwIsf.alert({ msg: err.errorMessage, type: 'error' });
        }
    });
    return shouldProceed;
}

//Start disabled step
function initiateDisabledStep(flowChartType, woid, projId, vNum, isCallingFromFlowChart = false) {
    let disabledStepDetails = isCallingFromFlowChart ? JSON.parse(window.parent.$('#woButtons_' + woid + ' .disabledStepObj').val()) : JSON.parse($('#woButtons_' + woid + ' .disabledStepObj').val());
  
    // CheckIfPreviousStepCompleted before start Task
    let ShouldProceed = CheckIfPreviousStepCompleted(woid, disabledStepDetails.taskID, disabledStepDetails.flowChartDefID, disabledStepDetails.flowChartStepId)

    if (ShouldProceed)
    {
        if (disabledStepDetails != null) {

            let startTaskInput;
            let taskId = disabledStepDetails.taskID;
            let flowChartDefID = disabledStepDetails.flowChartDefID;
            let executionType = disabledStepDetails.executionType;
            let stepId = disabledStepDetails.flowChartStepId;
            let bookingStatus = "";
            let bookingId = disabledStepDetails.bookingId;
            let botConfigData = null;
            let botType = "";
            let fileID = null;
            let stepName = null;
            let outputUpload = null;
            startTaskInput = PrepareStartFlowChartTaskInput(flowChartType, woid, projId, taskId, flowChartDefID, executionType, stepId, bookingStatus, bookingId, botConfigData, botType, fileID, executionType, vNum, stepName, outputUpload);
            executeDisabledStep(startTaskInput, isCallingFromFlowChart);
        } else {
            pwIsf.alert({ msg: 'No disabled step details found!', type: 'error' });
        }
    }
 


}

//play next disabled step
function playNextDisabledStepFromFlowChart(nextStepCell, eleattr) {

    let targetID = nextStepCell.id;
    let flowChartType = urlParams.get('flowchartType');
    let woid = urlParams.get("woID");
    let prjID = urlParams.get("prjID");
    let nextTaskID = nextStepCell.attrs.task.taskID;
    let nextExecType = C_EXECUTION_TYPE_MANUALDISABLED;
    let nextBookingStatus = nextStepCell.attrs.task.status;
    let nextBookingID = nextStepCell.attrs.task.bookingID;
    let nextRPAID = nextStepCell.attrs.tool.RPAID;
    let nextStepName = nextStepCell.name;
    let nextBotType = nextStepCell.botType;
    let nextRunOnServer = nextStepCell.attrs.tool.isRunOnServer;
    let isInputRequiredNext = nextStepCell.attrs.tool.isInputRequired;
    let nextOutputUpload = nextStepCell.outputUpload;
    if (nextRPAID == "") { nextRPAID = 0; }
    
    playNextStep(flowChartType, isInputRequiredNext, nextBotType, nextRPAID, woid, prjID, versionNo, eleattr.id, nextTaskID,
        subActivityFlowChartDefID, nextBookingID, targetID, nextBookingStatus, nextExecType, eleattr.attrs.task.taskID, bookingID, eleattr.attrs.task.executionType, nextStepName, nextOutputUpload, nextRunOnServer, true);

}

//check if any step is in onhold or started status
function isFlowChartCompletionValidated(woid) {

    let proficiencyLevel = $('#togBtnForFlowChartViewMode_' + woid).is(":checked") ? 2 : 1;
    let allCells = document.getElementById('iframe_' + woid).contentWindow.app.graph.toJSON().cells;

    //check enabled steps status
    let anyStepIncomplete = allCells.some(el => el.attrs.task ? el.attrs.task.status == C_BOOKING_STATUS_STARTED
        || el.attrs.task.status == C_BOOKING_STATUS_ONHOLD : false);

    let isDisabledIncomplete = false;

    if (proficiencyLevel == 2) {
        let disabledStepDetails = JSON.parse($('#woButtons_' + woid + ' .disabledStepObj').val());
        let disabledStepStatus = disabledStepDetails.status;

        //check disabled step status
        isDisabledIncomplete = disabledStepStatus == C_BOOKING_STATUS_STARTED || disabledStepStatus == C_BOOKING_STATUS_ONHOLD ? true : false;
    }

    let allowCompletion = !anyStepIncomplete && !isDisabledIncomplete;

    return allowCompletion;
}

function prevCellDecissionAndImmediateNextStepDisabledManual(cellId, arrAllShapes) {
    const prevCellId = app.graph.getCell(cellId).getSourceElement().id;
    const prevCell = arrAllShapes.filter(function (el) { return el.id === prevCellId })[0];
    displayPlayIconsOnOutboundLink(prevCell, arrAllShapes);

}

function displayPlayIconsOnOutboundLink(stepDetail, arrAllShapes) {
    let link = [];
    const outboundlink = app.graph.getConnectedLinks(stepDetail, { outbound: true });
    for (let m in outboundlink) {
        link = arrAllShapes.filter(function (el) { return el.id === outboundlink[m].get('id'); });
        link[0].showIcons = true;
    }
}

function commonErrorBlock(xhr) {
    var err = JSON.parse(xhr.responseText);
    pwIsf.alert({ msg: err.errorMessage, type: 'error' });
    window.parent.pwIsf.removeLayer();
}

// .zip validation for server bot input file url
function checkInputFileValidationForServerBot(inputFileUrl) {
    if (inputFileUrl == null || inputFileUrl === "") {
        pwIsf.alert({ msg: C_SERVER_BOT_EMPTY_INPUT_URL_MSG, type: C_WARNING });
        return false;
    }
    else {
        let fileExtensionIsZip = checkZipValidation(inputFileUrl);
        if (fileExtensionIsZip) {
            return true;
        }
        else {
            pwIsf.alert({ msg: C_SERVER_BOT_ZIP_VALIDATION_MSG, type: C_WARNING });
            return false;
        }
    }
}

//extract the file extension from input file URL
function checkZipValidation(inputUrl) {
    var inputFileName = inputUrl.split("/");
    inputFileName = (inputUrl.lastIndexOf('/') !== inputUrl.length - 1 ?
        inputFileName[inputFileName.length - 1] :
        inputFileName[inputFileName.length - 2]);

    let regexForZip = new RegExp("(.*?)\.(zip)");

    return regexForZip.test(inputFileName);
}

// validate input url for server bot through API
function validateInputUrlForServerBot(inputFileUrl, projectID) {
    let inputFileURL = inputFileUrl.toString();
    let validateInputUrlStatus;
    $.isf.ajax({
        url: `${service_java_URL}woExecution/validateServerBotInputUrl?projectID=${projectID}&inputUrl=${inputFileURL}`,
        type: "GET",
        async: false,
        success: function (data) {
            if (!data.isValidationFailed) {
                validateInputUrlStatus = data.responseData;
            }
            else {
                responseHandler(data);
            }
        },
        error: function (xhr) {
            responseHandler(data);
        }
    });
    return validateInputUrlStatus;
}

// Check is value is valid
function isNullEmptyOrUndefined(value) {
    if (value === null || value === undefined || value === "") {
        return true;
    }

    return false;
}

// check if sharepoint details configured with market area
function checkSharepointConfiguredWithMA(projectID) {
    let checkMarketAreaConfig;
   
    $.isf.ajax({
        url: `${service_java_URL}woExecution/validateSharepointDetailsWithMarketArea?projectID=${projectID}`,
        type: "GET",
        async:false,
        success: function (data) {
            if (!data.isValidationFailed) {
                checkMarketAreaConfig = data.responseData;
            }
            else {
                responseHandler(data);
            }
        },
        error: function (xhr) {
            // Blank
        }
    });

    return checkMarketAreaConfig;
}


function getOutputUploadFromAPIUsingStepId(stepID, subActivityFlowChartDefID) {
    let getOutputUpload;

    $.isf.ajax({
        url: `${service_java_URL}woExecution/getOutPutUploadByStepID?stepID=${stepID}&flowChartDefID=${subActivityFlowChartDefID}`,
        type: "GET",
        async: false,
        success: function (data) {
            if (!data.isValidationFailed) {
                getOutputUpload = data.responseData;
            }
            else {
                responseHandler(data);
            }
        },
        error: function (xhr) {
            // Blank
        }
    });

    return getOutputUpload;
}
