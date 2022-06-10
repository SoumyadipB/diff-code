using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;

namespace ISF_WEB.Models
{
    public class Employee
    {
        public int EmployeeID { get; set; }
        public string Signum { get; set; }
        public string EmployeeName { get; set; }
        public string EmployeeEmailID { get; set; }
        public string PersonnelNumber { get; set; }
        public string EmployeeNumber { get; set; }
        public string ContactNumber { get; set; }
        public string Gender { get; set; }
        public DateTime CareerStartDate { get; set; }
        public DateTime DateOfJoining { get; set; }
        public string Status { get; set; }
        public string ManagerSignum { get; set; }
        public string Team { get; set; }
        public string CostCenter { get; set; }
        public string FunctionalArea { get; set; }
        public string ServiceArea { get; set; }
        public string Grade { get; set; }
        public string JobRoleFamily { get; set; }
        public string JobRole { get; set; }
        public string JobStage { get; set; }
        public string City { get; set; }
        public string HRLocation { get; set; }
        public string OfficeBuilding { get; set; }
        public string Floor { get; set; }
        public string SeatNumber { get; set; }
        public string CreatedBy { get; set; }
        public DateTime CreatedOn { get; set; }
        public String LastModifiedBy { get; set; }
        public DateTime LastModifiedOn { get; set; }        
    }
}