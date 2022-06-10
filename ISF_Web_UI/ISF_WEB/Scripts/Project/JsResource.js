function onCalendarClick() {
    $('.ep_calendar_icon').on("click", function (event) {
        $(this).siblings('.ep_datepicker').datepicker('show');
    });
}

function ChangeStartDate100() {
    if ($('#end_date_dr').val() != "") {
        var MS_PER_DAY = 24 * 60 * 60 * 1000;
        var d1 = $('#start_date_dr').datepicker('getDate');
        var d2 = $('#end_date_dr').datepicker('getDate');
        var work = (Math.floor((d2 - d1) / MS_PER_DAY)) + 1;
        if (work >= 0) {
            var d = d1;
            while (d <= d2) {
                if ((d.getDay() || 7) > 5) { // Sat/Sun
                    work--;
                }
                d.setDate(d.getDate() + 1);
            }
            var fteVal, hours;
            $('#duration').val(work);
            var horas = work * 8;
            $('#FTE').mouseout(function (tecla) {
                if ($('#FTE').val() == "")
                    $('#FTE').val(100);
                else
                    fteVal = $('#FTE').val();
                 hours = (fteVal / 100) * horas;
                $('#HourDays').val(hours);
            });
            if ($('#FTE').val() == "")
                $('#FTE').val(100);
            else
                fteVal = $('#FTE').val();
             hours = (fteVal / 100) * horas;
            $('#HourDays').val(hours);
        }
        else {
            $('#end_date_dr').val("");
            $('#duration').val("");
            $('#HourDays').val("");
        }
    }
}

function ChangeStartDateEdit() {
    if ($('#end_dateEdit').val() != "") {
        var MS_PER_DAY = 24 * 60 * 60 * 1000;
        var d1 = $('#start_dateEdit').datepicker('getDate');
        var d2 = $('#end_dateEdit').datepicker('getDate');
        var work = (Math.floor((d2 - d1) / MS_PER_DAY)) + 1;
        if (work >= 0) {
            var d = d1;
            while (d <= d2) {
                if ((d.getDay() || 7) > 5) { // Sat/Sun
                    work--;
                }
                d.setDate(d.getDate() + 1);
            }
            var fteVal, hours;
            $('#durationEdit').val(work);
            var horas = work * 8;
            $('#FTEEdit').mouseout(function (tecla) {
                if ($('#FTEEdit').val() == "")
                    $('#FTEEdit').val(100);
                else
                    fteVal = $('#FTEEdit').val();
                 hours = (fteVal / 100) * horas;
                $('#HourDaysEdit').val(hours);
            });
            if ($('#FTEEdit').val() == "")
                $('#FTEEdit').val(100);
            else
                fteVal = $('#FTEEdit').val();
             hours = (fteVal / 100) * horas;
            $('#HourDaysEdit').val(hours);
        } else {
            $('#end_dateEdit').val("");
            $('#durationEdit').val("");
            $('#HourDaysEdit').val("");
        }
    }
}

function ChangeEndDate100()
{
    changeDate();
}

function changeDate()
{
    var MS_PER_DAY = 24 * 60 * 60 * 1000;
    var d1 = $('#start_date_dr').datepicker('getDate');
    var d2 = $('#end_date_dr').datepicker('getDate');
    var work = (Math.ceil((d2 - d1) / MS_PER_DAY)) + 1;
    if (work >= 0) {
        var d = d1;
        while (d <= d2) {
            if ((d.getDay() || 7) > 5) { // Sat/Sun
                work--;
            }
            d.setDate(d.getDate() + 1);
        }
        
        var fteVal, hours;
        $('#duration').val(work);
        var horas = work * 8;
        $('#FTE').mouseout(function (tecla) {
            if ($('#FTE').val() == "")
                $('#FTE').val(100);
            else
                fteVal = $('#FTE').val();
             hours = (fteVal / 100) * horas;
            $('#HourDays').val(hours);
        });
        if ($('#FTE').val() == "")
            $('#FTE').val(100);
        else
            fteVal = $('#FTE').val();
         hours = (fteVal / 100) * horas;
        $('#HourDays').val(hours);

    } else {
        $('#end_date_dr').val("");
        $('#duration').val("");
        $('#HourDays').val("");
    }
}

function getHoursByFTE()
{
    var fteVal = $("#FTE").val();
    var duration = $("#duration").val();
    var hours = (fteVal / 100) * duration * 8;
    $("#HourDays").val(hours);

}

function getHoursByFTEEdit()
{
    var fteValEdit = $("#FTEEdit").val();
    var durationEdit = $("#durationEdit").val();
    var hoursEdit = (fteValEdit / 100) * durationEdit * 8;
    $("#HourDaysEdit").val(hoursEdit);
}

function ChangeEndDateEdit() {
    var MS_PER_DAY = 24 * 60 * 60 * 1000;
    var d1 = $('#start_dateEdit').datepicker('getDate');
    var d2 = $('#end_dateEdit').datepicker('getDate');
    var work = (Math.floor((d2 - d1) / MS_PER_DAY)) + 1;
    if (work >= 0) {
        var d = d1;
        while (d <= d2) {
            if ((d.getDay() || 7) > 5) { // Sat/Sun
                work--;
            }
            d.setDate(d.getDate() + 1);
        }
        var fteVal, hours;
        $('#FTEEdit').mouseout(function (tecla) {
            if ($('#FTEEdit').val() == "")
                $('#FTEEdit').val(100);
            else
                fteVal = $('#FTEEdit').val();
             hours = (fteVal / 100) * horas;
            $('#HourDaysEdit').val(hours);
        });
        $('#durationEdit').val(work);
        var horas = work * 8;
        if ($('#FTEEdit').val() == "")
            $('#FTEEdit').val(100);
        else
            fteVal = $('#FTEEdit').val();
         hours = (fteVal / 100) * horas;
        $('#HourDaysEdit').val(hours);

    } else {
        $('#end_dateEdit').val("");
        $('#durationEdit').val("");
        $('#HourDaysEdit').val("");
    }
}

