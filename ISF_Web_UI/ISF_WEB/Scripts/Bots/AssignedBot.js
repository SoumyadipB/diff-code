
$(document).ready(function () {
    displayNoteForMacroBots();
    getFormBuilder();

    $("#approveBotRequestUL a").click(function () {
        $(this).tab('show');
        console.log($(this).prop('innerHTML'));
        if ($(this).prop('innerHTML') == "DTRCA") {
            $('.formBuilderDiv').hide();
        } else {
            $('.formBuilderDiv').show();
        }
    });

    // make unique ID
    $("#approveBotRequestUL > li > a").each(function (index) {
        $(this).on("click", function () {
            PILLNAME = $(this).attr("href").replace("#", "");
            document.getElementsByClassName("inputRequiredCheckboxOwnDev")[index].setAttribute("id", "inputRequiredCheckboxOwnDev-" + PILLNAME);
            document.getElementsByClassName("forLabel")[index].setAttribute("for", "inputRequiredCheckboxOwnDev-" + PILLNAME);
        });
    });

    var botRequestData; var assignRequestID; var versionNo; var auditRequestID;
    getDevRequestDetails(signumGlobal);

    $("#approveModal").on("hidden.bs.modal", function () {
        // put your default event here
        location.reload();
    });

    $("#rejectModal").on("hidden.bs.modal", function () {
        // put your default event here
        location.reload();
    });

    // GET LANGUAGE NAME WHEN CLICK ON TAB
    $('#approveBotRequestUL a[data-toggle="pill"]').on('shown.bs.tab', function (e) {

        let getText = $(e)[0].target.text; // activated tab
        $('#approveRequest_frm_botlanguage').attr('value', jQuery.trim(getText));
        $('#approveRequestJava_frm_botlanguage').attr('value', jQuery.trim(getText));
        $('#approveRequestPython_frm_botLanguage').attr('value', jQuery.trim(getText));
        $('#approveRequestDotNet_frm_botLanguage').attr('value', jQuery.trim(getText));
        $('#approveRequestBotNest_frm_botLanguage').attr('value', jQuery.trim(getText));
    });
    fileValidation();
    //START- REJECT REQUEST FORM SUBMIT
    $('#rejectRequest_frm').on('submit', function (e) {
        e.preventDefault();
        rejectRequest();

    });
    //END- REJECT REQUEST FORM SUBMIT

    //START-APPROVE REQUEST FOR BOT NEST
    $('#approveRequestBotNest_frm').on('submit', function (e) {
        e.preventDefault();
        $('#jsonFromFormBuilderForBotNest_hidden').attr('value', getJsonFormBuilder());
        //return true;
        submitBotforBotNest();
    });
    //END

    //START - APPROVE REQUEST FORM SUBMIT
    $('#approveRequest_frm').on('submit', function (e) {

        e.preventDefault();
        $('#jsonFromFormBuilderForMacro_hidden').attr('value', getJsonFormBuilder());
        //return true;
        submitBot();
    });
    //END - APPROVE REQUEST FORM SUBMIT

    //START - APPROVE REQUEST FOR JAVA
    $('#approveRequestJava_frm').on('submit', function (e) {
        e.preventDefault();
        $('#jsonFromFormBuilderForJava_hidden').attr('value', getJsonFormBuilder());
        //return true;
        submitBotRequestJava();

    });
    //END - APPROVE REQUEST FOR JAVA

    //START - APPROVE REQUEST FOR Python
    $('#approveRequestPython_frm').on('submit', function (e) {
        e.preventDefault();
        $('#jsonFromFormBuilderForPython_hidden').attr('value', getJsonFormBuilder());
        //return true;
        if (whlFieldsValidationWhenFilled('whlFileApprove', 'selectVersionApprove', 'packageNameApprove')) {
            submitBotRequestPython();
        }
    });

    //START - APPROVE REQUEST FOR .NET
    $('#approveRequestDotNet_frm').on('submit', function (e) {
        e.preventDefault();
        let json = getJsonFormBuilder();
        $('#jsonNet_hidden').attr('value', json);
        submitBotRequestDotNet();
    });

  

    $('#dtrcaConfigModal').on('show.bs.modal', function () {
        $(this).find('.modal-content').css({
            width: 'auto', //probably not needed
            height: 'auto', //probably not needed
            'max-width': '50%',
            'margin': 'auto'
        });
    });

    $('#botNestTestModal').on('show.bs.modal', function () {
        $(this).find('.modal-content').css({
            width: 'auto', //probably not needed
            height: 'auto', //probably not needed
            'max-width': '50%',
            'margin': 'auto'
        });
    });

    $('.testNewBotRequest').on('click', function () {

        if ($(this).attr('data-details') != '') {
            pwIsf.addLayer({ text: 'Please wait ...' });

            let jsonObj = JSON.parse($(this).attr('data-details'));
            let { reqId, signum } = jsonObj;

            let ajCall = $.isf.ajax({
                url: service_java_URL + "botStore/createBotTestingRequest/" + reqId + "/" + signum,
                contentType: 'application/json',
                type: 'POST',
                returnAjaxObj: true
            });

            ajCall.done(function (data) {
                if (data.apiSuccess) {
                    pwIsf.alert({ msg: 'Testing will be initiated after providing input to ISF Popup. Please ensure ISF Desktop App is running', type: 'success' });
                    window.open('isfalert:test_' + reqId, '_self');
                }
                else {
                    pwIsf.alert({ msg: '' + data.responseMsg, type: 'warning' });
                }

            });
            ajCall.fail(function () { pwIsf.alert({ msg: 'Error in BOT testing.', type: 'error' }); });
            ajCall.always(function () { pwIsf.removeLayer(); });

        } else {
            console.log('Error : Request id not received.');
        }
    });

    $('.stopTestBtnBotRequest').on('click', function () {
        pwIsf.addLayer({ text: 'Please wait ...' });

        let jsonObj = JSON.parse($(this).attr('data-details'));
        let { reqId, signum } = jsonObj;


        let ajCall = $.isf.ajax({
            url: service_java_URL + "botStore/stopInprogressBot/" + reqId + "/" + signum,
            contentType: 'application/json',
            type: 'POST',
            returnAjaxObj: true
        });

        ajCall.done(function (data) {
            if (data.apiSuccess) {
                pwIsf.alert({ msg: '' + data.responseMsg, type: 'success' });
                                //window.open('isfalert:test_' + requestID, '_self');
            } else {
                pwIsf.alert({ msg: '' + data.responseMsg, type: 'warning' });
            }
        });


        ajCall.fail(function () { pwIsf.alert({ msg: 'Error in BOT testing.', type: 'error' }); });
        ajCall.always(function () { pwIsf.removeLayer(); });

    });

    $('#approveModal').on('show.bs.modal', function () {
        $(this).find('.modal-content').css({
            width: 'auto', //probably not needed
            height: 'auto', //probably not needed
            'max-width': '90%',
            'margin': 'auto'
        });
    });

    $('#rejectModal').on('show.bs.modal', function () {
        $(this).find('.modal-content').css({
            width: 'auto', //probably not needed
            height: 'auto', //probably not needed
            'max-width': '50%',
            'margin': 'auto'
        });
    });

    $('#rejectEdit').on('show.bs.modal', function () {
        $(this).find('.modal-content').css({
            width: 'auto', //probably not needed
            height: 'auto', //probably not needed
            'max-width': '90%',
            'margin': 'auto'
        });
    });
});

//START - A COMMON FUNCTION FOR IMAGE UPLOAD
let imgDataForAjax = {};
let formBuilderObjEdit;

//create hidden flds from json
imgDataForAjax.createHiddenFlds = function (jsonNameIdVal, idPreFix) { 
    let html = '';
    for (let fk in jsonNameIdVal) {
        let nm = jsonNameIdVal[fk]['name'];
        let inputId = (jQuery.trim(jsonNameIdVal[fk]['id']) != '') ? `id="${idPreFix}_${jsonNameIdVal[fk]['id']}"` : '';
        let val = (jQuery.trim(jsonNameIdVal[fk]['value']) != '') ? `value="${jsonNameIdVal[fk]['value']}"` : '';

        html += `<input type="hidden" name="${nm}" ${inputId} ${val} />\n`;
    }
    return html;
};
//imgDataForAjax.createHiddenFlds([{name:'aa',id:'aa-id',value:''},{name:'bb',id:'',value:'test{}bbbb'}],'prefix');


// GET ALL FORM DATA IN JSON FORMAT USING {} AND []
imgDataForAjax.getFormDataJson = function (frm) {
    //frm jquery object
    let s = frm.serializeArray();
    let g = {};

    for (let i in s) {
        if ((s[i]['name'].match(/{}/g) || []).length) {
            let splitArr = s[i]['name'].split('{}');
            let add = {};
            add[splitArr['1']] = s[i]['value'];
            if (typeof g[splitArr[0]] === 'undefined') {
                g[splitArr[0]] = add;
            } else {
                g[splitArr[0]][splitArr['1']] = s[i]['value'];
            }

        } else if ((s[i]['name'].match(/\[\]/g) || []).length) {
            let splitArr = s[i]['name'].split('[]');
            if (typeof g[splitArr[0]] === 'undefined') {
                g[splitArr[0]] = [];
            }
            let add = {};
            add[splitArr['1']] = s[i]['value'];
            g[splitArr[0]].push(add);
        } else {
            g[s[i]['name']] = s[i]['value'];
        }
    }
    return g;
};
//imgDataForAjax.getFormDataJson($('#newBotMacroFrm'));

imgDataForAjax.getData = function (options) {
    let imgflds = options.setImgFldsJosn;
    let formId = options.formId;
    let formObj = $('#' + formId);
    let form = document.getElementById(formId);
    let data = new FormData(form);

    for (let i in imgflds) {
        for (let f in formObj[0]) {
            if (formObj[0][f]) {
                if (formObj[0][f].name == imgflds[i]['frmElmntAs']) {
                    //console.log('files-',formObj[0][f].files[0]);
                    data.append(imgflds[i]['sendAs'], formObj[0][f].files[0]);
                    break;
                }
            }
        }
    }
    return data;
};

imgDataForAjax.sendRequest = function (opt) {

    let url = opt.url;
    let progressId = opt.progressBarId;
    let sendData = new FormData();
    sendData = opt.frmData;

    let reqPromise = $.isf.ajax({
        type: "POST",
        enctype: "multipart/form-data",
        url: url,
        returnAjaxObj: true,
        data: sendData,
        processData: false,
        contentType: false,
        cache: false,
        timeout: 600000,

        // Custom XMLHttpRequest
        xhr: function () {
            var myXhr = $.ajaxSettings.xhr();
            if (myXhr.upload) {
                // For handling the progress of the upload
                myXhr.upload.addEventListener('progress', function (e) {
                    if (e.lengthComputable) {
                        let prog = '#' + progressId;
                        if ($(prog)) {
                            $(prog).attr({
                                value: e.loaded,
                                max: e.total,
                            });
                        }
                    }
                }, false);
            }
            return myXhr;
        },
        retrunAjaxObj:true
    });

    reqPromise.done(opt.onDone);
    reqPromise.fail(function () { alert("Image upload error -" + url); });
    reqPromise.always(function () { pwIsf.removeLayer(); });

};
//END - A COMMON FUNCTION FOR IMAGE UPLOAD


//FormBuider Start
let formBuilderObj;

function getFormBuilder() {
    formBuilderObj = configureFormBuilder('formBuilderWillComeHere');
}

function getJsonFormBuilder() {
    let getjson = getJsonFromFormBuilder(formBuilderObj);
    return getjson;
}

function downloadSampleJson() {

    let getjson = getJsonFromFormBuilder(formBuilderObj, 'json');

    let actualJson = common_getActualJsonFromFormBuilderJson(getjson);

    let data = "text/json;charset=utf-8," + encodeURIComponent(JSON.stringify(actualJson));

    let a = document.getElementById('aTagDownloadSampleJson');
    a.href = 'data:' + data;
    a.download = 'data.txt';
    a.innerHTML = '';

    document.getElementById('addDownloadLink').appendChild(a);

    $('#aTagDownloadSampleJson')[0].click();
}
//FormBuilder End

