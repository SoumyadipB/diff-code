var events = [

];

var Calendar_StartDate = '';
var Calendar_EndDate = '';

var calendarSignum = '';
var shiftStartTime = '08:00:00';
var shiftEndTime = '17:00:00';

$(document).ready(function () {
    var div = $("#calendar_view");
    div.hide();


});

function addTiles(data, num) {
    var tile = $('.tileBacklog'),
        ragStates = ['no-rag'],
        completionStates = ['incomplete'];
    generateTiles(data);
    for (var i = 0; i < num; i++) {

        var rag = ragStates[Math.floor(Math.random() * ragStates.length)];
        var com = completionStates[Math.floor(Math.random() * completionStates.length)];
        $('.tilesBacklog').append(tile.clone().addClass(rag).addClass(com));

    }

    tile.remove();
}

function init(events_data, isSetView, signum, currentDate) {

    // ConvertTimeZones(events_data, getResourceCalander_tzColumns);
    ///events_data.each
    scheduler.clearAll();
    scheduler.parse(events_data, "json");
    if (isSetView.toLowerCase() == "true") {
        scheduler.updateView(currentDate, "week");
    }
    if (signum == "" || signum == undefined || signum == null) {
        GetShiftTimingBySignum(signumGlobal, currentDate, true);
    }
    else {
        GetShiftTimingBySignum(signum, currentDate, true);
    }


}

function openFlowChart(url) {
    var width = window.innerWidth * 0.85;
    // define the height in
    var height = width * window.innerHeight / window.innerWidth;
    // Ratio the hight to the width as the user screen ratio
    window.open(url, 'newwindow', 'width=' + width + ', height=' + height + ', top=' + ((window.innerHeight - height) / 2) + ', left=' + ((window.innerWidth - width) / 2));
}

function openFlowChartInMyWork(url) {

    window.open(url);
}

function updateCalendar(currentDate, calanderMode) {
    getResourceCalendar(calendarSignum, "false", currentDate, calanderMode);
}


function convertDate(date) {
    var mm = date.getMonth() + 1; // getMonth() is zero-based
    var dd = date.getDate();

    return [mm, dd, date.getFullYear()].join('/');
}

function getResourceCalendar(signum, isSetView, currentDate, calanderMode) {

    calendarSignum = signum;
    console.log(convertDate(scheduler.getState().min_date));
    console.log(convertDate(scheduler.getState().max_date));
    var serviceUrl = service_java_URL + "resourceManagement/getResourceCalander?signum=" + calendarSignum + "&startdate=" + convertDate(scheduler.getState().min_date) + "&enddate=" + convertDate(scheduler.getState().max_date);
    if (ApiProxy == true) {
        serviceUrl = service_java_URL + "resourceManagement/getResourceCalander?signum=" + encodeURIComponent(calendarSignum + "&startdate=" + convertDate(scheduler.getState().min_date) + "&enddate=" + convertDate(scheduler.getState().max_date));
    }
    pwIsf.addLayer({ text: "Please wait ..." });
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
            var schduleEventsData = [];
            schduleEventsData = Array.from(data.responseData);
            var i = 0;
            for (i = 0; i < schduleEventsData.length; i++) {

                if (isSingleDayEventsAssociated(schduleEventsData[i]) == false && calanderMode !== "month" && schduleEventsData[i].type.toLowerCase() != 'workorder') {
                    splitMultiDayLeaveEventToSingleDayEvent(schduleEventsData[i], data.responseData, shiftStartTime, shiftEndTime);
                } 
            }
            events = data.responseData;
            init(events, isSetView, calendarSignum, currentDate);
            pwIsf.removeLayer();
        },
        error: function (xhr, status, statusText) {
            console.log('An error occurred');
            pwIsf.removeLayer();
        }
    })

}
var formattedEventDate = function (date, type, dateType) {
    var m = ("0" + (date.getMonth() + 1)).slice(-2); // in javascript month start from 0.
    var d = ("0" + date.getDate()).slice(-2); // add leading zero 
    var y = date.getFullYear();
    var hh = date.getHours();
    var mm = date.getMinutes();
    var eventTime = '';
    if (type == "Leave") {
        if (dateType == "s") {
            eventTime = shiftStartTime;
        } else {
            eventTime = shiftEndTime;
        }

    } else {
        eventTime = hh + ':' + mm;
    }
    return y + '-' + m + '-' + d + ' ' + eventTime;
}

