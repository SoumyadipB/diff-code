using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;

namespace ISF_WEB.Models
{
    public class UserMenu
    {
        public string id { get; set; }
        public string groupTitle { get; set; }
        public string groupHref { get; set; }
        public string groupOnClick { get; set; }
        public string groupIcon { get; set; }
        public List<SubMenu> submenu { get; set; }
    }

    public class SubMenu
    {
        public string id { get; set; }
        public string subMenuTitle { get; set; }
        public string subMenuHref { get; set; }
        public string onClick { get; set; }
    }

    public class MenuWithURL
    {
        public string id { get; set; }
        public string url { get; set; }
    }

}