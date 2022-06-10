
var allRptInitData;
var rptDetailsArr = [];
var generateRpt_chartTypeGlobal = '';
var chartTypeTable = 'table';
var filterQuery = "";
var filterIdPrefix = 'rptGenFlt_';
var globalFilterData = [];
var chartTypeConfig = '';
var marketAreaName = JSON.parse(ActiveProfileSession).organisation;
var accessProfileName = JSON.parse(ActiveProfileSession).accessProfileName;

//START- REPORT FILTERS /////////////////////////////////////////////



var fillSelectBoxVal = function (data, select2Id, firstText) {

    $('#' + select2Id).html(''); //clear select box

    //  $('#' + select2Id).append('<option value="">' + firstText + '</option>');

    $.each(data, function (value, caption) {

        if (select2Id == "rptGenFlt_ProjectName") {
            if (!marketAreaArray.includes(marketAreaName)) {
                $('#' + select2Id).append(
                    '<option value="' + value + '">' + caption + '</option>'
                );
            } else {
                if (caption != null && caption.includes(marketAreaName)) {
                    $('#' + select2Id).append(
                        '<option value="' + value + '">' + caption + '</option>'
                    );

                }


            }
        } else {
            $('#' + select2Id).append(
                '<option value="' + value + '">' + caption + '</option>'
            );
        }
    });

    if (marketAreaArray.includes(marketAreaName)) {
        $("#rptGenFlt_MarketAreaName option").filter(function () {
            return this.text == marketAreaName;
        }).attr('selected', true);

        $("#rptGenFlt_MarketAreaName").attr('disabled', 'disabled');
    }
    else
        $("#rptGenFlt_MarketAreaName").attr('disabled', false);

};

var getRptGenDateFld = function () {
    
    var dateRange = '<div class="col-sm-4 rptDateFilterRow">';
    dateRange += '      <div class="input-group input-daterange"> <label>*Start Date</label>';
    dateRange += '	    <input id="start_date_reports" type="text" class="form-control required/" >';
   
    dateRange += '	     <span class="input-group-addon">to</span> ';
    dateRange += '      <label>*End Date</label>';
    dateRange += '      <input id="end_date_reports" type="text" class="form-control required/" >';
   
    dateRange += '    </div>';
    dateRange += '</div>';

    return dateRange;

};

var appendDataToControl = function (colName,colValue, dataArr) {

    var dArr = {};
    var controlId = filterIdPrefix + colName;

    for (var i in dataArr) {
        dArr[dataArr[i][colValue]] = dataArr[i][colName];
    }
    
    fillSelectBoxVal(dArr, controlId, '');

};

var resetFiltersCall = function () {
    createFiltersHtml(globalFilterData);
};


var createControl = function (config, data) {

    if (config.length != 0)
        $('#reportsFilter').append('<div class="row">');


    for (var k in config) {

        if (config[k]['type'] == 'select') {

            var createSelect = $('<div class="col-sm-3 filterInput"></div>');
            var varLabel = $('<label style="white-space: nowrap;">' + config[k]['label'] + '</label>');

            var selID = filterIdPrefix + config[k]['colName'];
            var varSelect = $('<select id="' + selID + '" class="select2able" data-value-col-name="' + config[k]['colValue'] + '" data-control-name="' + config[k]['colName'] + '" multiple="multiple" ></select>');

            createSelect.append(varLabel, varSelect);

            $('#reportsFilter').append(createSelect);

            appendDataToControl(config[k]['colName'], config[k]['colValue'], data);

            $('#' + selID).select2({});
            //$('#' + selID).select2({}).on("change",
            //    function (e) { if (e) { callOnFilterChange(this, config, data); } });

        }
        if (config[k]['type'] == 'date') {
            $('#reportsFilter').append(getRptGenDateFld());

            $('#start_date_reports').datepicker({
                dateFormat:'yy-mm-dd'
                //pair: $('#end_date_reports'),
                //onChange: function (view, elements) { $('#end_date_reports').val(''); }

            });

            $('#end_date_reports').datepicker({
                dateFormat:'yy-mm-dd'
            });
        }
    }
    if (config.length != 0)
        $('#reportsFilter').append('</div>');
};


