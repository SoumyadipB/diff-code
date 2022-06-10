//Global Variable---
var start;
var servAreaID;
var select_product_area = "";
var isProjectManager = false;
var isProjectNameChanged = false;
var project = new Object();
var isPmChanged = false;
var isCpmChanged = false;
var isOmChanged = false;
var isStartDateChanged = false;
var isEndDateChanged = false;
var isDescChanged = false;
var isStatusChanged = false;

var isDocChanged = false;
var isRpmChanged = false;
var omValue;
var roleType = JSON.parse(UserDetailsSession)[0]['lstAccessProfileModel'][0]['role'];

var activeProfileObj = JSON.parse(ActiveProfileSession);
var roleID = activeProfileObj.roleID;
var role = activeProfileObj.role;

//Globals variables for View Project
const VPData = {};

$(document).ready(function (event) {
    getSharePointUrl();
    getProjectDetails(event);
    fillDocumentType();
    if (localStorage.getItem("projectType") === "ASP") {
        $("#liMenu1").remove();
    }
    $('#button_edit_save').hide();
    $('#button_cancel').hide();

    var ProjectID = localStorage.getItem("views_project_id");
    $("#projectID").text(ProjectID);

    if (role !== "Project Manager") {
        $('.icon-edit').hide();
        $('.icon-add').hide();
        $('.icon-delete').hide();
    }

    $('#project_name').title = $('#project_name').val();

    if ($('#input_Status').val().toUpperCase() === "CLOSED") {
        $('#divEditStatus').hide();
    }
});

if (roleType === "Project Manager") {
    isProjectManager = true;
}

$(document).on('click', '.panel-heading span.clickable', function (e) {
    var $this = $(this);
    if (!$this.hasClass('panel-collapse')) {
        $this.parents('.panel').find('.panel-body').slideDown();
        $this.addClass('panel-collapse');
        $this.find('i').removeClass('fa fa-angle-down').addClass('fa fa-angle-up');
    } else {
        $this.parents('.panel').find('.project-details').slideUp();
        $this.removeClass('panel-collapse');
        $this.find('i').removeClass('fa fa-angle-up').addClass('fa fa-angle-down');
    }
})

function clearWOPlanState() {
    closeAddDeliverableDiv();
    if ((typeof oTableWOPlan != "undefined") && (typeof oTableWOPlanWO != "undefined")) {
        var state1 = oTableWOPlan.state();
        if (state1) {
            oTableWOPlan.state.clear();
            oTableWOPlan.destroy();
        }
        var state2 = oTableWOPlanWO.state();
        if (state2) {
            oTableWOPlanWO.state.clear();
            oTableWOPlanWO.destroy();
        }
    }
}

function vp_disableButtons() {

    $('#vp_cpm').attr({
        'disabled': 'disabled'
    });
    $('#vp_project_description').attr({
        'disabled': 'disabled'
    });
    $('#vp_start_date').attr({
        'disabled': 'disabled'
    });
    $('#vp_end_date').attr({
        'disabled': 'disabled'
    });

    $('#vp_select_market_area').attr({
        'disabled': 'disabled'
    });
    $('#vp_select_customer_name').attr({
        'disabled': 'disabled'
    });
    $('#vp_select_country').attr({
        'disabled': 'disabled'
    });
    $('#vp_select_demand_owning_company').attr({
        'disabled': 'disabled'
    });

    $('#vp_select_Project_Type').attr({
        'disabled': 'disabled'
    });
    $('#vp_select_product_area').attr({
        'disabled': 'disabled'
    });
    $('#vp_select_opportunity_name').attr({
        'disabled': 'disabled'
    });

}

function vp_enableButtons() {
    $('#vp_cpm').removeAttr('disabled');
    $('#vp_project_description').removeAttr('disabled');
    $('#vp_start_date').removeAttr('disabled');
    $('#vp_end_date').removeAttr('disabled');

    $('#vp_select_market_area').removeAttr('disabled');
    $('#vp_select_customer_name').removeAttr('disabled');
    $('#vp_select_country').removeAttr('disabled');
    $('#vp_select_demand_owning_company').removeAttr('disabled');

    $('#vp_select_Project_Type').removeAttr('disabled');
    $('#vp_select_product_area').removeAttr('disabled');
    $('#vp_select_opportunity_name').removeAttr('disabled');
}

function vp_editProject() {
    $('#button_edit_save').show();
    $('#button_cancel').show();
    vp_enableButtons();

}

function fillProjectType() {
    $('#vp_select_Project_Type').append('<option value="Forecast">Forecast</option>');
    $('#vp_select_Project_Type').append('<option value="Pre - Sales">Pre - Sales</option>');
    $('#vp_select_Project_Type').append('<option value="Demand">Demand</option>');
    $('#vp_select_Project_Type').append('<option value="Non GTT">Non GTT</option>');
}

