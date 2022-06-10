let file = null;
var arr = [];
var viewModeArr = [];
var json = null;
var subActivityDefID = null;
var isPreview = true;
var urlParams = new URLSearchParams(window.location.search);
var subID = urlParams.get("subId");
var projId = urlParams.get("projID");
var transitionArr = [];
var global_toggleFlag = true;
var domain_id = localStorage.getItem('domain_id');
var tech_id = localStorage.getItem('tech_id');
var allToolsLabel = true; var anchorVisibleFlag = true; var clickcount = 0;
let botJsonData = new Object();
let bOTID = urlParams.get("newBotID");
//let botName = urlParams.get("newBOTName");
let versionArray = []; let deletedStepData = ''; let is20WOExecuted = false;
let Remark = ''; let selectedNewStep = [];
let selectedOldStep = []; let isAll20WOExecuted = false;
let isSavingFlag = "false"; let newStepAddArray = [];
let assesedWFDetail = [];
let tempJsonWI = [];
let stepDataFromTableCount = 0;
let botID = '';
let resetProficiencyFlag;
var mandatoryInputID;
let arrOfJsons = {};
let WFNameArr = []; var flagArr = []; var defIdArr = [];
let updateFutureWO;
let experiencedMode = localStorage.getItem('experiencedMode');
let ftr = localStorage.getItem('ftr');
let slahours = localStorage.getItem('slaHours');
let neRequired = localStorage.getItem('neneededforexecution');
let globFlChartAddJsonArr = [];
localStorage.setItem("loeValue", "-1");

let hasRules = {
    'Assessed': false,
    'Experienced': false
}
var addedFromTable = [];
$(document).ready(function () {
    var createdBy;
    var versionNameData;
   
    pwIsf.addLayer({ text: "Please wait..." });
    getProjectSpecificTools();
    getTasks(subID);
    getViewTasks(subID);
    getRPAId(projId);
    getWorkInstruction(domain_id, tech_id);
    $('#addInstructionStep').on('hidden.bs.modal', function () {
        $(this)
            .find("input,textarea,select")
            .val('')
            .end()
            .find("input[type=checkbox], input[type=radio]")
            .prop("checked", "")
            .end();

    });

    $('#linkcollapse').click(function () {

        $('#mainDivToOverlay').toggleClass('overlayWfDetailDiv');
    });

    $.isf.ajax({
        url: service_java_URL + "activityMaster/getActivityAndSubActivityDetailsByID/" + subID + '/' + signumGlobal,
        success: function (data) {
            for (var i = 0; i < data.length; i++) {
                createdBy = data[i].createdBy;
               
            }
            pwIsf.removeLayer();
        },
        error: function (xhr, status, statusText) {
            console.log("failed to fetch data");
            pwIsf.removeLayer();
        }
    });

    $("#btnPreview").click(function (event) {
        var wfName = $('#WFName').val();
        if (wfName == '' || wfName == null) {
            
            pwIsf.alert({ msg: "Please name the work flow ", type: 'error' });
        }
        else {
            $('#previewWF').modal('show');
            var fileName = file;
            var data = new FormData();
            var clearJson = '{ "cells": [] }';
            app1.graph.fromJSON(JSON.parse(clearJson))

            data.append("file", fileName);
            pwIsf.addLayer({ text: "Please wait..." });
            if (fileName != undefined) {
                var fileArr = { "projectID ": projId, "subActivityID": subID, "createdBy": signumGlobal, "file": data };
                $.isf.ajax({
                    type: "POST",
                    enctype: 'multipart/form-data',
                    url: service_java_URL_VM + "flowchartController/uploadFileForFlowChart/" + projId + '/' + subID + '/' + signumGlobal + '/' + wfName,
                    data: data,
                    processData: false,
                    contentType: false,
                    cache: false,
                    timeout: 600000,
                    success: function (response) {
                        // $("#loading").hide();

                        if ('ErrorData' in response) {

                            var elementExistssaveJSON = document.getElementById("saveJSON");
                            if (elementExistssaveJSON != null)
                                //   $('#saveJSON').remove();
                                if ($('#addStep').show())
                                    $('#addStep').hide();

                            $('#previewWF').modal('hide');
                            $('#previewErrorDetials').modal('show');
                            $('#dataTablepreviewErrorDetials_tBody').html('');
                            var errorBean = response.ErrorData.Error;
                            for (var i = 0; i < errorBean.length; i++) {
                                //   console.log(errorBean[i].message + ',' + errorBean[i].errorCategory + ',' + errorBean[i].errorDescription + ',' + errorBean[i].details);
                                var tr = '<tr>';

                                tr += '<td>' + errorBean[i].errorDescription + '</td>';

                                tr += '<td>' + errorBean[i].details + '</td>';

                                tr += '<td>' + errorBean[i].howToFix + '</td>';

                                tr += '</tr>';

                                $('#dataTablepreviewErrorDetials').append($(tr));
                            }
                            //  pwIsf.removeLayer();

                        }
                        else {
                            subActivityDefID = response.SuccessData.Success.subActivityFlowChartDefID;
                            json = JSON.parse(response.SuccessData.Success.flowChartJSON);
                            json = convertElements(json);
                            app1.graph.fromJSON(json);
                            app1.layoutDirectedGraph();
                     
                             
                                WFNameArr = [wfName]; flagArr = [true]; defIdArr = [subActivityDefID];
                                if (subActivityDefID != 0)
                                    $('#assesedDefid').val(subActivityDefID);
                               
                          
                        }
                    },
                    error: function (e) {

                        $("#result").text(e.responseText);
                        console.log("ERROR : ", e);
                    },
                    complete: function (e) {
                        pwIsf.removeLayer();
                    }
                });
            }
            else {
                pwIsf.alert({ msg: "Please choose a file", type: 'error' });
                
            }
        }
    });

    //if any change on paper reset validation flag.
    app.graph.on('all', function () {      
    });

    $('input[type="file"]').change(function (e) {
        file = e.target.files[0];
        var filename = $('input[type=file]').val().split('\\').pop();
       
        pwIsf.alert({ msg: 'The file "' + filename + '" has been selected.', type: 'info', autoClose: 1 });

        if (file != null)
            $("#btnPreview").prop("disabled", false);
    });   

    $("#viewExsistingWorkOrderModal").on('click', function () {
        pwIsf.addLayer({ text: "Please wait..." });
        $.isf.ajax({
            url: service_java_URL + "flowchartController/getDetailsForImportExistingWF/" + projId + "/" + subID,
            success: getTable,

            error: function (xhr, status, statusText) {
                console.log("api failed");
                pwIsf.removeLayer();
                pwIsf.alert({ msg: "Failed to fetch data", type: "error" });               

            }
        });
        function getTable(data) {
            $.each(data, function (i, d) {
                //var selectedVersion = $("versiondropdown" + i).val();
                d.act = '<a href="#" class="viewWfDef" data-id="' + i + '"><span class="fa fa-arrows-alt" class="close"  data-toggle="tooltip" title="Plot Graphs" style="color:blue"></span></a>';
            });
            oTable = $('#exsistingSub').DataTable({
                searching: true,
                responsive: true,
                "pageLength": 100,
                "data": data,
                order: [1],
                buttons: [
                    'colvis', 'excel'
                ],
                "destroy": true,
                "columns": [
                    //{ "title": "Project ID", "data": "projectID" },
                    { "title": "Project Name", "data": "projectName" },
                    { "title": "Country Name", "data": "countryName" },
                    { "title": "Market Name", "data": "marketAreaName" },
                    { "title": "Customer Name", "data": "customerName" },
                    //{ "title": "Version", "data": "version" },
                    {
                        "title": "Version No", "targets": 'no-sort', "orderable": false, "searchable": false, "data": null,
                        "defaultContent": '',
                        "render": function (d, t, r) {
                            var $select = $("<select></select>", {
                                "id": "versiondropdown",
                                "value": d.wfDetails
                            });
                            $.each(d.wfDetails, function (k, v) {
                                var $option = $("<option></option>", {
                                    "text": v.WorkFlowName,
                                    "value": v.SubActivityFlowChartDefID
                                });

                                $select.append($option);
                            });
                            return $select.prop("outerHTML");
                        }
                    },

                    { "title": "Action", "data": "act" }
                ]
            });
            pwIsf.removeLayer();
        }
        $('#viewExsistingWorkOrder').modal('show');
    });

    getLocalAndGlobalUrls();
    populateLoeMeasurement();
});
//Check for Penguin and Macro Bots
function checkIfRpaIDValid(rpaID) {
    let checkVar = 0;
    if (rpaID !== null || rpaID !== "" || rpaID !== undefined) {
        $.isf.ajax({
            type: "GET",
            async: false,
            url: service_java_URL + "botStore/getBotDetailById/" + rpaID,
            success: function (data) {
                if (data !== null || data !== "{}" || data !== "" || data !== undefined) {
                    if (data.responseData.botlanguage.toLowerCase() === "penguin") {
                        checkVar = 1;
                    }
                }


            },
        });
    }
    if (checkVar === 1) {
        return false;
    }
    else {
        return true;
    }
}


function acceptWF() {
    $('#previewWF').modal('hide');

    app.graph.fromJSON(app1.graph.toJSON());   
    hasRules.Experienced = false;
    hasRules.Assessed = false;

    app.layoutDirectedGraph();

    // Upload from excel case: Reset the initial flowchart json cell array for comparison with the resulting array
    botSavingsConstants.oldJsonArr = JSON.parse(JSON.stringify(app.graph.getCells()));
}

function rejectWF() {  
    $('#previewWF').modal('hide');
}

function saveBotConfig() {

    let currentCellID = $('#currentCellID').val();
    let formJson = $('#currentCellID').data('botConfigJson');
    let botForConfig = $('#currentCellID').data('rpaid');
    let botType = $('#currentCellID').data('botType');
    let botNestId = $('#currentCellID').data('botNestId');

    let getFormJson = updateFormBuilderJsonWithExistingFormData(JSON.parse(formJson), 'botConfigForm');

    app.graph.getCell(currentCellID).attributes.botConfigJson = getFormJson;
    app.graph.getCell(currentCellID).attributes.configForBot = botForConfig;
    app.graph.getCell(currentCellID).attributes.botType = botType;
    app.graph.getCell(currentCellID).attributes.botNestId = botNestId;

    if ($('#botConfigModal').css('display') == 'block') { $('#botConfigModal').modal('hide') }
    //   $('#botConfigModal').modal('toggle');

}

function openExistingConfig(currentCellID, obj) {
    // let botConfigJson = app.graph.getCell(currentCellID).attributes.botConfigJson;
    let botConfigJson = $(obj).data('configjson');
    createFormFromFormBuilderJson('botConfigBody', JSON.stringify(botConfigJson));

    $('#botConfigModal').modal('show');
}

function openConfig(rpa_id, cellid) {

    let botConfigJson = '';
    let configForBot = app.graph.getCell(cellid).attributes.configForBot;
    $('#currentCellID').val(cellid);
    getConfigForBot(rpa_id, cellid);
    getBotDetails(rpa_id, cellid);

}

function getBotDetails(rpaid, cellid) {
    $.isf.ajax({
        type: "GET",
        async: false,
        url: service_java_URL + "botStore/getBotDetailById/" + rpaid,
        success: function (response) {
            if (response.responseData != '') {
                botConfigJson = response.responseData.json;
                if (response.responseData.referenceBotId == undefined || response.responseData.referenceBotId == null) {
                    response.responseData.referenceBotId = 0;
                }
                $('#currentCellID').data('botType', response.responseData.botlanguage);//botType
                $('#currentCellID').data('botNestId', response.responseData.referenceBotId);//botNestId
                //$('#currentCellID').data('isInputRequired', response.isInputRequired);

                app.graph.getCell(cellid).attributes.botType = response.responseData.botlanguage;
                app.graph.getCell(cellid).attributes.botNestId = response.responseData.referenceBotId;
                //app.graph.getCell(cellid).attributes.isInputRequired = response.isInputRequired;
            }
        },
    });
}

function getConfigForBot(rpa_id, cellid) {

    var serviceUrl = service_java_URL + "rpaController/getBotConfig?type=BOT&referenceId=" + rpa_id;

    if (ApiProxy == true) {
        serviceUrl = service_java_URL + "rpaController/getBotConfig?type=BOT" + encodeURIComponent("&referenceId=" + rpa_id);
    }
    $.isf.ajax({
        type: "GET",
        async: false,
        url: serviceUrl,
        success: function (response) {
            // if(response!='' || response.json != "[]" || response.json !="") {
            if (response != '') {
                botConfigJson = response.json;
                $('#currentCellID').data('botConfigJson', botConfigJson);
                $('#currentCellID').data('rpaid', rpa_id);
                //$('#currentCellID').data('botType', response.botType);//botType
                //$('#currentCellID').data('botNestId', response.refBotId);//botNestId

                //newly edited
                app.graph.getCell(cellid).attributes.botConfigJson = '';
                app.graph.getCell(cellid).attributes.configForBot = rpa_id;
                //app.graph.getCell(cellid).attributes.botType = response.botType;
                //app.graph.getCell(cellid).attributes.botNestId = response.refBotId;

                if (response.json != "[]" && response.json != "") {
                    createFormFromFormBuilderJson('botConfigBody', botConfigJson);
                    $('#botConfigModal').modal('show');
                }
                else { pwIsf.alert({ msg: "Configuration for this BOT ID is not available.", type: "info" }); }
                //newly edited done 
                //$('#configSettings').hide();
            }
            else {
                // delete app.graph.getCell(cellid).attributes.botConfigJson;
                // delete app.graph.getCell(cellid).attributes.configForBot;
                app.graph.getCell(cellid).attributes.botConfigJson = '';
                app.graph.getCell(cellid).attributes.configForBot = rpa_id;

                pwIsf.alert({ msg: "Configuration for this BOT ID is not available", type: "info" });
                //  delete cell.attributes.botConfigJson;                   
            }

        },
    });
}

function openpdf(data) {
    downloadTemplateFileWorkinstruction({ 'wiid': data });
    //var parent = $('embed#embedpdf').parent();
    //var newElement = "<embed src='"+data+"' id='embedpdf'>";
    // $('embed#embedpdf').remove();
    //  $('object#objpdf').data()
    // parent.append(newElement);
    //var parent = $('#objpdf').parent();
    //var dataURL = data+'#toolbar=0';
    //var newElement = '<object id="objpdf" width="100%" height="100%" type="application/pdf" data="'+dataURL+'">';
    //$('#objpdf').remove();
    //parent.append(newElement);
    //$('#pdfDiv').modal('show');
}

$(document).on("click", ".viewWfDef", function () {

    currentSelectBox = $(this).closest('tr').find('select')
    var selected = $(currentSelectBox).val();
    $("#viewExsistingWorkOrder").modal('toggle');

    viewExsistingFlowChart(selected, removelayer)
});


function removelayer() {
    pwIsf.removeLayer();
}

function makeIconForCell(json2) {

    let startIcon = { src: '\uf04b', fill: 'blue', tooltip: 'Start', className: 'start', event: 'changeStepType' };
    json2.cells.forEach(function (cell) {
        if (cell.type == 'ericsson.Manual' || cell.type == "Automatic") {
            cell.icons = [startIcon];
        }
    }
    );
}

function viewExsistingFlowChart(index, callback) {

    var subActDefIdForExist = index;
    var json;
    pwIsf.addLayer({ text: "Please wait ..." });
    $.isf.ajax({
        type: "get",
        async: false,
        url: service_java_URL + "flowchartController/getVersionNameCurProjId/" + subActDefIdForExist,
        success: function (resp) {

            var WfversionNamedrpDown = resp[0].workFlowName;

            $('#WFName').val(WfversionNamedrpDown);
            var WfversionName = $('#WFName').val();

            subActDefIdForExist = 0;
            var version = 0;
            json = resp[0].flowChartJSON;
            json = convertElements(removeRulesFromFlowChart(JSON.parse(json)));
            app.graph.fromJSON(json);

            // Import from Existing case: Reset the initial flowchart json cell array for comparison with the resulting array
            botSavingsConstants.oldJsonArr = JSON.parse(JSON.stringify(app.graph.getCells()));

            hasRules.Experienced = false;
            hasRules.Assessed = false;
            if (resp[0].loeMeasurementCriterionID) {
                localStorage.setItem("loeValue", resp[0].loeMeasurementCriterionID);
                $("#loe").val(resp[0].loeMeasurementCriterionID).change();
            }
            flagArr = [true, true];
            defIdArr = [$('#assesedDefid').val(), $('#experienceDefid').val()];
           
            arrOfJsons['Assessed'] = json;
        },
        complete: function () {
            callback();
        },
        error: function (xhr, status, statusText) {
            pwIsf.alert({ msg: "error", type: 'error' });
        }

    });

}

