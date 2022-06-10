var tablePOC = "";

function loadData()
{
    pwIsf.addLayer({ text: "Please wait ..." });
    getPOCSignumFilter();
    //getPOCSignum();
    getMarketAreasPOC();
    getDomainSubdomainpoc();
    getserviceAreapoc();
    getDomainPocList();

}

function getPOCSignumFilter() {
    $("#selSignum").autocomplete({

        source: function (request, response) {
            $.isf.ajax({

                url: service_java_URL + "activityMaster/getEmployeesByFilter",
                type: "POST",

                data: {
                    term: request.term
                },
                success: function (data) {
                    var result = [];
                    if (data.length == 0) {

                        showErrorMsg('Signum-Required', 'Please enter a valid Signum.');
                        $('#selSignum').val("");
                        $('#selSignum').focus();
                        response(result);

                    }
                    else {
                        hideErrorMsg('Signum-Required');

                        $("#selSignum").autocomplete().addClass("ui-autocomplete-loading");

                        $.each(data, function (i, d) {
                            result.push({
                                "label": d.signum + "/" + d.employeeName,
                                "value": d.signum

                            });


                        });
                        response(result);
                        $("#selSignum").autocomplete().removeClass("ui-autocomplete-loading");
                    }

                }
            });
        },

        minLength: 3

    });
    $("#selSignum").autocomplete("widget").addClass("fixedHeight");


}

function getMarketAreasPOC() {

    $('#select_market_area_poc')
       .find('option')
       .remove();
    $.isf.ajax({
        //url: service_DotNET_URL + "MarketAreas",
        url: service_java_URL +"projectManagement/getMarketAreas",
        success: function (data) {

            $('#select_market_area_poc').append('<option value="0">Select Market Area</option>');
            $.each(data, function (i, d) {
                $('#select_market_area_poc').append('<option value="' + d.MarketAreaID + '">' + d.MarketAreaName + '</option>');
            });

            $('#select_market_area_poc').select2('val', '0');
           
        },
        error: function (xhr, status, statusText) {
            alert("Fail " + xhr.responseText);
            alert("status " + xhr.status);
            console.log('An error occurred');
        }
    });

}


function getDomainSubdomainpoc()
{
    $('#select_domainpoc')
      .find('option')
      .remove();
    $.isf.ajax({
        url: service_java_URL + "activityMaster/getDomainDetails",
        success: function (data)
        {
            $('#select_domainpoc').append('<option value="0">---Select Domain/Subdomain---</option>');
            $.each(data, function (i, d) {
                $('#select_domainpoc').append('<option value="' + d.domainID + '">' + d.domain + '</option>');
            });
            
            $('#select_domainpoc').select2('val','0');
        },
        error: function (xhr, status, statusText) {
            alert("Fail get domain " + xhr.responseText);
          }
      });

}

function getserviceAreapoc() {
    $('#select_servicepoc')
     .find('option')
     .remove();
    $.isf.ajax({

        url: service_java_URL + "activityMaster/getServAreaDetails",
        success: function (data) {
            $('#select_servicepoc').append('<option value="0">--Select Service Area--</option>');
            $.each(data, function (i, d) {
                $('#select_servicepoc').append('<option value="' + d.servAreaID + '">' + d.serviceArea + '</option>');
            });
            $('#select_servicepoc').select2('val', '0');
        },
        error: function (xhr, status, statusText) {
            alert("Fail get service area " + xhr.responseText);
        }

    });


}


function getPOCSignum() {

    $('#selSignum').append('<option value="0">Loading Signum.........</option>');
    
   
    $.isf.ajax({
     
        url: service_java_URL + "activityMaster/getEmployeeDetails",

        success: function (data) {
            $('#selSignum').empty();
            $('#selSignum').append('<option value="0">---Select Signum---</option>');
            var html = "";
            $.each(data, function (i, d) {
                $('#selSignum').append('<option value="' + d.signum + '">' + d.signum + '</option>');
            });
            $('#selSignum').select2('val', '0');
        },
        error: function (xhr, status, statusText) {
            alert("Fail Get Signum " + xhr.responseText);
            alert("status " + xhr.status);
         
        }
    });
}

