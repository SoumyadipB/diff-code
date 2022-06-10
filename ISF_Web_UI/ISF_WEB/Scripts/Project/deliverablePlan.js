var defaultDate, start, servAreaID;
var RRID = "";
var subActivityID = "";
$(document).ready(function () {
    getTableInfoScope();

    $('#SubScope').on('hidden.bs.modal', function () {
        cancelUpdateDetails();
    })

    $(document).on('click', '.deleteScopeProject', function () { deleteScopeProject(this); });
    $(document).on('click', '.editScopeProjectDetails', function () { getPlanSources(); editScopeProject(this); });


});



function ScopeNameValidation() {
    $('#scope_name').keypress(function (tecla) {
        var letterNumber = /^[0-9_a-z_ _A-Z_ -]+$/;
        if (!$("#scope_name").val().match(letterNumber)) {
            var text = $('#scope_name').val();
            text = $('#scope_name').val().substring(0, text.length - 1);
            $('#scope_name').val(text);
            $('#scope_name-Required').text("Only letters and numbers");
        }
        else
            $('#scope_name-Required').text("");
    });
}

/*---------------Erisite Related Code Starts----------------------*/


function fillExtRef() {
    
    externalProjectId = $("#select_ext_project option:selected").val();
    var ProjectID = localStorage.getItem("views_project_id");
    var sourceId = $("#select_ext_resource option:selected").val();
    var extWoTemp = $("#ext_source_wo_template option:selected").val();

    let activeExternalActObject = new Object();
    activeExternalActObject.sourceId = sourceId;
    activeExternalActObject.projectId = ProjectID;
    activeExternalActObject.externalProjectId = externalProjectId;
    activeExternalActObject.extWoTemp = extWoTemp;

    $.isf.ajax({
        url: service_java_URL + "externalInterface/getActiveExternalActivityList?sourceId=" + sourceId + "&projectId=" + ProjectID + "&externalProjectId=" + externalProjectId + "&extWoTemp=" + encodeURIComponent(extWoTemp),
        contentType: 'application/json',
        type: 'GET',
        success: function (data) {
            $("#externalActivityDropDown").empty();
            $('#externalActivityDropDown').append('<option value="0">Select External Reference</option>');

            if (data !== null && data !== undefined) {


                if (data.responseData) {
                    $.each(data.responseData, function (i, d) {
                        $("#externalActivityDropDown").append('<option value="' + d.ExternalActivityName + '">' + d.ExternalActivityName + '</option>');
                    })
                }


            }
            $("#externalActivityDropDown").select2();
        },
        error: function (xhr, status, statusText) {
            console.log('An error occurred on getActiveExternalActivityList: ' + xhr.error);
        },
        compelete: function () {
            $("#externalActivityDropDown").select2();
            $("#externalActivityDropDown").trigger('change');
		}
    })

}


function fillExtWOTemp() {
    $("#unlockExWOTemp").hide();
    $("#lockExWOTemp").show();
    externalProjectId = $("#select_ext_project option:selected").val();
    var ProjectID = localStorage.getItem("views_project_id");
    //var sourceId = $("#select_ext_resource option:selected").val();

    $("#ext_source_wo_template").val('');

    var sourceId = $("#select_ext_resource option:selected").val();
    var extprojID = $("#select_ext_project option:selected").val();
    $.isf.ajax({
        type: "GET",
        url: service_java_URL + "externalInterface/getActiveExternalWorkPlanTemplateList/" + sourceId + "/" + extprojID,
        success: function (data) {
            $("#ext_source_wo_template").empty();
            $('#ext_source_wo_template').append('<option value="0">Select External Workplan Template</option>');

            $.each(data, function (i, d) {
                $('#ext_source_wo_template').append('<option value="' + d.ParentWorkPlanTemplateName + '">' + d.ParentWorkPlanTemplateName + '</option>');

            });
        },
        error: function (xhr, status, statusText) {
            console.log('An error occurred on : fillExtWOTemp' + xhr.error);
        },
        complete: function () {
            $("#ext_source_wo_template").select2();
            $("#ext_source_wo_template").trigger("change");
            
        }
        
    });

}

function showExternalActivityDropDownIfReqDel() {

    $("#unlockExP").hide();
    $("#lockExP").show();
    $("#unlockExWOTemp").hide();
    $("#lockExWOTemp").show();
    const sourceTxt = $("#select_ext_resource option:selected").text();
    const sourceId = $("#select_ext_resource option:selected").val();
    const ProjectID = localStorage.getItem("views_project_id");
    if (sourceTxt !== 'ISF' && sourceId !== "0") {
        $("#hideExP").show();
        $("#externalReferenceInputDiv").hide();
        document.getElementById("externalRefDD").style.display = '';
        document.getElementById("externalWOTempDD").style.display = '';
        document.getElementById("extWOTempCss").style.display = '';
        document.getElementById("ExtWOTemplate").style.display = '';
        $("#externalReferenceDiv").show();
        $("#divExternalWTExternalProject").show();
        pwIsf.addLayer({ text: "Please wait ..." });
        $.isf.ajax({
            url: service_java_URL + "externalInterface/getExternalProjectIDByIsfProject/" + sourceId + "/" + ProjectID,
            success: function (data) {
                $("#select_ext_project").empty();
                $('#select_ext_project').append('<option value="0">Select External Project</option>');
                if (data !== null && data !== undefined) {
                    if (data.formMessageCount === 0) {
                        if (data.responseData.length > 0) {
                            $.each(data.responseData, function (i, d) {
                                $("#select_ext_project").append('<option value="' + d.ExternalProjectID + '">' + d.ExternalProjectID + '</option>');
                            })
                        }
                    }
                    else {
                        pwIsf.alert({ msg: data.formMessages[0], type: 'warning' });
                    }
                }
            },
            error: function (xhr, status, statusText) {
                console.log('An error occurred on getExternalProjectIDByIsfProject: ' + xhr.error);
            },
            complete: function () {
                pwIsf.removeLayer();
            }
        })
        $("#select_ext_project").select2();
        $("#select_ext_project").trigger('change');
    }
    else {

        $("#select_ext_project").select2();
        $("#select_ext_project").trigger('change');
        $("#hideExP").hide();
        $("#unlockExWOTemp").hide();
        $("#lockExWOTemp").hide();
        $("#extRefCss").css('margin-bottom', '0px')
        $("#extWOTempCss").css('margin-bottom', '0px')
        $("#externalReferenceInputDiv").show();
        document.getElementById("externalRefTxt").style.display = '';
        document.getElementById("externalRefDD").style.display = 'none';
        document.getElementById("externalWOTempDD").style.display = 'none';
        document.getElementById("ExtWOTemplate").style.display = 'none';
        $("#extWOTempCss").hide();
        $("#externalReferenceDiv").hide();

    }
}

