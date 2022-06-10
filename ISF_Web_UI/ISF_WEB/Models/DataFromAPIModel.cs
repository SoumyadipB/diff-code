using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;

namespace ISF_WEB.Models
{
    public class DataFromAPIModel
    {
        public bool IsSuccess { get; set; }
        public string ErrorMessage { get; set; }
        public dynamic response { get; set; }
    }
}