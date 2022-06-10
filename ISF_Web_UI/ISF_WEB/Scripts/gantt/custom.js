var start = getUrlVars()["start"].toString();
var end = getUrlVars()["end"].toString();
var status = getUrlVars()["status"].toString();
var scope = getUrlVars()["scope"].toString();
var signumId = signumGlobal;
var projID = getUrlVars()["projID"].toString();
var assignedTo = getUrlVars()["assignedTo"].toString();
var neid = getUrlVars()["neid"].toString();
var assignedBy = getUrlVars()["assignedBy"].toString();
//Get value of multiple workorders
var woList = getUrlVars()["workOrders"].toString().split(",");
var woLength = woList.length;
var workOrders = [];

var assignedCount = 0, inprogressCount = 0, onholdCount = 0, openCount = 0,
    closedCount = 0, deferredCount = 0, plannedCount = 0, rejectedCount = 0;

for (var i = 0; i < woLength - 1; i++) {
    workOrders.push(woList[i].split("/")[0]);
}

if (scope == "") {
    scope = 'all';
} else {
    scope = "'" + scope + "'"
}

if (assignedTo == "") {
    assignedTo = 'all';
} else {
    assignedTo = "'" + assignedTo + "'";
}

if (assignedBy == "") {
    assignedBy = 'all';
} else {
    assignedBy = "'" + assignedBy + "'";
}

if (workOrders == "") {
    workOrders = 'all';
} else {
    workOrders = "'" + workOrders + "'";
}

if (neid == "") {
    neid = 'all';
}
const ServiceUrl = service_java_URL + "woManagement/getWorkOrderViewDetails/" + projID + "/" + scope + "/all/" + workOrders
    + "/" + start + "/" + end + "/" + assignedTo + "/'" + signumId + "'/1/1/" + status + "/" + neid + "/" + assignedBy + "/";

const ServiceUrl2 = service_java_URL + "woManagement/getWorkOrderViewDetails/" + projID + "/" + scope + "/all/" + workOrders
    + "/" + start + "/" + end + "/" + assignedTo + "/'" + signumId + "'/1/1/all/" + neid + "/" + assignedBy + "/"; 

console.log(ServiceUrl);

var priority = getUrlVars()["priority"].toString();

//====================================Global Variables====================================
//========================================================================================

var iframeList = [];

var projectList = [];
var projectList1 = [];
var activityList = [];
var subActvityList = [];
var ActivityList = [];
var WorkOrderList = [];

var hourlyDataArray = [];
var DailyActivityData = [];

var hourlyActivityDataModel = {};

//========================================================================================
//========================================================================================

//====================================Hourly Activity Data================================
//========================================================================================

function getHourlyActivityData(gantt) {
    $(".gantt_data_area").append('<img id="loaddata" src="' + UiRootDir
        + '/Content/images/loadingGantt.gif" style=width="100px"; height="100px"; >');

    var serviceCall2 = $.isf.ajax({
        url: ServiceUrl2,
        returnAjaxObj: true,
        context: this,
        crossdomain: true,
        dataType: 'json',
        contentType: 'text/plain',
        type: 'GET',
        xhrFields: {
            withCredentials: false
        },
        success: function (data) {
            getCount(data);
            ConvertTimeZonesDT(data, getWorkOrderViewDetails_tzColumns);
        },
        error: function (xhr, status, statusText) {
            pwIsf.removeLayer();
        }
    })

    var serviceCall = $.isf.ajax({
        url: ServiceUrl,
        returnAjaxObj: true,
        context: this,
        crossdomain: true,
        dataType: 'json',
        contentType: 'text/plain',
        type: 'GET',
        xhrFields: {
            withCredentials: false
        },
        success: function (data) {
            ConvertTimeZonesDT(data, getWorkOrderViewDetails_tzColumns);
            $("#divPanelResult").css({ "visibility": "visible" });
            if (data.length === 0) {
                alert("No Data for selected filters");
            } else {
                processData(data, gantt);
            }
            $("#loaddata").hide();
            window.parent.$("#frame").prop('scrolling', 'no');
            pwIsf.removeLayer();
        },
        error: function (xhr, status, statusText) {
            pwIsf.removeLayer();
        }
    })
    serviceCall2.done(function () {
        //Empty block
    });
    serviceCall.done(function () {
        //Empty block
    });


}