function appendInErisiteActivity(data) {
    $('#externalActivityDropDown').empty();
    sourceDetails = data;
    var html = "";
    $.each(data, function (i, d) {
        externalProjectID = d.ExternalProjectID;
        $('#externalActivityDropDown').append('<option value="' + d.ExternalActivityName + '">' + externalProjectID + '_' + d.ExternalActivityName + '</option>');
    })
}

/*---------------Erisite Related Code Ends----------------------*/

/*---------------Get EP Sources in dropdown-----------------------*/

function getPlanSources() {
    newplanNames = []; planNames = []; rawPlanNames = [];
    $('#plan_name-Required').text("");
    $('#select_ext_resource').empty();
    $('#select_ext_resource').append('<option value="0">Select One</option>');

    if (projectID == undefined || projectID == null || projectID == '' || isNaN(parseInt(projectID))) {
        console.log("Project id is wrong");
        pwIsf.alert({ msg: 'Wrong Project Id', type: 'error' });
    }
    else {
        $.isf.ajax({
            type: "GET",
            url: service_java_URL + "woManagement/getSourcesForPlan?projectId=" + parseInt(projectID),
            success: appendPlanSources,
            error: function (msg) {

            },
            complete: function (msg) {
                //getWorkOrdersTasks();
            }
        });

    }

    function appendPlanSources(data) {
        sourceDetails = data;
        var html = "";
        $.each(data, function (i, d) {
            $('#select_ext_resource').append('<option value="' + d.sourceId + '">' + d.sourceName + '</option>');
        })
    }
}

/*----------------Save Plan on Adding subactivities----------------------*/