function createNew() {
    $("#WFName").val('');
    $('#saveVersionedWF').modal('toggle');
}

function updateWFAttributes() {
    let validationWorkflow = validateAndAdjustFlowchart(app);

    reloadAfterUpdate = true;
   
    if ((validationWorkflow.formValidation.status) && (validationWorkflow.WfValidation.status)) {
       
        btnSaveWorkFlow(projId, subID, version, 'updateexisting', false);

    }
  

}

function savejson() {
    
    let validationWorkflow = validateAndAdjustFlowchart(app);
   
    if (version != '0') { // Edit EWF+      
       
        var iWfLevelArrLength = 0;
        iWfLevelArrLength = $(".iRow").length;
        var goAh = false;
        $(".iRow").each(function () {
            var isDel = $(this).find("td:last").html();
            if (isDel == "false") {
                goAh = true;
            }
        });

        reloadAfterUpdate = true;

        var wf_name = $("#WFName").val();
        if ((validationWorkflow.formValidation.status) && (validationWorkflow.WfValidation.status)) {

            let newJsonArr = JSON.parse(JSON.stringify(app.graph.getCells())); //Final updated flowchart json cell array

            //Check if 2 bots restriction validation is checked
            if (isTwoBotsValidated(botSavingsConstants.oldJsonArr, newJsonArr)) {

                var btnName = "";
                pwIsf.confirm({
                    title: 'Save Changes', msg:
                        `<div id="wiPopupDiv" style="display:block"><label style="text-align: left;display: block;top: 20px;position: relative;">WI URL :</label><br><label id="pMsg" style="display:block;text-align: left">Do you want to add/edit the WI-URL for WorkFlow?</label>
                            <br>
                        <button type="button" onclick="checkOwner(); insideClicked();"  id="btnAddWIURLInside" title="add/edit work instructions" class="btn btn-danger">Add WI-URL</button>
                        <button type="button" class= "btn" id="keepPrevious" onclick="setKPState()" title="Keep previous work instruction" style="color:#fff;background-color: #428bca;">Keep Previous</button>
                        <button type="button" class="btn btn-danger" title="Update work instruction later" id="later" onclick="setLaterState();" >Later</button>
                        <br><hr></div><input type="checkbox" id="futureWoUpdateCheckBox"> Update future work orders<br/>` +
                        `Do you want to overwrite the flowchart?`,
                    'buttons': {
                        'Yes': {
                            'action': function () {
                                goAh = false;
                                if (signumGlobal.toLowerCase() == wfOwnerName.toLowerCase()) {
                                    $(".iRow").each(function () {
                                        var isDel = $(this).find("td:last").html();
                                        if (isDel == "false") {
                                            goAh = true;
                                        }
                                    });

                                    iWfLevelArrLength = $(".iRow").length;
                                    if (!(iWfLevelArrLength > 0) || goAh != true) {
                                        if (savingState == "noChange" || savingState == "later") {
                                            let isFutureWorkOrdersUpdate = $('#futureWoUpdateCheckBox').is(':checked');
                                            btnSaveWorkFlow(projId, subID, version, "updateexisting", false, isFutureWorkOrdersUpdate);
                                        }
                                        else {
                                            pwIsf.alert({ msg: 'Atleast one WorkFlow Instruction URL required.', type: 'error' });
                                        }


                                    }

                                    else {
                                        let isFutureWorkOrdersUpdate = $('#futureWoUpdateCheckBox').is(':checked');
                                        btnSaveWorkFlow(projId, subID, version, "updateexisting", false, isFutureWorkOrdersUpdate);
                                    }
                                }
                                else {
                                    let isFutureWorkOrdersUpdate = $('#futureWoUpdateCheckBox').is(':checked');
                                    btnSaveWorkFlow(projId, subID, version, "updateexisting", false, isFutureWorkOrdersUpdate);
                                }
                                updateFutureWO = $('#futureWoUpdateCheckBox').is(':checked');

                            },
                            'class': 'enableDisable btn btn-success'
                        },
                        'No': {
                            'action': function () {
                                //updateFutureWO = false;
                            },
                            'class': 'btn btn-danger'
                        },

                    }
                });


                let elems = $(".iHide");

                $(".enableDisable").css({ 'background-color': 'grey', 'pointer-events': 'none' });
                if ($(".iRow").length > 0) {
                    if (signumGlobal.toLowerCase() != wfOwnerName.toLowerCase()) {
                        $("#btnAddWIURLInside").prop("disabled", false);
                        $("#keepPrevious").prop('disabled', true);
                        $("#later").prop('disabled', true);
                        $(".enableDisable").css({ 'background-color': '', 'pointer-events': '' });
                    }

                    $("#btnAddWIURLInside").html('Edit WI-URL');
                    $("#pMsg").html('Do you want to edit the WI-URL for WorkFlow?')
                    if ($('#btnAddWIURLInside').hasClass('btn-danger')) {
                        //$('#btnAddWIURLInside').removeClass('btn-danger');
                        //$('#btnAddWIURLInside').addClass('WIBackColor');

                    }

                    if ($(".iRow:visible").length == 0) {
                        $("#btnAddWIURLInside").html($("#instructionBtn").html());
                        $("#btnAddWIURLInside").addClass("btn-danger");
                        $("#pMsg").html('Do you want to add the WI-URL for WorkFlow?')
                    }
                }
                else {
                    if (signumGlobal.toLowerCase() != wfOwnerName.toLowerCase()) {
                        $("#btnAddWIURLInside").prop("disabled", true);
                        $("#keepPrevious").prop('disabled', true);
                        $("#later").prop('disabled', true);
                        $(".enableDisable").css({ 'background-color': '', 'pointer-events': '' });
                    }


                    $("#btnAddWIURLInside").html("Add WI-URL");
                    $("#pMsg").html('Do you want to add the WI-URL for WorkFlow?')
                    if ($('#btnAddWIURLInside').hasClass('btn-danger') == false) {
                        $('#btnAddWIURLInside').removeClass('WIBackColor');
                        $('#btnAddWIURLInside').addClass('btn-danger');
                    }
                }

                // $("#btnAddWIURLInside").addClass("btn-danger");
                //$("#keepPrevious").prop('disabled', false);
                //$("#later").prop('disabled', false);
                // $(".enableDisable").css({ 'background-color': 'darkgrey', 'pointer-events': 'none' });



                for (var i = 0; i < elems.length; i++) {
                    //check if edited from outside
                    if ((elems[i].innerText == "true" || elems[i].innerText == "0") && clickedLocation == "outside") {
                        $("#wiPopupDiv").css({ 'display': 'none' });
                        $("#btnAddWIURLInside").innerText = "Edit WI-URL";
                        $("#btnAddWIURLInside").removeClass("btn-danger");
                        $("#keepPrevious").prop('disabled', true);
                        $("#later").prop('disabled', true);
                        $(".enableDisable").css({ 'background-color': '', 'pointer-events': '' });
                        break;
                    }
                    else if ((elems[i].innerText == "true" || elems[i].innerText == "0") && clickedLocation == "") {
                        $("#wiPopupDiv").css({ 'display': 'block' });
                        $("#btnAddWIURLInside").innerText = "Edit WI-URL";
                        $("#btnAddWIURLInside").removeClass("btn-danger");
                        $(".enableDisable").css({ 'background-color': '', 'pointer-events': '' });
                    }

                }
            }
        }
       

    }
    else { //Add WF+       

        //var iWfLevelArrLength = 0;
        //iWfLevelArrLength = $(".iRow").length;
        //if (signumGlobal.toLowerCase() == wfOwnerName.toLowerCase()) {
        //    if (!(iWfLevelArrLength > 0)) {
        //        pwIsf.alert({ msg: 'Atleast one work instruction required.', type: 'error' });
        //        return true;
        //    }
        //}
        reloadAfterUpdater = false;
       
        if ((validationWorkflow.formValidation.status) && (validationWorkflow.WfValidation.status)) {

            let newJsonArr = JSON.parse(JSON.stringify(app.graph.getCells())); //Final updated flowchart json cell array

            if (isTwoBotsValidated(botSavingsConstants.oldJsonArr, newJsonArr)) {
                btnSaveWorkFlow(projId, subID, version, "createnew");
            }
        }       
    }

    if ($(".iRow:visible").length == 0) {
        $("#btnAddWIURLInside").html($("#instructionBtn").html());
        $("#btnAddWIURLInside").addClass("btn-danger");
        $("#pMsg").html('Do you want to add the WI-URL for WorkFlow?')
    }

    if (oldWIData.length == 0) {
        $("#later").prop('disabled', true);
        $("#keepPrevious").prop('disabled', true);
    }
}

function setKPState() {
    savingState = "noChange";
    $(".enableDisable").css({ 'background-color': '', 'pointer-events': '' });

    $("#btnAddWIURLInside").prop('disabled', true);
    $("#later").prop('disabled', true);
}

function setLaterState() {
    savingState = "later";
    $(".enableDisable").css({ 'background-color': '', 'pointer-events': '' });
    $("#btnAddWIURLInside").prop('disabled', true);
    $("#keepPrevious").prop('disabled', true);
}

//Function to remove rules from flowchart json
function removeRulesFromFlowChart(targetJson) {
    let finalJson = targetJson;
    let ruleObj = new Object();

    ruleObj.ruleId = '';
    ruleObj.ruleName = '';
    ruleObj.ruleDescription = '';

    for (let i in finalJson.cells) {
        if (finalJson.cells[i].type == 'ericsson.Manual') {

            if (finalJson.cells[i].startRule == true) {

                finalJson.cells[i].startRule = false;
                finalJson.cells[i].startRuleObj = ruleObj;

            }
            if (finalJson.cells[i].stopRule == true) {

                finalJson.cells[i].stopRule = false;
                finalJson.cells[i].stopRuleObj = ruleObj;

            }
            if (!($('.multiModeCheck').is(':checked')) && finalJson.cells[i].viewMode == 'Experienced') {
                finalJson.cells[i].viewMode = 'Assessed';
            }
        }
    }

    return finalJson;
}

//Function to remove rules all rules array
function removeRulesFromAllRulesArray(targetJson) {
    let finalJson = targetJson;
    let ruleObj = new Object();

    ruleObj.ruleId = '';
    ruleObj.ruleName = '';
    ruleObj.ruleDescription = '';

    for (let i in finalJson.cells) {
        if (finalJson.cells[i].type == 'ericsson.Manual') {

            if (finalJson.cells[i].startRule == true) {

                var removeIndex = stepIDStepNameTaskId_Array.map(function (combo) { return combo.cellIDruleType }).indexOf(finalJson.cells[i].id + '_START');
                stepIDStepNameTaskId_Array.splice(removeIndex, 1);

            }
            if (finalJson.cells[i].stopRule == true) {

                var removeIndex = stepIDStepNameTaskId_Array.map(function (combo) { return combo.cellIDruleType }).indexOf(finalJson.cells[i].id + '_STOP');
                stepIDStepNameTaskId_Array.splice(removeIndex, 1);

            }
        }
    }
}


    
//******************** Start:R13.1 Novice/Qualified to Assesed/Experience **********************//
//Get list of View Modes.
function getListViewMode() {

    $.isf.ajax({

        url: service_java_URL + "flowchartController/getListOfViewMode",
        success: function (data) {
            pwIsf.removeLayer();
            if (data.isValidationFailed == false) {
                $.each(data.responseData, function (i, d) {
                    viewModeArr.push(d);

                })
            }
            else {
                pwIsf.alert({ msg: data.formErrors[0], type: "error" });
            }
        },
        error: function (xhr, status, statusText) {

            console.log('An error occurred on getListOfViewMode: ' + xhr.error);
        },
        complete: function (xhr, status, statusText) {
            pwIsf.removeLayer();

        }
    })
}

//Open the Experienced Preview.
function openExperienceViewDialog() {
    pwIsf.addLayer({ text: "Please wait..." });
    let currJsonWF = app.graph.toJSON();

    $("#experiencedPreviewModal").modal('show');

    setTimeout(function () { experiencedView(currJsonWF) }, 1000);
    pwIsf.removeLayer();
}

