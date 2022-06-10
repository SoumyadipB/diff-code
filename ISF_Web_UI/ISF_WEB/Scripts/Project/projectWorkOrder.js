const neType = "Network Element Type";
const neName = "Network Element Name/ID";
const neMarket = "Market";

$(document).on("click", "#Transfer", function () {
    getSignum();
    $('#receiver').modal('show');
});

$(document).on("click", "#massTransfer", function () {
    var checkedCount = $(".checkBoxClass:checked").length; var status = ''; var flag = true; var statusArr = [];
    for (var j = 0; j < checkedCount; j++) {
        statusArr.push($(".checkBoxClass:checked").closest("tr").find("#status")[j].textContent);
    }
    $.each(statusArr, function (index, value) {
        if (value == "INPROGRESS" || value == "ONHOLD")
            flag = false;
    });
    if (checkedCount > 0) {
        if (flag) {
            getSignumAR();
            $('#massTransferModal').modal('show');
        }
        else {
            alert("Mass Transfer can only be done on Assigned and Reopened Status!");
        }

    }
    else {
        alert("Please select any work order");
    }


});

$(document).on("change", "#cbxSignumAR", function () {
    selectedSignum = $(this).val();
});

$(document).on("change", "#cbxSignum", function () {
    selectedSignum = $(this).val();
});

$(document).on("click", "#massTransferSubmit", function () {
    //$(".checkBoxClass:checked").each(function () {
    //    // var currentStatus = $(this).closest('tr').find('td:eq(3)').text()       
    //    parseInt(chkArray.push($(this).val()));
    //});
    if ($('#cbxSignum').val() == "Loading Signums...")
        receiver = '0';
    else
    receiver = $('#cbxSignum').val();
    startDate = $('#start_date_mt').val();
    endDate = $('#end_date_mt').val();
    massTransferWorkOrder(receiver,startDate,endDate);
});

function getSignumAR() {
    //pwIsf.addLayer({ text: "Please wait ..." });
    
    //$.isf.ajax({
    //    url: service_java_URL + "activityMaster/getEmployeeDetails",
    //    beforeSend: function (xhr) {
    //        $("#cbxSignum").html("<option> Loading Signums... </option>");
           
    //      //  xhr.setRequestHeader("Authorization", "Bearer " + BearerKey);
    //    },
    //    success: function (data) {

    //        $('#cbxSignum').append('<option value="0">---Select Signum---</option>');
    //        var html = "";
    //        $.each(data, function (i, d) {
    //            $('#cbxSignum').append('<option value="' + d.signum + '">' + d.signum + " - " + d.employeeName + '</option>');
    //        })
    //        $('#cbxSignum').select2('val', '0');
    //    },
    //    complete: function () {
    //        pwIsf.removeLayer();
    //        $('#cbxSignum').find("option").eq(0).remove();
    //    },
    //    error: function (xhr, status, statusText) {
    //        alert("Fail Get Signum " + xhr.responseText);
    //        alert("status " + xhr.status);
    //        console.log('An error occurred');
    //    }
    //});
}

