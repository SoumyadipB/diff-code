using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;

namespace ISF_WEB.Models
{
    public class Competence
    {
        public int CompetenceID { get; set; }

        public string CompetenceName { get; set; }
        public string CompetenceType { get; set; }
        public int ParentCompetenceID { get; set; }

        public string CreatedBy { get; set; }

        public DateTime CreatedOn { get; set; }

        public String LastModifiedBy { get; set; }

        public DateTime LastModifiedOn { get; set; }
    }
}