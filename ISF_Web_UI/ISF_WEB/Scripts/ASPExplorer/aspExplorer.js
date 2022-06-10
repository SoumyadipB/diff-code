/*------------------------Get Tabular Data in ASP Explorer View--------------------------------*/
function getASPExplorerView()  {
    $('#MsjNodata').text("");
    if ($.fn.dataTable.isDataTable('#aspExplorerTable')) {
        oTable.destroy();
        $('#aspExplorerTable').empty();
    }
    pwIsf.addLayer({ text: "Please wait ..." });

    $.isf.ajax({
        url: service_java_URL + "aspManagement/getAspExplorerForManager?managerSignum=" + signumGlobal,
        success: function (data) {
            $.each(data, function (i, d) {
               
                if (d.isProfileActive && d.isActive)
                    d.actionIcon = '<div style="display:flex;" onclick="getASPSignum(\'' + d.signum + '\')"><a class="icon-edit" title="Extend ASP Access" href="#" data-toggle="modal" data-target="#updateASPAccess">' + getIcon('edit') + '</a><a class="icon-delete lsp" title="Revoke Access" href="#"  onclick="revokeASPAccess(\''+d.signum+'\')">' + getIcon('reinstate') + '</a><a class="icon-view lsp" title="Update Leave Plan" href="#" onclick=getPlannedLeaves(\''+d.signum+'\') data-toggle="modal" data-target="#updateASPLeaves">' + getIcon('signout') + '</a><a class="icon-edit lsp" href="#" onclick="getTimeZones();getShiftTiming(\''+d.signum+'\',\''+d.employeeName+'\',true);" title="Update Shift Time">' + getIcon('clock') + '</a></div>';
                else if(!d.isActive)
                    d.actionIcon = '<div style="display:flex;" onclick="getASPSignum(\'' + d.signum + '\')"><a class="icon-edit" title="Extend ASP Access" href="#" data-toggle="modal" data-target="#updateASPAccess">' + getIcon('edit') + '</a></div>';
                else
                    d.actionIcon = '<div style="display:flex;" onclick="getASPSignum(\'' + d.signum + '\')"><a class="icon-add" title="Approve ASP" href="#" data-toggle="modal" data-target="#approveASPModal">' + getIcon('accept') + '</a><a class="icon-delete lsp" title="Reject ASP" href="#" data-toggle="modal" data-target="#rejectASPModal">' + getIcon('reject') + '</a></div>';
               
            })
            pwIsf.removeLayer();
            $("#aspExplorerTable").append($('<tfoot><tr><th></th><th>Signum</th><th>Name</th><th>Status</th><th>Vendor Code/Name</th><th>Country</th><th>City</th><th>Start Date</th><th>End Date</th></tr></tfoot>'));
            oTable = $("#aspExplorerTable").DataTable({
                searching: true,
                responsive: true,
                colReorder: true,
                order: [1],
                dom: 'Bfrtip',
                buttons: [
                 'colvis', 'excel'
                ],
                "pageLength": 10,
                "data": data,
                "destroy": true,
                "columns": [{
                    "title": "Actions",
                    "targets": 'no-sort',
                    "orderable": false,
                    "searchable": false,
                    "data": "actionIcon"

                },
                            { "title": "Signum", "data": null,
                            "render": function ( data, type, row, meta ) {
                                if(data.isLocked)
                                    return '<a href="#" style="color:blue;" title="Click here to see ASP Details!" onclick="getASPDetailsBySignum(\'' + data.signum + '\')">'+data.signum+'</a>&nbsp;&nbsp;<i title="ASP Account is Locked" style="opacity:0.6" class="fa fa-lock"></i>';
                                else
                                    return '<a href="#" style="color:blue;" title="Click here to see ASP Details!" onclick="getASPDetailsBySignum(\'' + data.signum + '\')">'+data.signum+'</a>';
                            }
                            },
                            { "title": "Name", "data": "employeeName" },
                            { "title": "Status", "data": "status" },
                            { "title": "VendorCode/Name", "data": null ,
                            "render": function(data, type, row, meta){
                                return data.vendorCode + "/" + data.vendorName;
                            }
                            },
                            //{ "title": "Vendor Name", "data": "vendorName" },
                            { "title": "Country", "data": "country" },
                            { "title": "City", "data": "city" },
                            { "title": "Start Date", "data": "startDate" },
                            { "title": "End Date", "data": "endDate" }],
                initComplete: function () {

                    $('#aspExplorerTable tfoot th').each(function (i) {
                        var title = $('#aspExplorerTable thead th').eq($(this).index()).text();
                        if(title!="Actions")
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

                }
            });
            $('#aspExplorerTable tfoot').insertAfter($('#aspExplorerTable thead'));
        },
        error: function () {
            pwIsf.removeLayer();
            $('#MsjNodata').text("No data exists!");
        }
    });
}
/*------------------------API Call for Approve/Reject of ASP--------------------------------*/
function updateASPAccess(status, startDate, endDate, rejectReason, aspSignum) {
    var approveASPObj = new Object();
    approveASPObj.userSignum = aspSignum;
    approveASPObj.managerSignum = signumGlobal;
    approveASPObj.startDate = startDate;
    approveASPObj.endDate = endDate;
    approveASPObj.operation = status;
    approveASPObj.rejectMessage = rejectReason;

    var JsonObj = JSON.stringify(approveASPObj);
    $.isf.ajax({
        url: service_java_URL + "aspManagement/updateAspProfileAccess",
        type: "POST",
        contentType: 'application/json',
        data: JsonObj,
        success: function (data) {
            responseHandler(data);
            getASPExplorerView();
            switch(status){
                case "ACCEPTED":
                    $('#approveASPModal').modal('hide');
                    break;
                case "REJECTED":
                    $('#rejectASPModal').modal('hide');
                    break;
            }
        },
        error: function (xhr, status, statusText) {
            console.log("Fail " + xhr.responseText);
        }
    });
}
/*------------------------Set Signum of ASP on click of Modal-------------------------------*/
function getASPSignum(aspSignum) {
    Glob_aspSignum = aspSignum;
}
/*----------Validate Dates---------------*/
function validateDates(startDate, endDate) {
    var dateValidated = true;
    if (startDate == "" || endDate == "") {
        pwIsf.alert({ msg: "Start Date or End Date cannot be left blank!" });
        dateValidated = false;
    }
    else if (startDate > endDate) {
        pwIsf.alert({ msg: "End Date should be after Start Date!" });
        dateValidated = false;
    }
    else if ((new Date(endDate) - new Date(startDate)) / 86400000 > 90) {
        pwIsf.alert({ msg: "End Date cannot be more than 90 days!" });
        dateValidated = false;
    }
    return dateValidated;
}
/*------------------------On Click of Approve Reject Button in Modal--------------------------------*/
function updateASPStatus(status) {
    var startDate = "";
    var endDate = "";
    var rejectReason = "";
    var warningBool = true;
    switch (status) {
        case "ACCEPTED":
            startDate = $("#approveStartDate").val();
            endDate = $("#approveEndDate").val();
            rejectReason = "";
            warningBool = validateDates(startDate, endDate);
            if(warningBool)
                updateASPAccess(status, startDate, endDate, rejectReason, Glob_aspSignum);
            break;
        case "REJECTED":
            startDate = ""; endDate = "";
            rejectReason = $("#rejectReasonText").val();
            if (rejectReason == "")
                pwIsf.alert({ msg: "Please enter a valid reason for Rejection!" });
            else
                updateASPAccess(status, startDate, endDate, rejectReason, Glob_aspSignum);
            break;
        case "REVOKED":
            startDate = "";
            endDate = "";
            rejectReason = "";
            //warningBool = validateDates(startDate, endDate);
            //if (warningBool)
                updateASPAccess(status, startDate, endDate, rejectReason, Glob_aspSignum);
            break;
        case "EXTENDED":
            startDate = $("#accessStartDateASP").val();
            endDate = $("#accessEndDateASP").val();
            rejectReason = "";
            warningBool = validateDates(startDate, endDate);
            if (warningBool)
                updateASPAccess(status, startDate, endDate, rejectReason, Glob_aspSignum);
            break;
        default:
           
    }
}

function savePlannedLeaves() {
    var leave_start = $('#leaveStart_Date').val();
    var leave_end = $('#leaveEnd_Date').val();
    var leave_type = $('#leave_type option:selected').val();
    var leave_comment = $('#leave_comment').val();
    var signum = Glob_aspSignum;
    var leave = new Object();
    leave.startDate = leave_start;
    leave.endDate = leave_end;
    leave.type = leave_type;
    leave.comments = leave_comment;
    leave.isActive = 1;
    leave.createdBy = signumGlobal;
    leave.signum = signum;
    if (leave_start === null || leave_end === null || leave_type === null || leave_type === 0 || leave_comment === null || leave_comment === "" || leave_end === "" || leave_start === "") {
        pwIsf.alert({ msg: "Please fill all the fields", type: "warning" });
    }
    else {
        if (leave_start > leave_end) {
            pwIsf.alert({ msg: "Start Date Should before End Date", type: "warning" });
        }
        else {
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
                },
                error: function (xhr, status, statusText) {

                }
            });
        }
    }
}

