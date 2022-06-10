let file = null; 
let arr = []; let json = null;
let subActivityDefID = null; let isPreview = true;
let urlParams = new URLSearchParams(window.location.search);
let subID = urlParams.get("subId"); var projId = urlParams.get("projID");
let global_validationOfJsonsArray = { AssessedValidation: false, ExperiencedValidation: false };
let global_toggleFlag = true;
let domain_id = localStorage.getItem('domain_id');
let tech_id = localStorage.getItem('tech_id');
let allToolsLabel = true; let anchorVisibleFlag = true; let clickcount = 0;
let botJsonData = new Object();
let botID = urlParams.get("newBotID"); let botName = urlParams.get("newBOTName");
let versionArray = [];  let deletedStepData=''; let is20WOExecuted=false;
let Remark = ''; let isAll20WOExecuted = false;
let selectedNewStep=[]; let selectedOldStep=[]; let isSavingFlag="false";
let newStepAddArray = [];
let storeUniqueDeletedStepsArray = {'deletedSteps':[]};
let storeRedundantDeletedStepsArray = { 'deletedSteps': [] };
let wfIDUrl = urlParams.get("wfid");
let isExperienced = false;


var removeByAttr = function(arr, attr, value){
    var i = arr.length;
    while(i--){
        if( arr[i] 
            && arr[i].hasOwnProperty(attr) 
            && (arguments.length > 2 && arr[i][attr] === value ) ){ 

            arr.splice(i,1);

        }
    }
    return arr;
}

function onChangeReason() {
    document.getElementById('editReasonDDBot').style.border = '';
}

function saveBotConfig(){
   
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

    if($('#botConfigModal').css('display')=='block'){$('#botConfigModal').modal('hide')}
    //   $('#botConfigModal').modal('toggle');

}

function openExistingConfig(currentCellID,obj){
    // let botConfigJson = app.graph.getCell(currentCellID).attributes.botConfigJson;
    let botConfigJson = $(obj).data('configjson');
    createFormFromFormBuilderJson('botConfigBody',JSON.stringify(botConfigJson));

    $('#botConfigModal').modal('show');
}

function openConfig(rpa_id,cellid){
    let botConfigJson = '';
    let configForBot = app.graph.getCell(cellid).attributes.configForBot;
    $('#currentCellID').val(cellid);     
   
    
    getBotDetails(rpa_id, cellid);
    getConfigForBot(rpa_id, cellid);
}

function getBotDetails(rpaid,cellid) {
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
                //newly edited
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
            //if(response!='' && response.json != "[]" && response.json !="") {
            if (response != '') {
                botConfigJson = response.json;    
                $('#currentCellID').data('botConfigJson',botConfigJson);
                $('#currentCellID').data('rpaid', rpa_id);
                $('#currentCellID').data('botType', response.botType);
                //$('#currentCellID').data('botNestId', response.refBotId);//botNestId

                //newly edited
                app.graph.getCell(cellid).attributes.botConfigJson = '';
                app.graph.getCell(cellid).attributes.configForBot = rpa_id;
                app.graph.getCell(cellid).attributes.botType = response.botType;
                //app.graph.getCell(cellid).attributes.botNestId = response.refBotId;

                if (response.json != "[]" && response.json != "") {
                    createFormFromFormBuilderJson('botConfigBody', botConfigJson);
                    $('#botConfigModal').modal('show');
                }
                else { pwIsf.alert({ msg: "Configuration for this BOT ID is not available.", type: "info" }); }
                //newly edited done
             //   createFormFromFormBuilderJson('botConfigBody',botConfigJson);
             //   $('#botConfigModal').modal('show');
                //$('#configSettings').hide();
            }
            else {
                delete app.graph.getCell(cellid).attributes.botConfigJson;
                delete app.graph.getCell(cellid).attributes.configForBot;

                pwIsf.alert({msg:"Configuration for this BOT ID is not available.", type:"info"});
                //  delete cell.attributes.botConfigJson;                   
            }
          
        },
    });
}

