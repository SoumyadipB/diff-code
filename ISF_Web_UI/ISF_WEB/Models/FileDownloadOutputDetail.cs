using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;

namespace ISF_WEB.Models
{
    public class FileDownloadOutputDetail
    {

        public string botId { get; set; }
        public string botName { get; set; }
        public byte[] pFileContent { get; set; }
        public string pFileName { get; set; }
        
    }
}