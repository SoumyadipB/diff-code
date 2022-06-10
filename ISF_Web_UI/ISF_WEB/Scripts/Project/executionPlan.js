function unlockExtProject() {
    $("#lockExP").hide();
    $("#unlockExP").show();


    var ProjectID = localStorage.getItem("views_project_id");
    var sourceId = $("#select_ext_resource option:selected").val();
    pwIsf.addLayer({ text: "Please wait ..." });

    $.isf.ajax({
        url: service_java_URL + "externalInterface/getAllExternalProjectIDByIsfProject/" + sourceId + "/" + ProjectID,
        success: function (data) {
            pwIsf.removeLayer();

            $("#select_ext_project").empty();
            $('#select_ext_project').append('<option value="0">Select External Project</option>');
            $.each(data, function (i, d) {
                $("#select_ext_project").append('<option value="' + d.externalProjectId + '">' + d.externalProjectId + '</option>');
            })

        },
        error: function (xhr, status, statusText) {
            pwIsf.removeLayer();
            console.log('An error occurred on getAllExternalProject: ' + xhr.error);
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
    $('#ext_wo_temp_list').html('');
    var sourceId = $("#select_ext_resource option:selected").val();
    var extprojID = $("#select_ext_project option:selected").val();
    $.isf.ajax({
        type: "GET",
        url: service_java_URL + "externalInterface/getActiveExternalWorkPlanTemplateList/" + sourceId + "/" + extprojID,
        success: function (data) {
            $('#ext_wo_temp_list').append('<option value ="" ></option>');
            $.each(data, function (i, d) {
                $('#ext_wo_temp_list').append('<option data-id="' + d.ParentWorkPlanTemplateName + '" value="' + d.ParentWorkPlanTemplateName + '">');

            });
        },
        error: function (msg) {

        }
    });

}

function unlockExtRef() {
    var sourceId = $("#select_ext_resource option:selected").val();
    var sourceTxt = $("#select_ext_resource option:selected").text();

    if (sourceId != "0" && sourceTxt != "ISF") {
        $("#lockExR").hide();
        $("#unlockExR").show();


        var ProjectID = localStorage.getItem("views_project_id");
        var sourceId = $("#select_ext_resource option:selected").val();
        pwIsf.addLayer({ text: "Please wait ..." });

        $.isf.ajax({
            url: service_java_URL + "externalInterface/getAllExternalActivityList/" + sourceId,
            success: function (data) {
                pwIsf.removeLayer();

                $("#externalActivityDropDown").empty();
                $('#externalActivityDropDown').append('<option value="0">Select External Reference</option>');
                $.each(data, function (i, d) {
                    $("#externalActivityDropDown").append('<option value="' + d.ExtActivityID + '">' + d.ExtActivityID + '</option>');
                })

            },
            error: function (xhr, status, statusText) {
                pwIsf.removeLayer();
                console.log('An error occurred on getallExternalActivityList: ' + xhr.error);
            }
        });
    }

}

function fillExtRef() {

    $("#unlockExR").hide();
    $("#lockExR").show();
    externalProjectId = $("#select_ext_project option:selected").val();
    var ProjectID = localStorage.getItem("views_project_id");
    var sourceId = $("#select_ext_resource option:selected").val();
    var extWoTemp = $("#ext_source_wo_template").val();
        $.isf.ajax({
            url: service_java_URL + "externalInterface/getActiveExternalActivityList/" + sourceId + "/" + ProjectID + "/" + externalProjectId + "/" + extWoTemp,
            success: function (data) {

                $("#externalActivityDropDown").empty();
                $('#externalActivityDropDown').append('<option value="0">Select External Reference</option>');
                if (data !== null && data !== undefined) {
                   
                        if (data.responseData.length > 0) {
                            $.each(data.responseData, function (i, d) {
                                $("#externalActivityDropDown").append('<option value="' + d.ExternalActivityName + '">' + d.ExternalActivityName + '</option>');
                            })
                        }
                    
                   
                }
            },
            error: function (xhr, status, statusText) {
                console.log('An error occurred on getActiveExternalActivityList: ' + xhr.error);
            }
        })
    
}

function showExternalActivityDropDownIfReq() {

    $("#unlockExP").hide();
    $("#lockExP").show();
	var sourceTxt = $("#select_ext_resource option:selected").text();
	var sourceId = $("#select_ext_resource option:selected").val();
	var ProjectID = localStorage.getItem("views_project_id");
	//if (sourceTxt == 'ERISITE') {
	//	document.getElementById("externalRefTxt").style.display = 'none';
	//	document.getElementById("externalRefDD").style.display = '';
	//	$.isf.ajax({
	//		type: "GET",
	//		//url: service_java_URL + "woManagement/getSourcesForPlan/",
	//		url: service_java_URL + "/externalInterface/getActiveExternalActivityList/" + sourceId + "/" + ProjectID,
	//		success: appendInErisiteActivity,
	//		error: function (msg) {

	//		}
	//	});
	//} else {
	//	document.getElementById("externalRefTxt").style.display = '';
	//	document.getElementById("externalRefDD").style.display = 'none';
	//}
    if (sourceTxt != 'ISF' && sourceId!="0") {
        $("#hideExP").show();
        document.getElementById("externalRefTxt").style.display = 'none';
        document.getElementById("externalRefDD").style.display = '';
        $("#extRefCss").css('margin-bottom', '8px');

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
            }
        })

        $("#select_ext_project").trigger('change');
    }
    else {
        $("#hideExP").hide();
        $("#unlockExR").hide();
        $("#lockExR").hide();
        $("#extRefCss").css('margin-bottom', '0px')
        document.getElementById("externalRefTxt").style.display = '';
        document.getElementById("externalRefDD").style.display = 'none';
    }
}

