
$(document).ready(function () {
    var rejectRequestID; var authWindow = "";
    displayNoteForMacroBots();
    getDeployedBotDetails();
    $('#requestInfo').on('show.bs.modal', function () {
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
                //window.open('isfalert:test_' + requestID, '_self');
            } else {
                pwIsf.alert({ msg: '' + data.responseMsg, type: 'warning' });
            }
        });

        ajCall.fail(function () { pwIsf.alert({ msg: 'Error in BOT testing.', type: 'error' }); });
        ajCall.always(function () { pwIsf.removeLayer(); });

    });
});

function getDeployedBotDetails() {
    if ($.fn.dataTable.isDataTable('#tbBotBody')) {
        oTable.destroy();
        $("#tbReqBody").empty();
    }

    pwIsf.addLayer({ text: 'Please wait ...' });
    $.isf.ajax({
        url: service_java_URL + "rpaController/getBotIDByWFSignum/" + signumGlobal,
        context: this,
        crossdomain: true,
        processData: true,
        contentType: 'application/json',
        type: 'GET',
        xhrFields: {
            withCredentials: false
        },
        success: getDeployedBotTable,

        error: function (xhr, status, statusText) {
            console.log('An error occurred');
        }
    });

    function getDeployedBotTable(data) {
        pwIsf.removeLayer();
        botRequestData = data;

        oTable = $('#tbReqBody').DataTable({
            searching: true,
            responsive: true,
            "pageLength": 10,
            "data": data.data,
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
                        if (data.BotCurrentStatus == "DEPLOYED" || data.BotCurrentStatus == "Deployed") {
                            return "<div style='display:flex'><a class='icon-view' title='View' onclick='viewApprovalBotDetails(" + data.rpaRequestID + ",\"" + data.ProjectName + '"' + ",\"" + data.BotCurrentStatus + "\")' data-toggle='modal' data-target='#requestInfo' href='javascript:void()'><i class='fa fa-eye'></i></a><a class='icon-view lsp' onclick='getVersionFlowChart(" + data.ProjectID + "," + data.SubactivityID + "," + data.WorkflowDEFID + ")' title='View Workflow' href='#'><i class='fa fa-code-fork'></i></a></div>";
                        }
                        else {
                            if (data.BotCurrentStatus == "REJECTED" || data.BotCurrentStatus == "Rejected") {
                                return "<div style='display:flex'></div>";
                            }
                            else if (data.BotCurrentStatus == "APPROVED" || data.BotCurrentStatus == "Approved") {
                                return "<div style='display:flex'><a class='icon-view' title='View' onclick='viewApprovalBotDetails(" + data.rpaRequestID + ",\"" + data.ProjectName + '"' + ",\"" + data.BotCurrentStatus + "\")' data-toggle='modal' data-target='#requestInfo' href='javascript:void()'><i class='fa fa-eye'></i></a><a class='icon-view lsp' onclick='getVersionFlowChart(" + data.ProjectID + "," + data.SubactivityID + "," + data.WorkflowDEFID + ")' title='View Workflow' href='#'><i class='fa fa-code-fork'></i></a>&nbsp;&nbsp;<a class='icon-view' onclick='deployBOT(" + data.ProjectID + "," + data.SubactivityID + "," + data.WorkflowDEFID + "," + data.rpaRequestID + ",\"" + data.requestName + "\",\"" + data.FTR + "\"," + data.NENeededForExecution + "," + data.SLAHours + ",\"" + data.WFOwner + "\",\"" + data.WFSTEPID + "\",\"" + data.CreatedBY + "\"," + data.CreatedOn + "," + data.DomainID + "," + data.TechnologyID + ",\"" + data.WorkFlowName + "\"," + data.WFID + ", " + data.LOEMeasurementCriterionID+")' title='BOT Integration and Deployment' href='#'><i class='fa fa-rocket'></i></a></div>";
                            }
                            else {
                                return "<div style='display:flex'><a class='icon-view' title='View' onclick='viewApprovalBotDetails(" + data.rpaRequestID + ",\"" + data.ProjectName + '"' + ",\"" + data.BotCurrentStatus + "\")' data-toggle='modal' data-target='#requestInfo' href='javascript:void()'><i class='fa fa-eye'></i></a><a class='icon-view lsp' onclick='getVersionFlowChart(" + data.ProjectID + "," + data.SubactivityID + "," + data.WorkflowDEFID + ")' title='View Workflow' href='#'><i class='fa fa-code-fork'></i></a>&nbsp;&nbsp;<a class='icon-add' title='Approve' onclick='approveBotRequest(" + data.rpaRequestID + ",\"" + signumGlobal + "\")' ><i class='fa fa-check'></i></a><a class='icon-delete lsp' title='Reject' onclick='showRejectReasonModal(" + data.rpaRequestID + ")' href='#'><i class='fa fa-remove'></i></a></div>";
                            }
                        }
                    }
                },
                { "title": "Request Id", "data": "rpaRequestID" },
                {
                    "title": "Project", "data": null,
                    "render": function (data, type, row, meta) {
                        return data.ProjectID + "/" + data.ProjectName;
                    }

                },
                {
                    "title": "Subactivity", "data": null,
                    "defaultContent": "",
                    "render": function (data, type, row, meta) {
                        return data.SubactivityID + "/" + data.SubActivityName;
                    }
                },
                { "title": "WorkFlow", "data": "WorkFlowName" },

                {
                    "title": "SPOC", "data": null,
                    "defaultContent": "",
                    "render": function (data, type, row, meta) {
                        return '<a href="#" style="color:blue;" onclick="getEmployeeDetailsBySignum(\'' + data.SPOCSignum + '\')">' + data.SPOCSignum + '</a>';
                    }

                },
                { "title": "Execution(Weekly)", "data": "CurrentExecutioncountWeekly" },
                {
                    "title": "Current Status", "data": null,
                    "render": function (data, type, row, meta) {
                        return data.BotCurrentStatus;
                    }
                },
                { "title": "Request Type", "data": "RequestStatus" },
                {
                    "title": "BOT Saving", "data": null,
                    "render": function (data, type, row, meta) {
                        if (data.savings == null || data.savings == 0) { return "NA"; }
                        else { return data.savings; }
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
    }
}

function viewApprovalBotDetails(requestID, projectName, currStatus) {
    pwIsf.addLayer({ text: 'Please wait ...' });

    $('#requestDetailsInfo').empty();
    $('#requestInfo').modal('show');
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
                getData.botId = data[0].BOTID;
                getData.botName = data[0].BOTName;
                getData.tech = data[0].Technology;
                getData.WorkFlowName = data.workFlowName;
                getData.SubactivityName = data[0].SubActivity;
                getData.TaskName = data[0].TaskName;
                getData.toolNames = data[0].ToolName;
                getData.url = data[0].VideoURL;
                getData.botExecutionCount = data[0].BotExecutionCount;
                getData.botReuseCount = data[0].BotReuseCount;
                getData.isInputRequired = data[0].IsInputRequired;
                getData.LanguageBaseVersionID = data[0].LanguageBaseVersionID;
                getData.LanguageBaseVersion = data[0].LanguageBaseVersion;
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
                                    <td class="heading">Activity / Subactivity:</td>
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
                                    <td class="heading">Bot Execution Count:</td>
                                    <td>${getData.botExecutionCount}</td>
                                    <td class="heading">Bot Reuse Count:</td>
                                    <td>${getData.botReuseCount}</td>
                                </tr>

                                <tr>
                                    <td class="heading">Language Base Version:</td>
                                    <td>${getData.LanguageBaseVersion === null ? 'NA' : getData.LanguageBaseVersion}</td>
                                </tr>

                                <tr>
                                    <td class="heading">Downloadable Files:</td>
                                    <td colspan="3"><button class='infile btn btn-default' type="button" title='Input File' onclick="downloadBotFiles({thisObj:this,botId:${requestID},fileName:'input.zip'});"><i class='fa fa-download' aria-hidden='true'></i> Input</button><button class=' btn btn-default' type="button" title='Output File' onclick="downloadBotFiles({thisObj:this,botId:${requestID},fileName:'output.zip'});"><i class='fa fa-download' aria-hidden='true'></i> Output</button><button class=' btn btn-default' type="button" title='Code File' onclick="downloadBotFiles({thisObj:this,botId:${requestID},fileName:'code.zip'});"><i class='fa fa-download' aria-hidden='true'></i> Code</button><button class=' btn btn-default' type="button" title='Logic File' onclick="downloadBotFiles({thisObj:this,botId:${requestID},fileName:'logic.zip'});"><i class='fa fa-download' aria-hidden='true'></i>Logic</button></td>
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
                                    <td class="heading">Bot Execution Count:</td>
                                    <td>${getData.botExecutionCount}</td>
                                    <td class="heading">Bot Reuse Count:</td>
                                    <td>${getData.botReuseCount}</td>
                                </tr>

                                <tr>
                                    <td class="heading">Language Base Version:</td>
                                    <td>${getData.LanguageBaseVersion === null ? 'NA' : getData.LanguageBaseVersion}</td>
                                </tr>

                                <tr>
                                    <td class="heading">Downloadable Files:</td>
                                    <td colspan="3"><button class='infile btn btn-default' type="button" title='Input File' onclick="downloadBotFiles({thisObj:this,botId:${requestID},fileName:'input.zip'});"><i class='fa fa-download' aria-hidden='true'></i> Input</button><button class=' btn btn-default' type="button" title='Output File' onclick="downloadBotFiles({thisObj:this,botId:${requestID},fileName:'output.zip'});"><i class='fa fa-download' aria-hidden='true'></i> Output</button><button class=' btn btn-default' type="button" title='Code File' onclick="downloadBotFiles({thisObj:this,botId:${requestID},fileName:'code.zip'});"><i class='fa fa-download' aria-hidden='true'></i>Code</button><button class=' btn btn-default' type="button" title='Logic File' onclick="downloadBotFiles({thisObj:this,botId:${requestID},fileName:'logic.zip'});"><i class='fa fa-download' aria-hidden='true'></i>Logic</button><button class=' btn btn-default' type="button" title='Logic File' onclick="window.open(\'${getData.url}\','_blank');"><i class='fa fa-download' aria-hidden='true'></i>Video URL</button></td>
                                </tr>
                            </tbody>
                        </table>`;
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
                                <td>${getData.activity}/${getData.SubactivityName}</td>
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
                                 <td class="heading">Bot Execution Count:</td>
                                <td>${getData.botExecutionCount}</td>
                                <td class="heading">Bot Reuse Count:</td>
                                <td>${getData.botReuseCount}</td>
                                </tr>

                                <tr>
                                    <td class="heading">Language Base Version:</td>
                                    <td>${getData.LanguageBaseVersion === null ? 'NA' : getData.LanguageBaseVersion}</td>
                                </tr>
                            </tbody>
                        </table>`;

                }

                if (currStatus == "REJECTED" || currStatus == "Rejected") {
                    let newHtml = `<button type="button" class="btn btn-primary" data-dismiss="modal" style="margin-left:10px">Close</button>`;
                    $('#infofooter').html('').append(newHtml);
                }
                else {
                    let newHtml = `<button type="button" class="btn btn-primary" data-dismiss="modal">Close</button>
                                <button type="button" id="testBtnBotRequest" data-details="" class="btn btn-sm btn-warning testNewBotRequest" style="margin-left:10px">TEST BOT</button>
<button type="button" id="stopTestBtnBotRequest" data-details="" class="btn btn-sm btn-danger stopTestBtnBotRequest" style="margin-left:10px">STOP TEST-BOT</button>`;

                    $('#infofooter').html('').append(newHtml);
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
                            }
                            else {
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
                                //window.open('isfalert:test_' + requestID, '_self');
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
                $('#requestInfo').modal('show');
            }
            else {
                var newHtml = `No records returned !!!`;
                var newH = `<button type="button" class="btn btn-primary" data-dismiss="modal" style="margin-left:10px">Close</button>`;
                $('#requestDetailsInfo').append(newHtml);
                $('#requestfoot').html('').append(newH);
            }
        },
        error: function (xhr, status, statusText) {
            pwIsf.alert({ msg: 'Error in retriving data', type: 'error' });
            console.log('An error occurred');
        },
        complete: function () {
            pwIsf.removeLayer();
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
        //url: service_java_URL + "rpaController/getWoIDByProjectID/1/16578/10006",// + subactivityID + "/" + SubActivityFlowChartDefID,
        //async:false,
        //success: getData,
        error: function (msg) {

        },
        complete: function (msg) {
        }
    });

    function getData(data) {
        $.each(data.data, function (i, d) {
            //var WOID = d.WOID;
            versionNo = d.VersionNumber;

        })
        flowChartViewOpenInNewWindow(UiRootDir + '/Project/FlowChartViewEP?subId=' + subactivityID + '&projID=' + projectID + '&version=' + versionNo);
    }

    _jqXHR.done(function (status) {
        //var versionNo = status.data[0].VersionNumber;
        //flowChartViewOpenInNewWindow(UiRootDir + '/Project/FlowChartViewEP?subId=' + subactivityID + '&projID=' + projectID + '&version=' + versionNo);
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
            //var experiencedMode;

            //    if (status.data[0].MultiView == true) {
            //        experiencedMode = 1;
            //    }
            //    else {
            //        experiencedMode = 0;
            //    }


            //As discussed in 10.2 with Jayant we are making experiencedMode = 0 as BotRequest can be raised only for Assessed Workflow.
            var experiencedMode = 0;
        }
        flowChartViewOpenInNewWindow(UiRootDir + '/Project/FlowChartWorkOrderView\?subId=' + subActID + '&projID=' + projID + '&version=' + version_id + '&domain=' + domain + '&subDomain=' + subDomain + '&subServiceArea=' + subServiceArea + '&tech=' + tech + '&activity=' + activity + '&subActivity=' + subActivity + '&experiencedMode=' + experiencedMode + '&wfid=' + wfid);

    });
}

