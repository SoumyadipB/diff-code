var path = (document.location.pathname).split('/');
var UiRootDir = '';
if (path.length > 2) {
    if (path[1].indexOf("ISF")> -1) {
   
            UiRootDir = `/${path[1]}`;
        }
}
var cloudFlag = false;
const conf = {
    DEBUG: true
};

let getAllSession = JSON.parse(getAllSessionValues());
let configurationDetailsData;

if (localStorage.getItem("ConfigurationDetail") === null || localStorage.getItem("ConfigurationDetail") === '{}' || localStorage.getItem("ConfigurationDetail") === undefined) {
    configurationDetailsData = JSON.parse(getConfigurationDetail());
    let configurationDetail = JSON.stringify(getConfigurationDetail())
    localStorage.setItem("ConfigurationDetail", configurationDetail)
}
else {
    configurationDetailsData = JSON.parse(JSON.parse(localStorage.getItem("ConfigurationDetail")));
}

var environment = configurationDetailsData.environment;

//DEV

let limitDRAccessValue = configurationDetailsData.limitValueForDRAccess;
let manualAutoStepWFLimit = configurationDetailsData.manualAutoStepLimitValue;
let decisionStepWFLimit = configurationDetailsData.decisionStepLimitValue;
var MANUAL_STEP_EXE_DEFAULT_VALUE = configurationDetailsData.MANUAL_STEP_EXE_DEFAULT_VALUE;
var AUTOMATIC_STEP_EXE_DEFAULT_VALUE = configurationDetailsData.AUTOMATIC_STEP_EXE_DEFAULT_VALUE;
var NO_OF_WO_NEID_DEFAULT_VALUE = configurationDetailsData.NO_OF_WO_NEID_DEFAULT_VALUE;
var service_java_URL = configurationDetailsData.JavaApiUrl;
var service_java_URL_VM = configurationDetailsData.JavaApiVmUrl;
var downloadISFSetup = configurationDetailsData.downloadISFSetupUrl;
var downloadOutlookAdIn = configurationDetailsData.downloadOutlookAddinUrl;
var uploadNE = configurationDetailsData.uploadNE;
var pathUrlNE = configurationDetailsData.pathUrlNE;
var uploadRPAFile = "https://isfsit.internal.ericsson.com/fPath/botStore/UploadedOutput/";
const signumGlobal = getAllSession.Signum;
let UserDetailsSession = getAllSession.UserDetails;
let PageAccessSession = getAllSession.PageAccess;
let ActiveProfileSession = getAllSession.ActiveProfile;
let ProfileListSession = getAllSession.ProfileList;
let UserNameSession = getAllSession.UserName;
let UserImageUriSession = getAllSession.UserImageUri;
let UserMenuSession = getAllSession.UserMenu;
let MenuIdListSession = getAllSession.MenuIdList;
let MenueIdsWithUrlSession = getAllSession.MenueIdsWithUrl;
let IsAspUser = getSessionValuesByKey('IsAspUser');
let AspStatus = getSessionValuesByKey('AspStatus');
let isDesktopUpdateRequired = false;
let desktopVersionNumber = null;
if (getAllSession.UserWiseDesktopResponse) {
    const responseData = getAllSession.UserWiseDesktopResponse;
    isDesktopUpdateRequired = JSON.parse(responseData).result;
    desktopVersionNumber = JSON.parse(responseData).VersionNumber;
}


let UserPreferenceSession = getAllSession.UserPreference;
var UserPreferenceObj = new Object();
UserPreferenceObj.userPreferences = JSON.parse(UserPreferenceSession);
let OrganisationIdSession = getAllSession.OrganisationId;
let SetProfileHistoryStatusSession = getAllSession.SetProfileHistoryStatus;
let UserTimeZoneSession = getAllSession.UserTimeZone;

$(".removeHighlight").removeClass("highlight");
$(UserPreferenceObj.userPreferences).addClass("highlight");

localStorage.setItem("signumOrganisationId", OrganisationIdSession);
console.log(OrganisationIdSession);

if (UserTimeZoneSession == null) {
    localStorage.setItem("UserTimeZone", "Asia/Kolkata")
}
else {
    localStorage.setItem("UserTimeZone", UserTimeZoneSession);
}

if (SetProfileHistoryStatusSession === true) {
    console.log("History saved Successfully");
}
else {
    console.log("Error while saving user history");
}

var botNestExternal_URL = configurationDetailsData.botNestExternalUrl;  //<!-for testing-!>

//https://botnest.internal.ericsson.com/bn  <!-for prod-!>

//"http://10.184.19.206:5007/";

var uploadWIFile = configurationDetailsData.uploadWIFile;

//Server Push Configuration.
var startServerPush = true;
var BearerKey = localStorage.getItem("BearerKey");
var EnableSecurity = false;
var ApiProxy = false;
var gtagUrl = configurationDetailsData.gtagUrl;




if (UserDetailsSession != undefined) {
    var signumOrgId = JSON.parse(ActiveProfileSession).organisationID;
}

var Azure_SignalR_Hub_URL = configurationDetailsData.AzureSignalRHubUrl;
var AppInsights_Instrumentation_Key = configurationDetailsData.AppInsightsInstrumentationKey;
const notificationEnd = 10;
const PythonDefaultBaseVersion = "3.7.4";