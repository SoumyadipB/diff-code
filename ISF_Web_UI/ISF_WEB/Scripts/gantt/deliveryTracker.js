var legendsForGantt = '<div class="col-md-6">' +
        '<span style="margin-right: 3px ;padding: 0px 5px 0px 5px;background-color:#c7c7c7;"></span>Open&nbsp;&nbsp;&nbsp;'+
            '<span style=" margin-right: 3px ;padding: 0px 5px 0px 5px;background-color:#0677ad;"></span>Assigned&nbsp;&nbsp;&nbsp;' +
            '<span style="margin-right: 3px ;padding: 0px 5px 0px 5px;background-color:#ffec2c;"></span>In-Progress&nbsp;&nbsp;&nbsp;' +
            '<span style="margin-right: 3px ;padding: 0px 5px 0px 5px;background-color:#00983b;"></span>Closed&nbsp;&nbsp;&nbsp;' +
            '<span style="margin-right: 3px ;padding: 0px 5px 0px 5px;background-color:#ffad4d;"></span>On-Hold&nbsp;&nbsp;&nbsp;' +
            '<span style="margin-right: 3px ;padding: 0px 5px 0px 5px;background-color:#fb0606;"></span>Deferred&nbsp;&nbsp;&nbsp;' +
    '</div>';

//Constants: Element Ids/Classes for jQuery
const scopeListId = "#scopeList";
const AssignedToId = "#AssignedTo";
const AssignedById = "#AssignedBy";
const selectAllWOId = "#selectAllWO";
const durationButtonsId = "#durationButtons";
const removeActiveClass = ".removeActive";

//Constants: Common string literals
const UI_COMPLETE_LOADING = "ui-autocomplete-loading";

//Constants: Element Ids/Classes for plain javascript
const ELEMENT_NAME_ZOOMING = "Zooming";

function clearFields() {

    $("#projList").val("");
    $("#projList").trigger('change');

    $(scopeListId).find('option').not(':first').remove();
    $(scopeListId).val("");
    $(scopeListId).trigger('change');

    $("#neidList").val("");
    $("#neidList").trigger('change');

    $(AssignedToId).val("");
    $(AssignedToId).trigger('change');

    $(AssignedById).val("");
    $(AssignedById).trigger('change');

    $(selectAllWOId).val("");
    $(selectAllWOId).trigger('change');

    $("#dtStart_Date").val("");
    $("#dtEnd_Date").val("");
}

function goBack() {
    window.location.replace("./dashboard");
}

var cumulativeFlg;


/*--------------Date in YYYY-MM-DD Format-----------------------------*/

var formatted_date = function (date) {
    var m = ("0" + (date.getMonth() + 1)).slice(-2); // in javascript month start from 0.
    var d = ("0" + date.getDate()).slice(-2); // add leading zero 
    var y = date.getFullYear();
    return y + '-' + m + '-' + d;
}

var startDate = "";
var endDate = "";
var dtStatus = "";
var durationText = "";
var statusText = "";

$(document).ready(function () {
    var urlParams = new URLSearchParams(window.location.search);
    var ProjectID = urlParams.get("ProjectId");
    var TimeSelector = urlParams.get("TimePeriod");    
    cumulativeFlg = urlParams.get("cumulativeFlg");
    var ProjectName = urlParams.get("ProjName");
    var today = new Date();

    $("#dtStart_Date").datepicker({
        dateFormat: "yy-mm-dd",
        maxDate: new Date(new Date().setFullYear(new Date().getFullYear() + 1)),
        minDate: new Date(new Date().setFullYear(new Date().getFullYear() - 1))
        

    });
    $("#dtEnd_Date").datepicker({
        dateFormat: "yy-mm-dd",
        maxDate: new Date(new Date().setFullYear(new Date().getFullYear() + 1)),
        minDate: new Date(new Date().setFullYear(new Date().getFullYear() - 1))


    });
    const checkIfProjectSelected = function (e) {
        var project = $("#projList").val();
        if (project === null || project === undefined || project.length === 0) {
            pwIsf.alert({ msg: "Please select a project first!", type: "warning" });
        }
    }
    
    $(scopeListId).on("select2:open", checkIfProjectSelected);
    $("#neidList").click(checkIfProjectSelected);
   
    $.isf.ajax({
        url: service_java_URL + "woManagement/getProjectBySignum/" + signumGlobal,
        async:false,
        success: function (data) {
            if (!data.isValidationFailed) {
                $.each(data.responseData, function (i, d) {
                    $('#projList').append('<option value="' + d.projectID + '">' + d.projectID + '-' + d.projectName + '</option>');

                })
            }
        },
        error: function (xhr, status, statusText) {
            console.log('An error occurred on getAllProjects: ' + xhr.error);
        }

    });
    getSignum();
    getNEID();
    //Get Work Orders without any filters applied
    getWorkOrders();
    if (ProjectID !== null && ProjectID !== undefined) {
        getDurationDT(TimeSelector, ProjectID);
        generateGantt(ProjectID, ProjectName);
    }
});

