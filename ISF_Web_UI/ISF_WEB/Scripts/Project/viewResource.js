
var minDateArr = [];
var maxDateArr = [];
var minDate;
var maxDate;
var signumArr = [];
var signumList;

function loadAllocatedResourcesView(ProjectID) {
    var status = "'Resource Allocated','Proposal Pending','Deployed','Closed'"; var signum = signumGlobal;

    if ($.fn.dataTable.isDataTable('#dataTable_viewResource')) {
        ot.destroy();
        $('#dataTable_viewResource tbody').empty();
    }
    pwIsf.addLayer({ text: "Please wait ..." });

    if (ProjectID == undefined || ProjectID == null || ProjectID == '' || isNaN(parseInt(ProjectID))) {
        console.log("Project id is wrong");
        pwIsf.alert({ msg: 'Wrong Project Id', type: 'error' });
    }
    else {
        $.isf.ajax({
            url: service_java_URL + "cRManagement/getPositionsAndProposedResources/" + status + "/" + signum + "/" + parseInt(ProjectID),
            success: function (data) {
                pwIsf.removeLayer();
                ConfigDataTableARView(2, data);
            },
            error: function (err) {
                pwIsf.removeLayer();
                $('#dataTable_viewResource tfoot').remove();
                $("#noDataLabel").css({ "display": "block" });
                console.log("No Data Found: " + err.responseText);
            }
        });
    }

}

$(document).off('click', '.delete-resource-rpid').on('click', '.delete-resource-rpid', function (event) {
    var _$ele = $(event.target);
    var _$tr = _$ele.closest("tr");
    var projectID = _$tr.children()[5].innerText;
    var _$rpid = _$tr.children()[3].innerText;

    if (confirm("Are you sure to delete RPID : " + _$rpid)) {

        $.isf.ajax({

            url: service_java_URL + "cRManagement/deleteRpID/" + _$rpid + "?signum=" + signumGlobal,
            type: 'GET',
            success: function (data) {
                $(_$tr).remove();
                alert("RPID successfully deleted!!!");
                //loadAllocatedResourcesView(ProjectID);
            },
            error: function (xhr, status, statusText) {
                alert("Operation Failed to perform!!!");
            },
            complete: function (xhr,status,statusText) {
                loadAllocatedResourcesView(projectID);
            }

        });
        _$tr.remove();
    }

});

function fetchMinMaxDates(data, RPID) {
    $.each(data, function (i, d) {

        if (RPID == d.resourcePositionID && d.isVisible == 1) {
            signumArr.push((d.signum));
            minDateArr.push(new Date(d.startDate));
            maxDateArr.push(new Date(d.endDate));
            maxDate = new Date(Math.max.apply(null, maxDateArr));
            minDate = new Date(Math.min.apply(null, minDateArr));
            maxDate = maxDate.getFullYear() + '-' + ((maxDate.getMonth() + 1) < 10 ? '0' + (maxDate.getMonth() + 1) : (maxDate.getMonth() + 1)) + '-' + (maxDate.getDate() < 10 ? '0' + maxDate.getDate() : maxDate.getDate());
            minDate = minDate.getFullYear() + '-' + ((minDate.getMonth() + 1) < 10 ? '0' + (minDate.getMonth() + 1) : (minDate.getMonth() + 1)) + '-' + (minDate.getDate() < 10 ? '0' + minDate.getDate() : minDate.getDate());
        }
    });
    maxDateArr = [];
    minDateArr = [];
}