function openpdf(data){
   
    var parent = $('#objpdf').parent();
    var dataURL = data+'#toolbar=0';
    var newElement = '<object id="objpdf" width="100%" height="100%" type="application/pdf" data="'+dataURL+'">';
    $('#objpdf').remove();
    parent.append(newElement);
    $('#pdfDiv').modal('show');
}

$(document).on("click", ".viewWfDef", function () {

    currentSelectBox = $(this).closest('tr').find('select')
    var selected = $(currentSelectBox).val();
    $("#viewExsistingWorkOrder").modal('toggle');
   
    viewExsistingFlowChart(selected, removelayer)   
});


let oldDropDownStepsOnVersionChange = [];
$(document).on('change', '#wfVersionSelectOld', function() {

    storeUniqueDeletedStepsArray = {'deletedSteps':[]};
    storeRedundantDeletedStepsArray = {'deletedSteps':[]};
    let selectedVersion = $('#wfVersionSelection').val().split('_')[0];
    let selectedWfID = $('#wfVersionSelection').val().split('_')[2];
   
    $.isf.ajax({
        type:"GET",
      
        url: service_java_URL + "flowchartController/getFlowChartByDefID/" + subID + "/" + selectedVersion + "/" + projID + "/" + selectedWfID,
        success: function (resp) { 
            
            oldDropDownStepsOnVersionChange = [];
            let currOldStepSelectArr =  $('.oldStepDropDown');
            let savingTextArr =  $('.savingsText');
            let expectedSavingTextArr =  $('.expectedSavingsText');
            let calculateSavingsBtnArr =  $('.calculateSavings');
            let newStepDropdownArr =  $('.newStepDropdown');
            let botSavingTableData = $('#botSavingTable').dataTable().fnGetData();

            for(let m=0; m<currOldStepSelectArr.length; m++){
                $(currOldStepSelectArr[m]).html('');                    
                $(newStepDropdownArr[m]).val('').trigger('change');
                $(calculateSavingsBtnArr[m]).prop('disabled',true);
                $(savingTextArr[m]).val('');
                $(expectedSavingTextArr[m]).val('');
            }    

            if(resp[0].SubActivityFlowChartDefID != subActivityDefID){
                oDelTable.clear(); oDelTable.draw();

                for(let m=0; m<currOldStepSelectArr.length; m++){
                    $.each(resp,function(i,d){ 
                        if(d.ExecutionType == "Automatic"){
                            //  v.StepID + '_' + v.ExecutionType+'_'+v.RpaID+'_'+(Number)(v.Savings)
                            $(currOldStepSelectArr[m]).append('<option value="' + d.StepID + '_' + d.ExecutionType + '_' + d.RpaID + '_' + (Number)(d.Savings) + '_' + d.Task +'"> A/'+ d.StepName + '</option>');
                            if(m==0){ oldDropDownStepsOnVersionChange.push({ 'StepID':d.StepID, 'ExecutionType':d.ExecutionType, 'RpaID':d.RpaID, 'Savings':(Number)(d.Savings), 'StepName':d.StepName, 'task':d.Task});  }
                            
                        }
                        else{
                            $(currOldStepSelectArr[m]).append('<option value="' + d.StepID + '_' + d.ExecutionType + '_' + d.RpaID + '_' + (Number)(d.Savings) + '_' + d.Task +'"> M/'+ d.StepName + '</option>');
                            if(m==0){ oldDropDownStepsOnVersionChange.push({ 'StepID':d.StepID, 'ExecutionType':d.ExecutionType, 'RpaID':d.RpaID, 'Savings':(Number)(d.Savings), 'StepName':d.StepName, 'task':d.Task});  }
                        }
                });
                }
            }
            else{
                getDeletedStepTable(deletedStepData);

                for(let m=0; m<currOldStepSelectArr.length; m++){
                    $.each(deletedStepData.deletedSteps,function(i,d){

                        if(d.ExecutionType == "Automatic"){
                            //  v.StepID + '_' + v.ExecutionType+'_'+v.RpaID+'_'+(Number)(v.Savings)
                            $(currOldStepSelectArr[m]).append('<option value="' + d.StepID + '_' + d.ExecutionType + '_' + d.RpaID + '_' + (Number)(d.Savings) + '_' + d.Task +'"> A/'+ d.StepName + '</option>');
                        }
                        else{
                            $(currOldStepSelectArr[m]).append('<option value="' + d.StepID + '_' + d.ExecutionType + '_' + d.RpaID + '_' + (Number)(d.Savings) + '_' + d.Task +'"> M/'+ d.StepName + '</option>');
                        }
                    });
                }
                //calculate 20WO
                if(deletedStepData.deletedSteps.length!=0){      
                   
                    let is20WOExecutedInArray = deletedOldStepsData.deletedSteps.filter(function(el){if(el.ExecutionType === "Manual" && el.flag == true){return el.flag;}});
                    let getallmanualandtrue = deletedOldStepsData.deletedSteps.filter(function(el){if(el.ExecutionType === "Manual" && el.isAll20WOExecuted == true){ return true;} else {return false;}});

                    (is20WOExecutedInArray.length!=0)?is20WOExecuted = true:is20WOExecuted = false;
                    //(getallmanual.length == getallmanualandtrue.length )?isAll20WOExecuted = true:isAll20WOExecuted = false;
                    (getallmanualandtrue.length!=0 )?isAll20WOExecuted = true:isAll20WOExecuted = false;
                }
            }
           
        },
        error:function (xhr, status, statusText) {
            pwIsf.alert({ msg: "error", type: 'error' });
        }
    });

})





