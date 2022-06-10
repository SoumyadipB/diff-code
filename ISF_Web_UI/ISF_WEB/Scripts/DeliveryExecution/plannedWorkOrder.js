const C_PLANNED_PROJECT_LIST_TAB = '#plannedProjectListTab';
const C_KEYUP_CHANGE = 'keyup change';
const C_BACKGROUND_COLOR = 'background-color';
const C_DOMAIN_SUBDOMAIN = 'domain-subdomain';
const C_SERVICE_AREA_SUBSA= 'service-area-sub-service-area';
const C_ACTIVITY_AUBACTIVITY = 'activity-subactivity';
const C_NODE_SITE_NAME = 'node-site-name';
const C_NEW_NODE_SITE = 'new-node-site';
const C_EDIT_WO_FLAG = 'edit-work-order-flag';
const C_EWO_MODAL ="#myModalMadEwo";
$(document).on("click", "#Transfer", function () {
    getSignum();
    $('#receiver').modal('show');
});

$(document).on("change", "#cbxSignumAR", function () {
   $(this).val();
});

var nodeNamesValidated = false;
function bindDataTable(groupCol){
    var colNum = groupCol;
    var tableId = C_PLANNED_PROJECT_LIST_TAB;
    var table = $(tableId).DataTable({
        searching:true,
        responsive:true,
        destroy: true,
        colReorder: true,
        dom: 'Bfrtip',
        buttons:[ 'colvis', 'excelHtml5'],
        "order": [[colNum, 'desc']],
        "displayLength": 25,
        initComplete: function () {
            $('#plannedProjectListTab tfoot th').each(function (i) {
                var title = $('#plannedProjectListTab thead th').eq($(this).index()).text();
                if ((title !== "Action")) {
                    $(this).html(`<input type="text" class="form-control" style="font-size:12px;"  placeholder="Search ${title}" data-index="${i}"/>`);
                }
            });
            var api = this.api();
            api.columns().every(function () {
                var that = this;
                $('input', this.footer()).on(C_KEYUP_CHANGE, function () {
                    if (that.search() !== this.value) {
                        that
                          .columns($(this).parent().index() + ':visible')
                          .search(this.value)
                          .draw();
                    }
                });
            });                   
        },
        "rowCallback": function (row, data, index) {
            if (data[5].toLowerCase() === "normal") {
                $('td', row).css(C_BACKGROUND_COLOR, '#87CEFA');
            }
            else if (data[5].toLowerCase() === "low") {
                $('td', row).css(C_BACKGROUND_COLOR, '#98FB98');
            }
            else if (data[5].toLowerCase() === "high") {
                $('td', row).css(C_BACKGROUND_COLOR, '#ffd995');
            }
            else if (data[5].toLowerCase() === "critical") {
                $('td', row).css(C_BACKGROUND_COLOR, 'RED');
            }
        }
    });
    $('#plannedProjectListTab tfoot').insertAfter($('#plannedProjectListTab thead'));
}

function getElementNamesObj() {

    var elementIdPrefix = "workOrderViewIdPrefix_";
    var controlNames = ['project_name', 'estimated_effort', 'wo_name', 'priority', 'start_time', 'start_date', 'wo_id', 'project_id', 'assigned_to',

        C_DOMAIN_SUBDOMAIN, 'technology', C_SERVICE_AREA_SUBSA, C_ACTIVITY_AUBACTIVITY,

        'market', 'vendor', 'sitetype-node-type', C_NODE_SITE_NAME, C_NEW_NODE_SITE, 'node_master_select', 'hidden_status', 'full_wo_name'
    ];

    var controlObj = {};

    for (var i = 0; i < controlNames.length; i++) {
        controlObj[controlNames[i]] = $(`#${elementIdPrefix}${controlNames[i]}`);
    }
    return controlObj;
}