function format(that, data) {
    var table = '<table id="collapseWEID" class="table table-bordered table-hover" style="border: 2px solid #334b75;">' +
        '<thead>' +
        '<tr style="background-color:azure;">' +
        '<th>Work Effort ID</th>' +
        '<th>Status</th>' +
        '<th>Employee Signum/Name</th>' +
        '<th>StartDate</th>' +
        '<th>EndDate</th>' +
        '<th>Hours</th>' +
        '<th>Manager Signum/Name</th>' +
        '</tr>' +
        '</thead>' +
        '<tbody>';
    $.each(data, function (i, data) {
        if (data.resourcePositionID == that.textContent && data.isVisible == 1) {

            if ((data.workEffortStatus).toLowerCase() == "deployed")
                table += '<tr style="background-color:#87CEFA">';
            else if ((data.workEffortStatus).toLowerCase() == "proposal pending")
                table += '<tr style="background-color:#98FB98">';
            else if ((data.workEffortStatus).toLowerCase() == "resource allocated")
                table += '<tr style="background-color:#EEE9E8">';
            table += '<td>' + data.workEffortID + '</td>';
            table += '<td>' + data.workEffortStatus + '</td>';
            if (data.signum == null)
                table += '<td>-/-</td>';
            else
                table += '<td><a href="#" style="color:blue;" onclick="getEmployeeDetailsBySignum(\'' + data.signum + '\');">' + data.signum + '/</a><br/>' + data.employeeName + '</td>';
            table += '<td>' + data.startDate + '</td>';
            table += '<td>' + data.endDate + '</td>';
            table += '<td>' + data.hours + '</td>';
            if (data.managerSignum == null)
                table += '<td>-/-</td>';
            else
                table += '<td><a href="#" style="color:blue;" onclick="getEmployeeDetailsBySignum(\'' + data.managerSignum + '\');">' + data.managerSignum + '/</a><br/>' + data.managerName + '</td>';
            table += '</tr>';
        }

    });
    table += '</tbody>' +
            '</table>';
    return table;
}

function createGridARView(data) {
    RPID = ""; duplicateRPID = ""; hasMultipleRPID = 0;
    $('#viewResource_tbody').html('');
    var flag = true;
    var td = "";

    var today = new Date();
    var dd = today.getDate();
    var mm = today.getMonth() + 1;
    var yyyy = today.getFullYear();
    today = yyyy + '-' + mm + '-' + dd;

    for (var i = 0; i < data.length; i++) {
        if (data[i].positionStatus == null) { data[i].positionStatus = '' };
        if (RPID != data[i].resourcePositionID && data[i].isVisible == 1) {
            RPID = data[i].resourcePositionID;
            fetchMinMaxDates(data, RPID);
            var signumList = "";
            for (j = 0; j < signumArr.length; j++) {
                signumList += signumArr[j] + "/";
            }
          var technologyHtml = getTechnologyHtml(data[i].technology);
            var tr = '<tr>';
            if (data[i].cRID == null)
                data[i].cRID = 0;
            if (parseInt(data[i].cRID) > 0 && (parseInt(data[i].isPendingCR) > 0)) {
                if ((data[i].positionStatus).toLowerCase() == "proposal pending") {
                    tr += '<td><input type="checkbox" style="display:none;" name="changeRequest" value="' + data[i].resourcePositionID + '"><span class="withdraw-req" data-CRRequesID=' + data[i].cRID + '><i style="cursor: pointer" class="fa fa-external-link" title="Withdraw CR"></span></i>&nbsp;&nbsp;<span class="delete-resource-rpid"><i style="cursor: pointer;" class="fa fa-trash" title="Delete CR"></span></i></td>';
                  
                }
                else {
                    tr += '<td><input type="checkbox" style="display:none;" name="changeRequest" value="' + data[i].resourcePositionID + '"><span class="withdraw-req" data-CRRequesID=' + data[i].cRID + '><i style="cursor: pointer" class="fa fa-external-link" title="Withdraw CR"></span></i>&nbsp;&nbsp;<span style="display:none;"><i style="cursor: pointer;" class="fa fa-trash" style="display:none;" title="Delete CR"></span></i></td>';
                }

            }
            else {
                if ((data[i].positionStatus).toLowerCase() == "proposal pending") {
                    tr += '<td><input type="checkbox" name="changeRequest" value="' + data[i].resourcePositionID + '"><span class="withdraw-req" style="display:none;" data-CRRequesID=' + data[i].cRID + '><i style="cursor: pointer;" class="fa fa-external-link" title="Withdraw CR"></span></i>&nbsp;&nbsp;<span class="delete-resource-rpid"><i style="cursor: pointer;" class="fa fa-trash" title="Delete CR"></span></i></td>'
                    
                }
                else {
                    if ((data[i].positionStatus).toLowerCase() == "position completion") {
                        tr += '<td></td>'
                    }
                    else {
                        tr += '<td><input type="checkbox" name="changeRequest" value="' + data[i].resourcePositionID + '"><span class="withdraw-req" style="display:none;" data-CRRequesID=' + data[i].cRID + '><i style="cursor: pointer;" class="fa fa-external-link" title="Withdraw CR"></span></i>&nbsp;&nbsp;<span style="display:none;"><i style="cursor: pointer;" class="fa fa-trash" style="display:none;")" title="Delete CR"></span></i></td>'
                    }
                }
            }
            tr += '<td><input type = "text" id = "comments' + i + '" /></td>'
            tr += '<td>' + data[i].resourceRequestID + '/' + data[i].remote_Onsite+'</td>';
            tr += '<td class="details-control">' + RPID + '</td>';
            tr += '<td>' + data[i].positionStatus + '</td>';
            tr += '<td>' + data[i].projectID + '</td>';
            if (data[i].signum == null)
                tr += '<td>-/-</td>';
            else {
                tr += '<td>' + signumArr + '</td>';
            }
            signumArr = [];

            //Trial for searching and sorting StartDate
            tr += '<td><span id="hiddenstartdate">' + minDate + '</span><input type="hidden" id="changeStartDate' + i + '" class="form-control dateForAllRows startdate" data-date-start-date="0d"  value="' + minDate + '" onchange="ChangeStartDate(changeStartDate' + i + ',changeEndDate' + i + ', hours' + i + ', fte' + i + ')"></td>';
            //enddate
            tr += '<td><span id="hiddenenddate">' + maxDate + '</span><input type="hidden" id="changeEndDate' + i + '" class="form-control dateForAllRows enddate" data-date-start-date="0d" value="' + maxDate + '" onchange="ChangeEndDate(changeStartDate' + i + ',changeEndDate' + i + ', hours' + i + ', fte' + i + ')"></td>';

            tr += '<td id=fte' + i + '>' + data[i].fte + '</td>';
            tr += '<td id=hours' + i + '>' + data[i].hours + '</td>';
            if (data[i].jobStage == null)
                tr += '<td>-</td>';
            else
                tr += '<td>' + data[i].jobStage + '</td>';
            if (data[i].managerSignum == null)
                tr += '<td>-/-</td>';
            else {
                tr += `<td><a href="#" style="color:blue;"
                onclick="getEmployeeDetailsBySignum(\'${data[i].managerSignum}\');">' ${data[i].signum} '</a><br/>' ${data[i].managerName} '</td>`;
            }
            tr += '<td>' + data[i].domain + '</td>';
            tr += '<td>' + data[i].subDomain + '</td>';
            tr += '<td>' + data[i].serviceArea + '</td>';
            tr += '<td>' + data[i].subServiceArea + '</td>';
            tr += `<td>${technologyHtml}</td>`;
            tr += '</tr>';

            $('#viewResource_tbody').append($(tr));
        }

        $("#changeStartDate" + i).datepicker({
            dateFormat: 'yy-mm-dd',
            autoclose: true
           
        });

        $("#changeEndDate" + i).datepicker({
            dateFormat: 'yy-mm-dd',
            autoclose: true
        });

        var defaultStartDate, defaultEndDate;
        $("#changeStartDate" + i).datepicker().on('show changeDate', function (e) {
            defaultStartDate = $("#" + this.id).val();
        });
        $("#changeStartDate" + i).datepicker().on('hide', function (e) {
            $("#" + this.id).val(defaultStartDate);
        });
        $("#changeEndDate" + i).datepicker().on('show changeDate', function (e) {
            defaultEndDate = $("#" + this.id).val();
        });
        $("#changeEndDate" + i).datepicker().on('hide', function (e) {
            $("#" + this.id).val(defaultEndDate);
        });

    }
    $('input').filter('.dateForAllRows').datepicker({
        format: 'yyyy-mm-dd',
        autoclose: true,

    });

}

