using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;

namespace ISF_WEB.Models
{
    public class APIResponse
    {
        public int formErrorCount { get; set; }
        public int formMessageCount { get; set; }
        public int formWarningCount { get; set; }
        public dynamic formErrors { get; set; }
        public dynamic formMessages { get; set; }
        public dynamic formWarnings { get; set; }
        public dynamic responseData { get; set; }
        public dynamic errorsDetail { get; set; }
        public bool isValidationFailed { get; set; }
    }
}