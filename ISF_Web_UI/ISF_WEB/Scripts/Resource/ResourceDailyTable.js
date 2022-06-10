var DataObjResource;
var DataObjectTask;
var DataObjectWorkOrder;
var DailyResourceData = [];
var DailyTaskData = [];
var DailyResourceDataModel = {};
var projectList = [];
var ResourceList = [];
var ActivityList = [];
var WorkOrderList = [];
var oTableDailyResourceData;
var tableData;
var getWOResourceLevelDetailsObject = new Object();


$(document).on("click", "#checkAllEmployeeResource", function () {
    $(".checkBoxClassSignumResource").prop('checked', $(this).prop('checked'));
});
$(document).ready(function () {
    var activeProfileObj = JSON.parse(ActiveProfileSession);
    var roleID = activeProfileObj.roleID;
    var role = activeProfileObj.role;
    var marketArea = activeProfileObj.marketArea;
    getProjects();
    getAllSignumsEngineerEgg();
    if (roleID == 7) {
        $('#projDD').hide();
        $('#signumListParentDiv').hide();
        $('#divEESearch').hide();
        getDurationDT("TODAY");
    }
    else {
        $('#projDD').show();
    }

    $('#projList').on("select2:select", function (e) {
        $("#projectWarning").hide();
    });

    $('#tooltiplegends').on("mouseover", function () {
        $('#tooltiptext').css('visibility', 'visible');
    });

    $('#tooltiplegends').on("mouseout", function () {
        $('#tooltiptext').css('visibility', 'hidden');
    });
});

function selectWeek() {
    var datepickerValue = new Date();
    var year = datepickerValue.getFullYear();
    var month = datepickerValue.getMonth();
    var day = datepickerValue.getDate();
    var dateObj = new Date(year, month, datepickerValue.getDate());
    var week = $.datepicker.iso8601Week(dateObj);

    if (week > 50 && month === 0) {
        year--;
    }

    if (week === 1 && month === 11 && day > 20) {
        year++;
    }

    $('#datepicker_weekpicker').val(week + "/" + year);
}

function clearSearchedSignum() {

    $('#signumsList').val('').trigger('change');
}

function searchEngineerEngg() {
    
    if (searchValidation()) {
        let weekYear = $('#datepicker_weekpicker').val();
        getDateOfISOWeek(weekYear);
    }
}

function searchValidation() {

    OK = true;

    if ($('#projList').val() == "") {
        $('#projectWarning').show();
        OK = false;
    }

    return OK;
}

function getAllSignumsEngineerEgg() {
    clearSearchedSignum();
    $("#signumsList").select2({
        dropdownParent: $('#signumListParentDiv'),
        ajax: {
            url: service_java_URL + "activityMaster/getEmployeesByFilter",
            dataType: 'json',
            delay: 250,
            headers: commonHeadersforAllAjaxCall,
            type: 'POST',

            data: function (params) {
               return {
                    term: params.term, // search term
                    page: params.page
                };
            },
            processResults: function (data, params) {
                params.page = params.page || 1;
                var select2data = $.map(data, function (obj) {
                    obj.id = obj.signum;
                    obj.text = obj.signum + '/' + obj.employeeName
                    return obj;
                });

                return {
                    results: select2data,
                    pagination: {
                        more: (params.page * 30) < select2data.total_count
                    }
                };
            },
            cache: true
        },
        placeholder: 'Search Signum',
        escapeMarkup: function (markup) { return markup; }, // let our custom formatter work
        minimumInputLength: 3,
        templateResult: formatRepo,
        templateSelection: formatRepoSelection,
        closeOnSelect: true //close after selection
    });
}

    function formatRepo(data) {
        if (data.loading) {
            return data.text;
        }
        if (data.signum == '') { // adjust for custom placeholder values
            return 'Custom styled placeholder text';
        }
        var markup = data.signum + '/' + data.employeeName;

        return markup;
    }

    function formatRepoSelection(data) {
        return data.signum;
    }


