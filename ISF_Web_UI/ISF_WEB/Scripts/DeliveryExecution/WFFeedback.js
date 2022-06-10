//Add button validation

function validateComments(commentTextArea, feedbackType, woID) {

    if (feedbackType == 'WF') {
        if ($('#comments_' + woID).val() == "") {
            $('#btnaddComments_' + woID).attr('disabled', true);
        }
        else {
            $('#btnaddComments_' + woID).attr('disabled', false);
        }
    }
    else {
        let selectedval = $('#instantFeedback_' + woID + ' option:selected').text().toLowerCase();
        if (selectedval == 'others' && $('#comments_' + woID).val() == "") {
            $('#btnaddComments_' + woID).attr('disabled', true);
        }
        else if ($('#instantFeedback_' + woID + ' option:selected').val() == '-1') {
            $('#btnaddComments_' + woID).attr('disabled', true);
        }
        else {
            $('#btnaddComments_' + woID).attr('disabled', false);
        }
    }

    let characterCount = $(commentTextArea).val().length,
        current = $('#current_' + woID),
        left = 1000 - characterCount;
    current.text("Only 1000 characters are allowed (" + left + " left)");
}

function instantFeedbackChange(selectObj, woID) {
    let selectedval = $('#instantFeedback_' + woID + ' option:selected').text().toLowerCase();

    if (selectedval == "others" && $('#comments_' + woID).val() == "") {
        $('#btnaddComments_' + woID).attr('disabled', true);
    }
    else {
        $('#btnaddComments_' + woID).attr('disabled', false);
    }
}

//Feedback on WF level
function createFeedbackModal(obj) {
    // obj.preventDefault();



    let details = JSON.parse(obj.getAttribute('data-feedbackdetails'));
    let woId = details.woid;
    let projectID = details.projectID;
    let wfId = details.wfid;


    let iframeid = document.getElementById('iframe_' + woId);
    let versionNo = iframeid.contentWindow.document.querySelector('input[id="flowchartVersion"]').value;
    //details.versionNo;
    let flowchartDefId = iframeid.contentWindow.document.querySelector('input[id="defID"]').value;
    let wfName = iframeid.contentWindow.document.querySelector('input[id="wfName"]').value;
    //details.subActivityFlowChartDefID;
    let flowChartType = details.flowChartType;

    let commentsDetails = new Object();
    commentsDetails.woId = woId;
    commentsDetails.projectID = projectID;
    commentsDetails.versionNo = versionNo;
    commentsDetails.wfId = wfId;
    commentsDetails.wfName = wfName;
    commentsDetails.flowchartDefId = flowchartDefId;
    commentsDetails.feedbackType = 'WF';

    let stringifyComments = JSON.stringify(commentsDetails)
    let wflabel = wfId + '/' + wfName + '/' + versionNo;

    //if ($('.feedbackBox').length > 0) {
    //    $('.feedbackBox').remove();
    //}
    if ($('#openFeedbackModal_' + woId).length > 0) {
        $('#openFeedbackModal_' + woId).remove();
    }
    if ($('#openStepLevelFeedbackModal_' + woId).length > 0) {
        $('#openStepLevelFeedbackModal_' + woId).remove();
    }


    let html = `<div class="feedbackBox" id="openFeedbackModal_${woId}" >
        <div class="col-md-13">
            <div style="text-align:center;background-color: #d9edf7;height: 26px;line-height: 2;">
                <span><b>Add WF Feedback</b></span> <button type="button" class="close pull-right" style="padding-right: 3px;" onclick="closeFeedbackBox(${woId},'WF')">&times;</button>
            </div>        
        </div>   
        
        <div class="col-md-10" id="FeedbackThankYouText" style="padding-top:10px;padding-bottom:10px;">
        
        </div>


        <div class="col-md-12" style="margin-top:10px;"><label for="woidInfo">WF_ID/WFName/Version: </label>
            <input type="text" class="form-control" id="woidInfo" value="${wflabel}" disabled />
        </div><br />

        <div class="col-md-12"><label for="comments">Comments: </label>
            <span class="reqFld">*</span><textarea class="comments" style="width: 100%" id="comments_${woId}" rows="4" cols="65" maxLength="1000" onclick="removeFeedbackThankyoutext()" onkeyup="validateComments(this, 'WF', ${woId})"  required></textarea>
        </div>
  
        <div class="col-md-12"><span id="current_${woId}" style="color:darkgray" class="pull-right">Only 1000 characters are allowed (1000 left)</span></div><br/>
        <div class="col-md-12"> <input type="button" class="btn btn-primary pull-right btnaddComments" id="btnaddComments_${woId}" value="Add" onclick="addFeedback(this)" data-feedbackdetails = '${stringifyComments}' disabled/> </div>
        <br/><br/>

        <div class="col-md-12"> <span> <b> Feedback History (Latest 10): </b> </span> </div>
        <div class="col-md-12 feedbackHistoryDiv" id="feedbackHistoryDiv_${woId}">
           
        </div>
    </div>`;

    //  let responseFeedbackHistory = getFeedbackHistory(woId, flowchartDefId);

    $('#flow_chart_single_box_' + woId).append(html);

    let feedbackParent = document.getElementById('flow_chart_single_box_' + woId);

    let feedbackBox = $(feedbackParent).find('.feedbackBox');
    if ($(feedbackBox).css('display') == "none" || $(feedbackBox).css('display') == "") {
        $(feedbackBox).css('display', 'block');
        $('.feedback_' + woId).attr('disabled', true);
    }

    let historyObj = {};
    historyObj.woID = woId;
    historyObj.flowchartDefId = flowchartDefId;
    historyObj.feedbackType = 'WF'

    getFeedbackHistory(historyObj);
}


