var login_signum = signumGlobal;
var clean_date_resign = 0;
var role = JSON.parse(ActiveProfileSession).role;

$(document).ready(function () {
   
    if (role !== "Application Admin" && role !== "ISF Access Manager")
    {
        getEmployeeDetailsByManager();

    }
        
   
   
   
    $('#leaveModal').on('show.bs.modal', function () {
       // $(this).find('.modal').css({
       //     'overflow': 'hidden',
       //     'overflow-y': 'hidden'
           
       // //    //width: 'auto', //probably not needed
       // //    //height: 'auto', //probably not needed
       // //    'max-width': '60%',
       // //    'margin': 'auto'
       //});
    });

    $('#leave_type').select2({ placeholder: "Select Leave Type" });
    $("#date_resign").datepicker({
        dateFormat: "yy-mm-dd"
    });
    $("#date_release").datepicker({
        dateFormat: "yy-mm-dd"
    });
    $('#shiftModal').on('show.bs.modal', function () {
        $(this).find('.modal-content').css({
            width: 'auto', //probably not needed
            height: 'auto', //probably not needed
            'max-width': '50%',
            'margin': 'auto'
        });
    });



    var today = formatDate(new Date());
    var endDate90 = formatDate(new Date().setDate(new Date().getDate() + 90))
    $("#approveUserStartDate").val(today);
    $("[id$=approveUseStartDate]").attr('min', today);
    $("#approveUserEndDate").val(endDate90);
    $("#accessStartDateUser").val(today);
    $("#accessEndDateUser").val(endDate90);

});

/*---------------Format Date in yyyy-mm-dd format-----------------------*/
function formatDate(date) {
    var d = new Date(date),
        month = '' + (d.getMonth() + 1),
        day = '' + (d.getDate()),
        year = d.getFullYear();

    if (month.length < 2) month = '0' + month;
    if (day.length < 2) day = '0' + day;

    return [year, month, day].join('-');
}



//Update shift of multiple employees
function multipleSignumShiftUpdate() {

    pwIsf.addLayer({ text: "Please wait ..." });
    selectedSignums = getSignumsForBulkShiftUpdate();
    signumArrTeamExplorer = selectedSignums;
    if (selectedSignums != 0) {
        pwIsf.confirm({
            title: 'Multiple Shift Time Update', msg: "Do you want to update the shift time of the selected employees?",
            'buttons': {
                'Yes': {
                    'action': function () {
                        pwIsf.removeLayer();
                        getTimeZones();
                        $('#shiftModal').modal('show');
                        $('#shiftTitle').hide();
                        $('#timeZoneHome').hide();
                        $('#currentShiftDiv').hide();
                        $('#shiftModal .nav-tabs li a[href="#shiftTimingAdd_tab_1"]').tab('show');
                        $('#savedTimingsTab').hide();
                        $('#labelCurrentShift').hide();
                        $("#singleOrMultiple").val("multiple");


                    }
                },
                'No': {
                    'action': function () {
                        pwIsf.removeLayer();
                    }
                },

            }
        });





    }
    else {
        pwIsf.alert({ msg: "Please select at least one employee!" });
        pwIsf.removeLayer();
    }

}

//Get the selected employees for multiple shift update
function getSignumsForBulkShiftUpdate() {
    var chkArray = [];
    $(".checkBoxClassSignum:checked").each(function () {
        parseInt(chkArray.push($(this).val()));
    });
    var selected;
    selected = chkArray.join(',');
    if (selected.length > 1) {
        return chkArray;
    } else {
        return 0;
    }
}

$(function () {
    var today = new Date();
    var lastDate = new Date(today.getFullYear(), today.getMonth() + 3,
        today.getDate());
    var previousDate = new Date(today.getFullYear(), today.getMonth() - 3,
        today.getDate());

    $("[id$=date_resign]").datepicker({

        startDate: previousDate, // If you need to disable past dates
        autoClose: true,
        viewStart: 0,
        weekStart: 1,

    });

});