function getWeekDateRange() {
    var today = new Date();
    var day = today.getDay();
    var diff = today.getDate() - day + (day == 0 ? -6 : 1); // 0 for sunday
    var week_start_tstmp = today.setDate(diff);
    var week_start = new Date(week_start_tstmp);
    startDate = formatted_date(week_start);
    var week_end = new Date(week_start_tstmp);  // first day of week 
    week_end = new Date(week_end.setDate(week_end.getDate() + 6));
    endDate = formatted_date(week_end);
}

/*--------------Date in YYYY-MM-DD Format-----------------------------*/

var formatted_date = function (date) {
    var m = ("0" + (date.getMonth() + 1)).slice(-2); // in javascript month start from 0.
    var d = ("0" + date.getDate()).slice(-2); // add leading zero 
    var y = date.getFullYear();
    return y + '-' + m + '-' + d;
}

function getDateOfISOWeek(w) {
    var projId = $('#projList option:selected').val();
    var signum = $('#signumsList option:selected').val();
    var date = w.split('/');
    var w1 = date[0];
    var y = date[1];
    var simple = new Date(y, 0, 1 + (w1 - 1) * 7);
    var simpleEnd = new Date(y, 0, 1 + (w1 - 1) * 7);
    var dow = simple.getDay();
    var ISOweekStart = simple;
    var ISOweekEnd = simpleEnd;
    if (dow <= 4) {
        ISOweekStart.setDate(simple.getDate() - simple.getDay() + 1);
        ISOweekEnd.setDate((simpleEnd.getDate() - simpleEnd.getDay() + 1) + 6);
        startDate = formatted_date(ISOweekStart);
        endDate = formatted_date(ISOweekEnd);
        getResourceDayWise(startDate, endDate, projId, signum);
    }
    else {
        ISOweekStart.setDate(simple.getDate() + 8 - simple.getDay());
        ISOweekEnd.setDate((simpleEnd.getDate() + 8 - simpleEnd.getDay()) + 6);
        startDate = formatted_date(ISOweekStart);
        endDate = formatted_date(ISOweekEnd);
        getResourceDayWise(startDate, endDate, projId, signum);
    }
}
/*---------------Get DT Data According to Duration Clicked----------------------------*/
function getDurationDT(text) {
    var projId = $('#projList option:selected').val();
    var signum = $('#signumsList option:selected').val();
    var durationText = text;

    var today = new Date();
    switch (durationText) {
        case "PREVIOUS":
            var day = today.getDay();
            var diff = today.getDate() - day + (day == 0 ? -13 : -6);
            var week_start_tstmp = today.setDate(diff * clickPrev);
            var week_start = new Date(week_start_tstmp);
            startDate = formatted_date(week_start);
            var week_end = new Date(week_start_tstmp);  // first day of week 
            week_end = new Date(week_end.setDate(week_end.getDate() + 4));
            endDate = formatted_date(week_end);

            break;

        case "TODAY":
            var startOfWeek = moment().startOf('isoweek').toDate();
            var endOfWeek = moment().endOf('isoweek').toDate();
            startDate = formatted_date(startOfWeek);
            endDate = formatted_date(endOfWeek);
            today = startDate;
            break;
        case "NEXT":
            var day = today.getDay();
            var diff = today.getDate() - day + (day == 0 ? 1 : 8);
            var week_start_tstmp = today.setDate(diff * clickPrev);
            var week_start = new Date(week_start_tstmp);
            startDate = formatted_date(week_start);
            var week_end = new Date(week_start_tstmp);  // first day of week 
            week_end = new Date(week_end.setDate(week_end.getDate() + 6));
            endDate = formatted_date(week_end);
            break;
    }
    getResourceDayWise(startDate, endDate, projId, signum);
}


