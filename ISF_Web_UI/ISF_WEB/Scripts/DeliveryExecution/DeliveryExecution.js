var progressWorkOrderDetails = "";
var runningTaskDetails = "";
var selectedSignum = "";
const C_VIEW_BOOKING_DETAIL_TABLE = '#viewBookingDetailsTable';
const C_ERROR_GET_PROJECT_NAME = 'An error occurred on getProjectName: ';
const C_MODAL_ID =".modal-body #woId";

function refreshIframe(val, url) {
    var $iframe = $('#iframe' + val);
    if ($iframe.length) {
        $iframe.attr('src', url);
        return false;
    }
}

//load EditPage in Same Iframe
function loadWF(woId, subActId, prjID,version) {
    pwIsf.addLayer({ text: 'Please wait ...' });
    const wFUrl = `FlowChartExecution?mode=execute&version=${version}&woID=${woId}&subActID=${subActId}&prjID=${prjID}`;
    var loadurl = window.location.href.split("ProgAssignments")[0] + wFUrl;
    $('#iframe' + woId).attr('src', loadurl);
}

function getInProgressWorkOrderDetails(signum) {
    pwIsf.addLayer({ text: "Please wait ..." });
    $.isf.ajax({
        async:false,
        url: `${service_java_URL}woExecution/getInProgressWorkOrderDetails/${signum}`,
        success: function (data){   
            progressWorkOrderDetails = data.data;
            if (progressWorkOrderDetails.length) {
            var workOrderDetails = "";
                for (var i in progressWorkOrderDetails) {
                    const loadWFUrl = `${progressWorkOrderDetails[i].wOID},${progressWorkOrderDetails[i].subActivityID},
                    ${progressWorkOrderDetails[i].projectID},${progressWorkOrderDetails[i].versionNO}`;
                var taskLength = progressWorkOrderDetails[i].listOfStepTaskDetails.length;
                var eachWorkOrder = '<div class="row">'+
                '<div class="panel panel-default" style="margin-bottom:7px;" >' +
                    '<div class="panel-heading" id="wo_goto_' + progressWorkOrderDetails[i].wOID+ '">' +
                        '<div class="col-md-6" style="display:flex; margin-left:35%;">'+
                    '< a data-toggle="collapse" class="load-wf collapsed" data-loadiframe="loadWF(' + loadWFUrl + ')" style = "font-weight:bold;" href = "#WODiv' + progressWorkOrderDetails[i].wOID + '" >' +
                    'Work Order ID:' + progressWorkOrderDetails[i].wOID + '</a >' +
                    '<div class="divnowrap col-md-4"><span title="' + progressWorkOrderDetails[i].nodeNames + '"> (Node Name: ' + progressWorkOrderDetails[i].nodeNames + ')</span></div></div > ' +
                '<span style="float:right;margin-right:8px;display:flex;">' +
                        '<a href="#" style="margin-right:10px;" title="Booking Details" class="view-booking-details" data-toggle="modal" data-target="#viewBookingDetails"'+
                    'data - workid="' + progressWorkOrderDetails[i].wOID + '" > <i class="fa fa-edit"></i></a > ' +
                    '<a href="#" style="margin-right:10px;" onclick="flowChartOpenInNewWindow(' + ' \'FlowChartExecution\?mode\=execute&version=' + progressWorkOrderDetails[i].versionNO +
                        '&woID=' + progressWorkOrderDetails[i].wOID + '&subActID=' + progressWorkOrderDetails[i].subActivityID + '&prjID=' + progressWorkOrderDetails[i].projectID + '\' ' + ')">'+
                        '< span class="fa fa-list" data - toggle="tooltip" title = "View Work Flow" style = "color:blue" ></span ></a > ' +
                '<div class="dropdown"><a href="#" data-toggle="dropdown"><span class="fa fa-toggle-down"></span></a>' +
                '<ul class="dropdown-menu" role="menu" style="left:-164px!important;">' +
                '<li><a href="#" class="view-duplicate" data-id="' + progressWorkOrderDetails[i].wOID + '">Duplicate WO</a></li>' +
                '<li><a href="#" data-toggle="modal" data-target="#myModalMadEwo" data-workorder-id="' + progressWorkOrderDetails[i].wOID + '" class="viewWorkOrder">Edit / View Work Order</a></li>';
                if (progressWorkOrderDetails[i].status !== 'ONHOLD')
                    eachWorkOrder += '<li><a href="#" class="view-hold" data-id="' + progressWorkOrderDetails[i].wOID + '">Mark As On Hold</a ></li >';
                var markCompleteStatus = "";
                var l = taskLength - 1;
                for (var k in progressWorkOrderDetails[i].listOfStepTaskDetails[l].lstBookingDetailsModels) {
                    if ((progressWorkOrderDetails[i].listOfStepTaskDetails[l].lstBookingDetailsModels[k].bookingID > bookingID) && progressWorkOrderDetails[i].listOfStepTaskDetails[l].lstBookingDetailsModels[k].type == "BOOKING") {
                        markCompleteStatus = progressWorkOrderDetails[i].listOfStepTaskDetails[l].lstBookingDetailsModels[k].status;
                    }
                }
                if (markCompleteStatus !== 'STARTED' && markCompleteStatus !== 'ONHOLD') {
                    eachWorkOrder += '<li><a href="#" class="view-completed" data-toggle="modal" data-woid="' + progressWorkOrderDetails[i].wOID + '"' + 'data-woname="' + progressWorkOrderDetails[i].wOName.replace(/\"/g, "\\\"") + '"' + ' data-subactdefid="' + progressWorkOrderDetails[i].subActivityFlowChartDefID + '">Mark As Completed</a></li>';
                }
                eachWorkOrder += '<li><a href="#" class="view-deffered" data-toggle="modal" data-target="#modalDeffered" data-woid="' + progressWorkOrderDetails[i].wOID + '"> Mark As Deferred</a ></li >' +
                    '<li><a href="#" class="view-transferWO" data-toggle="modal" data-target="#modalTransferWO" data-woid="' + progressWorkOrderDetails[i].wOID + '">Transfer Work Order</a ></li >' +
                    '</ul ></div >' +
                    '</span>' +
                    '</div>';
                    eachWorkOrder += '<div class="panel-body collapse" id="WODiv' + progressWorkOrderDetails[i].wOID + '">';
                 var woinfo = '<h5> Work Order - ' + progressWorkOrderDetails[i].wOID + '  ' + progressWorkOrderDetails[i].wOName + ' ' + '( ' + '<strong>Work Flow Name</strong> : ' + progressWorkOrderDetails[i].wFName + ' )';
                 woinfo += '</h5><div class="row">' +
                '<div class="col-md-3"><strong>Node Type: </strong>' + progressWorkOrderDetails[i].nodeType + '</div>' +
                 '<div class="col-md-2"><strong>Node Count: </strong>' + progressWorkOrderDetails[i].nodeCount + '</div>' +
                    '<div class="col-md-4 divnowrap"><strong>Node Name: </strong>' + progressWorkOrderDetails[i].nodeNames + '</div>';
                if (progressWorkOrderDetails[i].actualStartDate !== null)
                    woinfo += '<div class="col-md-3"><strong>Started On: </strong>' + (new Date(progressWorkOrderDetails[i].actualStartDate)).toLocaleDateString() + '</div>';
                else
                    woinfo += '<div class="col-md-3"><strong>Started On: </strong>' + progressWorkOrderDetails[i].actualStartDate + '</div>';
                woinfo += '</div>' +
                '<div class="row">';
                if (progressWorkOrderDetails[i].actualEndDate !== null)
                    woinfo += '<div class="col-md-3"><strong>Closed On: </strong>' + (new Date(progressWorkOrderDetails[i].actualEndDate)).toLocaleDateString() + '</div>';
                else
                    woinfo += '<div class="col-md-3"><strong>Closed On: </strong>' + progressWorkOrderDetails[i].actualEndDate + '</div>';
                woinfo += '<div class="col-md-2"><strong>Digital Efforts: </strong>' + progressWorkOrderDetails[i].automatedHours + '</div>' +
                    '<div class="col-md-4"><strong>Manual Efforts: </strong>' + progressWorkOrderDetails[i].manualHours + '</div > ' +
                    '<div class="col-md-3"><strong>Total Efforts: </strong>' + progressWorkOrderDetails[i].totalHours + '</div>' +
                '</div><hr />';
                woinfo += '<div class="row">' +
                  '<div class="col-md-2">Started<span style="margin-left: 5px ;padding: 3px 10px 3px 10px;background-color:#6e88ff;"></span></div>' +
                  '<div class="col-md-2">Completed<span style=" margin-left: 5px ;padding: 3px 10px 3px 10px;background-color:#00983b;"></span></div>' +
                  '<div class="col-md-2">On Hold<span style="margin-left: 5px ;padding: 3px 10px 3px 10px;background-color:#E5E500 ;"></span></div>' +
                  '<div class="col-md-2">Skipped<span style=" margin-left: 5px ;padding: 3px 10px 3px 10px;background-color:#FFAB73 ;"></span></div>' +
                  '<div class="col-md-2">Manual<span style=" margin-left: 5px ;padding: 3px 17px 3px 17px;background-color:#dcd7d7;border: 1px solid #31d0c6 ;"></span></div>' +
                  '<div class="col-md-2" style="width:155px">Automatic<span style="margin-left: 5px;padding: 1px 4px 4px 4px;background-color:#ffffff;border: 1px solid red;"><span style=" margin-left: 0px ;padding: 1px 15px 1px 15px;background-color:#31d0c6;border: 1px solid red"></span></span></div>' +
              '</div><br/>';
                var contentWF = '<div id="divWfInfo">' +
                    '<div style="display:block;text-align: center;"><a href="javascript:void(0)" onclick="$(\'#wOdetailForWFprog' + i + '\').slideToggle();" style="font-size:10px;color: #007aff;text-decoration:underline;">WORK FLOW INFO <i class="fa fa-info-circle" style="font-size: 17px;color: #648ae2;margin-left: 6px;vertical-align: middle;"></i></a></div>' +
                  '<div id="wOdetailForWFprog' + i + '" style="display:none;">' + woinfo +'</div>'+
                  '</div>';
                eachWorkOrder += contentWF + '<br />';
                eachWorkOrder += '<div class="row">' +
                     '<ul class="nav nav-tabs" id="nav_barWOView' + progressWorkOrderDetails[i].woID + '">' +
                        '<li class="active"><a data-toggle="tab" data-target="#WFView' + progressWorkOrderDetails[i].wOID + '" class="view-wf-pane" href="#WFView' + progressWorkOrderDetails[i].wOID + '">Work Flow</a></li>' +
                        '<li class=""><a data-toggle="tab" data-target="#TaskView' + progressWorkOrderDetails[i].wOID + '" class="view-task-pane" href="#TaskView' + progressWorkOrderDetails[i].wOID + '">Task List</a></li>' +
                    '</ul></div>'
                eachWorkOrder += '<div class="tab-content" style="padding-top:10px;">';
                if (i == 0) {
                    eachWorkOrder += '<div class="tab-pane fade in active" id="WFView' + progressWorkOrderDetails[0].wOID + '">' +
                                             '<iframe id="iframe' + progressWorkOrderDetails[0].wOID + '" src="' + window.location.href.split("ProgAssignments")[0] + 'FlowChartExecution?mode=execute&woID=' + progressWorkOrderDetails[0].wOID + '&subActID=' + progressWorkOrderDetails[0].subActivityID + '&prjID=' + progressWorkOrderDetails[i].projectID + '" style="width:100%;height:475px;border: none;"></iframe>'
                                            + '</div>';
                }
                else {
                    eachWorkOrder += '<div class="tab-pane fade in active" id="WFView' + progressWorkOrderDetails[i].wOID + '">' +
                                             '<iframe id="iframe' + progressWorkOrderDetails[i].wOID + '" style="width:100%;height:475px;border: none;"></iframe>'
                                            + '</div>';
                }
                eachWorkOrder += '<div id="TaskView' + progressWorkOrderDetails[i].wOID + '" class="tab-pane fade">'
                for (var j in progressWorkOrderDetails[i].listOfStepTaskDetails) {
                    var bookingID = 0;
                    var bookingType = "";
                    var bookingStatus = "";
                    for (var m in updateTaskStatus[i].listOfStepTaskDetails[j].listOfTaskModel) {
                        for (var k in progressWorkOrderDetails[i].listOfStepTaskDetails[j].listOfTaskModel[m].lstBookingDetailsModels) {
                            if ((progressWorkOrderDetails[i].listOfStepTaskDetails[j].listOfTaskModel[m].lstBookingDetailsModels[k].bookingID > bookingID) && progressWorkOrderDetails[i].listOfStepTaskDetails[j].listOfTaskModel[m].lstBookingDetailsModels[k].type === "BOOKING") {
                                bookingID = progressWorkOrderDetails[i].listOfStepTaskDetails[j].listOfTaskModel[m].lstBookingDetailsModels[k].bookingID;
                                bookingType = progressWorkOrderDetails[i].listOfStepTaskDetails[j].listOfTaskModel[m].lstBookingDetailsModels[k].type;
                                bookingStatus = progressWorkOrderDetails[i].listOfStepTaskDetails[j].listOfTaskModel[m].lstBookingDetailsModels[k].status;
                            }
                        }
                        eachWorkOrder += '<div class="row">' +
                            '<div class="col-md-11"><p class="well well-sm"><a href="#" class="view-task" data-toggle="modal" data-target="#modalTaskBookingView" data-workid="' + progressWorkOrderDetails[i].wOID + '"' + 'data-taskid="' + progressWorkOrderDetails[i].listOfStepTaskDetails[j].listOfTaskModel[m].taskID + '" data-stepid="' + progressWorkOrderDetails[i].listOfStepTaskDetails[j].stepID + '">' + progressWorkOrderDetails[i].listOfStepTaskDetails[j].stepName + '_' + progressWorkOrderDetails[i].listOfStepTaskDetails[j].stepID + '<br>' + progressWorkOrderDetails[i].listOfStepTaskDetails[j].listOfTaskModel[m].task + ' -> ' + progressWorkOrderDetails[i].listOfStepTaskDetails[j].listOfTaskModel[m].executionType + '</a></p></div><div class="col-md-1 ">';
                        var start = '<a href="#" onclick="startTask(' + progressWorkOrderDetails[i].wOID + ',' + progressWorkOrderDetails[i].listOfStepTaskDetails[j].listOfTaskModel[m].taskID + ',' + progressWorkOrderDetails[i].subActivityFlowChartDefID + ',\'' + progressWorkOrderDetails[i].listOfStepTaskDetails[j].listOfTaskModel[m].executionType.trim() + '\',' + progressWorkOrderDetails[i].listOfStepTaskDetails[j].stepID + ')" id="play"><span class="fa fa-play" style="color:blue" data-toggle="tooltip" title="START"></span></a>';
                        var stop = '<a href="#" onclick="stopTask(' + progressWorkOrderDetails[i].wOID + ',' + progressWorkOrderDetails[i].listOfStepTaskDetails[j].listOfTaskModel[m].taskID + ',' + bookingID + ',' + progressWorkOrderDetails[i].listOfStepTaskDetails[j].stepID + ',' + progressWorkOrderDetails[i].subActivityFlowChartDefID + ')" id="stop"><span class="fa fa-stop" data-toggle="tooltip" title="Stop" style="color:orangered;margin-left:5px;"></span></a>';
                        var onHold = '<a id="pause" href="#" class="view-taskHold" data-taskname="' + progressWorkOrderDetails[i].listOfStepTaskDetails[j].listOfTaskModel[m].task + '"' + 'data-workidb="' + progressWorkOrderDetails[i].wOID + '"' + 'data-taskidb="' + progressWorkOrderDetails[i].listOfStepTaskDetails[j].listOfTaskModel[m].taskID + '"' + 'data-bookid="' + bookingID + '"' + '><span class="fa fa-pause" data-toggle="tooltip" title="On Hold" style="color:black;margin-left:5px;"></span></a>';
                        var skip = '<a href="#" class="view-skip" data-taskname="' + progressWorkOrderDetails[i].listOfStepTaskDetails[j].listOfTaskModel[m].task + '"' + 'data-workidb="' + progressWorkOrderDetails[i].wOID + '"' + 'data-taskidb="' + progressWorkOrderDetails[i].listOfStepTaskDetails[j].listOfTaskModel[m].taskID + '"' + 'data-bookid="' + bookingID + '"' + '> <span class="fa fa-forward" data-toggle="tooltip" title="Skip" style="color:black;margin-left:5px;"></span></a>';
                        var start = '';
                        var stop = '';
                        var onHold = '';
                        var skip = '';
                    }
                    var completed = '<a id="saved"><span class="fa fa-check" data-toggle="tooltip" title="Completed" style="color:green;padding-right:5px;"></span></a>';
                    var skipSign = '<a id="skipsign"><span class="fa fa-ban" data-toggle="tooltip" title="Skipped" style="color:red;padding-right:5px;"></span></a>';
                    var holdSign = '<a id="holdsign"><span class="fa fa-info" data-toggle="tooltip" title="On Hold" style="color:red;padding-right:5px;"></span></a>';
                    var completed = '';
                    var skipSign = '';
                    var holdSign = '';
                    if (bookingID == 0) {
                        eachWorkOrder += start + skip;
                    }
                    else {
                        if (bookingID != 0 && bookingType == "BOOKING" && bookingStatus == "STARTED") {
                            if (progressWorkOrderDetails[i].listOfStepTaskDetails[j].executionType == "Automatic")
                                eachWorkOrder += stop + skip;
                            else
                                eachWorkOrder += onHold + stop + skip;
                        }
                        if (bookingID != 0 && bookingType == "BOOKING" && bookingStatus == "ONHOLD") {
                            eachWorkOrder += holdSign + start + skip;
                        }
                        if (bookingID != 0 && bookingType == "BOOKING" && bookingStatus == "COMPLETED") {
                            eachWorkOrder += completed + start;
                        }
                        if (bookingID != 0 && bookingType == "BOOKING" && bookingStatus == "SKIPPED") {
                            eachWorkOrder += skipSign + start;
                        }
                    }
                    eachWorkOrder += '</div></div>';
                }
                eachWorkOrder += '</div ></div ></div ></div></div>';

                workOrderDetails += eachWorkOrder;
            }
            $('#workOrderDetails').append(workOrderDetails);
            workOrderMenus();

            bindClickEventOnAction(); //Bind for view work order
            }
            else {
                $('#workOrderDetails').append('<h4  style="padding-top:5px;text-color:red;" class="text-center">' + data.msg + '</h4>');
            }
        },
        error: function (xhr, status, statusText) {
            var err = JSON.parse(xhr.responseText);
            $('#workOrderDetails').append('<h4  style="padding-top:5px;text-color:red;" class="text-center">' + err.errorMessage + '</h4>');
            
        },
        complete: function (xhr, statusText) {
            pwIsf.removeLayer();

            $('#workOrderDetails > .row').on('show.bs.collapse', function () {
                var codeToExecute = $(this).find('.load-wf').attr('data-loadiframe');
                var tmpFunc = new Function(codeToExecute);
                tmpFunc();
            });
        }
    });
}

