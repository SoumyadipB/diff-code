using System;
using System.Collections.Generic;
using System.Linq;
using System.Net.Http;
using System.Threading;
using System.Threading.Tasks;
using System.Web;
using System.Web.Http.Controllers;
using System.Web.Http.Filters;
using ISF_WEB.Util;
using Microsoft.Owin.Security;
using Microsoft.Owin.Security.OpenIdConnect;

namespace ISF_WEB.Filters
{
    public class AzureAdAuthenticationAttribute : Attribute, IAuthenticationFilter
    {
        public bool AllowMultiple => true;

        public async Task AuthenticateAsync(HttpAuthenticationContext context, CancellationToken cancellationToken)
        {
            var request = context.Request;

        }

        public Task ChallengeAsync(HttpAuthenticationChallengeContext context, CancellationToken cancellationToken)
        {
            Challenge(context);
            return Task.FromResult(0);
        }
        private void Challenge(HttpAuthenticationChallengeContext context)
        {
            SignIn();
        }

        private void SignIn()
        {
            if (WebConfigDetail.IsLoginModeAzureAd)
            {
                if (!HttpContext.Current.Request.IsAuthenticated)
                {

                    HttpContext.Current.GetOwinContext().Authentication.Challenge(
                        new AuthenticationProperties { RedirectUri = "/Login" },
                        OpenIdConnectAuthenticationDefaults.AuthenticationType);
                }

            }


        }
    }
}