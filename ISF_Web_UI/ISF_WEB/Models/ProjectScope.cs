using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;

namespace ISF_WEB.Models
{
    public class ProjectScope
    {
        public int ProjectScopeID { get; set; }

        public string ScopeName { get; set; }

        public DateTime StartDate { get; set; }

        public DateTime EndDate { get; set; }

        public Project ProjectID { get; set; }

    }
}