function getSignumAutoComplete(request, response, elementId) {
    $.isf.ajax({
        url: service_java_URL + "activityMaster/getEmployeesByFilter",
        type: "POST",
        data: {
            term: request.term
        },
        success: function (data) {
            $(elementId).autocomplete().addClass(UI_COMPLETE_LOADING);
            var result = [];
            $.each(data, function (i, d) {
                result.push({
                    "label": d.signum + "/" + d.employeeName,
                    "value": d.signum

                });


            })
            response(result);
            $(elementId).autocomplete().removeClass(UI_COMPLETE_LOADING);

        }
    });
}

function getSignum() {
    $(AssignedToId).autocomplete({
        source: function (request, response) {
            getSignumAutoComplete(request, response, AssignedToId);
        },
        minLength: 3       
    }); 
    $(AssignedToId).autocomplete("widget").addClass("fixedHeight");

    $(AssignedById).autocomplete({
        source: function (request, response) {
            getSignumAutoComplete(request, response, AssignedById);
        },
        minLength: 3       
    });
    $(AssignedById).autocomplete("widget").addClass("fixedHeight");
}

function getNEID() {
    const ProjectID =  $('#projList').val();
    $("#neidList").autocomplete({
        
        source: function (request, response) {
            $.isf.ajax({
                 
                url: service_java_URL + "woManagement/getNEIDByProjectID?projectID=" + ProjectID ,
                data: {
                    term: request.term
                },
                success: function (data) {
                    $("#neidList").autocomplete().addClass(UI_COMPLETE_LOADING);                    
                    var result = [];
                    $.each(data, function (i, d) {
                        result.push({
                            "label": d.Name,
                            "value": d.Name

                        });


                    })
                    response(result);
                    $("#neidList").autocomplete().removeClass(UI_COMPLETE_LOADING);

                }
            });
        },
        minLength: 3        
    });
    $("#neidList").autocomplete("widget").addClass("fixedHeight");
    
}
//Autocomplete Multiple WorkOrders (with and without filters)
function getWorkOrders() {
    let ProjectID = $('#projList').val();
    let ProjectScopeID = $(scopeListId).val();
    let AssignedTo = $(AssignedToId).val();
    let AssignedBy = $(AssignedById).val();
    let NodeName = $('#neidList').val();
    if (ProjectID === "") {
        ProjectID = 'all'
    }
    if (ProjectScopeID === "" || ProjectScopeID === null || ProjectScopeID === undefined) {
        ProjectScopeID = '0'
    }
    if (AssignedTo === "") {
        AssignedTo = 'all'
    }
    if (AssignedBy === "") {
        AssignedBy = 'all'
    }
    if (AssignedTo === "") {
        AssignedTo = 'all'
    }
    if (NodeName === "") {
        NodeName = 'all'
    }
    var serviceUrl = service_java_URL + "woManagement/getWorkOrdersByProjectID?projectID=" + ProjectID + "&projectScopeID=" + ProjectScopeID
        + "&assignedTo=" + AssignedTo + "&assignedBy=" + AssignedBy + "&nodeName=" + encodeURI(NodeName) + "";
    if (ApiProxy === true) {
        serviceUrl = service_java_URL + "woManagement/getWorkOrdersByProjectID?projectID=" + encodeURIComponent(ProjectID + "&projectScopeID="
            + ProjectScopeID + "&assignedTo=" + AssignedTo + "&assignedBy=" + AssignedBy + "&nodeName=" + encodeURI(NodeName));
    }
    $(selectAllWOId).on("keydown", function (event) {

        if (event.keyCode === $.ui.keyCode.TAB &&
            $(this).autocomplete("instance").menu.active) {
            event.preventDefault();
        }
    })
        .autocomplete({

            source: function (request, response) {

                $.isf.ajax({
                    url: serviceUrl,
                    type: "GET",

                    data: {
                        term: extractLast(request.term)
                    },

                    success: function (data) {
                        $(selectAllWOId).autocomplete().addClass(UI_COMPLETE_LOADING);

                        var result = [];
                        $.each(data, function (i, d) {
                            result.push({
                                "label": d.WOID + "/" + d.WOName,
                                "value": d.WOID + "/" + d.WOName
                            });
                        });

                        response($.ui.autocomplete.filter(
                            result, extractLast(request.term)));
                        $(selectAllWOId).autocomplete().removeClass(UI_COMPLETE_LOADING);

                    }
                });
            },

            search: function () {
                // custom minLength
                var term = extractLast(this.value);
                if (term.length < 3) {
                    return false;
                }
            },
            focus: function () {
                // prevent value inserted on focus
                return false;
            },
            select: function (event, ui) {
                var terms = split(this.value);
                // remove the current input
                terms.pop();
                // add the selected item
                terms.push(ui.item.value);
                // add placeholder to get the comma-and-space at the end
                terms.push("");
                this.value = terms.join(",");
                return false;
            },
            minLength: 3

        });

    $(selectAllWOId).autocomplete("widget").addClass("fixedHeight");

    function split(val) {
        return val.split(/,\s*/);
    }
    function extractLast(term) {
        return split(term).pop();
    }


}

