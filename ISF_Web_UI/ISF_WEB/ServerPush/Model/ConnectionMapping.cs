using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Net.Http;
using System.Web;
using System.Net;
using System.IO;
using System.Configuration;
using ISF_WEB.Models;
using Newtonsoft.Json;
using ISF_WEB.ServerPush.Utility;
using ISF_WEB.Util;
using System.Collections.Concurrent;

namespace ISF_WEB.ServerPush.Model
{
    public class ConnectionMapping<T>
    {
        private readonly ConcurrentDictionary<T, List<string>> _connections = new ConcurrentDictionary<T, List<string>>();
        ConnectionUtil<T> conUtil = new ConnectionUtil<T>();

        public int Count
        {
            get
            {
                return _connections.Count;
            }
        }

        //public void Add(T key, string connectionId)
        //{
        //    if(_connections.TryAdd(key,connectionId)==false)
        //    _connections[key] = connectionId;
          
        //}

        //public void Add(T key, string connectionId)
        //{
        //    try
        //    {
        //        List<string> connections = new List<string>();

        //        if (_connections.TryGetValue(key, out connections))
        //        {
        //            connections = AddConnection(connectionId, connections);
        //            _connections[key] = connections;
        //        }
        //        else
        //        {
        //            connections = AddConnection(connectionId, connections);
        //            _connections.TryAdd(key, connections);
        //        }
        //    }
        //    catch(Exception ex)
        //    {

        //    }

        //}

        private static List<string> AddConnection(string connectionId, List<string> connections)
        {
            CreateLogs CreateLogsObj = CreateLogs.GetInstance(string.Empty);
            if (connections == null)
            {
                connections = new List<string>();
            }

            if (!connections.Contains(connectionId))
            {
                CreateLogsObj.WriteInformationLogs(string.Format("Connection has been added : {0}", connectionId));
                connections.Add(connectionId);
            }

            return connections;
        }


        public void Add(T key, string connectionId)
        {
            CreateLogs CreateLogsObj = CreateLogs.GetInstance(key);
            try
            {
                CreateLogsObj.WriteInformationLogs(string.Format("Connection is to be added is: {0}, Signum is: {1}", connectionId, key));
                List<string> connections = new List<string>();
                _connections.TryGetValue(key, out connections);
                connections= AddConnection(connectionId, connections);
                _connections.TryAdd(key, connections);
            }
            catch (Exception ex)
            {
                CreateLogsObj.WriteExceptionLogs(ex);
            }

        }




        public List<string> GetConnection(T key)
        {
            List<string> connectionId = new List<string>();
            CreateLogs CreateLogsObj = CreateLogs.GetInstance(string.Empty);

            CreateLogsObj.WriteInformationLogs("Get Connection Id in Dictionary Is  - Initiated");

            foreach (KeyValuePair<T, List<string>> kvp in _connections)
            {
                    CreateLogsObj.WriteInformationLogs(string.Format("Dictionary Data - {0}", string.Format("Key = {0}, Value = {1}", kvp.Key, kvp.Value)));
            }

            if (_connections.ContainsKey(key))
            {
                _connections.TryGetValue(key, out connectionId);
                CreateLogsObj.WriteInformationLogs(string.Format("Get Connection Id in Dictionary Is  - {0}", connectionId));

            }

            return connectionId;

        }

        public void RemoveAll(T key)
        {
            CreateLogs CreateLogsObj = CreateLogs.GetInstance(key);
            try
            {
                List<string> AllconnectionsPerSignum = new List<string>();
                foreach (var con in _connections)
                {
                    if (_connections.ContainsKey(key))
                    {
                        if (_connections.TryRemove(key, out AllconnectionsPerSignum))
                        {
                            CreateLogsObj.WriteInformationLogs(string.Format("Removed Signum - {0} from Dictionary of connection", key));
                        }
                        else
                        {
                            CreateLogsObj.WriteInformationLogs(string.Format("Couldn't remove Signum - {0} from Dictionary of connection", key));
                        }
                    }
                    else
                    {
                        break;
                    }
                }
            }
            catch(Exception ex)
            {
                CreateLogsObj.WriteExceptionLogs(ex);
            }
        }

        public void Remove(T key, string connectionId)
        {
            CreateLogs CreateLogsObj = CreateLogs.GetInstance(key);
            CreateLogsObj.WriteInformationLogs(string.Format("Connection Id to be removed is:  {0} ", connectionId));
            try
            {
               // lock (_connections)
                //{

                    foreach (var con in _connections)
                    {
                        if (!_connections.ContainsKey(key))
                        {
                            break;
                        }

                        List<string> connectionAsPerSignum = _connections[key];

                        bool IsRemoved = connectionAsPerSignum.Remove(connectionId);

                        if(connectionAsPerSignum.Count == 0)
                        {
                            if(_connections.TryRemove(key, out connectionAsPerSignum))
                            {
                                CreateLogsObj.WriteInformationLogs(string.Format("Removed Signum - {0} from Dictionary of connection", key));
                            }
                            else
                            {
                                connectionAsPerSignum.Remove(connectionId);
                                _connections[key] = connectionAsPerSignum;
                                CreateLogsObj.WriteInformationLogs(string.Format("Couldn't Removed Signum - {0} from Dictionary of connection", key));
                            }
                        }

                        if (IsRemoved)
                        {
                            CreateLogsObj.WriteInformationLogs(string.Format("Removed Connection Id in Dictionary Is  - {0}", connectionId));
                        }
                        else
                        {
                            CreateLogsObj.WriteInformationLogs(string.Format("Not Removed Connection Id in Dictionary Is - {0}", connectionId));
                        }

                    }
               // }

            }
            catch(Exception ex)
            {
                CreateLogsObj.WriteExceptionLogs(ex);
            }




            //    string connection;
            //    if (!_connections.TryGetValue(key, out connection))
            //    {
            //        return;
            //    }
            //    string fileName = string.Format("{0}_Web", key);

            //    if (_connections.TryRemove(key,out connectionId)){
            //        //Logs.WriteLogs("Removed Connection Id in Dictionary Is  - ", connectionId, fileName);
            //        CreateLogsObj.WriteInformationLogs(string.Format("Removed Connection Id in Dictionary Is  - {0}", connectionId));
            //    }
            //    else
            //    {
            //        //Logs.WriteLogs("Not Removed Connection Id in Dictionary Is  - ", connectionId, fileName);
            //        CreateLogsObj.WriteInformationLogs(string.Format("Not Removed Connection Id in Dictionary Is - {0}", connectionId));
            //    }

               
            //  //  conUtil.RemoveConnectionFromDB(key).Wait();


            //}
        }

        
    }
}