using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;

namespace ISF_WEB.Models
{
    public class FileUploadInputDetail
    {
        
        public string zipFileName { get; set; }
        public byte[] pFileContent { get; set; }
    }
}