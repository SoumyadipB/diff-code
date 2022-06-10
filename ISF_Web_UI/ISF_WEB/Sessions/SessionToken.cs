using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;

namespace ISF_WEB.Sessions
{
    public class SessionToken
    {
       
        public string Token { get; set; }
        public string Signum { get; set; }
        public string Role { get; set; }
        public string Organisation { get; set; }
        public int ExpirationTime { get; set; }
    }
}