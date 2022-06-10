
//------------------------------
//$(document).ready(function () {

//    var filterIdPrefix = 'rptGenFlt_';


//    var filtersData = '{"FG1": {"data": [{"projectName": "p1","subActivity": "subA1"},{ "projectName": "p1", "subActivity": "subA2"},{ "projectName": "p2","subActivity": "subA3"}],"config": [ { "label":"Project Name","colName": "projectName","type": "select" },{"label":"Subactivity Name","colName":"subActivity","type": "select" },{"type":"date"}] }}';


//    //var filtersData='{"DeliveryExecution": {"data": [ {"projectname": "MANA_AT&T_Managed Services_United States#1","servicearea": "Managed Services","subservicearea": "RAN Expansion"}, { "projectname": "MANA_AT&T_Managed Services_United States#1","servicearea": "Managed Services","subservicearea": "RAN Modernization and Swap"},{"projectname": "MANA_AT&T_Network Design and Tuning_United States#1","servicearea": "Managed Services","subservicearea": "RAN Expansion"},{"projectname": "MANA_AT&T_Network Design and g_United States#1","servicearea": "Network Design and tuning","subservicearea": "Network Audit and Benchmarking"  }, {"projectname": "MANA_AT&T_Network Design and Tuning_United States#1", "servicearea": "Network Design and tuning", "subservicearea": "Network Tuning" },      { "projectname": "MANA_VANTRIX CORPORATION_Managed Services_Canada#1", "servicearea": "Network Design and tuning", "subservicearea": "Network Tuning" },{ "projectname": "MANA_VANTRIX CORPORATION_Network Optimization_Canada#1", "servicearea": "etwork Design and tuning", "subservicearea": "Network Audit and Benchmarking"  },   { "projectname": "MOAI_ESMERALDA BÖHM CTIES_Managed Services_American Samoa", "servicearea": "Managed Services", "subservicearea": "RAN Build" }, { "projectname": "_ESMERALDA BÖHM PRODUCTIES_Managed Services_American Samoa",  "servicearea": "Managed Services",  "subservicearea": "RAN Expansion"  },  {"projectname": "MOAI_ESMERALDA BÖHM PRODUCTIES_Managed Services_American Samoa",  "servicearea": "Managed Services",  "subservicearea": "RAN Modernization and Swap"  }, { "projectname": "MOAI_ESMERALDA BÖHM PRODUCTIES_Managed Services_American Samoa#1", "servicearea": "Managed Services",  "subservicearea": "RAN Modernization and Swap" },{ "projectname": "MOAI_ESMERALDA BÖHM PRODUCTIES_Network Design and Tuning_Australia#1",  "servicearea": "Managed Services", "subservicearea": "RAN Expansion" }, {    "projectname": "MOAI_ESMERALDA BÖHM PRODUCTIES_Network Optimization_Australia#1",  "servicearea": "Managed Services",           "subservicearea": "RAN Expansion"  },{ "projectname": "MOAI_ESMERALDA BÖHM PRODUCTIES_Network Optimization_Australia#1","servicearea": "Network Optimization", "subservicearea": "Network Optimization"  } ],"config": [ {         "colName": "projectname",   "label": "project id",   "type": "select"  },    {    "colName": "servicearea", "label": "service area", "type": "select"  },   {  "colName": "subservicearea", "label": "sub service area",  "type": "select"  } ],       "description": "Delivery Execution" }}';


//    console.log(filtersData);



//    var fillSelectBoxVal = function (data, select2Id, firstText) {

//        $('#' + select2Id).html(''); //clear select box

//        $('#' + select2Id).append('<option value="">' + firstText + '</option>');

//        $.each(data, function (index, value) {
//            $('#' + select2Id).append(
//              '<option value="' + value + '">' + value + '</option>'
//            );
//        });

//    };


//    var getRptGenDateFld = function () {

//        var startDate='<div class="col-sm-2">';
//        startDate+='<label>Start Date</label>';
//        startDate+='<div>';
//        startDate+='<div class="input-group date datepicker">';
//        startDate+='<input id="start_date_reports" class="form-control ep_datepicker" data-date-format="yyyy-mm-dd" placeholder="YYYY/MM/DD" type="text">';
//        startDate+='<span class=" input-group-addon ep_calendar_icon"></span>';
//        startDate+='</div>';
//        startDate+='<div class="row">';
//        startDate+='<label id="start_date_reports-Required" style="color: red; font-size: 10px; text-align: center; display: none;"></label>';
//        startDate+='</div>';
//        startDate+='</div>';
//        startDate+='</div>';
       