function getDevRequestDetails(signumGlobal) {
    //var signum = "essrmma";
    //var signum = signumGlobal;
    if ($.fn.dataTable.isDataTable('#tbReqBody')) {
        oTable.destroy();
        $("#tbReqBody").empty();
    }
    $.isf.ajax({
        url: service_java_URL + "botStore/getAssignedRequestListForDev/" + signumGlobal,
        context: this,
        crossdomain: true,
        processData: true,
        contentType: 'application/json',
        type: 'GET',
        xhrFields: {
            withCredentials: false
        },

        success: function (data) {
            $("#tbReqBody").append($('<tfoot><tr><th></th><th>Request Id</th><th>Project</th><th>SubActivity</th><th>WorkFlow</th><th>SPOC</th><th>Execution(Weekly)</th><th>Current Status<th>Bot Language</th></tr></tfoot>'));
            oTable = $('#tbReqBody').DataTable({
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
                        "title": "Action", "targets": 'no-sort', "orderable": false, "searchable": false, "data": null,
                        "defaultContent": "",
                        "render": function (data, type, row, meta) {
                            if (data.status == "Assigned" || data.status == "ASSIGNED" || data.status == "assigned") {
                                let setData = JSON.stringify({
                                    rpaRequestId: data.tblRpaRequest.rpaRequestId, createdBy: data.createdBy, assignedTo: data.assignedTo, isActive: data.isActive, status: data.status, botname: data.botname, rpaexecutionTime: data.rpaexecutionTime, parallelWoexecution: data.parallelWoexecution, reuseFactor: data.reuseFactor, lineOfCode: data.lineOfCode, targetExecutionFileName: data.targetExecutionFileName, botlanguage: data.botlanguage, subActID: data.tblRpaRequest.subactivityId, isInputRequired: data.tblRpaRequest.isInputRequired
                                });
                                return '<select class="form-control" data-details=\'' + setData + '\' onchange="showOption(this)"><option value = ""> Select Feasibility</option ><option value="approve">Approve</option><option value="reject">Reject</option></select >';
                            }
                            else if (data.status == "ACCEPTED") {
                                if (data.botlanguage == "BOTNEST" || data.botlanguage == "BOT NEST") {
                                    let setData = JSON.stringify({
                                        rpaRequestId: data.tblRpaRequest.rpaRequestId, assignedTo: data.assignedTo, botname: data.botname, rpaexecutionTime: data.rpaexecutionTime, parallelWoexecution: data.parallelWoexecution, reuseFactor: data.reuseFactor, lineOfCode: data.lineOfCode, targetExecutionFileName: data.targetExecutionFileName, botlanguage: data.botlanguage, classname: data.moduleClassName, methodname: data.moduleClassMethod, botnestId: data.tblRpaRequest.referenceBotId, isInputRequired: data.tblRpaRequest.isInputRequired
                                    });
                                    return '<div style="display:flex;"><a class="icon-add" title="Test Bot" id="testBtnDev"  onclick="testBotNest(this)" data-details=\'' + setData + '\'><i class="fa fa-text-height"></i></a><div>';
                                }
                                else if (data.botlanguage.toLowerCase() === "penguin") {
                                    let setData = JSON.stringify({
                                        rpaRequestId: data.tblRpaRequest.rpaRequestId, assignedTo: data.assignedTo, botname: data.botname, rpaexecutionTime: data.rpaexecutionTime, parallelWoexecution: data.parallelWoexecution, reuseFactor: data.reuseFactor, lineOfCode: data.lineOfCode, targetExecutionFileName: data.targetExecutionFileName, botlanguage: data.botlanguage, classname: data.moduleClassName, methodname: data.moduleClassMethod, isInputRequired: data.tblRpaRequest.isInputRequired
                                    });
                                    return '<div style="display:flex;"><a class="icon-add testNewBotRequest" title="Test Bot" id="testBtnDev"  onclick="testReqDev(' + data.tblRpaRequest.rpaRequestId + ', \'' + data.assignedTo + '\')"><i class="fa fa-text-height"></i></a><a class="icon-delete stopTestNewBotRequest" title=" Stop Test-Bot" id="stopTestBtnDev"  onclick="stoptestReqDev(' + data.tblRpaRequest.rpaRequestId + ', \'' + data.assignedTo + '\')"><i class="fa fa-text-height"></i></a></div>';
                                }
                                else {
                                    let setData = JSON.stringify({
                                        rpaRequestId: data.tblRpaRequest.rpaRequestId, assignedTo: data.assignedTo, botname: data.botname, rpaexecutionTime: data.rpaexecutionTime, parallelWoexecution: data.parallelWoexecution, reuseFactor: data.reuseFactor, lineOfCode: data.lineOfCode, targetExecutionFileName: data.targetExecutionFileName, botlanguage: data.botlanguage, classname: data.moduleClassName, methodname: data.moduleClassMethod, isInputRequired: data.tblRpaRequest.isInputRequired
                                    });
                                    return '<div style="display:flex;"><a class="icon-add testNewBotRequest" title="Test Bot" id="testBtnDev"  onclick="testReqDev(' + data.tblRpaRequest.rpaRequestId + ', \'' + data.assignedTo + '\')"><i class="fa fa-text-height"></i></a><a class="icon-delete stopTestNewBotRequest" title=" Stop Test-Bot" id="stopTestBtnDev"  onclick="stoptestReqDev(' + data.tblRpaRequest.rpaRequestId + ', \'' + data.assignedTo + '\')"><i class="fa fa-text-height"></i></a><a class="icon-edit lsp" title="Edit" onclick="updateBotData(this)" data-details=\'' + setData + '\' data-toggle="modal" href="#rejectEdit"><i class="fa fa-edit"></i></a></div>';
                                }
                            }
                            else if (data.status == "REJECTED") {
                            }
                            else if (data.status == "TESTED" && data.botlanguage == "BOT NEST" || data.botlanguage == "BOTNEST") {
                                let setData = JSON.stringify({
                                    rpaRequestId: data.tblRpaRequest.rpaRequestId, assignedTo: data.assignedTo, botname: data.botname, rpaexecutionTime: data.rpaexecutionTime, parallelWoexecution: data.parallelWoexecution, reuseFactor: data.reuseFactor, lineOfCode: data.lineOfCode, targetExecutionFileName: data.targetExecutionFileName, botlanguage: data.botlanguage, classname: data.moduleClassName, methodname: data.moduleClassMethod, botnestId: data.tblRpaRequest.referenceBotId, isInputRequired: data.tblRpaRequest.isInputRequired
                                });
                                return '<div style="display:flex;"><a class="icon-add" title="Stream Bot" id="streamBotbtn" onclick="streamBot(this)"  data-details=\'' + setData + '\'><i class="fa fa-rss"></i></a><div>';
                            }
                        }
                    },
                    { "title": "Request Id", "data": "tblRpaRequest.rpaRequestId" }, 
                    {
                        "title": "Project", "data": null,
                        "render": function (data, type, row, meta)
                        {
                            return data.tblRpaRequest.tblProjects.projectId+"/"+data.tblRpaRequest.tblProjects.projectName;
                        }

                    },
                      {
                          "title": "Subactivity", "data": null,
                          "render": function (data, type, row, meta)
                          {
                              return data.tblRpaRequest.subactivityId+"/"+data.tblRpaRequest.subactivityName;
                          }

                      },
                     { "title": "WorkFlow", "data": "tblRpaRequest.workFlowName" },
                     {
                         "title": "SPOC","data": null,
                         "defaultContent": "",
                         "render": function (data, type, row, meta)
                         {
                             return '<a href="#" style="color:blue;" onclick="getEmployeeDetailsBySignum(\'' + data.tblRpaRequest.spocsignum + '\')">'+data.tblRpaRequest.spocsignum+'</a>';
                         }

                     },
                    { "title": "Execution Count(Weekly)", "data": "tblRpaRequest.currentExecutioncountWeekly" },
                    {
                        "title": "Current Status", "data": null,
                        "render": function (data, type, row, meta) {
                            return data.status;
                        }
                    },
                    {
                        "title": "Bot Language", "data": null,
                        "render": function (data, type, row, meta) {
                            return data.botlanguage;
                        }
                    }

                ],
                initComplete: function () {

                    $('#tbReqBody tfoot th').each(function (i) {
                        var title = $('#tbReqBody thead th').eq($(this).index()).text();
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
            $('#tbReqBody tfoot').insertAfter($('#tbReqBody thead'));
        },
        error: function (xhr, status, statusText) {
            console.log('An error occurred');
        }
    });


}

function getConfig() {
    //getTechnologies();
    //getTech();
    //getProjectDetails();
    $("#dtrcaConfigModal").modal('show');
}

function showOption(thisObj) {
    let jsonData = $(thisObj).data('details');
    let feasibility = thisObj.value;

   
    $modal1 = $('#approveModal');
    $modal2 = $('#rejectModal');
    if (feasibility === 'approve') {
        $modal1.modal('show');
        var html = ``;
        html += `<button class="inputDiv btn btn-default" type="button" title="Input File" onclick="downloadBotFiles({thisObj:this,botId:${jsonData.rpaRequestId},fileName:'input.zip'});"><i class="fa fa-download" aria-hidden="true"></i >Input File</button><button class="btn btn-default" type="button" title="Output File" onclick="downloadBotFiles({thisObj:this,botId:${jsonData.rpaRequestId},fileName:'output.zip'});"><i class="fa fa-download" aria-hidden="true"></i>Output File</button><button class="btn btn-default" type="button" title="Logic File" onclick="downloadBotFiles({thisObj:this,botId:${jsonData.rpaRequestId},fileName:'logic.zip'});"><i class="fa fa-download" aria-hidden="true"></i>Logic File</button>`;
        $('#downloadButton').html('').append(html);
        $('#downloadButtonJava').html('').append(html);
        $('#downloadButtonPython').html('').append(html);
        $('#downloadButtonDotNet').html('').append(html);
        $('#downloadButtonPenguin').html('').append(html);

        if (!jsonData.isInputRequired) {
            $('.inputRequiredCheckboxOwnDev').removeAttr('checked', false);
            $('.inputDiv').addClass('disabledbutton');
            $(".inputLabel").css("color", '#868598');
            $(".inputLabel").text("Bot Input Not Required");
        }
        
        hiddFldJson = [
            { name: 'assignedTo', value: jsonData.assignedTo },
            { name: 'tblRpaRequest{}rpaRequestId', value: jsonData.rpaRequestId },
            { name: 'botname', value: jsonData.botname },
            { name: 'rpaexecutionTime', value: jsonData.rpaexecutionTime },
            { name: 'targetExecutionFileName', value: jsonData.targetExecutionFileName },
            { name: 'parallelWoexcution', value: jsonData.parallelWoexecution },
            { name: 'status', value: "ACCEPTED" },
            { name: 'createdBy', value: jsonData.createdBy },
            { name: 'isActive', value: jsonData.isActive },
        ];

        html_botMacro = imgDataForAjax.createHiddenFlds(hiddFldJson, '');
        $('#newMacroHidden_tblRpaRequestStr').html(html_botMacro);
        $('#newJavaHidden_tblRpaRequestStr').html(html_botMacro);
        $('#newPythonHidden_tblRpaRequestStr').html(html_botMacro);
        $('#newDotNetHidden_tblRpaRequestStr').html(html_botMacro);
        $('#newPenguinHidden_tblRpaRequestStr').html(html_botMacro);
        $('#newBotNestHidden_tblRpaRequestStr').html(html_botMacro);
        hiddFldJson = [
            { name: 'moduleClassMethod', value: 'Test()' },
            //{ name: 'moduleClassName', value: 'Test' },
        ];
        html_botMacro = imgDataForAjax.createHiddenFlds(hiddFldJson, '');
        $('#newPythonHidden_tblRpaRequestStr1').html(html_botMacro);
        hiddFldJson = [
            { name: 'moduleClassMethod', value: 'Test()' },
        ];
        html_botMacro = imgDataForAjax.createHiddenFlds(hiddFldJson, '');
        $('#newJavaHidden_tblRpaRequestStr1').html(html_botMacro);

        hiddFldJson = [
            { name: 'moduleClassMethod', value: 'Test()' },
            { name: 'moduleClassName', value: 'Test' },
        ];
        html_botMacro = imgDataForAjax.createHiddenFlds(hiddFldJson, '');
        $('#newDotNetHidden_tblRpaRequestStr1').html(html_botMacro);

        hiddFldJson = [
            { name: 'moduleClassMethod', value: 'Test()' },
        ];
        html_botMacro = imgDataForAjax.createHiddenFlds(hiddFldJson, '');
        $('#newPenguinHidden_tblRpaRequestStr1').html(html_botMacro);

        hiddFldJson = [
            { name: 'moduleClassMethod', value: 'Test()' },
            { name: 'moduleClassName', value: 'Test' },
            { name: 'reuseFactor', value: 1 },
            { name: 'lineOfCode', value: 1 },
        ];
        html_botMacro = imgDataForAjax.createHiddenFlds(hiddFldJson, '');
        $('#newBotNestHidden_tblRpaRequestStr1').html(html_botMacro);       

        $('#testBtnNewBotRequestMacro').attr({ 'data-details': JSON.stringify({ reqId: jsonData.rpaRequestId, signum: signumGlobal }) });
        $('#testBtnNewBotRequestJava').attr({ 'data-details': JSON.stringify({ reqId: jsonData.rpaRequestId, signum: signumGlobal }) });
        $('#testBtnNewBotRequestPython').attr({ 'data-details': JSON.stringify({ reqId: jsonData.rpaRequestId, signum: signumGlobal }) });
        $('#testBtnNewBotRequestDotNet').attr({ 'data-details': JSON.stringify({ reqId: jsonData.rpaRequestId, signum: signumGlobal }) });
        $('#testBtnNewBotRequestPenguin').attr({ 'data-details': JSON.stringify({ reqId: jsonData.rpaRequestId, signum: signumGlobal }) });
        $('#testBtnNewBotRequestBotNest').attr({ 'data-details': JSON.stringify({ reqId: jsonData.rpaRequestId, signum: signumGlobal }) });


        $('#stopTestBtnBotRequestMacro').attr({ 'data-details': JSON.stringify({ reqId: jsonData.rpaRequestId, signum: signumGlobal }) });
        $('#stopTestBtnBotRequestJava').attr({ 'data-details': JSON.stringify({ reqId: jsonData.rpaRequestId, signum: signumGlobal }) });
        $('#stopTestBtnBotRequestPython').attr({ 'data-details': JSON.stringify({ reqId: jsonData.rpaRequestId, signum: signumGlobal }) });
        $('#stopTestBtnBotRequestDotNet').attr({ 'data-details': JSON.stringify({ reqId: jsonData.rpaRequestId, signum: signumGlobal }) });
        $('#stopTestBtnBotRequestPenguin').attr({ 'data-details': JSON.stringify({ reqId: jsonData.rpaRequestId, signum: signumGlobal }) });
        $('#stopTestBtnBotRequestBotNest').attr({ 'data-details': JSON.stringify({ reqId: jsonData.rpaRequestId, signum: signumGlobal }) });
}
    if (feasibility === 'reject') {
        $modal2.modal('show');

        $('#rejectModal_rpaRequestId').val(jsonData.rpaRequestId);
        $('#rejectModal_assignedTo').val(jsonData.assignedTo);

    }
}

function checkForDTRCA(subActID) {
    $.isf.ajax({
        url: service_java_URL + "projectManagement/checkDTRCASubActivityId?subActivityId=" + subActID,
        context: this,
        crossdomain: true,
        dataType: 'json',
        contentType: 'application/json',
        type: 'GET',
        xhrFields: {
            withCredentials: false
        },
        success: function (data) {
            return data;
        },
        error: function (xhr, status, statusText) {
            console.log('An error occurred');
        }
    });
}

function rejectRequest() {
    pwIsf.addLayer({ text: 'Please wait ...' });

    let restData = {};
    restData = imgDataForAjax.getFormDataJson($('#rejectRequest_frm'));

    let { rpaId, assignedTo, desc } = restData;

    $.isf.ajax({
        url: service_java_URL + "botStore/updateBotForNonFeasibile/" + rpaId + "/" + assignedTo + "/" + desc,
        context: this,
        crossdomain: true,
        processData: true,
        contentType: 'application/json',
        type: 'POST',
        xhrFields: {
            withCredentials: false
        },
        success: function (data) {
            if (data.apiSuccess) {
                pwIsf.alert({ msg: 'Rejected<br>' + data.responseMsg, type: 'success' });
                $('#rejectModal').modal('hide');
            } else {
                pwIsf.alert({ msg: 'Error-<br>' + data.responseMsg, type: 'error' });
            }

        },
        error: function (xhr, status, statusText) {
            console.log('An error occurred on : createProject' + xhr.error);
        },
        complete: function (xhr, status, statusText) {
            pwIsf.removeLayer();
        }

    });
}

// submit BOT for MACRO
function submitBot() {
    pwIsf.addLayer({ text: 'Uploading files please wait ...', progressId: 'approveNewfileProgress', showSpin: false })

    let restData = {};
    restData = imgDataForAjax.getFormDataJson($('#approveRequest_frm'));

    var isInputRqdCheckedNewDevMacro = $('#inputRequiredCheckboxOwnDev-macro')[0].checked;
    let opt = {};
    opt.setImgFldsJosn = [{ sendAs: 'codefile', frmElmntAs: 'codefile' }];
    opt.formId = 'approveRequest_frm';

    let form = document.getElementById(opt.formId);
    let frmData = new FormData(form);
    frmData = imgDataForAjax.getData(opt, isInputRqdCheckedNewDevMacro);
    frmData.append('tblRpaBotstagingStr', JSON.stringify(restData)); // for extra params
    frmData.append('isInputRequired', isInputRqdCheckedNewDevMacro); 


    let reqOpt = {};
    reqOpt.url = service_java_URL_VM + "botStore/submitBotWithLanguageForMacro";
    reqOpt.progressBarId = 'approveNewfileProgress';
    reqOpt.frmData = frmData;
    reqOpt.onDone = function (data) {
        if (data.apiSuccess) {
            pwIsf.alert({ msg: 'Uploaded<br>' + data.responseMsg, type: 'success' });
            $('#testBtnNewBotRequestMacro').attr({ 'disabled': false });
            $('#stopTestBtnBotRequestMacro').attr({ 'disabled': false });
    } 
    else {
            pwIsf.alert({ msg: 'Error-<br>' + data.responseMsg, type: 'error' });
        }
    };
    imgDataForAjax.sendRequest(reqOpt);
}

// submit BOT for BotNest
function submitBotforBotNest() {
    pwIsf.addLayer({ text: 'Please wait ...', showSpin: true })
    var botNestId=$('#botNestId').val();
    let restData = {};
    restData = imgDataForAjax.getFormDataJson($('#approveRequestBotNest_frm'));

    let opt = {};
    opt.formId = 'approveRequestBotNest_frm';

    let form = document.getElementById(opt.formId);
    let frmData = new FormData(form);
    frmData = imgDataForAjax.getData(opt);
    frmData.append('tblRpaBotstagingStr', JSON.stringify(restData)); // for extra params
    frmData.append('referenceid', botNestId);
    //let service_java_URL='http://169.144.20.182:8080/isf-rest-server-java/';

    let reqOpt = {};
   
    reqOpt.url = service_java_URL + "botStore/submitBotWithLanguageForOthers";
    reqOpt.frmData = frmData;
    reqOpt.onDone = function (data) {
        if (data.apiSuccess) {
            pwIsf.removeLayer();
            pwIsf.alert({ msg: 'Success<br>' + data.responseMsg, type: 'success' });
            window.location.reload();
        }
        else {
            pwIsf.alert({ msg: 'Error-<br>' + data.responseMsg, type: 'error' });
        }
    };

    imgDataForAjax.sendRequest(reqOpt);
}

// TO APPROVE for JAVA 
function submitBotRequestJava() {
    pwIsf.addLayer({ text: 'Uploading files please wait ...', progressId: 'approveJavaProgress', showSpin: false })

    let restData = {};
    restData = imgDataForAjax.getFormDataJson($('#approveRequestJava_frm'));
    let opt = {};
    opt.setImgFldsJosn = [{ sendAs: 'codefile', frmElmntAs: 'codefile' }, { sendAs: 'exeFile', frmElmntAs: 'exeFile' }];
    opt.formId = 'approveRequestJava_frm';

    // get checkbox value and update in restDate
    var isInputRqdCheckedJava = $('#inputRequiredCheckboxOwnDev-java')[0].checked;

    let form = document.getElementById(opt.formId);
    let frmData = new FormData(form);
    //frmData = imgDataForAjax.getData(opt);

    frmData = imgDataForAjax.getData(opt, isInputRqdCheckedJava); 
    frmData.append('tblRpaBotstagingStr', JSON.stringify(restData)); // for extra params
    frmData.append('isInputRequired', isInputRqdCheckedJava); 

    //let service_java_URL='http://100.96.33.166:8080/isf-rest-server-java/';

    let reqOpt = {};
    reqOpt.url = service_java_URL_VM + "botStore/submitBotWithLanguageForJavaPython";
    reqOpt.progressBarId = 'approveJavaProgress';
    reqOpt.frmData = frmData;
    reqOpt.onDone = function (data) {
        if (data.apiSuccess) {
            pwIsf.alert({ msg: 'Uploaded<br>' + data.responseMsg, type: 'success' });
            $('#testBtnNewBotRequestJava').attr({ 'disabled': false });
            $('#stopTestBtnBotRequestJava').attr({ 'disabled': false });
        } else {
            pwIsf.alert({ msg: 'Error-<br>' + data.responseMsg, type: 'error' });
        }
    };

    imgDataForAjax.sendRequest(reqOpt);
}


function streamBot(thisObj) {
    let jsonData = $(thisObj).data('details');
    let getJsonForFormBuilder = getBotConfigJsonByApi(jsonData['rpaRequestId']);

    $('#modalBotNestStreaming').modal('show');
    $('#botdetails').data('botnestId', getJsonForFormBuilder.botNestId);
    $('#botdetails').data('bookingId', 0);
    
    let bookingid = $('#botdetails').data('bookingId');
    let botNestID = $('#botdetails').data('botnestId');
    getBotLogs(botNestID, bookingid);
        myInterval = setInterval(function () {
            getBotLogs(botNestID, bookingid);
        }, 10000);
  
    $(document).on('hidden.bs.modal', '#modalBotNestStreaming', function (e) {
        e.preventDefault(); clearInterval(myInterval);
    });
}


function getBotLogs(botNestID, booking_Id) {
    let botStatusDetails = new FormData();
    botStatusDetails.append("botId", botNestID);//bot_ID;12000101
    botStatusDetails.append("bookingId", booking_Id);//booking_Id;1wkjc9k

    $.isf.ajax({
        type: "POST",
        enctype: 'multipart/form-data',
        url: botNestExternal_URL + "getBotStatus",
        processData: false,
        contentType: false,
        cache: false,
        timeout: 600000,
        data: botStatusDetails,
        success: function (data) {
            console.log(data);
            let status = data;
            let checkLastStatus = $('#botLogStatus ul').find('li :last').text();
            if (checkLastStatus != status) {
                $("#botLogStatus ul").append('<li><span class="tab">' + data + '</span></li>');
                $(".video-js").hide();
                $("#botStream").append('<b>Video Not Available For Bot</b>');
            }
            if (data == "Video stream started") {
                $(".video-js").show();
                $("#botStream").hide();
                getVideoStreamingUrl(botNestID, booking_Id);
                
            }
        },
        error: function () {
            console.log('error');
        }
    });
}

function getVideoStreamingUrl(bot_ID, booking_Id) {
    let botVideoURLDetails = new FormData();
    botVideoURLDetails.append("botId", bot_ID);
    botVideoURLDetails.append("bookingId", booking_Id);

    $.isf.ajax({
        type: "POST",
        enctype: 'multipart/form-data',
        url: botNestExternal_URL + "getVideoStreamURL",
        processData: false,
        contentType: false,
        cache: false,
        timeout: 600000,
        data: botVideoURLDetails,
        success: function (data) {
            console.log(data);
            $($('#botVideo').find('source')[0]).attr('src', data);
            //play video using HLS.
            if (Hls.isSupported()) {
                var video = document.getElementById('botVideo');
                var hls = new Hls();
                hls.loadSource(data);
                hls.attachMedia(video);
                hls.on(Hls.Events.MANIFEST_PARSED, function () {
                    video.play();
                });
            }
        },
        error: function () {
            console.log('error');
        }
    });
}

// TO APPROVE for PYTHON
function submitBotRequestPython() {
    pwIsf.addLayer({ text: 'Uploading files please wait ...', progressId: 'approvePythonProgress', showSpin: false })

    let restData = {};
    restData = imgDataForAjax.getFormDataJson($('#approveRequestPython_frm'));
    let opt = {};
    opt.setImgFldsJosn = [{ sendAs: 'codefile', frmElmntAs: 'codefile' }, { sendAs: 'exeFile', frmElmntAs: 'exeFile' }];
    opt.formId = 'approveRequestPython_frm';

    // get checkbox value and update in restDate
    var isInputRqdCheckedPython = $('#inputRequiredCheckboxOwnDev-python')[0].checked;

    let form = document.getElementById(opt.formId);
    let frmData = new FormData(form);
    frmData = imgDataForAjax.getData(opt, isInputRqdCheckedPython);
    frmData.append('tblRpaBotstagingStr', JSON.stringify(restData)); // for extra params
    frmData.append('isInputRequired', isInputRqdCheckedPython);

    //let service_java_URL='http://100.96.33.166:8080/isf-rest-server-java/';

    let reqOpt = {};
    reqOpt.url = service_java_URL_VM + "botStore/submitBotWithLanguageForJavaPython";
    reqOpt.progressBarId = 'approvePythonProgress';
    reqOpt.frmData = frmData;
    reqOpt.onDone = function (data) {
        if (data.apiSuccess) {
            pwIsf.alert({ msg: 'Uploaded<br>' + data.responseMsg, type: 'success' });
            $('#testBtnNewBotRequestPython').attr({ 'disabled': false });
            $('#stopTestBtnBotRequestPython').attr({ 'disabled': false });

        } else {
            pwIsf.alert({ msg: 'Error-<br>' + data.responseMsg, type: 'error' });
        }
    };

    imgDataForAjax.sendRequest(reqOpt);
}

// TO APPROVE for .NET
function submitBotRequestDotNet() {
    pwIsf.addLayer({ text: 'Uploading files please wait ...', progressId: 'approvePythonProgress', showSpin: false });

    let restData = {};
    restData = imgDataForAjax.getFormDataJson($('#approveRequestDotNet_frm'));
    let opt = {};
    opt.setImgFldsJosn = [{ sendAs: 'codefile', frmElmntAs: 'codefile' }, { sendAs: 'exeFile', frmElmntAs: 'exeFile' }];
    opt.formId = 'approveRequestDotNet_frm';

    // get checkbox value and update in restDate
    var isInputRqdCheckedDotNet = $('#inputRequiredCheckboxOwnDev-dotNet')[0].checked;


    let form = document.getElementById(opt.formId);
    let frmData = new FormData(form);
    frmData = imgDataForAjax.getData(opt);
    frmData.append('tblRpaBotstagingStr', JSON.stringify(restData)); // for extra params
    frmData.append('isInputRequired', isInputRqdCheckedDotNet);

    //let service_java_URL='http://100.96.33.166:8080/isf-rest-server-java/';

    let reqOpt = {};
    reqOpt.url = service_java_URL_VM + "botStore/submitBotWithLanguageForJavaPython";
    reqOpt.progressBarId = 'approvePythonProgress';
    reqOpt.frmData = frmData;
    reqOpt.onDone = function (data) {
        if (data.apiSuccess) {
            pwIsf.alert({ msg: 'Uploaded<br>' + data.responseMsg, type: 'success' });
            $('#testBtnNewBotRequestDotNet').attr({ 'disabled': false });
            $('#stopTestBtnBotRequestDotNet').attr({ 'disabled': false });

        } else {
            pwIsf.alert({ msg: 'Error-<br>' + data.responseMsg, type: 'error' });
        }
    };

    imgDataForAjax.sendRequest(reqOpt);
}



function testReqDev(rpaRequestId, assignedTo) {

    let ajCall = $.isf.ajax({
        url: service_java_URL + "botStore/createBotTestingRequest/" + rpaRequestId + "/" + assignedTo,
        contentType: 'application/json',
        type: 'POST',
        returnAjaxObj: true
    });

    ajCall.done(function (data) {
        if (data.apiSuccess) {
            pwIsf.alert({ msg: 'Testing will be initiated after providing input to ISF Popup. Please ensure ISF Desktop App is running', type: 'success' });
            window.open('isfalert:test_' + rpaRequestId, '_self');
        }
        else {
            pwIsf.alert({ msg: '' + data.responseMsg, type: 'warning' });
        }
    });
    ajCall.fail(function () { pwIsf.alert({ msg: 'Error in BOT testing.', type: 'error' }); });
    ajCall.always(function () { pwIsf.removeLayer(); });
}

function stoptestReqDev(rpaRequestId, assignedTo) {


    pwIsf.addLayer({ text: 'Please wait ...' });
    let ajCall = $.isf.ajax({
        url: service_java_URL + "botStore/stopInprogressBot/" + rpaRequestId + "/" + assignedTo,
        contentType: 'application/json',
        type: 'POST',
        returnAjaxObj: true
    });

    ajCall.done(function (data) {
        if (data.apiSuccess) {
            pwIsf.alert({ msg: '' + data.responseMsg, type: 'success' });
            //window.open('isfalert:test_' + requestID, '_self');
        } else {
            pwIsf.alert({ msg: '' + data.responseMsg, type: 'warning' });
        }
    });

    ajCall.fail(function () { pwIsf.alert({ msg: 'Error in BOT testing.', type: 'error' }); });
    ajCall.always(function () { pwIsf.removeLayer(); });


}



// Update Macro Bot Request
function updateMacroBot() { 
    pwIsf.addLayer({ text: 'Uploading files please wait ...', progressId: 'updateMacroBotProgress', showSpin: false })

    let restData = {};
    restData = imgDataForAjax.getFormDataJson($('#updateMacroRequest_frm'));

    restData['tblRpaBotrequirements'] = [{
        "isDataFetching": 1,
        "isReportPrepration": 1,
        "outputFormat": "Excel",
        "createdBy": "ESSRMMA",
        "isActive": 1,
        "tblRpaRequestTools":
            [{
                "toolId": 4,
                "toolName": "OSS",
                "isPasswordRequired": 1,
                "isMobilePassRequired": 1,
                "isVpnrequired": 0,
                "createdBy": "ESSRMMA",
                "isActive": 1
            },
            {
                "toolId": 5,
                "toolName": "ITK",
                "isPasswordRequired": 1,
                "isMobilePassRequired": 1,
                "isVpnrequired": 0,
                "createdBy": "ESAABEH",
                "isActive": 1
            }
            ]
    }];

    // get checkbox value and update in restDate
    var isInputRequired = $("#inputRequiredCheckboxOwnDev-macroDev").is(":checked") ? true : false;
    restData['tblRpaRequestStr']['isInputRequired'] = isInputRequired;

    restData['tblRpaBotstagingStr']['reuseFactor'] = restData['tblRpaBotstagingStr']['reuseFactor'];
    restData['tblRpaBotstagingStr']['lineOfCode'] = restData['tblRpaBotstagingStr']['lineOfCode'];

    let opt = {};
    opt.setImgFldsJosn = [{ sendAs: 'infile', frmElmntAs: 'inFile' }, { sendAs: 'outfile', frmElmntAs: 'outFile' }, { sendAs: 'logicfile', frmElmntAs: 'logicFile' }, { sendAs: 'codefile', frmElmntAs: 'codeFile' }];
    opt.formId = 'updateMacroRequest_frm';

    let form = document.getElementById(opt.formId);
    let frmData = new FormData(form);
    frmData = imgDataForAjax.getData(opt);
    frmData.append('tblRpaRequestStr', JSON.stringify(restData['tblRpaRequestStr'])); // for extra params
    frmData.append('tblRpaBotstagingStr', JSON.stringify(restData['tblRpaBotstagingStr']));

    //let service_java_URL = 'http://169.144.20.129:8080/isf-rest-server-java/'
    //frmData

    let reqOpt = {};
    reqOpt.url = service_java_URL_VM + "botStore/updateBotRequest";
    reqOpt.progressBarId = 'updateMacroBotProgress';
    reqOpt.frmData = frmData;
    reqOpt.onDone = function (data) {
        if (data.apiSuccess) {
            pwIsf.alert({ msg: 'Saved<br>' + data.responseMsg, type: 'success' });
            location.reload();
        } else {
            pwIsf.alert({ msg: 'Error-<br>' + data.responseMsg, type: 'error' });
        }
    };

    imgDataForAjax.sendRequest(reqOpt);

}

// Update JAVA Bot Request
function updateBotJava() { // Update REQUEST FOR NEW java BOT
    pwIsf.addLayer({ text: 'Uploading files please wait ...', progressId: 'updateJavaBotProgress', showSpin: false })

    let restData = {};
    restData = imgDataForAjax.getFormDataJson($('#updateRequestJava_frm'));

    restData['tblRpaBotrequirements'] = [{
        "isDataFetching": 1,
        "isReportPrepration": 1,
        "outputFormat": "Excel",
        "createdBy": "ESSRMMA",
        "isActive": 1,
        "tblRpaRequestTools":
            [{
                "toolId": 4,
                "toolName": "OSS",
                "isPasswordRequired": 1,
                "isMobilePassRequired": 1,
                "isVpnrequired": 0,
                "createdBy": "ESSRMMA",
                "isActive": 1
            },
            {
                "toolId": 5,
                "toolName": "ITK",
                "isPasswordRequired": 1,
                "isMobilePassRequired": 1,
                "isVpnrequired": 0,
                "createdBy": "ESAABEH",
                "isActive": 1
            }
            ]
    }];

    // get checkbox value and update in restDate
    var isInputRequired = $("#inputRequiredCheckboxOwnDev-javaDev").is(":checked") ? true : false;
    restData['tblRpaRequestStr']['isInputRequired'] = isInputRequired;

    restData['tblRpaBotstagingStr']['reuseFactor'] = restData['tblRpaBotstagingStr']['reuseFactor'];
    restData['tblRpaBotstagingStr']['lineOfCode'] = restData['tblRpaBotstagingStr']['lineOfCode'];

    let opt = {};
    opt.setImgFldsJosn = [{ sendAs: 'infile', frmElmntAs: 'infile' }, { sendAs: 'outfile', frmElmntAs: 'outfile' }, { sendAs: 'logicfile', frmElmntAs: 'logicFile' }, { sendAs: 'codefile', frmElmntAs: 'codeFile' }, { sendAs: 'exefile', frmElmntAs: 'exefile' }];
    opt.formId = 'updateRequestJava_frm';

    let form = document.getElementById(opt.formId);
    let frmData = new FormData(form);
    frmData = imgDataForAjax.getData(opt);
    frmData.append('tblRpaRequestStr', JSON.stringify(restData['tblRpaRequestStr'])); // for extra params
    frmData.append('tblRpaBotstagingStr', JSON.stringify(restData['tblRpaBotstagingStr']));

    //let service_java_URL='http://100.96.32.171:8080/isf-rest-server-java/';

    //frmData

    let reqOpt = {};
    reqOpt.url = service_java_URL_VM + "botStore/updateBotRequest";
    reqOpt.progressBarId = 'updateJavaBotProgress';
    reqOpt.frmData = frmData;
    reqOpt.onDone = function (data) {
        if (data.apiSuccess) {
            pwIsf.alert({ msg: 'Saved<br>' + data.responseMsg, type: 'success' });
            //$('#testBtnNewBotRequestJava').attr({ 'data-details': JSON.stringify({ reqId: data.data, signum: signumGlobal }), 'disabled': false });
        } else {
            pwIsf.alert({ msg: 'Error-<br>' + data.responseMsg, type: 'error' });
        }
    };

    imgDataForAjax.sendRequest(reqOpt);

}



function testBotNest(thisObj) {

    $('#botNestTestModal').modal('show');
    $('#fileInputDiv').show();
    $('#fileinputOnLocalPlay').val('');
    $('#btnUploadFile').prop('disabled',false);
    let jsonData = $(thisObj).data('details');
    let getJsonForFormBuilder = getBotConfigJsonByApi(jsonData['rpaRequestId']);
    var rpaId = jsonData['rpaRequestId'];

  
    $('#currentdata').data('botConfigJson', getJsonForFormBuilder.botjson);
     $('#currentdata').data('rpaid', rpaId);
    $('#currentdata').data('botType', getJsonForFormBuilder.botType);
    $('#currentdata').data('botnestId', getJsonForFormBuilder.botNestId);

    let listOfFileNames = new FormData();
    listOfFileNames.append('isfBotId', rpaId);//rpaId; 1728

    //list file names
    $.isf.ajax({
        url: botNestExternal_URL + "getInputFileNames",
        enctype: 'multipart/form-data',
        type: "POST",
        data: listOfFileNames,
        processData: false,
        contentType: false,
        cache: false,
        timeout: 600000,
        success: function (data) {
            if (data.includes('fileNames')) {
                pwIsf.alert({ msg: "Please use the filenames listed in popup to upload respective files.", type: "success" });
                $($('.fileNames')[0]).text(JSON.parse(data).fileNames[0]);
                $('#currentdataFile').data('fileNames', data.fileNames);
            }
        },
        error: function () {
            console.log('error');
        },
        complete: function () {
            pwIsf.removeLayer();
        }
    });


    createFormFromFormBuilderJson('botConfigBody',getJsonForFormBuilder.botjson);
    $(document).on('click', '#btnUploadFile', function () {
        let filearr;
        let botnestID = $('#currentdata').data('botnestId');
        let input = document.getElementById('fileinputOnLocalPlay');
        let fileDataFromPlay = new FormData();
        let fileID = '';
        let filevalue = input.value;
        var pattern = /\\/;
        filearr = filevalue.split(pattern);
        let filename = filearr[2];
        let expectedfile = $('.fileNames').text();

        if (filename == expectedfile) {

            fileDataFromPlay.append('file', input.files[0]);
            fileDataFromPlay.append('botId', botnestID);// 12000101
            fileDataFromPlay.append('bookingId', 0); //akjffijk

            pwIsf.addLayer({ text: "Please wait ..." });
            $.isf.ajax({
                url: botNestExternal_URL + "uploadUserInput",
                enctype: 'multipart/form-data',
                type: "POST",
                data: fileDataFromPlay,
                processData: false,
                contentType: false,
                cache: false,
                timeout: 600000,
                success: function (data) {
                    if (data.includes('fileIds') > -1) {
                        fileID = (JSON.parse(data).fileIds.length == 0) ? null : JSON.parse(data).fileIds[0];
                        $('#currentdata').data('fileID', fileID);
                        $('#btnUploadFile').prop('disabled', true);
                        pwIsf.alert({ msg: "File Uploaded", type: 'info' });
                    }
                    else {
                        pwIsf.alert({ msg: data, type: info });
                        $('#currentdata').data('fileID', null);
                    }
                },
                error: function () {
                    console.log('error');
                },
                complete: function () {
                    pwIsf.removeLayer();
                }
            });
            $('#saveBotConfigBtn').prop('disabled', false);
        }
        else {
            pwIsf.alert({ msg: "File name should same as ", type: 'info' });
        }
    });
    
    $('.submitBotForTest').on('click', function () {

        let fileID = $('#currentdata').data('fileID');
        let botnestID = $('#currentdata').data('botnestId');
            pwIsf.addLayer({ text: 'Please wait ...' });

        let ajCall = $.isf.ajax({
            url: service_java_URL + "botStore/createBotTestingRequest/" + rpaId + "/" + signumGlobal,
                contentType: 'application/json',
                type: 'POST',
                returnAjaxObj: true
            });

            ajCall.done(function (data) {              
                let testingBookingId = "TEST_"+data.dataMap.BOT_TEST_ID;
                pwIsf.removeLayer();
                let getFormJson = updateFormBuilderJsonWithExistingFormData(JSON.parse(getJsonForFormBuilder.botjson), 'botConfigForm');             

                let botDetails = new FormData();
                botDetails.append('botId', botnestID);  //botNestID;12000101
                botDetails.append('bookingId', testingBookingId);    //unique testing bookingID.
                botDetails.append('fileIds', fileID); // file uploaded Id
                botDetails.append('botConfig', JSON.stringify(getFormJson)); // BOtConfig Json.
                let botInstanceId = '';
                $.isf.ajax({
                    type: "POST",
                    async: false,
                    enctype: 'multipart/form-data',
                    processData: false,
                    contentType: false,
                    cache: false,
                    timeout: 600000,
                    url: botNestExternal_URL + "launchBotNest",
                    data: (botDetails),
                    success: function (data) {
                        if (data.botInstanceId) {
                            console.log("botInstanceId:" + data.botInstanceId);

                            botInstanceId = data.botInstanceId;
                            pwIsf.alert({ msg: "Submitted for Testing. Please check after sometime.", type: 'success' });
                            $('#botNestTestModal').modal('hide');
                        }
                        else {
                            pwIsf.alert({ msg: 'Bot Launched Failed. Please check inputs.', type: 'error' });
                        }
                        },
                        error: function () {
                            console.log("error");
                        }
                    });
            });
            ajCall.fail(function () { pwIsf.alert({ msg: 'Error in BOT testing.', type: 'error' }); });
        ajCall.always(function () {

        });


    });
  
}

function updateBotforBotNest() {



}

// Update REQUEST FOR  python BOT
function updateBotPython() { 
    pwIsf.addLayer({ text: 'Uploading files please wait ...', progressId: 'updatePythonBotProgress', showSpin: false })

    let restData = {};
    restData = imgDataForAjax.getFormDataJson($('#updateRequestPython_frm'));

    if (restData['tblRpaBotstagingStr']['moduleClassName'] === "") {
        delete restData['tblRpaBotstagingStr']['moduleClassName'];
    }

    if (restData['tblRpaBotstagingStr']['languageBaseVersionID'] === "-1") {
        delete restData['tblRpaBotstagingStr']['languageBaseVersionID'];
    }

    restData['tblRpaBotrequirements'] = [{
        "isDataFetching": 1,
        "isReportPrepration": 1,
        "outputFormat": "Excel",
        "createdBy": "ESSRMMA",
        "isActive": 1,
        "tblRpaRequestTools":
            [{
                "toolId": 4,
                "toolName": "OSS",
                "isPasswordRequired": 1,
                "isMobilePassRequired": 1,
                "isVpnrequired": 0,
                "createdBy": "ESSRMMA",
                "isActive": 1
            },
            {
                "toolId": 5,
                "toolName": "ITK",
                "isPasswordRequired": 1,
                "isMobilePassRequired": 1,
                "isVpnrequired": 0,
                "createdBy": "ESAABEH",
                "isActive": 1
            }
            ]
    }];

    // get checkbox value and update in restDate
    var isInputRequired = $("#inputRequiredCheckboxOwnDev-pythonDev").is(":checked") ? true : false;
    restData['tblRpaRequestStr']['isInputRequired'] = isInputRequired;

    restData['tblRpaBotstagingStr']['reuseFactor'] = restData['tblRpaBotstagingStr']['reuseFactor'];
    restData['tblRpaBotstagingStr']['lineOfCode'] = restData['tblRpaBotstagingStr']['lineOfCode'];

    let opt = {};
    opt.setImgFldsJosn = [{ sendAs: 'infile', frmElmntAs: 'infile' }, { sendAs: 'outfile', frmElmntAs: 'outfile' },
        { sendAs: 'logicfile', frmElmntAs: 'logicFile' }, { sendAs: 'codefile', frmElmntAs: 'codeFile' },
        { sendAs: 'exefile', frmElmntAs: 'exefile' }, { sendAs: 'whlfile', frmElmntAs: 'whlfile' }];
    opt.formId = 'updateRequestPython_frm';

    let form = document.getElementById(opt.formId);
    let frmData = new FormData(form);
    frmData = imgDataForAjax.getData(opt);
    frmData.append('tblRpaRequestStr', JSON.stringify(restData['tblRpaRequestStr'])); // for extra params
    frmData.append('tblRpaBotstagingStr', JSON.stringify(restData['tblRpaBotstagingStr']));

    let reqOpt = {};
    reqOpt.url = service_java_URL_VM + "botStore/updateBotRequest";
    reqOpt.progressBarId = 'updatePythonBotProgress';
    reqOpt.frmData = frmData;
    reqOpt.onDone = function (data) {
        if (data.apiSuccess) {
            pwIsf.alert({ msg: 'Saved<br>' + data.responseMsg, type: 'success' });
        } else {
            pwIsf.alert({ msg: 'Error-<br>' + data.responseMsg, type: 'error' });
        }
    };

    imgDataForAjax.sendRequest(reqOpt);

}

// Update REQUEST FOR  penguin BOT
function updateBotDotnet() { 
    pwIsf.addLayer({ text: 'Uploading files please wait ...', progressId: 'updateDotnetBotProgress', showSpin: false })

    let restData = {};
    restData = imgDataForAjax.getFormDataJson($('#updateRequestDotnet_frm'));

    restData['tblRpaBotrequirements'] = [{
        "isDataFetching": 1,
        "isReportPrepration": 1,
        "outputFormat": "Excel",
        "createdBy": "ESSRMMA",
        "isActive": 1,
        "tblRpaRequestTools":
            [{
                "toolId": 4,
                "toolName": "OSS",
                "isPasswordRequired": 1,
                "isMobilePassRequired": 1,
                "isVpnrequired": 0,
                "createdBy": "ESSRMMA",
                "isActive": 1
            },
            {
                "toolId": 5,
                "toolName": "ITK",
                "isPasswordRequired": 1,
                "isMobilePassRequired": 1,
                "isVpnrequired": 0,
                "createdBy": "ESAABEH",
                "isActive": 1
            }
            ]
    }];

    // get checkbox value and update in restDate
    var isInputRequired = $("#inputRequiredCheckboxOwnDev-dotnetDev").is(":checked") ? true : false;
    restData['tblRpaRequestStr']['isInputRequired'] = isInputRequired;

    restData['tblRpaBotstagingStr']['reuseFactor'] = restData['tblRpaBotstagingStr']['reuseFactor'];
    restData['tblRpaBotstagingStr']['lineOfCode'] = restData['tblRpaBotstagingStr']['lineOfCode'];

    let opt = {};
    opt.setImgFldsJosn = [{ sendAs: 'infile', frmElmntAs: 'infile' }, { sendAs: 'outfile', frmElmntAs: 'outfile' }, { sendAs: 'logicfile', frmElmntAs: 'logicFile' }, { sendAs: 'codefile', frmElmntAs: 'codeFile' }, { sendAs: 'exefile', frmElmntAs: 'exefile' }];
    opt.formId = 'updateRequestDotnet_frm';

    let form = document.getElementById(opt.formId);
    let frmData = new FormData(form);
    frmData = imgDataForAjax.getData(opt);
    frmData.append('tblRpaRequestStr', JSON.stringify(restData['tblRpaRequestStr'])); // for extra params
    frmData.append('tblRpaBotstagingStr', JSON.stringify(restData['tblRpaBotstagingStr']));

    //let service_java_URL ='http://169.144.28.65:8080/isf-rest-server-java/';

    //frmData

    let reqOpt = {};
    reqOpt.url = service_java_URL_VM + "botStore/updateBotRequest";
    reqOpt.progressBarId = 'updateDotnetBotProgress';
    reqOpt.frmData = frmData;
    reqOpt.onDone = function (data) {
        if (data.apiSuccess) {
            pwIsf.alert({ msg: 'Saved<br>' + data.responseMsg, type: 'success' });
        } else {
            pwIsf.alert({ msg: 'Error-<br>' + data.responseMsg, type: 'error' });
        }
    };

    imgDataForAjax.sendRequest(reqOpt);

}



// get BotConfig data
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
        getJson.isInputRequired = data.inputRequired;
    });

    ajaxCall.fail(function () { pwIsf.alert({ msg: 'Getting bot config json through API.', type: 'error' }); });
    ajaxCall.always(function () { pwIsf.removeLayer(); });

    return getJson;
}

