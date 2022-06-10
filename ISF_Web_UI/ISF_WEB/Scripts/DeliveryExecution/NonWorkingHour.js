var adhocBookingStatus = false;
var otherActivity = "";
var timerObjAdhoc = [];
var timerObjAdhocTotalEffort = [];
var arrPauseAllAdhoc = [];
var timeOuts = new Array();
var timerWorker;
const ullistItemId = `#adhocActivity`;
const _InternalActivity = 'internal';

$(document).ready(function () {
    $('#adhocBox').hide();
    timerObjAdhoc = [];
    timerObjAdhocTotalEffort = [];
        const adhocIcons = localStorage.getItem('adhocType');
        if (adhocIcons !== null) {
            const responseData = JSON.parse(adhocIcons);
            $.each(responseData, function (i, d) {
                getAnchorIcon(d);
            });
        }
        else {
            getAdhocActivityIcons();
    }

    if (IsAspUser === "False" || (IsAspUser === "True" && AspStatus === "APPROVED")) {
        getAdhocBookingDetail(true);
    }
});

function addAdhocCSS() {
    $("#adhocAnchor .circle").addClass("hoverAdhocCss");
}

function removeAdhocCSS() {
    $("#adhocAnchor .circle").removeClass("hoverAdhocCss");
}

//Stores the Manual tasks which are in started state
function createInprogressTasksHtmlAdhoc(passData) {    

    for (var i in passData) {
        if (passData[i].executionType) {
            var woID = (passData[i].woID) ? passData[i].woID : replaceNullBy;
            if ((passData[i].executionType === "Manual" || passData[i].executionType === "ManualDisabled") && passData[i].status === 'STARTED') {
                arrPauseAllAdhoc.push({
                    'woID': woID, 'bookingID': passData[i].bookingID, 'executionType': passData[i].executionType,
                    'flowChartStepID': passData[i].flowChartStepID, 'taskID': passData[i].taskID, 'flowChartDefID': passData[i].flowChartDefID,
                    'workFlowAutoSenseEnabled': passData[i].workFlowAutoSenseEnabled, 'workOrderAutoSenseEnabled': passData[i].workOrderAutoSenseEnabled,
                    'startRule': passData[i].startRule, 'stopRule': passData[i].stopRule
                });
            }
        }
    }

}

function CheckForParallelAdhocandWO(obj) {
    $.isf.ajax({
        url: `${service_java_URL}woManagement/v1/getInprogressTask/${signumGlobal}`,
        type: "GET",
        crossDomain: true,
        async: false,
        success: function (data) {
            arrPauseAllAdhoc = [];
            if (data.responseData !== null && data.responseData !== undefined && data.isValidationFailed === false) {
                createInprogressTasksHtmlAdhoc(data.responseData);
            }

        },
        error: function (xhr, status, statusText) {
            console.log('An error occurred at woManagement/v1/getInprogressTask');
        }
    });
    if (arrPauseAllAdhoc.length > 0) {
        pwIsf.confirm({
            title: `${arrPauseAllAdhoc.length} Tasks Running`,
            'buttons': {
                'Pause Tasks': {
                    'action': function () {
                        SaveNonWorkingHour(obj, true);
                        if (arrPauseAllAdhoc.length > 0) {
                            pauseAllRunningTask(arrPauseAllAdhoc, 'ONHOLD', 'Adhoc Booking through ISF');
                        }
                        if ($('#refreshInprogressTask').length !== 0) {
                            $('#refreshInprogressTask').trigger('click');
                        }

                    },
                    'class': 'btn btn-warning'

                },
                'Continue Tasks': { 'action': function () { SaveNonWorkingHour(obj, false); }, 'class': 'btn btn-success' },
                'Cancel': {
                    'action': function () { pwIsf.removeLayer(); }
                }

            }
        });
    }
    else {
        SaveNonWorkingHour(obj);
    }
}

