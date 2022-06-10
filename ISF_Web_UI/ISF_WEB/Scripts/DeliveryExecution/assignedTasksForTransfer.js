let globalAssignedTasksForTransferDataTable;
const C_USER_SIGNUM_TRANSFER = "#selectUserAllSignumTransfersingle";
const C_MODAL_TRANSFER = "#modalTransfer";
const C_AUTO_COMPLETE_LOADING = "ui-autocomplete-loading";
const C_EDIT_WO_FLAG = 'edit-work-order-flag';
const C_BACK_TO_PQ = '#btn_backToProjectQueue_';
const C_STEP_NAME_DIV = '#select_stepNameDiv';
const C_SUBMIT_MODAL = '#submitModal';
const C_MODAL_INPUT_WOID = '#modal_input_woid';
const C_COMMENTS_BOX='#tWOcommentsBox';

function getAllSignumForTransferWO() {
    $(C_USER_SIGNUM_TRANSFER).autocomplete({
        appendTo: C_MODAL_TRANSFER,
        source: function (request, response){
            $.isf.ajax({
                url: `${service_java_URL}activityMaster/getEmployeesByFilter`,
                type: "POST",
                data: {
                    term: request.term
                },
                success: function (data){
                    $(C_USER_SIGNUM_TRANSFER).autocomplete().addClass(C_AUTO_COMPLETE_LOADING);
                    var result = [];
                    if (data.length === 0) {
                        showErrorMsg('Signum-Required', 'Please enter a valid Signum.');
                        $(C_USER_SIGNUM_TRANSFER).val("");
                        $(C_USER_SIGNUM_TRANSFER).focus();
                        response(result);
                    }
                    else {
                        hideErrorMsg('Signum-Required');
                        $(C_USER_SIGNUM_TRANSFER).autocomplete().addClass(C_AUTO_COMPLETE_LOADING);
                        $.each(data, function (i, d) {
                            result.push({
                                "label": `${d.signum}/${d.employeeName}`,
                                "value": d.signum
                            })
                        });
                        response(result);
                        $(C_USER_SIGNUM_TRANSFER).autocomplete().removeClass(C_AUTO_COMPLETE_LOADING);
                    }
                }
            });
        },
        change: function (event, ui) {
            if (ui.item === null) {
                $(this).val('');
                $(C_USER_SIGNUM_TRANSFER).val('');
            }
            validateTransferButtonMyQueue();  
        },
        select: function (event, ui) {
            validateTransferButtonMyQueue();        
        },
        minLength: 3
    });
    $(C_USER_SIGNUM_TRANSFER).autocomplete("widget").addClass("fixedHeight");
}

function getDODetails(doid, woid) {
    $.isf.ajax({
        url: `${service_java_URL}woManagement/getWorkOrdersByDoid?doid=${doid}`,
        success: function (data) {
           oTableDODetail = $('#table_do_details').DataTable({
                "data": data,
                "destroy": true,
                "searching": true,
                order: [1],
                "columns": [
                    {
                        "title": "DOID",
                        "data": null,
                        "render": function (data, type, row) {
                            if (data.woid === woid) {
                                return '<i class="fa fa-chevron-circle-right" style="color:green"></i>' + data.doid;
                            } else {
                                return data.doid;
                            }
                        }
                    },
                    {
                        "title": "WOID",
                        "data": "woid"
                    },
                    {
                        "title": shortNeNameAsNEID,
                        "data": null,
                        "render": function (data, type, row, meta) {
                            let woViewDataArray = [];
                            const x = data.listOfNode;
                            let nodes = '';
                            for (var i = 0; i < x.length; i++) {
                                nodes = `${x[i].nodeNames},${nodes}`;
                                nodes = nodes.replace(/,\s*$/, '');
                            }
                            if (nodes == null || nodes === "null"){
                                nodes = "";
                            }
                            localStorage.setItem(`${data.woplanid}_${data.woid}`, JSON.stringify(data.listOfNode));
                            woViewDataArray.push(
                                {
                                    "NodeNames": nodes
                                });
                            var nodesView = `<i data-toggle="tooltip" id="showList${data.woid}" title="Netwrok Element Details"class="fa fa-list"`;
                            nodesView = `${nodesView}style="cursor:pointer" onclick="modalNodeWO(${data.woplanid},${data.woid})"></i>`;
                            return nodesView;
                        }
                    },
                    {
                        "title": "WFID_WFName_WFVErsion",
                        "data": null,
                        "render": function (data, type, row) {
                            return `${data.wfid}_${data.wfName}_${data.wfVersion}`;
                        },
                    },
                    {
                        "title": "Status",
                        "data": "status"
                    },
                    {
                        "title": "Signum",
                        "data": null,
                        "render": function (data, type, row) {
                            if (data.signum == null) {
                                return '';
                            }
                            else {
                                return data.signum;
                            }
                        }
                    }
                ],
                initComplete: function () {
                    //$('#table_do_details tfoot th').each(function (i) {
                    //   $('#table_do_details thead th').eq($(this).index()).text();
                    //});
                    var api = this.api();
                    api.columns().every(function () {
                        var that = this;
                        $('input').on('keyup change', function () {
                            if (that.search() === this.value) {
                                that
                                    .columns($(this).parent().index() + ':visible')
                                    .search(this.value)
                                    .draw();
                            }
                        });
                    });
                }
            });
        },
        error: function (xhr, status, statusText) {
            console.log('An error occurred on getDomain: ' + xhr.error);
        }
    });
}

