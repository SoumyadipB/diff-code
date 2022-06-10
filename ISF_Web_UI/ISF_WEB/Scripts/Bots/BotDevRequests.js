$(document).ready(function () {
    displayNoteForMacroBots();
});
function getRequestDetails() {
    if ($.fn.dataTable.isDataTable('#tbDevReqBody')) {
        oTable.destroy();
        $("#tbDevReqBody").empty();
    }
    var activeProfileObj = JSON.parse(ActiveProfileSession);
    var roleID = activeProfileObj.roleID;
    pwIsf.addLayer({ text: 'Please wait ...' });
    $.isf.ajax({
        url: `${service_java_URL_VM}botStore/getNewRequestListForRPAAdmin`,
        context: this,
        crossdomain: true,
        processData: true,
        contentType: 'application/json',
        type: 'GET',
        xhrFields: {
            withCredentials: false
        },
        success: function (responseData) {
            pwIsf.removeLayer();
            oTable = $('#tbDevReqBody').DataTable({
                searching: true,
                responsive: true,
                "pageLength": 10,
                "data": responseData,
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
                        "render": function (data, type, row, meta)
                        {
                            if (data.requestStatus === "New" || data.requestStatus === "NEW")
                                return "<div style='display:flex'><a class='icon-view' title='View' onclick='viewBotDetails(" + data.rpaRequestId + ",\"" + data.tblProjects.projectName + "\")' data-toggle='modal' href='#requestInfo'><i class='fa fa-eye'></i></a><a class='icon-edit lsp' title='Assign' data-toggle='modal' data-target='#assignDev' onclick='assignRequest(" + data.rpaRequestId + ")'><i class='fa fa-location-arrow'></i></a><a class='icon-view lsp' onclick='getVersionFlowChart(" + data.tblProjects.projectId + ", " + data.subactivityId + ", " + data.workflowDefid + ")' title='View Workflow' href='#'><i class='fa fa-code-fork'></i></a></div>";
                            else
                                return "<div style='display:flex'><a class='icon-view' title='View' onclick='viewBotDetails(" + data.rpaRequestId + ",\"" + data.tblProjects.projectName + "\")' data-toggle='modal' href='#requestInfo'><i class='fa fa-eye'></i></a><a class='icon-edit lsp' title='Assign' data-toggle='modal' data-target='#assignDev' onclick='assignRequest(" + data.rpaRequestId + ")'><i class='fa fa-location-arrow'></i></a><a class='icon-delete lsp' title='Reject' onclick='showRejectReasonModal(" + data.rpaRequestId + ")' href='#'><i class='fa fa-remove'></i></a><a class='icon-view lsp' onclick='getVersionFlowChart(" + data.tblProjects.projectId + ", " + data.subactivityId + ", " + data.workflowDefid + ")' title='View Workflow' href='#'><i class='fa fa-code-fork'></i></a></div>";

                        }
                    },
                    { "title": "Request Id", "data": "rpaRequestId" },
                      {
                          "title": "Project", "data": null,
                          "render": function (data, type, row, meta)
                          {
                              return data.tblProjects.projectId+"/"+data.tblProjects.projectName;
                          }

                      },
                      {
                          "title": "Subactivity", "data": null,
                          "render": function (data, type, row, meta)
                          {
                              return data.subactivityId+"/"+data.subactivityName;
                          }

                      },
                     { "title": "WorkFlow", "data": "workFlowName" },
                    {
                        "title": "SPOC","data": null,
                        "defaultContent": "",
                        "render": function (data, type, row, meta)
                        {
                            return '<a href="#" style="color:blue;" onclick="getEmployeeDetailsBySignum(\'' + data.spocsignum + '\')">'+data.spocsignum+'</a>';
                        }

                    },
                    {
                        "title": "Request Created On",
                        "render": function (data, row, type, meta) {
                            var d = new Date(type.createdOn);
                            return $.format.date(d, "yyyy-MM-dd");
                        }
                    },
                    { "title": "Execution(Weekly)", "data": "currentExecutioncountWeekly" },
                    {
                        "title": "Current Status", "data": null,
                        "render": function (data, type, row, meta) {
                            
                            return (data.tblRpaBotstagings[0]) ? data.tblRpaBotstagings[0].status:'NA';
                        }
                    },
                    { "title": "Request Type", "data": "requestStatus" },
                    {
                        "title": "Bot Language", "data": null,
                        "render": function (data, type, row, meta) {
                            return (data.tblRpaBotstagings[0]) ? data.tblRpaBotstagings[0].botlanguage:'NA';
                        }
                    },

                    {
                        "title": "Assigned To", "data": null,
                        "render": function (data, type, row, meta) {
                            return (data.tblRpaBotstagings[0]) ? data.tblRpaBotstagings[0].assignedTo : 'NA';
                        }

                    },
                    {
                        "title": "Bot Name", "data": null,
                        "render": function (data, type, row, meta) {
                            return (data.tblRpaBotstagings[0]) ? data.tblRpaBotstagings[0].botname : 'NA';
                        }
                        

                    }

                ],
                initComplete: function () {

                    $('#tbDevReqBody tfoot th').each(function (i) {
                        var title = $('#tbDevReqBody thead th').eq($(this).index()).text();
                        if (title !== "Action")
                            $(this).html(`<input type="text" class="form-control" style="font-size:12px;" placeholder="Search ${title}" data-index="'${i}'" />`);
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
            $('#tbDevReqBody tfoot').insertAfter($('#tbDevReqBody thead'));
        },
        error: function (xhr, status, statusText) {
            console.log(`An error occurred:${xhr.status}`);
        }
    });


}

function showRejectReasonModal(requestID) {
    rejectRequestID = requestID;
    $("#rejectReason")[0].value = "";
    $("#rejectReasonModal").modal('show');
    
}

function rejectBotRequest(requestID, rejectReasonText) {

    pwIsf.addLayer({ text: 'Please wait ...' });
    $.isf.ajax({
        url: `${ service_java_URL }botStore/updateBotForNonFeasibile/${requestID}/${signumGlobal}/${rejectReasonText}`,
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

function submitRejectRequest(){
    var rejectReasonText = $("#rejectReason").val();
    if (rejectReasonText == "")
        pwIsf.alert({ msg: "Please specify a reason for Rejection", type: "warning" });
    else
        rejectBotRequest(rejectRequestID, rejectReasonText);
}

function viewBotDetails(requestID, projectName,signumGlobal) {
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
                getData.botId = data[0].BOTID;
                getData.botName = data[0].BOTName;
                getData.WorkFlowName = data.workFlowName;
                getData.subactivityName = data[0].SubActivity;
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

                if (ApiProxy === false && getData.url === null) {
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
                                 <td class="heading">Technology:</td>
                                <td>${getData.tech}</td>
                                <td class="heading">Activity/Subactivity:</td>
                                <td>${getData.activity} / ${getData.subactivityName}</td>
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

                    $('#requestDetailsInfo').append(newHtml);

                    $('#botDetailPopUp').append(checkBox);

                    loadCheckbox(getData.isInputRequired);

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

                    $('#requestDetailsInfo').append(newHtml);

                    $('#botDetailPopUp').append(checkBox);

                    loadCheckbox(getData.isInputRequired);

                    $('#requestInfo').modal('show');
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
                                    <td>${getData.subactivityName}</td>
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

                    $('#requestDetailsInfo').append(newHtml);

                    $('#botDetailPopUp').append(checkBox);

                    loadCheckbox(getData.isInputRequired);

                }
                if (!getData.isInputRequired) {
                    $('.infile').addClass('disabledbutton');
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

    $('.checkbox-container').addClass('disabledbutton');

}
function getSignumWO(requestID) {

    $("#cbxSignumWO").autocomplete({
        appendTo: "#assignDevBody",
        source: function (request, response) {
            $.isf.ajax({

                url: `${service_java_URL}activityMaster/getEmployeesByFilter`,
                type: "POST",
                data: {
                    term: request.term
                },
                success: function (data) {
                    $("#cbxSignumWO").autocomplete().addClass("ui-autocomplete-loading");
                    var result = [];
                    $.each(data, function (i, d) {
                        result.push({
                            "label": d.signum + "/" + d.employeeName,
                            "value": d.signum

                        });


                    })
                    response(result);
                    $("#cbxSignumWO").autocomplete().removeClass("ui-autocomplete-loading");

                }
            });
        },
        minLength: 3
    });
    $("#cbxSignumWO").autocomplete("widget").addClass("fixedHeight");
}

function assignRequest(requestID) {
    assignRequestID = requestID;
    $('#assignDev').modal('show');
    getSignumWO();
    if ($('#cbxSignumWO').val() === "Loading Signums...") {
        receiver = '0';
    }
    else {
        receiver = $('#cbxSignumWO').val();
	}
}

$(document).off('click').on("click", "#assignRequestDev", function () {
    if ($('#cbxSignumWO').val() === "Loading Signums...")
        receiver = '0';
    else
        receiver = $('#cbxSignumWO').val();

    massTransferWorkOrderWO(receiver);
});

function massTransferWorkOrderWO(receiver) {
    var status = []; selectedWO = [];
    if (receiver == "0") {
        pwIsf.alert({msg:"Please select employee signum", type:"warning"});
    }
    else {
        var request = $.isf.ajax({
            url: service_java_URL + "botStore/assignRequestToDev/" + assignRequestID + "/" + receiver + "/" + signumGlobal,
            type: "POST",
            contentType: "application/json; charset=UTF-8",
            crossDomain:true,
            dataType: "html",
            success: assignDev,
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
                pwIsf.alert({ msg: newData.responseMsg, type: 'warning' });
                
        }
    }
       
}

function getVersionFlowChart(projectID, subactivityID, SubActivityFlowChartDefID) {
    var versionNo = 0;
    var width = window.innerWidth * 0.85;
    var height = width * window.innerHeight / window.innerWidth;
    authWindow = window.open('', '_blank', 'width=' + width + ', height=' + height + ', top=' + ((window.innerHeight - height) / 2) + ', left=' + ((window.innerWidth - width) / 2));

    var _jqXHR = $.isf.ajax({
        type: "GET",
        url: `${service_java_URL}rpaController/getWoIDByProjectID/${projectID}/${subactivityID}/${SubActivityFlowChartDefID}`,
        returnAjaxObj: true
    });

    function getData(data) {
        $.each(data.data, function (i, d) {
            versionNo = d.VersionNumber;
            
        })
    }

    _jqXHR.done(function (status) {
        if(status.data.length != 0){
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

            //As discussed in 10.2 with Jayant we are making experiencedMode = 0 as BotRequest can be raised only for Assessed Workflow.
            var experiencedMode = 0;
        }
        flowChartViewOpenInNewWindow(UiRootDir + '/Project/FlowChartWorkOrderView\?subId=' + subActID + '&projID=' + projID + '&version=' + version_id + '&domain=' + domain + '&subDomain=' + subDomain + '&subServiceArea=' + subServiceArea + '&tech=' + tech + '&activity=' + activity + '&subActivity=' + subActivity + '&experiencedMode=' + experiencedMode + '&wfid=' + wfid);
    });
}

function flowChartViewOpenInNewWindow(url) {
    // Ratio the hight to the width as the user screen ratio
    authWindow.location.replace(url); 
}




