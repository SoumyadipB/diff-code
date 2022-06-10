

var login_self_signum = signumGlobal;
login_self_signum = "ebhjpra";


function disableFields() {
    document.getElementById("self_employee_signum").disabled = true;
    document.getElementById("self_employee_email").disabled = true;    
    document.getElementById("self_employee_jobRole").disabled = true;    
    document.getElementById("self_employee_serviceArea").disabled = true;
    document.getElementById("self_employee_officeBuilding").disabled = true;
    document.getElementById("self_employee_contactNumber").disabled = true;
    document.getElementById("self_employee_employeeName").disabled = true;
    document.getElementById("self_employee_personnelNumber").disabled = true;    
    document.getElementById("self_employee_jobRoleFamily").disabled = true;    
    document.getElementById("self_employee_city").disabled = true;   
    document.getElementById("self_employee_costCenter").disabled = true;    
    document.getElementById("self_employee_functionalArea").disabled = true;    
    document.getElementById("self_employee_managerSignum").disabled = true;
}

function getEmployeeDetailsBySignumViewProfile() {

    $.isf.ajax({
        url: service_java_URL + "activityMaster/getEmployeeBySignum/" + signumGlobal,
        success: function (data) {

            $("#self_employee_signum").val(data.signum);
            $("#self_employee_email").val(data.employeeEmailId);
            $("#self_employee_unit").val(data.orgUnit);            
            $("#self_employee_jobRole").val(data.jobName);            
            $("#self_employee_serviceArea").val(data.serviceArea);
            $("#self_employee_officeBuilding").val(data.officeBuilding);
            $("#self_employee_contactNumber").val(data.contactNumber);
            $("#self_employee_employeeName").val(data.employeeName);
            $("#self_employee_personnelNumber").val(data.personnelNumber);            
            $("#self_employee_jobRoleFamily").val(data.jobRoleFamily);            
            $("#self_employee_city").val(data.city);            
            $("#self_employee_costCenter").val(data.costCenter);
            $("#self_employee_functionalArea").val(data.functionalArea);
            $("#self_employee_hrLocation").val(data.hrLocation);
            $("#self_employee_managerSignum").val(data.managerSignum);
            $("#self_employee_status").val(data.status);


        },
        error: function (xhr, status, statusText) {
            console.log('An error occurred : ' + xhr.responseText);
        }
    });


}