function getTableScopeResource() {
    //Rajeev
    onCalendarClick();

    getJobStage();

    getJobRoles();

    getVendorsDR();
    //Rajeev
        projectScopeID = $("#scope_Id").val();

        if ($.fn.dataTable.isDataTable('#tbScopeBody')) {
            oTables.destroy();
            $('#tbScopeBody').empty();

        }
        pwIsf.addLayer({ text: "Please wait ..." });
        
        $.isf.ajax({
            url: service_java_URL + "activityMaster/getScopeByScopeId?projectScopeID=" + ScopeID,
            success: function (data) {
                pwIsf.removeLayer();
                $("#scope_name").html(data.scopeName);
            },
            error: function (xhr, status, statusText) {
                pwIsf.removeLayer();
                console.log("Fail " + xhr.responseText);
                console.log('An error occurred');
            }

        });
    pwIsf.addLayer({ text: "Please wait ..." });
    //let service_java_URL = 'http://100.97.133.2:8080/isf-rest-server-java/';
        $.isf.ajax({
            url: service_java_URL + "activityMaster/getSubScopebyScopeID?projectScopeID=" + ScopeID,
            success: function (data) {
                pwIsf.removeLayer();
                var html = "";
                if (data.isValidationFailed == false) {
                    $.each(data.responseData, function (i, d) {
                    });
                }
                //else {
                //    pwIsf.alert({ msg: data.formErrors[0], type: 'error' });
                //}
            },
            error: function (xhr, status, statusText) {
                pwIsf.removeLayer();
                console.log("Fail " + xhr.responseText);
                console.log('An error occurred');
            }
        });
        pwIsf.addLayer({ text: "Please wait ..." });
    $.isf.ajax({
        url: service_java_URL + "demandForecast/getAllScopeDetailsByProject?projectId=" + projectID,
            async: false,
            success: function (data) {
                pwIsf.removeLayer();
                $.each(data, function (i, d) {
                   
                    d.actionIcon = "<i style='cursor: pointer;' data-toggle='modal' data-toggle='tooltip' title='Add Resource' data-target='#AddR' data-servSubservArea='" + d.ServiceArea + "' data-domainSubdomain='" + d.Domain + "' data-tech='" + d.Technology + "' onclick='ModalAddOpen(" + d.ProjectScopeDetailID + ")' class='fa fa-plus modalAddResource'></i>&thinsp;&thinsp;&thinsp;&thinsp;<i style='cursor: pointer;' data-toggle='modal' data-toggle='tooltip' title='View Resources' data-target='#AllR' onclick='ModalAllOpen(" + d.ProjectScopeDetailID + ")' class='fa fa-eye'></i>&thinsp;&thinsp;&thinsp;&thinsp;"
                   
                });

                $("#tbScopeBody").append($('<tfoot><tr><th></th><th>Deliverable Name</th><th>Domain / Sub Domain</th><th>Service Area / Sub Service Area</th><th>Technology</th></tr></tfoot>'));
                 oTables = $('#tbScopeBody').DataTable({
                    searching: true,
                    responsive: true,
                    "pageLength": 10,
                    "data": data,
                    "destroy": true,
                    colReorder: true,
                    order: [1],
                    dom: 'Bfrtip',
                    buttons: [
                     'colvis', 'excelHtml5'
                    ],
                    "columns": [{ "title": "Action", "targets": 'no-sort', "orderable": false, "searchable": false, "data": "actionIcon" },
                        { "title": "Deliverable Name", "data": "ScopeName" }, { "title": "Domain / Sub Domain", "data": "Domain" },
                        { "title": "Service Area / Sub Service Area", "data": "ServiceArea" }, { "title": "Technology", "data": "Technology" }],
                    initComplete: function () {

                        $('#tbScopeBody tfoot th').each(function (i) {
                            var title = $('#tbScopeBody thead th').eq($(this).index()).text();
                            if (title != "Action")
                                $(this).html('<input type="text" class="form-control" style="font-size:12px;" placeholder="Search ' + title + '" data-index="' + i + '" />');                           
                        });
                        var api = this.api();
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
                });
                $('#tbScopeBody tfoot').insertAfter($('#tbScopeBody thead'));
            },
            error: function (xhr, status, statusText) {
                pwIsf.removeLayer();
                console.log("Fail to scope name" + xhr.responseText);
                console.log('An error occurred');
            }

        });
        $('#tbScopeBody').hide();
    }

$(document).on('click', '.modalAddResource', function ()
{
    $("#detailsTitlePanel").empty();
    var domainSubDomain = $(this).data().domainsubdomain;
    var servSubServArea = $(this).data().servsubservarea;
    var technology = $(this).data().tech;
    $("#detailsTitlePanel").append("<b>Domain/SubDomain:</b> " + domainSubDomain + "&nbsp;<b>ServiceArea/SubServiceArea: </b>" + servSubServArea + "&nbsp;<b>Technology: </b>" + technology);
});

function addAllResourceSource()
{
    AddResourceSource("ALL-GSDU");
    AddResourceSource("GSDU-China");
    AddResourceSource("GSDU-India");
    AddResourceSource("GSDU-Romania");
    AddResourceSource("GSDU-Mexico");
    AddResourceSource("MANA");
    AddResourceSource("MELA");
    AddResourceSource("MMEA");
    AddResourceSource("MNEA");
    AddResourceSource("MOAI");
}

function AddResourceSource(Name)
{
    $('#resourceSource').append('<option value="' + Name + '">' + Name + '</option>');
    $('#resourceSourceEdit').append('<option value="' + Name + '">' + Name + '</option>');
}

function ModalAddOpen(subscopeID) {
    getRRIDs(subscopeID);
    pwIsf.addLayer({ text: "Please wait ..." });
    
    var jqXHR = $.isf.ajax({
        url: service_java_URL + "projectManagement/getProjectDetails?ProjectID=" + projectID,
        success: function (data) {
            pwIsf.removeLayer();
            start = data.startDate;
            $("#start_date_dr").datepicker("setDate", start);
            defaultDate = $('#start_date_dr').datepicker('getDate');
            $('#resourceSource option').filter(function () { return $.trim($(this).text()) == data.opportunityDetails.countryName; }).attr('selected', 'selected');
        },
        error: function (xhr, status, statusText) {
            pwIsf.removeLayer();
        }
    });
    
    SubScopeID = subscopeID;
    $('#select_requestType-Required').text("");
    $('#select_resourceType-Required').text("");
    $('#select_jobStage-Required').text("");
    $('#select_jobRole-Required').text("");
    $('#OnsiteCount-Required').text("");
    $('#RemoteCount-Required').text("");
    $('#FTE-Required').text("");
    $('#start_date-Required').text("");
    $('#end_date-Required').text("");
    $('#select_Competence-Required').text("");
    $('#select_Certificate-Required').text("");
    //codigo solucion linea 8 - Limpia campos Eve 
    $('#select_requestType').val("0");
    $("#select_vendorDR").select2("val", "");
    $('#select_vendorDR').empty();
    $("#resourceSource").select2("val", "");
    $('#resourceSource').empty();
    addAllVendors();
    addAllResourceSource();

    $('#select_resourceType').val("0");
    $('#select_resourceType').val("0");
    $('#OnsiteCount').val("");
    $('#select_jobStage').val("0");
    $('#RemoteCount').val("");

    $('#select_OnsiteCountry').val("0");
    $('#select_OnsiteState').val("0");
    $('#select_OnsiteCity').val("0");

    $('#select_jobRole').val("0");
    $('#select_vendorDR').val("0");
    $('#start_date_dr').val("");
    $('#end_date_dr').val("");
    $('#FTE').val("");
    $('#duration').val("");
    $('#HourDays').val("");
    $('#select_Level').val("0");
    $('#select_Competence').val("0");
    $('#select_Competence').text("");
    $('#tBodyCompetence tr').remove();
    $('#select_Issue').val("0");
    $('#select_Certificate').val("0");
    $('#select_Certificate').text("");
    $('#tBodyCertificate tr').remove();
    $('#thCompetence').hide();  
    $('#thCertificate').hide();
    $("#search").val('');
    $('#resource_description').val("");
    $('#locationAddAutocomplete-Required').text("");
    $('#locationAddAutocomplete').val("");
    // Fin

    $("lblScopeID").val(ScopeID);
    $("lblSubScopeID").val(SubScopeID);
    
    getAllIssues();
}

function isResourceAllocated(RRID) {
     $.isf.ajax({
         url: service_java_URL + "cRManagement/getRRIDFlag/" + RRID,
         async:false,
        success: function (data) {
            ResourceAllocated = data.total;
            ResourceDeleted = data.deleted;
                       
        },
        error: function () {

        }
    });
    return ResourceAllocated;
}

function ModalAllOpen(subscopeID) {
    SubScopeID = subscopeID;
    $('#TbodyResource tr').remove();
    pwIsf.addLayer({ text: "Please wait ..." });
    var serviceUrl = service_java_URL + "demandManagement/getResourceRequests?projectId=" + projectID + "&projectScopeDetailId=" + SubScopeID;
    if (ApiProxy == true) {
        serviceUrl = service_java_URL + "demandManagement/getResourceRequests?projectId=" + projectID + encodeURIComponent("&projectScopeDetailId=" + SubScopeID);
    }
 $.isf.ajax({
     url: serviceUrl,
        success: function (data) {
            pwIsf.removeLayer();
            var html = "";
            $.each(data, function (i, d) {
                //Get Vendor Names Mapping through IDs
                var task_name = [];
                if ((d.vendor.length) > 0) {
                    for (var l = 0; l <= d.vendor.length; l++) {
                        var vendor_name = vendorData.map(function (task, index, array) {
                            if (d.vendor[l] == task.vendorID)
                                task_name.push(task.vendor);
                            return task.vendor;
                        });
                    }
                }

                var start = d.startDate;
                var end = d.endDate;
                RRID = d.resourceRequestId; var rrFlag = isResourceAllocated(RRID);

                html = html + "<tr id='trResourceID" + d.resourceRequestId + "'>";
                html = html + "<td>" + d.resourceType;
                html = html + "</td>";
                if (d.vendor.length != 0)
                    html = html + "<td>" + task_name;
                else
                    html = html + "<td>"+"-";
                html = html + "</td>";
                var place = d.resourceCity + "," + d.resourceCountry;
                if (d.resourceCity == null && d.resourceCountry == null)
                    place = "";
                else if (d.resourceCountry == null)
                    place = d.resourceCity;
                else if (d.resourceCity == null)
                    place = d.resourceCountry;

                html = html + "<td>" + place;
                html = html + "</td>";
                html = html + "<td>" + d.jobRoleName;
                html = html + "</td>";
                html = html + "<td>" + d.jobStageName;
                html = html + "</td>";
                html = html + "<td>" + d.onsiteCount;
                html = html + "</td>";
                html = html + "<td>" + d.remoteCount;
                html = html + "</td>";
                html = html + "<td>" + start;
                html = html + "</td>";
                html = html + "<td>" + end;
                html = html + "</td>";
                html = html + "<td>" + d.ftePercent;
                html = html + "</td>";
                html = html + "<td>" + rrFlag;
                html = html + "</td>";
                html = html + "<td>" + ResourceDeleted;
                html = html + "</td>";
               
                if (ResourceAllocated > 0)
                    html = html + "<td></td>";
                else
                    html = html + "<td><i style='cursor: pointer;' data-toggle='modal' data-toggle='tooltip' title='Edit Resource' data-target='#EditR' onclick='ModalEditOpen(" + d.resourceRequestId + ")' class='fa fa-edit'></i></td>";


                html = html + "</tr>";
            });
            $("#TbodyResource").append(html);
        },
        error: function (xhr, status, statusText) {
            pwIsf.removeLayer();
            alert("Fail to get resources ");
        }
   });
   
}

function ModalEditOpen(resourceID) {

    $("EditR").modal('focus');
    $('#select_requestTypeEdit-Required').text("");
    $('#select_resourceTypeEdit-Required').text("");
    $('#select_jobStageEdit-Required').text("");
    $('#select_jobRoleEdit-Required').text("");
    $('#OnsiteCountEdit-Required').text("");
    $('#RemoteCountEdit-Required').text("");


    $('#FTEEdit-Required').text("");
    $('#start_dateEdit-Required').text("");
    $('#end_dateEdit-Required').text("");
    $('#select_CompetenceEdit-Required').text("");
    $('#select_CertificateEdit-Required').text("");
    //codigo solucion linea 8 - Limpia campos Eve 
    $('#select_requestTypeEdit').val("0");
    $("#select_vendorEditDR").select2("val", "");
    $('#select_vendorEditDR').empty();
    $("#resourceSourceEdit").select2("val", "");
    $('#resourceSourceEdit').empty();
    addAllVendors();
    addAllResourceSource();

    $('#select_resourceTypeEdit').val("0");
    $('#OnsiteCountEdit').val("");
    $('#select_jobStageEdit').val("0");
    $('#RemoteCountEdit').val("");

    $('#select_OnsiteCountryEdit').val("0");
    $('#select_OnsiteStateEdit').val("0");
    $('#select_OnsiteCityEdit').val("0");

    $('#select_jobRoleEdit').val("0");
    $('#start_dateEdit').val("");//.val(""); 
    $('#end_dateEdit').val("");
    $('#FTEEdit').val("");
    $('#durationEdit').val("");
    $('#HourDaysEdit').val("");

    $('#select_LevelEdit').val("0");
    $('#select_CompetenceEdit').val("0");
    $('#select_CompetenceEdit').text("");
    $('#tBodyCompetenceEdit tr').remove();
    $('#select_IssueEdit').val("0");
    $('#select_CertificateEdit').val("0");
    $('#select_CertificateEdit').text("");
    $('#tBodyCertificateEdit tr').remove();
    $('#thCompetenceEdit').hide();
    $('#thCertificateEdit').hide(); 
    $('#locationEditAutocomplete-Required').text("");
    $('#locationEditAutocomplete').val("");
    // Fin 

    ResourceID = resourceID;
    $("ResourceID").val(ResourceID);

    $("#OnsiteCountEdit").change(function () {
        minValueForOnsiteCount();
    });

    $("#RemoteCountEdit").change(function () {
        minValueForRemoteCount();
    });

    getAllIssues();
    getValuesEditResources(resourceID);
}
 
function getValuesEditResources()
{
    var varRemoteLocation = "";
    var serviceUrl = service_java_URL + "demandManagement/getResourceRequests?projectId=" + projectID + "&projectScopeDetailId=" + SubScopeID;
    if (ApiProxy == true) {
        serviceUrl = service_java_URL + "demandManagement/getResourceRequests?projectId=" + projectID + encodeURIComponent("&projectScopeDetailId=" + SubScopeID);
    }
    $.isf.ajax({
        url: serviceUrl,
        async: false,
            success: function (data) {
            $.each(data, function (i, d) {
                if (ResourceID == d.resourceRequestId) {
                    minRemoteCountValue = d.remoteCount;
                    minOnsiteCountValue = d.onsiteCount;
                    document.getElementById("RemoteCountEdit").min = d.remoteCount;
                    document.getElementById("OnsiteCountEdit").min = d.onsiteCount;
                    $('#select_requestTypeEdit').val(d.RequestType);                 
                    $('#select_resourceTypeEdit').val(d.resourceType);

                    varRemoteLocation = d.remoteLocation;
                    if (varRemoteLocation != null && varRemoteLocation != "") {
                        var array = varRemoteLocation.split(',');
                        $.each(array, function (index, reg) {
                            $("#resourceSourceEdit option[value='" + reg + "']").prop("selected", true);
                        });
                    }

                    vendorArrayEdit = d.vendor;
                    if (vendorArrayEdit != null && vendorArrayEdit != "") {
                        $.each(vendorArrayEdit, function (index, reg) {
                            $("#select_vendorEditDR option[value='" + reg + "']").prop("selected", true);
                        });
                    }

                    var place = d.resourceCity + "," + d.resourceCountry;
                    if (d.resourceCity == null && d.resourceCountry == null)
                        place = "";
                    else if (d.resourceCountry == null)
                        place = d.resourceCity;
                    else if (d.resourceCity == null)
                        place = d.resourceCountry;
                    $('#locationEditAutocomplete').val(place);
                    $('#OnsiteCountEdit').val(d.onsiteCount);
                    $('#select_jobStageEdit').val(d.jobStageId);
                    $('#RemoteCountEdit').val(d.remoteCount);



                    $('#select_jobRoleEdit').val(d.jobRoleId);
                    var start = d.startDate;
                    $("#start_dateEdit").datepicker("setDate", start.split("/")[0] + "/" + start.split("/")[1] + "/" + start.split("/")[2]);
                    $("#start_dateEdit").datepicker("setDate", start);
                    var end = d.endDate;
                    $("#end_dateEdit").datepicker("setDate", end);
                    $('#FTEEdit').val(d.ftePercent);
                    $('#durationEdit').val(d.duration);
                    $('#HourDaysEdit').val(d.hours);
                    //fill Table Competence                       
                    $.each(d.ResourceRequestCompetences, function (j, com) {
                        var html = "<tr id='trCompEdit" + com.CompetenceID + "'>";
                        html = html + "<td style='display: none;'>" + com.CompetenceID;//ResourceRequestCompetenceID
                        html = html + "</td>";
                        html = html + "<td>" + com.CompetenceName;
                        html = html + "</td>";
                        html = html + "<td>" + com.Level;
                        html = html + "</td>";
                        html = html + "<td><button class='fa fa-trash-o' title='Delete' style='border:none' onclick='DeleteCompetenceEdit(" + com.CompetenceID + "," + com.ResourceRequestCompetenceID + ")'></button>"
                        html = html + "</tr>";
                        $("#tBodyCompetenceEdit").append(html);

                    })
                    //fill table certificate
                    $.each(d.ResourceRequestCertificates, function (j, cer) {
                        var html = "<tr id='trCertEdit" + cer.CertificateID + "'>";
                        html = html + "<td style='display: none;'>" + cer.CertificateID;//ResourceRequestCertificateID
                        html = html + "</td>";
                        html = html + "<td>" + cer.CertificateName;
                        html = html + "</td>";
                        html = html + "<td>" + cer.Issuer; //cer.Issuer;
                        html = html + "</td>";
                        html = html + "<td><button class='fa fa-trash-o' style='border:none' onclick='DeleteCertificateEdit(" + cer.CertificateID + "," + cer.ResourceRequestCertificateID + ")'></button>"
                        html = html + "</tr>";
                        $("#tBodyCertificateEdit").append(html);
                    })
                }
            })
        },
        error: function (xhr, status, statusText) {
            console.log("Fail to edit resource" + xhr.responseText);
            console.log('An error occurred');
        }
    });
}

function minValueForRemoteCount() {
    var newValue = $('#RemoteCountEdit').val();
    if (newValue < minRemoteCountValue) {
        $('#RemoteCountEdit').val(minRemoteCountValue);
    }
}

function minValueForOnsiteCount() {
    var newValue = $('#OnsiteCountEdit').val();
    if (newValue < minOnsiteCountValue) {
        $('#OnsiteCountEdit').val(minOnsiteCountValue);
    }
}

function getValuesByRRID(ResourceID) {
    var serviceUrl = service_java_URL + "demandManagement/getResourceRequests?projectId=" + projectID + "&projectScopeDetailId=" + SubScopeID;
    if (ApiProxy == true) {
        serviceUrl = service_java_URL + "demandManagement/getResourceRequests?projectId=" + projectID + encodeURIComponent("&projectScopeDetailId=" + SubScopeID);
    }
    $.isf.ajax({
        url: serviceUrl,
        success: function (data) {
            $.each(data, function (i, d) {
                if (ResourceID == d.resourceRequestId) {
                    var str = d.remoteLocation;
                    var remoteLocationArray = '';
                    if (str != null) {
                        var remoteLocationArray = str.split(',');
                        for (var i = 0; i < remoteLocationArray.length; i++) {
                            // Trim the excess whitespace.
                            remoteLocationArray[i] = remoteLocationArray[i].replace(/^\s*/, "").replace(/\s*$/, "");
                            
                        }
                    }
                    var vendorArray = '';
                    var vendorStr = d.vendor;
                    if (vendorStr != null) {
                        //var vendorArray = vendorStr.split(',');
                        for (var i = 0; i < vendorStr.length; i++) {
                            // Trim the excess whitespace.
                            vendorStr[i] = vendorStr[i].replace(/^\s*/, "").replace(/\s*$/, "");

                        }
                    }
                    var place = d.resourceCity + "," + d.resourceCountry;
                    if (d.resourceCity == null && d.resourceCountry == null)
                        place = "";
                    else if (d.resourceCountry == null)
                        place = d.resourceCity;
                    else if (d.resourceCity == null)
                        place = d.resourceCountry;
                    $("#locationAddAutocomplete").val(place);

                    document.getElementById("RemoteCount").min = d.remoteCount;
                    document.getElementById("OnsiteCount").min = d.onsiteCount;
                    $('#select_vendorDR').val(vendorStr).trigger('change');
                    $('#select_requestType').val(d.requestType);
                    $('#resourceSource').val(remoteLocationArray).trigger('change');
                    $('#select_resourceType').val(d.resourceType);
                    $('#OnsiteCount').val(d.onsiteCount);
                    $('#select_jobStage').val(d.jobStageId);
                    $('#resource_description').val(d.resourceDescription);
                    $('#RemoteCount').val(d.remoteCount);
                    $('#select_jobRole').val(d.jobRoleId);
                    $('#FTE').val(d.ftePercent);
                    var start = d.startDate;
                    $("#start_date_dr").datepicker("setDate", start);

                    var end = d.endDate;
                    $("#end_date_dr").datepicker("setDate", end);
                    $('#duration').val(d.duration);
                    //fill Table Competence                       
                    $.each(d.ResourceRequestCompetences, function (j, com) {
                        var html = "<tr id='trComp" + com.CompetenceID + "'>";
                        html = html + "<td style='display: none;'>" + com.CompetenceID;//ResourceRequestCompetenceID
                        html = html + "</td>";
                        html = html + "<td>" + com.CompetenceName;
                        html = html + "</td>";
                        html = html + "<td>" + com.Level;
                        html = html + "</td>";
                        html = html + "<td><button class='fa fa-trash-o' title='Delete' style='border:none' onclick='DeleteCompetence(" + com.CompetenceID + "," + com.ResourceRequestCompetenceID + ")'></button>"
                        html = html + "</tr>";
                        $("#tBodyCompetence").append(html);

                    })
                    //fill table certificate
                    $.each(d.ResourceRequestCertificates, function (j, cer) {
                        var html = "<tr id='trCertEdit" + cer.CertificateID + "'>";
                        html = html + "<td style='display: none;'>" + cer.CertificateID;//ResourceRequestCertificateID
                        html = html + "</td>";
                        html = html + "<td>" + cer.CertificateName;
                        html = html + "</td>";
                        html = html + "<td>" + cer.Issuer; //cer.Issuer;
                        html = html + "</td>";
                        html = html + "<td><button class='fa fa-trash-o' style='border:none' onclick='DeleteCertificate(" + cer.CertificateID + "," + cer.ResourceRequestCertificateID + ")'></button>"
                        html = html + "</tr>";
                        $("#tBodyCertificate").append(html);
                    })
                }
            })
        },
        error: function (xhr, status, statusText) {
            alert("Fail to edit resource" + xhr.responseText);
        }
    });
}

function getJobStage() {
    $.isf.ajax({
        url:service_java_URL + "/resourceManagement/getJobStages",
        success: function (data) {

            $.each(data, function (i, d) {
                $('#select_jobStage').append('<option value="' + d.JobStageID + '">' + d.JobStageName + '</option>');
                $('#select_jobStageEdit').append('<option value="' + d.JobStageID + '">' + d.JobStageName + '</option>');
            })

        },
        error: function (xhr, status, statusText) {
            console.log("Fail " + xhr.responseText);
            console.log('An error occurred');
        }
    });
}

function getJobRoles() {

    $.isf.ajax({
        url: service_java_URL + "resourceManagement/getJobRoles",
        success: function (data) {

            $.each(data, function (i, d) {
                $('#select_jobRole').append('<option value="' + d.JobRoleID + '">' + d.JobRoleName + '</option>');
                $('#select_jobRoleEdit').append('<option value="' + d.JobRoleID + '">' + d.JobRoleName + '</option>');
            })

        },
        error: function (xhr, status, statusText) {
            console.log("Fail " + xhr.responseText);
            console.log('An error occurred');
        }
    });
}

function getOnsiteCountries() {
    url =service_CountryCity + "country/all/?key=" + BATTUTA_KEY + "&callback=?";
    $.getJSON(url, function (countries) {
        $.each(countries, function (key, country) {
            $('#select_OnsiteCountry').append('<option value="' + country.code + '">' + country.name + '</option>');
            $('#select_OnsiteCountryEdit').append('<option value="' + country.code + '">' + country.name + '</option>');
        })
    });
}

function getOnSiteStates() {
    $("#select_OnsiteState option").remove();
    $("#select_OnsiteCity option").remove();
    var cod = $("#select_OnsiteCountry").val();
    url = service_CountryCity + "region/" + cod + "/all/?key=" + BATTUTA_KEY + "&callback=?";
    $.getJSON(url, function (cities) {

        $('#select_OnsiteState').append('<option value="0">Select one</option>');
        $.each(cities, function (key, city) {            
            $('#select_OnsiteState').append('<option value="' + city.region + '">' + city.region + '</option>');
        })
    });
}

function getOnSiteStatesEdit() {
    $("#select_OnsiteStateEdit option").remove();
    $("#select_OnsiteCityEdit option").remove();
    var cod = $("#select_OnsiteCountryEdit").val();
    url = service_CountryCity + "region/" + cod + "/all/?key=" + BATTUTA_KEY + "&callback=?";
    $.getJSON(url, function (cities) {

        $('#select_OnsiteStateEdit').append('<option value="0">Select one</option>');
        $.each(cities, function (key, city) {
            $('#select_OnsiteStateEdit').append('<option value="' + city.region + '">' + city.region + '</option>');
        })
    });
}

function getOnSiteCities() {

    var coun = $("#select_OnsiteCountry option:selected").val();
    var st = $("#select_OnsiteState option:selected").val();
    var coordinates = "";
    $("#select_OnsiteCity option").remove();
    url = service_CountryCity + "city/" + coun + "/search/?region=" + st + "&key=" + BATTUTA_KEY + "&callback=?";
    $.getJSON(url, function (cities) {     
        $('#select_OnsiteCity').append('<option value="0">Select one</option>');
        $.each(cities, function (key, city) {
            coordinates = +"" + city.latitude + "," + city.longitude+"" ;            
            $('#select_OnsiteCity').append('<option value="' + coordinates + '">' + city.city + '</option>');
        })
    });
}

function getOnSiteCitiesEdit() {
    $("#select_OnsiteCityEdit option").remove();
    var coun = $("#select_OnsiteCountryEdit").val();
    var st = $("#select_OnsiteStateEdit").val();
    var coordinates = "";
    url = service_CountryCity + "city/" + coun + "/search/?region=" + st + "&key=" + BATTUTA_KEY + "&callback=?";
    $.getJSON(url, function (cities) {
     
        $('#select_OnsiteCityEdit').append('<option value="0">Select one</option>');
        $.each(cities, function (key, city) {
            coordinates = city.latitude + "," + city.longitude;
            $('#select_OnsiteCityEdit').append('<option value="' + coordinates + '">' + city.city + '</option>');
        })
    });
}

function AddOnsiteLocation() {
    var country = $("#select_OnsiteCountry option:selected").text();
    var state = $("#select_OnsiteState option:selected").text();
    var city = $("#select_OnsiteCity option:selected").text();
    var coordenadas = $("#select_OnsiteCity option:selected").val();
    var ID = 0;
    $('#tBodyOnsiteLocation tr').each(function () {
        ID = $(this).attr('id');
    });
    ID = parseInt(ID) + 1;
    var html = "<tr id='" + ID + "'>";
    html = html + "<td>" + country + "</td>";
    html = html + "<td>" + state+ "</td>";
    html = html + "<td>" + city + "</td>";
    html = html + "<td>" + coordenadas + "</td>";
    html = html + "<td><button class='fa fa-trash-o' style='border:none' onclick='DeleteOnsiteLocation(" + ID + ")'></button>";
    html = html + "</tr>";
    $("#tBodyOnsiteLocation").append(html);
}

function DeleteOnsiteLocation(ID) {
    $("#" + ID).remove();
}

function btnAddOnsiteLocationEdit() {
    var country = $("#select_OnsiteCountryEdit option:selected").text();
    var state = $("#select_OnsiteStateEdit option:selected").text();
    var city = $("#select_OnsiteCityEdit option:selected").text();
    var coordenadas = $("#select_OnsiteCityEdit option:selected").val();
    var ID = 0;
    $('#tBodyOnsiteLocationEdit tr').each(function () {
        ID = $(this).attr('id');
    });
    ID = parseInt(ID) + 1;
    var html = "<tr id='" + ID + "'>";
    html = html + "<td>" + country + "</td>";
    html = html + "<td>" + state + "</td>";
    html = html + "<td>" + city + "</td>";
    html = html + "<td>" + coordenadas + "</td>";
    html = html + "<td><button class='fa fa-trash-o' style='border:none' onclick='DeleteOnsiteLocationEdit(" + ID + ")'></button>";
    html = html + "</tr>";
    $("#tBodyOnsiteLocationEdit").append(html);
}

function DeleteOnsiteLocationEdit(ID) {
    $("#" + ID).remove();
}

function getOnsiteLocations() {

    $.isf.ajax({
        url: service_java_URL + "resourceManagement/getOnsiteLocations",
        success: function (data) {

            $.each(data, function (i, d) {
                $('#resourceSource').append('<option value="' + d.OnsiteLocationID + '">' + d.LocationName + '</option>');
                $('#resourceSourceEdit').append('<option value="' + d.OnsiteLocationID + '">' + d.LocationName + '</option>');
            })
        },
        error: function (xhr, status, statusText) {
            console.log("Fail " + xhr.responseText);
            //alert("status " + xhr.status);
            console.log('An error occurred');
        }
    });
}

function getRRIDs(subscopeID)
{
    RRID = "";
    $('#selectRRID').empty().append('<option value="0">Select RRID</option>');
    var serviceUrl = service_java_URL + "demandForecast/getResourceRequestedBySubScope?projectID=" + projectID + "&projectScopeDetailID=" + subscopeID ;
    if (ApiProxy == true) {
        serviceUrl = service_java_URL + "demandForecast/getResourceRequestedBySubScope?projectID=" + projectID + encodeURIComponent("&projectScopeDetailID=" + subscopeID);
    }
    $.isf.ajax({
        url: serviceUrl,
        success: function (data) {
            $.each(data, function (i, d) {
                //RRID = d.ResourceRequestID;
                if (RRID != d.ResourceRequestID) {
                    RRID = d.ResourceRequestID;
                    $('#selectRRID').append('<option value="' + RRID + '">' + RRID + '</option>');
                }
                
            });
        },
        error: function (xhr, status, statusText) {
            console.log("Fail to get resources ");

            console.log('An error occurred');
        }
    });

}

$(function () {
    $('#search').keyup(function (e) {
        if ($(this).val().length >= 3) {

            //////
            $('#select_Competence').empty()
            var url1 = service_java_URL + 'resourceManagement/getFilteredCompetences?competenceString=' + $('#search').val();
            //alert(url);

            $.isf.ajax({
                url: url1,
                async: false,
                success: function (data) {
                    $.each(data, function (i, d) {                        
                        $('#select_Competence').append('<option value="' + d.CompetenceID + '">' + d.CompetenceName + '</option>');
                    })
                },
                error: function (xhr, status, statusText) {
                    console.log("Fail to get competences");
                    console.log('An error occurred');
                }
            });		
        }
    })
}); 

$(function () {
    $('#searchEdit').keyup(function (e) {
        if ($(this).val().length >= 3) {

            //////
            $('#select_CompetenceEdit').empty()
            var url1 = service_java_URL + 'resourceManagement/getFilteredCompetences?competenceString=' + $('#searchEdit').val();
            //alert(url);

            $.isf.ajax({
                url: url1,
                async: false,
                success: function (data) {
                    $.each(data, function (i, d) {
                        $('#select_CompetenceEdit').append('<option value="' + d.CompetenceID + '">' + d.CompetenceName + '</option>');
                    })
                },
                error: function (xhr, status, statusText) {
                    console.log("Fail to get competences");
                    console.log('An error occurred');
                }
            });
        }
    })
});

function getAllIssues() {

    //var Aux = [];
    pwIsf.addLayer({ text: "Please wait ..." });
    
    $.isf.ajax({
        url: service_java_URL + "resourceManagement/getUniqueIssuer",
        async: false,
        success: function (data) {
            pwIsf.removeLayer();
            $.each(data, function (i, d) {
                $('#select_Issue').append('<option value="' + d.Issuer + '">' + d.Issuer + '</option>');
                $('#select_IssueEdit').append('<option value="' + d.Issuer + '">' + d.Issuer + '</option>');
            })
        },
        error: function (xhr, status, statusText) {
            pwIsf.removeLayer();
            console.log("Fail to get Issuers");
            console.log('An error occurred');
        }
    });
}

function IssueChange() {
    var issuer = $("#select_Issue option:selected").text();
    $('#select_Certificate option').remove();
    //http://10.184.56.132/ISF_API_UAT/Service1.svc/GetAllCertifications?Issuer=
    $.isf.ajax({
        url: service_java_URL + "resourceManagement/getAllCertifications?Issuer=" + issuer,
        success: function (data) {
            $.each(data, function (i, d) {
                $('#select_Certificate').append('<option value="' + d.CertificateID + '">' + d.CertificateName + '</option>');

            })
        },
        error: function (xhr, status, statusText) {
            console.log("Fail " + xhr.responseText);
            //alert("status " + xhr.status);
            console.log('An error occurred');
        }
    });
}

function IssueChangeEdit() {
    var issuer = $("#select_IssueEdit option:selected").text();
    $('#select_CertificateEdit option').remove();
    $.isf.ajax({
        url: service_java_URL + "resourceManagement/getAllCertifications?Issuer=" + issuer,
        //This API will return RRCID as well and you will use it in AddComp method
        success: function (data) {
            $.each(data, function (i, d) {
                $('#select_CertificateEdit').append('<option value="' + d.CertificateID + '">' + d.CertificateName + '</option>');
            })
        },
        error: function (xhr, status, statusText) {
            console.log("Fail " + xhr.responseText);
            //alert("status " + xhr.status);
            console.log('An error occurred');
        }
    });
}

function AddCompetence() {

    $("#select_Competence option:selected").each(function (i, sel) {
        var level = $("#select_Level option:selected").text();
        var html = "<tr id='trComp" + sel.value[i] + "'>";
        html = html + "<td style='display: none;'>" + sel.value;
        html = html + "</td>";
        html = html + "<td>" + sel.text;
        html = html + "</td>";
        html = html + "<td>" + level;
        html = html + "</td>";
        html = html + "<td><button class='fa fa-trash-o' title='Delete' style='border:none' onclick='DeleteCompetence(" + sel.value[i] + ")'></button>"
        html = html + "</tr>";
        $("#tBodyCompetence").append(html);
    })
    $("#select_Competence").select("val", "");

}

function AddCompetenceEdit()
{
    
    $("#select_CompetenceEdit option:selected").each(function (i, sel) {
        var html = "<tr id='trCompEdit" + sel.value[i] + "'>";
        html = html + "<td style='display: none;'>" + sel.value;
        html = html + "</td>";
        html = html + "<td>" + sel.text;
        html = html + "</td>";
        html = html + "<td>" + $("#select_LevelEdit option:selected").text();
        html = html + "</td>";
        html = html + "<td><button class='fa fa-trash-o' style='border:none' onclick='DeleteCompetenceEdit(" + sel.value[i] + ")'></button>"
        html = html + "</tr>";
        $("#tBodyCompetenceEdit").append(html);
        var service_data = '{"competenceLevel":"' + $("#select_LevelEdit option:selected").text() + '","createdBy":"' + signumGlobal + '","competenceId":"' + sel.value + '","resourceRequestId":"' + ResourceID + '"}';
        var ulrService = service_java_URL + "activityMaster/addCompetenceByRRID"
        $.isf.ajax({
            url: ulrService,
            context: this,
            crossdomain: true,
            processData: true,
            contentType: 'application/json',
            type: 'POST',
            data: service_data,
            xhrFields: {
                withCredentials: false
            },
            error: function (xhr, status, statusText) {
                console.log('An error occurred');
                alert('Fail to save competence');
            }
        });
    })
    $("#select_CompetenceEdit").select("val", "");
    $("#searchEdit").val("");
    //$("#select_LevelEdit").select("val", "");

}

function DeleteCompetence(ID) {
    $("#trComp" + ID).remove();
}

function DeleteCompetenceEdit(ID, ResourceRequestCompetenceID) {
    
    var service_data = '{"lastModifiedBy": "' + signumGlobal + '","active":"false","resourceRequestCompetenceId":"' + ResourceRequestCompetenceID + '"}';

    var ulrService = service_java_URL + "activityMaster/updateResourceRequestCompetence"
    $.isf.ajax({
        url: ulrService,
        context: this,
        crossdomain: true,
        processData: true,
        contentType: 'application/json',
        type: 'POST',
        data: service_data,
        success: function(data){
            
            $("#trCompEdit" + ID).remove();
        },
        xhrFields: {
            withCredentials: false
        },
        error: function (xhr, status, statusText) {
            console.log('An error occurred');
            alert('Fail to save competence');
        }
    });

   
}

function AddCertificate() {

    $("#select_Certificate option:selected").each(function (i, sel) {
        var html = "<tr id='trCert" + sel.value[i] + "'>";
        html = html + "<td style='display: none;'>" + sel.value;
        html = html + "</td>";
        html = html + "<td>" + sel.text;
        html = html + "</td>";
        html = html + "<td>" + $("#select_Issue option:selected").text();
        html = html + "</td>";
        html = html + "<td><button class='fa fa-trash-o' style='border:none' onclick='DeleteCertificate(" + sel.value[i] + ")'></button>"
        html = html + "</tr>";
        $("#tBodyCertificate").append(html);
    })
    $("#select_Certificate").select("val", "");

}

function AddCertificateEdit()
{
    $("#select_CertificateEdit option:selected").each(function (i, sel) {
        var html = "<tr id='trCertEdit" + sel.value[i] + "'>";
        html = html + "<td style='display: none;'>" + sel.value;
        html = html + "</td>";
        html = html + "<td>" + sel.text;
        html = html + "</td>";
        html = html + "<td>" + $("#select_IssueEdit option:selected").text();
        html = html + "</td>";
        //here then you will get the RRCID so put
        html = html + "<td><button class='fa fa-trash-o' style='border:none' onclick='DeleteCertificateEdit(" + sel.value[i] +','+sel.value[i]+ ")'></button>"
        html = html + "</tr>";
        $("#tBodyCertificateEdit").append(html);
        var service_data = '{"createdBy":"' + signumGlobal + '","certificateId":"' + sel.value + '","resourceRequestId":"' + ResourceID + '"}';
        var ulrService = service_java_URL + "activityMaster/addCertificationByRRID"
        $.isf.ajax({
            url: ulrService,
            context: this,
            crossdomain: true,
            processData: true,
            contentType: 'application/json',
            type: 'POST',
            data: service_data,
            xhrFields: {
                withCredentials: false
            },
            success: function (data) {
                // alert(data);
            },
            error: function (xhr, status, statusText) {
                console.log('An error occurred');
                alert('Fail to add certificates');
            }
        })
    })
    $("#select_CertificateEdit").select("val", "");
    // $("#select_IssueEdit").select("val", "");

}

function DeleteCertificate(ID) {
    $("#trCert" + ID).remove();
}

function DeleteCertificateEdit(ID, ResourceRequestCertificateID) {
    
    var service_data = '{"lastModifiedBy": "' + signumGlobal + '","active":"false","resourceRequestCertificationId":"' + ResourceRequestCertificateID + '"}';

    var ulrService = service_java_URL + "activityMaster/updateResourceRequestCertification"
    $.isf.ajax({
        url: ulrService,
        context: this,
        crossdomain: true,
        processData: true,
        contentType: 'application/json',
        type: 'POST',
        data: service_data,
        success: function (data) {
            $("#trCertEdit" + ID).remove();            
        },
        xhrFields: {
            withCredentials: false
        },
        error: function (xhr, status, statusText) {
            console.log('An error occurred');
            alert('Fail to save competence');
        }
    });
}

function validateAddResource() {
    var OK = true;
    $('#locationAddAutocomplete-Required').text("");
    if ($("#locationAddAutocomplete").val() == "" || $("#locationAddAutocomplete").val() == "null" ) {
        $('#locationAddAutocomplete-Required').text("Resource Location is required");
        OK = false;
    }
    else if($("#locationAddAutocomplete").val().lastIndexOf(",") == -1){
        $('#locationAddAutocomplete-Required').text("Please enter the resource location in proper format- CityName,CountryName");
        OK = false;
    }
    else {
        var resourceLocation = $("#locationAddAutocomplete").val();
        var lastIndex = resourceLocation.lastIndexOf(",");
        locCity = resourceLocation.substring(0, lastIndex);
        locCountry = resourceLocation.substring(lastIndex + 1);
        if (locCity == "null" || locCity == "" || locCountry == "null" || locCountry == "") {
            $('#locationAddAutocomplete-Required').text("Please enter the valid resource location");
            OK = false;
        }

    }

    $('#select_requestType-Required').text("");
    if ($("#select_requestType option:selected").val() == "0") {
        $('#select_requestType-Required').text("Level is required");
        OK = false;
    }
    $('#select_resourceType-Required').text("");
    if ($("#select_resourceType option:selected").val() == "0") {
        $('#select_resourceType-Required').text("Level is required");
        OK = false;
    }
    $('#select_jobStage-Required').text("");
    if ($("#select_jobStage option:selected").val() == "0") {
        $('#select_jobStage-Required').text("Level is required");
        OK = false;
    }
    $('#select_jobRole-Required').text("");
    if ($("#select_jobRole option:selected").val() == "0") {
        $('#select_jobRole-Required').text("Level is required");
        OK = false;
    }
    $('#resourceSource-Required').text("");
    if ($("#resourceSource option:selected").text() == "") {
        $('#resourceSource-Required').text("Resource source is required");
        OK = false;
    }
    $('#OnsiteCount-Required').text("");
    if ($("#OnsiteCount").val() == "") {
        $("#OnsiteCount").val('0');
        //$('#OnsiteCount-Required').text("On site count work is required");
        //OK = false;
    }
    else if (!$("#OnsiteCount").val().match(OnlyNumber)) {
            $('#OnsiteCount-Required').text("On site count is a number");
            OK = false;
        }
    $('#RemoteCount-Required').text("");
    if ($("#RemoteCount").val() == "") {
        $('#RemoteCount-Required').text("Remote count is required");
        OK = false;
    }
    else
        if (!$("#RemoteCount").val().match(OnlyNumber)) {
            $('#RemoteCount-Required').text("Remote count is a number");
            OK = false;
        }
        else if ($("#RemoteCount").val() == 0 && $("#OnsiteCount").val() == 0) {
            $('#RemoteCount-Required').text("Both Counts Cannot be 0");
            $('#OnsiteCount-Required').text("Both Counts Cannot be 0");
            OK = false;
        }

    $('#FTE-Required').text("");
    var NumberDecimals = /^[0-9]+([.][0-9]+)?$/;
    if (!$("#FTE").val().match(NumberDecimals)) {
        $('#FTE-Required').text("FTE is only number/decimals");
        OK = false;
    }
    else {
        if ( parseInt($('#FTE').val()) > "100")
            $('#FTE-Required').text("Maximum value 100");
    }

    $('#start_date-Required').text("");
    if ($("#start_date_dr").val() == null || $("#start_date_dr").val() == "") {
        $('#start_date-Required').text("Start date is required");
        OK = false;
    }
    $('#end_date-Required').text("");
    if ($("#end_date_dr").val() == null || $("#end_date_dr").val() == "") {
        $('#end_date-Required').text("End date is required");
        OK = false;
    }
    $('#start_date-Required').text("");
    var startDate = new Date($('#start_date_dr').val());
    if (defaultDate > startDate) {
        $('#start_date-Required').text("Not to preceed " + start);
        OK = false;
    }
    else {
        var startDate = new Date($('#start_date_dr').val());
        var endDate = new Date($('#end_date_dr').val());
        if (startDate > endDate) {
            $('#end_date-Required').text(" End date must be after Start date");
            OK = false;
        }
    }
    
    //$('#select_Competence-Required').text("");
    //if ($("#tBodyCompetence tr").length < 1) {
    //    $('#select_Competence-Required').text("Competence is required");
    //    OK = false;
    //}
    //$('#select_Certificate-Required').text("");
    //if ($("#tBodyCertificate tr").length < 1) {
    //    $('#select_Certificate-Required').text("Certificate is required");
    //    OK = false;
    //}
    if (OK) {
        SubmitAddResource();
    }
}

function SubmitAddResource() {
    var RequestType = $("#select_requestType").val();
    var ResourceType = $("#select_resourceType").val();
    var ProjScopeDetailID = SubScopeID;
    var JobRoleID = $("#select_jobRole").val();
    var JobStageID = $("#select_jobStage").val();
    var vendor = $("#select_vendorDR").val();
	if (JobStageID == 0) {
        JobStageID = 1;
    }
    var ResourceDescription = $("#resource_description").val();
    var LocationId = 0;// $("#resourceSource").val();
    var RemoteCount = $("#RemoteCount").val();
    var OnsiteCount = $("#OnsiteCount").val();
    var StartDate = $("#start_date_dr").val();
    var EndDate = $("#end_date_dr").val();
    var Duration = $("#duration").val();
    var FTEPercent = $("#FTE").val();
    var Hours = $("#HourDays").val();
    var resourceLocation = $("#locationAddAutocomplete").val();
    var signum = signumGlobal;

    var locLat = "";
    var locLng = "";
    var locTimezone = "";
    var locCity = "";
    var locCountry = "";
    var vendorArray = $("#select_vendorDR").val();
    var geocoder = "";
    var mapCheck = true;
    try {
        geocoder = new google.maps.Geocoder();
    }
    catch (e) {
        mapCheck = false;
    }

    if (mapCheck) {
        geocoder.geocode({ 'address': resourceLocation }, function (results, status) {
            if (status == google.maps.GeocoderStatus.OK) {
                locLat = results[0].geometry.location.lat();
                locLng = results[0].geometry.location.lng();
                var lastIndex = resourceLocation.lastIndexOf(",");
                locCity = resourceLocation.substring(0, lastIndex);
                locCountry = resourceLocation.substring(lastIndex + 1);
                $.isf.ajax({
                    url: "https://maps.googleapis.com/maps/api/timezone/json?location=" + locLat + "," + locLng + "&timestamp=" + (Math.round((new Date().getTime()) / 1000)).toString(),
                    async: false
                }).done(function (response) {
                    if (response.timeZoneId != null) {
                        locTimezone = response.timeZoneId;
                    }
                });
                StartDate = StartDate.toString().replace("/", "-");
                StartDate = StartDate.toString().replace("/", "-");
                EndDate = EndDate.toString().replace("/", "-");
                EndDate = EndDate.toString().replace("/", "-");

                var competences = buildCompetencesJSON();
                var certificates = buildCertificatesJSON();
                

                var OnsiteLocation = '';
                var RemoteLocation = $("#resourceSource option:selected").map(function () { return this.text }).get().join(",");
               
                var addResourceObj = {};
                addResourceObj.resourceType = ResourceType;
                addResourceObj.resourceDescription = ResourceDescription;
                addResourceObj.createdBy = signum;
                addResourceObj.projScopeDetailId = ProjScopeDetailID;
                addResourceObj.projectId = projectID;
                addResourceObj.vendor = vendorArray;
                addResourceObj.jobRoleId = JobRoleID;
                addResourceObj.jobStageId = JobStageID;
                addResourceObj.remoteCount = RemoteCount;
                addResourceObj.onsiteCount = OnsiteCount;
                addResourceObj.startDate = StartDate;
                addResourceObj.endDate = EndDate;
                addResourceObj.duration = Duration;
                addResourceObj.ftePercent = FTEPercent;
                addResourceObj.hours = Hours;
                addResourceObj.competences = competences;
                addResourceObj.certificates = certificates;
                addResourceObj.remoteLocation = RemoteLocation;
                addResourceObj.onsiteLocation = OnsiteLocation;
                addResourceObj.resourceCity = locCity;
                addResourceObj.resourceCountry = locCountry;
                addResourceObj.resourceLat = locLat;
                addResourceObj.resourceLng = locLng;
                addResourceObj.resourceTimeZone = locTimezone;
                var JsonObj = JSON.stringify(addResourceObj);
                // console.log(service_data);
                pwIsf.addLayer({ text: "Please wait ..." });
                
                $.isf.ajax({
                  
                    url: service_java_URL + "demandManagement/saveDemandResourceRequest",
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
                         AjaxSucceeded(data);
                     },

                      error: function (xhr, status, statusText) {
                          AjaxFailed(xhr)
                     }
                });
                function AjaxSucceeded(data) {
                    pwIsf.removeLayer();
                    pwIsf.alert({msg:"Resource Saved Successfully",type:"success"})
                    //alert("The resource has been saved.");
                    sendEmail(data);
                    $("#AddR").modal('hide');
                    $('.modal-backdrop').remove();
                    loadAllocatedResourcesView(projectID);
                }
                function AjaxFailed(result) {
                    pwIsf.removeLayer();
                    alert("Failed to save Demand Resource");
                    alert(result.status + ' ' + result.statusText);
                }

                function sendEmail(data) {
                    var service_data = '{"emailTemplateName":"CREATE_DR","currentUser":"' + signum + '","ResourceType":"' + ResourceType + '","ResourceDescription":"' + ResourceDescription + '","CreatedBy":"' + signum + '","ProjScopeDetailID":"' + ProjScopeDetailID + '",' +
                        '"ProjectID":"' + projectID + '","JobRoleID":"' + JobRoleID + '","JobStageID":"' + JobStageID + '","RemoteCount":"' + RemoteCount + '","OnsiteCount":"' + OnsiteCount + '"' +
                        ',"StartDate":"' + StartDate + '","EndDate":"' + EndDate + '","Duration":"' + Duration + '","FTEPercent":"' + FTEPercent + '","Hours":"' + Hours + '",' +
                        '"Competences":' + competences + "," +
                        '"Certificates":' + certificates + ',"RemoteLocation":"' + RemoteLocation + '","OnsiteLocation":"' + OnsiteLocation + '"' +
                        ',"ResourceCity":"' + locCity + '","ResourceCountry":"' + locCountry + '","ResourceLat":"' + locLat + '","ResourceLng":"' + locLng + '","ResourceTimezone":"' + locTimezone + '","RRID":"' + data.responseData + '"}';

                    var ulrService = service_java_URL + "appUtil/sendMail/"
                    $.isf.ajax({
                        url: ulrService,
                        //context: this,
                        crossdomain: true,
                        //processData: true,
                        contentType: 'application/json',
                        type: 'POST',
                        data: service_data,
                        success: function (data) {
                            // alert("Mail Sent Successfully");
                        },
                        xhrFields: {
                            withCredentials: false
                        },
                        error: function (xhr, status, statusText) {
                            pwIsf.alert({ msg: "Error sending Mail", type: "error" });
                        }
                    });
                }
            }
            else {
                alert("Please enter the valid  resource location");
            }
        });
    }
    else {
        locLat = 0.0;
        locLng = 0.0;
        locTimezone = " ";
        var lastIndex = resourceLocation.lastIndexOf(",");
        locCity = resourceLocation.substring(0, lastIndex);
        locCountry = resourceLocation.substring(lastIndex + 1);

        StartDate = StartDate.toString().replace("/", "-");
        StartDate = StartDate.toString().replace("/", "-");
        EndDate = EndDate.toString().replace("/", "-");
        EndDate = EndDate.toString().replace("/", "-");

        var competences = buildCompetencesJSON();
        var certificates = buildCertificatesJSON();
        
        var OnsiteLocation = '';
        var RemoteLocation = $("#resourceSource option:selected").map(function () { return this.text }).get().join(",");
        
        var addResourceObj = {};
        addResourceObj.resourceType = ResourceType;
        addResourceObj.resourceDescription = ResourceDescription;
        addResourceObj.createdBy = signum;
        addResourceObj.projScopeDetailId = ProjScopeDetailID;
        addResourceObj.projectId = projectID;
        addResourceObj.vendor = vendorArray;
        addResourceObj.jobRoleId = JobRoleID;
        addResourceObj.jobStageId = JobStageID;
        addResourceObj.remoteCount = RemoteCount;
        addResourceObj.onsiteCount = OnsiteCount;
        addResourceObj.startDate = StartDate;
        addResourceObj.endDate = EndDate;
        addResourceObj.duration = Duration;
        addResourceObj.ftePercent = FTEPercent;
        addResourceObj.hours = Hours;
        addResourceObj.competences = competences;
        addResourceObj.certificates = certificates;
        addResourceObj.remoteLocation = RemoteLocation;
        addResourceObj.onsiteLocation = OnsiteLocation;
        addResourceObj.resourceCity = locCity;
        addResourceObj.resourceCountry = locCountry;
        addResourceObj.resourceLat = locLat;
        addResourceObj.resourceLng = locLng;
        addResourceObj.resourceTimeZone = locTimezone;
        var JsonObj = JSON.stringify(addResourceObj);
        $.isf.ajax({
            url: service_java_URL + "demandManagement/saveDemandResourceRequest",
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
                AjaxSucceeded(data);
            },
            error: function (xhr, status, statusText) {
                AjaxFailed(xhr)
            }
        });
        function AjaxSucceeded(data) {
            pwIsf.alert({ msg: "Resource Saved Successfully", type: "success" });
            sendEmail();
            $("#AddR").modal('hide');
            $('.modal-backdrop').remove();
            loadAllocatedResourcesView(projectID);
        }
        function AjaxFailed(result) {
            pwIsf.alert({ msg: "Failed to save Demand Resource", type: "error" });
            alert(result.status + ' ' + result.statusText);
        }

        function sendEmail() {
            var service_data = '{"emailTemplateName":"CREATE_DR","currentUser":"' + signum + '","ResourceType":"' + ResourceType + '","ResourceDescription":"' + ResourceDescription + '","CreatedBy":"' + signum + '","ProjScopeDetailID":"' + ProjScopeDetailID + '",' +
                '"ProjectID":"' + projectID + '","JobRoleID":"' + JobRoleID + '","JobStageID":"' + JobStageID + '","LocationId":"' + LocationId + '","RemoteCount":"' + RemoteCount + '","OnsiteCount":"' + OnsiteCount + '"' +
                ',"StartDate":"' + StartDate + '","EndDate":"' + EndDate + '","Duration":"' + Duration + '","FTEPercent":"' + FTEPercent + '","Hours":"' + Hours + '",' +
                '"Competences":' + competences + "," +
                '"Certificates":' + certificates + ',"RemoteLocation":"' + RemoteLocation + '","OnsiteLocation":"' + OnsiteLocation + '"' +
                ',"ResourceCity":"' + locCity + '","ResourceCountry":"' + locCountry + '","ResourceLat":"' + locLat + '","ResourceLng":"' + locLng + '","ResourceTimezone":"' + locTimezone + '"}';

            var ulrService = service_java_URL + "appUtil/sendMail/"
            $.isf.ajax({
                url: ulrService,
                //context: this,
                crossdomain: true,
                //processData: true,
                contentType: 'application/json',
                type: 'POST',
                data: service_data,
                success: function (data) {
                    // alert("Mail Sent Successfully");
                },
                xhrFields: {
                    withCredentials: false
                },
                error: function (xhr, status, statusText) {
                    console.log('An error occurred');
                    alert('Fail to save competence');
                }
            });
        }
    }
}

