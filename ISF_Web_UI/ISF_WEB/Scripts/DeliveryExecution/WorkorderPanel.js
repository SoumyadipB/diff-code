//START - WORK ORDER AREA
const flagCallDeliveryPerformancePopup = false;

var nodeNamesValidated = false;
var nodeNamesValidatedWarning = false;
var isLeadTimeBreached = {};
let globalWorkOrderData = [];
var pasteOrInput;
const textAreaForNELeadTime = $("#textAreaForNELeadTime");
const myCurrentWOpanelMaxHeight = '300px';
const myCurrentWOpanelMiddHeight = '97px';
const nodeBoxNE = $("#node_box_DE");
const techIDList = $('#technologyWarning');
const domainIdList = $('#domainIdWarning');
const projIdWarning = $('#projectIdWarning');
let neMandateOrOptional = false;
let nodesUpdateSuccess = true;
let warningMsg = "Network Element needed for the work order";

function resSetShowAndHideWorkOrderBtn() {
    var obj = {};
    obj.targetObj = '#showWOToTop';
    obj.hasClassName = 'fa-arrow-up';
    obj.newClass = 'fa-arrow-down';
    obj.title = 'Show work order';

    resetbtnWhichHasI(obj);
}

function resetDropdownLabel(dropdownLblTarget, label, seacrhText) {

    $(dropdownLblTarget).text(label).attr('data-searchtext', seacrhText);

}

function disableViewFlowChartAction() {
    $('.flow_chart_main_panel .panel-body > div.flow_chart_single_box').each(function () {
        const that = this;
        const getDetails = JSON.parse($(that).attr('data-details'));
        const woId = getDetails.woid;
        $('#viewBtn_' + woId).attr('disabled', true);
        const percentage = $(`#progressBarOnFlowchartHeader_${woId}`).find('.progress-bar-success').text().trim().replace('%', '');
        getWOprogressBarHtml({ percentage: percentage, perText: '', woId: woId, progressBarFor: '' });
    });
}

//CREATE DUPLICATE WO
function createDuplicateWO(woID, projectID, comment) {
    if (comment == null || comment.length === 0) {
        pwIsf.alert({ msg: 'Please give reason...', type: 'warning' });
    }
    else if (woID == null || woID.length === 0 || signumGlobal == null || signumGlobal.length === 0 || projectID == null) {
        pwIsf.alert({ msg: 'Something went wrong', type: 'warning' });
    }
    else {

        $.isf.ajax({
            type: "POST",
            url: `${service_java_URL}woExecution/createDuplicateWorkOrder/${woID}/${projectID}/${signumGlobal}/${comment}`,
            success: function (data) {
                $('#modalDuplicate').modal('hide');
                location.reload();
            },
            error: function (xhr, status, statusText) {
                var err = JSON.parse(xhr.responseText);
                pwIsf.alert({ msg: err.errorMessage, type: 'error' });
            }
        });
    }
}

function searchByDate(signum, startDate, endDate) {
    var table = $('#plannedProjectListTab').DataTable();
    if ($.fn.dataTable.isDataTable('#plannedProjectListTab')) {
        table.destroy();
        $("#plannedProjectList_tbody").html('');
    }
}

$('body').on('keyup', '#nodeView', function (e) {
    $('#validStatusWarning').css('display', 'none');
});

function getElementNamesObj() {

    var elementIdPrefix = "workOrderViewIdPrefix_";
    var controlNames = ['project_name', 'estimated_effort', 'wo_name', 'priority', 'start_time', 'start_date', 'wo_id', 'project_id', 'assigned_to',

        'domain-subdomain', 'technology', 'service-area-sub-service-area', 'activity-subactivity',

        'market', 'vendor', 'sitetype-node-type', 'node-site-name', 'new-node-site', 'node_master_select'
    ];

    var controlObj = {};

    for (var i = 0; i < controlNames.length; i++) {
        controlObj[controlNames[i]] = $('#' + elementIdPrefix + controlNames[i]);
    }

    return controlObj;
}

function getValuesFromApiResponse(data) {

    var retElementsArrVal = [];

    retElementsArrVal['project_name'] = data.responseData[0].project;
    retElementsArrVal['project_id'] = data.responseData[0].projectID;
    retElementsArrVal['estimated_effort'] = data.responseData[0].effort;
    retElementsArrVal['wo_name'] = data.responseData[0].wOName;
    retElementsArrVal['priority'] = data.responseData[0].priority;
    retElementsArrVal['start_time'] = data.responseData[0].startTime;
    retElementsArrVal['start_date'] = data.responseData[0].startDate;
    retElementsArrVal['wo_id'] = data.responseData[0].wOID;
    retElementsArrVal['assigned_to'] = data.responseData[0].signumID;
    retElementsArrVal['domain-subdomain'] = data.responseData[0].domain;
    retElementsArrVal['technology'] = data.responseData[0].technology;
    retElementsArrVal['service-area-sub-service-area'] = data.responseData[0].serviceArea;
    retElementsArrVal['activity-subactivity'] = data.responseData[0].activity;
    retElementsArrVal['market'] = data.responseData[0].market;
    retElementsArrVal['vendor'] = data.responseData[0].vendor;
    retElementsArrVal['sitetype-node-type'] = data.responseData[0].listOfNode[0].nodeType;
    retElementsArrVal['node-site-name'] = data.responseData[0].listOfNode;
    retElementsArrVal['new-node-site'] = 'Missing';

    return retElementsArrVal;
}

function getDateFormat(date) {
    var d = new Date(date),
        month = `${(d.getMonth() + 1)}`,
        day = `${d.getDate()}`,
        year = d.getFullYear();
    if (month.length < 2) {
        month = `0${month}`;
    }
    if (day.length < 2) {
        day = `0${day}`;
    }
    return [day, month, year].join('-');
}

function setValuesToElements(elementsObj, valuesArr) {

    valuesArr['start_date'] = GetConvertedDate(`${new Date(valuesArr['start_date']).toDateString()} ${valuesArr['start_time']}`);
    valuesArr['start_time'] = GetConvertedTime(`${new Date(valuesArr['start_date']).toDateString()} ${valuesArr['start_time']}`);

    elementsObj['project_name'].val(valuesArr['project_name']);
    elementsObj['estimated_effort'].val(valuesArr['estimated_effort']);
    elementsObj['wo_name'].val(valuesArr['wo_name']);
    elementsObj['wo_id'].val(valuesArr['wo_id']);
    elementsObj['project_id'].val(valuesArr['project_id']);
    elementsObj['assigned_to'].val(valuesArr['assigned_to']);
    elementsObj['priority'].html('');
    elementsObj['priority'].append($('<option>', {
        value: valuesArr['priority'],
        text: valuesArr['priority']
    }));

    elementsObj['priority'].val(valuesArr['priority']);
    elementsObj['start_time'].val(valuesArr['start_time']);

    var formatted;
    formatted = getDateFormat(valuesArr['start_date']);

    elementsObj['start_date'].val(formatted);
    elementsObj['domain-subdomain'].val(valuesArr['domain-subdomain']);
    elementsObj['technology'].val(valuesArr['technology']);
    elementsObj['service-area-sub-service-area'].val(valuesArr['service-area-sub-service-area']);
    elementsObj['activity-subactivity'].val(valuesArr['activity-subactivity']);
    elementsObj['node-site-name'].html('');
    elementsObj['node_master_select'].html('');

    let prePopNodeName = '';
    let nodeNamesArr = [];
    let nodeNamesUnique = '';
    for (var i = 0; i < valuesArr['node-site-name'].length; i++) {
        prePopNodeName = prePopNodeName + ',' + valuesArr['node-site-name'][i].nodeNames;
    }

    prePopNodeName = prePopNodeName.replace(/(^,)|(,$)/g, "");

    if (prePopNodeName === null || prePopNodeName === "null") {
        prePopNodeName = "";
    }
    else {
        nodeNamesArr = prePopNodeName.split(',');
        nodeNamesUnique = jQuery.unique(nodeNamesArr.sort()).join(',');
    }
    elementsObj['new-node-site'].val(nodeNamesUnique);
    $('#workOrderViewIdPrefix_savedNodes').val(nodeNamesUnique);

}

function cleanNodeFieldsWOPanel() {
    hideNEAddEdit();
    $("#workOrderViewIdPrefix_sitetype-node-type").find('option').not(':first').remove();
    $('#workOrderViewIdPrefix_node-site-name_multi')
        .find('option')
        .remove();
    $("#workOrderViewIdPrefix_node-site-name_multi").empty();
    $('#btn_update_nodes').attr("disabled", "disabled");
    $('#btn_update_project').attr("disabled", "disabled");
    $('.projectDetailsIcon').hide();
    $('.nodeDetailsIcon').show();
    $('#EditNodeType').hide();
    $('#viewNodeType').show();
    $('#EditNodeType2').hide();
    $('#viewNodeType2').show();
    $('#viewMarketArea').hide();
    $('#viewMarket').show();
    $('#viewVendorArea').hide();
    $('#viewVendor').show();
    $("#workOrderViewIdPrefix_new-node-site").attr("readonly", "readonly");
}

function addMoreNodes() {
    nodeNamesValidated = false;
    $("#workOrderViewIdPrefix_new-node-site").prop('readonly', false);
    $('#addNodesValid').hide();
    $('#changeIcon').hide();
}

//MARK AS DEFFERED OR ONHOLD FUNCTION
function updateWOStatus(woID, markStatus, comment, priority) {
    var isCreateSubSequentWO;
    if ($('#isCreateSubSequentWODef').prop('checked') == true) {
        isCreateSubSequentWO = true;
    }
    else {
        isCreateSubSequentWO = false;
    }
    if (comment == null || comment.length === 0) {
        pwIsf.alert({ msg: 'Please give Reason ...', type: 'warning' });
    }
    else if (woID == null || woID.length === 0 || markStatus == null || markStatus.length === 0 || markStatus.length >= 500 || signumGlobal == null || signumGlobal.length === 0) {
        pwIsf.alert({ msg: 'Something went wrong...', type: 'error' });
    }
    else if (comment.length > 1000) {
        pwIsf.alert({ msg: 'Comment should be less than 1000 characters', type: 'error' });
    }
    else {
        if (priority == undefined) {
            priority = '';
        }
        var woDetails = {
            'wOID': woID,
            'statusComment': comment,
            'deliveryStatus': markStatus,
            'priority': priority,
            'signumID': signumGlobal,
            'isCreateSubSequentWO': isCreateSubSequentWO
        };
        pwIsf.addLayer({ text: C_PLEASE_WAIT });
         $.isf.ajax({
            type: "POST",
            contentType: C_CONTENT_TYPE_APPLICATION_JSON,
            url: `${service_java_URL}woExecution/updateWorkOrderStatus`,
            dataType: 'html',
            data: JSON.stringify(woDetails),
            success: function (data) {
                const woIDDetails = JSON.parse($('#viewBtn_' + woDetails.wOID).attr('data-details'));
                //clear locale storage
                if (localStorage.getItem('ASUSERINPUT_' + woDetails.wOID) != null || localStorage.getItem('ASUSERINPUT_' + woDetails.wOID) != undefined) {
                    localStorage.setItem('ASUSERINPUT_' + woDetails.wOID, null);
                    localStorage.removeItem('ASUSERINPUT_' + woDetails.wOID);
                }
                if (woIDDetails.workOrderAutoSenseEnabled == true) {
                   const autoSenseInputData = new Object();
                    autoSenseInputData.WoId = woDetails.wOID;
                    autoSenseInputData.flowchartDefID = woIDDetails.subActivityFlowChartDefID;
                    autoSenseInputData.stepID = "0";
                    autoSenseInputData.source = "UI";
                    autoSenseInputData.signumID = signumGlobal;
                    autoSenseInputData.overrideAction = "SUSPEND";
                    autoSenseInputData.action = "";
                    autoSenseInputData.taskID = "0";
                    autoSenseInputData.startRule = null;
                    autoSenseInputData.stopRule = null;
                    NotifyClientOnWorkOrderSuspended(autoSenseInputData, true); 
                }
                $('#modalHold').modal('hide');
                $('#modalDeffered').modal('hide');
                location.reload();
            },
            error: function (xhr, status, statusText) {
                var err = JSON.parse(xhr.responseText);
                pwIsf.alert({ msg: err.errorMessage, type: 'error' });
            }
        });
    }
}

function displayEfficiencyGauge(woId, projectId) {
    const getWoId = woId;
    const getProjectId = projectId;
    const signam = signumGlobal;
    const markAsComplete = 1;
    const url = `${service_java_URL}woExecution/getEfficiencyDevlieryIndexForUser?projectID=${getProjectId}
    &subActivityID=0&flowChartDefID=0&woID=${getWoId}&markAsComplete=${markAsComplete}&signumID=${signam}`;
    $.isf.ajax({
        url: url,
        success: function (data) {
            $('#modalCompleted').modal('hide');
            if (data['AfterExecution'][0]) {
                makeAllGauges(data);
            } else {
                pwIsf.removeLayer();
                $('#modalEEIndex').modal('show');
                $('#allGaugeContainerArea').append('DATA NOT FOUND');
            }
        },
        error: function (xhr, status, statusText) {
            pwIsf.removeLayer();
            var err = JSON.parse(xhr.responseText);
            pwIsf.alert({ msg: err.errorMessage, type: 'error' });
        },
    });

    let makeAllGauges = (getData) => {
        const marketAreaName = getData['AfterExecution'][0]['MarketAreaName'];
        const customerName = getData['AfterExecution'][0]['CustomerName'];
        const subActivityName = getData['AfterExecution'][0]['SubActivity'];
        $('#marketNameForDelPerformance').text(marketAreaName);
        $('#customerNameForDelPerformance').text(customerName);
        $('#subactivityNameForDelPerformance').text(subActivityName);
        const allGauge = ['E_PER', 'SLA_PER', 'FTR_PER', 'DP_PER'];
        const nameMapped = { E_PER: 'EFiciencyIndex', SLA_PER: 'SLA', FTR_PER: 'FTR', DP_PER: 'DeliveryIndex' };
        const titleObj = { E_PER: 'Engineer Efficiency (%)', SLA_PER: 'SLA (%)', FTR_PER: 'FTR (%)', DP_PER: 'Individual delivery performance (%)' };
        const minAndMax = {
            E_PER: { min: 0, max: 200 }, SLA_PER: { min: 50, max: 100 }, FTR_PER: { min: 50, max: 100 }, DP_PER: { min: 0, max: 200 }
        };
        const plotBand = {
            E_PER: [{
                from: 0,
                to: 80,
                color: '#DF5353' // red
            }, {
                from: 80,
                to: 100,
                color: '#DDDF0D' // yellow
            }, {
                from: 100,
                to: 120,
                color: '#00ff21' // light green
            }, {
                from: 120,
                to: 200,
                color: '#55BF3B' //  green
            }],
            SLA_PER: [{
                from: 50,
                to: 90,
                color: '#DF5353' // red

            }, {
                from: 90,
                to: 95,
                color: '#DDDF0D' // yellow
            }, {
                from: 95,
                to: 100,
                color: '#55BF3B' // green
            }],
            FTR_PER: [{
                from: 50,
                to: 90,
                color: '#DF5353' // red

            }, {
                from: 90,
                to: 95,
                color: '#DDDF0D' // yellow
            }, {
                from: 95,
                to: 100,
                color: '#55BF3B' // green
            }],
            DP_PER: [{
                from: 0,
                to: 80,
                color: '#DF5353' // red

            }, {
                from: 80,
                to: 100,
                color: '#DDDF0D' // yellow
            }, {
                from: 100,
                to: 120,
                color: '#00ff21' // light green
            }, {
                from: 120,
                to: 200,
                color: '#55BF3B' // green
            }]
        };

        let seriesData = {};

        const makeTitle = (gaugeType) => {
            return titleObj[gaugeType];
        };

        $('#allGaugeContainerArea').empty();
        let divHtml = `<div class="row">`;
        let colClass = 'col-md-4';
        let gaugeW = 600;
        let gaugeHeight = 180;
        for (let d in allGauge) { //Create div for gauges and append
            divHtml += `<div class="${colClass}"><div id="gaugeContainer${allGauge[d]}"
            style="min-width: 100px; max-width: ${gaugeW}px;
            height: ${gaugeHeight}px; margin: 0 auto"></div></div>`;
            if (d == 2) {
                colClass = 'col-md-12';
                gaugeW = 600;
                gaugeHeight = 210;
                divHtml += '</div><div class="row">';
            }

        }
        $('#allGaugeContainerArea').append(divHtml + '</div>');

        seriesData = {
            E_PER: [{
                name: 'E-PER',
                data: [getData['AfterExecution'][0][nameMapped['E_PER']]],

            }],
            SLA_PER: [{
                name: 'SLA-PER',
                data: [getData['AfterExecution'][0][nameMapped['SLA_PER']]],

            }],
            FTR_PER: [{
                name: 'FTR-PER',
                data: [getData['AfterExecution'][0][nameMapped['FTR_PER']]],

            }],
            DP_PER: [{
                name: 'DP-PER',
                data: [getData['AfterExecution'][0][nameMapped['DP_PER']]],

            }]

        }

        for (let i in allGauge) {
            const getTitle = makeTitle(allGauge[i]);
            createGauge('gaugeContainer' + allGauge[i], getTitle, seriesData[allGauge[i]], minAndMax[allGauge[i]], plotBand[allGauge[i]]);
        }

        pwIsf.removeLayer();
        $('#modalEEIndex').modal('show');
    };


}

function getEfficiencyTrend() {
    pwIsf.addLayer({ text: C_PLEASE_WAIT });
    const url = `${service_java_URL}externalInterface/getTableauReport`;
    $.isf.ajax({
        url: url,
        success: function (data) {
            if (data) {
                window.open('https://tbsrvp.internal.ericsson.com/t/ISFAnalytics/views/ISFEE_0/Reports?iframeSizedToWindow=true&:embed=y&:showAppBanner=false&:display_count=no&:showVizHome=no', "", "height=400;width=850;");
            } else {
                pwIsf.alert({ msg: 'Could not found efficiency trend.', type: 'error' });
            }
            pwIsf.removeLayer();
        },
        error: function (xhr, status, statusText) {
            pwIsf.removeLayer();
            var err = JSON.parse(xhr.responseText);
            pwIsf.alert({ msg: err.errorMessage, type: 'error' });
        },
    });
}