$(function () {
    $("[id$=start_date]").datepicker({ minDate: 0 });

});

$(function () {
    $("[id$=end_date]").datepicker({});
});

function getDashboardDetails() {
    var activeProfileObj = JSON.parse(ActiveProfileSession);
    var role = activeProfileObj.role;
    var organisation = activeProfileObj.organisation;
    var serviceUrl = service_java_URL + "reportManagement/getDeliveryDashboardData?signum=" + signumGlobal + "&marketArea=" + organisation + "&role=" + role;
    if (ApiProxy === true) {
        serviceUrl = service_java_URL + "reportManagement/getDeliveryDashboardData?signum=" + encodeURIComponent(signumGlobal + "&marketArea=" + organisation + "&role=" + role);
    }
    
    
    $.isf.ajax({
        url: serviceUrl,
        returnAjaxObj: true,
        context: this,
        crossdomain: true,
        dataType: 'json',
        contentType: 'application/json',
        type: 'GET',
        xhrFields: {
            withCredentials: false
        },
        success: function (data) {
            GenerateTable(data);
        },
        error: function (xhr, status, statusText) {
            console.log('An error occurred');
        }
    })
}

function GenerateTable(data) {

        var tableHTMLPre = '<table id="DashboardTable" class="table table-striped" style="margin: 0px;">';
        var tableHTMLPost = '</table>';

        var tableHeadPre = '<thead><tr>';
        var tableHead = '';
        var tableHeadPost = '</tr></thead>';
        var woTableHead = '';

    tableHead += `<th style="font-weight:bold;">Project ID</th><th style="font-weight:bold;">Project Name</th><th style="font-weight:bold;">Today's Status</th><th style="font-weight:bold;">This Week's Status</th><th style="font-weight:bold;">This Month's Status</th>`;

        var tableBodyPre = '<tbody>';
        var tableBody = '';
        var tableBodyPost = '</tbody>';

    for (const obj in data) {
            tableBody += '<tr>';
          
        var color = '#000000';
        var projectId = data[obj].PROJECTID;
        var completedCount = data[obj].CompletedCount;
        var completedCountToday = data[obj].CompletedTodayCount;
        var completedCountWeek = data[obj].CompletedWeekCount;
        var countToday = data[obj].TotalTodayCount;
        var countWeek = data[obj].TotalWeekCount;
        var woCount = data[obj].TotalWOCount;
        var projName = data[obj].ProjectName;
        var status = "In-Progress";
        tableBody += '<td style=" color:' + color + ';font-weight: bold;">' + projectId + '</td><td style=" color:' + color + ';font-weight: bold;">' + projName + '</td><td style=" color:blue;font-weight: bold;text-decoration:underline;"><a href="#" title="Total Closed WO Count vs Total Planned WO Count. Click to navigate to Delivery Tracker." onclick="moveToDeliveryTracker(\'' + projectId + '\',\'' + 1 + '\',\'' + status + '\',\'' + projName + '\');"><span class="progessDetails">' + completedCountToday + '/' + countToday + '</a></span></td>';
        tableBody += '<td style=" color:blue ;font-weight: bold;text-decoration:underline;"><a href="#" title="Total Closed WO Count vs Total Planned WO Count. Click to navigate to Delivery Tracker." onclick="moveToDeliveryTracker(\'' + projectId + '\',\'' + 2 + '\',\'' + status + '\',\'' + projName + '\');"><span class="progessDetails">' + completedCountWeek + '/' + countWeek + '</a></span></td>';
        tableBody += '<td title="Total Closed WO Count vs Total Planned WO Count. Click to navigate to Delivery Tracker." style=" color:blue;font-weight: bold;text-decoration:underline;"><a href="#" onclick="moveToDeliveryTracker(\'' + projectId + '\',\'' + 3 + '\',\'' + status + '\',\'' + projName + '\');"><span class="progessDetails">' + completedCount + '/' + woCount + '</a></td></span>';

            tableBody += '</tr>';
        }
    
        var tableHTML = tableHTMLPre + tableHeadPre + tableHead + tableHeadPost + tableBodyPre + tableBody + tableBodyPost + tableHTMLPost;
        $("#tableMain").html(tableHTML);
    $("#DashboardTable").DataTable(
        {
        "order": [[0, "asc"]],
        "columnDefs": [{
            "targets": [2, 3, 4],
            "orderable": false,
        }]
        }
    );

}

