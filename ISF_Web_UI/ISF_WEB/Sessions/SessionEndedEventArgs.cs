using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace ISF_WEB.Sessions
{
    public class SessionEndedEventArgs : EventArgs
    {
        public readonly string SessionId;
        public readonly dynamic SessionObject;

        public SessionEndedEventArgs(string sessionId, dynamic sessionObject)
        {
            SessionId = sessionId;
            SessionObject = sessionObject;
        }
    }
}