var createWhereCondition = function () {

    var whereStr = '';
    for (var i in globalFilterData) {

        var dt=globalFilterData[i]['config'];
        for (var k in dt) {

            if (dt[k]['type'] == 'select') {
                var ctlId = '#' + filterIdPrefix + dt[k]['colName'];
                var ctlVal = $(ctlId).val();
                if (ctlVal) {
                    ctlVal = ctlVal.toString();
                    var ctlValArr = ctlVal.split(',');
                    whereStr += dt[k]['colValue'] + " IN (";
                    for (var i in ctlValArr) {
                        if (i == 0)
                            whereStr += "'" + ctlValArr[i] + "'";
                        else
                            whereStr += ",'" + ctlValArr[i] + "'";
                    }
                    whereStr += ") AND ";
                }
            }
            if (dt[k]['type'] == 'date') {

                var dateFldName = dt[k]['colName'];
                if ($("#start_date_reports").val() != "" || $("#end_date_reports").val() != "") {
                    if ($("#start_date_reports").val() != "" && $("#end_date_reports").val() != "") {
                        whereStr += " "+dateFldName+" between '" + $("#start_date_reports").val() + "' AND '" + $("#end_date_reports").val() + "' AND ";
                    } else {
                        alert('Please correct date range');
                        return 'false';
                    }
                }
            }
        }

    }
    if (whereStr != '') {
        whereStr = whereStr.substring(0, whereStr.length - 4);
        whereStr = 'where ' + whereStr;
    }
   

    var UndefinedCount = (whereStr.match(/undefined/g) || []).length;
    if (UndefinedCount) { whereStr = ''; }
    return whereStr;
};

var createFiltersHtml = function (filterData) {
    $('#reportsFilter').html('');
    
    var filterCaption = '<div class="row"><div class="col-md-12"><span style="display: block;text-align: left;border-bottom: 1px solid #ccc;margin-bottom: 10px;font-size: 14px;">Filters</span></div></div>';
    $('#reportsFilter').append(filterCaption);

    

    for (var k in filterData) {
        //console.log('ABC ' + JSON.stringify(filterData[k]['data']));
            createControl(filterData[k]['config'], filterData[k]['data']);
    }

    //APPEND RESET BUTTON
    var resetBtn = $('<button class="btn btn-default" type="button" style="float:right;" title="Reset Filters">Reset</button>');
    resetBtn.on('click', function () { resetFiltersCall(); });

    $('#reportsFilter').append(resetBtn);

};

//END- REPORT FILTERS /////////////////////////////////////////////


function getChartTypeIconClassName(chartType) {
    var iconClassName = [];

    iconClassName['line'] = 'fa fa-line-chart';
    iconClassName['bar'] = 'fa fa-align-left';
    iconClassName['column'] = 'fa fa-bar-chart';
    iconClassName['area'] = 'fa fa-area-chart';
    iconClassName['pie'] = 'fa fa-pie-chart';
    iconClassName['stack'] = 'fa fa-th';
    iconClassName['heatmap'] = 'fa fa-th';
    iconClassName['boxplot'] = 'fa fa-sliders';
    iconClassName['pareto'] = 'fa fa-sliders';

    return iconClassName[chartType];
}

function createChartTypeIconsHtml(chartTypeIconsArr) {
    //CREATE CHART TYPE ICONS
    $('#generateRpt_toolbar').html('');
    var splitChartType = chartTypeIconsArr;
    
    var chartTypeHtml = '';
    for (var chtype = 0; chtype < splitChartType.length; chtype++) {
        var chartType = jQuery.trim(splitChartType[chtype]);

        if (chtype == 0) { generateRpt_chartTypeGlobal = chartType; }
        
        var btnClass = (chtype == 0) ? 'primary' : 'default';
        var iconClassName = getChartTypeIconClassName(chartType);

        chartTypeHtml += '<button type="button" class="btn btn-' + btnClass + '" aria-label="Right Align" data-charttype="' + chartType + '" title="' + chartType + ' Chart" ><i class="' + iconClassName + '" aria-hidden="true" ></i></button>';
    }

    $('#generateRpt_toolbar').html(chartTypeHtml);

    //BIND CLICK EVENT
    $("#generateRpt_toolbar.btn-group > .btn").click(function () {
        $(".btn-group > .btn").removeClass("btn-primary").addClass("btn-default");
        $(this).removeClass("btn-default").addClass("btn-primary");
        var chartType = $(this).data('charttype');
        generateRpt_chartTypeGlobal = chartType;

        $('.generateReport').trigger('click');
    });
}


