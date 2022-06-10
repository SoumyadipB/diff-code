using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;

namespace ISF_WEB.Models
{
    public class ProjectApproval
    {
        public int ProjectApprovalID { get; set; }
        public Project ProjectID { get; set; }
        public string ApproverSignum { get; set; }
        public bool ApprovedOrRejected { get; set; }
        public DateTime ApprovedOrRejectedOn { get; set; }
        public string RequestedBy { get; set; }
        public DateTime RequestedOn { get; set; }
        

    }
}