function getCount(data) {

    const flagP = priority === "All" ? true : false;

    for (const item of data) {
        for (const scopeData of item.listOfScope) {
            for (const activity of scopeData.listOfActivities) {
                for (const workOrder of activity.listOfWorkOrder) {
                    if ((!WorkOrderList.includes(workOrder.wOID)) && ((!flagP && workOrder.priority.toString().toLowerCase() === priority) || flagP)) {
                        if (workOrder.nodeNames === null || workOrder.nodeNames === undefined || workOrder.nodeNames === ",") {
                            workOrder.nodeNames = "-";
                        }
                        else {
                            workOrder.nodeNames = workOrder.nodeNames.slice(0, -1);
                        }

                        if (workOrder.status.toLowerCase() === "assigned") {
                            assignedCount++;
                        }
                        else if (workOrder.status.toLowerCase() === "inprogress") {
                            inprogressCount++;
                        }
                        else if (workOrder.status.toLowerCase() === "open") {
                            openCount++
                        }
                        else if (workOrder.status.toLowerCase() === "closed") {
                            closedCount++;
                        }
                        else if (workOrder.status.toLowerCase() === "onhold") {
                            onholdCount++;
                        }
                        else if (workOrder.status.toLowerCase() === "deferred") {
                            deferredCount++;
                        }
                        else if (workOrder.status.toLowerCase() === "planned") {
                            plannedCount++;
                        }
                        else if (workOrder.status.toLowerCase() === "rejected") {
                            rejectedCount++;
                        }
                    }
                }
            }
        }
    }

    window.parent.$("label#assigned")[0].textContent = "Assigned- " + assignedCount;
    window.parent.$("label#inprogress")[0].textContent = "InProgress- " + inprogressCount;
    window.parent.$("label#closed")[0].textContent = "Closed- " + closedCount;
    window.parent.$("label#onhold")[0].textContent = "OnHold- " + onholdCount;
    window.parent.$("label#deferred")[0].textContent = "Deferred- " + deferredCount;
    window.parent.$("label#planned")[0].textContent = "Planned- " + plannedCount;
    window.parent.$("label#rejected")[0].textContent = "Rejected- " + rejectedCount;
}

