var flagWOFullScreen = false;
var flagFlowChartFullScreen = false;
var runningTaskDetails = '';
var progressWorkOrderDetails = '';
var replaceNullBy = '-';
let expertFlowChartName = 'projectdefined_expert';
let selectedTabHref;
var global_marketsOfWO = [];
var IsNotficationNavigation = false;
var isFloatingWindow = false;
var woIDFW = "";

//START - SOME COMMON FUNCTIONS

function getWOpercentageAndPerTextFromAPI(passParam ) {
    let defaults = {
        async: true,
        progressBarFor: ''
    };


    let actualParam = $.extend({}, defaults, passParam || {});
  // let ifTimeOut = '';

    let woId = actualParam.woId;

    let woDetails = $('#viewBtn_' + woId).data('details');
    let toggleButtonObj = $('#togBtnForFlowChartViewMode_' + woId);
    let proficiencyLevel;
    if (toggleButtonObj.length != 0) {
        proficiencyLevel = $('#togBtnForFlowChartViewMode_' + woId).is(":checked") ? 2 : 1;
    }
    else {
        proficiencyLevel = $('#viewBtn_' + woId).data('details').proficiencyLevel;
    }
    let woStatus = woDetails.status;

    if (proficiencyLevel == 1 /*&& woStatus.toLowerCase() == 'inprogress'*/) {
        let ajaxRequest = $.isf.ajax({
            url: service_java_URL + "woExecution/getWorkOrderProgress?workOrderId=" + woId,
            returnAjaxObj: true,
            async: actualParam.async
        });

        ajaxRequest.done(function (data) {

            //let testData = data.responseData.isTimeout;
            //if (testData === "true") {
            //    progressBarHtml = '';
            //    $(`#progressBarOnFlowchartHeader_${woId}`).hide();


            //}
            getWOprogressBarHtml({ percentage: data.responseData.completedPercentage, perText: data.responseData.progressDescription, woId: woId, progressBarFor: actualParam.progressBarFor });
        });
        ajaxRequest.fail(function () {
        });
        ajaxRequest.always(function () {

        });
        let returnOutput = { percentage: '30', perText: 'Percentage text' };

        return returnOutput;
    }

    

}

function getWOprogressBarHtml(passParam = {}) {
    let percentage = passParam.percentage;
    let perText = passParam.perText;
    let woId = passParam.woId;
    let progressBarFor = passParam.progressBarFor;
    let woIdForDetails = passParam.woIdForDetails

    let woDetails = woIdForDetails ? $('#viewBtn_' + woIdForDetails).data('details') : $('#viewBtn_' + woId).data('details');
    let proficiencyLevel;
    let toggleButtonObj = $('#togBtnForFlowChartViewMode_' + woId);

    if (passParam.isExperienced != undefined && passParam.isExperienced != null ) {
        proficiencyLevel = passParam.isExperienced ? 2 : 1;
    }
    else if (toggleButtonObj.length != 0) {
        proficiencyLevel = $('#togBtnForFlowChartViewMode_' + woId).is(":checked") ? 2 : 1;
    }
    else {
        proficiencyLevel = $('#viewBtn_' + woId).data('details').proficiencyLevel;
    }
    

    
    let woStatus = woDetails.status;
    let returnOutput = ``;

    if (proficiencyLevel == 1 /*&& woStatus.toLowerCase() == 'inprogress'*/) {

        perText = `${percentage}% Completed`;
        

        let woPanelProgress = `<div class="myWorkOrderPanel progress" title="${perText}"> 
                       <div data-percentage="${percentage}" data-percentageText="${perText}" class="progress-bar progress-bar-success" role="progressbar" style="width:${percentage}%">
                       </div>
                      
                   </div>`;

        let flowchartPanelHeaderProgress = `<div class="progress" title="${perText}"> 
                       <div class="progress-bar progress-bar-success" role="progressbar" style="width:${percentage}%">${percentage}%
                       </div>
                      
                   </div>`;

        if (progressBarFor === 'myWOPanel') {
            return woPanelProgress;
        }
        if (progressBarFor === 'flowchartHeader') {
            return flowchartPanelHeaderProgress;
        }

        if (progressBarFor === '' && woId != 0) {

            if ($(`#myWorkOrderProgressBarArea_${woId}`)) {
                $(`#myWorkOrderProgressBarArea_${woId}`).html(woPanelProgress);
            }
            if ($(`#progressBarOnFlowchartHeader_${woId}`)) {
                $(`#progressBarOnFlowchartHeader_${woId}`).html(flowchartPanelHeaderProgress);
            }

        }
        
    }

    return returnOutput;

}

function resetbtnWhichHasI(obj) {
    var that = obj.targetObj;
    var hasClassName = obj.hasClassName;
    var newClass = obj.newClass;
    var title = obj.title;

    var iObj = $(that).find('i');
    if (iObj.hasClass(hasClassName)) {
        iObj.removeClass(hasClassName).addClass(newClass);
        $(that).attr('title', title);
    }
}

function expandAndCollapse(passObj) {

    var that = passObj.that;
    var selector = passObj.targetSelector;
    var maxHeight = passObj.maxHeight;
    var middHeight = passObj.middHeight;
    var minHeight = passObj.minHeight;
    var conditionTarget = passObj.conditionTarget; // We can use this var for a particular condition 

    if ($(that).find('i').hasClass('fa-angle-down')) {
        //selector.animate({ 'height': maxHeight }, function () { selector.css({ 'overflow-y': 'auto' }); });
        selector.css({ 'height': maxHeight, 'overflow-y': 'auto' });
        $(that).find('i').removeClass('fa-angle-down').addClass('fa-angle-up');
    } else if ($(that).find('i').hasClass('fa-angle-up')) {
        //selector.animate({ 'height': middHeight }, function () { selector.css({ 'overflow': 'hidden' }); });
        selector.css({ 'height': middHeight, 'overflow': 'hidden' });
        $(that).find('i').removeClass('fa-angle-up').addClass('fa-angle-down');
    }
}

function makeItFullScreen(thisObj, targetObj) {
    var that = thisObj;
    var iObg = $(that).find('i');

    if (iObg.hasClass('fa-expand')) {
        targetObj.addClass('make_full_screen');
        iObg.removeClass('fa-expand').addClass('fa-compress');
        $(that).attr('title', 'Exit from full screen');
        return true;
    } else {
        targetObj.removeClass('make_full_screen');
        iObg.removeClass('fa-compress').addClass('fa-expand');
        $(that).attr('title', 'Full screen');
        return false;
    }
}

function searchInDiv(searchObj, callBacksObj) { //A common function for searching in a div 

    var searchIn = $(searchObj.searchInTarget);
    searchIn.each(function (i, val) {

        var allText = $(val).text().toLowerCase();
        var inputText = searchObj.textToBeSearch;
        if (inputText != '') {
            $('.notFound').remove();
        }
        else {
            $('.notFound').remove();
        }

        var ar = inputText.split(",");
        var condition = '';
        for (var j in ar) {
            condition += "allText.indexOf('" + ar[j] + "') == -1 || ";
        }
        condition = condition.substr(0, condition.length - 3);

        if (eval(condition)) {
            searchIn.eq(i).hide();
        }
        else {
            $('.notFound').remove();
            searchIn.eq(i).css('display', '');
        }


    });

    if (callBacksObj.callBack) {
        $(callBacksObj.mainContainer).trigger(callBacksObj.callBackFunction);
    }
    if (searchIn.children(':visible').length == 0) {
        $(callBacksObj.mainContainer).append('<div class="notFound">Oooops</div>');

    }
}

//END - SOME COMMON FUNCTIONS


//Start - a common function to break single line into chunks
function breakTooltipContent(yourString, lineLength) {
    let argRegEx = new RegExp('.{1,' + lineLength + '}', 'g');
    return yourString.match(argRegEx).join('\n');
}
//End - a common function to break single line into chunks

function afterLoadingFlowchart(param) {
    pwIsf.removeLayer();

}