//MARK AS COMPLETE WO FUNCTION
function workOrderClosure(woID, woName, delStatus, delReason, comment, subActivityFlowChartDefID, reasonCycleTime, cycleTime, projectId, subActivityId, wfId) {
    var isCreateSubSequentWO;
    commentBoxMandatoryCheck();
    if ($('#isCreateSubSequentWO').prop('checked') == true) {
        isCreateSubSequentWO = true;
    }
    else {
        isCreateSubSequentWO = false;
    }


    if (delStatus == null || delStatus.length === 0) {
        pwIsf.alert({ msg: 'Please select the delivery status', type: 'warning' });
    }
    else if (cycleTime < 0 && reasonCycleTime.length === 0) {
        pwIsf.alert({ msg: 'Please select Cycle Time Breach Reason', type: 'warning' });
    }
    else if (signumGlobal == null || signumGlobal.length === 0 || woID == null || woID.length === 0 || woName == null || woName.length === 0) {
        pwIsf.alert({ msg: 'Something went wrong!!', type: 'warning' });
    }
    else if (delReason.length >= 500) {
        pwIsf.alert({ msg: 'Reason should be less than 500 characters', type: 'warning' });
    }
    else if (comment.length > 1000) {
        pwIsf.alert({ msg: 'Comment should be less than 1000 characters', type: 'error' });
    }
    else if (!comment.length && delStatus === "Success" && delReason === "Others") {
        pwIsf.alert({ msg: 'Please fill comment for delivery reason others', type: 'warning' });
    }
    else if (delStatus === "Failure" && (delReason == null || delReason.length === 0)) {
        pwIsf.alert({ msg: 'Please select the reason of failure', type: 'warning' });
    }
    else if (delStatus === "Success" && !delReason) {
        pwIsf.alert({ msg: 'Please select the reason of Success', type: 'warning' });
    }
    else if (comment.includes('"')) {
        pwIsf.alert({ msg: 'Double Quotes are not allowed. Please remove them and try saving again.', type: 'warning' });
    }

    else if (comment.length > 1000) {
        pwIsf.alert({ msg: `Comment Characters ${comment.length} are greater than 1000. try removing some characters and spaces then try again.`, type: 'warning' });
    }
    else {
        pwIsf.addLayer({ text: C_PLEASE_WAIT });
           if (delStatus === "Success") {
            var wODetailsSuccess = {
                "wOID": woID,
                "wOName": unescape(woName),
                "deliveryStatus": delStatus,
                "reason": encodeURIComponent(delReason),
                "statusComment": encodeURIComponent(comment),
                "lastModifiedBy": signumGlobal,
                "subactivityDefID": subActivityFlowChartDefID,
                "isCreateSubSequentWO": true
            };
            const wfDetailsWithUserProficiency = new Object();
            const subActivityidListArr = [];
            const subActivityidListObj = new Object();

            subActivityidListObj.signumID = signumGlobal;
            subActivityidListObj.subactivityID = subActivityId;
            subActivityidListObj.wfID = wfId;
            subActivityidListObj.woID = woID;

            subActivityidListArr.push(subActivityidListObj);

            wfDetailsWithUserProficiency.subActivityWfIDModel = subActivityidListArr;
            wfDetailsWithUserProficiency.projectID = projectId;
            wfDetailsWithUserProficiency.proficiencyMeasurement = C_PROFICIENCY_FORWARD;
            wfDetailsWithUserProficiency.createdBy = signumGlobal;
            $.isf.ajax({
                type: "GET",
                url: `${service_java_URL}woExecution/checkIFLastStepNew/${woID}/${subActivityFlowChartDefID}`,
                success: function (data) {
                    pwIsf.removeLayer();
                    if (data.isValidationFailed == false) {
                        if (data.responseData == false) {
                            pwIsf.alert({ msg: "Please complete all the tasks of this work order to close this.", type: 'warning' });
                        }
                        else {
                            $.isf.ajax({
                                type: "POST",
                                contentType: C_CONTENT_TYPE_APPLICATION_JSON,
                                url: `${service_java_URL}woExecution/saveClosureDetailsForWO`,
                                dataType: 'html',
                                data: JSON.stringify(wODetailsSuccess),
                                success: function (data) {
                                    data = JSON.parse(data);
                                    if (data.isValidationFailed) {
                                        pwIsf.removeLayer();
                                        errorHandler(data);
                                    }
                                    else {
                                        cycleTimeReasonPost();
                                        $('#modalCompleted').modal('hide');
                                        if (flagCallDeliveryPerformancePopup) {
                                            displayEfficiencyGauge(woID, projectId);
                                        } else {
                                            $.isf.ajax({
                                                type: "POST",
                                                async: false,
                                                contentType: C_CONTENT_TYPE_APPLICATION_JSON,
                                                url: `${service_java_URL}/woExecution/saveWFUserProficiency`,
                                                data: JSON.stringify(wfDetailsWithUserProficiency),
                                                success: function (d) {
                                                    console.log(d);
                                                },
                                                error: function (xhr, status, statusText) {
                                                    console.log('error');
                                                }
                                            });

                                            pwIsf.alert({ msg: 'Work order closed successfully', type: 'success' });

                                            const woIDDetails = JSON.parse($('#viewBtn_' + wODetailsSuccess.wOID).attr('data-details'));
                                            if (localStorage.getItem('ASUSERINPUT_' + wODetailsSuccess.wOID) != null ||
                                                localStorage.getItem('ASUSERINPUT_' + wODetailsSuccess.wOID) !== undefined) {
                                                localStorage.setItem('ASUSERINPUT_' + wODetailsSuccess.wOID, null);
                                                localStorage.removeItem('ASUSERINPUT_' + wODetailsSuccess.wOID);
                                            }
                                            if (woIDDetails.workOrderAutoSenseEnabled == true) {
                                                let autoSenseInputData = new Object();
                                                autoSenseInputData.WoId = wODetailsSuccess.wOID;
                                                autoSenseInputData.flowchartDefID = woIDDetails.subActivityFlowChartDefID;
                                                autoSenseInputData.stepID = "0";
                                                autoSenseInputData.source = "UI";
                                                autoSenseInputData.signumID = signumGlobal;
                                                autoSenseInputData.overrideAction = "SUSPEND";
                                                autoSenseInputData.action = "";
                                                autoSenseInputData.taskID = "0";
                                                autoSenseInputData.startRule = null;
                                                autoSenseInputData.stopRule = null;
                                                NotifyClientOnWorkOrderSuspended(autoSenseInputData, true);
                                            }
                                            setTimeout(function () {
                                                location.reload();

                                            }, 3500);
                                        }
                                    }
                                },
                                error: function (xhr, status, statusText) {
                                    pwIsf.removeLayer();
                                    var err = JSON.parse(xhr.responseText);
                                    pwIsf.alert({ msg: err.errorMessage, type: 'error' });
                                }
                            });
                        }
                    }
                    else {
                        responseHandler(data);
                    }
                },
                error: function (xhr, status, statusText) {
                    pwIsf.removeLayer();
                    var err = JSON.parse(xhr.responseText);
                    pwIsf.alert({ msg: err.errorMessage, type: 'error' });
                }
            });
         }
           else if (delStatus == "Failure") {
            cycleTimeReasonPost();
            var wODetailsFailure = {
                "wOID": woID,
                "wOName": unescape(woName),
                "deliveryStatus": delStatus,
                "reason": encodeURIComponent(delReason),
                "statusComment": encodeURIComponent(comment),
                "lastModifiedBy": signumGlobal,
                "subactivityDefID": subActivityFlowChartDefID,
                "isCreateSubSequentWO": isCreateSubSequentWO
            };
            $.isf.ajax({
                type: "POST",
                contentType: C_CONTENT_TYPE_APPLICATION_JSON,
                url: `${service_java_URL}woExecution/saveClosureDetailsForWO`,
                dataType: 'html',
                data: JSON.stringify(wODetailsFailure),
                success: function (data) {
                    data = JSON.parse(data);
                    if (data.isValidationFailed) {
                        pwIsf.removeLayer();
                        errorHandler(data);
                    }
                    else {
                        $('#modalCompleted').modal('hide');
                        pwIsf.alert({ msg: 'Work order closed successfully', type: 'success' });
                        const woIDDetails = JSON.parse($('#viewBtn_' + wODetailsFailure.wOID).attr('data-details'));
                        if (woIDDetails.workOrderAutoSenseEnabled == true) {
                            let autoSenseInputData = new Object();
                            autoSenseInputData.WoId = wODetailsFailure.wOID;
                            autoSenseInputData.flowchartDefID = woIDDetails.subActivityFlowChartDefID;
                            autoSenseInputData.stepID = "0";
                            autoSenseInputData.source = "UI";
                            autoSenseInputData.signumID = signumGlobal;
                            autoSenseInputData.overrideAction = "SUSPEND";
                            autoSenseInputData.action = "";
                            autoSenseInputData.taskID = "0";
                            autoSenseInputData.startRule = null;
                            autoSenseInputData.stopRule = null;
                            NotifyClientOnWorkOrderSuspended(autoSenseInputData, true);
                        }
                        setTimeout(function () {
                            location.reload();
                        }, 3000);
                    }
                },
                error: function (xhr, status, statusText) {
                    var err = JSON.parse(xhr.responseText);
                    pwIsf.alert({ msg: err.errorMessage, type: 'error' });
                }
            });
         }
         }
}

//TRANSFER WO FUNCTION
function transferWOInProgress(woId, senderSignum, stepName, userComments) {
    if (JSON.parse(ActiveProfileSession).accessProfileName === "ASP") {
        senderSignum = $('#selectAspUserSignumTransfer').val()
    }
    if (stepName == null || stepName.length === 0 || stepName === '-1') {
        pwIsf.alert({ msg: 'Please Select Step Name', type: 'warning' });
        return;
    }
    if (userComments.length > 250) {
        pwIsf.alert({ msg: 'Please Comment within 250 characters', type: 'warning' });
        return;
    }
    if (senderSignum == null || senderSignum.length === 0 || senderSignum.length !== 7) {
        pwIsf.alert({ msg: 'Please select the sender Signum', type: 'warning' });
        $('#submitButtonMyWorkTab').attr('disabled', true);
    }
    else if (signumGlobal == null || signumGlobal.length === 0 || woId == null || woId.length === 0) {
        pwIsf.alert({ msg: 'Something went wrong!!', type: 'warning' });
    }
    else if (signumGlobal.trim() === senderSignum.toLowerCase().trim()) {
        pwIsf.alert({
            msg: "Work Order(s) already assigned to you,Please select someone else's signum for transfer.", type: 'warning'
        });
    }
    else {
        var transferArr = {
            "woID": [woId],
            "senderID": signumGlobal,
            "receiverID": senderSignum,
            "logedInSignum": signumGlobal,
            "stepName": stepName,
            "userComments": userComments
        };
        $.isf.ajax({
            url: `${service_java_URL}woManagement/transferWorkOrder`,
            method: "POST",
            contentType: C_CONTENT_TYPE_APPLICATION_JSON,
            data: JSON.stringify(transferArr),
            dataType: "html",
            success: function (data) {
                var convertedData = JSON.parse(data);
                if (!convertedData.isValidationFailed) {
                    pwIsf.alert({ msg: convertedData.formMessages[0], type: 'success', autoClose: 1 })
                    const woIDDetails = JSON.parse($('#viewBtn_' + woId).attr('data-details'));
                //clear locale storage
                    if (localStorage.getItem('ASUSERINPUT_' + woId) != null || localStorage.getItem('ASUSERINPUT_' + woId) != undefined) {
                        localStorage.setItem('ASUSERINPUT_' + woId, null);
                        localStorage.removeItem('ASUSERINPUT_' + woId);
                    }

                    if (woIDDetails.workOrderAutoSenseEnabled == true) {
                        const autoSenseInputData = new Object();
                        autoSenseInputData.WoId = woId;
                        autoSenseInputData.flowchartDefID = woIDDetails.subActivityFlowChartDefID;
                        autoSenseInputData.stepID = "0";
                        autoSenseInputData.source = "UI";
                        autoSenseInputData.signumID = signumGlobal;
                        autoSenseInputData.overrideAction = "SUSPEND";
                        autoSenseInputData.action = "";
                        autoSenseInputData.taskID = "0";
                        autoSenseInputData.startRule = null;
                        autoSenseInputData.stopRule = null;
                        NotifyClientOnWorkOrderSuspended(autoSenseInputData, true);
                    }
                    location.reload();
                }
                else {
                    pwIsf.alert({ msg: convertedData.formErrors[0], type: 'error' });
                }
            },
            error: function (xhr, status, statusText) {
                var err = JSON.parse(xhr.responseText);
                pwIsf.alert({ msg: err.errorMessage, type: 'error' });
                $('#submitButtonMyWorkTab').attr('disabled', true);
            }
        });
    }
}

function getCycleTimeReson() {
    $('#reasonCycleTime').select2({
        placeholder: "Please select a Reason for Cyle Time Breach"
    });
    $('#reasonCycleTime').empty();
    var reasonType = "CycleTime";
    $.isf.ajax({
        type: "GET",
        url: `${service_java_URL}woExecution/getWOFailureReasons/${reasonType}`,
        success: function (data) {
            $('#reasonCycleTime').append('<option value=""></option>');
            if (data.isValidationFailed == false) {
                $.each(data.responseData, function (i, d) {
                    $('#reasonCycleTime').append(`<option value="${d.failureReason}">${d.failureReason}</option>`);
                });
            }
            else {
                pwIsf.alert({ msg: data.formErrors[0], type: 'error' });
            }
        },
        error: function (xhr, status, statusText) {
            console.log('An error occurred on get Reasons');
        }
    });
}

function getAllStepNameForTransferMyWork(woID) {
    $.isf.ajax({
        async: false,
        type: "GET",
        url: `${service_java_URL}woExecution/getAllStepsByWOID?woID=${woID}`,
        dataType: "json",
        crossdomain: true,
        success: function (data) {
            if (data.isValidationFailed == false) {
                var workFlowSteps = data.responseData;
                $("#select_stepNameMyWork").empty();
                $("#select_stepNameMyWork").append("<option value=-1>Select Step Name</option>");
                $('#select_stepNameMyWork').index = -1;
                $.each(workFlowSteps, function (i, d) {
                    $('#select_stepNameMyWork').append('<option value="' + d["stepName"] + '">' + d["stepName"] + '</option>');
                });
            }
        },
        complete: function () {
            pwIsf.removeLayer();
        }
    });

    $('#select_stepNameMyWork').select2({
        closeOnSelect: true
    });

    $("#select_stepNameMyWork").on('change', function () {
        validateTransferButton();
    });
}

function validateTransferButton() {
    const selectedStep = $('#select_stepNameMyWork').val();
    const selectedSignum = $('#selectUserAllSignumTransferInProgress').val();
    if (selectedStep !== -1 && selectedSignum !== "") {
        $('#submitButtonMyWorkTab').attr('disabled', false);
    }
    else {
        $('#submitButtonMyWorkTab').attr('disabled', true);
    }
}

function getAllSignumForTransferInProgress() {
    $("#selectUserAllSignumTransferInProgress").autocomplete({

        appendTo: "#modalTransferWO",
        source: function (request, response) {
            $.isf.ajax({
                url: `${service_java_URL}activityMaster/getEmployeesByFilter`,
                type: "POST",
                data: {
                    term: request.term
                },
                success: function (data) {
                    $("#selectUserAllSignumTransferInProgress").autocomplete().addClass("ui-autocomplete-loading");

                    var result = [];
                    if (data.length === 0) {
                        showErrorMsg('Signum-Required', 'Please enter a valid Signum.');
                        $('#selectUserAllSignumTransferInProgress').val("");
                        $('#selectUserAllSignumTransferInProgress').focus();
                        response(result);
                    }
                    else {
                        hideErrorMsg('Signum-Required');
                        $("#selectUserAllSignumTransferInProgress").autocomplete().addClass("ui-autocomplete-loading");
                        $.each(data, function (i, d) {
                            result.push({
                                "label": `${d.signum}/${d.employeeName}`,
                                "value": d.signum
                            });
                        });
                        response(result);
                        $("#selectUserAllSignumTransferInProgress").autocomplete().removeClass("ui-autocomplete-loading");
                    }
                }
            });
        },

        change: function (event, ui) {
            if (ui.item === null) {
                $(this).val('');
                $('#selectUserAllSignumTransferInProgress').val('');
            }
            validateTransferButton();
        },
        select: function (event, ui) {
            validateTransferButton();
        },
        minLength: 3
    });
    $("#selectUserAllSignumTransferInProgress").autocomplete("widget").addClass("fixedHeight");
}

