/*---------API Calls on Load------------*/
/*some Test*/
$(document).ready(function () {
    LoadCompetenceData();
    selectCompetenceType();
    selectDomain();
    selectVendor();
    selectTechnology();
    selectGrade();
    $("#btnBulkUpload").prop("disabled", true);
    $('#bulkUploadFile').change(function (e) {
        fl = e.target.files[0];
        var filename = $('#bulkUploadFile').val().split('\\').pop();
        var ext = filename.split('.').pop();
        if (ext == 'txt') {
            $("lbltxt").text = fl.name;
            pwIsf.alert({ msg: 'The file "' + filename + '" has been selected', type: 'info', autoClose: 2 });
            $("#btnBulkUpload").prop("disabled", false);
        }
        else {
            pwIsf.alert({ msg: 'Wrong file type selected', type: 'error', autoClose: 3 });
            $("#btnBulkUpload").prop("disabled", true);
        }
    });
});

var fl = null;

//var service_java_URL = "https://isfdevservices.internal.ericsson.com:8443/isf-rest-server-java_dev_major/";

/*-----------GET: Fill Competence Type Dropdown--------*/
function selectCompetenceType() {
    pwIsf.addLayer({ text: "Please wait ..." });
    $.isf.ajax({
        url: service_java_URL + "competenceController/getCompetence",
        success: function (data) {
            pwIsf.removeLayer();
            $('#competenceType').append('<option value="' + 0 + '"></option>');
            $.each(data, function (i, d) {
                $('#competenceType').append('<option value="' + d.CompetenceTypeID + '">' + d.CompetenceType + '</option>');

            })

            //$('#ModalVendorCountry').append('<option value="' + 0 + '"></option>');
            //$.each(data, function (i, d) {
            //    $('#ModalVendorCountry').append('<option value="' + d.CountryID + '">' + d.CountryName + '</option>');

            //})
        },
        error: function (xhr, status, statusText) {
            pwIsf.removeLayer();
            console.log('An error occurred on getCountries');
        }
    });
}

/*-----------GET: Fill Domain Dropdown----------------*/
function selectDomain() {
    pwIsf.addLayer({ text: "Please wait ..." });
    $.isf.ajax({
        url: service_java_URL + "competenceController/getAllDomain",
        success: function (data) {
            pwIsf.removeLayer();
            $('#domainCompetence').append('<option value="' + 0 + '"></option>');
            $.each(data, function (i, d) {
                $('#domainCompetence').append('<option value="' + d.DomainID + '">' + d.Domain + '</option>');
            })
        },
        error: function (xhr, status, statusText) {
            pwIsf.removeLayer();
            console.log('An error occurred on getCountries');
        }
    });
}

/*-----------GET: Fill Vendor Dropdown---------------*/
function selectVendor() {
    pwIsf.addLayer({ text: "Please wait ..." });
    $.isf.ajax({
        url: service_java_URL + "competenceController/getAllVendor",
        success: function (data) {
            pwIsf.removeLayer();
            $('#vendorCompetence').append('<option value="' + 0 + '"></option>');
            $.each(data, function (i, d) {
                $('#vendorCompetence').append('<option value="' + d.VendorID + '">' + d.Vendor + '</option>');
            })
        },
        error: function (xhr, status, statusText) {
            pwIsf.removeLayer();
            console.log('An error occurred on getCountries');
        }
    });
}

/*-----------GET: Fill Technology Dropdown----------*/
function selectTechnology() {
    pwIsf.addLayer({ text: "Please wait ..." });
    $.isf.ajax({
        url: service_java_URL + "competenceController/getAllTechnology",
        success: function (data) {
            pwIsf.removeLayer();
            $('#technologyCompetence').append('<option value="' + 0 + '"></option>');
            $.each(data, function (i, d) {
                $('#technologyCompetence').append('<option value="' + d.TechnologyID + '">' + d.Technology + '</option>');
            })
        },
        error: function (xhr, status, statusText) {
            pwIsf.removeLayer();
            console.log('An error occurred on getCountries');
        }
    });
}

/*-----------GET: Fill Grade Dropdown--------------*/
function selectGrade() {
    pwIsf.addLayer({ text: "Please wait ..." });
    $.isf.ajax({
        url: service_java_URL + "competenceController/getBaseline",
        success: function (data) {
            pwIsf.removeLayer();
            $('#competenceGrade').append('<option value="' + 0 + '"></option>');
            $.each(data, function (i, d) {
                $('#competenceGrade').append('<option value="' + d.CompetenceGRADEID + '">' + d.GradeName + '</option>');
            })
        },
        error: function (xhr, status, statusText) {
            pwIsf.removeLayer();
            console.log('An error occurred on getCountries');
        }
    });
}

