//Document Ready method is in Dashboard.cshtml page


function loadFilters()
{
    var idsArray = [];
    var arrDistinct = [];
        $.isf.ajax({
            url: service_java_URL + "/projectManagement/getDashboardProjects",
            success: function (data) {
                $.each(data, function (i, d) {
                    if ($.inArray(d.ProjectID, idsArray) == -1)
                    {
                        idsArray.push(d.ProjectID);
                        arrDistinct.push(d);
                    }
                });
                $.each(arrDistinct,function (i, d) {
                    $('#selectProjectDashboard').append('<option value="' + d.ProjectID + '/' + d.ProjectName + '&&Project Manager : ' + d.ProjectCreator + ', CPM : ' + d.CPM + ' , PCODE : ' + d.PCode + '">' + d.ProjectID + '/' + d.ProjectName + '</option>');                    
                });
            },
            error: function (xhr, status, statusText) {
                console.log('An error occurred on getProject: ' + xhr.error);
            }
        });
}
function loadDashboard() {
    if (validateFilters($("#selectProjectDashboard").val(), $('#sdate').val(), $("#edate").val())) {
        projectID = ($("#selectProjectDashboard").val().split("&&")[0]).split("/")[0];
        $("#btnSlideFilter1").css("display", "block");
        closeNav();
        //showHideBtn();
        $("#divProjectDetails").css("display", "block");
        $("#lblProjectHeading").text($("#selectProjectDashboard").val().split("&&")[0]);
        $("#lblselectedProjDetails").text($("#selectProjectDashboard").val().split("&&")[1] + " , Report Duration :" + $('#sdate').val() + " to " + $("#edate").val());
        $("#divDashboardPanel").css("display", "block");

        loadTiles();
        fillActivityDropDown();
        commonAjaxCall(5, 'chart_plot_01');
        commonAjaxCall(3, 'chart_plot_02');
        commonAjaxCall(4, 'chart_plot_03');

        commonAjaxCall(2, 'chart_plot_05_status');
        commonAjaxCall(2, 'chart_plot_05_weekly');
        commonAjaxCall(2, 'chart_plot_05_resource');
        commonAjaxCall(2, 'chart_plot_05_activity');

    
    }
    
   
}
function tempChart() {
    rptName = "Delivery Execution";
    moduleName = "Delivery Execution";
    subModuleName = "Project Work FLows";
    groupBy = "subactivity";
    filterQuery = "";
    var requestArr = {
        "reportName": rptName,
        "moduleName": moduleName,
        "subModuleName": subModuleName,
        "selectGroupBy": groupBy,
        "filterAttribute": filterQuery
    };

    var request = $.isf.ajax({
        url: service_java_URL + "reportManagement/generateReport",
        returnAjaxObj: true,
        success: function (data) {
            
                createChart(data, groupBy, rptName);
            

        },
        method: "POST",
        contentType: "application/json; charset=utf-8",
        data: JSON.stringify(requestArr),
        dataType: "json"
    });
}

function createChart(data,divID, chartName) {
    var chartdiv = '#' + divID;
    if (!data.data.length) {
        $(chartdiv).html('<span style="display:block;text-align:center;padding-top:20px;">No Data Found</span>');
        return true;
    }

    var groupBy = data.conf.selectGroupBy;

    yAxisLabel = data.conf.yAxisLabel;
    xAxisLabel = data.conf.xAxisLabel;
    var chartType = "column";

    var callStackFlag = false;
    if (chartType == 'stack') {
        callStackFlag = true;
        chartType = 'column';
    }

    var _categories = [],
        _seriesData = [];

    var _data = data.data;
    var groupByUpper = data.conf.selectGroupBy.toUpperCase();

    for (key in _data) {

        for (key2 in _data[key]) {
            var key2Upper = key2.toUpperCase();
            if (jQuery.trim(key2Upper) == jQuery.trim(groupByUpper)) {
                _categories.push(_data[key][key2]);
            } else {

                if (!_seriesData[key2]) { _seriesData[key2] = []; }

                _seriesData[key2].push(_data[key][key2]);
            }

        }

    }


    yAxisValuesFromApi = JSON.parse(yAxisLabel);

    var createYaxis = [];

    for (var yxKey in yAxisValuesFromApi) {
        if (yAxisValuesFromApi[yxKey]['ySide'] == 1) {
            createYaxis.push({ labels: { format: '{value}' + yAxisValuesFromApi[yxKey]['valueSuffix'] }, title: { text: yAxisValuesFromApi[yxKey]['title'] }, opposite: true });
        } else {
            createYaxis.push({ title: { text: yAxisValuesFromApi[yxKey]['title'] } });
        }
    }


    var seriesDataArr = [];
    var getYaxisDetails = [];
    for (key3 in _seriesData) {

            var retArr = [];
            for (var yxKey in yAxisValuesFromApi) {

                if (yAxisValuesFromApi[yxKey]['columnName'] == key3) {
                    if (yAxisValuesFromApi[yxKey]['ySide'] == 1) {
                        retArr = { yAxis: yAxisValuesFromApi[yxKey]['ySide'], valueSuffix: yAxisValuesFromApi[yxKey]['valueSuffix'], type: 'spline' };

                    } else {
                        retArr = { yAxis: yAxisValuesFromApi[yxKey]['ySide'], valueSuffix: yAxisValuesFromApi[yxKey]['valueSuffix'], type: data.conf.chartType };//---
                    }
                }
            }

   
            getYaxisDetails = retArr;

        if (getYaxisDetails['yAxis'] == 0 || getYaxisDetails['yAxis'] == 1) {
            seriesDataArr.push({
                'name': key3, 'data': _seriesData[key3], 'yAxis': getYaxisDetails['yAxis'], 'type': getYaxisDetails['type'], tooltip: {
                    valueSuffix: getYaxisDetails['valueSuffix']
                }
            });
        } else {
            seriesDataArr.push({ 'name': key3, 'data': _seriesData[key3] });
        }
    }
    


   
    //if (callStackFlag) {
    //    changeChartTypeToStack(myChart);
    //}
}
function changeChartTypeToStack(chartObj) {
    chartObj.update({
        plotOptions: {
            series: {
                stacking: 'normal'
            }
        }
    });
}
//function createPieChart(data, divID, chartName) {
//    var chartdiv = '#' + divID;
//    var _data = data.data;
//    var groupByUpper = data.conf.selectGroupBy.toUpperCase();
//    var _pieData = [];
//    for (key in _data) {
        