function getProjects() {
    $.isf.ajax({
        url: service_java_URL + "woManagement/getProjectBySignum/" + signumGlobal,
        async: false,
        success: function (data) {
            if (data.isValidationFailed == false) {
                $.each(data.responseData, function (i, d) {
                    $('#projList').append('<option value="' + d.projectID + '">' + d.projectID + '-' + d.projectName + '</option>');
                    // $('#sel_project_name').append('<option value="' + d.ProjectName + '">' + d.ProjectName + '</option>');
                });
            }
        },
        error: function (xhr, status, statusText) {
            console.log('An error occurred on getAllProjects: ' + xhr.error);
        }

    });
}
$(document).on('click', '.panel-heading span.clickable', function (e) {
    var $this = $(this);
    if (!$this.hasClass('panel-collapsed')) {
        $this.parents('.panel').find('.panel-body').slideUp();
        $this.addClass('panel-collapsed');
        $this.find('i').removeClass('fa-angle-up').addClass('fa-angle-down');
    } else {
        $this.parents('.panel').find('.panel-body').slideDown();
        $this.removeClass('panel-collapsed');
        $this.find('i').removeClass('fa-angle-down').addClass('fa-angle-up');
    }
})


function getTaskResourceWise(projId) {
    GenerateDataTable(projId);
}

function GenerateDataTable(projId) {
    GenerateTable(DailyResourceData, DailyTaskData, projId);
}

function getResourceDayWise(startDate, endDate, projId, signum) {

    $('#dateRange').text(startDate + '~' + endDate);
    if (projId === "") {
        projId = 0;
    }
    pwIsf.addLayer({ text: 'Please wait ...' });

    getWOResourceLevelDetailsObject.projId = projId;
    getWOResourceLevelDetailsObject.startDate = startDate;
    getWOResourceLevelDetailsObject.endDate = endDate;
    getWOResourceLevelDetailsObject.signum = signum;

    getTaskResourceWise(projId);

}



function showInfoModel(ID) {
    $('#empInfo').modal('show');
    getEmployeeDetailsBySignumViewProfile(ID);

}
function getEmployeeDetailsBySignumViewProfile(ID) {

    $.isf.ajax({
        url: service_java_URL + "activityMaster/getEmployeeBySignum/" + ID,
        success: function (data) {

            $("#self_employee_signum").val(data.signum);
            $("#self_employee_email").val(data.employeeEmailId);
            $("#self_employee_unit").val(data.orgUnit);
            $("#self_employee_jobRole").val(data.jobName);
            $("#self_employee_serviceArea").val(data.serviceArea);
            $("#self_employee_officeBuilding").val(data.officeBuilding);
            $("#self_employee_contactNumber").val(data.contactNumber);
            $("#self_employee_employeeName").val(data.employeeName);
            $("#self_employee_personnelNumber").val(data.personnelNumber);
            $("#self_employee_jobRoleFamily").val(data.jobRoleFamily);
            $("#self_employee_city").val(data.city);
            $("#self_employee_costCenter").val(data.costCenter);
            $("#self_employee_functionalArea").val(data.functionalArea);
            $("#self_employee_hrLocation").val(data.hrLocation);
            $("#self_employee_managerSignum").val(data.managerSignum);
            $("#self_employee_status").val(data.status);


        },
        error: function (xhr, status, statusText) {
            console.log('An error occurred : ' + xhr.responseText);
        }
    });


}
function showCalendarModel(signum) {
    getResourceCalendar(signum);
    $('#ResCalendar').modal({
        backdrop: 'static'
    })
    $('#ResCalendar').modal('show');
    var dt = new Date();
}


