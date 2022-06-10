var projectID = 0;
var signum = 'ebhjpra';

$(document).ready(function () {
    $("#revoke").hide();
    getAllSignums();
    getAccessProfiles();
    getLineManagerList();
    getCapability()
    //list of signums in signum dropdown changes on change of LM
    $(C_Line_Manager_List_Id).change(function (e) {
        $(C_Signum_List).empty(); //to clear previously appended values
        var selectedLineManager = document.getElementById(C_Line_Manager_List).value;
        if (selectedLineManager !== '') {
            getUpdatedSignumList(selectedLineManager);
        }
        else {
            getAllSignums();
        }
    });

});

/*--- Get All Signums in Select To   ---*/
function getAllSignums() {
    $(C_Signum_List).select2({
        dropdownParent: $('#signumListDiv'),
        ajax: {
            url: `${service_java_URL}activityMaster/getEmployeesByFilter`,
            dataType: 'json',
            delay: 250,
            headers: commonHeadersforAllAjaxCall,
            type: 'POST',
            data: function (params) {
                return {
                    term: params.term, // search term
                    page: params.page
                };
            },
            processResults: function (data, params) {
                params.page = params.page || 1;
                var select2data = $.map(data, function (obj) {
                    obj.id = obj.signum;
                    obj.text = `${obj.signum}/${obj.employeeName}`
                    return obj;
                });

                return {
                    results: select2data,
                    pagination: {
                        more: (params.page * 30) < select2data.total_count
                    }
                };
            },
            cache: true
        },
        placeholder: 'Search Signum',
        escapeMarkup: function (markup) { return markup; }, // let our custom formatter work
        minimumInputLength: 3,
        templateResult: formatRepo,
        templateSelection: formatRepoSelection,
        closeOnSelect: true  //close select2 after selection
    });
}



function formatRepo(data) {
    if (data.loading) {
        return data.text;
    }
    if (data.signum === '') { // adjust for custom placeholder values
        return 'Custom styled placeholder text';
    }
    return `${data.signum}/${data.employeeName}`;
}

function formatRepoSelection(data) {
    return data.signum;
}

/*--- Clear Signum before searching for new one ---*/

function clearSearchFields() {
    $(C_Signum_List).val('').trigger('change');
}


function getAccessProfiles() {
    $(C_Profile_Name)
        .find('option')
        .remove();
    $(C_Profile_Name).select("val", "");
    $(C_Profile_Name).append('<option value="0">Select one</option>');
    $.isf.ajax({
        url: `${service_java_URL}accessManagement/getAccessProfiles`,
        success: function (data) {
            $.each(data, function (i, d) {
                $(C_Profile_Name).append(`<option value="${d.accessProfileID}"> ${d.accessProfileName}</option>`);
            })
        },
        error: function (xhr, status, statusText) {
            console.log(`An error occurred on getAccessProfiles ${xhr.responseText}`);
        }
    });
}

function getRoleList() {
    $(C_Role)
        .find('option')
        .remove();
    $(C_Role).select("val", "");
    $(C_Role).append('<option value="0">Select one</option>');
    $.isf.ajax({
        url: `${service_java_URL}accessManagement/getRoleList`,
        success: function (data) {
            $.each(data, function (i, d) {
                $(C_Role).append(`<option value="${d.accessRoleID}> ${d.role}</option>`);
            })
        },
        error: function (xhr, status, statusText) {
            console.log(`An error occurred on getRoleList ${xhr.responseText}`);
        }
    });
}

function getOrganizationList() {
    $(C_Organization)
        .find('option')
        .remove();
    $(C_Organization).select("val", "");
    $(C_Organization).append('<option value="0">Select one</option>');
    $.isf.ajax({
        url: `${service_java_URL}accessManagement/getOrganizationList`,
        success: function (data) {
            $.each(data, function (i, d) {
                $(C_Organization).append(`<option value="${d.organisationID}"> ${d.organisation}</option>`);
            })
        },
        error: function (xhr, status, statusText) {
            console.log('An error occurred on getRoleList' + xhr.responseText);
        }
    });
}

/*----Signum List based on Line Manager Selected---*/

