var global_myClosedWOTable;
function myClosedWO_modalNodeDE(wOPlanID, wOID) {
    if ($.fn.dataTable.isDataTable('#table_wo_nodes_de')) {
            table.destroy();
        $("#table_wo_nodes_de").html('');
    } 
    const nodeList = localStorage.getItem(`${wOPlanID}_${wOID}`);
    
    if (nodeList && nodeList !== "undefined") {
        let listOfNode = JSON.parse(nodeList);

        $.each(listOfNode, function (i, d) {
            if (d.nodeNames === "" && d.nodeType === "" && d.market === "") {
                listOfNode = [];
            }
        });

        if (listOfNode.length > 0) {
            const isExist = checkNodeExist(listOfNode);
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
            }
            else {
                $('#table_wo_nodes_de').html(getNoDataRow());

            }
        } else {
            $('#table_wo_nodes_de').html(getNoDataRow());
		}
    }
    $('#modal_wo_nodes_de').modal('show');
    $('#table_wo_nodes_de td').css('overflow', 'auto');
    $('#table_wo_nodes_de td').css('max-width', '0px');
}


function myClosedWO_modalNodeNames(wOPlanID, wOID) {
    var listOfNode;
    if ($.fn.dataTable.isDataTable('#table_wo_nodes_de')) {
        table.destroy();
        $("#table_wo_nodes_de").html('');
    }
    $.isf.ajax({
        url: service_java_URL + "woExecution/getListOfNode?wOID=" + wOID,
        success: function (data) {
            if (data.responseData) {
                if (data.isValidationFailed === false) {
                    listOfNode = data.responseData;
                    $.each(listOfNode, function (i, d) {
                        if (d.nodeNames === "" && d.nodeType === "" && d.market === "") {
                            listOfNode = [];
                        }
                    });
                }
                else {
                    listOfNode = {};
                }
                    listOfNode = JSON.parse(JSON.stringify(data.responseData));
                    if (listOfNode.length > 0) {
                       let isExist= checkNodeExist(listOfNode);
                        if (isExist===true) {
                            table = $('#table_wo_nodes_de').DataTable({
                                "info": true,
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
                        else {
                            $('#table_wo_nodes_de').html(getNoDataRow());
                        }
                    }
                    else {
                        $('#table_wo_nodes_de').html(getNoDataRow());

                    }
                }
        },
        error: function (xhr, status, statusText) {

            pwIsf.alert("Error:" + status);
        }
    });
    $('#modal_wo_nodes_de').modal('show');
    $('#table_wo_nodes_de td').css('overflow', 'auto');
    $('#table_wo_nodes_de td').css('max-width', '0px');
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
    return '';
}

function getDODetails(doid, woid) {
    $.isf.ajax({
        url: service_java_URL + "woManagement/getWorkOrdersByDoid?doid=" + doid,
        success: function (data) {
            if (data.length > 0) {
                let result = data.find(x => x.doid == doid);
                localStorage.setItem(result.woplanid + "_" + result.woid, JSON.stringify(result.listOfNode));
                modalNodeWOPopup(result.woplanid, result.woid);
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
                                woViewDataArray.push(
                                    {

                                        "NodeNames": nodes

                                    });

                                var nodesView = `<i data-toggle="tooltip" id="showList${data.woid}"  title="Network Element Details" class="fa fa-list" style="cursor:pointer"`;
                                nodesView = `${nodesView}onclick="myClosedWO_modalNodeDE(${data.woplanid},${data.woid})"></i>`;
                                return nodesView;
                            }
                        },
                        {
                            "title": "WFID_WFName_WFVErsion",
                            "data": null,
                            "render": function (data, type, row) {
                                return data.wfInfo;
                            }

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
            }
            else {
                $('#table_do_details').html(getNoDataRow());

        }
        },
        error: function (xhr, status, statusText) {

            $('#table_do_details').html(getNoDataRow());
            console.log('An error occurred on getDomain: ' + xhr.error);
        }
    });
}


function modalNodeWOPopup(wOPlanID, wOID) {
    var listOfNode;
    var editTable;
    const getWoplanWoId = localStorage.getItem(wOPlanID + "_" + wOID);
    if (getWoplanWoId != undefined && getWoplanWoId != null && getWoplanWoId != 'undefined' && getWoplanWoId != 'null') {
        listOfNode = JSON.parse(localStorage.getItem(wOPlanID + "_" + wOID));
        $.each(listOfNode, function (i, d) {
            if (d.nodeNames === "" && d.nodeType === "" && d.market === "") {
                listOfNode = [];
            }
        });
    } else {
        listOfNode = [];
    }
    if ($.fn.dataTable.isDataTable('#table_wo_nodes_detail_edit')) {
        $('#table_wo_nodes_detail_edit').DataTable().destroy();
        $("#table_wo_nodes_detail_edit").html('');
    }
    if (getWoplanWoId) {
        if (listOfNode.length > 0) {
            const isExist = checkNodeExist(listOfNode);
            if (isExist === true) {
                editTable = $('#table_wo_nodes_detail_edit').DataTable({
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
                        }
                    ]
                });
            }
            else {
                $('#table_wo_nodes_detail_edit').html(getNoDataRow());

            }
        } else {
            $('#table_wo_nodes_detail_edit').html(getNoDataRow());
		}
    }
}

