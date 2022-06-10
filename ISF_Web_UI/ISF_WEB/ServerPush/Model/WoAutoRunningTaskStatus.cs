using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;

namespace ISF_WEB.ServerPush.Model
{
    public class WoAutoRunningTaskStatus
    {
        public string Signum { get; set; }
        public List<WoAutoTaskInfo> WoTaskInfo { get; set; }
    }
}