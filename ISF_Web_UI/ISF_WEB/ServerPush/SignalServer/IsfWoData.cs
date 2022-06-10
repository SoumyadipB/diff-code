using Microsoft.AspNet.SignalR;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;
using Microsoft.AspNet.SignalR.Hubs;
using System.Data;
using System.Data.SqlClient;
using System.Configuration;
using ISF_WEB.ServerPush.Model;
using ISF_WEB.Util;
using ISF_WEB.Models;

namespace ISF_WEB.ServerPush
{
    public class IsfWoData
    {
        #region Global Private Variables

        // Singleton instance
        private readonly static Lazy<IsfWoData> _instance = new Lazy<IsfWoData>(() => new IsfWoData(GlobalHost.ConnectionManager.GetHubContext<ServerPushHub>().Clients));
        private readonly string conStr = ConfigurationManager.ConnectionStrings["IsfWoConnection"].ConnectionString;

        private readonly static ConnectionMapping<string> _connections = new ConnectionMapping<string>();
        private readonly static DesktopAppUserConnectionMapping<string> _desktopConnections = new DesktopAppUserConnectionMapping<string>();
        private static Dictionary<string, int> lastStatusCountList = new Dictionary<string, int>();
        #endregion


        #region Constructor and Property
        private IsfWoData(IHubConnectionContext<dynamic> clients)
        {
            Clients = clients;

        }

        public static IsfWoData Instance
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


        #region Connection For Signal R Clinet 


        // Below Code For managing  connection with Web User --  START
        public void MakeConnection(string signumId, string connectionId)
        {
            string fileName = string.Format("{0}_Web_SignalR", signumId);
            CreateLogs CreateLogsObj = CreateLogs.GetInstance(signumId);
            //CreateLogs.WriteLogs("Added Connection Id Is  - ", connectionId, fileName);
            CreateLogsObj.WriteInformationLogs(string.Format("Added Connection Id Is  - {0}", connectionId));
            try
            {
                //  _connections.Remove(signumId.ToUpper(), connectionId);
                _connections.Add(signumId.ToUpper(), connectionId);
            }
            catch (Exception ex)
            {
                // CreateLogs.WriteLogs("Exception  Is  - ", ex.Message.ToString(), fileName);
                CreateLogsObj.WriteExceptionLogs(ex);
            }
        }

        public void RemoveConnection(string signumId, string connectionId)
        {
            _connections.Remove(signumId.ToUpper(), connectionId);
        }

        public void RemoveAllConnection(string signumId)
        {
            _connections.RemoveAll(signumId.ToUpper());
        }



        // Below Code For managing  connection with Desktop User --  START
        public void MakeDesktopConnection(string signumId, string connectionId)
        {
            CreateLogs CreateLogsObj = CreateLogs.GetInstance(signumId);
            //string fileName = string.Format("{0}_Desktop", signumId);
            //CreateLogs.WriteLogs("Added Connection Id Is  - ", connectionId, fileName);
            CreateLogsObj.WriteInformationLogs(string.Format("Added Desktop Connection Id Is  -  {0}", connectionId));
            try
            {
                _desktopConnections.Remove(signumId.ToUpper(), connectionId);
                _desktopConnections.Add(signumId.ToUpper(), connectionId);
            }
            catch (Exception ex)
            {
                CreateLogsObj.WriteExceptionLogs(ex);
                //CreateLogs.WriteLogs("Exception  Is  - ", ex.Message.ToString(), fileName);
            }


        }
        public void RemoveDesktopConnection(string signumId, string connectionId)
        {
            _desktopConnections.Remove(signumId.ToUpper(), connectionId);
        }



        #endregion

        #region Private Methods

        private void UpdateAllClient(DataTable dt)
        {
            Clients.All.updateWorkOrder(dt);

        }

