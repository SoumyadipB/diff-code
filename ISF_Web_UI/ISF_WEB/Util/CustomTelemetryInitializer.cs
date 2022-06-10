//using Microsoft.ApplicationInsights.Channel;
//using Microsoft.ApplicationInsights.Extensibility;
//using System;
//using System.Collections.Generic;
//using System.Linq;
//using System.Web;

//namespace ISF_WEB.Util
//{
//	public class CustomTelemetryInitializer : ITelemetryInitializer
//	{
//        public void Initialize(ITelemetry telemetry)
//        {
//            if (string.IsNullOrEmpty(telemetry.Context.Cloud.RoleName))
//            {
//                //set custom role name here
//                telemetry.Context.Cloud.RoleName = "ISF WEB UI";
//                telemetry.Context.Cloud.RoleInstance = "ISF WEB UI";
//            }
//        }
//    }
//}