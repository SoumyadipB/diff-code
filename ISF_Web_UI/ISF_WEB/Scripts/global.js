var IFrameURL = ""; Glob_sortOrder = 'newest';
var isWorkFlowAutoSenseEnabled = undefined;
var isWorkOrderAutoSenseEnabled = undefined;

const neType = "Network Element Type";
const neName = "Network Element Name/ID";
const neMarket = "Market";
const shortNeNameAsNEID = "NEID";
const deleteNESuccessMsg = "Network Element Deleted";
const deleteNEFailMsg = "Fail to delete network element";
//constants for NE Add Edit
const C_NE_VALIDATE = 'Validate';
const C_NE_ADD_EDIT_SAVE = 'Save';
const C_NE_TYPE_NETWORK = 'NETWORK';
const C_NE_TYPE_CLUSTER = 'CLUSTER';
const C_NE_TYPE_SITE = 'SITE';
const C_NE_TYPE_NODE = 'NODE';
const C_NE_TYPE_LINK = 'LINK';
const C_DO_CLICK = 'DOPlus';
const C_DEL_EXCUTION_CLICK = 'DeliveryExecution';
const C_DPLAN_CLICK = 'DOPlan';
const C_NE_STATUS_GROUP = 'Group';
const C_NE_STATUS_VALID = 'Valid';
const C_NE_STATUS_INVALID = 'InValid';
const C_CONTENT_TYPE_APPLICATION_JSON = 'application/json; charset=utf-8';
const C_ID_TABLE_NE_DETAILS = '#table_NE_Details';
const C_PLEASE_WAIT = 'Please wait ...';
const C_PASTE = 'paste';
const C_INPUT = 'input';
const C_CONTENT_TYPE = 'application/json';
const C_COMMON_ERROR_MSG = 'An error occurred';
const C_WARNING = 'warning';
const C_ERROR_MSG = 'Error Occured!';
const C_INFO = 'info';
const C_API_POST_REQUEST_TYPE = 'POST';
const C_API_GET_REQUEST_TYPE = 'GET';
const C_PDF_EXT = 'pdf';
const C_EXCEL_EXT = 'xls';
const C_EXCELX_EXT = 'xlsx';
const C_DOC_EXT = 'doc';
const C_DOCX_EXT = 'docx';
const C_TXT_EXT = 'txt';
const C_WRONG_TYPE = "Wrong File Type Selected";
const C_ZIP_EXT = "zip";
const C_WHL_EXT = "whl";
const C_VALID_DOC_URL = "Please enter valid document Url.";
const C_MP4 = "mp4";
const C_MOV = "mov";
const C_WMV = "wmv";
const C_AVI = "avi";
const C_FLV = "flv";
const C_MKV = "mkv";
const C_M4A = "m4a";
const C_MP3 = "mp3";
const C_WAV = "wav";
const C_WMA = "wma";
const C_AAC = "aac";
const C_MPEG = "mpeg";
const C_MPG = "mpg";
const C_MOVIES_MSG = "Audio & Video files are not allowed";
const C_WHL_FILE_NAME = "whlfile";
const C_ERROR = "error";
const C_EXEC_TYPE_MAN = "Manual";
const C_EXEC_TYPE_AUTO = "Automatic";

$(document).click(function (e) { 

    if (e.target.id != "arHead") {
        $('#mydivheader').css({
            height: '40px'
        });
        $('div #arHead').removeClass('arrow-down').addClass('arrow-up');
    }
})
$(document).ready(function () {    
  
    $(".select2able").select2();
    var url = window.location.pathname;    
    //console.log(url);
    var instancee = ""; //para probar en local 
    var id = "";
    context = "";
    if (localStorage.getItem("clickedWOID") != 'undefined') {
        clickedWOID = JSON.parse(localStorage.getItem("clickedWOID"));
    }
    if (IsAspUser == "False" || (IsAspUser == "True" && AspStatus == "APPROVED"))
    var role = JSON.parse(ActiveProfileSession).role;
    flagButtonBoolean = mailButtonBoolean = 0;
    var allMenueIdsWithUrl = JSON.parse(MenueIdsWithUrlSession);

    //console.log(allMenueIdsWithUrl);

    for (var k in allMenueIdsWithUrl) {

        if (allMenueIdsWithUrl[k]['url'] == instancee + url) {
            id = allMenueIdsWithUrl[k]['id'];
            break;
        }

    }

     $("#"+this.id).addClass("active expanded");
    $("#navbar").addClass("demo");
    //Make the DIV element draggagle:
    dragElement(document.getElementById("loginMsgdiv"));
    getActiveLoginMessage();
    $('#arHead').click(function (e) {
        if ($('div #arHead').hasClass('arrow-up')) {
            $('#mydivheader').css({
                height: 'fit-content'

            });
            $('div #arHead').removeClass('arrow-up').addClass('arrow-down');
        }
        else {
            $('#mydivheader').css({
                height: '40px'

            });
            $('div #arHead').removeClass('arrow-down').addClass('arrow-up');
        }
        e.stopPropagation();
    });  

    if (sessionStorage.getItem("msgFlag") != 'true') {
        sessionStorage.setItem("msgFlag", false);
    }

    

});


function getUrlParams() {
    // IT WILL RETURN undefined IF PARAM IS NOT IN URL
    var vars = {};
    var parts = window.location.href.replace(/[?&]+([^=&]+)=([^&]*)/gi, function (m, key, value) {
        vars[key] = value;
    });
    return vars;
}

function setIFrameURL(iFURL) {
    IFrameURL = iFURL;    
}
function getParameterByName(name, url) {
    if (!url) url = window.location.href;
    name = name.replace(/[\[\]]/g, '\\$&');
    var regex = new RegExp('[?&]' + name + '(=([^&#]*)|&|#|$)'),
        results = regex.exec(url);
    if (!results) return null;
    if (!results[2]) return '';
    return decodeURIComponent(results[2].replace(/\+/g, ' '));
}
function getCommentCategory() {
    var urlParams = new URLSearchParams(window.location.search);
    var commentCategory = urlParams.get("commentCategory");
    if ((commentCategory == null || commentCategory == undefined) && IFrameURL!=undefined) {
        commentCategory = getParameterByName('commentCategory', IFrameURL)
    }
    
    var commentCategoryStep = "";
    commentCategoryStep = commentCategory;
    var workOrderOrNot = window.parent.$('#selectStepsWO' + clickedWOID + ' :selected').text() 
    if (workOrderOrNot == null || workOrderOrNot == undefined) {
        workOrderOrNot = "";
    }
    else if (!workOrderOrNot.includes("WorkOrder") && workOrderOrNot != "") {

        if (commentCategory == "WO_LEVEL") {
            commentCategoryStep = "WO_STEP_LEVEL";
        }
        else if (commentCategory == "WO_DT_WO_LEVEL") {
            commentCategoryStep = "WO_DT_STEP_LEVEL";
        }
        else if (commentCategory == "WO_DA_WO_LEVEL") {
            commentCategoryStep = "WO_DA_STEP_LEVEL";
        }

    }
    return commentCategoryStep;

}

function getIcon(iType, options) {

    var defaults = {
        textWith: '',
        textCss: 'font-size:9px;padding-right: 3px;'
    };

    var actual = $.extend({}, defaults, options || {});
    var icons = [];

    var text = '';

    if (actual.textWith) {
        text = '<span style="' + actual.textCss + '">' + actual.textWith + '</span>';
    }
    icons['reopen'] = text + '<i class="fa fa-repeat"></i>';
    icons['add'] = text + '<i class="fa fa-plus"></i>';
    icons['edit'] = text + '<i class="fa fa-edit"></i>';
    icons['delete'] = text + '<i class="fa fa-trash-o"></i>';
    icons['view'] = text + '<i class="fa fa-eye"></i>';
    icons['reinstate'] = text + '<i class="fa fa-retweet"></i>';
    icons['flowchart'] = text + '<i class="fa fa-sitemap"></i>';
    icons['user'] = text + '<i class="fa fa-user"></i>';
    icons['signout'] = text + '<i class="fa fa-sign-out"></i>';
    icons['clock'] = text + '<i class="fa fa-clock-o"></i>';
    icons['accept'] = text + '<i class="fa fa-check"></i>';
    icons['reject'] = text + '<i class="fa fa-times"></i>';
    icons['close'] = text + '<i class="fa fa-close"></i>';
    icons['plan'] = text + '<i class="fa fa-file-powerpoint-o"></i>';
    icons['comments'] = text + '<i class="fa fa-comments"></i>';
    icons['file'] = `${text}<i class="fa fa-file-o"></i>`; 
    icons['calendar'] = `${text}<i class="fa fa-calendar"></i>`; 
    return icons[iType];
}

//