//Close WF level Feedback Box
function closeFeedbackBox(id, feedbackType) {
    if (feedbackType == 'WF') {
        $('#openFeedbackModal_' + id).remove();
        $('.feedback_' + id).attr('disabled', false);
    }
    else {
        $('#openStepLevelFeedbackModal_' + id).remove();
    }
}

//Add WorkOrder Feedback 
function addFeedback(obj) {
    let commentObj = obj.getAttribute('data-feedbackdetails');
    commentObj = JSON.parse(commentObj);

    let projectID = commentObj.projectID;
    let workOrderID = commentObj.woId;
    let signum = signumGlobal;
    let wfName = commentObj.wfName;
    let defID = commentObj.flowchartDefId;
    let wfId = commentObj.wfId;
    let feedbackType = commentObj.feedbackType;
    let stepID = '', stepName = '';

    //if (feedbackType == 'STEP') {
    //    if ($('.instantFeedbackSelect option:selected').text() == 'others' && $("#comments_" + workOrderID).val() == "") {
    //        pwIsf.alert({ msg: "Please give your comments. ", type: "warning" });
    //    }
    //}
    //else if ($("#comments_" + workOrderID).val() == "") {
    //    pwIsf.alert({ msg: "Please give your comments. ", type: "warning" });
    //}
    //else {
    let wrkFlowFeedbackActivityModel = new Object();
    wrkFlowFeedbackActivityModel.feedbackComment = $("#comments_" + workOrderID).val();
    let feedbackObj = new Object();
    feedbackObj.projectID = projectID;
    feedbackObj.workOrderID = workOrderID;
    feedbackObj.signum = signum;
    feedbackObj.wfName = wfName;
    feedbackObj.defID = defID;
    feedbackObj.wFID = wfId;


    if (commentObj.feedbackType != 'WF') {
        stepID = commentObj.stepID;
        stepName = commentObj.stepName;

        feedbackObj.stepID = stepID;
        feedbackObj.stepName = stepName;
        feedbackObj.instantFeedbackID = $("#instantFeedback_" + workOrderID).val();
        feedbackObj.instantFeedback = $('#instantFeedback_' + workOrderID + ' option:selected').text();

    }
    feedbackObj.workFlowFeedbackActivityModel = wrkFlowFeedbackActivityModel;
    //console.log(commentObj);
    pwIsf.addLayer({ text: "Please wait ..." });


    //Ajax call 
    $.isf.ajax({
        type: "POST",
        url: service_java_URL + "activityMaster/saveWFFeedbackComments",
        crossdomain: true,
        processData: true,
        contentType: 'application/json',
        data: JSON.stringify(feedbackObj),
        success: function (data) {
            if (!data.isValidationFailed) {
                //pwIsf.alert({ msg: "Thank you for your Feedback!", type: "info", autoClose: 1 });  

                let historyObj = {};
                historyObj.woID = workOrderID;
                historyObj.flowchartDefId = defID;
                historyObj.feedbackType = feedbackType;
                historyObj.stepID = stepID;
                historyObj.stepName = stepName;

                getFeedbackHistory(historyObj);
                $("#comments_" + workOrderID).val("");
                $('#btnaddComments_' + workOrderID).attr('disabled', true);
                $('#current_' + workOrderID).text('Only 1000 characters are allowed (1000 left)');             

            }
            else {
                pwIsf.alert({ msg: data.formErrors[0], type: "info" });
            }
            //to add feedback thankyou text to the div     
            let newFeedbackHtml = '<span id="feedbackcomment"><b style="color:green;">Thank you for your Feedback!</b></span>';
            $("#FeedbackThankYouText").append(newFeedbackHtml);
        },
        error: function (status) {
            pwIsf.alert({ msg: "Error while saving feedback", type: "error" });
        },
        complete: function () {
            pwIsf.removeLayer();
           
        }
    });
}