function saveDeliverablePlan(panelWOID) {
    //externalWorkplanTemplate = $("#ext_source_wo_template").val();
    tasksArray = gantt.getTaskByTime();
    var workflowArray = [];
    $.each(tasksArray, function (i, d) {
        if (tasksArray[i].workflow != "")
            workflowArray.push(tasksArray[i].workflow);
        var julFirstDay = Math.ceil((new Date(2018, 6, 1).getTime()) / 86400000);

        var taskDate = new Date(tasksArray[i].start_date);
        var taskStartDate = Math.ceil((new Date(taskDate.getFullYear(), taskDate.getMonth(), taskDate.getDate()).getTime()) / 86400000);
        var dayOfYearFromJuly = Math.abs(taskStartDate - julFirstDay + 1);

        var tid = tasksArray[i].id;
        var task = gantt.getTask(tid);
        task.day = dayOfYearFromJuly;
        task.hour = new Date(tasksArray[i].start_date).getHours();
        gantt.updateTask(tid);
      
    });
    linksArray = [];
    linksArray = gantt.getLinks();
    if (tasksArray.length != 0 && workflowArray.length == tasksArray.length) {
        $.each(tasksArray, function (i, d) {
            tasksArray[i].start_date = new Date(moment(tasksArray[i].start_date).toDate().toString().split("GMT")[0] + "+0530 (India Standard Time)")
            tasksArray[i].end_date = new Date(moment(tasksArray[i].end_date).toDate().toString().split("GMT")[0] + "+0530 (India Standard Time)")
        });
        var executionPlanArr = [];
        var executionPlanObj = new Object();
        executionPlanObj.projectId = projectID;
        executionPlanObj.planName = planName;
        executionPlanObj.externalProjectId = externalProjectID;
        executionPlanObj.planSourceid = sourceID;
        executionPlanObj.externalWorkplanTemplate = externalWorkplanTemplate;
        executionPlanObj.planExternalReference = externalReference;
        executionPlanObj.currentUser = signumGlobal;
        executionPlanObj.subactivityId = subActivityID;
        executionPlanObj.scopeId = scopeID;
        executionPlanObj.tasks = tasksArray;
        executionPlanObj.links = linksArray;
        executionPlanObj.duplicateExecutionPlan = false;
        var JsonObj = JSON.stringify(executionPlanObj);

        $.isf.ajax({
            type: "POST",
            contentType: 'application/json',
            url: service_java_URL + "woManagement/saveDeliverablePlan",
            data: JsonObj,
            success: planSaved,
            error: function (msg) {
                pwIsf.alert({ msg: msg.responseJSON.errorMessage, type: 'warning' });
                CloseSliderView();
            },
            complete: function (msg) {
                $("#saveDeliverable").addClass("disabled");
            }
        });
        function planSaved(data) {

            if ((data.isValidationFailed == true) && (data.formWarningCount == 1)) {
                pwIsf.confirm({
                    title: 'Deliverable Plan Updation', msg: "" + data.formErrors[0],
                    'buttons': {
                        'Yes': {
                            'action': function () {
                                //let service_java_URL = "http://169.144.28.204:8080/isf-rest-server-java/";

                                updateDeliverablePlan(panelWOID);
                            }
                        },
                        'No': {
                            'action': function () {  /* errorHandler(data);*/ }
                        },

                    }
                });
            }
            else if (data.isValidationFailed == true) {

                pwIsf.confirm({
                    title: 'Deliverable Plan Updation', msg: "" + data.formErrors[0],
                    'buttons': {
                        'OK': {
                            'action': function () {
                                //let service_java_URL = "http://169.144.28.204:8080/isf-rest-server-java/";


                            }
                        }


                    }
                });

            }
            else {
                if (panelWOID == 0) {
                    //getWorkOrdersTasks();
                    $('#modal_wo_creation_ep').modal('hide');
                }
                //updateDeliverablePlan(panelWOID);
                // savePlan(panelWOID);
                pwIsf.alert({ msg: 'Deliverable Plan Saved Successfully', type: 'success' });
                getTableInfoScope();
                CloseSliderView();
            }

        }

    }
    else {
        if (tasksArray.length != 0) {
            pwIsf.alert({ msg: 'Please add all the workflows to Save Deliverable Plan', type: 'warning' });
        }
        else {
            pwIsf.alert({ msg: 'Deliverable Plan cannot be saved without subactivity.', type: 'warning' });
        }

    }

}
//saving Plan
function savePlan(panelWOID) {
    //externalWorkplanTemplate = $("#ext_source_wo_template").val();
    tasksArray = gantt.getTaskByTime();
    var workflowArray = [];
    $.each(tasksArray, function (i, d) {
        if (tasksArray[i].workflow != "")
            workflowArray.push(tasksArray[i].workflow);
        var julFirstDay = Math.ceil((new Date(2018, 6, 1).getTime()) / 86400000);

        var taskDate = new Date(tasksArray[i].start_date);
        var taskStartDate = Math.ceil((new Date(taskDate.getFullYear(), taskDate.getMonth(), taskDate.getDate()).getTime()) / 86400000);
        var dayOfYearFromJuly = Math.abs(taskStartDate - julFirstDay + 1);

        var tid = tasksArray[i].id;
        var task = gantt.getTask(tid);
        task.day = dayOfYearFromJuly;
        task.hour = new Date(tasksArray[i].start_date).getHours();
        gantt.updateTask(tid);
        //tasksArray[i].start_date = new Date(moment(tasksArray[i].start_date,).toDate().toString().split("GMT")[0] + "+0530 (India Standard Time)")
        //tasksArray[i].end_date = new Date(moment(tasksArray[i].end_date).toDate().toString().split("GMT")[0] + "+0530 (India Standard Time)")
        //tasksArray[i].end_date = moment(tasksArray[i].end_date).tz('Asia/Kolkata').toDate();
        //tasksArray[i].start_date = moment.tz(tasksArray[i].start_date.toString().split("GMT")[0] + "+0530 (India Standard Time)", localStorage.getItem("UserTimeZone")).toDate()
        //tasksArray[i].end_date = moment.tz(tasksArray[i].start_date.toString().split("GMT")[0] + "+0530 (India Standard Time)", localStorage.getItem("UserTimeZone")).toDate()
    });
    linksArray = [];
    linksArray = gantt.getLinks();
    if (tasksArray.length != 0 && workflowArray.length == tasksArray.length) {
        $.each(tasksArray, function (i, d) {
            tasksArray[i].start_date = new Date(moment(tasksArray[i].start_date).toDate().toString().split("GMT")[0] + "+0530 (India Standard Time)")
            tasksArray[i].end_date = new Date(moment(tasksArray[i].end_date).toDate().toString().split("GMT")[0] + "+0530 (India Standard Time)")
        });
        var executionPlanArr = [];
        var executionPlanObj = new Object();
        executionPlanObj.projectId = projectID;
        executionPlanObj.planName = planName;
        executionPlanObj.externalProjectId = externalProjectID;
        executionPlanObj.planSourceid = sourceID;
        executionPlanObj.externalWorkplanTemplate = externalWorkplanTemplate;
        executionPlanObj.planExternalReference = externalReference;
        executionPlanObj.currentUser = signumGlobal;
        executionPlanObj.subactivityId = subActivityID;
        executionPlanObj.scopeId = scopeID;
        executionPlanObj.tasks = tasksArray;
        executionPlanObj.links = linksArray;
        executionPlanObj.duplicateExecutionPlan = true;
        var JsonObj = JSON.stringify(executionPlanObj);

        $.isf.ajax({
            type: "POST",
            contentType: 'application/json',
            url: service_java_URL + "woManagement/saveDeliverablePlan",
            data: JsonObj,
            success: function (data) {

                pwIsf.alert({ msg: 'Deliverable Plan Saved Successfully', type: 'success' });
                getTableInfoScope();
                CloseSliderView();
            },
            error: function (msg) {
                pwIsf.alert({ msg: 'Error Updating Deliverable Plan', type: 'warning' });
                CloseSliderView();
            },
            complete: function (msg) {
                //$("#saveDeliverable").addClass("disabled");
                pwIsf.removeLayer();
            }
        });


    }
    else {
        if (tasksArray.length != 0) {
            pwIsf.alert({ msg: 'Please add all the workflows to Save Deliverable Plan', type: 'warning' });
        }
        else {
            pwIsf.alert({ msg: 'Deliverable Plan cannot be saved without subactivity.', type: 'warning' });
        }

    }

}



