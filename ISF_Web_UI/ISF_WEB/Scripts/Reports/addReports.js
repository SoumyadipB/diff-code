
var allRptInitData;
var abc = 0;

function getFilterattributes() {
    //var filterGroupId = document.getElementById("filter_attr").value;

    var jqXHR = $.isf.ajax({
        url: service_java_URL + "reportManagement/getFilterGroups/",
        datatype: "json",
        returnAjaxObj: true
    });

    jqXHR.done(function (data) {
        $.each(data, function (i, d) {
            //$('#filter_attr').append('<option value="' + d.filterGroup + '">' + d.filterDescription + '</option>');
        });
    });

    jqXHR.fail(function (jqXHR, textStatus) {
        alert("(report) Request failed: " + textStatus);
    });

}

//Load Initial data for Module, submodule and report details
function getRptInitForAdd() {

    var finalArr = {};
    finalArr['Module'] = []; finalArr['subModule'] = []; finalArr['rpt'] = [];

    var request = $.isf.ajax({
        url: service_java_URL + "reportManagement/getReportInitData/",
        dataType: "json",
        returnAjaxObj: true
    });

    request.done(function (msg) {


        allRptInitData = msg.MODULE_SUBMODULE;
        getModule(allRptInitData);

        //Collaps report options by default
        // $(".panel-heading span.clickable").trigger("click");
    });

    request.fail(function (jqXHR, textStatus) {
        alert("(getReportInitData) Request failed: " + textStatus);
    });

}

//Fill Module Dropdown 
function getModule(data) { // DISPLAY ALL MODULES
    var modArr = [];

    for (var i = 0; i < data.length; i++) {
        if (modArr.indexOf(data[i].moduleName) < 0) {
            modArr.push(data[i].moduleName);
        }

    }

    fill(modArr, 'module', 'Select Module');

    $("#module").select2({}).on("select2:selecting", function (e) { if (e.params.args.data) { getSubModule(e.params.args.data.id); } })

    //$("#module").select2({}).on("select2-selecting", function (e) { if (e.val) { getSubModule(e.val); } })

}

//Fill submodule Dropdown by module selection
function getSubModule(selectedModule) { // GET SUBMODULE AGAINST MODULE
    var data = allRptInitData;
    var subMod = [];

    for (var i = 0; i < data.length; i++) {

        if (data[i].moduleName == selectedModule && subMod.indexOf(data[i].subModuleName) < 0) {
            subMod.push(data[i].subModuleName);
        }
    }

    fill(subMod, 'subModule', 'Select Sub-Module');

    // $("#sel_genrpt_sub_module").select2({}).off("select2-selecting").on("select2-selecting", function (e) { if (e.val) { getAllRptAgainstSubmod(selectedModule, e.val); } })


}
////Fill select  common function
function fill(data, select2Id, firstText) { // A COMMON FUNCTION 
    $('#' + select2Id).html(''); //clear select box

    $('#' + select2Id).append('<option value="">' + firstText + '</option>');

    $.each(data, function (index, value) {
        $('#' + select2Id).append(
          '<option value="' + value + '">' + value + '</option>'
        );
    });
}

//Validate detail query
function validateQuery() {
    var detailQuery = $('#detail_query').val();
    $('#group_by')
        .find('option')
        .remove();
    $('#filter_attr')
        .find('option')
        .remove();
    if (detailQuery == null || detailQuery == '')
    {
        alert("please enter query");

    }
    else{
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
        processData: true,
        returnAjaxObj: true,
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
        document.getElementById('detail_query').readOnly = true;
        $('#validate_query').hide();
        $('#Change_query').show();
        document.getElementById('group_by').disabled = false;
        document.getElementById('filter_attr').disabled = false;
        
        addGroupBy(dataparam);
        addFilter(dataparam);
    });
    jqXHR.fail(function (jqXHR, textStatus) {
        alert("Request Failed ,query not valid" + textStatus);
    });

    
   
    }

}
//Submit or save report in db 
function ChangeQuery()
{
    document.getElementById('detail_query').readOnly = false;
    $('#validate_query').show();
    $('#Change_query').hide();
    document.getElementById('group_by').disabled = true;
    document.getElementById('filter_attr').disabled = true;
}

function addGroupBy(dataparam)
{
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
                $('#group_by').append('<option value="' + data[i] + '">' + data[i] + '</option>');
            }

        },
        error: function (xhr, status, statusText) {
            alert("Group by CAN NOT BE FETCHED");
            console.log('An error occurred on : createWO' + xhr.error);
        }
    });
}