        private void UpdateClientByConnectionId(WoAutoTaskInfo woAutoRunningTaskStatus)
        {
            if (woAutoRunningTaskStatus != null && !string.IsNullOrEmpty(woAutoRunningTaskStatus.Signum))
            {
                List<string> connectionIds = _connections.GetConnection(woAutoRunningTaskStatus.Signum.ToUpper());
                CreateLogs CreateLogsObj = CreateLogs.GetInstance(woAutoRunningTaskStatus.Signum);

                CreateLogsObj.WriteInformationLogs(string.Format("Signum is: {0} Get Connection Id Is  - {1}", woAutoRunningTaskStatus.Signum.ToUpper(), connectionIds));

                if (connectionIds.Count > 0)
                {
                    foreach (var con in connectionIds)
                    {
                        Clients.Client(con).updateInprogressTasks(woAutoRunningTaskStatus);
                        CreateLogsObj.WriteInformationLogs(string.Format("Updated to client method updateInprogressTasks with connection id -  {0} for Booking Id - {1}", con, woAutoRunningTaskStatus.BookingId));

                    }
                }
            }
        }

        private void UpdateAllDesktopClientByConnectionId(string changeType, WoAutoRunningTaskStatus woAutoRunningTaskStatus)
        {
            CreateLogs CreateLogsObj = CreateLogs.GetInstance(woAutoRunningTaskStatus.Signum);

            try
            {

                string fileName = string.Format("{0}_Desktop", woAutoRunningTaskStatus.Signum);
                string connectionId = GetDesktopClientConnection(woAutoRunningTaskStatus.Signum.ToUpper());
                CreateLogsObj.WriteInformationLogs(string.Format("Connection Id Is  -  {0}", connectionId));
                //CreateLogs.WriteLogs("Connection Id Is  - ", connectionId, fileName);
                if (String.IsNullOrWhiteSpace(connectionId) == false)
                {
                    Clients.Client(connectionId).newChangeDetected(changeType);

                    //CreateLogs.WriteLogs(string.Format("Updated desktop client for client ID - {0}", connectionId), changeType, fileName);
                    CreateLogsObj.WriteInformationLogs(string.Format("Updated desktop client for client ID - {0}", connectionId));

                }
            }
            catch (Exception ex)
            {
                // string fileName = string.Format("{0}_Desktop", woAutoRunningTaskStatus.Signum);

                //CreateLogs.WriteExceptionLogs(ex,fileName);
                CreateLogsObj.WriteExceptionLogs(ex);

            }


        }

        private void AuditDetailUserCommentByConnectionId(AuditDetailUserCommentWoDetail _auditDetailUserCommentWoDetail)
        {
            CreateLogs CreateLogsObj = CreateLogs.GetInstance(_auditDetailUserCommentWoDetail.SignumId.ToUpper());
            List<string> connectionIds = _connections.GetConnection(_auditDetailUserCommentWoDetail.SignumId.ToUpper());
            if (connectionIds.Count > 0)
            {
                foreach (var con in connectionIds)
                {
                    Clients.Client(con).updateMyWorkAuditDetailUserComment(_auditDetailUserCommentWoDetail);
                    CreateLogsObj.WriteInformationLogs(string.Format("Updated to client method updateMyWorkAuditDetailUserComment with connection id -  {0}", con));
                }
            }

        }
        private void UpdateClientByConnectionIdForAdhoc(AdhocTaskStatus adhocTaskStatus)
        {
            List<string> connectionIds = _connections.GetConnection(adhocTaskStatus.SignumId.ToUpper());
            CreateLogs CreateLogsObj = CreateLogs.GetInstance(adhocTaskStatus.SignumId);

            if (connectionIds.Count > 0)
            {
                foreach (var con in connectionIds)
                {
                    CreateLogsObj.WriteInformationLogs(string.Format("Get Connection Id Is  - {0}", con));
                    Clients.Client(con).updateadhoctasks(adhocTaskStatus);
                    CreateLogsObj.WriteInformationLogs(string.Format("Updated to client  -  {0} , method: UpdateClientByConnectionIdForAdhoc", con));
                }
            }
        }


        #endregion

        #region Method to Expose Outside world

        public void UpdateAllDesktopClient(string changeType)
        {
            Clients.All.newChangeDetected(changeType);

        }

        public void UpdateAllFloatingClientByConnectionId(string signum, bool IsLogout)
        {
            CreateLogs CreateLogsObj = CreateLogs.GetInstance(signum);

            try
            {
                string connectionId = GetDesktopClientConnection(signum);

                CreateLogsObj.WriteInformationLogs(string.Format("Floating Window Connection Id Is  -  {0}", connectionId));

                if (String.IsNullOrWhiteSpace(connectionId) == false)
                {
                    if (IsLogout)
                    {
                        Clients.Client(connectionId).LogoutFloatingWindow(signum);
                    }
                    else
                    {
                        Clients.Client(connectionId).UpdateFloatingWindow(signum);
                    }

                    CreateLogsObj.WriteInformationLogs(string.Format("Updated Floating Window client for client ID - {0}", connectionId));

                }
            }
            catch (Exception ex)
            {
                CreateLogsObj.WriteExceptionLogs(ex);
            }


        }