//Updating Deliverable plan

function updateDeliverablePlan(panelWOID) {
    //externalWorkplanTemplate = $("#ext_source_wo_template").val();
    tasksArray = gantt.getTaskByTime();
    var workflowArray = [];
    $.each(tasksArray, function (i, d) {
        if (tasksArray[i].workflow != "")
            workflowArray.push(tasksArray[i].workflow);
        var julFirstDay = Math.ceil((new Date(2018, 6, 1).getTime()) / 86400000);

        var taskDate = new Date(tasksArray[i].start_date);
        var taskStartDate = Math.ceil((new Date(taskDate.getFullYear(), taskDate.getMonth(), taskDate.getDate()).getTime()) / 86400000);
        var dayOfYearFromJuly = Math.abs(taskStartDate - julFirstDay + 1);

        var tid = tasksArray[i].id;
        var task = gantt.getTask(tid);
        task.day = dayOfYearFromJuly;
        task.hour = new Date(tasksArray[i].start_date).getHours();
        gantt.updateTask(tid);
        //tasksArray[i].start_date = new Date(moment(tasksArray[i].start_date,).toDate().toString().split("GMT")[0] + "+0530 (India Standard Time)")
        //tasksArray[i].end_date = new Date(moment(tasksArray[i].end_date).toDate().toString().split("GMT")[0] + "+0530 (India Standard Time)")
        //tasksArray[i].end_date = moment(tasksArray[i].end_date).tz('Asia/Kolkata').toDate();
        //tasksArray[i].start_date = moment.tz(tasksArray[i].start_date.toString().split("GMT")[0] + "+0530 (India Standard Time)", localStorage.getItem("UserTimeZone")).toDate()
        //tasksArray[i].end_date = moment.tz(tasksArray[i].start_date.toString().split("GMT")[0] + "+0530 (India Standard Time)", localStorage.getItem("UserTimeZone")).toDate()
    });
    linksArray = [];
    linksArray = gantt.getLinks();
    if (tasksArray.length != 0 && workflowArray.length == tasksArray.length) {
        $.each(tasksArray, function (i, d) {
            tasksArray[i].start_date = new Date(moment(tasksArray[i].start_date).toDate().toString().split("GMT")[0] + "+0530 (India Standard Time)")
            tasksArray[i].end_date = new Date(moment(tasksArray[i].end_date).toDate().toString().split("GMT")[0] + "+0530 (India Standard Time)")
        });
        var executionPlanArr = [];
        var executionPlanObj = new Object();
        executionPlanObj.projectId = projectID;
        executionPlanObj.planName = planName;
        executionPlanObj.externalProjectId = externalProjectID;
        executionPlanObj.planSourceid = sourceID;
        executionPlanObj.externalWorkplanTemplate = externalWorkplanTemplate;
        executionPlanObj.planExternalReference = externalReference;
        executionPlanObj.currentUser = signumGlobal;
        executionPlanObj.subactivityId = subActivityID;
        executionPlanObj.scopeId = scopeID;
        executionPlanObj.tasks = tasksArray;
        executionPlanObj.links = linksArray;
        executionPlanObj.duplicateExecutionPlan = true;
        var JsonObj = JSON.stringify(executionPlanObj);

        $.isf.ajax({
            type: "POST",
            contentType: 'application/json',
            url: service_java_URL + "woManagement/saveDeliverablePlan",
            data: JsonObj,
            success: function (data) {

                pwIsf.alert({ msg: 'Deliverable Plan Updated Successfully', type: 'success' });
                getTableInfoScope();
                CloseSliderView();
            },
            error: function (msg) {
                pwIsf.alert({ msg: 'Error Updating Deliverable Plan', type: 'warning' });
                CloseSliderView();
            },
            complete: function (msg) {
                //$("#saveDeliverable").addClass("disabled");
                pwIsf.removeLayer();
            }
        });


    }
    else {
        if (tasksArray.length != 0) {
            pwIsf.alert({ msg: 'Please add all the workflows to Save Deliverable Plan', type: 'warning' });
        }
        else {
            pwIsf.alert({ msg: 'Deliverable Plan cannot be saved without subactivity.', type: 'warning' });
        }

    }

}


/*-----------------Open Modal & Render Gantt(both new and saved)---------------------*/