function getFeedbackHistory(historyObj) {

    let woId = historyObj.woID;
    let feedbackType = historyObj.feedbackType;

    let feedbackHistoryObj = new Object();
    feedbackHistoryObj.workOrderID = historyObj.woID;
    feedbackHistoryObj.signum = signumGlobal;
    feedbackHistoryObj.defID = historyObj.flowchartDefId;

    if (feedbackType != 'WF') {
        feedbackHistoryObj.stepID = historyObj.stepID;
        feedbackHistoryObj.stepName = historyObj.stepName;
    }

    let result = '';

    pwIsf.addLayer({ text: "Please wait ..." });

    return $.isf.ajax({
        type: "POST",
        url: service_java_URL + "activityMaster/getFeedbackHistory",
        returnAjaxObj: true,
        async: false,
        crossdomain: true,
        processData: true,
        contentType: 'application/json',
        data: JSON.stringify(feedbackHistoryObj),
        success: function (data) {
            let feedbackHTML = '', deletecommentDetails = '';

            if (feedbackType == 'WF') { $('#feedbackHistoryDiv_' + woId).html(''); }
            else { window.parent.$('#feedbackHistoryDiv_' + woId).html(''); }

            //Make feedbackhistory div
            for (let i = 0; i < data.length; i++) {

                let feedbackStatus = data[i].feedbackStatus;
                let feedbackOwner = data[i].createdBy;

                let feedbackComment = data[i].feedbackComment;

                let currdate = data[i].modifiedOn.split(" ")[0];
                if (feedbackType == 'WF') {
                    deletecommentDetails = { workOrderID: woId, feedbackDetailID: data[i].feedbackDetailID, modifiedBy: data[i].modifiedBy, flowchartDefId: historyObj.flowchartDefId, feedbackType: feedbackType };
                }
                else {
                    deletecommentDetails = { workOrderID: woId, feedbackDetailID: data[i].feedbackDetailID, modifiedBy: data[i].modifiedBy, flowchartDefId: historyObj.flowchartDefId, stepID: historyObj.stepID, stepName: historyObj.stepName, feedbackType: feedbackType };
                }
                deletecommentDetails = JSON.stringify(deletecommentDetails);
                let deletebuttonHTML = 'none'

                if (feedbackStatus.toLowerCase() == "new" && feedbackOwner.toLowerCase() == signumGlobal.toLowerCase()) {
                    deletebuttonHTML = 'block';
                }

                if (feedbackComment != undefined && feedbackComment != null && feedbackComment != "") {
                    feedbackComment = escapeHtml(feedbackComment);
                }
                else {
                    feedbackComment = "NA";
                }
                feedbackHTML += `<div class="feedbackBar" ><span id="feedbackContent_${woId}">${feedbackComment}</span>
            <div class="feebackBarUserInfo">
                <div class="col-md-7" style="padding-left:0px;display:flex;"> <label for="feebackBy_${woId}"><b> Name(Signum): </b></label> <span id="feebackBy_${woId}" class="feedbackowner" title="${data[i].creatorName}(${feedbackOwner})" > ${data[i].creatorName}(${feedbackOwner}) </span> </div>
                <div class="col-md-4" style="padding-left:0px"> <label for="feedbackDate_${woId}"><b> Date: </b></label> <span id="feedbackDate_${woId}">${currdate}<span> </div>
                <div> <a href="javascript:void(0)" role="button" class="btn btn-xs pull-right btnFeedbackDelete" id="btnFeedbackDelete_${woId}" data-deleteDetails= '${deletecommentDetails}' onclick="removeFeedbackThankyoutext(); deleteWFLevelComment(this);" style="color:orangered; display:${deletebuttonHTML};"> <i class="fa fa-trash"> </i> </a> </div>
            </div>
            </div>`;
            }

            if (feedbackType == 'WF') { $('#feedbackHistoryDiv_' + woId).append(feedbackHTML); }
            else { window.parent.$('#feedbackHistoryDiv_' + woId).append(feedbackHTML); }

        },
        error: function (status) {
            pwIsf.alert({ msg: "Error in getting Feedback", type: "error" });
        },
        complete: function () {
            pwIsf.removeLayer();
        }
    });

}

