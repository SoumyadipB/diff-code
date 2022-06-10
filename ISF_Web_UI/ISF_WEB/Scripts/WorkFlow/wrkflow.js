var rpaid = [];
let Global_newAddedCells = [];
function getRPAId(projectId) {
    type:'GET',
    $.isf.ajax({
        url: service_java_URL + "rpaController/getRPADeployedDetails/" + projectId,
        async: false,
        success: function (data) {
            rpaid = [];
            var html = "";
            //    $('#rpaIds').html('');
            //    $('#rpaIds').append('<option disabled selected value> -- select an option -- </option>');

            $.each(data, function (i, d) {
                if (d !== null)
                    //   $('#rpaIds').append('<option value="' + d.bOTName + '">' + d.automation_Plugin + '</option>');
                    rpaid.push({ value: d.bOTName + '@' + d.automation_Plugin, content: '<span style="font-family: Alegreya Sans">' + d.automation_Plugin + '</span>' });
                //  a.push(d.bOTName);

            })



        },
        complete: function () {
            // $('#rpaIds').find("option").eq(0).remove();

        },
        error: function (xhr, status, statusText) {
            alert("Failed to Get RPA Id ");
        }
    });
}

function getWorkInstruction(domainID, vendorID) {
    if ((domainID != null || domainID.length != 0) && (vendorID != null || vendorID.length != 0)) {
        $.isf.ajax({

            url: service_java_URL + "activityMaster/getActiveWorkInstruction?domainID=" + domainID + "&vendorID=" + vendorID,
            success: function (data) {
                data = data.responseData;
                arr_workinstruction = [];
                var html = "";
                $.each(data, function (i, d) {
                    if (d !== null)
                        arr_workinstruction.push({ value: d.hyperlink + '@' + d.workInstructionName, content: d.workInstructionName });
                    //  a.push(d.bOTName);

                })
            },
            complete: function () {
                // $('#rpaIds').find("option").eq(0).remove();
            },
            error: function (xhr, status, statusText) {
                alert("Failed to Get RPA Id ");
            }
        });
    }

    else {
        if (!domainID) {
            pwIsf.alert({ msg: "Domain ID missing", type: "error" });
        }
        else {
            pwIsf.alert({ msg: "Vendor ID missing", type: "error" });
        }
    }
}


var tools = [];
    //Populate Tool DropDown
    function getTools() {
        $.isf.ajax({
            type:'GET',
            url: service_java_URL + "/toolInventory/getToolInventoryDetails",
            success: function (result) {
                                  
                var option = '';
                for (var i = 0; i < result.length; i++) {
                    option += '<option value="' + result[i].toolID + '">' + result[i].tool + '</option>';
                    tools.push({ value: result[i].toolID + '@' + result[i].tool, content: '<span style="font-family: Alegreya Sans">' + result[i].tool + '</span>' });
                }
                $('#tool').append(option);
            }
        });
    }
    var tasks = [];
    //Populate task dropdown
    function getTasks(subActivityID) {
       
        $.isf.ajax({
            type:'GET',
            url: service_java_URL + "/activityMaster/getTaskDetails/" + subActivityID + "/" + signumGlobal,
            success: function (result) {
                                
                var option = '';
                for (var i = 0; i < result.length; i++) {
                    option += '<option value="' + result[i].taskID + '">' + result[i].task + '</option>';
                    tasks.push({ value: result[i].taskID + '@' + result[i].masterTask, content: '<span style="font-family: Alegreya Sans">' + result[i].masterTask + '</span>' });
                }
                $('#task').append(option);
            }
        });
    }

    //Reset Fields
    $(document).on('click', '.resetbtn', function () {

        $("#step").val("");
        $("#reason").val("");
    })
    
