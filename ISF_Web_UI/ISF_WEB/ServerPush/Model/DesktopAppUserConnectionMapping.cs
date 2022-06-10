using ISF_WEB.Util;
using System;
using System.Collections.Concurrent;
using System.Collections.Generic;
using System.Linq;
using System.Web;

namespace ISF_WEB.ServerPush.Model
{
    public class DesktopAppUserConnectionMapping<T>
    {
        private readonly ConcurrentDictionary<T, string> _connections = new ConcurrentDictionary<T, string>();
        CreateLogs createLogs = CreateLogs.GetInstance(string.Empty);

        public int Count
        {
            get
            {
                return _connections.Count;
            }
        }

        public void Add(T key, string connectionId)
        {
            try
            {
                if (_connections.TryAdd(key, connectionId) == false)
                    _connections[key] = connectionId;
            }
            catch(Exception ex)
            {
                createLogs.WriteExceptionLogs(ex);
            }
        }

        public string GetConnection(T key)
        {
            string connectionId = string.Empty;
            try
            {
                if (_connections.TryGetValue(key, out connectionId))
                {
                    return connectionId;
                }
            }
            catch (Exception ex)
            {
                createLogs.WriteExceptionLogs(ex);
            }

            return connectionId;
        }

        //public void Remove(T key, string connectionId)
        //{
        //    lock (_connections)
        //    {
        //        string connection;
        //        if (!_connections.TryGetValue(key, out connection))
        //        {
        //            return;
        //        }
        //        _connections.TryRemove(key);

        //    }
        //}

        public void Remove(T key, string connectionId)
        {
            CreateLogs CreateLogsObj = CreateLogs.GetInstance(string.Empty);
            lock (_connections)
            {
                string connection;
                if (!_connections.TryGetValue(key, out connection))
                {
                    return;
                }
                string fileName = string.Format("{0}_Web", key);

                if (_connections.TryRemove(key, out connectionId))
                {
                    //Logs.WriteLogs("Removed Connection Id in Dictionary Is  - ", connectionId, fileName);
                    CreateLogsObj.WriteInformationLogs(string.Format("Removed Connection Id in Dictionary Is  - {0}", connectionId));
                }
                else
                {
                    //Logs.WriteLogs("Not Removed Connection Id in Dictionary Is  - ", connectionId, fileName);
                    CreateLogsObj.WriteInformationLogs(string.Format("Not Removed Connection Id in Dictionary Is - {0}", connectionId));
                }


                //  conUtil.RemoveConnectionFromDB(key).Wait();


            }
        }
    }
}