function splitMultiDayLeaveEventToSingleDayEvent(eventData, oData, shiftStartTime, shiftEndTime) {
    var startDateOfLeave;
    var endDateOfLeave;
    var eventCollection = [eventData];
    if ((eventData.start_date instanceof Date)) {
        shiftStartTime = eventData.start_date.getHours() + ':' + eventData.start_date.getMinutes();
        shiftEndTime = eventData.end_date.getHours() + ':' + eventData.end_date.getMinutes();
    }
    if (eventData && eventCollection.length > 0) {
        endDateOfLeave = (eventData.end_date);
        startDateOfLeave = (eventData.start_date);
        oData.splice(oData.findIndex(x => x.ID === eventData.ID), 1);
        let indx = eventData.id;
        let increment = 0;
        while (startDateOfLeave <= endDateOfLeave) {
            var curentSDate = getStartDateFormat(startDateOfLeave);
            var curentEDate = getEndDateFormat(startDateOfLeave);

            var coustomEventData = returnCoustomEvent(eventData, curentSDate, curentEDate, indx);
            indx = eventData.id + "_" + ++increment;
            startDateOfLeave.setDate(startDateOfLeave.getDate() + 1);
            oData.push(coustomEventData);//append to origin array

        }
    }
}

function getEndDateFormat(date) {
    var d = new Date(date),
        month = '' + (d.getMonth() + 1),
        day = '' + d.getDate(),
        year = d.getFullYear();

    if (month.length < 2)
        month = '0' + month;
    if (day.length < 2)
        day = '0' + day;
     date = new Date();
    date.toLocaleDateString();
    var returnDate = [year, month, day].join('-');
    returnDate = returnDate + ' ' + shiftEndTime;
    return returnDate;
}
function getStartDateFormat(date) {
    var d = new Date(date),
        month = '' + (d.getMonth() + 1),
        day = '' + d.getDate(),
        year = d.getFullYear();

    if (month.length < 2)
        month = '0' + month;
    if (day.length < 2)
        day = '0' + day;
     date = new Date();
    date.toLocaleDateString();
    var returnDate = [year, month, day].join('-');
    returnDate = returnDate + ' ' + shiftStartTime;
    return returnDate;
}

function returnCoustomEvent(eventData, curentSDate, curentEDate, index) {
    return  {
        id: index,
        projectID: eventData.projectID,
        projectName: eventData.projectName,
        type: eventData.type,
        start_date: curentSDate,
        end_date: curentEDate,
        text: eventData.text,
        status: eventData.status,
        proficiencyID: eventData.proficiencyID,
        proficiencyName: eventData.projectName
    };
}
var CalendarHTML = '<div id="scheduler_here" class="dhx_cal_container" style="width: 100%; height: 90 %;overflow: auto; ">'
    + '< div class="dhx_cal_navline" >'
    + '<div class="dhx_cal_prev_button">&nbsp;</div>'
    + '<div class="dhx_cal_next_button">&nbsp;</div>'
    + '<div class="dhx_cal_today_button"></div>'
    + '<div class="dhx_cal_date"></div>'
    + '<div class="dhx_cal_tab" name="day_tab" style="right:204px;"></div>'
    + '<div class="dhx_cal_tab" name="week_tab" style="right:140px;"></div>'
    + '<div class="dhx_cal_tab" name="month_tab" style="right:76px;"></div>'
    + '</div >'
    + '<div class="dhx_cal_header">'
    + '</div>'
    + '<div class="dhx_cal_data">'
    + '</div>'
    + '</div >';


function getShift(signum) {
    let tempDate = formatted_date(new Date());
    let weekNo = getWeekNumber(tempDate);
    let startDate = dateFromWeekNumber(weekNo[0], weekNo[1]);
    let currentStartDate = formatted_date(startDate);
    $.isf.ajax({
        url: `${service_java_URL}activityMaster/getShiftTimmingBySignum/${signum}/${currentStartDate}`,
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
            ConvertTimeZones(data, getShitTimmingBySignum_tzColumns);
        },
        error: function (xhr, status, statusText) {
            console.log('An error occurred');
        }
    });
}


