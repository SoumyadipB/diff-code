using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;

namespace ISF_WEB.ServerPush.Model
{
    public class AuditDetailUserCommentWoDetail
    {
        public string SignumId { get; set; }
        public string UniquePageID { get; set; }
        public string Message { get; set; }
        public string ActorType { get; set; }
        public string CreatedBy { get; set; }
    }
}