function runningWorkOrderDetails(signum) {
    arrPauseAll = [];
    $.isf.ajax({
        async: false,
        url: `${service_java_URL}woExecution/getCurrentWorkOrderDetails/${signum}0`,
        success: function (data) {
            runningTaskDetails = data;
            $('#autoRunningTask').empty();
            $('#runningTask').empty();
            if (data.length !== 0) {
                $('.pauseAll').css('display', 'block');
            }
            else {
                $('.pauseAll').css('display', 'none');
            }  
            for (var i = 0; i < runningTaskDetails.length; i++) {
                var clockId = runningTaskDetails[i].wOID;
                for (var j in runningTaskDetails[i].listOfStepTaskDetails[0].listOfTaskModel) {
                    var eachWorkOrder = "";
                    var autoRunningTask = "";
                    var serverTime = (new Date(runningTaskDetails[i].serverTime)).getTime(); // in miliseconds
                    var elapseSeconds = (serverTime - runningTaskDetails[i].listOfStepTaskDetails[0].listOfTaskModel[j].lstBookingDetailsModels[0].startDate) / 1000; // in seconds
                    var bookingID = runningTaskDetails[i].listOfStepTaskDetails[0].listOfTaskModel[j].lstBookingDetailsModels[0].bookingID;
                    var bookingType = runningTaskDetails[i].listOfStepTaskDetails[0].listOfTaskModel[j].lstBookingDetailsModels[0].type;
                    var bookingStatus = runningTaskDetails[i].listOfStepTaskDetails[0].listOfTaskModel[j].lstBookingDetailsModels[0].status;
                    var elapsedTimeOfRunningTask = Math.floor(((runningTaskDetails[i].listOfStepTaskDetails[0].listOfTaskModel[j].lstBookingDetailsModels[0].hours) * 60 * 60) + elapseSeconds); // in seconds
                    var taskType = runningTaskDetails[i].listOfStepTaskDetails[0].listOfTaskModel[j].executionType.trim();
                    if (bookingStatus === 'STARTED' && taskType === "Manual") {
                        arrPauseAll.push({
                            'woID': runningTaskDetails[i].wOID, 'bookingID': bookingID, 'executionType': taskType,
                            'flowChartStepID': runningTaskDetails[i].listOfStepTaskDetails[0].stepID,
                            'taskID': runningTaskDetails[i].listOfStepTaskDetails[0].listOfTaskModel[j].taskID,
                            'flowChartDefID': runningTaskDetails[i].subActivityFlowChartDefID
                        });
                        clockId = `${taskType}_${clockId}`;
                    }   
                    if (elapsedTimeOfRunningTask ===0) {
                        var starttimeofruntask = runningTaskDetails[i].listOfStepTaskDetails[0].listOfTaskModel[j].lstBookingDetailsModels[0].startDate;
                        var servertime = new Date(runningTaskDetails[i].serverTime);
                        var stdate = new Date(starttimeofruntask);
                        var seconds = Math.floor((servertime.getTime() - stdate.getTime()) / (1000));
                        elapsedTimeOfRunningTask = seconds;
                    }
                    eachWorkOrder = '<div class="running-task">' +
                         '<div class="col-md-2 running-header"><h4><strong style="line-height:2.1;">' + taskType + ' Task</strong></h4></div>' +
                        '<div class="col-md-3 runningtask_name" style="overflow:hidden;white-space:nowrap;text-overflow:ellipsis;" title="' + runningTaskDetails[i].listOfStepTaskDetails[0].listOfTaskModel[j].task + '">' +
                        '<span style="font-weight:bold; line-height:3.1;">Name:  </span><span>' + runningTaskDetails[i].listOfStepTaskDetails[0].listOfTaskModel[j].task + '</span></div>' +
                        '<div class="col-md-2 runningtask_woid"><p style="white-space:nowrap;"><span style="font-weight:bold; line-height:3.1;">WOID:  </span>' +
                        '<a class="goThisWoId" title="Go to this WO" id="' + runningTaskDetails[i].wOID + '" href="#" style="text-decoration: underline;color: blue;">' + runningTaskDetails[i].wOID + '</a></p></div>' +
                         '<div class="col-md-2 runningtask_projid"><p style="white-space:nowrap;"><span style="font-weight:bold; line-height:3.1;">Proj:  </span>' + runningTaskDetails[i].projectID + '</p></div>';
                          if (bookingID !== 0 && bookingType === "BOOKING" && bookingStatus === "STARTED") {
                            eachWorkOrder += '<div class="col-md-1 runningtask_action" style="line-height:3.1;">'+
                          '<a href="#" onclick="InvokeStopFlowChartTask(' + runningTaskDetails[i].wOID + ',' + runningTaskDetails[i].subActivityFlowChartDefID + ',' + runningTaskDetails[i].listOfStepTaskDetails[0].stepID + ',' + runningTaskDetails[i].listOfStepTaskDetails[0].listOfTaskModel[j].taskID + ',false,' + bookingID + ', \'' + taskType.toUpperCase() + '\' )" id="stop"><span class="fa fa-stop" data-toggle="tooltip" title="Stop" style="color:orangered;"></span></a>' +
                            '<a href="#" class="view-skip" data-status-wf="SKIPPED" data-defid="' + runningTaskDetails[i].subActivityFlowChartDefID + '" data-stepid="' + runningTaskDetails[i].listOfStepTaskDetails[0].stepID + '" data-exectype="' + taskType + '" data-taskname="' + runningTaskDetails[i].listOfStepTaskDetails[0].listOfTaskModel[j].task + '"' + 'data-workidb="' + runningTaskDetails[i].wOID + '"' + 'data-taskidb="' + runningTaskDetails[i].listOfStepTaskDetails[0].listOfTaskModel[j].taskID + '"' + 'data-bookid="' + bookingID + '"' + '> <span class="fa fa-forward" data-toggle="tooltip" title="Skip" style="color:black;margin-left:5px;"></span></a>' +
                            '<a href="#" class="view-taskHold" data-status-wf="ONHOLD" data-defid="' + runningTaskDetails[i].subActivityFlowChartDefID + '" data-stepid="' + runningTaskDetails[i].listOfStepTaskDetails[0].stepID + '" data-exectype="' + taskType + '" data-taskname="' + runningTaskDetails[i].listOfStepTaskDetails[0].listOfTaskModel[j].task + '"' + 'data-workidb="' + runningTaskDetails[i].wOID + '"' + 'data-taskidb="' + runningTaskDetails[i].listOfStepTaskDetails[0].listOfTaskModel[j].taskID + '"' + 'data-bookid="' + bookingID + '"' + ' id="pause"><span class="fa fa-pause" data-toggle="tooltip" title="On Hold" style="color:black;margin-left:5px;"></span></a>' +
                             '<a href="#" class=""><span class="fa fa-info icon-size" data-toggle="tooltip" title="' + runningTaskDetails[i].listOfStepTaskDetails[0].listOfTaskModel[j].task + '" style="color:red;margin-left:5px;"></span></a>';
                        }
                        eachWorkOrder += '</div >';
                        eachWorkOrder += '<div class="col-md-2 runningtask_time">' +
                            '<div id="clockdate">' +
                            '<div class="clockdate-wrapper">' +
                            '<div id="' + clockId + '" class="clock"><h4></h4></div>' +
                            '</div></div></div>';
                        $('#runningTask').append(eachWorkOrder);
                        startTimeManual(elapsedTimeOfRunningTask, clockId);
                }
            }
            $('.goThisWoId').on('click', function (e) {
                e.preventDefault();
                var that = this;
                $('#WODiv' + that.id).collapse('show');

                $('html, body').animate({
                    scrollTop: $("#wo_goto_" + that.id).offset().top - 115
                }, 500);
             
            });   
        },
        error: function (xhr, status, statusText) {
            $('#autoRunningTask').empty();
            $('#runningTask').empty();
            var err = JSON.parse(xhr.responseText);
            if (err.errorMessage == "No Data exists for selected Signum")
                var errorTask = '<div class="running-task"><div class="text-center running-header" style="width:100%;"><h4><strong style="line-height:2.1;">No Task Is Running</strong></h4></div></div>'
            $('#runningTask').append(errorTask);
        },
        complete: function (xhr, statusText) {
            fixRunningTask(); // reset fixed strip of running tasks
        }
    });
}