function SaveNonWorkingHour(obj, pauseTasks) {

    var now = new Date();

    var day = ("0" + now.getDate()).slice(-2);
    var month = ("0" + (now.getMonth() + 1)).slice(-2);
    var year = now.getFullYear();

    var hour = (now.getHours() < 10 ? '0' : '') + now.getHours();
    var minute = (now.getMinutes() < 10 ? '0' : '') + now.getMinutes();
    var finalHr = 0;
    var selectedMin = parseInt($("#durationInterval").val());
    var TotalMin = selectedMin + parseInt(minute);
    var TotalHr = 0;

    if (TotalMin >= 60) {
        finalHr = parseInt(TotalMin / 60);
        TotalMin = TotalMin % 60;
    }
    TotalHr = finalHr + parseInt(hour);

    if (TotalHr >= 24) {
        TotalHr = TotalHr % 24;
        var tomorrow = new Date();
        tomorrow.setDate(tomorrow.getDate() + 1);
        var day1 = ("0" + tomorrow.getDate()).slice(-2);
        var month1 = ("0" + (tomorrow.getMonth() + 1)).slice(-2);
        month = month1;
        day = day1;
        year = tomorrow.getFullYear();

    }
        if (ValidateNonWorkingHourData()) {
        var nonWorkingHourData = new Object();

        var selAssignedProject = $(obj).data('value').toString();
        if (selAssignedProject != "" && selAssignedProject != null && selAssignedProject != "undefined") {
            var tblProjects = new Object();
            tblProjects.projectId = selAssignedProject
            nonWorkingHourData.tblProjects = tblProjects;

        }

        var tblAdhocBookingActivity = new Object();
            tblAdhocBookingActivity.adhocBookingActivityId = $(obj).data('adhocactivityid');
        nonWorkingHourData.tblAdhocBookingActivity = tblAdhocBookingActivity;
        nonWorkingHourData.signumID = signumGlobal;
        nonWorkingHourData.plannedEndDate = new Date();
        nonWorkingHourData.status = 'Started';
        if (pauseTasks)
            nonWorkingHourData.pausedBooking = getRunningBookingId();
        nonWorkingHourData.createdBy = signumGlobal;
        nonWorkingHourData.comment = "START"
            pwIsf.addLayer({ text: "Please wait ..." });
        $.isf.ajax({
            url: `${service_java_URL}adhocActivity/saveAdhocBooking`,
            context: this,
            crossdomain: true,
            processData: true,
            contentType: 'application/json',
            type: 'POST',
            data: JSON.stringify(nonWorkingHourData),
            xhrFields: {
                withCredentials: false
            },
            success: function (data) {
                $('#adhocBookingID').val(data.data);
                $('#divProject').hide();
                $('#selectProjectName').hide();
                $('#modalAdhocTimeBooking').modal('hide');
                
                
                    pwIsf.removeLayer();
                getAdhocBookingDetail();
                
                if (data.apiSuccess === false && data.responseMsg != null) {
                    pwIsf.alert({ msg: data.responseMsg, type: 'warning' });
                }
                LaunchFloatingWindow(null, true, false);

            },
            error: function (xhr, status, statusText) {
                pwIsf.removeLayer();
                console.log('An error occurred on : record  adhoc booking' + xhr.error);
            }
        })
    } else {
        console.log('Validation Failed');
    }

}
function getRunningBookingId() {
    var bookingIdList = "";

    $.each(arrPauseAllAdhoc, function (i, d) {
        if (i == 0) {
            bookingIdList = d.bookingID;
        } else {
            bookingIdList = `${bookingIdList}#${d.bookingID}`;
        }
    })

    return bookingIdList;

}
function ValidateNonWorkingHourData() {
    var result = true;
    return result;
}