function planDeliverable(panelWOID, externalProjID, hasDeliverablePlan, scopeID, scopeName, source, extRef, extWoTemp) {
    sourceID = source;
    externalReference = extRef;
    externalProjectID = externalProjID;
    externalWorkplanTemplate = unescape(extWoTemp);
    planName = scopeName;
    var UserTimeZone = localStorage.getItem("UserTimeZone");
    moment.tz.setDefault("Asia/Kolkata");
    //inputDate = moment(object[x]).tz('Asia/Kolkata');
    //var newVal = moment.tz(inputDate, UserTimeZone).format('YYYY-MM-DD HH:mm');
    if (!panelWOID == 0) {

        $.isf.ajax({
            url: service_java_URL + "woManagement/getDeliverablePlan?scopeId=" + scopeID,
            success: function (data) {
                GlobDelPlanID = data.executionPlanId;
                if (data.length == 0) {

                }
                else {
                    var taskObject = {};
                    var taskArray = [];
                    var linksArray = [];
                    for (var i = 0; i < data.tasks.length; i++) {
                        if (data.tasks[i].executionPlanId == 0) {
                            var customTaskObj = {};
                            customTaskObj.text = data.tasks[i].subActivityDetails;
                            customTaskObj.subActivityID = data.tasks[i].subActivityID;
                            customTaskObj.scopeId = scopeID;
                            customTaskObj.workflow = "";
                            customTaskObj.activityScopeId = data.tasks[i].activityScopeId;
                            //customTaskObj.start_date = moment("2018-07-01 00:00:00").toDate();
                            //customTaskObj.end_date = moment("2018-07-01 01:00:00").toDate();
                            //customTaskObj.start_date = moment("30-06-2018 18:30:00", 'DD-MM-YYYY HH:mm:ss').tz('Asia/Kolkata').format('DD-MM-YYYY HH:mm:ss');
                            //customTaskObj.end_date = moment("30-06-2018 19:30:00", 'DD-MM-YYYY HH:mm:ss').tz('Asia/Kolkata').format('DD-MM-YYYY HH:mm:ss');
                            //customTaskObj.start_date = moment.tz("2018-07-01 00:00:00", 'Asia/Kolkata').format('YYYY-MM-DD HH:mm');
                            //customTaskObj.end_date = moment.tz("2018-07-01 01:00:00", 'Asia/Kolkata').format('YYYY-MM-DD HH:mm');
                            //customTaskObj.start_date = new Date(2018, 06, 01, 0, 0, 00, 0);
                            //customTaskObj.end_date = new Date(2018, 06, 01, 1, 0, 00, 0);
                            //customTaskObj.start_date = new Date(2018, 06, 30, 18, 30, 00, 0);
                            //customTaskObj.end_date = new Date(2018, 06, 30, 19, 30, 00, 0);
                            customTaskObj.start_date = "30-06-2018 18:30:00";
                            customTaskObj.end_date = "30-06-2018 19:30:00";
                            taskArray.push(customTaskObj);
                        }
                        else {
                            if (data.tasks[i].active) {
                                taskArray.push(data.tasks[i]);
                                linksArray = data.links;
                            }
                        }
                    }
                    $.each(taskArray, function (i, d) {
                        //1 utc get
                        var taskSDate = taskArray[i].start_date;
                        var taskEDate = taskArray[i].end_date;
                        //2 utc to ist
                        var gmtSDateTime = moment.utc(taskSDate, "DD-MM-YYYY HH:mm:ss");
                        var sdate = gmtSDateTime.tz('Asia/Kolkata').format('DD-MM-YYYY HH:mm:ss');

                        var gmtEDateTime = moment.utc(taskEDate, "DD-MM-YYYY HH:mm:ss");
                        var edate = gmtEDateTime.tz('Asia/Kolkata').format('DD-MM-YYYY HH:mm:ss');
                        //3 TZ set
                        var newStartDate = moment(sdate, 'DD-MM-YYYY HH:mm:ss').tz('Asia/Kolkata').format('DD-MM-YYYY HH:mm:ss');
                        var newEndDate = moment(edate, 'DD-MM-YYYY HH:mm:ss').tz('Asia/Kolkata').format('DD-MM-YYYY HH:mm:ss');
                        //4 TZ - > UTC
                        //var gsdate = moment.utc(newStartDate, "DD-MM-YYYY HH:mm:ss").format('DD-MM-YYYY HH:mm:ss');
                        //var gedate = moment.utc(newEndDate, "DD-MM-YYYY HH:mm:ss").format('DD-MM-YYYY HH:mm:ss');


                        taskArray[i].start_date = newStartDate;
                        taskArray[i].end_date = newEndDate;
                    });
                    taskObject.data = taskArray;
                    taskObject.links = linksArray;
                    generateWOGantt1(1);
                    gantt.parse(taskObject);
                }
                if (hasDeliverablePlan) {
                    var html = '<a class="CellWithComment icon-add lsp"><i class="icon-add lsp fa fa-file-powerpoint-o">&nbsp;' + scopeName + '' + ' (' + data.executionPlanId + ')&nbsp; </i>' + ' <span class="CellComment"><ul style="list-style-type:none;padding-left:3px;"><li>Project ID: ' + data.projectId + '</li><li>Scope ID: ' + scopeID + '</li><li>DeliverablePlan ID:' + data.executionPlanId + '</li></ul></span></a>';
                    $("#calendar_title").empty().append(html);
                }
                else {
                    var html = '<a class="CellWithComment icon-plan lsp"><i class="icon-plan lsp fa fa-file-powerpoint-o"></i>&nbsp;' + scopeName + '<span class="CellComment"><ul style="list-style-type:none;padding-left:3px;"><li>Project ID: ' + projectID + '</li><li>Scope ID: ' + scopeID + '</li></ul></span></a>';
                    $("#calendar_title").empty().append(html);
                    dhtmlx.message({ text: "Important! Please add all the workflows before Saving the Plan!", expire: 6000, type: "customCss" });
                }
            },
            error: function (msg) {
                pwIsf.alert({ msg: msg.responseJSON.errorMessage, type: "error" });
                CloseSliderView();
            },
            complete: function (msg) {
            }
        });
    }
    else {


    }

}

/*--------------Gantt config function------------------------*/

