//$.ajaxSetup({
//    beforeSend: function (xhr) {

     
//      //  xhr.setRequestHeader("Authorization", "Bearer " + BearerKey);
//    }
//});
var isValidated = false; var isPasted = false; var nodesValidated = false;
var periodicity = 'Single'; var nodeNameClicked = 0;
var select1 = '';

function uploadWOFile() {
    var fileName = fl;
    var data = new FormData();
    data.append("file", fileName);
    params = projectID + '/' + signumGlobal;
    pwIsf.addLayer({ text: "Please wait ..." });
    $.isf.ajax({
        type: "POST",
        enctype: "multipart/form-data",
        url: service_java_URL_VM+"woManagement/uploadFileForWOCreation/" + params,
        data: data,
        processData: false,
        contentType: false,
        cache: false,
        timeout: 600000,
        success: function (data) {
            if (data.isValidationFailed == true) {
                pwIsf.alert({ msg: data.formErrors[0], type: 'error' });
            }
            else {
                pwIsf.alert({ msg: data.formMessages[0], type: 'success' });
            }
            pwIsf.removeLayer();
            $("#myFileWOUpload").val('');
            fileName = '';
            $('#btnUpload').attr("disabled", true);
        },
        error: function (jqXHR, textStatus, errorThrown) {
          //  alert("Please check your Template data. It should not be the blank or NA where Interger value is required");
            pwIsf.alert({ msg: jqXHR.responseJSON.errorMessage, type: 'error' });
            pwIsf.removeLayer();
            $("#myFile").val('')
       
        }
    }); // end ajax call
}


function downloadWOTemplate() {
var filePath;
$.isf.ajax({
    url: service_java_URL_VM + "flowchartController/downloadFile/WOCreationTemplate",
        success: function (data) {
            filePath = data;
    //filePath = "C:/NAM/2017/ISF/Network Element/NodeTypeTemplate.xlsx"
    window.location = UiRootDir + '/Project/Download?file=' +filePath;
    },
            error: function (xhr, status, statusText) {
        console.log("Fail " +xhr.responseText);
            //alert("status " + xhr.status);
        console.log('An error occurred');
        }
        });
        }

function getDomains() {
    $('#cbxDomain')
        .find('option')
        .remove();
    $("#cbxDomain").select2("val", "");
    $('#cbxDomain').append('<option value="0"></option>');
    var serviceAreaId= $('#cbxServiceArea').val();

    $.isf.ajax({
        url: service_java_URL + "activityMaster/getDomainDetails?ProjectID=" + projectID + "&ServiceAreaID=" + serviceAreaId,
        success: function (data) {
            var html = "";
            $.each(data, function (i, d) {
                aux = d.domain.split("/");                
                $('#cbxDomain').append('<option value="' + d.domainID + '">' + d.domain+ '</option>');
                $('#txtDomain').val(aux[0]);
            })
        },
        error: function (xhr, status, statusText) {
           // console.log("Fail " + xhr.responseText);
           //console.log("status " + xhr.status);
            console.log('An error occurred');
        }
    });
}

function getServiceAreas() {
    $('#cbxServiceArea')
        .find('option')
        .remove();
    $("#cbxServiceArea").select2("val", "");
    $('#cbxServiceArea').append('<option value="0"></option>');

    if (projectID == undefined || projectID == null || projectID == '' || isNaN(parseInt(projectID))) {
        console.log("Project id is wrong");
        pwIsf.alert({ msg: 'Wrong Project Id', type: 'error' });
    }
    else {
        $.isf.ajax({
            url: service_java_URL + "activityMaster/getServiceAreaDetails?ProjectID=" + parseInt(projectID),
            success: function (data) {
                var html = "";
                $.each(data, function (i, d) {
                    aux = d.serviceArea.split("/");
                    $('#cbxServiceArea').append('<option value="' + d.serviceAreaID + '">' + d.serviceArea + '</option>');
                    $('#txtServiceArea').val(aux[0]);
                })


            },
            error: function (xhr, status, statusText) {
                console.log("Fail " + xhr.responseText);

            }
        });
    }
 
}

function getTechnologiesWF() {
   
    $('#cbxTechnology')
        .find('option')
        .remove();
    $("#cbxTechnology").select2("val", "");
    $('#cbxTechnology').append('<option value="0"></option>');
    var domainID = $('#cbxDomain').val();

    $.isf.ajax({
        url: service_java_URL + "activityMaster/getTechnologyDetails?domainID=" + domainID + "&projectID=" + projectID,
        success: function (data) {
            var html = "";
            $.each(data, function (i, d) {
                $('#cbxTechnology').append('<option value="' + d.technologyID + '">' + d.technology + '</option>');
                $('#txtTechnology').val(d.technology);
            })
        },
        error: function (xhr, status, statusText) {
            //console.log("Fail " + xhr.responseText);
            //console.log("status " + xhr.status);
            console.log('An error occurred');
        }
    });
}

function searchSubActivity() {
    $('#cbxActivity')
        .find('option')
        .remove();
    $("#cbxActivity").select2("val", "");
    $('#cbxActivity').append('<option value="0"></option>');
    

    var domainID = $("#cbxDomain").val();
    var serviceAreaID = $("#cbxServiceArea").val();
    var technologyID = $("#cbxTechnology").val();
    
    $('#cbxSubActivity')
        .find('option')
        .remove();
    $("#cbxSubActivity").select2("val", "");
    $('#cbxSubActivity').append('<option value="0"></option>');  

    var domainNsubDomainName = $("#cbxDomain option:selected").text();
    var serviceAreaNsubServiceAreaName = $("#cbxServiceArea option:selected").text();
    var technologyName = $("#cbxTechnology option:selected").text();

    $.isf.ajax({
        url: service_java_URL + "activityMaster/getActivitySubActivityByProject_V2/" + domainID + "/" + serviceAreaID + "/" + technologyID + '/' + projectID +'/0',

        success: function (data) {
            var html = "";
            $.each(data, function (i, d) {
                var activity = d.Activity;
                var subActivity = d.subActivity;
                $('#cbxActivity').append('<option value="' + d.SubActivityID + '">' + activity + '</option>');
                
               
                $('#cbxSubActivity').append('<option value="' + d.SubActivityID + '">' + subActivity + '</option>');
             
            })

        },
        error: function (xhr, status, statusText) {
            //alert("Sorry there are not Activity/Sub-Activity for this domain, services area and technology");
            console.log('An error occurred');
        }
    });
}

function reset() {
    
    $('#cbxDomain')
        .find('option')
        .remove();
    $("#cbxDomain").select2("val", "");

    $('#cbxSubDomain')
        .find('option')
        .remove();
    $("#cbxSubDomain").select2("val", "");

    $('#cbxServiceArea')
        .find('option')
        .remove();
    $("#cbxServiceArea").select2("val", "");

    $('#cbxSubServiceArea')
        .find('option')
        .remove();
    $("#cbxSubServiceArea").select2("val", "");

    $('#cbxTechnology')
        .find('option')
        .remove();
    $("#cbxTechnology").select2("val", "");

    $('#cbxActivity')
        .find('option')
        .remove();
    $("#cbxActivity").select2("val", "");

    $('#cbxSubActivity')
        .find('option')
        .remove();
    $("#cbxSubActivity").select2("val", "");
    $('#cbxActivity').append('<option value="0"></option>');
    $('#cbxSubActivity').append('<option value="0"></option>');
    
    getServiceAreas();
    
}

function downloadFileWFTable(flowchartdefId) {
    $.isf.ajax({

        url:service_java_URL + '/test/generateNewExcelFromJson/' + flowchartdefId,
        error: function (xhr, status, statusText) {
            console.log('An error occurred on getProjectDetails: ' + xhr.error);
        }
    });

}

function initModal() {
    getCountries();
    getCustomers();
    getMarketAreas();
    getProjectDetailsWF();
}

function getProjectDetailsWF() {
    projectID = 4
    $('#cbxProjectName')
        .find('option')
        .remove();
    $("#cbxProjectName").select2("val", "");

    $('#cbxProject')
        .find('option')
        .remove();
    $("#cbxProject").select("val", "");
    
    $.isf.ajax({

        url: service_java_URL + "projectManagement/getProjectDetails?ProjectID=" + projectID,
        success: function (data) {

            $.each(data, function (i, d) {
                $('#cbxProject').append('<option value="' + projectID + '">' + projectID + '</option>');
                $('#cbxProjectName').append('<option value="' + projectID + '">' + d.projectName + '</option>');
            })


        },
        error: function (xhr, status, statusText) {
            console.log('An error occurred on getProjectDetails: ' + xhr.error);
        }
    })

}

function getCustomers() {
    $('#cbxCustomer')
        .find('option')
        .remove();
    $("#cbxCustomer").select("val", "");
    $('#cbxCustomer').append('<option value="0">0</option>');
    
    $.isf.ajax({
        //url: service_DotNET_URL + "Customers",
        url: service_java_URL + "projectManagement/getCustomers?countryID=",
        success: function (data) {

            $.each(data, function (i, d) {
                if (i < 50) {
                    $('#cbxCustomer').append('<option value="' + d.CustomerID + '">' + d.CustomerName + '</option>');
                }
            })
        },
        error: function (xhr, status, statusText) {
            console.log('An error occurred on getCustomers' + xhr.responseText);
        }
    });

}

function getMarketAreas() {
    $('#cbxMarket')
        .find('option')
        .remove();
    $("#cbxMarket").select("val", "");
    $('#cbxMarket').append('<option value="0">Select one</option>');
    $.isf.ajax({
        //url: service_DotNET_URL + "MarketAreas",
        url: service_java_URL + "projectManagement/getMarketAreas",
        success: function (data) {

            $.each(data, function (i, d) {
                $('#cbxMarket').append('<option value="' + d.MarketAreaID + '">' + d.MarketAreaName + '</option>');
            })

        },
        error: function (xhr, status, statusText) {
          //  console.log("Fail " + xhr.responseText);
           // console.log("status " + xhr.status);
            console.log('An error occurred');
        }
    });

}

function getCountries() {
    $('#cbxCountry')
        .find('option')
        .remove();
    $("#cbxCountry").select("val", "");
    $('#cbxCountry').append('<option value="0">Select one</option>');
    $.isf.ajax({
        url: service_java_URL + "activityMaster/getCountries",
        success: function (data) {

            $.each(data.responseData, function (i, d) {
                $('#cbxCountry').append('<option value="' + d.countryID + '">' + d.countryName + '</option>');
            })

        },
        error: function (xhr, status, statusText) {
            console.log('An error occurred');
        }
    });

}

function openCreateWorkFlow() {
    window.location.href = 'Creation';
}

function showTaskPopup(subActivityID) {
    $("#tBodyTask tr").remove();
    //alert(service_java_URL + "activityMaster/getTaskDetails?/" + subActivityID + "/" + signum);
    $.isf.ajax({
        url: service_java_URL + "activityMaster/getTaskDetails?/" + subActivityID + "/" + signum,
        success: function (data) {
            var html = "";
            $.each(data, function (i, d) {
                var tool = "";
                var toolID = 0
                if (d.tools.length != 0) {
                    tool = d.tools[0].tool;
                    tool = d.tools[0].toolID;
                }
                html = html + "<tr>";
                html = html + "<td>" + d.task + "</td>";
                html = html + "<td>" + d.executionType + "</td>";
                html = html + "<td>" + d.avgEstdEffort + "</td>";
                html = html + "<td>" + tool + "</td>";
                html = html + "</tr>";
            })
            $("#tBodyTask").append(html);
            
            $('#tableTask').DataTable({
                responsive: true
            });

        },
        error: function (xhr, status, statusText) {
            console.log('An error occurred');
        }
    });
}

