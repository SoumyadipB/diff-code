using System;
using System.Collections.Generic;
using System.Linq;
using System.Reflection;
using System.Web;
using System.Web.Script.Serialization;

namespace ISF_WEB.ModelsBinder
{
    public static class JavaScriptSerializerExt
    {
        public static object Deserialize(this JavaScriptSerializer serializer, string input, Type objType)
        {
            var deserializerMethod = serializer.GetType().GetMethod("Deserialize", BindingFlags.NonPublic | BindingFlags.Static);

            // internal static method to do the work for us
            //Deserialize(this, input, null, this.RecursionLimit);

            return deserializerMethod.Invoke(serializer,
                new object[] { serializer, input, objType, serializer.RecursionLimit });
        }
    }
}