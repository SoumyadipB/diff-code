var activitySubActivityList = [];
const prevObject = new Object();
let todayDate = new Date();
const TBODYSCOPE = "#TbodyScope";
const EXTPROJECTREQUIREDFIELDID = "#select_ext_project_Required";
const REFWOTEMPLATEFIELDID = "#reference_Wo-Temp-Required";
const EXTERNALACTIVITYREQUIREDID = "#externalActivityReference_Required";
const REQUESTTYPEREQUIREDMSG = "Please select a Request Type.";
const C_SCOPENAMEID = "#scope_name";
const C_STARTDATEID = "#start_date1";
const C_ENDDATEID = "#end_date1";
const C_SELECTDELEVERABLEUNITID = "#select_deliverable_unit";
const C_SELECTREQSTTYPEID = "#select_req_type";
const C_SELECTSCOPESTATUSID = "#select_scope_status";
const C_SELECTASPVENDORID = "#select_asp_vendor";
const C_SELECTEXTRESOURCEID = "#select_ext_resource";
const C_SELECTREFERENCENAMEID = "#reference_name";
const C_SELECTEXTPROJECTID = "#select_ext_project";
const C_SELECTEXTWOTEMPID = "#ext_source_wo_template";
const C_SELECTEXTACTIVITYDROPDOWNID = "#externalActivityDropDown";
const C_SELECTMETHODID = "#selectMethod";
const C_SELECTSCOPETYPEID = "#selectScopeType";
const C_SELECTPROJECTFINANCIALID = "#selectProjectFinancial";
const C_SELECTOPERATORCOUNTID = "#selectOperatorCount";
const C_SELECTREMARKSID = "#msgBox";
var oTable = $(TBODYSCOPE).DataTable();
const modeObject = new Object();
clearValueForEditDropdown();
$(document).ready(function () {
    let dd = todayDate.getDate();
    let mm = todayDate.getMonth() + 1;
    const yyyy = todayDate.getFullYear();
    if (dd < 10) {
        dd = `0${dd}`;
    }
    if (mm < 10) {
        mm = `0${mm}`;
    }
    todayDate = `${yyyy}-${mm}-${dd}`;
    $(C_STARTDATEID).datepicker({
        currentText: "Now",
        minDate: 0,
        maxDate: getFormat(new Date("2049-12-31")),
        dateFormat: 'yy-mm-dd',
        yearRange: "2020:2050"
    }).on('change', function () {
        ChangeStartDateScope();
       detectChangeForUpdate($(C_STARTDATEID).attr("id"));
    });
    $(C_STARTDATEID).val(todayDate).trigger("change");
    $(C_ENDDATEID).val("")
    $(C_ENDDATEID).datepicker({
        minDate: 0,
        maxDate: getFormat(new Date("2049-12-31")),
        dateFormat: 'yy-mm-dd',
        yearRange: "2020:2050"
    }).on('change', function () {
        ChangeEndDateScope();
       detectChangeForUpdate($(C_ENDDATEID).attr("id"));

    });
    getServices(servAreaID);
    $(C_SELECTDELEVERABLEUNITID).on("change", function (e) {
        populateScopeTypeAndMethod();
        detectChangeForUpdate($(C_SELECTDELEVERABLEUNITID).attr("id"));
    });
    $('.fa.fa-calendar.sDate').click(function () {
        $(C_STARTDATEID).focus();
    });
    $('.fa.fa-calendar.eDate').click(function () {
        $(C_ENDDATEID).focus();
    });
    $(C_SCOPENAMEID).on("change keyup paste", function () {
        detectChangeForUpdate($(C_SCOPENAMEID).attr("id"));
    })
    $(C_SELECTREQSTTYPEID).on("change", function () {
        detectChangeForUpdate($(C_SELECTREQSTTYPEID).attr("id"));
    })
    $(C_SELECTASPVENDORID).on("change", function () {
        detectChangeForUpdate($(C_SELECTASPVENDORID).attr("id"));
    })
    $(C_SELECTEXTRESOURCEID).on("change", function () { 
        detectChangeForUpdate($(C_SELECTEXTRESOURCEID).attr("id"));
    })
    $(C_SELECTSCOPESTATUSID).on("change", function () {
        detectChangeForUpdate($(C_SELECTSCOPESTATUSID).attr("id"));
    })
    $(C_SELECTREFERENCENAMEID).on("change keyup paste", function () {
        detectChangeForUpdate($(C_SELECTREFERENCENAMEID).attr("id"));
    })
    $(C_SELECTEXTPROJECTID).on("change", function () {
        detectChangeForUpdate($(C_SELECTEXTPROJECTID).attr("id"));
    })
    $(C_SELECTEXTWOTEMPID).on("change", function () {
        detectChangeForUpdate($(C_SELECTEXTWOTEMPID).attr("id"));
    })
    $(C_SELECTEXTACTIVITYDROPDOWNID).on("change", function () {
        detectChangeForUpdate($(C_SELECTEXTACTIVITYDROPDOWNID).attr("id"));
    })
    $(C_SELECTSCOPETYPEID).on("change", function () {
        detectChangeForUpdate($(C_SELECTSCOPETYPEID).attr("id"));
    })
    $(C_SELECTMETHODID).on("change", function () {
        detectChangeForUpdate($(C_SELECTMETHODID).attr("id"));
    })
    $(C_SELECTPROJECTFINANCIALID).on("change", function () {
        detectChangeForUpdate($(C_SELECTPROJECTFINANCIALID).attr("id"));
    })
    $(C_SELECTOPERATORCOUNTID).on("change", function () {
        detectChangeForUpdate($(C_SELECTOPERATORCOUNTID).attr("id"));
    })
    $(C_SELECTREMARKSID).on("change keyup paste", function () {
        detectChangeForUpdate($(C_SELECTREMARKSID).attr("id"));
    })
});

function selectDeliverableUnit() {
    $(C_SELECTDELEVERABLEUNITID).empty();
    pwIsf.addLayer({ text: "Please wait ..." });
    $.isf.ajax({
        url: `${service_java_URL}projectManagement/getAllDeliverableUnit`,
        returnAjaxObj:true,
        success: function (data) {
            $(C_SELECTDELEVERABLEUNITID).append("<option value=-1></option>");
            $(C_SELECTDELEVERABLEUNITID).index = -1;
            $.each(data, function (i, d) {
                $(C_SELECTDELEVERABLEUNITID).append(`<option value="${d.DeliverableUnitID}">${d.DeliverableUnitName}</option>`);
            })
            $(C_SELECTDELEVERABLEUNITID).val(-1);
            pwIsf.removeLayer();
        },
        error: function (xhr, status, statusText) {
            pwIsf.removeLayer();
            console.log('An error occurred on getRequest');
        }
    });
    $(C_SELECTDELEVERABLEUNITID).select2({
        closeOnSelect: true
    });
}

function populateScopeTypeAndMethod() {
    pwIsf.addLayer({ text: "Please wait ..." });
    $(C_SELECTSCOPETYPEID).empty();
    $(C_SELECTMETHODID).empty();
    $(C_SELECTPROJECTFINANCIALID).empty();
    $(C_SELECTOPERATORCOUNTID).empty();
    $("#scopeType").css("display", "none");
    $("#method").css("display", "none");
    $("#projectFinancialOCDiv").css("display", "none");
    $("#divRemarks").css("margin-top", "0px");
    $("#projectFinancials").css("display", "none");
    $("#operatorCount").css("display", "none");
    $("#divScopeTypeMethod").css("display", "none");
    var selectedDeliverablerUnit = document.getElementById("select_deliverable_unit").value;
    if (selectedDeliverablerUnit !== '' && selectedDeliverablerUnit !== '-1') {
        selectScopeType(selectedDeliverablerUnit);
        selectMethod(selectedDeliverablerUnit);
        selectProjectFinancials(selectedDeliverablerUnit);
        selectOperatorCount(selectedDeliverablerUnit);
    } else {
        pwIsf.removeLayer();

	}
}

function selectRequestType() {
    pwIsf.addLayer({ text: "Please wait ..." });
    $(C_SELECTREQSTTYPEID).empty();
    $(C_SELECTREQSTTYPEID).append(`<option value="0">Select Request Type</option>`);
    $.isf.ajax({
        url: `${service_java_URL}projectManagement/getRequestType`,
        success: function (data) {
            $.each(data, function (i, d) {
                $(C_SELECTREQSTTYPEID).append(`<option value="${d.RequestTypeName}">${d.RequestTypeName}</option>`);
            })
            pwIsf.removeLayer();
        },
        error: function (xhr, status, statusText) {
            console.log('An error occurred on getRequest');
            pwIsf.removeLayer();
        }
    });

}

function selectAspVendor() {
    pwIsf.addLayer({ text: "Please wait ..." });
    $(C_SELECTASPVENDORID).empty();
    $(C_SELECTASPVENDORID).append(`<option value="0">Select Vendor</option>`);
    return $.isf.ajax({
        url: `${service_java_URL}aspManagement/getAllActiveAspVendors`,
        success: function (data) {
            $.each(data, function (i, d) {
                $(C_SELECTASPVENDORID).append(`<option value="${d.vendorCode}">${d.vendorCode}/${d.vendorName}</option>`);
            })
        },
        error: function (xhr, status, statusText) {
            console.log("Fail " + xhr.responseText);
            pwIsf.removeLayer();
        }
    });
}

function DetailClick(ScopeID) {
    $('#MsjNodata').text("");
    if ($.fn.dataTable.isDataTable('#TbodyActivityDetails')) {
        oTable1.destroy();
        $('#TbodyActivityDetails').empty();
    }
    pwIsf.addLayer({ text: "Please wait ..." });
    
    $.isf.ajax({
        url: `${service_java_URL}activityMaster/getViewScopeDetails?projectScopeID=${ScopeID}`,
        success: function (data) {
            pwIsf.removeLayer();
            $("#TbodyActivityDetails").append($('<tfoot><tr><th>Domain / Sub Domain</th><th>Service Area / Sub Service Area</th><th>Technology</th><th>SubActivity</th></tr></tfoot>'));
            oTable1 = $("#TbodyActivityDetails").DataTable({
                searching: true,
                responsive: true,
                colReorder: true,                
                order: [1],
                "destroy": true,
                "data": data.responseData,
                dom: 'Bfrtip',
                buttons: [
                 'colvis','excel'
                ],
                "pageLength": 10,
                "columns": [{ "title": "Domain / Sub Domain", "data": "Domain_SubDomain" }, { "title": "Service Area / Sub Service Area", "data": "ServiceArea" },
                            { "title": "Technology", "data": "Technology" }, { "title": "SubActivity", "data": "ViewScopeDetails[].SubActivity" }],
                initComplete: function () {

                    $('#TbodyActivityDetails tfoot th').each(function (i) {
                        var title = $('#TbodyActivityDetails thead th').eq($(this).index()).text();
                        $(this).html('<input type="text" class="form-control" style="font-size:12px;"  placeholder="Search ' + title + '" data-index="' + i + '" />');
                    });
                    var api = this.api();
                    api.columns().every(function () {
                        var that = this;
                        $('input[type=text]', this.footer()).on('keyup change', function () {
                            if (that.search() !== this.value) {
                                that
                                  .columns($(this).parent().index() + ':visible')
                                  .search(this.value)
                                  .draw();
                            }
                        });
                    });

                }
            });
            $('#TbodyActivityDetails tfoot').insertAfter($('#TbodyActivityDetails thead'));
        },
        error: function () {
            pwIsf.removeLayer();
            $('#MsjNodata').text("No data exists!");           
        }
    });
}

function getact(projectDetailID) {
    $.isf.ajax({
        url: `${service_java_URL}activityMaster/getActivitiesBySubScopeId?projectScopeDetailID=${projectDetailID}`,
        success: function (data) {
            if (!data.isValidationFailed) {
                 $("#TbodyActivityDetails").DataTable({
                    searching: true,
                    responsive: true,
                    "pageLength": 10,
                    "data": data.responseData,
                    "destroy": true,
                    "columns": [{ "title": "Activity/Sub-Activity", "data": "SubActivity" },
                    { "title": "Est. Work Effort (Hrs)", "data": "AvgEstdEffort" }]
                });
            }
        },
        error: function (xhr, status, statusText) {
            console.log('An error occurred');
        }
    });
}

