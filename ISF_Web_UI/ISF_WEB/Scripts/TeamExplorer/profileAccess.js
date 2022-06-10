var renewalList = [];
var approveList = [];
var singleRejectArr = [];
var singleApproveArr = [];
var role;
var globalAccessProfileName;
var globalUserAccessProfId;
var globalUserSignum;
const profileId = JSON.parse(ActiveProfileSession).accessProfileID;
const C_APPROVEDSTATUS = 'APPROVED';
const C_REJECTEDSTATUS = 'REJECTED';
const C_SINGLEREJECTEDSTATUS = 'SINGLEREJECTED';
const C_SINGLEAPPROVEDSTATUS = 'SINGLEAPPROVED';
const C_REVOKED = "REVOKED";
const C_RENEWED = 'RENEWED';

$(document).ready(function () {
    getUserAccessView();
    if (profileId === 40 || profileId === 67) {
        $('#renewButton').hide();
    }
    $('#accessProvideTable').on('draw.dt', function () {
        clearAllUserAccessCollection();
        $('#chkApproveRejectHeader').prop('checked', false);
    });
    configureApproveUserModalShow();
    configurerejectUserModalShow();
   
});
function configureApproveUserModalShow() {
    $('#approveUserModal').on('show.bs.modal', function (e) {
        var approveRequestList = fetchApprovedOrRejectDetails();
        if (approveRequestList.length === 0) {
            e.stopPropagation();
            pwIsf.alert({ msg: 'Please select at least 1 record.', type: 'error' });
            return e.preventDefault();
        }
        else {
            // For ISF Access Manager, MDM and EU no Approval popup should be dispalyed 
            var nonExpiredProfiles = checkNonExpiredProfiles(approveRequestList);
            if (nonExpiredProfiles.length > 0) {
                updateUserStatus('APPROVED', "", false)
                return e.preventDefault();
            }
            else {
                let apRqst;
                $('#tblApprovalRejectList').html("");
                $('#tblApprovalRejectList').append($('<thead><tr><th>Signum</th><th>Profile Id</th><th>Profile Name</th></tr></thead>'));
                for (apRqst of approveRequestList) {
                    $('#tblApprovalRejectList').append($(`<tbody><tr><td>${apRqst.signum}</td><td>${apRqst.profileId}</td><td>${apRqst.profileName}</td></tr></tbody>`));
                }
                var today = formatDate(new Date());
                var endDate90 = formatDate(new Date().setDate(new Date().getDate() + 90));
                $('#approveUserStartDate').val(today);
                $('#approveUserEndDate').val(endDate90);
                $('#tblApprovalRejectList thead > tr:first-child').css({
                    'padding': '10px',
                    "position": 'sticky',
                    'z-index': '1001',
                    'top': '0px'
                });
            }
        }
    });
}

function configurerejectUserModalShow() {
    $('#rejectUserModal').on('show.bs.modal', function (e) {
        var approveRequestList = fetchApprovedOrRejectDetails();
        if (approveRequestList.length === 0) {
            e.stopPropagation();
            pwIsf.alert({ msg: 'Please select at least 1 record.', type: 'error' });
            return e.preventDefault();
        }
        else {
            let appRqst;
            $('#rejectReasonText').val('');
            $('#tblRejectList').html("");
            $('#tblRejectList').append($('<thead><tr><th>Signum</th><th>Profile Id</th><th>Profile Name</th></tr></thead>'));
            for (appRqst of approveRequestList) {
                $('#tblRejectList').append($(`<tbody><tr><td>${appRqst.signum}</td><td>${appRqst.profileId}</td><td>${appRqst.profileName}</td></tr></tbody>`));
            }
            $('#tblRejectList thead > tr:first-child').css({
                'padding': '10px',
                "position": 'sticky',
                'z-index': '1001',
                'top': '0px'
            });
        }
    });
}
// To get List of non expired profiles
function checkNonExpiredProfiles(approveRequestList) {
    var nonExpiredProfileList = [];
    let appRqst;
    for (appRqst of approveRequestList) {
        if (appRqst.profileId === 67 || appRqst.profileId === 31 || appRqst.profileId === 32 || appRqst.profileId === 33 || appRqst.profileId === 34 || appRqst.profileId === 35 || appRqst.profileId === 38 || appRqst.profileId === 41) {
            nonExpiredProfileList.push(appRqst.profileId);
        }
    }
    return nonExpiredProfileList;
}
/*------------------------Access ITSA Renew Revoke Starts-------------------------------*/

