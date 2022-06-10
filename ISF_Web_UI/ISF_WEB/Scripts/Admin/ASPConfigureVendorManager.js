$(document).ready(function () {

    LoadVendorData();
    selectCountry();
    getSignum();


});
var fl = null;


function LoadVendorData() {
    if ($.fn.dataTable.isDataTable('#Access_Profile_table')) {
        oTable.destroy();
        $('#Access_Profile_table').empty();
    }

    $.isf.ajax({

        url: service_java_URL + "aspManagement/getAllAspVendors",
        //crossDomain: true,
        success: function (data) {
            pwIsf.removeLayer();
            $('#div_table').show();
            $('#div_table_message').hide();

            $.each(data, function (i, d) {

                let jsonData = JSON.stringify({
                    vendorCode: d.vendorCode, country: escape(d.country), vendorName: d.vendorName, vendorContactDetails: d.vendorContactDetails, managerSignum: d.managerSignum, isActive: d.isActive, signum: d.signum, aspVendorDetailId: d.aspVendorDetailId

                });

                d.actionIcon = '<div style="display:flex"><a href="#editUnit" class="icon-edit" title="Edit Delivery Unit " data-details= \'' + jsonData + '\' data-toggle="modal"  onclick="updateVendor(this)">' + getIcon('edit') + '</a>';


            }),
                console.log(data);
            $("#Access_Profile_table").append($('<tfoot><tr><th></th><th></th><th>Vendor Name</th><th>Country</th><th>Contact</th><th>Vendor Code</th><th>Manager</th></tfoot>'));
            oTable = $('#Access_Profile_table').DataTable({
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
                            if (data.isActive) {
                                return '<label class="switchSource"><input type="checkbox" checked class="toggleActive" onclick="deleteASPVendor(\'' + data.vendorCode + '\')" id="togBtnSource_' + data.vendorCode + '" /><div class="sliderSource round"><span class="onSource">Enabled</span><span class="offSource">Disabled</span></div></label>';
                            }
                            else {
                                return '<label class="switchSource"><input type="checkbox" class="toggleActive" onclick="deleteASPVendor(\'' + data.vendorCode + '\')" id="togBtnSource_' + data.vendorCode + '"/><div class="sliderSource round"><span class="onSource">Enabled</span><span class="offSource">Disabled</span></div></label>';

                            }
                        },
                    },
                    {
                        "title": "Vendor Code",
                        "data": "vendorCode",
                    },
                    {
                        "title": "Vendor Name",
                        "data": "vendorName",
                    },
                    {
                        "title": "Vendor Country",
                        "data": "country",
                        "defaultContent": "-"
                    },
                    {
                        "title": "Status", "targets": 'no-sort', "orderable": false, "data": null,
                        "render": function (data, type, row, meta) {
                            if (data.isActive == 1) {
                                return "Enabled";
                            }
                            else {
                                return "Disabled";
                            }

                        }
                    },
                    {
                        "title": "Vendor Contact Details",
                        "data": "vendorContactDetails",
                        "defaultContent": "-"
                    },
                    {
                        "title": "Manager",
                        "data": "managerSignum",
                        "defaultContent": "-"
                    },
                    


                ],
                initComplete: function () {

                    $('#Access_Profile_table tfoot th').each(function (i) {
                        var title = $('#Access_Profile_table thead th').eq($(this).index()).text();
                        if (title != "Action" && title != "Active/Inactive")
                            $(this).html('<input type="text" class="form-control" style="font-size:12px;"  placeholder="Search ' + title + '" data-index="' + i + '" />');
                    });
                    var api = this.api();
                    api.columns().every(function () {
                        var that = this;
                        $('#Access_Profile_table :input', this.footer()).on('keyup change', function () {
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


            $('#Access_Profile_table tfoot').insertAfter($('#Access_Profile_table thead'));


        },

    })

}


function selectCountry() {


    $.isf.ajax({
        url: service_java_URL + "activityMaster/getCountries",
        success: function (data) {
            $('#VendorContryId').append('<option value="' + 0 + '"></option>');
            $.each(data.responseData, function (i, d) {
                $('#VendorContryId').append('<option value="' + d.countryID + '">' + d.countryName + '</option>');

            })

            $('#ModalVendorCountry').append('<option value="' + 0 + '"></option>');
            $.each(data.responseData, function (i, d) {
                $('#ModalVendorCountry').append('<option value="' + d.countryID + '">' + d.countryName + '</option>');

            })
        },
        error: function (xhr, status, statusText) {
            console.log('An error occurred on getCountries');
        }
    });


}

function getSignum() {
    $("#ManagerID").autocomplete({

        source: function (request, response) {
            $.isf.ajax({
                //url: service_java_URL + "activityMaster/getEmployeesByFilter",
                url: service_java_URL + "activityMaster/getASPMsByFilter",
                type: "POST",
                data: {
                    term: request.term
                },
                success: function (data) {
                    var result = [];
                    if (data.length == 0) {

                        showErrorMsg('Signum-Required', 'Please enter a valid Signum.');
                        $('#ManagerID').val("");
                        $('#ManagerID').focus();
                        response(result);

                    }
                    else {
                        hideErrorMsg('Signum-Required');

                        $("#ManagerID").autocomplete().addClass("ui-autocomplete-loading");
                        $.each(data, function (i, d) {
                            result.push({
                                "label": d.signum + "/" + d.employeeName,
                                "value": d.signum

                            });


                        })
                        response(result);
                        $("#ManagerID").autocomplete().removeClass("ui-autocomplete-loading");
                    }

                }
            });
        },
        minLength: 3
    });

    $("#ManagerID").autocomplete("widget").addClass("fixedHeight");

    $("#ModalManagerID").autocomplete({

        source: function (request, response) {
            $.isf.ajax({

                url: service_java_URL + "activityMaster/getASPMsByFilter",
                type: "POST",
                data: {
                    term: request.term
                },
                success: function (data) {
                    var result = [];
                    if (data.length == 0) {

                        showErrorMsg('Signum-Required', 'Please enter a valid Signum.');
                        $('#ModalManagerID').val("");
                        $('#ModalManagerID').focus();
                        response(result);

                    }
                    else {
                        hideErrorMsg('Signum-Required');

                        $("#ModalManagerID").autocomplete().addClass("ui-autocomplete-loading");
                        $.each(data, function (i, d) {
                            result.push({
                                "label": d.signum + "/" + d.employeeName,
                                "value": d.signum

                            });


                        })
                        response(result);
                        $("#ModalManagerID").autocomplete().removeClass("ui-autocomplete-loading");
                    }

                }
            });
        },
        minLength: 3
    });
    $("#ModalManagerID").autocomplete("widget").addClass("fixedHeight");


}


function ValidateVendorAdd() {
    var OK = true;

    var checkEmpty = document.getElementsByClassName("checkRequired");
    for (var i = 0; i < checkEmpty.length; i++) {
        if (checkEmpty[i].validity.valueMissing) {
        OK = false;
        checkEmpty[i].setCustomValidity("this field is required");
    } else {
        checkEmpty[i].setCustomValidity("");
        }
    }

    if (checkEmpty[2].value == "0") {
        OK = false;
        checkEmpty[2].setCustomValidity("this field is required");
    }

    if (OK) {
        addNewVendor();
        var checkEmpty = document.getElementsByClassName("checkRequired");
        //for (var i = 0; i < checkEmpty.length; i++) {
        //    checkEmpty.val("");
         //   }
        }
 

}



function addNewVendor() {
    var vendor = new Object();
    vendor.vendorCode = $("#VendorCode").val();
    vendor.vendorName = $("#VendorName").val();
    vendor.country = $('#VendorContryId option:selected').text();
    vendor.country = vendor.country.replace(/"/g, "");
    vendor.vendorContactDetails = $('#VendorContact').val();
    vendor.managerSignum = $('#ManagerID').val();
    vendor.isActive =1;
    vendor.signum = signumGlobal;
    console.log(vendor.country);
    //var service_data = "{\"vendor\":\"" + customerName + "\",\"country\":\"" + country + "\"\",\"countryID\":\"" + countryID + "\"}";


    pwIsf.addLayer({ text: "Please wait ..." });
    $.isf.ajax({
        url: service_java_URL+ "aspManagement/insertAspVendorDetails",
        context: this,
        async : false,
        crossdomain: true,
        processData: true,
        contentType: 'application/json',
        type: 'POST',
        data: JSON.stringify(vendor),
        xhrFields: {
            withCredentials: false
        },
        success: AjaxSucceeded,
        error: AjaxFailed,
        complete: function (xhr, statusText) {
            pwIsf.removeLayer();
        }
        
    });
    function AjaxSucceeded(data, textStatus) {
        if (data.isValidationFailed) {
            errorHandler(data);
        }
        else {
            //getExternalSourceTable();
            pwIsf.alert({ msg: "Successfully Added", autoClose: 10 });
        }
        
        //    location.reload();
       
    }
    function AjaxFailed(xhr, status, statusText) {
        pwIsf.alert({ msg: 'Vendor Addition Failed', type: 'warning' });
    }
    return false;
}


function updateVendor(thisObj) {
    let jsonData = $(thisObj).data('details');

    console.log(jsonData);
    var modalVendorName = document.getElementById('ModalVendorName');
    modalVendorName.setAttribute("value", jsonData.vendorName);
    var modalVendorCode = document.getElementById('ModalVendorCode');
    modalVendorCode.setAttribute("value", jsonData.vendorCode);
    var modalVendorContact = document.getElementById('ModalVendorContact');
    modalVendorContact.setAttribute("value", jsonData.vendorContactDetails);
   // var modalVendorManager = document.getElementById('ModalManagerID');
    //modalVendorManager.setAttribute("value", jsonData.managerSignum);
    $("#ModalManagerID").val(jsonData.managerSignum);
    var modalVendorRowId = document.getElementById('aspVendorDetailId');
    modalVendorRowId.setAttribute("value", jsonData.aspVendorDetailId);


    var textToFind = unescape(jsonData.country);
    var textToFind2 = '\"' + textToFind + '\"';
    console.log(textToFind + "  " + jsonData.vendorName);
    var dd = document.getElementById('ModalVendorCountry');
    var it;
    for (it = 0; it < dd.options.length; it++) {
        if (dd.options[it].text == textToFind || dd.options[it].text == textToFind2) {
            break;
        }
    }
    var txt = dd.options[it].value;
    
    
    var $newOption = $("<option selected='selected'></option>").val(txt).text(unescape(jsonData.country))

    $("#ModalVendorCountry").append($newOption).trigger('change');
    $("#editAccessProfile").modal("show");



}

function updateVendorFinal() {
    
    var OK = true;

    console.log($("#ModalManagerID").val());
    console.log($("#ModaLVendorCountry").val())
    $('#tbEditName-Required').text("");
    $('#tbEditCode-Required').text("");
    $('#tbEditCountry-Required').text("");
    $('#tbEditManager-Required').text("");
    if ($("#ModalVendorName").val() == null || $("#ModalVendorName").val() == "") {
        $('#tbEditName-Required').text("Vendor name is required");

        OK = false;
    }
    else if ($("#ModalVendorCode").val() == null || $("#ModalVendorCode").val() == "") {
        $('#tbEditCode-Required').text("Vendor Code  is required");

        OK = false;
    }
    else if ($("#ModalManagerID").val() == "") {

        $('#tbEditManager-Required').text("Manager is required");
        OK = false;
    }
    else if ($('#ModalVendorCountry option:selected').text() == "") {

        $('#tbEditCountry-Required').text("Vendor Country is required");
        OK = false;
    }
    


    if (OK) {
        var vendor = new Object();
        vendor.vendorCode = $("#ModalVendorCode").val();
        vendor.vendorName = $("#ModalVendorName").val();
        vendor.country = $('#ModalVendorCountry option:selected').text();
        vendor.country = vendor.country.replace(/"/g, "");
        vendor.vendorContactDetails = $('#ModalVendorContact').val();
        vendor.managerSignum = $('#ModalManagerID').val();
        vendor.aspVendorDetailId = $('#aspVendorDetailId').val();
        vendor.signum = signumGlobal;

        console.log(vendor);
        pwIsf.addLayer({ text: "Please wait ..." });
        $.isf.ajax({
            url: service_java_URL + "aspManagement/updateAspVendorDetails",
            context: this,
            crossdomain: true,
            processData: true,
            contentType: 'application/json',
            type: 'POST',
            data: JSON.stringify(vendor),
            xhrFields: {
                withCredentials: false
            },
            success: AjaxSucceeded,
            error: AjaxFailed,
            complete: function (xhr, statusText) {
                pwIsf.removeLayer();
            }

        });

        function AjaxSucceeded(data, textStatus) {
            if (data.isValidationFailed) {
                errorHandler(data);
            }
            else {
                //getExternalSourceTable();
                pwIsf.alert({ msg: "Successfully Updated", autoClose: 10 });
            }

            location.reload();

        }
        function AjaxFailed(xhr, status, statusText) {
            pwIsf.alert({ msg: 'Vendor Updation Failed', type: 'warning' });
        }

        
    }
}

function deleteASPVendor(Code) {
    //console.log(Code);
    var checkbox = document.querySelector('input[id="togBtnSource_' + Code + '"]');
    var serviceUrl = service_java_URL + "aspManagement/enableDisableAspVendorDetails?vendorCode=" + Code + "&signum=" + signumGlobal;
    if (ApiProxy)
        serviceUrl = service_java_URL + "aspManagement/enableDisableAspVendorDetails?vendorCode=" + Code + encodeURIComponent("&signum=" + signumGlobal);
    if (checkbox.checked) {
        pwIsf.confirm({
            title: 'Enable Vendor', msg: 'Do you wish to enable the vendor?', type: 'success',
            'buttons': {
                'YES': {
                    'action': function () {
                        pwIsf.addLayer({ text: "Please wait ..." });
                        $.isf.ajax({
                            //url: service_java_URL + "aspManagement/enableDisableAspVendorDetails?vendorCode=" + Code + "&signum=" + signumGlobal,
                            url: serviceUrl,
                            context: this,
                            crossdomain: true,
                            processData: true,
                            contentType: 'application/json',
                            type: 'POST',
                            success: function (data) {
                                pwIsf.alert({ msg: "Successfully Enabled.", autoClose: 10 });
                            },
                            error: function (xhr, status, statusText) {
                                pwIsf.alert({ msg: 'Error in Enabling', type: 'warning', });
                            },
                            complete: function (xhr, statusText) {
                                pwIsf.removeLayer();
                                location.reload();
                            }

                        });
                    }
                },
                'NO': {
                    'action': function () {
                        LoadVendorData();
                    }
                }

            }
        });
    }
    else {
        pwIsf.confirm({
            title: 'Disable Vendor', msg: 'Do you wish to disable the vendor?', type: 'success',
            'buttons': {
                'YES': {
                    'action': function () {
                        pwIsf.addLayer({ text: "Please wait ..." });
                        $.isf.ajax({
                            url: serviceUrl,
                            //url: service_java_URL + "aspManagement/enableDisableAspVendorDetails?vendorCode=" + Code + "&signum=" + signumGlobal,
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
                            },
                            complete: function (xhr, statusText) {
                                pwIsf.removeLayer();
                                location.reload();
                            }

                        });
                    }
                },
                'NO': {
                    'action': function () {
                        LoadVendorData();
                    }
                }

            }
        });
    }
}

function resetFile() {
    var $el = $('#aspBulkVendorUpload');
    $el.wrap('<form>').closest('form').get(0).reset();
    $el.unwrap();
}

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


//To Upload Bulk ASP Vendors
function uploadBulkVendor() {

    let data = new FormData();
    let fileName = fl;
    data.append("file", fileName);
    pwIsf.addLayer({ text: "Please wait ..." });
    $.isf.ajax({
        type: "POST",
        enctype: "multipart/form-data",
        url: service_java_URL_VM + "aspManagement/insertAspVendorDetailsFromFile",
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
                $('#aspBulkVendorFile').modal('hide');
            }, 2000);
        }
        
    }
    function AjaxFailed(xhr, status, statusText) {
        pwIsf.removeLayer();
        pwIsf.alert({ msg: 'An error has occurred, please check your file again!', type: 'error' });
    }

}