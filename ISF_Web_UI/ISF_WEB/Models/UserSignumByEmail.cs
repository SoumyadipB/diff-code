using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;

namespace ISF_WEB.Models
{
    public class UserSignumByEmail
    {
        public string signumID { get; set; }
        public string employeeName { get; set; }
        public string unit { get; set; }
        public string managerSignum { get; set; }
        public string isLineManager { get; set; }
        public string jobStage { get; set; }
        public string aspStatus { get; set; }
    }
}