function getRptNameAndDescription(data) {
    
    var retHtml='';
    retHtml+= data.reportName + ' (Search Options)';
    retHtml += '<span class="rpt_description"><br /><b>Report Description: </b>' + data.reportDescription + '</span>';
    return retHtml;
}

function getRptDetails(rptName, moduleName, subModuleName) { //GET REPORT DETAILS BASED ON MODULE,SUBMODULE,REPORT NAME
    var finalArr = {};
    
    var moduleName, subModule, rptName;
    moduleName = moduleName;
    subModuleName = subModuleName;
    rptName = rptName;
    var serviceUrl = service_java_URL + "reportManagement/getReportDetail?reportName=" + rptName + "&subModule=" + subModuleName + "&module=" + moduleName;
    if (ApiProxy == true) {
        serviceUrl = service_java_URL + "reportManagement/getReportDetail?reportName=" + encodeURIComponent(rptName + "&subModule=" + subModuleName + "&module=" + moduleName);
    }
    var request = $.isf.ajax({
        url: serviceUrl,
        returnAjaxObj: true,
        dataType: "json"
    });

    request.done(function (msg) {

        //START - REPORTS FILTERS CALL  
        if ($.trim(msg.filterAttribute) != "") {  
            var serviceUrl = service_java_URL + "reportManagement/getReportFilters?reportName=" + rptName + "&subModuleName=" + subModuleName + "&moduleName=" + moduleName ;
            if (ApiProxy == true) {
                serviceUrl = service_java_URL + "reportManagement/getReportFilters?reportName=" + encodeURIComponent(rptName + "&subModuleName=" + subModuleName + "&moduleName=" + moduleName);
            }
            var filterRequest = $.isf.ajax({
                url: serviceUrl,
                returnAjaxObj: true,
                dataType: "json",
                success: function (data, status, xhr) {
                    console.log(document.MarketArea);
                }
            });

            filterRequest.done(function (filtersData) {
                //console.log('XYZ ' + JSON.stringify(filtersData));
                globalFilterData = filtersData;
                createFiltersHtml(filtersData);
            });
            
            filterRequest.fail(function (jqXHR, textStatus) {
                alert("Request filter: " + textStatus);
            });
        } else {
            $('#reportsFilter').html('');
        }
        //END - REPORTS FILTERS CALL 
        
        rptDetailsArr = msg;

        $('#generateRptDownArea').show();
        $('#generateRptNameSelected').html(getRptNameAndDescription(msg));
        
        //GET CHART TYPE CONFIG
        chartTypeConfig = msg.chartTypeConfig;

        //CREATE HTML FOR GROUP BY 
        var fetchGroupBy = msg.selectGroupBy;
        var splitGroupBy = fetchGroupBy.split(';');

        var groupOptionDiv = '';

        for (var i = 0; i < splitGroupBy.length; i++) {
            var active = 'active';
            var displayVal = splitGroupBy[i].replace(/(\[|\])/g, "");
            groupOptionDiv += '<label class="btn btn-primary ' + active + '"> <input type="radio" name="groupByOptions" value="' + splitGroupBy[i] + '" autocomplete="off">&nbsp ' + displayVal + '</label>';
        }
               
        var groupByHtml = '<span style="display: block;text-align: left;border-bottom: 1px solid #ccc;margin-bottom: 10px;font-size: 14px;">Group By</span>' + groupOptionDiv;
        $('#generateRptGroupByDiv').html(groupByHtml);
 
        //GET CHART TYPES FROM API --
        var splitChartType = msg.chartType.split(';');
        //splitChartType = ['boxplot'];
        
        if (splitChartType.length == 1 && splitChartType[0] == chartTypeTable) {
            generateRpt_chartTypeGlobal = chartTypeTable;

            $('.nav-tabs a[href="#genRpt_chartView"]').hide();
            $('.nav-tabs a[href="#genRpt_tableView"]').tab('show');

        } else {
            $('.nav-tabs a[href="#genRpt_chartView"]').show();
            createChartTypeIconsHtml(splitChartType);
        }
               
        $('#generaterpt_container').html('');
        $('#generateRpt_table_view_div').html('');

    });

    request.fail(function (jqXHR, textStatus) {
        alert("Request failed: " + textStatus);
    });

}


