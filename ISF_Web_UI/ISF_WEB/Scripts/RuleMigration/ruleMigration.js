
$(document).ready(function () {

    getAutosenseMigrationRules();

});
//For manually validation checkbox
function manualValidation(object) {
    let manualValidationObj = new Object();
    let manualValidated;
    if (object.checked == true) {
        manualValidated = 1;
    }
    else {
        manualValidated = 0;
    }
    manualValidationObj.manualValidated = manualValidated;
    manualValidationObj.ruleMigrationID = $(object).attr("data-ruleID");
    pwIsf.addLayer({ text: "Please wait" });
    $.isf.ajax({
        type: "POST",
        url: service_java_URL + "autoSense/manualValidationMigrationAutoSenseRule",
        crossdomain: true,
        processData: true,
        contentType: 'application/json',
        data: JSON.stringify(manualValidationObj),
        success: function (data) {

            if (data.isValidationFailed == true) {
                pwIsf.alert({ msg: data.formErrors[0], type: "info", autoClose: 2 });
            }

        },
        error: function (status) {
            pwIsf.alert({ msg: "Error while updating", type: "error", autoClose: 2 });
        },
        complete: function () {
            pwIsf.removeLayer();
            getAutosenseMigrationRules();
        }
    })
}
//Deletion of a rule while migration 
function deleteMigrationAutoSenseRule(object) {
    let deleteRuleObj = new Object();
    deleteRuleObj.ruleMigrationID = $(object).attr("data-ruleID");
    var msgVar = '';
    msgVar = 'Do you want to delete this Autosense Rule?';
    pwIsf.confirm({
        title: 'Deletion Of Autosense Rule while Migration', msg: msgVar,
        'buttons': {
            'Yes': {
                'action': function () {
                    pwIsf.addLayer({ text: 'Please wait ...' });
                    $.isf.ajax({
                        url: service_java_URL + "autoSense/deleteMigrationAutoSenseRule",
                        context: this,
                        crossdomain: true,
                        processData: true,
                        contentType: 'application/json',
                        type: 'POST',
                        data: JSON.stringify(deleteRuleObj),
                        xhrFields: {
                            withCredentials: false
                        },
                        success: function (data) {

                            if (data.isValidationFailed == true) {
                                pwIsf.alert({ msg: data.formErrors[0], type: "error", autoClose: 2 });
                            }
                        },
                        error: function (xhr, status, statusText) {
                            pwIsf.alert({ msg: "Error in API.", type: "error", autoClose: 2 });
                        },
                        complete: function (xhr, statusText) {
                            pwIsf.removeLayer();
                            getAutosenseMigrationRules();
                            
                        }
                    });
                }
            },
            'No': { 'action': function () { } },

        }
    });
}
//Downloading json for a specific rule in a text file
function downloadJsonRule(object) {
    let ruleID = $(object).attr("data-ruleID");
    let ruleName = $(object).attr("data-ruleName");
    pwIsf.addLayer({ text: "Please wait" });
    $.isf.ajax({
        type: "GET",
        async: false,
        url: service_java_URL + "autoSense/getRuleJsonByMigrationID?ruleMigrationID=" + ruleID,
        crossdomain: true,
        processData: true,
        contentType: 'application/json',
        success: function (data) {


            if (data.isValidationFailed == true) {
                pwIsf.alert({ msg: "Error in downloading file.", type: "error", autoClose: 2 });
            }
            else if (data.isValidationFailed == false) {
                let responseJson = data.responseData.ruleJsonForValidation;
                var dataStrRule = "data:text/json;charset=utf-8," + encodeURIComponent(responseJson);
                var ruleMgtElementdl = document.getElementById("btnDownloadRuleMigration_" + ruleID + "");
                ruleMgtElementdl.setAttribute("href", dataStrRule);
                ruleMgtElementdl.setAttribute("download", "RuleMigrationJson_" + ruleName + ".txt");
            }

        },
        error: function (status) {
            pwIsf.alert({ msg: "Error in API.", type: "error", autoClose: 2 });
        },
        complete: function () {
            pwIsf.removeLayer();

        }
    })
}
//Transferring rule to master table
function TransferRule(object) {
    let ruleID = $(object).attr("data-ruleID");
    pwIsf.addLayer({ text: "Please wait" });
    $.isf.ajax({
        type: "POST",
        async: false,
        url: service_java_URL + "autoSense/transferRuleIntoMasterTable?ruleMigrationID=" + ruleID,
        crossdomain: true,
        processData: true,
        contentType: 'application/json',
        success: function (data) {


            if (data.isValidationFailed == true) {
                pwIsf.alert({ msg: "Error in API.", type: "error", autoClose: 2 });
            }
            else if (data.isValidationFailed == false) {
                pwIsf.alert({ msg: data.formMessages[0], type: 'success', autoClose: '1' }); 
            }

        },
        error: function (status) {
            pwIsf.alert({ msg: "Error in API.", type: "error", autoClose: 2 });
        },
        complete: function () {
            pwIsf.removeLayer();
            getAutosenseMigrationRules();
            

        }
    })
}


