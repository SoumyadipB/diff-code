using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;
using Microsoft.AspNet.SignalR;

namespace ISF_WEB.ServerPush
{
    public class ISFConnectionsHub : Hub
    {
        ISFConnectionClass isfObj;
        public ISFConnectionsHub()
        {
            isfObj = new ISFConnectionClass();
        }

        public void GetClientInfo(string name, string message)
        {
            // Call the broadcastMessage method to update clients.
            Clients.Others.broadcastMessage(name, message);
        }

        public void clientInfo(string ConnectionID, string clientType, string signumID, string clientPage)
        {
            
            isfObj.clientConnected(ConnectionID, clientType, signumID, clientPage);
        }

        public void reminder(string connectionID, int WOID, int TaskID)
        {

        }




        public override System.Threading.Tasks.Task OnDisconnected(bool stopCalled)
        {
            isfObj.clientDisconnected(Context.ConnectionId);
            return base.OnDisconnected(stopCalled);
        }

        public override System.Threading.Tasks.Task OnConnected()
        {
            Clients.Client(Context.ConnectionId).ClientIdentity(Context.ConnectionId);

            return base.OnConnected();
        }
    }
}