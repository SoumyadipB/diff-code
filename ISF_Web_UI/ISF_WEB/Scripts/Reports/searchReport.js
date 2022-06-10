
var allRptInitData;
var reportTable;
var module;
var subModule;
var reportName;
var urlInput;
var validateChartQueryChk = 0; 
var colFilter = 0;
var validateQueryEditChk = 0;
var editclicked = 0;

function getFilterattributesEdit() {
    //var filterGroupId = document.getElementById("filter_attr").value;

    var jqXHR = $.isf.ajax({
        url: service_java_URL + "reportManagement/getFilterGroups/",
        returnAjaxObj: true,
        datatype: "json"
    });

    jqXHR.done(function (data) {
        $.each(data, function (i, d) {
           // $('#edit_filter_attr').append('<option value="' + d.filterGroup + '">' + d.filterDescription + '</option>');
        })
    });

    jqXHR.fail(function (jqXHR, textStatus) {
        alert("(report) Request failed: " + textStatus);
    });

}
//Referenace data List of Module/Submodule
function getReportNameModuleSubModule() {

    var finalArr = {};
    finalArr['Module'] = []; finalArr['subModule'] = []; finalArr['rpt'] = [];

    var request = $.isf.ajax({
        url: service_java_URL + "reportManagement/getReportInitData/",
        returnAjaxObj: true,
        dataType: "json"
    });

    request.done(function (msg) {


        allRptInitData = msg.MODULE_SUBMODULE;
        getModuleSrch(allRptInitData);

        //Collaps report options by default
        // $(".panel-heading span.clickable").trigger("click");
    });

    request.fail(function (jqXHR, textStatus) {
        alert("(getReportInitData) Request failed: " + textStatus);
    });

}
function getModuleSrch(data) { // DISPLAY ALL MODULES
    var modArr = [];

    for (var i = 0; i < data.length; i++) {
        if (modArr.indexOf(data[i].moduleName) < 0) {
            modArr.push(data[i].moduleName);
        }

    }

    fillSelect(modArr, 'Module', 'Select Module');

    $("#Module").select2({}).on("select2:selecting", function (e) { if (e.params.args.data) { getSubModuleSrch(e.params.args.data.id); } })

   

}
function getSubModuleSrch(selectedModule) { // GET SUBMODULE AGAINST MODULE
    $('#submodule_edit')
        .find('option')
        .remove();
    var data = allRptInitData;
    $('#txtReportName').val('');
    var subMod = [];

    for (var i = 0; i < data.length; i++) {

        if (data[i].moduleName == selectedModule && subMod.indexOf(data[i].subModuleName) < 0) {
            subMod.push(data[i].subModuleName);
        }
    }

    fillSelect(subMod, 'SubModule', 'Select Sub-Module');

   
    $("#SubModule").select2({}).off("select2:selecting").on("select2:selecting", function (e) { if (e.params.args.data.id) { getAllRptBySubmod(selectedModule, e.params.args.data.id); } })
   


}

function getAllRptBySubmod(module, subMod) { // GET REPORT AGAINST MODULE AND SUBMODULE
    $('#json-reportlist').html('');
    $('#txtReportName').val('');
    var data = allRptInitData;
    var rpt = [];

    for (var i = 0; i < data.length; i++) {

        if (data[i].moduleName == module && data[i].subModuleName == subMod) {
            rpt.push(data[i].reportName);
            $('#json-reportlist').append('<option value = " ' + data[i].reportName + ' " >' + data[i].reportName + '</option>');
        }
    }


}
function fillSelect(data, select2Id, firstText) { // A COMMON FUNCTION 
    $('#' + select2Id).html(''); //clear select box

    $('#' + select2Id).append('<option value="">' + firstText + '</option>');

    $.each(data, function (index, value) {
        $('#' + select2Id).append(
          '<option value="' + value + '">' + value + '</option>'
        );
       

    });
}

//Referenace data List of filter groups
function getFilterGroups() {
    var filterGroupId = document.getElementById("filter_groups").value;

    var jqXHR = $.isf.ajax({
        url: service_java_URL + "reportManagement/getReportFilters/",
        returnAjaxObj: true
    });

    jqXHR.done(function (data) {
        $.each(data, function (i, d) {
            $('#edit_filter_groups').append('<option value="' + d.filterGroup + '">' + d.filterGroup + '</option>');
        });
    });

    jqXHR.fail(function (jqXHR, textStatus) {
        alert("(report) Request failed: " + textStatus);
    });

}

