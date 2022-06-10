using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;

namespace ISF_WEB.Models
{
    public class Project
    {
        public int ProjectID { get; set; }
        public string ProjectName { get; set; }
        public string ProjectType { get; set; }
        public ProductArea ProductArea { get; set; }
        public string CPM { get; set; }
        public string ProjectCreator { get; set; }
        public Opportunity Opportunity { get; set; }
        public DateTime StartDate { get; set; }
        public DateTime EndDate { get; set; }
        public string ProjectDescription { get; set; }       
    }
}