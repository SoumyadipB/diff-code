using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;
using System.IO;
using System.Web.Script.Serialization;
using System.Web.Mvc;

namespace ISF_WEB.ModelsBinder
{
    public class JsonModelBinder : DefaultModelBinder
    {
        public override object BindModel(ControllerContext controllerContext, ModelBindingContext bindingContext)
        {
            if (!IsJSONRequest(controllerContext))
            {
                return base.BindModel(controllerContext, bindingContext);
            }

            // Get the JSON data that's been posted
            var request = controllerContext.HttpContext.Request;
            //in some setups there is something that already reads the input stream if content type = 'application/json', so seek to the begining
            request.InputStream.Seek(0, SeekOrigin.Begin);
            var jsonStringData = new StreamReader(request.InputStream).ReadToEnd();

            // Use the built-in serializer to do the work for us
            return new JavaScriptSerializer()
                .Deserialize(jsonStringData, bindingContext.ModelMetadata.ModelType);

           
        }

        private static bool IsJSONRequest(ControllerContext controllerContext)
        {
            var contentType = controllerContext.HttpContext.Request.ContentType;
            return contentType.Contains("application/json");
        }
    }
}