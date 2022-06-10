using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;

namespace ISF_WEB.Models
{
    public class ProjectDocument
    {
        public int ProjectDocumentID { get; set; }        
        public string DocumentType { get; set; }
        public string DocumentLinks { get; set; }
        public Project Project { get; set; }
    }
}