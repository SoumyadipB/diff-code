var knownIssueOrBugs = "https://support.integratedserviceflow.ericsson.net/support/solutions/15000045407";
$(document).ready(function () {
    // $.protip();
    $('.tree').treegrid({
        expanderExpandedClass: 'glyphicon glyphicon-minus',
        expanderCollapsedClass: 'glyphicon glyphicon-plus'
    })

    $(".checkKnownIssueOrBugsLink").attr('href', knownIssueOrBugs);   //Assigning the URL from constant 'knownIssueOrBugs' to 'knownIssueOrBugs href referred by class 'checkKnownIssueOrBugsLink'
    if (knownIssueOrBugs === "")
    {                                   //checking if the value of constant "knownIssueOrBugs" defined in index.js is empty or not
        
        
        $('.listKnownIssueOrBugs').css("display", "none");
        $('.checkKnownIssueOrBugsLink').css("display", "none");    //if the URL is empty,the placeholder or icon is not displayed
    }




        



    var activeProfileObj = JSON.parse(ActiveProfileSession);
    var roleID = activeProfileObj.roleID;
    var role = activeProfileObj.role.toLowerCase();

    if (roleID == 5 || roleID == 1 || roleID == 4 || roleID == 11 || role == "authorized service professional delivery responsible") {
        $('#PMDR').show();
        $('#Everyone').hide();
    } else {
        $('#PMDR').hide();
        $('#Everyone').show();
    }
    var serviceUrl = service_java_URL + "projectManagement/getProjectAndScopeDetailBySignum?signum=" + signumGlobal + "&role=" + role;
    if (ApiProxy == true) {
        serviceUrl = service_java_URL + "projectManagement/getProjectAndScopeDetailBySignum?signum=" + signumGlobal + encodeURIComponent("&role=" + role);
    }
    pwIsf.addLayer({ text: "Please wait ..." });
    $.isf.ajax({
        async: true,
        url: serviceUrl,
        success: function (data) {
            pwIsf.removeLayer();
                var row = "";
                var curParentId = 0;
                var curTrNumber = 0;
                row += '<tr><th></th><th>Project</th><th></th><th>Actions</th></tr>';
                $.each(data, function (i, d) {
                    curTrNumber = curTrNumber + 1;
                    curParentId = curTrNumber;
                    row += '<tr project_type="' + d.projectType + '"class="treegrid-' + curTrNumber + '">';
                    row += "<td> </td>";
                    row += "<td> " + d.projectID + "</td>";
                    row += "<td>" + d.projectName;
                    row += "</td>";
                    if (roleID == 4 || roleID == 11) {
                        if (d.scopeDetails.length == 0) {
                            row += '<td > <div style="display:flex;"  ><a class="icon-view lsp" title="View Project details" href="#" onclick=ViewProjectDetail(' + d.projectID + ') ><i class="fa fa-eye"></i></a></td></div>';
                        }
                        else {
                            row += '<td > <div style="display:flex;" ><a class="icon-view lsp" title="View Project details" href="#" onclick=ViewProjectDetail(' + d.projectID + ') ><i class="fa fa-eye"></i></a></td></div>';
                            curTrNumber = curTrNumber + 1;
                            row += '<tr class="treegrid-' + curTrNumber + ' treegrid-parent-' + curParentId + '"><th></th><th>Deliverable</th><th></th></tr>';
                        }
                    }
                    else {

                        if (d.scopeDetails.length == 0) {
                            row += '<td > <div style="display:flex;" ><a class="icon-view lsp" title="View Project details" href="#" onclick=ViewProjectDetail(' + d.projectID + ') ><i class="fa fa-eye"></i></a><a class="icon-view" title="Create Deliverable Order" style="pointer-events: none;">DO' + getIcon('add') + '</a></td></div>';

                        }
                        else {
                            var exeFlag = false;
                            $.each(d.scopeDetails, function (j, c) {
                                let executionPlan = c.haveExecutionPlan;
                                if (executionPlan == true) {
                                    exeFlag = true;
                                }
                            });
                            if (exeFlag == false) {

                                row += '<td > <div style="display:flex;" ><a class="icon-view lsp" title="View Project details" href="#" onclick=ViewProjectDetail(' + d.projectID + ') ><i class="fa fa-eye"></i></a><a class="icon-view" title="Create Deliverable Order Plan" style="pointer-events: none;">DO' + getIcon('add') + '</a></td></div>';
                            }
                            else {
                                row += '<td > <div style="display:flex;" ><a class="icon-view lsp" title="View Project details" href="#" onclick=ViewProjectDetail(' + d.projectID + ') ><i class="fa fa-eye"></i></a><a class="icon-add" title="Create Deliverable Order Plan" href="#" onclick=CreateWorkOrderModelPopUp($(this).closest("tr")) >DO' + getIcon('add') + '</a></td></div>';
                            }
                            curTrNumber = curTrNumber + 1;
                            row += '<tr class="treegrid-' + curTrNumber + ' treegrid-parent-' + curParentId + '"><th></th><th>Deliverable</th><th></th><th>Actions</th></tr>';
                        }
                    }
                    row += "</tr>";

                    $.each(d.scopeDetails, function (j, c) {

                        curTrNumber = curTrNumber + 1;
                        row += '<tr project_type="' + d.projectType + '" class="treegrid-' + curTrNumber + ' treegrid-parent-' + curParentId + '" style="border-bottom:1px solid; border-color:lightgray;" >';
                        row += "<td> </td>";
                        row += "<td>" + c.scopeID + "</td>";
                        row += "<td>" + c.scopeName;
                        row += "</td>";
                        if (roleID == 4 || roleID == 11) {
                            //row += '<td><div style="display: flex" ><a class="icon-add" title="Create Work Order" href="#" onclick=CreateWorkOrderModelPopUpFromScope($(this).closest("tr")) >WO' + getIcon('add') + '</a></div></td>';
                        }
                        else if (c.haveExecutionPlan == false) {
                            row += '<td><div style="display:flex;"><a class="icon-view" title="Create Deliveravble Order Plan" style="pointer-events: none;">DO' + getIcon('add') + '</a></div></td>';
                        }
                        else {
                            row += '<td><div style="display:flex;"><a class="icon-add" title="Create Deliveravble Order Plan" href="#" onclick=CreateWorkOrderModelPopUpFromScope($(this).closest("tr")) >DO' + getIcon('add') + '</a></div></td>';
                        }
                        row += "</tr>";

                    });
                });

                $('#tableProjectScopeDetail').append(row);
                $('#tableProjectScopeDetail').treegrid();
                $('#tableProjectScopeDetail').treegrid('collapseAll');
        },
        error: function (xhr, status, statusText) {
            pwIsf.removeLayer();
            console.log('An error occurred on getProjectDetails: ' + xhr.error);
        }
    })

    $(".createWOPopUp").hide();
});