/*---GET: Fill Competence Upgrade Dropdown: Training----*/
function selectCompetenceUpgrade() {
    pwIsf.addLayer({ text: "Please wait ..." });
    $.isf.ajax({
        url: service_java_URL + "competenceController/getCompetenceUpgrade",
        success: function (data) {
            pwIsf.removeLayer();
            $("#competencyUpgrade").append('<option value="' + 0 + '"></option>');
            $.each(data, function (i, d) {
                $("#competencyUpgrade").append('<option value="' + d.CompetenceUpgradeID + '">' + d.CompetencyUpgrade + '</option>');

            })
        },
        error: function (xhr, status, statusText) {
            pwIsf.removeLayer();
            console.log('An error occurred on getCountries');
        }
    });
}

/*-------GET: Load Competence table on Load--------*/
function LoadCompetenceData() {
    if ($.fn.dataTable.isDataTable('#competenceTable')) {
        oTable.destroy();
        $('#competenceTable').empty();
    }
    pwIsf.addLayer({ text: "Please wait ..." });
    $.isf.ajax({

        url: service_java_URL + "competenceController/getAllCompetenceServiceArea",
        success: function (data) {
            pwIsf.removeLayer();
            $('#div_table').show();
            $('#div_table_message').hide();

            $.each(data, function (i, d) {

                let jsonData = JSON.stringify({
                    CompetenceID: d.CompetenceID, VendorID: d.VendorID, DeliveryCompetenceID: d.DeliveryCompetanceID

                });

                d.actionIcon = '<div style="display:flex"><a href="#addTraining" class="icon-edit" title="Add Training" data-details= \'' + jsonData + '\' data-toggle="modal"  onclick="openTrainingModule(this)">' + getIcon('add') + '</a>';
}),
                $("#competenceTable").append($('<tfoot><tr><th></th><th></th><th>Vendor Name</th><th>Vendor Name</th><th>Country</th><th>Contact</th><th>Vendor Code</th><th>Manager</th><th>Manager</th></tfoot>'));
            oTable = $('#competenceTable').DataTable({
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
                    },
                    {
                        "title": "Active/Inactive", "targets": 'no-sort', "orderable": false, "searchable": false, "data": null,
                        "render": function (data, type, row, meta) {
                            if (data.Active) {
                                return '<label class="switchSource"><input type="checkbox" checked class="toggleActive" onclick="enableDisableCompetence(\'' + data.CompetenceID + '\')" id="togBtnSource_' + data.CompetenceID + '" /><div class="sliderSource round"><span class="onSource">Enabled</span><span class="offSource">Disabled</span></div></label>';
                            }
                            else {
                                return '<label class="switchSource"><input type="checkbox" class="toggleActive" onclick="enableDisableCompetence(\'' + data.CompetenceID + '\')" id="togBtnSource_' + data.CompetenceID + '"/><div class="sliderSource round"><span class="onSource">Enabled</span><span class="offSource">Disabled</span></div></label>';

                            }
                        },
                    },
                    
                     {
                        "title": "Competence Type",
                         "data": "CompetenceType",
                    },
                    {
                        "title": "Competence Service Area",
                        "data": "Competency_Service_Area",
                    },
                    {
                        "title": "Description",
                        "data": "Description"
                    },                  
                  
                    {
                        "title": "Vendor",
                        "data": "Vendor"
                    },
                    {
                        "title": "Technology",
                        "data": "Technology"
                    },
                    {
                        "title": "Domain",
                        "data": "Domain"
                    },
                    {
                        "title": "Grade Name",
                        "data": "GradeName"
                    }
                ],
                initComplete: function () {

                    $('#competenceTable tfoot th').each(function (i) {
                        var title = $('#competenceTable thead th').eq($(this).index()).text();
                        if (title != "Action" && title != "Active/Inactive")
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


            $('#competenceTable tfoot').insertAfter($('#competenceTable thead'));


        },

    })

}

/*----CALL: Check Empty Fields and Call Add Competence-----*/
function validateCompetences() {
    var checkEmpty = document.getElementsByClassName("checkRequired");
    var OK = true;
    for (var i = 0; i < checkEmpty.length; i++) {
        if (checkEmpty[i].value == "0") {
            OK = false;
        }
        else if (checkEmpty[i].value == "") {
            OK = false;
        }
        
    }
    return OK;
}

/*-----------POST: Add Competence API----------------*/
function addNewCompetence() {
    var OK = validateCompetences();
    if (OK) {
        var competence = new Object();
        competence.competencyServiceArea = $("#competenceServ").val();
        competence.description = $("#descriptionCompetence").val();
        competence.vendorID = $('#vendorCompetence').val();
        competence.competenceTypeID = $("#competenceType").val();
        competence.competenceGradeID = $('#competenceGrade').val();
        competence.technologyID = $('#technologyCompetence').val();
        competence.domainID = $('#domainCompetence').val();

        pwIsf.addLayer({ text: "Please wait ..." });
        $.isf.ajax({
            url: service_java_URL + "competenceController/insertCompetenceServiceArea",
            contentType: 'application/json',
            type: 'POST',
            data: JSON.stringify(competence),
            success: AjaxSucceeded,
            error: AjaxFailed,
            complete: function (xhr, statusText) {
                pwIsf.removeLayer();
            }

        });
        function AjaxSucceeded(data, textStatus) {
            if (data.isValidationFailed) {
                responseHandler(data);
            }
            else {
                pwIsf.alert({ msg: "Successfully Added", autoClose: 10 });
                LoadCompetenceData();
                clearFields();
            }


        }
        function AjaxFailed(xhr, status, statusText) {
            pwIsf.alert({ msg: 'Addition Failed', type: 'warning' });
        }
    }
    else {
        pwIsf.alert({ msg: "Please fill all the mandatory fields", type: "warning" });
    }
    
}

function clearFields() {
    $("#competenceServ").val('');
    $("#descriptionCompetence").val('');
    $('#vendorCompetence').val('0').trigger('change');
    $("#competenceType").val('0').trigger('change');
    $('#competenceGrade').val('0').trigger('change');
    $('#technologyCompetence').val('0').trigger('change');
    $('#domainCompetence').val('0').trigger('change');
}

/*-----------CALL: Open Modal for Training----------------*/
function openTrainingModule(thisObj)
{
    let jsonData = $(thisObj).data('details');
    vendorID = jsonData.VendorID;
    competenceID = jsonData.CompetenceID;
    deliveryCompetenceID = jsonData.DeliveryCompetenceID;
    selectCompetenceUpgrade();
    getTrainingData();
    $("#editAccessProfile").modal("show");
    $('#trainingHeading').html("").append("Add Training for Competence ID: " + competenceID);
}

/*-----------GET: Get Training Datatable----------------*/
function getTrainingData() {
    if ($.fn.dataTable.isDataTable('#trainingTable')) {
        oTable.destroy();
        $('#trainingTable').empty();
    }
    pwIsf.addLayer({ text: "Please wait ..." });
    $.isf.ajax({

        url: service_java_URL + "competenceController/getTrainingData/" + competenceID,
        success: function (data) {
            pwIsf.removeLayer();
            $('#div_table').show();
            $('#div_table_message').hide();

            $.each(data, function (i, d) {

                let jsonData = JSON.stringify({
                    CompetenceID: d.CompetenceID, VendorID: d.VendorID, DeliveryCompetenceID: d.DeliveryCompetence

                });

                //d.actionIcon = '<div style="display:flex"><a href="#addTraining" class="icon-edit" title="Add Training" data-details= \'' + jsonData + '\' data-toggle="modal"  onclick="openTrainingModule(this)">' + getIcon('add') + '</a>';
            }),
                $("#trainingTable").append($('<tfoot><tr><th></th><th></th><th>Vendor Name</th><th>Vendor Name</th><th>Country</th><th>Contact</th></tfoot>'));
            oTable = $('#trainingTable').DataTable({
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
                    //{
                    //    "title": "Action",
                    //    "targets": 'no-sort',
                    //    "orderable": false,
                    //    "searchable": false,
                    //    "data": "actionIcon",
                    //},
                    {
                        "title": "External Training ID",
                        "data": "ExternalTrainingID"
                    },
                    {
                        "title": "Training ID",
                        "data": "TrainingID",
                    },
                    {
                        "title": "Training Name",
                        "data": "TrainingName",
                    },
                    {
                        "title": "Training Type",
                        "data": "Trainingtype"
                    },
                    {
                        "title": "Competency Service Area",
                        "data": "Competency_Service_Area"
                    },
                    {
                        "title": "Competency Upgrade",
                        "data": "CompetencyUpgrade"
                    }
                    
                ],
                initComplete: function () {

                    $('#trainingTable tfoot th').each(function (i) {
                        var title = $('#trainingTable thead th').eq($(this).index()).text();
                        if (title != "Action" && title != "Active/Inactive")
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


            $('#trainingTable tfoot').insertAfter($('#trainingTable thead'));


        },

    })

}

/*-----------POST: Save Training API Call------------------*/
function addTraining() {

    var OK = true;

    $('#tbEditName-Required').text("");
    $('#tbEditCode-Required').text("");
    $('#tbEditCountry-Required').text("");
    $('#tbEditManager-Required').text("");
    if ($("#externalIDTraining").val() == null || $("#externalIDTraining").val() == "") {
        $('#tbEditName-Required').text("External ID is required");

        OK = false;
    }
    else if ($("#trainingName").val() == null || $("#trainingName").val() == "") {
        $('#tbEditCode-Required').text("Training Name is required");

        OK = false;
    }
    else if ($("#trainingType").val() == "") {

        $('#tbEditManager-Required').text("Training Type is required");
        OK = false;
    }
    else if ($('#competencyUpgrade').val() == "0") {

        $('#tbEditCountry-Required').text("Competency Upgrade is required");
        OK = false;
    }
    if (OK) {
        var training = new Object();
        training.trainingName = $("#trainingName").val();
        training.trainingType = $("#trainingType").val();
        training.vendorID = vendorID;
        training.externalTrainingID = $('#externalIDTraining').val();
        //training.competenceID =competenceID;
        training.competenceUpgradeID = $('#competencyUpgrade').val();
        training.deliveryCompetence = deliveryCompetenceID;

        pwIsf.addLayer({ text: "Please wait ..." });
        $.isf.ajax({
            url: service_java_URL + "competenceController/insertTraining",
            context: this,
            crossdomain: true,
            processData: true,
            contentType: 'application/json',
            type: 'POST',
            data: JSON.stringify(training),
            xhrFields: {
                withCredentials: false
            },
            success: AjaxSucceeded,
            error: AjaxFailed,
            complete: function (xhr, statusText) {
                pwIsf.removeLayer();
                $("#trainingName").val('');
                $("#trainingType").val('');
                $('#externalIDTraining').val('');
                $('#competencyUpgrade').val('').trigger('change');
            }

        });

        function AjaxSucceeded(data, textStatus) {
            if (data.isValidationFailed) {
                errorHandler(data);
            }
            else {
                getTrainingData();
                pwIsf.alert({ msg: "Training Successfully Added", autoClose: 10 });
            }


        }
        function AjaxFailed(xhr, status, statusText) {
            pwIsf.alert({ msg: 'Training Addition Failed', type: 'warning' });
        }


    }
}

/*-----------POST: Enable/Disable Competence-----------*/
function enableDisableCompetence(Code) {
    var checkbox = document.querySelector('input[id="togBtnSource_' + Code + '"]');
    var serviceUrl = service_java_URL + "competenceController/enableDisableCompetence/" + Code + "/" + signumGlobal;
    var enable = true; var disable = false;
    if (checkbox.checked) {
        pwIsf.confirm({
            title: 'Enable Competence', msg: 'Do you wish to enable the Competence?', type: 'success',
            'buttons': {
                'YES': {
                    'action': function () {
                        pwIsf.addLayer({ text: "Please wait ..." });
                        $.isf.ajax({
                            url: service_java_URL + "competenceController/enableDisableCompetence/" + Code + "/" + enable,
                            context: this,
                            crossdomain: true,
                            processData: true,
                            contentType: 'application/json',
                            type: 'POST',
                            success: function (data) {
                                //if (data.isValidationFailed) {
                                //    errorHandler(data);
                                //}
                                //else {
                                //    getTrainingData();
                                //    pwIsf.alert({ msg: "Training Successfully Added", autoClose: 10 });
                                //}
                                pwIsf.alert({ msg: "Successfully Enabled.", autoClose: 10 });
                            },
                            error: function (xhr, status, statusText) {
                                pwIsf.alert({ msg: 'Error in Enabling', type: 'warning', });
                                LoadCompetenceData();
                            },
                            complete: function (xhr, statusText) {
                                pwIsf.removeLayer();
                                //location.reload();
                            }

                        });
                    }
                },
                'NO': {
                    'action': function () {
                        LoadCompetenceData();
                    }
                }

            }
        });
    }
    else {
        pwIsf.confirm({
            title: 'Disable Competence', msg: 'Do you wish to disable the Competence?', type: 'success',
            'buttons': {
                'YES': {
                    'action': function () {
                        pwIsf.addLayer({ text: "Please wait ..." });
                        $.isf.ajax({
                            url: service_java_URL + "competenceController/enableDisableCompetence/" + Code + "/" + disable,
                            context: this,
                            crossdomain: true,
                            processData: true,
                            contentType: 'application/json',
                            type: 'POST',
                            success: function (data) {
                                pwIsf.alert({ msg: "Successfully Disabled.", autoClose: 10 });
                            },
                            error: function (xhr, status, statusText) {
                                pwIsf.alert({ msg: 'Error in Disabling', type: 'warning', });
                                LoadCompetenceData();
                            },
                            complete: function (xhr, statusText) {
                                pwIsf.removeLayer();
                                //location.reload();
                            }

                        });
                    }
                },
                'NO': {
                    'action': function () {
                        LoadCompetenceData();
                    }
                }

            }
        });
    }
}

/*----------Call: Reset File-----------*/
function resetFile() {
    var $el = $('#bulkUploadModal');
    $el.wrap('<form>').closest('form').get(0).reset();
    $el.unwrap();
}

/*-----------Call: Form Error----------*/
function responseHandlerForASP(data) {
    if (data.formErrorCount > 0 && data.formMessageCount > 0) {
        var errorIcon = '<i class="fa fa-times-circle" style="font-size:22px;color:#ff3131;"></i>';
        var successIcon = '<i class="fa fa-thumbs-up" style="font-size:22px;color:#09de09;"></i>';
        var str = "<ul style='list-style-type: none;'>";
        for (var i = 0; i < Object.keys(data.formErrors).length; i++) {
            str += "<li>" + errorIcon + " " + data.formErrors[i].replace(/\n/g, '<br/>') + "</li>";
        }
        str += "</ul><ul style='list-style-type: none;'>";;
        for (var i = 0; i < Object.keys(data.formMessages).length; i++) {
            str += "<li>" + successIcon + " " + data.formMessages[i].replace(/\n/g, '<br/>') + "</li>";
        }
        str += "</ul>"
        pwIsf.alert({ msg: str, type: 'info' });
        //return false;
    }

    else if (data.formErrorCount > 0) {
        var str = "Error(s):";
        for (var i = 0; i < Object.keys(data.formErrors).length; i++) {
            str += "<li>" + data.formErrors[i].replace(/\n/g, '<br/>') + "</li>";
        }
        pwIsf.alert({ msg: str, type: 'error' });
        return false;
    }
    else if (data.formMessageCount > 0) {
        var str = "";
        for (var i = 0; i < Object.keys(data.formMessages).length; i++) {
            str += data.formMessages[i].replace(/\n/g, '<br/>');
        }
        pwIsf.alert({ msg: str, type: 'success', autoClose: 3 });
        return true;
    }
    else if (data.formWarningCount > 0) {
        var str = "";
        for (var i = 0; i < Object.keys(data.formWarnings).length; i++) {
            str += data.formWarnings[i].replace(/\n/g, '<br/>');
        }
        pwIsf.alert({ msg: str, type: 'warning' });
        return true;
    }

}

/*---------POST: Bulk Upload Competence-----------*/
function uploadBulkCompetence() {

    let data = new FormData();
    let fileName = fl;
    data.append("file", fileName);
    pwIsf.addLayer({ text: "Please wait ..." });
    $.isf.ajax({
        type: "POST",
        enctype: "multipart/form-data",
        url: service_java_URL_VM + "competenceController/insertCompetenceDataBulk/" + signumGlobal,
        data: data,
        processData: false,
        contentType: false,
        cache: false,
        timeout: 600000,
        success: AjaxSucceeded,
        error: AjaxFailed
    }); // end ajax call

    function AjaxSucceeded(data, textStatus) {
        pwIsf.removeLayer();
        if (data.isValidationFailed) {
            responseHandlerForASP(data);
        }
        else {
            resetFile();
            responseHandlerForASP(data);
            setTimeout(function () {
                $('#bulkUploadModal').modal('hide');
            }, 2000);
            LoadCompetenceData();
        }

    }
    function AjaxFailed(xhr, status, statusText) {
        pwIsf.removeLayer();
        pwIsf.alert({ msg: 'An error has occurred, please check your file again!', type: 'error' });
    }

}