function assessedOrExperienced(obj, iframeSrc, iframeId, proficiencyId) {
    let woId = iframeId.split('_')[1];
    if ($('#openFeedbackModal_' + woId).length > 0) {
        $('#openFeedbackModal_' + woId).remove();
    }
    if ($('#openStepLevelFeedbackModal_' + woId).length > 0) {
        $('#openStepLevelFeedbackModal_' + woId).remove();
    }

    $('.feedback_' + woId).attr('disabled', false);


    let that = obj;
    let chkBoxId = that.id;

    var checkbox = document.querySelector('input[id="' + chkBoxId + '"]');
    let experiencedParam = '';
    //experiencedParam = (checkbox.checked) ? '&experienced=1&isExperiencedChecked=1' : '&experienced=0&isExperiencedChecked=0';
    let instanceId = proficiencyId.slice(0, -1);
    experiencedParam = (checkbox.checked) ? '&proficiencyLevel=2&proficiencyId=' + instanceId + '2' : '&proficiencyLevel=1&proficiencyId=' + instanceId + '1';

    if (checkbox.checked) {
        $('#progressBarOnFlowchartHeader_' + woId).hide();
        $('#woButtons_' + woId).show();
        $('#myWorkOrderProgressBarArea_' + woId).hide();
    }
    else {
        $('#progressBarOnFlowchartHeader_' + woId).show();
        $('#woButtons_' + woId).hide();
        $('#myWorkOrderProgressBarArea_' + woId).show();
        getWOprogressBarHtml({ percentage: '0', perText: '', woId: woId, progressBarFor: '' });
    }

    let iframeSrcTemp = '';

    if (iframeSrc.search('isExperiencedChecked') != -1) {
        iframeSrcTemp = iframeSrc.split('&isExperiencedChecked');
        //iframeSrc = iframeSrcTemp[0];
        iframeSrc = iframeSrcTemp[0] + experiencedParam;
    }
    else {
        iframeSrc = iframeSrc + experiencedParam;
    }
    //iframeSrc = iframeSrc + '&status=' + that.status;
    pwIsf.addLayer({text:"Please wait ..."});
    $('#' + iframeId).load(function () {
        pwIsf.removeLayer();
    });
    $('#' + iframeId).attr('src', iframeSrc);

}