function generateWOGantt1(panelWOID) {
    var UserTimeZone = localStorage.getItem("UserTimeZone");
    gantt = Gantt.getGanttInstance(); //new instance for multiple charts
    gantt.config.drag_progress = false;
    gantt.config.show_errors = false;
    gantt.config.server_utc = false;
    gantt.config.grid_resize = true;
    gantt.config.start_date = new Date(2018, 06, 01);
    gantt.config.end_date = new Date(2018, 08, 29);
    //gantt.config.start_date = moment.tz("2018-07-01 00:00:00", 'Asia/Kolkata').format('YYYY-MM-DD HH:mm');
    //gantt.config.end_date = moment.tz("2018-09-29 00:00:00", 'Asia/Kolkata').format('YYYY-MM-DD HH:mm');


    gantt.config.scale_unit = "day";
    gantt.config.date_scale = "%d";

    gantt.attachEvent("onGanttReady", function () {
        gantt.templates.date_scale = function (date) {
            var month = date.getMonth();

            if (month == 6) {
                return "Day" + gantt.date.date_to_str(gantt.config.date_scale)(date);
            }
            else if (month == 7) {
                var day = (parseInt(gantt.date.date_to_str(gantt.config.date_scale)(date))) + 31; //Next Starts from 32
                return "Day" + day.toString();
            }
            else if (month == 8) {
                var day = parseInt(gantt.date.date_to_str(gantt.config.date_scale)(date)) + 62; //Next startrs from 63
                return "Day" + day.toString();
            }


        };
    });

    //Task Customisation
    gantt.config.columns = [
        {
            name: "buttons", label: "Action", width: '50', align: "center", template: function (task) {
                return '<a title="Click to Add Workflow and Edit Duration of Timeline" onclick="editTaskDP(' + task.id + ')"><i class="fa fa-edit" style="color:#17b2fb;font-weight:bold;"></i></a>' +
                    '<a title = "Click to Duplicate the Subactivity and Edit WorkFlow" onclick = "duplicateTaskDP(' + task.id + ')" > <i class="fa fa-copy" style="color:#17b2fb;font-weight:bold;"></i></a >' +
                    '<a title = "Click to Delete the Subactivity" onclick = "deleteTaskDP(' + task.id + ')" > <i class="fa fa-times" style="color:#17b2fb;font-weight:bold;"></i></a >';
            }
        },
        { name: "text", label: "Activity/SubActivity Name", tree: false, width: 250, resize: true },
        { name: "workflow", label: "WorkFlow", align: "center", tree: false, width: 100, resize: true },
        { name: "duration", label: "SLA", align: "center", width: 100, resize: true }
    ];

    gantt.templates.tooltip_text = function (start, end, task) {
        return "<b>Activity/SubActivity:</b> " + task.text + "<br/><b>SLA:</b> " + task.duration;
    };

    gantt.attachEvent("onLinkDblClick", function (id, e) {
        return true;
    });

    gantt.templates.task_text = function (start, end, task) {
        return task.text;
    };

    gantt.attachEvent("onBeforeLinkAdd", function (id, link) {
        if (link.type == 2 || link.type == 3) {
            pwIsf.alert({ msg: 'This relation is not supported!', type: 'warning' });
            return false;
        }
    });


    gantt.attachEvent("onBeforeLinkUpdate", function (id, new_item) {
        $("#saveDeliverable").removeClass("disabled");
        return true;
    });

    gantt.attachEvent("onBeforeTaskUpdate", function (id, mode, task) {
        $("#saveDeliverable").removeClass("disabled");
        return true;
    });

    gantt.attachEvent("onBeforeLinkDelete", function (id, item) {
        $("#saveDeliverable").removeClass("disabled");
        return true;
    });

    gantt.attachEvent("onBeforeLinkAdd", function (id, link) {
        $("#saveDeliverable").removeClass("disabled");
        return true;
    });

    gantt.attachEvent("onLightboxSave", function (id, task, is_new) {
        if ($("#subActivityDetails").val() == 0 || $("#subActivityDetails").val() == null) {
            pwIsf.alert({ msg: 'Please select required fields!', type: 'warning' });
            return false;
        }
        else
            return true;
    })

    gantt.attachEvent("onLightbox", function (id) {
        var task = gantt.getTask(id);
        $('.gantt_duration').find('span').css("display", "none");
        Glob_SubActivityName = task.text;
        Glob_SubActivityId = task.subActivityID;
        var serviceUrl = service_java_URL + "flowchartController/getWFBySubActivityId?projectId=" + localStorage.getItem("views_project_id") + "&subActivityId=" + Glob_SubActivityId;
        if (ApiProxy == true) {
            serviceUrl = service_java_URL + "flowchartController/getWFBySubActivityId?projectId=" + localStorage.getItem("views_project_id") + encodeURIComponent("&subActivityId=" + Glob_SubActivityId);
        }
        pwIsf.addLayer({ text: "Please wait ..." });
        $.isf.ajax({
            type: "GET",
            url: serviceUrl,
            success: appendPlanWFList,
            error: function (msg) {

            },
            complete: function (msg) {
                pwIsf.removeLayer();
            }
        });
        function appendPlanWFList(data) {
            $('#subActivityDetails').empty();
            $('#daySelect').empty();
            $('#hourSelect').empty();
            $('#subActivityDetails').append('<option value="0">Select One</option>');
            for (var i = 1; i <= 90; i++) {
                $("#daySelect").append('<option value="' + i + '">' + i + '</option>');
            }
            for (var i = 0; i < 24; i++) {
                if (i < 10)
                    $("#hourSelect").append('<option value="' + i + '">0' + i + ':00</option>');
                else
                    $("#hourSelect").append('<option value="' + i + '">' + i + ':00</option>');
            }
            $('#subActivityDetails').select2({ dropdownParent: $("#my_form") });

            $.each(data, function (i, d) {
                $('#subActivityDetails').append('<option name="' + d.WorkFlowName + '" value="' + d.WFID + '/' + d.VersionNumber + '">' + d.WFID + '/' + d.WorkFlowName + '/' + d.VersionNumber + '</option>');
            })

            editTasks = gantt.getTask(id);

            var julFirstDay = Math.ceil((new Date(2018, 6, 1).getTime()) / 86400000);
            var taskDate = editTasks.start_date;
            var taskStartDate = Math.ceil((new Date(taskDate.getFullYear(), taskDate.getMonth(), taskDate.getDate()).getTime()) / 86400000);
            var dayOfYearFromJuly = Math.abs(taskStartDate - julFirstDay + 1);

            editText = editTasks.workFlowVersionNo;//subActivityDetails;
            let wfID = editTasks.workflow.split('/')[0];
            let version = editTasks.workflow.split('/')[2];
            var hour = editTasks.start_date.getHours();
            $('#daySelect').val(dayOfYearFromJuly);
            $('#hourSelect').val(hour);
            $('#subActivityDetails').val(wfID + '/' + version);
            $('#subActivityDetails').trigger('change');

        }
        if (task.$new) {
            $('#subActivityDetails').val('0').trigger('change');
            $('#daySelect').val('1');
            $('#hourSelect').val('0');
            return true;
        }
        else {
            return true;
        }

    });

    gantt.attachEvent("onAfterLightbox", function () {

    });

    gantt.attachEvent("onTaskDblClick", function (id, e) {
        if (id == null) {
            return true;
        } else {

            var versionNo = this.getTask(id).workFlowVersionNo;
            var sid = this.getTask(id).subActivityID;
            var pid = projectID;
            var wfid = this.getTask(id).workflow.split('/')[0];
            showWOModel(versionNo, sid, pid, wfid);
            return false;
        }
    });

    gantt.attachEvent("onLightboxSave", function (id, task, is_new) {
        customDuration = (newEndDate - newStartDate) / 3600000;
        task.start_date = newStartDate;
        task.text = Glob_SubActivityName;
        task.end_date = newEndDate;
        task.day = taskDayNo;
        task.hour = taskHours;
        task.duration = customDuration;
        gantt.updateTask(id);
        return true;
    })

    //Remove Add button from the subactivities
    gantt.templates.grid_row_class = function (start, end, task) {

        if (task.$level >= 0) {
            return "nested_task"
        }
        return "";
    };

    gantt.config.undo = true;
    gantt.config.redo = true;
    gantt.config.duration_unit = "hour";
    gantt.config.min_column_width = 25;
    gantt.config.subscales = [
        { unit: "hour", step: 1, date: "%G" }
    ];

    //Customise the Form Popup
    gantt.form_blocks["my_editor"] = {
        render: function (sns) {

            var html = '';
            html += '<div id="my_form" style="padding-left:25px;"><br/><label><b>WorkFlowID/WorkflowName/VersionNumber </b></label><div style="width: 20px;  margin-left: -8px; margin-top: -25px; font-size: 17px; color:red;">*</div><select name="description" class="select2able select2-offscreen" id="subActivityDetails"><option value="0">Select one</option></select>';
            html += '<br/><br/>';
            html += '<b>Day </b><select id="daySelect"></select>&nbsp;&nbsp;<b>Hour </b><select id="hourSelect"></select></div><br/><div style="padding-left:25px;"><label><b>SLA (Duration) </b></label></div>';

            return html;

        },
        set_value: function (node, value, task, section) {
            node.childNodes[3].value = task.workflow || "";
        },
        get_value: function (node, task, section) {
            taskStartDate = new Date(2018, 06, 01, 00, 00, 00);
            taskDayNo = node.childNodes[8].value;
            taskHours = node.childNodes[11].value;
            newStartDate = gantt.date.add(taskStartDate, parseInt(taskDayNo - 1), 'day');

            newStartDate.setHours(taskHours);
            newEndDate = gantt.date.add(taskStartDate, parseInt(taskDayNo - 1), 'day');

            newEndDate.setHours(parseInt(taskHours) + parseInt($(".gantt_duration_value").val()));

            var splitIDs = node.childNodes[3].value.split('/');
            workFlowVersionNo = splitIDs[1];
            if (workFlowVersionNo == null || workFlowVersionNo == undefined) {
                workFlowVersionNo = "";
            }
            scopeID = splitIDs[1];
            subActivityID = splitIDs[2];
            task.workFlowVersionNo = workFlowVersionNo;
            task.text = Glob_SubActivityName;
            if (workFlowVersionNo != "")
                task.workflow = $(node.childNodes[3]).select2('data')[0].text;
            return task.workflow;
        },
        focus: function (node) {
            var a = node.childNodes[2];
        }
    };

    gantt.config.lightbox.sections = [
        { name: "description", height: 200, map_to: "text", type: "my_editor", focus: true },
        { name: "time", height: 72, type: "duration", map_to: "auto" }
    ];

    gantt.attachEvent("onBeforeGanttRender", function () {
        $(".gantt_grid_data").on('scroll', function () {
            $(".gantt_data_area").scrollTop($(this).scrollTop());
        });
        $(".gantt_data_area").on('scroll', function () {
            $(".gantt_grid_data").scrollTop($(this).scrollTop());
        });
    });

    //Initialise Gantt Chart
    if (panelWOID != 0)
        gantt.init("gantt_here_dp");
}