function bindElementOfWorkorder(param) {

    //START - HEADER CALCULATION
    var gTotal = param.inprogressTotal + param.assignedTotal + param.onHoldTotal + param.defferedTotal + param.reopenedTotal;
    $('#woStatusGrandTotal').text(gTotal);
    $('#woGrandTotal').text(gTotal);
    $('#woInprogressTotal').text(param.inprogressTotal);
    $('#woAssignedTotal').text(param.assignedTotal);
    $('#woDefferedTotal').text(param.defferedTotal);
    $('#woReopenedTotal').text(param.reopenedTotal);
    $('#woOnHoldTotal').text(param.onHoldTotal);
    $('#woPriorityGrandTotal').text(gTotal);
    $('#woHighTotal').text(param.highTotal);
    $('#woLowTotal').text(param.lowTotal);
    $('#woNormalTotal').text(param.normalTotal);
    $('#woCriticalTotal').text(param.criticalTotal);

    //END - HEADER CALCULATION

    // START - BIND DROPDOWN
    $(".infoDropdown,.statusDropdown").on("show.bs.dropdown", function () {

        var $btnDropDown = $(this).find(".dropdown-toggle");
        var $listHolder = $(this).find(".dropdown-menu");
        //reset position
        $(this).css("position", "static");

        if ($(this).hasClass('infoDropdown')) {

            if (flagWOFullScreen) {
                $listHolder.css({
                    "top": ($btnDropDown.offset().top + $btnDropDown.outerHeight(true)) + "px",
                    "left": $btnDropDown.offset().left - 185 + "px"
                });
            } else {
                $listHolder.css({
                    "top": ($btnDropDown.offset().top + $btnDropDown.outerHeight(true) - 54) + "px",
                    "left": $btnDropDown.offset().left - 248 + "px"
                });
            }
        }
        if ($(this).hasClass('statusDropdown')) {

            if (flagWOFullScreen) {
                $listHolder.css({
                    "top": ($btnDropDown.offset().top + $btnDropDown.outerHeight(true)) + "px",
                    "left": $btnDropDown.offset().left - 25 + "px",
                    "width": "150px"
                });
            } else if (flagFlowChartFullScreen) {
                $listHolder.css({
                    "top": ($btnDropDown.offset().top + $btnDropDown.outerHeight(true)) + "px",
                    "left": $btnDropDown.offset().left - 25 + "px",
                    "width": "150px"
                });
            } else {
                $listHolder.css({
                    "top": ($btnDropDown.offset().top + $btnDropDown.outerHeight(true) - 54) + "px",
                    "left": $btnDropDown.offset().left - 65 + "px",
                    "width": "150px"
                });
            }
        }
        $listHolder.data("open", true);
    });
    //add BT DD hide event
    $(".dropdown").on("hidden.bs.dropdown", function () {
        var $listHolder = $(this).find(".dropdown-menu");
        $listHolder.data("open", false);
    });
    // END - BIND DROPDOWN

    //STATUS DROPDOWN 
    $('#woStatusDropdownUl li').on('click', function (e) {
        e.preventDefault();
        var that = this;
        resetDropdownLabel('.woStatusLbl', $(that).data('label'), $(that).data('searchtext'));
        $('#searchInWorkOrder').trigger('keyup');
    })

    //PRIORITY DROPDOWN 
    $('#woPriorityDropdown li').on('click', function (e) {
        e.preventDefault();
        var that = this;
        resetDropdownLabel('.woPriorityLbl', $(that).data('label'), $(that).data('searchtext'));
        $('#searchInWorkOrder').trigger('keyup');
    })

    //SORT BY
    $('#woSortByFilterDropdown li').off('click').on('click', function (e) {
        e.preventDefault();
        var that = this;
        resetDropdownLabel('.woSortByFilter', $(that).data('label'), $(that).data('sortbytext')); // set searchText by sortbytext
        createWorkorderHtml(globalWorkOrderData, bindElementOfWorkorder);
        $('#searchInWorkOrder').trigger('keyup');
    })


    //GET TOTAL
    $('#woGrandTotalBtn').on('click', function () {
        $('#expandAndCollapseWorkOrder').val('');
        resetDropdownLabel('.woStatusLbl', 'Status (All)', '');
        resetDropdownLabel('.woPriorityLbl', 'Priority (All)', '');
        $('#searchInWorkOrder').trigger('keyup');
    });

    //VIEW WORK ORDER
    $('.viewWorkFlowExec').on('click', function () {
        var woID = $(this).data('workorder-id');
        var status = $(this).data('status');
        var projid = $(this).data('prjid');
        var subActID = $(this).data('subactid');
        var versionNO = $(this).data('versionno');
        if (status === "ASSIGNED") {
            localStorage.setItem('expertise', 1);
        }
        else {
            localStorage.setItem('expertise', 0);
        }
        flowChartOpenInNewWindow(`FlowChartExecution?mode=execute&version=${versionNO}&woID=${woID}&subActID=${subActID}&prjID=${projid}`);
    });

    //CHANGE DELIVERY STATUS FUNCTION
    $("#delStatus").change(function () {
        var val = $(this).val();
        var woId = $('.modal-body #workOrderIDComp').val();
        document.getElementById("reasonDiv").style.display = "none";
        document.getElementById("subsequentDiv").style.display = "none";
        $('#reasonDiv').find('option').remove().end();
        $('#isCreateSubSequentWO').prop('checked', false);
        if (val === "Failure") {
            document.getElementById("reasonDiv").style.display = "block";
            document.getElementById("subsequentDiv").style.display = "none";
            $.isf.ajax({
                type: "GET",
                url: `${service_java_URL}woManagement/hasSubSequentPlanDetail?wOID=${woId}`,
                success: function (data) {
                    if (data == false) {
                        $("#subsequentDiv").hide();
                    }
                    else {
                        $("#subsequentDiv").show();
                    }
                },
                error: function (msg) {
                    console.log(msg);
                },
            });
            var reason = "";
            var reasonType = "DeliveryFailure";
            pwIsf.addLayer({ text: C_PLEASE_WAIT });
            $.isf.ajax({
                type: "GET",
                url: `${service_java_URL}woExecution/getWOFailureReasons/${reasonType}`,
                success: function (data) {
                    reason += "<option value=''>--Select one--</option>";
                    if (!data.isValidationFailed) {
                        for (var i in data.responseData) {
                            reason = `${reason}<option value='${data.responseData[i].failureReason}'>${data.responseData[i].failureReason}</option>`;
                        }
                    }
                    else {
                        pwIsf.alert({ msg: data.formErrors[0], type: 'error' });
                    }
                    $('#delReason').append(reason);
                },
                complete: function (xhr, statusText) {
                    pwIsf.removeLayer();
                },
                error: function (xhr, status, statusText) {
                    var err = JSON.parse(xhr.responseText);
                    alert(err.errorMessage);
                }
            });
            $("#delReason").append(reason);
            testForSuccessReasonCheck();
        }
        if (val === "Success") {
            document.getElementById("reasonDiv").style.display = "block";
            document.getElementById("subsequentDiv").style.display = "none";
            var successReason = "";
            var reasonType = "DeliverySuccess";
            pwIsf.addLayer({ text: C_PLEASE_WAIT });
            $.isf.ajax({
                type: "GET",
                url: `${service_java_URL}woExecution/getWorkOrderSuccessReasons?wOID=${woId}`,
                success: function (data) {
                    successReason += "<option value=''>--Select one--</option>";
                    if (data.isValidationFailed == false) {
                        for (var i in data.responseData) {
                            successReason = `${successReason}<option value='${data.responseData[i].encodedSuccessReason}'>${data.responseData[i].encodedSuccessReason}</option>`;
                        }
                    }
                        $('#delReason').append(successReason);
                        $('#delReason').val("Not Applicable");
                        $("#commentCompleted").show();
                },
                complete: function (xhr, statusText) {
                    pwIsf.removeLayer();
                },
                error: function (xhr, status, statusText) {
                    var err = JSON.parse(xhr.responseText);
                    alert(err.errorMessage);
                }
            });
            $("#delReason").append(successReason);
            testForSuccessReasonCheck();
        }
    });

    //VIEW BOOKING DETAILS
    $(document).on("click", ".view-booking-details", function () {
        $('#viewBookingDetailsTable').find('div').remove().end();
        $('#viewBookingDetailsTable').find('table').remove().end();
        $('#viewBookingDetailsHeader').find('p').remove().end();

        var workId = $(this).data('workorder-id');
        var viewTaskHeader = "";
        var viewTaskDetailTable = "";
        var progressWorkOrderDetails = '';
        var totalEffortHeader = "";

        $.isf.ajax({
            url: `${service_java_URL}woManagement/getBookingDetails/${workId}/${signumGlobal}`,
            success: function (data) {
                progressWorkOrderDetails = data;
                $('#viewBookingDetailsHeader').html('');
                $('#viewBookingTotalEffort').html('');
                $('#viewBookingDetailsTable').html('');
                if (progressWorkOrderDetails.length !== 0) {
                    viewTaskHeader = '<span style="font-weight:bold">WORK ORDER ID :  </span>' + workId;
                    viewTaskDetailTable =
                        '<table class="table table-striped">' +
                        '<thead><tr><th>Start Date</th><th>End Date</th><th>Hours</th><th>Type</th><th>Status</th><th>Signum</th></tr></thead><tbody>';
                    var uniqueStepId = [];
                    var totalEffort = 0;
                    for (var i in progressWorkOrderDetails) {
                        var stepId = progressWorkOrderDetails[i].FlowChartStepID;
                        var stepName = progressWorkOrderDetails[i].StepName;
                        var execType = progressWorkOrderDetails[i].ExecutionType;
                        var startDate = progressWorkOrderDetails[i].StartDate;
                        var edDate = progressWorkOrderDetails[i].EndDate;
                        var hours = progressWorkOrderDetails[i].hours;
                        var signumID = progressWorkOrderDetails[i].signumID;
                        var status = progressWorkOrderDetails[i].status;
                        var type = progressWorkOrderDetails[i].type;

                        if (type === "BOOKING" && hours !== "-") {
                            totalEffort = totalEffort + hours;
                        }
                        if (execType !== C_EXECUTION_TYPE_MANUALDISABLED) {
                            if (!hours)
                            {
                                hours = replaceNullBy;
                            }
                            if (!uniqueStepId.includes(stepId)) {
                                stepWiseTotalEffort = CalculateStepWiseEffort(stepId, progressWorkOrderDetails);
                                viewTaskDetailTable += '<tr>';
                                viewTaskDetailTable += '<td colspan="6" style="text-align: center;"><b>STEP NAME:</b> ' + stepName + ' | ' + stepId + ' | ' + execType + ' | Total Effort - ' + stepWiseTotalEffort + ' </td>'
                                viewTaskDetailTable += '</tr>';
                                uniqueStepId.push(stepId);
                            }
                            var stFormatDate = null;
                            var endFormatDate = null;

                            if (startDate) { var stDate = new Date(startDate); stFormatDate = stDate.getFullYear() + '-' + checkTime(stDate.getMonth() + 1) + '-' + checkTime(stDate.getDate()) + ' ' + checkTime(stDate.getHours()) + ':' + checkTime(stDate.getMinutes()); } else { stFormatDate = replaceNullBy }
                            if (edDate) { var endDate = new Date(edDate); endFormatDate = endDate.getFullYear() + '-' + checkTime(endDate.getMonth() + 1) + '-' + checkTime(endDate.getDate()) + ' ' + checkTime(endDate.getHours()) + ':' + checkTime(endDate.getMinutes()); } else { endFormatDate = replaceNullBy }
                            viewTaskDetailTable += '<tr><td>' + stFormatDate + '</td>' +
                                '<td>' + endFormatDate + '</td>' +
                                '<td>' + hours + '</td>' +
                                '<td>' + type + '</td>' +
                                '<td>' + status + '</td>' +
                                '<td>' + signumID + '</td></tr>';
                        }

                    }//for close

                    viewTaskDetailTable += '</tbody></table>';
                    totalEffortHeader = '<span style="font-weight:bold">Work Order Total Effort :  </span>' + Math.round(totalEffort * 100) / 100;
                    $('#viewBookingDetailsHeader').append('<p>' + viewTaskHeader + '</p>');
                    $('#viewBookingTotalEffort').append('<p>' + totalEffortHeader + '</p>');
                    $('#viewBookingDetailsTable').append(viewTaskDetailTable);
                }
                $('#viewBookingDetails').modal('show');
            },
            error: function (xhr, status, statusText) {
                pwIsf.removeLayer();
                $("#viewBookingDetails").modal('hide');
                alert("Failed to Fetch Data");

            }
        });
    });

    //Calculate Step Wise Total Effort
    function CalculateStepWiseEffort(flowCharetStepID, progressWorkOrderDetails) {
        var stepEffort = 0.0;
        $.each(progressWorkOrderDetails, function (i, d) {
            if (d.FlowChartStepID != null && d.FlowChartStepID === flowCharetStepID && d.type === "BOOKING" && d.hours != null) {
                stepEffort = stepEffort + d.hours;
            }
        });
        return Math.round(stepEffort * 100) / 100;
    }

    function getDODetails(doid, woid) {
        $.isf.ajax({
            url: `${service_java_URL}woManagement/getWorkOrdersByDoid?doid=${doid}`,
            success: function (data) {
                if (data.length > 0) {
                    oTableDODetail = $('#table_do_details').DataTable({
                        "data": data,
                        "destroy": true,
                        "searching": true,
                        order: [1],
                        "columns": [
                            {
                                "title": "DOID",
                                "data": null,
                                "render": function (data, type, row) {
                                    if (data.woid == woid) {
                                        return '<i class="fa fa-chevron-circle-right" style="color:green"></i>' + data.doid;
                                    } else {
                                        return data.doid;
                                    }
                                }
                            },
                            {
                                "title": "WOID",
                                "data": "woid"
                            },
                            {
                                "title": shortNeNameAsNEID,
                                "data": null,
                                "render": function (data, type, row, meta) {
                                    woViewDataArray = [];
                                    const x = data.listOfNode;
                                    let nodes = '';
                                    for (var i = 0; i < x.length; i++) {
                                        nodes = x[i].nodeNames + ',' + nodes;
                                        nodes = nodes.replace(/,\s*$/, '');
                                    }
                                    if (nodes == null || nodes == "null")
                                        nodes = "";
                                    localStorage.setItem(data.woplanid + "_" + data.woid, JSON.stringify(data.listOfNode));
                                    woViewDataArray.push(
                                        {
                                            "NodeNames": nodes
                                        }
                                    );

                                    var nodesView = `<i data-toggle="tooltip" id="showList${data.woid}"  title="Network Element Details" class="fa fa-list" style="cursor:pointer"`;
                                    nodesView = `${nodesView}onclick="modalNodeDetail(${data.woplanid},${data.woid})"></i>`;

                                    return nodesView;
                                }
                            },
                            {
                                "title": "WFID_WFName_WFVErsion",
                                "data": null,
                                "render": function (data, type, row) {
                                    return `${data.wfid}_${data.wfName}_${data.wfVersion}`;
                                },
                            },
                            {
                                "title": "Status",
                                "data": "status"
                            },
                            {
                                "title": "Signum",
                                "data": null,
                                "render": function (data, type, row) {
                                    if (data.signum == null) {
                                        return '';
                                    }
                                    else {
                                        return data.signum;
                                    }
                                }

                            }
                        ],
                        initComplete: function () {
                            var api = this.api();
                            api.columns().every(function () {
                                var that = this;
                                $('input').on('keyup change', function () {
                                    if (that.search() == this.value) {
                                        that
                                            .columns($(this).parent().index() + ':visible')
                                            .search(this.value)
                                            .draw();
                                    }
                                });
                            });
                        }
                    });
                } else {
                    $('#table_do_details').html('<tr><td colspan="11" style="text-align:center">No data found.</td></tr>');
                }
            },
            error: function (xhr, status, statusText) {
                console.log('An error occurred on getDomain: ' + xhr.error);
            }
        });
    }

    function modalNodeWOPopup(wOPlanID, wOID, doid) {
        if ($.fn.dataTable.isDataTable('#table_wo_nodes_detail_edit')) {
            table.destroy();
        }
        var nodes;
        $.isf.ajax({
            url: `${service_java_URL}woManagement/getWorkOrdersByDoid?doid=${doid}`,
            success: function (data) {

                $.each(data, function (i, d) {
                    if (d.woid === wOID) {
                        nodes = JSON.stringify(d.listOfNode);
                    }
                });

                var listOfNode = JSON.parse(nodes);
                $.each(listOfNode, function (i, d) {
                    if (d.nodeNames === "" && d.nodeType === "" && d.market === "") {
                        listOfNode = [];
                    }
                });

                if (listOfNode.length > 0) {
                    const isExist = checkNodeExist(listOfNode);
                    if (isExist === true) {
                        table = $('#table_wo_nodes_detail_edit').DataTable({
                            "data": listOfNode,
                            "destroy": true,
                            searching: true,
                            order: [1],
                            "columns": [

                                {
                                    "title": neType,
                                    "data": "nodeType"
                                },
                                {
                                    "title": neName,
                                    "data": "nodeNames"
                                },
                                {
                                    "title": neMarket,
                                    "data": "market"
                                }
                            ]
                        });
                    } else {
                        $('#table_wo_nodes_detail_edit').html('<tr><td colspan="11" style="text-align:center">No data found.</td></tr>');
                    }
                }
                else {
                    $('#table_wo_nodes_detail_edit').html('<tr><td colspan="11" style="text-align:center">No data found.</td></tr>');
                }
            }
        });
    }

    //MARK AS ONHOLD
    $(document).off("click", ".view-hold").on("click", ".view-hold", function () {
        var woId = $(this).data('id');
        var priority = $(this).data('priority');
        $.isf.ajax({
            type: "GET",
            url: `${service_java_URL}woExecution/getAlreadyStartedBookingsByWoID/${woId}`,
            success: checkIfWOINProgress,
            error: function (msg) {},
            complete: function (msg) {}
        });

        function checkIfWOINProgress(data) {
            if (data.isValidationFailed) {
                responseHandler(data);
            }
            else {
                $(".modal-body #workOrderIDHold").val(woId);
                $(".modal-body #commentHold").val("");
                $(".modal-body #priorityHold").val(priority);
                $('#modalHold').modal('show');
            }
        }
    });

    //MARK AS DEFFERED
    $(document).off("click", ".view-deffered").on("click", ".view-deffered", function () {
        var woId = $(this).data('woid');
        $.isf.ajax({
            type: "GET",
            url: `${service_java_URL}woExecution/getAlreadyStartedBookingsByWoID/${woId}`,
            success: checkIfWOINProgress,
            error: function (msg) {},
            complete: function (msg) {}
        });

        function checkIfWOINProgress(data) {
            if (data.isValidationFailed) {
                responseHandler(data);
            }
            else {
                $(".modal-body #workOrderIDDef").val(woId);
                $(".modal-body #commentDeffered").val("");
                $('#modalDeffered').modal('show');
                $.isf.ajax({
                    type: "GET",
                    url: `${service_java_URL}woManagement/hasSubSequentPlanDetail?wOID=${woId}`,
                    success: function (data) {
                        if (!data) {
                            $("#subsequentDivDef").hide();
                        }
                        else {
                            $("#subsequentDivDef").show();
                        }
                    },
                    error: function (msg) {},
                    complete: function (msg) {}
                });
            }
        }
    });

    //TRANSFER WO
    $(document).on("click", ".view-transferWO", function () {
        var woId = $(this).data('woid');
        $(".modal-body #woId").val(woId);
        $(".modal-body #cbxSignumAR").val("");
        getAllSignumForTransferInProgress();
        getAllStepNameForTransferMyWork(woId);
        $('#selectUserAllSignumTransferInProgress').val("");
        $('#tWOcommentsBoxMyWork').val("");
        $('#submitButtonMyWorkTab').attr('disabled', true);
        $('#modalTransferWO').modal('show');
    });


    //DUPLICATE WO
    $(document).on("click", ".view-duplicate", function () {
        var woId = $(this).data('id');
        var projectId = $(this).data('projectid');
        $(".modal-body #workOrderIDDup").val(woId);
        $(".modal-body #ProjectID").val(projectId);
        $(".modal-body #commentDuplicate").val("");
        $('#modalDuplicate').modal('show');
    });

    //WORK ORDER VIEW
    $('.ewoWorkOrder').on('click', function (e, data) {
        console.log('dffffffffview');
        var workOrderId = $(this).data('workorder-id');
        var status = $(this).data('status');
        var doid = $(this).data('doid');
        var woplanid = $(this).data('woplan-id');
        $('#workOrderViewIdPrefix_doid').val(doid);
        getDODetails(doid, workOrderId);
        $('#WODetailPanel').addClass('collapse');
        $('#activityPanel').addClass('collapse');
        $('#WODetailPanel').removeClass('in');
        $('#activityPanel').removeClass('in');
        $('#nodePanel').addClass('collapse');
        $('#nodePanel').removeClass('in');
        $('#nodeEditPanel').hide();
        modalNodeWOPopup(woplanid, workOrderId, doid);
        $('#btn_update_project').hide();
        $('.projectDetailsIcon').hide();
        $('#wODetailHeader').find('p').remove().end();
        $('#wODetailHeader').append('<p> WO Details - ' + workOrderId + '</p>');
        $('#DODetailHeader').find('p').remove().end();
        $('#DODetailHeader').append('<p> DO Details - ' + doid + '</p>');
        var editFlag = true;
        var getEditFlag = $(this).data('edit-work-order-flag');
        if (typeof $(this).data('edit-work-order-flag') !== 'undefined' || $(this).data('edit-work-order-flag') == false) {
            editFlag = false;
        }
        pwIsf.addLayer({ text: C_PLEASE_WAIT });

        $.isf.ajax({
            url: `${service_java_URL}woExecution/getCompleteWoDetails/${workOrderId}`,
            success: function (data) {
                pwIsf.removeLayer();
                $('#all_nodes_master').hide();
                if (!data.isValidationFailed) {
                    if (data.responseData[0].externalGroup === "ERISITE") {
                        $("#woNameDiv").css("display", "none");
                        $("#woNameDivErisite").css("display", "block");
                        $("#workOrderViewIdPrefix_full_wo_name").attr("title", data.responseData[0].wOName);
                    }
                    else {
                        $("#woNameDiv").css("display", "block");
                        $("#woNameDivErisite").css("display", "none");
                    }
                    var formElements = getElementNamesObj();
                    var valuesFromApiResponse = getValuesFromApiResponse(data);
                    // Finaly - Set values to elements
                    setValuesToElements(formElements, valuesFromApiResponse);

                    //Hide Save button on view order form
                    if ((status === "CLOSED") || (status === "DEFERRED") || (status === "REJECTED")) {
                        $('#btn_update_nodes').attr("disabled", true);
                        $("#edit_node_details").hide();

                    } else {
                        $("#edit_node_details").show();
                    }

                    setValuesToNEPartialView(data.responseData, 'edit');
                    getDefaultValuesForNEAndCount('edit', data.responseData);
                    projIdWarning.val(data.responseData[0].projectID);
                    $('#domainWarning').val(data.responseData[0].domainID);
                    techIDList.val(data.responseData[0].technologyID);                    
                }
                else {
                    pwIsf.alert({ msg: data.formErrors[0], type: 'error' });
                }
                $("#myModalMadEwo").modal('show');

            },
            error: function (xhr, status, statusText) {
                pwIsf.removeLayer();
                $("#myModalMadEwo").modal('hide');
                alert("Failed to Fetch Data");
            }
        });
    });


    //View WorkOrder from Menu in WorkOrder Box
    $('.viewWorkOrder').on('click', function (e, data) {

        var workOrderId = $(this).data('workorder-id');
        var status = $(this).data('status');
        var doid = $(this).data('doid');
        var woplanid = $(this).data('woplan-id');
        $('#workOrderViewIdPrefix_doid').val(doid);
        getDODetails(doid, workOrderId);
        $('#WODetailPanel').addClass('collapse');
        $('#activityPanel').addClass('collapse');
        $('#WODetailPanel').removeClass('in');
        $('#activityPanel').removeClass('in');
        $('#nodePanel').addClass('collapse');
        $('#nodePanel').removeClass('in');
        $('#nodeEditPanel').hide();
        modalNodeWOPopup(woplanid, workOrderId, doid);
        $('#btn_update_project').hide();
        $('.projectDetailsIcon').hide();
        $('#wODetailHeader').find('p').remove().end();
        $('#wODetailHeader').append('<p> WO Details - ' + workOrderId + '</p>');
        $('#DODetailHeader').find('p').remove().end();
        $('#DODetailHeader').append('<p> DO Details - ' + doid + '</p>');
        var editFlag = true;
        var getEditFlag = $(this).data('edit-work-order-flag');
        if (typeof $(this).data('edit-work-order-flag') !== 'undefined' || $(this).data('edit-work-order-flag') == false) {
            editFlag = false;
        }
        pwIsf.addLayer({ text: C_PLEASE_WAIT });

        $.isf.ajax({
            url: `${service_java_URL}woExecution/getCompleteWoDetails/${workOrderId}`,
            success: function (data) {
                pwIsf.removeLayer();
                $('#all_nodes_master').hide();

                if (data.isValidationFailed == false) {
                    if (data.responseData[0].externalGroup === "ERISITE") {
                        $("#woNameDiv").css("display", "none");
                        $("#woNameDivErisite").css("display", "block");
                        $("#workOrderViewIdPrefix_full_wo_name").attr("title", data.responseData[0].wOName);
                    }
                    else {
                        $("#woNameDiv").css("display", "block");
                        $("#woNameDivErisite").css("display", "none");
                    }
                    var formElements = getElementNamesObj();
                    var valuesFromApiResponse = getValuesFromApiResponse(data);
                    // Finaly - Set values to elements
                    setValuesToElements(formElements, valuesFromApiResponse);

                    //Hide Save button on view order form


                    if ((status === "CLOSED") || (status === "DEFERRED") || (status === "REJECTED")) {
                        $('#btn_update_nodes').attr("disabled", true);
                        $("#edit_node_details").hide();

                    } else {
                        $("#edit_node_details").show();
                    }

                    setValuesToNEPartialView(data.responseData, 'edit');
                    getDefaultValuesForNEAndCount('edit', data.responseData);
                    projIdWarning.val(data.responseData[0].projectID);
                    $('#domainWarning').val(data.responseData[0].domainID);
                    techIDList.val(data.responseData[0].technologyID);
                }
                else {
                    pwIsf.alert({ msg: data.formErrors[0], type: 'error' });
                }
                let selectedTabHref = $('#projectQueueTab').hasClass('active');
                if (selectedTabHref) {
                    $('.nodeDetailsIcon').hide();
                }
                $("#myModalMadEwo").modal('show');

            },
            error: function (xhr, status, statusText) {
                pwIsf.removeLayer();
                $("#myModalMadEwo").modal('hide');
                alert("Failed to Fetch Data");
            }
        });
    });

    $('#fullScreenForWO').off('click').on('click', function () {

        if (makeItFullScreen(this, $('.work_order_panel'))) {
            flagWOFullScreen = true;
        } else {
            flagWOFullScreen = false;
        }

    });

    $('#expandAndCollapseWorkOrder').off('click').on('click', function () {
        var obj = {};
        obj.that = this;
        obj.targetSelector = $('.work_order_dBox_container > .work_order_dBox');
        obj.conditionTarget = 'work_order_dBox_container';
        obj.maxHeight = myCurrentWOpanelMaxHeight;
        obj.middHeight = myCurrentWOpanelMiddHeight;
        obj.minHeight = '0px';
        expandAndCollapse(obj);
    });

    $('#searchInWorkOrder').keyup(function () {

        var searchObj = {}; callBacksObj = {};
        var that = this;

        var makeText = $(that).val().toLowerCase() + ',' + $('.woStatusLbl').attr('data-searchtext') + ',' + $('.woPriorityLbl').attr('data-searchtext');
        searchObj.textToBeSearch = makeText;
        searchObj.searchInTarget = '.work_order_dBox_container > .panel';
        callBacksObj.callBack = false;
        callBacksObj.mainContainer = '.work_order_dBox_container';
        callBacksObj.callBackFunction = '';

        searchInDiv(searchObj, callBacksObj);
    });

    $('.on-wo-viewFlowchart').on('click', function (e) {
        e.preventDefault();
        defaultResetViewFlowChart();
        var passObj = {};
        let that = this;
        let isOnHoldWOExecuted;
        let proficiency;
        passObj = $(that).data('details');
        //Check if user is opening WF for the first time
        if (passObj.proficiencyLevel == null || passObj.proficiencyId == null) {
            proficiency = getWFUserProficiency(signumGlobal, passObj.subActId, passObj.wfid, passObj.projectID, passObj.subActivityFlowChartDefID);
            let experiencedMode = (parseInt(proficiency.proficiencyLevel) > 1) ? 1 : 0;
            passObj.proficiencyLevel = proficiency.proficiencyLevel;
            passObj.proficiencyId = proficiency.proficiencyId;
            passObj.experiencedMode = experiencedMode;
            $(that).data('details', passObj); // Update it so that it doesn't call the api again if user opens the wf again without refreshing the page
        }

        let wfownername = passObj.wfowner;
        $("#openWF").off("click");
        $("#openWF").on("click", function () { openWorkFlow(afterLoadingFlowchart, that); })

        //GET WO PROGRESS FROM DATA PROPERTIES OF THIS PROGRESS BAR
        let getElement = $(`#myWorkOrderProgressBarArea_${passObj.woid}`);
        let getPercentage = $(getElement).find('.myWorkOrderPanel .progress-bar').attr('data-percentage');
        let getPercentageText = $(getElement).find('.myWorkOrderPanel .progress-bar').attr('data-percentageText');
        $("#projectIdWarning").val(passObj.projectID);
        //NOW SET PERCENTAGE IN passObj
        passObj.woPercentage = getPercentage;
        passObj.woPercentageText = getPercentageText;
        //GET NODES FROM DATA PROPERTIES
        let getNodeElement = $(`#nodeFieldCurrentWO_${passObj.woid}`);
        let getNodeValue = $(`#nodeFieldCurrentWO_${passObj.woid}`).text();
        //NOW SET Nodes IN passObj
        passObj.nodeNames = getNodeValue;

        localStorage.setItem('clickedWOID', passObj.woid);

        if (passObj.status.toUpperCase() === 'ONHOLD') {
            isOnHoldWOExecuted = checkIfExecuted(passObj.woid);
        }
        if ((passObj.status.toUpperCase() == 'ASSIGNED' || passObj.status.toUpperCase() == 'REOPENED' || isOnHoldWOExecuted == false) && (passObj.nodeNames == '-' || passObj.nodeNames == "")) { // if NEID/nodeNames is not defined and WO status is ASSIGNED or ONHOLD
            $("#woidWarning").val(passObj.woid);
            let btnJson = {
                'Update NEID': {
                    'action': function () {

                        $(that).siblings('.viewWorkOrder').trigger('click', [{ woid: true }]);
                    },
                    'class': 'btn btn-success'
                },
                'Close': {
                    'action': function () {},
                    'class': 'btn btn-danger'
                },
            };

            let btnJson2 = {
                'Update NEID': {
                    'action': function () {

                        var reason = $("#sourceId3").val();
                        var comment = $("#commentsBox").val();
                        if (reason == "0") {
                            pwIsf.alert({ msg: 'Please select a reason', type: 'warning' });
                            return false;
                        }
                        else if (reason == "Others" && !comment) {
                            pwIsf.alert({ msg: 'Comments cannot be left blank!', type: 'warning' });
                            return false;
                        }
                        else {
                            isLeadTimeBreached[passObj.woid].comment = comment;
                            isLeadTimeBreached[passObj.woid].reason = reason;
                            $(that).siblings('.viewWorkOrder').trigger('click', [{ woid: true }]);
                        }
                    },
                    'class': 'btn btn-success'
                },
                'Close': {
                    'action': function () {},
                    'class': 'btn btn-danger'
                },

            };

            let btnContinueWithoutUpdate = {
                'Continue Without Update': {
                    'action': function () {
                        createFlowchartPanel(passObj, afterLoadingFlowchart);
                        $(that).attr('disabled', true);
                    },
                    'class': 'btn btn-warning'
                }
            };

            let btnContinueWithoutUpdate2 = {
                'Continue Without Update': {
                    'action': function () {
                        var reason = $("#sourceId3").val();
                        var comment = $("#commentsBox").val();
                        if (reason == "0") {
                            pwIsf.alert({ msg: 'Please select a reason', type: 'warning' });
                            return false;
                        }
                        else if (reason == "Others" && !comment) {
                            pwIsf.alert({ msg: 'Comments cannot be left blank!', type: 'warning' });
                            return false;
                        }
                        else {
                            leadTimeReasonCommentPost(passObj.woid, passObj.difference);
                            createFlowchartPanel(passObj, afterLoadingFlowchart);
                            $(that).attr('disabled', true);
                        }
                    },
                    'class': 'btn btn-warning'
                }
            };
            if (passObj.neRequired) {
                neMandateOrOptional = true;
                $('#flowChartWarning').modal('show');
                $("#slideUp").hide();
                $("#slideDwn").hide();
                $("#node_box_DE").show();
                getWODetailsWarning();

                if (passObj.difference < 0) {
                    $("#flowChartWarningTitle").text(`Lead Time Breached (WOID:${passObj.woid}")`);
                    $("#commentReason").show();
                    $("#warningMsg").text("Network element is mandatory for this work order(WO), please update it.");
                }
                else {
                    $("#flowChartWarningTitle").text(`Network Elements not defined (WOID:${passObj.woid})`);
                    $("#commentReason").hide();
                    $("#warningMsg").text("Network element is mandatory for this work order(WO), please update it.");
                }
            }


            else if (!passObj.neRequired) {
                neMandateOrOptional = false;
                $('#flowChartWarning').modal('show');
                $('#node_box_DE').hide();
                if (passObj.difference < 0) {
                    $("#flowChartWarningTitle").text(`Lead Time Breached (WOID:${passObj.woid})`);
                    $("#commentReason").show();
                    $("#warningMsg").text("Network elements are not defined with this work order(WO), please update if required.");
                    btnJson2 = { ...btnContinueWithoutUpdate2, ...btnJson2 };
                }
                else {
                    $("#flowChartWarningTitle").text(`Network Elements not defined (WOID:${ passObj.woid })`);
                    $("#commentReason").hide();
                    $("#warningMsg").text("Network elements are not defined with this work order(WO), please update if required.");
                    btnJson = { ...btnContinueWithoutUpdate, ...btnJson };
                }
            }

            $('#sourceId3').select2();
            $('#sourceId3').on('select2:select', function (e) {});
            selectReason();

        }
        else if (passObj.nodeNames && passObj.difference < 0 && (passObj.status.toUpperCase() == 'ASSIGNED' || passObj.status.toUpperCase() == 'REOPENED' || isOnHoldWOExecuted == false)) {
            $("#woidWarning").val(passObj.woid);
            $('#flowChartWarning').modal('show');
            $('#node_box_DE').hide();
            $("#commentReason").show();
            $("#slideUp").show();
            $("#slideDwn").hide();
            $("#warningMsg").text("");
            let OK = {
                'OK': {
                    'action': function () {
                        var reason = $("#sourceId3").val();
                        var comment = $("#commentsBox").val();
                        if (reason == "0") {
                            pwIsf.alert({ msg: 'Please select a reason', type: 'warning' });
                        }
                        else if (reason == "Others" && !comment) {
                            pwIsf.alert({ msg: 'Comments cannot be left blank!', type: 'warning' });
                            return false;
                        }
                        else {
                            leadTimeReasonCommentPost(passObj.woid, passObj.difference);
                            $(that).attr('disabled', true);
                            createFlowchartPanel(passObj, afterLoadingFlowchart);
                        }
                    },
                    'class': 'btn btn-success'

                },
                'Close': {
                    'action': function () {},
                    'class': 'btn btn-danger'
                },
            };
            $("#flowChartWarningTitle").text(`Lead Time Breached (WOID: ${passObj.woid})`);
            selectReason();
            $('#sourceId3').select2();
            $('#sourceId3').on('select2:select', function (e) {});
        }
        else {
            $(that).attr('disabled', true);
            createFlowchartPanel(passObj, afterLoadingFlowchart);
        }


    });


    //REFRESH CURRENT WORK ORDER PANEL
    $('#refreshCurrentWorkOrderTask').off('click').on('click', function () {
        global_marketsOfWO = [];
        globalWorkOrderData = [];
        requestWorkorder();
    });


    //START- Expand or collapse panel based on previous state of icon 
    let targetSelector = $('.work_order_dBox_container > .work_order_dBox');
    if ($('#expandAndCollapseWorkOrder').find('i').hasClass('fa-angle-down')) {
        targetSelector.css({ 'height': myCurrentWOpanelMiddHeight });
    } else {
        targetSelector.css({ 'height': myCurrentWOpanelMaxHeight });
    }
    //END- Expand or collapse panel based on previous state of icon 

    disableViewFlowChartAction(); // Disabled view action button if flow chart opened 

    if (adhocBookingStatus) {}
}

