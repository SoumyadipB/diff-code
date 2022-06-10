$(document).ready(function () {
    getRolesAR();  
    getSignum();
});

function getRolesAR() {
    $.isf.ajax({
        url: service_java_URL + "accessManagement/getRoleList",
        success: function (data) {
            var html = "";
            $('#cbxRolesAR').append('<option value="0">----Select Role----</option>');
            $.each(data, function (i, d) {
                $('#cbxRolesAR').append('<option value="' + d.accessRoleID + '">' + d.role + '</option>');
            });

            $('#cbxRolesAR').select2('val', '0');
        },
        error: function (xhr, status, statusText) {
           
            console.log('An error occurred');
        }
    });
}

function getAccessProfileByRole() {
    

    var e = document.getElementById("cbxRolesAR");
    var role = e.options[e.selectedIndex].value;

    

    $('#cbxAccessProfileAR').find('option').remove();
    if (role != 0) {
        $.isf.ajax({
            url: service_java_URL + "accessManagement/getAccessProfilesByRole/" + role,
            success: function (data) {
                $('#cbxAccessProfileAR').append('<option value="0">------Select Access Profiles------</option>');
                $.each(data, function (i, d) {
                    $('#cbxAccessProfileAR').append('<option value="' + d.accessProfileID + '">' + d.accessProfileName + '</option>');
                });

                $('#cbxAccessProfileAR').select2('val', '0');
            },
            error: function (xhr, status, statusText) {
                alert("Fail Get Access Profiles " + xhr.responseText);
                alert("status " + xhr.status);
                console.log('An error occurred');
            }
        });
    }
    else
        {
        
        $('#cbxAccessProfileAR').append('<option value="0">------Select Access Profiles------</option>');
        $('#cbxAccessProfileAR').select2('val', '0');
        }
    }

function showErrorMsg(inpEle, msg) {

    $('#' + inpEle).text(msg);
    $('#' + inpEle).css('display', '');
}

function hideErrorMsg(inpEle) {
    $('#' + inpEle).text('');
    $('#' + inpEle).css('display', 'none');
}

function getSignum() {
    $("#cbxSignumAR").autocomplete({

        source: function (request, response) {
            $.isf.ajax({

                url: service_java_URL + "activityMaster/getEmployeesByFilter",
                type: "POST",
               
                data: {
                    term: request.term
                },
                success: function (data) {
                    var result = [];
                    if (data.length == 0) {

                        showErrorMsg('Signum-Required', 'Please enter a valid Signum.');
                        $('#cbxSignumAR').val("");
                        $('#cbxSignumAR').focus();
                        response(result);

                    }
                    else {
                        hideErrorMsg('Signum-Required');

                        $("#cbxSignumAR").autocomplete().addClass("ui-autocomplete-loading");
                       
                        $.each(data, function (i, d) {
                            result.push({
                                "label": d.signum + "/" + d.employeeName,
                                "value": d.signum

                            });


                        });
                        response(result);
                        $("#cbxSignumAR").autocomplete().removeClass("ui-autocomplete-loading");
                    }

                }
            });
        },
       
        minLength: 3
       
    });
    $("#cbxSignumAR").autocomplete("widget").addClass("fixedHeight");


}

function btnAssignRole() {
    var roleID = $('#cbxRolesAR').val();
    var accessProfileID = $('#cbxAccessProfileAR').val();
    var signum = $('#cbxSignumAR').val();
    console.log('signum ' + $('#cbxSignumAR').val());
    var makeSignumMan = 1;
    if ($('#cbxSignumAR').val() == 0 || $('#cbxSignumAR').val() == null || $('#cbxSignumAR').val() == undefined) {
        makeSignumMan = 0;
        alert('Signum not filled');
    }
    if (roleID != 0 && accessProfileID != 0 && makeSignumMan ==1) {

        $.isf.ajax({
            url: service_java_URL + "accessManagement/assignAccessToUser/" + roleID + "/" + accessProfileID + "/" + signum,
            context: this,
            crossdomain: true,
            processData: true,
            contentType: 'application/json',
            type: 'POST',
            xhrFields: {
                withCredentials: false
            },
            success: AjaxSucceeded,
            error: AjaxFailed
        });
    }
    else
        alert("Please select all three fields--> role,AccessProfile and signum ");
    function AjaxSucceeded(data, textStatus) {
        pwIsf.alert({ msg: data, type: 'info' });
    }
    function AjaxFailed(result) {
        pwIsf.alert({ msg: 'Failed to assign role', type: 'error' });
    }
}