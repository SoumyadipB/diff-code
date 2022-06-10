const UpdateErrMsg = "Error while updating";
$(document).ready(function () {

    $(document).on('submit', '#addLocationForm', function () {

        return false;
    });

    $(btnaddLocation).prop('disabled', true);
    $(btnCancelLocation).prop('disabled', true);

    getAddLocationType();

    //For functionality on click of "Add" or "Click" button(data fetched from corresponding ADD/EDIT API)
    $('#btnaddLocation').click(function () {
        var all = validationForTypeAndForm();

        if (this.value === "Add" && all[0] != "") {
            //AJAX Call
            const locationObj = new Object();
            locationObj.locationType = all[0];
            locationObj.isActive = all[1];
            locationObj.createdBy = signumGlobal;

            pwIsf.addLayer({ text: C_LOADDER_MSG });
            $.isf.ajax({
                type: "POST",
                url: service_java_URL + "accessManagement/addLocationType",
                crossdomain: true,
                processData: true,
                async: false,
                contentType: 'application/json',
                data: JSON.stringify(locationObj),
                success: function (data) {
                    if (data.isValidationFailed === true) {
                        pwIsf.alert({ msg: data.formErrors[0], type: "info", autoClose: 3 });
                    }
                    else {
                        pwIsf.alert({ msg: data.formMessages[0], type: "info", autoClose: 3 });
                        getAddLocationType();
                        $('#locationText').val("");
                        $('#radioActive').prop('checked', true);
                        $(btnaddLocation).prop('disabled', true);
                        $(btnCancelLocation).prop('disabled', true);
                    }

                },
                error: function (status) {
                    pwIsf.alert({ msg: "Error while adding", type: "error" });
                },
                complete: function () {
                    pwIsf.removeLayer();

                }
            })
        }
        else if (this.value === "Update") {

            $(".addLocationDiv").removeClass('EditLocationDiv');
            if (all[0] != "") {
                //AJAX Call
                const locationObj = new Object();
                locationObj.locationType = all[0];
                locationObj.isActive = all[1];
                locationObj.locationTypeID = $(locationID).val();
                pwIsf.addLayer({ text: C_LOADDER_MSG });
                $.isf.ajax({
                    type: "POST",
                    url: service_java_URL + "accessManagement/addLocationType",
                    crossdomain: true,
                    processData: true,
                    contentType: 'application/json',
                    data: JSON.stringify(locationObj),
                    success: function (data) {

                        if (data.isValidationFailed === true) {
                            pwIsf.alert({ msg: data.formErrors[0], type: "info", autoClose: 3 });
                        }
                        else {
                            pwIsf.alert({ msg: data.formMessages[0], type: "info", autoClose: 3 });
                            getAddLocationType();
                            $('#locationText').val("");
                            $('#radioActive').prop('checked', true);
                            $(btnaddLocation).prop('disabled', true);
                            $(btnCancelLocation).prop('disabled', true);
                            $('#btnaddLocation').val("Add");
                            validateForEnablingAddButton();
                            validateForCancelButton();
                        }

                    },
                    error: function (status) {
                        pwIsf.alert({ msg: UpdateErrMsg, type: "error" });
                    },
                    complete: function () {
                        pwIsf.removeLayer();

                        $('#addLocationTypeTable tr').each(function (i) {
                            $(this).removeClass('colorChange');
                        });

                    }
                })
            }

        }

    });
    $('#btnCancelLocation').click(function () {
        if ($('#btnaddLocation').val("Update")) {
            $('#btnaddLocation').val("Add");
            $(".addLocationDiv").removeClass('EditLocationDiv');

        }
        $('#addLocationTypeTable tr').each(function (i) {
            $(this).removeClass('colorChange');


        });
        $('#locationText').val("");
        $('#radioActive').prop('checked', true);
        $(btnaddLocation).prop('disabled', true);
        $(btnCancelLocation).prop('disabled', true);

    });
});

//Function on click of edit icon to populate corresponding row data in the edit section above
function updateAddedLocation(updateLocationObj) {
    $(".addLocationDiv").addClass('EditLocationDiv');
    var active;
    
    const locationText = $(updateLocationObj).closest('tr').find('td:nth-child(3)').text();
    const activeHtml = $(updateLocationObj).closest('tr').find('td:nth-child(5)').html();
    if ($(activeHtml).hasClass('editWrongSign')) {
        active = "false";

    }
    else if ($(activeHtml).hasClass('editRightSign')) {
        active = "true";
    }
    const locationID = $(updateLocationObj).closest('tr').find('td:nth-child(2)').text();
    $('#addLocationTypeTable tr').each(function (i) {
        $(this).removeClass('colorChange');


    });

    $(updateLocationObj).closest('tr').addClass('colorChange');
    $('#locationText').val(locationText).trigger("change");
    $('#locationID').val(locationID).trigger("change");

    $('#btnaddLocation').val("Update");

    if (active === "true") {
        $('#radioActive').prop('checked', true);
    }
    else if (active === "false") {
        $('#radioInactive').prop('checked', true);
    }
   validateForEnablingAddButton();
    validateForCancelButton();
}