function modalNodeWOPopup(wOPlanID, wOID) {
    const nodeList = localStorage.getItem(`${wOPlanID}_${wOID}`);
    if (nodeList) {
        let listOfNode = JSON.parse(nodeList);
        $.each(listOfNode, function (i, d) {
            if (d.nodeNames === "" && d.nodeType === "" && d.market === "") {
                listOfNode = [];
            }
        });
        if (listOfNode.length > 0) {
            if (listOfNode.length === 1 && listOfNode[0].nodeType !== '' && listOfNode[0].nodeNames !== '' && listOfNode[0].market !== '') {
                $('#table_wo_nodes_detail_edit').DataTable({
                    "data": listOfNode,
                    "destroy": true,
                    searching: true,
                    order: [1],
                    "columns": [
                        {
                            "title": neType,
                            "data": "nodeType",
                            "defaultContent": "NA"
                        },
                        {
                            "title": neName,
                            "data": "nodeNames",
                            "defaultContent": "NA"
                        },
                        {
                            "title": neMarket,
                            "data": "market",
                            "defaultContent": "NA"
                        }],
                });
            }
            else {
                $('#table_wo_nodes_detail_edit').html('<tr><td colspan="11" style="text-align:center">No data found.</td></tr>');
            }
        }
    }
}

const bindClickEventOnActionAssignedTasks=function (){  //bind work order view
    $(document).on("click", ".viewWorkOrder", function () {
        var workOrderId = $(this).data('workorder-id');
        var status = $(this).data('status');
        var doid = $(this).data('doid');
        var woplanid = $(this).data('woplan-id');
        $('#workOrderViewIdPrefix_doid').val(doid);
        getDODetails(doid, workOrderId);
        $('.nodeEditCol').hide();
        $('.nodeDetailsIcon').show();
        $('.nodeDetailCol').show();
        modalNodeWOPopup(woplanid, workOrderId);
        $('#WODetailPanel').addClass('collapse');
        $('#activityPanel').addClass('collapse');
        $('#WODetailPanel').removeClass('in');
        $('#activityPanel').removeClass('in');
        $('#nodePanel').addClass('collapse');
        $('#nodePanel').removeClass('in');
        $('#nodeEditPanel').hide();
        $('.projectDetailsIcon').hide();
        $('#btn_update_project').hide();
        $('#wODetailHeader').find('p').remove().end();
        $('#wODetailHeader').append(`<p> WO Details - ${workOrderId}</p>`);
        $('#DODetailHeader').find('p').remove().end();
        $('#DODetailHeader').append(`<p> DO Details - ${doid}</p>`);
        var editFlag = true;
        if (typeof $(this).data(C_EDIT_WO_FLAG) !== 'undefined' || $(this).data(C_EDIT_WO_FLAG) === false) {
            editFlag = false;
        }
        pwIsf.addLayer({ text: "Please wait ..." });

        $.isf.ajax({
            url: `${service_java_URL}woExecution/getCompleteWoDetails/${workOrderId}`,
            success: function (data) {
                pwIsf.removeLayer();
                if (data.isValidationFailed === false) {
                    $('#all_nodes_master').hide();
                    if (data.responseData[0].externalGroup === "ERISITE") {
                        $("#woNameDiv").css("display", "none");
                        $("#woNameDivErisite").css("display", "block");
                        $("#workOrderViewIdPrefix_full_wo_name").attr("title", data.responseData[0].wOName);
                    }
                    else {
                        $("#woNameDiv").css("display", "block");
                        $("#woNameDivErisite").css("display", "none");
                    }
                    var formElements = getElementNamesObj();
                    var valuesFromApiResponse = getValuesFromApiResponse(data);
                    // Finaly - Set values to elements
                    setValuesToElements(formElements, valuesFromApiResponse);
                    //Hide Save button on view order form
                    if ((status === "CLOSED") || (status === "DEFERRED") || (status === "REJECTED")) {
                        $('#btn_update_nodes').attr("disabled", true);
                        $("#edit_node_details").hide();

                    } else {
                        $("#edit_node_details").show();
                    }
                    $('#projectIdWarning').val(data.responseData[0].projectID);
                    $('#domainWarning').val(data.responseData[0].domainID);
                    $('#technologyWarning').val(data.responseData[0].technologyID);
                    setValuesToNEPartialView(data.responseData, 'edit');
                    getDefaultValuesForNEAndCount('edit', data.responseData);
                }
                else {
                    pwIsf.alert({ msg: data.formErrors[0], type: 'error' });
                }
                const selectedTabHref = $('#projectQueueTab').hasClass('active');
                if (selectedTabHref) {
                    $('.nodeDetailsIcon').hide();
                }
                $("#myModalMadEwo").modal('show');
            },
            error: function (xhr, status, statusText) {
                pwIsf.removeLayer();
                $("#myModalMadEwo").modal('hide');
                alert("Failed to Fetch Data");

            }
        });

    });
}

