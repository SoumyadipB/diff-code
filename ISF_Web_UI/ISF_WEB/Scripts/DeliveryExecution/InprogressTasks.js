const inprogressPanelBoxMaxHeight = '145px';
const inprogressPanelBoxMiddHeight = '65px';
var timerWorkerDE = new Object();
//START - IN-PROGRESS TASKS 

function resSetShowAndHideInprogressBtn() {
    var obj = {};
    obj.targetObj = '#showInprogessToTop';
    obj.hasClassName = 'fa-arrow-circle-o-up';
    obj.newClass = 'fa-arrow-circle-o-down';
    obj.title = 'Show inprogress task';

    resetbtnWhichHasI(obj);
}

function getReasons(reasonType, element) {
    $.isf.ajax({
        async: false,
        url: service_java_URL + "woExecution/getWOFailureReasons/" + reasonType,
        success: function (data) {
            //pwIsf.removeLayer();
            $('#' + element).html('');
            if (data.isValidationFailed == false) {
                $.each(data.responseData, function (i, d) {
                    $('#' + element).append('<option value="' + d.failureReason + '">' + d.failureReason + '</option>');
                });
            }
            else {
                pwIsf.alert({ msg: data.formErrors[0], type: 'error' });
            }
        },
        error: function (xhr, status, statusText) {
            // pwIsf.removeLayer();
            console.log('An error occurred on getProjectName: ' + xhr.error);
        },
        complete: function (xhr, statusText) {
            //  $('#commentSkippedWF').select2();
        }
    });

}

//Check validations and complete WO
function validateAndCompleteWorkOrder(woId) {
    if ($('#iframe_' + woId)[0] != undefined) {
        let allowCompletion = isFlowChartCompletionValidated(woId);
        if (allowCompletion) {
            completeWorkOrder(woId);
        }
        else {
            pwIsf.alert({ msg: "Please complete all the tasks of this work order to close this.", type: 'warning' });
        }
    }
    else {
        completeWorkOrder(woId);
    }
}

//Complete WO
function completeWorkOrder(woId){

    let completeId = '#completeBtn_' + woId;
    var woName = $(completeId).data('woname');
    var subActDefId = $(completeId).data('subactdefid');
    var cycletime = $(completeId).data('cycletime');
    let projId = $(completeId).data('projid');
    let subactivityid = $(completeId).data('subactivityid');
    let wfid = $(completeId).data('wfid');

    $('#woidForDelPerformance').text(woId);
    $('#userNameForDelPerformance').text(UserNameSession);


    $(".modal-body #completedProjectId").val(projId);
    $(".modal-body #workOrderNameComp").val(woName);
    $(".modal-body #workOrderIDComp").val(woId);
    $(".modal-body #subActivityDefID").val(subActDefId);
    $(".modal-body #cycletime").val(cycletime);
    $(".modal-body #subActivityId").val(subactivityid);
    $(".modal-body #wfId").val(wfid);
    $(".modal-body #commentCompleted").val("");
    $("#commentCompleted").css("border-bottom-color", "rgba(12, 12, 12, 0.12)");
    //$(".modal-body #delStatus").val("");
    $(".modal-body #reasonDiv").css("display", "none");
    $(".modal-body #subsequentDiv").css("display", "none");
    $('#modalCompleted').modal('show');
    //$('#delStatus').val('Success').change('trigger');
    $('.modal-body #delStatus').val('Success').change('trigger');
    if (cycletime < 0) {
        $("#cycleTimeReasondiv").show();
    }
    else {
        $("#cycleTimeReasondiv").hide();
    }

    getCycleTimeReson();


    getSuccessReasons(woId); // Checking conditions for reasons and the comment box on mark as completed field

}