function modalNodeDetail(wOPlanID, wOID) {
    var listOfNode = JSON.parse(localStorage.getItem(`${wOPlanID}_${wOID}`));
    $.each(listOfNode, function (i, d) {
        if (d.nodeNames === "" && d.nodeType === "" && d.market === "") {
            listOfNode = [];
        }
    });

    $('#table_wo_nodes_detail').DataTable({
        info:true,
        "data": listOfNode,
        "destroy": true,
        searching: true,
        order: [1],
        "columns": [
            {
                "title": neType,
                "data": "nodeType"
            },
            {
                "title": neName,
                "data": "nodeNames"
            },
            {
                "title": neMarket,
                "data": "market"
            }
        ]
    });

    $('#modal_wo_nodes_detail').modal('show');
}

function fillMarketOfWO(data) {
    if (data.length != 0) {
        for (i in data) {
            if ($('#marketCurrentWO_' + data[i].wOID).length != 0) {
                if (data[i].markets.length) {

                    $('#marketCurrentWO_' + data[i].wOID).text(data[i].markets);
                }
                else {
                    $('#marketCurrentWO_' + data[i].wOID).text('-');
                }
            }

        }
    }
}

function getMarketOfWorkOrder(marketDetails) {
    var serviceUrl = `${service_java_URL}woExecution/getMarketsByWorkOrders`;
    $.isf.ajax({
        type: 'POST',
        url: serviceUrl,
        async: false,
        contentType: C_CONTENT_TYPE_APPLICATION_JSON,
        dataType: 'json',
        data: JSON.stringify(marketDetails),
        success: function (data) {
            let d = '';
            if (!data.isValidationFailed) {
                d = data.responseData;
                global_marketsOfWO = d;
                fillMarketOfWO(d);
            }
            else {
                pwIsf.alert({ msg: data.formErrors[0], type: 'error' });
            }
        },
        error: function () {},
    });
}

function defaultResetViewFlowChart() {
    $("#commentReason").hide();
    $("#slideUp").show();
    $("#slideDwn").hide();
    $("#node_box").hide();
    $('#validStatusWarning').css('display', 'none');
    $("#sourceId3").val("0");
    $("#commentsBox").val("");
    $('#woc_select_nodeName').find('option').remove();
    $("#woc_select_nodeName").select2("val", "");
    $('#select_nodeType').find('option').remove();
    $("#select_nodeType").select2("val", "");
    $('#select_nodeType2').find('option').remove();
    $("#select_nodeType2").select2("val", "");
    $('#select_markArea').find('option').remove();
    $('#select_markArea').select2("val", "");
    $('#select_vendor').find('option').remove();
    $('#select_vendor').select2("val", "");
    $("#nodeView").val("");
    $('#validStatus').hide();
}

function getWODetailsWarning() {
    var workOrderId = $("#woidWarning").val();
    pwIsf.addLayer({ text: C_PLEASE_WAIT });
    $.isf.ajax({
        url: `${service_java_URL}woExecution/getCompleteWoDetails/${workOrderId}`,
        success: function (data) {
            pwIsf.removeLayer();
            if (data.isValidationFailed == false) {
                $("#domainWarning").val(data.responseData[0].domain.split('-')[0]);
                $("#subDomainWarning").val(data.responseData[0].domain.split('-')[1]);
                $("#technologyWarning").val(data.responseData[0].technology);
                techIDList.val(data.responseData[0].technologyID);
                projIdWarning.val(data.responseData[0].projectID);
                domainIdList.val(data.responseData[0].domainID);
                let prePopNodeName = '';
                for (var i = 0; i < data.responseData[0].listOfNode.length; i++) {
                    prePopNodeName = prePopNodeName + ',' + data.responseData[0].listOfNode[i].nodeNames;
                }

                prePopNodeName = prePopNodeName.replace(/(^,)|(,$)/g, "");
                if (prePopNodeName == null || prePopNodeName == "null")
                    prePopNodeName = "";

                var sortedUniqueArr = jQuery.unique(prePopNodeName.split(",").sort(function (a, b) {
                    if (a.toUpperCase() < b.toUpperCase())
                        return -1;
                    if (a.toUpperCase() > b.toUpperCase())
                        return 1;

                    return 0;
                }))

                setValuesToNEPartialView(data.responseData, 'neMandatory');
                getDefaultValuesForNEAndCount('neMandatory', data.responseData);
            }
            else {
                pwIsf.alert({ msg: data.formErrors[0], type: 'error' });
            }
            let nodeUniqueString = sortedUniqueArr.join(",");
            $("#nodeView").val(nodeUniqueString);
        },
        error: function (xhr, status, statusText) {
            pwIsf.removeLayer();
            alert("Failed to Fetch Data");

        },
        complete: function () {
            setDefaultValuesForNEAndCount('neMandatory');
        }
    });
}

function selectReason() {
    var failureCategory = "LeadTime";
    $('#sourceId3').empty();
    $.isf.ajax({
        url: `${service_java_URL}woExecution/getWOFailureReasons/${failureCategory}`,
        success: function (data) {
            $('#sourceId3').append('<option value="0"></option>');
            if (data.isValidationFailed == false) {
                $.each(data.responseData, function (i, d) {
                    $('#sourceId3').append(`<option value="${d.failureReason}">${d.failureReason}</option>`);
                });
            }
            else {
                pwIsf.alert({ msg: data.formErrors[0], type: 'error' });
            }
        },
        error: function (xhr, status, statusText) {
            console.log('An error occurred on getSourcesForMapping');
        }
    });
}

function formatDate(date) {
    var d = new Date(date),
        month = '' + (d.getMonth() + 1),
        day = '' + d.getDate(),
        year = d.getFullYear(),
        hour = d.getHours(),
        min = d.getMinutes(),
        sec = d.getSeconds(),
        msec = d.getMilliseconds();

    if (month.length < 2) month = '0' + month;
    if (day.length < 2) day = '0' + day;

    return ([year, month, day].join('-') + " " + [hour, min, sec].join(':') + "." + msec);
}

function sortByOptions(passData, sortBy) {
    if (sortBy == 'plannedSdate') {
        passData.sort((a, b) => {
            return new Date(a.plannedstartDate).getTime() - new Date(b.plannedstartDate).getTime();
        });
    }
    if (sortBy == 'plannedEdate') {
        passData.sort((a, b) => { return new Date(a.plannedclosedOn).getTime() - new Date(b.plannedclosedOn).getTime(); });
    }
    return passData;
}

function resetAllFilterOfWO() {
    resetDropdownLabel('.woStatusLbl', 'Status (All)', '');
    resetDropdownLabel('.woPriorityLbl', 'Priority (All)', '');
    resetDropdownLabel('.woSortByFilter', 'Sort By', '');
    $('#searchInWorkOrder').val('');
}

function getInputFilesDE(woidInput) {
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
                        htmlInput += `<li><a style="margin-left: 0px;" href="${d.inputUrl}"
                        target="_blank"><i class="fa fa-file-text"></i>${d.inputName}</a></li>`;
                    });
                }
                else {
                    htmlInput += '<li><a href="#">No Input File</a></li>';
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
    $('#inputFilesList_' + woidInput).html(htmlInput);
}

//Gets user proficiency for a particular WF
function getWFUserProficiency(signum, subActId, wfid, projectID, subActivityFlowChartDefID) {

    let proficiency = new Object();

    $.isf.ajax({
        url: service_java_URL + 'woExecution/getWFUserProficiency?signumID=' + signum + '&subactivityID=' + subActId + '&WFID=' + wfid + '&projectID=' + projectID + '&defID=' + subActivityFlowChartDefID,
        type: 'GET',
        async: false,
        success: function (data) {},
        complete: function (xhr, status) {
            if (status == "success") {
                if (xhr.responseJSON.isValidationFailed) {}
                else {
                    proficiency.proficiencyLevel = parseInt(xhr.responseJSON.responseData.proficiencyLevel);
                    proficiency.proficiencyId = parseInt(xhr.responseJSON.responseData.displayedMode);
                }
            }
        },
        error: function (xhr, status, statusText) {}
    });
    return proficiency;

}