function startTimeManual(execTimeInSeconds, clockId) {
    var totalSeconds = execTimeInSeconds;
    var hours = Math.floor(totalSeconds / 3600);
    var minutes = Math.floor((totalSeconds - (hours * 3600)) / 60);
    var seconds = totalSeconds - (hours * 3600) - (minutes * 60);
    min = checkTime(minutes);
    sec = checkTime(seconds);
    hr = checkTime(hours);
    document.getElementById(clockId).innerHTML = hr + " : " + min + " : " + sec;
    var time = setTimeout(function () { startTimeManual(totalSeconds + 1, clockId) }, 1000);
}

function startTimeAutomatic(execTimeInSeconds, clockId) {
    var totalSeconds = execTimeInSeconds;
    var hours = Math.floor(totalSeconds / 3600);
    var minutes = Math.floor((totalSeconds - (hours * 3600)) / 60);
    var seconds = totalSeconds - (hours * 3600) - (minutes * 60);
    min = checkTime(minutes);
    sec = checkTime(seconds);
    hr = checkTime(hours);
    document.getElementById(clockId).innerHTML = hr + " : " + min + " : " + sec;
    var time = setTimeout(function () { startTimeAutomatic(totalSeconds + 1, clockId) }, 1000);
}

function checkTime(i) {
    if (i < 10)
        i = "0" + i;
    return i;
}