function removelayer() {
    pwIsf.removeLayer();
}

//Check if rule is present in flowchart
function isRulePresent(app) {

    let ruleCells = app.graph.getCells().filter(function (cell) { return (cell.attributes.startRule == true || cell.attributes.stopRule == true) });

    if (ruleCells.length != 0) {
        return true;
    }
    else {
        return false;
    }
}

function savejson() {

    //if(Object.keys(arrOfJsons).length==1){
    //    if(!global_validationOfJsonsArray.NoviceValidation){
    //        pwIsf.alert({msg: 'Validate the Flow Chart first', type:'error'});
    //        return true;
    //    }
    //}
    let validationWorkflow = validateAndAdjustFlowchart(app);

    let hasRule = isRulePresent(app);
    let infoIcon = '<i class="fa fa-info-circle" style="font-size:22px;color:#9cb3e8;"></i>';
    let autoSenseWarningHtml = '<br />' + infoIcon + '&nbsp; The WF contains autosense rules which are by default disabled and can be enabled via WF edit page'

    reloadAfterUpdate = false;
    var wf_name = $("#WFName").text();
    if ((validationWorkflow.formValidation.status) && (validationWorkflow.WfValidation.status)) {

        let newJsonArr = JSON.parse(JSON.stringify(app.graph.getCells())); //Final updated flowchart json cell array
        let deployedBot = urlParams.get("newBotID"); //Deployed BOT

        //Check if 2 bots restriction validation is checked
        if (isTwoBotsValidated(botSavingsConstants.oldJsonArr, newJsonArr, deployedBot)) {

            pwIsf.confirm({
                title: 'Save Changes'
                , msg: '<input type="checkbox" id="futureWoUpdateCheckBox"> Update future work orders<br/>' +
                    'Do you want to overwrite the flowchart?' + (hasRule ? autoSenseWarningHtml : ''),
                'buttons': {
                    'Yes': {
                        'action': function () {
                            // saveJsonfromUI(projId, subID, subActivityDefID, version, flag, $('#WFName').val(), 'updateexisting');
                            var isFutureWorkOrdersUpdate = $('#futureWoUpdateCheckBox').is(':checked');
                            //btnSaveWorkFlow(projId, subID, version, "updateexisting", true, isFutureWorkOrdersUpdate);
                            btnSaveWorkFlow(projId, subID, version, "updateexisting", false, isFutureWorkOrdersUpdate);
                        }
                    },
                    'No': {
                        'action': function () {

                        }
                    },

                }
            });
        }
    }
    //else {
    //    pwIsf.alert({ msg: 'Please enter WF Name!!', type: 'warning' });
    //}
}