//Function for checking "Type" select box,"Feedback Text" is null or not and to initialise form data
function validationForTypeAndForm() {

    const formData = $('form').serializeArray();
    var locationText = "", locationState = "";
    $.each(formData, function (i, field) {
        console.log(i + field);
        if (field.name === "locationText") {
            if (field.value == "") {
                return false;
            }
            else { locationText = field.value; }
        }
        else {
            locationState = field.value;
        }
    });

    return [locationText, locationState];

}

function validateForEnablingAddButton() {
    if ($("#locationText").val() == "" || $("#locationText").val() == null) {
        $(btnaddLocation).prop('disabled', true);


    }
    else {
        $(btnaddLocation).prop('disabled', false);

    }
}

function changeIconOnClick(changeLocationElement) {
    
        var active;
        const locationObj = new Object();
        locationObj.locationTypeID = $(changeLocationElement).closest('tr').find('td:nth-child(2)').text();
        const activeHtmlChange = $(changeLocationElement).closest('tr').find('td:nth-child(5)').html();
        if ($(activeHtmlChange).hasClass('editWrongSign')) {
            active = "false";

        }
        else if ($(activeHtmlChange).hasClass('editRightSign')) {
            active = "true";

        }
        locationObj.isActive = active;
        pwIsf.addLayer({ text: C_LOADDER_MSG });
        $.isf.ajax({
            type: "POST",
            url: service_java_URL + "accessManagement/changeLocationTypeStatus",
            crossdomain: true,
            processData: true,
            contentType: 'application/json',
            data: JSON.stringify(locationObj),
            success: function (data) {
                genericUpdateSuccessFunction(data);
            },
            error: function (status) {
                pwIsf.alert({ msg: UpdateErrMsg, type: "error" });
            },
            complete: function () {

                getAddLocationType();
                pwIsf.removeLayer();

            }
        })

}
function changeIconOnClickDefaultColumn(changeLocationDefaultElement) {
    
    const locationDefaultObj = new Object();
    let prevLocationTypeID;
    locationDefaultObj.locationTypeID = $(changeLocationDefaultElement).closest('tr').find('td:nth-child(2)').text();
    $('#addLocationTypeTable tr').each(function (i) {
        const defaultHtml=$(this).find('td:nth-child(4)').html();
        if ($(defaultHtml).hasClass('editRightSign')) {
            prevLocationTypeID = $(this).find('td:nth-child(2)').text();
        }
    });
    if (prevLocationTypeID == ' ' || prevLocationTypeID === null || prevLocationTypeID ==='undefined' ) {
        prevLocationTypeID = 0;
    }

    locationDefaultObj.previousDefaultID = prevLocationTypeID;
   
    pwIsf.addLayer({ text: C_LOADDER_MSG });
    $.isf.ajax({
        type: "POST",
        url: service_java_URL + "accessManagement/updateDefaultLocationType",
        crossdomain: true,
        processData: true,
        contentType: 'application/json',
        data: JSON.stringify(locationDefaultObj),
        success: function (data) {
            genericUpdateSuccessFunction(data);
        },
        error: function (status) {
            pwIsf.alert({ msg: UpdateErrMsg, type: "error" });
        },
        complete: function () {

            getAddLocationType();
            pwIsf.removeLayer();

        }
    })

}

