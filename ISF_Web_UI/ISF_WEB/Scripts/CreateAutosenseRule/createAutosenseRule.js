
$(document).ready(function () {

    getAutosenseRules();

});

//To mark the checkbox as active or inactive
function activateRule(object) {
    let activateRuleObj = new Object();
    activateRuleObj.active = object.checked;
    activateRuleObj.ruleId = $(object).attr("data-ruleID");
    activateRuleObj.ruleName = $(object).closest('tr').find('td:nth-child(2)').text();
    activateRuleObj.ruleDescription = $(object).closest('tr').find('td:nth-child(3)').text();

    pwIsf.addLayer({ text: "Please wait" });
    $.isf.ajax({
        type: "POST",
        url: service_java_URL + "autoSense/activeInactiveAutoSenseRule",
        crossdomain: true,
        processData: true,
        contentType: 'application/json',
        data: JSON.stringify(activateRuleObj),
        success: function (data) {
            getAutosenseRules();
            if (data.isValidationFailed == true) {
                pwIsf.alert({ msg: data.formErrors[0], type: "info", autoClose:2});
            }
            
        },
        error: function (status) {
            pwIsf.alert({ msg: "Error while updating", type: "error", autoClose:2 });
        },
        complete: function () {

           
            pwIsf.removeLayer();
        }
    })
}
//Deletion of a rule 
function deleteRule(object) {
    let deleteRuleObj = new Object();
    deleteRuleObj.ruleId = $(object).attr("data-ruleID");
    deleteRuleObj.ruleName = $(object).closest('tr').find('td:nth-child(2)').text();
    deleteRuleObj.serviceAreaID = $(object).attr("data-serviceAreaID");
    deleteRuleObj.domainID = $(object).attr("data-domainID");
    deleteRuleObj.technologyID = $(object).attr("data-technologyID");
    deleteRuleObj.taskID = $(object).attr("data-taskID");
    deleteRuleObj.subactivityID = $(object).attr("data-subactivityID");
    var msgVar = '';
    msgVar = 'Do you want to delete this Autosense Rule?';
    pwIsf.confirm({
        title: 'Deletion Of Autosense Rule', msg: msgVar,
        'buttons': {
            'Yes': {
                'action': function () {
                    pwIsf.addLayer({ text: 'Please wait ...' });
                    $.isf.ajax({
                        url: service_java_URL + "autoSense/deleteAutoSenseRule",
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
                                pwIsf.alert({ msg: data.formErrors[0], type: "error", autoClose:2 });
                            }
                        },
                        error: function (xhr, status, statusText) {
                            pwIsf.alert({ msg: "Error in API.", type: "error", autoClose:2});
                        },
                        complete: function (xhr, statusText) {
                            pwIsf.removeLayer();
                            getAutosenseRules();
                            
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
        url: service_java_URL + "autoSense/getRuleJsonByID?ruleID=" + ruleID,
        crossdomain: true,
        processData: true,
        contentType: 'application/json',
        success: function (data) {
        

            if (data.isValidationFailed == true) {
                pwIsf.alert({ msg: "Error in downloading file.", type: "error", autoClose:2 });
            }
            else if (data.isValidationFailed == false) {
                let responseJson = data.responseData[0].jsonManualValidation;
                var dataStrRule = "data:text/json;charset=utf-8," + encodeURIComponent(responseJson);
                var ruleJsonElementdl = document.getElementById("btnDownloadRuleJson_" + ruleID + "");
                ruleJsonElementdl.setAttribute("href", dataStrRule);
                ruleJsonElementdl.setAttribute("download", "RuleJson_" + ruleName + ".txt");
            }
               
        },
        error: function (status) {
            pwIsf.alert({ msg: "Error in API.", type: "error", autoClose:2 });
        },
        complete: function () {
            pwIsf.removeLayer();
            
        }
    })
}

//Populating the datatable
function getAutosenseRules() {

    if ($.fn.dataTable.isDataTable('#addAutoSenseRuleTable')) {
        oaddAutoSenseRuleTable.destroy();
        $('#addAutoSenseRuleTable').empty();

    }
    pwIsf.addLayer({ text: "Please wait ..." });

    var serviceUrl = service_java_URL + "autoSense/getAllRulesInMasterData";


    //$.isf.ajax({
    //    url: service_java_URL + "autoSense/getAllRulesInMasterData?",

    //    success: function (data) {
            

    //        $.each(data.responseData, function (i, d) {
    //            let action;
    //            var isChecked = '';
    //            if (d.active == true) {
    //                isChecked = 'checked';
    //            }
                
    //            if (d.createdBy.toLowerCase() == signumGlobal.toLowerCase()) {
    //                if (d.ruleStatus == null) {
    //                    action = '<div class="checkBoxRow" style="width:100px !important;padding-top: 2px;"><a href="#" class="icon-edit editWrongSign" id="ruleDeleteIcon" style="background-color:#ca402ae6;border: 1px solid #cc1204;padding-left: 5px;padding-right: 5px;padding-top: 2px;padding-bottom: 2px;margin-bottom: 0px;font-size:10px;" data-toggle="tooltip" title="Click to Delete the Rule" data-ruleID=\'' + d.ruleId + '\' data-serviceAreaID=\'' + d.serviceAreaID + '\' data-domainID=\'' + d.domainID + '\' data-technologyID=\'' + d.technologyID + '\' data-taskID=\'' + d.taskID + '\' data-subactivityID=\'' + d.subactivityID + '\' onclick="deleteRule(this)">' + '<span class="fa fa-times"  style="color:white;height: 13px;"> </span></a>' + '<a style="margin-right: 4px;position: relative;top: 2px;margin-left: 2px;left: 7px;" href="#" data-toggle="tooltip" title="Click to Copy the Json of Corresponding Rule." id="btnCopyRule" data-ruleID=\'' + d.ruleId + '\' data-copy=\'' + true + '\' data-ruleName=\'' + escapeHtml(d.ruleName) + '\' data-ruleDescription=\'' + escapeHtml(d.ruleDescription) + '\' data-serviceAreaID=\'' + d.serviceAreaID + '\' data-domainID=\'' + d.domainID + '\' data-technologyID=\'' + d.technologyID + '\' data-taskID=\'' + d.taskID + '\' data-subactivityID=\'' + d.subactivityID + '\' data-activity=\'' + d.activity + '\' data-edit=\'' + true + '\' data-active=\'' + d.active + '\' style="margin-right: 4px;" onclick="openAutoSenseDialog(this,true)"><i class="fa fa-files-o" style="padding - top: 6px;font-size: 18px;"></i></a>'
    //                        + ' <a href = "#" class="icon-edit" id = "ruleEditIcon" style = "margin-left: 10px;background - color: #256fb3;border: 1px solid #2e6da4;margin-right: 4px;padding-right: 5px;padding-left: 5px;padding-top: 3px;padding-bottom: 2px;font-size:10px;" data - toggle="tooltip" title = "Click to edit the rule." data-ruleID=\'' + d.ruleId + '\' data-ruleName=\'' + escapeHtml(d.ruleName) + '\' data-copy=\'' + false + '\' data-ruleDescription=\'' + escapeHtml(d.ruleDescription) + '\' data-serviceAreaID=\'' + d.serviceAreaID + '\' data-domainID=\'' + d.domainID + '\' data-technologyID=\'' + d.technologyID + '\' data-taskID=\'' + d.taskID + '\' data-subactivityID=\'' + d.subactivityID + '\' data-activity=\'' + d.activity + '\' data-edit=\'' + true + '\' data-active=\'' + d.active + '\'  onclick="openAutoSenseDialog(this,true)">' + '<span class="fa fa-edit" style="color:white;border-top-width: 1px;"></span></a></div>'
    //                        + '<div style=""><a href="#"  data-toggle="tooltip" title="Click to Download the Json of Corresponding Rule." id="btnDownloadRuleJson_' + d.ruleId + '"\' data-ruleName=\'' + escapeHtml(d.ruleName) + '\' style="margin-right: 4px;" data-ruleID=\'' + d.ruleId + '\' data-ruleName=\'' + escapeHtml(d.ruleName) + '\' onclick="downloadJsonRule(this)"><i class="fa fa-download glyphicon-ring glyphicon-green" style="font-size: 11px;padding-top: 5px;"></i></a>' +
    //                        '<span class="autosenseCheckbox" > <input type="checkbox" title="Click to Activate/Inactivate the Rule" onclick="activateRule(this)" data-ruleID=\'' + d.ruleId + '\'  id="checkboxRule_' + d.ruleId + '" name="ruleCheck" style="width: 19px;height: 23px;position: relative;top: 6px;left: 1px;" ' + isChecked + '></span></div>';

    //                }
    //                else {
    //                    action = '<div class="checkBoxRow" style="width:100px !important;padding-top: 2px;"><a href="#" class="icon-edit editWrongSign" id="ruleDeleteIcon" style="background-color:#ca402ae6;border: 1px solid #cc1204;padding-left: 5px;padding-right: 5px;padding-top: 2px;padding-bottom: 2px;margin-bottom: 0px;font-size:10px;" data-toggle="tooltip" title="Click to Delete the Rule" data-ruleID=\'' + d.ruleId + '\' data-serviceAreaID=\'' + d.serviceAreaID + '\' data-domainID=\'' + d.domainID + '\' data-technologyID=\'' + d.technologyID + '\' data-taskID=\'' + d.taskID + '\' data-subactivityID=\'' + d.subactivityID + '\' onclick="deleteRule(this)">' + '<span class="fa fa-times"  style="color:white;height: 13px;"> </span></a>' + '<a style = "margin-right: 4px;position: relative;top: 2px;margin-left: 2px;left: 7px;" href = "#" data - toggle="tooltip" title = "Click to Copy the Json of Corresponding Rule." id = "btnCopyRule" data-ruleID=\'' + d.ruleId + '\' data-copy=\'' + true + '\' data-ruleName=\'' + escapeHtml(d.ruleName) + '\' data-ruleDescription=\'' + escapeHtml(d.ruleDescription) + '\' data-serviceAreaID=\'' + d.serviceAreaID + '\' data-domainID=\'' + d.domainID + '\' data-technologyID=\'' + d.technologyID + '\' data-taskID=\'' + d.taskID + '\' data-subactivityID=\'' + d.subactivityID + '\' data-activity=\'' + d.activity + '\' data-edit=\'' + true + '\' data-active=\'' + d.active + '\' style="margin-right: 4px;" onclick="openAutoSenseDialog(this,true)"><i class="fa fa-files-o" style="padding - top: 6px;font-size: 18px;padding-left: 1px;"></i></a></div>' + '<div style=""><a href="#"  data-toggle="tooltip" title="Click to Download the Json of Corresponding Rule." id="btnDownloadRuleJson_' + d.ruleId +  '"\' data-ruleName=\'' + escapeHtml(d.ruleName) +  '\' style="margin-right: 4px;"  data-ruleID=\'' + d.ruleId + '\' onclick="downloadJsonRule(this)"><i class="fa fa-download glyphicon-ring glyphicon-green" style="font-size: 11px;padding-top: 5px;"></i></a>' +
    //                        '<span class="autosenseCheckbox" > <input type="checkbox" title="Click to Activate/Inactivate the Rule" onclick="activateRule(this)" data-ruleID=\'' + d.ruleId + '\'  id="checkboxRule_' + d.ruleId + '" name="ruleCheck" style="width: 19px;height: 23px;position: relative;top: 6px;left: 1px;" ' + isChecked + '></span></div>';
    //                }
    //            }
    //            else if (d.createdBy.toLowerCase() != signumGlobal.toLowerCase()) {
    //                action = '<div style=""><a href="#"  data-toggle="tooltip" title="Click to Download the Json of Corresponding Rule." id="btnDownloadRuleJson_' + d.ruleId + '"\' data-ruleName=\'' + escapeHtml(d.ruleName) + '\' style="margin-right: 4px;"  data-ruleID=\'' + d.ruleId + '\' onclick="downloadJsonRule(this)"><i class="fa fa-download glyphicon-ring glyphicon-green" style="font-size: 11px;padding-top: 5px;"></i></a>' + '<a style = "margin-right: 4px;position: relative;top: 2px;margin-left: 2px;" href = "#" data - toggle="tooltip" title = "Click to Copy the Json of Corresponding Rule." id = "btnCopyRule" data-ruleID=\'' + d.ruleId + '\' data-copy=\'' + true + '\' data-ruleName=\'' + escapeHtml(d.ruleName) + '\' data-ruleDescription=\'' + escapeHtml(d.ruleDescription) + '\' data-serviceAreaID=\'' + d.serviceAreaID + '\' data-domainID=\'' + d.domainID + '\' data-technologyID=\'' + d.technologyID + '\' data-taskID=\'' + d.taskID + '\' data-subactivityID=\'' + d.subactivityID + '\' data-activity=\'' + d.activity + '\' data-edit=\'' + true + '\' data-active=\'' + d.active + '\' style="margin-right: 4px;" onclick="openAutoSenseDialog(this,true)"><i class="fa fa-files-o" style="padding-top: 6px;font-size: 18px;padding-left: 1px;"></i></a></div>';
    //            }

    //            d.action = action;
    //        })


            $("#addAutoSenseRuleTable").append($('<tfoot><tr><th></th><th></th><th></th><th></th><th></th><th></th><th></th><th></th><th></th><th></th></tr></tfoot>'));

    oaddAutoSenseRuleTable = $('#addAutoSenseRuleTable').DataTable({
               //  "bSort": false,
                //"pagingType": "full_numbers",
                "processing": true,
                "serverSide": true,
                searching: true,
                responsive: true,
                "pageLength": 10,
                "order": [[1, "none"]],
               // "data": data.responseData,
                "bLengthChange": false,
                dom: 'Bfrtip',
                buttons: [
                    'colvis',
                    {
                        extend: 'excelHtml5',
                        titleAttr: 'Excel',
                        attr: {

                            id: 'btnDownloadRules'
                        },
                        action: function (e, dt, node, config) {
                            downloadRuleFiles();
                        }
                    }
                ],
                "ajax": {
                    "headers": commonHeadersforAllAjaxCall,
                    "url": serviceUrl,
                    "type": "POST",
                    "dataSrc": "data"
                },
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
                    {
                        "title": "Actions", "orderable": false, "searchable": false,
                        "render": function (data, type, row, meta) {
                            //data = data.responseData;
                         //  console.log(data);
                            let action;
                            var isChecked = '';
                            if (row.active == true) {
                                isChecked = 'checked';
                            }

                            if (row.createdBy.toLowerCase() == signumGlobal.toLowerCase()) {
                                if (row.ruleStatus == null) {
                                    action = '<div class="checkBoxRow" style="width:100px !important;padding-top: 2px;"><a href="#" class="icon-edit editWrongSign" id="ruleDeleteIcon" style="background-color:#ca402ae6;border: 1px solid #cc1204;padding-left: 5px;padding-right: 5px;padding-top: 2px;padding-bottom: 2px;margin-bottom: 0px;font-size:10px;" data-toggle="tooltip" title="Click to Delete the Rule" data-ruleID=\'' + row.ruleId + '\' data-serviceAreaID=\'' + row.serviceAreaID + '\' data-domainID=\'' + row.domainID + '\' data-technologyID=\'' + row.technologyID + '\' data-taskID=\'' + row.taskID + '\' data-subactivityID=\'' + row.subactivityID + '\' onclick="deleteRule(this)">' + '<span class="fa fa-times"  style="color:white;height: 13px;"> </span></a>' + '<a style="margin-right: 4px;position: relative;top: 2px;margin-left: 2px;left: 7px;" href="#" data-toggle="tooltip" title="Click to Copy the Json of Corresponding Rule." id="btnCopyRule" data-ruleID=\'' + row.ruleId + '\' data-copy=\'' + true + '\' data-ruleName=\'' + escapeHtml(row.ruleName) + '\' data-ruleDescription=\'' + escapeHtml(row.ruleDescription) + '\' data-serviceAreaID=\'' + row.serviceAreaID + '\' data-domainID=\'' + row.domainID + '\' data-technologyID=\'' + row.technologyID + '\' data-taskID=\'' + row.taskID + '\' data-subactivityID=\'' + row.subactivityID + '\' data-activity=\'' + row.activity + '\' data-edit=\'' + true + '\' data-active=\'' + row.active + '\' style="margin-right: 4px;" onclick="openAutoSenseDialog(this,true)"><i class="fa fa-files-o" style="padding - top: 6px;font-size: 18px;"></i></a>'
                                        + ' <a href = "#" class="icon-edit" id = "ruleEditIcon" style = "margin-left: 10px;background - color: #256fb3;border: 1px solid #2e6da4;margin-right: 4px;padding-right: 5px;padding-left: 5px;padding-top: 3px;padding-bottom: 2px;font-size:10px;" data - toggle="tooltip" title = "Click to edit the rule." data-ruleID=\'' + row.ruleId + '\' data-ruleName=\'' + escapeHtml(row.ruleName) + '\' data-copy=\'' + false + '\' data-ruleDescription=\'' + escapeHtml(row.ruleDescription) + '\' data-serviceAreaID=\'' + row.serviceAreaID + '\' data-domainID=\'' + row.domainID + '\' data-technologyID=\'' + row.technologyID + '\' data-taskID=\'' + row.taskID + '\' data-subactivityID=\'' + row.subactivityID + '\' data-activity=\'' + row.activity + '\' data-edit=\'' + true + '\' data-active=\'' + row.active + '\'  onclick="openAutoSenseDialog(this,true)">' + '<span class="fa fa-edit" style="color:white;border-top-width: 1px;"></span></a></div>'
                                        + '<div style=""><a href="#"  data-toggle="tooltip" title="Click to Download the Json of Corresponding Rule." id="btnDownloadRuleJson_' + row.ruleId + '"\' data-ruleName=\'' + escapeHtml(row.ruleName) + '\' style="margin-right: 4px;" data-ruleID=\'' + row.ruleId + '\' data-ruleName=\'' + escapeHtml(row.ruleName) + '\' onclick="downloadJsonRule(this)"><i class="fa fa-download glyphicon-ring glyphicon-green" style="font-size: 11px;padding-top: 5px;"></i></a>' +
                                        '<span class="autosenseCheckbox" > <input type="checkbox" title="Click to Activate/Inactivate the Rule" onclick="activateRule(this)" data-ruleID=\'' + row.ruleId + '\'  id="checkboxRule_' + row.ruleId + '" name="ruleCheck" style="width: 19px;height: 23px;position: relative;top: 6px;left: 1px;" ' + isChecked + '></span></div>';
                                    return action;
                                }
                                else {
                                    action = '<div class="checkBoxRow" style="width:100px !important;padding-top: 2px;"><a href="#" class="icon-edit editWrongSign" id="ruleDeleteIcon" style="background-color:#ca402ae6;border: 1px solid #cc1204;padding-left: 5px;padding-right: 5px;padding-top: 2px;padding-bottom: 2px;margin-bottom: 0px;font-size:10px;" data-toggle="tooltip" title="Click to Delete the Rule" data-ruleID=\'' + row.ruleId + '\' data-serviceAreaID=\'' + row.serviceAreaID + '\' data-domainID=\'' + row.domainID + '\' data-technologyID=\'' + row.technologyID + '\' data-taskID=\'' + row.taskID + '\' data-subactivityID=\'' + row.subactivityID + '\' onclick="deleteRule(this)">' + '<span class="fa fa-times"  style="color:white;height: 13px;"> </span></a>' + '<a style = "margin-right: 4px;position: relative;top: 2px;margin-left: 2px;left: 7px;" href = "#" data - toggle="tooltip" title = "Click to Copy the Json of Corresponding Rule." id = "btnCopyRule" data-ruleID=\'' + row.ruleId + '\' data-copy=\'' + true + '\' data-ruleName=\'' + escapeHtml(row.ruleName) + '\' data-ruleDescription=\'' + escapeHtml(row.ruleDescription) + '\' data-serviceAreaID=\'' + row.serviceAreaID + '\' data-domainID=\'' + row.domainID + '\' data-technologyID=\'' + row.technologyID + '\' data-taskID=\'' + row.taskID + '\' data-subactivityID=\'' + row.subactivityID + '\' data-activity=\'' + row.activity + '\' data-edit=\'' + true + '\' data-active=\'' + row.active + '\' style="margin-right: 4px;" onclick="openAutoSenseDialog(this,true)"><i class="fa fa-files-o" style="padding - top: 6px;font-size: 18px;padding-left: 1px;"></i></a></div>' + '<div style=""><a href="#"  data-toggle="tooltip" title="Click to Download the Json of Corresponding Rule." id="btnDownloadRuleJson_' + row.ruleId + '"\' data-ruleName=\'' + escapeHtml(row.ruleName) + '\' style="margin-right: 4px;"  data-ruleID=\'' + row.ruleId + '\' onclick="downloadJsonRule(this)"><i class="fa fa-download glyphicon-ring glyphicon-green" style="font-size: 11px;padding-top: 5px;"></i></a>' +
                                        '<span class="autosenseCheckbox" > <input type="checkbox" title="Click to Activate/Inactivate the Rule" onclick="activateRule(this)" data-ruleID=\'' + row.ruleId + '\'  id="checkboxRule_' + row.ruleId + '" name="ruleCheck" style="width: 19px;height: 23px;position: relative;top: 6px;left: 1px;" ' + isChecked + '></span></div>';
                                    return action;
                                }
                            }
                            else if (row.createdBy.toLowerCase() != signumGlobal.toLowerCase()) {
                                action = '<div style=""><a href="#"  data-toggle="tooltip" title="Click to Download the Json of Corresponding Rule." id="btnDownloadRuleJson_' + row.ruleId + '"\' data-ruleName=\'' + escapeHtml(row.ruleName) + '\' style="margin-right: 4px;"  data-ruleID=\'' + row.ruleId + '\' onclick="downloadJsonRule(this)"><i class="fa fa-download glyphicon-ring glyphicon-green" style="font-size: 11px;padding-top: 5px;"></i></a>' + '<a style = "margin-right: 4px;position: relative;top: 2px;margin-left: 2px;" href = "#" data - toggle="tooltip" title = "Click to Copy the Json of Corresponding Rule." id = "btnCopyRule" data-ruleID=\'' + row.ruleId + '\' data-copy=\'' + true + '\' data-ruleName=\'' + escapeHtml(row.ruleName) + '\' data-ruleDescription=\'' + escapeHtml(row.ruleDescription) + '\' data-serviceAreaID=\'' + row.serviceAreaID + '\' data-domainID=\'' + row.domainID + '\' data-technologyID=\'' + row.technologyID + '\' data-taskID=\'' + row.taskID + '\' data-subactivityID=\'' + row.subactivityID + '\' data-activity=\'' + row.activity + '\' data-edit=\'' + true + '\' data-active=\'' + row.active + '\' style="margin-right: 4px;" onclick="openAutoSenseDialog(this,true)"><i class="fa fa-files-o" style="padding-top: 6px;font-size: 18px;padding-left: 1px;"></i></a></div>';
                                return action;
                            }

                         //   row.action = action;


                        },


                        "data": null
                    },
                    {
                        "title": "Rule Name", "data": "ruleName", "name": "ruleName", "orderable": false,

                        "render": function (data, type, row, meta) {                         

                            return escapeHtml(row.ruleName);
                        }
                    },
                    {
                        "title": "Rule Description", "data": "ruleDescription", "className": "ruleDescStyling", "orderable": false,

                        "render": function (data, type, row, meta) {

                            let rd = escapeHtml(row.ruleDescription);
                            let rdesc = '<span text-overflow="ellipsis" title="' + rd + '">' + rd + '</span>';

                            return rdesc;
                        }

                    },
                    { "title": "Rule Type", "data": "ruleType", "orderable": false },
                    { "title": "Service Area/Subservice Area", "data": "serviceArea", "orderable": false},
                    { "title": "Domain/Subdomain", "data": "domain", "orderable": false },
                    { "title": "Activity/Subactivity", "data": "activity", "orderable": false},
                    { "title": "Technology", "data": "technology", "orderable": false},
                    { "title": "Task", "data": "task", "orderable": false },
                    { "title": "Created By", "data": "createdBy", "orderable": false }

                ],
                initComplete: function () {

                    $('#addAutoSenseRuleTable tfoot th').each(function (i) {
                        var title = $('#addAutoSenseRuleTable thead th').eq($(this).index()).text();
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

            $('#addAutoSenseRuleTable tfoot').insertAfter($('#addAutoSenseRuleTable thead'));
   // $("#addAutoSenseRuleTable_info").hide();
    pwIsf.removeLayer();

}
//To open the Autosense dialog in all add,edit & copy modes,in copy mode both checkEdit & checkCopy is set to true
function openAutoSenseDialog(object, isEdit = false) {
    pwIsf.addLayer({ text: "Please wait..." });

    let ruleIframeBody = document.getElementById('iframeAutosenseRule').contentWindow.document.body;
    $('#autosenseRuleData').attr("data-isMigrate", false);
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
       
        
        $('#iframeAutosenseRule').attr('src', UiRootDir + '/CreateAutosenseRule/AutoSenseIndex');

        $("#createAutosenseRule").modal('show');
       
        }
    else {
        
        $('#downloadJsonIcon').css({ "pointer-events": "none", "color": "darkgrey" });
        $('#autosenseRuleData').attr("data-checkEdit", false);
        $('#iframeAutosenseRule').attr('src', UiRootDir + '/CreateAutosenseRule/AutoSenseIndex');

        $("#createAutosenseRule").modal('show');
    }

   
       
}

//For merging some Columns in datatable
function getActivityAndSubactivityColumn(data, type, dataToSet) {
    return data.activity + "/" + data.subactivity;
}
function getDomainAndSubdomainColumn(data, type, dataToSet) {
    return data.domain + "/" + data.subdomain;
}
function getServiceSubserviceArea(data, type, dataToSet) {
    return data.serviceArea + "/" + data.subServiceArea;
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



function downloadJson() {
    
    document.getElementById("iframeAutosenseRule").contentWindow.downloadObjectAsJson();
}


//API call for save,edit & copy Rule
function saveRuleJson() {
    //pwIsf.addLayer({ text: "Please wait ..." });
    //let validation = false;    

    let ruleIframeBody = document.getElementById('iframeAutosenseRule').contentWindow.document.body;
    let selectServiceArea = ruleIframeBody.querySelector('#select_serviceAreaRuleDiv .select2-selection');
    let select_domainRule = ruleIframeBody.querySelector('#select_domainRuleDiv .select2-selection');
    let select_technologyRule = ruleIframeBody.querySelector('#select_technologyRuleDiv .select2-selection');
    let select_activityRuleDiv = ruleIframeBody.querySelector('#select_activityRuleDiv .select2-selection');
    let select_taskRule = ruleIframeBody.querySelector('#select_taskRuleDiv .select2-selection');
    let input_ruleName = ruleIframeBody.querySelector('#ruleName');
    let input_ruleDesc = ruleIframeBody.querySelector('#ruleDesc');    

    selectServiceArea.style.border = '';
    select_domainRule.style.border = '';
    select_technologyRule.style.border = '';
    select_activityRuleDiv.style.border = '';
    select_taskRule.style.border = '';
    input_ruleName.style.border = '';
    input_ruleDesc.style.border = '';

    //Hide the error box.
    if (ruleIframeBody.querySelector('#wf_error_list_area')) {
        ruleIframeBody.querySelector('#wf_error_list_area').style.display = 'none'
    }
   
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

    (ruleIframeBody.querySelector('#rule_active').checked) ? isActive = true : isActive = false;

    if ((!validation.formValidation.status) || (!validation.jsonValidation.status)) { }

    else {

        var ruleJSON = JSON.stringify(ruleJson);
        var parsedRuleJson = JSON.stringify(ruleJson);
        var ruleSaveObj = new Object();
        ruleSaveObj.ruleId = ruleId;
        ruleSaveObj.ruleName = ruleName;
        ruleSaveObj.ruleDescription = description;
        ruleSaveObj.ruleType = ruleType;
        ruleSaveObj.ruleJson = ruleJSON;
        ruleSaveObj.parsedRuleJson = parsedRuleJson;
        ruleSaveObj.serviceAreaID = serviceAreaID;
        ruleSaveObj.domainID = domainID;
        ruleSaveObj.technologyID = techID;
        ruleSaveObj.subactivityID = subactID;
        ruleSaveObj.taskID = taskID;
        ruleSaveObj.active = isActive;

        if (validation.formValidation.status == true && validation.jsonValidation.status == true) {
            pwIsf.addLayer({ text: "Please wait ..." });
          //  let saveButtonTitle = window.parent.$('#saveRule').html();           
            let saveMode = window.parent.$('#saveRule').attr('data-mode');       
           // if (saveButtonTitle == "SAVE THIS RULE" || saveButtonTitle == "COPY THIS RULE") {
            if (saveMode == "save" || saveMode == "copy") {
                
                $.isf.ajax({
                    url: service_java_URL + "autoSense/saveAutoSenseRule",
                    type: "POST",
                  //  async: false,
                    data: JSON.stringify(ruleSaveObj),
                    contentType: "application/json",
                    success: function (data) {
                        if (data.isValidationFailed == false) {
                            pwIsf.removeLayer();
                            pwIsf.alert({ msg: data.formMessages[0], type: 'success', autoClose: '2' });
                            $("#saveRule").prop('disabled', true);
                            $("#saveRule").css("background", "white"); 
                            $('#downloadJsonIcon').css({ "pointer-events": "auto", "color": "" });
                            ruleIframeBody.querySelector('#ruleID').value = data.responseData;
                            getAutosenseRules();
                        }
                        else {
                            pwIsf.removeLayer();
                            pwIsf.alert({ msg: data.formErrors[0], type: 'error', autoClose: '2' });
                        }
                    },
                    error: function () {
                        pwIsf.alert({ msg: 'Error while saving rule.Please contact support.', type: 'error', autoClose: '2' });
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
                    url: service_java_URL + "autoSense/editAutoSenseRule",
                    type: "POST",
                  //  async: false,
                    data: JSON.stringify(ruleSaveObj),
                    contentType: "application/json",
                    success: function (data) {
                        if (data.isValidationFailed == false) {

                            $("#saveRule").prop('disabled', true);
                            window.parent.$('#saveRule').css("background", "white"); 
                            pwIsf.alert({ msg: data.formMessages[0], type: 'success', autoClose: 2 });
                            $('#downloadJsonIcon').css({ "pointer-events": "auto", "color": "" });
                            ruleIframeBody.querySelector('#ruleID').value = ruleId;
                            getAutosenseRules();
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
    $("#createAutosenseRule").modal('hide');
    $("#autoSenseClose").modal('hide');
    $("#saveRule").prop('disabled', true);
    window.parent.$('#saveRule').css("background", "white"); 
  
}

function closeAutosenseAlert() {
    $("#autoSenseClose").modal('hide');   
}




//close of create autosense rule popup with confirmation alert 
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
        $("#createAutosenseRule").modal('hide');        
    }
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
    return '';
}


function downloadRuleFiles()
{

    let serviceUrl = `${service_java_URL}autoSense/downloadAutoSenseRuleExcel`;

    let fileDownloadUrl;
    fileDownloadUrl = UiRootDir + "/data/GetFileFromApi?apiUrl=" + serviceUrl;
    window.location.href = fileDownloadUrl;
}