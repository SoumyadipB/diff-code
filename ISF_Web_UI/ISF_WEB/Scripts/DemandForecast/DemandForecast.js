let Glob_logedInUserRole = JSON.parse(ActiveProfileSession).role;
var Glob_marketArea = JSON.parse(ActiveProfileSession).organisation;
let Glob_pageSize = 6;
let Glob_projectID = 0;
let Glob_startDate = formatDate(new Date());
let Glob_resourceSum = 0;
let Glob_dateForButtons = new Date();
let Glob_projectStartDate = new Date();
let Glob_projectStartDateConfig = new Date();
let Glob_projectEndDate = getLastDayOfYearAndMonth();
let Glob_projectEndDateConfig = getLastDayOfYearAndMonth();
var bool3Months = false;

const C_ROLE_FM = 'Fulfillment Manager';
const C_NO_DATA_FOUND = 'Data not found';
let arrVendorTechForFM = [];

let statusColorArray = {
    "PROPOSAL PENDING": "#ffff00",//Proposal Pending
    "PROPOSED": "#a9d08e",
    "RESOURCE ALLOCATED": "#00b0f0",//Resource Allocated
    "DEPLOYED": "#00b050",
    "COMPETENCENOTAVAILABLE": "#ed7d31",
    "CAPACITYNOTAVAILABLE": "#ff0000",//capacityNotAvailable
    "CLOSED": "#595959",
    "ALL": "#FFFFFF"
};

function getLastDayOfYearAndMonth() {
    let d = new Date();
    let y = d.getFullYear();
    let m = d.getMonth();
    let dd = d.getDate();
    return (new Date((new Date(y + 1, m, dd))));
}

function createGanttCustomLegends() {
    let btnHtml = ``;
    var totalCount = 0;
    $.isf.ajax({
        type: C_API_GET_REQUEST_TYPE,
        url: `${service_java_URL}resourceManagement/getAllPositionsCount?projectId=${Glob_projectID}`,
        success: function (data) {
            for (let i in statusColorArray)
            {
                var tempVar = data.filter(function (el) {
                    return el.positionStatus == i;
                })
                if (tempVar.length !== 0) {
                    btnHtml += `<button type="button" class="btn" style="background-color: ${statusColorArray[i]};
                    font-size: 9px;padding-top: 4px;  padding-bottom: 5px;"
                    onclick="DemandViewFilter('${i}');">${i}(${tempVar[0].count})</button>`;
                    totalCount = totalCount + tempVar[0].count;
                }
                else {
                    if (i !== "ALL") {
                        btnHtml += `<button type="button" class="btn" style="background-color: ${statusColorArray[i]};
                        font-size: 9px;padding-top: 4px;  padding-bottom: 5px;"
                        onclick="DemandViewFilter('${i}');">${i}(0)</button>`;
                    }
                    else {
                        btnHtml += `<button type="button" class="btn" style="background-color: ${statusColorArray[i]};
                        font-size: 9px;padding-top: 4px;  padding-bottom: 5px;"
                        onclick="DemandViewFilter('${i}');">${i}(${totalCount})</button>`;
                    }
                }
            }
            $('.custom_legend_btn_area').empty();
            $('.custom_legend_btn_area').html(btnHtml);
        },
        error: function (msg) {
         //BLANK
        },
        complete: function (msg) {
            //BLANK
        }
    });
}

function DemandViewFilter(positionStatus) {
    pwIsf.addLayer({ text: C_PLEASE_WAIT });
    var projectID = $("#DV_projectId").val();
    var kt_filter; var objk_filter = {};
    var URLvar = "";
    if (Glob_logedInUserRole === C_ROLE_FM) {
        URLvar = `${service_java_URL}resourceManagement/getAllPositions?spoc=${signumGlobal}&projectId=${projectID}`;
    }
    else {
        URLvar = `${service_java_URL}resourceManagement/getAllPositions?projectId=${projectID}`;
    }

    if (positionStatus != "ALL") {
        URLvar = `${URLvar}&positionStatus=${positionStatus}`;
    }
    $.isf.ajax({
        type: C_API_GET_REQUEST_TYPE,
        url: URLvar,
        success: appendPlanSourcesFMFilter_modal,
        error: function (msg) {
            pwIsf.removeLayer();
            $("#DV_projectId").val(projectID);
        },
        complete: function (msg) {
            //BLANK
        }
    });

    function appendPlanSourcesFMFilter_modal(data) {
        if (data.length) {
            ganttInstanceForModal.attachEvent("onBeforeParse", function () {
                ganttInstanceForModal.clearAll();
            });
            kt_filter = data;
            objk_filter.data = kt_filter;
            ganttInstanceForModal.config.readonly = true;
            ganttInstanceForModal.config.drag_move = false;
            ganttInstanceForModal.config.drag_progress = false;
            ganttInstanceForModal.config.drag_resize = false;
            ganttInstanceForModal.config.static_background = true;
            ganttInstanceForModal.init("demandBody");
            ganttInstanceForModal.clearAll();
            ganttInstanceForModal.parse(objk_filter);
            ganttInstanceForModal.attachEvent('onBeforeLightbox', function (id) {
                return false;
            });
            ganttInstanceForModal.eachTask(function (task) {
                colorize_task(task);
            });
            ganttInstanceForModal.templates.task_text = function (start, end, task) {
                return `<span style='color:black;float:left;padding-left:10px;'>${task.text}</span>`;
            };

            let eveOnGanttRender = ganttInstanceForModal.attachEvent("onGanttRender", function () {
                pwIsf.removeLayer();
                console.log('onGanttRender');
            });

            ganttInstanceForModal.render();
            ganttInstanceForModal.detachEvent(eveOnGanttRender);
            $('.modal-backdrop.fade.in').hide();
            $(".ganttControlls").show();
            createGanttCustomLegends();
        } else {
            pwIsf.alert({ msg: "Data not found", type: C_WARNING });
            $('#demandBody').html('').append('<label><b >No Records Found</b></label>');
            pwIsf.removeLayer();
            $('.modal-backdrop.fade.in').hide();
            $(".ganttControlls").hide();
        }
    }
}

function makeItFullScreenForGantt(thisObj, targetObjsWithMakeItFullClass) {
    var that = thisObj;
    var iObg = $(that).find('i');

    if (iObg.hasClass('fa-expand')) {

        for (let i in targetObjsWithMakeItFullClass) {
            targetObjsWithMakeItFullClass[i]['target'].addClass(targetObjsWithMakeItFullClass[i]['fullClass']);
        }
        $(".gantt_grid_data").css("max-height", "570px");
        $(".gantt_data_area").css("max-height", "570px");
        $(".gantt_parent_area").css("width", "95%");
        iObg.removeClass('fa-expand').addClass('fa-compress');
        $(that).attr('title', 'Exit from full screen');
        return true;
    } else {
        for (let i in targetObjsWithMakeItFullClass) {
            targetObjsWithMakeItFullClass[i]['target'].removeClass(targetObjsWithMakeItFullClass[i]['fullClass']);
        }
        $(".gantt_grid_data").css("max-height", "235px");
        $(".gantt_data_area").css("max-height", "235px");
        $(".gantt_parent_area").css("width", "100%");
        iObg.removeClass('fa-compress').addClass('fa-expand');
        $(that).attr('title', 'Full screen');
        return false;
    }
}

function maxLengthCheck(object) {
    if (object.value.length > object.max.length) {
        object.value = object.value.slice(0, object.max.length)
        pwIsf.alert({ msg: "Head Counts cannot Exceed 99", type: "warning", autoClose: 2 })
    }

}

function isNumeric(evt) {
    var theEvent = evt || window.event;
    var key = theEvent.keyCode || theEvent.which;
    if (key == "13") {

    }
    else {
        key = String.fromCharCode(key);
        var regex = /[0-9]|\./;
        if ((!regex.test(key)) || (key == ".")) {
            theEvent.returnValue = false;
            pwIsf.alert({ msg: "Invalid Head Count!", type: "warning" })
            if (theEvent.preventDefault) theEvent.preventDefault();
        }
    }

}

$(document).ready(function () {
    if (Glob_logedInUserRole == "Project Manager")
        $('.saveDraft').css('display', 'none');
    getDemandForecastSummary(new Date(), Glob_pageSize);

    $('#fullScreenForGant').off('click').on('click', function () {

        makeItFullScreenForGantt(this, [{ target: $('.gantt_parent_area'), fullClass: 'make_full_screen' }, { target: $('.ganttControlls'), fullClass: 'make_full_screen_for_ganttControls' }]);

    });


});