/*
function loadCheckbox(isInputRequired) {
    $('.inputRequiredCheckboxOwnDev').attr('checked', isInputRequired);

    if (!isInputRequired) {
        $(".infile").addClass("disabledbutton");
        $(".inputLabel").css("color", '#868598');
        $(".inputLabel").text("Input Not Required");
    }

    $('.checkbox-container').addClass('disabledbutton');

}
*/

function flowChartViewOpenInNewWindow(url) {
    //var width = window.innerWidth * 0.85;
    // define the height in
    //var height = width * window.innerHeight / window.innerWidth;
    // Ratio the hight to the width as the user screen ratio
    authWindow.location.replace(url);
    //window.open(url, '_blank', 'width=' + width + ', height=' + height + ', top=' + ((window.innerHeight - height) / 2) + ', left=' + ((window.innerWidth - width) / 2));

}

function deployBOT(projectID, subactivityID, SubActivityFlowChartDefID, reqID, reqName, FTR, isNeMandate, SLAHours, wfOwnerName, stepID, createdBy, createdOn, domainId, techID, workFlowName, wfid, loeMeasurement) {
    //let service_java_URL = 'http://localhost:8080/isf-rest-service-java/';
    var width = window.innerWidth * 0.85;
    var height = width * window.innerHeight / window.innerWidth;
    authWindow = window.open('', '_blank', 'width=' + width + ', height=' + height + ', top=' + ((window.innerHeight - height) / 2) + ', left=' + ((window.innerWidth - width) / 2));
    let jsonData = new Object();
    jsonData.projectID = projectID;
    jsonData.subactivityID = subactivityID;
    jsonData.subActivityFlowChartDefID = SubActivityFlowChartDefID;
    jsonData.workFlowName = workFlowName;
    var _jqXHR = $.isf.ajax({
        type: "GET",
        returnAjaxObj: true,
        url: service_java_URL + "rpaController/getLatestVersionOfWfBySubactivityID?projectID=" + projectID + "&subactivityID=" + subactivityID + "&subActivityFlowChartDefID=" + SubActivityFlowChartDefID + "&workFlowName=" + workFlowName + "&wfid=" + wfid,
        error: function (msg) {

        },
        complete: function (msg) {
        }
    });
    _jqXHR.done(function (status) {
        var experiencedMode;
        if (status.data.length != 0) {
            var subActID = subactivityID;
            var projID = projectID;
            var version_id = status.data[0].VersionNumber;
            experiencedMode = status.data[0].MultiView;

        }
        localStorage.setItem('experiencedView', experiencedMode);
        localStorage.setItem('newBotID', reqID + '@' + reqName);
        localStorage.setItem('slaHrs', SLAHours);
        localStorage.setItem('ftr', FTR);
        localStorage.setItem('loe', loeMeasurement);
        localStorage.setItem('isNeMandate', isNeMandate);
        localStorage.setItem('BotCreatedBy', createdBy);
        localStorage.setItem('BotCreatedOn', createdOn);
        localStorage.setItem('domain_id', domainId);
        localStorage.setItem('tech_id', techID);
        flowChartViewOpenInNewWindow('FlowChartBOTEdit\?subId=' + subActID + '&projID=' + projID + '&version=' + version_id + '&wfownername=' + wfOwnerName + '&stepId=' + stepID + '&newBotID=' + reqID + '&newBOTName=' + reqName + '&wfid=' + wfid);
    });
}

