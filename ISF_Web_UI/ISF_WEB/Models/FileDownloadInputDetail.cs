using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;

namespace ISF_WEB.Models
{
    public class FileDownloadInputDetail
    {
        public string botId { get; set; } // FIle Name with Extension
        public string botName { get; set; }
        public string isFixedPath { get; set; }
        public string downloadBasePath { get; set; }
        public string templateName { get; set; }

    }
}