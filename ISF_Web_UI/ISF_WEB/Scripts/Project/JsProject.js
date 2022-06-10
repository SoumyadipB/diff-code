
var marketAreaName = JSON.parse(ActiveProfileSession).organisation; 

function tooltipVal() {
    var x = $("#project_name").text() + $("#projectName").val();
    $("#projectName").attr("title", x);
    $("#project_name").attr("title", x);
    
};
function ChangeStartDateCreate() {
    document.getElementById('end_date').value = document.getElementById('start_date').value;
}
//function ChangeEndDateCreate() {
//    hideErrorMsg('end_date-Required');
//    var startDate = new Date($('#start_date').val());
//    var endDate = new Date($('#end_date').val());
//    if (startDate > endDate) {
//        $('#end_date').val("");
//        showErrorMsg('end_date-Required', 'End date must be after start date');
        
//        OK = false;
//    }
//}


//Fill selects without WS
function fillProjectType() {    
        $('#select_Project_Type').append('<option value="Forecast">Forecast</option>');
        $('#select_Project_Type').append('<option value="Pre - Sales">Pre - Sales</option>');
        $('#select_Project_Type').append('<option value="Demand">Demand</option>');
        $('#select_Project_Type').append('<option value="ASP">ASP</option>');
}

function fillDocumentType() {

    $('#select_document_type').append('<option value="RFP Document">RFP Document</option>');
    $('#select_document_type').append('<option value="Mail Reference">Mail Reference</option>');
    $('#select_document_type').append('<option value="Approved WLA Document">Approved WLA Document</option>');
    $('#select_document_type').append('<option value="Others">Others</option>');

}

//Fill selects with WS
function getServiceAreaDetails() {

    $.isf.ajax({

        url: service_java_URL + "activityMaster/getServAreaDetails",
        success: function (data) {

            $.each(data, function (i, d) {
                $('#select_product_area').append('<option value="' + d.servAreaID + '">' + d.serviceArea + '</option>');
            })

        },
        error: function (xhr, status, statusText) {
            console.log('An error occurred on getProjectDetails: ' + xhr.error);
        }
    });

}

function getCountries() {
    return $.ajax({
        url: service_java_URL + "activityMaster/getCountries",
        headers: commonHeadersforAllAjaxCall,
        success: function (data) {

            $.each(data.responseData, function (i, d) {
                $('#select_country').append('<option value="' + d.countryID + '">' + d.countryName + '</option>');
            })
        },
        error: function (xhr, status, statusText) {
            console.log("Fail " + xhr.responseText);
            //alert("status " + xhr.status);
            console.log('An error occurred');
        }
    });

}

function isDisabled(eleId) {
    return !!$("#"+eleId).prop('disabled');
}

function getSetPcode() {
    var serviceArea = $("#select_product_area option:selected").val();
    $.isf.ajax({
        url: service_java_URL + "resourceManagement/getSubServiceAreaPCode?serviceAreaID="+serviceArea,
        success: function (data) {
            //$.each(data, function (i, d) {
            //    $('#project_code').val(d['PCode']);
            //})
            $('#project_code').val(data.PCode);
        },
        error: function (xhr, status, statusText) {
            console.log("Fail " + xhr.responseText);
           // alert("status " + xhr.status);
            console.log('An error occurred in getSetPcode');
        }
    });
}

function getMarketAreas() {
    $.isf.ajax({
        //url: service_DotNET_URL + "MarketAreas",
        url: service_java_URL + "projectManagement/getMarketAreas",
        success: function (data) {

            $.each(data, function (i, d) {
                $('#select_market_area').append('<option value="' + d.MarketAreaID + '">' + d.MarketAreaName + '</option>');
            })
            
            $("#select_market_area option").filter(function () {
                return this.text == marketAreaName;
            }).attr('selected', true).trigger('change');

            $("#select_market_area").attr('disabled', 'disabled');
            getCountrybyMarketAreaID();
            fillProjectName();
            
        },
        error: function (xhr, status, statusText) {
            console.log("Fail " + xhr.responseText);
            //alert("status " + xhr.status);
            console.log('An error occurred');
        }
    });

}