//Show Experienced WF Preview.
function experiencedView(currJsonWF) {

    //clear Json 
    let clearJson = '{ "cells": [] }';
    app2.graph.fromJSON(JSON.parse(clearJson))

    //Filter the Manual>Assessed Steps. 
    let tempJson = currJsonWF.cells.filter(function (el) {
        return (el.type == 'ericsson.Manual' && el.viewMode.toLowerCase() == 'assessed')
    });

    tempJson.forEach(function (el) {
        el.attrs.header.stroke = el.attrs.header.fill = "#9D9D9D";
        el.attrs.footer.stroke = el.attrs.footer.fill = "#9D9D9D";
        el.attrs.body.stroke = "#9D9D9D";
    });

    //Replace the color changed steps in main json and plot on graph.
    let finalJson = currJsonWF.cells.map(obj => tempJson.find(o => o.id === obj.id) || obj);


    app2.graph.fromJSON({ "cells": finalJson });

}
//Get Forward and Reverse WF Transition
function getfowardReverseFWTransition() {
    $('#transitionFwd')
        .find('option')
        .remove();
    $("#transitionFwd").select("val", "");
    $('#transitionBck')
        .find('option')
        .remove();
    $("#transitionBck").select("val", "");
    pwIsf.addLayer({ text: "Please wait ..." });
    if (transitionArr.length == 0) {
       
        $.isf.ajax({

            url: service_java_URL + "flowchartController/getForwardReverseWFTransition",
            success: function (data) {
                transitionArr.push(data);
                pwIsf.removeLayer();
                if (data.responseData != null && data.isValidationFailed == false) {
                    $.each(data.responseData, function (i, d) {
                        if (d.Flag === "Forward") {
                            $('#transitionFwd').append('<option value="' + d.TransitionID + '">' + d.Transition + '</option>');

                        }
                        else {
                            $('#transitionBck').append('<option value="' + d.TransitionID + '">' + d.Transition + '</option>');
                        }

                    })
                    $('#transitionFwd option[value="21>>22"]').attr("selected", true);
                    $('#transitionBck option[value="22>>21"]').attr("selected", true);
                    if (version == 0) {
                        getListOfFwdKPI();
                        getListOfBckKPI();
                    }
                    else {
                        getListOfKPIForEdit();

                    }
                }
                else {
                    pwIsf.alert({ msg: data.formErrors[0], type: "error" });
                }
            },
            error: function (xhr, status, statusText) {

                console.log('An error occurred on getForwardReverseWFTransition: ' + xhr.error);
            },
            complete: function (xhr, status, statusText) {
                pwIsf.removeLayer();

            }
        })
    }
    else {
        pwIsf.removeLayer();
        $.each(transitionArr[0].responseData, function (i, d) {
            if (d.Flag === "Forward") {
                $('#transitionFwd').append('<option value="' + d.TransitionID + '">' + d.Transition + '</option>');

            }
            else {
                $('#transitionBck').append('<option value="' + d.TransitionID + '">' + d.Transition + '</option>');
            }

        })
        $('#transitionFwd option[value="21>>22"]').attr("selected", true);
        $('#transitionBck option[value="22>>21"]').attr("selected", true);
        if (version == 0) {
            getListOfFwdKPI();
            getListOfBckKPI();
        }
        else {
            getListOfKPIForEdit();

        }
    }
}
//To create Html for Forward and Reverse KPI List
function htmlforKPI(data){
    var html = ``;
    if (data.responseData != null && data.isValidationFailed == false) {

        $.each(data.responseData, function (i, d) {
          
            let kpiInputID = 'kpiInput' + d.kpiID;
            if (d.mandatory == false && (d.kpiDefaultValue == 0 || d.kpiDefaultValue == null) && d.kpiName != "Consecutive WO Rejected") {
                let tooltipData = d.kpiDescription + '<br/>Default Value: -<br/>Range:' + d.kpiMinValue + '-' + d.kpiMaxValue;
                html += `<div class="col-md-3 input-group" id="divKPIList${i}" style="padding-left:51px;">
                                <span id="basic-addon3" style="padding-right:171px;" >${d.kpiName}:<i class="circle fa fa-info-circle protip" style="color:blue"data-toggle="tooltip" data-pt-scheme="aqua" data-pt-position="bottom" data-pt-skin="square" data-pt-width="400" data-pt-size="small" data-pt-title="${tooltipData}"></i></span>`;
                if (d.kpiDataType.toLowerCase() == "number" || d.kpiDataType.toLowerCase() == "integer") {
                    html += ` <input type="number" class="form-control KpiInput formInput" id="${kpiInputID}" aria-describedby="basic-addon3" data-kpiId ="${d.kpiID}" min="${d.kpiMinValue}" max="${d.kpiMaxValue}" placeholder="Range:${d.kpiMinValue}-${d.kpiMaxValue}" style="width:266px;" data-minValue="${d.kpiMinValue}" data-maxValue="${d.kpiMaxValue}" onkeyup="NumberOnly(this)"  />`;
                }
                else if (d.kpiDataType.toLowerCase() == "text" || d.kpiDataType.toLowerCase() == "string") {
                    html += ` <input type="text" class="form-control KpiInput formInput" id="${kpiInputID}" aria-describedby="basic-addon3" data-kpiId ="${d.KpiID}"  placeholder="Range:${d.KpiMinValue}-${d.KpiMaxValue}" style="width:266px;" />`;
                }
                html += ` </div>`;
            }
            else if ((d.kpiDefaultValue != null || d.kpiDefaultValue != 0) && d.isMandatory != false && d.kpiName != "Consecutive WO Rejected") {
                let tooltipData = d.kpiDescription + '<br/>Default Value:' + d.kpiDefaultValue + '<br/>Range:' + d.kpiMinValue + '-' + d.kpiMaxValue;
                mandatoryInputID = kpiInputID;
                $('#mandatoryInputFlag').val(mandatoryInputID);
                html += `<div class="col-md-3 input-group" id="divKPIList${i}" style="padding-left:51px;">
                                <span id="basic-addon3" style="padding-right:171px;" >${d.kpiName}:<a class="text-danger">*</a><i class="circle fa fa-info-circle protip" style="color:blue"data-toggle="tooltip" data-pt-scheme="aqua" data-pt-position="bottom" data-pt-skin="square" data-pt-width="400" data-pt-size="small" data-pt-title="${tooltipData}"></i></span>`;
                if (d.kpiDataType.toLowerCase() == "number" || d.kpiDataType.toLowerCase() == "integer") {
                    html += `<input type = "number" class="form-control KpiInput formInput" id = "${kpiInputID}" aria - describedby="basic-addon3" data-kpiId ="${d.kpiID}" min = "${d.kpiMinValue}" max = "${d.kpiMaxValue}" placeholder = "Range:${d.kpiMinValue}-${d.kpiMaxValue}" style = "width:266px;" value ="${d.kpiDefaultValue}" data-minValue="${d.kpiMinValue}" data-maxValue="${d.kpiMaxValue}" onchange="checkValue(this)" onkeyup="NumberOnly(this)"  /> `;
                }
               
                else if (d.kpiDataType.toLowerCase() == "text" || d.kpiDataType.toLowerCase() == "string") {
                    html += `<input type = "text" class="form-control KpiInput formInput" id = "${kpiInputID}" aria - describedby="basic-addon3" data-kpiId ="${d.kpiID}" placeholder = "Range:${d.KpiMinValue}-${d.KpiMaxValue}" style = "width:266px;"  /> `;
                }
                html += ` </div >`;

            }

            else if ((d.kpiDefaultValue != null || d.kpiDefaultValue != 0) && d.kpiName == "Consecutive WO Rejected") {
                let tooltipData = d.kpiDescription + '<br/>Default Value: -<br/>Range:' + d.kpiMinValue + '-' + d.kpiMaxValue;
                html += `<div class="col-md-3 input-group" id="divKPIList${i}" style="padding-left:51px;">
                                <span id="basic-addon3" style="padding-right:115px;" >${d.kpiName}:<i class="circle fa fa-info-circle protip" style="color:blue"data-toggle="tooltip" data-pt-scheme="aqua" data-pt-position="bottom" data-pt-skin="square" data-pt-width="400" data-pt-size="small" data-pt-title="${tooltipData}"></i></span>`;
                if (d.kpiDataType.toLowerCase() == "number" || d.kpiDataType.toLowerCase() == "integer") {
                    html += `<input type = "number" class="form-control KpiInput formInput" id ="${kpiInputID}" aria - describedby="basic-addon3" data-kpiId ="${d.kpiID}" min = "${d.kpiMinValue}" max = "${d.kpiMaxValue}" placeholder = "Range:${d.kpiMinValue}-${d.kpiMaxValue}" style = "width:266px;" data-minValue="${d.kpiMinValue}" data-maxValue="${d.kpiMaxValue}" onkeyup="NumberOnly(this)" onchange="checkValue(this)" /> `;
                }
                else if (d.kpiDataType.toLowerCase() == "text" || d.kpiDataType.toLowerCase() == "string") {
                    html += `<input type = "text" class="form-control KpiInput formInput" id = "${kpiInputID}" aria - describedby="basic-addon3"  data-kpiId ="${d.kpiID}" placeholder = "Range:${d.kpiMinValue}-${d.kpiMaxValue}" style = "width:266px;"  /> `;
                }
                html += ` </div >`;
            }
            else {
                let tooltipData = d.kpiDescription + '<br/>Default Value: -<br/>Range:' + d.kpiMinValue + '-' + d.kpiMaxValue;
                html += `<div class="col-md-3 input-group" id="divKPIList${i}" style="padding-left:51px;">
                                <span id="basic-addon3" style="padding-right:171px;" >${d.kpiName}:<i class="circle fa fa-info-circle protip" style="color:blue"data-toggle="tooltip" data-pt-scheme="aqua" data-pt-position="bottom" data-pt-skin="square" data-pt-width="400" data-pt-size="small" data-pt-title="${tooltipData}"></i></span>`;
                if (d.kpiDataType.toLowerCase() == "number" || d.kpiDataType.toLowerCase() == "integer") {
                    html += `<input type = "number" class="form-control KpiInput formInput" id ="${kpiInputID}" aria - describedby="basic-addon3" data-kpiId ="${d.kpiID}" min = "${d.kpiMinValue}" max = "${d.kpiMaxValue}" placeholder = "Range:${d.kpiMinValue}-${d.kpiMaxValue}" style = "width:266px;" data-minValue="${d.kpiMinValue}" data-maxValue="${d.kpiMaxValue}" onkeyup="NumberOnly(this)" onchange="checkValue(this)" /> `;
                }
                else if (d.kpiDataType.toLowerCase() == "text" || d.kpiDataType.toLowerCase() == "string") {
                    html += `<input type = "text" class="form-control KpiInput formInput" id = "${kpiInputID}" aria - describedby="basic-addon3"  data-kpiId ="${d.kpiID}" placeholder = "Range:${d.kpiMinValue}-${d.kpiMaxValue}" style = "width:266px;"  /> `;
                }
                html += ` </div >`;
            }
        });
       
    }
    return html;
}

// to create the html for edit functionality
function createEditKPIListHTML(d,i) {
    var html =``;
    let kpiInputID = 'kpiInput' + d.kpiID;
    if (d.kpiValue != null) {
        if (d.mandatory == false && d.kpiName != "Consecutive WO Rejected") {
            let tooltipData = d.kpiDescription + '<br/>Default Value: -<br/>Range:' + d.kpiMinValue + '-' + d.kpiMaxValue;
            html += `<div class="col-md-3 input-group" id="divKPIList${i}" style="padding-top:0px;padding-left:51px;">
                                <span id="basic-addon3" style="padding-right:171px;" >${d.kpiName}:<i class="circle fa fa-info-circle protip" style="color:blue"data-toggle="tooltip" data-pt-scheme="aqua" data-pt-position="bottom" data-pt-skin="square" data-pt-width="400" data-pt-size="small" data-pt-title="${tooltipData}"></i></span>`;
            if (d.kpiDataType.toLowerCase() == "number" || d.kpiDataType.toLowerCase() == "integer") {
                html += ` <input type="number" class="form-control KpiInput formInput" id="${kpiInputID}" aria-describedby="basic-addon3" data-kpiId ="${d.kpiID}" min="${d.kpiMinValue}" max="${d.kpiMaxValue}" placeholder="Range:${d.kpiMinValue}-${d.kpiMaxValue}" style="width:266px;" value ="${d.kpiValue}" data-minValue="${d.kpiMinValue}" data-maxValue="${d.kpiMaxValue}" onkeyup="NumberOnly(this)"  />`;
            }
            else if (d.kpiDataType.toLowerCase() == "text" || d.kpiDataType.toLowerCase() == "string") {
                html += ` <input type="text" class="form-control KpiInput formInput" id="${kpiInputID}" aria-describedby="basic-addon3" data-kpiId ="${d.kpiID}"  placeholder="Range:${d.kpiMinValue}-${d.kpiMaxValue}" style="width:266px;" />`;
            }
            html += ` </div>`;
        }
        else if (d.mandatory != false && d.kpiName != "Consecutive WO Rejected") {
            mandatoryInputID = kpiInputID;
            $('#mandatoryInputFlag').val(mandatoryInputID);
            let tooltipData;
            if (d.kpiDefaultValue) {
                tooltipData = d.kpiDescription + '<br/>Default Value:' + d.kpiDefaultValue + '<br/>Range:' + d.kpiMinValue + '-' + d.kpiMaxValue;
            }
            else {
                tooltipData = d.kpiDescription + '<br/>Default Value:0<br/>Range:' + d.kpiMinValue + '-' + d.kpiMaxValue;
            }
            html += `<div class="col-md-3 input-group" id="divKPIList${i}" style="padding-top:0px;padding-left:51px;">
                                <span id="basic-addon3" style="padding-right:171px;" >${d.kpiName}:<a class="text-danger">*</a><i class="circle fa fa-info-circle protip" style="color:blue"data-toggle="tooltip" data-pt-scheme="aqua" data-pt-position="bottom" data-pt-skin="square" data-pt-width="400" data-pt-size="small" data-pt-title="${tooltipData}"></i></span>`;

            if (d.kpiDataType.toLowerCase() == "number" || d.kpiDataType.toLowerCase() == "integer") {
                html += `<input type = "number" class="form-control KpiInput formInput" id = "${kpiInputID}" aria - describedby="basic-addon3" data-kpiId ="${d.kpiID}" min="${d.kpiMinValue}" max = "${d.kpiMaxValue}" placeholder = "Range:${d.kpiMinValue}-${d.kpiMaxValue}" style = "width:266px;" value ="${d.kpiValue}" data-minValue="${d.kpiMinValue}" data-maxValue="${d.kpiMaxValue}" onchange="checkValue(this)" onkeyup="NumberOnly(this)"  /> `;
            }
            else if (d.kpiDataType.toLowerCase() == "text" || d.kpiDataType.toLowerCase() == "string") {
                html += `<input type = "text" class="form-control KpiInput formInput" id = "${kpiInputID}" aria - describedby="basic-addon3" data-kpiId ="${d.kpiID}" placeholder = "Range:${d.kpiMinValue}-${d.kpiMaxValue}" style = "width:266px;"  value ="${d.kpiDefaultValue}"/> `;
            }

            html += `</div >`;

        }
        else if (d.kpiName == "Consecutive WO Rejected") {
            let tooltipData = d.kpiDescription + '<br/>Default Value: -<br/>Range:' + d.kpiMinValue + '-' + d.kpiMaxValue;
            html += `<div class="col-md-3 input-group" id="divKPIList${i}" style="padding-top:0px;padding-left:51px;">
                                <span id="basic-addon3" style="padding-right:115px;" >${d.kpiName}:<i class="circle fa fa-info-circle protip" style="color:blue"data-toggle="tooltip" data-pt-scheme="aqua" data-pt-position="bottom" data-pt-skin="square" data-pt-width="400" data-pt-size="small" data-pt-title="${tooltipData}"></i></span>`;

            if (d.kpiDataType.toLowerCase() == "number" || d.kpiDataType.toLowerCase() == "integer") {
                html += `<input type = "number" class="form-control KpiInput formInput" id = "${kpiInputID}" aria - describedby="basic-addon3" data-kpiId ="${d.kpiID}" min="${d.kpiMinValue}" max = "${d.kpiMaxValue}" placeholder = "Range:${d.kpiMinValue}-${d.kpiMaxValue}" style = "width:266px;" value ="${d.kpiValue}" data-minValue="${d.kpiMinValue}" data-maxValue="${d.kpiMaxValue}" onkeyup="NumberOnly(this)" onchange="checkValue(this)" /> `;
            }
            else if (d.kpiDataType.toLowerCase() == "text" || d.kpiDataType.toLowerCase() == "string") {
                html += `<input type = "text" class="form-control KpiInput formInput" id = "${kpiInputID}" aria - describedby="basic-addon3" data-kpiId ="${d.kpiID}" placeholder = "Range:${d.kpiMinValue}-${d.kpiMaxValue}" style = "width:266px;"  value ="${d.kpiDefaultValue}"/> `;
            }
            html += `</div >`;
        }
        else {
            let tooltipData = d.kpiDescription + '<br/>Default Value: -<br/>Range:' + d.kpiMinValue + '-' + d.kpiMaxValue;
            html += `<div class="col-md-3 input-group" id="divKPIList${i}" style="padding-top:16px;padding-left:51px;">
                                <span id="basic-addon3" style="padding-right:171px;" >${d.kpiName}:<i class="circle fa fa-info-circle protip" style="color:blue"data-toggle="tooltip" data-pt-scheme="aqua" data-pt-position="bottom" data-pt-skin="square" data-pt-width="400" data-pt-size="small" data-pt-title="${tooltipData}"></i></span>`;
            if (d.kpiDataType.toLowerCase() == "number" || d.kpiDataType.toLowerCase() == "integer") {
                html += `<input type = "number" class="form-control KpiInput formInput" id = "${kpiInputID}" aria - describedby="basic-addon3" data-kpiId ="${d.kpiID}" min = "${d.kpiMinValue}" max = "${d.kpiMaxValue}" placeholder = "Range:${d.kpiMinValue}-${d.kpiMaxValue}" style = "width:266px;" value ="${d.kpiValue}" data-minValue="${d.kpiMinValue}" data-maxValue="${d.kpiMaxValue}" onkeyup="NumberOnly(this)" onchange="checkValue(this)" /> `;
            }
            else if (d.kpiDataType.toLowerCase() == "text" || d.kpiDataType.toLowerCase() == "string") {
                html += `<input type = "text" class="form-control KpiInput formInput" id = "${kpiInputID}" aria - describedby="basic-addon3" data-kpiId ="${d.kpiID}"  placeholder = "Range:${d.kpiMinValue}-${d.kpiMaxValue}" value ="${d.kpiValue}" style = "width:266px;" /> `;
            }
            html += ` </div >`;

        }
    }
    else {
        if (d.mandatory == false && d.kpiName != "Consecutive WO Rejected") {
            let tooltipData = d.kpiDescription + '<br/>Default Value: -<br/>Range:' + d.kpiMinValue + '-' + d.kpiMaxValue;
            html += `<div class="col-md-3 input-group" id="divKPIList${i}" style="padding-top:0px;padding-left:51px;">
                                <span id="basic-addon3" style="padding-right:171px;" >${d.kpiName}:<i class="circle fa fa-info-circle protip" style="color:blue"data-toggle="tooltip" data-pt-scheme="aqua" data-pt-position="bottom" data-pt-skin="square" data-pt-width="400" data-pt-size="small" data-pt-title="${tooltipData}"></i></span>`;
            if (d.kpiDataType.toLowerCase() == "number" || d.kpiDataType.toLowerCase() == "integer") {
                html += ` <input type="number" class="form-control KpiInput formInput" id="${kpiInputID}" aria-describedby="basic-addon3" data-kpiId ="${d.kpiID}" min="${d.kpiMinValue}" max="${d.kpiMaxValue}" placeholder="Range:${d.kpiMinValue}-${d.kpiMaxValue}" style="width:266px;" value ="${d.kpiDefaultValue}" data-minValue="${d.kpiMinValue}" data-maxValue="${d.kpiMaxValue}" onkeyup="NumberOnly(this)"  />`;
            }
            else if (d.kpiDataType.toLowerCase() == "text" || d.kpiDataType.toLowerCase() == "string") {
                html += ` <input type="text" class="form-control KpiInput formInput" id="${kpiInputID}" aria-describedby="basic-addon3" data-kpiId ="${d.kpiID}"  placeholder="Range:${d.kpiMinValue}-${d.kpiMaxValue}" style="width:266px;" />`;
            }
            html += ` </div>`;
        }
        else if (d.mandatory != false && d.kpiName != "Consecutive WO Rejected") {
            let tooltipData = d.kpiDescription + '<br/>Default Value:' + d.kpiDefaultValue + '<br/>Range:' + d.kpiMinValue + '-' + d.kpiMaxValue;
            mandatoryInputID = kpiInputID;
            $('#mandatoryInputFlag').val(mandatoryInputID);

            html += `<div class="col-md-3 input-group" id="divKPIList${i}" style="padding-top:0px;padding-left:51px;">
                                <span id="basic-addon3" style="padding-right:171px;" >${d.kpiName}:<a class="text-danger">*</a><i class="circle fa fa-info-circle protip" style="color:blue"data-toggle="tooltip" data-pt-scheme="aqua" data-pt-position="bottom" data-pt-skin="square" data-pt-width="400" data-pt-size="small" data-pt-title="${tooltipData}"></i></span>`;

            if (d.kpiDataType.toLowerCase() == "number" || d.kpiDataType.toLowerCase() == "integer") {
                html += `<input type = "number" class="form-control KpiInput formInput" id = "${kpiInputID}" aria - describedby="basic-addon3" data-kpiId ="${d.kpiID}" min="${d.kpiMinValue}" max = "${d.kpiMaxValue}" placeholder = "Range:${d.kpiMinValue}-${d.kpiMaxValue}" style = "width:266px;" value ="${d.kpiDefaultValue}" data-minValue="${d.kpiMinValue}" data-maxValue="${d.kpiMaxValue}" onchange="checkValue(this)" onkeyup="NumberOnly(this)"  /> `;
            }
            else if (d.kpiDataType.toLowerCase() == "text" || d.kpiDataType.toLowerCase() == "string") {
                html += `<input type = "text" class="form-control KpiInput formInput" id = "${kpiInputID}" aria - describedby="basic-addon3" data-kpiId ="${d.kpiID}" placeholder = "Range:${d.kpiMinValue}-${d.kpiMaxValue}" style = "width:266px;"  value ="${d.kpiDefaultValue}"/> `;
            }

            html += `</div >`;

        }
        else if (d.kpiName == "Consecutive WO Rejected") {
            let tooltipData = d.kpiDescription + '<br/>Default Value: -<br/>Range:' + d.kpiMinValue + '-' + d.kpiMaxValue;
            html += `<div class="col-md-3 input-group" id="divKPIList${i}" style="padding-top:0px;padding-left:51px;">
                                <span id="basic-addon3" style="padding-right:115px;" >${d.kpiName}:<i class="circle fa fa-info-circle protip" style="color:blue"data-toggle="tooltip" data-pt-scheme="aqua" data-pt-position="bottom" data-pt-skin="square" data-pt-width="400" data-pt-size="small" data-pt-title="${tooltipData}"></i></span>`;

            if (d.kpiDataType.toLowerCase() == "number" || d.kpiDataType.toLowerCase() == "integer") {
                html += `<input type = "number" class="form-control KpiInput formInput" id = "${kpiInputID}" aria - describedby="basic-addon3" data-kpiId ="${d.kpiID}" min="${d.kpiMinValue}" max = "${d.kpiMaxValue}" placeholder = "Range:${d.kpiMinValue}-${d.kpiMaxValue}" style = "width:266px;" value ="${d.kpiDefaultValue}" data-minValue="${d.kpiMinValue}" data-maxValue="${d.kpiMaxValue}" onkeyup="NumberOnly(this)" onchange="checkValue(this)" /> `;
            }
            else if (d.kpiDataType.toLowerCase() == "text" || d.kpiDataType.toLowerCase() == "string") {
                html += `<input type = "text" class="form-control KpiInput formInput" id = "${kpiInputID}" aria - describedby="basic-addon3" data-kpiId ="${d.kpiID}" placeholder = "Range:${d.kpiMinValue}-${d.kpiMaxValue}" style = "width:266px;"  value ="${d.kpiDefaultValue}"/> `;
            }
            html += `</div >`;
        }
        else {
            let tooltipData = d.kpiDescription + '<br/>Default Value: -<br/>Range:' + d.kpiMinValue + '-' + d.kpiMaxValue;
            html += `<div class="col-md-3 input-group" id="divKPIList${i}" style="padding-top:16px;padding-left:51px;">
                                <span id="basic-addon3" style="padding-right:171px;" >${d.kpiName}:<i class="circle fa fa-info-circle protip" style="color:blue"data-toggle="tooltip" data-pt-scheme="aqua" data-pt-position="bottom" data-pt-skin="square" data-pt-width="400" data-pt-size="small" data-pt-title="${tooltipData}"></i></span>`;
            if (d.kpiDataType.toLowerCase() == "number" || d.kpiDataType.toLowerCase() == "integer") {
                html += `<input type = "number" class="form-control KpiInput formInput" id = "${kpiInputID}" aria - describedby="basic-addon3" data-kpiId ="${d.kpiID}" min = "${d.kpiMinValue}" max = "${d.kpiMaxValue}" placeholder = "Range:${d.kpiMinValue}-${d.kpiMaxValue}" style = "width:266px;" value ="${d.kpiDefaultValue}" data-minValue="${d.kpiMinValue}" data-maxValue="${d.kpiMaxValue}" onkeyup="NumberOnly(this)" onchange="checkValue(this)" /> `;
            }
            else if (d.kpiDataType.toLowerCase() == "text" || d.kpiDataType.toLowerCase() == "string") {
                html += `<input type = "text" class="form-control KpiInput formInput" id = "${kpiInputID}" aria - describedby="basic-addon3" data-kpiId ="${d.kpiID}"  placeholder = "Range:${d.kpiMinValue}-${d.kpiMaxValue}" value ="${d.kpiDefaultValue}" style = "width:266px;" /> `;
            }
            html += ` </div >`;

        }
    }
    return html;
}
// To call api to create Forward and Backward KPI List for EWF+
function getListOfKPIForEdit() {

    let fwdTransitionVal =  $("#transitionFwd option:selected").val(); 
    let fwdTarnsitionText = $("#transitionFwd option:selected").text();   
    let tempFwdTransition = fwdTarnsitionText.split('>>');
    let tempFwdTransitionID = fwdTransitionVal.split('>>');
    let source = tempFwdTransition[0];
    let target = tempFwdTransition[1];
    let sourceId = tempFwdTransitionID[0];
    let targetId = tempFwdTransitionID[1];
    let saveMode;
    let APIurl;
    let subActDefId = $("#assesedDefid").val();

    if (version == 0) {
        saveMode = "createNew";
        APIurl = "flowchartController/getListOfKPIs?proficiencyLevelSource=" + sourceId + "&saveMode=" + saveMode;
    }
    else {
        saveMode = "updateExisting";
        APIurl = "flowchartController/getListOfKPIs?saveMode=" + saveMode + "&subactivityFlowChartDefID=" + subActDefId
    }

    

    $.isf.ajax({
        type: "GET",
        url: service_java_URL + APIurl,       
        contentType: "application/json; charset=utf-8",        
        success: AjaxSuccess,
        error: function (xhr, status, statusText) {
            pwIsf.removeLayer();
        }
    });

    function AjaxSuccess(data) {
        var Fwdhtml = ``;
        var Bckhtml = ``;
        $("#listOfFwdKPIdiv").html('');
        $("#listOfKPIBckDiv").html('');
        if (data.responseData != null && data.isValidationFailed == false) {
          
            $.each(data.responseData, function (i, d) {
               
                if (d.flag == "Forward") {
                    Fwdhtml += createEditKPIListHTML(d,i);
                }
                else {
                    Bckhtml += createEditKPIListHTML(d,i);

                }
            });

            $("#listOfFwdKPIdiv").append(Fwdhtml);
            $("#listOfKPIBckDiv").append(Bckhtml);
            if ($('#kpiInput21').val() == 0 && $('#kpiInput21').val() != '') {
                $('#kpiInput24').attr('readonly', true);
                $('#kpiInput24').addClass('text-muted');
                $('#kpiInput25').attr('readonly', true);
                $('#kpiInput25').addClass('text-muted');
                $('#kpiInput24').val('');
                $('#kpiInput25').val('');
                $('#kpiInput22').attr('readonly', true);
                $('#kpiInput22').addClass('text-muted');
                $('#kpiInput23').attr('readonly', true);
                $('#kpiInput23').addClass('text-muted');
                $('#kpiInput22').val('');
                $('#kpiInput23').val('');
            }
            else {
                $('#kpiInput24').attr('readonly', false);
                $('#kpiInput24').removeClass('text-muted');
                $('#kpiInput25').attr('readonly', false);
                $('#kpiInput25').removeClass('text-muted');
                $('#kpiInput22').attr('readonly', false);
                $('#kpiInput22').removeClass('text-muted');
                $('#kpiInput23').attr('readonly', false);
                $('#kpiInput23').removeClass('text-muted');
            }
                   
        }
    }
}


