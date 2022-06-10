using ISF.AzureRedisCache.Configuration;
using StackExchange.Redis;
using System;
using System.Collections.Generic;
using System.IO;
using System.Linq;

namespace ISF.AzureRedisCache
{
    public class ConnectionEstablishment
    {
        private static List<Lazy<ConnectionMultiplexer>> lazyConnection = null;
        private static readonly Object connectionPoolLock = new Object();
        private static readonly Lazy<ConnectionEstablishment> lazyAzRedisCacheOperation = new Lazy<ConnectionEstablishment>(() =>
        {
            InitConnectionPool();
            return new ConnectionEstablishment();
        });

        public static ConnectionEstablishment Instance
        {
            get
            {
                return lazyAzRedisCacheOperation.Value;
            }
        }

        /// <summary>
        /// InitConnectionPool.
        /// </summary>
        private static void InitConnectionPool()
        {
           int _poolSize = AzureRedisCacheConfiguration.PoolSize;
            lock (connectionPoolLock)
            {
                if (lazyConnection == null)
                {
                    lazyConnection = new List<Lazy<ConnectionMultiplexer>>();
                }


                for (int i = 0; i < _poolSize && lazyConnection.Count <= _poolSize; i++)
                {
                    lazyConnection.Add(new Lazy<ConnectionMultiplexer>(() =>
                    {
                            Options = ConfigurationOptions.Parse(AzureRedisCacheConfiguration.ConnectionString);
                            Options.SyncTimeout = AzureRedisCacheConfiguration.SyncTimeout;
                            Options.AllowAdmin = true;

                        ConnectionMultiplexer redis;

                        using (var logger = new StringWriter())
                        {
                            redis = ConnectionMultiplexer.Connect(Options, logger);
                            Console.WriteLine(logger.ToString());
                        }

                        redis.InternalError += Redis_InternalError;
                        redis.ConnectionFailed += Redis_ConnectionFailed;
                        redis.ConnectionRestored += Redis_ConnectionRestored;
                        redis.ErrorMessage += Redis_ErrorMessage;
                        redis.ConfigurationChanged += Redis_ConfigurationChanged;

                        return redis;
                    }));
                }
            }
        }

        private static ConnectionMultiplexer GetLeastLoadedConnection()
        {
            //choose the least loaded connection from the pool
            var minValue = lazyConnection.Min((lazyConnect) => lazyConnect.Value.GetCounters().TotalOutstanding);
            var lazyContext = lazyConnection.First((lazyConnect) => lazyConnect.Value.GetCounters().TotalOutstanding == minValue);
            lazyContext.Value.IncludeDetailInExceptions = true;
            lazyContext.Value.IncludePerformanceCountersInExceptions = true;
            return lazyContext.Value;
        }

        public ConnectionMultiplexer Connection
        {
            get
            {
                lock (connectionPoolLock)
                {
                    return GetLeastLoadedConnection();
                }
            }
        }
        public static ConfigurationOptions Options { get; private set; }

        #region redis Events captured.
        private static void Redis_InternalError(object sender, InternalErrorEventArgs e)
        {

            Console.WriteLine($"Redis Internal Error: {e.Exception.Message}");
        }

        private static void Redis_ConnectionFailed(object sender, ConnectionFailedEventArgs e)
        {
            Console.WriteLine(
                "Endpoint failed: " + e.EndPoint + ", " + e.FailureType +
                (e.Exception == null ? "" : ", " + e.Exception.Message));
        }

        private static void Redis_ConnectionRestored(object sender, ConnectionFailedEventArgs e)
        {
            Console.WriteLine("Endpoint restored: " + e.EndPoint);
        }

        private static void Redis_ErrorMessage(object sender, RedisErrorEventArgs e)
        {
            Console.WriteLine(e.EndPoint + ": " + e.Message);
        }

        private static void Redis_ConfigurationChanged(object sender, EndPointEventArgs e)
        {
            Console.WriteLine("Configuration changed: " + e.EndPoint);
        }
        #endregion

    }
}
