using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;
using System.Data;
using System.Data.SqlClient;
using System.Configuration;

namespace ISF_WEB.ServerPush
{
    public class ISFConnectionClass
    {
        public void clientConnected(string ConnectionID, string clientType, string singumID, string clientPage)
        {
            connectionUpdate(ConnectionID, clientType, singumID, clientPage, 1);
        }

        public void clientDisconnected(string connectionID)
        {
            DataTable connectionInfoDT = new DataTable();

            connectionInfoDT = getConnectionInfo(connectionID);

            connectionUpdate(
                connectionInfoDT.Rows[0][1].ToString(), 
                connectionInfoDT.Rows[0][2].ToString(), 
                connectionInfoDT.Rows[0][3].ToString(), 
                connectionInfoDT.Rows[0][4].ToString(), 
                0);
        }

        private DataTable getConnectionInfo(string connectionID)
        {
            using (var connection = new SqlConnection(ConfigurationManager.ConnectionStrings["IsfWoConnection"].ConnectionString))
            {
                connection.Open();
                using (SqlCommand command = new SqlCommand(@"SELECT [ConnectionID]
                                                              ,[ClientConnectionID]
                                                              ,[ClientType]
                                                              ,[SignumID]
                                                              ,[ClientPage]
                                                              ,[IsConnected]
                                                              ,[TransactionOn]
                                                          FROM [transactionalData].[TBL_ISFConnections] where ClientConnectionID = '" + connectionID + "'", connection))
                {
                    if (connection.State == ConnectionState.Closed)
                        connection.Open();

                    DataSet connectionInfoDS = new DataSet();
                    SqlDataAdapter da = new SqlDataAdapter(command);
                    da.Fill(connectionInfoDS);

                    return connectionInfoDS.Tables[0];
                    

                }
            }
        }

        private void connectionUpdate(string ConnectionID, string clientType, string singumID, string clientPage, int IsConnected)
        {
            using (var connection = new SqlConnection(ConfigurationManager.ConnectionStrings["IsfWoConnection"].ConnectionString))
            {   
                string insertConnectedClient = @"insert into [transactionalData].[TBL_ISFConnections] ([ClientConnectionID]
                                               ,[ClientType]
                                               ,[SignumID]
                                               ,[ClientPage]
                                               ,[IsConnected]) values (@ClientConnectionID, @ClientType, @SignumID, @ClientPage, @IsConnected)";
                using (SqlCommand command = new SqlCommand(insertConnectedClient, connection))
                {
                    command.Parameters.AddWithValue("@ClientConnectionID", ConnectionID);
                    command.Parameters.AddWithValue("@ClientType", clientType);
                    command.Parameters.AddWithValue("@SignumID", singumID);
                    command.Parameters.AddWithValue("@ClientPage", clientPage);
                    command.Parameters.AddWithValue("@IsConnected", IsConnected);

                    try
                    {
                        connection.Open();
                        command.ExecuteNonQuery();
                    }
                    catch (SqlException)
                    {
                        // error here
                    }
                    finally
                    {
                        connection.Close();
                    }

                }

            }
        }
    }
}