function createWorkorderHtml(passData, callback) {


    //START - APPLY SORT BY FUNCTIONALITY
    //modifie json data according to sort by 
    if ($('.woSortByFilter').attr('data-searchtext').trim() !== '') {  // sort by request is not empty
        let sortByText = $('.woSortByFilter').attr('data-searchtext');
        passData = sortByOptions(passData, sortByText);
    } else {
        ConvertTimeZoneInPreferance(passData, getWorkOrders_tzColumns);
    }
    //END - APPLY SORT BY FUNCTIONALITY


    var data = passData;
    var woBox = '';
    var status = wfowner = woid = market = woName = projId = wfName = nodeTypa = nodeCount = dHours = mHours = priority = cPercentage = inPercentage = versionNum = subActivityID = subActivityFlowChartDefID = woPriority = '';

    var inprogressTotal = assignedTotal = onHoldTotal = defferedTotal = reopenedTotal = 0;
    var highTotal = lowTotal = criticalTotal = normalTotal = 0;
    let neRequired;
    let flowChartType;
    let progressTooltipText;
    let wfid;
    let nodeNamesArr;
    let nodeNamesUnique;
    let tooltipContent;
    let marketDetailsArray = [];
    let proficiencyLevel;
    let proficiencyId;
    let isWOFresh;
    var Doid;

    for (var i in data) {
        var panelClass = '';
        var statusSearchClass = prioritySearchClass = '';
        wfowner = data[i].wFOwnerName;
        subActivityFlowChartDefID = data[i].flowChartDefID;
        status = data[i].status;
        status = status.toUpperCase().trim();
        woplanid = data[i].woPlanID;
        woid = data[i].woID;
        woName = data[i].woName;
        market = data[i].market;
        projId = data[i].projectID;
        wfName = (data[i].workFlowName) ? data[i].workFlowName : "";
        nodeTypa = data[i].nodeType;
        nodeCount = data[i].nodeCount;
        dHours = data[i].digitialHours;
        mHours = data[i].manualHours;
        priority = data[i].priority;
        woPriority = data[i].woPriority;
        flowChartDefID = data[i].flowChartDefID;
        cPercentage = data[i].completedPercentage;
        inPercentage = data[i].inProgressPrecentage;
        versionNum = data[i].versionNumber;
        subActivityID = data[i].subActivityID;
        nodeName = data[i].nodeNames;
        isWOFresh = data[i].proficiencyType == null ? true : false;
        proficiencyLevel = data[i].proficiencyType !=null ? data[i].proficiencyType.proficiencyLevel : null;
        proficiencyId = data[i].proficiencyType != null ? data[i].proficiencyType.proficiencyID : null;


        let workOrderAutoSenseEnabled = data[i].workOrderAutoSenseEnabled;

        if (nodeName.length > 1) {
            var nodes = [];
            for (j = 0; j < nodeName.length; j++) {

                nodes.push(nodeName[j]);
            }
            nodeName = nodes.toString();
        }
        else {
            nodeName = data[i].nodeNames[0];
        }
        if (market.length > 1) {
            var marketName = [];
            for (j = 0; j < market.length; j++) {

                marketName.push(market[j]);
            }
            market = marketName.toString();
        }
        else {
            market = data[i].market[0];
        }
        actualDateStarted = data[i].actualstartDate;
        actualDateClosed = data[i].actualclosedOn;
        plannedDateStarted = data[i].plannedstartDate;
        plannedDateClosed = data[i].plannedclosedOn;
        Doid = data[i].doid;
        let deliverablePlanName = data[i].deliverablePlanName;
        let deliverableUnitName = data[i].deliverableUnitName;
        let parentWOID = data[i].parentWOID;
        progressTooltipText = data[i].progressDescription;
        wfid = data[i].wfid;
        var currDate = new Date();

        var x = formatDate(currDate);
        var plannedDateStartedinist = GetConvertedDateTime(plannedDateStarted);
        var startDateActualIST = GetConvertedDateTime(x);
        var difference = ((new Date(plannedDateStartedinist) - new Date(startDateActualIST)) / 1000 / 60)
        let experiencedMode = (parseInt(proficiencyLevel) > 1) ?1 : 0;
        neRequired = data[i].neRequired;
        flowChartType = data[i].flowChartType;

        currDate = new Date();

        var todaydate = formatDate(currDate);
        var cycleTime = ((new Date(plannedDateClosed) - new Date(todaydate)) / 1000 / 60);
        if (!nodeTypa) { nodeTypa = replaceNullBy; }
        if (!nodeName) { nodeName = replaceNullBy; }
        if (plannedDateStarted) {
        }
        else {
            plannedDateStarted = replaceNullBy;
        }
        if (plannedDateClosed) {
        }
        else {
            plannedDateClosed = replaceNullBy;
        }
        if (actualDateStarted) {
        }
        else {
            actualDateStarted = replaceNullBy;
        }
        if (actualDateClosed) {
        }
        else {
            actualDateClosed = replaceNullBy;
        }

        //COUNT STATUS
        if (status.toLowerCase().trim() === 'inprogress') {
            ++inprogressTotal;
            statusSearchClass = 's:inprogress';
        } else if (status.toLowerCase().trim() === 'assigned') {
            ++assignedTotal;
            statusSearchClass = 's:assigned';
        } else if (status.toLowerCase().trim() === 'onhold') {
            ++onHoldTotal;
            statusSearchClass = 's:onhold';
        } else if (status.toLowerCase().trim() === 'deferred') {
            ++defferedTotal;
            statusSearchClass = 's:deferred';
        } else if (status.toLowerCase().trim() === 'reopened') {
            ++reopenedTotal;
            statusSearchClass = 's:reopened';
        }

        //COUNT PRIORITY    
        if (priority.toLowerCase().trim() === 'high') {
            ++highTotal;
            panelClass = 'panel-danger';
            prioritySearchClass = 'p:high';
        } else if (priority.toLowerCase().trim() === 'low') {
            ++lowTotal;
            panelClass = 'panel-success';
            prioritySearchClass = 'p:low';
        } else if (priority.toLowerCase().trim() === 'normal') {
            ++normalTotal;
            panelClass = 'panel-info';
            prioritySearchClass = 'p:normal';
        } else if (priority.toLowerCase().trim() === 'critical') {
            ++criticalTotal;
            panelClass = 'panel-warning';
            prioritySearchClass = 'p:critical';
        }
        nodeName = escapeJsonHtml(nodeName);
        market = escapeJsonHtml(market);
        var viewFlowchartDetails = JSON.stringify({
            wfowner: wfowner, woid: woid, projectID: projId, subActId: subActivityID,
            vNum: versionNum, status: status, nodeNames: nodeName, experiencedMode: experiencedMode,
            wfName: wfName, neRequired: neRequired, flowChartType: flowChartType, subActivityFlowChartDefID: subActivityFlowChartDefID,
            difference: difference, wfid: wfid, doid: Doid, workOrderAutoSenseEnabled: workOrderAutoSenseEnabled,
            proficiencyLevel: proficiencyLevel, proficiencyId: proficiencyId, isWOFresh: isWOFresh
        });
        isLeadTimeBreached[woid] = { difference };
        isLeadTimeBreached[woid].comment = "";
        isLeadTimeBreached[woid].reason = "";

        if (nodeName != null && nodeName != undefined && nodeName != "" && nodeName.length != 0) {
        tooltipContent = breakTooltipContent(nodeName, 40);
        }
        tooltipContent = tooltipContent || '';
      
        woBox += '<div class="panel ' + panelClass + ' work_order_dBox work_order_dBox_' + woid + '">' +

            '<div class="panel-heading" style="font-size:15px;">' +
            '<span>WOID: ' + woid + '</span>' +
            '<div class="pull-right action-buttons">' +
            '<div class="btn-group pull-right infoDropdown" >' +
            '<a class="btn btn-info btn-xs on-wo-viewFlowchart" id="viewBtn_' + woid + '" data-details=\'' + viewFlowchartDetails + '\' title="View flow chart" href="#"><i class="fa fa-eye"></i></a>';
        if (status != 'DEFERRED') {
            woBox = `${woBox}<a onclick="cleanNodeFieldsWOPanel()" href="#" class="btn btn-info btn-xs ewoWorkOrder"  data-toggle="modal" data-target="#myModalMadEwo"
            data-status="${status}" data-workorder-id="${woid}" data-doid="${Doid}" data-woplan-id="${woplanid}" title="Edit Network Elements"><i class="fa fa-edit"></i></a>`;
        }

        woBox = `${woBox}<button class="btn btn-info btn-xs view-completed" id="completeBtn_${woid}" data-toggle="modal" data-woid="${woid}"
        data-woname="${escape(woName)}" data-subactdefid="${flowChartDefID}" data-cycletime="${cycleTime}" data-subactivityid="${subActivityID}" 
        data-wfId="${wfid}" data-projid="${projId}" type="button" title="Mark as completed"><i class="fa fa-check-square-o" aria-hidden="true"></i>
        </button>`;

        woBox += '<button type="button" class="btn btn-info btn-xs dropdown-toggle" onclick="getInputFilesDE(' + woid + ')" title="Take other action" data-toggle="dropdown">' +
            '<i class="fa fa-toggle-down" style="margin-right: 0px;"></i>' +
            '</button>' +
            '<ul class="dropdown-menu" role="menu">';

        if (status != 'DEFERRED') {
            woBox += '<li><a href="#" data-toggle="modal" onclick="hideNEAddEdit();" data-target="#myModalMadEwo" data-doid="' + Doid + '" data-woplan-id="' + woplanid + '" data-status="' + status + '" data-workorder-id="' + woid + '" class="viewWorkOrder">Edit / View Work Order</a></li>';
            woBox += '<li><a href="#"  data-wfid="' + wfid + '" data-subActId="' + subActivityID + '" data-projID="' + projId + '" data-versionNo="' + versionNum + '" data-defid="' + subActivityFlowChartDefID + '" data-proficiencylevel="' + proficiencyLevel + '" data-experiencedMode="' + experiencedMode + '" class="viewOnlyWorkFlow">View Work Flow</a></li>';
        }
        woBox += '<li><a href="#" data-toggle="modal" data-target="#viewBookingDetails" data-projid="' + projId + '" data-workorder-id="' + woid + '" class="view-booking-details">Booking Details</a></li>';
        if (status != "ONHOLD" && status != 'DEFERRED') {
            woBox += '<li><a href="#" class="view-hold" data-id="' + woid + '" data-priority="' + woPriority + '">Mark As On Hold</a></li>';
        }
        woBox = `${woBox}<li><a href="#" class="view-completed" data-toggle="modal" data-projid="${projId}" data-woid="${woid}" data-woname="${escape(woName)}"
        data-subactdefid="${flowChartDefID}" data-cycletime="${cycleTime}" data-subactivityid="${subActivityID}" data-wfid="${wfid}">Mark As Completed</a></li>`;
        if (status != 'DEFERRED') {
            woBox += '<li><a href="#" class="view-deffered" data-woid="' + woid + '"> Mark As Deferred</a></li>';
        }
        woBox += '<li><a id="openTransferWo_' + woid + '" data-details=\'' + viewFlowchartDetails + '\' href="#" style="display:none" class="view-transferWO"  data-target="#modalTransferWO" data-woid="' + woid + '"></a><a href="#" onclick="transferWo(' + woid + ')">Transfer Work Order</a></li>';

        woBox += '<li class="inputSubMenu dropdown-submenu"><a href="#" class="test">Input File URLs</a><ul class="inputFilesList dropdown-menu" id="inputFilesList_' + woid + '">';

        woBox += '</ul></li><li><a href="#"  data-toggle="modal" data-target="#outputFilesModal"  onclick="addOutput(' + woid + ',' + projId + '); getLocalGlobalURLs(\'outputFilesModal\',' + projId + ')">Provide Output File URLs</a></li>';

        woBox += '</ul>' +
            '</div>' +
            '</div>' +
            '</div>' +
            '<div class="panel-body">' +
            '<div style="display:none;">' + statusSearchClass + ' ' + prioritySearchClass + '</div>' +
            '<div id="myWorkOrderProgressBarArea_' + woid + '">';

        woBox += '</div><div class="content-row protip" id="toolTipCurrentWO_' + woid + '" data-pt-scheme="aqua" data-pt-position="bottom-left" data-pt-skin="square" data-pt-width="400" data-pt-title="' + tooltipContent.toString() + '">' +
            '<label>NEID: </label>' +
            '<p class="nodeNameP" id="nodeFieldCurrentWO_' + woid + '" >' + nodeName + '</p>' +
            '</div>' +
            '<div class="content-row">' +
            '<label>WO Name: </label>' +
            '<p>' + woName + '</p>' +
            '</div>' +
            '<div class="content-row">' +
            '<label> DOID: </label> <p>' + Doid + '</p> <br>' + '</div>' + '<div class="content-row">' +
            '<label> Deliverable Name: </label> <p>' + deliverablePlanName + '</p>' +
            '</div>' + '<div class="content-row">' +
            '<label> Delivery Unit Name: </label> <p>' + deliverableUnitName + '</p>' +
            '</div>' +
            '<div class="content-row marketDiv protip" id="toolTipCurrentWO_' + woid + '" data-pt-scheme="aqua" data-pt-position="bottom-left" data-pt-skin="square" data-pt-width="400" data-pt-title="Market holds city/state/circle/market of Network Elements">' +
            '<label>Market: </label>';

        if (nodeName != "" && nodeName != "-") {
            woBox += '<div class="nodeNameP marketP" id="marketCurrentWO_' + woid + '">' + market + '</div>';
        }
        else {
            woBox += '<div class="nodeNameP marketP" id="marketCurrentWO_' + woid + '">-</div>';
        }
        woBox += '</div>' +
            '<div class="content-row">' +
            '<label>Project Id: </label>' +
            '<p>' + projId + '</p>' +
            '</div>' +
            '<div class="content-row">' +
            '<label>WF Name: </label>' +
            '<p>' + wfName + '</p>' +
            '</div>' +
            '<div class="content-row">' +
            '<label class="' + parentWOID + '">Parent WOID: </label>';
        if (parentWOID == 0) { woBox += '<p>-</p>'; }
        else {
            woBox += '<p>' + parentWOID + '</p>';
        }
        woBox += '</div>' +
            '<div class="content-row">' +
            '<label>Efforts <br> (Digital/Manual): </label>' +
            '<p>' + dHours + '<br>' + mHours + '</p>' +
            '</div>' +
            '<div class="content-row">' +
            '<label>Actual Date <br> (Started/Closed): </label>' +
            '<p> ' + actualDateStarted + ' <br> ' + actualDateClosed + '</p>' +
            '</div>' +
            '<div class="content-row">' +
            '<label>Planned Date <bR>(Started/Closed): </label>' +
            '<p> ' + plannedDateStarted + ' <br> ' + plannedDateClosed + '</p>' +
            '</div>' +

            '</div>' +

            '</div>';

        //Call to load the Market of WO
        let marketDetails = {};
        if (data[i].nodeNames.length != 0) {
            marketDetails.projectID = projId;
            marketDetails.wOID = woid;
            marketDetails.nodeNames = data[i].nodeNames;

            marketDetailsArray.push(marketDetails);
    }
    }

    $('.work_order_panel .pull-right').show();
    $('.work_order_dBox_container').html('').append(woBox);

    var passParam = {};

    passParam.inprogressTotal = inprogressTotal;
    passParam.assignedTotal = assignedTotal;
    passParam.onHoldTotal = onHoldTotal;
    passParam.highTotal = highTotal;
    passParam.lowTotal = lowTotal;
    passParam.normalTotal = normalTotal;
    passParam.criticalTotal = criticalTotal;
    passParam.defferedTotal = defferedTotal;
    passParam.reopenedTotal = reopenedTotal;
    passParam.marketDetails = marketDetailsArray;

    if (typeof callback === "function") {

        callback(passParam);
    }

}

function checkFileURL(el) {
    let OK = true;
    let index = $(el).closest('li').index();
    let outputFileUrl = "";
    if (el == undefined)
        outputFileUrl = $("#editOutputUrl").val();
    else
        outputFileUrl = $("#outputFile li:eq(" + index + ") #outputUrl").val();
    if (outputFileUrl == "" || outputFileUrl == null) {
        pwIsf.alert({ msg: 'Please provide URL of the file!', type: 'warning' });
        OK = false;
    }
    else if (!(/^(http|https|ftp):\/\/[a-z0-9]+([\-\.]{1}[a-z0-9]+)*\.[a-z]{2,5}(:[0-9]{1,5})?(\/.*)?$/i.test(outputFileUrl))) {
        pwIsf.alert({ msg: 'Invalid URL', type: 'warning' });
        OK = false;
    }
    if (OK) {
        var win = window.open(outputFileUrl, '_blank');
        win.focus();
    }
}

function deleteFile(el) {
    let index = $(el).closest('li').index();
    $("#outputFile li:eq(" + index + ")").remove();
}

function addOutput(woid, projectID) {
    $('#woidOutput').val(woid);
    $('#woidProjectId').val(projectID);
    $('#outputFile').html('');
    let html = '<li><div class="row"><div class="col-lg-11" style="margin-bottom: 0px;"><div class="row"><div class="col-lg-4" style="margin-bottom: 0px;"><input id="outputName" class="inputFileClass" type="text">'
        + '</div><div class="col-lg-8" style="margin-bottom: 0px;"><input id="outputUrl" class="inputFileClass" type="text"></div>'
        + '</div></div><div class="col-lg-1" style="padding-left: 0px;margin-bottom: 0px;">'
        + '<a title="Check URL" style="color:#367bab;cursor:pointer" onclick="checkFileURL(this)"> <i class="fa fa-lg fa-paper-plane"></i></a> <a title="Delete File" onclick="deleteFile(this)" style="color: #d9534f;cursor:pointer"><i class="fa fa-lg fa-trash"></i></a>'
        + '</div></div></li>';
    $('#outputFile').append(html);
}



function filesArray() {
    let fileArr = [];
    let fileLength = $('#outputFile li').length;
    for (let i = 0; i < fileLength; i++) {
        let fileObj = new Object();
        fileObj["outputName"] = $("#outputFile li:eq(" + i + ") #outputName").val();
        fileObj["outputUrl"] = $("#outputFile li:eq(" + i + ") #outputUrl").val();
        fileArr.push(fileObj);
    }
    return fileArr;
}

function validateFiles() {

    let fileNameLength = $('#outputFile li').length;
    let filesArray = [];
    if (fileNameLength == 0) {
        pwIsf.alert({ msg: 'There is no file to add!', type: 'warning' });
        return false;
    }
    for (let i = 0; i < fileNameLength; i++) {
        let fileName = $("#outputFile li:eq(" + i + ") #outputName").val();
        let fileUrl = $("#outputFile li:eq(" + i + ") #outputUrl").val();
        if (fileName == "" || fileUrl == "" || fileName == null || fileUrl == null) {
            pwIsf.alert({ msg: 'Please provide both Name and URL of the file!', type: 'warning' });
            return false;
        }
        else if (!(/^(http|https|ftp):\/\/[a-z0-9]+([\-\.]{1}[a-z0-9]+)*\.[a-z]{2,5}(:[0-9]{1,5})?(\/.*)?$/i.test(fileUrl))) {
            pwIsf.alert({ msg: 'Invalid URL', type: 'warning' });
            return false;
        }
        filesArray.push(fileName);
    }
    let finalFilesArray = filesArray.sort();
    let finalFilesArrayDuplicate = [];
    for (var i = 0; i < finalFilesArray.length - 1; i++) {
        if (finalFilesArray[i + 1] == finalFilesArray[i]) {
            finalFilesArrayDuplicate.push(finalFilesArray[i]);
        }
    }
    if (finalFilesArrayDuplicate.length != 0) {
        let filesString = finalFilesArrayDuplicate.toString();
        let isOrAre = '';
        if (finalFilesArrayDuplicate.length == 1)
            isOrAre = 'is';
        else
            isOrAre = 'are'
        pwIsf.alert({ msg: '(' + filesString + ') ' + isOrAre + ' already added', type: 'warning' });
        return false;
    }
    return true;

}

