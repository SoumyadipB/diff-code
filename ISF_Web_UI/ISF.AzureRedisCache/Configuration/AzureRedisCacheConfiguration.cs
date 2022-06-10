namespace ISF.AzureRedisCache.Configuration
{
	public static class AzureRedisCacheConfiguration
	{
		

		/// <summary>
		/// get/set redis connection string
		/// </summary>
		public static string ConnectionString { get; set; }

		/// <summary>
		/// get/set redis PoolSize.
		/// </summary>
		public static int PoolSize { get; set; }


		/// <summary>
		/// get/set redis SyncTimeout.
		/// </summary>
		public static int SyncTimeout { get; set; }

		/// <summary>
		/// get/set redis connection number of retry.
		/// </summary>
		public static int ConnectRetry { get; set; }

		/// <summary>
		/// get/set redis connection after how many second retry for reconnect.
		/// </summary>
		public static int ReconnectRetry { get; set; }

		/// <summary>
		/// get/set check connection validity in defined seconds.
		/// </summary>
		public static int KeepAlive { get; set; }
		


	}
}