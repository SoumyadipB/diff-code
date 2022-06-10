let global_assignToMeDataTable;
const woNodesDetailsId = '#table_wo_nodes_detail';
const noDataHtml = getNoDataRow();
//
function modalNodeDetail(wOPlanID, wOID) {
    if ($.fn.dataTable.isDataTable(woNodesDetailsId)) {
        $(woNodesDetailsId).DataTable().destroy();
    }
    const nodeList = localStorage.getItem(`${wOPlanID}_${wOID}`);
    if (nodeList) {
        const listOfNode = JSON.parse(nodeList);
        if (listOfNode.length > 0) {
            const isExist = checkNodeExist(listOfNode);
            if (isExist === true) {
                $(woNodesDetailsId).DataTable({
                    "data": listOfNode,
                    "destroy": true,
                    searching: true,
                    order: [1],
                    "columns": [
                        {
                            "title": neType,
                            "data": "nodeType"
                        },
                        {
                            "title": neName,
                            "data": "nodeNames"
                        },
                        {
                            "title": neMarket,
                            "data": "market"
                        }
                    ]
                });
            } else {
                $(woNodesDetailsId).html(noDataHtml);
            }
        } else {
            $(woNodesDetailsId).html(noDataHtml);
        }
    }
    $('#modal_wo_nodes_detail').modal('show');
}
function AssignToMe_modalNodeDE(wOPlanID, wOID) {
    if ($.fn.dataTable.isDataTable('#table_wo_nodes_de')) {
        table.destroy();
        $("#table_wo_nodes_de").html('');
    }
    const nodeList = localStorage.getItem(`${wOPlanID}_${wOID}`);
    if (nodeList) {
        let listOfNode = JSON.parse(nodeList);
        let nodeNameStringArray = [];
        $.each(listOfNode, function (i, d) {
            const nodeNameString = d.nodeNames;
            nodeNameStringArray = nodeNameString.split(",");
            if (nodeNameStringArray[0] === "" && d.nodeType === "" && d.market === "") {
                listOfNode = [];
            }
        });
        if (listOfNode.length > 0) {
            const isExist = checkNodeExist(listOfNode);
            if (isExist === true) {
                table = $('#table_wo_nodes_de').DataTable({

                    "data": listOfNode,
                    "destroy": true,
                    searching: true,
                    order: [1],
                    "columns": [
                        {
                            "title": neType,
                            "data": "nodeType"
                        },
                        {
                            "title": neName,
                            "data": "nodeNames"
                        },
                        {
                            "title": neMarket,
                            "data": "market"
                        }
                    ]
                });
            } else {
                $('#table_wo_nodes_de').html(noDataHtml);
            }
        } else {
            $('#table_wo_nodes_de').html(noDataHtml);
        }

    }
    $('#modal_wo_nodes_de').modal('show');
    $('#table_wo_nodes_de td').css('overflow', 'auto');
    $('#table_wo_nodes_de td').css('max-width', '0px');
}