/*--------------Fill WorkFlow DropDown in Lightbox------------------------*/

function getDataforLB(editTasks) {
    Glob_SubActivityId = editTasks.subActivityID;
    var serviceUrl = service_java_URL + "flowchartController/getWFBySubActivityId?projectId=" + localStorage.getItem("views_project_id") + "&subActivityId=" + Glob_SubActivityId;
    if (ApiProxy == true) {
        serviceUrl = service_java_URL + "flowchartController/getWFBySubActivityId?projectId=" + localStorage.getItem("views_project_id") + encodeURIComponent("&subActivityId=" + Glob_SubActivityId);
    }

    $.isf.ajax({
        type: "GET",
        url: serviceUrl,
        success: appendPlanWFList,
        error: function (msg) {

        },
        complete: function (msg) {
            pwIsf.removeLayer();
        }
    });
    function appendPlanWFList(data) {
        for (var i = 1; i <= 90; i++) {
            $("#daySelect").append('<option value="' + i + '">' + i + '</option>');
        }
        for (var i = 0; i < 24; i++) {
            if (i < 10)
                $("#hourSelect").append('<option value="' + i + '">0' + i + ':00</option>');
            else
                $("#hourSelect").append('<option value="' + i + '">' + i + ':00</option>');
        }
        $('#subActivityDetails').select2({ dropdownParent: $("#my_form") });
        $.each(data, function (i, d) {
            scopeID = d.scopeId;
            $('#subActivityDetails').append('<option value="' + d.VersionNumber + '/' + d.scopeId + '/' + d.subactivityid + '/' + d.SubActivity + '/' + d.WorkFlowName + '">' + d.ScopeName + '/' + d.domain + '/' + d.SubDomain + '/' + d.Technology + '/' + d.Activity + '/' + d.SubActivity + '/' + d.WorkFlowName + '</option>');

        })

    }
}