function openModalTaskView() {
    var subActivityID = $("#txtSubActivity").val();
    $("#tBodyTask tr").remove();
    
    $.isf.ajax({
        url: service_java_URL + "activityMaster/getTaskDetails?/" + subActivityID + "/" + signum,
        success: function (data) {
            var html = "";
            $.each(data, function (i, d) {
                var tool = "";
                var toolID = 0
                if (d.tools.length != 0) {
                    tool = d.tools[0].tool;
                    tool = d.tools[0].toolID;
                }
                html = html + "<tr>";
                html = html + "<td>" + d.task + "</td>";
                html = html + "<td>" + d.executionType + "</td>";
                html = html + "<td>" + d.avgEstdEffort + "</td>";
                html = html + "<td>" + tool + "</td>";
                html = html + "</tr>";
            })
            $("#tBodyTask").append(html);
           
            $('#tableTask').DataTable({
                responsive: true
            });

        },
        error: function (xhr, status, statusText) {
            console.log('An error occurred');
        }
    });
}

function addScope() {
    var date = new Date();
    var dat = date.getFullYear() + "-" + (date.getMonth() + 1) + "-" + date.getDate();

    var subActivityID = $("#txtSubActivity").val();
    var flowChartJSON = "{}";
    var sourceProjectID = $("#cbxProject").val();
    var createdBy = signumGlobal;
    var createdDate = dat;
    var active = "1";
    var versionNumber = "1";

    var service_data = "{\"projectID\":\"" + projectID + "\",\"subActivityID\":\"" + subActivityID + "\",\"flowChartJSON\":\"" + flowChartJSON + "\",\"sourceProjectID\":\"" + sourceProjectID + "\",\"createdBy\":\"" + createdBy + "\",\"createdDate\":\"" + createdDate + "\",\"active\":\"" + active + "\",\"versionNumber\":\"" + versionNumber + "\"}";
    
    $.isf.ajax({
        url: service_java_URL + "flowchartController?/addWorkFlowToProject???/",
        context: this,
        crossdomain: true,
        processData: true,
        contentType: 'application/json',
        type: 'POST',
        data: service_data,
        xhrFields: {
            withCredentials: false
        },
        success: AjaxSucceeded,
        error: AjaxFailed
    });
    function AjaxSucceeded(data, textStatus) {
        pwIsf.alert({ msg: "Deliverable Added.", type: 'info' });
    }
    function AjaxFailed(result) {
        pwIsf.alert({ msg: "Failed to save the Deliverable.", type: 'info' });
    }
}

function searchProjects() {
    $("#project_tBody tr").remove();
    var projectID = $("#cbxProject").val();
    var subActivityID = $("#txtSubActivity").val();
    var customerID = $("#cbxCustomer").val();
    var countryID = $("#cbxCountry").val();
    var marketAreaID = $("#cbxMarket").val();
    pwIsf.addLayer({ text: "Please wait..." });
    $.isf.ajax({
        url: service_java_URL + "flowchartController/getDetailsForImportExistingWF?/" + projectID + "/" + subActivityID + "/" + customerID + "/" + countryID + "/" + marketAreaID,
        success: function (data) {
            var html = "";
            $.each(data, function (i, d) {
                html = html + "<tr>";
                html = html + "<td></td>";
                html = html + "<td></td>";
                html = html + "<td></td>";
                html = html + "<td></td>";
                html = html + "<td></td>";
                html = html + "<td></td>";
                html = html + "</tr>";
            })
            $("#project_tBody").append(html);
           
            $('#table_project').DataTable({
                responsive: true
            });
            pwIsf.removeLayer();
        },
        error: function (xhr, status, statusText) {
            console.log('An error occurred');
            pwIsf.removeLayer();
        }
    });
}

function openModalStepDetail() {
    $('#StepDetail').modal('show');
    var projectID = projectID;
    var subActivityID = $("#cbxSubActivity").val();
    var scopeID = scopeID;
    
    $.isf.ajax({
        url: service_java_URL + "flowchartController/getStepTaskDetails?/" + scopeID + "/" + subActivityID + "/" + projectID,
        
        success: function (data) {
            var aux = data["lstScopeTaskMapping"][0];
            
            $("#TaskNameText").val(aux.task);
            $("#TaskNameTextE").val(aux.task);

            $("#ExecutionTypeText").val(aux.executionType);
            $("#ExecutionTypeE").val(aux.executionType);

            $("#EstdeffortText").val(aux.avgEstdEffort);
            $("#EstdeffortE").val(aux.avgEstdEffort);

            $("#ToolText").val(aux.tool);
            $("#ToolE").val(aux.tool);

            $("#_projectID").val(aux.projectID);
            $("#_scopeID").val(aux.scopeID);
            $("#_subactivityID").val(aux.subActivityID);
            $("#_taskID").val(aux.taskID);
            $("#_toolID").val(aux.toolID);
        },
        error: function (xhr, status, statusText) {
            console.log('An error occurred');
        }
    });
}

function saveStepDetails() {
    var projectID = $("#_projectID").val();
    var scopeID = $("#_scopeID").val();
    var subActivityID = $("#_subactivityID").val();
    var taskID = $("#_taskID").val();
    var task = $("#TaskNameTextE").val();
    var executionType = $("#ExecutionTypeE").val();
    var avgEstdEffort = $("#EstdeffortE").val();
    var toolID = $("#_toolID").val();
    var toolName = $("#ToolE").val();
    var service_data = "{\"projectID\":\"" + projectID + "\",\"scopeID\":\"" + scopeID + "\",\"subActivityID\":\"" + subActivityID + "\",\"taskID\":\"" + taskID + "\",\"task\":\"" + task + "\",\"executionType\":\"" + executionType + "\",\"avgEstdEffort\":\"" + avgEstdEffort + "\",\"toolID\":\"" + toolID + "\",\"toolName\":\"" + toolName + "\"}";
    $.isf.ajax({
        url: service_java_URL + "flowchartController?/updateProjectDefinedTask???/",
        context: this,
        crossdomain: true,
        processData: true,
        contentType: 'application/json',
        type: 'POST',
        data: service_data,
        xhrFields: {
            withCredentials: false
        },
        success: AjaxSucceeded,
        error: AjaxFailed
    });
    function AjaxSucceeded(data, textStatus) {
        pwIsf.alert({ msg: "Task Saved!", type: 'info' });
        $('#StepDetail').modal('hide');
    }
    function AjaxFailed(result) {
        pwIsf.alert({ msg: "Failed to save the task", type: 'error' });
    }
}

list = [];


function onRejectWorkFlow(projID, SubActID, flowChartDefID, employeeSignum, versionNumber, reasonToReject, wfid) {
    for (var i = 0; i < reasonToReject.length; i++) {
        if (reasonToReject.charAt(i) == "/") {
            reasonToReject = reasonToReject.replace(reasonToReject.charAt(i), "@");
     }
    }
    $.isf.ajax({
        url: service_java_URL + "flowchartController/rejectWorkFlow/?projectID=" + projID + '&subActivityID=' + SubActID + '&flowchartDefID=' + flowChartDefID + '&wfVersion=' + versionNumber + '&managerSignumID=' + signumGlobal + '&employeeSignumID=' + employeeSignum + '&reason=' + reasonToReject,
        success: function (data) {
            refreshTables();
            pwIsf.alert({ msg: "Work Flow Rejected!.", type: 'info' });

        },
        error: function (xhr, status, statusText) {
            pwIsf.alert({ msg: "Error: Not Rejected.", type: 'error' });
        }
    });
}

function createNew(projID, SubActID, flowChartDefID, employeeSignum, wfVersion, wfName, isCreateNew, newWFName, wfid) {
    let wfEditReason = '';
    $.isf.ajax({
        url: service_java_URL + 'flowchartController/approveWorkFlow/?projectID=' + projID + '&subActivityID=' + SubActID + '&flowchartDefID=' + flowChartDefID + '&wfName=' + wfName + '&wfVersion=' + wfVersion + '&managerSignumID=' + signumGlobal + '&employeeSignumID=' + employeeSignum + '&isCreateNew=' + isCreateNew + '&newWFName=' + newWFName + '&wfid=' + wfid + '&wfEditReason=' + wfEditReason,
        crossdomain: true,
        processData: true,
        contentType: 'application/json',
        type: 'POST',
        data: JSON.parse(localStorage.getItem('json')),
        success: function (data) {
                responseHandler(data);
                refreshTables();
        },
        error: function (xhr, status, statusText) {
            pwIsf.alert({ msg: "Error:Not Accepted", type: 'error' });
        }
    });
}

function updateExisting(projID, SubActID, flowChartDefID, employeeSignum, wfVersion, wfName, isCreateNew, wfid, wfEditReason) {
    if (wfEditReason == 0) {
        pwIsf.alert({ msg: "Please select any reason", type: "warning" });
    }
    else{
        $.isf.ajax({
            url: service_java_URL + 'flowchartController/approveWorkFlow/?projectID=' + projID + '&subActivityID=' + SubActID + '&flowchartDefID=' + flowChartDefID + '&wfName=' + wfName + '&wfVersion=' + wfVersion + '&managerSignumID=' + signumGlobal + '&employeeSignumID=' + employeeSignum + '&isCreateNew=' + isCreateNew + '&newWFName=' + wfName + '&wfid=' + wfid + '&wfEditReason=' + wfEditReason,
            crossdomain: true,
            processData: true,
            contentType: 'application/json',
            type: 'POST',
            data: JSON.parse(localStorage.getItem('json')),
            success: function (data) {
                responseHandler(data);
                refreshTables();
            },
            error: function (xhr, status, statusText) {
                pwIsf.alert({ msg: "Error: Not Accepted", type: 'error' });
            }
        });
    }
}

function refreshTables() {
    getWFApprovals();
    searchWfAvail();
}

//Check if rule is present in flowchart
function isRulePresent(flowChartJSON) {
    let allCells = JSON.parse(flowChartJSON).cells;
    let ruleCells = allCells.filter(function (cell) { return (cell.startRule == true || cell.stopRule == true) });

    if (ruleCells.length != 0) {
        return true;
    }
    else {
        return false;
    }
}