var arrOfJsons = {};
var WFNameArr = []; var flagArr=[]; var defIdArr = [];

var experiencedMode = '0'
var ftr = localStorage.getItem('ftr');
var neneededforexecution = localStorage.getItem('neneededforexecution');
var slahours = localStorage.getItem('slaHours');


let globFlChartAddJsonArr = [];
let stepIDStepNameTaskId_Container = { 'Assessed': [], 'Experienced': [] };

function convertElements(json, flowChartMode = ''){
    json.cells.forEach(function (cell) {
        if (cell.type == 'basic.Rect' || cell.type == 'app.ericssonStep' || cell.type == 'ericsson.Manual') {

            //Creating stepIDStepNameTaskId Object to store the combination which is later used for deleting the rules
            if (cell.type == 'ericsson.Manual' && (cell.startRule == true || cell.stopRule == true)) {
                if (cell.startRule == true) {
                    let stepIDStepNameTaskId_Obj = new Object();
                    stepIDStepNameTaskId_Obj.cellIDruleType = cell.id + '_START';
                    stepIDStepNameTaskId_Obj.stepIDStepNameTaskId = cell.id + '_' + cell.name + '_' + cell.action.split('@')[0];
                    flowChartMode == 'Assessed' ? stepIDStepNameTaskId_Container.Assessed.push(stepIDStepNameTaskId_Obj) : stepIDStepNameTaskId_Container.Experienced.push(stepIDStepNameTaskId_Obj);
                }
                if (cell.stopRule == true) {
                    let stepIDStepNameTaskId_Obj = new Object();
                    stepIDStepNameTaskId_Obj.cellIDruleType = cell.id + '_STOP';
                    stepIDStepNameTaskId_Obj.stepIDStepNameTaskId = cell.id + '_' + cell.name + '_' + cell.action.split('@')[0];
                    flowChartMode == 'Assessed' ? stepIDStepNameTaskId_Container.Assessed.push(stepIDStepNameTaskId_Obj) : stepIDStepNameTaskId_Container.Experienced.push(stepIDStepNameTaskId_Obj);
                }
            }

            cell.type = 'ericsson.Manual';
            cell.name = cell.attrs.bodyText.text.split('\n\n')[0];
            cell.tool = cell.attrs.task.toolID + '@' + cell.attrs.task.toolName;
            cell.action = cell.attrs.task.taskID + '@' + cell.attrs.task.taskName;      
            cell.responsible = (cell.responsible) ? cell.responsible:"";
            delete cell.attrs.rect; delete cell.attrs.text;

        }
        if (cell.type == 'erd.WeakEntity' || cell.type == 'app.ericssonWeakEntity' || cell.type == 'ericsson.Automatic') {
            cell.type = 'ericsson.Automatic';
            //  cell.avgEstdTime = cell.attrs.task.avgEstdEffort.toString();
            cell.name = cell.attrs.bodyText.text.split('\n\n')[0];
            cell.execType = "Automatic";
            cell.tool = cell.attrs.task.toolID + '@' + cell.attrs.task.toolName;
            cell.action = cell.attrs.task.taskID + '@' + cell.attrs.task.taskName;
            cell.rpaid = cell.attrs.tool.RPAID + '@' + cell.attrs.tool.RPAID+'-'+cell.attrs.tool.RPAName;
            cell.responsible = (cell.responsible) ? cell.responsible : "";
            delete cell.attrs.rect; delete cell.attrs.text;
           
        }
       
                           
    });
    return json;
}


