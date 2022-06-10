using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;

namespace ISF_WEB.Util
{
    public static class ConstantKeywords
    {
        public const string C_UserGivenName = "UserGivenName";
        public const string C_UserSurName = "UserSurName";
        public const string C_UserEmaiId = "UserEmaiId";
        public const string C_UserType = "UserType";
        public const string C_UserUpn = "UserUpn";
        public const string C_IsLoginModeAzureAd = "IsLoginModeAzureAd";
        public const string C_ExpirationTime = "ExpirationTime";
        public const string C_IsUserLogOut = "IsUserLogOut";
        public const string C_IsSessionTimeOut = "IsSessionTimeOut";
        public const string C_Signum = "Signum";
        public const string C_PageAccessDetailsByRoleOfSignum = "PageAccessDetailsByRoleOfSignum";
        public const string C_UserAccessProfile = "UserAccessProfile";
        public const string C_UserObjectIdentifier = "ObjectIdentifier";

        public const string C_UserDetails = "UserDetails";
        public const string C_PageAccess = "PageAccess";
        public const string C_ActiveProfile = "ActiveProfile";
        public const string C_ProfileList = "ProfileList";
        public const string C_Authorised = "Authorised";
        public const string C_UserName = "UserName";
        public const string C_UserImageUri = "UserImageUri";
        public const string C_UserRole = "UserRole";
        public const string C_Organisation = "Organisation";

        public const string C_NavigationList = "NavigationList";
        public const string C_MenuIdList = "MenuIdList";
        public const string C_MenueIdsWithUrl = "MenueIdsWithUrl";
        public const string C_UserMenu = "UserMenu";
        public const string C_UserMenuByRole = "UserMenuByRole";
        public const string C_UserMenuByRoleDictionary = "UserMenuByRoleDictionary";
        public const string C_SignumDetailsFromEmail = "SignumDetailsFromEmail";
        public const string C_SignumOrganisationId = "SignumOrganisationId";
        public const string C_ProfileHistoryStatus = "ProfileHistoryStatus";
        public const string C_UserPreference = "UserPreference";

        //For Headers to API
        public const string C_MarketArea = "MarketArea";
        public const string C_Role = "Role";
        public const string C_X_Auth_Token = "X-Auth-Token";

        public const string C_Index = "Index";

        public const string C_ReferrerController = "ReferrerController";
        public const string C_ReferrerAction = "ReferrerAction";

        public const string C_Isignum = "Isignum";
        public const string C_IsignumBaseEmail = "xead.ericsson.com";
        public const string C_EmployeeTypeAsp = "ASP";
        public const string C_EmployeeTypeEricson = "ERICSSON";
        public const string C_AspStatus = "AspStatus";
        public const string C_AspStatusApproved = "APPROVED";
        public const string C_IsAspUser = "IsAspUser";
        public const string C_StatusLogin = "Login";
        public const string C_StatusLogout = "Logout";
        public const string C_UserTimeZone = "UserTimeZone";
        public const string C_UserSessionTokenData = "UserSessionTokenData";
        public const string C_UserSessionAllData = "UserSessionAllData";
        public const string C_UserInfoAlreadySaved = "UserInfoAlreadySaved";
        public const string C_UserLoginId = "UserLoginId";
        public const char C_IsignumBaseCharSignum = 'Z';
        public const string C_UI = "ISFWebUI";
        public const string C_FloatingWindow = "FloatingWindow";
        public const string C_EventPublisher = "EventPublisher";
        public const string C_BookingType = "BOOKING";
        public const string C_SUCCESS = "SUCCESS";
        public const string C_FAIL = "FAIL";
        public const string C_FAIL_MSG = "FAIL - No data recieved for work order closer";
        public const string C_Scanner = "Scanner";
        public const string C_SessionDetailsForLogout = "SessionDetailsForLogout";
        public const string C_RedisKeyExpiredEvent = "expired";
        public const string C_LoginDetails = "_LoginDetails";
        public const string C_DBPattenFront = "__keyspace@";
        public const string C_DBPattenBack = "__:*";
        public const string C_MinThread = "MinThread";
        public const string C_EndpointProperty = "endpoint";
        public const string C_AccessKeyProperty = "accesskey";
        public const string C_APITransactionLogFile = "_APITransactionLogFile.log";
        public const string C_ExceptionLogFile = "_ExceptionLogFile.log";
        public const string C_InformationLogFile = "_InformationLogFile.log";
        public const string C_SignalRInfoLogFile = "_SignalRInfoLogFile.log";
        public const string C_SignalRMessageTransaction = "_SignalRMessageTransaction.log";
        public const string C_IsDesktopVersionUpdateRequired = "isDesktopUpdateRequired";
        public const string C_RedisKey = "_LatestDesktopVersion";
        public const string C_NotificationRedisKey = "_Sit";
        public const string C_NotificationData = "NotificationData";
    }

    public enum BookingStatus
    {
        STARTED,
        ONHOLD,
        SKIPPED,
        COMPLETED,

    }


}