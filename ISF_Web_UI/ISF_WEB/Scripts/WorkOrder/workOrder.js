var woListOfNode;
var modalCreatedBy = "";
var woProjectID = localStorage.getItem("views_project_id");
var scopeID = 0;
var wOIDglobal;
var projectID = localStorage.getItem("views_project_id");

const C_SERAREA_ERR_MSG = 'An error occurred on getServiceAreaDetails:';
const C_DOM_ERR_MSG = 'An error occurred on getDomain:';
const C_ERR_MSG = 'An error occurred';
const C_TBL_WO_PLAN_ID = '#table_wo_planning tbody';
const C_EDIT_SUC_MODAL_ID = '#edit_success';
const C_EMP_STAT_CHANGE_ID = '#employee_change_status';
const C_START_DATE_ID = '#Start_Date';
const C_START_TIME_ID = '#start_time';
const C_PROJ_NAME_ID = '#select_project_name';
const C_WO_DOMAIN_ID = '#woc_select_domain';
const C_PROD_AREA_ID = '#select_product_area';
const C_ASSIGN_TO_USER_ID = '#select_assign_to_user';
const C_NODE_TYPE_SELECT_ID = '#select_nodeType';
const C_NODE_NAME_SELECT_ID = '#select_nodeName';
const C_SELECT_PRIORITY_ID = '#select_priority';
const C_TECH_SELECT_ID = '#select_technology';
const C_SELECT_ACT_ID = '#select_activity';
const C_WO_CREATE_MODAL_ID = '#modal_wo_creation';
const C_SIGNUM_MODAL_ID = '#select_modal_signum';

$(document).ready(function () {
    const C_WO_TABLE_ID = '#table_work_order';
    const C_MY_TAB_ID = '#my-tab-content';
    const C_TABLE_MESSAGE_ID = '#table_message';
    $(C_WO_TABLE_ID).DataTable();
    wo_getEmployeeDetails();
    wo_getPriority();
    wo_getNodeType();
    $(C_MY_TAB_ID).hide();
    $(C_TABLE_MESSAGE_ID).hide();
    $("[id$=modal_planned_start_date]").datepicker({ minDate: 0 });
    $("[id$=modal_planned_end_date]").datepicker({ minDate: 0 });
    $("[id$=modal_actual_start_date]").datepicker({ minDate: 0 });
    $("[id$=modal_actual_end_date]").datepicker({ minDate: 0 });
    search();
});

function wo_getServiceAreaDetails() {
    const C_WO_SELECT_PROD_AREA_ID = '#wo_select_product_area';
    const C_ZERO_VALUE_SELECT_HTML = '<option value="0">select</option>';
    const C_WRONG_PROJID_MSG = 'Wrong Project Id';

    $(C_WO_SELECT_PROD_AREA_ID).html('');
    $(C_WO_SELECT_PROD_AREA_ID).append(C_ZERO_VALUE_SELECT_HTML);
    if (projectID === undefined || projectID === null || projectID === '' || isNaN(parseInt(projectID))) {
        console.log(C_WRONG_PROJID_MSG);
        pwIsf.alert({ msg: C_WRONG_PROJID_MSG, type: 'error' });
    }
    else {
        $.isf.ajax({
            url: `${service_java_URL}activityMaster/getServiceAreaDetails?ProjectID=${parseInt(projectID)}`,
            success: function (data) {
                $.each(data, function (i, d) {
                    $(C_WO_SELECT_PROD_AREA_ID).append(`<option value="${d.serviceAreaID}">${d.serviceArea}</option>`);
                });
            },
            error: function (xhr, status, statusText) {
                console.log(`${C_SERAREA_ERR_MSG} ${xhr.error}`);
            }
        });
    }
}

function wo_getDomainSub_change() {
    const C_WO_SELECT_DOMAIN_ID = '#wo_select_domain';
    const C_ZERO_VALUE_SELECT_HTML = '<option value="0">select</option>';
    $(C_WO_SELECT_DOMAIN_ID).html('');
    $(C_WO_SELECT_DOMAIN_ID).append(C_ZERO_VALUE_SELECT_HTML);
    var ServiceAreaID = document.getElementById('wo_select_product_area').value;
    $.isf.ajax({
        url: `${service_java_URL}activityMaster/getDomainDetails?ProjectID=${projectID}&ServiceAreaID=${ServiceAreaID}`,
        success: function (data) {
            $.each(data, function (i, d) {
                $(C_WO_SELECT_DOMAIN_ID).append(`<option value="${d.domainID}">${d.domain}</option>`);
            });
        },
        error: function (xhr, status, statusText) {
            console.log(`${C_DOM_ERR_MSG} ${xhr.error}`);
        }
    });
}

function wo_getTechSub_change() {
    const C_WO_SELECT_TECH_ID = '#wo_select_technology';
    const C_ZERO_VALUE_SELECT_HTML = '<option value="0">select</option>';
    $(C_WO_SELECT_TECH_ID).html('');
    $(C_WO_SELECT_TECH_ID).append(C_ZERO_VALUE_SELECT_HTML);
    var domainID = document.getElementById('wo_select_domain').value;
    $.isf.ajax({
        url: `${service_java_URL}activityMaster/getTechnologyDetails?domainID=${domainID}&projectID=${projectID}`,
        success: function (data) {
            $.each(data, function (i, d) {
                $(C_WO_SELECT_TECH_ID).append(`<option value="${d.technologyID}">${d.technology}</option>`);
            });
        },
        error: function (xhr, status, statusText) {
            console.log(C_COMMON_ERROR_MSG);
        }
    });
}

