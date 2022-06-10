const UpdateErrMsg = "Error while updating";
const demandDescText = "#demandDescText";
const demandText = "#demandText";
const demandTypeTable = '#addDemandTypeTable';
const addDemandButton = '#btnaddDemand';
const btnCancelDemand = '#btnCancelDemand';
const demandTypeTableRow = '#addDemandTypeTable tr';
const addDemandDiv = ".addDemandDiv";
var demandID;
$(document).ready(function () {
    $(document).on('submit', '#addDemandForm', function () {
        return false;
    });
    $(btnaddDemand).prop('disabled', true);
    $(btnCancelDemand).prop('disabled', true);
    getAddDemandType();

    //For functionality on click of "Add" or "Update" button(data fetched from corresponding ADD/EDIT API)
    $(addDemandButton).click(function () {
        var all = validationForTypeAndForm();
        if (this.value === "Add" && all[0] !== "" && all[1] !== "") {
            //AJAX Call
            const demandObj = new Object();
            demandObj.demandType = all[0];
            demandObj.createdBy = signumGlobal;
            demandObj.demandTypeDescription = all[1];
            pwIsf.addLayer({ text: C_LOADDER_MSG });
            $.isf.ajax({
                url: `${service_java_URL}accessManagement/insertDemandType`,
                context: this,
                crossdomain: true,
                processData: true,
                contentType: C_CONTENT_TYPE,
                type: 'POST',
                data: JSON.stringify(demandObj),
                xhrFields: {
                    withCredentials: false
                },
                success: function (data) {
                    if (data.isValidationFailed === true) {
                        pwIsf.alert({ msg: data.formErrors[0], type: "info", autoClose: 3 });
                    }
                    else {
                        pwIsf.alert({ msg: data.formMessages[0], type: "info", autoClose: 3 });
                        getAddDemandType();
                        $(demandText).val("");
                        $(demandDescText).val("");
                        $(btnaddDemand).prop('disabled', true);
                        $(btnCancelDemand).prop('disabled', true);
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
            $(addDemandDiv).removeClass('EditDemandDiv');
            if (all[0] !== "") {
                //AJAX Call
                const demandObj = new Object();
                demandObj.demandType = all[0];
                demandObj.lastModifyBy = signumGlobal;
                demandObj.demandTypeDescription = all[1];
                demandObj.demandTypeId = demandID;
                pwIsf.addLayer({ text: C_LOADDER_MSG });
                $.isf.ajax({
                    type: "POST",
                    url: `${service_java_URL}accessManagement/updateDemandType`,
                    crossdomain: true,
                    processData: true,
                    contentType: C_CONTENT_TYPE,
                    data: JSON.stringify(demandObj),
                    success: function (data) {
                        if (data.isValidationFailed === true) {
                            pwIsf.alert({ msg: data.formErrors[0], type: "info", autoClose: 3 });
                        }
                        else {
                            pwIsf.alert({ msg: data.formMessages[0], type: "info", autoClose: 3 });
                            getAddDemandType();
                         }
                    },
                    error: function (status) {
                        pwIsf.alert({ msg: UpdateErrMsg, type: "error" });

                    },
                    complete: function () {
                        pwIsf.removeLayer();

                        $(demandTypeTableRow).each(function (i) {
                            $(this).removeClass('colorChange');
                        });
                        $(demandText).val("");
                        $(demandDescText).val("");
                        $(addDemandButton).val("Add");
                        $(btnaddDemand).prop('disabled', true);
                        $(btnCancelDemand).prop('disabled', true);
                    }
                })
            }

        }
    });
    $(btnCancelDemand).click(function () {
        if ($(addDemandButton).val("Update")) {
            $(addDemandButton).val("Add");
            $(addDemandDiv).removeClass('EditDemandDiv');
            $(demandTypeTableRow).each(function (i) {
                $(this).removeClass('colorChange');
            });
        }
        $(demandText).val("");
        $(demandDescText).val("");
        $(btnaddDemand).prop('disabled', true);
        $(btnCancelDemand).prop('disabled', true);
    });
});

//Function for checking "Type" select box,"Feedback Text" is null or not and to initialise form data
function validationForTypeAndForm() {
    const formData = $('form').serializeArray();
    var demandTypeText = "", demandDesc = "";
    $.each(formData, function (i, field) {
        console.log(i + field);
        if (field.name === "demandTypeText") {
            if (field.value === "") {
                demandTypeText="";
            }
            else { demandTypeText = field.value; }
        }
        else {
            if (field.value === "") {
                demandDesc="";
            }
            else {
                demandDesc = field.value;
            }
        }
    });

    return [demandTypeText, demandDesc];
}

function validateForEnablingAddButton() {
    const demandTypeFormat = /[~`!@#$%^&*()_+\-=\[\]{};':"\\|,.<>\/?]+/;
    const demandDescFormat = /[/\\#^%?;|]/;
    if ($(demandText).val() === "" || $(demandText).val() === null) {
        $(btnaddDemand).prop('disabled', true);
    }
    else if (demandTypeFormat.test($(demandText).val())) {
        $(btnaddDemand).prop('disabled', true);
    }
    else if ($(demandDescText).val() === "" || $(demandDescText).val() === null) {
        $(btnaddDemand).prop('disabled', true);
    }
    else if (demandDescFormat.test($(demandDescText).val())) {
        $(btnaddDemand).prop('disabled', true);
    }
    else {
        $(addDemandButton).prop('disabled', false);
    }
}


function validateForCancelButton() {
    if (($(demandText).val() !== "" || $(demandText).val() !== null) && ($(demandDescText).val() !== "" || $(demandDescText).val() !== null)) {
        $(btnCancelDemand).prop('disabled', false);
    }
}


function getAddDemandType() {
    if ($.fn.dataTable.isDataTable(demandTypeTable)) {
        $(demandTypeTable).DataTable().destroy();
        $(demandTypeTable).empty();
    }
    pwIsf.addLayer({ text: C_LOADDER_MSG });
    $.isf.ajax({
        url: `${service_java_URL }accessManagement/getDemandType`,
        success: function (data) {
            pwIsf.removeLayer();
            $.each(data.responseData, function (i, d) {
                const action = `<div style="display:flex">
                 <a href="#editUnit" class="icon-edit" title="Edit Deliverable Type "
                 data-toggle="modal"  onclick="updateAddedDemand(this)">
                 ${getIcon('edit')}</a>
                <a class="icon-delete lsp" title="Delete Demand Type" onclick="deleteDemandType(${d.demandTypeId})">${getIcon('delete')}</a>`;
                d.action = action;
            })
            $(demandTypeTable).append($('<tfoot><tr><th></th><th></th><th></th><th></th><th></th><th></th></tr></tfoot>'));
            $(demandTypeTable).DataTable({
                searching: true,
                responsive: true,
                "pageLength": 10,
                "data": data.responseData,
                colReorder: true,
                order: [1],
                dom: 'Bfrtip',
                "buttons": [
                     'colvis','excelHtml5'
                ],
                "destroy": true,
                "columns": [
                    { "title": "Action", "targets": 'no-sort', "orderable": false, "searchable": false, "data": "action" },
                    { "title": "Demand Type Id", "targets": 'no-sort', "data": "demandTypeId" },
                    { "title": "Demand Type", "data": "demandType" },
                    { "title": "Demand Type Description", "data": "demandTypeDescription" },
                    { "title": "Last Modified By", "data": "lastModifyBy" },
                    { "title": "Last Modified On", "data": "lastModifyOn" }
                ],
                initComplete: function () {
                   const thisObj = this;
                    addDemandTypeTableInitComplete(thisObj);
                }
            });        
            $('#addDemandTypeTable tfoot').insertAfter($('#addDemandTypeTable thead'));
        },
        error: function (xhr, status, statusText) {
            pwIsf.removeLayer();
            console.log("failed to fetch data");
        }
    });
}

function addDemandTypeTableInitComplete(thisObj) {
    $('#addDemandTypeTable tfoot th').each(function (i) {
        var title = $('#addDemandTypeTable thead th').eq($(this).index()).text();
        if ((title !== "Action" && title !== "Default Selected")) {
            $(this).html(`<input type="text" class="form-control" style="font-size:12px;"  placeholder="Search ${title}" data-index="' + i + '" />`);
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

function deleteDemandType(demandTypeId) {
    pwIsf.confirm({
        title: 'Delete Demand Type', msg: 'Are you sure you want to delete this Demand Type?',
        'buttons': {
            'Yes': {
                'action': function () {
                    const deleteObj = new Object();
                    deleteObj.demandTypeId = demandTypeId;
                    deleteObj.lastModifyBy = signumGlobal;
                    deleteObj.inactivateDemandType = "true";
                    pwIsf.addLayer({ text: 'Please wait ...' });
                    $.isf.ajax({
                        url: `${service_java_URL}accessManagement/updateDemandType/`,
                        context: this,
                        crossdomain: true,
                        processData: true,
                        contentType: C_CONTENT_TYPE,
                        data: JSON.stringify(deleteObj),
                        type: 'POST',
                        xhrFields: {
                            withCredentials: false
                        },
                        success: function (data) {
                            pwIsf.alert({ msg: "Successfully Deleted.", autoClose: 3 });
                            getAddDemandType();
                        },
                        error: function (xhr, status, statusText) {
                            pwIsf.alert({ msg: "Not Deleted, please try Again.", autoClose: 3 });
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

//Function on click of edit icon to populate corresponding row data in the edit section above
function updateAddedDemand(updateDemandObj) {
    $(addDemandDiv).addClass('EditDemandDiv');
    const demandType = $(updateDemandObj).closest('tr').find('td:nth-child(3)').text();
    const demandTypeDescription = $(updateDemandObj).closest('tr').find('td:nth-child(4)').text();
    const demandTypeId = $(updateDemandObj).closest('tr').find('td:nth-child(2)').text();
    $(demandTypeTableRow).each(function (i) {
        $(this).removeClass('colorChange');
    });
    $(updateDemandObj).closest('tr').addClass('colorChange');
    $('#demandText').val(demandType).trigger("change");
    $('#demandDescText').val(demandTypeDescription).trigger("change");
    demandID = demandTypeId;
    $('#btnaddDemand').val("Update");
    validateForEnablingAddButton();
    validateForCancelButton();
}