//Delete WF Level Comment.
function deleteWFLevelComment(deleteObj) {

    let deleteObjDetails = JSON.parse(deleteObj.getAttribute('data-deleteDetails'));

    let workOrderID = deleteObjDetails.workOrderID;
    let feedbackDetailID = deleteObjDetails.feedbackDetailID;
    let modifiedBy = deleteObjDetails.modifiedBy;
    let flowchartDefId = deleteObjDetails.flowchartDefId;
    let feedbackType = deleteObjDetails.feedbackType;

    pwIsf.confirm({
        title: 'Delete Comment', msg:
            'Do you want delete the comment?',
        'buttons': {
            'Yes': {
                'action': function () {
                    pwIsf.addLayer({ text: "Please wait ..." });

                    $.isf.ajax({
                        type: "POST",
                        url: service_java_URL + "activityMaster/deleteFeedbackComment",
                        crossdomain: true,
                        processData: true,
                        contentType: 'application/json',
                        data: JSON.stringify(deleteObjDetails),
                        success: function (data) {
                            if (!data.isValidationFailed) {
                                // pwIsf.alert({ msg: data.formMessages[0], type: "info", autoClose: 2 });
                                let newFeedbackHtml = `<span id="feedbackcomment"><b style="color:green;">${data.formMessages[0]}</b></span>`;
                                $("#FeedbackThankYouText").append(newFeedbackHtml);

                                let historyObj = {};
                                historyObj.woID = workOrderID;
                                historyObj.flowchartDefId = flowchartDefId;
                                historyObj.feedbackType = feedbackType;

                                if (feedbackType != 'WF') {
                                    historyObj.stepID = deleteObjDetails.stepID;
                                    historyObj.stepName = deleteObjDetails.stepName;
                                }                              


                                ////remove the thank you text
                                //removeFeedbackThankyoutext();

                                getFeedbackHistory(historyObj);    
                                
                            }
                            else {
                                pwIsf.alert({ msg: data.formMessages[0], type: "info", autoClose: 2 });
                            }
                        },
                        error: function (status) {
                            pwIsf.alert({ msg: "Error while deleting", type: "error" });
                        },
                        complete: function () {
                            pwIsf.removeLayer();

                        }
                    });

                }
            },
            'No': {
                'action': function () {

                }
            },

        }
    });
}
//Feedback on WF level END

