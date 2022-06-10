using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;

namespace ISF_WEB.Models
{
    public class ActivityScope
    {
        public int ActivityScopeID { get; set; }

        public ProjectScopeDetail ProjectScopeDetailID { get; set; }

        public int SubActivityID { get; set; }

        public int AvgEstdEffort { get; set; }
    }
}