function getServiceAreaDetails2() {
    $("#select_product_area").empty();
    var pId = localStorage.getItem("views_project_id");
    if (pId == undefined || pId == null || pId == '' || isNaN(parseInt(pId))) {
        pwIsf.alert({ msg: 'Project Id is wrong', type: 'error' });
    }
    else {
        $.isf.ajax({

            url: service_java_URL + "activityMaster/getServiceAreaDetailsByProject?projectID=" + parseInt(pId),
            success: function (data) {

                $.each(data, function (i, d) {
                    $('#select_product_area').append('<option value="' + d.ServAreaID + '">' + d.serviceArea + '</option>');
                })

            },
            error: function (xhr, status, statusText) {
                console.log('An error occurred on getServiceAreaDetails: ' + xhr.error);
            }
        });
    }

}

function getOpportunities() {
    $.isf.ajax({
        url: service_java_URL + "projectManagement/getOpportunities",
        success: function (data) {

            $.each(data, function (i, d) {
                $('#vp_select_opportunity_name').append('<option value="' + d.OpportunityID + '">' + d.OpportunityName + '</option>');
            })

        },
        error: function (xhr, status, statusText) {
            console.log('An error occurred on getOpportunities');
        }
    });

}

function getMarketAreas() {

    $.isf.ajax({
        url: service_java_URL + "projectManagement/getMarketAreas",
        success: function (data) {

            $.each(data, function (i, d) {
                $('#vp_select_market_area').append('<option value="' + d.MarketAreaID + '">' + d.MarketAreaName + '</option>');
            })

        },
        error: function (xhr, status, statusText) {
            console.log('An error occurred on getMarketAreas');
        }
    });

}

function getCountries() {

    $.isf.ajax({
        url: service_java_URL + "activityMaster/getCountries",
        success: function (data) {

            $.each(data.responseData, function (i, d) {
                $('#vp_select_country').append('<option value="' + d.countryID + '">' + d.countryName + '</option>');
            })
            console.log("Calling service getCountries.");
        },
        error: function (xhr, status, statusText) {
            console.log('An error occurred on getCountries');
        }
    });

}

function getCustomers() {
    $.isf.ajax({
        url: service_java_URL + "projectManagement/getCustomers?countryID=",
        success: function (data) {

            $.each(data, function (i, d) {
                if (i < 50) {
                    $('#vp_select_customer_name').append('<option value="' + d.CustomerID + '">' + d.CustomerName + '</option>');
                }
            })
        },
        error: function (xhr, status, statusText) {
            console.log('An error occurred on getCustomers' + xhr.responseText);
        }
    });

}

function getDemandOwningCompanies() {
    $.isf.ajax({
        url: service_java_URL + "projectManagement/getDemandOwningCompanies",
        success: function (data) {

            $.each(data, function (i, d) {
                $('#vp_select_demand_owning_company').append('<option value="' + d.CompanyID + '">' + d.CompanyName + '</option>');
            })

        },
        error: function (xhr, status, statusText) {
            console.log('An error occurred');
        }
    });

}


function getPosition(string, subString, index) {
    return string.split(subString, index).join(subString).length;
}