function processData(data, gantt) {

    const flagP = priority === "All" ? true : false;

    if (status === "all") {
        assignedCount = 0;
        inprogressCount = 0;
        openCount = 0;
        closedCount = 0;
        onholdCount = 0;
        plannedCount = 0;
        rejectedCount = 0;
    }
    else {
        switch (status) {
            case "Assigned": assignedCount = 0;
                break;
            case "InProgress": inprogressCount = 0;
                break;
            case "Closed": closedCount = 0;
                break;
            case "OnHold": onholdCount = 0;
                break;
            case "Deferred": deferredCount = 0;
                break;
            case "Planned": plannedCount = 0;
                break;
            case "Rejected": rejectedCount = 0;
                break;
        }
    }
      
    for (const item of data) {
        var projectID = item.projectID;
        if (!isInArray(projectID, projectList)) {
            const DataObjProject = new Object();
            projectList.push(projectID);
            DataObjProject.id = projectID;
            DataObjProject.text = item.projectName.toString();
            DataObjProject.start_date = convertStringToDate(item.project_StartDate.toString()).toString();//missing
            DataObjProject.duration = item.projectHours.toString();//missing
            DataObjProject.open = true;
            DataObjProject.resource = "";
            DataObjProject.wocount = item.projectWOCount.toString();//missing
            DataObjProject.resType = "";
            DataObjProject.status = "";
            DataObjProject.nodes = "";
            DataObjProject.createdby = "";
            DataObjProject.itemType = "project";
            DailyActivityData.push(DataObjProject);
            createModels("project", item);
        }

        for (const scopeData of item.listOfScope) {
            for (const activity of scopeData.listOfActivities) {
                var subActID = projectID + "_" + activity.workFlowName;
                if (!isInArray(subActID, ActivityList)) {
                    const DataObjectActivity = new Object();
                    ActivityList.push(subActID);
                    DataObjectActivity.id = subActID.toString();
                    DataObjectActivity.text = activity.workFlowName.toString();
                    DataObjectActivity.start_date = convertStringToDate(activity.subActivityStartDate.toString()).toString();//missing
                    DataObjectActivity.duration = activity.subActivityHours.toString();//missing
                    DataObjectActivity.progress = "1.0";
                    DataObjectActivity.open = true;
                    DataObjectActivity.resource = "";
                    DataObjectActivity.wocount = activity.subActivityWOCount.toString();//missing
                    DataObjectActivity.resType = "";
                    DataObjectActivity.status = "";
                    DataObjectActivity.nodes = "";
                    DataObjectActivity.createdby = "";
                    DataObjectActivity.itemType = "activity";
                    DataObjectActivity.parent = data[i].projectID.toString();
                    DailyActivityData.push(DataObjectActivity);
                }
                for (const workOrder of activity.listOfWorkOrder) {
                    if (workOrder.nodeNames === null || workOrder.nodeNames === undefined || workOrder.nodeNames === ",") {
                        workOrder.nodeNames = "-";
                    }
                    else {
                        workOrder.nodeNames = workOrder.nodeNames.slice(0, -1);
                    }
                    if ((!isInArray(workOrder.wOID, WorkOrderList)) && ((!flagP && workOrder.priority.toString().toLowerCase() === priority) || flagP)) {
                        const DataObjectWorkOrder = new Object();
                        WorkOrderList.push(workOrder.wOID);
                        DataObjectWorkOrder.id = subActID + "_" + workOrder.wOID.toString();
                        DataObjectWorkOrder.text = " " + workOrder.wOID.toString() + "_" + activity.workFlowName.toString();
                        DataObjectWorkOrder.start_date = convertStringToDate(workOrder.startDate.toString()).toString();
                        DataObjectWorkOrder.duration = Math.round(workOrder.woHours).toString();
                        DataObjectWorkOrder.parent = subActID.toString();
                        DataObjectWorkOrder.progress = "1.0";
                        DataObjectWorkOrder.open = true;
                        if (workOrder.assignedTo == null) {
                            DataObjectWorkOrder.resource = "";
                        } else {
                            DataObjectWorkOrder.resource = workOrder.assignedTo.toString();
                        }
                        if (workOrder.createdby == null) {
                            DataObjectWorkOrder.createdby = "";
                        } else {
                            DataObjectWorkOrder.createdby = workOrder.createdby.toString();
                        }
                        DataObjectWorkOrder.wocount = "0";
                        DataObjectWorkOrder.itemType = "subactivity";
                        DataObjectWorkOrder.status = workOrder.status.toString();
                        DataObjectWorkOrder.proficiencyId = workOrder.proficiencyID;
                        DataObjectWorkOrder.proficiencyName = workOrder.proficiencyName;
                        if (workOrder.status.toLowerCase() === "assigned") {
                            assignedCount++;
                        }
                        else if (workOrder.status.toLowerCase() === "inprogress") {
                            inprogressCount++;
                        }
                        else if (workOrder.status.toLowerCase() === "open") {
                            openCount++;
                        }
                        else if (workOrder.status.toLowerCase() === "closed") {
                            closedCount++;
                        }
                        else if (workOrder.status.toLowerCase() === "onhold") {
                            onholdCount++;
                        }
                        else if (workOrder.status.toLowerCase() === "deferred") {
                            deferredCount++;
                        }
                        else if (workOrder.status.toLowerCase() === "planned") {
                            plannedCount++;
                        }
                        else if (workOrder.status.toLowerCase() === "rejected") {
                            rejectedCount++;
                        }

                        DataObjectWorkOrder.sid = "24671";//missing
                        DataObjectWorkOrder.projectID = data[i].projectID.toString();
                        DataObjectWorkOrder.subActivityName = activity.subActivityName.toString();
                        DataObjectWorkOrder.digitalHours = "0.0 seconds";//missing
                        DataObjectWorkOrder.manualHours = workOrder.woHours.toString();//missing
                        DataObjectWorkOrder.totalHours = workOrder.woHours.toString();//missing
                        DataObjectWorkOrder.startedOn = "No Booking Done";//missing
                        DataObjectWorkOrder.closedOn = "No Booking Done";//missing
                        DataObjectWorkOrder.resType = "partial";//missing
                        DataObjectWorkOrder.nodes = workOrder.nodeNames;
                        DailyActivityData.push(DataObjectWorkOrder);
                        createModels("wo", DataObjectWorkOrder);
                    }
                }
            }
        }
    }
    hourlyActivityDataModel.data = DailyActivityData;
    hourlyActivityDataModel.links = [];
    gantt.templates.tooltip_text = function (start, end, task) {
        return DailyActivityData.resType;
    };
    gantt.parse(hourlyActivityDataModel);

    switch (status) {
        case "Assigned": window.parent.$("label#assigned")[0].textContent = "Assigned- " + assignedCount;
            break;
        case "InProgress": window.parent.$("label#inprogress")[0].textContent = "InProgress- " + inprogressCount;
            break;
        case "Closed": window.parent.$("label#closed")[0].textContent = "Closed- " + closedCount;
            break;
        case "Deferred": window.parent.$("label#deferred")[0].textContent = "Deferred- " + deferredCount;
            break;
        case "Planned": window.parent.$("label#planned")[0].textContent = "Planned- " + plannedCount;
            break;
        case "Rejected": window.parent.$("label#rejected")[0].textContent = "Rejected- " + rejectedCount;
            break;
        case "all":

            window.parent.$("label#assigned")[0].textContent = "Assigned- " + assignedCount;
            window.parent.$("label#inprogress")[0].textContent = "InProgress- " + inprogressCount;
            window.parent.$("label#closed")[0].textContent = "Closed- " + closedCount;
            window.parent.$("label#onhold")[0].textContent = "OnHold- " + onholdCount;
            window.parent.$("label#deferred")[0].textContent = "Deferred- " + deferredCount;
            window.parent.$("label#planned")[0].textContent = "Planned- " + plannedCount;
            window.parent.$("label#rejected")[0].textContent = "Rejected- " + rejectedCount;
            break;
    }

}

