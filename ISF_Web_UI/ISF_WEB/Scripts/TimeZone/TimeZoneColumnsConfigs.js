//Format of Columns Config
//var APIName_tzColumns = [Column Names Array]
//For example : 
//var xyzAPI_tzColumns = ["Column 1", "Column 2",..."Column n"];

var getclosedWODetails_tzColumns = ["plannedOn", "closedOn"];

var getacceptedWODetails_tzColumns = ["plannedOn", "closedOn"];

var getWorkOrderViewDetailsByWOID_tzColumns = ["plannedStartDate", "plannedEndDate", "actualStartDate", "actualEndDate"];

var getResourceCalander_tzColumns = ["start_date", "end_date"];
var getBacklogWorkOrders_tzColumns = ["plannedStartDate", "PlannedEndDate"];

var getWorkOrders_tzColumns = ["plannedstartDate", "plannedclosedOn", "actualstartDate", "actualclosedOn"];

var getUnassignedWorkOrders_tzColumns = ["startDate", "endDate"];
var getAssignedWorkOrders_tzColumns = ["startDate", "endDate"];

var searchPlannedWorkOrders_tzColumns = ["startDate", "endDate"];
var getAuditData_tzColumns = ["created"];
var getShitTimmingBySignum_tzColumns = ["shiftISTEndTime", "shiftISTStartTime"];
var getWorkOrderPlans_tzColumns = ["plannedStartDate", "plannedEndDate", "actualStartDate", "actualEndDate"];
var getWorkOrderViewDetails_tzColumns = ["project_StartDate", "project_EndDate", "subActivityStartDate", "subActivityEndDate", "startDate", "endDate", "actualStartDate","actualEndDate"];