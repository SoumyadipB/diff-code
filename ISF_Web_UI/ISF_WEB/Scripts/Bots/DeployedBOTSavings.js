
let newSubDefID='';
let version = '';
let oldDefID = '';
//let botID = selectedRow[3];
let workFlowName = '';
let botJsonData = {};
let botJsonData1 = {};
let JsonStringArr =[];
let selectedNewStep=[];
let selectedOldStep=[]; 
let isSavingFlag="false";
let storeUniqueDeletedStepsArray = {'deletedSteps':[]};
let storeRedundantDeletedStepsArray = {'deletedSteps':[]};
let oldDropDownStepsOnVersionChange =[];

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
    

function getDeployedBotSavingDetails() {
    if ($.fn.dataTable.isDataTable('#tbBotBody')) {
        oTable.destroy();
        $("#tbSavingBody").empty();
    }

    pwIsf.addLayer({ text: 'Please wait ...' });
    $.isf.ajax({
        //url: service_java_URL +"rpaController/getBotIDByWFSignum",
        //url: service_java_URL + "botStore/getNewRequestListForRPAAdmin",
        url: service_java_URL + "flowchartController/getDeployedBotList/" + signumGlobal,
        context: this,
        crossdomain: true,
        processData: true,
        contentType: 'application/json',
        type: 'GET',
        xhrFields: {
            withCredentials: false
        },
        success: getDeployedBotSavingTable,

        error: function (xhr, status, statusText) {
            //console.log("Fail " + xhr.responseText);
            //console.log("status " + xhr.status);
            console.log('An error occurred');
        }
    });
}