//confirmApproval
$(document).on('click', '.cnfrmbtn', function () {

        $.isf.ajax({
            type: "POST",
            url: service_java_URL + "woExecution/updateWFDefinition/" + woID + "/" + signumGlobal,
            dataType: 'json',
            data: JSON.stringify(app.graph.toJSON()),
            contentType: "application/json; charset=utf-8",
            //crossDomain:true,
            success: function (result) {
                if ('Success' in result) {
                    pwIsf.alert({ msg: "FlowChart Updated", type: 'info' });

                    let woIDDetails = JSON.parse(window.parent.$('#viewBtn_' + woID).attr('data-details'));
                   
                    if (woIDDetails.workOrderAutoSenseEnabled == true) {
                        let autoSenseInputData = new Object();
                        autoSenseInputData.WoId = woIDDetails.woid;
                        autoSenseInputData.flowchartDefID = woIDDetails.subActivityFlowChartDefID;
                        autoSenseInputData.stepID = "0";
                        autoSenseInputData.source = "UI";
                        autoSenseInputData.signumID = signumGlobal;
                        autoSenseInputData.overrideAction = "SUSPEND";
                        autoSenseInputData.action = "";
                        autoSenseInputData.taskID = "0";
                        autoSenseInputData.startRule = null;
                        autoSenseInputData.stopRule = null;
                        window.parent.NotifyClientOnWorkOrderSuspended(autoSenseInputData, true);
                    }

                    var parsedJSON = JSON.parse(result.Success.flowChartJSON);
                    app.graph.fromJSON(parsedJSON);
                    window.parent.$("#iframe_" + woID).attr("src", window.location.href.split("FlowChartEdit")[0] + "FlowChartExecution?mode=execute&version=" + result.Success.version + "&woID=" + woID + "&subActID=" + subActID + '&prjID=' + projid + '&flowchartType=custom' + '&proficiencyId=' + proficiencyId + '&status=' + woStatus + '&proficiencyLevel=' + proficiencyLevel);
                    window.parent.requestWorkorder();
                    window.parent.$('#togBtnForFlowChartViewMode_' + woID).prop('disabled', 'disabled'); //disabled toggal button of assessed/experienced

                    window.parent.$('.feedback_' + woID).hide();
                }
           
                else {
                    pwIsf.alert({ msg: result.Error, type: 'error' });
              
                }             

            },
            error: function (xhr, status, statusText) {
                pwIsf.alert({ msg: "Upload Fail", type: 'error' });
                // alert("Fail " + xhr.responseText);
                // alert("status " + xhr.status);
                // console.log('An error occurred');
            }
        });
    });

   //Add Step
    $(document).on('click', '.addbtn', function () {

        let stepName = $("#step").val();
        let taskName = $("#task  option:selected").text();
        let taskID = $("#task  option:selected").val();
        let toolName = $("#tool  option:selected").text();
        let toolID = $("#tool option:selected").val();
        let execType = $("#execType  option:selected").text();
        let responsible = $("#responsible option:selected").text();
        //var avgHrs = $("#avgHrs").val();
        //if (avgHrs == "") {
        //    avgHrs = "0"
        //}

        if (execType == "Decision") {
            taskName = null;
            taskID = 0;
            toolName = null;
            toolID = 0;

        }
        //if (taskName != null) {
        //    for (var i = 0; i < taskName.length; i++) {
        //        if (taskName.charAt(i) == "/") {
                   
        //            taskName = taskName.replace(taskName.charAt(i), "@");
        //        }
        //        if (stepName.charAt(i) == "/") {
                   
        //            stepName = stepName.replace(stepName.charAt(i), "@");
        //        }
        //        if (toolName.charAt(i) == "/") {
                   
        //            toolName = toolName.replace(taskName.charAt(i), "@");
        //        }
        //    }
        //}
        var reason = $("#reason").val();


        let sendData = {
            "stepType": execType,
            "stepName": stepName,
            "taskID": taskID,
            "taskName": taskName,
            "toolID": toolID,
            "toolName": toolName,
            "reason": reason,
            "responsible": responsible
        };


        
        var data = false;
        if (stepName !== "" && reason !== "" && responsible !== "") {
            $.isf.ajax({                
                //url: service_java_URL + "flowchartController/createJSONForStep?type=" + execType + "&&stepID=0&&stepName=" + stepName + "&&taskID=" + taskID + "&&taskName=" + taskName + "&&executionType=" + execType + "&&toolID=" + toolID + "&&toolName=" + toolName + "&&reason=" + reason + "&&responsible=" + responsible,
                type:'POST',
                url: service_java_URL + "flowchartController/createJSONForStep",
                dataType: 'json',
                data: JSON.stringify(sendData),
                contentType: "application/json; charset=utf-8",
                success: function (result) {
                    //console.log(result);
                    data = result;
                    $("#jsonval").val(data);
                    var currentjson = app.graph.toJSON();

                    var datatype = typeof data;
                    if (datatype == "string") {
                        data = JSON.parse(data);
                    }                   

                    //Save newly added element (so that user can delete this)
                    let getIdOfNewCell = data.cells[0].id;
                    Global_newAddedCells.push(getIdOfNewCell);

                    var jointJSON = $.merge(currentjson.cells, data.cells);
                    var finalJSON = { "cells": jointJSON };
                    app.graph.fromJSON(finalJSON);

                    let element = app.graph.getCell(getIdOfNewCell);
                    //autoresize new step.
                    autoResize(app, element);
                                        
                    $("#addEditStep").modal("toggle");

                }
            });

            return data;
        }
        else {
           // alert("Enter all details");
            pwIsf.alert({ msg: "Enter all details", type: 'error' });
        }

    });