function downloadSampleJsonEdit() {
    
    let getjson = getJsonFromFormBuilder(formBuilderObjEdit, 'json');

    let actualJson = common_getActualJsonFromFormBuilderJson(getjson);

    let data = "text/json;charset=utf-8," + encodeURIComponent(JSON.stringify(actualJson));

    let a = document.getElementById('aTagDownloadSampleJsonEdit');
    a.href = 'data:' + data;
    a.download = 'data.txt';
    a.innerHTML = '';

    document.getElementById('addDownloadLinkEdit').appendChild(a);

    $('#aTagDownloadSampleJsonEdit')[0].click();


}

// INSIDE EDIT BUTTON
function updateBotData(thisObj) {
        
    let jsonData = $(thisObj).data('details');
    $('#editBody').empty();
    $('#rejectEdit').find('div.modal-title').html(`Edit Bot (${(jsonData.botlanguage === '')?'Language Not Defined' : jsonData.botlanguage})`);
    
    let getJsonForFormBuilder = getBotConfigJsonByApi(jsonData['rpaRequestId']);

    let formBuilderCommonHtml = `<div class="formBuilderDivEdit">
                                    <div class="formBuilderDivHeader" style="padding-left: 13px;">
                                    <div class="row" style="width:100%">
                                        <div class="col-md-9" style="padding:0px;"><h5>Settings For Bot Configuration</h5></div>
                                        <div class="col-md-3">
                                        <div id="addDownloadLinkEdit"><a id="aTagDownloadSampleJsonEdit"></a></div>
                                        <a href="#" onclick="downloadSampleJsonEdit()" title="Download Sample File">Sample JSON</a></div>
                                    </div>
                                </div>
                                    <div id="formBuilderWillComeHereForEdit"></div>
                                </div>`;

    let checkbox = `<div class="col-md-6">
                        <label>Input Required :</label>
                        <div style="margin-left:-15px;">
                            <div class="col-md-2" style="margin: 0px;">
                                <div class="checkbox-container">
                                    <label class="checkbox-label">
                                        <input id="inputRequiredCheckboxOwnDev-${jsonData.botlanguage.toLowerCase()}Dev" class="inputRequiredCheckboxOwnDev" type="checkbox" onchange="toggleCheckbox(this)">
                                        <span class="checkbox-custom rectangular"></span>
                                    </label>
                                </div>
                            </div>
                            <div>
                                <label class="forLabel inputLabel" for="inputRequiredCheckboxOwnDev-${jsonData.botlanguage.toLowerCase()}Dev">Bot Input Required</label>
                            </div>
                        </div>
                    </div>`;

    if (jsonData.botlanguage.toLowerCase() == "macro") {
        if (ApiProxy === false) {
            let html = `<div id="macro">
                            <div class="inputGroup row">
                                <div class="col-md-6">
                                    <form method="POST" enctype="multipart/form-data" id="updateMacroRequest_frm">
                                        <input type="hidden" class="form-control" name="tblRpaRequestStr{}rpaRequestId" value="${jsonData.rpaRequestId}" />
                                        <input type="hidden" class="form-control" name="tblRpaBotstagingStr{}statusDescription" value="ByDev" />
                                        <input type="hidden" class="form-control" name="tblRpaBotstagingStr{}modifiedBy" value="${signumGlobal}" />
                                        <input type="hidden" class="form-control" name="tblRpaBotstagingStr{}botlanguage" value="MACRO" />
                                        <input type="hidden" id="jsonFromFormBuilder_hiddenEdit" name="botConfigJson" />

                                        <div class="row">
                                            <div class="col-md-6">
                                                <label class="required">Module Name : </label>
                                                <input class="form-control" type="text" name="tblRpaBotstagingStr{}moduleClassName" value="${jsonData.classname}"/>
                                                <small>Min 1 and max 100 (No special chracters allowed)</small>
                                            </div>

                                            <div class="col-md-6">
                                                <label class="required">Module Method Name :</label>
                                                <input class="form-control" type="text" name="tblRpaBotstagingStr{}moduleClassMethod" value="${jsonData.methodname}"/>
                                                <small>Min 1 and max 100 (No special chracters allowed)</small>
                                            </div>
                                        </div>
                                        <br />

                                        <div class="row">
                                            <div class="col-md-6">
                                                <label class="required">Reuse Factor : </label>
                                                <input class="form-control" type="number" name="tblRpaBotstagingStr{}reuseFactor" value="${jsonData.reuseFactor}"/>
                                            </div>
                                            <div class="col-md-6">
                                                <label class="required">Line Of Code : </label>
                                                <input class="form-control" type="number" name="tblRpaBotstagingStr{}lineOfCode" value="${jsonData.lineOfCode}" />
                                            </div>
                                        </div>

                                       <div class="row">
                                            <div class="col-md-12">
                                                <label class="required">Upload Macro File</label>
                                                <input id="editcodefile" name="codefile" type="file"  required accept=".zip" >
                                                
                                            </div>
                                        </div>
                                        <hr />

                                        <h5><small>NOTE: Zip format allowed for file upload.</small></h5>

                                        <div class="row">
                                            ${checkbox}
                                        </div>
                                        <hr />

                                        <div class="row">
                                        <div class="col-md-4">
                                            <label class="required">Input Sample :</label>
                                            <input class="infile inputDiv" type="file"  accept=".zip" name="infile">
                                        </div>
                                        <div class="col-md-4">
                                            <label class="required">Output Sample :</label>
                                            <input type="file"  accept=".zip" name="outfile">
                                        </div>
                                        <div class="col-md-4">
                                            <label class="required">Logic Doc :</label>
                                            <input type="file"  accept=".zip" name="logicfile">
                                        </div>
                                    </div>
                                    <hr />
                                    
                                    <div class="row">
                                        <div class="pull-right">
                                            <button type="submit" class="btn btn-sm btn-success" style="margin-left:10px">Update</button>
                                           <button id="closeModal" type="button" class="btn btn-sm btn-default" data-dismiss="modal">Close</button>
                                        </div>
                                    </div>
                                </form>
                            </div>

                            <div class="col-md-6">
                                ${formBuilderCommonHtml}        
                            </div>
                        </div>

                        <div class="row">
                            <div classs="text-center">Downloadable Files</div>
                            <button class="infile inputDiv btn btn-default" type="button" title="Input File" onclick="downloadBotFiles({thisObj:this,botId:${jsonData.rpaRequestId},fileName:'input.zip'});"><i class="fa fa-download" aria-hidden="true"></i >Input File</button><button class="btn btn-default" type="button" title="Output File" onclick="downloadBotFiles({thisObj:this,botId:${jsonData.rpaRequestId},fileName:'output.zip'});"><i class="fa fa-download" aria-hidden="true"></i>Output File</button><button class="btn btn-default" type="button" title="Logic File" onclick="downloadBotFiles({thisObj:this,botId:${jsonData.rpaRequestId},fileName:'logic.zip'});"><i class="fa fa-download" aria-hidden="true"></i>Logic File</button>
                        </div>
                    </div>`;
            $('#editBody').html('').append(html);
            fileValidation();
        }
        else {
            let html = `<div id="macro">
                        <div class="inputGroup row">
                            <div class="col-md-6">
                            <form method="POST" enctype="multipart/form-data" id="updateMacroRequest_frm">
                                <input type="hidden" id="jsonFromFormBuilder_hiddenEdit" name="botConfigJson" />
                                <input type="hidden" class="form-control" name="tblRpaRequestStr{}rpaRequestId" value="${jsonData.rpaRequestId}" />
                                <input type="hidden" class="form-control" name="tblRpaBotstagingStr{}statusDescription" value="ByDev" />
                                <input type="hidden" class="form-control" name="tblRpaBotstagingStr{}modifiedBy" value="${signumGlobal}" />
                                <input type="hidden" class="form-control" name="tblRpaBotstagingStr{}botlanguage" value="MACRO" />
                                <div class="row">
                                    <div class="col-md-6">
                                        <label class="required">Module Name : </label>
                                        <input class="form-control" type="text" name="tblRpaBotstagingStr{}moduleClassName" value="${jsonData.classname}"/>
                                        <small>Min 1 and max 100 (No special chracters allowed)</small>
                                    </div>

                                    <div class="col-md-6">
                                        <label class="required">Module Method Name :</label>
                                        <input class="form-control" type="text" name="tblRpaBotstagingStr{}moduleClassMethod" value="${jsonData.methodname}"/>
                                        <small>Min 1 and max 100 (No special chracters allowed)</small>
                                    </div>
                                </div>
                                <br />
                                <div class="row">
                                    <div class="col-md-6">
                                        <label class="required">Reuse Factor : </label>
                                        <input class="form-control" type="number" name="tblRpaBotstagingStr{}reuseFactor" value="${jsonData.reuseFactor}"/>
                                    </div>
                                    <div class="col-md-6">
                                        <label class="required">Line Of Code : </label>
                                        <input class="form-control" type="number" name="tblRpaBotstagingStr{}lineOfCode" value="${jsonData.lineOfCode}" />
                                    </div>
                                </div>
                                <div class="row">
                                    <div class="col-md-4">
                                        <label class="required">Upload Macro File</label>
                                        <input id="codefile" name="codefile" type="file"  accept=".zip">
                                    </div>
                                </div>
                                <hr />
                                <h5><small>NOTE: Zip format allowed for file upload.</small></h5>

                                <div class="row">
                                    ${checkbox}
                                </div>
                                <hr />

                                <div class="row">
                                    <label class="required">Input Sample :</label>
                                    <input class="infile inputDiv" type="file"  accept=".zip" name="infile">
                                </div>
                                <div class="row">
                                    <label class="required">Output Sample :</label>
                                    <input type="file"  accept=".zip" name="outfile">
                                </div>
                                <div class="row">
                                    <label class="required">Logic Doc :</label>
                                    <input type="file"  accept=".zip" name="logicfile">
                                </div>
                                                   
                                <hr />
                                <div class="row">
                                    <div class="pull-right">
                                        <button type="submit" class="btn btn-sm btn-success" style="margin-left:10px">Update</button>
                                       <button id="closeModal" type="button" class="btn btn-sm btn-default" data-dismiss="modal">Close</button>
                                    </div>
                                </div>
                            </form>

                        </div>

                            <div class="col-md-6">
                               ${formBuilderCommonHtml} 
                            </div>
                        </div>
                      </div>`;
            $('#editBody').html('').append(html);
            fileValidation();
        }
    }
    else if (jsonData.botlanguage.toLowerCase() == "java") {
        if (ApiProxy === false) {
            let html = `<div id="java">
                        <div class="inputGroup row">
                            <div class="col-md-6">
                            <form method="POST" enctype="multipart/form-data" id="updateRequestJava_frm">
                            <input type="hidden" id="jsonFromFormBuilder_hiddenEdit" name="botConfigJson" />

                                <div id="newJavaHidden_tblRpaRequestStr"></div>
                                <div id="newJavaHidden_tblRpaRequestStr1"></div>
                               
                                <input type="hidden" class="form-control" name="tblRpaRequestStr{}rpaRequestId" value="${jsonData.rpaRequestId}" />
                                <input type="hidden" class="form-control" name="tblRpaBotstagingStr{}moduleClassMethod" value="${jsonData.methodname}" />
                                <input type="hidden" class="form-control" name="tblRpaBotstagingStr{}statusDescription" value="ByDev" />
                                <input type="hidden" class="form-control" name="tblRpaBotstagingStr{}modifiedBy" value="${signumGlobal}" />
                                <input type="hidden" class="form-control" name="tblRpaBotstagingStr{}botlanguage" value="JAVA" />
                                <div class="row">
                                    <div class="col-md-6">
                                        <label class="required">Java Class Name:</label>
                                        <input class="form-control" type="text" name="tblRpaBotstagingStr{}moduleClassName" value="${jsonData.classname}" />
                                        <small>Min 1 and max 100 (No special chracters allowed)</small>
                                    </div>
                                </div>
                                <br />
                                <div class="row">
                                    <div class="col-md-6">
                                        <label class="required">Reuse Factor : </label>
                                        <input class="form-control" type="number" name="tblRpaBotstagingStr{}reuseFactor" value="${jsonData.reuseFactor}"/>
                                    </div>
                                    <div class="col-md-6">
                                        <label class="required">Line Of Code : </label>
                                        <input class="form-control" type="number" name="tblRpaBotstagingStr{}lineOfCode" value="${jsonData.lineOfCode}"/>
                                    </div>
                                </div>
                                <br />
                                <div class="row">
                                    <div class="col-md-6">
                                        <label class="required">Upload BOT Jar File</label>
                                        <input type="file"  accept=".zip" name="exeFile">
                                    </div>
                                    <div class="col-md-6">
                                        <label class="required">Upload code zip : </label>
                                        <input type="file"  accept=".zip" name="codefile">
                                    </div>
                                </div>

                                <hr />
                                <h5><small>NOTE: Zip format allowed for file upload.</small></h5>

                                <div class="row">
                                    ${checkbox}
                                </div>
                                <hr />

                                <div class="row">
                                    <div class="col-md-4">
                                        <label class="required">Input Sample :</label>
                                        <input class="infile inputDiv" type="file"  accept=".zip" name="infile">
                                    </div>
                                    <div class="col-md-4">
                                        <label class="required">Output Sample :</label>
                                        <input type="file"  accept=".zip" name="outfile">
                                    </div>
                                    <div class="col-md-4">
                                        <label class="required">Logic Doc :</label>
                                        <input type="file"  accept=".zip" name="logicfile">
                                    </div>
                                </div>

                                <hr />

                                <div class="row">
                                    <div class="pull-right">
                                        <button type="submit" class="btn btn-sm btn-success" style="margin-left:10px">Update</button>
                                       <button id="closeModal" type="button" class="btn btn-sm btn-default" data-dismiss="modal">Close</button>
                                    </div>
                                </div>
                            </form>

                            </div>
                            <div class="col-md-6">
                                ${formBuilderCommonHtml} 
                            </div>

                        </div>
                        <div class="row">
                        <div classs="text-center">Downloadable Files</div>
                        <button class="infile inputDiv btn btn-default" title="Input File" type="button" onclick="downloadBotFiles({thisObj:this,botId:${jsonData.rpaRequestId},fileName:'input.zip'});"><i class="fa fa-download" aria-hidden="true"></i >Input File</button><button class="btn btn-default" title="Output File" type="button" onclick="downloadBotFiles({thisObj:this,botId:${jsonData.rpaRequestId},fileName:'output.zip'});"><i class="fa fa-download" aria-hidden="true"></i>Output File</button><button class="btn btn-default" type="button" title="Logic File" onclick="downloadBotFiles({thisObj:this,botId:${jsonData.rpaRequestId},fileName:'logic.zip'});"><i class="fa fa-download" aria-hidden="true"></i>Logic File</button>
                        </div>
                        </div>`;
            $('#editBody').html('').append(html);
            fileValidation();
        }
        else {
            let html = `<div id="java">
                        <div class="inputGroup row">
                            <div class="col-md-6">

                            <form method="POST" enctype="multipart/form-data" id="updateRequestJava_frm">
                                <div id="newJavaHidden_tblRpaRequestStr"></div>
                                <div id="newJavaHidden_tblRpaRequestStr1"></div>
                                <input type="hidden" id="jsonFromFormBuilder_hiddenEdit" name="botConfigJson" />
                                <input type="hidden" class="form-control" name="tblRpaRequestStr{}rpaRequestId" value="${jsonData.rpaRequestId}" />
                                <input type="hidden" class="form-control" name="tblRpaBotstagingStr{}moduleClassMethod" value="${jsonData.methodname}" />
                                <input type="hidden" class="form-control" name="tblRpaBotstagingStr{}statusDescription" value="ByDev" />
                                <input type="hidden" class="form-control" name="tblRpaBotstagingStr{}modifiedBy" value="${signumGlobal}" />
                                <input type="hidden" class="form-control" name="tblRpaBotstagingStr{}botlanguage" value="JAVA" />
                                <div class="row">
                                    <div class="col-md-6">
                                        <label class="required">Java Class Name:</label>
                                        <input class="form-control" type="text" name="tblRpaBotstagingStr{}moduleClassName" value="${jsonData.classname}" />
                                        <small>Min 1 and max 100 (No special chracters allowed)</small>
                                    </div>
                                </div>

                                <br />

                                <div class="row">
                                    <div class="col-md-6">
                                        <label class="required">Reuse Factor : </label>
                                        <input class="form-control" type="number" name="tblRpaBotstagingStr{}reuseFactor" value="${jsonData.reuseFactor}"/>
                                    </div>
                                    <div class="col-md-6">
                                        <label class="required">Line Of Code : </label>
                                        <input class="form-control" type="number" name="tblRpaBotstagingStr{}lineOfCode" value="${jsonData.lineOfCode}"/>
                                    </div>
                                </div>

                                <br />

                                <div class="row">
                                    <div class="col-md-6">
                                        <label class="required">Upload BOT Jar File</label>
                                        <input type="file"  accept=".zip" name="exeFile">
                                    </div>
                                    <div class="col-md-6">
                                        <label class="required">Upload code zip : </label>
                                        <input type="file"  accept=".zip" name="codefile">
                                    </div>
                                </div>

                                <hr />

                                <h5><small>NOTE: Zip format allowed for file upload.</small></h5>
                                
                                <div class="row">
                                    ${checkbox}
                                </div>
                                <hr />

                                <div class="row">
                                    <div class="col-md-4">
                                        <label class="required">Input Sample :</label>
                                        <input class="infile inputDiv" type="file"  accept=".zip" name="infile">
                                    </div>
                                    <div class="col-md-4">
                                        <label class="required">Output Sample :</label>
                                        <input type="file"  accept=".zip" name="outfile">
                                    </div>
                                    <div class="col-md-4">
                                        <label class="required">Logic Doc :</label>
                                        <input type="file"  accept=".zip" name="logicfile">
                                    </div>
                                </div>
                                                   

                                <hr />

                                <div class="row">
                                    <div class="pull-right">
                                        <button type="submit" class="btn btn-sm btn-success" style="margin-left:10px">Update</button>
                                       <button id="closeModal" type="button" class="btn btn-sm btn-default" data-dismiss="modal">Close</button>
                                    </div>
                                </div>
                            </form>
                        </div>
                            <div class="col-md-6">
                               ${formBuilderCommonHtml} 
                            </div>

                        </div>



            </div>`;
            $('#editBody').html('').append(html);
            fileValidation();
        }
    }
    else if (jsonData.botlanguage.toLowerCase() == "python") {
        if (ApiProxy === false) {
            let html = `<div id="python">
                        <div class="inputGroup row">
                        <div class="col-md-6">
                            <form method="POST" enctype="multipart/form-data" id="updateRequestPython_frm">
                                <div id="newPythonHidden_tblRpaRequestStr"></div>
                                <div id="newPythonHidden_tblRpaRequestStr1"></div>
                                <input type="hidden" id="jsonFromFormBuilder_hiddenEdit" name="botConfigJson" />
                                <input type="hidden" class="form-control" name="tblRpaRequestStr{}rpaRequestId" value="${jsonData.rpaRequestId}" />
                                <input type="hidden" class="form-control" name="tblRpaBotstagingStr{}moduleClassMethod" value="${jsonData.methodname}" />
                                <input type="hidden" class="form-control" name="tblRpaBotstagingStr{}modifiedBy" value="${signumGlobal}" />
                                <input type="hidden" class="form-control" name="tblRpaBotstagingStr{}moduleClassName" value="${jsonData.classname}" />
                                <input type="hidden" class="form-control" name="tblRpaBotstagingStr{}statusDescription" value="ByDev" />
                                <input type="hidden" class="form-control" name="tblRpaBotstagingStr{}botlanguage" value="PYTHON" />
                                <div class="row">
                                    <div class="col-md-6">
                                        <label class="required">Reuse Factor : </label>
                                        <input class="form-control" type="number" name="tblRpaBotstagingStr{}reuseFactor" value="${jsonData.reuseFactor}"/>
                                    </div>
                                    <div class="col-md-6">
                                        <label class="required">Line Of Code : </label>
                                        <input class="form-control" type="number" name="tblRpaBotstagingStr{}lineOfCode" value="${jsonData.lineOfCode}"/>
                                    </div>
                                </div>
                                <br />
                                <div class="row">
                                    <div class="col-md-6">
                                        <label class="required">Upload whl file</label>
                                        <input type="file" aria-required="true" accept=".whl" name="whlfile" id="whlFileInput">
                                    </div>
                                    <div class="col-md-6">
                                        <label class="required">Upload code zip : </label>
                                        <input type="file"  accept=".zip" name="codefile">
                                    </div>
                                </div>

                                <hr />
                                <h5><small>NOTE: Zip format allowed for file upload. For Python Bots .whl file need to be Uploaded.</small></h5>

                                <div class="row">
                                    ${checkbox}
                                </div>
                                <hr />

                                <div class="row">
                                    <div class="col-md-4">
                                        <label class="required">Input Sample :</label>
                                        <input class="infile inputDiv" type="file"  accept=".zip" name="infile">
                                    </div>
                                    <div class="col-md-4">
                                        <label class="required">Output Sample :</label>
                                        <input type="file"  accept=".zip" name="outfile">
                                    </div>
                                    <div class="col-md-4">
                                        <label class="required">Logic Doc :</label>
                                        <input type="file"  accept=".zip" name="logicfile">
                                    </div>
                                </div>

                                <hr />

                                <div class="row">
                                    <br />
                                    <div class="col-md-6">
                                        <label class="required">Python Base Version :</label>
                                        <select class="select2able select2-offscreen select2-hidden-accessible" name="tblRpaBotstagingStr{}languageBaseVersionID"
                                            id="selectVersion" data-selectnext="0" tabindex="-1" aria-hidden="true">
                                        </select>
                                    </div>
                                    <div class="col-md-6">
                                        <label class="required">Package Name :</label>
                                        <input type="text" class="form-control" name="tblRpaBotstagingStr{}moduleClassName" id="packageNameInput" style="display: block;">
                                    </div>
                                </div>

                                <hr />

                                <div class="row">
                                    <div class="pull-right">
                                        <button type="submit" class="btn btn-sm btn-success" style="margin-left:10px">Update</button>
                                        <button id="closeModal" type="button" class="btn btn-sm btn-default" data-dismiss="modal">Close</button>
                                    </div>
                                </div>

                            </form>
                            </div>
                            <div class="col-md-6">
                                ${formBuilderCommonHtml} 
                            </div>

                        </div>
                    


                        <div class="row">
                        <div classs="text-center">Downloadable Files</div>
                        <button class="infile inputDiv btn btn-default" title="Input File" type="button" onclick="downloadBotFiles({thisObj:this,botId:${jsonData.rpaRequestId},fileName:'input.zip'});"><i class="fa fa-download" aria-hidden="true"></i >Input File</button><button class="btn btn-default" title="Output File" type="output" onclick="downloadBotFiles({thisObj:this,botId:${jsonData.rpaRequestId},fileName:'output.zip'});"><i class="fa fa-download" aria-hidden="true"></i>Output File</button><button class="btn btn-default" type="button" title="Logic File" onclick="downloadBotFiles({thisObj:this,botId:${jsonData.rpaRequestId},fileName:'logic.zip'});"><i class="fa fa-download" aria-hidden="true"></i>Logic File</button>
                        </div>
                        </div>`;
            $('#editBody').html('').append(html);
            fileValidation();
        }
        else {
            let html = `<div id="python">
                        <div class="inputGroup row">
                        <div class="col-md-6">
                            <form method="POST" enctype="multipart/form-data" id="updateRequestPython_frm">
                                <div id="newPythonHidden_tblRpaRequestStr"></div>
                                <div id="newPythonHidden_tblRpaRequestStr1"></div>
                                <input type="hidden" id="jsonFromFormBuilder_hiddenEdit" name="botConfigJson" />
                                <input type="hidden" class="form-control" name="tblRpaRequestStr{}rpaRequestId" value="${jsonData.rpaRequestId}" />
                                <input type="hidden" class="form-control" name="tblRpaBotstagingStr{}moduleClassMethod" value="${jsonData.methodname}" />
                                <input type="hidden" class="form-control" name="tblRpaBotstagingStr{}modifiedBy" value="${signumGlobal}" />
                                <input type="hidden" class="form-control" name="tblRpaBotstagingStr{}moduleClassName" value="${jsonData.classname}" />
                                <input type="hidden" class="form-control" name="tblRpaBotstagingStr{}statusDescription" value="ByDev" />
                                <input type="hidden" class="form-control" name="tblRpaBotstagingStr{}botlanguage" value="PYTHON" />
                                <div class="row">
                                    <div class="col-md-6">
                                        <label class="required">Reuse Factor : </label>
                                        <input class="form-control" type="number" name="tblRpaBotstagingStr{}reuseFactor" value="${jsonData.reuseFactor}"/>
                                    </div>
                                    <div class="col-md-6">
                                        <label class="required">Line Of Code : </label>
                                        <input class="form-control" type="number" name="tblRpaBotstagingStr{}lineOfCode" value="${jsonData.lineOfCode}"/>
                                    </div>
                                </div>
                                <br />
                                <div class="row">
                                    <div class="col-md-6">
                                        <label class="required">Upload whl file</label>
                                        <input type="file" aria-required="true" accept=".whl" name="whlfile" id="whlFileInput">
                                    </div>
                                    <div class="col-md-6">
                                        <label class="required">Upload code zip : </label>
                                        <input type="file"  accept=".zip" name="codefile">
                                    </div>
                                </div>

                                <hr />
                                <h5><small>NOTE: Zip format allowed for file upload. For Python Bots .whl file need to be Uploaded.</small></h5>

                                <div class="row">
                                    ${checkbox}
                                </div>
                                <hr />

                                 <div class="row">
                                    <div class="col-md-4">
                                        <label style="margin-left:10px" class="required">Input Sample :</label>
                                        <input class="infile inputDiv" style="margin-left:10px" class="btn btn-default" type="file"  accept=".zip" name="infile">
                                    </div>
                                    <div class="col-md-4">
                                        <label style="margin-left:10px" class="required">Output Sample :</label>
                                        <input style="margin-left:10px" class="btn btn-default" type="file"  accept=".zip" name="outfile">
                                    </div>
                                    <div class="col-md-4">
                                        <label style="margin-left:10px" class="required">Logic Doc :</label>
                                        <input style="margin-left:10px" class="btn btn-default" type="file"  accept=".zip" name="logicfile">
                                    </div>
                                </div>

                                <hr />

                                <div class="row">
                                    <br />
                                    <div class="col-md-6">
                                        <label class="required">Python Base Version :</label>
                                        <select class="select2able select2-offscreen select2-hidden-accessible" name="tblRpaBotstagingStr{}languageBaseVersionID"
                                            id="selectVersion" data-selectnext="0" tabindex="-1" aria-hidden="true">
                                        </select>
                                    </div>
                                    <div class="col-md-6">
                                        <label class="required">Package Name :</label>
                                        <input type="text" class="form-control" name="tblRpaBotstagingStr{}moduleClassName" id="packageNameInput" style="display: block;">
                                    </div>
                                </div>
                                    
                                <hr />

                                <div class="row">
                                    <div class="pull-right">
                                        <button type="submit" class="btn btn-sm btn-success" style="margin-left:10px">Update</button>
                                        <button id="closeModal" type="button" class="btn btn-sm btn-default" data-dismiss="modal">Close</button>
                                    </div>
                                </div>

                            </form>

                            </div>
                            <div class="col-md-6">
                                ${formBuilderCommonHtml} 
                            </div>

                        </div>
                  </div>`;
            $('#editBody').html('').append(html);
            fileValidation();
        }
        getPythonBaseVersion('#selectVersion');
    }
    else if (jsonData.botlanguage.toLowerCase() == "dotnet") {
        if (ApiProxy === false) {
            let html = `<div id="dotnet">
                        <div class="inputGroup row">
                        <div class="col-md-6">
                            <form method="POST" enctype="multipart/form-data" id="updateRequestDotnet_frm">
                                <div id="newDotNetHidden_tblRpaRequestStr"></div>
                                <div id="newDotNetHidden_tblRpaRequestStr1"></div>
                            <input type="hidden" id="jsonFromFormBuilder_hiddenEdit" name="botConfigJson" />
                            <input type="hidden" class="form-control" name="tblRpaRequestStr{}rpaRequestId" value="${jsonData.rpaRequestId}" />
                            <input type="hidden" class="form-control" name="tblRpaBotstagingStr{}moduleClassMethod" value="${jsonData.methodname}" />
                            <input type="hidden" class="form-control" name="tblRpaBotstagingStr{}modifiedBy" value="${signumGlobal}" />
                            <input type="hidden" class="form-control" name="tblRpaBotstagingStr{}moduleClassName" value="${jsonData.classname}" />
                            <input type="hidden" class="form-control" name="tblRpaBotstagingStr{}statusDescription" value="ByDev" />
                            <input type="hidden" class="form-control" name="tblRpaBotstagingStr{}botlanguage" value="DOTNET" />
                                <div class="row">
                                    <div class="col-md-6">
                                        <label class="required">Reuse Factor : </label>
                                        <input class="form-control" type="number" name="tblRpaBotstagingStr{}reuseFactor" value="${jsonData.reuseFactor}"/>
                                    </div>
                                    <div class="col-md-6">
                                        <label class="required">Line Of Code : </label>
                                        <input class="form-control" type="number" name="tblRpaBotstagingStr{}lineOfCode" value="${jsonData.lineOfCode}"/>
                                    </div>
                                </div>
                                <br />
                                <div class="row">
                                    <div class="col-md-6">
                                        <label class="required">Upload EXE</label>
                                        <input type="file" accept=".zip" name="exeFile">
                                    </div>
                                    <div class="col-md-6">
                                        <label class="required">Upload code zip : </label>
                                        <input type="file"  accept=".zip" name="codefile">
                                    </div>
                                </div>

                                <hr />
                                <h5><small>NOTE: Zip format allowed for file upload.</small></h5>

                                <div class="row">
                                    ${checkbox}
                                </div>
                                <hr />

                                <div class="row">
                                    <div class="col-md-4">
                                        <label class="required">Input Sample :</label>
                                        <input class="infile inputDiv" type="file"  accept=".zip" name="infile">
                                    </div>
                                    <div class="col-md-4">
                                        <label class="required">Output Sample :</label>
                                        <input type="file"  accept=".zip" name="outfile">
                                    </div>
                                    <div class="col-md-4">
                                        <label class="required">Logic Doc :</label>
                                        <input type="file"  accept=".zip" name="logicfile">
                                    </div>
                                </div>

                                <hr />

                                <div class="row">
                                    <div class="pull-right">
                                        <button type="submit" class="btn btn-sm btn-success" style="margin-left:10px">Update</button>
                                        <button id="closeModal" type="button" class="btn btn-sm btn-default" data-dismiss="modal">Close</button>
                                    </div>
                                </div>

                            </form>
                            </div>
                            <div class="col-md-6">
                                ${formBuilderCommonHtml} 
                            </div>

                        </div>
                    


                        <div class="row">
                        <div classs="text-center">Downloadable Files</div>
                        <button class="infile inputDiv btn btn-default" title="Input File" type="button" onclick="downloadBotFiles({thisObj:this,botId:${jsonData.rpaRequestId},fileName:'input.zip'});"><i class="fa fa-download" aria-hidden="true"></i >Input File</button><button class="btn btn-default" title="Output File" type="button" onclick="downloadBotFiles({thisObj:this,botId:${jsonData.rpaRequestId},fileName:'output.zip'});"><i class="fa fa-download" aria-hidden="true"></i>Output File</button><button class="btn btn-default" title="Logic File" type="button" onclick="downloadBotFiles({thisObj:this,botId:${jsonData.rpaRequestId},fileName:'logic.zip'});"><i class="fa fa-download" aria-hidden="true"></i>Logic File</button>
                        </div>
                        </div>`;
            $('#editBody').html('').append(html);
            fileValidation();
        }
        else {
            let html = `<div id="dotnet">
                        <div class="inputGroup row">
                        <div class="col-md-6">

                            <form method="POST" enctype="multipart/form-data" id="updateRequestDotnet_frm">
                                <div id="newDotNetHidden_tblRpaRequestStr"></div>
                                <div id="newDotNetHidden_tblRpaRequestStr1"></div>
                                
                                <input type="hidden" id="jsonFromFormBuilder_hiddenEdit" name="botConfigJson" />

                                <input type="hidden" class="form-control" name="tblRpaRequestStr{}rpaRequestId" value="${jsonData.rpaRequestId}" />
                                <input type="hidden" class="form-control" name="tblRpaBotstagingStr{}moduleClassMethod" value="${jsonData.methodname}" />
                                <input type="hidden" class="form-control" name="tblRpaBotstagingStr{}modifiedBy" value="${signumGlobal}" />
                                <input type="hidden" class="form-control" name="tblRpaBotstagingStr{}moduleClassName" value="${jsonData.classname}" />
                                <input type="hidden" class="form-control" name="tblRpaBotstagingStr{}statusDescription" value="ByDev" />
                                <input type="hidden" class="form-control" name="tblRpaBotstagingStr{}botlanguage" value="DOTNET" />
                                <div class="row">
                                    <div class="col-md-6">
                                        <label class="required">Reuse Factor : </label>
                                        <input class="form-control" type="number" name="tblRpaBotstagingStr{}reuseFactor" value="${jsonData.reuseFactor}"/>
                                    </div>
                                    <div class="col-md-6">
                                        <label class="required">Line Of Code : </label>
                                        <input class="form-control" type="number" name="tblRpaBotstagingStr{}lineOfCode" value="${jsonData.lineOfCode}"/>
                                    </div>
                                </div>
                                <br />
                                <div class="row">
                                    <div class="col-md-6">
                                        <label class="required">Upload EXE</label>
                                        <input type="file" accept=".zip" name="exeFile">
                                    </div>
                                    <div class="col-md-6">
                                        <label class="required">Upload code zip : </label>
                                        <input type="file"  accept=".zip" name="codefile">
                                    </div>
                                </div>

                                <hr />
                                <h5><small>NOTE: Zip format allowed for file upload.</small></h5>

                                <div class="row">
                                    ${checkbox}
                                </div>
                                <hr />

                                 <div class="row">
                                    <div class="col-md-4">
                                        <label style="margin-left:10px" class="required">Input Sample :</label>
                                        <input class="infile inputDiv" style="margin-left:10px" class="btn btn-default" type="file"  accept=".zip" name="infile">
                                    </div>
                                    <div class="col-md-4">
                                        <label style="margin-left:10px" class="required">Output Sample :</label>
                                        <input style="margin-left:10px" class="btn btn-default" type="file"  accept=".zip" name="outfile">
                                    </div>
                                    <div class="col-md-4">
                                        <label style="margin-left:10px" class="required">Logic Doc :</label>
                                        <input style="margin-left:10px" class="btn btn-default" type="file"  accept=".zip" name="logicfile">
                                    </div>
                                </div>

                                <hr />

                                <div class="row">
                                    <div class="pull-right">
                                        <button type="submit" class="btn btn-sm btn-success" style="margin-left:10px">Update</button>
                                        <button id="closeModal" type="button" class="btn btn-sm btn-default" data-dismiss="modal">Close</button>
                                    </div>
                                </div>

                            </form>

                            </div>
                            <div class="col-md-6">
                               ${formBuilderCommonHtml} 
                            </div>

                        </div>
                  </div>`;
            $('#editBody').html('').append(html);
            fileValidation();
        }
    }


    //populate checkbox by isInputRequired value from DB
    loadCheckbox(jsonData);

    function getJsonFormBuilderEdit() {
        let getjson = getJsonFromFormBuilder(formBuilderObjEdit);
        return getjson;
    }
    

    $('#updateMacroRequest_frm').on('submit', function (e) {
        e.preventDefault();
        $('#jsonFromFormBuilder_hiddenEdit').val(getJsonFormBuilderEdit());
        updateMacroBot();

    });
    //END - APPROVE REQUEST FORM SUBMIT

    //START - APPROVE REQUEST FOR JAVA
    $('#updateRequestJava_frm').on('submit', function (e) {
        e.preventDefault();
        $('#jsonFromFormBuilder_hiddenEdit').val(getJsonFormBuilderEdit());
        updateBotJava();

    });


    //END - APPROVE REQUEST FOR JAVA
    //START - APPROVE REQUEST FOR BOT NEST
    $('#updateRequestBotNest_frm').on('submit', function (e) {
        e.preventDefault();
        $('#jsonFromFormBuilder_hiddenEdit').val(getJsonFormBuilderEdit());
        updateBotforBotNest();

    });

    //END - APPROVE REQUEST FOR BOT NEST
    //START - APPROVE REQUEST FOR Python
    $('#updateRequestPython_frm').on('submit', function (e) {
        e.preventDefault();
        $('#jsonFromFormBuilder_hiddenEdit').val(getJsonFormBuilderEdit());
        if (areWhlFieldsValidated()) {
            updateBotPython();
        }

    });

  

    //SUBMIT FOR DOTNET
    $('#updateRequestDotnet_frm').on('submit', function (e) {
        e.preventDefault();
        $('#jsonFromFormBuilder_hiddenEdit').val(getJsonFormBuilderEdit());
        updateBotDotnet();

    });

    $('#rejectEdit').modal('show');
    $('#formBuilderWillComeHereForEdit').empty();
    if (typeof getJsonForFormBuilder != 'undefined') {
        formBuilderObjEdit = configureFormBuilderFromOldJson('formBuilderWillComeHereForEdit', getJsonForFormBuilder.botjson);
    } else {
        formBuilderObjEdit = configureFormBuilder('formBuilderWillComeHereForEdit');
    }

}