function callFor_WOForAssignedTasksForTransfer(durationText) {
    $("a.dividerDuration").removeClass("active");
    $('a.dividerDuration').each(function (e)
    {
        if ($(this).text() === durationText)
        {
            $(this).addClass('active');
        }
    });
    const getDates = getStartDateAndEndDateFromSelectedDuration(durationText);
    const statusText = $('a.dividerStatus.active').text();
    const getStatus = getWOStatusFilter(statusText);
    getWO_forAssignedTasksForTransfer(getDates.sdate, getDates.edate, getStatus);
}

function getAllCheckboxForAssignedTasks() {  
    const all = [];
    globalAssignedTasksForTransferDataTable.rows().nodes().to$().find("input[name='checkBoxAssignedTasks']:checked").each(function () {
        all.push($(this).val());
    });
    if (all.length) {
        $('#lbl_withAllSelected_assignedTasks').text(`With selected(${all.length})`);
        $('#btn_assignedTasksAllTransfer').attr('disabled', false);
        $('#btn_sendAllBackToProjectQueue').attr('disabled', false);
    } else {
        $('#btn_assignedTasksAllTransfer').attr('disabled', true);
        $('#lbl_withAllSelected_assignedTasks').text(`With selected`);
        $('#btn_sendAllBackToProjectQueue').attr('disabled', true);
    }   
}

function makeRequestForBackToProjQueue_single(woid, clickObj) {
    const woidsWithSignum = [{ "wOID": woid, "signumID": signumGlobal }];
    const defIdAutoSenseObj = new Object();
    defIdAutoSenseObj.flowchartDefID = $(clickObj).data('flowchartdefid');
    defIdAutoSenseObj.workOrderAutoSenseEnabled = $(clickObj).data('workorderautosenseenabled');
    defIdAutoSenseObj.woid = $(clickObj).data('woid');
    backToProjectQueue(woidsWithSignum, defIdAutoSenseObj);
}

function makeRequestForBackToProjQueue_multiple() {
    const all = [];
    globalAssignedTasksForTransferDataTable.rows().nodes().to$().find("input[name='checkBoxAssignedTasks']:checked").each(function () {
        all.push($(this).val());
    });

    if (all.length) {
        const woidsWithSignum = [];
        for (let i in all) {
            woidsWithSignum.push({ 'wOID': all[i], 'signumID': signumGlobal });
        }
        backToProjectQueue(woidsWithSignum, false);
    }
}