function resizeCell(cellModel) {
    _.each(cellModel.getElements(), function (el) {
        var letterSize = 8;
        var width = el.attributes.size.width;
        var height = 2 * ((el.attributes.attrs.text.text.split('\n').length + 1) * letterSize);
        height = height + 50;
        el.set({ size: { width: width, height: height } })
        app.layoutDirectedGraph()
    });
}
// A COMMON SCRIPT/FUNCTION FOR LOADER  
function pleaseWaitISF(options) {

    var defaults = {
        showSpin: true,
        spinCss: 'font-size:24px;',
        text: '',
        textCss: 'font-size:12px;vertical-align: text-bottom;padding-left: 5px;',
        progressId:''
    };

    var actual = $.extend({}, defaults, options || {});

    var spinCss = actual.spinCss;
    var textCss = actual.textCss;
    var text = actual.text;
    var showSpin = actual.showSpin;

    var spinIcon = (showSpin) ? '<i class="fa fa-spinner fa-spin" style="' + spinCss + '"></i>' : '';
    var loadingText = (text != '') ? '<span style="' + textCss + '"> ' + text + '</span>' : '';

    let progressHtml = (actual.progressId != '') ? '<progress id="' + actual.progressId + '" value="0"></progress>' : '';
    
    var retStr = progressHtml+'<div style="display:block;text-align:center;width:100%;"> ' + spinIcon + loadingText + '</div>';

    return retStr;
}

function errorHandler(data) {
    for (var i = 0; i < Object.keys(data.formErrors).length; i++) {
        pwIsf.alert({ msg: Object.values(data.formErrors), type: 'error'});
    }
}

function responseHandlerPA(data) {
    if (data.formErrorCount > 0) {
        var str = "Warning:";
      
            str += "<li>" + data.formErrors[0].replace(/\n/g, '<br/>') + "</li>";
        
        pwIsf.alert({ msg: str, type: 'warning' });
        return false;
    }
    else if (data.formMessageCount > 0) {
        var str = "";
        for (var i = 0; i < Object.keys(data.formMessages).length; i++) {
            if (str != data.formMessages[i].replace(/\n/g, '<br/>')) {
                str += data.formMessages[i].replace(/\n/g, '<br/>');
            }
        }
        pwIsf.alert({ msg: str, type: 'success', autoClose: 3 });
        return true;
    }
    else if (data.formWarningCount > 0) {
        var str = "";
        for (var i = 0; i < Object.keys(data.formWarnings).length; i++) {
            str += data.formWarnings[i].replace(/\n/g, '<br/>');
        }
        pwIsf.alert({ msg: str, type: 'warning' });
        return true;
    }
}


