using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;

namespace ISF_WEB.Models
{
    public class ApiData
    {
        public string Param { get; set; }
        public string Technology { get; set; }
        public string CallType { get; set; }
        public string ContentType { get; set; }
        public dynamic Data { get; set; }
    }
}