//Feedback on Step Level
//Step Sad Count
function makeStepSadFunction(makeStepSadObject) {
    if (window.parent.$('#openStepLevelFeedbackModal_' + makeStepSadObject.woID).length > 0) {
        window.parent.$('#openStepLevelFeedbackModal_' + makeStepSadObject.woID).remove();
        //window.parent.$('.feedback_' + woId).attr('disabled', false);
    }


    let currentTaskName = makeStepSadObject.taskName;
    let currentShape = makeStepSadObject.shape;
    let currentTaskID = makeStepSadObject.taskID;
    let bookingID = makeStepSadObject.bookingID;
    let bookingStatus = makeStepSadObject.status;
    let stepID = makeStepSadObject.stepID;
    let stepName = makeStepSadObject.stepName;
    let executionType = makeStepSadObject.execType;
    let cell = makeStepSadObject.currentCell;
    let arrAllShapes = makeStepSadObject.currentJson;
    let iconsOnNextStep = makeStepSadObject.iconsOnNextStep;
    let flowChartType = makeStepSadObject.flowChartType;
    let sadCount = makeStepSadObject.sadCount;

    let sadCountObj = {};
    sadCountObj.projectID = makeStepSadObject.projectID;
    sadCountObj.workOrderID = makeStepSadObject.woID;
    sadCountObj.signum = signumGlobal;
    sadCountObj.wfName = makeStepSadObject.wfName;
    sadCountObj.defID = makeStepSadObject.subActivityDefID;
    sadCountObj.wFID = makeStepSadObject.wFID;
    sadCountObj.stepID = stepID;
    sadCountObj.stepName = stepName;
    sadCountObj.workFlowFeedbackActivityModel = {
        "sadCount": sadCount
    }

    $.isf.ajax({
        type: "POST",
        async: false,
        //returnAjaxObj: true,
        url: service_java_URL + "activityMaster/saveStepSadCount",
        crossdomain: true,
        processData: true,
        contentType: 'application/json',
        data: JSON.stringify(sadCountObj),
        success: function (data) {
            if (!data.isValidationFailed) {
                makeContentIcon(makeStepSadObject);
                app.graph.fromJSON(app.graph.toJSON());
            }
            else {
                pwIsf.alert({ msg: data.formMessages[0], type: 'warning', alertCount: 2 });
            }
        },
        error: function (status) {
            pwIsf.alert({ msg: "Error in saving sad count", type: "error" });
        },
        complete: function () {
            pwIsf.removeLayer();
        }
    });

}

