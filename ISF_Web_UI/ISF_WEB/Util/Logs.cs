using System;
using System.Collections.Generic;
using System.IO;
using System.Linq;
using System.Web;

namespace ISF_WEB.Util
{
    public static class Logs
    {
      static string folderName= AppDomain.CurrentDomain.BaseDirectory;
        static  Logs()
        {
         folderName = @"C:\IsfLogs";
        }
        public static void WriteExceptionLogs(Exception ex)
        {
            if (WebConfigDetail.IsLoggingEnabled)
            {
                string filePath = string.Format(@"{0}\Error.text", folderName);
                using (StreamWriter writer = new StreamWriter(filePath, true))
                {
                    writer.WriteLine("Message :" + ex.Message + "<br/>" + Environment.NewLine + "StackTrace :" + ex.StackTrace +
                       "" + Environment.NewLine + "Date :" + DateTime.Now.ToString());
                    writer.WriteLine(Environment.NewLine + "-----------------------------------------------------------------------------" + Environment.NewLine);
                }
            }
              
        }
        public static void WriteExceptionLogs(Exception ex, string fileName)
        {
            if (WebConfigDetail.IsLoggingEnabled)
            {
                string filePath = string.Format(@"{0}\{1}_Error.text", folderName, fileName);
                using (StreamWriter writer = new StreamWriter(filePath, true))
                {
                    writer.WriteLine("Message :" + ex.Message + "<br/>" + Environment.NewLine + "StackTrace :" + ex.StackTrace +
                       "" + Environment.NewLine + "Date :" + DateTime.Now.ToString());
                    writer.WriteLine(Environment.NewLine + "-----------------------------------------------------------------------------" + Environment.NewLine);
                }
            }

            
        }
        public static void WriteLogs(string msg, string methodName)
        {
            if (WebConfigDetail.IsLoggingEnabled)
            {
                string filePath = string.Format(@"{0}\Log.text", folderName);

                using (StreamWriter writer = new StreamWriter(filePath, true))
                {
                    writer.WriteLine("Message :" + methodName + "<br/>" + Environment.NewLine + "StackTrace :" + msg +
                       "" + Environment.NewLine + "Date :" + DateTime.Now.ToString());
                    writer.WriteLine(Environment.NewLine + "-----------------------------------------------------------------------------" + Environment.NewLine);
                }
            }

             

        }
        public static void WriteLogs(string msg, string methodName,string fileName)
        {

            if (WebConfigDetail.IsLoggingEnabled)
            {
                string filePath = string.Format(@"{0}\{1}_Log.text", folderName, fileName);

                using (StreamWriter writer = new StreamWriter(filePath, true))
                {
                    writer.WriteLine("Message :" + methodName + "<br/>" + Environment.NewLine + "StackTrace :" + msg +
                       "" + Environment.NewLine + "Date :" + DateTime.Now.ToString());
                    writer.WriteLine(Environment.NewLine + "-----------------------------------------------------------------------------" + Environment.NewLine);
                }
            }
           
          

        }
       
    }
}