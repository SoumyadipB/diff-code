var rrid = '';
$(document).on('click', '.clickable', function (e) {
    var $this = $(this);
    if (!$this.find('i').hasClass('fa-angle-up')) {
        $this.find('i').removeClass('fa-angle-up').addClass('fa-angle-down');
    } else {
        $this.find('i').removeClass('fa-angle-down').addClass('fa-angle-up');
    }
});

//**************************
function fillSearchFilter() {
    getProject();
    getDomain("#selectDomainID");
    getSubServiceArea("#selectSubServiceAreaID");
    getTech();
    getPositionStatus();
    getAllocatedResourceID();
}

//**************************
function getProject() {    
    $.isf.ajax({
        url: service_java_URL + "projectManagement/getAllProjects",
        success: function (data) {
            $.each(data, function (i, d) {
                $('#selectProjectID').append('<option value="' + d.ProjectID + '">' + d.ProjectID + '/' + d.ProjectName + '</option>');
            })
        },
        error: function (xhr, status, statusText) {
            console.log('An error occurred on getProject: ' + xhr.error);
        }
    });
}

//**************************
function getDomain(elementID) {    
    $.isf.ajax({
        url: service_java_URL + "activityMaster/getDomainDetails",
        success: function (data) {
            $.each(data, function (i, d) {
                $(elementID).append('<option value="' + d.domainID + '">' + d.domain + '</option>');
            })
        },
        error: function (xhr, status, statusText) {
            console.log('An error occurred on getDomain: ' + xhr.error);
        }
    });
}

//**************************
function getSubServiceArea(elementID) {    
    $.isf.ajax({
        url: service_java_URL + "activityMaster/getServiceAreaDetails",
        success: function (data) {
            $.each(data, function (i, d) {
                $(elementID).append('<option value="' + d.serviceAreaID + '">' + d.serviceArea + '</option>');
            })
        },
        error: function (xhr, status, statusText) {
            console.log('An error occurred on getServiceAreaDetails: ' + xhr.error);
        }
    });
}

//**************************
function getTech() {    
    $.isf.ajax({
        url: service_java_URL + "activityMaster/getTechnologyDetails",
        success: function (data) {
            $.each(data, function (i, d) {
                $('#selectTechID').append('<option value="' + d.technologyID + '">' + d.technology + '</option>');
            })
        },
        error: function (xhr, status, statusText) {
            console.log('An error occurred');
        }
    });
}

//**************************
function getPositionStatus() {
    $.isf.ajax({
        url: service_java_URL + "resourceManagement/getPositionStatus",
        success: function (data) {
            $.each(data, function (i, d) {
                $('#selectPositionID').append('<option value="' + d.PositionStatus + '">' + d.PositionStatus + '</option>');
            })
        },
        error: function (xhr, status, statusText) {
            console.log('An error occurred on getPositionStatus: ' + xhr.error);
        }
    });
}

//**************************
function getAllocatedResourceID() {
    //var result;
    //$.isf.ajax({
    //    url: service_java_URL + "activityMaster/getEmployeeDetails",
    //    success: function (data) {
    //        result = data;           
    //        $.each(data, function (i, d) {
    //            $('#selectARID').append('<option value="' + d.signum + '">' + d.signum +" / "+d.employeeName + '</option>');
    //        })
    //    },
    //    error: function (xhr, status, statusText) {
    //        console.log('An error occurred on getAllocatedResourceID: ' + status);
    //    }
    //}); 
}

//******************************
function getFormValues(formId) {
    var obj = {};
    var elem = document.getElementById(formId).elements;
    for (var i = 1; i < elem.length; i++) {
        obj[elem[i].name] = elem[i].value
    }
    return obj;

}

//******************************
function submitSearchBtn() {
    pwIsf.addLayer({ text: "Please wait ..." });
    //$("#loaderGif").css("display", "block");
    $("#spocID").val(signumGlobal);
    var filterData = $("#projectViewForm").serialize();
    $.isf.ajax({
        url: service_java_URL + "resourceManagement/GetResourceRequestsByFilter?" + filterData,
        success: function (data) {
            pwIsf.removeLayer();
            if (data == "")
                pwIsf.alert({ msg: "No data found to display!", type: "warning" })
            else {
                resultGridData = data;
                dataTableSetup(1, resultGridData);
                $("#groubyDiv").css("display", "block");
                $("#alertErr").css("display", "none");
            }
            
            //$("#loaderGif").css("display", "none");
        },
        error: function (err) {
            pwIsf.removeLayer();
            console.log("No Data Found: " + err.responseText);
            $("#loaderGif").css("display", "none");
            $("#alertErr").css("display", "block");
            $("#groubyDiv").css("display", "none");
            if ($.fn.dataTable.isDataTable('#filterDataTable')) {
                dataTable.destroy();
                $('#filterDataTable').empty();
            }
        }
    });
    $('html, body').animate({ scrollTop: 0 }, 'fast');
}