        public void UpdateFloatingWindowOnRuleSesningStart(string signum)
        {
            CreateLogs CreateLogsObj = CreateLogs.GetInstance(signum);

            try
            {
                string connectionId = GetDesktopClientConnection(signum);

                CreateLogsObj.WriteInformationLogs(string.Format("Floating Window Connection Id Is  -  {0}", connectionId));

                if (String.IsNullOrWhiteSpace(connectionId) == false)
                {
                    Clients.Client(connectionId).UpdateFloatingWindowForSensingStart(signum);
                    CreateLogsObj.WriteInformationLogs(string.Format("UpdateFloatingWindowOnRuleSesningStart method is called and Updated Floating Window client for client ID - {0}", connectionId));

                }
            }
            catch (Exception ex)
            {
                CreateLogsObj.WriteExceptionLogs(ex);
            }
        }

        public void UpdateFloatingWindowOnRuleSesningStop(string signum)
        {
            CreateLogs CreateLogsObj = CreateLogs.GetInstance(signum);

            try
            {
                string connectionId = GetDesktopClientConnection(signum);

                CreateLogsObj.WriteInformationLogs(string.Format("Floating Window Connection Id Is  -  {0}", connectionId));

                if (String.IsNullOrWhiteSpace(connectionId) == false)
                {
                    Clients.Client(connectionId).UpdateFloatingWindowForSensingStop(signum);
                    CreateLogsObj.WriteInformationLogs(string.Format("UpdateFloatingWindowOnRuleSesningStop method is called and Updated Floating Window client for client ID - {0}", connectionId));
                }


            }
            catch (Exception ex)
            {
                CreateLogsObj.WriteExceptionLogs(ex);
            }
        }

        public void CheckInProgressTaskStatus(List<WoAutoTaskInfo> listWoAutoRunningTaskStatus)
        {
            CreateLogs CreateLogsObj = CreateLogs.GetInstance(listWoAutoRunningTaskStatus[0].Signum);

            // UpdateAllDesktopClient("AutoTask");
            foreach (var connectedUser in listWoAutoRunningTaskStatus)
            {
                UpdateClientByConnectionId(connectedUser);
                CreateLogsObj.WriteInformationLogs("Signum: " + connectedUser.Signum + ", Booking Id: " + connectedUser.BookingId + ", Status: " + connectedUser.Status + ": Connection with user is up and running. ");
                //UpdateAllDesktopClientByConnectionId("AutoTask", connectedUser);
            }
        }


        public void CheckAdhocTaskStatus(List<AdhocTaskStatus> listAdhocTaskStatus)
        {
            CreateLogs CreateLogsObj = CreateLogs.GetInstance(listAdhocTaskStatus[0].SignumId);


            foreach (var connectedUser in listAdhocTaskStatus)
            {
                UpdateClientByConnectionIdForAdhoc(connectedUser);
                CreateLogsObj.WriteInformationLogs("Signum: " + connectedUser.SignumId);
            }
        }

        public void CheckAuditDetailUserCommentConnection(List<AuditDetailUserCommentWoDetail> listAuditDetailUserCommentWoDetail)
        {
            CreateLogs CreateLogsObj = CreateLogs.GetInstance(listAuditDetailUserCommentWoDetail[0].SignumId);

            foreach (var connectedUser in listAuditDetailUserCommentWoDetail)
            {
                AuditDetailUserCommentByConnectionId(connectedUser);
                CreateLogsObj.WriteInformationLogs("Signum: " + connectedUser.SignumId + ": Process of Audit of User details is up and runnig.");
            }
        }

        public void UpdateClientByConnectionIdForWorkOrderCompletion(WorkOrderCompletionData workOrderCompletionData)
        {
            List<string> connectionIds = _connections.GetConnection(workOrderCompletionData.Signum.ToUpper());
            CreateLogs CreateLogsObj = CreateLogs.GetInstance(workOrderCompletionData.Signum);

            if (connectionIds.Count > 0)
            {
                foreach (var con in connectionIds)
                {
                    CreateLogsObj.WriteInformationLogs(string.Format("Get Connection Id Is  - {0}", con));
                    Clients.Client(con).updateForWorkOrderCompletion(workOrderCompletionData);
                    CreateLogsObj.WriteInformationLogs(string.Format("Updated to client  -  {0} , method: UpdateClientByConnectionIdForWorkOrderCompletion", con));
                }
            }
        }