function DeleteScope(ScopeID) {
    var r = confirm("Do you want to delete this scope detail?!");
    if (r===true) {
        $.isf.ajax({
            url: `${service_java_URL}projectManagement/DeleteScope/${ScopeID}/${signumGlobal}`,
            context: this,
            crossdomain: true,
            processData: true,
            contentType: 'text/plain',
            type: 'POST',
            data: '',
            xhrFields: {
                withCredentials: false
            },
            success: function (data) {
                alert("Deliverable deleted");
                getTableInfoScope();
            },
            error: function (xhr, status, statusText) {
                console.log('An error occurred');
                alert('Fail in delete Deliverable');
            }
        })
    }

}

function onSubServiceChangeDomain() {

    const selectNext = $('#select_serviceScope').attr('data-selectnext');
    const serviceAreaID = document.getElementById("select_serviceScope").value;
    $("#select_domainScope").select2("val", "");
    $("#select_technologyScope").select2("val", "");
    pwIsf.addLayer({ text: "Please wait ..." });
    $.isf.ajax({
        url: `${service_java_URL}activityMaster/getAllDomainDetailsByService?ServiceAreaID=${serviceAreaID}`,
        success: function (data) {
            pwIsf.removeLayer();
            $('#select_domainScope').empty();
            $('#select_technologyScope').empty();
            $('#select_domainScope').append('<option value="">Select one</option>');
            $('#select_technologyScope').append('<option value="">Select one</option>');
            if (!data.isValidationFailed) {
                $.each(data.responseData, function (i, d) {
                    $('#select_domainScope').append(`<option value="${d.domainID}">${d.domain}</option>`);
                })
            }
            if (selectNext) {
                $('#select_domainScope').val(selectNext).trigger('change');
            }
        },
        error: function (xhr, status, statusText) {
            pwIsf.removeLayer();
            console.log(`An error occurred on getDomain: ${xhr.error}`);
        }
    });
}

function onDomainChangeTech() {
    const selectNext = $('#select_domainScope').attr('data-selectnext');
    const domainID = document.getElementById("select_domainScope").value;
    const serviceAreaID = document.getElementById("select_serviceScope").value;
    $("#select_technologyScope").select2("val", "");
    pwIsf.addLayer({ text: "Please wait ..." });
    let serviceUrl = `${service_java_URL}activityMaster/getTechnologyDetailsByDomain?domainId=${domainID}&serviceAreaID=${serviceAreaID}`;
    if (ApiProxy === true) {
        const urlEncoded = `${domainID}&serviceAreaID=${serviceAreaID}`;
        serviceUrl = `${service_java_URL}activityMaster/getTechnologyDetailsByDomain?domainId=${encodeURIComponent(urlEncoded)}`;
    }
    $.isf.ajax({
        url: serviceUrl,
        success: function (data) {
            pwIsf.removeLayer();
            $('#select_technologyScope').empty();
            $('#select_technologyScope').append('<option value="">Select one</option>');
            $.each(data, function (i, d) {
                $('#select_technologyScope').append(`<option value="${d.technologyID}">${d.technology}</option>`);
            })
            if (selectNext) {
                $('#select_technologyScope').val(selectNext).trigger('change');
            } 
        },
        error: function (xhr, status, statusText) {
            pwIsf.removeLayer();
            console.log('An error occurred');
        }
    });
}

function ChangeStartDateScope() {
    const startDate = new Date($(C_STARTDATEID).val());
    const sDate = getFormat(startDate);
    const tDate = getFormat(new Date());
    const projectStart = new Date(start);
    if (startDate < projectStart) {
        $(C_STARTDATEID).val(tDate);
        pwIsf.alert({ msg: "Start date should not be before today's date", type: WARNINGTEXT });
    }
    else if (sDate < tDate) {
        pwIsf.alert({ msg: "Start date should not be before today's date", type: WARNINGTEXT });
        $(C_STARTDATEID).val(tDate);
    }
    else {
        var endDate = new Date($(C_ENDDATEID).val());
        if (endDate < startDate) {
            $(C_ENDDATEID).val($(C_STARTDATEID).val());
            $(C_ENDDATEID).trigger('change');
		}    
    }
}

function ChangeEndDateScope() {
    var startDate = new Date($(C_STARTDATEID).val());
    var endDate = new Date($(C_ENDDATEID).val());
    if (endDate < startDate) {
        $(C_ENDDATEID).val($(C_STARTDATEID).val());
        $(C_ENDDATEID).trigger('change');
    }
}

function getStartDate() {
   
    const now = new Date();
    const day = (`0${now.getDate()}`).slice(-2);
    const month = (`0${now.getMonth()+1}`).slice(-2);
    const today = `${now.getFullYear()}-${month}-${day}`;
    $(C_STARTDATEID).val(today);
    $(C_ENDDATEID).val(today);
    $("[id$=start_date1]").attr('min', today);
    $("[id$=end_date1]").attr('min', today);
    defaultDate = start;
   
    getServices(servAreaID);

}

function getServices(servAreaID) {
    if (!servAreaID || isNaN(parseInt(servAreaID))) {
        console.log("service area id is wrong");
        pwIsf.alert({ msg: 'Wrong Service Id', type: 'error' });
    }
    else {
        $.isf.ajax({
            url: `${service_java_URL}activityMaster/getServiceAreaDetails?ServAreaID=${parseInt(servAreaID)}`,
            success: function (data) {
                $.each(data, function (i, d) {
                    $('#select_serviceScope').append(`<option value="${d.serviceAreaID}">${d.serviceArea}</option>`);
                })
            },
            error: function (xhr, status, statusText) {
                console.log(`API ${status} ${xhr.responseText}`);
                console.log('An error occurred');
            }
        });
    }
    
}

function getActivitiesSubScope(projectId, scopeId, subscope, domain, service, techn) {
    $("#subScope_Id").val(subscope);
    $('#hidden_scopeId').val(scopeId);
    $('#select_activitysubScope option').remove();
    $("#select_activitysubScope").select2("val", "");
    $('#select_activitysubScope').append('<option value="0">Select one</option>');
    $("#estimatedWorkEffort").val("");

    pwIsf.addLayer({ text: "Please wait ..." });
   
    $.isf.ajax({
        url: `${service_java_URL}activityMaster/getActivityAndSubActivityDetails/${domain}/${service}/${techn}`,
        success: function (data) {
            $.each(data, function (i, d) {
                $('#select_activitysubScope').append(`<option value="${d.subActivityID}">${d.subActivityName}</option>`);
            })
        },
        error: function (xhr, status, statusText) {
            console.log(`An API ${status} ${xhr.responseText}`);
        },
        complete: function () {
            pwIsf.removeLayer();
        }
    });


    $('#tBodyActivitiesScope tr').remove();
    pwIsf.addLayer({ text: "Please wait ..." });
   
    $.isf.ajax({
        url: `${service_java_URL}activityMaster/getActivitiesBySubScopeId?projectScopeDetailID=${subscope}`,
        success: function (data) {
            let html = "";
            if (data.isValidationFailed === false) {
                $.each(data.responseData, function (i, d) {
                    html = "<tr id='trAct" + d.ActivityScopeID + "'>";
                    html = html + "<td>" + d.SubActivity;
                    html = html + "</td>";
                    html = html + '<td><a class="icon-delete" title="Delete Activity/Sub-activity" onclick="DeleteActivity(' + d.ActivityScopeID + ',' + projectId + ',' + scopeId + ',' + subscope + ')">' + getIcon('delete') + '</a>'
                    html = html + "</tr>";
                    $("#tBodyActivitiesScope").append(html);
                })
            }

        },
        error: function (xhr, status, statusText) {     
            console.log(`An API ${status} ${xhr.responseText}`);
        },
        complete: function (xhr, statusText) {
            pwIsf.removeLayer();
        }
    });

   const projectScopeID = $("#scope_Id").val();
    pwIsf.addLayer({ text: "Please wait ..." });
    $.isf.ajax({
        url: `${service_java_URL}activityMaster/getSubScopebyScopeID?projectScopeID=${projectScopeID}`,
        success: function (data) {
            if (!data.isValidationFailed) {
                $.each(data.responseData, function (i, d) {
                    if (d.ProjectScopeDetailID === subscope)
                        $('#scopeActTitle').text(`${d.Domain_SubDomain} | ${d.ServiceArea} | ${d.Technology}`);
                });
            }
        },
        error: function (xhr, status, statusText) {
            var err = JSON.parse(xhr.responseText);
            pwIsf.alert({ msg: err.errorMessage, type: "error" });
        },
        complete: function () {
            pwIsf.removeLayer();

        }
    });
}

function editScopeDeatilsProject(params) {
    
    var selectAllselectBox = function (serviceId,domainId,techId) {
        $("#select_serviceScope").attr('data-selectnext', domainId);
        $("#select_domainScope").attr('data-selectnext', techId);
        $('#select_serviceScope').val(serviceId).trigger('change');        
    };
    const myObject = JSON.parse(params);
    myObject.loggedInUser = signumGlobal;
    const JsonObj = JSON.stringify(myObject);
    pwIsf.addLayer({ text: 'Please wait ...' });
    $.isf.ajax({
        url: `${service_java_URL}projectManagement/editProjectComponents`,
        context: this,
        crossdomain: true,
        processData: true,
        contentType: 'application/json',
        type: 'POST',
        data: JsonObj,//service_data,
        xhrFields: {
            withCredentials: false
        },
        success: function (data) {

            if (data.isEdited) {

                selectAllselectBox(`${data.data[0].ServiceAreaID}`,`${data.data[0].DomainID}`,`${data.data[0].TechnologyID}`);

                //copying array to check validity for update
                activitySubActivityList = [];
                activitySubActivityList = data.activitySubActivityList;

                disabledAllScopeDetailsField();
                for (var i in data.editableItems) {
                    $(`#${data.editableItems[i]}`).attr('disabled', false);
                }

                $('#btnAddScopeDetails').hide();
                $('#btnUpdateScopeDetails').show();
                $('#btnUpdateScopeDetails').attr('data-btndetails', JsonObj);
                $('#btnCloseUpdateScopeDetails').show();
                $('.updateBlockOfScopeDetails').addClass('updateBlockCss');

            } else {
                pwIsf.alert({ msg: data.msg, type: 'warning' });
            }

        },
        error: function (xhr, status, statusText) {
            var err = JSON.parse(xhr.responseText);
            pwIsf.alert({ msg: err.errorMessage, type: "error" });
        },
        complete: function (xhr, statusText) {
            pwIsf.removeLayer();
        }
    });


    console.log('test');
}




function getTableSubScope(ScopeID,projectId) {
    
    CloseSliderView();
    $("#scope_Id").val(ScopeID);
    $("#select_domainScope").select2("val", "");
    $("#select_serviceScope").select2("val", "");
    $("#select_technologyScope").select2("val", "");

    $('#TbodyScopeDetails2 tr').remove();
    pwIsf.addLayer({ text: "Please wait ..." });
    $.isf.ajax({
        url: `${service_java_URL}activityMaster/getSubScopebyScopeID?projectScopeID=${ScopeID}`,
        success: function (data) {
            pwIsf.removeLayer();
            var html = "";
            if (!data.isValidationFailed) {
                $.each(data.responseData, function (i, d) {
                    var parameters = `${projectId},${ScopeID},${d.ProjectScopeDetailID},${d.DomainID},${d.ServiceAreaID},${d.TechnologyID}`;
                    var editBtnDetails = JSON.stringify({ scopeDetailID: d.ProjectScopeDetailID, projectID: ProjectID, scopeID: d.ProjectScopeID, type: 'SCOPEDETAIL' });
                    html = `<tr id="trSubScopeID${d.ProjectScopeDetailID}">`;
                    html = html + "<td>" + d.Domain_SubDomain;
                    html = html + "</td>";
                    html = html + "<td>" + d.ServiceArea;
                    html = html + "</td>";
                    html = html + "<td>" + d.Technology;
                    html = html + "</td>";
                    html = html + '<td><a href="#" title="Add Activity/Sub-activity" class="icon-add" data-toggle="modal" data-target="#ActivityScope" onclick="getActivitiesSubScope(' + parameters + ')">' + getIcon('add') + '</a>' +
                        '<a class="icon-edit lsp editProjectScopeDetailsCss" data-btndetails=\'' + editBtnDetails + '\'>' + getIcon('edit') + '</a></td>';
                    html = html + "</tr>";
                    $("#TbodyScopeDetails2").append(html);


                });
            }
            $('.editProjectScopeDetailsCss').on('click', function () { editScopeDeatilsProject(this.getAttribute('data-btndetails')); });
        },
        error: function (xhr, status, statusText) {
            pwIsf.removeLayer();            
        }
    });

    
}

