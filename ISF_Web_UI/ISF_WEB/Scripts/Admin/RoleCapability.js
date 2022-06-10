$(document).ready(function () {
    getRoleCapabilities();
    $('#updateInfo').on('show.bs.modal', function () {
        showModal(this);
    });

    $('#capabilityAdd').on('show.bs.modal', function () {
        showModal(this);
    });

function showModal(modalId) {
    $(modalId).find('.modal-content').css({
        width: 'auto', //probably not needed
        height: 'auto', //probably not needed
        'max-width': '50%',
        'margin': 'auto'
    });
}

    $('#subAddCapability').on('click', function () {
        $("form#capability_frm").submit(function (event) {
            event.preventDefault();
            //grab all form data
            const capabilityRole = new Object();
            capabilityRole.capabilityPageName = $("#page_name").val();
            capabilityRole.capabilityPageGroup = $("#page_group").val();
            capabilityRole.groupTitle = $("#group_title").val();
            capabilityRole.subMenuTitle = $("#submenu_title").val();
            capabilityRole.groupIcon = $("#group_icon").val();
            capabilityRole.subMenuHref = $("#submenu_href").val();
            capabilityRole.groupHref = $("#group_href").val();
            capabilityRole.priority = $("#priority_p").val();
            $.isf.ajax({
                url: `${service_java_URL}accessManagement/addCapability`,
                type: 'POST',
                crossDomain: true,
                context: this,
                processData: true,
                contentType: C_Application_Json,
                data: JSON.stringify(capabilityRole),
                xhrFields: {
                    withCredentials: false
                },
                success: function (returndata) {
                    pwIsf.alert({ msg: 'Role Added Sucessfuly.', type: 'success' });
                    location.reload();
                },
                error: function (xhr, status, statusText) {
                    pwIsf.alert({ msg: 'Role Addition Failed!', type: 'warning' });
                },

            });
            return false;
        });
    });
}
);