$('[data-toggle="tooltip"]').tooltip();

function workOrderMenus() {
    var today = new Date();
}

function updateWOStatus(woID, markStatus, comment) {
    if (comment == null || comment.length === 0) {
       pwIsf.alert({ msg: 'Please give Reason ...' ,type:'warning'});
    }
    else if (woID == null || woID.length === 0 || markStatus == null || markStatus.length === 0 || markStatus.length >= 500|| signumGlobal == null || signumGlobal.length === 0) {
        pwIsf.alert({ msg: 'Something went wrong...', type: 'error' });
    }
    else if (comment.length > 1000) {
        pwIsf.alert({ msg: 'Comment should be less than 1000 characters', type: 'error' });
    }
    else {
        $.isf.ajax({
            type: "POST",
            url: `${service_java_URL}woExecution/updateWorkOrderStatus/${woID}/${signumGlobal}/${markStatus}/${comment}`,
            success: function (data) {
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

$(document).on("click", ".view-completed", function () {
    var woId = $(this).data('woid');
    var woName = $(this).data('woname');
    var subActDefId = $(this).data('subactdefid');
    var projId = $(this).data('projid');
    var subactivityid = $(this).data('subactivityid');
    var wfId = $(this).data('wfid');

    $(".modal-body #workOrderNameComp").val(woName);
    $(".modal-body #workOrderIDComp").val(woId);
    $(".modal-body #subActivityDefID").val(subActDefId);
    $(".modal-body #subActivityId").val(subactivityid);
    $(".modal-body #wfId").val(wfId);
    $(".modal-body #completedProjectId").val(projId);

    $(".modal-body #commentCompleted").val("");
    $(".modal-body #reasonDiv").css("display", "none");
    $('#modalCompleted').modal('show');
 $('.modal-body #delStatus').val('Success').change('trigger');
});

$(document).on("click", ".view-hold", function () {
    var woId = $(this).data('id');
    $(".modal-body #workOrderIDHold").val(woId);
    $(".modal-body #commentHold").val("");
    $('#modalHold').modal('show');
});

$(document).on("click", ".view-deffered", function () {
    var woId = $(this).data('woid');
    $(".modal-body #workOrderIDDef").val(woId);
    $(".modal-body #commentDeffered").val("");
    $('#modalDeffered').modal('show');
});

$(document).on("click", ".view-transferWO", function () {
    var woId = $(this).data('woid');
    $(C_MODAL_ID).val(woId);
    $(".modal-body #cbxSignumAR").val("");
    getSignum();
    $('#modalTransferWO').modal('show');
});

$(document).on("click", ".view-duplicate", function () {
    var woId = $(this).data('id');
    $(".modal-body #workOrderIDDup").val(woId);
    $(".modal-body #commentDuplicate").val("");
    $('#modalDuplicate').modal('show');
});

$(document).on("click", ".view-skip", function () {
    var woId = $(this).data('workidb');
    var bookId = $(this).data('bookid');
    var taskId = $(this).data('taskidb');
    var taskName = $(this).data('taskname');
    var execType = $(this).data('exectype');
    var stepid = $(this).data('stepid');
    var defid = $(this).data('defid');

    $('#skippedHeader').find('p').remove().end();
    $('#skippedHeader').append(`<p>Skipped Task:${taskName}</p>`)
    $(C_MODAL_ID).val(woId);
    $(".modal-body #taskId").val(taskId);
    $(".modal-body #bookId").val(bookId);
    $(".modal-body #execType").val(execType);
    $(".modal-body #stepid").val(stepid);
    $(".modal-body #defid").val(defid);

    var reasonType = "DeliveryExecution";
    $.isf.ajax({
        url: `${service_java_URL}woExecution/getWOFailureReasons/${reasonType}`,
        success: function (data) {
            if (data.isValidationFailed === false) {
                $.each(data.responseData, function (i, d) {
                    $('#commentSkipped').append(`<option value="${d.failureReason}">${d.failureReason}</option>`);
                })
            }
            else {
                pwIsf.alert({ msg: data.formErrors[0], type: 'error' });
            }
        },
        error: function (xhr, status, statusText) {
            console.log(C_ERROR_GET_PROJECT_NAME + xhr.error);
        }  
    });

    $('#modalSkipped').modal('show');
});

$(document).on("click", ".view-taskHold", function () {
    var woId = $(this).data('workidb');
    var bookId = $(this).data('bookid');
    var taskId = $(this).data('taskidb');
    var taskName = $(this).data('taskname');
    var execType = $(this).data('exectype');
    var stepid = $(this).data('stepid');
    var defid = $(this).data('defid');
    $('#taskHoldHeader').find('p').remove().end();
    $('#taskHoldHeader').append(`<p>On Hold:${taskName}</p>`)
    $(C_MODAL_ID).val(woId);
    $(".modal-body #taskId").val(taskId);
    $(".modal-body #bookId").val(bookId);
    $(".modal-body #execType").val(execType);
    $(".modal-body #stepid").val(stepid);
    $(".modal-body #defid").val(defid);
    var reasonType = "DeliveryExecution";
    $.isf.ajax({
        url: `${service_java_URL}woExecution/getWOFailureReasons/${reasonType}`,
        success: function (data) {
            if (data.isValidationFailed === false) {
                $.each(data.responseData, function (i, d) {
                    $('#commentOnHold').append(`<option value="${d.failureReason}">${d.failureReason}</option>`);
                });
            }
            else {
                pwIsf.alert({ msg: data.formErrors[0], type: 'error' });
            }
        },
        error: function (xhr, status, statusText) {
            console.log(C_ERROR_GET_PROJECT_NAME + xhr.error);
        }
    });
    $('#modalTaskHold').modal('show');
});


$(document).on("click", ".pauseAll", function () { 
    var reasonType = "DeliveryExecution";
    $.isf.ajax({
        url: `${service_java_URL}woExecution/getWOFailureReasons/${reasonType}`,
        success: function (data) {
            if (data.isValidationFailed === false) {
                $.each(data.responseData, function (i, d) {
                    $('#commentPauseAll').append(`<option value="${d.failureReason}">${d.failureReason}</option>`);
                });
            }
            else {
                pwIsf.alert({ msg: data.formErrors[0], type: 'error' });
            }
        },
        error: function (xhr, status, statusText) {
            console.log(C_ERROR_GET_PROJECT_NAME + xhr.error);
        }
    });

    $('#modalPauseAll').modal('show');
});

$(document).on("click", ".view-booking-details", function () {
    $(C_VIEW_BOOKING_DETAIL_TABLE).find('div').remove().end();
    $(C_VIEW_BOOKING_DETAIL_TABLE).find('table').remove().end();
    $('#viewBookingDetailsHeader').find('p').remove().end();
    var workId = $(this).data('workid');
    var viewTaskHeader = "";
    var viewTaskDetailTable = "";
    if (progressWorkOrderDetails.length !==0) {
        for (var i in progressWorkOrderDetails) {
            if (progressWorkOrderDetails[i].wOID === workId) {
                viewTaskHeader = '<span style="font-weight:bold">WOORK ORDER ID :  </span>' + workId;
                viewTaskDetailTable =
                    '<table class="table table-striped">' +
                    '<thead><tr><th>Start Date</th><th>End Date</th><th>Hours</th><th>Type</th><th>Status</th><th>Signum</th></tr></thead><tbody>';
                for (var task in progressWorkOrderDetails[i].listOfStepTaskDetails) {
                    console.log(progressWorkOrderDetails[i].listOfStepTaskDetails);
                    var taskJson = progressWorkOrderDetails[i].listOfStepTaskDetails[task].listOfTaskModel[0];
                    var stepName = progressWorkOrderDetails[i].listOfStepTaskDetails[task].stepName;
                    var stepId = progressWorkOrderDetails[i].listOfStepTaskDetails[task].stepID;
                    viewTaskDetailTable += '<tr>';
                    viewTaskDetailTable += `<td colspan="6" style="text-align: center;"><b>STEP NAME:</b>${stepName}|${stepId}|${taskJson.executionType}</td>`;
                    viewTaskDetailTable += '</tr>';
                    for (var j in taskJson.lstBookingDetailsModels) {
                        if (taskJson.lstBookingDetailsModels[j].bookingID !== 0) {
                            var bookJson = taskJson.lstBookingDetailsModels[j];
                            var stFormatDate = null;
                            var endFormatDate = null;
                            if (bookJson.startDate != null) {
                                var stDate = new Date(bookJson.startDate);
                                stFormatDate = `${stDate.getFullYear()}-${checkTime(stDate.getMonth() + 1)}
                                -${checkTime(stDate.getDate())} ${checkTime(stDate.getHours())}
                               :${checkTime(stDate.getMinutes())}`;
                            }
                            if (bookJson.endDate != null) {
                                var endDate = new Date(bookJson.endDate);
                                endFormatDate = `${endDate.getFullYear()}-${checkTime(endDate.getMonth() + 1)}
                                -${checkTime(endDate.getDate())} ${checkTime(endDate.getHours())}
                                :${checkTime(endDate.getMinutes())}`;
                            }
                            viewTaskDetailTable += `<tr><td>${stFormatDate}</td>
                            <td>${endFormatDate}</td>
                            <td>${bookJson.hours}</td>
                            <td>${bookJson.type}</td>
                            <td>${bookJson.status}</td>
                            <td>${bookJson.signumID}</td></tr>`;
                        }
                    }
                }
                viewTaskDetailTable += '</tbody></table>';
            }
        }
    }
    $('#viewBookingDetailsHeader').append(`<p>${viewTaskHeader}</p>`);
    $(C_VIEW_BOOKING_DETAIL_TABLE).append(viewTaskDetailTable);
    $('#viewBookingDetails').modal('show');
});

$(document).on("click", ".view-task", function () {
    $('#viewTaskDetailTable').find('div').remove().end();
    $('#viewTaskDetailTable').find('table').remove().end();
    $('#viewTaskHeader').find('p').remove().end();
    var workId = $(this).data('workid');
    var taskId = $(this).data('taskid');
    var stepID = $(this).data('stepid');
    var viewTaskHeader = "";
    var viewTaskDetailTable = "";
    if (progressWorkOrderDetails.length !== 0) {
        for (var i in progressWorkOrderDetails) {
            if (progressWorkOrderDetails[i].wOID === workId) {
                for (var task in progressWorkOrderDetails[i].listOfStepTaskDetails) {
                    if (progressWorkOrderDetails[i].listOfStepTaskDetails[task].stepID === stepID) {
                        var taskJson = progressWorkOrderDetails[i].listOfStepTaskDetails[task].listOfTaskModel[0];
                        var stepName = progressWorkOrderDetails[i].listOfStepTaskDetails[task].stepName;
                        viewTaskHeader = `<span style="font-weight:bold">STEP NAME:</span>${stepName}`;
                        viewTaskDetailTable = `<div class="row"><div class="col-md-4"><span style="font-weight:bold">Execution Type: </span>${taskJson.executionType}</div>
                            <div class="col-md-4"><span style="font-weight:bold">Description: </span>${taskJson.description}</div>
                            <div class="col-md-4"><span style="font-weight:bold">Avg Est Effort: </span>${taskJson.avgEstdEffort}hrs</div>
                            <br/><hr/></div>
                            <table class="table table-striped">
                            <thead><tr><th>Start Date</th><th>End Date</th><th>Hours</th><th>Type</th><th>Status</th><th>Signum</th></tr></thead><tbody>`;
                        for (var j in taskJson.lstBookingDetailsModels) {
                            if (taskJson.lstBookingDetailsModels[j].bookingID !== 0) {
                                var bookJson = taskJson.lstBookingDetailsModels[j];
                                var stFormatDate = null;
                                var endFormatDate = null;
                                if (bookJson.startDate != null) {
                                    var stDate = new Date(bookJson.startDate);
                                    stFormatDate = `${stDate.getFullYear()}-${checkTime(stDate.getMonth() + 1)}
-${checkTime(stDate.getDate())} ${checkTime(stDate.getHours())}:${checkTime(stDate.getMinutes())}`;
                                }
                                if (bookJson.endDate != null) {
                                    var endDate = new Date(bookJson.endDate);
                                    endFormatDate = `${endDate.getFullYear()}-${checkTime(endDate.getMonth() + 1)}
-${checkTime(endDate.getDate())} ${checkTime(endDate.getHours())}:${checkTime(endDate.getMinutes())}`;
                                }
                                viewTaskDetailTable += `<tr><td>${stFormatDate}</td>
                                <td>${endFormatDate} </td>
                                <td>${ bookJson.hours} </td>
                                <td> ${bookJson.type} </td>
                                <td>${bookJson.status} </td>
                                <td>${bookJson.signumID}</td></tr>`;
                            }
                        }
                        viewTaskDetailTable += '</tbody></table>';
                    }
                }
            }
        }
    }
    $('#viewTaskHeader').append(`<p>${viewTaskHeader}</p>`);
    $('#viewTaskDetailTable').append(viewTaskDetailTable);
    $('#modalTaskBookingView').modal('show');
});

function workOrderClosure(woID, woName, delStatus, delReason, comment, subActivityFlowChartDefID, projId , subActivityId, wfId) {
    if (delStatus === null || delStatus.length ===0) {
        pwIsf.alert({ msg: 'Please select the delivery status', type: 'warning' });
    }
    else if (signumGlobal === null || signumGlobal.length === 0 || woID === null || woID.length === 0 || woName === null || woName.length === 0) {
        pwIsf.alert({ msg: 'Something went wrong!!', type: 'warning' });
    }
    else if (delStatus === "Failure" && (delReason === null || delReason.length === 0)) {
        pwIsf.alert({ msg: 'Please select the reason of failure', type: 'warning' });
    }
    else if (delReason.length >= 500) {
        pwIsf.alert({ msg: 'Reason should be less than 500 characters', type: 'warning' });
    }
    else if (comment.length > 1000) {
        pwIsf.alert({ msg: 'Comment should be less than 1000 characters', type: 'error' });
    }
   else if (!comment.length && delStatus === "Success" && delReason ==="Others") {
        pwIsf.alert({ msg: 'Please fill comment for delivery reason others', type: 'warning' });
  }
    else {
        var wODetails = {
            "wOID": woID, "wOName": unescape(woName), "deliveryStatus": delStatus, "reason": delReason,
            "statusComment": comment, "lastModifiedBy": signumGlobal,
            "subactivityDefID": subActivityFlowChartDefID
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
        if (delStatus === "Success") {
            $.isf.ajax({
                type: "GET",
                url: `${service_java_URL}woExecution/checkIFLastStepNew/${woID}/${subActivityFlowChartDefID}`,
                success: function (data) {
                    if (data.isValidationFailed === false && data.responseData === true) {
                        $.isf.ajax({
                            type: "POST",
                            contentType: C_CONTENT_TYPE_APPLICATION_JSON,
                            url: `${ service_java_URL }woExecution/saveClosureDetailsForWO`,
                            dataType: 'html',
                            data: JSON.stringify(wODetails),
                            success: function (data) {
                                if (data.isValidationFailed) {
                                    errorHandler(data);
                                }
                                else {
                                    $('#modalCompleted').modal('hide');
                                    $.isf.ajax({
                                        type: "POST",
                                        contentType: C_CONTENT_TYPE_APPLICATION_JSON,
                                        url: `${service_java_URL}/woExecution/saveWFUserProficiency`,
                                        data: JSON.stringify(wfDetailsWithUserProficiency),
                                    });
                                    location.reload();
                                }
                            },
                            error: function (xhr, status, statusText) {
                                var err = JSON.parse(xhr.responseText);
                                pwIsf.alert({ msg: err.errorMessage, type: 'error' });
                            }
                        });
                    }
                    else {
                        pwIsf.alert({ msg: "Please complete all the tasks of this work order to close this.", type: 'warning' });
                    }
                },
                error: function (xhr, status, statusText) {
                    var err = JSON.parse(xhr.responseText);
                    pwIsf.alert({ msg: err.errorMessage, type: 'error' });
                }
            });
        }
        else if (delStatus === "Failure") {
            $.isf.ajax({
                type: "POST",
                contentType: C_CONTENT_TYPE_APPLICATION_JSON,
                url: `${service_java_URL}woExecution/saveClosureDetailsForWO`,
                dataType: 'html',
                data: JSON.stringify(wODetails),
                success: function (data) {
                    if (data.isValidationFailed) {
                        errorHandler(data);
                    }
                    else {
                        $('#modalCompleted').modal('hide');
                        location.reload();
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

function transferWOInProgress(woId, senderSignum, stepName, userComments) {
    if (stepName === null || stepName.length === 0 || stepName === '-1') {
        pwIsf.alert({ msg: 'Please select the Step Name', type: 'warning' });
        return;
    } else if (userComments.length > 250) {
        pwIsf.alert({ msg: 'Please Comment within 250 characters', type: 'warning' });
        return true;
    }
    if (senderSignum === null || senderSignum.length === 0 || senderSignum.length !== 7) {
        pwIsf.alert({ msg: 'Please select the sender Signum', type: 'warning' });
    }
    else if (signumGlobal === null || signumGlobal.length === 0 || woId === null || woId.length === 0) {
        pwIsf.alert({ msg: 'Something went wrong!!', type: 'warning' });
    }
    else if (signumGlobal.trim() === senderSignum.toLowerCase().trim()) {
        pwIsf.alert({ msg: 'Work order cannot be transferred to yourself', type: 'warning' });
    }
    else {
        var transferArr = {
            "woID": [woId], "senderID": signumGlobal, "receiverID": senderSignum, "logedInSignum": signumGlobal, "stepName": stepName,
            "userComments": userComments
        };
        $.isf.ajax({
            url: `${service_java_URL}woManagement/transferWorkOrder`,
            method: "POST",
            contentType: C_CONTENT_TYPE_APPLICATION_JSON,
            data: JSON.stringify(transferArr),
            dataType: "html",
            success: function (data) {
                var convertedJson = JSON.parse(data);
                if (convertedJson.isValidationFailed) {
                    pwIsf.alert({ msg: data.formMessages[0], type: 'error' });
                    location.reload();
                }
                else {
                    pwIsf.alert({ msg: convertedJson.formErrors[0], type: 'error' });
                }
            },
            error: function (xhr, status, statusText) {
                var err = JSON.parse(xhr.responseText);
                pwIsf.alert({ msg: err.errorMessage, type: 'error' });
            }
        });
    }
}

function flowChartOpenInNewWindow(url) {
    var width = window.innerWidth * 0.95;
    // define the height in
    var height = width * window.innerHeight / window.innerWidth;
    // Ratio the hight to the width as the user screen ratio
    const topHeight = ((window.innerHeight - height) / 2);
    const leftHeight = ((window.innerWidth - width) / 2);
    const openInNewWinUrl = `${url}&commentCategory=WO_LEVEL,newwindow,width=${width},height=${height},top=${topHeight},left=${leftHeight}`;
    window.open(openInNewWinUrl);
}

