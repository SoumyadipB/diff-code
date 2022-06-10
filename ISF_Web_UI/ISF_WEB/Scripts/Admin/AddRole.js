$(document).ready(function () {
    getRoles();
 
    $('#roleAdd').on('show.bs.modal', function () {
        updateModal(this);
    });
    $('#updateRole2').on('show.bs.modal', function () {
        updateModal(this);
    });
    $('#deleteRole').on('show.bs.modal', function () {
        updateModal(this);
    });
});

function updateModal(modalId) {
    $(this).find('.modal-content').css({
        width: 'auto', //probably not needed
        height: 'auto', //probably not needed
        'max-width': '50%',
        'margin': 'auto'
    });
}

// Table to View All Roles Available
function getRoles() {
   
    if ($.fn.dataTable.isDataTable('#tblRoles')) {
        oTable.destroy();
        $("#tblRoles").empty();
    }
   
    
    pwIsf.addLayer({ text: 'Please wait ...' });
    $.isf.ajax({
        url: service_java_URL + "accessManagement/getRoleList",
        context: this,
        crossdomain: true,
        processData: true,
        contentType: 'application/json',
        type: 'GET',
        xhrFields: {
            withCredentials: false
        },

        success: function (data) {
           
            pwIsf.removeLayer();
         
          

            $("#tblRoles").append($('<tfoot><tr><th></th><th>Role ID</th><th>Role Name</th><th>Role Alias</th><th>Status</th></tr></tfoot>'));
            oTable = $('#tblRoles').DataTable({
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
                        "render": function (d, type, row, meta) {
                            let jsonData = JSON.stringify({
                                roleID: d.accessRoleID, roleName: d.role, roleAlias: d.alias, active: d.active

                            });
                            return '<div style="display:flex"><a class="icon-edit lsp" href="#updateRole2" data-details= \'' + jsonData + '\' title="Edit Role" onclick="editRole(this)" data-toggle="modal" ><i class="fa fa-edit"></i></a><a class="icon-delete lsp" href="#" title="Delete Capability" onclick="deleteRole(\'' + d.accessRoleID + '\')" data-toggle="modal"><i class="fa fa-trash"></i></a></div>';


                        }

                    },
                    {
                        "title": "Role ID", "data": null, "searchable": true,
                        "render": function (response, type, row, meta) {
                            console.log(response);
                            return response.accessRoleID;
                        }
                    },
                    {
                        "title": "Role Name", "data": null, "searchable": true,
                        "render": function (responseData, type, row, meta) {
                            return responseData.role;
                        }
                    },
                    {
                        "title": "Role Alias", "data": null,
                        "render": function (dt, type, row, meta) {
                            return dt.alias;
                        }
                    },
                    {
                        "title": "Status", "data": null,
                        "render": function (res, type, row, meta) {

                            if (res.active)
                                return 'Active';
                            else
                                return 'Inactive';
                        }
                    },
                ],
                initComplete: function () {

                    $('#tblRoles tfoot th').each(function (i) {
                        var title = $('#tblRoles thead th').eq($(this).index()).text();
                        if (title != "Action")
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

                }
            });
            $('#tblRoles tfoot').insertAfter($('#tblRoles thead'));
        },
        error: function (xhr, status, statusText) {
           
            console.log('An error occurred');
        }
    });


}
// Add Role
function addRole() {
    var roleName = $('#role').val();
    var aliasName = $('#alias').val();

    pwIsf.addLayer({ text: 'Please wait ...' });
    $.isf.ajax({
        url: service_java_URL + "accessManagement/addAccessRole/" + roleName + "/" + aliasName,
        context: this,
        crossdomain: true,
        processData: true,
        contentType: 'application/json',
        type: 'POST',
        xhrFields: {
            withCredentials: false
        },
        success: function (data) {
            $("#roleAdd").modal('hide');
            if (data.apiSuccess)
                pwIsf.alert({ msg: data.responseMsg, type: "success" });
            else
                pwIsf.alert({ msg: data.responseMsg, type: 'warning' });


        },
        error: function (xhr, status, statusText) {
            pwIsf.alert({ msg: "Error while changing status", type: "warning" });
        },
        complete: function (xhr, statusText) {
            pwIsf.removeLayer();
        }
    });

}

// Delete Role
function deleteRole(roleID) {
    pwIsf.confirm({
        title: 'Delete Page', msg: "Are you sure you want to delete the Page?",
        'buttons': {
            'Yes': {
                'action': function () {
                    pwIsf.addLayer({ text: 'Please wait ...' });
                    $.isf.ajax({
                        url: service_java_URL + "accessManagement/deleteAccessRole/" + roleID,
                        context: this,
                        crossdomain: true,
                        processData: true,
                        contentType: 'application/json',
                        type: 'POST',
                        xhrFields: {
                            withCredentials: false
                        },
                        success: function (data) {

                            if (data.apiSuccess)
                                pwIsf.alert({ msg: "successfully Deleted.", type: "success" });
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
            'No': { 'action': function () { pwIsf.removeLayer();} },

        }
    });

}


// Edit Role Modal and Functionality
function editRole(thisObj) {
    let jsonData = $(thisObj).data('details');
    console.log(jsonData);
    var html = `  <form id="access_role">
                            <div class="row">
                                <div class="col-md-5">
<input type="hidden" id="role_ID1" name="roleID" value="${jsonData.roleID}" />
<input type="hidden" id="status" name="active" value="${jsonData.active}" />
                                    <label style="margin-left:10px">Role Name:</label>
                                    <input type="text" class="form-control" id="role2" name="role" value="${jsonData.roleName}" required/>
                                </div>
                                <div class="col-md-5">
                                    <label style="margin-left:10px">Role Alias:</label>
                                    <input type="text" class="form-control" id="alias2" name="alias"  value="${jsonData.roleAlias}" required/>
                                </div>

                            </div>
                            <hr />
                            
                        </form>
                            <div class="row">
                                <div class="col-md-2 pull-right">
                                    <button type="submit" id="UpdateRoleFinal" onclick="updateRolefinal()" class="btn btn-sm btn-success">UPDATE</button>
                                </div>
                            </div>
                        </form>`;
    $('#updateRole').html('').append(html);

}



// Update Role Function
function updateRolefinal() {
    var Access_Role = new Object();

    Access_Role.accessRoleID = $("#role_ID1").val();
    Access_Role.role = $("#role2").val();
    Access_Role.active = $("#status").val();
    Access_Role.alias = $("#alias2").val();



    $.isf.ajax({
        url: service_java_URL + 'accessManagement/updateAccessRole' ,
        type: 'POST',
        crossDomain: true,
        context: this,
        contentType: "application/json",

        data: JSON.stringify(Access_Role),
        success: function (returndata) {
            alert("Role Updated");
        },
        error: function (xhr, status, statusText) {
            pwIsf.alert({ msg: 'Role Update Failed.Please check all fields are filled and no field is  same as previous. If still getting an error, kindly contact technical team.', type: 'error' });
        },
    });



    return false;
}