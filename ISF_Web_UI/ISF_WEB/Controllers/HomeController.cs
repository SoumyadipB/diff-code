using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;
using System.Web;
using System.Web.Mvc;
using ISF.AzureRedisCache;
using ISF_WEB.Filters;
using ISF_WEB.Sessions;
using ISF_WEB.Util;
using Microsoft.Owin.Security;
using Microsoft.Owin.Security.OpenIdConnect;

namespace ISF_WEB.Controllers
{
    [HandleError]

    [SessionManager(RoleCheckRequired = false)]
    public class HomeController : Controller
    {
        public async Task<ActionResult> Index()
        {
            SessionData sessionData = new SessionData();
            string referrerController = string.Empty;
            string referrerAction = string.Empty;
            referrerController = (Session[ConstantKeywords.C_ReferrerController] != null ?
                Session[ConstantKeywords.C_ReferrerController].ToString() : "");
            referrerAction = (Session[ConstantKeywords.C_ReferrerAction] != null ?
                Session[ConstantKeywords.C_ReferrerAction].ToString() : "");
            if (string.IsNullOrWhiteSpace(referrerController) == false
                && string.IsNullOrWhiteSpace(referrerAction) == false
                && string.Equals(referrerController, "Home") == false
                && string.Equals(referrerController, "Login") == false)
            {
                Session[ConstantKeywords.C_ReferrerController] = null;
                Session[ConstantKeywords.C_ReferrerAction] = null;
                return RedirectToAction(referrerAction, referrerController);
            }
            else {             
                return View();
            }
          
        }

        public ActionResult About()
        {
            ViewBag.Message = "Your application description page.";
            return View();
        }

        public ActionResult Contact()
        {
            ViewBag.Message = "Your contact page.";

            return View();
        }
        public ActionResult Projectlink()
        {
            return View();
        }








       


    }
}