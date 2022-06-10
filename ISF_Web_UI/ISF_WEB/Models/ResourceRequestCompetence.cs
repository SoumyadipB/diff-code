using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;

namespace ISF_WEB.Models
{
    public class ResourceRequestCompetence
    {
        public int ResourceRequestCompetenceID { get; set; }
        public string CompetenceLevel { get; set; }
        public ResourceRequest ResourceRequestID { get; set; }
        public Competence CompetenceID { get; set; }
        public string CreatedBy { get; set; }

        public DateTime CreatedOn { get; set; }

        public String LastModifiedBy { get; set; }

        public DateTime LastModifiedOn { get; set; }
    }
}