function loadCalendar() {
    var calendar_mode = '';
    var calendar_date = '';
    var ProfileName = '';
    var dt = new Date();
    scheduler.config.fix_tab_position = true;
    scheduler.config.xml_date = "%Y-%m-%d %H:%i";
    scheduler.config.readonly = true;
    scheduler.attachEvent("onViewChange", function (new_mode, new_date) {
        if (new_mode != calendar_mode || new_date != calendar_date) {
            calendar_mode = new_mode;
            calendar_date = new_date;
            updateCalendar(new_date, new_mode);
        }
    });
    if (ActiveProfileSession) {
        ProfileName = JSON.parse(ActiveProfileSession).accessProfileName;
	}
    scheduler.attachEvent("onClick", function (id, e) {
        var obj = scheduler.getEvent(id);
        if (obj.type && obj.type.toLowerCase() === 'workorder') {
            if (ProfileName && ProfileName.toLowerCase() === "network engineer") {
                openFlowChartInMyWork(`${UiRootDir}DeliveryExecution/WorkorderAndTask?woid=${obj.id}`);
            }
            else {
                const profId = getProficiencyId(obj.id, calendarSignum);
                const proficiencyId = (profId === null || profId === undefined) ? 0 : profId;
                if (Boolean(profId) !== false) {
                    openFlowChart(`${UiRootDir}/DeliveryExecution/FlViewWithoutComment?mode=view&woID=${obj.id}&proficiencyId=${proficiencyId}`);
                }
            }
        }
        return false;
    });
    scheduler.init('scheduler_here', new Date(dt.getUTCFullYear(), dt.getUTCMonth(), dt.getUTCDate()), "week");
    scheduler.templates.event_text = function (start, end, event) {
        if (event.type.toLowerCase() == 'workorder') {
            return `${event.id}_${event.text}`;
        }
        else {
            return event.text;
        }
    }

    scheduler.templates.event_class = function (start, end, event) {
      
        var selectedProjectID = $('#projList option:selected').val();
        if (event.projectID == selectedProjectID && event.type.toLowerCase() == 'workorder') {
            if (event.status.toLowerCase() == 'closed') {
                return "closed_event";
            }
            else if (event.status.toLowerCase() === 'deferred') {
                return "deferred_event";
            }
            else if (event.status.toLowerCase() === 'assigned') {
                return "assigned_event";
            }
            else if (event.status.toLowerCase() === 'inprogress') {
                return "inprogress_event";
            }
            else if (event.status.toLowerCase() === 'onhold') {
                return "onhold_event";
            }        
        }
        else if (event.type.toLowerCase() === 'project') {
                return "project_non_wo_booking";
        }
        else if (event.type.toLowerCase() === 'internal') {
                return "internal_non_wo_booking";
       }
        else if (event.type.toLowerCase() === 'leave') {

            return "grayleave";
        }
        else if (event.type.toLowerCase() != 'workorder') {
            return "others_event";
        }
        else {
            return "otherprojectWO_event";
        }
        
    };
    scheduler.templates.event_bar_text = function (start, end, event) {
        if (event.type.toLowerCase() === 'workorder') {
            return `${event.id}_${event.text}`;
        }
        else {
            return event.text;
        }
    };
    scheduler.templates.tooltip_text = function (start, end, ev) {
        let event_type = '';
        if (ev.type.toLowerCase() === 'workorder') {
            event_type = "WorkOrder";
        }
        else if (ev.type.toLowerCase() === 'leave') {
            event_type = "Leave";
        }
        else {
            event_type = "Others";
        }
        return "<b>" + event_type + " :</b> " + ev.id + "_" + ev.text + "<br/><b>Start Date:</b> " +
            scheduler.templates.tooltip_date_format(start) +
            "<br/><b>End Date:</b> " + scheduler.templates.tooltip_date_format(end);

    }
}
function getBacklogItems(signum, projId) {
    $.isf.ajax({
        url: `${service_java_URL}resourceManagement/getBacklogWorkOrders?signum=${signum}`,
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
            $('.tilesBacklog').html('');
            if (data.length != 0) {
                addTiles(data, data.length);
            }

        },
        error: function (xhr, status, statusText) {
            console.log('An error occurred at API call');
        }
    })
}
function breakTooltipContent(yourString, lineLength) {
    let argRegEx = new RegExp('.{1,' + lineLength + '}', 'g');
    return yourString.match(argRegEx).join('\n');
}
function generateTiles(data) {
    ConvertTimeZones(data, getBacklogWorkOrders_tzColumns);
    var tileHTML = "";
    for (var obj in data) {
        let nodeNameHtml = '';
        if (data[obj].nodeNames === null) {
            var nodeNames = "Not Available";
        }
        else {
            var nodeNames = data[obj].nodeNames
        }
        var profLevel = getProficiencyLevel(data[obj].status, data[obj].ProficiencyName);
        let proficiencyId = 0;
        if (data[obj].ProficiencyID == null || data[obj].ProficiencyID == undefined) {
            proficiencyId = 0;
        } else {
            proficiencyId = data[obj].ProficiencyID;
		}
        if (jQuery.trim(nodeNames).length > 1) {
            let tooltipContent = breakTooltipContent(nodeNames, 40);
            nodeNameHtml = `<b>${shortNeNameAsNEID}:</b> <p class="nodeName protip" style="width:auto;max-width:40%;" data-pt-scheme="aqua" data-pt-position="bottom"
            data-pt-skin="square" data-pt-width="400" data-pt-title="${tooltipContent}">${nodeNames}</p>`;
        }
        else {
            nodeNameHtml = `<b>${shortNeNameAsNEID}:</b> <p>${nodeNames}</p>`;
        }

        if (JSON.parse(ActiveProfileSession).accessProfileName.toLowerCase() == 'network engineer') {
            tileHTML += '<li class="tileBacklog"><header style="background-color:#730000"><label><span><b>WOID:</b><a onclick="openFlowChartInMyWork(\'' + UiRootDir + '/DeliveryExecution/WorkorderAndTask?woid=' + data[obj].woid + '\'); ">' + data[obj].woid + '</a></span></label></header >';
        }
        else {
            tileHTML += '<li class="tileBacklog"><header style="background-color:#730000"><label><span><b>WOID:</b><a onclick="openFlowChart(\'' + UiRootDir + '/DeliveryExecution/FlViewWithoutComment?mode=view&woID=' + data[obj].woid + "\&proficiencyId=" + proficiencyId + "\&proficiencyLevel=" + profLevel + '\'); ">' + data[obj].woid + '</a></span></label></header >';
        }


        tileHTML += `<section="bodyBacklog"><h5 style="margin:5px;"><h5 style="margin:5px;"><b>Project ID:</b>` + data[obj].projectId + `</h5><h6 style = "margin: 5px;"><b>Planned Start Date:</b>` + $.format.date(data[obj].plannedStartDate, "dd-MM-yyyy HH:mm") + `</h6><h5 style="margin: 5px;">` + nodeNameHtml + `</h5>`;
        $('.tilesBacklog').append(tileHTML);

        tileHTML = "";
    }
}