function appendInErisiteActivity(data) {
	$('#externalActivityDropDown').empty();
	sourceDetails = data;
	var html = "";
	$.each(data, function (i, d) {
	    externalProjectID = d.ExternalProjectID;
	    $('#externalActivityDropDown').append('<option value="' + d.ExternalActivityName + '">' + externalProjectID + '_' +d.ExternalActivityName + '</option>');
	})
}

/*---------------Get EP Sources in dropdown-----------------------*/

function getPlanSources() {
    newplanNames = []; planNames = []; rawPlanNames = [];
    $('#plan_name-Required').text("");
    $('#select_ext_resource').empty();
    $('#select_ext_resource').append('<option value="0">Select One</option>');
    $.isf.ajax({
        type: "GET",
        //url: service_java_URL + "woManagement/getSourcesForPlan/",
        url: service_java_URL + "woManagement/getSourcesForPlan?projectId=" + projectID,
        success: appendPlanSources,
        error: function (msg) {

        },
        complete: function (msg) {
            getWorkOrdersTasks();
        }
    });

    function appendPlanSources(data) {
        sourceDetails = data;
        var html = "";
        $.each(data, function (i, d) {
            $('#select_ext_resource').append('<option value="' + d.sourceId + '">' + d.sourceName + '</option>');
        })
    }
}

/*--------------Add New EP------------------------*/

function addExecutionPlan()
{
    if (validateExecutionPlan() == true) {
        planName = $("#plan_name").val();
        rawPlanNames.push(planName);
        sourceID = $("#select_ext_resource").val();
        externalResource = $("#select_ext_resource option:selected").text();
        if (externalResource=="ISF")
            externalReference = $("#reference_name").val();
        else {
            //extProjId = $('#externalActivityDropDown :selected').text().split("-")[0];
            externalReference = $('#externalActivityDropDown :selected').text();
            externalProjectID = $("#select_ext_project option:selected").val();

        }
            
        externalReferenceText = $('#externalActivityDropDown :selected').text();
        getWOPlans(planAddedCount);
        cleanFields();
    }
}

/*--------------Validate Required fields while adding EP------------------------*/