// to call api for Reverse KPI List for WF+
function getListOfBckKPI() {
    //let fwdTransitionVal = $("#transitionFwd option:selected").val();
    let bckTransitionVal =  $("#transitionBck option:selected").val();
    //let fwdTarnsitionText = $("#transitionFwd option:selected").text();
    let bckTransitionText = $("#transitionBck option:selected").text();
    let tempBckTransition = bckTransitionText.split('>>');
    let tempBckTransitionID = bckTransitionVal.split('>>');
    let source = tempBckTransition[0];
    let target = tempBckTransition[1];
    let sourceId = tempBckTransitionID[0];
    let targetId = tempBckTransitionID[1];
    let saveMode;
    let APIurl;
    let subActDefId = $("#assesedDefid").val();
    if (version == 0) {
        saveMode = "createNew";
        APIurl = "flowchartController/getListOfKPIs?proficiencyLevelSource=" + sourceId + "&saveMode=" + saveMode;
        //proficiencyLevelSource = 21 & saveMode=createNew
    }
    else {
        saveMode = "updateExisting";
        APIurl = "flowchartController/getListOfKPIs?saveMode=" + saveMode + "&subactivityFlowChartDefID=" + subActDefId;
    }

   
    $.isf.ajax({
        type: "GET",
        url: service_java_URL + APIurl,
        //dataType: 'json',
        contentType: "application/json; charset=utf-8",
        //data: JsonObj,
        success: AjaxSuccess,
        error: function (xhr, status, statusText) {
            pwIsf.removeLayer();

        }
    });
    function AjaxSuccess(data) {
        let html = htmlforKPI(data);
        $("#listOfKPIBckDiv").html('');
       
            $("#listOfKPIBckDiv").append(html);
        if ($('#kpiInput21').val() == 0 && $('#kpiInput21').val() != '') {
          
            $('#kpiInput22').attr('readonly', true);
            $('#kpiInput22').addClass('text-muted');
            $('#kpiInput23').attr('readonly', true);
            $('#kpiInput23').addClass('text-muted');
            $('#kpiInput22').val('');
            $('#kpiInput23').val('');
        }
        else {
           
            $('#kpiInput22').attr('readonly', false);
            $('#kpiInput22').removeClass('text-muted');
            $('#kpiInput23').attr('readonly', false);
            $('#kpiInput23').removeClass('text-muted');
        }
    }
}
// to call api for Forward KPI List for WF+
function getListOfFwdKPI() {
    let fwdTransitionVal = $("#transitionFwd option:selected").val();
    //let bckTransitionVal =  $("#transitionBck option:selected").val();
    let fwdTarnsitionText = $("#transitionFwd option:selected").text();
    //let bckTransitionText = $("#transitionBck option:selected").text();
    let tempFwdTransition = fwdTarnsitionText.split('>>');
    let tempFwdTransitionID = fwdTransitionVal.split('>>');
    let source = tempFwdTransition[0];
    let target = tempFwdTransition[1];
    let sourceId = tempFwdTransitionID[0];
    let targetId = tempFwdTransitionID[1];
    let saveMode;
    let APIurl;
    let subActDefId = $("#assesedDefid").val();
    if (version == 0) {
        saveMode = "createNew";
        APIurl = "flowchartController/getListOfKPIs?proficiencyLevelSource=" + sourceId + "&saveMode=" + saveMode;
    }
    else {
        saveMode = "updateExisting";
        APIurl = "flowchartController/getListOfKPIs?saveMode=" + saveMode + "&subactivityFlowChartDefID=" + subActDefId
    }

    var getKPiListObj = {};
    getKPiListObj.source = source;
    getKPiListObj.target = target;
    getKPiListObj.sourceID = sourceId;
    getKPiListObj.targetID = targetId;
    var JsonObj = JSON.stringify(getKPiListObj);

    $.isf.ajax({
        type: "GET",
        url: service_java_URL + APIurl,        
        contentType: "application/json; charset=utf-8",      
        success: AjaxSuccess,
        error: function (xhr, status, statusText) {
            pwIsf.removeLayer();

        }
    });
    function AjaxSuccess(data) {
        let html = htmlforKPI(data);
        $("#listOfFwdKPIdiv").html('');
        $("#listOfFwdKPIdiv").append(html);
        if ($('#kpiInput21').val() == 0 && $('#kpiInput21').val() != '') {
            $('#kpiInput24').attr('readonly', true);
            $('#kpiInput24').addClass('text-muted');
            $('#kpiInput25').attr('readonly', true);
            $('#kpiInput25').addClass('text-muted');
            $('#kpiInput24').val('');
            $('#kpiInput25').val('');
            $('#kpiInput22').attr('readonly', true);
            $('#kpiInput22').addClass('text-muted');
            $('#kpiInput23').attr('readonly', true);
            $('#kpiInput23').addClass('text-muted');
            $('#kpiInput22').val('');
            $('#kpiInput23').val('');
        }
        else {
            $('#kpiInput24').attr('readonly', false);
            $('#kpiInput24').removeClass('text-muted');
            $('#kpiInput25').attr('readonly', false);
            $('#kpiInput25').removeClass('text-muted');
            $('#kpiInput22').attr('readonly', false);
            $('#kpiInput22').removeClass('text-muted');
            $('#kpiInput23').attr('readonly', false);
            $('#kpiInput23').removeClass('text-muted');
        }
    }
}

//Function on click of MultiMode Execution
$(document).on("change", ".multiModeCheck", function () {

    if ($('.multiModeCheck').is(':checked')) {       
        $('#experienceBtn').show();
        getfowardReverseFWTransition();
      
        $("#multiModeViewDiv").show();
        if (viewModeArr.length == 0) {
            getListViewMode();
        }
        if (version != 0) {
            $('#resetProficiencyDiv').show();
        }
        //let cellID = $('#stepIDforCurrentStep').val();
        //let cell = app.graph.getCell(cellID);
        //if (cellID != null && cellID != '' && cellID != undefined) {
        //    createInspector(cell);
        //}
        //$('#experiencedCheck').style.display = 'block';
        //$('#experiencedText').style.display = 'block';
        if ($('.experiencedCheck').length != 0) {
            let len = $('.experiencedCheck').length;
            $('.experiencedCheck')[len-1].style.display = 'block';
        }
    }
    else {
        $("#multiModeViewDiv").hide();
        $('#experienceBtn').hide();
        if (version != 0) {
            $('#resetProficiencyDiv').hide();
        }
        //let cellID = $('#stepIDforCurrentStep').val();
        //let cell = app.graph.getCell(cellID);
        //if (cellID != null && cellID != '' && cellID != undefined) {
        //    createInspector(cell);
        //}
        //$('#experiencedCheck').style.display = 'none';
        //$('#experiencedText').style.display = 'none';
        if ($('.experiencedCheck').length != 0) {
            let len = $('.experiencedCheck').length;
            $('.experiencedCheck')[len-1].style.display = 'none';
        }
    }
});

// Click event for Reset Proficiency tab
$(document).on("change", ".resetProficiencyCheck", function () {
    if ($('.resetProficiencyCheck').is(':checked')) {
        resetProficiencyFlag = true;
    }
    else {
        resetProficiencyFlag = false;
       
    }
});

//******************** END:R13.1 Novice/Qualified to Assesed/Experience **********************//