function addOutputFiles() {
    let listOfFiles = filesArray();
    let woOutputObj = new Object();
    woOutputObj["woid"] = $('#woidOutput').val();
    woOutputObj["file"] = listOfFiles;
    woOutputObj["createdBy"] = signumGlobal;
    woOutputObj["projectID"] = $('#woidProjectId').val();
    pwIsf.addLayer({ text: "Please wait ..." });
    $.isf.ajax({
        url: `${service_java_URL}woManagement/insertOutputFileWO`,
        context: this,
        crossdomain: true,
        processData: true,
        contentType: 'application/json',
        type: 'POST',
        data: JSON.stringify(woOutputObj),
        success: function (data) {

            let success = responseHandler(data);
            if (success) {
                $('#outputFilesModal').modal('hide');
                $('#outputName').val('');
                $('#outputUrl').val('');
                let woidOutput = $('#woidOutput').val();
                $('#woidOutput').val('');
            }
            pwIsf.removeLayer();
        },
        error: function (xhr, status, statusText) {
            var err = JSON.parse(xhr.responseText);
            pwIsf.alert({ msg: err.errorMessage, type: 'error' });
            pwIsf.removeLayer();

        }
    });
}

function giveTitle(el) {
    let title = $(el).val();
    $(el).attr('title', title);
}

function checkFileUrlDE(el) {
    let OK = true;
    let index = $(el).closest('li').index();
    let outputFileUrl = "";
    outputFileUrl = $("#outputFileWP li:eq(" + index + ") #outputUrlWP").val();
    if (outputFileUrl == "" || outputFileUrl == null) {
        pwIsf.alert({ msg: 'Please provide URL of the file!', type: 'warning' });
        OK = false;
    }
    else if (!(/^(http|https|ftp):\/\/[a-z0-9]+([\-\.]{1}[a-z0-9]+)*\.[a-z]{2,5}(:[0-9]{1,5})?(\/.*)?$/i.test(outputFileUrl))) {
        pwIsf.alert({ msg: 'Invalid URL', type: 'warning' });
        OK = false;
    }
    if (OK) {
        var win = window.open(outputFileUrl, '_blank');
        win.focus();
    }
}

function editOutputFilesDE() {
    $('#outputUpdateButton').show();
    $('#addOutputButton').show();
    $('#editOutputButton').hide();
    $('#outputFileHeadersWP').show();
    if ($('#outputFileWP').html() == '<li><a href="#">No Output URL</a></li>') {
        let html = '<li><input id="outputIndexWO" type="hidden"><div class="row"><div class="col-lg-11" style="margin-bottom: 0px;"><div class="row"><div class="col-lg-4" style="margin-bottom: 0px;"><input onkeyup="giveTitle(this)" id="outputNameWP" class="inputFileClass" type="text">'
            + '</div><div class="col-lg-8" style="margin-bottom: 0px;"><input onkeyup="giveTitle(this)" id="outputUrlWP" class="inputFileClass" type="text"></div>'
            + '</div></div><div class="col-lg-1" style="padding-left: 0px;margin-bottom: 0px;">'
            + '<a title="Check URL" style="color:#367bab;cursor:pointer" onclick="checkFileUrlDE(this)"> <i class="fa fa-lg fa-paper-plane"></i></a> <a title="Delete File" onclick="deleteFileDE(this)" style="color: #d9534f;cursor:pointer"><i class="fa fa-lg fa-trash"></i></a>'
            + '</div></div></li>';
        $('#outputFileWP').html('');
        $('#outputFileWP').append(html);
        $('#outputUpdateButton').show();
    }
    else {
        let fileNameLength = $('#outputFileWP li').length;
        for (let i = 0; i < fileNameLength; i++) {
            $("#outputFileWP li:eq(" + i + ") #outputNameWP").attr('disabled', false);
            $("#outputFileWP li:eq(" + i + ") #outputUrlWP").attr('disabled', false);
            $("#outputFileWP li:eq(" + i + ") #deleteFilesDE").show();
        }
    }
}

function validateNameAndUrlDE() {
    let fileNameLength = $('#outputFileWP li').length;
    let filesArray = [];
    if (fileNameLength == 0) {
        pwIsf.alert({ msg: 'There is no file to add!', type: 'warning' });
        return false;
    }
    for (let i = 0; i < fileNameLength; i++) {
        let fileName = $("#outputFileWP li:eq(" + i + ") #outputNameWP").val();
        let fileUrl = $("#outputFileWP li:eq(" + i + ") #outputUrlWP").val();
        if (fileName == "" || fileUrl == "" || fileName == null || fileUrl == null) {
            pwIsf.alert({ msg: 'Please provide both Name and URL of the file!', type: 'warning' });
            return false;
        }
        else if (!(/^(http|https|ftp):\/\/[a-z0-9]+([\-\.]{1}[a-z0-9]+)*\.[a-z]{2,5}(:[0-9]{1,5})?(\/.*)?$/i.test(fileUrl))) {
            pwIsf.alert({ msg: 'Invalid URL', type: 'warning' });
            return false;
        }
        filesArray.push(fileName);
    }
    let finalFilesArray = filesArray.sort();
    let finalFilesArrayDuplicate = [];
    for (var i = 0; i < finalFilesArray.length - 1; i++) {
        if (finalFilesArray[i + 1] == finalFilesArray[i]) {
            finalFilesArrayDuplicate.push(finalFilesArray[i]);
        }
    }
    if (finalFilesArrayDuplicate.length != 0) {
        let filesString = finalFilesArrayDuplicate.toString();
        let isOrAre = '';
        if (finalFilesArrayDuplicate.length == 1)
            isOrAre = 'is';
        else
            isOrAre = 'are'
        pwIsf.alert({ msg: '(' + filesString + ') ' + isOrAre + ' already added', type: 'warning' });
        return false;
    }
    return true;
}

function updateOutputFileWO() {
    let outputFileObj = new Object();
    outputFileObj.woid = $('#outputWOIDWO').val();
    let outputFileArrList = [];
    let listLength = $('#outputFileWP li').length;
    for (let i = 0; i < listLength; i++) {
        let id = $('#outputFileWP li:eq(' + i + ') #outputIndexWO').val();
        let name = $('#outputFileWP li:eq(' + i + ') #outputNameWP').val();
        let url = $('#outputFileWP li:eq(' + i + ') #outputUrlWP').val();
        let output = new Object();
        if (id == "" || id == null)
            id = 0;
        output["id"] = id;
        output["outputName"] = name;
        output["outputUrl"] = url;
        outputFileArrList.push(output);
    }
    outputFileObj.file = outputFileArrList;
    outputFileObj.createdBy = signumGlobal;
    outputFileObj.projectID = $('#outputProjectID').val();
    $.isf.ajax({
        url: `${service_java_URL}woManagement/editOutputFile`,
        crossdomain: true,
        processData: true,
        contentType: C_CONTENT_TYPE_APPLICATION_JSON,
        type: 'POST',
        data: JSON.stringify(outputFileObj),
        success: function (data) {

            let success = responseHandler(data);
            if (success) {
                $('#OpenOutputFilesModal').modal('hide');
                $('#inputWOIDWO').val('');
            }
        },
        error: function (xhr, status, statusText) {
            pwIsf.alert({ msg: "Error", type: 'error' });
        }
    });
}


function addMoreOutputFilesDE() {

    let html = '<li><input id="outputIndexWO" type="hidden"><div class="row"><div class="col-lg-11" style="margin-bottom: 0px;"><div class="row"><div class="col-lg-4" style="margin-bottom: 0px;"><input onkeyup="giveTitle(this)" id="outputNameWP" class="inputFileClass" type="text">'
        + '</div><div class="col-lg-8" style="margin-bottom: 0px;"><input onkeyup="giveTitle(this)" id="outputUrlWP" class="inputFileClass" type="text"></div>'
        + '</div></div><div class="col-lg-1" style="padding-left: 0px;margin-bottom: 0px;">'
        + '<a title="Check URL" style="color:#367bab;cursor:pointer" onclick="checkFileUrlDE(this)"> <i class="fa fa-lg fa-paper-plane"></i></a> <a title="Delete File" onclick="deleteFileDE(this)" style="color: #d9534f;cursor:pointer"><i class="fa fa-lg fa-trash"></i></a>'
        + '</div></div></li>';
    $('#outputFileWP').append(html);
}

function getOutputFilesFlowChartDE(woidOutput, projectId) {
    let htmlInput = '';
    $('#addOutputButton').hide();
    $('#editOutputButton').show();
    $('#outputWOIDWO').val(woidOutput);
    $('#outputProjectID').val(projectId);
    $('#outputFileWP').html(htmlInput);
    $.isf.ajax({
        url: `${service_java_URL}woManagement/getWOOutputFile?woid=${woidOutput}`,
        async: false,
        success: function (data) {
            if (data.responseData.length != 0 && data.isValidationFailed == false) {
                $('#outputFileHeadersWP').show();
                $.each(data.responseData, function (i, d) {
                    htmlInput += '<li><div class="row"><div class="col-lg-11" style="margin-bottom: 0px;"><div class="row"><div class="col-lg-4" style="margin-bottom: 0px;"><input onkeyup="giveTitle(this)" id="outputNameWP" class="inputFileClass" type="text" title=\"' + d.outputName + '\" value=\"' + d.outputName + '\" disabled><input id="outputIndexWO" type="hidden" value=' + d.id + '>'
                        + '</div><div class="col-lg-8" style="margin-bottom: 0px;"><input onkeyup="giveTitle(this)" id="outputUrlWP" class="inputFileClass" type="text" title=\"' + d.outputUrl + '\"  value=\"' + d.outputUrl + '\" disabled></div>'
                        + '</div></div><div class="col-lg-1" style="padding-left: 0px;margin-bottom: 0px;">'
                        + '<a title="Check URL" style="color:#367bab;cursor:pointer" onclick="checkFileUrlDE(this)"> <i class="fa fa-lg fa-paper-plane"></i></a> <a id="deleteFilesDE" title="Delete File" onclick="deleteFileDE(this)" style="color: #d9534f;cursor:pointer;display:none"><i class="fa fa-lg fa-trash"></i></a>'
                        + '</div></div></li>'
                });
            }
            else {
                htmlInput += '<li><a href="#">No Output URL</a></li>';
                $('#outputFileHeadersWP').hide();
                $('#outputUpdateButton').hide();
            }
        }
    })
    $('#outputFileWP').append(htmlInput);
}

function deleteFileDE(el) {
    let index = $(el).closest('li').index();
    let id = $("#outputFileWP li:eq(" + index + ") #outputIndexWO").val();
    if (id == null || id == "")
        $("#outputFileWP li:eq(" + index + ")").remove();
    else {
        let btnJson = {
            'Yes': {
                'action': function () {
                    $.isf.ajax({
                        url: `${service_java_URL}woManagement/deleteOutputFile/${id}/${signumGlobal}`,
                        crossdomain: true,
                        processData: true,
                        contentType: C_CONTENT_TYPE_APPLICATION_JSONon-wo-viewFlowchart,
                        success: function (data) {
                            $("#outputFileWP li:eq(" + index + ")").remove();
                            pwIsf.alert({ msg: "Output file deleted.", type: 'success' });
                        },
                        error: function (xhr, status, statusText) {
                            pwIsf.alert({ msg: "Error", type: 'error' });
                        }
                    });
                },
                'class': 'btn btn-success'
            },
            'No': {
                'action': function () {

                },
                'class': 'btn btn-danger'
            },

        };

        pwIsf.confirm({
            title: 'Do you want to delete this file?',
            msg: '',
            'buttons': btnJson
        });
    }

}

function addMoreOutputFiles() {

    let html = '<li><div class="row"><div class="col-lg-11" style="margin-bottom: 0px;"><div class="row"><div class="col-lg-4" style="margin-bottom: 0px;"><input id="outputName" class="inputFileClass" type="text">'
        + '</div><div class="col-lg-8" style="margin-bottom: 0px;"><input id="outputUrl" class="inputFileClass" type="text"></div>'
        + '</div></div><div class="col-lg-1" style="padding-left: 0px;margin-bottom: 0px;">'
        + '<a title="Check URL" style="color:#367bab;cursor:pointer" onclick="checkFileURL(this)"> <i class="fa fa-lg fa-paper-plane"></i></a> <a title="Delete File" onclick="deleteFile(this)" style="color: #d9534f;cursor:pointer"><i class="fa fa-lg fa-trash"></i></a>'
        + '</div></div></li>';
    $('#outputFile').append(html);
}

$(document).on("click", ".viewOnlyWorkFlow", function () {

    var version_id = $(this).data('versionno');

    if (version_id != -1) {

        $('#versionNumber').val(version_id);
        var subActID = $(this).data('subactid');
        var projID = $(this).data('projid');
        let experiencedMode = $(this).data('experiencedmode');
        let wfid = $(this).data('wfid');
        let defid = $(this).data('defid');
        let proficiencyLevel = $(this).data('proficiencylevel');

        //Check if user is opening WF for the first time
        if (proficiencyLevel == null) {
            pwIsf.addLayer({ text: "Please wait ..." });

            setTimeout(function () {
                proficiency = getWFUserProficiency(signumGlobal, subActID, wfid, projID, defid);
                experiencedMode = (parseInt(proficiency.proficiencyLevel) > 1) ? 1 : 0;
                pwIsf.removeLayer();
                flowChartViewOpenInNewWindow('../Project/FlowChartWorkOrderView\?subId=' + subActID + '&projID=' + projID + '&version=' + version_id + '&experiencedMode=' + experiencedMode + '&wfid=' + wfid);
            }, 0);           

        } else {
            flowChartViewOpenInNewWindow('../Project/FlowChartWorkOrderView\?subId=' + subActID + '&projID=' + projID + '&version=' + version_id + '&experiencedMode=' + experiencedMode + '&wfid=' + wfid);
        }
        
    }
    else {
        alert("Please select Version.");
    }

});

function flowChartViewOpenInNewWindow(url) {
    var width = window.innerWidth * 0.85;
    // define the height in
    var height = width * window.innerHeight / window.innerWidth;
    // Ratio the hight to the width as the user screen ratio
    window.open(url, 'newwindow', 'width=' + width + ', height=' + height + ', top=' + ((window.innerHeight - height) / 2) + ', left=' + ((window.innerWidth - width) / 2));

}


function transferWo(woId) {
    if (isWoAnyStepRunning(woId)) {
        pwIsf.alert({ msg: "Work Order cannot be transferred while in progress", type: 'error' });
    } else {
        if (JSON.parse(ActiveProfileSession).accessProfileName == "ASP") {
            $('#selectAspUserSignumTransfer')
                .find('option')
                .remove();
            $('#selectallSignumTransferInProgressdiv').remove();
            getAspByWoId(woId);
        }
        else {
            $('#selectAspUserSignumTransferDiv').hide();
        }
        $("#openTransferWo_" + woId).click();
        console.log("show MOdel");
    }
}

function sleep(ms) {
    return new Promise(resolve => setTimeout(resolve, ms));
}

//Market Details for Work Orders.
async function appendMarketDetails(tempArrayWOwithoutNEID) {
    let j = 0;
    let marketDetailsArray = [];

    for (let i in tempArrayWOwithoutNEID) {
        let marketDetails = {};
        marketDetails.projectID = tempArrayWOwithoutNEID[i].projectID;
        marketDetails.wOID = tempArrayWOwithoutNEID[i].woID;
        marketDetails.nodeNames = tempArrayWOwithoutNEID[i].nodeNames;
        marketDetailsArray.push(marketDetails);
    }
    if (global_marketsOfWO.length != 0) {
        setTimeout(function () {
            fillMarketOfWO(global_marketsOfWO);
        }, 2000);
    }
    else {
        setTimeout(function () {
            if (marketDetailsArray.length != 0) {
                getMarketOfWorkOrder(marketDetailsArray);
            }
        }, 2000);

    }
}

function requestWorkorder() {

    var serviceUrl = service_java_URL + "woExecution/v1/getWorkOrders?signumID=" + signumGlobal + "&" + "status=INPROGRESS,ASSIGNED,ONHOLD,REOPENED";
    if (ApiProxy == true) {
        serviceUrl = service_java_URL + "woExecution/v1/getWorkOrders?signumID=" + encodeURIComponent(signumGlobal + "&" + "status=INPROGRESS,ASSIGNED,ONHOLD,REOPENED");
    }
    $('.work_order_dBox_container').html(pleaseWaitISF({ text: 'Loading your work order(s) ...' }));

    var jqxhr = $.isf.ajax({
        url: serviceUrl,
        async: false,
        returnAjaxObj: true
    });

    jqxhr.done(function (data) {
        if (data.isValidationFailed == false && data.responseData.length != 0) {
            var d = data.responseData;

            globalWorkOrderData = d;
            createWorkorderHtml(d, bindElementOfWorkorder);
        }

        else {
            $('.work_order_dBox_container').html('<div class="funMsg">No Work Order Found For You.</div>');
            $('.work_order_panel .pull-right').hide();
        }
    });
    jqxhr.always(function () {
        $('#searchInWorkOrder').trigger('keyup');
    });
    $('.marketP .fa-spin').css('font-size', '12px');
}


function RequestWorkOrderUsingWOId(woID) {
    var serviceUrl = service_java_URL + "woExecution/v1/getWorkOrders?signumID=" + signumGlobal + "&" + "status=INPROGRESS,ASSIGNED,ONHOLD,REOPENED";
    if (ApiProxy) {
        serviceUrl = service_java_URL + "woExecution/v1/getWorkOrders?signumID=" + encodeURIComponent(signumGlobal + "&" + "status=INPROGRESS,ASSIGNED,ONHOLD,REOPENED");
    }
    $('.work_order_dBox_container').html(pleaseWaitISF({ text: 'Loading your work order(s) ...' }));

    var jqxhr = $.isf.ajax({
        url: serviceUrl,
        async: false,
        returnAjaxObj: true
    });

    jqxhr.done(function (data) {
        var d = '';
        if (!data.isValidationFailed) {
            d = data.responseData;

        if (d.length) {
            var openWo = d.find(function (o) { return o.woID == woID; })
            var wfMaxWidthAllowed = getTextWidth("WWWWW...");
            var neidMaxWidthAllowed = getTextWidth("WWWWWWW...");
            if (openWo) {
                var wfNameWidthOriginal = getTextWidth(openWo.workFlowName);
                var wfCharWidth = 0;
                var wfCharCountShort = 0;
                var wfCharCountOriginal = openWo.workFlowName.length;
                for (let i = 0; i < wfCharCountOriginal; i++) {
                    if (wfCharWidth <= wfMaxWidthAllowed) {
                        wfCharWidth += getTextWidth(openWo.workFlowName[i]);
                        wfCharCountShort = i;
                    }
                }
                if (wfNameWidthOriginal >= wfMaxWidthAllowed) {
                    document.title = 'ISF-WOID#' + openWo.woID + ', WF-' + openWo.workFlowName.substr(0, wfCharCountShort) + '...';
                }
                else {
                    document.title = 'ISF-WOID#' + openWo.woID + ', WF-' + openWo.workFlowName.substr(0, wfCharCountOriginal);
                }
                    let tempnodeName = ''
                    tempnodeName = jQuery.unique(openWo.nodeNames.sort()).join(',');
                    tempnodeName = removeDuplicateNEID(tempnodeName);
                    var nodeNameWidthOriginal = getTextWidth(tempnodeName);
                    if (openWo != undefined && openWo != null && tempnodeName != undefined && tempnodeName != null) {
                        var neidCharCountOriginal = tempnodeName.length;
                    var neidCharWidth = 0;
                    var neidCharCountShort = 0;
                    for (let i = 0; i < neidCharCountOriginal; i++) {
                        if (neidCharWidth <= neidMaxWidthAllowed) {
                                neidCharWidth += getTextWidth(tempnodeName[i]);
                            neidCharCountShort = i;
                        }
                    }
                    if (nodeNameWidthOriginal >= neidMaxWidthAllowed) {

                            document.title = document.title + ', NEID-' + tempnodeName.substr(0, neidCharCountShort) + '...';
                    }
                    else {
                            document.title = document.title + ', NEID-' + tempnodeName.substr(0, neidCharCountOriginal);
                    }
                }
                

            }

            globalWorkOrderData = d;
            createWorkorderHtml(d, bindElementOfWorkorder);
            triggerButton(woID);
            }


            else {
            $('.work_order_dBox_container').html('<div class="funMsg">No Work Order Found For You.</div>');
            $('.work_order_panel .pull-right').hide();
        }
        }
        else {
            $('.work_order_dBox_container').html('<div class="funMsg">No Work Orders Found For You.</div>');
            $('.work_order_panel .pull-right').hide();
        }
    })
    jqxhr.fail(function () {
        console.log("method RequestWorkOrderUsingWOId is having issue while ajax call");
    })
    jqxhr.always(function () {
        $('#searchInWorkOrder').trigger('keyup');

    });

    $('.marketP .fa-spin').css('font-size', '12px');
}
function removeDuplicateNEID(nodeID) {
    var x;
    if (nodeID) {
        x = Array.from(new Set(nodeID.split(','))).toString();
    }
    return x;
}
function getTextWidth(inputText) {
    font = "12px times new roman";
    canvas = document.createElement("canvas");
    context = canvas.getContext("2d");
    context.font = font;
    width = context.measureText(inputText).width;
    formattedWidth = Math.ceil(width);
    return formattedWidth;
}