function getValuesFromApiResponse(data) {

    var retElementsArrVal = [];
    var n = getPosition(data.responseData[0].wOName, "_", 1);
    retElementsArrVal['project_name'] = data.responseData[0].project;
    retElementsArrVal['project_id'] = data.responseData[0].projectID;
    retElementsArrVal['estimated_effort'] = data.responseData[0].effort;
    retElementsArrVal['wo_name'] = data.responseData[0].wOName.substring(n);
    retElementsArrVal['full_wo_name'] = data.responseData[0].wOName;
    retElementsArrVal['hidden_status'] = data.responseData[0].status;
    retElementsArrVal['priority'] = data.responseData[0].priority;
    retElementsArrVal['start_time'] = data.responseData[0].startTime;
    retElementsArrVal['start_date'] = data.responseData[0].startDate;
    retElementsArrVal['wo_id'] = data.responseData[0].wOID;
    retElementsArrVal['assigned_to'] = data.responseData[0].signumID;
    retElementsArrVal[C_DOMAIN_SUBDOMAIN] = data.responseData[0].domain;
    retElementsArrVal['technology'] = data.responseData[0].technology;
    retElementsArrVal[C_SERVICE_AREA_SUBSA] = data.responseData[0].serviceArea;
    retElementsArrVal[C_ACTIVITY_AUBACTIVITY] = data.responseData[0].activity;
    retElementsArrVal['vendor'] = data.responseData[0].vendor;
    retElementsArrVal['sitetype-node-type'] = data.responseData[0].listOfNode[0].nodeType;
    retElementsArrVal[C_NODE_SITE_NAME] = data.responseData[0].listOfNode;
    retElementsArrVal[C_NEW_NODE_SITE] = 'Missing';
    console.log('retElementsArrVal');
    console.log(retElementsArrVal);
    $('#woNameBefore').val(data.responseData[0].woNameAlias);
    $('#woNameAfter').text(data.responseData[0].wOName.substring(n));
    return retElementsArrVal;
}

function getDateFormat(date) {
    var d = new Date(date),
            month = ` ${(d.getMonth() + 1)}`,
            day = '' + d.getDate(),
            year = d.getFullYear();
    if (month.length < 2) {
        month = '0' + month;
    }    
    if (day.length < 2) {
        day = '0' + day;
    }
   return [day, month, year].join('-');
}

function setValuesToElements(elementsObj, valuesArr) {
    valuesArr['start_date'] = GetConvertedDate(new Date(valuesArr['start_date']).toDateString() + " " + valuesArr['start_time']);
    valuesArr['start_time'] = GetConvertedTime(new Date(valuesArr['start_date']).toDateString() + " " + valuesArr['start_time']);
    elementsObj['project_name'].val(valuesArr['project_name']);
    elementsObj['estimated_effort'].val(valuesArr['estimated_effort']);
    elementsObj['wo_name'].val(valuesArr['wo_name']);
    elementsObj['wo_id'].val(valuesArr['wo_id']);
    elementsObj['project_id'].val(valuesArr['project_id']);
    elementsObj['assigned_to'].val(valuesArr['assigned_to']);
    elementsObj['full_wo_name'].val(valuesArr['full_wo_name']);
    elementsObj['hidden_status'].val(valuesArr['hidden_status']);
    elementsObj['priority'].html('');
    elementsObj['priority'].append($('<option>', {
        value: valuesArr['priority'],
        text: valuesArr['priority']
    }));
    elementsObj['priority'].val(valuesArr['priority']);
    elementsObj['start_time'].val(valuesArr['start_time']);
    elementsObj['start_date'].val(valuesArr['start_date']);
    elementsObj[C_DOMAIN_SUBDOMAIN].val(valuesArr[C_DOMAIN_SUBDOMAIN]);
    elementsObj['technology'].val(valuesArr['technology']);
    elementsObj[C_SERVICE_AREA_SUBSA].val(valuesArr[C_SERVICE_AREA_SUBSA]);
    elementsObj[C_ACTIVITY_AUBACTIVITY].val(valuesArr[C_ACTIVITY_AUBACTIVITY]);
    elementsObj[C_NODE_SITE_NAME].html('');
    elementsObj['node_master_select'].html('');
    console.log(valuesArr[C_NODE_SITE_NAME]);
    let prePopNodeName='';
    for (var i = 0; i < valuesArr[C_NODE_SITE_NAME].length; i++)
    {
        prePopNodeName = prePopNodeName + ',' + valuesArr[C_NODE_SITE_NAME][i].nodeNames;
    }
    prePopNodeName = prePopNodeName.replace(/(^,)|(,$)/g, "");
    if (prePopNodeName == null || prePopNodeName === "null")
        prePopNodeName = "";
    elementsObj[C_NEW_NODE_SITE].val(prePopNodeName);
    $('#workOrderViewIdPrefix_savedNodes').val(prePopNodeName);
}


