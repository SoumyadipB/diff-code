//$(document).ready(function () {


//    //var todayDate = new Date().getDate();
//    //$("#datepickerID").datepicker({ startDate: new Date(new Date().setDate(todayDate)) , endDate: new Date(new Date().setDate(todayDate + 30)) });

//    fillSelect();
//    $("[id$=date_start]").datepicker({ minDate: 0 });


//    $("#srch-btn").on("click", function getProjByFltr() {
//        getProjectByFilters();
//    })

//    var obj = {};
//    obj.disableProject = function (url, _id, signumGlobal) {
//        var param = "{\"projectID\":" + _id + ",\"lastModifiedBy\":\"" + signumGlobal + "\"}";
//        return $.isf.ajax({
//            method: 'POST',
//            data: param,
//            context: this,
//            crossDomain: true,
//            processData: true,
//            contentType: 'application/json',
//            url: url,
//            error: function (xhr, status, statusText) {
//                alert("Fail " + xhr.responseText);
//                //alert("status " + xhr.status);
//            },
//            xhrFields: { withCredentials: false }
//        })
//    }


//    //Delete Project on Delete Icon Click in table
//    $(document).on("click", "#table_project_search tbody i.del_project", function (event) {
//        var _$ele = $(event.target);
//        var _$tr = _$ele.closest("tr");
//        var _$firstTd = _$tr.children()[0];

//        var _$fifthTd = _$tr.children()[4].innerText;
//        var _$eigthTd = _$tr.children()[7].innerText;


//        var _id = parseInt(_$firstTd.innerText, 10);
//        if (confirm("Are you sure to delete Project id : " + _id)) {
//            var _$promise = obj.disableProject(service_java_URL + "projectManagement/deleteProject", _id, signumGlobal);
//            _$promise.done(function (data) {
//                alert('Deleted Successfully.');
//            });
//            _$tr.remove();
//        }
//    });


//    $(document).on("change", "#select_country", function () {
//        if ($("#select_country").val() == 0) {
//            $('#select_customer_name').empty();
//            $('#select_customer_name').append('<option value="0">Select One</option>');
//        } else {
//            var _promise = getCustomers($("#select_country").val());
//            _promise.done(function () {
//            });
//        }
//    });
//});

var marketAreaName = JSON.parse(ActiveProfileSession).organisation; 
let userRoleID = JSON.parse(ActiveProfileSession).roleID; 


$(document).ready(function () {

    let userRollArr = [1,4,5]; // PM,DR,OM
    if (userRollArr.includes(userRoleID)) {
        
        if ($('#srch-btn')) {
            setTimeout(function () { $('#srch-btn').click() }, 1000);
        }
    }


});

function fillSelect() {

    //getAllProjects();
    getMarketAreas();
    getCountries();
    getAllProjectByMarketArea();

    fillStatus();

    
}
function getAllProjectByMarketArea() {
    var marketArea = $('#select_market_area').val();
    $.isf.ajax({
        url: service_java_URL + "projectManagement/getProjectByFilters?marketAreaID=" + marketArea,
        success: function (data) {

            $.each(data, function (i, d) {
                $('#select_projectID').append('<option value="' + d.projectID + '">' + d.projectID + '</option>');
                $('#select_project_name').append('<option value="' + d.projectID + '">' + d.projectName + '</option>');
            })

        },
        error: function (xhr, status, statusText) {
            console.log('An error occurred on getAllProjects: ' + xhr.error);
        }
    });
}


function getAllProjects() {

    $.isf.ajax({
        url: service_java_URL + "projectManagement/getAllProjects" /*+ marketAreaID + "&CountryID=" + countryID*/,
        success: function (data) {

            $.each(data, function (i, d) {
                $('#select_projectID').append('<option value="' + d.ProjectID + '">' + d.ProjectID + '</option>');
                $('#select_project_name').append('<option value="' + d.ProjectID + '">' + d.ProjectName + '</option>');
            })

        },
        error: function (xhr, status, statusText) {
            console.log('An error occurred on getAllProjects: ' + xhr.error);
        }
    });

}