//        var endDate='<div class="col-sm-2">';
//        endDate+='<label>End Date</label>';
//        endDate+='<div>';
//        endDate+='<div class="input-group date datepicker">';
//        endDate+='<input id="end_date_reports" class="form-control ep_datepicker" data-date-format="yyyy-mm-dd" placeholder="YYYY/MM/DD" type="text">';
//        endDate+='<span class=" input-group-addon ep_calendar_icon"></span>';
//        endDate+='</div>';
//        endDate+='<div class="row">';
//        endDate+='<label id="end_date_reports-Required" style="color: red; font-size: 10px; text-align: center; display: none;"></label>';
//        endDate+='</div>';
//        endDate+='</div>';
//        startDate += '</div>';

//        return startDate + startDate;

//    };

//    var appendDataToControl = function (colName,dataArr) {

//        var dArr = [];
//        var controlId = filterIdPrefix + colName;

        
//        for (var i in dataArr) {
//            if (dArr.indexOf(dataArr[i][colName]) < 0) {
//                dArr.push(dataArr[i][colName]);
//            }
//        }
       
//        fillSelectBoxVal(dArr, controlId, 'Select One');

//    };

//    var callOnFilterChange = function (selectedControl,controlList,data) {
       
//        var ctlId = selectedControl.id;
//        var ctlName = $('#' + ctlId).data('control-name');
//        var selVal = $('#' + ctlId).val();
        
//        //console.log(ctlId + '---' + ctlName + '---' + selVal);

//        //console.log(controlList);
//        //console.log(data);
        
//        for (var k in controlList) {
            
//            if (controlList[k]['colName'] != ctlName && controlList[k]['type']=='select') {
//                var dArr = [];
//                var ctlIdToBeFilled = filterIdPrefix + controlList[k]['colName'];
                
//                for (var dk in data) {

//                    if (selVal == '') {
//                        if (dArr.indexOf(data[dk][controlList[k]['colName']]) < 0 ) {
//                            dArr.push(data[dk][controlList[k]['colName']]);
//                        }
//                    } else {
//                        if (dArr.indexOf(data[dk][controlList[k]['colName']]) < 0 && data[dk][ctlName] == selVal) {
//                            dArr.push(data[dk][controlList[k]['colName']]);
//                        }
//                    }
                   
//                }

                
//                if ($('#' + ctlIdToBeFilled).val()==''){
//                    fillSelectBoxVal(dArr, ctlIdToBeFilled, 'Select One');
//                } else {
//                    var oldVal = $('#' + ctlIdToBeFilled).val();
//                    fillSelectBoxVal(dArr, ctlIdToBeFilled, 'Select One');
//                    $('#' + ctlIdToBeFilled).val(oldVal);
//                }
//            }
//        }
        
//    };

//var createControl = function (config,data) {
    
//    for (var k in config) {

//        if (config[k]['type'] == 'select') {

//            var createSelect = $('<div class="col-sm-2"></div>');
//            var varLabel = $('<label>' + config[k]['label'] + '</label>');

//            var selID = filterIdPrefix + config[k]['colName'];
//            var varSelect = $('<select id="' + selID + '" class="select2able" data-control-name="' + config[k]['colName'] + '" ><option value="">Select one</option></select>');

//            createSelect.append(varLabel, varSelect);

//            $('#reportsFilter').append(createSelect);
            
//            appendDataToControl(config[k]['colName'], data);

//            $('#' + selID).select2({}).on("change",
//                function (e) { if (e) { callOnFilterChange(this, config,data); } });

//        }
//        if (config[k]['type'] == 'date') {

//            //getRptGenDateFld();
//            $('#reportsFilter').append(getRptGenDateFld());

//            $(function () {
//                $("[id$=start_date_reports]").datepicker({ minDate: 0 });
//            });
//            $(function () {
//                $("[id$=end_date_reports]").datepicker({ minDate: 0 });
//            });
//            $(function () {
//                $("[id$=end_date_reports]").datepicker({
//                    startDate: $("[id$=start_date_reports]").val(),
//                    autoClose: true,
//                    viewStart: 0,
//                    weekStart: 1
//                });
//            });


//        }


//    }
//};

//    filtersData = JSON.parse(filtersData);

//    for (var k in filtersData) {
        
//        createControl(filtersData[k]['config'],filtersData[k]['data']);

//    }
    
//})
//------------------------------------------


//function getActivitySubActivity() {
//    $("#cbxSubActivity").select2("val", "");
//    var SubActivity = "";
//    var projectID = document.getElementById("cbxprojectID").value;
//    $.ajax({

//        url: service_java_URL + "activityMaster/getActivitySubActivityByProjectId?ProjectID=" + projectID,
//        success: function (data) {
//            $('#cbxSubActivity').empty().append('<option value="">Select one</option>');
//            $.each(data, function (i, d) {
//                if (SubActivity != d.SubActivity) {
//                    SubActivity = d.SubActivity;
//                    $('#cbxSubActivity').append('<option value="' + d.SubActivityId + '">' + SubActivity + '</option>');
//                }
                    