function viewFlowChartWorkOrder(projID, subId, version, wfid) {   

    let startStepJson = '{"cells":[{"type":"ericsson.StartStep","position":{"x":260,"y":290},"size":{"width":60,"height":60},"angle":0,"preserveAspectRatio":true,"id":"f5d73221-92e9-4bc7-8748-b903899bdeac","z":1,"attrs":{"circle":{"fill":"green","cx":30,"cy":30,"stroke-width":0},"text":{"x":17,"y":35,"text":"Start","fill":"white","font-family":"Roboto Condensed","font-weight":"Normal","font-size":15,"stroke-width":0},"root":{"dataTooltipPosition":"left","dataTooltipPositionSelector":".joint-stencil"}}}]}';


    let noFlowchartAddFoundJson = '{ "cells": [{ "size": { "width": 200, "height": 90 }, "angle": 0, "z": 1, "position": { "x": 225, "y": 230 }, "type": "basic.Rect", "attrs": { "rect": { "rx": 2, "ry": 2, "width": 50, "stroke-dasharray": "0", "stroke-width": 2, "fill": "#dcd7d7", "stroke": "#31d0c6", "height": 30 }, "text": { "font-weight": "Bold", "font-size": 11, "font-family": "Roboto Condensed", "text": "No FlowChart Available", "stroke-width": 0, "fill": "#222138" }, ".": { "data-tooltip-position-selector": ".joint-stencil", "data-tooltip-position": "left" } } }] }';
    pwIsf.addLayer({ text: "Please wait ..." });
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
        $('#editReasonDDBot').empty();
        $('#editReasonDDBot').append('<option value="0" selected>Select One</option>')
        $.each(data, function (i, d) {
            $('#editReasonDDBot').append('<option value="' + d.EditReason + '">' + d.EditReason + '</option>');
        })
    }
    $.isf.ajax({
        url: service_java_URL + "flowchartController/viewFlowChartForSubActivity/" + projID + "/" + subId + "/0/" + version + '/' + false + '/' + wfid,
        async: false,
        success: function (data) {
            //return true;
            let subActivityDefIDArr = [];
            for (let i in data) {
                if ('Success' in data[i]) {
                    if ((data[i].Success.FlowChartJSON == null) || (data[i].Success.FlowChartJSON == '') || (data[i] == '')) {
                        if (data[i] == '') {
                            //data[i] = 
                           
                            app.graph.fromJSON(noFlowchartAddFoundJson);
                        }
                        else {
                            //  data[i].Success.FlowChartJSON = '{ "cells": [{ "size": { "width": 200, "height": 90 }, "angle": 0, "z": 1, "position": { "x": 225, "y": 230 }, "type": "basic.Rect", "attrs": { "rect": { "rx": 2, "ry": 2, "width": 50, "stroke-dasharray": "0", "stroke-width": 2, "fill": "#dcd7d7", "stroke": "#31d0c6", "height": 30 }, "text": { "font-weight": "Bold", "font-size": 11, "font-family": "Roboto Condensed", "text": "No FlowChart Available", "stroke-width": 0, "fill": "#222138" }, ".": { "data-tooltip-position-selector": ".joint-stencil", "data-tooltip-position": "left" } } }] }'
                            app.graph.fromJSON(JSON.parse(noFlowchartAddFoundJson));
                       
                        }
                    }
                    else {
                        // app.graph.fromJSON(JSON.parse(data[i].Success.FlowChartJSON));
                        isExperienced = data[i].Success.ExpertViewNeeded;
                        var changedJson = '';
                        let flowChartMode = data[i].Success.Mode;
                        changedJson = JSON.parse(data[i].Success.FlowChartJSON);

                        changedJson = convertElements(changedJson, flowChartMode)
                        $('#hiddenWfidBot').val(data[i].Success.WFID);
                        if (experiencedMode == "0"){
                            app.graph.fromJSON(changedJson);

                            // Set the initial flowchart json cell array for comparison with the resulting array
                            botSavingsConstants.oldJsonArr = JSON.parse(JSON.stringify(app.graph.getCells()));

                            // app.layoutDirectedGraph();
                            subActivityDefID = data[i].Success.SubActivityFlowChartDefID;
                            $('#oldDefID').val(subActivityDefID);
                            $('#currentWf').val(data[i].Success.WorkFlowName);
                            $('#currentWfId').val(data[i].Success.WFID);
                            subActivityDefIDArr.push(subActivityDefID);
                            $("#WFName").text(data[i].Success.WorkFlowName)                      
                            arrOfJsons['Assessed']=changedJson;
                            getLastVersionsOfFlowChart(data[i].Success.WorkFlowName);
                        }
                   
                        if(neneededforexecution == '1'){  $("#IsNEMandortoryCheck").prop('checked',true) }  
                        $('#SLAHours').val(slahours);
                        $('#ftr').val(ftr);
                        $("#editReasonDDBot").val(data[i].Success.WFEditReason);
                    }

                    //   resizeCell(app.paper.model);
                   
                }
                else {
                   
                    app.graph.fromJSON(JSON.parse(startStepJson));
                    arrOfJsons['Assessed']= JSON.parse(startStepJson);
                }
            }
            // maxID(max_id);

            deleteExistingRulesFromTempTable(version, subActivityDefIDArr);
        },
        complete:function(){
            pwIsf.removeLayer();
        },
        error: function (xhr, status, statusText) {
            var err = JSON.parse(xhr.responseText);
            pwIsf.alert({ msg: "error", type: 'error' });     
        }
    });
}

