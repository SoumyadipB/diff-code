using ISF_WEB.ServerPush.Model;
using ISF_WEB.Util;
using Microsoft.Ajax.Utilities;
using Microsoft.AspNet.SignalR;
using Microsoft.AspNet.SignalR.Hubs;
using Newtonsoft.Json;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;

namespace ISF_WEB.ServerPush.SignalServer
{
    public class AutoSenseNotifications
    {
        private readonly static Lazy<AutoSenseNotifications> _instance = new Lazy<AutoSenseNotifications>(() => new AutoSenseNotifications(GlobalHost.ConnectionManager.GetHubContext<ServerPushHub>().Clients));
        private readonly static ConnectionMapping<string> _connections = new ConnectionMapping<string>();
        private readonly static DesktopAppUserConnectionMapping<string> _desktopConnections = new DesktopAppUserConnectionMapping<string>();

        #region Constructor and Property
        private AutoSenseNotifications(IHubConnectionContext<dynamic> clients)
        {
            Clients = clients;

        }

        public static AutoSenseNotifications Instance
        {
            get
            {
                return _instance.Value;
            }
        }

        private IHubConnectionContext<dynamic> Clients
        {
            get;
            set;
        }


        #endregion

        public string SetDecisionValue(string decisionValueDetails)
        {
            DecisionValueModel decisionValue = new DecisionValueModel();
            try
            {
                if (decisionValueDetails != null)
                {
                    decisionValue = JsonConvert.DeserializeObject<DecisionValueModel>(decisionValueDetails);
                }
                if (decisionValue != null && !string.IsNullOrEmpty(decisionValue.signumId))
                {
                    List<string> connectionIds = IsfWoData.Instance.GetConnections(decisionValue.signumId.ToUpper());
                    CreateLogs CreateLogsObj = CreateLogs.GetInstance(decisionValue.signumId);
                    CreateLogsObj.WriteSignalRInfoLogs("SetDecisionValue method is called");
                    CreateLogsObj.WriteSignalRInfoLogs(string.Format("json input received: {0}", decisionValueDetails));
                    CreateLogsObj.WriteSignalRInfoLogs(string.Format("Step Id: {0}  Decision Value: {1}", decisionValue.flowChartStepId, decisionValue.decisionValue));

                    if (connectionIds != null && connectionIds.Count > 0)
                    {
                        foreach (var con in connectionIds)
                        {
                            Clients.Client(con).setDecisionValue(decisionValue.flowChartStepId, decisionValue.decisionValue, decisionValue.woId);
                        }
                    }

                    return decisionValue.signumId;
                }
            }
            catch (Exception ex)
            {

            }

            return string.Empty;
        }

    }
}