function moveToDeliveryTracker(projectID, filterkey_time, filterkey_status, projectName) {
    let timePeriod;
    if (filterkey_time === "1" || filterkey_time === 1) {
        timePeriod = "Today";
    }
    else if (filterkey_time === "2" || filterkey_time === 2) {
        timePeriod = "This Week";
    }
    else {
        timePeriod = "This Month";
    }
    var url = "./deliveryTracker?ProjectId=" + projectID + "&TimePeriod=" + timePeriod + "&Status=" + filterkey_status + "&cumulativeFlg=0&ProjName=" + projectName;
    window.location.replace(url);
    

   
}

function getDetails() {
    var project = $("#projList").val();
    let zoom;
    if (project === null || project === undefined || project.length === 0) {
        project = "all";
    }
    const elem = document.getElementsByName(ELEMENT_NAME_ZOOMING);
    for (const item of elem) {
        if (item.selected) {
            zoom = item.value;
        }
    }
    switch (zoom) {
        case "Hours": {
            document.getElementById("divIframe").innerHTML = '<iframe width="100%" src="activityHourly?priority=All&start=all&end=all&projID=' + project + '" style="min-height:500px;"  frameborder="0" id="frame"></iframe>';
            break;
        }
        case "Days": {
            document.getElementById("divIframe").innerHTML = '<iframe width="100%" src="activityDaily?priority=All&start=all&end=all&projID=' + project + '" style="min-height:500px;"  frameborder="0" id="frame"></iframe>';
            break;
        }
        case "Months": {
            document.getElementById("divIframe").innerHTML = '<iframe width="100%" src="activityMonthly?priority=All&start=all&end=all&projID=' + project + '" style="min-height:500px;"  frameborder="0" id="frame"></iframe>';
            break;
        }
    }

}

function ChangeEndDate() {
    var startDate = new Date($('#start_date').val());
    var endDate = new Date($('#end_date').val());
    var duration = (endDate - startDate) / (1000 * 60 * 60 * 24);
    if (duration >= 0) {
        //Empty block
    } else {
        $('#end_date').val("");
    }

}

function resetDates() {
    
    $('#start_date').val("");
    $('#end_date').val("");
    $("#btnResetDates").attr("disabled", true);
}

