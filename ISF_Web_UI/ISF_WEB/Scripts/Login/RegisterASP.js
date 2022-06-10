$(document).ready(function () {
    if ((getSessionValuesByKey('AspStatus')=="NEW")) {
        $("#registerASPModal").modal('show');
        $('#firstNameAsp').val(getSessionValuesByKey('UserGivenName'));
        $('#lastNameAsp').val(getSessionValuesByKey('UserSurName'));
        $('#emailIdAsp').val(getSessionValuesByKey('UserEmaiId'));

        $('#firstNameAsp').css('pointer-events', 'none');
        $('#lastNameAsp').css('pointer-events', 'none');
        $('#emailIdAsp').css('pointer-events', 'none');
    }
    else if ((getSessionValuesByKey('AspStatus') == "REGISTERED")) {
        $("#approvalPendingASPModal").modal('show');
    }

    $("#cityAsp")[0].value = "";
    $('#signumASP').val(getSessionValuesByKey("Signum"));
    $('#signumASP').css('pointer-events', 'none');
    getCountries();
    getOrganisation();

    $("#registerASPModal").on('hidden.bs.modal', function () {
        $('#requiredAspFields').text("");
        $("#firstNameAsp")[0].value = "";
        $("#lastNameAsp")[0].value = "";
        $("#emailIdAsp")[0].value = "";
        $("#phoneAsp")[0].value = "";
        $("#vendorCodeAsp")[0].value = "";
        $("#vendorNameAsp")[0].value = "";
        $("#countryAspNew")[0].value = "";
        $("#cityAsp")[0].value = "";
       
    })

    $("#resetASPPasswordModal").on('hidden.bs.modal', function () {
        $('#requiredResetAspFields').text("");
        $('#emailAspReset')[0].value = "";
        $("#oldPswdReset")[0].value = "";
        $("#newPswdReset")[0].value = "";
        $("#confirmPswdReset")[0].value = ""
    })

    $("#forgotASPPasswordModal").on('hidden.bs.modal', function () {
        $('#requiredForgotAspFields').text("");
        $('#emailAspForgot')[0].value = "";
    })

    $("#vendorCodeAsp").focusout(function () {
        var vendorCode = $(this).val();
        $.isf.ajax({
            url: service_java_URL + "aspManagement/getVendorDetailsByID?vendorCode=" + vendorCode,
            success: function (data) {
                if (!data == "") {
                    $('#vendorNameAsp').val(data.vendorName);
                    $('#countryAspNew').val(data.country);
                    $('#vendorNameAsp').css('pointer-events', 'none');
                    $('#countryAspNew').css('pointer-events', 'none');
                }
                else {
                    pwIsf.alert({ msg: "Please enter a valid Vendor Code!" });
                    $('#vendorNameAsp').val("");
                    $('#countryAspNew').val("");
                    $('#vendorNameAsp').css('pointer-events', 'none');
                    $('#countryAspNew').css('pointer-events', 'none');
                }
                    
               
            },
            error: function (xhr, status, statusText) {
                $('#vendorNameAsp').css('pointer-events', 'none');
                $('#countryAspNew').css('pointer-events', 'none');
                console.log("Fail " + xhr.responseText);
            }
        });
    });
    
});

function phoneNumberValidation(inputtxt) {
    var phoneno = /^\s*(?:\+?(\d{1,3}))?[-. (]*(\d{3})[-. )]*(\d{3})[-. ]*(\d{4})(?: *x(\d+))?\s*$/;
    if (inputtxt.match(phoneno)) {
        return true;
    }
    else {
        pwIsf.alert({msg:"Please enter a valid Phone Number", type:"warning"});
        return false;
    }
}

function getCountries() {
    return $.isf.ajax({
        url: service_java_URL + "activityMaster/getCountries",
        success: function (data) {

            $.each(data.responseData, function (i, d) {
                $('#countryAsp').append('<option value="' + d.countryName + '">' + d.countryName + '</option>');
            })
        },
        error: function (xhr, status, statusText) {
            console.log("Fail " + xhr.responseText);
        }
    });

}

function getOrganisation() {
    return $.isf.ajax({
        url: service_java_URL + "accessManagement/getOrganizationList",
        success: function (data) {

            $.each(data, function (i, d) {
                $('#orgAsp').append('<option value="' + d.organisation + '">' + d.organisation + '</option>');
            })
        },
        error: function (xhr, status, statusText) {
            console.log("Fail " + xhr.responseText);
        }
    });

}

function getAspVendors() {
    return $.isf.ajax({
        url: service_java_URL + "/aspManagement/getAllAspVendors",
        success: function (data) {
            $.each(data, function (i, d) {
                $('#vendorAsp').append('<option value="' + d.vendorCode + '">' + d.vendorCode + '/' + d.organisation + '</option>');
            })
        },
        error: function (xhr, status, statusText) {
            console.log("Fail " + xhr.responseText);
        }
    });
}

function validateAspFields()
{
    $('#requiredAspFields').text("");
    var OK = true;
    if ($("#firstNameAsp").val() == "")
        OK = false;
    if ($("#lastNameAsp").val() == "")
        OK = false;
    if ($("#emailIdAsp").val() == "")
        OK = false;    
    if ($("#countryAsp").val() == "0")
        OK = false;
    if ($("#vendorCodeAsp").val() == "0")
        OK = false;
    if ($("#cityAsp").val() == "0")
        OK = false;
    if (!phoneNumberValidation($("#phoneAsp").val()))
        OK = false;
    if (OK)
        registerASPUser();
    else
        $("#requiredAspFields").append('<label id="lastNameAsp-Required" style="color:red; font-size:10px; padding-left:15px;">Please fill all mandatory fields</label>');
}

