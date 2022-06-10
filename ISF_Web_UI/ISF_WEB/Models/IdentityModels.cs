using Microsoft.AspNet.Identity.EntityFramework;
using System.Data.Entity;

namespace ISF_WEB.Models
{
    // You can add profile data for the user by adding more properties to your ApplicationUser class, please visit http://go.microsoft.com/fwlink/?LinkID=317594 to learn more.
    public class ApplicationUser : IdentityUser
    {
    }

    public class ApplicationDbContext : IdentityDbContext<ApplicationUser>
    {
        public DbSet<Project> project { get; set; }
        public DbSet<Opportunity> opportunity { get; set; }
        public DbSet<MarketArea> MarketArea { get; set; }
        public DbSet<Country> Country { get; set; }
        public DbSet<Customer> customer { get; set; }
        public DbSet<Company> company { get; set; }
        public DbSet<ProjectDocument> projectDocument { get; set; }
        public DbSet<ProductArea> productArea { get; set; }
        
        //public DbSet<Projects> projects { get; set; }
        public ApplicationDbContext()
            : base("DefaultConnection")
        {
        }
    }
}