function genericUpdateSuccessFunction(data) {
    if (data.isValidationFailed === true) {
        pwIsf.alert({ msg: data.formErrors[0], type: "info", autoClose: 3 });
    }
    else {
        pwIsf.alert({ msg: data.formMessages[0], type: "info", autoClose: 3 });

    }
}
function getAddLocationType() {

    if ($.fn.dataTable.isDataTable('#addLocationTypeTable')) {
        $('#addLocationTypeTable').DataTable().destroy();
        $('#addLocationTypeTable').empty();
    }
    pwIsf.addLayer({ text: C_LOADDER_MSG });

    $.isf.ajax({
        url: service_java_URL + "accessManagement/getAllLocationTypes",
        success: function (data) {
            pwIsf.removeLayer();

            $.each(data, function (i, d) {

                const action = "<a href='#' class='icon-edit' style='background-color: #256fb3; border: 1px solid #2e6da4;'"
                    + " data-toggle='tooltip' title='Edit Location Type' onclick='updateAddedLocation(this)'>"
                    + "<span class='fa fa-edit'  style='color:white;'> </span></a>";

                const rightIconForActiveColumn = "<a href='#' class='icon-edit editRightSign'"
                    + " style='background-color:#4aaf4a;border: 1px solid #468e45;padding-right: 6px;padding-left: 6px;'"
                    + " data-toggle='tooltip' title='Click to change into Inactive' onclick='changeIconOnClick(this)'>"
                    + "<span class='fa fa-check'  style='color:white;'> </span></a>";

                const wrongIconForActiveColumn = "<a href='#' class='icon-edit editWrongSign'"
                    + " style='background-color:#ca402ae6;border: 1px solid #cc1204;'"
                    + " data-toggle='tooltip' title='Click to change into Active' onclick='changeIconOnClick(this)'>"
                    + "<span class='fa fa-times'  style='color:white;'> </span></a>";

                const rightIconForDefaultColumn = "<a class='icon-edit editRightSign'"
                    + " style='background-color:#4aaf4a;border: 1px solid #468e45;padding-right: 6px;padding-left: 6px;'"
                    + " data-toggle='tooltip' title='Unclickable'>"
                    + "<span class='fa fa-check'  style='color:white;'> </span></a>";

                const wrongIconForDefaultColumn = "<a href='#' class='icon-edit editWrongSign'"
                    + " style='background-color:#ca402ae6;border: 1px solid #cc1204;'"
                    + " data-toggle='tooltip' title='Click to make it Default Selected'"
                    + " onclick='changeIconOnClickDefaultColumn(this)'>"
                    + "<span class='fa fa-times'  style='color:white;'> </span></a>";

                d.action = action;
                if (d.selected === true) {
                    d.selectedDefaultIcon = rightIconForDefaultColumn;
                }
                else if (d.selected === false) {
                    d.selectedDefaultIcon = wrongIconForDefaultColumn;
                }
                if (d.active === true) {
                    d.activeIcon = rightIconForActiveColumn;
                }
                else if (d.active === false) {
                    d.activeIcon = wrongIconForActiveColumn;
                }

            })

            $("#addLocationTypeTable").append($('<tfoot><tr><th></th><th></th><th></th><th></th><th></th></tr></tfoot>'));

            $('#addLocationTypeTable').DataTable({
                searching: true,
                responsive: true,
                "pageLength": 10,
                "data": data,
                colReorder: true,
                order: [1],
                dom: 'Bfrtip',
                buttons: [
                    'colvis', 'excel'
                ],
                "destroy": true,
                "columns": [
                    { "title": "Action", "targets": 'no-sort', "orderable": false, "searchable": false, "data": "action" },
                    { "title": "Location Type Id", "targets": 'no-sort', "data": "locationTypeID" },
                    { "title": "Location Type", "data": "locationType" },
                    { "title": "Default Selected", "data": "selectedDefaultIcon" },
                    { "title": "Active/Inactive", "data": "activeIcon" }

                ],
                initComplete: function () {

                    const thisObj = this;
                    addLocationTypeTableInitComplete(thisObj);

                }

            });

            $('#addLocationTypeTable tfoot').insertAfter($('#addLocationTypeTable thead'));

        },
        error: function (xhr, status, statusText) {
            pwIsf.removeLayer();
            console.log("failed to fetch data");
        }

    });
}

function addLocationTypeTableInitComplete(thisObj) {
    $('#addLocationTypeTable tfoot th').each(function (i) {
        var title = $('#addLocationTypeTable thead th').eq($(this).index()).text();
        if ((title !== "Action" && title !== "Default Selected" && title !== "Active/Inactive")) {
            $(this).html('<input type="text" class="form-control" style="font-size:12px;"  placeholder="Search ' + title + '" data-index="' + i + '" />');
        }
    });
    var api = thisObj.api();
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
function validateForCancelButton(){

    if ($("#locationText").val() == "" || $("#locationText").val() == null) {
        if ($('#radioInactive').is(':checked') && $('#btnaddLocation').val() === "Add") {
            $(btnCancelLocation).prop('disabled', false);
        }
        else if ($('#radioActive').is(':checked') && $('#btnaddLocation').val() === "Add") {
            $(btnCancelLocation).prop('disabled', true);
        }
    }
    else if ($("#locationText").val() != "" || $("#locationText").val() != null)
    {
        $(btnCancelLocation).prop('disabled', false);
   }

}


