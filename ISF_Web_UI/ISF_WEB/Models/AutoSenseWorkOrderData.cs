using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;

namespace ISF_WEB.Models
{
    public class AutoSenseWorkOrderData
    {
        public string flowchartDefID { get; set; }
        public string woID { get; set; }
        public string stepID { get; set; }
        public string taskID { get; set; }
        public string signumID { get; set; }
        public string overrideAction { get; set; }
        public string source { get; set; }
        public string startRule { get; set; }
        public string stopRule { get; set; }
        public string action { get; set; }
        
    }
}