function fillSelect2Box(data, select2Id,firstText) { // A COMMON FUNCTION 
    $('#' + select2Id).html(''); //clear select box

    if (firstText.length != 0)
    $('#' + select2Id).append('<option value="">' + firstText + '</option>');

 
    $.each(data, function (index, value) {
        if (value != null) {
            $('#' + select2Id).append(
                '<option value="' + value + '">' + value + '</option>'
            );
        }
        });
}


function getAllRptAgainstSubmod(module,subMod) { // GET REPORT AGAINST MODULE AND SUBMODULE

    var data = allRptInitData;
    var rpt = [];

    for (var i = 0; i < data.length; i++) {

        if (data[i].moduleName == module && data[i].subModuleName == subMod) {
            rpt.push(data[i].reportName);
        }
    }

    fillSelect2Box(rpt, 'sel_genrpt_report_name', 'Select Report');
    $("#sel_genrpt_report_name").select2({}).off("select2:selecting").on("select2:selecting", function (e) { if (e.params.args.data.id) { getRptDetails(e.params.args.data.id); } })
}

function getSubModuleRptInit(selectedModule) { // GET SUBMODULE AGAINST MODULE
    var data = allRptInitData;
    var subMod = [];
    for (var i = 0; i < data.length; i++) {

        if (data[i].moduleName == selectedModule && subMod.indexOf(data[i].subModuleName) < 0) {
            subMod.push(data[i].subModuleName);
        }
    }

    fillSelect2Box(subMod, 'sel_genrpt_sub_module', 'Select Sub-Module');
    $("#sel_genrpt_sub_module").select2({}).off("select2:selecting").on("select2:selecting", function (e) { if (e.params.args.data.id) { getAllRptAgainstSubmod(selectedModule, e.params.args.data.id); } })
}

function getModuleFormRptInit(data) { // DISPLAY ALL MODULES
    var modArr = [];

    for (var i = 0; i < data.length; i++) {
        if (modArr.indexOf(data[i].moduleName) < 0) {
            modArr.push(data[i].moduleName);
        }
    }
    fillSelect2Box(modArr, 'sel_genrpt_module', 'Select Module');
    $("#sel_genrpt_module").select2({}).on("select2:selecting", function (e) { if (e.params.args.data) { getSubModuleRptInit(e.params.args.data.id); } })


    $("#sel_genrpt_module").val(getSearchParams('moduleName')).change();

    getSubModuleRptInit(getSearchParams('subModuleName'));
    $("#sel_genrpt_sub_module").val(getSearchParams('subModuleName')).change();

    getAllRptAgainstSubmod(getSearchParams('moduleName'), getSearchParams('subModuleName'));
    $("#sel_genrpt_report_name").val(getSearchParams('reportName')).change();


     $("#sel_genrpt_module").prop('disabled', true);
    $("#sel_genrpt_sub_module").prop('disabled', true);
    $("#sel_genrpt_report_name").prop('disabled', true); 
}

function getRptInit() {

    var ActiveProfile = [];
    var ProfileList = [];
    var admin = 0;
    ProfileList = JSON.parse(ProfileListSession);
    ActiveProfile = JSON.parse(ActiveProfileSession);
  
    for (i = 0; i < ProfileList.length; i++)
    {
        if (ProfileList[i].accessProfileName == 'Admin')
        {
            admin = 1;
            break;
        }
        else
        {
            admin = 0;
        }
    }
        if (admin == 1) {
            
            if (ActiveProfile.accessProfileName != 'Admin') {
                admin = 0;
            }
            else {
                admin = 1;
            }
        }
        
  
    var urlParam;
    if (admin == 1)
        urlParam = '';
    else
        urlParam = 'active=1';

    var finalArr = {};
    finalArr['Module'] = []; finalArr['subModule'] = []; finalArr['rpt'] = [];
    
    var request = $.isf.ajax({
        url: service_java_URL + "reportManagement/getReportInitData?" + urlParam,
        returnAjaxObj: true,
        dataType: "json"
    });

    request.done(function (msg) {
        allRptInitData = msg.MODULE_SUBMODULE;
        getModuleFormRptInit(allRptInitData);
    });

    request.fail(function (jqXHR, textStatus) {
        alert("Request failed: " + textStatus);
    });

}