function registerASPUser() {
    pwIsf.addLayer({ text: "Please wait until we Register you on ISF.." });
    var fNameASP = $("#firstNameAsp").val();
    var lNameASP = $("#lastNameAsp").val();
    var emailASP = $("#emailIdAsp").val();    
    var vendorCodeASP = $("#vendorCodeAsp").val();
    var cityASP = $("#cityAsp").val();
    var contactNumberASP = $("#phoneAsp").val();
    var signumASP = getSessionValuesByKey('Signum');
    //vendorCode city
    var registerDetailsObj = new Object();
    registerDetailsObj.email = emailASP;
    registerDetailsObj.firstName = fNameASP;
    registerDetailsObj.lastName = lNameASP;   
    registerDetailsObj.vendorCode = vendorCodeASP;
    registerDetailsObj.city = cityASP;
    registerDetailsObj.contactNumber = contactNumberASP;
    registerDetailsObj.signum = signumASP;

    var JsonObj = JSON.stringify(registerDetailsObj);
    $.isf.ajax({
        url: service_java_URL + "accessManagement/aspIsfRegistration",
        context: this,
        crossdomain: true,
        processData: true,
        contentType: 'application/json',
        type: 'POST',
        data: JsonObj,
        xhrFields: {
            withCredentials: false
        },
        success: function (data) {
            pwIsf.removeLayer();
            if (data.isSuccess) {
                pwIsf.alert({ msg: data.msg, type: "success" });
                logoutUser();
                $("#registerASPModal").modal('hide');
            }
            else
            {
                pwIsf.alert({ msg: data.msg, type: "warning" });
            }            
        },
        error: function (xhr, status, statusText) {
            pwIsf.removeLayer();
            pwIsf.alert({ msg: "An Error Occurred", type: "error" });
        }
    });
}

function validateAspResetFields() {
    $('#requiredResetAspFields').text("");
    var OK = true;
    if ($("#emailAspReset").val() == "")
        OK = false;
    if ($("#oldPswdReset").val() == "")
        OK = false;
    if ($("#newPswdReset").val() == "")
        OK = false;
    if ($("#confirmPswdReset").val() == "")
        OK = false;
    if (OK)
        resetASPPassword();
    else
        $("#requiredResetAspFields").append('<label style="color:red; font-size:10px; padding-left:15px;">Please fill all mandatory fields</label>');
}

function logoutUser() {
    localStorage.clear();
    clearInterval(idleInterval);
    window.location.href = UiRootDir + '/Login/SignOut';
}

function resetASPPassword()
{
    var emailAspReset = $("#emailAspReset").val();
    var oldPswdReset = $("#oldPswdReset").val();
    var newPswdReset = $("#newPswdReset").val();
    var confirmPswdReset = $("#confirmPswdReset").val();
    if (newPswdReset == confirmPswdReset) {
        var resetDetailsObj = new Object();
        resetDetailsObj.email = emailAspReset;
        resetDetailsObj.oldPassword = oldPswdReset;
        resetDetailsObj.newPassword = newPswdReset;

        var JsonObj = JSON.stringify(resetDetailsObj);
        $.isf.ajax({
            url: service_java_URL + "accessManagement/aspIsfReset",
            context: this,
            crossdomain: true,
            processData: true,
            contentType: 'application/json',
            type: 'POST',
            data: JsonObj,
            xhrFields: {
                withCredentials: false
            },
            success: function (data) {
                if (data.isSuccess) {
                    pwIsf.alert({ msg: data.msg, type: "success" });
                    $("#resetASPPasswordModal").modal('hide');
                }
                else {
                    pwIsf.alert({ msg: data.msg, type: "warning" });
                }

            },
            error: function (xhr, status, statusText) {

            }
        });
    }
    else {
        pwIsf.alert({ msg: "Password Does Not Match" , type: "warning"});
    }    
}

function validateAspForgotPasswordFields() {
    $('#requiredForgotAspFields').text("");
    var OK = true;
    if ($("#emailAspForgot").val() == "")
        OK = false;
    if (OK)
        forgotASPPassword();
    else
        $("#requiredForgotAspFields").append('<label style="color:red; font-size:10px; padding-left:15px;">Please fill the mandatory field</label>');
}

function forgotASPPassword() {
    var emailAspReset = $("#emailAspForgot").val();
        var forgotPasswordObj = new Object();
        forgotPasswordObj.email = emailAspReset;

        var JsonObj = JSON.stringify(forgotPasswordObj);
        $.isf.ajax({
            url: service_java_URL + "accessManagement/aspIsfForgetPassword",
            context: this,
            crossdomain: true,
            processData: true,
            contentType: 'application/json',
            type: 'POST',
            data: JsonObj,
            xhrFields: {
                withCredentials: false
            },
            success: function (data) {
                if (data.isSuccess) {
                    pwIsf.alert({ msg: data.msg, type: "success" });
                    $("#forgotASPPasswordModal").modal('hide');
                    setTimeout(function () { location.reload(); }, 1000)
                }
                else {
                    pwIsf.alert({ msg: data.msg, type: "warning" });
                }

            },
            error: function (xhr, status, statusText) {

            }
        });
    }