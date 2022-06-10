var wo_projectId = localStorage.getItem("views_project_id") ;
var scopeID = 0;

//var wo_projectId2 = 1262;


$(document).ready(function () {
    console.log("Load function called");
    getServiceAreaDetails();
    getDomain();
    getTechnologies();
    getProjectName();
    getUser();
    getPriority();
    getWCNodeType();



    if (wo_projectId > 0) {
        localStorage.setItem("projectId", 0);
    }
});




function getServiceAreaDetails() {

    $.isf.ajax({

        url: service_java_URL + "activityMaster/getServiceAreaDetails",
        success: function (data) {

            $.each(data, function (i, d) {
                $('#select_product_area').append('<option value="' + d.serviceAreaID + '">' + d.serviceArea + '</option>');
            })

        },
        error: function (xhr, status, statusText) {
            console.log('An error occurred on getServiceAreaDetails: ' + xhr.error);
        }
    });

}

function getDomain() {

    $.isf.ajax({

        url: service_java_URL + "activityMaster/getDomainDetails",
        success: function (data) {

            $.each(data, function (i, d) {
                $('#woc_select_domain').append('<option value="' + d.domainID + '">' + d.domain + '</option>');
            })
            
        },
        error: function (xhr, status, statusText) {
            console.log('An error occurred on getDomain: ' + xhr.error);
        }
    });

}

function getTechnologies() {

    $.isf.ajax({
        url: service_java_URL + "activityMaster/getTechnologyDetails",
        success: function (data) {

            $.each(data, function (i, d) {
                $('#woc_select_technology').append('<option value="' + d.technologyID + '">' + d.technology + '</option>');
            })

        },
        error: function (xhr, status, statusText) {
            console.log('An error occurred');
        }
    });

}

function getActivitiesSub() {
    var domainID = document.getElementById("woc_select_domain").value;
    var serviceAreaID = document.getElementById("select_product_area").value;
    var technologyID = document.getElementById("woc_select_technology").value;
    //alert(domainID + " / " + serviceAreaID + " / "+technologyID)
    $.isf.ajax({
        url: service_java_URL + "activityMaster/getActivityAndSubActivityDetails/" + domainID + "/" + serviceAreaID + "/" + technologyID,
        success: function (data) {

            $.each(data, function (i, d) {
                $('#woc_select_activitysub').append('<option value="' + d.subActivityID + '">' + d.subActivityName + '</option>');
                $('#select_activity').append('<option value="' + d.activity + '">' + d.activity + '</option>');
            })

        },
        error: function (xhr, status, statusText) {
            console.log('An error occurred');
        }
    });

}

function getProjectName() {

    $.isf.ajax({

        url: service_java_URL + "projectManagement/getProjectDetails?ProjectID=" + wo_projectId,
        success: function (data) {

            $('#select_project_name').append('<option value="' + data.projectID + '">' + data.projectName + '</option>');

        },
        error: function (xhr, status, statusText) {
            console.log('An error occurred on getProjectName: ' + xhr.error);
        }
    })

}

function getPriority() {

    $.isf.ajax({

        url: service_java_URL + "woManagement/getPriority ",
        success: function (data) {

            $.each(data, function (i, d) {
                $('#select_priority').append('<option value="' + d + '">' + d + '</option>');
            })

        },
        error: function (xhr, status, statusText) {
            console.log('An error occurred on getProjectName: ' + xhr.error);
        }
    })

}

function getUser() {

    //$.isf.ajax({

    //    url: service_java_URL + "activityMaster/getEmployeeDetails",
    //    success: function (data) {

    //        $.each(data, function (i, d) {
    //            $('#select_assign_to_user').append('<option value="' + d.signum + '">' + d.signum + " - " + d.employeeName + '</option>');
    //        })

    //    },
    //    error: function (xhr, status, statusText) {
    //        console.log('An error occurred on getEmployeeDetails: ' + xhr.error);
    //    }
    //});

}

