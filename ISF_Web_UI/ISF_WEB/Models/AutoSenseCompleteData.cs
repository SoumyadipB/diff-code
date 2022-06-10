using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;

namespace ISF_WEB.Models
{
    public class AutoSenseCompleteData
    {
        public string WoId { get; set; }
        public string FlowChartStepID { get; set; }
        public string FlowChartDefID { get; set; }
        public string ExecutionType { get; set; }
        public string IsStepAutoSenseEnabled { get; set; }
        public string WorkFlowAutoSenseEnabled { get; set; }
        public string WorkOrderAutoSenseEnabled { get; set; }
        public string StartRule { get; set; }
        public string StopRule { get; set; }
        public string NextStepExecutionType { get; set; }
        public string IsNextStepAutoSenseEnabled { get; set; }
        public string NextStepStartRule { get; set; }
        public string NextStepStopRule { get; set; }
        public string TaskID { get; set; }
        public string NextStepType { get; set; }
    }
}