function buildCompetencesJSON() {
    jsonObj = [];
    var table = $("#table_competence tbody");

    table.find('tr').each(function (i) {
        var $tds = $(this).find('td');

        var competenceId = $tds.eq(0).text();
        var competenceName = $tds.eq(1).text();
        var competenceLevel = $tds.eq(2).text();

        record = {};
        record["CompetenceID"] = competenceId;
        record["CompetenceLevel"] = competenceLevel;

        jsonObj.push(record);
    });  

    var jsonString = JSON.stringify(jsonObj);
    return jsonString;
}

function buildCertificatesJSON() {
    jsonObj = [];
    var table = $("#table_certificate tbody");

    table.find('tr').each(function (i) {
        var $tds = $(this).find('td');

        var certificateId = $tds.eq(0).text();
        var certificateName = $tds.eq(1).text();
        var certificateIssuer = $tds.eq(2).text();

        record = {};
        record["CertId"] = certificateId;

        jsonObj.push(record);
    });

    var jsonString = JSON.stringify(jsonObj);
    return jsonString;
}

function validateEditResource() {
    var OK = true;

    $('#locationEditAutocomplete-Required').text("");
    if ($("#locationEditAutocomplete").val() == "") {
        $('#locationEditAutocomplete-Required').text("Resource Location is required");
        OK = false;
    } else if ($("#locationEditAutocomplete").val().lastIndexOf(",") == -1) {
        $('#locationEditAutocomplete-Required').text("Please enter the resource location in proper format- CityName, CountryName");
        OK = false;
    }else {
        var resourceLocation = $("#locationEditAutocomplete").val();
        var lastIndex = resourceLocation.lastIndexOf(",");
        locCity = resourceLocation.substring(0, lastIndex);
        locCountry = resourceLocation.substring(lastIndex + 1);
        if (locCity == "null" || locCity == "" || locCountry == "null" || locCountry == "") {
            $('#locationEditAutocomplete-Required').text("Please enter the valid resource location");
            OK = false;
        }
    }
    
    $('#select_requestTypeEdit-Required').text("");
    if ($("#select_requestTypeEdit option:selected").val() == "0") {
        $('#select_requestTypeEdit-Required').text("Level is required");
        OK = false;
    }

    $('#select_resourceTypeEdit-Required').text("");
    if ($("#select_resourceTypeEdit option:selected").val() == "0") {
        $('#select_resourceTypeEdit-Required').text("Level is required");
        OK = false;
    }
    $('#select_jobStageEdit-Required').text("");
    if ($("#select_jobStageEdit option:selected").val() == "0") {
        $('#select_jobStageEdit-Required').text("Level is required");
        OK = false;
    }

    $('#select_jobRoleEdit-Required').text("");
    if ($("#select_jobRoleEdit option:selected").val() == "0") {
        $('#select_jobRoleEdit-Required').text("Level is required");
        OK = false;
    }
    $('#resourceSourceEdit-Required').text("");
    if ($("#resourceSourceEdit option:selected").text() == "") {
        $('#resourceSourceEdit-Required').text("Resource source is required");
        OK = false;
    }
    $('#OnsiteCountEdit-Required').text("");
    if ($("#OnsiteCountEdit").val() == "") {
        $("#OnsiteCountEdit").val('0');
    }
    else
        if (!$("#OnsiteCountEdit").val().match(OnlyNumber)) {
            $('#OnsiteCountEdit-Required').text("On site count is a number");
            OK = false;
        }

    $('#RemoteCountEdit-Required').text("");
    if ($("#RemoteCountEdit").val() == "") {
        $('#RemoteCountEdit-Required').text("Remote count work is required");
        OK = false;
    }
    else
        if (!$("#RemoteCountEdit").val().match(OnlyNumber)) {
            $('#RemoteCountEdit-Required').text("Remote count is a number");
            OK = false;
        }
    $('#FTEEdit-Required').text("");
    var NumberDecimals = /^[0-9]+([.][0-9]+)?$/;
    if (!$("#FTEEdit").val().match(NumberDecimals)) {
        $('#FTEEdit-Required').text("FTE is only number/decimals");
        OK = false;
    }
    else {
        if (parseInt($('#FTEEdit').val()) > "100")
            $('#FTEEdit-Required').text("Maximum value 100");
    }
    $('#start_dateEdit-Required').text("");
    if ($("#start_dateEdit").val() == null || $("#start_dateEdit").val() == "") {
        $('#start_dateEdit-Required').text("Start date is required");
        OK = false;
    }
    $('#end_dateEdit-Required').text("");
    if ($("#end_dateEdit").val() == null || $("#end_dateEdit").val() == "") {
        $('#end_dateEdit-Required').text("End date is required");
        OK = false;
    }
    $('#start_dateEdit-Required').text("");
    var startDate = new Date($('#start_dateEdit').val());
    if (defaultDate > startDate) {
        $('#start_dateEdit-Required').text("Not to preceed " + start);
        OK = false;
    }
    else {
        var startDate = new Date($('#start_dateEdit').val());
        var endDate = new Date($('#end_dateEdit').val());
        if (startDate > endDate) {
            $('#end_dateEdit-Required').text(" End date must be after Start date");
            OK = false;
        }
    }
    if (OK) 
        SubmitEditResource();
    
}