function getRoleCapabilities() {
    if ($.fn.dataTable.isDataTable(C_TableRolesBody)) {
        oTable.destroy();
        $(C_TableRolesBody).empty();
    }
    pwIsf.addLayer({ text: 'Please wait ...' });
    $.isf.ajax({
        url: `${service_java_URL}accessManagement/getAllCapabilities`,
        context: this,
        crossdomain: true,
        processData: true,
        contentType: C_Application_Json,
        type: 'GET',
        xhrFields: {
            withCredentials: false
        },

        success: function (finalResult) {
            pwIsf.removeLayer();
            $(C_TableRolesBody).append($(`<tfoot><tr><th></th><th>Page ID</th><th>Page Group</th>
<th>Page Name</th><th>Status</th><th>Group Title</th><th>Sub-Menu Title</th><th>Group Icon</th>
<th>Sub-Menu URL</th><th>Group URL</th><th>Priority</th></tr></tfoot>`));
           oTable = $(C_TableRolesBody).DataTable({
                searching: true,
                responsive: true,
                "pageLength": 10,
                "data": finalResult,
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
                        "render": function (data, type, row, meta) {
                            const jsonData = JSON.stringify({
                                pageID: data.capabilityPageId,
                                pageName: data.capabilityPageName,
                                pageGroup: data.capabilityPageGroup,
                                groupTitle: data.groupTitle,
                                submenuTitle: data.subMenuTitle,
                                groupIcon: data.groupIcon,
                                groupURL: data.groupHref,
                                subMenuURL: data.subMenuHref,
                                Priority: data.priority
                            });
                            return `<div style="display:flex"><a class="icon-edit lsp" href="#updateInfo" title="Edit Capability"
                                    data-details= \' ${jsonData} \'  onclick="editCapabilty(this)" data-toggle="modal">
                                    <i class="fa fa-edit"></i></a><a class="icon-delete lsp" href="#" title="Delete Capability"
                                    onclick="deletePage(${data.capabilityPageId})" data-toggle="modal">
                                    <i class="fa fa-trash"></i></a></div>`;
                        }
                    },
                    {
                        "title": "Page ID", "data": null, "searchable": true,
                        "render": function (dt, type, row, meta) {
                            console.log(dt);
                            return dt.capabilityPageId;
                        } },
                    {
                        "title": "Page Group", "data": null,"searchable": true,
                        "render": function (res, type, row, meta) {
                            return res.capabilityPageGroup;
                        }},
                    {
                        "title": "Page Name", "data": null,
                        "render": function (resData, type, row, meta) {
                            return resData.capabilityPageName;
                        }},
                    {
                        "title": "Status", "data": null,
                        "render": function (responseData, type, row, meta) {
                            return responseData.active;
                        }
                    },
                    {
                        "title": "Group Title", "data": null,
                        "render": function (botData, type, row, meta) {
                            return botData.groupTitle;
                        }
                    },
                    {
                        "title": "Sub-Menu Title", "data": null,
                        "render": function (botRequestData, type, row, meta) {
                            return botRequestData.subMenuTitle;
                        }
                    },
                    {
                        "title": "Group Icon", "data": null,
                        "render": function (rd, type, row, meta) {
                            return rd.groupIcon;
                        }
                    },
                    {
                        "title": "Sub-Menu URL", "data": null,
                        "render": function (response, type, row, meta) {
                            return response.subMenuHref;
                        }
                    },
                    {
                        "title": "Group URL", "data": null,
                        "render": function (botData, type, row, meta) {
                            return botData.groupHref;
                        }
                    },
                    {
                        "title": "Priority", "data": null,
                        "render": function (result, type, row, meta) {
                            return result.priority;
                        }
                    },
                ],
                initComplete: function () {
                    $('#tblRolesBody tfoot th').each(function (i) {
                        var title = $('#tblRolesBody thead th').eq($(this).index()).text();
                        if (title !== "Action") {
                            $(this).html(`<input type="text" class="form-control" style="font-size:12px;" placeholder="Search ${title}" data-index="${i}"/>`);
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
            $('#tblRolesBody tfoot').insertAfter($('#tblRolesBody thead'));
        },
        error: function (xhr, status, statusText) {
            console.log('An error occurred');
        }
    });
}

function deletePage(pageID) {
    pwIsf.confirm({
        title: 'Delete Page', msg: "Are you sure you want to delete the Page?",
        'buttons': {
            'Yes': {
                'action': function () {
                    pwIsf.addLayer({ text: 'Please wait ...' });
                    $.isf.ajax({
                        url: `${ service_java_URL }accessManagement/deleteCapability/${pageID}` ,
                        context: this,
                        crossdomain: true,
                        processData: true,
                        contentType: C_Application_Json,
                        type: 'POST',
                        xhrFields: {
                            withCredentials: false
                        },
                        success: function (data) {
                            if (data.apiSuccess) {
                                pwIsf.alert({ msg: data.responseMsg, type: "success" });
                                location.reload();
                            }
                            else {
                                pwIsf.alert({ msg: data.responseMsg, type: 'warning' });
                            }
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

function editCapabilty(thisObj) {
    let jsonData = $(thisObj).data('details');
    jsonData = JSON.parse(jsonData);
    console.log(jsonData);
    var html = ` <form id="capability_frm1">
                            <input type="hidden" id="page_ID1" name="capabilityPageId" value="${jsonData.pageID}" />
                            <div class="row">
                                <div class="col-md-5">
                                    <label style="margin-left:10px">Page Name:</label>
                                    <input type="text" id="page_name1" class="form-control" name="capabilityPageName" value="${jsonData.pageName}" />
                                </div>
                                <div class="col-md-5">
                                    <label style="margin-left:10px">Page Group:</label>
                                    <input type="text" id="page_group1" class="form-control" name="capabilityPageGroup" value="${jsonData.pageGroup}" />
                                </div>
                            </div>
                            <hr />
                            <div class="row">
                                <div class="col-md-5">
                                    <label style="margin-left:10px">Group Title:</label>
                                    <input type="text" id="group_title1" class="form-control" name="groupTitle" value="${jsonData.groupTitle}" />
                                </div>
                                <div class="col-md-5">
                                    <label style="margin-left:10px">Sub-Menu Title:</label>
                                    <input type="text" id="submenu_title1" class="form-control" name="subMenuTitle" value="${jsonData.submenuTitle}"/>
                                </div>
                            </div>
                            <hr />
                            <div class="row">
                                <div class="col-md-5">
                                    <label style="margin-left:10px">Group Icon:</label>
                                    <input type="text" id="group_icon1" class="form-control" name="groupIcon" value="${jsonData.groupIcon}"/>
                                </div>
                                <div class="col-md-5">
                                    <label style="margin-left:10px">Sub-Menu URL:</label>
                                    <input type="text" id="submenu_href1" class="form-control" name="subMenuHref" value="${jsonData.subMenuURL}"/>
                                </div>
                            </div>
                            <hr />
                            <div class="row">
                                <div class="col-md-5">
                                    <label style="margin-left:10px">Group URL:</label>
                                    <input type="text" id="group_href1" class="form-control" name="groupHref" value="${jsonData.groupURL}"/>
                                </div>
                                <div class="col-md-5">
                                    <label style="margin-left:10px">Priority:</label>
                                    <input type="number" id="priority_p1" class="form-control"  name="priority" value="${jsonData.Priority}"/>
                                </div>
                            </div>
                            <hr />
                            <div class="row">
                                <div class="col-md-2 pull-right">
                                    <button type="submit" id="subupdateCapability" onclick="updatecapabilityfinal()" class="btn btn-sm btn-success">UPDATE</button>
                                </div>
                            </div>
                        </form>`;
    $('#updateDetails').html('').append(html);
}

function updatecapabilityfinal() {
    const capabilityRole1={
  capabilityPageId : $("#page_ID1").val(),
  capabilityPageName : $("#page_name1").val(),
    capabilityPageGroup : $("#page_group1").val(),
   groupTitle : $("#group_title1").val(),
    subMenuTitle : $("#submenu_title1").val(),
    groupIcon : $("#group_icon1").val(),
    subMenuHref : $("#submenu_href1").val(),
   groupHref : $("#group_href1").val(),
  priority : $("#priority_p1").val(),
    }
    $.isf.ajax({
        url: `${service_java_URL}accessManagement/addCapability`,
        type: 'POST',
        crossDomain: true,
        context: this,
        contentType: C_Application_Json,
        data: JSON.stringify(capabilityRole1),
        success: function(returndata) {
            alert("Role Updated");
        },
        error: function(xhr, status, statusText) {
            pwIsf.alert({
                msg: `Role Update Failed.Please check all fields are filled and no field is
                      same as previous.If still getting an error, kindly contact technical team.`, type: 'error'
            });
        },
    });
    return false;
}