function bindElementOfInprogress(param) {
    let startDateDE = new Date();
    $('#inprogressAutoTotal').text(param.autoTotal);
    $('#inprogressManualTotal').text(param.manualTotal);
    $('#inprogressGrandTotal').text(param.gTotal);

    $('.inprogressTask_panel .pull-right').show();
    $('.inprogressTask_container').html('').append(param.htm);

    
    $('#displayAutomaticTask').off('click').on('click', function () {
        $('#searchInInprogress').val('Auto..').trigger('keyup').val('');
    });

    $('#displayManualTask').off('click').on('click', function () {
        $('#searchInInprogress').val('Manu..').trigger('keyup').val('');
    });
    $('#displayAllTaskOfInprogress').off('click').on('click', function () {
        $('#searchInInprogress').val('').trigger('keyup');
    });
    
    for (var i = 0; i < param.timerObj.length; i++) {

        startTimeManual(param.timerObj[i].elapsedTime, param.timerObj[i].clockId, startDateDE);
    }
    
    for (var i = 0; i < param.timerObjTotalEffort.length; i++) {

        startTimeManual(param.timerObjTotalEffort[i].elapsedTimeTotalEffort, param.timerObjTotalEffort[i].clockIdTotalEffort, startDateDE);
    }

    $('#searchInInprogress').keyup(function () {

        var searchObj = {}; callBacksObj = {};
        var that = this;

        searchObj.textToBeSearch = $(that).val().toLowerCase();
        searchObj.searchInTarget = '.inprogressTask_container > .inprogressTask_dBox';
        callBacksObj.callBack = false;
        callBacksObj.mainContainer = '.inprogressTask_container';
        callBacksObj.callBackFunction = 'onInprogressContainerChange';

        searchInDiv(searchObj, callBacksObj);


    });

    $('#expandAndCollapseInprogress').off('click').on('click', function () {
        var obj = {};
        obj.that = this;
        obj.targetSelector = $('.inprogressTask_container > .dBox');
        obj.maxHeight = inprogressPanelBoxMaxHeight;
        obj.middHeight = inprogressPanelBoxMiddHeight;
        obj.minHeight = '0px';
        expandAndCollapse(obj);
    });

    $("#pauseAll").off('click').off('click').on('click', function () {
        var reasonType = "DeliveryExecution";
        getReasons(reasonType, 'commentPauseAll');
        $('#modalPauseAll').modal('show');
    })

    $('.flowchartlink').on('click', function () {

        var woID = $(this).data('woid');
        var workorderpanel = $(".work_order_dBox_container").find('span').filter(function () { return ($(this).text().indexOf(woID) > -1) });
        var viewFCBtn = workorderpanel.closest('.panel-heading').find('.on-wo-viewFlowchart');
        $(viewFCBtn).click();
    });

   
    //
    $('.pauseTaskBtn').on('click', function () {
        var woID = $(this).data('woid');
        let subActivityFlowChartDefID = $("#hiddenVal_" + woID).data('defid');
        let stepID = $("#hiddenVal_" + woID).data('stepid');
        let taskID = $("#hiddenVal_" + woID).data('taskid');
        let bookingID = $("#hiddenVal_" + woID).data('bookingid');
        let executionType = $("#hiddenVal_" + woID).data('exectype');
        let bookingType = $("#hiddenVal_" + woID).data('bookingtype');
        let flowChartType = $(this).data('flowcharttype');

        if (executionType == C_EXECUTION_TYPE_MANUALDISABLED) {
            let stopDisabledStepDetails = new Object();
            stopDisabledStepDetails.flowChartType = flowChartType;
            stopDisabledStepDetails.woID = woID;
            stopDisabledStepDetails.subActivityFlowChartDefID = subActivityFlowChartDefID;
            stopDisabledStepDetails.stepID = stepID;
            stopDisabledStepDetails.taskID = taskID;
            stopDisabledStepDetails.executionType = executionType;
            stopDisabledStepDetails.bookingID = bookingID;
            stopDisabledStepDetails.bookingType = bookingType;
            $('#isCallingFromFlowChartHide').val("false");
            $('#stopDisabledStepDetails').val(JSON.stringify(stopDisabledStepDetails));
            $('#disabledStepHeader').find('p').remove().end();
            $('#disabledStepHeader').append('<p>Pause Disabled Step for WOID: ' + woID + '</p>')
            getReasons('DeliveryExecution', 'disabledStepReasons');
            $('#modalDisabledStepPause').modal('show');

            //InvokeStopFlowChartTask(flowChartType, woID, subActivityFlowChartDefID, stepID, taskID, false, executionType, bookingID, bookingType, '', afterLoadingFlowchart, '', false, false, false)
        }
        else {
            
            $('#taskHoldHeader').find('p').remove().end();
            $('#taskHoldHeader').append('<p>On Hold: ' + $("#hiddenVal_" + woID).data('taskname') + '</p>')
            $(".modal-body #woId").val($("#hiddenVal_" + woID).data('woid'));
            $(".modal-body #taskId").val($("#hiddenVal_" + woID).data('taskid'));
            $(".modal-body #bookId").val($("#hiddenVal_" + woID).data('bookingid'));
            $(".modal-body #execType").val($("#hiddenVal_" + woID).data('exectype'));
            $(".modal-body #stepid").val($("#hiddenVal_" + woID).data('stepid'));
            $(".modal-body #defid").val($("#hiddenVal_" + woID).data('defid'));
            $(".modal-body #flowChartType").val($(this).data('flowcharttype'));            

            var reasonType = "DeliveryExecution";
            getReasons(reasonType, 'commentOnHold');
            $('#modalTaskHold').modal('show');
        }
        
    });

    $('.stopTaskBtn').on('click', function () {
        //console.log('click on stopTaskBtn');
        let woID = $(this).data('woid');
        let subActivityFlowChartDefID = $("#hiddenVal_"+woID).data('defid');
        let stepID = $("#hiddenVal_" + woID).data('stepid');
        let taskID = $("#hiddenVal_" + woID).data('taskid');
        let bookingID = $("#hiddenVal_" + woID).data('bookingid');
        let executionType = $("#hiddenVal_" + woID).data('exectype');
        let botType = $("#hiddenVal_" + woID).data('bottype');
        let bookingType = $(this).data('bookingtype');
        let flowChartType = $(this).data('flowcharttype');
        let nextStepType = $(this).data('nextsteptype');
      
        let reasonType = "Automatic_Forced_stop";

        //Close WO if Manual Disabled Step is last step(logic fo last step already handled)
        if (executionType == C_EXECUTION_TYPE_MANUALDISABLED) {
            let stopDisabledStepDetails = new Object();
            stopDisabledStepDetails.flowChartType = flowChartType;
            stopDisabledStepDetails.woID = woID;
            stopDisabledStepDetails.subActivityFlowChartDefID = subActivityFlowChartDefID;
            stopDisabledStepDetails.stepID = stepID;
            stopDisabledStepDetails.taskID = taskID;
            stopDisabledStepDetails.executionType = executionType;
            stopDisabledStepDetails.bookingID = bookingID;
            stopDisabledStepDetails.bookingType = bookingType;

            //if (nextStepType == C_STEP_TYPE_END) {
            //    stopDisabledStep(stopDisabledStepDetails, '', woID, true);
            //}
            //else {
            //    stopDisabledStep(stopDisabledStepDetails, '', woID, false);
            //}

            // Mark as Complete form wouldn't show in case of disabled Manual Stop.
            stopDisabledStep(stopDisabledStepDetails, '', woID, false);
        }
        else if (executionType == "Automatic") {
            if (botType.toLowerCase().includes('nest')) {
                stopBotNestTask(bookingID, stepID, woID, flowChartType, false, afterLoadingFlowchart);
            //    stopFlowChartTask(woID, subActivityFlowChartDefID, stepID, taskID, false, executionType, afterLoadingFlowchart);
            }
            else {
         
                let bookingDetails = getBookingDetailsByBookingId(woID, taskID, stepID);
                let reason = bookingDetails.reason;
                bookingType = bookingDetails.type;
                //bookingID = bookingDetails.bookingID;

                getReasons(reasonType, 'commentStoppedWF');

                if (bookingType != undefined && bookingType.toLowerCase() == "queued") {
                    $('.stopAutoFromProg #woidWF').val(woID); $('.stopAutoFromProg #stepIDWF').val(stepID);
                    $('.stopAutoFromProg #taskIDWF').val(taskID); $('.stopAutoFromProg #bookingIDWF').val(bookingID);
                    $('.stopAutoFromProg #botType').val(botType); $('.stopAutoFromProg #subActDefID').val(subActivityFlowChartDefID);
                    $('.stopAutoFromProg #bookingTypeWF').val(bookingType);
                    $('.stopAutoFromProg #calledFromSkip').val('false');
                    $(".stopAutoFromProg #flowChartType").val($(this).data('flowcharttype'));
                    $('#modalStopWF').modal('show');
                }
                else {
                    if (reason != "Success") {
                        pwIsf.confirm({
                            title: 'Stop BOT Execution?', msg:
                                'Bot is already executing. It is not recommended to stop it in between. Please wait!',
                            'buttons': {
                                'Force Stop': {
                                    'class': 'btn btn-danger',
                                    'action': function () {


                                        //Select Reason for Denail.
                                        //  getReasons(reasonType, 'commentStoppedWF');

                                        $('.stopAutoFromProg #woidWF').val(woID); $('.stopAutoFromProg #stepIDWF').val(stepID);
                                        $('.stopAutoFromProg #taskIDWF').val(taskID); $('.stopAutoFromProg #bookingIDWF').val(bookingID);
                                        $('.stopAutoFromProg #botType').val(botType); $('.stopAutoFromProg #subActDefID').val(subActivityFlowChartDefID);
                                        $(".stopAutoFromProg #flowChartType").val($(this).data('flowcharttype'));
                                        $('.stopAutoFromProg #calledFromSkip').val('false');
                                        $('#modalStopWF').modal('show');
                                    }
                                },
                                'Cancel': {
                                    'class': 'btn btn-success',
                                    'action': function () {
                                        $('#modalStopWF').modal('hide');
                                    }
                                },

                            }
                        });
                    }
                }
                //    
            }
        }
        else {//manual step stop
            InvokeStopFlowChartTask(flowChartType, woID, subActivityFlowChartDefID, stepID, taskID, false, executionType, bookingID, bookingType, '', afterLoadingFlowchart,'',false,false,false)
            //stopFlowChartTask(flowChartType, woID, subActivityFlowChartDefID, stepID, taskID, false, executionType, bookingID, bookingType, '', afterLoadingFlowchart);
        }
    });


    $('.skipTaskBtn').on('click', function () {
        //console.log('click on skipTaskBtn');
        var woID = $(this).data('woid');
       // let subActivityFlowChartDefID = $(this).data('defid');
        $('#skippedHeader').find('p').remove().end();
        $('#skippedHeader').append('<p>Skipped Task: ' + $("#hiddenVal_"+woID).data('taskname') + '</p>')
        $(".modal-body #woId").val($("#hiddenVal_"+woID).data('woid'));
        $(".modal-body #taskId").val($("#hiddenVal_"+woID).data('taskid'));
        $(".modal-body #bookId").val($("#hiddenVal_"+woID).data('bookingid'));
        $(".modal-body #execType").val($("#hiddenVal_"+woID).data('exectype'));
        $(".modal-body #stepid").val($("#hiddenVal_"+woID).data('stepid'));
        $(".modal-body #defid").val($(this).data('defid'));
        $(".modal-body #flowChartType").val($(this).data('flowcharttype'));
        
        let subActFlowChartDefID = $(this).data('defid');

        var reasonType = "DeliveryExecution";    
        
        if ($("#hiddenVal_" + woID).data('exectype') == "Automatic") {

            let bookingDetails = getBookingDetailsByBookingId(woID, $("#hiddenVal_" + woID).data('taskid'), $("#hiddenVal_" + woID).data('stepid'));
            bookingType = bookingDetails.type;
            let bookId = bookingDetails.bookingID;
            let currentStatus = bookingDetails.status;

            if (bookingType != undefined && bookingType.toLowerCase() == "queued" && currentStatus != "COMPLETED") {
                // GetReasonForDenail($('#commentSkippedWF'));
                reasonType = 'Automatic_Forced_stop';
                getReasons(reasonType, 'commentSkipped');
                $('#modalSkipped').modal('show');
                $('.modal-body #defid').val(subActFlowChartDefID);
            }
              else if (bookingType != undefined && bookingType.toLowerCase() == "queued" && currentStatus == "COMPLETED") {
                updateFlowChartTaskStatus(woID, subActFlowChartDefID, $("#hiddenVal_" + woID).data('stepid'), $("#hiddenVal_" + woID).data('taskid'), $("#hiddenVal_" + woID).data('bookingid'), 'SKIPPED', null, $("#hiddenVal_" + woID).data('exectype'), false);
        }
            else if (bookingType != undefined && bookingType.toLowerCase() == "booking" && currentStatus != 'COMPLETED') {

                reasonType = 'Automatic_Forced_stop';

                getReasons(reasonType, 'commentStoppedWF')
                pwIsf.confirm({
                    title: 'Stop BOT Execution?', type: 'warning', msg:
                        'Bot is already executing. Inorder to skip the step, stop execution first.',
                    'buttons': {
                        'Force Stop': {
                            'class': 'btn btn-danger',
                            'action': function () {
                              //  updateFlowChartTaskStatus(woID, subActFlowChartDefID, $("#hiddenVal_" + woID).data('stepid'), $("#hiddenVal_" + woID).data('taskid'), $("#hiddenVal_" + woID).data('bookingid'), 'SKIPPED', null, $("#hiddenVal_" + woID).data('exectype'), false);
                                $('.stopAutoFromProg #woidWF').val(woID); $('.stopAutoFromProg #stepIDWF').val($("#hiddenVal_" + woID).data('stepid'));
                                $('.stopAutoFromProg #taskIDWF').val($("#hiddenVal_" + woID).data('taskid')); $('.stopAutoFromProg #bookingIDWF').val(bookId);
                                $('.stopAutoFromProg #subActDefID').val(subActFlowChartDefID);
                                $('.stopAutoFromProg #bookingTypeWF').val('FORCE_STOP');
                                $(".stopAutoFromProg #calledFromSkip").val('true');
                                $('#modalStopWF').modal('show');
                                // $('#modalSkippedWF').modal('show');
                            }
                        },
                        'Cancel': {
                            'action': function () {
                                $('#modalSkipped').modal('hide');
                            }
                        },
                    }
                });

            }
            //else {
            //    getReasons(reasonType, 'commentSkipped');
            //    $('#modalSkipped').modal('show');
            //}
            else if (bookingType != undefined && bookingType.toLowerCase() == "booking" && currentStatus == "COMPLETED") {
                //Stopped and then skipped, bookingtype = "booking".
                updateFlowChartTaskStatus(woID, subActFlowChartDefID, $("#hiddenVal_" + woID).data('stepid'), $("#hiddenVal_" + woID).data('taskid'), $("#hiddenVal_" + woID).data('bookingid'), 'SKIPPED', null, $("#hiddenVal_" + woID).data('exectype'), false);
                //   GetReasonForDenail('DeliveryExecution', $('#commentSkippedWF'));
                //$('#modalSkippedWF').modal('show');
            }
            else {
                getReasons('Automatic_Forced_stop', 'commentSkipped');
                $('#modalSkippedWF').modal('show');
            }
        }
        else {
            getReasons(reasonType, 'commentSkipped');
            $('#modalSkipped').modal('show');
        }

    });

    $('.startNextTaskBtn').on('click', function () {
        playNextClicked = true;
        let woID = $(this).data('woid');
        let getDetails = JSON.parse(JSON.stringify($(this).data('details')));
        let currentTaskID = $('.dBox-content #hiddenVal_' + woID).data('taskid');
        var currentBookingID = $("#hiddenVal_" + woID).data('bookingid');
        var currentExecutionType = $("#hiddenVal_" + woID).data('exectype');
        var stepID = $("#hiddenVal_" + woID).data('stepid');
        var subActivityFlowChartDefID = $("#hiddenVal_" + woID).data('defid');
        var nextStepID = getDetails.nextStepID;
        var nextRPAID = getDetails.nextStepRpaId;
        var nextStepType = getDetails.nextStepType;
        var prjID = getDetails.projectId;
        var nextStepName = getDetails.nextStepName;
        var version = getDetails.version;
        var nextTaskID = getDetails.nextStepTaskId;
        var nextBookingID = getDetails.nextStepBookingId;
        var nextBookingStatus = getDetails.nextStepBookingStatus;
        var nextExecutionType = getDetails.nextStepExeType;
        let botType = getDetails.botType;
        let outputUpload = getDetails.outputUpload;
	    var nextRunOnServer = getDetails.nextRunOnServer;
        let isInputRequiredNext = getDetails.isInputRequiredNext;  
        let flowChartType = $(this).data('flowcharttype');

        let stopDisabledStepDetails = new Object();
        stopDisabledStepDetails.flowChartType = flowChartType;
        stopDisabledStepDetails.woID = woID;
        stopDisabledStepDetails.subActivityFlowChartDefID = subActivityFlowChartDefID;
        stopDisabledStepDetails.stepID = stepID;
        stopDisabledStepDetails.taskID = currentTaskID;
        stopDisabledStepDetails.executionType = currentExecutionType;
        stopDisabledStepDetails.bookingID = currentBookingID;
        stopDisabledStepDetails.bookingType = $("#hiddenVal_" + woID).data('bookingtype');
        

        if (nextStepType == C_STEP_TYPE_DECISION || nextStepType == C_STEP_TYPE_END) { 
            if (currentExecutionType == C_EXECUTION_TYPE_MANUALDISABLED) {
                stopDisabledStep(stopDisabledStepDetails, '', woID, false, false);
            }
            else {
                StopTaskInput = PrepareStopFlowChartTaskInput(flowChartType, woID, subActivityFlowChartDefID, stepID, currentTaskID, false, currentExecutionType, currentBookingID, 'BOOKING', '', afterLoadingFlowchart, false, false, true, false);
                stopFlowChartTask(StopTaskInput);
            }
            if (nextStepType == C_STEP_TYPE_DECISION) {
                let timerIcrement = 0;
                if (!document.getElementById("iframe_" + woID)) {
                    var intervalToCheckViewBtn = setInterval(function () {
                        let viewFCBtn = $('#viewBtn_' + woID);

                        if (viewFCBtn.length) {
                            viewFCBtn.trigger('click');
                            clearInterval(intervalToCheckViewBtn);
                        }
                        if (timerIcrement > 3) {
                            pwIsf.alert({ msg: 'This work order is not in your queue.', type: 'warning' });
                            clearInterval(intervalToCheckViewBtn);
                        }
                        timerIcrement++;
                    }, 500);
                    //execute after 5 minute bcs taking time to open FC
                    setTimeout(function () {
                        scrollToDecisionStepOfFlowChart(woID, nextStepID);
                    }, 5000);
                } else {
                    scrollToDecisionStepOfFlowChart(woID, nextStepID);
                }
            }
            else {
                validateAndCompleteWorkOrder(woID);
            }
        }
        else {
            //Loader for synchronous ajax calls
            pwIsf.addLayer({ text: "Please wait ..." });
            setTimeout(function () {
                //playNextStep(flowChartType, isInputRequiredNext, botType, nextRPAID, woid, prjID, version, stepID, nextTaskID, subActivityFlowChartDefID, nextBookingID, nextStepID, nextBookingStatus, nextExecutionType, currentTaskID, currentBookingID, currentExecutionType, nextStepName, outputUpload, nextRunOnServer, false);
                playNextStep(flowChartType, isInputRequiredNext, botType, nextRPAID, woID, prjID, version, stepID, nextTaskID, subActivityFlowChartDefID, nextBookingID, nextStepID, nextBookingStatus, nextExecutionType, currentTaskID, currentBookingID, currentExecutionType, nextStepName, outputUpload, nextRunOnServer, false);
            }, 0);            
           
        }
    });
    $('.infoTaskBtn').on('click', function () {
    });

    $('#refreshInprogressTask').off('click').on('click', function () {
		clearTimeouts();
        requestInprogressTasks();
    });

    //Start- Expand or collapse panel based on previous state of icon 
    let targetSelector = $('.inprogressTask_container > .dBox');
    if ($('#expandAndCollapseInprogress').find('i').hasClass('fa-angle-down')) {
        targetSelector.css({ 'height': inprogressPanelBoxMiddHeight });
    } else {
        targetSelector.css({ 'height': inprogressPanelBoxMaxHeight, 'overflow-y': 'auto'});
    }
    //End- Expand or collapse panel based on previous state of icon 
    
    //if (adhocBookingStatus) {
    //    //freezeAllPage();
    //}
}
function scrollToDecisionStepOfFlowChart(woID, nextStepID) {
    let decisionStep = document.getElementById('iframe_' + woID).contentWindow.app.graph.toJSON().cells.filter(function (el) { return el.id == nextStepID });
    document.getElementById('iframe_' + woID).contentWindow.app.paperScroller.scroll(decisionStep[0].position.x, decisionStep[0].position.y);
    $('html, body').animate({
        scrollTop: $('#flow_chart_single_box_' + woID).offset().top
    }, 100);
}
var timeOuts = new Array();
function startTimeManual(execTimeInSeconds, clockId, startDateDE) {    

    //Check if browser supports Web Workers
    if (typeof (Worker) !== "undefined") {
        if (typeof (timerWorkerDE[clockId]) == "undefined") {
            timerWorkerDE[clockId] = new Worker(rootDir + '/Scripts/WebWorkers/deliveryExecutionWorker.js');
        }
        let DETimer = new Object();
        DETimer.elapsedTime = execTimeInSeconds;
        DETimer.startDateDE = startDateDE;
        //DETimer.clockId = clockId;

        timerWorkerDE[clockId].postMessage(DETimer);

        timerWorkerDE[clockId].onmessage = function (e) {

            if (document.getElementById(clockId)) {
                document.getElementById(clockId).innerHTML = e.data;
            }
        }
    }
    else {
        document.getElementById(clockId).innerHTML = "Sorry! No Web Worker support.";
    }

}