//Submit clicked after Reportname, module and submodule has been selected to fetch Report list
function getRptDetailsSubmit() {
   
   
    var ReportName = Trim($("#txtReportName").val());
    var Module = "";
    var SubModule = "";
    if ($("#Module").val() != "")
    {
     Module = Trim($("#Module").val());
     SubModule = Trim($("#SubModule").val());
    }
    var actionIcon = "";
    var filterData = "";

    if (ReportName != 0 || Module != "" || SubModule != "" || ReportName != "") {

        if ((ReportName != 0 && ReportName != "") && Module != "" && SubModule != "")
            filterData = "reportName=" + ReportName + "&subModule=" + SubModule + "&module=" + Module + "";
        else if ((ReportName != 0 && ReportName != "") && Module != "" && SubModule == "")
            filterData = "reportName=" + ReportName + "&module=" + Module + "";
        else if ((ReportName != 0 && ReportName != "") && Module == "" && SubModule != "")
            filterData = "reportName=" + ReportName + "&subModule=" + SubModule + "";
        else if ((ReportName == 0 || ReportName == "") && Module != "" && SubModule != "")
            filterData = "module=" + Module + "&subModule=" + SubModule + "";
        else if ((ReportName != 0 && ReportName != "") && Module == "" && SubModule == "")
            filterData = "reportName=" + ReportName;
        else if ((ReportName == 0 || ReportName == "") && Module != "" && SubModule == "")
            filterData = "module=" + Module;
        else if ((ReportName == 0 || ReportName == "") && Module == "" && SubModule != "")
            filterData = "subModule=" + SubModule;
      
        $("#table_serachReportBody tr").remove();
        $.isf.ajax({
            url: service_java_URL + "reportManagement/searchReport?" + filterData,
            success: function (data) {
                var html = "";                
                $.each(data, function (i, d) {

                    html = html + "<tr>";
                    //html = html + "<td><input type='checkbox' name='check[]'/></td>";
                    html = html + "<td>" + d.reportName + "</td>";
                    html = html + "<td>" + d.moduleName + "</td>";
                    html = html + "<td>" + d.subModuleName + "</td>";

                    if (d.active == 1)
                    {
                        html = html + '<td>' +
                                '<i style="cursor: pointer;" data-toggle="modal" data-target="#modalEditReport" class="fa fa-eye" title="View/Edit Report" onclick="loadEditReportData(\'' + d.reportName + '\',\'' + d.moduleName + '\',\'' + d.subModuleName + '\' )"></i>&thinsp;&thinsp;&thinsp;' +
                               ' &nbsp;|&nbsp;&nbsp;<i style="cursor: pointer;" data-toggle="modal" data-target="#modalDeleteReport" class="fa fa-trash" title="DeleteReport" onclick="sendDataDelete(\'' + d.reportName + '\',\'' + d.moduleName + '\',\'' + d.subModuleName + '\')"></i>&thinsp;&thinsp;&thinsp;';
                    }
                    else if (d.active ==0)
                    {
                        html = html + '<td>' +
                                     '<i style="cursor: pointer;" data-toggle="modal" data-target="#modalEditReport" class="fa fa-eye" title="View/Edit Report" onclick="loadEditReportData(\'' + d.reportName + '\',\'' + d.moduleName + '\',\'' + d.subModuleName + '\' )"></i>&thinsp;&thinsp;&thinsp;';
                                   // ' &nbsp;|&nbsp;&nbsp;<i style="cursor: pointer;" data-toggle="modal" data-target="#modalDeleteReport" class="fa fa-trash" title="DeleteReport" onclick="sendDataDelete(\'' + d.reportName + '\',\'' + d.moduleName + '\',\'' + d.subModuleName + '\')"></i>&thinsp;&thinsp;&thinsp;';
                    }
                   
                   
                    html = html + "</tr>";
                })
                $("#table_serachReportBody").append(html);

                if ($.fn.dataTable.isDataTable('#table_SearchReport')) {
                    searchdataTable.distroy();
                       $('#table_SearchReport').empty();
                }
                var searchdataTable = $('#table_SearchReport').DataTable({
                    paging:true,                   
                    searching: true,                                 
                  "colReorder": true,                 
                  "responsive": true,
                  "distroy": true,                 
                  dom:'Bfrtip',
                    buttons: [
                     'colvis','excel'
                    ],
                    initComplete: function () {

                        $('#table_SearchReport tfoot th').each(function (i) {
                            var title = $('#table_SearchReport thead th').eq($(this).index()).text();
                            $(this).html('<input type="text" placeholder="Search ' + title + '" data-index="' + i + '" />');
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

              $('#table_SearchReport tfoot').insertAfter($('#table_SearchReport thead'));
            
              
            },
            error: function (err) {
                alert("Unable to Search Report, Error : " + err);
            }
        });
    }
    else {
        alert("Kindly Select On of the filter");
    }
}

//Trim Function
function Trim(x) {
    return x.replace(/^\s+|\s+$/gm, '');
}

// data to be sent to model before delete confirmation
function sendDataDelete(reportName, module, subModule) {
    $("#reportname_delete").val(reportName);
    $("#module_delete").val(module);
    $("#subModule_delete").val(subModule);

}
// Make a report inactive  after clicking delete confirm button
function deleteReport() {

    reportName = document.getElementById("reportname_delete").value;
    module = document.getElementById("module_delete").value;
    subModule = document.getElementById("subModule_delete").value;
    urlInput = '{"reportName":"' + reportName + '","moduleName":"' + module + '","subModuleName":"' + subModule + '"}';

   
   // alert(urlInput);
    $.isf.ajax({
        url: service_java_URL + "reportManagement/deleteReport/",
        context: this,
        crossdomain: true,
        processData: true,
        contentType: 'application/json',
        type: 'POST',
        data: urlInput,
        xhrFields: {
            withCredentials: false
        },
        success: function (data) {
            $('#messageOK').show();
            document.getElementById('lblDeletereport').style.display = 'none';
            document.getElementById('contentFormDelete').style.display = 'none';
            document.getElementById('deletebutton').style.display = 'none';
            //document.getElementByID('cancelModal').style.display = 'none';
            getRptDetailsSubmit();
            

        },
        error: function (xhr, status, statusText) {
            alert('Report can not be deleted' + xhr.error);
            console.log('An error occurred');
        }
    });

}


// Load data for View report Model
function loadViewReport(reportName, module, subModule) {
    
    var urlFilterdata = "reportName=" + reportName + "&module=" + module + "&subModule=" + subModule + "";
    if (ApiProxy == true) {
        urlFilterdata = encodeURIComponent("reportName=" + reportName + "&module=" + module + "&subModule=" + subModule + "");
    }
    $.isf.ajax({
        url: service_java_URL + "reportManagement/getReportDetail?" + urlFilterdata,

        success: function (data) {
            $("#view_report_ID").val(data.reportId);
            $("#view_report_name").val(data.reportName);
            $("#view_module_name").val(data.moduleName);
            $("#view_subModule_Name").val(data.subModuleName);
            $("#view_Description").val(data.reportDescription);
            $("#view_Formula").val(data.formula);
            $("#view_Technical_Explanation").val(data.technicalExplaination);
            //$("#view_Remarks").val(data.remarks);
            $("#view_DetailQuesry").val(data.detailQuery);
            $("#view_Select_GroupBy").val(data.selectGroupBy);
            //$("#view_AggregationColumn").val(data.aggColumn);
            $("#view_FilterAttribute").val(data.filterAttribute);
            $("#view_ChartType").val(data.chartType);
            $("#view_AxisType").val(data.axisType);
            $("#input_X_Axis").val(data.xAxisLabel);
           
            
            $("#view_Y_Axis").val(data.yAxisLabel);
            $("#view_active").val(data.active);
            $("#view_ColumnChart").val(data.columnChart);
            $("#view_column_Detail_Query").val(data.columnDetailQuery);
            $("#view_createdBy").val(data.createdBy);
            $("#view_createdOn").val(data.createdOn);

        },
        error: function (xhr, status, statusText) {
            alert('An error occurred on getReportDetails: ' + xhr.error);
        }
    })
}

// Load data for selected report for Edit Report Model
function loadEditReportData(reportName, module, subModule) {
    var y_axis_arr = [];
    var urlFilterdata = "reportName=" + reportName + "&module=" + module + "&subModule=" + subModule + "";
    if (ApiProxy == true) {
        urlFilterdata = encodeURIComponent("reportName=" + reportName + "&module=" + module + "&subModule=" + subModule + "");
    }
    $.isf.ajax({
        url: service_java_URL + "reportManagement/getReportDetail?" + urlFilterdata,

        success: function (data) {
            $("#edit_report_ID").val(data.reportId);
            $("#edit_report_name").val(data.reportName);
            $("#edit_module").val(data.moduleName);
            $("#edit_subModule").val(data.subModuleName);
            $("#edit_report_description").val(data.reportDescription);
            $("#edit_formula").val(data.formula);
            $("#edit_technical_explanation").val(data.technicalExplaination);
            //$("#edit_fltrAttr").val(data.filterAttribute);
            $("#edit_detail_query").val(data.detailQuery);
            $("#edit_group_by").val(data.selectGroupBy);
            $("#edit_filter_attr").val(data.filterAttribute);
            $("#edit_chart_query").val(data.chartQuery);
            $("#view_chart_type").val(data.chartType);
            //$("#edit_chart_type").selected = true;
            $("#view_axis_type").val(data.axisType);           
           // $("#edit_axis_type").selected = true;
            $("#edit_x_label").val(data.xAxisLabel);
            y_axis_arr = JSON.parse(data.yAxisLabel);
            console.log("data.axisType " + data.axisType)
            if (data.axisType == 'single' || data.axisType == 'Single')
                {
                    $("#y_title_EDIT").val(y_axis_arr[0].title);
                    $("#y_val_suff_EDIT").val(y_axis_arr[0].valueSuffix);
                    $("#y_val_col_EDIT").val(y_axis_arr[0].columnName);
                    $("#y_title_mul_EDIT").val('');
                    $("#y_val_suff_mul_EDIT").val('');
                    $("#y_val_col_mul_EDIT").val('');
                    $("#y_axis_lab_mul_EDIT").hide();

                    
                }
                else
                {
                    $("#y_title_EDIT").val(y_axis_arr[0].title);
                    $("#y_val_suff_EDIT").val(y_axis_arr[0].valueSuffix);
                    $("#y_val_col_EDIT").val(y_axis_arr[0].columnName);
                    $("#y_title_mul_EDIT").val(y_axis_arr[1].title);
                    $("#y_val_suff_mul_EDIT").val(y_axis_arr[1].valueSuffix);
                    $("#y_val_col_mul_EDIT").val(y_axis_arr[1].columnName);
                    $("#y_axis_lab_mul_EDIT").show();
            }

            console.log("ACTIVE " + data.active);
            $("#active_read").val(data.active);         


        },
        error: function (xhr, status, statusText) {
            alert('An error occurred on getReportDetails: ' + xhr.error);
        }
    })

}

function editFields()
{
    editclicked = 1;
    getRptInitForEDIT($('#edit_module').val());
    validateQueryEDITforFilterPop();
   

    console.log("submodule_edit " + $('#edit_subModule').val());
    $('#submodule_edit').append('<option value="' + $('#edit_subModule').val() + '" selected="true" disabled>' + $('#edit_subModule').val() + '</option>');
    $("#edit_axis_type").val($("#view_axis_type").val()).change();
    var active = $("#active_read").val();
    if (active == 'true' || active == 'True')
        active = '1';
    else
        active = '0';
    console.log("active " + active);
    $("#edit_active").val(active).change();

    var chartTypeval = [];
    var chartType = $('#view_chart_type').val();
    chartTypeval = chartType.split(';');
    console.log(chartTypeval);
    $("#edit_chart_type").val(chartTypeval).change();
   

    console.log("AXIS TYPE "+$('#view_axis_type').val());
    if ($('#view_axis_type').val() == 'single' || $('#view_axis_type').val() == 'Single')
    {
        $("#y_axis_lab_mul_EDIT").hide();
    }
    else
    {
        $("#y_axis_lab_mul_EDIT").show();
    }
    
    $('#y_col_name_EDIT').append('<option value="0" selected="true" disabled>Column Name</option>');
    $('#y_col_name_mul_EDIT').append('<option value="0" selected="true" disabled>Column Name</option>');

    document.getElementById('edit_report_ID').removeAttribute('readonly');
    document.getElementById('edit_report_name').removeAttribute('readonly');
    document.getElementById('edit_report_description').removeAttribute('readonly');
    document.getElementById('edit_formula').removeAttribute('readonly');
    document.getElementById('edit_technical_explanation').removeAttribute('readonly');
   // document.getElementById('edit_detail_query').removeAttribute('readonly');
    document.getElementById('edit_group_by').removeAttribute('readonly');
   // document.getElementById('edit_chart_query').removeAttribute('readonly');
    document.getElementById('edit_axis_type').removeAttribute('disabled');
    document.getElementById('edit_x_label').removeAttribute('readonly');


    document.getElementById('edit_detail_query').readOnly = true;
    document.getElementById('edit_chart_query').readOnly = true;
  
    document.getElementById('edit_active').removeAttribute('disabled');

    document.getElementById('y_title_EDIT').removeAttribute('readonly');
    document.getElementById('y_val_suff_EDIT').removeAttribute('readonly');

    document.getElementById('y_title_mul_EDIT').removeAttribute('readonly');
    document.getElementById('y_val_suff_mul_EDIT').removeAttribute('readonly');
    
  
    $("#module_edit_view").show();
    $("#edit_module").hide();
    $("#submodule_edit").show();
    $("#edit_subModule").hide();
    $("#btnEdit").show();

    $("#validate_query_edit").hide();
    $("#Change_query_edit").show();

    $("#validate_chart_query_edit").hide();
    $("#changeChartQuery_ForFilter_edit").show();

    $("#group_by_edit_div").show();
    
    $("#filter_attr_edit_div").show();
    $("#group_by_view_div").hide();
    $("#edit_filter_view_div").hide();
    $("#view_chart_type").hide();
    $("#edit_chart_type_div").show();
    $("#view_axis_type").hide();
    $("#edit_axis_type_div").show();
    $("#active_read").hide();
    $("#edit_active_view_div").show();
    $("#y_col_name_EDIT").show();
    $("#y_col_name_mul_EDIT").show();
    $("#y_val_col_mul_EDIT").hide();
    $("#y_val_col_EDIT").hide();
    $("#btnSubmit").prop("disabled", true);
}


function funcCallReset() {
    $('#module_edit')
        .find('option')
        .remove();
    $('#submodule_edit')
        .find('option')
        .remove();
    console.log("AXIS TYPE " + $('#view_axis_type').val());
    if ($('#view_axis_type').val() == 'single' || $('#view_axis_type').val() == 'Single') {
        $("#y_axis_lab_mul_EDIT").hide();
    }
    else {
        $("#y_axis_lab_mul_EDIT").show();
    }



    document.getElementById('edit_report_ID').readOnly = true;
    document.getElementById('edit_report_name').readOnly = true;
    document.getElementById('edit_report_description').readOnly = true;
    document.getElementById('edit_formula').readOnly = true;
    document.getElementById('edit_technical_explanation').readOnly = true;

    document.getElementById('edit_group_by').readOnly = true;
    
    document.getElementById('edit_axis_type').disabled = true;
    document.getElementById('edit_x_label').readOnly = true;
  
    document.getElementById('edit_active').disabled = true;
    document.getElementById('y_title_EDIT').readOnly = true;
    document.getElementById('y_val_suff_EDIT').readOnly = true;

    document.getElementById('y_title_mul_EDIT').readOnly = true;
    document.getElementById('y_val_suff_mul_EDIT').readOnly = true;

    document.getElementById('edit_detail_query').readOnly = false;
    document.getElementById('edit_chart_query').readOnly = false;

    $("#module_edit_view").hide();
    $("#edit_module").show();
    $("#submodule_edit").hide();
    $("#edit_subModule").show();
    $("#btnEdit").hide();


    $("#validate_query_edit").hide();
    $("#Change_query_edit").hide();

    $("#validate_chart_query_edit").hide();
    $("#changeChartQuery_ForFilter_edit").hide();

    $("#group_by_edit_div").hide();
    $("#validate_chart_query_edit").hide();
    $("#filter_attr_edit_div").hide();
    $("#group_by_view_div").show();
    $("#edit_filter_view_div").show();
    $("#view_chart_type").show();
    $("#edit_chart_type_div").hide();
    $("#view_axis_type").show();
    $("#edit_axis_type_div").hide();
    $("#active_read").show();
    $("#edit_active_view_div").hide();
    $("#y_col_name_EDIT").hide();
    $("#y_col_name_mul_EDIT").hide();
    $("#y_val_col_mul_EDIT").show();
    $("#y_val_col_EDIT").show();
    $("#Change_query_edit").hide();
    
    $("#btnSubmit").prop("disabled", false);
}

function yAxisAddEDIT()
{
    var axisType = $('#edit_axis_type').val();
    if (axisType == 'multiple' || axisType == 'Multiple') {
        console.log("multiple");
        $("#y_axis_lab_mul_EDIT").show();


    }
    else {
        console.log("single");
        $("#y_axis_lab_mul_EDIT").hide();

    }
    getChartfiltersEDIT(axisType);
}

function getChartfiltersEDIT(axisTypeOP) {

    $('#y_col_name_EDIT')
        .find('option')
        .remove();
    $('#y_col_name_mul_EDIT')
        .find('option')
        .remove();
    var chartQuery = $('#edit_chart_query').val();
    var detailQuery = $('#edit_detail_query').val();
    if (editclicked==1)
        var selectGroupBy = $('#edit_group_by').val();
    else
        var selectGroupBy = $('#group_by_edit').val().join(';');

    var validateChartQuery = {
        "chartQuery": chartQuery,
        "detailQuery": detailQuery,
        "selectGroupBy": selectGroupBy
    }
    var dataparam = JSON.stringify(validateChartQuery);
    console.log(dataparam);
    $.isf.ajax({
        url: service_java_URL + "reportManagement/getChartFilters",
        context: this,
        crossdomain: true,
        processData: true,
        contentType: 'application/json',
        type: 'POST',
        data: dataparam,
        xhrFields: {
            withCredentials: false
        },
        success: function (data) {

            for (var i = 0; i < data.length; i++)
            {
                if (axisTypeOP == 'Single' || axisTypeOP == 'single')
                    $('#y_col_name_EDIT').append('<option value="' + data[i] + '">' + data[i] + '</option>');
                else {
                    $('#y_col_name_EDIT').append('<option value="' + data[i] + '">' + data[i] + '</option>');
                    $('#y_col_name_mul_EDIT').append('<option value="' + data[i] + '">' + data[i] + '</option>');
                }
            }
              
            colFilter = 1;
            var yValColEdit = $("#y_val_col_EDIT").val();
            var yValColEditMul = $("#y_val_col_mul_EDIT").val();
            console.log("colfilter filter populate " + colFilter + ' yValColEdit ' + yValColEdit + ' yValColEditMul ' + yValColEditMul);           
            $("#y_col_name_EDIT").val(yValColEdit).change();
            $("#y_col_name_mul_EDIT").val(yValColEditMul).change();
        },
        error: function (xhr, status, statusText) {
            console.log('An error occurred on getCustomers' + xhr.responseText);
        }
    });
}


// Update Report after click on save button.

function getRptInitForEDIT(moduleName) {

    var finalArr = {};
    finalArr['Module'] = []; finalArr['subModule'] = []; finalArr['rpt'] = [];

    var request = $.isf.ajax({
        url: service_java_URL + "reportManagement/getReportInitData/",
        returnAjaxObj: true,
        dataType: "json"
    });

    request.done(function (msg) {


        allRptInitData = msg.MODULE_SUBMODULE;
        getModuleEDIT(allRptInitData, moduleName);

        //Collaps report options by default
        // $(".panel-heading span.clickable").trigger("click");
    });

    request.fail(function (jqXHR, textStatus) {
        alert("(getReportInitData) Request failed: " + textStatus);
    });

}

//Fill Module Dropdown 
function getModuleEDIT(data, moduleName) { // DISPLAY ALL MODULES
    var modArr = [];

    for (var i = 0; i < data.length; i++) {
        if (modArr.indexOf(data[i].moduleName) < 0 && data[i].moduleName != moduleName ) {
            modArr.push(data[i].moduleName);
        }

    }

    fillEDIT(modArr, 'module_edit', moduleName);

    $("#module_edit").select2({}).on("select2:selecting", function (e) { if (e.params.args.data) { getSubModuleEDIT(e.params.args.data.id); } })

    //$("#module").select2({}).on("select2-selecting", function (e) { if (e.val) { getSubModule(e.val); } })

}

//Fill submodule Dropdown by module selection
function getSubModuleEDIT(selectedModule) { // GET SUBMODULE AGAINST MODULE
   
    var data = allRptInitData;
    var subMod = [];

    for (var i = 0; i < data.length; i++) {

        if (data[i].moduleName == selectedModule && subMod.indexOf(data[i].subModuleName) < 0 ) {
            subMod.push(data[i].subModuleName);
        }
    }
    
        fillEDIT(subMod, 'submodule_edit', 'select Sub Module');
    // $("#sel_genrpt_sub_module").select2({}).off("select2-selecting").on("select2-selecting", function (e) { if (e.val) { getAllRptAgainstSubmod(selectedModule, e.val); } })


}
////Fill select  common function
function fillEDIT(data, select2Id, firstText) { // A COMMON FUNCTION 
    $('#' + select2Id).html(''); //clear select box

    $('#' + select2Id).append('<option value="' + firstText +'">' + firstText + '</option>');

    $.each(data, function (index, value) {
        $('#' + select2Id).append(
            '<option value="' + value + '">' + value + '</option>'
        );
    });
}

function onEditReport() {
  
    var charttype = "";
    var result = [];
    var yAxisLabel = [];
    $("#edit_chart_type option:selected").each(function () {
        result.push(this.text);
        charttype = result.join(';');
        console.log("CHART TYPE " + charttype);
    });
    var filter_groups = "";
    var resultgrp = [];
    $("#filter_attr_edit option:selected").each(function () {
        resultgrp.push(this.text);
        filter_groups = resultgrp.join(';');
        console.log("filter_groups " + filter_groups);
    });
    var reportId = $('#edit_report_ID').val();
    var moduleName = $('#module_edit').val();
    var subModuleName = $('#submodule_edit').val();
    var report_description = $('#edit_report_description').val();
    var reportName = $('#edit_report_name').val();
    var formula = $('#edit_formula').val();
    var technical_explanation = $('#edit_technical_explanation').val();
    var remarks = $('#edit_remarks').val();
    var detail_query = $('#edit_detail_query').val();
    var chart_query = $('#edit_chart_query').val();
    var group_by;
    if ($('#group_by_edit').val() == null || $('#group_by_edit').val() == '')
        group_by = '';
    else
     group_by = $('#group_by_edit').val().join(';');
    var axisType = $("#edit_axis_type").val();
    var x_label = $("#edit_x_label").val();
    var ySide = $("#y_side_EDIT").val();
    var title = $("#y_title_EDIT").val();
    var valueSuffix = $("#y_val_suff_EDIT").val();
    var columnName = $("#y_col_name_EDIT").val();
    var y_lable_obj = {
        "ySide": ySide, "title": title, "valueSuffix": valueSuffix, "columnName": columnName
    }
    var ySide_mul = $("#y_side_mul_EDIT").val();
    var title_mul = $("#y_title_mul_EDIT").val();
    var valueSuffix_mul = $("#y_val_suff_mul_EDIT").val();
    var columnName_mul = $("#y_col_name_mul_EDIT").val();
    console.log("columnName_mul " + columnName_mul);
    var y_lable_obj_mul = {
        "ySide": ySide_mul, "title": title_mul, "valueSuffix": valueSuffix_mul, "columnName": columnName_mul
    }
    if (axisType == 'Multiple' || axisType == 'multiple') {
        yAxisLabel.push(y_lable_obj);
        yAxisLabel.push(y_lable_obj_mul);
    }
    else
        yAxisLabel.push(y_lable_obj);
    var active = $("#edit_active").val();
    if (active == 1)
        active = true;
    else
        active = false;
    console.log(" Y AXIS LABLE " + JSON.stringify(yAxisLabel));
    var inputVar = {
        "reportId": reportId,
        "reportName": reportName,
        "moduleName": moduleName,
        "subModuleName": subModuleName,
        "reportDescription": report_description,
        "formula": formula,
        "technicalExplaination": technical_explanation,
        "remarks": remarks,
        "detailQuery": detail_query,
        "chartQuery": chart_query,
        "selectGroupBy": group_by,
        "filterAttribute": filter_groups,
        "chartType": charttype,
        "axisType": axisType,
        "xAxisLabel": x_label,
        "yAxisLabel": JSON.stringify(yAxisLabel),
        "columnChart": "",
        "columnDetailQuery": "",
        "active": active,
        "modifiedBy": signumGlobal
    }
    var dataParamUpdateReport = JSON.stringify(inputVar);
    console.log("dataParamUpdateReport edit " + dataParamUpdateReport);
    if (reportName == "" || moduleName == "" || subModuleName == "" || report_description == "" || formula == "" || technical_explanation == "" || remarks == "" || detail_query == "" || chart_query == "" || group_by == "" || validateQueryEditChk == 1 || validateChartQueryChk == 1) {
        alert("please fill all the inputs!");
        if (validateQueryEditChk == 1)
            alert("Detail Query not validated");
        else
            console.log("Detail Query  validated");
        if (validateChartQueryChk == 1)
            alert("Chart Query not validated");
        else
            console.log("Chart Query  validated");
    }
    else {
        var request = $.isf.ajax({
            url: service_java_URL + "reportManagement/updateReport",
            type: "POST",
            context: this,
            returnAjaxObj: true,
            crossdomain: true,
            processData: true,
            contentType: "application/json; charset=utf-8",
            data: dataParamUpdateReport,
            xhrFields: {
            withCredentials: false
             }
           
        });

        request.done(function (msg) {
            alert('Report ' + reportName + ' has been updated Successfully!!!');           
           
            getRptDetailsSubmit();
            funcCallReset();
            $('#modalEditReport').modal('toggle');
        });

        request.fail(function (jqXHR, textStatus) {
            alert(' Error !!!Report' + reportName + 'could not be updated !!!');
        });

    }

}


function validateQueryEDIT() {
    var detailQuery = $('#edit_detail_query').val();
    $('#group_by_edit')
        .find('option')
        .remove();
    $('#filter_attr_edit')
        .find('option')
        .remove();
    if (detailQuery == null || detailQuery == '') {
        alert("please enter query");

    }
    else {
        var requestParameters = {
            "detailQuery": detailQuery
        };
        var dataparam = JSON.stringify(requestParameters);
        console.log("dataparam " + dataparam);
        //alert(dataparam);
        var jqXHR = $.isf.ajax({
            url: service_java_URL + "reportManagement/validateDetailedQuery",
            context: this,
            crossdomain: true,
            returnAjaxObj: true,
            processData: true,
            contentType: "application/json; charset=utf-8",
            type: 'POST',
            data: dataparam,
            xhrFields: {
                withCredentials: false
            },
        });
        jqXHR.done(function (data) {
            alert("Valid Query!!! You may proceed ahead to save this Report");
            // console.log(data);
            addGroupByEDIT(dataparam);
            addFilterEDIT(dataparam);
            validateQueryEditChk = 0;
            document.getElementById('edit_detail_query').readOnly = true;
            $('#validate_query_edit').hide();
            $('#Change_query_edit').show();
            document.getElementById('group_by_edit').disabled = false;
            document.getElementById('filter_attr_edit').disabled = false;
        });
        jqXHR.fail(function (jqXHR, textStatus) {
            alert("Request Failed ,query not valid" + textStatus);
            validateQueryEditChk = 1;
        });



    }

}


function validateQueryEDITforFilterPop() {
    var detailQuery = $('#edit_detail_query').val();
   
    if (detailQuery == null || detailQuery == '') {
        alert("please enter query");

    }
    else {
        var requestParameters = {
            "detailQuery": detailQuery
        };
        var dataparam = JSON.stringify(requestParameters);
        console.log("dataparam " + dataparam);
        //alert(dataparam);
        var jqXHR = $.isf.ajax({
            url: service_java_URL + "reportManagement/validateDetailedQuery",
            context: this,
            returnAjaxObj: true,
            crossdomain: true,
            processData: true,
            contentType: "application/json; charset=utf-8",
            type: 'POST',
            data: dataparam,
            xhrFields: {
                withCredentials: false
            },
        });
        jqXHR.done(function (data) {
            console.log("Valid Query!!! You may proceed ahead to save this Report");
            // console.log(data);
            addGroupByEDITforFilterPop(dataparam);
            addFilterEDITforFilterPop(dataparam);
            validateQueryEditChk = 0;
          
        });
        jqXHR.fail(function (jqXHR, textStatus) {
            alert("Request Failed ,query not valid" + textStatus);
            validateQueryEditChk = 1;
        });



    }

}


function addGroupByEDITforFilterPop(dataparam) {
    $('#group_by_edit')
        .find('option')
        .remove();
    var groupByval = [];
    $.isf.ajax({
        url: service_java_URL + "reportManagement/getReportGroupBy",
        context: this,
        crossdomain: true,
        processData: true,
        contentType: 'application/json',
        type: 'POST',
        data: dataparam,
        xhrFields: {
            withCredentials: false
        },
        success: function (data) {

            console.log("DATA fetched successfully " + data);
            for (var i = 0; i < data.length; i++) {
                console.log(data[i]);
                $('#group_by_edit').append('<option value="' + data[i] + '">' + data[i] + '</option>');
            }
            
            var groupBy = $('#edit_group_by').val();
            groupByval = groupBy.split(';');
            console.log("groupByval "+groupByval);
            $("#group_by_edit").val(groupByval).change();

           
        },
        error: function (xhr, status, statusText) {
            alert("Group by CAN NOT BE FETCHED");
            console.log('An error occurred on : createWO' + xhr.error);
        }
    });
}

function addFilterEDITforFilterPop(dataparam) {
    
    $('#filter_attr_edit')
        .find('option')
        .remove();
    var filterAttrval = [];
    $.isf.ajax({
        url: service_java_URL + "reportManagement/getFilters",
        context: this,
        crossdomain: true,
        processData: true,
        contentType: 'application/json',
        type: 'POST',
        data: dataparam,
        xhrFields: {
            withCredentials: false
        },
        success: function (data) {

            console.log("DATA fetched successfully " + data);
            var filters = Object.keys(data);
            for (var i = 0; i < filters.length; i++) {
                console.log(data[i]);
                $('#filter_attr_edit').append('<option value="' + filters[i] + '">' + filters[i] + '</option>');
            }
           
            var filterAttr = $('#edit_filter_attr').val();
            filterAttrval = filterAttr.split(';');
            console.log("filterAttrval " + filterAttrval);
            $("#filter_attr_edit").val(filterAttrval).change();
        },
        error: function (xhr, status, statusText) {
            alert("Filter CAN NOT BE FETCHED");
            console.log('An error occurred on : createWO' + xhr.error);
        }
    });
}




function ChangeQueryEDIT() {
    document.getElementById('edit_detail_query').readOnly = false;
    $('#validate_query_edit').show();
    $('#Change_query_edit').hide();
    document.getElementById('group_by_edit').disabled = true;
    document.getElementById('filter_attr_edit').disabled = true;
    validateQueryEditChk = 1;
}


function addGroupByEDIT(dataparam) {
    $.isf.ajax({
        url: service_java_URL + "reportManagement/getReportGroupBy",
        context: this,
        crossdomain: true,
        processData: true,
        contentType: 'application/json',
        type: 'POST',
        data: dataparam,
        xhrFields: {
            withCredentials: false
        },
        success: function (data) {

            console.log("DATA fetched successfully " + data);
            for (var i = 0; i < data.length; i++) {
                console.log(data[i]);
                $('#group_by_edit').append('<option value="' + data[i] + '">' + data[i] + '</option>');
            }

        },
        error: function (xhr, status, statusText) {
            alert("Group by CAN NOT BE FETCHED");
            console.log('An error occurred on : createWO' + xhr.error);
        }
    });
}

function addFilterEDIT(dataparam) {
    $.isf.ajax({
        url: service_java_URL + "reportManagement/getFilters",
        context: this,
        crossdomain: true,
        processData: true,
        contentType: 'application/json',
        type: 'POST',
        data: dataparam,
        xhrFields: {
            withCredentials: false
        },
        success: function (data) {

            console.log("DATA fetched successfully " + data);
            var filters = Object.keys(data);
            for (var i = 0; i < filters.length; i++) {
                console.log(data[i]);
                $('#filter_attr_edit').append('<option value="' + filters[i] + '">' + filters[i] + '</option>');
            }

        },
        error: function (xhr, status, statusText) {
            alert("Filter CAN NOT BE FETCHED");
            console.log('An error occurred on : createWO' + xhr.error);
        }
    });
}


function validateChartQueryEDIT() {
    var validate = true;
    var chartQuery = $('#edit_chart_query').val();
    var group_by = $('#group_by_edit').val();
    var detailQuery = $('#edit_detail_query').val();
    if (chartQuery == null || chartQuery == '') {
        alert("Please enter Chart Query");
        validate = false;
    }
    else
    console.log("VALIDATE chartQuery " + validate);
    if (detailQuery == null || detailQuery == '') {
        alert("Please enter Detail Query");
        validate = false;
    }
    else
        console.log("VALIDATE detailQuery " + validate);
    if (group_by == null || group_by == '') {
        alert("Please enter Group by " + group_by);
        validate = false;
    }
    else
        console.log("VALIDATE group_by " + validate + ' ' + group_by);
    if (validate == true) {
        var selectGroupBy = group_by.join(';');
        var validateChartQuery = {
            "chartQuery": chartQuery,
            "detailQuery": detailQuery,
            "selectGroupBy": selectGroupBy
        }
        var dataparam = JSON.stringify(validateChartQuery);
        console.log(dataparam);

        $.isf.ajax({
            url: service_java_URL + "reportManagement/previewReport",
            context: this,
            crossdomain: true,
            processData: true,
            contentType: 'application/json',
            type: 'POST',
            data: dataparam,
            xhrFields: {
                withCredentials: false
            },
            success: function (data) {

                console.log("DATA fetched successfully " + JSON.stringify(data));
                alert("Chart query validated successfully");
                validateChartQueryChk = 0;
                console.log("validate chart query " + validateChartQueryChk);
                $('#changeChartQuery_ForFilter_edit').show();
                $('#validate_chart_query_edit').hide();
                document.getElementById('edit_chart_query').readOnly = true;
                document.getElementById('y_col_name_EDIT').disabled = false;
                document.getElementById('y_col_name_mul_EDIT').disabled = false;

            },
            error: function (xhr, status, statusText) {
                alert("Chart query Not valid ");
                console.log('An error occurred on : createWO' + xhr.error);
                validateChartQueryChk = 1;
            }
        });


        console.log("VALIDATION  COMPLETE" + validate);
    }
    else
        console.log("VALIDATION NOT COMPLETE" + validate);

}

function changeChartQueryForFilterEDIT() {
    validateChartQueryChk = 1;
    editclicked = 0;
    document.getElementById('y_col_name_EDIT').disabled = true;
    document.getElementById('y_col_name_mul_EDIT').disabled = true;
    $('#changeChartQuery_ForFilter_edit').hide();
    $('#validate_chart_query_edit').show();
    document.getElementById('edit_chart_query').readOnly = false;
}