//========================================================================================
//========================================================================================

//====================================Model Creation======================================
//========================================================================================

function showModel(ID) {
    $('#' + ID).modal({
        backdrop: 'static'
    })
    $('#' + ID).modal('show');
}

function showWOModel(ID, sid, projectID) {
    var iframeHTML = '';
    var link = UiRootDir+'/DeliveryExecution/FlowChart?mode=view&woID=' + ID;

    iframeHTML = '<iframe src="' + link + '" width="100%" height="400" frameborder="0"></iframe>';
    $('#' + projectID + '_' + sid + '_' + ID + '_body').html(iframeHTML);

    $('#' + projectID + '_' + sid + '_' + ID).modal({
        backdrop: 'static'
    })
    $('#' + projectID + '_' + sid + '_' + ID).modal('show');
}

function createModels(type, dataObj) {
    var modelHeaderPreFix = '';

    var modelHeaderStr = '';
    var modelBodyStr = '';
    var modelFooter = '</div></div></div>';

    var startedOn = '';
    var closedOn = '';
    let modelHTML = '';
    if (type === "project") {
        modelHeaderPreFix = '<div id="' + dataObj.projectID.toString() + '" class="modal fade" tabindex="-1" role="dialog"><div class="modal-dialog2" role="document">'
            + '<div class="modal-content"><div class="modal-header"><button type="button" class="close" data-dismiss="modal" aria-label="Close">'
            + '<span aria-hidden="true">&times;</span></button>';

        modelHeaderStr = '<h4 class="modal-title">' + dataObj.projectName + '</h4></div>';

        modelBodyStr = '<div class="modal-body"><div class="row">'
            + '<div class="col-md-6"><strong>Market Area: </strong>' + dataObj.marketArea + '</div>'
            + '<div class="col-md-6"><strong>Country: </strong>' + dataObj.country + '</div>'
            + '<div class="col-md-6"><strong>Operator: </strong>' + dataObj.operator + '</div>'
            + '<div class="col-md-6"><strong>Product Area: </strong>' + dataObj.productArea + '</div>'
            + '<div class="col-md-12"><strong>Service Area: </strong>' + dataObj.serviceArea + '</div>'
            + '<div class="col-md-6"><strong>Start Date: </strong>' + dataObj.project_StartDate + '</div>'
            + '<div class="col-md-6"><strong>End Date: </strong>' + dataObj.project_EndDate + '</div>'
            + '</div></div>';

        modelHTML = modelHeaderPreFix + modelHeaderStr + modelBodyStr + modelFooter;
        $("#modelContainer").append(modelHTML);

    }
    else if (type === "wo") {

        if (dataObj.startedOn.toString().indexOf('1900') > -1) {
            //Empty block
        }
        else {
            startedOn = dataObj.startedOn.toString();
        }

        if (dataObj.closedOn.toString().indexOf('1900') > -1) {
            closedOn = "";
        }
        else {
            closedOn = dataObj.closedOn.toString();
        }

        modelHeaderPreFix = '<div id="' + dataObj.id.toString() + '" class="modal fade" tabindex="-1" role="dialog">'
            + '<div class="modal-dialog2" role="document" style="width: 90%;">'
            + '<div class="modal-content"><div class="modal-header"><button type="button" class="close" data-dismiss="modal" aria-label="Close">'
            + '<span aria-hidden="true">&times;</span></button>';

        modelHeaderStr = '<h4 class="modal-title">' + dataObj.id.toString() + "_" + dataObj.subActivityName + '</h4>'
            + '<br/>'
            + '<div class="row" style="font-size: 12px;">'
            + '<div class="col-md-3"><strong>Node Type: </strong>' + dataObj.nodeType + '</div>'
            + '<div class="col-md-3"><strong>Node Count: </strong>' + dataObj.nodeCount + '</div>'
            + '<div class="col-md-3"><strong>Node Name: </strong>' + dataObj.nodeNames + '</div>'
            + '<div class="col-md-3"><strong>Started On: </strong>' + startedOn + '</div>'
            + '<div class="col-md-3"><strong>Closed On: </strong>' + closedOn + '</div>'
            + '<div class="col-md-3"><strong>Digital Hours: </strong>' + dataObj.digitalHours + '</div>'
            + '<div class="col-md-3"><strong>Manual Hours: </strong>' + dataObj.manualHours + '</div>'
            + '<div class="col-md-3"><strong>Total Hours: </strong>' + dataObj.totalHours + '</div>'


            + '</div></div>'

        modelBodyStr = '<div class="modal-body"><div class="row" align = "center"><div id="' + dataObj.id.toString() + '_body">'
            + '</div></div>';

        modelHTML = modelHeaderPreFix + modelHeaderStr + modelBodyStr + modelFooter;
        $("#modelContainer").append(modelHTML);
    }
}