function addMoreNodes() {
    nodeNamesValidated = false;
    $("#workOrderViewIdPrefix_new-node-site").prop('readonly', false);
    $('#addNodesValid').hide();
    $('#changeIcon').hide();
}


function bindClickEventOnAction_myClosedWO(doId, woStatus, woId, woPlanId) {  //bind work order view

    var workOrderId = woId;
    var status = woStatus;
    var doid = doId;
    var woplanid = woPlanId;
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


                    if ((status == "CLOSED") || (status == "DEFERRED") || (status == "REJECTED")) {
                        $('#btn_update_nodes').attr("disabled", true);
                        $("#edit_node_details").hide();

                    } else {
                        $('#btn_update_nodes').attr("disabled", false);
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
                $("#myModalMadEwo").modal('show');

            },
            error: function (xhr, status, statusText) {
                pwIsf.removeLayer();
                $("#myModalMadEwo").modal('hide');
                alert("Failed to Fetch Data");

            }
        });
}

function callFor_closedWO(durationText) {

    $(".closedWO a.divider").removeClass("active");
    //$(el).addClass("active");

    $('.closedWO a.divider').each(function (e) { if ($(this).text() == durationText) { $(this).addClass('active'); } });

    let getDates = getStartDateAndEndDateFromSelectedDuration(durationText);

    getAll_ClosedWO(getDates.sdate, getDates.edate);
}

function cleanNodeFields(doId, woStatus, woId, woPlanId) {
    hideNEAddEdit();
    $("#workOrderViewIdPrefix_sitetype-node-type").find('option').not(':first').remove();
    $('#workOrderViewIdPrefix_node-site-name_multi')
        .find('option')
        .remove();
    $("#workOrderViewIdPrefix_node-site-name_multi").empty();
    $('#btn_update_nodes').attr("disabled", "disabled");
    $('#btn_update_project').attr("disabled", "disabled");
    $('.projectDetailsIcon').hide();
    $('.nodeDetailsIcon').show();
    $('#EditNodeType').hide();
    $('#viewNodeType').show();
    $('#EditNodeType2').hide();
    $('#viewNodeType2').show();
    $('#viewMarketArea').hide();
    $('#viewMarket').show();
    $('#viewVendorArea').hide();
    $('#viewVendor').show();
    $("#workOrderViewIdPrefix_new-node-site").attr("readonly", "readonly");
    bindClickEventOnAction_myClosedWO(doId, woStatus, woId, woPlanId);

}


