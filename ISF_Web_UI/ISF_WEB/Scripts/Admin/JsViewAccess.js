var dTable;
var selectedProfile;
$(document).ready(function () {
    getAllRoles();
    getAllOrganizations();
    $('#dtAccessProfile').on('draw.dt', function () {       
        $('#revokeAllAccessProfile').prop('checked', false);
    });
   

});

$(document).on("click", "#revokeAllAccessProfile", function () {
    $(".chkRevokeAccessProfile").prop('checked', $(this).prop('checked'));
});

function getAllRoles() {
    $('#role')
        .find('option')
        .remove();
    $("#role").select("val", "");
    $('#role').append('<option value="0">Select one</option>');
    $.isf.ajax({
        url: service_java_URL + "accessManagement/getRoleList",
       
        success: function (data) {
 
            $.each(data, function (i, d) {
                $('#role').append('<option value="' + d.accessRoleID + '">' + d.role + '</option>');
            });

        },
        error: function (xhr, status, statusText) {
            console.log('An error occurred on getAllRoles: ' + xhr.error);
        }
    });

}

function getAllOrganizations() {

    $('#organization')
        .find('option')
        .remove();
    $("#organization").select("val", "");
    $('#organization').append('<option value="0">Select one</option>');
    $.isf.ajax({
        url: service_java_URL + "accessManagement/getOrganizationList",
        success: function (data) {

            $.each(data, function (i, d) {
                $('#organization').append('<option value="' + d.organisationID + '">' + d.organisation + '</option>');
            });

        },
        error: function (xhr, status, statusText) {
            console.log('An error occurred on getAllOrganizations: ' + xhr.error);
        }
    });

}

function ValidateSearchAll() {
    var signum = document.getElementById("signumList").value;
    var role = document.getElementById("role").value.split('/');
    var roleId = role[0];
    var organization = document.getElementById("organization").value.split('/');
    var organizationId = organization[0];
    var lineManager = document.getElementById("lineManagerList").value;
    if (signum == '' && roleId == 0 && organizationId == 0 && lineManager == '') {
        // atleast one is mandatory
        pwIsf.alert({ msg: "Please select atleast one!", type: "warning" });
    }

    else {

        // case: signum and LineManager are not selected
        if (signum == '') {
            signum = 'All';
        }

        if (lineManager == '') {
            lineManager = 'All';
        }

        ShowResults(signum, roleId, organizationId, lineManager);
    }
}


function ShowResults(signum, role, organization, lineManager) {
  //  var oTable = $('#dtAccessProfile').DataTable();
    if ($.fn.dataTable.isDataTable("#dtAccessProfile")) {
        oTable.destroy();
        $('#dtAccessProfile').empty();
    }
    var ServiceUrl = service_java_URL + "accessManagement/searchAccessDetailsByFilter/" + signum + "/" + role + "/" + organization + "/" + lineManager;
    // To add footer to Datatable

    $("#dtAccessProfile").append($('<tfoot><tr><th></th><th>Signum</th><th>Access Profile Name</th><th>Role</th><th>Access Organization</th><th>Line Manager</th><th>Access Expiry</th><th>Approved By</th></tr></tfoot>'));
    oTable = $('#dtAccessProfile').on('page.dt', function ()
    {
        $("#revokeAllAccessProfile").prop('checked', false);
    }).DataTable({
        "searching": true, //to enable searching in entire Data table
        "processing": true, //paging
        "serverSide": true, //paging
        responsive: true,
        
        "ajax": {
            "headers": commonHeadersforAllAjaxCall,
            "url": ServiceUrl,
            "type": "POST",
            "complete": function (response) {
                disableEnableSelectAllAndShowHideRevoke(response);
            },
            error: function (xhr, status, statusText) {
                $('#div_table').hide();
            }
        },
        "pageLength": 10, //length of records
        "destroy": true,
         colReorder: true,
        order: [[2, "desc"]],
        dom: 'Brtip',
        buttons: [
            //for column visibility
            'colvis', {
                "text": 'Export', // for excel
                "sClass": "dt-button buttons-excel buttons-html5",
                action: function (e, dt, node, config) {
                    DownloadFile(signum, role, organization, lineManager);
                }
            }],

        "columns": [
            {
                "title": "Action",
                "targets": 'no-sort',
                "orderable": false,
                "searching": false,
                "data": null,
                "render": function (response, type, row, count) {
                    count++
                    return '<input type="checkbox" class="chkRevokeAccessProfile" title="Revoke Access Profile" id="revokeAccessProfile' + count + '" value="' + response.signumID + '/' + response.accessProfileID + '/' + response.approvedBy + '" ><a class="icon-delete lsp" title="Revoke Access Profile" onclick="revokeIconRevokeClickedViewAccessProfile(\'' + response.signumID + '\',' + response.accessProfileID + ',\'' + response.approvedBy + '\')">' + getIcon('reopen') + '</a>'
                }
            },
            {
                "title": "Signum",
                "data": "signumID",
                "searchable": true
            },

            {
                "title": "Access Profile Name",
                "data": "accessProfileName",
                "searchable": true
            },
            {
                "title": "Role",
                "data": "role",
                "searchable": true
            },
            {
                "title": "Access Organization",
                "data": "organisation",
                "searchable": true
            },
            {
                "title": "Line Manager",
                "data": "managerSignum",
                "searchable": true
            },
            {
                "title": "Access Expiry",
                "data": "endDate",
                "searchable": true
            },
            {
                "title": "Approved By",
                "data": "approvedBy",
                "searchable": true
            }
        ],
        initComplete: function () {
            addFieldsInFooter();  // To append Search and Select all in footer
            var api = this.api();
            colSearch(api);   // For Column Search and Disable col Search for Action Column
        }
    });
    $('#dtAccessProfile tfoot').insertAfter($('#dtAccessProfile thead'));
    oTable.draw();
}

