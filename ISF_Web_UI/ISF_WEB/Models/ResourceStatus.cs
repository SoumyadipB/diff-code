using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;

namespace ISF_WEB.Models
{
    public class ResourceStatus
    {
        public int ResourceStatusID { get; set; }
        public string ResourceStatusName { get; set; }
        public string Signum { get; set; }
        public DateTime ResignedOrTransferredDate { get; set; }
        public DateTime ReleaseDate { get; set; }
        public string Reason { get; set; }
        public string CreatedBy { get; set; }
        public DateTime CreatedOn { get; set; }
        public String LastModifiedBy { get; set; }
        public DateTime LastModifiedOn { get; set; }
    }
}