function getDODetails(doid, woid) {
    $.isf.ajax({
        url: `${service_java_URL}woManagement/getWorkOrdersByDoid?doid=${doid}`,
        success: function (data) {
            if (data.length > 0) {
                oTableDODetail = $('#table_do_details').DataTable({
                    "data": data,
                    "destroy": true,
                    "searching": true,
                    "order": [1],
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
                                    nodes = x[i].nodeNames + ',' + nodes;
                                    nodes = nodes.replace(/,\s*$/, '');
                                }
                                if (nodes == null || nodes === "null") {
                                    nodes = "";
                                }
                                localStorage.setItem(`${ data.woplanid }_${ data.woid }`, JSON.stringify(data.listOfNode));
                                woViewDataArray.push(
                                    {
                                        "NodeNames": nodes
                                    });

                                var nodesView = `<i data-toggle="tooltip" id="showList${data.woid}"  title="Network Element Details"class="fa fa-list"`;
                                nodesView = `${nodesView}style="cursor:pointer" onclick="modalNodeDE(${data.woplanid},${data.woid})"></i>`;
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
                        $('#table_do_details tfoot th').each(function (i) {
                            var title = $('#table_do_details thead th').eq($(this).index()).text();
                        });
                        var api = this.api();
                        api.columns().every(function () {
                            var that = this;
                            $('input').on(C_KEYUP_CHANGE, function () {
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
            }
            else {
                $('#table_do_details').html('<tr><td colspan="11" style="text-align:center">No data found.</td></tr>');

            }
        },
        error: function (xhr, status, statusText) {
            console.log('An error occurred on getDomain: ' + xhr.error);
        }
    });
}


function modalNodeWOPopup(wOPlanID, wOID, doid) {
    var nodes;
    $.isf.ajax({
        url: `${service_java_URL}woManagement/getWorkOrdersByDoid?doid=${doid}`,
        success: function (data) {
            $.each(data, function (i, d) {
                if (d.woid === wOID) {
                    nodes = JSON.stringify(d.listOfNode);
                }
            });
            var listOfNode = JSON.parse(nodes);
            if (listOfNode.length > 0) {
                const isExist = checkNodeExist(listOfNode);
    $.each(listOfNode, function (i, d) {
                if (d.nodeNames === "" && d.nodeType === "" && d.market === "") {
                    listOfNode = [];
                }
            });
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
                }
                }
             else {
                $('#table_wo_nodes_detail_edit').html('<tr><td colspan="11" style="text-align:center">No data found.</td></tr>');
			}
        }
    });
}

function bindClickEventOnAction() {  //bind work order view
    //bind work flow view
    $('.viewWorkFlowExec').on('click',function(){
        var woID = $(this).data('workorder-id');
        var status = $(this).data('status');
        var projid = $(this).data('prjid');
        var subActID = $(this).data('subactid');
        var versionNO = $(this).data('versionno');
        if (status == "ASSIGNED") {
            localStorage.setItem('expertise', 1);
        }
        else {
            localStorage.setItem('expertise', 0);
        }     
        flowChartOpenInNewWindow(`FlowChartExecution?mode=execute&version=${versionNO}&woID=${woID}&subActID=${subActID}&prjID=${projid}`);
    });
    //bind work order view
    $(document).on("click", ".viewWorkOrder",  function () {
        var workOrderId = $(this).data('workorder-id');
        var status = $(this).data('status');
        var doid = $(this).data('doid');
        var woplanid = $(this).data('woplan-id');
        $('#workOrderViewIdPrefix_doid').val(doid);
        getDODetails(doid, workOrderId);
        $('.nodeDetailCol').show();
        $('#WODetailPanel').addClass('collapse');
        $('#activityPanel').addClass('collapse');
        $('#WODetailPanel').removeClass('in');
        $('#activityPanel').removeClass('in');
        $('#nodePanel').addClass('collapse');
        $('#nodePanel').removeClass('in');
        $('#nodeEditPanel').hide();
        modalNodeWOPopup(woplanid, workOrderId, doid);
        $('.projectDetailsIcon').hide();
        $('#btn_update_project').hide();
        $('#wODetailHeader').find('p').remove().end();
        $('#wODetailHeader').append(`<p> WO Details - ${workOrderId}</p>`);
        $('#DODetailHeader').find('p').remove().end();
        $('#DODetailHeader').append(`<p> DO Details - ${doid}</p>`);
        var editFlag = true;
        var getEditFlag = $(this).data(C_EDIT_WO_FLAG);
        if (typeof $(this).data(C_EDIT_WO_FLAG) !== 'undefined' || $(this).data(C_EDIT_WO_FLAG) === false) {
            editFlag = false;
        }
        pwIsf.addLayer({ text: "Please wait ..." });
        $.isf.ajax({
            url: `${service_java_URL}woExecution/getCompleteWoDetails/${workOrderId}`,
            success: function (data) {
                pwIsf.removeLayer();
                $('#all_nodes_master').hide();
                if (data.isValidationFailed === false) {
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
                }
                else {
                    pwIsf.alert({ msg: data.formErrors[0], type: 'error' });
                }
                const selectedTabHref = $('#projectQueueTab').hasClass('active');
                if (selectedTabHref) {
                    $('.nodeDetailsIcon').hide();
                }
                $(C_EWO_MODAL).modal('show');

            },
            error: function (xhr, status, statusText) {
                pwIsf.removeLayer();
                $(C_EWO_MODAL).modal('hide');
                alert("Failed to Fetch Data");   
            }
        });

    });
}

function getValueUsingClass() {
    /* declare an checkbox array */
    var chkArray = [];
    /* look for all checkboes that have a class 'chk' attached to it and check if it was checked */
    $(".checkBoxClass:checked").each(function () {
        parseInt(chkArray.push($(this).val()));
    });
    /* we join the array separated by the comma */
    let selected = chkArray.join(',');
    /* check if there is selected checkboxes, by default the length is 1 as it contains one single comma */
    if (selected.length > 1) {
        return chkArray;
    } else {

        return 0;
    }
}

function massTransferWorkOrder(receiver, sd, ed) {
    if (receiver.length === "0" || receiver.length > 7) {
        alert("Please select employee signum");
    }
    else if (signumGlobal.trim() === receiver.toLowerCase().trim()) {
        alert('Work order cannot be transferred to yourself');
    }
    else {
        var selectedworkorders = getValueUsingClass();

        if (selectedworkorders !== 0) {
            if (receiver === 0) {
                receiver = null;
            }
            else if (sd === "") {
                sd = null;
            }
            var transferArr = { "woID": selectedworkorders, "senderID": signumGlobal, "receiverID": receiver, "startDate": sd };
            var request = $.isf.ajax({
                url: `${service_java_URL}woManagement/massUpdateWorkOrder`,
                method: "POST",
                returnAjaxObj: true,
                contentType: "application/json; charset=utf-8",
                data: JSON.stringify(transferArr),
                dataType: "html"
            });
            request.done(function (msg) {
                location.reload();
            });

            request.fail(function (jqXHR, textStatus) {
                alert(" Request failed: ");
            });
        }
        else {
            alert("Please select at least one work order");
        }
    }
}

function transferWorkOrder(receiver) {
    if (receiver.length === 0 || receiver == null || receiver.length > 7) {
        alert("Please select employee signum");
    }
    else if(signumGlobal.trim() === receiver.toLowerCase().trim()) {
        alert('Work order cannot be transferred to yourself');
    }
    else {
            var selectedworkorders = getValueUsingClass();
        
             if (selectedworkorders !== 0) {
                var transferArr = { "woID": selectedworkorders, "senderID": signumGlobal, "receiverID": receiver, "logedInSignum": signumGlobal };
                var request = $.isf.ajax({
                    url: `${service_java_URL}woManagement/transferWorkOrder`,
                    method: "POST",
                    returnAjaxObj: true,
                    contentType: C_CONTENT_TYPE_APPLICATION_JSON,
                    data: JSON.stringify(transferArr),
                    dataType: "html"
                });
                request.done(function (msg) {
                    var convertedJson = JSON.parse(msg);
                    if (convertedJson.isValidationFailed) {
                        pwIsf.alert({ msg: convertedJson.formMessages[0], type: 'error' });
                        location.reload();
                    }
                    else {
                        pwIsf.alert({ msg: convertedJson.formErrors[0], type: 'error' });
                    }
                });
                request.fail(function (jqXHR, textStatus) {
                    alert(" Request failed: ");
                });
             }
             else {
                alert("Please select at least one work order");
             }
         }
}

function searchByDate(signum, startDate, endDate) {
    var table = $(C_PLANNED_PROJECT_LIST_TAB).DataTable();
    if ($.fn.dataTable.isDataTable(C_PLANNED_PROJECT_LIST_TAB)) {
        table.destroy();
        $("#plannedProjectList_tbody").html('');
    }
    getAllPlannedWorkOrderDetails(signum, startDate, endDate);
}

function getAllPlannedWorkOrderDetails(signum, startDate, endDate) {
    if(startDate==="all" || endDate==="all")
    {
        var today = new Date();
        var tomorrow = new Date(today);
        tomorrow.setDate(tomorrow.getDate() + 1);
        startDate = formatted_date(today);
        endDate = formatted_date(tomorrow);
    }
    if ($.fn.dataTable.isDataTable(C_PLANNED_PROJECT_LIST_TAB)) {
        oTable.destroy();
        $(C_PLANNED_PROJECT_LIST_TAB).empty();
    } 
    pwIsf.addLayer({ text: "Please wait ..." });
    var urlSearch = `${ service_java_URL }woExecution/searchPlannedWorkOrders?signum=${signumGlobal}&startDate=${startDate}&endDate=${endDate}`;
    var serviceUrl = `${service_java_URL}woExecution/searchPlannedWorkOrders?signum=${signumGlobal}&startDate=${startDate}&endDate=${endDate}`;
    if (ApiProxy == true) {
        serviceUrl = service_java_URL + "woExecution/searchPlannedWorkOrders?signum='" + encodeURIComponent(signumGlobal + "'&startDate=" + startDate + "&endDate=" + endDate);
        urlSearch = service_java_URL + "woExecution/searchPlannedWorkOrders?signum='" + encodeURIComponent(signumGlobal + "'&startDate=" + startDate + "&endDate=" + endDate);
    }
    $("#plannedProjectListTab").html('');
    $.isf.ajax({
        url: serviceUrl,
        success: function (data) {
            pwIsf.removeLayer();
            if (data.length > 0) {
                ConvertTimeZones(data, searchPlannedWorkOrders_tzColumns);
                $.each(data, function (i, d) {
                    localStorage.setItem(d.woPlanId + "_" + d.woId, JSON.stringify(d.listOfNode));
                    d.act = '<a href="#" class="viewWorkOrder" data-status="' + d.status + '" data-workorder-id="' + d.woId + '" data-doid="' + d.doid + '" data-woplan-id="' + d.woPlanId + '"><span class="fa fa-edit" data-toggle="tooltip" title="View Work Order" style="color:purple"></span></a>&nbsp;&nbsp;';
                    d.act += '<a href="#" class="viewWorkFlow" onclick="flowChartOpenInNewWindow(' + ' \'FlowChart\?mode\=view&woID=' + d.woId + '&proficiencyId=' + d.proficiencyID + '\' ' + ')"><span class="fa fa-eye" data-toggle="tooltip" title="View work Flow" style="color:green"></span></a>&nbsp;&nbsp;';
                    d.act += '<a class="popClick" id="popOver' + d.woId + '" href= "#"  data-woid="' + d.woId + '" data-html="true" data-trigger="focus" title="Work Order Info" ><span class="fa fa-info-circle" style="color: blue;cursor:pointer" ></span></a>';
                });

                $("#plannedProjectListTab").append($('<tfoot><tr><th></th><th></th><th></th><th></th><th></th><th></th><th></th><th></th><th></th><th></th><th></th><th></th></tr></tfoot>'));
                oTable = $("#plannedProjectListTab").DataTable({
                    searching: true,
                    responsive: true,
                    destroy: true,
                    data: data,
                    colReorder: true,
                    dom: 'Bfrtip',
                    buttons: ['colvis', 'excelHtml5'],
                    columns: [
                        { "title": "Action", "data": "act" },
                        { "title": "Project ID", "data": "projectId" },
                        { "title": "WO ID", "data": "woId" },
                        { "title": "WO Name", "data": "woName" },
                        { "title": "Priority", "data": "priority" },
                        { "title": "Status", "data": "status" },
                        { "title": "LOE", "data": "loe" },
                        { "title": "Start Date", "data": "startDate" },
                        { "title": "End Date", "data": "endDate" },
                        { "title": "Activity", "data": "activity" },
                        { "title": "SubActivity", "data": "subActivity" },
                        {
                            "title": "Nodes", "targets": 'no-sort', "orderable": false, "data": null,
                            "defaultContent": "",
                            "render": function (d, type, row, meta) {
                                if (d.listOfNode[0].nodeNames == null)
                                    d.listOfNode[0].nodeNames = "";
                                if (d.listOfNode[0].nodeNames.length < 40) {
                                    return '<div class="nodes" >' + d.listOfNode[0].nodeNames.substr(0, 40) + '<i data-toggle="tooltip" style="cursor:pointer" title="Network Element Details"class="fa fa-list"  onclick="modalNodeDE(' + d.woPlanId + ',' + d.woId + ')"></i></div>';
                                }
                                else
                                    return '<div class="nodes">' + d.listOfNode[0].nodeNames.substr(0, 40) + '<a id="showPara' + d.woId + '" style="cursor:pointer" onclick="showParaDE(' + d.woId + ')">.....</a><p id="paraShowhide' + d.woId + '" style="display:none">' + d.listOfNode[0].nodeNames.substr(11, d.listOfNode[0].nodeNames.length) + '</p><i data-toggle="tooltip" id="collapseParagraph' + d.woId + '"  title="collapse"class="fa fa-chevron-up" style="display:none;cursor:pointer" onclick="collapseParaDE(' + d.woId + ')"></i>&nbsp;&nbsp;<i data-toggle="tooltip" id="showList' + d.woId + '"  title="Network Element Details"class="fa fa-list" style="display:none;cursor:pointer" onclick="modalNodeDE(' + d.woPlanId + ',' + d.woId + ')"></i></div>';
                            }
                        },
                    ],
                    "displayLength": 25,
                    initComplete: function () {

                        $('#plannedProjectListTab tfoot th').each(function (i) {
                            var title = $('#plannedProjectListTab thead th').eq($(this).index()).text();
                            if ((title != "Action"))
                                $(this).html('<input type="text" class="form-control" style="font-size:12px;"  placeholder="Search ' + title + '" data-index="' + i + '" />');
                        });
                        var api = this.api();
                        api.columns().every(function () {
                            var that = this;
                            $('input', this.footer()).on(C_KEYUP_CHANGE, function () {
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
                $('#plannedProjectListTab tfoot').insertAfter($('#plannedProjectListTab thead'));
                bindClickEventOnAction();
            }
            else {
                $(C_PLANNED_PROJECT_LIST_TAB).html('<tr><td colspan="11" style="text-align:center">No data found.</td></tr>');
            }
        },
        error: function (xhr, status, statusText) {
            pwIsf.removeLayer();
            $(C_PLANNED_PROJECT_LIST_TAB).html('<tr><td colspan="11" style="text-align:center">No data found.</td></tr>');
        }
    });
}

function showParaDE(item) {
    // alert(item);
    $('#paraShowhide' + item).show();
    $('#showPara' + item).hide();
    $('#collapseParagraph' + item).show();
    $('#showList' + item).show();
}

function collapseParaDE(item) {
    $('#paraShowhide' + item).hide();
    $('#showPara' + item).show();
    $('#collapseParagraph' + item).hide();
    $('#showList' + item).hide();
}

function modalNodeDE(wOPlanID, wOID) {
    let nodeList = localStorage.getItem(wOPlanID + "_" + wOID);
    if (nodeList) {
        const listOfNode = JSON.parse(nodeList);
        $.each(listOfNode, function (i, d) {
        if (d.nodeNames === "" && d.nodeType === "" && d.market === "") {
            listOfNode = [];
        }
    });
        if (listOfNode.length > 0) {
            let isExist = checkNodeExist(listOfNode);
            if (isExist === true) {
                table = $('#table_wo_nodes_de').DataTable({
                    info: true,
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
            $('#table_wo_nodes_de').html('<tr><td colspan="11" style="text-align:center">No data found.</td></tr>');
        }
        }
        else {
            $('#table_wo_nodes_de').html('<tr><td colspan="11" style="text-align:center">No data found.</td></tr>');
        }
    }
    $('#modal_wo_nodes_detail').modal('show');
    $('#table_wo_nodes_de td').css('overflow', 'auto');
    $('#table_wo_nodes_de td').css('max-width', '0px');
}

function showNodesModel(nodeNames, nodeType, wOId) {
    if (wOId == null || wOId.length == 0) {
        alert('Something went wrong!!');
    }
    else if ((nodeNames == 'null' || nodeNames.length == 0) && (nodeType == 'null' || nodeType.length == 0)) {
        alert("No Nodes available for Work order -" + wOId);
    }
    else {
        $('#scopeTitleModal').find('p').remove().end();
        $('#tableNodes').find('tr').remove().end();
        $('#scopeTitleModal').append('<p class="text-center">Nodes Of Word Order- ' + wOId + '</p>');
        $('#tableNodes').append('<tr><td>' + nodeType + '</td><td>' + nodeNames + '</td></tr>');
        $('#modalWONodes').modal('show');
    }
}

$(document).on("click", ".hold-WO", function () {
    var woId = $(this).data('id');
    $(".modal-body #workOrderIDHoldWO").val(woId);
    $(".modal-body #commentHoldWO").val("");
    $('#modalHoldWO').modal('show');
});

$(document).on("click", ".deferred-WO", function () {
    var woId = $(this).data('woid');
    $(".modal-body #workOrderIDDefWO").val(woId);
    $(".modal-body #commentDefferedWO").val("");
    $('#modalDefferedWO').modal('show');
});

$(document).on("click", ".popClick", function ($e){
    $e.preventDefault();
    var woId = $(this).data('woid');
    var idPop = "#popOver" + woId;
    var workOrderInfo = "";
    $.isf.ajax({
        url: service_java_URL + "woDeliveryAcceptance/getStatusReasons/" + woId,
        async: false,
        success: function (data) {
            var userDeliveryStatus = data.userDeliveryStatus;
            if (userDeliveryStatus == null || userDeliveryStatus == "null")
                userDeliveryStatus = "";

            var userReason = data.userReason;
            if (userReason == null || userReason == "null")
                userReason = "";

            var userComments = data.userComments;
            if (userComments == null || userComments == "null")
                userComments = "";

            var deliveryStatus = data.deliveryStatus;
            if (deliveryStatus == null || deliveryStatus == "null")
                deliveryStatus = "";

            var deliveryRatings = data.deliveryRatings;
            if (deliveryRatings == null || deliveryRatings == "null")
                deliveryRatings = "";

            var deliveryComment = data.deliveryComment;
            if (deliveryComment == null || deliveryComment == "null")
                deliveryComment = "";

            var deliveryReason = data.deliveryReason
            if (deliveryReason == null || deliveryReason == "null")
                deliveryReason = "";

            if (data.isParent) {
                workOrderInfo += '<div class=\'container\' style=\'font-size:14px;\'><p><strong>WO Status - REOPENED</strong></p><p>Parent WO Details-</p><table class=\'table table-bordered\'><tbody>';
            }
            else {
                workOrderInfo += '<div class=\'container\' style=\'font-size:14px;\'><table class=\'table table-bordered\'><tbody>';   
            }
            workOrderInfo += '<tr><td><strong>WO Status</strong></td><td>' + data.woStatus + '</td></tr>';
            workOrderInfo += '<tr><td><strong>User Delivery Status</strong></td><td>' + userDeliveryStatus + '</td></tr>';

                workOrderInfo += '<tr><td><strong>User Reason</strong></td><td style=\'color:red\'>' + userReason + '</td></tr>';
                workOrderInfo += '<tr><td><strong>User Comment</strong></td><td style=\'color:red\'>' + userComments + '</td></tr>';

                workOrderInfo += '<tr><td><strong>Delivery Status</strong></td><td>' + deliveryStatus + '</td></tr>';
            if (data.deliveryStatus == 'Accepted') {
                workOrderInfo += '<tr><td><strong>Delivery Ratings</strong></td><td>' + deliveryRatings + '</td></tr>';
                workOrderInfo += '<tr><td><strong>Delivery Comment</strong></td><td>' + deliveryComment + '</td></tr>';
            }
            else {
                workOrderInfo += '<tr><td><strong>Delivery Reason</strong></td><td style=\'color:red\'>' + deliveryReason + '</td></tr>';
                workOrderInfo += '<tr><td><strong>Delivery Comment</strong></td><td style=\'color:red\'>' + deliveryComment + '</td></tr>';
            }
            workOrderInfo += '</tbody></table></div>';

        },
        error: function (xhr, status, statusText) {
            var err = JSON.parse(xhr.responseText);
            workOrderInfo = err.errorMessage;
        }

    });
    $(idPop).popover({
        content: workOrderInfo,
        container: 'body'
    });
   $(idPop).popover('toggle');
});

function reset() {
    $('#start_date').val('');
    $('#end_date').val('');
}

//------------------------------------------------------------------------- node edit functionality ----------------------------------------------------------

function addMoreNodes()
{ //abc
    nodeNamesValidated = false;
    $("#workOrderViewIdPrefix_new-node-site").prop('readonly', false);
    $('#addNodesValid').hide();
    $('#changeIcon').hide();
}

function closeModalWorkOrderViews()
{
    $('#nodeNames').hide();
    $('#viewnodeNames').show();
    $('#viewVendorArea').hide();
    $('#viewVendor').show();
    $('#viewMarketArea').hide();
    $('#viewMarket').show();
    $('#EditNodeType2').hide();
    $('#viewNodeType2').show();
    $('#btn_update_project').hide();
    $('.projectDetailsIcon').hide();
    $('#EditNodeType').hide();
    $('#viewNodeType').show();    
    $('#validStatus').css('display', 'none');
    $("#workOrderViewIdPrefix_new-node-site").val('');
    $("#workOrderViewIdPrefix_new-node-site").prop('readonly', true);
    $(C_EWO_MODAL).modal('hide');
}

/*--------------Date in YYYY-MM-DD Format-----------------------------*/

var formatted_date = function (date) {
    var m = ("0" + (date.getMonth() + 1)).slice(-2); // in javascript month start from 0.
    var d = ("0" + date.getDate()).slice(-2); // add leading zero 
    var y = date.getFullYear();
    return y + '-' + m + '-' + d;
}

/*---------------Get WO Data According to Duration Clicked----------------------------*/

function getPastDuration(text,el) {
    durationText = text;
    //if(text !="Today")
        $("a.divider").removeClass("active");
    $(el).addClass("active");
    switch (durationText) {
        case "Today":
            var today = new Date();
            var tomorrow = new Date(today);
            tomorrow.setDate(tomorrow.getDate() + 1);
            startDate = formatted_date(today);
            endDate = formatted_date(tomorrow);
            break;
            
        case "This Week":
            var today = new Date();
            var day = today.getDay();
            var diff = today.getDate() - day + (day == 0 ? -6 : 1); // 0 for sunday
            var week_start_tstmp = today.setDate(diff);
            var week_start = new Date(week_start_tstmp);
            startDate = formatted_date(week_start);
            var week_end = new Date(week_start_tstmp);  // first day of week 
            week_end = new Date(week_end.setDate(week_end.getDate() + 6));
            endDate = formatted_date(week_end);
            break;
        case "Last Week":
            var today = new Date();
            var day = today.getDay();
            var diff = today.getDate() - day + (day == 0 ? -13 : -6);
            var week_start_tstmp = today.setDate(diff);
            var week_start = new Date(week_start_tstmp);
            startDate = formatted_date(week_start);
            var week_end = new Date(week_start_tstmp);  // first day of week 
            week_end = new Date(week_end.setDate(week_end.getDate() + 6));
            endDate = formatted_date(week_end);
            break;
        case "This Month":
            var today = new Date();
            y = today.getFullYear(),m = today.getMonth();
            var firstDay = new Date(y, m, 1);
            var lastDay = new Date(y, m + 1, 0);
            startDate = formatted_date(firstDay);
            endDate = formatted_date(lastDay);
            break;
        case "Last Month":
            var today = new Date();
             y = today.getFullYear(),m = today.getMonth();
            var firstDay = new Date(y, m - 1, 1);
            var lastDay = new Date(y, m , 0);
            startDate = formatted_date(firstDay);
            endDate = formatted_date(lastDay);
            break;
        case "Last 3 Months":
            var today = new Date();
            y = today.getFullYear(),m = today.getMonth();
            var firstDay = new Date(y, m - 2, 1);
            var lastDay = new Date(y, m + 1, 0);
            startDate = formatted_date(firstDay);
            endDate = formatted_date(lastDay);
            break;
    }
    getAllPlannedWorkOrderDetails(signumGlobal, startDate, endDate);
}

function getPosition(string, subString, index) {
    return string.split(subString, index).join(subString).length;
}
function tooltipValWO() {
    var x = $("#woNameBefore").val() + $("#woNameAfter").text();
    $("#woNameBefore").attr("title", x);
    $("#woNameBefore").attr("title", x);
}