function getWFApprovals() {

    if ($.fn.dataTable.isDataTable('#table_wf_creation_approval')) {
        oWFApprovalTable.destroy();
        $('#table_wf_creation_approval').empty();

    }
    pwIsf.addLayer({ text: "Please wait ..." });
    
    $.isf.ajax({
        url: service_java_URL + "flowchartController/getWorkFlowForApproval/" + projectID,
        success: function (data) {
            pwIsf.removeLayer();
            $("#loaderDivWFA").hide();
            //console.log(data);

            $.each(data, function (i, d) {

                let hasRule = isRulePresent(d.flowChartJSON);

                d.act = "<a href='javascript:void(0)' class='view-WF' data-subactid='" + d.subActivityID + "' data-projid= '" + d.projectID + "' data-versionno= '" + d.versionNo[d.versionNo.length-1] + "'>" +
                  "<span class='fa fa-eye' data-toggle='tooltip' title='Work Flow View' style='color:blue'></span></a>&nbsp;&nbsp;" +
                    "<a href='javascript:void(0)' class='reject-WF' data-wfid='" + d.wfid + "' data-subactid='" + d.subActivityID + "' data-projid= '" + d.projectID + "' data-flowchartdefid= '" + d.flowchartDefID + "' data-employeesignum= '" + d.modifiedBy + "' data-versionno= '" +d.versionNo + "'>"+
                  "<span class='fa fa-times' data-toggle='tooltip' title='Reject' style='color: red'></span></a >&nbsp;&nbsp;" +
                 "<input type='hidden' id='flowchart' value='"+ JSON.stringify(d.flowChartJSON).replace(/'/g, '&apos;') + "'>";
                //check = [{ key: d.flowchartDefID, value: d.flowChartJSON }];
                d.actWrkCrt = "<a href='javascript:void(0)' class='accept-WF' data-wfid='" + d.wfid + "' data-hasrule=" + hasRule + " data-subactid='" + d.subActivityID + "' data-projid= '" + d.projectID + "' data-flowchartdefid= '" + d.flowchartDefID + "' data-employeesignum= '" + d.modifiedBy + "' data-wfname= '" + d.workFlowName + "' data-wfversion= '" + d.versionNo[d.versionNo.length - 1] + "'>" +
                    "<span class='fa fa-check' data-toggle='tooltip' title='Accept' style='color: green'></span></a >&nbsp;&nbsp; ";
               
              
                d.action = d.act + d.actWrkCrt;
            })

            $("#table_wf_creation_approval").append($('<tfoot><tr><th></th><th></th><th>Deliverable Name</th><th>Domain</th><th>Sub Domain</th><th>Service Area</th><th>Technology</th><th>Activity</th><th>Sub Activity</th><th>Modified By</th><th>Modified On</th><th>Type</th></tr></tfoot>'));

            oWFApprovalTable = $('#table_wf_creation_approval').DataTable({
                searching: true,
                responsive: true,
                "pageLength": 10,
                "data": data,
                colReorder: true,
                order: [1],
                dom: 'Bfrtip',
                buttons: [
                 'colvis','excel'
                ],
                "destroy": true,
                "columns": [
                    { "title": "Action", "targets": 'no-sort', "orderable": false, "searchable": false, "data": "action"},
                    { "title": "WorkFlow Name", "targets": 'no-sort', "orderable": false, "searchable": false, "data": "workFlowName" },
                    { "title": "Deliverable Name", "data": "scopeName" }, { "title": "Domain", "data": "domain" },
                    { "title": "Sub Domain", "data": "subDomain" }, { "title": "Service Area", "data": "serviceArea" },
                    { "title": "Technology", "data": "technology" }, { "title": "Activty", "data": "activity" },
                    { "title": "Sub Activity", "data": "subActivity" }, { "title": "Modified By", "data": "modifiedBy" },
                    { "title": "Modified On", "data": "modifiedOn" }, { "title": "Type", "data": "type" }
                     
                ],
                initComplete: function () {

                    $('#table_wf_creation_approval tfoot th').each(function (i) {
                        var title = $('#table_wf_creation_approval thead th').eq($(this).index()).text();
                        if ((title != "Action") && (title != "WorkFlow Name"))
                            $(this).html('<input type="text" class="form-control" style="font-size:12px;"  placeholder="Search ' + title + '" data-index="' + i + '" />');
                    });
                    var api = this.api();
                    api.columns().every(function () {
                        var that = this;
                        $('input', this.footer()).on('keyup change', function () {
                            if (that.search() !== this.value) {
                                that
                                  .columns($(this).parent().index() + ':visible')
                                  .search(this.value)
                                  .draw();
                            }
                        });
                    });

                }
                
            });
            
            $('#table_wf_creation_approval tfoot').insertAfter($('#table_wf_creation_approval thead'));
            //console.log(data);
            
        },
        error: function (xhr, status, statusText) {
            pwIsf.removeLayer();
            console.log("failed to fetch data");
        }
       
    });
}


function onProjectDeleteWF(thisElement) {

    var that = $(thisElement);
    var data_str = that.attr("data-btndetails");
    var my_object = JSON.parse(decodeURIComponent(data_str));
    my_object.loggedInUser = signumGlobal;

    var JsonObj = JSON.stringify(my_object);
    

    pwIsf.addLayer({ text: 'Please wait ...' });
    $.isf.ajax({
        url: service_java_URL + "projectManagement/deleteProjectComponents",
        context: this,
        crossdomain: true,
        processData: true,
        contentType: 'application/json',
        type: 'POST',
        data: JsonObj,//service_data,
        xhrFields: {
            withCredentials: false
        },
        success: function (data) {

            if (data.isDeleted) {

                my_object.type = 'DELETE_WORKFLOW';
                var msgVar = '';
                if (data.count_woid < 1) {
                    msgVar = 'The deletion of WF will be applicable to all associated Deliverables. Do you still want to delete this work flow? ';
                } else {
                    msgVar = 'The deletion of WF will be applicable to all associated Deliverables. ' + data.msg;
                }
                
                var JsonObj = JSON.stringify(my_object);

                pwIsf.confirm({
                    title: 'Delete work flow', msg: msgVar,
                    'buttons': {
                        'Yes': {
                            'action': function () {
                                pwIsf.addLayer({ text: 'Please wait ...' });
                                $.isf.ajax({
                                    url: service_java_URL + "projectManagement/deleteProjectComponents",
                                    context: this,
                                    crossdomain: true,
                                    processData: true,
                                    contentType: 'application/json',
                                    type: 'POST',
                                    data: JsonObj,//service_data,
                                    xhrFields: {
                                        withCredentials: false
                                    },
                                    success: function (data) {

                                        if (data.isDeleted) {
                                            that.closest('tr').hide('slow');
                                            pwIsf.alert({ msg: "Successfully Deleted.", autoClose: 3 });
                                        } else {
                                            pwIsf.alert({ msg: data.msg, type: 'warning' });
                                        }

                                    },
                                    error: function (xhr, status, statusText) {

                                    },
                                    complete: function (xhr, statusText) {
                                        pwIsf.removeLayer();
                                    }
                                });
                            }
                        },
                        'No': { 'action': function () { } },

                    }
                });

                
            } else {
                pwIsf.alert({ msg: data.msg, type: 'warning' });
            }

        },
        error: function (xhr, status, statusText) {

        },
        complete: function (xhr, statusText) {
            pwIsf.removeLayer();
        }
    });







    //=========================
    

}


function searchWfAvail() {   

    var k = 0;
   
    var domain = $("#cbxDomain").val() == null ? 0 : document.getElementById('cbxDomain').options[document.getElementById('cbxDomain').selectedIndex].text;
    var serviceArea = $("#cbxServiceArea").val() == null ? 0 : document.getElementById('cbxServiceArea').options[document.getElementById('cbxServiceArea').selectedIndex].text;
    var technology = $("#cbxTechnology").val() == null ? 0 : document.getElementById('cbxTechnology').options[document.getElementById('cbxTechnology').selectedIndex].text;
    var activity = $("#cbxActivity").val() == null ? 0 : document.getElementById('cbxActivity').options[document.getElementById('cbxActivity').selectedIndex].text;
    var subActivity = $("#cbxSubActivity").val() == null ? 0 : document.getElementById('cbxSubActivity').options[document.getElementById('cbxSubActivity').selectedIndex].text;
    
    if (domain != 0) {
        var domainSplit = domain.split("/");
        domain = domainSplit[0];
        
    }
    else
    domain = domain;
    
    var subDomain = $("#cbxSubDomain").val();
   
    var subServiceArea = $("#cbxSubServiceArea").val();
   
    var activity = "";
    
    if (domain != "0" && domain != "" && domain!=null) {
        domain = "'" + domain + "'";
    } else {
        domain = "all";
    }
    if (subDomain != "0" && subDomain != "" && subDomain != null) {
        subDomain = "'" + subDomain + "'";
    } else {
        subDomain = "all";
    }
    if (serviceArea != "0" && serviceArea != "" && serviceArea != null) {
        serviceArea = "'" + serviceArea + "'";
    } else {
        serviceArea = "all";
    }
    if (subServiceArea != "0" && subServiceArea != "" && subServiceArea != null) {
        subServiceArea = "'" + subServiceArea + "'";
    } else {
        subServiceArea = "all";
    }
    if (technology != "0" && technology != "" && technology != null) {
        technology = "'" + technology + "'";
    } else {
        technology = "all";
    }
    if (activity != "0" && activity != "" && activity != null) {
        activity = "'" + activity + "'";
    } else {
        activity = "all";
    }
    if (subActivity != "0" && subActivity != "" && subActivity != null) {
        subActivity = "'" + subActivity + "'";
    } else {
        subActivity = "all";
    }
    if ($.fn.dataTable.isDataTable('#wfSearchTable')) {
        oTable.destroy();
        $('#wfSearchTable').empty();
    }
    $('#wfSearchTable').html("");
    pwIsf.addLayer({ text: "Please wait ..." });
    $.isf.ajax({
        url: service_java_URL + "flowchartController/searchWFAvailabilityforScope/" + projectID + "/" + domain + "/" + subDomain + "/" + serviceArea + "/" + subServiceArea + "/" + technology + "/" + activity + "/" + subActivity,
        success: function (data) {
            //console.log(data);
            pwIsf.removeLayer();
            $.each(data, function (i, d) {
                //console.log(d);
                d.serviceAreaSubServiceArea = d.serviceArea + '/' + d.subServiceArea;
              //  d.version = '<select id="versiondropdown' + i + '" class="select2able" style="display:none;"><option value="-1"> Please Select</option>';
                if (d.wfAvailability === 'No') {
                    d.act = '<a href="#" class="icon-add lsp addWorkFlow" title="Add work flow" data-slahours="' + d.slaHours + '" data-ftr="' + d.ftr + '" data-domainid="' + d.domainID + '" data-techid="' + d.technologyID + '" data-neneededforexecution="' + d.neNeededForExecution + '" data-experiencedmode="' + d.experiencedMode +  '" data-wfownername= "' + d.wfOwnerName + '" data-subActId="' + d.subActivityID + '" data-projID="' + d.projectID + '" data-versionNo="' + d.lstVersionNumber + '" data-workFlowName="' + d.lstWFName + '">' + getIcon('add', { textWith: 'WF' }) + '</a>';
                    d.actWrkCrt = "";
                }
                else {

                    d.act = '<a href="#" class="createWF icon-view" title="View work flow" data-subActId="' + d.subActivityID + '" data-wfid="' + d.wfid + '" data-projID="' + d.projectID + '" data-versionNo="' + d.lstVersionNumber + '" data-workFlowName="' + d.lstWFName + '" data-domain="' + d.domain + '" data-subdomain="' + d.subDomain + '" data-subServiceArea="' + d.subServiceArea + '" data-tech="' + d.technology + '" data-activity="' + d.activity + '" data-subActivity="' + d.subActivity + '" data-scopeName="' + d.scopeName + '" data-scopeID="' + d.scopeID + '" data-subActivityID="' + d.subActivityID + '"  data-experiencedMode="' + d.experiencedMode + '" data-wfownname="' + d.wfOwnerName +'" > ' + getIcon('view', { textWith: 'WF' }) + '</a > ' +
                        '<a href="#" class="icon-add lsp addWorkFlow" title="Add work flow" data-slahours="' + d.slaHours + '" data-ftr="' + d.ftr + '" data-domainid="' + d.domainID + '" data-techid="' + d.technologyID + '" data-neneededforexecution="' + d.neNeededForExecution + '" data-experiencedmode="' + d.experiencedMode + '" data-wfownername= "' + d.wfOwnerName + '" data-subActId="' + d.subActivityID + '" data-projID="' + d.projectID + '" data-versionNo="' + d.lstVersionNumber + '" data-workFlowName="' + d.lstWFName + '">' + getIcon('add', { textWith: 'WF' }) + '</a>' + 
                        '<a href="#" class="icon-edit lsp addWorkFlow editWF" title="Edit work flow" data-slahours="' + d.slaHours + '" data-ftr="' + d.ftr + '" data-wfid="' + d.wfid + '" data-domainid="' + d.domainID + '" data-techid="' + d.technologyID + '" data-neneededforexecution="' + d.neNeededForExecution + '" data-experiencedmode="' + d.experiencedMode + '" data-wfownername= "' + d.wfOwnerName + '" data-subActId="' + d.subActivityID + '" data-projID="' + d.projectID + '" data-versionNo="' + d.lstVersionNumber +  '" data-workFlowName="' + d.lstWFName + '">' + getIcon('edit', { textWith: 'EWF' }) + '</a>';
                   // d.actWrkCrt = '<a href="#" class="view-createWO icon-add lsp" title="Create work order" data-domain="' + d.domain + '" data-subdomain="' + d.subDomain + '" data-subServiceArea="' + d.subServiceArea + '" data-tech="' + d.technology + '" data-activity="' + d.activity + '" data-subActivity="' + d.subActivity + '" data-scopeName="' + d.scopeName + '" data-scopeID="' + d.scopeID + '" data-subActivityID="' + d.subActivityID + '" data-index="' + i + '" data-versionNo="' + d.lstVersionNumber + '" data-flowchartdefid="'+d.subActivityFlowChartDefID+'" data-workFlowName="' + d.lstWFName + '"> '+getIcon('add')+'</a >';
                }
                
                var delBtnDetails = JSON.stringify({ subActivityFlowChartDefID: d.subActivityFlowChartDefID, type: 'WORKFLOW', projectID: d.projectID, scopeID: d.scopeID, subActivityID: d.subActivityID });
                d.downloadFlow = '';
                d.actaAll = '<a href="#" class="view-taskProject icon-view lsp" title="View task" data-projID="' + d.projectID + '" data-subActId="' + d.subActivityID + '"' + '">' + getIcon('view') + '</a>';
                d.deleteBtn = '';
                if (d.subActivityFlowChartDefID) {
                    d.downloadFlow = '&nbsp;&nbsp; <a data-toggle="tooltip" class="infoTitle" title="Flow Chart Data (Kindly remove the columns ToolID, TaskName, TaskID and StepId before uploading the file!)" type="button" onclick="downloadWFTableFile({ thisObj: this, flowchartdefId:\'' + d.subActivityFlowChartDefID+'\' })" download><i class="fa fa-download glyphicon-ring glyphicon-green"></i></a></i>';
                    //d.downloadFlow = '&emsp; <a class = "icon-download" style = "font-size: 9px; padding - right: 3px;"  data-toggle="tooltip" title="Excel File"  type = "button" href="https://isfdevservices.internal.ericsson.com:8443/isf-rest-server-java_dev_major/flowchartController/downloadFlowChartData?flowChartDefID=' + d.subActivityFlowChartDefID + '" download>' + getIcon('download') +'</i></a>';
                    d.deleteBtn = '<a href="#" class="icon-delete lsp cssOnProjectDeleteWF" data-btndetails=\'' + delBtnDetails + '\' title="Delete work flow">' + getIcon('delete') + '</a>';
                }

               // d.action = '<div style="display:flex;">' + d.act + d.actaAll + d.actWrkCrt + d.deleteBtn + '</div>';
                d.action = '<div style="display:flex;">' + d.act + d.actaAll + d.deleteBtn + d.downloadFlow + '</div>';
            })
            
            $("#wfSearchTable").append($('<tfoot><tr><th>Action</th><th>Service Area/Sub service Area</th><th>Domain/SubDomain</th><th>Activity/SubActivity</th><th>SubActivityID</th><th>Technology</th><th>Work Flow Name</th><th>WorkFlow Owner</th><th>Network Elements Mandatory</th><th>SLA Hours</th><th>FTR</th><th>IsMutiMode</th></th><th>Work Flow Availability</th></tr></tfoot>'));

            oTable = $('#wfSearchTable').DataTable({
                 searching: true,
                 responsive: true,
                 "pageLength": 10,
                 "data": data,
                 colReorder: true,
                 order: [1],
                 dom: 'Bfrtip',
                 buttons: [
                  'colvis','excel'
                 ],
                 "destroy": true,
                 "columns": [
                     { "title": "Action", "targets": 'no-sort', "orderable": false, "searchable": false, "data": "action"},                  
                     { "title": "Service Area/Sub service Area", "data": "serviceAreaSubServiceArea"},
                     {
                         "title": "Domain/SubDomain",
                         "data":null,
                         "render": function (data, type, row) {
                                   return data.domain+ "/"+ data.subDomain;
                           }
                     },
                     {
                         "title": "Activity/SubActivity",
                         "data": null,
                         "render": function (data, type, row) {
                             return data.activity + "/" + data.subActivity;
                         }
                     },
                     {
                         "title": "SubActivityID",
                         "data": null,
                         "render": function (data, type, row) {
                             return data.subActivityID;
                         }
                     },
                     { "title": "Technology", "data": "technology" },   { "title": "Work Flow Name", "data": "lstWFName" },
                     { "title": "WorkFlow Owner", "data": "wfOwnerName" }, { "title": "Network Element Mandatory", "data": "neNeededForExecution" },
                     { "title": "SLA Hours", "data": "slaHours" }, {
                         "title": `FTR <i class="fa fa-info-circle" style="font-size: 24px; color: blue" title="
                                FTR 1- Count of Closed WO whose ${shortNeNameAsNEID} repeated in last 45 Days for same SubActivity
                                FTR 2- Percentage of the Difference between the Sum of Closed WO and the Sum of the Rejected WO divided by the Sum of the Closed WO
                                FTR 3 - Count of Closed and Rejected WO With Failure Delivery Status"></i>`, "data": "ftr" },
                     { "title": "IsMultiMode", "data": "experiencedMode" },
                    
                     { "title": "Work Flow Availability", "data": "wfAvailability" },
                 ],

                 initComplete: function () {

                     $('#wfSearchTable tfoot th').each(function (i) {
                         var title = $('#wfSearchTable thead th').eq($(this).index()).text();
                         if ((title != "Action"))
                         $(this).html('<input type="text" class="form-control" style="font-size:12px;"  placeholder="Search ' + title + '" data-index="' + i + '" />');
                     });
                     var api = this.api();
                     api.columns().every(function () {
                         var that = this;
                         $('input', this.footer()).on('keyup change', function () {
                             if (that.search() !== this.value) {
                                 that
                                   .columns($(this).parent().index() + ':visible')
                                   .search(this.value)
                                   .draw();
                             }
                         });
                     });
                     //$('#wfSearchTable thead th[FTR]').tooltip(
                     //    {
                     //        content: 'FTR'
                     //    });        
                 }
                
            });
            
            $('#wfSearchTable tfoot').insertAfter($('#wfSearchTable thead'));
                      
        },
        error: function (xhr, status, statusText) {
            pwIsf.removeLayer();
            console.log("failed to fetch data");
        }
    });
 var data = oTable.rows().data();
    data.each(function (value, index) {
       // console.log('Data in index: ' + index + ' is: ' + value);
    });
}

function workOrderCreateModal(domain, subDomain, subServiceArea, tech, act, subAct, scope, scopeID, subActId, version, versionName, flowchartdefid, slaHrs) {
    if (version == -1) {
        pwIsf.alert({ msg: "Please select the workflow name", type: 'error' });
    }      
    else {
        $('#versionNumber').val(version);
        // $('#domainSp').append(domain + '/' + subDomain);
        // $('#subServSp').append(subServiceArea);
        // $('#techSp').append(tech);
        $('#WfName').append(versionName);
        $('#ActSubActSp').append(act + '/' + subAct);
        // $('#scopeSp').append(scope);
        $('#scopeID').val(scopeID);
        $('#SubActivityID').val(subActId);
        $('#techSp').val(tech);
        $('#domainSp').val(domain + '/' + subDomain);
        $('#scopeSp').val(scope);
        $('#subServSp').val(subServiceArea);
        $('#flowchartdefid').val(flowchartdefid);
      //  getPriority();
        //getWFUserByProjectId();
        $(".mandatory_Box").slideDown();
        $(".node_box").css("display", "none");
        $("#slideUp").css("display", "inline");
        $("#slideDwn").css("display", "none");

		$("#slaHrsDiv").css("display", "block");
		$("#planDtArea").css("display", "block");
       
        $(".recurrenceOrder").removeClass("disabledbutton");
        //$("#slaHrsDiv").removeClass("disabledbutton");
        //$("#ep_box").css("display", "none");
        

        nodeNameClicked = 0;
       // $('#modal_wo_creation').modal('show');
    }
}

function getTaskBysubActivity(projId, subId) {
    if ($.fn.dataTable.isDataTable('#view_Task')) {
        oTable.destroy();
        $('#view_Task_tBody').empty();
    }
    subId = parseInt(subId);
    projId = parseInt(projId)
    var tools = "";
    var toolName = "";
    pwIsf.addLayer({ text: "Please wait ..." });
    
    $.isf.ajax({
        url: service_java_URL + "activityMaster/viewTaskDetails/" + projId + '/' + subId,
        success: function (data) {
            pwIsf.removeLayer();
            $('#view_Task_tBody').html('');
            for (var i = 0; i < data.length; i++) {
                
                var tr = '<tr>'
                tr += '<td>' + data[i].masterTask + '</td>';
                if (data[i].executionType == null)
                    tr += '<td> -- </td>';
                else
                    tr += '<td>' + data[i].executionType + '</td>';
                if (data[i].description == null)
                    tr += '<td> -- </td>';
                else
                tr += '<td>' + data[i].description + '</td>';
                tr += '<td>' + data[i].rpaID + '</td>';
                if (data[i].tools.length == 0)
                    toolName = 'NA';
                else{
                    for (var j = 0; j < data[i].tools.length; j++)
                        tools = data[i].tools[j].tool + ',' + tools;
                        toolName = tools.replace(/,\s*$/, "");
                                      
                }
                tr += '<td>' + toolName+ '</td>'; 
                tr += '<td>' + data[i].avgEstdEffort + '</td>';

                tr += '</tr>';
                toolName = "";
                tools = "";
                $('#view_Task').append($(tr));
               
                
            }
           
            oTable = $("#view_Task").DataTable({
                responsive: true,
                searching:true,
                destroy: true,
                colReorder: true,
                dom: 'Bfrtip',
                buttons: [
                'excel'
                ],
                initComplete: function () {

                    $('#view_Task tfoot th').each(function (i) {
                        var title = $('#view_Task thead th').eq($(this).index()).text();
                        $(this).html('<input type="text" class="form-control" style="font-size:12px;"  placeholder="Search ' + title + '" data-index="' + i + '" />');
                    });
                    var api = this.api();
                    api.columns().every(function () {
                        var that = this;
                        $('input', this.footer()).on('keyup change', function () {
                            if (that.search() !== this.value) {
                                that
                                  .columns($(this).parent().index() + ':visible')
                                  .search(this.value)
                                  .draw();
                            }
                        });
                    });

                },
                "displayLength": 25
            });
            $('#view_Task tfoot').insertAfter($('#view_Task thead'));
        },
        error: function (xhr, status, statusText) {
            pwIsf.removeLayer();
            console.log("failed to fetch data");
        }
    });
}

$(document).on("click", ".view-createWO", function () {
    var version = $(this).data('versionno');
var index = $(this).data('index');
    var versionName = $(this).data('workflowname');
console.log('version ' + versionName + ' ' + version);
 var domain = $(this).data('domain');
 var subDomain = $(this).data('subdomain');
 var subServiceArea = $(this).data('subservicearea');
 var tech = $(this).data('tech');
 var activity = $(this).data('activity');
 var subActivity = $(this).data('subactivity');
 var subActivityID = $(this).data('subactivityid');
 var scopeName = $(this).data('scopename');
 var scopeID = $(this).data('scopeid');
 var flowchartdefid = $(this).data('flowchartdefid');

 getExecutionPlans(subActivityID, version);
 workOrderCreateModal(domain, subDomain, subServiceArea, tech, activity, subActivity, scopeName, scopeID, subActivityID, version, versionName, flowchartdefid);
    //$(".modal-body #woId").val(woId);
 //getVersionName(subActivityID);
   
});

function getWorkFlows() {

    searchWfAvail();
    getWFApprovals();
    //getExecutionPlans();
    $("#loaderDivWFA").show();
  
}

function getVersionName(subActivityID)
{
    $('#select_versionName')
        .find('option')
        .remove();
    $("#select_versionName").select("val", "");
    $('#select_versionName').append('<option value="0">Select one</option>');
    $.isf.ajax({
        type: "GET",
        url: service_java_URL + "flowchartController/getVersionNameCurProjId/" + projectID + "/" + subActivityID,
        success: function (data) {
            $.each(data, function (j, dat) {
                // console.log(dat);
                $("#select_versionName").append('<option value="' + dat.versionNumber + '">' + dat.workFlowName + '</option>');
            });


        },
        error: function (xhr, status, statusText) {
            console.log("Fail " + xhr.responseText);
            console.log("status " + xhr.status);
            console.log('An error occurred');
        }

    });
}

$(document).on("click", ".view-taskProject", function () {
    var projID = $(this).data('projid')
    var subActId = $(this).data('subactid');
    getTaskBysubActivity(projID, subActId);
    //$(".modal-body #woId").val(woId);
    $('#viewTask').modal('show');
});

$(document).on("click", ".addWorkFlow", function () {
    let version_id = $(this).data('versionno')
    let version_name = $(this).data('workflowname')
    let wfOwnerName = $(this).data('wfownername');
    let experiencedMode = $(this).data('experiencedmode');
    let neneededforexecution = $(this).data('neneededforexecution');
    let slaHours = $(this).data('slahours');
    let domainID = $(this).data('domainid');
    let techID = $(this).data('techid');
    let ftr = $(this).data('ftr');
    if (ftr == null) {
        ftr = 'NA';
    }
    let wfID = $(this).data('wfid');
    if (experiencedMode == 'No') { experiencedMode = 0; } else { experiencedMode = 1; }
    if (neneededforexecution == 'No') { neneededforexecution = 0; } else { neneededforexecution = 1; }
    localStorage.setItem('experiencedMode', experiencedMode);
    localStorage.setItem('neneededforexecution', neneededforexecution);
    localStorage.setItem('slaHours', slaHours);
    localStorage.setItem('ftr', ftr);
    localStorage.setItem('domain_id', domainID);
    localStorage.setItem('tech_id', techID);

    if (version_id != -1) {
        $('#versionNumber').val(version_id);
        var subActID = $(this).data('subactid');
        var projID = $(this).data('projid');
        //  $('#versionName').val(version_name);
        if ($(this).hasClass('editWF')) {
            version_id = version_id;
            flowChartViewOpenInNewWindow('FlowChartWorkOrderAdd\?subId=' + subActID + '&projID=' + projID + '&version=' + version_id + '&wfownername=' + wfOwnerName + '&wfid=' + wfID);
        }

        else {
            version_id = 0;
            flowChartViewOpenInNewWindow('FlowChartWorkOrderAdd\?subId=' + subActID + '&projID=' + projID + '&version=' + version_id + '&wfownername=' + wfOwnerName);
        }
            
        
        //flowChartViewOpenInNewWindow('FlowChartWorkOrderAdd\?subId=' + subActID + '&projID=' + projID + '&version=' + version_id + '&wfownername=' + wfOwnerName + '&wfid=' + wfID);
    }
})

$(document).on("click", ".createWF", function () {
    var version_id = $(this).data('versionno');
    var version_name = $(this).data('workflowname');
    var wfOwnername = $(this).data('wfownname');
    localStorage.setItem('wfOwnername', wfOwnername);
  //  var version = $(this).closest('tr').find('td:eq(1)').find('select option:selected').val();
    // var versionname = $(this).closest('tr').find('td:eq(1)').find('select option:selected').text();
    if (version_id != -1) {
        $('#versionNumber').val(version_id);
        $('#versionName').val(version_name);
        var subActID = $(this).data('subactid');
        var projID = $(this).data('projid');
        var domain = $(this).data('domain');
        var subDomain = $(this).data('subdomain');
        var subServiceArea = $(this).data('subservicearea');
        var tech = $(this).data('tech');
        var activity = $(this).data('activity');
        var subActivity = $(this).data('subactivity');
        var scopeName = $(this).data('scopename');
        var scopeID = $(this).data('scopeid');
        var wfID = $(this).data('wfid');
        let experiencedMode = ($.trim($(this).data('experiencedmode').toLowerCase()) == 'yes') ? '1' : '0';
     
        flowChartViewOpenInNewWindow('FlowChartWorkOrderView\?subId=' + subActID + '&projID=' + projID + '&version=' + version_id + '&domain=' + domain + '&subDomain=' + subDomain + '&subServiceArea=' + subServiceArea + '&tech=' + tech + '&activity=' + activity + '&subActivity=' + subActivity + '&scopeName=' + scopeName + '&scopeID=' + scopeID + '&experiencedMode=' + experiencedMode + '&wfOwnername=' + wfOwnername + '&wfid=' + wfID);
    }
    else {
        pwIsf.alert({ msg: "Please select Version", type: 'error' });
    }

});

//Accept WorkFlow
$(document).on("click", ".accept-WF", function () {
    var json = $(this).closest('td').find("#flowchart").val()
    var projID = $(this).data('projid')
    var subActId = $(this).data('subactid');
    var flowchartDefId = $(this).data('flowchartdefid');
    var employeeSignum = $(this).data('employeesignum');
    var wfName = $(this).data('wfname');
    var wfVersion = $(this).data('wfversion');
    var wfid = $(this).data('wfid');
    var hasRule = $(this).data('hasrule');
    localStorage.setItem("json",json);
    
    $('#projID-approval').val(projID);
    $('#SubActID-approval').val(subActId);
    $('#flowchartDefID-approval').val(flowchartDefId);
    $('#employeeSignum-approval').val(employeeSignum);
    $('#wfVersion-approval').val(wfVersion);
    $('#wfName-approval').val(wfName);
    $('#wfid-approval').val(wfid);

    let infoIcon = '<i class="fa fa-info-circle" style="font-size:22px;color:#9cb3e8;"></i>';
    let autoSenseWarningHtml = '<br />' + infoIcon + '&nbsp; The WF contains autosense rules which are by default disabled and can be enabled via WF edit page'

    pwIsf.confirm({
        title: 'Work Flow Approval Confirmation', msg: 'Are you sure you want to approve the workflow?' + (hasRule ? autoSenseWarningHtml : ''),
        'buttons': {
            'Create New': {
                'action': function () {
                    $('#newVersionWFName').modal({
                              backdrop: 'static'
                          })
                    $('#newVersionWFName').modal('show');
                }
                  },
            'Update Existing': {
                'action': function () {
                    makeEditReasonDD();
                    $('#customWFApprovalReasonModal').modal({
                        backdrop: 'static'
                    })
                    $('#customWFApprovalReasonModal').modal('show');
                    //updateExisting(projID, subActId, flowchartDefId, employeeSignum, wfVersion, wfName, false, wfid)
                }
            },
            'Cancel': {
                'action': function(){},
            }

        }
    });

    function makeEditReasonDD() {
        $.isf.ajax({
            type: "GET",
            url: service_java_URL + "flowchartController/getFlowChartEditReason/",
            success: appendEditReason,
            error: function (msg) {

            },
            complete: function (msg) {

            }
        });

        function appendEditReason(data) {
            $('#editReasonDDApproval').empty();
            $('#editReasonDDApproval').append('<option value="0" selected>Select One</option>')
            $.each(data, function (i, d) {
                $('#editReasonDDApproval').append('<option value="' + d.EditReason + '">' + d.EditReason + '</option>');
            })
        }
    }

  //  $('#acceptApprovalModal').modal({
  //      backdrop: 'static'
  //  })
  //  $('#acceptApprovalModal').modal('show');
});

//Reject WorkFlow
$(document).on("click", ".reject-WF", function () {
    var projID = $(this).data('projid')
    var subActId = $(this).data('subactid');
    var flowchartDefId = $(this).data('flowchartdefid');
    var employeeSignum = $(this).data('employeesignum');
    var versionNo = $(this).data('versionno');
    var wfid = $(this).data('wfid');
    $('#projID').val(projID);
    $('#SubActID').val(subActId);
    $('#flowchartDefID').val(flowchartDefId);
    $('#employeeSignum').val(employeeSignum);
    $('#versionNo').val(versionNo);
    $('#wfid').val(wfid);
    var reasonType = "WorkFlow";
    $.isf.ajax({
            url: service_java_URL + "woExecution/getWOFailureReasons/" +reasonType,
            success: function (data) {
                //pwIsf.removeLayer();
                if (data.isValidationFailed == false) {
                    $.each(data.responseData, function (i, d) {
                        $('#rejectWF').append('<option value="' + d.failureReason + '">' + d.failureReason + '</option>');
                    })
                }
                else {
                    pwIsf.alert({ msg: data.formErrors[0], type: 'error' });
                }
                
                },
                    error: function (xhr, status, statusText) {
               // pwIsf.removeLayer();
                console.log('An error occurred on getProjectName: ' +xhr.error);
                }
        });
    $('#rejectApprovalModal').modal({
        backdrop: 'static'
    })
    $('#rejectApprovalModal').modal('show');
});

$(document).on("click", ".view-WF", function () {
    var json = $(this).closest('td').find("#flowchart").val();
    var projID = $(this).data('projid')
    var subActId = $(this).data('subactid');
    var version = $(this).data('versionno');
    localStorage.setItem("json", json);
   // var myParams = JSON.parse($(this).attr('data-json'));
    flowChartOpenInNewWindow('FlowChartApprovalView?subId=' + subActId + '&projID=' + projID + '&version=' + version);
});

function flowChartOpenInNewWindow(url) {
    var width = window.innerWidth * 0.85;
    // define the height in
    var height = width * window.innerHeight / window.innerWidth;
    // Ratio the hight to the width as the user screen ratio
    window.open(url + '&commentCategory=WO_LEVEL', 'newwindow', 'width=' + width + ', height=' + height + ', top=' + ((window.innerHeight - height) / 2) + ', left=' + ((window.innerWidth - width) / 2));
}

function flowChartViewOpenInNewWindow(url)
{
    var width = window.innerWidth * 0.85;
    // define the height in
    var height = width * window.innerHeight / window.innerWidth;
    // Ratio the hight to the width as the user screen ratio
    //window.open(url, 'newwindow', 'width=' + width + ', height=' + height + ', top=' + ((window.innerHeight - height) / 2) + ', left=' + ((window.innerWidth - width) / 2));
    window.open(url, 'newwindow');

}

function getPriority() {
    $('#select_priority')
        .find('option')
        .remove();
    $("#select_priority").select2({
        dropdownParent: $("#select_priority").closest(".modal")
    });
    $("#select_priority").select2("val", "");
    $('#select_priority').append('<option value="0"></option>');
    pwIsf.addLayer({ text: "Please wait ..." });
   
    $.isf.ajax({

        url: service_java_URL + "woManagement/getPriority ",
        success: function (data) {
            pwIsf.removeLayer();
            $.each(data, function (i, d) {
                $('#select_priority').append('<option value="' + d + '">' + d + '</option>');
            })

        },
        error: function (xhr, status, statusText) {
            pwIsf.removeLayer();
            console.log('An error occurred on getProjectName: ' + xhr.error);
        }
    })

}

$("#select_assign_to_user").on("focus", function () {

})

function getWFUser() {
    //let service_java_URL = "http://169.144.20.66:8080/isf-rest-server-java/"
    $('#select_assign_to_user')
         .find('option')
        .remove();
    $("#select_assign_to_user").select2("val", "");
    $('#select_assign_to_user').append('<option value="0"></option>');

  //  if ($("#Start_Date").val() == '' || $("#End_Date").val() == '') {
    //    $("#select_assign_to_user-Required").text("Please Select Start and End Dates")
       // alert("Please select start and end dates");
        //$("#select_assign_to_user").unbind('onfocus');
       
 //   }
   // else {
        $("#select_assign_to_user-Required").text("")
       /* $("#select_assign_to_user").select2({
            multiple: true,
            minimumInputLength: 1,
            ajax: {
                // url: function (params) {
                //     return service_java_URL + "woManagement/getNodeNames" + "/" + params.projecID + "/" + params.nodeType;
                //  },
               // url: service_java_URL + "activityMaster/getEmployeeByProject/" + projectID + "/'" + $('#Start_Date').val() + "'/'" + $('#End_Date').val() + "'",
                url: service_java_URL + "activityMaster/getEmployeeDetails",
                type: "GET",
                dataType: 'json',
                contentType: "application/json",
                delay: 250,
                data: function (params) {
                    searchTerm = params.term.toUpperCase(); // search term
                },
                processResults: function (data) {

                    return {

                        results: $.map(data, function (obj) {
                            $(".select2-selection--multiple").css("width", "330px");
                            $("#select_assign_to_user-Required").css("display","none");
                            if (obj.signum.indexOf(searchTerm) != -1)
                                return { id: obj.signum, text: obj.signum + "-" + obj.employeeName };
                        })
                    };
                },
                cache: true
            },
        });*/
       // pwIsf.addLayer({ text: "Please wait ..." });


    function split(val) {
        return val.split(/,\s*/);
    }
    function extractLast(term) {
        return split(term).pop();
    }

    var signumResult = [];
    $('#selectUserAllSignum').val('');

    $("#selectUserAllSignum").autocomplete({
        source: function (request, response) {
            $.isf.ajax({
                url: service_java_URL + "activityMaster/getEmployeesByFilter",
                type: "POST",
                data: {
                    term: request.term
                },
                success: function (data) {
                    $("#selectUserAllSignum").autocomplete().addClass("ui-autocomplete-loading");
                    var result = [];
                    signumResult = [];
                    $.each(data, function (i, d) {
                        result.push({
                            "label": d.signum + "/" + d.employeeName,
                            "value": d.signum
                        });
                        signumResult.push(d.signum.toLowerCase());

                    })
                    response(result);
                    $("#selectUserAllSignum").autocomplete().removeClass("ui-autocomplete-loading");

                }
            });
        },
        minLength: 3,
        change: function (event, ui) {
            $("#selectUserAllSignum-required").hide();
            if (!signumResult.includes(event.target.value.toLowerCase())) {

                if (event.target.value.toLowerCase().indexOf(",") > -1) {
                    $("#selectUserAllSignum-required").text("Only one signum can be selected").show();
                }
                else {
                    $("#selectUserAllSignum-required").text("Invalid Signum").show();
                }
               
                //$("#selectUserAllSignum").val("");
            }
            

        }
    });
    $("#selectUserAllSignum").autocomplete("widget").addClass("fixedHeight");
    
}

function getWFUserByProject()
{
    $('#selectallSignumdiv').hide();
    $('#selectUserAllSignum').val('');
    $('#emptysignum-message').hide();
    $('#signumbyprojectdiv').show();
   
    //$('#WOsnumber').attr('value', '1');
    //$('#WOsnumber').prop('readonly', true);
    $('#allUserAnchor').show();
    $('#userByProjAnchor').hide();    
    getWFUserByProjectId();

}


function getWFUserByProjectId() {

    $('#select_assign_to_user')
        .find('option')
        .remove();
    $("#select_assign_to_user").select2("val", "");
    $('#select_assign_to_user').append('<option value="0"></option>');

   // $("#select_assign_to_user-Required").text("")
   
    pwIsf.addLayer({ text: "Please wait ..." });

    $.isf.ajax({

        url: service_java_URL + "activityMaster/getEmployeeByProject/" + projectID,
        beforeSend: function (xhr) {
            //$("#select_assign_to_user").html("<option></option>"); //doesn't work
           
          //  xhr.setRequestHeader("Authorization", "Bearer " + BearerKey);
        },
        success: function (data) {
            pwIsf.removeLayer();
            $.each(data, function (i, d) {
                $('#select_assign_to_user').append('<option value="' + d.signum + '">' + d.signum + " - " + d.employeeName + '</option>');
            });
            //$("#select_assign_to_user").val([signumGlobal.toUpperCase()]).trigger('change');
        },
        complete: function () {
            //$('#select_assign_to_user').find("option").eq(0).remove();
        },
        error: function (xhr, status, statusText) {
            pwIsf.removeLayer();
            console.log('An error occurred on getEmployeeDetails: ' + xhr.error);
        }
    });
 
}

function openRecurrence() {
    if (periodicity == 'Single') {
        periodicity = '';
        document.getElementById("recGrp").style.display = 'inline';
        document.getElementById("recGrp").style.float = 'right';
        document.getElementById("faDwn").style.display = 'none';
        document.getElementById("faUp").style.display = 'inline';
        document.getElementById("endDateDiv").style.display = "inline";
    }
    else {
        periodicity = 'Single';
        document.getElementById("recGrp").style.display = 'none';
        document.getElementById("ContentDaily").style.display = "none";
        document.getElementById("ContentHourly").style.display = "none";
        document.getElementById("ContentWeekly").style.display = "none";
        document.getElementById("endDateDiv").style.display = "none";
        document.getElementById("faDwn").style.display = 'inline';
        document.getElementById("faUp").style.display = 'none';
        if ($('.daily label ').hasClass('active'))
            $('.daily label ').removeClass('active');
        if (daily != null)
            daily = [];
        if ($('.hourly label ').hasClass('active'))
            $('.hourly label ').removeClass('active');
        if (hourly != null)
            hourly = [];
        if ($('.weekly label ').hasClass('active'))
            $('.weekly label ').removeClass('active');
        if (weekly != '')
            weekly = '';
        if ($('.recurrence label ').hasClass('active'))
            $('.recurrence label ').removeClass('active');
        //$('.recurrence input[type=radio]').prop("checked", false);
        //$("'.recurrence input[type=radio]'").find("active").remove("active");
    }

}



function openModalSlaperiodicity(value) {
    console.log(value);
    if (value == "Weekly") {
      
        document.getElementById("ContentDaily").style.display = "none";
        document.getElementById("ContentHourly").style.display = "none";
        document.getElementById("ContentWeekly").style.display = "inline";
      //  document.getElementById("endDateDiv").style.display = "inline";

    }
    if (value == "Daily") {
        
        $('.weekly input[type=radio]').siblings().removeClass('active');
        document.getElementById("ContentWeekly").style.display = "none";
        document.getElementById("ContentHourly").style.display = "none";
        document.getElementById("ContentDaily").style.display = "inline";
        //document.getElementById("endDateDiv").style.display = "inline";

    }
    if (value == "Hourly") {

        $('.weekly input[type=radio]').siblings().removeClass('active');
        document.getElementById("ContentWeekly").style.display = "none";
        document.getElementById("ContentDaily").style.display = "none";
        document.getElementById("ContentHourly").style.display = "inline";
    }
    if (value == "Single") {
        daily = [];
        weekly = '';
        hourly = [];
        document.getElementById("ContentDaily").style.display = "none";
        document.getElementById("ContentWeekly").style.display = "none";
        document.getElementById("ContentHourly").style.display = "none";
        document.getElementById("endDateDiv").style.display = "none";

    }


}

function createTransition() {
    if (validationsWrkFlow()) {
        //$(".mandatory_box").slideup();
        $(".node_box").css("display", "inline");
        $("#slideUp").css("display", "none");
        $("#slideDwn").css("display", "inline");
        if (nodeNameClicked == 0) {
            nodeNameClicked = 1;            
        }
        
    }
    

}

function createTransitionEP() {
    document.getElementById("faDown").style.display = 'inline';
    document.getElementById("faUps").style.display = 'none';
    $(".ep_box").css("display", "inline");
}

function resetTransition() {
    $(".mandatory_Box").slideDown();
    $(".node_box").css("display", "none");
    $("#slideUp").css("display", "inline");
    $("#slideDwn").css("display", "none");
    resetNetworkElementFields('create');

    nodeNameClicked = 0;
    pwIsf.alert({ msg: 'Network Element details are not provided properly, the respective data will be lost!', type: 'warning' });
}

function getAllUsers()
{
    $('#allUserAnchor').hide();
    $('#userByProjAnchor').show();
    $("#selectallSignumdiv").show();
    //$("#WOsnumber").removeAttr('readonly');
    //$("#WOsnumber").removeAttr('value');
    $("#signumbyprojectdiv").hide();
    $('#woCountDiv').show();
    $("#selectUserAllSignum-required").hide();
    //$("#woCount").val('1');
    //alert('All users unlocked');
    getWFUser();
}

function checkNumber(el) {
    if ($(el).val() < 0 || $(el).val() == 0) {
        $(el).val('1');
    }
    else if ($(el).val() > 20) {
        $(el).val('20');
    }
}

function addMoreNodes() { 
    nodeNamesValidated = false;
    $("#workOrderViewIdPrefix_new-node-site").prop('readonly', false);
    $('#addNodesValid').hide();
    $('#changeIcon').hide();
}


function deleteFile(el) {
    let index = $(el).closest('li').index();
    $("#inputFile li:eq(" + index + ")").remove();
}

function deleteDisplayFile(el) {
    let index = $(el).closest('li').index();
    $("#displayFiles li:eq(" + index + ")").remove();
    if ($('#displayFiles li').length == 0) {
        $('#displayFilesDiv').hide();
    }
}

function checkFileURL(el) {
    let OK = true;
    let inputFileUrl = '';
    let index = $(el).closest('li').index();
    if (el == undefined)
        inputFileUrl = $("#editInputUrl").val();
    else
        inputFileUrl = $("#inputFile li:eq(" + index + ") #inputUrl").val();
    if (inputFileUrl == "" || inputFileUrl == null) {
        pwIsf.alert({ msg: 'Please provide URL of the file!', type: 'warning' });
        OK = false;
    }
    else if (!(/^(http|https|ftp):\/\/[a-z0-9]+([\-\.]{1}[a-z0-9]+)*\.[a-z]{2,5}(:[0-9]{1,5})?(\/.*)?$/i.test(inputFileUrl))) {
        pwIsf.alert({ msg: 'Invalid URL', type: 'warning' });
        OK = false;
    }
    if (OK) {
        var win = window.open(inputFileUrl, '_blank');
        win.focus();
    }
}

function filesArray() {
    let fileArr = [];
    let fileLength = $("#displayFiles li").length;
    for (let i = 0; i < fileLength; i++) {
        let fileObj = new Object();
        fileObj["inputName"] = $('#displayFiles li:eq(' + i + ') .inputFileSelected').text();
        fileObj["inputUrl"] = $('#displayFiles li:eq(' + i + ') .inputFileSelected').attr('href');
        fileArr.push(fileObj);
    }
    return fileArr;
}

function createWorkOrderWrkFlow(scopeID, subActId, version, flowchartdefid) {
    
    $('#validStatus').css('display', 'none');
   
    if (validationsWrkFlow()) {
        var isNodeWise;
        if ($('#nodeWise').prop('checked') == true) {
            isNodeWise = true;
        }
        else {
            isNodeWise = false;
        }
        let doVolume = $('#woCount').val();
        let wOName = $('#WO_name').val();
        let scopeName = $('#Select_Scope_Wo option:selected').text();
        let WO = new Object();
        if (doVolume == null || doVolume == undefined || doVolume == '') {
            WO.doVolume = 1;
        }
        else {
            WO.doVolume = doVolume;
        }
        WO.projectID = projectID;
        WO.executionPlanName = scopeName;
        WO.scopeID = scopeID;
        WO.wOPlanID = 1;
        WO.isNodeWise = isNodeWise;
        WO.woName = wOName;
        WO.subActivityID = subActId;
        WO.wfVersion = version;
        WO.priority = priority;
        WO.flowchartDefId = flowchartdefid;
        if (daily == null || daily.length == 0)
            daily = null;
        else
            daily = daily.join()
        WO.periodicityDaily = daily;
        if (weekly == '' || weekly == null)
            weekly = null;
        else
            weekly = weekly;
        WO.periodicityWeekly = weekly;
        if (hourly == null || hourly.length == 0)
            hourly = null;        
        WO.periodicityHourly = hourly;
        var timeDate = $("#Start_Date").val() + " " + $("#start_time").val();
        var startDateIST = GetConvertedDate_tz(timeDate);
        WO.startDate = startDateIST;
        var startTimeIST = GetConvertedTime_tz(timeDate);
        WO.startTime = startTimeIST;
        WO.endDate = $("#End_Date").val();
        WO.externalSourceName = "WEB";
        WO.wOName = $("#WO_name").val();
        let slaHrs;
        if ($("#slaHrs").val() == null || $("#slaHrs").val() == '')
            slaHrs = '';
        else
            slaHrs = $("#slaHrs").val();
        WO.selectUser =  select1;
        var allSignumIDArr = [];
        var SignumArr = [];
        var TempSig = $('#selectUserAllSignum').val();
        var signumIDArr = [];
        if (localStorage.getItem("projectType") != "ASP") {
            var sig = $("#select_assign_to_user option:selected").val();
            signumIDArr.push(sig);
        }
        else {
            allSignumIDArr.push($("#selectAspUserSignum").val());
            if (!allSignumIDArr) {
                allSignumIDArr = [];
            }
        }
        if (TempSig != null && TempSig != undefined && TempSig != "") {
            allSignumIDArr.push(TempSig);
            WO.lstSignumID = allSignumIDArr;
        }
        else if ((TempSig == "") && (sig != null && sig != undefined && sig != "0")) {
            WO.lstSignumID = signumIDArr;
        }
        else if (signumIDArr != null && signumIDArr[0] != undefined && signumIDArr[0] != "0" ) {
            WO.lstSignumID = signumIDArr;
        }
        else if (signumIDArr != null && signumIDArr[0] == "0") {
            allSignumIDArr.push(TempSig);
            WO.lstSignumID = allSignumIDArr;
        }
        else if (allSignumIDArr != null && allSignumIDArr != undefined && allSignumIDArr[0] != undefined && allSignumIDArr[0] != "0" && (WO.lstSignumID == null || WO.lstSignumID == undefined)) {
            WO.lstSignumID = allSignumIDArr;
        }
        else {
            WO.lstSignumID = [];
        }
            
        WO.lastModifiedBy = signumGlobal;
        WO.createdBy = signumGlobal;
        WO.externalSourceName = "WEB";
        let nodeNameAuto = $("#woc_select_nodeName").select2("val");
        let nodeNamesFinal = ""; var nodeNamesDistinct = "";

        if (nodeNamesFinal == null)
            nodeNamesFinal = "";

        arr = $.unique(nodeNamesFinal.split(','));
        nodeNamesDistinct = arr.join(",");
        var nodeType = $("#select_nodeType").val();
        var listOfNodeJson = { "nodeNames": nodeNamesDistinct, "nodeType": nodeType }
        var listOfNode = [];
        listOfNode.push(listOfNodeJson);
        WO.executionPlanId = $("#Select_Scope_Wo").find(':selected').data('planid');
        WO.listOfNode = listOfNode;
        WO.comment = $('#commentsBox').val();
        let listOfFiles = filesArray();
        let woInputObj = new Object();
        woInputObj["woid"] = "";
        woInputObj["file"] = listOfFiles;
        woInputObj["createdBy"] = "";
        if (!$('#displayFilesDiv').is(":hidden")) {
            WO.workOrderInputFileModel = woInputObj;
        }

        if ($('#slideDwn').is(':visible')) {
            WO.tableName = $(`#tableName_create`).val();
            WO.count = $('#textAreaForNECount_create').val();
            WO.elementType = $('#networkElementType_create').val();

            if ($('#commaSeparatedFreeTextArea_create').is(':visible')) {
                WO.neTextName = $('#freeTextAreaForNE_create').val();
            }
        }

        console.log("The final result for call " + JSON.stringify(WO));
        pwIsf.addLayer({ text: "Please wait ..." });
        
        $.isf.ajax({
            url: service_java_URL + "woManagement/createWorkOrderPlan",
            context: this,
            crossdomain: true,
            processData: true,
            contentType: 'application/json',
            type: 'POST',
            data: JSON.stringify(WO),
            xhrFields: {
                withCredentials: false
            },
            success: function (data) {
                pwIsf.removeLayer();
                if (data.WorkOrderID.length !=0 && data.woPlanId != 0) {
                    pwIsf.alert({ msg:  "<center> DELIVERABLE ORDER PLAN : " + data.woPlanId + " created successfully</center>", type: 'success' });
                    priority = '';
                    $("#start_time").select2("val", 0);
                    $('#select_nodeType').find('option').remove();
                    $("#select_nodeType").select2("val", "");
                    $('#select_nodeType2').find('option').remove();
                    $("#select_nodeType2").select2("val", "");
                    $('#select_versionName').find('option').remove();
                    $("#select_versionName").select2("val", "");
                    $('#woc_select_nodeName').find('option').remove();
                    $("#woc_select_nodeName").select2("val", "");//for select
                    select1 = '';
                    $('#Start_Date').val("");

                    $('#select_vendor').find('option').remove();
                    $('#select_vendor').select2("val", "");
                    $('#select_markArea').find('option').remove();
                    $('#select_markArea').select2("val", "");
                    $('#End_Date').val("");
                    $('#WO_name').val("");
                    $('#domainSp').val("");
                    $('#subServSp').val("");
                    $('#scopeSp').val("");
                    $('#techSp').val("");
                    $('#WfName').html("");
                    $('#slaHrs').val("");
                    $('#ActSubActSp').html('');
                    if ($('.weekly label ').hasClass('active'))
                        $('.weekly label ').removeClass('active');

                    if ($('.priority label ').hasClass('active'))
                        $('.priority label ').removeClass('active');

                    if ($('.daily label ').hasClass('active'))
                        $('.daily label ').removeClass('active');

                    if ($('.hourly label ').hasClass('active'))
                        $('.hourly label ').removeClass('active');

                    if ($('.recurrence label ').hasClass('active'))
                        $('.recurrence label ').removeClass('active');

                    daily = [];
                    hourly = [];
                    weekly = '';
                    periodicity = 'Single';
                    recurr = '';
                    priority = '';
                    nodeNameClicked = 0;
                    document.getElementById("ContentDaily").style.display = "none";
                    document.getElementById("ContentHourly").style.display = "none";
                    document.getElementById("ContentWeekly").style.display = "none";
                    document.getElementById("endDateDiv").style.display = "none";
                    document.getElementById("recGrp").style.display = 'none';
                    document.getElementById("faDwn").style.display = 'inline';
                    document.getElementById("faUp").style.display = 'none';
                    $('#nodeWise').prop('checked', false);
                    $('#modal_wo_creation').modal('hide');
                    $('#allUserAnchor').show();
                    $('#userByProjAnchor').hide();
                    $('#displayFilesDiv').hide();
                    $('#displayFiles').html('');
                    resetNetworkElementFields('create');
                    getWorkOrderDetails();
                }
                else {
                    pwIsf.alert({ msg: data.msg, type: 'error' });
                }
                isValidated = false;
            },
            error: function (xhr, status, statusText) {
                pwIsf.removeLayer();
                pwIsf.alert({ msg: "Data cannot be saved.", type: 'error' });
            }
        });

   }
   else
       pwIsf.alert({ msg: "Fields not properly filled.", type: 'error' });
}

function closeModal()
{
    $("#displayFilesDiv").hide();
    $('#displayFiles').html('');
    //$("#select_periodicity").select2("val", 0);
    $("#start_time").select2("val", 0);
    $('#select_nodeType').find('option').remove();
    $("#select_nodeType").select2("val", "");
    $('#select_nodeType2').find('option').remove();
    $("#select_nodeType2").select2("val", "");
    $('#woc_select_nodeName').find('option').remove();
    $('#validStatus').css('display', 'none');
    $("#woc_select_nodeName").select2("val", "");//for select
    priority = '';
    $('#select_vendor').find('option').remove();
    $('#select_vendor').select2("val", "");
    $('#select_markArea').find('option').remove();
    $('#select_markArea').select2("val", "");
    $('#Start_Date').val("");
    $('#End_Date').val("");
    $('#WO_name').val("");
    $('#slaHrs').val("");
    select1 = '';
    $('#nodeWise').prop('checked', false);
    $('#domainSp').val("");
    $('#subServSp').val("");
    $('#scopeSp').val("");
    $('#techSp').val("");
    $('#WfName').html('');
    $('#ActSubActSp').html('');
    if ($('.weekly label ').hasClass('active'))
        $('.weekly label ').removeClass('active');

    if ($('.daily label ').hasClass('active'))
        $('.daily label ').removeClass('active');

    if ($('.hourly label ').hasClass('active'))
        $('.hourly label ').removeClass('active');

    if ($('.priority label ').hasClass('active'))
        $('.priority label ').removeClass('active');

    if ($('.recurrence label ').hasClass('active'))
        $('.recurrence label ').removeClass('active');

    daily = [];
    hourly = [];
    weekly = '';
    periodicity = 'Single';
    recurr = '';
    priority = ''
    nodeNameClicked = 0;
    document.getElementById("ContentDaily").style.display = "none";
    document.getElementById("ContentHourly").style.display = "none";
    document.getElementById("ContentWeekly").style.display = "none";
    document.getElementById("endDateDiv").style.display = "none";
    document.getElementById("recGrp").style.display = 'none';
    document.getElementById("faDwn").style.display = 'inline';
    document.getElementById("faUp").style.display = 'none';
    $('#WO_name-Required').text("");
    $('#slaHrsRequired').text("");
    $('#priorityRequired').text("");
    $('#select_assign_to_user-Required').text("");
    $('#Start_Date-Required').text("");
    $('#periodReq').text("");
    $('#End_Date-Required').text("");
    $('#start_time-Required').text("");
    $('#select_versionName-Required').text("");
    $('#modal_wo_creation').modal('hide');
    $('#allUserAnchor').show();
    $('#userByProjAnchor').hide();

    resetNetworkElementFields('create');
}
function validationsWrkFlow() {
    var OK = true;
    var k = "";
	var st = $("#Start_Date").val();
    var timeZone = localStorage.getItem("UserTimeZone");
    if (timeZone == null || timeZone == "" || timeZone == undefined) {
        timeZone = "Asia/Calcutta";
    }
    var now = new Date();
    var nosofWO = $("#WOsnumber").val();
    var today = moment(now.toString()).tz(timeZone).format();
    var today1 = today.split("T")[0]; 
  
    var woName = $("#WO_name").val();
    
    var comment = $('#commentsBox').val();
    var commLength = comment.length;

    $('#comments-Required').text("");
    if (commLength > 1000) {
        OK = false;
        $('#comments-Required').text("Comments should be less than or equal to 1000");
    }

    $('#Scope_Name-Required').text("");
    if ($("#Select_Scope_Wo option:selected").val() == "-1") {
        $('#Scope_Name-Required').text("Deliverable Name is required");
        OK = false;
    }

    if(allScopesBool){

    }

    $('#WO_name-Required').text("");
    if ($("#WO_name").val() == null || $("#WO_name").val() == "") {
        $('#WO_name-Required').text("WorkOrder Name name is required");
        OK = false;
    }

    if ($("#selectUserAllSignum-required").is(":visible")) {
        OK = false;
    }

    $('#priorityRequired').text("");
    if (priority=='') {
        $('#priorityRequired').text("priority is required");
        OK = false;

    }
    
    $('#Start_Date-Required').text("");
    if ($("#Start_Date").val() == null || $("#Start_Date").val() == "") {
        $('#Start_Date-Required').text("Start date is required");
        OK = false;
    } else if (new Date(today1) > new Date(st)) {
        $('#Start_Date-Required').text("Start date should not be less than today's date");
        OK = false;
    }

    $('#periodReq').text("");
    if (periodicity != 'Single' && (hourly.length == 0 && daily.length == 0 && weekly == ''))
    {
        console.log('periodicity ' + periodicity + ' daily ' + daily + ' weekly ' + weekly + ' hourly ' + hourly )
        $('#periodReq').text("Select Recurrence");
        OK = false;
    }

    $('#End_Date-Required').text("");
    if (periodicity != 'Single' && ($("#Start_Date").val() > $("#End_Date").val())) {
        console.log("Start Time is greater than End Date 1")
        $('#End_Date-RequireddReq').text("Start Date is greater than End Date");
        OK = false;
    }

    $('#End_Date-Required').text("");
    if (periodicity != 'Single' && ($("#End_Date").val() != "" && $("#Start_Date").val() > $("#End_Date").val())) {
        console.log("Start DATE is greater than End Date 2")
        $('#End_Date-Required').text("Start Date is greater than End Date");
        OK = false;
    }

    $('#Planned_End_Date-Required').text("");
    if (($("#Plan_End_Date").val() != "" && $("#Start_Date").val() > $("#Plan_End_Date").val())) {        
        $('#Planned_End_Date-Required').text("Start Date is greater than End Date");
        OK = false;
    }

    $('#Planned_End_Date-Required').text("");
    if ($("#Start_Date").val() > $("#Plan_End_Date").val()) {
        $('#Planned_End_Date-Required').text("Start Date is greater than End Date");
        OK = false;
    }

    $('#start_time-Required').text("");
    if ($("#start_time option:selected").val() == "0") {
        $('#start_time-Required').text("Start Time is required");
        OK = false;
    }


    $('#End_Date-Required').text("");
    if (periodicity != 'Single' && ($("#End_Date").val() == null || $("#End_Date").val() == "")) {
        console.log('Date '+$("#End_Date").val());
        $('#End_Date-Required').text("End date is required");
        OK = false;
    }

    var dt = Date.parse(localStorage.getItem("StartDate"));
   
    $('#End_Date-Required').text("");
    if (new Date($("#Start_Date").val()) < new Date(localStorage.getItem("StartDate"))) {
        $('#End_Date-Required').text("Start date is smaller then project start date.");
        OK = false;
    }


    if (new Date($("#End_Date").val()) < new Date(localStorage.getItem("StartDate"))) {
        pwIsf.alert({ msg: "End date is smaller then project start date.", type: 'error' });
        OK = false;
    }


    if ((select1 == 'singleUserTrue') && (k == null || k.length > 1)) {
        pwIsf.alert({ msg: "Multiple user selected.", type: 'error' });
        $('#nodeWise').prop('checked', false);
        OK = false;
        select1 = '';
    }

    $('#select_versionName-Required').text("");
    if ($("#select_versionName option:selected").val() == "0") {
        $('#select_versionName-Required').text("Work Version Name  is required");
        OK = false;
    }

    if ($('#slideDwn').is(':visible')) {
        let isNEValidated = $('#isNEValidated_create').val();
        if (!JSON.parse(isNEValidated)) {
            $('#networkElementValidationMsg_create').show();
            OK = false;
        }
    }
  
    return OK;

}

function userWiseNodeSelectOtion()
{
    //var k = $("#select_assign_to_user").val();
    //if (select1 == '') {
    //    select1 = 'singleUserTrue';
    //    console.log('nodeWise is chekd');
    //    $('#nodeWise').prop('checked', true);

    //}
    //else {
    //    select1 = '';
    //    $('#nodeWise').prop('checked', false);
    //}

    //if (k == null ||k.length > 1  )
    //{
    //    alert('No user or Multiple user selected');
    //    $('#nodeWise').prop('checked', false);
    //    select1 = '';      

    //}
    //else
    //{
    //    $('#nodeWise').prop('checked', true);
    //    select1 = 'singleUserTrue';
        

    //}
    var assignToAllUserLength = 0;
    var assignToUserLength = 0;
    var assignToUser = $("#select_assign_to_user").val()
    var assignToAllUser = $("#selectUserAllSignum").val()
    var assignToAllUser1 = assignToAllUser.split(", ");
    var assignToAllUserLength1 = assignToAllUser1.slice(0,-1);

    if (assignToUser != null)
        assignToUserLength = assignToUser.length;
    if (assignToAllUserLength1 != null)
        assignToAllUserLength = assignToAllUserLength1.length;
    
   // console.log('select1 ' + select1);
    if (select1 == '')
        select1 = 'singleUserTrue';
    else
        select1 = '';
   // console.log('select1 after if else ' + select1 + ' ' + assignToUserLength);

    //if (((assignToUserLength == 0 && assignToAllUserLength == 0) || (assignToUserLength > 1 || assignToAllUserLength > 1)) && (select1 == 'singleUserTrue')) {
    //    select1 = '';
    //    $('#nodeWise').prop('checked', false);
        
    //    pwIsf.alert({ msg: "Check this only for node wise work order for single user. This will increase number of Work Order", type: 'info' });
    //}

    if ((assignToUserLength > 1 || assignToAllUserLength > 1) && (select1 == 'singleUserTrue')) {
        select1 = '';
        $('#nodeWise').prop('checked', false);

        pwIsf.alert({ msg: "Check this only for node wise work order for single user. This will increase number of Work Order", type: 'info' });
    }

    //console.log('select1 after if else assign  ' + select1);
     
}

function getExecutionPlans(subActivityID, wfVersionNo) {
    $('#executionPlan').empty();
    $('#executionPlan').append('<option value="0" selected disabled>Select one</option>');
    $.isf.ajax({
        type: "GET",
        url: service_java_URL + "woManagement/searchExecutionPlans?subactivityId=" + subActivityID + "&workFlowVersionNo=" + wfVersionNo,
        //url: service_java_URL + "woManagement/getActiveExecutionPlansBySubactivity?subactivityId=" + subActivityID,
        success: appendExecutionPlans,
        error: function (msg) {

        },
        complete: function (msg) {
            pwIsf.removeLayer();
        }
    });
    function appendExecutionPlans(data) {
        var html = "";
        $.each(data, function (i, d) {
            $('#executionPlan').append('<option value="' + d.executionPlanId + '">' + d.planName + '</option>');
            //$('#subActivityDetails').select2();
        })
    }
}

//Function to get both local and global URLs
function getLocalGlobalURLs(modalID) {

    hideLocalGlobalURLs(modalID);
    getAllGlobalURLsWOCreation(modalID);
    getAllLocalURLs(modalID);

}

//Funtion to get all the global URLs
function getAllGlobalURLsWOCreation(modalID) {

    let service_URL = service_java_URL + 'activityMaster/getAllGlobalUrl';

    $.isf.ajax({
        url: service_URL,
        type: 'GET',
        success: function (data) {
            console.log("success");

        },
        complete: function (xhr, status) {
            if (status == "success") {

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

    let configureGlobalURLDataTable = (getData) => {

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
function getAllLocalURLs(modalID) {
    if (projectID) {
        let service_URL = service_java_URL + 'activityMaster/getAllLocalUrl?projectID=' + projectID;

        $.isf.ajax({
            url: service_URL,
            type: 'GET',
            success: function (data) {
                console.log("success");

            },
            complete: function (xhr, status) {
                if (status == "success") {

                    configureLocalURLDataTable(xhr.responseJSON.responseData, modalID); // Add Datatable configuration to the table
                }
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





 