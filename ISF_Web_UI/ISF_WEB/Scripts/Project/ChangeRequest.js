var tableCM = "";
var params = new Object();
var defaultDate, start, servAreaID;
$(document).ready(function () {
    loadChangeListPM();
});

function loadChangeListPM() {


    $('#TbodyCMDetails tr').remove();
    pwIsf.addLayer({ text: C_LOADDER_MSG });
    $.isf.ajax({
        url: service_java_URL + "cRManagement/getCRDetails/pm/" + signumGlobal,
        success: function (data) {
            pwIsf.removeLayer();
            if (data.length > 0) {
                $("#div_CRbtns").show();
            }
                ConfigDataTableCR(data);
        },
        error: function (err) {
            console.log("An Error occured during load change request");
        }
    });
}

function createTableHTML(data) {
    const emptyRowData = '<td>-</td>';
    $("#dtChangeListPM tbody").html("");
    if (data.length == 0) {
        $("#dtChangeListPM_tfoot").hide();
    }
    for (let item of data) {
        var tr = '<tr>';
        tr += '<td><label><input id="CRapproveResource" type="checkbox" name="CRapproveResource" class="checkBoxClass" value="' + item.ResourcePositionID + '"></label></td>';
        tr += '<td><textarea class="textAreaClass" id="commentsApprovalCR' + item.ResourcePositionID + '" disabled></textarea></td>';
        tr += '<td>' + item.crID + '</td>';
        tr += '<td>' + item.status + '</td>';
        tr += '<td>' + item.projectID + '</td>';
        tr += '<td>' + item.resourceRequestID + '</td>';
        tr += '<td>' + item.resourcePositionID + '</td>';
        if (item.workEffortIdExisting == null) {
            tr += emptyRowData;
        }
        else {
            tr += '<td>' + item.workEffortIdExisting + '</td>';
        }       
        tr += '<td>' + item.workEffortIdProposed + '</td>';
        if (item.currentSignumID == null) {
            tr += emptyRowData;
        }
        else {
            tr += '<td><a href="#" style="color:blue;" onclick="getEmployeeDetailsBySignum(\'' + item.currentSignumID + '\');">' + item.currentSignumID + '</a></td>';
        }
        tr += '<td><a href="#" style="color:blue;" onclick="getEmployeeDetailsBySignum(\'' + item.proposedSignumID + '\');">' + item.proposedSignumID + '</a></td>';     
        if (item.currentStartDate == null) {
            tr += emptyRowData;
        }
        else {
            tr += '<td>' + item.currentStartDate + '</td>';
        }
        if (item.currentEndDate == null) {
            tr += emptyRowData;
        }
        else {
            tr += '<td>' + item.currentEndDate + '</td>';
        }
        tr += '<td>' + item.proposedStartDate + '</td>';
        tr += '<td>' + item.proposedEndDate + '</td>';
        tr += '<td>' + item.previouscomment + '</td>';
        tr += '</tr>';
        $("#dtChangeListPM tbody").append(tr);

        $(".checkBoxClass").on('change', function () {

            if (!$(this).prop("checked")) {
                $(this).closest('tr').find('textarea').prop("disabled", true);
            }
            else {
                $(this).closest('tr').find('textarea').prop("disabled", false);
            }
        });
    }
    //Checking all boxes
    $("#CMCheckAll").click(function () {
        $(".checkBoxClass").prop('checked', $(this).prop('checked'));
    });

    $(".checkBoxClass").on('change', function () {

        if (!$(this).prop("checked")) {
            $("#CMCheckAll").prop("checked", false);
        }

    });
    //Checking all boxes

}

function convert(str) {
    var date = new Date(str),
        mnth = ("0" + (date.getMonth() + 1)).slice(-2),
        day = ("0" + date.getDate()).slice(-2);
    return [date.getFullYear(), mnth, day].join("-");
}

function ConfigDataTableCR(data) {

    if ($.fn.dataTable.isDataTable('#dtChangeListPM')) {
        tableCM.destroy();
        $('#dtChangeListPM tbody').html('');
    }
    createTableHTML(data)

    tableCM = $("#dtChangeListPM").DataTable({
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
        'excel',
        ],
        columnDefs: [{
            "targets": [0],
            "orderable": false
        }],
        initComplete: function () {

            $('#dtChangeListPM tfoot th').each(function (i) {
                var title = $('#dtChangeListPM thead th').eq($(this).index()).text();
                if (title !== 'SelectAll' && title !== 'Comments') {
                    $(this).html('<input type="text" class="form-control" style="font-size:12px;"  placeholder="Search ' + title + '" data-index="' + i + '" />');
                }
                if (title === "SelectAll") {
                    $(this).html('<input type="checkbox" value="" id="CMCheckAll"/>');
                }
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
            //Empty block
        }

    });
   
    $("#CMCheckAll").click(function () {
        $(".checkBoxClass").prop('checked', $(this).prop('checked'));
    });
    $(".checkBoxClass").on('change', function () {
        if (!$(this).prop("checked")) {
            $("#CMCheckAll").prop("checked", false);
        }
    });
    $('#dtChangeListPM tfoot').insertAfter($('#dtChangeListPM thead'));


}

function onAccept() {
    saveAccept();
}

function onReject() {

    saveReject();
}

function onSendProposal() {
    $('input[name="chkChangeManagement"]:checked').each(function () {

        this.closest('tr').remove();
    });
}

function saveAccept() {
    var paramlist = [];
    $('input[name="CRapproveResource"]:checked').each(function () {
        let param = new Object();
        param.cRID = this.closest('tr').cells[2].innerText;
        param.proposedStartDate = this.closest('tr').cells[13].innerText;
        param.currentSignumID = signumGlobal;
        param.proposedEndDate = this.closest('tr').cells[14].innerText;

        param.comments = signumGlobal + " : " + $(this).closest('tr').find('.textAreaClass').val();
        paramlist.push(param);
    });

    var dataparamAccept = JSON.stringify(paramlist);
    pwIsf.addLayer({ text: C_LOADDER_MSG });
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
            pwIsf.alert({ msg: "Accepted successfully", type: "success" });
            loadChangeListPM()

        },
        error: function (xhr, status, statusText) {
            alert("Unsucessful");
            console.log('An error occurred : ' + xhr.responseText);
        }
    });

    
}

function saveReject() {
    var paramlist = [];
    
    $('input[name="CRapproveResource"]:checked').each(function () {
        
        const param = new Object();
        param.cRID = this.closest('tr').cells[2].innerText;
        param.proposedStartDate = this.closest('tr').cells[13].innerText;
        param.currentSignumID = signumGlobal;
        param.proposedEndDate = this.closest('tr').cells[14].innerText;

        param.comments = signumGlobal + ': ' + this.closest('tr').cells[1].lastChild.value;
        paramlist.push(param);
        
    });
    
    var dataparamReject = JSON.stringify(paramlist);
    pwIsf.addLayer({ text: C_LOADDER_MSG });
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
            pwIsf.alert({ msg: "Rejected successfully", type: "success" });
            loadChangeListPM();

        },
        error: function (xhr, status, statusText) {
            alert("Unsucessful");
            console.log('An error occurred : ' + xhr.responseText);
        }
    })


}

function Removedata()
{

    $('input[name="CRapproveResource"]:checked').each(function () {
        this.closest('tr').remove();
    });
}