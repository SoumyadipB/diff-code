using Newtonsoft.Json;
using Newtonsoft.Json.Linq;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;

namespace ISF_WEB.Util
{
	public class JsonHelper
	{
		private readonly static Lazy<JsonHelper> _instance = new Lazy<JsonHelper>(() => new JsonHelper());
		private readonly static CreateLogs createLogsObj = CreateLogs.GetInstance(string.Empty);

		private JsonHelper() { }
		public static JsonHelper Instance
		{
			get
			{
				return _instance.Value;
			}
		}

		public bool IsValidJson(string inputString)
		{
			bool isValid;
			try
			{
				if (string.IsNullOrWhiteSpace(inputString))
				{
					isValid = false;
				}
				else
				{
					inputString = inputString.Trim();
					JToken.Parse(inputString);
					isValid = true;
				}

			}
			catch (JsonReaderException ex)
			{
				createLogsObj.WriteExceptionLogs(ex);
				isValid= false;
			}
			return isValid;
		}

	}
}