function getPlannedLeaves(signum) {
    //$('#leaveModal').modal('show');
    var serviceCall = $.isf.ajax({
        url: service_java_URL + "activityMaster/getLeavePlanBySignum/" + signum,
        context: this,
        crossdomain: true,
        dataType: 'json',
        contentType: 'application/json',
        type: 'GET',
        xhrFields: {
            withCredentials: false
        },
        success: function (data) {
            GenerateTable(data);

        },
        error: function (xhr, status, statusText) {
            console.log('An error occurred');
            //alert('Sorry, an error occurred. Please try reloading the page. \n\nIf that doesn\'t work please send us the following error message, using the contact us page. \n\n' + this.url + ' ' + xhr.status + ' ' + xhr.statusText);

        }
    })

}

function GenerateTable(data) {


    var tableHTMLPre = '<table class="table table-striped" style="margin: 0px;">';
    var tableHTMLPost = '</table>';

    var tableHeadPre = '<thead><tr>';
    var tableHead = '';
    var tableHeadPost = '</tr></thead>';
    var woTableHead = '';

    tableHead += `<th style="font-weight:bold;">Action</th><th style="font-weight:bold;">Leave From:</th><th style="font-weight:bold;">Leave To:</th><th style="font-weight:bold;">Leave Type:</th>`;

    var tableBodyPre = '<tbody>';
    var tableBody = '';
    var tableBodyPost = '</tbody>';

    for (obj in data) {
        //tableBody += '<tr>' + obj + '</tr>';
        tableBody += '<tr>';


        //tableBody += '<td style="text-align: center; color:' + color + '; background-color: ' + bgcolor + '; font-weight: bold;">' + wo + 'WO' + '<br>' + hours.toString() + 'Hrs' + '</td>';
        var d = new Date();
        var month = d.getMonth() + 1;
        var day = d.getDate();

        var output = d.getFullYear() + '-' +
            (month < 10 ? '0' : '') + month + '-' +
            (day < 10 ? '0' : '') + day;
        var color = '#000000';
        var leave_id = data[obj].leavePlanID;
        var signum = data[obj].signum;
        var createdBy = data[obj].createdBy;
        var leave_startDate = data[obj].startDate;
        var leave_endDate = data[obj].endDate;
        var isActive = data[obj].isActive;
        var type = data[obj].type;
        var comment = data[obj].comment;
        //if (leave_startDate == output || leave_startDate < output) {
        //    tableBody +='<td></td>';
        //}
        //else {
        tableBody += '<td><div style="display:flex;"><a class="icon - delete lsp" title="Delete Leave" onclick="deleteLeaveRequest(\'' + leave_id + '\',\'' + signum + '\');" href="#"><i class="fa fa-remove"></i></a></div></td>';
        //}
        tableBody += '<td style=" color:' + color + ';font-weight: bold;">' +leave_startDate + '</td>';
        tableBody += '<td style=" color:' + color + ';font-weight: bold;">' + leave_endDate + '</td>';
        tableBody += '<td style=" color:' + color + ';font-weight: bold;">' + type + '</td>';
        //}

        tableBody += '</tr>';
    }

    var tableHTML = tableHTMLPre + tableHeadPre + tableHead + tableHeadPost + tableBodyPre + tableBody + tableBodyPost + tableHTMLPost;
    $("#tableMain").html(tableHTML);

}

