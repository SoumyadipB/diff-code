//Nahar singh vaibhav test
$(document).ready(function () {

    selectOrganizationID();
    selectAccessRoleID();
    getAccessProfile();
    $('#editAccessProfile').on('show.bs.modal', function () {
        $(this).find('.modal-content').css({
            width: 'auto', //probably not needed
            height: 'auto', //probably not needed
            'max-width': '50%',
            'margin': 'auto'
        });
    });
});

const ACCESSROLEID = '#AcessRoleId';
const ORGID = '#OrganizationID';
const APPLICATIONJSON = 'application/json';
const ACCESSMGMTGETACCESSPROFILEAPI = "accessManagement/getAccessProfiles";
const ACCESSPROFILETABLEID = '#Access_Profile_table';
//Add Role Function
function addAccessProfile() {
    var AccessProfileName = $('#ProfileName').val();
    var AccessRole = $(ACCESSROLEID).val();
    var Organization = $(ORGID).val();
    pwIsf.addLayer({ text: WAITMSG });
    $.isf.ajax({
        url: `${service_java_URL}accessManagement/createNewAccessProfile/${AccessProfileName}/${AccessRole}/${Organization}`,
        context: this,
        crossdomain: true,
        processData: true,
        contentType: APPLICATIONJSON,
        type: 'POST',
        xhrFields: {
            withCredentials: false
        },
        success: function (data) {
            pwIsf.alert({ msg: "Successfuly Added", type: "success" });
        },
        error: function (xhr, status, statusText) {
            pwIsf.alert({ msg: "Error while changing status", type: "warning" });
        },
        complete: function (xhr, statusText) {
            pwIsf.removeLayer();
        }
    });

}



// Get All Organization
function selectOrganizationID() {


    $.isf.ajax({
        url: service_java_URL + ACCESSMGMTGETACCESSPROFILEAPI,
        success: function (data) {
            $(ORGID).empty();
            $(ORGID).append(`<option value="0"</option>`);
            var orgArr = [];
            $.each(data, function (i, d) {
                //Fill distinct organization list
                if (!orgArr.includes(d.organisationID)) {
                    orgArr.push(d.organisationID);
                    $(ORGID).append(`<option value="${d.organisationID}">${d.organisation} </option>`);

                }

            })
        },
        error: function (xhr, status, statusText) {
            console.log('An error occurred on get organization ID');
        }
    });
}

//Update for Organization
function selectOrganizationIDforupdate() {
    $.isf.ajax({
        url: service_java_URL + ACCESSMGMTGETACCESSPROFILEAPI,
        success: function (data) {
            $('#OrganizationID1').append(`<option value="0"></option>`);
            $.each(data, function (i, d) {
                $('#OrganizationID1').append(`<option value=" ${d.organisationID}">${d.organisation} </option>`);
            })
        },
        error: function (xhr, status, statusText) {
            console.log('An error occurred on get organization ID');
        }
    });

}

//Update for Access Role
function selectAccessRoleIDforupdate() {
    $.isf.ajax({
        url: service_java_URL + "accessManagement/getRoleList",
        success: function (data) {
            $('#AcessRoleId1').append(`<option value="0"></option>`);
            $.each(data, function (i, d) {
                $('#AcessRoleId1').append(`<option value="${ d.accessRoleID}">${d.role}</option>`);
            })
        },
        error: function (xhr, status, statusText) {
            console.log('An error occurred on get Access Profile ID');
        }
    });
}
// Get All Access role
function selectAccessRoleID() {
    $.isf.ajax({
        url: service_java_URL + "accessManagement/getRoleList",
        success: function (data) {
            $(ACCESSROLEID).empty();
            $(ACCESSROLEID).append(`<option value="0"></option>`);
            $.each(data, function (i, d) {
                $(ACCESSROLEID).append(`<option value="${d.accessRoleID}"> ${d.role}</option>`);

            })
        },
        error: function (xhr, status, statusText) {
            console.log('An error occurred on get Access Profile ID');
        }
    });

}