//Set Global Values on Click of Row
function getUserDetails(accessProfName, accessProfileID, userSignum) {
    globalAccessProfileName = accessProfName;
    globalUserAccessProfId = accessProfileID;
    globalUserSignum = userSignum;
}

function FetchUserDetails() {
    return renewalList;
}
function fetchApprovedOrRejectDetails() {
    return approveList;
}

//Renew, Revoke
function updateUserAccess(status, startDate, endDate, rejectReason, aspSignum, bulkApproval) {

    if (status === C_APPROVEDSTATUS || status === C_REJECTEDSTATUS || status === C_SINGLEREJECTEDSTATUS || status === C_SINGLEAPPROVEDSTATUS) {
        var approveData = [];
        if (status === C_APPROVEDSTATUS || status === C_REJECTEDSTATUS) {
            approveData = getApprovedOrRejectData(approveList, startDate, endDate, rejectReason, status);
        } else if (status === C_SINGLEREJECTEDSTATUS) {
            approveData = getApprovedOrRejectData(singleRejectArr, startDate, endDate, rejectReason, status);
        } else if (status === C_SINGLEAPPROVEDSTATUS) {
            approveData = getApprovedOrRejectData(singleApproveArr, startDate, endDate, rejectReason, status);
        }
        const jsonObj = JSON.stringify(approveData);
        pwIsf.addLayer({ text: "Please wait ..." });
        $.isf.ajax({
            url: `${service_java_URL}accessManagement/updateAccessRequestStatus`,
            type: "POST",
            contentType: 'application/json',
            data: jsonObj,
            success: function (apiResponse) {
                ajaxSuccessUpdateAccessRqst(apiResponse,status);
            },
            error: function (xhr) {
                pwIsf.removeLayer();
                console.log(`Fail:  ${xhr.responseText}`);
            }
        });
    }
    else {
        const jsonObj = JSON.stringify(getJsonRquestBody(renewalList, bulkApproval, startDate, endDate, status));
        pwIsf.addLayer({ text: "Please wait ..." });
        $.isf.ajax({
            url: `${service_java_URL}accessManagement/updateAccessStatus`,
            type: "POST",
            contentType: 'application/json',
            data: jsonObj,
            success: function (apiResponse) {
                ajaxSuccessRenewList(apiResponse,status);
            },
            error: function (xhr) {
                pwIsf.removeLayer();
                console.log(`Fail:  ${xhr.responseText}`);
            }
        });
    }
}
function getJsonRquestBody(renewalListArr, bulkApproval,stDate,edDate,uStatus) {
    var requestArray = [];
    let approveUserObj = new Object();
    let rnList;
    if (renewalListArr !== undefined && renewalListArr.length > 0 && bulkApproval === true) {
        for (rnList of renewalListArr) {
            approveUserObj = new Object();
            approveUserObj.approvalStatus = uStatus;
            approveUserObj.approvedBy = signumGlobal;
            approveUserObj.userAccessProfileId = rnList.profileId;
            approveUserObj.startDate = stDate;
            approveUserObj.endDate = edDate;
            approveUserObj.signumId = rnList.signum;
            approveUserObj.accessProfileName = rnList.profileName;
            requestArray.push(approveUserObj);
        }
    }
    else {
        approveUserObj.approvalStatus = uStatus;
        approveUserObj.approvedBy = signumGlobal;
        approveUserObj.userAccessProfileId = globalUserAccessProfId;
        approveUserObj.startDate = stDate;
        approveUserObj.endDate = edDate;
        approveUserObj.signumId = globalUserSignum;
        approveUserObj.accessProfileName = globalAccessProfileName;
        requestArray.push(approveUserObj);
    }
    return requestArray;
}
function ajaxSuccessUpdateAccessRqst(data,uStatus) {
    pwIsf.removeLayer();
    responseHandlerPA(data);
    getUserAccessView();
    clearAllUserAccessCollection();
    switch (uStatus) {
        case C_APPROVEDSTATUS:
            $('#approveUserModal').modal('hide');
            break;
        case C_SINGLEAPPROVEDSTATUS:
            $('#approveSingleUserModal').modal('hide');
            break;
        case C_REJECTEDSTATUS:
            $('#rejectUserModal').modal('hide');
            break;
        case C_SINGLEREJECTEDSTATUS:
            $('#rejectIndividualUserModal').modal('hide');
            break;
    }
}
function ajaxSuccessRenewList(data, uStatus) {
    pwIsf.removeLayer();
    responseHandler(data);
    getRenewRevokeView();
    switch (uStatus) {
        case C_RENEWED:
            $('#updateUserAccess').modal('hide');
            $('#EditMultipleUserAccess').modal('hide');
            break;
        case C_REVOKED:
            $('#RevokeMultipleUserAccess').modal('hide');
            break;
    }
}
function getApprovedOrRejectData(accessData, startDate, endDate, rejectReason, status) {
    let approveOrRejectArray = [];
    let ac;
    if (accessData != undefined && accessData.length > 0) {
        for (ac of accessData) {
            let approveUserObj = new Object();
            approveUserObj.signumId = ac.signum;
            approveUserObj.approvalStatus = status;
            approveUserObj.approvedBy = signumGlobal;
            approveUserObj.accessProfileId = ac.profileId;
            approveUserObj.startDate = startDate;
            approveUserObj.endDate = endDate;
            approveUserObj.accessProfileName = ac.profileName;
            approveOrRejectArray.push(approveUserObj);
        }
    }
    return approveOrRejectArray;
}
function getDateFormat(date) {
    const d = new Date(date);
    let month = (d.getMonth() + 1).toString();
    let day = d.getDate().toString();
    let year = d.getFullYear();
    if (month.length < 2) {
        month = `0${month}`;
    }
    if (day.length < 2) {
        day = `0${day}`;
	}
    return [year, month, day].join('-');
}