function generateGantt(Projectid, projName) {

    const manualSDate = $('#dtStart_Date').val();
    const manualEDate = $('#dtEnd_Date').val();
    if (window.startDate === "" || window.startDate === undefined || window.startDate === null
        || window.endDate === "" || window.endDate === undefined || window.endDate === null && (manualSDate === null || manualSDate === undefined || manualSDate === "" || manualEDate === null || manualEDate === undefined || manualEDate === "")) {
        getWeekDateRange();
    }
    startDate = window.startDate;
    endDate = window.endDate;
    if ((manualSDate) && (manualEDate)) {
        startDate = manualSDate;
        endDate = manualEDate;
    }
    if (window.dtStatus === "" || window.dtStatus === undefined || window.dtStatus === null) {
        statusText = 'inprogress';
    }
    else {
        statusText = window.dtStatus;
    }
    if ((manualSDate !== "") && (manualEDate !== "")) {
        if (new Date(manualEDate) < new Date(manualSDate)) {
            pwIsf.alert({ msg: "End Date cannot be earlier than Start date", type: 'warning', autoClose: 3 });
        }
        else if (new Date(manualSDate) > new Date(manualEDate)) {
            pwIsf.alert({ msg: "Start Date cannot be ahead of end date", type: 'warning', autoClose: 3 });
        }
        else {
            generateGanttDT(startDate, endDate, statusText, Projectid);
        }
    } else {
        pwIsf.alert({ msg: "Please select Start Date or End Date", type: 'warning', autoClose: 3 });
    }
    
}



function generateGanttDT(startDate, endDate, dtStatus,projectid)
{
    var proj;
    if (projectid ) {
        proj = projectid;
        $('#projList').val(projectid);
        $('#projList').trigger('change'); 
    }
    else {
        proj = $("#projList").val();
    }
    var neidVal = $("#neidList").val();
    var sigVal = $(AssignedToId).val();
    var sigVal2 = $(AssignedById).val();
    var scopeId = $(scopeListId).val();
    var scope = "";
    if (scopeId !== "" && scopeId !== 0 && scopeId !== "0") {
        scope = $(`${scopeListId} :selected`).text();
    }
    var workOrders = $(selectAllWOId).val();
    const isProjInvalid = proj === null || proj === undefined || proj.length === 0;
    const isWorkOrdersInvalid = workOrders === null || workOrders === undefined || workOrders.length === 0;
    const isNeidInvalid = neidVal === null || neidVal === undefined || neidVal.length === 0;
    const isSigValInvalid = sigVal === null || sigVal === undefined || sigVal.length === 0;
    const isScopeInvalid = scope === null || scope === undefined || scope.length === 0;
    const isSigVal2Invalid = sigVal2 === null || sigVal2 === undefined || sigVal2.length === 0;
    if (isProjInvalid &&
        isWorkOrdersInvalid &&
        isNeidInvalid &&
        isSigValInvalid &&
        isScopeInvalid &&
        isSigVal2Invalid) {

        pwIsf.alert({ msg: "Please select a filter", type: "warning" });
    }
    else if (isProjInvalid && isWorkOrdersInvalid){
        pwIsf.alert({ msg: "Please select a Project first", type: "warning" });
        $('#neidList').val('');
    }
    else {
       
        var assignedTo = $(AssignedToId).val();
        var assignedBy = $(AssignedById).val();
        var neid = $("#neidList").val();
        
        let start = startDate;
        let end = endDate;
        let zoom;
        let priority;
        const elem = document.getElementsByName(ELEMENT_NAME_ZOOMING);
        for (const item of elem) {
            if (item.selected) {
                zoom = item.value;
            }
        }

        const elem2 = document.getElementsByName('Filtering');
        for (const item of elem2) {
            if (item.checked) {
                priority = item.value;
            }
        }

        if (isProjInvalid) {
            proj = "all";
        }
        if (start === "") {
            start = "all";
        }
        if (end === "") {
            end = "all";
        }
        var monthFlag = true;
        switch (zoom) {
            case "Hours": {
                document.getElementById("divIframe").innerHTML = '<iframe width="100%" src="activityHourly?priority=' + priority + '&start=' + start + '&end=' + end + '&projID=' + proj + '&status=' + dtStatus + '&scope=' + scope + '&neid=' + neid + '&assignedTo=' + assignedTo + '&assignedBy=' + assignedBy + '&workOrders=' + workOrders +'" style="min-height:450px;"  frameborder="0" id="frame"></iframe>';
                break;
            }
            case "Days": {
                monthFlag = false;
                document.getElementById("divIframe").innerHTML = '<iframe width="100%" src="activityDaily?priority=' + priority + '&start=' + start + '&end=' + end + '&monthFlag=' + monthFlag + '&projID=' + proj + '&status=' + dtStatus + '&scope=' + scope + '&neid=' + neid + '&assignedTo=' + assignedTo + '&assignedBy=' + assignedBy + '&workOrders=' + workOrders+'" style="min-height:500px;"  frameborder="0" id="frame"></iframe>';
                break;
            }
            case "Months": {
                document.getElementById("divIframe").innerHTML = '<iframe width="100%" src="activityMonthly?priority=' + priority + '&start=' + start + '&end=' + end + '&monthFlag=' + monthFlag + '&projID=' + proj + '&status=' + dtStatus + '&scope=' + scope + '&neid=' + neid + '&assignedTo=' + assignedTo + '&assignedBy=' + assignedBy + '&workOrders=' + workOrders +'" style="min-height:450px;"  frameborder="0" id="frame"></iframe>';
                break;
            }
        }
        if ((start !== "") && (end !== "")) {
            $("#btnResetDates").attr("disabled", false);
        }
    }
}

