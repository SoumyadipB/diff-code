using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;

namespace ISF_WEB.Models
{
    public class UserDetailsForSignum
    {
        public string employeeEmailID { get; set; }
        public string employeeType { get; set; }
        public string employeeUpn { get; set; }
        public string requestId { get; set; }
        public string pgid { get; set; }
        public string uiRootDir { get; set; }
    }
}