//Truncate all web workers
function stopWorkersDE() {
    if (Object.keys(timerWorkerDE).length > 0) {
        for (key in timerWorkerDE) {
            timerWorkerDE[key].terminate();
            delete timerWorkerDE[key];
        }
    }
}

//clear all settimeout handles.
function clearTimeouts() {
    for (key in timeOuts) {
        clearTimeout(timeOuts[key]);
    }
}

function checkTime(i) {
    if (i < 10)
        i = "0" + i;
    return i;
}

function isWoAnyStepRunning(tmpWoId) {
    if (tmpWoId in arrAllRunningWoIds) {
        return true;
    } else {
        return false;
    }

    //arrAllRunningWoIds
}

//function FloatingWindow() {
//   //window.open('isfFloatingWindow:exec_' + signumGlobal, '_self');

//    var IsFloatingWindowRunning = localStorage.getItem("IsFloatingWindowRunning");

//    if (IsFloatingWindowRunning == undefined || IsFloatingWindowRunning == null || IsFloatingWindowRunning == "") {
//        localStorage.setItem("IsFloatingWindowRunning", true);
//        setTimeout(function () {
//            window.open('FloatingWindow:exec_' + signumGlobal, '_self');
//        }, 2000);
//    }
//    else {
//        //ConnectSignalR();
//    }
   
