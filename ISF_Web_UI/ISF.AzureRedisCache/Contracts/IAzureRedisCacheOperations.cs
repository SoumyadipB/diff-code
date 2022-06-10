using System;

namespace ISF.AzureRedisCache.Contracts
{
	interface IAzureRedisCacheOperations
	{
		// Get value of given key from redis cache
		string GetValueByKey(string key);

		// Save value and key of given key in redis cache with expiration time
		bool SaveValueByKey(string key, dynamic value, TimeSpan exprirationTime);

		// Save value and key of given key redis cache without expiration time
		bool SaveValueByKey(string key, dynamic value);


		/// <summary>
		/// CheckKeyExistOrNot method use to check key exist or not.
		/// </summary>
		/// <param name="key"></param>
		/// <returns></returns>
		bool CheckKeyExistsOrNot(string key);
		/// <summary>
		/// RemoveValueByKey method used to remove value from redis cache  using key.
		/// </summary>
		/// <param name="key"></param>
		/// <returns></returns>
		bool RemoveValueByKey(string key);

	}
}