function getAll_ClosedWO(startDate, endDate) {
    if (startDate == "all" || endDate == "all") {
        var today = new Date();
        var tomorrow = new Date(today);
        tomorrow.setDate(tomorrow.getDate() + 1);
        startDate = formatted_date(today);
        endDate = formatted_date(tomorrow);
    }

    if ($.fn.dataTable.isDataTable('#dataTable_for_closedWO')) {
        myClosedWOTable.destroy();
        $('#dataTable_for_closedWO').empty();
    }
    var serviceUrl = service_java_URL + "woExecution/searchPlannedWorkOrders?signum='" + signumGlobal + "'&startDate=" + startDate + "&endDate=" + endDate;
    $('#dataTable_for_closedWO').html("");
    $("#dataTable_for_closedWO").append($('<tfoot><tr><th></th><th>Project ID</th><th>Nodes</th><th>Deliverable Name</th><th>Deliverable Unit</th><th>DOID</th><th>WO ID</th><th>WO Name</th><th>Priority</th><th>Status</th><th>Start Date</th><th>End Date</th><th>Activity</th><th>SubActivity</th></tr></tfoot>'));
    myClosedWOTable = $('#dataTable_for_closedWO').DataTable({
        "processing": true,
        serverSide: true,
        searching: true,
        responsive: true,
        destroy: true,
        "pageLength": 10,
        colReorder: true,
        "ajax": {
            "headers": commonHeadersforAllAjaxCall,
            "url": serviceUrl,
            "type": "POST",
            "dataSrc": function (data) {
                    data.recordsTotal = data.recordsTotal;
                    data.recordsFiltered = data.recordsFiltered;
                    data.draw = data.draw;
                    return data.data;   
            },
            "complete": function (d) {
                $('#div_table').show();
            },
            error: function (xhr, status, statusText) {
                $('#div_table').hide();
            }
        },
        order: [[2, "desc"]],
        dom: 'Bfrtip',
        buttons: [
            //for column visibility
            'colvis', {
                text: 'Excel',
                action: function (e, dt, node, config) {
                    downloadWorkOrderHistoryFile(startDate, endDate);

                },
                filename: function () {
                    var d = (new Date()).toLocaleDateString();
                    return 'WorkOrderHistory_' + signumGlobal + "_" + d;
                }
            }],
        "columnDefs": [
            { "searchable": false, "targets": 3 },
                { "searchable": false, "targets": 4 }
        ],
        "columns": [
            {
                "title": "Action", "orderable": false, "searchable": false, "targets": 'no-sort',
                "render": function (response, type, row, count) {
                    ConvertTimeZones(row, searchPlannedWorkOrders_tzColumns);
                    let experienced = 0;
                    localStorage.setItem(row.woPlanId + "_" + row.woId, JSON.stringify(row.listOfNode));

                    if (row.type.toUpperCase() == 'PROJECTDEFINED_EXPERT') {
                        experienced = 1;
                    }
                    else {
                        experienced = 0;
                    }
                    return '<div style="display:flex;"><a href="#" class="viewWorkFlow icon-view" title=" Workflow shown here is as per your current proficiency for this Workorder " onclick="flowChartOpenInNewWindow(' + ' \'FlowChart\?mode\=view&woID=' + row.woId + '&proficiencyId=' + row.proficiencyID + '&experiencedMode=' + experienced + '\'' + ')">' + getIcon('flowchart') + '</a>&nbsp;&nbsp;<a id="woHistory_' + row.woId + '" href="#" onclick="cleanNodeFields(' + ' \'' + row.doid + '\'' + ',\'' + row.status + '\'' + ',\'' + row.woId + '\'' + ',\'' + row.woPlanId+  '\'' +')" class="viewWorkOrderWoHistory icon-view" data-doid="' + row.doid + '" data-woplan-id="' + row.woPlanId + '" data-status="' + row.status + '" data-workorder-id="' + row.woId + '" title="View Work Order">' + getIcon('view') + '</a>&nbsp;&nbsp;<a title="Edit/Update Output Files" data-toggle="modal" data-target="#OpenOutputFilesModal" href="#" class="icon-view" onclick="getOutputFilesFlowChartDE(' + row.woId + ',' + row.projectId + '); getLocalGlobalURLs(\'OpenOutputFilesModal\',' + row.projectId + ')"><i class="fa fa-file-text"></i></a>&nbsp;&nbsp;<a class="popClick" id="popOver' + row.woId + '" href= "#"  data-woid="' + row.woId + '" data-html="true" data-trigger="focus" title="Work Order Info" ><span class="fa fa-info-circle" style="color: blue;cursor:pointer" ></span></a></div>';
                }
            },
            { "title": "Project ID", "data": "projectId", "searchable": true },
            {
                "title": "Network Element Name/ID",
                "targets": 'no-sort',
                "data": 'nodeNames',
                "orderable": false,
                "searchable": true,
                "defaultContent": "",
                "render": function (d, type, row, meta) { 
                    return getNodeNamesHtml(row);
                }
            },

            {
                "title": "Deliverable Name",
                "data": "deliverableName",
                "searchable": false,
                "targets": 'no-sort', "orderable": false
            },

            {
                "title": "Deliverable Unit",
                "data": "deliverableUnitName",
                "searchable": false,
                "targets": 'no-sort', "orderable": false
            },
            {
                "title": "DOID",
                "data": "doid",
                "searchable": true,

            },
            {
                "title": "WO ID",
                "data": "woId",
                "searchable": true
            },
            {
                "title": "WO Name",
                "data": "woName",
                "searchable": true
            },


            {
                "title": "Priority",
                "data": "priority",
                "searchable": true
            },
            {
                "title": "Status",
                "data": "status",
                "searchable": true
            }
            ,
            {
                "title": "Start Date",
                "data": "startDate",
                "searchable": true
            },
            {
                "title": "End Date",
                "data": "endDate",
                "searchable": true
            },
            {
                "title": "Activity",
                "data": "activity",
                "searchable": true
            },
            {
                "title": "SubActivity",
                "data": "subActivity",
                "searchable": true
            }

        ],
        initComplete: function () {
            addFieldsInFooter();
            var api = this.api();
            api.columns().every(function () {
                var that = this;
                let columnHeader= myClosedWOTable.column(that[0]).header().innerText;
                $('input', this.footer()).on('keyup change', function (e) {
                    if (that.search() !== this.value && (e != undefined && e.target != undefined && e.target.type !== "checkbox")) {
                        if (e.target.parentElement.parentNode.id === "dataTable_for_closedWO_filter") {
                            myClosedWOTable.search(this.value).draw();
                        } else {

                            if (columnHeader !== 'Deliverable Name' && columnHeader !== 'Deliverable Unit') {
                                that
                                    .columns($(this).parent().index() + ':visible')
                                    .search(this.value)
                                    .draw();
							}
                            
                        }
                    }
                });
            });
        }

    });
    $('#dataTable_for_closedWO tfoot').insertAfter($('#dataTable_for_closedWO thead'));
    myClosedWOTable.draw();
}