// disable checkbox Visibillity button and hide Revoke button in case there is no data

function disableEnableSelectAllAndShowHideRevoke(response) {
    if (response != undefined && response.responseJSON != undefined && response.responseJSON.data != undefined && response.responseJSON.data.length == 0) {
        $("#dtAccessProfile tfoot :input[type=checkbox]").attr("disabled", true);
        $('#revoke').hide();
    }
    else {
        $('#revoke').show();
        $("#dtAccessProfile tfoot :input[type=checkbox]").attr("disabled", false);
    }
}

// Add Search Box and Select all Check Box in footer as per header of Datatable

function addFieldsInFooter() {
    $('#dtAccessProfile tfoot th').each(function (i) {
        var title = $('#dtAccessProfile thead th').eq($(this).index()).text();
        if (title == "Action") {
            $(this).html('<div style="display:flex;"><input type="checkbox" title="Revoke All Access Profile" id="revokeAllAccessProfile"><div class="col-lg-2" style="margin-bottom: 10px;margin-left: 2px; text-align: right;">Select All </div></div>');
        }
        else {
            $(this).html('<input type="text" class="form-control" style="font-size:12px;"  placeholder="Search ' + title + '" data-index="' + i + '" />');
        }
    });
}

// Server Side Searching and Disable Searching on Checkbox

function colSearch(api) {
  
    api.columns().every(function () {
        var that = this;
        $('input', this.footer()).on('keyup change', function (e) {
            if (that.search() !== this.value && (e != undefined && e.target != undefined && e.target.type != "checkbox")) {
                that
                    .columns($(this).parent().index() + ':visible')
                    .search(this.value)
                    .draw();
            }
        });
    });
}

// download Excel File

function DownloadFile(signum, role, organization, lineManager) {
    pwIsf.addLayer({ text: 'Please wait ...' });
    let serviceUrl = `${service_java_URL}accessManagement/downloadViewAccessPageDataInExcel/${signum}/${role}/${organization}/${lineManager}`;
    let fileDownloadUrl;
    fileDownloadUrl = UiRootDir + "/data/GetFileFromApi?apiUrl=" + serviceUrl;
    window.location.href = fileDownloadUrl;
    pwIsf.removeLayer();
}


function test_vars(infoo) {
    var partsOfStr = infoo.split(',');
    var signum = partsOfStr[0];
    var userAccessProfileID = partsOfStr[1];
    var role = partsOfStr[2];
    var org = partsOfStr[3];
    modalTableData(signum, userAccessProfileID, role, org);
}

function modalTableData(signum, userAccessProfileID, role, org) {
    var userAccessProfileId = 2;
    var roleId = 1;
    var orgId = 2;
    var ServiceUrl = service_java_URL + "accessManagement/searchAccessDetails/" + userAccessProfileId + "/" + roleId + "/" + orgId + "";
    $("#tbodyDatatable2 tr").remove();
    $.isf.ajax({
        url: ServiceUrl,
        success: function (data) {
            var html = "";
            $.each(data, function (x, d) {

                var len = d.lstAccessUserProfile.length;
               
                for (var i = 0; i < len; i++) {
                    html = html + "<tr class='odd'>";
                    html = html + "<td class='check hidden-xs  sorting_1'><input type='checkbox' name='check[]'/></td>";
                    html = html + "<td>" + d.lstAccessUserProfile[i].signumID + "</td>";
                    html = html + "<td>" + d.lstAccessUserProfile[i].employeeName + "</td>";
                    html = html + "<td>" + d.organisation + "</td>";// 
                    html = html + "<td>" + d.lstAccessUserProfile[i].managerSignum + "</td>";
                    html = html + "<td>" + d.lstAccessUserProfile[i].employeeEmailID + "</td>";
                    html = html + "<td>" + d.lstAccessUserProfile[i].approvedBy + "</td>";//
                    html = html + "<td>" + d.lastModifiedBy + "</td>";
                    html = html + "<td>" + d.lastModifiedBy + "</td>";
                    html = html + "<td>" + d.lastModifiedOn + "</td>";
                    html = html + '<td><i style="cursor: pointer;" onclick="showModalInactive(\'' + d.lstAccessUserProfile[i].signumID + ',' + d.accessProfileID + ',' + userAccessProfileId + '\')" class="fa fa-tasks"></i></td>';
                    html = html + "</tr>";
                }
            });
            $("#tbodyDatatable2").append(html);
            $('#dataTable2').DataTable({
                retrieve: true,
                paging: false
            });

            $('#modal_Table').modal('show');

        },
        error: function (xhr, status, statusText) {
            console.log('An error occurred');
        }
    });
}