function convertElements(json, flowChartMode = '') {
    json.cells.forEach(function (cell) {
        if (cell.type == 'basic.Rect' || cell.type == 'app.ericssonStep' || cell.type == 'ericsson.Manual') {        

            cell.type = 'ericsson.Manual';
            cell.name = cell.attrs.bodyText.text.split('\n\n')[0];
            cell.tool = cell.attrs.task.toolID + '@' + cell.attrs.task.toolName;
            cell.action = cell.attrs.task.taskID + '@' + cell.attrs.task.taskName;
            cell.responsible = (cell.responsible) ? cell.responsible : "";
            delete cell.attrs.rect; delete cell.attrs.text;

        }
        if (cell.type == 'erd.WeakEntity' || cell.type == 'app.ericssonWeakEntity' || cell.type == 'ericsson.Automatic') {
            cell.type = 'ericsson.Automatic';
            //  cell.avgEstdTime = cell.attrs.task.avgEstdEffort.toString();
            cell.name = cell.attrs.bodyText.text.split('\n\n')[0];
            cell.execType = "Automatic";
            cell.tool = cell.attrs.task.toolID + '@' + cell.attrs.task.toolName;
            cell.action = cell.attrs.task.taskID + '@' + cell.attrs.task.taskName;
            let rpaName = cell.attrs.tool.RPAName.split('-');
            let rpaTemp = rpaName[0];
            let validRpaID = checkIfRpaIDValid(rpaTemp);
            if (validRpaID === false) {

                cell.rpaid = "";
                cell.attrs.tool.RPAID = "";
                cell.attrs.tool.RPAName = "";
            }
            else {
                if ($.isNumeric(rpaTemp) == true) {
                    cell.rpaid = cell.attrs.tool.RPAID + '@' + cell.attrs.tool.RPAName;
                }
                else {
                    cell.rpaid = cell.attrs.tool.RPAID + '@' + cell.attrs.tool.RPAID + '-' + cell.attrs.tool.RPAName;
                }
            }
            cell.responsible = (cell.responsible) ? cell.responsible : "";
            delete cell.attrs.rect; delete cell.attrs.text;

        }
        if (cell.type == 'erd.Relationship') {
         
        }
        if (cell.type == 'basic.Circle' && cell.attrs.text.text.toLowerCase().trim() == 'start') {
            //cell.type = 'ericsson.StartStep';
            //cell.attrs['circle'] = {'fill':'green','cx':30, 'cy':30, 'strokeWidth':0};
            //cell.attrs['root'] = {'dataTooltipPosition': "left", 'dataTooltipPositionSelector': '.joint-stencil'};
            //cell.attrs['text'] = {'x':17, 'y':35, 'text':cell.attrs.text.text,'fill':'white', 'fontFamily': 'Roboto Condensed', 'fontSize':12, 'fontWeight':'Normal', 'strokeWidth':0, }
        }
        if (cell.type == 'basic.Circle' && cell.attrs.text.text.toLowerCase().trim() != 'start') {
            //cell.type = 'ericsson.EndStep';
            //cell.attrs['circle'] = {'fill':'red','cx':30, 'cy':30, 'strokeWidth':0};
            //cell.attrs['root'] = {'dataTooltipPosition': "left", 'dataTooltipPositionSelector': '.joint-stencil'};
            //cell.attrs['text'] = {'x':17, 'y':35, 'text':cell.attrs.text.text,'fill':'white', 'fontFamily': 'Roboto Condensed', 'fontSize':12, 'fontWeight':'Normal', 'strokeWidth':0, }
        }

    });
    return json;
}


function viewFlowChartWorkOrder(projID, subId, version, wfID) {
    var proficiencyModeArr = [];
    let startStepJson = '{"cells":[{"type":"ericsson.StartStep","position":{"x":260,"y":290},"size":{"width":60,"height":60},"angle":0,"preserveAspectRatio":true,"id":"f5d73221-92e9-4bc7-8748-b903899bdeac","z":1,"attrs":{"circle":{"fill":"green","cx":30,"cy":30,"stroke-width":0},"text":{"x":17,"y":35,"text":"Start","fill":"white","font-family":"Roboto Condensed","font-weight":"Normal","font-size":15,"stroke-width":0},"root":{"dataTooltipPosition":"left","dataTooltipPositionSelector":".joint-stencil"}}}]}';


    let noFlowchartAddFoundJson = '{ "cells": [{ "size": { "width": 200, "height": 90 }, "angle": 0, "z": 1, "position": { "x": 225, "y": 230 }, "type": "basic.Rect", "attrs": { "rect": { "rx": 2, "ry": 2, "width": 50, "stroke-dasharray": "0", "stroke-width": 2, "fill": "#dcd7d7", "stroke": "#31d0c6", "height": 30 }, "text": { "font-weight": "Bold", "font-size": 11, "font-family": "Roboto Condensed", "text": "No FlowChart Available", "stroke-width": 0, "fill": "#222138" }, ".": { "data-tooltip-position-selector": ".joint-stencil", "data-tooltip-position": "left" } } }] }';
   
    pwIsf.addLayer({ text: "Please wait ..." });
    if (version == '0') {
        $("#editReasonDiv").hide();
        $("#resetProficiencyDiv").hide();
        app.graph.fromJSON(JSON.parse(startStepJson));
        deleteExistingRulesFromTempTable(version);

        if (wfOwnerName.toLowerCase() == null || wfOwnerName.toLowerCase() == "null") {
            wfOwnerName = $("#WFOwnerName").val();
        }

        if (signumGlobal.toLowerCase() != wfOwnerName.toLowerCase()) {
            $("#instructionBtn").prop('disabled', true);
        }
        else {
            $("#instructionBtn").prop('disabled', false);
        }

        // Set the initial flowchart json cell array for comparison with the resulting array
        botSavingsConstants.oldJsonArr = JSON.parse(JSON.stringify(app.graph.getCells()));

        pwIsf.removeLayer();
    }
    else {
        $("#resetProficiencyDiv").show();
        $("#editReasonDiv").show();
        $.isf.ajax({
            type: "GET",
            async: false,
            url: service_java_URL + "flowchartController/getFlowChartEditReason/",
            success: appendEditReason,
            error: function (msg) {
                console.log('An error occurred on getFlowChartEditReason');
            }
        });

        function appendEditReason(data) {
            $('#editReasonDD').empty();
            $('#editReasonDD').append('<option value="0" selected>Select One</option>');
            $.each(data, function (i, d) {
                $('#editReasonDD').append('<option value="' + d.EditReason + '">' + d.EditReason + '</option>');
            })
        }
            $.isf.ajax({
                url: service_java_URL + "flowchartController/viewFlowChartForSubActivity/" + projID + "/" + subId + "/0/" + version + '/' + experiencedMode + '/' + wfID,
                async: false,
                success: function (data) {
                    let subActivityDefIDArr = [];
                    for (let i in data) {
                        if ('Success' in data[i]) {
                            if ((data[i].Success.FlowChartJSON == null) || (data[i].Success.FlowChartJSON == '') || (data[i] == '')) {
                                if (data[i] == '') {
                                    //data[i] = 
                                    app.graph.fromJSON(noFlowchartAddFoundJson);
                                }
                                else {
                                    app.graph.fromJSON(JSON.parse(noFlowchartAddFoundJson));
                                }
                            }
                            else {
                                $('#currentWf').val(data[i].Success.WorkFlowName);
                                $('#currentWfId').val(data[i].Success.WFID);
                                getLastVersionsOfFlowChart(data[i].Success.WorkFlowName);
                                $('#hiddenWfid').val(data[i].Success.WFID);
                                if (experiencedMode == 1) {
                                    $("#multiModeCheck").prop('checked', true);
                                    $("#multiModeCheck").trigger('change');
                                }
                                else {
                                    $("#multiModeCheck").prop('checked', false)
                                    $("#multiModeCheck").trigger('change');
                                }
                                if (neRequired == 1) {
                                    $("#IsNEMandortoryCheck").prop('checked', true);
                                    $("#IsNEMandortoryCheck").trigger('change');
                                }
                                else {
                                    $("#IsNEMandortoryCheck").prop('checked', false);
                                    $("#IsNEMandortoryCheck").trigger('change');

                                }
                                $('#SLAHours').val(slahours);
                                $('#projectId').val(projID);
                                $('#ftr').val(ftr);
                                if (data[i].Success.loeMeasurementCriterionID) {
                                    localStorage.setItem("loeValue", data[i].Success.loeMeasurementCriterionID);
                                    $("#loe").val(data[i].Success.loeMeasurementCriterionID).change();
                                    
                                }
                                let changedJson = '';
                                let flowChartMode = data[i].Success.Mode;

                                changedJson = JSON.parse(data[i].Success.FlowChartJSON);
                                changedJson = convertElements(changedJson, flowChartMode)

                                let editReason = data[i].Success.WFEditReason;
                                $("#editReasonDD").val(editReason);

                                subActivityDefID = data[i].Success.SubActivityFlowChartDefID;
                                $('#oldDefID').val(subActivityDefID);
                                subActivityDefIDArr.push(subActivityDefID);

                                $("#WFName").val(data[i].Success.WorkFlowName)
                                $("#hiddenAssessedwfname").val(data[i].Success.WorkFlowName);
                                $("#hiddenExperiencedwfname").val(data[i].Success.WorkFlowName);
                                $("#assesedDefid").val(subActivityDefID);
                                $("#experiencedDefid").val(subActivityDefID);

                                arrOfJsons['Assessed'] = changedJson;
                                app.graph.fromJSON(changedJson);

                                // Set the initial flowchart json cell array for comparison with the resulting array
                                botSavingsConstants.oldJsonArr = JSON.parse(JSON.stringify(app.graph.getCells()));

                                if (data[i].Success.Mode.indexOf(',') > -1) {
                                    proficiencyModeArr = data[i].Success.Mode.split(',');
                                } else {
                                    proficiencyModeArr.push(data[i].Success.Mode);
                                }

                            }
                        }
                        getInstructionData();
                        const assessedRuleLength = arrOfJsons['Assessed'].cells.filter(function (cell) { return (cell.startRule == true || cell.stopRule == true) }).length;
                        if (assessedRuleLength > 0) {
                            $("#IsAutoSenseDisabled").prop('disabled', false);
                        }
                        deleteExistingRulesFromTempTable(version, subActivityDefIDArr);
                    }
                },
                error: function (xhr, status, statusText) {
                    const err = JSON.parse(xhr.responseText);
                    pwIsf.alert({ msg: err, type: 'error' });

                }
            });
    }
}


function btnSaveWorkFlow(projID, subID, version, saveOption, updateSameVersion = true, isFutureWorkOrdersUpdate = false) {
    let WFJsonArray = new Object();
    WFName = $('#WFName').val();
    var jsonToPass = '';
    var json = '';
    let editReason = '';
    let wfID = '';
    let WFType = '';
    if (version == '0') { //New WF+
        editReason = '';
        wfID = '';
    }

    else { //Edit EWF+
        editReason = $('#editReasonDD').val();
        wfID = $('#hiddenWfid').val();
    }


   
        let JsonStringArr = [];
        let KpiInputArr = [];
        

        let MultiModeCheck = $('#multiModeCheck').prop('checked');
        let NeMandatoryCheck = $('#IsNEMandortoryCheck').prop('checked');
        let SLAHours = $('#SLAHours').val();
        let FTRValue = $('#ftr').val();
        let IsAutoSenseEnabled = !$('#IsAutoSenseDisabled').prop('checked');
        let resetProficiency = $('#resetProficiencyCheck').prop('checked'); 

    if (!MultiModeCheck) {
        WFType = "Assessed";

    }
    else {
        WFType = "Experienced";

        $('.KpiInput').each(function (i, d) {
            KpiInputArr.push({
                kpiID: $(d).data('kpiid'),
                kPIValue: $(d).val()
            });
        });       
    }

    jsonToPass = app.graph.toJSON();
        if (jsonToPass.cells.length != 0) {           

                if (updateSameVersion == false) { flagArr = false; }
                else { flagArr = true; }

                defIdArr = $('#assesedDefid').val();              
                
                WFJsonArray.projectID = projID;
                WFJsonArray.subActivityID = subID;
                WFJsonArray.versionNumber = version;
                WFJsonArray.saveMode = saveOption;
                WFJsonArray.futureWorkOrdersUpdate = isFutureWorkOrdersUpdate;
                WFJsonArray.signumID = signumGlobal;               
                WFJsonArray.neMandatory = NeMandatoryCheck;
                WFJsonArray.enableField = IsAutoSenseEnabled;
                WFJsonArray.slaHours = SLAHours;
                WFJsonArray.ftrValue = FTRValue;
                WFJsonArray.experiencedFlow = MultiModeCheck;
                WFJsonArray.lstKpiModel = KpiInputArr;
               
                WFJsonArray.wfOwner = wfOwnerName;
                WFJsonArray.resetProficiency = resetProficiency;
                WFJsonArray.flowChartDefID = defIdArr;
                WFJsonArray.update = flagArr;
                WFJsonArray.wfName = WFName;
                WFJsonArray.wfType = WFType;
                WFJsonArray.wfEditReason = editReason;
                WFJsonArray.workFlowID = wfID;
                WFJsonArray.loeMeasurementCriterionID = $('#loe').val();
                WFJsonArray.flowchartJSON = JSON.stringify(jsonToPass);
            
                //call for saving the wf level instruction
                fetchWfLevelInstructioFromTable();
                // call for saving the step level data
                let tempArrStep;
                tempArrStep = pushStepJsonWIinArr();
                if (tempArrStep[0].length != 0) {
                    for (let t = 0; t < tempArrStep[0].length; t++) {
                        arr.push(tempArrStep[0][t]);
                    }

                }
                if (tempArrStep[1].length != 0) {
                    for (let t = 0; t < tempArrStep[1].length; t++) {
                        arr.push(tempArrStep[1][t]);
                    }

                }

                WFJsonArray.lstWFStepInstructionModel = arr;
                saveJsonUI(WFJsonArray);
                tempJsonWI = [];
                addedFromTable = [];
                stepDataFromTableCount = 0;

            


        }
        else {
            pwIsf.alert({ msg: "Flow Chart cannot be empty", type: 'error' });
        }
    
}

function validateHhMm(inputHours) {
   
    var isValid = /^(7[0-2]|[0-9]|[0-6][0-9])(\.5)?$/.test(inputHours.value);
    if (isValid) {
        inputHours.style.border = '';
    } else {
        inputHours.style.border = '2px solid red';
    }
    return isValid;
}

function saveJsonUI(WFJsonObject) {
    window.pwIsf.addLayer({ text: "Please wait ..." });
    assesedWFDetail = [];   

    $.isf.ajax({
        type: "POST",
        //  async:false,
        contentType: 'application/json',
        data: JSON.stringify(WFJsonObject),        

        url: service_java_URL + "flowchartController/saveJSONFromUI",
        success: function (data) {

            if ($('#saveVersionedWF').hasClass('in')) { $('#saveVersionedWF').modal('toggle'); }            

            if ('Success' in data) {
                file = null;

                app.graph.fromJSON(JSON.parse(data.Success.FlowChartJSON));
                    $('#hiddenAssessedwfname').val(WFName);
                    $('#assesedDefid').val(data.Success.FlowChartDefID);
                        subActivityDefID = data.Success.FlowChartDefID;
                        $("#WFName").val($("#hiddenAssessedwfname").val());            

                window.opener.searchWfAvail();
               
                assesedWFDetail.push(data.Success.FlowChartDefID);
                assesedWFDetail.push(data.Success.Version);

                $('#newDefId').val(assesedWFDetail[0]);
                $('#newVersion').val(assesedWFDetail[1]);                

                pwIsf.alert({ msg: "WORK FLOW SUCCESSFULLY SAVED", type: 'info', autoClose: 2 });
                data.Success.bOTID = botID;
                getBOTSummaryDetails(data.Success, true);
                getLastVersionsOfFlowChart(WFName);
            }
            else {

                $('#previewErrorDetials').modal('show');
                $('#dataTablepreviewErrorDetials_tBody').html('');
                var errorBean = data.ErrorData.Error;
                for (let j = 0; j < errorBean.length; j++) {

                    var tr = '<tr>';

                    tr += '<td>' + errorBean[j].errorDescription + '</td>';

                    if (errorBean[j].details == null) {
                        errorBean[j].details = 'NA';
                    }
                    tr += '<td>' + errorBean[j].details + '</td>';

                    tr += '<td>' + errorBean[j].howToFix + '</td>';

                    tr += '</tr>';

                    $('#dataTablepreviewErrorDetials').append($(tr));

                }
            }

        },
        error: function (xhr, status, statusText) {           
            pwIsf.alert({ msg: "error in api", type: 'error' });
        },
        complete: function () {
            pwIsf.removeLayer();

        }
    });
}









let deletedOldStepsData = { 'deletedSteps': [] }






function resetProficiency() {
    let wfOwnerName = $("#WFOwnerName").val();
    let subActDefId = $("#assesedDefid").val();
    let WFName = $("#WFName").val();
    let WFProficiencyObject = new Object();
    
    WFProficiencyObject.workFlowId = wfid;
    WFProficiencyObject.projectId = projId;
    WFProficiencyObject.subActivityId = subID;
    WFProficiencyObject.modifiedBy = signumGlobal;
    WFProficiencyObject.signum = wfOwnerName;
    WFProficiencyObject.subActivityFlowchartDefID = subActDefId;
    WFProficiencyObject.triggeredBy = "Machine";
    WFProficiencyObject.futureWorkOrdersUpdate = updateFutureWO;
    WFProficiencyObject.workFlowName = WFName;


    $.isf.ajax({
        type: "POST",
        contentType: 'application/json',
        data: JSON.stringify(WFProficiencyObject),
        async: false,
        url: service_java_URL + "flowchartController/resetProficiency",
        success: function (data) {
            //pwIsf.alert({ msg: "BOT not Deployed and new changes of workFlow is not saved", type: "info" })
            if (data.isValidationFailed == false) {
                pwIsf.alert({ msg: data.formMessages[0], type: "info" });
                setTimeout("self.close()", 3000);
            }
            else {
                pwIsf.alert({ msg: data.formErrors[0], type: "error" });
                setTimeout("self.close()", 5000);
            }
        },
        complete: function () {
            // $('#rpaIds').find("option").eq(0).remove();

        },
        error: function (xhr, status, statusText) {
            pwIsf.alert({ msd: "Failed to reset proficiency", type: "error" });
        }
    });
}