function getNodeNamesHtml(row) {
    let cmt = '';
    if (row.nodeNames != null) {
        let arrNodeName = row.nodeNames.split(',');
        let rd;
        if (arrNodeName.length > 1) {
            rd = escapeHtml(arrNodeName[0]);
        } else {
            rd = escapeHtml(row.nodeNames);
        }
        cmt += '<div class="node">';
        cmt += '<span text-overflow="ellipsis" title="' + row.nodeNames + '">' + rd + '</span>';
        cmt += '<i data-toggle="tooltip" style="cursor:pointer" title="Network Element Details"class="fa fa-list"  onclick="myClosedWO_modalNodeNames(' + row.woPlanId + ',' + row.woId + ')"></i>';
        cmt += '</div>';
    }
    else {
        cmt += '<div class="node">';
        cmt += '<i data-toggle="tooltip" style="cursor:pointer" title="Network Element Details"class="fa fa-list"  onclick="myClosedWO_modalNodeNames(' + row.woPlanId + ',' + row.woId + ')"></i>';
        cmt += '</div>';
    }
    return cmt;
}
// Add Search Box and Select all Check Box in footer as per header of Datatable
function addFieldsInFooter() {
    $('#dataTable_for_closedWO tfoot th').each(function (i) {
        var title = $('#dataTable_for_closedWO thead th').eq($(this).index()).text();
        if (title != "Action") {
            $(this).html('<input type="text" class="form-control" style="font-size:12px;"  placeholder="Search ' + title + '" data-index="' + i + '" />');
        }
    });
}

function downloadWorkOrderHistoryFile(startDate, endDate) {
    if (startDate == "all" || endDate == "all") {
        var today = new Date();
        var tomorrow = new Date(today);
        tomorrow.setDate(tomorrow.getDate() + 1);
        startDate = formatted_date(today);
        endDate = formatted_date(tomorrow);
    }
    var paramSearch = "signum='" + signumGlobal + "'&startDate=" + startDate + "&endDate=" + endDate;
    const woHistoryEncode = encodeURIComponent(paramSearch);
    const serviceUrl = `${service_java_URL}woExecution/downloadWorkHistoryData?${woHistoryEncode}`;
    const fileDownloadUrl = `${UiRootDir}/data/GetFileFromApi?apiUrl=${serviceUrl}`;
    window.location.href = fileDownloadUrl;
}


$(document).on("click", ".popClick", function ($e) {
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

            workOrderInfo += '<tr><td><strong>User Reason</strong></td><td style=\'color:red\'>' + decodeURIComponent(userReason) + '</td></tr>';
            workOrderInfo += '<tr><td><strong>User Comment</strong></td><td style=\'color:red\'>' + decodeURIComponent(userComments) + '</td></tr>';


            workOrderInfo += '<tr><td><strong>Delivery Status</strong></td><td>' + deliveryStatus + '</td></tr>';
            if (data.deliveryStatus == 'Accepted') {
                workOrderInfo += '<tr><td><strong>Delivery Ratings</strong></td><td>' + deliveryRatings + '</td></tr>';
                workOrderInfo += '<tr><td><strong>Delivery Comment</strong></td><td>' + deliveryComment + '</td></tr>';
            }
            else {
                workOrderInfo += '<tr><td><strong>Delivery Reason</strong></td><td style=\'color:red\'>' + decodeURIComponent(deliveryReason) + '</td></tr>';
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