function backToProjectQueue(woidsWithSignum, defIdAutoSenseObj = '') {
    pwIsf.addLayer({ text: "Please wait ..." });
    const makeResponceHtml = function (d) {
        const responce = d;
        let html = `<table border="1"><tr>
                    <th>WOID</th>
                    <th>Status</th>
                    
                    </tr>`;
        let innerHtml = ``;
        for (let i in responce) {
            innerHtml += `<tr>
                        <td>${responce[i]['wOID']}</td>
                        <td>${(responce[i]['Status'])?'Successfully Sent':'Failed'}</td>
                        
                        </tr>`;

            // make the button disabled
            if (responce[i]['Status']) {
                $(C_BACK_TO_PQ + responce[i]['wOID']).attr('disabled', true);
                $('#btn_callAssignedTasksTransfer_' + responce[i]['wOID']).attr('disabled', true);
                $(C_BACK_TO_PQ + responce[i]['wOID']).closest('tr').find('input[name=checkBoxAssignedTasks]').attr({ 'name': 'removedNow', 'disabled': true }).hide();
                getAllCheckboxForAssignedTasks();
            }
        }
        html += innerHtml + `</table>`;
        pwIsf.alert({ msg: html, type: 'success' });
    };

    var request = $.isf.ajax({
        url: `${service_java_URL}woExecution/updateWoSignumToNull`,
        method: "POST",
        returnAjaxObj: true,
        contentType: "application/json; charset=utf-8",
        data: JSON.stringify(woidsWithSignum),
        dataType: "json"
    });

    request.done(function (msg) {
        pwIsf.removeLayer();
        makeResponceHtml(msg);
        if (defIdAutoSenseObj !== '') {
            if (defIdAutoSenseObj.workOrderAutoSenseEnabled === true || defIdAutoSenseObj.workOrderAutoSenseEnabled === 'true') {
                const autoSenseInputData = new Object();
                autoSenseInputData.WoId = defIdAutoSenseObj.woid;
                autoSenseInputData.flowchartDefID = defIdAutoSenseObj.flowchartDefID;
                autoSenseInputData.stepID = "0";
                autoSenseInputData.source = "UI";
                autoSenseInputData.signumID = signumGlobal;
                autoSenseInputData.overrideAction = "SUSPEND";
                autoSenseInputData.action = "";
                autoSenseInputData.taskID = "0";
                autoSenseInputData.startRule = null;
                autoSenseInputData.stopRule = null;
                NotifyClientOnWorkOrderSuspended(autoSenseInputData, true);
            }
        }
                
    });

    request.fail(function (jqXHR, textStatus) {
        pwIsf.removeLayer();
        pwIsf.alert({ msg: "Request failed: " + textStatus });

    });
}

function validateTransferButtonMyQueue() {
    const selectedStep = $('#select_stepName').val();
    const selectedSignum = $(C_USER_SIGNUM_TRANSFER).val();
    const validateBtnCondition = $(C_STEP_NAME_DIV).is(':visible') === true ? (selectedStep !== -1 && selectedSignum !== "") : (selectedSignum !== "");
    if (validateBtnCondition) {
        $(C_SUBMIT_MODAL).attr('disabled', false);
    }
    else {
        $(C_SUBMIT_MODAL).attr('disabled', true);
    }
}

