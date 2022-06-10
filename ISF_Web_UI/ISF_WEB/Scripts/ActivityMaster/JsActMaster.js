//var signumGlobal = "signum";
$(document).ready(function () {
    $('#tbAddSAAvgEstdEffort').keypress(function (tecla) {
        var letterNumber = /^[0-9]+$/;
        if (!$('#tbAddSAAvgEstdEffort').val().match(letterNumber)) {
            var text = $('#tbAddSAAvgEstdEffort').val();
            text = $('#tbAddSAAvgEstdEffort').val().substring(0, text.length - 1);
            $('#tbAddSAAvgEstdEffort').val(text);
            $('#tbAddSAAvgEstdEffort-Required').text("Only numbers");
        }
        else
            $('#tbAddSAAvgEstdEffort-Required').text("");
    });
    

    getDomains();
});
var globFlChartJsonArrMaster = [];

//#region DOMAIN
function getDomains() {
    var table = $('#tblDomains').DataTable({
        responsive: true,
        retrieve: true,
        lengthMenu: [[50], [50]],
        "language": {
            "info": ""
        },
        searching: true,
        //responsive: true,
        //retrieve: true,
        "pageLength": 10,
        colReorder: true,
        dom: 'Bfrtip',       
        buttons: [
                'colvis', 'excelHtml5'
        ],
        
       // "destroy": true,
    });
    table.clear();

    $.isf.ajax({
        url: service_java_URL + "activityMaster/getDomainDetails",
        success: function (data) {
            $("#domainLoader").hide();
            $.each(data, function (i, d) {
               
                table.row.add([d.domain, '&thinsp;&thinsp;&thinsp;&thinsp;&thinsp;&thinsp;&thinsp;&thinsp;<button style="cursor: pointer;" data-toggle="modal" data-target="#EditDomain" class="fa fa-edit" onclick="showEditDomain(' + d.domainID + ',\'' + d.domain + '\')"></button>&thinsp;&thinsp;&thinsp;&thinsp;<button style="cursor: pointer;" data-toggle="modal" data-target="#DeleteDomain" class="fa fa-trash-o" onclick="showDeleteDomain(' + d.domainID + ')"></button>']);
            })
            //$("#tbodyDomains").append(html);
            table.draw();
        },
        error: function (xhr, status, statusText) {
            alert("Fail Get Domains " + xhr.responseText);
            alert("status " + xhr.status);
            console.log('An error occurred');
        }
    });
}

function getDomainByID(ID) {
    var domain;
    $.isf.ajax({
        url: service_java_URL + "activityMaster/getDomainDetailsByID/" + ID + "/" + signumGlobal,
        async: false,
        success: function (data) {
            $.each(data, function (i, d) {
                domain = d.domain;
            })
        },
        error: function (xhr, status, statusText) {
           
            console.log('An error occurred');
        }
    });
    return domain;
}

function showEditDomain(ID, Domain) {
    var arr = Domain.split('/');
    $("#lblEditDomain").html(ID);
    $("#tbEditDomain").val(arr[0]);       
    $("#tbEditSubdomain").val(arr[1]);
}

function showDeleteDomain(ID) {
    $("#lblDeleteDomain").html(ID);
}

function deleteDomain() {
    var domainID = $("#lblDeleteDomain").text();

    $.isf.ajax({
        url: service_java_URL + "activityMaster/deleteDomainDetails/" + domainID + "/" + signumGlobal,
        success: function (data) {
            alert("Record Deleted");
            refreshDomainTable();
        },
        error: function (xhr, status, statusText) {
            alert("Fail in delete domain" + xhr.responseText);
            console.log('An error occurred');
        }
    });

}

function refreshView() {
    window.location.href = 'Index';
}

