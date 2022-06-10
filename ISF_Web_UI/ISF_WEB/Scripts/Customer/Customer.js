$(document).ready(function () {
    getCustomers();
    selectCountry();
});

//#region CUSTOMERS
function getCustomers() {
    var table = $('#tblCustomers').DataTable({
        responsive: true,
        "retrieve": true,
        "destroy": true,
        "lengthMenu": [[50], [50]],
    });
    table.clear();

    $.ajax({
        url: service_java_URL + "activityMaster/getCustomerDetails",
        success: function (data) {
            var html = "";
            $.each(data, function (i, d) {
                table.row.add([d.customerName, d.country]);
            })
            table.draw();
        },
        error: function (xhr, status, statusText) {
            alert("Fail " + xhr.responseText);
            alert("status " + xhr.status);
            console.log('An error occurred');
        }
    });
}

/*
function showEditVendor(ID, Domain) {
    $("#lblEditVendor").html(ID);
    $("#tbEditVendor").val(Domain);
}

function showDeleteVendor(ID) {
    $("#lblDeleteVendor").html(ID);
}

function deleteVendor() {
    var vendorID = $("#lblDeleteVendor").text();
    //var signum = "signum";

    $.ajax({
        url: service_java_URL + "activityMaster/deleteVendorDetails/" + vendorID + "/" + signumGlobal,
        success: function (data) {
            alert("Record Deleted");
            refreshVendorTable();
        },
        error: function (xhr, status, statusText) {
            alert("Fail " + xhr.responseText);
            alert("status " + xhr.status);
            console.log('An error occurred');
        }
    });

}
*/

function ValidateAddCustomer() {
    var OK = true;

    $('#tbAddNewCustomer-Required').text("");
    if ($("#name1").val() == null || $("#name1").val() == "") {
        $('#tbAddNewCustomer-Required').text("Customer  is required");

        OK = false;
    }
    if (OK)
        addCustomer();
}
/*
function ValidateEditVendor() {

    var OK = true;
    $('#tbEditVendor-Required').text("");
    if ($("#tbEditVendor").val() == null || $("#tbEditVendor").val() == "") {
        $('#tbEditVendor-Required').text("Vendor  is required");
        OK = false;
    }
    if (OK)
        updateVendor();
}
*/
function ClearData() {
    $('#name1').val("");
    $('#tbAddNewCustomer-Required').text("");
}

function addCustomer() {
    var cus = new Object();
    cus.customerName = $("#name1").val();
    cus.countryID = $('#sel1').val();
    cus.country = $("#sel1 option:selected").text();
    //var signum = "signum";

    //var service_data = "{\"vendor\":\"" + customerName + "\",\"country\":\"" + country + "\"\",\"countryID\":\"" + countryID + "\"}";

    $.ajax({
        url: service_java_URL + "activityMaster/saveCustomerDetails",
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
        alert("Customer Saved");
        //refreshCustomerTable();
    }
    function AjaxFailed(result) {
        alert("The Customer already exists");

    }
}
/*
function updateVendor() {
    var vendorID = $("#lblEditVendor").text();
    var vendor = $("#tbEditVendor").val();
    //var signum = "signum";

    //var service_data = "{\"vendorID?\":\"" + vendorID + "\",\"vendor?\":\"" + vendor + "\",\"lastModifiedBy??\":\"" + signum + "\"}";
    var service_data = '{"vendorID":"' + vendorID + '","vendor":"' + vendor + '","lastModifiedBy":"' + signumGlobal + '"}';

    $.ajax({
        url: service_java_URL + "activityMaster/updateVendorDetails",
        context: this,
        crossdomain: true,
        processData: true,
        contentType: 'application/json',
        type: 'POST',
        data: service_data,
        async: false,
        xhrFields: {
            withCredentials: false
        },
        success: AjaxSucceeded,
        error: AjaxFailed
    });
    function AjaxSucceeded(data, textStatus) {
        alert("Vendor Updated");
        refreshVendorTable();
    }
    function AjaxFailed(result) {
        alert("Failed to update Vendor");
        //alert(result.status + ' ' + result.statusText);
    }
}
*/
function refreshCustomerTable() {
    $('#addcus').modal('hide');

    //refreshView();
    $('.modal-backdrop').remove();
    getCustomers();
}

function selectCountry() {


    $.ajax({
        url: service_DotNET_URL + "Countries",
        success: function (data) {
            $('#sel1').append('<option value="' + 0 + '"></option>');
            $.each(data, function (i, d) {
                $('#sel1').append('<option value="' + d.CountryID + '">' + d.CountryName + '</option>');

            })
        },
        error: function (xhr, status, statusText) {
            console.log('An error occurred on getCountries');
        }
    });

}



/*

$('#addCust').on('click', function () {
    $("form#uploadTool").submit(function (event) {
        event.preventDefault();
        var cus = new Object();
        cus.customerName = $("#name1").val();
        cus.country = $("sel1").val();
        cus.active = document.frmTool.active.value;
        cus.createdBy = signumGlobal;
        cus.lastModifiedBy = signumGlobal;
     
        if ($("#name1").val() && !$("input[name='active']:checked").val()) {

            pwIsf.alert({ msg: "Please select tool status", type: 'warning' });

            return false;

        }

        $.ajax({

            url: service_java_URL + 'toolInventory/saveToolInventory',

            type: 'POST',

            crossDomain: true,

            context: this,

            contentType: "application/json",

            data: JSON.stringify(tool_name),



            success: function (returndata) {

                alert("Tool Added");

                location.reload();



            },

            error: function (xhr, status, statusText) {

                pwIsf.alert({ msg: 'Tool Addition Failed.Tool already exists.', type: 'warning' });

            },



        });



        return false;

    });

});
*/

//#end region CUSTOMERS