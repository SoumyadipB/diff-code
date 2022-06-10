var ot;
$(document).ready(function () {
    loadModal();
});

function loadModal() {
    var status = "'Proposed'";
    var signum = signumGlobal;
    $.isf.ajax({
        url: `${service_java_URL}cRManagement/getPositionsAndProposedResources/${status}/${signum}/0`,
        async: false,
        success: function (data) {
            $("#div_loader").hide();
            $("#div_ARbtns").show();
            ConfigDataTableRA(data);
        },
        error: function (err) {
            $("#div_loader").hide();
            $("#dataTable_allocatedResource tfoot").remove();
            $("#noDataLabel").css({"display":"block"});
            console.log("An Error occured during position and allocated resource");
        }
    });
}

function ConfigDataTableRA(data) {

    if ($.fn.dataTable.isDataTable('#dataTable_allocatedResource')) {
        ot.destroy();
        $('#dataTable_allocatedResource').empty();
    }

    //create html with data
    createGridRA(data);

    if ($.fn.dataTable.isDataTable('#dataTable_allocatedResource')) {
        ot = $('#dataTable_allocatedResource').DataTable();
    }
    else {
        ot = $("#dataTable_allocatedResource").DataTable({
            "displayLength": 10,
            "searching": true,
            responsive: true,
            colReorder: true,
            order: [1],
            dom: 'Bfrtip',
            "drawCallback": function (settings) {
                $('body').tooltip({
                    selector: '.aqua-tooltip',
                    placement: 'top'
                });
            },
            columnDefs: [{
                "targets": [0,1],
                "orderable": false
            }],
            buttons: [{
                extend: 'colvis',
                columns: ':gt(0)'
            },
            'excel'],
            initComplete: function () {
                $('#dataTable_allocatedResource tfoot th').each(function (i) {
                    var title = $('#dataTable_allocatedResource thead th').eq($(this).index()).text();
                    if ((title != "Select") && (title != "Comments"))
                        $(this).html(`<input type="text"  class="form-control" style="font-size:12px;"  placeholder="Search ${title}" data-index="${i}" />`);
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
                $("#dataTable_allocatedResource").wrap('<div class="tabWrap" style="overflow:auto;width:100%;"></div>');
            }
        });
        ot.columns([11,12,13,14,15]).visible(false, false);
       ot.columns.adjust().draw(false); // adjust column sizing and redraw
        $('#dataTable_allocatedResource tfoot').insertAfter($('#dataTable_allocatedResource thead'));
    }
}

function createGridRA(data) {
    $("#dataTable_allocatedResource tbody").html("");
    $.each(data, function (i, d) {
        if (d.isVisible) {
            $("#aprove_projectId").val(d.projectID);
            $("#reject_projectId").val(d.projectID);

            var tr = `<tr>
            <td>
            <label><input id="approveResource" type="checkbox" name="approveResource" class="checkBoxClass"
            value="${d.resourcePositionID}"></label>
            </td>
            <td>
            <textarea id="commentsApproval${d.resourcePositionID}" disabled></textarea>
            </td>
            <td>${d.projectID}</td>
            <td>${d.resourcePositionID}</td>
            <td data-weid=${d.workEffortID}>${d.workEffortID}</td>`;
            if (d.demandType == null) {
                var demandType = " ";
                tr += `<td>${demandType}</td>`;
            }
            else {
                tr += `<td>${d.demandType}</td>`;
            }
            if (d.signum == null) {
                tr = `${tr}<td>-/-</td>`;
            }
            else {
                tr += '<td><a href="#" style="color:blue;" onclick="getEmployeeDetailsBySignum(\'' + d.signum + '\');">' + d.signum + '/</a><br/>' + d.employeeName + '</td>';
            }

            tr = `${tr}
            <td>${d.startDate}</td>
            <td>${d.positionStartDate}</td>
            <td>${d.endDate}</td>
            <td>${d.positionEndDate}</td>
            <td>${d.fte}</td>
            <td>${d.jobStage}</td>`;

            if (d.managerSignum == null) {
                tr = `${tr}<td>-/-</td>`;
            }
            else {
                tr = `${tr}<td><a href="#" style="color:blue;"
                onclick="getEmployeeDetailsBySignum(${d.managerSignum});">${d.managerSignum}</a><br/>${d.managerName}</td>`;
            }

            const technologyHtml = getTechnologyHtml(d.technology);

            tr = `${tr}
            <td>${d.domain}</td>
            <td>${d.subDomain}</td>
            <td>${d.serviceArea}</td>
            <td>${d.subServiceArea}</td>
            <td>${technologyHtml}</td>
			<td>${d.commentsByFm}</td>
			</tr>`;

            $("#dataTable_allocatedResource tbody").append(tr);

            $(".checkBoxClass").on('change', function () {
                if (!$(this).prop("checked")) {
                    $(this).closest('tr').find('textarea').prop("disabled", true);
                }
                else {
                    $(this).closest('tr').find('textarea').prop("disabled", false);
                }
            });
        }
        //Checking all boxes
        $("#resourceCheckAll").click(function () {
            $(".checkBoxClass").prop('checked', $(this).prop('checked'));
        });

        $(".checkBoxClass").on('change', function () {
            if (!$(this).prop("checked")) {
                $("#resourceCheckAll").prop("checked", false);
            }
        });
    });
}

function updateResourceApproval(flag)
{
    let projectID = "";
    if (flag) {
        projectID = $("#aprove_projectId").val();
    }
    else {
         projectID = $("#reject_projectId").val();
    }
    let WEID = "", positionStartDate, positionEndDate, startDate, endDate;
    let rpID = "", status = "";
    const project = new Object();
    let isError = false;

    if ( $('input:checkbox[id="approveResource"]:checked').length == 0 )
    {
        pwIsf.alert({ msg: "Please select a resource to take action", type: "warning" });
        isError = true;
    }
    else if (!flag) {
        var resPosId = 0;
        $('input:checkbox[id="approveResource"]:checked').each(function () {
            resPosId = $(this).val();
            if ($(`#commentsApproval${resPosId}`).val() === "") {
                alert(`Please fill the reason to reject the resource for ResourcePositionId : ${resPosId}`);
                isError = true;
            }
        });
    }

    if (!isError) {
        pwIsf.addLayer({ text: C_PLEASE_WAIT });
        $('input[name="approveResource"]:checked').each(function () {
            rpID = this.value;
            WEID = $(this).closest('tr').find('td:eq(4)').text();
            const positionStartDate = $(this).closest('tr').find('td:eq(8)').text();
            const positionEndDate = $(this).closest('tr').find('td:eq(10)').text();
            const startDate = $(this).closest('tr').find('td:eq(7)').text();
            const endDate = $(this).closest('tr').find('td:eq(9)').text();
            if (flag) {
                status = "Resource Allocated";
            }
            else {
                status = "Proposal Pending";
            }
            project.weid = WEID;
            project.rpID = rpID;
            project.allocatedStatus = status;
            project.loggedInUser = signumGlobal;
            project.comments = `${signumGlobal} : ${$('#commentsApproval' + rpID).val()}`;
            project.positionStartDate = positionStartDate;
            project.positionEndDate = positionEndDate;
            project.startDate = startDate;
            project.endDate = endDate;

            updateAllocatedResourcesAPICall(flag, project, projectID);
            
        });
    }
}

function updateAllocatedResourcesAPICall(flag, projectDetails, projectID) {
    $.isf.ajax({
        url: `${service_java_URL}activityMaster/updateAllocatedResources?projectId=${projectID}`,
        context: this,
        crossdomain: true,
        processData: true,
        contentType: C_CONTENT_TYPE_APPLICATION_JSON,
        type: C_API_POST_REQUEST_TYPE,
        data: JSON.stringify([projectDetails]),
        xhrFields: {
            withCredentials: false
        },
        success: function (data) {
            pwIsf.removeLayer();
            if (flag) {
                pwIsf.alert({ msg: "Your resources  have been approved", type: C_SUCCESS });
            }
            else if (!flag) {
                pwIsf.alert({ msg: "Your resources have been rejected", type: C_SUCCESS });
            }
            location.reload();
        },
        error: function (xhr, stat, statusText) {
            console.log(`${C_COMMON_ERROR_MSG} : ${xhr.responseText}`);
        }
    });
}

function getTechnologyHtml(arrTechnology) {
    arrTechnology = arrTechnology.join(', ');
    return `<div class="truncateTableFields aqua-tooltip" data-toggle="tooltip" data-original-title=\'${arrTechnology}\'>
        <span>${arrTechnology} </span>
        </div>`;
}


