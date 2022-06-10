//$(document).ready(function () {
//    getApprovalRequestBySignum();    
//});


//var login_signum = signumGlobal /*"ebhjpra"*/;

function selectAll() {
    for (i = 0; i < document.f1.elements.length; i++) { 
        if (document.f1.elements[i].type == "checkbox") { 
            
            if (document.f1.elements[i].checked == 1) {
                document.f1.elements[i].checked = 0
            }
            else {
                document.f1.elements[i].checked = 1
            }
        }
    }
}

function OpenModalApprove() {
    var approve = false;
    for (i = 0; i < document.f1.elements.length; i++) {
        if (document.f1.elements[i].type == "checkbox") {
            if (document.f1.elements[i].checked == 1) {
                approve = true;
            }
        }
    }
    if (approve)
        $('#myModal2').modal('show');
    else
        $('#myModal5').modal('show');
}
function OpenModalReject() {
    var reject = false;
    for (i = 0; i < document.f1.elements.length; i++) {
        if (document.f1.elements[i].type == "checkbox") {
            if (document.f1.elements[i].checked == 1) {
                reject = true;
            }
        }
    }
    if (reject)
        $('#myModal3').modal('show');
    else
        $('#myModal5').modal('show');
}

function getApprovalRequestBySignum() {
    //let service_java_URL = "http://169.144.23.223:8080/isf-rest-server-java/";
    //console.log(service_java_URL + "activityMaster/getProjectApprovalsByApprover/" + login_signum);
    $.isf.ajax({
        url: service_java_URL + "activityMaster/getProjectApprovalsByApprover/" + login_signum,
        success: function (data) {

            var row = "";

            $.each(data, function (i, d) {
                if (d.approvedOrRejected != true && d.approvedOrRejected != false && d.status != "Closed")
                {
                    row += "<tr id='trProjectID" + d.projectId + "'>";
                    //row += "<td><label class='checkbox'><input type='checkbox'> </label> </td>";
                    row += "<td> <input type='checkbox'>   </td>";

                
                    row += "<td>" + d.projectId;
                    row += "</td>";
                    row += "<td>" + d.approverSignum;
                    row += "</td>";
                    row += "<td>" + d.requestedBy;
                    row += "</td>";
                    row += "<td>" + d.requestedOn;
                    row += "</td>";
                

                    row += "</tr>";
                }
            });

            $("#table_lm_approval").append(row);

        },
        error: function (xhr, status, statusText) {
            console.log('An error occurred : ' + xhr.responseText);
            pwIsf.alert({ msg: 'No project to be approved by the user', type: 'error' });
        }
    });

}

function updateProjectApprovalDetails(approveReject) {

    var table = document.getElementById('table_lm_approval');
    var arrayProjectID = "";
    for (r = table.rows.length-1; r > 0; r--) {
    
        var row = table.rows[r];
        var selected = row.cells[0].children[0].checked;
        var projectId = row.cells[1].innerHTML;
       
        if (selected) {
         //   alert(r + " Project " + projectId);
            sendRequest(approveReject, projectId);
       
        }
    }
  
    if (approveReject) {
        $('#modal_result').text('Your projects have been approved.');
    } else {
        $('#modal_result').text('Your projects have been rejected.');
    }

    //getApprovalRequestBySignum();    
}

function sendRequest(approveReject, projectId){

    var project = new Object();
    project.approvedOrRejected = approveReject;
    project.projectId = projectId;
 
   
    $.isf.ajax({
        url: service_java_URL + "activityMaster/updateProjectApprovalDetails/",
        context: this,
        crossdomain: true,
        processData: true,
        async: false,
        contentType: 'application/json',
        type: 'POST',
        data: JSON.stringify(project),
        xhrFields: {
            withCredentials: false
        },
        success: function (data) {
            //var result = (new XMLSerializer()).serializeToString(data);            
            //alert(data);
            $("#trProjectID" + projectId).remove();
            if (approveReject == false) {
                var _$promise = obj.disableProject(service_java_URL + "projectManagement/deleteProjectComponents", projectId, signumGlobal);
            }
           
        },
        error: function (xhr, status, statusText) {
            console.log('An error occurred : ' + xhr.responseText);
        }
    })

}

var obj = {};
obj.disableProject = function (url, _id, signumGlobal) {
    var _project = "REJECT_PROJECT";
    var param = "{\"projectID\":" + _id + ",\"type\":\"" + _project + "\",\"loggedInUser\":\"" + signumGlobal + "\"}";
    return $.isf.ajax({
        method: 'POST',
        data: param,
        context: this,
        crossDomain: true,
        processData: true,
        contentType: 'application/json',
        url: url,
        error: function (xhr, status, statusText) {
            alert("Fail " + xhr.responseText);
            //alert("status " + xhr.status);
        },
        xhrFields: { withCredentials: false }
    })
}
