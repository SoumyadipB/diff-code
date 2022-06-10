
function prevent() {
    localStorage.clear();
    if ((IsAspUser == "True") && (AspStatus != "APPROVED")) {
        window.location.href = UiRootDir + '/Login/asplogin';
    }
    else
    {
        $.isf.ajax({
            type: "POST",
            contentType: "application/json",
            async: false,
            crossdomain: true,
            // data: JSON.stringify(userDetail),
            url: UiRootDir + "/Login/OpenDefaultPageForUser",
            success: function (result) {
                if (result.Data != undefined && result.Data != null) {
                    var resultObject = new Object();
                    resultObject.userPreferences = JSON.parse(result.Data);
                    resultObject.UserTimeZone = result.UserTimeZone;
                    resultObject.signumOrganisationId = result.signumOrganisationId;
                    resultObject.SetProfileHistoryStatus = result.SetProfileHistoryStatus;
                    OpenDefaultWindow(resultObject);
                }
            }
        });
    }
}

function OpenDefaultWindow(resultObject) {
    var userPreferences = resultObject.userPreferences;
    $(".removeHighlight").removeClass("highlight");
    $(resultObject.userPreferences).addClass("highlight");

    localStorage.setItem("signumOrganisationId", resultObject.signumOrganisationId);
    console.log(resultObject.signumOrganisationId);

    if (resultObject.UserTimeZone == null) {
        localStorage.setItem("UserTimeZone", "Asia/Kolkata")
    }
    else {
        localStorage.setItem("UserTimeZone", resultObject.UserTimeZone);
    }

    if (resultObject.SetProfileHistoryStatus == true) {
        console.log("History saved Successfully");
    }
    else {
        console.log("Error while saving user history");
    }

    var requestId = getUrlParameter("requestId");

    var pgid = getUrlParameter("pgid");

    if (pgid == "requestapproval") {
        window.location.href = UiRootDir + "/RequestApproval/RequestApproval?pgid=requestapproval&requestId=" + requestId;
    }
    else {
        if (userPreferences.defaultPageId != "" && userPreferences.defaultPageId != undefined) {
            var href = userPreferences.defaultPageName.split(",");
            window.location.href = UiRootDir + href[1];
        }
        else {
            window.location.href = UiRootDir + "/Home";
        }

    }
}

function reverseStr(s) {
    return s.split("").reverse().join("");
}

function pEncrypt(p) {
    var revStr, revStrInArr, strAsci = '';
    //reverse str
    revStr = reverseStr(p);

    for (var i = 0; i < revStr.length; i++) {

        strAsci += revStr.charCodeAt(i) + '|';
    }

    strAsci = strAsci.substring(0, strAsci.length - 1);

    var fString = window.btoa(strAsci);

    return fString;
}

function getLoginMessage() {
    //document.getElementById("lblLogin").textContent = "We will have downtime next  month nearly about on 5th of May . please try to understand.";
    serviceUrl = service_java_URL + "accessManagement/getSiteStatus";

    $.isf.ajax({
        url: serviceUrl,

        success: function (data) {
            var msgData = data;


            if (msgData) {
                // document.getElementById("lblLogin").textContent = "ISF application will be unavailable in the duration between" + msgData.StartDate + " to " + msgData.EndDate;
                document.getElementById("lblLogin").textContent = msgData.Message;
                var color = '#' + msgData.Color;
                $("#lblLogin").css({ "color": color });
            }
            //else 
            //{
            //   // document.getElementById("lblLogin").textContent = "ISF application will be unavailable in future ,Downtime will be communicated soon."
            //    document.getElementById("lblLogin").textContent = "";
            //}


        },
        error: function (xhr, status, statusText) {

            console.log('An error occurred /n/n' + statusText);
        }
    });
   // getNavigationItems();
}