function responseHandler(data) {
    if (data.formErrorCount > 0) {
        var str = "Error(s):";
        for (var i = 0; i < Object.keys(data.formErrors).length; i++) {
            str += "<li>"+ data.formErrors[i].replace(/\n/g, '<br/>')+"</li>";
        }
        pwIsf.alert({ msg: str, type: 'error' });
        return false;
    }
    else if (data.formMessageCount > 0) {
        var str = "";
        for (var i = 0; i < Object.keys(data.formMessages).length; i++) {
            if (str != data.formMessages[i].replace(/\n/g, '<br/>')) {
                str += data.formMessages[i].replace(/\n/g, '<br/>');
            }
        }
        pwIsf.alert({ msg: str, type: 'success', autoClose: 3 });
        return true;
    }
    else if (data.formWarningCount > 0) {
        var str = "";
        for (var i = 0; i < Object.keys(data.formWarnings).length; i++) {
             str += data.formWarnings[i].replace(/\n/g, '<br/>');
        }
        pwIsf.alert({ msg: str, type: 'warning' });
        return true;
    }
    
}
//Comments Start
function getCommentsGeneric(pageID, commentsArray)
{   
    
    var commentsContainer = $('#comments-container' + pageID);
    commentsContainer.comments({
        profilePictureURL: (UserImageUriSession == 'null' || UserImageUriSession == null || UserImageUriSession == '') ? null : localStorage.UserImageUriSession,
        roundProfilePictures: true,
        getComments: function (success, error) {
            success(commentsArray);
            //enrichComments(commentsArray);
            $.each( commentsArray, function( index, comm ){
                formatComment(comm);
            });
        },
        postComment: function (commentsJSON, success, error) {        
            var commentCategoryNew = getCommentCategory();
            context = $('#selectStepsWO' + woid + ' :selected').text();
            var n = pageID.lastIndexOf('_');
            var auditID = pageID.substring(n + 1);
            var groupID = pageID.substring(0, n);

            commentsJSON.content = commentsJSON.content.replace(/`/g, ' ');

            commentsJSON.auditPageId = auditID;
            commentsJSON.auditGroupCategory = groupID;
            commentsJSON.fullname = signumGlobal;
            commentsJSON.context = context;
            commentsJSON.notificationFlag = mailButtonBoolean;
            commentsJSON.importance = flagButtonBoolean;
            commentsJSON.commentCategory = commentCategoryNew;
            let isAuditColumnlengthExceeded = false;

            if (commentsJSON.content.length > 1000 || commentsJSON.context.length > 1000) {
                isAuditColumnlengthExceeded = true;
            }
            if (!isAuditColumnlengthExceeded) {
                $.isf.ajax({
                    type: 'post',
                    contentType: "application/json; charset=utf-8",
                    url: service_java_URL + "auditing/addComment",
                    data: JSON.stringify(commentsJSON),
                    success: function (data) {
                        let comment = data.responseData;

                        if (data.isValidationFailed == true) {
                            pwIsf.alert({ msg: data.formErrors[0], type: 'warning' });
                        }
                        else {
                            // pwIsf.alert({ msg: data.formMessages[0], type: 'success', autoClose: 3 });
                            NotifyMobileDevice(commentsJSON, comment);
                            success(comment);
                            formatComment(comment);
                        }
                    },
                    error: error
                });
            }

            else {
                pwIsf.alert({ msg: "Max comment character length(1000) exceeded", type: "error", autoClose: 4 });
            }

        },
        timeFormatter: function(time) {
            return ConvertDateTime_tz(new Date(time)).toLocaleString();
        }
    });

    

}

function showCurrentExecutingStep(stepName){

}

function searchInComments(that, uniquePageID) {
    $('#prevComments' + clickedWOID).addClass('disabledbutton');
    $('#moreComments' + clickedWOID).addClass('disabledbutton');
    $('#labelAllComments' + clickedWOID).css("display", "inline-block");
    var role = JSON.parse(ActiveProfileSession).role;
     searchtext = window.parent.$("#searchInComments"+clickedWOID)[0].value;
    if(searchtext!="")
        var textToBeSearched = $(that).val().toLowerCase();
    else
        var textToBeSearched=searchtext;
        getCommentsSection(uniquePageID, textToBeSearched,role);
}

function searchCommentsOnEnter(e, that, uniquePageID){
    var keycode = (e.keyCode ? e.keyCode : e.which);
    
    if (keycode == '13') {
        searchInComments(that, uniquePageID);
    }
    //else if(keycode == '8'){
    //    searchInComments(that, uniquePageID);
    //}
}

//On Click of Step Comments get filtered on basis of Step
function filterCommentsByStep(stepName)
{
    window.parent.$("#searchInComments"+clickedWOID)[0].value=stepName;
    uniquePageID = "WORK_ORDER_"+clickedWOID;
    textToBeSearched = stepName;
    //commentCategoryNew3 = getCommentCategory();


    getCommentsSection(uniquePageID, textToBeSearched, '');

   

}

//On Running Step Context dropdown changes to current running Step
function setRunningStep(stepID, stepName)
{
    //window.parent.$('#selectStepsWO' + clickedWOID).val($.trim(stepName)).trigger('change');
    window.parent.$('#selectStepsWO' + clickedWOID).val(stepID).trigger('change');
    filterCommentsByStep(stepName);
}

function setRunningDisabledStep(woId) {
    let disabledStepName = "WORK_ORDER_" + woId;
    let disabledStepValue = "WorkOrder " + woId;
    $('#selectStepsWO' + woId).val(disabledStepValue).trigger('change');
    filterCommentsByStep(disabledStepName);
}

//Download Comments Data in CSV
function downloadCommentsCSV() {
    var csv = 'Name,Title\n';
    
    arrayComments = Array.prototype.slice.call(commentsArrayGlobal);
    arrayComments.forEach(function(row) {
        csv += row.join(',');
        csv += "\n";
    });
    var hiddenElement = document.createElement('a');
    hiddenElement.href = 'data:text/csv;charset=utf-8,' + encodeURI(csv);
    hiddenElement.target = '_blank';
    hiddenElement.download = 'people.csv';
    hiddenElement.click();
}

function convertArrayOfObjectsToCSV(args) {  
    var result, ctr, keys, columnDelimiter, lineDelimiter, data;

    data = args.data || null;
    if (data == null || !data.length) {
        return null;
    }

    columnDelimiter = args.columnDelimiter || ',';
    lineDelimiter = args.lineDelimiter || '\n';

    var obj = data[0];
    ['parent', 'modified','fileURL','auditgroupid','fieldName','oldValue','newValue','id','profile_picture_url',''].forEach(e => delete obj[e]);
    keys = Object.keys(obj);
    result = '';
    result += keys.join(columnDelimiter);
    result += lineDelimiter;

    data.forEach(function(item) {
        ctr = 0;
        keys.forEach(function(key) {
            if (ctr > 0) result += columnDelimiter;

            var val;
            if(key=='created'){
                val=new Date(item[key]).toLocaleString()
            }else{
                val=item[key]
            }

            result += removeComma(removeLine(val));
            ctr++;
        });
        result += lineDelimiter;
    });

    return result;
}

//Download Comments Data using API
function downloadCommentsData(uniquePageID) {

    if (uniquePageID == undefined) {
        console.log("Page ID undefined");
    } else {

        let searchText = '';
    
        let u = "pageId=" + uniquePageID + "&searchString=" + searchText;
        
        let urlEncode = encodeURIComponent(u);
        //cl(urlEncode);
        let serviceUrl = `${service_java_URL_VM}auditing/downloadAuditData?${urlEncode}`;
        
        let fileDownloadUrl;

        fileDownloadUrl = UiRootDir + "/data/GetFileFromApi?apiUrl=" + serviceUrl;

        window.location.href = fileDownloadUrl;
    }
}
//Download Comments Data
function downloadCSV(args) {  
    var data, filename, link;
    if (args.pageID == undefined) {
        var csv = convertArrayOfObjectsToCSV({
            data: commentsArrayGlobal
        });
        if (csv == null) return;

        filename = args.filename || 'export.csv';

        if (!csv.match(/^data:text\/csv/i)) {
            csv = 'data:text/csv;charset=utf-8,' + csv;
        }
        data = encodeURI(csv);

        link = document.createElement('a');
        document.body.appendChild(link);
        link.setAttribute('href', data);
        link.setAttribute('download', filename);
        link.click();
        document.body.removeChild(link);
    }
    else {
        var pageID = args.pageID;
        //var pageID = args.filename.split('.')[0].substring(9);
        pwIsf.addLayer({ msg: "Please wait while your file is being downloaded..." });
        var searchText = '';
        var serviceUrl;
        serviceUrl = service_java_URL + 'auditing/getAuditData?pageId=' + pageID + '&searchString=' + searchText;
        if (ApiProxy)
            serviceUrl = service_java_URL + 'auditing/getAuditData?' + encodeURIComponent("pageId=" + pageID + "&searchString=" + searchText);

        $.isf.ajax({
            type: 'get',
            url: serviceUrl,
            success: function (data) {
                if (data.ErrorFlag == false) {
                    var csv = convertArrayOfObjectsToCSV({
                        data: data.commentsData
                    });
                    if (csv == null) return;

                    filename = args.filename || 'export.csv';

                    if (!csv.match(/^data:text\/csv/i)) {
                        csv = 'data:text/csv;charset=utf-8,' + csv;
                    }
                    data = encodeURI(csv);

                    link = document.createElement('a');
                    document.body.appendChild(link);
                    link.setAttribute('href', data);
                    link.setAttribute('download', filename);
                    link.click();
                    document.body.removeChild(link);
                    pwIsf.removeLayer();
                }
                else {
                    pwIsf.alert({ msg: data.Error, type: 'error' });
                    pwIsf.removeLayer();
                }
            }

        });
    }
    
}

// remove Line 

function removeLine(data) {
    String.prototype.replaceAt = function (index, replacement) {
        return this.substr(0, index) + replacement + this.substr(index + replacement.length);
    };

    //$("#theTextArea2").val('test');
    var text = data;
    var match = /\r|\n/.exec(text);
    var str = text;
    while (match) {

        str = str.replaceAt(match.index, "~");
        str = str.replaceAt(match.index, "^");
        // $("#theTextArea2").val(str);
        match = /\r|\n/.exec(str);

    }
    return str;
}

function removeComma(data) {
    String.prototype.replaceAt = function (index, replacement) {
        return this.substr(0, index) + replacement + this.substr(index + replacement.length);
    };

    //$("#theTextArea2").val('test');
    var text = data;
    var match = /\r|,/.exec(text);
    var str = text;
    while (match) {

        str = str.replaceAt(match.index, " ");
        // $("#theTextArea2").val(str);
        match = /\r|,/.exec(str);

    }
    return str;
}
//Change Divs on change of checkbox - Bot Language - EORSTVE//
function toggleCheckbox(element) {
    var inputRqdStr = 'Bot Input Required';
    var inputNotRqdStr = 'Bot Input Not Required';
    var greenColor = '#1fb65b';
    var greyColor = '#868598';
    if (!element.checked) {
        $(element).closest('.inputGroup').find('.inputDiv').addClass("disabledbutton");
        $(element).closest('.inputGroup').find('.inputLabel').css("color", greyColor);
        $(element).closest('.inputGroup').find('.inputLabel').text(inputNotRqdStr);
        $(element).closest('.inputGroup').find('.inputDiv').removeAttr("required");
        $(element).closest('.inputGroup').find('.inputDiv').removeClass("required");
        $(element).closest('.inputGroup').find('.infile').addClass("disabledbutton");
    }
    else {
        $(element).closest('.inputGroup').find('.inputDiv').removeClass("disabledbutton");
        $(element).closest('.inputGroup').find('.inputLabel').css("color", greenColor);
        $(element).closest('.inputGroup').find('.inputLabel').text(inputRqdStr);
        $(element).closest('.inputGroup').find('.inputDiv').addClass("required");
        $(element).closest('.inputGroup').find('.inputDiv').attr("required",true);
        $(element).closest('.inputGroup').find('.infile').removeClass("disabledbutton");
    }
}
//Bot Language Pill Click - Check/Uncheck respective Divs - To create Dynamic IDs - EORSTVE//
// e.g : inputRequiredCheckboxOwnDev-macroDiv
$("#languageUl_forCreation > li > a").each(function (index) {
    $(this).on("click", function () {
        PILLNAME = $(this).attr("href").replace("#", "");
        document.getElementsByClassName("inputRequiredCheckboxOwnDev")[index].setAttribute("id", "inputRequiredCheckboxOwnDev-" + PILLNAME);
        document.getElementsByClassName("forLabel")[index].setAttribute("for", "inputRequiredCheckboxOwnDev-" + PILLNAME);
    });
});
//Get Comments
var cDataExists = false;
//var flog1 = true;
var vars = {};
function getCommentsSection(pageID, searchText = '', role = '', woLevel = '', start = 0, cFlag = true )
{
   
    cDataExists = false;
        var length = 5;
    //var start = 0;
        var woid = pageID.split("_")[2];
    clickedWOID = woid;

    vars['flog' + woid] = true;
        if($('#comments-container' + pageID).val() != undefined)
            var commentsContainer = $('#comments-container' + pageID);
        else
            var commentsContainer = window.parent.$('#comments-container' + pageID);
    if(role=="Delivery Responsible" || role=="Project Manager"){
        mailButtonBoolean = 0;
        $('button#mailStateButton').removeClass('btn-info-outline').addClass('btn-success');
    }
    else if(role=="Operational Manager")
        mailButtonBoolean = 0
    else{
        role = JSON.parse(ActiveProfileSession).role;
        if(role=="Default User")
            role ="Network Engineer";
        mailButtonBoolean = 0;
    }
    
    //GET Comments
    ////A callback function that is used to fetch the comments array from the server
    ////The success callback takes the comment array as a parameter.
    commentsContainer.comments({
        profilePictureURL: (UserImageUriSession == 'null' || UserImageUriSession == null || UserImageUriSession == '') ? null : UserImageUriSession,
        roundProfilePictures: true,
        getComments: function (success, error) {


            if (cFlag == true) {
                var serviceUrl;
                var urlParams = new URLSearchParams(window.location.search);
                var commentCategory = urlParams.get("commentCategory");
                if (searchText != "" || commentCategory != null) {
                    serviceUrl = service_java_URL + 'auditing/getAuditData?pageId=' + pageID + '&searchString=' + searchText;
                    if (ApiProxy)
                        serviceUrl = service_java_URL + 'auditing/getAuditData?' + encodeURIComponent("pageId=" + pageID + "&searchString=" + searchText);
                }
                else {
                    $('#moreComments' + woid).removeClass('disabledbutton');
                    serviceUrl = service_java_URL + 'auditing/getAuditData?pageId=' + pageID + '&searchString=' + searchText + '&start=' + start + '&length=' + length;
                    if (ApiProxy)
                        serviceUrl = service_java_URL + 'auditing/getAuditData?' + encodeURIComponent("pageId=" + pageID + "&searchString=" + searchText + '&start=' + start + '&length=' + length);

                }

                $.isf.ajax({
                    type: 'get',
                    url: serviceUrl,
                    success: function (data) {
                        if (data.ErrorFlag == false) {
                            if (data.commentsData.length == 0)
                                $('#moreComments' + woid).addClass('disabledbutton');
                            enrichComments(data);
                            commentsArrayGlobal = data.commentsData;
                            success(data.commentsData);
                            customProcessing(data.commentsData, pageID);
                            cDataExists = true;
                            vars['flog' + woid] = false;
                        }
                        else {
                            pwIsf.alert({ msg: data.Error, type: 'error' });
                        }
                    },
                    error: error

                });
            }
            else {
                
                $(".spinner").hide();
               // $("button").remove("#expandAndCollapseComments");

                var elems = document.getElementsByClassName("data-container")

                for (x = 0; x < elems.length; x++) {
                    // console.log(elems[x].classList.length)
                    if (elems[x].classList.length < 3) {

                        elems[x].classList.add("in")
                        elems[x].classList.add("customCollapse" + woid)

                        // elems[x].addClass("in").addClass("customCollapse" + woid);
                        $(".navigation-wrapper").addClass("in").addClass("customCollapse" + woid);
                    }
                }       


                var telems = document.getElementsByClassName("commenting-field");

                for (x = 0; x < telems.length; x++) {

                    if (telems[x].children[2] == undefined) {

                        var html = '<button data-toggle="collapse" data-target=".customCollapse' + woid + '" class="fa fa-angle-down" style="float:right;" type="button" id="expandAndCollapseComments"></button>'
                        telems[x].insertAdjacentHTML('beforeend', html);
                    }
                }


              //  $(".commenting-field").append($('<button data-toggle="collapse" data-target=".customCollapse' + woid + '" class="fa fa-angle-down" style="float:right;" type="button" id="expandAndCollapseComments"></button>'));
            }
            
        },
        //Add New Comments/POST Comments
        //A callback function that is used to create a new comment to the server. 
        //The first parameter of the callback is commentJSON that contains the data of the new comment. 
        //The callback provides both success and error callbacks which should be called based on the result from the server. 
        //The success callback takes the created comment as a parameter.
        postComment: function (commentsJSON, success, error) {
            cFlag = true;
            var commentCategoryNew2 = getCommentCategory();
            //context = $('#selectStepsWO'+woid+' :selected').text();
            context = window.parent.$('#selectStepsWO' + clickedWOID + ' :selected').text()
            //context = window.parent.$('#selectStepsWO' + woid).val();
            mailButtonBoolean = window.parent.mailButtonBoolean;
            flagButtonBoolean = window.parent.flagButtonBoolean;
            var n = pageID.lastIndexOf('_');
            var auditID = pageID.substring(n + 1);
            var groupID = pageID.substring(0, n);
            commentsJSON.content = commentsJSON.content.replace(/`/g, ' ');
            commentsJSON.auditPageId = auditID;
            commentsJSON.auditGroupCategory = groupID;            
            commentsJSON.fullname = signumGlobal;
            commentsJSON.context = context;
            commentsJSON.notificationFlag = mailButtonBoolean;
            commentsJSON.importance = flagButtonBoolean;
            commentsJSON.actorType = role;
            commentsJSON.commentCategory = commentCategoryNew2;

            let isAuditColumnlengthExceeded = false;

            if (commentsJSON.content.length > 1000 || commentsJSON.context.length > 1000) {
                isAuditColumnlengthExceeded = true;
            }
            if (!isAuditColumnlengthExceeded) {
                $.isf.ajax({
                    type: 'post',
                    contentType: "application/json; charset=utf-8",
                    url: service_java_URL + "auditing/addComment",
                    data: JSON.stringify(commentsJSON),
                    success: function (data) {
                        let comment = data.responseData;
                        if (data.isValidationFailed == true) {
                            pwIsf.alert({ msg: data.formErrors[0], type: 'warning' });
                        }
                        else {
                            NotifyMobileDevice(commentsJSON, comment);
                            //  pwIsf.alert({ msg: data.formMessages[0], type: 'success', autoClose: 3 });
                            comment.profile_picture_url = (UserImageUriSession == 'null' || UserImageUriSession == null || UserImageUriSession == '') ? null : UserImageUriSession,
                                success(comment);
                            formatComment(comment);
                            customProcessing(comment.commentsData, pageID);
                            cDataExists = true;
                            //$("#refreshAudit").click();
                            pwIsf.addLayer({ text: "Please wait ..." });
                            setTimeout(function () {

                                if (!$("button[data-target='.collapse" + clickedWOID + "']").hasClass("fa-angle-up") && vars['flog' + woid]) {
                                    $("#expandAndCollapseComments").click();
                                    vars['flog' + woid] = false;
                                }
                                pwIsf.removeLayer();
                            }, 1000);
                        }


                    },
                    error: error
                });
            }
            else {
                pwIsf.alert({ msg: "Max comment character length(1000) exceeded", type: "error", autoClose: 4 });
            }



        },
        refresh: function () {
            commentsContainer.addClass('rendered');
        },
        timeFormatter: function(time) {
            return ConvertDateTime_tz(new Date(time)).toLocaleString();
        }
    });

    

}