function executeAssignedTasksForTransfer() {
    const woIds = $(C_MODAL_INPUT_WOID).val();
    const senderSignum = $(C_USER_SIGNUM_TRANSFER).val();
    let stepName = $('#select_stepName').val();
    const userComments = $(C_COMMENTS_BOX).val();

    if (woIds === '' || senderSignum === '' || senderSignum.length !== 7) {
        pwIsf.alert({ msg: 'Please select the sender Signum', type: 'warning' });
        $(C_SUBMIT_MODAL).attr('disabled', true);
        return true;
    }
    if ($(C_STEP_NAME_DIV).is(':visible')) {
        if (stepName == null || stepName === '' || stepName === '-1') {
            pwIsf.alert({ msg: 'Please select the Step Name', type: 'warning' });
            $(C_SUBMIT_MODAL).attr('disabled', true);
            return true;
        }
    }
    else {
        stepName = '';
    }

    if (userComments.length > 250) {
        pwIsf.alert({ msg: 'Please Comment within 250 characters', type: 'warning' });
        return true;
    }

    const all = woIds.split(',');
    let transferArr = {};
    if (all.length) {
        transferArr = {
            "woID": all, "senderID": signumGlobal, "receiverID": senderSignum,
            "logedInSignum": signumGlobal, "stepName": stepName,
            "userComments": userComments
        };
    }
    let makeResponceHtml = function (d) {
        const responce = d;
        let html = `<table border="1"><tr>
                    <th>WOID</th>
                    <th>Status</th>
                    
                    </tr>`;
        let innerHtml = ``;
        for (let i in responce) {
                innerHtml += `<tr>
                        <td>${responce[i]['wOID']}</td>
                        <td>${responce[i]['msg']}</td> 
                        </tr>`;
            // make the button disabled
            if (responce[i]['status']) {
                $(C_BACK_TO_PQ+ responce[i]['wOID']).attr('disabled', true);
                $('#btn_callAssignedTasksTransfer_' + responce[i]['wOID']).attr('disabled', true);
                $(C_BACK_TO_PQ + responce[i]['wOID']).closest('tr').find('input[name=checkBoxAssignedTasks]').attr({ 'name': 'removedNow', 'disabled': true }).hide();
                getAllCheckboxForAssignedTasks();
            }
        }

        html += innerHtml + `</table>`;
                pwIsf.alert({ msg: html, type: 'info' });
    };

    var request = $.isf.ajax({
        url: `${service_java_URL}woManagement/transferWorkOrder`,
        method: "POST",
        returnAjaxObj: true,
        contentType: C_CONTENT_TYPE_APPLICATION_JSON,
        data: JSON.stringify(transferArr),
        dataType: "json"
    });

    request.done(function (msg) {
        pwIsf.removeLayer();
        $(C_MODAL_TRANSFER).modal('hide');
        if (!msg.isValidationFailed) {
            pwIsf.alert({ msg: msg.formMessages[0], type: 'success', autoClose: 1 })
        } else {
            pwIsf.alert({ msg: msg.formErrors[0], type: 'error'})
        }
        getAllCheckboxForAssignedTasks();

        if ($('#woidForSingle').val() !== '') {
            let flowchartDefID = $('#flowchartDefID').val();
            const workOrderAutoSenseEnabled = $('#workOrderAutoSenseEnabled').val();
            const woidForSingle = $('#woidForSingle').val();
            if (workOrderAutoSenseEnabled === true || workOrderAutoSenseEnabled == 'true') {
                let autoSenseInputData = new Object();
                autoSenseInputData.WoId = woidForSingle;
                autoSenseInputData.flowchartDefID = flowchartDefID;
                autoSenseInputData.stepID = "0";
                autoSenseInputData.source = "UI";
                autoSenseInputData.signumID = signumGlobal;
                autoSenseInputData.overrideAction = "SUSPEND";
                autoSenseInputData.action = "";
                autoSenseInputData.taskID = "0";
                autoSenseInputData.startRule = null;
                autoSenseInputData.stopRule = null;
                NotifyClientOnWorkOrderSuspended(autoSenseInputData, true);
            }
        }
        else {
            let autoSenseInputDataArr = [];
            let flowchartDefID = $('#flowchartDefID').val().split(',');
            let workOrderAutoSenseEnabled = $('#workOrderAutoSenseEnabled').val().split(',');
            let woidArr = woIds.split(',');

            for (let i = 0; i < woidArr.length; i++) {
                if (workOrderAutoSenseEnabled[i] === true || workOrderAutoSenseEnabled[i] == 'true') {
                    let autoSenseInputData = new Object();
                    autoSenseInputData.WoId = woidArr[i];
                    autoSenseInputData.flowchartDefID = flowchartDefID[i];
                    autoSenseInputData.stepID = "0";
                    autoSenseInputData.source = "UI";
                    autoSenseInputData.signumID = signumGlobal;
                    autoSenseInputData.overrideAction = "SUSPEND";
                    autoSenseInputData.action = "";
                    autoSenseInputData.taskID = "0";
                    autoSenseInputData.startRule = null;
                    autoSenseInputData.stopRule = null;
                    autoSenseInputDataArr.push(autoSenseInputData);
                }
            }
            if (autoSenseInputDataArr.length > 0) {
                NotifyClientOnWorkOrderMassSuspended(autoSenseInputDataArr, true);
            }
        }

    });

    request.fail(function (jqXHR, textStatus) {
        pwIsf.removeLayer();
        pwIsf.alert({ msg: "Request failed: " + textStatus });

    });

}