//pause all woids.
function pauseAllRunningTask(arrPauseAllAdhoc, status, reason) {

    $.isf.ajax({
        type: "POST",
        contentType: 'application/json',
        async: false,
        data: JSON.stringify(arrPauseAllAdhoc),
        url: `${service_java_URL}woExecution/pauseAllWOTasks/${reason}/${signumGlobal}`,
        success: function (data) {
            if ('Success' in data) {
                pwIsf.alert({ msg: data.Success, type: 'info' });
                let autoSenseInputDataArr = [];
                let tempArr = arrPauseAllAdhoc.filter(x => x.workOrderAutoSenseEnabled === true);
                $.each(tempArr, function (i, k) {
                    autoSenseInputDataArr.push(getAutoSenseObject(tempArr[i].woID, tempArr[i].flowChartDefID, '0', 'UI', signumGlobal, 'SUSPEND', '', '0', null, null));
                });
                $('.flow_chart_main_panel .panel-body > .flow_chart_single_box').remove();
                $('.flowChartInfoMsg').show();
                $('.on-wo-viewFlowchart').attr('disabled', false);
                // reload all opened tabs and update floating window
                NotifyClientOnPauseAllWO(autoSenseInputDataArr);
                changeDisabledManualStepColor(status);
            }
        },
        error: function (xhr, status, statusText) {
            console.log('Error in Pause all');

        }

    });
}
function UpdateNonWorkingHourBooking() {

    pwIsf.confirm({
        title: 'Adhoc Booking Confirmation', msg: "What Do you want to Update Planned or Actual Hour?",
        'buttons': {
            'Planned': { 'action': function () { SendDataForUpdateNonWorkingHourBooking("PLANNED"); } },
            'Actual': { 'action': function () { SendDataForUpdateNonWorkingHourBooking("ACTUAL"); } },
            'Cancel': { 'action': function () { } }
        }
    })

}
function SendDataForUpdateNonWorkingHourBooking(duration,IsCalledFromServerPusHub) {

   

    if (IsCalledFromServerPusHub == false || IsCalledFromServerPusHub == undefined || IsCalledFromServerPusHub == null) {

        var nonWorkingHourData = new Object();
        nonWorkingHourData.adhocBookingId = $('#adhocBookingID').val();
        nonWorkingHourData.bookedDuration = duration;
        nonWorkingHourData.lastModifiedBy = signumGlobal;
        nonWorkingHourData.comment = "STOP";
        pwIsf.addLayer({ text: "Please wait ..." });
        $.isf.ajax({
            url: `${service_java_URL}adhocActivity/saveAdhocBooking`,
            context: this,
            crossdomain: true,
            processData: true,
            contentType: 'application/json',
            type: 'POST',
            data: JSON.stringify(nonWorkingHourData),
            xhrFields: {
                withCredentials: false
            },
            success: function (data) {
                pwIsf.removeLayer();
                adhocBookingStatus = false;
                $('#adhocBox').hide();
                adhocoverlapGone();
                $('.adhocBooking_container2').html('').empty();
                if (data.apiSuccess === false && data.responseMsg != null) {
                    pwIsf.alert({ msg: data.responseMsg, type: 'warning', });
                    location.reload();
                }
                else {
                    stopWorker();
                    pwIsf.alert({ msg: "Adhoc booking hours have been logged", type: 'success' });
                }
                timerObjAdhoc = [];
                timerObjAdhocTotalEffort = [];

            },
            error: function (xhr, status, statusText) {
                pwIsf.removeLayer();
                console.log('An error occurred on : Update adhoc booking' + xhr.error);
            }
        });

        LaunchFloatingWindow(null,false);



    }
    else {
      
        $('#adhocBox').hide();
        $('.adhocBooking_container2').html('').empty();
        stopWorker();
        timerObjAdhoc = [];
        timerObjAdhocTotalEffort = [];
    }
    $('div.leftRoundCorner').show();
    $('div.rightRoundCorner').show();
    $('div.adhocActivities').show();


}
   
function truncate(string,length) {
    if (string.length > length)
        return `${string.substring(0, length)}...`;
    else
        return string;
};

function StartNonWorkingRecordingPopUp(data) {
    $('#adhocBookingID').val(data.id);
    adhocBookingStatus = true;
    var activityType = data.actType;
    var durationPlanned = Number(data.plannedDuration);
    var durationActual = Number(data.actualDuration);
    var clockIdPlanned = "clockIdPlannedEffort";
    var clockIdActual = "clockIdActualEffort";
    var activityTypeFinal = truncate(activityType,20);
    var dBox = `<div style="color:white;font-weight: bold;">
        <span >Activity :</span> <span title="${activityType}(${data.name})"class="val">${activityTypeFinal}(${data.name})</span>
        </div>
        <span style="white-space:nowrap;color:white;font-weight: bold;">Actual Effort : </span>
        <div class="time_area" style="display:inline;">
        <div id="clockPlannedEffort" style="display:inline;">
        <div class="clockdate-wrapper" style="display:inline;">
        <div id="${clockIdActual}" class="clock" style="display:inline;font-size:14px;color:#3CFE33;font-weight: bold;" ></div>
        </div></div>
        </div>`;
    timerObjAdhoc.push({ "elapsedTime": durationActual, "clockId": clockIdActual });
    timerObjAdhocTotalEffort.push({ "elapsedTimeTotalEffort": durationPlanned, "clockIdTotalEffort": clockIdPlanned });

    var passParam = {};
    passParam.htm = dBox;
    passParam.timerObjAdhoc = timerObjAdhoc;
    passParam.timerObjAdhocTotalEffort = timerObjAdhocTotalEffort;
    bindElementOfAdhocBooking(passParam);
    adhocoverlapGone();
    $("#adhocActivity").children().remove();
    $("#adhocActivity").toggle();
    $("li#updatedAdhocPanel").find('div').css('display', 'none !important');
    $('div.leftRoundCorner').hide();
    $('div.rightRoundCorner').hide();
    $('div.adhocActivities').hide();

}

