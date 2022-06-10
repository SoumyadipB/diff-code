using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;

namespace ISF_WEB.Models
{
    public class ProjectScopeDetail
    {
        public int ProjectScopeDetailID { get; set; }

        public ProjectScope ProjectScopeID { get; set; }

        public int DomainID { get; set; }

        public int ServiceAreaID { get; set; }

        public int TechnologyID { get; set; }
    }
}