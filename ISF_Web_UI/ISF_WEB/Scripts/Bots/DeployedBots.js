$(document).ready(function () {
    displayNoteForMacroBots();
});
function getDeployedBots() {
    if ($.fn.dataTable.isDataTable('#tbBotBody')) {
        oTable.destroy();
        $("#tbReqBody").empty();
    }

    pwIsf.addLayer({ text: 'Please wait ...' });
    $.isf.ajax({
        url: service_java_URL + "botStore/getBOTsForExplore",
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
            $("#tbReqBody").append($('<tfoot><tr><th></th><th>Request Id</th><th>Project Name</th><th>SPOC</th><th>Execution(Weekly)</th><th>Status</th><th>Bot Execution Count</th><th>Bot Execution Hours</th><th>Bot Execution Fail Count</th></tr></tfoot>'));
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
                            return "<div style='display:flex'><a class='icon-view' title='View' onclick='viewBotDetailsDeployed(" + data.rpaRequestId + ",\"" + data.projectName + "\")' data-target='#requestInfo' data-toggle='modal' href='#requestInfo'><i class='fa fa-eye'></i></a><a class='icon-view lsp' title='Audit' onclick='showAuditModal(" + data.rpaRequestId + "," + data.isInputRequired + ")'><i class='fa fa-search'></i></a></div>";
                            }
                    },
                    { "title": "Request Id", "data": "rpaRequestId" }, { "title": "Project Name", "data": "projectName" },
                    { "title": "SPOC", "data": "spocsignum" },
                    { "title": "Execution(Weekly)", "data": "currentExecutioncountWeekly" },
                    { "title": "Status", "data": "requestStatus" },
                   

                ],
                initComplete: function () {

                    $('#tbReqBody tfoot th').each(function (i) {
                        var title = $('#tbReqBody thead th').eq($(this).index()).text();
                        if ((title != "Action") && (title != "Status"))
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
            oTable
        .search('')
        .columns(5).search('Deployed')
        .draw();
            $('#tbReqBody tfoot').insertAfter($('#tbReqBody thead'));
        },
        error: function (xhr, status, statusText) {
            console.log('An error occurred');
        }
    });
}