// Get all Access role in form a datatable
function getAccessProfile() {
    if ($.fn.dataTable.isDataTable(ACCESSPROFILETABLEID)) {
        oTable.destroy();
        $(ACCESSPROFILETABLEID).empty();
    }
    pwIsf.addLayer({ text: WAITMSG });
    $.isf.ajax({
        url: service_java_URL + ACCESSMGMTGETACCESSPROFILEAPI,

        context: this,
        crossdomain: true,
        processData: true,
        contentType: APPLICATIONJSON,
        type: 'GET',
        xhrFields: {
            withCredentials: false
        },

        success: function (data) {
            pwIsf.removeLayer();
            oTable = $(ACCESSPROFILETABLEID).DataTable({
                searching: true,
                responsive: true,
                "pageLength": 20,
                "data": data,
                "destroy": true,
                colReorder: true,
                dom: 'Bfrtip',
                buttons: [
                    'colvis', 'excelHtml5'
                ],
                "columns": [
                    {
                        "title": "Action", "targets": 'no-sort', "orderable": false, "searchable": false, "data": null,
                        "defaultContent": "",
                        "render": function (dt, type, row, meta) {
                            const jsonData = JSON.stringify({
                                accessprofileID: dt.accessProfileID, accessprofileName: dt.accessProfileName, roleID1: dt.roleID,
                                roleName: dt.role, organisationID: dt.organisationID, organisationName: dt.organisation,
                            });
                            return `<div style="display:flex"><a class="icon-edit lsp" href="#editAccessProfile" 
data-details= '${jsonData}' title="Edit Capability"  onclick="updateAccessProfileMethod(this);" data-toggle="modal"><i class="fa fa-edit"></i></a>
                        <a class="icon-delete lsp" href="#" title="Delete Profile" onclick="deleteProfile(\' ${ dt.accessProfileID}\')" data-toggle="modal">
                        <i class="fa fa-trash"></i></a></div>`;
                        }
                    },
                    {
                        "title": "Access Profile", "data": null, "searchable": true,
                        "render": function (res, type, row, meta) {
                            return `${res.accessProfileID}/${res.accessProfileName}`;
                        }
                    },
                    {
                        "title": "Role", "data": null,
                        "render": function (response, type, row, meta) {
                            return `${response.roleID}/${response.role}`;
                        }
                    },
                    {
                        "title": "Organization", "data": null,
                        "render": function (responseData, type, row, meta) {
                            return `${responseData.organisationID}/${responseData.organisation}`;
                        }
                    },
                    {
                        "title": "Status", "data": null,
                        "render": function (rd, type, row, meta) {
                            return rd.active;
                        }
                    },
                ],
                initComplete: function () {

                    $(`${ACCESSPROFILETABLEID} tfoot th`).each(function (i) {
                        var title = $(`${ACCESSPROFILETABLEID} thead th`).eq($(this).index()).text();
                        if (title !== "Action") {
                            $(this).html(`<input type="text" class="form-control" style="font-size:12px;" placeholder="Search ${title}" data-index="${i}" />`);
                        }
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
            $(`${ACCESSPROFILETABLEID} tfoot`).insertAfter($(`${ACCESSPROFILETABLEID} thead`));
        },
        error: function (xhr, status, statusText) {
            console.log('An error occurred');
        }
    });


}

// Delete Profile
function deleteProfile(accessprofileID) {
    pwIsf.confirm({
        title: 'Delete Page', msg: "Are you sure you want to delete the Profile?",
        'buttons': {
            'Yes': {
                'action': function () {
                    pwIsf.addLayer({ text: WAITMSG});
                    $.isf.ajax({
                        url: `${service_java_URL}accessManagement/deleteAccessProfile/${accessprofileID}`,
                        context: this,
                        crossdomain: true,
                        processData: true,
                        contentType: APPLICATIONJSON,
                        type: 'POST',
                        xhrFields: {
                            withCredentials: false
                        },
                        success: function (data) {

                            if (data.apiSuccess) {
                                pwIsf.alert({ msg: "successfully Deleted.", type: "success" });
                            }
                            location.reload();
                        },
                        error: function (xhr, status, statusText) {
                            pwIsf.alert({ msg: "Error in Deleting", type: 'warning' });
                        },
                        complete: function (xhr, statusText) {
                            pwIsf.removeLayer();
                        }
                    });
                }
            },
            'No': { 'action': function () { pwIsf.removeLayer(); } },

        }
    });

}

// Update Access Profile
function updateAccessProfileMethod(thisObj) {
    selectOrganizationIDforupdate();
    selectAccessRoleIDforupdate();
    const jsonData = $(thisObj).data('details');
    console.log(jsonData);
    var html = `
                         <div class="col-lg-12">
                        Profile Name<a class="text-danger">*</a>: <input class="form-control required " placeholder="Input Profile Name"
id="ProfileName1" name="Profile" type="text"value="${jsonData.accessprofileName}" required />
<input type="hidden" id="accessProfile_ID" name="accessProfileID" value="${jsonData.accessprofileID}"/>

                    </div>
                     <div class="col-lg-12">
                        Access Role:<br />
                        <select class="select2able select2-offscreen" id="AcessRoleId1">
                             <option value="${jsonData.roleID1}">${jsonData.roleName}</option>
                        </select>
                    </div>
                    <div class="col-lg-12">
                        Organization:<br />
                        <select class="select2able select2-offscreen" id="OrganizationID1">
                            <option value="${jsonData.organisationID}">${jsonData.organisationName}</option>
                        </select>
                    </div>

                    <div class="col-lg-3">
                        <button id="btnUpdateAccessProfile" type="submit" class="btn btn-success pull right" onclick="updateAccessProfilefinal()">+ Update Access Profile</button>

                    </div>
                    `;

    $('#updateAccessProfilemodal').html('').append(html);

}




// API to update Access Profile
function updateAccessProfilefinal() {
    var AccessRole = new Object();

    AccessRole.accessProfileID = $("#accessProfile_ID").val();
    AccessRole.accessProfileName= $("#ProfileName1").val();
    AccessRole.roleID = $('#AcessRoleId1 option:selected').val();
    AccessRole.organisationID = $('#OrganizationID1 option:selected').val();
    $.isf.ajax({
        url: service_java_URL + 'accessManagement/editAccessProfile',
        type: 'POST',
        crossDomain: true,
        context: this,
        contentType: APPLICATIONJSON,
        data: JSON.stringify(AccessRole),

        success: function (returndata) {
            alert("Access Profile Updated");
            location.reload();

        },
        error: function (xhr, status, statusText) {
            pwIsf.alert({
                msg: `Access Profile Update Failed.Please check all fields are filled and no field is  same as previous. 
If still getting an error, kindly contact technical team.`, type: 'error'
            });
        },
    });
    return false;
}