function wo_getActivitiesSub() {
    const C_WO_SELECT_SUBACT_ID = '#wo_select_activitysub';
    const C_WO_SELECT_ACT_ID = '#wo_select_activity';
    const C_ZERO_VALUE_SELECT_HTML = '<option value="0">select</option>';

    $(C_WO_SELECT_SUBACT_ID).html('');
    $(C_WO_SELECT_ACT_ID).html('');
    $(C_WO_SELECT_SUBACT_ID).append(C_ZERO_VALUE_SELECT_HTML);
    $(C_WO_SELECT_ACT_ID).append(C_ZERO_VALUE_SELECT_HTML);
    var domainID = document.getElementById("wo_select_domain").value;
    var serviceAreaID = document.getElementById("wo_select_product_area").value;
    var technologyID = document.getElementById("wo_select_technology").value;
    $.isf.ajax({
        url: `${service_java_URL}activityMaster/getActivityAndSubActivityDetails/${domainID}/${serviceAreaID}/${technologyID}`,
        success: function (data) {
            $(C_WO_SELECT_SUBACT_ID).empty();
            $(C_WO_SELECT_ACT_ID).empty();
            $.each(data, function (i, d) {
                $(C_WO_SELECT_SUBACT_ID).append(`<option value="${d.subActivityID}">${d.subActivityName}</option>`);
                $(C_WO_SELECT_ACT_ID).append(`<option value="${d.activity}">${d.activity}</option>`);
            });
        },
        error: function (xhr, status, statusText) {
            console.log(C_ERR_MSG);
        }
    });
}

function wo_getPriority() {
    const C_SELECT_MODAL_PRIORITY_ID = '#select_modal_priority';
    const C_ERROR_MSG = 'An error occurred on getProjectName:';

    $.isf.ajax({
        url: `${service_java_URL}woManagement/getPriority`,
        success: function (data) {
            $.each(data, function (i, d) {
                $(C_SELECT_MODAL_PRIORITY_ID).append(`<option value="${d}">${d}</option>`);
            });
        },
        error: function (xhr, status, statusText) {
            console.log(`${C_ERROR_MSG} ${xhr.error}`);
        }
    });
}

function wo_getNodeType() {
    const C_MISSING_PROJID_MSG = "Project ID missing or invalid";
    const C_SELECT_NODE_TYPE_ID = '#select_node_type';
    const C_ZERO_VALUE_SELECT_HTML = '<option value="0"></option>';
    const C_ERROR_MSG = 'An error occurred on getProjectName:';

    if (projectID === undefined || projectID === null || projectID === '' || isNaN(parseInt(projectID))) {
        pwIsf.alert({ msg: C_MISSING_PROJID_MSG, type: "error" });
    }
    else {
        $.isf.ajax({
            url: `${service_java_URL}woManagement/getNodeType/${parseInt(projectID)}`,
            success: function (data) {
                $(C_SELECT_NODE_TYPE_ID).append(C_ZERO_VALUE_SELECT_HTML);
                $.each(data, function (i, d) {
                    $(C_SELECT_NODE_TYPE_ID).append(`<option value="${d}">${d}</option>`);
                });
            },
            error: function (xhr, status, statusText) {
                console.log(`${C_ERROR_MSG} ${xhr.error}`);
            }
        });
    }
}

function wo_getNodeNames() {
    var type = $('#select_node_type').val();
    const C_MODAL_SELECT_NODE_NAME_ID = '#modal_select_node_name';
    const C_ERROR_MSG = 'An error occurred on getProjectName:';

    $.isf.ajax({
        url: `${service_java_URL}woManagement/getNodeNames/${projectID}/${type}`,
        success: function (data) {
            $(C_MODAL_SELECT_NODE_NAME_ID).empty();
            $.each(data, function (i, d) {
                $(C_MODAL_SELECT_NODE_NAME_ID).append(`<option value="${d.lstNodeName}">${d.lstNodeName}</option>`);
            });
        },
        error: function (xhr, status, statusText) {
            console.log(`${C_ERROR_MSG} ${xhr.error}`);
        }
    });
}

function format(data) {
    var projectId = data.projectID;
    var table =
        `<table cellpadding="5" cellspacing="0" border="0" style="padding-left:50px;width:100%;margin-left:50px;">
        <thead>
        <tr>
        <th></th>
        <th>WOID</th>
        <th>Planned Start Date</th>
        <th>Planned End Date</th>
        <th>Status</th>
        <th>StartedDate</th>
        <th>ClosedOn</th>
        <th>Signum</th>
        <th>Node Type</th>
        <th>Priority</th>
        </tr>
        </thead>
        <tbody>`;

    $.each(data.listOfWorkOrder, function (i, d) {
        if (d.actualStartDate !== null) {
            d.actualStartDate = (new Date(d.actualStartDate)).toLocaleDateString();
        }
        if (d.closedOn !== null) {
            d.closedOn = (new Date(d.closedOn)).toLocaleDateString();
        }
        table = `${table}<tr>`;
        table = `${table}<td>`;

        if (d.status === "ASSIGNED" || d.status === "REOPENED") {
            table = ` ${table}<i class="md md-mode-edit" title="Edit"
            style="display:none;cursor: pointer;"
            onclick="openModalEdit(${projectId},${d.wOID},${d.wOPlanID},${d.actualStartDate},
            ${d.actualEndDate},${d.plannedStartDate},${d.plannedEndDate},${d.signumID},
            ${d.priority},${d.listOfNode[0].nodeType},${d.listOfNode[0].nodeNames})"/>
            <i class="md md-delete" title="Delete" style="cursor: pointer;"
            onclick="deleteWO(${d.wOID},${d.lastModifiedBy},${d.status})"> </i>  `;
        }
        if (d.status === "ONHOLD") {
            table = `${table}<i class="md md-mode-edit" title="Edit"
            style="display:none;cursor: pointer;"
            onclick="openModalEdit(${projectId},${d.wOID},${d.wOPlanID},${d.actualStartDate},
            ${d.actualEndDate},${d.plannedStartDate},${d.plannedEndDate},${d.signumID},
            ${d.priority},${d.listOfNode[0].nodeType},${d.listOfNode[0].nodeNames})"/>
            <i class="md md-credit-card" title="Transfer" style="cursor: pointer;"
            onclick="modalTransfer(${d.wOID},${d.createdBy})"></i> `;
        }
        if (d.status !== "CLOSED" || d.status !== "REJECTED") {
            table =
                `${table}<i class="md md-credit-card" title="Transfer"
                style="display:none;cursor: pointer;"
                onclick="modalTransfer(${d.wOID},${d.createdBy})"></i> `;
        }
        table = `${table}</td>`;
        table = `${table}
             <td>${d.wOID}</td>
             <td>${(new Date(d.plannedStartDate)).toLocaleDateString()}</td>
             <td>${(new Date(d.plannedEndDate)).toLocaleDateString()}</td>
             <td>${d.status}</td>
             <td>${d.actualStartDate}</td>
             <td>${d.closedOn}</td>
             <td>${d.signumID}</td>
             <td><i class="md md-settings-cell" title="Node" style="cursor: pointer;"
                onclick="modalNode(${d.wOPlanID},${d.wOID})"></i></td>
             <td>${d.priority}</td>
             </tr>`;
        localStorage.setItem(`${d.wOPlanID}_${d.wOID}`, JSON.stringify(d.listOfNode));
    });

    table = `${table}</tbody></table>`;
    return table;
}

