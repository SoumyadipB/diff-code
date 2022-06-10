$(document).ready(function () {
    $('body').on('DOMNodeInserted', 'select', function () {
        $(this).select2();
    });

    $("select").select2();
    getActivityName();
    selectSource();
});

function getActivityName() {
    const userRoleID = JSON.parse(ActiveProfileSession).roleID.toString();
    const noActionRoles = ["1", "5"];
    const isUserActionPrevented = noActionRoles.includes(userRoleID);

    if ($.fn.dataTable.isDataTable('#Activity_Name_Search')) {
        $('#Activity_Name_Search').DataTable().destroy();
        $('#Activity_Name_Search').empty();
    }

    pwIsf.addLayer({ text: 'Please wait ...' });
    $.isf.ajax({
        url: service_java_URL + "toolInventory/getExternalActivity",
        crossDomain: true,
        success: function (data) {
            pwIsf.removeLayer();
            $('#div_table').show();
            $('#div_table_message').hide();

            $.each(data, function (i, d) {

                const jsonData = JSON.stringify({
                    extActivityID: d.extActivityID, sourceName: d.sourceName, lastModifiedBy: d.lastModifiedBy,
                    lastModifiedDate: d.lastModifiedDate, isfSubactivityID: d.isfSubactivityID, sourceID: d.sourceID,
                    activityID: d.activityID
                });

                d.actionIcon = '<div style="display:flex"><a href="#editActivityName" class="icon-edit" title="Edit Tool Kit " data-details= \''
                    + jsonData + '\' data-toggle="modal"  onclick="updateActivityMethod(this)">' + getIcon('edit') + '</a>';

                d.actionIcon += '<a class="icon-delete lsp" title="Delete Document" onclick="deleteToolKit('
                    + d.activityID + ')">' + getIcon('delete') + '</a></div>';



            });
            $("#Activity_Name_Search").append($(`<tfoot><tr><th></th><th>Tool Name</th> <th>External Source</th>
<th>Last Modified By</th><th>Last Modified On</th></tr></tfoot>`));
            $('#Activity_Name_Search').DataTable({
                searching: true,
                responsive: true,

                "pageLength": 10,
                colReorder: true,
                dom: 'Bfrtip',
                order: [1],
                buttons: [
                    'colvis', 'excelHtml5'
                ],
                "data": data,
                "destroy": true,
                "columns": [
                    {
                        "title": "Action",
                        "targets": 'no-sort',
                        "orderable": false,
                        "searchable": false,
                        "data": "actionIcon",
                        "visible": isUserActionPrevented ? false : true
                    },
                    {
                        "title": "Activity Name",
                        "data": "extActivityID",
                    },
                    {
                        "title": "External Source",
                        "data": "sourceName",
                        "defaultContent": "-"
                    },
                    {
                        "title": "Last Modified By",
                        "data": "lastModifiedBy",
                        "defaultContent": "-"
                    },
                    {
                        "title": "Last Modified On",
                        "data": "lastModifiedDate",
                        "defaultContent": "-"
                    },


                ],
                initComplete: function () {

                    $('#Activity_Name_Search tfoot th').each(function (i) {
                        var title = $('#Activity_Name_Search thead th').eq($(this).index()).text();
                        if (title !== "Action") {
                            $(this).html('<input type="text" class="form-control" style="font-size:12px;"  placeholder="Search ' + title + '" data-index="' + i + '" />');
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


            $('#Activity_Name_Search tfoot').insertAfter($('#Activity_Name_Search thead'));

            $('#Activity_Name_Search tbody').on('click', 'a.icon-view', function () {
                var data = $('#Activity_Name_Search').DataTable().row($(this).parents('tr')).data();
                localStorage.setItem("views_project_id", data.projectID);

                window.location.href = "../ActivityMaster/ExternalActivityList";

            });


        },
        error: function (xhr, status, statusText) {
            pwIsf.removeLayer();
            $('#div_table').hide();
            $('#div_table_message').show();
            console.log('An error occurred on' + xhr.error);
        }
    })


}



function addFieldsToFooter() {
    $('#Activity_Name_Search tfoot th').each(function (i) {
        var title = $('#Activity_Name_Search thead th').eq($(this).index()).text();
        if (title != "Actions")
            $(this).html('<input type="text" class="form-control" style="font-size:12px;"  placeholder="Search ' + title + '" data-index="' + i + '" />');
    });
}

function deleteToolKit(activityID) {

    pwIsf.confirm({
        title: 'Delete External Activity', msg: 'Are you sure to delete this Activity ? ',
        'buttons': {
            'Yes': {
                'action': function () {
                    pwIsf.addLayer({ text: 'Please wait ...' });
                    $.isf.ajax({
                        url: service_java_URL + "toolInventory/deleteExternalActivity/" + activityID,
                        context: this,
                        crossdomain: true,
                        processData: true,
                        contentType: 'application/json',
                        type: 'GET',

                        xhrFields: {
                            withCredentials: false
                        },
                        crossDomain: true,
                        success: function (data) {



                            pwIsf.alert({ msg: "Successfully Deleted.", autoClose: 3 });

                            getActivityName();
                        },
                        error: function (xhr, status, statusText) {
                            //Empty block
                        },
                        complete: function (xhr, statusText) {
                            pwIsf.removeLayer();
                        }
                    });
                }
            },
            'No': {
                'action': function () {
                    //Empty Block
                }
            },

        }
    });
}








function updateActivityMethod(thisObj) {
    const jsonData = $(thisObj).data('details');
    console.log(jsonData);

    var html = `
                        <div class="col-lg-4">
                            Activity Name: <input class="form-control " id="activityName2" name="tool" type="text" value="${jsonData.extActivityID}"  />
<label id="tbEditActivity-Required" style="color: red; font-size: 10px; text-align: center; padding-left:15px;"></label>
                            <input type="hidden" id="license_Type" name="licenseType" value="test1" />
<input type="hidden" id="lastmodifiedby" name="lastmodifiedBy" value="${jsonData.signumGlobal}" />
<input type="hidden" id="actID"  value="${jsonData.activityID}" />
                        </div>
                        <div class="col-lg-4">External Source : 
                            <select class="select2able" id="sourceId3"></select>
                           <label id="tbEditSource-Required" style="color: red; font-size: 10px; text-align: center; padding-left:15px;"></label>

                        </div>

                        <div class="col-lg-4">
                            <button id="btnUpdateToolKitName" type="submit" onclick="updateActivityFinal()" pull-right class="btn btn-primary">Update Activity</button>
                        
                        </div>
                    `;
    $('#updatetoolmodal').html('').append(html);
    selectSource2(jsonData.sourceID);



}





function updateActivityFinal() {

    var OK = true;

    $('#tbAddNewActivity-Required').text("");
    if ($("#activityName2").val() == null || $("#activityName2").val() == "") {
        $('#tbEditActivity-Required').text("Activity  is required");

        OK = false;
    }
    else if ($("#sourceId3").val() == null || $("#sourceId3").val() == 0) {
        $('#tbEditSource-Required').text("Source ID  is required");

        OK = false;
    }
    if (OK) {




        var activity_name1 = new Object();


        activity_name1.extActivityID = $("#activityName2").val();
        activity_name1.activityID = $("#actID").val();
        activity_name1.isfSubactivityID = 0;
        activity_name1.sourceID = $("#sourceId3").val();
        activity_name1.lastModifiedBy = signumGlobal;


        pwIsf.addLayer({ text: C_LOADDER_MSG });

        $.isf.ajax({
            url: service_java_URL + 'toolInventory/updateExternalActivity',
            type: 'POST',
            crossDomain: true,
            context: this,
            contentType: "application/json",
            data: JSON.stringify(activity_name1),

            success: function (returndata) {
                pwIsf.removeLayer();
                hideAndResetEditActivityPopup();
                pwIsf.alert({ msg: "Activity Updated", type: "success" });
                getActivityName();

            },
            error: function (xhr, status, statusText) {
                pwIsf.removeLayer();
                pwIsf.alert({ msg: xhr.responseJSON.errorMessage, type: 'error' });
            },



        });





        return false;
    }
}
function ClearData() {
    $('#tbAddNewSource-Required').text("");
    $('#tbAddNewActivity-Required').text("");
}

function validateAddActivity() {
    var OK = true;

    $('#tbAddNewActivity-Required').text("");
    if ($("#activityName").val() == null || $("#activityName").val() == "") {
        $('#tbAddNewActivity-Required').text("Activity  is required");

        OK = false;
    }
    else if ($("#sourceId1").val() == null || $("#sourceId1").val() == 0) {
        $('#tbAddNewSource-Required').text("Source ID is required");

        OK = false;
    }
    if (OK) {
        addNewActivity();
    }
}

function addNewActivity() {

    pwIsf.addLayer({ text: C_LOADDER_MSG });

    var activity = new Object();
    activity.extActivityID = $("#activityName").val();
    activity.sourceID = $('#sourceId1').val();
    activity.isfSubactivityID = 0;
    activity.lastModifiedBy = signumGlobal;

    $.isf.ajax({
        url: service_java_URL + "toolInventory/saveExternalActivity",
        context: this,
        crossdomain: true,
        processData: true,
        contentType: 'application/json',
        type: 'POST',
        data: JSON.stringify(activity),
        xhrFields: {
            withCredentials: false
        },
        success: AjaxSucceeded,
        error: AjaxFailed
    });
    function AjaxSucceeded(data, textStatus) {
        pwIsf.removeLayer();
        hideAndResetAddActivityPopup();
        pwIsf.alert({ msg: "Activity Saved", type: "success" });
        getActivityName();
    }
    function AjaxFailed(xhr, status, statusText) {
        pwIsf.removeLayer();
        pwIsf.alert({ msg: 'Activity Addition Failed. Activity already exists.', type: 'warning' });

    }
}

function selectSource() {
    $.isf.ajax({
        url: service_java_URL + "woManagement/getSourcesForMapping",
        success: function (data) {
            $('.sourceId').append('<option value="' + 0 + '"></option>');
            $.each(data, function (i, d) {
                $('.sourceId').append('<option value="' + d.sourceId + '">' + d.sourceName + '</option>');

            })
        },
        error: function (xhr, status, statusText) {
            console.log('An error occurred on getSourcesForMapping');
        }
    });
}

function selectSource2(sourceId) {
    $.isf.ajax({
        url: service_java_URL + "woManagement/getSourcesForMapping",
        success: function (data) {
            $('#sourceId3').append('<option value="' + 0 + '"></option>');
            $.each(data, function (i, d) {
                $('#sourceId3').append('<option value="' + d.sourceId + '">' + d.sourceName + '</option>');

            })
            $("#sourceId3").val(sourceId).trigger("change");
        },
        error: function (xhr, status, statusText) {
            console.log('An error occurred on getSourcesForMapping');
        }
    });
}

function hideAndResetAddActivityPopup() {
    $('#addActivity').modal('hide');
    $('#activityName').val('');
    $('#sourceId1').val('').change();
}

function hideAndResetEditActivityPopup() {
    $('#editActivityName').modal('hide');
    $('#activityName2').val('');
    $('#sourceId3').val('').change();
}