function getCountrybyMarketAreaID() {
    var marketAreaId = document.getElementById("select_market_area").value;
    $.isf.ajax({
        url: service_java_URL + "projectManagement/getCountrybyMarketAreaID?marketAreaID=" + marketAreaId,
        success: function (data) {
            $('#select_country').empty();
            $('#select_country').append('<option value="0">Select One</option>');
            $.each(data, function (i, d) {
                $('#select_country').append('<option value="' + d.countryID + '">' + d.countryName + '</option>');
            })

        },
        error: function (xhr, status, statusText) {
            console.log("Fail " + xhr.responseText);
            //alert("status " + xhr.status);
            console.log('An error occurred');
        }
    });

}

function getDemandOwningCompanies() {
    $.isf.ajax({
        //url: service_DotNET_URL + "DemandOwningCompanies",
        url: service_java_URL + "projectManagement/getDemandOwningCompanies",
        success: function (data) {

            $.each(data, function (i, d) {
                $('#select_demand_owning_company').append('<option value="' + d.CompanyID + '">' + d.CompanyName + '</option>');
            })

        },
        error: function (xhr, status, statusText) {
            console.log("Fail " + xhr.responseText);
            //alert("status " + xhr.status);
            console.log('An error occurred');
        }
    });

}


function getOperationalManager() {
    var signumOrganisationId = JSON.parse(ActiveProfileSession).organisationID;
    //let signumOrganisationId = localStorage.getItem("signumOrganisationId");
    var operationalRoleId = 4;
    $.isf.ajax({
        url: service_java_URL + "/accessManagement/getUserByFilter/" + operationalRoleId + "/" + signumOrganisationId,
        success: function (data) {
            var signum = '';
            var name = '';
            $.each(data, function (i, d) {
                signum = d.replace(/\(.*$/g, "");
                name = d;
                $('#select_operational_manager').append('<option value="' + signum + '">' + name + '</option>');

            })
            
        },
        error: function (xhr, status, statusText) {
            console.log("Fail in getOperationalManager " + xhr.responseText);
           // alert("status " + xhr.status);
            console.log('Error Occured in getOperationalManager');
        }
    });

}

function getOpportunities() {
    $.isf.ajax({
        //url: service_DotNET_URL + "Opportunities",
        url: service_java_URL + "projectManagement/getOpportunities",
        success: function (data) {
            $.each(data, function (i, d) {
               
                $('#opportunity_code_list').append('<option data-id="' + d.OpportunityID + '" value="' + d.OpportunityCode + '">');
            })

        },
        error: function (xhr, status, statusText) {
            console.log("Fail " + xhr.responseText);
            //alert("status " + xhr.status);
            console.log('An error occurred');
        }
    });
}


function getOpportunityDetails() {
    
    var opportunityId = $("#opportunity_code_list option[value='" + $('#opportunity_code').val() + "']").attr('data-id');
    $('#opportunity_code').attr('data-opportunity-id', opportunityId);
   if (opportunityId != undefined && opportunityId > 0) {
      $("#select_country").val(0).change();
       $("#select_country").attr("disabled", false);
       $("#select_market_area").attr("disabled", "disabled");
       $("#select_customer_name").attr("disabled", false);
       $("#select_demand_owning_company").attr("disabled", false);
       $("#opportunity_name").attr("disabled", false);
       //var _promise;
        $.isf.ajax({
            //url: service_DotNET_URL + "OpportunityDetails?OpportunityID=" + opportunityId,
            url: service_java_URL + "projectManagement/getOpportunityDetailsById?opportunityID=" + opportunityId,
            success: function (data) {
                let _promise = getCountries();
                 _promise.done(function () {
                    
                    $('#select_country').find('option').each(function () {

                        $("#select_market_area").val(data.MarketAreaID);
                       
                     });
                     $("#select_country").val(data.CountryID).trigger('change');

                   //2vsn
                   let  promise = getCustomers($("#select_country").val());
                     promise.done(function () {

                         setTimeout(function () {

                             $("#select_customer_name").val(data.CustomerID).change();
                             fillProjectName();

                         }, 800);

                        
                    });
                   
                    $("#select_demand_owning_company").val(data.CompanyID).change();
                    $("#opportunity_name").val(data.OpportunityName);
                });

                
            },
            error: function (xhr, status, statusText) {
                console.log('An error occurred on getOpportunityDetails' + xhr.responseText);
            }
        });
   } else {
       if ( $('#opportunity_code').attr('data-opportunity-id') != 0 ) {
           $('#opportunity_code').attr('data-opportunity-id', "0");
          // $("#select_market_area").attr("disabled", false);
           $("#select_country").attr("disabled", false);
           $("#select_customer_name").attr("disabled", false);
           $("#select_demand_owning_company ").attr("disabled", false);
           $("#opportunity_name").attr("disabled", false);
           $("#opportunity_name").val("");
          //to enable only market area that was already selected
          // $("#select_market_area").val('0').change();
           //$("#select_market_area").trigger('change');
           $("#select_country").trigger('change');
           $("#select_customer_name").trigger('change');
           $("#select_demand_owning_company ").val('0').change();
           
       }
    }

}

function getCustomers( cntryId ) {
    $('#select_customer_name').val(0).change();


    //var url = service_DotNET_URL + "Customers";
    var url = service_java_URL + "projectManagement/getCustomers";
    if ( cntryId > 0 ) {
        url += "?countryID="+cntryId;
        
    } else {
        url += "?countryID="+99999999;  // No entry needs to be displayed
    }
    return $.ajax({
        url: url,
        headers: commonHeadersforAllAjaxCall,
        success: function (data) {
            $('#select_customer_name').empty();
            $('#select_customer_name').append('<option value="0">Select One</option>');
            $.each(data, function (i, d) {
                //if (i < 50) {
                    $('#select_customer_name').append('<option value="' + d.CustomerID + '">' + d.CustomerName + '</option>');
                //}
            })
        },
        error: function (xhr, status, statusText) {
            console.log('An error occurred on getCustomers' + xhr.responseText);
        }
    });

}

function editProject() {
    if (validateProject) {

        $.isf.ajax({

            url: service_java_URL + "projectManagement/getProjectDetails?ProjectID=" + projectID,
            success: function (data) {

                $("#project_name").text(data.projectName);
                $("#select_Project_Type").val(data.projectType);
                $("#cpm").val(data.cPM);
                $("#project_creator").val(data.projectCreator);
                $("#select_opportunity_name").val(data.opportunityDetails.opportunityID);

                //project.OpportunityName = $("#opportunity_name").val(data.OpportunityName);
                $("#select_country").val(data.opportunityDetails.countryID);


                $("#select_demand_owning_company").val(data.opportunityDetails.companyID);
                $("#select_customer_name").val(data.opportunityDetails.countryID);

                $("#select_market_area").val(data.opportunityDetails.marketAreaID);
                $("#start_date").val(data.startDate);
                $("#end_date").val(data.endDate);

                $("#project_description").val(data.projectDescription);
                $("#select_product_area").val(data.productAreaID);
                //MOdify **********************************************************************************
                $("#select_product_area").val("1");


            },
            error: function (xhr, status, statusText) {
                console.log('An error occurred on getProjectDetails: ' + xhr.error);
            }
        })
    }
}

function saveProject() {
    if (validateProject) {
        var project = new Object();
        project.ProjectID = projectID;
        project.CPM = $("#cpm").val();
        project.cpmName = $("#cpm").attr('cpmName');
        project.cpmMailId = $("#cpm").attr('cpmMailId');
        project.OpportunityID = $('#opportunity_code').attr('data-opportunity-id');
        project.OpportunityCode = $("#opportunity_code").val();
        project.OpportunityName = $("#select_opportunity_name option:selected").text();
        project.CountryID = $("#select_country").val();
        project.CompanyID = $("#select_demand_owning_company").val();
        project.CustomerID = $("#select_customer_name").val();
        project.OperationalManager = $("#select_operational_manager option:selected").val();
        project.PCode = $("#project_code").val();
        //MOdify ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
        project.CustomerID = "1";
        project.MarketAreaID = $("#select_market_area").val();
        project.StartDate = $("#start_date").val();
        project.EndDate = $("#end_date").val();
        project.ProjectDescription = $("#project_description").val();
        //MOdify ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
        project.ProductAreaID = $("#select_product_area").val();
                
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

                var result = $(data).find("string").text();
                alert("result: " + result);
                //if (result == 1) {
                $('#scopeModal2').modal('show');
                $('#modal_text').text("Your project was updated successfully ");
                //}

            },
            error: function (xhr, status, statusText) {
                console.log('An error occurred on : createProject' + xhr.error);
            }
        })
    }
}