function openModalperiodicity(value) {
    if (value == "Daily" || value == "Weekly") {
        document.getElementById("woModal1ContentDaily").style.display = "none";
        document.getElementById("woModal1ContentWeekly").style.display = "none";
        if (value == "Daily") {
            document.getElementById("woModal1ContentDaily").style.display = "inline";
        } else {
            document.getElementById("woModal1ContentWeekly").style.display = "inline";
        }

        $("#woModal1Title").html(value);
        $("#woModal1").modal("show");
    }
}

function getWCNodeType() {
    if (wo_projectId == undefined || wo_projectId == null || wo_projectId == '' || isNaN(parseInt(wo_projectId))) {
        pwIsf.alert({ msg: "Project ID missing or invalid", type: "error" });
    }
    else {
        $.isf.ajax({

            //url: service_java_URL + "woManagement/getNodeType/1262", <-- This URL is for hardcode testing
            url: service_java_URL + "woManagement/getNodeType/" + parseInt(wo_projectId),
            success: function (data) {
                $('#select_nodeType').empty();
                $.each(data, function (i, d) {
                    $('#select_nodeType').append('<option value="' + d + '">' + d + '</option>');
                })

            },
            error: function (xhr, status, statusText) {
                console.log('An error occurred on getDomain: ' + xhr.error);
            }
        });
    }


}

function getNodeNames() {
    var type = $('#select_nodeType').val();
    $.isf.ajax({

        url: service_java_URL + "woManagement/getNodeNames" + "/" + projectID + "/" + type,
        success: function (data) {
            $('#woc_select_nodeName').empty();

            for (i = 0; i < data[0].lstNodeName.length; i++) {
                $('#woc_select_nodeName').append('<option value="' + data[0].lstNodeName[i] + '">' + data[0].lstNodeName[i] + '</option>');
            }

        },
        error: function (xhr, status, statusText) {
            console.log('An error occurred on getProjectName: ' + xhr.error);
        }
    });

}


function validations() {

    var OK = true;

    $('#project_name-Required').text("");
    if ($("#select_project_name").val() == null || $("#project_name").val() == "") {
        $('#project_name-Required').text("Project name is required");
        OK = false;
    }


    $('#woc_select_domain-Required').text("");
    if ($("#woc_select_domain option:selected").val() == "0") {
        $('#woc_select_domain-Required').text("Domain/Subdomain is required");
        OK = false;
    }

    $('#select_product_area-Required').text("");
    if ($("#select_product_area option:selected").val() == "0") {
        $('#select_product_area-Required').text("Service Area / Subservice area is required");
        OK = false;
    }

    $('#woc_select_technology-Required').text("");
    if ($("#woc_select_technology option:selected").val() == "0") {
        $('#woc_select_technology-Required').text("Technology is required");
        OK = false;
    }

    $('#select_activity-Required').text("");
    if ($("#select_activity option:selected").val() == "0") {
        $('#select_activity-Required').text("Activity is required");
        OK = false;
    }

    $('#woc_select_activitysub-Required').text("");
    if ($("#woc_select_activitysub option:selected").val() == "0") {
        $('#woc_select_activitysub-Required').text("SubActivity is required");
        OK = false;
    }

    $('#select_nodeType-Required').text("");
    if ($("#select_nodeType option:selected").val() == null || $("#select_nodeType option:selected").val() == "0") {
        $('#select_nodeType-Required').text("Node Type is required");
        OK = false;
    }

    $('#woc_select_nodeName-Required').text("");
    if ($("#woc_select_nodeName option:selected").val() == null || $("#woc_select_nodeName option:selected").val() == "0") {
        $('#woc_select_nodeName-Required').text("Node Names is required");
        OK = false;
    }

    $('#WO_name-Required').text("");
    if ($("#WO_name").val() == null || $("#WO_name").val() == "") {
        $('#WO_name-Required').text("WorkOrder Name name is required");
        OK = false;
    }

    $('#select_periodicity-Required').text("");
    if ($("#select_periodicity option:selected").val() == "0") {
        $('#select_periodicity-Required').text("Periodicity is required");
        OK = false;
    }

    $('#select_assign_to_user-Required').text("");
    if ($("#select_assign_to_user option:selected").val() == "0") {
        $('#select_assign_to_user-Required').text("Assign to User is required");
        OK = false;
    }

    $('#select_priority-Required').text("");
    if ($("#select_priority option:selected").val() == "0") {
        $('#select_priority-Required').text("Priority is required");
        OK = false;
    }

    $('#Start_Date-Required').text("");
    if ($("#Start_Date").val() == null || $("#Start_Date").val() == "") {
        $('#Start_Date-Required').text("Start date is required");
        OK = false;
    } else if ($("#Start_Date").val() > $("#End_Date").val()) {
        $('#Start_Date-Required').text("Start Time is greater than End Date");
        OK = false;
    }

    $('#start_time-Required').text("");
    if ($("#start_time option:selected").val() == "0") {
        $('#start_time-Required').text("Start Time is required");
        OK = false;
    } 

    $('#End_Date-Required').text("");
    if ($("#End_Date").val() == null || $("#Start_Date").val() == "") {
        $('#End_Date-Required').text("End date is required");
        OK = false;
    }

    
    
    
    return true;


}


