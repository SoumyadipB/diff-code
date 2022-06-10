//gets the project data for the delivery execution feedback
$(document).ready(function () {
    //$('#btnDownloadFeedback').hide();
    userRoleID = JSON.parse(ActiveProfileSession).roleID; 
    //for PM
    if (userRoleID == 1 || userRoleID == 4 ) {
       // $('#btnDownloadFeedback').hide();
        $("#table_DeliveryExecution_feedbackPM").show();
        $("#table_DeliveryExecution_feedback").hide();
        $("#content").css('width', '99%');
        $("#dexLegendNE").hide();
        $("#dexLegendNE").css("visibility", "hidden");
        $("#dexLegend").show();
        $("#dexLegend").css("visibility", "visible");

        $('#table_DeliveryExecution_feedbackPM').DataTable({
            "paging": true,
            "ordering": false,
            "bLengthChange": false,         
            "columnDefs": [
                {
                    "targets": [13],
                    "visible": false,
                    "searchable": false
                },
                {
                    "targets": [14],
                    "visible": false,
                    "searchable": false
                }
            ],
            dom: 'Bfrtip',
           
            buttons: [
                'colvis',
                {
                    extend: 'excelHtml5',     
                    titleAttr: 'Excel',
                    attr: {
                        id: 'btnDownloadFeedback'
                    },
                    action: function (e, dt, node, config) {
                        downloadFeedbackFiles();
                    }
                }
            ],
        
            initComplete: function () {
                $('#btnDownloadFeedback').hide();
               
                $('#table_DeliveryExecution_feedbackPM tfoot th').each(function (i) {
                    var title = $('#table_DeliveryExecution_feedbackPM thead th').eq($(this).index()).text();
                    if (title != "Action") {
                        $(this).html('<input type="text" class="form-control" style="font-size:12px;"  placeholder="Search ' + title + '" data-index="' + i + '" />');
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

            }
        });
        
        $("#table_DeliveryExecution_feedbackPM_length").css("display", "none");
        $("#table_DeliveryExecution_feedbackPM_filter").css("display", "none")
        $('#table_DeliveryExecution_feedbackPM tfoot').insertAfter($('#table_DeliveryExecution_feedbackPM thead'));
    }
    else {
        //$('#btnDownloadFeedback').hide();
  
        $("#table_DeliveryExecution_feedbackPM").hide();
        $("#table_DeliveryExecution_feedback").show();
        $("#dexLegend").hide();
        $("#dexLegend").css("visibility", "hidden");


        $('#table_DeliveryExecution_feedback').DataTable({
            "paging": true,
            "ordering": false,           
            "bLengthChange": false,
           
            dom: 'Bfrtip',
            buttons: [
                'colvis',
                {
                    extend: 'excelHtml5',
                   
                    titleAttr: 'Excel',
                    attr: {
                        id: 'btnDownloadFeedback'
                    },
                    action: function (e, dt, node, config) {
                        downloadFeedbackFiles();
                    }
                }
            ],

            initComplete: function () {
                $('#btnDownloadFeedback').hide();

                $('#table_DeliveryExecution_feedback tfoot th').each(function (i) {
                    var title = $('#table_DeliveryExecution_feedback thead th').eq($(this).index()).text();
                    if (title != "Action") {
                        $(this).html('<input type="text" class="form-control" style="font-size:12px;"  placeholder="Search ' + title + '" data-index="' + i + '" />');
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

            }
        });
       
        $("#table_DeliveryExecution_feedback_length").css("display", "none");
        $("#table_DeliveryExecution_feedback_filter").css("display", "none");
        $('#table_DeliveryExecution_feedback tfoot').insertAfter($('#table_DeliveryExecution_feedback thead'));
    }

    $('#workFlowForFeedback').select2({
        closeOnSelect: false,
        height: 30

    });

    pwIsf.addLayer({ text: "Please wait ..." });

    let serviceUrl = service_java_URL + "activityMaster/getProjectList"
    if (ApiProxy == true) {
        serviceUrl = service_java_URL + "activityMaster/getProjectList"
    }

    $.isf.ajax({
        url: serviceUrl,
        method: 'GET',
        headers: commonHeadersforAllAjaxCall,
        success: function (data) {
            pwIsf.removeLayer();
            for (var i = 0; i < data.length; i++) {
                $('#ProjectforFeedback').append('<option value=' + data[i]["projectID"] + '>' + data[i]["projectID"] + '-' +  data[i]["projectName"] + '</option>');
            }
        },
        error: function (xhr, status, statusText) {
            pwIsf.removeLayer();
            console.log('An error occurred');
        }
    });

  
    //$('#table_DeliveryExecution_feedback tfoot th').each(function () {
    //    var title = $(this).text();
    //    $(this).html('<input type="text" placeholder="Search ' + title + '" />');
    //});
});

//on change of project dropdown get the data of workflow based on project

function projectChange() {
    $('#btnDownloadFeedback').hide();
    $("#selectAllWF").prop("checked", false)
    $('#workFlowForFeedback').empty();
    $("#dexFed").empty();
    $("#table_DeliveryExecution_feedback_info").css("display", "none")
    $("#table_DeliveryExecution_feedbackPM_info").css("display", "none")

    pwIsf.addLayer({ text: "Please wait ..." });

    let serviceUrl = service_java_URL + "activityMaster/getWorkFlowList"
    if (ApiProxy == true) {
        serviceUrl = service_java_URL + "activityMaster/getWorkFlowList"
    }

    var obj = new Object();
    obj.projectID = $("#ProjectforFeedback").val()

    $.isf.ajax({
        url: serviceUrl,
        data: JSON.stringify(obj),
        dataType: 'json',
        contentType: "application/json; charset=utf-8",
        method: 'POST',
        success: function (data) {
           
            pwIsf.removeLayer();
            for (var j = 0; j < data.length; j++) {
                $('#workFlowForFeedback').append('<option value="' + data[j]['workFlowId'] + '" data-name="' + data[j]['subActivityFlowChartDefID']+  '">' + data[j]['workFlowName'] + '</option>');
            }

            if (data.length != 0) {
                $("#selectAllWF").removeAttr("disabled");
            }
            else {
                $("#selectAllWF").attr("disabled", true);
            }
        },
        error: function (xhr, status, statusText) {
            pwIsf.removeLayer();
            console.log('An error occurred');
        }
    });
}

// call on clicking on the duration filters buttons
function callOnDEFeedbackDuration(durationText) {

    $("a.divider").removeClass("active");    

    $('a.divider').each(function (e) { if ($(this).text() == durationText) { $(this).addClass('active'); } });

    let getDates = getStartDateAndEndDateFromSelectedDuration(durationText);
    let statusText = $('a.dividerStatus.active').text();
    let getStatus = getDEFeedbackFilter(statusText);
    let projectID = $("#ProjectforFeedback").val();
    let workFlowId = $("#workFlowForFeedback").val();
    let listOfFlowchartdefId = [];

    listOfFlowchartdefId = $("#workFlowForFeedback :selected").map(function (i, el) {
        return $(el).data("name");;
    }).get();

    if (getDates && statusText && getStatus && projectID && workFlowId && listOfFlowchartdefId) {

        if (userRoleID == 1 || userRoleID == 4) {
            getDeliveryExecutionFeedbackPM(projectID, getDates.sdate, getDates.edate, getStatus, workFlowId, listOfFlowchartdefId);
        }
        else {
            getDeliveryExecutionFeedback(projectID, getDates.sdate, getDates.edate, getStatus, workFlowId, listOfFlowchartdefId);
        }
    }
    else {
        pwIsf.alert({ msg: 'Please select mandatory fields', type: 'warning' });
    }
}

//call on feedback status filters buttons clicks
function callOnDEFeedbackStatus(el) {
    let statusText = el.innerText;
    $("a.dividerStatus").removeClass("active");
    $(el).addClass("active");

    let getStatus = getDEFeedbackFilter(statusText);
    let durationText = $('.filterHrefs a.divider.active').text();
    let getDates = getStartDateAndEndDateFromSelectedDuration(durationText);

    let projectID = $("#ProjectforFeedback").val();
    let workFlowId = $("#workFlowForFeedback").val();
    let listOfFlowchartdefId = [];
    listOfFlowchartdefId = $("#workFlowForFeedback :selected").map(function (i, el) {
        return $(el).data("name");;
    }).get();
    if (getDates && durationText && getStatus && projectID && workFlowId && listOfFlowchartdefId) {

        if (userRoleID == 1 || userRoleID == 4) {
            getDeliveryExecutionFeedbackPM(projectID, getDates.sdate, getDates.edate, getStatus, workFlowId, listOfFlowchartdefId);
        }
        else {
            getDeliveryExecutionFeedback(projectID, getDates.sdate, getDates.edate, getStatus, workFlowId, listOfFlowchartdefId);
        }

    }
    else {
        pwIsf.alert({ msg: 'Please select mandatory fields', type: 'warning' });
    }    
}

// returning start and end date in the specified format 
function getStartDateAndEndDateFromSelectedDuration(text, el) {
    let durationText = text;

    let startDate = '2020-01-01';
    //let endDate = 'NA';

    var today = new Date();
    //var tomorrow = new Date(today);
    //tomorrow.setDate(tomorrow.getDate() + 1);

    endDate = formatted_date(today);

    if (durationText.toLowerCase() != 'all') {

        let formatted_date = function (date) {
            var m = ("0" + (date.getMonth() + 1)).slice(-2); // in javascript month start from 0.
            var d = ("0" + date.getDate()).slice(-2); // add leading zero 
            var y = date.getFullYear();
            return y + '-' + m + '-' + d;
        };

        switch (durationText) {
            case "Today":
                var today = new Date();
                var tomorrow = new Date(today);
                tomorrow.setDate(tomorrow.getDate() + 0);
                startDate = formatted_date(today);
                endDate = formatted_date(tomorrow);
                break;

            case "This Week":
                var today = new Date();
                var day = today.getDay();
                var diff = today.getDate() - day + (day == 0 ? -6 : 1); // 0 for sunday
                var week_start_tstmp = today.setDate(diff);
                var week_start = new Date(week_start_tstmp);
                startDate = formatted_date(week_start);
                var week_end = new Date(week_start_tstmp);  // first day of week 
                week_end = new Date(week_end.setDate(week_end.getDate() + 6));
                endDate = formatted_date(week_end);
                break;
            case "Last Week":
                var today = new Date();
                var day = today.getDay();
                var diff = today.getDate() - day + (day == 0 ? -13 : -6);
                var week_start_tstmp = today.setDate(diff);
                var week_start = new Date(week_start_tstmp);
                startDate = formatted_date(week_start);
                var week_end = new Date(week_start_tstmp);  // first day of week 
                week_end = new Date(week_end.setDate(week_end.getDate() + 6));
                endDate = formatted_date(week_end);
                break;
            //case "Next Week":
            //    var today = new Date();
            //    var day = today.getDay();
            //    var diff = today.getDate() + (8 - (day == 0 ? 7 : day));
            //    var week_start_tstmp = today.setDate(diff);
            //    var week_start = new Date(week_start_tstmp);
            //    startDate = formatted_date(week_start);
            //    var week_end = new Date(week_start_tstmp);  // first day of week 
            //    week_end = new Date(week_end.setDate(week_end.getDate() + 5));
            //    endDate = formatted_date(week_end);
            //    break;
            case "This Month":
                var today = new Date();
                y = today.getFullYear(), m = today.getMonth();
                var firstDay = new Date(y, m, 1);
                var lastDay = new Date(y, m + 1, 0);
                startDate = formatted_date(firstDay);
                endDate = formatted_date(lastDay);
                break;
            case "Last Month":
                var today = new Date();
                y = today.getFullYear(), m = today.getMonth();
                var firstDay = new Date(y, m - 1, 1);
                var lastDay = new Date(y, m, 0);
                startDate = formatted_date(firstDay);
                endDate = formatted_date(lastDay);
                break;

            //case "Next Month":
            //    var today = new Date();
            //    y = today.getFullYear(), m = today.getMonth();
            //    var firstDay = new Date(y, m + 1, 1);
            //    var lastDay = new Date(y, m + 2, 0);
            //    startDate = formatted_date(firstDay);
            //    endDate = formatted_date(lastDay);
            //    break;

            //case "Next 3 Months":
            //    var today = new Date();
            //    y = today.getFullYear(), m = today.getMonth();
            //    var firstDay = new Date(y, m + 1, 1);
            //    var lastDay = new Date(y, m + 4, 0);
            //    startDate = formatted_date(firstDay);
            //    endDate = formatted_date(lastDay);
            //    break;

            case "Last 3 Months":
                var today = new Date();
                y = today.getFullYear(), m = today.getMonth();
                var firstDay = new Date(y, m - 2, 1);
                var lastDay = new Date(y, m + 1, 0);
                startDate = formatted_date(firstDay);
                endDate = formatted_date(lastDay);
                break;
        }
    }
    return { 'sdate': startDate, 'edate': endDate };  
}

// setting up the wo filter status text
function getDEFeedbackFilter(statusText) {

    let fbStatus = '';
    switch (statusText) {
        case "All":
            fbStatus = "All";
            break;
        case "New":
            fbStatus = "New";
            break;
        case "Feasible":
            fbStatus = "Feasible";
            break;
        case "On Hold":
            fbStatus = "On Hold";
            break;
        case "Not Feasible":
            fbStatus = "Not Feasible";
            break;
        case "Implemented":
            fbStatus = "Implemented";
            break;
        case "Not Applicable":
            fbStatus = "Not Applicable";
            break;
        case "Send Back To PM (I)":
            fbStatus = "Send Back To PM (I)";
            break;
        case "Send Back To PM (NF)":
            fbStatus = "Send Back To PM (NF)";
            break;
        case "Closed":
            fbStatus = "Closed";
            break;     
    }

    return fbStatus;

}


 //object to store data for Refresh
let dataForRefresh = {};



function setParameterForFeedback() { 


    let statusText = $('.filterHrefs a.dividerStatus.active').text();
    let getStatus = getDEFeedbackFilter(statusText);

    let durationText = $('.filterHrefs a.divider.active').text();
    let getDates = getStartDateAndEndDateFromSelectedDuration(durationText);


    let projectID = $("#ProjectforFeedback").val();
    let workFlowId = $("#workFlowForFeedback").val();
    let listOfFlowchartdefId = [];
    listOfFlowchartdefId =  $("#workFlowForFeedback :selected").map(function (i, el) {
        return $(el).data("name");;
    }).get();

    if (getDates && durationText && getStatus && projectID && workFlowId && listOfFlowchartdefId) {
        //1 :PM
        //4 :OM
        if (userRoleID == 1 || userRoleID == 4) {
            getDeliveryExecutionFeedbackPM(projectID, getDates.sdate, getDates.edate, getStatus, workFlowId, listOfFlowchartdefId);
            dataForRefresh = {
                project: projectID, startDate: getDates.sdate, endDate: getDates.edate, feedbackStatus: getStatus, workFlow: workFlowId, defID: listOfFlowchartdefId,
                durText: durationText, getdate: getDates
            }
        }
        else {
            getDeliveryExecutionFeedback(projectID, getDates.sdate, getDates.edate, getStatus, workFlowId, listOfFlowchartdefId);
            dataForRefresh = {
                project: projectID, startDate: getDates.sdate, endDate: getDates.edate, feedbackStatus: getStatus, workFlow: workFlowId, defID: listOfFlowchartdefId,
                durText: durationText, getdate: getDates
            }
        }
        
    }
    else {
        pwIsf.alert({ msg: 'Please select mandatory fields', type: 'warning' });
    }
}

//get the data for the NE delivery execution feedback and draw it in a table view
function getDeliveryExecutionFeedback(projectID, startDate, endDate, status, workFlowId, listOfFlowchartdefId) {
    $("#table_DeliveryExecution_feedbackPM").empty();

    if (startDate == "all" || endDate == "all") {
        var today = new Date();
        var tomorrow = new Date(today);
        tomorrow.setDate(tomorrow.getDate() + 1);
        startDate = formatted_date(today);
        endDate = formatted_date(tomorrow);
    }
    if ($.fn.dataTable.isDataTable('#table_DeliveryExecution_feedback')) {

        $("#dexFed").empty();
    }

    pwIsf.addLayer({ text: "Please wait ..." });

    var serviceUrl = service_java_URL + "activityMaster/getAllFeedback?projectID=" + projectID + "&startDate=" + startDate + "&endDate=" + endDate + "&feedbackStatus=" + status + "&listOfWorkFlowId=" + workFlowId + "&listOfFlowchartdefId=" + listOfFlowchartdefId;
    if (ApiProxy == true) {
        serviceUrl = service_java_URL + "activityMaster/getAllFeedback?projectID=" + projectID + "&startDate=" + startDate + "&endDate=" + endDate + "&feedbackStatus=" + status + "&listOfWorkFlowId=" + workFlowId + "&listOfFlowchartdefId=" + listOfFlowchartdefId;
    }

    var table = $('#table_DeliveryExecution_feedback').DataTable({
        "processing": true,
        "serverSide": true,
        "bLengthChange": false,
        // "searching": true,
        //"stateSave": true,
        dom: 'Bfrtip',
        buttons: [
            'colvis',
            {
                extend: 'excelHtml5',
                titleAttr: 'Excel',
                attr: {
                    id: 'btnDownloadFeedback'
                },
                action: function (e, dt, node, config) {
                    downloadFeedbackFiles();
                }
            }
        ],
        "ajax": {
            "headers": commonHeadersforAllAjaxCall,
            "url": serviceUrl,
            "type": "POST"            
},
        "order": [[0, "none"]],
        "responsive": true,
       // "retrieve": true,
        "destroy": true,
        "columns": [    
        //NE View
            {

                "title": "Action",
                "targets": 'no-sort',
                "orderable": false,
                "searchable": false,
                "data": "feedbackStatus",
                "defaultContent": "",
                "render": function (data, type, row, meta) {
                    var obj = {};
                    obj.workFlowName = row.workFlowName;
                    obj.feedbackStatus = row.feedbackStatus;
                    obj.feedbackDetailID = row.feedbackDetailID;
                    obj.feedbackActivityID = row.feedbackActivityID;
                    dta = JSON.stringify(obj);
                    jsonData = JSON.stringify(row);
                    
                   

                    if (data.toLowerCase() == "not feasible" || data.toLowerCase() == "implemented" && row.feedbackType != "ONSTEP_SAD_COUNT") {
                        let sbtpmType = '';
                        if (data.toLowerCase() == "not feasible") {
                            sbtpmType = 'NF'
                        }
                        else {
                            sbtpmType = 'I'
                        }
                        return '<div style="width: 67px;padding-top: 2px;"><button href="#addComment" class="fa fa-check btn btn-xs btn-success"  style="cursor: pointer;color:black;margin-right: 5px;padding-top: 3px;padding-bottom: 3px;" data-toggle="modal" data-toggle="tooltip" name="Accepted" title="Accepted" data-details= \'' + dta + '\' onclick="updateFeedbackStatus(this)"></button>' +
                         '<button href="#addComment" class="fa fa-arrow-left btn btn-xs btn-danger"  style="cursor: pointer;color:black;margin-right: 5px;padding-top: 3px;padding-bottom: 3px;padding-right: 6px;" data-toggle="modal" data-toggle="tooltip" name="Send Back to PM (' + sbtpmType + ')" title="Send Back to PM (' + sbtpmType + ')" data-details= \'' + dta + '\' onclick="updateFeedbackStatus(this)"></button></div>';

                    }
                    else if (row.feedbackType == "ONSTEP_SAD_COUNT") {
                        return '<b>NA</b>';
                    }
                    else {
                        return '';
                    }

                }

            },
            {
                "title": "Created Date",
                "data": "createdDate",
                "orderable": false
               
            },
            {
                "title": "Project ID",
                "data": "projectID",
                "orderable": false
               
            },
            {
                "title": "Feedback Status",
                "data": "feedbackStatus",
                "orderable": false

            },
            {
                "title": "WFID_Name_Version",
                "data": "workFlowName",
                "orderable": false
            },
            {
                "title": "Step Name",
                "data": "stepName",
                "orderable": false
            },
            //{
            //    "title": "Feedback On",
            //    "data": "feedbackOn",
            //    "orderable": false
            //},
            {
                "title": "Unsatisfied Count",
                "data": "sadCount",
                "orderable": false,
                "width" : "5%"
            },
            {
                "title": "Instant Feedback",
                "data": "feedbackText",
                "orderable": false
            },        
            {
                "title": "Feedback Comment",
                "data": "feedbackComment",
                "orderable": false,
                 "render": function (data, type, row, meta) {
                     if (data) {
                             data
                             .replace(/&/g, "&amp;")
                             .replace(/</g, "&lt;")
                             .replace(/>/g, "&gt;")
                             .replace(/"/g, "&quot;")
                             .replace(/'/g, "&#039;");
                         return "<div class='text-wrap'>" + data + "</div>";
                     }
                     else {
                         return "";
                     }
                }
                 
            },
            {
                "title": "PM/OM Comment",
                "data": "replyOnComment",
                "orderable": false,
                "render": function (data, type, row, meta) {

                    if (data) {
                         data =  data
                            .replace(/&/g, "&amp;")
                            .replace(/</g, "&lt;")
                            .replace(/>/g, "&gt;")
                            .replace(/"/g, "&quot;")
                            .replace(/'/g, "&#039;");
                        return "<div class='text-wrap'>" + data + "</div>";
                    }
                    else {
                        return "";
                    }
                }
            },
            {
                "title": "NE/DR Comment",
                "data": "neComments",
                "orderable": false,
                "render": function (data, type, row, meta) {
                    
                    if (data) {
                            data = data
                                .replace(/&/g, "&amp;")
                                .replace(/</g, "&lt;")
                                .replace(/>/g, "&gt;")
                                .replace(/"/g, "&quot;")
                                .replace(/'/g, "&#039;");
                          
                                return "<div class='text-wrap'>" + data + "</div>";
                        
                        }
                    
                    else {
                        return "";
                    }
                }
            },
           
            {
                "title": "Feedback Type",
                "data": "feedbackType",
                "orderable": false
            },
              {
                "title": "User Role",
                  "data": "userRole",
                  "orderable": false
            }
        ],
    
        initComplete: function () {
            $('#btnDownloadFeedback').hide();
            $('#table_DeliveryExecution_feedback tfoot th').each(function (i) {
                var title = $('#table_DeliveryExecution_feedback thead th').eq($(this).index()).text();
                if (title != "Action") {
                    $(this).html('<input type="text" class="form-control" style="font-size:12px;"  placeholder="Search ' + title + '" data-index="' + i + '" />');
                }
                    
            });
            if (table.data().count() > 0) {
                $('#btnDownloadFeedback').show();
            }
            else {
                $('#btnDownloadFeedback').hide();
            }

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

    $("#table_DeliveryExecution_feedback_filter").css("display", "none");

    $('#table_DeliveryExecution_feedback tfoot').insertAfter($('#table_DeliveryExecution_feedback thead'));
    // table.draw();
    $("#loaderDiv").hide();
    pwIsf.removeLayer();
   // $("#panel_infoNE").css("display", "block");
          
}

//to get the feedback data for the PM view********************************************************************************** P M  *******************************
//get the data for the delivery execution feedback and draw it in a table view
function getDeliveryExecutionFeedbackPM(projectID, startDate, endDate, status, workFlowId, listOfFlowchartdefId) {

    $("#table_DeliveryExecution_feedback").empty();
    $("#content").css('width', '99%');
    if (startDate == "all" || endDate == "all") {
        var today = new Date();
        var tomorrow = new Date(today);
        tomorrow.setDate(tomorrow.getDate() + 1);
        startDate = formatted_date(today);
        endDate = formatted_date(tomorrow);
    }
    if ($.fn.dataTable.isDataTable('#table_DeliveryExecution_feedbackPM')) {

        $("#dexFed").empty();
    }

    pwIsf.addLayer({ text: "Please wait ..." });

    var serviceUrl = service_java_URL + "activityMaster/getAllFeedback?projectID=" + projectID + "&startDate=" + startDate + "&endDate=" + endDate + "&feedbackStatus=" + status + "&listOfWorkFlowId=" + workFlowId + "&listOfFlowchartdefId=" + listOfFlowchartdefId;
    if (ApiProxy == true) {
        serviceUrl = service_java_URL + "activityMaster/getAllFeedback?projectID=" + projectID + "&startDate=" + startDate + "&endDate=" + endDate + "&feedbackStatus=" + status + "&listOfWorkFlowId=" + workFlowId + "&listOfFlowchartdefId=" + listOfFlowchartdefId;
    }

    var table = $('#table_DeliveryExecution_feedbackPM').DataTable({
        
        "processing": true,
        "serverSide": true,
        "bLengthChange": false,
        // "searching": true,
        //"stateSave": true,
        dom: 'Bfrtip',
        buttons: [
            'colvis',
            {
                extend: 'excelHtml5',               
                titleAttr: 'Excel',                
                attr: {
                    
                    id: 'btnDownloadFeedback'
                },                
                action: function (e, dt, node, config) {
                    downloadFeedbackFiles();
                }
            }
            
        ],
  
        "ajax": {
            "headers": commonHeadersforAllAjaxCall,
            "url": serviceUrl,
            "type": "POST"
        },
        "order": [[1, "none"]],
        "responsive": true,
        // "retrieve": true,
        "destroy": true,    
        

        "columns": [
            {                
                "title": "Action",
                "targets": 'no-sort',
                "orderable": false,
                "searchable": false,
                "data": "feedbackStatus",
                "defaultContent": "",  
                
                "render": function (data, type, row, meta) {
                    var obj = {};
                    obj.workFlowName = row.workFlowName;
                    obj.feedbackStatus = row.feedbackStatus;
                    obj.feedbackDetailID = row.feedbackDetailID;
                    obj.feedbackActivityID = row.feedbackActivityID;
                    dta = JSON.stringify(obj);
                    jsonData = JSON.stringify(row);
                   if (data.toLowerCase() == "new" && row.feedbackType != "ONSTEP_SAD_COUNT") {                       

                       return '<div style="width:60px !important"><button href="#addComment" class="fa fa-ban  btn btn-xs btn-warning" style="cursor: pointer;color:black;margin: 2px;" name="On Hold" data-toggle="modal" data-toggle="tooltip" title="On Hold" data-details= \'' + dta + '\'  onclick="updateFeedbackStatus(this)"></button>' +
                            '<button href="#addComment" class="fa fa-thumbs-up btn btn-xs btn-success"  style="cursor: pointer;color:black;margin: 2px; " data-toggle="modal" name="Feasible" data-toggle="tooltip" title="Feasible" data-details= \'' + dta + '\'  onclick="updateFeedbackStatus(this)"></button>' +
                            '<button href="#addComment"  class="fa fa-thumbs-down btn btn-xs btn-danger" style="cursor: pointer;color:black;margin: 2px;" data-toggle="modal" name="Not Feasible" data-toggle="tooltip" title="Not Feasible" data-details= \'' + dta + '\'  onclick="updateFeedbackStatus(this)"></button>'
                           
                           + '<button href="#addComment"  type="button" class="btn btn-danger btn-xs NAbutton" style="cursor: pointer;margin: 2px;font-size: 9px;" data-toggle="modal" name="Not Applicable" data-toggle="tooltip" title="Not Applicable" data-details= \'' + dta + '\'  onclick="updateFeedbackStatus(this)">NA</button></div>';
                   }

                    else if (data.toLowerCase() == "feasible" && row.feedbackType != "ONSTEP_SAD_COUNT")  {
                      
                       return '<button href="#addComment" class="fa fa-check btn btn-xs btn-info"  style="cursor: pointer;color:black;padding-top: 4px;border-bottom-width: 1px;padding-right: 6px;padding-left: 6px;padding-bottom: 4px;margin-top: 2px;margin-bottom: 2px;margin-left: 10px;" name="Implemented" data-toggle="modal" data-toggle="tooltip" title="Implement" data-details= \'' + dta + '\' onclick="updateFeedbackStatus(this)"></button>';
                    }
                    else if (data.toLowerCase() == "on hold" && row.feedbackType != "ONSTEP_SAD_COUNT") {
                       
                       return '<div style="display:flex"><button href="#addComment" class="fa fa-thumbs-up btn btn-xs btn-success"  style="cursor: pointer;color:black;border-top-width: 1px;padding-top: 5px;padding-bottom: 6px;padding-left: 7px;padding-right: 7px;margin-right: 4px;" name="Feasible" data-toggle="modal" data-toggle="tooltip" title="Feasible" data-details= \'' + dta + '\' onclick="updateFeedbackStatus(this)"></button>' +
                            '<button href="#addComment" class="fa fa-thumbs-down btn btn-xs btn-danger"  style="cursor: pointer;color:black;padding-top: 5px;padding-bottom: 6px;border-top-width: 1px;padding-left: 7px;padding-right: 7px;" data-toggle="modal" name="Not Feasible" data-toggle="tooltip" title="Not Feasible" data-details= \'' + dta + '\' onclick="updateFeedbackStatus(this)"></button></div>';
                   }
                   else if (data.toLowerCase() == "not applicable" && row.feedbackType != "ONSTEP_SAD_COUNT") {
                      
                       return '<button href="#addComment" class="fa fa-close btn btn-xs btn-default"  style="cursor: pointer;color:black;padding-top: 4px;border-bottom-width: 1px;padding-right: 6px;padding-left: 6px;padding-bottom: 4px;margin-top: 2px;margin-bottom: 2px;margin-left: 10px;" data-toggle="modal" data-toggle="tooltip" name="Closed" title="Close" data-details= \'' + dta + '\' onclick="updateFeedbackStatus(this)"></button>';
                   }
                   else if (data.toLowerCase() == "send back to pm (i)" && row.feedbackType != "ONSTEP_SAD_COUNT")
                   {
                       
                   return '<button href="#addComment" class="fa fa-check btn btn-xs btn-info"  style="cursor: pointer;color:black;padding-top: 4px;border-bottom-width: 1px;padding-right: 6px;padding-left: 6px;padding-bottom: 4px;margin-top: 2px;margin-bottom: 2px;margin-left: 10px;" name="Implemented" data-toggle="modal" data-toggle="tooltip" title="Implement" data-details= \'' + dta + '\' onclick="updateFeedbackStatus(this)"></button>';
                   }
                   else if (data.toLowerCase() == "send back to pm (nf)" && row.feedbackType != "ONSTEP_SAD_COUNT") {
                      
                       return '<div style="display:flex"><button href="#addComment" class="fa fa-ban  btn btn-xs btn-warning" style="cursor: pointer;color:black;border-top-width: 1px;padding-top: 5px;padding-bottom: 6px;padding-left: 7px;padding-right: 7px;margin-right: 5px;" name="On Hold" data-toggle="modal" data-toggle="tooltip" title="On Hold" data-details= \'' + dta + '\'  onclick="updateFeedbackStatus(this)"></button>' +
                           '<button href="#addComment" class="fa fa-thumbs-up btn btn-xs btn-success"  style="cursor: pointer;color:black;border-top-width: 1px;padding-top: 5px;padding-bottom: 6px;padding-left: 7px;padding-right: 7px; margin-right: 5px;" data-toggle="modal" name="Feasible" data-toggle="tooltip" title="Feasible" data-details= \'' + dta + '\'  onclick="updateFeedbackStatus(this)"></button>' +
                           '<button href="#addComment"  class="fa fa-thumbs-down btn btn-xs btn-danger" style="cursor: pointer;color:black;border-top-width: 1px;padding-top: 5px;padding-bottom: 6px;padding-left: 7px;padding-right: 7px;" data-toggle="modal" name="Not Feasible" data-toggle="tooltip" title="Not Feasible" data-details= \'' + dta + '\'  onclick="updateFeedbackStatus(this)"></button></div>'
                   }
                   else if (data.toLowerCase() == "not feasible" && row.feedbackType != "ONSTEP_SAD_COUNT") {
                        
                   }
                   else if (data.toLowerCase() == "implemented" && row.feedbackType != "ONSTEP_SAD_COUNT") {

                   }
                    else {
                        return 'Closed';
                    }                   
                }
            },
            {
                "title": "Created Date",
                "data": "createdDate",
                "orderable": false,
                
               
                "visible": true
            },
            
            {
                "title": "Project ID",
                "data": "projectID",
                "orderable": false               

            },
            {
                "title": "Feedback Status",
                "data": "feedbackStatus",
                "orderable": false
            },
            {
                "title": "WFID_Name_Version",
                "data": "workFlowName",
                "orderable": false,   
                "width": "64px"
            },
            {
                "title": "Step Name",
                "data": "stepName",
                "orderable": false
            },           
            {
                "title": "Unsatisfied Count",
                "data": "sadCount",
                "orderable": false,
                
            },
            {
                "title": "Instant Feedback",
                "data": "feedbackText",
                "orderable": false
            },
            {
                "title": "Created By",
                "data": "createdBy",
                "orderable": false
            },
            {
                "title": "Feedback Comment",
                "data": "feedbackComment",               
                "orderable": false,
                "render": function (data, type, row, meta) {

                    if (data) {
                             data
                            .replace(/&/g, "&amp;")
                            .replace(/</g, "&lt;")
                            .replace(/>/g, "&gt;")
                            .replace(/"/g, "&quot;")
                            .replace(/'/g, "&#039;");
                        return "<div class='text-wrap'>" + data + "</div>";

                    }
                    else {
                        return "";
                    }
                }
            },
      
            {
                "title": "PM/OM Comment",
                "data": "replyOnComment",
                "orderable": false,
                //"width": "200px",
                "render": function (data, type, row, meta) {

                    if (data) {
                        data = 
                        data
                            .replace(/&/g, "&amp;")
                            .replace(/</g, "&lt;")
                            .replace(/>/g, "&gt;")
                            .replace(/"/g, "&quot;")
                            .replace(/'/g, "&#039;");
                        return "<div class='text-wrap'>" + data + "</div>";
                    }
                    else {
                        return "";
                    }
                }
            },
            {
                "title": "NE/DR Comment",
                "data": "neComments",
                "orderable": false,
               // "width": "200px",
                "render": function (data, type, row, meta) {

                    if (data) {
                        data =
                            data
                                .replace(/&/g, "&amp;")
                                .replace(/</g, "&lt;")
                                .replace(/>/g, "&gt;")
                                .replace(/"/g, "&quot;")
                                .replace(/'/g, "&#039;");
                    
                            return "<div class='text-wrap'>" + data + "</div>";
                        
                    
                    }
                    else {
                        return "";
                    }
                }
            },
            
            //{
            //    "title": "End Date",
            //    "data": "endDate",
            //    "orderable": false,
            //    "visible": false
            //},
            {
                "title": "Feedback Type",
                "data": "feedbackType",
                "orderable": false
            },
            {
                "title": "User Role",
                "data": "userRole",
                "orderable": false,             
                
                
                "visible": true

            },
            {
                "title": "DetailID",
                "data": "feedbackDetailID",
                "orderable": false,
                "visible": false
            },
            {
                "title": "ActivityID",
                "data": "feedbackActivityID",
                "orderable": false,
                "visible": false
            }
          
        ],
        "columnDefs": [{
            render: function (data, type, full, meta) {
                return "<div id='dvNotes' style='white-space: normal;width: 121px;word-wrap: break-word; word-break: break-all;'>" + data + "</div>";
            },
            targets: 4
        },
            { render: function (data, type, full, meta) {
                return "<div id='dvNotes' style='white-space: normal;width: 60px;word-wrap: break-word; word-break: break-word;'>" + data + "</div>";
            },
            targets: 3
        }

        ],

        initComplete: function () {
            $('#btnDownloadFeedback').hide();
            $('#table_DeliveryExecution_feedbackPM tfoot th').each(function (i) {
                var title = $('#table_DeliveryExecution_feedbackPM thead th').eq($(this).index()).text();
                if (title != "Action") {
                    $(this).html('<input type="text" class="form-control" style="font-size:9px;"  placeholder="Search ' + title + '" data-index="' + i + '" />');
                }

            });
            if (table.data().count() > 0) {
                $('#btnDownloadFeedback').show();
            }
            else {
                $('#btnDownloadFeedback').hide();
            }
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
    
    $("#table_DeliveryExecution_feedbackPM_filter").css("display", "none")
    $('#table_DeliveryExecution_feedbackPM tfoot').insertAfter($('#table_DeliveryExecution_feedbackPM thead'));
    // table.draw();
    $("#loaderDiv").hide();
    pwIsf.removeLayer();
   
}
 
function updateFeedbackStatus(thisObj) {
   
   // var data_row = table.row($(this).closest('tr')).data();
    if (!$("#ProjectforFeedback").val() || !$("#workFlowForFeedback").val()) {

        $("#defHead").empty();
        $("#updateFeedback").empty();
        setTimeout(() => {
            $("#addComment").modal("hide");
            pwIsf.alert({ msg: 'Mandatory fields not selected.', type: "warning" });
        }, 1000);
    }

    else if ($(thisObj).data('details').workFlowName  && $(thisObj).data('details').feedbackStatus && $(thisObj).data('details').feedbackDetailID && $(thisObj).data('details').feedbackActivityID) {
        var btnTxt = thisObj.name;
    // console.log(thisObj);
    var jsonData = $(thisObj).data('details')

    var head = `
                        <div>
                            <button type="button" class="close" data-dismiss="modal">&times;</button>
<div class="row">
                          <label style="float:left"><b>Work Flow Name :</b></label>
                            <div  id="fbWF" >${jsonData.workFlowName}</div>
</div>
<div class="row">
                          <label style="float:left"> <b>Current Status :</b> </label>
                             <div  id="fbStatus" >${jsonData.feedbackStatus}</div></div>
<input type="text" id="fbID" style="display:none" value="${jsonData.feedbackDetailID}">
<input type="text" id="fbActID" style="display:none" value="${jsonData.feedbackActivityID}">
                           
                           <div>
<div >  `;


        var body = ``;
//<label><b>Comment:</b> 
//<textarea  rows="4" cols="80" id="changeCommentTxt" maxlength="1000" placeholder="Max Length 1000 character"></textarea>
//<span style="color:red;">Comment is mandatory in case of On Hold and Not Feasible status.</span>
// <label>                         
//</div>
//<br>
//             <button class="btn btn-primary" type="submit" data-txt="${btnTxt}"  onclick="submitComment(this)">Change to ${btnTxt}</button>   
//<button class="btn btn-secondary"  onclick="closePopup(this)">Cancel</button>`;

        if (userRoleID == 1 || userRoleID == 4) {

            body += '<label><b>Comment: </b>' +
                '<textarea  rows="4" cols="80" id="changeCommentTxt" maxlength="250" onkeyup="commentLengthFeedback(this)" placeholder="Max Length 250 characters"></textarea>' +
               '<div id="feedbackCommentCharacterCount" style="color:darkgray;margin-left: 267px;" class="col-md-12" style="padding-left: 15px;margin-left: 252px;">Only 250 characters are allowed (250 left)</div>' +
                '<span style="color:red;">Comment is mandatory in case of On Hold and Not Feasible status.</span>' +
                '</label>' +
                '</div>' +
                '<br>' +
                '<button class="btn btn-primary" type="submit" data-txt="' + btnTxt + '" onclick="submitComment(this)">Change to ' + btnTxt + '</button>' +
                '<button class="btn btn-secondary"  onclick="closePopup(this)">Cancel</button>';


        }
        else {
            body += '<label><b>Comment: </b>' +
                '<textarea  rows="4" cols="80" id="changeCommentTxt" maxlength="250" onkeyup="commentLengthFeedback(this)" placeholder="Max Length 250 characters"></textarea>' +
                '<div id="feedbackCommentCharacterCount" style="color:darkgray;margin-left: 267px;" class="col-md-12" style="padding-left: 15px;margin-left: 252px;">Only 250 characters are allowed (250 left)</div>' +
                '<span style="color:red;">Comment is mandatory in case of Send Back to PM(I/NF)</span>' +
                '</label>' +
                '</div>' +
                '<br>' +
                '<button class="btn btn-primary" type="submit" data-txt= "' + btnTxt  + '" onclick="submitComment(this)">Change to ' + btnTxt + '</button>' +
                '<button class="btn btn-secondary"  onclick="closePopup(this)">Cancel</button>';
        }

    $("#defHead").empty();
    $("#defHead").append(head);
    $("#updateFeedback").empty();
    $("#updateFeedback").append(body);

}
    else {
        
        setTimeout(() => {
            $("#addComment").modal("hide");
            pwIsf.alert({ msg: 'Some data for this feedback row is invalid/missing.', type: "error" });
        }, 500);
    }
}

function submitComment(btn) {
    let btntxt = btn.getAttribute("data-txt")

    if (!$("#changeCommentTxt").val() && (btntxt.toLowerCase() == "on hold" || btntxt.toLowerCase() == "not feasible")) {
        pwIsf.alert({ msg: 'Comment is mandatory in case of On Hold and Not Feasible status.', type: "warning" });
    }
    else if (!$("#changeCommentTxt").val() && (btntxt.toLowerCase() == "send back to pm (i)" || btntxt.toLowerCase() == "send back to pm (nf)")) {
        pwIsf.alert({ msg: 'Comment is mandatory in case of Send Back to PM (I/NF) status.', type: "warning" });
    }
    else if (!$("#ProjectforFeedback").val() || !$("#workFlowForFeedback").val()) {
        pwIsf.alert({ msg: 'Mandatory fields not selected.', type: "warning" });
        $("#addComment").modal("hide");
    }
    else {       
        pwIsf.addLayer({ text: "Please wait ..." });

        let serviceUrl = service_java_URL + "activityMaster/updateFeedbackStatus"
        if (ApiProxy == true) {
            serviceUrl = service_java_URL + "activityMaster/updateFeedbackStatus"
        }
        var obj = new Object();
        obj.feedbackActivityID = $("#fbActID").val();
        obj.feedbackDetailID = $("#fbID").val();
        obj.statusUpdateComment = $("#changeCommentTxt").val();
        obj.feedbackStatus = $("#fbStatus").html();
        obj.feedbackStatusNew = btntxt;

        $.isf.ajax({
            url: serviceUrl,
            data: JSON.stringify(obj),
            dataType: 'json',
            contentType: "application/json; charset=utf-8",
            method: 'POST',
            success: function (data) {
                pwIsf.removeLayer();
                if (!data.isValidationFailed) {
                    
                    pwIsf.alert({ msg: data.formMessages[0], type: "success", autoClose: 2  });
                    $("#addComment").modal("hide");
                    setParameterForFeedback();
                }
                else {
                   
                    pwIsf.alert({ msg: data.formErrors[0], type: "error", autoClose: 2  });
                    pwIsf.removeLayer();
                    $("#addComment").modal("hide");
                }
                
            },
            error: function (xhr, status, statusText) {
                pwIsf.removeLayer();
                console.log('An error occurred');
            }
        });                          
    }   
}

function WFChange() {

    if ($("#selectAllWF").prop('checked') == true) {
        $('#workFlowForFeedback option').prop('selected', 'selected');
        $("#workFlowForFeedback").trigger("change");
    }
    else {
        $('#workFlowForFeedback option').prop('selected', false);
        $("#workFlowForFeedback").trigger("change");
    }

       // $('#workFlowForFeedback option').prop('selected', true);    
}

function closePopup(x) {
    $("#addComment").modal("hide");
}

function escapeHtml(text) {
    if (text) {
        return text
            .replace(/&/g, "&amp;")
            .replace(/</g, "&lt;")
            .replace(/>/g, "&gt;")
            .replace(/"/g, "&quot;")
            .replace(/'/g, "&#039;");
    }
    return '';
}

function wfDDchange() {
    $('#btnDownloadFeedback').hide();
    $("#dexFed").empty();
    $("#table_DeliveryExecution_feedback_info").css("display", "none")
    $("#table_DeliveryExecution_feedbackPM_info").css("display", "none")
    if ($("#workFlowForFeedback").val() && $("#ProjectforFeedback").val()) {
        $("#searchFeedbacksID").removeAttr('disabled');
        $("#feedbackRefresh").removeAttr('disabled');
        
    }
    else {
        $("#searchFeedbacksID").prop("disabled", true);
        $("#feedbackRefresh").prop("disabled", true);
    }  

    $('#workFlowForFeedback option').each(function () {
        if (!this.selected) {
            $("#selectAllWF").prop("checked", false);
        }
    });

    var selected_option_count = $('#workFlowForFeedback :selected').length;
    var total_option_count = $('#workFlowForFeedback option').length;

    if (selected_option_count === total_option_count) {
        $("#selectAllWF").prop("checked", true);

    }


}

var formatted_date = function (date) {
    var m = ("0" + (date.getMonth() + 1)).slice(-2); // in javascript month start from 0.
    var d = ("0" + date.getDate()).slice(-2); // add leading zero 
    var y = date.getFullYear();
    return y + '-' + m + '-' + d;
}

function downloadTemplateFeedbackFileData(projectID, sdate, edate, getStatus, workFlowId, listOfFlowchartdefId) {
    
    let projectid = projectID;
    currentRole = JSON.parse(ActiveProfileSession).role;
   
    let startDate = sdate;
    let endDate = edate;
    let feedbackStatus = getStatus;
    let listOfFlowchartdefIds = listOfFlowchartdefId;
    let workFlowIds = workFlowId;  
    var FeedbackSearch = "projectID=" + projectid + "&startDate=" + startDate + "&endDate=" + endDate + "&feedbackStatus=" + feedbackStatus + "&listOfWorkFlowId=" + workFlowIds + "&listOfFlowchartdefId=" + listOfFlowchartdefIds + "&signum=" + signumGlobal + "&role=" + currentRole;
    let FeedbackEncode = encodeURIComponent(FeedbackSearch);
    let serviceUrl = `${service_java_URL}activityMaster/downloadFeedbackFile?${FeedbackEncode}`;
  
    let fileDownloadUrl;
    fileDownloadUrl = UiRootDir + "/data/GetFileFromApi?apiUrl=" + serviceUrl;
    window.location.href = fileDownloadUrl;

}

function downloadFeedbackFiles() {
    let statusText = $('.filterHrefs a.dividerStatus.active').text();
    let getStatus = getDEFeedbackFilter(statusText);

    let durationText = $('.filterHrefs a.divider.active').text();
    let getDates = getStartDateAndEndDateFromSelectedDuration(durationText);


    let projectID = $("#ProjectforFeedback").val();
    let workFlowId = $("#workFlowForFeedback").val();
    let listOfFlowchartdefId = [];
    listOfFlowchartdefId = $("#workFlowForFeedback :selected").map(function (i, el) {
        return $(el).data("name");;
    }).get();

    if (getDates && durationText && getStatus && projectID && workFlowId && listOfFlowchartdefId) {
        //1 :PM
        if (userRoleID == 1) {
            downloadTemplateFeedbackFileData(projectID, getDates.sdate, getDates.edate, getStatus, workFlowId, listOfFlowchartdefId);
        }
        else {
            downloadTemplateFeedbackFileData(projectID, getDates.sdate, getDates.edate, getStatus, workFlowId, listOfFlowchartdefId);
        }

    }
    else {
        pwIsf.alert({ msg: 'Please select mandatory fields', type: 'warning' });
    }
}


//Refresh the feedback table based on previous data
function refreshFeedback() {

    let statusText = $('.filterHrefs a.dividerStatus.active').text();
    let getStatus = getDEFeedbackFilter(statusText);

    let durationText = $('.filterHrefs a.divider.active').text();
    let getDates = getStartDateAndEndDateFromSelectedDuration(durationText);

    let startDate = dataForRefresh.startDate;
    let project = dataForRefresh.project;
    let endDate = dataForRefresh.endDate;
    let status = getStatus;           //dataForRefresh.feedbackStatus;
    let workFlow = dataForRefresh.workFlow;
    let defid = dataForRefresh.defID;

    if (getDates && durationText && status && project && workFlow && defid) {
        //1 :PM
        //4 :OM
        if (userRoleID == 1 || userRoleID == 4) {
            getDeliveryExecutionFeedbackPM(project, startDate, endDate, status, workFlow, defid);
        }
        else {
            getDeliveryExecutionFeedback(project, startDate, endDate, status, workFlow, defid);
        }
    }
}
function commentLengthFeedback(commentTextArea){
    let feedbackCharCount = $(commentTextArea).val().length,
    left = 250 - feedbackCharCount;
    $('#feedbackCommentCharacterCount').text("Only 250 characters are allowed (" + left + " left)");
}