//}

function createInprogressTasksHtml(passData, callback) {
    //FloatingWindow();
   
    var dBox = ''; let tooltipContent = '';
    var manualTotal = 0; autoTotal = 0; gTotal = 0; disabledTotal = 0;
    for (var i in passData) {
        
        if (passData[i].executionType){
            var boxClass = ''; let nodeNamesUnique = '' 
            var desc = (passData[i].description) ? passData[i].description : replaceNullBy;
            var nodeNames = (passData[i].nodeNames) ? passData[i].nodeNames : replaceNullBy;
            
            if (nodeNames.length != 0) {
                nodeNamesUnique = jQuery.unique(nodeNames.sort()).join(',');
                tooltipContent = breakTooltipContent(nodeNamesUnique, 40);
                nodeNames = nodeNamesUnique;
                tooltipContent = breakTooltipContent(nodeNames, 40);
            }
            else {
                nodeNames = replaceNullBy;
            }
           
            var woID = (passData[i].woID) ? passData[i].woID : replaceNullBy;
            arrAllRunningWoIds[woID] = 1;

            let nextStepName = replaceNullBy; let nextStepNameArr = [];
            let allSteps = (passData[i].nextSteps) ? passData[i].nextSteps : replaceNullBy;
            let infoTip = (passData[i].task) ? '<b>Current Task:</b> ' + passData[i].task : replaceNullBy;

            let nextStepType = '';
            let aTagLinkText = '<span class="lbl">WOID :</span><span class="val">' + woID + '</span>';


            if (allSteps != replaceNullBy) { 
                for (let k in allSteps) {
                    nextStepNameArr.push(allSteps[k].nextStepName);
                    nextStepType = allSteps[k].nextStepType;
                }
                nextStepName = nextStepNameArr.toString();
                infoTip = '<b>Next Step Name:</b> ' + nextStepName +'<br>'+ infoTip;
            }

            if (nodeNames != replaceNullBy) {
                nodeNamesArr = nodeNames.split(',');
                nodeNamesUnique = jQuery.unique(nodeNamesArr.sort()).join(',');
                tooltipContent = breakTooltipContent(nodeNamesUnique, 40);
                nodeName = nodeNamesUnique;
                infoTip = infoTip + '<br><b>Network Element Name/ID:</b> ' + tooltipContent; 
                aTagLinkText = '<span class="lbl">NEID :</span><span class="val"><p class="nodeNameP protip" style="width:125px" data-pt-scheme="aqua" data-pt-position="bottom-right-corner" data-pt-skin="square" data-pt-width="400" data-pt-title="' + tooltipContent +'">' + nodeNames + '</p></span>';
            }

            if (passData[i].executionType == C_EXECUTION_TYPE_MANUALDISABLED) {
                ++manualTotal;
                boxClass = 'dBox-default';
            }
            else if (passData[i].executionType == 'Manual') {
                ++manualTotal;
                boxClass = 'dBox-info';
            } else {
                ++autoTotal;
                boxClass = 'dBox-warning';
            }
            let botType = '';
            (passData[i].botType == undefined) ? botType : botType = passData[i].botType;

            let nextBotType = '';
            (passData[i].nextSteps[0].nextBotType == undefined) ? botType : nextBotType = passData[i].nextSteps[0].nextBotType;
            nextRunOnServer = (passData[i].nextSteps) ? passData[i].nextSteps[0].isRunOnServer : replaceNullBy;
            let isInputRequiredNext;
            isInputRequiredNext = (passData[i].nextSteps) ? passData[i].nextSteps[0].isInputRequired : replaceNullBy;

            let proficiencyId = passData[i].proficiencyType.proficiencyID;

            let stepData = JSON.stringify({ isInputRequiredNext: isInputRequiredNext, botType: nextBotType, outputUpload: passData[i].outputUpload, nextStepRpaId: passData[i].nextSteps[0].nextStepRpaId, nextStepID: passData[i].nextSteps[0].nextStepID, nextStepType: passData[i].nextSteps[0].nextStepType, projectId: passData[i].projectID, nextStepName: passData[i].nextSteps[0].nextStepName, version: passData[i].versionNumber, nextStepExeType: passData[i].nextSteps[0].nextExecutionType, nextStepBookingId: passData[i].nextSteps[0].bookingID, nextStepBookingStatus: passData[i].nextSteps[0].bookingStatus, nextStepTaskId: passData[i].nextSteps[0].nextTaskID, nextRunOnServer: nextRunOnServer, proficiencyId: proficiencyId });
			
            clockId = passData[i].executionType + '_' + woID;
            clockIdTotalEffort = passData[i].executionType + '_TotalEffort' + woID;

            elapsedTimeOfRunningTask = (passData[i].serverTime < 0) ? 0 : passData[i].serverTime;
            elapsedTimeTotalEffort = (passData[i].totalEffort < 0) ? 0 : passData[i].totalEffort;
      
            timerObj.push({ "elapsedTime": elapsedTimeOfRunningTask, "clockId": clockId });
            timerObjTotalEffort.push({ "elapsedTimeTotalEffort": elapsedTimeTotalEffort, "clockIdTotalEffort": clockIdTotalEffort });

            if ((passData[i].executionType == C_EXECUTION_TYPE_MANUAL || passData[i].executionType == C_EXECUTION_TYPE_MANUALDISABLED) && passData[i].status == 'STARTED') 
                arrPauseAll.push({ 'woID': woID, 'bookingID': passData[i].bookingID, 'executionType': passData[i].executionType, 'flowChartStepID': passData[i].flowChartStepID, 'taskID': passData[i].taskID, 'flowChartDefID': passData[i].flowChartDefID, 'workFlowAutoSenseEnabled': passData[i].workFlowAutoSenseEnabled, 'workOrderAutoSenseEnabled': passData[i].workOrderAutoSenseEnabled, 'startRule': passData[i].startRule, 'stopRule': passData[i].stopRule });

            dBox += '<div class="dBox ' + boxClass + ' inprogressTask_dBox" id="inprogressTask_' + woID + '">' +
                    '<div class="shape">' +
                    '<div class="shape-text">' +
                passData[i].executionType.substr(0, 4) +
                    '..</div>' +
                '</div>' +
                '<div class="dBox-content"><input type="hidden" id="hiddenVal_' + woID + '" data-bottype="' + passData[i].botType + '" data-bookingtype="' + passData[i].type + '" data-taskname="' + passData[i].task + '" data-woid=' + woID + ' data-defid=' + passData[i].flowChartDefID + ' data-stepid=' + passData[i].flowChartStepID + ' data-taskid=' + passData[i].taskID + ' data-bookingid=' + passData[i].bookingID + ' data-exectype=' + passData[i].executionType + '>' +
                    '<div class="content-row">' +
                '<a href="#" class="flowchartlink" style="display:inline-flex;" data-woid=' + woID + '>' + aTagLinkText +'</a>' +
                    '</div>' +
                    '<div class="content-row">' +
                '<div class="action_area">';
            //Pause button for Manual and Manual Disabled Steps
            if (passData[i].executionType == "Manual" || passData[i].executionType == C_EXECUTION_TYPE_MANUALDISABLED) {
                dBox += '<i class="fa fa-pause pauseTaskBtn" data-flowcharttype="' + passData[i].flowcharttype + '" data-bookingtype="' + passData[i].type + '" data-woid="' + woID + '" style="color:black;" title="Pause Step"></i>';
            }

            dBox += '<i class="fa fa-stop stopTaskBtn" data-flowcharttype="' + passData[i].flowcharttype + '" data-bookingtype="' + passData[i].type + '" data-nextsteptype="' + nextStepType + '" data-woid="' + woID + '" style="color: red;" title="Stop Step"></i>';

            //No skip button for Manual Disabled Step
            if (passData[i].executionType != C_EXECUTION_TYPE_MANUALDISABLED) {
                dBox += '<i class="fa fa-forward skipTaskBtn" data-flowcharttype="' + passData[i].flowcharttype + '" data-defid="' + passData[i].flowChartDefID + '" data-woid="' + woID + '" style="color: black;" title="Skip Step"></i>';
            }
            // modified on 4 feb 2021 display playnext button always
            //if (nextStepType != 'ericsson.EndStep' && nextStepType != 'basic.Circle' && passData[i].stepType != 'ericsson.Automatic' && nextStepType != 'ericsson.Decision') { // Display playNextStep icon if nextStep id is not zero
                //dBox += '<i class="fa fa-step-forward startNextTaskBtn" data-flowcharttype="' + passData[i].flowcharttype + '" data-nextsteptype="' + nextStepType +'" data-woid="' + woID + '" style="color: black;" title="Start Next Step" data-details=\'' + stepData + '\'></i>';
            //}
            if (passData[i].stepType != 'ericsson.Automatic') { // Display playNextStep icon if nextStep id is not zero
                dBox += '<i class="fa fa-step-forward startNextTaskBtn" data-flowcharttype="' + passData[i].flowcharttype + '" data-nextsteptype="' + nextStepType + '" data-woid="' + woID + '" style="color: black;" title="Start Next Step" data-details=\'' + stepData.replace(/\'/g, '&apos;') + '\'></i>';
            }

            dBox += '<i class="fa fa-info infoTaskBtn protip" data-toggle="tooltip" data-pt-scheme="aqua" data-pt-position="bottom-right-corner" data-pt-skin="square" data-pt-width="400" data-pt-size="small" data-pt-title="' + infoTip.replace(/\"/g, '&quot;') + '" style="color: #e2aa02;"></i>' +
                        '</div>'+
                        '<div class="time_area">'+
                           // '12 : 34 : 10'+
                            '<div id="clockdate">' +
                                '<div class="clockdate-wrapper">' +
                        '<div id="' + clockId + '" class="clock" style="font-size:16px;" ></div>' +
                                '</div></div>'+
                        '</div>'+
                    '</div>'+
                 '<div class="content-row" style="height:23px;">' +
                        '<span class="lbl" style="white-space:nowrap;">WO Total Effort : </span>' +
                        '<div class="time_area">' +
                        // '12 : 34 : 10'+
                            '<div id="clockdateTotalEffort">' +
                            '<div class="clockdate-wrapper">' +
                        '<div id="' + clockIdTotalEffort + '" class="clock" style="font-size:14px;color:green;"></div>' +
                            '</div></div>' +
                        '</div>' +

                '</div>' +
                '<div class="content-row">' +
                '<span class="lbl">Project ID :</span><p class="val">' + passData[i].projectID + '</p>' +
                '</div>' +
                '<div class="content-row">'+
                    '<span class="lbl" style="white-space:nowrap;">WF Step: </span>'+
                '<p>' + (passData[i].executionType == C_EXECUTION_TYPE_MANUALDISABLED ? 'WO In-Progress' : passData[i].stepName) + '</p>' +
                '</div>' +
               '<div class="content-row">'+
                    '<span class="lbl">NextStepName: </span>' +
                '<p>' + (passData[i].nextSteps[0].nextExecutionType == C_EXECUTION_TYPE_MANUALDISABLED ? 'WO Level' : nextStepName) + '</p>' +
                '</div>' +

                
            '</div>'+
            '</div>';

        
        } else {
            dBox += '<div class="dBox ' + boxClass + ' inprogressTask_dBox">' +
                '<div style="padding:13px;"><span style="display:block;color:red;">Execution type not defined</span>' +
                'Work order ID: ' + passData[i].woID +

                '</div></div>';
        }
    }
    gTotal = autoTotal + manualTotal;
    var passParam = {};
    passParam.htm = dBox;
    passParam.autoTotal = autoTotal;
    passParam.manualTotal = manualTotal;
    passParam.gTotal = gTotal;
    passParam.timerObj = timerObj;
    passParam.timerObjTotalEffort = timerObjTotalEffort;
   
    if (typeof callback === "function") {
        callback(passParam);
    }
}

function requestInprogressTasks() {
    var result;
    clearTimeouts();
    stopWorkersDE();
    //openISFDesktop('isfFloatingWindow:exec_' + signumGlobal);
    $('.inprogressTask_container').empty();
    $('.inprogressTask_container').html(pleaseWaitISF({text:'Loading your in-progress task(s) ...'}));
    arrPauseAll = [];
    arrAllRunningWoIds = {};
    timerObj = [];
    timerObjTotalEffort = [];
    var jqxhr = $.isf.ajax({
        async: false,
        cache: false,
        url: service_java_URL + "woManagement/v1/getInprogressTask/" + signumGlobal,
        returnAjaxObj:true
    });
   
    jqxhr.done(function (data) {
        if (data.responseData != null && data.isValidationFailed == false) {
            //   FloatingWindow();
            //  console.log(data.responseData);
            if (data.responseData.length != 0) {
                runningTaskDetails = data.responseData;
                createInprogressTasksHtml(data.responseData, bindElementOfInprogress);
            }
            else {
                $('.inprogressTask_container').html('<div class="funMsg">No In-progress Task(s).</div>');
                $('.inprogressTask_panel .pull-right').hide();
            }
        } else {

            $('.inprogressTask_container').html('<div class="funMsg">No In-progress Task(s).</div>');
            $('.inprogressTask_panel .pull-right').hide();
        }

        //SignalRConnection();
        result = data.responseData;
    });
    jqxhr.fail(function () {
  })
    jqxhr.always(function () {
      //  return result;
    });
    var autSenseData = FetchAutoSenseDetailsOfWO(result);
    SaveAutoSenseData(autSenseData);
    localStorage.setItem("AutoSenseData", JSON.stringify(autSenseData));
    return autSenseData;
}
//END - IN-PROGRESS TASKS 

function FetchAutoSenseDetailsOfWO(data) {
    var autoSenseDetailsList = [];
 

    for (var i in data)
    {
        let autoSenseDetails = {};
        autoSenseDetails.WoId = data[i].woID;
        autoSenseDetails.FlowChartStepID = data[i].flowChartStepID; 
        autoSenseDetails.FlowChartDefID = data[i].flowChartDefID;
        autoSenseDetails.ExecutionType = data[i].executionType;
        autoSenseDetails.IsStepAutoSenseEnabled = data[i].isStepAutoSenseEnabled;
        autoSenseDetails.WorkFlowAutoSenseEnabled = data[i].workFlowAutoSenseEnabled; 
        autoSenseDetails.WorkOrderAutoSenseEnabled = data[i].workOrderAutoSenseEnabled; 
        autoSenseDetails.StartRule = data[i].startRule;
        autoSenseDetails.StopRule = data[i].stopRule;
        autoSenseDetails.NextStepExecutionType = data[i].nextSteps[0].nextExecutionType
        autoSenseDetails.IsNextStepAutoSenseEnabled = data[i].nextSteps[0].isStepAutoSenseEnabled
        autoSenseDetails.NextStepStartRule = data[i].nextSteps[0].startRule
        autoSenseDetails.NextStepStopRule = data[i].nextSteps[0].stopRule
        //autoSenseDetails.NextStepStartRule = true
        //autoSenseDetails.NextStepStopRule = true
        autoSenseDetails.TaskID = data[i].taskID
        autoSenseDetails.NextStepType = data[i].nextSteps[0].nextStepType
        
        autoSenseDetailsList.push(autoSenseDetails);
    }

    return autoSenseDetailsList;
}

function SaveAutoSenseData(AutosenseData) {
   // dataToPost = JSON.stringify({ data: AutosenseData });
    dataToPost = JSON.stringify(AutosenseData);
    $.isf.ajax({
        type: "POST",
        url: UiRootDir + '/Data/SaveAutoSenseData',
        data: dataToPost,
        async: false,
        contentType: "application/json; charset=utf-8", // specify the content type
        dataType: 'JSON', 
        success: function (response) {
            console.log(response);
        },
        error: function (xhr, status, statusText) {
            console.log("error while saving autosense data");
        }
    });
}