function onChangeProject() {
    $(selectAllWOId).val("");
    $(selectAllWOId).trigger('change');
    var ProjectID = $('#projList').select2('val');
    if ($('#projList').val() !== "" && !(ProjectID === undefined || ProjectID === null || ProjectID === '' || isNaN(parseInt(ProjectID)))) {
        
        pwIsf.addLayer({ text: "Please wait ..." });

        $.isf.ajax({
            url: service_java_URL + "/projectManagement/v1/ScopeByProject?projectId=" + parseInt(ProjectID),
            success: function (data) {
                pwIsf.removeLayer();
                $(scopeListId).empty();
                $(scopeListId).append('<option value="0">Select Deliverable</option>');
                $.each(data, function (i, d) {
                    $(scopeListId).append('<option value="' + d.projectScopeID + '">' + d.scopeName + '</option>');
                })

            },
            error: function (xhr, status, statusText) {
                pwIsf.removeLayer();
                console.log('An error occurred on getDomain: ' + xhr.error);
            }
        });
        getNEID();
    }
    else {
        $(scopeListId).find('option').not(':first').remove();
        $(scopeListId).val("");
        $(scopeListId).trigger('change');
    }
}

function onScopeChange() {
    $(selectAllWOId).val("");
    $(selectAllWOId).trigger('change');
}

/*--------------Get Current Week Date Range (Default)-----------------------------*/

function getWeekDateRange() {
    var today = new Date();
    var day = today.getDay();
    var diff = today.getDate() - day + (day == 0 ? -6 : 1); // 0 for sunday
    var week_start_tstmp = today.setDate(diff);
    var week_start = new Date(week_start_tstmp);
    startDate = formatted_date(week_start);    
    var week_end = new Date(week_start_tstmp);  // first day of week 
    week_end = new Date(week_end.setDate(week_end.getDate() + 6));
    endDate = formatted_date(week_end);
}

/*---------------Get DT Data According to Duration Clicked----------------------------*/