function modalNode(wOPlanID, wOID) {
    var listOfNode = JSON.parse(localStorage.getItem(`${wOPlanID}_${wOID}`));
    const C_TABLE_WO_NODES = '#table_wo_nodes';
    const C_MODAL_WO_NODES = '#modal_wo_nodes';

    $(C_TABLE_WO_NODES).DataTable({
        "dataSrc": listOfNode,
        "destroy": true,
        searching: true,
        "columns": [
            {
                "title": neType,
                "data": "nodeType"
            },
            {
                "title": neName,
                "data": "nodeNames"
            }
        ]
    });
    $(C_MODAL_WO_NODES).modal('show');
}

function search() {
    var domainID = 0;
    var serviceAreaID = 0;
    var technologyID = 0;
    var activityID = 0;
    var subactivityID = 0;
    var oTable;
    const C_TABLE_WO_PLANNING = '#table_wo_planning';

    $(C_TABLE_WO_PLANNING).empty();
    var urlSearch = `${projectID}/${domainID}/${serviceAreaID}/${technologyID}/${activityID}/${subactivityID}`;
    console.log(`url ${urlSearch}`);
    $.isf.ajax({
        url: `${service_java_URL}woManagement/searchWorkOrderPlanDetails/${urlSearch}`,
        success: function (data) {
            $('#my-tab-content').show();
            $('#table_message').hide();
            $.each(data, function (i, d) {
                d.startDate = (new Date(d.startDate)).toLocaleDateString();
                d.endDate = (new Date(d.endDate)).toLocaleDateString();
            });
            oTable = $(C_TABLE_WO_PLANNING).DataTable({
                "data": data,
                "destroy": true,
                searching: true,
                "pageLength": 50,
                "columns": [
                    {
                        "className": 'details-control',
                        "orderable": false,
                        "data": null,
                        "defaultContent": ''
                    },
                    {
                        "targets": 'no-sort',
                        "orderable": false,
                        "searchable": false,
                        "data": null,
                        "defaultContent": "<i style='cursor: pointer;display:none;' title='Edit' class='md md-edit'></i><i style='cursor: pointer;' title='Delete' class='md md-delete first_delete'  '></i> "
                    },
                    {
                        "title": "wOPlanID",
                        "data": "wOPlanID"
                    },
                    {
                        "title": "Project",
                        "data": "projectID"
                    },
                    {
                        "title": "Domain / Subdomain",
                        "data": "activityDetails[0].domain"
                    },
                    {
                        "title": "Service Area / Subservice Area",
                        "data": "activityDetails[0].serviceArea"
                    },
                    {
                        "title": "Technology",
                        "data": "activityDetails[0].technology"
                    },
                    {
                        "title": "Activity",
                        "data": "subActivity[0].activity",
                    },
                    {
                        "title": "Subactivity",
                        "data": "subActivity[0].subActivityName",
                    },
                    {
                        "title": "Periodicity",
                        "data": "periodicityDaily"
                    },
                    {
                        "title": "Start Date",
                        "data": "startDate",
                    },
                    {
                        "title": "End Date",
                        "data": "endDate"
                    },
                    {
                        "title": "Assigned To",
                        "data": "signumID"
                    },
                    {
                        "title": "Priority",
                        "data": "priority"
                    }
                ]
            });

            $(C_TBL_WO_PLAN_ID).on('click', 'td.details-control', function () {
                var tr = $(this).closest('tr');
                var row = oTable.row(tr);

                if (row.child.isShown()) {
                    // This row is already open - close it
                    row.child.hide();
                    tr.removeClass('shown');
                }
                else {
                    // Open this row
                    row.child(format(row.data())).show();
                    tr.addClass('shown');
                }
            });

            $(C_TBL_WO_PLAN_ID).on('click', 'i.first_delete', function () {
                var data = oTable.row($(this).parents('tr')).data();
                if (validateDeleteEditWorkOrderPlan(data.listOfWorkOrder)) {
                    console.log("data.wOPlanID " + data.wOPlanID);
                    console.log("data.lastModifiedBy  " + data.lastModifiedBy);
                    deleteWorkOrderPlan(data.wOPlanID, data.lastModifiedBy);
                } else {
                    $(C_EDIT_SUC_MODAL_ID).modal('show');
                    $(C_EMP_STAT_CHANGE_ID).html('Work Order status is in progress, so you cannot delete this work order plan .');
                }
            });

            $('#table_wo_planning tbody').on('click', 'i.md-edit', function () {
                var data = oTable.row($(this).parents('tr')).data();
                if (validateDeleteEditWorkOrderPlan(data.listOfWorkOrder)) {
                    getWorkOrderPlanDetails(data.wOPlanID, data.projectID, data.signumID);
                    localStorage.setItem("projectID", data.projectID);
                    localStorage.setItem("woPlanID", data.wOPlanID);
                    localStorage.setItem("signumID", data.signumID);
                    localStorage.setItem("lastModifiedBy", data.lastModifiedBy);
                    localStorage.setItem("createdBy", data.createdBy);
                    listOfNode = [{
                        "nodeType": data.listOfNode.nodeType,
                        "nodeNames": data.listOfNode.nodeNames
                    }]
                    localStorage.setItem("listOfNode", data.listOfNode);
                    $('.nav a[href="#menu5"]').tab('show')
                } else {
                    $(C_EDIT_SUC_MODAL_ID).modal('show');
                    $(C_EMP_STAT_CHANGE_ID).html('The work order plan cannot be edited. There is a work order with status different from ASSIGNED.');
                }
            });
            $('#my-tab-content').show();
        },
        error: function (xhr, status, statusText) {
            $('#my-tab-content').hide();
            $('#table_message').show();
            console.log(`${C_DOM_ERR_MSG} ${xhr.error}`);
        }
    });
}