function bindDataTable(groupCol) {
    
    var colNum = groupCol;
    var tableId = '#plannedProjectListTab';
   
  
    table = $(tableId).DataTable({
        
        destroy: true,
        colReorder: true,
        dom: 'Bfrtip',
       // "autoWidth": false,
        buttons: [{
            extend: 'colvis',
            postfixButtons: ['colvisRestore'],
            columns: '2, 3, 4, 5, 6, 7, 8,9,10'
        }, 'excel'],
        //"columns": [{ "width": "25px" }, { "width": "25px" }, { "width": "450px" }, { "width": "200px" }, { "width": "20px" }, { "width": "20px" }, { "width": "20px" }, { "width": "20px" }],
        "columnDefs": [
            { "width": "25px", "targets": 4 },
            { "visible": false, "targets": colNum }
        ],
        "order": [[colNum, 'desc']],
        "displayLength": 25,
        "drawCallback": function (settings) {
           
            var api = this.api();
            var rows = api.rows({ page: 'current' }).nodes();
            var last = null;

            api.column(colNum, { page: 'current' }).data().each(function (group, i) {
                if (last !== group) {
                    $(rows).eq(i).before(
                        '<tr class="group"><td colspan="11">' + group + '</td></tr>'
                    );

                    last = group;
                }
            });
        },
        "rowCallback": function (row, data, index) {
            if (data[5] == "Normal") {
                $('td', row).css('background-color', '#87CEFA');
            }
            else if (data[5] == "Low") {
                $('td', row).css('background-color', '#98FB98');
            }
            else if (data[5] == "High") {
                $('td', row).css('background-color', '#ffd995');
            }         
            else if (data[5] == "Critical") {
                $('td', row).css('background-color', 'RED');
            }
        }
    });

    $(tableId + ' tfoot th').each(function () {
        var title = $(this).text();
        if (title.length != 0)
            $(this).html('<input type="text" class="form-control" style="font-size:12px;" placeholder="Search" />');
    });
    // Apply the search
    table.columns().every(function () {
        var that = this;

        $('input', this.footer()).off('keyup change').on('keyup change', function () {
            if (that.search() !== this.value) {
                that
                    .columns($(this).parent().index() + ':visible')
                    .search(this.value)
                    .draw();
            }
        });
    });


    table.on('column-reorder', function (e, settings, details) {
        //Apply the search
        table.columns().every(function () {
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
    });

    // Order by the grouping
    $(tableId + ' tbody').on('click', 'tr.group', function () {
        var currentOrder = table.order()[0];
        if (currentOrder[0] === colNum && currentOrder[1] === 'asc') {
            table.order([colNum, 'desc']).draw();
        }
        else {
            table.order([colNum, 'asc']).draw();
        }
    });


}

function getElementNamesObj() {

    var elementIdPrefix = "workOrderViewIdPrefix_";
    var controlNames = ['project_name', 'estimated_effort', 'wo_name', 'priority', 'start_time', 'start_date', 'wo_id', 'project_id', 'assigned_to',

        'domain-subdomain', 'technology', 'service-area-sub-service-area', 'activity-subactivity',

        'market', 'vendor', 'sitetype-node-type', 'node-site-name', 'new-node-site', 'node_master_select'
    ];

    var controlObj = {};

    for (var i = 0; i < controlNames.length; i++) {
        controlObj[controlNames[i]] = $('#' + elementIdPrefix + controlNames[i]);
    }

    return controlObj;
}

function getValuesFromApiResponse(data) {

    var retElementsArrVal = [];

    //console.log(data);

    retElementsArrVal['project_name'] = data.responseData[0].project;
    retElementsArrVal['project_id'] = data.responseData[0].projectID;
    retElementsArrVal['estimated_effort'] = data.responseData[0].effort;
    retElementsArrVal['wo_name'] = data.responseData[0].wOName;
    retElementsArrVal['priority'] = data.responseData[0].priority;
    retElementsArrVal['start_time'] = data.responseData[0].startTime;
    retElementsArrVal['start_date'] = data.responseData[0].startDate;
    retElementsArrVal['wo_id'] = data.responseData[0].wOID;
    retElementsArrVal['assigned_to'] = data.responseData[0].signumID;

    retElementsArrVal['domain-subdomain'] = data.responseData[0].domain;
    retElementsArrVal['technology'] = data.responseData[0].technology;
    retElementsArrVal['service-area-sub-service-area'] = data.responseData[0].serviceArea;
    retElementsArrVal['activity-subactivity'] = data.responseData[0].activity;

    retElementsArrVal['market'] = data.responseData[0].market;
    retElementsArrVal['vendor'] = data.responseData[0].vendor;
    retElementsArrVal['sitetype-node-type'] = data.responseData[0].listOfNode[0].nodeType;
    retElementsArrVal['node-site-name'] = data.responseData[0].listOfNode;
    retElementsArrVal['new-node-site'] = 'Missing';

    return retElementsArrVal;
}

function getDateFormat(date) {
    var d = new Date(date),
            month = '' + (d.getMonth() + 1),
            day = '' + d.getDate(),
            year = d.getFullYear();

    if (month.length < 2)
        month = '0' + month;
    if (day.length < 2)
        day = '0' + day;
    var date = new Date();
    date.toLocaleDateString();

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

    elementsObj['priority'].html('');
    elementsObj['priority'].append($('<option>', {
        value: valuesArr['priority'],
        text: valuesArr['priority']
    }));


    elementsObj['priority'].val(valuesArr['priority']);
    elementsObj['start_time'].val(valuesArr['start_time']);

    //var dt = new Date(valuesArr['start_date']);
    //var formatted = dt.getFullYear() + '-' + dt.getMonth() + '-' + dt.getDate();

    var formatted;
    formatted = getDateFormat(valuesArr['start_date']);

    elementsObj['start_date'].val(formatted);

    elementsObj['domain-subdomain'].val(valuesArr['domain-subdomain']);
    elementsObj['technology'].val(valuesArr['technology']);
    elementsObj['service-area-sub-service-area'].val(valuesArr['service-area-sub-service-area']);
    elementsObj['activity-subactivity'].val(valuesArr['activity-subactivity']);

    elementsObj['market'].val(valuesArr['market']);
    elementsObj['vendor'].val(valuesArr['vendor']);
    elementsObj['sitetype-node-type'].html('');
   // elementsObj['sitetype-node-type'].val(valuesArr['sitetype-node-type']);
    elementsObj['sitetype-node-type'].append($('<option>', {
        value: valuesArr['sitetype-node-type'],
        text: valuesArr['sitetype-node-type']
    }));

    elementsObj['node-site-name'].html('');
    elementsObj['node_master_select'].html('');

   
    for (var i = 0; i < valuesArr['node-site-name'].length; i++) {
        elementsObj['node-site-name'].append($('<option>', {
            value: valuesArr['node-site-name'][i].nodeNames,
            text: valuesArr['node-site-name'][i].nodeNames
        }));
        elementsObj['node_master_select'].append($('<option>', {
            value: valuesArr['node-site-name'][i].nodeNames,
            text: valuesArr['node-site-name'][i].nodeNames
        }));


    }



}

function getDODetails(doid, woid) {

    $.isf.ajax({
        url: service_java_URL + "woManagement/getWorkOrdersByDoid?doid=" + doid,
        success: function (data) {
            //pwIsf.removeLayer();


            //$("#table_do_details").append($('<tfoot><tr><th>DOID</th><th>WOID</th><th>NEID</th><th>WFID_WFName_WFVErsion</th><th>Status</th><th>Signum</th></tr></tfoot>'));
            oTableDODetail = $('#table_do_details').DataTable({
                "data": data,
                "destroy": true,
                "searching": true,
                //"pageLength": 5,
                //colReorder: true,
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
                            //$.each(data.listOfNode, function (i, d) {
                            //$.each(d.listOfNode, function (j, e) {
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




                            //});
                            //});

                            var nodesView = `<i data-toggle="tooltip" id="showList${data.woid}"  title="Network Element Details" class="fa fa-list"`;
                            nodesView = `${nodesView}style = "cursor:pointer" onclick = "modalNode(${data.woplanid},${data.woid})" ></i>`;

                            return nodesView;
                        }
                        //"data": "NodeNames",
                        //"searchable": true,
                        //"render": function (data, type, row, meta) //{
                        //    if (row.NodeNames.length < 40) {
                        //        var nodesView = '<span class="nodes" >' + row.NodeNames + '<i data-toggle="tooltip" style="cursor:pointer" title="Node Details"class="fa fa-list"  onclick="modalNodeWO(' + row.wOPlanID + ',' + row.wOID + ')"></i></span>';
                        //    }
                        //    else
                        //        var nodesView = '<span class="nodes">' + row.NodeNames.substr(0, 11) + '<a id="showPara' + row.woid + '" style="cursor:pointer" onclick="showParaWO(' + row.wOID + ')">.....</a><p id="paraShowhide' + row.woid + '" style="display:none">' + row.NodeNames.substr(11, row.NodeNames.length) + '</p><i data-toggle="tooltip" id="collapseParagraph' + row.woid + '"  title="collapse"class="fa fa-chevron-up" style="display:none;cursor:pointer" onclick="collapseParaWO(' + row.woid + ')"></i>&nbsp;&nbsp;<i data-toggle="tooltip" id="showList' + row.woid + '"  title="Node Details"class="fa fa-list" style="display:none;cursor:pointer" onclick="modalNodeWO(' + row.wOPlanID + ',' + row.woid + ')"></i></span>';
                        //    return nodesView;
                        //}
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



                //$('#table_do_details tfoot').insertAfter($('#table_do_details thead'));




            });
        },
        error: function (xhr, status, statusText) {


            console.log('An error occurred on getDomain: ' + xhr.error);
        }
    });
}


function modalNodeWOPopup(wOPlanID, wOID) {
    var listOfNode = JSON.parse(localStorage.getItem(wOPlanID + "_" + wOID));

    $.each(listOfNode, function (i, d) {
        if (d.nodeNames === "" && d.nodeType === "" && d.market === "") {
            listOfNode = [];
        }
    });

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

function bindClickEventOnAction() {  //bind work order view

    //bind work order view
    $('.viewWorkOrder').on('click', function () {

        var workOrderId = $(this).data('workorder-id');
        var status = $(this).data('status');
        var doid = $(this).data('doid');
        var woplanid = $(this).data('woplan-id');
        $('#workOrderViewIdPrefix_doid').val(doid); 
        getDODetails(doid, workOrderId);
        $('.nodeDetailCol').show();
        $('#WODetailPanel').addClass('collapse');
        $('#activityPanel').addClass('collapse');
        //$('#nodeDetailPanel').addClass('collapse');
        //$('#nodeEditPanel').addClass('collapse');
        $('#nodeEditPanel').hide();
        $('#WODetailPanel').removeClass('in');
        $('#activityPanel').removeClass('in');
        //$('#nodeDetailPanel').removeClass('in');
        //$('#nodeEditPanel').removeClass('in');
        //$('.nodeEditCol').hide();
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
        if (typeof $(this).data('edit-work-order-flag') !== 'undefined' || $(this).data('edit-work-order-flag') == false) {
            editFlag = false;
        }
        pwIsf.addLayer({ text: "Please wait ..." });
        
        $.isf.ajax({
            url: service_java_URL + "woExecution/getCompleteWoDetails/" + workOrderId,
            success: function (data) {
                pwIsf.removeLayer();
                $('#all_nodes_master').hide();

                if (data.isValidationFailed == false) {
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
                    $('#btn_update_nodes').attr("disabled", true);

                    if (editFlag == true) {
                        $('#btn_update_nodes').show();
                        $('#edit_node_details').show();
                    } else {
                        $('#btn_update_nodes').hide();
                        $('#edit_node_details').hide();
                    }

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
}

function getValueUsingClass() {
    /* declare an checkbox array */
    var chkArray = [];

    /* look for all checkboes that have a class 'chk' attached to it and check if it was checked */
    $(".checkBoxClass:checked").each(function () {
        // var currentStatus = $(this).closest('tr').find('td:eq(3)').text()       
        parseInt(chkArray.push($(this).val()));
    });

    /* we join the array separated by the comma */
    var selected;
    selected = chkArray.join(',');

    /* check if there is selected checkboxes, by default the length is 1 as it contains one single comma */
    if (selected.length > 1) {
        
        return chkArray;
    } else {

        return 0;
    }
}

function massTransferWorkOrder(receiver, sd, ed) {
   
    var status = []; selectedWO = [];
    
    //if (receiver.length == "0" || receiver.length > 7) {
    //    alert("Please select employee signum");
    //}
    if (signumGlobal.trim() == receiver.toLowerCase().trim()) {
        alert('Work order cannot be transferred to yourself');
    }
    else {
        var selectedworkorders = getValueUsingClass();

        if (selectedworkorders != 0)
        {
        if (receiver == 0 ) {
            receiver = null; 
        }
        else if(sd=="")
            {
                sd = null;
            }
            //{"woID":["5292","5293"],"senderID":"ekuyoge","receiverID":"ESUWXYL","startDate":"<date>"}}
            //{"woID":["5228","5234"],"senderID":"ekuyoge","receiverID":"EORSTVE","startDate":"04/18/2018"}
            var transferArr = { "woID": selectedworkorders, "senderID": signumGlobal, "receiverID": receiver, "startDate":sd };
            //console.log(JSON.stringify(transferArr));
            var request = $.isf.ajax({
                url: service_java_URL + "woManagement/massUpdateWorkOrder",
                method: "POST",
                contentType: "application/json; charset=utf-8",
                data: JSON.stringify(transferArr),
                returnAjaxObj: true,
                dataType: "html"
            });
            request.done(function (msg) {
                getAllPlannedWorkOrderDetailsProject('all','all');
                alert('WO successfully transferred');
                $('#massTransferModal').modal('hide');
                //location.reload();

            });

            request.fail(function (jqXHR, textStatus) {
                alert(" Request failed: ");
            });
        }
        else
            alert("Please select at least one work order");
    }
}

//function transferWorkOrder(receiver) {
//    var status = []; selectedWO = [];
//    if (receiver.length == 0 || receiver == null || receiver.length > 7) {
//        alert("Please select employee signum");
//    }
//    else if(signumGlobal.trim() == receiver.toLowerCase().trim()) {
//        alert('Work order cannot be transferred to yourself');
//    }
//    else {
//        var selectedworkorders = getValueUsingClass();
        
//        if (selectedworkorders != 0) {
//             var transferArr = { "woID": selectedworkorders, "senderID": signumGlobal, "receiverID": receiver };
                    
//                    var request = $.isf.ajax({
//                        url: service_java_URL + "woManagement/transferWorkOrder",
//                        method: "POST",
//                        contentType: "application/json; charset=utf-8",
//                        data: JSON.stringify(transferArr),
//                        dataType: "html"
//                    });
//                    request.done(function (msg) {
                        
//                        location.reload();
                        
//                    });

//                    request.fail(function (jqXHR, textStatus) {
//                        alert(" Request failed: ");
//                    });
//        }
//        else
//            alert("Please select at least one work order");
//    }
//}

function searchByDate(startDate, endDate) {
    if (startDate == '') startDate = 'all'
    if (endDate == '') endDate = 'all'
    var table = $('#plannedProjectListTab').DataTable();
    if ($.fn.dataTable.isDataTable('#plannedProjectListTab')) {
        table.destroy();
        $("#plannedProjectList_tbody").html('');
    }
    getAllPlannedWorkOrderDetailsProject(startDate,endDate);

}

function getAllPlannedWorkOrderDetailsProject(startDate, endDate) {
    if ($.fn.dataTable.isDataTable('#plannedProjectListTab')) {
        table.destroy();
        $('#plannedProjectList_tbody').empty();
    }
    pwIsf.addLayer({ text: "Please wait ..." });
    
    $.isf.ajax({
        url: service_java_URL + "woExecution/searchPlannedWorkOrders/all/all/all/all/all/all/'" + localStorage.getItem("views_project_id") + "'/all/all/all/all/" + startDate + "/" + endDate,
        //url: service_java_URL + "woExecution/searchPlannedWorkOrders/all/all/all/all/all/all/'" + localStorage.getItem("views_project_id") + "'/all/all/all/all/all/all",
        success: function (data) {
            pwIsf.removeLayer();
            //if ($.fn.dataTable.isDataTable('#plannedProjectListTab')) {
            //    table.destroy();
            //    $('#plannedProjectList_tbody').empty();
            //}

            ConvertTimeZones(data, searchPlannedWorkOrders_tzColumns);
           
            $('#plannedProjectList_tbody').html('');

            for (var rec = 0; rec < data.length; rec++) {
                for (recWo = 0; recWo < data[rec].listOfDetails.length; recWo++) {
                    //console.log(data[0]);

                    //var act = '<a href="#" data-toggle="modal" class="viewWorkOrder" data-target="#myModalMadEwo" data-workorder-id="84"><span class="glyphicon glyphicon-edit" data-toggle="tooltip" title="Edit Work Flow" style="color:orangered"></span></a>&nbsp;&nbsp;&nbsp;';
                    localStorage.setItem(data[rec].listOfDetails[recWo].woPlanId + "_" + data[rec].listOfDetails[recWo].woID, JSON.stringify(data[rec].listOfDetails[recWo].listOfNode));
                    var act = '<a href="#" class="viewWorkOrder" data-status="' + data[rec].listOfDetails[recWo].status + '" data-workorder-id="' + data[rec].listOfDetails[recWo].woID + '" data-doid="' + data[rec].listOfDetails[recWo].doid + '" data-woplan-id="' + data[rec].listOfDetails[recWo].woplanID +'"><span class="fa fa-edit" data-toggle="tooltip" title="View Work Order" style="color:purple"></span></a>&nbsp;&nbsp;';
                    //if ((data[rec].listOfDetails[recWo].status == "CLOSED") || (data[rec].listOfDetails[recWo].status == "DEFERRED") || (data[rec].listOfDetails[recWo].status == "REJECTED")) {
                    //    act += '<a href="#" class="viewWorkFlow" onclick="flowChartOpenInNewWindow(' + ' \'FlowChartView\?mode\=view&woID=' + data[rec].listOfDetails[recWo].woID + '\' ' + ')"><span class="fa fa-play" data-toggle="tooltip" title="View work Flow" style="color:green"></span></a>&nbsp;&nbsp;';

                    //}
                    //else
                    //    act += '<a href="#" class="viewWorkFlow" onclick="flowChartOpenInNewWindow(' + ' \'FlowChartProject\?mode\=view&woID=' + data[rec].listOfDetails[recWo].woID + '&subActID=' + data[rec].listOfDetails[recWo].subActivityID + '\' ' + ')"><span class="fa fa-play" data-toggle="tooltip" title="View work Flow" style="color:green"></span></a>&nbsp;&nbsp;';

                    //if ((data[rec].listOfDetails[recWo].status == "ASSIGNED") || (data[rec].listOfDetails[recWo].status == "REOPENED") || (data[rec].listOfDetails[recWo].status == "INPROGRESS"))
                    //    act += '<a href="#" class="hold-WO"  data-id="' + data[rec].listOfDetails[recWo].woID + '" data-toggle="modal" data-target="#modalHoldWO"><span class="fa fa-pause"  title="Mark as on Hold" style="color:black"></span></a>&nbsp;&nbsp;<a href="#" class="deferred-WO" data-woid="' + data[rec].listOfDetails[recWo].woID + '" data-toggle="modal" data-target="#modalDefferedWO"><span class="fa fa-warning" title="Mark as Deferred" style="color:black"></span></a>&nbsp;&nbsp;'

                    if ((data[rec].listOfDetails[recWo].status == "CLOSED") || (data[rec].listOfDetails[recWo].status == "REJECTED") || (data[rec].listOfDetails[recWo].status == "REOPENED")) {
                        act += '<a class="popClick" id="popOver' + data[rec].listOfDetails[recWo].woID + '" href= "#"  data-woid="' + data[rec].listOfDetails[recWo].woID + '" data-html="true" data-trigger="focus" title="Work Order Info" ><span class="fa fa-info-circle" style="color: blue;cursor:pointer" ></span></a>';
                    }

                    var tr = '<tr>';
                    if ((data[rec].listOfDetails[recWo].status == "CLOSED") || (data[rec].listOfDetails[recWo].status == "DEFERRED") || (data[rec].listOfDetails[recWo].status == "REJECTED")) {
                        tr += '<td></td>';
                    }
                    else {
                        tr += '<td><label><input type="checkbox" class="checkBoxClass" value="' + data[rec].listOfDetails[recWo].woID + '"></label></td>';
                    }

                    if (data[rec].listOfDetails[recWo].listOfNode[0].nodeNames == null)
                        data[rec].listOfDetails[recWo].listOfNode[0].nodeNames = '';
                    tr += '<td>' + act + '</td>';
                    tr += '<td>' + data[rec].projectName + '</td>';
                    tr += '<td>' + data[rec].listOfDetails[recWo].woID + '</td>';
                    tr += '<td>' + data[rec].listOfDetails[recWo].woName + '</td>';
                    tr += '<td>' + data[rec].listOfDetails[recWo].priority + '</td>';
                    tr += '<td id="status">' + data[rec].listOfDetails[recWo].status + '</td>';
                    tr += '<td>' + data[rec].listOfDetails[recWo].loe + '</td>';
                    tr += '<td>' + data[rec].listOfDetails[recWo].signumID + '</td>';
                    tr += '<td>' + data[rec].listOfDetails[recWo].plannedStartDate + ' ' + data[rec].listOfDetails[recWo].plannedStartTime + '</td>';

                    tr += '<td>' + data[rec].listOfDetails[recWo].activity + '</td>';
                    tr += '<td>' + data[rec].listOfDetails[recWo].subActivity + '</td>';
                    if (data[rec].listOfDetails[recWo].listOfNode[0].nodeNames.length < 40) {
                        tr += '<td class="nodes" >' + data[rec].listOfDetails[recWo].listOfNode[0].nodeNames.substr(0, 40) + ' <i data-toggle="tooltip" style="cursor:pointer" title="Node Details"class="fa fa-list"  onclick="modalNode(' + data[rec].listOfDetails[recWo].woPlanId + ',' + data[rec].listOfDetails[recWo].woID + ')"></i></td>';
                    }
                    else
                        tr += '<td class="nodes">' + data[rec].listOfDetails[recWo].listOfNode[0].nodeNames.substr(0, 40) + ' <a id="showPara' + data[rec].listOfDetails[recWo].woID + '" style="cursor:pointer" onclick="showPara(' + data[rec].listOfDetails[recWo].woID + ')">.....</a><p id="paraShowhide' + data[rec].listOfDetails[recWo].woID + '" style="display:none">' + data[rec].listOfDetails[recWo].listOfNode[0].nodeNames.substr(11, data[rec].listOfDetails[recWo].listOfNode[0].nodeNames.length) + '</p><i data-toggle="tooltip" id="collapseParagraph' + data[rec].listOfDetails[recWo].woID + '"  title="collapse"class="fa fa-chevron-up" style="display:none;cursor:pointer" onclick="collapsePara(' + data[rec].listOfDetails[recWo].woID + ')"></i>&nbsp;&nbsp;<i data-toggle="tooltip" id="showList' + data[rec].listOfDetails[recWo].woID + '"  title="Node Details"class="fa fa-list" style="display:none;cursor:pointer" onclick="modalNode(' + data[rec].listOfDetails[recWo].woPlanId + ',' + data[rec].listOfDetails[recWo].woID + ')"></i></td>';

                    //tr += '<td>' + '<i class="md md-settings-cell" title="Node" style="cursor: pointer;" onclick="showNodesModel(' + '\'' + data[rec].listOfDetails[recWo].listOfNode[0].nodeNames + '\',\'' + data[rec].listOfDetails[recWo].listOfNode[0].nodeType + '\',\'' + data[rec].listOfDetails[recWo].woID + '\'' + ')"></i> ' + '</td>';

                    tr += '</tr>';

                    $('#plannedProjectList_tbody').append($(tr));
                }
            }
            
            bindClickEventOnAction();

            bindDataTable(2);

            $('#groupDiv').show();
            $('#groupDiv').css("display", "inline-flex");

            //Checking all boxes
            $("#ckbCheckAll").click(function () {

                $(".checkBoxClass").prop('checked', $(this).prop('checked'));
            });
            $(".checkBoxClass").on('change', function () {

                if (!$(this).prop("checked")) {
                    $("#ckbCheckAll").prop("checked", false);
                }
            });
            //Checking all boxes

        },        
        error: function (xhr, status, statusText) {
            pwIsf.removeLayer();
            $('#plannedProjectList_tbody').html('<tr><td colspan="11" style="text-align:center">No data found for this user.</td></tr>');
        }
    });
}

function showPara(item) {
    // alert(item);
    $('#paraShowhide' + item).show();
    $('#showPara' + item).hide();
    $('#collapseParagraph' + item).show();
    $('#showList' + item).show();
}

function collapsePara(item) {
    $('#paraShowhide' + item).hide();
    $('#showPara' + item).show();
    $('#collapseParagraph' + item).hide();
    $('#showList' + item).hide();
}

function modalNode(wOPlanID, wOID) {
    var listOfNode = JSON.parse(localStorage.getItem(wOPlanID + "_" + wOID));

    // $(".fa-play-circle").('listOfNode[]: ' + listOfNode[0].nodeType);

    $('#table_wo_nodes_project').DataTable({
        "data": listOfNode,
        "destroy": true,
        searching: true,
        order: [1],

        //"pageLength": 50,
        "columns": [

            {
                "title": "Node Type",
                "data": "nodeType"
            },
            {
                "title": "Node Names",
                "data": "nodeNames"
            }
        ]
    });

    $('#modal_wo_nodes_project').modal('show');

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
            // console.log(err.errorMessage);
            workOrderInfo = err.errorMessage;
        }

    });
    $(idPop).popover({
        content: workOrderInfo,
        container: 'body'
    });
   $(idPop).popover('toggle');
});

function resetDate() {
    $('#start_date_wo').val('');
    $('#end_date_wo').val('');
}