function getDeployedBotSavingTable(data) {

    //console.log(data);

    if ($.fn.dataTable.isDataTable('#tbSavingBody')) {
        oTable.destroy();
        $('#tbSavingBody').empty();

    }
        pwIsf.removeLayer();
        botSavingData = data;

        oTable = $('#tbSavingBody').DataTable({
            searching: true,
            responsive: true,
            "pageLength": 10,
            "data": data,
            "destroy": true,
            colReorder: true,
            dom: 'Bfrtip',
            buttons: [
                'colvis', 'excelHtml5'
            ],
            "columns": [
                {
                    "title": "Action", "targets": 0, "orderable": false, "searchable": false, "data": null,
                    "defaultContent": "",
                    "render": function (data, type, row, meta) {
                        if (data.isCalculationNeeded != 1) {
                            let details = JSON.stringify({ wfid: data.WFID, subDefID: data.SubActivityFlowChartDefID, versionNumber: data.VersionNumber, oldWFDefID: data.OldWfDefID, botid: data.RpaID, WorkFlowName: data.WorkFlowName, ProjectID: data.ProjectID, SubActivityID: data.SubActivityID, BotStepID: data.BotStepId, StepID: data.StepID, CurrentYearSavings: data.CurrentYearSavings});
                            return '<input type="radio" class="select-botid" name="botsaving" data-details=\'' + details + '\' />';
                        }
                    }
                },
                { "title": "Project Id", "data": "ProjectID" },                
                { "title": "Project", "data": "ProjectName", "defaultContent": "NA" },
                { "title": "MA", "data": "MarketAreaName", "defaultContent": "NA" },
                { "title": "BOT ID", "data": "RpaID", "defaultContent": "NA" },
                {
                    "title": "Integration Date", "data": "BotIntegrationDate", "defaultContent": "NA",
                   "render": function (data, type, row, meta) {
                       var botIntegrationDate = (new Date(row.BotIntegrationDate)).toISOString().split('T')[0];
                       return botIntegrationDate;
                       //return (new Date(row.endDate)).toLocaleDateString();
                    }},
                { "title": "BOT Activation Date", "data": "BotActivationDate", "defaultContent": "NA" },
                { "title": "Work Flow ID", "data": "SubActivityFlowChartDefID", "defaultContent": "NA" },
                { "title": "Activity Name", "data": "Activity", "defaultContent": "NA" },
                { "title": "Sub Activity Name", "data": "SubActivity", "defaultContent": "NA" },
                { "title": "Task Name", "data": "Task", "defaultContent": "NA" },
                { "title": "Step Name (Automated)", "data": "StepName", "defaultContent": "NA" },
                { "title": "Pre Manual Hours", "data": "preManualHour", "defaultContent": "NA" },
                { "title": "Post Manual Hours", "data": "postManualHour", "defaultContent": "NA" },
                { "title": "Savings", "data": "Savings", "defaultContent": "NA" },
                { "title": "Equivalent Manual Hrs (EME)", "data": "Savings", "defaultContent": "NA" },
                { "title": "Saving Remarks", "data": "Remarks", "defaultContent": "NA" },
               { "title": "Current Year Savings", "data": "CurrentYearSavings", "defaultContent": "YES" },

            ],
            ////"columnDefs": [
            ////{
            ////    "targets": [ 17 ],
            ////    "visible": false,
            ////    "searchable": false
            ////},
            //],
            initComplete: function () {

                $('#tbSavingBody tfoot th').each(function (i) {
                    var title = $('#tbSavingBody thead th').eq($(this).index()).text();
                    if (title != "Action")
                        $(this).html('<input type="text" class="form-control" style="font-size:12px;" placeholder="Search ' + title + '" data-index="' + i + '" />');
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
        
        $('#tbSavingBody tfoot').insertAfter($('#tbSavingBody thead'));

}

$(document).on('click', 'input[name=botsaving]', function(){
    $('#btneditParentMapping').prop('disabled', false);
    $('#btneditPostPeriod').prop('disabled', false);    
});

$(document).on('change', '#wfVersionSelectOld', function() {

    storeUniqueDeletedStepsArray = {'deletedSteps':[]};
    storeRedundantDeletedStepsArray = {'deletedSteps':[]};
    let selectedVersion = $('#wfVersionSelection').val().split('_')[0];
    let rowWfID = $('#wfVersionSelection').val().split('_')[2];
    // $('#deletedStepWarning').hide();
    $.isf.ajax({
        type:"GET",
      
        url: service_java_URL + "flowchartController/getFlowChartByDefID/" + subID + "/" + selectedVersion + "/" + projId + "/" + rowWfID,
        success: function (resp) { 
            oldDropDownStepsOnVersionChange=[];
         
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
 
});





$(document).on('click', '#btneditParentMapping', function () {
   // let botSavingJson = Object();
    $('#botDetailModal').modal('show');
    let selectedRow = $('input[name=botsaving]:checked').data('details');
    wfid = selectedRow.wfid;
    newSubDefID = selectedRow.subDefID;
    version = selectedRow.versionNumber;
    oldDefID = selectedRow.oldWFDefID;
    botID = selectedRow.botid;
    workFlowName = selectedRow.WorkFlowName;
    projId = selectedRow.ProjectID;
    subID = selectedRow.SubActivityID;
    botForStepID = selectedRow.BotStepID;
    subActivityDefID = newSubDefID;


    botJsonData.newDefID = newSubDefID;
    botJsonData.newVersion = version;
    botJsonData.oldDefID = oldDefID;
    botJsonData.bOTID = botID;

    $('#newDefId').val(botJsonData.newDefID);
    $('#newVersion').val(botJsonData.newVersion);
    $('#oldDefID').val(subActivityDefID);
    $('#currentWf').val(workFlowName);
    $('#currentWfId').val(wfid);

    let botData = {
        FlowChartDefID: newSubDefID,
        Version: version,
        OldDefID: oldDefID,
        bOTID: botID
    }

    getBOTSummaryDetails(botData);


   
    //pwIsf.addLayer({ text: 'Please wait ...' });
    //$.isf.ajax({

    //    type: "POST",
    //    //  async:false,
    //    contentType: 'application/json',
    //    data: JSON.stringify(botJsonData),
    //    url: service_java_URL + "flowchartController/getAndSaveBotSavingSummary",

    //    xhrFields: {
    //        withCredentials: false
    //    },
    //    success: function(response){

    //        storeUniqueDeletedStepsArray = {'deletedSteps':[]};
    //        storeRedundantDeletedStepsArray = {'deletedSteps':[]};
    //        getBOTSummaryTables(response);
           
    //        },
    //    error: function (xhr, status, statusText) {
    //        console.log('An error occurred');
    //    }
    //});
     getLastVersionsOfFlowChart(workFlowName);
});

//function getLastVersionsOfFlowChart(workFlowName){
//    let WFObject = new Object();
//    WFObject.projectID = projId;
//    WFObject.subActivity = subID;
//    WFObject.versionCount = "2";
//    WFObject.wfName = workFlowName;
//    WFObject.assessedVersion = version; 
//    versionArray = [];
//    $.isf.ajax({
//        type:"POST",
//        contentType: 'application/json',
//        data:JSON.stringify(WFObject),
//        url: service_java_URL + "flowchartController/getVersionsFromSubactivityID",
//        success: function (resp) {
//            versionArray = resp;
//            $('#wfVersionSelect').html('')
//            $('#wfVersionSelect').append('<option value="-1" disabled selected>--Select One--</option>')
//            $.each(resp,function(i,d){               
//                $('#wfVersionSelect').append('<option value="' + d.VersionNumber + '_' +d.SubActivityFlowChartDefID + '">' + d.VersionNumber + '</option>')
//            })
//            $('#wfVersionSelect').append('<option value="' + version + '_' + subActivityDefID + '">' + version + '</option>')
//        },
//        error:function (xhr, status, statusText) {
//            pwIsf.alert({ msg: "error", type: 'error' });
//        }
//    });
//}


    

    let deletedOldStepsData={'deletedSteps':[]}
    

    $(document).on('select2:unselecting', ".oldStepDropDownOld", function(e){
        let currentSelectBox = $('.oldStepDropDown');   storeRedundantDeletedStepsArray = {'deletedSteps':[]};
        $.each(currentSelectBox, function(d,v){                  
                  
            for(let a = 0; a < currentSelectBox[d].selectedOptions.length; a++){
                let temp = currentSelectBox[d].selectedOptions[a].value.split('_');
                storeRedundantDeletedStepsArray.deletedSteps.push({'ExecutionType': temp[1], 'RpaID': temp[2], 'Savings': temp[3], 'StepName': currentSelectBox[d].selectedOptions[a].text, 'task':temp[4], 'StepID':temp[0]});
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

    $(document).on('click', '#saveBotDetailsBtnOld', function () {
    
        let btnclass= $(".calculateSavings"); let flag = false;
        for(var a=0; a < btnclass.length; a++){
            if(!$($(btnclass)[a]).prop('disabled')){flag=true;}
            else{flag=false; break;}
        }
        if(flag){
           // let currentYrSaving = $('#currentYrSaving').val();
            let tableLength = $('#botSavingTable').dataTable().fnGetData().length;
            let allBotDetailsObj = new Object(); let botJsonArray=[];
            let botSavingRows=$('#botSavingTable').dataTable().fnGetNodes();
            JsonStringArr =[];
    
            for(let i=0; i<tableLength;i++){
                allBotDetailsObj = {};
                allBotDetailsObj.projectID = projId;
                allBotDetailsObj.subActivity = subID;
                allBotDetailsObj.bOTID = $($(botSavingRows)[i]).find('td:eq(0)')[0].innerText;
                allBotDetailsObj.bOTStepID = botForStepID;
                allBotDetailsObj.oldDefID =  botJsonData.oldDefID
                allBotDetailsObj.newDefID = botJsonData.newDefID;       
                allBotDetailsObj.createdBy = localStorage.getItem('BotCreatedBy');
                allBotDetailsObj.createdOn = localStorage.getItem('BotCreatedOn');
                let currRowButtonData = $($(botSavingRows)[i]).find('td:eq(6)').find('#calculateSavings');
                allBotDetailsObj.totalSavings = $(currRowButtonData).data('saving');
                allBotDetailsObj.expectedSavings = $(currRowButtonData).data('expectedSavings');
                allBotDetailsObj.lastModifiedBy = signumGlobal;            
                allBotDetailsObj.lastModifiedOn = new Date().toJSON().slice(0,10)              
                allBotDetailsObj.oldSteps =  $(currRowButtonData).data('selectedOldStep');
                allBotDetailsObj.newSteps = $(currRowButtonData).data('selectedNewStep');
                allBotDetailsObj.remark = $(currRowButtonData).data('Remark');
                allBotDetailsObj.isSavingFlag = $(currRowButtonData).data('isSavingFlag');
                allBotDetailsObj.currentYearSavings =  $(currRowButtonData).data('currentSavingYr');

                JsonStringArr.push(allBotDetailsObj);
            } 
            $('#botDetailModal').modal('hide');
        }
        else{
            pwIsf.alert({msg:"Please calculate Savings for all BOTS", type:"error"})
        }
    });


    function getPostPeriod() {
       
    $('#postPeriodModal').modal('show');
    
}

function saveBOTPostPeriod() {
    if ($("#start_date").val() == null || $("#start_date").val() == "") {
        showErrorMsg('start_date-Required', 'Start date is required');
    }
    else if ($("#end_date").val() == null || $("#end_date").val() == "") {
        showErrorMsg('end_date-Required', 'End date is required');
    }
    else if (JsonStringArr.length == 0) {
        pwIsf.alert({ msg: 'Please do the mapping for BOTs first', type: 'error' });
    }
    else {
        $('#start_date-Required').hide();
        $('#end_date-Required').hide();
        let startDate = new Date($('#start_date').val());
        let endDate = new Date($('#end_date').val());
        if (startDate > endDate) {
            $('#end_date').val("");
            showErrorMsg('end_date-Required', 'End date must be after start date');
        }
        else{

        
            //  let startDate = $("#start_date").val();
            //  let endDate = $("#end_date").val();

            $('#btnRecalcEME').prop('disabled', false);
            $('#postPeriodModal').modal('hide');

            let selectedRow1 = $('input[name=botsaving]:checked').data('details');
            newSubDefID = selectedRow1.subDefID;
            version = selectedRow1.versionNumber;
            oldDefID = selectedRow1.oldWFDefID;
            botID = selectedRow1.botid;
            workFlowName = selectedRow1.WorkFlowName;
            projId = selectedRow1.ProjectID;
            subID = selectedRow1.SubActivityID;
            botForStepID = selectedRow1.BotStepID;
            subActivityDefID = newSubDefID;
            bOTStepID = selectedRow1.StepID

            botJsonData1.newDefID = newSubDefID;
            botJsonData1.newVersion = version;
            botJsonData1.oldDefID = oldDefID;
            botJsonData1.bOTID = botID;
            botJsonData1.bOTStepID = bOTStepID;
            botJsonData1.postPeriodStartDate = startDate;
            botJsonData1.postPeriodEndDate = endDate;

            pwIsf.addLayer({ text: 'Please wait ...' });
            $.isf.ajax({

                type: "POST",

                contentType: 'application/json',
                data: JSON.stringify(botJsonData1),
                url: service_java_URL + "flowchartController/updatePostPeriodDates",

                xhrFields: {
                    withCredentials: false
                },
                success: function (response) {
                    pwIsf.alert({ msg: "Dates Updated", type: "success" });
                    pwIsf.removeLayer();

                },
                error: function (xhr, status, statusText) {
                    console.log('An error occurred');
                    pwIsf.alert({ msg: "Dates Update failed", type: "error" });
                    pwIsf.removeLayer();
                }
            });
            //getLastVersionsOfFlowChart(workFlowName);

        }
    }
}


function recalculateEME(){
    pwIsf.addLayer({ text: 'Please wait ...' });
    var dateCheck = formatted_date(new Date());
    var signumCheck = signumGlobal;
    var remarkforSave = JsonStringArr[0].remark;
    if (remarkforSave.includes(dateCheck) && remarkforSave.includes(signumCheck)) {
        JsonStringArr[0].remark = `The EME was recalculated, by ISF.${remarkforSave}`;
    }
    else if (remarkforSave.includes(dateCheck)) {
        JsonStringArr[0].remark = `${C_RECALCULATE_EME} ${signumGlobal}.${remarkforSave}`;
    }
    else if (remarkforSave.includes(signumCheck)) {
        JsonStringArr[0].remark = `The EME was recalculated, by ISF, on ${formatted_date(new Date())}.${remarkforSave}`;
    }
    else {
        JsonStringArr[0].remark = `${C_RECALCULATE_EME} ${signumGlobal}, on ${formatted_date(new Date())}.${remarkforSave}`;
    }
    $.isf.ajax({
        type: "POST",               
        contentType: 'application/json',
        data: JSON.stringify(JsonStringArr),
        url: service_java_URL + "flowchartController/saveBotSavingDetailsAndHistory",       
        success: function (data) {
            pwIsf.alert({msg:"Saving Saved",type:"info", autoClose:2});
        },
        complete: function () {
            pwIsf.removeLayer();
            $('#botDetailModal').modal('hide');
            location.reload();
       

        },
        error: function (xhr, status, statusText) {
            pwIsf.alert({msd:"Failed to Deploy Bot ",type:"error"});
        }
    });
}

function  flowChartOpenInNewWindow(url) {
    var width = window.innerWidth * 0.85;
    // define the height in
    var height = width * window.innerHeight / window.innerWidth;
    // Ratio the hight to the width as the user screen ratio
    window.open(url + '&commentCategory=WO_LEVEL', 'newwindow', 'width=' + width + ', height=' + height + ', top=' + ((window.innerHeight - height) / 2) + ', left=' + ((window.innerWidth - width) / 2));
}