function validateExecutionPlan() {
    var OK = true;
   
    // Validation for Plan Name
    $('#plan_name-Required').text("");
    if ($("#plan_name").val() == null || $("#plan_name").val() == "") {
        $('#plan_name-Required').text("Plan name is required");
        OK = false;
    }
    else {
        var letterNumber = /^[0-9_a-z_ _A-Z_ ]+$/;
        if (!$("#plan_name").val().match(letterNumber)) {
            $('#plan_name-Required').text("Only letters and numbers");
            OK = false;
        }
        else {
            if ($("#plan_name").val().length > 30) {
                $('#plan_name-Required').text("Plan name cannot exceed 30 characters");
                OK = false;
            }
                
        }

        newplanNames = planNames.concat(rawPlanNames);
        //Validation for Same Plan Name
        for (var i = 0; i < newplanNames.length; i++) {
            if ($("#plan_name").val().toLowerCase() == newplanNames[i].toLowerCase()) {
                $('#plan_name-Required').text("Plan already exists");
                OK = false;
            }
        }

    }

    //Validation for Source
    $("#select_ext_resource-Required").text("");
    if ($("#select_ext_resource").val() == 0 || $("#select_ext_resource").val() == null ) {
        $('#select_ext_resource-Required').text("Source is required");
        OK = false;
    }
    //Validation for Reference Name 
    $('#reference_name-Required').text("");
    if ($("#select_ext_resource").val() == 1) {
        if ($("#reference_name").val() == null || $("#reference_name").val() == "") {
            $('#reference_name-Required').text("Reference name is required");
            OK = false;
        }

        else {
            var letterNumber = /^[0-9_a-z_ _A-Z_ ]+$/;
            if (!$("#reference_name").val().match(letterNumber)) {
                $('#reference_name-Required').text("Only letters and numbers");
                OK = false;
            }
            else {
                if ($("#reference_name").val().length > 30) {
                    $('#reference_name-Required').text("Reference name cannot exceed 30 characters");
                    OK = false;
                }
            }

        }
    }
    else
        if ($('#externalActivityDropDown :selected').val() == "") {
            $('#reference_name-Required').text("Reference name is required");
            OK = false;
        }
    

    return OK;
}

/*---------------Reset after adding EP-----------------------*/

function cleanFields() {
    $("#plan_name").val("");    

    $("#select_ext_project").val("0");
    $("#select_ext_project").trigger('change');

    $("#externalActivityDropDown").val("0");
    $("#externalActivityDropDown").trigger('change');

    $("#select_ext_resource").val("0");
    $("#select_ext_resource").trigger('change');

    $("#reference_name").val("");
}

/*----------------Make Panel for storing EPs----------------------*/

function getWOPlans(panelWOID) {
    $('#msgNodataEP').text("");
    var ProjectID = localStorage.getItem("views_project_id");
    var html = "";
    html += '<div class="panel panel-default"  style="border-radius:10px;" id="panel_executionPlan' + panelWOID + '">';
    if (externalResource == "ISF")
        html += '<div class="panel-heading gradient" style="border-radius:10px;text-align:left;"><span class="col-lg-4">' + planName + '</span><span class="col-lg-2">' + externalResource + '</span><span class="col-lg-4">' + externalReference + '</span>';
    else
        html += '<div class="panel-heading gradient" style="border-radius:10px;text-align:left;"><span class="col-lg-4">' + planName + '</span><span class="col-lg-2">' + externalResource + '</span><span class="col-lg-4">' + externalReferenceText + '</span>';
    html += '<a title="View/Edit Execution Plan" class="pull-right fa fa-external-link icon-edit" href="#WOPanel' + panelWOID + '" aria-expanded="false" aria-controls="WOPanel" style="text-align:center;margin-top:5px;" onclick="onPlanCollapse(' + panelWOID + ')" data-toggle="collapse"></a></div></div>';
    //html += '<li class="gantt-menu-item gantt-menu-item-right"><li class="gantt-menu-item gantt-menu-item-right"><a style="display:none;" title="Redo Task" onclick="redoTask(' + panelWOID + ')" id="redoEP' + panelWOID + '"><i class="fa fa-rotate-right" style="color:blue;font-weight:bold;"></i></a></li><li class="gantt-menu-item gantt-menu-item-right"><a style="display:none;" title="Undo Task" onclick="undoTask(' + panelWOID + ')" id="undoEP' + panelWOID + '"><i class="fa fa-rotate-left" style="color:blue;font-weight:bold;"></i></a></li></div>';
    $("#executionPlanBody").prepend(html);
    generateWOGantt(panelWOID);
}

/*----------------Active/Inactive Toggle----------------------*/

