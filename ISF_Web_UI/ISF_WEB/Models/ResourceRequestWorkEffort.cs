using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;

namespace ISF_WEB.Models
{
    public class ResourceRequestWorkEffort
    {
        public int ResourceRequestWorkEffortID { get; set; }
        public ResourceRequest ResourceRequestID { get; set; }
        public DateTime StartDate { get; set; }
        public DateTime EndDate { get; set; }
        public int Duration { get; set; }
        public float FTEPercent { get; set; }
        public float Hours { get; set; }
        public string CreatedBy { get; set; }

        public DateTime CreatedOn { get; set; }

        public String LastModifiedBy { get; set; }

        public DateTime LastModifiedOn { get; set; }
    }
}