function deleteLeaveRequest(leaveID, signum) {
    pwIsf.confirm({
        title: 'Delete Request', msg: "Are you sure want to Delete the Leave Request?",
        'buttons': {
            'Yes': {
                'action': function () {
                    //let service_java_URL = "http://169.144.28.204:8080/isf-rest-server-java/";
                    pwIsf.addLayer({ text: 'Please wait ...' });
                    $.isf.ajax({
                        url: service_java_URL + "activityMaster/deleteLeavePlan?signum=" + signum + "&leavePlanID=" + leaveID,
                        //url: service_java_URL + "botStore/changeDeployedBotStatus/" + requestID + "/" + signum+"/1",
                        context: this,
                        crossdomain: true,
                        processData: true,
                        contentType: 'application/json',
                        type: 'POST',
                        xhrFields: {
                            withCredentials: false
                        },
                        success: function (data) {
                            pwIsf.alert({ msg: "Leave Request" + "Deleted", type: "success" });
                            //if (data.apiSuccess)
                            //$('#leaveHistory').panel('refresh');
                            //else
                            //    pwIsf.alert({ msg: data.responseMsg, type: 'warning' });
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

function getASPDetailsBySignum(signum) {

    //cleanModalFields();

    $('#getASPDetails').modal('show');
  $.isf.ajax({
        url: service_java_URL + "aspManagement/getAspDetailsBySignum?signum=" + signum,
        success: function (data) {
            $("#asp_signum").val(data.signum);
            $("#asp_email").val(data.email);
            $("#asp_country").val(data.country);
            $("#asp_employeeName").val(data.firstName);
            $("#asp_vendorCodeID").val(data.vendorCode);
            $("#asp_city").val(data.city);
            $("#asp_contactNumber").val(data.contactNumber);
            $("#asp_vendorCodeName").val(data.vendorName);
            $("#asp_manager").val(data.managerName);
        },
        error: function (xhr, status, statusText) {
            console.log('An error occurred : ' + xhr.responseText);
        }
    });
}

function revokeASPAccess(signum) {
    Glob_aspSignum = signum;
    pwIsf.confirm({
        title: 'Revoke ASP Access', msg: "Are you sure want to Revoke the Access?",
        'buttons': {
            'Yes': {
                'action': function(){ updateASPStatus("REVOKED"); }
            },
            'No': { 'action': function () { } },

        }
    });

}