function getProjectDetails(e) {
    localStorage.setItem("IsProjectTabsAccessible", 0);
    var pId = localStorage.getItem("views_project_id")
    // in case of missing project id, redirect to Home Page.
    if (pId == undefined || pId == null || pId == '' || isNaN(parseInt(pId))) {
        window.location.href = UiRootDir + "/Home";
        e.preventDefault()       
    }
    else {
        $.isf.ajax({
            async: false,
            url: service_java_URL + "projectManagement/getProjectDetails?ProjectID=" + parseInt(pId),
            success: function (data) {
                localStorage.setItem("StartDate", data.startDate);
                localStorage.setItem("projectType", data.projectType);
                localStorage.setItem("projCreater", data.projectCreator);

                if (data.projectCreator != null || data.cPM != null || data.createdBy != null) {
                    if (((signumGlobal.toLowerCase() === data.projectCreator.toLowerCase()) ||
                        (signumGlobal.toLowerCase() === data.cPM.toLowerCase()) ||
                        (signumGlobal.toLowerCase() === data.createdBy.toLowerCase()))
                        && role !== "Resource Planning Manager" && data.status !== "Closed") {
                        $("#nav_barProjectView").attr('style', '');
                        $("#home").attr('style', '');

                        localStorage.setItem("IsProjectTabsAccessible", 1);
                    }
                    else if (data.status === "Closed") {
                        $('.icon-edit').hide();
                        $('.icon-add').hide();
                        $('.icon-delete').hide();
                        $("#manage_proj").hide();
                        $("#project_others").hide();
                        $("nav_barProjectView").hide();
                        $("#home").hide();
                        activaTab('project_view');
                        $("#select_document_type").prop('disabled', true);
                        $("#input_document_url").attr('disabled', 'disabled');
                    }
                    else if (role === "Delivery Responsible") {
                        $("#nav_barProjectView").attr('style', '');
                        $("#home").attr('style', '');
                        localStorage.setItem("IsProjectTabsAccessible", 1);
                        $("#select_document_type").prop('disabled', true);
                        $("#input_document_url").attr('disabled', 'disabled');
                    }
                    else {
                        $('.icon-edit').hide();
                        $('.icon-add').hide();
                        $('.icon-delete').hide();
                        $("#project_others").show();
                        $("nav_barProjectView").hide();
                        $("#home").hide();
                        activaTab('project_view');


                    }

                }

                if (role === "Resource Planning Manager") {

                    activaTab('project_others');
                    $("#nav_barProjectView").attr('style', '');
                    $("#select_document_type").prop('disabled', true);
                    $("#input_document_url").attr('disabled', 'disabled');
                    $("#liMenuProjectScope").hide();
                    $("#liMenu0").hide();
                    $("#liMenu2").hide();
                    $("#liMenu3").hide();
                    $("#liMenu4").hide();
                    $("#liMenu5").hide();
                    $($("#liMenu1").find('a')).click();
                    localStorage.setItem("IsProjectTabsAccessible", 1);
                }
                else if (roleID == 4) {
                    $("#manage_proj").hide();
                    activaTab('project_view');
                    $("#select_document_type").prop('disabled', true);
                    $("#input_document_url").attr('disabled', 'disabled');
                }
                var n = getPosition(data.projectName, "_", 1) + 1;
                var ma = data.projectName.substring(0, n);
                var remainingName = data.projectName.substring(n);
                $("#project_name2").text(ma);
                $("#projectName").val(remainingName);

                $("#project_name").val(data.projectName);
                $("#project_creator").val(data.createdBy);
                $("#project_manager").val(data.projectCreator);
                $("#cpm").val(data.cPM);
                $("#operational_manager").val(data.operationalManager);
                $("#Project_Type").val(data.projectType);

                $('#project_code').val(data.pCode);
                $('#textRpm').val(data.rPM);

                //Start Date Assignment

                var now = new Date(data.startDate);
                var day = ("0" + now.getDate()).slice(-2);
                var month = ("0" + (now.getMonth() + 1)).slice(-2);
                var today = now.getFullYear() + "-" + (month) + "-" + (day);

                $("#project_view_date_start").val(today);
                // End Date Assignment
                now = new Date(data.endDate);
                day = ("0" + now.getDate()).slice(-2);
                month = ("0" + (now.getMonth() + 1)).slice(-2);

                today = now.getFullYear() + "-" + (month) + "-" + (day);

                $("#project_view_date_end").val(today);


                $("#project_description").val(data.projectDescription);
                $("#input_Status").val(data.status);
                if (data.opportunityDetails != null) {
                    $("#select_opportunity_name").val(data.opportunityDetails.opportunityName);
                    $("#select_market_area").val(data.opportunityDetails.marketAreaName);
                    $("#select_country").val(data.opportunityDetails.countryName);
                    $("#select_demand_owning_company").val(data.opportunityDetails.companyName);
                    $("#select_customer_name").val(data.opportunityDetails.customerName);
                }


                if (data.startDate != null) {
                    start = (data.startDate).split("/")[2] + "-" + (data.startDate).split("/")[0] + "-" + (data.startDate).split("/")[1];
                }

                servAreaID = data.productAreaID;
                localStorage.setItem("ProjectServiceAreaID", data.productAreaID);
                var row = "";
                var anchor = '<td><a class="icon-delete" title="Delete Document" onclick="DeleteDocumentRow(this)">' + getIcon('delete') + '</a>';
                $.each(data.projectDocuments, function (i, d) {

                    row += "<tr>";


                    row += "<td class='did' style='visibility:hidden'>" + d.projectDocumentID;
                    row += "</td>";

                    row += "<td class='type'>" + d.documentType;
                    row += "</td>";


                    row += "<td class='link'><a href=" + d.documentLinks + ">" + d.documentLinks;
                    row += "</a></td>";
                    if (data.status !== "Closed" && role === "Project Manager") {
                        row += anchor;
                    }

                    row += "</td>";
                });
                $("#table_project_documents td").remove();
                $("#table_project_documents").append(row);

                getServiceArea();


            },
            error: function (xhr, status, statusText) {
                console.log('An error occurred on getProjectDetails: ' + xhr.error);
            }
        });
    }
}


function getServiceArea() {
    var dataServiceArea = "";

    var pId = localStorage.getItem("views_project_id");

    if (pId === undefined || pId === null || pId === '' || isNaN(parseInt(pId))) {
        pwIsf.alert({ msg: 'Wrong Project Id', type: 'error' });
        window.location.href = UiRootDir + "/Home";
        e.preventDefault()
       
    }
    else {
        $.isf.ajax({

            url: service_java_URL + "activityMaster/getServiceAreaDetailsByProject?projectID=" + parseInt(pId),
            success: function (data) {
                dataServiceArea = data[0].serviceArea.split("/")[0];

                //document.getElementById("servArea").innerHTML = dataServiceArea//service[0];
                $("#serviceArea").val(dataServiceArea);

            },
            error: function (xhr, status, statusText) {
                console.log('An error occurred on getServiceAreaDetails: ' + xhr.error);
            }
        });
    }
 
}



function vp_ChangeStartDate() {
    $('#vp_end_date').val("");
}