function addDomain() {
    var domain = $("#tbAddNewDomain").val();
    var subdomain = $("#tbAddNewSubDomain").val();
    var signum = signumGlobal;

    var service_data = "{\"domain\":\"" + domain + "\",\"subDomain\":\"" + subdomain + "\",\"createdBy\":\"" + signum + "\",\"lastModifiedBy\":\"" + signum + "\"}";

    if (domain.includes('"') || subdomain.includes('"') || subdomain.includes("'") || domain.includes("'")) {

        alert("Domain and Subdomain cannot contain \" or ' ");
    }
    else {
        $.isf.ajax({
            url: service_java_URL + "activityMaster/saveDomainDetails/",
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
            alert("Domain Saved");
            refreshDomainTable();
        }
        function AjaxFailed(result) {
            alert("The Domain/Subdomain already exist.");

        }
    }

}

function updateDomain() {
    var domain = $("#tbEditDomain").val();
    var subdomain = $("#tbEditSubdomain").val();
    var signum = signumGlobal;
    var domainID = $("#lblEditDomain").text();

    var service_data = "{\"domain\":\"" + domain + "\",\"subDomain\":\"" + subdomain + "\",\"lastModifiedBy\":\"" + signum + "\",\"domainID\":\"" + domainID + "\"}";

    $.isf.ajax({
        url: service_java_URL + "activityMaster/updateDomainDetails/",
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
        alert("Domain Updated");
        refreshDomainTable();
    }
    function AjaxFailed(result) {
        alert("Domain/Subdomain already exist.");
        
    }

}

function refreshDomainTable() {
    $('#AddDomain').modal('hide');
    $('#EditDomain').modal('hide');
    $('#DeleteDomain').modal('hide');
    $('.modal-backdrop').remove();
    getDomains();

}

function ValidateAddDomain() {
    var OK = true;
    $('#tbAddNewDomain-Required').text("");
    if ($("#tbAddNewDomain").val() == null || $("#tbAddNewDomain").val() == "") {
        $('#tbAddNewDomain-Required').text("Domain  is required");
        OK = false;
    }
    $('#tbAddNewSubDomain-Required').text("");
    if ($("#tbAddNewSubDomain").val() == null || $("#tbAddNewSubDomain").val() == "") {
        $('#tbAddNewSubDomain-Required').text("Subdomain  is required");
        OK = false;
    }
    if (OK)
        addDomain();

}

function ValidateEditDomain() {

    var OK = true;
    $('#tbEditDomain-Required').text("");
    if ($("#tbEditDomain").val() == null || $("#tbEditDomain").val() == "") {
        $('#tbEditDomain-Required').text("Domain  is required");
        OK = false;
    }
    $('#tbEditSubdomain-Required').text("");
    if ($("#tbEditSubdomain").val() == null || $("#tbEditSubdomain").val() == "") {
        $('#tbEditSubdomain-Required').text("Subdomain  is required");
        OK = false;
    }
    if (OK)
        updateDomain();
}

function getServiceAreas() {

    var table = $('#tblServiceAreas').DataTable({
        responsive: true,
        retrieve: true,
        lengthMenu: [[50], [50]],
        "language": {
        "info": ""
    },
        searching: true,
        "pageLength": 10,
        colReorder: true,
        dom: 'Bfrtip',       
        buttons: [
                'colvis', 'excelHtml5'
        ],
    });
    table.clear();

    $.isf.ajax({
        url: service_java_URL + "activityMaster/getServiceAreaDetails",
        success: function (data) {
            $("#serviceLoader").hide();
            $.each(data, function (i, d) {
                
                table.row.add([d.serviceArea, '&thinsp;&thinsp;&thinsp;&thinsp;&thinsp;&thinsp;&thinsp;&thinsp;<button style="cursor: pointer;" data-toggle="modal" data-target="#EditSA" class="fa fa-edit" onclick="showEditServiceArea(' + d.serviceAreaID + ',\'' + d.serviceArea + '\')"></button>&thinsp;&thinsp;&thinsp;&thinsp;<button style="cursor: pointer;" data-toggle="modal" data-target="#DeleteSA" class="fa fa-trash-o" onclick="showDeleteServiceArea(' + d.serviceAreaID + ')"></button></td>']);
            })
           
            table.draw();
        },
        error: function (xhr, status, statusText) {
           
            console.log('An error occurred');
        }
    });
}

function getServiceAreaByID(ID) {
    var serviceArea;
    $.isf.ajax({
        url: service_java_URL + "activityMaster/getServiceAreaDetailsByID/" + ID + "/" + signumGlobal,
        async: false,
        success: function (data) {
            var html = "";
            $.each(data, function (i, d) {
                serviceArea = d.serviceArea;
            })
        },
        error: function (xhr, status, statusText) {
            alert("Fail " + xhr.responseText);
            alert("status " + xhr.status);
            console.log('An error occurred');
        }
    });
    return serviceArea;
}

function addServiceArea() {
    var serviceArea = $("#tbServiceArea").val();
    var subServiceArea = $("#tbSubServiceArea").val();
    var service_data = "{\"serviceArea\":\"" + serviceArea + "\",\"subServiceArea\":\"" + subServiceArea + "\",\"createdBy\":\"" + signumGlobal + "\"}";

    $.isf.ajax({
        url: service_java_URL + "activityMaster/saveServiceAreaDetails",
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
        alert("Service Area Saved");
        refreshServiceAreaTable();
    }
    function AjaxFailed(result) {
        alert("Service area/SubService area already exist.");
        
    }

}

function showEditServiceArea(ID, SA) {
    var arr = SA.split('/');
    $("#lblEditSA").html(ID);
    $("#tbEditServiceArea").val(arr[0]);
    $("#tbEditSubServiceArea").val(arr[1]);
}

function saveEditServiceArea() {
    var serviceArea = $("#tbEditServiceArea").val();
    var subServiceArea = $("#tbEditSubServiceArea").val();
    var signum = signumGlobal;
    var ID = $("#lblEditSA").text();
    var lastModifiedBy = signumGlobal;
    var service_data = "{\"serviceArea\":\"" + serviceArea + "\",\"subServiceArea\":\"" + subServiceArea + "\",\"signumID\":\"" + signum + "\",\"serviceAreaID\":\"" + ID + "\",\"lastModifiedBy\":\"" + lastModifiedBy + "\"}";
    $.isf.ajax({
        url: service_java_URL + "activityMaster/updateServiceAreaDetails",
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
        alert("Service Area Updated");
        refreshServiceAreaTable();
    }
    function AjaxFailed(result) {
        alert("Service area/SubService area already exist.");
        
    }

}

function showDeleteServiceArea(ID) {
    $("#lblDelServiceArea").html(ID);
}

function deleteServiceArea() {
    var SAID = $("#lblDelServiceArea").text();
    var signum = signumGlobal;

    $.isf.ajax({
        url: service_java_URL + "activityMaster/deleteServiceArea/" + SAID + "/" + signum,
        async: false,
        success: function (data) {
            alert("Record Deleted");
            refreshServiceAreaTable();
        },
        error: function (xhr, status, statusText) {
            alert("Fail in delete service area" + xhr.responseText);
            console.log('An error occurred');
        }
    });

}

function refreshServiceAreaTable() {

    $('#AddSA').modal('hide');
    $('#EditSA').modal('hide');
    $('#DeleteSA').modal('hide');
    $('.modal-backdrop').remove();
    getServiceAreas();
}

function ValidateAddServiceArea() {

    var OK = true;
    
    $('#tbServiceArea-Required').text("");
    if ($("#tbServiceArea").val() == null || $("#tbServiceArea").val() == "") {
        $('#tbServiceArea-Required').text("Service area is required");        
        OK = false;
    }
   
    $('#tbSubServiceArea-Required').text("");
    if ($("#tbSubServiceArea").val() == null || $("#tbSubServiceArea").val() == "") {
        $('#tbSubServiceArea-Required').text("Subservice area is required");        
        OK = false;
    }
    if (OK)
        addServiceArea();
}

function ValidateEditServiceArea() {

    var OK = true;
    
    $('#tbEditServiceArea-Required').text("");
    if ($("#tbEditServiceArea").val() == null || $("#tbEditServiceArea").val() == "") {
        $('#tbEditServiceArea-Required').text("Service area is required");        
        OK = false;
    }
    
    $('#tbEditSubServiceArea-Required').text("");
    if ($("#tbEditSubServiceArea").val() == null || $("#tbEditSubServiceArea").val() == "") {
        $('#tbEditSubServiceArea-Required').text("Subservice area is required");        
        OK = false;
    }
    if (OK)
        saveEditServiceArea();
}

function addTechnology() {
    var technology = $("#tbNewTechnology").val();

    var service_data = "{\"technology\":\"" + technology + "\",\"createdBy\":\"" + signumGlobal + "\"}";

    $.isf.ajax({
        url: service_java_URL + "activityMaster/saveTechnologyDetails",
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
        alert("Technology Saved");
        refreshTechnologyTable();
    }
    function AjaxFailed(result) {
        alert("Technology already exist.");
        
    }

}

function getTechnologies() {

    var table = $('#tblTechnology').DataTable({
        responsive: true,
        retrieve: true,
        lengthMenu: [[50], [50]],
        "language": {
            "info": ""
        },
        searching: true,
        "pageLength": 10,
        colReorder: true,
        dom: 'Bfrtip',
        buttons: [
                'colvis', 'excelHtml5'
        ],
    });
    table.clear();

    $.isf.ajax({
        url: service_java_URL + "activityMaster/getTechnologyDetails",
        success: function (data) {
            $("#techLoader").hide();
            $.each(data, function (i, d) {
                
                table.row.add([d.technology, '&thinsp;&thinsp;&thinsp;&thinsp;&thinsp;&thinsp;&thinsp;&thinsp;<button style="cursor: pointer;" data-toggle="modal" data-target="#EditTech" class="fa fa-edit" onclick="showEditTechnology(' + d.technologyID + ',\'' + d.technology + '\')"></button>&thinsp;&thinsp;&thinsp;&thinsp;<button style="cursor: pointer;" data-toggle="modal" data-target="#DeleteTech" class="fa fa-trash-o" onclick="showDeleteTechnology(' + d.technologyID + ')"></button>']);
            })           

            table.draw();
        },
        error: function (xhr, status, statusText) {           
            console.log('An error occurred');
        }
    });
}

function getTechnologyByID(ID) {
    var technology;
    $.isf.ajax({
        url: service_java_URL + "activityMaster/getTechnologyDetailsByID/" + ID + "/" + signumGlobal,
        async: false,
        success: function (data) {
            var html = "";
            $.each(data, function (i, d) {
                technology = d.technology;
            })
        },
        error: function (xhr, status, statusText) {
            alert("Fail " + xhr.responseText);
            alert("status " + xhr.status);
            console.log('An error occurred');
        }
    });
    return technology;
}

function showEditTechnology(ID, technology) {
    $("#lblEditTechnology").html(ID);
    $("#tbEditTechnology").val(technology);
}

function updateTechnology() {
    var technology = $("#tbEditTechnology").val();
    var signum = signumGlobal;
    var technologyID = $("#lblEditTechnology").text();

    var service_data = "{\"technology\":\"" + technology + "\",\"lastModifiedBy\":\"" + signum + "\",\"technologyID\":\"" + technologyID + "\"}";
    $.isf.ajax({
        url: service_java_URL + "activityMaster/updateTechnologyDetails",
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
        alert("Technology Updated");
    }
    function AjaxFailed(result) {
        alert("Technology already exist.");
        
    }
    refreshTechnologyTable();
}

function showDeleteTechnology(ID) {
    $("#lblDeleteTechnology").html(ID);
}

function deleteTechnology() {
    var technologyID = $("#lblDeleteTechnology").text();
    var signum = signumGlobal;

    $.isf.ajax({
        url: service_java_URL + "activityMaster/deleteTechnology/" + technologyID + "/" + signum,
        async: false,
        success: function (data) {
            alert("Record Deleted");
            refreshTechnologyTable();
        },
        error: function (xhr, status, statusText) {
            alert("Fail in delete technology" + xhr.responseText);
            console.log('An error occurred in delete Domain');
        }
    });

}

function refreshTechnologyTable() {


    $('#AddTech').modal('hide');
    $('#EditTech').modal('hide');
    $('#DeleteTech').modal('hide');
    
    $('.modal-backdrop').remove();
    getTechnologies();
}

function ValidateAddTechnology() {

    var OK = true;
    
    $('#tbNewTechnology-Required').text("");
    if ($("#tbNewTechnology").val() == null || $("#tbNewTechnology").val() == "") {
        $('#tbNewTechnology-Required').text("Techology is required");
        
        OK = false;
    }

    if (OK)
        addTechnology();

}

function ValidateEditTechnology() {

    var OK = true;
    
    $('#tbEditTechnology-Required').text("");
    if ($("#tbEditTechnology").val() == null || $("#tbEditTechnology").val() == "") {
        $('#tbEditTechnology-Required').text("Techology is required");
        
        OK = false;
    }

    if (OK)
        updateTechnology();

}

function onLoadSubActivity() {
    $("#tbodySubActivities tr").remove();

    getDomainsSA();
    getServiceAreasSA();
    getTechnologiesSA();
}

function getDomainsSA() {

    $('#cbxDomain')
        .find('option')
        .remove();
    $("#cbxDomain").select2("val", "");
    $('#cbxDomain').append('<option value="0">Select one</option>');

    $.isf.ajax({
        url: service_java_URL + "activityMaster/getDomainDetails",
        async: false,
        success: function (data) {
            $.each(data, function (i, d) {
                $('#cbxDomain').append('<option value="' + d.domainID + '">' + d.domain + '</option>');
            })
        },
        error: function (xhr, status, statusText) {            
            console.log('An error occurred');
        }
    });
}

function getServiceAreasSA() {
    $('#cbxSA')
        .find('option')
        .remove();
    $("#cbxSA").select2("val", "");
    $('#cbxSA').append('<option value="0">Select one</option>');
    $.isf.ajax({
        url: service_java_URL + "activityMaster/getServiceAreaDetails",
        async: false,
        success: function (data) {
            var html = "";
            $.each(data, function (i, d) {
                $('#cbxSA').append('<option value="' + d.serviceAreaID + '">' + d.serviceArea + '</option>');
            })
        },
        error: function (xhr, status, statusText) {
            console.log('An error occurred');
        }
    });
}

function getTechnologiesSA() {
    $('#cbxTechnology')
        .find('option')
        .remove();
    $("#cbxTechnology").select2("val", "");
    $('#cbxTechnology').append('<option value="0">Select one</option>');
    $.isf.ajax({
        url: service_java_URL + "activityMaster/getTechnologyDetails",
        async: false,
        success: function (data) {
            var html = "";
            $.each(data, function (i, d) {
                $('#cbxTechnology').append('<option value="' + d.technologyID + '">' + d.technology + '</option>');
            })
        },
        error: function (xhr, status, statusText) {
            console.log('An error occurred');
        }
    });
}

function ValidateAddSubActivity() {
    var OK = true;
    $('#tbAddSAActivity-Required').text("");
    if ($("#tbAddSAActivity").val() == null || $("#tbAddSAActivity").val() == "") {
        $('#tbAddSAActivity-Required').text("Activity is required");
        OK = false;
    }
    $('#tbAddSASubActivity-Required').text("");
    if ($("#tbAddSASubActivity").val() == null || $("#tbAddSASubActivity").val() == "") {
        $('#tbAddSASubActivity-Required').text("SubActivity is required");
        OK = false;
    }
    $('#tbAddSAAvgEstdEffort-Required').text("");
    if ($("#tbAddSAAvgEstdEffort").val() == null || $("#tbAddSAAvgEstdEffort").val() == "") {
        $('#tbAddSAAvgEstdEffort-Required').text("Estimate work effort is required");
        OK = false;
    }
    else {
        var letterNumber = /^[0-9]+$/;
        if (!$("#tbAddSAAvgEstdEffort").val().match(letterNumber)) {
            $('#tbAddSAAvgEstdEffort-Required').text("Estimate work is a number");
            OK = false;
        }
    }
    if (OK) {
        addSubActivity();

    }
}

function refreshSubActivityTable() {

    $('#modalAddAct').modal('hide');
    $('#modalEditAct').modal('hide');
    $('#modalDeleteAct').modal('hide');
    $('.modal-backdrop').remove();
    searchSubActivity(true);
}

function ClearData() {
    $('#tbAddNewDomain').val("");

    $('#tbAddNewSubDomain').val("");
    $('#tbServiceArea').val("");
    $('#tbSubServiceArea').val("");

    $('#tbNewTechnology').val("");

    //subactivity
    $('#tbAddSAActivity').val("");
    $('#tbAddSASubActivity').val("");
    $('#tbAddSAAvgEstdEffort').val("");
    $('#tbAddNewDomain-Required').text("");
    $('#tbAddNewSubDomain-Required').text("");
    $('#tbServiceArea-Required').text("");
    $('#tbSubServiceArea-Required').text("");
    $('#tbNewTechnology-Required').text("");
    $('#tbAddSAActivity-Required').text("");
    $('#tbAddSASubActivity-Required').text("");
    $('#tbAddSAAvgEstdEffort-Required').text("");
}

function ClearDataEdit() {

    $('#tbEditDomain').val("");
    $('#tbEditSubdomain').val("");

    $('#tbEditServiceArea').val("");
    $('#tbEditSubServiceArea').val("");

    $('#tbEditTechnology').val("");

    //subactivity
    $('#tbEditActivitySA').val("");
    $('#tbEditSubActivitySA').val("");
    $('#tbEditAvgSA').val("");
    $('#tbEditDomain-Required').text("");
    $('#tbEditServiceArea-Required').text("");
    $('#tbEditSubServiceArea-Required').text("");
    $('#tbEditSubdomain-Required').text("");
    $('#tbEditTechnology-Required').text("");
    $('#tbEditActivitySA-Required').text("");
    $('#tbEditSubActivitySA-Required').text("");
    $('#tbEditAvgSA-Required').text("");

}

function addSubActivity() {

    $("#tbodySubActivities tr").remove();
    var serviceAreaID = $("#cbxSA").val();
    var technologyID = $("#cbxTechnology").val();
    var domainID = $("#cbxDomain").val();
    var subActivityName = $("#tbAddSASubActivity").val();
    var activity = $("#tbAddSAActivity").val();
    var avgEstdEffort = $("#tbAddSAAvgEstdEffort").val();
    
    var service_data = "{\"serviceAreaID\":\"" + serviceAreaID + "\",\"technologyID\":\"" + technologyID + "\",\"domainID\":\"" + domainID + "\",\"subActivityName\":\"" + subActivityName + "\",\"activity\":\"" + activity + "\",\"avgEstdEffort\":\"" + avgEstdEffort + "\",\"createdBy\":\"" + signumGlobal + "\"}";

    $.isf.ajax({
        url: service_java_URL + "activityMaster/saveActivityAndSubActivityDetails",
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
        alert("SubActivity Saved");
        refreshSubActivityTable();
    }
    function AjaxFailed(result) {
        alert("Activity/SubActivity already exist.");
    }

}

function ValidateSearchSubActivity() {
    var OK = true;
    $('#cbxDomain-Required').text("");

    if ($("#cbxDomain option:selected").val() == "0") {
        $('#cbxDomain-Required').text("Domain is required");
        OK = false;
    }
    $('#cbxSA-Required').text("");
    if ($("#cbxSA option:selected").val() == "0") {
        $('#cbxSA-Required').text("Service is required");
        OK = false;
    }
    $('#cbxTechnology-Required').text("");
    if ($("#cbxTechnology option:selected").val() == "0") {
        $('#cbxTechnology-Required').text("Technology is required");
        OK = false;
    }
    if (OK)
        searchSubActivity(false);


}

function ValidateComboxAddSubActivity() {
    var OK = true;
    $('#cbxDomain-Required').text("");

    if ($("#cbxDomain option:selected").val() == "0") {
        $('#cbxDomain-Required').text("Domain is required");
        OK = false;
    }
    $('#cbxSA-Required').text("");
    if ($("#cbxSA option:selected").val() == "0") {
        $('#cbxSA-Required').text("Service is required");
        OK = false;
    }
    $('#cbxTechnology-Required').text("");
    if ($("#cbxTechnology option:selected").val() == "0") {
        $('#cbxTechnology-Required').text("Technology is required");
        OK = false;
    }
    if (OK) {
        ClearData();
        $('#modalAddAct').modal('show');
    }
    
}

function searchSubActivity(Delete) {
 
    var table = $('#tbsubactivities').DataTable({
        responsive: true,
        retrieve: true,
        lengthMenu: [[50], [50]],
        "language": {
            "info": ""
        },
        searching: true,
        "pageLength": 10,
        colReorder: true,
        dom: 'Bfrtip',
        buttons: [
                'colvis', 'excelHtml5'
        ],
    });
    table.clear();

    var domainID = $("#cbxDomain").val();
    var serviceAreaID = $("#cbxSA").val();
    var technologyID = $("#cbxTechnology").val();

    var domainNsubDomainName = $("#cbxDomain option:selected").text();
    var serviceAreaNsubServiceAreaName = $("#cbxSA option:selected").text();
    var technologyName = $("#cbxTechnology option:selected").text();

    $.isf.ajax({
        url: service_java_URL + "activityMaster/getActivityAndSubActivityDetails/" + domainID + "/" + serviceAreaID + "/" + technologyID,
        async: false,
        success: function (data) {
           // var html = "";
            $.each(data, function (i, d) {

                table.row.add([domainNsubDomainName, serviceAreaNsubServiceAreaName, technologyName,d.subActivityName, d.executionType, d.avgEstdEffort, '<i style="cursor: pointer;" data-toggle="modal" data-target="#modalAddTask" class="fa fa-plus" onclick="showAddTask(' + d.subActivityID + ')"></i>&thinsp;&thinsp;&thinsp;' +
                    '<i style="cursor: pointer;" data-toggle="modal" data-target="#modalEditAct" class="fa fa-edit" onclick="showEditSubActivity(' + d.subActivityID + ',\'' + d.subActivityName + '\',\'' + d.avgEstdEffort + '\')"></i>&thinsp;&thinsp;&thinsp;' +
                    '<i style="cursor: pointer;" data-toggle="modal" data-target="#modalDeleteAct" class="fa fa-trash-o" onclick="showDeleteSubActivity(' + d.subActivityID + ')"></i>']);

            })
          
            table.draw();
        },
        error: function (xhr, status, statusText) {
            if (!Delete)
                alert("There are not Activity/Sub-Activity for this domain, services area and technology");
            console.log('An error occurred');
        }
    });
}

function showEditSubActivity(ID, activity, avgEstd) {

    $('#tbEditActivitySA-Required').text("");
    $('#tbEditSubActivitySA-Required').text("");
    $('#tbEditAvgSA-Required').text("");

    var arr = activity.split('/');
    $("#lblEditSA").html(ID);
    $("#tbEditDomainSA").val($("#cbxDomain option:selected").text());
    $("#tbEditServAreaSA").val($("#cbxSA option:selected").text());
    $("#tbEditTechSA").val($("#cbxTechnology option:selected").text());
    $("#tbEditActivitySA").val(arr[0]);
    $("#tbEditSubActivitySA").val(arr[1]);
    $("#tbEditAvgSA").val(avgEstd);
}

function ValidateEditSubActivity() {
    var OK = true;
    $('#tbEditActivitySA-Required').text("");
    if ($("#tbEditActivitySA").val() == null || $("#tbEditActivitySA").val() == "") {
        $('#tbEditActivitySA-Required').text("Activity is required");
        OK = false;
    }
    $('#tbEditSubActivitySA-Required').text("");
    if ($("#tbEditSubActivitySA").val() == null || $("#tbEditSubActivitySA").val() == "") {
        $('#tbEditSubActivitySA-Required').text("SubActivity is required");
        OK = false;
    }
    $('#tbEditAvgSA-Required').text("");
    if ($("#tbEditAvgSA").val() == null || $("#tbEditAvgSA").val() == "") {
        $('#tbEditAvgSA-Required').text("Estimate work effort is required");

        OK = false;
    }
    else {
        var letterNumber = /^[0-9]+$/;
        if (!$("#tbEditAvgSA").val().match(letterNumber)) {
            $('#tbEditAvgSA-Required').text("Estimate work is a number");
            OK = false;
        }
    }
    if (OK) {
        saveEditSubActivity();

    }
}

function saveEditSubActivity() {
    $("#tbodySubActivities tr").remove();

    var serviceAreaID = $("#cbxSA").val();
    var technologyID = $("#cbxTechnology").val();
    var domainID = $("#cbxDomain").val();
    var subActivityName = $("#tbEditSubActivitySA").val();
    var activity = $("#tbEditActivitySA").val();
    var avgEstdEffort = $("#tbEditAvgSA").val();
    var subActivityID = $("#lblEditSA").text();

    var service_data = "{\"serviceAreaID\":\"" + serviceAreaID + "\",\"technologyID\":\"" + technologyID + "\",\"domainID\":\"" + domainID + "\",\"subActivityName\":\"" + subActivityName + "\",\"activity\":\"" + activity + "\",\"avgEstdEffort\":\"" + avgEstdEffort + "\",\"subActivityID\":\"" + subActivityID + "\",\"lastModifiedBy\":\"" + signumGlobal + "\"}";

    $.isf.ajax({
        url: service_java_URL + "activityMaster/updateActivityAndSubActivityDetails",
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
        alert("Sub Activity Updated");
        refreshSubActivityTable();
    }
    function AjaxFailed(result) {
        alert("Activity/SubActity already exist.");
     }
}

function showDeleteSubActivity(ID) {
    $("#lblDeleteSubActivity").html(ID);
}

function deleteSubActivity() {
    var subActivityID = $("#lblDeleteSubActivity").text();
    var signum = signumGlobal;
    $.isf.ajax({
        url: service_java_URL + "activityMaster/deleteActivityAndSubActivity/" + subActivityID + "/" + signum,
        success: function (data) {
            alert("Record Deleted");
            refreshSubActivityTable();
        },
        error: function (xhr, status, statusText) {
            alert("Fail in delete activity " + xhr.responseText);
            console.log('An error occurred');
        }
    });
}

function showAddTask(subActivityID) {
    cleanAddTask(subActivityID);
    $('#select_Tools option').remove();
    $('#select_Tools').append('<option value="0">Select one</option>');
    $.isf.ajax({
        url: service_java_URL + "toolInventory/getToolInventoryDetails",
        success: function (data) {
            $.each(data, function (i, d) {
                $('#select_Tools').append('<option value="' + d.toolID + '">' + d.tool + '</option>');
            })
        },
        error: function (xhr, status, statusText) {
            alert("Fail " + xhr.responseText);
            console.log('An error occurred');
        }
    });
    getTaskList(subActivityID);
}

function cleanAddTask(subActivityID) {
    $('#tbTask-Required').text("");
    $('#tbAvgEstd-Required').text("");
    $('#cbxExecutionType-Required').text("");
    $('#select_Tools-Required').text("");
    $('#tbTask').val("");
    $('#tbDescription').val("");
    $('#cbxExecutionType').val("0");
    $('#select_ToolsEdit').val("");
    $('#tbAvgEstd').val("");
    $("#lblAddTask").html(subActivityID);
    $("#select_Tools").val("");
}

function ValidateAddTask() {

    var OK = true;
    
    $('#tbTask-Required').text("");
    if ($("#tbTask").val() == null || $("#tbTask").val() == "") {
        $('#tbTask-Required').text("Task is required");
        
        OK = false;
    }

    
    $('#tbAvgEstd-Required').text("");
    if ($("#tbAvgEstd").val() == null || $("#tbAvgEstd").val() == "") {
        $('#tbAvgEstd-Required').text("Estimate work effort is required");
        
        OK = false;
    }
    else {
        var letterNumber = /^[0-9]+$/;
        if (!$("#tbAvgEstd").val().match(letterNumber)) {
            $('#tbAvgEstd-Required').text("Estimate work is a number");
            OK = false;
        }
    }
    
    $('#cbxExecutionType-Required').text("");
    if ($("#cbxExecutionType option:selected").val() == "0") {
        $('#cbxExecutionType-Required').text("Execution is required");
        
        OK = false;
    }
    $('#select_Tools-Required').text("");
    if ($("#select_Tools option:selected").val() == "0") {
        $('#select_Tools-Required').text("Tool is required");
        
        OK = false;
    }
    if (OK)
        addTask();
}

function addTask() {
    var subActivity = $("#lblAddTask").text();
    var task = $("#tbTask").val();
    var executionType = $("#cbxExecutionType option:selected").val();
    var avgEstdEffort = $("#tbAvgEstd").val();
    var description = $("#tbDescription").val();
    var toolID = $("#select_Tools option:selected").val();
    var toolName = $("#select_Tools option:selected").text();
    var createdBy = signumGlobal;
    var service_data = "{\"subActivityID\":\"" + subActivity + "\",\"task\":\"" + task + "\",\"executionType\":\"" + executionType + "\",\"avgEstdEffort\":\"" + avgEstdEffort + "\",\"createdBy\":\"" + createdBy + "\",\"description\":\"" + description + "\",\"tools\":[{\"toolID\":\"" + toolID + "\",\"toolName\":\"" + toolName + "\"}]}";

    $.isf.ajax({
        url: service_java_URL + "activityMaster/saveTaskDetails",
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
        alert("Task Saved");
        cleanAddTask(subActivity);
        getTaskList($("#lblAddTask").text());
    }
    function AjaxFailed(result) {
        alert("Task already exist.");
    }
}

function getTaskList(subActivityID) {
    $("#tbodyTasks tr").remove();

    var subActivityID = $("#lblAddTask").text();
    var signum = signumGlobal;

    $.isf.ajax({
        url: service_java_URL + "activityMaster/getTaskDetails/" + subActivityID + "/" + signum,

        success: function (data) {
            var html = "";

            $.each(data, function (i, d) {
                var tool = "";
                var toolID = 0
                if (d.tools.length != 0) {
                    tool = d.tools[0].tool;
                    toolID = d.tools[0].toolID;
                }
                html = html + "<tr>";
                html = html + "<td>" + d.task + "</td>";
                html = html + "<td>" + d.description + "</td>";
                html = html + "<td>" + d.executionType + "</td>";
                html = html + "<td>" + tool + "</td>";
                html = html + "<td>" + d.avgEstdEffort + "</td>";
                html = html + '<td><i style="cursor: pointer;" data-toggle="modal" data-target="#modalEditTask" class="fa fa-edit" onclick="showEditTask(' + d.taskID + ', \'' + d.task + '\', \'' + d.description + '\', \'' + d.executionType + '\', \'' + toolID + '\', \'' + d.avgEstdEffort + '\', \'' + d.subActivityID + '\')"></i>&thinsp;&thinsp;&thinsp;' +
                              '<i style="cursor: pointer;" data-toggle="modal" data-target="#modalDeleteTask" class="fa fa-trash-o" onclick="showDeleteTask(' + d.taskID + ')"></i>';
                html = html + "</tr>";
            })
            $("#tbodyTasks").append(html);
        },
        error: function (xhr, status, statusText) {
            console.log('An error occurred');
        }
    });
}

function showEditTask(taskID, taskname, description, execType, toolID, avgEstdEffort, subActID) {

    $('#select_ToolsEdit option').remove();

    $('#select_ToolsEdit').append('<option value="0">Select one</option>');

    $.isf.ajax({
        url: service_java_URL + "toolInventory/getToolInventoryDetails",
        async: false,
        success: function (data) {
            $.each(data, function (i, d) {
                $('#select_ToolsEdit').append('<option value="' + d.toolID + '">' + d.tool + '</option>');
            })
        },
        error: function (xhr, status, statusText) {
            alert("Fail " + xhr.responseText);
            alert("status " + xhr.status);
            console.log('An error occurred');
        }
    });

    $('#tbTask_edit-Required').text("");
    $('#tbAvgEstd_edit-Required').text("");
    $('#cbxExecutionType_edit-Required').text("");
    $('#select_ToolsEdit-Required').text("");

    $("#lblEditTask").html(taskID);
    $("#lblSubActID").html(subActID);
    $("#tbTask_edit").val(taskname);
    $("#tbDescription_edit").val(description);
    $("#cbxExecutionType_edit").val(execType);
    $("#select_ToolsEdit").val(toolID);

    $("#tbAvgEstd_edit").val(avgEstdEffort);

}

function showDeleteTask(taskID) {
    $("#lblDeleteTask").html(taskID);
}

function ValidateEditTask(subActID) {
    var OK = true;
    $('#tbTask_edit-Required').text("");
    if ($("#tbTask_edit").val() == null || $("#tbTask_edit").val() == "") {
        $('#tbTask_edit-Required').text("Task is required");
        OK = false;
    }
    var valor = $("#tbAvgEstd_edit").val();
    $('#tbAvgEstd_edit-Required').text("");
    if ($("#tbAvgEstd_edit").val() == null || $("#tbAvgEstd_edit").val() == "") {
        $('#tbAvgEstd_edit-Required').text("Estimate work effort is required");
        OK = false;
    }
    else {
        var letterNumber = /^[0-9]+$/;
        if (!$("#tbAvgEstd_edit").val().match(letterNumber)) {
            $('#tbAvgEstd_edit-Required').text("Estimate work is a number");
            OK = false;
        }
    }
    $('#cbxExecutionType_edit-Required').text("");
    if ($("#cbxExecutionType_edit option:selected").val() == "0") {
        $('#cbxExecutionType_edit-Required').text("Execution is required");
        OK = false;
    }
    $('#select_ToolsEdit-Required').text("");
    if ($("#select_ToolsEdit option:selected").val() == "0") {
        $('#select_ToolsEdit-Required').text("Tool is required");
        OK = false;
    }
    if (OK)
        saveEditTask(subActID);
}

function saveEditTask(subActivityID) {
    $("#tbodyTasks tr").remove();

    var task = $("#tbTask_edit").val();
    var taskID = $("#lblEditTask").text();
    var executionType = $("#cbxExecutionType_edit").val();
    var lastModifiedBy = signumGlobal;
    var description = $("#tbDescription_edit").val();
    var toolID = $("#select_ToolsEdit option:selected").val();
    var toolName = $("#select_ToolsEdit option:selected").text();
    var avgEstdEffort = $("#tbAvgEstd_edit").val();

    var service_data = "{\"subActivityID\":\"" + subActivityID + "\",\"task\":\"" + task + "\",\"taskID\":\"" + taskID + "\",\"executionType\":\"" + executionType + "\",\"avgEstdEffort\":\"" + avgEstdEffort + "\",\"lastModifiedBy\":\"" + lastModifiedBy + "\",\"description\":\"" + description + "\",\"tools\":[{\"toolID\":\"" + toolID + "\",\"toolName\":\"" + toolName + "\"}]}";

    $.isf.ajax({
        url: service_java_URL + "activityMaster/updateTaskDetails/",
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
        alert("Task Updated");
        refreshTaskTable();
    }
    function AjaxFailed(result) {
        alert("Failed to update task");
    }
}

function refreshTaskTable() {
    $('#modalEditTask').modal('hide');
    $('#modalDeleteTask').modal('hide');
    $('.modal-backdrop').remove();
    getTaskList($("#lblAddTask").text());
}

function deleteTask() {
    taskID = $("#lblDeleteTask").text();
    var signum = signumGlobal;

    $.isf.ajax({
        url: service_java_URL + "activityMaster/deleteTaskDetails/" + taskID + "/" + signum,

        success: function (data) {
            alert("Task Deleted");
            refreshTaskTable();
        },
        error: function (xhr, status, statusText) {
            alert("Fail in delete task");
            console.log('An error occurred');
        }
    });
}


function getAllProjectDetails() {
    $("#combo_project").html('');
    pwIsf.addLayer({ text: "Please wait ..." });
    $("#combo_project").append("<option value='' selected >Select Project</option>");
    $.isf.ajax({
        url: service_java_URL + "flowchartController/searchWFAvailabilityforScope/all/all/all/all/all/all/all/all",
        success: function (data)
        {
            allProjectDetails = data;
            //console.log(data);
            pwIsf.removeLayer();
            var projects = [];
            
            $.each(data, function (key, value) {
                if (projects.indexOf(value.projectID) == -1) {
                    $("#combo_project").append("<option value=" + value.projectID + ">" + value.projectID + "_" + value.projectName + "</option>");
                    projects.push(value.projectID);
                }
            });
        },
        complete: function () {
            $("#combo_project").select2();
        }
    });
}

function projectChange() {
    var selectedproj = $('#combo_project option:selected').val();
    $("#combo_subact").html('');
    $("#combo_subact").append("<option value='' selected >Select SubActivity</option>");
    tblProjectDetails = [];
    for (var k in allProjectDetails) {
        if (allProjectDetails[k].projectID == selectedproj) {
            // console.log(k)Domain SubDomain Technology SubActivity
            $("#combo_subact").append("<option value=" + allProjectDetails[k].subActivityID + ">" + allProjectDetails[k].domain + "/" + allProjectDetails[k].subDomain + "/" + allProjectDetails[k].technology + "/" + "/" + allProjectDetails[k].activity + "/" + "/" + allProjectDetails[k].subActivity + "/" + allProjectDetails[k].subActivity + "</option>");
            tblProjectDetails.push(allProjectDetails[k])
        }
    }
}

function subactChange() {
    $("#combo_subact").select2();
    var selectedproj = $('#combo_project option:selected').val();
    var selectedSubact = $('#combo_subact option:selected').val();
    if (selectedproj != '' && selectedSubact == '') {
        tblProjectDetails = [];
        for (var k in allProjectDetails) {
            if (allProjectDetails[k].projectID == selectedproj) {
                // console.log(k)Domain SubDomain Technology SubActivity
                
                tblProjectDetails.push(allProjectDetails[k])
            }
        }
    }
   
    if (selectedSubact!='') {
        tblProjectDetails = [];
        for (var i = 0; i < allProjectDetails.length; i++) {
            if ((allProjectDetails[i].projectID == selectedproj) && allProjectDetails[i].subActivityID == selectedSubact)
                tblProjectDetails.push(allProjectDetails[i])
        }
    }
}



function assessdOrExperincedOnViewFlMaster(iframeSrc, iframeId) {

    var checkbox = document.querySelector('input[id="togBtnAssessedOrExperiencedOnViewFlMaster"]');
    let allGraphJson = app.graph.toJSON();
    if (checkbox.checked) {
        let cells = allGraphJson.cells;
        cells.forEach(function (el) {
            if (el.type == 'ericsson.Manual' && el.viewMode == 'Assessed') {
                el.attrs.header.stroke = el.attrs.header.fill = "#9D9D9D";
                el.attrs.footer.stroke = el.attrs.footer.fill = "#9D9D9D";
                el.attrs.body.stroke = "#9D9D9D";
            }
        });
        app.graph.fromJSON({ cells: cells });

    } else {
        allGraphJson.cells.forEach(function (el) {
            if (el.type == 'ericsson.Manual') {
                el.attrs.header.stroke = el.attrs.header.fill = "#31d0c6";
                el.attrs.footer.stroke = el.attrs.footer.fill = "#31d0c6";
                el.attrs.body.stroke = "#31d0c6";
            }
        });
        app.graph.fromJSON(allGraphJson);
    }


}

function viewFlowChartWorkOrder(projID, subId, version, experiencedMode, wfid) {
    let proficiencyModeArr = [];
    let noFlowchartFoundJson = '{ "cells": [{ "size": { "width": 200, "height": 90 }, "angle": 0, "z": 1, "position": { "x": 225, "y": 230 }, "type": "basic.Rect", "attrs": { "rect": { "rx": 2, "ry": 2, "width": 50, "stroke-dasharray": "0", "stroke-width": 2, "fill": "#dcd7d7", "stroke": "#31d0c6", "height": 30 }, "text": { "font-weight": "Bold", "font-size": 11, "font-family": "Roboto Condensed", "text": "No FlowChart Available", "stroke-width": 0, "fill": "#222138" }, ".": { "data-tooltip-position-selector": ".joint-stencil", "data-tooltip-position": "left" } } }] }';
    $.isf.ajax({
        url: service_java_URL + "flowchartController/viewFlowChartForSubActivity/" + projID + "/" + subId + "/0/" + version + "/" + experiencedMode + "/" + wfid,
        success: function (data) {

            if (data.length) {
                for (let k in data) {
                    if ('Success' in data[k]) {
                        if (data[k].Success.Mode.indexOf(',') > -1) {
                            proficiencyModeArr = data[k].Success.Mode.split(',');
                        } else {
                            proficiencyModeArr.push(data[k].Success.Mode);
                        }
                        // clear array
                        globFlChartJsonArrMaster.splice(0, globFlChartJsonArrMaster.length);
                        globFlChartJsonArrMaster.push(JSON.parse(data[k].Success.FlowChartJSON));

                            $('#WFName').text(data[k].Success.WorkFlowName);
                            $('#projectId').text(projID+", ");
                        
                        //app.graph.fromJSON(JSON.parse(data[k].Success.FlowChartJSON));

                    } else {
                        app.graph.fromJSON(JSON.parse(data[k].Failed));
                    }
                }
            } else {
                app.graph.fromJSON(JSON.parse(noFlowchartFoundJson));
            }

            //console.log(flChartJsonArr);

            let experiencedSwitchHtml = `<label class="switchSource" ><input type="checkbox"  class="toggleActive" onclick="assessdOrExperincedOnViewFlMaster()" id="togBtnAssessedOrExperiencedOnViewFlMaster" ><div class="sliderSource round " style="width:94px;"><span class="onSource">Experienced </span><span class="offSource">Assessed</span></div></label>`;

            if (proficiencyModeArr.length==2) {
                $('#assessedOrExpriencedDiv').show().html(experiencedSwitchHtml);
            }
            if (globFlChartJsonArrMaster.length>0) {
                app.graph.fromJSON(globFlChartJsonArrMaster[0]);
            }


        },
        error: function (xhr, status, statusText) {
            var err = JSON.parse(xhr.responseText);
            // console.log(err.errorMessage);

        }
    });
}


$(document).on("click", ".createWF", function () {
    var version_id = $(this).data('versionno')
    var version_name = $(this).data('workflowname')

    if (version_id != -1) {
        $('#versionNumber').val(version_id);
        $('#versionName').val(version_name);
        var subActID = $(this).data('subactid');
        var projID = $(this).data('projid');
        var wfid = $(this).data('wfid');
        var experiencedMode = ($(this).data('experiencedmode').toLowerCase() == 'yes') ? '1' : '0';
        flowChartViewOpenInNewWindow(UiRootDir + '/ActivityMaster/FlowChartViewMaster\?subId=' + subActID + '&projID=' + projID + '&version=' + version_id + '&experiencedMode=' + experiencedMode + '&wfid=' + wfid);
    }
    else {
        alert("Please select Version.");
    }

});

function populateTable() {
    if ($.fn.dataTable.isDataTable('#wfSearchTable')) {
        oTable.destroy();
        $('#wfSearchTable').empty();

    }
    //if (($('#combo_subact option:selected').val() == '') || ($('#combo_project option:selected').val() == '')) {
      if (($('#combo_project option:selected').val() == '')) {
        pwIsf.alert({ msg: "Please select Project and Subactivity both/", type: "warning" });
    }
    else {
        pwIsf.addLayer({ text: "Please wait ..." });
        $.each(tblProjectDetails, function (i, d) {

            // d.act = '<a class="icon-add">' + getIcon('add') + '</a>';
            if (d.wfAvailability == "Yes") {
                d.act = '<a href="#" class="createWF icon-view" title="View work flow" data-wfid="' + d.wfid + '"data-experiencedMode="' + d.experiencedMode + '" data-subActId="' + d.subActivityID + '" data-projID="' + d.projectID + '" data-versionNo="' + d.lstVersionNumber + '" data-workFlowName="' + d.lstWFName + '">' + getIcon('view') + '</a>'
            }
            else {
                d.act = '';
            }
            d.action = '<div style="display:flex;">' + d.act + '</div>';
        });
        $("#wfSearchTable").append($('<tfoot><tr><th></th><th></th><th>Project Name </th><th>Domain</th><th>Sub Domain</th><th>Service Area</th><th>Technology</th><th>Activity</th><th>Sub Activity</th><th>Work Flow Availability</th></tr></tfoot>'));

        oTable = $('#wfSearchTable').DataTable({
            searching: true,
            responsive: true,
            "pageLength": 10,
            "data": tblProjectDetails,
            colReorder: true,
            order: [1],
            dom: 'Bfrtip',
            buttons: [
             'colvis', 'excel'
            ],
            "destroy": true,
            "columns": [
                { "title": "Action", "targets": 'no-sort', "orderable": false, "searchable": false, "data": "action" },
                { "title": "Project Name", "data": "projectName" },
                { "title": "Sub Service Area", "data": "serviceArea" },
                { "title": "Domain", "data": "domain" },
                { "title": "Sub Domain", "data": "subDomain" },
                { "title": "Technology", "data": "technology" }, { "title": "Activity", "data": "activity" },
                { "title": "Sub Activity", "data": "subActivity" }, { "title": "Work Flow Version", "data": "lstWFName" },
                { "title": "Work Flow Availability", "data": "wfAvailability" }

            ],
            initComplete: function () {

                $('#wfSearchTable tfoot th').each(function (i) {
                    var title = $('#wfSearchTable thead th').eq($(this).index()).text();
                    if ((title != "Action"))
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

        $('#wfSearchTable tfoot').insertAfter($('#wfSearchTable thead'));
        //console.log(data);


        pwIsf.removeLayer();
    }
}

function flowChartViewOpenInNewWindow(url) {
    var width = window.innerWidth * 0.85;
    // define the height in
    var height = width * window.innerHeight / window.innerWidth;
    // Ratio the hight to the width as the user screen ratio
    window.open(url, 'newwindow', 'width=' + width + ', height=' + height + ', top=' + ((window.innerHeight - height) / 2) + ', left=' + ((window.innerWidth - width) / 2));

}
