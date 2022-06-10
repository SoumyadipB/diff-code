var tableCM;
var params = new Object();
var flag = 0;

function loadChangeList() {    
  
    

    $("#btnAccept").css("display", "none");
    $("#btnReject").css("display", "none");
   
    $.isf.ajax({
        url: service_java_URL + "cRManagement/getCRDetails/fm/" + signumGlobal,
        success: function (data) {
            var html = "";
            if(data.length>0)
                ConfigDataTableCM(data);            
        },
        error: function (err) {
            console.log("An Error occured during load change request");
        }
    });
}

function createTableHTML(data) {
    
      
    var tr = '<tr>';
    var trfoot = '<tr>';
    var td = "";
    var tdfoot = "";
    td = td + '<th>SelectAll</th>';
    tdfoot = tdfoot + '<th></th>';
    for (keys in data[0]) {        
        td = td + "<th>" + keys + "</th>";
        tdfoot = tdfoot + "<th>" + keys + "</th>";
    }

    tr = tr + td + "</tr>";
    trfoot = trfoot + tdfoot + "</tr>";
    $("#dtChangeList").html('<thead></thead><tbody></tbody><tfoot></tfoot>');
    $("#dtChangeList thead").html(tr);
    $("#dtChangeList tbody").html("");
    $("#dtChangeList tfoot").html(trfoot);

    for (var i = 0; i < data.length; i++) {
        var keyValue;
        var txtid = 'txt' + i;
        var chkvalue = i + ',' + signumGlobal;// i + ',' + dPS + ',' + dPE + ',' + signumGlobal;
        td = '<td><input type="checkbox" name="chkChangeManagement" class="checkBoxClass" value="' + chkvalue + '"/></td>';
        tr = "<tr>";
        for (keys in data[i]) {
            keyValue = data[i][keys];
            if (data[i][keys] == null) {
                 keyValue = "-"
            }    
            if(keys == "comments"){
                td = td + '<td><textarea class="textAreaClass" rows="3"></textarea></td>';
            } else if ((keys.toLowerCase() == "currentsignumid" || keys.toLowerCase() == "proposedsignumid" || keys.toLowerCase() == "requestedby") && keyValue != "-") {
                //td = td + '<td><a href="#" style="color:blue;" onclick="getEmployeeDetailsBySignum(\'' + data[i][keys] + '\')">' + data[i][keys] + '</a></td>';
                td = td + '<td><a href="#" style="color:blue;" onclick="getEmployeeDetailsBySignum(\'' + keyValue + '\')">' + keyValue + '</a></td>';
            }else{
                //td = td + '<td>' + data[i][keys] + '</td>';
                td = td + '<td>' + keyValue + '</td>';
            }
        }
        tr = tr + td + "</tr>";
        $("#dtChangeList tbody").append(tr);
    }


}

function convert(str) {
    var date = new Date(str),
        mnth = ("0" + (date.getMonth() + 1)).slice(-2),
        day = ("0" + date.getDate()).slice(-2);
    return [date.getFullYear(), mnth, day].join("-");
}

