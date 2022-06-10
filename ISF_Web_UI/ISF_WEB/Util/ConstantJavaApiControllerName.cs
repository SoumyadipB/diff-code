using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;

namespace ISF_WEB.Util
{
    public static class ConstantJavaApiControllerName
    {
        public const string C_getUserSignumByEmail = @"/accessManagement/getUserSignumByEmail";
        public const string C_getPageAccessDetailsByRoleOfSignum = @"/accessManagement/getPageAccessDetailsByRoleOfSignum";
        public const string C_getUserAccessProfile = @"accessManagement/getUserAccessProfile/";
        public const string C_getUserMenu = @"accessManagement/getUserMenu";
        public const string C_saveLoginDetails = @"accessManagement/saveLoginDetails";
        public const string C_pauseAllTaskOnLogout = @"woExecution/pauseAllTaskOnLogout";
        public const string C_saveLogoutDetails = @"accessManagement/saveLogoutDetails";
        public const string C_getUserPreferance = @"accessManagement/getUserPreferences/";
        public const string C_getUserMenuByRole = @"accessManagement/getUserMenuByRole/";
        public const string C_getUserAccessProfileBySignum = @"accessManagement/getUserAccessProfileBySignum/";
        public const string C_getAccessProfileOfUser = @"accessManagement/getAccessProfileOfUser/";
        public const string C_saveUserProfileHistory = @"accessManagement/saveUserProfileHistory";
        public const string C_botDowload = @"appUtil/botDownload/";
        public const string C_isInstalledDesktopVersionUpdated = @"woExecution/isInstalledDesktopVersionUpdated";





    }
}