function isValidateSource(OK) {
    $("#select_ext_resource-Required").text("");
    if ($("#select_ext_resource").val() === null || $("#select_ext_resource").val() === 0 || $("#select_ext_resource").val() === '0') {
        $('#select_ext_resource-Required').text("Source is required");
        OK = false;
    }
    return OK;
}

function isDeliverableNameAndStatusValidated() {
    let OK = true;
    let deliverableFormat = /[/\\#^%.?;|]/; //Check for these special characters in deliverable name


    $('#scope_name-Required').text("");
    if ($(C_SCOPENAMEID).val() == null || $(C_SCOPENAMEID).val() == "") {
        $('#scope_name-Required').text("Deliverable name is required");
        OK = false;
    }
    else if (deliverableFormat.test($(C_SCOPENAMEID).val())) {
        $('#scope_name-Required').text(String.raw`These special characters are not allowed in Deliverable Name: /,\,#,^,%,.,?,;,|`);
        OK = false;
    }

    $("#select_scope_status-Required").text("");
    if ($("#select_scope_status").val() == null || $("#select_scope_status").val() == 0 || $("select_scope_status").val() == "") {
        $('#select_scope_status-Required').text("Source is required");
        OK = false;
    }

    return OK;
}

function cancelUpdateDetails() {
    cleanScopeDetailsFields();

    $('#select_serviceScope').attr('data-selectnext', '0');
    $('#select_domainScope').attr('data-selectnext', '0');

    $('#btnAddScopeDetails').show();
    $('#btnUpdateScopeDetails').hide();
    $('#btnCloseUpdateScopeDetails').hide();

    $('.updateBlockOfScopeDetails').removeClass('updateBlockCss');

}

function cancelUpdate() {
    closeAddDeliverableDiv();
    cleanScopeFields();
    $('#btnAddScope').show();
    $('#btnUpdateScope').hide();
     
    clearPrevObjectFields();
}

function disabledAllScopeField() {
        $(C_SCOPENAMEID).attr('disabled', true);
        $(C_SELECTREQSTTYPEID).attr('disabled', true);
        $(C_STARTDATEID).attr('disabled', true);
        $(C_ENDDATEID).attr('disabled', true);
        $('#select_deliverable_unit').attr('disabled', true);
        $(C_SELECTEXTRESOURCEID).attr('disabled', true);
        $(C_SELECTASPVENDORID).attr('disabled', true);
        $('#reference_name').attr('disabled', true);
  
}

function disabledAllScopeDetailsField() {
    $('#select_serviceScope').attr('disabled',true);
    $('#select_domainScope').attr('disabled', true);
    $('#select_technologyScope').attr('disabled', true);
    
}


// validate Scope details
function validateScopeDetailForActivity(params) {

    var activityModel = [];
    // if activity is not added yet. Domain,Technology can be updated
    if (activitySubActivityList.length === 0) {
        updateScopeDetails(params);
    } else {
        for (let actListValue of activitySubActivityList) {
            const obj = JSON.parse(JSON.stringify(actListValue));
            var activityObj = {};
            activityObj["domainID"] = $("#select_domainScope").val();
            activityObj["serviceAreaID"] = $("#select_serviceScope").val();
            activityObj["technologyID"] = $("#select_technologyScope").val();
            activityObj["activity"] = obj.Activity;
            activityObj["subActivity"] = obj.SubActivity;
            activityModel.push(activityObj);
        }
        console.log(`activityModel : ${JSON.stringify(activityModel)}`);
        $.isf.ajax({
            url: `${service_java_URL}projectManagement/validateScopeDetailForActivitySubActivity`,
            context: this,
            crossdomain: true,
            processData: true,
            contentType: 'application/json',
            type: 'POST',
            data: JSON.stringify(activityModel),
            xhrFields: {
                withCredentials: false
            },
            success: function (data) {
                if (!data.responseData) {
                    pwIsf.alert({ msg: "Deliverable details are not valid for update", autoClose: 3 });
                } else {
                    updateScopeDetails(params);
                }
            },
            error: function (xhr, status, statusText) {
                var err = JSON.parse(xhr.responseText);
                pwIsf.alert({ msg: err.errorMessage, type: "error" });
            },
            complete: function (xhr, statusText) {
                pwIsf.removeLayer();
            }
        });
    }
}

function updateScopeDetails(params) {

    if (validateSubScope() === true) {
        var obj = JSON.parse(params);
        obj.projectScopeID = obj.scopeID;
        obj.projectScopeDetailID = obj.scopeDetailID;
        obj.domainID = $("#select_domainScope").val();
        obj.serviceAreaID = $("#select_serviceScope").val();
        obj.technologyID = $("#select_technologyScope").val();
        obj.createdBy = signumGlobal;
        obj.loggedInUser = signumGlobal;
        pwIsf.addLayer({ text: 'Please wait ...' });
        $.isf.ajax({
            url: `${service_java_URL}projectManagement/updateEditProjectScopeDetail`,
            context: this,
            crossdomain: true,
            processData: true,
            contentType: 'application/json',
            type: 'POST',
            data: JSON.stringify(obj),//service_data,
            xhrFields: {
                withCredentials: false
            },
            success: function (data) {

                if (data.isUpdated) {
                    pwIsf.alert({ msg: "Deliverable details is successfully updated", autoClose: 3 });
                    cancelUpdateDetails();
                    getTableSubScope(obj.projectScopeID, obj.projectID);

                } else {
                    pwIsf.alert({ msg: data.msg, type: 'warning' });
                }

            },
            error: function (xhr, status, statusText) {
                var err = JSON.parse(xhr.responseText);
                pwIsf.alert({ msg: err.errorMessage, type: "error" });
            },
            complete: function (xhr, statusText) {
                pwIsf.removeLayer();
            }
        });
    }
}

function updateScope() {
    if (validateScope() === true) {
        var obj = JSON.parse($('#btnUpdateScope').attr('data-btndetails'));
        let remarks = $("#msgBox").val();
        if (remarks === "") {
            remarks = null;
        }
        obj.projectScopeID = obj.scopeID;
        var scopename = $.trim($(C_SCOPENAMEID).val());
        obj.scopeName = scopename;
        obj.startDate = getFormat($(C_STARTDATEID).val());
        obj.endDate = getFormat($(C_ENDDATEID).val());
        obj.deliverableUnit = $(C_SELECTDELEVERABLEUNITID).val();
        obj.requestType = $(C_SELECTREQSTTYPEID).val();
        obj.source = $(C_SELECTEXTRESOURCEID).val();
        obj.externalReference = $(C_SELECTREFERENCENAMEID).val();
        obj.loggedInUser = signumGlobal;
        obj.vendorCode = $(C_SELECTASPVENDORID).val();
        obj.deliverableStatus = $(C_SELECTSCOPESTATUSID).val();
        obj.scopeTypeId = $(C_SELECTSCOPETYPEID).val();
        obj.methodID = $(C_SELECTMETHODID).val();
        obj.remarks = remarks;
        obj.operatorCountId = $(C_SELECTOPERATORCOUNTID).val();
        obj.projectFinancialsID = $(C_SELECTPROJECTFINANCIALID).val();
        const JsonObj = JSON.stringify(obj);
        if (obj.deliverableUnit === '-1' || obj.deliverableUnit === "" ) {
            pwIsf.alert({ msg: 'Please select a Deliverable Unit.', type: 'warning' });
        }
        else if (obj.requestType === "0" || obj.requestType  === "") {
            pwIsf.alert({ msg: REQUESTTYPEREQUIREDMSG, type: 'warning' });
        }
        else if (obj.source === "0" || obj.source === "") {
            pwIsf.alert({ msg: 'Please select a Source.', type: 'warning' });
        }
        else if (obj.scopeTypeId === "0" || obj.scopeTypeId === "") {
            pwIsf.alert({ msg: 'Please select a Scope Type.', type: 'warning' });
        }
        else if (obj.methodID === "0" || obj.methodID === "") {
            pwIsf.alert({ msg: 'Please select a Method.', type: 'warning' });
        }
        else if (localStorage.getItem("projectType") !== "ASP" && (obj.requestType === "" || obj.requestType === null)) {
            pwIsf.alert({ msg: REQUESTTYPEREQUIREDMSG, type: 'warning' });
        }
        else if (localStorage.getItem("projectType") === "ASP" && (obj.vendorCode === "" || obj.vendorCode === null || obj.vendorCode === "0")) {
            pwIsf.alert({ msg: 'Please select a Vendor code.', type: 'warning' });
        }


        else {

        pwIsf.addLayer({ text: 'Please wait ...' });
        $.isf.ajax({
            url: `${service_java_URL}projectManagement/updateEditProjectScope`,
            context: this,
            crossdomain: true,
            processData: true,
            contentType: 'application/json',
            type: 'POST',
            data: JsonObj,//service_data,
            xhrFields: {
                withCredentials: false
            },
            success: function (data) {              
                if (!data.isValidationFailed) {
                    const responseData = data.responseData;
                    if (data.responseData !== null && responseData.isUpdated) {
                        closeAddDeliverableDiv();
                        pwIsf.alert({ msg: `Deliverable  (${obj.scopeName}) is successfully updated`, type: 'success', autoClose: 3 });
                    } else {
                        if (data.formMessageCount > 0) {
                            pwIsf.alert({ msg: data.formMessages[0], type: WARNINGTEXT, autoClose: 2 });
						}
					}
                }
                else {
                    pwIsf.alert({ msg: data.formErrors[0], type: "error"});
                }
            },
            error: function (xhr, status, statusText) {
                pwIsf.alert({ msg: 'Error in API, please contact ISF support.', type: WARNINGTEXT, autoClose: 2});
            },
            complete: function (xhr, statusText) {
                pwIsf.removeLayer();
                    cancelUpdate();
                    getTableInfoScope();
            }
        });
        }
    }

}

function SaveScope() {

    if (validateScope() === true) {
        const scopeObj = getScopeData();
        const serviceData = JSON.stringify(scopeObj);

        if (scopeObj.deliverableUnit === '-1' || scopeObj.deliverableUnit ==="") {
            pwIsf.alert({ msg: 'Please select a Deliverable Unit.', type: 'warning' });
        }
        else if (scopeObj.requestType === "0" || scopeObj.requestType==="")
        {
            pwIsf.alert({ msg: REQUESTTYPEREQUIREDMSG, type: 'warning' });
        }
        else if (scopeObj.source === "0" || scopeObj.source === "") {

            pwIsf.alert({ msg: 'Please select a Source.', type: 'warning' });
        } 
        else if (scopeObj.scopeTypeId === "0" || scopeObj.scopeTypeId === "") {
            pwIsf.alert({ msg: 'Please select a Scope Type.', type: 'warning' });
        }
        else if (scopeObj.methodID === "0" || scopeObj.methodID === "") {
            pwIsf.alert({ msg: 'Please select a Method.', type: 'warning' });
        }
        else if (localStorage.getItem("projectType") !== "ASP" && (scopeObj.requestType === "" || scopeObj.requestType === null)) {
            pwIsf.alert({ msg: REQUESTTYPEREQUIREDMSG, type: 'warning' });
        }
        else if (localStorage.getItem("projectType") === "ASP" && (scopeObj.vendorCode === "" || scopeObj.vendorCode === null || scopeObj.vendorCode === "0")) {
            pwIsf.alert({ msg: 'Please select a Vendor code.', type: 'warning' });
        }
        else {
            ajaxPostCallSaveScope(serviceData);
        }
    }
}

function cleanScopeFields() {
    $(C_SCOPENAMEID).val("");
    const now = new Date();
    const day = (`0${now.getDate()}`).slice(-2);
    const month = (`0${(now.getMonth() + 1)}`).slice(-2);
    const today = `${now.getFullYear()}-${month}-${day}`;
    $(C_STARTDATEID).val(today);
    $(C_STARTDATEID).attr("disabled", false);
    $(C_ENDDATEID).val("");
    $(C_SELECTREQSTTYPEID).val("");
    $(C_SELECTDELEVERABLEUNITID).val("-1");
    $(C_SELECTDELEVERABLEUNITID).trigger("change");
    $(C_SELECTREFERENCENAMEID).val("");

    $(EXTPROJECTREQUIREDFIELDID).text("");
    $(REFWOTEMPLATEFIELDID).text("");
    $(EXTERNALACTIVITYREQUIREDID).text("");
    $('#start_date1-Required').text("");
    $('#end_date1-Required').text("");
    $('#reference_name-Required').text("");
    $('#scope_name-Required').text("");
    $("#select_ext_resource-Required").text("");
    $("#select_scope_status-Required").text("");
    $(C_SELECTEXTPROJECTID).val("0");
    $(C_SELECTEXTPROJECTID).trigger('change');

    $(C_SELECTEXTACTIVITYDROPDOWNID).val("0");
    $(C_SELECTEXTACTIVITYDROPDOWNID).trigger('change');

    $(C_SELECTEXTRESOURCEID).val("0");
    $(C_SELECTEXTRESOURCEID).trigger('change');
    $(C_SELECTEXTRESOURCEID).attr("disabled", false);
    $(C_SELECTREFERENCENAMEID).val("");
    $(C_SELECTPROJECTFINANCIALID).val("0");
    $(C_SELECTPROJECTFINANCIALID).trigger('change');

    $(C_SELECTOPERATORCOUNTID).val("0");
    $(C_SELECTOPERATORCOUNTID).trigger('change');
    $(C_SELECTMETHODID).val("0");
    $(C_SELECTMETHODID).trigger('change');
    $(C_SELECTSCOPETYPEID).val("0");
    $(C_SELECTSCOPETYPEID).trigger('change');
    $("#msgBox").val("");
    $("#btnUpdateScope").prop("disabled", "disabled");
}

function cleanScopeDetailsFields() {
    $("#select_serviceScope").val('').trigger('change');
    $("#select_domainScope").val('').trigger('change');
    $("#select_technologyScope").val('').trigger('change');
    $(C_SELECTDELEVERABLEUNITID).val('-1');
    $(C_SELECTDELEVERABLEUNITID).trigger("change");
}

function deleteScopeProject(thisElement) {
    CloseSliderView();
    var that = $(thisElement);
    const dataStr = that.attr("data-btndetails");
    const myObject = JSON.parse(decodeURIComponent(dataStr));
    myObject.loggedInUser = signumGlobal;
    const JsonObj = JSON.stringify(myObject);
    pwIsf.confirm({title: 'Delete Deliverable', msg: 'Are you sure to delete this deliverable ? ',
        'buttons': {
            'Yes': {
                'action': function () {
                    pwIsf.addLayer({ text: 'Please wait ...' });
                    $.isf.ajax({
                        url: `${service_java_URL}projectManagement/deleteProjectComponents`,
                        context: this,
                        crossdomain: true,
                        processData: true,
                        contentType: 'application/json',
                        type: 'POST',
                        data: JsonObj,//service_data,
                        xhrFields: {
                            withCredentials: false
                        },
                        success: function (data) {

                            if (data.isDeleted) {
                                that.closest('tr').hide('slow');
                                pwIsf.alert({ msg: "Successfully Deleted.",autoClose:3 });
                            } else {
                                pwIsf.alert({ msg: data.msg ,type:'warning'});
                            }
                            
                        },
                        error: function (xhr, status, statusText) {
                            const err = JSON.parse(xhr.responseText);
                            pwIsf.alert({ msg: err.errorMessage, type: 'warning' });
                        },
                        complete: function (xhr, statusText) {
                            pwIsf.removeLayer();
                        }
                    });
                }
            },
            'No': { 'action': function () { console.log("No action triggered") } },
            
        }
    });
}

function getFormat(date) {
    let d = new Date(date),
        month = '' + (d.getMonth() + 1),
        day = '' + d.getDate(),
        year = d.getFullYear();

    if (month.length < 2)
        month = '0' + month;
    if (day.length < 2) {
        day = '0' + day;
	}
     date = new Date();
    date.toLocaleDateString();
    return [year, month, day].join('-');
}
function editScopeProject(thisElement) {
    $("#divAddDeliverableScope").find("div.panel-heading span").text("Edit Deliverable");
    var that = $(thisElement);
    pwIsf.addLayer({ text: 'Please wait ...' });
    openAddDeliverableDiv();
    CloseSliderView();
    clearPrevObjectFields();
    const dataStr = that.attr("data-btndetails");
    const myObject = JSON.parse(decodeURIComponent(dataStr));
    myObject.loggedInUser = signumGlobal;
    const JsonObj = JSON.stringify(myObject);
    pwIsf.addLayer({ text: 'Please wait ...' });
    const ajaxDelverableUnit = $.isf.ajax({
        url: `${service_java_URL}projectManagement/getAllDeliverableUnit`,
        returnAjaxObj: true
    });

    $.isf.ajax({
        url: `${service_java_URL}projectManagement/editProjectComponents`,
        context: this,
        crossdomain: true,
        processData: true,
        contentType: 'application/json',
        type: 'POST',
        data: JsonObj,//service_data,
        xhrFields: {
            withCredentials: false
        },
        success: function (data) {

            if (data.isEdited && data.data.length > 0) {
                ajaxDelverableUnit.done(function (deleverableResponseData) {
                    $(C_SELECTDELEVERABLEUNITID).empty();
                    $(C_SELECTDELEVERABLEUNITID).append("<option value=-1></option>");
                    $(C_SELECTDELEVERABLEUNITID).index = -1;
                    $.each(deleverableResponseData, function (i, d) {
                        $(C_SELECTDELEVERABLEUNITID).append(`<option value=${d.DeliverableUnitID}>${d.DeliverableUnitName}</option>`);
                    })
                    $(C_SELECTDELEVERABLEUNITID).val(-1);
                    $(C_SELECTDELEVERABLEUNITID).select2({
                        closeOnSelect: true
                    });
                    $(C_SELECTDELEVERABLEUNITID).val(myObject.deliverableUnitID).trigger("change");
                    if (modeObject.selectRequestTypeValue) {
                    $(C_SELECTREQSTTYPEID).val(modeObject.selectRequestTypeValue);
					}
                });
                $("#divExternalWTExternalProject").css("display", "none");
                $(C_SCOPENAMEID).val(data.data[0].ScopeName);
                if (data.data[0].requestType === null || data.data[0].requestType==="") {
                    $(C_SELECTREQSTTYPEID).val(0);
                    data.data[0].requestType = "0";     
                }
                else {
                    $(C_SELECTREQSTTYPEID).val(data.data[0].requestType);

                }
               
                $(C_SELECTASPVENDORID).val(data.data[0].vendorCode);
                $(C_STARTDATEID).val((data.data[0].StartDate));
                $(C_ENDDATEID).val((data.data[0].EndDate));
                $(C_SELECTEXTRESOURCEID).val(data.data[0].source);
                $(C_SELECTREFERENCENAMEID).val(data.data[0].externalReference);
                $(C_SELECTSCOPESTATUSID).val(data.data[0].deliverableStatus);
                $("#msgBox").val(data.data[0].Remarks);
                modeObject.selectScopeTypeValue = data.data[0].ScopeTypeID;
                modeObject.selectMethodValue = data.data[0].MethodID;
                modeObject.selectProjectFinancialValue = data.data[0].ProjectFinancialsID;
                modeObject.selectOperatorCountValue = data.data[0].OperatorCountID;
                modeObject.selectRequestTypeValue = data.data[0].requestType;
                modeObject.selectVendorCodeValue = data.data[0].vendorCode;
                data.deliverableUnitID = myObject.deliverableUnitID;
                setObjectDeliverableUnit(data);
                disabledAllScopeField();
                for (var i in data.editableItems) {
                    $(`#${data.editableItems[i]}`).attr('disabled', false);
                }
                $('#btnAddScope').hide();
                $('#btnUpdateScope').show();
                $('#btnUpdateScope').attr('data-btndetails', JsonObj);
                $('#btnCloseUpdateScope').show();     
                pwIsf.removeLayer();
            }
        },
        error: function (xhr) {
            const err = JSON.parse(xhr.responseText);
            pwIsf.alert({ msg: err.errorMessage, type: 'warning' });
            pwIsf.removeLayer();
        }
    });
}
function callFor_WOForAssignToMe(statusText) {
    $("a.dividerStatus").removeClass("active");
     $('a.dividerStatus').each(function (e) {
        if ($(this).text() === statusText) {
            $(this).addClass('active');
            priority = $(this).text();
        }
    });
    const ProjectID = localStorage.getItem("views_project_id");
    if (ProjectID === undefined || ProjectID === null || ProjectID === '' || isNaN(parseInt(ProjectID))) {
        pwIsf.alert({ msg: "Project ID missing or invalid", type: "error" });  
    }
    else {
        if ($.fn.dataTable.isDataTable(TBODYSCOPE)) {
            oTable.destroy();
            $(TBODYSCOPE).empty();
        }
        $.fn.dataTable.ext.search.push(function (settings, data, dataIndex) {
            var term = $('.dataTables_filter input').val().replace(/\s/, '&nbsp;');
            if (!term) return true;
            for (var i = 0, l = data.length; i < l; i++) {
                if (~data[i].replace(/\s/, '&nbsp;').indexOf(term)) return true;
            }
            return false;
        });
        bindProjectScopeTable(ProjectID, statusText);
    }
}

function getTableInfoScope()
{
    $("a.dividerStatus").removeClass("active");
    $('a.dividerStatus').each(function (e) {
        if ($(this).text() === "Active") {
            $(this).addClass('active');
            priority = $(this).text();
        }
    });
    var ProjectID = localStorage.getItem("views_project_id");
    if (ProjectID === undefined || ProjectID === null || ProjectID === '' || isNaN(parseInt(ProjectID))) {
        pwIsf.alert({ msg: "Project ID missing", type: "error" });
    }
    else {
        if ($.fn.dataTable.isDataTable(TBODYSCOPE)) {
            oTable.destroy();
            $(TBODYSCOPE).empty();
        }
        bindProjectScopeTable(ProjectID);

    }
}

function openSliderView(externalProjID, hasDeliverablePlan, scopeID, scopeName, source, extRef, extWoTemp) {
    $("#saveDeliverable").addClass("disabled");
    var div = $("#deliverablePlanView");
    div.show();
    div.animate({ right: '0%', opacity: 1.0 }, "slow");
    planDeliverable(1, externalProjID, hasDeliverablePlan, scopeID, scopeName, source, extRef, extWoTemp);    
}

/*---------------Close Details View Slider-----------------------*/
function CloseSliderView() {
    var div = $("#deliverablePlanView");
    div.animate({ right: '-3%' }, "slow");
    div.hide();
}

function fmtDateToDisplay(dt) {
    if (dt == "" || dt == null) {
        return '';
    } else {
        var from = dt.split(/[\D]/);
        return from[0] + '/' + from[1] + '/' + from[2];
    }

}

function getTableScopeResource() {
    $.isf.ajax({
        url: `${service_java_URL}demandForecast/getAllScopeDetailsByProject?projectId=${ProjectID}`,
        success: function (data) {
            $.each(data, function (i, d) {
                d.actionIcon = "<i style='cursor: pointer;' data-toggle='modal' data-target='#AddR' onclick='ModalAddOpen(" + d.ProjectScopeDetailID + ")' class='fa fa-plus'></i>&thinsp;&thinsp;&thinsp;&thinsp;<i style='cursor: pointer;' data-toggle='modal' data-target='#AllR' onclick='ModalAllOpen(" + d.ProjectScopeDetailID + ")' class='fa fa-edit'></i>&thinsp;&thinsp;&thinsp;&thinsp;<a style='padding-left:2px;' href='#'  data-toggle='modal' data-target='#ARmodal' onclick='loadAllocatedResources(" + d.ProjectID + ',' + d.ProjectScopeDetailID + ")'><i class='fa fa-user' title='ViewPositions&AllocatedResources'></i></a>"
            });
            $("#tbScopeBody").DataTable({
                searching: true,
                responsive: true,
                "pageLength": 10,
                "data": data,
                "destroy": true,
                "columns": [{ "title": "Deliverable Name", "data": "ScopeName" }, { "title": "Domain / Sub Domain", "data": "Domain" },
                    { "title": "Service Area / Sub Service Area", "data": "ServiceArea" }, { "title": "Technology", "data": "Technology" },
                            { "title": "Actions", "targets": 'no-sort', "orderable": false, "searchable": false, "data": "actionIcon" }]
            });
        },
        error: function (xhr, status, statusText) {
            alert("Fail to scope name" + xhr.responseText);
            console.log('An error occurred');
        }

    });
}

function validateSubScope() {
    var OK = true;
    $('#select_domainScope-Required').text("");
    if ($("#select_domainScope").val() == "" || $("#select_domainScope").val() == null) {
        $('#select_domainScope-Required').text("Domain is required");
        OK = false;
    }
    $('#select_serviceScope-Required').text("");
    if ($("#select_serviceScope").val() == "" || $("#select_serviceScope").val() == null) {
        $('#select_serviceScope-Required').text("Service is required");
        OK = false;
    }
    $('#select_technologyScope-Required').text("");
    if ($("#select_technologyScope").val() == "" || $("#select_technologyScope").val() == null) {
        $('#select_technologyScope-Required').text("Technology is required");
        OK = false;
    }
    return OK
}

function SaveScopeDetails() {
    if (validateSubScope() === true) {
        pwIsf.addLayer({ text: 'Please wait ...' });

        var projectId = $('#projectID').text();

        var SubScopeObj = new Object();
        SubScopeObj.projectScopeID = $("#scope_Id").val();
        SubScopeObj.domainID = $("#select_domainScope").val();
        SubScopeObj.serviceAreaID = $("#select_serviceScope").val();
        SubScopeObj.technologyID = $("#select_technologyScope").val();
        SubScopeObj.createdBy = signumGlobal;
        SubScopeObj.deliverableStatus = $(C_SELECTSCOPESTATUSID).val();
        var JsonObj = JSON.stringify(SubScopeObj);

        $.isf.ajax({
            url: `${service_java_URL}projectManagement/SaveProjectScopeDetail`,
            context: this,
            crossdomain: true,
            processData: true,
            contentType: 'application/json',
            type: 'POST',
            data: JsonObj,//service_data,
            xhrFields: {
                withCredentials: false
            },
            success: AjaxSucceeded,
            error: AjaxFailed,
            complete: function (xhr, statusText) {
                pwIsf.removeLayer();
            }
        });
        function AjaxSucceeded(data, textStatus) {
            pwIsf.alert({ msg: 'Deliverable details is added.', autoClose: 3 });
            cleanScopeDetailsFields();
            getTableSubScope(SubScopeObj.projectScopeID, projectId);
        }
        function AjaxFailed(xhr, status, statusText) {
            pwIsf.alert({ msg: 'Error in adding Deriverable Details.', autoClose: 3 });
        }
    }
}

function getDRTable() {
    $.isf.ajax({
        url: `${service_java_URL}demandForecast/getAllScopeDetailsByProject?projectId=${ProjectID}`,
        success: function (data) {
            $.each(data, function (i, d) {
              const html = [`<tr id='trSubScopeID${d.ProjectScopeDetailID}'>`,
                            `<td>${d.ScopeName}</td>`,
			                `<td>${d.Domain}</td>`,
	                        `<td>${d.ServiceArea}</td>`,
	                        `<td>${d.Technology}</td>`,
	                        "<td>",
		                        `<i style='cursor: pointer;' data-toggle='modal' data-target='#AddR' onclick='ModalAddOpen(${d.ProjectScopeDetailID})' class='fa fa-plus'></i>`,
		                        "&thinsp;&thinsp;&thinsp;&thinsp;",
		                        `<i style='cursor: pointer;' data-toggle='modal' data-target='#AllR' onclick='ModalAllOpen(${d.ProjectScopeDetailID})' class='fa fa-edit'></i>`,
		                        "&thinsp;&thinsp;&thinsp;&thinsp;",
	                        "</td>",
                        "</tr>"].join("");

                $("#tbScopeBody").append(html);
            });
        },
        error: function (xhr, status, statusText) {
            var err = JSON.parse(xhr.responseText);
            pwIsf.alert({ msg: err.errorMessage,type:'error' });
        }
    });
}

function DeleteScopeDetails(SubScopeID,projectId,scopeId) {
    var myObject = { scopeDetailID: SubScopeID, projectID: projectId, scopeID: scopeId ,active:0,type:'SCOPEDETAIL'};
    myObject.loggedInUser = signumGlobal;
    var JsonObj = JSON.stringify(myObject);
    pwIsf.confirm({
        title: 'Delete Deliverable details', msg: 'Do you want to delete this Deliverable details?',
        'buttons': {
            'Yes': {
                'action': function () {
                    pwIsf.addLayer({text: 'Please wait ...'});
                    $.isf.ajax({
                        url: `${service_java_URL}projectManagement/deleteProjectComponents`,
                        context: this,
                        crossdomain: true,
                        processData: true,
                        contentType: 'application/json',
                        type: 'POST',
                        data: JsonObj,
                        xhrFields: {
                            withCredentials: false
                        },
                        success: function (data) {
                            if (data.isDeleted) {
                                $("#trSubScopeID" + SubScopeID).remove();
                                pwIsf.alert({ msg: "Deliverable details is deleted.", autoClose: 3 });
                            } else {
                                pwIsf.alert({ msg: data.msg ,type:'warning'});
                            }
                            
                        },
                        error: function (xhr, status, statusText) {                            
                            pwIsf.alert({ msg: `Sorry, an error occurred. Please try reloading the page. \n\nIf that doesn\'t work please send us the following error message, using the contact us page. \n\n ${this.url}  ${xhr.status} ${xhr.statusText}` });
                        },
                        complete: function (xhr, statusText) {
                            pwIsf.removeLayer();
                        }
                    })
                }
            },
            'No': { 'action': function () { console.log('No action triggered'); } },
        }
    });

}

function validateActivity() {
    var OK = true;
    $('#select_activitysubScope-Required').text("");
    if ($("#select_activitysubScope option:selected").val() === "0") {
        $('#select_activitysubScope-Required').text("Activity is required");
        OK = false;
    }
    
    return OK
}

function SaveActivity() {

    const projectId = $('#projectID').text();
    const scopeId = $('hidden_scopeId').val();
    const subScopeId = $("#subScope_Id").val();

    if (validateActivity() === true) {

        pwIsf.addLayer({text:'Please wait ...'});

        var activityObj = new Object();
        activityObj.avgEstdEffort = 0;
        activityObj.subActivityID = $("#select_activitysubScope").val();
        activityObj.projectScopeDetailID = $("#subScope_Id").val();
        activityObj.createdBy = signumGlobal;
        var jqXHR = $.isf.ajax({
            url: `${service_java_URL}projectManagement/SaveActivityScope`,
            context: this,
            crossdomain: true,
            processData: true,
            contentType: 'application/json',
            type: 'POST',
            data: JSON.stringify(activityObj),
            returnAjaxObj: true, 
            xhrFields: {
                withCredentials: false
            },
            success: function (data) {
                if (data !== "0") {
                    var html = "<tr id='trAct" + data + "'>";
                    html = html + "<td>" + $('#select_activitysubScope  option:selected').text();
                    html = html + "</td>";                    
                    html = html + '<td><a class="icon-delete" title="Delete Activity/Sub-activity" onclick="DeleteActivity(' + data + ',' + projectId + ',' + scopeId + ',' + subScopeId + ')">' + getIcon('delete') + '</a>'
                    html = html + "</tr>";
                    $("#tBodyActivitiesScope").append(html);
                    getTableInfoScope();
                }
				else
				{
                    pwIsf.alert({ msg: "Activity already added." ,autoClose:3});
				}
            },
            error: function (xhr, status, statusText) {
                console.log('An error occurred');
                pwIsf.alert({ msg: `Sorry, an error occurred. Please try reloading the page. \n\nIf that doesn\'t work please send us the following error message, using the contact us page. \n\n ${this.url}  ${xhr.status} ${xhr.statusText}`, type: 'error' });
            },
            complete: function (xhr, statusText) {
                pwIsf.removeLayer();
            }
        })
        jqXHR.done(function () {
            $("#select_activitysubScope").select2("val", "");
            $('#estimatedWorkEffortScope').val("");
        });
    }
}

function DeleteActivity(ActID,projectId,scopeId,subScopeId) {
   
    pwIsf.addLayer({ text: 'Please wait ...' });
    var ActivityObj = new Object();
    ActivityObj.activityID = ActID;
    ActivityObj.projectID = projectId;
    ActivityObj.scopeID = scopeId;
    ActivityObj.scopeDetailID = subScopeId;
    ActivityObj.active = 0;
    ActivityObj.type = 'ACTIVITY';
    ActivityObj.loggedInUser = signumGlobal;

    const jsonActivityObj = JSON.stringify(ActivityObj);
    $.isf.ajax({
        url: `${service_java_URL}projectManagement/deleteProjectComponents`,
        context: this,
        crossdomain: true,
        processData: true,
        contentType: 'application/json',
        type: 'POST',
        data: jsonActivityObj,
        xhrFields: {
            withCredentials: false
        },
        success: function (data) { 
            if (data.isDeleted) {
                ActivityObj.type = 'DELETE_ACTIVITY';
                var msgVar='';
                if (data.count_flowChart < 1 && data.count_woid < 1) {
                    msgVar='Do you want to delete this activity? ';
                } else {
                    msgVar = 'This activity is mapped with: Flowchart(' + data.count_flowChart + ') / Workorder(' + data.count_woid + '<br>Are you sure to delete ?)';
                }

                pwIsf.confirm({
                    title: 'Delete Activity/Sub-activity', msg: msgVar,
                    'buttons': {
                        'Yes': {
                            'action': function () {

                                pwIsf.addLayer({ text: 'Please wait ...' });
                                
                                const jsonObj = JSON.stringify(ActivityObj);
                                $.isf.ajax({
                                    url: `${service_java_URL}projectManagement/deleteProjectComponents`,
                                    context: this,
                                    crossdomain: true,
                                    processData: true,
                                    contentType: 'application/json',
                                    type: 'POST',
                                    data: jsonObj,
                                    xhrFields: {
                                        withCredentials: false
                                    },
                                    success: function (data) {

                                        if (data.isDeleted) {

                                            $("#trAct" + ActID).remove();
                                            pwIsf.alert({ msg: "Activity/Sub-activity deleted.", autoClose: 3 });
                                            

                                        } else {
                                            pwIsf.alert({ msg: data.msg, type: 'warning' });
                                        }
                                    },
                                    error: function (xhr, status, statusText) {
                                        pwIsf.alert({ msg: `Sorry, an error occurred. Please try reloading the page. \n\nIf that doesn\'t work please send us the following error message, using the contact us page. \n\n ${this.url}  ${xhr.status} ${xhr.statusText}`, type: 'error' });
                                    },
                                    complete: function (xhr, statusText) {
                                        pwIsf.removeLayer();
                                    }
                                });

                            }
                        },
                        'No': { 'action': function () { console.log('triggered no action');} },
                    }
                });

            } else {
                pwIsf.alert({ msg: data.msg, type: 'warning' });
            }
        },
        error: function (xhr, status, statusText) {
            pwIsf.alert({ msg: `Sorry, an error occurred. Please try reloading the page. \n\nIf that doesn\'t work please send us the following error message, using the contact us page. \n\n ${this.url}  ${xhr.status} ${xhr.statusText}`, type: 'error' });
        },
        complete: function (xhr, statusText) {
            pwIsf.removeLayer();
        }
    });

    
}
function displayAddDeliverableDiv(e) {
    e.stopPropagation();
    $(C_STARTDATEID).val(getFormat(todayDate)).trigger("change");
    $(C_ENDDATEID).val("").trigger("change");
    $("#divAddDeliverableScope").find("div.panel-heading span").text("Add Deliverable");
    $("#divExternalWTExternalProject").hide();
    $("#externalReferenceInputDiv").show();
    $("#divScopeTypeMethod").hide();
    $("#projectFinancialOCDiv").hide();
    clearValueForEditDropdown();
    openAddDeliverableDiv();
    getPlanSources();
    selectDeliverableUnit();
    $(C_SELECTPROJECTFINANCIALID).empty();
    $(C_SELECTOPERATORCOUNTID).empty();
    $(C_SELECTSCOPETYPEID).empty();
    $(C_SELECTMETHODID).empty();
    $("#scopeType").css("display", "none");
    $("#method").css("display", "none");
    $('#btnAddScope').show();
    $('#btnUpdateScope').hide();
   
    $("#hideExP").hide();
    $("#divScopeTypeMethod").css("display", "none");
    $('.updateBlockOfScope').removeClass('updateBlockCss');
    $("#divRemarks").css("margin-top", "0px");
    $("#msgBox").val("");
    $(C_SELECTREFERENCENAMEID).val("");
    $(C_SCOPENAMEID).val("");
    $(C_SELECTSCOPESTATUSID).val("New");

}

function getActionIconsHtml(d, ProjectID) {
    let actionIconHtml = '';
        d.startDate = fmtDateToDisplay(d.startDate);
        d.endDate = fmtDateToDisplay(d.endDate);
        const workpalnTemplate = escape(d.externalWorkplanTemplate);
        const deliverablePlanId = (d.hasDeliverablePlan && d.workFlowCreated) ? d.deliverablePlanId : 0;
    const delBtnDetails = JSON.stringify({ projectID: ProjectID, scopeID: d.projectScopeID, type: "SCOPE", active: 0, executionPlanId: deliverablePlanId, deliverableUnitID: d.deliverableUnitID});
    const editBtnDetails = JSON.stringify({ projectID: ProjectID, scopeID: d.projectScopeID, type: 'SCOPE', deliverableUnitID: d.deliverableUnitID });
        
        if (d.workFlowCreated && !d.hasDeliverablePlan) {
            actionIconHtml = returnActionIconDivForDontHavePlan(workpalnTemplate, delBtnDetails, editBtnDetails, d);
		}
        else if (d.workFlowCreated && d.hasDeliverablePlan) {
            actionIconHtml = returnActionIconDivForDeliverablePlan(workpalnTemplate, delBtnDetails, editBtnDetails, d);
        }
        else {
            actionIconHtml = returnActionIconDivDefault(delBtnDetails, editBtnDetails, d);
		}
            
    return actionIconHtml;
  
    
}
function returnActionIconDivForDeliverablePlan(workpalnTemplate, delBtnDetails, editBtnDetails, d) {   
    return `<div style="display:flex;">
                    <a class="icon-add" title="Add Deliverable details" href="#"  data-toggle="modal" data-target="#SubScope" onclick="getTableSubScope(${d.projectScopeID}, ${ProjectID})">${getIcon('add')}</a>
                    <a class="icon-view lsp" title="View Deliverable details" href="#"  data-toggle="modal" data-target="#VCmodal" onclick="DetailClick(${d.projectScopeID})">${getIcon('view')}</a>
                    <a title="Delete" class="icon-delete lsp deleteScopeProject" data-btndetails='${delBtnDetails}'>${getIcon('delete')}</a>
                    <a title="Edit" class="icon-edit lsp editScopeProjectDetails" data-btndetails='${editBtnDetails}'>${getIcon('edit')}</a>

                    <a title="Click to Edit Saved Deliverable Plan Template!" class="icon-add lsp" onclick="openSliderView(${d.externalProjectId},${d.hasDeliverablePlan},${d.projectScopeID},'${d.scopeName}',${d.sourceId},'${d.externalReference}', '${workpalnTemplate}')" data-btndetails='${editBtnDetails}'>${getIcon('plan')}</a></div>`;
}
function returnActionIconDivForDontHavePlan(workpalnTemplate, delBtnDetails, editBtnDetails, d) {
    return `<div style="display:flex;">
                    <a class="icon-add" title="Add Deliverable details" href="#"  data-toggle="modal" data-target="#SubScope" onclick="getTableSubScope(${d.projectScopeID}, ${ProjectID})">${getIcon('add')}</a>
                    <a class="icon-view lsp" title="View Deliverable details" href="#"  data-toggle="modal" data-target="#VCmodal" onclick="DetailClick(${d.projectScopeID})">${getIcon('view')}</a>
                    <a title="Delete" class="icon-delete lsp deleteScopeProject" data-btndetails='${delBtnDetails}'>${getIcon('delete')}</a>
                    <a title="Edit" class="icon-edit lsp editScopeProjectDetails" data-btndetails='${editBtnDetails}'>${getIcon('edit')}</a>
                    <a title="Click to Create Deliverable Plan Template!" class="icon-plan lsp" onclick="openSliderView(${d.externalProjectId},${d.hasDeliverablePlan},${d.projectScopeID},'${d.scopeName}',${d.sourceId},'${d.externalReference}', '${workpalnTemplate}')" data-btndetails='${editBtnDetails}'>${getIcon('plan')}</a></div>`;
}
function returnActionIconDivDefault(delBtnDetails, editBtnDetails, d) {
    return `<div style="display:flex;">
                    <a class="icon-add" title="Add Deliverable details" href="#"  data-toggle="modal" data-target="#SubScope" onclick="getTableSubScope(${d.projectScopeID}, ${ProjectID})">${getIcon('add')}</a>
                    <a class="icon-view lsp" title="View Deliverable details" href="#"  data-toggle="modal" data-target="#VCmodal" onclick="DetailClick(${d.projectScopeID})">${getIcon('view')}</a>
                    <a title="Delete" class="icon-delete lsp deleteScopeProject" data-btndetails='${delBtnDetails}'>${getIcon('delete')}</a>
                    <a title="Edit" class="icon-edit lsp editScopeProjectDetails" data-btndetails='${editBtnDetails}'>${getIcon('edit')}</a>
                    </div>`;
}

function bindProjectScopeTable(ProjectID, status = null) { 
    const ServiceUrl = urlForScopeByProject(status, ProjectID);
    $(TBODYSCOPE).append($('<tfoot><tr><th></th><th>Deliverable</th><th>Deliverable Status</th><th>Deliverable Unit</th><th>Start Date</th><th>End Date</th><th>Request Type</th><th>Vendor</th><th>Scope Type</th><th>Method</th><th>Deliverable Type</th><th>Source</th><th>External Reference</th><th>External ProjectID</th><th>External WorkPlan Template</th></tr></tfoot>'));
    oTable = $(TBODYSCOPE).DataTable({
        "searching": true, //to enable searching in entire Data table
        "processing": true, //paging
        "serverSide": true, //paging
        responsive: true,
        "ajax": {
            "headers": commonHeadersforAllAjaxCall,
            "url": ServiceUrl,
            "type": "POST",
            "complete": function (response) {
                $('#div_table').show();
            },
            error: function (xhr) {
                $('#div_table').hide();
            }
        },
        drawCallback: function () {
            checkSharePointUrl();
        },
        "pageLength": 10, //length of records
        "destroy": true,
        colReorder: true,
        order: [[2, "desc"]],
        dom: 'Brtip',
        buttons: [
            //for column visibility
            'colvis', {
                "text": 'Export', // for excel
                "sClass": "dt-button buttons-excel buttons-html5",
                action: function (e, dt, node, config) {
                    DownloadFile(ProjectID, status);
                }
            },
            {
                className: 'btn topDataTableBtn',
                text: 'Add Deliverable',
                action: function (e) {
                    displayAddDeliverableDiv(e);
                }

            },
            ],
        "columns": [
            {
                "title": "Action", "targets": 'no-sort', "orderable": false, "searchable": false, "data": null,
                "render": function (data, type, row, meta) {
                    return getActionIconsHtml(data, ProjectID);
                }

            },
            { "title": "Deliverable (Plan ID)", "data": "deliverable"},
            { "title": "Deliverable Status", "data": "deliverableStatus" },
            { "title": "Deliverable Unit", "data": "deliverableUnit" },
            { "title": "Start Date", "data": "startDate" },
            { "title": "End Date", "data": "endDate" },
            { "title": "Request Type", "data": "requestType" },
            { "title": "Vendor code/Name", "data": "vendor"},
            {
                "title": "Scope Type", "data": "scopeType",
                "render": function (data, type, row, meta) {
                    if (row.scopeType === null) {
                        return "";
                    }
                    else {
                        return `<pre>${row.scopeType}</pre>`;
                    }   
                }
            },
            {
                "title": "Method", "data": "method", "render": function (data, type, row, meta) {
                    if (row.method === null) {
                        return "";
                    }
                    else {
                        return `<pre>${row.method}</pre>`;
                    }
                }
            },

            {
                "title": "Deliverable Type", "data": "projectFinancials", "render": function (data, type, row, meta) {
                    if (row.projectFinancials === null) {
                        return "";
                    }
                    else {
                        return `<pre>${row.projectFinancials}</pre>`;
                    }
                }
            },
            { "title": "Source", "data": "source" },
            { "title": "External Reference", "data": "externalReference" },
            {
                "title": "External ProjectID", "data": "externalProjectId",
                "render": function (data, type, row, meta) {
                    if (row.externalProjectId === null) {
                        return "";
                    }
                    else {
                        return `<pre>${row.externalProjectId}</pre>`;
                    }
                }
            },
            {
                "title": "External WorkPlan Template", "data": "externalWorkPlanTemplate",
                "render": function (data, type, row, meta) {
                    return getExternalWorkPlanTemplateColumnData(row);
                }
            }
        ],
        "columnDefs": [{
            targets: '_all',
            render: function (data, type, row, meta) {
                return `<pre>${data}</pre>`;
            }
        }],
        initComplete: function () {
            $("div#TbodyScope_wrapper").find(".dt-buttons").find(".dt-button").last().removeClass("dt-button");
            const listItem = document.createElement("i");
            $(listItem).addClass("fa fa-long-arrow-right");
            $(listItem).css({ "margin-left": "5px" });
            $(".btn.topDataTableBtn").find("span").append(listItem);
            addFieldsInFooter();
            const api = this.api();
            colSearch(api);
        }

    });

    if (localStorage.getItem("projectType") === 'ASP') {
        oTable.column(6).visible(false);
    }
    else {
        oTable.column(7).visible(false);
	}
    $('#TbodyScope tfoot').insertAfter($('#TbodyScope thead'));
    oTable.draw();
}


function addFieldsInFooter() {
    $('#TbodyScope tfoot th').each(function (i) {
        var title = $('#TbodyScope thead th').eq($(this).index()).text();
        if (title !== "Action") {
            $(this).html(`<input type="text" class="form-control" style="font-size:12px;"  placeholder="Search ${title}" data-index="${i}" />`);
        }

    });
}

function colSearch(api) {
    api.columns().every(function () {
        var that = this;
        $('input', this.footer()).on('keyup change', function () {
            if (that.search() !== this.value) {
                that
                    .columns($(this).parent().index() + ':visible')
                    .search(this.value)
                    .draw();
            }
        });
    });
}


function DownloadFile(projectId, statusText) {
    pwIsf.addLayer({ text: 'Please wait ...' });
    let serviceUrl = '';
    if (statusText===null) {
        statusText = "Active";
    }
    var params = [
        "projectId=" + projectId,
        "deliverableStatus=" + statusText
    ];
    var urlSearch = params.join('&');
    let urlEncode = encodeURIComponent(urlSearch);
    
    serviceUrl = `${service_java_URL}projectManagement/downloadDeliverableData?${urlEncode}`;
    let fileDownloadUrl =`${UiRootDir}/data/GetFileFromApi?apiUrl=${serviceUrl}` ;
    window.location.href = fileDownloadUrl;
    pwIsf.removeLayer();
}

function getDeliverablePlanColumnData(data) {
    if (data.workFlowCreated && data.hasDeliverablePlan) {
        return `<pre>${data.scopeName}(${data.deliverablePlanId})</pre>`;
    }
    else {
        return `<pre>${data.scopeName}</pre>`;
	}
}
function getExternalWorkPlanTemplateColumnData(data) {
    if (data.externalWorkplanTemplate === null || data.externalWorkplanTemplate==="") {
        return `<pre>NA</pre>`;
    }
    else {
        return `<pre>${data.externalWorkplanTemplate}</pre>`;
    }
}

function selectScopeType(selectedDeliverableUnit) {
    $(C_SELECTSCOPETYPEID).empty();
    $.isf.ajax({
        url: `${service_java_URL}projectManagement/getScopeType?deliverableUnit=${selectedDeliverableUnit}`,
        success: function (data) {           
            if (data.responseData !== null) {
                $("#divScopeTypeMethod").css("display", "block");
                $("#scopeType").css("display", "block");
                $(C_SELECTSCOPETYPEID).append(`<option value="0">Select Scope Type</option>`);
                $(C_SELECTSCOPETYPEID).index = 0;
                $.each(data.responseData, function (i, d) {
                    $(C_SELECTSCOPETYPEID).append(`<option value="${d.ScopeTypeID}">${d.ScopeType}</option>`);
                })
                $(C_SELECTSCOPETYPEID).val("0");
            } else {
                $("#scopeType").css("display", "none");
            }
            if (modeObject.selectScopeTypeValue) {
                $(C_SELECTSCOPETYPEID).val(modeObject.selectScopeTypeValue).trigger("change");
			}
        },
        error: function (xhr, status, statusText) {
            console.log('An error occurred on getScopeType');
        }
    });
}

function selectMethod(selectedDeliverableUnit) {
    $(C_SELECTMETHODID).empty();
    $.isf.ajax({
        url: `${service_java_URL}projectManagement/getMethodForDU?deliverableUnit=${selectedDeliverableUnit}`,
        success: function (data) {
            if (data.responseData != null) {
                $("#divScopeTypeMethod").css("display", "block");
                $("#method").css("display", "block");
                $(C_SELECTMETHODID).append(`<option value="0">Select Method</option>`);
                $(C_SELECTMETHODID).index = 0;
                $.each(data.responseData, function (i, d) {
                    $(C_SELECTMETHODID).append(`<option value="${d.MethodID}">${d.Method}</option>`);
                })
                $(C_SELECTMETHODID).val("0");
            }
            if (modeObject.selectMethodValue) {
                $(C_SELECTMETHODID).val(modeObject.selectMethodValue).trigger("change");
            }
           
        },
        error: function (xhr, status, statusText) {
            console.log('An error occurred on getScopeType');
        }
    });
}
function isValidReferenceName(OK) {
    $('#reference_name-Required').text("");
    if ($(C_SELECTEXTRESOURCEID).val() === 1) {
        if ($(C_SELECTREFERENCENAMEID).val() !== null && $(C_SELECTREFERENCENAMEID).val() !== "") {
            const letterNumber = /^[0-9_a-z_ _A-Z_ ]+$/;
            if (!$(C_SELECTREFERENCENAMEID).val().match(letterNumber)) {
                $('#reference_name-Required').text("Only letters and numbers");
                OK = false;
            }
            else {
                if ($(C_SELECTREFERENCENAMEID).val().length > 30) {
                    $('#reference_name-Required').text("Reference name cannot exceed 30 characters");
                    OK = false;
                }
            }
        }
    }
    return OK;
}
function isValidSopeName(OK) {
    if (!isDeliverableNameAndStatusValidated()) {
        OK = false;
    }
    return OK;
}
function isValidExtResource(OK) {
    $("#select_ext_resource-Required").text("");
    if ($(C_SELECTEXTRESOURCEID).val() === null || $(C_SELECTEXTRESOURCEID).val() === 0) {
        $('#select_ext_resource-Required').text("Source is required");
        OK = false;
    } 
    return OK;
}
function isValidScopeStatus(OK) {
    $("#select_scope_status-Required").text("");
    if ($(C_SELECTSCOPESTATUSID).val() === null || $(C_SELECTSCOPESTATUSID).val() === 0 || $("select_scope_status").val() === "") {
        $('#select_scope_status-Required').text("Source is required");
        OK = false;
    }
    return OK;
}
function isValidDates(OK) {

    const startDate = moment($(C_STARTDATEID).val(),'YYYY-MM-DD', true);
    const endDate = moment($(C_ENDDATEID).val(),'YYYY-MM-DD', true);
    $('#start_date1-Required').text("");
    if (startDate.isValid()) {
        if ($(C_STARTDATEID).val() === null || $(C_STARTDATEID).val() === "") {
            $('#start_date1-Required').text("Start date is required");
            OK = false;
        }
    } else {
        $('#start_date1-Required').text("Invalid Start date");
        OK = false;
	}
    $('#end_date1-Required').text("");
    if (endDate.isValid()) {
        if ($(C_ENDDATEID).val() === null || $(C_ENDDATEID).val() === "") {
            $('#end_date1-Required').text("End date is required");
            OK = false;
        }
    } else {
        $('#end_date1-Required').text("Invalid End date.");
        OK = false;
	}
    if (defaultDate > startDate) {
        $("#start_date1-Required").text(`Not to preceed ${startDate}`);
        OK = false;
    }
    else {
        if (startDate > endDate) {
            $('#end_date1-Required').text(" End date must be after Start date");
            OK = false;
        }
    }
    return OK;
}
function getScopeData() {
    const scopeName = $.trim($(C_SCOPENAMEID).val());
    const requestType = $(C_SELECTREQSTTYPEID).val();
    const deliverableUnit = $(C_SELECTDELEVERABLEUNITID).val();
    const startDate = getFormat($(C_STARTDATEID).val());
    const endDate = getFormat($(C_ENDDATEID).val());
    let projectID = localStorage.getItem("views_project_id");
    const vendorCode = $(C_SELECTASPVENDORID).val();
    const sourceId = $("#select_ext_resource option:selected").val();
    var externalReference = "";
    var externalProjectID = "";
    var externalWorkplanTemplate = "";
    var deliverableStatus = $(C_SELECTSCOPESTATUSID).val();
    let externalResource = $("#select_ext_resource option:selected").text();
    if (externalResource === "ISF") {
        externalReference = $(C_SELECTREFERENCENAMEID).val();
	}
    else {
        externalReference = $('#externalActivityDropDown :selected').text();
        externalProjectID = $("#select_ext_project option:selected").val();
        externalWorkplanTemplate = $("#ext_source_wo_template option:selected").val();
    }
    externalReferenceText = $('#externalActivityDropDown :selected').text();
    if (projectID === null) {
        projectID = 10;
	}
    const createdBy = signumGlobal;
    let remarks = $("#msgBox").val();
    if (remarks === "") {
        remarks = null;
    }
    var scopeObj = {};
    scopeObj.scopeName = scopeName;
    scopeObj.vendorCode = vendorCode;
    scopeObj.deliverableUnit = deliverableUnit;
    scopeObj.requestType = requestType;
    scopeObj.startDate = startDate;
    scopeObj.endDate = endDate;
    scopeObj.projectID = projectID;
    scopeObj.createdBy = createdBy;
    scopeObj.source = sourceId;
    scopeObj.externalReference = externalReference;
    scopeObj.externalProjectId = externalProjectID;
    scopeObj.externalWorkplanTemplate = externalWorkplanTemplate;
    scopeObj.deliverableStatus = deliverableStatus;
    scopeObj.scopeTypeId = $(C_SELECTSCOPETYPEID).val();
    scopeObj.methodID = $(C_SELECTMETHODID).val();
    scopeObj.remarks = remarks;
    scopeObj.operatorCountId = $(C_SELECTOPERATORCOUNTID).val();
    scopeObj.projectFinancialsID = $(C_SELECTPROJECTFINANCIALID).val();
    return scopeObj;
}

function closeAddDeliverableDiv() {
    $("#divAddDeliverableScope").hide();
    $("#divAddDeliverableScope").animate({ right: '0%', opacity: 1.0 }, "slow");
}
function openAddDeliverableDiv() {
    $("#divAddDeliverableScope").show();
    $("#divAddDeliverableScope").animate({ right: '0%', opacity: 1.0 }, "slow");
    if (localStorage.getItem("projectType") === 'ASP') {
        selectAspVendor();
        $('#selectRequestType').remove();
    }
    else {
        selectRequestType();
        $('#aspSelectVendor').remove();
    }
}

function selectProjectFinancials(selectedDeliverableUnit) {
    $(C_SELECTPROJECTFINANCIALID).empty();
    $.isf.ajax({
        url: `${service_java_URL}projectManagement/getProjectFinancials?deliverableUnit=${selectedDeliverableUnit}`,
        success: function (data) {
            if (data.responseData != null) {
                $("#projectFinancials").css("display", "block");
                $("#projectFinancialOCDiv").css("display", "block");
                $("#divRemarks").css("margin-top", "13px");
                $(C_SELECTPROJECTFINANCIALID).append(`<option value="0">Select Deliverable Type</option>`);
                $(C_SELECTPROJECTFINANCIALID).index = 0;
                $.each(data.responseData, function (i, d) {
                    $(C_SELECTPROJECTFINANCIALID).append(`<option value="${d.ProjectFinancialsID}">${d.ProjectFinancials}</option>`);
                })
                $(C_SELECTPROJECTFINANCIALID).val("0");
            } else {
                $("#divRemarks").css("margin-top", "13px");
			}
            if (modeObject.selectProjectFinancialValue) {
                $(C_SELECTPROJECTFINANCIALID).val(modeObject.selectProjectFinancialValue).trigger("change");
            }
        },
        error: function (xhr, status, statusText) {
            console.log('An error occurred on getProjectFinancials');
        }
    });
}

function selectOperatorCount(selectedDeliverableUnit) {
    $(C_SELECTOPERATORCOUNTID).empty();
    $.isf.ajax({
        url: `${service_java_URL}projectManagement/getOperatorCount?deliverableUnit=${selectedDeliverableUnit}`,
        success: function (data) {
            if (data.responseData != null) {
                $("#operatorCount").css("display", "block");
                $(C_SELECTOPERATORCOUNTID).index = 0;
                $.each(data.responseData, function (i, d) {
                    $(C_SELECTOPERATORCOUNTID).append(`<option value="${d.OperatorCountID}">${d.OperatorCount}</option>`);
                })
                
            }
            if (modeObject.selectOperatorCountValue) {
                $(C_SELECTOPERATORCOUNTID).val(modeObject.selectOperatorCountValue).trigger("change");
            }
            pwIsf.removeLayer();
        },
        error: function (xhr, status, statusText) {
            console.log('An error occurred on getProjectFinancials');
            pwIsf.removeLayer();
        }
    });
}
function urlForScopeByProject(statusText, ProjectID) {
    let ServiceUrl = '';
    if (statusText == null) {
        ServiceUrl = `${service_java_URL}/projectManagement/ScopeByProject?projectId=${parseInt(ProjectID)}&deliverableStatus=Active`;
    }
    else {
        ServiceUrl = `${service_java_URL}/projectManagement/ScopeByProject?projectId=${parseInt(ProjectID)}&deliverableStatus=${statusText}`;
    }
    return ServiceUrl;
}
function clearValueForEditDropdown() {
        if (modeObject) {
            modeObject.selectScopeTypeValue = null;
            modeObject.selectMethodValue = null;
            modeObject.selectProjectFinancialValue = null;
            modeObject.selectOperatorCountValue = null;
            modeObject.selectRequestTypeValue = null;
		}  
}

function validateScope() {
    let OK = true;
        OK = isValidReferenceName(OK);
    if (OK === true) {
        OK = isValidSopeName(OK);
    }
    if (OK === true) {
        OK = isValidateSource(OK);
	}
    if (OK === true) {
        OK = isValidExtResource(OK);
    }
    if (OK === true) {
        OK = isValidScopeStatus(OK);
    }
    if (OK === true) {
        OK = isValidDates(OK);
    }
    if (OK === true) {
        OK = isValidateExtProject(OK);
    }
    return OK;
}
function isValidateExtProject(OK) {
    $(REFWOTEMPLATEFIELDID).text("");
    $(EXTPROJECTREQUIREDFIELDID).text("");
    $(EXTERNALACTIVITYREQUIREDID).text("");
    const sourceTxt = $("#select_ext_resource option:selected").text();
    const externalProjectID = $("#select_ext_project option:selected").val();
    const externalWorkplanTemplate = $("#ext_source_wo_template option:selected").val();
    if (sourceTxt.toLocaleLowerCase() !== "isf" && sourceTxt.toLocaleLowerCase() !== "select one" && !$(C_SELECTEXTRESOURCEID).is('[disabled=disabled]')) {
            if (externalProjectID === "0" || externalProjectID === "") {
                $(EXTPROJECTREQUIREDFIELDID).text("External project required");
                OK = false;
            }
            if (externalWorkplanTemplate === "0" || externalWorkplanTemplate === "") {
                $(REFWOTEMPLATEFIELDID).text("External workplan template required");
                OK = false;
            }
            if ($(C_SELECTEXTACTIVITYDROPDOWNID).val() === "0") {
                $(EXTERNALACTIVITYREQUIREDID).text("External Reference required");
                OK = false;
            }

        }
    return OK;
}
function ajaxPostCallSaveScope(serviceData) {
    pwIsf.addLayer({ text: 'Please wait ...' });
    $.isf.ajax({
        url: `${service_java_URL}projectManagement/SaveScope`,
        context: this,
        crossdomain: true,
        processData: true,
        contentType: 'application/json',
        type: 'POST',
        data: serviceData,
        xhrFields: {
            withCredentials: false
        },
        success: function (data) {
            if (data.isValidationFailed === false) {
                closeAddDeliverableDiv();
                pwIsf.alert({ msg: "Deliverable is added.", type: 'success', autoClose: 3 });
                getTableInfoScope();
                cleanScopeFields();

            } else if (data.isValidationFailed === true) {
                pwIsf.alert({ msg: data.formWarnings[0], type: 'warning' });
            }
        },
        error: function (xhr, status, statusText) {
            const err = JSON.parse(xhr.responseText);
            pwIsf.alert({ msg: err.errorMessage, type: 'warning' });
        },
        complete: function (xhr, statusText) {
            pwIsf.removeLayer();
        }
    })
}

function setObjectDeliverableUnit(data) {

    prevObject.ScopeName = data.data[0].ScopeName;
    let deliverableUnit = data.data[0].deliverableUnit;
    if (deliverableUnit !== null) {
        deliverableUnit = deliverableUnit.toString();
    }
    prevObject.DeliverableUnit = deliverableUnit;
    prevObject.RequestType = data.data[0].requestType;
    prevObject.Vendor = data.data[0].vendorCode;
    prevObject.StartDate = data.data[0].StartDate;
    prevObject.EndDate = data.data[0].EndDate;
    prevObject.Source = data.data[0].source.toString();
    prevObject.ExternalReference = data.data[0].externalReference;
    prevObject.deliverableStatus = data.data[0].deliverableStatus;
    prevObject.Remarks = data.data[0].Remarks;
    prevObject.ScopeTypeID = data.data[0].ScopeTypeID;
    prevObject.MethodID = data.data[0].MethodID;
    prevObject.ProjectFinancialsID = data.data[0].ProjectFinancialsID;
    prevObject.OperatorCount = data.data[0].OperatorCountID;
    prevObject.ExternalProject = 0;
    prevObject.extWoTemplate = 0;
    prevObject.extActivity = 0;
}

function enableUpdateButton() {
    $("#btnUpdateScope").prop("disabled", false);
}
function desableUpdateButton() {
    $("#btnUpdateScope").prop("disabled", true);
}


function detectChangeForUpdate(IdControle) {
    switch (IdControle) {
        case "scope_name":
            changeUpdateBtnStatusForScopeName(IdControle);
            break;
        case "select_deliverable_unit":          
            enableDisableUpdateBtnForDelUnit(IdControle);
            break;
        case "start_date1":
            changeUpdateBtnStatusForStartDate(IdControle);
            break;
        case "end_date1":
            changeUpdateBtnStatusForEndDate(IdControle);
            break;
        case "select_req_type":
            changeUpdateBtnStatusForRequestType(IdControle);
            break;
        case "select_asp_vendor":
            changeUpdateBtnStatusForVendorCode(IdControle);
            break;
        case "select_ext_resource":           
            enableDisableUpdateBtnForExtResource(IdControle);
            break;
        case "select_scope_status":
            enableDisableUpdateBtnForScopeStatus(IdControle);
            break;
        case "reference_name":
            enableDisableUpdateBtnForExtRefrence(IdControle);
            break;
        case "select_ext_project":
            enableDisableExtProject(IdControle);
            break;
        case "ext_source_wo_template":
            enableDisableUpdateBtnForExtWOTemplateFiled(IdControle);
            break;
        case "externalActivityDropDown":
            enableDisableUpdateBtnForExtActivityDropDownFiled(IdControle);
            break;
        case "selectMethod":         
            enableDisableUpdateForMethodFiled(IdControle);
            break;
        case "selectScopeType":
            enableDisableUpdateForScopeTypeFiled(IdControle);
            break;
        case "selectProjectFinancial":
            enableDisableUpdateForDeliverableTypeFiled(IdControle);
            break;
        case "selectOperatorCount":
            enableDisableUpdateForDeliverableUnitFiled(IdControle); 
            break;
        case "msgBox":
            enableDisableUpdateForRemarksFiled(IdControle);
            break;
    }
}
function enableDisableUpdateForRemarksFiled(IdControle) {
    if (prevObject.Remarks !== null && prevObject.Remarks != '') {
        if ($(`#${IdControle}`).val() === "" || prevObject.Remarks !== $(`#${IdControle}`).val() && $(`#${IdControle}`).val()) {
            enableUpdateButton();
        }
        else {
            desableUpdateButton();
        }
    }
    else {
        if ($(`#${IdControle}`).val() !== "" && prevObject.Remarks !== $(`#${IdControle}`).val() && $(`#${IdControle}`).val()) {
            enableUpdateButton();
        }
        else {
            desableUpdateButton();
        }
    }
}
function enableDisableUpdateForDeliverableUnitFiled(IdControle) {
    if (prevObject.DeliverableUnit !== $(C_SELECTDELEVERABLEUNITID).val() || prevObject.OperatorCount !== parseInt($(`#${IdControle}`).val()) && parseInt($(`#${IdControle}`).val())) {
        enableUpdateButton();
    }
    else {
        desableUpdateButton();
    }
}
function enableDisableUpdateForDeliverableTypeFiled(IdControle) {
    if (prevObject.ProjectFinancialsID !== null) {
        if (prevObject.DeliverableUnit !== $(C_SELECTDELEVERABLEUNITID).val() || $(`#${IdControle}`).val() === "0" || prevObject.ProjectFinancialsID !== parseInt($(`#${IdControle}`).val()) && parseInt($(`#${IdControle}`).val())) {
            enableUpdateButton();
        }
        else {
            desableUpdateButton();
        }
    }
    else {
        if (prevObject.DeliverableUnit !== $(C_SELECTDELEVERABLEUNITID).val() || $(`#${IdControle}`).val() !== "0" && prevObject.ProjectFinancialsID !== parseInt($(`#${IdControle}`).val()) && parseInt($(`#${IdControle}`).val())) {
            enableUpdateButton();
        }
        else {
            desableUpdateButton();
        }
    }
}
function enableDisableUpdateForScopeTypeFiled(IdControle) {
    if (prevObject.ScopeTypeID !== null) {

        if (prevObject.DeliverableUnit !== $(C_SELECTDELEVERABLEUNITID).val() || $(`#${IdControle}`).val() === "0" || prevObject.ScopeTypeID !== parseInt($(`#${IdControle}`).val()) && parseInt($(`#${IdControle}`).val())) {
            enableUpdateButton();
        }
        else {
            desableUpdateButton();
        }
    }
    else {
        if (prevObject.DeliverableUnit !== $(C_SELECTDELEVERABLEUNITID).val() || $(`#${IdControle}`).val() !== "0" && prevObject.ScopeTypeID !== parseInt($(`#${IdControle}`).val()) && parseInt($(`#${IdControle}`).val())) {
            enableUpdateButton();
        }
        else {
            desableUpdateButton();
        }
    }  
}
function enableDisableUpdateForMethodFiled(IdControle) {
    if (prevObject.MethodID !== null) {
        if (prevObject.DeliverableUnit !== $(C_SELECTDELEVERABLEUNITID).val() || $(`#${IdControle}`).val() === "0" || prevObject.MethodID !== parseInt($(`#${IdControle}`).val()) && parseInt($(`#${IdControle}`).val())) {
            enableUpdateButton();
        }
        else {
            desableUpdateButton();
        }
    }
    else {
        if (prevObject.DeliverableUnit !== $(C_SELECTDELEVERABLEUNITID).val() || $(`#${IdControle}`).val() !== "0" && prevObject.MethodID !== parseInt($(`#${IdControle}`).val()) && parseInt($(`#${IdControle}`).val())) {
            enableUpdateButton();
        }
        else {
            desableUpdateButton();
        }
    }
}
function enableDisableUpdateBtnForExtActivityDropDownFiled(IdControle) {
    if ($(C_SELECTEXTRESOURCEID).val() === "2" && prevObject.Source !== 2) {
        if (prevObject.Source !== $(C_SELECTEXTRESOURCEID).val() || prevObject.extActivity !== parseInt($(`#${IdControle}`).val()) && parseInt($(`#${IdControle}`).val())) {
            enableUpdateButton();
        }
        else {
            desableUpdateButton();
        }
    }
}
function enableDisableUpdateBtnForExtWOTemplateFiled(IdControle) {
    if ($(C_SELECTEXTRESOURCEID).val() === "2" && prevObject.Source !== 2) {
        if (prevObject.Source !== $(C_SELECTEXTRESOURCEID).val() || prevObject.extWoTemplate !== parseInt($(`#${IdControle}`).val()) && parseInt($(`#${IdControle}`).val())) {
            enableUpdateButton();
        }
        else {
            desableUpdateButton();
        }
    }
}
function enableDisableExtProject(IdControle) {
    if ($(C_SELECTEXTRESOURCEID).val() === "2" && prevObject.Source !== 2) {
        if (prevObject.Source !== $(C_SELECTEXTRESOURCEID).val() || prevObject.ExternalProject !== parseInt($(`#${IdControle}`).val()) && parseInt($(`#${IdControle}`).val())) {
            enableUpdateButton();
        }
        else {
            desableUpdateButton();
        }
    }
}
function changeUpdateBtnStatusForScopeName(IdControle) {
    if ($(`#${IdControle}`).val() === '' || prevObject.ScopeName !== $(`#${IdControle}`).val() && $(`#${IdControle}`).val()) {
        enableUpdateButton();
    }
    else {
        desableUpdateButton();
    }
}
function enableDisableUpdateBtnForExtRefrence(IdControle) {
    if (prevObject.ExternalReference !== null && prevObject.ExternalReference != "") {
        if ($(`#${IdControle}`).val() === "" || prevObject.ExternalReference !== $(`#${IdControle}`).val() && $(`#${IdControle}`).val()) {
            enableUpdateButton();
        }
        else {
            desableUpdateButton();
        }

    }
    else {
        if ($(`#${IdControle}`).val() !== "" && prevObject.ExternalReference !== $(`#${IdControle}`).val() && $(`#${IdControle}`).val()) {
            enableUpdateButton();
        }
        else {
            desableUpdateButton();
        }
    }
}
function enableDisableUpdateBtnForScopeStatus(IdControle) {
    if (prevObject.deliverableStatus !== $(`#${IdControle}`).val() && $(`#${IdControle}`).val()) {
        enableUpdateButton();
    }
    else {
        desableUpdateButton();
    }
}
function enableDisableUpdateBtnForExtResource(IdControle) {
    if (prevObject.Source !== $(`#${IdControle}`).val() && $(`#${IdControle}`).val()) {
        enableUpdateButton();
    }
    else {
        desableUpdateButton();
    }
}
function enableDisableUpdateBtnForDelUnit(IdControle) {
    if (prevObject && prevObject.DeliverableUnit) {
        if (prevObject.DeliverableUnit !== $(`#${IdControle}`).val() && $(`#${IdControle}`).val()) {
            enableUpdateButton();
        }
        else {
            desableUpdateButton();
        }
    }
}
function changeUpdateBtnStatusForVendorCode(IdControle) {
    if (prevObject.Vendor !== $(`#${IdControle}`).val() && $(`#${IdControle}`).val()) {
        enableUpdateButton();
    }
    else {
        desableUpdateButton();
    }
}
function changeUpdateBtnStatusForEndDate(IdControle) {
    if (prevObject.EndDate !== undefined) {
        if (prevObject.EndDate !== $(`#${IdControle}`).val() && $(`#${IdControle}`).val()) {
            enableUpdateButton();
        }
        else {
            desableUpdateButton();
        }
    }
}
function changeUpdateBtnStatusForStartDate(IdControle) {
    if (prevObject.StartDate !== undefined) {
        if (prevObject.StartDate !== $(`#${IdControle}`).val() && $(`#${IdControle}`).val()) {
            enableUpdateButton();
        }
        else {
            desableUpdateButton();
        }
    }
}
function changeUpdateBtnStatusForRequestType(IdControle) {
    if (prevObject.RequestType !== $(`#${IdControle}`).val() && $(`#${IdControle}`).val()) {
        enableUpdateButton();
    }
    else {
        desableUpdateButton();
    }
}
function clearPrevObjectFields() {
    prevObject.ScopeName = undefined;
    prevObject.DeliverableUnit = undefined;
    prevObject.RequestType = undefined;
    prevObject.Vendor = undefined;
    prevObject.StartDate = undefined;
    prevObject.EndDate = undefined;
    prevObject.Source = undefined;
    prevObject.ExternalReference = undefined;
    prevObject.deliverableStatus = undefined;
    prevObject.Remarks = undefined;
    prevObject.ScopeTypeID = undefined;
    prevObject.MethodID = undefined;
    prevObject.ProjectFinancialsID = undefined;
    prevObject.OperatorCount = undefined;
    prevObject.ExternalProject = 0;
    prevObject.extWoTemplate = 0;
    prevObject.extActivity = 0;
}