function refreshTable() {
    $('#table_wo_planning').empty();
    search();
}

function getWorkOrderPlanDetails(wOPlanID, projectID, signumID) {
    gotoWorkOrder();
    $.isf.ajax({
        url: `${service_java_URL}woManagement/getWorkOrderPlanDetails/${wOPlanID}/${projectID}/${signumID}`,
        success: function (data) {
            console.log("select_product_area: " + data[0].subActivity[0].serviceAreaID);
            console.log("select_technology: " + data[0].subActivity[0].technologyID);
            console.log("select_activity: " + data[0].subActivity[0].activity);
            console.log("select_activitysub: " + data[0].subActivity[0].subActivityID);
            console.log("select_nodeType: " + data[0].listOfNode[0].nodeType);
            console.log("select_nodeName: " + data[0].listOfNode[0].nodeNames);
            console.log("startDate: " + data[0].startDate);
            console.log("startTime: " + data[0].startTime);
            console.log("startTime: " + data[0].startTime.substring(0, 2));
            console.log("endDate: " + data[0].endDate);
            console.log("priority: " + data[0].priority);
            console.log("signumID: " + data[0].signumID);
            var startDate = (new Date(data[0].startDate));
            var endDate = (new Date(data[0].endDate));
            console.log("start: " + startDate.getFullYear() + "-" + startDate.getMonth() + "-" + (startDate.getDay().toString().length == 1 ? "0" + startDate.getDay() : startDate.getDay()));
            $(C_START_DATE_ID).val(startDate.getFullYear() + "-" + startDate.getMonth() + "-" + (startDate.getDay().toString().length == 1 ? "0" + startDate.getDay() : startDate.getDay()));
            $(C_START_TIME_ID).val(data[0].startTime.substring(0, 2) + ":00");
            $(C_START_TIME_ID).trigger('change');
            $('#End_Date').val(endDate.getFullYear() + "-" + endDate.getMonth() + "-" + (endDate.getDay().toString().length == 1 ? "0" + endDate.getDay() : endDate.getDay()));



            $(C_PROJ_NAME_ID).val(data[0].projectID);
            $(C_PROJ_NAME_ID).trigger('change');

            $(C_WO_DOMAIN_ID).val(data[0].activityDetails[0].domainID);
            $(C_WO_DOMAIN_ID).trigger('change');

            $(C_PROD_AREA_ID).val(data[0].activityDetails[0].serviceAreaID);
            $(C_PROD_AREA_ID).trigger('change');

            $(C_ASSIGN_TO_USER_ID).val(data[0].signumID);
            $(C_ASSIGN_TO_USER_ID).trigger('change');





            $(C_NODE_TYPE_SELECT_ID).val(data[0].listOfNode[0].nodeType);
            $(C_NODE_TYPE_SELECT_ID).trigger('change');

            $(C_NODE_NAME_SELECT_ID).val(data[0].listOfNode[0].nodeNames);
            $(C_NODE_NAME_SELECT_ID).trigger('change');
            $(C_SELECT_PRIORITY_ID).val(data[0].priority);

            $(C_TECH_SELECT_ID).val(data[0].activityDetails[0].technologyID);
            $(C_TECH_SELECT_ID).trigger('change');

            $(C_SELECT_ACT_ID).val(data[0].subActivity[0].activity);
            $(C_SELECT_ACT_ID).trigger('change');

            $('#select_activitysub').val(data[0].subActivity[0].subActivityID);
            $('#select_activitysub').trigger('change');
        },
        error: function (xhr, status, statusText) {
            console.log(`${C_SERAREA_ERR_MSG} ${xhr.error}`);
        }
    });

}

function validateDeleteEditWorkOrderPlan(listOfNode) {
    var result = true;

    $.each(listOfNode, function (i, d) {
        if (d.status !== "ASSIGNED") {
            result = false;
        }
    });

    return result;
}