function btnSaveWorkFlow(projID, subID, version, saveOption, updateSameVersion = true, isFutureWorkOrdersUpdate = false) {
    let WFJsonArray = new Object();
    WFName = $('#WFName').text();
    var jsonToPass = '';
    let wfID = '';
    let editReason = '';
    wfID = $('#hiddenWfidBot').val();
    editReason = $('#editReasonDDBot').val();
    let KpiInputArr = [];
    WFNameArr = [WFName];
    jsonToPass = app.graph.toJSON();

    if (jsonToPass.cells.length != 0) {

        let WFType = ['Assessed'];
        if (updateSameVersion == false) { flagArr = false; }
        else { flagArr = true; }        
        WFJsonArray.botId = botID;
        WFJsonArray.projectID = projID;
        WFJsonArray.subActivityID = subID;
        WFJsonArray.versionNumber = version;
        WFJsonArray.saveMode = saveOption;
        WFJsonArray.futureWorkOrdersUpdate = isFutureWorkOrdersUpdate;
        WFJsonArray.signumID = signumGlobal;
        WFJsonArray.neMandatory = localStorage.getItem('isNeMandate');
        //WFJsonArray.enableField = IsAutoSenseEnabled;
        WFJsonArray.slaHours = localStorage.getItem('slaHrs');
        WFJsonArray.ftrValue = localStorage.getItem('ftr');
        WFJsonArray.experiencedFlow = localStorage.getItem('experiencedView');
        WFJsonArray.lstKpiModel = KpiInputArr;
        // WFJsonArray.wfOwner = $("#WFOwnerName").val();
        WFJsonArray.wfOwner = wfOwnerName;
        WFJsonArray.resetProficiency = false;
        WFJsonArray.flowChartDefID = subActivityDefID;
        WFJsonArray.update = flagArr;
        WFJsonArray.wfName = WFName;
        WFJsonArray.wfType = WFType[0];
        WFJsonArray.botapprovalPage = 'BOTAPPROVALVIEW';
        WFJsonArray.wfEditReason = editReason;
        WFJsonArray.workFlowID = wfID;
        WFJsonArray.loeMeasurementCriterionID = localStorage.getItem('loe');
        WFJsonArray.flowchartJSON = JSON.stringify(jsonToPass);
        saveJsonUI(WFJsonArray);  
    }
    else {
        pwIsf.alert({ msg: "Flow Chart cannot be empty", type: 'error' });

    }

}

    function validateHhMm(inputHours){
        // var isValid = /^([01]?[0-9]|7[0-2]).([5]*)?$/.test(inputHours.value);
        var isValid  = /^(7[0-2]|[0-9]|[0-6][0-9])(\.5)?$/.test(inputHours.value ); 
        if (isValid) {
            inputHours.style.border = '';
        } else {
            inputHours.style.border = '2px solid red';
        }

        return isValid;    
    }

    function saveJsonUI(WFJsonObject){
        window.pwIsf.addLayer({ text: "Please wait ..." });
   
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

                    if('Success' in data){
                        defIdArr=[];

                        defIdArr.push(data.Success.FlowChartDefID);
                        defIdArr.push(data.Success.Version);
                        $('#newDefId').val(defIdArr[0]);
                        $('#newVersion').val(defIdArr[1]);                        
                    }
                    
                  
                          
                    else {
                        app.graph.fromJSON(JSON.parse(data.Success.FlowChartJSON));
                    }
                       
                 
                    data.Success.bOTID = botID;
                    getBOTSummaryDetails(data.Success)                   
                  
                }
                else {
                    pwIsf.alert({ msg: data.ErrorMsg, type: 'error' }); 
                             
                }              
            },
            error: function (xhr, status, statusText) {
                pwIsf.alert({ msg: xhr.responseJSON.errorMessage, type: 'error' });
                //alert("No new data to be saved JSon already saved");

            },
            complete:function(){
                pwIsf.removeLayer();
           
            }
        });
    }


    
    
    
    function expectedToSavings(){    }

      

    



    let deletedOldStepsData={'deletedSteps':[]}
    

    $(document).on('select2:unselecting', ".oldStepDropDownOld", function(e){
        let currentSelectBox = $('.oldStepDropDown');   storeRedundantDeletedStepsArray = {'deletedSteps':[]};
        $.each(currentSelectBox, function(d,v){                  
                  
            for(let a = 0; a < currentSelectBox[d].selectedOptions.length; a++){
                let temp = currentSelectBox[d].selectedOptions[a].value.split('_');
                storeRedundantDeletedStepsArray.deletedSteps.push({'ExecutionType': temp[1], 'RpaID': temp[2], 'Saving': temp[3], 'StepName': currentSelectBox[d].selectedOptions[a].text, 'task':temp[4], 'StepID':temp[0]});
            }
                                 
        });
        unselected_value = [e.params.args.data.id.split('_')[0]];                    
        let temp = storeRedundantDeletedStepsArray.deletedSteps.filter(function(el) { return el.StepID == unselected_value[0]})

        if(temp.length >= 0){
            for (var i = storeRedundantDeletedStepsArray.deletedSteps.length - 1; i >= 0; --i) {
                if (storeRedundantDeletedStepsArray.deletedSteps[i].StepID == unselected_value[0]) {
                    storeRedundantDeletedStepsArray.deletedSteps.splice(i,1);
                    break;
                }
            }
        } 
        temp = storeRedundantDeletedStepsArray.deletedSteps.filter(function(el) { return el.StepID == unselected_value[0]});
        if(temp.length == 0 ) {
            let index = storeUniqueDeletedStepsArray.deletedSteps.findIndex(x => x.StepID === unselected_value[0])
            storeUniqueDeletedStepsArray.deletedSteps.splice(index,1);
        }
                  
    });

    

    

        function deleteAllBotDetails() {
            $('#wfVersionSelect').val($('#wfVersionSelect option:first').val());
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
                    pwIsf.alert({msg:"BOT not Deployed and new changes of workFlow is not saved", type:"info"})
                },
                complete: function () {
                    // $('#rpaIds').find("option").eq(0).remove();

                },
                error: function (xhr, status, statusText) {
                    pwIsf.alert({msd:"Failed to delete workflow ",type:"error"});
                }
            });
        }
    
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

                });
            },
            complete: function () { },
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

    function getRPAId(projectId) {
        $.isf.ajax({
            url: service_java_URL + "rpaController/getRPADeployedDetails/" + projectId,
            async: false,
            success: function (data) {
                rpaid = [];
                var html = "";            

                $.each(data, function (i, d) {
                    if (d !== null){                        
                        rpaid.push({ value: d.bOTName + '@' + d.automation_Plugin, content: d.automation_Plugin });
                    }
                });

                if(botID!='' || botID!=null) {                  
                    rpaid.unshift({value: botID + '@' + botName, content: botID + '-' + botName});
                }
            },
            complete: function () {},
            error: function (xhr, status, statusText) {
                alert("Failed to Get RPA Id ");
            }
        });
    }

    function getAllTools(){
       getTools();    
    }

    //MODAL ADD STEP FUNCTIONS

    function getTools() {
     
        $.isf.ajax({
            url: service_java_URL + "/toolInventory/getToolInventoryDetails",
            async:false,
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
            complete: function(){
                // return tools;
            }
        });
    }

    function getOnlyProjectSpecificTools() {
        getProjectSpecificTools();
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
                    //  return tools;
                }
            });
        }
        else {

            pwIsf.alert({ msg: "Project ID missing", type: "error" });
        }
        
    }

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
                    tasks.push({ value: result[i].taskID + '@' + result[i].masterTask, content:  result[i].masterTask  });
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

    //$(document).on('input', 'input.expectedSavingsText', function() {
    //    this.value = this.value.replace(/[^0-9.]/g, '').replace(/(\..*)\./g, '$1');
        //});
    function isNumberKey(evt)
    {
        var charCode = (evt.which) ? evt.which : evt.keyCode;
        if (charCode != 46 && charCode > 31 
          && (charCode < 48 || charCode > 57))
            return false;

        return true;
    }

    //collapse div when focus out.
    $(document).on('click','#app', function(){

        $('#accordionOne').addClass('collapse'); $('#accordionOne').removeClass('in');
    })

    function toggleIcon(e) {
        $(e.target)
            .prev('.panel-heading')
            .find(".more-less")
            .toggleClass('fa-plus fa-minus');
    }

    function flowChartOpenInNewWindow(url,windowName) {
        var width = window.innerWidth * 0.85;
        // define the height in
        var height = width * window.innerHeight / window.innerWidth;
        // Ratio the hight to the width as the user screen ratio
        window.open(url, windowName, 'width=' + width + ', height=' + height + ', top=' + ((window.innerHeight - height) / 2) + ', left=' + ((window.innerWidth - width) / 2))
    }

    function NumberOnly(control) {
        var node = $('#' + control.id);
        node.val(node.val().replace(/[^.0-9]/g, ''));
    }
      

   function highlightElement(stepid){
        let arrAllShapes = app.graph.toJSON();

        for(let i = 0 ; i < arrAllShapes.cells.length ; i++){
            if(arrAllShapes.cells[i].id == stepid){
                arrAllShapes.cells[i].attrs.body.fill='red';
                arrAllShapes.cells[i].attrs.bodyText.fill='white';
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
            // pwIsf.removeLayer();
        }
    });
    return response;
    //https://isfdevservices.internal.ericsson.com:8443/isf-rest-server-java_dev_major/rpaController/getRPAIsRunOnServer/02 
}