function callAllSelectedAssignedTasksTransfer() {
    pwIsf.addLayer({ text: "Please wait ..." });
    let all = [];
    let defId = [];
    let autosense = [];
    globalAssignedTasksForTransferDataTable.rows().nodes().to$().find("input[name='checkBoxAssignedTasks']:checked").each(function () {
        all.push($(this).val());
        let woid = $(this).val();
        let btnObj = this.parentElement.getElementsByTagName('button')["btn_callAssignedTasksTransfer_" + woid];
        let flowchartDefID = $(btnObj).data('flowchartdefid');
        let workOrderAutoSenseEnabled = $(btnObj).data('workorderautosenseenabled');

        defId.push(flowchartDefID);
        autosense.push(workOrderAutoSenseEnabled);

    });

    if (all.length) {
        let woids = [];
        for (let i in all) {
            woids.push(all[i]);
        }

        $(C_MODAL_INPUT_WOID).val(woids.join(','));
        $('#flowchartDefID').val(defId.join(','));
        $('#workOrderAutoSenseEnabled').val(autosense.join(','));
        if (woids.length > 5) {
            $('#modal_input_woid_totalLabel').show();
            $('#modal_input_woid_totalLabel').attr('title', woids.join(',')).find('.badge').text(woids.length);
        } else {
            $('#modal_input_woid_totalLabel').hide();
        }
        $(C_STEP_NAME_DIV).hide();
        $('#stepNameLabel').hide();
        $('#commentDiv').hide();
        $('#commentLabel').hide();
        $(C_COMMENTS_BOX).val("");
        $(C_USER_SIGNUM_TRANSFER).val('');
        $(C_SUBMIT_MODAL).attr('disabled', true);
        $(C_MODAL_TRANSFER).modal('show');

        //reset these values for multiple call
        
        $('#woidForSingle').val('');
    }

    pwIsf.removeLayer();

}

function getAllStepNameForTransferMyQueue(woID) {
    $.isf.ajax({
        async: false,
        type: "GET",
        url: service_java_URL + "woExecution/getAllStepsByWOID?woID=" + woID,
        dataType: "json",
        crossdomain: true,
        success: function (data) {
            if (data.isValidationFailed == false) {
                var workFlowSteps = data.responseData;
                $("#select_stepName").empty();
                $("#select_stepName").append("<option value=-1>Select Step Name</option>");
                $('#select_stepName').index = -1;
                $.each(workFlowSteps, function (i, d) {
                    $('#select_stepName').append('<option value="' + d["stepName"] + '">' + d["stepName"] + '</option>');
                })           
            }            
        },
        complete: function () {
            pwIsf.removeLayer();
        }
    });

    $("#select_stepName").on('change', function () {
        validateTransferButtonMyQueue();
    })
}

function callAssignedTasksTransfer(woid, clickObj) {
    pwIsf.addLayer({ text: "Please wait ..." });
    let flowchartDefID = $(clickObj).data('flowchartdefid');
    let workOrderAutoSenseEnabled = $(clickObj).data('workorderautosenseenabled');
    let woidForSingle = $(clickObj).data('woid');
    $('#flowchartDefID').val(flowchartDefID);
    $('#workOrderAutoSenseEnabled').val(workOrderAutoSenseEnabled);
    $('#woidForSingle').val(woidForSingle);
    $(C_MODAL_INPUT_WOID).val(woid);
    $('#modal_input_woid_totalLabel').hide();
    $(C_STEP_NAME_DIV).show();
    $('#stepNameLabel').show();
    $('#commentDiv').show();
    $('#commentLabel').show();
    $(C_COMMENTS_BOX).val("");
    $(C_USER_SIGNUM_TRANSFER).val('');
    $(C_SUBMIT_MODAL).attr('disabled', true);
    $(C_MODAL_TRANSFER).modal('show');
    pwIsf.removeLayer();   
}

function chkAll_assignedTasks(chk) {
    let chkbox = $(chk);
    if (chkbox.prop('checked') == true) {
        $("input[name='checkBoxAssignedTasks']").each(function () {
            $(this).prop('checked', true);
        });
    } else {
        $("input[name='checkBoxAssignedTasks']").each(function () {
            $(this).prop('checked', false);
        });
    }

    getAllCheckboxForAssignedTasks();
}

function getWOStatusFilter(statusText) {
    
    let woStatus = '';
    switch (statusText) {
        case "All":
            woStatus = "";
            break;
        case "Assigned":
            woStatus = "Assigned";
            break;
        case "InProgress":
            woStatus = "InProgress";
            break;
        case "OnHold":
            woStatus = "OnHold";
            break;
        case "Reopened":
            woStatus = "Reopened";
            break;
    }

    return woStatus;
    
}

function callFor_getStatusWO(el) {
    let statusText = el.innerText;
    $("a.dividerStatus").removeClass("active");
    $(el).addClass("active");

    let getStatus = getWOStatusFilter(statusText);
    let durationText = $(' .transfer a.dividerDuration.active').text();
    let getDates = getStartDateAndEndDateFromSelectedDuration(durationText);

    getWO_forAssignedTasksForTransfer(getDates.sdate, getDates.edate, getStatus);
}

