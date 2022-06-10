using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;

namespace ISF_WEB.Models
{
    public class MqttDataModel
    {
        public string signum { get; set; }
        public string userName { get; set; }
        public string password { get; set; }
        public string hostNameMQTT { get; set; }
    }
}