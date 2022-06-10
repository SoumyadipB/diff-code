using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;

namespace ISF_WEB.Models
{
    public class Role
    {
        public int RoleID { get; set; }
        public string RoleName { get; set; }
        public string CreatedBy { get; set; }
        public DateTime CreatedOn { get; set; }
        public String LastModifiedBy { get; set; }
        public DateTime LastModifiedOn { get; set; }

    }
}