function loadCheckbox(jsonData) {
    let ids = '#inputRequiredCheckboxOwnDev-' + jsonData.botlanguage.toLowerCase() + 'Dev';

    if (jsonData.isInputRequired) {
        $(ids).attr('checked', jsonData.isInputRequired);
        $('.infile').removeClass("disabledbutton");
    } else {
        $(".inputLabel").css("color", '#868598');
        $(".inputLabel").text("Bot Input Not Required");
        $('.infile').addClass("disabledbutton");
    }
}

function getPythonBaseVersion(id) {

    $.isf.ajax({
        async: false,
        type: "GET",
        url: `${service_java_URL}botStore/getLanguageBaseVersion`,
        dataType: "json",
        success: function (data) {
            if (!data.isValidationFailed) {
                var versions = data.responseData;
                $(id).empty();
                $(id).append("<option value='-1'>Select Version</option>");
                $.each(versions, function (i, d) {
                    if (d.languageBaseVersion === PythonDefaultBaseVersion) {
                        $(id).append(`<option selected value="${d.languageBaseVersionID}">${d.languageBaseVersion}</option>`);
                    }
                    else {
                        $(id).append(`<option value="${d.languageBaseVersionID}">${d.languageBaseVersion}</option>`);
                    }
                });
            }
        },
        error: function () {
            pwIsf.removeLayer();
            console.log("Error in Python version API calling");
        },
        complete: function () {
            pwIsf.removeLayer();
        }
    });

    $(id).select2({
        closeOnSelect: true,
        placeholder: 'Select Version'
    });
}