/*---------------2. GET API for Demand Summary-----------------------*/
function getDemandForecastSummary(dateParam, pageSize) {
    Glob_pageSize = pageSize;
    var today = formatDate(dateParam);
    Glob_startDate = today;
    pwIsf.addLayer({ text: "Please wait ..." });
    $('.sendNext').removeClass("disabledbutton");
    var serviceUrl = service_java_URL + "demandForecast/getDemandSummary?signum=" + signumGlobal + "&pageSize=" + Glob_pageSize + "&startDate=" + Glob_startDate + "&role=" + Glob_logedInUserRole + "&marketArea=" + Glob_marketArea;
    if (ApiProxy) {
        serviceUrl = service_java_URL + "demandForecast/getDemandSummary?signum=" + signumGlobal + encodeURIComponent("&pageSize=" + Glob_pageSize + "&startDate=" + Glob_startDate + "&role=" + Glob_logedInUserRole + "&marketArea=" + Glob_marketArea);
    }

    $.isf.ajax({
        type: "GET",
        url: serviceUrl,
        success: populateSummaryTable,
        error: function (msg) {

        },
        complete: function (msg) {
            pwIsf.removeLayer();
        }
    });

    function populateSummaryTable(datak) {

        pwIsf.removeLayer();

        if (datak.length != 0) {
            //to remove max error stack size
            $('.demand_table_area').empty();
            $('.demand_table_area').html(`<table id="example" class="responsive table table-bordered table-hover" cellspacing="0" width="100%"></table>`);

            createTableHTML(datak);
            oTables = $('#example').DataTable({
                searching: true,
                responsive: true,
                retrieve: true,
                //destroy: true,
                "pageLength": 30,
                colReorder: true,
                "info": false,
                'columnDefs': [
                    {
                    }
                ],
                "aoColumnDefs": [
                    { 'bSortable': false, 'aTargets': ['no-sort'] }
                ],
                'rowsGroup': [0, 1],
                dom: 'Bfrtip',
                buttons: [
                    'colvis', 'excelHtml5'
                ],
                'rowCallback': function (row, data, index) {
                    var $tds = $(row).find("td").not(':first').not(':nth-child(2)');
                    $.each($tds, function (i, el) {
                        if (data[2] == "Plan")
                            $(this).css('background-color', 'rgb(249, 236, 173)');
                        else if (data[2] == "Effective")
                            $(this).css('background-color', 'rgb(207, 207, 253)');
                        else if (data[2] == "Fulfilled")
                            $(this).css('background-color', 'rgb(178, 253, 178)');
                    });
                },
                initComplete: function () {

                    $('#example tfoot th').each(function (i) {
                        var title = $('#example thead th').eq($(this).index()).text();
                        if (title == "Project Name")
                            $(this).html('<input type="text" class="form-control" style="font-size:12px;" placeholder="Search ' + title + '" data-index="' + i + '" />');
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
            if (pageSize == 12)
                $($("#example").find("thead").find("th")[0]).css("min-width", "80px");
            if (pageSize == 9)
                $($("#example").find("thead").find("th")[0]).css("min-width", "80px");
            $('#example tfoot').insertAfter($('#example thead'));
        }
        else {
            $('.demand_table_area').html('<div style="text-align:center;">No Data Found!</div>');

            $('.durationButtons').css("display", "none");
            pwIsf.alert({ msg: "No Data exists! ", type: "warning", autoClose: 2 });
        }

    }
}

/*---------------Format Date in yyyy-mm-dd format-----------------------*/
function formatDate(date) {
    var d = new Date(date),
        month = '' + (d.getMonth() + 1),
        day = '01',
        year = d.getFullYear();

    if (month.length < 2) month = '0' + month;
    if (day.length < 2) day = '0' + day;

    return [year, month, day].join('-');
}

/*---------------Dynamically load table-----------------------*/
function createTableHTML(data) {
    var tr = '<tr>';
    var trfoot = '<tr>';
    var td = "";
    var tdfoot = "";
    for (keys in data[0]) {
        if (keys == "Project Name")
            td = td + "<th>" + keys + "</th>";
        else
            td = td + "<th class='no-sort'>" + keys + "</th>";
        if (keys == "Project Name")
            tdfoot = tdfoot + "<th style='width:280px;'>" + keys + "</th>";
        else
            tdfoot = tdfoot + "<th style='width:80px;'></th>";
    }

    tr = tr + td + "</tr>";
    trfoot = trfoot + tdfoot + "</tr>";
    $("#example").html('<thead></thead><tbody></tbody><tfoot></tfoot>');
    $("#example thead").html(tr);
    $("#example tbody").html("");
    $("#example tfoot").html(trfoot);

    for (var i = 0; i < data.length; i++) {
        var keyValue;
        tr = "<tr>";
        td = "";
        for (keys in data[i]) {
            keyValue = data[i][keys];
            if (data[i][keys] == null) {
                keyValue = defaultVal;
            }
            if (keys == "Actions") {
                if (Glob_logedInUserRole == "Project Manager" || Glob_logedInUserRole == "Fulfillment Manager") {
                    td = td + '<td id="' + keyValue + '"><button class="btn btn-primary" title="Demand Details" style="padding:4px 10px 4px 6px;"  id="' + keyValue + '" onclick="opendetailsview(this,' + keyValue + ')"><i class="fa fa-info"></i></button> '

                } else {
                    td = td + '<td style="min-width:80px;" id="' + keyValue + '"><input id="check' + keyValue + '" class="checkClassDemand" type="checkbox"></input>&nbsp;&nbsp;<button class="btn btn-primary"  style="padding:4px 10px 4px 6px;" title="Demand Details" id="' + keyValue + '" onclick="opendetailsview(this,' + keyValue + ')"><i class="fa fa-info"></i></button>';
                }
                td = td + '<button  title="Demand Summary History" class="btn btn-primary" style="padding:4px 6px 4px 6px;"  id="' + keyValue + '" onclick="showDemandSummaryHistory(this,' + keyValue + ')"><i class="fa fa-history"></i></button>'
                '</td>';
            }
            else {
                if (keyValue === "Resource Planning Manager")
                    td = td + '<td>Plan</td>';
                else if (keyValue === "Project Manager")
                    td = td + '<td>Effective</td>';
                else if (keyValue === "Fulfillment Manager")
                    td = td + '<td>Fulfilled</td>';
                else
                    td = td + '<td>' + keyValue + '</td>';
            }
        }
        tr = tr + td + "</tr>";
        $("#example tbody").append(tr);

    }
}

function showDemandSummaryHistory(details,pId){

    var config = {};
    config.pageId = 'DEMAND_SUMMARY_' + pId;
    config.title = "Demand Forecast Summary History (Project Id:" + pId + ")";
    config.contextEnabled = false;
    config.isCollapsed = false;
    config.isMailDisabled = true;

    $("#historyDialogue").dialog({
        title: config.title,
        modal: true,
        minWidth: 800,
        minHeight: 540,
        top: 10,
        open: function () {
            $(this).closest(".ui-dialog")
                .find(".ui-dialog-titlebar-close")
                .html("x");
        },
        close: function (event, ui) {
            $(this).dialog("close");
            $(this).empty();
        }
    });

    $("#historyDialogue").append('<div id="comments-container' + config.pageId + '"></div>');
    $('#comments-container').commentIsf(config);

}

$(".checkClassDemand").on('change', function () {

    if (!$(this).prop("checked")) {
        $(this).closest('tr').find('textarea').prop("disabled", true);
    }
    else
        $(this).closest('tr').find('textarea').prop("disabled", false);
});

//Checking all boxes
$("#resourceCheckAllForecast").click(function () {
    $(".checkClassDemand").prop('checked', $(this).prop('checked'));
});

$(".checkClassDemand").on('change', function () {

    if (!$(this).prop("checked")) {
        $("#resourceCheckAllForecast").prop("checked", false);
    }

});

$(document).on('click', '.editCells', function (event) {
    $('.saveDraft').css("display", "block");
    $(this).find("i").removeClass("fa-edit").addClass("fa-ban");
    $(this).removeClass("btn-success").addClass("btn-warning");
    var $row = $(this).closest("tr").off("mousedown");
    var $tds = $row.find("td").not(':first').not(':nth-child(2)').not(':nth-child(3)');

    $.each($tds, function (i, el) {
        var txt = $(this).text();
        $(this).html("").append("<input style='width:50px;' type='number' step='0.5' min='0' value=\"" + txt + "\">");
    });
})

/*---------------On Click of checkbox Edit Cells-----------------------*/
$(document).on('click', '#example tr', function (event) {
    if (event.target.type == 'checkbox') {
        var $row = $(this).closest("tr").off("mousedown");
        var $tds = "";
        if (!bool3Months)
            $tds = $row.find("td").not(':first').not(':nth-child(2)').not(':nth-child(3)');
        else
            $tds = $row.find("td").not(':first').not(':nth-child(2)').not(':nth-child(3)').not(':nth-child(4)').not(':nth-child(5)').not(':nth-child(6)');
        var projectID = event.target.offsetParent.getAttribute("id");

        if (event.target.checked) {

            if (Glob_logedInUserRole == "Resource Planning Manager") {
                $('.saveDraft').css("display", "block");
                $.each($tds, function (i, el) {
                    var txt = $(this).text();
                    $(this).html("").append("<input class='typeNumberInput' style='width:50px;' onkeypress='return isNumeric(event)' oninput='maxLengthCheck(this)' type='number' step='1' min='0' max='99' value=\"" + txt + "\">");
                });

            }
        }

        else {
            $.each($tds, function (i, el) {
                $('.saveDraft').css("display", "none");
                var txt = $(this).find("input").val();
                if (txt == "") {
                    var td = $(this).html("");
                    $(td).append("<td style='width:50px;'>" + defaultVal + "</td>");
                }

                else {
                    var td = $(this).html("");
                    $(td).append("<td style='width:50px;'>" + txt + "</td>");
                }

            });

        }

    }

})

function getNonInputData(that) {
    var $row = $(that).closest("tr").off("mousedown");
    var $tds = "";
    if (!bool3Months)
        $tds = $row.find("td").not(':first').not(':nth-child(2)').not(':nth-child(3)');
    else
        $tds = $row.find("td").not(':first').not(':nth-child(2)').not(':nth-child(3)').not(':nth-child(4)').not(':nth-child(5)').not(':nth-child(6)');

    $.each($tds, function (i, el) {
        var txt = $(this).find("input").val();
        if (txt == "") {
            var td = $(this).html("");
            $(td).append(defaultVal);
        }

        else {
            var td = $(this).html("");
            $(td).append(txt);
        }

    });
}

$(document).on('click', '.previous3Months', function (event) {
    bool3Months = true;
    Glob_pageSize = 9;
    Glob_dateForButtons = new Date().setMonth(new Date().getMonth() - 3);
    $(this).addClass('disabledbutton');
    $('.currentDuration').removeClass('disabledbutton');
    $('.next6Months').removeClass('disabledbutton');
    getDemandForecastSummary(Glob_dateForButtons, Glob_pageSize); //9
})
$(document).on('click', '.currentDuration', function (event) {
    bool3Months = false;
    Glob_pageSize = 6;
    Glob_dateForButtons = new Date();
    $('.previous3Months').removeClass('disabledbutton');
    $('.next6Months').removeClass('disabledbutton');
    $(this).addClass('disabledbutton')
    getDemandForecastSummary(Glob_dateForButtons, Glob_pageSize);
})
$(document).on('click', '.next6Months', function (event) {
    bool3Months = false;
    Glob_pageSize = 12;
    Glob_dateForButtons = new Date();
    $('.previous3Months').removeClass('disabledbutton');
    $('.currentDuration').removeClass('disabledbutton');
    $(this).addClass('disabledbutton')
    getDemandForecastSummary(Glob_dateForButtons, Glob_pageSize);//12
})

/*---------------Save Edited Resources-----------------------*/
var defaultVal = '-';
$(document).on('click', '.saveDraft', function (event) {
    var checkedCount = $(".checkClassDemand:checked").length; var status = ''; var flag = true; var statusArr = []; var txt = [];
    var requestArray = []; var headerArray = [];
    var requestObj = {};
    for (var j = 0; j < checkedCount; j++) {
        var $row = $(".checkClassDemand:checked").closest("tr").off("mousedown");
        var $tds = $row.find("td").not(':nth-child(1)').not(':nth-child(2)').not(':nth-child(3)');
        var resourceArray = [];
        $.each($tds, function (i, el) {
            var $th = $(this).closest('table').find('th').eq($(this).index());
            tHeader = $th[0].innerText;
            var projectID = $(this).closest('tr').children('td:first')[0].id;
            txt = $(this).find("input").val();
            var resourceObj = new Object();
            resourceObj.projectId = projectID;
            resourceObj.role = Glob_logedInUserRole;
            resourceObj.month = tHeader;
            resourceObj.positionCount = txt;
            resourceObj.signum = signumGlobal;
            if (txt != defaultVal && txt != "") {
                resourceArray.push(resourceObj);
            }
        });
    }
    if (checkedCount > 0) {
        postEditedResources(resourceArray);
    }
    else {
        pwIsf.alert({ msg: "Please select any Project" });
    }
})

/*---------------POST API to Save Demand Summary-----------------------*/
function postEditedResources(resourceArray) {
    pwIsf.addLayer({ text: "Please wait ..." });
    $.isf.ajax({
        url: service_java_URL + "demandForecast/saveDemandSummary",
        context: this,
        crossdomain: true,
        processData: true,
        contentType: 'application/json',
        type: 'POST',
        data: JSON.stringify(resourceArray),
        xhrFields: {
            withCredentials: false
        },
        success: function (data) {
            pwIsf.alert({ msg: "Head Counts Successfully Saved!", type: "success" });
            getDemandForecastSummary(new Date(), 6);
        },
        error: function (xhr, status, statusText) {
            dhtmlx.message({ type: "error", text: "An Error occurred!" });
            getDemandForecastSummary(new Date(), 6);
        },
        complete: function (xhr, statusText) {
        }
    });
}

function getAllSignumsForProposal(passGanttRowId = 0) {
    $("#selectUserAllSignumForProposal").autocomplete({

        appendTo: "#modalTransfer",
        select: function (event, ui) {
            let selectedSignum = ui.item.value;
            $('#modalDialog_userAvailabilityModal').css({ width: '850px' });
            getAvailabilityPercentage(selectedSignum, passGanttRowId);

        },
        source: function (request, response) {
            $.isf.ajax({
                url: service_java_URL + "activityMaster/getEmployeesByFilter",
                type: "POST",
                data: {
                    term: request.term
                },
                success: function (data) {
                    $("#selectUserAllSignumForProposal").autocomplete().addClass("ui-autocomplete-loading");

                    var result = [];
                    $.each(data, function (i, d) {
                        result.push({
                            "label": d.signum + "/" + d.employeeName,
                            "value": d.signum
                        });
                    })
                    response(result);
                    $("#selectUserAllSignumForProposal").autocomplete().removeClass("ui-autocomplete-loading");
                }

            });
        },

        minLength: 3

    });
    $("#selectUserAllSignumForProposal").autocomplete("widget").addClass("fixedHeight");


}

var findOccurrence = function (array) {
    "use strict";
    var result = {};
    if (array instanceof Array) { // Check if input is array.
        array.forEach(function (v, i) {
            if (!result[v]) { // Initial object property creation.
                result[v] = [i]; // Create an array for that property.
            } else { // Same occurrences found.
                result[v].push(i); // Fill the array.
            }
        });
    }

    return result;
};

function configureGanttForFM() {

    ganttInstanceForFM.config.grid_resize = true;
    ganttInstanceForFM.config.grid_width = 480;

    ganttInstanceForFM.config.scale_height = 60;
    ganttInstanceForFM.config.scale_unit = "year";
    ganttInstanceForFM.config.date_scale = "%Y";
    ganttInstanceForFM.config.subscales = [
        { unit: "month", step: 1, date: "%M" },
        { unit: "week", step: 1, date: "#%W" }
    ];
    ganttInstanceForFM.templates.tooltip_text = function (start, end, task) {
        return "";
    };
    ganttInstanceForFM.config.show_errors = false;
    var commentFMEditor = { type: "text", map_to: "commentsFM" };

    ganttInstanceForFM.config.columns = [
        {
            name: "signums", label: "Actions", resize: true, align: "center", width: 140, template: taskObj => {
                let detailsParams = JSON.stringify({ rowId: taskObj.id });
                let paramsCapacityNotAvailable = JSON.stringify({ rowId: taskObj.id, positionStatus: 'capacityNotAvailable', actName: 'capacity not available' });
                let paramsCompetenceNotAvailable = JSON.stringify({ rowId: taskObj.id, positionStatus: 'competenceNotAvailable', actName: 'competence not available' });
                if (taskObj.signums) {
                    return `${taskObj.signums} <button class="btn btn-danger btn-xs" style="margin:0px" data-details='${detailsParams}' title="Remove this allocation" onclick="clearAssignment(this);">X</button>`;
                } else {
                    return `<button class="btn btn-success btn-xs" style="margin:0px" data-details='${detailsParams}' onclick="letsAssignSignum(this);">Assign</button>
                        <button class="btn btn-danger btn-xs" title="Capacity Not Available" style="margin:0px" data-details='${paramsCapacityNotAvailable}' onclick="rejectRequest(this);"><i class="fa fa-mail-reply " ></i></button>
                        <button class="btn btn-danger btn-xs" title="Competence Not Available" style="margin:0px" data-details='${paramsCompetenceNotAvailable}' onclick="rejectRequest(this);"><i class="fa fa-mail-reply-all" ></i></button>
                         <button class="btn btn-success btn-xs" id="allInfoPopup" title="More Info" style="margin:0px" data-details='${paramsCompetenceNotAvailable}' onclick="openAllPositionInfoModal(this)"><i class="fa fa-info"></i></button>`;
                }
            }
        },
        { name: "projectScopeName", label: "Deliverable", align: "center", width: 85, resize: true },
        { name: "lstVendorTechModel", label: "Vendor-Tech", align: "center", width: 45, resize: true, template: vendorTechTemplateForFM},
        { name: "demandType", label: "Demand Type", align: "center", width: 95, resize: true },
        { name: "location", label: "Location", align: "center", width: 75, resize: true },
        { name: "jStageJRole", label: "JS/Job Role", align: "center", width: 80, resize: true },
        { name: "positionId_WorkEffortID", label: "PID/WEID", align: "center", width: 70, resize: true },
        { name: "start_date", label: "Start Date", align: "center", width: 75, resize: true },
        { name: "end_date", label: "End Date", align: "center", width: 75, resize: true },
        { name: "commentsFM", label: "Comments", align: "center", width: 75, resize: true, text: "", editor: commentFMEditor }//emeebha

    ];


}

function configureGanttForModal() {
    ganttInstanceForModal.config.grid_resize = true;
    ganttInstanceForModal.config.grid_width = 480;
    ganttInstanceForModal.config.scale_height = 60;
    ganttInstanceForModal.config.scale_unit = "year";
    ganttInstanceForModal.config.date_scale = "%Y";
    ganttInstanceForModal.config.subscales = [
        { unit: "month", step: 1, date: "%M" },
        { unit: "week", step: 1, date: "#%W" }
    ];
    ganttInstanceForModal.config.columns = [
        { name: "serviceArea_SubServiceArea", label: "Sub-ServiceArea", align: "center", width: 120, resize: true  },
        { name: "domain_Subdomain", label: "Sub-Domain", align: "center", width: 120, resize: true  },
        { name: "vendor_tech", label: "Vendor-Tech", align: "center", width: 50, resize: true, template: showMultiVendorTechPositionView},
        { name: "signum", label: "Signum", align: "center", width: 70, resize: true },
        { name: "jobStage", label: "Job Stage", align: "center", width: 80, resize: true },
        { name: "pid_Weid", label: "PID/WEID", align: "center", width: 90, resize: true }
    ];

}

function sendProposalByFM() {

    pwIsf.addLayer({ text: "Please wait ..." });
    let tasksArray = ganttInstanceForFM.getTaskByTime();
    let projID = $('#hidId_selectedProjectId').val();
    let sendData = [];
    for (let i in tasksArray) {
        if (tasksArray[i]['signums']) {

            let weid = tasksArray[i]['workEffortId'];
            let signum = tasksArray[i]['signums'];
            let commentByFM = tasksArray[i]['commentsFM'];
            let allocatedStatus = tasksArray[i]["remote_onsite"];
            let loggedInUser = signumGlobal;
            let sDate = $.format.date(tasksArray[i]['start_date'], 'yyyy-MM-dd');
            let eDate = $.format.date(tasksArray[i]['proposed_date'], 'yyyy-MM-dd');
            let ftePercent = tasksArray[i]["fte"];


            let obj = { "weid": weid, "signum": signum, "allocatedStatus": allocatedStatus, "loggedInUser": loggedInUser, "startDate": sDate, "endDate": eDate, "positionStatus": "Proposed", "ftePercentage": ftePercent, "commentsByFM": commentByFM };

            sendData.push(obj);

        }
    }


    if (sendData.length) {
        let projID = $('#hidId_selectedProjectId').val();
        $.isf.ajax({
            url: service_java_URL + "activityMaster/updateProposedResources?projectId=" + projID,
            method: 'POST',
            data: JSON.stringify(sendData),
            dataType: "json",
            contentType: "application/json; charset=utf-8",
            success: function (data) {
                pwIsf.confirm({
                    title: 'Proposal Sent', type: 'success',
                    'buttons': {
                        'OK': {
                            'action': function () {
                                getGanttForFM(projectId);
                            }
                        }


                    }
                });
                let projectId = $('#hidId_selectedProjectId').val();



            },
            error: function (xhr, status, statusText) {
                console.log('An error occurred in sendProposal() ::' + xhr.responseText);
                pwIsf.removeLayer();
            },
            complete: function () {
                pwIsf.removeLayer();
            }
        });
    } else {
        pwIsf.alert({ msg: "Signum not assigned", type: 'warning' });
        pwIsf.removeLayer();
    }



}

function clearAssignment(eleObj) {
    let params = $(eleObj).data('details');
    let getGanttRowId = params.rowId;

    ganttInstanceForFM.getTask(getGanttRowId).text = '';
    ganttInstanceForFM.getTask(getGanttRowId).signums = '';
    ganttInstanceForFM.updateTask(getGanttRowId);
    ganttInstanceForFM.render();

}

function rejectRequest(eleObj) {
    //Data will refresh,if any signum assigned send them for Proposal first!!! Do you still want to proceed?
    var params = $(eleObj).data('details');

    pwIsf.confirm({
        title: 'Reject request',
        msg: `Do you want to reject this request with <font color="#81baea">${params.actName} </font>?`,
        'buttons': {
            'YES': {
                'action': function () {


                    let rowData = ganttInstanceForFM.getTaskBy(task => task.id == params.rowId);
                    let weid = rowData[0].workEffortId;
                    let positionStatus = params.positionStatus;

                    let sendObj = [{ "weid": weid, "loggedInUser": signumGlobal, "positionStatus": positionStatus }];
                    let projID = $('#hidId_selectedProjectId').val()
                    pwIsf.addLayer({ text: "Please wait ..." });

                    $.isf.ajax({
                        url: service_java_URL + "activityMaster/updateProposedResources?projectId=" + projID,
                        method: 'POST',
                        data: JSON.stringify(sendObj),
                        dataType: "json",
                        contentType: "application/json; charset=utf-8",
                        success: function (data) {


                            pwIsf.alert({ msg: "Rejected", type: 'success' });
                            let projectId = $('#hidId_selectedProjectId').val();

                            getGanttForFM(projectId); // refresh gantt chart
                            pwIsf.removeLayer();
                        },
                        error: function (xhr, status, statusText) {
                            pwIsf.alert({ msg: 'An error occurred in rejectRequest() ::' + xhr.responseText, type: 'error' });
                            pwIsf.removeLayer();
                        }
                    });



                },
                'class': 'btn btn-danger'

            },
            'NO': {
                'action': function () {

                },
                'class': 'btn btn-success'
            },
        }
    });

}

function getProjectScopeDetails(passScopeDetailId) {

    $("#tabProjectScopeDetails td").remove();
    $("#tabProjectScopeDetails").append(`<tr><td style="text-align: center;" colspan="10">${pleaseWaitISF()}</td></tr>`);

    let scopeDetailId = passScopeDetailId;
    $.isf.ajax({
        async: false,
        url: service_java_URL + "activityMaster/getScopeDetailsById?projectScopeDetailID=" + scopeDetailId,
        success: function (data) {
            var row = `<tr><td style="text-align: center;" colspan="10">Data Not Found</td></tr>`;

            if (data) {

                row = ``;
                for (let i in data) {
                    row += `<tr>
                    <td>${data[i].scopeName}</td>
                    <td>${data[i].deliverableUnitName}</td>
                    <td>${data[i].requestType}</td>
                    <td>${new Date(data[i].startDate).toDateString()}</td>
                    <td>${new Date(data[i].endDate).toDateString()}</td>
                    <td>${data[i].domain}</td>
                    <td>${data[i].subDomain}</td>
                    <td>${data[i].serviceArea}</td>
                    <td>${data[i].subServiceArea}</td>
                    </tr>`;
                }
            }

            $("#tabProjectScopeDetails td").remove();
            $("#tabProjectScopeDetails").append(row);

        },
        error: function (xhr, status, statusText) {
            console.log('An error occurred on getProjectScopeDetails: ' + xhr.error);
        }
    });

}

function openAllPositionInfoModal(eleObj) {
    let params = $(eleObj).data('details');
    let rowData = ganttInstanceForFM.getTaskBy(task => task.id == params.rowId);
    if ($.fn.dataTable.isDataTable('#allPositionInfoTable')) {
        oNewTable.destroy();
        $("#allPositionInfoTable").empty();
    }
    getProjectScopeDetails(rowData[0].projectScopeDetailId);
    oNewTable = $('#allPositionInfoTable').DataTable({
        responsive: true,
        "pageLength": 10,
        "data": rowData,
        "destroy": true,
        colReorder: false,
        dom: 'Bfrtip',
        "bPaginate": false,
        "bFilter": false,
        buttons: [],
        "columns": [
            { data: "positionId", title: "PID", align: "center", width: 10, resize: true },
            { data: "workEffortId", title: "WEID", align: "center", width: 10, resize: true },
            { title: "Deliverable", data: "projectScopeName" },
            { data: "sAreaSubSArea", title: "Sub Service Area" },
            { data: "domainSubDomain", title: "Sub-Domain" },
            { data: null, title: "Vendor-Tech", align: "center", width: 20, resize: true,
                "render": function (rowData, type, row, meta) {
                    return vendorTechTemplateForFM(rowData);
                }},
            { data: "demandType", title: "Demand Type", align: "center", width: 20, resize: true },
            { data: "location", title: "Location", align: "center", width: 20, resize: true },
            { data: "resourceType", title: "SP Type", align: "center", width: 20, resize: true },
            { data: "jStageJRole", title: "Job Role", align: "center", width: 20, resize: true },
            { data: "start_date.toDateString()", title: "Start Date", align: "center", width: 20, resize: true },
            { data: "end_date.toDateString()", title: "End Date", align: "center", width: 20, resize: true },
            { data: "remote_onsite", title: "Allocate", align: "center", width: 20, resize: true },
            { data: "duration", title: "Duration", align: "center", width: 20, resize: true },
            { data: "fte", title: "FTE%", align: "center", width: 20, resize: true }
        ],
    });
    $('#modalDialog_allPositionInfo').modal('show');
}


function letsAssignSignum(eleObj) {


    $('#modalDialog_userAvailabilityModal').css({ width: '400px' });


    let params = $(eleObj).data('details');
    let tasksArray = ganttInstanceForFM.getTaskByTime();

    let rowData = ganttInstanceForFM.getTaskBy(task => task.id == params.rowId);
    let weid = rowData[0].workEffortId;

    getAllSignumsForProposal(params.rowId);

    //START - RESET MODAL
    var availabilityPopup = $('#userAvailabilityModal');
    $(availabilityPopup)
        .find('.modal-body')
        .hide();

    $(availabilityPopup)
        .find('.modal-title')
        .html('Select signum');

    $(availabilityPopup)
        .find('.modal-footer .legends')
        .hide();

    $(availabilityPopup).modal('show');


    $('#hidId_selectedSignum').val('');
    $('#hidId_ganttRowId').val('');
    $('#selectUserAllSignumForProposal').val('');
    $('#selectUserAllSignumForProposal').focus();


    //END - RESET MODAL



}

function getAvailabilityPercentage(signum, passGanttRowId) {

    let rowData = ganttInstanceForFM.getTaskBy(task => task.id == passGanttRowId);
    let weid = rowData[0].workEffortId;

    var serviceUrl = service_java_URL + "resourceManagement/getBookedResourceBySignum?signum=" + signum + "&weid=" + weid;
    if (ApiProxy == true) {
        serviceUrl = service_java_URL + "resourceManagement/getBookedResourceBySignum?signum=" + signum + encodeURIComponent("&weid=" + weid);
    }
    $.isf.ajax({
        url: serviceUrl,
        success: function (data) {

            let passObj = { signum: signum, percent: data.availability, ganttRowId: passGanttRowId };
            getAvailabilityOfUser(passObj);

        },
        error: function (xhr, status, statusText) {

            console.log('An error occurred in getAvailabilityPercentage() ::' + xhr.responseText);
        }
    });


}

function getAvailabilityOfUser(paramObj) {

    let rowData = ganttInstanceForFM.getTaskBy(task => task.id == paramObj.ganttRowId);
    let weid = rowData[0].workEffortId;

    let signum = paramObj.signum;

    var params = { 'signum': signum, 'percent': paramObj.percent, ganttRowId: paramObj.ganttRowId };

    var availabilityPopup = $('#userAvailabilityModal');


    $(availabilityPopup)
        .find('.modal-title')
        .html('Availability for ' + signum);

    $(availabilityPopup)
        .find('.modal-body')
        .show()
        .html(pleaseWaitISF({ text: 'Please wait ...', textCss: 'font-size:12px;' }));

    $(availabilityPopup)
        .find('.modal-footer .legends')
        .show();

    $(availabilityPopup).modal('show');
    var serviceUrl = service_java_URL + "resourceManagement/getBookedResourceBySignum?signum=" + signum + "&weid=" + weid;
    if (ApiProxy == true) {
        serviceUrl = service_java_URL + "resourceManagement/getBookedResourceBySignum?signum=" + signum + encodeURIComponent("&weid=" + weid);
    }
    $.isf.ajax({
        url: serviceUrl,
        success: function (data) {

            makeavailabilityStructure(data, availabilityPopup, params);

        },
        error: function (xhr, status, statusText) {

            console.log('An error occurred in getAvailabilityOfUser() ::' + xhr.responseText);
        }
    });


}

function acceptSlot(thisObj) {

    let selectedSignum = $('#hidId_selectedSignum').val();
    let ganttRowId = $('#hidId_ganttRowId').val();
    let edate = $('#availability_edate').val();

    ganttInstanceForFM.getTask(ganttRowId)['text'] = selectedSignum + `  (Till: ${edate})`;
    ganttInstanceForFM.getTask(ganttRowId)['proposed_date'] = new Date(edate);
    ganttInstanceForFM.getTask(ganttRowId).signums = selectedSignum;
    ganttInstanceForFM.updateTask(ganttRowId);
    ganttInstanceForFM.render();


}

function makeavailabilityStructure(AvailabileData, modal, params) {

    let rowData = ganttInstanceForFM.getTaskBy(task => task.id == params.ganttRowId);

    //SET HIDDEN PARAMS
    $('#hidId_selectedSignum').val(params.signum);
    $('#hidId_ganttRowId').val(params.ganttRowId);

    var monthNameArr = [];
    var dtData = AvailabileData.details;

    var recommendedSdate = AvailabileData.recommendedStartDate;
    var recommendedEdate = AvailabileData.recommendedEndDate;

    var positionSdate = $.format.date(rowData[0].start_date, "yyyy-MM-dd");//$('#lbl_selected_signum_' + params.nearLblUid).data('sdate');
    var positionEdate = $.format.date(rowData[0].end_date, "yyyy-MM-dd");//$('#lbl_selected_signum_' + params.nearLblUid).data('edate');
    var availablePercentage = AvailabileData.availability;
    var percentageBarClass = 'p_bar_green_color';

    var makeEndDate = positionEdate;
    if ($('#chk_propose_partially_' + params.nearLblUid).is(':checked')) {
        makeEndDate = $('#lbl_selected_signum_' + params.nearLblUid).attr('data-edate');
    }

    for (var mk in dtData) {
        if (!monthNameArr.includes(dtData[mk].month_name)) {
            monthNameArr.push(dtData[mk].month_name);
        }
    }

    //set modal title
    $(modal)
        .find('.modal-title').css({ 'font-size': '15px' })
        .html('Availability for ' + params.signum + ' [ Between ' + positionSdate + ' and ' + positionEdate + ' ]');

    percentageBarClass = (availablePercentage <= 70 && availablePercentage >= 40) ? 'p_bar_yellow_color' : (availablePercentage < 40) ? 'p_bar_red_color' : 'p_bar_green_color';


    var progressBarDiv = '<div class="progress ' + percentageBarClass + '">'
        + '<div class="progress-bar" role="progressbar" aria-valuenow="10" aria-valuemin="0" aria-valuemax="100" style="width: ' + availablePercentage + '%;">'
        + '<span>Availability ' + availablePercentage + '%</span>'
        + '</div>'
        + '</div>';
    progressBarDiv = '';  // stop printing of progress bar

    var dateRangeDiv = '';
    dateRangeDiv = '<div class="date_range">'
        + '<label style="padding-right:10px;">Booking Slot from </label>'
        + '<input class="datepicker" readonly name="availability_sdate" id="availability_sdate" data-date-format="yyyy-mm-dd" value="' + positionSdate + '" style="background-color:#f1f1f1;">'
        + '<label style="padding-right:10px;padding-left:10px;"> to </label>'
        + '<input class="datepicker" readonly name="availability_edate" id="availability_edate" data-date-format="yyyy-mm-dd" value="' + makeEndDate + '">'
        + '<button type="button" class="btn btn-primary" style="padding: 3px 10px;margin-top: 6px;margin-left: 12px;"  data-dismiss="modal" data-lblid="' + params.nearLblUid + '" onclick="acceptSlot(this)" >Assign</button>'
        + '</div>';



    var detailsDiv = '<div class="details_div">' + progressBarDiv + dateRangeDiv + '</div>';

    var lblsStr = '<div class="dt_box_label"><div class="dt_label">Date</div><div class="hr_part"><div class="req_hr_label"><span class="hr_caption">Req. Hr</span></div><div class="avai_hr_label"><span class="hr_caption">Ava. Hr</span></div></div></div>';

    var monthNameStr = '';
    var finalStr = '';
    var startRangeFlag = false;

    for (var k in monthNameArr) {
        var monthName = monthNameArr[k];

        monthNameStr = '<div class="month_name">' + monthName + '</div>';
        var dtBoxStr = '';

        for (var inK in dtData) {

            var dtRowStr = '<div class="dt_row">';


            if (monthNameArr[k] == dtData[inK].month_name) {

                if (!dtData[inK].isweekend) {

                    var d = new Date(dtData[inK].dt);
                    var getDateNum = d.getDate();

                    var reqHr = dtData[inK].req_hr;
                    var avaHr = dtData[inK].avail_hr;

                    var avaHrCss = '';

                    if (avaHr >= reqHr) {
                        if (startRangeFlag) {
                            avaHrCss = 'available with_in_range';
                        } else {
                            avaHrCss = 'availableOutofRange';
                        }
                        if (recommendedSdate == dtData[inK].dt) {
                            avaHrCss = 'available start_slot with_in_range';
                            startRangeFlag = true;
                        }
                        if (recommendedEdate == dtData[inK].dt) {
                            avaHrCss = 'available end_slot with_in_range';
                            startRangeFlag = false;
                        }
                    } else {
                        avaHrCss = 'notAvailable';
                    }

                    dtBoxStr += '<div class="dt_box">'
                        + '<div class="dt">' + getDateNum + '</div>'
                        + '<div class="hr_part">'
                        + '     <div class="req_hr">'

                        + '         <span class="req_hr_value">' + reqHr + '</span>'
                        + '    </div>'
                        + '     <div class="avai_hr ' + avaHrCss + '">'

                        + '        <span class="hr_value">' + avaHr + '</span>'
                        + '    </div>'
                        + ' </div>'
                        + ' </div>';
                } else {
                    dtBoxStr += '<div class="dt_box_weekend"></div>';
                }
            }

        }

        finalStr += monthNameStr + dtRowStr + lblsStr + dtBoxStr + '</div>';


    }

    finalStr = detailsDiv + '<div class="dt_availability_parent_div">' + finalStr + '</div>';
    $(modal)
        .find('.modal-body')
        .html(finalStr);

    $('#availability_edate').datepicker({
        format: 'yyyy-mm-dd',
        startDate: positionSdate,
        autoclose: true,
        minDate: new Date(positionSdate),

    });

}

function getColor(PositionStatus) {

    return statusColorArray[PositionStatus];//[PositionStatus.toLowerCase()];

}

function getGanttForFM(projectID) {
    Glob_projectID = projectID;
    pwIsf.addLayer({ text: "Please wait ..." });
    projectNo = projectID;
    var kt; var objk = {};
    var serviceUrl = `${service_java_URL}demandForecast/getDemandForecastDetailsByfilter?positionStatus=Proposal Pending&projectID=${projectID}`;
    if (ApiProxy) {
        const encodedUriContent = encodeURIComponent(`&projectID=${projectID}`);
        serviceUrl = `${service_java_URL}demandForecast/getDemandForecastDetailsByfilter?positionStatus=Proposal Pending${encodedUriContent}`;
    }
    $.isf.ajax({
        type: "GET",
        url: serviceUrl,
        success: appendPlanSourcesFM,
        error: function (msg) {
            pwIsf.removeLayer();
        },
        complete: function (msg) {
            //BLANK
        }
    });


    function appendPlanSourcesFM(data) {
        if (data.length) {
            ganttInstanceForFM.attachEvent("onBeforeParse", function () { ganttInstanceForFM.clearAll(); });
            kt = data;
            objk.data = kt;
            $.each(objk.data, function (i, d) {
                if (d.demandType === null) {
                    d.demandType = "";
                }
            })
            ganttInstanceForFM.config.show_errors = false;
            ganttInstanceForFM.config.server_utc = true;
            ganttInstanceForFM.config.drag_move = false;
            ganttInstanceForFM.config.drag_progress = false;
            ganttInstanceForFM.config.drag_resize = false;
            ganttInstanceForFM.attachEvent("onBeforeGanttRender", function () {
                $(".gantt_grid_data").on('scroll', function () {
                    $(".gantt_data_area").scrollTop($(this).scrollTop());
                });
                $(".gantt_data_area").on('scroll', function () {
                    $(".gantt_grid_data").scrollTop($(this).scrollTop());
                });
            });
            ganttInstanceForFM.init("gantt_for_FM");
            ganttInstanceForFM.parse(objk);
            $(".ganttControlls").show();
            $(".pull-left").show();
        }
        else {
            pwIsf.alert({ msg: C_NO_DATA_FOUND, type: C_WARNING });
            $(".pull-left").hide();
            $("#headerForTable").hide();
            $(".ganttControlls").hide();
            $("#gantt_for_FM").hide();
        }

        pwIsf.removeLayer();
        ganttInstanceForFM.attachEvent('onBeforeLightbox', function (id) {
            return false;
        });

    }
}

/*---------------On Click of Details;Open Slider for Details View-----------------------*/
function opendetailsview(that, projectID) {
    if ($('.checkClassDemand').prop("checked"))
        getNonInputData(that);
    $.isf.ajax({
        url: service_java_URL + "projectManagement/getProjectDetails?ProjectID=" + projectID,
        async: false,
        success: function (data) {
            //var todayDate = new Date();
            //if (new Date(data.StartDate) > todayDate)
            Glob_projectStartDateConfig = new Date(data.startDate);
            //else
            //Glob_projectStartDateConfig = todayDate;
        }
    });
    $('.checkClassDemand').prop("checked", false);
    if (Glob_logedInUserRole != "Fulfillment Manager") {
        $("#tooltiplegends").removeAttr("style");
        populateDataServerList();
        var serviceUrl = service_java_URL + "demandForecast/isDraftAllowed?projectId=" + projectID + "&role=" + Glob_logedInUserRole;
        if (ApiProxy)
            serviceUrl = service_java_URL + 'demandForecast/isDraftAllowed?' + encodeURIComponent("projectId=" + projectID + "&role=" + Glob_logedInUserRole);

        $.isf.ajax({
            //url: service_java_URL + "demandForecast/isDraftAllowed?projectId=" + projectID + "&role=" + Glob_logedInUserRole,
            url: serviceUrl,
            success: function (data) {
                if (!data) {
                    if (Glob_logedInUserRole == "Project Manager") {
                        $(".saveDraftDetails ").css("display", "none");

                    }
                    else if (Glob_logedInUserRole == "Resource Planning Manager") {
                        $('#gantt_here_demand').css('pointer-events', 'none');
                        $(".saveDraftDetails ").css("display", "none");
                        $(".sendNextDetails ").css("display", "none");

                        pwIsf.alert({ msg: 'Edit is not allowed once sent to SPM!', type: 'info' });
                    }
                }
                else {
                    $('#gantt_here_demand').css('pointer-events', '');
                }

            }
        });
    }


    $(".saveDraftDetails ").removeAttr("style");
    $(".sendNextDetails ").removeAttr("style");

    $('.calendar_panel').css('z-index', '12');
    var projectDetails = $(that).closest('td').next('td')[0].textContent;
    if (Glob_logedInUserRole == "Project Manager") {
        $('.sendNextDetails')[0].innerHTML = "Send to FM&nbsp;<i class='fa fa-arrow-right'></i>";
        $('.sendBackRPM').removeAttr("style");

    }

    getResourceTable(that, projectID); //Get Table in 2nd view

    if (Glob_logedInUserRole === C_ROLE_FM) {
        ganttInstanceForFM = Gantt.getGanttInstance();
        ganttInstanceForModal = Gantt.getGanttInstance();
        configureGanttForFM();
        configureGanttForModal();
        $('#gantt_for_FM').show();
        $('#gantt_here_demand').hide();
        $('.saveDraftDetails').hide();
        $('.sendNextDetails').hide();
        $('.proposalbtn').show();
        $('.sendProposalbtn').show().off('click').on('click', function () { sendProposalByFM(); });
        $('.othersbtn').show().off('click').on('click', function () {
            showdataforFM(projectID);
            $("#calendar_title_Modal").text = projectID;
            $("#calendar_title_Modal")[0].innerHTML = `<span>Demand Position View : </span>
            <label style="font-size:smaller">${projectDetails}</label>`;
        });
        getGanttForFM(projectID);
    }
    else {
        ganttInstanceForModal = Gantt.getGanttInstance();
        configureGanttForModal();
        $('#gantt_for_FM').hide();
        $('#gantt_here_demand').show();
        $('.saveDraftDetails').show();
        $('.sendNextDetails').show();
        $('.sendProposalbtn').hide();
        $('.proposalbtn').hide();
        $('.othersbtn').show().off('click').on('click', function () {
            showdataforFM(projectID);
            $("#calendar_title_Modal").text = projectID;
            $("#calendar_title_Modal")[0].innerHTML = '<span>Demand Position View : </span><label style="font-size:smaller">' + projectDetails + '</label>';
        });
        getGantt(projectID); //Get Gantt Data in 2nd View
    }

    $('.monthInput').prop("checked", true);


    $("#hidId_selectedProjectId").val(projectID);
    $("#calendar_title").text = projectID;
    $("#calendar_title")[0].innerHTML = '<span>Demand Details View : </span><label style="font-size:smaller">' + projectDetails + '</label>';
    var div = $("#calendar_view");
    div.show();
    div.animate({ right: '0%', opacity: 1.0 }, "slow");
}

function colorize_task(task_object) {
    let col = getColor(task_object.PositionStatus);
    task_object.color = col;
}

function showdataforFM(projectID) {
    pwIsf.addLayer({ text: C_PLEASE_WAIT });
    $("#demandFMModal").modal('show');
    $("#DV_projectId").val(projectID);
    projectNo = projectID;
    var kt; var objk = {};
    var URLvar = "";
    if (Glob_logedInUserRole === C_ROLE_FM) {
        URLvar = `${service_java_URL}resourceManagement/getAllPositions?spoc=${signumGlobal}&projectId=${projectID}`;
    }
    else{
        URLvar = `${service_java_URL}resourceManagement/getAllPositions?projectId=${projectID}`;
    }

    $.isf.ajax({
        type: C_API_GET_REQUEST_TYPE,
        url: URLvar,
        success: appendPlanSourcesFM_modal,
        error: function (msg) {
            pwIsf.removeLayer();
            $("#DV_projectId").val(projectID);
        },
        complete: function (msg) {
            //BLANK
        }
    });

    function appendPlanSourcesFM_modal(data) {
        if (data.length) {
            ganttInstanceForModal.attachEvent("onBeforeParse", function () {
                ganttInstanceForModal.clearAll();
            });
            kt = data;
            objk.data = kt;
            ganttInstanceForModal.config.readonly = true;
            ganttInstanceForModal.config.drag_move = false;
            ganttInstanceForModal.config.drag_progress = false;
            ganttInstanceForModal.config.drag_resize = false;
            ganttInstanceForModal.templates.tooltip_text = function (start, end, task) {
                return "";
            };
            ganttInstanceForModal.config.show_errors = false;
            ganttInstanceForModal.config.static_background = true;
            ganttInstanceForModal.init("demandBody");
            ganttInstanceForModal.clearAll();
            ganttInstanceForModal.parse(objk);

            ganttInstanceForModal.attachEvent('onBeforeLightbox', function (id) {
                return false;
            });
            ganttInstanceForModal.eachTask(function (task) {
                colorize_task(task);
            });
            ganttInstanceForModal.templates.task_text = function (start, end, task) {
                return `<span style='color:black;float:left;padding-left:10px;'>${task.text}</span>`;
            };
            let eveOnGanttRender = ganttInstanceForModal.attachEvent("onGanttRender", function () {
                pwIsf.removeLayer();
                console.log('onGanttRender')
            });
            ganttInstanceForModal.render();
            ganttInstanceForModal.detachEvent(eveOnGanttRender);
            $('.modal-backdrop.fade.in').hide();
            $(".ganttControlls").show();
            createGanttCustomLegends();
        }
        else {
            pwIsf.alert({ msg: C_NO_DATA_FOUND, type: C_WARNING });
            $('#demandBody').html('').append('<label><b >No Records Found</b></label>');
            pwIsf.removeLayer();
            $('.modal-backdrop.fade.in').hide();
            $(".ganttControlls").hide();
        }

    }
}

function getResourceTable(that, projectID) {
    Glob_resourceSum = 0;
    $('#resourceTable').empty();
    var currentRow = that.closest('tr');
    var $tds = $(currentRow).find("td").not(':first').not(':nth-child(2)').not(':nth-child(3)');
    $.each($tds, function (i, el) {
        Glob_resourceSum += Math.ceil($(this).text());
    });
    var firstRow = currentRow.innerHTML;
    var secondRow = currentRow.parentNode.rows[currentRow.rowIndex].innerHTML;
    var thirdRow = currentRow.parentNode.rows[currentRow.rowIndex + 1].innerHTML;
    var thead = $(that).closest('table').find('thead')[0].innerHTML;
    var tfoot = $(that).closest('table').find('tfoot')[0].innerHTML;
    var tableHTML = '<thead>' + thead + '</thead><tbody>' + '<tr>' + firstRow + '</tr><tr>' + secondRow + '</tr><tr>' + thirdRow + '</tr></tbody>';
    $('#resourceTable').append(tableHTML);
    $("#resourceTable th:first-child").remove();
    $("#resourceTable td:first-child").remove();
}

/*---------------Close Details View Slider-----------------------*/
function CloseDetailsView() {
    var div = $("#calendar_view");
    div.animate({ right: '-3%' }, "slow");
    div.hide();
}

function getJobRoles() {

    $.isf.ajax({
        url: service_java_URL + "resourceManagement/getJobRoles",
        success: function (data) {
            var arrJobRoles = [];
            var objJobRoles = {};
            $.each(data, function (i, d) {
                objJobRoles.key = d.JobRoleID;
                objJobRoles.label = d.JobRoleName;
                arrJobRoles.push(objJobRoles);
            })
            return arrJobRoles;

        },
        error: function (xhr, status, statusText) {
            console.log("Fail " + xhr.responseText);
            console.log('An error occurred');
        }
    });
}

function getVendorsDR() {
    $.isf.ajax({
        url: service_java_URL + "activityMaster/getVendorDetails",
        success: function (data) {
            $.each(data, function (i, d) {
                vendorData = data;
                $('#select_vendorDR').append('<option value="' + d.vendorID + '">' + d.vendor + '</option>');
                $('#select_vendorEditDR').append('<option value="' + d.vendorID + '">' + d.vendor + '</option>');
            })
        },
        error: function (xhr, status, statusText) {
            console.log('An error occurred');
        }
    });
}

function getOnsiteCountries() {
    url = service_CountryCity + "country/all/?key=" + BATTUTA_KEY + "&callback=?";
    $.getJSON(url, function (countries) {
        $.each(countries, function (key, country) {
            $('#select_OnsiteCountry').append('<option value="' + country.code + '">' + country.name + '</option>');
            $('#select_OnsiteCountryEdit').append('<option value="' + country.code + '">' + country.name + '</option>');
        });
    });
}

function getOnSiteStates() {
    $("#select_OnsiteState option").remove();
    $("#select_OnsiteCity option").remove();
    var cod = $("#select_OnsiteCountry").val();
    url = service_CountryCity + "region/" + cod + "/all/?key=" + BATTUTA_KEY + "&callback=?";
    $.getJSON(url, function (cities) {

        $('#select_OnsiteState').append('<option value="0">Select one</option>');
        $.each(cities, function (key, city) {
            $('#select_OnsiteState').append('<option value="' + city.region + '">' + city.region + '</option>');
        });
    });
}

function getOnSiteCities() {

    var coun = $("#select_OnsiteCountry option:selected").val();
    var st = $("#select_OnsiteState option:selected").val();
    var coordinates = "";
    $("#select_OnsiteCity option").remove();
    url = service_CountryCity + "city/" + coun + "/search/?region=" + st + "&key=" + BATTUTA_KEY + "&callback=?";
    $.getJSON(url, function (cities) {
        $('#select_OnsiteCity').append('<option value="0">Select one</option>');
        $.each(cities, function (key, city) {
            coordinates = +"" + city.latitude + "," + city.longitude + "";
            $('#select_OnsiteCity').append('<option value="' + coordinates + '">' + city.city + '</option>');
        });
    });
}

function callGanttZoom(thisObj) {

    pwIsf.addLayer({ text: "Please wait ..." });

    setTimeout(function () {

        if ($(thisObj).data('ganttpopup')) {
            zoom_tasks(thisObj, ganttInstanceForModal);
        } else {
            if (Glob_logedInUserRole == "Fulfillment Manager") {
                zoom_tasks(thisObj, ganttInstanceForFM);

            } else {
                zoom_tasks(thisObj, ganttDemand);
            }
        }


    }, 100);





}

var arrServSubServLst = [];
var arrDomSubDom = [];
var arrTech = [];

function getExistingData() {
    if (arrServSubServLst.length > 0) {
        var data = ganttDemand.getTaskByTime();
        $.each(data, function (i, d) {
            var objServ = {};
            objServ.key = d.ServiceAreaID;
            objServ.label = d.ServiceArea;
            arrServSubServLst.push(objServ);
            var objDom = {};
            objDom.key = d.domainID;
            objDom.label = d.Domain_SubDomain;
            arrDomSubDom.push(objDom);
            var objTech = {};
            objTech.key = d.technologyID;
            objTech.label = d.Technology;
            arrTech.push(objTech);
        });
    }

}

var GanttData = [];

function GetSubScopebyScopeID(scope_id, ganttDemand, arrServSubServ, arrDomSubDom, arrTech, state) {
    GanttData = ganttDemand.getTaskByTime();
    //let service_java_URL = 'http://100.97.133.2:8080/isf-rest-server-java/';
    $.isf.ajax({
        url: service_java_URL + "activityMaster/getSubScopebyScopeID?projectScopeID=" + scope_id,
        success: function (data) {
            if (data.isValidationFailed == false) {
                $.each(data.responseData, function (i, d) {
                    var objServSubServ = {};
                    objServSubServ.key = d.ServiceAreaID;
                    objServSubServ.label = d.ServiceArea;
                    arrServSubServLst.push(objServSubServ);
                })
            }
            //else {
            //    pwIsf.alert({ msg: data.formErrors[0], type: 'error' });
            //}
            ganttDemand.updateCollection("serviceArea", arrServSubServLst);


        },
        complete: function (xhr, statusText) {
            arrServSubServ = [];

            $.each(GanttData, function (i, d) {
                ganttDemand.getTask(d.id).projectScopeId = scope_id;
                ganttDemand.getTask(d.id).serviceAreaID = "";
                ganttDemand.getTask(d.id).serviceArea = d.serviceArea;
                ganttDemand.getTask(d.id).domainID = "";
                ganttDemand.getTask(d.id).domain_subDomain = d.domain_subDomain;
                ganttDemand.getTask(d.id).technologyID = "";
                ganttDemand.getTask(d.id).tech = d.tech;
                if (d.id == state.id) {
                    ganttDemand.getTask(d.id).domain_subDomain = "";
                    ganttDemand.getTask(d.id).serviceArea = "";
                    ganttDemand.getTask(d.id).tech = "";
                }
                ganttDemand.updateTask(d.id);
            });

            ganttDemand.render();
            pwIsf.removeLayer();
        },
        error: function (xhr, status, statusText) {
            arrServSubServ = [];
            ganttDemand.updateCollection("serviceArea", arrServSubServ);
        }

    });
}

function GetDomainSubdomain(scopeID, servArea_id, state) {
    var serviceUrl = service_java_URL + "demandForecast/getDomainSubdomain?projectScopeID=" + scopeID + "&serviceAreaID=" + servArea_id;
    if (ApiProxy) {
        serviceUrl = service_java_URL + "demandForecast/getDomainSubdomain?" + encodeURIComponent("projectScopeID=" + scopeID + "&serviceAreaID=" + servArea_id)
    }
    else {

    }
    $.isf.ajax({
        url: serviceUrl,
        success: function (data) {
            $.each(data, function (i, d) {
                var objDomSubDom = {};
                objDomSubDom.key = d.domainID;
                objDomSubDom.label = d.Domain_SubDomain;
                arrDomSubDom.push(objDomSubDom);
            })
            ganttDemand.updateCollection("domain", arrDomSubDom);
        },
        complete: function (xhr, statusText) {
            pwIsf.removeLayer();
            ganttDemand.getTask(state.id).projectScopeId = scopeID;
            ganttDemand.getTask(state.id).serviceAreaID = servArea_id;
            ganttDemand.updateTask(state.id);
            ganttDemand.render();
        },
        error: function (xhr, status, statusText) {
            arrDomSubDom = [];
            ganttDemand.updateCollection("domain", arrDomSubDom);
        }

    });
}

function GetTechnologies(ScopeID, ServiceAreaID, DomainID, state) {
    var serviceUrl = service_java_URL + "demandForecast/getTechnologies?projectScopeID=" + ScopeID + "&serviceAreaID=" + ServiceAreaID + "&domainID=" + DomainID;
    if (ApiProxy == true) {

        serviceUrl = service_java_URL + "demandForecast/getTechnologies?projectScopeID=" + encodeURIComponent(ScopeID + "&serviceAreaID=" + ServiceAreaID + "&domainID=" + DomainID);
    }
    $.isf.ajax({
        url: serviceUrl,
        success: function (data) {
            $.each(data, function (i, d) {
                var objTech = {};
                objTech.key = d.technologyID;
                objTech.label = d.Technology;
                arrTech.push(objTech);
            })
            ganttDemand.updateCollection("technology", arrTech);
        },

        complete: function (xhr, statusText) {
            pwIsf.removeLayer();

            ganttDemand.getTask(state.id).projectScopeId = ScopeID;
            ganttDemand.getTask(state.id).serviceAreaID = ServiceAreaID;
            ganttDemand.getTask(state.id).domainID = DomainID;
            ganttDemand.updateTask(state.id);
            ganttDemand.render();
        },
        error: function (xhr, status, statusText) {
            arrTech = [];
            ganttDemand.updateCollection("technology", arrTech);
        }

    });
}

function viewProjectDocs() {
    $('#modal_showProjectDocs').modal('show');

    let projectId = Glob_projectID;

    $.isf.ajax({
        async: false,
        url: service_java_URL + "projectManagement/getProjectDetails?ProjectID=" + projectId,
        success: function (data) {

            var row = `<tr><td colspan="2">Data Not Found</td></tr>`;
            if (data) {
                if (data.projectDocuments.length) {
                    row = "";
                    $.each(data.projectDocuments, function (i, d) {

                        row += "<tr>";
                        row += "<td class='type'>" + d.documentType;
                        row += "</td>";

                        row += "<td><a id='demandURL' href=" + d.documentLinks + " target='_blank'>" + d.documentLinks;
                        row += "</a></td>";

                        row += "</td>";
                    });
                }
            }

            $("#table_project_documents td").remove();
            $("#table_project_documents").append(row);


        },
        error: function (xhr, status, statusText) {
            console.log('An error occurred on getProjectDetails: ' + xhr.error);
        }
    })


}

/*-------------Show Vendor-Tech Name on Gantt for FM -----------------*/
function vendorTechTemplateForFM(task) {
    if (task.lstVendorTechModel.length > 1) {
        arrVendorTechForFM = [];
        $.each(task.lstVendorTechModel, function (i, d) {
            arrVendorTechForFM.push(d.vendorTech);
        })

        return `<span onclick="showMultiVendorTechView(${task.id})">
            <input id="selectedVendorTech_${task.id}" type="hidden" value="${arrVendorTechForFM}"/>
            <u style="color:blue;cursor:pointer;"  title="Click to View All Vendor-Tech Combinations">View Multi</u>
            </span>`;
    }
    else {
        if (task.lstVendorTechModel !== "") {
            arrVendorTechForFM = task.lstVendorTechModel[0].vendorTech;
            return `<span>
            <input id="selectedVendorTech_${task.id}" type="hidden" value="${arrVendorTechForFM}"/>
            ${arrVendorTechForFM}
            </span>`;
        }
        else {
            arrVendorTechForFM = [];
            return `<span>
            <input id="selectedVendorTech_${task.id}" type="hidden" value="${arrVendorTechForFM}"/>
            ${arrVendorTechForFM}
            </span>`;
        }
    }
}

function showMultiVendorTechPositionView(task) {
    if (task.vendor_tech.length > 1) {
        return `<span onclick="showMultiVendorTechView(${task.id})">
            <input id="selectedVendorTech_${task.id}" type="hidden" value="${task.vendor_tech}"/>
            <u style="color:blue;cursor:pointer;"  title="Click to View All Vendor-Tech Combinations">View Multi</u>
            </span>`;
    }
    else {
        if (task.vendor_tech !== "" && task.vendor_tech !== null) {
            console.log(task.vendor_tech);
            return `<span>
            <input id="selectedVendorTech_${task.id}" type="hidden" value="${task.vendor_tech}"/>
            ${task.vendor_tech}
            </span>`;
        }
        else {
            return `<span>
            <input id="selectedVendorTech_${task.id}" type="hidden" value="${task.vendor_tech}"/>
            ${task.vendor_tech}
            </span>`;
        }
    }
}
