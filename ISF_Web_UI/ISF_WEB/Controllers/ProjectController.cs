using ISF_WEB.Filters;
using System;
using System.Collections.Generic;
using System.IO;
using System.Linq;
using System.Web;
using System.Web.Mvc;


namespace ISF_WEB.Controllers
{
 
    public class ProjectController : Controller
    {
        // GET: Project
        public ActionResult Index()
        {

            
           
            return View();
        }

        public ActionResult Add()
        {
            return View();
        }

        public ActionResult Scope()
        {
            return View();
        }
        public ActionResult DemandRequest()
        {
            return View();
        }
        public ActionResult NodeUpload()
        {
            return View();
        }
        public ActionResult WfCreation()
        {
            return View();
        }
        public ActionResult WorkOrderPlan()
        {
            return View();
        }
        public ActionResult Approval()
        {
            return View();
        }

        public ActionResult Search()
        {
            return View();
        }
        public ActionResult ViewProject()
        {
            try
            {
                return View();
            }

            catch (Exception ex)
            {
                throw ex;

            }
        }

        public ActionResult ProjectView()
        {
            return View();
        }
        public ActionResult DeliveryAcceptance()
        {
            return View();
        }
        public ActionResult Detail()
        {
            return View();
        }

        public ActionResult FlowChartWorkOrderAdd()
        {
            return View();
        }

        public ActionResult FlowChartWorkOrderView()
        {
            return View();
        }

        public ActionResult FlowChartApprovalView()
        {
            return View();
        }

        public ActionResult ResourceApproval()
        {
            return View();
        }

        public ActionResult FlowChartView()
        {
            return View();
        }

        public ActionResult FlowChartProject()
        {
            return View();
        }

        public ActionResult FlowChartExecutionProject()
        {
            return View();
        }

        public ActionResult FlowChartEditProject()
        {
            return View();
        }

        public ActionResult FlowChartViewEP()
        {
            return View();
        }

        //public ActionResult BotStore()
        //{
        //    return View();
        //}

        public ActionResult DRBotCreation()
        {
            return View();
        }
        [HttpPost]
        public ActionResult Upload(HttpPostedFileBase file)
        {
            //if (file != null && file.ContentLength > 0)
            //{
            //    var fileName = Path.GetFileName(file.FileName);
            //    var path = Path.Combine(Server.MapPath("~/Images/"), fileName);
            //    file.SaveAs(path);
            //}

            //return RedirectToAction("UploadDocument");
            return View("NodeUpload");
        }

        [HttpGet]

        public ActionResult Download(string file)
        {
            string fileName = Path.GetFileName(file);

            //return the file for download, this is an Excel 
            //so I set the file content type to "application/vnd.ms-excel"
            return File(file, "application/vnd.ms-excel", fileName);
        }

        public ActionResult WorkOrderCreatePopUp()
        {
            return View();
        }
    }
}