        public void PauseAllWorkOrders(string signum, string connectionId)
        {
            List<string> connectionIds = _connections.GetConnection(signum.ToUpper());
            CreateLogs CreateLogsObj = CreateLogs.GetInstance(signum);

            if (connectionIds.Count > 0)
            {
                foreach (var con in connectionIds)
                {
                    if (con != connectionId)
                    {
                        CreateLogsObj.WriteInformationLogs(string.Format("Get Connection Id Is  - {0}", con));
                        Clients.Client(con).updateForPauseAllTasks();
                        CreateLogsObj.WriteInformationLogs(string.Format("Updated to client  -  {0} , method: UpdateClientByConnectionIdForWorkOrderCompletion", con));
                    }
                }
            }
        }


        public void UpdateWebUI(string signum, string connectionId)
        {
            List<string> connectionIds = _connections.GetConnection(signum.ToUpper());
            CreateLogs CreateLogsObj = CreateLogs.GetInstance(signum);

            if (connectionIds.Count > 0)
            {
                foreach (var con in connectionIds)
                {
                    if (con != connectionId)
                    {
                        CreateLogsObj.WriteInformationLogs(string.Format("Get Connection Id Is  - {0}", con));
                        Clients.Client(con).updateInprogressTaskPanelUI();
                        CreateLogsObj.WriteInformationLogs(string.Format("Updated to client  -  {0} , method: UpdateWebUI", con));
                    }
                }
            }
        }

        public void UpdateWebUI(dynamic stepDetailsData, string signum, string connectionId)
        {
            List<string> connectionIds = _connections.GetConnection(signum.ToUpper());
            CreateLogs CreateLogsObj = CreateLogs.GetInstance(signum);

            if (connectionIds.Count > 0)
            {
                foreach (var con in connectionIds)
                {
                    if (con != connectionId)
                    {
                        CreateLogsObj.WriteInformationLogs(string.Format("Get Connection Id Is  - {0}", con));
                        Clients.Client(con).updateInprogressTaskPanelUI(stepDetailsData);
                        CreateLogsObj.WriteInformationLogs(string.Format("Updated to client  -  {0} , method: UpdateWebUI", con));
                    }
                }
            }
        }

        public void UpdateWebUIAfterAutoScan(dynamic stepDetailsData, string signum)
        {
            List<string> connectionIds = _connections.GetConnection(signum.ToUpper());
            CreateLogs CreateLogsObj = CreateLogs.GetInstance(signum);

            if (connectionIds.Count > 0)
            {
                foreach (var con in connectionIds)
                {
                    CreateLogsObj.WriteInformationLogs(string.Format("Connection Id for UpdateWebUIAfterAutoScan method Is  - {0}", con));
                    Clients.Client(con).refreshWebUIAfterAutoScan(stepDetailsData);
                    CreateLogsObj.WriteInformationLogs(string.Format("Updated to client  -  {0} , method: UpdateWebUIAfterAutoScan", con));
                }
            }
        }

        public void UpdateAdhocTimerOnMultipleTabs(dynamic data, string signum, string connectionId, bool IsAdhocStarted)
        {
            List<string> connectionIds = _connections.GetConnection(signum.ToUpper());
            CreateLogs CreateLogsObj = CreateLogs.GetInstance(signum);

            if (connectionIds.Count > 0)
            {
                foreach (var con in connectionIds)
                {
                    if (con != connectionId)
                    {
                        CreateLogsObj.WriteInformationLogs(string.Format("Get Connection Id Is  - {0}", con));
                        if (IsAdhocStarted)
                        {
                            Clients.Client(con).StartAdhocTimerOnWebUI(data);
                        }
                        else
                        {
                            AdhocTaskStatus SignumData = new AdhocTaskStatus { SignumId = signum };
                            Clients.Client(con).updateadhoctasks(SignumData);
                        }
                        CreateLogsObj.WriteInformationLogs(string.Format("Updated to client  -  {0} , method: UpdateAdhocTimerOnMultipleTabs", con));
                    }
                }
            }
        }

