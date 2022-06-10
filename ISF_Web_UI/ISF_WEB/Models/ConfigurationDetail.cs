using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;

namespace ISF_WEB.Models
{
    public class ConfigurationDetail
    {
        
        public dynamic JavaApiUrl { get; set; }
        public dynamic JavaApiVmUrl { get; set; }
        public dynamic downloadISFSetupUrl { get; set; }
        public dynamic downloadOutlookAddinUrl { get; set; }
        public dynamic botNestExternalUrl { get; set; }
        public dynamic UiRootDir { get; set; }
        public dynamic environment { get; set; }
        public dynamic AzureSignalRHubUrl { get; set; }
        public dynamic AppInsightsInstrumentationKey { get; set; }
        public dynamic gtagUrl { get; set; }
        public dynamic uploadWIFile { get; set; }
        
        public dynamic uploadNE { get; set; }
        public dynamic pathUrlNE { get; set;}
        
        public dynamic limitValueForDRAccess { get; set; }

        public dynamic MANUAL_STEP_EXE_DEFAULT_VALUE { get; set; }
        public dynamic AUTOMATIC_STEP_EXE_DEFAULT_VALUE { get; set; }
        public dynamic NO_OF_WO_NEID_DEFAULT_VALUE { get; set; }
        public dynamic manualAutoStepLimitValue { get; set; }
        public dynamic decisionStepLimitValue { get; set; }

    }
}