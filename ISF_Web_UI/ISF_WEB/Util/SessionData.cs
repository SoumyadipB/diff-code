using ISF_WEB.Models;
using Newtonsoft.Json;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Net.Http;
using System.Threading.Tasks;
using System.Web;

namespace ISF_WEB.Util
{
    public class SessionData
    {

        public async Task<dynamic> GetSignumFromApiAsync()
        {
            string signum = string.Empty;
            bool isAsp = false;
            var ctx = HttpContext.Current;
            if (ctx.Session != null && ctx.Session[ConstantKeywords.C_UserEmaiId] !=null)
            {
                AdfsUserDetail userDetail = new AdfsUserDetail();
                userDetail.employeeEmailID = ctx.Session[ConstantKeywords.C_UserEmaiId].ToString();
                userDetail.employeeUpn = (ctx.Session[ConstantKeywords.C_UserUpn] != null
                        ? ctx.Session[ConstantKeywords.C_UserUpn].ToString() : null);
                userDetail.employeeName = string.Format("{0} {1}",
                    ctx.Session[ConstantKeywords.C_UserGivenName].ToString(),
                    ctx.Session[ConstantKeywords.C_UserSurName].ToString());
                if (ctx.Session[ConstantKeywords.C_Isignum] != null)
                {
                    userDetail.employeeType = ConstantKeywords.C_EmployeeTypeAsp;
                    isAsp = true;
                }
                else
                {
                    userDetail.employeeType = ConstantKeywords.C_EmployeeTypeEricson;
                }
                DataFromAPi dataFromAPi = new DataFromAPi();
                dynamic result = await dataFromAPi.PostSignumDataAsync(userDetail, ConstantJavaApiControllerName.C_getUserSignumByEmail);
                List<UserSignumByEmail> convertedResult = JsonConvert.DeserializeObject<List<UserSignumByEmail>>(result);
                signum = convertedResult.Select(x => x.signumID).FirstOrDefault();
                ctx.Session[ConstantKeywords.C_AspStatus] = convertedResult.Select(x => x.aspStatus).FirstOrDefault();
                ctx.Session[ConstantKeywords.C_IsAspUser] = isAsp;
                return await Task.FromResult<dynamic>(signum);

            }
            else
            {
                return null;
            }
        }
        public async Task<dynamic> GetPageAccessDetailsByRoleOfSignum()
        {
            

            var ctx = HttpContext.Current;
            if (ctx.Session != null && ctx.Session[ConstantKeywords.C_Signum] != null)
            {
                string paramController = string.Format("{0}", ConstantJavaApiControllerName.C_getPageAccessDetailsByRoleOfSignum);

                DataFromAPi dataFromAPi = new DataFromAPi();
                dynamic result = await dataFromAPi.GetDataAsync(paramController);
                return await Task.FromResult<dynamic>(result);

            }
            else
            {
                return null;
            }


        }
        public async Task<dynamic> GetUserAccessProfile()
        {

            var ctx = HttpContext.Current;
            if (ctx.Session != null && ctx.Session[ConstantKeywords.C_Signum] != null)
            {
                try
                {
                    string paramController = string.Format("{0}/{1}", ConstantJavaApiControllerName.C_getUserAccessProfile, ctx.Session[ConstantKeywords.C_Signum]);

                    DataFromAPi dataFromAPi = new DataFromAPi();
                    dynamic result = await dataFromAPi.GetDataAsync(paramController);
                    return await Task.FromResult<dynamic>(result);
                }
                catch(Exception ex)
                {
                    return ex.InnerException.ToString();
                }
               

            }
            else
            {
                return null;
            }


        }

        public async Task<dynamic> GetUserMenu()
        {

            var ctx = HttpContext.Current;
            if (ctx.Session != null && ctx.Session[ConstantKeywords.C_Signum] != null)
            {
                string paramController = string.Format("{0}", ConstantJavaApiControllerName.C_getUserMenu);

                DataFromAPi dataFromAPi = new DataFromAPi();
                dynamic result = await dataFromAPi.GetDataAsync(paramController);
                return await Task.FromResult<dynamic>(result);

            }
            else
            {
                return null;
            }


        }

    }
}