function showModalInactive(info2) {
    var partsOfStr = info2.split(',');
    var signumID = partsOfStr[0];
    var accessProfileID = partsOfStr[1];
    var userAccessProfileID = partsOfStr[2];

    $('#modal_Table').modal('hide');
    $('#modalMarkInactive').modal('show');
    $("#woID_MarkInactive").val(signumID);
    $("#accessProfileID").val(accessProfileID);
    $("#userAccessProfileID").val(userAccessProfileID);

}

function MarkInactive() {
    var woID_MarkInactive = document.getElementById("woID_MarkInactive").value;
    var accessProfileID = document.getElementById("accessProfileID").value;
    var userAccessProfileID = document.getElementById("userAccessProfileID").value;
    $.isf.ajax({
        url: service_java_URL + "accessManagement/disableAccessToUser/" + userAccessProfileID + "/" + accessProfileID + "/" + woID_MarkInactive + "",
        context: this,
        crossdomain: true,
        processData: true,
        contentType: 'application/json',
        type: 'POST',
        xhrFields: {
            withCredentials: false
        },
        success: AjaxSucceeded,
        error: AjaxFailed
    });
    function AjaxSucceeded(data, textStatus) {
        $('#messageOK_MarkInactive').show();
        document.getElementById('contentMarkInactive').style.display = 'none';
        document.getElementById('MarkInactive').style.display = 'none';
    }
    function AjaxFailed(result) {
        alert("Error");
    }
}

function refreshPage() {
    location.reload();
}

// Method called on click of revoke Icon

function revokeIconRevokeClickedViewAccessProfile(signumID, accessProfileID, approvedBy) {
    var obj = { "signumId": signumID, "approvedBy": approvedBy, "accessProfileId": accessProfileID };
    var approveData = [];
    approveData.push(obj);
    var data = [];
    data = approveData;
    revokeViewAccessProfile(data);
}

// On click of Revoke button to revoke checked profile/profiles
function revokeCheckedViewAccessProfile() {
    var data = [];
    data = getDataRevokeCheckedViewAccessProfile(); // To fetch checked rows data
    revokeViewAccessProfile(data);   // To revoke Profile/profiles (API call)  
}

// Get data from checked checkbox required for revoke

function getDataRevokeCheckedViewAccessProfile() {
    var chkArray = [];
    $(".chkRevokeAccessProfile:checked").each(function () {
        var valuesAppendedToCheckBox = $(this).val().split('/');
        var signumId = valuesAppendedToCheckBox[0];
        var accessProfileId = valuesAppendedToCheckBox[1];
        var approvedBy = valuesAppendedToCheckBox[2];
        var obj = { "signumId": signumId, "approvedBy": approvedBy, "accessProfileId": accessProfileId };
        chkArray.push(obj);
    });
    if (chkArray.length > 0) {
        return chkArray;
    } else {
        return 0;
    }
}

// revoke profile based on data provided

function revokeViewAccessProfile(selectedData) {
    if (selectedData != 0) {
       
        if (selectedData.length != 1) {
            selectedProfile = 'Profiles';
        }
        else {
            selectedProfile = 'Profile';
        }
        var JsonObj = JSON.stringify(selectedData);
        pwIsf.confirm({
            title: 'Revoke Access Profile', msg: 'Are you sure to revoke selected Access' + " " + selectedProfile + '?',
            'buttons': {
                'Yes': {
                    'action': function () {
                        ajaxCallOnYesButtonClick(JsonObj);
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
        pwIsf.alert({ msg: "Please select at least one profile!" });
        pwIsf.removeLayer();
    }
}

// Ajax ApI Call to Revoke profile/Profiles on click of yes button
function ajaxCallOnYesButtonClick(JsonObj) {   
    pwIsf.addLayer({ text: 'Please wait ...' });
    $.isf.ajax({
        url: service_java_URL + 'accessManagement/deleteAccessProfileBySignum',
        context: this,
        crossdomain: true,
        processData: true,
        contentType: 'application/json',
        type: 'POST', //check the type of API
        data: JsonObj,
        xhrFields: {
            withCredentials: false
        },
        crossDomain: true,
        success: function (data) {

            responseHandler(data);
            $("#dtAccessProfile").dataTable().fnDraw();
        },
        error: function (xhr, status, statusText) {
            pwIsf.removeLayer();
            console.log('An error occurred');

        },
        complete: function (xhr, statusText) {
            pwIsf.removeLayer();
        }
    });
}


// Clear Selected All checkboxes on Renew/Revoke Table
function ClearSelection() {
  
    var table = $('#renewRevokeTable').DataTable();
    table.rows().nodes().to$().find('input[type="checkbox"]').each(function () {
        $(this).prop('checked', false);

    });

    $("#chk_selectAll_action").prop('checked', false);
}