function SubmitEditResource() {
    var RequestType = $("#select_requestTypeEdit").val();
    var ResourceType = $("#select_resourceTypeEdit").val();
    var ResourceRequestID = ResourceID;
    var ProjScopeDetailID = SubScopeID;
    var JobRoleID = $("#select_jobRoleEdit").val();
    var JobStageID = $("#select_jobStageEdit").val();
    if (JobStageID == "0") {
        JobStageID = "1";
    }
    var ResourceDescriptionEdit = $("#resource_description-edit").val();
    var LocationId = $("#resourceSourceEdit").val();
    var RemoteCount = $("#RemoteCountEdit").val();
    var OnsiteCount = $("#OnsiteCountEdit").val();
    var ResourceRequestWorkEffortID = ResourceID;
    var StartDate = $("#start_dateEdit").val();
    var EndDate = $("#end_dateEdit").val();
    var Duration = $("#durationEdit").val();
    var FTEPercent = $("#FTEEdit").val();
    var Hours = $("#HourDaysEdit").val();
    var signum = signumGlobal;
    var resourceLocation = $("#locationEditAutocomplete").val();
    var vendorArray = $("#select_vendorEditDR").val();
    
    var locLat = "";
    var locLng = "";
    var locTimezone = "";
    var locCity = "";
    var locCountry = "";
    var geocoder = "";
    var mapCheck = true;
    try {
        geocoder = new google.maps.Geocoder();
    }
    catch (e) {
        mapCheck = false;
    }

    if (mapCheck) {
        geocoder.geocode({ 'address': resourceLocation }, function (results, status) {
            if (status == google.maps.GeocoderStatus.OK) {
                locLat = results[0].geometry.location.lat();
                locLng = results[0].geometry.location.lng();
                var lastIndex = resourceLocation.lastIndexOf(",");
                locCity = resourceLocation.substring(0, lastIndex);
                locCountry = resourceLocation.substring(lastIndex + 1);
                $.isf.ajax({
                    url: "https://maps.googleapis.com/maps/api/timezone/json?location=" + locLat + "," + locLng + "&timestamp=" + (Math.round((new Date().getTime()) / 1000)).toString(),
                    async: false
                }).done(function (response) {
                    if (response.timeZoneId != null) {
                        locTimezone = response.timeZoneId;
                    }
                });

                StartDate = StartDate.toString().replace("/", "-");
                StartDate = StartDate.toString().replace("/", "-");
                EndDate = EndDate.toString().replace("/", "-");
                EndDate = EndDate.toString().replace("/", "-");
                var competences = buildCompetencesEditJSON();
                var certificates = buildCertificatesEditJSON();
                var RemoteLocation = $("#resourceSourceEdit option:selected").map(function () { return this.text }).get().join(",");
                var OnsiteLocation = "";
                var service_data = {
                    "ResourceType": ResourceType,
                    "ResourceDescription": ResourceDescriptionEdit,
                    "LastModifiedBy": signum,
                    "ResourceRequestID": ResourceRequestID,
                    "ProjectID": projectID,
                    "JobRoleID": JobRoleID,
                    "JobStageID": JobStageID,
                    "LocationId": 0,
                    "RemoteCount": RemoteCount,
                    "OnsiteCount": OnsiteCount,
                    "ResourceRequestWorkEffortID": ResourceRequestWorkEffortID,
                    "StartDate": StartDate,
                    "EndDate": EndDate,
                    "Duration": Duration,
                    "FTEPercent": FTEPercent,
                    "Hours": Hours,
                    "RemoteLocation": RemoteLocation,
                    "OnsiteLocation": OnsiteLocation,
                    "ResourceCity": locCity,
                    "ResourceCountry": locCountry,
                    "ResourceLat": locLat,
                    "ResourceLng": locLng,
                    "ResourceTimezone": locTimezone
                }

                var editResourceObj = {};
                editResourceObj.resourceType = ResourceType;
                editResourceObj.resourceDescription = ResourceDescriptionEdit;
                editResourceObj.createdBy = signum;
                editResourceObj.projScopeDetailId = ProjScopeDetailID;
                editResourceObj.resourceRequestId = ResourceRequestID;
                editResourceObj.resourceRequestWorkEffortId = ResourceRequestWorkEffortID;
                editResourceObj.projectId = projectID;
                editResourceObj.vendor = vendorArray;
                editResourceObj.jobRoleId = JobRoleID;
                editResourceObj.jobStageId = JobStageID;
                editResourceObj.remoteCount = RemoteCount;
                editResourceObj.onsiteCount = OnsiteCount;
                editResourceObj.startDate = StartDate;
                editResourceObj.endDate = EndDate;
                editResourceObj.duration = Duration;
                editResourceObj.ftePercent = FTEPercent;
                editResourceObj.hours = Hours;
                editResourceObj.competences = competences;
                editResourceObj.certificates = certificates;
                editResourceObj.remoteLocation = RemoteLocation;
                editResourceObj.onsiteLocation = OnsiteLocation;
                editResourceObj.resourceCity = locCity;
                editResourceObj.resourceCountry = locCountry;
                editResourceObj.resourceLat = locLat;
                editResourceObj.resourceLng = locLng;
                editResourceObj.resourceTimeZone = locTimezone;
                var JsonObj = JSON.stringify(editResourceObj);
              
                // alert(service_data);
                var param = JSON.stringify(service_data);
              
                $.isf.ajax({
                    url: service_java_URL + "demandManagement/updateDemandResourceRequest",
                    //url: service_DotNET_URL + "ModifyDemandRequest",
                    context: this,
                    crossdomain: true,
                    processData: true,
                    contentType: 'application/json',
                    type: 'POST',
                    data:JsonObj,
                    //data: param,
                    xhrFields: {
                        withCredentials: false
                    },
                    success: AjaxSucceeded,
                    error: AjaxFailed
                });

                function AjaxSucceeded(data) {
                    pwIsf.alert({ msg: "Resource Modified Successfully", type: "success" });
                    //alert("The resource has been modified.");
                    $("#EditR").modal('hide');
                    $('.modal-backdrop').remove();
                    ModalAllOpen(ProjScopeDetailID);
                }
                function AjaxFailed(result) {
                    pwIsf.alert({ msg: "Failed to save Demand Resource", type: "error" });
                }
            } else {
                alert("Please enter the valid resource location");
            }
        });
    } else {
        locLat = 0.0;
        locLng = 0.0;
        locTimezone = " ";
        var lastIndex = resourceLocation.lastIndexOf(",");
        locCity = resourceLocation.substring(0, lastIndex);
        locCountry = resourceLocation.substring(lastIndex + 1);
       
        StartDate = StartDate.toString().replace("/", "-");
        StartDate = StartDate.toString().replace("/", "-");
        EndDate = EndDate.toString().replace("/", "-");
        EndDate = EndDate.toString().replace("/", "-");
        var competences = buildCompetencesEditJSON();
        var certificates = buildCertificatesEditJSON();
        var RemoteLocation = $("#resourceSourceEdit option:selected").map(function () { return this.text }).get().join(",");
        var OnsiteLocation = "";
      
        var editResourceObj = {};
        editResourceObj.resourceType = ResourceType;
        editResourceObj.resourceDescription = ResourceDescriptionEdit;
        editResourceObj.createdBy = signum;
        editResourceObj.projScopeDetailId = ProjScopeDetailID;
        editResourceObj.resourceRequestId = ResourceRequestID;
        editResourceObj.resourceRequestWorkEffortId = ResourceRequestWorkEffortID;
        editResourceObj.projectId = projectID;
        editResourceObj.vendor = vendorArray;
        editResourceObj.jobRoleId = JobRoleID;
        editResourceObj.jobStageId = JobStageID;
        editResourceObj.remoteCount = RemoteCount;
        editResourceObj.onsiteCount = OnsiteCount;
        editResourceObj.startDate = StartDate;
        editResourceObj.endDate = EndDate;
        editResourceObj.duration = Duration;
        editResourceObj.ftePercent = FTEPercent;
        editResourceObj.hours = Hours;
        editResourceObj.competences = competences;
        editResourceObj.certificates = certificates;
        editResourceObj.remoteLocation = RemoteLocation;
        editResourceObj.onsiteLocation = OnsiteLocation;
        editResourceObj.resourceCity = locCity;
        editResourceObj.resourceCountry = locCountry;
        editResourceObj.resourceLat = locLat;
        editResourceObj.resourceLng = locLng;
        editResourceObj.resourceTimeZone = locTimezone;
        var JsonObj = JSON.stringify(editResourceObj);

        $.isf.ajax({
            url: service_java_URL + "demandManagement/updateDemandResourceRequest",
            context: this,
            crossdomain: true,
            processData: true,
            ContentType: 'application/json; charset=utf-8',
            type: 'POST',
            data:JsonObj,
            xhrFields: {
                withCredentials: false
            },
            success: AjaxSucceeded,
            error: AjaxFailed
        });

        function AjaxSucceeded(data) {
            pwIsf.alert({ msg: "Resource Modified Successfully", type: "success" });
            $("#EditR").modal('hide');
            $('.modal-backdrop').remove();
            ModalAllOpen(ProjScopeDetailID);
        }
        function AjaxFailed(result) {
            pwIsf.alert({ msg: "Failed to save Demand Resource", type: "error" });
        }
    }
}