function isQueryStringWoID(fruit) {
    return fruit.woID === woID;
}
function triggerButton(woID) {
    var workorderpanel = $(".work_order_dBox_container").find('span').filter(function () { return ($(this).text().indexOf(woID) > -1) });
    if (workorderpanel.length > 0) {
        var viewFCBtn = workorderpanel.closest('.panel-heading').find('.on-wo-viewFlowchart');
        $(viewFCBtn).trigger("click");

        var idleInterval = "";
        clearInterval(idleInterval);
        idleInterval = setInterval(deliveryTimerIncrement, 5000); // 1 second  
        //  extendSession();
        //getAllSessionValues();



    }
    else {
        pwIsf.alert({ msg: "The Requested WOID does not belong here!", type: "warning", autoClose: 2 })
    }

}
//END - WORK ORDER AREA

function cycleTimeReasonPost() {

    var cycle_time = new Object();
    var todaydate = new Date();

    var role = JSON.parse(ActiveProfileSession).role;
    if (role == "Default User")
        role = "Network Engineer";
    cycle_time.actorType = role;
    cycle_time.auditPageId = $("#workOrderIDComp").val();
    cycle_time.auditgroupid = 0;
    cycle_time.auditGroupCategory = "WORK_ORDER";
    cycle_time.additionalInfo = $("#reasonCycleTime").val();
    cycle_time.content = $("#commentCompleted").val();
    cycle_time.context = "WORK_ORDER" + "_" + $("#workOrderIDComp").val();
    cycle_time.created = todaydate;
    cycle_time.fullname = signumGlobal;
    cycle_time.importance = 0;
    cycle_time.modified = todaydate;
    cycle_time.notificationFlag = 0;
    cycle_time.parent = 0;
    cycle_time.type = "Cycle Time";
    cycle_time.fieldName = "";
    cycle_time.fileURL = "";
    cycle_time.id = 0;
    cycle_time.newValue = "";
    cycle_time.oldValue = "";
    cycle_time.commentCategory = "WO_CYCLE_TIME_BREACH";
    var isAuditColumnlengthExceeded = false; 

    if (cycle_time.content.length > 1000 || cycle_time.context.length > 1000 || cycle_time.additionalInfo.length > 1000) {
        isAuditColumnlengthExceeded = true;       
    }
    if (!isAuditColumnlengthExceeded) {
        $.isf.ajax({
            url: `${service_java_URL}auditing/addComment`,
            type: 'POST',
            crossDomain: true,
            context: this,
            contentType: C_CONTENT_TYPE_APPLICATION_JSON,
            data: JSON.stringify(cycle_time),

            success: function (returndata) {

                if (returndata.isValidationFailed == true) {
                    pwIsf.alert({ msg: returndata.formErrors[0], type: 'warning' });
                }
                else {
                    //  pwIsf.alert({ msg: returndata.formMessages[0], type: 'success', autoClose: 3 });
                    NotifyMobileDevice(cycle_time, returndata.responseData);
                }
            },
            error: function (xhr, status, statusText) {
                pwIsf.alert({ msg: 'Reason and comment cannot be stored', type: 'error' });
                //   pwIsf.alert({ msg: returndata.formErrors[0], type: 'warning' });
            },

        });
    }
    else {
        pwIsf.alert({ msg: "Max comment character length(1000) exceeded", type: "error", autoClose: 4 });
    }

}

function leadTimeReasonCommentPost(woid, diff) {

    var lead_time = new Object();
    var todaydate = new Date();
    var role = JSON.parse(ActiveProfileSession).role;

    if (role == "Default User")
        role = "Network Engineer";

    lead_time.actorType = role;
    lead_time.auditPageId = woid;
    lead_time.auditgroupid = 0;
    lead_time.auditGroupCategory = "WORK_ORDER";
    lead_time.additionalInfo = $("#sourceId3").val();
    lead_time.content = $("#commentsBox").val();
    lead_time.context = "WORK_ORDER" + "_" + woid;
    lead_time.created = todaydate;
    lead_time.fullname = signumGlobal;
    lead_time.importance = 0;
    lead_time.modified = todaydate;
    lead_time.notificationFlag = 0;
    lead_time.parent = 0;
    lead_time.type = "Lead Time";
    lead_time.fieldName = "";
    lead_time.fileURL = "";
    lead_time.newValue = "";
    lead_time.oldValue = "";
    lead_time.commentCategory = "WO_LEAD_TIME_BREACH";
    let isAuditColumnlengthExceeded = false;

    if (lead_time.content.length > 1000 || lead_time.context.length > 1000 || lead_time.additionalInfo.length > 1000) {
        isAuditColumnlengthExceeded = true;
    }

    if (!isAuditColumnlengthExceeded) {
        $.isf.ajax({
            url: service_java_URL + 'auditing/addComment',
            type: 'POST',
            crossDomain: true,
            context: this,
            contentType: "application/json",
            data: JSON.stringify(lead_time),
            success: function (data) {
                if (data.isValidationFailed == true) {
                    pwIsf.alert({ msg: data.formErrors[0], type: 'warning' });
                }
                else {
                    //  pwIsf.alert({ msg: data.formMessages[0], type: 'success', autoClose: 3 });
                    NotifyMobileDevice(lead_time, data.responseData);
                }
            },
            error: function (xhr, status, statusText) {
                pwIsf.alert({ msg: 'Reason and comment cannot be stored', type: 'error' });
                //  pwIsf.alert({ msg: data.formErrors[0], type: 'error' });
            }

        });
    }
    else {
        pwIsf.alert({ msg: "Max comment character length(1000) exceeded", type: "error", autoClose: 4 });
    }

}

function leadTimeReasonCommentPostNeid(woid, comment, reason, diff) {

    var lead_time = new Object();
    var todaydate = new Date();
    var role = JSON.parse(ActiveProfileSession).role;
    if (role == "Default User")
        role = "Network Engineer";
    lead_time.actorType = role;
    lead_time.auditPageId = woid;
    lead_time.auditgroupid = 0;
    lead_time.auditGroupCategory = "WORK_ORDER";
    lead_time.additionalInfo = reason;
    lead_time.context = "WORK_ORDER" + "_" + woid;
    lead_time.content = comment;
    lead_time.created = todaydate;
    lead_time.fullname = signumGlobal;
    lead_time.importance = 0;
    lead_time.modified = todaydate;
    lead_time.notificationFlag = 0;
    lead_time.parent = 0;
    lead_time.type = "Lead Time";
    lead_time.fieldName = "";
    lead_time.fileURL = "";
    lead_time.newValue = "";
    lead_time.oldValue = "";
    lead_time.commentCategory = "WO_LEAD_TIME_BREACH";

    let isAuditColumnlengthExceeded = false;

    if (lead_time.content.length > 1000 || lead_time.context.length > 1000 || lead_time.additionalInfo.length > 1000) {
        isAuditColumnlengthExceeded = true;
    }

    if (!isAuditColumnlengthExceeded) {

        $.isf.ajax({
            url: `${service_java_URL}auditing/addComment`,
            type: 'POST',
            crossDomain: true,
            context: this,
            contentType: C_CONTENT_TYPE_APPLICATION_JSON,
            data: JSON.stringify(lead_time),

            success: function (returndata) {
                if (returndata.isValidationFailed == true) {
                    pwIsf.alert({ msg: returndata.formErrors[0], type: 'warning' });
                }
                else {
                    NotifyMobileDevice(lead_time, returndata.responseData);
                }
            },
            error: function (xhr, status, statusText) {
                pwIsf.alert({ msg: 'Reason and comment cannot be stored', type: 'error' });
            }
        });
    }
    else {
        pwIsf.alert({ msg: "Max comment character length(1000) exceeded", type: "error", autoClose: 4 });
    }
}

//Open Node update div in warning pop up
function createTransitionLeadTime() {
    $('#node_box_DE').css("display", "inline");
    $("#slideUp").css("display", "none");
    $("#slideDwn").css("display", "inline");
    //getWFNodeTypeWarning();
    getWODetailsWarning();
}

//Close Node update div in warning pop up
function resetTransition() {
    $(".mandatory_Box").slideDown();
    $('#node_box_DE').css("display", "none");
    $("#slideUp").css("display", "inline");
    $("#slideDwn").css("display", "none");
    let nodeType = $("#select_nodeType").val();
    let nodeType2 = $("#select_nodeType2").val();
    let nodeNameAuto = $("#woc_select_nodeName").select2("val");
    let vendor = $('#select_vendor').val();
    let market = $('#select_markArea').val();
    if (nodeNameAuto == null || nodeNameAuto == "" || nodeType == '' || nodeType == null || nodeType2 == '' || nodeType2 == null || vendor == '' || vendor == undefined || vendor == null || vendor == 0 || market == '' || market == undefined || market == null || market == 0) {
        $('#woc_select_nodeName').find('option').remove();
        $("#woc_select_nodeName").select2("val", "");//for select
        $('#select_nodeType').find('option').remove();
        $("#select_nodeType").select2("val", "");
        $('#select_nodeType2').find('option').remove();
        $("#select_nodeType2").select2("val", "");
        $('#select_markArea').find('option').remove();
        $('#select_markArea').select2("val", "");
        $('#select_vendor').find('option').remove();
        $('#select_vendor').select2("val", "");
        pwIsf.alert({ msg: 'Network Element details are not provided properly, the respective data will be lost!', type: 'warning' });
    }
}

//Validate Reason and Comment in warning pop up
function validateReasonComment(passObj) {
    var reason = $("#sourceId3").val();
    var comment = $("#commentsBox").val();
    if (reason == "0") {
        pwIsf.alert({ msg: 'Please select a reason', type: 'warning' });
        return false;
    }
    else if (reason == "Others" && !comment) {
        pwIsf.alert({ msg: 'Comments cannot be left blank!', type: 'warning' });
        return false;
    }
    else {
        isLeadTimeBreached[passObj.woid].comment = comment;
        isLeadTimeBreached[passObj.woid].reason = reason;
        return true;
    }
}

//Validation of node fields in warning pop up
function validateNodesFields() {
    OK = true;
    let nodeType = $("#select_nodeType").val();
    let nodeType2 = $("#select_nodeType2").val();
    let nodeNameAuto = $("#woc_select_nodeName").select2("val");
    let vendor = $('#select_vendor').val();
    let market = $('#select_markArea').val();
    if (nodeNameAuto == null || nodeNameAuto == "" || nodeType == '' || nodeType == null || nodeType2 == '' || nodeType2 == null || vendor == '' || vendor == undefined || vendor == null || vendor == 0 || market == '' || market == undefined || market == null || market == 0) {
        OK = false;
        pwIsf.alert({ msg: 'You have not filled all the node related fields! ', type: 'warning' });
    }
    return OK;
}

//Open Work Flow on click of OK in warning pop up
function openWorkFlow(afterLoadingFlowchart, that) {
    var comment = $("#commentsBox").val();
    if (comment.length > 1000) {
        pwIsf.alert({ msg: "Max comment character length(1000) exceeded", type: "error", autoClose: 4 });
    }
    else {
        let passObj = $(that).data('details');
        let doid = passObj.doid;
        let leadTimeDiff = passObj.difference;
        if ((leadTimeDiff < 0)) {
            if (validateReasonComment(passObj)) {
                if (($("#node_box_DE").is(":visible") && updateNodesWarning(doid)) || !$("#node_box_DE").is(":visible")) {
                    leadTimeReasonCommentPost(passObj.woid, passObj.difference);
                    createFlowchartPanel(passObj, afterLoadingFlowchart);
                    $(that).attr('disabled', true);
                    $('#flowChartWarning').modal('hide');
                    resetNetworkElementFields('neMandatory');
                }
                else {
                    if ($('#btnValidateNEAddEdit_neMandatory').hasClass("btnEnabledForNEAddEdit")) {
                        pwIsf.alert({ msg: "Validate Network Elements", type: "warning" });
                    }
                    else {
                        pwIsf.alert({ msg: warningMsg, type: "warning" });
                    }
                    nodesUpdateSuccess = true;
                }
            } else
                return;
        }
        else if (($("#node_box_DE").is(":visible") && updateNodesWarning(doid)) || !$("#node_box_DE").is(":visible")) {
            createFlowchartPanel(passObj, afterLoadingFlowchart);
            $(that).attr('disabled', true);
            $('#flowChartWarning').modal('hide');
            resetNetworkElementFields('neMandatory');
        }
        else {
                if ($('#btnValidateNEAddEdit_neMandatory').hasClass(`btnEnabledForNEAddEdit`)) {
                    pwIsf.alert({ msg: "Validate Network Elements", type: "warning" });
                }
                else {
                    pwIsf.alert({ msg: warningMsg, type: "warning" });
                }
            
                nodesUpdateSuccess = true;
             }
    }

}

//Update NE in warning pop up
function updateNodesWarning(doid) {    
    let workOID;
    let selectedNodes = [];
    let updatedNEWoid = $(`#woDetails_neMandatory`).data('updateNEDetails');
    workOID = updatedNEWoid.woid;
    selectedNodes = $(`#textAreaForNE_neMandatory`).val().split(',');

    if (selectedNodes[0] === '' && neMandateOrOptional) {
        nodesUpdateSuccess = false;
        return nodesUpdateSuccess;
    }
    else if (selectedNodes[0] === '' && !neMandateOrOptional) {
        nodesUpdateSuccess = true;
        return nodesUpdateSuccess;
    }
    else {
            // check for validate true
         if ($('#btnValidateNEAddEdit_neMandatory').hasClass(`btnEnabledForNEAddEdit`)) {
                let isNEValidated = $('#isNEValidated_neMandatory').val();
                if (!JSON.parse(isNEValidated)) {
                    nodesUpdateSuccess = false;
                    return nodesUpdateSuccess;
                }
                else {
                    nodesUpdateSuccess = true;
                    updateNE(workOID, selectedNodes, updatedNEWoid, doid);               
                    return nodesUpdateSuccess;
                }
            }
         else {
                nodesUpdateSuccess = true;
                updateNE(workOID, selectedNodes, updatedNEWoid, doid);
                return nodesUpdateSuccess;
            }
    }
}

// taken from Demand branch appended X at last
function updateNodesWarningX(doid) {

    let nodesUpdateSuccess = true;
    let validateFieldNodeName = true;
    let doID = doid;
    var nodeType = $('#select_nodeType').val();
    var selectedNodes = [];
    if ($("#woc_select_nodeName").val() != null)
        selectedNodes = $("#woc_select_nodeName").val();

    var nodesArr = [];
    var workOID = $('#woidWarning').val();
    var projectID = $('#projectIdWarning').val();
    var nodeNamesVal;
    var nodeNamesArr;
    nodeNamesVal = $('#nodeView')[0].value;
    if (nodeNamesValidatedWarning) {
        nodeNamesArr = nodeNamesVal.split(',');
        selectedNodes = selectedNodes.concat(nodeNamesArr);
    }

    if (selectedNodes != null) {
        selectedNodes = jQuery.unique(selectedNodes);
        for (var i = 0; i < selectedNodes.length; i++) {
            if (/\S/.test(selectedNodes[i]) && selectedNodes[i] != null) {
                selectedNodes[i] = selectedNodes[i].trim();
                nodesArr.push({ wOID: workOID, nodeType: nodeType, nodeNames: selectedNodes[i], createdBy: signumGlobal });
            }
        }
    }

    if (!nodesArr.length) {
        pwIsf.alert({ msg: 'Inputs are blank', type: 'error' });
        nodesUpdateSuccess = false;
        return nodesUpdateSuccess;
    }
    pwIsf.addLayer({ text: "Please wait ..." });

    if (nodeNamesVal != '') {
        if (nodeNamesValidatedWarning == false)
            validateFieldNodeName = false;
        else
            validateFieldNodeName = true;
    }
    else
        validateFieldNodeName = true;

    if (validateFieldNodeName) {

        var request = $.isf.ajax({
            url: `${service_java_URL}woExecution/updateWorkOrderNodes`,
            method: "POST",
            returnAjaxObj: true,
            contentType: C_CONTENT_TYPE_APPLICATION_JSON,
            data: JSON.stringify(nodesArr),
            dataType: "html"
        });
        request.done(function (msg) {
            nodesUpdateSuccess = true;
            if (selectedTabHref != '#assignToMe_tab_2' && selectedTabHref != '#assignedTasks_tab_3' && selectedTabHref != '#closedWorkorders_tab_4') {

                searchByDate(signumGlobal, 'all', 'all');

                //Check flowchart already opened or not
                let alreadyOpened = false;

                let getNodeNameForTooltipFormat = (passNodes) => {
                    let retStr = '';
                    let breakTooltipStr = '';
                    if (typeof selectedNodes === 'object') {
                        retStr = selectedNodes.join();
                    } else {
                        retStr = selectedNodes;
                    }
                    breakTooltipStr = breakTooltipContent(retStr, 40);
                    breakTooltipStr = breakTooltipStr || '';

                    return breakTooltipStr.toString();
                };

                $('.flow_chart_main_panel .panel-body > .flow_chart_single_box').each(function (i, flowchartBox) {

                    if ($(flowchartBox).data('details').woid == workOID) {
                        alreadyOpened = true;
                    }

                });
                if (alreadyOpened) {
                    document.getElementById("nodeFieldHeaderWO_" + workOID).innerHTML = selectedNodes;

                    $("#nodeFieldHeaderWO_" + workOID).protipSet({
                        title: getNodeNameForTooltipFormat(selectedNodes)
                    });

                }
                if (document.getElementById("nodeFieldCurrentWO_" + workOID) != null)
                    document.getElementById("nodeFieldCurrentWO_" + workOID).innerHTML = selectedNodes;

                if (document.getElementById("marketCurrentWO_" + workOID) != null)
                    document.getElementById("marketCurrentWO_" + workOID).innerHTML = msg;

                $("#toolTipCurrentWO_" + workOID).protipSet({
                    title: getNodeNameForTooltipFormat(selectedNodes)
                });

            } else {
                pwIsf.alert({ msg: 'Nodes updated !! If you want to check updated nodes in nodes column, click on duration filter again.', type: 'success' });
            }

            $("#myModalMadEwo").modal('hide');
            pwIsf.removeLayer();
        });
        request.fail(function (jqXHR, textStatus) {
            nodesUpdateSuccess = false;
            pwIsf.removeLayer();
            pwIsf.alert({ msg: "(updateNodes) Request failed: " + textStatus });
        });
    }
    else {
        pwIsf.alert({ msg: 'Comma separated nodes present please validate nodes', type: 'error' });
        pwIsf.removeLayer();
        nodesUpdateSuccess = false;
    }
    return nodesUpdateSuccess;
}

