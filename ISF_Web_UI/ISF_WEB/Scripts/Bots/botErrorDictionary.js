$(document).ready(function () {
    $('#errType').prop("disabled", true);
    $('#errMsg').prop("disabled", true);   
    selectSource();
    $("#extRef").select2({
        placeholder: "Select External Reference",
        selectOnClose: true
    });  
   
    ShowErrorDetails(0);
    const profileId = JSON.parse(ActiveProfileSession).accessProfileID;
    if (profileId !== 41) {
        $("#divAddError").css("visibility", "hidden");
        $("#divAddError").hide();
        $("#divAddError").html('');
	}
});




function selectSource() {
    pwIsf.addLayer({ text: 'Please wait ...' });
    $("#extRef").find('option').not(':first').remove();
    $.isf.ajax({
  
       url: service_java_URL + "externalInterface/getAllExternalSource?pageCounter="+true,
        async: false,
        success: function (data) {
            $('#extRef').append('<option value="' + 0 + '">All</option>');
            $.each(data, function (i, d) {    
                $('#extRef').append('<option value="' + d.SourceID + '">' + d.SourceName + '</option>')
            });
            pwIsf.removeLayer();
        },
        error: function (xhr, status, statusText) {
            console.log('');
        }
    });
}


function enableTextFields() {
   
    var extSourceName = $("#extRef option:selected").text();
    var extSourceId = $("#extRef option:selected").val();
    if (extSourceName !== "" && extSourceName !== null && extSourceName !== undefined && extSourceId != 0) {
        $('#errType').prop("disabled", false);
        $('#errMsg').prop("disabled", false);
    }
    else {
        
        $('#errType').prop("disabled", true);
        $('#errMsg').prop("disabled", true);
    }
   
    ShowErrorDetails(extSourceId);
}

function AddError() {
 
    var extSourceName = $("#extRef option:selected").text();
    var extSourceId = $("#extRef option:selected").val();
    var errorType = $('#errType').val();
    var errorMessage = $('#errMsg').val();
    if (extSourceName !== "" && extSourceName !== null && extSourceName !== undefined && extSourceId != 0 && errorType !== '' && errorMessage !== '') {
        var errorDetails = new Object();
        errorDetails.sourceID = extSourceId;
        errorDetails.errorType = errorType;
        errorDetails.errorMessage = errorMessage;

        pwIsf.confirm({
            title: 'Add Error', msg: 'Are you sure about Adding Error?',
            'buttons': {
                'Yes': {
                    'action': function () {
                        addNewErrorCode(errorDetails);
                    }
                },
                'No': {
                    'action': function () {
                        pwIsf.removeLayer();
                    }
                },
            }
        });
    }
    else {

        pwIsf.alert({ msg: "Please fill all the mandatory Fields", type: "warning" });

    }
}