function buildCompetencesEditJSON() {
    jsonObj = [];
    var table = $("#table_competenceEdit tbody");

    table.find('tr').each(function (i) {
        var $tds = $(this).find('td');

        var competenceId = $tds.eq(0).text();
        var competenceName = $tds.eq(1).text();
        var competenceLevel = $tds.eq(2).text();

        record = {};
        record["CompetenceID"] = competenceId;
        record["CompetenceLevel"] = competenceLevel;

        jsonObj.push(record);
    });

    var jsonString = JSON.stringify(jsonObj);
    return jsonString;
}

function buildCertificatesEditJSON() {
    jsonObj = [];
    var table = $("#table_certificateEdit tbody");

    table.find('tr').each(function (i) {
        var $tds = $(this).find('td');

        var certificateId = $tds.eq(0).text();
        var certificateName = $tds.eq(1).text();
        var certificateIssuer = $tds.eq(2).text();

        record = {};
        record["CertId"] = certificateId;
        //  record["CertificateIssuer"] = certificateIssuer;
 
        jsonObj.push(record);
    });

    var jsonString = JSON.stringify(jsonObj);
    return jsonString;
}

function getVendorsDR() {
    $.isf.ajax({
        url: service_java_URL + "activityMaster/getVendorDetails",
        success: function (data) {
            $.each(data, function (i, d) {
                vendorData = data;
                $('#select_vendorDR').append('<option value="' + d.vendorID + '">' + d.vendor + '</option>');
                $('#select_vendorEditDR').append('<option value="' + d.vendorID + '">' + d.vendor + '</option>');
            })
        },
        error: function (xhr, status, statusText) {
            console.log('An error occurred');
        }
    });
}

function addAllVendors()
{
    AddVendors(1,"ALU");
    AddVendors(2,"NSN");
    AddVendors(3,"Huawei");
    AddVendors(4,"Nortel");
    AddVendors(5,"Ericsson");
    AddVendors(6,"Juniper");
    AddVendors(7,"SSR");
    AddVendors(8,"Extreme");
    AddVendors(9, "ZTE");
    AddVendors(10, "F5 network");
    AddVendors(11,"Avocent");
    AddVendors(12,"Multivendor");
}

function AddVendors(ID, Name)
{
    $('#select_vendorDR').append('<option value="' + ID + '">' + Name + '</option>');
    $('#select_vendorEditDR').append('<option value="' + ID + '">' + Name + '</option>');
}