function ViewProjectDetail(rowValue) {
    localStorage.setItem("views_project_id", rowValue);
    window.location.href = "./Project/ViewProject";
}
function CreateWorkOrderModelPopUp(rowValue) {
    localStorage.setItem("projectType", rowValue.attr("project_type"));
   $('#emptysignum-message').hide();
    $("#selectallSignumdiv").hide();
    $('#selectUserAllSignum').val('');
    $("#assignUserDiv").css("display", "none");
    $(".recurrenceOrder").css("display", "none");
    $("#slaHrsDiv").css("display", "none");
    $("#planEndArea").css("display", "none");
    $("#slaHrs").val("");
    $("#signumbyprojectdiv").show();
    localStorage.setItem("views_scope_id", null);
    var projectID = rowValue.find("td:nth-child(2)")[0].innerText;
    localStorage.setItem("views_project_id", projectID);
    workOrderDisplayPopUpModal(projectID, null);
}
function CreateWorkOrderModelPopUpFromScope(rowValue) {
    $("#selectallSignumdiv").hide();
    $("#signumbyprojectdiv").show();
    var scopeId = rowValue.find("td:nth-child(2)")[0].innerText;
    localStorage.setItem("projectType", rowValue.attr("project_type"));
    localStorage.setItem("views_scope_id", scopeId);
    var parentClass = GetParentId(rowValue);
    rowValue = $("." + parentClass);
    var projectID = rowValue.find("td:nth-child(2)")[0].innerText;
    localStorage.setItem("views_project_id", projectID);
    workOrderDisplayPopUpModal(projectID, scopeId);
}
function GetParentId(rowValue) {
    var childClass = rowValue[0].className;
    childClass = childClass.substr(childClass.lastIndexOf("-"), childClass.length);
    return "treegrid" + childClass;
}