function getDODetails(doid, woid) {

    $.isf.ajax({
        url: `${service_java_URL}woManagement/getWorkOrdersByDoid?doid=${doid}`,
        success: function (responseData) {        
            oTableDODetail = $('#table_do_details').DataTable({
                "data": responseData,
                "destroy": true,
                "searching": true,
                order: [1],
                "columns": [

                    {
                        "title": "DOID",
                        "data": null,
                        "render": function (data, type, row) {
                            if (data.woid == woid) {
                                return '<i class="fa fa-chevron-circle-right" style="color:green"></i>' + data.doid;
                            } else
                                return data.doid;

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
                            woViewDataArray = [];
                            let x = data.listOfNode;
                            let nodes = '';
                            for (var i = 0; i < x.length; i++) {
                                nodes = x[i].nodeNames + ',' + nodes;
                                nodes = nodes.replace(/,\s*$/, '');
                            }
                            if (nodes == null || nodes == "null")
                                nodes = "";
                            localStorage.setItem(data.woplanid + "_" + data.woid, JSON.stringify(data.listOfNode));
                            woViewDataArray.push(
                                {
                                    "NodeNames": nodes
                                });
                            var nodesView = `<i data-toggle="tooltip" id="showList${data.woid}"  title="Network Element Details" class="fa fa-list" style="cursor:pointer"`;
                            nodesView = `${nodesView}onclick = "AssignToMe_modalNodeDE(${data.woplanid},${data.woid})" ></i>`;

                            return nodesView;
                        }
                    },
                    {
                        "title": "WFID_WFName_WFVErsion",
                        "data": null,
                        "render": function (data, type, row) {
                            return data.wfInfo;
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
                    var api = this.api();
                    api.columns().every(function () {
                        var that = this;
                        $('input').on('keyup change', function () {
                            if (that.search() == this.value) {
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
            console.log(`An error occurred on getDomain: ${xhr.error}`);
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
            })
        if (listOfNode.length > 0) {
            const isExist = checkNodeExist(listOfNode);
            if (isExist === true) {
                $('#table_wo_nodes_detail_edit').DataTable({
                    "data": listOfNode,
                    "destroy": true,
                    searching: true,
                    order: [1],
                    "columns": [
                        {
                            "title": neType,
                            "data": "nodeType"
                        },
                        {
                            "title": neName,
                            "data": "nodeNames"
                        },
                        {
                            "title": neMarket,
                            "data": "market"
                        }
                    ]
                });
            } else {
                $('#table_wo_nodes_detail_edit').html(noDataHtml);
            }
        } else {
            $('#table_wo_nodes_detail_edit').html(noDataHtml);
        }

    }
}

let bindClickEventOnAction = function () {  //bind work order view


    //bind work order view
    $(document).off("click", ".viewWorkOrder").on("click", ".viewWorkOrder", function () {
        var workOrderId = $(this).data('workorder-id');
        var woStatus = $(this).data('status');
        var doid = $(this).data('doid');
        var woplanid = $(this).data('woplan-id');
        $('#workOrderViewIdPrefix_doid').val(doid);
        getDODetails(doid, workOrderId);
        $('#WODetailPanel').addClass('collapse');
        $('#activityPanel').addClass('collapse');
        $('#WODetailPanel').removeClass('in');
        $('#activityPanel').removeClass('in');
        $('.nodeDetailsIcon').hide();
        $('#nodeEditPanel').hide();
        $('#nodePanel').addClass('collapse');
        $('#nodePanel').removeClass('in');
        modalNodeWOPopup(woplanid, workOrderId);
        $('.projectDetailsIcon').hide();
        $('#btn_update_project').hide();
        $('#wODetailHeader').find('p').remove().end();
        $('#wODetailHeader').append('<p> WO Details - ' + workOrderId + '</p>');
        $('#DODetailHeader').find('p').remove().end();
        $('#DODetailHeader').append('<p> DO Details - ' + doid + '</p>');
        var editFlag = true;
        var getEditFlag = $(this).data('edit-work-order-flag');
        if (typeof $(this).data('edit-work-order-flag') !== 'undefined' || $(this).data('edit-work-order-flag') === false) {
            editFlag = false;
        }
        pwIsf.addLayer({ text: "Please wait ..." });

        $.isf.ajax({
            url: `${service_java_URL}woExecution/getCompleteWoDetails/${workOrderId}`,
            success: function (data) {
                pwIsf.removeLayer();
                $('#all_nodes_master').hide();
                if (data.isValidationFailed === false) {
                    if (data.responseData[0].externalGroup == "ERISITE") {
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
                    if ((woStatus == "CLOSED") || (woStatus == "DEFERRED") || (woStatus == "REJECTED")) {
                        $('#btn_update_nodes').attr("disabled", true);
                        $("#edit_node_details").hide();

                    } else {
                        $("#edit_node_details").show();
                    }

                    $('#projectIdWarning').val(data.responseData[0].projectID);
                    $('#domainWarning').val(data.responseData[0].domainID);
                    $('#technologyWarning').val(data.responseData[0].technologyID);
                }
                else {
                    pwIsf.alert({ msg: data.formErrors[0], type: 'error' });
                }
                let selectedTabHref = $('#projectQueueTab').hasClass('active');
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
};

function getStartDateAndEndDateFromSelectedDuration(text, el) {
    let durationText = text;
    var timeZone = localStorage.getItem("UserTimeZone");
    if (timeZone == null || timeZone == "" || timeZone == undefined) {
        timeZone = "Asia/Calcutta";
    }
    let startDate = 'NA';
    let endDate = 'NA';
    let m = '';
    let d = '';
    let y = '';
    let today = new Date();
    let day = today.getDay();
    let diff = '';
    let firstDay1 = '';
    let lastDay1 = '';
    if (durationText.toLowerCase() != 'all') {

        let formatted_date = function (date) {
            m = ("0" + (date.getMonth() + 1)).slice(-2); // in javascript month start from 0.
            d = ("0" + date.getDate()).slice(-2); // add leading zero 
            y = date.getFullYear();
            return y + '-' + m + '-' + d;
        };
        switch (durationText) {
            case "Today":
                var tomorrow = new Date(today);
                var today1 = moment(today.toString()).tz(timeZone).format();
                tomorrow.setDate(tomorrow.getDate() + 0);
                var tomorrow1 = moment(tomorrow.toString()).tz(timeZone).format();
                startDate = today1.split("T")[0];
                endDate = tomorrow1.split("T")[0];
                break;

            case "This Week":
                diff = today.getDate() - day + (day == 0 ? -6 : 1); // 0 for sunday
                var week_start_tstmp = today.setDate(diff);
                var week_start = new Date(week_start_tstmp);
                var week_start1 = moment(week_start.toString()).tz(timeZone).format();
                startDate = week_start1.split("T")[0];
                var week_end = new Date(week_start_tstmp);  // first day of week 
                week_end = new Date(week_end.setDate(week_end.getDate() + 6));
                var week_end1 = moment(week_end.toString()).tz(timeZone).format();
                endDate = week_end1.split("T")[0];
                break;
            case "Last Week":
                diff = today.getDate() - day + (day == 0 ? -13 : -6);
                var week_start_tstmp = today.setDate(diff);
                var week_start = new Date(week_start_tstmp);
                var week_start1 = moment(week_start.toString()).tz(timeZone).format();
                startDate = week_start1.split("T")[0];
                var week_end = new Date(week_start_tstmp);  // first day of week 
                week_end = new Date(week_end.setDate(week_end.getDate() + 6));
                var week_end1 = moment(week_end.toString()).tz(timeZone).format();
                endDate = week_end1.split("T")[0];
                break;
            case "Next Week":
                diff = today.getDate() + (8 - (day == 0 ? 7 : day));
                var week_start_tstmp = today.setDate(diff);
                var week_start = new Date(week_start_tstmp);
                var week_start1 = moment(week_start.toString()).tz(timeZone).format();
                startDate = week_start1.split("T")[0];
                var week_end = new Date(week_start_tstmp);  // first day of week 
                week_end = new Date(week_end.setDate(week_end.getDate() + 5));
                var week_end1 = moment(week_end.toString()).tz(timeZone).format();
                endDate = week_end1.split("T")[0];
                break;
            case "This Month":
                y = today.getFullYear();
                m = today.getMonth();
                var firstDay = new Date(y, m, 1);
                var lastDay = new Date(y, m + 1, 0);
                 firstDay1 = moment(firstDay.toString()).tz(timeZone).format();
                 lastDay1 = moment(lastDay.toString()).tz(timeZone).format();
                startDate = firstDay1.split("T")[0];
                endDate = lastDay1.split("T")[0];
                break;
            case "Last Month":
                y = today.getFullYear(), m = today.getMonth();
                var firstDay = new Date(y, m - 1, 1);
                var lastDay = new Date(y, m, 0);
                 firstDay1 = moment(firstDay.toString()).tz(timeZone).format();
                 lastDay1 = moment(lastDay.toString()).tz(timeZone).format();
                startDate = firstDay1.split("T")[0];
                endDate = lastDay1.split("T")[0];
                break;

            case "Next Month":
                y = today.getFullYear(), m = today.getMonth();
                var firstDay = new Date(y, m + 1, 1);
                var lastDay = new Date(y, m + 2, 0);
                 firstDay1 = moment(firstDay.toString()).tz(timeZone).format();
                 lastDay1 = moment(lastDay.toString()).tz(timeZone).format();
                startDate = firstDay1.split("T")[0];
                endDate = lastDay1.split("T")[0];
                break;

            case "Next 3 Months":
                y = today.getFullYear(), m = today.getMonth();
                var firstDay = new Date(y, m + 1, 1);
                var lastDay = new Date(y, m + 4, 0);
                 firstDay1 = moment(firstDay.toString()).tz(timeZone).format();
                 lastDay1 = moment(lastDay.toString()).tz(timeZone).format();
                startDate = firstDay1.split("T")[0];
                endDate = lastDay1.split("T")[0];
                break;
            case "Last 3 Months":
                 firstDay1 = getStartDateWhileLastThreeMonthSelect(today, timeZone);
                 lastDay1 = getEndDateWhileLastThreeMonthSelect(today, timeZone);
                startDate = firstDay1.split("T")[0];
                endDate = lastDay1.split("T")[0];
                break;
        }
    }
    return { 'sdate': startDate, 'edate': endDate };
}

var isPQTableIsNotRequiredToDisplay = false;

function onChangeDoStatus() {
    isPQTableIsNotRequiredToDisplay = true;
    const durationText = $('a.dividerDate.active').text();
    const status = $('a.dividerStatusPQ.active').text();
    const getDates = getStartDateAndEndDateFromSelectedDuration(durationText);
    const priority = $('a.dividerPriorityProjectQueue.active').text();
    const doStatus = $('#do_status option:selected').val();
    filterValidationAndPopulateData(getDates.sdate, getDates.edate, priority, status, doStatus);
}

function callFor_getPriorityWO(priorityText) {
    isPQTableIsNotRequiredToDisplay = true;
    $("a.dividerPriorityProjectQueue").removeClass("active");
    $(".linkPriority").removeClass("active");
    let priority = "";
    $('a.dividerPriorityProjectQueue').each(function (e) {
        if ($(this).text() == priorityText) {
            $(this).addClass('active');
            priority = $(this).text();
        }
    });
    $('.linkPriority').each(function (e) {
        if ($(this).text() == priorityText) {
            $(this).addClass('active');
        }
    });
    const durationText = $('a.dividerDate.active').text();
    const getDates = getStartDateAndEndDateFromSelectedDuration(durationText);
    const doStatus = $('#do_status option:selected').val();
    const woStatus = $('a.dividerStatusPQ.active').text();
    filterValidationAndPopulateData(getDates.sdate, getDates.edate, priority, woStatus, doStatus);
}

function projectQWOStatus(status) {
    isPQTableIsNotRequiredToDisplay = true;
    $("a.dividerStatusPQ").removeClass("active");
    $(".linkStatus").removeClass("active");
    let woStatus = "";
    $('a.dividerStatusPQ').each(function (e) {
        if ($(this).text() === status) {
            $(this).addClass('active');
            woStatus = $(this).text();
        }
    });
    $('.linkStatus').each(function (e) {
        if ($(this).text() === status) {
            $(this).addClass('active');
        }
    });
    const durationText = $('a.dividerDate.active').text();
    const priority = $('a.dividerPriorityProjectQueue.active').text();
    const getDates = getStartDateAndEndDateFromSelectedDuration(durationText);
    const doStatus = $('#do_status option:selected').val();
    
    filterValidationAndPopulateData(getDates.sdate, getDates.edate, priority, status, doStatus);
}


function clearFields() {

    $("#pStart_Date").val("");
    $("#pEnd_Date").val("");
    $("#btnCompetence").attr("disabled", false);
}
function filterbyDate() {
    $("a.dividerPriorityProjectQueue").removeClass("active");
    $('a.dividerPriorityProjectQueue').each(function (e) {
        if ($(this).text() === "Normal") {
            $(this).addClass('active');
        }
    });
    const doStatus = $('#do_status option:selected').val();
    const startDate = $("#pStart_Date").val();
    const endDate = $("#pEnd_Date").val();
    const priority = $('a.dividerPriorityProjectQueue.active').text();
    const status = $('a.dividerStatusPQ.active').text();
    filterValidationAndPopulateData(startDate, endDate, priority, status, doStatus); 
}


let flowChartOpenInNewWindow = function (url) {
    var width = window.innerWidth * 0.85;
    // define the height in
    var height = width * window.innerHeight / window.innerWidth;
    // Ratio the hight to the width as the user screen ratio
    window.open(url + '&commentCategory=WO_LEVEL', 'newwindow', 'width=' + width + ', height=' + height + ', top=' + ((window.innerHeight - height) / 2) + ', left=' + ((window.innerWidth - width) / 2));
}

function filterValidationAndPopulateData(startDate, endDate, priority, woStatus, doStatus) {
    const projectIdList = $('#selectprojId').val();
    if ((startDate !== "" && startDate !== null && startDate !== undefined) && (endDate === "" || endDate === null || endDate === undefined)) {
        pwIsf.alert({ msg: 'End Date should be selected', type: 'warning' });
    }
    else if ((endDate !== "" && endDate !== null && endDate !== undefined) && (startDate === "" || startDate === null || startDate === undefined)) {
        pwIsf.alert({ msg: 'Start Date should be selected', type: 'warning' });
    }
    else if (endDate < startDate) {
        pwIsf.alert({ msg: 'End Date should not be before Start Date', type: 'warning' });
    } else if (projectIdList === null || projectIdList === 'null') {
        pwIsf.alert({ msg: 'ProjectID should be selected', type: 'warning' });
    }
    else {
       const isSelected= startDateAndEndDateSelectedFromPicker();
        if (isSelected) {
            const duration = durationCheck();
            if (duration > 180) {
                pwIsf.alert({ msg: 'Duration should be less than 180', type: 'warning' });
            }
            else {
                getProjectQueueWorkOrders(startDate, endDate, priority, woStatus, doStatus, projectIdList.join(','));
            }
        }
        else {
            getProjectQueueWorkOrders(startDate, endDate, priority, woStatus, doStatus, projectIdList.join(','));
        }
    }
}

function getAllCheckbox() {
    
    let all = [];
    
    global_assignToMeDataTable.rows().nodes().to$().find("input[name='checkBoxAssignToMe']:checked").each(function () {
        all.push($(this).val());
    });
    
    if (all.length) {
        $('#btn_assignAllSelectedToMe').text(`Assign All selected(${all.length}) To Me`).attr('disabled', false);

    } else {
        $('#btn_assignAllSelectedToMe').text('Assign All selected To Me').attr('disabled', true);

    }
    
}

function deactivateAssignToMe(woidsWithSignum) {
   const btnId = `#btn_callAssignToMe_${woidsWithSignum.workOrderModel[0].wOID}`;
    $(btnId).removeClass('icon-notAssigned icon-assigned');
    $(btnId).addClass('icon-assignedToMe disabled');
    $(btnId).removeAttr('onclick data-woid data-workorderautosenseenabled data-flowchartdefid title');
    $(btnId).attr('title','Assigned');
}

function executeAssignToMe(woId,woidsWithSignum = {}) {
    let makeResponceHtml = function (d) {
        let responce = d;
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
        }
        html += innerHtml + `</table>`;
        pwIsf.alert({ msg: html, type: 'success' });
    };

    const request = $.isf.ajax({
        url: `${service_java_URL}woExecution/assignWo`,
        method: "POST",
        returnAjaxObj: true,
        contentType: "application/json; charset=utf-8",
        data: JSON.stringify(woidsWithSignum),
        dataType: "json"
    });

    request.done(function (msg) {
        pwIsf.removeLayer();        
        makeResponceHtml(msg);
        deactivateAssignedToMeAndUpdateSignum(woidsWithSignum, woId);
        if ($('#woid').val() != '') {
            let flowchartDefID = $('#flowchartDefID').val();
            let workOrderAutoSenseEnabled = $('#workOrderAutoSenseEnabled').val();
            let woid = $('#woid').val();

            if (workOrderAutoSenseEnabled ===true || workOrderAutoSenseEnabled === 'true') {
                let autoSenseInputData = new Object();
                autoSenseInputData.WoId = woid;
                autoSenseInputData.flowchartDefID = flowchartDefID;
                autoSenseInputData.stepID = "0";
                autoSenseInputData.source = "UI";
                autoSenseInputData.signumID = signumGlobal;
                autoSenseInputData.overrideAction = "SUSPEND";
                autoSenseInputData.action = "";
                autoSenseInputData.taskID = "0";
                autoSenseInputData.startRule = null;
                autoSenseInputData.stopRule = null;
        
            }
        }

    });

    request.fail(function (jqXHR, textStatus) {
        pwIsf.removeLayer();
        pwIsf.alert({ msg: `Request failed: ${textStatus}` });
    });

}


function callAssignToMe(woid,clickedObject) {
    let obj = assignedToMeClikedDetails(clickedObject, woid);
    woAssignedOrNot($(clickedObject).data('woid'), obj); 
}

function assignedToMeClikedDetails(clickedObject, woid) {
    pwIsf.addLayer({ text: "Please wait ..." });
    let wids = [];
    let obj = {};
    let flowchartDefID = $(clickedObject).data('flowchartdefid');
    let workOrderAutoSenseEnabled = $(clickedObject).data('workorderautosenseenabled');
    let woidAssigntome = $(clickedObject).data('woid');
    $('#flowchartDefID').val(flowchartDefID);
    $('#workOrderAutoSenseEnabled').val(workOrderAutoSenseEnabled);
    $('#woid').val(woidAssigntome);
    wids.push({ 'wOID': woid, 'signumID': signumGlobal, 'senderSignum': signumGlobal });
    obj.workOrderModel = wids;
    return obj;
}

function chkAll_assignToMe(chk) {

    const chkbox = $(chk);

    if (chkbox.prop('checked') === true) {
        $("input[name='checkBoxAssignToMe']").each(function () {
            $(this).prop('checked', true);
        });
    } else {
        $("input[name='checkBoxAssignToMe']").each(function () {
            $(this).prop('checked', false);
        });
    }
}

function collapseParaDE(item) {
    $('#paraShowhide' + item).hide();
    $('#showPara' + item).show();
    $('#collapseParagraph' + item).hide();
    $('#showList' + item).hide();
}

function showParaDE(item) {
    $('#paraShowhide' + item).show();
    $('#showPara' + item).hide();
    $('#collapseParagraph' + item).show();
    $('#showList' + item).show();
}


function callCommentsProjectQueue(details, pId) {

    var config = {};
    config.pageId = 'WORK_ORDER_' + pId;
    config.title = "Project Queue Work Order History (WO Id:" + pId + ")";
    config.contextEnabled = false;
    config.isCollapsed = false;
    config.readOnly = true;
    config.isMailDisabled = true;

    $("#historyDialogueProjectQueue").dialog({
        title: config.title,
        modal: true,
        minWidth: 800,
        minHeight: 540,
        top: 10,
        open: function () {
            $(this).closest(".ui-dialog")
                .find(".ui-dialog-titlebar-close")
                .html("x");
        },
        close: function (event, ui) {
            $(this).dialog("close");
            $(this).empty();
        }
    });

    $("#historyDialogueProjectQueue").append('<div id="comments-container' + config.pageId + '"></div>');
    $('#comments-container').commentIsf(config);

}


function getWoAssignToMeTableActionIcons(data) {
    $.each(data, function (i, d) {
        localStorage.setItem(`${d.woPlanId}_${d.woId}`, JSON.stringify(d.listOfNode));
        if (d.srID && parseInt(d.srID) === 0) {
            d.srID = ""
        }
        
        const openFCInNewwindowUrl = `FlViewWithoutComment\?mode\=view&woID=${d.woId}`;
        if (d.signum === signumGlobal.toLowerCase() || d.signum === signumGlobal) {
            getActionIconsForMySignum(d, openFCInNewwindowUrl);
        } else if (d.signum && (d.signum !== signumGlobal.toLowerCase() || d.signum !== signumGlobal))
        {
            getActionIconsForOtherSignum(d, openFCInNewwindowUrl);
		}
        else {
            getActionIconsForNullSignum(d, openFCInNewwindowUrl);
        }

    });
}

function getActionIconsForNullSignum(d, openFCInNewwindowUrl) {
    const btnCallAssignToMeId = `btn_callAssignToMe_${d.woId}`;
    d.act = `<div style="display:flex;">
    <a href = "#" class="viewWorkFlow icon-view marginRight3px" title="Workflow shown here is as per your current proficiency for this Workorder " onclick = "callProficiencyID('${d.woId}','${d.signum}', '${openFCInNewwindowUrl}')" > ${getIcon('flowchart')}</a>
    <a href = "#" class="assignToMeIcon icon-notAssigned marginRight3px" id="${btnCallAssignToMeId}" data-woid='${d.woId}' data-workorderautosenseenabled='${d.workOrderAutoSenseEnabled}' data-flowchartdefid='${d.defID}'onclick = "callAssignToMe('${d.woId}',this)" title="Assign To Me"> ${getIcon('file')}</a>
    <a href="#" class="icon-view marginRight3px"  id="btn_comments_project_queue_${d.woId}" title="View Comments" onclick="callCommentsProjectQueue(this,${d.woId})">${getIcon('comments')}</a>
    <a href="#" class="viewWorkOrder icon-view" onclick="hideNEAddEdit();" data-status="${d.status}" data-workorder-id="${d.woId}" data-doid="${d.doid}" data-woplan-id="${d.woPlanId}" title="View Work Order">${getIcon('view')}</a>
  </div>`;

}
function getActionIconsForOtherSignum(d, openFCInNewwindowUrl) {
    const btnCallAssignToMeId = `btn_callAssignToMe_${d.woId}`;
    d.act = `<div style="display:flex;">
    <a href = "#" class="viewWorkFlow icon-view marginRight3px" title="Workflow shown here is as per your current proficiency for this Workorder " onclick = "callProficiencyID('${d.woId}','${d.signum}', '${openFCInNewwindowUrl}')" > ${getIcon('flowchart')}</a>
    <a href = "#" class="assignToMeIcon icon-assigned marginRight3px" id="${btnCallAssignToMeId}" data-woid='${d.woId}' data-workorderautosenseenabled='${d.workOrderAutoSenseEnabled}' data-flowchartdefid='${d.defID}'onclick = "callAssignToMe('${d.woId}',this)"  title="Assign To Me"> ${getIcon('file')}</a>
    <a href="#" class="icon-view marginRight3px"  id="btn_comments_project_queue_${d.woId}" title="View Comments" onclick="callCommentsProjectQueue(this,${d.woId})">${getIcon('comments')}</a>
    <a href="#" class="viewWorkOrder icon-view" onclick="hideNEAddEdit();" data-status="${d.status}" data-workorder-id="${d.woId}" data-doid="${d.doid}" data-woplan-id="${d.woPlanId}" title="View Work Order">${getIcon('view')}</a>
  </div>`;
}
function getActionIconsForMySignum(d, openFCInNewwindowUrl){
    const btnCallAssignToMeId = `btn_callAssignToMe_${d.woId}`;
    d.act = `<div style="display:flex;">
    <a href = "#" class="viewWorkFlow icon-view marginRight3px" title="Workflow shown here is as per your current proficiency for this Workorder " onclick = "callProficiencyID('${d.woId}','${d.signum}', '${openFCInNewwindowUrl}')" > ${getIcon('flowchart')}</a>
    <a class="assignToMeIcon icon-assignedToMe disabled marginRight3px" id="${btnCallAssignToMeId}" title="Assigned"> ${getIcon('file')}</a>
<a href="#" class="icon-view marginRight3px"  id="btn_comments_project_queue_${d.woId}" title="View Comments" onclick="callCommentsProjectQueue(this,${d.woId})">${getIcon('comments')}</a>
    <a href="#" class="viewWorkOrder icon-view" onclick="hideNEAddEdit();" data-status="${d.status}" data-workorder-id="${d.woId}" data-doid="${d.doid}" data-woplan-id="${d.woPlanId}" title="View Work Order">${getIcon('view')}</a>
  </div>`;
}

function callProficiencyID(woId,signum, openFCInNewwindowUrl) {

    var proficiencyID = getProficiencyId(woId, signum);
    const openFCInNewwindowUrl1 = openFCInNewwindowUrl + '&proficiencyId=' + proficiencyID;
    flowChartOpenInNewWindow(openFCInNewwindowUrl1);
}   

//check whether wo is Assigned to someone on click of button

function woAssignedOrNot(woId, obj) {
    $.isf.ajax({
        url: `${service_java_URL}woExecution/getAssignedToSignumByWOID?wOID=${woId}`,
        success: function (data) {
            if (!data.isValidationFailed) {
                if (data.responseData !== null) {
                    const signum = data.responseData; //To be verified once API is developed
                    workOrderAssignedAlert(signum, woId);
                }
                else {
                    executeAssignToMe(woId, obj);

                }
            } else {
                pwIsf.removeLayer();
                $(`#btn_callAssignToMe_${woId}`).remove();
                responseHandler(data);
            }
        },
        error: function () {
            console.log('An error occurred on WOAssignedToSignum');
        }
    });
}

//Alert when work order is already assigned to a signum
function workOrderAssignedAlert(signum, woId) {
    updateRowData(signum, woId);
    pwIsf.confirm({
        title: `Work Order: ${woId} is already assigned`,
        msg: `This WO is already assigned to ${signum}. Are you sure you want to assign it to yourself?`,
        buttons: {
            'Yes': {
                'action': function () {
                    //Write logic for 'Yes'
                    const woidArray = [];
                    const tempJsonObj = {};
                    woidArray.push({ 'wOID': woId, 'signumID': signumGlobal, 'senderSignum': signumGlobal });
                    tempJsonObj.workOrderModel = woidArray;
                    executeAssignToMe(woId,tempJsonObj);
                }
            },
            'No': {
                'action': function () {
                    //No action needed here
                    pwIsf.removeLayer();
                },
                'class': 'btn btn-danger'
            }
        }
    });

}

//Update Assign to me button color and the signum column
function updateRowData(signum, woId) {
    const buttonElement = $(`#btn_callAssignToMe_${woId}`);
    const notAssigned = 'icon-notAssigned';
    const assignedToMe = 'icon-assigned';

    //Get the column index
    const signumElement = getColumnElement(woId, 'WO Assigned To');

    //Change signum only if it's different
    if (signumElement.text().toLowerCase() !== signum.toLowerCase()) {
        signumElement.text(signum);
    }

    //Change button color only if it's different
    if (buttonElement.hasClass(notAssigned)) {
        buttonElement.removeClass(notAssigned);
        buttonElement.addClass(assignedToMe);
    }

}

function getColumnElement(woId, columnName) {
    const buttonElement = $(`#btn_callAssignToMe_${woId}`);
    const index = $('#table_WO_for_assignToMe thead th').filter(
        function() {
            return $(this).text() === columnName;
        }).index();
    return buttonElement.closest('tr').find(`td:nth-child(${index + 1})`);
}


function deactivateAssignedToMeAndUpdateSignum(woidsWithSignum, woId) {
    deactivateAssignToMe(woidsWithSignum);
    updateSignum(woId);
}

function updateSignum(woId) {
    const signumElement = getColumnElement(woId, 'WO Assigned To');
    //Change signum only if it's different
     signumElement.text(signumGlobal);
    
}

function getProjectQueueWorkOrders(startDate, endDate, priority, status, doStatus, projectIdList) {
    let isValueSearched = false;
    if (projectIdList) {
        const convertedStartEndDate = startAndEndDatesConversion(startDate, endDate);
        startDate = convertedStartEndDate[0];
        endDate = convertedStartEndDate[1];
        $("#startDateTable").val(startDate);
        $("#endDateTable").val(endDate);
        if ($.fn.dataTable.isDataTable('#table_WO_for_assignToMe')) {
            table.destroy();
            $("#table_WO_for_assignToMe").empty();
        }
        $('.scroll-div2').css('height', 'auto');

        let encodedUrlSearch = encodeURIComponent(`${signumGlobal}&startDate=${startDate}&endDate=${endDate}
&priority=${priority}&status=${status}&doStatus=${doStatus}&projectIdList=${projectIdList}&${isValueSearched}`);

        pwIsf.addLayer({ text: "Please wait ..." });

        let serviceUrl = `${service_java_URL}woExecution/getProjectQueueWorkOrders?signum=${signumGlobal}&startDate=${startDate}
&endDate=${endDate}&priority=${priority}&status=${status}&doStatus=${doStatus}&projectIdList=${projectIdList}&checkSearch=${isValueSearched}`;

        if (ApiProxy === true) {
            serviceUrl = `${service_java_URL}woExecution/getProjectQueueWorkOrders?signum=${encodedUrlSearch}`;
        }

        $("#table_WO_for_assignToMe").html('');
        $("#table_WO_for_assignToMe").append($(`<tfoot><tr>
            <th></th> <th></th> <th></th> <th></th> <th></th> <th></th> <th></th>
            <th></th> <th></th> <th></th> <th></th> <th></th> <th></th> <th></th>
            <th></th> <th></th> <th></th> <th></th>
            </tr></tfoot>`));
        table = $('#table_WO_for_assignToMe').DataTable({
            "processing": true,
            serverSide: true,
            searching: true,
            responsive: true,
            destroy: true,
            "pageLength": 20,
            colReorder: true,
            "drawCallback": function (settings) {
                $('body').tooltip({
                    container: 'body',
                    selector: '.aqua-tooltip',
                    placement: function (tip, element) { //$this is implicit
                        if ($(element).hasClass("leftToolTip")) {
                            return "left";
                        }
                        else {
                            return "top";
                        }
                    }
                });

                $('.leftToolTip').on('mouseout', function () {
                    $('.leftToolTip').attr('data-original-title', 'Copy to clipboard');
                })

            },
            "columnDefs": [
                { "width": "100px", "targets": 0 },
                {
                    "width": "130px",
                    render: function (data, type, row) {
                        if (data !== null && data !== "") {
                            return renderCopyEnabledData(data);
                        }
                        else {
                            return "";
                        }
                    },
                    "targets": [2, 3, 5, 9, 10, 12, 13, 17]
                },
                {
                    "width": "130px",
                    "className": "truncateTableFields",
                    "targets": [11, 14, 15, 16]
                },
                {
                    "width": "60px",
                    "targets": [1, 6, 7, 8]
                }
            ],
            "ajax": {
                "headers": commonHeadersforAllAjaxCall,
                "url": serviceUrl,
                "type": "POST",
                "dataSrc": function (data) {
                    pwIsf.removeLayer();
                        if (data && !data.isValidationFailed) {
                            if (data.responseData !== null && data.responseData.data.length !== 0 && data.responseData.recordsTotal) {
                                data.recordsTotal = data.responseData.recordsTotal;
                                data.recordsFiltered = data.responseData.recordsFiltered;
                                data.draw = data.responseData.draw;
                                getWoAssignToMeTableActionIcons(data.responseData.data);
                                return data.responseData.data;
                            }
                            else {
                                if (isPQTableIsNotRequiredToDisplay) {
                                    table.destroy();
                                   $("#table_WO_for_assignToMe").empty();
                                    $('#table_WO_for_assignToMe').html('<tr><td colspan="5" style="text-align:center">No data found.</td></tr>');
                                }
                                data.recordsTotal = 0;
                                data.recordsFiltered = 0;
                                data.data = [];
                                isPQTableIsNotRequiredToDisplay = false;

                                return data.data;
                            }
                        }
                        else {
                            pwIsf.removeLayer();
                            responseHandler(data);
                        }
                }

            },
            "bInfo": true,
            dom: 'Brtip',
            buttons: ['colvis', {
                text: 'Excel',
                action: function () {
                    downloadProjectQueueFile( priority, status, doStatus, projectIdList);
                },
                filename: function () {
                    var d = (new Date()).toLocaleDateString();
                    return 'ProjectQueueWO' + signumGlobal + "_" + d;
                }
            }],
            order: [[1, "desc"]],
            columns: [
                {
                    "title": "Action", orderable: false, searchable: false, data: null,
                    "render": function (data, type, row, meta) {
                        return data.act;
                    }
                },
                { "title": "Project ID", "data": "projectId" },
                { "title": "Project Name", "data": "projectName" },
                {
                    "title": "Market", "targets": 'no-sort', "orderable": true, "data": "listOfNode[0].market",
                    "defaultContent": "",
                    "render": function (d, type, row, meta) {
                        if (row.listOfNode[0].market == null) {
                            row.listOfNode[0].market = "";
                        }
                        return `<div class="nodes truncateTableFields">
                         <i data-toggle="tooltip " style="cursor:pointer" title="Network Element Details"class="fa fa-list"
                         onclick="myClosedWO_modalNodeNames(${row.woPlanId},${row.woId})"></i>
                         ${row.listOfNode[0].market}</div>`;
                    }
                },
                {
                    "title": "Network Element Name/ID", "data": "listOfNode[0].nodeNames",
                    "defaultContent": "",
                    "width": "130px",
                    "render": function (d, type, row, meta) {
                        if (row.listOfNode[0].nodeNames == null) {
                            row.listOfNode[0].nodeNames = "";
                        }

                        return `<div class="nodes truncateTableFields">
                         <i data-toggle="tooltip " style="cursor:pointer" title="Network Element Details" class="fa fa-list"
                         onclick="myClosedWO_modalNodeNames(${row.woPlanId},${row.woId})"></i>
                         ${row.listOfNode[0].nodeNames.substr(0, 40)}</div>`;
                    }
                },

                { "title": "Deliverable Name", "data": "deliverableName" },
                { "title": "DOID", "data": "doid" },
                {
                    "title": "SRID", "data": "srID",
                    "defaultContent": "",
                    "render": function (data, type, row, meta) {
                        let srID = "";
                        if (data != "0") {
                            srID = data;
                        }
                        return srID;
                    }
                },
                { "title": "WO ID", "data": "woId" },
                { "title": "WO Name", "data": "woName" },
                { "title": "WF Name", "data": "workFlowName" },
                { "title": "WO Assigned To", "data": "signum" },
                { "title": "Created By", "data": "employeeName" },
                { "title": "Technology", "data": "technology" },
                {
                    "title": "Start Date", "targets": 'no-sort', "orderable": true, "data": "startDate", searchable: false,
                    "defaultContent": "",
                    "render": function (data, type, row, meta) {
                        startDate = "";
                        if (data == "" || data == null) {
                            startDate = "";
                        }
                        else {
                            startDate = ConvertDateTime_tz(new Date(data));
                            var splittedDate = startDate.split(":");
                            var splittedSecoundUTC = splittedDate[2].split(" ");
                            startDate = splittedDate.slice(0, -1).join(':');
                             var dateUTC = splittedSecoundUTC.slice(1);
                            startDate = startDate + " " + dateUTC;
                         }
                        return startDate;
                    }
                },
                {
                    "title": "End Date", "targets": 'no-sort', "orderable": true, "data": "endDate", searchable: false,
                    "defaultContent": "",
                    "render": function (data, type, row, meta) {
                        endDate = "";
                        if (data == "" || data == null) {
                            endDate = "";
                        }
                        else {
                            endDate = ConvertDateTime_tz(new Date(data));
                            var splittedDate = endDate.split(":");
                            var splittedSecoundUTC = splittedDate[2].split(" ");
                            endDate = splittedDate.slice(0, -1).join(':');
                            var dateUTC = splittedSecoundUTC.slice(1);
                            endDate = endDate + " " + dateUTC;
                        }
                        return endDate;
                    }
                },
                {
                    "title": "Priority Last Modified", "data": "priorityModifiedOn",
                    "defaultContent": "",
                    "render": function (data, type, row, meta) {
                        endDate = "";
                        if (data === "" || data === null) {
                            endDate = "";
                        }
                        else {
                            endDate = ConvertDateTime_tz(data);
                        }
                        return endDate;
                    }
                },
                { "title": "SubActivity", "data": "subActivity" },
            ],
            "displayLength": 10,
            initComplete: function () {
                $('#table_WO_for_assignToMe tfoot th').each(function (i) {
                    var title = $('#table_WO_for_assignToMe thead th').eq($(this).index()).text();
                    if ((title !== "Action" && title !== "Start Date" && title !== "End Date")) {
                        if (title == "Market" || title == "Network Element Name/ID") {
                            $(this).html(`<input type="text" class="form-control searchArea" style="font-size:12px;"  placeholder="Search ${title}" data-index="${i}" />`);
                        }
                        else {
                            $(this).html(`<input type="text" class="form-control" style="font-size:12px;"  placeholder="Search ${title}" data-index="${i}" />`);
                        }
                    }
                    if (title === "Start Date" || title === "End Date") {
                        $(this).html('<label for="html">Search Not Available</label>');
                    }
                });
                var api = this.api();
                api.columns().every(function () {
                    var that = this;
                    $('input[type=text]', this.footer()).on('keyup change', function () {

                        // to set the chechSearch param true when searching
                        isValueSearched = checkSearch(this.value);
                        startDate = $("#startDateTable").val();
                        endDate = $("#endDateTable").val();

                        serviceUrl = `${service_java_URL}woExecution/getProjectQueueWorkOrders?signum=${signumGlobal}&startDate=${startDate}
&endDate=${endDate}&priority=${priority}&status=${status}&doStatus=${doStatus}
&projectIdList=${projectIdList}&checkSearch=${isValueSearched}`;

                        //new URL after search
                        table.ajax.url(serviceUrl);

                        if (that.search() !== this.value) {
                            that
                                .columns($(this).parent().index() + ':visible')
                                .search(this.value)
                                .draw();
                        }
                    });
                });
                isValueSearched = false;
            },
        });
        $('#table_WO_for_assignToMe tfoot').insertAfter($('#table_WO_for_assignToMe thead'));
        bindClickEventOnAction();
    }
}

//set value for param for search
function checkSearch(searchedString) {
    let isSearchedTextPresent = false;
    $.each($('.searchArea'), function (i, d) {
        if ($(d).val() !== "") {
            isSearchedTextPresent = true;
            return false;
        }
    })
    return isSearchedTextPresent;
}

// start & end date conversion
function startAndEndDatesConversion(startDate, endDate) {
    let startAndEndDate = [];
    if (startDate === "all" || endDate === "all") {
        const today = new Date();
        const tomorrow = new Date(today);
        tomorrow.setDate(tomorrow.getDate() + 1);
        startDate = formatted_date(today);
        endDate = formatted_date(tomorrow);
    }
    startAndEndDate = [startDate, endDate];
    return startAndEndDate;
}


//To copy text to clipboard
function copyToClipboard(thisObj) {
    let text = $(thisObj).data('copyvalue').data;
    navigator.clipboard.writeText(text);
    $(thisObj).attr('data-original-title', 'Copied');
    $(thisObj).trigger('mouseenter');
}


function renderCopyEnabledData(data) {

    let copyData = JSON.stringify({ data: data });
    data = data || '';
    return `<div class="truncateTableFields aqua-tooltip" data-toggle="tooltip" data-original-title=\'${data.toString()}\'>
                                            <a class="leftToolTip aqua-tooltip" data-toggle="tooltip" data-original-title="Copy to clipboard"
                                                onclick="copyToClipboard(this)" data-copyvalue=\'${copyData}\' style="cursor:pointer !important; margin-right: 5px;">
                                                    <i class="fa fa-copy"></i>
                                            </a>
                                        <span>${data}</span>
                                        </div>`;
}


function downloadProjectQueueFile(priority, status, doStatus, projectIdList) {

    let startDate = $("#startDateTable").val();
    let endDate = $("#endDateTable").val();
    var paramSearch = `signum=${signumGlobal}&startDate=${startDate}&endDate=${endDate}&priority=${priority}&status=${status}&doStatus=${doStatus}&projectIdList=${projectIdList}`;
    let encodedParam = encodeURIComponent(paramSearch);
    let serviceUrl = `${service_java_URL}woExecution/downloadProjectQueueWorkOrders?${encodedParam}`;

    let fileDownloadUrl;
    fileDownloadUrl = UiRootDir + "/data/GetFileFromApi?apiUrl=" + serviceUrl;
    window.location.href = fileDownloadUrl;
}

function convertdate(dt) {
    var datestring = dt.toString();
    const array = datestring.split(" ");
    var date = array[0];
    return date;
}
function getStartDateWhileLastThreeMonthSelect(today, timeZone) {
    let firstDay = new Date(today.getFullYear(), today.getMonth() - 2, 1);
    return moment(firstDay.toString()).tz(timeZone).format();
}
function getEndDateWhileLastThreeMonthSelect(today, timeZone) {
    let lastDay = new Date(today.getFullYear(), today.getMonth() + 1, 0);
    return moment(lastDay.toString()).tz(timeZone).format();
}

function callFor_WOForAssignToMe(durationText, isFromProjectQueueTab = false) {
    isPQTableIsNotRequiredToDisplay = true;
    $("a.dividerDate").removeClass("active");
    $(".linkDuration").removeClass("active");

    $('a.dividerDate').each(function (e) {
        if ($(this).text() === durationText) {
            $(this).addClass('active');
        }
    });
    $('.linkDuration').each(function (e) {
        if ($(this).text() === durationText) {
            $(this).addClass('active');
        }
    });
    const priority = $('a.dividerPriorityProjectQueue.active').text();
    const status = $('a.dividerStatusPQ.active').text();
    const getDates = getStartDateAndEndDateFromSelectedDuration(durationText);
    const doStatus = $('#do_status option:selected').val();
    if ((isFromProjectQueueTab && $('#selectprojId').val() !== null) || (!isFromProjectQueueTab)) {
        filterValidationAndPopulateData(getDates.sdate, getDates.edate, priority, status, doStatus);
    }
}


function onSearchButtonClick() {
    isPQTableIsNotRequiredToDisplay = true;
    const doStatus = $('#do_status option:selected').val();
    let startDate = $("#pStart_Date").val();
    let endDate = $("#pEnd_Date").val();
    const priority = $('a.dividerPriorityProjectQueue.active').text();
    const status = $('a.dividerStatusPQ.active').text();
    if (startDate == "" && endDate == "") {
        const durationText = $('a.dividerDate.active').text();
        const getDates = getStartDateAndEndDateFromSelectedDuration(durationText);
        startDate = getDates.sdate;
        endDate = getDates.edate;
    }
    filterValidationAndPopulateData(startDate, endDate, priority, status, doStatus);  
}

function durationCheck() {
    let start = $("#pStart_Date").datepicker("getDate");
    let end = $("#pEnd_Date").datepicker("getDate");
    let days = (end - start) / (1000 * 60 * 60 * 24);
    return Math.round(days);
}

function startDateAndEndDateSelectedFromPicker() {
    let startAndEndDateSelected = false;
    const start = $("#pStart_Date").datepicker("getDate");
    const end = $("#pEnd_Date").datepicker("getDate");
    if (start !== "" && end !== "") {
        startAndEndDateSelected = true;
    }
    return startAndEndDateSelected;
}