function getNavigationItems() {
    var navArr = []; getNav = [];

    var getNav = NavigationListSession;
        
    //console.log(data);
    navArr =JSON.parse(UserMenuSession);
        var str = '';

        var menuIdArr = [];
        var menueIdsWithUrl = [];

        for (var i in navArr) {

            var pTitle = navArr[i]['groupTitle'];
            var pId = navArr[i]['id'];
            var pHref = navArr[i]['groupHref'];
            var iClass = navArr[i]['groupIcon'];

            menuIdArr.push(pId); //store all ids of groups

            if (!pHref.match(/javascript|#/)) {

                pHref = UiRootDir + pHref;

                menueIdsWithUrl.push({ url: pHref, id: pId }); // store all ids with url location of groups (parent menue)
            } else {
                if (pHref.match(/#/)) {

                    menueIdsWithUrl.push({ url: pHref, id: pId }); // store all ids with url location of groups (parent menue)
                }
            }

            str = str + '<li title="' + pTitle + '" id="' + pId + '">';
            str = str + '<a href="' + pHref + '">';
            str = str + '<div class="gui-icon">';
            str = str + '<i class="' + iClass + '"></i>';
            str = str + '</div> <span class="title">' + pTitle + '</span>';
            str = str + '</a>';


            if (navArr[i]['submenu']) {

                str = str + '<ul>';
                var inMenue = navArr[i]['submenu'];
                for (var inArr in inMenue) {

                    menuIdArr.push(inMenue[inArr]['id']); //store all ids of submenues


                    if (inMenue[inArr]['onClick'] == "1") {
                        str = str + '<li id="' + inMenue[inArr]['id'] + '" onclick="ActivePage(\'' + inMenue[inArr]['id'] + '\');"><a href="' + UiRootDir + inMenue[inArr]['subMenuHref'] + '"><span class="title">' + inMenue[inArr]['subMenuTitle'] + ' </span></a></li>';

                        menueIdsWithUrl.push({ url: UiRootDir + inMenue[inArr]['subMenuHref'], id: pId }); // store all ids with url location of submenues

                    } else {
                        str = str + '<li id="' + inMenue[inArr]['id'] + '"><a href="' + inMenue[inArr]['subMenuHref'] + '"><span class="title">' + inMenue[inArr]['subMenuTitle'] + ' </span></a></li>';
                    }
                }

                str = str + '</ul>';
            }

            str = str + '</li>';

        }
        //console.log(menueIdsWithUrl);
      
        set_Session_Values('NavigationList', str);
        set_Session_Values('MenuIdList', JSON.stringify(menuIdArr));
        set_Session_Values('MenueIdsWithUrl', JSON.stringify(menueIdsWithUrl));

    
}
function setUserPreference(signum) {
    let result;

    
     $.isf.ajax({
        url: service_java_URL + "accessManagement/getUserPreferences/" + signum,
        type: "GET",
        crossDomain: true,
        async: false,
        success: function (data) {
            var accessProfileName = JSON.parse(ActiveProfileSession).accessProfileName;
           
            if (data.length != 0) {
                if (data.defaultZoneValue != null)
                    localStorage.setItem("UserTimeZone", data.defaultZoneValue.split(" - ")[1].trim());
                if (data.defaultProfileValue != accessProfileName && data.defaultProfileValue != null) {
                    switchProfileUserPreference(data);

                } else {
                    SetProfileHistory(JSON.parse(ActiveProfileSession));
                }
            }
           
            result = data;
        },
        error: function (xhr, status, statusText) {

            console.log('An error occurred');
        }
     }); 

    return result;
}

function switchProfileUserPreference(obj) {
    var activateProfileName = obj.defaultProfileValue;
    var profileList = JSON.parse(ProfileListSession);
    var organizationalModal = JSON.parse(UserDetailsSession)[0].lstOrganizationModel;

    for (var i = 0; i < profileList.length; i++) {
        if (profileList[i].accessProfileName == activateProfileName) {
            var profile = JSON.stringify(profileList[i]);
            var pageAccess = JSON.stringify(profileList[i].lstCapabilityModel);
            let org = profileList[i].organisation;
            let userRole = profileList[i].role;
            //localStorage.setItem("ActiveProfile", profile);
            //localStorage.setItem("PageAccess", pageAccess);
            set_Session_Values('Organisation', org);
            set_Session_Values('UserRole', userRole);
            set_Session_Values('ActiveProfile', profile);
            set_Session_Values('PageAccess', pageAccess);
            set_Session_Values('UserTimeZone', obj.defaultZoneValue.split(" - ")[1].trim());
            localStorage.setItem("UserTimeZone", obj.defaultZoneValue.split(" - ")[1].trim());
            SetProfileHistory(profileList[i]);
        }
    }
    var signumOrganisationId = JSON.parse(ActiveProfileSession).organisationID;
    console.log(signumOrganisationId);
    localStorage.setItem("signumOrganisationId", signumOrganisationId);
    $(".removeHighlight").removeClass("highlight");
    $(obj).addClass("highlight");
   // getNavigationItems();

}


function authenticateUser() {

    if ($("#username").val() == "" || $("#password").val() == "") {
        pwIsf.alert({ msg: 'Please provide User Name and Password..', type: 'error' });
        return;
    }
    var hostName = document.location.hostname;
    var un = 'ericsson\\' + $("#username").val();
    var pw = pEncrypt($("#password").val());

    // Get navigation data
    //return;

    if ($("#username").val() != "isfadmin" && $("#password").val() == "isfadmin") {
        //localStorage.setItem("userLoggedIn", $("#username").val());
        localStorage.setItem("isSuperAdminLogin", false);
       
        AutherizeUser($("#username").val());
        //getNavigationItems();
    }
    else if ($("#username").val() != "isfadmin" && $("#password").val() == "isfsuperadmin" && (hostName == "localhost" || hostName == "isfdev.internal.ericsson.com")) {
        //localStorage.setItem("userLoggedIn", $("#username").val());
        localStorage.setItem("isSuperAdminLogin", false);

       
        AutherizeUser($("#username").val());
       // getNavigationItems();
    }
    else {
        var JsonObj = new Object();
        JsonObj.username = un;
        JsonObj.password = pw;
        var postData = JSON.stringify(JsonObj);
        var serviceUrl;
        serviceUrl = service_java_URL + "accessManagement/authorize";

        $.isf.ajax({
            url: serviceUrl,
            
            context: this,
            crossdomain: true,
            processData: true,
            contentType: 'application/json',
            type: 'POST',
            data: postData,
            xhrFields: {
                withCredentials: false
            },
            success: function (data) {
                if (data == true) {
                    //localStorage.setItem("userLoggedIn", $("#username").val());
                    localStorage.setItem("isSuperAdminLogin", false);
                    AutherizeUser($("#username").val());
                    //getNavigationItems();
                }
                else {
                    $("#loginAlert").show();
                }
            },
            error: function (xhr, status, statusText) {
                console.log('An error occurred \n\n' + this.url + ' ' + xhr.status + ' ' + xhr.statusText);
            }
        })
    }
}

function authenticateUserASP() {
    var un = $("#asp_username").val();
    var pw = $("#asp_password").val();

    var JsonObj = new Object();
    JsonObj.email = un;
    JsonObj.currentPassword = pw;
    var postData = JSON.stringify(JsonObj);
    var serviceUrl;
    serviceUrl = service_java_URL + "accessManagement/aspIsfLogin";
    $.isf.ajax({
        url: serviceUrl,

        context: this,
        crossdomain: true,
        processData: true,
        contentType: 'application/json',
        type: 'POST',
        data: postData,
        xhrFields: {
            withCredentials: false
        },
        success: function (data) {
            if (data.isSuccess) {
                var signumASP = data.data.Signum;
                //localStorage.setItem("userLoggedIn", signumASP);
                localStorage.setItem("isSuperAdminLogin", false);
                if ((IsAspUser == "True") && (AspStatus != "APPROVED")) {
                    AutherizeUser(signumASP);
                   // getNavigationItems();
                }
            }
            else {
                pwIsf.alert({ msg: data.msg, type: "warning" });
            }
        },
        error: function (xhr, status, statusText) {
            console.log('An error occurred \n\n' + this.url + ' ' + xhr.status + ' ' + xhr.statusText);
        }
    });
}

function getUrlParameter(sParam) {
    var sPageURL = decodeURIComponent(window.location.search.substring(1)),
        sURLVariables = sPageURL.split('&'),
        sParameterName,
        i;

    for (i = 0; i < sURLVariables.length; i++) {
        sParameterName = sURLVariables[i].split('=');

        if (sParameterName[0] === sParam) {
            return sParameterName[1] === undefined ? true : sParameterName[1];
        }
    }
};

function dismissLoginAlert() {
    $("#loginAlert").hide();
}

function dismissAuthAlert() {
    $("#authAlert").hide();
}

function AutherizeUser(signum) {
    var userPreferences = '';
    serviceUrl = service_java_URL + "accessManagement/getUserAccessProfile/" + signum;

    $.isf.ajax({
        url: serviceUrl,
        async: false,
        beforeSend: function (xhr) { xhr.setRequestHeader('Signum', signum); }, 
        success: function (data) {
            if (data.isValidationFailed) {
                // $("#loginAlert").show();
            }
            else {
                userPreferences = setUserPreference(signum);
                localStorage.setItem("UserPreferance", userPreferences);
                serviceUrl = service_java_URL + "accessManagement/saveLoginDetails/" + signum;

                $.isf.ajax({
                    type: 'POST',
                    url: serviceUrl,
                    beforeSend: function (xhr) { xhr.setRequestHeader('Signum', signum); }, 
                    async: false,
                    success: function (data) {
                        console.log("success");
                        //  logId = data.logID;
                    },
                    error: function (xhr, status, statusText) {
                        //  alert("Fail " + xhr.responseText);
                        //  alert("status " + xhr.status);
                        console.log('An error occurred');
                    }
                });

                var requestId = getUrlParameter("requestId");

                var pgid = getUrlParameter("pgid");

                if (pgid == "requestapproval") {
                    window.location.href = UiRootDir + "/RequestApproval/RequestApproval?pgid=requestapproval&requestId=" + requestId;
                }
                else {
                    if (userPreferences.defaultPageId != "" && userPreferences.defaultPageId != undefined) {
                        var href = userPreferences.defaultPageName.split(",");
                        //console.log(href[1]);
                        window.location.href = UiRootDir + href[1];
                    }
                    else {
                        window.location.href = UiRootDir + "/Home";
                    }

                }
            }


        },
        error: function (xhr, status, statusText) {
            $("#authAlert").show();
            //localStorage.setItem("Authorised", false);
            //localStorage.removeItem("userLoggedIn");
            console.log('An error occurred /n/n' + statusText);
        }
    });
}

function getInstance() {
    alert(window.location.pathname.substr(0, window.location.pathname.lastIndexOf('/')));
}


function test() {
    var loginObj = Login();
    loginObj.test();
}



function Login() {
    this.login = function (username, password) {
        $.isf.ajax({
            url: service_java_URL + "accessManagement/authorize/" + username + '/' + password,

            context: this,
            crossdomain: true,
            processData: true,
            contentType: 'text/plain',
            type: 'POST',
            data: '',
            xhrFields: {
                withCredentials: false
            },
            success: function (data) {
            },
            error: function (xhr, status, statusText) {
                console.log('An error occurred \n\n' + this.url + ' ' + xhr.status + ' ' + xhr.statusText);
            }
        })
    }

    this.test = function () {
        alert("test");
    }
}

function openDefaultPage(signum) {
    userPreferences = setUserPreference(signum);

    var requestId = getUrlParameter("requestId");

    var pgid = getUrlParameter("pgid");

    if (pgid == "requestapproval") {
        window.location.href = UiRootDir + "/RequestApproval/RequestApproval?pgid=requestapproval&requestId=" + requestId;
    }
    else {
        if (userPreferences.defaultPageId != "" && userPreferences.defaultPageId != undefined) {
            var href = userPreferences.defaultPageName.split(",");
            //console.log(href[1]);
            window.location.href = UiRootDir + href[1];
        }
        else {
            window.location.href = UiRootDir + "/Home";
        }

    }
}

function SetProfileHistory(obj) {
    var profileHistory = new Object();
    profileHistory.signum = signumGlobal;
    profileHistory.accessTime = "";

    if (obj != undefined && obj.accessProfileID != undefined && obj.accessProfileName != undefined) {
        profileHistory.accessProfileID = parseInt(obj.accessProfileID);
        profileHistory.accessProfileName = obj.accessProfileName;
    }
    else {
        profileHistory.accessProfileID = JSON.parse(ActiveProfileSession).accessProfileID
        profileHistory.accessProfileName = JSON.parse(ActiveProfileSession).accessProfileName
    }

    var JsonObj = JSON.stringify(profileHistory);
    $.isf.ajax({
        url: service_java_URL + "accessManagement/saveUserProfileHistory",
        type: "POST",
        contentType: 'application/json',
        async: false,
        data: JsonObj,
        async: false,
        success: function (data) {
            if (data.formErrorCount == 0 && data.formMessages.length != 0) {
                console.log(data.formMessages[0]);
            }
        },
        error: function (xhr, status, statusText) {
            console.log("Fail " + xhr.responseText);
        }
    });
}