var booleanIsFirstRender=true;

function enrichComments(response){
    //if(response.commentsData!=undefined){
        $.each( response.commentsData, function( index, comm ){
            comm.profile_picture_url=response.userProfileData[comm.fullname];
        });
    //}
    //else{
    //    $.each( response, function( index, comm ){
    //        comm.profile_picture_url=response.userProfileData[comm.fullname];
    //    });
    //}
    

}

function customProcessing(commentsArray, pageID) {
    var tempString = pageID.split('_');
    var woid = tempString[2];
    // $(".commenting-field").find('#expandAndCollapseComments').remove();
    window.parent.$('button#flagStateButton' + woid).removeClass('btn-danger').addClass('btn-info-outline');
    window.parent.$('button#mailStateButton' + woid).removeClass('btn-success').addClass('btn-info-outline');
    flagButtonBoolean = 0;
    mailButtonBoolean = 0;
    window.parent.mailButtonBoolean = 0;
    window.parent.flagButtonBoolean = 0;
    //var elems = document.getElementsByClassName("data-container")

    //for (x = 0; x < elems.length; x++) {
    //    if (elems[x].hasClass('collapse')) {
    //        elems[x].removeClass('collapse').addClass('in').addClass('customCollapse')
    //    }
    //}


    if (!booleanIsFirstRender) {
      //  $(".data-container").removeClass();
     //   $(".data-container").addClass("collapse").addClass("customCollapse" + woid);
      //  $(" .navigation-wrapper").addClass("collapse").addClass("customCollapse" + woid);
        booleanIsFirstRender = false;
    } else {


          var elems = document.getElementsByClassName("data-container")

        for (x = 0; x < elems.length; x++) {
           // console.log(elems[x].classList.length)
            if (elems[x].classList.length < 3) {

                elems[x].classList.add("in")
                elems[x].classList.add("customCollapse" + woid)

               // elems[x].addClass("in").addClass("customCollapse" + woid);
            $(".navigation-wrapper").addClass("in").addClass("customCollapse" + woid);
        }
        }       

        var telems = document.getElementsByClassName("commenting-field");

        for (x = 0; x < telems.length; x++) {
           
            if (telems[x].children[2] == undefined) {
                var html = '<button data-toggle="collapse" data-target=".customCollapse' + woid + '" class="fa fa-angle-up" style="float:right;" type="button" id="expandAndCollapseComments"></button>'
                telems[x].insertAdjacentHTML('beforeend', html);
            }
        }



     //   $(".commenting-field").append($('<button data-toggle="collapse" data-target=".customCollapse' + woid+ '" class="fa fa-angle-up" style="float:right;" type="button" id="expandAndCollapseComments"></button>'));

        $.each(commentsArray, function (index, comm) {
            formatComment(comm);
        });
    } 
}