function validateProject() {

    var OK = true;
    //project name
    $('#vp_project_name-Required').text("");
    if ($("#vp_project_name").val() == null || $("#vp_project_name").val() == "") {
        $('#vp_project_name-Required').text("Project name is required");
        OK = false;
    }
    //CPM
    $('#vp_cpm-Required').text("");
    if ($("#vp_cpm").val() == null || $("#vp_cpm").val() == "") {
        $('#vp_cpm-Required').text("CPM is required");
        OK = false;
    } else {
        var letterNumber = /^[0-9a-zA-Z]+$/;
        var text = $("#vp_cpm").val();

        if (!$.trim(text).match(letterNumber)) {
            $('#vp_cpm-Required').text("The field only allows letters and numbers.");
            OK = false;
        }
    }


    //Opportunity
    $('#vp_select_opportunity_name-Required').text("");
    if ($("#vp_select_opportunity_name option:selected").val() == "0") {
        $('#vp_select_opportunity_name-Required').text("Opportunity is required");
        OK = false;
    }
    //Dates 
    $('#vp_start_date-Required').text("");
    if ($("#vp_start_date").val() == null || $("#vp_start_date").val() == "") {
        $('#vp_start_date-Required').text("Start date is required");
        OK = false;
    }
   

    $('#vp_end_date-Required').text("");
    var startDate = new Date($('#vp_start_date').val());
    var endDate = new Date($('#vp_end_date').val());
    if (endDate != null && endDate != "" && startDate > endDate) {
        $('#vp_end_date-Required').text("End date must be after Start date");
        OK = false;
    }



    //Project Type
    $('#vp_select_Project_Type-Required').text("");
    if ($("#vp_select_Project_Type option:selected").val() == "0") {
        $('#vp_select_Project_Type-Required').text("Project type is required");
        OK = false;
    }
    //Product Area
    $('#vp_select_product_area-Required').text("");
    if ($("#vp_select_product_area option:selected").val() == "0") {
        $('#vp_select_product_area-Required').text("Service area is required");
        OK = false;
    }
    //Project Creator
    $('#vp_project_creator-Required').text("");
    if ($("#vp_project_creator").val() == null || $("#vp_project_creator").val() == "") {
        $('#vp_project_creator-Required').text("Project creator is required");
        OK = false;
    }
    //Description
    $('#vp_project_description-Required').text("");
    if ($("#vp_project_description").val() == null || $("#vp_project_description").val() == "") {
        $('#vp_project_description-Required').text("Project description is required");
        OK = false;
    }
    //Country
    $('#vp_select_country-Required').text("");
    if ($("#vp_select_country option:selected").val() == "0") {
        $('#vp_select_country-Required').text("Country is required");
        OK = false;
    }
    //Market
    $('#vp_select_market_area-Required').text("");
    if ($("#vp_select_market_area option:selected").val() == "0") {
        $('#vp_select_market_area-Required').text("Market is required");
        OK = false;
    }
    //Customer
    $('#vp_select_customer_name-Required').text("");
    if ($("#vp_select_customer_name option:selected").val() == "0") {
        $('#vp_select_customer_name-Required').text("Customer is required");
        OK = false;
    }
    //Demand
    $('#vp_select_demand_owning_company-Required').text("");
    if ($("#vp_select_demand_owning_company option:selected").val() == "0") {
        $('#vp_select_demand_owning_company-Required').text("Demand owning company is required");
        OK = false;
    }
    return OK;
}

function vp_saveProject() {

    if (validateProject()) {
        var project = new Object();
        project.ProjectID = projectID;
        project.CPM = $("#vp_cpm").val();
        project.OpportunityID = $("#vp_select_opportunity_name").val();
        project.OpportunityName = $("#vp_select_opportunity_name option:selected").text();
        project.CountryID = $("#vp_select_country").val();
        project.CompanyID = $("#vp_select_demand_owning_company").val();
        project.CustomerID = $("#vp_select_customer_name").val();
        //MOdify ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
        project.CustomerID = "1";
        project.MarketAreaID = $("#vp_select_market_area").val();
        project.StartDate = $("#vp_start_date").val();
        project.EndDate = $("#vp_end_date").val();
        project.ProjectDescription = $("#vp_project_description").val();
        //MOdify ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
        project.ProductAreaID = $("#vp_select_product_area").val();

        $.isf.ajax({
            url: service_DotNET_URL + "ModifyProject",
            context: this,
            crossdomain: true,
            processData: true,
            contentType: 'text/plain',
            type: 'POST',
            data: JSON.stringify(project),
            xhrFields: {
                withCredentials: false
            },
            success: function (data) {

                $('#scopeModal2').modal('show');
                $('#modal_text').text("Your project was updated successfully ");

            },
            error: function (xhr, status, statusText) {
                console.log('An error occurred on : createProject' + xhr.error);
            }
        })
    } else {

        $('#validationModal').modal('show');

    }
}


function approveProject() {


    var project = new Object();
    project.approvedOrRejected = "true";
    project.projectId = projectID;


    $.isf.ajax({
        url: service_java_URL + "activityMaster/updateProjectApprovalDetails/",
        context: this,
        crossdomain: true,
        processData: true,
        contentType: 'application/json',
        type: 'POST',
        data: JSON.stringify(project),
        xhrFields: {
            withCredentials: false
        },
        success: function (data) {

            if (JSON.stringify(data) === "true") {
                $('#modal_approved_text').text('Your project was approved.');
            } else {
                $('#modal_approved_text').text('Your project could not be approved.');
            }
            $('#modal_approved').modal('show');
        },
        error: function (xhr, status, statusText) {
            $('#modal_approved').modal('show');
            $('#modal_approved_text').text('Your project could not be approved.');
            console.log('An error occurred : ' + xhr.responseText);
        }
    })

}