//Populating the datatable
function getAutosenseMigrationRules() {

    if ($.fn.dataTable.isDataTable('#migrateAutoSenseRuleTable')) {
        omigrateAutoSenseRuleTable.destroy();
        $('#migrateAutoSenseRuleTable').empty();

    }
    pwIsf.addLayer({ text: "Please wait ..." });

    $.isf.ajax({
        url: service_java_URL + "autoSense/getAllRuleMigrationDetails?creatorSignum=" + signumGlobal,

        success: function (data) {


            $.each(data.responseData, function (i, d) {
                let action;
                var isValidated = '';
                if (d.manualValidated == true) {
                    isValidated = 'checked';
                    if (d.transferred == false) {
                       // var transferIcon = '<button href = "" class="btn btn-xs btn-primary transferbtn" disabled style="position: relative;top: 3px;left: 2px;font-size: 9px !important; data- toggle="tooltip" name ="Transfer" title ="Transfer" data-ruleID=\'' + d.ruleMigrationID + '\' id="transferRule_' + d.ruleMigrationID + '" >Transfer</button>';
                    }

                }
                else if (d.manualValidated == false) {
                    isValidated = '';

                    if (d.transferred == false) {
                     //   transferIcon = '<button href = "" class="btn btn-xs btn-default transferbtn" style="position: relative;top: 3px;left: 2px;font-size: 9px !important;border-color:#9a9a9a" data- toggle="tooltip" name ="Transfer" disabled title ="Transfer" data-ruleID=\'' + d.ruleMigrationID + '\' id="transferRule_' + d.ruleMigrationID + '" onclick="TransferRule(this)">Transfer</button>';
                    }
                }


                if (d.verifiedByISF == true && d.transferred == false) {

                    action = '<div class="checkBoxRow" style="width:100px !important;padding-top: 2px;"><a href="#" class="icon-edit editWrongSign" id="ruleDeleteIcon" style="background-color:#ca402ae6;border: 1px solid #cc1204;padding-left: 5px;padding-right: 5px;padding-top: 2px;padding-bottom: 2px;margin-bottom: 0px;position: relative;top: -1px;font-size: 10px;" data-toggle="tooltip" title="Delete rule(Hard delete)" data-ruleID=\'' + d.ruleMigrationID + '\' data-serviceAreaID=\'' + d.serviceAreaID + '\' data-domainID=\'' + d.domainID + '\' data-technologyID=\'' + d.technologyID + '\' data-taskID=\'' + d.taskID + '\' data-subactivityID=\'' + d.subactivityID + '\' onclick="deleteMigrationAutoSenseRule(this)">' + '<span class="fa fa-times"  style="color:white;height: 13px;"> </span></a>'
                        + ' <a href = "#" class="icon-edit" id = "ruleEditIcon" style = "margin-left: 5px;background - color: #256fb3;border: 1px solid #2e6da4;margin-right: 4px;padding-right: 4px;padding-left: 5px;padding-top: 3px;padding-bottom: 2px;font-size:10px;" data - toggle="tooltip" title = "Edit rule" data-ruleID=\'' + d.ruleMigrationID + '\' data-ruleName=\'' + escapeHtml(d.ruleName) + '\' data-copy=\'' + false + '\' data-ruleDescription=\'' + escapeHtml(d.ruleDescription) + '\' data-serviceAreaID=\'' + d.serviceAreaID + '\' data-domainID=\'' + d.domainID + '\' data-technologyID=\'' + d.technologyID + '\' data-ownerSignum=\'' + d.ownerSignum + '\' data-taskID=\'' + d.taskID + '\' data-subactivityID=\'' + d.subactivityID + '\' data-activity=\'' + d.activity + '\' data-edit=\'' + true + '\' data-active=\'' + d.active + '\' data-dropdownFill=\'' + true + '\' onclick="openAutoSenseDialog(this,true,isMigrationEdit=true,isUpload=false)">' + '<span class="fa fa-edit" style="color:white;border-top-width: 1px;"></span></a>'
                        + '<a href="#"  data-toggle="tooltip" title="Verified in ISF" id="btnVerify_' + d.ruleMigrationID + '" style="margin-right: 4px;" data-ruleID=\'' + d.ruleMigrationID + '\'><i class="fa fa-circle" style="font-size: 21px;padding-top: 2px;color: #30b646;position: relative;top: 3px;left: 2px;"></i></a>'
                        + '<span class="manualCheck" > <input type="checkbox" title="Manual validation completed" onclick="manualValidation(this)" data-ruleID=\'' + d.ruleMigrationID + '\'  id="mnlValdnCheck_' + d.ruleMigrationID + '" name="ruleCheck" style="width: 17px;height: 21px;position: relative;top: 6px;left: 1px;" ' + isValidated + '></span></div>'
                        + '<div style="padding-top: 5px;"><a href="#"  data-toggle="tooltip" title="Download rule json for manual validation" id="btnDownloadRuleMigration_' + d.ruleMigrationID + '"\' data-ruleName=\'' + escapeHtml(d.ruleName) + '\' style="margin-right: 4px;"  data-ruleID=\'' + d.ruleMigrationID + '\' onclick="downloadJsonRule(this)"><i class="fa fa-download glyphicon-ring glyphicon-green" style="font-size: 11px;padding-top: 5px;"></i></a>'
                       // + transferIcon
                        + '</div>';
                }

                else if (d.verifiedByISF == false && d.transferred == false) {
                    action = '<div class="checkBoxRow" style="width:100px !important;padding-top: 2px;"><a href="#" class="icon-edit editWrongSign" id="ruleDeleteIcon" style="background-color:#ca402ae6;border: 1px solid #cc1204;padding-left: 5px;padding-right: 5px;padding-top: 2px;padding-bottom: 2px;margin-bottom: 0px;position: relative;top: -1px;font-size: 10px;" data-toggle="tooltip" title="Delete rule(Hard delete)" data-ruleID=\'' + d.ruleMigrationID + '\' data-serviceAreaID=\'' + d.serviceAreaID + '\' data-domainID=\'' + d.domainID + '\' data-technologyID=\'' + d.technologyID + '\' data-taskID=\'' + d.taskID + '\' data-subactivityID=\'' + d.subactivityID + '\' onclick="deleteMigrationAutoSenseRule(this)">' + '<span class="fa fa-times"  style="color:white;height: 13px;"> </span></a>'
                        + ' <a href = "#" class="icon-edit" id = "ruleEditIcon" style = "margin-left: 5px;background - color: #256fb3;border: 1px solid #2e6da4;margin-right: 4px;padding-right: 4px;padding-left: 5px;padding-top: 3px;padding-bottom: 2px;font-size:10px;" data - toggle="tooltip" title = "Edit rule" data-ruleID=\'' + d.ruleMigrationID + '\' data-ruleName=\'' + escapeHtml(d.ruleName) + '\' data-copy=\'' + false + '\' data-ruleDescription=\'' + escapeHtml(d.ruleDescription) + '\' data-serviceAreaID=\'' + d.serviceAreaID + '\' data-domainID=\'' + d.domainID + '\' data-technologyID=\'' + d.technologyID + '\' data-ownerSignum=\'' + d.ownerSignum + '\' data-taskID=\'' + d.taskID + '\' data-subactivityID=\'' + d.subactivityID + '\' data-activity=\'' + d.activity + '\' data-edit=\'' + true + '\' data-active=\'' + d.active + '\' data-dropdownFill=\'' + true + '\' onclick="openAutoSenseDialog(this,true,isMigrationEdit=true,isUpload=false)">' + '<span class="fa fa-edit" style="color:white;border-top-width: 1px;"></span></a>'
                        + '<a href="#"  data-toggle="tooltip" title="Not Verified in ISF" id="btnVerify_' + d.ruleMigrationID + '" style="margin-right: 4px;" data-ruleID=\'' + d.ruleMigrationID + '\'><i class="fa fa-circle" style="font-size: 21px;padding-top: 2px;color:red;position: relative;top: 3px;left: 2px;"></i></a></div>';
                }
                else if (d.transferred == true) {
                    action = '<button href = "" class="btn btn-xs btn-success transferbtn" style="position: relative;top: 3px;left: 2px;font-size: 9px !important;  pointer-events: none; data- toggle="tooltip" name ="Transferred" title ="Transferred"  data-ruleID=\'' + d.ruleMigrationID + '\' id="transferRule_' + d.ruleMigrationID + '" onclick="">Transferred</button>';
                }



                d.action = action;
            })


            $("#migrateAutoSenseRuleTable").append($('<tfoot><tr><th></th><th></th><th></th><th></th><th></th><th></th><th></th><th></th><th></th><th></th></tr></tfoot>'));

            omigrateAutoSenseRuleTable = $('#migrateAutoSenseRuleTable').DataTable({
                searching: true,
                responsive: true,
                "pageLength": 10,
                "order": [],
                "data": data.responseData,
                dom: 'Bfrtip',
                buttons: [
                    'colvis', 
                    {
                        extend: 'excel',
                        text: 'Excel',                       
                        exportOptions: {
                            columns: 'th:not(:first-child)'
                        }
                    }
                ],
                "destroy": true,
                 "columnDefs": [
                    {
                        targets: [ 2 ],
                        render: function (data, type) {
                          
                            if (type == 'display') {
                                return `<span class="td-container" title='${data}'>${data}</span>`;
                            } else {
                                return data;
                            }
                        }
                    }
                ],     
                "columns": [
                    { "title": "Actions", "orderable": false, "searchable": false, "data": "action" },
                    { "title": "Rule Name", "data": getruleName },
                    { "title": "Rule Description", "data": getruleDescription, "className": "ruleDescStyling"},
                    { "title": "Rule Type", "data": "ruleType" },
                    { "title": "Service Area/Subservice Area", "data": getServiceSubserviceArea },
                    { "title": "Domain/Subdomain", "data": getDomAndSubdomColumn },
                    { "title": "Activity/Subactivity", "data": getActAndSubactColumn },
                    { "title": "Technology", "data": "technology" },
                    { "title": "Task", "data": "task" },
                    { "title": "Created By", "data": "createdBy" }

                ],
                initComplete: function () {

                    $('#migrateAutoSenseRuleTable tfoot th').each(function (i) {
                        var title = $('#migrateAutoSenseRuleTable thead th').eq($(this).index()).text();
                        if ((title != "Actions"))
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

            $('#migrateAutoSenseRuleTable tfoot').insertAfter($('#migrateAutoSenseRuleTable thead'));


        },
        error: function (xhr, status, statusText) {
            pwIsf.removeLayer();
            console.log("failed to fetch data");
        },
        complete: function (xhr, statusText) {
            pwIsf.removeLayer();
        }

    });
}
//For merging some Columns in datatable
function getActAndSubactColumn(data, type, dataToSet) {
    if (data.activity == null || data.subactivity == null) {
        return '';
    }
    else {
        return data.activity + "/" + data.subactivity;
    }
}
function getDomAndSubdomColumn(data, type, dataToSet) {
    if (data.domain == null || data.subdomain == null) {
        return '';
    }
    else {
        return data.domain + "/" + data.subdomain;
    }
}
function getServiceSubserviceArea(data, type, dataToSet) {
    if (data.serviceArea == null || data.subServiceArea == null) {
        return '';
    }
    else {
        return data.serviceArea + "/" + data.subServiceArea;
    }
}

function getruleDescription(data, type, dataToSet) {

    if (!data.ruleDescription) {
        return '';
    }
    else {
        return escapeHtml(data.ruleDescription);
    }
}
function getruleName(data, type, dataToSet) {

    if (!data.ruleName) {
        return '';
    }
    else {
        return escapeHtml(data.ruleName);
    }
}
function closeAutosensePopup() {
    //   alert(document.getElementById('iframeAutosenseRule').contentWindow.document.body.querySelector('#select_serviceAreaRule').value);
    //$("#createAutosenseRule").modal('hide');
    if ($("#saveRule").is(":disabled")) {
        $("#createAutosenseRule").modal('hide');
    }
    else {
        $("#autoSenseClose").modal('show');


    }

}
//isMigrationEdit true for pop up opened in case of rule migration,isUpload is true if popup opened just after uploading of json file 
function openAutoSenseDialog(object, isEdit = false, isMigrationEdit = false, isUpload = false, ruleIDMgn, ruleNameMgn, uploadMigrateDataDD) {
    pwIsf.addLayer({ text: "Please wait..." });

    $('#autosenseRuleData').attr("data-name", $(object).attr("name"));
    $('#autosenseRuleData').attr("data-isMigrate", true);
    if (isEdit) {
        $('#downloadJsonIcon').css({ "pointer-events": "auto", "color": "" });
        $('#autosenseRuleData').attr("data-checkEdit", $(object).attr("data-edit"));
        $('#autosenseRuleData').attr("data-checkCopy", $(object).attr("data-copy"));
        $('#autosenseRuleData').attr("data-ruleID", $(object).attr("data-ruleID"));
        $('#autosenseRuleData').attr("data-ruleName", $(object).attr("data-ruleName"));
        $('#autosenseRuleData').attr("data-ruleDescription", $(object).attr("data-ruleDescription"));
        $('#autosenseRuleData').attr("data-serviceAreaID", $(object).attr("data-serviceAreaID"));
        $('#autosenseRuleData').attr("data-domainID", $(object).attr("data-domainID"));
        $('#autosenseRuleData').attr("data-technologyID", $(object).attr("data-technologyID"));
        $('#autosenseRuleData').attr("data-subactivityID", $(object).attr("data-subactivityID"));
        $('#autosenseRuleData').attr("data-taskID", $(object).attr("data-taskID"));
        $('#autosenseRuleData').attr("data-active", $(object).attr("data-active"));
        $('#autosenseRuleData').attr("data-dropdownFill", $(object).attr("data-dropdownFill"));
        $('#autosenseRuleData').attr("data-ownerSignum", $(object).attr("data-ownerSignum"));
        $('#autosenseRuleData').attr("data-id", $(object).attr("id"));
       
        //For Rule migration,isUpload true indicates while uploading the json file,false indicates from edit button click
        if (isMigrationEdit) {
            $('#autosenseRuleData').attr("data-mgnPlot", true);
            if (isUpload) {
                $('#downloadJsonIcon').css({ "pointer-events": "none", "color": "darkgray" });
                $('#autosenseRuleData').attr("data-checkEdit", isEdit);
                $('#autosenseRuleData').attr("data-dropdownFill",false);
                $('#autosenseRuleData').attr("data-serviceAreaID", uploadMigrateDataDD.serviceAreaId);
                $('#autosenseRuleData').attr("data-domainID", uploadMigrateDataDD.domainId);
                $('#autosenseRuleData').attr("data-technologyID", uploadMigrateDataDD.technologyId);
                $('#autosenseRuleData').attr("data-subactivityID", uploadMigrateDataDD.subactivityId);
                $('#autosenseRuleData').attr("data-taskID", uploadMigrateDataDD.taskId);
                $('#autosenseRuleData').attr("data-ownerSignum", uploadMigrateDataDD.ownerSignum);
                $('#autosenseRuleData').attr("data-fileName", uploadMigrateDataDD.fileName);
                $('#autosenseRuleData').attr("data-serviceAreaID", uploadMigrateDataDD.serviceAreaId);
                $('#autosenseRuleData').attr("data-isFormatUpload", true);
            }
            $('#autosenseRuleData').attr("data-ruleID", ruleIDMgn);
            $('#autosenseRuleData').attr("data-ruleName", ruleNameMgn);
           
        }
        


        $('#iframeAutosenseRule').attr('src', UiRootDir + '/CreateAutosenseRule/AutoSenseIndex');

        $("#createAutosenseRule").modal('show');

    }
    else {

        $('#downloadJsonIcon').css({ "pointer-events": "none", "color": "darkgrey" });
        $('#autosenseRuleData').attr("data-checkEdit", false);
        $('#iframeAutosenseRule').attr('src', UiRootDir + '/CreateAutosenseRule/AutoSenseIndex');

        $("#createAutosenseRule").modal('show');
    }
    //pwIsf.addLayer({ text: "Please wait" });
}

function downloadJson() {

    document.getElementById("iframeAutosenseRule").contentWindow.downloadObjectAsJson();
}


//API call for save,edit & copy Rule
function saveRuleJson() {
    
    // selecting div for red border
    let ruleIframeBody = document.getElementById('iframeAutosenseRule').contentWindow.document.body;
    let selectServiceArea = ruleIframeBody.querySelector('#select_serviceAreaRuleDiv .select2-selection');
    let select_domainRule = ruleIframeBody.querySelector('#select_domainRuleDiv .select2-selection');
    let select_technologyRule = ruleIframeBody.querySelector('#select_technologyRuleDiv .select2-selection');
    let select_activityRuleDiv = ruleIframeBody.querySelector('#select_activityRuleDiv .select2-selection');
    let select_taskRule = ruleIframeBody.querySelector('#select_taskRuleDiv .select2-selection');
    let input_ruleName = ruleIframeBody.querySelector('#ruleName');
    let input_ruleDesc = ruleIframeBody.querySelector('#ruleDesc');

    let inputMigrateSignum = ruleIframeBody.querySelector('#workOrderViewIdPrefix_assigned_to').value;
   // $('#workOrderViewIdPrefix_assigned_to').css('border', '1px solid red');
    if (!inputMigrateSignum) {
        ruleIframeBody.querySelector('#workOrderViewIdPrefix_assigned_to').style.border = "1px solid red";
    }

    selectServiceArea.style.border = '';
    select_domainRule.style.border = '';
    select_technologyRule.style.border = '';
    select_activityRuleDiv.style.border = '';
    select_taskRule.style.border = '';
    input_ruleName.style.border = '';
    input_ruleDesc.style.border = '';


    let ruleJson = document.getElementById('iframeAutosenseRule').contentWindow.graph.toJSON();
    let validation = document.getElementById('iframeAutosenseRule').contentWindow.validateAndAdjustFlowchartAutosense(ruleJson);
    //let formValidation = document.getElementById('iframeAutosenseRule').contentWindow.validateFormForAutosense(ruleJson);
    let serviceAreaID = ruleIframeBody.querySelector('#select_serviceAreaRule').value;

    for (let i in ruleJson.cells) {
        if (ruleJson.cells[i].type == "html.ConnectorNotification") {
            var ruleType = ruleJson.cells[i].fields.actionName0;
        }
    }
    let ruleId = window.parent.$('#autosenseRuleData').attr('data-ruleID');
    let domainID = ruleIframeBody.querySelector('#select_domainRule').value;
    let subactID = ruleIframeBody.querySelector('#select_activityRule').value;
    let techID = ruleIframeBody.querySelector('#select_technologyRule').value;
    let taskID = ruleIframeBody.querySelector('#select_taskRule').value;
    let ruleName = ruleIframeBody.querySelector('input[id="ruleName"]').value;
    let description = ruleIframeBody.querySelector('#ruleDesc').value;
    let isActive;

    let fileName = window.parent.$('#autosenseRuleData').attr('data-fileName');
    let ownerSignum = ruleIframeBody.querySelector('#workOrderViewIdPrefix_assigned_to').value;

    (ruleIframeBody.querySelector('#rule_active').checked) ? isActive = true : isActive = false;   

    if ((!validation.formValidation.status) || (!validation.jsonValidation.status)) {

    }

    else {

        var ruleJSON = JSON.stringify(ruleJson);
        var parsedRuleJson = JSON.stringify(ruleJson);
        var ruleSaveObj = new Object();
      
        ruleSaveObj.ruleName = ruleName;
        ruleSaveObj.ruleDescription = description;
        ruleSaveObj.ruleType = ruleType;      
        ruleSaveObj.newRuleJson = ruleJSON;      
        ruleSaveObj.serviceAreaID = serviceAreaID;
        ruleSaveObj.domainID = domainID;
        ruleSaveObj.technologyID = techID;
        ruleSaveObj.subactivityID = subactID;
        ruleSaveObj.taskID = taskID;
        ruleSaveObj.ownerSignum = ownerSignum;
       

        var MgnEditRuleSaveObj = new Object();
        MgnEditRuleSaveObj.ruleMigrationID = ruleId;
        MgnEditRuleSaveObj.ruleName = ruleName;
        MgnEditRuleSaveObj.ruleDescription = description;
        MgnEditRuleSaveObj.ruleType = ruleType;
        MgnEditRuleSaveObj.newRuleJson = ruleJSON;
        //MgnEditRuleSaveObj.parsedRuleJson = parsedRuleJson;
        MgnEditRuleSaveObj.serviceAreaID = serviceAreaID;
        MgnEditRuleSaveObj.domainID = domainID;
        MgnEditRuleSaveObj.technologyID = techID;
        MgnEditRuleSaveObj.subactivityID = subactID;
        MgnEditRuleSaveObj.taskID = taskID;
        MgnEditRuleSaveObj.active = isActive;
        MgnEditRuleSaveObj.migrationFileName = fileName;
        MgnEditRuleSaveObj.ownerSignum = ownerSignum;


        if (validation.formValidation.status == true && validation.jsonValidation.status == true) {
            pwIsf.addLayer({ text: "Please wait ..." });
            //  let saveButtonTitle = window.parent.$('#saveRule').html();           
            let saveMode = window.parent.$('#saveRule').attr('data-mode');
            // if (saveButtonTitle == "SAVE THIS RULE" || saveButtonTitle == "COPY THIS RULE") {
            if (saveMode == "save" || saveMode == "copy") {

                $.isf.ajax({
                   // url: service_java_URL + "autoSense/saveAutoSenseRule",
                    url: service_java_URL + "autoSense/saveMigrationAutoSenseRule",
                    type: "POST",
                    //  async: false,
                    data: JSON.stringify(ruleSaveObj),
                    contentType: "application/json",
                    success: function (data) {
                        if (data.isValidationFailed == false) {

                            $("#saveRule").prop('disabled', true);
                            $("#saveRule").css("background", "white");
                            pwIsf.removeLayer();
                            pwIsf.alert({ msg: "Rule Saved Successfully", type: 'success', autoClose: 2 });
                            $('#downloadJsonIcon').css({ "pointer-events": "auto", "color": "" });
                            ruleIframeBody.querySelector('#ruleID').value = data.responseData;
                           // pwIsf.alert({ msg: data.formMessages[0], type: 'success',autoClose: 2 });
                            getAutosenseMigrationRules();

                        }
                        else {
                            pwIsf.removeLayer();
                            pwIsf.alert({ msg: data.formErrors[0], type: 'error',autoClose: 2 });
                        }
                    },
                    error: function () {
                        pwIsf.alert({ msg: 'Error while saving rule.Please contact support.', type: 'error',autoClose: 2 });
                        pwIsf.removeLayer();
                    },
                    complete: function () {
                        pwIsf.removeLayer();
                    }
                });
            }
            else if (saveMode == "edit") {
            //else if (saveButtonTitle == "EDIT THIS RULE") {
                pwIsf.addLayer({ text: "Please wait ..." });

                $.isf.ajax({
                    url: service_java_URL + "autoSense/updateMigrationAutoSenseRule",
                    type: "POST",
                    //  async: false,
                    data: JSON.stringify(MgnEditRuleSaveObj),
                    contentType: "application/json",
                    success: function (data) {

                        if (data.isValidationFailed == false) {
                          //  pwIsf.alert({ msg: data.formMessages[0], type: 'success', autoClose: 2 });
                            pwIsf.alert({ msg: "Rule Updated Successfully", type: 'success', autoClose: 2 });
                            $('#downloadJsonIcon').css({ "pointer-events": "auto", "color": "" });
                            $("#saveRule").prop('disabled', true);
                            window.parent.$('#saveRule').css("background", "white");
                            ruleIframeBody.querySelector('#ruleID').value = ruleId;
                            getAutosenseMigrationRules();
                        }
                        else {
                            pwIsf.alert({ msg: data.formErrors[0], type: 'error', autoClose: 2 });
                        }
                       
                    },
                    error: function () {
                        pwIsf.alert({ msg: 'Error while editing rule.Please contact support.', type: 'error', autoClose: 2 });
                        pwIsf.removeLayer();
                    },
                    complete: function () {
                        pwIsf.removeLayer();
                      
                    }
                });
            }
        }
    }
}
//Yes click
function closeAutosense() {

    $('#autosenseRuleData').attr("data-ruleID", '');
    $('#autosenseRuleData').attr("data-ruleName",'');
    $('#autosenseRuleData').attr("data-ruleDescription",'');
    $('#autosenseRuleData').attr("data-serviceAreaID",'');
    $('#autosenseRuleData').attr("data-domainID", '');
    $('#autosenseRuleData').attr("data-technologyID", '');
    $('#autosenseRuleData').attr("data-subactivityID",'');
    $('#autosenseRuleData').attr("data-taskID",'');
    $('#autosenseRuleData').attr("data-active",'');
    $('#autosenseRuleData').attr("data-dropdownFill", '');
    $('#autosenseRuleData').attr("data-ownersignum", '');
    $('#autosenseRuleData').attr("data-filename", '');
    $('#autosenseRuleData').attr("data-id",'');

    $("#createAutosenseRule").modal('hide');
    $("#autoSenseClose").modal('hide');
    $("#saveRule").prop('disabled', true);
    window.parent.$('#saveRule').css("background", "white");
    
}

function closeAutosenseAlert() {
    $("#autoSenseClose").modal('hide');
}




//2VN close of create autosense rule popup with confirmation alert 
function closeAutosensePopup() {

    if (!$("#saveRule").is(":disabled")) {

        var msgVar = '';
        msgVar = 'Do you want to close without saving?';
        pwIsf.confirm({
            title: 'Close Confirmation', msg: msgVar,
            'buttons': {
                'Yes': {
                    'action': function () {
                        closeAutosense();

                    }
                },
                'No': {
                    'action': function () {
                        closeAutosenseAlert();
                    }
                },

            }
        });

    }

    else {
         closeAutosense();
        $("#createAutosenseRule").modal('hide');
        
    }
}

//2VN check for file extension
function getExtension(filename) {
    var parts = filename.split('.');
    return parts[parts.length - 1];
}

//2VN on change of input file choose validate the file extension 
function checkfileAndUpload(e) {
    //let ruleID = 1405;
    
    let fl = e.target.files[0];
    let fotmatfilename = e.target.files[0].name;
    let nameValid = false;
    let fileNameData = [];
    fileNameData = fotmatfilename.split("_");
    let ruleType = ['start', 'stop'];
    if (fileNameData.length >= 4 && Number.isInteger(parseInt(fileNameData[1])) && ruleType.indexOf(fileNameData[2].toLowerCase()) !== -1 ) {

        nameValid = true;
    }



    let fileName = $("#oldJsonChoose").val();
    let ext = getExtension(fileName);
    if (ext == 'json' && nameValid) {
        $("#jsonFileError").css({ "visibility": "hidden" });
        // open the rappid rule create popup


        var files = document.getElementById('oldJsonChoose').files;
        console.log(files);
        if (files.length <= 0) {
            return false;
        }

        var fr = new FileReader();

        fr.onload = function (e) {
            console.log(e);

            if (isJson(e.target.result)) {

                var fileName = fl;
                var data = new FormData();
                data.append("file", fileName);


                console.log(data);
                pwIsf.addLayer({ text: "Please wait we are parsing it" });
                $.isf.ajax({
                    type: "POST",
                    enctype: "multipart/form-data",  
                    url: service_java_URL + "autoSense/migrateRuleJSON", //2VN api call to convert the old json data to json compatible with rapid
                    data: data,
                    processData: false,                    
                    contentType: false,
                    cache: false,
                    timeout: 600000,
                    success: function (data) {
                        Rdata = data.responseData;
                        if (data.isValidationFailed == false) {
                            //getAutosenseRules()
                            // $('#autosenseRuleData').attr("data-checkEdit", true);

                            getAutosenseMigrationRules();

                            var uploadMigrateDataDD = new Object();
                            uploadMigrateDataDD.serviceAreaId = Rdata.serviceAreaId;
                            uploadMigrateDataDD.domainId = Rdata.domainId;
                            uploadMigrateDataDD.technologyId = Rdata.technologyId;
                            uploadMigrateDataDD.subactivityId = Rdata.subactivityId;
                            uploadMigrateDataDD.taskId = Rdata.taskId;
                            uploadMigrateDataDD.ownerSignum = Rdata.ownerSignum;
                            uploadMigrateDataDD.fileName = Rdata.fileName;
                            uploadMigrateDataDD.ruleType = Rdata.ruleType;

                            

                            openAutoSenseDialog(this, true, isMigrationEdit = true, isUpload = true, Rdata.ruleMigrationId, Rdata.ruleName, uploadMigrateDataDD);
                           
                          
                            setTimeout(function () { $("#oldJsonChoose").val(''); }, 3000);                            

                            //2VN get the parsed data and then open the popup window and plot the rapid diagram , rule name as returned by api
                        }
                        else {
                            pwIsf.removeLayer();
                            pwIsf.alert({ msg: 'JSON data migration failed! Please check the JSON format of uploaded file.', type: 'error',autoClose: 2 });
                           // pwIsf.alert({ msg: data.formErrors[0], type: 'error',autoClose: 2 });
                            setTimeout(function () { $("#oldJsonChoose").val(''); }, 1000);
                         
                        }
                    },
                    error: function () {
                        pwIsf.alert({ msg: 'Error while saving rule.Please contact support.', type: 'error', autoClose: '2' });
                        pwIsf.removeLayer();
                    },
                    complete: function () {
                        setTimeout(function () { $("#oldJsonChoose").val(''); }, 1000);
                        pwIsf.removeLayer();
                    }
                });
            }

        }

        fr.readAsText(files.item(0));



    }
    else {

        if (ext != 'json') {
            $("#oldJsonChoose").val('');
            $("#jsonFileError").css({ "visibility": "visible" });
        }
      
        if (nameValid == false) {
            pwIsf.alert({ msg: "Invalid File Name", type: "error" });
            $("#oldJsonChoose").val('');
        }
    }
}

//2VN functon to check if its valid json
function isJson(str) {
    try {
        JSON.parse(str);
    } catch (e) {
        pwIsf.alert({ msg: "Json is not valid.", type: "error" });
        return false;

    }
    return true;
}


function escapeHtml(text) {
    if (text) {
        return text
            .replace(/&/g, "&amp;")
            .replace(/</g, "&lt;")
            .replace(/>/g, "&gt;")
            .replace(/"/g, "&quot;")
            .replace(/'/g, "&#039;");
    }
    else {
        return '';
    }

}