function getWO_forAssignedTasksForTransfer(startDate, endDate, status) {

    if (startDate == "all" || endDate == "all") {
        var today = new Date();
        var tomorrow = new Date(today);
        tomorrow.setDate(tomorrow.getDate() + 1);
        startDate = formatted_date(today);
        endDate = formatted_date(tomorrow);
    }
    if ($.fn.dataTable.isDataTable('#table_WO_for_assignedWorkForTransfer')) {
        globalAssignedTasksForTransferDataTable.destroy();
        $("#table_WO_for_assignedWorkForTransfer").empty();
    }
    $("#table_WO_for_assignedWorkForTransfer").html('');
    pwIsf.addLayer({ text: "Please wait ..." });
    let urlParams = ``;
    if (startDate != 'NA') {
         urlParams += `&startDate=${startDate}&endDate=${endDate}`;
    } 
    if (status != '') {
        urlParams += `&woStatus=${status}`;
    }
    var serviceUrl = service_java_URL + "woExecution/getAssignedWorkOrders?signum=" + signumGlobal + urlParams;
    if (ApiProxy === true) {
        serviceUrl = service_java_URL + "woExecution/getAssignedWorkOrders?signum=" + encodeURIComponent(signumGlobal + urlParams);
    }
    $.isf.ajax({
        url: serviceUrl,
        success: function (data) {
            pwIsf.removeLayer();
           if (data.length > 0) {
            ConvertTimeZones(data, getAssignedWorkOrders_tzColumns);
            $.each(data, function (i, d) {
                localStorage.setItem(d.woPlanId + "_" + d.woId, JSON.stringify(d.listOfNode));
                d.act = '<div style="display:flex;">';
                d.act += '<input type="checkbox" onchange="getAllCheckboxForAssignedTasks()" value="' + d.woId + '" name="checkBoxAssignedTasks">&nbsp;&nbsp;&nbsp;';
                d.act += '<a href="#" class="viewWorkFlow icon-view" title=" Workflow shown here is as per your current proficiency for this Workorder " onclick="flowChartOpenInNewWindow(' + ' \'FlowChart\?mode\=view&woID=' + d.woId + '&proficiencyId=' + d.proficiencyID + '\' ' + ')">' + getIcon('flowchart') + '</a>&nbsp;&nbsp;';

                d.act += '<a href="#" class="viewWorkOrder icon-view" onclick="hideNEAddEdit();" data-doid="' + d.doid + '" data-woplan-id="' + d.woPlanId+'" data-status="' + d.status + '" data-workorder-id="' + d.woId + '" title="View Work Order">' + getIcon('view') + '</a>&nbsp;&nbsp;';

                d.act += '<button class="btn btn-success btn-xs" id="btn_callAssignedTasksTransfer_' + d.woId + '" type="button" data-woid=' + d.woId + ' data-workorderautosenseenabled=' + d.workOrderAutoSenseEnabled + ' data-flowchartdefid=' + d.defID + ' onclick="callAssignedTasksTransfer(' + d.woId + ', this),	getAllStepNameForTransferMyQueue(' + d.woId + ');" title="Transfer"><i class="fa fa-exchange"></i></button >';
                if (d.status == 'ASSIGNED') {
                    d.act += '<button class="btn btn-warning btn-xs" id="btn_backToProjectQueue_' + d.woId + '" type="button" data-woid=' + d.woId + ' data-workorderautosenseenabled=' + d.workOrderAutoSenseEnabled + ' data-flowchartdefid=' + d.defID + ' onclick="makeRequestForBackToProjQueue_single(' + d.woId + ', this)" title="Back to project queue"><i class="fa fa-reply" aria-hidden="true"></i></button >';
                }
                d.act += '</div>';
            });
           
            $("#table_WO_for_assignedWorkForTransfer").append($(`<tfoot><tr>
            <th></th> <th></th> <th></th> <th></th> <th></th> <th></th> <th></th>
            <th></th> <th></th> <th></th> <th></th> <th></th> <th></th> <th></th> <th></th> <th></th><th></th><th></th><th></th>
            </tr></tfoot>`));
               globalAssignedTasksForTransferDataTable = $("#table_WO_for_assignedWorkForTransfer").DataTable({
                searching: true,
                responsive: true,
                destroy: true,
                data: data,
                colReorder: true,
                dom: 'Bfrtip',
                buttons: ['colvis', 'excelHtml5'],
                columns: [
                    { "title": "Action", "data": "act", orderable: false },
                    { "title": "Project ID", "data": "projectId" },
                    { "title": "Project Name", "data": "projectName" },
                    {
                        "title": "Network Element Name/ID", "targets": 'no-sort', "orderable": false, "data": null,
                        "defaultContent": "",
                        "render": function (d, type, row, meta) {
                            if (d.listOfNode[0].nodeNames == null)
                                d.listOfNode[0].nodeNames = "";
                            if (d.listOfNode[0].nodeNames.length < 40) {
                                return '<div class="nodes" >' + d.listOfNode[0].nodeNames.substr(0, 40) + '<i data-toggle="tooltip" style="cursor:pointer" title="Network Element Details"class="fa fa-list"  onclick="AssignToMe_modalNodeDE(' + d.woPlanId + ',' + d.woId + ')"></i></div>';
                            }
                            else
                                return '<div class="nodes">' + d.listOfNode[0].nodeNames.substr(0, 40) + '<a id="showPara' + d.woId + '" style="cursor:pointer" onclick="showParaDE(' + d.woId + ')">.....</a><p id="paraShowhide' + d.woId + '" style="display:none">' + d.listOfNode[0].nodeNames.substr(11, d.listOfNode[0].nodeNames.length) + '</p><i data-toggle="tooltip" id="collapseParagraph' + d.woId + '"  title="collapse"class="fa fa-chevron-up" style="display:none;cursor:pointer" onclick="collapseParaDE(' + d.woId + ')"></i>&nbsp;&nbsp;<i data-toggle="tooltip" id="showList' + d.woId + '"  title="Network Element Details"class="fa fa-list" style="display:none;cursor:pointer" onclick="AssignToMe_modalNodeDE(' + d.woPlanId + ',' + d.woId + ')"></i></div>';
                        }
                    },
                    { "title": "Deliverable Name", "data": "deliverableName" },
                    { "title": "Deliverable Unit", "data": "deliverableUnitName" },
                    { "title": "DOID", "data": "doid" },
                    { "title": "WO ID", "data": "woId" },
                    { "title": "WO Name", "data": "woName" },
                    { "title": "WF Name", "data": "workFlowName" },
                    { "title": "Created By", "data": "employeeName" },
                    { "title": "Priority", "data": "priority" },
                    { "title": "Status", "data": "status" },
                    { "title": "SLA", "data": "slaHours" },
                    { "title": "Tech.", "data": "technology" },
                    { "title": "Start Date", "data": "startDate" },
                    { "title": "End Date", "data": "endDate" },
                    { "title": "Activity", "data": "activity" },
                    { "title": "SubActivity", "data": "subActivity" },
                ],
                "displayLength": 10,
                initComplete: function () {
                    $('#table_WO_for_assignedWorkForTransfer tfoot th').each(function (i) {
                        var title = $('#table_WO_for_assignedWorkForTransfer thead th').eq($(this).index()).text();
                        if ((title != "Action")) {
                            $(this).html('<input type="text" class="form-control" style="font-size:12px;"  placeholder="Search ' + title + '" data-index="' + i + '" />');

                        } else {
                            $(this).html('<input type="checkbox" onchange="chkAll_assignedTasks(this)" id="chk_selectAllForAssignedTasks"> <label for="chk_selectAllForAssignedTasks">Select All</label>');
                        }
                    });
                    var api = this.api();
                    api.columns().every(function () {
                        var that = this;
                        $('input[type=text]', this.footer()).on('keyup change', function () {
                            if (that.search() !== this.value) {
                                that
                                    .columns($(this).parent().index() + ':visible')
                                    .search(this.value)
                                    .draw();
                            }
                        });
                    });
                },
            });
            $('#table_WO_for_assignedWorkForTransfer tfoot').insertAfter($('#table_WO_for_assignedWorkForTransfer thead'));
            bindClickEventOnActionAssignedTasks();
            getAllCheckboxForAssignedTasks();
        }
           else {
                $('#table_WO_for_assignedWorkForTransfer').html('<tr><td colspan="11" style="text-align:center">No data found.</td></tr>');

        }
            },    
        error: function (xhr, status, statusText) {
            pwIsf.removeLayer();
            $('#table_WO_for_assignedWorkForTransfer').html('<tr><td colspan="11" style="text-align:center">No data found.</td></tr>');
        }
    });
}