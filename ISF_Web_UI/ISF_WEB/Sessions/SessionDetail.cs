using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;

namespace ISF_WEB.Sessions
{
    public class SessionDetail
    {
        
        public dynamic UserEmaiId { get; set; }
        public dynamic IsLoginModeAzureAd { get; set; }
        public dynamic ExpirationTime { get; set; }
        public dynamic Signum { get; set; }
        public dynamic PageAccessDetailsByRoleOfSignum { get; set; }
        public dynamic UserAccessProfile { get; set; }
        public dynamic UserDetails { get; set; }
        public dynamic PageAccess { get; set; }
        public dynamic ActiveProfile { get; set; }
        public dynamic ProfileList { get; set; }
        public dynamic UserName { get; set; }
        public dynamic UserImageUri { get; set; }
        public dynamic UserRole { get; set; }
        public dynamic Organisation { get; set; }
        public dynamic NavigationList { get; set; }
        public dynamic MenuIdList { get; set; }
        public dynamic MenueIdsWithUrl { get; set; }
        public dynamic UserMenu { get; set; }
        public dynamic UserUpn { get; set; }
        public dynamic UserTimeZone { get; set; }
        public dynamic UserPreference { get; set; }
        public dynamic SetProfileHistoryStatus { get; set; }
        public dynamic OrganisationId { get; set; }
        public dynamic UserWiseDesktopResponse { get; set; }

    }
}