function openModalEdit(projectID, wOID, wOPlanID, actualStartDate, actualEndDate, plannedStartDate, plannedEndDate, signumID, priority, nodeType, nodeNames) {

    console.log("priority: " + priority);
    console.log("nodeType: " + nodeType);
    console.log("nodeNames: " + nodeNames);

    getNodeType(projectID);
    $('#select_node_type').val(nodeType);
    $('#select_node_type').trigger('change');
    $('#modal_input_wOID').val(wOID);
    $('#modal_input_wOPlanID').val(wOPlanID);
    console.log((new Date(plannedStartDate)).toDateString());
    $('#modal_planned_start_date').val((new Date(plannedStartDate)).toLocaleDateString());
    $('#modal_planned_end_date').val((new Date(plannedEndDate)).toLocaleDateString());
    $('#modal_actual_start_date').val(actualStartDate);
    $('#modal_actual_end_date').val(actualEndDate);
    $('#select_modal_signum2').val(signumID);
    $('#select_modal_signum2').trigger('change');
    $('#select_modal_priority').val(priority);
    $('#select_modal_priority').trigger('change');
    $('#modal_select_node_name').val(nodeNames);
    $('#modal_select_node_name').trigger('change');
    $('#modal_work_order').modal("show");
}

function getNodeType(projectID) {
    if (projectID === undefined || projectID == null || projectID === '' || isNaN(parseInt(projectID))) {
        pwIsf.alert({ msg: "Project ID missing or invalid", type: "error" });
    }
    else {
        $.isf.ajax({
            url: `${service_java_URL}woManagement/getNodeType/${parseInt(projectID)}`,
            success: function (data) {
                $.each(data, function (i, d) {
                    $(C_NODE_TYPE_SELECT_ID).append('<option value="' + d + '">' + d + '</option>');
                })

            },
            error: function (xhr, status, statusText) {
                console.log(`${C_DOM_ERR_MSG} ${xhr.error}`);
            }
        });
    }
}

function deleteWorkOrderPlan(wOPlanID, lastModifiedBy) {

    var wo = new Object();
    wo.projectID = localStorage.getItem("views_project_id");
    wo.wOPlanID = wOPlanID;
    wo.lastModifiedBy = signumGlobal;

    console.log("JSON.stringify(wo)   " + JSON.stringify(wo));
    $.isf.ajax({
        url: `${service_java_URL}woManagement/deleteWorkOrderPlan/`,
        context: this,
        crossdomain: true,
        processData: true,
        contentType: C_CONTENT_TYPE_APPLICATION_JSON,
        type: 'POST',
        data: JSON.stringify(wo),
        xhrFields: {
            withCredentials: false
        },
        success: function (data) {
            $(C_EDIT_SUC_MODAL_ID).modal('show');
            $(C_EMP_STAT_CHANGE_ID).html('Your work order plan was deleted.');
            search();
        },
        error: function (xhr, status, statusText) {
            console.log('An error occurred on deleteWorkOrderPlan: ' + xhr);

            $(C_EDIT_SUC_MODAL_ID).modal('show');
            $(C_EMP_STAT_CHANGE_ID).html('There was an error to delete the work order. ');
        }
    });
}

function deleteWO(wOID,LastModifiedBy,Status) {
    $.isf.ajax({
        url: `${service_java_URL}woManagement/deleteWorkOrder/${wOID}/${signumGlobal}`,
        context: this,
        crossdomain: true,
        processData: true,
        contentType: C_CONTENT_TYPE_APPLICATION_JSON,
        type: 'POST',
        xhrFields: {
            withCredentials: false
        },
        success: function (data) {
            refreshTable();
            $(C_EDIT_SUC_MODAL_ID).modal('show');
            $(C_EMP_STAT_CHANGE_ID).html('Your work order was deleted.');
        },
        error: function (xhr, status, statusText) {
            $(C_EDIT_SUC_MODAL_ID).modal('show');
            $(C_EMP_STAT_CHANGE_ID).html('Your work order cannot be deleted.');
        }
    });
}

function modalTransfer(wOPlanID, createdBy) {
    $('#modalTransfer').modal('show');
    $('#modal_input_woid').val(wOPlanID);
    $(C_SIGNUM_MODAL_ID).val(0);
    $(C_SIGNUM_MODAL_ID).trigger('change');
    modalCreatedBy = createdBy;
}

function updateWorkOrder() {

    var wo = new Object();
    wo.wOID = $("#modal_input_wOID").val();
    wo.signumID = $("#select_modal_signum2").val();
    wo.lastModifiedBy = signumGlobal;
    wo.priority = $('#select_modal_priority').val();
    wo.listOfNode = [{
        "nodeType": $('#select_node_type').val(),
        "nodeNames": $('#modal_select_node_name').val(),
    }];


    $.isf.ajax({
        url: `${service_java_URL}woManagement/updateWorkOrder`,
        context: this,
        crossdomain: true,
        processData: true,
        contentType: 'application/json',
        type: 'POST',
        data: JSON.stringify(wo),
        xhrFields: {
            withCredentials: false
        },
        success: function (data) {
            if (data === "") {
                $('#modal_work_order').modal("hide");
                $('#scopeModal2').modal('show');
                $('#modal_text').text("The work oder was updated successfully ");
            } else {
                $('#modal_work_order').modal("hide");
                $('#scopeModal2').modal('show');
                $('#modal_text').text("The work oder wasn't updated successfully ");
            }
            search();
        },
        error: function (xhr, status, statusText) {
            $('#modal_work_order').modal("hide");
            $('#scopeModal2').modal('show');
            $('#modal_text').text("The work oder wasn't updated successfully ");
            console.log('An error occurred on : createProject' + xhr.error);
        }
    });


}