function createTableView(data) {

    var _data = data.data;
    var headerS = '';
    var headerTR = '';
    var tBodyTR = '';
    var tFooter = '';
    var incrementJ = 0;

    if (_data.length) {

        for (var r in _data) {

            if (incrementJ == 0) {
                for (var colName in _data[r]) {

                    headerS += '<th>' + colName + '</th>';
                }
            }
            var innerTR = '';
            for (var colVal in _data[r]) {

                innerTR += '<td>' + _data[r][colVal] + '</td>';

            }
            tBodyTR += '<tr>' + innerTR + '</tr>';

            incrementJ++;
        }

        headerTR = '<thead><tr>' + headerS + '</tr></thead>';
        tFooter = '<tfoot><tr>' + headerS + '</tr></tfoot>';
        tBodyTR = '<tbody>' + tBodyTR + '</tbody>';

        var finalTable = '<table id="rptTableViewTable" cellspacing="0" width="100%" class="table table-striped table-bordered table-hover dataTable no-footer">' + headerTR + tFooter + tBodyTR + '</table>';

        //var finalTable = '<table id="rptTableViewTable" cellspacing="0" width="100%" class="display nowrap">' + headerTR + tFooter + tBodyTR + '</table>';

        $('#generateRpt_table_view_div').html(finalTable);

        //$('#rptTableViewTable').DataTable();
       

        // DataTable
        var rptTableView = $('#rptTableViewTable').DataTable({
            dom: 'Bfrtip',
            buttons: [
             'colvis','excel'
            ],
            //scrollX: true,
            responsive: true,
            colReorder: true,
            initComplete: function () {

                //CREATE TEXT BOXES FOR INDIVIDUAL COLUMN SEARCH
                $('#rptTableViewTable tfoot th').each(function () {
                    var title = $(this).text();
                    $(this).html('<input type="text" />');
                });

                var api = this.api();

                // Apply the search
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

                //APPLY X SCROLL
                $("#rptTableViewTable").wrap('<div class="tabWrap" style="overflow:auto;width:100%;"></div>');
            }
        });


        rptTableView.on('column-reorder', function (e, settings, details) {
            //Apply the search
            rptTableView.columns().every(function () {
                var that = this;

                $('input', this.footer()).on('keyup change', function () {
                    if (that.search() !== this.value) {
                        that
                            .columns( $(this).parent().index()+':visible' )
                            .search(this.value)
                            .draw();
                    }
                });
            });
        });

        
    } else {
        $('#generateRpt_table_view_div').html('<span style="display:block;text-align:center;padding-top:20px;">No Data Found</span>');
    }
}

function changeChartTypeToStack(chartObj){
    chartObj.update({
        plotOptions: {
            series: {
                stacking: 'normal'
            }
        }
    });
}


function displayDataLabel(chartObj,trueOrFalse) {
    chartObj.update({
        plotOptions: {
            series: {
                dataLabels: {
                    enabled: trueOrFalse,

                }
            }
        }
    });
}



function getY2axis(colName,yAxisArr) {
    var retArr = [];
    for (var yxKey in yAxisArr) {

        if (yAxisArr[yxKey]['columnName'] == colName) {
            if (yAxisArr[yxKey]['ySide'] == 1) {
                retArr = { yAxis: yAxisArr[yxKey]['ySide'], valueSuffix: yAxisArr[yxKey]['valueSuffix'], type: 'spline' };

            } else {
                
                    retArr = { yAxis: yAxisArr[yxKey]['ySide'], valueSuffix: yAxisArr[yxKey]['valueSuffix'], type: ((generateRpt_chartTypeGlobal == 'stack')?'column':generateRpt_chartTypeGlobal) };
            }
        } 
    }

    return retArr;

}