function getMarketAreas() {

    $.isf.ajax({
        //url: service_DotNET_URL + "MarketAreas",
        url: service_java_URL + "projectManagement/getMarketAreas",
        async: false,
        success: function (data) {

            $.each(data, function (i, d) {
                $('#select_market_area').append('<option value="' + d.MarketAreaID + '">' + d.MarketAreaName + '</option>');
            })
            var accessProfileName = JSON.parse(ActiveProfileSession).accessProfileName;
            if (marketAreaArray.includes(marketAreaName)) {
                $("#select_market_area option").filter(function () {
                    return this.text == marketAreaName;
                }).attr('selected', true).trigger('change');

                $("#select_market_area").attr('disabled', 'disabled');

                // Call getCustomer based on market area
                getCustomers(0, $("#select_market_area").val());
            }
            else 
            $("#select_market_area").attr('disabled', false); 
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
            $('#select_country').append('<option value="' + 0 + '"></option>');
            $.each(data.responseData, function (i, d) {
                $('#select_country').append('<option value="' + d.countryID + '">' + d.countryName + '</option>');
            })
        },
        error: function (xhr, status, statusText) {
            console.log('An error occurred on getCountries');
        }
    });

}

function getCountrybyMarketAreaID() {
    var marketAreaId = document.getElementById("select_market_area").value;

    $.isf.ajax({
        url: service_java_URL + "projectManagement/getCountrybyMarketAreaID?marketAreaID=" + marketAreaId,
        success: function (data) {
            $('#select_country').empty();
            $('#select_country').append('<option value="' + 0 + '"></option>');
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

function getProjectDetailsBy(paramsObj) {

    let url = service_java_URL + "projectManagement/getProjectByFilters";
    let addInUrl = ''; let count = 0;
    for (let i in paramsObj) {
        if (count == 0) {
            addInUrl += '?' + i + '=' + paramsObj[i];
        } else {
            addInUrl += '&' + i + '=' + paramsObj[i];
        }

        count++;
    }

    url += addInUrl;

    var promise = $.isf.ajax({
        url: url,
        returnAjaxObj: true
        //async: false,
        //success: function (data) {
           
        //},
        //error: function (xhr, status, statusText) {
        //    console.log('An error occurred on getCustomers' + xhr.responseText);
        //}
    });
    return promise;

}

function getProjectNameByProjectId(projectId) {

    let maId = $("#select_market_area").val();
    
    if (projectId > 0) {
        url = service_java_URL + "projectManagement/getProjectByFilters?projectID=" + projectId;
    } else {
        url = service_java_URL + "projectManagement/getProjectByFilters?marketAreaID=" + maId;
    }
    return $.isf.ajax({
        url: url,
        success: function (data) {
            $('#select_project_name').empty();
            $('#select_project_name').append('<option value="0">Select One</option>');
            $.each(data, function (i, d) {
                //if (i < 50) {
                $('#select_project_name').append('<option value="' + d.projectID + '">' + d.projectName + '</option>');
                //}
            });
        },
        error: function (xhr, status, statusText) {
            console.log('An error occurred on getCustomers' + xhr.responseText);
        }
    });

}




function getCustomers(cntryId, marketAreaId = 0) {

    if (marketAreaId == 0) {
        return true;
    }

    $('#select_customer_name').val(0);
    $('#select_customer_name').empty();
    $('#select_customer_name').append('<option value="0">Select One</option>');

    //var url = service_DotNET_URL + "Customers";
    let url = service_java_URL + `activityMaster/getCustomerDetailsByMA?`;
    let addInUrl = '';
    if (cntryId > 0) {
        addInUrl = "countryID=" + cntryId;
    }
    if (marketAreaId > 0) {
        addInUrl = "marketAreaID=" + marketAreaId;  // No entry needs to be displayed
    }
    if (cntryId > 0 && marketAreaId > 0) {
        addInUrl = "countryID=" + cntryId+"&marketAreaID=" + marketAreaId;  // No entry needs to be displayed
    }

    url += addInUrl; 
    
    return $.isf.ajax({
        url: url,
        returnAjaxObj: true,
        success: function (data) {

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

function fillStatus() {
    $('#select_status').append('<option value="Initiation">Initiation</option>');
    $('#select_status').append('<option value="Approved">Approved</option>');
    $('#select_status').append('<option value="Rejected">Rejected</option>');


}

function cleanFields() {

    getMarketAreas();
    $("#select_projectID").val(0);
    $("#select_projectID").trigger('change');

    $("#select_project_name").val(0);
    $("#select_project_name").trigger('change');

    //$("#select_market_area").val(0);
    //$("#select_market_area").trigger('change');

    $("#select_country").val(0);
    $("#select_country").trigger('change');

    $("#select_customer_name").val(0);
    $("#select_customer_name").trigger('change');

    $("#select_status").val(0);
    $("#select_status").trigger('change');

    $("#select_service_area").val(0);
    $("#select_service_area").trigger('change');

    getCountries();


}


function isFirstParam(firstParam) {
    if (firstParam == 0) {
        return "";
    }
    return "&";
}

function fmtDateToDisplay(dt) {
    if (dt == "" || dt == null) {
        return '';
    } else {
        var from = dt.split(/[\D]/);
        return from[1] + '/' + from[2] + '/' + from[0];
    }

}

function getProjectByFilters() {
    if ($.fn.dataTable.isDataTable('#table_project_search')) {
        oTable.destroy();
        $('#table_project_search').empty();
    }
    var project = new Object();
    project.projectID = $("#select_projectID").val();
    project.projectNameID = $("#select_project_name").val();
    project.Status = $("#select_status").val();
    project.countryID = $("#select_country").val() == null ? 0 : $("#select_country").val();
    project.customerID = $("#select_customer_name").val();
    project.marketAreaID = $("#select_market_area").val();
    project.serviceAreaID = $("#select_service_area").val();

    var params = "";
    var firstParam = 0;
    //if ($("#select_projectID").val() == 0 && $("#select_status").val() == 0 && $("#select_country").val() == 0 && $("#select_customer_name").val() == 0 && $("#select_market_area").val() == 0) {
    //    url = "projectManagement/getProjectByFilters";
    //} else {
    //    url = "projectManagement/getProjectByFilters?" + "projectID=" + project.projectID + "&Status=" + project.Status + 
    //"&countryID=" + project.countryID + "&customerID=" + project.customerID + "&marketAreaID=" + project.marketAreaID;
    //}

    if ($("#select_projectID").val() != 0) {
        params += isFirstParam(firstParam) + "projectID=" + project.projectID
        firstParam = 1;
    } else if ($("#select_project_name").val() != 0) {
        params += isFirstParam(firstParam) + "projectID=" + project.projectNameID
        firstParam = 1;
    }

    if ($("#select_status").val() != 0) {
        params += isFirstParam(firstParam) + "status=" + project.Status
        firstParam = 1;
    }
    if (project.countryID != 0) {
        params += isFirstParam(firstParam) + "countryID=" + project.countryID
        firstParam = 1;
    }
    if ($("#select_customer_name").val() != 0) {
        params += isFirstParam(firstParam) + "customerID=" + project.customerID
        firstParam = 1;
    }
    if ($("#select_market_area").val() != 0) {
        params += isFirstParam(firstParam) + "marketAreaID=" + project.marketAreaID
        firstParam = 1;
    }
    if ($("#select_service_area").val() != 0) {
        params += isFirstParam(firstParam) + "serviceAreaID=" + project.serviceAreaID
        firstParam = 1;
    }
    

    var addParam = firstParam == 1 ? "?" + params : "";
    pwIsf.addLayer({ text: "Please wait ..." });
   
    $.isf.ajax({
        url: service_java_URL + "projectManagement/getProjectByFilters" + addParam,
        success: function (data) {
            pwIsf.removeLayer();
            $('#div_table').show();
            $('#div_table_message').hide();

            $.each(data, function (i, d) {
                d.startDate = fmtDateToDisplay(d.startDate);
                d.endDate = fmtDateToDisplay(d.endDate);
                // d.actionIcon = "<i style='cursor: pointer;'  class='fa fa-align-justify'  \" title='View Project'></i>&nbsp;&nbsp;";
                d.actionIcon = '<div style="display:flex"><a class="icon-view" title="View scope details" href="#"  >' + getIcon('view') + '</a>';
                // d.actionIcon += "<i style='cursor: pointer;'  class='fa fa-trash del_project'  title='Delete Project' \"></i>&nbsp;&nbsp;";
                if (d.status === "Closed") {
                    if (userRoleID != 5) {
                        d.actionIcon += '<a class="icon-delete lsp disabled" title="Delete Project" >' + getIcon('delete') + '</a>';
                        d.actionIcon += '<a class="icon-close lsp disabled" title="Close Project" >' + getIcon('close') + '</a></div > ';
                    }
                    
                }
                else {
                    if (userRoleID != 5) {
                        d.actionIcon += '<a class="icon-delete lsp" title="Delete Project" >' + getIcon('delete') + '</a>';
                        d.actionIcon += '<a class="icon-close lsp" title="Close Project" >' + getIcon('close') + '</a></div > ';
                    }
                    

                }
            });
            $("#table_project_search").append($('<tfoot><tr><th></th><th>Project ID</th><th>Project Name</th><th>Market Area</th><th>Country</th><th>Status</th><th>Start Date</th><th>End Date</th><th>Created By</th><th>PM/SPM</th></tr></tfoot>'));
            oTable = $('#table_project_search').DataTable({
                searching: true,
                responsive: true,
                //retrieve: true,
                "pageLength": 10,
                colReorder: true,
                dom: 'Bfrtip',
                order: [1],
                buttons: [                  
                        'colvis', 'excelHtml5'
                   ],
                "data": data,
                "destroy": true,
                "columns": [
                    {
                        "title": "Action",
                        "targets": 'no-sort',
                        "orderable": false,
                        "searchable": false,
                        "data": "actionIcon",
                    },
                    {
                        "title": "Project ID",
                        "data": "projectID"
                    },
                    {
                        "title": "Project Name",
                        "data": "projectName"
                    },
                    {
                        "title": "Market Area",
                        "data": "marketAreaName"
                    },
                    {
                        "title": "Country",
                        "data": "countryName"
                    },
                    {
                        "title": "Status",
                        "data": "status"
                    },
                    {
                        "title": "Start Date",
                        "data": "startDate"
                    },
                    {
                        "title": "End Date",
                        "data": "endDate"
                    },
                    {
                        "title": "Created By",
                        "data": "createdBy"
                    },
                    {
                        "title": "PM/SPM",
                        "data": "projectCreator"
                    }
                    
                ],
                initComplete: function () {

                    $('#table_project_search tfoot th').each(function (i) {
                        var title = $('#table_project_search thead th').eq($(this).index()).text();
                        if(title!="Action")
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

                }

            });

            
            $('#table_project_search tfoot').insertAfter($('#table_project_search thead'));

            $('#table_project_search tbody').on('click', 'a.icon-view', function () {
                var data = oTable.row($(this).parents('tr')).data();
                localStorage.setItem("views_project_id", data.projectID);
                
                window.location.href = "../Project/ViewProject";

            });


        },
        error: function (xhr, status, statusText) {
            pwIsf.removeLayer();
            $('#div_table').hide();
            $('#div_table_message').show();
            console.log('An error occurred on : createProject' + xhr.error);
        }
    })


}




function getServiceArea() {


    $.isf.ajax({

        url: service_java_URL + "activityMaster/getServAreaDetails",
        success: function (data) {

            $.each(data, function (i, d) {
                $('#select_service_area').append('<option value="' + d.servAreaID + '">' + d.serviceArea + '</option>');
            })

        },
        error: function (xhr, status, statusText) {
            console.log('An error occurred on getProjectDetails: ' + xhr.error);
        }
    });

}








/*
function checkAll() {
   
    var select_projectID = document.getElementById("select_projectID").value;
    var select_status = document.getElementById("select_status").value;
    var select_project_name = document.getElementById("select_project_name").value;
    var select_country = document.getElementById("select_country").value;
    var select_customer_name = document.getElementById("select_customer_name").value;
    var select_market_area = document.getElementById("select_market_area").value;
    
  
    if (select_projectID == 0) {
        alert("Project ID");

    } else {
        if (select_project_name == 0) {

            alert("project name");
        }
        else {
            if (select_market_area == 0) {
                alert("market area");
            }
            else {
                if (select_country == 0) {
                    alert("country");
                }
                else {
                    if (select_customer_name == 0) {
                        alert("customer name");
                    }
                    else {
                        if (select_status == 0) {
                            alert("status");
                        }
                        else {
                            getProjectByFilters(select_projectID, select_project_name, select_market_area, select_country, select_customer_name, select_status);
                        }
                    }
                }
            }
        }
    }
}
*/