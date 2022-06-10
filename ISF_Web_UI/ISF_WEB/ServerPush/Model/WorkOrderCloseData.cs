using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;

namespace ISF_WEB.ServerPush.Model
{
    public class WorkOrderCompletionData
    {
        public string Signum { get; set; }
        public int WoId { get; set; }
        public bool IsWorkOrderCompleted { get; set; }
    }
}