function transferWorkOrder(wOID, signumID, createdBy, stepName, userComments) {
    var wo = new Object();
    wo.woID = [$('#modal_input_woid').val()];
    wo.receiverID = $(C_SIGNUM_MODAL_ID).val();
    wo.senderID = signumGlobal;
    wo.stepName = $('#select_stepName').val();
    wo.userComments = $('#tWOcommentsBox').val();
    const C_WO_TRANSFER_MODAL_ID = '#modalTransfer';
    const C_WO_TRANSFER_SUCCESS_ID = '#edit_success';
    const C_EMP_CHANGE_STATUS_ID = '#employee_change_status';
    const C_WO_TRANSFER_ERROR_MSG = 'There was an error, your work order couldn\'t be transferred.';

    $.isf.ajax({
        url: `${service_java_URL}woManagement/transferWorkOrder`,
        context: this,
        crossdomain: true,
        processData: true,
        contentType: C_CONTENT_TYPE_APPLICATION_JSON,
        type: 'POST',
        data: JSON.stringify(wo),
        xhrFields: {
            withCredentials: false
        },
        success: function (data) {
            var convertedJson = JSON.parse(data);
            if (convertedJson.isValidationFailed) {
                refreshTable();
                $(C_WO_TRANSFER_MODAL_ID).modal('hide');
                $(C_WO_TRANSFER_SUCCESS_ID).modal('show');
                $(C_EMP_CHANGE_STATUS_ID).html(convertedJson.formMessages[0]);
            }
            else {
                $(C_WO_TRANSFER_MODAL_ID).modal('hide');
                $(C_WO_TRANSFER_SUCCESS_ID).modal('show');
                $(C_EMP_CHANGE_STATUS_ID).html(convertedJson.formErrors[0]);
            }
        },
        error: function (xhr, status, statusText) {
            $(C_WO_TRANSFER_MODAL_ID).modal('hide');
            $(C_WO_TRANSFER_SUCCESS_ID).modal('show');
            $(C_EMP_CHANGE_STATUS_ID).html(C_WO_TRANSFER_ERROR_MSG);
        }
    });
}

function modalClose() {


    $(C_WO_CREATE_MODAL_ID).modal('hide');
}


function gotoWorkOrder() {
    getServiceAreaDetails();
    getProjectName();
    getUser();
    getPriority();
    getNodeType();
    $(C_WO_CREATE_MODAL_ID).modal('show');
}

function getServiceAreaDetails() {
    $(C_PROD_AREA_ID)
        .find('option')
        .remove();
    $(C_PROD_AREA_ID).select2("val", "");
    $(C_PROD_AREA_ID).append('<option value="0"></option>');

    if (woProjectID === undefined || woProjectID === null || woProjectID === '' || isNaN(parseInt(woProjectID))) {
        console.log("Project id is wrong");
        pwIsf.alert({ msg: 'Wrong Project Id', type: 'error' });
    }
    else {
        $.isf.ajax({
            url: `${service_java_URL}activityMaster/getServiceAreaDetails?ProjectID=${parseInt(woProjectID)}`,
            success: function (data) {

                $.each(data, function (i, d) {
                    console.log("d.serviceAreaID $$$$$$$$$ " + d.serviceAreaID);
                    $('#select_product_area').append(`<option value="${d.serviceAreaID}">${d.serviceArea}</option>`);
                });
            },
            error: function (xhr, status, statusText) {
                console.log(`${C_SERAREA_ERR_MSG} ${xhr.error}`);
            }
        });
    }
}

function getDomain() {
    $(C_WO_DOMAIN_ID)
        .find('option')
        .remove();
    $(C_WO_DOMAIN_ID).select2("val", "");
    $(C_WO_DOMAIN_ID).append('<option value="0"></option>');
    var ServiceAreaID = $(C_PROD_AREA_ID).val();
    $.isf.ajax({

        url: `${service_java_URL}activityMaster/getDomainDetails?ProjectID=${woProjectID}&ServiceAreaID=${ServiceAreaID}`,
        success: function (data) {

            $.each(data, function (i, d) {
                $('#woc_select_domain').append(`<option value="${d.domainID}">${d.domain}</option>`);
            });
        },
        error: function (xhr, status, statusText) {
            console.log(`${C_DOM_ERR_MSG} ${xhr.error}`);
        }
    });
}

function getTechnologies() {

    $('#woc_select_technology')
        .find('option')
        .remove();
    $("#woc_select_technology").select2("val", "");
    $('#woc_select_technology').append('<option value="0"></option>');
    var domainID = $("#woc_select_domain").val();
    $.isf.ajax({
        url: `${service_java_URL}activityMaster/getTechnologyDetails?domainID=${domainID}&projectID=${woProjectID}`,
        success: function (data) {
            $.each(data, function (i, d) {
                $('#woc_select_technology').append(`<option value="${d.technologyID}">${d.technology}</option>`);
            });
        },
        error: function (xhr, status, statusText) {
            console.log(C_ERR_MSG);
        }
    });
}

function getActivitiesSub() {
    $('#woc_select_activitysub')
        .find('option')
        .remove();
    $("#woc_select_activitysub").select2("val", "");
    $('#woc_select_activitysub').append('<option value="0"></option>');

    $(C_SELECT_ACT_ID)
        .find('option')
        .remove();
    $(C_SELECT_ACT_ID).select2("val", "");
    $(C_SELECT_ACT_ID).append('<option value="0"></option>');

    var domainID = document.getElementById("woc_select_domain").value;
    var serviceAreaID = document.getElementById("select_product_area").value;
    var technologyID = document.getElementById("woc_select_technology").value;
    $.isf.ajax({
        url: `${service_java_URL}activityMaster/getActivityAndSubActivityDetails/${domainID}/${serviceAreaID}/${technologyID}`,
        success: function (data) {
            $.each(data, function (i, d) {
                $('#woc_select_activitysub').append(`<option value="${d.subActivityID}">${d.subActivityName}</option>`);
                $('#select_activity').append(`<option value="${d.activity}">${d.activity}</option>`);
            });
        },
        error: function (xhr, status, statusText) {
            console.log(C_ERR_MSG);
        }
    });
}

