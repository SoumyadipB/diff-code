using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;

namespace ISF_WEB.Models
{
    public class UserRoleMapping
    {
        public int UserRoleMappingID { get; set; }
        public string Signum { get; set; }
        public Role RoleID { get; set; }
        public string CreatedBy { get; set; }
        public DateTime CreatedOn { get; set; }
        public String LastModifiedBy { get; set; }
        public DateTime LastModifiedOn { get; set; }
        
    }
}