//Feedback on Step Level 
function createStepLevelFeedbackModal(stepFeedbackObj) {

    let woId = stepFeedbackObj.woid;
    let projectID = stepFeedbackObj.projectID;
    let stepID = stepFeedbackObj.stepID.toString();
    let wfId = stepFeedbackObj.wfID;
    let stepName = stepFeedbackObj.stepName;
    let flowchartDefId = stepFeedbackObj.subActivityDefID;
    let versionNo = stepFeedbackObj.versionNo;
    let wfName = stepFeedbackObj.wfName;

    stepName = stepName.replace(/(\r\n|\n|\r|)/gm, "");
    stepName = stepName.replace(/'/g, '&apos;');
    // stepName = stepName.replace(/'\/g,"\\'");
    let commentsDetails = new Object();
    commentsDetails.woId = woId;
    commentsDetails.projectID = projectID;
    commentsDetails.stepID = stepID;
    commentsDetails.wfId = wfId;
    commentsDetails.stepName = stepName;
    commentsDetails.flowchartDefId = flowchartDefId;
    commentsDetails.versionNo = versionNo;
    commentsDetails.feedbackType = 'STEP';
    commentsDetails.wfName = wfName;

    let stringifyComments = JSON.stringify(commentsDetails);

    //if (window.parent.$('.feedbackBox').length > 0) {
    //    window.parent.$('.feedbackBox').remove();
    //    window.parent.$('.feedback_' + woId).attr('disabled', false);
    //}
    if (window.parent.$('#openStepLevelFeedbackModal_' + woId).length > 0) {
        window.parent.$('#openStepLevelFeedbackModal_' + woId).remove();
        window.parent.$('.feedback_' + woId).attr('disabled', false);
    }

    if (window.parent.$('#openFeedbackModal_' + woId).length > 0) {
        window.parent.$('#openFeedbackModal_' + woId).remove();
        window.parent.$('.feedback_' + woId).attr('disabled', false);

    }

    let html = `<div class="feedbackBox" id="openStepLevelFeedbackModal_${woId}" >
        <div class="col-md-13">
            <div style="text-align:center;background-color: #d9edf7;height: 26px;line-height: 2;">
                <span><b>Add Step Feedback</b></span> <button type="button" class="close pull-right" style="padding-right: 3px;" onclick="closeFeedbackBox('${woId}', 'STEP')">&times;</button>
            </div>        
        </div>   

        <div class="col-md-12" id="FeedbackThankYouText" style="padding-top:10px;padding-bottom:10px;">

        </div>

        <div class="col-md-12" style="margin-top:10px;"><label for="stepInfo">Step Name: </label>
            <input type="text" class="form-control" id="stepInfo" value="${stepName}" disabled />
        </div><br />

        <div class="col-md-12" onclick="removeFeedbackThankyoutext()"><span class="reqFld">*</span><label for="woidInfo">Instant Feedback: </label>
          <select class="select2able instantFeedbackSelect" id="instantFeedback_${woId}" onchange="instantFeedbackChange(this, ${woId})"></select>
        </div><br />

        <div class="col-md-12"><label for="comments">Comments: </label><span> (If Instant Feedback = "others", comments are mandatory.)</span>
             <textarea class="comments" id="comments_${woId}" style="width: 100%" rows="4" cols="65" maxLength="1000" onclick="removeFeedbackThankyoutext()" onkeyup="validateComments(this, 'STEP', ${woId})" required></textarea>
        </div>

        <div class="col-md-12"><span id="current_${woId}" style="color:darkgray" class="pull-right">Only 1000 characters are allowed (1000 left) </span></div><br/>
        <div class="col-md-12"> <input type="button" class="btn btn-primary pull-right btnaddComments" id="btnaddComments_${woId}" value="Add" onclick="addFeedback(this)" data-feedbackdetails = '${stringifyComments}' disabled /> </div>
        <br/><br/>

        <div class="col-md-12"> <span> <b> Feedback History (Latest 10): </b> </span> </div>
        <div class="col-md-12 stepLevelFeedbackHistoryDiv" id="feedbackHistoryDiv_${woId}">
           
        </div>
    </div>`;

    window.parent.$('#flow_chart_single_box_' + woId).append(html);

    let feedbackParent = window.parent.document.getElementById('flow_chart_single_box_' + woId);

    let feedbackBox = $(feedbackParent).find('.feedbackBox');
    if ($(feedbackBox).css('display') == "none" || $(feedbackBox).css('display') == "") {
        $(feedbackBox).css('display', 'block');

    }

    let historyObj = {};
    historyObj.woID = woId;
    historyObj.flowchartDefId = flowchartDefId;
    historyObj.stepID = stepID;
    historyObj.stepName = stepName
    historyObj.feedbackType = 'STEP';

    getInstantFeedback(woId, feedbackBox);

    getFeedbackHistory(historyObj);


}

//Get Instant Feedbacks from Master Data

function getInstantFeedback(id, feedbackBox) {

    $.isf.ajax({
        type: "GET",
        async: false,
        //returnAjaxObj: true,
        url: service_java_URL + "activityMaster/getInstantFeedbackForDropDown",
        crossdomain: true,
        processData: true,
        success: function (data) {

            let option = '';
            option += '<option value="-1" disabled selected>Select One </option>';
            //  $('select#toolsCombo.select2-hidden-accessible').find('option').remove();
            for (let i = 0; i < data.length; i++) {
                option += '<option value="' + data[i].instantFeedbackID + '">' + data[i].feedbackText + '</option>';
            }
            let selectBox = $(feedbackBox).find('.instantFeedbackSelect');
            $(selectBox).append(option);

        },
        error: function (status) {
            pwIsf.alert({ msg: "Error in getting data", type: "error" });
        },
        complete: function () {
            pwIsf.removeLayer();
            window.parent.$('#instantFeedback_' + id).select2();
        }
    });
}
//Feedback on Step Level END 


//to remove feedback thankyou text from the div  
function removeFeedbackThankyoutext() {
    $("#feedbackcomment").remove();
}


//
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