$(function () {
    var today = new Date();
    var lastDate = new Date(today.getFullYear(), today.getMonth() + 3,
        today.getDate());
    var previousDate = new Date(today.getFullYear(), today.getMonth() - 3,
        today.getDate());


    $("[id$=date_release]").datepicker({

        autoClose: true,
        viewStart: 0,
        weekStart: 1,
        endDate: lastDate

    });
});
function getLeaveType() {
    //clearShiftModal();

    $.isf.ajax({
        url: service_java_URL + "activityMaster/getEssLeave",
        context: this,
        crossdomain: true,
        dataType: 'json',
        contentType: 'application/json',
        type: 'GET',
        xhrFields: {
            withCredentials: false
        },
        success: function (data) {
            var currentDate = new Date();
            $("#leave_type").empty();
            $.each(data, function (i, d) {
                $("#leave_type").append('<option value="' + d.LeaveType + '">' + d.LeaveType + '</option>');
            });

        },
        error: function (xhr, status, statusText) {
            console.log('An error occurred');
            //alert('Sorry, an error occurred. Please try reloading the page. \n\nIf that doesn\'t work please send us the following error message, using the contact us page. \n\n' + this.url + ' ' + xhr.status + ' ' + xhr.statusText);

        }
    });
}

function getEmployeeDetailsByManager() {

    if ($.fn.dataTable.isDataTable('#table_team_explorer')) {
        oTable.destroy();
        $('#table_team_explorer').empty();
    }

    pwIsf.addLayer({ text: 'Please wait ...' });
    $.isf.ajax({
        url: service_java_URL + "activityMaster/getEmployeesByManager/" + login_signum,
        success: function (data) {
            pwIsf.removeLayer();
            if (data.isValidationFailed == false) {
                $.each(data.responseData, function (i, d) {

                    if (d.status.toLowerCase() == 'notice period') {
                        d.actionIcon = "<i style='cursor: pointer;'  class='md md-done' \"></i>&nbsp;&nbsp;" +
                            "<i style='cursor: pointer;' class='fa fa-user' \"></i>&nbsp;&nbsp;" +
                            "<i style='cursor:pointer;' title='Update Leave Plan' class='fa fa-sign-out' data-toggle='modal'  \></i>&nbsp;&nbsp;" +
                            "<i style='cursor:pointer;' title='Update Shift Time' class='fa fa-clock-o' \></i>&nbsp;&nbsp;";
                    } else if (d.status.toLowerCase() == 'active') {
                        d.actionIcon = "<i style='cursor: pointer;' title='Change Employee Status' class='fa fa-edit' \"></i> &nbsp;&nbsp; " +
                            "<i style='cursor: pointer;' title='Employee Details' class='fa fa-user' \></i> &nbsp;&nbsp;" +
                            "<i style='cursor:pointer;' title='Update Leave Plan' class='fa fa-sign-out' data-toggle='modal' \></i>&nbsp;&nbsp;" +
                            "<i style='cursor:pointer;' title='Update Shift Time' class='fa fa-clock-o' \></i>&nbsp;&nbsp;";
                    } else if (d.status.toLowerCase() == 'resigned' || d.status.toLowerCase() == 'inactive') {
                        d.actionIcon = "<i style='cursor: pointer;'  \"></i> &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; " +
                            "<i style='cursor: pointer;' class='fa fa-user' \"></i>";
                    }
                    else {
                        d.actionIcon = "&nbsp;";
                    }

                })

                $('#teamExplorerList_tbody').html('');
                $("#table_team_explorer").append($('<tfoot><tr><th></th><th></th><th>Signum</th><th>Name</th><th>Status</th><th>Cost Center</th><th>Building</th><th>Location</th></tr></tfoot>'));
                var oTable = $('#table_team_explorer').on('page.dt', function () { $("#checkAllEmployee").prop('checked', false); $(".checkBoxClassSignum").prop('checked', false) }).DataTable({
                    "order": [[1, "asc"]],
                    searching: true,
                    colReorder: true,
                    "pageLength": 10,
                    "paging": true,
                    dom: 'Bfrtip',
                    buttons: ['colvis',
                        {
                            extend: 'excel',
                            text: 'Export',
                            filename: function () {
                                var d = (new Date()).toLocaleDateString();
                                return 'Team Details_' + login_signum + "_" + d;
                            },
                        },
                        {
                            text: 'Update Shift Time',
                            action: function (e, dt, node, config) {
                                multipleSignumShiftUpdate();
                            }
                        }
                    ],
                    "data": data.responseData,
                    "destroy": true,
                    "columns": [
                        {
                            "title": '<input type="checkbox" title="Select All Employees"  id="checkAllEmployee">',
                            "orderable": false,
                            "searchable": false,
                            "targets": 'no-sort',
                            "defaultContent": "",
                            "render": function (data, type, row, meta) {
                                if (row.status.toLowerCase() == 'active')
                                    return '<div style="text-align:center"><input class="checkBoxClassSignum" type="checkbox" value="' + row.signum + '"></div>'
                                else
                                    return '';
                            }
                        },
                        {
                            "title": "Actions",
                            "targets": 'no-sort',
                            "orderable": false,
                            "searchable": false,
                            "data": "actionIcon"

                        },
                        {
                            "title": "Signum",
                            "data": "signum"
                        },
                        {
                            "title": "Name",
                            "data": "employeeName"
                        },

                        {
                            "title": "Status",
                            "data": "status"
                        },
                        {
                            "title": "Cost Center",
                            "data": "costCenter"
                        },
                        {
                            "title": "Building",
                            "data": "hrLocation"
                        },

                        {
                            "title": "Location",
                            "data": "city",
                            // "visible": true
                        }
                        //{
                        //    "title": "ContactNumber",
                        //    "data": "contactNumber",
                        //    // "visible": true
                        //},

                    ],
                    initComplete: function () {
                        $('#table_team_explorer tfoot th').each(function (i) {
                            var title = $('#table_team_explorer thead th').eq($(this).index()).text();
                            if (title != "Actions" && title != "")
                                $(this).html('<input type="text" class="form-control" style="font-size:12px;"  placeholder="Search ' + title + '" data-index="' + i + '" />');
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

                        $('#table_team_explorer tbody').on('click', 'i.md-done', function () {
                            var data = oTable.row($(this).parents('tr')).data();
                            changeEmployeeStatusToActive(data.signum);
                        });

                        $('#table_team_explorer tbody').on('click', 'i.fa-edit', function () {
                            var data = oTable.row($(this).parents('tr')).data();
                            getEmployeeDetailsBySignum(data.signum, data.status);
                        });

                        $('#table_team_explorer tbody').on('click', 'i.fa-user', function () {
                            var data = oTable.row($(this).parents('tr')).data();
                            getEmployeeDetailsBySignum_View(data.signum);
                        });

                        $('#table_team_explorer tbody').on('click', 'i.fa-sign-out', function () {
                            var data = oTable.row($(this).parents('tr')).data();
                            getPlannedLeaves(data.signum);
                            $('#leaveModal').modal('show');
                            $('#leaveSignum').val(data.signum);
                            $('#leaveTitle').text(data.signum + " (" + data.employeeName + ")");
                            //$('#leave_type').select2({ placeholder: "Select Leave Type" });
                            getLeaveType();
                        });

                        $('#table_team_explorer tbody').on('click', 'i.fa-clock-o', function () {
                            var data = oTable.row($(this).parents('tr')).data();
                            var timeZoneVisible = true;
                            clearShiftModal();
                            getTimeZones();
                            getShiftTiming(data.signum, data.employeeName, timeZoneVisible);
                        });

                        var table = document.getElementById("table_team_explorer");
                        $("#checkAllEmployee").click(function (e) {
                            $(".checkBoxClassSignum").prop('checked', $(this).prop('checked'));
                        });
                    }
                });
            }
            else {
                pwIsf.removeLayer();
                pwIsf.alert({ msg: data.formErrors[0], type: 'error' });
            }
            $('#table_team_explorer tfoot').insertAfter($('#table_team_explorer thead'));
        },
        error: function (xhr, status, statusText) {
            pwIsf.removeLayer();
            pwIsf.alert({ msg: xhr.responseJSON["errorMessage"], type: "error" });

        }
    });
}

function refresh() {
    if (role!== "Application Admin" && role!== "ISF Access Manager") {
        getEmployeeDetailsByManager();

    }
}

function cleanModalFields() {

    $("#input_reason").val("");
    $("#date_resign").val("");
    $("#date_release").val("");
    $("#select_status").val(0);
    $('#select_status').trigger('change');
}

function getPlannedLeaves(signum) {
    if ($.fn.dataTable.isDataTable('#tblLeaveinfo')) {
        oTable.destroy();
        $('#tblLeaveinfo').empty();
    }
    pwIsf.addLayer({ text: 'Please wait ...' });
    var serviceCall = $.isf.ajax({
        url: service_java_URL + "activityMaster/getLeavePlanBySignum/" + signum,
        returnAjaxObj: true,
        context: this,
        crossdomain: true,
        dataType: 'json',
        contentType: 'application/json',
        type: 'GET',
        xhrFields: {
            withCredentials: false
        },
        success: function (data) {
            createPlanLeaveTable(data);
            /// GenerateTable(data);
            pwIsf.removeLayer();
        },
        error: function (xhr, status, statusText) {
            console.log('An error occurred');
            pwIsf.removeLayer();
        }
    });
    
}

function deleteLeaveRequest(leaveID, signum) {
    pwIsf.confirm({
        title: 'Delete Request', msg: "Are you sure want to Delete the Leave Request?",
        'buttons': {
            'Yes': {
                'action': function () {
                    pwIsf.addLayer({ text: 'Please wait ...' });
                    $.isf.ajax({
                        url: service_java_URL + "activityMaster/deleteLeavePlan?signum=" + signum + "&leavePlanID=" + leaveID,
                        context: this,
                        crossdomain: true,
                        processData: true,
                        contentType: 'application/json',
                        type: 'POST',
                        xhrFields: {
                            withCredentials: false
                        },
                        success: function (data) {
                           // pwIsf.alert({ msg: 'The file "' + filename + '" has been selected', type: 'info', autoClose: 2 });
                            pwIsf.alert({ msg: "Leave Request " + "Deleted", type: "success", autoClose: 2 });
                            getPlannedLeaves(signum);
                            $('#leaveStart_Date').val('');
                            $('#leaveEnd_Date').val('');
                            $('#leave_type').val('');
                            $('#leave_comment').val('');

                        },
                        error: function (xhr, status, statusText) {
                            pwIsf.alert({ msg: "Error while Deleting Leave Request", type: "warning" });
                        },
                        complete: function (xhr, statusText) {
                            pwIsf.removeLayer();
                        }
                    });
                }
            },
            'No': { 'action': function () { } },

        }
    });

}

function createPlanLeaveTable(data) {
    $.each(data, function (i, d) {
        if (d.isActive === 1) {
           
            d.actionIcon = '<div><a class="icon - delete lsp" title="Delete Leave" onclick="deleteLeaveRequest(\'' + d.leavePlanID + '\',\'' + d.signum + '\');" href="#"><i class="fa fa-remove darkredcolor"></i></a></div>';
        }
    })
    $('#tblLeaveinfo_tbody').html('');
    var oTable = $('#tblLeaveinfo').DataTable({
        "order": [[1, "asc"]],
        searching: true,
        responsive: true,
        destroy: true,
        colReorder: true,
        "pageLength": 10,
        "paging": true,
        dom: 'Bfrtip',
        buttons: [
            {
                extend: 'excel',
                text: 'Export',
                filename: function () {
                    var d = (new Date()).toLocaleDateString();
                    return 'Leave Details_' + login_signum + "_" + d;
                },
            }

        ],
        "data": data,
        "destroy": true,
        "columns": [

            {
                "title": "Actions",
                "targets": 'no-sort',
                "orderable": false,
                "searchable": false,
                "data": "actionIcon"

            },
            {
                "title": "Leave From",
                "data": "startDate"
            },
            {
                "title": "Leave To",
                "data": "endDate"
            },

            {
                "title": "Leave Type",
                "data": "type"
            }

        ],
        initComplete: function () {



        }
    });

    $('.dt-buttons').addClass('margin-10px');
    $('.dataTables_filter').addClass('margin-10px');
    $('#tblLeaveinfo_tbody').find('td:first-child').each(function () {
        $(this).css({ 'text-align':'center'});
    });
        

}

//function GenerateTable(data) {

//    tblLeaveinfo
//    var tableHTMLPre = '<table class="table table-striped" style="margin: 0px;">';
//    var tableHTMLPost = '</table>';

//    var tableHeadPre = '<thead><tr>';
//    var tableHead = '';
//    var tableHeadPost = '</tr></thead>';
//    var woTableHead = '';

//    tableHead += `<th style="font-weight:bold;">Action</th><th style="font-weight:bold;">Leave From:</th><th style="font-weight:bold;">Leave To:</th><th style="font-weight:bold;">Leave Type:</th>`;

//    var tableBodyPre = '<tbody>';
//    var tableBody = '';
//    var tableBodyPost = '</tbody>';

//    for (obj in data) {
//        tableBody += '<tr>';
//        var d = new Date();
//        var month = d.getMonth() + 1;
//        var day = d.getDate();

//        var output = d.getFullYear() + '-' +
//            (month < 10 ? '0' : '') + month + '-' +
//            (day < 10 ? '0' : '') + day;
//        var color = '#000000';
//        var leave_id = data[obj].leavePlanID;
//        var signum = data[obj].signum;
//        var createdBy = data[obj].createdBy;
//        var leave_startDate = data[obj].startDate;
//        var leave_endDate = data[obj].endDate;
//        var isActive = data[obj].isActive;
//        var type = data[obj].type;
//        var comment = data[obj].comment;        
//        tableBody += '<td><div style="display:flex;"><a class="icon - delete lsp" title="Delete Leave" onclick="deleteLeaveRequest(\'' + leave_id + '\',\'' + signum + '\');" href="#"><i class="fa fa-remove darkredcolor"></i></a></div></td>';
//        tableBody += '<td style=" color:' + color + ';font-weight: bold;">' +leave_startDate + '</td>';
//        tableBody += '<td style=" color:' + color + ';font-weight: bold;">' + leave_endDate + '</td>';
//        tableBody += '<td style=" color:' + color + ';font-weight: bold;">' + type + '</td>';

//        tableBody += '</tr>';
//    }

//    var tableHTML = tableHTMLPre + tableHeadPre + tableHead + tableHeadPost + tableBodyPre + tableBody + tableBodyPost + tableHTMLPost;
//    $("#tableMain").html(tableHTML);

//}

function savePlannedLeaves() {
    var leave_start = $('#leaveStart_Date').val();
    var leave_end = $('#leaveEnd_Date').val();
    var leave_type = $('#leave_type option:selected').val();
    var leave_comment = $('#leave_comment').val();
    var signum = $('#leaveSignum').val();
    var leave = new Object();
    leave.startDate = leave_start;
    leave.endDate = leave_end;
    leave.type = leave_type;
    leave.comments = leave_comment;
    leave.isActive = 1;
    leave.createdBy = signumGlobal;
    leave.signum = signum;
    if (leave_start === null || leave_end === null || leave_type === null || leave_type === 0 || leave_comment === null || leave_comment === "") {
        pwIsf.alert({ msg: "Please fill all the fields", type: "warning" });
    }
    else {
        if (leave_start > leave_end) {
            pwIsf.alert({ msg: "Start Date Should before End Date", type: "warning" });
        }
        else {
            pwIsf.addLayer({ text: 'Please wait ...' });
            $.isf.ajax({
                url: service_java_URL + "activityMaster/saveLeavePlan",
                context: this,
                crossdomain: true,
                processData: true,
                contentType: 'application/json',
                type: 'POST',
                data: JSON.stringify(leave),
                xhrFields: {
                    withCredentials: false
                },
                success: function (data) {
                    if (data === true) {
                        pwIsf.alert({ msg: "Leave Requested Successfully", type: "success" });
                        getPlannedLeaves(signum);
                        $('#leaveStart_Date').val('');
                        $('#leaveEnd_Date').val('');
                        $('#leave_type').val('');

                        $('#leave_comment').val('');
                    }
                    else {
                        pwIsf.alert({ msg: "Cannot request for same time period", type: "warning" });
                    }
                    pwIsf.removeLayer();
                },
                error: function (xhr, status, statusText) {

                }
            });
        }
    }
}

function getEmployeeDetailsBySignum(signum, status) {

    cleanModalFields();

    $('#scopeModal2').modal('show');

    $.isf.ajax({
        url: service_java_URL + "activityMaster/getEmployeeBySignum/" + signum,
        success: function (data) {

            $("#input_signum").val(data.signum);
            $("#input_current_status").val(data.status);


        },
        error: function (xhr, status, statusText) {
            console.log('An error occurred : ' + xhr.responseText);
        }
    });


}

function getEmployeeDetailsBySignum_View(signum) {

    $('#scopeModal3').modal('show');

    $.isf.ajax({
        url: service_java_URL + "activityMaster/getEmployeeBySignum/" + signum,
        success: function (data) {

            $("#employee_signum").val(data.signum);
            $("#employee_email").val(data.employeeEmailId);
            $("#employee_jobRole").val(data.jobRole);
            $("#employee_serviceArea").val(data.serviceArea);
            $("#employee_officeBuilding").val(data.officeBuilding);
            $("#employee_contactNumber").val(data.contactNumber);
            $("#employee_employeeName").val(data.employeeName);
            $("#employee_personnelNumber").val(data.personnelNumber);
            $("#employee_jobStage").val(data.jobStage);
            $("#employee_jobRoleFamily").val(data.jobRoleFamily);
            $("#employee_city").val(data.city);
            $("#employee_floor").val(data.floor);
            $("#employee_costCenter").val(data.costCenter);
            $("#employee_functionalArea").val(data.functionalArea);
            $("#employee_gender").val(data.gender);
            $("#employee_hrLocation").val(data.hrLocation);
            $("#employee_orgunit").val(data.orgUnit);
            $("#employee_managerSignum").val(data.managerSignum);
            $("#employee_grade").val(data.grade);
            $("#employee_cc").val(data.costCenter);

        },
        error: function (xhr, status, statusText) {
            console.log('An error occurred : ' + xhr.responseText);
        }
    });




}

function ValidateData() {
    var OK = true;
    $("#modal_message").text("");
    $('#input_reason-Required').text("");
    $('#date_resign-Required').text("");
    $('#date_release-Required').text("");
    $('#date_release-Mayor').text("");


    if ($("#input_current_status").val() === $("#select_status").val()) {
        $("#modal_validation").modal("show");
        $("#modal_message").text("The new status should be different from the current status.");
    }

    var letterNumber = /^[0-9a-zA-Z]+$/;
    var text = $("#input_reason").val();

    if ($("#input_reason").val().trim() == '') {
        $('#input_reason-Required').text("Reason cannot be blank.");
        OK = false;
    }

    if (validateStatus($("#select_status").val()) == false) {
        OK = false;
    }

    return OK;

}

function validateStatus(status) {
    var validation = true;
    var date_resign = new Date($('#date_resign').val());
    var date_release = new Date($('#date_release').val());
    var currentDate = new Date();

    switch (status) {
        case "Active":

            break;
        default:

            if ($("#date_resign").val() == null || $("#date_resign").val() == "") {
                $('#date_resign-Required').text("Resign date is required");
                validation = false;
            }



            if ($("#date_release").val() == null || $("#date_release").val() == "") {
                $('#date_release-Required').text("Release date is required");
                validation = false;
            }


            if ($("#date_resign").val() != null && $("#date_resign").val() != null) {


                var oneDay = 24 * 60 * 60 * 1000; // hours*minutes*seconds*milliseconds
                var diffDays = Math.round(Math.abs((date_resign.getTime() - date_release.getTime()) / (oneDay)));

                if (date_resign > date_release) {
                    $('#date_release-Mayor').text(" Release date must be after  Resign/Transfer date");
                    validation = false;
                } else if (diffDays > 90) {
                    $('#date_release-Required').text("Release date cannot be greater than 90 days from Resign/Transfer date. ");
                    validation = false;
                }
            }
            break;
    }
    return validation;
}

function changeEmployeeStatusToActive(signum) {

    var employee = new Object();

    employee.resourceStatusName = "Active";
    employee.signum = signum == null ? "" : signum;
    employee.resignedOrTransferredDate = "";
    employee.releaseDate = "";
    employee.reason = "";
    employee.createdBy = login_signum;


    $.isf.ajax({
        url: service_java_URL + "activityMaster/changeEmployeeStatus",
        context: this,
        crossdomain: true,
        processData: true,
        contentType: 'application/json',
        type: 'POST',
        data: JSON.stringify(employee),
        xhrFields: {
            withCredentials: false
        },
        success: function (data) {

            var result = $(data).find("string").text();
            if (data == "Successfull") {

                $('#myModal1').modal('show');

            } else {
                $('#employee_change_status').text('Your changes couldn\'t be saved.');
            }

        },
        error: function (xhr, status, statusText) {
            console.log('An error occurred on ChangeEmployeeStatus: ' + xhr.responseText);
        }
    })
    refresh();

}

var formatted_date = function (date) {
    var m = ("0" + (date.getMonth() + 1)).slice(-2); // in javascript month start from 0.
    var d = ("0" + date.getDate()).slice(-2); // add leading zero 
    var y = date.getFullYear();
    return y + '-' + m + '-' + d;
}

function changeEmployeeStatus() {

    var select_status = $("#select_status").val();

    if (ValidateData() == true) {
        var input_signum = $("#input_signum").val();

        var input_reason = $("#input_reason").val();
        var date_resign = $("#date_resign").val();
        var date_release_1 = formatted_date(new Date($("#date_release").val()));
        var date_release = $("#date_release").val();
        var employee = new Object();
        employee.resourceStatusName = select_status;
        employee.signum = input_signum;
        employee.resignedOrTransferredDate = date_resign;
        employee.releaseDate = date_release;
        employee.reason = input_reason;
        employee.createdBy = login_signum;

        var employeeResign = new Object();
        employeeResign.signum = input_signum;
        employeeResign.releaseDate = date_release_1;
        employeeResign.currentUser = signumGlobal;


        $.isf.ajax({
            url: service_java_URL + "resourceManagement/updateResignedResource",
            context: this,
            crossdomain: true,
            processData: true,
            contentType: 'application/json; charset=utf-8',
            type: 'POST',
            data: JSON.stringify(employeeResign),
            xhrFields: {
                withCredentials: false
            },
            success: function (reData) {

                if (reData) {

                    $.isf.ajax({
                        url: service_java_URL + "activityMaster/changeEmployeeStatus",
                        context: this,
                        crossdomain: true,
                        processData: true,
                        contentType: 'application/json',
                        type: 'POST',
                        data: JSON.stringify(employee),
                        xhrFields: {
                            withCredentials: false
                        },
                        success: function (data) {

                            var result = $(data).find("string").text();
                            if (data == "Successfull") {
                                $('#scopeModal2').modal('hide');
                                $('#employee_change_status').text('Your changes has been saved');

                            } else {
                                $('#employee_change_status').text('Your changes couldn\'t be saved.');
                            }
                            $('#edit_success').modal('show');
                        },
                        error: function (xhr, status, statusText) {
                            console.log('An error occurred in ChangeEmployeeStatus : ' + xhr.responseText);
                        }
                    })
                } else {
                    $('#edit_success').modal('show');
                    $('#employee_change_status').text('Your changes couldn\'t be saved..');

                }

            },
            error: function (xhr, status, statusText) {
                console.log('An error occurred in updateResignedResource: ' + xhr.responseText);
            }
        });
    }
    refresh();

}

function cleanResignDate() {

    $('#date_release').val("");
}