function formatComment(comm,){
    if(comm.type=='AUDIT'){
        $('[data-id='+comm.id).addClass("commentAudit");
        
    }
    if (comm.actorType == "Delivery Responsible" || comm.actorType == "ASP-DR" || comm.actorType == "Authorized Service Professional Delivery Responsible") {
        $('[data-id='+comm.id).addClass("commentDR");
        
    }
    if(comm.actorType=="Fulfillment Manager"){
        $('[data-id='+comm.id).addClass("commentFM");
        
    }
    if(comm.actorType=="Resource Planning Manager"){
        $('[data-id='+comm.id).addClass("commentRPM");
        
    }
    if(comm.actorType=="Project Manager"){
        $('[data-id='+comm.id).addClass("commentPM");
        
    }

    if(comm.importance==1){
        window.parent.$('[data-id='+comm.id).find('.wrapper').append($('<span class="fa fa-flag commentAttributes"/>'));
    }

    if (comm.notificationFlag == 1) {
        // $('#comment-list').find('div.wrapper').append($('<span class="fa fa-envelope commentAttributes"/>'));
        
        window.parent.$('[data-id=' + comm.id).find('.wrapper').append($('<span class="fa fa-envelope commentAttributes"/>'));
    }
}
function clearShiftModal() {
    hideErrorMsg('StartLessThanEnd');
    hideErrorMsg('Start_Time-Required');
    hideErrorMsg('End_Time-Required');
    hideErrorMsg('timeZone-Required');
    hideErrorMsg('timeWeek-Required');
    $('#shiftTitle').text("");
    $('#shiftStartTime').val("00:00");
    $('#shiftEndTime').val("00:00");
    $("#time_zone").find('option').not(':first').remove();
    $("#datepickerShift").val("");
    $("#weekNumberSelected").val("");
    $('#startDateSelected').val("");
    $('#endDateSelected').val("");
}
function getTimeZones() {
    clearShiftModal();

    $.isf.ajax({
        url: service_java_URL + "activityMaster/getTimeZones",
        context: this,
        crossdomain: true,
        dataType: 'json',
        contentType: 'application/json',
        type: 'GET',
        xhrFields: {
            withCredentials: false
        },
        success: function (data) {
            var currentDate = new Date();
            $("#time_zone").empty();
            $.each(data, function (i, d) {
                $("#time_zone").append('<option value="' + d.timeZone.trim() + '">' + '(UTC ' + moment(currentDate).tz(d.timeZone.trim()).format('Z') + ') ' + d.utcOffset + " - " + d.timeZone + '</option>');
            });

            var userTimeZone = localStorage.getItem("UserTimeZone");
            if (userTimeZone !== null)
                $('#time_zone').val(userTimeZone);
            else
                $('#time_zone').val("Asia/Kolkata");
            $("#time_zone").trigger('change');
        },
        error: function (xhr, status, statusText) {
            console.log('An error occurred');


        }
    });
}


//Get Context Data
function getStepsWO(subActivityFlowChartDefID, woid, experienced = '21') {
    $('#selectStepsWO'+woid).empty();
    
    context ="WorkOrder:"+ woid;
    $.isf.ajax({
        type: "GET",
        url: service_java_URL + "woExecution/getWOWorkFlow/" + woid + "/" + experienced,
        success: function (data) {
            //2VN
            data = data.responseData;
            $('#selectStepsWO' + woid).append('<option value="WorkOrder ' + woid + '">WorkOrder ' + woid + '</option>');
            if ('Success' in data) {
                $.each(data.Success.workFlowSteps , function (i, d) {
                    $('#selectStepsWO' + woid).append('<option value="' + data.Success.workFlowSteps[i].stepID + '">' + data.Success.workFlowSteps[i].stepName + '</option>');
                    $('#selectStepsWO' + woid).select2();
                })
            }
            else {
                console.log(data)
            }

            context = $("#selectStepsWO" + woid).val
        },
        error: function (xhr, status, statusText) {
            console.log("error");

        },
        complete: function (xhr, statusText) {
            
            window.pwIsf.removeLayer();
        }
    });
    }
function refreshCommentsSection(that, woid, uniquePageID, subActivityFlowChartDefID) {
    $('#moreComments' + woid).removeClass('disabledbutton');
    $('#prevComments' + woid).addClass('disabledbutton');
    $('#labelAllComments' + woid).css("display", "none");
    $("#searchInComments"+woid)[0].value='';
    
    getStepsWO(subActivityFlowChartDefID, woid, experienced = '21');
    
    getCommentsSection(uniquePageID, '', '');
    $('button#flagStateButton'+woid).removeClass('btn-danger').addClass('btn-info-outline');
    $('button#mailStateButton'+woid).removeClass('btn-success').addClass('btn-info-outline');
    flagButtonBoolean = 0;
    mailButtonBoolean = 0;
    $('button#showHideAuditData'+woid).removeClass('btn-info-outline').addClass('btn-info');
    $('button#showHideAuditData'+woid).find('i').removeClass('fa-eye-slash').addClass('fa-eye');
    $('button#showHideFlaggedData'+woid).removeClass('btn-danger').addClass('btn-info');
}

$(document).on('click', '#expandAndCollapseComments', function (e) {
    var $this = $(this);
    if ($this.hasClass('fa-angle-up')) {
        $this.removeClass('fa fa-angle-up').addClass('fa fa-angle-down');

        vars['flog' + $this.parent().parent().attr('id').split("_")[2]] = true;

    } else {

        if ($this.parent().parent().children()[3] != undefined) {
            var noDataDivExist = $this.parent().parent().children()[3].getElementsByClassName("no-comments").length
        }

        

        if (!cDataExists || noDataDivExist ==1) {
            pwIsf.addLayer({ text: "Please wait ..." });
            // $(".spinner").show();
            var rwoID = $this.parent().parent().attr('id').split("_")[2];

            $("#refreshAudit"+rwoID).click();
           //cDataExists = true;
          
                
              // $("#expandAndCollapseComments").click()
            //   $(".spinner").hide();
            //$(".data-container").addClass("in").addClass("customCollapse");
            //$(".navigation-wrapper").addClass("in").addClass("customCollapse");

                pwIsf.removeLayer();
          
        }
        $this.removeClass('fa fa-angle-down').addClass('fa fa-angle-up');
     //   flog1 = false;
          //$(".data-container").addClass("in").addClass("customCollapse");
          //$(" .navigation-wrapper").addClass("in").addClass("customCollapse");
    }
});

//On Change of Context Dropdown
function onChangeContext(selectedElement) {
    context = selectedElement.value;
}
//Comments End
var pwIsf = {};


pwIsf.alert = function (options) {

    var msgType = ['info','success','warning','error'];

    var defaults = {
        msg: 'ISF',
        type: 'info',
        autoClose:0
    };

    var actual = $.extend({}, defaults, options || {});

    var msgStr = '<span style="padding-left:10px;font-size:13px;">' + actual.msg + '</span>';
    var blackOverlay = $('<div id="isfBlackOverlayAlert" style="background-color:black;opacity:.4;top:0;left:0;width:100%;position:fixed;z-index:10002;height:100%;"></div>').prop('title','Click to close this alert.').on('click', function () { pwIsf.closeAlert() });

    var centerTextPlace = $('<div id="isfOverlayTextPlaceAlert" style="background-color:rgb(45, 44, 44);z-index:10003;position:fixed;padding:20px 20px 10px 20px;color:white;border-radius:7px;text-align: center;"></div>');

    var infoIcon = $('<i class="fa fa-info-circle" style="font-size:22px;color:#9cb3e8;"></i>');
    var successIcon = $('<i class="fa fa-thumbs-up" style="font-size:22px;color:#09de09;"></i>');
    var warningIcon = $('<i class="fa fa-exclamation-triangle" style="font-size:22px;color:#e6c414;"></i>');
    var errorIcon = $('<i class="fa fa-times-circle" style="font-size:22px;color:#ff3131;"></i>');
    var alertIcon;

    if (actual.type == 'info')
        alertIcon = infoIcon;
    if (actual.type == 'success')
        alertIcon = successIcon;
    if (actual.type == 'warning')
        alertIcon = warningIcon;
    if (actual.type == 'error')
        alertIcon = errorIcon;
    
    if (!alertIcon)
        alertIcon = infoIcon;
    

    var alertBtn = $('<div><button type="button" style="margin-top: 15px;" class="btn btn-primary">OK</button></div>').on('click', function () { pwIsf.closeAlert() });

    
    centerTextPlace.append(alertIcon, msgStr);
    centerTextPlace.append(alertBtn);
    $(document.body).append(blackOverlay, centerTextPlace);
    
    centerTextPlace.css({
        left: ($(window).width()) / 2 - ((centerTextPlace.width()) / 2),
        top: ($(window).height()) / 2 - (centerTextPlace.height() / 2)
    })
    
    if (actual.autoClose) {
        setTimeout(
          function () {
              pwIsf.closeAlert();
          }, actual.autoClose * 1000);
    }

}

pwIsf.closeAlert = function () {
    var bOverlay = $('#isfBlackOverlayAlert');
    var txtPlace = $('#isfOverlayTextPlaceAlert');
    if (bOverlay) { bOverlay.remove(); }
    if (txtPlace) { txtPlace.remove(); }
}