//========================================================================================
//========================================================================================

//====================================Utility Functions===================================
//========================================================================================

function isInArray(value, array) {
    return array.indexOf(value) > -1;
}

function convertStringToDate(strDate) {
    var date = strDate.split(" ");
    var d = date[0].split("-");
    const dat = d[2] + "-" + d[1] + "-" + d[0] + " " + date[1];
    return dat;
}

function _convertStringToDate(strDate) {
    var date = new Date(strDate),
        mnth = ("0" + (date.getMonth() + 1)).slice(-2),
        day = ("0" + date.getDate()).slice(-2);
    return [day, mnth, date.getFullYear()].join("-") + " " + convertStringToTime(strDate);
}


function convertStringToTime(strDateTime) {
    var dateTime = strDateTime.split(" ");
    return dateTime[3].toString();
}
//========================================================================================
//========================================================================================


function _zooming(z) {
    switch (z) {
        case "Hours": {
            location.href = "activityHourly";
            break;
        }
        case "Days": {
            location.href = "activityDaily";
            break;
        }
        case "Months": {
            location.href = "activityMonthly";
            break;
        }
    }
}

function getUrlVars() {
    var vars = [], hash;
    var hashes = window.location.href.slice(window.location.href.indexOf('?') + 1).split('&');

    for (const hashItem of hashes) {
        hash = hashItem.split('=');
        vars.push(hash[0]);
        vars[hash[0]] = hash[1];
    }
    return vars;
}

function flowChartOpenInNewWindow(url) {
    var width = window.innerWidth * 0.95;
    // define the height in
    var height = width * window.innerHeight / window.innerWidth;
    // Ratio the hight to the width as the user screen ratio
    window.open(url + '&commentCategory=WO_DT_WO_LEVEL', 'newwindow', 'width=' + width + ', height=' + height
        + ', top=' + ((window.innerHeight - height) / 2) + ', left=' + ((window.innerWidth - width) / 2));
}