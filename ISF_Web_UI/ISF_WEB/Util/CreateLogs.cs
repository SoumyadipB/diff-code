using Serilog;
using Serilog.Core;
using System;

namespace ISF_WEB.Util
{
	public sealed class CreateLogs
	{
		static readonly string fileName = DateTime.Now.ToString("dd-MM-yyyy");
		private static string _signum = string.Empty;
		private static readonly Lazy<CreateLogs> _instance = new Lazy<CreateLogs>(() => new CreateLogs());

		private static readonly ILogger apiTransactionLog = new LoggerConfiguration()
					  .MinimumLevel.ControlledBy(new LoggingLevelSwitch(Serilog.Events.LogEventLevel.Information))
					  .WriteTo.File(WebConfigDetail.LogDirectoryPath + fileName + ConstantKeywords.C_APITransactionLogFile,
							rollingInterval: RollingInterval.Day,
							fileSizeLimitBytes: long.MaxValue,
							rollOnFileSizeLimit: true, shared: true,
							retainedFileCountLimit: WebConfigDetail.RetainedFileCountLimit)
					  .CreateLogger();
		private static readonly ILogger exceptionLog = new LoggerConfiguration()
							  .MinimumLevel.ControlledBy(new LoggingLevelSwitch(Serilog.Events.LogEventLevel.Error))
							  .WriteTo.File(WebConfigDetail.LogDirectoryPath + fileName + ConstantKeywords.C_ExceptionLogFile,
							  rollingInterval: RollingInterval.Day,
							fileSizeLimitBytes: long.MaxValue,
							rollOnFileSizeLimit: true, shared: true,
							retainedFileCountLimit: WebConfigDetail.RetainedFileCountLimit)
					  .CreateLogger();

		private static readonly ILogger informationLog = new LoggerConfiguration()
			.MinimumLevel.ControlledBy(new LoggingLevelSwitch(Serilog.Events.LogEventLevel.Information))
						 .WriteTo.File(WebConfigDetail.LogDirectoryPath + fileName + ConstantKeywords.C_InformationLogFile,
							  rollingInterval: RollingInterval.Day,
							fileSizeLimitBytes: long.MaxValue,
							rollOnFileSizeLimit: true, shared: true,
							retainedFileCountLimit: WebConfigDetail.RetainedFileCountLimit)
					  .CreateLogger();

		private static readonly ILogger signalRInfoLogFile = new LoggerConfiguration()
			.MinimumLevel.ControlledBy(new LoggingLevelSwitch(Serilog.Events.LogEventLevel.Information))
						.WriteTo.File(WebConfigDetail.LogDirectoryPath + fileName + ConstantKeywords.C_SignalRInfoLogFile,
							 rollingInterval: RollingInterval.Day,
							fileSizeLimitBytes: long.MaxValue,
							rollOnFileSizeLimit: true, shared: true,
							retainedFileCountLimit: WebConfigDetail.RetainedFileCountLimit)
					  .CreateLogger();
		private static readonly ILogger signalRMessageTransaction = new LoggerConfiguration()
			.MinimumLevel.ControlledBy(new LoggingLevelSwitch(Serilog.Events.LogEventLevel.Information))
					   .WriteTo.File(WebConfigDetail.LogDirectoryPath + fileName + ConstantKeywords.C_SignalRMessageTransaction,
							rollingInterval: RollingInterval.Day,
							fileSizeLimitBytes: long.MaxValue,
							rollOnFileSizeLimit: true, shared: true,
							retainedFileCountLimit: WebConfigDetail.RetainedFileCountLimit)
					  .CreateLogger();
		private CreateLogs()
		{

		}



		public static CreateLogs GetInstance(dynamic signum)
		{
			if (signum == null)
			{
				_signum = string.Empty;
			}
			else
			{
				_signum = signum.ToString();
			}
			return _instance.Value;
		}

		//private CreateLogs(dynamic signum)
		//{

		//}

		public void WriteExceptionLogs(dynamic exception)
		{
			if (WebConfigDetail.IsFileLoggingEnabled)
			{
				exceptionLog.Error("WEB_UI Signum:" + _signum + " Error: => " + exception);
			}
		}
		public void WriteAPIRequestLogs(dynamic apiName, dynamic request)
		{
			if (WebConfigDetail.IsFileLoggingEnabled)
			{
				apiTransactionLog.Information("WEB_UI Signum:" + _signum + " " + apiName + " => Request: " + request);
			}
		}
		public void WriteAPIResponseLogs(dynamic apiName, dynamic response)
		{
			if (WebConfigDetail.IsFileLoggingEnabled)
			{
				apiTransactionLog.Information("WEB_UI Signum:" + _signum + " " + apiName + " => Response: " + response);
			}
		}
		public void WriteInformationLogs(dynamic message)
		{
			if (WebConfigDetail.IsFileLoggingEnabled)
			{
				informationLog.Information("WEB_UI Signum:" + _signum + " Information: " + message);
			}
		}

		public void WriteSignalRInfoLogs(dynamic message)
		{
			if (WebConfigDetail.IsFileLoggingEnabled)
			{
				signalRInfoLogFile.Information("WEB_UI Signum:" + _signum + " Information: " + message);
			}
		}

		public void WriteSignalRMessageTransactionLogs(bool acknowledgementReceived, string signum, string methodName, string connectionId, string client)
		{
			if (WebConfigDetail.IsFileLoggingEnabled)
			{
				signalRMessageTransaction.Information(string.Format("WEB_UI Signum: {0}  MessageAcknowlegment: {1}  ConnectionId: {2}  Client: {3}  MethodName: {4}", signum, acknowledgementReceived, connectionId, client, methodName));
			}
		}


	}
}