//90 Days Validation
function validateDates(startDate, endDate) {
    var dateValidated = true;
    var today = getDateFormat(new Date());

    if (startDate < today) {
        pwIsf.alert({ msg: "Start Date cannot be before today's date!" });
        dateValidated = false;
    }
    if (startDate === "" || endDate === "") {
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

//Approve, Reject, Revoke, Renew Calls
function updateUserStatus(status, bulkApproval, isExpired) {
    var startDate = "";
    var endDate = "";
    var rejectReason = "";
    var warningBool = true;
    switch (status) {
        case C_SINGLEAPPROVEDSTATUS:
        case C_APPROVEDSTATUS:
            //check for profiles that can be expired
            if (isExpired === true) {
                startDate = $("#approveUserStartDate").val();
                endDate = $("#approveUserEndDate").val();
                warningBool = validateDates(startDate, endDate);
              
            }
            if (warningBool === true) {
                updateUserAccess(status, startDate, endDate, rejectReason, globalUserSignum, bulkApproval);
            }
            break;
        case C_REJECTEDSTATUS:
            rejectReason = $("#rejectReasonText").val();
            if (rejectReason === "") {
                pwIsf.alert({ msg: "Please enter a valid reason for Rejection!" });
			}
            else {
                updateUserAccess(status, startDate, endDate, rejectReason, globalUserSignum, bulkApproval);
			}
            break;
        case C_SINGLEREJECTEDSTATUS:
            rejectReason = $("#rejectIndividualReasonText").val();
            if (rejectReason === "") {
                pwIsf.alert({ msg: "Please enter a valid reason for Rejection!" });
			}
            else {
                updateUserAccess(status, startDate, endDate, rejectReason, globalUserSignum, bulkApproval);
			}
            break;
        case C_REVOKED:
            updateUserAccess(status, startDate, endDate, rejectReason, globalUserSignum, bulkApproval);
            break;
        case C_RENEWED:
            renewedUpdateUserAccess(bulkApproval, startDate, endDate, rejectReason, status);
            break;
    }
}
function renewedUpdateUserAccess(bulkApproval, stDate, eDate, rejectReason, status) {
    if (bulkApproval) {
        stDate = $("#accessStartDateUser_multiple").val();
        eDate = $("#accessEndDateUser_multiple").val();
    }
    else {
        stDate = $("#accessStartDateUser").val();
        eDate = $("#accessEndDateUser").val();
    }
    let warningBool = validateDates(stDate, eDate);
    if (warningBool === true) {
        updateUserAccess(status, stDate, eDate, rejectReason, globalUserSignum, bulkApproval);
    } 
}
//Revoke User
function revokeUserAccess(signum) {
    pwIsf.confirm({
        title: 'Revoke User Access', msg: "Are you sure want to Revoke the Access?",
        'buttons': {
            'Yes': {
                'action': function () { updateUserStatus(C_REVOKED); }
            },
            'No': { 'action': function () { console.log('No'); } },
        }
    });
}

// Clear Selected All checkboxes on Renew/Revoke Table
function clearSelection() {
    renewalList = [];
    var table = $('#renewRevokeTable').DataTable();
    table.rows().nodes().to$().find('input[type="checkbox"]').each(function () {
        $(this).prop('checked', false);
    });
    $("#chk_selectAll_action").prop('checked', false);
}

// Store detail on click of checkboxes/Select All check box
function StoreEmployeesDetails(profileName, userProfileId, Signum) {

    if (userProfileId != undefined) {
        var obj = { "profileName": profileName, "profileId": userProfileId, "signum": Signum };

        if (renewalList != undefined && renewalList.length == 0) {
            renewalList.push(obj);
        }
        else if (renewalList != undefined && renewalList.length > 0) {
            let alreadyExistsObj = renewalList.find(temp => temp.profileId === profileId && temp.signum === Signum);

            if (alreadyExistsObj == undefined) {
                renewalList.push(obj);
            }
        }
    }
}

// Delete employee details from "renewalList" object on deselecting on checkboxes
function DeleteEmployeesDetails(viewProfileId, signum) {

    if (viewProfileId != undefined) {

        if (renewalList != undefined && renewalList.length > 0) {
            let index = renewalList.findIndex(temp => temp.profileId === viewProfileId && temp.signum === signum);
            if (index != -1) {
                renewalList.splice(index, 1);
            }
        }
    }
}

// Add or delete employee details on selecting or deselecting checkboxes.
function CheckboxClick(index, profileName, viewProfileId, Signum) {
    if ($(`#chk_RenewEmpDetails_${index}`).prop('checked') === true) {
        StoreEmployeesDetails(profileName, viewProfileId, Signum)
    }
    else {
        DeleteEmployeesDetails(viewProfileId, Signum);
    }
}

// Select/Deselect all checkboxes on selecting/Deselecting "Select All" checkbox and store/Delete the details accordingly 
function SelectAllCheckBoxes(e) {
    $("#renewRevokeTable .chk_renew_revoke").prop('checked', $("#chk_selectAll_action").prop('checked'));
    var table = $('#renewRevokeTable').DataTable();
    var info = table.page.info();
    const allEmployeesDetails = table.rows().data();
    if (allEmployeesDetails != undefined && allEmployeesDetails.length > 0 && allEmployeesDetails.length >= info.end) {
        for (var i = 0; i < parseInt(info.end); i++) {
            if ($("#chk_selectAll_action").prop('checked') === true) {
                StoreEmployeesDetails(allEmployeesDetails[i].accessProfileName, allEmployeesDetails[i].userAccessProfileId, allEmployeesDetails[i].signumId);
            } else {
                DeleteEmployeesDetails(allEmployeesDetails[i].userAccessProfileId, allEmployeesDetails[i].signumId);
            }
        }
    }
};


//Get Renew Revoke List
function getRenewRevokeView() {
    $('#MsjNodata').text("");
    renewalList = [];
    if ($.fn.dataTable.isDataTable('#renewRevokeTable')) {
        oTable.destroy();
        $('#renewRevokeTable').empty();
    }
    pwIsf.addLayer({ text: "Please wait ..." });
    $('#renewRevokeTable').html("");
    $.isf.ajax({
        url: `${service_java_URL}accessManagement/getRenewRequestsBySignum?signum=${signumGlobal}`,
        success: function (data) {
            data=addActionIconField(data);
            pwIsf.removeLayer();
            $("#renewRevokeTable").append($('<tfoot><tr><th></th><th>Signum/Employee Name</th><th>Profile</th><th>Organisation</th><th>Approved Start Date/Name</th><th>Approved End Date</th></tr></tfoot>'));
            oTable = $("#renewRevokeTable").DataTable({
                searching: true,
                responsive: true,
                colReorder: true,
                order: [1],
                dom: 'Bfrtip',
                buttons: [
                    'colvis', 'excel',
                    {
                        text: 'Clear Selection',
                        action: function () {
                            clearSelection();
                        }
                    }
                ],
                "pageLength": 10,
                "data": data,
                "destroy": true,
                "drawCallback": function (settings) {

                    $("#chk_selectAll_action").prop('checked', $('.chk_renew_revoke').prop('checked'))

                },
                "columns": [
                    {
                        "title": "Actions",
                        "targets": 'no-sort',
                        "orderable": false,
                        "searchable": false,
                        "data": "actionIcon"
                    },
                    {
                        "title": "Signum/Employee Name", "data": "signumId",
                        "render": function (data,type, row, meta) {
                            return `<a href="#" style="color:blue;" title="Click here to see User Details!" onclick="getEmployeeDetailsBySignum_View('${row.signumId}')">${row.signumId}/${row.employeeName}</a>`;
                        }
                    },
                    { "title": "Profile", "data": "accessProfileName" },
                    { "title": "Organisation", "data": "organisation" },
                    { "title": "Approved Start Date", "data": "startDate" },
                    { "title": "Approved End Date", "data": "endDate" }],
                initComplete: function () {
                    addColumnSearch();
                    var api = this.api();
                    api.columns().every(function () {
                        var that = this;
                        $('input', this.footer()).on('keyup change', function (e) {
                            if (that.search() !== this.value && (e !== undefined && e.target !== undefined && e.target.type !== "checkbox")) {
                                that
                                    .columns(`${$(this).parent().index()} :visible`)
                                    .search(this.value)
                                    .draw();
                            }
                        });
                    });
                }
            });
            $('#renewRevokeTable tfoot').insertAfter($('#renewRevokeTable thead'));
        },
        error: function () {
            pwIsf.removeLayer();
            $('#MsjNodata').text("No data exists!");
        }
    });

}
function addActionIconField(apiResponse)
{
    if (profileId === 40 || profileId === 67) {
        $.each(apiResponse, function (i, d) {
            d.actionIcon = `<div style="display:flex;" onclick="getUserDetails('${d.accessProfileName}',${d.userAccessProfileId},'${d.signumId}')">
                                        <input type="checkbox" id="chk_RenewEmpDetails_${i}" class="chk_renew_revoke" onclick="CheckboxClick(${i},'${d.accessProfileName}',${d.userAccessProfileId},'${d.signumId}')"></input>
                                        <a class="icon-delete lsp" title="Revoke Access" href="#"  onclick="revokeUserAccess(${d.signum})">${getIcon('reinstate')}</a></div>`;
        });
    }
    else {
        $.each(apiResponse, function (i, d) {
            d.actionIcon = `<div style="display:flex;" onclick="getUserDetails('${d.accessProfileName}',${d.userAccessProfileId},'${d.signumId}')">
                            <input type="checkbox" id="chk_RenewEmpDetails_${i}" class="chk_renew_revoke" onclick="CheckboxClick(${i},'${d.accessProfileName}',${d.userAccessProfileId},'${d.signumId}')"></input>
                                <a class="icon-edit" title="Renew User Access" href="#" data-toggle="modal" data-target="#updateUserAccess">${getIcon('edit')}</a>
                                <a class="icon-delete lsp" title="Revoke Access" href="#"  onclick="revokeUserAccess('${d.signum}')">${getIcon('reinstate')}</a></div>`;
        });
    }
    return apiResponse;
}
function addColumnSearch() {
    $('#renewRevokeTable tfoot th').each(function (i) {
        var title = $('#renewRevokeTable thead th').eq($(this).index()).text();
        if (title !== "Actions")
            $(this).html('<input type="text" class="form-control" style="font-size:12px;"  placeholder="Search ' + title + '" data-index="' + i + '" />');
        else if (title === "Actions")
            $(this).html('<div style="display:flex; justify-content: right; margin-left:-7px;"><input style="margin-right:5px;" id="chk_selectAll_action" type="checkbox" onclick="SelectAllCheckBoxes(this)">Select All</div>');
    });
}
function getUserAccessView() {
    pwIsf.addLayer({ text: "Please wait ..." });
    $('#MsjNodata').text("");
    if ($.fn.dataTable.isDataTable('#accessProvideTable')) {
        oTable.destroy();
        $('#accessProvideTable').empty();
    }
    const url = `${service_java_URL}accessManagement/getAccessRequestsBySignum?signum=${signumGlobal}`;
    var count = 0;
    $("#accessProvideTable").append($('<tfoot><tr><th></th><th>Signum/Employee Name</th><th>Profile</th><th>Organisation</th><th>Requested On</th></tr></tfoot>'));
    $('#accessProvideTable tfoot th').each(function () {
        var title = $(this).text();
        $(this).html(`<input type="text" placeholder="Search ${title}" />`);
    });
    oTable = $('#accessProvideTable').DataTable({
        "processing": true,
        "serverSide": true,
        searching: true,
        responsive: true,
        destroy: true,
        "pageLength": 10,
        colReorder: true,
        "order": [[1, "asc"]],
        dom: 'Brtip',
        buttons: ['colvis', {
            "text": 'Excel',
            "sClass": "dt-button buttons-excel buttons-html5",
            action: function (e, dt, node, config) {
                downloadExcelReport();
            }
        }],
        "ajax": {
            "headers": commonHeadersforAllAjaxCall,
            "url": url,
            "type": "POST",
            "complete": function (response) {
                if (response != undefined && response.responseJSON != undefined && response.responseJSON.data != undefined && response.responseJSON.data.length == 0) {
                    $('#btnApprovedMenu0').css({ 'display': 'none' });
                    $('#btnRejectedMenu0').css({ 'display': 'none' });
                    $("#accessProvideTable tfoot :input[type=checkbox]").attr("disabled", true);
                } else {
                    $('#btnApprovedMenu0').css({ 'display': 'block' });
                    $('#btnRejectedMenu0').css({ 'display': 'block' });
                    $("#accessProvideTable tfoot :input[type=checkbox]").attr("disabled", false);
                }
                pwIsf.removeLayer();
            },
            "error": function () {
                pwIsf.removeLayer();
                $('#accessProvideTable').hide();
            }
        },
        "columns": [
            {
                "title": "Actions",
                "targets": 'no-sort',
                "orderable": false,
                searching: false,
                "data": null,
                "defaultContent": "",
                "render": function (data, type, row, meta) {
                    count++;
                    return getChildActionOfuserAccessTable(count, row);
                }
            },
            {
                "title": "Signum/Employee Name",
                "data": "EmployeeName",
                "searchable": true,
                "render": function (data, type, row, meta) {
                    return `<a href="#" style="color:blue;" title="Click here to see User Details!" onclick="getEmployeeDetailsBySignum_View('${row.signum}')">${row.signum}/${row.employeeName}</a>`;
                }
            },
            {
                "title": "Profile",
                "searchable": true,
                "data": "accessProfileName"
            },
            {
                "title": "Organisation",
                "searchable": true,
                "data": "organisation"
            },
            {
                "title": "Requested Date",
                "searchable": true,
                "data": "createdOn"
            }
        ],
        initComplete: function () {
            addColumnSearchAccessTable();
            var api = this.api();
            api.columns().every(function () {
                var that = this;
                $('input', this.footer()).on('keyup change', function (e) {
                    if (that.search() !== this.value && (e !== undefined && e.target !== undefined && e.target.type !== "checkbox")) {

                        that
                            .columns(`${$(this).parent().index()}:visible`)
                            .search(this.value)
                            .draw();
                    }
                });

            });
        }
    });
    $('#accessProvideTable tfoot').insertAfter($('#accessProvideTable thead'));
    //oTable.draw();
}
function addColumnSearchAccessTable() {
    $('#accessProvideTable tfoot th').each(function (i) {
        var title = $('#accessProvideTable thead th').eq($(this).index()).text();
        if (title !== "Actions") {
            $(this).html(`<input type="text" class="form-control" style="font-size:12px;"  placeholder="Search ${title}" data-index="${i}" />`);
        }
        if (title === "Actions") {
            $(this).html('<div style="display:flex; justify-content: right; margin-left:-7px;"><input  id="chkApproveRejectHeader" type="checkbox" class="newcheckbox" style="margin-top:0px" onclick="chkApproveRejectHeaderCheckBoxe(this)">&nbsp;Select All</div>');
        }
    });
}
/*------------------------Access ITSA Renew Revoke Ends-------------------------------*/

function getChildActionOfuserAccessTable(index, d) {
    return `<div style="display:flex;" >
        <input type="checkbox"  id="chkChildApproveRejects${index}"  class="newcheckbox"  onclick="chkApproveRejectChildCheck('${index}','${d.accessProfileName}',${d.accessProfileID},'${d.signum}')">&nbsp;&nbsp;</input>
        <a class="icon-add" id="addIcon${index}" title = "Approve User" href="#" onclick="singleApproved('${index}','${d.accessProfileName}',${d.accessProfileID},'${d.signum}','approve')" >${getIcon('accept')}</a>
          <a class="icon-delete lsp" id="rejectIcon${index}" title="Reject User" href="#" data-toggle="modal" data-target="#rejectIndividualUserModal"  onclick="singleReject('${index}','${d.accessProfileName}',${d.accessProfileID},'${d.signum}','reject')" >${getIcon('reject')}</a>
        </div>`;  
}

function clearAllUserAccessCollection() {
    approveList.splice(0, approveList.length);
    singleRejectArr.splice(0, singleRejectArr.length);
    singleApproveArr.splice(0, singleApproveArr.length);
}

function chkApproveRejectHeaderCheckBoxe(e) {
    clearAllUserAccessCollection();
    const prpCheck = $('#chkApproveRejectHeader').prop('checked');
    $("#accessProvideTable .newcheckbox").prop('checked', prpCheck);
    const table = $('#accessProvideTable').DataTable();
    const allEmployeesDetails = table.rows().data().toArray();
    let apRqst;
    if (allEmployeesDetails != undefined && allEmployeesDetails.length > 0) {
        for (apRqst of allEmployeesDetails) {
            if ($("#chkApproveRejectHeader").prop('checked') === true) {
                saveEmpProfileDetailsToCollection(apRqst.accessProfileName, apRqst.accessProfileID, apRqst.signum);
            }
            else {
                removeEmpProfileDetailsFromCollection(apRqst.accessProfileID, apRqst.signum);
            }
        }
    }
};
// Add or delete employee details on selecting or deselecting checkboxes.
function chkApproveRejectChildCheck(index, profileName, userprofileId, signum) {

    if ($(`#chkChildApproveRejects${index}`).prop('checked') === true) {
        saveEmpProfileDetailsToCollection(profileName, userprofileId, signum);
    }
    else {
        removeEmpProfileDetailsFromCollection(userprofileId, signum);
    }
}

function singleApproved(index, profileName, userprofileId, signum, type) {
    // clear all data from collection.

    if (userprofileId != undefined) {
        singleRejectArr.splice(0, singleRejectArr.length);
        singleApproveArr.splice(0, singleApproveArr.length);

        var obj = { "profileName": profileName, "profileId": userprofileId, "signum": signum, "typeOfData": type };

        if (singleApproveArr != undefined && singleApproveArr.length == 0) {
            singleApproveArr.push(obj);
        }
        else if (singleApproveArr != undefined && singleApproveArr.length > 0) {
            let alreadyExistsObj = singleApproveArr.find(temp => temp.profileId === userprofileId && temp.signum === signum);

            if (alreadyExistsObj == undefined) {
                singleApproveArr.push(obj);
            }
        }
    }
    // For ISF Access Manager, MDM and EU no Approval popup should be dispalyed   
    if (userprofileId === 67 || userprofileId === 31 || userprofileId === 32 || userprofileId === 33 || userprofileId === 34 || userprofileId === 35 || userprofileId === 38 || userprofileId === 41) {
        updateUserStatus(C_SINGLEAPPROVEDSTATUS, "", false);
    }
    else {
        var today = formatDate(new Date());
        var endDate90 = formatDate(new Date().setDate(new Date().getDate() + 90));
        $('#approveSingleUserStartDate').val(today);
        $('#approveSingleUserEndDate').val(endDate90);
        $('#approveSingleUserModal').modal('show');
       
       

    }

    return false;

}


function singleReject(index, profileName, userprofileId, signum, type) {
    // clear all data from collection.
    $("#rejectIndividualReasonText").val('');
    singleRejectArr.splice(0, singleRejectArr.length);
    singleApproveArr.splice(0, singleApproveArr.length);
    if (userprofileId != undefined) {
        var obj = {
            "profileName": profileName,
            "profileId": userprofileId,
            "signum": signum,
            "typeOfData": type
        };

        if (singleRejectArr != undefined && singleRejectArr.length == 0) {
            singleRejectArr.push(obj);
        }
        else if (singleRejectArr != undefined && singleRejectArr.length > 0) {
            let alreadyExistsObj = singleRejectArr.find(temp => temp.profileId === userprofileId && temp.signum === signum);

            if (alreadyExistsObj == undefined) {
                singleRejectArr.push(obj);
            }
        }
    }
    return false;
}



function removeEmpProfileDetailsFromCollection(userprofileId, signum) {
    if (userprofileId !== undefined) {
        if (approveList !== undefined && approveList.length > 0) {
            let index = approveList.findIndex(temp => temp.profileId === userprofileId && temp.signum === signum);
            if (index !== -1) {
                approveList.splice(index, 1);
            }
        }
    }
}
function saveEmpProfileDetailsToCollection(profileName, userprofileId, signum, type) {
    if (userprofileId !== undefined) {
        var obj = {
            "profileName": profileName,
            "profileId": userprofileId,
            "signum": signum,
            "typeOfData": type
        };

        if (approveList !== undefined && approveList.length === 0) {
            approveList.push(obj);
        }
        else if (approveList != undefined && approveList.length > 0) {
            let alreadyExistsObj = approveList.find(temp => temp.profileId === userprofileId && temp.signum === signum);
            if (alreadyExistsObj == undefined) {
                approveList.push(obj);
            }
        }
    }
}

function downloadExcelReport() {
    var activeProfileObj = JSON.parse(ActiveProfileSession);
    role = activeProfileObj.role;
    var params = [
        `signum=${signumGlobal}`,
        `role=${role}`
    ];

    pwIsf.addLayer({ text: 'Please wait ...' });
    var urlSearch = params.join('&');
    let urlEncode = encodeURIComponent(urlSearch);
    var serviceUrl = `${service_java_URL}accessManagement/downloadProfileAccessPageDataInExcel?${urlEncode}`;
    var fileDownloadUrl;
    fileDownloadUrl = `${UiRootDir}/data/GetFileFromApi?apiUrl=${serviceUrl}`;
    window.location.href = fileDownloadUrl;
    pwIsf.removeLayer();
}