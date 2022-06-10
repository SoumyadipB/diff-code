let formBuilderObjOnBotStore;

function getFormBuilderOnBotStore() {
    $('#formBuilderWillComeHere').empty();
    formBuilderObjOnBotStore = configureFormBuilder('formBuilderWillComeHere');
}

function getJsonFormBuilderOnBotStore() {
    let getjson = getJsonFromFormBuilder(formBuilderObjOnBotStore);
    return getjson;
}

$(document).ready(function () {

    $("#languageUl_forCreation a").click(function (e) {  // Event on language tab
        let selectedTabHref = $(e.target).attr('href');

        $(this).tab('show');
        getFormBuilderOnBotStore();
        //if (selectedTabHref == "#macroDiv" || selectedTabHref == "#javaDiv" || selectedTabHref == "#pythonDiv") {
        //    $('.formBuilderDivOnBotStore').show();
            
        //} else {
        //    $('.formBuilderDivOnBotStore').hide();
        //}
    });
    //$('#dtrcaConfig').modal('toggle');
    $('#dtrcaConfig').on('show.bs.modal', function () {
        $(this).find('.modal-content').css({
            width: 'auto', //probably not needed
            height: 'auto', //probably not needed
            'max-width': '50%',
            'margin': 'auto'
        });
    });
    var urlParams = new URLSearchParams(window.location.search);
    subID = urlParams.get("subId");
    projID = urlParams.get("projID");
    version = urlParams.get("version");
    domain = urlParams.get("domain");
    subDomain = urlParams.get("subDomain");
    subServiceArea = urlParams.get("subServiceArea");
    tech = urlParams.get("tech");
    activity = urlParams.get("activity");
    subActivity = urlParams.get("subActivity");
    scopeName = urlParams.get("scopeName");
    scopeID = urlParams.get("scopeID");
    experiencedMode = urlParams.get('experiencedMode');
    wfid = urlParams.get('wfid');
    if (experiencedMode == null) { experiencedMode=0;}
    wfOwnerName = urlParams.get('wfownerName');
    $('#projectData').append("<table style='display:initial'><tr><th>ProjectID</th><th>Domain</th><th>SubDomain</th><th>SubServiceArea</th><th>Technology</th><th>DeliverableID</th><th>DeliverableName</th><th>Activity</th><th>SubActivity</th></tr><tr><td>" + projID + "</td><td>" + domain + "</td><td>" + subDomain + "</td><td>" + subServiceArea + "</td><td>" + tech + "</td><td>" + scopeID + "</td><td>" + scopeName + "</td><td>" + activity + "</td><td>" + subActivity + "</td></tr></table>");

    //START- submit new request for bot
    
    $('input[type="file"]').on("change", function (evt) {
        let botType = $('#currentCelldata').data('botType');
        const files = evt.target.files;
        if (files.length !== 0) {
            botType = botType || '';
            if (botType && !(botType.toLowerCase().includes('nest'))) {
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
        }
    });

    $('#raisNewBotFrm').on('submit', function (e) {
        e.preventDefault(); 
            sendNewBotRequest();       
    });

    //END- submit new request for bot
    //START - submit new request with bot
    $('#newBotMacroFrm').on('submit', function (e) {
        e.preventDefault();
        $('#jsonFromFormBuilderForMacro_hiddenOnBotStore').val(getJsonFormBuilderOnBotStore());
        newBotRequest();
    });
    //END - submit new request with bot

    //START - submit new request with bot
    $('#newBotJavaFrm').on('submit', function (e) {
        e.preventDefault();
        $('#jsonFromFormBuilderForJava_hiddenOnBotStore').val(getJsonFormBuilderOnBotStore());      
        newBotRequestJava();
    });
    //END - submit new request with bot

    //START - submit new request with bot
    $('#newBotPythonFrm').on('submit', function (e) {
        e.preventDefault();
        $('#jsonFromFormBuilderForPython_hiddenOnBotStore').val(getJsonFormBuilderOnBotStore());
            newBotRequestPython();
    });
    //END - submit new request with bot

    //START - submit new request with bot
    $('#newBotDotNetFrm').on('submit', function (e) {
        e.preventDefault();
        $('#jsonFromFormBuilderForDotnet_hiddenOnBotStore').val(getJsonFormBuilderOnBotStore());
            newBotRequestDotNet();
    });
    //END - submit new request with bot


  
    if (ApiProxy == true) {
        $('#creationTabBotStore').hide();
        $('#tabnewbotRequest').hide();
    }
    //START - GET TAB CLICK EVENT
    $('#mainTabs a[data-toggle="pill"]').on('shown.bs.tab', function (e) {
        var target = $(e.target).attr("href") // activated tab
        
            if (target == '#creationMaster') {
                var activeProfileObj = JSON.parse(ActiveProfileSession);
                var roleID = activeProfileObj.roleID;
                if (roleID == 5 || roleID == 1 || roleID == 7 || roleID == 9) {
                    $('#creationDR').show();
                    $('#creation').hide();
                } else {
                    $('#creationDR').hide();
                    $('#creation').show();
                }
            }
        
        //else {
        //    $('#creationTabBotStore').hide();
        //    $('#tabnewbotRequest').hide();
        //}
    });


    // GET LANGUAGE NAME WHEN CLICK ON TAB 
    $('#approveBotRequestUL a[data-toggle="pill"]').on('shown.bs.tab', function (e) {

        let getText = $(e)[0].target.text; // activated tab
        $('#botLanguage').val(jQuery.trim(getText));
    });

    // GET LANGUAGE NAME WHEN CLICK ON TAB - CREATE BOT REQUEST
    $('#languageUl_forCreation a[data-toggle="pill"]').on('shown.bs.tab', function (e) {

        let getText = $(e)[0].target.text; // activated tab
        $('#hiddenLanguageNameForBotNewRequest_Macro').val(jQuery.trim(getText));
        $('#hiddenLanguageNameForBotNewRequest_Java').val(jQuery.trim(getText));
        $('#hiddenLanguageNameForBotNewRequest_Python').val(jQuery.trim(getText));
        $('#hiddenLanguageNameForBotNewRequest_DotNet').val(jQuery.trim(getText));
        $('#hiddenLanguageNameForBotNewRequest_Penguin').val(jQuery.trim(getText));
    });


    //START- REJECT REQUEST FORM SUBMIT

    $('#rejectRequest_frm').on('submit', function (e) {
        e.preventDefault();
        rejectRequest();

    });
    //END- REJECT REQUEST FORM SUBMIT

    //START - APPROVE REQUEST FORM SUBMIT
    $('#approveRequest_frm').on('submit', function (e) {
        e.preventDefault();
        submitBot();

    });
    //END - APPROVE REQUEST FORM SUBMIT

    //START - APPROVE REQUEST FOR JAVA
    $('#approveRequestJava_frm').on('submit', function (e) {
        e.preventDefault();
        submitBotRequestJava();

    });


    //END - APPROVE REQUEST FOR JAVA

    //START - APPROVE REQUEST FOR Python
    $('#approveRequestPython_frm').on('submit', function (e) {
        e.preventDefault();
        submitBotRequestPython();

    });


    //END - APPROVE REQUEST FOR Python

    //START - APPROVE REQUEST FOR DotNet
    $('#approveRequestDotNet_frm').on('submit', function (e) {
        e.preventDefault();
        submitBotRequestDotNet();

    });


    //END - APPROVE REQUEST FOR DotNet


  


    //START - TEST FOR NEW BOT
    $('.testNewBotRequest').on('click', function () {
        if ($(this).attr('data-details') != '') {
            pwIsf.addLayer({ text: 'Please wait ...' });

            let jsonObj = JSON.parse($(this).attr('data-details'));
            let { reqId, signum } = jsonObj;

            let ajCall = $.isf.ajax({
                url: service_java_URL + "botStore/createBotTestingRequest/" + reqId + "/" + signum,
                contentType: 'application/json',
                returnAjaxObj: true, 
                type: 'POST'

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

        //END - TEST FOR NEW BOT
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
   
});

//START - A COMMON FUNCTION FOR IMAGE UPLOAD
let imgDataForAjax = {};

imgDataForAjax.createHiddenFlds=function(jsonNameIdVal,idPreFix){  //create hidden flds from json 
    let html='';
    for(let fk in jsonNameIdVal){
        let nm=jsonNameIdVal[fk]['name'];
        let inputId=(jQuery.trim(jsonNameIdVal[fk]['id'])!='')?`id="${idPreFix}_${jsonNameIdVal[fk]['id']}"`:'';
        let val=(jQuery.trim(jsonNameIdVal[fk]['value'])!='')?`value="${jsonNameIdVal[fk]['value']}"`:'';
        //if(replaceName[nm]){ nm= replaceName[nm];}
        html+=`<input type="hidden" name="${nm}" ${inputId} ${val} />\n`;
    }
    //console.log(html);
    return html;
};

imgDataForAjax.getFormDataJson=function(frm){ // GET ALL FORM DATA IN JSON FORMAT USING {} AND []
    //frm jquery object
    let s = frm.serializeArray();
    let g = {};
    
    for (let i in s) {
        if ((s[i]['name'].match(/{}/g) || []).length) {
            let splitArr = s[i]['name'].split('{}');
            let add = {};
            add[splitArr['1']]=s[i]['value'];
            if(typeof g[splitArr[0]] === 'undefined'){
                g[splitArr[0]] =add;
            }else{
                //if (typeof g[splitArr[0]][splitArr['1']] === 'undefined') {
                //    g[splitArr[0]][splitArr['1']] = {};
                //}
                g[splitArr[0]][splitArr['1']] =s[i]['value'];
            }

        }
        else if ((s[i]['name'].match(/\[\]/g) || []).length) {
        let splitArr = s[i]['name'].split('[]');
        if (typeof g[splitArr[0]] === 'undefined') {
            g[splitArr[0]] = [];
        }
        let add={};
        add[splitArr['1']]=s[i]['value'];
        g[splitArr[0]].push(add);
        }
        else {
            g[s[i]['name']] = s[i]['value'];
        }
}
    //console.log(g);
    return g;

};
  
imgDataForAjax.getData = function (options, isIpRqd) {
    let imgflds = options.setImgFldsJosn;
    let formId=options.formId;
    let formObj = $('#'+formId);
    let form = document.getElementById(formId);
    let data = new FormData(form);
    //let isIpRqd = $('#inputRequiredCheckboxNewDev')[0].checked;
    for (let i in imgflds) {
        for (let f in formObj[0]) {
            if(formObj[0][f]){
                if (formObj[0][f].name == imgflds[i]['frmElmntAs']) {
                    //console.log('files-',formObj[0][f].files[0]);
                    if (!isIpRqd && imgflds[i]['sendAs'] =="infile")
                        data.append(imgflds[i]['sendAs'], '');
                    else
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
    let progressId=opt.progressBarId;
    let sendData = new FormData();
    sendData = opt.frmData;
            
    let reqPromise=$.isf.ajax({
        type: "POST",
        enctype: "multipart/form-data",
        url: url,
        returnAjaxObj: true, 
        data: sendData,
        processData: false,
        contentType: false,
        cache: false,
        crossDomain:true,
        timeout: 600000,
        // Custom XMLHttpRequest
        xhr: function() {
            var myXhr = $.ajaxSettings.xhr();
            if (myXhr.upload) {
                // For handling the progress of the upload
                myXhr.upload.addEventListener('progress', function(e) {
                    if (e.lengthComputable) {
                        let prog='#'+progressId;
                        if($(prog)){
                            $(prog).attr({
                                value: e.loaded,
                                max: e.total,
                            });
                        }
                    }
                } , false);
            }
            return myXhr;
        }
    });

    reqPromise.done(opt.onDone);
    reqPromise.fail(function(){alert("Image upload error -"+url);});
    reqPromise.always(function(){ pwIsf.removeLayer();});

};
//END - A COMMON FUNCTION FOR IMAGE UPLOAD

let globFlChartJsonArr = [];

function sendNewBotRequest() { // SEND REQUEST FOR NEW BOT
    pwIsf.addLayer({text:'Uploading files please wait ...',progressId:'reqNewfileProgress',showSpin:false})
    var wfownername = localStorage.getItem('wfOwnername');
    var isInputRqdCheckedNewDev = $('#inputRequiredCheckboxNewDev')[0].checked;
    let restData={};
    restData=imgDataForAjax.getFormDataJson($('#raisNewBotFrm'));

    restData['tblRpaBotrequirements']=[{
        "isDataFetching":1,
        "isReportPrepration":1,
        "outputFormat":"Excel",
        "createdBy":"ESSRMMA",
        "isActive":1,
        "tblRpaRequestTools":
            [{
                "toolId":4,
                "toolName":"OSS",
                "isPasswordRequired":1,
                "isMobilePassRequired":1,
                "isVpnrequired":0,
                "createdBy":"ESSRMMA",
                "isActive":1
            },
            {
                "toolId":5,
                "toolName":"ITK",
                "isPasswordRequired":1,
                "isMobilePassRequired":1,
                "isVpnrequired":0,
                "createdBy":"ESAABEH",
                "isActive":1
            }
            ]
    }];

    restData['tblRpaRequestStr']['tblProjects'] = restData['tblProjects'];
    restData['tblRpaRequestStr']['tblRpaBotrequirements'] = restData['tblRpaBotrequirements'];  
    let opt = {};
    opt.setImgFldsJosn = [{ sendAs: 'infile', frmElmntAs: 'infile' }, { sendAs: 'outfile', frmElmntAs: 'outfile' }, { sendAs: 'logicfile', frmElmntAs: 'logicFile' }];
    opt.formId = 'raisNewBotFrm';
    let form = document.getElementById(opt.formId);
    let frmData = new FormData(form); 
    frmData = imgDataForAjax.getData(opt, isInputRqdCheckedNewDev);
    frmData.append('tblRpaRequestStr', JSON.stringify(restData['tblRpaRequestStr'])); // for extra params
    frmData.append('isInputRequired', isInputRqdCheckedNewDev);
    frmData.append('wfOwner', wfownername); 
    let reqOpt = {};
    reqOpt.url = service_java_URL_VM + "botStore/createNewRequest/";
    reqOpt.progressBarId='reqNewfileProgress';
    reqOpt.frmData = frmData;
    reqOpt.onDone = function (data) {
        if(data.apiSuccess){
            pwIsf.alert({msg:'Saved<br>'+data.responseMsg,type:'success'});
            $("#modalBotStore").modal('hide');
        }else{
            pwIsf.alert({msg:'Error-<br>'+data.responseMsg,type:'error'});
        }
    };
                        
    imgDataForAjax.sendRequest(reqOpt);

}
// SEND REQUEST FOR NEW macro BOT
function newBotRequest() {
    pwIsf.addLayer({ text: 'Uploading files please wait ...', progressId: 'reqNewBotProgress', showSpin: false })
    var wfownername = localStorage.getItem('wfOwnername');
    var isInputRqdCheckedMacro = $('#inputRequiredCheckboxOwnDev-macroDiv')[0].checked;

    let restData = {};
    restData = imgDataForAjax.getFormDataJson($('#newBotMacroFrm'));

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

    restData['tblRpaRequestStr']['tblRpaBotrequirements'] = restData['tblRpaBotrequirements'];
    restData['tblRpaRequestStr']['tblProjects'] = restData['tblProjects'];
    restData['tblRpaRequestStr']['currentAvgExecutiontime'] = restData['tblRpaBotstagingStr']['rpaexecutionTime'];
    restData['tblRpaRequestStr']['requestName'] = restData['tblRpaBotstagingStr']['botname'];

    let opt = {};
    opt.setImgFldsJosn = [{ sendAs: 'infile', frmElmntAs: 'inFile' }, { sendAs: 'outfile', frmElmntAs: 'outFile' }, { sendAs: 'logicfile', frmElmntAs: 'logicFile' }, { sendAs: 'codefile', frmElmntAs: 'codeFile' }];
    opt.formId = 'newBotMacroFrm';

    let form = document.getElementById(opt.formId);
    let frmData = new FormData(form);
    frmData = imgDataForAjax.getData(opt, isInputRqdCheckedMacro);
    frmData.append('tblRpaRequestStr', JSON.stringify(restData['tblRpaRequestStr'])); // for extra params
    frmData.append('tblRpaBotstagingStr', JSON.stringify(restData['tblRpaBotstagingStr']));
    frmData.append('isInputRequired', isInputRqdCheckedMacro);
    frmData.append('wfOwner', wfownername);

    let reqOpt = {};
    reqOpt.url = service_java_URL_VM + "botStore/createBotRequest/";
    reqOpt.progressBarId = 'reqNewBotProgress';
    reqOpt.frmData = frmData;
    reqOpt.onDone = function (data) {
        if (data.apiSuccess) {
            pwIsf.alert({ msg: 'Saved<br>' + data.responseMsg, type: 'success' });
            $('#testBtnNewBotRequestMacro').attr({ 'data-details': JSON.stringify({ reqId: data.data, signum: signumGlobal }), 'disabled': false });
            $('#stopTestBtnBotRequestMacro').attr({ 'data-details': JSON.stringify({ reqId: data.data, signum: signumGlobal }), 'disabled': false });
        } else {
            pwIsf.alert({ msg: 'Error-<br>' + data.responseMsg, type: 'error' });
        }
    };

    imgDataForAjax.sendRequest(reqOpt);

}

function newBotRequestJava() { // SEND REQUEST FOR NEW java BOT
    pwIsf.addLayer({ text: 'Uploading files please wait ...', progressId: 'reqNewBotProgress', showSpin: false })
    var wfownername = localStorage.getItem('wfOwnername');
    var isInputRqdCheckedJava = $('#inputRequiredCheckboxOwnDev-javaDiv')[0].checked;
    let restData = {};
    restData = imgDataForAjax.getFormDataJson($('#newBotJavaFrm'));

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

    restData['tblRpaRequestStr']['tblRpaBotrequirements'] = restData['tblRpaBotrequirements'];
    restData['tblRpaRequestStr']['tblProjects'] = restData['tblProjects'];
    restData['tblRpaRequestStr']['currentAvgExecutiontime'] = restData['tblRpaBotstagingStr']['rpaexecutionTime'];
    restData['tblRpaRequestStr']['requestName'] = restData['tblRpaBotstagingStr']['botname'];

    let opt = {};
    opt.setImgFldsJosn = [{ sendAs: 'infile', frmElmntAs: 'infile' }, { sendAs: 'outfile', frmElmntAs: 'outfile' }, { sendAs: 'logicfile', frmElmntAs: 'logicFile' }, { sendAs: 'codefile', frmElmntAs: 'codeFile' }, { sendAs: 'exefile', frmElmntAs: 'exefile' }];
    opt.formId = 'newBotJavaFrm';

    let form = document.getElementById(opt.formId);
    let frmData = new FormData(form);
    frmData = imgDataForAjax.getData(opt, isInputRqdCheckedJava);
    frmData.append('tblRpaRequestStr', JSON.stringify(restData['tblRpaRequestStr'])); // for extra params
    frmData.append('tblRpaBotstagingStr', JSON.stringify(restData['tblRpaBotstagingStr']));
    frmData.append('isInputRequired', isInputRqdCheckedJava);
    frmData.append('wfOwner', wfownername);

    let reqOpt = {};
    reqOpt.url = service_java_URL_VM + "botStore/createBotRequestForJavaPython/";
    reqOpt.progressBarId = 'reqNewBotProgress';
    reqOpt.frmData = frmData;
    reqOpt.onDone = function (data) {
        if (data.apiSuccess) {
            pwIsf.alert({ msg: 'Saved<br>' + data.responseMsg, type: 'success' });
            $('#testBtnNewBotRequestJava').attr({ 'data-details': JSON.stringify({ reqId: data.data, signum: signumGlobal }), 'disabled': false });
            $('#stopTestBtnBotRequestJava').attr({ 'data-details': JSON.stringify({ reqId: data.data, signum: signumGlobal }), 'disabled': false });
        } else {
            pwIsf.alert({ msg: 'Error-<br>' + data.responseMsg, type: 'error' });
        }
    };

    imgDataForAjax.sendRequest(reqOpt);
}

function newBotRequestPython() { // SEND REQUEST FOR NEW python BOT

    let isContinue = ValidateForm();

    if (isContinue) {

        pwIsf.addLayer({ text: 'Uploading files please wait ...', progressId: 'reqPythonProgress', showSpin: false })
        var wfownername = localStorage.getItem('wfOwnername');
        var isInputRqdCheckedPython = $('#inputRequiredCheckboxOwnDev-pythonDiv')[0].checked;
        let restData = {};
        restData = imgDataForAjax.getFormDataJson($('#newBotPythonFrm'));

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

        restData['tblRpaRequestStr']['tblRpaBotrequirements'] = restData['tblRpaBotrequirements'];
        restData['tblRpaRequestStr']['tblProjects'] = restData['tblProjects'];
        restData['tblRpaRequestStr']['currentAvgExecutiontime'] = restData['tblRpaBotstagingStr']['rpaexecutionTime'];
        restData['tblRpaRequestStr']['requestName'] = restData['tblRpaBotstagingStr']['botname'];

        restData['tblRpaBotstagingStr']['languageBaseVersionID'] = $("#selectVersion").val();
        restData['tblRpaBotstagingStr']['moduleClassName'] = restData.packageName;

        let opt = {};
        opt.setImgFldsJosn = [{ sendAs: 'infile', frmElmntAs: 'infile' }, { sendAs: 'outfile', frmElmntAs: 'outfile' }, { sendAs: 'logicfile', frmElmntAs: 'logicFile' }, { sendAs: 'codefile', frmElmntAs: 'codeFile' }, { sendAs: 'whlfile', frmElmntAs: 'whlfile' }];
        opt.formId = 'newBotPythonFrm';

        let form = document.getElementById(opt.formId);
        let frmData = new FormData(form);
        frmData = imgDataForAjax.getData(opt, isInputRqdCheckedPython);
        frmData.append('tblRpaRequestStr', JSON.stringify(restData['tblRpaRequestStr'])); // for extra params
        frmData.append('tblRpaBotstagingStr', JSON.stringify(restData['tblRpaBotstagingStr']));
        frmData.append('isInputRequired', isInputRqdCheckedPython);
        frmData.append('wfOwner', wfownername);

        let reqOpt = {};
        reqOpt.url = service_java_URL_VM + "botStore/createBotRequestForJavaPython/";
        reqOpt.progressBarId = 'reqPythonProgress';
        reqOpt.frmData = frmData;
        reqOpt.onDone = function (data) {
            if (data.apiSuccess) {
                pwIsf.alert({ msg: 'Saved<br>' + data.responseMsg, type: 'success' });
                $('#testBtnNewBotRequestPython').attr({ 'data-details': JSON.stringify({ reqId: data.data, signum: signumGlobal }), 'disabled': false });
                $('#stopTestBtnBotRequestPython').attr({ 'data-details': JSON.stringify({ reqId: data.data, signum: signumGlobal }), 'disabled': false });
            } else {
                pwIsf.alert({ msg: 'Error-<br>' + data.responseMsg, type: 'error' });
            }
        };

        imgDataForAjax.sendRequest(reqOpt);
    }
}

function ValidateForm() {
    let pythonVersion = $('#selectVersion').find(":selected").text();
    let whlFileName = document.getElementById("whlInputFile").files[0].name.split('.whl')[0];
    let whlFileContentArr = whlFileName.split('-');
    let isWhlFileFormatInValid = (whlFileContentArr.length - 1) < 4;
    let packageNameFromWhlFile = isWhlFileFormatInValid ? "" : whlFileContentArr.reverse().slice(4).reverse().join('-');
    let isContinue = true;

    if (pythonVersion == "-1") {
        pwIsf.alert({ msg: 'Warning:  ' + "Select Python Base Version.", type: 'warning' });
        isContinue = false;
    }

    if (!isWhlFileFormatInValid) {
        let packageName = $("#packageName").val();

        if (packageNameFromWhlFile != packageName) {
            pwIsf.alert({ msg: 'Warning:  ' + "WHL file's name should match with package Name", type: 'warning' });
            isContinue = false;
        }
        else {
            whlFileContentArr.reverse();
            let versionFromFile = whlFileContentArr[whlFileContentArr.length - 3];
            let versionArray = pythonVersion.split(".");
            let versionToCompare = "py" + versionArray[0] + versionArray[1];

            if (isContinue && versionToCompare != versionFromFile) {
                pwIsf.alert({ msg: 'Warning:  ' + "WHL file's Version should match with Python Base Version", type: 'warning' });
                isContinue = false;
            }
        }
    }
    else {
        pwIsf.alert({ msg: 'Warning:  ' + "WHL file's name is not in correct format", type: 'warning' });
        isContinue = false;
    }
    return isContinue;
}

function newBotRequestDotNet() { // SEND REQUEST FOR NEW DotNet BOT
    pwIsf.addLayer({ text: 'Uploading files please wait ...', progressId: 'reqDotNetProgress', showSpin: false })
    var wfownername = localStorage.getItem('wfOwnername');
    var isInputRqdCheckedDotNet = $('#inputRequiredCheckboxOwnDev-dotNetDiv')[0].checked;
    let restData = {};
    restData = imgDataForAjax.getFormDataJson($('#newBotDotNetFrm'));

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

    restData['tblRpaRequestStr']['tblRpaBotrequirements'] = restData['tblRpaBotrequirements'];
    restData['tblRpaRequestStr']['tblProjects'] = restData['tblProjects'];
    restData['tblRpaRequestStr']['currentAvgExecutiontime'] = restData['tblRpaBotstagingStr']['rpaexecutionTime'];
    restData['tblRpaRequestStr']['requestName'] = restData['tblRpaBotstagingStr']['botname'];

    //console.log(restData);
    //return;
    let opt = {};
    opt.setImgFldsJosn = [{ sendAs: 'infile', frmElmntAs: 'infile' }, { sendAs: 'outfile', frmElmntAs: 'outfile' }, { sendAs: 'logicfile', frmElmntAs: 'logicFile' }, { sendAs: 'codefile', frmElmntAs: 'codeFile' }, { sendAs: 'exefile', frmElmntAs: 'exefile' }];
    opt.formId = 'newBotDotNetFrm';

    let form = document.getElementById(opt.formId);
    let frmData = new FormData(form);
    frmData = imgDataForAjax.getData(opt, isInputRqdCheckedDotNet);
    frmData.append('tblRpaRequestStr', JSON.stringify(restData['tblRpaRequestStr'])); // for extra params
    frmData.append('tblRpaBotstagingStr', JSON.stringify(restData['tblRpaBotstagingStr']));
    frmData.append('isInputRequired', isInputRqdCheckedDotNet);
    frmData.append('wfOwner', wfownername); 

    let reqOpt = {};
    reqOpt.url = service_java_URL_VM + "botStore/createBotRequestForJavaPython/";
    reqOpt.progressBarId = 'reqDotNetProgress';
    reqOpt.frmData = frmData;
    reqOpt.onDone = function (data) {
        if (data.apiSuccess) {
            pwIsf.alert({ msg: 'Saved<br>' + data.responseMsg, type: 'success' });
            $('#testBtnNewBotRequestDotNet').attr({ 'data-details': JSON.stringify({ reqId: data.data, signum: signumGlobal }), 'disabled': false });
            $('#stopTestBtnBotRequestDotNet').attr({ 'data-details': JSON.stringify({ reqId: data.data, signum: signumGlobal }), 'disabled': false });
        } else {
            pwIsf.alert({ msg: 'Error-<br>' + data.responseMsg, type: 'error' });
        }
    };

    imgDataForAjax.sendRequest(reqOpt);

}



function assessedOrExperiencedOnViewFl(iframeSrc, iframeId) {

    var checkbox = document.querySelector('input[id="togBtnAssessedOrExperiencedOnViewFl"]');
    let allGraphJson = app.graph.toJSON();
    if (checkbox.checked) {
        let cells=allGraphJson.cells;
        cells.forEach(function (el) {
            if (el.type == 'ericsson.Manual' && el.viewMode == 'Assessed' ) {
                el.attrs.header.stroke = el.attrs.header.fill = "#9D9D9D";
                el.attrs.footer.stroke = el.attrs.footer.fill = "#9D9D9D";
                el.attrs.body.stroke = "#9D9D9D";
			}
        });
        app.graph.fromJSON({ cells: cells});
        subActivityDefID= $('#experiencedHiddenVal').val();

    } else {
        allGraphJson.cells.forEach(function (el) {
            if (el.type == 'ericsson.Manual') {
                el.attrs.header.stroke = el.attrs.header.fill = "#31d0c6";
                el.attrs.footer.stroke = el.attrs.footer.fill = "#31d0c6";
                el.attrs.body.stroke = "#31d0c6";
            }
        });
        app.graph.fromJSON(allGraphJson);
        subActivityDefID= $('#assessedHiddenVal').val();
    }
    
    
}

function viewFlowChartWorkOrder(projID, subId,version,wfid)
{
    let proficiencyMode =[];
    if (wfid == undefined || wfid == null)
        wfid = 0;
    let noFlowchartFoundJson = '{ "cells": [{ "size": { "width": 200, "height": 90 }, "angle": 0, "z": 1, "position": { "x": 225, "y": 230 }, "type": "basic.Rect", "attrs": { "rect": { "rx": 2, "ry": 2, "width": 50, "stroke-dasharray": "0", "stroke-width": 2, "fill": "#dcd7d7", "stroke": "#31d0c6", "height": 30 }, "text": { "font-weight": "Bold", "font-size": 11, "font-family": "Roboto Condensed", "text": "No FlowChart Available", "stroke-width": 0, "fill": "#222138" }, ".": { "data-tooltip-position-selector": ".joint-stencil", "data-tooltip-position": "left" } } }] }';
    $.isf.ajax({
        url: service_java_URL + "flowchartController/viewFlowChartForSubActivity/" + projID + "/" + subId + "/0/" + version + "/" + experiencedMode + "/" + wfid,
        success: function (data) {
            
                 
            if (data.length) { 
                    if ('Success' in data[0]) {
                        var changedJson = '';
                        changedJson = JSON.parse(data[0].Success.FlowChartJSON);
                    
                        changedJson.cells.forEach(function (cell) {
                            if (cell.type == 'basic.Rect' || cell.type == 'app.ericssonStep' || cell.type == 'ericsson.Manual') {
                                cell.type = 'ericsson.Manual';
                               
                                cell.name = cell.attrs.bodyText.text.split('\n\n')[0];
                                cell.tool = cell.attrs.task.toolID + '@' + cell.attrs.task.toolName;
                                cell.action = cell.attrs.task.taskID + '@' + cell.attrs.task.taskName;      
                                cell.responsible = (cell.responsible) ? cell.responsible:"";
                                delete cell.attrs.rect;
                                delete cell.attrs.text;

                            }
                            if (cell.type == 'erd.WeakEntity' || cell.type == 'app.ericssonWeakEntity' || cell.type == 'ericsson.Automatic') {
                                cell.type = 'ericsson.Automatic';
                            
                                cell.name = cell.attrs.bodyText.text.split('\n\n')[0];
                                cell.execType = "Automatic";
                                cell.tool = cell.attrs.task.toolID + '@' + cell.attrs.task.toolName;
                                cell.action = cell.attrs.task.taskID + '@' + cell.attrs.task.taskName;
                                cell.rpaid = cell.attrs.tool.RPAID + '@' + cell.attrs.tool.RPAID+'-'+cell.attrs.tool.RPAName;
                                cell.responsible = (cell.responsible) ? cell.responsible : "";
                                delete cell.attrs.rect; delete cell.attrs.text;
           
                            }
                            
                            
                        });
                       
                        if (data[0].Success.Mode.indexOf(',') > -1) {
                            proficiencyMode = data[0].Success.Mode.split(',');
                        } else {
                            proficiencyMode.push(data[0].Success.Mode);
                        }
                        
                            $('#WFName').text(data[0].Success.WorkFlowName);
                            $('#projectId').text(projID+",");
                            subActivityDefID = data[0].Success.SubActivityFlowChartDefID;
                        $('#assessedHiddenVal').val(data[0].Success.SubActivityFlowChartDefID);
                        $('#experiencedHiddenVal').val($('#assessedHiddenVal').val());
                        //if (proficiencyMode[k].toLowerCase() === 'assessed') { 
                           
                        //    $('#WFName').text(data[0].Success.WorkFlowName);
                        //    $('#projectId').text(projID+",");
                        //    subActivityDefID = data[0].Success.SubActivityFlowChartDefID;
                        //    $('#assessedHiddenVal').val(data[0].Success.SubActivityFlowChartDefID);
                        //}
                        //else{
                          
                        //    $('#experiencedHiddenVal').val(data[0].Success.SubActivityFlowChartDefID);
                        //}
                        

                    } else {
                        app.graph.fromJSON(JSON.parse(data[0].Failed));
                        app.layoutDirectedGraph();
                    }
                
            } else {
                app.graph.fromJSON(JSON.parse(noFlowchartFoundJson));
                app.layoutDirectedGraph();
            }
            let experiencedHtml = `<label class="switchSource" ><input type="checkbox"  class="toggleActive" onclick="assessedOrExperiencedOnViewFl()" id="togBtnAssessedOrExperiencedOnViewFl" ><div class="sliderSource round "><span class="onSource">Experienced</span><span class="offSource">Assessed</span></div></label>`;
          
            if (proficiencyMode.length == 2 && experiencedMode != 0) {
                $('#assessedOrExperiencedDiv').show().html(experiencedHtml);
            }
            app.graph.fromJSON(changedJson);
            //if (proficiencyMode[0] == 'Assessed') {
            //    let assessedJson = (globFlChartJsonArr.filter(x => x.mode.toLowerCase() === 'assessed'))[0].flowchartjson;
                
            //   // app.layoutDirectedGraph();
            //}

            
        },
        error: function (xhr, status, statusText) {
            var err = JSON.parse(xhr.responseText);
           // console.log(err.errorMessage);
            
        }
    });
}

$("#toolView").change(function () {
   // alert($('option:selected', this).val());
    $('#toolIDView').val($('option:selected', this).val());

});

$("#toolView").change(function () {
   // alert($('option:selected', this).val());
    $('#toolIDView').val($('option:selected', this).val());

});

function getRPAId(projectId, scopeRPA) {
    $.isf.ajax({
        url: service_java_URL + "rpaController/getRPADeployedDetails/" + projectId,
        async:false,
        success: function (data) {
            var html = "";
            $('#rpaIds').html('');
            $('#rpaIds').append('<option disabled selected value> -- select an option -- </option>');
          
            $.each(data, function (i, d) {
                if (d !== null)
                    $('#rpaIds').append('<option value="' + d.bOTName + '">' + d.automation_Plugin + '</option>');
              
               //  a.push(d.bOTName);
                
            })
          
            var exists = 0 != $('#rpaIds option[value=' + scopeRPA + ']').length;
            if (!exists) { $('#rpaIds').append('<option value="' + scopeRPA + '">' + scopeRPA + '</option>'); }

        },
        complete: function () {
               $('#rpaIds').find("option").eq(0).remove();
          
        },
        error: function (xhr, status, statusText) {
            alert("Failed to Get RPA Id ");
        }
    });
}

function funcRpa1(currentShape, executionType, avgEstdEffort, taskId, taskName, rpaId, stepID, version, tool_ID, tool_Name,extraParams={}) {
    displayNoteForMacroBots();
    var wfOwner = localStorage.getItem('wfOwnername');
    getAllBotDetails(taskId);
    getDevRequestDetails(signumGlobal);
    let comingFromWOInprogressPage=false;
    if(Object.keys(extraParams).length){ // Means coming from workOrderAndTask page
        
        comingFromWOInprogressPage=true;
        subActivityDefID=extraParams.subActDefId;
        var subID = extraParams.subActId;
        var projId = extraParams.projId;
    }else{
        var urlParams = new URLSearchParams(window.location.search);
        var subID = urlParams.get("subId");
        var projId = urlParams.get("projID");
    }


    if(comingFromWOInprogressPage){
        $('#projectData').hide();
        $('#mainTabs li a[href="#tabNewRequest"').hide();
    }else{
        $('#mainTabs li a[href="#tabNewRequest"').show();
    }
    //if ((currentShape == "basic.Rect") || (currentShape == "erd.WeakEntity")) {
    //    $(".inspector-container").find('[data-attribute="attrs/text/taskName"]').val(taskName);
    //    $(".inspector-container").find('[data-attribute="attrs/executiontype"]').val(executionType);
    //    $(".inspector-container").find('[data-attribute="attrs/avgEstdTime"]').val(avgEstdEffort);
    //}
    
    $('#toolView').html('');
    pwIsf.addLayer({ text: "Please wait ..." });
    //$.ajax({
    //    url: service_java_URL + "toolInventory/getToolInventoryDetails",
    //    async: false,
    //    success: function (data) {
    //        var option = '<option disabled selected value> -- select an option -- </option>';
    //        for (var i = 0; i < data.length; i++) {
    //            option += '<option value="' + data[i].toolID + '">' + data[i].tool + '</option>';
    //        }          
    //     //   console.log((tool_ID));
    //        $('#toolView').append(option);
    //        $("#toolView").val(tool_ID);
    //       // console.log($("#toolView").val(tool_ID));
    //    }
       
    //});
 
    $.isf.ajax({
        url: service_java_URL + "flowchartController/getTaskDetailsForJSONStep/" + projId + '/' + subID + '/' + taskId + '/' + version + '/' + stepID + '/' + subActivityDefID,
        success: function (data) {
            var task = '';
            var avg = '';
            var exe = '';
            var tool = '';
            var rpa = '';
            var scopeID = '';
            var projectID = '';
            var subActivityID = '';
            var taskID = '';
            var toolID = '';
            var rpaID = '';
            var taskCompId = '';
            var rpaId = '';

            var scopeTask = '';
            var scopeAvg = '';
            var scopeExe = '';
            var scopeTool = '';
            var scopeToolID = '';
            var scopeRpa = '';

            //console.log('flowchart data',data);
            if (data.scopeTaskMapping != null) {
                task = data.masterTaskModels.task;
                scopeTask = data.scopeTaskMapping.task;

                avg = data.masterTaskModels.avgEstdEffort;
                scopeAvg = data.scopeTaskMapping.avgEstdEffort;

                exe = data.masterTaskModels.executionType;
                scopeExe = data.scopeTaskMapping.executionType;


                rpa = data.masterTaskModels.rpaID;
                scopeRpa = data.scopeTaskMapping.rpaID;

                //getRPAId(projId, scopeRpa);

                if (data.masterTaskModels.tools.length != 0)
                    tool = data.masterTaskModels.tools[0].tool;

                scopeTool = data.scopeTaskMapping.tool;
                scopeToolID = data.scopeTaskMapping.toolID;

                scopeID = data.scopeTaskMapping.scopeID;
                projectID = data.scopeTaskMapping.projectID;
                subActivityID = data.scopeTaskMapping.subActivityID;
                taskID = data.scopeTaskMapping.taskID;
                // toolID = data.toolID;
                rpaID = data.scopeTaskMapping.rpaID;
                taskCompId = taskID;
                rpaCompId = rpaID;

                $('#taskTextpView').html('');
                $('#avgTextpView').html('');
                $('#exeTextpView').html('');
                $('#toolTextpView').html('');
                $('#RpaTextpView').html('');

                $('#taskTextpView').append(task);
                $('#taskView').val(scopeTask);

                $('#avgTextpView').append(avg);
                $('#avgView').val(scopeAvg);

                $('#exeTextpView').append(exe);
                $('#exeView').val(scopeExe);

                $('#toolTextpView').append(tool);
                $("#toolView option:contains(" + scopeTool + ")").attr('selected', 'selected');

                $('#RpaTextpView').append(rpa);
                $('#rpaIds').val(scopeRpa);

                //START- FOR NEW REQUEST FOR MACRO BOT

                let hiddFldJson = [
                    { name: 'tblRpaBotstagingStr{}parallelWoexecution', value: 'NO' },
                    { name: 'tblRpaBotstagingStr{}status', value: 'Assigned' },
                    { name: 'tblRpaBotstagingStr{}isActive', value: '1' },
                    { name: 'tblRpaBotstagingStr{}createdBy', value: signumGlobal },
                    { name: 'tblRpaBotstagingStr{}assignedTo', value: signumGlobal },
                ];

                let html_botMacro = imgDataForAjax.createHiddenFlds(hiddFldJson, '');
                $('#botMacroHidden_tblRpaBotstagingStr').html(html_botMacro);
                $('#botJavaHidden_tblRpaBotstagingStr').html(html_botMacro);
                $('#botPythonHidden_tblRpaBotstagingStr').html(html_botMacro);
                $('#botDotNetHidden_tblRpaBotstagingStr').html(html_botMacro);
                $('#botPenguinHidden_tblRpaBotstagingStr').html(html_botMacro);

                hiddFldJson = [
                    { name: 'tblRpaBotstagingStr{}moduleClassName', value: 'Test' },
                    { name: 'tblRpaBotstagingStr{}moduleClassMethod', value: 'Test()' },];
                html_botMacro = imgDataForAjax.createHiddenFlds(hiddFldJson, '');
                $('#botPythonHidden_tblRpaBotstagingStr1').html(html_botMacro);

                hiddFldJson = [
                    { name: 'tblRpaBotstagingStr{}moduleClassName', value: 'Test' },
                    { name: 'tblRpaBotstagingStr{}moduleClassMethod', value: 'Test()' },];
                html_botMacro = imgDataForAjax.createHiddenFlds(hiddFldJson, '');
                $('#botDotNetHidden_tblRpaBotstagingStr1').html(html_botMacro);

                hiddFldJson = [
                    { name: 'tblRpaBotstagingStr{}moduleClassMethod', value: 'Test()' },];
                html_botMacro = imgDataForAjax.createHiddenFlds(hiddFldJson, '');
                $('#botPenguinHidden_tblRpaBotstagingStr1').html(html_botMacro);

                hiddFldJson = [
                    { name: 'tblRpaBotstagingStr{}moduleClassMethod', value: 'Test()' },];
                html_botMacro = imgDataForAjax.createHiddenFlds(hiddFldJson, '');
                $('#botJavaHidden_tblRpaBotstagingStr1').html(html_botMacro);


                hiddFldJson = [
                    { name: 'tblRpaRequestStr{}spocsignum', value: signumGlobal },
                    { name: 'tblProjects{}projectId', value: projId },
                    { name: 'tblRpaRequestStr{}workflowDefid', value: subActivityDefID },
                    { name: 'tblRpaRequestStr{}workflowOwner', value: wfOwner },
                    { name: 'tblRpaRequestStr{}wfstepid', value: stepID },
                    { name: 'tblRpaRequestStr{}subactivityId', value: subID },
                    { name: 'tblRpaRequestStr{}taskId', value: taskID },
                    { name: 'tblRpaRequestStr{}requestStatus', value: 'New' },
                    { name: 'tblRpaRequestStr{}isActive', value: '1' },
                    { name: 'tblRpaRequestStr{}createdBy', value: signumGlobal },
                ];

                html_botMacro = imgDataForAjax.createHiddenFlds(hiddFldJson, '');
                $('#botMacroHidden_tblRpaRequestStr').html(html_botMacro);
                $('#botJavaHidden_tblRpaRequestStr').html(html_botMacro);
                $('#botPythonHidden_tblRpaRequestStr').html(html_botMacro);
                $('#botDotNetHidden_tblRpaRequestStr').html(html_botMacro);
                $('#botPenguinHidden_tblRpaRequestStr').html(html_botMacro);
                //END- FOR NEW REQUEST FOR MACRO BOT
                //START- FOR NEW REQUEST FOR BOT
                $('#newRequestHidden_tblRpaRequestStr').html(html_botMacro);

                //goBackToUpdate();
                if ($.trim(executionType.toLowerCase()) == 'manual') {
                    //$('#goToBotStoreid').show();
                    //goToBotStore();
                    $("#modalBotStore").modal('show');
                } else {
                    return false;
                    //$('#goToBotStoreid').hide();
                }
            }
            else {
                pwIsf.alert({ msg: "Bot Request not allowed on Experienced Workflow.", type: "info" });
            }
        },
        error: function (xhr, status, statusText) {
            alert("failed to fetch data");

        }
    });
    pwIsf.removeLayer();
    //
}

$("button[data-dismiss-modal=modal2]").click(function(){
    $('#botData').modal('hide');
});

$("button[data-dismiss=modal]").click(function(){
    $('#botData').modal('hide');
});

function ViewsaveUpdatedJson(task, avg, exe, tool, scopeID, projectID, subActivityID, taskID, toolID, rpa, rpaID, stepID) {
    var temp;
    if (tool == '' || toolID == '' || toolID == '-1') {
        alert("Please select Tool");
    }
    else {
    for (var i = 0; i < app.graph.attributes.cells.models.length; i++) {

        if (app.graph.attributes.cells.models[i].attributes.type === 'basic.Rect' || app.graph.attributes.cells.models[i].attributes.type === 'erd.WeakEntity' || app.graph.attributes.cells.models[i].attributes.type === 'app.ericssonWeakEntity' || app.graph.attributes.cells.models[i].attributes.type === 'app.ericssonStep') {

            if (app.graph.attributes.cells.models[i].attributes.id === stepID) {

                app.graph.attributes.cells.models[i].attributes.attrs.task.taskName = task;

                app.graph.attributes.cells.models[i].attributes.attrs.task.executionType = exe;

                app.graph.attributes.cells.models[i].attributes.attrs.task.avgEstdEffort = avg;

                //app.graph.attributes.cells.models[i].attributes.attrs.text.text = taskName + "\n\nExecutionType:" + exe + "\n\nAvgEstdEffort:" + avg;

                if (app.graph.attributes.cells.models[i].attributes.type === 'app.ericssonWeakEntity' || app.graph.attributes.cells.models[i].attributes.type === 'app.ericssonStep') {
                    app.graph.attributes.cells.models[i].attributes.avgEstdTime = avg;
                    app.graph.attributes.cells.models[i].attributes.tool = toolID + "@" + tool;

                }

                temp = app.graph.attributes.cells.models[i].attributes.attrs.text.text.split(':');
                temp1 = temp[1].split('\n\n'); temp1[0] = tool;
                temp[1] = temp1[0] + '\n\n' + temp1[1];
                temp[2] = avg;
                
                app.graph.attributes.cells.models[i].attributes.attrs.text.text = temp[0] + ":" + temp[1] + ":" + temp[2];
                app.graph.attributes.cells.models[i].attributes.attrs.task.toolName = tool;
                app.graph.attributes.cells.models[i].attributes.attrs.task.toolID = toolID;

                app.graph.attributes.cells.models[i].attributes.attrs.tool.RPAID = rpaID;

            }
            else {
                if (app.graph.attributes.cells.models[i].attributes.id === stepID) {
                    var temp1 = [];
                    temp1 = app.graph.attributes.cells.models[i].attributes.attrs.text.text.split(':');                    
                    app.graph.attributes.cells.models[i].attributes.attrs.text.text = temp1[0] + ":" + temp1[1] + ":" + avg;
                }

                console.log("will chek in next iteration for task");
            }

        }
        else
            console.log("else block " + app.graph.attributes.cells.models[i].attributes.type);
    }

    var updateProjectDefinedTaskData = { "projectID": projectID, "scopeID": scopeID, "subActivityID": subActivityID, "taskID": taskID, "task": task, "executionType": exe, "avgEstdEffort": avg, "toolID": toolID, "tool": tool, "rpaID": rpaID, "versionNo": version, "flowChartStepID": stepID, "subActivityFlowChartDefID": subActivityDefID };
    
    $.isf.ajax({
        type: "POST",
        contentType: 'application/json',
        data: JSON.stringify(updateProjectDefinedTaskData),
        url: service_java_URL + "flowchartController/updateProjectDefinedTask/",
        success: function () {

            alert("UPDATED FIELDS SUCCESSFULLY SAVED");
        },
        error: function (xhr, status, statusText) {
            alert("UPDATED FIELDS CAN NOT BE SUCCESSFULLY SAVED");
        }
    });


    $.isf.ajax({
        type: "POST",
        contentType: 'application/json',
        data: JSON.stringify(app.graph.toJSON()),
        url: service_java_URL + "flowchartController/saveJSONFromUI/" + projectID + '/' + subActivityID + '/' + subActivityDefID + '/' + signumGlobal +'/'+version + '/' + $("#WFName").text() + '/' + false + '/3'  ,
        success: function (response) {
            if ('Success' in response) {
              //  alert("UPDATED GRAPH SAVED");
                pwIsf.alert({ msg: "UPDATED GRAPH SAVED", type: 'info' });
                location.reload();
            }
            else {
                $('#viewErrorDetials').modal('show');
                $('#dataTableViewErrorDetials_tBody').html('');
                var errorBean = response.Error;
                for (var i = 0; i < errorBean.length; i++) {
                    console.log(errorBean[i].message + ',' + errorBean[i].errorCategory + ',' + errorBean[i].errorDescription + ',' + errorBean[i].details);
                    var tr = '<tr>';

                    tr += '<td>' + errorBean[i].errorCategory + '</td>';

                    tr += '<td>' + errorBean[i].errorDescription + '</td>';

                   // tr += '<td>' + errorBean[i].details + '</td>';

                    tr += '</tr>';

                    $('#dataTableViewErrorDetials').append($(tr));
                }
              //  alert(response.Error.errorCategory);
            }
        },
        error: function (xhr, status, statusText) {
            alert("UPDATED GRAPH CAN NOT BE SUCCESSFULLY SAVED ");
        }
    });
}
}

function ViewsaveJsonfromUI(projID, subID, subDefId, version, WFName, flag, saveOption) {
    var jsonToPass = '';
    var json = '';
    
    jsonToPass = JSON.stringify(app.graph.toJSON());
    $.isf.ajax({
        type: "POST",
        contentType: 'application/json',
        data: jsonToPass,
        url: service_java_URL + "flowchartController/saveJSONFromUI/" + projID + '/' + subID + '/' + subDefId + '/' + signumGlobal + '/' + version + '/' + WFName + '/' + flag + '/' + saveOption,
        success: function () {
            alert("WORK FLOW SUCCESSFULLY SAVED");
            file = null;
         
             json = app.graph.toJSON();
            app.graph.fromJSON(json);
            app.layoutDirectedGraph();
            window.opener.searchWfAvail();
            location.reload();
          
        },
        error: function (xhr, status, statusText) {
            alert(xhr.responseJSON.errorMessage);
            //alert("No new data to be saved JSon already saved");
        }
    });

}

function btnAddStep() {

    var stepName = $("#stepName").val();
    var taskName = $("#taskName  option:selected").text();
    var taskID = $("#taskName  option:selected").val();
    var toolName = $("#toolName  option:selected").text();
    var toolID = $("#toolName option:selected").val();
    var execType = $("#execTypeName  option:selected").text();

    if (execType == "Decision") {
        taskName = null;
        taskID = 0;
        toolName = null;
        toolID = 0;
        
    }
    if (taskName != null) {
        for (var i = 0; i < taskName.length; i++) {
            if (taskName.charAt(i) == "/") {
                // console.log(taskName.charAt(i));
                taskName = taskName.replace(taskName.charAt(i), "_");
            }
        }
    }
    var avgHrs = $("#avgHrs").val();
    if (avgHrs == "") {
        avgHrs = "0"
    }
    var reason = $("#reasonName").val();

    var data = false;
    if (stepName !== "" && reason !== "") {
        $.isf.ajax({
            url: service_java_URL + "flowchartController/createJSONForStep/" + execType + "/0/" + stepName + "/" + taskID + "/" + taskName + "/" + execType + "/" + avgHrs + "/" + toolID + "/" + toolName + "/" + reason,
            success: function (result) {

                data = result;
                $("#jsonval").val(data);

                var currentjson = app.graph.toJSON();
                var datatype = typeof data;
                if (datatype == "string") { data = JSON.parse(data); }

                var jointJSON = $.merge(currentjson.cells, data.cells);
                var finalJSON = { "cells": jointJSON };

                app.graph.fromJSON(finalJSON);
                $("#addEditStep").modal("hide");
            }
        });

        return data;
    }
    else {
        alert("Enter all details");
    }

};

function getTools() {

    $.isf.ajax({
        url: service_java_URL + "/toolInventory/getToolInventoryDetails",
        success: function (result) {

            var option = '';
            for (var i = 0; i < result.length; i++) {
                option += '<option value="' + result[i].toolID + '">' + result[i].tool + '</option>';
            }

            $('#toolName').append(option);
        }
    });
}

function getViewTasks(projectId,subActivityID) {

    $.isf.ajax({
        url: service_java_URL + "/activityMaster/viewTaskDetails/" + projectId + "/" + subActivityID,
        success: function (result) {
            //tasks = result;
            tasks = [];
            var option = '';
            for (var i = 0; i < result.length; i++) {
                option += '<option value="' + result[i].taskID + '">' + result[i].task + '</option>';
                tasks.push({ value: result[i].taskID + '_' + result[i].mastertask, content: '<span style="font-family: Alegreya Sans">' + result[i].task + '</span>' });
            }

           // $('#taskName').append(option);
        }
    });
}

function getTasks(subActivityID) {

    $.isf.ajax({
        url: service_java_URL + "/activityMaster/getTaskDetails/" + subActivityID + "/" + signumGlobal,
        success: function (result) {
          //  tasks = [];
            var option = '';
            for (var i = 0; i < result.length; i++) {
                option += '<option value="' + result[i].taskID + '">' + result[i].task + '</option>';
           //     tasks.push({ value: result[i].taskID+'_'+result[i].task, content: '<span style="font-family: Alegreya Sans">' + result[i].task + '</span>' });
            }

            $('#taskName').append(option);
        }
    });
}

$("#btn_reset").click(function () {

    $("#stepName").val("");
    $("#reasonName").val("");
})
//---------------------------------------------------ISF BOT STROE ---------------------------------------------------------------------------
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
            //$.each(data, function (i, d) {
            //    $("#time_zone").append('<option value="' + d.timeZone + '">' + d.utcOffset + " - " + d.timeZone + '</option>');
            //})
            return data;
            //console.log(data);
        },
        error: function (xhr, status, statusText) {
            console.log('An error occurred');
            //alert('Sorry, an error occurred. Please try reloading the page. \n\nIf that doesn\'t work please send us the following error message, using the contact us page. \n\n' + this.url + ' ' + xhr.status + ' ' + xhr.statusText);

        }
    });
}

function generateConfigIdDTRCA() {
    var urlParams = new URLSearchParams(window.location.search);
    subID = urlParams.get("subId");
    projID = urlParams.get("projID");
    //version = urlParams.get("version");
    domain = urlParams.get("domain");
    //subDomain = urlParams.get("subDomain");
    subServiceArea = urlParams.get("subServiceArea");
    tech = urlParams.get("tech");
    activity = urlParams.get("activity");
    subActivity = urlParams.get("subActivity");
    //scopeName = urlParams.get("scopeName");
    //scopeID = urlParams.get("scopeID");
    //experiencedMode = urlParams.get('experiencedMode');
    var generateId = new Object();
    generateId.defaultZoneId = time_zone_id;
    generateId.defaultZoneValue = time_zone;
    generateId.defaultProfileId = user_profile_id;
    generateId.userSignum = signum;
    generateId.createdBy = signumGlobal;
    generateId.defaultProfileValue = user_profile;
    generateId.lastModifiedBy = signumGlobal;

    $.isf.ajax({
        url: service_java_URL + "accessManagement/addUserPreferences",
        context: this,
        crossdomain: true,
        processData: true,
        contentType: 'application/json',
        type: 'POST',
        data: JSON.stringify(generateId),
        xhrFields: {
            withCredentials: false
        },
        success: function (data) {
            //pwIsf.alert({ msg: "User Preference Saved Sucessfully", type: "success" });
            //$('#userModal').modal('hide');
            //localStorage.setItem("UserTimeZone", time_zone.split("-")[1].trim());
            //clearShiftModal();
            //clearUserPreferenceModal();
            var html = `<a href="http://10.184.56.40:3300/addproject/configID/${data}">Link</a>`;
            $("#configId").val(data);
            $("#dtrcaLink").append(html);
        },
        error: function (xhr, status, statusText) {

        }
    });
}

function showOption(thisObj) {
    let jsonData = $(thisObj).data('details');
    let feasibility = thisObj.value;
    
    $modal1 = $('#approveModal');
    $modal2 = $('#rejectModal');
    if (feasibility === 'approve') {
        $modal1.modal('show');
        //DATA FOR MACRO
        $('#approveModal_rpaRequestId').val(jsonData.rpaRequestId);
        $('#approveModal_assignedTo').val(jsonData.assignedTo);
        $('#approveModal_botname').val(jsonData.botname);
        $('#approveModal_rpaexecutionTime').val(jsonData.rpaexecutionTime);
        $('#approveModal_parallelWoexecution').val(jsonData.parallelWoexecution);
        $('#approveModal_status').val(jsonData.status);
        $('#approvalModal_createdBy').val(jsonData.createdBy);
        $('#approvalModal_isActive').val(jsonData.isActive);
        $('#approveModal_botlanguage').val($('#botLanguage').val());

        //DATA FOR JAVA
        $('#approveRequestJava_frm_rpaRequestId').val(jsonData.rpaRequestId);
        $('#approveRequestJava_frm_assignedTo').val(jsonData.assignedTo);
        $('#approveRequestJava_frm_botname').val(jsonData.botname);
        $('#approveRequestJava_frm_rpaexecutionTime').val(jsonData.rpaexecutionTime);
        $('#approveRequestJava_frm_parallelWoexecution').val(jsonData.parallelWoexecution);
        $('#approveRequestJava_frm_status').val(jsonData.status);
        $('#approveRequestJava_frm_createdBy').val(jsonData.createdBy);
        $('#approveRequestJava_frm_isActive').val(jsonData.isActive);
        $('#approveRequestJava_frm_botlanguage').val($('#botLanguage').val());

        //DATA FOR PYTHON
        $('#approveRequestPython_frm_rpaRequestId').val(jsonData.rpaRequestId);
        $('#approveRequestPython_frm_assignedTo').val(jsonData.assignedTo);
        $('#approveRequestPython_frm_botname').val(jsonData.botname);
        $('#approveRequestPython_frm_rpaexecutionTime').val(jsonData.rpaexecutionTime);
        $('#approveRequestPython_frm_parallelWoexecution').val(jsonData.parallelWoexecution);
        $('#approveRequestPython_frm_status').val(jsonData.status);
        $('#approveRequestPython_frm_createdBy').val(jsonData.createdBy);
        $('#approveRequestPython_frm_isActive').val(jsonData.isActive);
        $('#approveRequestPython_frm_botlanguage').val($('#botLanguage').val());

        //DATA FOR DotNet
        $('#approveRequestDotNet_frm_rpaRequestId').val(jsonData.rpaRequestId);
        $('#approveRequestDotNet_frm_assignedTo').val(jsonData.assignedTo);
        $('#approveRequestDotNet_frm_botname').val(jsonData.botname);
        $('#approveRequestDotNet_frm_rpaexecutionTime').val(jsonData.rpaexecutionTime);
        $('#approveRequestDotNet_frm_parallelWoexecution').val(jsonData.parallelWoexecution);
        $('#approveRequestDotNet_frm_status').val(jsonData.status);
        $('#approveRequestDotNet_frm_createdBy').val(jsonData.createdBy);
        $('#approveRequestDotNet_frm_isActive').val(jsonData.isActive);
        $('#approveRequestDotNet_frm_botlanguage').val($('#botLanguage').val());

       
    }
    if (feasibility === 'reject') {
        $modal2.modal('show');

        $('#rejectModal_rpaRequestId').val(jsonData.rpaRequestId);
        $('#rejectModal_assignedTo').val(jsonData.assignedTo);
      
    }
}

function getAllBotDetails(taskId) {
    //pwIsf.addLayer({ text: "Please wait ..." });
    if ($.fn.dataTable.isDataTable('#tbBotBody')) {
        oTables.destroy();
        $("#tbBotBody").empty();
    }
    $.isf.ajax({
        url: service_java_URL + "rpaController/getBoTsForExplore/" +taskId,
        //url: service_java_URL + "botStore/getBOTsForExplore/" + taskId,
        context: this,
        crossdomain: true,
        processData: true,
        contentType: 'application/json',
        type: 'GET',
        xhrFields: {
            withCredentials: false
        },

        success: function (data) {
            //pwIsf.removeLayer();
            $("#tbBotBody").append($('<tfoot><tr><th></th><th>Rank</th><th>BOT ID</th><th>BOT Name</th><th>Project</th><th>Market Area</th><th>Deployed On</th><th>Bot Execution Count</th><th>Bot Execution Hours</th><th>Bot Execution Fail Count</th><th>Server Status Bot</th></tr></tfoot>'));
           oTables =  $('#tbBotBody').DataTable({
                searching: true,
                responsive: true,
                "pageLength": 10,
                "data": data,
                "destroy": true,
                colReorder: true,
                dom: 'Bfrtip',
                buttons: [
                  'excelHtml5'
                ],
                "columns": [
                    
                   {
                       "title": "BOT Details", "targets": 'no-sort', "orderable": false, "searchable": false, "data": null,
                       "defaultContent": "",
                       "render": function (data, type, row, meta) {
                           
                           var projectName = data.projectName;
                           return '<a title="Click for Bot Details" class="pull-right icon-edit" href="#botData" data-toggle="modal" class="btn code-dialog" onclick="getBotDetails(' + data.rpaRequestID + ',\'' + projectName + '\',\''+data.BOTID+'\')" id="detail"><i class="fa fa-external-link"></i></a>';
                           //return "<a href='#botData' data-toggle='modal' class='btn code-dialog' onclick='getBotDetails(" + data.tblRpaRequest.rpaRequestId + ",\"" + projectName + "\")' id='detail'>Details</a>";
                       }
                   },
                   { "title": "Bot Ranking", "data":null,
                   "render": function(data, type, row, data){
                       if(row._Rank == 1)
                           return row._Rank+"<label>&nbsp;&nbsp;<i data-toggle='tooltip' title='Rank 1 - Activity + SubActivity + Tech + Task Name' class='fa fa-info-circle'></i></label>";
                       else if(row._Rank == 2)
                           return row._Rank+"<label>&nbsp;&nbsp;<i data-toggle='tooltip' title='Rank 2 - SubActivity + Tech + Task Name' class='fa fa-info-circle'></i></label>";
                       else if(row._Rank == 3)
                           return row._Rank+"<label>&nbsp;&nbsp;<i data-toggle='tooltip' title='Rank 3 - Tech + Task Name' class='fa fa-info-circle'></i></label>";
                   }
                   },
                   { "title": "BOT ID", "data": "BOTID" }, { "title": "BOT Name", "data": "BOTName" },
                   { "title": "Project", "data": "projectName" },
                   { "title": "Market Area", "data": "MarketAreaName" },
                   {
                       "title": "Deployed On",
                       "render": function (data, row, type, meta) {
                           var d = new Date(type.createdOn); createdOn = $.format.date(d, "yyyy-MM-dd");
                           return createdOn;
                       }
                       
                   },
                   { "title": "Bot Execution Count", "data": "RPAExecutionTime" },
                   { "title": "Bot Execution Hours", "data": "CurrentAvgExecutionTime" },
                   { "title": "Bot Execution Fail Count", "data": "ReuseFactor" },
                   {
                       "title": "Server Status Bot", "targets": 'no-sort', "orderable": false, "searchable": false, "data": null,
                       "defaultContent": "",
                       "render": function (data, type, row, meta) {
                           var isRunOnServer = data.isRunOnServer;
                           var isActive = data.isActive;
                           var isAuditPass = data.isAuditPass;
                           if (isRunOnServer == 1) {
                               return 'Enabled';
                           }
                           else {
                               return 'Disabled';
                           }
                       }
                   }
               ],
                initComplete: function () {
                    $('#tbBotBody tfoot th').each(function (i) {
                        var title = $('#tbBotBody thead th').eq($(this).index()).text();
                        if (title != "BOT Details")
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
            $('#tbBotBody tfoot').insertAfter($('#tbBotBody thead'));
        },
        
        error: function (xhr, status, statusText) {
            
            console.log('An error occurred');
        }
    });


}

function getBotDetails(rpaRequestId, project,botid) {
    pwIsf.addLayer({text:'Please wait ...'});
    $('.modal-body #botDetails').empty();
        var proj = project;
        $.isf.ajax({
            url: service_java_URL + "botStore/getRPARequestDetails/" + rpaRequestId,
            context: this,
            crossdomain: true,
            processData: true,
            contentType: 'application/json',
            type: 'GET',
            xhrFields: {
                withCredentials: false
            },
            success: function (data) {


                if (!jQuery.isEmptyObject(data)) {
                let getData = {};
                let yesStr='YES',noStr='NO',notFound='-';
                getData.spocsignum = data.spocsignum;
                getData.isDataFetching = (data.tblRpaBotrequirements.length) ? (data.tblRpaBotrequirements[0]) ? (data.tblRpaBotrequirements[0].isDataFetching)?yesStr:noStr : notFound :notFound;
                getData.isReportPrepration = (data.tblRpaBotrequirements.length) ? (data.tblRpaBotrequirements[0]) ? (data.tblRpaBotrequirements[0].isReportPrepration)?yesStr:noStr: notFound : notFound;
               

                getData.isPasswordRequired = (data.tblRpaBotrequirements.length) ? (data.tblRpaBotrequirements[0]) ? (data.tblRpaBotrequirements[0].tblRpaRequestTools[0])?(data.tblRpaBotrequirements[0].tblRpaRequestTools[0].isPasswordRequired)?yesStr:noStr: notFound : notFound : notFound;

                getData.isMobilePassRequired = (data.tblRpaBotrequirements.length) ? (data.tblRpaBotrequirements[0]) ? (data.tblRpaBotrequirements[0].tblRpaRequestTools[0])?(data.tblRpaBotrequirements[0].tblRpaRequestTools[0].isMobilePassRequired)?yesStr:noStr:notFound : notFound : notFound;


                getData.isVpnrequired = (data.tblRpaBotrequirements.length) ? (data.tblRpaBotrequirements[0]) ? (data.tblRpaBotrequirements[0].tblRpaRequestTools[0])?(data.tblRpaBotrequirements[0].tblRpaRequestTools[0].isVpnrequired)?yesStr:noStr :notFound: notFound : notFound;

                getData.currentExecutioncountWeekly = data.currentExecutioncountWeekly;
                getData.currentAvgExecutiontime = data.currentAvgExecutiontime;
                getData.description = data.description;
                getData.outputFormat = (data.tblRpaBotrequirements.length) ? (data.tblRpaBotrequirements[0]) ? data.tblRpaBotrequirements[0].outputFormat : notFound : notFound;
                getData.toolId = (data.tblRpaBotrequirements.length) ? (data.tblRpaBotrequirements[0]) ? data.tblRpaBotrequirements[0].tblRpaRequestTools.toolId : notFound : notFound;
                    getData.toolName = (data.tblRpaBotrequirements.length) ? (data.tblRpaBotrequirements[0]) ? (data.tblRpaBotrequirements[0].tblRpaRequestTools[0]) ? data.tblRpaBotrequirements[0].tblRpaRequestTools[0].toolName : notFound : notFound : notFound;
                    getData.WorkFlowName = data.workFlowName;
                    getData.StepID = data.wfstepid
                    getData.SubactivityName = data.subactivityName;
                    getData.TaskName = data.taskName;
                    getData.isInputRequired = data.isInputRequired;


                    //step ID, step name, WF ID, WF name
                    getData.stepID = data.stepID;
                    getData.stepName = data.stepName;
                    getData.wfID = data.wFID;
                    getData.wfName = data.workFlowName;

                    if (ApiProxy === false) {
                        var newHtml = `<table class="table table-striped">
                        <thead>
                            <tr>
                                <th colspan="4"><h5><b>Project Name:</b> ${proj}</h5></th>

                            </tr>
                        </thead>
                        <tbody>
                            <tr>
                                <td class="heading">Spoc(Signum):</td>
                                <td>${getData.spocsignum}</td>
                                <td class="heading">Sub-Activity Name:</td>
                                <td>${getData.SubactivityName}</td>
                            </tr>
                            <tr>
                                <td class="heading">Current Execution Time(Weekly):</td>
                                <td>${getData.currentExecutioncountWeekly}</td>
                                <td class="heading">Current Execution Time(Average):</td>
                                <td>${getData.currentAvgExecutiontime}</td>
                            </tr>
                            <tr>                               
                                <td class="heading">Task Name:</td>
                                <td>${getData.TaskName}</td>
                                <td class="heading"></td>
                                <td></td>
                            </tr>
                            <tr>
                                <td class="heading">Step ID/Name:</td>
                                <td>${getData.stepID}/${getData.stepName}</td>
                                <td class="heading">WF ID/Name:</td>
                                <td>${getData.wfID}/${getData.wfName}</td>
                            </tr>
                            <tr>
                                <td class="heading">Description:</td>
                                <td colspan="3">${getData.description}</td>
                            </tr>
                            
                            <tr>
                                <div class="col-md-6">
                                    <td class="heading"><label>Input Required :</label></td>
                                    <td colspan="3">
                                    <div style="margin-left:-15px;">
                                        <div class="col-md-1" style="margin: 0px;padding-left: 4px;">
                                            <div class="checkbox-container disabledbutton">
                                                <label class="checkbox-label">
                                                    <input class="inputRequiredCheckboxOwnDev" type="checkbox" onchange="toggleCheckbox(this)">
                                                    <span class="checkbox-custom rectangular"></span>
                                                </label>
                                            </div>
                                        </div>
                                        <div>
                                            <label class="forLabel inputLabel">Bot Input Required</label>
                                        </div>
                                    </div>
                                    </td>
                                </div>                           
                            </tr>
                            <tr>
                                <td class="heading">Downloadable Files:</td>
                                <td colspan="3"><button class=' infile btn btn-default' type="button" title='Input File' onclick="downloadBotFiles({thisObj:this,botId:${rpaRequestId},fileName:'input.zip'});"><i class='fa fa-download' aria-hidden='true'></i> Input</button><button class=' btn btn-default' type="button" title='Output File' onclick="downloadBotFiles({thisObj:this,botId:${rpaRequestId},fileName:'output.zip'});"><i class='fa fa-download' aria-hidden='true'></i> Output</button><button class=' btn btn-default' type="button" title='Code File' onclick="downloadBotFiles({thisObj:this,botId:${rpaRequestId},fileName:'code.zip'});"><i class='fa fa-download' aria-hidden='true'></i> Code</button><button class=' btn btn-default' type="button" title='Logic File' onclick="downloadBotFiles({thisObj:this,botId:${rpaRequestId},fileName:'logic.zip'});"><i class='fa fa-download' aria-hidden='true'></i>Logic</button></td>
                                
                            </tr>
                        </tbody>
                    </table>`;

                    }
                    else {
                        var newHtml = `<table class="table table-striped">
                        <thead>
                            <tr>
                                <th colspan="4"><h5><b>Project Name:</b> ${proj}</h5></th>

                            </tr>
                        </thead>
                        <tbody>
                            <tr>
                                <td class="heading">Spoc(Signum):</td>
                                <td>${getData.spocsignum}</td>
                                <td class="heading">Sub-Activity Name:</td>
                                <td>${getData.SubactivityName}</td>
                            </tr>
                            <tr>
                                <td class="heading">Current Execution Time(Weekly):</td>
                                <td>${getData.currentExecutioncountWeekly}</td>
                                <td class="heading">Current Execution Time(Average):</td>
                                <td>${getData.currentAvgExecutiontime}</td>
                            </tr>
                            <tr>                              
                                <td class="heading">Task Name:</td>
                                <td>${getData.TaskName}</td>
                                <td class="heading"></td>
                                <td></td>
                            </tr>
                            <tr>
                                <td class="heading">Step ID/Name:</td>
                                <td>${getData.stepID}/${getData.stepName}</td>
                                <td class="heading">WF ID/Name:</td>
                                <td>${getData.wfID}/${getData.wfName}</td>
                            </tr>
                            <tr>
                                <td class="heading">Description:</td>
                                <td colspan="3">${getData.description}</td>
                            </tr>
                             </tbody>
                    </table>`;
                    }
                                //$('#requestDetailsInfo').append(newHtml);
                                //$('#requestDetailsInfo').append(html);
                    $('.modal-body #botDetails').append(newHtml);
                    //$('.inputRequiredCheckboxOwnDev').prop("disabled", true);
                    loadCheckbox(getData.isInputRequired);

            }
                let footHtml = `<button type="button" id="testBtnBotRequest" data-details="" class="btn btn-sm btn-warning testNewBotRequest" style="margin-left:10px">TEST BOT</button>
<button type="button" id="stopTestBtnBotRequest" data-details="" class="btn btn-sm btn-danger stopTestBtnBotRequest" style="margin-left:10px">STOP TEST-BOT</button>`;
                $('#botFooter').html('').append(footHtml);
                $('.testNewBotRequest').on('click', function () {


                    //if ($(this).attr('data-details') != '') {
                    pwIsf.addLayer({ text: 'Please wait ...' });

                    //let jsonObj = JSON.parse($(this).attr('data-details'));
                    //let { reqId, signum } = jsonObj;

                    let ajCall = $.isf.ajax({
                        url: service_java_URL + "botStore/createBotTestingRequest/" + botid + "/" + signumGlobal,
                        contentType: 'application/json',
                        returnAjaxObj: true, 
                        type: 'POST'

                    });

                    ajCall.done(function (data) {
                        if (data.apiSuccess) {
                            pwIsf.alert({ msg: 'Testing will be initiated after providing input to ISF Popup. Please ensure ISF Desktop App is running', type: 'success' });
                            window.open('isfalert:test_' + botid, '_self');
                        }
                        else {
                            pwIsf.alert({ msg: '' + data.responseMsg, type: 'warning' });
                        }

                    });
                    ajCall.fail(function () { pwIsf.alert({ msg: 'Error in BOT testing.', type: 'error' }); });
                    ajCall.always(function () { pwIsf.removeLayer(); });

                    //} else {
                    //    console.log('Error : Request id not received.');
                    //}


                });


                $('.stopTestBtnBotRequest').on('click', function () {
                    pwIsf.addLayer({ text: 'Please wait ...' });
                    let ajCall = $.isf.ajax({
                        url: service_java_URL + "botStore/stopInprogressBot/" + botid + "/" + signumGlobal,
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
            },
            error: function (xhr, status, statusText) {
                //console.log("Fail " + xhr.responseText);
                //console.log("status " + xhr.status);
                console.log('An error occurred');
            },
            complete:function(){
                pwIsf.removeLayer();
            }
        });
    
}

function loadCheckbox(isInputRequired) {
    $('.inputRequiredCheckboxOwnDev').attr('checked', isInputRequired);

    if (!isInputRequired) {
        $(".infile").addClass("disabledbutton");
        $(".inputLabel").css("color", '#868598');
        $(".inputLabel").text("Input Not Required");
    }
}


function getDevRequestDetails(signumGlobal) {
    var signum = "essrmma";
    //var signum = signumGlobal;
    if ($.fn.dataTable.isDataTable('#tbReqBody')) {
        oTable.destroy();
        $("#tbReqBody").empty();
    }
    $.isf.ajax({
        url: service_java_URL + "botStore/getAssignedRequestListForDev/" + signum,
        context: this,
        crossdomain: true,
        processData: true,
        contentType: 'application/json',
        type: 'GET',
        xhrFields: {
            withCredentials: false
        },

        success: function (data) {
            $("#tbReqBody").append($('<tfoot><tr><th>Action</th><th>Request Id</th><th>Project Name</th><th>SPOC</th><th>Execution(Weekly)</th><th>Status</th></tr></tfoot>'));
            oTable = $('#tbReqBody').DataTable({
                searching: true,
                responsive: true,
                "pageLength": 10,
                "data": data,
                "destroy": true,
                colReorder: true,
                dom: 'Bfrtip',
                buttons: [
                    'colvis','excelHtml5'
                ],
                "columns": [
                    {
                        "title": "Action", "targets": 'no-sort', "orderable": false, "searchable": false, "data": null,
                        "defaultContent": "",
                        "render": function (data, type, row, meta) {
                            let setData = JSON.stringify({
                                rpaRequestId: data.tblRpaRequest.rpaRequestId, createdBy: data.createdBy, assignedTo: data.assignedTo, isActive: data.isActive, status: data.status, botname: data.botname, rpaexecutionTime: data.rpaexecutionTime, parallelWoexecution: data.parallelWoexecution });

                            return '<select class="form-control" data-details=\'' + setData +'\' onchange="showOption(this)"><option value = ""> Select Feasibility</option ><option value="approve">Approve</option><option value="reject">Reject</option></select >';
                        }
                    },
                    { "title": "Request Id", "data": "tblRpaRequest.rpaRequestId" }, { "title": "Project Name", "data": "tblRpaRequest.tblProjects.projectName" },
                    { "title": "SPOC", "data": "tblRpaRequest.spocsignum" },
                    { "title": "Execution(Weekly)", "data": "tblRpaRequest.currentExecutioncountWeekly" },
                    { "title": "Status", "data": "status" },

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
            //console.log("Fail " + xhr.responseText);
            //console.log("status " + xhr.status);
            console.log('An error occurred');
        }
    });


}

function rejectRequest() {


    pwIsf.addLayer({ text: 'Please wait ...'})


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
        complete:function (xhr, status, statusText) {
            pwIsf.removeLayer();
        }

    });

}

function createTestRequestMacro() {
    pwIsf.addLayer({ text: 'Please wait ...' })
    let restData = {};
    restData = imgDataForAjax.getFormDataJson($('#newBotMacroFrm'));
    let { rpaId, assignedTo } = restData;
    $.isf.ajax({
        url: service_java_URL + "botStore/createBotTestingRequest/" + rpaId + "/" + assignedTo,
        context: this,
        crossdomain: true,
        processData: true,
        contentType: 'application/json',
        type: 'POST',
        xhrFields: {
            withCredentials: false
        },
        success: function (data) {
            console.log('Test Request Raised');
            pwIsf.removeLayer();
        },
        error: function (xhr, status, statusText) {
            console.log('An error occurred on : createProject' + xhr.error);
        }
    });
}

function createTestRequestJava() {
    pwIsf.addLayer({ text: 'Please wait ...' })
    let restData = {};
    restData = imgDataForAjax.getFormDataJson($('#newBotJavaFrm'));
    let { rpaId, assignedTo } = restData;
    $.isf.ajax({
        url: service_java_URL + "botStore/createBotTestingRequest/" + rpaId + "/" + assignedTo,
        context: this,
        crossdomain: true,
        processData: true,
        contentType: 'application/json',
        type: 'POST',
        xhrFields: {
            withCredentials: false
        },
        success: function (data) {
            console.log('Test Request Raised');
            pwIsf.removeLayer();
        },
        error: function (xhr, status, statusText) {
            console.log('An error occurred on : createProject' + xhr.error);
        }
    });
}

function createTestRequestPython() {
    pwIsf.addLayer({ text: 'Please wait ...' })
    let restData = {};
    restData = imgDataForAjax.getFormDataJson($('#newBotPythonFrm'));
    let { rpaId, assignedTo } = restData;
    $.isf.ajax({
        url: service_java_URL + "botStore/createBotTestingRequest/" + rpaId + "/" + assignedTo,
        context: this,
        crossdomain: true,
        processData: true,
        contentType: 'application/json',
        type: 'POST',
        xhrFields: {
            withCredentials: false
        },
        success: function (data) {
            console.log('Test Request Raised');
            pwIsf.removeLayer();
        },
        error: function (xhr, status, statusText) {
            console.log('An error occurred on : createProject' + xhr.error);
        }
    });
}
//Priyanka - DotNet and Penguine Code
function createTestRequestDotNet() {
    pwIsf.addLayer({ text: 'Please wait ...' })
    let restData = {};
    restData = imgDataForAjax.getFormDataJson($('#newBotDotNetFrm'));
    let { rpaId, assignedTo } = restData;
    $.isf.ajax({
        url: service_java_URL + "botStore/createBotTestingRequest/" + rpaId + "/" + assignedTo,
        context: this,
        crossdomain: true,
        processData: true,
        contentType: 'application/json',
        type: 'POST',
        xhrFields: {
            withCredentials: false
        },
        success: function (data) {
            console.log('Test Request Raised');
            pwIsf.removeLayer();
        },
        error: function (xhr, status, statusText) {
            console.log('An error occurred on : createProject' + xhr.error);
        }
    });
}


//Priyanka Penguine an dDotNet code//
function submitBot() {
    pwIsf.addLayer({ text: 'Uploading files please wait ...', progressId: 'approveNewfileProgress', showSpin: false })
    let restData = {};
    restData = imgDataForAjax.getFormDataJson($('#approveRequest_frm'));
    let opt = {};
    opt.setImgFldsJosn = [{ sendAs: 'codefile', frmElmntAs: 'codefile' }];
    opt.formId = 'approveRequest_frm';

    let form = document.getElementById(opt.formId);
    let frmData = new FormData(form);
    frmData = imgDataForAjax.getData(opt);
    frmData.append('TblRpaBotstaging', JSON.stringify(restData)); // for extra params

    //let service_java_URL='http://100.96.33.166:8080/isf-rest-server-java/';

    let reqOpt = {};
    reqOpt.url = service_java_URL_VM + "botStore/submitBotWithLanguageForMacro";
    reqOpt.progressBarId = 'approveNewfileProgress';
    reqOpt.frmData = frmData;
    reqOpt.onDone = function (data) {
        if (data.apiSuccess) {
            pwIsf.alert({ msg: 'Uploaded<br>' + data.responseMsg, type: 'success' });
            createTestRequest();
        } else {
            pwIsf.alert({ msg: 'Error-<br>' + data.responseMsg, type: 'error' });
        }
    };

    imgDataForAjax.sendRequest(reqOpt);

}

function submitBotRequestJava() {  // FOR APPROVE 
    pwIsf.addLayer({ text: 'Uploading files please wait ...', progressId: 'approveJavaProgress', showSpin: false })
    let restData = {};
    restData = imgDataForAjax.getFormDataJson($('#approveRequestJava_frm'));
    let opt = {};
    opt.setImgFldsJosn = [{ sendAs: 'codefile', frmElmntAs: 'codefile' }, { sendAs: 'exeFile', frmElmntAs: 'exeFile' }];
    opt.formId = 'approveRequestJava_frm';

    let form = document.getElementById(opt.formId);
    let frmData = new FormData(form);
    frmData = imgDataForAjax.getData(opt);
    frmData.append('TblRpaBotstaging', JSON.stringify(restData)); // for extra params

    //let service_java_URL ='http://100.96.35.194:8080/isf-rest-server-java/';

    let reqOpt = {};
    reqOpt.url = service_java_URL_VM + "botStore/submitBotWithLanguageForJavaPython";
    reqOpt.progressBarId = 'approveJavaProgress';
    reqOpt.frmData = frmData;
    reqOpt.onDone = function (data) {
        if (data.apiSuccess) {
            pwIsf.alert({ msg: 'Uploaded<br>' + data.responseMsg, type: 'success' });
            createTestRequest();
        } else {
            pwIsf.alert({ msg: 'Error-<br>' + data.responseMsg, type: 'error' });
        }
    };

    imgDataForAjax.sendRequest(reqOpt);
}

function submitBotRequestPython() {
    pwIsf.addLayer({ text: 'Uploading files please wait ...', progressId: 'approvePythonProgress', showSpin: false })
    let restData = {};
    restData = imgDataForAjax.getFormDataJson($('#approveRequestPython_frm'));
    let opt = {};
    opt.setImgFldsJosn = [{ sendAs: 'codefile', frmElmntAs: 'codefile' }, { sendAs: 'exeFile', frmElmntAs: 'exeFile' }];
    opt.formId = 'approveRequestPython_frm';

    let form = document.getElementById(opt.formId);
    let frmData = new FormData(form);
    frmData = imgDataForAjax.getData(opt);
    frmData.append('TblRpaBotstaging', JSON.stringify(restData)); // for extra params

    //let service_java_URL='http://100.96.33.166:8080/isf-rest-server-java/';

    let reqOpt = {};
    reqOpt.url = service_java_URL_VM + "botStore/submitBotWithLanguageForJavaPython";
    reqOpt.progressBarId = 'approvePythonProgress';
    reqOpt.frmData = frmData;
    reqOpt.onDone = function (data) {
        if (data.apiSuccess) {
            pwIsf.alert({ msg: 'Uploaded<br>' + data.responseMsg, type: 'success' });
            createTestRequest();
           
        } else {
            pwIsf.alert({ msg: 'Error-<br>' + data.responseMsg, type: 'error' });
        }
    };

    imgDataForAjax.sendRequest(reqOpt);
}

function submitBotRequestDotNet() {
    pwIsf.addLayer({ text: 'Uploading files please wait ...', progressId: 'approveDotNetProgress', showSpin: false })
    let restData = {};
    restData = imgDataForAjax.getFormDataJson($('#approveRequestDotNet_frm'));
    let opt = {};
    opt.setImgFldsJosn = [{ sendAs: 'codefile', frmElmntAs: 'codefile' }, { sendAs: 'exeFile', frmElmntAs: 'exeFile' }];
    opt.formId = 'approveRequestDotNet_frm';

    let form = document.getElementById(opt.formId);
    let frmData = new FormData(form);
    frmData = imgDataForAjax.getData(opt);
    frmData.append('TblRpaBotstaging', JSON.stringify(restData)); // for extra params

    //let service_java_URL='http://100.96.33.166:8080/isf-rest-server-java/';

    let reqOpt = {};
    reqOpt.url = service_java_URL_VM + "botStore/submitBotWithLanguageForJavaPython";
    reqOpt.progressBarId = 'approveDotNetProgress';
    reqOpt.frmData = frmData;
    reqOpt.onDone = function (data) {
        if (data.apiSuccess) {
            pwIsf.alert({ msg: 'Uploaded<br>' + data.responseMsg, type: 'success' });
            createTestRequest();
           
        } else {
            pwIsf.alert({ msg: 'Error-<br>' + data.responseMsg, type: 'error' });
        }
    };

    imgDataForAjax.sendRequest(reqOpt);
}