function OpenCalendarView(signum, emp_name, projId) {

    calendarSignum = signum;
    let currentDate = new Date();

    getResourceCalendar(signum, "true", currentDate, "");
    getBacklogItems(signum, projId);
    $("#calendar_title").text = emp_name;
    $("#calendar_title")[0].innerHTML = '<span class="fa fa-calendar" aria-hidden="true" style="margin-right: 10px;"></span>' + emp_name;
    var div = $("#calendar_view");
    div.show();
    div.animate({ right: '0%', opacity: 1.0 }, "slow");
    ShowBackLogs();
}

function CloseCalendarView() {
    var div = $("#calendar_view");
    div.animate({ right: '-3%' }, "slow");
    div.hide();
}

function setEmpNameOnCalendar(emp_obj) {
    $("#calendar_title").html = emp_obj["EmployeeName"];
}

function ShowBackLogs() {
    var div = $("#backlogPanel");
    div.show();
    div.animate({ right: '0%', opacity: 1.0 }, "slow");
}

function CloseBackLogs() {
    var div = $("#backlogPanel");
    div.animate({ right: '-3%' }, "slow");
    div.hide();
}

function dateFromWeekNumber(year, week) {
    var d = new Date(year, 0, 1);
    var dayNum = d.getDay();
    var diff = --week * 7;

    // If 1 Jan is Friday to Sunday, go to next week
    if (!dayNum || dayNum > 4) {
        diff += 7;
    }

    // Add required number of days
    d.setDate(d.getDate() - d.getDay() + ++diff);
    return d;
}

