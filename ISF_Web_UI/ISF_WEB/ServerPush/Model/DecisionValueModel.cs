using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;

namespace ISF_WEB.ServerPush.Model
{
    public class DecisionValueModel
    {
        public int woId { get; set; }
        public string status { get; set; }
        public string decisionValue { get; set; }
        public string signumId { get; set; }
        public string executionType { get; set; }
        public int wOFCStepDetailsID { get; set; }
        public string flowChartStepId { get; set; }
    }
}