function createWorkOrder() {
    
    if (validations()) {

        var periodicity = $("#select_periodicity").val();
        var periodicityDaily = null;
        var periodicityWeekly = null;
        cadena = [];
        if (periodicity == "Daily") {
            periodicityDaily = "";
            $('#checkbox_daily:checked').each(
                function () {
                    cadena.push($(this).val());
                }
            );
            periodicityDaily = cadena.join(",")
        } else if (periodicity == "Weekly") {
            periodicityWeekly = $("input[name='radio_weekly']:checked").val();
            console.log(periodicityWeekly);
        }


        var WO = new Object();
        WO.projectID = wo_projectId;
        WO.scopeID = scopeID;
        WO.wOPlanID = 1;
        WO.subActivityID = $("#woc_select_activitysub").val();
        WO.priority = $("#select_priority").val();
        WO.periodicityDaily = periodicityDaily;
        WO.periodicityWeekly = periodicityWeekly;

        WO.startDate = $("#Start_Date").val();
        WO.startTime = $("#start_time").val();
        WO.endDate = $("#End_Date").val();

        WO.wOName = $("#WO_name").val();

        WO.signumID = $("#select_assign_to_user").val();
        WO.lastModifiedBy = signumGlobal;
        WO.createdBy = signumGlobal;
        var nodeNameArr = $("#woc_select_nodeName").val();
        var nodeName = nodeNameArr.join();
        console.log("nodeName " + nodeName);
        var nodeType = $("#select_nodeType").val();
        var listOfNodeJson = {"nodeNames": nodeName, "nodeType": nodeType}
        var listOfNode = [];
        listOfNode.push(listOfNodeJson);
        //console.log(JSON.stringify(WO));
        console.log(nodeType + ',' + nodeName + ',' + JSON.stringify(listOfNodeJson) + ',' + listOfNode[0] + ',' + listOfNode[1]);
        WO.listOfNode = listOfNode;
        console.log("The final result for call " + JSON.stringify(WO));
        $.isf.ajax({
            url: service_java_URL + "woManagement/createWorkOrderPlan",
            context: this,
            crossdomain: true,
            processData: true,
            contentType: 'application/json',
            type: 'POST',
            data: JSON.stringify(WO),
            xhrFields: {
                withCredentials: false
            },
            success: function (data) {
                //var result = $(data).find("string").text();
                alert("DATA SUCCESSFULLY SAVED");             
                $('#WOmodal2').modal('show');
                

            },
            error: function (xhr, status, statusText) {
                alert("DATA CAN NOT BE SAVED");
                console.log('An error occurred on : createWO' + xhr.error);
            }
        });

    }
}