$(document).ready(function () {
    let params = (new URL(document.location)).searchParams;
    let moduleName = params.get("moduleName");
    let subModuleName = params.get("subModuleName");
    let rptName = params.get("reportName");
    console.log('  ' + moduleName);
    console.log('  ' + subModuleName);
    console.log('  ' + rptName);



    getRptDetails(rptName, moduleName, subModuleName) 
    //GenerateReport()   
})

    
    //$('.generateReport').on('click', function () {

    function GenerateReport() {

        filterQuery = createWhereCondition();
        if (filterQuery == 'false') { return false; }

        
        var loaderHtml = $('#rptImgLoader').html();
        $('#generaterpt_container').html('<span style="display:block;text-align:center;padding-top:20px;">' + loaderHtml + '</span>');
        $('#generateRpt_table_view_div').html('<span style="display:block;text-align:center;padding-top:20px;">' + loaderHtml + '</span>');

        var chartType = generateRpt_chartTypeGlobal;

        var moduleName, subModule, rptName, groupBy;
        moduleName = $('#sel_genrpt_module').val();
        subModuleName = $('#sel_genrpt_sub_module').val();
        rptName = $('#sel_genrpt_report_name').val();
        groupBy = $("input[name=groupByOptions]:checked").val();
        if ($("#start_date_reports").length && $("#end_date_reports").length) {


            if ($("#start_date_reports") && !$("#start_date_reports").val()) {
                pwIsf.alert({ msg: "Please select any Start Date", type: 'warning' });
                return false;

            }
            if ($("#end_date_reports") && !$("#end_date_reports").val()) {
                pwIsf.alert({ msg: "Please select any End Date", type: 'warning' });
                return false;

            }
            if (!groupBy) {
                pwIsf.alert({ msg: "Please select any group by option", type: 'warning' });
                return false;
            }
        }
        var requestArr = {
            "reportName": rptName,
            "moduleName": moduleName,
            "subModuleName": subModuleName,
            "selectGroupBy": groupBy,
            "filterAttribute": filterQuery,
            "signumID": signumGlobal
        }

        pwIsf.addLayer({text:"Please wait ..."});

        var request = $.isf.ajax({
            url: service_java_URL + "reportManagement/generateReport",
            returnAjaxObj: true,
            success: function (data) {
                
                pwIsf.removeLayer();
                //ADD DOWNLOAD LINK
                var aNormalLink = '<a title="Download" href="' + service_java_URL + 'reportManagement/downloadChartReport?reportName=' + rptName + '&moduleName=' + moduleName + '&subModuleName=' + subModuleName + '&selectGroupBy=' + groupBy + '&filterAttribute=' + filterQuery + '"><i class="fa fa-download" aria-hidden="true"></i></a>';
                if (ApiProxy == true) {
                    aNormalLink = '<a title="Download" href="' + service_java_URL + 'reportManagement/downloadChartReport?reportName=' + encodeURIComponent(rptName + '&moduleName=' + moduleName + '&subModuleName=' + subModuleName + '&selectGroupBy=' + groupBy + '&filterAttribute=' + filterQuery) + '"><i class="fa fa-download" aria-hidden="true"></i></a>';
                }
               
                var aDetailedLink = '<a title="Download Detailed Data" onclick="Alert()" "><i class="fa fa-cloud-download" aria-hidden="true"></i></a>';
                $('#rpt_downloadLink').html(aNormalLink + aDetailedLink);
                
                createTableView(data);
                if (chartType != chartTypeTable) {
                    createChart(data, groupBy, rptName);
                }

            },
            method: "POST",
            contentType: "application/json; charset=utf-8",
            data: JSON.stringify(requestArr),
            dataType: "json"
        });

        request.done(function (msg) {
        });
        request.fail(function (jqXHR, textStatus) {
            alert("Request failed: " + textStatus);
        });


}
function getSearchParams(k) {
    var p = {};
    location.search.replace(/[?&]+([^=&]+)=([^&]*)/gi, function (s, k, v) { p[k] = v })
    return k ? decodeURIComponent(p[k]) : p;
} 
function Alert() {

    pwIsf.alert({ msg: "Please go to the Report Index page - Detail Dump tab to download", type: 'info' });

}