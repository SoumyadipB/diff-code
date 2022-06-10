using System.Web;
using System.Web.Optimization;

namespace ISF_WEB
{
    public class BundleConfig
    {
        // For more information on bundling, visit http://go.microsoft.com/fwlink/?LinkId=301862
        public static void RegisterBundles(BundleCollection bundles)
        {

            //********************************************Style Bundles***************************************************
            //************************************************************************************************************
            //Note!! List of all CSS files bundles start from here

            //datatable css
            bundles.Add(new StyleBundle("~/bundles/css/DataTableStyle").Include(
                //"~/Content/CSS/DataTable/datatables.css",
                "~/Scripts/DataTable/DataTableApi/datatables.min.css", new CssRewriteUrlTransform()).Include(
                "~/Scripts/DataTable/DataTableApi/Buttons-1.5.1/css/buttons.dataTables.min.css",
                "~/Scripts/DataTable/datatable-override.css"
                ));
            //jquery treegrid
            bundles.Add(new StyleBundle("~/bundles/css/Treegrid").Include(
               //"~/Content/CSS/DataTable/datatables.css",
               "~/Content/CSS/jquery.treegrid.css", new CssRewriteUrlTransform()
               ));

            //protip
            bundles.Add(new StyleBundle("~/bundles/css/protip").Include(
               //"~/Content/CSS/DataTable/datatables.css",
               "~/Scripts/lib/tooltip/protip/protip.min.css"
               ));

            //FlexMonster Styles
            //bundles.Add(new StyleBundle("~/bundles/css/FlexMonsterStyle").Include(
            //    "~/Scripts/flexmonster/flexmonster.min.css",
            //    "~/Scripts/flexmonster/flexmonster.css"
            //    ));

            //rappid css
            bundles.Add(new StyleBundle("~/bundles/css/rappid").Include(
                "~/Scripts/Rappid/build/rappid.min.css",
                "~/Content/CSS/style.css", 
                "~/Scripts/Rappid/apps/KitchenSink/css/theme-picker.css",
                "~/Scripts/Rappid/apps/KitchenSink/css/style.dark.css").Include(
                "~/Scripts/Rappid/apps/KitchenSink/css/style.material.css", new CssRewriteUrlTransform()).Include(
                "~/Scripts/Rappid/apps/KitchenSink/css/style.modern.css").Include(
                // "~/Scripts/Rappid/apps/KitchenSink/css/joint.ui.lightbox.min.css",
                "~/Content/CSS/font-awesome.min.css", new CssRewriteUrlTransform()
                ).Include("~/Scripts/Rappid/apps/KitchenSink/css/style.css", new CssRewriteUrlTransform()));
            //jqueryui 
            bundles.Add(new StyleBundle("~/bundles/css/jqueryui").Include(
                       "~/Content/CSS/jquery-ui.min.css", new CssRewriteUrlTransform()
                      ));

            //bootstarp
            bundles.Add(new StyleBundle("~/bundles/css/bootstrap").Include(
                       "~/Content/CSS/bootstrap.min.css"
                      ));

            //Checkbox and Radio style
            bundles.Add(new StyleBundle("~/bundles/css/checkboxAndRadioStyle").Include(
                      "~/Content/CSS/checbox_radio_style/chk_and_radio.css"
                     ));

            //gantt
            bundles.Add(new StyleBundle("~/bundles/css/gantt").Include(

                "~/Content/CSS/gantt/dhtmlxgantt.css"
                //"~/Content/CSS/gantt/dhtmlxgantt_broadway.css"
                ));
            //gantt Enterprise Edition CSS
            bundles.Add(new StyleBundle("~/bundles/css/ganttEnterprise").Include(

               "~/Scripts/ganttEnterprise/dhtmlxgantt.css",
               //"~/Scripts/ganttEnterprise/dhtmlxgantt_broadway.css",
               "~/Scripts/ganttEnterprise/dhtmlxgantt_skyblue.css"
                ));
            //DeliveryTracker
            bundles.Add(new StyleBundle("~/bundles/css/DeliveryTracker").Include(
                "~/Content/CSS/gantt/activityDaily.css"
                ));
            bundles.Add(new StyleBundle("~/bundles/css/Resource").Include(
                "~/Content/CSS/Resource/site.css"
                ));

            bundles.Add(new StyleBundle("~/bundles/css/ResourceCalendar").Include(
                "~/Content/CSS/Resource/ResourceCalendar/dhtmlxscheduler_material.css"
                ));

            bundles.Add(new StyleBundle("~/bundles/css/DeliveryExecution").Include(
               "~/Content/CSS/DeliveryExecution/execution.css"
               ));
            bundles.Add(new StyleBundle("~/bundles/css/CreateAutosenseRule").Include(
              "~/Content/CSS/CreateAutosenseRule/createAutosenseRule.css",
              "~/ Content/CSS/style.css"
              ));
            bundles.Add(new StyleBundle("~/bundles/css/select2").Include(
                "~/Scripts/select2/select2.min.css"
                ));
            bundles.Add(new StyleBundle("~/bundles/css/Dashboard").Include(
               "~/Content/CSS/DashboardCSS.css"
               ));
            //Zebra DatePicker
            bundles.Add(new StyleBundle("~/bundles/css/datepicker").Include(
               "~/Scripts/DatePicker/zebra_datepicker.min.css"
               ));


            bundles.Add(new StyleBundle("~/bundles/css/bootstrapDatepicker").Include(
               "~/Content/CSS/datepicker/bootstrap-datepicker.min.css"
               ));

            bundles.Add(new StyleBundle("~/bundles/css/TimePicker").Include(

               "~/Content/CSS/TimePicker/bootstrap-clockpicker.min.css",
               "~/Content/CSS/TimePicker/jquery-clockpicker.min.css",
               "~/Content/CSS/TimePicker/standalone.css"
               ));

            bundles.Add(new StyleBundle("~/bundles/css/DeliveryExecutionFeedback").Include(

               "~/Content/CSS/DeliveryExecution/DeliveryExecutionFeedback.css"

               ));

            //autosense Css
            bundles.Add(new StyleBundle("~/bundles/css/autosenseApp").Include(
               "~/Scripts/CreateAutosenseRule/rappid.min.css",
               "~/Scripts/CreateAutosenseRule/joint.min.css",
               "~/Scripts/CreateAutosenseRule/css/autoSense.css",
               "~/Scripts/Rappid/apps/KitchenSink/css/style.css",
               "~/Content/CSS/style.css",
               //"~/Scripts/Rappid/apps/KitchenSink/css/theme-picker.css",
               "~/Scripts/Rappid/apps/KitchenSink/css/style.dark.css",
               "~/Scripts/Rappid/apps/KitchenSink/css/style.material.css",
               "~/Scripts/Rappid/apps/KitchenSink/css/style.modern.css"
               // "~/Scripts/Rappid/apps/KitchenSink/css/joint.ui.lightbox.min.css",
               //"~/Content/CSS/font-awesome.min.css"
               ));

            bundles.Add(new StyleBundle("~/bundles/css/darkTheme").Include(
                "~/Content/CSS/edsBlackTheme.css"
                ));

            //*********************** Script Bundles ****************
            bundles.Add(new ScriptBundle("~/bundles/customConsole").Include(
               "~/Scripts/commonJs/customConsole.js"
                ));

            bundles.Add(new ScriptBundle("~/bundles/ajaxRequest").Include(
               "~/Scripts/commonJs/ajaxRequest.js"
                ));


            //Note!! List of all Script files bundles start from here
            //jqueryui
            bundles.Add(new ScriptBundle("~/bundles/jqueryui").Include(
               "~/Scripts/jquery-ui.min.js"
                ));

            bundles.Add(new ScriptBundle("~/bundles/formbuilder").Include(
               "~/Scripts/lib/formBuilder/form-builder.min.js",
               "~/Scripts/lib/formBuilder/form-render.min.js",
               "~/Scripts/lib/formBuilder/custom_functions.js"
                ));



            //jquery
            bundles.Add(new ScriptBundle("~/bundles/jquery").Include(
               "~/Scripts/jquery-2.1.4.js", "~/Scripts/jquery-2.2.4.min.js"
                ));

            //FlexMonsterScripts
            bundles.Add(new ScriptBundle("~/bundles/flexmonsterScript").Include(
               "~/Scripts/flexmonster/flexmonster.js"
                ));
            //Config
            bundles.Add(new ScriptBundle("~/bundles/config").Include(
                 "~/Scripts/services/config.js"
                 ));


            //Bootstrap
            bundles.Add(new ScriptBundle("~/bundles/bootstrap").Include(
                 "~/Scripts/bootstrap.min.js"
                 ));
            //DatePicker
            bundles.Add(new ScriptBundle("~/bundles/datepicker").Include(
                 "~/Scripts/bootstrap-datepicker.js"
                 ));

            //layout bundles
            bundles.Add(new ScriptBundle("~/Scripts/menu").Include(
                      "~/Scripts/global.js"
                      ));

            //select2
            bundles.Add(new ScriptBundle("~/bundles/select2").Include(
                "~/Scripts/select2/select2.full.min.js"
                ));

            //Rappid
            bundles.Add(new ScriptBundle("~/bundles/rappid").Include(
                "~/Scripts/Rappid/node_modules/jquery/dist/jquery.js",
                "~/Scripts/Rappid/node_modules/lodash/index.js",
                "~/Scripts/Rappid/node_modules/backbone/backbone.js",
                "~/Scripts/Rappid/node_modules/graphlib/dist/graphlib.core.js",
                "~/Scripts/Rappid/node_modules/dagre/dist/dagre.core.js",
                "~/Scripts/Rappid/build/rappid.min.js",
                "~/Scripts/Rappid/apps/KitchenSink/js/config/halo.js",
                "~/Scripts/Rappid/apps/KitchenSink/js/config/selection.js",
                "~/Scripts/Rappid/apps/KitchenSink/js/config/inspector.js",
                "~/Scripts/Rappid/apps/KitchenSink/js/config/stencil.js",
                "~/Scripts/Rappid/apps/KitchenSink/js/config/toolbar.js",
                "~/Scripts/constants.js",
                "~/Scripts/Rappid/apps/KitchenSink/js/views/main.js",
                "~/Scripts/Rappid/apps/KitchenSink/js/views/theme-picker.js",
                "~/Scripts/Rappid/apps/KitchenSink/js/models/joint.shapes.app.js",
                "~/Scripts/Rappid/apps/KitchenSink/js/views/navigator.js",
                "~/Scripts/Rappid/apps/KitchenSink/js/config/sample-graphs.js"

                ));


            //Autosense
            bundles.Add(new ScriptBundle("~/bundles/autosenseApp").Include(
               "~/Scripts/Rappid/node_modules/jquery/dist/jquery.js",
               "~/Scripts/Rappid/node_modules/lodash/index.js",
               "~/Scripts/Rappid/node_modules/backbone/backbone.js",
               "~/Scripts/CreateAutosenseRule/rappid.min.js",
               "~/Scripts/CreateAutosenseRule/createHTMLMarkups.js",
               "~/Scripts/CreateAutosenseRule/joint.shapes.html.js",
                //"~/Scripts/Rappid/apps/KitchenSink/js/views/theme-picker.js",
                "~/Scripts/CreateAutosenseRule/validation_rule.js"

               ));

            //datatable
            bundles.Add(new ScriptBundle("~/bundles/DataTableScript").Include(
                "~/Scripts/DataTable/jquery.dataTables.min.js",
                "~/Scripts/DataTable/DataTableApi/Buttons-1.5.1/js/buttons.html5.min.js"
                ));

            bundles.Add(new ScriptBundle("~/bundles/DataTablesAdvancedScript").Include(
                "~/Scripts/DataTable/DataTableApi/ColReorder-1.4.1/js/dataTables.colReorder.min.js",
                "~/Scripts/DataTable/DataTableApi/FixedColumns-3.2.4/js/dataTables.fixedColumns.min.js",
                "~/Scripts/DataTable/DataTableApi/Buttons-1.5.1/js/dataTables.buttons.min.js",
                "~/Scripts/DataTable/DataTableApi/Buttons-1.5.1/js/buttons.colVis.min.js",
                "~/Scripts/DataTable/jszip.min.js"
                ));

            bundles.Add(new ScriptBundle("~/bundles/ActivityMaster").Include(
                "~/Scripts/ActivityMaster/JsActMaster.js"
                ));
            bundles.Add(new ScriptBundle("~/bundles/Admin").Include(
                "~/Scripts/Admin/JsViewAccess.js",
                "~/Scripts/Admin/JsCreateAccess.js",
                "~/Scripts/Admin/JsAssignAccess.js",
                 "~/Scripts/Admin/JsconfigureDomainSpoc.js",
                 "~/Scripts/Admin/mssLeaveUpload.js"
                ));
            bundles.Add(new ScriptBundle("~/bundles/BotStore").Include(
                "~/Scripts/Bots/BotStore.js",
                "~/Scripts/Bots/DeployedBots.js",
                "~/Scripts/Bots/BotApproval.js",
                "~/Scripts/Bots/BotDevRequests.js"
               
               
                ));
            bundles.Add(new ScriptBundle("~/bundles/DeliveryAcceptance").Include(
                "~/Scripts/DeliveryExecution/plannedWorkOrder.js",
               "~/Scripts/DeliveryAcceptance/JsDelAcceptance.js"

                ));
            bundles.Add(new ScriptBundle("~/bundles/DeliveryExecution").Include(
                "~/Scripts/DeliveryExecution/DeliveryExecution.js",
                "~/Scripts/DeliveryExecution/plannedWorkOrder.js",
                "~/Scripts/WorkFlow/flowchart.js",
                "~/Scripts/WorkFlow/wrkflow.js"
                ));

            bundles.Add(new ScriptBundle("~/bundles/WorkorderAndTask").Include(
                "~/Scripts/DeliveryExecution/InprogressTasks.js",
                "~/Scripts/DeliveryExecution/WorkorderPanel.js",
                "~/Scripts/DeliveryExecution/WorkorderAndTask.js",
                //"~/Scripts/constants.js",
                //"~/Scripts/DeliveryExecution/NonWorkingHour.js",
                "~/Scripts/DeliveryExecution/WFFeedback.js",
                "~/Scripts/WorkFlow/flowchart.js",
                "~/Scripts/WorkFlow/wrkflow.js",
                "~/Scripts/NetworkElement/constantsForNetworkElement.js"
                ));

            bundles.Add(new ScriptBundle("~/bundles/ResourceEngagement").Include(
               "~/Scripts/Resource/ResourceDailyTable.js",
               "~/Scripts/Resource/ResourceCalendar/dhtmlxscheduler.js",
               "~/Scripts/Resource/ResourceCalendar/dhtmlxscheduler_tooltip.js",
               "~/Scripts/Resource/ResourceCalendar/dhtmlxscheduler_active_links.js",
               "~/Scripts/Resource/ResourceCalendar/dhtmlxscheduler_limit.js",
               "~/Scripts/Resource/ResourceCalendar/Calendar.js"
               ));

            bundles.Add(new ScriptBundle("~/bundles/WorkOrderCreatePopUp").Include(
                "~/Scripts/Project/workOrder.js",
                "~/Scripts/Project/JsWorkFlow.js"
                ));

            bundles.Add(new ScriptBundle("~/bundles/Login").Include(
                "~/Scripts/Login/Login.js"
                ));
            bundles.Add(new ScriptBundle("~/bundles/AzureADLogin").Include(
               "~/Scripts/Login/AzureADLogin.js"
               ));
            bundles.Add(new ScriptBundle("~/bundles/Project").Include(
                "~/Scripts/Project/ViewProject.js",
                "~/Scripts/Project/project_search.js",
                "~/Scripts/Project/project_approval.js",
                "~/Scripts/Project/JsProject.js",
                "~/Scripts/Project/FlowChartWorkOrderView.js"

                ));
            bundles.Add(new ScriptBundle("~/bundles/Reports").Include(
                "~/Scripts/Reports/addreports.js",
                "~/Scripts/Reports/generateReports.js",
                "~/Scripts/Reports/searchReport.js",
                "~/Scripts/Reports/ReportFilter.js"

                ));

            bundles.Add(new ScriptBundle("~/bundles/Resource").Include(
                 "~/Scripts/Resource/JsSearchAllocateResource.js",
                 "~/Scripts/Resource/DemandFulfilment.js",
                 "~/Scripts/Resource/JsChangeManagement.js"
                ));

            bundles.Add(new ScriptBundle("~/bundles/SelfService").Include(
                "~/Scripts/SelfService/selfService.js"
                ));
            bundles.Add(new ScriptBundle("~/bundles/TeamExplorer").Include(
                "~/Scripts/TeamExplorer/teamExplorer.js",
                "~/Scripts/TeamExplorer/profileAccess.js"
                ));
            bundles.Add(new ScriptBundle("~/bundles/Vendor").Include(
                "~/Scripts/Vendor/Vendor.js"
                ));
            bundles.Add(new ScriptBundle("~/bundles/WorkFlow").Include(
                "~/Scripts/WorkFlow/wrkflow.js",
                 "~/Scripts/WorkFlow/flowchart.js",
                 "~/Scripts/WorkFlow/DeliveryExecution.js",
                "~/Scripts/WorkFlow/plannedWorkOrder.js"
                ));
            bundles.Add(new ScriptBundle("~/bundles/WorkOrder").Include(
                "~/Scripts/WorkOrder/workOrder.js",
                "~/Scripts/WorkOrder/workOrderCreate.js"
                ));
            bundles.Add(new ScriptBundle("~/bundles/Dashboard").Include(
                "~/Scripts/Dashboard/dashboardMainJS.js"
                ));
            //DeliveryTracker 
            bundles.Add(new ScriptBundle("~/bundles/DeliveryTracker").Include(
                "~/Scripts/gantt/ActivityDaywiseAjax.js",
                "~/Scripts/gantt/custom.js",
                "~/Scripts/gantt/deliveryTracker.js",
                "~/Scripts/gantt/workflow_new.js"
                ));
            //gantt Licensed Version 
            bundles.Add(new ScriptBundle("~/bundles/ganttEnterprise").Include(
                "~/Scripts/ganttEnterprise/dhtmlxgantt.js",
                "~/Scripts/ganttEnterprise/dhtmlxgantt_fullscreen.js",
                "~/Scripts/ganttEnterprise/dhtmlxgantt_undo.js",
                "~/Scripts/ganttEnterprise/dhtmlxgantt_tooltip.js"
                //"~/Scripts/ganttEnterprise/dhtmlxgantt_keyboard_navigation.js"
                ));
            //gantt 
            bundles.Add(new ScriptBundle("~/bundles/gantt").Include(
                "~/Scripts/gantt/dhtmlxgantt.js",
                "~/Scripts/gantt/dhtmlxgantt_fullscreen.js",
                "~/Scripts/gantt/dhtmlxgantt_undo.js"
                ));
            //zebra Datepicker 
            bundles.Add(new ScriptBundle("~/bundles/zebradatepicker").Include(
                "~/Scripts/DatePicker/zebra_datepicker.min.js"
                ));
            //erittsh
            bundles.Add(new ScriptBundle("~/bundles/RequestApproval").Include(
               "~/Scripts/RequestApproval.js"

               ));
            bundles.Add(new ScriptBundle("~/bundles/TimePicker").Include(

              "~/Scripts/TimePicker/bootstrap-clockpicker.min.js",
              "~/Scripts/TimePicker/jquery-clockpicker.min.js"
              ));

            bundles.Add(new ScriptBundle("~/bundles/TimeZone").Include(
              "~/Scripts/TimeZone/moment.js",
              "~/Scripts/TimeZone/moment-timezone-with-data.js",
              "~/Scripts/TimeZone/TimeZone.js",
              "~/Scripts/TimeZone/TimeZoneColumnsConfigs.js"
              ));
            bundles.Add(new ScriptBundle("~/bundles/SessionManagement").Include(
              "~/Scripts/commonJs/sessionManagement.js"
              ));


            bundles.Add(new ScriptBundle("~/bundles/GetSessionVars").Include(
              "~/Scripts/commonJs/getSessionVars.js"
              ));



            bundles.Add(new ScriptBundle("~/bundles/userLocation").Include(
             "~/Scripts/Login/userLocation.js"
              ));

            bundles.Add(new ScriptBundle("~/bundles/MDMGlobalURL").Include(
                "~/Scripts/ActivityMaster/MDMGlobalURL.js"
                ));
            bundles.Add(new ScriptBundle("~/bundles/UserMenu").Include(
                "~/Scripts/UserMenu/UserMenu.js"
                //"~/Scripts/signalr.min.js"
                ));

            bundles.Add(new ScriptBundle("~/bundles/Home").Include(
                "~/Scripts/Home/Index.js"
                ));

            bundles.Add(new ScriptBundle("~/bundles/BotRequests").Include(
               "~/Scripts/commonJs/downloadBotFiles.js",
               "~/Scripts/Bots/BotStore.js"
               ));

            bundles.Add(new ScriptBundle("~/bundles/Treegrid").Include(
                "~/Scripts/jquery.treegrid.js",
                "~/Scripts/jquery.treegrid.bootstrap3.js",
                "~/Scripts/jquery.treegrid.min.js"
                ));
            bundles.Add(new ScriptBundle("~/bundles/Customer").Include(
               "~/Scripts/ActivityMaster/Customer.js"
               ));
            bundles.Add(new ScriptBundle("~/bundles/DeliveryUnit").Include(
                "~/Scripts/Project/DeliveryUnit.js"
                ));
            bundles.Add(new ScriptBundle("~/bundles/ExternalActivityList").Include(
                "~/Scripts/ActivityMaster/ExternalActivityList.js"
                ));
            bundles.Add(new ScriptBundle("~/bundles/global").Include(
                "~/Scripts/global.js",
                "~/Scripts/commonJs/commonUtility.js"
                ));
            bundles.Add(new ScriptBundle("~/bundles/WorkInstructions").Include(
                "~/Scripts/ActivityMaster/workInstructions.js"
                ));
            bundles.Add(new ScriptBundle("~/bundles/AccessProfile").Include(
                "~/Scripts/Admin/AccessProfile.js"
                ));
            bundles.Add(new ScriptBundle("~/bundles/AddRole").Include(
                "~/Scripts/Admin/AddRole.js"
                ));
            bundles.Add(new ScriptBundle("~/bundles/downloadTemplates").Include(
                "~/Scripts/commonJs/downloadTemplates.js"
                ));
            bundles.Add(new ScriptBundle("~/bundles/Capabilitiestable").Include(
                "~/Scripts/Admin/Capabilitiestable.js"
                ));
            bundles.Add(new ScriptBundle("~/bundles/ExternalKeyGenerator").Include(
                "~/Scripts/Admin/ExternalKeyGenerator.js"
                ));
            bundles.Add(new ScriptBundle("~/bundles/Message").Include(
                "~/Scripts/Admin/Message.js",
                "~/Scripts/bootstrap-datetimepicker.min.js"
                ));
            bundles.Add(new ScriptBundle("~/bundles/RoleCapability").Include(
                "~/Scripts/Admin/RoleCapability.js"
                ));
            bundles.Add(new ScriptBundle("~/bundles/aspExplorer").Include(
                "~/Scripts/ASPExplorer/aspExplorer.js"
                ));
            bundles.Add(new ScriptBundle("~/bundles/AssignedBot").Include(
                "~/Scripts/Bots/AssignedBot.js"
                ));
            bundles.Add(new ScriptBundle("~/bundles/BotAudit").Include(
                "~/Scripts/Bots/BotAudit.js"
                ));
            bundles.Add(new ScriptBundle("~/bundles/jquery-dateformat").Include(
                "~/Scripts/lib/jquery-dateformat.min.js"
                ));
            bundles.Add(new ScriptBundle("~/bundles/BotApproval").Include(
                "~/Scripts/Bots/BotApproval.js"
                ));
            bundles.Add(new ScriptBundle("~/bundles/DeployedBots").Include(
               "~/Scripts/Bots/DeployedBots.js"
               ));
            bundles.Add(new ScriptBundle("~/bundles/downloadBotFiles").Include(
               "~/Scripts/commonJs/downloadBotFiles.js"
               ));
            bundles.Add(new ScriptBundle("~/bundles/BotDevRequests").Include(
               "~/Scripts/Bots/BotDevRequests.js"
               ));
            bundles.Add(new ScriptBundle("~/bundles/BotRepository").Include(
               "~/Scripts/Bots/botRepository.js"
               ));
            bundles.Add(new ScriptBundle("~/bundles/protip").Include(
               "~/Scripts/lib/tooltip/protip/protip.min.js"
               ));
            bundles.Add(new ScriptBundle("~/bundles/BotStoreScript").Include(
               "~/Scripts/Bots/BotStore.js"
               ));
            bundles.Add(new ScriptBundle("~/bundles/DeployedBOTSavings").Include(
               "~/Scripts/Bots/DeployedBOTSavings.js"
               ));
            bundles.Add(new ScriptBundle("~/bundles/AdminBotStore").Include(
              "~/Scripts/Admin/BotStore.js"
              ));
            bundles.Add(new ScriptBundle("~/bundles/ourCustom").Include(
              "~/Scripts/Rappid/our_custom.js"
              ));
            bundles.Add(new ScriptBundle("~/bundles/FlowChartBOTEdit").Include(
              "~/Scripts/Bots/FlowChartBOTEdit.js"
              ));
            bundles.Add(new ScriptBundle("~/bundles/FlowChartScript").Include(
              "~/Scripts/FlowChartScripts/FlowChartScript.js"
              ));
            bundles.Add(new ScriptBundle("~/bundles/wrkflowScript").Include(
              "~/Scripts/services/wrkflow.js"
              ));
            bundles.Add(new ScriptBundle("~/bundles/FlowChartEdit").Include(
              "~/Scripts/FlowChartScripts/FlowChartEdit.js"
              ));
            bundles.Add(new ScriptBundle("~/bundles/MainPage").Include(
              "~/Scripts/services/mainPage.js"
              ));
            bundles.Add(new ScriptBundle("~/bundles/FlowChartExecution").Include(
              "~/Scripts/FlowChartScripts/FlowChartExecution.js"
              ));
            bundles.Add(new ScriptBundle("~/bundles/WorkorderPanel").Include(
              "~/Scripts/DeliveryExecution/WorkorderPanel.js"
              ));
            bundles.Add(new ScriptBundle("~/bundles/FlowChartWorkOrderView").Include(
              "~/Scripts/Project/FlowChartWorkOrderView.js"
              ));
            bundles.Add(new ScriptBundle("~/bundles/FlViewWithoutComment").Include(
              "~/Scripts/FlowChartScripts/FlViewWithoutComment.js"
              ));
            bundles.Add(new ScriptBundle("~/bundles/PlannedAssignment").Include(
              "~/Scripts/DeliveryExecution/PlannedAssignment.js"
              ));
            bundles.Add(new ScriptBundle("~/bundles/introjs").Include(
              "~/Scripts/lib/introjs/intro.min.js"
              ));
            bundles.Add(new ScriptBundle("~/bundles/DeliveryIndex").Include(
              "~/Scripts/DeliveryIndex/DeliveryIndex.js"
              ));
            bundles.Add(new ScriptBundle("~/bundles/DemandForecastDetails").Include(
              "~/Scripts/DemandForecast/DemandForecastDetails.js"
              ));
            bundles.Add(new ScriptBundle("~/bundles/profileRole").Include(
              "~/Scripts/services/profileRole.js"
              ));
            bundles.Add(new ScriptBundle("~/bundles/treegrid").Include(
              "~/Scripts/jquery.treegrid.min.js"
              ));
            bundles.Add(new ScriptBundle("~/bundles/Index").Include(
              "~/Scripts/Home/Index.js"
              ));
            bundles.Add(new ScriptBundle("~/bundles/Monitoring").Include(
              "~/Scripts/Monitoring/Monitoring.js"
              ));
            bundles.Add(new ScriptBundle("~/bundles/FlowChartView").Include(
              "~/Scripts/FlowChartScripts/FlowChartView.js"
              ));
            bundles.Add(new ScriptBundle("~/bundles/FlowchartJsonPlot").Include(
              "~/Scripts/Project/FlowchartJsonPlot.js"
              ));
            bundles.Add(new ScriptBundle("~/bundles/projectCreation").Include(
              "~/Scripts/Project/createProject.js",
              "~/Scripts/Project/JsProject.js"
              ));
            bundles.Add(new ScriptBundle("~/bundles/dhtmlx").Include(
              "~/Scripts/gantt/dhtmlx.js"
              ));
            bundles.Add(new ScriptBundle("~/bundles/JsProject").Include(
              "~/Scripts/Project/JsProject.js"
              ));
            bundles.Add(new ScriptBundle("~/bundles/ChangeRequest").Include(
              "~/Scripts/Project/ChangeRequest.js"
              ));
            bundles.Add(new ScriptBundle("~/bundles/ResourceApproval").Include(
              "~/Scripts/Project/ResourceApproval.js"
              ));
            bundles.Add(new ScriptBundle("~/bundles/viewResource").Include(
              "~/Scripts/Project/viewResource.js"
              ));
            bundles.Add(new ScriptBundle("~/bundles/ReportIndex").Include(
              "~/Scripts/Reports/ReportIndex.js"
              ));
            bundles.Add(new ScriptBundle("~/bundles/ReportIndexSP").Include(
              "~/Scripts/Reports/ReportIndexSP.js"
              ));
            bundles.Add(new ScriptBundle("~/bundles/DemandFulfilment").Include(
              "~/Scripts/Resource/DemandFulfilment.js"
              ));
            bundles.Add(new ScriptBundle("~/bundles/weekpicker").Include(
              "~/Scripts/jquery.weekpicker.js"
              ));
            bundles.Add(new ScriptBundle("~/bundles/layoutTheme").Include(
              "~/Scripts/layoutTheme.js"
              ));
            bundles.Add(new ScriptBundle("~/bundles/App").Include(
              "~/Scripts/App.js",
              "~/Scripts/AppNavigation.js"
              ));
            bundles.Add(new ScriptBundle("~/bundles/nanoscroller").Include(
              "~/Scripts/jquery.nanoscroller.js"
              ));
            bundles.Add(new ScriptBundle("~/bundles/NonWorkingHour").Include(
              "~/Scripts/DeliveryExecution/NonWorkingHour.js",
               "~/Scripts/DeliveryExecution/WebNotification.js"
              ));
            bundles.Add(new ScriptBundle("~/bundles/activityDaily").Include(
              "~/Scripts/gantt/activityDaily.js"
              ));
            bundles.Add(new ScriptBundle("~/bundles/custom").Include(
              "~/Scripts/gantt/custom.js"
              ));
            bundles.Add(new ScriptBundle("~/bundles/activityHourly").Include(
              "~/Scripts/gantt/activityHourly.js"
              ));
            bundles.Add(new ScriptBundle("~/bundles/activityMonthly").Include(
              "~/Scripts/gantt/activityMonthly.js"
              ));
            bundles.Add(new ScriptBundle("~/bundles/workflowNew").Include(
              "~/Scripts/gantt/workflow_new.js"
              ));
            bundles.Add(new ScriptBundle("~/bundles/competenceAndTraining").Include(
              "~/Scripts/Competence/competenceAndTraining.js"
              ));
            bundles.Add(new ScriptBundle("~/bundles/mdmCompetenceUpload").Include(
              "~/Scripts/Competence/mdmCompetenceUpload.js"
              ));
            bundles.Add(new ScriptBundle("~/bundles/projectApproval").Include(
              "~/Scripts/Project/project_approval.js"
              ));
            bundles.Add(new ScriptBundle("~/bundles/deliveryResponsible").Include(
              "~/Scripts/Project/delivery_responsible.js"
              ));
            bundles.Add(new ScriptBundle("~/bundles/JsResource").Include(
              "~/Scripts/Project/JsResource.js"
              ));
            bundles.Add(new ScriptBundle("~/bundles/executionPlan").Include(
              "~/Scripts/Project/executionPlan.js"
              ));
            bundles.Add(new ScriptBundle("~/bundles/JsNetworkElement").Include(
              "~/Scripts/Project/JsNetworkElement.js"
              ));
            bundles.Add(new ScriptBundle("~/bundles/projectWorkOrder").Include(
              "~/Scripts/Project/projectWorkOrder.js"
              ));
            bundles.Add(new ScriptBundle("~/bundles/Scope").Include(
              "~/Scripts/Project/jsScope.js",
              "~/Scripts/Project/deliverablePlan.js"
              ));
            bundles.Add(new ScriptBundle("~/bundles/projectSearch").Include(
              "~/Scripts/Project/project_search.js"
              ));
            bundles.Add(new ScriptBundle("~/bundles/ViewProject").Include(
              "~/Scripts/Project/ViewProject.js"
              ));
            bundles.Add(new ScriptBundle("~/bundles/WorkOrderPlan").Include(
              "~/Scripts/Project/workOrder.js"
              ));
            bundles.Add(new ScriptBundle("~/bundles/ASPConfigureVendorManager").Include(
              "~/Scripts/Admin/ASPConfigureVendorManager.js"
              ));
            bundles.Add(new ScriptBundle("~/bundles/LocationType").Include(
               "~/Scripts/ActivityMaster/LocationType.js"
               ));
            bundles.Add(new ScriptBundle("~/bundles/DemandType").Include(
               "~/Scripts/ActivityMaster/DemandType.js"
               ));

            bundles.Add(new ScriptBundle("~/bundles/DeliveryExecutionFeedback").Include(
                "~/Scripts/DeliveryExecutionFeedback/deliveryExecutionFeedback.js"
                ));
            bundles.Add(new ScriptBundle("~/bundles/CreateAutosenseRule").Include(
                "~/Scripts/CreateAutosenseRule/createAutosenseRule.js"
                ));
            bundles.Add(new ScriptBundle("~/bundles/RuleMigration").Include(
                "~/Scripts/RuleMigration/ruleMigration.js"
                ));

            bundles.Add(new ScriptBundle("~/bundles/gtag").Include(
                "~/Scripts/commonJs/gtag.js"
                ));
            bundles.Add(new ScriptBundle("~/bundles/constants").Include(
                "~/Scripts/constants.js"
                ));
            bundles.Add(new ScriptBundle("~/bundles/MSSLeaveUpload").Include(
             "~/Scripts/MSSLeave/mssLeaveUpload.js"
             ));
            bundles.Add(new ScriptBundle("~/bundles/BotErrorDictionary").Include(
               "~/Scripts/Bots/BotErrorDictionary.js"
               ));
            bundles.Add(new ScriptBundle("~/bundles/azure").Include(
               "~/Scripts/Azure/azure-storage.blob.js",
               "~/Scripts/Azure/azure-storage.blob.min.js",
                "~/Scripts/Azure/azure-storage.file.js",
                "~/Scripts/Azure/azure-storage.file.min.js",
                "~/Scripts/Azure/azure-storage.queue.js",
                "~/Scripts/Azure/azure-storage.queue.min.js",
                "~/Scripts/Azure/azure-storage.table.js",
                 "~/Scripts/Azure/azure-storage.table.min.js"
               ));
            bundles.Add(new ScriptBundle("~/bundles/NetworkElementAddEdit").Include(
                "~/Scripts/NetworkElement/networkElementAddEdit.js"
                ));

            bundles.Add(new StyleBundle("~/bundles/CSS/EDS/EDSStyle").Include(
                      "~/Content/CSS/EDS/EDSStyle.css"
                     ));
            bundles.Add(new ScriptBundle("~/bundles/AutoSense").Include(
                    "~/Scripts/DeliveryExecution/AutoSense.js"
                   ));
            bundles.Add(new ScriptBundle("~/bundles/commentsJs").Include(
                   "~/Scripts/lib/commentsjs/jquery-comments.js"
                  ));
            bundles.Add(new ScriptBundle("~/bundles/WorkOrderAndQueue").Include(
                   "~/Scripts/DeliveryExecution/assignToMe.js",
                   "~/Scripts/Project/workOrder.js",
                   "~/Scripts/DeliveryExecution/assignedTasksForTransfer.js",
                   "~/Scripts/DeliveryExecution/MyClosedWorkOrder.js"
                  ));
            bundles.Add(new StyleBundle("~/bundles/Scripts/lib/introjs/WorkOrderAndQueueCss").Include(
              "~/Scripts/lib/introjs/introjs.min.css",
              "~/Scripts/lib/tooltip/protip/protip.min.css"
              ).Include("~/Scripts/lib/commentsjs/jquery-comments.css", new CssRewriteUrlTransform()));
            
            bundles.Add(new ScriptBundle("~/bundles/WFFeedback").Include(
              "~/Scripts/DeliveryExecution/WFFeedback.js"
              ));
            bundles.Add(new ScriptBundle("~/bundles/MQTT").Include(
              "~/Scripts/MQTT/mqttConnection.js"
              ));

            bundles.Add(new ScriptBundle("~/bundles/SharePointConfig").Include(
                "~/Scripts/ActivityMaster/SharePointConfig.js"
                ));
            //Unzip
            bundles.Add(new ScriptBundle("~/bundles/zip").Include(
               "~/Scripts/zip/jszip.min.js"
               ));

            bundles.Add(new ScriptBundle("~/bundles/botSavingEmeCalculation").Include(
                "~/Scripts/BotSaving/botSavingEmeCalculation.js",
                "~/Scripts/BotSaving/botSavingContants.js"
                ));

            bundles.Add(new ScriptBundle("~/bundles/rowsGroup").Include(
                "~/Scripts/DataTable/dataTables.rowsGroup.js"
                ));
        }
    }
}