function addPoc() {
   
            var marketarea = $("#select_market_area_poc").val();

            var domain = $("#select_domainpoc").val();
            var servicearea = $("#select_servicepoc").val();
            var signum = $("#selSignum").val();


            if (marketarea != 0 && servicearea != 0 && domain != 0 && signum!=0)
                {
            var poc = new Object();
            poc.marketAreaID = marketarea;
            poc.domainID = domain;
            poc.servAreaID = servicearea;
            poc.spoc = signum;
            poc.spocLevel = "1";
            poc.createdBy = signumGlobal;

            var datapoc = JSON.stringify(poc);

                $.isf.ajax({
                    url: service_java_URL + "activityMaster/addDomainSpoc",
                    context: this,
                    crossdomain: true,
                    processData: true,
                    contentType: 'application/json',
                    type: 'POST',
                    data: datapoc,
                    xhrFields: {
                        withCredentials: false
                    },
                    success: function (data) {

                        var result = $(data).find("string").text();
                        alert("POC Added Successfully");
                       
                    },
                    error: function (xhr, status, statusText) {
                       
                        alert("Error: " + xhr.responseJSON.errorMessage);
                        
                    }
                });
       }

                  else 
                   {
                     alert("Please select all fields");
                   }

   
}

function ValidateData()
{

    var OK = true;
   
}

function refresh()
{
  
   loadData();
 
}

function getDomainPocList()
{
    var ServiceUrl = service_java_URL + "activityMaster/getAllDomainSpoc";

    $.isf.ajax({
        url: ServiceUrl,
        success: function (data) {
            congifurePOCDatatable(data);
        },
        error: function (xhr, status, statusText) {
           
            alert(xhr.responseJSON.errorMessage);
        }
    });


}

function congifurePOCDatatable(data)
{

    if ($.fn.dataTable.isDataTable('#dtpoclist')) {
        tablePOC.destroy();
        $('#dtpoclist').empty();

    }

    createGridPOC(data);


    tablePOC = $("#dtpoclist").DataTable({
        //"order": [[targetCol, 'asc']],
        "displayLength": 10,
        "searching": true,
        colReorder: true,
        responsive: true,
        'order': [1, 'asc'],

        dom: 'Bfrtip',
        buttons: [
         'colvis', 'excelHtml5'
        ],
        initComplete: function () {

            $('#dtpoclist tfoot th').each(function (i) {
                var title = $('#dtpoclist thead th').eq($(this).index()).text();
                if (title != "Action")
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
    $('#dtpoclist tfoot').insertAfter($('#dtpoclist thead'));

}


function createGridPOC(data)
{

    var tr = '<tr>';
    var trfoot = '<tr>';
    var td = '';
    
    var tdfoot = "";

    for (keys in data[0]) {

     if (keys == "domainSPOCID") {
                
         td = td + '<th hidden="hidden"><label style="padding-right: 5px;">' + keys + '</label></th>';
    }
        td = td + "<th><label style='padding-right: 5px;'>" + keys + "</label></th>";
        tdfoot = tdfoot + "<th>" + keys + "</th>";
    }

    tr = tr + td;
    trfoot = trfoot + tdfoot + "</tr>";
    $("#dtpoclist").html('<thead></thead><tbody></tbody><tfoot></tfoot>');
    $("#dtpoclist thead").html(tr);
    $("#dtpoclist tbody").html("");
    $("#dtpoclist tfoot").html(trfoot);

    for (var i = 0; i < data.length; i++) {
        td = '';
        tr = '<tr>';
        for (keys in data[i]) {

            if (keys == "CreatedDate") {
                var dt = new Date(data[i][keys]);
                td = td + '<td>' + dt.getUTCFullYear() + '-' + (dt.getMonth() + 1) + '-' + dt.getDate() + '</td>';
            }
             else if (keys == "domainSPOCID") {
                
                td = td + '<td hidden="hidden">'+data[i][keys] +'</td>';
            }
            else {
                td = td + '<td>' + data[i][keys] + '</td>';
            }
        }
        tr = tr + td + '</tr>';
        $("#dtpoclist tbody").append(tr);
        pwIsf.removeLayer();
    }


}