function getProjectName() {

    $(C_PROJ_NAME_ID)
        .find('option')
        .remove();
    $(C_PROJ_NAME_ID).select2("val", "");
    $(C_PROJ_NAME_ID).append('<option value="0"></option>');

    $.isf.ajax({

        url: `${service_java_URL}projectManagement/getProjectDetails?ProjectID=${woProjectID}`,
        success: function (data) {

            $(C_PROJ_NAME_ID).append(`<option value="${data.projectID}">${data.projectName}</option>`);

        },
        error: function (xhr, status, statusText) {
            console.log('An error occurred on getProjectName: ' + xhr.error);
        }
    })

}

function getPriority() {
    $(C_SELECT_PRIORITY_ID)
        .find('option')
        .remove();
    $(C_SELECT_PRIORITY_ID).select2("val", "");
    $(C_SELECT_PRIORITY_ID).append('<option value="0"></option>');

    $.isf.ajax({
        url: service_java_URL + "woManagement/getPriority ",
        success: function (data) {
            $.each(data, function (i, d) {
                $('#select_priority').append(`<option value="${d}">${d}</option>`);
            });
        },
        error: function (xhr, status, statusText) {
            console.log('An error occurred on getProjectName: ' + xhr.error);
        }
    });
}

function openModalperiodicity(value) {
    if (value === "Daily" || value === "Weekly") {
        document.getElementById("woModal1ContentDaily").style.display = "none";
        document.getElementById("woModal1ContentWeekly").style.display = "none";
        if (value === "Daily") {
            document.getElementById("woModal1ContentDaily").style.display = "inline";
        } else {
            document.getElementById("woModal1ContentWeekly").style.display = "inline";
        }
        $("#woModal1Title").html(value);
        $("#woModal1").modal("show");
    }
}

function getNodeType() {
    $(C_NODE_TYPE_SELECT_ID)
        .find('option')
        .remove();
    $(C_NODE_TYPE_SELECT_ID).select2("val", "");
    $(C_NODE_TYPE_SELECT_ID).append('<option value="0"></option>');
    if (woProjectID === undefined || woProjectID === null || woProjectID === '' || isNaN(parseInt(woProjectID))) {
        pwIsf.alert({ msg: "Project ID missing or invalid", type: "error" });
    }
    else {
        $.isf.ajax({
            url: `${service_java_URL}woManagement/getNodeType/${parseInt(woProjectID)}`,
            success: function (data) {
                $(C_NODE_TYPE_SELECT_ID).empty();
                $.each(data, function (i, d) {
                    $('#select_nodeType').append(`<option value="${d}">${d}</option>`);
                });
            },
            error: function (xhr, status, statusText) {
                console.log(`${C_DOM_ERR_MSG} ${xhr.error}`);
            }
        });
    }
}

function getNodeNames() {
    $('#woc_select_nodeName')
        .find('option')
        .remove();
    $("#woc_select_nodeName").select2("val", "");
    $('#woc_select_nodeName').append('<option value="0"></option>');
    var type = $(C_NODE_TYPE_SELECT_ID).val();
    $.isf.ajax({

        url: `${service_java_URL}woManagement/getNodeNames/${woProjectID}/${type}`,
        success: function (data) {
            $('#woc_select_nodeName').empty();
            for (var i = 0; i < data[0].lstNodeName.length; i++) {
                $('#woc_select_nodeName').append(`<option value="${data[0].lstNodeName[i]}">${data[0].lstNodeName[i]}</option>`);
            }
        },
        error: function (xhr, status, statusText) {
            console.log('An error occurred on getProjectName: ' + xhr.error);
        }
    });
}