//        for (key2 in _data[key]) {
//            if (key2 == 'ProjectID' || key2 == 'ProjectName'){}else{
//            var key2Upper = key2.toUpperCase();
//            _pieData.push([key2Upper,_data[key][key2]]);
//            }
//        }
//    }



//}
function createTable(maindata, divID, chartName) {
     divID = '#' + divID;
    var data = maindata.data;
    var headerTR = '';
    var tBodyTR = '';
    var incrementJ = 0;

    if (data.length) {
        for (var r in data) {
            if (incrementJ == 0) {
                for (var colName in data[r]) {
                    headerTR += '<th>' + colName + '</th>';
                }
            }
            var innerTR = '';
            for (var colVal in data[r]) {
                innerTR += '<td>' + data[r][colVal] + '</td>';
            }
            tBodyTR += '<tr>' + innerTR + '</tr>';
            incrementJ++;
        }
        headerTR = '<thead><tr>' + headerTR + '</tr></thead>';
        tBodyTR = '<tbody>' + tBodyTR + '</tbody>';        
        var finalTable = '<table id="tableOperational" class="demo-placeholder table table-condensed" style="width: 97%;margin-left: 10px;">' + headerTR + tBodyTR + '</table>';
        $(divID).html(finalTable);

        
    } else {
        $(divID).html('<span style="display:block;text-align:center;padding-top:20px;">No Data Found</span>');
    }
}
//***********************
function fillActivityDropDown() {
    $.isf.ajax({        
            url: service_java_URL + "activityMaster/getActivityByProjectId?projectId="+ projectID,
            success: function (data) {
                $.each(data, function (i, d) {
                    $('#selectActivityID').append('<option value="' + d.Activity + '">' + d.Activity + '</option>');
                });
                },
                error: function (xhr, status, statusText) {
                    console.log('An error occurred: ' + xhr.error);
                }
            });
}
function loadTiles() {
    $("#divTilesContainer").html("");
    var serviceUrl = service_java_URL + "reportManagement/getDashboardData?dashboardId=1&sequence=1&projectId=" + projectID + "&fromDate=" + $('#sdate').val() + "&toDate=" + $('#edate').val();
    if (ApiProxy == true) {
        serviceUrl = service_java_URL + "reportManagement/getDashboardData?" + encodeURIComponent("dashboardId=1&sequence=1&projectId=" + projectID + "&fromDate=" + $('#sdate').val() + "&toDate=" + $('#edate').val());
    }
   
    $.isf.ajax({
        url: serviceUrl,
        success: function (data) {
            var tile = "";
            $.each(data.data, function (i, d) {
                if(i == 0)
                    tile = '<div class="col-md-2 col-sm-4 col-xs-6 tile_stats_count" style="background: -webkit-gradient(linear, left top, left bottom, from(#784583), to(#953783));">';
                if (i == 1)
                    tile = '<div class="col-md-2 col-sm-4 col-xs-6 tile_stats_count" style="background: -webkit-gradient(linear, left top, left bottom, from(#008866), to(#029FC4));">';
                if (i == 2)
                    tile = '<div class="col-md-2 col-sm-4 col-xs-6 tile_stats_count" style="background: -webkit-gradient(linear, left top, left bottom, from(#6CA738), to(#88B916));">';
                if (i == 3)
                    tile = '<div class="col-md-2 col-sm-4 col-xs-6 tile_stats_count" style="background: -webkit-gradient(linear, left top, left bottom, from(#ef3a07), to(#e37618));">';
                if (i == 4)
                    tile = '<div class="col-md-2 col-sm-4 col-xs-6 tile_stats_count" style="background: -webkit-gradient(linear, left top, left bottom, from(#03555d), to(#5358ea));">';
                if (i == 5)
                    tile = '<div class="col-md-2 col-sm-4 col-xs-6 tile_stats_count" style="background: -webkit-gradient(linear, left top, left bottom, from(#2d5a05), to(#6b9014));">';
                

                if(i<6){
                    tile = tile+'<span class="count_top"> ' + d.header + '</span>'
                               + '<div class="count"><!--<i class="fa fa-star" style="color:orange"></i>-->' + d.value + '</div>'
                               + '<span class="count_bottom"><i class="green"><i class="' + d.footer + '"></i>&nbsp;</span>'
                           + '</div>'
                $("#divTilesContainer").append(tile);
                }
            })
         
        },
        error: function (xhr, status, statusText) {
            console.log('An error occurred: ' + xhr.error);
        }
    });
   
    
}
function tilesProjectTeamPyramid() {

}
function workOrderExecTrends() {

}
function costTimeTrends() {

}
function deliveryReadinessTrends() {

}
function plannedvsActualTrends() {

}
function deliveryQualityPieChart() {

}
function subActivityAutomationPieChart() {

}
function subActivitySection() {

}
function commonAjaxCall(sequenceID, divID) {
    
    if (sequenceID == '7' || sequenceID == '6') {
        var serviceUrl = service_java_URL + "reportManagement/getDashboardData?" + "dashboardId=1&sequence=" + sequenceID + "&projectId=" + projectID;
        if (ApiProxy == true) {
            serviceUrl = service_java_URL + "reportManagement/getDashboardData?" + encodeURIComponent("dashboardId=1&sequence=" + sequenceID + "&projectId=" + projectID);
        }
        $.isf.ajax({
            url: serviceUrl,
            success: function (data) {
                if (data.conf.chartType == "pie") {
                    createPieChart(data, divID, data.conf.description);
                } else {
                    createChart(data, divID, data.conf.description);
                }
            },
            error: function (xhr, status, statusText) {
                console.log('An error occurred: ' + xhr.error);
            }
        });
    } else {
        var serviceUrl = service_java_URL + "reportManagement/getDashboardData?" + "dashboardId=1&sequence=" + sequenceID + "&projectId=" + projectID + "&fromDate=" + $('#sdate').val() + "&toDate=" + $('#edate').val();
        if (ApiProxy == true) {
            serviceUrl = service_java_URL + "reportManagement/getDashboardData?" + encodeURIComponent("dashboardId=1&sequence=" + sequenceID + "&projectId=" + projectID + "&fromDate=" + $('#sdate').val() + "&toDate=" + $('#edate').val());
        }
        $.isf.ajax({
            url: serviceUrl,
            success: function (data) {
                if (data.conf.chartType == "pie") {
                    createPieChart(data, divID, data.conf.description);
                }else if(data.conf.chartType=="table"){
                    createTable(data, divID, data.conf.description);
                } else {
                    createChart(data, divID, data.conf.description);
                }
            },
            error: function (xhr, status, statusText) {
                console.log('An error occurred: ' + xhr.error);
            }
        });
    }
    
}
function showHideBtn() {
    
    if (($('#caretImg1').attr('class') == 'fa fa-caret-down') || ($('#caretImg2').attr('class') == 'fa fa-caret-down')) {
        $("#divDashboardFilters").slideDown(700);
        $("#caretImg1").removeClass();
        $("#caretImg1").addClass("fa fa-caret-up");
        $("#caretImg2").removeClass();
        $("#caretImg2").addClass("fa fa-caret-up");
    }else{
        $("#divDashboardFilters").slideUp(700);
        $("#caretImg1").removeClass();
        $("#caretImg1").addClass("fa fa-caret-down");
        $("#caretImg2").removeClass();
        $("#caretImg2").addClass("fa fa-caret-down");
    }   
}
function validateFilters(projectVal, startdate, enddate) {
    $('#projectValid').text("");
    $('#startDateValid').text("");
    $('#endDateValid').text("");
    var startDate = new Date(startdate);
    var endDate = new Date(enddate);
    var today = new Date();
    var dd = today.getDate();
    var mm = today.getMonth()+1;
    var yyyy = today.getFullYear();

    today = dd + '/' + mm + '/' + yyyy;
    today = new Date(today);
    if (projectVal == "" || projectVal == "undefined" || projectVal == null) {
        $('#projectValid').text("Project not selected");
        return false;
    }
    else if (startdate == "" || startdate == "undefined" || startdate == null) {
        $('#startDateValid').text(" Start date not selected");
        return false;
    }
    else if (enddate == "" || enddate == "undefined" || enddate == null) {
        $('#endDateValid').text(" End date not selected");
        return false;
    }
    else if (startDate > endDate) {
            $('#endDateValid').text(" End date must be after Start date");
            return false;
    }
    //else if (startdate > today) {
    //    $('#startDateValid').text(" Start date cannot be in future");
    //    return false;
    //}
    else {        
        return true;
    }
}