function addFilter(dataparam) {
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
                $('#filter_attr').append('<option value="' + filters[i] + '">' + filters[i] + '</option>');
            }

        },
        error: function (xhr, status, statusText) {
            alert("Filter CAN NOT BE FETCHED");
            console.log('An error occurred on : createWO' + xhr.error);
        }
    });
}


function validateChartQueryForFilter()
{
    var validate = true;
    var chartQuery = $('#chart_query').val();
    var group_by = $('#group_by').val();
    var detailQuery = $('#detail_query').val();
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
        alert("Please enter Group by ");
        validate = false;
    }
    else
        console.log("VALIDATE group_by " + validate);
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
                abc = 1;
                console.log("validate chart query " + abc);
                $('#changeChartQuery_ForFilter').show();
                $('#validateChartQuery_ForFilter').hide();
                document.getElementById('chart_query').readOnly = true;
                document.getElementById('y_col_name').disabled = false;
                document.getElementById('y_col_name_mul').disabled = false;
            },
            error: function (xhr, status, statusText) {
                alert("Chart query Not valid ");
                console.log('An error occurred on : createWO' + xhr.error);
                abc = 0;
            }
        });


        console.log("VALIDATION  COMPLETE" + validate)
    }
    else
        console.log("VALIDATION NOT COMPLETE" + validate);

}

function changeChartQueryForFilter()
{
    abc = 0;
    document.getElementById("select0").selected = "true";
    document.getElementById('y_col_name').disabled = true;
    document.getElementById('y_col_name_mul').disabled = true;
    $('#changeChartQuery_ForFilter').hide();
    $('#validateChartQuery_ForFilter').show();
    document.getElementById('chart_query').readOnly = false;
}

function getChartfilters(axisTypeOP)
{

    $('#y_col_name')
        .find('option')
        .remove();
    $('#y_col_name_mul')
        .find('option')
        .remove();
    var chartQuery = $('#chart_query').val();
    var detailQuery = $('#detail_query').val();
    var selectGroupBy = $('#group_by').val().join(';');;
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
                if (axisTypeOP=='Single')
                    $('#y_col_name').append('<option value="' + data[i] + '">' + data[i] + '</option>');
                else
                {
                    $('#y_col_name').append('<option value="' + data[i] + '">' + data[i] + '</option>');
                    $('#y_col_name_mul').append('<option value="' + data[i] + '">' + data[i] + '</option>');
                }
           
        },
        error: function (xhr, status, statusText) {
            console.log('An error occurred on getCustomers' + xhr.responseText);
        }
    });
}

function submitReport() {
    
    var charttype = "";
    var result = [];
    var yAxisLabel=[];
    $("#chart_type option:selected").each(function () {
        result.push(this.text);
        charttype = result.join(';')        
    });
    var filter_groups = "";
    var resultgrp = [];
    $("#filter_attr option:selected").each(function () {
        resultgrp.push(this.text);
        filter_groups = resultgrp.join(';')
    });
    var reportName = $('#report_name').val();
    var module = $('#module').val();
    var subModule = $('#subModule').val();
    var report_description = $('#report_description').val();
    var formula = $('#formula').val();
    var technical_explanation = $('#technical_explanation').val();
    var remarks = $('#remarks').val();
    var detail_query = $('#detail_query').val();
    var chart_query = $('#chart_query').val();
    var group_by;
    if ($('#group_by').val() == null || $('#group_by').val() == '')
        group_by = '';
    else
     group_by = $('#group_by').val().join(';');
   //filter_groups = $("#filter_attr").val(),
   //chart_type = $("#chart_type").val(),
   
    var axisType = $("#axis_type").val();
    var x_label = $("#x_label").val();
    var ySide = $("#y_side").val();
    var title = $("#y_title").val();
    var valueSuffix = $("#y_val_suff").val();
    var columnName = $("#y_col_name").val();
    var y_lable_obj = {
        "ySide": ySide, "title": title, "valueSuffix": valueSuffix, "columnName": columnName
    }
    var ySide_mul = $("#y_side_mul").val();
    var title_mul = $("#y_title_mul").val();
    var valueSuffix_mul = $("#y_val_suff_mul").val();
    var columnName_mul = $("#y_col_name_mul").val();
    var y_lable_obj_mul = {
        "ySide": ySide_mul, "title": title_mul, "valueSuffix": valueSuffix_mul, "columnName": columnName_mul
    }
    var col_chart = $("#col_chart").val();
    var col_detail_query = $("#col_detail_query").val();
    var active = $("#active").val();
    if (axisType == 'Multiple')
    {
        yAxisLabel.push(y_lable_obj);
        yAxisLabel.push(y_lable_obj_mul);
    }
    else
        yAxisLabel.push(y_lable_obj);
    console.log(" Y AXIS LABLE "+JSON.stringify(yAxisLabel));
    var inputVar = {
        "reportName": reportName,
        "moduleName": module,
        "subModuleName": subModule,
        "reportDescription": report_description,
        "formula": formula,
        "technicalExplaination": technical_explanation,
        "remarks": null,
        "detailQuery": detail_query,
        "chartQuery": chart_query,
        "selectGroupBy": group_by,
        "filterAttribute": filter_groups,
        "aggColumn": null,
        "chartType": charttype,
        "axisType": axisType,
        "xAxisLabel": x_label,
        "yAxisLabel": JSON.stringify(yAxisLabel),
        "columnChart": col_chart,
        "columnDetailQuery": col_detail_query,
        "active": true,
        "createdBy": signumGlobal,
        "modifiedBy": signumGlobal
    }

    var dataparam = JSON.stringify(inputVar);
    console.log("submission of query " + dataparam);
    //alert(dataparam);
    if (reportName == "" || module == "" || subModule == "" || report_description == "" || formula == "" || technical_explanation == ""
        || detail_query == "" || chart_query == "" || chart_type == "" || axisType == "" || x_label == "" || yAxisLabel == null || active == "") {
        alert("please fill all the inputs!");
    }
    

    else {
        //validateChartQueryForFilter();
        var request = $.isf.ajax({
            url: service_java_URL + "reportManagement/addReport",
            context: this,
            crossdomain: true,
            returnAjaxObj: true,
            processData: true,
            contentType: "application/json; charset=utf-8",
            data: dataparam,
            type: 'POST',
            xhrFields: {
                withCredentials: false
            },

        });

        request.done(function (msg) {
            alert('Report has been added sucessfully in database!!!:)');
        });

        request.fail(function (jqXHR, textStatus) {
            alert('Submit failed!!');
        });

        // }
    }


}

