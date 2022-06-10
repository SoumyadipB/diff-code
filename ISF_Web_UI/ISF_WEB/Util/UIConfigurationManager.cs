using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;
using System.Web.Configuration;

namespace ISF_WEB.Util
{
    public class UIConfigurationManager
    {

        

        public string GetEnvironmnetName()
        {
            string envName = String.Empty;
            envName = System.Configuration.ConfigurationManager.AppSettings["AppEnvironment"];
            
            if (envName == null || envName == "")
            {
                envName = "";
            }
            return envName;
        }

        public void  SetExternalConfigFileName(string envName)
        {
            string FilePathForConfig = String.Empty;

            switch (envName){
                case "DevMajor":
                    FilePathForConfig = WebConfigUrlConstant.C_DevMajorFilePath;
                    break;
                case "DevMinor":
                    FilePathForConfig = WebConfigUrlConstant.C_DevMinorFilePath;
                    break;
                case "SitMajor":
                    FilePathForConfig = WebConfigUrlConstant.C_SitMajorFilePath;
                    break;
                case "SitMinor":
                    FilePathForConfig = WebConfigUrlConstant.C_SitMinorFilePath;
                    break;
                case "Sandbox1":
                    FilePathForConfig = WebConfigUrlConstant.C_SitMajorSandboxFilePath;
                    break;
                case "Sandbox2":
                    FilePathForConfig = WebConfigUrlConstant.C_SitMinorSandboxFilePath;
                    break;
                case "PerfTest":
                    FilePathForConfig = WebConfigUrlConstant.C_PerfTestFilePath;
                    break;
                case "Preprod":
                    FilePathForConfig = WebConfigUrlConstant.C_PreprodFilePath;
                    break;
                case "DryRun":
                    FilePathForConfig = WebConfigUrlConstant.C_DryRunPath;
                    break;
                case "Prod":
                    FilePathForConfig = WebConfigUrlConstant.C_ProdPath;
                    break;
                case "DevMajorS1":
                    FilePathForConfig = WebConfigUrlConstant.C_DevMajorS1FilePath;
                    break;
                default:
                    FilePathForConfig = WebConfigUrlConstant.C_LocalPath;
                    break;
                }
            
            var webConfigFileName = String.Format( "{0}/{1}",WebConfigUrlConstant.C_EnvSettingLocation, FilePathForConfig);
            var configuration = WebConfigurationManager.OpenWebConfiguration("~");
            configuration.AppSettings.File = webConfigFileName;
            configuration.Save();
           
        }

    }
}