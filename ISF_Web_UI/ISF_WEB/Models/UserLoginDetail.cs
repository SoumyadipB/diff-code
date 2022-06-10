using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;

namespace ISF_WEB.Models
{
    public class UserLoginDetail
    {
        public string sessionId { get; set; }
        public string emailID { get; set; }
        public string signumID { get; set; }
        public string status { get; set; }
        public string sourceDomain { get; set; }
        public string ipAddress { get; set; }
        public string device { get; set; }
        public string  browser { get; set; }
        public int logID { get; set; }
        public string sessionToken { get; set; }

    }
}