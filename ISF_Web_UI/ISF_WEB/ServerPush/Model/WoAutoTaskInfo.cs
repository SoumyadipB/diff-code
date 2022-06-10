using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;

namespace ISF_WEB.ServerPush.Model
{
    public class WoAutoTaskInfo
    {
        public string Signum { get; set; }
        public int WoId { get; set; }
        public int BookingId { get; set; }
        public string StartDate { get; set; }
        public string EndDate { get; set; }
        public string FlowChartStepId { get; set; }
        public string Hour { get; set; }
        public string Status { get; set; }
        public string Reason { get; set; }
        public string OutputLink { get; set; }
        public bool IconsOnNextStep { get; set; }
        public int FlowChartDefID { get; set; }

        public string flowChartType { get; set; }
        public string ExecutionType { get; set; }

        public string refferer { get; set; }
        public string actionPerfomed { get; set; }
        public string taskId { get; set; }
        public string overrideAction { get; set; }
        public bool? startRule { get; set; }
        public bool? stopRule { get; set; }
    }
}