function getProjectViewEditOperationalManager() {
    var signumOrganisationId = JSON.parse(ActiveProfileSession).organisationID;
    var operationalRoleId = 4;
    $.isf.ajax({
        url: service_java_URL + "/accessManagement/getUserByFilter/" + operationalRoleId + "/" + signumOrganisationId,
        success: function (data) {
            var signum = '';
            var name = '';
            $('#select_operational_manager').empty();
           $('#select_operational_manager').append('<option value="0">Select One</option>');
               
            $.each(data, function (i, d) {
                signum = d.replace(/\(.*$/g, "");
                name = d;
                $('#select_operational_manager').append('<option value="' + signum + '">' + name + '</option>');

            })
            $("#select_operational_manager").val($('#operational_manager').val());
        },
        error: function (xhr, status, statusText) {
            console.log("Fail in getOperationalManager " + xhr.responseText);
            console.log('Error Occured in getOperationalManager');
        }
    });

}

function ProjectViewDataLoad() {
    getProjectDetails();
}

function UpdateProjectViewPage() {


    $('#project_manager').prop('disabled', true);
    $('#cpm').prop('disabled', true);

    if (isOmChanged) {
        var inputPm = $('#operational_manager');
        var selectOm = $('#select_operational_manager');
        inputPm.show();
        inputPm.val(selectOm.val());
        inputPm.prop('disabled', true);
        selectOm.hide();
    }
    if (isStatusChanged) {
        var inputStatus = $('#input_Status');
        var selectStatus = $('#select_Status');
        inputStatus.show();
        inputStatus.val(selectStatus.val());
        inputStatus.prop('disabled', true);
        selectStatus.hide();
    }
    

    $('#operational_manager').show();
    $('#project_view_date_start').prop('disabled', true);
    $('#project_view_date_end').prop('disabled', true);
    $('#button_save').hide();
    $('#button_cancel_edit').hide();
    $('#project_description').prop('disabled', true);
    $('#projectName').prop('disabled', true);
}
function ValidateProjectDetailEdit() {
    var validSuccess = true;
    //Project Manager
    if (isPmChanged) {
        if (checkSpm() == false) {
            validSuccess = false;

        }
        
        hideErrorMsg('pm-Required');
        if ($("#project_manager").val() == null || $("#project_manager").val() == "") {
           
            showErrorMsg('pm-Required', "Project Manager is required");
            $("#project_manager").focus();
            validSuccess = false;

        } else {
            var letterNumber = /^[a-zA-Z]+$/;
            var text = $("#project_manager").val();
            var firstChar = text.charAt(0);

            if (!$.trim(text).match(letterNumber)) {
                showErrorMsg('pm-Required', "The field only allows letters .");
                $("#project_manager").focus();
                validSuccess = false;
                
            }
            if (firstChar.toUpperCase() != 'E' || text.length != 7) {
                showErrorMsg('pm-Required', "Not a valid project manager Signum.");
                $("#project_manager").focus();
                validSuccess = false;
            }
        }
    }
    //CPM
    if (isCpmChanged) {
        hideErrorMsg('cpm-Required');
        if ($("#cpm").val() == null || $("#cpm").val() == "") {
            showErrorMsg('cpm-Required', 'CPM is required');
           $("#cpm").focus(); 
            validSuccess = false;
        }
    }

    //Operational Manager
    if (isOmChanged) {
        var omReqControl = $('#select_operational_manager-Required');
        omReqControl.text("");
        var omControl = $("#operational_manager");
        if (omControl.val() == null || omControl.val() == "" || omControl.val() == 0) {
            omReqControl.text("operatiobal manager is required");
            validSuccess = false;

        }
    }

    if (isStartDateChanged || isEndDateChanged) {
        //Dates 
        hideErrorMsg('project_view_date_start-Required');
        if ($("#project_view_date_start").val() == null || $("#project_view_date_start").val() == "") {
            
            showErrorMsg('project_view_date_start-Required', 'Start date is required');
            validSuccess = false;
            $("#project_view_date_start").focus();
        }


        hideErrorMsg('project_view_date_end-Required');
        if ($("#project_view_date_end").val() == null || $("#project_view_date_end").val() == "") {

            showErrorMsg('project_view_date_end-Required', 'End date is required');
            validSuccess = false;
            $("#project_view_date_end").focus();
        }

       
        var startDate = new Date($('#project_view_date_start').val());
        var endDate = new Date($('#project_view_date_end').val());
        if (endDate != null && endDate != "" && startDate > endDate) {
           
            showErrorMsg('project_view_date_end-Required', 'End date must be after start date');
            validSuccess = false;
            $("#project_view_date_end").focus();
        }

    }
    if (isDescChanged) {
        

        hideErrorMsg('project_description-Required');
        if ($("#project_description").val() == null || $("#project_description").val() == "") {
            showErrorMsg('project_description-Required', 'Description is required');
            $("#project_description").focus();
            validSuccess = false;
        }
    }

    if (isProjectNameChanged) {

        var projectManualName = $("#projectName").val();
        hideErrorMsg('project-Required');
        if (projectManualName == null || projectManualName == "") {
            showErrorMsg('project-Required', 'Project Name is required');
            $("#projectName").focus();
            validSuccess = false;
        }
        else if (blockSpecialChar(projectManualName)) {
            showErrorMsg('project-Required', 'Special character is not allowed in Project Name except space, comma(,), underscore(_), hyphen(-), ampersand(&), hash(#) and dot(.)');
            validSuccess = false;
        }
        else if ((projectManualName[projectManualName.length - 1]) === "_") {
            showErrorMsg('project-Required', 'Project Name must not contain underscore at the end!');
            validSuccess = false;
        }
        else if ((projectManualName[0]) === "_") {
            showErrorMsg('project-Required', 'Project Name must not contain double underscore!');
            validSuccess = false;
        }
        else {
            var pName = $("#project_name2").text() + $("#projectName").val();
            if (allProjects.includes(pName)) {
                showErrorMsg('project-Required', 'Project Name already exists');
                validSuccess = false;
            }

        }

    }
    return validSuccess;
}

function blockSpecialChar(e) {
    return /[^A-Za-z0-9-_#&,. ()]/.test(e);
}

function activaTab(tab) {
    $('.nav-tabs a[href="#' + tab + '"]').tab('show');
};

function OnEditProjectManager() {
    var inputPm = $('#project_manager');
    $('#button_save').show();
    $('#button_cancel_edit').show();
    $("#button_save").prop("disabled", true);

    inputPm.prop('disabled', false);
    inputPm.focus();
    isPmChanged = true;

}

function OnEditProjectName() {
    var inputPm = $('#projectName');
    $('#button_save').show();
    $('#button_cancel_edit').show();

    inputPm.prop('disabled', false);
    inputPm.focus();
    isProjectNameChanged = true;

}

function OnEditCpm() {
    var inputPm = $('#cpm');
    $('#button_save').show();
    $('#button_cancel_edit').show();
    $("#button_save").prop("disabled", true);

    inputPm.prop('disabled', false);
    inputPm.focus();
    isCpmChanged = true;
}

function OnEditOperationalManager() {
    var inputPm = $('#operational_manager');
    var selectOm = $('#select_operational_manager');
    $('#button_save').show();
    $('#button_cancel_edit').show();
    selectOm.show();
    getProjectViewEditOperationalManager();

    inputPm.hide();
    isOmChanged = true;
}
function OnEditStartDate() {
    var startDateControl = $('#project_view_date_start');
    var endDate = new Date($('#project_view_date_end').val());
    $.isf.ajax({

        url: service_java_URL + "woManagement/checkProjectEditByProjID/" + localStorage.getItem("views_project_id"),
        success: function (data) {
            var now = new Date();

            var day = ("0" + now.getDate()).slice(-2);
            var month = ("0" + (now.getMonth() + 1)).slice(-2);
            var today = now.getFullYear() + "-" + (month) + "-" + (day);
            var maxDay;
            var maxMonth;
            var maxDate;

            if (data.length == 0 || data[0].startDate == null || data[0].startDate == 'undefined') {

                startDateControl.prop('disabled', false);
                startDateControl.focus();
                startDateControl.attr("min", today);

                maxDay = ("0" + endDate.getDate()).slice(-2);
                maxMonth = ("0" + (endDate.getMonth() + 1)).slice(-2);
                maxDate = endDate.getFullYear() + "-" + (maxMonth) + "-" + (maxDay);
                 

                startDateControl.attr("max", maxDate);

                $('#button_save').show();
                $('#button_cancel_edit').show();
                isStartDateChanged = true;
            }
            else {
                var minStartDate = new Date(data[0].startDate).toLocaleDateString();
                var minWoStartDate = new Date(minStartDate);
                
                if (minWoStartDate > now) {
                    startDateControl.prop('disabled', false);
                    startDateControl.focus();
                    startDateControl.attr("min", today);

                    maxDay = ("0" + minWoStartDate.getDate()).slice(-2);
                    maxMonth = ("0" + (minWoStartDate.getMonth() + 1)).slice(-2);
                    maxDate = minWoStartDate.getFullYear() + "-" + (maxMonth) + "-" + (maxDay);
                   
                    startDateControl.attr("max", maxDate);
                   
                    $('#button_save').show();
                    $('#button_cancel_edit').show();
                    isStartDateChanged = true;
                } else {
                    pwIsf.alert({ msg: 'Start date can not be change for this project', type: 'warning' })
                }

            }


        },
        error: function (xhr, status, statusText) {
            console.log('An error occurred on getting start date: ' + xhr.error);
        }
    })


}
function OnEditEndDate() {

    var endDateControl = $('#project_view_date_end');
    $.isf.ajax({

        url: service_java_URL + "woManagement/checkProjectEditByProjID/" + localStorage.getItem("views_project_id"),
        success: function (data) {
            var now = new Date();

            var day = ("0" + now.getDate()).slice(-2);
            var month = ("0" + (now.getMonth() + 1)).slice(-2);
            var today = now.getFullYear() + "-" + (month) + "-" + (day);

            if (data.length == 0 || data[0].endDate == null || data[0].endDate == 'undefined') {

                endDateControl.prop('disabled', false);
                endDateControl.focus();
                endDateControl.attr("min", today);

                $('#button_save').show();
                $('#button_cancel_edit').show();
                isEndDateChanged = true;
            }
            else {
                var maxEndDate = new Date(data[0].endDate).toLocaleDateString();
                var maxWoEndDate = new Date(maxEndDate);
                var maxDay;
                var maxMonth;
                var maxDate;
               
                    endDateControl.prop('disabled', false);
                    endDateControl.focus();

                    maxDay = ("0" + maxWoEndDate.getDate()).slice(-2);
                    maxMonth = ("0" + (maxWoEndDate.getMonth() + 1)).slice(-2);
                    maxDate = maxWoEndDate.getFullYear() + "-" + (maxMonth) + "-" + (maxDay);
                endDateControl.attr("min", maxDate);
                   
                    $('#button_save').show();
                    $('#button_cancel_edit').show();
                    isEndDateChanged = true;
               

            }


        },
        error: function (xhr, status, statusText) {
            console.log('An error occurred on getting end date: ' + xhr.error);
        }
    })



}
function OnEditDescription() {

    $('#project_description').attr('disabled', false);
    
    $('#project_description').focus();
    $('#button_save').show();
    $('#button_cancel_edit').show();
    isDescChanged = true;
}
function OnChangeProjectViewEndDate() {
    hideErrorMsg('project_view_date_end-Required');
    var startDate = new Date($('#project_view_date_start').val());
    var endDate = new Date($('#project_view_date_end').val());
    if (startDate > endDate) {
        $('#project_view_date_end').val("");
        showErrorMsg('project_view_date_end-Required', 'End date must be after start date');
    }
}

function OnEditStatus() {
    $('#button_save').show();
    $('#button_cancel_edit').show();
   
    var selectStatus = $('#select_Status');
    var inputStatus = $('#input_Status');

    selectStatus.show();
    selectStatus.empty();
    selectStatus.append('<option value=' + inputStatus.val() + '>' + inputStatus.val() + ' </option>');
    selectStatus.append('<option value="CLosed">Closed</option>');
    selectStatus.val(inputStatus.val());
    inputStatus.hide();
    isStatusChanged = true;
}

function addProjectViewDocument() {
    isDocChanged = true;
    $('#button_save').show();
    var docType = $("#select_document_type option:selected").text();
    var docUrl = $("#input_document_url").val();
    var docFlg = false;
    if ($("#select_document_type option:selected").val() == "0") {
        showErrorMsg('select_document_type-Required', "Please select Document Type.");
        docFlg = true;
    } else {
        hideErrorMsg('select_document_type-Required');
    }
    if (
        docUrl == undefined || docUrl == ''
        || (! /^(ftp|https?):\/\//i.test(docUrl))
    ) {
        // Doc Url Error
        showErrorMsg('input_document_url-Required', "Please enter document Url.");
        docFlg = true;
    } else {
        hideErrorMsg('input_document_url-Required');
    }
    var anchor = '<td><a class="icon-delete" title="Delete Document" onclick="DeleteDocumentRow(this)">' + getIcon('delete') + '</a>';
    if (!docFlg) {
        var row = "";

        row += "<tr>";

        row += "<td class='did' style='visibility:hidden'>" + "";
        row += "</td>";

        row += "<td class='type'>" + docType;
        row += "</td>";

        row += "<td  class='link'><a href='" + docUrl + "'>" + docUrl + "</a>";
        row += "</td>";
        row += anchor;
        row += "</td>";

        $("#table_project_documents").append(row);
    }



}
function DeleteDocumentRow(row) {
    $(row).parents("tr").remove();
    isDocChanged = true;
    $('#button_save').show();
    $('#button_cancel_edit').show();
}


function checkSpm() {
    if ($("#project_manager").val() != null && $("#project_manager").val() != "") {
        var spmTxtVal = $("#project_manager").val();
        pwIsf.addLayer({ text: 'Please wait ...' });
        $.isf.ajax({
            url: service_java_URL + "accessManagement/checkEmpAsPM/" + spmTxtVal + "/" + localStorage.getItem("views_project_id"),
            success: function (data) {
                if (data.length == 0 || data[0].Role.toUpperCase() != "PROJECT MANAGER") {

                    showErrorMsg('pm-Required', 'Please enter valid SPM Signum.');
                    $('#project_manager').val("");
                    $('#project_manager').focus();
                    $("#button_save").prop("disabled", true);
                    return false;

                }                
                else {
                    hideErrorMsg('pm-Required');
                    $("#button_save").prop("disabled", false);
                    return true;
                }
            },
            error: function (xhr, status, statusText) {
                console.log('An error occurred on getSPMdetails: ' + xhr.error);
            },
            complete: function (xhr, statusText) {
                pwIsf.removeLayer();
            }
        });
    } else {
        showErrorMsg('pm-Required', 'Please enter valid SPM Signum.');
        $('#project_manager').val("");
        $('#project_manager').focus();
        return false;
    }
}

function changeProjectName() {
    var pName = $("#project_name2").text() + $("#projectName").val();
    $("#project_name").val(pName);

}


function SaveProjectDetail() {
    project.status = null;
    if (isStatusChanged && $('#select_Status').val().toUpperCase() === "CLOSED") {
        var _id = projectID;
        pwIsf.confirm({
            title: 'Close Project', msg: 'After Clicking OK Project id : ' + _id + ' along with all related details will be closed. Are you Sure?',
            'buttons': {
                'Yes': {
                    'action': function () {
                        pwIsf.addLayer();
                        $.isf.ajax({
                            url: service_java_URL + "projectManagement/closeProject/" + _id,
                            success: function (data) {
                                pwIsf.removeLayer();

                                if (data.IsApiSuccess == "true") {

                                editProjectDetail();
                                getProjectDetails();
                                pwIsf.removeLayer();
                            } else {
                                 pwIsf.alert({ msg: data.msg });
                            }

                            },
                            error: function (xhr, status, statusText) {
                                pwIsf.removeLayer();
                            }
                        })

                    }
                },
                'No': {
                    'action': function () {
                        //Empty block
                    }
                }

            }
        });

    } else {
       
        editProjectDetail();
    }

}
function editProjectDetail() {

    if (ValidateProjectDetailEdit()) {
        project.projectID = projectID;
        //Project Manager
        if (isPmChanged) {

            project.projectCreator = $('#project_manager').val();
        } else {
            project.projectCreator = null;
        }
        //CPM
        if (isCpmChanged) {
            project.cPM = $("#cpm").val();
        } else {
            project.cPM = null;
        }

        // Operational Manager
        if (isOmChanged) {
            project.operationalManager = $("#select_operational_manager option:selected").val();
            omValue = project.operationalManager;
        } else {
            project.operationalManager = null;
        }
        //start date
        if (isStartDateChanged) {
            project.startDate = $("#project_view_date_start").val();
        } else {
            project.startDate = null;
        }

        // end date
        if (isEndDateChanged) {
            project.endDate = $("#project_view_date_end").val();
        } else {
            project.endDate = null;
        }
        //Description 
        if (isDescChanged) {
            project.projectDescription = $("#project_description").val()
        } else {
            project.projectDescription = null;
        }
        //Project Name
        if (isProjectNameChanged) {
            var pName = $("#project_name2").text() + $("#projectName").val();
            project.projectName = pName;
        } else {
            project.projectName = null;
        }

        //Document 
        project.createdBy = signumGlobal;
        project.projectDocument = [];

        $('#table_project_documents tr').each(function () {
            var type = $(this).find(".type").html();
            var did = $(this).find(".did").html();
            var link = $(this).find(".link").html();
            if (type && link) {
                project.projectDocument.push({
                    "documentId": did,
                    "documentType": type,
                    "documentLinks": $(this).find(".link a").html(),
                    "createdBy": signumGlobal
                });
            }

        });

        console.log(JSON.stringify(project));
        $.isf.ajax({
            url: service_java_URL + "projectManagement/updateProject",
            context: this,
            crossdomain: true,
            processData: true,
            contentType: 'application/json',
            type: 'POST',
            data: JSON.stringify(project),
            xhrFields: {
                withCredentials: false
            },
            success: function (data) {

                pwIsf.alert({ msg: "Your project was updated successfully", type: 'success' });
                UpdateProjectViewPage();
                
            },
            error: function (xhr, status, statusText) {
                console.log('An error occurred on : Update project' + xhr.error);
            }
        })

    } else {
        logData('Validation Failed');


    }
}
var obj = {};
obj.closeProject = function (url, _id, signumGlobal) {
    url = url + _id;
    return $.isf.ajax({
        method: 'GET',
        context: this,
        crossDomain: true,
        processData: true,
        async: true,
        contentType: 'application/json',
        url: url,
        error: function (xhr, status, statusText) {
            alert("Fail " + xhr.responseText);
        },
        xhrFields: { withCredentials: false }
    });
};

function closeProject() {
    var isProjectClosed = false;
   

    return isProjectClosed;
}

//Check if Output URL for this project is configured
function checkSharePointUrl() {
    if (!VPData.sharePointUrl) {
        pwIsf.addRelativeLayer($('#home'), {
            text: `Please configure market with SharePoint!`,
            showSpin: false,
            position: 'top'
        })
    }
}

// Get sharepoint url against Market 
function getSharePointUrl() {

    let projId = localStorage.getItem("views_project_id");
    $.isf.ajax({
        url: `${service_java_URL}woExecution/validateSharepointDetailsWithMarketArea?projectID=${projId}`,
        type: "GET",
        async: false,
        success: function (data) {
            if (!data.isValidationFailed) {
                VPData.sharePointUrl = data.responseData;
            }
            else {
                responseHandler(data);
            }
        },
        error: function (data) {
            //Blank
        }
    });
    
}