function ConfigDataTableCM( data) {

    if ($.fn.dataTable.isDataTable('#dtChangeList')) {
        tableCM.destroy();
        $('#dtChangeList').empty();

    }
    createTableHTML(data)
   
    tableCM = $("#dtChangeList").DataTable({
        //"order": [[targetCol, 'asc']],
        "displayLength": 10,
        "searching": true,
        colReorder: true,
        responsive: true,      
        'order': [1, 'asc'],             
        dom: 'Bfrtip',
        buttons: [{
            extend: 'colvis',
            columns: ':gt(0)'
        },
        'excel'
        ],
        columnDefs: [{
            "targets": [0],
            "orderable": false
        }],
        initComplete: function () {

            $('#dtChangeList tfoot th').each(function (i) {
                var title = $('#dtChangeList thead th').eq($(this).index()).text();
                if (title != 'SelectAll' && title != 'Comments')
                    $(this).html('<input type="text" class="form-control" style="font-size:12px;"  placeholder="Search ' + title + '" data-index="' + i + '" />');
                if (title == "SelectAll")
                    $(this).html('<input type="checkbox" value="" id="CMCheckAll"/>');
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
  //  tableCM.columns([10, 11, 12, 13, 14]).visible(false, false);
  //  tableCM.columns.adjust().draw(false); // adjust column sizing and redraw

    $("#CMCheckAll").click(function () {
        $(".checkBoxClass").prop('checked', $(this).prop('checked'));
        flag = tableCM.rows().nodes().to$().find("input[type='checkbox']:checked").length;

        if (flag > 0) {
            $("#btnAccept").css("display", "block");
            $("#btnReject").css("display", "block");
        }
        else {
            $("#btnAccept").css("display", "none");
            $("#btnReject").css("display", "none");

        }
      
    });

    //$(".checkBoxClass").on('change', function () {
    tableCM.rows().nodes().to$().find(".checkBoxClass").on('change', function () {
        
        if (!$(this).prop("checked")) {
            $("#CMCheckAll").prop("checked", false);
        }

        
    });

    //$(".checkBoxClass").on('click', function () {
    tableCM.rows().nodes().to$().find(".checkBoxClass").on('click', function () {
        
        flag = tableCM.rows().nodes().to$().find("input[type='checkbox']:checked").length;


        //flag = $('#dtChangeList :input[type="checkbox"]:checked').length;

        if (flag > 0) {
            $("#btnAccept").css("display", "block");
            $("#btnReject").css("display", "block");
        }
        else
        {
            $("#btnAccept").css("display", "none");
            $("#btnReject").css("display", "none");

        }
    });
    $('#dtChangeList tfoot').insertAfter($('#dtChangeList thead'));
    
}

function createGridCM(data) {
  
    var tr = "";
    var td = "";

    for (var i = 0; i < data.length; i++) {
        //<input type="checkbox" id="chk_"'+i+'>
        td = '<td><input type="checkbox" name="chkForAllRows" value="' + i + '"></td>';
        tr = "<tr>";
        for (keys in data[i]) {

            if (keys == "requestedOn" || keys == "currentStartDate" || keys == "proposedStartDate" || keys == "currentEndDate" || keys == "proposedEndDate" || keys == "actionTakenOn") {
               
                var dt = new Date(data[i][keys]);
                td = td + '<td><input id="change_date_' + i + '" type="text" class="form-control dateForAllRows" value="' + dt.getUTCFullYear() + "-" + (dt.getMonth() + 1) + "-" + dt.getDate() + '" ></td>';
               
            }
            else {
                td = td + "<td>" + data[i][keys] + "</td>";
            }
        }
        //td = td + '<td><a href="#">ChangeReq</a></td>';
        tr = tr + td + "</tr>";
        $("#dtChangeList tbody").append(tr);

        $('input').filter('.dateForAllRows').datepicker({
            format: 'yyyy-mm-dd',
            autoclose: true,
        });
    }
        }

function onAccept()
{
    saveAccept();
}


function onReject()
{
    
    saveReject();
}


function onSendProposal()
{
  //  alert('on send proposal');
    $('input[name="chkChangeManagement"]:checked').each(function () {
        
        this.closest('tr').remove();
    });
}

function saveAccept()
{
    //var paramlist = [];
    var paramlist = [];
    $('input[name="chkChangeManagement"]:checked').each(function () {
        // alert(this.value);
        param = new Object();
        param.cRID = this.closest('tr').cells[8].innerText;
        param.proposedStartDate = this.closest('tr').cells[4].innerText;
        param.currentSignumID = signumGlobal;
        param.proposedEndDate = this.closest('tr').cells[6].innerText;
       
       param.comments = signumGlobal +" : "+ $(this).closest('tr').find('.textAreaClass').val();//': ' + this.closest('tr').cells[14].innerText;
        paramlist.push(param);
       

    });


    if (paramlist.length != 0) {
        var dataparamAccept = JSON.stringify(paramlist);

        //  alert("Accept Parameters:" + dataparamAccept);

        $.isf.ajax({
            url: service_java_URL + "cRManagement/acceptCr",
            context: this,
            crossdomain: true,
            processData: true,
            contentType: 'application/json',
            type: 'POST',
            data: dataparamAccept,
            xhrFields: {
                withCredentials: false
            },
            success: function (data) {

                alert("Accepted successfully");
                //loadChangeList();
                location.reload();

            },
            error: function (xhr, status, statusText) {
                alert("Unsucessful");
                console.log('An error occurred : ' + xhr.responseText);
            }
        });
    }
    else
        alert('Kindly select atleast one Record for Approval');


  }

function saveReject(paramlist) {
    var paramlist = [];
   // var thisvar;
    $('input[name="chkChangeManagement"]:checked').each(function () {
        // alert(this.value);
        //alert(this.closest('tr'));
       // thisvar = this;
        param = new Object();
        param.cRID = this.closest('tr').cells[8].innerText;
        param.currentSignumID = signumGlobal;
        param.comments = signumGlobal + ': ' + $(this).closest('tr').find('.textAreaClass').val();
        param.proposedStartDate = this.closest('tr').cells[4].innerText;
        param.proposedEndDate = this.closest('tr').cells[6].innerText;

        paramlist.push(param);
        
        //saveReject(param);

    });


    if (paramlist.length != 0) {
        var dataparamReject = JSON.stringify(paramlist);
        //alert("Reject Params:"+dataparamReject);

        $.isf.ajax({
            url: service_java_URL + "cRManagement/rejectCr",
            context: this,
            crossdomain: true,
            processData: true,
            contentType: 'application/json',
            type: 'POST',
            data: dataparamReject,
            xhrFields: {
                withCredentials: false
            },
            success: function (data) {

                //alert('on reject');
                //  thisvar.closest('tr').remove();
                //var result = (new XMLSerializer()).serializeToString(data);
                alert("Rejected Successfully");
                //loadChangeList();
                location.reload();
            },
            error: function (xhr, status, statusText) {
                alert("Unsucessful");
                console.log('An error occurred : ' + xhr.responseText);
            }
        });
    }
    else {
        alert('Kindly select one record for Rejection');

    }

   
}