function ShowErrorDetails(extSourceId) {
    if ($.fn.dataTable.isDataTable("#botErrorDictionaryTable")) {
        oTable.destroy();
        $('#botErrorDictionaryTable').empty();
    }
    var ServiceUrl = service_java_URL + "rpaController/getAllErrorDetails";
   if (extSourceId!=0)
    {
        var ServiceUrl = service_java_URL + "rpaController/getAllErrorDetails?sourceID=" + extSourceId;
    }
    // To add header to Datatable
    $('#botErrorDictionaryTable').html("");
    $("#botErrorDictionaryTable").append($('<tfoot><tr><th>External Reference</th><th>Error Code</th><th>Error Type</th><th>Error Message</th></tr></tfoot>'));
    oTable = $('#botErrorDictionaryTable').DataTable({
       "searching": true, //to enable searching in entire Data table
        "processing": true, //paging
        "serverSide": true, //paging
        "responsive": true,
        "ajax": {
            "headers": commonHeadersforAllAjaxCall,
            "url": ServiceUrl,
            "type": "POST",
            "complete": function (d) {
                $('#div_table').show();    
            },
            error: function (xhr, status, statusText) {
                $('#div_table').hide();
            }
        },
        "pageLength": 10, //length of records
        "destroy": true,
        colReorder: true,
        order: [[1, "desc"]],
        dom: 'Brtip',
        buttons: [
            //for column visibility
            'colvis', {
                text: 'Excel',
                action: function () {
                    downloadErrorDictionaryFile(extSourceId);
                }
            }],

        "columns": [
            
            {
                "title": "External Reference",
                "data": "externalReference",             
                "searchable": true,
                "width":"150px"
            },

            {
                "title": "Error Code",
                "data": "errorCode",
                "searchable": true,
                "width": "100px"
            },
            {
                "title": "Error Type",
                "data": "errorType",
                "searchable": true,
                "className": "errTypeStyling",
              
                "render": function (data, type, row, meta) {

                    let rd = escapeHtml(row.errorType);
                    let rdesc = '<span text-overflow="ellipsis" title="' + rd + '">' + rd + '</span>';                 
                    return rdesc;
                }
            },
            {
                "title": "Error Message",
                "data": "errorMessage",
                "className": "ruleDescStyling",
  
                "searchable": true,
                "render": function (data, type, row, meta) {
                    let rd = escapeHtml(row.errorMessage);
                    let rdesc = '<span text-overflow="ellipsis" title="' + rd + '">' + rd + '</span>';
                    return rdesc;
                }

            }
            
        ],
        initComplete: function () {
            addFieldsInErrorPageFooter();  // To append Search in footer
            var api = this.api();
            colSearchInErrorPage(api);   // For Column Search
        }
    });
    $('#botErrorDictionaryTable tfoot').insertAfter($('#botErrorDictionaryTable thead'));
    oTable.draw();
}


// implementation of search row
function addFieldsInErrorPageFooter() {
    $('#botErrorDictionaryTable tfoot th').each(function (i) {
        var title = $('#botErrorDictionaryTable thead th').eq($(this).index()).text();
        $(this).html('<input type="text" class="form-control" style="font-size:12px;"  placeholder="Search ' + title + '" data-index="' + i + '" />');
        
    });
}

// column Search Implementation
function colSearchInErrorPage(api) {
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


function downloadErrorDictionaryFile(sourceId) {
    let url = `${service_java_URL_VM}activityMaster/downloadErrorDictionaryFile?sourceID=${sourceId}`;
    let fileDownloadUrl;
    fileDownloadUrl = UiRootDir + "/data/GetFileFromApi?apiUrl=" + url;
    window.location.href = fileDownloadUrl;
}



// add new error code API call

function addNewErrorCode(errorDetails) {
    var errorDetail = new Object(); 
    errorDetail.sourceID = errorDetails.sourceID;
    errorDetail.errorType = errorDetails.errorType;
    errorDetail.errorMessage = errorDetails.errorMessage;
    errorDetail.createdBy = signumGlobal;
    pwIsf.addLayer({ text: "Please wait" });
    $.isf.ajax({
        url: service_java_URL + "rpaController/addErrorDetail",
        context: this,
        crossdomain: true,
        processData: true,
        contentType: 'application/json',
        type: 'POST',
        data: JSON.stringify(errorDetail),
        xhrFields: {
            withCredentials: false
        },
        success: AjaxSucceeded,
        error: AjaxFailed,
        complete: function () {
        pwIsf.removeLayer();
        }
    });
    function AjaxSucceeded(data, textStatus) {
        if (data.isValidationFailed === false) {
            pwIsf.alert({ msg: data.formMessages[0], type: 'success', autoClose: 2 });
            $('#errType').val('');
            $('#errMsg').val('');
            ShowErrorDetails(errorDetails.sourceID);
        }
        else {
            pwIsf.alert({ msg: data.formErrors[0], type: "error", autoClose: 2 });
        }
        
    }
    function AjaxFailed(xhr, status, statusText) {
        pwIsf.alert({ msg: 'Error in API, please contact ISF support.', type: 'warning' });

    }
}


