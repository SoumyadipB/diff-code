const EditCustomerCountryId = 'editCustomerCountry';

$(document).ready(function () {
    getCustomers();
    selectCountry();

});

//  #region CUSTOMERS
function getCustomers() {

    var table = $('#tblCustomers').DataTable({
        responsive: true,
        "retrieve": true,
        "destroy": true,
    });
    table.clear();
    pwIsf.addLayer({ text: C_LOADDER_MSG });
    $.isf.ajax({
        url: `${service_java_URL}activityMaster/getCustomerDetails`,
        success: function (data) {

            $.each(data, function (i, d) {
                const jsonData = JSON.stringify({
                    customerName: d.customerName, country: d.country, customerID: d.customerID

                });
                d.actionIcon = '<div style="display:flex">'
                    + '<a href="#editUnit" class="icon-edit" title="Edit Delivery Unit " data-details= \'' + jsonData + '\' data-toggle="modal"  onclick="showEditCustomer(this)">'
                    + getIcon('edit') + '</a>';
                d.actionIcon += '<a class="icon-delete lsp" title="Delete Customer" onclick="deleteCustomer('+ d.customerID + ')">' + getIcon('delete') + '</a></div>';
                table.row.add([d.actionIcon,d.customerName, d.country]);
            })
            table.draw();
            pwIsf.removeLayer();
        },
        error: function (xhr, status, statusText) {
            alert("Fail " + xhr.responseText);
            alert("status " + xhr.status);
            console.log('An error occurred');
            pwIsf.removeLayer();
        }
    });
}

function showEditCustomer(thisObj) {
    const jsonData = $(thisObj).data('details');
    console.log(jsonData);
    var customerName = document.getElementById('editCustomerName');
    customerName.setAttribute("value", jsonData.customerName);
    var customerID = document.getElementById('editCustomerID');
    customerID.setAttribute("value", jsonData.customerID);

    var temp = jsonData.country.split("-");
    const textToFind = temp[0].trim();
    var textToFind2 = '\"' + textToFind + '\"';
    console.log(textToFind);
    var dd = document.getElementById(EditCustomerCountryId);
    var it;
    for (it = 0; it < dd.options.length; it++) {
        if (dd.options[it].text === textToFind || dd.options[it].text === textToFind2) {
            break;
        }
    }
    var txt = dd.options[it].value;
    console.log(txt);
    var $newOption = $("<option selected='selected'></option>").val(txt).text(jsonData.country);

    $(`#${EditCustomerCountryId}`).append($newOption).trigger('change');

    $("#editCustomerModal").modal("show");
}

function deleteCustomer(CustomerId) {

    pwIsf.confirm({
        title: 'Delete Customer', msg: 'Are you sure to delete this Customer ? ',
        'buttons': {
            'Yes': {
                'action': function () {
                    pwIsf.addLayer({ text: 'Please wait ...' });

                    $.isf.ajax({
                        url: `${service_java_URL}activityMaster/deleteCustomerDetails/${CustomerId}/${signumGlobal}`,
                        type: 'POST',
                        crossDomain: true,
                        context: this,
                        processData: true,
                        contentType: "application/json",
                        success: function (data) {

                            pwIsf.alert({ msg: "Successfully Deleted.", autoClose: 3 });
                            location.reload();

                        },
                        error: function (xhr, status, statusText) {
                            pwIsf.alert({ msg: 'Customer delete Failed', type: 'error' });
                        },
                        complete: function (xhr, statusText) {
                            pwIsf.removeLayer();
                        }
                    });

                }
            },
            'No': {
                'action': function () {
                    //empty response
                }
            },

        }
    });
}



function ValidateAddCustomer() {
    var OK = true;

    $('#tbAddNewCustomer-Required').text("");
    if ($("#name1").val() == null || $("#name1").val() == "") {
        $('#tbAddNewCustomer-Required').text("Customer  is required");

        OK = false;
    }
    if (OK) {
        addCustomer();
    }
}

function updateCustomer() {

    var OK = true;
    $('#editCustomerNameRequired').text("");
    $('#editCustomerCountryRequired').text("");
    if ($("#editCustomerName").val() == null || $("#editCustomerName").val() == "") {
        $('#editCustomerNameRequired').text("Customer Name is required");
        OK = false;
    }
    else if ($(`#${EditCustomerCountryId} option:selected`).text() == "") {

        $('#editCustomerCountryRequired').text("Vendor Country is required");
        OK = false;
    }
    if (OK) {
        var customer = new Object();
        customer.country = $(`#${EditCustomerCountryId} option:selected`).text();
        customer.customerName = $("#editCustomerName").val();
        customer.customerID = $("#editCustomerID").val();
        customer.countryID = null;
        customer.isActive = 1;
        customer.signum = signumGlobal;

        console.log(customer);

        pwIsf.addLayer({ text: "Please wait ..." });
        $.isf.ajax({
            url: `${service_java_URL}activityMaster/editCustomerName`,
            context: this,
            crossdomain: true,
            processData: true,
            contentType: 'application/json',
            type: 'POST',
            data: JSON.stringify(customer),
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
                pwIsf.alert({ msg: "Successfully Updated", autoClose: 10 });
            }

            location.reload();

        }
        function AjaxFailed(xhr, status, statusText) {
            pwIsf.alert({ msg: 'Customer Updation Failed', type: 'warning' });
        }
    }
}

function ClearData() {
    $('#name1').val("");
    $('#tbAddNewCustomer-Required').text("");
}

function addCustomer() {
    var cus = new Object();
    cus.customerName = $("#name1").val();
    cus.countryID = $('#sel1').val();
    cus.country = $("#sel1 option:selected").text();
    cus.signum = signumGlobal;
   
    $.isf.ajax({
        url: `${service_java_URL}activityMaster/saveCustomerDetails`,
        context: this,
        crossdomain: true,
        processData: true,
        contentType: 'application/json',
        type: 'POST',
        data: JSON.stringify(cus),
        xhrFields: {
            withCredentials: false
        },
        success: AjaxSucceeded,
        error: AjaxFailed
    });
    function AjaxSucceeded(data, textStatus) {
        if (!data.isValidationFailed) {

            pwIsf.alert({ msg: data.formMessages[0], type: 'success' });
        }
        else {
            pwIsf.alert({ msg: data.formErrors[0], type: 'error' });
        }
    }
    function AjaxFailed(result) {
        pwIsf.alert({ msg: 'There is an issue in the UI/API, please contact developer', type: 'error' });

    }
}


function refreshCustomerTable() {
    $('#addcus').modal('hide');
    $('.modal-backdrop').remove();
    getCustomers();
}

function selectCountry() {


    $.isf.ajax({
        url: `${service_java_URL}activityMaster/getCountries`,
        success: function (data) {
            $('#sel1').append('<option value="' + 0 + '"></option>');
            $.each(data.responseData, function (i, d) {
                $('#sel1').append('<option value="' + d.countryID + '">' + d.countryName + '</option>');

            })

            $(`#${EditCustomerCountryId}`).append('<option value="' + 0 + '"></option>');
            $.each(data.responseData, function (i, d) {
                $(`#${EditCustomerCountryId}`).append('<option value="' + d.countryID + '">' + d.countryName + '</option>');

            })
        },
        error: function (xhr, status, statusText) {
            console.log('An error occurred on getCountries');
        }
    });

}