pwIsf.addLayer = function (options) {

    var defaults = {};
        
    var actual = $.extend({}, defaults, options || {});

    var blackOverlay = $('<div id="isfBlackOverlay" style="background-color:black;opacity:.4;top:0;left:0;width:100%;height:100%;position:fixed;z-index:10001;"></div>');

    var centerTextPlace = $('<div id="isfOverlayTextPlace" style="background-color:#333;z-index:10002;position:fixed;padding:20px;color:white;border-radius:7px;text-align: center;"></div>');
    
    //blackOverlay.css({
    //    height:$(document).height()
    //});

    centerTextPlace.append(pleaseWaitISF(actual));
    $(document.body).append(blackOverlay, centerTextPlace);
    //$(window.parent.document.body).append(blackOverlay, centerTextPlace);

    centerTextPlace.css({
        left: ($(window).width()) / 2 - (centerTextPlace.width() / 2),
        top: ($(window).height()) / 2 - (centerTextPlace.height() / 2)
    })
         

}

pwIsf.removeLayer = function () {

    var bOverlay =$('#isfBlackOverlay');
    var txtPlace =$('#isfOverlayTextPlace');
    if (bOverlay) { bOverlay.remove(); }
    if (txtPlace) { txtPlace.remove(); }

    var bOverlayIframe = $('#isfBlackOverlay', window.parent.document);
    var txtPlaceIframe = $('#isfOverlayTextPlace', window.parent.document);
    if (bOverlayIframe) { bOverlayIframe.remove(); }
    if (txtPlaceIframe) { txtPlaceIframe.remove(); }

}


    pwIsf.confirm = function (params) {

        if ($('#confirmOverlay').length) {
            // A confirm is already shown on the page:
            return false;
        }

        var buttonHTML = '';
        $.each(params.buttons, function (name, obj) {

            // Generating the markup for the buttons:
            if (obj['class']) {
                var className = obj['class'];
            } else {
                if (name.toLowerCase() == 'yes') {
                    var className ='btn btn-success';
                } else {
                    var className = 'btn btn-default';
                }
            }
            

            buttonHTML += '<a href="#" class="button ' + className + '">' + name + '<span></span></a>';

            if (!obj.action) {
                obj.action = function () { };
            }
        });

        var blackOverlay = 'style="background-color:black;opacity:.4;top:0;left:0;width:100%;height:100%;position:fixed;z-index:10001;"';

        var centerTextPlace = 'style="background-color:#333;z-index:10002;position:fixed;padding:20px;color:white;border-radius:7px;text-align: center;left:50%;top:50%;"';

        var confirmHtml = [
            '<div id="confirmOverlay" ' + blackOverlay + '></div>',
            '<div id="confirmBox" '+ centerTextPlace +'>',
            '<h4 style="border-bottom:1px solid;text-align:left;">', params.title, '</h4>',
            '<p style="font-size:13px;">', params.msg, '</p>',
            '<div id="confirmButtons">',
            buttonHTML,
            '</div></div>'
        ].join('');

        $(confirmHtml).hide().appendTo('body').show();

        $('#confirmBox').css({
            left: ($(window).width()) / 2 - ($('#confirmBox').width() / 2),
            top: ($(window).height()) / 2 - ($('#confirmBox').height() / 2)
        })

        var buttons = $('#confirmBox .button'),
            i = 0;

        $.each(params.buttons, function (name, obj) {
            buttons.eq(i++).click(function () {

                // Calling the action attribute when a
                // click occurs, and hiding the confirm.

                let returnType = obj.action();
                if (returnType!=false) { // If return type == false it will not close confirm box
                    pwIsf.confirm.hide();
                }
                return false;
            });
        });
    }

    pwIsf.confirm.hide = function () {
        
        var bOverlay = $('#confirmOverlay');
        var txtPlace = $('#confirmBox');
        if (bOverlay) { bOverlay.remove(); }
        if (txtPlace) { txtPlace.remove(); }

    }

//Add layer relative to a DOM element
pwIsf.addRelativeLayer = function (el, options) {

    let blackOverlay = $('<div id="isfBlackOverlayRelative" style="background-color:black;opacity:.4;top:0;left:0;width:100%;height:100%;position:absolute;z-index:1001;"></div>');

    let textPlace = $('<div id="isfOverlayTextPlaceRelative" style="background-color:#333;z-index:1002;position:absolute;padding:20px;color:white;border-radius:7px;text-align: center;"></div>');

    let sizeRatio;

    textPlace.append(pleaseWaitISF(options));
    $(el).append(blackOverlay, textPlace);

    switch (options.position) {
        case 'top': sizeRatio = 1 / 4;
            break;
        case 'bottom': sizeRatio = 3 / 4;
            break;
        case 'center': sizeRatio = 1 / 2;
            break;
        default: sizeRatio = 1 / 2;
    }

    textPlace.css({
        left: ($(el).outerWidth()) / 2 - (textPlace.outerWidth() / 2),
        top: ($(el).outerHeight()) * sizeRatio - (textPlace.outerHeight() / 2)
    })
}

//Remove layer relative to a DOM element
pwIsf.removeRelativeLayer = function (el) {
    var bOverlay = $(el).children('#isfBlackOverlayRelative');
    var txtPlace = $(el).children('#isfOverlayTextPlaceRelative');
    if (bOverlay) { bOverlay.remove(); }
    if (txtPlace) { txtPlace.remove(); }
}

//------------------changes for new comments------------------//


jQuery.fn.extend({
    commentIsf: function(config) {
      
        config.divId=this.selector;
        initComments(config);
      
    },
    
  });

var commentsContainerGl;