function createFlowchartPanel(param, callback) {

    pwIsf.addLayer({ text: "Loading flow chart please wait ..." });

    var woid = param.woid;
    var projId = param.projectID;
    var subActId = param.subActId;
    var status = param.status;
    var vNum = param.vNum;
    var wFOwner = param.wfowner;
    var iframeSrc = '';
    let iframeCoreSrc = '';
    let tooltipContent;
    let nodeName = param.nodeNames;
    if (nodeName != null && nodeName != undefined && nodeName != "") {
        nodeNamesArr = nodeName.split(',');
        nodeNamesUnique = jQuery.unique(nodeNamesArr.sort()).join(',');
        tooltipContent = breakTooltipContent(nodeNamesUnique, 40);
        nodeName = nodeNamesUnique;
    }
    let experienced = param.experiencedMode;
    let wfName = param.wfName;
    let wfid = param.wfid;
    let flowChartType = (param.flowChartType) ? jQuery.trim(param.flowChartType.toLowerCase()) : '';

    let experiencedParamForUrl = '';
    var subActivityFlowChartDefID = param.subActivityFlowChartDefID;
    var iframeId = "iframe_" + woid;
    var flowchartBoxDetails = JSON.stringify({ woid: woid, projectID: projId, flowChartType: flowChartType });

    let feedbackDetails = JSON.stringify({ woid: woid, projectID: projId, versionNo: vNum, wfid: wfid, wfName: wfName, subActivityFlowChartDefID: param.subActivityFlowChartDefID, flowChartType: flowChartType });

    let woPercentage = param.woPercentage;
    let woPercentageText = param.woPercentageText;

    let workOrderAutoSenseEnabled = param.workOrderAutoSenseEnabled;

    let proficiencyLevel = param.proficiencyLevel == 2 ? (workOrderAutoSenseEnabled == false ? param.proficiencyLevel : 1) : param.proficiencyLevel;
    let instanceId = param.proficiencyId.toString().slice(0, -1);
    let proficiencyId = parseInt(instanceId + proficiencyLevel);
    let proficiencyParamForUrl = '';


    let colClass = 'col-md-12';
    let coloumnIcon = $('#changeColumns').find('i');
    if (coloumnIcon.hasClass('fa-th-large')) {
        colClass = 'col-md-12';
    } else {
        colClass = 'col-md-6';
    }

    //Remove flowchart from DOM if exist
    $('.flow_chart_main_panel .panel-body > .flow_chart_single_box').each(function (i, flowchartBox) {

        if ($(flowchartBox).data('details').woid == woid) {
            this.remove();
        }

    });


    let nodeNameHtml = '';
    if (jQuery.trim(nodeName).length >= 40) {
        tooltipContent = breakTooltipContent(nodeName, 40);
        let node = nodeName.substring(0, 40) + "...";
        nodeNameHtml = `<span class="nodeNameP protip" id="nodeFieldHeaderWO_${woid}" style="width:auto;max-width:23%;"
        data-pt-scheme="aqua" data-pt-position="bottom" data-pt-skin="square" data-pt-width="400"
        data-pt-title="${tooltipContent}"> [<b>${shortNeNameAsNEID}:</b>  ${node}</span>]`;
    }
    else if (jQuery.trim(nodeName).length < 40) {
        if (nodeName != null && nodeName != undefined && nodeName != "" && nodeName.length != 0) {
            tooltipContent = breakTooltipContent(nodeName, 40);
            nodeNameHtml = `<span class="nodeNameP protip" id="nodeFieldHeaderWO_${woid}" style="width:auto;max-width:23%;"
            data-pt-scheme="aqua" data-pt-position="bottom" data-pt-skin="square" data-pt-width="400"
            data-pt-title="${tooltipContent}"> [<b>${shortNeNameAsNEID}:</b>  ${nodeName}</span>]`;
        }
    }
    else {
        nodeNameHtml = `<span class="nodeNameP protip" id="nodeFieldHeaderWO_${woid}" style="width:auto;max-width:23%;"
        data-pt-scheme="aqua" data-pt-position="bottom" data-pt-skin="square" data-pt-width="400"
        data-pt-title="-"> [<b>${shortNeNameAsNEID}:</b> - </span>]`;
    }

    // Params for btn
    let assessedDisableBg = '';
    let chkDisabled = '';
    let experiencedTitle = 'Change flowchart version for Experienced/Assessed';

    //let chkBoxExperiencedCheckedOrNot = (flowChartType == expertFlowChartName) ? 'checked' : '';
    let chkBoxExperiencedCheckedOrNot = proficiencyLevel == 2 ? 'checked' : '';
    let isExperiencedChecked = (chkBoxExperiencedCheckedOrNot == 'checked') ? 1 : 0;
    

    if (status != 'ASSIGNED' || workOrderAutoSenseEnabled == true) {
        chkDisabled = 'disabled';
        assessedDisableBg = 'disableAssessedBg';
        // experiencedTitle = 'You are not eligible for experienced version';
    }

    //commented by komal
    //  if (status != 'ASSIGNED') {
    // chkDisabled = 'disabled';
    experiencedTitle = 'You are working on this version';
    experiencedParamForUrl = (isExperiencedChecked) ? '&experienced=1' : '&experienced=0';
    proficiencyParamForUrl = '&proficiencyLevel=' + proficiencyLevel + '&proficiencyId=' + proficiencyId;
    //} else {
    //   experiencedParamForUrl = '&experienced=0';// set it to zero if status is ASSIGNED
    //}

    // Prepare iframe source for flowchart
    if (status == "DEFERRED") {
        iframeCoreSrc = window.location.href.split("WorkorderAndTask")[0] + 'FlowChartView?mode=view&version=' + vNum + '&woID=' + woid + '&subActID=' + subActId + '&prjID=' + projId + '&wfid=' + wfid + '&commentCategory=WO_LEVEL' + '&flowchartType=' + flowChartType;
        iframeSrc = iframeCoreSrc + '&status=' + param.status + '&isExperiencedChecked=' + isExperiencedChecked;
    }
    else {
        iframeCoreSrc = window.location.href.split("WorkorderAndTask")[0] + 'FlowChartExecution?mode=execute&version=' + vNum + '&woID=' + woid + '&subActID=' + subActId + '&prjID=' + projId + '&wfid=' + wfid + '&commentCategory=WO_LEVEL' + '&flowchartType=' + flowChartType;
        iframeSrc = iframeCoreSrc + '&status=' + param.status + '&isExperiencedChecked=' + isExperiencedChecked;

        setIFrameURL(iframeSrc);
    }


    // start - Qualified button 

    //New Approach
    if (proficiencyLevel == 1) {
        chkDisabled = 'disabled';
        assessedDisableBg = 'disableAssessedBg';
        experiencedTitle = 'You are not eligible for experienced version';
    }

    //Old Approach
    //if (!experienced) {
    //    chkDisabled = 'disabled';
    //    assessedDisableBg = 'disableAssessedBg';
    //    experiencedTitle = 'You are not eligible for experienced version';
    //}

    // ADD PROGRESS BAR HTML IN HEADER

    let progressBarHtml = (isExperiencedChecked) ? '<div id="progressBarOnFlowchartHeader_' + woid + '" style="margin-left:0px;width:20%;display:none"></div>' : '<div id="progressBarOnFlowchartHeader_' + woid + '" style="margin-left:0px;width:20%;">' + getWOprogressBarHtml({ percentage: woPercentage, perText: woPercentageText, woId: 0, progressBarFor: 'flowchartHeader', isExperienced: isExperiencedChecked, woIdForDetails: woid }) + '</div>';

    let experiencedHtml = '<label class="switchSource" title="' + experiencedTitle + '"><input type="checkbox"  ' + chkDisabled + ' class="toggleActive" onclick="assessedOrExperienced(this,\'' + iframeSrc + '\',\'' + iframeId + '\', \'' + proficiencyId + '\')" id="togBtnForFlowChartViewMode_' + woid + '" ' + chkBoxExperiencedCheckedOrNot + '><div id="divTogBtnForFlowChartViewMode" class="sliderSource round ' + assessedDisableBg + '"><span class="onSource">Experienced</span><span class="offSource">Assessed</span></div></label>';

    let inputFilesHtml = '<div class="filesDiv dropdown" style="height: 50%;">'
        + '<a href="#" class="btn btn-xs btn-success" role="button" style="text-decoration:none;width:100%" title="Input URLs">Input URLs'
        + '</a>'
        + '<ul id="inputFile_' + woid + '" class="dropdown-menu" style="top: 99%;">'
        + getInputFilesFlowChart(woid)
        + '</ul>'
        + '</div>';
    let outputFilesHtml = '<div class="filesDiv dropdown" style="height: 50%;">'
        + '<a onclick="getOutputFilesFlowChartDE(' + woid + ',' + projId + '); getLocalGlobalURLs(\'OpenOutputFilesModal\',' + projId + ')" data-toggle="modal" data-target="#OpenOutputFilesModal" href="#" class="btn btn-xs btn-primary" role="button" style="text-decoration:none;width:100%" title="Output URLs">Output URLs'
        + '</a>'
        + '</div>';

    //Feedback Comments 
    let workFlowFeedbackHTML = "";
    if (flowChartType != "custom") {
        workFlowFeedbackHTML = '<div class="feedbackDiv_' + woid + '"><a href="javascript:void(0)" class="btn btn-primary btn-xs feedback_' + woid + '" role = "button" title="Feedback" onclick="createFeedbackModal(this)" data-feedbackdetails =\'' + feedbackDetails + '\'> ' +
            '<i class="fa fa-comment" aria-hidden="true"></i>' +
            '</a></div>';
    }


    let wiUrlhtml = "";
    wiUrlhtml = '<div style="position:relative; bottom:2px;"><button   id=inst_' + subActivityFlowChartDefID + ' onclick="showInst(this)" data-feedbackdetails =\'' + feedbackDetails + '\'' + 'class="btn btn-xs btn-primary dropdown-toggle" style="padding:3px; position: reletive; bottom:3px;" title="WI URL" data-toggle="dropdown">WI URL</button><ul class="dropdown-menu" id="wf_instruction" style="z-index:auto; "></ul ></div>'
    // end - Experienced button
    uniquePageID = "WORK_ORDER_" + woid;
    startPageComments = 0;
    commentsCount = 5;

    let woButtonsForExperiencedWF = "";
    woButtonsForExperiencedWF = '<div class="experiencedWOButtons" id="woButtons_' + woid + '">' + 
        '<input class="disabledStepObj" type="hidden"> ' +
        '<i class="fa fa-play woActionButtons startExperiencedWO disabledWOButton" data-woid=' + woid + ' style="color:black;padding-left:4px;" title="Start"></i>' + '&nbsp;' + '&nbsp;' +
        '<i class="fa fa-pause woActionButtons fa-sm pauseExperiencedWO disabledWOButton" data-woid=' + woid + ' style="color:black;" title="Pause"></i>' + '&nbsp;' + '&nbsp;' +
        '<i class="fa fa-stop woActionButtons fa-sm stopExperiencedWO disabledWOButton" data-woid=' + woid + ' style="color:red;" title="Stop"></i>' + '&nbsp;' + '&nbsp;' +
        '<i class="fa fa-step-forward woActionButtons nextExperiencedStep disabledWOButton" data-woid=' + woid + ' style="color:black;padding-left:4px;" title="Play Next"></i>' + '&nbsp;' + 
        '</div > ';


    let htmlForAssessedOrExperiencedWF = '';
    let proficiency = "Assessed";

    if (!isExperiencedChecked) {
        htmlForAssessedOrExperiencedWF = '<div style = "display:inline-flex;width:100%;font-size:small;" >' +
            progressBarHtml + '&emsp;' + inputFilesHtml + '&emsp;' + outputFilesHtml + '&emsp;' + workFlowFeedbackHTML + '&emsp;' + experiencedHtml + '&emsp;' + wiUrlhtml +
            '</div > '
    }
    else
    {
        htmlForAssessedOrExperiencedWF = '<div style = "display:inline-flex;width:100%;font-size:small;" >' +
            progressBarHtml + '&emsp;' + inputFilesHtml + '&emsp;' + outputFilesHtml + '&emsp;' + workFlowFeedbackHTML + '&emsp;' + experiencedHtml + '&emsp;' + wiUrlhtml + '&emsp;' + woButtonsForExperiencedWF +
            '</div > '
    }


    let height = '';

    (colClass == 'col-md-12') ? height = "60px" : height = "63px";

    var html = '<div class="' + colClass + ' nopadding flow_chart_single_box" id="flow_chart_single_box_' + woid + '" data-details=\'' + flowchartBoxDetails + '\'>' +
        '<div class="panel panel-default flow_chart_one_panel">' +
        '<div class="panel-heading WFOwnerHeightAdjustment" style="height:' + height + ';">' +

        '<div class="row" style="width:100%;font-size:small;margin-left:0px;" >' +
        '<div class="col-md-11" style="margin-bottom:0px;padding-left:0px;font-size:11px;overflow:hidden;white-space:nowrap;text-overflow:ellipsis;"><b> WF OWNER:</b> ' + wFOwner + '&emsp;' + '<b>WOID:</b>' + woid + '&emsp;' + nodeNameHtml +
        '</div>' +
        '<div class="col-md-1" style="padding-right:0px;">' +
        '<button class="btn btn-danger btn-xs flowchartCloseBtn pull-right" type="button" title="Close" style="">' +
        '<i class="fa fa-close" aria-hidden="true"></i>' +
        '</button>' +
        '</div >' +
        '</div>' +


        '<div class="row" style="margin-left:0px;margin-right:0px;">' +
        '<div class="col-md-12" style="padding-left:0px;padding-right: 0px;">' +
        htmlForAssessedOrExperiencedWF + 
        '</div>' +
        '</div>' +



        '</div>' +
        '<div class="panel-body">' +
        '<iframe id="' + iframeId + '" style="width:100%;height:540px;border: none;" ></iframe>' +
        //Comments Starts

        '<div id="addCommentPlaceholder"/>' +

        '<div class="panel panel-default">' +
        '<div class="panel panel-heading">' +
        "Comments" +

        '</div>' +
        '<div class="panel-body" id="commentsBody' + woid + '">' +
        '<div class="panel panel-info"><div class="panel-heading">' +
        '<a href="#" style="color: #60c560" title="Click to download Comments Data" onclick="downloadCommentsData(uniquePageID);"><i class="fa fa-download glyphicon-ring glyphicon-green" style="width:22px;height:22px;" aria-hidden="true"></i></a>' +       
        '<div class="pull-right"><input type="text" onkeydown="searchCommentsOnEnter(event, this,\'' + uniquePageID + '\')" name="q" placeholder="Type and Press Enter to Search..." class="searchText" id="searchInComments' + woid + '"></input>&nbsp;' +
        '<button class="btn btn-info btn-xs" type="button" id="showHideFlaggedData' + woid + '">' +
        '<i class="fa fa-flag" title="Click to Filter Flagged Audits" aria-hidden="true"></i>' +
        '</button>&nbsp;' +
        '<button class="btn btn-info btn-xs" type="button" id="showHideAuditData' + woid + '">' +
        '<i class="fa fa-eye" title="Show/Hide Audit Comments" aria-hidden="true"></i>' +
        '</button>&nbsp;' +
        '<button class="btn btn-info btn-xs" type="button" onclick="refreshCommentsSection(this,\'' + woid + '\',\'' + uniquePageID + '\',\'' + subActivityFlowChartDefID + '\')" id="refreshAudit' + woid + '">' +
        '<i class="fa fa-refresh" title="Refresh Data" aria-hidden="true"></i>' +
        '</button>' +
        '</div>' +
        '</div><div class="panel-body"></div>' +
        '<div class="col-lg-2"><select class="select2able select2-offscreen" id="selectStepsWO' + woid + '" onchange="onChangeContext(this);"></select>' +
        '<div class="pull-right" style="padding:6px 11px;"><button class="btn btn-info-outline btn-xs" type="button" id="flagStateButton' + woid + '" title="Enable to make an Escalated Comment">' +
        '<i class="fa fa-flag" aria-hidden="true"></i>' +
        '</button>&nbsp;' +
        '<button class="btn btn-info-outline btn-xs" type="button" id="mailStateButton' + woid + '" title="Enable to send an E-mail with Comment">' +
        '<i class="fa fa-envelope"  aria-hidden="true"></i>' +
        '<button class="btn btn-success btn-xs" type="button" id="normalComment' + woid + '" title="Normal Comment">' +
        '<i class="fa fa-info" aria-hidden="true"></i>' +
        '</button>&nbsp;' +
        '</button>&nbsp;</div></div>' +
        '<div id="comments-container' + uniquePageID + '"></div>' +
        '<div style="float:right;"><label id="labelAllComments' + woid + '" style="color: mediumseagreen;display:none;">Showing All Comments with specific Filter applied&nbsp;</label><button id="prevComments' + woid + '" class="btn btn-success btn-xs disabledbutton" type="button" title="Load Previous Comments"><i class="fa fa-angle-double-left" aria-hidden="true"></i> Prev</button>' +
        '<button id="moreComments' + woid + '" class="btn btn-success btn-xs" type="button" title="Load More Comments">Next <i class="fa fa-angle-double-right" aria-hidden="true"></i></button></div>' +
        '</div>' +
        '</div>' +//ends
        '</div>' +

        '</div></div>' +
        '</div>' +
        '</div>'


    $('.flowChartInfoMsg').hide();
    $('#flowchartRightHeader').show();
    $('.flow_chart_main_panel > .panel-body').prepend(html);

    //Actions on WO Icons
    //Start click
    $('#woButtons_' + woid + ' .startExperiencedWO').on('click', function () {         
        initiateDisabledStep(flowChartType, woid, projId, vNum, false);
    });

    //Pause click
    $('#woButtons_' + woid + ' .pauseExperiencedWO').on('click', function () {
        console.log('FC View pause clicked!!');
        //flowChartType, woID, subActivityFlowChartDefID, stepID, taskID, bookingID, status, reason, executionType, isCallingFromFlowchart

        var woID = woid;
        let subActivityFlowChartDefID = $("#hiddenVal_" + woID).data('defid');
        let stepID = $("#hiddenVal_" + woID).data('stepid');
        let taskID = $("#hiddenVal_" + woID).data('taskid');
        let bookingID = $("#hiddenVal_" + woID).data('bookingid');
        let executionType = $("#hiddenVal_" + woID).data('exectype');
        
        let bookingType = $('#inprogressTask_' + woid + ' .pauseTaskBtn').data('bookingtype');
        let flowChartType = $('#inprogressTask_' + woid + ' .pauseTaskBtn').data('flowcharttype');

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
            $('#isCallingFromFlowChartHide').val("true");
            $('#stopDisabledStepDetails').val(JSON.stringify(stopDisabledStepDetails));
            $('#disabledStepHeader').find('p').remove().end();
            $('#disabledStepHeader').append('<p>Pause Disabled Step for WOID: ' + woID + '</p>')
            getReasons('DeliveryExecution', 'disabledStepReasons');
            $('#modalDisabledStepPause').modal('show');

        }
        else if (executionType == C_EXECUTION_TYPE_MANUAL) {

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

    //Playnext click
    $('#woButtons_' + woid + ' .nextExperiencedStep').on('click', function () {
         
        let flowChartType = $('#inprogressTask_' + woid + ' .startNextTaskBtn').data('flowcharttype');
        let getDetails = JSON.parse(JSON.stringify($('#inprogressTask_' + woid + ' .startNextTaskBtn').data('details')));
        let nextStepType = getDetails.nextStepType;
        let isInputRequiredNext = getDetails.isInputRequiredNext; 
        let botType = getDetails.botType;
        let nextRPAID = getDetails.nextStepRpaId;
        let prjID = getDetails.projectId;
        let version = getDetails.version;
        let stepID = $('.dBox-content #hiddenVal_' + woid).data('stepid');
        var nextTaskID = getDetails.nextStepTaskId;
        let subActivityFlowChartDefID = $('.dBox-content #hiddenVal_' + woid).data('defid');
        let nextBookingID = getDetails.nextStepBookingId;
        let nextStepID = getDetails.nextStepID;
        let nextBookingStatus = getDetails.nextStepBookingStatus;
        let nextExecutionType = getDetails.nextStepExeType;
        let currentTaskID = $('.dBox-content #hiddenVal_' + woid).data('taskid');
        let currentBookingID = $('.dBox-content #hiddenVal_' + woid).data('bookingid');
        let currentExecutionType = $('.dBox-content #hiddenVal_' + woid).data('exectype');
        let nextStepName = getDetails.nextStepName;
        let outputUpload = getDetails.outputUpload;
        var nextRunOnServer = getDetails.nextRunOnServer;

        let stopDisabledStepDetails = new Object();
        stopDisabledStepDetails.flowChartType = flowChartType;
        stopDisabledStepDetails.woID = woid;
        stopDisabledStepDetails.subActivityFlowChartDefID = subActivityFlowChartDefID;
        stopDisabledStepDetails.stepID = stepID;
        stopDisabledStepDetails.taskID = currentTaskID;
        stopDisabledStepDetails.executionType = currentExecutionType;
        stopDisabledStepDetails.bookingID = currentBookingID;
        stopDisabledStepDetails.bookingType = $(".dBox-content #hiddenVal_" + woid).data('bookingtype');

        if (nextStepType == C_STEP_TYPE_DECISION || nextStepType == C_STEP_TYPE_END) {
            
            if (currentExecutionType == C_EXECUTION_TYPE_MANUALDISABLED) {                
                stopDisabledStep(stopDisabledStepDetails, '', woid, false, false);
            }
            else {
                StopTaskInput = PrepareStopFlowChartTaskInput(flowChartType, woid, subActivityFlowChartDefID, stepID, currentTaskID, false, currentExecutionType, currentBookingID, 'BOOKING', '', afterLoadingFlowchart, false, false, true, false);
                stopFlowChartTask(StopTaskInput);
            }


            if (nextStepType == C_STEP_TYPE_DECISION)
                scrollToDecisionStepOfFlowChart(woid, nextStepID);
            else
                validateAndCompleteWorkOrder(woid);

        }        
        else {
            playNextStep(flowChartType, isInputRequiredNext, botType, nextRPAID, woid, prjID, version, stepID, nextTaskID, subActivityFlowChartDefID, nextBookingID, nextStepID, nextBookingStatus, nextExecutionType, currentTaskID, currentBookingID, currentExecutionType, nextStepName, outputUpload, nextRunOnServer, false);
        }


    });

    //Stop click
    $('#woButtons_' + woid + ' .stopExperiencedWO').on('click', function () {
        //if ($('.dBox-content #hiddenVal_' + woid).length != 0) {
            let subActivityFlowChartDefID = $('.dBox-content #hiddenVal_' + woid).data('defid');
            let stepID = $('.dBox-content #hiddenVal_' + woid).data('stepid');
            let taskID = $('.dBox-content #hiddenVal_' + woid).data('taskid');
            let bookingID = $('.dBox-content #hiddenVal_' + woid).data('bookingid');
            let executionType = $('.dBox-content #hiddenVal_' + woid).data('exectype');
            let bookingType = $('#inprogressTask_' + woid + ' .stopTaskBtn').data('bookingtype');
            let flowChartType = $('#inprogressTask_' + woid + ' .stopTaskBtn').data('flowcharttype');

            let stopDisabledStepDetails = new Object();
            stopDisabledStepDetails.flowChartType = flowChartType;
            stopDisabledStepDetails.woID = woid;
            stopDisabledStepDetails.subActivityFlowChartDefID = subActivityFlowChartDefID;
            stopDisabledStepDetails.stepID = stepID;
            stopDisabledStepDetails.taskID = taskID;
            stopDisabledStepDetails.executionType = executionType;
            stopDisabledStepDetails.bookingID = bookingID;
            stopDisabledStepDetails.bookingType = bookingType;

            if (executionType == C_EXECUTION_TYPE_MANUALDISABLED) {

                // Mark as complete wouldn't be displayed after Stop 
                //stopDisabledStep(stopDisabledStepDetails, '', woid, true);
                stopDisabledStep(stopDisabledStepDetails, '', woid, false);
            }
            else {
                InvokeStopFlowChartTask(flowChartType, woid, subActivityFlowChartDefID, stepID, taskID, false, executionType, bookingID, bookingType, '', afterLoadingFlowchart, '', false, false, false)
                //completeWorkOrder(woid);
            }            
        //}
        //else {
        //    // Mark as complete wouldn't be displayed after Stop 
        //    //stopDisabledStep(null, '', woid, true);
        //    stopDisabledStep(stopDisabledStepDetails, '', woid, false);
        //}
    });

    $('.flowchartCloseBtn').on('click', function () {
        var that = this;
        var flowchartBox = $(that).closest('.flow_chart_single_box');
        var boxData = flowchartBox.data('details');
        $('#viewBtn_' + boxData.woid).attr('disabled', false);
        flowchartBox.remove();
        let woIDDetails = JSON.parse($('#viewBtn_' + boxData.woid).attr('data-details'));
        if (woIDDetails.experienced == '1' ) {
            requestWorkorder();
        }
      

    });


    $('#' + iframeId).load(function () {
        if (((window.location.search != "") && isFloatingWindow != undefined && isFloatingWindow != null && isFloatingWindow.toLowerCase() == "true") || IsNotficationNavigation) {
            var currentRunningPosition = '';
            var inProgressTaskArr = document.getElementById('iframe_' + woIDFW).contentWindow.app.graph.toJSON().cells.filter(function (el) { return ((el.type == 'ericsson.Manual' || el.type == 'ericsson.Automatic') && el.attrs.task.status == "STARTED") });
            var StepToScroll;
            // in case of no running task, find out if it is decision step.
            if (inProgressTaskArr.length == 0) {
                if (IsNotficationNavigation === true) {
                    var idToScroll = document.getElementById('iframe_' + woIDFW).contentWindow.app.graph.toJSON().cells[0].id;
                    StepToScroll = document.getElementById('iframe_' + woIDFW).contentWindow.app.graph.toJSON().cells.filter(function (el) { return (el.id === idToScroll) });
                }
                else {
                    StepToScroll = document.getElementById('iframe_' + woIDFW).contentWindow.app.graph.toJSON().cells.filter(function (el) { return (el.id === stepIdFromFloatingWindw) });
                }
                if (StepToScroll.length != 0 && StepToScroll != undefined && StepToScroll != null) {
                    currentRunningPosition = StepToScroll[0].position;
                    document.getElementById('iframe_' + woIDFW).contentWindow.app.paperScroller.scroll(currentRunningPosition.x, currentRunningPosition.y + 140);
                    window.scrollTo(0, 550);
                }
            }
            else {
                //  get position of running task
                for (var shape = 0; shape < inProgressTaskArr.length; shape++) {
                    currentRunningPosition = inProgressTaskArr[shape].position;
                    document.getElementById('iframe_' + woIDFW).contentWindow.app.paperScroller.scroll(currentRunningPosition.x, currentRunningPosition.y + 140);
                    window.scrollTo(0, 550);
                }
            }

        }  
        var passParam = {};
        if (typeof callback === 'function') {
            callback(passParam);
        }
    });
    //$('#' + iframeId).attr('src', iframeSrc + experiencedParamForUrl);
    $('#' + iframeId).attr('src', iframeSrc + proficiencyParamForUrl);

   // getStepsWO(subActivityFlowChartDefID, woid, experienced = '0');
    var role = JSON.parse(ActiveProfileSession).role;
    clickedWOID = JSON.parse(localStorage.getItem("clickedWOID"));
    var cFlag = false;
    //var cDataExistsFlag = false;
    getCommentsSection(uniquePageID, '', role, "WO_LEVEL", startPageComments, cFlag);

    $('button#showHideAuditData' + woid).on('click', function () {
        pwIsf.addLayer({ text: "Please wait ..." });
        $('#prevComments' + woid).addClass('disabledbutton');
        $('#moreComments' + woid).addClass('disabledbutton');
        $('#labelAllComments' + woid).css("display", "inline-block");
        that = this;
        uniquePageID = "WORK_ORDER_" + woid;

        commentsJQXHR = getCommentsData(uniquePageID);

        commentsJQXHR.done(function (data) {
            pwIsf.removeLayer();
            if (data.ErrorFlag == false) {
                commentsArrayGlobal = data.commentsData;
                const auditCommentsArray = commentsArrayGlobal.filter(element => element.type == "USER_COMMENT");
                if ($(that).hasClass('btn-info')) {
                    $(that).removeClass('btn-info').addClass('btn-info-outline');
                    getCommentsGeneric(uniquePageID, auditCommentsArray);
                } else if ($(that).hasClass('btn-info-outline')) {
                    getCommentsGeneric(uniquePageID, commentsArrayGlobal);
                    $(that).removeClass('btn-info-outline').addClass('btn-info');
                }
                if ($(that).find('i').hasClass('fa-eye')) {
                    $(that).find('i').removeClass('fa-eye').addClass('fa-eye-slash');
                } else if ($(that).find('i').hasClass('fa-eye-slash')) {
                    $(that).find('i').removeClass('fa-eye-slash').addClass('fa-eye');
                }
            }
            else {
                pwIsf.alert({ msg: data.Error, type: 'error' });
            }
            $('button#showHideFlaggedData' + woid).removeClass('btn-danger').addClass('btn-info');
            $("#searchInComments" + woid)[0].value = '';
        });


    });

    $('button#showHideFlaggedData' + woid).on('click', function () {
        pwIsf.addLayer({ text: "Please wait ..." });
        $('#prevComments' + woid).addClass('disabledbutton');
        $('#moreComments' + woid).addClass('disabledbutton');
        $('#labelAllComments' + woid).css("display", "inline-block");
        that = this;
        uniquePageID = "WORK_ORDER_" + woid;
        commentsJQXHR = getCommentsData(uniquePageID);
        commentsJQXHR.done(function (data) {
            pwIsf.removeLayer();
            if (data.ErrorFlag == false) {
                commentsArrayGlobal = data.commentsData;
                const flaggedCommentsArray = commentsArrayGlobal.filter(element => element.importance == 1);
                if ($(that).hasClass('btn-info')) {
                    $(that).removeClass('btn-info').addClass('btn-danger');
                    getCommentsGeneric(uniquePageID, flaggedCommentsArray);
                } else if ($(that).hasClass('btn-danger')) {
                    $(that).removeClass('btn-danger').addClass('btn-info');
                    getCommentsGeneric(uniquePageID, commentsArrayGlobal);
                }
            }
            else {
                pwIsf.alert({ msg: data.ErrorData.Error, type: 'error' });
            }
            $('button#showHideAuditData' + woid).removeClass('btn-info-outline').addClass('btn-info');
            $('button#showHideAuditData' + woid).find('i').removeClass('fa-eye-slash').addClass('fa-eye');
            $("#searchInComments" + woid)[0].value = '';
        });
    });

    function getCommentsData(uniquePageID) {
        searchText = "";
        var serviceUrl = service_java_URL + 'auditing/getAuditData?pageId=' + uniquePageID + '&searchString=' + searchText;// + '&start='+startPageComments+'&length='+commentsCount;
        if (ApiProxy == true) {
            serviceUrl = service_java_URL + 'auditing/getAuditData?' + encodeURIComponent("pageId=" + uniquePageID + "&searchString=" + searchText);// + '&start=' + startPageComments + '&length=' + commentsCount);
        }
        var jqXHR = $.isf.ajax({
            type: 'get',
            url: serviceUrl,
            returnAjaxObj: true
        });
        return jqXHR;
    }

    $('button#normalComment' + woid).on('click', function () {
        $('button#flagStateButton' + woid).removeClass('btn-danger').addClass('btn-info-outline');
        $('button#normalComment' + woid).removeClass('btn-info-outline').addClass('btn-success');
        flagButtonBoolean = 1;
    });

    $('button#flagStateButton' + woid).on('click', function () {
        that = this;
        if ($(that).hasClass('btn-info-outline')) {
            $(that).removeClass('btn-info-outline').addClass('btn-danger');
            $('button#normalComment' + woid).removeClass('btn-success').addClass('btn-info-outline');
            flagButtonBoolean = 1;
        } else if ($(that).hasClass('btn-danger')) {
            $(that).removeClass('btn-danger').addClass('btn-info-outline');
            $('button#normalComment' + woid).removeClass('btn-info-outline').addClass('btn-success');
            flagButtonBoolean = 0;
        }
    });

    $('button#mailStateButton' + woid).on('click', function () {
        that = this;
        if ($(that).hasClass('btn-info-outline')) {
            $(that).removeClass('btn-info-outline').addClass('btn-success');
            mailButtonBoolean = 1;
        } else if ($(that).hasClass('btn-success')) {
            $(that).removeClass('btn-success').addClass('btn-info-outline');
            mailButtonBoolean = 0;
        }
    });

    $('button#moreComments' + woid).on('click', function () {
        $('#prevComments' + woid).removeClass('disabledbutton');
        uniquePageID = "WORK_ORDER_" + woid;
        searchtext = window.parent.$("#searchInComments" + woid)[0].value;
        startPageComments = startPageComments + commentsCount;
        getCommentsSection(uniquePageID, searchtext, role, "WO_LEVEL", startPageComments);

    });

    $('button#prevComments' + woid).on('click', function () {
        $('#moreComments' + woid).removeClass('disabledbutton');
        uniquePageID = "WORK_ORDER_" + woid;
        searchtext = window.parent.$("#searchInComments" + woid)[0].value;
        //if (startPageComments == 0) startPageComments = 1;
        startPageComments = startPageComments - commentsCount;
        if (startPageComments < 0)
            $('#prevComments' + woid).addClass('disabledbutton');
        getCommentsSection(uniquePageID, searchtext, role, "WO_LEVEL", startPageComments);
    });

    getWOpercentageAndPerTextFromAPI({ woId: woid });
     // update progress bar after flowchart opened



}

function fetchCommentsData(woid, subActivityFlowChartDefID) {
    getCommentsGeneric(uniquePageID, commentsArrayGlobal);
}

//INTRO JS
function myWorkStartHelp() {
    var intro = introJs();
    intro.setOptions({
        steps: [
            {
              element: '#onMainHeader_ClosedWO',
              intro: "All closed WOs can be viewed here."
            },
            {
              element: '#onMainHeader_downloadApp',
              intro: "ISF Desktop application which is required to run any BOTs<br> can be downloaded and installed from here."
              //position: 'right'
            },
            {
              element: '.inprogressTask_panel',
              intro: "In this section, in-progress WO & their running steps will be visible.<br>Users will be able to see time spent, step details and take actions for running steps.<br>Users can pause all running steps with 2 clicks."
              //position: 'right'
            },
            {
              element: '.work_order_panel',
              intro: "In this section, all open work orders will appear.Users will be able to see overall:<ul><li> WO progress</li><li>View/Edit Nodes</li><li>Complete WO</li><li>Execute Steps</li><li>Transfer WO</li></ul>Users can filter WO based on WO status & priority."
              //position: 'right'
            },
            {
              element: '.flow_chart_main_panel',
              intro: "Selected Work orders will appear here.<br> Users will be able to review and execute Workflows"
              //position: 'right'
            },


        ]
    });

    intro.start();
}

$(document).ready(function () {  // ON DOCUMET READY

    //Register any callback methods
    registerCallbackEvents();

    // START - ON TAB SELECTION 
    $('.nav-tabs a').on('shown.bs.tab', function (event) {
        let selectedTabHref = $(event.target).attr('href');

        if (selectedTabHref == '#assignToMe_tab_2') {
            $(".wmd-view-topscroll").scroll(function () {
                $(".wmd-view")
                    .scrollLeft($(".wmd-view-topscroll").scrollLeft());
            });
            $(".wmd-view").scroll(function () {
                $(".wmd-view-topscroll")
                    .scrollLeft($(".wmd-view").scrollLeft());
            });

            $("#selectprojId").select2({ placeholder: "Search...", maximumSelectionLength: 5, allowClear: true, multiple: true });
            $('#selectprojId').empty();
            $.isf.ajax({
                url: `${service_java_URL}woManagement/getProjectBySignumForProjectQueue/${signumGlobal}`,
                async: false,
                success: function (data) {
                    getProjectQueueData(data);
                },
                error: function (xhr) {
                    console.log(`An error occurred on getAllProjects: ${xhr.error}`);
                }
            });            
        }

        if (selectedTabHref == '#assignedTasks_tab_3') {


            let durationText = "This Month";
            let statusText = "Assigned";
            $("a.dividerDuration").removeClass("active");
            $("a.dividerStatus").removeClass("active");
            $('a.dividerDuration').each(function (e) { if ($(this).text() == durationText) { $(this).addClass('active'); } });
            $('a.dividerStatus').each(function (e) { if ($(this).text() == statusText) { $(this).addClass('active'); } });

            let getDates = getStartDateAndEndDateFromSelectedDuration(durationText);
            let getStatus = getWOStatusFilter(statusText);

            getWO_forAssignedTasksForTransfer(getDates.sdate, getDates.edate, getStatus);

            getAllSignumForTransferWO();

        }
        if (selectedTabHref == '#closedWorkorders_tab_4') {
            callFor_closedWO('This Month');
        }

        if (selectedTabHref == '#MyWork_tab_1') {
            resetAllFilterOfWO(); // reset all filters of WO
            $('.flow_chart_main_panel .panel-body > .flow_chart_single_box').remove();
            $('.flowChartInfoMsg').show();
            clearTimeouts();
            requestInprogressTasks();
            requestWorkorder();
        }


    });
    // END - ON TAB SELECTION 


    //start- Bind close event of edit NEID/Nodename

    //$('#myModalMadEwo').on('hidden.bs.modal', function () {

    //})
    //start- Bind close event of edit NEID/Nodename

    //Check amount of characters entered.
    //$('.feedbackBox #comments').keyup(function (event) {
    //    let maxLen = 10;
    //    let Length = $(".feedbackBox #comments").val().length;
    //    let AmountLeft = maxLen - Length;
    //    //$('#txt-length-left').html(AmountLeft);
    //    if (Length >= maxLen) {
    //        if (event.which != 8) {
    //            return false;
    //        }
    //    }
    //});

    $('#onMainRefreshAll').on('click', function () {

        pwIsf.confirm({
            title: 'Refresh All', msg: 'Actually It will update/refresh following: <ul><li>In-progress Task</li><li> My current work order </li><li>Flow chart panel.</li></ul>Are you sure to refresh ? ',
            'buttons': {
                'Yes': {
                    'action': function () {
                        resetAllFilterOfWO(); // reset all filters of WO
                        $('.flow_chart_main_panel .panel-body > .flow_chart_single_box').remove();
                        $('.flowChartInfoMsg').show();
                        clearTimeouts();
                        requestInprogressTasks();
                        requestWorkorder();
                    }
                },
                'No': { 'action': function () { } },

            }
        });



    });

    $('input[type=search]').on('search', function () {
        $(this).trigger('keyup');
    });

    $('.myWorkHelpBtn').on('click', function () {
        myWorkStartHelp();
    });

    function adjustBodyOnScroll(passSroll) {
        var woPanel = $('.work_order_panel');
        if (passSroll) {
            woPanel.css({ 'margin-top': '100px' });
        } else {
            woPanel.css({ 'margin-top': '0px' });
        }
    }

    function fixRunningTask() {
        var $inProgressArea = $('.inprogressTask_panel');

        if ($inProgressArea.find('.dBox-content').length) {

            if ($(window).scrollTop() > 65) {
                $inProgressArea.addClass('fixedThisToTop');
                adjustBodyOnScroll(true);
            } else {
                $inProgressArea.removeClass('fixedThisToTop');
                adjustBodyOnScroll(false);
            }
        } else {
            if ($inProgressArea.hasClass('fixedThisToTop')) {
                $inProgressArea.removeClass('fixedThisToTop');
                adjustBodyOnScroll(false);
            }
        }
    }

    $(window).scroll(fixRunningTask);

    ///////////////////////////////////////////////////



    $('#changeColumns').on('click', function () {
        var that = this;
        var iObj = $(that).find('i');


        let flowchartCanvas = '';

        if (iObj.hasClass('fa-th-large')) {
            $('.flow_chart_main_panel > .panel-body > div').removeClass('col-md-12').addClass('col-md-6');
            $('.WFOwnerHeightAdjustment').removeAttr('style');
            $('.WFOwnerHeightAdjustment').css('height', '63');
            $('.flowchartCloseBtn').removeAttr('style');
           // $('.flowchartCloseBtn').css('margin-left', '8');
            $('.flowchartCloseBtn').addClass('pull-right');
            iObj.removeClass('fa-th-large').addClass('fa-align-justify');
            $(that).attr('title', 'Make it one Column');
        } else {
            $('.flow_chart_main_panel > .panel-body > div').removeClass('col-md-6').addClass('col-md-12');
           // $('.flowchartCloseBtn').css('margin-left', '+=52');
            $('.flowchartCloseBtn').addClass('pull-right');
            $('.WFOwnerHeightAdjustment').css('height', '60');     
            iObj.removeClass('fa-align-justify').addClass('fa-th-large');
            $(that).attr('title', 'Make it two Columns');
        }

    });


    $('#fullScreenForFlowChart').on('click', function () {

        if (makeItFullScreen(this, $('.flow_chart_main_panel'))) {
            $('#showWOToTop').show();
            resSetShowAndHideWorkOrderBtn();
            flagFlowChartFullScreen = true;
        } else {
            flagFlowChartFullScreen = false;            
            var iObj2 = $('#showWOToTop').find('i');
            if (iObj2.hasClass('fa-arrow-up')) {
                $('.work_order_panel').removeClass('fixedThisToTop');
            }
            $('.flow_chart_main_panel').attr('style', '');
            $('#showWOToTop').hide();
        }

    });



    $('#showInprogessToTop').on('click', function () {

        var that = this;
        var iObj = $(that).find('i');
        if (iObj.hasClass('fa-arrow-circle-o-down')) {
            $('.inprogressTask_panel').addClass('fixedThisToTop');
            iObj.removeClass('fa-arrow-circle-o-down').addClass('fa-arrow-circle-o-up');
            $('.flow_chart_main_panel').animate({ 'top': '105px' }, 500);
            $(that).attr('title', 'Hide inprogress task');
        } else {
            $('.flow_chart_main_panel').animate({ 'top': '0px' }, 500, function () {
                $('.inprogressTask_panel').removeClass('fixedThisToTop');
            });
            iObj.removeClass('fa-arrow-circle-o-up').addClass('fa-arrow-circle-o-down');
            $(that).attr('title', 'Show inprogress task');
        }
    });


    $('#showWOToTop').on('click', function () {

        var that = this;
        var iObj = $(that).find('i');
        if (iObj.hasClass('fa-arrow-down')) {
            $('.work_order_panel').addClass('fixedThisToTop');
            iObj.removeClass('fa-arrow-down').addClass('fa-arrow-up');
            $('.flow_chart_main_panel').animate({ 'top': '137px' }, 500);
            $(that).attr('title', 'Hide work order');
        } else {
            $('.flow_chart_main_panel').animate({ 'top': '0px' }, 500, function () {
                $('.work_order_panel').removeClass('fixedThisToTop');
            });
            iObj.removeClass('fa-arrow-up').addClass('fa-arrow-down');
            $(that).attr('title', 'Show work order');
        }
    });



    ///////////
    requestInprogressTasks();
    InitiateSignalRConnection();
    var url = window.location.href;
    isFloatingWindow = getParameterByName("isFloatingWindow", url);
    var stepIdFromFloatingWindw = getParameterByName("stepID", url);
    woIDFW = getParameterByName("woID", url);

    if (window.location.search == "" && isFloatingWindow == null && (woIDFW == undefined || woIDFW == null)) {
        requestWorkorder();
    }
    else {
        var url = window.location.href;
         isFloatingWindow = false;
        isFloatingWindow = getParameterByName("isFloatingWindow", url);
        tabIDFW = getParameterByName("tabID", url);
        if (isFloatingWindow != null && woIDFW != null && isFloatingWindow.toLowerCase() == 'true') {
            //create workOrder tabs details to local storage.
            createEntryForCurTabWorkOrder(woIDFW, tabIDFW);
            RequestWorkOrderUsingWOId(woIDFW);
        }
        else if ((isFloatingWindow == undefined || isFloatingWindow == null) && woIDFW != undefined && woIDFW != null) {
            IsNotficationNavigation = true;
            RequestWorkOrderUsingWOId(woIDFW);
        }
        else
            requestWorkorder();
    }




    //START- GET FLOW CHART ID FROM URL AND OPEN THAT FLOW CHART 
    let getWoidFromUrl = getUrlParams()['woid'];

    if (typeof getWoidFromUrl != 'undefined') {
        let timerIcrement = 0;
        var intervalToCheckViewBtn = setInterval(function () {

            let viewFCBtn = $('#viewBtn_' + getWoidFromUrl);

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
    }
    //END- GET FLOW CHART ID FROM URL AND OPEN THAT FLOW CHART 


});

//var dFlag = false;
function showInst(elem) {
    //var defidForInst = "";
    //defidForInst = elem.id.split('_')[1] 
    $('#WFLevelInst').css('left', event.clientX);      // <<< use pageX and pageY
    $('#WFLevelInst').css('top', event.clientY);
    //$('#WFLevelInst').css('display', 'inline');
    //$("#WFLevelInst").css("position", "absolute");  
    let woid = JSON.parse(elem.dataset.feedbackdetails).woid;
    var ifrm = document.getElementById('iframe_' + woid);
    defid = ifrm.contentWindow.document.getElementById('defID').value;

    let JsonStringArr = [];

    var wfDataObj = new Object();
    wfDataObj.flowChartDefID = defid;
    wfDataObj.stepID = null;

    JsonStringArr.push(wfDataObj);

    if (JsonStringArr) {
            $.isf.ajax({
                url: service_java_URL + "activityMaster/getInstructionURLList",
            context: this,
            crossdomain: true,
            processData: true,
                contentType: 'application/json',
                type: 'POST',
                data: JSON.stringify(JsonStringArr),
                xhrFields: {
                    withCredentials: false
            },
                success: AjaxSucceeded,
                error: AjaxFailed
            });
        function AjaxSucceeded(data, textStatus) {
            data = data.data;
            $("#wf_instruction").html('');
            if (data.length == 0) {
                //var html = `No data`

                //$("#wf_instruction").append(html);
                pwIsf.alert({ msg: "Work Flow WI URL is not updated! Please ask WorkFlow owner to update the same.", type: "info" });
            }
             else {

                for (x in data) {

                    if (data[x].stepID == "" || data[x].stepID == null) {

                        var html = `<li><a class="instructionP" style="color:blue;" target="_blank" href='${data[x].urlLink}'>${escapeHtml(data[x].urlName)} </a></li>`

                        $("#wf_instruction").append(html);
                    }
                }
                dFlag = true;
            }

                //else
                //pwIsf.alert({ msg: "Error in data.", type:'error'});
        }
        function AjaxFailed(xhr, status, statusText) {
            pwIsf.alert({ msg: 'Error fetching data', type: 'warning' });
        }
    }




}


//$(function () {
//    $("a").not('#lnkLogout').click(function () {
//        window.onbeforeunload = null;
//    });
//    $(".btn").click(function () {
//        window.onbeforeunload = null;
//    });
//})




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


function GetReasonForDenailX(reasonType, selectID) {
    $.isf.ajax({
        async: false,
        url: service_java_URL + "woExecution/getWOFailureReasons/" + reasonType,
        success: function (data) {
            //pwIsf.removeLayer();
            selectID.html('');
            if (data.isValidationFailed == false) {
                $.each(data.responseData, function (i, d) {
                    selectID.append('<option value="' + d.failureReason + '">' + d.failureReason + '</option>');
                })
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
            $('#commentSkippedWF').select2();
        }
    });
}

//pause one woid.
function updateFlowChartTaskStatusX(flowChartType, woID, subActivityFlowChartDefID, stepID, taskID, bookingID, status, reason, executionType, isCallingFromFlowchart) {
    //$(".joint-popup").hide();
    var nextStartRule = null;
    var nextStopRule = null;
    var nextExecutionType = null;


    if (status === C_BOOKING_STATUS_SKIPPED && isCallingFromFlowchart === true) {
        let getAllCells = app.graph.getCells();
        let getAllFilteredCells = app.graph.getCells().filter(x => x.attributes.type === C_STEP_TYPE_AUTOMATIC || x.attributes.type === C_STEP_TYPE_MANUAL);
        let currentStep = getAllFilteredCells.filter(function (el) { return el.attributes.id == stepID });
        let currentCell = app.graph.getCell(stepID);
        var outboundlink = app.graph.getConnectedLinks(currentCell, { outbound: true });
        var nextStep = getNextStep(outboundlink[0], stepID, getAllCells);
        var currentStartRule = currentStep[0].attributes.startRule;
        var currentStopRule = currentStep[0].attributes.stopRule;
        var nextStepType = nextStep.attributes.type;
        var nextStepId = nextStep.attributes.id;
        if (nextStep.attributes.type === C_STEP_TYPE_AUTOMATIC || nextStep.attributes.type === C_STEP_TYPE_MANUAL) {
            nextStartRule = nextStep.attributes.startRule;
            nextStopRule = nextStep.attributes.stopRule;
            nextExecutionType = nextStep.attributes.executionType;
        }

    }

    var refferer = 'ui';
    if (woID == null || woID.length == 0 || taskID == null || taskID.length == 0 || status == null || status.length == 0 || signumGlobal == null || signumGlobal.length == 0) {
        pwIsf.alert({ msg: 'Something went wrong!!', type: 'error' });
    }
    //else if ((status == 'SKIPPED' || status == "ONHOLD") && (reason == null || reason.length == 0)) {
    //    pwIsf.alert({ msg: 'Please give a reason...', type: 'error' });
    //}
    else {
        if (bookingID == "%BOOKING_ID%" || bookingID == "") {
            bookingID = 0;
        }
        if (reason != null) {
            for (var i = 0; i < reason.length; i++) {
                if (reason.charAt(i) == "/") {
                    // console.log(taskName.charAt(i));
                    reason = reason.replace(reason.charAt(i), "@");
                }
            }
        }
        pwIsf.addLayer({ text: "Please wait ..." });
        $.isf.ajax({
            type: "POST",
            url: service_java_URL + "woExecution/updateBookingDetailsStatus/" + woID + "/" + signumGlobal + "/" + taskID + '/' + bookingID + '/' + status + '/' + reason + '/' + stepID + '/' + subActivityFlowChartDefID + '/' + refferer,
            success: function (data) {
                if ('Success' in data) {
                    let stepDetails = new Object;
                    stepDetails.woId = woID;
                    stepDetails.taskID = taskID;
                    stepDetails.flowChartStepId = stepID;
                    stepDetails.flowChartDefID = subActivityFlowChartDefID;
                    stepDetails.signumId = signumGlobal;
                    stepDetails.decisionValue = '';
                    stepDetails.executionType = executionType;

                    let stepDetailsData = new Object();
                    //let flowChartType = window.parent.$('.feedback_' + woID).data().feedbackdetails.flowChartType;
                    stepDetailsData.flowChartType = flowChartType;

                    if (isCallingFromFlowchart) {
                        $("#modalSkippedWF").modal("hide");
                        $("#commentSkippedWF").val('');
                        $("#commentSkippedWF").select2();
                        //getFlowChart(woID);

                        stepDetailsData.status = status;
                        stepDetailsData.hour = data.hour;
                        stepDetailsData.stepID = stepID;
                        stepDetailsData.iconsOnNextStep = true;
                        changeStepColor(stepDetailsData);
                        // To Notify Floating Window and All opened tabs
                        if (status === C_BOOKING_STATUS_SKIPPED) {
                            let autoSenseDataForDirectSkip = PrepareAutoSenseInputDataForSkip(woID, subActivityFlowChartDefID, stepID, taskID, status, executionType, nextStartRule, nextStopRule, currentStartRule, currentStopRule, nextExecutionType, nextStepType, nextStepId);
                            if (Object.keys(autoSenseDataForDirectSkip).length === 0 && bookingID == 0) {
                                window.parent.NotifyClientOnWorkOrderStatusChange(stepDetailsData, woID, null, false);

                            }
                            else {
                                window.parent.NotifyClientOnWorkOrderStatusChange(stepDetailsData, woID, autoSenseDataForDirectSkip, true);

                            }



                        }
                        else {
                            //  var InProgressTasks = JSON.parse(localStorage.getItem("AutoSenseData"));
                            var InProgressTasks = GetSavedAutoSenseData();
                            let autoSenseInputData = PrepareAutoSenseInputData(woID, subActivityFlowChartDefID, stepID, status, InProgressTasks);
                            window.parent.NotifyClientOnWorkOrderStatusChange(stepDetailsData, woID, autoSenseInputData, true);


                        }
                        window.parent.requestInprogressTasks();
                        window.parent.requestWorkorder();
                        window.parent.getWOpercentageAndPerTextFromAPI({ woId: woID });
                        disableAssessedExperiencedToggle(woID);//Toggle assessed and qual
                    }
                    else {
                        if (status == C_BOOKING_STATUS_SKIPPED) { $("#modalSkipped").modal("hide"); }
                        else { $("#modalTaskHold").modal("hide"); }

                        //if ($("#WODiv" + woID).hasClass("in")) {
                        //    $("#WODiv" + woID).removeClass("in")
                        //    $("#WODiv" + woID).addClass("collapse")
                        if ($('#iframe_' + woID)[0] != undefined) {
                            //$('#iframe_' + woID)[0].contentWindow.location.reload(true);

                            stepDetailsData.status = status;
                            stepDetailsData.hour = data.hour;
                            stepDetailsData.stepID = stepID;
                            document.getElementById('iframe_' + woID).contentWindow.changeStepColor(stepDetailsData)
                        }


                        // To Notify Floating Window and All opened tabs

                        // var InProgressTasks = JSON.parse(localStorage.getItem("AutoSenseData"));
                        var InProgressTasks = GetSavedAutoSenseData();
                        let autoSenseInputData = PrepareAutoSenseInputData(woID, subActivityFlowChartDefID, stepID, status, InProgressTasks);
                        window.parent.NotifyClientOnWorkOrderStatusChange(stepDetailsData, woID, autoSenseInputData, true);
                        requestInprogressTasks();
                        requestWorkorder();
                        getWOpercentageAndPerTextFromAPI({ woId: woID });
                        disableAssessedExperiencedToggle(woID, false);


                    }
                    //  pwIsf.removeLayer();
                }
                else {
                    pwIsf.removeLayer();
                    pwIsf.alert({ msg: data.Error, type: 'error' });
                }
            },
            error: function (xhr, status, statustext) {
                pwIsf.removeLayer();
                //var err = JSON.parse(xhr.responseText);
                var errorMessage = JSON.parse(xhr.responseText).errorMessage;
                if (errorMessage == undefined || errorMessage == '') {
                    errorMessage = "Some issue occured. Please try again!"
                }
                pwIsf.alert({ msg: errorMessage, type: 'error' });
            },
            complete: function () {
                pwIsf.removeLayer();
                handleWOActionButtons(urlParams.get("woID"), true);
            }

        });
    }
}

function registerCallbackEvents() {
    $('#flowChartWarning').on('hidden.bs.modal', function (e) {
        resetNetworkElementFields('neMandatory');
    })
}

function getProjectQueueData(data) {
    if (!data.isValidationFailed) {
        $.each(data.responseData, function (i, d) {
            $('#selectprojId').append(`<option value="${d.projectID}">${d.projectID}</option>`);
        });
        const durationText = $('a.dividerDate.active').text();
        let priority = $('a.dividerPriorityProjectQueue.active').text();
        let status = $('a.dividerStatusPQ.active').text();
        let getDates = getStartDateAndEndDateFromSelectedDuration(durationText);
        $('#do_status').select2('destroy');
        $('#do_status').val('All').select2();
        let doStatus = $('#do_status option:selected').val();
        if (data.responseData.length === 1) {
            $('#selectprojId').val(data.responseData[0].projectID).trigger('change');
            filterValidationAndPopulateData(getDates.sdate, getDates.edate, priority, status, doStatus);
        }
        else {
            callFor_WOForAssignToMe('Today', true);
        }
    }
}