/*---------------Edit of tasks on Gantt-----------------------*/

function deleteTaskDP(taskID) {
    gantt.deleteTask(taskID);
    $("#saveDeliverable").removeClass("disabled");
}

function duplicateTaskDP(taskID) {
    var task = gantt.getTask(taskID);
    var allTasks = gantt.getTaskByTime();
    var length = allTasks.length;
    var clone = gantt.copy(task);
    clone.id = +(new Date());
    console.log("task", task, "clone", clone);
    gantt.addTask(clone, clone.parent, length);
    $("#saveDeliverable").removeClass("disabled");
}

function editTaskDP(taskID) {
    gantt.showLightbox(taskID);
    editTasks = gantt.getTask(taskID);
    editText = editTasks.workFlowVersionNo;
    var day = editTasks.day;
    var hour = editTasks.hour;
    $('#daySelect').val(day);
    $('#hourSelect').val(hour);
    $('#subActivityDetails').val(editText);
    $('#subActivityDetails').trigger('change');
    gantt.showLightbox(taskID);
}

/*-----------------Open Flowchart---------------------*/

function showWOModel(ID, sid, projectID, wfid) {
    flowChartOpenInNewWindow(UiRootDir + '/Project/FlowChartViewEP?subId=' + sid + '&projID=' + projectID + '&version=' + ID + '&experiencedMode=0' + '&wfid=' + wfid);
}

/*-----------------Get saved Plan details and Render Panel---------------------*/

function getWorkOrdersTasks() {
    $('#msgNodataEP').text("");
    $("#executionPlanBody").empty();
    pwIsf.addLayer({ text: "Please wait ..." });

    $.isf.ajax({
        url: service_java_URL + "woManagement/getExecutionPlans?projectId=" + localStorage.getItem("views_project_id"),
        success: function (data) {

            if (data.length == 0) {
                $('#msgNodataEP').text("No data exists!");
            } else {
                $.each(data, function (i, d) {
                    var task_name = "";

                    var task_nam = sourceDetails.map(function (task, index, array) {
                        if (d.planSourceid == task.sourceId)
                            task_name = task.sourceName;
                        return task.sourceName;
                    });

                    planNames.push(d.planName);

                    var html = "";
                    var tobj = {};
                    var isActive = "";
                    tobj.data = data[i].tasks;
                    tobj.links = data[i].links;
                    isActive = data[i].active;
                    html += '<div class="panel panel-default" style="border-radius:10px;" id="panel_executionPlan' + d.executionPlanId + '">';
                    html += '<div class="panel-heading gradient" style="border-radius:10px;text-align:left;"><span class="col-lg-4">' + d.planName + ' (' + d.executionPlanId + ')</span><span class="col-lg-2">' + task_name + '</span><span class="col-lg-4">' + d.planExternalReference + '</span>';
                    html += '<a title="View/Edit Execution Plan" class="pull-right fa fa-external-link icon-edit" href="#WOPanel' + d.executionPlanId + '" aria-expanded="false" aria-controls="WOPanel" style="text-align:center;margin-top:5px;"  onclick="onPlanCollapse(' + d.executionPlanId + ')" data-toggle="collapse"></a></div></div>';

                    //html += '<li class="gantt-menu-item gantt-menu-item-right"><a style="display:none;" title="Redo Task" onclick="redoTask(' + d.executionPlanId + ')" id="redoEP' + d.executionPlanId + '"><i class="fa fa-rotate-right" style="color:blue;font-weight:bold;"></i></a></li><li class="gantt-menu-item gantt-menu-item-right"><a style="display:none;" title="Undo Task" onclick="undoTask(' + d.executionPlanId + ')" id="undoEP' + d.executionPlanId + '"><i class="fa fa-rotate-left" style="color:blue;font-weight:bold;"></i></a></li></div>';

                    $("#executionPlanBody").prepend(html);

                })

            }
        },
        error: function (xhr, status, statusText) {

        },
        complete: function (msg) {
            pwIsf.removeLayer();
        }
    });
}