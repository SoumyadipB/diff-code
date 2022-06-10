using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;

namespace ISF_WEB.Models
{
    public class Opportunity
    {
        public int OpportunityID { get; set; }
        public string OpportunityName { get; set; }
        public MarketArea MarketArea { get; set; }
        public Country Country { get; set; }
        public Customer Customer { get; set; }
        public Company Company { get; set; }
    }
}