function initComments(config){
    if($(config.divId + config.pageId).val() != undefined)
        var commentsContainer = $('#comments-container' + config.pageId);
    else{
        var commentsContainer = window.parent.$('#comments-container' + config.pageId);
    }
    commentsContainerGl=commentsContainer;
    var n = config.pageId.lastIndexOf('_');
    var auditID = config.pageId.substring(n + 1);
    var groupID = config.pageId.substring(0, n);
    var notificationFlag=0;
    var importance=0;

    var hideAuditData=0;
    var showFlaggedData=0;
    var searchString="";
    var allCommentsData={};
    var filteredCommentsData={};
    var fetchDataFromServer=true;

    var getCommentsFromServer=function (success, error) {
        var serviceUrl = service_java_URL + 'auditing/getAuditData?pageId=' + config.pageId + '&searchString=' + searchString;
        if(ApiProxy == true){
            serviceUrl = service_java_URL + 'auditing/getAuditData?' + encodeURIComponent("pageId=" + config.pageId + "&searchString=" + searchString);
        }
        $.isf.ajax({ 
            type: 'get',
            url: serviceUrl,
            success: function (data) {
                if (data.ErrorFlag == false) {
                    enrichComments(data);
                    allCommentsData = data.commentsData;
                    success(data.commentsData);
                    customProcessingComments(data.commentsData, config.pageId);
                }
                else {
                    pwIsf.alert({ msg: data.Error, type: 'error' });
                }
            },
            error: error
            
        });
    }

    var getCommentsFromArray=function (success, error,commentsArray) {
        
        enrichComments(commentsArray);
        success(commentsArray);
        customProcessingComments(commentsArray,config.pageId);
        
    };

    var commentsInitParams={
        profilePictureURL: (UserImageUriSession == 'null' || UserImageUriSession == null || UserImageUriSession == '') ? null : UserImageUriSession,
        roundProfilePictures: true,
        readOnly: config.readOnly,
        defaultNavigationSortKey: Glob_sortOrder,
        getComments:function (success, error){
            if(fetchDataFromServer){
                getCommentsFromServer(success, error);
            }else{
                getCommentsFromArray(success, error,filteredCommentsData);
            }
        },
        //Add New Comments/POST Comments
        //A callback function that is used to create a new comment to the server. 
        //The first parameter of the callback is commentJSON that contains the data of the new comment. 
        //The callback provides both success and error callbacks which should be called based on the result from the server. 
        //The success callback takes the created comment as a parameter.
        postComment: function (commentsJSON, success, error) {
            var commentCategoryNew2 = getCommentCategory();
            if(config.contextEnabled){
                context = window.parent.$('#selectStepsWO' + woid).val();
            }
           
            commentsJSON.content = commentsJSON.content.replace(/`/g, ' ');
            commentsJSON.auditPageId = auditID;
            commentsJSON.auditGroupCategory = groupID;            
            commentsJSON.fullname = signumGlobal;
            commentsJSON.context = context;
            commentsJSON.notificationFlag = notificationFlag;
            commentsJSON.importance = importance;
            commentsJSON.actorType = JSON.parse(ActiveProfileSession).role;
            commentsJSON.commentCategory = commentCategoryNew2;
            let isAuditColumnlengthExceeded = false;

            if (commentsJSON.context.length > 1000 || commentsJSON.content.length > 1000) {
                isAuditColumnlengthExceeded = true;
            }
            if (!isAuditColumnlengthExceeded) {
                $.isf.ajax({
                    type: 'post',
                    contentType: "application/json; charset=utf-8",
                    url: service_java_URL + "auditing/addComment",
                    data: JSON.stringify(commentsJSON),
                    success: function (data) {
                        let comment = data.responseData;

                        if (data.isValidationFailed == true) {
                            pwIsf.alert({ msg: data.formErrors[0], type: 'warning' });
                        }
                        else {
                            //  pwIsf.alert({ msg: data.formMessages[0], type: 'success', autoClose: 3 });
                            NotifyMobileDevice(commentsJSON, comment);
                            comment.profile_picture_url = (UserImageUriSession == 'null' || UserImageUriSession == null || UserImageUriSession == '') ? null : UserImageUriSession,
                                success(comment);
                            formatComment(comment);
                            allCommentsData.push(comment);
                            importance = 0;
                            notificationFlag = 0;
                        }

                    },
                    error: error
                });
            }

            else {
                pwIsf.alert({ msg: "Max comment character length(1000) exceeded", type: "error", autoClose: 4 });
            }



        },
        refresh: function () {
            commentsContainer.addClass('rendered');
        },
        timeFormatter: function(time) {
            return ConvertDateTime_tz(new Date(time)).toLocaleString();
        }
    };

    function filterComments(){
        fetchDataFromServer=false;   
        filteredCommentsData = allCommentsData.filter(
            function (element){
                let isVisible=true;
                if(hideAuditData==1 && element.type != "USER_COMMENT"){
                    isVisible=false;
                }else{
                    isVisible=true;
                }
                if(showFlaggedData==1 && element.importance != showFlaggedData){
                    isVisible=false;
                }
            
                if(!(""==searchString)){
                    isVisible=false;
                   if(element.content.toUpperCase().indexOf(searchString.toUpperCase())!=-1
                   || (element.fullname.toUpperCase().indexOf(searchString.toUpperCase()))!=-1
                   || (element.fieldName!=null && (element.fieldName.toUpperCase().indexOf(searchString.toUpperCase()))!=-1)
                   ){
                        isVisible=true;
                   }

                }




                return isVisible;
            }
        );
        commentsInitParams.defaultNavigationSortKey = Glob_sortOrder;
        commentsContainer.comments(commentsInitParams);   
        
    }

//A callback function that is used to fetch the comments array from the server
//The success callback takes the comment array as a parameter.
commentsContainer.comments(commentsInitParams);

function resetCommentsView(){
    notificationFlag=0;
    showFlaggedData=0;
    $('#mailStateButton_'+config.pageId).removeClass('selected').addClass('deselected');
    $('#flagSelectButton_'+config.pageId).removeClass('selected').addClass('deselected');
}

function customProcessingComments(commentsArray,pageID){
    $(".commenting-field").find('#expandAndCollapseComments').remove();
    if(config.isCollapsed){
        $(".data-container").addClass("collapse").addClass("customCollapse");
        $(" .navigation-wrapper").addClass("collapse").addClass("customCollapse");
            config.isCollapsed=false;
    }else{
        $(".data-container").addClass("in").addClass("customCollapse");
        $(" .navigation-wrapper").addClass("in").addClass("customCollapse");
    }
    
    $(".commenting-field").append($('<span data-toggle="collapse" title="Show/Hide Comments Section" data-target=".customCollapse" class="fa fa-angle-up pullBtnComments" style="float:right;" type="button" id="expandAndCollapseComments"></span>'));
    $.each( commentsArray, function( index, comm ){
        formatComment(comm);
    });


    var mailBtn=$('<span/>', {
        id: 'mailStateButton_'+config.pageId,
        class:'fa fa-envelope enabled deselected',
        click: function () { 
            that = this;
            if ($(that).hasClass('selected')) {
                $(that).removeClass('selected').addClass('deselected');
                notificationFlag = 0;
            } else if ($(that).hasClass('deselected')) {
                $(that).removeClass('deselected').addClass('selected');
                notificationFlag = 1;
            }
         }
    });
    
    var flagBtn=$('<span/>', {
        id: 'flagSelectButton_'+config.pageId,
        class:'fa fa-flag enabled deselected',
        click: function () { 
            that = this;
            if ($(that).hasClass('selected')) {
                $(that).removeClass('selected').addClass('deselected');
                importance = 0;
            } else if ($(that).hasClass('deselected')) {
                $(that).removeClass('deselected').addClass('selected');
                importance = 1;
            }
         }
    });

    var refreshBtn=$('<span/>', {
        id: 'refreshCommentsButton_'+config.pageId,
        class:'fa fa-refresh commentsTools',
        title:"Refresh",
        click: function () { 
            searchString="";
            fetchDataFromServer=true;
            importance=0;
            notificationFlag=0;
            hideAuditData=0;
            showFlaggedData=0;
            commentsContainer.comments(commentsInitParams);
         }
    });  

    var showFlaggedBtn=$('<span/>', {
        id: 'showFlaggedButton_'+config.pageId,
        class:'fa fa-flag commentsTools '+((showFlaggedData==1)?'selected':'deselected'),
        title:"Show Flagged Comments",
        click: function () {

            that = this;
            Glob_sortOrder = $($(that.parentNode).find('li.active')[0]).attr('data-sort-key');
            if (showFlaggedData) {
                $(that).removeClass('selected').addClass('deselected');
                showFlaggedData = 0;
            } else if (!showFlaggedData) {
                $(that).removeClass('deselected').addClass('selected');
                showFlaggedData = 1;
            }
            filterComments();
         }
    });

    var hideAuditBtn=$('<span/>', {
        id: 'hideAuditButton_'+config.pageId,
        class:'fa fa-eye commentsTools '+((hideAuditData==1)?'selected':'deselected'),
        title:"Hide Audit Comments",
        click: function () {

            that = this;
            Glob_sortOrder = $($(that.parentNode).find('li.active')[0]).attr('data-sort-key');
            if (hideAuditData) {
                $(that).removeClass('selected').addClass('deselected');
                hideAuditData = 0;
            } else if (!hideAuditData) {
                $(that).removeClass('deselected').addClass('selected');
                hideAuditData = 1;
            }
            filterComments();
            return true;
         }
    });
    
    var searchBox=$('<input/>', {
        id: 'searchBox_'+config.pageId,
        class:'searchBox searchText ',
        val:searchString,
        placeholder:"Press enter to search...",
        keydown: function (e) { 
            
                var keycode = (e.keyCode ? e.keyCode : e.which);
            if (keycode == '13') {
                Glob_sortOrder = $($(this.parentNode).find('li.active')[0]).attr('data-sort-key');
                    searchString=$(this).val();
                    fetchDataFromServer=false;
                    filterComments();
                
             }
         }
    });

    if(!config.isMailDisabled){
        $('.control-row').append(mailBtn);
    }

    $('.control-row').append(flagBtn);
    $('.navigation-wrapper').append(searchBox);
    
    $('.navigation-wrapper').append(showFlaggedBtn);
    $('.navigation-wrapper').append(hideAuditBtn);
    
    $('.navigation-wrapper').append(refreshBtn);

    
}


}

function getAutoSenseObject(woID,flowChartdefID,stepID,source,signum,overrideAction,action,taskID,strRule,stopRule) {
    let autoSenseInputData = new Object();
    autoSenseInputData.WoId = woID;
    autoSenseInputData.flowchartDefID = flowChartdefID;
    autoSenseInputData.stepID = stepID;
    autoSenseInputData.source = source;
    autoSenseInputData.signumID = signum;
    autoSenseInputData.overrideAction = overrideAction;
    autoSenseInputData.action = action;
    autoSenseInputData.taskID = taskID;
    autoSenseInputData.startRule = strRule;
    autoSenseInputData.stopRule = stopRule;
    return autoSenseInputData;
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


function getActiveLoginMessage() {

    if (sessionStorage.getItem("msgFlag") != 'true') {
        sessionStorage.setItem("msgFlag", false);
    }
    if (sessionStorage.getItem("msgFlag") == 'false') {
        $.isf.ajax({
            url: service_java_URL + "accessManagement/getEnabledMessage",
            context: this,
            crossdomain: true,
            processData: true,
            contentType: 'application/json',
            type: 'GET',

            success: function (data) {
                if (data.isValidationFailed == false) {
                    if (data.responseData != null && data.responseData.length > 0) {
                        sessionStorage.setItem("colorHeader", data.responseData[0].color);
                        $("#loginMsgdiv").css({
                            display: "block", color: '#' + data.responseData[0].color                           
                        });
                        $("#mydivheader").css({
                            display: "block", color: '#' + data.responseData[0].color, height: 'auto'
                        });
                        $('#mydivheader').text('');
                        $('#mydivheader').append(escapeHtml(data.responseData[0].message));
                        sessionStorage.setItem("msgHeader", data.responseData[0].message);
          
                        
                    } else {
                        $("#loginMsgdiv").css({
                            display: "none"
                        });
                        $("#mydivheader").css({
                            display: "none"
                        });
                        sessionStorage.setItem("msgHeader",'');
                    }
                    sessionStorage.setItem("msgFlag", true);
                }
               
            },
            error: function (xhr, status, statusText) {
                pwIsf.removeLayer();
                pwIsf.alert({ msg: 'error', type: "error" });
                $("#loginMsgdiv").css({
                    display: "none"
                });
                $("#mydivheader").css({
                    display: "none"
                });

            }
        });
    }

    else {
        if (escapeHtml(sessionStorage.getItem("msgHeader"))) {
            $("#loginMsgdiv").css({
                display: "block", color: '#' + sessionStorage.getItem("colorHeader")
            });
            $("#mydivheader").css({
                display: "block", color: '#' + sessionStorage.getItem("colorHeader"), height: '40px'
            });
            $('#mydivheader').text('');
            $('#mydivheader').append(escapeHtml(sessionStorage.getItem("msgHeader")));


            $('div #arHead').removeClass('arrow-down').addClass('arrow-up');
        }
        else {
            $("#mydivheader").css({
                display: "none"
            });
        }


    }
}
function adhocOverlap() {

        $("#loginMsgdiv").css({
            width: "560px"
        });
}

function adhocoverlapGone() {
    if ($("#adhocBox").is(":visible")) {
        $("#loginMsgdiv").css({
            width: "375px",
           
        });
    }
    else {
        $("#loginMsgdiv").css({
            width: "560px"
        });
    }
}

function closeBanner() {
    $("#loginMsgdiv").css({
        display: "none"
    });
    sessionStorage.setItem("msgHeader", '');
    sessionStorage.setItem("msgFlag", true);
}

function dragElement(elmnt)
{
    if (elmnt != null && elmnt != undefined) {
        var pos1 = 0, pos2 = 0, pos3 = 0, pos4 = 0;
        if (document.getElementById(elmnt.id + "header")) {
            /* if present, the header is where you move the DIV from:*/
            document.getElementById(elmnt.id + "header").onmousedown = dragMouseDown;
        } else {
            /* otherwise, move the DIV from anywhere inside the DIV:*/
            elmnt.onmousedown = dragMouseDown;
        }

        function dragMouseDown(e) {
            e = e || window.event;
            e.preventDefault();
            // get the mouse cursor position at startup:
            pos3 = e.clientX;
            pos4 = e.clientY;
            document.onmouseup = closeDragElement;
            // call a function whenever the cursor moves:
            document.onmousemove = elementDrag;
        }

        function elementDrag(e) {
            e = e || window.event;
            e.preventDefault();
            // calculate the new cursor position:
            pos1 = pos3 - e.clientX;
            pos2 = pos4 - e.clientY;
            pos3 = e.clientX;
            pos4 = e.clientY;
            // set the element's new position:
            elmnt.style.top = (elmnt.offsetTop - pos2) + "px";
            if ((elmnt.offsetTop - pos2) <= 0 || (elmnt.offsetTop - pos2) > 600) {
                elmnt.style.top = '0px';
            }
            elmnt.style.left = (elmnt.offsetLeft - pos1) + "px";
            if ((elmnt.offsetLeft - pos1) <= -500 || (elmnt.offsetLeft - pos1) > 1220) {
                elmnt.style.left = '253px';
            }
           
        }

        function closeDragElement() {
            /* stop moving when mouse button is released:*/
            document.onmouseup = null;
            document.onmousemove = null;
        }
	}
		
}
function getProficiencyLevel(wOStatus, profName) {
    let proficiencyLevel = 1;
    if (wOStatus.toLowerCase() == 'assigned' || wOStatus.toLowerCase() == 'planned' || wOStatus.toLowerCase() == 'reopened') {
        proficiencyLevel = 1;
    } else {
        if (profName != null) {
            if (profName.toLowerCase() == 'assessed')
                proficiencyLevel = 1;
            else
                proficiencyLevel = 2;
		}
       
    }
    return proficiencyLevel;
}

function NotifyMobileDevice(commentData, addCommentResponse) {
    if (addCommentResponse.srid != null && addCommentResponse.srid != 0) {
        var apipayload = {};

        apipayload.Text = `SR (${addCommentResponse.srid}): ${commentData.content}`;
        apipayload.Title = `${signumGlobal.toUpperCase()} (${UserNameSession})`;
        apipayload.Tags = [addCommentResponse.srCreator];
        apipayload.Silent = false;

        var action = {};
        action.ReferenceID = addCommentResponse.srid;
        action.SRID = addCommentResponse.srid;
        action.Module = "SR";
        action.BySignum = signumGlobal;
        action.ByName = UserNameSession;
        action.WOID = addCommentResponse.auditPageId;
        apipayload.Action = action;

        dataToPost = JSON.stringify(apipayload);
        $.isf.ajax({
            type: "POST",
            url: UiRootDir + '/Data/MobileNotification',
            data: dataToPost,
            async: false,
            contentType: "application/json; charset=utf-8", // specify the content type
            dataType: 'JSON',
            success: function (response) {
                console.log(`mobile notification sent: ${response}`);
            },
            error: function (xhr, status, statusText) {
                console.log(`error while calling Notifymobiledevice method. Error: ${statusText} Status: ${status}`);
            }
        });
    }

}


function confirmForIsfDesktopVersionUpdate() {
    var sessionData = JSON.parse(ActiveProfileSession);
    let isDesktopUpdateVisible = localStorage.getItem("IsDesktopUpdateDisplay");
    if (sessionData != null && sessionData != undefined && (sessionData.roleID === 5 || sessionData.roleID === 7 || sessionData.roleID === 14 || sessionData.roleID == 15)) {
        if (isDesktopUpdateVisible === undefined || isDesktopUpdateVisible === null || isDesktopUpdateVisible === "") {
            pwIsf.confirm({
                title: `New ISFDesktop Version (${desktopVersionNumber})`, msg: 'Update available please download. <br> <b><u>Note:</u></b> Click on open button at the next popup for upgrade to start.',
                'buttons': {
                    'Now': {
                        'action': function () {
                            window.open('isfalert:test_' + 0, '_self');
                            localStorage.setItem("IsDesktopUpdateDisplay", true);
                        },
                      'class': 'btn btn-success'
                    },
                    'Later': {
                        'action': function () {
                            pwIsf.removeLayer();
                            localStorage.setItem("IsDesktopUpdateDisplay", true);
                        }
                    },
                }
            });
            
        }
    }
}

function removeExtension(filename) {
    var lastDotPosition = filename.lastIndexOf(".");
    if (lastDotPosition === -1) return filename;
    else return filename.substr(0, lastDotPosition);
}
function displayNoteForMacroBots() {
    $('#lblMacroBotsMsg').empty();
    $('#lblMacroBotsMsg').css({ color: 'red' });
    $('#lblMacroBotsMsg').append('<span style="color: red">*</span>').append(MACROBOTMSG);
}
//Get the proficiencyId
function getProficiencyId(woId, signum) {

    let proficiencyIdReturned = false;

    $.isf.ajax({
        url: `${service_java_URL}woExecution/getProficiencyId?wOID=${woId}&&signum=${signum}`,
        type: 'GET',
        crossDomain: true,
        context: this,
        async: false,
        success: function (data) {
            //Empty block
        },
        complete: function (xhr, status) {
            if (status == "success") {
                if (xhr.responseJSON.isValidationFailed) {
                    responseHandler(xhr.responseJSON);
                    proficiencyId = false;
                }
                else {
                    proficiencyId = xhr.responseJSON.responseData;
                }
            }
        },
        error: function (data) {
            //Empty block
        }
    });
    return proficiencyId;
}

function getExtension(filename) {
    return filename.split('.').pop().toLowerCase();
}

function zipFolderType(folderName) {
    let isFolderTypeZip = false;
        const folderExt = getExtension(folderName);
        if (folderExt === C_ZIP_EXT) {
            isFolderTypeZip = true;
        }
    return isFolderTypeZip;
}



function whlFileType(whlFile) {
    let whlFileExt = C_WHL_EXT;
    if (whlFile) {
        whlFileExt = getExtension(whlFile);
    }
    return whlFileExt;
}

function handleFiles(evt, file) {
    JSZip.loadAsync(file)
        .then(function (zip) {
            for (let [filename, file] of Object.entries(zip.files)) {
                const unzipedFileExt = getExtension(filename);
                if (unzipedFileExt === C_MP4 || unzipedFileExt === C_MOV || unzipedFileExt === C_WMV || unzipedFileExt === C_AVI || unzipedFileExt === C_FLV || unzipedFileExt === C_MKV || unzipedFileExt === C_M4A || unzipedFileExt === C_MP3 || unzipedFileExt === C_WAV || unzipedFileExt === C_WMA || unzipedFileExt === C_AAC || unzipedFileExt === C_MPEG || unzipedFileExt === C_MPG ) {
                    pwIsf.alert({ msg: C_MOVIES_MSG, autoClose: 3 });
                    evt.target.value = "";
                }
            }
       })
}

