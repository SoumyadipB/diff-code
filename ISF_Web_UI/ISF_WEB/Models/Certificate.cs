using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;

namespace ISF_WEB.Models
{
    public class Certificate
    {
        public int CertificateID { get; set; }

        public string CertificateName { get; set; }
        public string Issuer { get; set; }
        public string CertificateType { get; set; }

        public string CreatedBy { get; set; }

        public DateTime CreatedOn { get; set; }

        public String LastModifiedBy { get; set; }

        public DateTime LastModifiedOn { get; set; }
    }
}