//******************************
function dataTableSetup(targetCol, resultGridData) {

    var data = resultGridData;
    if ($.fn.dataTable.isDataTable('#filterDataTable')) {
        dataTable.destroy();
        $('#filterDataTable').empty();

    }

    createProjDetailsTable(resultGridData);
    dataTable = $("#filterDataTable").DataTable({        
        "displayLength": 10,
        colReorder: true,
        "searching": true,
        dom: 'Bfrtip',
        order: [1],
        buttons: [
         'colvis', 'excelHtml5'
        ],
        fixedHeader: {
            header: true
        },
        initComplete: function () {

            $('#filterDataTable tfoot th').each(function (i) {
                var title = $('#filterDataTable thead th').eq($(this).index()).text();
                if (title != "Action")
                    $(this).html('<input type="text"  class="form-control" style="font-size:12px;"  placeholder="Search ' + title + '" data-index="' + i + '" />');
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
            $("#filterDataTable").wrap('<div class="tabWrap" style="overflow:auto;width:100%;"></div>');
        },
        "drawCallback": function (settings) {
            var api = this.api();
            var rows = api.rows({ page: 'current' }).nodes();
            var last = null;

            api.column(targetCol, { page: 'current' }).data().each(function (group, i) {
                if (last !== group) {
                    $(rows).eq(i).before(
                        '<tr class="group"><td colspan="15">' + group + '</td></tr>'
                    );

                    last = group;
                }
            });
        }

    });
    dataTable.columns([7,8,9,10, 11, 12, 13]).visible(false, false);
    dataTable.columns.adjust().draw(false);

    $('#filterDataTable tfoot').insertAfter($('#filterDataTable thead'));

}
//******************************
function createProjDetailsTable(resultGridData) {
    var data = resultGridData;   
    var tr = "";
    tr = '<tr>';
    var td = "";
    var trfoot = "";
    var tdfoot = "";
    td = td + '<th style="width:120px;"><label style="padding-right: 5px;">Action</label></th>';
    tdfoot=tdfoot + '<th></th>'
    for (keys in data[0]) {
        if (keys == "Domain_SubDomain") {
            keys = "Domain/Subdomain";
            td = td + "<th><label style='padding-right: 5px;'>" + keys + "</label></th>";
            tdfoot = tdfoot + "<th>" + keys + "</th>";
        }else if(keys.toLowerCase() == "projectname"){
            keys = "Project ID/Name";
            td = td + "<th><label style='padding-right: 5px;'>" + keys + "</label></th>";
            tdfoot = tdfoot + "<th>" + keys + "</th>";
        }else if (keys.toLowerCase() == "projectid") {

        } else {
            td = td + "<th><label style='padding-right: 5px;'>" + keys + "</label></th>";
            tdfoot = tdfoot + "<th>" + keys + "</th>";
        }
        
    }
    tr = tr + td + "</tr>";
    trfoot = "<tr>" + tdfoot + "</tr>";
    $("#filterDataTable").html("<thead></thead><tbody></tbody><tfoot></tfoot>");    
    $("#filterDataTable thead").html(tr);
    $("#filterDataTable tbody").html("");
    $("#filterDataTable tfoot").html(trfoot);
    for (var i = 0; i < data.length; i++) {
        
        if (data[i].AllocatePercentage == "100") {
            td = '<td>' +
                //'<a style="padding-left:2px;" href="#"  data-toggle="modal" data-target="#ARmodal" onclick="loadAllocatedResources(' + data[i].ResourceRequestID + ')"><i class="fa fa-user" title="ViewPositions"></i></a>' +
                '<a style="padding-left:2px;" href="#" onclick="getProjectResourceDetails(' + data[i].projectId + ')" data-toggle="modal" data-target="#myModal"><i class="fa fa-bars" title="ViewProjectDetails"></i></a>' +
                '<a style="padding-left:2px;" href="#" onclick=getResourceRequestDetailsByRRID(' + data[i].ResourceRequestID + ') data-toggle="modal" data-target="#myModal2"><i class="fa fa-envelope" title="ViewResourceRequestDetails"></i></a></td>';
        } else {
            td = '<td><a style="padding-left:2px;" href="SearchAllocateResource?RRID=' + data[i].ResourceRequestID + '"><i class="fa fa-search" title="SearchAndAllocateResource"></i></a>' +
               // '<a style="padding-left:2px;" href="#"  data-toggle="modal" data-target="#ARmodal" onclick="loadAllocatedResources(' + data[i].ResourceRequestID + ')"><i class="fa fa-user" title="ViewPositions"></i></a>' +
                '<a style="padding-left:2px;" href="#" onclick="getProjectResourceDetails(' + data[i].projectId + ')" data-toggle="modal" data-target="#myModal"><i class="fa fa-bars" title="ViewProjectDetails"></i></a>' +
                '<a style="padding-left:2px;" href="#" onclick=getResourceRequestDetailsByRRID(' + data[i].ResourceRequestID + ') data-toggle="modal" data-target="#myModal2"><i class="fa fa-envelope" title="ViewResourceRequestDetails"></i></a></td>';
        }
        
        tr = "<tr>";
        for (keys in data[i]) {
            if (keys.toLowerCase()=="projectname") {
                td = td + "<td>" + data[i].projectId + "/" + data[i].ProjectName + "</td>";
            } else if (keys.toLowerCase() == "projectid") {
                td = td;
            }else
                td = td + "<td>" + data[i][keys] + "</td>";            
        }
       tr = tr + td + "</tr>";
        $("#filterDataTable tbody").append(tr);
    }
}

//******************************
$(document).on("click", ".groupByBtns", function () {
    console.log("click");
    groupByBtnsClick(this);
});

//*******************************
//function setRRID(rrid) {
//    $("#rrid").text(rrid);
//    getDemandRequestDetail(rrid);
//}

//******************************
function groupByBtnsClick (that) {
  //  console.log($(this).attr("colIndex"));
    $(".groupByBtns").removeClass("active");
    $(that).addClass("active");

    if ($(that).index() == 0) {

        return 0;
    }
    dataTable.destroy();
    dataTableSetup(parseInt($(that).attr("colIndex"), 10), resultGridData);
}

//**************** AllocatedResources View****************
function loadAllPositions() {
    $.isf.ajax({
        url: service_java_URL + "resourceManagement/getPositionsAndAllocatedResources?spoc=" + signumGlobal,
        success: function (data) {            
            ConfigDataTableAR_ReservedPosition(2, data);
            $("#loaderGif").css("display", "none");
        },
        error: function (err) {
            // console.log(err);
            // $("#ARModalError").css("display", "block");
            console.log("An Error occured during position  and allocated resource");
            $("#loaderGif").css("display", "none");
        }
    });
}

//**************** AllocatedResources View****************
function loadAllocatedResources(RRID) {
    $("#ARModalError").css("display", "none");
    rrid = RRID;
    $.isf.ajax({
        url: service_java_URL + "resourceManagement/getPositionsAndAllocatedResources?spoc=" + signumGlobal + "&RRID=" + RRID + "&positionStatus=Proposed",
            success: function (data) {                
                ConfigDataTableAR(2, data);
            },
            error: function (err) {
                // console.log(err);
                //$("#ARModalError").css("display", "block");
                console.log("An Error occured during position  and allocated resource");
            }
        });

    //Calling Reserved Positions API
    $.isf.ajax({
        url: service_java_URL + "resourceManagement/getPositionsAndAllocatedResources?spoc=" + signumGlobal + "&RRID=" + RRID + "&positionStatus=Resource Allocated",
            success: function (data) {                
                ConfigDataTableAR_ReservedPosition(2, data);
            },
            error: function (err) {
                // console.log(err);
                // $("#ARModalError").css("display", "block");
                console.log("An Error occured during position  and allocated resource");
            }
        });


    }

//***************************
function createGridAR(data)
    {
        var tr = '<tr>';
        var trfoot = '<tr>';
        var td = "";
        var tdfoot = "";
        td = td + '<th style="width:100px;"><label style="padding-right: 5px;">Action</label></th>';
        tdfoot = tdfoot + '<th></th>';
        for (keys in data[0]) {
            //td = td + "<th><label style='padding-right: 5px;'>" + keys + "</label></th>";
            //if (keys.toLowerCase() == "signum") {
            //    td = td + "<th><label style='width:100px;padding-right: 5px;'>" + keys + "</label></th>";                
            //} else {
                td = td + "<th><label style='padding-right: 5px;'>" + keys + "</label></th>";                
            //}
            tdfoot = tdfoot + "<th>" + keys + "</th>";

        }
        
        tr = tr + td + "</tr>";
        trfoot = trfoot + tdfoot + "</tr>";
        $("#dataTable_allocatedResource").html('<thead></thead><tbody></tbody><tfoot></tfoot>');
        $("#dataTable_allocatedResource thead").html(tr);
        $("#dataTable_allocatedResource tbody").html("");
        $("#dataTable_allocatedResource tfoot").html(trfoot);

        for (var i = 0; i < data.length; i++) {

            //td = '<td><input type="checkbox" id="chkForAllRowsID" name="chkForAllRows" value="' + i + '"></td>';
            if (data[i].PositionStatus == "Proposed") {
                td = '<td><button class="btn btn-danger-outline" style="padding:0px" title="Cancel Proposal"><i class="fa fa-close" style="padding: 3px 3px;" ></i></button></td>';
            }
            //else if (data[i].PositionStatus == "Resource Allocated") {
            //    td = '<td><button class="btn btn-danger" style="padding:0px" title="SP Change Request"  data-toggle="modal" data-target="#createCRModal" onclick="loadCRModalFilter()">&nbsp; CR &nbsp;</button></td>';
                //}
            else if (data[i].PositionStatus == "Resource Allocated") {

                td = '<td>';
                if (data[i].isPendingCR == "0") {
                    td = td + '<button class="btn btn-danger td_open_cr_modal" style="padding:0px" title="SP Change Request"  data-toggle="modal" data-target="#createCRModal"  onclick="loadCRModalFilter()" >&nbsp; CR &nbsp;</button>';
                }
                td = td + '</td>';
            }
            else {
                td = '<td></td>';
            }
            
            tr = "<tr>";
            for (keys in data[i]) {
                td = td + '<td>' + data[i][keys] + '</td>';
            }
            tr = tr + td + "</tr>";
            $("#dataTable_allocatedResource tbody").append(tr);
        }

    }

//***************************
function loadCRModalFilter() {
    loadAllPositions();
    //getDomain("#selectDomainIDCR");        
    getSubServiceArea("#selectSubServiceAreaIDCR");
}
//***************************
function createGridAR_ReservedPosition(data) {
        var tr = '<tr>';
        var trfoot = '<tr>';
        var td = "";
        var tdfoot = "";
        td = td + '<th style="width:100px;"><label style="padding-right: 5px;">Action</label></th>';
        tdfoot = tdfoot+ '<th></th>'
        for (keys in data[0]) {
            if (keys != "isPendingCR"){
            td = td + "<th><label style='padding-right: 5px;'>" + keys + "</label></th>";
            tdfoot = tdfoot + "<th>" + keys + "</th>";
            }
        }

        tr = tr + td + "</tr>";
        trfoot = trfoot + tdfoot + "</tr>";
        $("#dataTable_allocatedResource_Reserved").html('<thead></thead><tbody></tbody><tfoot></tfoot>');
        $("#dataTable_allocatedResource_Reserved thead").html(tr);
        $("#dataTable_allocatedResource_Reserved tbody").html("");
        $("#dataTable_allocatedResource_Reserved tfoot").html(trfoot);

        for (var i = 0; i < data.length; i++) {
            td = '';            
            if (data[i].PositionStatus == "Proposed") {
                td = '<td><button class="btn btn-danger-outline td_cancel_proposal" style="padding:0px" title="Cancel Proposal-1"><i class="fa fa-close" style="padding: 3px 3px;" ></i></button></td>';
            } else if (data[i].PositionStatus == "Resource Allocated") {
                
                td = '<td>';
                if (data[i].isPendingCR == "0") {
                    td = td +'<button class="btn btn-danger td_open_cr_modal" style="padding:0px" title="SP Change Request"  data-toggle="modal" data-target="#createCRModal"  onclick="loadCRModalFilter()" >&nbsp; CR &nbsp;</button>';
                }
                td = td + '</td>';
            } else {
                td = '<td></td>';
            } tr = "<tr>";
            for (keys in data[i]) {
                if (keys != "isPendingCR") {
                    if (keys.toLowerCase() == "signum" || keys.toLowerCase() == "managername") {
                        if(data[i][keys]!=null)
                        td = td + '<td><a href="#" style="color:blue;" onclick="getEmployeeDetailsBySignum(\'' + (data[i][keys]).split("/")[0] + '\')">' + data[i][keys] + '/</a></td>';
                        else
                            td = td + "<td>" + data[i][keys] + "</td>";
                    } else
                        td = td + "<td>" + data[i][keys] + "</td>";
                }
            }
            tr = tr + td + "</tr>";
            $("#dataTable_allocatedResource_Reserved tbody").append(tr);
        }
    }

//***************************
function ConfigDataTableAR(targetCol,data)
    {

        if ($.fn.dataTable.isDataTable('#dataTable_allocatedResource')) {
            tableAR.destroy();
            $('#dataTable_allocatedResource').empty();

        }

        createGridAR(data);
              
                    tableAR = $("#dataTable_allocatedResource").DataTable({                        
                        //"order": [[targetCol, 'asc']],
                        "displayLength": 10,
                        "searching": true,
                        colReorder: true,
                        responsive: true,
                        order: [1],
                       
                        dom: 'Bfrtip',
                        buttons: [
                         'colvis', 'excelHtml5'
                        ],
                        initComplete: function () {

                            $('#dataTable_allocatedResource tfoot th').each(function (i) {
                                var title = $('#dataTable_allocatedResource thead th').eq($(this).index()).text();
                                if(title!='Action')
                                    $(this).html('<input type="text" class="form-control" style="font-size:12px;"  placeholder="Search ' + title + '" data-index="' + i + '" />');
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

                        },
                        "drawCallback": function (settings) {
                            var api = this.api();
                            var rows = api.rows({ page: 'current' }).nodes();
                            var last = null;

                        }

                    });
                    tableAR.columns([10, 11, 12, 13, 14, 15,17]).visible(false, false);
                    tableAR.columns.adjust().draw(false); 

                    $('#dataTable_allocatedResource tfoot').insertAfter($('#dataTable_allocatedResource thead'));

                    var checkedRowsData = [];
                    $('#dataTable_allocatedResource tbody').on('click', 'tr', function () {                        
                        var d = tableAR.row(this).data();
                        //dataTable_allocatedResource                        
                        if (d[4] == "Proposed") {
                            var service_data = '[{"weid":"' + d[1] + '","loggedInUser":"' + signumGlobal + '"}]';

                                $.isf.ajax({

                                    url: service_java_URL + "activityMaster/cancelProposedResources",
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
                                        loadAllocatedResources(rrid);
                                    },
                                    error: function (xhr, status, statusText) {
                                        alert("Failed to Cancel Proposal");
                                        //console.log('An error occurred ::' + xhr.responseText);
                                    }
                                });
                            }                            

                    });
                    
   
    }

//***************************
function ConfigDataTableAR_ReservedPosition(targetCol, data) {
        if ($.fn.dataTable.isDataTable('#dataTable_allocatedResource_Reserved')) {
            tableARReservedPosition.destroy();
            $('#dataTable_allocatedResource_Reserved').empty();

        }
      $('#dataTable_allocatedResource_Reserved_tbody').html('');
    $("#dataTable_allocatedResource_Reserved").append($(`<tfoot><tr><th> </th><th> WORKEFFORTID</th>
    <th>RESOURCEPOSITIONID</th><th>RESOURCEREQUESTID</th><th>PROJECTID</th>
    <th>POSITIONSTATUS</th><th>START_DATE</th><th>END_DATE</th><th>WORKEFFORTSTATUS</th>
   <th>SIGNUM</th><th>AVAILABILITY</th><th>JOBSTAGE</th><th>MANAGERNAME</th><th>DOMAIN/SUBDOMAIN</th>
   <th>SERVICEAREA/SUBSERVICEAREA</th><th>VENDOR-TECH</th><th>TEXT</th><th>STATUS</th><th>CRID</th></tr></tfoot>`));
        tableARReservedPosition = $("#dataTable_allocatedResource_Reserved").DataTable({
            "displayLength": 10,
            "searching": true,
            colReorder: true,
            responsive: true,
            order: [1],
            dom: 'Bfrtip',
            buttons: [
             'colvis', 'excelHtml5'
            ], "data": data,
            "columns": [
                {
                    "title": "ACTION",
                    "data": null,
                    "render": function (data, type, row, meta) {
                        if (data.positionStatus === "Proposed") {
                            return `<span><button class="btn btn-danger-outline td_cancel_proposal" style="padding:0px"
                               title="Cancel Proposal-1"><i class="fa fa-close" style="padding: 3px 3px;" ></i></button></span>`;
                        } else if (data.positionStatus === "Resource Allocated") {


                            if (data.isPendingCR === "0") {
                                return `<span><button class="btn btn-danger td_open_cr_modal" style="padding:0px"
                          title="SP Change Request"  data-toggle="modal" data-target="#createCRModal"  onclick="loadCRModalFilter()">&nbsp; CR &nbsp;</button></span>`;
                            }
                            return '';
                        } else {
                            return '';
                        }
                    }
                },
                {
                    "title": "WORKEFFORTID",
                    "data": "workEffortID"

                },
                {
                    "title": "RESOURCEPOSITIONID",
                    "data": "resourcePositionId"
                },
                {
                    "title": "RESOURCEREQUESTID",
                    "data": "resourceRequestID"
                },

                {
                    "title": "PROJECTID",
                    "data": "projectid"
                },
                {
                    "title": "POSITIONSTATUS",
                    "data": "positionStatus"
                },
                {
                    "title": "START_DATE",
                    "data": "start_date"
                },

                {
                    "title": "END_DATE",
                    "data": "end_date"
                },
                {
                    "title": "WORKEFFORTSTATUS",
                    "data": "workEffortStatus"
                },
                {
                    "title": "SIGNUM",
                    "data": null,
                    "render": function (data, type, row, meta) {
                        if (data.positionStatus !== "isPendingCR") {
                            if (data.signum != null) {
                                return `<a href="#" style="color:blue;
                                       " onclick="getEmployeeDetailsBySignum(\'${(data.signum).split("/")[0]}\')">${data.signum}/</a>`;
                            } else {
                                return `<span>${data.signum}</span>`;
                            }
                        }
                    }
                },
                {
                    "title": "AVAILABILITY",
                    "data": "availability"
                },

                {
                    "title": "JOBSTAGE",
                    "data": null,
                    "render": function (data, type, row, meta) {
                        var jobstage = '-';
                        return `<span>${jobstage}</span>`;
                    }
                },
                {
                    "title": "MANAGERNAME",
                    "data": null,
                    "render": function (data, type, row, meta) {
                        if (data.positionStatus !== "isPendingCR") {
                            if (data.managerName != null) {
                                return `<a href="#" style="color:blue;"
                                onclick = "getEmployeeDetailsBySignum(\'${(data.managerName).split("/")[0]}\')">${data.managerName}/</a>`;
                            } else {
                                return `<span>${data.managerName}</span>`;
                            }
                        }
                    }
                },
                {
                    "title": "DOMAIN/SUBDOMAIN",
                    "data": "domain_Subdomain"
                },
                {
                    "title": "SERVICEAREA/SUBSERVICEAREA",
                    "data": "serviceArea_SubServiceArea"
                },

                {
                    "title": "VENDOR-TECH",
                    "data": null,
                    "render": function (data, type, row, meta) {
                        return vendorTechTemplateForDemandFulfilment(data);
                    }
                },
                {
                    "title": "TEXT",
                    "data": "text"
                },

                {
                    "title": "STATUS",
                    "data": "status"
                },
                {
                    "title": "CRID",
                    "data": "crid"
                }
            ],
            initComplete: function () {

                $('#dataTable_allocatedResource_Reserved tfoot th').each(function (i) {
                    var title = $('#dataTable_allocatedResource_Reserved thead th').eq($(this).index()).text();
                    if (title !== 'ACTION') {
                        $(this).html('<input type="text" class="form-control" style="font-size:12px;" placeholder="Search ' + title + '" data-index="' + i + '" />');
                    }
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
            },
            "drawCallback": function (settings) {
                var api = this.api();
                var rows = api.rows({ page: 'current' }).nodes();
                var last = null;

            }

        });
        $('#dataTable_allocatedResource_Reserved tfoot').insertAfter($('#dataTable_allocatedResource_Reserved thead'));
        $('#dataTable_allocatedResource_Reserved tbody').on('click', '.td_open_cr_modal,.td_cancel_proposal', function () {
            var _$ele = $(event.target);
            var _$tr = _$ele.closest("tr");
            WEID = _$tr.children()[1].innerText;
            RPID = _$tr.children()[2].innerText;
            SDATE = _$tr.children()[6].innerText;
            EDATE = _$tr.children()[7].innerText;
            if (_$tr.children()[5].innerText === "Proposed") {
                var service_data = '[{"weid":"' + _$tr.children()[1].innerText + '","loggedInUser":"' + signumGlobal + '"}]';

                $.isf.ajax({

                    url: service_java_URL + "activityMaster/cancelProposedResources",
                    context: _$tr,
                    crossdomain: true,
                    processData: true,
                    contentType: 'application/json',
                    type: 'POST',
                    data: service_data,
                    xhrFields: {
                        withCredentials: false
                    },
                    success: function (data) {
                        //loadAllocatedResources(rrid);
                        location.reload();
                    },
                    error: function (xhr, status, statusText) {
                          alert("Failed to Cancel Proposal");
                       // console.log('An error occurred ::' + xhr.responseText);
                    }
                });
            }
            else if (_$tr.children()[5].innerText === "Resource Allocated") {
               
                //PreviousResourceDiv
                var htmlstr = "";
                $("#PreviousResourceDiv").html("");
                htmlstr = '<div class="col-lg-3 form-group">' +
                    '<label for="contain">Signum : ' + _$tr.children()[9].outerHTML + '</label>' +///7
                            '</div>' +
                            '<div class="col-lg-2 form-group">' +
                    '<label for="contain">Availability : ' + _$tr.children()[10].innerText + '</label>' +///7
                            '</div>' +
                           
                            '<div class="col-lg-2 form-group">' +
                    '<label for="contain">RpId : <span id="chngReqPositionRpId">' + _$tr.children()[2].innerText + '</span></label>' +///7
                            '</div>' +
                            '<div class="col-lg-2 form-group">' +
                    '<label for="contain">Start Date : <span id="chngReqPositionStartDate">' + _$tr.children()[6].innerText + '</span></label>' +///7
                            '</div>' +
                            
                            '<div class="col-lg-2 form-group">' +
                    '<label for="contain">End Date : <span id="chngReqPositionEndDate">' + _$tr.children()[7].innerText + '</span></label>' +///7
                            '</div>' +
                            '<div class="col-lg-2 form-group">' +
                    '<label for="contain">JobStage : ' + _$tr.children()[11].innerText + '</label>' +///7
                            '</div>' +
                    '<div class="col-lg-3 form-group">' +
                    '<label for="contain">Manager : ' + _$tr.children()[12].outerHTML + '</label>' +///7
                            '</div>';

                $("#PreviousResourceDiv").append(htmlstr);
            }
            
        });

    }
//*****************Vendor Tech Functions

function vendorTechTemplateForDemandFulfilment(task) {
    if (task.vendor_tech.length > 1) {
        return `<span onclick="showMultiVendorTechViewDF(${task.resourceRequestID})">
            <input id="selectedVendorTech_${task.resourceRequestID}" type="hidden" value="${task.vendor_tech}"/>
            <u style="color:blue;cursor:pointer;"  title="Click to View All Vendor-Tech Combinations">View Multi</u>
            </span>`;
    }
    else {
        if (task.vendor_tech.length === 1) {
           var arrVendorTechForDF = task.vendor_tech;
            return `<span>
            <input id="selectedVendorTech_${task.resourceRequestID}" type="hidden" value="${arrVendorTechForDF}"/>
            ${arrVendorTechForDF}
            </span>`;
        }
        else {
           var arrVendorTechForDF = [];
            return `<span>
            <input id="selectedVendorTech_${task.resourceRequestID}" type="hidden" value="${arrVendorTechForDF}"/>
            ${arrVendorTechForDF}
            </span>`;
        }
    }
}

    function showMultiVendorTechViewDF(taskId) {
        let multiVendorTechTablehtml = '';
        $("#tblMultipleVenderTech").empty();
        var multiVendorTechVal = $(`#selectedVendorTech_${taskId}`).val();
        multiVendorTechVal = multiVendorTechVal.split(',');
        if (multiVendorTechVal !== null) {
            $.each(multiVendorTechVal, function (i, d) {
                multiVendorTechTablehtml = `${multiVendorTechTablehtml}<tr><td>${d}</td></tr>`;
            })
        }
        else {
            multiVendorTechTablehtml = `${multiVendorTechTablehtml}<tr><td>Error</td></tr>`
        }
        $('#tblMultipleVenderTech').append(multiVendorTechTablehtml);
        $('#multiVendorTechView').modal('show');
    }


    function closeVendorTechModal() {
        $('#multiVendorTechView').modal('hide');
    }

function getProjectResourceDetails(project) {

        $("#myModalError").css("display", "none");
       $.isf.ajax({

           url: service_java_URL + "projectManagement/getProjectDetails?ProjectID=" + project,
            success: function (data) {
                $(project_ID).html(data.projectID);
               // $("view_project_id").val(data.projectID);
                $("#project_name").html(data.projectName);
                $("#select_Project_Type").html(data.projectType);
                $("#cpm").html(data.cPM);
                $("#project_creator").html(data.projectCreator);
                $("#select_opportunity_name").html(data.opportunityDetails.opportunityName);
                $("#start_date").html(data.startDate);
                $("#select_enddate").html(data.endDate);
                $("#select_market_area").html(data.opportunityDetails.marketAreaName);
                $("#select_country").html(data.opportunityDetails.countryName);
                $("#select_demand_owning_company").html(data.opportunityDetails.companyName);
                $("#select_customer_name").html(data.opportunityDetails.customerName);
                $("#project_description").html(data.projectDescription);
                //$("#select_product_area").html(data.ProductAreaID);
                $("#select_operationalmanager").html(data.operationalManager);
                $("#select_status").html(data.status);
                var row = "";
                $.each(data.projectDocuments, function (i, d) {

                    row += "<tr>";
                    row += "<td>" + d.documentType;
                    row += "</td>";

                    row += "<td>" + d.documentLinks;
                    row += "</td>";
                });
                
                $("#dataTables_ViewDocuments td").remove();
                $("#dataTables_ViewDocuments").append(row);

                getserviceArea();
                
            },
            error: function (xhr, status, statusText) {
                
                $("#myModalError").css("display", "block");
                console.log('An error occurred on getProjectDetails: ' + xhr.error);
            }
        })
    }

//*****************Refresh Btn click
function  cleanFieldsRequest() {
        $("#selectProjectID").val(0);
        $("#selectProjectID").trigger('change');
        $("#selectDomainID").val(0);
        $("#selectDomainID").trigger('change');
        $("#selectSubServiceAreaID").val(0);
        $("#selectSubServiceAreaID").trigger('change');
        $("#selectTechID").val(0);
        $("#selectTechID").trigger('change');
        $("#selectPositionID").val(0);
        $("#selectPositionID").trigger('change');
        $("#selectARID").val(0);
        $("#selectARID").trigger('change');
        $("#selectSubDomainID").val(0);
        $("#selectSubDomainID").trigger('change');
        fillSearchFilter();        
    }

//*****************
function getserviceArea()
    {
        var dataServiceArea = "";
        var sep;
    var projectID = $("#project_ID").text();

    if (projectID == undefined || projectID == null || projectID == '' || isNaN(parseInt(projectID))) {
        console.log("Project id is wrong");
        pwIsf.alert({ msg: 'Wrong Project Id', type: 'error' });
    }
    else {
        $.isf.ajax({

            url: service_java_URL + "activityMaster/getServiceAreaDetailsByProject?projectID=" + parseInt(projectID),
            success: function (data) {
                $.each(data, function (i, d) {
                    dataServiceArea += d.serviceArea;
                });
                var service = dataServiceArea.split("/");
                document.getElementById("select_product_area").innerHTML = service[0];
            },
            error: function (xhr, status, statusText) {
                console.log('An error occurred on getServiceAreaDetails: ' + xhr.error);
            }
        });
    }

       
    }

//***************** Get Resource Request Details Modal**********
function getResourceRequestDetailsByRRID(RRID) {
    $("#lblRequestType").text('');
    $("#lblVendors").text('');
        $("#lblResourceType").text('');
        $("#lblJobStage").text('');
        $("#lblJobRole").text('');
        $("#lblDeliveryLocation").text('');
        $("#lblOnsiteCount").text('');
        $("#lblRemoteCount").text('');
         $("#myModalErr").css("display", "none");
         if ($.fn.dataTable.isDataTable('#CompetenceDetailsTable')) {
             CompetenceTable.destroy();
             $('#CompetenceDetailsTable').empty();
         }

         if ($.fn.dataTable.isDataTable('#CertificationDetailsTable')) {
             CertificationTable.destroy();
             $('#CertificationDetailsTable').empty();
         }
        $.isf.ajax({
            url: service_java_URL + "resourceManagement/getDemandRequestDetail/" + RRID,
            success: function (d) {

                $("#lblRequestType").text(d[0].requestType);
                $("#lblVendors").text(d[0].vendors);
                $("#lblResourceType").text(d[0].resourceType);
                $("#lblJobStage").text(d[0].jobStageName);
                $("#lblJobRole").text(d[0].jobRoleName);
                if (d[0].deliveryLocation != null)
                    $("#lblDeliveryLocation").text(d[0].deliveryLocation);
                else
                    $("#lblDeliveryLocation").text(d[0].remoteLocation);
                $("#lblOnsiteCount").text(d[0].onsiteCount);//rpefList[0]["onsiteCount"]);
                $("#lblRemoteCount").text(d[0].remoteCount);//rpefList[0]["remoteCount"]);


                var sDate = d[0].rpefList[0]["startDate"];
                var eDate = d[0].rpefList[0]["endDate"];
                var totalHours = d[0].rpefList[0]["totalHours"];
                var flag = true;
                $("#lblStartDate").text(sDate);
                $("#lblEndDate").text(eDate);
                $("#lblHoursPerDay").text(totalHours);
              
                    if ((d[0].competenceList.length) != 0) {
                        flag = true;
                    } else {
                        flag = false;
                    }
                    if (flag == true) {
                        CompetenceTable = $('#CompetenceDetailsTable').DataTable({
                            "searching": true,
                            colReorder: true,
                            responsive: true,
                            paging:false,
                            distroy:true,
                            "data": d[0].competenceList, "bFilter": false, "bInfo": false,
                            dom: 'Bfrtip',
                            buttons: [
                             'colvis', 'excelHtml5'
                            ],
                            "columns": [
                                {
                                    "title": "CompetenceType",
                                    "data": "competenceType"

                                },
                                {
                                    "title": "Competence",
                                    "data": "competenceName"
                                }
                                ,
                                {
                                    "title": "Level",
                                    "data": "competenceLevel"
                                }
                            ],
                            initComplete: function () {
                                $('#CompetenceDetailsTable tfoot th').each(function (i) {
                                    var title = $('#CompetenceDetailsTable thead th').eq($(this).index()).text();
                                    $(this).html('<input type="text"  class="form-control" style="font-size:12px;"  placeholder="Search ' + title + '" data-index="' + i + '" />');
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

                        $('#CompetenceDetailsTable tfoot').insertAfter($('#CompetenceDetailsTable thead'));                  
                }
              

                if ((d[0].certificateList.length) != 0) {
                    flag = true;
                } else {
                    flag = false;
                }
                if (flag == true) {
                        CertificationTable = $('#CertificationDetailsTable').DataTable({
                            "searching": true,
                            colReorder: true,
                            responsive: true,
                            paging:false,
                            "data": d[0].certificateList, "bFilter": false, "bInfo": false,
                           
                            dom: 'Bfrtip',
                            buttons: [
                             'colvis'
                            ],
                            "columns": [
                                {
                                    "title": "CertificateType",
                                    "data": "CertificateType"
                                },
                                {
                                    "title": "Certificate",
                                    "data": "CertificateName"
                                }

                            ],
                            initComplete: function () {

                                $('#CertificationDetailsTable tfoot th').each(function (i) {
                                    var title = $('#CertificationDetailsTable thead th').eq($(this).index()).text();
                                    $(this).html('<input type="text"  class="form-control" style="font-size:12px;"  placeholder="Search ' + title + '" data-index="' + i + '" />');
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
                        $('#CertificationDetailsTable tfoot').insertAfter($('#CertificationDetailsTable thead'));
                }
               
               
            },
            error: function (xhr, status, statusText) {
                $("#myModalErr").css("display", "block");
                console.log('An error occurred : ' + xhr.responseText);
            }
        });
}
    
//******************************************************
function searchNewPositionForCR() {

    var jobStage = $('#search_select_jobstage').val();
    var jobName = $('#search_select_jobrole').val();
    var mgrSignum=$("#search_select_manager").val();
    var Domain = $("#selectDomainIDCR").val();
    var ServiceArea = $("#selectSubServiceAreaIDCR").val();
    var startDate = $('#chngReqPositionStartDate').text();
    var endDate = $('#chngReqPositionEndDate').text();

    var service_data = {
        startDate: startDate,
        endDate: endDate,
        hours:10,
        jobStage: jobStage,
        jobName: jobName,
        managerSignum: mgrSignum,
        domain:Domain,
        serviceArea:ServiceArea
    };

        //var service_data = '{"resourceRequestID":' + rrid + 
        //       ',"jobStageID":"' + jobStageID +'"
        //       ',"jobRoleID":"' + jobRoleID +'"'
        //       ',"certificateID":""' +
        //       ',","competenceString":""' +
        //       ',"competenceLevel":""' +
        //       ',"signum":' + localStorage.getItem("userLoggedIn") +
        //       ',"subDomainID":""' +
        //       ',"subServiceAreaID":' + subServiceAreaID +
        //       ',"startDate":""' +
        //       ',"endDate":"" '+
        //       ',"hours":""' +
        //       ',"managerSignum":' + $("#search_select_manager").val() +
        //       ',"","domainID":' + domainID +
        //       ',"domain":' + Domain +
        //       ',"serviceArea":' + ServiceArea +
        //       ',"subDomain":"" '+
        //       ',"jobStage":' + JobStage +
        //       ',"jobName":""}';


    $('#loadingSearchTab').html('Loading ...');
        $.isf.ajax({

            url: service_java_URL + "resourceManagement/SearchResourcesByFilter",
            context: this,
            crossdomain: true,
            processData: true,
            contentType: 'application/json',
            type: 'POST',
            data: JSON.stringify(service_data),
            xhrFields: {
                withCredentials: false
            },
            success: function (data){

                var table = '#dataTableNewResourceList';
                if ($.fn.dataTable.isDataTable(table)) {
                    dataTable.destroy();
                    $(table).empty();

                }
                createhtmlGrid(data);

                dataTable = $(table).DataTable({
                    
                    "order": [[2, 'asc']],
                    "displayLength": 10,
                    "searching": true,
                    "colReorder": true,
                    "paging": true,
                    "responsive": true,
                    "destroy": true,
                    dom: 'Bfrtip',
                    buttons: [
                     'colvis', 'excelHtml5'
                    ],
                    "drawCallback": function (settings) {
                        var api = this.api();
                        var rows = api.rows({ page: 'current' }).nodes();
                        var last = null;

                    }
                });

                $('#loadingSearchTab').html('');

            },
            error: function (data) {
                alert("Unsuccessful");
                }
              
        });

        
    }

//*******************************
function createhtmlGrid(data) {
        var tablediv = '';
        tablediv = tablediv + '<thead><tr><th >Action</th><th>Signum</th><th>Name</th><th>ManagerSignum</th><th>ManagerName</th><th>Jobstage</th><th>Availability %</th></tr></thead>';
        tablediv = tablediv + '<tbody>';

        for (var i = 0; i < data.length; i++) {
            tablediv = tablediv + '<tr class="odd gradeX">';
            tablediv = tablediv + '<td><input type="radio" name="selectNewSignumForCR" value="' + data[i].Signum + '" >';
            tablediv = tablediv + '<td>' + data[i].Signum + '</td>';
            tablediv = tablediv + '<td>' + data[i].employeeName + '</td>';
            tablediv = tablediv + '<td>' + data[i].managerSignum + '</td>';
            tablediv = tablediv + '<td>' + data[i].managerName + '</td>';
            tablediv = tablediv + '<td>' + data[i].JobStage + '</td>';
            tablediv = tablediv + '<td>' + data[i].Availability + '</td>';
            tablediv = tablediv + '</tr>';
        }
        tablediv = tablediv + '</tbody>';
        tablediv = tablediv + '<tfoot><tr>';
        tablediv = tablediv + '</tr><tr><td colspan="7">';
        tablediv = tablediv + '<div id="allocatesuccessSAR" style="display:none" class="alert alert-danger">';
        tablediv = tablediv + '<strong>Success!</strong>Resources has been allocated successfully</div>';
        tablediv = tablediv + '<div id="allocatefailureSAR" style="display:none" class="alert alert-danger"><strong>Failure!</strong>Resources could not be allocated</div>';
        tablediv = tablediv + '</td></tr></tfoot> ';
        $("#dataTableNewResourceList").append(tablediv);

    }