

var botRequestData; var assignRequestID; var versionNo; var auditRequestID; var authWindow = "";
$(document).ready(function () {
    getRequestDetails();
    $('#requestInfo').on('show.bs.modal', function () {
        $(this).find('.modal-content').css({
            width: 'auto', //probably not needed
            height: 'auto', //probably not needed
            'max-width': '50%',
            'margin': 'auto'
        });
    });
    $('#UpdateInfo').on('show.bs.modal', function () {
        $(this).find('.modal-content').css({
            width: 'auto', //probably not needed
            height: 'auto', //probably not needed
            'max-width': '50%',
            'margin': 'auto'
        });
    });
    $('#demoURLPopup').on('show.bs.modal', function () {
        $(this).find('.modal-content').css({
            width: 'auto', //probably not needed
            height: 'auto', //probably not needed
            'max-width': '50%',
            'margin': 'auto'
        });
    });

    $('.testNewBotRequest').on('click', function () {
        if ($(this).attr('data-details') !== '') {
            pwIsf.addLayer({ text: 'Please wait ...' });
            let jsonObj = JSON.parse($(this).attr('data-details'));
            let { reqId, signum } = jsonObj;
            let ajCall = $.isf.ajax({
                url: `${service_java_URL}botStore/createBotTestingRequest/${reqId}/${signum}`,
                contentType: 'application/json',
                type: 'POST'
            });
            ajCall.done(function (data) {
                if (data.apiSuccess) {
                    pwIsf.alert({ msg: 'Testing will be initiated after providing input to ISF Popup. Please ensure ISF Desktop App is running', type: 'success' });
                    window.open(`isfalert:test_${reqId}`, '_self');
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
    //END - TEST FOR NEW BOT
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
            } else {
                pwIsf.alert({ msg: '' + data.responseMsg, type: 'warning' });
            }
        });

        ajCall.fail(function () { pwIsf.alert({ msg: 'Error in BOT testing.', type: 'error' }); });
        ajCall.always(function () { pwIsf.removeLayer(); });

    });
});
function getRequestDetails() {
    if ($.fn.dataTable.isDataTable('#tbBotBody')) {
        oTable.destroy();
        $("#tbReqBody").empty();
    }
    var activeProfileObj = JSON.parse(ActiveProfileSession);
    var roleID = activeProfileObj.roleID;
    pwIsf.addLayer({ text: 'Please wait ...' });

    $.isf.ajax({
        url: `${service_java_URL}botStore/getNewRequestListForDRSP/${signumGlobal}`,
        context: this,
        crossdomain: true,
        processData: true,
        contentType: 'application/json',
        type: 'GET',
        xhrFields: {
            withCredentials: false
        },

        success: function (data) {
            pwIsf.removeLayer();
            botRequestData = data;
            oTable = $('#tbReqBody').DataTable({
                searching: true,
                responsive: true,
                "pageLength": 10,
                "data": botRequestData,
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
                            const jsonData = getJsonObject(data);
                            const jsonForm = getStringfyJsonObject(data);
                            return getTableAction(data, jsonData, jsonForm);
                        }

                    },
                    { "title": "Request Id", "data": "rpaRequestId" },
                    {
                        "title": "Project", "data": null,
                        "render": function (data, type, row, meta) {
                            return data.tblProjects.projectId + "/" + data.tblProjects.projectName;
                        }

                    },
                    {
                        "title": "Subactivity", "data": null,
                        "render": function (data, type, row, meta) {
                            return data.subactivityId + "/" + data.subactivityName;
                        }

                    },
                    { "title": "WorkFlow", "data": "workFlowName" },
                    {
                        "title": "SPOC", "data": null,
                        "defaultContent": "",
                        "render": function (data, type, row, meta) {
                            return '<a href="#" style="color:blue;" onclick="getEmployeeDetailsBySignum(\'' + data.spocsignum + '\')">' + data.spocsignum + '</a>';
                        }

                    },
                    { "title": "Execution(Weekly)", "data": "currentExecutioncountWeekly" },
                    {
                        "title": "Current Status", "data": null,
                        "defaultContent": "",
                        "render": function (data, type, row, meta) {
                            return (data.tblRpaBotstagings[0]) ? data.tblRpaBotstagings[0].status : 'NA';
                        }
                    },
                    { "title": "Request Type", "data": "requestStatus" },
                    {
                        "title": "Bot Language", "data": null,
                        "defaultContent": "",
                        "render": function (data, type, row, meta) {

                            return (data.tblRpaBotstagings[0]) ? data.tblRpaBotstagings[0].botlanguage : 'NA';
                        }
                    },
                    {
                        "title": "Demo Video URL", "data": null,
                        "defaultContent": "",
                        "render": function (data, type, row, meta) {
                            if (data.videoURL == null) {
                                return `<div id="URL_` + data.rpaRequestId + `"></div>`;
                            }
                            else
                                return `<div id="URL_` + data.rpaRequestId + `"><a onclick="window.open('` + data.videoURL + `','_blank')">` + data.videoURL + `</a></div>`;

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

function viewBotDetails(requestID, projectName, currStatus) {
    pwIsf.addLayer({ text: 'Please wait ...' });
    $('#requestInfo').modal('show');
    $('#requestDetailsInfo').empty();
    var proj = projectName;

    $.isf.ajax({
        url: service_java_URL + "rpaController/getAllRPARequestDetails/" + requestID,
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
                let yesStr = 'YES', noStr = 'NO', notFound = '-';
                getData.spocsignum = data[0].SPOC;
                getData.proj = data[0].ProjectName;
                getData.botexecutioncount = data[0].BOTExecutedCount;
                getData.botexecutionhours = data[0].BOTExecutionHours;
                getData.description = data[0].Description;
                getData.tooltype = data[0].ToolType;
                getData.activity = data[0].Activity;
                getData.accessmethod = data[0].AccessMethod;
                getData.domain = data[0].Domain;
                getData.eme = data[0].EME;
                getData.BotExecutionCount = data[0].BotExecutionCount;
                getData.BotReuseCount = data[0].BotReuseCount;
                getData.botId = data[0].BOTID;
                getData.botName = data[0].BOTName;
                getData.tech = data[0].Technology;
                getData.WorkFlowName = data.workFlowName;
                getData.SubactivityName = data[0].SubActivity;
                getData.TaskName = data[0].TaskName;
                getData.toolNames = data[0].ToolName;
                getData.url = data[0].VideoURL;
                getData.isInputRequired = data[0].IsInputRequired;
                // language version id
                getData.LanguageBaseVersionID = data[0].LanguageBaseVersionID;
                getData.LanguageBaseVersion = data[0].LanguageBaseVersion === null ? 'NA' : data[0].LanguageBaseVersion;


                $('.testNewBotRequest').attr('data-details', JSON.stringify({ reqId: requestID, signum: signumGlobal }));
                $('.stopTestBtnBotRequest').attr('data-details', JSON.stringify({ reqId: requestID, signum: signumGlobal }));

                // step ID, step name, WF ID and WF name
                getData.stepID = data[0].StepID;
                getData.stepName = data[0].StepName;
                getData.wfID = data[0].WFID;
                getData.wfName = data[0].WorkFlowName;
                var stepIdNameDisplay = "";
                var wfIdNameDisplay = "";
                if (!getData.stepID) {
                    stepIdNameDisplay = "none";
                }
                if (!getData.wfID) {
                    wfIdNameDisplay = "none";
                }

                var checkBox = `<tr>
                                    <div class="col-md-6">
                                        <td class="heading"><label>Input Required :</label></td>
                                        <td colspan="3">
                                        <div style="margin-left:-15px;">
                                            <div class="col-md-1" style="margin: 0px;padding-left: 4px;">
                                                <div class="checkbox-container">
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
                                    </tr>`;

                if (ApiProxy === false && getData.url == null) {
                    var newHtml = `<table class="table table-striped">
                            <thead>
                                <tr>
                                    <th colspan="2"><h5><b>Bot ID:</b> ${getData.botId}</h5></th>
                                    <th colspan="2"><h5><b>Bot Name:</b> ${getData.botName}</h5></th>

                                </tr>
                                <tr>
                                    <th colspan="4"><h5><b>Project Name:</b> ${getData.proj}</h5></th>

                                </tr>
                            </thead>
                            <tbody id="botDetailPopUp">
                                <tr>
                                    <td class="heading">Spoc(Name/Signum):</td>
                                    <td>${getData.spocsignum}</td>

                                </tr>
                                <tr>
                                    <td class="heading">Domain/Subdomain:</td>
                                    <td>${getData.domain}</td>
                                    <td class="heading">Tool Name:</td>
                                    <td>${getData.toolNames}</td>
                                </tr>
                                <tr>
                                    <td class="heading">Tool Type:</td>
                                    <td colspan="3">${getData.tooltype}</td>
                                </tr>
                                 <tr>
                                <td class="heading">Task Name:</td>
                                <td colspan="3">${getData.TaskName}</td>
                                </tr>

                                 <tr>
                                <td class="heading" style="display:${stepIdNameDisplay}">Step ID/Name:</td>
                                    <td style="display:${stepIdNameDisplay}">${getData.stepID}/${getData.stepName}</td>
                                    <td class="heading" style="display:${wfIdNameDisplay}">WF ID/Name:</td>
                                    <td style="display:${wfIdNameDisplay}">${getData.wfID}/${getData.wfName}</td>
                                </tr>

                                <tr>
                                 <td class="heading">Technology:</td>
                                <td>${getData.tech}</td>
                                <td class="heading">Activity/Subactivity:</td>
                                <td>${getData.activity} / ${getData.SubactivityName}</td>
                                </tr>
                                <tr>
                                    <td class="heading">Access Method:</td>
                                        <td colspan="3">${getData.accessmethod}</td>
                                    </tr>
                                <tr>
                                    <td class="heading">Description:</td>
                                    <td colspan="3">${getData.description}</td>
                                </tr>
                                 <tr>
                                        <td class="heading">Bot Executions:</td>
                                        <td >${getData.BotExecutionCount}</td>
                                         <td class="heading">Bot Reused:</td>
                                        <td >${getData.BotReuseCount}</td>
                                  </tr>
 <tr>
                                    <td class="heading">Language Base Version</td>
                                    <td colspan="3">${getData.LanguageBaseVersion}</td>
                                </tr>
                                
                                <tr>
                                    <td class="heading">Downloadable Files:</td>
                                    <td colspan="3">
										<button class="btn btn-default infile" type="button" title='Input File'
										onclick="downloadBotFiles({thisObj:this,botId:${requestID},fileName:'input.zip'});">
											<i class='fa fa-download' aria-hidden='true'></i> Input</button>
										<button class=' btn btn-default' type="button" title='Output File'
										onclick="downloadBotFiles({thisObj:this,botId:${requestID},fileName:'output.zip'});">
											<i class='fa fa-download' aria-hidden='true'></i>Output</button>
										<button class=' btn btn-default' type="button" title='Code File'
										onclick="downloadBotFiles({thisObj:this,botId:${requestID},fileName:'code.zip'});">
											<i class='fa fa-download' aria-hidden='true'></i>Code</button></button>
										<button class=' btn btn-default' type="button" title='Logic File'
										onclick="downloadBotFiles({thisObj:this,botId:${requestID},fileName:'logic.zip'});">
											<i class='fa fa-download' aria-hidden='true'></i>Logic</button>
									</td>
                                </tr>
                            </tbody>
                        </table>`;

                }
                else if (ApiProxy === false) {
                    var newHtml = `<table class="table table-striped">
                            <thead>
                                    <tr>
                                    <th colspan="2"><h5><b>Bot ID:</b> ${getData.botId}</h5></th>
                                    <th colspan="2"><h5><b>Bot Name:</b> ${getData.botName}</h5></th>

                                </tr>
                                <tr>
                                    <th colspan="4"><h5><b>Project Name:</b> ${proj}</h5></th>

                                </tr>
                            </thead>
                            <tbody id="botDetailPopUp">
                                <tr>
                                    <td class="heading">Spoc(Name/Signum):</td>
                                    <td>${getData.spocsignum}</td>

                                </tr>
                               <tr>
                                    <td class="heading">Domain/Subdomain:</td>
                                    <td>${getData.domain}</td>
                                    <td class="heading">Tool Name:</td>
                                    <td>${getData.toolNames}</td>
                                </tr>
                                <tr>
                                    <td class="heading">Tool Type:</td>
                                    <td colspan="3">${getData.tooltype}</td>
                                </tr>
                                 <tr>
                                <td class="heading">Task Name:</td>
                                <td colspan="3">${getData.TaskName}</td>
                                </tr>


                                 <tr>
                                <td class="heading" style="display:${stepIdNameDisplay}">Step ID/Name:</td>
                                    <td style="display:${stepIdNameDisplay}">${getData.stepID}/${getData.stepName}</td>
                                    <td class="heading" style="display:${wfIdNameDisplay}">WF ID/Name:</td>
                                    <td style="display:${wfIdNameDisplay}">${getData.wfID}/${getData.wfName}</td>
                                </tr>

                                <tr>
                                 <td class="heading">Technology:</td>
                                <td>${getData.tech}</td>
                                <td class="heading">Activity/Subactivity:</td>
                                <td>${getData.activity} /${getData.SubactivityName}</td>
                                </tr>
                                <tr>
                                    <td class="heading">Access Method:</td>
                                        <td colspan="3">${getData.accessmethod}</td>
                                    </tr>
                                <tr>
                                    <td class="heading">Description:</td>
                                    <td colspan="3">${getData.description}</td>
                                </tr>
                                    

                                <tr>
                                        <td class="heading">Bot Executions:</td>
                                        <td >${getData.BotExecutionCount}</td>
                                         <td class="heading">Bot Reused:</td>
                                        <td >${getData.BotReuseCount}</td>
                                  </tr>
 <tr>
                                    <td class="heading">Language Base Version</td>
                                    <td colspan="3">${getData.LanguageBaseVersion}</td>
                                </tr>
                                
                                <tr>
                                    <td class="heading">Downloadable Files:</td>
                                    <td colspan="3">
										<button class=' btn btn-default infile' type="button" title='Input File'
										onclick="downloadBotFiles({thisObj:this,botId:${requestID},fileName:'input.zip'});">
										<i class='fa fa-download' aria-hidden='true'></i> Input</button>
										<button class=' btn btn-default' type="button" title='Output File'
										onclick="downloadBotFiles({thisObj:this,botId:${requestID},fileName:'output.zip'});">
										<i class='fa fa-download' aria-hidden='true'></i> Output</button>
										<button class=' btn btn-default' type="button" title='Code File'
										onclick="downloadBotFiles({thisObj:this,botId:${requestID},fileName:'code.zip'});">
										<i class='fa fa-download' aria-hidden='true'></i>Code</button>
										<button class=' btn btn-default' type="button" title='Logic File'
										onclick="downloadBotFiles({thisObj:this,botId:${requestID},fileName:'logic.zip'});">
										<i class='fa fa-download' aria-hidden='true'></i>Logic</button>
										<button class=' btn btn-default' type="button" title='Logic File'
										onclick="window.open(\'${getData.url}\','_blank');">
										<i class='fa fa-download' aria-hidden='true'></i>Video URL</button>
									</td>
                                </tr>
                            </tbody>
                        </table>`;

                    $('#requestInfo').modal('show');
                }
                else {
                    var newHtml = `<table class="table table-striped">
                            <thead>
                                <tr>
                                    <th colspan="2"><h5><b>Bot ID:</b> ${getData.botId}</h5></th>
                                    <th colspan="2"><h5><b>Bot Name:</b> ${getData.botName}</h5></th>

                                </tr>
                                <tr>
                                    <th colspan="4"><h5><b>Project Name:</b> ${proj}</h5></th>

                                </tr>
                            </thead>
                            <tbody id="botDetailPopUp">
                                <tr>
                                    <td class="heading">Spoc(Name/Signum):</td>
                                    <td>${getData.spocsignum}</td>

                                </tr>
                               <tr>
                                    <td class="heading">Domain/Subdomain:</td>
                                    <td>${getData.domain}</td>
                                    <td class="heading">Tool Name:</td>
                                    <td>${getData.toolNames}</td>
                                </tr>
                                <tr>
                                    <td class="heading">Tool Type:</td>
                                    <td colspan="3">${getData.tooltype}</td>
                                </tr>
                                 <tr>
                                <td class="heading">Task Name:</td>
                                <td colspan="3">${getData.TaskName}</td>
                                </tr>


                                 <tr>
                                <td class="heading" style="display:${stepIdNameDisplay}">Step ID/Name:</td>
                                    <td style="display:${stepIdNameDisplay}">${getData.stepID}/${getData.stepName}</td>
                                    <td class="heading" style="display:${wfIdNameDisplay}">WF ID/Name:</td>
                                    <td style="display:${wfIdNameDisplay}">${getData.wfID}/${getData.wfName}</td>
                                </tr>

                                <tr>
                                 <td class="heading">Technology:</td>
                                <td>${getData.tech}</td>
                                <td class="heading">Activity/Subactivity:</td>
                                <td>${getData.activity} / ${getData.SubactivityName}</td>
                                </tr>
                                <tr>
                                    <td class="heading">Access Method:</td>
                                        <td colspan="3">${getData.accessmethod}</td>
                                    </tr>
                                <tr>
                                    <td class="heading">Description:</td>
                                    <td colspan="3">${getData.description}</td>
                                </tr>

                                <tr>
                                        <td class="heading">Bot Executions:</td>
                                        <td >${getData.BotExecutionCount}</td>
                                         <td class="heading">Bot Reused:</td>
                                        <td >${getData.BotReuseCount}</td>
                                  </tr>
 <tr>
                                    <td class="heading">Language Base Version</td>
                                    <td colspan="3">${getData.LanguageBaseVersion}</td>
                                </tr>
                            </tbody>
                        </table>`;

                    $('#requestInfo').modal('show');
                }

                if (currStatus == 'REJECTED' || currStatus == 'Rejected') {
                    let newHtml = `<button type="button" class="btn btn-primary" data-dismiss="modal" style="margin-left:10px">Close</button>`;
                    $('#requestfoot').html('').append(newHtml);
                } else {
                    let newHtml = `<button type="button" class="btn btn-primary" data-dismiss="modal">Close</button>
                                    <button type="button" id="testBtnBotRequest" data-details="" class="btn btn-sm btn-warning testNewBotRequest" style="margin-left:10px">TEST BOT</button>
 <button type="button" id="stopTestBtnBotRequest" data-details="" class="btn btn-sm btn-danger stopTestBtnBotRequest" style="margin-left:10px">STOP TEST-BOT</button>`;

                    $('#requestfoot').html('').append(newHtml);

                    $('.testNewBotRequest').on('click', function () {
                        pwIsf.addLayer({ text: 'Please wait ...' });
                        let ajCall = $.isf.ajax({
                            url: service_java_URL + "botStore/createBotTestingRequest/" + requestID + "/" + signumGlobal,
                            contentType: 'application/json',
                            type: 'POST',
                            returnAjaxObj: true
                        });

                        ajCall.done(function (data) {
                            if (data.apiSuccess) {
                                pwIsf.alert({ msg: 'Testing will be initiated after providing input to ISF Popup. Please ensure ISF Desktop App is running', type: 'success' });
                                window.open('isfalert:test_' + requestID, '_self');
                            } else {
                                pwIsf.alert({ msg: '' + data.responseMsg, type: 'warning' });
                            }
                        });

                        ajCall.fail(function () { pwIsf.alert({ msg: 'Error in BOT testing.', type: 'error' }); });
                        ajCall.always(function () { pwIsf.removeLayer(); });

                    });


                    $('.stopTestBtnBotRequest').on('click', function () {
                        pwIsf.addLayer({ text: 'Please wait ...' });
                        let ajCall = $.isf.ajax({
                            url: service_java_URL + "botStore/stopInprogressBot/" + requestID + "/" + signumGlobal,
                            contentType: 'application/json',
                            type: 'POST',
                            returnAjaxObj: true
                        });

                        ajCall.done(function (data) {
                            if (data.apiSuccess) {
                                pwIsf.alert({ msg: '' + data.responseMsg, type: 'success' });

                            } else {
                                pwIsf.alert({ msg: '' + data.responseMsg, type: 'warning' });
                            }
                        });

                        ajCall.fail(function () { pwIsf.alert({ msg: 'Error in BOT testing.', type: 'error' }); });
                        ajCall.always(function () { pwIsf.removeLayer(); });

                    });
                }



                $('#requestDetailsInfo').html('').append(newHtml);
                $('#botDetailPopUp').append(checkBox);

                // checkbox disabled
                $('.checkbox-container').addClass('disabledbutton');

                if (getData.isInputRequired) {
                    $('.inputRequiredCheckboxOwnDev').attr('checked', getData.isInputRequired);
                } else {
                    $(".infile").addClass("disabledbutton");
                    $(".inputLabel").css("color", '#868598');
                    $(".inputLabel").text("Bot Input Not Required");
                }
            }
            else {

                var newHtml = `No records returned !!!`;
                var newH = `<button type="button" class="btn btn-primary" data-dismiss="modal" style="margin-left:10px">Close</button>`;
                $('#requestDetailsInfo').append(newHtml);
                $('#requestfoot').html('').append(newH);
            }

        },
        error: function (xhr, status, statusText) {
            console.log('An error occurred');
        },
        complete: function () {
            pwIsf.removeLayer();
        }
    });

}

function submitURL() {
    let rpaID = $("#rpaID").val();
    let url = $("#videoURL").val();

    if ($("#videoURL").val() == null || $("#videoURL").val() == "") {
        pwIsf.alert({ msg: "Url is not entered", type: 'warning' });
    }
    else {
        let urljson = new Object();
        urljson.rpaRequestID = rpaID;
        urljson.videoURL = url;
        pwIsf.addLayer({ text: 'Please wait ...' });
        $.isf.ajax({
            url: service_java_URL + "rpaController/updateVideoURL",
            context: this,
            crossdomain: true,
            processData: true,
            contentType: 'application/json',
            data: JSON.stringify(urljson),
            type: 'POST',
            xhrFields: {
                withCredentials: false
            },
            success: function (data) {
                if (data.apiSuccess === true) {
                    pwIsf.alert({ msg: "Request ID " + rpaID + " successfully updated with URL.", type: "success" });
                } else {
                    pwIsf.alert({ msg: data.responseMsg, type: "error" });
                }
                $('#demoURLPopup').modal('hide');
                $("#videoURL").val('');

                //getRequestDetails();
            },
            error: function (xhr, status, statusText) {
                $('#demoURLPopup').modal('hide');
                $("#videoURL").val('');
                pwIsf.alert({ msg: "Error in Updating URL for Request ID:" + rpaID, type: 'warning' });

            },
            complete: function (xhr, statusText) {
                let blankURL = '_blank';
                $("#videoURL").val('');
                $("#URL_" + rpaID).html('');
                $("#URL_" + rpaID).append('<a onclick="window.open(\'' + url + '\',\'' + blankURL + '\')">' + url + '</a>');
                pwIsf.removeLayer();
            }
        });


    }
}

function getSignumWO(requestID) {
    $.isf.ajax({

        url: service_java_URL + "accessManagement/searchAccessDetailsByFilter/all/9/0",

        beforeSend: function (xhr) {
            $("#cbxSignumWO").html("<option> Loading Signums... </option>");
        },
        success: function (data) {

            $('#cbxSignumWO').append('<option value="0">---Select Signum---</option>');
            var html = "";
            $.each(data, function (i, d) {
                $('#cbxSignumWO').append('<option value="' + d.signumID + '">' + d.signumID + '</option>');
            })
        },
        complete: function () {
            pwIsf.removeLayer();
            $('#cbxSignumWO').find("option").eq(0).remove();
        },
        error: function (xhr, status, statusText) {
            alert("Fail Get Signum " + xhr.responseText);
            alert("status " + xhr.status);
            console.log('An error occurred');
        }
    });
}

function assignRequest(requestID) {
    assignRequestID = requestID;
    getSignumWO();
    if ($('#cbxSignumWO').val() == "Loading Signums...")
        receiver = '0';
    else
        receiver = $('#cbxSignumWO').val();
}

function massTransferWorkOrderWO(receiver) {
    var status = []; selectedWO = [];

    if (receiver == "0") {
        pwIsf.alert({ msg: "Please select employee signum", type: "warning" });
    }
    else {
        var request = $.isf.ajax({
            url: service_java_URL + "botStore/assignRequestToDev/" + assignRequestID + "/" + receiver + "/" + signumGlobal,
            method: "POST",
            success: assignDev,
            contentType: "application/json; charset=utf-8",
            dataType: "html",
            returnAjaxObj: true
        });

        request.fail(function (jqXHR, textStatus) {
            pwIsf.removeLayer();
            pwIsf.alert({ msg: "Request Failed" });
        });

        function assignDev(data) {
            var newData = JSON.parse(data);
            pwIsf.removeLayer();
            if (newData.apiSuccess) {
                pwIsf.alert({ msg: newData.responseMsg, type: 'success' });
            }
            else
                pwIsf.alert({ msg: "Error in Assigning", type: 'warning' });

        }
    }

}

function showAuditModal(requestID) {
    auditRequestID = requestID;
    $("#auditModal").modal('show');
}

function submitAuditStatus() {
    var status = $("#auditOptions")[0].value;
    updateAuditStatus(auditRequestID, status);
}

function updateAuditStatus(requestID, status) {

    pwIsf.addLayer({ text: 'Please wait ...' });
    $.isf.ajax({
        url: service_java_URL + "botStore/updateBotTestResultsForAuditUAT/" + requestID + "/" + signumGlobal + "/" + status,
        context: this,
        crossdomain: true,
        processData: true,
        contentType: 'application/json',
        type: 'POST',
        xhrFields: {
            withCredentials: false
        },
        success: function (data) {
            $("#auditModal").modal('hide');
            if (data.apiSuccess)
                pwIsf.alert({ msg: data.responseMsg, type: "success" });
            else
                pwIsf.alert({ msg: data.responseMsg, type: 'warning' });


        },
        error: function (xhr, status, statusText) {
            pwIsf.alert({ msg: "Error while changing status", type: "warning" });
        },
        complete: function (xhr, statusText) {
            pwIsf.removeLayer();
        }
    });

}

function deleteBotRequest(requestID, signum) {
    pwIsf.confirm({
        title: 'Delete Request', msg: "Are you sure you want to delete the Request?",
        'buttons': {
            'Yes': {
                'action': function () {
                    pwIsf.addLayer({ text: 'Please wait ...' });
                    $.isf.ajax({
                        url: service_java_URL + "botStore/deleteAutomationRequest/" + requestID + "/" + signum,
                        context: this,
                        crossdomain: true,
                        processData: true,
                        contentType: 'application/json',
                        type: 'POST',
                        xhrFields: {
                            withCredentials: false
                        },
                        success: function (data) {

                            if (data.apiSuccess)
                                pwIsf.alert({ msg: "Request ID " + requestID + " successfully Deleted.", type: "success" });
                            else
                                pwIsf.alert({ msg: data.responseMsg, type: 'warning' });
                            getRequestDetails();

                        },
                        error: function (xhr, status, statusText) {
                            pwIsf.alert({ msg: "Error in Deleting", type: 'warning' });
                        },
                        complete: function (xhr, statusText) {
                            pwIsf.removeLayer();
                        }
                    });
                }
            },
            'No': { 'action': function () { } },

        }
    });

}

function deployBotRequest(requestID, signum) {
    pwIsf.confirm({
        title: 'Deploy Request', msg: "Do you want to Deploy?",
        'buttons': {
            'Yes': {
                'action': function () {
                    pwIsf.addLayer({ text: 'Please wait ...' });
                    $.isf.ajax({
                        url: service_java_URL + "botStore/deployBot/" + requestID + "/" + signum,
                        context: this,
                        crossdomain: true,
                        processData: true,
                        contentType: 'application/json',
                        type: 'POST',
                        xhrFields: {
                            withCredentials: false
                        },
                        success: function (data) {

                            if (data.apiSuccess)
                                pwIsf.alert({ msg: "Request id " + requestID + "successfully deployed", type: 'success' });
                            else
                                pwIsf.alert({ msg: data.responseMsg, type: 'warning' });


                        },
                        error: function (xhr, status, statusText) {
                            pwIsf.alert({ msg: "Error in Deploying", type: 'warning' });
                        },
                        complete: function (xhr, statusText) {
                            pwIsf.removeLayer();
                        }
                    });
                }
            },
            'No': { 'action': function () { } },

        }
    });

}

function getVersionFlowChart(projectID, subactivityID, SubActivityFlowChartDefID) {
    var versionNo = 0;
    var width = window.innerWidth * 0.85;
    var height = width * window.innerHeight / window.innerWidth;
    authWindow = window.open('', '_blank', 'width=' + width + ', height=' + height + ', top=' + ((window.innerHeight - height) / 2) + ', left=' + ((window.innerWidth - width) / 2));
    var _jqXHR = $.isf.ajax({
        type: "GET",
        url: service_java_URL + "rpaController/getWoIDByProjectID/" + projectID + "/" + subactivityID + "/" + SubActivityFlowChartDefID,
        returnAjaxObj: true,
        error: function (msg) {

        },
        complete: function (msg) {
        }
    });

    function getData(data) {
        $.each(data.data, function (i, d) {
            versionNo = d.VersionNumber;

        })

    }

    _jqXHR.done(function (status) {
        if (status.data.length != 0) {
            var subActID = subactivityID;
            var projID = projectID;
            var version_id = status.data[0].VersionNumber;
            var wfid = status.data[0].WFID;
            var domain = status.data[0].domain;
            var subDomain = status.data[0].SubDomain;
            var subServiceArea = status.data[0].SubServiceArea;
            var tech = status.data[0].technology;
            var activity = status.data[0].activity;
            var subActivity = status.data[0].SubActivity;
            var scopeName = status.data[0].ScopeName;
            var scopeID = status.data[0].ProjectScopeID;


            //As discussed with Jayant in 10.2 we are making experiencedMode = 0 as BotRequest can be raised only for Assessed Workflow.
            var experiencedMode = 0;
        }



        flowChartViewOpenInNewWindow(UiRootDir + '/Project/FlowChartWorkOrderView\?subId=' + subActID + '&projID=' + projID + '&version=' + version_id + '&domain=' + domain + '&subDomain=' + subDomain + '&subServiceArea=' + subServiceArea + '&tech=' + tech + '&activity=' + activity + '&subActivity=' + subActivity + '&experiencedMode=' + experiencedMode + '&wfid=' + wfid + '&botMode=false');

    });


}

function fileupdloadForMacro(e) {

    fl = e.files[0];
    var filename = $('#macroCodeFile').val().split('\\').pop();
    var filesize = fl.size;
    let fileNameRegEX = /^[ A-Za-z0-9_@./#&+-]*$/;


    var ext = filename.split('.').pop();
    if (ext != 'xlsm' && ext != 'xlsb') {
        pwIsf.alert({ msg: 'Code file should be in either .xlsm or .xlsb extension file', type: 'info', autoClose: 4 });
    }
    else if (filesize > 2097152) {
        pwIsf.alert({ msg: 'Code file should no be greater than 2MB', type: 'info', autoClose: 4 });
    }
    else if (filename.length > 100) {
        pwIsf.alert({ msg: 'Code file name should be less than 100 characters', type: 'info', autoClose: 4 });
    }
    else if (fileNameRegEX.test(filename) != true) {
        pwIsf.alert({ msg: 'Code file name should only contain alphanumeric and special characters allowed by windows', type: 'info', autoClose: 4 });
    }

}

function uploadURL(rpaID) {
    $('#rpaID').val(rpaID);
    $('#demoURLPopup').modal('show');

}

function flowChartViewOpenInNewWindow(url) {
    authWindow.location.replace(url);
}

function getWFUser() {

    $("#select_assign_to_user").removeAttr('readonly');
    $("#select_assign_to_user").append(' ');
    $("#select_assign_to_user").autocomplete({
        appendTo: "#updateBody",
        source: function (request, response) {
            $.isf.ajax({

                url: service_java_URL + "activityMaster/getEmployeesByFilter",

                type: "POST",
                data: {
                    term: request.term
                },
                success: function (data) {
                    $("#select_assign_to_user").autocomplete().addClass("ui-autocomplete-loading");

                    var result = [];
                    $.each(data, function (i, d) {
                        result.push({
                            "label": d.signum + "/" + d.employeeName,
                            "value": d.signum

                        });


                    })
                    response(result);
                    $("#select_assign_to_user").autocomplete().removeClass("ui-autocomplete-loading");

                }
            });
        },
        minLength: 3

    });
    $("#select_assign_to_user").autocomplete("widget").addClass("fixedHeight");
}

function getAllUsers() {
    getWFUser();
}

imgDataForAjax = {};

imgDataForAjax.createHiddenFlds = function (jsonNameIdVal, idPreFix) {  //create hidden flds from json 
    let html = '';
    for (let fk in jsonNameIdVal) {
        let nm = jsonNameIdVal[fk]['name'];
        let inputId = (jQuery.trim(jsonNameIdVal[fk]['id']) != '') ? `id="${idPreFix}_${jsonNameIdVal[fk]['id']}"` : '';
        let val = (jQuery.trim(jsonNameIdVal[fk]['value']) != '') ? `value="${jsonNameIdVal[fk]['value']}"` : '';
        html += `<input type="hidden" name="${nm}" ${inputId} ${val} />\n`;
    }
    return html;
};

imgDataForAjax.getFormDataJson = function (frm) { // GET ALL FORM DATA IN JSON FORMAT USING {} AND []
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
        data: sendData,
        processData: false,
        contentType: false,
        cache: false,
        timeout: 600000,
        returnAjaxObj: true,
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
        }
    });

    reqPromise.done(opt.onDone);
    reqPromise.fail(function () { alert("Image upload error -" + url); });
    reqPromise.always(function () { pwIsf.removeLayer(); });

};

// SEND UPDATE REQUEST FOR NEW BOT
function updateNewBotRequest() {
    pwIsf.addLayer({ text: 'Uploading files please wait ...', progressId: 'reqUpdatefileProgress', showSpin: false })

    let restData = {};
    restData = imgDataForAjax.getFormDataJson($('#UpdateBotFrm'));

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
    var isInputRequired = $(".inputRequiredCheckboxOwnDev").is(":checked") ? true : false;

    restData['tblRpaRequestStr']['isInputRequired'] = isInputRequired;

    let opt = {};
    opt.setImgFldsJosn = [{ sendAs: 'infile', frmElmntAs: 'infile' }, { sendAs: 'outfile', frmElmntAs: 'outfile' }, { sendAs: 'logicfile', frmElmntAs: 'logicFile' }];
    opt.formId = 'UpdateBotFrm';

    let form = document.getElementById(opt.formId);
    let frmData = new FormData(form);
    frmData = imgDataForAjax.getData(opt);
    frmData.append('tblRpaRequestStr', JSON.stringify(restData['tblRpaRequestStr'])); // for extra params

    let reqOpt = {};
    reqOpt.url = service_java_URL_VM + "botStore/updateNewRequest";
    reqOpt.progressBarId = 'reqUpdatefileProgress';
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

// Update Macro Bot Request
function updateMacroBotRequest() {
    pwIsf.addLayer({ text: 'Uploading files please wait ...', progressId: 'updateNewMacroBotProgress', showSpin: false })

    let restData = {};
    restData = imgDataForAjax.getFormDataJson($('#updateBotMacroFrm'));

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

    restData['tblRpaBotstagingStr']['reuseFactor'] = restData['tblRpaBotstagingStr']['reuseFactor'];
    restData['tblRpaBotstagingStr']['lineOfCode'] = restData['tblRpaBotstagingStr']['lineOfCode'];

    // get checkbox value and update in restDate
    var isInputRequired = $(".inputRequiredCheckboxOwnDev").is(":checked") ? true : false;

    restData['tblRpaRequestStr']['isInputRequired'] = isInputRequired;

    let opt = {};
    opt.setImgFldsJosn = [{ sendAs: 'infile', frmElmntAs: 'inFile' }, { sendAs: 'outfile', frmElmntAs: 'outFile' }, { sendAs: 'logicfile', frmElmntAs: 'logicFile' }, { sendAs: 'codefile', frmElmntAs: 'codeFile' }];
    opt.formId = 'updateBotMacroFrm';

    let form = document.getElementById(opt.formId);
    let frmData = new FormData(form);
    frmData = imgDataForAjax.getData(opt);
    frmData.append('tblRpaRequestStr', JSON.stringify(restData['tblRpaRequestStr'])); // for extra params
    frmData.append('tblRpaBotstagingStr', JSON.stringify(restData['tblRpaBotstagingStr']));

    let reqOpt = {};
    reqOpt.url = service_java_URL_VM + "botStore/updateBotRequest";
    reqOpt.progressBarId = 'updateNewMacroBotProgress';
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

// Update REQUEST FOR NEW java BOT
function updateBotRequestJava() {
    pwIsf.addLayer({ text: 'Uploading files please wait ...', progressId: 'updateJavaBotProgress', showSpin: false })
    let restData = {};
    restData = imgDataForAjax.getFormDataJson($('#updateBotJavaFrm'));
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


    restData['tblRpaBotstagingStr']['reuseFactor'] = restData['tblRpaBotstagingStr']['reuseFactor'];
    restData['tblRpaBotstagingStr']['lineOfCode'] = restData['tblRpaBotstagingStr']['lineOfCode'];
    restData['tblRpaBotstagingStr']['botname'] = restData['tblRpaRequestStr']['botname'];
    restData['tblRpaRequestStr']['requestName'] = restData['tblRpaRequestStr']['botname'];

    // get checkbox value and update in restDate
    var isInputRequired = $(".inputRequiredCheckboxOwnDev").is(":checked") ? true : false;
    restData['tblRpaRequestStr']['isInputRequired'] = isInputRequired;

    let opt = {};
    opt.setImgFldsJosn = [{ sendAs: 'infile', frmElmntAs: 'infile' }, { sendAs: 'outfile', frmElmntAs: 'outfile' }, { sendAs: 'logicfile', frmElmntAs: 'logicFile' }, { sendAs: 'codefile', frmElmntAs: 'codeFile' }, { sendAs: 'exefile', frmElmntAs: 'exefile' }];
    opt.formId = 'updateBotJavaFrm';

    let form = document.getElementById(opt.formId);
    let frmData = new FormData(form);
    frmData = imgDataForAjax.getData(opt);
    frmData.append('tblRpaRequestStr', JSON.stringify(restData['tblRpaRequestStr'])); // for extra params
    frmData.append('tblRpaBotstagingStr', JSON.stringify(restData['tblRpaBotstagingStr']));

    let reqOpt = {};
    reqOpt.url = service_java_URL_VM + "botStore/updateBotRequest";
    reqOpt.progressBarId = 'updateJavaBotProgress';
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

// Update REQUEST FOR  python BOT
function updateBotRequestPython() {
    const isContinue = ValidateFormPythonBot();

    if (isContinue) {
        pwIsf.addLayer({ text: 'Uploading files please wait ...', progressId: 'updatePythonProgress', showSpin: false })

        let restData = {};
        restData = imgDataForAjax.getFormDataJson($('#updateBotPythonFrm'));

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

        //restData['tblRpaBotstagingStr']['reuseFactor'] = restData['tblRpaBotstagingStr']['reuseFactor'];
        //restData['tblRpaBotstagingStr']['lineOfCode'] = restData['tblRpaBotstagingStr']['lineOfCode'];
        restData['tblRpaBotstagingStr']['moduleClassName'] = restData.packageName;
        restData['tblRpaBotstagingStr']['languageBaseVersionID'] = $("#selectVersionEditBot").val() == "-1" ? null : $("#selectVersionEditBot").val();
        restData['tblRpaBotstagingStr']['botname'] = restData['tblRpaRequestStr']['botname'];
        restData['tblRpaRequestStr']['requestName'] = restData['tblRpaRequestStr']['botname'];

        // get checkbox value and update in restDate
        var isInputRequired = $(".inputRequiredCheckboxOwnDev").is(":checked") ? true : false;
        restData['tblRpaRequestStr']['isInputRequired'] = isInputRequired;

        let opt = {};
        opt.setImgFldsJosn = [{ sendAs: 'infile', frmElmntAs: 'infile' }, { sendAs: 'outfile', frmElmntAs: 'outfile' },
        { sendAs: 'logicfile', frmElmntAs: 'logicFile' }, { sendAs: 'codefile', frmElmntAs: 'codeFile' },
        { sendAs: 'whlfile', frmElmntAs: 'whlfile' }];
        opt.formId = 'updateBotPythonFrm';

        let form = document.getElementById(opt.formId);
        let frmData = new FormData(form);
        frmData = imgDataForAjax.getData(opt);
        frmData.append('tblRpaRequestStr', JSON.stringify(restData['tblRpaRequestStr'])); // for extra params
        frmData.append('tblRpaBotstagingStr', JSON.stringify(restData['tblRpaBotstagingStr']));

        let reqOpt = {};
        reqOpt.url = service_java_URL_VM + "botStore/updateBotRequest";
        reqOpt.progressBarId = 'updatePythonProgress';
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
}


function ValidateFormPythonBot() {

    let isContinue = true;

    let isFileEmpty = document.getElementById('editBotWHLFile').files.length === 0;
    let isVersionEmpty = $('#selectVersionEditBot').val() === '-1';
    let isPackageNameEmpty = $('#editBotPackageName').val() === '';
    let areAllWhlFieldsEmpty = isFileEmpty && isVersionEmpty && isPackageNameEmpty;
    let areWhlFieldImbalanced = (!areAllWhlFieldsEmpty) && (isFileEmpty || isVersionEmpty || isPackageNameEmpty);

    if (areWhlFieldImbalanced) {
        pwIsf.alert({ msg: "Python Base Version, Package Name and .whl file are mandatory if any one is filled!", type: "warning" });
        isContinue = false
    }
    else if (!areAllWhlFieldsEmpty) {
        isContinue = ValidatePythonNewFields(isContinue);
    }
    return isContinue;
}

function ValidatePythonNewFields(isContinue) {
    let pythonVersion = $('#selectVersionEditBot').find(":selected").text();

    let whlFileName = document.getElementById("editBotWHLFile").files[0].name.split('.whl')[0];
    let whlFileContentArr = whlFileName.split('-');
    const isWhlFileFormatInValid = (whlFileContentArr.length - 1) < 4;
    const packageNameFromWhlFile = isWhlFileFormatInValid ? "" : whlFileContentArr.reverse().slice(4).reverse().join('-');


    if (pythonVersion === "-1") {
        pwIsf.alert({ msg: 'Warning:  ' + "Select Python Base Version.", type: 'warning' });
        isContinue = false;
    }

    if (isContinue) {
        if (!isWhlFileFormatInValid) {
            isContinue = PackageNameValidation(isContinue, packageNameFromWhlFile);

            if (isContinue) {
                isContinue = ComparePythonVersion(whlFileContentArr, pythonVersion, isContinue);
            }
        }
        else {
            pwIsf.alert({ msg: 'Warning:  ' + "WHL file's name is not in correct format", type: 'warning' });
            isContinue = false;
        }
    }
    return isContinue;
}

function PackageNameValidation(isContinue, packageNameFromWhlFile) {
    let packageName = $("#editBotPackageName").val();

    if (packageName === undefined || packageName == null) {
        pwIsf.alert({ msg: 'Warning:  ' + "Package Name is missing", type: 'warning' });
        isContinue = false;
    }
    else if (packageNameFromWhlFile !== packageName) {
        pwIsf.alert({ msg: 'Warning:  ' + "WHL file's name should match with package Name", type: 'warning' });
        isContinue = false;
    }
    return isContinue;
}

function ComparePythonVersion(whlFileContentArr, pythonVersion, isContinue) {
    whlFileContentArr.reverse();
    const versionFromFile = whlFileContentArr[whlFileContentArr.length - 3];
    const versionArray = pythonVersion.split(".");
    const versionToCompare = `py${versionArray[0]}${versionArray[1]}`;

    if (versionToCompare != versionFromFile) {
        pwIsf.alert({ msg: 'Warning:  ' + "WHL file's Version should match with Python Base Version", type: 'warning' });
        isContinue = false;
    }
    return isContinue;
}

// Update for DotNet
function updateBotRequestDotNet() { // Update REQUEST FOR  DotNet BOT
    pwIsf.addLayer({ text: 'Uploading files please wait ...', progressId: 'updateDotNetProgress', showSpin: false })

    let restData = {};
    restData = imgDataForAjax.getFormDataJson($('#updateBotDotNetFrm'));

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

    restData['tblRpaBotstagingStr']['reuseFactor'] = restData['tblRpaBotstagingStr']['reuseFactor'];
    restData['tblRpaBotstagingStr']['lineOfCode'] = restData['tblRpaBotstagingStr']['lineOfCode'];
    restData['tblRpaBotstagingStr']['botname'] = restData['tblRpaRequestStr']['botname'];
    restData['tblRpaRequestStr']['requestName'] = restData['tblRpaRequestStr']['botname'];

    // get checkbox value and update in restDate
    var isInputRequired = $(".inputRequiredCheckboxOwnDev").is(":checked") ? true : false;
    restData['tblRpaRequestStr']['isInputRequired'] = isInputRequired;

    let opt = {};
    opt.setImgFldsJosn = [{ sendAs: 'infile', frmElmntAs: 'infile' }, { sendAs: 'outfile', frmElmntAs: 'outfile' }, { sendAs: 'logicfile', frmElmntAs: 'logicFile' }, { sendAs: 'codefile', frmElmntAs: 'codeFile' }, { sendAs: 'exefile', frmElmntAs: 'exefile' }];
    opt.formId = 'updateBotDotNetFrm';

    let form = document.getElementById(opt.formId);
    let frmData = new FormData(form);
    frmData = imgDataForAjax.getData(opt);
    frmData.append('tblRpaRequestStr', JSON.stringify(restData['tblRpaRequestStr'])); // for extra params
    frmData.append('tblRpaBotstagingStr', JSON.stringify(restData['tblRpaBotstagingStr']));

    let reqOpt = {};
    reqOpt.url = service_java_URL_VM + "botStore/updateBotRequest";
    reqOpt.progressBarId = 'updateDotNetProgress';
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


// Update funtion for penguin
function updateBotRequestPenguin() { // Update REQUEST FOR  penguin BOT
    pwIsf.addLayer({ text: 'Uploading files please wait ...', progressId: 'updatePenguinProgress', showSpin: false })

    let restData = {};
    restData = imgDataForAjax.getFormDataJson($('#updateBotPenguinFrm'));

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

    restData['tblRpaBotstagingStr']['reuseFactor'] = restData['tblRpaBotstagingStr']['reuseFactor'];
    restData['tblRpaBotstagingStr']['lineOfCode'] = restData['tblRpaBotstagingStr']['lineOfCode'];
    restData['tblRpaBotstagingStr']['botname'] = restData['tblRpaRequestStr']['botname'];
    restData['tblRpaRequestStr']['requestName'] = restData['tblRpaRequestStr']['botname'];

    // get checkbox value and update in restDate
    var isInputRequired = $(".inputRequiredCheckboxOwnDev").is(":checked") ? true : false;
    restData['tblRpaRequestStr']['isInputRequired'] = isInputRequired;

    let opt = {};
    opt.setImgFldsJosn = [{ sendAs: 'infile', frmElmntAs: 'infile' }, { sendAs: 'outfile', frmElmntAs: 'outfile' }, { sendAs: 'logicfile', frmElmntAs: 'logicFile' }, { sendAs: 'codefile', frmElmntAs: 'codeFile' }, { sendAs: 'exefile', frmElmntAs: 'exefile' }];
    opt.formId = 'updateBotPenguinFrm';

    let form = document.getElementById(opt.formId);
    let frmData = new FormData(form);
    frmData = imgDataForAjax.getData(opt);
    frmData.append('tblRpaRequestStr', JSON.stringify(restData['tblRpaRequestStr'])); // for extra params
    frmData.append('tblRpaBotstagingStr', JSON.stringify(restData['tblRpaBotstagingStr']));

    let reqOpt = {};
    reqOpt.url = service_java_URL_VM + "botStore/updateBotRequest";
    reqOpt.progressBarId = 'updatePenguinProgress';
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


//Update  end for DOTNet and Penguin
function updateReqData(thisObj) {
    $('#UpdateInfo').modal('show');

    let jsonData = $(thisObj).data('details');

    let isInputRequired = jsonData.isInputRequired;

    // checkbox template added in html var
    if (jsonData.requeststatus == "NEW") {
        let html = EditBotHTMLForNewStatus(jsonData);
        $('#updateBody').html('').append(html);
        fileValidation();
    }
    else {
        EditBotForCreateStatus(jsonData);
    }

    // populate checkbox on isInputRequired
    if (isInputRequired) {
        $('.inputRequiredCheckboxOwnDev').attr('checked', isInputRequired);
    } else {
        $(".infile").addClass("disabledbutton");
        $(".inputLabel").css("color", '#868598');
        $(".inputLabel").text("Bot Input Not Required");
    }

    $('#UpdateBotFrm').on('submit', function (e) {
        e.preventDefault();
        updateNewBotRequest();
    });

    $('#updateBotMacroFrm').on('submit', function (e) {
        e.preventDefault();
        updateMacroBotRequest();
    });

    $('#updateBotJavaFrm').on('submit', function (e) {
        e.preventDefault();
        updateBotRequestJava();
    });

    $('#updateBotPythonFrm').on('submit', function (e) {
        e.preventDefault();
        updateBotRequestPython();
    });

    $('#updateBotDotNetFrm').on('submit', function (e) {
        e.preventDefault();
        updateBotRequestDotNet();
    });

    $('#updateBotPenguinFrm').on('submit', function (e) {
        e.preventDefault();
        updateBotRequestPenguin();
    });
}

function EditBotForCreateStatus(jsonData) {
    if (jsonData.requeststatus == "CREATE" && jsonData.language == "MACRO" || jsonData.language == "Macro") {
        let html = EditBotHTMLForMacro(jsonData);
        $('#updateBody').html('').append(html);
    }
    else if (jsonData.requeststatus == "CREATE" && jsonData.language == "JAVA" || jsonData.language == "Java") {
        let html = EditBotHTMLForJava(jsonData);
        $('#updateBody').html('').append(html);
    }
    else if (jsonData.requeststatus == "CREATE" && jsonData.language == "PYTHON" || jsonData.language == "Python") {
        let html = EditBotHTMLForPython(jsonData);
        $('#updateBody').html('').append(html);
        getPythonBaseVersionForEditBot();
    }
    else if (jsonData.requeststatus == "CREATE" && jsonData.language == "DotNet" || jsonData.language == "DotNet") {
        let html = EditBotHTMLForDotNet(jsonData);
        $('#updateBody').html('').append(html);
    }
    else if (jsonData.requeststatus == "CREATE" && jsonData.language == "PENGUIN" || jsonData.language == "Penguin") {
        let html = EditBotHTMLForPenguin(jsonData);

        $('#updateBody').html('').append(html);
    }
    else if (jsonData.requeststatus == "CREATE" && jsonData.language == "BOT NEST" || jsonData.language == "BOTNEST" || jsonData.language == "NA") {
        let html = `This request cannot be updated`;
        $('#updateBody').html('').append(html);
    }
    fileValidation();
}

function EditBotHTMLForPenguin(jsonData) {
    return `<div id="PenguinDiv">
                            <form method="POST" enctype="multipart/form-data" id="updateBotPenguinFrm">
                                <input type="hidden" name="tblRpaBotstagingStr{}botlanguage" id="hiddenLanguageNameForBotNewRequest_Penguin" />
                                <input type="hidden" class="form-control" name="tblRpaRequestStr{}rpaRequestId" value="${jsonData.requestId}"  />
                                <input type="hidden" class="form-control" name="tblRpaRequestStr{}requestName" value="${jsonData.requestName}"  />
                                <input type="hidden" class="form-control" name="tblRpaRequestStr{}currentAvgExecutiontime" value="${jsonData.currentAvgExecutiontime}"  />
                                <input type="hidden" class="form-control" name="tblRpaBotstagingStr{}modifiedBy" value="${signumGlobal}"  />
                                <input type="hidden" class="form-control" name="tblRpaRequestStr{}modifiedBy" value="${signumGlobal}"  />
                                <input type="hidden" class="form-control" name="tblRpaBotstagingStr{}moduleClassName" value="${jsonData.classname}"  />
                                <input type="hidden" class="form-control" name="tblRpaBotstagingStr{}moduleClassMethod" value="${jsonData.methodname}"  />                                                   <input type="hidden" class="form-control" name="tblRpaBotstagingStr{}statusDescription" value="ByUser" />
                                <input type="hidden" class="form-control" name="tblRpaBotstagingStr{}parallelWoexecution" value="${jsonData.parallelWoexecution}"  />
                                <input type="hidden" class="form-control" name="tblRpaBotstagingStr{}botlanguage" value="${jsonData.language}"  />

                                <div id="botPenguinHidden_tblRpaRequestStr"></div>
                                <div id="botPenguinHidden_tblRpaBotstagingStr"></div>
                                <div id="botPenguinHidden_tblRpaBotstagingStr1"></div>

                                <div class="row">
                                    <div class="col-md-4">
                                        <label style="margin-left:10px" class="required">Automation Name:</label>
                                        <input type="text" class="form-control" name="tblRpaRequestStr{}botname" value="${jsonData.botname}" />
                                    </div>
                                    <div class="col-md-4">
                                        <label style="margin-left:10px" class="required">Manual Execution Time (Min.):</label>
                                        <input type="number" class="form-control" name="tblRpaRequestStr{}currentAvgExecutiontime" min="0" value="${jsonData.currentAvgExecutiontime}"/>
                                    </div>
                                    <div class="col-md-4">
                                        <label style="margin-left:10px" class="required">Weekly Execution Count:</label>
                                        <input type="number" class="form-control" name="tblRpaRequestStr{}currentExecutioncountWeekly"  min="0" value="${jsonData.currentExecutioncountWeekly}"/>
                                    </div>
                                </div>
                                <hr />

                                <div class="row">
                                    <div class="col-lg-6" style="margin-bottom:0px; overflow: auto;">
                                        <div class="form-group">
                                            <label style="margin-left:10px">Spoc(Signum):</label> <a onclick="getAllUsers()" id="allUserAnchor" style="color:blue;font-size:10px;cursor:pointer"><i class="fa fa-lock" style="color:black"></i>(Click to unlock all users)</a>&nbsp;&nbsp;<span style="color:darkred;font-size:10px;">Edit Assigned SPOC for Request</span>
                                            <div style="width: 20px;  margin-left: 1px; margin-top: -25px; font-size: 17px; color:red; ">*</div>
                                            <input id="select_assign_to_user" style='width:100%' placeholder="Signum/Name" value="${jsonData.spoc}"  name="tblRpaRequestStr{}spocsignum" readonly/>
                                            <br />
                                            <b>Minimum 3 characters required.(choose signum from the list)</b>
                                            
                                            <div id="emptysignum-message" style="display:none; color: red">Please seletct an Signum from the list</div>
                                        </div>

                                        <div class="row">
                                            <label id="select_assign_to_user-Required" style="color:red;margin-top: 14px; font-size:10px; text-align:center;"></label>
                                        </div>
                                    </div>
                                </div>

                                <div class="row">
                                    <div class="col-md-12">
                                        <label style="margin-left:10px"><b>Description:</b></label>
                                        <textarea class="form-control" rows="3" name="tblRpaRequestStr{}description" maxlength="2000">${jsonData.description}</textarea>
                                        <small>Max size of the description should be 2000 characters</small>
                                    </div>
                                </div>
                                <br />

                                <div class="row">
                                    <div class="col-md-6">
                                        <label style="margin-left:10px" class="required">Upload EXE</label>
                                        <input style="margin-left:10px" class="btn btn-default" type="file"  accept=".zip" name="exefile">
                                    </div>
                                    <div class="col-md-6">
                                        <label style="margin-left:10px" class="required">Upload code zip : </label>
                                        <input class="btn btn-default" type="file"  accept=".zip" name="codefile">
                                    </div>
                                </div>
                                <hr />

                                <div class="col-md-2" style="margin: 0px;padding-left: 4px;">
                                    <div class="checkbox-container">
                                        <label class="checkbox-label">
                                            <input class="inputRequiredCheckboxOwnDev" name="tblRpaRequestStr{}isInputRequired" type="checkbox" onchange="toggleCheckbox(this)">
                                            <span class="checkbox-custom rectangular"></span>
                                        </label>
                                    </div>
                                </div>

                                <div>
                                    <label class="forLabel inputLabel">Bot Input Required</label>
                                </div>

                                <div class="row infile">
                                    <label style="margin-left:10px" class="required">Input Sample :</label>
                                    <input style="margin-left:10px" class="inputDiv btn btn-default" type="file"  accept=".zip" name="infile">
                                </div>
                                <div class="row">
                                    <label style="margin-left:10px" class="required">Output Sample :</label>
                                    <input style="margin-left:10px" class="btn btn-default" type="file"  accept=".zip" name="outfile">
                                </div>
                                <div class="row">
                                    <label style="margin-left:10px" class="required">Logic Doc :</label>
                                    <input style="margin-left:10px" class="btn btn-default" type="file"  accept=".zip" name="logicfile">
                                </div>
                                <hr />
                                                    
                                <h5><small>NOTE: Zip format allowed for file upload.</small></h5>
                    
                                <div class="row">
                                    <div class="pull-right">
                                        <button type="submit"  class="btn btn-sm btn-success" style="margin-left:10px">Update</button>
                                        <button id="closeModal" type="button" class="btn btn-sm btn-default" data-dismiss="modal">Close</button>
                                    </div>
                                </div>
                            </form>
                        </div>`;
}

function EditBotHTMLForNewStatus(jsonData) {
    return `<form method="POST" enctype="multipart/form-data" id="UpdateBotFrm">
                        <div id="newRequestHidden_tblRpaRequestStr"></div>
                        <input type="hidden" class="form-control" name="tblRpaRequestStr{}rpaRequestId" value="${jsonData.requestId}"  />
                        <input type="hidden" class="form-control" name="tblRpaRequestStr{}modifiedBy" value="${signumGlobal}"  />

                        <div class="row">
                            <div class="col-md-4">
                                <label style="margin-left:10px" class="required">Automation Name:</label>
                                <input type="text" class="form-control" name="tblRpaRequestStr{}requestName" value="${jsonData.requestName}" required />
                            </div>
                            
                            <div class="col-md-4">
                                <label style="margin-left:10px" class="required">Manual Execution Time (Min.):</label>
                                <input type="number" class="form-control" name="tblRpaRequestStr{}currentAvgExecutiontime" value="${jsonData.currentAvgExecutiontime}" min="0" />
                            </div>
                            
                            <div class="col-md-4">
                                <label style="margin-left:10px" class="required">Weekly Execution Count:</label>
                                <input type="number" class="form-control" name="tblRpaRequestStr{}currentExecutioncountWeekly" value="${jsonData.currentExecutioncountWeekly}" min="0" />
                            </div>
                        </div>
                        <hr />
                        
                        <div class="row">
                            <div class="col-lg-6" style="margin-bottom:0px; overflow: auto;">
                                <div class="form-group">
                                    <label style="margin-left:10px">Spoc(Signum):</label> <a onclick="getAllUsers()" id="allUserAnchor" style="color:blue;font-size:10px;cursor:pointer"><i class="fa fa-lock" style="color:black"></i>(Click to unlock all users)</a>&nbsp;&nbsp;<span style="color:darkred;font-size:10px;">Edit Assigned SPOC for Request</span>
                                    <div style="width: 20px;  margin-left: 1px; margin-top: -25px; font-size: 17px; color:red; ">*</div>
                                    <input id="select_assign_to_user" style='width:100%' placeholder="Signum/Name" value="${jsonData.spoc}"  name="tblRpaRequestStr{}spocsignum" readonly/>
                                    <br />
                                    <b>Minimum 3 characters required.(choose signum from the list)</b>
                                    <div id="emptysignum-message" style="display:none; color: red">Please seletct an Signum from the list</div>
                                </div>

                                <div class="row">
                                    <label id="select_assign_to_user-Required" style="color:red;margin-top: 14px; font-size:10px; text-align:center;"></label>
                                </div>
                            </div>
                        </div>
                        <hr />
                                        
                        <div class="col-md-2" style="margin: 0px;padding-left: 4px;">
                            <div class="checkbox-container">
                                <label class="checkbox-label">
                                    <input class="inputRequiredCheckboxOwnDev" name="tblRpaRequestStr{}isInputRequired" type="checkbox" onchange="toggleCheckbox(this)">
                                    <span class="checkbox-custom rectangular"></span>
                                </label>
                            </div>
                        </div>
                        
                        <div>
                            <label class="forLabel inputLabel">Bot Input Required</label>
                        </div>

                        <div class="row infile">
                            <div class="col-md-4">
                                <label style="margin-left:10px" class="required">Input Sample :</label>
                                <input class="inputDiv btn btn-default" type="file" id="inputFile" name="infile"  accept=".zip">
                            </div>
                        </div>
                        
                        <div class="row">
                            <div class="col-md-4">
                                <label style="margin-left:10px" class="required">Output Sample :</label>
                                <input class="btn btn-default" type="file" id="outputFile" name="outfile" accept=".zip">
                            </div>
                        </div>

                        <div class="row">
                            <div class="col-md-4">
                                <label style="margin-left:10px" class="required">Logic Doc :</label>
                                <input class="btn btn-default" type="file" id="logicFile" name="logicFile"  accept=".zip">
                            </div>
                        </div>
                        <hr />

                        <div class="row">
                            <div class="col-md-12">
                                <label style="margin-left:10px">Description:</label>
                                <textarea class="form-control" rows="3" name="tblRpaRequestStr{}description" maxlength="2000">${jsonData.description}</textarea>
                                <small>Max size of the description should be 2000 characters</small>
                            </div>
                        </div>
                        <hr />

                        <h5><small>NOTE: Zip format allowed for file upload.</small></h5>

                        <div class="row">
                            <div class="pull-right">
                                <button type="submit" id="updateBotRequest" class="btn btn-sm btn-success">Update Request</button>
                                <button id="closeModal" type="button" class="btn btn-sm btn-default" data-dismiss="modal">Close</button>
                            </div>
                        </div>
                    </form>`;
}

function EditBotHTMLForMacro(jsonData) {
    return ` <div id="macroDiv">
                                <form method="POST" enctype="multipart/form-data" id="updateBotMacroFrm">
                                <input type="hidden" class="form-control" name="tblRpaRequestStr{}rpaRequestId" value="${jsonData.requestId}"  />
                                <input type="hidden" class="form-control" name="tblRpaRequestStr{}requestName" value="${jsonData.requestName}"  />
                                <input type="hidden" class="form-control" name="tblRpaBotstagingStr{}modifiedBy" value="${signumGlobal}"  />
                                <input type="hidden" class="form-control" name="tblRpaRequestStr{}modifiedBy" value="${signumGlobal}"  />
                                <input type="hidden" class="form-control" name="tblRpaBotstagingStr{}statusDescription" value="ByUser" />
                                <input type="hidden" class="form-control" name="tblRpaBotstagingStr{}botname" value="${jsonData.botname}"  />
                                <input type="hidden" class="form-control" name="tblRpaBotstagingStr{}rpaexecutionTime" value="${jsonData.rpaexecutionTime}"  />
                                <input type="hidden" class="form-control" name="tblRpaBotstagingStr{}parallelWoexecution" value="${jsonData.parallelWoexecution}"  />
                                <input type="hidden" class="form-control" name="tblRpaBotstagingStr{}botlanguage" value="${jsonData.language}"  />

                                <div id="botMacroHidden_tblRpaRequestStr"></div>

                                <div id="botMacroHidden_tblRpaBotstagingStr"></div>

                                <div class="row">
                                    <div class="col-md-4">
                                        <label style="margin-left:10px" class="required">Automation Name:</label>
                                        <input type="text" class="form-control" name="tblRpaRequestStr{}botname"  value="${jsonData.botname}" />
                                    </div>

                                    <div class="col-md-4">
                                        <label style="margin-left:10px" class="required">Manual Execution Time (Min.):</label>
                                        <input type="number" class="form-control" name="tblRpaRequestStr{}currentAvgExecutiontime"  min="0" value="${jsonData.currentAvgExecutiontime}"/>
                                    </div>

                                    <div class="col-md-4">
                                        <label style="margin-left:10px" class="required">Weekly Execution Count:</label>
                                        <input type="number" class="form-control" name="tblRpaRequestStr{}currentExecutioncountWeekly"  min="0" value="${jsonData.currentExecutioncountWeekly}" />
                                    </div>
                                </div>
                                <hr />

                                <div class="row">
                                    <div class="col-lg-6" style="margin-bottom:0px; overflow: auto;">
                                        <div class="form-group">
                                            <label style="margin-left:10px">Spoc(Signum):</label> <a onclick="getAllUsers()" id="allUserAnchor" style="color:blue;font-size:10px;cursor:pointer"><i class="fa fa-lock" style="color:black"></i>(Click to unlock all users)</a>&nbsp;&nbsp;<span style="color:darkred;font-size:10px;">Edit Assigned SPOC for Request</span>
                                                <div style="width: 20px;  margin-left: 1px; margin-top: -25px; font-size: 17px; color:red; ">*</div>

                                                <input id="select_assign_to_user" style='width:100%' placeholder="Signum/Name" value="${jsonData.spoc}"  name="tblRpaRequestStr{}spocsignum" readonly/>
                                                <br />
                                                <b>Minimum 3 characters required.(choose signum from the list)</b>
                                                <div id="emptysignum-message" style="display:none; color: red">Please seletct an Signum from the list</div>
                                </div>

                                <div class="row">
                                    <label id="select_assign_to_user-Required" style="color:red;margin-top: 14px; font-size:10px; text-align:center;"></label>
                                </div>
                            </div>
                        </div>

                        <div class="row">
                            <div class="col-md-12">
                                <label style="margin-left:10px"><b>Description:</b></label>
                                <textarea class="form-control" rows="3" name="tblRpaRequestStr{}description" maxlength="2000">${jsonData.description}</textarea>
                                <small>Max size of the description should be 2000 characters</small>
                            </div>
                        </div>
                        <hr />

                        <div class="row">
                            <div class="col-md-4">
                                <label style="margin-left:10px" class="required">Upload Macro File</label>
                                <input style="margin-left:10px" class="btn btn-default" id="macroCodeFile" type="file"  name="codefile" accept=".zip" >
                               
                            </div>
                        </div>

                        <div class="row">
                            <div class="col-md-4">
                                <label style="margin-left:10px" class="required">Module Name : </label>
                                <input class="form-control" type="text"  name="tblRpaBotstagingStr{}moduleClassName"  value="${jsonData.classname}">
                                <small>Min 1 and max 100 (No special chracters allowed)</small>
                            </div>

                            <div class="col-md-4">
                                <label style="margin-left:10px" class="required">Module Method Name :</label>
                                <input class="form-control" type="text"  name="tblRpaBotstagingStr{}moduleClassMethod"  value="${jsonData.methodname}">
                                <small>Min 1 and max 20 (No special chracters allowed)</small>
                            </div>
                        </div>
                        <hr />

                        <div class="col-md-2" style="margin: 0px;padding-left: 4px;">
                            <div class="checkbox-container">
                                <label class="checkbox-label">
                                    <input class="inputRequiredCheckboxOwnDev" name="tblRpaRequestStr{}isInputRequired" type="checkbox" onchange="toggleCheckbox(this)">
                                    <span class="checkbox-custom rectangular"></span>
                                </label>
                            </div>
                        </div>

                        <div>
                            <label class="forLabel inputLabel">Bot Input Required</label>
                        </div>
                        <hr />

                        <div class="row infile">
                            <label style="margin-left:10px" class="required">Input Sample :</label>
                            <input style="margin-left:10px" class="inputDiv btn btn-default" type="file"  accept=".zip" name="infile">
                        </div>
                        <div class="row">
                            <label style="margin-left:10px" class="required">Output Sample :</label>
                            <input style="margin-left:10px" class="btn btn-default" type="file"  accept=".zip" name="outfile">
                        </div>
                        <div class="row">
                            <label style="margin-left:10px" class="required">Logic Doc :</label>
                            <input style="margin-left:10px" class="btn btn-default" type="file"  accept=".zip" name="logicfile">
                        </div>
                                                   
                        <h5><small>NOTE: Zip format allowed for file upload.</small></h5>
                        <div class="row">
                            <div class="pull-right">
                                <button type="submit" class="btn btn-sm btn-success" style="margin-left:10px">SUBMIT</button>
                                <button id="closeModal" type="button" class="btn btn-sm btn-default" data-dismiss="modal">Close</button>
                            </div>
                         </div>
                    </form>
                </div>`;
}

function EditBotHTMLForJava(jsonData) {
    return `<form method="POST" enctype="multipart/form-data" id="updateBotJavaFrm">
                            <input type="hidden" class="form-control" name="tblRpaRequestStr{}rpaRequestId" value="${jsonData.requestId}"  />
                            <input type="hidden" class="form-control" name="tblRpaRequestStr{}requestName" value="${jsonData.requestName}"  />
                            <input type="hidden" class="form-control" name="tblRpaBotstagingStr{}modifiedBy" value="${signumGlobal}"  />
                            <input type="hidden" class="form-control" name="tblRpaRequestStr{}modifiedBy" value="${signumGlobal}"  />
                            <input type="hidden" class="form-control" name="tblRpaBotstagingStr{}statusDescription" value="ByUser" />
                            <input type="hidden" class="form-control" name="tblRpaRequestStr{}currentAvgExecutiontime" value="${jsonData.currentAvgExecutiontime}"  />
                            <input type="hidden" class="form-control" name="tblRpaBotstagingStr{}parallelWoexecution" value="${jsonData.parallelWoexecution}"  />
                            <input type="hidden" class="form-control" name="tblRpaBotstagingStr{}botlanguage" value="${jsonData.language}"  />

                            <div id="botJavaHidden_tblRpaRequestStr"></div>
                            <div id="botJavaHidden_tblRpaBotstagingStr"></div>
                            <div id="botJavaHidden_tblRpaBotstagingStr1"></div>

                            <div class="row">
                                <div class="col-md-4">
                                    <label style="margin-left:10px" class="required">Automation Name:</label>
                                    <input type="text" class="form-control" name="tblRpaRequestStr{}botname"  value="${jsonData.botname}"/>
                                </div>

                                <div class="col-md-4">
                                    <label style="margin-left:10px" class="required">Manual Execution Time (Min.):</label>
                                    <input type="number" class="form-control" name="tblRpaRequestStr{}currentAvgExecutiontime"  min="0" value="${jsonData.currentAvgExecutiontime}"/>
                                </div>

                                <div class="col-md-4">
                                    <label style="margin-left:10px" class="required">Weekly Execution Count:</label>
                                    <input type="number" class="form-control" name="tblRpaRequestStr{}currentExecutioncountWeekly" min="0" value="${jsonData.currentExecutioncountWeekly}" />
                                </div>
                            </div>
                            <hr />

                            <div class="row">
                                <div class="col-lg-6" style="margin-bottom:0px; overflow: auto;">
                                    <div class="form-group">
                                        <label style="margin-left:10px">Spoc(Signum):</label> <a onclick="getAllUsers()" id="allUserAnchor" style="color:blue;font-size:10px;cursor:pointer"><i class="fa fa-lock" style="color:black"></i>(Click to unlock all users)</a>&nbsp;&nbsp;<span style="color:darkred;font-size:10px;">Edit Assigned SPOC for Request</span>

                                        <div style="width: 20px;  margin-left: 1px; margin-top: -25px; font-size: 17px; color:red; ">*</div>

                                        <input id="select_assign_to_user" style='width:100%' placeholder="Signum/Name" value="${jsonData.spoc}"  name="tblRpaRequestStr{}spocsignum" readonly/>
                                        <br />
                                        <b>Minimum 3 characters required.(choose signum from the list)</b>

                                        <div id="emptysignum-message" style="display:none; color: red">Please seletct an Signum from the list</div>
                                    </div>

                                    <div class="row">
                                        <label id="select_assign_to_user-Required" style="color:red;margin-top: 14px; font-size:10px; text-align:center;"></label>
                                    </div>
                                </div>
                            </div>

                            <div class="row">
                                <div class="col-md-12">
                                    <label style="margin-left:10px"><b>Description:</b></label>
                                    <textarea class="form-control" rows="3" name="tblRpaRequestStr{}description" maxlength="2000">${jsonData.description}</textarea>
                                    <small>Max size of the description should be 2000 characters</small>
                                </div>
                            </div>
                            <br />

                            <div class="row">
                                <div class="col-md-6">
                                    <label style="margin-left:10px" class="required">Java Class Name:</label>
                                    <input class="form-control" type="text" name="tblRpaBotstagingStr{}moduleClassName"  value="${jsonData.classname}">                                                       
                                    <small>Min 1 and max 100 (No special chracters allowed)</small>
                                </div>
                            </div>

                            <div class="row">
                                <div class="col-md-6">
                                    <label style="margin-left:10px" class="required">Upload BOT Jar File</label>
                                    <input style="margin-left:10px" class="btn btn-default" type="file"  accept=".zip" name="exefile">
                                </div>

                                <div class="col-md-6">
                                    <label style="margin-left:10px" class="required">Upload code zip : </label>
                                    <input class="btn btn-default" type="file" accept=".zip" name="codefile">
                                </div>
                            </div>

                            <hr />
                            <div class="col-md-2" style="margin: 0px;padding-left: 4px;">
                                <div class="checkbox-container">
                                    <label class="checkbox-label">
                                        <input class="inputRequiredCheckboxOwnDev" name="tblRpaRequestStr{}isInputRequired" type="checkbox" onchange="toggleCheckbox(this)">
                                        <span class="checkbox-custom rectangular"></span>
                                    </label>
                                </div>
                            </div>

                            <div>
                                <label class="forLabel inputLabel">Bot Input Required</label>
                            </div>

                            <div class="row infile">
                                <label style="margin-left:10px" class="required">Input Sample :</label>
                                <input style="margin-left:10px" class="inputDiv btn btn-default" type="file"  accept=".zip" name="infile">
                            </div>
                            <div class="row">
                                <label style="margin-left:10px" class="required">Output Sample :</label>
                                <input style="margin-left:10px" class="btn btn-default" type="file"  accept=".zip" name="outfile">
                            </div>
                            <div class="row">
                                <label style="margin-left:10px" class="required">Logic Doc :</label>
                                <input style="margin-left:10px" class="btn btn-default" type="file"  accept=".zip" name="logicfile">
                            </div>
                                                   
                            <hr />
                            <h5><small>NOTE: Zip format allowed for file upload.</small></h5>
                            <div class="row">
                                <div class="pull-right">
                                    <button type="submit" class="btn btn-sm btn-success" style="margin-left:10px">Update</button>
                                    <button id="closeModal" type="button" class="btn btn-sm btn-default" data-dismiss="modal">Close</button>
                                </div>
                            </div>
                        </form>`;
}

function EditBotHTMLForDotNet(jsonData) {
    return `<div id="DotNetDiv">
 <form method="POST" enctype="multipart/form-data" id="updateBotDotNetFrm">
<input type="hidden" name="tblRpaBotstagingStr{}botlanguage" id="hiddenLanguageNameForBotNewRequest_DotNet" />
<input type="hidden" class="form-control" name="tblRpaRequestStr{}rpaRequestId" value="${jsonData.requestId}"  />
<input type="hidden" class="form-control" name="tblRpaRequestStr{}requestName" value="${jsonData.requestName}"  />
<input type="hidden" class="form-control" name="tblRpaRequestStr{}currentAvgExecutiontime" value="${jsonData.currentAvgExecutiontime}"  />
<input type="hidden" class="form-control" name="tblRpaBotstagingStr{}modifiedBy" value="${signumGlobal}"  />
<input type="hidden" class="form-control" name="tblRpaRequestStr{}modifiedBy" value="${signumGlobal}"  />
<input type="hidden" class="form-control" name="tblRpaBotstagingStr{}moduleClassName" value="${jsonData.classname}"  />
<input type="hidden" class="form-control" name="tblRpaBotstagingStr{}moduleClassMethod" value="${jsonData.methodname}"  />                                                   <input type="hidden" class="form-control" name="tblRpaBotstagingStr{}statusDescription" value="ByUser" />
<input type="hidden" class="form-control" name="tblRpaBotstagingStr{}parallelWoexecution" value="${jsonData.parallelWoexecution}"  />
<input type="hidden" class="form-control" name="tblRpaBotstagingStr{}botlanguage" value="${jsonData.language}"  />
                                                    <div id="botDotNetHidden_tblRpaRequestStr"></div>
                                                    <div id="botDotNetHidden_tblRpaBotstagingStr"></div>
                                                    <div id="botDotNetHidden_tblRpaBotstagingStr1"></div>

                                                    <div class="row">

                                                        <div class="col-md-4">
                                                            <label style="margin-left:10px" class="required">Automation Name:</label>
                                                            <input type="text" class="form-control" name="tblRpaRequestStr{}botname" value="${jsonData.botname}" />
                                                        </div>
                                                        <div class="col-md-4">
                                                            <label style="margin-left:10px" class="required">Manual Execution Time (Min.):</label>
                                                            <input type="number" class="form-control" name="tblRpaRequestStr{}currentAvgExecutiontime" min="0" value="${jsonData.currentAvgExecutiontime}"/>
                                                        </div>
                                                        <div class="col-md-4">
                                                            <label style="margin-left:10px" class="required">Weekly Execution Count:</label>
                                                            <input type="number" class="form-control" name="tblRpaRequestStr{}currentExecutioncountWeekly"  min="0" value="${jsonData.currentExecutioncountWeekly}"/>
                                                        </div>
                                                    </div>
                                                    <hr />
<div class="row">
                        <div class="col-lg-6" style="margin-bottom:0px; overflow: auto;">
                            <div class="form-group">
                                <label style="margin-left:10px">Spoc(Signum):</label> <a onclick="getAllUsers()" id="allUserAnchor" style="color:blue;font-size:10px;cursor:pointer"><i class="fa fa-lock" style="color:black"></i>(Click to unlock all users)</a>&nbsp;&nbsp;<span style="color:darkred;font-size:10px;">Edit Assigned SPOC for Request</span>
                                <div style="width: 20px;  margin-left: 1px; margin-top: -25px; font-size: 17px; color:red; ">*</div>

                               <input id="select_assign_to_user" style='width:100%' placeholder="Signum/Name" value="${jsonData.spoc}"  name="tblRpaRequestStr{}spocsignum" readonly/>

                                    <br />
                                    <b>Minimum 3 characters required.(choose signum from the list)</b>
                               
<div id="emptysignum-message" style="display:none; color: red">Please seletct an Signum from the list</div>
                            </div>

                            <div class="row">
                                <label id="select_assign_to_user-Required" style="color:red;margin-top: 14px; font-size:10px; text-align:center;"></label>
                            </div>
                        </div>
                    </div>
                                                    <div class="row">
                                                        <div class="col-md-12">
                                                            <label style="margin-left:10px"><b>Description:</b></label>
                                                            <textarea class="form-control" rows="3" name="tblRpaRequestStr{}description" maxlength="2000">${jsonData.description}</textarea>
                                                            <small>Max size of the description should be 2000 characters</small>
                                                        </div>
                                                    </div><br />

                                                    <div class="row">
                                                        <div class="col-md-6">
                                                            <label style="margin-left:10px" class="required">Upload EXE</label>
                                                            <input style="margin-left:10px" class="btn btn-default" type="file"  accept=".zip" name="exefile">
                                                        </div>
                                                        <div class="col-md-6">
                                                            <label style="margin-left:10px" class="required">Upload code zip : </label>
                                                            <input class="btn btn-default" type="file"  accept=".zip" name="codefile">
                                                        </div>
                                                    </div>

                                                    <hr />
                                                    <div class="col-md-2" style="margin: 0px;padding-left: 4px;">
                                                        <div class="checkbox-container">
                                                            <label class="checkbox-label">
                                                                <input class="inputRequiredCheckboxOwnDev" name="tblRpaRequestStr{}isInputRequired" type="checkbox" onchange="toggleCheckbox(this)">
                                                                <span class="checkbox-custom rectangular"></span>
                                                            </label>
                                                        </div>
                                                    </div>
                                                    <div>
                                                        <label class="forLabel inputLabel">Bot Input Required</label>
                                                    </div>

                                                        <div class="row infile">
                                                            <label style="margin-left:10px" class="required">Input Sample :</label>
                                                            <input style="margin-left:10px" class="inputDiv btn btn-default" type="file"  accept=".zip" name="infile">
                                                        </div>
                                                        <div class="row">
                                                            <label style="margin-left:10px" class="required">Output Sample :</label>
                                                            <input style="margin-left:10px" class="btn btn-default" type="file"  accept=".zip" name="outfile">
                                                        </div>
                                                        <div class="row">
                                                            <label style="margin-left:10px" class="required">Logic Doc :</label>
                                                            <input style="margin-left:10px" class="btn btn-default" type="file"  accept=".zip" name="logicfile">
                                                        </div>

                                                 

                                                    <hr />
                                                    <h5><small>NOTE: Zip format allowed for file upload.</small></h5>
                                                    <div class="row">
                                                        <div class="pull-right">
                                                            <button type="submit" class="btn btn-sm btn-success" style="margin-left:10px">Update</button>
<button id="closeModal" type="button" class="btn btn-sm btn-default" data-dismiss="modal">Close</button>
                                                            
                                                        </div>


                                                    </div>
                                                </form>
                                            </div>`;
}

function EditBotHTMLForPython(jsonData) {
    return `<div id="pythonDiv">
                            <form method="POST" enctype="multipart/form-data" id="updateBotPythonFrm">
                                <input type="hidden" name="tblRpaBotstagingStr{}botlanguage" id="hiddenLanguageNameForBotNewRequest_Python" />
                                <input type="hidden" class="form-control" name="tblRpaRequestStr{}rpaRequestId" value="${jsonData.requestId}"  />
                                <input type="hidden" class="form-control" name="tblRpaRequestStr{}requestName" value="${jsonData.requestName}"  />
                                <input type="hidden" class="form-control" name="tblRpaRequestStr{}currentAvgExecutiontime" value="${jsonData.currentAvgExecutiontime}"  />
                                <input type="hidden" class="form-control" name="tblRpaBotstagingStr{}modifiedBy" value="${signumGlobal}"  />
                                <input type="hidden" class="form-control" name="tblRpaBotstagingStr{}botname" value="${jsonData.botname}"  />
                                <input type="hidden" class="form-control" name="tblRpaRequestStr{}modifiedBy" value="${signumGlobal}"  />
                                <input type="hidden" class="form-control" name="tblRpaBotstagingStr{}moduleClassName" value="${jsonData.classname}"  />
                                <input type="hidden" class="form-control" name="tblRpaBotstagingStr{}moduleClassMethod" value="${jsonData.methodname}"  />                                                   <input type="hidden" class="form-control" name="tblRpaBotstagingStr{}statusDescription" value="ByUser" />
                                <input type="hidden" class="form-control" name="tblRpaBotstagingStr{}parallelWoexecution" value="${jsonData.parallelWoexecution}"  />
                                <input type="hidden" class="form-control" name="tblRpaBotstagingStr{}botlanguage" value="${jsonData.language}"  />

                                <div id="botPythonHidden_tblRpaRequestStr"></div>
                                <div id="botPythonHidden_tblRpaBotstagingStr"></div>
                                <div id="botPythonHidden_tblRpaBotstagingStr1"></div>

                                <div class="row">
                                    <div class="col-md-4">
                                        <label style="margin-left:10px" class="required">Automation Name:</label>
                                        <input type="text" id='botName' class="form-control" name="tblRpaRequestStr{}botname" value="${jsonData.botname}" />
                                    </div>

                                    <div class="col-md-4">
                                        <label style="margin-left:10px" class="required">Manual Execution Time (Min.):</label>
                                        <input type="number" class="form-control" name="tblRpaRequestStr{}currentAvgExecutiontime" min="0" value="${jsonData.currentAvgExecutiontime}"/>
                                    </div>

                                    <div class="col-md-4">
                                        <label style="margin-left:10px" class="required">Weekly Execution Count:</label>
                                        <input type="number" class="form-control" name="tblRpaRequestStr{}currentExecutioncountWeekly"  min="0" value="${jsonData.currentExecutioncountWeekly}"/>
                                    </div>
                                </div>
                                <hr />

                                <div class="row">
                                    <div class="col-lg-6" style="margin-bottom:0px; overflow: auto;">
                                        <div class="form-group">
                                            <label style="margin-left:10px">Spoc(Signum):</label> <a onclick="getAllUsers()" id="allUserAnchor" style="color:blue;font-size:10px;cursor:pointer"><i class="fa fa-lock" style="color:black"></i>(Click to unlock all users)</a>&nbsp;&nbsp;<span style="color:darkred;font-size:10px;">Edit Assigned SPOC for Request</span>
                                            <div style="width: 20px;  margin-left: 1px; margin-top: -25px; font-size: 17px; color:red; ">*</div>

                                            <input id="select_assign_to_user" style='width:100%' placeholder="Signum/Name" value="${jsonData.spoc}"  name="tblRpaRequestStr{}spocsignum" readonly/>
                                            <br />
                                            <b>Minimum 3 characters required.(choose signum from the list)</b>
                               
                                            <div id="emptysignum-message" style="display:none; color: red">Please seletct an Signum from the list</div>
                                        </div>

                                        <div class="row">
                                            <label id="select_assign_to_user-Required" style="color:red;margin-top: 14px; font-size:10px; text-align:center;"></label>
                                        </div>
                                    </div>
                                </div>

                                <div class="row">
                                    <div class="col-md-12">
                                        <label style="margin-left:10px"><b>Description:</b></label>
                                        <textarea class="form-control" rows="3" name="tblRpaRequestStr{}description" maxlength="2000">${jsonData.description}</textarea>
                                        <small>Max size of the description should be 2000 characters</small>
                                    </div>
                                </div><br />

                                <div class="row">
                                    <div class="col-md-6">
                                       <label style = "color: red; font-weight: 300;" >*</label> <label style="margin-left:10px" class="required">Upload WHL File</label>
                                        <input style="margin-left:10px; text-transform: lowercase !important;" class="btn btn-default" type="file"  accept=".whl" id="editBotWHLFile" name="whlfile">
                                    </div>
                                    <div class="col-md-6">
                                        <label style="margin-left:10px" class="required">Upload code zip : </label>
                                        <input class="btn btn-default" type="file"  accept=".zip" name="codefile">
                                    </div>
                                </div>

                                <hr />

                                <div class="col-md-2" style="margin: 0px;padding-left: 4px;">
                                    <div class="checkbox-container">
                                        <label class="checkbox-label">
                                            <input class="inputRequiredCheckboxOwnDev" name="tblRpaRequestStr{}isInputRequired" type="checkbox" onchange="toggleCheckbox(this)">
                                            <span class="checkbox-custom rectangular"></span>
                                        </label>
                                    </div>
                                </div>

                                <div>
                                    <label class="forLabel inputLabel">Bot Input Required</label>
                                </div>

                                <div class="row infile">
                                    <label style="margin-left:10px" class="required">Input Sample :</label>
                                    <input style="margin-left:10px" class="inputDiv btn btn-default" type="file"  accept=".zip" name="infile">
                                </div>
                                <div class="row">
                                    <label style="margin-left:10px" class="required">Output Sample :</label>
                                    <input style="margin-left:10px" class="btn btn-default" type="file"  accept=".zip" name="outfile">
                                </div>
                                <div class="row">
                                    <label style="margin-left:10px" class="required">Logic Doc :</label>
                                    <input style="margin-left:10px" class="btn btn-default" type="file"  accept=".zip" name="logicfile">
                                </div>

													<div class="row">
                                                            <div class="col-md-6">
                                                                <label style = "color: red; font-weight: 300;" >*</label> <label>Python Base Version :</label>
                                                                <select class="select2able select2-offscreen " name= "selectVersionEditBot" id="selectVersionEditBot" data-selectnext="0" tabindex="-1" aria-hidden="true">
                                                                </select>
                                                            </div>
                                                            <div class="col-md-6">
																<label style = "color: red; font-weight: 300;" >*</label> <label>Package Name :</label>
                                                                <input type="text" class="form-control" id="editBotPackageName" name="packageName" style="display: block;">
                                                            </div>
														</div>

                                <hr />
                                <h5><small>NOTE: Zip format allowed for file upload.For Python Bots .whl file need to be Uploaded</small></h5>
                                <div class="row">
                                    <div class="pull-right">
                                        <button type="submit" class="btn btn-sm btn-success" style="margin-left:10px">Update</button>
                                        <button id="closeModal" type="button" class="btn btn-sm btn-default" data-dismiss="modal">Close</button>
                                    </div>
                                </div>
                            </form>
                        </div>`;
}

// toggel checkbox
function toggleCheckbox(element) {
    var inputRqdStr = 'Bot Input Required';
    var inputNotRqdStr = 'Bot Input Not Required';
    var greenColor = '#1fb65b';
    var greyColor = '#868598';

    if (!element.checked) {
        $('.infile').addClass("disabledbutton");
        $('.inputLabel').css("color", greyColor);
        $('.inputLabel').text(inputNotRqdStr);
        $('.inputDiv').removeAttr("required");
        $('.inputDiv').removeClass("required");
    } else {
        $('.infile').removeClass("disabledbutton");
        $('.inputLabel').css("color", greenColor);
        $('.inputLabel').text(inputRqdStr);
        $('.inputDiv').attr("required", true);
        $('.inputRequiredCheckboxOwnDev').attr('checked', element.checked);
    }
}

function getTableAction(data, jsonData, jsonForm) {
    const rowData = data;
    const iconsEditVideoUrlHtml = hideAndShowIconsForMacro(rowData, jsonForm);
    if ((jsonData.status === 'TESTED' || jsonData.status === 'ACCEPTED' || jsonData.status === 'ASSIGNED' || jsonData.status === 'Assigned' || jsonData.status === 'NA' || jsonData.status === 'DEPLOYED' || jsonData.status === 'REJECTED') && jsonData.language && jsonData.language.toLowerCase() === "penguin") {
        return `<div style='display:flex' ><a class='icon-view' title='View' onclick="viewBotDetails(${rowData.rpaRequestId},'${rowData.tblProjects.projectName}','${jsonData.status}')" data-toggle='modal' href='#requestInfo'><i class='fa fa-eye'></i></a><a class='icon-delete lsp' onclick=deleteBotRequest(${rowData.rpaRequestId},"${signumGlobal}") title='Delete' href='#'><i class='fa fa-trash'></i></a> <a class='icon-view lsp' onclick=getVersionFlowChart(${rowData.tblProjects.projectId},${rowData.subactivityId},${rowData.workflowDefid}) title='View Workflow' href='#'><i class='fa fa-code-fork'></i></a></div>`;
    }
    if (jsonData.status === 'REJECTED') {
        return getRejectActionIcons(rowData, jsonData, jsonForm);
    }
    else {
        if (jsonData.status === 'DEPLOYED') {
            return getDeployedBotActionIcons(rowData, jsonData);
        }
        if (jsonData.status === 'APPROVED' || jsonData.status === 'Approved') {

            return `<div style='display:flex'>
				<a class='icon-view' title = 'View' onclick="viewBotDetails(${rowData.rpaRequestId},'${rowData.tblProjects.projectName}','${jsonData.status}')" data-toggle='modal' href = '#requestInfo' > <i class='fa fa-eye'></i></a >
                 <a class='icon-view lsp' onclick=getVersionFlowChart(${rowData.tblProjects.projectId},${rowData.subactivityId},${rowData.workflowDefid}) title='View Workflow' href='#'><i class='fa fa-code-fork'></i></a>
                ${iconsEditVideoUrlHtml}`;
        }
        else {
            return getAcceptedTestedActionIcons(rowData, jsonData, jsonForm);
        }
    }
}
function getRejectActionIcons(rowdata, jsonData, jsonForm) {
    const data = rowdata;
    const iconsEditVideoUrlHtml = hideAndShowIconsForMacro(rowdata, jsonForm);
    return `<div style='display:flex'>
                                   <a class='icon-view' title='View' onclick="viewBotDetails(${data.rpaRequestId},'${data.tblProjects.projectName}','${jsonData.status}')" data-toggle='modal' href='#requestInfo'><i class='fa fa-eye'></i></a>
                                    <a class='icon-delete lsp' onclick=deleteBotRequest(${data.rpaRequestId},"${signumGlobal}") title='Delete' href='#'><i class='fa fa-trash'></i>
                                    </a> <a class='icon-view lsp' onclick=getVersionFlowChart(${data.tblProjects.projectId},${data.subactivityId},${data.workflowDefid}) title='View Workflow' href='#'>
                                    <i class='fa fa-code-fork'></i></a>
                                    ${iconsEditVideoUrlHtml}`;
}
function getAcceptedTestedActionIcons(rowdata, jsonData, jsonForm) {
    const data = rowdata;
    const iconsEditVideoUrlHtml = hideAndShowIconsForMacro(data, jsonForm);
    return `<div style='display:flex' ><a class='icon-view' title='View' onclick="viewBotDetails(${data.rpaRequestId},'${data.tblProjects.projectName}','${jsonData.status}')" data-toggle='modal' href='#requestInfo'><i class='fa fa-eye'></i></a><a class='icon-delete lsp' onclick=deleteBotRequest(${data.rpaRequestId},"${signumGlobal}") title='Delete' href='#'>
<i class='fa fa-trash'></i></a> 
<a class='icon-view lsp' onclick=getVersionFlowChart(${data.tblProjects.projectId},${data.subactivityId},${data.workflowDefid}) title='View Workflow' href='#'>
<i class='fa fa-code-fork'></i></a>${iconsEditVideoUrlHtml}`;
}
function getEditUploadDisplayAction(rowdata, jsonForm) {
    const data = rowdata;
    return `<a class='icon-edit lsp' onclick='updateReqData(this)' data-details='${jsonForm}' title='Edit Request' href='#UpdateInfo' data-toggle='modal'><i class='fa fa-edit'></i></a>
        <a class='icon-edit lsp' onclick='uploadURL(${data.rpaRequestId})' title='Upload Demo URL' href='#'><i class='fa fa-upload'></i></a></div >`;
}
function getEditUploadHideAction(rowdata, jsonForm) {
    const data = rowdata;
    return `<a class='icon-edit lsp' style='display:none;' onclick='updateReqData(this)' data-details='${jsonForm}' title='Edit Request' href='#UpdateInfo' data-toggle='modal'><i class='fa fa-edit'></i></a>
        <a class='icon-edit lsp' style='display:none;'  onclick='uploadURL(${data.rpaRequestId})' title='Upload Demo URL' href='#'><i class='fa fa-upload'></i></a></div >`;
}

function getJsonObject(rowdata) {
    const data = rowdata;
    return {
        requestId: data.rpaRequestId, requestName: data.requestName, currentExecutioncountWeekly: data.currentExecutioncountWeekly, currentAvgExecutiontime: data.currentAvgExecutiontime, description: data.description, status: (data.tblRpaBotstagings[0]) ? data.tblRpaBotstagings[0].status : 'NA', requeststatus: data.requestStatus, language: (data.tblRpaBotstagings[0]) ? data.tblRpaBotstagings[0].botlanguage : 'NA', botname: (data.tblRpaBotstagings[0]) ? data.tblRpaBotstagings[0].botname : 'NA', methodname: (data.tblRpaBotstagings[0]) ? data.tblRpaBotstagings[0].moduleClassMethod : 'NA',
        classname: (data.tblRpaBotstagings[0]) ? data.tblRpaBotstagings[0].moduleClassName : 'NA', spoc: data.spocsignum, lineOfCode: (data.tblRpaBotstagings[0]) ? data.tblRpaBotstagings[0].lineOfCode : 'NA', rpaexecutionTime: (data.tblRpaBotstagings[0]) ? data.tblRpaBotstagings[0].rpaexecutionTime : 'NA', parallelWoexecution: (data.tblRpaBotstagings[0]) ? data.tblRpaBotstagings[0].parallelWoexecution : 'NA', reuseFactor: (data.tblRpaBotstagings[0]) ? data.tblRpaBotstagings[0].reuseFactor : 'NA'
    };
}
function getStringfyJsonObject(rowdata) {
    const data = rowdata;
    return JSON.stringify({
        requestId: data.rpaRequestId, requestName: data.requestName, currentExecutioncountWeekly: data.currentExecutioncountWeekly, currentAvgExecutiontime: data.currentAvgExecutiontime, description: data.description, status: (data.tblRpaBotstagings[0]) ? data.tblRpaBotstagings[0].status : 'NA', requeststatus: data.requestStatus, language: (data.tblRpaBotstagings[0]) ? data.tblRpaBotstagings[0].botlanguage : 'NA', botname: (data.tblRpaBotstagings[0]) ? data.tblRpaBotstagings[0].botname : 'NA', methodname: (data.tblRpaBotstagings[0]) ? data.tblRpaBotstagings[0].moduleClassMethod : 'NA',
        classname: (data.tblRpaBotstagings[0]) ? data.tblRpaBotstagings[0].moduleClassName : 'NA', spoc: data.spocsignum, lineOfCode: (data.tblRpaBotstagings[0]) ? data.tblRpaBotstagings[0].lineOfCode : 'NA', rpaexecutionTime: (data.tblRpaBotstagings[0]) ? data.tblRpaBotstagings[0].rpaexecutionTime : 'NA', parallelWoexecution: (data.tblRpaBotstagings[0]) ? data.tblRpaBotstagings[0].parallelWoexecution : 'NA', reuseFactor: (data.tblRpaBotstagings[0]) ? data.tblRpaBotstagings[0].reuseFactor : 'NA', isInputRequired: data.isInputRequired
    });
}
function hideAndShowIconsForMacro(rowdata, jsonForm) {
    let iconsEditVideoUrlHtml = '';
    if (rowdata.tblRpaBotstagings.length > 0 && rowdata.tblRpaBotstagings[0].botlanguage && rowdata.tblRpaBotstagings[0].botlanguage.toLowerCase() === 'macro') {
        iconsEditVideoUrlHtml = getEditUploadHideAction(rowdata, jsonForm)
    } else {
        iconsEditVideoUrlHtml = getEditUploadDisplayAction(rowdata, jsonForm)
    }
    return iconsEditVideoUrlHtml;
}
function getDeployedBotActionIcons(rowdata, jsonData) {
    const data = rowdata;
    let iconsEditVideoUrlHtml = '';
    if (rowdata.tblRpaBotstagings.length > 0 && rowdata.tblRpaBotstagings[0].botlanguage.toLowerCase() === 'macro') {
        iconsEditVideoUrlHtml = `<a class='icon-edit lsp' style='display:none;'  onclick='uploadURL(${data.rpaRequestId})' title='Upload Demo URL' href='#'><i class='fa fa-upload'></i></a></div >`;
    } else {
        iconsEditVideoUrlHtml = `<a class='icon-edit lsp'   onclick='uploadURL(${data.rpaRequestId})' title='Upload Demo URL' href='#'><i class='fa fa-upload'></i></a></div >`;
    }
    return `<div style='display:flex'><a class="icon-view" title='View' onclick="viewBotDetails(${data.rpaRequestId},'${data.tblProjects.projectName}','${jsonData.status}')" data-toggle='modal' href='#requestInfo'><i class='fa fa-eye'></i></a><a class='icon-view lsp' onclick=getVersionFlowChart(${data.tblProjects.projectId},${data.subactivityId},${data.workflowDefid}) title='View Workflow' href='#'><i class='fa fa-code-fork'></i></a>${iconsEditVideoUrlHtml}`;
}

function getPythonBaseVersionForEditBot() {

    $.isf.ajax({
        async: false,
        type: "GET",
        url: service_java_URL + "botStore/getLanguageBaseVersion",
        dataType: "json",
        // crossdomain: true,
        success: function (data) {
            if (!data.isValidationFailed) {
                var versions = data.responseData;
                $("#selectVersionEditBot").empty();
                $("#selectVersionEditBot").append("<option value=-1>Select Version</option>");
                $.each(versions, function (i, d) {
                    if (d["languageBaseVersion"] === PythonDefaultBaseVersion) {
                        $('#selectVersionEditBot').append('<option selected value="' + d["languageBaseVersionID"] + '">' + d["languageBaseVersion"] + '</option>');
                    }
                    else {
                        $('#selectVersionEditBot').append('<option value="' + d["languageBaseVersionID"] + '">' + d["languageBaseVersion"] + '</option>');
                    }
                })
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

    $('#selectVersionEditBot').select2({
        closeOnSelect: true
    });

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