function showErrorMsg(inpEle, msg) {
    
    $('#' + inpEle).text(msg);
    $('#' + inpEle).css('display', '');
}

function hideErrorMsg(inpEle) {
    $('#' + inpEle).text('');
    $('#' + inpEle).css('display', 'none');
}

function validateProject() {
    var OK = true;
    //hideErrorMsg('end_date-Required');
    //project name
    hideErrorMsg('project-Required');
    if ($("#projectName").val() != null && $("#projectName").val() != "") {
        var pName = $("#projectName").val().toLowerCase();
        if ($("#select_customer_name").val() != "0" && !pName.includes($("#select_customer_name option:selected").text().toLowerCase().replace(/["']/g, ""))) {
            if (pName.includes($("#select_country option:selected").text().toLowerCase()) || pName.includes($("#select_market_area option:selected").text().toLowerCase())) {
                showErrorMsg('project-Required', 'Market Area or Country is already present');
                $('#projectName').focus();
                if (OK == true) { $("#projectName").focus(); }
                OK = false;
            }
        }
        
    }
    if ($("#projectName").val() == null || $("#projectName").val() == "") {
        showErrorMsg('project-Required', 'Please enter the project name');
        if (OK == true) { $('#projectName').focus(); }
    }
    hideErrorMsg('project_name-Required');
    if ($("#project_name").text() == null || $("#project_name").text() == "") {
        showErrorMsg('project_name-Required', "Project name is required");
        if (OK == true) { $("#project_name").focus(); }
        OK = false;
    }
    hideErrorMsg('project-Required');
    var fullName = $("#project_name").text() + $("#projectName").val();
    if (fullName.includes("__")) {
        showErrorMsg('project-Required', "Project name should not contain double underscore");
        if (OK == true) { $("#projectName").focus(); }
        OK = false;
    }
    //CPM
    hideErrorMsg('cpm-Required');
    if ($("#cpm").val() == null || $("#cpm").val() == "") {
        showErrorMsg('cpm-Required', 'CPM is required');
        if (OK == true) { $("#cpm").focus(); }
        OK = false;
    } else {
        if (! $("#cpm_flg").val()) {
            showErrorMsg('cpm-Required', 'Please click find to validate CPM');
            if (OK == true) { $("#cpm").focus(); }
            OK = false;
        }
    }
    //Opportunity
    hideErrorMsg('opportunity_name-Required');
    if ($("#opportunity_name").val() == null || $("#opportunity_name").val() == "") {
        showErrorMsg('opportunity_name-Required', 'Opportunity is required');
        if (OK == true) { $("#select_opportunity_name").focus(); }
        OK = false;
    }
    //Opportunity Code
    hideErrorMsg('opportunity_code-Required');
    if ($("#opportunity_code").val() == null || $("#opportunity_code").val() == "") {
        showErrorMsg('opportunity_code-Required', 'Opportunity is required');
        if (OK == true) { $("#opportunity_code").focus(); }
        OK = false;
    }
    //Dates
    hideErrorMsg('start_date-Required');
    if ($("#start_date").val() == null || $("#start_date").val() == "") {
        showErrorMsg('start_date-Required', 'Start date is required');
        if (OK == true) { $("#start_date").focus(); }
        OK = false;
    }
    hideErrorMsg('end_date-Required');
    if ($("#end_date").val() == null || $("#end_date").val() == "") {
        //showErrorMsg('end_date-Is-Required', 'End date is required');
        //showErrorMsg('end_date-Required', 'End date is required');
       
        if (OK == true) {
            $("#end_date").focus();
            showErrorMsg('end_date-Required', 'End date is required.');
            //$('#end_date-Required').text("End date is required");
        }
        OK = false;
    }

    //hideErrorMsg('end_date-Required');
    if ($("#end_date").val() != null && $("#end_date").val() != "") {
        //hideErrorMsg('end_date-Required');
    
        var startDate = new Date($('#start_date').val());
        var endDate = new Date($('#end_date').val());
        
        if (startDate > endDate) {        
            $('#end_date').val("");
            showErrorMsg('end_date-Required', 'End date must be after start date');
            
            OK = false;
        }

    }

    //Dates
    //hideErrorMsg('end_date-Required');
    //let start_Date = new Date($('#start_date').val());
    //let end_Date = new Date($('#end_date').val());
    //if (start_Date > end_Date) {
    //    $('#end_date').val("");
    //    showErrorMsg('end_date-Required', 'End date must be after start date');
    //    OK = false;
    //}

    //hideErrorMsg('start_date-Required');
    //let today_Date = new Date();
    //let sd = new Date($('#start_date').val());
    //today_Date = today.getMonth() + 1 + '/' + today.getDate() + '/' + today.getFullYear();
    //if (sd != today_Date) {
    //    $('#start_date').val("");
    //    showErrorMsg('start_date-Required', 'start date must be c');
    //    OK = false;
    //}

    //Project Type
    hideErrorMsg('select_Project_Type-Required');
    if ($("#select_Project_Type option:selected").val() == "0") {
        showErrorMsg('select_Project_Type-Required', 'Project type is required');
        if (OK == true) { $("#select_Project_Type").focus(); }
        OK = false;
    }
    //Product Area
    hideErrorMsg('select_product_area-Required');
    if ($("#select_product_area option:selected").val() == "0") {
        showErrorMsg('select_product_area-Required', 'Product area is required');
        if (OK == true) { $("#select_product_area").focus(); }
        OK = false;
    }
    //Project Creator
    hideErrorMsg('project_creator-Required');
    if ($("#project_creator").val() == null || $("#project_creator").val() == "") {
        showErrorMsg('project_creator-Required', 'Project creator is required');
        if (OK == true) { $("#project_creator").focus(); }
        OK = false;
    }
    //Operational Manager
    hideErrorMsg('select_operational_manager-Required');
    if ($("#select_operational_manager option:selected").val() == "0") {
        showErrorMsg('select_operational_manager-Required', 'Operational Manager is required');
        if (OK == true) { $("#select_operational_manager").focus(); }
        OK = false;
    }

    //Description
    hideErrorMsg('project_description-Required');
    if ($("#project_description").val() == null || $("#project_description").val() == "") {
        showErrorMsg('project_description-Required', 'Project description is required');
        if (OK == true) { $("#project_description").focus(); }
        OK = false;
    }
    else
        if ($("#project_description").val().length > 1000) {
            showErrorMsg('project_description-Required', 'Project description cannot exceed 1000 characters');
            if (OK == true) { $("#project_description").focus(); }
            OK = false;
        }
    //Country
    hideErrorMsg('select_country-Required');
    if ($("#select_country option:selected").val() == "0") {
        showErrorMsg('select_country-Required', 'Country is required');
        if (OK == true) { $("#select_country").focus(); }
        OK = false;
    }
    //Market
    hideErrorMsg('select_market_area-Required');
    if ($("#select_market_area option:selected").val() == "0") {
        showErrorMsg('select_market_area-Required', 'Market is required');
        if (OK == true) { $("#select_market_area").focus(); }
        OK = false;
    }
    //Customer
    hideErrorMsg('select_customer_name-Required');
    if ($("#select_customer_name option:selected").val() == "0") {
        showErrorMsg('select_customer_name-Required', 'Customer is required');
        if (OK == true) { $("#select_customer_name").focus(); }
        OK = false;
    }
    //Demand
    hideErrorMsg('select_demand_owning_company-Required');
    if ($("#select_demand_owning_company option:selected").val() == "0") {
        showErrorMsg('select_demand_owning_company-Required', 'Demand owning company is required');
        if (OK == true) { $("#select_demand_owning_company").focus(); }
        OK = false;
    }
    return OK;
}


function logData( msg ) {
    //console.log(msg);
}

function createProject() {
    if (validateProject() == true) {
        var project = new Object();
        project.projectName = $("#project_name").text() + $("#projectName").val() ;
        project.projectType = $("#select_Project_Type").val();
        project.cPM = $("#cpm").val();
        project.cpmName = $("#cpm").attr('cpmName');
        project.cpmMailId = $("#cpm").attr('cpmMailId');
        project.opportunityID = $('#opportunity_code').attr('data-opportunity-id');
        project.opportunityCode = $("#opportunity_code").val();

        project.projectCreator = $("#project_creator").val();
        
        project.opportunityName = $("#opportunity_name").val();
        project.countryID = $("#select_country").val();


        project.companyID = $("#select_demand_owning_company").val();
        project.customerID = $("#select_customer_name").val();
        project.operationalManager = $("#select_operational_manager option:selected").val();
        project.pCode = $("#project_code").val();

        project.marketAreaID = $("#select_market_area").val();
        project.startDate = $("#start_date").val();
        project.endDate = $("#end_date").val();

        project.projectDescription = $("#project_description").val();
        project.productAreaID = $("#select_product_area").val();
        project.active = "0";
        //if ($("#select_RPM").val() != 0) {
        //    project.RPM = $("#select_RPM").val();
        //} else {
            project.rPM = null;
        //}
        


        project.projectDocuments = [];

        $('#table_project_documents tr').each(function () {
            var type = $(this).find(".type").html();

            if (type) {
                project.projectDocuments.push({
                    "documentType": type,
                    "documentLinks": $(this).find(".link").html()
                });
            }

        });
        
        $.isf.ajax({
            url: service_java_URL + "projectManagement/createProject",
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

               // projectID = data.substring(data.indexOf('">') + 2, data.indexOf('</'));
               // projectID = $(data).find("string").text();
                projectID = data.responseData;
                $('#modal_text').text("Your project was created successfully (ProjectID : " + projectID + ").");
                $('#scopeModal2').modal('show');
                getOpportunities();
                cleanFields();
                
            },
            error: function (xhr, status, statusText) {
                console.log('An error occurred on : createProject' + xhr.error);
            }
        })
    } else {
        logData('Validation Failed');
    }
}

function checkCpm() {
    if ($("#cpm").val() != null && $("#cpm").val() != "") {
        var cpmTxtVal = $("#cpm").val();
        pwIsf.addLayer({ text: 'Please wait ...' });
        $.isf.ajax({
            url: service_java_URL + "accessManagement/getCPMdetails/" + cpmTxtVal,
            success: function (data) {
                if (data.length == 0) {

                    showErrorMsg('cpm-Required', 'Please enter valid CPM Signum.');
                    $('#cpm').val("");
                    $('#cpm').focus();
                    $("#button_save").prop("disabled", true);

                } else {
                    $("#cpm_flg").val('1');
                    hideErrorMsg('cpm-Required');
                    $("#cpm").attr('title', data[0]['name'] + ' (' + data[0]['mailID'] + ')');
                    $("#cpm").attr('CpmName', data[0]['name']);
                    $("#cpm").attr('CpmMailId', data[0]['mailID']);
                    $("#button_save").prop("disabled", false);
                }
            },
            error: function (xhr, status, statusText) {
                console.log('An error occurred on getCPMdetails: ' + xhr.error);
            },
            complete: function (xhr, statusText) {
                pwIsf.removeLayer();
            }
        });
    } else {
        showErrorMsg('cpm-Required', 'Please enter valid CPM Signum.');
        $('#cpm').val("");
        $('#cpm').focus();
        $("#button_save").prop("disabled", true);
    }
}

function checkProject() {
    if ($("#projectName").val() != null || $("#projectName").val() != "") {
        $('#cpm').focus();
        //$("#header1").css("margin-bottom", "20px")
        //showErrorMsg('project-Required', 'Please enter the project name');
        //$('#projectName').focus();

    }
    //else {
    //    hideErrorMsg('project-Required');
    //    $("#header1").css("margin-bottom", "0px");
    //    $('#cpm').focus();
    //}

}


function cleanFields() {
    $("#project_name").text("");

    $("#cpm").val("");    

    $("#start_date").val("");
    $("#end_date").val("");

    $("#project_description").val("");

}

function newProject() {
    cleanFields();
    intialButtons();
    projectID = 0;
}


function intialButtons() {
    $("#button_edit").hide();
    $("#button_edit_save").hide();
    //$("#button_new").hide();

    $("#button_cancel").show();
    $("#button_create").show();

}

function editButtons() {
    $("#button_edit").show();
    $("#button_edit_save").show();
    //$("#button_new").show();

    $("#button_create").hide();
    $("#button_cancel").hide();
}

function addDocument() {

    var docType = $("#select_document_type option:selected").text();
    var docUrl = $("#input_document_url").val();
    var docFlg = false;
    const sharePointUrl = "ericsson.sharepoint.com";
    if ($("#select_document_type option:selected").val() == "0") {
        showErrorMsg('select_document_type-Required', "Please select Document Type.");
        docFlg = true;
    } else {
        hideErrorMsg('select_document_type-Required');
    }

    if (docUrl && (docUrl.toLowerCase().includes("ericsson.com") || docUrl.toLowerCase().indexOf(sharePointUrl)!==-1))
    {
        hideErrorMsg('input_document_url-Required');
    }
    else
    {
        showErrorMsg('input_document_url-Required', C_VALID_DOC_URL);
        docFlg = true;
    }
 
    if ( ! docFlg ) {
        var row = "";
        row += "<tr>";
        row += "<td class='type'>" + docType;
        row += "</td>";
        row += "<td  class='link'>" + docUrl;
        row += "</td>";
        row += "</tr>";
        $("#table_project_documents").append(row);
    } 
}

function getAllIndexes(arr, val) {
    var indexes = [], i = -1;
    while ((i = arr.indexOf(val, i + 1)) != -1) {
        indexes.push(i);
    }
    return indexes;
}

function fillProjectName() {

    //Market Area> Country > Customer > Company for Opportunity

    var ma = $("#select_market_area option:selected").text().indexOf("Select") >= 0 ? " " : $("#select_market_area option:selected").text();
    var customer = $("#select_customer_name option:selected").text().indexOf("Select") >= 0 ? " " : $("#select_customer_name option:selected").text();
    var company = $("#select_product_area option:selected").text().indexOf("Select") >= 0 ? " " : $("#select_product_area option:selected").text();
    var country = $("#select_country option:selected").text().indexOf("Select") >= 0 ? " " : $("#select_country option:selected").text();

    var maName = ma + "_";
    var customerCompany = country + "_" + customer + "_" + company;

    maName = JSON.stringify(maName).replace(/\"/g, "");
    customerCompany = JSON.stringify(customerCompany).replace(/\"/g, "");
   
    $("#project_name").text(maName);
    
    if ($("#select_customer_name").val() != 0 || $("#select_product_area").val() != 0 || $("#select_country").val() != 0) {
        
        var pName = $("#projectName").val(); 
        var indexes = getAllIndexes(pName, "_");
        var countrySelected = pName.substring(0,pName.indexOf("_"));
        var customerSelected = pName.substring(indexes[0] + 1, indexes[1]);
        var companySelected = pName.substring(indexes[1] + 1);
        
        //if ($("#projectName").val() == "" || $("#projectName").val()includes($("#select_customer_name option:selected").text()) || $("#projectName").val()includes($("#select_product_area option:selected").text()))
        if (pName == "" || checkIfPresent('select_country', countrySelected) || checkIfPresent('select_customer_name', customerSelected) || checkIfPresent('select_product_area', companySelected))
        $("#projectName").val(customerCompany);
    }
    else
        $("#projectName").val("");

    tooltipVal();
}

function checkIfPresent(id,item) {
    var exists = false;
    $('#' + id + ' option').each(function () {
        if (this.text == item) {
            exists = true;
            return false;
        }
        
    });
    return exists;
}
function CheckRpm() {
    var node;
}

function getRPMList() {
    var signumOrganisationId = JSON.parse(ActiveProfileSession).organisationID;
    //let signumOrganisationId = localStorage.getItem("signumOrganisationId");
    var RpmRole = 'RPM';
    $.isf.ajax({
        url: service_java_URL + "/accessManagement/getUsersByAlias/" + RpmRole + "/" + signumOrganisationId,
        success: function (data) {
            var signum = '';
            var name = '';
          //  $('#select_RPM').empty();
          //  $('#select_RPM').append('<option value="0">Select One</option>');
            $.each(data, function (i, d) {
                signum = d.replace(/\(.*$/g, "");
                name = d;
           //     $('#select_RPM').append('<option value="' + signum + '">' + name + '</option>');

            })
           
        },
        error: function (xhr, status, statusText) {
            console.log("Fail in getRPM " + xhr.responseText);
            // alert("status " + xhr.status);
            console.log('Error Occured in getRPM');
        }
    });

}