function multipleSignumShiftUpdate() {
    pwIsf.addLayer({ text: "Please wait ..." });
    selectedSignums = getSignumsForBulkShiftUpdate();
    signumArrTeamExplorer = selectedSignums;
    if (selectedSignums != 0) {
        pwIsf.confirm({
            title: 'Multiple Shift Time Update', msg: "Do you want to update the shift time of the selected employees?",
            'buttons': {
                'Yes': {
                    'action': function () {
                        pwIsf.removeLayer();
                        getTimeZones();
                        $('#shiftModal').modal('show');
                        $('#timeZoneHome').hide();
                        $('#shiftTitle').hide();
                        $('#currentShiftDiv').hide();
                        $('#shiftModal .nav-tabs li a[href="#shiftTimingAdd_tab_1"]').tab('show');
                        $('#savedTimingsTab').hide();
                        $('#labelCurrentShift').hide();
                        $("#singleOrMultiple").val("multiple");
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
        pwIsf.alert({ msg: "Please select at least one employee!" });
        pwIsf.removeLayer();
    }

}

function getSignumsForBulkShiftUpdate() {
    var chkArray = [];
    $(".checkBoxClassSignumResource:checked").each(function () {
        parseInt(chkArray.push($(this).val()));
    });
    var selected;
    selected = chkArray.join(',');
    if (selected.length > 1) {
        return chkArray;
    } else {
        return 0;
    }
}


function GenerateTable(DailyResourceData, DailyTaskData, projId) {
    BuildColumnHeaderText();
    CreateDataTable();
    pwIsf.removeLayer();
}

var AllColumnText = new Array();


function BuildColumnHeaderText() {
    AllColumnText = new Array();
    var tempStartDate = new Date(getWOResourceLevelDetailsObject.startDate);
    var tempEndDate = new Date(getWOResourceLevelDetailsObject.endDate);
    AllColumnText.push({ "title": "Shift" });
    AllColumnText.push({ "title": "Current Backlog Count" });

    while (tempStartDate <= tempEndDate) {
        let date = tempStartDate.toISOString().slice(0, 10);
        AllColumnText.push({ "title": date });
        tempStartDate.setDate(tempStartDate.getDate() + parseInt(1));
    }
}


function GetCurrentDate() {
    var d = new Date();
    var month = d.getMonth() + 1;
    var day = d.getDate();
    var output = d.getFullYear() + '-' +
        (month < 10 ? '0' : '') + month + '-' +
        (day < 10 ? '0' : '') + day;

    return output;
}


var columnData = new Array();
function CreateDataTable() {


    if ($.fn.dataTable.isDataTable("#tblinfo")) {
           oTableDailyResourceData.destroy();
        $('#tblinfo').empty();
    }

    var url = service_java_URL + "resourceManagement/getEEDashboardData/" + signumGlobal + "/" + getWOResourceLevelDetailsObject.projId + "/" + getWOResourceLevelDetailsObject.startDate + "/" + getWOResourceLevelDetailsObject.endDate + "?term=" + getWOResourceLevelDetailsObject.signum;
    var count = 0;

    oTableDailyResourceData = $('#tblinfo').DataTable({
        "processing": true,
        "language": {

            processing: '<i class="fa fa-spinner fa-spin" style="font-size:24px;"></i><span class="sr-only">Processing...</span>'
        },

        "serverSide": true,
        searching: false,
        responsive: true,
        destroy: true,
        "pageLength": 10,
        colReorder: true,

        "ajax": {
            "headers": commonHeadersforAllAjaxCall,
            "url": url,
            "type": "POST", 
            "error": function () {
                pwIsf.removeLayer();
                $('#div_table').hide();
            }
        },
        "aoColumnDefs": [
            { "bSortable": false, "aTargets": [0, 1, 2, 3, 4, 5, 6, 7, 8] },
            {
                "className": "clickableCell",
                "targets": [2, 3, 4, 5, 6, 7, 8]
            }
        ],
        "columns": [

            {
                "title": AllColumnText[0].title,
                "data": "signum",
                "render": function (data, type, row) {
                    count++
                    var signum = row["signum"].split(' ');
                    var resSignum = signum[0];
                    var resName = row["signum"].substr(row["signum"].indexOf("("));
                    resName = resName.replace(/[()]/g, '');
                    var resourceButtons = CreateResourceButtonHTML(row["signum"], resSignum, resName, getWOResourceLevelDetailsObject.projId, count);
                    return resourceButtons;
                },
                "sClass": "sorting_disabled"
            },
            {
                "title": AllColumnText[1].title,
                "data": "dates",
                "render": function (data, type, row) {
                    return ResourceBacklogCount(row.backlogCounts);
                },

            },
            {
                "title": AllColumnText[2].title,
                "data": "dates",
                "render": function (data, type, row) {
                    return BuildEffortsHTML(row, AllColumnText[2].title);
                },

            },
            {
                "title": AllColumnText[3].title,
                "data": "dates",
                "render": function (data, type, row) {
                    return BuildEffortsHTML(row, AllColumnText[3].title);
                },

            },
            {
                "title": AllColumnText[4].title,
                "data": "dates",
                "render": function (data, type, row) {
                    return BuildEffortsHTML(row, AllColumnText[4].title);
                },

            },
            {
                "title": AllColumnText[5].title,
                "data": "dates",
                "render": function (data, type, row) {
                    return BuildEffortsHTML(row, AllColumnText[5].title);
                },

            },
            {
                "title": AllColumnText[6].title,
                "data": "dates",
                "render": function (data, type, row) {
                    return BuildEffortsHTML(row, AllColumnText[6].title);
                },

            },
            {
                "title": AllColumnText[7].title,
                "data": "dates",
                "render": function (data, type, row) {
                    return BuildEffortsHTML(row, AllColumnText[7].title);
                },


            },
            {
                "title": AllColumnText[8].title,
                "data": "dates",
                "render": function (data, type, row) {
                    return BuildEffortsHTML(row, AllColumnText[8].title);
                },

            }

        ],
        "createdRow": function (row, data, index) {

            $.each($('td.clickableCell', row), function (colIndex) {
                $(this).attr('title', 'Click for more details');
            });

        },

        initComplete: function () {
            $("#tblinfo_wrapper").addClass("margintop");

            if ($("#tblinfo_length").length != 0) {
                var btnDownload = '<button id="btnDownload" onclick="DownloadFile()" class="dt-button buttons-excel buttons-html5" style="margin-left: 10px;">Excel</button>';
                $("#tblinfo_length").append(btnDownload);
            }
            $('#tblinfo th').each(function (i) {
                var title = $('#tblinfo th').eq($(this).index()).text();
                $('#tblinfo th:first').removeClass('sorting_asc').addClass('sorting_disabled');

                if (title == "Shift") {
                    $(this).html('<div class="row" style="width:342px;"><div class="col-lg-1";  style="margin-bottom: 0px; margin-left: -8px;"><input type="checkbox" title="Select All Employees" id="checkAllEmployeeResource" /></div>&emsp;<div class="col-lg-2";  style="margin-bottom: 0px;"><input type="button" onclick="multipleSignumShiftUpdate();" class="btn btn-info btn-xs" value="Update Shift Time"></div>&emsp;<div class="col-lg-3";  style="margin-bottom: 0px;margin-left: 120px; text-align: right;">Resource</div>');
                }
                if (title == "Backlog Hours")
                    $(this).html('<div style="text-align: center;">Backlog Hours</div>');
            });

                registerTableCellEvents();

        }
    });
}

function DownloadFile() {
    pwIsf.addLayer({ text: 'Please wait ...' });
    let serviceUrl = `${service_java_URL}resourceManagement/downlaodEEDashboardData/${signumGlobal}/${getWOResourceLevelDetailsObject.projId}/${getWOResourceLevelDetailsObject.startDate}/${getWOResourceLevelDetailsObject.endDate}?term=${getWOResourceLevelDetailsObject.signum}`;
    let fileDownloadUrl;
    fileDownloadUrl = UiRootDir + "/data/GetFileFromApi?apiUrl=" + serviceUrl;
    window.location.href = fileDownloadUrl;
    pwIsf.removeLayer();
}

function CreateResourceButtonHTML(obj, resSignum, resName, projId, ik) {

    return '<div class="row" style="text-align: left;display: table; margin-left: 0px; ">'

        + ' <div style= "display: table-cell; margin-left: -10px; "><input class="checkBoxClassSignumResource" type="checkbox" value="' + resSignum + '"></div> '

        + ' <div style= "display: table-cell; width:10px; padding-left:25px;  "><button type="button" class="btn btn-default btn-xs" onclick="OpenCalendarView(\'' + resSignum + '\', \'' + obj + '\',\'' + projId + '\');" >'
        + '<span id="' + obj + ik + '_spn" class="fa fa-calendar" aria-hidden="true"></span></button></div>'
        + '<div style= "display: table-cell; width:10px; padding-left:10px; "><button type="button" class="btn btn-default btn-xs" onclick=showInfoModel("' + resSignum + '")>'
        + '<span id="' + obj + ik + '_spn1" class="fa fa-info" aria-hidden="true"></span></button></div>'
        + '<div style= "display: table-cell;  width:10px; padding-left:10px; "><button type="button" class="btn btn-default btn-xs" onclick="getTimeZones();getShiftTiming(\'' + resSignum + '\',\'' + resName + '\',true)">'
        + '<span id="' + obj + ik + '_spn2" class="fa fa-clock-o" aria-hidden="true"></span></button></div>'
        + '<div style= "display: table-cell; width:170px padding-left:25px;overflow-wrap: break-word;">' + obj + '</div></div>'

}

function ResourceBacklogCount(data) {
    let backlogWOCounts;
    
    if (data !== null && data !== undefined) {
        backlogWOCounts = '<div id="bcklog_signum" style="text-align:center; color:#000000;font-weight:bold;">' + data.toString() + '</div>';
    }
    else {
        backlogWOCounts = '<div id="bcklog_signum" style="text-align:center; color:#000000;font-weight:bold;">NA</div>'
    }

    return backlogWOCounts;
}

function CreateEffortsHoursHTML(data) {
    let closedWOCount = data.closedWOCounts;    
    let color = '#000000';

    return '<div style="text-align: center; color:' + color + '; font-weight: bold;">' + closedWOCount.toString() + ' Closed WO</div>';
}

function BuildEffortsHTML(data, Column) {

    for (var obj in data["dates"]) {
        if (obj == Column) {
            return CreateEffortsHoursHTML(data["dates"][obj]);
        }
    }
    return "";
}

function hideEnggDetails() {
    $('#divShowEnggHours').hide();
    $('#divShowEnggHours').animate({ right: '0%', opacity: 1.0 }, "slow");
}

//Register events for each cell of the table
function registerTableCellEvents() {
    $('#tblinfo').off('click').on('click', 'td.clickableCell', function (e) {

        let signumCell = this.parentNode.cells[0];
        let dateCell = e.delegateTarget.tHead.rows[0].cells[this.cellIndex];

        let signum = $(signumCell).text().trim().split(' (')[0];
        let selectedDate = $(dateCell).text();
        let projectId = $('#projList').val();
        showEngineerDetails(signum, projectId, selectedDate)
    });

    $('#tblinfo').off('mouseenter').on('mouseenter', 'td.clickableCell', function (e) {

        $(this).addClass('addBackground');

    });

    $('#tblinfo').off('mouseleave').on('mouseleave', 'td.clickableCell', function (e) {

        $(this).removeClass('addBackground');

    });

}

//Call api to get enginner details
function showEngineerDetails(signum, projectId, selectedDate) {

    pwIsf.addLayer({ text: C_LOADDER_MSG });
    let projectIdParam = `&projectID=${projectId}`;
    let requestParams = `signumID=${signum}${projectId === "" ? "" : projectIdParam}&selectedDate=${selectedDate}`;

    $.isf.ajax({
        url: `${service_java_URL}resourceManagement/getEngineerDetails?${requestParams}`,
        type: 'POST',
        success: function (data) {
            pwIsf.removeLayer();
            if (!data.isValidationFailed) {
                showEngineerDetailsDiv(data.responseData, signum, selectedDate);
            } else {
                responseHandler(data);
            }
        },
        error: function (xhr, status, statusText) {
            pwIsf.removeLayer();
        }

    });
}

//Show engineer details on screen
function showEngineerDetailsDiv(data, signum, selectedDate) {
    $('#dateSignumHeader').text(`${selectedDate} : ${signum}`);
    modifyEngineerData(data, selectedDate);
    for (const property in data) {
        $(`.rowInfo [name="${property}"]`).text(`${data[property] !== undefined ? data[property] : "NA"}`);
    }

    $('#engineerDetailsModal').modal('show');
}


function modifyEngineerData(data, selectedDate) {
    var timeZone = localStorage.getItem("UserTimeZone");
    if (timeZone == null || timeZone == "" || timeZone == undefined) {
        timeZone = "Asia/Calcutta";
    }
    const currentDate = moment(new Date()).tz(timeZone).format('YYYY-MM-DD');

    if (selectedDate > currentDate) {
        data.inprogressWOCount = "NA";
    }
    else if (selectedDate < currentDate) {
        data.plannedAssignedWOCount = "NA";
        data.inprogressWOCount = "NA";
    }
}