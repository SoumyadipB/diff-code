using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;

namespace ISF_WEB.Models
{
    public class OnsiteLocation
    {
        public int OnsiteLocationID { get; set; }

        public String LocationName { get; set; }

        public string CreatedBy { get; set; }

        public DateTime CreatedOn { get; set; }

        public String LastModifiedBy { get; set; }

        public DateTime LastModifiedOn { get; set; }

    }
}