function approveBotRequest(requestID, signum) {

    pwIsf.confirm({
        title: 'Approve Request', msg: "Are you sure want to Approve the Request?",
        'buttons': {
            'Yes': {
                'action': function () {
                    //let service_java_URL = "http://169.144.28.204:8080/isf-rest-server-java/";
                    pwIsf.addLayer({ text: 'Please wait ...' });
                    $.isf.ajax({
                        url: service_java_URL + "botStore/approveBot/" + requestID + "/" + signum,
                        //url: service_java_URL + "botStore/changeDeployedBotStatus/" + requestID + "/" + signum+"/1",
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
                                pwIsf.alert({ msg: data.responseMsg + "(Approved)", type: "success" });
                            else
                                pwIsf.alert({ msg: data.responseMsg, type: 'warning' });


                        },
                        error: function (xhr, status, statusText) {
                            pwIsf.alert({ msg: "Error while Approving", type: "warning" });
                        },
                        complete: function (xhr, statusText) {
                            pwIsf.removeLayer();
                            getDeployedBotDetails()
                        }
                    });
                }
            },
            'No': { 'action': function () { } },

        }
    });

}

function showRejectReasonModal(requestID) {
    rejectRequestID = requestID;
    $("#rejectReason")[0].value = "";
    $("#rejectReasonModal").modal('show');

}

function submitRejectRequest() {
    var rejectReasonText = $("#rejectReason").val();
    if (rejectReasonText == "")
        pwIsf.alert({ msg: "Please specify a reason for Rejection", type: "warning" });
    else
        rejectBotRequest(rejectRequestID, rejectReasonText);
}

function rejectBotRequest(requestID, rejectReasonText) {

    pwIsf.addLayer({ text: 'Please wait ...' });
    $.isf.ajax({
        url: service_java_URL + "botStore/updateBotForNonFeasibile/" + requestID + "/" + signumGlobal + "/" + rejectReasonText,
        context: this,
        crossdomain: true,
        processData: true,
        contentType: 'application/json',
        type: 'POST',
        xhrFields: {
            withCredentials: false
        },
        success: function (data) {
            $("#rejectReasonModal").modal('hide');
            if (data.apiSuccess)
                pwIsf.alert({ msg: data.responseMsg + " (Rejected)", type: "success" });
            else
                pwIsf.alert({ msg: data.responseMsg, type: 'warning' });

        },
        error: function (xhr, status, statusText) {
            pwIsf.alert({ msg: "Error while Rejecting", type: "warning" });
        },
        complete: function (xhr, statusText) {
            pwIsf.removeLayer();
        }
    });
}