function toggleSwitch(panelID)
{
    var checkbox = document.querySelector('input[id="togBtn' + panelID + '"]');
    if (panelID != 0)
    {
        $(".ganttChart").off('change').on("change", checkbox, function (e) {
            var activeInactiveObj = new Object();
            var service_data = '[{executionPlanId:"' + panelID + '",isActive:true}]';
            if (checkbox.checked) {
                activeInactiveObj.executionPlanId = panelID;
                activeInactiveObj.isActive = true;
                activeInactiveObj.createdBy = signumGlobal;
                var JsonObj = JSON.stringify(activeInactiveObj);
                $.isf.ajax({
                    type: "POST",
                    url: service_java_URL + "woManagement/updateExecutionPlanStatus/",
                    dataType: 'json',
                    contentType: "application/json; charset=utf-8",
                    data: JsonObj,
                    success: activeInactive(),
                    error: function (msg) {

                    },
                    complete: function (msg) {

                    }
                });
                function activeInactive() {
                    pwIsf.alert({ msg: 'Plan Activated successfully!', type: 'success' });
                }

            } else {
                activeInactiveObj.executionPlanId = panelID;
                activeInactiveObj.isActive = false;
                activeInactiveObj.createdBy = signumGlobal;
                var JsonObj = JSON.stringify(activeInactiveObj);
                $.isf.ajax({
                    type: "POST",
                    url: service_java_URL + "woManagement/updateExecutionPlanStatus/",
                    dataType: 'json',
                    contentType: 'application/json; charset=utf-8',
                    data: JsonObj,
                    success: activeInactive(),
                    error: function (msg) {

                    },
                    complete: function (msg) {

                    }
                });
                function activeInactive() {
                    pwIsf.alert({ msg: 'Plan Deactivated successfully!', type: 'success' });
                    $('.switch').css("display", "none");
                }

            }
        });
    }
    else
    {
        pwIsf.alert({ msg: 'Save the Plan to Activate', type: 'warning' });
    }

}

/*----------------Save Plan on Adding subactivities----------------------*/

function saveExecutionPlan(panelWOID)
{
    tasksArray = gantt.getTaskByTime();

    $.each(tasksArray, function (i, d) {

        //var setFirstDay = new Date().setFullYear(new Date().getFullYear(), 6, 1);
        //var julFirstDay = Math.floor(setFirstDay / 86400000);

        var julFirstDay = Math.ceil((new Date(2018, 6, 1).getTime()) / 86400000);

        var taskDate = tasksArray[i].start_date
        var taskStartDate = Math.ceil((new Date(taskDate.getFullYear(), taskDate.getMonth(), taskDate.getDate()).getTime()) / 86400000);
        var dayOfYearFromJuly = Math.abs(taskStartDate - julFirstDay + 1);

        var tid = tasksArray[i].id;
        var task = gantt.getTask(tid);
        task.day = dayOfYearFromJuly;
        task.hour = tasksArray[i].start_date.getHours();
        gantt.updateTask(tid);

    });
    

    linksArray = [];
    //tasksArray = gantt.getTaskByTime();
    linksArray = gantt.getLinks();
    if (tasksArray.length != 0 || panelWOID != 0) {
        var executionPlanArr = [];
        var executionPlanObj = new Object();
        executionPlanObj.executionPlanId = panelWOID;
        executionPlanObj.projectId = projectID;
        executionPlanObj.planName = planName;
        executionPlanObj.externalProjectId = externalProjectID;
        executionPlanObj.planSourceid = sourceID;
        //if(sourceID == 2)
        //    executionPlanObj.planExternalReference = externalProjectID+"_"+externalReference;
        //else
            executionPlanObj.planExternalReference = externalReference;
        executionPlanObj.currentUser = signumGlobal;
        executionPlanObj.subactivityId = subActivityID;
        executionPlanObj.scopeId = scopeID;
        executionPlanObj.tasks = tasksArray;
        executionPlanObj.links = linksArray;
                                     
        var JsonObj = JSON.stringify(executionPlanObj);
        $.isf.ajax({
            type: "POST",
            contentType: 'application/json',
            url: service_java_URL + "woManagement/saveExecutionPlan",
            data: JsonObj,
            success: planSaved,
            error: function (msg) {
                //on success return a flag on that basis show error
                //{msg:"",data:{} }
                pwIsf.alert({ msg: msg.responseJSON.errorMessage, type: 'warning' });
                //pwIsf.alert({ msg: data.msg, type: 'warning' });
            },
            complete: function (msg) {
                //pwIsf.removeLayer();
            }
        });
        function planSaved(data) {
            if (data.isValidationFailed){
                errorHandler(data);
            }
            else {
                if (panelWOID == 0) {
                    getWorkOrdersTasks();
                    $('#modal_wo_creation_ep').modal('hide');
                }

                pwIsf.alert({ msg: 'Execution Plan Saved Successfully ID: ' + data.responseData, type: 'success' });
            }
        }
    }
    else {
        pwIsf.alert({ msg: 'Please add Sub Activities to Save Plan', type: 'warning' });
    }

}