function deleteAllBotDetails() {
    // $('#wfVersionSelect').val($('#wfVersionSelect option:first').val());
    let botJsonDataForDelDef = new Object();
    botJsonDataForDelDef.newDefID = botJsonData.newDefID
    botJsonDataForDelDef.newVersion = botJsonData.newVersion
    botJsonDataForDelDef.oldDefID = botJsonData.oldDefID
    botJsonDataForDelDef.oldStepId = botJsonData.oldStepId
    botJsonDataForDelDef.bOTID = botJsonData.bOTID

    $.isf.ajax({
        type: "POST",
        contentType: 'application/json',
        data: JSON.stringify(botJsonDataForDelDef),
        url: service_java_URL + "flowchartController/deleteDefID",
        success: function (data) {
            pwIsf.alert({ msg: "BOT not Deployed and new changes of workFlow is not saved", type: "info" })
            reloadAfterUpdate = true;
            if (reloadAfterUpdate) { location.reload(); }
        },
        complete: function () {
            // $('#rpaIds').find("option").eq(0).remove();

        },
        error: function (xhr, status, statusText) {
            pwIsf.alert({ msd: "Failed to delete workflow ", type: "error" });
        }
    });
}



//Work Instruction List
function getWorkInstruction(domainID, techID) {
    if ((domainID != null || domainID.length != 0) && (techID != null || techID.length != 0)) {
        $.isf.ajax({

            url: service_java_URL + "activityMaster/getActiveWorkInstruction?domainID=" + domainID + "&technologyID=" + techID,
            success: function (data) {
                data = data.responseData;
                arr_workinstruction = [];
                var html = "";
                $.each(data, function (i, d) {
                    if (d !== null)
                        arr_workinstruction.push({ value: d.hyperlink + '@' + d.workInstructionName + '@' + d.description + '@' + d.wIID, content: d.workInstructionName });
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
            pwIsf.alert({ msg: "Tech ID missing", type: "error" });
        }
    }

}

//get RPAID list
function getRPAId(projectId) {
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
                    rpaid.push({ value: d.bOTName + '@' + d.automation_Plugin, content: d.automation_Plugin });
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

//get all tool list
function getAllTools() {   
    getTools();
}

//get project specific tools list.
function getOnlyProjectSpecificTools() {
    getProjectSpecificTools();
}

function getTools() {
  
    $.isf.ajax({
        url: service_java_URL + "/toolInventory/getToolInventoryDetails",
        async: false,
        success: function (result) {
            tools = [];
            var option = '';
            $('select#toolsCombo.select2-hidden-accessible').find('option').remove();
            for (var i = 0; i < result.length; i++) {
                option += '<option value="' + result[i].toolID + '">' + result[i].tool + '</option>';
                tools.push({ value: result[i].toolID + '@' + result[i].tool, content: result[i].tool });               
            }
            $('#toolName').append(option);           
        },
        complete: function () {            
        }
    });
}

function getProjectSpecificTools() {

    if (projId) {
        $.isf.ajax({
            url: service_java_URL + "/projectManagement/getProjectSpecificTools?projectID=" + projId,
            async: false,
            success: function (result) {
                result = result.responseData;
                tools = [];
                var option = '';
                $('select#toolsCombo.select2-hidden-accessible').find('option').remove();
                if (result.length != 0) {
                    for (var i = 0; i < result.length; i++) {
                        option += '<option value="' + result[i].ToolID + '">' + result[i].Tool + '</option>';
                        tools.push({ value: result[i].ToolID + '@' + result[i].Tool, content: result[i].Tool });

                    }
                }              
            },
            complete: function () {
             
            }
        });
    }
    else {
        pwIsf.alert({ msg: "Project ID missing", type: "error" });
    }

}

//Function to get bot Config
function getBotConfigJsonByApi(botId) {

    //https://isfdevservices.internal.ericsson.com:8443/isf-rest-server-java_dev_major/rpaController/getBotConfig?type=BOT&referenceId=1165

    var serviceUrl = service_java_URL + "rpaController/getBotConfig?type=BOT&referenceId=" + botId;

    if (ApiProxy == true) {
        serviceUrl = service_java_URL + "rpaController/getBotConfig?type=BOT" + encodeURIComponent("&referenceId=" + botId);
    }

    pwIsf.addLayer({ text: 'Please wait ...' });
    let getJson = new Object();
    let ajaxCall = $.isf.ajax({
        url: serviceUrl,
        async: false,
        contentType: 'application/json',
        type: 'GET',
        returnAjaxObj: true

    });

    ajaxCall.done(function (data) {
        getJson.botjson = data.json;
        getJson.botType = data.botType;
        getJson.botNestId = data.refBotId;
    });

    ajaxCall.fail(function () { pwIsf.alert({ msg: 'Getting bot config json through API.', type: 'error' }); });
    ajaxCall.always(function () { pwIsf.removeLayer(); });

    return getJson;


}

//Get All Tasks
function getTasks(subActivityID) {
    $.isf.ajax({
        url: service_java_URL + "/activityMaster/getTaskDetails/" + subActivityID + "/" + signumGlobal,
        success: function (result) {
            //tasks = result;
            //  tasks=[];
            var option = '';
            for (var i = 0; i < result.length; i++) {
                option += '<option value="' + result[i].taskID + '">' + result[i].task + '</option>';
                //    tasks.push({ value: result[i].taskID + '_' + result[i].task, content: '<span style="font-family: Alegreya Sans">' + result[i].task + '</span>' });
            }

            $('#taskName').append(option);
        }
    });
}

function getViewTasks(subActivityID) {
    $.isf.ajax({
        url: service_java_URL + "/activityMaster/viewTaskDetails/" + projId + "/" + subActivityID,
        success: function (result) {
            //tasks = result;
            tasks = [];
            var option = '';
            for (var i = 0; i < result.length; i++) {
                option += '<option value="' + result[i].taskID + '">' + result[i].masterTask + '</option>';
                tasks.push({ value: result[i].taskID + '@' + result[i].masterTask, content: result[i].masterTask });
                //  tasks.push({ value: result[i].taskID + '@' + result[i].masterTask, content: '<span style="font-family: Alegreya Sans">' + result[i].masterTask + '</span>' });
            }

            $('#taskName').append(option);
        }
    });
}

$("#btn_reset").click(function () {

    $("#step").val("");
    $("#reason").val("");
});

function isNumberKey(evt) {
    var charCode = (evt.which) ? evt.which : evt.keyCode;
    if (charCode != 46 && charCode > 31
        && (charCode < 48 || charCode > 57))
        return false;

    return true;
}

//collapse div when focus out.
$(document).on('click', '#app', function () {

    $('#accordionOne').addClass('collapse'); $('#accordionOne').removeClass('in');
})

function toggleIcon(e) {
    $(e.target)
        .prev('.panel-heading')
        .find(".more-less")
        .toggleClass('fa-plus fa-minus');
}

function flowChartOpenInNewWindow(url, windowName) {
    var width = window.innerWidth * 0.85;
    // define the height in
    var height = width * window.innerHeight / window.innerWidth;
    // Ratio the hight to the width as the user screen ratio
    window.open(url + '&commentCategory=WO_LEVEL', windowName, 'width=' + width + ', height=' + height + ', top=' + ((window.innerHeight - height) / 2) + ', left=' + ((window.innerWidth - width) / 2))
}

function checkValue(control) {
    var node = $('#' + control.id);
    if (node.val() == 0 && node.val() != '') {
        $('#kpiInput24').attr('readonly', true);
        $('#kpiInput24').addClass('text-muted');
        $('#kpiInput25').attr('readonly', true);
        $('#kpiInput25').addClass('text-muted');
        $('#kpiInput24').val('');
        $('#kpiInput25').val('');
        $('#kpiInput22').attr('readonly', true);
        $('#kpiInput22').addClass('text-muted');
        $('#kpiInput23').attr('readonly', true);
        $('#kpiInput23').addClass('text-muted');
        $('#kpiInput22').val('');
        $('#kpiInput23').val('');
    }
    else {
        $('#kpiInput24').attr('readonly', false);
        $('#kpiInput24').removeClass('text-muted');
        $('#kpiInput25').attr('readonly', false);
        $('#kpiInput25').removeClass('text-muted');
        $('#kpiInput22').attr('readonly', false);
        $('#kpiInput22').removeClass('text-muted');
        $('#kpiInput23').attr('readonly', false);
        $('#kpiInput23').removeClass('text-muted');
    }
    
    //if
}
function NumberOnly(control) {
    var node = $('#' + control.id);
    console.log(node.val());
    node.val(node.val().replace(/[^.0-9]/g, ''));
    if (node.val() % 1 != 0) {
        node.val(node.val().replace(''));
    }
    if (node.val().includes('.')) {

        node.val(node.val().replace(''));
    }
    let lowerLimit = parseInt(node[0].dataset.minvalue);
    let upperLimit = parseInt(node[0].dataset.maxvalue);
    if (parseInt(node.val()) < lowerLimit || parseInt(node.val()) > upperLimit) {
        node.val('');
    }
    if (node.val() != null || node.val() != undefined || node.val() != '') {
        node[0].style.border = '';
    }
    
}

function NumberOnlySLA(control) {
    var node = $('#' + control.id);
    node.val(node.val().replace(/[^.0-9]/g, ''));
    if ($("#SLAHours").val()) {
        document.getElementById('SLAHours').style.border = '';

    }
    else {

    }
}

function checkForInputWFName() {
    if ($("#WFName").val()) {
        document.getElementById('WFName').style.border = '';

    }
    else {

    }
}

function onFTRChange() {

    document.getElementById('ftr').style.border = '';
}

function onReasonChange() {
    document.getElementById('editReasonDD').style.border = '';

}

function highlightElement(stepid) {
    let arrAllShapes = app.graph.toJSON();

    for (let i = 0; i < arrAllShapes.cells.length; i++) {
        if (arrAllShapes.cells[i].id == stepid) {
            arrAllShapes.cells[i].attrs.body.fill = 'red';
            arrAllShapes.cells[i].attrs.bodyText.fill = 'white';
            app.graph.fromJSON(arrAllShapes)
            break;
        }
    }
}

function calculateRunOnServer(rpaid) {
    let response = '';
    $.isf.ajax({
        async: false,
        type: "GET",
        contentType: 'application/json',
        url: service_java_URL + "rpaController/getRPAIsRunOnServer/" + rpaid,
        // dataType: "json",

        success: function (data) {
            response = data;
        },
        complete: function () {
        }
    });
    return response;
    //https://isfdevservices.internal.ericsson.com:8443/isf-rest-server-java_dev_major/rpaController/getRPAIsRunOnServer/02 
}


//nahar
//Soft adding and updating Instruction to html table on click of Add /Edit BUTTON at wf level
function addInstruction(type) {
    type = type.innerText;

    var name = escapeHtml($("#instructionName").val());
    var url = escapeHtml($("#instructionURL").val().toLowerCase());

    //mandatory, valid and name exist check
    var elems = $(".iName");
    iNames = [];
    iNamesInLowerCase = [];
    for (var i = 0; i < elems.length; i++) {
        iNames.push(elems[i].innerText);
        iNamesInLowerCase.push(elems[i].innerText.toLowerCase());
    }
    //for url exist check
    var elemsUrl = $(".iURL");
    iURL = [];
    for (var i = 0; i < elemsUrl.length; i++) {
        iURL.push(escapeHtml(elemsUrl[i].innerText));
    }

    if (type == 'Edit') {
        var index1 = iNames.indexOf($(".editing").children('td').eq(2).text());
        var index11 = iNamesInLowerCase.indexOf($(".editing").children('td').eq(2).text().toLowerCase());
        iNames.splice(index1, 1);
        iNamesInLowerCase.splice(index11, 1);
        var index2 = iURL.indexOf($(".editing").children('td').eq(3).text());
        iURL.splice(index2, 1);

    }

    if (iNamesInLowerCase.indexOf(name.toLowerCase()) > -1 || iURL.indexOf(url) > -1) {


        if (iNamesInLowerCase.indexOf(name.toLowerCase()) > -1) {
            pwIsf.alert({ msg: "Instruction name already exist.", type: "error" });
        }
        else if (iURL.indexOf(url) > -1) {
            pwIsf.alert({ msg: "Instruction URL already exist.", type: "error" });
        }
        //else if () {
        //    pwIsf.alert({ msg: "Invalid URL. URL should be from global url domain.", type: "error" });
        //}


    }
    else if (!name || !url) {

        pwIsf.alert({ msg: "Please fill Mandatory fields", type: "error" });
    }

    else if (!checkValidURLInProj(unescapeHtml(url))) {
        pwIsf.alert({ msg: "Invalid URL! URL should be from Global/Local URLS", type: "error" });
    }
    else {
        //soft add instruction to table
        if (type == 'Add') {
            var action = `<a title="Edit" onclick="editInstruction(this)" class="icon-edit lsp">` + getIcon('edit') + `</a>` +
                `<a title="Delete" onclick="deleteInstruction(this)" class="icon-delete lsp">` + getIcon('delete') + `</a>`;
            var html = `<tr class="iRow" style="visibility:visible"><td class="iHide">0</td><td class="iAction">${action}</td><td class="iName">${name}</td><td class="iURL"><a target="_blank" href="${url}">${url}</a></td><td class="iHide">false</td><td class="iHide">false</td></tr> `;
            $("#wfInstructionTable").append(html);
            $("#addInstructionBtn").prop('disabled', true);

        }
        //soft update instruction in html table
        else if (type == 'Edit') {
            if ($(".editing").children('td').eq(2).text() != name || $(".editing").children('td').eq(3).text() != url) {
                $(".editing").addClass('edited');
            }
            $(".editing").children('td').eq(2).text(unescapeHtml(name));
            $(".editing").children('td').eq(3).html("<a href='" + url + "'>" + url + "</a>");
            $(".editing").children('td').eq(4).text(true);
            $("#addInstructionBtn").html('Add');
            $('#wfInstructionTable tr').removeClass("editing");
            $("#addInstructionBtn").prop('disabled', true);
        }

        $("#instructionName").val('');
        $("#instructionURL").val('');

        //disable button if row greater than 3
        if ($(".iRow:visible").length >= 3) {
            $("#addInstructionBtn").prop('disabled', true);
        }
        changeBackColorOfWIButton();
        //else {
        //    $("#addInstructionBtn").prop('disabled', false);
        //}

    }

    if ($(".iRow:visible").length > 0) {
        $("#instructionBtn").html('Edit WI-URL');        
        $("#btnAddWIURLInside").html('Edit WI-URL');
        $(".enableDisable").css({ 'background-color': '', 'pointer-events': '' });
        if ($("#wfInstrcutionUpdated").is(':checked') == false) {
            if (type == 'Add') 
            $('#instructionText').html('');
		}
    }
}

function changeBackColorOfWIButton(type=null) {
    if ($('.iRow').length > 0) {
        if ($('#instructionBtn').hasClass('btn-danger')) {
            $('#instructionBtn').removeClass('btn-danger');
            $('#instructionBtn').addClass('WIBackColor');
            $('#btnAddWIURLInside').removeClass('btn-danger');
            $('#btnAddWIURLInside').addClass('WIBackColor');
            if ($("#wfInstrcutionUpdated").is(':checked') == false)
            $('#instructionText').html('WI-URL not updated yet');

        }
    } else {
        $('#instructionBtn').html('Add WI-URL');
        if ($('#instructionBtn').hasClass('btn-danger') == false) {
            $('#instructionBtn').removeClass('WIBackColor');
            $('#instructionBtn').addClass('btn-danger');
            $('#btnAddWIURLInside').removeClass('WIBackColor');
            $('#btnAddWIURLInside').addClass('btn-danger');
           if($("#wfInstrcutionUpdated").is(':checked')==false)
            $('#instructionText').html('WI-URL not added yet');
        }
    }
}

// soft delete Instruction
function deleteInstruction(elem) {

    if (elem.parentElement.parentElement.classList.contains('editing')) {
        $("#addInstructionBtn").html('Add');
        $("#instructionName").val('');
        $("#instructionURL").val('');
    }

    if (elem.parentElement.parentElement.firstChild.innerText == "0") {
        elem.parentElement.parentElement.remove();
    }
    else {
        elem.parentElement.parentElement.lastElementChild.innerText = true;
        elem.parentElement.parentElement.style.display = 'none';
        elem.parentElement.parentElement.style.visibility = 'hidden';
    }

    if ($(".iRow:visible").length >= 3) {
        $("#addInstructionBtn").prop('disabled', true);
    }

    else if ($("#instructionName").val().length > 0 && $("#instructionURL").val().length > 0) {
        $("#addInstructionBtn").prop('disabled', false);
    }
    if ($(".iRow:visible").length == 0) {
        $("#btnAddWIURLInside").html('Add WI-URL');
        $("#instructionBtn").html('Add WI-URL');
        $("#addInstructionBtn").html('Add');
        $("#instructionName").val('');
        $("#instructionURL").val('');
        $(".enableDisable").css({ 'background-color': 'grey', 'pointer-events': 'none' });
    }
    else {
        $("#instructionBtn").html('Edit WI-URL');
    }
    changeBackColorOfWIButton();

}

//soft editing instruction  on edit icon click
function editInstruction(elem) {

    $('#wfInstructionTable tr').removeClass("editing");
    elem.parentElement.parentElement.classList.add('editing');
    $("#addInstructionBtn").html('Edit');
    $("#addInstructionBtn").prop('disabled', false);
    $("#instructionName").val(unescapeHtml(elem.parentElement.parentElement.childNodes[2].innerText));
    $("#instructionURL").val(unescapeHtml(elem.parentElement.parentElement.childNodes[3].innerText));

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

function unescapeHtml(safe) {
    return safe.replace(/&amp;/g, '&')
        .replace(/&lt;/g, '<')
        .replace(/&gt;/g, '>')
        .replace(/&quot;/g, '"')
        .replace(/&#039;/g, "'");
}

function stoppedTyping() {
    if ($(".iRow:visible").length >= 3 && $("#addInstructionBtn").text() == 'Edit') {
        $("#addInstructionBtn").prop('disabled', false);
    }


    if ($("#instructionName").val().length > 0 && $("#instructionURL").val().length > 0) {
        document.getElementById('addInstructionBtn').disabled = false;
    } else {
        document.getElementById('addInstructionBtn').disabled = true;
    }

    if ($(".iRow:visible").length >= 3 && $("#addInstructionBtn").text() == 'Add') {
        $("#addInstructionBtn").prop('disabled', true);
    }



}

function checkValidURLInProj(str) {
    var trimmedString = str.trim().toLowerCase();

    if (!(/^(http|https|ftp):\/\/[a-z0-9]+([\-\.]{1}[a-z0-9]+)*\.[a-z]{2,5}(:[0-9]{1,5})?(\/.*)?$/i.test(str))) {
        // pwIsf.alert({ msg: 'Invalid URL', type: 'warning' });
        return false;
    }
    var m = 0;
    // var myarr = ["https://access.se.sdt.ericsson.net/", "https://eridoc.internal.ericsson.com/", "https://eridoc.ericsson.se/", "https://ericsson-my.sharepoint.com/", "https://nextgentm.sdt.ericsson.net/", "https://ericsson.sharepoint.com", "https://sitehandler-anza.internal.ericsson.com/", "https://sitehandler-anza.ericsson.net/", "https://sitehandler-apac.ericsson.net/"];
    var myarr = allUrls;
    for (let i = 0; i < myarr.length; i++) {
        let arrUrl = new URL(myarr[i]);
        let passUrl = new URL(trimmedString);
        if (arrUrl.host.toLowerCase() == passUrl.host.toLowerCase() && arrUrl.protocol == passUrl.protocol) {
            if (trimmedString.startsWith(myarr[i].toLowerCase())) {
            m = m + 1;
        }
        }
    
    }
    if (m == 0) {
        return false;
    }
    else if (m != 0) {
        return true;
    }

}

var arr = [];
function fetchWfLevelInstructioFromTable() {
    if (savingState == "") {
        var elems = $(".iRow");
        arr = [];
        for (var i = 0; i < elems.length; i++) {
            var obj = new Object();
            obj.flowChartDefID = subActivityDefID;
            obj.instructionID = elems[i].children[0].innerText;
            obj.urlName = elems[i].children[2].innerText;
            obj.urlLink = elems[i].children[3].innerText;
            obj.edited = elems[i].children[4].innerText;
            obj.deleted = elems[i].children[5].innerText;
            arr.push(obj);
        }   
        return arr;
    }

    else if (savingState == "save") {
        var elems = $(".iRow");
        arr = [];
        for (var i = 0; i < elems.length; i++) {
            var obj = new Object();
            obj.flowChartDefID = subActivityDefID;
            obj.instructionID = elems[i].children[0].innerText;
            obj.urlName = elems[i].children[2].innerText;
            obj.urlLink = elems[i].children[3].innerText;
            obj.edited = elems[i].children[4].innerText;
            obj.deleted = elems[i].children[5].innerText;
            obj.state = "save";
            arr.push(obj);

        }
     
        return arr;
    }
    else if (savingState == "noChange") {

        var result = oldWIData.map(function (el) {
            var o = Object.assign({}, el);
            o.state = "noChange";
            return o;
        })
        arr = result;
        return arr;
    }

    else if (savingState == "later") {
        var result = oldWIData.map(function (el) {
            var o = Object.assign({}, el);
            o.state = "later";
            return o;
        })
        arr = result;
        return arr;

    }
    else {
        return arr;
    }

}


//*******************************************STEP level***************************************
//For adding & editing instructions in table
//(global variable) tempJsonWI-newly added instructions in table before 'save workflow' button click(which are not saved in DB yet)
//(global variable) addedFromTable-added instructions in table that are fetched from DB (saved in DB after 'save workflow' button click)
function addInstructionStep(type) {
    type = type.innerText;
    let subActivityDefID;
    let mode;
    let currentCellID = ($('#stepIdIdentifier').val());

     subActivityDefID = $("#assesedDefid").val();
     mode = "PROJECTDEFINED";

    var name = escapeHtml($("#instructionNameStep").val());
    var url = escapeHtml($("#instructionURLStep").val().toLowerCase());
    var ref = escapeHtml($("#instructionReferenceStep").val());
    //mandatory, valid and name exist check
    var elems = $(".iNameStep");
    iNamesStep = [];
    iNameStepInLowerCase = [];
    for (var i = 0; i < elems.length; i++) {
        iNamesStep.push(elems[i].innerText);
        iNameStepInLowerCase.push(elems[i].innerText.toLowerCase());
    }
    //for url exist check
    var elemsUrl = $(".iURLStep");
    iURLStep = [];
    for (var i = 0; i < elemsUrl.length; i++) {
        iURLStep.push(escapeHtml(elemsUrl[i].innerText.toLowerCase()));
    }
    var elemsRef = $(".iRefStep");
    iRefStep = [];
    for (var i = 0; i < elemsRef.length; i++) {
        iRefStep.push(escapeHtml(elemsRef[i].innerText));
    }

    if (type == 'Edit') {
        var index1 = iNamesStep.indexOf($(".editingStep").children('td').eq(2).text());
        var index11 = iNameStepInLowerCase.indexOf($(".editingStep").children('td').eq(2).text().toLowerCase());
        iNamesStep.splice(index1, 1);
        iNameStepInLowerCase.splice(index11, 1);
        var index2 = iURLStep.indexOf($(".editingStep").children('td').eq(3).text());
        iURLStep.splice(index2, 1);
        var index3 = iRefStep.indexOf($(".editingStep").children('td').eq(4).text());
        iRefStep.splice(index3, 1);

    }

    if ((iNameStepInLowerCase.indexOf(name.toLowerCase()) > -1 || iURLStep.indexOf(url) > -1)) {


        if (iNameStepInLowerCase.indexOf(name.toLowerCase()) > -1) {
            pwIsf.alert({ msg: "Instruction name already exists.", type: "error" });
        }
        else if (iURLStep.indexOf(url) > -1) {
            pwIsf.alert({ msg: "Instruction URL already exists.", type: "error" });
        }
    }
    else if (!name || !url) {

        pwIsf.alert({ msg: "Please fill Mandatory fields", type: "error" });
    }

    else if (!checkValidURLInProj(unescapeHtml(url))) {
        pwIsf.alert({ msg: "Invalid URL!", type: "error" });
    }
    else {
        //soft add instruction to table
        if (type == 'Add') {
            var action = `<a title="Edit" onclick="editInstructionStep(this)" class="icon-edit lsp">` + getIcon('edit') + `</a>` +
                `<a title="Delete" onclick="deleteInstructionStep(this)" class="icon-delete lsp">` + getIcon('delete') + `</a>`;
            var html = `<tr class="iRowStep" style="visibility:visible"><td class="iHideStep">0</td><td class="iActionStep" style="padding-left: 10px;padding-top: 14px;">${action}</td><td class="iNameStep">${name}</td><td class="iURLStep"><a target="_blank" href="${url}">${url}</a></td><td class="iRefStep">${ref}</td><td class="iHideStep">false</td><td class="iHideStep">false</td></tr> `;
            $("#wfInstructionTableStep").append(html);
            tempJsonWI.push({ "urlName": name, "urlLink": url, "instructionID": 0, "reference": ref, "flowChartDefID": subActivityDefID, "deleted": false, "edited": false, "active": true, "stepID": $('#stepIdIdentifier').val(), "mode": mode });

            app.graph.getCell($('#stepIdIdentifier').val()).attributes.isStepInstructionExist = true;

            $("#addInstructionBtnStep").prop('disabled', true);



        }
        //soft update instruction in html table
        else if (type == 'Edit') {
            //let temp;
            var curName = $(".editingStep").children('td').eq(2).text();
            var curURL = $(".editingStep").children('td').eq(3).text();
            var curRef = $(".editingStep").children('td').eq(4).text();
            var insID = $(".editingStep").children('td').eq(0).text();
            if (insID == 0) {
                for (let t = 0; t < tempJsonWI.length; t++) {

                    if (curName == tempJsonWI[t].urlName && curURL == tempJsonWI[t].urlLink && curRef == tempJsonWI[t].reference && tempJsonWI[t].stepID == $('#stepIdIdentifier').val() && mode == tempJsonWI[t].mode) {
                        // tempJsonWI[i] = '';
                        tempJsonWI[t].urlName = name;
                        tempJsonWI[t].urlLink = url;
                        tempJsonWI[t].reference = ref;
                        tempJsonWI[t].stepID == $('#stepIdIdentifier').val();
                        tempJsonWI[t].edited = true;
                        tempJsonWI[t].flowChartDefID = subActivityDefID;
                        tempJsonWI[t].instructionID = insID;
                        tempJsonWI[t].mode = mode;

                        tempJsonWI[t].deleted = false;
                        tempJsonWI[t].active = true;
                        //temp= i;
                    }

                }
            }
            if (insID != 0) {


                for (let k = 0; k < addedFromTable.length; k++) {

                    if (addedFromTable[k].instructionID == insID && addedFromTable[k].stepID == currentCellID && addedFromTable[k].mode == mode) {

                        addedFromTable[k].urlName = name;
                        addedFromTable[k].urlLink = url;
                        addedFromTable[k].reference = ref;
                        addedFromTable[k].stepID == $('#stepIdIdentifier').val();
                        addedFromTable[k].edited = true;
                        addedFromTable[k].flowChartDefID = subActivityDefID;
                        addedFromTable[k].instructionID = insID;
                        addedFromTable[k].mode = mode;


                        addedFromTable[k].deleted = false;
                        addedFromTable[k].active = true;

                    }

                }


            }


            if ($(".editingStep").children('td').eq(2).text() != name || $(".editingStep").children('td').eq(3).text() != url || $(".editingStep").children('td').eq(4).text() != ref) {
                $(".editingStep").addClass('editedStep');
            }
            $(".editingStep").children('td').eq(2).text(unescapeHtml(name));

            $(".editingStep").children('td').eq(3).html("<a href='" + url + "'>" + url + "</a>");

            $(".editingStep").children('td').eq(4).text(unescapeHtml(ref));


            $(".editingStep").children('td').eq(5).text(true);
            //tempJsonWI[temp].edited = true;
            $("#addInstructionBtnStep").html('Add');
            $('#wfInstructionTableStep tr').removeClass("editingStep");

            $("#addInstructionBtnStep").prop('disabled', true);
        }

        $("#instructionNameStep").val('');
        $("#instructionURLStep").val('');
        $("#instructionReferenceStep").val('');


        if ($(".iRowStep:visible").length >= 3) {
            $("#addInstructionBtnStep").prop('disabled', true);
        }



    }

}

//For deletion of instructions from table
function deleteInstructionStep(elem) {
    let currentCellID = ($('#stepIdIdentifier').val());
    let subActivityDefID;
    let mode;

     subActivityDefID = $("#assesedDefid").val();
     mode = "PROJECTDEFINED";
    
    //let subActivityDefID = $("#assesedDefid").val();
    elem.parentElement.parentElement.classList.add('deletingStep');
    var curName = $(".deletingStep").children('td').eq(2).text();
    var curURL = $(".deletingStep").children('td').eq(3).text();
    var curRef = $(".deletingStep").children('td').eq(4).text();
    var curInsID = $(".deletingStep").children('td').eq(0).text();

    // wfDataObj.flowChartDefID = subActivityDefID;


    if (elem.parentElement.parentElement.classList.contains('editingStep')) {
        $("#addInstructionBtnStep").html('Add');
        $("#instructionNameStep").val('');
        $("#instructionURLStep").val('');
        $("#instructionReferenceStep").val('');
    }

    if (elem.parentElement.parentElement.firstChild.innerText == "0") {


        for (let t = 0; t < tempJsonWI.length; t++) {

            if (curName == tempJsonWI[t].urlName && curURL == tempJsonWI[t].urlLink && curRef == tempJsonWI[t].reference && tempJsonWI[t].stepID == $('#stepIdIdentifier').val() && mode == tempJsonWI[t].mode) {

                tempJsonWI.splice(t, 1);

            }

        }

        $('#wfInstructionTableStep tr').removeClass("deletingStep");
        elem.parentElement.parentElement.remove();
       // updateStepInstructionExist(currentCellID);
    }
    else {
        for (let t = 0; t < addedFromTable.length; t++) {

            if (addedFromTable[t].instructionID == curInsID && addedFromTable[t].stepID == currentCellID && addedFromTable[t].mode == mode) {
                addedFromTable[t].deleted = true;
                //deletedTrueFromTable.splice(t, 1);
            }

        }
        elem.parentElement.parentElement.lastElementChild.innerText = true;

        //deletedTrueFromTable.push()({ "urlName": curName, "urlLink": curURL, "instructionID": curInsID, "reference": curRef, "flowChartDefID": subActivityDefID, "deleted": true, "edited": false, "active": true, "stepID": currentCellID});


        $('#wfInstructionTableStep tr').removeClass("deletingStep");
        elem.parentElement.parentElement.style.display = 'none';
        elem.parentElement.parentElement.style.visibility = 'hidden';
      //  updateStepInstructionExist(currentCellID);
    }

    if ($(".iRowStep:visible").length >= 3) {
        $("#addInstructionBtnStep").prop('disabled', true);
    }



    else if ($("#instructionNameStep").val().length > 0 && $("#instructionURLStep").val().length > 0) {
        $("#addInstructionBtnStep").prop('disabled', false);
    }
    if ($(".iRowStep:visible").length == 0) {
        $("#addInstructionBtnStep").html('Add');
        $("#instructionNameStep").val('');
        $("#instructionURLStep").val('');
        $("#instructionReferenceStep").val('');
        app.graph.getCell($('#stepIdIdentifier').val()).attributes.isStepInstructionExist = false;

    }

}

//soft editing instruction  on edit icon click
function editInstructionStep(elem) {

    $('#wfInstructionTableStep tr').removeClass("editingStep");
    elem.parentElement.parentElement.classList.add('editingStep');
    $("#addInstructionBtnStep").html('Edit');
    $("#addInstructionBtnStep").prop('disabled', false);
    $("#instructionNameStep").val(unescapeHtml(elem.parentElement.parentElement.childNodes[2].innerText));
    $("#instructionURLStep").val(unescapeHtml(elem.parentElement.parentElement.childNodes[3].innerText));
    $("#instructionReferenceStep").val(unescapeHtml(elem.parentElement.parentElement.childNodes[4].innerText));
}

function stoppedTypingStep() {
    if ($(".iRowStep:visible").length >= 3 && $("#addInstructionBtnStep").text() == 'Edit') {
        $("#addInstructionBtnStep").prop('disabled', false);
    }


    if ($("#instructionNameStep").val().length > 0 && $("#instructionURLStep").val().length > 0) {
        document.getElementById('addInstructionBtnStep').disabled = false;
    } else {
        document.getElementById('addInstructionBtnStep').disabled = true;
    }

    if ($(".iRowStep:visible").length >= 3 && $("#addInstructionBtnStep").text() == 'Add') {
        $("#addInstructionBtnStep").prop('disabled', true);
    }

}

//populating newly added instructions the table in modal(from tempJsonWI global variable)
function populateTableForNewlyAddedData() {
    let mode;
    let multiModeCheck = $('#multiModeCheck').prop('checked');
        mode = "PROJECTDEFINED";
    let tempHtmlTable = '';
    let firstRowHtml = '<tr id="wfInstructionTableStepFirstRow">' + '<th class="iHideStep">ID</th>' + '<th style="width: 90px;" class="iActionStep">Action </th>' + '<th>Name</th>' + '<th>URL</th>' + '<th>Reference</th>' + '<th class="iHideStep">isEdited</th>' + '<th class="iHideStep">isDelete</th>' + '</tr>';
    $("#wfInstructionTableStep tr").find("tr:gt(0)").remove();
    //$("#wfInstructionTableStep tr").remove();

    let currentCell = app.graph.getCell($('#stepIdIdentifier').val());



    let currentCellID = ($('#stepIdIdentifier').val());
    let filteredStepData = tempJsonWI.filter(function (el) {
        return ((el.stepID) == currentCellID && (el.mode) == mode);
    });
    // console.log(filteredStepData);
    for (let i = 0; i < filteredStepData.length; i++) {
        var action = `<a title="Edit" onclick="editInstructionStep(this)" class="icon-edit lsp">` + getIcon('edit') + `</a>` +
            `<a title="Delete" onclick="deleteInstructionStep(this)" class="icon-delete lsp">` + getIcon('delete') + `</a>`;
        let tempName = filteredStepData[i].urlName;
        let tempURL = filteredStepData[i].urlLink;
        let tempRef = filteredStepData[i].reference;
        let tempEdited = filteredStepData[i].edited;
        let tempDeleted = filteredStepData[i].deleted;

        var html = `<tr class="iRowStep" style="visibility:visible"><td class="iHideStep">0</td><td class="iActionStep" style="padding-left: 10px;padding-top: 14px;">${action}</td><td class="iNameStep">${tempName}</td><td class="iURLStep"><a target="_blank" href="${tempURL}">${tempURL}</a></td><td class="iRefStep">${tempRef}</td><td class="iHideStep">${tempEdited}</td><td class="iHideStep">${tempDeleted}</td></tr> `;
        tempHtmlTable += html;

    }
    $("#wfInstructionTableStep").append(firstRowHtml);
    $("#wfInstructionTableStep").append(html);

    populateTableForStepDataFromDB(tempHtmlTable);
}

//populating instructions in the table in modal fetched from already saved instructions in DB (from addedFromTable global variable)
function populateTableForStepDataFromDB(stepTableHtml) {   

    let mode;
    mode = "PROJECTDEFINED";
   
    $("#wfInstructionTableStep tr").remove();
    let tempHTML = '';
    let firstRowHtml = '<tr id="wfInstructionTableStepFirstRow">' + '<th class="iHideStep">ID</th>' + '<th style="width: 90px;" class="iActionStep">Action </th>' + '<th>Name</th>' + '<th>URL</th>' + '<th>Reference</th>' + '<th class="iHideStep">isEdited</th>' + '<th class="iHideStep">isDelete</th>' + '</tr>';
    let currentCell = app.graph.getCell($('#stepIdIdentifier').val());
    let currentCellID = ($('#stepIdIdentifier').val());

    let filteredStepDataToAdd = addedFromTable.filter(function (el) {
        return ((el.stepID) == currentCellID && (el.deleted) == false && (el.mode) == mode);
    });
    for (let k = 0; k < filteredStepDataToAdd.length; k++) {
        // stepURLButtonText = "Edit URL";
        let tempName = filteredStepDataToAdd[k].urlName;
        let tempURL = filteredStepDataToAdd[k].urlLink;
        let tempRef = filteredStepDataToAdd[k].reference;
        let tempInsId = filteredStepDataToAdd[k].instructionID;
        let tempEdited = filteredStepDataToAdd[k].edited;
        let tempDeleted = filteredStepDataToAdd[k].deleted;


        var action = `<a title="Edit" onclick="editInstructionStep(this)" class="icon-edit lsp">` + getIcon('edit') + `</a>` +
            `<a title="Delete" onclick="deleteInstructionStep(this)" class="icon-delete lsp">` + getIcon('delete') + `</a>`;

        var html = `<tr class="iRowStep" style="visibility:visible"><td class="iHideStep">${tempInsId}</td><td class="iActionStep" style="padding-left: 10px;padding-top: 14px;">${action}</td><td class="iNameStep">${tempName}</td><td class="iURLStep"><a target="_blank" href="${tempURL}">${tempURL}</a></td><td class="iRefStep">${tempRef}</td><td class="iHideStep">${tempEdited}</td><td class="iHideStep">${tempDeleted}</td></tr> `;
        app.graph.getCell($('#stepIdIdentifier').val()).attributes.isStepInstructionExist = true;
        tempHTML += html;
    }
    $("#wfInstructionTableStep").append(firstRowHtml);
    $("#wfInstructionTableStep").append(tempHTML);

    $("#wfInstructionTableStep").append(stepTableHtml);
    if ($($('.addStepURL')[2]).text() == 'View URL') {
        $(".iActionStep").css('display', 'none');
        $("#WIstepRow").css('display', 'none');
    }
    else {
        $(".iActionStep").css('display', '');
        $("#WIstepRow").css('display', '');
    }
}
var oldWIData = [];

//fetching instructions saved in DB  while editing a WF on click of WF+
function getInstructionData() {

    
    let wfDataObj = new Object();
    let JsonStringArr = [];
    let subActivityDefIDArray = [$('#assesedDefid').val(), $('#experienceDefid').val()];

    for (let i = 0; i < 2; i++) {
        wfDataObj = {};
        wfDataObj.flowChartDefID = subActivityDefIDArray[i];
        wfDataObj.stepID = null;
        JsonStringArr.push(wfDataObj);
    }
    $.isf.ajax({
        url: service_java_URL + "activityMaster/getInstructionURLList",
        context: this,
        crossdomain: true,
        processData: true,
        async: false,
        contentType: 'application/json',
        type: 'POST',
        data: JSON.stringify(JsonStringArr),
        success: function (data) {
            data = data.data;
            if (data && data.length != 0) {

                if (data[0].workFlowUpdated == false && data[0].instructionType == "WF_INSTRUCTION") {
                    $("#instructionBtn").addClass('btn-danger');
                    $("#instructionBtn").css('background-color', '');
                    $("#instructionBtn").text('Edit WI-URL');
                    $('#wfInstrcutionUpdated').prop('checked', false);
                    $('#instructionText').html('WI-URL not updated yet');
                }
                else if (data[0].workFlowUpdated == true && data[0].instructionType == "WF_INSTRUCTION") {
                    $("#instructionBtn").removeClass('btn-danger');
                    $("#instructionBtn").css('background-color', '');
                    $("#instructionBtn").text('Edit WI-URL');
                    $('#instructionText').html('');
                    $('#wfInstrcutionUpdated').prop('checked', true);
                }

                else {
                    $("#instructionBtn").removeClass('btn-danger');
                    $("#instructionBtn").css('background-color', '#E0E0E0');
                    $("#instructionBtn").text('Add WI-URL');
                    if ($("#wfInstrcutionUpdated").is(':checked') == false)
                    $('#instructionText').html('WI-URL not added yet');
                }

                for (x in data) {

                    if (data[x].stepID == "" || data[x].stepID == null) {

                        if (data[x].mode == 'PROJECTDEFINED') {

                            var action = `<a title="Edit" onclick="editInstruction(this)" class="icon-edit lsp">` + getIcon('edit') + `</a>` +
                                `<a title="Delete" onclick="deleteInstruction(this)" class="icon-delete lsp">` + getIcon('delete') + `</a>`;
                            var html = `<tr class="iRow" style="visibility:visible"><td class="iHide">${data[x].instructionID}</td><td class="iAction">${action}</td><td class="iName">${escapeHtml(data[x].urlName)}</td><td class="iURL"><a target="_blank" href="${data[x].urlLink}">${escapeHtml(data[x].urlLink)}</a></td><td class="iHide">false</td><td class="iHide">false</td></tr> `;
                            $("#wfInstructionTable").append(html);
                            $("#addInstructionBtn").prop('disabled', true);
                            if (data[0].workFlowUpdated != false) {
                                changeBackColorOfWIButton();
                            }
                        }
                        oldWIData = fetchWfLevelInstructioFromTable();
                    }

                    else if (data[x].stepID != "" || data[x].stepID != null) {
                        
                        if (app.graph.getCell(String(data[x].stepID))) {
                            console.log(String(data[x].stepID));
                            app.graph.getCell(String(data[x].stepID)).attributes.isStepInstructionExist = true;
                        }
                        
                    }

                }
                dFlag = true;

            }
            checkForDataNonOwner();           

            if (stepDataFromTableCount == 0) {

                for (let i = 0; i < data.length; i++) {
                    let cellID = data[i].stepID;
                    if (cellID) {
                        addedFromTable.push(data[i]);
                        stepDataFromTableCount = 1;

                    }
                }
            }
        },
        error: function (status) {
            pwIsf.alert({ msg: 'Error fetching data', type: "error" });
        },
        complete: function () {
            pwIsf.removeLayer();
        }
    });
}

//For sending the json saved in global variables to SaveJsonFromUI API on click of 'Save Workflow' button
function pushStepJsonWIinArr() {


    let AssessedTempData = tempJsonWI.filter(function (el) {
        return ((el.mode) == "PROJECTDEFINED");
    });
    let AssessedSavedData = addedFromTable.filter(function (el) {
        return ((el.mode) == "PROJECTDEFINED");
    });
   
    let arrAssExpInstruction = [];
    arrAssExpInstruction = [AssessedTempData, AssessedSavedData];
    return arrAssExpInstruction;


}

//naming of 'Add URL' OR 'Edit URL' button in inspector
function AddURLbuttonNameSet(cellID) {
    let mode;
     mode = "PROJECTDEFINED";
 
    let filteredStepDataToAdd = addedFromTable.filter(function (el) {
        return ((el.stepID) == cellID && (el.deleted) == false && (el.mode) == mode)
    });
    let tempAddedData = tempJsonWI.filter(function (el) {
        return ((el.stepID) == cellID && (el.deleted) == false && (el.mode) == mode);
    });
    let lengthTableRow = filteredStepDataToAdd.length + tempAddedData.length;
    if (lengthTableRow != 0) {
        $($('.addStepURL')[2]).text('Edit URL');
        stepURLButtonText = "Edit URL";
    }
    else {
        $($('.addStepURL')[2]).text('Add URL');
        stepURLButtonText = "Add URL";
    }
    checkOwner(true);
}
//nahar
var allUrls = [];
function getLocalAndGlobalUrls() {
    pwIsf.addLayer({ text: "Please wait..." });
    let service_URL1 = service_java_URL + 'activityMaster/getAllGlobalUrl';

    $.isf.ajax({
        url: service_URL1,
        type: 'GET',
        success: function (data) {
         
            if (data) {
                for (x in data) {
                    allUrls.push(data[x].urlLink);
                }

            }
        },
        complete: function (xhr, status) {
            if (status == "success") {
            }

        },
        error: function (data) {
         
        }
    });

    var service_URL2 = service_java_URL + 'activityMaster/getAllLocalUrl?projectID=' + projId;

    $.isf.ajax({
        url: service_URL2,
        type: 'GET',
        success: function (data) {
           
            data = data.responseData;
            if (data) {
                for (x in data) {
                    allUrls.push(data[x].localUrlLink);
                }

            }
            pwIsf.removeLayer();
        },
        complete: function (xhr, status) {
            if (status == "success") {
            }
        },
        error: function (data) {
            console.log('error');
        }
    });

}
var savingState = "";

//ChecK WF owner accessibility for Work instruction URL 
function checkOwner(isStep = false) {
   
    savingState = "save";
    //pwIsf.confirm.hide();
    iWfLevelArrLength = $(".iRow").length;
    if (signumGlobal.toLowerCase() != wfOwnerName.toLowerCase()) {
        $(".iActionStep").css('display', 'none');
        $("#WIrow").css('display', 'none');
        if (iWfLevelArrLength == 0) {
            $("#instructionBtn").prop('disabled', true);
        }
        else {
            $("#instructionBtn").prop('disabled', false);
        }
    }
    else {
        $(".iActionStep").css('display', '');
        $("#WIrow").css('display', '');
        $("#instructionBtn").prop('disabled', false);
    }
    
    if (isStep) {
        if (signumGlobal.toLowerCase() != wfOwnerName.toLowerCase()) {
            $(".iActionStep").css('display', 'none');
            $("#WIrowStep").css('display', 'none');
            if ($($('.addStepURL')[2]).text() == 'Add URL') {
                $($('.addStepURL')[2]).attr("disabled", true);
            }
            else if ($($('.addStepURL')[2]).text() == 'Edit URL') {
                $($('.addStepURL')[2]).attr("disabled", false);
                $($('.addStepURL')[2]).text('View URL');
                $("#instructionNameStep").prop('disabled', true);
                $("#instructionURLStep").prop('disabled', true);
                $("#instructionReferenceStep").prop('disabled', true);

                $("#addInstructionBtnStep").prop('disabled', true);

            }
        }
        else {

            $($('.addStepURL')[2]).attr("disabled", false);
            $($('.addStepURL')[2]).text(stepURLButtonText);

            $("#instructionNameStep").prop('disabled', false);
            $("#instructionURLStep").prop('disabled', false);
            $("#instructionReferenceStep").prop('disabled', false);
            $(".iActionStep").css('display', '');
            $("#WIrowStep").css('display', '');

        }
    }
    else {
        if (signumGlobal.toLowerCase() != wfOwnerName.toLowerCase()) {

            $("#instructionName").prop('disabled', true);
            $("#instructionURL").prop('disabled', true);
            $(".iAction").css('display', 'none');
            $("#WIrow").css('display', 'none');

        }
        else {
            $("#instructionName").prop('disabled', false);
            $("#instructionURL").prop('disabled', false);
            // $(".iAction").children().css('visibility', 'visible');
            $(".iAction").css('display', '');
            $("#WIrowStep").css('display', '');
        }
        $('#addInstruction').modal('show');
    }
}

function checkForDataNonOwner() {
    var iWfLevelArrLength = 0;
    iWfLevelArrLength = $(".iRow").length;
 
    if (signumGlobal.toLowerCase() != wfOwnerName.toLowerCase()) {
        $(".iActionStep").css('display', 'none');
        if (iWfLevelArrLength == 0) {
            $("#instructionBtn").prop('disabled', true);
        }
        else {
            $("#instructionBtn").prop('disabled', false);
        }
    }
    else {
        $(".iActionStep").css('display', '');
        $("#instructionBtn").prop('disabled', false);
    }
    //  $("#instructionBtn").show()
}

//Check if rule is present in flowchart and take action accordingly
function isRulePresent(app) {
    let isMultiModeEnabled = $('#multiModeCheck').prop('checked');
    let ruleCells = app.graph.getCells().filter(function (cell) { return (cell.attributes.startRule == true || cell.attributes.stopRule == true) });

    if (ruleCells.length == 0) {

        $('#IsAutoSenseDisabled').prop({ 'disabled': true, 'checked':'checked' });
        
    }
    else {
        $('#IsAutoSenseDisabled').prop({ 'disabled': false });
    }

   

}
var clickedLocation = "";
function setoutside() {
    clickedLocation = "outside";
}
function insideClicked() {
   
    clickedLocation = "";
    $("#keepPrevious").prop('disabled', true);
    $("#later").prop('disabled', true);
    setTimeout(function () {
        if ($(".iRow:visible").length > 0) {
            $(".enableDisable").css({ 'background-color': '', 'pointer-events': '' });
        } }, 400);


}

function populateLoeMeasurement() {
    $.isf.ajax({
        url: `${service_java_URL}flowchartController/getLoeMeasurementCriterion`,
        success: function (data) {
            if (data.responseData != null) {
                $('#loe').empty();
                $('#loe').append(`<option value="-1" selected disabled>--Select One--</option>`);
                $.each(data.responseData, function (i, d) {
                    $('#loe').append(`<option value="${d.loeMeasurementCriterionID}">${d.loeMeasurementCriterion}</option>`);
                    if (localStorage.getItem('loeValue') !== null) {
                        $('#loe').val(localStorage.getItem('loeValue')).change();
                    }
                });
            }
        },
        error: function (xhr, status, statusText) {
            console.log('An error occurred on getLoeMeasurementCriterion');
        }
    });
   
}

function onLoeChange() {
    document.getElementById('loe').style.border = '';

}