//            })

//        },
//        error: function (xhr, status, statusText) {
//            console.log('An error occurred on getProject: ' + xhr.error);
//        }
//    });
//}

//function getProjects() {

//    $.ajax({

//        url: service_DotNET_URL + "AllProjects",
//        success: function (data) {

//            $.each(data, function (i, d) {
//                $('#cbxprojectID').append('<option value="' + d.ProjectID +'">' + d.ProjectID + '/' + d.ProjectName + '</option>');
//            })

//        },
//        error: function (xhr, status, statusText) {
//            console.log('An error occurred on getProject: ' + xhr.error);
//        }
//    });
//}

//function getDomains() {
//    $('#cbxDomain')
//        .find('option')
//        .remove();
//    $("#cbxDomain").select2("val", "");
//    $('#cbxDomain').append('<option value="0"></option>');
//    $.ajax({
//        url: service_java_URL + "activityMaster/getDomainDetails",
//        success: function (data) {
//            var html = "";
//            $.each(data, function (i, d) {
//                aux = d.domain.split("/");
//                $('#cbxDomain').append('<option value="' + d.domainID + '">' + d.domain + '</option>');
                
//            })
//        },
//        error: function (xhr, status, statusText) {
//            console.log('An error occurred');
//        }
//    });
//}

//function getServiceAreas() {
//    $('#cbxServiceArea')
//        .find('option')
//        .remove();
//    $("#cbxServiceArea").select2("val", "");
//    $('#cbxServiceArea').append('<option value="0"></option>');

//    $.ajax({
//        url: service_java_URL + "activityMaster/getServiceAreaDetails",
//        success: function (data) {
//            var html = "";
//            $.each(data, function (i, d) {
//                aux = d.serviceArea.split("/");
//                $('#cbxServiceArea').append('<option value="' + d.serviceAreaID + '">' + d.serviceArea + '</option>');
               
//            })


//        },
//        error: function (xhr, status, statusText) {
//            console.log('An error occurred');
//        }
//    });
//}

//function getTechnologies() {

//    $('#cbxTechnology')
//        .find('option')
//        .remove();
//    $("#cbxTechnology").select2("val", "");
//    $('#cbxTechnology').append('<option value="0"></option>');
//    var domainID = $('#cbxDomain').val();

//    $.ajax({
//        url: service_java_URL + "activityMaster/getTechnologyDetails?domainID",
//        success: function (data) {
//            var html = "";
//            $.each(data, function (i, d) {
//                $('#cbxTechnology').append('<option value="' + d.technologyID + '">' + d.technology + '</option>');
                
//            })
//        },
//        error: function (xhr, status, statusText) {
//            console.log('An error occurred');
//        }
//    });
//}

//function searchActivity() {
//    $('#cbxActivity')
//        .find('option')
//        .remove();
//    $("#cbxActivity").select2("val", "");
//    $('#cbxActivity').append('<option value="0"></option>');
//    var domainID = $("#cbxDomain").val();
//    var serviceAreaID = $("#cbxServiceArea").val();
//    var technologyID = $("#cbxTechnology").val();

//    var domainNsubDomainName = $("#cbxDomain option:selected").text();
//    var serviceAreaNsubServiceAreaName = $("#cbxServiceArea option:selected").text();
//    var technologyName = $("#cbxTechnology option:selected").text();

//    $.ajax({
//        url: service_java_URL + "activityMaster/getActivityAndSubActivityDetails/" + domainID + "/" + serviceAreaID + "/" + technologyID,

//        success: function (data) {
//            var html = "";
//            $.each(data, function (i, d) {
//                aux = d.subActivityName.split("/");
//                $('#cbxActivity').append('<option value="' + d.subActivityID + '">' + aux[0] + '</option>');
                
//            })

//        },
//        error: function (xhr, status, statusText) {
//            console.log('An error occurred');
//        }
//    });
//}

//function getSubActivity() {
//    var subActivityID = $("#cbxActivity").val();

//    $('#cbxSubActivity')
//        .find('option')
//        .remove();
//    $("#cbxSubActivity").select2("val", "");
//    $('#cbxSubActivity').append('<option value="0"></option>');

//    $.ajax({
//        url: service_java_URL + "activityMaster/getActivityAndSubActivityDetailsByID/" + subActivityID + "/" + signumGlobal,
//        success: function (data) {
//            var html = "";
//            $.each(data, function (i, d) {
//                auxS = d.subActivityName.split("/");
//                $('#cbxSubActivity').append('<option value="' + d.subActivityID + '">' + auxS[1] + '</option>');
                
//            })
//        },
//        error: function (xhr, status, statusText) {
//            console.log('An error occurred');
//        }
//    });
//}