function getDurationDT(text, el) {
    durationText = text;
    let y, m;
    if (text !== "This Week" && jQuery.type(el) !== "integer") {
        $(removeActiveClass).removeClass("active");
        $(el).addClass("active");
        durationText = durationText.toUpperCase();
    }
    if (text === "Today") {
        $(removeActiveClass).removeClass("active");        
        $(`${durationButtonsId} > .btn`).eq(2).addClass('active')
        durationText = text.toUpperCase();
    }
    else if (text === "This Month") {
        $(removeActiveClass).removeClass("active");        
        $(`${durationButtonsId} > .btn`).eq(4).addClass('active')
        durationText = text.toUpperCase();
    }
    else if (text === "This Week") {
        $(removeActiveClass).removeClass("active");
        $(`${durationButtonsId} > .btn`).eq(3).addClass('active')
        durationText = text.toUpperCase()
    }
    let today;
    let day;
    let firstDay;
    let lastDay;
    let diff;
    let week_start_tstmp;
    let week_start;
    let week_end;
    switch (durationText) {
        case "LAST WEEK":
            today = new Date();
            day = today.getDay();
            diff = today.getDate() - day + (day == 0 ? -13 : -6);
            week_start_tstmp = today.setDate(diff);
            week_start = new Date(week_start_tstmp);
            startDate = formatted_date(week_start);
            week_end = new Date(week_start_tstmp);  // first day of week 
            week_end = new Date(week_end.setDate(week_end.getDate() + 6));
            endDate = formatted_date(week_end);
            
            break;
        case "LAST MONTH":
            today = new Date();
            y = today.getFullYear();
            m = today.getMonth();
            firstDay = new Date(y, m - 1, 1);
            lastDay = new Date(y, m, 0);
            startDate = formatted_date(firstDay);
            endDate = formatted_date(lastDay);
            break;
        case "TODAY":
            today = new Date();
            var tomorrow = new Date(today);
            tomorrow.setDate(tomorrow.getDate() + 1);
            startDate = formatted_date(today);
            endDate = formatted_date(today);
            
            break;
        case "THIS WEEK":
            getWeekDateRange();
            break;
        case "THIS MONTH":
            today = new Date();
            y = today.getFullYear();
            m = today.getMonth();
            firstDay = new Date(y, m, 1);
            lastDay = new Date(y, m + 1, 0);
            startDate = formatted_date(firstDay);
            endDate = formatted_date(lastDay);
            break;
        case "NEXT WEEK":
            today = new Date();
            day = today.getDay();
            diff = today.getDate() - day + (day == 0 ? 1 : 8);
            week_start_tstmp = today.setDate(diff);
            week_start = new Date(week_start_tstmp);
            startDate = formatted_date(week_start);
            week_end = new Date(week_start_tstmp);  // first day of week 
            week_end = new Date(week_end.setDate(week_end.getDate() + 6));
            endDate = formatted_date(week_end);
            break;
        case "NEXT MONTH":
            today = new Date();
            y = today.getFullYear();
            m = today.getMonth();
            firstDay = new Date(y, m + 1, 1);
            lastDay = new Date(y, m + 2, 0);
            startDate = formatted_date(firstDay);
            endDate = formatted_date(lastDay);
            break;
    }
    
    if (statusText.toUpperCase() === "ALL") {
        statusText = "all";
    }
    let projectid;
    if (jQuery.type(el) !== "string") {
        projectid = null;
        generateGanttDT(startDate, endDate, statusText, projectid);
    }
    else {
        projectid = el;
        $('#dtStart_Date').val(startDate).trigger('change');
        $('#dtEnd_Date').val(endDate).trigger('change');
        generateGanttDT(startDate, endDate, statusText,projectid);
    }
}

/*---------------Get DT Data According to Status Clicked----------------------------*/

function getStatusDT(el) {
    statusText = el.innerText;
    if (statusText.indexOf("-") > 0) {
        statusText = statusText.substr(0, statusText.indexOf("-"));
    }
    statusText = statusText.toUpperCase();    
    $(".removeActive2").removeClass("active");
    $(el).addClass("active");
    switch (statusText) {
        case "ALL":
            dtStatus = "all";
            break;
        case "ASSIGNED":
            dtStatus = "Assigned";
            break;
        case "INPROGRESS":
            dtStatus = "InProgress";
            break;
        case "ONHOLD":
            dtStatus = "OnHold";
            break;
        case "DEFERRED":
            dtStatus = "Deferred";
            break;
        case "CLOSED":
            dtStatus = "Closed";
            break;
        case "PLANNED":
            dtStatus = "Planned";
            break;
        case "REJECTED":
            dtStatus = "Rejected";
            break;
    }
    if (startDate === undefined || startDate === null) {
        getWeekDateRange();
        startDate = window.startDate;
    }
    generateGanttDT(startDate, endDate, dtStatus);
}
