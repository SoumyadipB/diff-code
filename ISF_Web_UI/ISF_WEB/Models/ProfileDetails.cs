using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;

namespace ISF_WEB.Models
{
    public class ProfileDetails
    {
        public int accessProfileID { get; set; }
        public string accessProfileName { get; set; }
        public int roleID { get; set; }
        public string role { get; set; }
        public int organisationID { get; set; }
        public string organisation { get; set; }
        public bool active { get; set; }
        public string alias { get; set; }
        public dynamic lstCapabilityModel { get; set; }
    }

    public class ProfileHistoryDetails
    {
        public int accessProfileID { get; set; }
        public string accessProfileName { get; set; }
        public string signum { get; set; }
        public string accessTime { get; set; }
    }
}