$(document).ready(function () {
    getVendors();
});

//#region VENDORS
function getVendors() {    
    var table = $('#tblVendors').DataTable({
                    responsive: true,
                    "retrieve": true,
                    "destroy": true,
                    "lengthMenu": [[50], [50]],
                });
    table.clear();  

    $.isf.ajax({
        url: service_java_URL + "activityMaster/getVendorDetails",
        success: function (data) {
            var html = "";
            $.each(data, function (i, d) { 
                table.row.add([d.vendor, '&thinsp;&thinsp;&thinsp;&thinsp;&thinsp;&thinsp;&thinsp;&thinsp;<button style="cursor: pointer;" data-toggle="modal" data-target="#EditVendor" class="fa fa-edit" onclick="showEditVendor(' + d.vendorID + ',\'' + d.vendor + '\')"></button>&thinsp;&thinsp;&thinsp;&thinsp;<button style="cursor: pointer;" data-toggle="modal" data-target="#DeleteVendor" class="fa fa-trash-o" onclick="showDeleteVendor(' + d.vendorID + ')"></button>']);
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

    $.isf.ajax({
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

function ValidateAddVendor() {
    var OK = true;
    
    $('#tbAddNewVendor-Required').text("");
    if ($("#tbAddNewVendor").val() == null || $("#tbAddNewVendor").val() == "") {
        $('#tbAddNewVendor-Required').text("Vendor  is required");
        
        OK = false;
    }
    if (OK)
        addVendor();
}

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

function ClearData() {
    $('#tbAddNewVendor').val("");
    $('#tbAddNewVendor-Required').text("");
}

function addVendor() {
    var vendor = $("#tbAddNewVendor").val();    
    //var signum = "signum";

    var service_data = "{\"vendor\":\"" + vendor + "\",\"createdBy\":\"" + signumGlobal + "\"}";

    $.isf.ajax({
        url: service_java_URL + "activityMaster/saveVendorDetails",
        context: this,
        crossdomain: true,
        processData: true,
        contentType: 'application/json',
        type: 'POST',
        data: service_data,
        xhrFields: {
            withCredentials: false
        },
        success: AjaxSucceeded,
        error: AjaxFailed
    });
    function AjaxSucceeded(data, textStatus) {
        alert("Vendor Saved");
        refreshVendorTable();
    }
    function AjaxFailed(result) {
        alert("The Vendor already exists");

    }
}

function updateVendor() {
    var vendorID = $("#lblEditVendor").text();
    var vendor = $("#tbEditVendor").val();
    //var signum = "signum";

    //var service_data = "{\"vendorID​\":\"" + vendorID + "\",\"vendor​\":\"" + vendor + "\",\"lastModifiedBy​​\":\"" + signum + "\"}";
    var service_data = '{"vendorID":"' + vendorID + '","vendor":"' + vendor + '","lastModifiedBy":"' + signumGlobal + '"}';

    $.isf.ajax({
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

function refreshVendorTable() {
    $('#AddVendor').modal('hide');
    $('#EditVendor').modal('hide');
    $('#DeleteVendor').modal('hide');
    //refreshView();
    $('.modal-backdrop').remove();
    getVendors();
}

//#end region VENDORS