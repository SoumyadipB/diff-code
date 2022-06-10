using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;

namespace ISF_WEB.Models
{
    public class JobRole
    {
        public int JobRoleID { get; set; }

        public String JobRoleName { get; set; }

        public string CreatedBy { get; set; }

        public DateTime CreatedOn { get; set; }

        public String LastModifiedBy { get; set; }

        public DateTime LastModifiedOn { get; set; }
    }
}