function getAllBotDetails() {
    pwIsf.addLayer({ text: "Please wait ..." });

    if ($.fn.dataTable.isDataTable('#tbBotBody')) {
        oTable.destroy();
        $("#tbBotBody").empty();
    }
    $.isf.ajax({
        url: service_java_URL + "botStore/getBOTsForExplore",
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
            $("#tbBotBody").append($('<tfoot><tr><th></th><th>BOT ID</th><th>BOT Name</th><th>Project</th><th>SubActivity</th><th>WorkFlow</th><th>Market Area</th><th>Deployed On</th><th>Bot Execution Count</th><th>Bot Execution Hours</th><th>Bot Execution Fail Count</th></tr></tfoot>'));
            oTable =  $('#tbBotBody').DataTable({
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
                            var projectName = data.projectName;
                            return "<div style='display:flex'><a class='icon-view' title='View' onclick='viewBotDetailsDeployed(" + data.rpaRequestId + ",\"" + projectName + "\")' data-toggle='modal' data-target='#requestInfo' href='javascript:void()'><i class='fa fa-eye'></i></a><a class='icon-view lsp' title='Audit' onclick='showAuditModal(" + data.botid + "," + data.isInputRequired + ")'><i class='fa fa-search'></i></a></div>";
                        }
                    },
                    { "title": "BOT ID", "data": "botid" }, 
                    { "title": "BOT Name", "data": "botname" },
                    {
                        "title": "Project", "data": null,
                        "render": function (data, type, row, meta)
                        {
                            return data.projectId + "/" + data.projectName;
                        }

                    },
                    {
                        "title": "Subactivity", "data": null,
                        "render": function (data, type, row, meta)
                        {
                            return data.subactivityId+"/"+data.subactivityName;
                        }

                    },
                    {"title": "WorkFlow", "data": "workFlowName"},
                    { "title": "Market Area", "data": "marketAreaName" },
                    {
                        "title": "Deployed On",
                        "render": function (data, row, type, meta) {
                            var d = new Date(type.createdOn); createdOn = $.format.date(d, "yyyy-MM-dd");
                            return createdOn;
                        }
                    },
                    { "title": "Bot Execution Count", "data": "rpaexecutionTime" },
                    { "title": "Bot Execution Hours", "data": "currentAvgExecutiontime" },
                    { "title": "Bot Execution Fail Count", "data": "reuseFactor" }
                ],
                initComplete: function () {

                    $('#tbBotBody tfoot th').each(function (i) {
                        var title = $('#tbBotBody thead th').eq($(this).index()).text();
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
            $('#tbBotBody tfoot').insertAfter($('#tbBotBody thead'));
        },
        error: function (xhr, status, statusText) {
            console.log('An error occurred');
        }
    });
}


function viewBotDetailsDeployed(requestID, projectName) {
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
                let yesStr='YES',noStr='NO',notFound='-';
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
                getData.tech = data[0].Technology;
                getData.WorkFlowName = data.workFlowName;
                getData.SubactivityName = data[0].SubActivity;
                getData.TaskName = data[0].TaskName;
                getData.toolNames = data[0].ToolName;
                getData.url = data[0].VideoURL;
                getData.botExecutionCount = data[0].BotExecutionCount;
                getData.botReuseCount = data[0].BotReuseCount;
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
                const checkBox = `<tr>
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

                if (ApiProxy === false && getData.url === null) {
                    var newHtml = `<table class="table table-striped">
                            <thead>
                                <tr>
                                    <th colspan="4"><h5><b>Project Name:</b> ${getData.proj}</h5></th>

                                </tr>
                            </thead>
                            <tbody id="botDetailPopUp">
                                <tr>
                                    <td class="heading">Spoc(Name/Signum):</td>
                                    <td>${getData.spocsignum}</td>
                                        <td class="heading">Sub-Activity Name:</td>
                                    <td>${getData.SubactivityName}</td>
                                </tr>
                                 <tr>
                                    <td class="heading">Domain Name:</td>
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
                                <td class="heading">Activity Name:</td>
                                <td>${getData.activity}</td>
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
                                    <td class="heading">Language Base Version</td>
                                    <td colspan="3">${getData.LanguageBaseVersion}</td>
                                </tr>
                                
                                <tr>
                                    <td class="heading">Downloadable Files:</td>
                                    <td colspan="3"><button class='infile btn btn-default' type="button" title='Input File' onclick="downloadBotFiles({thisObj:this,botId:${requestID},fileName:'input.zip'});"><i class='fa fa-download' aria-hidden='true'></i> Input</button><button class=' btn btn-default' type="button" title='Output File' onclick="downloadBotFiles({thisObj:this,botId:${requestID},fileName:'output.zip'});"><i class='fa fa-download' aria-hidden='true'></i> Output</button><button class=' btn btn-default' type="button" title='Code File' onclick="downloadBotFiles({thisObj:this,botId:${requestID},fileName:'code.zip'});"><i class='fa fa-download' aria-hidden='true'></i>Code</button><button class=' btn btn-default' type="button" title='Logic File' onclick="downloadBotFiles({thisObj:this,botId:${requestID},fileName:'logic.zip'});"><i class='fa fa-download' aria-hidden='true'></i>Logic</button></td>
                                
                                </tr>
                            </tbody>
                        </table>`;
                }
                else if (ApiProxy === false) {
                    var newHtml = `<table class="table table-striped">
                            <thead>
                                <tr>
                                    <th colspan="4"><h5><b>Project Name:</b> ${proj}</h5></th>

                                </tr>
                            </thead>
                            <tbody id="botDetailPopUp">
                                <tr>
                                    <td class="heading">Spoc(Name/Signum):</td>
                                    <td>${getData.spocsignum}</td>
                                    <td class="heading">Sub-Activity Name:</td>
                                    <td>${getData.SubactivityName}</td>
                                </tr>
                               <tr>
                                    <td class="heading">Domain Name:</td>
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
                                <td class="heading">Activity Name:</td>
                                <td>${getData.activity}</td>
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
                                    <td class="heading">Language Base Version</td>
                                    <td colspan="3">${getData.LanguageBaseVersion}</td>
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
                                    <th colspan="4"><h5><b>Project Name:</b> ${proj}</h5></th>

                                </tr>
                            </thead>
                            <tbody id="botDetailPopUp">
                                <tr>
                                    <td class="heading">Spoc(Name/Signum):</td>
                                    <td>${getData.spocsignum}</td>
                                    <td class="heading">Sub-Activity Name:</td>
                                    <td>${getData.SubactivityName}</td>
                                </tr>
                                <tr>
                                    <td class="heading">Domain Name:</td>
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
                                <td class="heading">Activity Name:</td>
                                <td>${getData.activity}</td>
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
                                    <td class="heading">Language Base Version</td>
                                    <td colspan="3">${getData.LanguageBaseVersion}</td>

                                </tr>
                            </tbody>
                        </table>`;
                }

                $('#requestDetailsInfo').append(newHtml);
                $('#botDetailPopUp').append(checkBox);
                $('#requestInfo').modal('show');

                if (getData.isInputRequired) {
                    $('.inputRequiredCheckboxOwnDev').attr('checked', getData.isInputRequired);
                } else {
                    $(".infile").addClass("disabledbutton");
                    $(".inputLabel").css("color", '#868598');
                    $(".inputLabel").text("Bot Input Not Required");
                }

                // checkbox disabled
                $('.checkbox-container').addClass('disabledbutton');

            }
            else {

                const newNoRecordsHtml = `No records returned !!!`;
                const newH = `<button type="button" class="btn btn-primary" data-dismiss="modal" style="margin-left:10px">Close</button>`;
                $('#requestDetailsInfo').append(newNoRecordsHtml);
                $('#requestfoot').html('').append(newH);
            }
            
        },
        error: function (xhr, status, statusText) {
            console.log('An error occurred');
        },
        complete:function(){
            pwIsf.removeLayer();
        }
    });

}

function showAuditModal(requestID, isInputRequired) {
    console.log(isInputRequired);

    auditRequestID = requestID;
    $("#auditModal").modal('show');
    
    var html = ` <button class='infile btn btn-default' type="button" title='Input File' onclick="downloadBotFiles({thisObj:this,botId:${requestID},fileName:'input.zip'});"><i class='fa fa-download' aria-hidden='true'></i> Input</button><button class=' btn btn-default' type="button" title='Output File' onclick="downloadBotFiles({thisObj:this,botId:${requestID},fileName:'output.zip'});"><i class='fa fa-download' aria-hidden='true'></i> Output</button><button class=' btn btn-default' type="button" title='Code File' onclick="downloadBotFiles({thisObj:this,botId:${requestID},fileName:'code.zip'});"><i class='fa fa-download' aria-hidden='true'></i>Code</button><button class=' btn btn-default' type="button" title='Logic File' onclick="downloadBotFiles({thisObj:this,botId:${requestID},fileName:'logic.zip'});"><i class='fa fa-download' aria-hidden='true'></i>Logic</button>`;
    $('#downloadFiles').html('').append(html);

    if (!isInputRequired) {
        $(".infile").addClass("disabledbutton");
    } else {
        $(".infile").removeClass("disabledbutton");
    }

    }

function submitAuditStatus(){
    var status = $("#auditOptions")[0].value;
    updateAuditStatus(auditRequestID, status);
}

function updateAuditStatus(requestID, status) {

    pwIsf.addLayer({ text: 'Please wait ...' });
    $.isf.ajax({
        url: service_java_URL + "botStore/changeAuditPassFail/" + requestID + "/" + signumGlobal+"/" + status,
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
            if (data.apiSuccess) {
                pwIsf.alert({ msg: data.responseMsg, type: "success" });
                $('#downloadFiles').html('');
            }
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