//Preview  report data before saving in db 

function previewReportForm() {
    var reportName = $('#report_name').val(),
    module = $('#module').val(),
    subModule = $('#subModule').val(),
    report_description = $('#report_description').val(),
    formula = $('#formula').val(),
    technical_explanation = $('#technical_explanation').val(),
    remarks = $('#remarks').val(),
    detail_query = $('#detail_query').val(),
    select_group_by = $('#select_group_by').val(),
    filter_attr = $("#filter_attr").val(),
    chart_type = $("#chart_type").val(),
    axisType = $("#axis_type").val(),
    x_label = $("#x_label").val(),
    y_label = $("#y_label").val(),
    col_chart = $("#col_chart").val(),
    col_detail_query = $("#col_detail_query").val(),
    active = $("#active").val();
    var reportsAddArr = [];
    reportsAddArr.push({
        reportName: reportName, module: module, subModule: subModule, reportDescription: report_description, formula: formula,
        technicalExplaination: technical_explanation, remarks: remarks, detailQuery: detail_query, selectGroupBy: select_group_by,
        aggColumn: agg_col, filterAttribute: filter_attr, chartType: chart_type, axisType: axisType, xAxisLabel: x_label, yAxisLabel: y_label, columnChart: col_chart,
        columnDetailQuery: col_detail_query, active: active, createdBy: signumGlobal, modifiedBy: signumGlobal
    });

}


function cleanfields() {
    $('#report_name').val(''),
   $('#select_module').val(''),
   $('#select_subModule').val(''),
    $('#select_subModule').html('');
    $('#report_description').val(''),
       $('#formula').val(''),
       $('#technical_explanation').val(''),
    $('#remarks').val(''),
    $('#detail_query').val(''),
    $('#chart_query').val(''),
    $('#group_by').val(''),
    $("#group_by").val(''),
   $("#chart_type").val(''),
   $("#axis_type").val(''),
    $("#x_label").val(''),
   $("#y_label").val(''),
   $("#col_chart").val(''),
   $("#col_detail_query").val(''),
   $("#active").val('');

    getRptInitForAdd();

}


function yAxisAdd()
{
    console.log("validate chart query " + abc);
    var axisType = $('#axis_type').val();
    if (axisType == 'Multiple') {
        console.log("multiple");
        $("#y_axis_lab_mul").show();
        
        
    }
    else {
        console.log("single");
        $("#y_axis_lab_mul").hide();
        
    }
    if (abc > 0)
        getChartfilters(axisType);
    else
      alert("Chart Query not Validated");
}