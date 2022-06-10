using ISF_WEB.Util;
using Microsoft.Owin;
using Owin;

[assembly: OwinStartup(typeof(ISF_WEB.Startup))]

namespace ISF_WEB
{
    public partial class Startup
    {
        public void Configuration(IAppBuilder app)
        {
          
           if(WebConfigDetail.IsLoginModeAzureAd)
            {
                ConfigureAzureAdAuth(app);
            }
            else
            {
                ConfigureAuth(app);
            }
            
            ISF_WEB.ServerPush.Startup.ConfigureSignalR(app);
        }
    }
}