        // To provide Auto Sense Enabled WO Details to FW Client 

        public void AutoSenseWoDetailForFloatingWindowClient(AutoSenseWorkOrderData autoSenseWoDetail)
        {
            CreateLogs CreateLogsObj = CreateLogs.GetInstance(autoSenseWoDetail?.signumID);
            CreateLogsObj.WriteSignalRInfoLogs("Hub method AutoSenseWoDetailForFloatingWindowClient is called");
            try
            {
                string connectionId = GetDesktopClientConnection(autoSenseWoDetail?.signumID);
                CreateLogsObj.WriteSignalRInfoLogs(string.Format("Floating Window Connection Id Is  -  {0}", connectionId));
                if (String.IsNullOrWhiteSpace(connectionId) == false)
                {
                    //client method that would be notified
                    CreateLogsObj.WriteSignalRInfoLogs("Floating Window client method 'AutoSenseEnabledWODetail' is called");
                    Clients.Client(connectionId).AutoSenseEnabledWODetail(autoSenseWoDetail);
                    CreateLogsObj.WriteSignalRInfoLogs(string.Format("Provide Def Id For Auto Sense Enabled Work order to Floating Window client for client ID - {0}", connectionId));
                }
            }
            catch (Exception ex)
            {
                CreateLogsObj.WriteExceptionLogs(ex);
            }
        }



        private string GetDesktopClientConnection(string signum)
        {
            return _desktopConnections.GetConnection(signum.ToUpper());
        }

        // To start auto sense Enabled process WO Details to FW Client 

        public void StartAutoScannerProcessForFloatingWindowClient(List<AutoSenseWorkOrderData> autoSenseWoDetail)
        {
            CreateLogs CreateLogsObj = CreateLogs.GetInstance(autoSenseWoDetail?[0].signumID);

            try
            {
                string connectionId = GetDesktopClientConnection(autoSenseWoDetail?[0].signumID);

                CreateLogsObj.WriteInformationLogs(string.Format("Start method StartAutoScannerProcessForFloatingWindowClient with Floating Window Connection Id Is -{0}", connectionId));
                if (String.IsNullOrWhiteSpace(connectionId) == false)
                {
                    //client method that would be notified
                    Clients.Client(connectionId).StartAutoScannerWODetail(autoSenseWoDetail);
                    CreateLogsObj.WriteInformationLogs(string.Format("END method StartAutoScannerProcessForFloatingWindowClientProvide Def Id For Auto Sense Enabled Work order to Floating Window client for client ID - {0}", connectionId));
                }
            }
            catch (Exception ex)
            {
                CreateLogsObj.WriteExceptionLogs(ex);
            }
        }

        // To provide Auto Sense Enabled WO Details List to FW Client  in case of mass transfer i.e. OverrideAction =Suspend
        public void AutoSenseWoDetailListForFloatingWindowClient(List<AutoSenseWorkOrderData> autoSenseWoDetail)
        {
            CreateLogs CreateLogsObj = CreateLogs.GetInstance(autoSenseWoDetail?[0].signumID);
            CreateLogsObj.WriteSignalRInfoLogs("Hub method AutoSenseWoDetailForFloatingWindowClient is called");
            try
            {
                string connectionId = GetDesktopClientConnection(autoSenseWoDetail?[0].signumID);
                CreateLogsObj.WriteSignalRInfoLogs(string.Format("Floating Window Connection Id Is  -  {0}", connectionId));
                if (String.IsNullOrWhiteSpace(connectionId) == false)
                {
                    //client method that would be notified
                    CreateLogsObj.WriteSignalRInfoLogs("Floating Window client method 'AutoSenseEnabledWODetail' is called");
                    Clients.Client(connectionId).AutoSenseEnabledWODetailListForMassSuspend(autoSenseWoDetail);
                    CreateLogsObj.WriteSignalRInfoLogs(string.Format("Provide Def Id For Auto Sense Enabled Work order to Floating Window client for client ID - {0}", connectionId));
                }
            }
            catch (Exception ex)
            {
                CreateLogsObj.WriteExceptionLogs(ex);
            }
        }

        public List<string> GetConnections(string signum)
        {
            return _connections.GetConnection(signum.ToUpper());
        }

        #endregion
    }
}