/*----------------Export Gantt Chart to PDF----------------------*/

function exportGanttPdf(panelWOID)
{
    gantt.exportToPDF({name:"ExecutionPlan"+panelWOID+".pdf",raw:true});
}

/*-----------------Get saved Plan details and Render Panel---------------------*/

function getWorkOrdersTasks()
{
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

/*-----------------Open Modal & Render Gantt(both new and saved)---------------------*/

function onPlanCollapse(panelWOID) {
    $(".ganttChart").empty();

    $("#executionPlanTitleModal").empty();

    
    if (!panelWOID == 0) {

        $.isf.ajax({
            url: service_java_URL + "woManagement/getExecutionPlans?projectId=" + localStorage.getItem("views_project_id"),
            success: function (data) {

                if (data.length == 0) {

                } else {
                    $.each(data, function (i, d) {
                        if (d.executionPlanId == panelWOID) {
                            if (!d.planSourceid == 1)
                            externalReference = d.planExternalReference;
                            sourceID = d.planSourceid;
                            sourceIDNew = d.planSourceid;
                            var task_name = "";
                            var task_nam = sourceDetails.map(function (task, index, array) {
                                if (d.planSourceid == task.sourceId)
                                    task_name = task.sourceName;
                                return task.sourceName;
                            });
                            $("#executionPlanTitleModal").append('<span class="col-lg-4"><a title="Export to PDF" class="icon-add" onclick="exportGanttPdf(' + panelWOID + ')"><i class="fa fa-file-pdf-o"></i></a>&nbsp' + d.planName + ' (' + d.executionPlanId + ')</span><span class="col-lg-2">' + task_name + '</span><span class="col-lg-4">' + d.planExternalReference + '</span><span style="font-size:10px;color:red;">Read Only Mode !!!</span><button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>')
                            var html1 = "";
                            var tobj = {};
                            var isActive = "";
                            tobj.data = data[i].tasks;
                            tobj.links = data[i].links;
                            isActive = data[i].active;
                            //html += '<li class="gantt-menu-item gantt-menu-item-right"><a style="display:none;" title="Redo Task" onclick="redoTask(' + d.executionPlanId + ')" id="redoEP' + d.executionPlanId + '"><i class="fa fa-rotate-right" style="color:blue;font-weight:bold;"></i></a></li><li class="gantt-menu-item gantt-menu-item-right"><a style="display:none;" title="Undo Task" onclick="undoTask(' + d.executionPlanId + ')" id="undoEP' + d.executionPlanId + '"><i class="fa fa-rotate-left" style="color:blue;font-weight:bold;"></i></a></li></div>';
                            html1 += '<div class="content panel-body" id="WOPanel' + panelWOID + '"><div id="movingDiv" style="float:right;margin-top:-26px;position:fixed;"><button id="saveEPButton" onclick="saveExecutionPlan(' + panelWOID + ')" class="btn btn-primary">Save Plan</button>&nbsp';
                            if (!isActive)
                                html1 += '';
                            else
                                html1 += '<label class="switch"><input type="checkbox" checked class="toggleActive' + panelWOID + '" onclick="toggleSwitch(' + panelWOID + ')" id="togBtn' + panelWOID + '"><div class="slider round"><span class="off">Inactive</span><span class="on">Active</span></div></label>';
                            html1 += '</div><div id="gantt_here_' + panelWOID + '" style="padding-top:35px;width:100%; height:400px;"></div></div></div>';
                            html1 += '</div>';
                            showModel(html1, panelWOID);
                            gantt.parse(tobj); // to get saved data trough API
                            gantt.config.readonly = true;
                            $("#saveEPButton").css("display","none");
                            //pwIsf.alert({ msg: 'View only mode as work flow is in progress!', type: 'info' });
                            //$.isf.ajax({
                            //    type: "GET",
                            //    url: service_java_URL + "woManagement/isPlanInExecution?executionPlanId=" + panelWOID,
                            //    success: ganttReadOnly,
                            //    error: function (msg) {

                            //    },
                            //    complete: function (msg) {

                            //    }
                            //});

                            //function ganttReadOnly(data) {
                            //    if (data) {
                            //        //gantt.config.readonly = true;
                            //        //$("#saveEPButton").addClass("disabledbutton");
                            //        //pwIsf.alert({ msg: 'View only mode as work flow is in progress!', type: 'info' });
                            //    }
                            //}
                                
                        }
                    })

                }
            },
            error: function (xhr, status, statusText) {
                
            },
            complete: function (msg) {
             
            }
        });
    }
    else {
        sourceIDNew = sourceID;
        if(externalResource=="ISF")
            $("#executionPlanTitleModal").append('<span class="col-lg-4">' + planName + '</span><span class="col-lg-2">' + externalResource + '</span><span class="col-lg-4">' + externalReference + '</span><button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>')
        else
            $("#executionPlanTitleModal").append('<span class="col-lg-4">' + planName + '</span><span class="col-lg-2">' + externalResource + '</span><span class="col-lg-4">' + externalReferenceText + '</span><button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>')
        var html = "";
        html += '<div class="content panel-body collapse" id="WOPanel' + panelWOID + '"><div id="movingDiv" style="float:right;margin-top:-12px;position:fixed;"><button onclick="saveExecutionPlan(' + panelWOID + ')" class="btn btn-primary">Save Plan</button>&nbsp;<label style="display:none;" class="switch"><input type="checkbox" class="toggleActive' + panelWOID + '" onclick="toggleSwitch(' + panelWOID + ')" id="togBtn' + panelWOID + '"><div class="slider round"><span class="on">Active</span><span class="off">Inactive</span></div></label>';
        html += '</div><div id="gantt_here_' + panelWOID + '" style="padding-top:35px;width:100%; height:400px;"></div></div></div>';
        html += '</div>';
        showModel(html, panelWOID);

    }

}

/*--------------------------------------*/

function undoTask(panelWOID) {
    //var undo = document.getElementById("undoEP" + panelWOID);
    //undo.onclick = function () {
    //    gantt.undo();
    //};

}

/*--------------------------------------*/

function redoTask(panelWOID) {
    //var redo = document.getElementById("redoEP" + panelWOID);
    //redo.onclick = function () {
    //    gantt.redo();
    //};
}

/*--------------Gantt config function------------------------*/

function generateWOGantt(panelWOID) {
    gantt = Gantt.getGanttInstance(); //new instance for multiple charts
    gantt.config.show_errors = false;
    gantt.config.server_utc = true;
   
    //var undo = document.getElementById("undoEP" + panelWOID);
    //undo.onclick = function () {
    //    gantt.undo();
    //};
    //var redo = document.getElementById("redoEP" + panelWOID);
    //redo.onclick = function () {
    //    gantt.redo();
    //};

    //gantt.config.start_date = gantt.date.day_start(new Date());
    gantt.config.start_date = new Date(2018, 06, 01);
    gantt.config.end_date = new Date(2018, 08, 29);

    gantt.config.autosize = "x";
    gantt.config.autosize_min_width = 800;

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
        //{
        //    name: "buttons", label: "Action", width: '50', align: "center", template: function (task)
        //    {
        //        //return '<a  id="editTasktry(' + task.id + ')"><i class="fa fa-edit" style="color:#17b2fb;font-weight:bold;"></i></a>';
        //        return '<a  onclick="editTask(' + task.id + ')"><i class="fa fa-edit" style="color:#17b2fb;font-weight:bold;"></i></a>';
        //    }
        //},
            { name: "text", label: "SubActivity/WorkFlow Name", tree: false, width: 250, resize: true },
            { name: "duration", label: "SLA (Duration)", align: "center", width: 100, resize: true }
            //{ name: "add", label: "", align: "center", width: 50, resize: true }
    ];

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
        if (task.$new) {
            $('#subActivityDetails').val('0').trigger('change');
            $('#daySelect').val('1');//.trigger('change');
            $('#hourSelect').val('0');//.trigger('change');
            return true;
        }
        else {
            //gantt.showLightbox(taskID);
            editTasks = gantt.getTask(id);
            editText = editTasks.subActivityDetails;
            var day = editTasks.day;
            var hour = editTasks.hour;
            $('#daySelect').val(day);
            $('#hourSelect').val(hour);
            $('#subActivityDetails').val(editText);
            $('#subActivityDetails').trigger('change');

            return true;
        }
        
    });

    gantt.attachEvent("onAfterLightbox", function () {
        $('#subActivityDetails').val('0').trigger('change');
        $('#daySelect').val('1');//.trigger('change');
        $('#hourSelect').val('0');//.trigger('change');
    });

    gantt.attachEvent("onTaskDblClick", function (id, e) {
        if (id == null) {
            return true;
        } else {

            var versionNo = this.getTask(id).workFlowVersionNo;
            var sid = this.getTask(id).subActivityID;
            var pid = projectID;
            showWOModel(versionNo, sid, pid);
            return false;
        }
    });


    gantt.attachEvent("onLightboxSave", function (id, task, is_new) {
        task.start_date = newStartDate;
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

    //gantt.config.autofit = true;
    gantt.config.fit_tasks = true;

    gantt.templates.tooltip_text = function (start, end, task) {
        return "<b>SubActivity/WorkFlow:</b> " + task.text + "<br/><b>Day:</b> " + task.day + "<br/><b>Hour:</b>  " + task.hour + "";
    };

    // For Functionalities like undo, redo, fullscreen
    //gantt.attachEvent("onTemplatesReady", function () {
    //    var toggle = document.createElement("i");
    //    toggle.className = "fa fa-expand gantt-fullscreen";
    //    gantt.toggleIcon = toggle;
    //    gantt.$container.appendChild(toggle);
    //    toggle.onclick = function () {
    //        if (!gantt.getState().fullscreen) {
    //            gantt.expand();
    //        }
    //        else {
    //            gantt.collapse();
    //        }
    //    };
    //});
    //gantt.attachEvent("onExpand", function () {
    //    var icon = gantt.toggleIcon;
    //    if (icon) {
    //        icon.className = icon.className.replace("fa-expand", "fa-compress");
    //    }

    //});
    //gantt.attachEvent("onCollapse", function () {
    //    var icon = gantt.toggleIcon;
    //    if (icon) {
    //        icon.className = icon.className.replace("fa-compress", "fa-expand");
    //    }    

    //Customise the Form Popup
    gantt.form_blocks["my_editor"] = {
        render: function (sns) {
            
            var html = '';
            html += '<div id="my_form" style="padding-left:25px;"><br/><label><b>Deliverable/ Domain/ Sub Domain/ Technology/ Activity/ Sub Activity/ Workflow Name </b></label><div style="width: 20px;  margin-left: -8px; margin-top: -25px; font-size: 17px; color:red;">*</div><select name="description" class="select2able select2-offscreen" id="subActivityDetails"><option value="0">Select one</option></select>';
            html += '<br/><br/>';
            html += '<b>Day </b><select id="daySelect"></select>&nbsp;&nbsp;<b>Hour </b><select id="hourSelect"></select></div><br/><div style="padding-left:25px;"><label><b>SLA (Duration) </b></label></div>';
            pwIsf.addLayer({ text: "Please wait ..." });
            var serviceUrl = service_java_URL + "woManagement/getWfListForPlan?projectID=" + localStorage.getItem("views_project_id") + "&sourceId=" + sourceIDNew;
            if (ApiProxy == true) {
                serviceUrl = service_java_URL + "woManagement/getWfListForPlan?projectID=" + localStorage.getItem("views_project_id") + encodeURIComponent("&sourceId=" + sourceIDNew);
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
                var html = "";
                $('#subActivityDetails').select2({ dropdownParent: $("#my_form") });
                $.each(data, function (i, d) {
                        scopeID = d.scopeId;
                        $('#subActivityDetails').append('<option value="' + d.VersionNumber + '/' + d.scopeId + '/' + d.subactivityid + '/' + d.SubActivity + '/' + d.WorkFlowName + '">' + d.ScopeName + '/' + d.domain + '/' + d.SubDomain + '/' + d.Technology + '/' + d.Activity + '/' + d.SubActivity + '/' + d.WorkFlowName + '</option>');
                       // $('#subActivityDetails').select2();
                })
               
            }
            //$(".ganttChart").append(html);
            return html;

        },
        set_value: function (node, value, task, section) {
            node.childNodes[3].value = task.subActivityDetails || "";
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
            workFlowVersionNo = splitIDs[0];
            scopeID = splitIDs[1];
            subActivityID = splitIDs[2];
            task.workFlowVersionNo = workFlowVersionNo;
            task.subActivityDetails = node.childNodes[3].value;
            task.scopeId = scopeID;
            task.subActivityID = subActivityID;
            return splitIDs[3] + "/" + splitIDs[4];
        },
        focus: function (node) {
            var a = node.childNodes[2];
            
        }
    };

    gantt.config.lightbox.sections = [
        { name: "description", height: 200, map_to: "text", type: "my_editor", focus: true },
        { name: "time", height: 72, type: "duration", map_to: "auto" }
    ];

    //Initialise Gantt Chart
    if (panelWOID != 0)
        gantt.init("gantt_here_" + panelWOID);
}

/*---------------Edit of tasks on Gantt-----------------------*/

function editTask(taskID)
{
    //trial();
    gantt.showLightbox(taskID);    
    editTasks = gantt.getTask(taskID);
    editText = editTasks.subActivityDetails;
    var day = editTasks.day;
    var hour = editTasks.hour;
    $('#daySelect').val(day);
    $('#hourSelect').val(hour);
    $('#subActivityDetails').val(editText);
    $('#subActivityDetails').trigger('change');
    gantt.showLightbox(taskID);
    
    
}

/*----------------Redirects to modal of Gantt----------------------*/

function showModel(html1, pwid) {
    $("#modal_wo_creation_ep").modal('show');
    $(".ganttChart").append(html1);

    generateWOGantt(pwid);
    if (pwid == 0)
        gantt.init("gantt_here_" + pwid);
}

/*-----------------Open Flowchart---------------------*/

//function showWOModel(ID, sid, projectID)
//{
//    flowChartOpenInNewWindow(UiRootDir + '/Project/FlowChartViewEP?subId=' + sid + '&projID=' + projectID + '&version=' + ID + '&experiencedMode=0');
//}

function trial() {
    var html = '';
    html += '<div style="padding-left:25px;"><br/><label><b>Deliverable/ Domain/ Sub Domain/ Technology/ Activity/ Sub Activity/ Workflow Name </b></label><div style="width: 20px;  margin-left: -8px; margin-top: -25px; font-size: 17px; color:red;">*</div><select class="select2able select2-offscreen" id="subActivityDetails"><option value="0">Select one</option></select>';
    html += '<br/><br/>';
    html += '<b>Day </b><select id="daySelect"></select>&nbsp;&nbsp;<b>Hour </b><select id="hourSelect"></select></div><br/><div style="padding-left:25px;"><label><b>SLA (Duration) </b></label></div>';
    //pwIsf.addLayer({ text: "Please wait ..." });

    //$.isf.ajax({
    //    type: "GET",
    //    url: service_java_URL + "woManagement/getWfListForPlan?projectID=" + localStorage.getItem("views_project_id"),
    //    success: appendPlanWFList,
    //    error: function (msg) {

    //    },
    //    complete: function (msg) {
    //        pwIsf.removeLayer();
    //    }
    //});
    //function appendPlanWFList(data) {
    //    for (var i = 1; i <= 90; i++) {
    //        $("#daySelect").append('<option value="' + i + '">' + i + '</option>');
    //    }
    //    for (var i = 0; i < 24; i++) {
    //        if (i < 10)
    //            $("#hourSelect").append('<option value="' + i + '">0' + i + ':00</option>');
    //        else
    //            $("#hourSelect").append('<option value="' + i + '">' + i + ':00</option>');
    //    }
    //    var html = "";
    //    $('#subActivityDetails').select2();
    //    $.each(data, function (i, d) {
    //        scopeID = d.scopeId;
    //        $('#subActivityDetails').append('<option value="' + d.VersionNumber + '/' + d.scopeId + '/' + d.subactivityid + '/' + d.SubActivity + '/' + d.WorkFlowName + '">' + d.ScopeName + '/' + d.domain + '/' + d.SubDomain + '/' + d.Technology + '/' + d.Activity + '/' + d.SubActivity + '/' + d.WorkFlowName + '</option>');
    //        // $('#subActivityDetails').select2();
    //    })

    //}
    return html;
}