function areWhlFieldsValidated() {
    const isFileEmpty = document.getElementById('whlFileInput').files.length === 0;
    const isVersionEmpty = $('#selectVersion').val() === '-1';
    const isPackageNameEmpty = $('#packageNameInput').val() === '';
    const areAllWhlFieldsEmpty = isFileEmpty && isVersionEmpty && isPackageNameEmpty;
    const areWhlFieldImbalanced = (!areAllWhlFieldsEmpty) && (isFileEmpty || isVersionEmpty || isPackageNameEmpty);

    let validationStatus = false;

    if (areAllWhlFieldsEmpty) {
        validationStatus = true;
    }
    else if (areWhlFieldImbalanced) {
        pwIsf.alert({ msg: "Python Base Version, Package Name and .whl file are mandatory if any one is filled!", type: "warning" });
    }
    else {
        validationStatus = whlFieldsValidationWhenFilled('whlFileInput', 'selectVersion', 'packageNameInput');
    }

    return validationStatus;
}

function whlFieldsValidationWhenFilled(whlId, versionId, packageId) {
    const whlFileName = document.getElementById(whlId).files[0].name.split('.whl')[0];
    const whlFileContentArr = whlFileName.split('-');
    const whlFileContentArrCopy = [...whlFileContentArr];
    const isWhlFileFormatInValid = (whlFileContentArr.length - 1) < 4;
    const packageName = $(`#${packageId}`).val();
    const packageNameFromWhlFile = isWhlFileFormatInValid ? "" : whlFileContentArrCopy.reverse().slice(4).reverse().join('-');
    const inputVersionNameArr = $(`#${versionId} option:selected`).text().split('.');
    const inputVersionNameForComparison = `${inputVersionNameArr[0]}${inputVersionNameArr[1]}`;
    const fileVersionNameForComparison = isWhlFileFormatInValid ? "" : whlFileContentArr[whlFileContentArr.length - 3].replace('py', '');
    let validationStatus = false;

    if (isWhlFileFormatInValid) {
        pwIsf.alert({ msg: "Warning:  WHL file's name is not in correct format", type: "warning" });
    }
    else if (packageName !== packageNameFromWhlFile) {
        pwIsf.alert({ msg: "Warning:  WHL file's name should match with package Name", type: "warning" });
    }
    else if (inputVersionNameForComparison !== fileVersionNameForComparison) {
        pwIsf.alert({ msg: "Warning:  WHL file's Version should match with Python Base Version", type: "warning" });
    }
    else {
        validationStatus = true;
    }

    return validationStatus;
}

function fileValidation() {
    $('input[type="file"]').on("change", function (evt) {
        const files = evt.target.files;
        if (files.length !== 0) {
            const elementName = event.target.name.toLowerCase();
            const fileName = evt.target.files[0].name;
            if (elementName !== C_WHL_FILE_NAME) {
                const isZipFolder = zipFolderType(fileName);
                if (!isZipFolder) {
                    pwIsf.alert({ msg: C_WRONG_TYPE, autoClose: 3 });
                    evt.target.value = "";
                }
                else {
                    handleFiles(evt, files[0]);
                }
            }
            else {
                const whlType = whlFileType(fileName);
                if (whlType !== C_WHL_EXT) {
                    pwIsf.alert({ msg: C_WRONG_TYPE, autoClose: 3 });
                    evt.target.value = "";
                }
            }
        }
    });
}

