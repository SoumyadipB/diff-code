using ISF.AzureRedisCache.Contracts;
using StackExchange.Redis;
using System;

namespace ISF.AzureRedisCache
{
	public  class AzureRedisCacheOperations : IAzureRedisCacheOperations
	{
		
		private static readonly IConnectionMultiplexer _redisConnection = ConnectionEstablishment.Instance.Connection;
		private static readonly IDatabase _dbCache = _redisConnection.GetDatabase();

		/// <summary>
		/// create lazy instance of AzureRedisCacheOperations.
		/// </summary>
		private static readonly Lazy<AzureRedisCacheOperations> lazyAzRedisCacheOperation = new Lazy<AzureRedisCacheOperations>(() =>
		{
			return new AzureRedisCacheOperations();
		});
		/// <summary>
		/// get Instance
		/// </summary>
		public static AzureRedisCacheOperations Instance
		{
			get
			{
				return lazyAzRedisCacheOperation.Value;
			}
		}


		public string GetValueByKey(string key)
		{
			string keyValue = string.Empty;
			if(_redisConnection.IsConnected)
			{
				keyValue = _dbCache.StringGet(key);
				
			}
			return keyValue;
		}

		public bool SaveValueByKey(string key, dynamic value, TimeSpan exprirationTime)
		{
			bool isSaved = false;
			if (_redisConnection.IsConnected)
			{
				isSaved = _dbCache.StringSet(key, value, exprirationTime);

			}
			return isSaved;
		}
        

		public bool SaveValueByKey(string key, dynamic value)
		{
			bool isSaved = false;
			if (_redisConnection.IsConnected)
			{
				isSaved = _dbCache.StringSet(key, value);

			}
			return isSaved;
		}

		public bool CheckKeyExistsOrNot(string key)
		{
			bool isExists = false;
			if (_redisConnection.IsConnected)
			{
				isExists = _dbCache.KeyExists(key);
			}
			return isExists;
		}

		public bool RemoveValueByKey(string key)
		{
			bool isDeleted = false;
			if (_redisConnection.IsConnected)
			{
				isDeleted = _dbCache.KeyDelete(key);

			}
			return isDeleted;
		}

		public void Clear()
		{
			var endpoints = _redisConnection.GetEndPoints(true);
			foreach (var endpoint in endpoints)
			{
				var server = _redisConnection.GetServer(endpoint);
				server.FlushAllDatabases();
			}
		}
	}
}
