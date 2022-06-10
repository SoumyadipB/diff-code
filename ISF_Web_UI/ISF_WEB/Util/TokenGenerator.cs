using ISF_WEB.Models;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Web;

namespace ISF_WEB.Util
{
    public class TokenGenerator
    {
        public string GenerateSessionToken()
        {
            var ctx = HttpContext.Current;
            string token = string.Empty;
            StringManipulation _stringManipulation = new StringManipulation();
            
            StringBuilder sb = new StringBuilder();
            RandomGenerator _randomGenerator = new RandomGenerator();
            if (ctx.Session != null && ctx.Session[ConstantKeywords.C_UserEmaiId] != null)
            {
                string emailId = _stringManipulation.ConvertStringToAsciiWithPipe(
                                _stringManipulation.Reverse(
                                    ctx.Session[ConstantKeywords.C_UserEmaiId].ToString()
                                    ));
                sb.Append("Session ");
                sb.Append(ctx.Session.SessionID);
                sb.Append("-");
                sb.Append(_randomGenerator.RandomPassword());
                sb.Append(emailId);
                sb.Append(_randomGenerator.RandomPassword());
                token = sb.ToString();
               
            }
            else
            {
                token = "IGNORE";
            }
            return token;
        }

        public string GenerateSessionToken(string sessionId,string emailId)
        {
            

            string token = string.Empty;
            StringManipulation _stringManipulation = new StringManipulation();
            StringBuilder sb = new StringBuilder();
            RandomGenerator _randomGenerator = new RandomGenerator();

            emailId = _stringManipulation.ConvertStringToAsciiWithPipe(
                              _stringManipulation.Reverse(
                                  emailId
                                  ));


            sb.Append("Session ");
            sb.Append(sessionId);
            sb.Append("-");
            sb.Append(_randomGenerator.RandomPassword());
            sb.Append(emailId);
            sb.Append(_randomGenerator.RandomPassword());
            token = sb.ToString();
            return token;
        }

        public string Encrypt(string value)
        {
            StringManipulation _stringManipulation = new StringManipulation();
            StringBuilder sb = new StringBuilder();
            RandomGenerator _randomGenerator = new RandomGenerator();

            sb.Append("key*");
            sb.Append(_stringManipulation.Reverse(value));
            sb.Append("*");
            sb.Append(_randomGenerator.RandomPassword());
            var finalValue = sb.ToString();
            return finalValue;
        }
    }
}