var formatted_date = function (date) {
    var m = ("0" + (date.getMonth() + 1)).slice(-2); // in javascript month start from 0.
    var d = ("0" + date.getDate()).slice(-2); // add leading zero 
    var y = date.getFullYear();
    return y + '-' + m + '-' + d;
}

function GetShiftTimingBySignum(Signum, currentDate, isMarkedTimeSpan) {
    let weekNo = getWeekNumber(currentDate);
    let startDate = dateFromWeekNumber(weekNo[0], weekNo[1]);
    let currentStartDate = formatted_date(startDate);
    scheduler.deleteMarkedTimespan();
    $.isf.ajax({
        url: `${service_java_URL}activityMaster/getShiftTimmingBySignum/${Signum}/${currentStartDate}`,
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
            if (isMarkedTimeSpan) {
                addMarkedTimeSpan(data);
            }
        },
        error: function (xhr, status, statusText) {
            console.log('An error occurred');
        }
    });
}

function addMarkedTimeSpan(data) {
    if (data.length == 0) {
        scheduler.addMarkedTimespan({
            days: [0, 1, 2, 3, 4, 5, 6], // marks each Friday
            zones: [0 * 60, 8 * 60, 17 * 60, 24 * 60],
            css: "gray_section"   // the name of applied CSS class
        });

        scheduler.updateView();
    }
    else {
        var startDateTime = data[0].shiftISTStartDate + " " + data[0].shiftISTStartTime;
        var startConvertedTime = GetConvertedTime(startDateTime);
        var endDateTime = data[0].shiftISTEndDate + " " + data[0].shiftISTEndTime;
        var endConvertedTime = GetConvertedTime(endDateTime);
        var startTime = parseInt(startConvertedTime.split(":")[0], 10);
        var endTime = parseInt(endConvertedTime.split(":")[0], 10);
        let startDate = new Date(data[0].shiftISTStartDate)
        let endDate = new Date(data[0].shiftISTEndDate)
        if (startTime > endTime) {
            for (let i = startDate; startDate <= endDate; startDate.setDate(startDate.getDate() + 1)) {
                let day = startDate.getDate();
                let month = startDate.getMonth();
                let year = startDate.getFullYear();
                scheduler.addMarkedTimespan({
                    start_date: new Date(year, month, day, endTime),
                    css: "gray_section",   // the name of applied CSS class
                    end_date: new Date(year, month, day, startTime)
                });
            }

            scheduler.updateView();
        }
        else {

            for (let i = startDate; startDate <= endDate; startDate.setDate(startDate.getDate() + 1)) {
                let day = startDate.getDate();
                let month = startDate.getMonth();
                let year = startDate.getFullYear();
                scheduler.addMarkedTimespan({
                    start_date: new Date(year, month, day, 0),
                    //days: [0, 1, 2, 3, 4, 5, 6], // marks each Friday
                    //zones: [endTime * 60, startTime * 60],
                    css: "gray_section",   // the name of applied CSS class
                    end_date: new Date(year, month, day, startTime)
                });

                scheduler.addMarkedTimespan({
                    start_date: new Date(year, month, day, endTime),
                    //days: [0, 1, 2, 3, 4, 5, 6], // marks each Friday
                    //zones: [endTime * 60, startTime * 60],
                    css: "gray_section",   // the name of applied CSS class
                    end_date: new Date(year, month, day, 24)
                });
            }
            scheduler.updateView();
        }


    }
}

function isSingleDayEventsAssociated(e) {
    e.start_date = new Date(e.start_date);
    e.end_date = new Date(e.end_date);
    var t = e.end_date.getDate() - e.start_date.getDate(); return t ? (0 > t && (t = Math.ceil((e.end_date.valueOf() - e.start_date.valueOf()) / 864e5)),
        1 == t && !e.end_date.getHours() && !e.end_date.getMinutes() && (e.start_date.getHours() || e.start_date.getMinutes())) : e.start_date.getMonth() == e.end_date.getMonth() && e.start_date.getFullYear() == e.end_date.getFullYear()
}