function checkTime(i) {
    if (i < 10)
        i = "0" + i;
    return i;
}

function stopWorker() {
    timerWorker.terminate();
    timerWorker = undefined;
}

function startTimeManualAdhoc(execTimeInSeconds, clockId, startDateAdhoc) {

    //Check if browser supports Web Workers
    if (typeof (Worker) !== "undefined") {        
        if (typeof (timerWorker) == "undefined") {
            timerWorker = new Worker(rootDir + '/Scripts/WebWorkers/timerWebWorker.js');            
        }

        let AdhocTimer = new Object();
        AdhocTimer.elapsedTime = execTimeInSeconds;
        AdhocTimer.startDateAdhoc = startDateAdhoc;

        timerWorker.postMessage(AdhocTimer);

        timerWorker.onmessage = function (e) {

            if (document.getElementById(clockId)) {
                document.getElementById(clockId).innerHTML = e.data;
            }
        }
    }
    else {
        document.getElementById(clockId).innerHTML = "Sorry! No Web Worker support.";
    }
}

function bindElementOfAdhocBooking(param) {

    let startDateAdhoc = new Date();
    $('#adhocBox').show();
    $('.adhocBooking_container2').html('').append(param.htm);
    for (var i = 0; i < param.timerObjAdhoc.length; i++) {

        startTimeManualAdhoc(param.timerObjAdhoc[i].elapsedTime, param.timerObjAdhoc[i].clockId, startDateAdhoc);
    }
}

function getAdhocBookingDetail(IsPageRefresh) {
    $.isf.ajax({
        url: `${service_java_URL}activityMaster/getAdhocBookingProject/${signumGlobal}`,
        success: function (data) {
            if (data.responseData != null && data.isValidationFailed === false) {
                LaunchFloatingWindow(data.responseData, true, IsPageRefresh);
                StartNonWorkingRecordingPopUp(data.responseData);
            }
        },
        cache: false,
        error: function (xhr, status, statusText) {
            console.log(`Fail in  getAdhocBookingProject ${xhr.responseText}`);
            console.log('Error Occured in getAdhocBookingProject');
        }
    });
}

function LaunchFloatingWindow(data, IsAdhocStarted, IsPageRefresh) {

    var IsFloatingWindowRunning = localStorage.getItem("IsFloatingWindowRunning");

    if (IsFloatingWindowRunning == undefined || IsFloatingWindowRunning == null || IsFloatingWindowRunning == "") {
        localStorage.setItem("IsFloatingWindowRunning", true);
        var url = 'FloatingWindow:exec_' + signumGlobal;
        setTimeout(function () {
            window.open(url, '_self');
        }, 2000);
        if (InitiateSignalRConnection() == true && (IsPageRefresh == undefined || IsPageRefresh == null || IsPageRefresh == false)) {
            UpdateFloatingWindowServeMethods(false, false);
            UpdateAdhocTimerOnMultipleTabs(data, IsAdhocStarted)
        }
    }
    else {
        if (InitiateSignalRConnection() == true && (IsPageRefresh == undefined || IsPageRefresh == null || IsPageRefresh == false)) {
            UpdateFloatingWindowServeMethods(false, true);
            UpdateAdhocTimerOnMultipleTabs(data, IsAdhocStarted)
        }
    }
}

function freezeAllPage() {

    $("#onMainHeader_AdhocTimeBooking").prop("disabled", true);

    $(".inprogressTask_panel *").prop("disabled", true);
    $(".work_order_panel *").prop("disabled", true);

    $('#recordPlannedAdhocBooking').prop("disabled", false);
    $('#recordActualAdhocBooking').prop("disabled", false);
    $('.action-buttons').hide();
    $('.action_area').hide()

}

function releaseAllPage() {

    $("#onMainHeader_AdhocTimeBooking").prop("disabled", false);
    $(".inprogressTask_panel :input,select,textarea,button,a").prop("disabled", false);
    $(".inprogressTask_panel *").prop("disabled", false);
    $(".work_order_panel *").prop("disabled", false);

    $('#recordPlannedAdhocBooking').prop("disabled", true);
    $('#recordActualAdhocBooking').prop("disabled", true);

    $('.action-buttons').show();
    $('.action_area').show()
}