function ConfigDataTableARView(targetCol, data) {

    createGridARView(data);

    ot = $("#dataTable_viewResource").DataTable({
        "displayLength": 10,
        "searching": true,
        "autoWidth": true,
        "destroy": true,
        colReorder: true,
        responsive: true,
        order: [1],
        dom: 'Bfrtip',
        buttons: [
         'colvis', 'excel'
        ],
        initComplete: function () {

            $('#dataTable_viewResource tfoot th').each(function (i) {
                var title = $('#dataTable_viewResource thead th').eq($(this).index()).text();
                if ((title != "Select") && (title != "Comments"))
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

        },
        "drawCallback": function (settings) {
           var api = this.api();
            var rows = api.rows({ page: 'current' }).nodes();
            var last = null;
            $('[data-toggle="tooltip"]').tooltip();

        },
        "rowCallback": function (row, data, index) {
            if (data[3].toLowerCase() == "deployed") {
                $('td', row).css('background-color', '#87CEFA');
            }
            else if (data[3].toLowerCase() == "proposal pending") {
                $('td', row).css('background-color', '#98FB98');
            }
            else if (data[3].toLowerCase() == "proposed") {
                $('td', row).css('background-color', '#ffd995');
            }
            else if (data[3].toLowerCase() == "rejected") {
                $('td', row).css('background-color', 'RED');
            }
            else if (data[3].toLowerCase() == "resource allocated") {
                $('td', row).css('background-color', '#EEE9E8');
            }
        }
    });
    ot.columns([11, 12, 13, 14, 15, 16]).visible(false, false);
    ot.columns.adjust().draw(false); // adjust column sizing and redraw
    $('#dataTable_viewResource tfoot').insertAfter($('#dataTable_viewResource thead'));

    registerAddClick(data);

}

function registerAddClick(data) {
    $(document).find('#dataTable_viewResource').off('click').on('click', 'td.details-control', function (e) {
        var tr = $(this).closest('tr');
        var row = ot.row(tr);

        if (row.child.isShown()) {
            // This row is already open - close it
            row.child.hide();
            tr.removeClass('shown');
        }
        else {
            // Open this row    
            row.child(format(this, data)).show();
            tr.addClass('shown');
        }
    });
}

function changeRequest(event) {
    //let service_java_URL = "http://169.144.28.228:8080/isf-rest-server-java/";
    var CRRequest = new Object();
    var rpID = ''; var startDate = ''; var endDate = ''; var comments = ''; var status = ''; var hours = 0; var reason = $('#crSummaryReason option:selected').val();
    $('input[name="changeRequest"]:checked').each(function () {
        rpID = this.value;
        prevStartDate = $(this).closest('tr').find('td:eq(7)').find("#hiddenstartdate")[0].textContent;
        prevEndDate = $(this).closest('tr').find('td:eq(8)').find("#hiddenenddate")[0].textContent;
        startDate = $(this).closest('tr').find('td:eq(7)').find("input")[0].value;
        endDate = $(this).closest('tr').find('td:eq(8)').find("input")[0].value;
        comments = $(this).closest('tr').find('td:eq(1)').find("input").val();
        status = $(this).closest('tr').find('td:eq(4)')[0].innerHTML;
        projectID = $(this).closest('tr').find('td:eq(5)')[0].innerHTML;
        hours = $(this).closest('tr').find('td:eq(10)')[0].innerHTML;
        currentCRTr = $(this).parents('td');
        var currentRowStartDate = $(this).parents('tr').find("td:eq(7)").find("input");
        var currentRowEndDate = $(this).parents('tr').find("td:eq(8)").find("input");
        var checkStartEndDate = (new Date(endDate) - new Date(startDate));
        var projectID = $(this).closest('tr').find('td:eq(5)')[0].innerHTML;
        CRRequest = [
                    {
                        "rpID": rpID,
                        "startDate": startDate + " 00:00:00",
                        "endDate": endDate + " 00:00:00",
                        "comments": signumGlobal + ":" + comments,
                        "loggedInSignum": signumGlobal,
                        "actionType": "pm",
                       
                    }
        ];
        

        var reasonCr = new Object();

        reasonCr.raiseCRMannagmentModel = CRRequest;
        reasonCr.reason = reason;
        
         
       
        if (status.toLowerCase() == "proposal pending") {
            if (checkStartEndDate < 0) {
                pwIsf.alert({ msg: 'End date cannot be less than the Start Date', type: 'warning' });

            }
            else if (reason.length == 0) {
                pwIsf.alert({ msg: 'Please Select a reason for change request.', type: 'warning' });
            }
            //else if ((prevStartDate == startDate) && (prevEndDate == endDate)) {
            //    pwIsf.alert({ msg: 'Please change the end date or start date for change request', type: 'warning' });
            //}
            else {


                $.isf.ajax({

                    url: service_java_URL + "cRManagement/updateRPDates/" + rpID + "/" + startDate + "/" + endDate + "/" + parseInt(hours) + "/" + signumGlobal + "/" + reason,
                    method: "POST",
                    success: function () {
                        alert("Successfully updated!!");
                        //loadAllocatedResourcesView(projectID);
                    },
                    error: function (xhr, status, statusText) {
                        alert("Operation Failed to perform!!!");
                    },
                    complete: function (xhr,status,statusText) {
                        loadAllocatedResourcesView(projectID);
                    }

                })
            }
        }
        else if (reason.length == 0) {
            pwIsf.alert({ msg: 'Please Select a reason for ramp down/ramp up.', type: 'warning' });

        }
        else if ((prevStartDate == startDate) && (prevEndDate == endDate)) {
            pwIsf.alert({ msg: 'Please change the end date or start date for change request', type: 'warning' });
        }

        else {
            //$('#reasondropdown').show();
                $.isf.ajax({

                    url: service_java_URL + "cRManagement/raiseChangeManagment",
                    method: "POST",
                    contentType: "application/json; charset=utf-8",
                    data: JSON.stringify(reasonCr),
                    dataType: 'html',
                    success: function (data) {
                        console.log("success");
                        currentRowStartDate.attr("disabled", "false");
                        currentRowEndDate.attr("disabled", "false");
                        currentCRTr.find("input").hide();
                        currentCRTr.find("span.withdraw-req").show();
                        //loadAllocatedResourcesView(projectID);
                        pwIsf.alert({ msg: 'Change Request has been raised', type: 'success' });
                    },
                    error: function (xhr, status, statusText) {
                        console.log('An error occurred : ' + xhr.responseText);
                        pwIsf.alert({ msg: 'An error occured while raising Change Request', type: 'error' });
                    },
                    complete: function(xhr,status,statusText) {
                        loadAllocatedResourcesView(projectID);
                    }
                });
            }
      

    });

}

$(document).on('click', '#dataTable_viewResource tr', function (event) {
    if (event.target.type == 'checkbox') {
        if (event.target.checked) {

            if ($(this).find("td:eq(4)")[0].innerHTML === "Deployed") {
                $(this).find("td:eq(8)").find('input')[0].type = "text";
                $(this).find("td:eq(8)").find('span')[0].hidden = true;

            }
            else if ($(this).find("td:eq(4)")[0].innerHTML === "Position Completion") {
                $(this).find("td:eq(7)").find('input')[0].type = "hidden";
                $(this).find("td:eq(8)").find('input')[0].type = "hidden";

            }
            else {
                $(this).find("td:eq(7)").find('input')[0].type = "text";
                $(this).find("td:eq(7)").find('span')[0].hidden = true;
                $(this).find("td:eq(8)").find('input')[0].type = "text";
                $(this).find("td:eq(8)").find('span')[0].hidden = true;
            }

        }
        else {
            if ($(this).find("td:eq(4)")[0].innerHTML === "Deployed") {
                $(this).find("td:eq(8)").find('input')[0].type = "hidden";
                $(this).find("td:eq(8)").find('span')[0].hidden = false;

            }
            else {
                $(this).find("td:eq(7)").find('input')[0].type = "hidden";    //show span only; hide input date
                $(this).find("td:eq(7)").find('span')[0].hidden = false;
                $(this).find("td:eq(8)").find('input')[0].type = "hidden";    //show span only; hide input date
                $(this).find("td:eq(8)").find('span')[0].hidden = false;
            }
        }
    }

})

$(document).on('click', '.withdraw-req', function () {
    var changeRequestID = $(this).data().crrequesid;
    var $_tr = $(this).closest("tr");
    var currentRowStartDate = $(this).parents('tr').find("td:eq(7)").find("input");
    var currentRowEndDate = $(this).parents('tr').find("td:eq(8)").find("input");
    currentTr = $(this).parents('td');
    $.isf.ajax({
        url: service_java_URL + "cRManagement/updateCrStatus/" + changeRequestID,
        success: function (data) {
            if (data) {
                var status = "CLOSED";
                $('input').filter('.dateForAllRows').datepicker({
                    format: 'yyyy-mm-dd',
                    autoclose: true,

                });
                currentTr.find("input").show();
                currentTr.find("span").hide();
            }
            else {

            }
        }
    })
});

function modalSummary() {
   
    selectReasonForCr();
    var list = new Object();
    var listArray = [];
    var showRejectionReason = false;
    $("input[name='changeRequest']:checked").each(function () {

        var rpID = ''; var empName = ''; var FTE = ' '; var hrs = ''; var status = '';
        var updatedStartDate = ''; var updatedEndDate = '';
        rpID = this.value;
        oldStartDate = $(this).closest('tr').find('td:eq(7)').find("#hiddenstartdate")[0].textContent;
        oldEndDate = $(this).closest('tr').find('td:eq(8)').find("#hiddenenddate")[0].textContent;
        updatedStartDate = $(this).closest('tr').find('td:eq(7)').find("input")[0].value;
        updatedEndDate = $(this).closest('tr').find('td:eq(8)').find("input")[0].value;
        empName = $(this).closest('tr').find('td:eq(6)')[0].innerHTML;
        FTE = $(this).closest('tr').find('td:eq(9)')[0].innerHTML;
        hrs = $(this).closest('tr').find('td:eq(10)')[0].innerHTML;
        status = $(this).closest('tr').find('td:eq(4)')[0].innerHTML;

        if (status.toLowerCase() == "deployed" || status.toLowerCase() == "resource allocated" || status.toLowerCase() == "proposal pending") {
            showRejectionReason = true;
            
        }
        


        list =
                  {
                      "rpID": rpID,
                      "oldstartDate": oldStartDate,
                      "oldendDate": oldEndDate,
                      "updatedStartDate": updatedStartDate,
                      "updatedEndDate": updatedEndDate,
                      "empName": empName,
                      "FTE": FTE,
                      "hours": hrs
                  };
        listArray.push(list);
    });
    if (showRejectionReason) {
        $('#reasondropdown').show();
    } else {
        $('#reasondropdown').hide();
    }
    $('#summarytable').DataTable({
        "data": listArray,
        "destroy": true,
        searching: true,
        order: [1],
        "columns": [

            {
                "title": "RPID",
                "data": "rpID"
            },
            {
                "title": "Employee Name",
                "data": "empName"
            },
            {
                "title": "Prev StartDate",
                "data": "oldstartDate"
            },
            {
                "title": "Prev EndDate",
                "data": "oldendDate"
            },
            {
                "title": "Updated StartDate",
                "data": "updatedStartDate"
            },
            {
                "title": "Updated EndDate",
                "data": "updatedEndDate"
            },
            {
                "title": "FTE%",
                "data": "FTE"
            },
            {
                "title": "Hours",
                "data": "hours"
            },
        ]
    });
}

function ChangeEndDate(startDateID, endDateID, hoursID, fteID) {

    if ($(endDateID).val() < $(startDateID).val()) { $(endDateID).val(""); }
    else {
        var MS_PER_DAY = 24 * 60 * 60 * 1000;
        var d1 = new Date($(startDateID).val());
        var d2 = $(endDateID).datepicker('getDate');

        var work = (Math.ceil((d2 - d1) / MS_PER_DAY)) + 1;
        if (work >= 0) {
            var d = d1;
            while (d <= d2) {
                if ((d.getDay() || 7) > 5) { // Sat/Sun
                    work--;
                }
                d.setDate(d.getDate() + 1);
            }
            var horas = work * 8;
            var fteVal = fteID.textContent;
            var hours = (fteVal / 100) * horas;
            $(hoursID)[0].innerHTML = hours;
        } else {

        }
    }
}

function ChangeStartDate(startDateID, endDateID, hoursID, fteID) {

    if ($(endDateID).value != "") {
        if ($(endDateID).val() < $(startDateID).val()) { $(endDateID).val(""); }
        else {
            var MS_PER_DAY = 24 * 60 * 60 * 1000;
            var d1 = $(startDateID).datepicker('getDate');
            var d2 = $(endDateID).datepicker('getDate');
            var work = (Math.floor((d2 - d1) / MS_PER_DAY)) + 1;
            if (work >= 0) {
                var d = d1;
                while (d <= d2) {
                    if ((d.getDay() || 7) > 5) { // Sat/Sun
                        work--;
                    }
                    d.setDate(d.getDate() + 1);
                }
                var horas = work * 8;
                var fteVal = fteID.textContent;
                var hours = (fteVal / 100) * horas;
                $(hoursID)[0].innerHTML = hours;
            }
            else {

            }
        }
    }
}

function selectReasonForCr() {

    $('#crSummaryReason').empty();
    $.isf.ajax({
        url: service_java_URL + "cRManagement/getReason",
        success: function (data) {
            $('#crSummaryReason').append('<option value=""></option>');
            $.each(data, function (i, d) {
                $('#crSummaryReason').append('<option value="' + d.Reason + '">' + d.Reason + '</option>');

            })
        },
        error: function (xhr, status, statusText) {
            console.log('An error occurred on get Reasons');
        }
    });

}

function getTechnologyHtml(arrTechnology) {
    arrTechnology = arrTechnology.join(', ');
    return `<div class="truncateTableFields" data-toggle="tooltip" data-placement="bottom" title=\'${arrTechnology}\'>
        <span>${arrTechnology}</span>
        </div>`;
}