// update NE
function updateNE(workOID, selectedNodes, updatedNEWoid, doid) {
    let updatedNEdata = {};
        pwIsf.addLayer({ text: "Please wait ..." });

        if (($(`#neNameCount_neMandatory`).data('defaultNeDetails').neNames) === '') {
            updatedNEdata.doid = doid;
            updatedNEdata.count = $(`#textAreaForNECount_neMandatory`).val();
            updatedNEdata.neTextName = $(`#freeTextAreaForNE_neMandatory`).val();
            updatedNEdata.signum = signumGlobal;
            updatedNEdata.tablename = $(`#tableName_neMandatory`).val();

            nodesUpdateSuccess = updateNeUsingDoId(updatedNEdata, workOID, selectedNodes);
        }
        else if (($(`#neNameCount_neMandatory`).data('defaultNeDetails').neNames) !== '') {
            updatedNEdata.wOID = updatedNEWoid.woid;
            updatedNEdata.count = $(`#textAreaForNECount_neMandatory`).val();
            updatedNEdata.neTextName = $(`#freeTextAreaForNE_neMandatory`).val();
            updatedNEdata.signum = signumGlobal;
            updatedNEdata.tablename = $(`#tableName_neMandatory`).val();

            nodesUpdateSuccess = updateNeUsingWoId(updatedNEdata, workOID, selectedNodes);
        }
}

// update NE using DOID
function updateNeUsingDoId(updatedNEdata, workOID, selectedNodes) {
    $.isf.ajax({
        url: `${service_java_URL}woExecution/updateDeliverableOrderNodes`,
        method: "POST",
        contentType: C_CONTENT_TYPE_APPLICATION_JSON,
        data: JSON.stringify(updatedNEdata),
        dataType: "html",
        success: function (msg) {
            nodesUpdateSuccess = true;
            if (selectedTabHref != '#assignToMe_tab_2' && selectedTabHref != '#assignedTasks_tab_3' && selectedTabHref != '#closedWorkorders_tab_4') {
                searchByDate(signumGlobal, 'all', 'all');
                //Check flowchart already opened or not
                let alreadyOpened = false;
                $('.flow_chart_main_panel .panel-body > .flow_chart_single_box').each(function (i, flowchartBox) {
                    if ($(flowchartBox).data('details').woid == workOID) {
                        alreadyOpened = true;
                    }
                });
                commonBlockUploadNEUsingDoidOrWoid(workOID, alreadyOpened, selectedNodes, "");
            }
            else {
                pwIsf.alert({ msg: 'NE updated !! If you want to check updated nodes in nodes column, click on duration filter again.', type: 'success' });
            }
            $("#myModalMadEwo").modal('hide');
            pwIsf.removeLayer();
        },
        error: function (jqXHR, textStatus) {
            nodesUpdateSuccess = false;
            pwIsf.removeLayer();
            pwIsf.alert({ msg: "(updateNE) Request failed: " + textStatus });
        }
    });

    return nodesUpdateSuccess;
}    

// update NE using WOID
function updateNeUsingWoId(updatedNEdata, workOID, selectedNodes) {
    var request = $.isf.ajax({
        url: `${service_java_URL}woExecution/updateWorkOrderNodes`,
        method: "POST",
        returnAjaxObj: true,
        contentType: C_CONTENT_TYPE_APPLICATION_JSON,
        data: JSON.stringify(updatedNEdata),
        dataType: "html"
    });
    request.done(function (msg) {
        nodesUpdateSuccess = true;
        if (selectedTabHref != '#assignToMe_tab_2' && selectedTabHref != '#assignedTasks_tab_3' && selectedTabHref != '#closedWorkorders_tab_4') {
            searchByDate(signumGlobal, 'all', 'all');
            //Check flowchart already opened or not
            let alreadyOpened = false;
            $('.flow_chart_main_panel .panel-body > .flow_chart_single_box').each(function (i, flowchartBox) {
                if ($(flowchartBox).data('details').woid == workOID) {
                    alreadyOpened = true;
                }
            });
            commonBlockUploadNEUsingDoidOrWoid(workOID, alreadyOpened, selectedNodes, msg);
        }
        else {
            pwIsf.alert({ msg: 'NE updated !! If you want to check updated nodes in nodes column, click on duration filter again.', type: 'success' });
        }
        $("#myModalMadEwo").modal('hide');
        pwIsf.removeLayer();
    });
    request.fail(function (jqXHR, textStatus) {
        nodesUpdateSuccess = false;
        pwIsf.removeLayer();
        pwIsf.alert({ msg: `(updateNE) Request failed: ${textStatus}`});
    });
    
    return nodesUpdateSuccess;
}

// common block for ne upload using DOID or WOID
function commonBlockUploadNEUsingDoidOrWoid(workOID, alreadyOpened, selectedNodes, msg) {
    if (alreadyOpened) {
        document.getElementById(`nodeFieldHeaderWO_${workOID}`).innerHTML = `[<b>${shortNeNameAsNEID}:</b> - ${selectedNodes}`;
        $(`#nodeFieldHeaderWO_${workOID}`).protipSet({
            title: getNodeNameForTooltipFormat(selectedNodes)
        });
    }
    if (document.getElementById(`nodeFieldCurrentWO_${workOID}`) != null) {
        document.getElementById(`nodeFieldCurrentWO_${workOID}`).innerHTML = ` ${selectedNodes}`;
    }
    if (document.getElementById(`marketCurrentWO_${workOID}`) != null) {
        document.getElementById(`marketCurrentWO_${workOID}`).innerHTML = msg;
    }
    $(`#toolTipCurrentWO_${workOID}`).protipSet({
        title: getNodeNameForTooltipFormat(selectedNodes)
    });
}

// get the network element tooltip in desired format
function getNodeNameForTooltipFormat(passNodes) {
    let retStr = '';
    if (typeof passNodes === 'object') {
        retStr = passNodes.join();
    } else {
        retStr = passNodes;
    }
    return breakTooltipContent(retStr, 40);
}

function checkIfExecuted(workId) {
    let OK = false;
    $.isf.ajax({
        url: `${service_java_URL}woManagement/getBookingDetails/${workId}/${signumGlobal}`,
        async: false,
        success: function (data) {
            progressWorkOrderDetails = data;
            if (progressWorkOrderDetails.length != 0) {
                OK = true;
            }
            else {
                OK = false;
            }
        },
        error: function (xhr, status, statusText) {
            pwIsf.removeLayer();
            alert("Failed to Fetch Data");

        }
    })
    return OK;

}
//API Call on by default 'success' value in Delivery Status in 'Mark as Completed' modal
function getSuccessReasons(woId) {
    var successReason = "";
    $('#reasonDiv').find('option').remove().end();
    $('#isCreateSubSequentWO').prop('checked', false);


    document.getElementById("reasonDiv").style.display = "block";
    var successReason = "";
    var reasonType = "DeliverySuccess";
    pwIsf.addLayer({ text: C_PLEASE_WAIT });
    $.isf.ajax({
        type: "GET",
        url: `${service_java_URL}woExecution/getWorkOrderSuccessReasons?wOID=${woId}`,
        success: function (data) {
            successReason += "<option value=''>--Select one--</option>";
            if (data.isValidationFailed == false) {
                for (var i in data.responseData) {
                    successReason += "<option value='" + data.responseData[i].encodedSuccessReason + "'>" + data.responseData[i].encodedSuccessReason + "</option>";
                }
            }
            $('#reasonDiv').find('option').remove().end();
            $('#delReason').append(successReason);

            $("#delReason option").each(function () {
                if ($(this).text() == "Not Applicable") {
                    $(this).attr('selected', 'selected');
                    $("#commentCompleted").show();
                }
            });
        },
        complete: function (xhr, statusText) {
            pwIsf.removeLayer();
        },
        error: function (xhr, status, statusText) {
            var err = JSON.parse(xhr.responseText);
            alert(err.errorMessage);
        }
    });
    $("#delReason").append(successReason);

    testForSuccessReasonCheck(); //check for display of comment box 

}
//Condition check implemented for display of comment box based on Delivery status and Delivery Reason value.Should not display comment box even if no delivery reason is selected
function testForSuccessReasonCheck() {
    var selectedSuccessReason = $("#delReason").val(); //in case of delivery reason success,comment box should be displayed for all reasons except for no reason,but mandatory only for 'Others' reason  
    if ($("#delStatus").val() == "Success" && selectedSuccessReason !== '') {

        $("#commentCompleted").show();
        // $("#commentCompleted").prop('required', false);
    }
    if ($("#delStatus").val() == "Success") {
        if (selectedSuccessReason === '' || selectedSuccessReason == undefined) {

            $("#commentCompleted").hide();
            $('#commentCompleted').removeAttr('value');
        }
    }
    //in case of delivery reason failure, comment box should be displayed for all reasons except for no reason, but mandatory for none
    if ($("#delStatus").val() == "Failure" && selectedSuccessReason !== '') {

        $("#commentCompleted").show();
        // $("#commentCompleted").prop('required', false);
    }
    if ($("#delStatus").val() == "Failure") {
        if (selectedSuccessReason === '' || selectedSuccessReason == undefined) {

            $("#commentCompleted").hide();
            $('#commentCompleted').removeAttr('value');
        }
    }
}
function commentBoxMandatoryCheck() {
    if ($("#delStatus").val() == "Success" && ($("#delReason").val() == "Others")) {


        if (!$("#commentCompleted").val()) {
            $("#commentCompleted").css("border-bottom-color", "rgba(255, 0, 0, 0.9)");
            // pwIsf.alert({ msg: 'Please fill out the comment field.', type: 'warning' });

        }
        else if ($("#commentCompleted").val()) {
            $("#commentCompleted").css("border-bottom-color", "rgba(12, 12, 12, 0.12)");
        }
    }
}

//Function to get both local and global URLs
function getLocalGlobalURLs(modalID, projectID) {

    hideLocalGlobalURLs(modalID)
    getAllGlobalURLsWOCreation(modalID);
    getAllLocalURLsWOCreation(modalID, projectID);

}

//Funtion to get all the global URLs
function getAllGlobalURLsWOCreation(modalID) {

    let service_URL = `${service_java_URL}activityMaster/getAllGlobalUrl`;

    $.isf.ajax({
        url: service_URL,
        type: 'GET',
        success: function (data) {
            console.log("success");

        },
        complete: function (xhr, status) {
            if (status === "success") {
            configureGlobalURLDataTable(xhr.responseJSON, modalID); // Add Datatable configuration to the table    
            }
        },
        error: function (data) {
            console.log('error');
        }
    });



    let createglobalURLTableWOCreation = (getData, modalID) => {
        let thead = `<thead>
                        <tr>
                            <th>URL Name</th>
                            <th>URL</th>
                        </tr>
                    </thead>`;

        let tfoot = ``;

        let tbody = ``;

        $(getData).each(function (i, d) {
            tbody += `<tr>
                            <td>${d.urlName}</td>
                            <td>${d.urlLink}</td>
                        </tr>`;
        });

        $('#' + modalID + ' .globalURLTableWOCreation').html(thead + '<tbody>' + tbody + '</tbody>' + tfoot);

    };

    let configureGlobalURLDataTable = (getData, modalID) => {
        if ($.fn.dataTable.isDataTable('#' + modalID + ' .globalURLTableWOCreation')) {
            $('#' + modalID + ' .globalURLTableWOCreation').DataTable().destroy();
            $('#' + modalID + ' .globalURLTableAreaWOCreation').html('<table class="table table-bordered table-striped table-hover globalURLTableWOCreation"></table >');
        }
        createglobalURLTableWOCreation(getData, modalID); //Create the Table for Global URLs dynamically
        let GlobalURLDataTable = $('#' + modalID + ' .globalURLTableWOCreation').DataTable({
            searching: true,
            responsive: true,
            retrieve: true,
            destroy: true,
            //"lengthMenu": [10, 15, 20, 25],
            pageLength: 5,
            lengthChange: false,
            'info': false,
            'columnDefs': [
                {
                    'searching': true,
                    'targets': [0, 1]
                }
            ]
        });

    }

}

//Function to get all the local URLs
function getAllLocalURLsWOCreation(modalID, projectID) {

    if (projectID) {
        let service_URL = service_java_URL + 'activityMaster/getAllLocalUrl?projectID=' + projectID;
        $.isf.ajax({
            url: service_URL,
            type: 'GET',
            success: function (data) {
                console.log("success");

            },
            complete: function (xhr, status) {
                if (status == "success")   
                        configureLocalURLDataTable(xhr.responseJSON.responseData, modalID); // Add Datatable configuration to the table                 
            },
            error: function (data) {
                console.log('error');
            }
        });



        let createLocalURLTable = (getData, modalID) => {
            let thead = `<thead>
                        <tr>
                            <th>URL Name</th>
                            <th>URL</th>
                        </tr>
                    </thead>`;

            let tfoot = ``;

            let tbody = ``;

            $(getData).each(function (i, d) {
                if (d.localUrlStatus) {
                    tbody += `<tr>
                            <td>${d.localUrlName}</td>
                            <td>${d.localUrlLink}</td>
                        </tr>`;
                }
            });

            $('#' + modalID + ' .localURLTable').html(thead + '<tbody>' + tbody + '</tbody>' + tfoot);

        };

        let configureLocalURLDataTable = (getData, modalID) => {

            if ($.fn.dataTable.isDataTable('#' + modalID + ' .localURLTable')) {
                $('#' + modalID + ' .localURLTable').DataTable().destroy();
                $('#' + modalID + ' .localURLTableArea').html('<table class="table table-bordered table-striped table-hover localURLTable"></table >');
            }

            createLocalURLTable(getData, modalID); //Create the Table for Global URLs dynamically

            let LocalURLDataTable = $('#' + modalID + ' .localURLTable').DataTable({
                searching: true,
                responsive: true,
                retrieve: true,
                destroy: true,
                //"lengthMenu": [10, 15, 20, 25],
                pageLength: 5,
                lengthChange: false,
                'info': false,
                'columnDefs': [
                    {
                        'searching': true,
                        'targets': [0, 1]
                    }
                ]
            });

        }
    }

    else {
        pwIsf.alert({ msg: 'Project ID Missing', type: 'error' });
    }

}

//Show Local Global URL table div
function showLocalGlobalURLs(modalID) {

    $('#' + modalID + ' .localGlobalURLTables').show();
    $('#' + modalID + ' .hideLocalGlobal').show();
    $('#' + modalID + ' .showLocalGlobal').hide();

}

//Hide Local Global URL table div
function hideLocalGlobalURLs(modalID) {

    $('#' + modalID + ' .localGlobalURLTables').hide();
    $('#' + modalID + ' .hideLocalGlobal').hide();
    $('#' + modalID + ' .showLocalGlobal').show();

}
function formatDate(date) {
    var hours = date.getHours();
    var minutes = date.getMinutes();
    var seconds = date.getSeconds();
    var ampm = hours >= 12 ? 'pm' : 'am';
    hours = hours % 12;
    hours = hours ? hours : 12; // the hour '0' should be '12'
    minutes = minutes < 10 ? '0' + minutes : minutes;
    var strTime = hours + ':' + minutes + ':' + seconds + ' ' + ampm;
    return (date.getMonth() + 1) + "/" + date.getDate() + "/" + date.getFullYear() + "  " + strTime;
}
function getCurTabClosingReqValue() {
    var isCurTabClosingReq = false;
    var woID = parseInt(getUrlParams()['woID']);
    var tabID = parseInt(getUrlParams()['tabID']);
    var allWoTabDetail = JSON.parse(localStorage.getItem("WoTabDetails")) || [];
    var curWotabDetails = getCurWoTabDetails(woID);
    var curWoCount = curWotabDetails.length;
    if (curWoCount > 1) {
        var curKey = getCurWoTabKey(woID, tabID);
        var curWoMaxKey = getMaxKeyForCurTabWo(woID);
        if (curKey < curWoMaxKey) {
            isCurTabClosingReq = true;
            removeEntryForCurTabWorkOrder(tabID);
        }
    }
    return isCurTabClosingReq;

}
// return key of current tab .
function getCurWoTabKey(woID, tabID) {
    var curKey;
    var curWoDetails = getCurWoTabDetails(woID);
    var curTabDetails = curWoDetails.filter(item => item.RequestTabid == tabID);
    if (curTabDetails) {
        curKey = curTabDetails.map(x => x.Key)[0];
    }
    return curKey;
}
// return array of work order details.
function getCurWoTabDetails(woID) {
    var tabWoDetails = [];

    tabWoDetails = JSON.parse(localStorage.getItem("WoTabDetails")) || [];
    return tabWoDetails.filter(item => item.Woid == woID);
}
//create entries to local storage  WoTabDetails.
function createEntryForCurTabWorkOrder(woID, tabID) {
    // executes when complete page is fully loaded, including all frames, objects and images

    if (woID) {
        if (localStorage.getItem("WoTabDetails") === null) {
            var localdata = [{
                Key: parseInt(1),
                Woid: parseInt(woID),
                CreatedTime: formatDate(new Date()),
                Url: window.location.href,
                RequestTabid: parseInt(tabID)

            }];
            localStorage.setItem("WoTabDetails", JSON.stringify(localdata));
        };

        var allWoDetails = JSON.parse(localStorage.getItem('WoTabDetails')) || [];

        var maxKey = Math.max.apply(Math, allWoDetails.map(function (o) { return o.Key; }));
        maxKey = parseInt(maxKey) + 1;
        var appendData = {
            Key: maxKey,
            Woid: parseInt(woID),
            CreatedTime: formatDate(new Date()),
            Url: window.location.href,
            RequestTabid: parseInt(tabID)

        };
        var isRefreshWindow = [];
        isRefreshWindow = allWoDetails.filter(item => item.RequestTabid == tabID);
        if (isRefreshWindow.length == 0) {
            allWoDetails.push(appendData);
            localStorage.setItem("WoTabDetails", JSON.stringify(allWoDetails));
        }

    }
}
// remove current tab work order id
function removeEntryForCurTabWorkOrder(tabID) {

    var allTtabWoDetails = JSON.parse(localStorage.getItem("WoTabDetails")) || [];
    allTtabWoDetails.splice(allTtabWoDetails.findIndex(item => item.RequestTabid == tabID), 1);
    localStorage.setItem("WoTabDetails", JSON.stringify(allTtabWoDetails));
}
// return max key for curent wo
function getMaxKeyForCurTabWo(woID) {
    var woDetails = getCurWoTabDetails(woID);
    var maxKey = Math.max.apply(Math, woDetails.map(function (o) { return o.Key; }));
    return maxKey;
}

function deliveryTimerIncrement() {
    var isCurTabClosingReq = getCurTabClosingReqValue();
    if (isCurTabClosingReq == true) {
        window.close();
    }
}

function checkifshouldclose(url, storeUrl) {
    if (url != storeUrl) {
        return true;
    } else {
        return false;
    }
}

var blinkTab = function (message) {
    var oldTitle = document.title,
        timeoutId,
        blink = function () {
            document.title = document.title === message ? ' ' : message;
        },
        clear = function () {
            clearInterval(timeoutId);
            document.title = oldTitle;
            window.onmousemove = null;
            timeoutId = null;
        };
    if (!timeoutId) {
        timeoutId = setInterval(blink, 1000);
        window.onmousemove = clear;
    }
};

//MARK AS COMPLETED
$(document).on("click", ".view-completed", function () {
    let woId = $(this).data('woid');
    validateAndCompleteWorkOrder(woId);
});

function escapeJsonHtml(text) {
    if (text) {
        return text
            .replace(/&/g, "&amp;")
            .replace(/</g, "&lt;")
            .replace(/>/g, "&gt;")
            .replace(/"/g, "&quot;")
            .replace(/'/g, "&#039;");
    }
    return '';
}