function getAdhocActivityIcons() {
        $.isf.ajax({
            url: `${service_java_URL}adhocActivity/getAdhocActivities`,
            success: function (responseData) {
                if (responseData.length > 1) {
                    $.each(responseData, function (i, d) {
                        getAnchorIcon(d);
                    });
                    localStorage.setItem('adhocType', JSON.stringify(responseData));
				}
            },
            error: function (xhr, status, statusText) {
                console.log(`An error occurred for adhocActivity/getAdhocActivities: ${xhr.responseText}`);
            }
        });
}
function getAnchorIcon(data) {
    const listItem = $('<div/>');
    let anchorEle = $(`<a href="#" title="${data.activity}" data-name="${data.name}" 
        class="dropdown-toggle" data-toggle="dropdown"  aria-haspopup="true" aria-expanded="false"
        data-activity="${data.activity}" style="margin-left:3px" data-sequence="${data.sequence}" 
        onclick="getProjectsActivity('${data.activity}','${data.sequence}')" />`);
    let iconHtml = '';
    if (data.activity === 'Non WO Adhoc Booking') {
        anchorEle = $(`<a href="#" title="${data.activity}" data-name="${data.name}" 
        class="dropdown-toggle" data-toggle="dropdown"  aria-haspopup="true" aria-expanded="false"
        data-activity="${data.activity}" style="margin-left:7px" data-sequence="${data.sequence}" 
        onclick="getProjectsActivity('${data.activity}','${data.sequence}')" />`);
        if (path.length === 3) {
            iconHtml = $('<img />').attr({
                src: `../${UiRootDir}/Content/images/tasks.svg`,
            }).css('height', '17px');
        } else {
            iconHtml = $('<img />').attr({
                src: `../Content/images/tasks.svg`,
            }).css('height', '17px');
        }
        listItem.addClass('leftRoundCorner');
    } else if (data.activity === 'Non WO Meeting Booking') {
        iconHtml = $('<i class="fa fa-calendar fa-lg" aria-hidden="true" style="line-height: .6em;"></i>');
        listItem.addClass('adhocActivities');
    } else if (data.activity === 'Non WO Competence/Training Booking') {
        iconHtml = $('<i class="fa fa-trophy fa-lg" aria-hidden="true" style="line-height: .6em;"></i>');
        listItem.addClass('adhocActivities');
    } else if (data.activity === 'Non WO Automation Booking') {
        iconHtml = $('<i class="fa fa-rocket fa-lg" aria-hidden="true" style="line-height: .6em;"></i>');
        listItem.addClass('rightRoundCorner');
    }
    $(anchorEle[0]).append(iconHtml[0])
    $(listItem[0]).append(anchorEle[0]);
    $(listItem[0]).append('<br>');
    $(listItem[0]).append(`<span style="font-size:11px"><h9> ${data.abbreviation}</h9></span>`);
    $('#updatedAdhocPanel').append(listItem[0]);
}
function getProjectsActivity(act,seq) {
    event.preventDefault();
    event.stopPropagation();
    $(ullistItemId).css('display', 'none');
    const activeProfileObj = JSON.parse(ActiveProfileSession);
    $.isf.ajax({
        url: `${service_java_URL}projectManagement/getAdhocTypes?signum=${signumGlobal}&role=${activeProfileObj.role}&activity=${act}`,
        success: function (responseData) {
            $(`${ullistItemId} li`).remove();
            if (responseData.length > 0) {
                let listItemElement = '';
                let projNameTruncated = '';
                let fullProjectName = '';
                $.each(responseData, function (i, d) {
                    fullProjectName = `${d.type}`;
                    projNameTruncated = truncate(fullProjectName, 20);
                    listItemElement += getAdhocListItem(fullProjectName, projNameTruncated, d);
                });
                $(ullistItemId).append(listItemElement);
                $(ullistItemId).css('display', 'block');
                $('.x').hide();
                $('.x').removeClass("displayInline");
                $('.x').removeClass("displayBlock");
                $('.x').css("display", "none !important");
                $('#notification').hide();
                $('#switchProfile').removeClass('open');
            }
        },
        error: function (xhr, status, statusText) {
            console.log(`An error occurred : ${xhr.responseText}`);
        }
    });
}
function getAdhocListItem(fullProjectName, projNameTruncated, d) {
    const arradhocType = d.type.split('-');
    if (arradhocType[0].toString().toLowerCase() === _InternalActivity) {
        arradhocType[0] = "0";
	}
    return `<li onhover="changeAdhocCSS()">
              <a title="${fullProjectName}" onclick="CheckForParallelAdhocandWO(this);" 
                style="text-decoration: none;font-weight: bold;margin-left: 0px;" href="#" data-adhocactivityid="${d.id}" data-value="${arradhocType[0]}">
                     <i class="fa fa-tags"></i>${projNameTruncated}
               </a>
      </li>`;
}
