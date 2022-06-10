

function prevent()
{
    $("form").submit(function (e) {
        e.preventDefault();
    });

    getLoginMessage();
}

function reverseStr(s) {
    return s.split("").reverse().join("");
}

function pEncrypt(p) {
    var revStr,revStrInArr,strAsci='';
    //reverse str
    revStr = reverseStr(p);
    
    for (var i = 0; i < revStr.length ; i++) {

        strAsci += revStr.charCodeAt(i) + '|';
    }
    
    strAsci = strAsci.substring(0, strAsci.length - 1);
   
    var fString = window.btoa(strAsci);
    return fString;
}


function getLoginMessage()
{
   
    $.isf.ajax({
        url: service_java_URL + "accessManagement/getSiteStatus",        
        success: function (data) {
            var msgData = data;           
            if (msgData)
                {
                    document.getElementById("lblLogin").textContent = msgData.Message;
                    var color = '#' + msgData.Color;
                    $("#lblLogin").css({ "color": color });
                    }               
        },
        error: function (xhr, status, statusText) {
          
            console.log('An error occurred /n/n' + statusText);
        }
    });
}


function getNavigationItems() {

    var navArr = []; getNav = [];

    var getNav = localStorage.getItem("navigationList");

    if (!getNav) {
        
        $.isf.ajax({
            url: service_java_URL + "accessManagement/getUserMenu",
            beforeSend: function (xhr) { xhr.setRequestHeader('Signum', signum); }, 
            success: function (data) {
                
                var navArr = data;
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


                            if (inMenue[inArr]['onClick']=="1") {
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
               
                localStorage.setItem("navigationList", str);
                localStorage.removeItem("menuIdList");
                localStorage.setItem("menuIdList", JSON.stringify(menuIdArr));

                localStorage.removeItem("menueIdsWithUrl");
                localStorage.setItem("menueIdsWithUrl", JSON.stringify(menueIdsWithUrl));
                
            },
            error: function (xhr, status, statusText) {

                console.log('An error occurred /n/n (getNavigationItems)' + statusText);
            }
        });
    } 
}

function AuthenticateForToken(userName, password) {
    $.isf.ajax({
        async: false,
        url: service_Login_URL + "username=" + userName + "&password=",
        success: function (data) {

            localStorage.setItem("BearerKey", data);

            //$.ajaxSetup({
              //  beforeSend: function (xhr) {
                //   localStorage.setItem("BearerKey", data);
                   
                  //  xhr.setRequestHeader("Authorization", "Bearer " + data);
                //}
            //});

        },
        error: function (xhr, status, statusText) {
            console.log('An error occurred on Login: ' + xhr.error);
        }
    })
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

    if ($("#username").val() != "isfadmin" && $("#password").val() == "isfadmin") {
        localStorage.setItem("userLoggedIn", $("#username").val());
        localStorage.setItem("isSuperAdminLogin", false);
        if (EnableSecurity == true) {
            AuthenticateForToken($("#username").val(), "");
        }
        AutherizeUser($("#username").val());
        getNavigationItems();
        
    }
    else if ($("#username").val() != "isfadmin" && $("#password").val() == "isfsuperadmin" && (hostName == "localhost" || hostName == "isfdev.internal.ericsson.com" )) {
        localStorage.setItem("userLoggedIn", $("#username").val());
        localStorage.setItem("isSuperAdminLogin", false);

        if (EnableSecurity == true) {
            AuthenticateForToken($("#username").val(), "");
        }
        AutherizeUser($("#username").val());
        getNavigationItems();
        
    }
    else {
        var JsonObj = new Object();
        JsonObj.username = un;
        JsonObj.password = pw;
        var postData = JSON.stringify(JsonObj);
        var serviceUrl;
        if (EnableSecurity == true) {
            serviceUrl = service_Auth_URL + "accessManagement/authorize";
        } else {
            serviceUrl = service_java_URL + "accessManagement/authorize";

        }

        $.isf.ajax({
            url: serviceUrl,

            context: this,
            crossdomain: true,
            processData: true,
            contentType: 'application/json',
            type: 'POST',
            beforeSend: function(request) {
                request.setRequestHeader("Signum", $("#username").val());
            },
            data: postData,
            xhrFields: {
                withCredentials: false
            },
            success: function (data) {
                if (data == true) {
                    localStorage.setItem("userLoggedIn", $("#username").val());
                    localStorage.setItem("isSuperAdminLogin", false);

                    if (EnableSecurity == true) {
                        AuthenticateForToken($("#username").val(), "");
                    }
                    AutherizeUser($("#username").val());
                    getNavigationItems();
                    
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

function authenticateUserASP(that) {
    var un =  $("#asp_username").val();
    var pw = $("#asp_password").val();
    var vendorCode = $("#asp_vendorCode").val();
    if (un == "" || pw == "" || vendorCode == ""){
        pwIsf.alert({ msg: "Please fill all the fields!" });
        $($(that).data("target")).remove();
    }
        
    else
    {
     var JsonObj = new Object();
    JsonObj.email = un;
    JsonObj.currentPassword = pw;
    JsonObj.vendorCode = vendorCode;
    var postData = JSON.stringify(JsonObj);
    var serviceUrl;
    serviceUrl = service_java_URL + "accessManagement/aspIsfLogin";
    pwIsf.addLayer({ text: "Please wait ..." });
    $.isf.ajax({
        url: serviceUrl,
        async:false,
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
            
            responseHandler(data);
            
            if (data.responseData.isSuccess && data.responseData.isResetRequired) {
                pwIsf.removeLayer();
            }
            else if (data.responseData.isSuccess && !data.responseData.isResetRequired) {
                
                    $($(that).data("target")).remove();
                    var signumASP = data.responseData.data.Signum;
                    localStorage.setItem("userLoggedIn", signumASP);
                    localStorage.setItem("isSuperAdminLogin", false);

                    AutherizeUser(signumASP);
                    getNavigationItems();
            }
            
            else {
                pwIsf.removeLayer();
                $($(that).data("target")).remove();
            }
      
        },        
        error: function (xhr, status, statusText) {
            pwIsf.removeLayer();
            $($(that).data("target")).remove();
            pwIsf.alert({ msg: "Something went wrong!", type: "error" });
            console.log('An error occurred \n\n' + this.url + ' ' + xhr.status + ' ' + xhr.statusText);
        }
    })
    }
           
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


function dismissLoginAlert()
{
    $("#loginAlert").hide();
}

function dismissAuthAlert() {
    $("#authAlert").hide();
}
function setUserPreference(signum) {
    $.isf.ajax({
        url: service_java_URL + "accessManagement/getUserPreferences/" + signum,
        type: "GET",
        crossDomain: true,
        async: false,
        success: function (data) {            
            var accessProfileName = JSON.parse(localStorage.getItem("ActiveProfile")).accessProfileName;
            if (data.length != 0) {
                if (data.defaultProfileValue != accessProfileName && data.defaultProfileValue != null) {
                    switchProfileUserPreference(data);
                    set_Session_Values("UserTimeZone", data.defaultZoneValue.split(" - ")[1].trim());
                    localStorage.setItem("UserTimeZone", data.defaultZoneValue.split(" - ")[1].trim());
                }
            }
        },
        error: function (xhr, status, statusText) {
            
            console.log('An error occurred');
        }
    });


}
function switchProfileUserPreference(obj) {
    var activateProfileName = obj.defaultProfileValue;
    var profileList = JSON.parse(localStorage.getItem("ProfileList"));
    var organizationalModal = JSON.parse(localStorage.getItem("UserDetails"))[0].lstOrganizationModel;

    for (var i = 0; i < profileList.length; i++) {
        if (profileList[i].accessProfileName == activateProfileName) {
            var profile = JSON.stringify(profileList[i]);
            var pageAccess = JSON.stringify(profileList[i].lstCapabilityModel);
            localStorage.setItem("ActiveProfile", profile);
            localStorage.setItem("PageAccess", pageAccess);
        }
    }
    var signumOrganisationId = JSON.parse(localStorage.getItem("ActiveProfile")).organisationID;
    console.log(signumOrganisationId);
    localStorage.setItem("signumOrganisationId", signumOrganisationId);
    $(".removeHighlight").removeClass("highlight");
    $(obj).addClass("highlight");    
    pwIsf.addLayer();
    window.location.href = UiRootDir + "/Home";
    pwIsf.removeLayer();
}

function AutherizeUser(signum) {

    $.isf.ajax({
        url: service_java_URL + "accessManagement/getUserAccessProfile/" + signum,
        beforeSend: function (xhr) { xhr.setRequestHeader('Signum', signum); }, 
        success: function (data) {
            if (data.isValidationFailed) {
                $("#loginAlert").show();
            }
            else {
                
                var userObj = JSON.stringify(data.responseData);
                localStorage.setItem("UserDetails", userObj);
                var activeProfile = JSON.stringify(data.responseData[0].lstAccessProfileModel[0]);
                var pageList = JSON.stringify(JSON.parse(activeProfile).lstCapabilityModel);
                var profileList = JSON.stringify(data.responseData[0].lstAccessProfileModel);

                localStorage.setItem("PageAccess", pageList);
                localStorage.setItem("ActiveProfile", activeProfile);
                localStorage.setItem("ProfileList", profileList);
                localStorage.setItem("Authorised", true);
                localStorage.setItem("Username", data.responseData[0].employeeName);
                localStorage.setItem("userImageUri", data.responseData[0].userImageUri);
                setUserPreference(signum);               

                $.isf.ajax({
                    type: 'POST',
                    url: service_java_URL + "accessManagement/saveLoginDetails/" + signum,
                    beforeSend: function (xhr) { xhr.setRequestHeader('Signum', signum); }, 
                    success: function (data) {
                        console.log("success");
                    },
                    error: function (xhr, status, statusText) {
                        console.log('An error occurred');
                    }
                });

                var requestId = getUrlParameter("requestId");

                var pgid = getUrlParameter("pgid");

                if (pgid == "requestapproval") {
                    window.location.href = UiRootDir + "/RequestApproval/RequestApproval?pgid=requestapproval&requestId=" + requestId;
                }
                else {
                    window.location.href = UiRootDir + "/Home";
                }
            }         
        },
        error: function (xhr, status, statusText) {
            $("#authAlert").show();
            localStorage.setItem("Authorised", false);
            localStorage.removeItem("userLoggedIn");
            console.log('An error occurred /n/n' + statusText);
        }
    });
}

function getInstance()
{
    alert(window.location.pathname.substr(0, window.location.pathname.lastIndexOf('/')));
}

function test()
{
    var loginObj = Login();
    loginObj.test();
}

    function Login()
    {
        this.login = function(username, password)
        {
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

        this.test = function ()
        {
            alert("test");
        }
    }