function getUpdatedSignumList(selectedLineManager) {
    var managerSelected = selectedLineManager;
    $(C_Signum_List).select2({
        dropdownParent: $('#signumListDiv'),
        ajax: {
            url: `${ service_java_URL }activityMaster/getEmployeesOrManager?managerSignum=${managerSelected}`,
            dataType: 'json',
            delay: 250,
            headers: commonHeadersforAllAjaxCall,
            type: 'POST',
            data: function (params) {
                return {
                    term: params.term, // search term
                    page: params.page
                };
            },
            processResults: function (data, params) {
                params.page = params.page || 1;
                var select2data = $.map(data, function (obj) {
                    obj.id = obj.signum;
                    obj.text = `${obj.signum}/${obj.employeeName}`;
                    return obj;
                });
                return {
                    results: select2data,
                    pagination: {
                        more: (params.page * 30) < select2data.total_count
                    }
                };
            },
            cache: true
        },
        placeholder: 'Search Signum',
        escapeMarkup: function (markup) { return markup; }, // let our custom formatter work
        minimumInputLength: 3,
        templateResult: formatRepo,
        templateSelection: formatRepoSelection,
        closeOnSelect: true  //close select2 after selection
    });   
}

/*---Line Manager List--- */
function getLineManagerList() {
    clearSearchedLineManager();
    $("#lineManagerList").select2({
        dropdownParent: $('#lineManagerDiv'),
        ajax: {
            url: service_java_URL + "activityMaster/getLineManagersBySearch",
            dataType: 'json',
            delay: 250,
            headers: commonHeadersforAllAjaxCall,
            type: 'POST',
            data: function (params) {
                return {
                    term: params.term, // search term
                    page: params.page
                };
            },
            processResults: function (data, params) {
                params.page = params.page || 1;
                var select2data = $.map(data, function (obj) {
                    obj.id = obj.managerSignum;
                    obj.text = obj.managerSignum + '/' + obj.managerName
                    return obj;
                });

                return {
                    results: select2data,
                    pagination: {
                        more: (params.page * 30) < select2data.total_count
                    }
                };
            },
            cache: true
        },
        placeholder: 'Search Line Manager',
        escapeMarkup: function (markup) { return markup; }, // let our custom formatter work
        minimumInputLength: 3,
        templateResult: formatRepo,
        templateSelection: formatRepoSelection,
        closeOnSelect: true //close select2 after selection
    });

    function formatRepo(data) {
        if (data.loading) {
            return data.text;
        }
        if (data.managerSignum == '') { // adjust for custom placeholder values
            return 'Custom styled placeholder text';
        }
        var markup = data.managerSignum + '/' + data.managerName;

        return markup;
    }

    function formatRepoSelection(data) {
        return data.managerSignum;
    }
}

/*--- Clear Line Manager before searching for new one ---*/

function clearSearchedLineManager() {
    $('#lineManagerList').val('').trigger('change');
}

function getCapability() {
    $.isf.ajax({
        url: `${service_java_URL}accessManagement/getCapability`,
        success: function (data) {
            let html = "<table>";
            $.each(data, function (i, d) {
                html = `${html}<tr><td><input type='checkbox' value='${d.capabilityPageID}'></td><td>${d.capabilityPageName} </td></tr>`;
            })
            html = `${html}</table>`;
            $("#capability").append(html);
        },
        error: function (xhr, status, statusText) {
            console.log(`An error occurred on getCapability ${xhr.responseText}`);
        }
    });
}


function createNewAccessProfile() {
    $("input[type=checkbox]:checked").each(function () {
        //cada elemento seleccionado
        alert($(this).val());
    });
    const subActivityID = $("#txtSubActivity").val();
    const serviceData = "{\"projectID\":\"" + projectID + "\",\"subActivityID\":\"" + subActivityID + "\",\"flowChartJSON\":\"" + flowChartJSON + "\",\"sourceProjectID\":\"" + sourceProjectID + "\",\"createdBy\":\"" + createdBy + "\",\"createdDate\":\"" + createdDate + "\",\"active\":\"" + active + "\",\"versionNumber\":\"" + versionNumber + "\"}";
    alert(serviceData);
    $.isf.ajax({
        url: `${service_java_URL}accessManagement​/createNewAccessProfile​​​/`,
        context: this,
        crossdomain: true,
        processData: true,
        contentType: 'application/json',
        type: 'POST',
        data: serviceData,
        xhrFields: {
            withCredentials: false
        },
        success: AjaxSucceeded,
        error: AjaxFailed
    });
    function AjaxSucceeded(data, textStatus) {
        alert("Access Added!");
        window.location.href = 'CreateAccessProfile';
    }
    function AjaxFailed(result) {
        alert("Failed to create the Access");
    }
}