function validations() {

    var OK = true;
    console.log("woc_select_domain " + $("#woc_select_domain option:selected").val());
    $('#woc_select_domain-Required').text("");
    if ($("#woc_select_domain option:selected").val() === "0") {
        $('#woc_select_domain-Required').text("Domain/Subdomain is required");
        OK = false;
    }
    console.log("woc_select_domain " + OK);

    $('#select_product_area-Required').text("");
    if ($("#select_product_area option:selected").val() === "0") {
        $('#select_product_area-Required').text("Service Area / Subservice area is required");
        OK = false;
    }

    console.log("select_product_area " + OK);

    $('#woc_select_technology-Required').text("");
    if ($("#woc_select_technology option:selected").val() === "0") {
        $('#woc_select_technology-Required').text("Technology is required");
        OK = false;
    }

    console.log("woc_select_technology " + OK);

    $('#select_activity-Required').text("");
    if ($("#select_activity option:selected").val() === "0") {
        $('#select_activity-Required').text("Activity is required");
        OK = false;
    }

    console.log("select_activity " + OK);


    $('#woc_select_activitysub-Required').text("");
    if ($("#woc_select_activitysub option:selected").val() === "0") {
        $('#woc_select_activitysub-Required').text("SubActivity is required");
        OK = false;
    }


    console.log("woc_select_activitysub " + OK);

    $('#select_nodeType-Required').text("");
    if ($("#select_nodeType option:selected").val() == null || $("#select_nodeType option:selected").val() === "0") {
        $('#select_nodeType-Required').text("Node Type is required");
        OK = false;
    }

    console.log("select_nodeType " + OK);

    $('#woc_select_nodeName-Required').text("");
    if ($("#woc_select_nodeName option:selected").val() == null || $("#woc_select_nodeName option:selected").val() === "0") {
        $('#woc_select_nodeName-Required').text("Node Names is required");
        OK = false;
    }

    console.log("woc_select_nodeName " + OK);

    $('#WO_name-Required').text("");
    if ($("#WO_name").val() == null || $("#WO_name").val() === "") {
        $('#WO_name-Required').text("WorkOrder Name name is required");
        OK = false;
    }

    console.log("WO_name " + OK);

    $('#select_periodicity-Required').text("");
    if ($("#select_periodicity option:selected").val() === "0") {
        $('#select_periodicity-Required').text("Periodicity is required");
        OK = false;
    }

    console.log("select_periodicity " + OK);

    $('#select_assign_to_user-Required').text("");
    if ($("#select_assign_to_user option:selected").val() === "0") {
        $('#select_assign_to_user-Required').text("Assign to User is required");
        OK = false;
    }

    console.log("select_assign_to_user " + OK);

    $('#select_priority-Required').text("");
    if ($("#select_priority option:selected").val() === "0") {
        $('#select_priority-Required').text("Priority is required");
        OK = false;
    }

    console.log("select_priority " + OK);

    $('#Start_Date-Required').text("");
    if ($("#Start_Date").val() == null || $("#Start_Date").val() === "") {
        $('#Start_Date-Required').text("Start date is required");
        OK = false;
    } else if ($("#select_periodicity option:selected").val() !== "Single") {
        if ($("#Start_Date").val() > $("#End_Date").val()) {//&& $("#End_Date").val() != ""
            $('#Start_Date-Required').text("Start Time is greater than End Date");
            OK = false;
        }
    } else if ($("#select_periodicity option:selected").val() === "Single") {
        if ($("#End_Date").val() !== "" && $("#Start_Date").val() > $("#End_Date").val()) {
            $('#Start_Date-Required').text("Start Time is greater than End Date");
            OK = false;
        }
    }

    console.log("select_periodicity " + OK);

    $('#start_time-Required').text("");
    if ($("#start_time option:selected").val() === "0") {
        $('#start_time-Required').text("Start Time is required");
        OK = false;
    }

    console.log("start_time " + OK);

    $('#End_Date-Required').text("");
    if ($("#End_Date").val() == null || $("#Start_Date").val() === "") {
        $('#End_Date-Required').text("End date is required");
        OK = false;
    }

    console.log("End_Date " + OK);

    console.log("STATUS "+OK);


    return OK;


}


function createWorkOrder() {

    if (validations()) {

        var periodicity = $("#select_periodicity").val();
        var periodicityDaily = null;
        var periodicityWeekly = null;
        var cadena = [];
        if (periodicity === "Daily") {
            $('#checkbox_daily:checked').each(
                function () {
                    cadena.push($(this).val());
                }
            );
            periodicityDaily = cadena.join(",")
        } else if (periodicity === "Weekly") {
            periodicityWeekly = $("input[name='radio_weekly']:checked").val();
            console.log(periodicityWeekly);
        }


        var WO = new Object();
        WO.projectID = woProjectID;
        WO.scopeID = scopeID;
        WO.wOPlanID = 1;
        WO.subActivityID = $("#woc_select_activitysub").val();
        WO.priority = $(C_SELECT_PRIORITY_ID).val();
        WO.periodicityDaily = periodicityDaily;
        WO.periodicityWeekly = periodicityWeekly;

        WO.startDate = $("#Start_Date").val();
        WO.startTime = $("#start_time").val();
        WO.endDate = $("#End_Date").val();

        WO.wOName = $("#WO_name").val();

        WO.signumID = $("#select_assign_to_user").val();
        WO.lastModifiedBy = signumGlobal;
        WO.createdBy = signumGlobal;
        var nodeNameArr = $("#woc_select_nodeName").val();
        var nodeName = nodeNameArr.join();
        console.log("nodeName " + nodeName);
        var nodeType = $("#select_nodeType").val();
        var listOfNodeJson = { "nodeNames": nodeName, "nodeType": nodeType }
        var listOfNode = [];
        listOfNode.push(listOfNodeJson);
        console.log(`${nodeType},${nodeName},${JSON.stringify(listOfNodeJson)},${listOfNode[0]},${listOfNode[1]}`);
        WO.listOfNode = listOfNode;
        console.log("The final result for call " + JSON.stringify(WO));
        $.isf.ajax({
            url: service_java_URL + "woManagement/createWorkOrderPlan",
            context: this,
            crossdomain: true,
            processData: true,
            contentType: 'application/json',
            type: 'POST',
            data: JSON.stringify(WO),
            xhrFields: {
                withCredentials: false
            },
            success: function (data) {
                alert("DATA SUCCESSFULLY SAVED");
                $(C_SELECT_ACT_ID).find('option').remove();
                $(C_SELECT_ACT_ID).select2("val", "");
                $('#woc_select_activitysub').find('option').remove();
                $("#woc_select_activitysub").select2("val", "");
                $("#select_periodicity").select2("val", 0);
                $("#start_time").select2("val", 0);
                $(C_NODE_TYPE_SELECT_ID).find('option').remove();
                $(C_NODE_TYPE_SELECT_ID).select2("val", "");
                $('#woc_select_nodeName').find('option').remove();
                $("#woc_select_nodeName").select2("val", "");
                $('#Start_Date').val("");
                $('#End_Date').val("");
                $('#WO_name').val("");
                $(C_WO_CREATE_MODAL_ID).modal('hide');
                search();
            },
            error: function (xhr, status, statusText) {
                alert("DATA CAN NOT BE SAVED");
                console.log('An error occurred on : createWO' + xhr.error);
            }
        });
    }
    else {
        alert("Fields Not properly filled");
    }
}
