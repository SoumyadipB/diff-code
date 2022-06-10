using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;

namespace ISF_WEB.Models
{
    public class ResourceRequest
    {
        public int ResourceRequestID { get; set; }
        public string ResourceType { get; set; }
        public string RequestType { get; set; }
        public ProjectScopeDetail ProjectScopeDetailID { get; set; }
        public Project ProjectID { get; set; }
        public JobRole JobRoleID { get; set; }
        public JobStage JobStageID { get; set; }
        public OnsiteLocation OnsiteLocationID { get; set; }
        public int OnsiteCount { get; set; }
        public int RemoteCount { get; set; }
        public string CreatedBy { get; set; }

        public DateTime CreatedOn { get; set; }

        public String LastModifiedBy { get; set; }

        public DateTime LastModifiedOn { get; set; }
    }
}