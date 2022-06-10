using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;

namespace ISF_WEB.Models
{
    public class Country
    {
        public int CountryID { get; set; }
        public MarketArea MarketArea { get; set; }
        public string CountryName { get; set; }
        public string Description { get; set; }
    }
}