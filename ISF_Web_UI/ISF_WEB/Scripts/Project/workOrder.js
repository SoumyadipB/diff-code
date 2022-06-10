window.onbeforeunload = function (event) {
    if ((typeof oTableWOPlan != "undefined") && (typeof oTableWOPlanWO != "undefined")) {
        var state1 = oTableWOPlan.state();
        if (state1) {
            oTableWOPlan.state.clear();
        }
        var state2 = oTableWOPlanWO.state();
        if (state2) {
            oTableWOPlanWO.state.clear();
        }
    }
};

var nodeNamesValidated = false;
var woListOfNode;
var modalCreatedBy = "";
var woProjectID = localStorage.getItem("views_project_id");
var scopeID = 0;
var wOIDglobal;
var prioritywp = '';
var dailywp = [];
var hourlywp = [];
var weeklywp = '';
var recurrwp = '';
var periodicitywp = 'Single';
var nodeNameClicked = 0;
var select1wp = '';
var execPlan = false;
var nodesValidatedwp = false;
var startDate = "";
var endDate = "";
var woStatus = "";
var durationText = "";
var statusText = "";
var wpStatus = false;
var startDateWO = "";
var endDateWO = "";
var woStatusWO = "";
var durationTextWO = "";
var statusTextWO = "";
var projectID = localStorage.getItem("views_project_id");
var flag = 0;

/* Function to hide or show WO count*/
function changeWOCountWP(el) {
    const C_COUNT_DIV_WP_ID = '#woCountDivWP';
    const C_COUNT_WP_ID = '#woCountWP';

    if (flag === 3) {
        if ($(el).val() === "" || $(el).val() == null) {
            $(C_COUNT_DIV_WP_ID).show();
            $(C_COUNT_WP_ID).val('1');
        }
        else {
            $(C_COUNT_DIV_WP_ID).hide();
            $(C_COUNT_WP_ID).val('');
        }
    }
}

function changeHourlywp(el) {
    const C_WEEK_LABEL = '.weeklywp label ';
    const C_DAILY_LABEL = '.dailywp label ';

    if ($(C_WEEK_LABEL).hasClass('active')) {
        $(C_WEEK_LABEL).removeClass('active');
    }
    if ($(C_DAILY_LABEL).hasClass('active')) {
        $(C_DAILY_LABEL).removeClass('active');
    }
    if (weeklywp !== '') {
        weeklywp = '';
    }
    if (dailywp != null) {
        dailywp = [];
    }
    const index = hourlywp.indexOf(el.value);
    if (index > -1) {
        hourlywp.splice(index, 1);
    }
    else {
        hourlywp.push(el.value)
    }
    console.log(dailywp);
    console.log(weeklywp);
    console.log(hourlywp);
}

$(document).ready(function (e) {
    $("#WOViewButtons").html("");
    $("#WOViewButtons").html('<a class="fa fa-share-square-o" style="color: blue; " id="massTransferWOView" href="#" title="Mass Transfer"></a> <a href="#" class="icon-delete first_delete" title="Delete" name="WO View" onclick="multipleWODelete(this)">' + getIcon('delete') + '</a><a href="#" class="pull-right" data-toggle="tooltip" title="Click to Download Deliverable Order View Data" id="btnDownloadWOViewData" onclick="downloadWOFile(this)"><i class="fa fa-download glyphicon-green" style="padding:4px;background:#129c12;color:white;"></i></a>');
    $("#WOPlanViewButtons").html('<a href="#" class="icon-delete first_delete" title="Delete"name="WO Plan View"  onclick="deleteWOPlanOrWO(this)">' + getIcon('delete') + '</a>');
    $("#start_date_mtWO").datepicker({ dateFormat: "yy-mm-dd", minDate:new Date() }).datepicker("setDate", new Date());
    $('#table_work_order').DataTable();
    $("#Start_Date_WP").datepicker({
        dateFormat: "yy-mm-dd"
    });
    $("#End_Date_wp").datepicker({
        dateFormat: "yy-mm-dd"
    });
    $('#workOrderViewIdPrefix_start_date').datepicker({
        dateFormat: "yy-mm-dd",
        minDate: new Date()
    });
    $('#workOrderViewIdPrefix_start_date').on("change", function (){
    var currentTime = new Date();
    const date = $("#workOrderViewIdPrefix_start_date").val();
    const time = $("#workOrderViewIdPrefix_start_time").val() + ":00";
    const dateTime = `${date} ${time}`;
    const dateTimeConverted = new Date(dateTime);
        if (dateTimeConverted < currentTime) {
            pwIsf.alert({ msg: "Invalid Entry! Start Time can not be before current date and time. " });
            document.getElementById('workOrderViewIdPrefix_start_time').value = `${currentTime.getHours()}:${currentTime.getMinutes()}`;
        }
    });
    $("#workOrderViewIdPrefix_start_time").on("focusout", function (e) {
        var currentTime = new Date();
        const date = $("#workOrderViewIdPrefix_start_date").val();
        const time = $("#workOrderViewIdPrefix_start_time").val()+":00";
        const dateTime = `${date} ${time}`;
        const dateTimeConverted = new Date(dateTime);
        if (dateTimeConverted < currentTime) {
            pwIsf.alert({ msg: "Invalid Entry! Start Time can not be before current date and time. " });
            document.getElementById('workOrderViewIdPrefix_start_time').value = `${currentTime.getHours()}:${currentTime.getMinutes()}`;
        }
    });
    $('#my-tab-content').hide();
    $('#table_message').hide();
    $('#my-tab-content-RPA').hide();
    $('#table_message_rpa').hide();
    $("[id$=modal_planned_start_date]").datepicker({ minDate: 0 });
    $("[id$=modal_planned_end_date]").datepicker({ minDate: 0 });
    $("[id$=modal_actual_start_date]").datepicker({ minDate: 0 });
    $("[id$=modal_actual_end_date]").datepicker({ minDate: 0 });

    $(function () {
        $("[id$=start_date]").datepicker({ minDate: 0 });
        $("[id$=start_date_mtWO]").datepicker({ minDate: new Date() });
    });

    $('.recurrencewp input[type=radio]').on('change', function () {
        recurrwp = this.value;
        console.log('recurr ' + recurr);
        openModalSlaperiodicitywp(this.value);
    });

    $('.weeklywp input[type=radio]').on('change', function () {
        if ($('.dailywp label ').hasClass('active')) {
            $('.dailywp label ').removeClass('active');
        }
        if (dailywp != null) {
            dailywp = [];
        }
        if ($('.hourlywp label ').hasClass('active')) {
            $('.hourlywp label ').removeClass('active');
        }
        if (hourlywp != null) {
            hourlywp = [];
        }
        weeklywp = this.value;
        console.log(weeklywp);
        console.log(dailywp);
        console.log(hourlywp);
    });

    const C_DAILY_WP_INPUT_CLASS = '.dailywp input[type=checkbox]';
    const C_WEEKLY_LABEL_CLASS = '.weeklywp label ';
    const C_HOURLY_LABEL_CLASS = '.hourlywp label ';

    $(C_DAILY_WP_INPUT_CLASS).on('change', function () {
        if ($(C_WEEKLY_LABEL_CLASS).hasClass('active')) {
            $(C_WEEKLY_LABEL_CLASS).removeClass('active');
        }
        if (weeklywp !== '') {
            weeklywp = '';
        }
        if ($(C_HOURLY_LABEL_CLASS).hasClass('active')) {
            $(C_HOURLY_LABEL_CLASS).removeClass('active');
        }
        if (hourly != null) {
            hourly = [];
        }
        const index = dailywp.indexOf(this.value);
        if (index > -1) {
            dailywp.splice(index, 1);
        }
        else {
            dailywp.push(this.value);
        }
        console.log(dailywp);
        console.log(weeklywp);
    });

    $('.prioritywp input[type=radio]').on('change', function () {
        priority = this.value;
        console.log(priority);
    });
    populateHourly();
});

function checkPlans() {
    const C_PROJID_MISS_ERROR_MSG = "Project ID missing or invalid";
    const C_FAIL_MSG = "Fail to get deliverables";

    if (projectID === undefined || projectID == null || projectID === '' || isNaN(parseInt(projectID))) {
        pwIsf.alert({ msg: C_PROJID_MISS_ERROR_MSG, type: "error" });
    }
    else {
        $.isf.ajax({
            url: `${service_java_URL }/projectManagement/v1/ScopeByProject?deliverableStatus=Active,New&projectId=${parseInt(projectID)}`,
            success: function (data) {
                let hasPlan = false;
                $.each(data, function (i, d) {
                    if (d.hasDeliverablePlan) {
                        $("#createDOPlanButton .createWOPopUp").css({ "pointer-events": "all" }).removeClass("icon-view").addClass("icon-add");
                        hasPlan = true;
                        return false;
                    }
                });
                if (!hasPlan) {
                    $("#createDOPlanButton .createWOPopUp").css({ "pointer-events": "none" }).addClass("icon-view").removeClass("icon-add");
                }
            },
            error: function (xhr, status, sText) {
                alert(C_FAIL_MSG);
                console.log(C_ERROR_MSG);
            }
        });
    }
}

function wo_getServiceAreaDetails() {
    const C_ERROR_MSG = 'An error occurred on getServiceAreaDetails: ';
    $('#wo_select_product_area').html('');
    $('#wo_select_product_area').append('<option value="0">select</option>');

    if (projectID === undefined || projectID == null || projectID === '' || isNaN(parseInt(projectID))) {
        console.log("Project id is wrong");
        pwIsf.alert({ msg: 'Wrong Project Id', type: 'error' });
    }
    else {
        $.isf.ajax({
            url: `${service_java_URL}activityMaster/getServiceAreaDetails?ProjectID=${parseInt(projectID)}`,
            success: function (data) {
                $.each(data, function (i, d) {
                    $('#wo_select_product_area').append(`<option value="${d.serviceAreaID}">${d.serviceArea}</option>`);
                })
            },
            error: function (xhr, status, statText) {
                console.log(`${C_ERROR_MSG}${xhr.error}`);
            }
        });
    }
}

function wo_getDomainSub_change() {
    const C_ERROR_MSG = 'An error occurred on getDomain: ';
    $('#wo_select_domain').html('');
    $('#wo_select_domain').append('<option value="0">select</option>');
    var ServiceAreaID = document.getElementById("wo_select_product_area").value;
    $.isf.ajax({
        url: `${service_java_URL}activityMaster/getDomainDetails?ProjectID=${projectID}&ServiceAreaID=${ServiceAreaID}`,
        success: function (data) {
            $.each(data, function (i, d) {
                $('#wo_select_domain').append(`<option value="${d.domainID}">${d.domain}</option>`);
            })
        },
        error: function (xhr, status, text) {
            console.log(`${C_ERROR_MSG}${xhr.error}`);
        }
    });
}

function wo_getTechSub_change() {

    $('#wo_select_technology').html('');
    $('#wo_select_technology').append('<option value="0">select</option>');
    var domainID = document.getElementById("wo_select_domain").value;
    $.isf.ajax({
        url: `${service_java_URL}activityMaster/getTechnologyDetails?domainID=${domainID}&projectID=${projectID}`,
        success: function (data) {
            $.each(data, function (i, d) {
                $('#wo_select_technology').append(`<option value="${d.technologyID}">${d.technology}</option>`);
            })
        },
        error: function (xhr, status, stext) {
            console.log(C_COMMON_ERROR_MSG);
        }
    });
}

function wo_getActivitiesSub() {
    $('#wo_select_activitysub').html('');
    $('#wo_select_activity').html('');
    $('#wo_select_activitysub').append('<option value="0">select</option>');
    $('#wo_select_activity').append('<option value="0">select</option>');
    var domainID = document.getElementById("wo_select_domain").value;
    var serviceAreaID = document.getElementById("wo_select_product_area").value;
    var technologyID = document.getElementById("wo_select_technology").value;
    $.isf.ajax({
        url: `${service_java_URL}activityMaster/getActivityAndSubActivityDetails/${domainID}/${serviceAreaID}/${technologyID}`,
        success: function (data) {
            $('#wo_select_activitysub').empty();
            $('#wo_select_activity').empty();
            $.each(data, function (i, d) {
                $('#wo_select_activitysub').append(`<option value="${d.subActivityID}">${d.subActivityName}</option>`);
                $('#wo_select_activity').append(`<option value="${d.activity}">${d.activity}</option>`);
            });
        },
        error: function (xhr, status, statusText) {
            console.log(C_COMMON_ERROR_MSG);
        }
    });
}

function getAllSignumForTransferSingle() {
    $("#selectUserAllSignumTransfersingle").autocomplete({
        appendTo: "#modalTransfer",
        source: function (request, response) {
            $.isf.ajax({
                url: `${service_java_URL}activityMaster/getEmployeesByFilter`,
                type: "POST",
                data: {
                    term: request.term
                },
                success: function (data) {
                    $("#selectUserAllSignumTransfersingle").autocomplete().addClass("ui-autocomplete-loading");
                    var result = [];
                    if (data.length === 0) {
                        showErrorMsg('Signum-Required', 'Please enter a valid Signum.');
                        $('#selectUserAllSignumTransfersingle').val("");
                        $('#selectUserAllSignumTransfersingle').focus();
                        response(result);
                    }
                    else {
                        hideErrorMsg('Signum-Required');
                        $("#selectUserAllSignumTransfersingle").autocomplete().addClass("ui-autocomplete-loading");
                        $.each(data, function (i, d) {
                            result.push({
                                "label": `${d.signum}/${d.employeeName}`,
                                "value": d.signum
                            });
                        });
                        response(result);
                        $("#selectUserAllSignumTransfersingle").autocomplete().removeClass("ui-autocomplete-loading");
                    }
                }
            });
        },
        change: function (event, ui) {
            if (ui.item === null) {
                $(this).val('');
                $('#selectUserAllSignumTransfersingle').val('');
            }
            validateTransferButtonDOPlan();
        },
        select: function (event, ui) {
            validateTransferButtonDOPlan();
        },
        minLength: 3
    });
    $("#selectUserAllSignumTransfersingle").autocomplete("widget").addClass("fixedHeight");
}

//Validate submit button for transfer WO
function validateTransferButtonDOPlan() {
    let selectedStep = $('#select_stepNameDOPlan').val();
    let selectedSignum = $('#selectUserAllSignumTransfersingle').val();
    if (selectedStep != -1 && selectedSignum != "") {
        $('#submitbtnModal').attr('disabled', false);
    }
    else {
        $('#submitbtnModal').attr('disabled', true);
    }
}

function getAllSignumForTransferMass() {
    $("#selectUserAllSignumTransferMass").autocomplete({
        appendTo: "#massTransferModalWO",
        source: function (request, response) {
            $.isf.ajax({
                url: `${service_java_URL}activityMaster/getEmployeesByFilter`,
                type: "POST",
                data: {
                    term: request.term
                },
                success: function (data) {
                    $("#selectUserAllSignumTransferMass").autocomplete().addClass("ui-autocomplete-loading");
                    var result = [];
                    $.each(data, function (i, d) {
                        result.push({
                            "label": `${d.signum}/${d.employeeName}`,
                            "value": d.signum
                        });
                    })
                    response(result);
                    $("#selectUserAllSignumTransferMass").autocomplete().removeClass("ui-autocomplete-loading");
                }
            });
        },
        minLength: 3
    });
    $("#selectUserAllSignumTransferMass").autocomplete("widget").addClass("fixedHeight");
}

function wo_getPriority() {
    const C_PROJ_ERROR_MSG = 'An error occurred on getProjectName: ';
    $.isf.ajax({
        url: `${service_java_URL}woManagement/getPriority `,
        success: function (data) {
            $.each(data, function (i, d) {
                $('#select_modal_priority').append(`<option value="${d}">${d}</option>`);
            })
        },
        error: function (xhr, status, statusText) {
            console.log(`${C_PROJ_ERROR_MSG}${xhr.error}`);
        }
    })
}

function wo_getNodeType() {
    const C_PROID_MISS_ERROR = "Project ID missing or invalid";
    const C_PROJID_ERR = 'An error occurred on getProjectName: ';
    if (projectID === undefined || projectID == null || projectID === '' || isNaN(parseInt(projectID))) {
        pwIsf.alert({ msg: C_PROID_MISS_ERROR, type: "error" });
    }
    else {
        $.isf.ajax({
            url: `${service_java_URL}woManagement/getNodeType/${parseInt(projectID)}`,
            success: function (data) {
                $('#select_node_type').append('<option value="' + 0 + '">' + "" + '</option>');
                $.each(data, function (i, d) {
                    $('#select_node_type').append(`<option value="${d}">${d}</option>`);
                })
            },
            error: function (xhr, status, statusText) {
                console.log(`${C_PROJID_ERR}${xhr.error}`);
            }
        });
    }
}

function wo_getNodeNames() {
    const C_PROJNAME_ERR = 'An error occurred on getProjectName: ';
    var type = $('#select_node_type').val();
    $.isf.ajax({
        url: `${service_java_URL}woManagement/getNodeNames/${projectID}/${type}`,
        success: function (data) {
            $('#modal_select_node_name').empty();
            $.each(data, function (i, d) {
                $('#modal_select_node_name').append(`<option value="${d.lstNodeName}">${d.lstNodeName}</option>`);
            })
        },
        error: function (xhr, status, statusText) {
            console.log(`${C_PROJNAME_ERR}${xhr.error}`);
        }
    });
}

function validateNameAndUrlWO() {
    let fileNameLength = $('#inputFileWP li').length;
    let filesArray = [];
    if (fileNameLength == 0) {
        pwIsf.alert({ msg: 'There is no file to add!', type: 'warning' });
        return false;
    }
    for (let i = 0; i < fileNameLength; i++) {
        let fileName = $("#inputFileWP li:eq(" + i + ") #inputNameWP").val();
        let fileUrl = $("#inputFileWP li:eq(" + i + ") #inputUrlWP").val();
        if (fileName == "" || fileUrl == "" || fileName == null || fileUrl == null) {
            pwIsf.alert({ msg: 'Please provide both Name and URL of the file!', type: 'warning' });
            return false;
        }
        else if (!(/^(http|https|ftp):\/\/[a-z0-9]+([\-\.]{1}[a-z0-9]+)*\.[a-z]{2,5}(:[0-9]{1,5})?(\/.*)?$/i.test(fileUrl))) {
            pwIsf.alert({ msg: 'Invalid URL', type: 'warning' });
            return false;
        }
        filesArray.push(fileName);
    }
        let finalFilesArray = filesArray.sort();
        let finalFilesArrayDuplicate = [];
        for (var i = 0; i < finalFilesArray.length - 1; i++) {
            if (finalFilesArray[i + 1] == finalFilesArray[i]) {
                finalFilesArrayDuplicate.push(finalFilesArray[i]);
            }
        }
        if (finalFilesArrayDuplicate.length != 0) {
            let filesString = finalFilesArrayDuplicate.toString();
            let isOrAre = '';
            if (finalFilesArrayDuplicate.length == 1)
                isOrAre = 'is';
            else
                isOrAre = 'are'
            pwIsf.alert({ msg: `(${filesString}) ${isOrAre} already added`, type: 'warning' });
            return false;
        }
    return true;
}

function updateInputFileWO() {
        let inputFileObj = new Object();
        inputFileObj.woid = $('#inputWOIDWO').val();
        let inputFileArrList = [];
        let listLength = $('#inputFileWP li').length;
        for (let i = 0; i < listLength; i++) {
            let id = $('#inputFileWP li:eq(' + i + ') #inputIndexWO').val();
            let name = $('#inputFileWP li:eq(' + i + ') #inputNameWP').val();
            let url = $('#inputFileWP li:eq(' + i + ') #inputUrlWP').val();
            let input = new Object();
            if (id == "" || id == null)
                id = 0;
            input["id"] = id;
            input["inputName"] = name;
            input["inputUrl"] = url;
            inputFileArrList.push(input);
        }
        inputFileObj.file = inputFileArrList;
        inputFileObj.createdBy = signumGlobal;
        inputFileObj.projectID = localStorage.getItem("views_project_id");
        $.isf.ajax({
            url: `${service_java_URL}woManagement/editInputFile`,
            crossdomain: true,
            processData: true,
            contentType: C_CONTENT_TYPE_APPLICATION_JSON,
            type: 'POST',
            data: JSON.stringify(inputFileObj),
            success: function (data) {
                let success = responseHandler(data);
                if (success) {
                    $('#OpenInputFilesModal').modal('hide');
                }
            },
            error: function (xhr, status, statusText) {
                pwIsf.alert({ msg: "Error", type: 'error' });
            }
        });
        $('#inputWOIDWO').val('');
}

function deleteInputFileWO(el) {
    let index = $(el).closest('li').index();
    let id = $("#inputFileWP li:eq(" + index + ") #inputIndexWO").val();
    if (id == null || id == "")
        $("#inputFileWP li:eq(" + index + ")").remove();
    else {
        let btnJson = {
            'Yes': {
                'action': function () {
                    $.isf.ajax({
                        url: `${service_java_URL}woManagement/deleteInputFile/${id}/${signumGlobal}`,
                        crossdomain: true,
                        processData: true,
                        contentType: C_CONTENT_TYPE_APPLICATION_JSON,
                        type: 'POST',
                        success: function (data) {
                            $("#inputFileWP li:eq(" + index + ")").remove();
                            pwIsf.alert({ msg: "Input file deleted.", type: 'success' });
                        },
                        error: function (xhr, status, statusText) {
                            pwIsf.alert({ msg: "Error", type: 'error' });
                        }
                    });
                },
                'class': 'btn btn-success'
            },
            'No': {
                'action': function () {

                },
                'class': 'btn btn-danger'
            },
        };
        pwIsf.confirm({
            title: 'Do you want to delete this file?',
            msg: '',
            'buttons': btnJson
        });
    }
}

function checkFileUrlWO(el) {
    let OK = true;
    let index = $(el).closest('li').index();
    let inputFileUrl = "";
    if (el == undefined)
        inputFileUrl = $("#editInputUrlWO").val();
    else
        inputFileUrl = $("#inputFileWP li:eq(" + index + ") #inputUrlWP").val();
    if (inputFileUrl == "" || inputFileUrl == null) {
        pwIsf.alert({ msg: 'Please provide URL of the file!', type: 'warning' });
        OK = false;
    }
    else if (!(/^(http|https|ftp):\/\/[a-z0-9]+([\-\.]{1}[a-z0-9]+)*\.[a-z]{2,5}(:[0-9]{1,5})?(\/.*)?$/i.test(inputFileUrl))) {
        pwIsf.alert({ msg: 'Invalid URL', type: 'warning' });
        OK = false;
    }
    if (OK) {
        var win = window.open(inputFileUrl, '_blank');
        win.focus();
    }
}

function getInputFilesWO(woidInput) {
    let inputFilesArray = [];
    $('#addInputButton').hide();
    $('#editInputButton').show();    
    $('#inputWOIDWO').val(woidInput);
    let htmlInput = '';
    $('#inputFileWP').html(htmlInput);
    $.isf.ajax({
        url: `${service_java_URL}woManagement/getWOInputFile?woid=${woidInput}`,
        async: false,
        success: function (data) {
            if (data.isValidationFailed == false) {
                if (data.responseData.length != 0) {
                    $('#inputFileHeadersWP').show();
                    $.each(data.responseData, function (i, d) {
                        inputFilesArray.push(d.InputName)
                        htmlInput += '<li><div class="row"><div class="col-lg-11" style="margin-bottom: 0px;"><div class="row"><div class="col-lg-4" style="margin-bottom: 0px;"><input onkeyup="giveTitle(this)" id="inputNameWP" class="inputFileClass" type="text" title=\'' + d.inputName + '\' value=\'' + d.inputName + '\' disabled><input id="inputIndexWO" type="hidden" value=' + d.id + '>'
                            + '</div><div class="col-lg-8" style="margin-bottom: 0px;"><input onkeyup="giveTitle(this)" id="inputUrlWP" class="inputFileClass" type="text" title=\'' + d.inputUrl + '\' value=\'' + d.inputUrl + '\' disabled></div>'
                            + '</div></div><div class="col-lg-1" style="padding-left: 0px;margin-bottom: 0px;">'
                            + '<a title="Check URL" style="color:#367bab;cursor:pointer" onclick="checkFileUrlWO(this)"> <i class="fa fa-lg fa-paper-plane"></i></a> <a id="deleteFilesWO" title="Delete File" onclick="deleteInputFileWO(this)" style="color: #d9534f;cursor:pointer;display:none"><i class="fa fa-lg fa-trash"></i></a>'
                            + '</div></div></li>'
                    });
                }
                else {
                    htmlInput += '<li><a href="#">No Input File</a></li>';
                    $('#inputFileHeadersWP').hide();
                    $('#inputUpdateButton').hide();
                }
            }
            else {
                pwIsf.alert({ msg: data.formErrors[0], type: 'error' });
            }
        }
    });
    $('#inputFileWP').append(htmlInput);
}

function editInputFilesWP() {
    $('#inputUpdateButton').show();
    $('#addInputButton').show();
    $('#editInputButton').hide();
    $('#inputFileHeadersWP').show();
    if ($('#inputFileWP').html() == '<li><a href="#">No Input File</a></li>') {
        let html = '<li><input id="inputIndexWO" type="hidden"><div class="row"><div class="col-lg-11" style="margin-bottom: 0px;"><div class="row"><div class="col-lg-4" style="margin-bottom: 0px;"><input onkeyup="giveTitle(this)" id="inputNameWP" class="inputFileClass" type="text">'
            + '</div><div class="col-lg-8" style="margin-bottom: 0px;"><input onkeyup="giveTitle(this)" id="inputUrlWP" class="inputFileClass" type="text"></div>'
            + '</div></div><div class="col-lg-1" style="padding-left: 0px;margin-bottom: 0px;">'
            + '<a title="Check URL" style="color:#367bab;cursor:pointer" onclick="checkFileUrlWO(this)"> <i class="fa fa-lg fa-paper-plane"></i></a> <a title="Delete File" onclick="deleteFileWP(this)" style="color: #d9534f;cursor:pointer"><i class="fa fa-lg fa-trash"></i></a>'
            + '</div></div></li>';
        $('#inputFileWP').html('');
        $('#inputFileWP').append(html);
        $('#inputUpdateButton').show();
    }
    else {
        let fileNameLength = $('#inputFileWP li').length;
        for (let i = 0; i < fileNameLength; i++) {
            $("#inputFileWP li:eq(" + i + ") #inputNameWP").attr('disabled', false);
            $("#inputFileWP li:eq(" + i + ") #inputUrlWP").attr('disabled', false);
            $("#inputFileWP li:eq(" + i + ") #deleteFilesWO").show();
        }
    }
}

function deleteFileWP(el) {
    let index = $(el).closest('li').index();
    $("#inputFileWP li:eq(" + index + ")").remove();
}

function addMoreInputFilesWP() {
    let html = '<li><input id="inputIndexWO" type="hidden"><div class="row"><div class="col-lg-11" style="margin-bottom: 0px;"><div class="row"><div class="col-lg-4" style="margin-bottom: 0px;"><input onkeyup="giveTitle(this)" id="inputNameWP" class="inputFileClass" type="text">'
        + '</div><div class="col-lg-8" style="margin-bottom: 0px;"><input onkeyup="giveTitle(this)" id="inputUrlWP" class="inputFileClass" type="text"></div>'
        + '</div></div><div class="col-lg-1" style="padding-left: 0px;margin-bottom: 0px;">'
        + '<a title="Check URL" style="color:#367bab;cursor:pointer" onclick="checkFileUrlWO(this)"> <i class="fa fa-lg fa-paper-plane"></i></a> <a title="Delete File" onclick="deleteFileWP(this)" style="color: #d9534f;cursor:pointer"><i class="fa fa-lg fa-trash"></i></a>'
        + '</div></div></li>';
    $('#inputFileWP').append(html);
}

function sortByKey(passData, sortBy) {
    if (sortBy == 'DOID') {
        passData.sort((a, b) => {
            return b.doID - a.doID;
        });
    }
    return passData;
}

function formatData(data, isNotRPA) {
    var nodes = '';
    var projectID = data.projectID;
    var subActivityID = data.subActivityID;
    var table = '<table class="table table-bordered table-hover" style="border: 2px solid #334b75;" id="tableWithNodeDetails">' +
        '<thead>' +
        '   <tr>' +
        '<th style="background-color: #d9edf7;"></th>';
    table +=
    '<th style="background-color: #d9edf7;">Network Element Name/ID</th>' +
    '<th style="background-color: #d9edf7;">Market</th>' +
    '<th style="background-color: #d9edf7;">DOID</th>' +
    '<th style="background-color: #d9edf7;">WOID</th>' +
    '<th style="background-color: #d9edf7;">WO AssignedTo</th>';
    if (isNotRPA)
        table += '<th style="background-color: #d9edf7;">Work Flow Version Name</th>';
    table += '<th style="background-color: #d9edf7;">Priority Last Modified On</th>' +
        '<th style="background-color: #d9edf7;">Created By</th>' +
        '<th style="background-color: #d9edf7;">Priority</th>' +
        '<th style="background-color: #d9edf7;">Planned Start Date</th>' +
        '<th style="background-color: #d9edf7;">Planned End Date</th>' +
        '<th style="background-color: #d9edf7;">WO Name</th>' +
        '<th style="background-color: #d9edf7;">SLA</th>' +
        '<th style="background-color: #d9edf7;">Technology</th>' +
        '<th style="background-color: #d9edf7;">Activity</th>' +
        '<th style="background-color: #d9edf7;">Subactivity</th>' +
        '<th style="background-color: #d9edf7;">StartedOn</th>' +
        '<th style="background-color: #d9edf7;">Status</th>' +
        '</tr>' +
        '</thead>' +
        '<tbody>';
    let workOrderData = sortByKey(data.listOfWorkOrder, 'DOID')
    $.each(workOrderData, function (i, d) {
        table += '<tr>';
        table += '<td>';
        if (d.status == "REOPENED") {
            table += '<div style="display:flex;"><label class="container2 hideHtmlElement"><input type="checkbox" class="checkBoxClassWO"  value="' + d.wOID + '" name="' + d.status + '"><span class="checkmark"></span></label> ' + //change for Merging tabs
                ' <i class="md md-mode-edit" title="Edit" style="display:none;cursor: pointer;" onclick="openModalEdit(' + projectID + ',' + d.wOID + ',' + d.wOPlanID + ',' + d.actualStartDate + ',' + d.actualEndDate + ',' + d.plannedStartDate + ',' + d.plannedEndDate + ',\'' + d.signumID + '\',\'' + d.priority + '\',\'' + d.listOfNode[0].nodeType + '\',\'' + d.listOfNode[0].nodeNames + '\'' + ')"/>' +
                '<a href="javascript:void(0)" class="icon-delete" title="Delete WO" onclick="deleteWO(' + d.wOID + ',' + d.lastModifiedBy + ', \'' + d.status + '\')">' + getIcon('delete') + '</a>' +
                '<a href="#" class="icon-add lsp" title="Transfer" onclick="modalTransfer(' + d.wOID + ',\'' + d.createdBy + '\',\'' + d.signumID + '\'),getAllStepNameForTransferDOPlan(' + d.wOID + ')"><i class="fa fa-exchange"></i> </a>' + '<a href="#" title="Edit WO"  class="icon-edit lsp" onclick="openModalEditDetails(' + d.wOID + ',\'' + d.status + '\',' + d.wOPlanID + ',' + d.doID + ',\'' + d.createdBy + '\')"><i class="fa fa-pencil-square"></i></a>';
        }
        if (d.status == "ONHOLD" || d.status == "INPROGRESS") {
            table += '<div style="display:flex;"> <label class="container2 hideHtmlElement"><input type="checkbox" class="checkBoxClassWO"  value="' + d.wOID + ' " name="' + d.status + '"><span class="checkmark"></span></label> ' + //change for Merging tabs
                '<i class="md md-mode-edit" title="Edit" style="display:none;cursor: pointer;" onclick="openModalEdit(' + projectID + ',' + d.wOID + ',' + d.wOPlanID + ',' + d.actualStartDate + ',' + d.actualEndDate + ',' + d.plannedStartDate + ',' + d.plannedEndDate + ',\'' + d.signumID + '\',\'' + d.priority + '\',\'' + d.listOfNode[0].nodeType + '\',\'' + d.listOfNode[0].nodeNames + '\'' + ')"/>' +
                '<a href="#" class="icon-add lsp" title="Transfer" onclick="modalTransfer(' + d.wOID + ',\'' + d.createdBy + '\',\'' + d.signumID + '\'),getAllStepNameForTransferDOPlan(' + d.wOID + ')"><i class="fa fa-exchange"></i> </a>' + '<a href="#" title="Edit WO"  class="icon-edit lsp" onclick="openModalEditDetails(' + d.wOID + ',\'' + d.status + '\',' + d.wOPlanID + ',' + d.doID + ',\'' + d.createdBy + '\')"><i class="fa fa-pencil-square"></i></a>';
        }
        if (d.status == "DEFERRED" && d.parentWorkOrderID != -1) {
            table += '<div style="display:flex;"><a href="#" class="icon-edit lsp" id="reopenIcon' + d.wOID + '" title="Reopen WO" onclick="reopenDeferredWO(' + d.wOID + ')">' + getIcon('reopen') + '</a>'+
             '<a href="#" class="icon-delete lsp" id="deleteIcon' + d.wOID + '" title="Close WO" onclick="closeDeferredWO(' + d.wOID + ')">' + getIcon('delete') + '</a>'+
                '<a href="#" class="icon-add lsp" id="reinstateIcon' + d.wOID + '" title="Reinstate WO" onclick="reinstateDeferredWO(' + d.wOID + ')">' + getIcon('reinstate') + '</a>' + '<a href="#" title="Edit WO"  class="icon-edit lsp" onclick="openModalEditDetails(' + d.wOID + ',\'' + d.status + '\',' + d.wOPlanID + ',' + d.doID + ',\'' + d.createdBy + '\')"><i class="fa fa-pencil-square"></i></a>';
        }
        if (d.status == "ASSIGNED") {
            table += '<div style="display:flex;"><label class="container2 hideHtmlElement"><input type="checkbox" class="checkBoxClassWO"  value="' + d.wOID + '" name="' + d.status + '"><span class="checkmark"></span></label> ' + //change for Merging tabs
                '<a href="javascript:void(0)" class="icon-delete" title="Delete WO" onclick="deleteWO(' + d.wOID + ',' + d.lastModifiedBy + ', \'' + d.status + '\')">' + getIcon('delete') + '</a>' +
                '<a href="#" class="icon-add lsp" title="Transfer" onclick="modalTransfer(' + d.wOID + ',\'' + d.createdBy + '\',\'' + d.signumID + '\'),getAllStepNameForTransferDOPlan(' + d.wOID + ')"><i class="fa fa-exchange"></i> </a>' + '<a href="#" title="Edit WO"  class="icon-edit lsp" onclick="openModalEditDetails(' + d.wOID + ',\'' + d.status + '\',' + d.wOPlanID + ',' + d.doID + ',\'' + d.createdBy + '\')"><i class="fa fa-pencil-square"></i></a>';
            if (isNotRPA)
                table += '<a href="#" class="icon-edit lsp" title="EDIT WF version" onclick="openModalEditVersion(' + d.wfid + ',' + projectID + ',' + d.wOID + ',' + subActivityID + ',' + '\'wo plan\')">' + getIcon('edit') + '</a>';
        }
        table += '<div class="dropdown" style="margin-top: 5px;"><a title="Input URLs" data-toggle="modal" data-target="#OpenInputFilesModal" href="#" class="icon-view lsp dropdown-toggle" onclick="getInputFilesWO(' + d.wOID + ');getLocalGlobalURLs(\'OpenInputFilesModal\')"><i class="fa fa-info"></i></a></div>';
        table += '</div></td>';
        localStorage.setItem(d.wOPlanID + "_" + d.wOID, JSON.stringify(d.listOfNode));
        var x = d.listOfNode;
        var loe = '';
        for (var i = 0; i < d.listOfNode.length; i++) {
            nodes = x[i].nodeNames + ',' + nodes;
            nodes = nodes.replace(/,\s*$/, "");
        }
        var reason = d.reason;
        if (reason == null || reason == "null")
            reason = "";
        if (d.actualStartDate != null)
            d.actualStartDate = (new Date(d.actualStartDate)).toLocaleDateString();
        if (d.closedOn != null)
            d.closedOn = (new Date(d.closedOn)).toLocaleDateString();
        if (d.actualStartDate == null || d.actualStartDate == "null")
            actualStartDate = "";
        else {
            var actualStartDate = d.actualStartDate;
        }
        if (closedOn == null || closedOn == "null")
            closedOn = "";
        else {
            var closedOn = d.closedOn;
        }
        if (nodes == null || nodes == "null")
            nodes = "";
        if (nodes.length < 40) {
            table += '<td class="nodes" >' + nodes + '<i data-toggle="tooltip" style="cursor:pointer" title="Network Element Details"class="fa fa-list"  onclick="modalNodeWO(' + d.wOPlanID + ',' + d.wOID + ')"></i></td>';
        }
        else
            table += '<td class="nodes">' + nodes.substr(0, 11) + '<a id="showPara' + d.wOID + '" style="cursor:pointer" onclick="showParaWO(' + d.wOID + ')">.....</a><p id="paraShowhide' + d.wOID + '" style="display:none">' + nodes.substr(11, nodes.length) + '</p><i data-toggle="tooltip" id="collapseParagraph' + d.wOID + '"  title="collapse"class="fa fa-chevron-up" style="display:none;cursor:pointer" onclick="collapseParaWO(' + d.wOID + ')"></i>&nbsp;&nbsp;<i data-toggle="tooltip" id="showList' + d.wOID + '"  title="Network Element Details"class="fa fa-list" style="display:none;cursor:pointer" onclick="modalNodeWO(' + d.wOPlanID + ',' + d.wOID + ')"></i></td>';
        var plannedSDate = new Date(d.plannedStartDate);
        var plannedEDate = new Date(d.plannedEndDate);
        var sDate = new Date(data.startDate);
        var eDate = new Date(data.endDate);
        if (d.listOfNode[0].market != null)
            table += '<td>' + d.listOfNode[0].market + '</td>';
        else
            table += '<td></td>';
		table += '<td class="doid">' + d.doID + '</td>';
        table += '<td class="woid">' + d.wOID + '</td>';
        table += '<td class="signum">' + d.signumID + '</td>';
        if (isNotRPA)
            table += '<td> ' + d.wfVersionName + '</td>';
        table += '<td>' + ConvertDateTime_tz(new Date(data.lastModifiedDate)) + '</td>';
        table += '<td>' + d.createdBy + '</td>';
        table += '<td>' + d.priority + '</td>';
        table += '<td>' + ConvertDateTime_tz((plannedSDate)) + '</td>';
        table += '<td>' + ConvertDateTime_tz((plannedEDate)) + '</td>';
        table += '<td>' + d.wOName + '</td>';
        table += '<td>' + data.slaHrs + '</td>';
        table += '<td>' + data.activityDetails[0].technology + '</td>';
        table += '<td>' + data.subActivity[0].activity + '</td>';
        table += '<td>' + data.subActivity[0].subActivity + '</td>';
        table += '<td>' + actualStartDate + '</td>';
        table += '<td class="status">' + d.status + '</td>';
        table += '</tr>';
        nodes = '';
    });
    table += '</tbody>' +
        '</table>';
    return table;
}

function reopenDeferredWO(woID) {
    pwIsf.confirm({
        title: 'Reopen Deferred Work Order', msg: "Are you sure want to Reopen the work order?",
        'buttons': {
            'Yes': {
                'action': function () {
                    pwIsf.addLayer({ text: C_PLEASE_WAIT });
                    $.isf.ajax({
                        url: `${service_java_URL}woManagement/reopenDefferedWorkOrder?workOrderId=${woID}`,
                        context: this,
                        crossdomain: true,
                        processData: true,
                        contentType: C_CONTENT_TYPE_APPLICATION_JSON,
                        type: 'POST',
                        xhrFields: {
                            withCredentials: false
                        },
                        success: function (data) {
                            var convertedJson = JSON.parse(JSON.stringify(data));
                            pwIsf.alert({ msg: convertedJson.msg, type: 'error' });
                            if (convertedJson.msg != 'WO cannot be reopened as it is linked to SRID') {
                                pwIsf.alert({ msg: convertedJson.msg, type: 'success' });
                                $("#reopenIcon" + woID).remove();
                                $("#deleteIcon" + woID).remove();
                                $("#reinstateIcon" + woID).remove();
                            }
                        },
                        error: function (xhr, status, statusText) {
                            pwIsf.alert({ msg: "Error while Closing", type: "error" });
                        },
                        complete: function (xhr, statusText) {
                            pwIsf.removeLayer();
                        }
                    });
                }
            },
            'No': { 'action': function () { } },
        }
    });
}

function closeDeferredWO(woID) {
    pwIsf.confirm({
        title: 'Close Deferred Work Order', msg: "Do you want to close the work order permanently?",
        'buttons': {
            'Yes': {
                'action': function () {
                    pwIsf.addLayer({ text: C_PLEASE_WAIT });
                    $.isf.ajax({
                        url: `${service_java_URL}woManagement/closeDeferedWorkOrder?workOrderId=${woID}`,
                        context: this,
                        crossdomain: true,
                        processData: true,
                        contentType: C_CONTENT_TYPE_APPLICATION_JSON,
                        type: 'POST',
                        xhrFields: {
                            withCredentials: false
                        },
                        success: function (data) {
                            pwIsf.alert({ msg: "WorkOrder ID " + data + " successfully closed", type: "success" });
                            $("#reopenIcon" + woID).remove();
                            $("#deleteIcon" + woID).remove();
                            $("#reinstateIcon" + woID).remove();
                        },
                        error: function (xhr, status, statusText) {
                            pwIsf.alert({ msg: "Error while Reopening", type: "warning" });
                        },
                        complete: function (xhr, statusText) {
                            pwIsf.removeLayer();
                        }
                    });
                }
            },
            'No': { 'action': function () { } },
        }
    });
}

function reinstateDeferredWO(woID) {
    pwIsf.confirm({
        title: 'Reinstate Deferred Work Order', msg: "Do you want to reinstate the deferred work order?",
        'buttons': {
            'Yes': {
                'action': function () {
                    pwIsf.addLayer({ text: C_PLEASE_WAIT });
                    $.isf.ajax({
                        url: `${service_java_URL}woManagement/reinstateDeferedWorkOrder?workOrderId=${woID}`,
                        context: this,
                        crossdomain: true,
                        processData: true,
                        contentType: C_CONTENT_TYPE_APPLICATION_JSON,
                        type: 'POST',
                        xhrFields: {
                            withCredentials: false
                        },
                        success: function (data) {
                            var convertedJson = JSON.parse(JSON.stringify(data))
                            if (convertedJson.isValidationFailed == false) {
                                pwIsf.alert({ msg: convertedJson.formMessages[0], type: 'success' });
                                $("#reopenIcon" + woID).remove();
                                $("#deleteIcon" + woID).remove();
                                $("#reinstateIcon" + woID).remove();
                            }
                            else {
                                pwIsf.alert({ msg: convertedJson.formErrors[0], type: 'error' });
                            }
                        },
                        error: function (xhr, status, statusText) {
                            pwIsf.alert({ msg: "Error while Reopening", type: "warning" });
                        },
                        complete: function (xhr, statusText) {
                            pwIsf.removeLayer();
                        }
                    });
                }
            },
            'No': { 'action': function () { } },
        }
    });
}

function showParaWO(item) {
    $('#paraShowhide' + item).show();
    $('#showPara' + item).hide();
    $('#collapseParagraph' + item).show();
    $('#showList' + item).show();
}

function collapseParaWO(item) {
    $('#paraShowhide' + item).hide();
    $('#showPara' + item).show();
    $('#collapseParagraph' + item).hide();
    $('#showList' + item).hide();
}

function modalNodeWO(wOPlanID, wOID) {
    var listOfNode = JSON.parse(localStorage.getItem(wOPlanID + "_" + wOID));
    $.each(listOfNode, function (i, d) {
        if (d.nodeNames === "" && d.nodeType === "" && d.market === "") {
            listOfNode = [];
        }
    });

    $('#table_wo_nodes').DataTable({
        info: true,
        "data": listOfNode,
        "destroy": true,
        searching: true,
        order: [1],
        "columns": [
            {
                "title": neType,
                "data": "nodeType"
            },
            {
                "title": neName,
                "data": "nodeNames"
            },
            {
                "title": neMarket,
                "data": "market"
            }
        ]
    });
    $('#modal_wo_nodes').modal('show');
}

function modalNodeWOPopup(wOPlanID, wOID) {
    var listOfNode = JSON.parse(localStorage.getItem(wOPlanID + "_" + wOID));
    $.each(listOfNode, function (i, d) {
        if (d.nodeNames === "" && d.nodeType === "" && d.market === "") {
            listOfNode = [];
        }
    });
    $('#table_wo_nodes_detail_edit').DataTable({
        "data": listOfNode,
        "destroy": true,
        searching: true,
        order: [1],
        "columns": [
            {
                "title": "Network Element Type",
                "data": "nodeType"
            },
            {
                "title": "Network Element Name/ID",
                "data": "nodeNames"
            },
            {
                "title": "Market",
                "data": "market"
            }
        ]
    });
}

function getDODetails(doid,woid) {
    $.isf.ajax({
        url: `${service_java_URL}woManagement/getWorkOrdersByDoid?doid=${doid}`,
        success: function (data) {
            oTableDODetail = $('#table_do_details').DataTable({
                "data": data,
                "destroy": true,
                "searching": true,
                order: [1],
                "columns": [
                    {
                        "title": "DOID",
                        "data": null,
                        "render": function (data, type, row) {
                            if (data.woid == woid) {
                                return  '<i class="fa fa-chevron-circle-right" style="color:green"></i>'+data.doid;
                            } else
                                return data.doid;
                        },
                        "searchable": true
                    },
                    {
                        "title": "WOID",
                        "data": "woid",
                        "searchable": true
                    },
                    {
                        "title": shortNeNameAsNEID,
                        "data": null,
                        "render": function (data, type, row, meta) {
                            woViewDataArray = [];
                                let x = data.listOfNode;
                                let nodes = '';
                                for (var i = 0; i < x.length; i++) {
                                    nodes = x[i].nodeNames + ',' + nodes;
                                    nodes = nodes.replace(/,\s*$/, '');
                                }
                                if (nodes == null || nodes == "null")
                                    nodes = "";
                                localStorage.setItem(`${data.woplanid}_${data.wOID}`, JSON.stringify(data.listOfNode));
                                woViewDataArray.push(
                                    {
                                        "NodeNames": nodes
                                    });
                            var nodesView = `<i data-toggle="tooltip" id="showList${data.woid}"  title="Network Element Details" class="fa fa-list"`;
                            nodesView = `${nodesView}style="cursor:pointer" onclick="modalNodeWO(${data.woplanid},${data.woid})"></i>`;
                            return nodesView;
                        }
                    },
                    {
                        "title": "WFID_WFName_WFVErsion",
                        "data": null,
                        "render": function (data, type, row) {
                            return data.wfInfo;
                        },
                        "searchable": true
                    },
                    {
                        "title": "Status",
                        "data": "status",
                        "searchable": true
                    },
                    {
                        "title": "Signum",
                        "data": null,
                        "render": function (data, type, row) {
                            if (data.signum == null) {
                                return '';
                            }
                            else {
                                return data.signum;
                            }
                        },
                         "searchable": true
                    }
                ],
                initComplete: function () {
                    $('#table_do_details tfoot th').each(function (i) {
                        var title = $('#table_do_details thead th').eq($(this).index()).text();
                    });
                    var api = this.api();
                    api.columns().every(function () {
                        var that = this;
                        $('input').on('keyup change', function () {
                            if (that.search() == this.value) {
                                that
                                    .columns($(this).parent().index() + ':visible')
                                    .search(this.value)
                                    .draw();
                            }
                        });
                    });
                }
            });
        },
        error: function (xhr, status, statusText) {
            console.log('An error occurred on getDomain: ' + xhr.error);
        }
    });
}

function getWorkOrdersWOPlan() {
    statusText = "Assigned";
    statusTextWO = "Assigned";
    woStatus = "Assigned";
    woStatusWO = "Assigned";
    getWorkOrderDetails(startDate, endDate, statusText);
    createWOView(startDate, endDate, statusText);
    getAllSignumForTransferSingle();
    wo_getPriority();
    wo_getNodeType();
    $('#createWO .icon-add').show();
}

var detailsPlan = [];
var detailsRPA = [];

function getWorkOrderDetailsRPA() {
    var subactivityID = 0;
    $('#table_wo_planning_RPA').empty();
    $('#table_wo_planning_RPA').html('');
    var urlSearch = +projectID + "/" + subactivityID;
    pwIsf.addLayer({ text: C_PLEASE_WAIT });
    $.isf.ajax({
        url: `${service_java_URL}rpaController/getRPAWorkOrderDetails/${urlSearch}`,
        success: function (data) {
            pwIsf.removeLayer();
            $("#loaderDivRPA").hide();
            $('#table_wo_planning_RPA').html('');
            $('#my-tab-content-RPA').show();
            $('#table_message_rpa').hide();
            $.each(data, function (i, d) {
                d.startDate = (new Date(d.startDate)).toLocaleDateString();
                d.endDate = (new Date(d.endDate)).toLocaleDateString();
            })
            $("#table_wo_planning_RPA").append($('<tfoot><tr><th></th><th>wOPlanID</th><th>wOName</th><th>Priority</th><th>Start Date</th><th>End Date</th><th>Activity</th><th>Subactivity</th></tr></tfoot>'));
            oTableWOPlanRPA = $('#table_wo_planning_RPA').DataTable({
                "data": data,
                "destroy": true,
                searching: true,
                "pageLength": 10,
                colReorder: true,
                order: [1],
                dom: 'Bfrtip',
                buttons: [
                    'colvis', 'excelHtml5'
                ],
                "columns": [
                    {
                        "title": "Action",
                        "className": 'details-control-rpa',
                        "orderable": false,
                        "data": null,
                        "defaultContent": "<i style='cursor: pointer;display:none;' title='Edit' class='md md-edit'></i><i style='cursor: pointer;display:none;margin-left: 56px;margin-top: 0px;' title='Delete' class='md md-delete first_delete'  '></i> ",
                        "render": function (data, type, row, meta) {
                            detailsRPA[meta.row] = formatData(data, false);
                        }
                    },
                    {
                        "title": "WO Adhoc ID",
                        "data": "adhocWOID"
                    },
                    {
                        "title": "WO Name",
                        "data": "woName"
                    },
                    {
                        "title": "Priority",
                        "data": "priority"
                    },
                    {
                        "title": "Start Date",
                        "data": "startDate",
                    },
                    {
                        "title": "End Date",
                        "data": "endDate"
                    },
                    {
                        "title": "Activity",
                        "data": "subActivity[0].activity",
                    },
                    {
                        "title": "Subactivity",
                        "data": "subActivity[0].subActivityName",
                    }
                ],
                initComplete: function () {
                    $('#table_wo_planning_RPA tfoot th').each(function (i) {
                        var title = $('#table_wo_planning_RPA thead th').eq($(this).index()).text();
                        if (title != "Action")
                            $(this).html('<input type="text" class="form-control" style="font-size:12px;"  placeholder="Search ' + title + '" data-index="' + i + '" />');
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
            $('#table_wo_planning_RPA tfoot').insertAfter($('#table_wo_planning_RPA thead'));

            $('#table_wo_planning_RPA tbody').on('click', 'td.details-control-rpa', function () {
                var tr = $(this).closest('tr');
                var row = oTableWOPlanRPA.row(tr);
                if (row.child.isShown()) {
                    row.child.hide();
                    tr.removeClass('shown');
                }
                else {
                    row.child(detailsRPA[row.index()]).show()
                    tr.addClass('shown');
                }
            });

            $('#table_wo_planning_RPA tbody').on('click', 'i.md-edit', function () {
                var data = oTableWOPlanRPA.row($(this).parents('tr')).data();

                if (validateDeleteEditWorkOrderPlan(data.listOfWorkOrder)) {

                    getWorkOrderPlanDetails(data.wOPlanID, data.projectID, data.signumID);

                    localStorage.setItem("projectID", data.projectID);
                    localStorage.setItem("woPlanID", data.wOPlanID);
                    localStorage.setItem("signumID", data.signumID);
                    localStorage.setItem("lastModifiedBy", data.lastModifiedBy);
                    localStorage.setItem("createdBy", data.createdBy);
                    listOfNode = [{
                        "nodeType": data.listOfNode.nodeType,
                        "nodeNames": data.listOfNode.nodeNames
                    }]
                    localStorage.setItem("listOfNode", data.listOfNode);
                    $('.nav a[href="#menu5"]').tab('show')
                } else {
                    $('#edit_success').modal('show');
                    $('#employee_change_status').html('The work order plan cannot be edited. There is a work order with status different from ASSIGNED.');
                }
            });

            // Apply the search
            oTableWOPlanRPA.columns().every(function () {
                $('input', this.footer()).on('keyup change', function () {
                    filterByDetailsWoidAndSignumRPA(this.value);
                });
            });

            $('#table_wo_planning_RPA_filter input')
                .off()
                .on('keyup', function () {
                    filterByDetailsWoidAndSignumRPA(this.value);
                });
            $('#my-tab-content-rpa').show();
        },
        error: function (xhr, status, statusText) {
            pwIsf.removeLayer();
            $('#my-tab-content-rpa').hide();
            $('#table_message_rpa').show();
            console.log('An error occurred on getDomain: ' + xhr.error);
        }
    });
}

function filterByDetailsWoidAndSignum(term) {
    $.fn.dataTable.ext.search.push(
        function (settings, data, dataIndex) {
            if ($(detailsPlan[dataIndex]).find('.woid').text().indexOf(term) != -1) return true;
            if ($(detailsPlan[dataIndex]).find('.signum').text().toLowerCase().indexOf(term.toLowerCase()) != -1) return true;
            if ($(detailsPlan[dataIndex]).find('.nodes').text().toLowerCase().indexOf(term.toLowerCase()) != -1) return true;
            if ($(detailsPlan[dataIndex]).find('.status').text().toLowerCase().indexOf(term.toLowerCase()) != -1) return true;
            for (var i = 0; i < data.length; i++) {
                if (data[i].toLowerCase().indexOf(term.toLowerCase()) >= 0) {
                    return true
                }
            }
            return false;
        }
    )
    $.fn.dataTable.ext.search.pop();
}

function filterByDetailsWoidAndSignumRPA(term) {
    $.fn.dataTable.ext.search.push(
        function (settings, data, dataIndex) {
            if ($(detailsRPA[dataIndex]).find('.woid').text() == term) return true;
            if ($(detailsRPA[dataIndex]).find('.signum').text().toLowerCase() == term.toLowerCase()) return true;
            if ($(detailsRPA[dataIndex]).find('.nodes').text().toLowerCase().indexOf(term.toLowerCase()) != -1) return true;
            if ($(detailsRPA[dataIndex]).find('.status').text().toLowerCase().indexOf(term.toLowerCase()) != -1) return true;
            for (var i = 0; i < data.length; i++) {
                if (data[i].toLowerCase().indexOf(term.toLowerCase()) >= 0) {
                    return true
                }
            }
            return false;
        }
    )
    oTableWOPlanRPA.draw();
    $.fn.dataTable.ext.search.pop();
}

function refreshTable() {
    $('#table_wo_planning').empty();
    getWorkOrderDetails();
    $('#table_wo_planning_RPA').empty();
}

function getWorkOrderPlanDetails(wOPlanID, projectID, signumID) {
    gotoWorkOrder();
    $.isf.ajax({
        url: `${service_java_URL}woManagement/getWorkOrderPlanDetails/${wOPlanID}/${projectID}/${signumID}`,
        success: function (data) {
            var startDate = (new Date(data[0].startDate));
            var endDate = (new Date(data[0].endDate));
            $('#Start_Date').val(startDate.getFullYear() + "-" + startDate.getMonth() + "-" + (startDate.getDay().toString().length == 1 ? "0" + startDate.getDay() : startDate.getDay()));
            $('#start_time').val(data[0].startTime.substring(0, 2) + ":00");
            $('#start_time').trigger('change');
            $('#End_Date').val(endDate.getFullYear() + "-" + endDate.getMonth() + "-" + (endDate.getDay().toString().length == 1 ? "0" + endDate.getDay() : endDate.getDay()));
            $('#select_project_name').val(data[0].projectID);
            $('#select_project_name').trigger('change');
            $('#woc_select_domain').val(data[0].activityDetails[0].domainID);
            $('#woc_select_domain').trigger('change');
            $('#select_product_area').val(data[0].activityDetails[0].serviceAreaID);
            $('#select_product_area').trigger('change');
            $('#select_assign_to_user').val(data[0].signumID);
            $('#select_assign_to_user').trigger('change');
            $('#select_nodeType').val(data[0].listOfNode[0].nodeType);
            $('#select_nodeType').trigger('change');
            $('#select_nodeName').val(data[0].listOfNode[0].nodeNames);
            $('#select_nodeName').trigger('change');
           $('#select_priority').val(data[0].priority);
            $('#select_technology').val(data[0].activityDetails[0].technologyID);
            $('#select_technology').trigger('change');
            $('#select_activitysub').val(data[0].subActivity[0].subActivityID);
            $('#select_activitysub').trigger('change');
        },
        error: function (xhr, status, statusText) {
            console.log('An error occurred on getServiceAreaDetails: ' + xhr.error);
        }
    });
}

function validateDeleteEditWorkOrderPlan(listOfNode) {
    var result = true;
    $.each(listOfNode, function (i, d) {
        if (d.status != "ASSIGNED") {
            result = false;
        }
    });
    return result;
}

function openModalEditVersion(wfid, projectID, woID, subActivityID, woOrWOPlan) {
    $("#woID").val(woID);
    $("#woOrWOPlan").val(woOrWOPlan);
    $("#versionDropDown").html("");
    $("#versionDropDown").append('<option value="-1">Please Select</option');

    $.isf.ajax({
        type: "GET",
        url: service_java_URL + "flowchartController/getWorkFlowVersionData/" + projectID + "/" + subActivityID + "/" + wfid,
        success: function (data) {
            $.each(data, function (j, dat) {
                $("#versionDropDown").append('<option data-id="' + dat.subActivityFlowChartDefID+'" value="' + dat.versionNumber + '">' + dat.workFlowName + '</option>');
                $("#subActDefID").val(dat.subActivityFlowChartDefID);
            });
            $("#versionDropDown option:last").attr("selected", "selected");
        }
    });

    $("#modal-edit-version").modal("show");
}

function openModalEdit(projectID, wOID, wOPlanID, actualStartDate, actualEndDate, plannedStartDate, plannedEndDate, signumID, priority, nodeType, nodeNames) {
    getNodeType(projectID);
    $('#select_node_type').val(nodeType);
    $('#select_node_type').trigger('change');
    $('#modal_input_wOID').val(wOID);
    $('#modal_input_wOPlanID').val(wOPlanID);
    $('#modal_planned_start_date').val((new Date(plannedStartDate)).toLocaleDateString());
    $('#modal_planned_end_date').val((new Date(plannedEndDate)).toLocaleDateString());
    $('#modal_actual_start_date').val(actualStartDate);
    $('#modal_actual_end_date').val(actualEndDate);
    $('#select_modal_signum2').val(signumID);
    $('#select_modal_signum2').trigger('change');
    $('#select_modal_priority').val(priority);
    $('#select_modal_priority').trigger('change');
    $('#modal_select_node_name').val(nodeNames);
    $('#modal_select_node_name').trigger('change');
    $('#modal_work_order').modal("show");
}

function getNodeType(projectID) {
    if (projectID == undefined || projectID == null || projectID == '' || isNaN(parseInt(projectID))) {
        pwIsf.alert({ msg: "Project ID missing or invalid", type: "error" });
    }
    else {
        $.isf.ajax({
            url: `${service_java_URL}woManagement/getNodeType/${parseInt(projectID)}`,
            success: function (data) {
                $.each(data, function (i, d) {
                    $('#select_nodeType').append(`<option value="${d}">${d}</option>`);
                })
            },
            error: function (xhr, status, statusText) {
                console.log('An error occurred on getDomain: ' + xhr.error);
            }
        });
    }
}

function deleteWorkOrderPlan(wOPlanID, lastModifiedBy) {
    var wo = new Object();
    wo.projectID = localStorage.getItem("views_project_id");
    wo.woPlanID = wOPlanID;
    wo.lastModifiedBy = signumGlobal;
    wo.loggedInUser = signumGlobal;
    wo.type = "WOPLAN";
    pwIsf.addLayer({ text: C_PLEASE_WAIT });
    $.isf.ajax({
        url: `${service_java_URL}projectManagement/deleteProjectComponents`,
        context: this,
        crossdomain: true,
        processData: true,
        contentType: C_CONTENT_TYPE_APPLICATION_JSON,
        type: 'POST',
        data: JSON.stringify(wo),
        xhrFields: {
            withCredentials: false
        },
        success: function (data) {
            if (data.isDeleted) {
                wo.type = 'DELETE_WOPLAN';
                var msgVar = '';
                if (data.count_woid < 1) {
                    msgVar = 'Do you want to delete this work plan (' + wo.woPlanID + ')? ';
                } else {
                    msgVar = 'This work plan(' + wo.woPlanID + ') is mapped with work order (Count: ' + data.count_woid + ').<br>Do you want to delete ?';
                }
                pwIsf.confirm({
                    title: 'Delete work plan', msg: msgVar,
                    'buttons': {
                        'Yes': {
                            'action': function () {
                                pwIsf.addLayer({ text: C_PLEASE_WAIT });
                                $.isf.ajax({
                                    url: `${service_java_URL}projectManagement/deleteProjectComponents`,
                                    context: this,
                                    crossdomain: true,
                                    processData: true,
                                    contentType: C_CONTENT_TYPE_APPLICATION_JSON,
                                    type: 'POST',
                                    data: JSON.stringify(wo),
                                    xhrFields: {
                                        withCredentials: false
                                    },
                                    success: function (data) {
                                        if (data.isDeleted) {
                                            pwIsf.alert({ msg: "Successfully Deleted.", autoClose: 3 });
                                            getWorkOrderDetails();
                                        } else {
                                            pwIsf.alert({ msg: data.msg, type: 'warning' });
                                        }
                                    },
                                    error: function (xhr, status, statusText) {

                                    },
                                    complete: function (xhr, statusText) {
                                        pwIsf.removeLayer();
                                    }
                                });
                            }
                        },
                        'No': { 'action': function () { } },
                    }
                });
            } else {
                pwIsf.alert({ msg: data.msg, type: 'warning' });
            }
        },
        error: function (xhr, status, statusText) {

        },
        complete: function (xhr, statusText) {
            pwIsf.removeLayer();
        }
    });
}

// delete WO
function deleteWO(wOID, LastModifiedBy, Status, str = null) {
    const C_WO_DELETE_TITLE = 'Delete WO (Work order)';
    const C_WO_DELETE_MSG = 'Are you sure to delete this WO ?';
    const C_WO_DELETE_SUCCESS_MSG = 'Successfully deleted';
    const C_WO_DELETE_FAIL_MSG = 'Your work order cannot be deleted.';
    const C_WO_VIEW = 'WO VIEW';
    const C_WO_DELETE_MODAL_ID = '#edit_success';
    const C_EMP_STATUS_HTML_ID = '#employee_change_status';
    pwIsf.confirm({
        title: C_WO_DELETE_TITLE, msg: C_WO_DELETE_MSG,
        'buttons': {
            'Yes': {
                'action': function () {
                    pwIsf.addLayer({ text: C_PLEASE_WAIT });
                    $.isf.ajax({
                        url: `${service_java_URL}woManagement/deleteWorkOrder/${wOID}/${signumGlobal}`,
                        context: this,
                        crossdomain: true,
                        processData: true,
                        contentType: C_CONTENT_TYPE_APPLICATION_JSON,
                        type: 'POST',
                        xhrFields: {
                            withCredentials: false
                        },
                        success: function (data) {
                            if (str !== null) {
                                if (str.toUpperCase() === C_WO_VIEW) {
                                    refreshTableWOView();
                                }
                            }
                            else {
                                refreshTable();
                            }
                            pwIsf.alert({ msg: C_WO_DELETE_SUCCESS_MSG, autoClose: 4 });
                        },
                        error: function (xhr, status, sText) {
                            $(C_WO_DELETE_MODAL_ID).modal('show');
                            $(C_EMP_STATUS_HTML_ID).html(C_WO_DELETE_FAIL_MSG);
                        },
                        complete: function (xhr, statText) {
                            pwIsf.removeLayer();
                        }
                    });
                }
            },
            'No': {},
        }
    });
}

function isWoAnyStepRunning(tmpWoId, signumID) {
    var stepRunningStatus = false;
    arrAllRunningWoIds = {};
    $.isf.ajax({
        url: `${service_java_URL}woManagement/getInprogressTask/${signumID}`,
        async: false,
        success: function (data) {
            for (var i in data.responseData) {
                var woID = (data.responseData[i].woID) ? data.responseData[i].woID : replaceNullBy;
                arrAllRunningWoIds[woID] = 1;
            }

            if (tmpWoId in arrAllRunningWoIds) {
                stepRunningStatus = true;
            }
        },
        error: function (xhr, status, statusText) {
            console.log(C_COMMON_ERROR_MSG);
        }
    });
    return stepRunningStatus;
}

function getAllStepNameForTransferDOPlan(woID) {
    $('#tWOcommentsBoxDOPlan').val('');
    $.isf.ajax({
        async: false,
        type: "GET",
        url: `${service_java_URL}woExecution/getAllStepsByWOID?woID=${woID}`,
        dataType: "json",
        crossdomain: true,
        success: function (data) {
            if (data.isValidationFailed == false) {
                var workFlowSteps = data.responseData;
                $("#select_stepNameDOPlan").empty();
                $("#select_stepNameDOPlan").append("<option value=-1>Select Step Name</option>");
                $('#select_stepNameDOPlan').index = -1;
                $.each(workFlowSteps, function (i, d) {
                    $('#select_stepNameDOPlan').append('<option value="' + d["stepName"] + '">' + d["stepName"] + '</option>');
                })
            }
        },
        complete: function () {
            pwIsf.removeLayer();
        }
    });

    $('#select_stepNameDOPlan').select2({
        closeOnSelect: true
    });

    $('#select_stepNameDOPlan').on('change', function () {
        validateTransferButtonDOPlan();
    })
}

function modalTransfer(wOPlanID, createdBy, signumID, str) {
    if (localStorage.getItem("projectType") == "ASP") {
        $("#selectallSignumTransfersinglediv").remove();
        getAspByWoId(wOPlanID);
    }
    else {
        $("#selectAspUserSignumTransferDiv").remove();
    }
    $("#viewValueSIngleTransfer").val("");
    $('#assingedTo_modal').val(signumID);
    if (isWoAnyStepRunning(wOPlanID, signumID)) {
        pwIsf.alert({ msg: "Work Order cannot be transferred while in progress", type: 'error' });
    }
    else {
        $('#modalTransfer').modal('show');
        $("#viewValueSIngleTransfer").val(str);
        $('#modal_input_woid').val(wOPlanID);
        $('#select_modal_signum').val(0);
        $('#select_modal_signum').trigger('change');
        $('#modal_assignedTo').val(signumID);
        $('#selectUserAllSignumTransfersingle').val('');
        modalCreatedBy = createdBy;
        $('#submitbtnModal').attr('disabled', true);
    }
}

function updateWorkFlowVersion(version, woID, subActivityDefID) {
    if (version == -1) {
        alert("Please select version number");
    }
    else {
        $.isf.ajax({
            type: "POST",
            url: `${service_java_URL}woManagement/updateWOWFVersion/${woID}/${version}/${subActivityDefID}`,
            success: function (data) {
                console.log("success");
                if ($("#woOrWOPlan").val() == "wo plan")
                    getWorkOrderDetails();
                else
                    createWOView();
            }
        });
    }
}

function updateWorkOrder() {
    var wo = new Object();
    wo.wOID = $("#modal_input_wOID").val();
    wo.signumID = $("#select_modal_signum2").val();
    wo.lastModifiedBy = signumGlobal;
    wo.priority = $('#select_modal_priority').val();
    wo.listOfNode = [{
        "nodeType": $('#select_node_type').val(),
        "nodeNames": $('#modal_select_node_name').val(),
    }];

    $.isf.ajax({
        url: `${service_java_URL}woManagement/updateWorkOrder`,
        context: this,
        crossdomain: true,
        processData: true,
        contentType: C_CONTENT_TYPE_APPLICATION_JSON,
        type: 'POST',
        data: JSON.stringify(wo),
        xhrFields: {
            withCredentials: false
        },
        success: function (data) {
            if (data == "") {
                $('#modal_work_order').modal("hide");
                $('#scopeModal2').modal('show');
                $('#modal_text').text("The work oder was updated successfully ");
            } else {
                $('#modal_work_order').modal("hide");
                $('#scopeModal2').modal('show');
                $('#modal_text').text("The work oder wasn't updated successfully ");
            }
            getWorkOrderDetails();
        },
        error: function (xhr, status, statusText) {
            $('#modal_work_order').modal("hide");
            $('#scopeModal2').modal('show');
            $('#modal_text').text("The work oder wasn't updated successfully ");
            console.log('An error occurred on : createProject' + xhr.error);
        }
    });
}

function transferWorkOrder(wOID, signumID, createdBy, stepName, userComments) {
    var assignedTo = $('#assingedTo_modal').val();
    var wo = new Object();
    wo.woID = [$('#modal_input_woid').val()];
    if (localStorage.getItem("projectType") == 'ASP') {
        var temp = $('#selectAspUserSignumTransfer').val();
        wo.receiverID = temp[0];
    }
    else {
        wo.receiverID = $('#selectUserAllSignumTransfersingle').val();
    }
    wo.senderID = assignedTo;
    wo.logedInSignum = signumGlobal;
    wo.stepName = $('#select_stepNameDOPlan').val();
    wo.userComments = $('#tWOcommentsBoxDOPlan').val();

    if (wo.receiverID.length == 0 || wo.receiverID == "0" || wo.receiverID.length != 7) {
        $("#modalTransfer .btn-primary").attr('disabled', true);
        pwIsf.alert({ msg: 'Please select the employee Signum', type: 'warning' });
    }
    else if (wo.stepName == null || wo.stepName == '' || wo.stepName == '-1') {
        $("#modalTransfer .btn-primary").attr('disabled', true);
        pwIsf.alert({ msg: 'Please select the Step Name', type: 'warning' });
    } else if (wo.userComments.length != '' && wo.userComments.length > 250) {
        pwIsf.alert({ msg: 'Please Comment within 250 characters', type: 'warning' });
        return true;
    }
    else if (assignedTo.toLowerCase().trim() == wo.receiverID.toLowerCase().trim()) {
        pwIsf.alert({ msg: "Work Order(s) already assigned to you,Please select someone else's signum for transfer.", type: 'warning' });
    }
    else {
        if (wo.senderID == " " || wo.senderID == "null" || wo.senderID == null || wo.senderID == "" || wo.senderID == "undefined" || wo.senderID == undefined) {
			wo.senderID = signumGlobal;
		}
        transferWO(wo);
    }
}

// WO Transfer
function transferWO(wo) {
    const C_VIEW_SINGLE_TRANSFER_ID = "#viewValueSIngleTransfer";
    const C_WO_VIEW = "WO VIEW";
    const C_WO_TRANSFER_MODAL_ID = '#modalTransfer';
    const C_WO_TRANSFER_SUCCESS_ID = '#edit_success';
    const C_EMP_CHANGE_STATUS_ID = '#employee_change_status';
    const C_WO_TRANSFER_ERROR_MSG = 'There was an error, your work order couldn\'t be transferred.';

    $.isf.ajax({
        url: `${service_java_URL}woManagement/transferWorkOrder`,
        context: this,
        crossdomain: true,
        processData: true,
        contentType: C_CONTENT_TYPE_APPLICATION_JSON,
        type: 'POST',
        data: JSON.stringify(wo),
        xhrFields: {
            withCredentials: false
        },
        success: function (data) {
            var convertedJson = JSON.parse(JSON.stringify(data));
            if (!convertedJson.isValidationFailed) {
                if ($(C_VIEW_SINGLE_TRANSFER_ID).val().toUpperCase() === C_WO_VIEW) {
                    createWOView();
                }
                else {
                    getWorkOrderDetails();
                }
                $(C_WO_TRANSFER_MODAL_ID).modal('hide');
                pwIsf.alert({ msg: convertedJson.formMessages[0], type: 'success' });
            }
            else {
                pwIsf.alert({ msg: convertedJson.formErrors[0], type: 'error' });
            }
        },
        error: function (xhr, status, stext) {
            var err = JSON.parse(xhr.responseText);
            pwIsf.alert({ msg: err.errorMessage, type: 'error' });
            $(C_WO_TRANSFER_MODAL_ID).modal('hide');
            $(C_WO_TRANSFER_SUCCESS_ID).modal('show');
            $(C_EMP_CHANGE_STATUS_ID).html(C_WO_TRANSFER_ERROR_MSG);
        }
    });
}

function modalClose() {
    $('#modal_wo_creation1').modal('hide');
}

function gotoWorkOrder() {
    getServiceAreaDetails();
    getProjectName();
    getUser();
    getPriority();
    getNodeType();
    $('#modal_wo_creation1').modal('show');
}

function getServiceAreaDetails() {
    $('#select_product_area')
        .find('option')
        .remove();
    $("#select_product_area").select2("val", "");
    $('#select_product_area').append('<option value="0"></option>');
    if (woProjectID=== undefined || woProjectID== null || woProjectID=== '' || isNaN(parseInt(wo_projectId))) {
        console.log("Project id is wrong");
        pwIsf.alert({ msg: 'Wrong Project Id', type: 'error' });
    }
    else {
        $.isf.ajax({
            url: `${service_java_URL}activityMaster/getServiceAreaDetails?ProjectID=${parseInt(wo_projectId)}`,
            success: function (data) {
                $.each(data, function (i, d) {
                    $('#select_product_area').append('<option value="' + d.serviceAreaID + '">' + (d.serviceArea).split("/")[1] + '</option>');
                })
            },
            error: function (xhr, status, statusText) {
                console.log('An error occurred on getServiceAreaDetails: ' + xhr.error);
            }
        });
    }
}

function getDomain() {
    $('#woc_select_domain')
        .find('option')
        .remove();
    $("#woc_select_domain").select2("val", "");
    $('#woc_select_domain').append('<option value="0"></option>');
    var ServiceAreaID = $("#select_product_area").val();
    $.isf.ajax({
        url: `${service_java_URL}activityMaster/getDomainDetails?ProjectID=${woProjectID}&ServiceAreaID=${ServiceAreaID}`,
        success: function (data) {
            $.each(data, function (i, d) {
                $('#woc_select_domain').append('<option value="' + d.domainID + '">' + d.domain + '</option>');
            })
        },
        error: function (xhr, status, statusText) {
            console.log('An error occurred on getDomain: ' + xhr.error);
        }
    });
}

function getTechnologies() {
    $('#woc_select_technology')
        .find('option')
        .remove();
    $("#woc_select_technology").select2("val", "");
    $('#woc_select_technology').append('<option value="0"></option>');
    var domainID = $("#woc_select_domain").val();
    $.isf.ajax({
        url: service_java_URL + "activityMaster/getTechnologyDetails?domainID=" + domainID + "&projectID=" + wo_projectId,
        success: function (data) {
            $.each(data, function (i, d) {
                $('#woc_select_technology').append('<option value="' + d.technologyID + '">' + d.technology + '</option>');
            })
        },
        error: function (xhr, status, statusText) {
            console.log(C_COMMON_ERROR_MSG);
        }
    });
}

function getActivitiesSub() {
    $('#woc_select_activitysub')
        .find('option')
        .remove();
    $("#woc_select_activitysub").select2("val", "");
    $('#woc_select_activitysub').append('<option value="0"></option>');
    var domainID = document.getElementById("woc_select_domain").value;
    var serviceAreaID = document.getElementById("select_product_area").value;
    var technologyID = document.getElementById("woc_select_technology").value;
    $.isf.ajax({
        url: service_java_URL + "activityMaster/getActivitySubActivityByProject_V2/" + domainID + "/" + serviceAreaID + "/" + technologyID + '/' + projectID + '/1',
        success: function (data) {
            $.each(data, function (i, d) {
                $('#woc_select_activitysub').append('<option value="' + d.SubActivityID + '/' + d.projectscopeid + '">' + d.activitySubActivity + '</option>');
            })
        },
        error: function (xhr, status, statusText) {
            console.log(C_COMMON_ERROR_MSG);
        }
    });
}

function getProjectName() {
    $('#select_project_name')
        .find('option')
        .remove();
    $("#select_project_name").select2("val", "");
    $('#select_project_name').append('<option value="0"></option>');
    $.isf.ajax({
        url: service_java_URL + "projectManagement/getProjectDetails?ProjectID=" + wo_projectId,
        success: function (data) {
            $('#select_project_name').append('<option value="' + data.projectID + '">' + data.projectName + '</option>');
        },
        error: function (xhr, status, statusText) {
            console.log('An error occurred on getProjectName: ' + xhr.error);
        }
    });
}

function getPriority() {
    $('#select_priority')
        .find('option')
        .remove();
    $("#select_priority").select2("val", "");
    $('#select_priority').append('<option value="0"></option>');
    $.isf.ajax({
        url: service_java_URL + "woManagement/getPriority ",
        success: function (data) {
            $.each(data, function (i, d) {
                $('#select_priority').append('<option value="' + d + '">' + d + '</option>');
            })
        },
        error: function (xhr, status, statusText) {
            console.log('An error occurred on getProjectName: ' + xhr.error);
        }
    });
}

function openModalperiodicity(value) {
    if (value == "Daily" || value == "Weekly") {
        document.getElementById("woModal1ContentDaily").style.display = "none";
        document.getElementById("woModal1ContentWeekly").style.display = "none";
        if (value == "Daily") {
            document.getElementById("woModal1ContentDaily").style.display = "inline";
        } else {
            document.getElementById("woModal1ContentWeekly").style.display = "inline";
        }
        $("#woModal1Title").html(value);
        $("#woModal1").modal("show");
    }
}

function getNodeType() {
    $('#select_nodeType')
        .find('option')
        .remove();
    $("#select_nodeType").select2("val", "");
    $('#select_nodeType').append('<option value="0"></option>');
    if (woProjectID=== undefined || woProjectID== null || woProjectID=== '' || isNaN(parseInt(wo_projectId))) {
        pwIsf.alert({ msg: "Project ID missing or invalid", type: "error" });
    }
    else {
        $.isf.ajax({
            url: service_java_URL + "woManagement/getNodeType/" + parseInt(wo_projectId),
            success: function (data) {
                $('#select_nodeType').empty();
                $.each(data, function (i, d) {
                    $('#select_nodeType').append('<option value="' + d + '">' + d + '</option>');
                })
            },
            error: function (xhr, status, statusText) {
                console.log('An error occurred on getDomain: ' + xhr.error);
            }
        });
    }
}

function getNodeNames() {
    $('#woc_select_nodeName')
        .find('option')
        .remove();
    $("#woc_select_nodeName").select2("val", "");
    $('#woc_select_nodeName').append('<option value="0"></option>');
    var nodeType = "";
    nodeType = $('#select_nodeType').val();
    pwIsf.addLayer({ text: C_PLEASE_WAIT });
    $.isf.ajax({
        url: `${service_java_URL}woManagement/getNodeNames/${woProjectID}/${nodeType}`,
        success: function (data) {
            pwIsf.removeLayer();
            $('#woc_select_nodeName').empty();
            if (data != null && data.length > 0) {
                for (i = 0; i < data[0].lstNodeName.length; i++) {
                    $('#woc_select_nodeName').append(`<option value="${data[0].lstNodeName[i]}">${data[0].lstNodeName[i]}</option>`);
                }
            }
        },
        error: function (xhr, status, statusText) {
            pwIsf.removeLayer();
            console.log('An error occurred on getProjectName: ' + xhr.error);
        }
    });
}

function createWorkOrder() {
    if (validations()) {
        var periodicity = $("#select_periodicity").val();
        var periodicityDaily = null;
        var periodicityWeekly = null;
        cadena = [];
        if (periodicity == "Daily") {
            periodicityDaily = "";
            $('#checkbox_daily:checked').each(
                function () {
                    cadena.push($(this).val());
                }
            );
            periodicityDaily = cadena.join(",")
        } else if (periodicity == "Weekly") {
            periodicityWeekly = $("input[name='radio_weekly']:checked").val();
        }
        var WO = new Object();
        WO.projectID = wo_projectId;
        var subActvityProjectScopeId = $("#woc_select_activitysub").val();
        var splitID = subActvityProjectScopeId.split('/');
        WO.scopeID = splitID[1];
        WO.wOPlanID = 1;
        WO.subActivityID = splitID[0];
        WO.priority = $("#select_priority").val();
        WO.periodicityDaily = periodicityDaily;
        WO.periodicityWeekly = periodicityWeekly;
        WO.startDate = $("#Start_Date").val();
        WO.startTime = $("#start_time").val();
        WO.endDate = $("#End_Date").val();
        WO.wOName = $("#WO_name").val();
        WO.signumID = $("#select_assign_to_user").val();
        WO.lastModifiedBy = signumGlobal;
        WO.createdBy = signumGlobal;
        var nodeNameArr = $("#woc_select_nodeName").val();
        var nodeName = nodeNameArr.join();
        var nodeType = $("#select_nodeType").val();
        var listOfNodeJson = { "nodeNames": nodeName, "nodeType": nodeType }
        var listOfNode = [];
        listOfNode.push(listOfNodeJson);
        WO.listOfNode = listOfNode;
        $.isf.ajax({
            url: `${service_java_URL}woManagement/createWorkOrderPlan`,
            context: this,
            crossdomain: true,
            processData: true,
            contentType: C_CONTENT_TYPE_APPLICATION_JSON,
            type: 'POST',
            data: JSON.stringify(WO),
            xhrFields: {
                withCredentials: false
            },
            success: function (data) {
                alert("DATA SUCCESSFULLY SAVED");
                $('#woc_select_activitysub').find('option').remove();
                $("#woc_select_activitysub").select2("val", "");
                $("#select_periodicity").select2("val", 0);
                $("#start_time").select2("val", 0);
                $('#select_nodeType').find('option').remove();
                $("#select_nodeType").select2("val", "");
                $('#woc_select_nodeName').find('option').remove();
                $("#woc_select_nodeName").select2("val", "");
                $('#Start_Date').val("");
                $('#End_Date').val("");
                $('#WO_name').val("");
                $('#modal_wo_creation').modal('hide');
                getWorkOrderDetails();
            },
            error: function (xhr, status, statusText) {
                alert("DATA CAN NOT BE SAVED");
                console.log('An error occurred on : createWO' + xhr.error);
            }
        });
    }
    else {
        alert("Fields Not properly filled");
    }
}

//-----------------------------------Edit view WOPlan----------------------------------------------------

var detailsPlan = [];

function convertDate(str) {
    var date = new Date(str),
        mnth = ("0" + (date.getMonth() + 1)).slice(-2),
        day = ("0" + date.getDate()).slice(-2);
    return [date.getFullYear(), mnth, day].join("-");
}

function getMarketAreaWOwp() {
    $('#select_markAreawp')
        .find('option')
        .remove();
    $("#select_markAreawp").select("val", "");
    $('#select_markAreawp').append('<option value="0">Select 1</option>');
    let elementType = $('#select_nodeTypewp').val();
    let nodeType = $('#select_nodeTypewp2').val();
    let projectID = $('#projectIDwp').val();
    pwIsf.addLayer({ text: C_PLEASE_WAIT });
    let serviceUrl = service_java_URL + "woManagement/getMarketArea?projectID=" + projectID + "&type=" + elementType + "&nodeType=" + nodeType;
    if (ApiProxy == true) {
        serviceUrl = service_java_URL + "woManagement/getMarketArea?" + encodeURIComponent("projectID=" + projectID + "&type=" + elementType + "&nodeType=" + nodeType);
    }
    $.isf.ajax({
        url: serviceUrl,
        success: function (data) {
            pwIsf.removeLayer();
            for (var market = 0; market < data.length; market++) {
                $('#select_markAreawp').append('<option value="' + data[market] + '">' + data[market] + '</option>');
            }
        },
        error: function (xhr, status, statusText) {
            pwIsf.removeLayer();          
            console.log(C_COMMON_ERROR_MSG);
        }
    });
}

function getVendorwp() {
    $('#select_vendorwp')
        .find('option')
        .remove();
    $("#select_vendorwp").select("val", "");
    $('#select_vendorwp').append('<option value="0">Select one</option>');
    let nodeType = $('#select_nodeTypewp').val();
    let marketArea = $('#select_markAreawp').val();
    console.log(marketArea);
    let projectID = $('#projectIDwp').val();
    pwIsf.addLayer({ text: "Please wait ..." });

    $.isf.ajax({
        url: service_java_URL + "woManagement/getVendor/" + projectID + '/' + marketArea + '/' + nodeType,
        success: function (data) {
            pwIsf.removeLayer();
            if (data.isValidationFailed == false) {
                for (var vendor = 0; vendor < data.responseData.length; vendor++) {
                    $('#select_vendorwp').append('<option value="' + data.responseData[vendor] + '">' + data.responseData[vendor] + '</option>');
                }
            }
            else {
                pwIsf.alert({ msg: data.formErrors[0], type: 'error' });
            }
        },
        error: function (xhr, status, statusText) {
            pwIsf.removeLayer();        
            console.log(C_COMMON_ERROR_MSG);
        }
    });
}

function getWFNodeTypewo() {
    $('#select_nodeTypewp')
        .find('option')
        .remove();
    $("#select_nodeTypewp").select2("val", "");
    $('#select_nodeTypewp').append('<option value=""></option>');
    pwIsf.addLayer({ text: "Please wait ..." });
    let projectID = $('#projectIDwp').val();
    if (projectID == undefined || projectID == null || projectID == '' || isNaN(parseInt(projectID))) {
        pwIsf.alert({ msg: "Project ID missing or invalid", type: "error" });
    }
    else {
        $.isf.ajax({

            url: service_java_URL + "woManagement/getNodeType/" + parseInt(projectID),
            success: function (data) {
                pwIsf.removeLayer();
                $.each(data, function (i, d) {
                    $('#select_nodeTypewp').append('<option value="' + d + '">' + d + '</option>');
                })
            },
            error: function (xhr, status, statusText) {
                pwIsf.removeLayer();
                console.log('An error occurred on getDomain: ' + xhr.error);
            }
        });
    }
}

function getNodeTypeEdit() {
    $('#select_nodeTypewp2')
        .find('option')
        .remove();
    $("#select_nodeTypewp2").select("val", "");
    $('#select_nodeTypewp2').append('<option value="0">Select 1</option>');
    let domainSubDomain = $('#domainSpwp').val().split('/');
    let tech = $('#techSpwp').val();
    let nodeType = $("#select_nodeTypewp").val();
    let domain = domainSubDomain[0];
    let subDomain = domainSubDomain[1];    
    pwIsf.addLayer({ text: "Please wait ..." });
    if (projectID && domain && subDomain && tech && nodeType) {
        var serviceUrl = service_java_URL + "activityMaster/getNodeTypeByFilter/" + projectID + '/' + domain + '/' + subDomain + '/' + tech + '/' + nodeType;
        if (ApiProxy == true) {
            serviceUrl = service_java_URL + "activityMaster/getNodeTypeByFilter/" + projectID + '/' + encodeURIComponent(domain) + '/' + subDomain + '/' + tech + '/' + nodeType;
        }

        $.isf.ajax({
            url: serviceUrl,
            success: function (data) {
                data = data.responseData;
                pwIsf.removeLayer();
                $.each(data, function (i, d) {

                    $('#select_nodeTypewp2').append('<option value="' + d + '">' + d + '</option>');
                })



            },
            error: function (xhr, status, statusText) {
                pwIsf.removeLayer();
                console.log(C_COMMON_ERROR_MSG);
            }
        });
    }

    else {
        var temp = [{ name: "Project", value: projectID }, { name: "Domain", value: domain }, { name: "Subdomain", value: subDomain }, { name: "Technology", value: tech }, { name: "Node Type", value: nodeType }];
        var inputs = "";
        for (var i = 0; i < temp.length; i++) {
            if (!temp[i].value) {
                inputs = inputs + temp[i].name + ", ";

            }
        }
        pwIsf.alert({ msg: inputs + 'Missing', type: 'warning' });
    }
}

function getWFNodeNameswp() {
    $('#woc_select_nodeNamewp')
        .find('option')
        .remove();
    $("#woc_select_nodeNamewp").select2("val", "");
    $('#woc_select_nodeNamewp').append('<option value="0"></option>');
    let nodeType = "";
    lstElementType = $('#select_nodeTypewp').val();
    nodeType = $('#select_nodeTypewp2').val(); 

    let projID = localStorage.getItem("views_project_id");
    searchTerm = null;
    let tech = $('#techSpwp').val();
    let domainSubDomain = $('#domainSpwp').val().split('/');
    vendor = $('#select_vendorwp').val();
    market = $('#select_markAreawp').val();
   
    $('#woc_select_nodeNamewp').select2({
        ajax: {
            url: service_java_URL + "woManagement/getNodeNamesByFilter",
            headers: commonHeadersforAllAjaxCall,
            dataType: 'json',
            contentType: "application/json; charset=utf-8",
            delay: 250,
            type: 'POST',
            data: function (params) {
                var query = {
                    projectID: projID,
                    elementType: lstElementType,
                    vendor: vendor,
                    market: market,
                    tech: tech,
                    domain: domainSubDomain[0],
                    subDomain: domainSubDomain[1],
                    term: params.term,
                    type: nodeType,

                }
                return JSON.stringify(query);
            }, 
            processResults: function (data) {
                let mkResult = [];
                if (data[0]) {
                    if (data[0]['lstNodeName']) {
                        let allNodeIds = data[0]['lstNodeName'];
                        for (let i in allNodeIds) {
                            mkResult.push({ id: allNodeIds[i], text: allNodeIds[i] });
                        }
                    }
                }
                return {
                    results: mkResult
                };
            },
        },
        minimumInputLength: 3,
    });
}

function validateNodeswp() {
    let projID = $('#projectIDwp').val();
    lstElementType = $('#select_nodeTypewp').val();
    let nodeType = $('#select_nodeTypewp2').val();
    vendor = $('#select_vendorwp').val();
    market = $('#select_markAreawp').val();
    let tech = $('#techSpwp').val();
    let domainSubDomain = $('#domainSpwp').val().split('/');;
    let domain = domainSubDomain[0];
    let subDomain = domainSubDomain[1];

    let nodeNames = $('#nodeViewwp')[0].value;
    console.log(nodeNames);
    let activitySubactivity = $('#subServSpwp').val();

    if (vendor == '' || vendor == undefined || vendor == null || vendor == 0)
        vendor = 'NA';
    if (market == '' || market == undefined || market == null || market == 0)
        market = 'NA';
    if (nodeType == '' || nodeType == undefined || nodeType == null || nodeType == 0)
        nodeType = 'NA';
    if (lstElementType == '' || lstElementType == undefined || lstElementType == null || lstElementType == 0)
        lstElementType = 'NA';

    pwIsf.addLayer({ text: "Please wait ..." });

    var nodesValidateObj = {};
    nodesValidateObj.projectID = projID;
    nodesValidateObj.elementType = lstElementType;
    nodesValidateObj.type = nodeType;
    nodesValidateObj.vendor = vendor;
    nodesValidateObj.market = market;
    nodesValidateObj.tech = tech;
    nodesValidateObj.domain = domain;
    nodesValidateObj.subDomain = subDomain;
    nodesValidateObj.nodeNames = nodeNames;
    var JsonObj = JSON.stringify(nodesValidateObj);

    $.isf.ajax({
        type: "POST",
        url: service_java_URL + "woManagement/getNodeNamesValidate/",
        dataType: 'json',
        contentType: "application/json; charset=utf-8",
        data: JsonObj,
        success: nodesValidation,
        error: function (xhr, status, statusText) {
            pwIsf.removeLayer();

        }
    });

    function nodesValidation(data) {
        pwIsf.removeLayer();
        $('#validStatus').css('display', 'block');
        var resultStatus = responseHandler(data);
        if (resultStatus) {
            $('#changeIcon').removeClass('fa fa-close glyphicon-orangered').addClass('fa fa-check glyphicon-green');
            nodesValidatedwp = true;
        }
        else {
            $('#changeIcon').removeClass('fa fa-check glyphicon-green').addClass('fa fa-close glyphicon-orangered');
            nodesValidatedwp = false;
        }
    }
}

function isPlanEditable(WOPlanID) {
    var statusJQXHR = $.isf.ajax({
        type: "GET",
        url: service_java_URL + "/woManagement/isPlanEditable?woPlanId=" + WOPlanID,      
        returnAjaxObj: true,
    })    
    return statusJQXHR;
}

function capitalizeText(string) {
    return string.charAt(0).toUpperCase() + string.slice(1).toLowerCase();
}

function populateHourly() {
    $('#ContentHourlywp .hourlywp').html('');
    for (let i = 0; i < 24; i++) {
        let hour = i;
        if (i < 10) {
            hour = '0' + i;
        }
        $('#ContentHourlywp .hourlywp').append('<label class="btn btn-info" id="' + hour + 'hour"><input type="checkbox" name="checkbox_hourlywp" id="checkbox_hourlywp" value="' + hour + ':00:00" onchange="changeHourlywp(this)">' + hour + '</label >');
    }
}

function getTableWOPlan(WOPlanID, mode, statusBool, editClicked, WoPlanType) {
    
    if (editClicked)
        statusJQXHR = isPlanEditable(WOPlanID);

    if (mode == 1 || mode == '1') {
        console.log('Edit called');
        viewWP(statusJQXHR);
    }
    else {
        console.log('Copy called');
        copyWP(statusJQXHR);
    }

    prioritywp = '';
    dailywp = [];
    hourlywp = [];
    weeklywp = '';
    recurrwp = '';
    periodicitywp = 'Single';
    select1wp = '';   
    var wopid = WOPlanID;//1514;
    $('#select_assign_to_user_wp')
        .find('option')
        .remove();

    $("input[name=radio_prioritywp]").prop('checked', false);
    $('.prioritywp label').removeClass('active');

    $('.recurrencewp label#Weekly_radio').removeClass('active');
    $("input[name=radio_recurrwp][value=Weekly]").prop('checked', false);

    $("input[name=radio_recurrwp][value=Daily]").prop('checked', false);
    $('.recurrencewp label#Daily_radio').removeClass('active');

    $("input[name=radio_recurrwp][value=Hourly]").prop('checked', false);
    $('.recurrencewp label#Hourly_radio').removeClass('active');

    $("input[name=radio_weeklywp]").prop('checked', false);
    $('.weeklywp label').removeClass('active');
    getWFUserByProjectIdwp();

    localStorage.setItem("woPlanID", WOPlanID);
    $('#projectIDwp').val(localStorage.getItem("woPlanID"));

    var startDate = "";
    var endDate = "";
    var startTime;
    var sd = "";
    var ed = "";
    var url = "";
    var st = "";
    $.isf.ajax({
        url: service_java_URL + "woManagement/getWOPlanDetails/" + wopid,

        success: function (data) {

            startDate = data.wOPlanDetails[0].StartDate;
            sd = GetConvertedDateWO(startDate);
            endDate = data.wOPlanDetails[0].EndDate;
            ed = GetConvertedDateWO(endDate);
            startTime = sd+" "+data.wOPlanDetails[0].StartTime;
            st = GetConvertedTimeWO(startTime);

            $('#Start_Date_WP').val(sd);
            $('#End_Date_wp').val(ed);
            $('#WO_nameWP').val(data.wOPlanDetails[0].WOName);
            $('#WoPlanType').val(WoPlanType);
            $('#slaHrswp').val(data.wOPlanDetails[0].SLA);
            $("#start_time_WP").val(st);
            $("#flowchartdefID").val(data.domainDetails[0].flowchartdefID);
            $("#start_time_WP").trigger('change');
            var nTArr = data.nodeType.NodeType;

            $('#versionNumberwp').val(data.domainDetails[0].wfVersion);
            $('#domainSpwp').val(data.domainDetails[0].domain + '/' + data.domainDetails[0].SubDomain);
            $('#subServSpwp').val(data.domainDetails[0].SubServiceArea);
            $('#scopeSpwp').val(data.domainDetails[0].ScopeName);
            $('#techSpwp').val(data.domainDetails[0].Technology);
            $('#ActSubActSpwp').append(data.domainDetails[0].Activity + '/' + data.domainDetails[0].SubActivity);
            $('#scopeIDwp').val(data.domainDetails[0].ProjectScopeID);
            $('#SubActivityIDwp').val(data.domainDetails[0].SubActivityID);
            $('#WfNamewp').append(data.domainDetails[0].WorkFlowName);
            $('#versionNamewp').val(data.domainDetails[0].WorkFlowName);
            $('#projectIDwp').val(data.domainDetails[0].ProjectID);
            $('#select_assign_to_user_wp').find('option')
                .remove();
            $('#selectAspUserSignumwp')
                .find('option')
                .remove();
            getAspByScopeId();
            var array = [];
            var users = data.assignedUsers;
            if (users != null && users != "") {
                
                if (localStorage.getItem("projectType") == "ASP") {
                    $('#nonAspEdit').remove();
                    for (var i = 0; i < users.length; i++) {
                        if (array.indexOf(users[i].SignumID) < 0) {
                            console.log(array);
                            array.push(users[i].SignumID);
                            $('#selectAspUserSignumwp').append('<option value="' + users[i].SignumID + '">' + users[i].SignumID + " - " + users[i].EmployeeName + '</option>');
                        }
                    }
                    $.each(array, function (index, reg) {
                        $("#selectAspUserSignumwp option[value='" + reg + "']").prop("selected", true);                        
                    });
                    if (flag == 3) 
                        $("#selectAspUserSignumwp").trigger('change');

                }
                else {
                    $('#aspEdit').remove();
                    for (var i = 0; i < users.length; i++) {
                        if (array.indexOf(users[i].SignumID) < 0) {
                            console.log(array);
                            array.push(users[i].SignumID);
                            $('#select_assign_to_user_wp').append('<option value="' + users[i].SignumID + '">' + users[i].SignumID + " - " + users[i].EmployeeName + '</option>');
                        }
                    }
                    $.each(array, function (index, reg) {
                        $("#select_assign_to_user_wp option[value='" + reg + "']").prop("selected", true);
                    });
                    if( flag ==3 )
                        $("#select_assign_to_user_wp").trigger('change');
                }
            }
            else{
                if (localStorage.getItem("projectType") == "ASP") {
                    $('#nonAspEdit').remove();
                }
                else{
                    $('#aspEdit').remove();
                }
            }

            if (data.wOPlanDetails[0].Periodicity_Daily != null || data.wOPlanDetails[0].Periodicity_Weekly != null || data.wOPlanDetails[0].PeriodicityHourly != null) {
                periodicitywp = '';
                $('#recGrpwp').show();
                $('#faUpwp').show();
                $('#faDwnwp').hide();

                if (data.wOPlanDetails[0].Periodicity_Daily != null) {
                    recurrwp = 'Daily';
                    $('.recurrencewp label#Weekly_radio').removeClass('active');
                    $("input[name=radio_recurrwp][value=Weekly]").prop('checked', false);
                    $("input[name=radio_recurrwp][value=Hourly]").prop('checked', false);
                    $("input[name=radio_recurrwp][value=Daily]").prop('checked', true);
                    $('.recurrencewp label#Daily_radio').removeClass('disabled');
                    $('.recurrencewp label#Daily_radio').addClass('active');

                    $('#ContentWeeklywp').hide();
                    $('#ContentHourlywp').hide();
                    $('#endDateDivwp').hide();
                    let dailyarr = [];
                    dailyarr = data.wOPlanDetails[0].Periodicity_Daily.split(",");
                    for (var ch = 0; ch < dailyarr.length; ch++) {
                        console.log(dailyarr[ch]);
                        $("input[name='checkbox_dailywp']").each(function () {
                            let value = $(this).val();
                            if (value == dailyarr[ch]) {
                                $(this).prop('checked', true);
                                $('.dailywp label#' + value + 'Chk').addClass('active');
                                $('.dailywp label#' + value + 'Chk').removeClass('disabled');
                                dailywp.push(dailyarr[ch]);
                            }

                        });


                    }
                    $('#ContentDailywp').show();
                    $('#endDateDivwp').show();

                }
                else if (data.wOPlanDetails[0].PeriodicityHourly != null) {
                    recurrwp = 'Hourly';
                    $('.recurrencewp label#Weekly_radio').removeClass('active');
                    $("input[name=radio_recurrwp][value=Weekly]").prop('checked', false);
                    $("input[name=radio_recurrwp][value=Daily]").prop('checked', false);
                    $("input[name=radio_recurrwp][value=Hourly]").prop('checked', true);
                    $('.recurrencewp label#Hourly_radio').removeClass('disabled');
                    $('.recurrencewp label#Hourly_radio').addClass('active');

                    $('#ContentDailywp').hide();
                    $('#ContentWeeklywp').hide();
                    $('#endDateDivwp').hide();
                    let dailyarr = [];
                    dailyarr = data.wOPlanDetails[0].PeriodicityHourly.split(",");
                    for (var ch = 0; ch < dailyarr.length; ch++) {
                        console.log(dailyarr[ch]);
                        $("input[name='checkbox_hourlywp']").each(function () {
                            let value = $(this).val();
                            if (value == dailyarr[ch]) {
                                $(this).prop('checked', true);
                                let idNumber = value.substring(0, 2);
                                $('.hourlywp label#' + idNumber + 'hour').addClass('active');
                                $('.hourlywp label#' + idNumber + 'hour').removeClass('disabled');
                                hourlywp.push(dailyarr[ch]);
                            }

                        });


                    }
                    $('#ContentHourlywp').show();
                    $('#endDateDivwp').show();

                }
                else if (data.wOPlanDetails[0].Periodicity_Weekly != null) {
                    recurrwp = 'Weekly';
                    $('.recurrencewp label#Daily_radio').removeClass('active');
                    $("input[name=radio_recurrwp][value=Daily]").prop('checked', false);
                    $("input[name=radio_recurrwp][value=Hourly]").prop('checked', false);
                    $("input[name=radio_recurrwp][value=Weekly]").prop('checked', true);
                    $('.recurrencewp label#Weekly_radio').removeClass('disabled');
                    $('.recurrencewp label#Weekly_radio').addClass('active');

                    $('#ContentDailywp').hide();
                    $('#ContentHourlywp').hide();
                    $('#endDateDivwp').hide();
                    $('#ContentWeeklywp').show();
                    $('#endDateDivwp').show();
                    let weekly = '';
                    weekly = data.wOPlanDetails[0].Periodicity_Weekly;
                    console.log(weekly);
                    weeklywp = weekly;
                    $("input[name=radio_weeklywp][value=" + weekly + "]").prop('checked', true);
                    $('.weeklywp label#' + weekly).removeClass('disabled');
                    $('.weeklywp label#' + weekly).addClass('active');
                }
            }
            else
                periodicitywp = 'Single';


            let priority;
            priority = data.wOPlanDetails[0].Priority;
            prioritywp = priority;
            console.log(priority);
            let priorityCapitalized = capitalizeText(priority);
            $("input[name=radio_prioritywp][value=" + priorityCapitalized + "]").prop('checked', true);
            $('.prioritywp label#' + priorityCapitalized + 'wp').removeClass('disabled');
            $('.prioritywp label#' + priorityCapitalized + 'wp').addClass('active');




            if (data.isChecked == true) {
                $("shownodeWisewp").prop('checked', true);
                $("nodeWisewp").prop('checked', true);
                select1wp = 'singleUserTrue';
            }
            else {
                $("shownodeWisewp").prop('checked', false);
                $("nodeWisewp").prop('checked', false);
                select1wp = '';
            }

            if (data.nodeNames.length > 0) {
                console.log(data.nodeNames.length);
                if (mode == 1 || mode == '1') {
                    $('#slideUpwp').hide();
                    $('#slideDwnwp').hide();
                    $('.node_infowp').show();
                }


                $("#nodetypeInfowp").append(data.nodeType[0].NodeType);
                $("#mrktInfowp").append(data.nodeType[0].NodeType);
                $("#vendorInfowp").append(data.nodeType[0].NodeType);

                let nodeNames = data.nodeNames;
                let nodeNamesPop = '';
                for (let i = 0; i < nodeNames.length; i++) {
                    nodeNamesPop = nodeNamesPop + ',' + nodeNames[i].NodeNames;
                }
                $("#nodeNamesInfowp").append(nodeNamesPop.replace(/(^,)|(,$)/g, ""));


                $("#nodeViewwp").val(nodeNamesPop.replace(/(^,)|(,$)/g, ""));




            }
            else
                $('.node_infowp').hide();


            if (data.isChecked == false) {
                $('#shownodeWisewp').prop('checked', false);
                $('#nodeWisewp').prop('checked', false);
                select1wp = '';
            }
            else {
                $('#nodeWisewp').prop('checked', true);
                $('#shownodeWisewp').prop('checked', true);
                select1wp = 'singleUserTrue';
            }

            if (data.execPlanDetails != null) {
                execPlan = true;
                $('#execPlanIdwp').html('');
                $('#exePlanwp').html('');

                $('#execPlandiv').show();
                $('#slaHrsDivwp').hide();

                $('#exePlanwp').prop('disabled', false);
                $('#execPlanIdwp').prop('disabled', false);

                $('#execPlanIdwp').append(data.execPlanDetails.executionPlanId);
                $('#exePlanwp').append(data.execPlanDetails.planName);

                $("#btnRecurr").prop("disabled", true);
            }
            else {
                execPlan = false;
                $('#execPlandiv').hide();
                $('#execPlanIdwp').html('');
                $('#exePlanwp').html('');
                $('#slaHrsDivwp').show();
            }
        },
        error: function (xhr, status, statusText) {
            alert("DATA CAN NOT BE GET");
            console.log('An error occurred on : ModifyWO' + xhr.error);
        }
    });
    $.isf.ajax({
        url: service_java_URL + "woManagement/getCountOfUnassignedWOByWOPLAN?woplanid=" + wopid,
        success: function (data) {
            $("#woCountForWOPlan").text(data);
        }
    })


}

function closeModalwp() {

    prioritywp = '';
    dailywp = [];
    hourlywp = [];
    weeklywp = '';
    recurrwp = '';
    periodicitywp = 'Single';
    select1wp = '';
    $('#select_assign_to_user_wp')
        .find('option')
        .remove();
    $('#select_assign_to_user_wp')
        .select2("val", "");
    $('#woCountDivWP').hide();
    $("#woCountWP").val('');
    $("input[name=radio_prioritywp]").prop('checked', false);
    $('.prioritywp label').removeClass('active');

    $('.recurrencewp label#Weekly_radio').removeClass('active');
    $("input[name=radio_recurrwp][value=Weekly]").prop('checked', false);

    $("input[name=radio_recurrwp][value=Daily]").prop('checked', false);
    $('.recurrencewp label#Daily_radio').removeClass('active');

    $("input[name=radio_weeklywp]").prop('checked', false);
    $('.weeklywp label').removeClass('active');

    $('#select_markAreawp')
        .find('option')
        .remove();
    $("#select_markAreawp").select("val", "");


    $('#select_vendorwp')
        .find('option')
        .remove();
    $("#select_vendorwp").select("val", "");


    $('#select_nodeTypewp')
        .find('option')
        .remove();
    $("#select_nodeTypewp").select("val", "");

    $("#select_nodeTypewp2").select("val", "");


    $('#woc_select_nodeNamewp')
        .find('option')
        .remove();
    $('#woc_select_nodeNamewp')
        .find('option')
        .remove();
    $("#woc_select_nodeNamewp").select("val", "");

    $('#nodeViewwp').val("");


    $('#recGrpwp').hide();
    $('#faUpwp').hide();
    $('#faDwnwp').show();
    $('#ContentWeeklywp').hide();
    $('#endDateDivwp').hide();
    $('#ContentDailywp').hide();
    $('#ContentHourlywp').hide();
    $('#endDateDivwp').hide();

    $('#slideUpwp').show();
    $('#slideDwnwp').hide();
    $('#saveWorkOrderwp').hide();
    $('#allUserAnchorwpspan').hide();
    $('.node_infowp').hide();

    $("#nodetypeInfowp").html("");
    $("#mrktInfowp").html("");;
    $("#vendorInfowp").html("");
    $("#nodeNamesInfowp").html("");
    $('#ActSubActSpwp').html("");
    $('#WfNamewp').html("");
    $('#Start_Date_WP').val("");
    $('#End_Date_wp').val("");
    $('#WO_nameWP').val("");
    $('#slaHrswp').val("");
    $('#projectIDwp').val("");
    $("#start_time_WP").select2("val", "");

    $('#versionNumberwp').val("");
    $('#domainSpwp').val("");
    $('#subServSpwp').val("");
    $('#scopeSpwp').val("");
    $('#techSpwp').val("");
    $('#scopeIDwp').val("");
    $('#SubActivityIDwp').val("");
    $('#versionNamewp').val("");
    $('#modal_WP_EditView').modal('hide');

    $('.dailywp label ').removeClass('active');
    $('.hourlywp label ').removeClass('active');
    $('.weeklywp label ').removeClass('active');
    $('.prioritywp label ').removeClass('active');

    $('#showUserSelect').hide();

    execPlan = false;
    $('#execPlandiv').hide();
    $('#execPlanIdwp').html('');
    $('#exePlanwp').html('');
    $('#slaHrsDivwp').show();

    $('#execPlanWarn').hide();
    console.log('periodicitywp ' + periodicitywp);
    console.log('dailywp ' + dailywp);
    console.log('hourlywp ' + hourlywp);
    console.log('weeklywp ' + weeklywp);
    console.log('prioritywp ' + prioritywp);
    console.log('recurrwp ' + recurrwp);
    console.log('select1wp ' + select1wp);
}

function viewWP(statusJQXHR) {
    flag = 1;
    $('#woCountDivWP').hide();
    $("#woCountWP").val('');
    $('.commenting-field').css("display", "none");
    $('#btnview').prop('disabled', true);
    statusJQXHR.done(function (status) {
        if (!status) {
            $('#btnedit').prop('disabled', true);
            $('#btnedit').css('background', "#ffffff");
        }
        else
            $('#btnedit').prop('disabled', false);

        wpStatus = !!status;
    });
    $('#btncopy').prop('disabled', false);
    $('.node_infowp').show();
    $('.node_boxwp').hide();
    $('.recurrencewp label').addClass('disabled');
    $('.prioritywp label').addClass('disabled');
    $('.dailywp label').addClass('disabled');
    $('.hourlywp label').addClass('disabled');
    $('.weeklywp label').addClass('disabled');
    $('#btnRecurr').prop('disabled', true);
    $('#WO_nameWP').prop("readonly", true);
    $("#select_assign_to_user_wp").prop("disabled", true);
    $("#selectAspUserSignumwp").prop("disabled", true);
    $("#select_periodicitywp").prop("disabled", true);
    $("#Start_Date_WP").prop("disabled", true);
    $("#start_time_WP").prop("disabled", true);
    $("#End_Date_wp").prop("disabled", true);
    $("#slaHrswp").prop("disabled", true);
    $('#nodeWise').prop("disabled", true);
    $("#nodetypeInfowp").html("");
    $("#mrktInfowp").html("");;
    $("#vendorInfowp").html("");
    $("#nodeNamesInfowp").html("");
    $('#ActSubActSpwp').html("");
    $('#WfNamewp').html("");
    $('#showUserSelect').show();
    $('#execPlanWarn').hide();
    $('#saveWorkOrderwp').hide();
    $('#allUserAnchorwpspan').hide();
    $("#selectallSignumdivwp").hide();
    $("#signumbyprojectdivwp").show();
    console.log(periodicitywp);
    console.log(dailywp);
    console.log(hourlywp);
    console.log(weeklywp);
    console.log(prioritywp);
    console.log(recurrwp);


}

function openWP() {
    flag = 2;
    $('#woCountDivWP').hide();
    $("#woCountWP").val('');
    $('#btnview').prop('disabled', false);
    $('#btnedit').prop('disabled', true);
    $('#btncopy').prop('disabled', false);
    $('.commenting-field').css("display", "block");
    $('.recurrencewp label').removeClass('disabled');
    $('.prioritywp label').removeClass('disabled');
    $('.dailywp label').removeClass('disabled');
    $('.hourlywp label').removeClass('disabled');
    $('.weeklywp label').removeClass('disabled');
    if (!execPlan)
        $('#btnRecurr').prop('disabled', false);
    else
        $('#btnRecurr').prop('disabled', true);
    $('#WO_nameWP').prop("readonly", false);
    $("#select_assign_to_user_wp").prop("disabled", true);
    
    $("#selectAspUserSignumwp").prop("disabled", false);
    $("#select_periodicitywp").prop("disabled", false);
    $("#Start_Date_WP").prop("disabled", false);
    $("#start_time_WP").prop("disabled", false);
    $("#End_Date_wp").prop("disabled", false);
    $("#slaHrswp").prop("disabled", false);
    $('#nodeWise').prop("disabled", false);

    $('#slideUpwp').hide();
    $('#slideDwnwp').hide();
    $('.node_infowp').hide();
    $('.node_boxwp').show();
    $('#showUserSelect').hide();
    $('#execPlanWarn').hide();
    $('#saveWorkOrderwp').show();
    $('#allUserAnchorwpspan').hide();
    $("#selectallSignumdivwp").hide();
    $("#signumbyprojectdivwp").show();
    getWFNodeTypewo();

    console.log(periodicitywp);
    console.log(dailywp);
    console.log(hourlywp);
    console.log(weeklywp);
    console.log(prioritywp);
    console.log(recurrwp);
}

function getAspByScopeId() {
    return $.isf.ajax({
        url: service_java_URL + "/aspManagement/getAspByScope?projectScopeId=" + $('#scopeIDwp').val(),
        success: function (data) {
            $.each(data, function (i, d) {
                if (!$("#selectAspUserSignumwp option[value='" + d.Signum + "']").length) {
                    $('#selectAspUserSignumwp').append('<option value="' + d.Signum + '">' + d.aspNameSignum + '</option>');
                }
            })
        },
        error: function (xhr, status, statusText) {
            console.log("Fail " + xhr.responseText);
        }
    });
}

function getAspByWoId(woid) {
    return $.isf.ajax({
        url: service_java_URL + "/aspManagement/getAspByWoid?woId=" + woid,
        success: function (data) {
            $.each(data, function (i, d) {
                $('#selectAspUserSignumTransfer').append('<option value="' + d.Signum + '">' + d.aspNameSignum + '</option>');
            })

           

        },
        error: function (xhr, status, statusText) {
            console.log("Fail " + xhr.responseText);
        }
    });
}

function copyWP(statusJQXHR) {

    flag = 3;
    $('.commenting-field').css("display", "block");
    $('#btnview').prop('disabled', false);
    statusJQXHR.done(function (status) {
        if (!status) {
            $('#btnedit').prop('disabled', true);
            $('#btnedit').css('background', "#ffffff");
        }
        else
            $('#btnedit').prop('disabled', false);

        wpStatus = !!status;
    });
    $('#btncopy').prop('disabled', true);
    $('.node_infowp').show();
    $('.node_boxwp').hide();
    $('.recurrencewp label').removeClass('disabled');
    $('.prioritywp label').removeClass('disabled');
    $('.dailywp label').removeClass('disabled');
    $('.hourlywp label').removeClass('disabled');
    $('.weeklywp label').removeClass('disabled');
    $('#btnRecurr').prop('disabled', false);
    $('#WO_nameWP').prop("readonly", false);
    $("#select_assign_to_user_wp").prop("disabled", false);
    $("#select_periodicitywp").prop("disabled", false);
    $("#Start_Date_WP").prop("disabled", false);
    $("#start_time_WP").prop("disabled", false);
    $("#End_Date_wp").prop("disabled", false);
    $("#slaHrswp").prop("disabled", false);
    $('#nodeWise').prop("disabled", false);
    $("#nodetypeInfowp").html("");
    $("#mrktInfowp").html("");;
    $("#vendorInfowp").html("");
    $("#nodeNamesInfowp").html("");
    $('#ActSubActSpwp').html("");
    $('#WfNamewp').html("");
    $('#saveWorkOrderwp').show();
    $('#slideUpwp').hide();
    $('#slideDwnwp').hide();
    $('.node_infowp').hide();
    $('.node_boxwp').show();
    $('#showUserSelect').hide();
    $('#execPlanWarn').show();
    $('#saveWorkOrderwp').show();
    $('#allUserAnchorwpspan').show();
    $("#selectallSignumdivwp").hide();
    $("#signumbyprojectdivwp").show();
    $("#selectAspUserSignumwp").prop("disabled", false);
    $("#select_assign_to_user_wp").val('').trigger('change');
    getWFNodeTypewo();

    console.log(periodicitywp);
    console.log(dailywp);
    console.log(hourlywp);
    console.log(weeklywp);
    console.log(prioritywp);
    console.log(recurrwp);


}

function openRecurrencewp() {
    if (periodicitywp == 'Single') {
        periodicitywp = '';
        document.getElementById("recGrpwp").style.display = 'inline';
        document.getElementById("recGrpwp").style.float = 'right';
        document.getElementById("faDwnwp").style.display = 'none';
        document.getElementById("faUpwp").style.display = 'inline';
        document.getElementById("endDateDivwp").style.display = "inline";
    }
    else {
        periodicity = 'Single';
        document.getElementById("recGrpwp").style.display = 'none';
        document.getElementById("ContentDailywp").style.display = "none";
        document.getElementById("ContentHourlywp").style.display = "none";
        document.getElementById("ContentWeeklywp").style.display = "none";
        document.getElementById("endDateDivwp").style.display = "none";
        document.getElementById("faDwnwp").style.display = 'inline';
        document.getElementById("faUpwp").style.display = 'none';
        if ($('.dailywp label ').hasClass('active'))
            $('.dailywp label ').removeClass('active');
        if (daily != null)
            daily = [];
        if ($('.hourlywp label ').hasClass('active'))
            $('.hourlywp label ').removeClass('active');
        if (hourlywp != null)
            hourlywp = [];
        if ($('.weeklywp label ').hasClass('active'))
            $('.weeklywp label ').removeClass('active');
        if (weekly != '')
            weekly = '';
        if ($('.recurrencewp label ').hasClass('active'))
            $('.recurrencewp label ').removeClass('active');
        
    }

}

function openModalSlaperiodicitywp(value) {
    console.log(value);
    if (value == "Weekly") {

        document.getElementById("ContentDailywp").style.display = "none";
        document.getElementById("ContentHourlywp").style.display = "none";
        document.getElementById("ContentWeeklywp").style.display = "inline";
    }
    if (value == "Daily") {

        $('.weeklywp input[type=radio]').siblings().removeClass('active');
        document.getElementById("ContentWeeklywp").style.display = "none";
        document.getElementById("ContentHourlywp").style.display = "none";
        document.getElementById("ContentDailywp").style.display = "inline";
    }
    if (value == "Hourly") {

        $('.weeklywp input[type=radio]').siblings().removeClass('active');
        document.getElementById("ContentWeeklywp").style.display = "none";
        document.getElementById("ContentDailywp").style.display = "none";
        document.getElementById("ContentHourlywp").style.display = "inline";
    }
    if (value == "Single") {
        dailywp = [];
        weeklywp = '';
        hourlywp = [];
        document.getElementById("ContentDailywp").style.display = "none";
        document.getElementById("ContentWeeklywp").style.display = "none";
        document.getElementById("ContentHourlywp").style.display = "none";
        document.getElementById("endDateDivwp").style.display = "none";
    }
}

function getAllUserswp() {
    $('#allUserAnchorwp').hide();
    $('#userByProjAnchorwp').show();
    $("#selectallSignumdivwp").show();    
    $("#signumbyprojectdivwp").hide();
    getWFUserwp();
    if (flag == 3) {
        $('#woCountDivWP').show();
        $("#woCountWP").val('1');
    }
}

function checkNumberWP(el) {
    if ($(el).val() < 0 || $(el).val() == 0) {
        $(el).val('1');
    }
    else if ($(el).val() > 20) {
        $(el).val('20');
    }
}

function getWFUserwp() {

    $('#select_assign_to_user_wp')
        .find('option')
        .remove();
    $("#select_assign_to_user_wp").select2("val", "");
    $('#select_assign_to_user_wp').append('<option value="0"></option>');


    $("#select_assign_to_user-Required").text("")

    
    function split(val) {
        return val.split(/,\s*/);
    }
    function extractLast(term) {
        return split(term).pop();
    }



    $("#selectUserAllSignumwp").autocomplete({

        appendTo: "#modal_WP_EditView",

        source: function (request, response) {

            $.isf.ajax({

                url: service_java_URL + "activityMaster/getEmployeesByFilter",
                type: "POST",
                data: {
                    term: extractLast(request.term)
                },

                success: function (data) {                    
                    $("#selectUserAllSignumwp").autocomplete().addClass("ui-autocomplete-loading");

                    var result = [];
                    $.each(data, function (i, d) {
                        result.push({
                            "label": d.signum + "/" + d.employeeName,
                            "value": d.signum
                        });
                    });

                    response($.ui.autocomplete.filter(
                        result, extractLast(request.term)));
                    $("#selectUserAllSignumwp").autocomplete().removeClass("ui-autocomplete-loading");

                }
            });
        },

        search: function () {
            // custom minLength
            var term = extractLast(this.value);
            if (term.length < 3) {
                return false;
            }
        },
        focus: function () {
            // prevent value inserted on focus
            return false;
        },
        select: function (event, ui) {
            var terms = split(this.value);
            // remove the current input
            terms.pop();
            // add the selected item
            terms.push(ui.item.value);
            // add placeholder to get the comma-and-space at the end
            terms.push("");
            this.value = terms.join(", ");
            return false;
        },
        change: function (event, ui) {
            if (!ui.item) {
                $(this).val("");                
                $('#emptysignum-message').show();
                if (flag == 3) {
                    $("#woCountDivWP").show();
                    $("#woCountWP").val('1');
                }
            } else {
                $('#emptysignum-message').hide();
                $("#woCountDivWP").hide();
            }
        },


        minLength: 3

    });

    $("#selectUserAllSignumwp").autocomplete("widget").addClass("fixedHeight");
}

function getWFUserByProjectwp() {
    $('#selectallSignumdivwp').hide();
    $('#selectUserAllSignumwp').val('');
    $('#emptysignum-message').hide();


    $('#signumbyprojectdivwp').show();
    $('#allUserAnchorwp').show();
    $('#userByProjAnchorwp').hide();
    getWFUserByProjectIdwp();

}

function getWFUserByProjectIdwp() {

    $('#select_assign_to_user_wp')
        .find('option')
        .remove();
    $("#select_assign_to_user_wp").select2("val", "");
    $('#select_assign_to_user_wp').append('<option value="0"></option>');
    pwIsf.addLayer({ text: "Please wait ..." });

    $.isf.ajax({

        url: service_java_URL + "activityMaster/getEmployeeByProject/" + projectID,
        beforeSend: function (xhr) {
            $("#select_assign_to_user_wp").html("<option> loading ... </option>"); //doesn't work
        },
        success: function (data) {
            pwIsf.removeLayer();
            $.each(data, function (i, d) {
                $('#select_assign_to_user_wp').append('<option value="' + d.signum + '">' + d.signum + " - " + d.employeeName + '</option>');
            })
           
        },
        complete: function () {
            $('#select_assign_to_user_wp').find("option").eq(0).remove();
        },
        error: function (xhr, status, statusText) {
            pwIsf.removeLayer();
            console.log('An error occurred on getEmployeeDetails: ' + xhr.error);
        }
    });

}

function validationsWP() {
    let OK = true;
    let noOfusers = $("#select_assign_to_user_wp").val();
    var k = "";
    let st = $("#Start_Date_WP").val();
    var timeZone = localStorage.getItem("UserTimeZone");
    if (timeZone == null || timeZone == "" || timeZone == undefined) {
        timeZone = "Asia/Calcutta";
    }
    var now = new Date();
    var today = moment(now.toString()).tz(timeZone).format();
    var today1 = today.split("T")[0];
    $('#Start_Date-Requiredwp').text("");
    if (st < today1) {
        pwIsf.alert({ msg: "Date should not be less than today's date", type: 'warning' });
        OK = false;
    }
    $('#WO_name-Requiredwp').text("");
    if ($("#WO_nameWP").val() == null || $("#WO_nameWP").val() == "") {
        $('#WO_name-Requiredwp').text("WorkOrder Name name is required");
        console.log('WO Name not valid');
        OK = false;
    }   
    $('#slaHrsRequiredwp').text("");
    if ($('#slaHrswp').val() == null || $('#slaHrswp').val() == "" || $('#slaHrswp').val() == 'undefined' || $('#slaHrswp').val() == 0 || $('#slaHrswp').val() == '0') {
        $('#slaHrsRequiredwp').text("SLA Hrs required");
        console.log('SLA HRS' + $('#slaHrswp').val());
        console.log('Sla not valid');
        OK = false;
    }
  
    $('#priorityRequiredwp').text("");
    if (prioritywp == '') {
        $('#priorityRequiredwp').text("priority is required");
        console.log('Priority not valid');
        OK = false;
    }
    let allSignumIDArr1 = $("#selectUserAllSignumwp").val();


    if (localStorage.getItem("projectType") == "ASP") {
        allSignumIDArr = $("#selectAspUserSignumwp").val();
        allSignumIDArr.push("");
    }
    else {
        allSignumIDArr = allSignumIDArr1.split(", ");
    }

    let lenthpopallSignumIDArr = allSignumIDArr.splice(-1, 1);
    let lengthofallSignumIDArr = lenthpopallSignumIDArr.length;

    let signumIDArr = $("#select_assign_to_user_wp").val();

    $('#select_assign_to_user_wp-Required').text("");
    if (signumIDArr == null && lengthofallSignumIDArr == 0) {

        $('#select_assign_to_user_wp-Required').text("Assign to User is required");
        OK = false;
    }

    if (signumIDArr != null) {
        var k = $("#select_assign_to_user_wp").val();
    }
    else {
        var k = allSignumIDArr;
    }
    
    $('#Start_Date-Requiredwp').text("");
    if ($("#Start_Date_WP").val() == null || $("#Start_Date_WP").val() == "") {
        $('#Start_Date-Requiredwp').text("Start date is required");
        console.log('Start date 1 not valid');
        OK = false;
    } else if (new Date(today1).getDate() > new Date(st).getDate()) {
        $('#Start_Date-Requiredwp').text("Start date should not be less than today's date");
        console.log('Start date 2 not valid');
        OK = false;
    }

    $('#periodReqwp').text("");
    if (periodicitywp != 'Single' && (dailywp.length == 0 && weeklywp == '' && hourlywp.length == 0)) {
        console.log('periodicity ' + periodicitywp + ' daily ' + dailywp + ' weekly ' + weeklywp + ' hourly ' + hourlywp)
        $('#periodReqwp').text("Select Recurrence");
        console.log('Periodicity not valid');
        OK = false;
    }

  
    $('#End_Date-Requiredwp').text("");
    if (periodicitywp != 'Single' && ($("#Start_Date_WP").val() > $("#End_Date_wp").val())) {
        console.log("Start Time is greater than End Date 1")
        $('#End_Date-RequireddReqwp').text("Start Date is greater than End Date");
        console.log('END date 1 not valid');
        OK = false;
    }
    $('#End_Date-Requiredwp').text("");
    if (periodicity != 'Single' && ($("#End_Date_wp").val() != "" && $("#Start_Date_WP").val() > $("#End_Date_wp").val())) {
        console.log("Start DATE is greater than End Date 2")
        $('#End_Date-Requiredwp').text("Start Date is greater than End Date");
        console.log('END date 2 not valid');
        OK = false;
    }


    $('#start_time-Requiredwp').text("");
    if ($("#start_time_WP option:selected").val() == "0") {
        $('#start_time-Requiredwp').text("Start Time is required");
        console.log('Start TIME  not valid');
        OK = false;
    }

    $('#End_Date-Requiredwp').text("");
    if (periodicitywp != 'Single' && ($("#End_Date_wp").val() == null || $("#End_Date_wp").val() == "")) {
        console.log('Date ' + $("#End_Date_wp").val());
        $('#End_Date-Requiredwp').text("End date is required");
        console.log('END date 2 not valid');
        OK = false;
    }

    var dt = Date.parse(localStorage.getItem("StartDate"));


    if (new Date($("#Start_Date_WP").val()) < new Date(localStorage.getItem("StartDate"))) {
        alert("Start date is smaller than project start date");
        console.log('Start date < LESS THAN END DATE');
        OK = false;
    }

    console.log('11' + OK);
    if (new Date($("#End_Date_wp").val()) < new Date(localStorage.getItem("StartDate"))) {
        alert("End date is smaller than project start date");
        console.log("End_Date is smaller that project start date");
        OK = false;
    }

    console.log('12' + OK);

    let nodeNameAuto = $("#woc_select_nodeNamewp").select2("val");
    let nodeNameCustom = $('#nodeViewwp')[0].value;
    let nodeNamesFinal = ""; let nodeNamesDistinct = "";

    if (nodeNameAuto == null || nodeNameAuto == "" || nodeNameCustom == "") {
        if (nodeNameAuto == "" || nodeNameAuto == null)
            nodeNamesFinal = nodeNameCustom;
        else
            nodeNamesFinal = nodeNameAuto.toLocaleString();
    }
    else { nodeNamesFinal = nodeNameAuto + "," + nodeNameCustom; }

    if (nodeNamesFinal == null)
        nodeNamesFinal = "";

    let arr = $.unique(nodeNamesFinal.split(','));
    nodeNamesDistinct = arr.join(",");


    if ((select1wp == 'singleUserTrue') && (k == null || k.length > 1)) {
        if (noOfusers == null || noOfusers.length > 1 || nodeNamesDistinct == '' || nodeNamesDistinct == null || nodeNamesDistinct == 'undefined') {
            alert('Multiple user selected or no nodes selected to create node wise work order');
            $('#nodeWisewp').prop('checked', false);
            select1wp = '';
            OK = false;
        }
    }
    console.log('13' + OK);

    if ($('#nodeViewwp')[0].value != "") {
        if ((!nodesValidatedwp)) {
            $('#validateNodes-Requiredwp').text("Validate Nodes");
            OK = false;
        }
    }


    return OK;



}

function userWiseNodeSelectOtionwp() {

    let assignToUserLength = 0;
    var assignToAllUserLength = 0;
    let assignToUser = $("#select_assign_to_user_wp").val()
    var assignToAllUser = $("#selectUserAllSignumwp").val()
    var assignToAllUser1 = assignToAllUser.split(", ");
    var assignToAllUserLength1 = assignToAllUser1.slice(0, -1);

    if (assignToUser != null)
        assignToUserLength = assignToUser.length;
    if (assignToAllUserLength1 != null)
        assignToAllUserLength = assignToAllUserLength1.length;


    if (select1wp == '')
        select1wp = 'singleUserTrue';
    else
        select1wp = '';

    let nodeNameAuto = $("#woc_select_nodeNamewp").select2("val");
    let nodeNameCustom = $('#nodeViewwp')[0].value;
    let nodeNamesFinal = ""; let nodeNamesDistinct = "";

    if (nodeNameAuto == null || nodeNameAuto == "" || nodeNameCustom == "") {
        if (nodeNameAuto == "" || nodeNameAuto == null)
            nodeNamesFinal = nodeNameCustom;
        else
            nodeNamesFinal = nodeNameAuto.toLocaleString();
    }
    else { nodeNamesFinal = nodeNameAuto + "," + nodeNameCustom; }

    if (nodeNamesFinal == null)
        nodeNamesFinal = "";

    let arr = $.unique(nodeNamesFinal.split(','));
    nodeNamesDistinct = arr.join(",");

    if ((assignToUserLength > 1 || assignToAllUserLength > 1) && (select1wp == 'singleUserTrue')) {
        if (assignToUserLength == null || assignToUserLength.length > 1 || nodeNamesDistinct == '' || nodeNamesDistinct == null || nodeNamesDistinct == 'undefined') {
            select1wp = '';
            $('#nodeWisewp').prop('checked', false);

            pwIsf.alert({ msg: "Check this only for node wise work order for single user. This will increase number of Work Order", type: 'info' });
        }
    }
}

function modifyWorkOrderWrkFlow(scopeID, subActId, version) {
    if (validationsWP()) {

        let periodicityDaily = null;
        let periodicityWeekly = null;
        let WO = new Object();
        WO.projectID = localStorage.getItem("views_project_id");
        WO.scopeID = scopeID;

        WO.wOPlanID = localStorage.getItem("woPlanID");
        WO.subActivityID = subActId;
        WO.wfVersion = version;
        WO.priority = $('.prioritywp').find('.active')[0].textContent.trim();
        if (dailywp == null || dailywp.length == 0)
            dailywp = null;
        else
            dailywp = dailywp.join();
        WO.periodicityDaily = dailywp;
        if (weeklywp == '' || weeklywp == null)
            weeklywp = null;
        else
            WO.periodicityWeekly = weeklywp;
        if (hourlywp == null || hourlywp.length == 0)
            hourlywp = null;
        WO.periodicityHourly = hourlywp;
        WO.flowchartDefId = $("#flowchartdefID").val();
        var timeDate = $("#Start_Date_WP").val() + " " + $("#start_time_WP").val();
        var startDateIST = GetConvertedDate_tz(timeDate);
        WO.startDate = startDateIST;
        var startTimeIST = GetConvertedTime_tz(timeDate);
        WO.startTime = startTimeIST;
        WO.endDate = $("#End_Date_wp").val();
        WO.wOName = $("#WO_nameWP").val();
        let slaHrs;
        if ($("#slaHrswp").val() == null || $("#slaHrswp").val() == '')
            slaHrs = '';
        else
            slaHrs = $("#slaHrswp").val();
        WO.selectUser = select1wp;
        WO.slaHrs = slaHrs;

        var signumIDArr = $("#select_assign_to_user_wp").val();

        if (allSignumIDArr.length != 0) {
            WO.lstSignumID = allSignumIDArr;
        }
        else if (signumIDArr != null) {
            WO.lstSignumID = signumIDArr;
        }
        else if (signumIDArr == null)
            WO.lstSignumID = [];
        else {
            WO.lstSignumID = [];
        }
        WO.lastModifiedBy = signumGlobal;
        WO.createdBy = signumGlobal;
        if ($("#execPlanTbl #execPlanIdwp").text() == '' || $("#execPlanTbl #execPlanIdwp").text() == null)
            WO.executionPlanId = 0;
        else
            WO.executionPlanId = parseInt($("#execPlanTbl #execPlanIdwp").text());

        let nodeNameAuto = $("#woc_select_nodeNamewp").select2("val");
        let nodeNameCustom = $('#nodeViewwp')[0].value;
        let nodeNamesFinal = ""; let nodeNamesDistinct = "";

        if (nodeNameAuto == null || nodeNameAuto === "" || nodeNameCustom === "") {
            if (nodeNameAuto === "" || nodeNameAuto == null) {
                nodeNamesFinal = nodeNameCustom;
            }
            else {
                nodeNamesFinal = nodeNameAuto.toLocaleString();
            }
        }
        else { nodeNamesFinal = nodeNameAuto + "," + nodeNameCustom; }

        if (nodeNamesFinal == null) {
            nodeNamesFinal = "";
        }
        let arr = $.unique(nodeNamesFinal.split(','));
        nodeNamesDistinct = arr.join(",");
        let nodeType;

        if ($('#nodeViewwp')[0].value !== "" && ($("#select_nodeTypewp").val() === '' || $("#select_nodeTypewp").val() == null || $("#select_nodeTypewp").val() === 'undefined')) {
            nodeType = $("#nodeDisplaywp #nodetypeInfowp").text();
        }
        else {
            nodeType = $("#select_nodeTypewp").val();
        }
        let listOfNodeJson = { "nodeNames": nodeNamesDistinct, "nodeType": nodeType }
        let listOfNode = [];
        listOfNode.push(listOfNodeJson);

        WO.listOfNode = listOfNode;
        if (flag == 2) {
            WO.workOrderPlanID = localStorage.getItem("woPlanID");
        }
        WO.comment = $('#commentsBox').val();
        WO.type = $('#WoPlanType').val();
        console.log(JSON.stringify(WO));

        if (flag == 2) {
            WO.woCount = parseInt($('#woCountForWOPlan').text());;
            $.isf.ajax({
                url: service_java_URL + "woManagement/updateEditWorkOrderPlan",
                context: this,
                crossdomain: true,
                processData: true,
                contentType: 'application/json',
                type: 'POST',
                data: JSON.stringify(WO),
                xhrFields: {
                    withCredentials: false
                },
                success: function (data) {

                    alert("DATA SUCCESSFULLY SAVED");
                    closeModalwp();
                    getWorkOrderDetails();
                    flag = 0;
                },
                error: function (xhr, status, statusText) {
                    alert("DATA CAN NOT BE SAVED");
                    console.log('An error occurred on : createWO' + xhr.error);
                }
            });
        }

        else if (flag == 3) {
            if (WO.lstSignumID.length == 0) {
                WO.woCount = $('#woCountWP').val();
            }
            $.isf.ajax({

                url: service_java_URL + "woManagement/createWorkOrderPlan",
                context: this,
                crossdomain: true,
                processData: true,
                contentType: 'application/json',
                type: 'POST',
                data: JSON.stringify(WO),
                xhrFields: {
                    withCredentials: false
                },
                success: function (data) {

                    alert("DATA SUCCESSFULLY SAVED");
                    closeModalwp();
                    flag = 0;
                    getWorkOrderDetails();
                },
                error: function (xhr, status, statusText) {
                    alert("DATA CAN NOT BE SAVED");
                    console.log('An error occurred on : createWO' + xhr.error);
                }
            });
        }
    }
    else {
        alert("Fields Not properly filled");
    }
}

function getPriorityWP() {
    $('#select_prioritywp')
        .find('option')
        .remove();
    $("#select_prioritywp").select2("val", "");
    $('#select_prioritywp').append('<option value="0"></option>');

    $.isf.ajax({

        url: service_java_URL + "woManagement/getPriority ",
        success: function (data) {

            $.each(data, function (i, d) {
                $('#select_prioritywp').append('<option value="' + d + '">' + d + '</option>');
            })

        },
        error: function (xhr, status, statusText) {
            console.log('An error occurred on getProjectName: ' + xhr.error);
        }
    })

}

/*-----------------On click of Mass Transfer WO View--------------------------*/
$(document).on("click", "#massTransferWOView", function () {
    $("#viewValue").val("WO View");
    var checkedCount = $(".checkBoxClassWOView:checked").length; var status = ''; var flag = true; var statusArr = [];
    for (var j = 0; j < checkedCount; j++) {
        statusArr.push($(".checkBoxClassWOView:checked").closest("tr").find(".statusWOView")[j].textContent);
        woID = $(".checkBoxClassWOView:checked").closest("tr").find(".woidWOView")[j].textContent;
        signumID = $(".checkBoxClassWOView:checked").closest("tr").find(".signumWOView")[j].textContent;

        if (isWoAnyStepRunning(woID, signumID)) {
            pwIsf.alert({ msg: "Work Order(" + woID + ") cannot be transferred while in progress ", type: 'error' });
            return;
        }
    }

    if (checkedCount > 0) {
        if (flag) {
            getAllSignumForTransferMass();
            $('#massTransferModalWO').modal('show');
        }
        else {

            pwIsf.alert({ msg: "Mass Transfer can only be done on Assigned and Reopened Status!" });
        }
    }
    else {
        pwIsf.alert({ msg: "Please Select Any Work Order" });
    }
});



/*-----------------On click of Mass Transfer--------------------------*/

$(document).on("click", "#massTransferWO", function () {
    $("#viewValue").val("WO Plan View");
    var checkedCount = $(".checkBoxClassWO:checked").length; var status = ''; var flag = true; var statusArr = [];
    for (var j = 0; j < checkedCount; j++) {
        statusArr.push($(".checkBoxClassWO:checked").closest("tr").find(".status")[j].textContent);
        woID = $(".checkBoxClassWO:checked").closest("tr").find(".woid")[j].textContent;
        signumID = $(".checkBoxClassWO:checked").closest("tr").find(".signum")[j].textContent;

        if (isWoAnyStepRunning(woID, signumID)) {
            pwIsf.alert({ msg: "Work Order(" + woID + ") cannot be transferred while in progress ", type: 'error' });
            return;
        }
    }



    if (checkedCount > 0) {
        if (flag) {
            getAllSignumForTransferMass();
            $('#massTransferModalWO').modal('show');
        }
        else {

            pwIsf.alert({ msg: "Mass Transfer can only be done on Assigned and Reopened Status!" });
        }

    }
    else {
        pwIsf.alert({ msg: "Please Select Any Work Order" });
    }


});

/*-----------------On Mass Transfer Submit (Params calculated)--------------------------*/

$(document).on("click", "#massTransferSubmitWO", function () {  
    if ($('#selectUserAllSignumTransferMass').val() == "Loading Signums...")
        receiver = '0';
    else
        receiver = $('#selectUserAllSignumTransferMass').val();
    startDateMassTransfer = $('#start_date_mtWO').val();

    endDate = $('#end_date_mtWO').val();
    massTransferWorkOrderWO(receiver, startDateMassTransfer);
});

/*-----------------On Mass Transfer Submit (API called)--------------------------*/

function massTransferWorkOrderWO(receiver, sd) {
    pwIsf.addLayer({ text: C_PLEASE_WAIT });
    var status = []; selectedWO = [];
    var today = formattedDate(new Date());
    
    if (signumGlobal.trim() == receiver.toLowerCase().trim()) {
        pwIsf.alert({ msg: "Work order cannot be transferred to yourself", type: "warning" });
        pwIsf.removeLayer();
    }
    else if (sd < today) {
        pwIsf.alert({ msg: "Please don't select date before today's date", type: "warning" });
        pwIsf.removeLayer();
    } 
    else {
        if ($("#viewValue").val() == "WO Plan View")
            var selectedworkorders = getValueUsingClassWO();
        else 
            var selectedworkorders = getWOsForMultipleDelete();           
        

        if (selectedworkorders != 0) {
            if (receiver == 0) {
                receiver = null;
            }
            
            else if (sd == "") {
                sd = null;
            }
           
            var transferArr = { "woID": selectedworkorders, "logedInSignum": signumGlobal, "receiverID": receiver, "startDate": sd, "senderID": signumGlobal };
            var request = $.isf.ajax({
                url: `${service_java_URL}woManagement/massUpdateWorkOrder`,
                method: "POST",
                contentType: C_CONTENT_TYPE_APPLICATION_JSON,
                data: JSON.stringify(transferArr),
                returnAjaxObj: true,
                dataType: "html"
            });
            request.success(function (msg) {
                pwIsf.removeLayer();
                if ($("#viewValue").val() == "WO Plan View")
                    getWorkOrderDetails();
                else {
                    createWOView();
                    $('html, body').animate({
                        scrollTop: $("div.WOViewScreen").offset().top
                    }, 100);
                }
                pwIsf.alert({ msg: "WO successfully transferred" });
                $('#massTransferModalWO').modal('hide');

            });

            request.fail(function (jqXHR, textStatus) {
                pwIsf.removeLayer();
                var err = JSON.parse(jqXHR.responseText).errorMessage;
                pwIsf.alert({ msg: err, type: "error" });
            });
        }
        else
            pwIsf.alert({ msg: "Please select at least one work order" });
    }
}


function getValueUsingClassWO() {
    /* declare an checkbox array */
    var chkArray = [];

    /* look for all checkboes that have a class 'chk' attached to it and check if it was checked */
    $(".checkBoxClassWO:checked").each(function () {
        parseInt(chkArray.push($(this).val()));
    });

    /* we join the array separated by the comma */
    var selected;
    selected = chkArray.join(',');

    /* check if there is selected checkboxes, by default the length is 1 as it contains one single comma */
    if (selected.length > 1) {

        return chkArray;
    } else {

        return 0;
    }
}

// To download DO Plan View Tables
function downloadDOPlanFile(thisObj) {
    var d = new Date(0);
    var startDateUniversal = formatted_date(d);
    var endDate = localStorage.getItem("wo_endDate");
    var status = localStorage.getItem("wo_status");
    downloadDOViewData({ thisObj: thisObj, projectid: localStorage.getItem("views_project_id"), startDate: startDateUniversal, endDate: endDate, status: status });
}

/*--------------Get Current Week Date Range (Default)-----------------------------*/

function getWeekDateRangeWO() {
    var today = new Date();
    var day = today.getDay();
    var diff = today.getDate() - day + (day == 0 ? -6 : 1); // 0 for sunday
    var week_start_tstmp = today.setDate(diff);
    var week_start = new Date(week_start_tstmp);
    var d = new Date(0);
    var startDate = formatted_date(d);
    var week_end = new Date(week_start_tstmp);  // first day of week 
    week_end = new Date(week_end.setDate(week_end.getDate() + 7));
    endDate = formatted_date(week_end);
}

/*--------------Date in YYYY-MM-DD Format-----------------------------*/

var formatted_date = function (date) {
    var m = ("0" + (date.getMonth() + 1)).slice(-2); // in javascript month start from 0.
    var d = ("0" + date.getDate()).slice(-2); // add leading zero 
    var y = date.getFullYear();
    return y + '-' + m + '-' + d;
}

/*---------------Get Work Order (Pagination)----------------------------*/

function getWorkOrderDetails(startDate, endDate, woStatus) {
    woStatusForWOView = woStatus;
    if ($.fn.dataTable.isDataTable("#table_wo_planning")) {
        oTableWOPlan.destroy();
      
    }  

    if (startDate == "" || endDate == "") {
        getWeekDateRangeWO();
        startDate = window.startDate;
        endDate = window.endDate;
    }
    else if (startDate == undefined || endDate == undefined) {
        if (window.durationText == undefined || window.durationText == "")
            window.durationText = "This Week";
        getDurationWO(window.durationText);
    }
    if (!woStatus == "All")
        woStatus = window.statusText;
    var d = new Date(0);
    var startDate = formatted_date(d);
    localStorage.setItem("wo_endDate", endDate);
   
    var urlSearch = "?projectID=" + projectID + "&startDate=" + startDate + "&endDate=" + endDate;
    if (ApiProxy == true) {
        urlSearch = "?projectID=" + projectID + encodeURIComponent("&startDate=" + startDate + "&endDate=" + endDate);
    }

    var serviceURL = "";
    if (woStatus == "" || woStatus == undefined)
        serviceURL = `${service_java_URL}woManagement/getWorkOrderPlans${urlSearch}`;
    else {
        localStorage.setItem("wo_status", woStatus);
        serviceURL = `${service_java_URL}woManagement/getWorkOrderPlans${urlSearch}&woStatus=${woStatus}`;
        if (ApiProxy == true) {
            serviceURL = `${service_java_URL}woManagement/getWorkOrderPlans${urlSearch}${encodeURIComponent(`&woStatus=${woStatus}`)}`;
        }
    }

    $('#my-tab-content').show();

    var recordsFiltered = null;

    oTableWOPlan = $('#table_wo_planning').on('page.dt', function () { $("#ckbCheckAllWO").prop('checked', false);}).DataTable({
        stateSave: true,
        "processing": true, //paging
        "serverSide": true, //paging
        "ajax": {
            "headers": commonHeadersforAllAjaxCall,
            "url": serviceURL,
            "type": "POST",
            "complete": function (data) {
                click = false;
                if (data && data.responseJSON && data.responseJSON.recordsTotal) {
                    recordsFiltered = data.responseJSON.recordsTotal;
				}
                $("#expandCollapseLink").removeClass("collapseButton");
                $("#expandCollapseLink").addClass("expandButton");
            },
            "data": function (d) {
               d.recordsTotal = recordsFiltered;
            }
        },
        
         order: [[4, "desc"]],        
        "lengthMenu": [5,10,15,20,25],
        "destroy": true,
		"retrieve": true,
        "searching": true,
        colReorder: true,
        dom: 'Bflrtip',
        buttons: [ ],

        "columns": [
            {
                "title": '<div style="display:flex;"><label title="Select All Deliverable Order Plans" class="containerClass"><input type="checkbox" title="Select All Deliverable Order Plans" value="wOID" id="ckbCheckAllWO"><span class="checkmark"></span></label> </div>',
                "orderable": false,
                "searchable": false,
                "defaultContent": "",
                "render": function (data, type, row, meta) {
                    detailsPlan[meta.row] = formatData(row, true);
                    return '<div style="display:flex;"><label class="containerClass" style="padding-left: 0px;"><input  class="checkBoxClassWOPlan" type="checkbox" value="' + row.wOPlanID + '"><span class="checkmark"></span></label> &emsp;<a style="margin-left:0px;margin-right:0px;padding-right: 20px;"  class="details-control expandButton"></a></div>'
                }
            },

            {
                //To add conditions
                "title": "Action",
                "targets": 'no-sort',
                "orderable": false,
                "searchable": false,
                "data": null,
                "defaultContent": "",
                "render": function (data, type, row, meta) {
                    return '<div style="display:flex;"><i style="cursor: pointer;display:none;" title="Edit" class="md md-edit"></i><a href="#" class="icon-delete first_delete" title="Delete">' + getIcon('delete') + '</a></div>';
                }
            },
            {
                "title": "Deliverable Name",
                "data": "deliverableName",
                "searchable": true
            },
            {
                "title": "Deliverable Unit",
                "data": "deliverableUnitName",
                "searchable": true
            },
            {
                "title": "DO Plan ID",
                "data": "wOPlanID",
                "searchable": true

            },
            {
                "title": "WO Name",
                "data": "wOName",
                "searchable": true,
                "visible": false
            },
            {
                "title": "SLA",
                "data": "slaHrs",
                "searchable": true,
                "visible": false
            },
            {
                "title": "Technology",
                "data": "activityDetails.0.technology",
                "searchable": true,
                "visible": false

            },
            
            {
                "title": "Start Date",
                "searchable": true,
                "data": "startDate",
                "visible": true,
                "render": function (data, type, row, meta) {
                    var startDate = new Date(row.startDate);
                    return GetConvertedDate_WOPlan(formatted_date(startDate));


                }

            },
            {
                "title": "End Date",
                "searchable": true,
                "data": "endDate",
                "visible": true,
                "render": function (data, type, row, meta) {
                    var endDate = new Date(row.endDate);
                    return GetConvertedDate_WOPlan(formatted_date(endDate));

                }

            },
            {
                "title": "Activity",
                "data": "subActivity.0.activity",
                "searchable": true,
                "visible": false

            },
            {
                "title": "Subactivity",
                "data": "subActivity.0.subActivity",
                "searchable": true,
                "visible": false

            },
            
            {
                "title": "Type",
                "data": "type",
                "searchable": true
            }

        ],
        initComplete: function () {
            $('#table_wo_planning tfoot th').each(function (i,value) {
                var title = $('#table_wo_planning thead th').eq($(this).index()).text();
                if (title != "Action" && title != " ")
                    $(this).html('<input type="text" class="form-control" style="font-size:12px;"  placeholder="Search ' + title + '" data-index="' + i + '" value="' + oTableWOPlan.column(i).search() +'"/>');
                else if (title == " ")
                    $(this).html('<a style="margin-left:0px;margin-right:0px;" id="expandCollapseLink" class="expandButton" title="Expand/Collapse All WOs" onclick="expandCollapseWO(this)"></a>');
                

                
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

   
    $('#table_wo_planning tfoot').insertAfter($('#table_wo_planning thead'));

    $('#table_wo_planning tbody').off('click').on('click', '.details-control', function () {
        var tr = $(this).closest('tr');
        var row = oTableWOPlan.row(tr);

        if (row.child.isShown()) {
            // This row is already open - close it
            row.child.hide();
            tr.removeClass('shown');
            $(this).removeClass('collapseButton');
            $(this).addClass('expandButton');
        }
        else {
            // Open this row
            row.child(formatData(row.data(), true)).show();
            tr.addClass('shown');
            $(this).removeClass('expandButton');
            $(this).addClass('collapseButton');
        }
    });

    $('#table_wo_planning tbody').on('click', 'a.first_delete', function (e) {
        e.preventDefault();
        var data = oTableWOPlan.row($(this).parents('tr')).data();
        deleteWorkOrderPlan(data.wOPlanID, data.lastModifiedBy);

    });

   

    $('#table_wo_planning tbody').on('click', 'i.md-edit', function (e) {
        e.preventDefault();
        var data = oTableWOPlan.row($(this).parents('tr')).data();

        if (validateDeleteEditWorkOrderPlan(data.listOfWorkOrder)) {

            getWorkOrderPlanDetails(data.wOPlanID, data.projectID, data.signumID);

            localStorage.setItem("projectID", data.projectID);
            localStorage.setItem("woPlanID", data.wOPlanID);

            localStorage.setItem("signumID", data.signumID);
            localStorage.setItem("lastModifiedBy", data.lastModifiedBy);
            localStorage.setItem("createdBy", data.createdBy);
            listOfNode = [{
                "nodeType": data.listOfNode.nodeType,
                "nodeNames": data.listOfNode.nodeNames
            }]

            localStorage.setItem("listOfNode", data.listOfNode);

            $('.nav a[href="#menu5"]').tab('show')
        } else {
            $('#edit_success').modal('show');
            $('#employee_change_status').html('The work order plan cannot be edited. There is a work order with status different from ASSIGNED.');
        }

    });

    //Checking all boxes
    $("#ckbCheckAllWO").click(function (e) {
        $(".checkBoxClassWOPlan").prop('checked', $(this).prop('checked')); 
    });

    $(".checkBoxClassWO").on('change', function (e) {
        if (!$(this).prop("checked")) {
            $("#ckbCheckAllWO").prop("checked", false);
        }
    });

    // Handle click on "Expand All" button
    $('#btn-show-all-children').on('click', function (e) {
        e.preventDefault();
        $('#btn-show-all-children').prop("disabled", true);
        $('#btn-hide-all-children').prop("disabled", false);
        // Enumerate all rows
        oTableWOPlan.rows().every(function () {
            // If row has details collapsed
            if (!this.child.isShown()) {
                // Open this row
                this.child(formatData(this.data())).show();
                $(this.node()).addClass('shown');
            }
        });
    });

    // Handle click on "Collapse All" button
    $('#btn-hide-all-children').on('click', function (e) {
        e.preventDefault();
        $('#btn-show-all-children').prop("disabled", false);
        $('#btn-hide-all-children').prop("disabled", true);
        // Enumerate all rows
        oTableWOPlan.rows().every(function () {
            // If row has details expanded
            if (this.child.isShown()) {
                // Collapse row details
                this.child.hide();
                $(this.node()).removeClass('shown');
            }
        });
    });


    var state = oTableWOPlan.state.loaded();
    if (state) {
        oTableWOPlan.columns().eq(0).each(function (colIdx) {
            var colSearch = state.columns[colIdx].search;

            if (colSearch.search) {
                $('input', oTableWOPlan.column(colIdx).footer()).val(colSearch.search);
            }
        });

        oTableWOPlan.draw(false);
    }




}
var woViewDataArray = [];

function getWOViewData(data) {
    woViewDataArray = [];
    $.each(data, function (i, d) {
        $.each(d.listOfWorkOrder, function (j, e) {
            let x = e.listOfNode;
            let nodes = '';
            for (var i = 0; i < x.length; i++) {
                nodes = x[i].nodeNames + ',' + nodes;
                nodes = nodes.replace(/,\s*$/, '');
            }
            if (nodes == null || nodes == "null")
                nodes = "";
            localStorage.setItem(d.wOPlanID + "_" + e.wOID, JSON.stringify(e.listOfNode));
            woViewDataArray.push(
                {
                    "wOPlanID": d.wOPlanID,
                    "wOName": e.wOName,
                    "WorkFlowName": e.wfVersionName,
                    "deliverableName": d.deliverableName,
                    "deliverableUnitName": d.deliverableUnitName,
                    "wOID": e.wOID,
                    "doid": e.doID,
                    "NodeNames": nodes,
                    "StartDate": e.plannedStartDate,
                    "EndDate": e.plannedEndDate,
                    "status": e.status,
                    "reason": e.reason,
                    "actualStartDate": e.actualStartDate,
                    "actualEndDate": e.actualEndDate,
                    "WoSignumID": e.signumID,
                    "active": e.active,
                    "projectID": d.projectID,
                    "subActivityID": d.subActivityID,
                    "priority": e.priority,
                    "listOfNode": e.listOfNode,
                    "lastModifiedBy": e.lastModifiedBy,
                    "createdBy": e.createdBy,
                    "parentWorkOrderID": e.parentWorkOrderID,
                    "lastModifiedDate": d.lastModifiedDate,
                    "slaHrs": d.slaHrs,
                    "technology": d.activityDetails[0].technology,
                    "activity": d.subActivity[0].activity,
                    "subActivity": d.subActivity[0].subActivity,
                    "market": e.listOfNode[0].market,
                    "wfid": e.wfid
                })
        })
    })
    return woViewDataArray;
}

function createWOView(startDate, endDate, woStatus) {
    woStatusForWOView = woStatus;
    if ($.fn.dataTable.isDataTable("#table_wo")) {
        oTableWOPlanWO.destroy();
    }

    if (startDate == "" || endDate == "") {
        getWeekDateRangeWO();
        startDate = window.startDate;
        endDate = window.endDate;
    }
    else if (startDate == undefined || endDate == undefined) {
        if (window.durationText == undefined || window.durationText == "")
            window.durationText = "This Week";
        getDurationWOView(window.durationText);
    }
    if (!woStatus == "All")
        woStatus = window.statusText;
    var d = new Date(0);
    var startDate = formatted_date(d);
    var urlSearch = "?projectID=" + projectID + "&startDate=" + startDate + "&endDate=" + endDate;
    if (ApiProxy == true) {
        urlSearch = "?projectID=" + projectID + encodeURIComponent("&startDate=" + startDate + "&endDate=" + endDate);
    }

    var serviceURL = "";
    if (woStatus == "" || woStatus == undefined)
        serviceURL = `${service_java_URL}woManagement/getWorkOrderPlans${urlSearch}`;
    else {
        serviceURL = `${service_java_URL}woManagement/getWorkOrderPlans${urlSearch}&woStatus=${woStatus}`;
        if (ApiProxy == true) {
            serviceURL = `${service_java_URL}woManagement/getWorkOrderPlans${urlSearch}${encodeURIComponent(`&woStatus=${woStatus}`)}`;
        }
    }
    $('#my-tab-content-WOView').show();
    let recordsFiltered = null;
    let woViewDataArray = [];
    oTableWOPlanWO = $('#table_wo').on('page.dt', function () { $("#ckbCheckAllWOView").prop('checked', false); }).DataTable({
        stateSave: true,
        "searching": true,
        "responsive": true,
        "processing": true, //paging
        "serverSide": true, //paging
        "ajax": {
            "headers": commonHeadersforAllAjaxCall,
            "url": serviceURL,
            "dataSrc": function (data) {
                let dataView;
                dataView = data.data;
                dataView = getWOViewData(dataView);
                return dataView;
            },
            "type": "POST",
            "complete": function (data) {
                click = false;
                if (data && data.responseJSON && data.responseJSON.recordsTotal) {
                    recordsFiltered = data.responseJSON.recordsTotal;
				}
            }
        },
        order: [[4, "desc"]],
        "destroy": true,
        "lengthMenu": [[5, 10, 15, -1], [5, 10, 15 , 'All']],
        "retrieve": true,
        "language": {
            "lengthMenu": "Showing WOs of _MENU_ WO Plans",
        },
        "pageLength": 5,
        info: false,
		colReorder: true,
        dom: 'Blfrtip',
        buttons: [
            'excelHtml5', 'colvis'
        ],
        "columns": [
            {
                "title": '<label title="Select All Work Orders" class="container2"><input type="checkbox" title="Select All Work Orders" value="wOID" id="ckbCheckAllWOView"><span class="checkmark"></span></label>',
                "orderable": false,
                "searchable": false,
                "defaultContent": "",
                "render": function (data, type, row, meta) {
                    return '<div style="text-align:center"><label class="container2" style="margin-left:18px;"><input name="' + row.status + '" class="checkBoxClassWOView" type="checkbox" value="' + row.wOID + '"><span class="checkmark"></span></label></div>'
                }
            },
            {
                //To add conditions
                "title": "Action",
                "targets": 'no-sort',
                "orderable": false,
                "searchable": false,
                "data": null,
                "defaultContent": "",
                "className":"actionCss",
                "render": function (data, type, row, meta) {
                    let WOViewicon = '';
                    WOViewicon += '<div style="display: flex;">';
                    if (row.status == "REOPENED") {
                        WOViewicon += //change for Merging tabs
                            ' <i class="md md-mode-edit" title="Edit" style="display:none;cursor: pointer;" onclick="openModalEdit(' + projectID + ',' + row.wOID + ',' + row.wOPlanID + ',' + row.actualStartDate + ',' + row.actualEndDate + ',' + row.StartDate + ',' + row.EndDate + ',\'' + row.WoSignumID + '\',\'' + row.priority + '\',\'' + row.listOfNode[0].nodeType + '\',\'' + row.listOfNode[0].nodeNames + '\'' + ')"></i>' +
                        '<a href="#"  class="icon-delete" title="Delete WO" onclick="deleteWO(' + row.wOID + ',' + row.lastModifiedBy + ', \'' + row.status + '\', \'WO View\')">' + getIcon('delete') + '</a>' +
                        '<a href="#"  class="icon-add lsp" title="Transfer" onclick="modalTransfer(' + row.wOID + ',\'' + row.createdBy + '\',\'' + row.WoSignumID + '\', \'WO View\'),getAllStepNameForTransferDOPlan(' + row.wOID + ')"><i class="fa fa-exchange"></i> </a>' +
                        '<a href="#"  class="icon-edit lsp" title="Priority" onclick="modalPriority(' + row.wOID + ')"><i class="fa fa-pinterest-p"></i></a>' + '<a href="#" title="Edit WO"  class="icon-edit lsp" onclick="openModalEditDetails(' + row.wOID + ',\'' + row.status + '\',' + row.wOPlanID + ',' + row.doid + ',\'' + row.createdBy + '\')"><i class="fa fa-pencil-square"></i></a>';

                    }
                    if (row.status == "ONHOLD" || row.status == "INPROGRESS") {
                        WOViewicon += //change for Merging tabs
                            '<i class="md md-mode-edit" title="Edit" style="display:none;cursor: pointer;" onclick="openModalEdit(' + projectID + ',' + row.wOID + ',' + row.wOPlanID + ',' + row.actualStartDate + ',' + row.actualEndDate + ',' + row.StartDate + ',' + row.EndDate + ',\'' + row.WoSignumID + '\',\'' + row.priority + '\',\'' + row.listOfNode[0].nodeType + '\',\'' + row.listOfNode[0].nodeNames + '\'' + ')"></i>' +
                        '<a href="#"  class="icon-add lsp" title="Transfer" onclick="modalTransfer(' + row.wOID + ',\'' + row.createdBy + '\',\'' + row.WoSignumID + '\', \'WO View\'),getAllStepNameForTransferDOPlan(' + row.wOID + ')"><i class="fa fa-exchange"></i> </a>' +
                        '<a href="#"  class="icon-add lsp" title="Priority" onclick="modalPriority(' + row.wOID + ')"><i class="fa fa-pinterest-p"></i> </a>' + '<a href="#" title="Edit WO"  class="icon-edit lsp" onclick="openModalEditDetails(' + row.wOID + ',\'' + row.status + '\',' + row.wOPlanID + ',' + row.doid + ',\'' + row.createdBy +'\')"><i class="fa fa-pencil-square"></i></a>';
                    }
                    if (row.status == "DEFERRED" && row.parentWorkOrderID != -1) {
                        WOViewicon += '<a name="WO View" href="#" class="icon-edit lsp" id="reopenIcon' + row.wOID + '" title="Reopen WO" onclick="reopenDeferredWO(' + row.wOID + ')">' + getIcon('reopen') + '</a>';
                        WOViewicon += '<a href="#"  class="icon-delete lsp" id="deleteIcon' + row.wOID + '" title="Close WO" onclick="closeDeferredWO(' + row.wOID + ')">' + getIcon('delete') + '</a>';
                        WOViewicon += '<a href="#"  class="icon-add lsp" id="reinstateIcon' + row.wOID + '" title="Reinstate WO" onclick="reinstateDeferredWO(' + row.wOID + ')">' + getIcon('reinstate') + '</a>' +
                            '<a href="#"  class="icon-add lsp" title="Priority" onclick="modalPriority(' + row.wOID + ')"><i class="fa fa-pinterest-p"></i> </a>' + '<a href="#" title="Edit WO"  class="icon-edit lsp" onclick="openModalEditDetails(' + row.wOID + ',\'' + row.status + '\',' + row.wOPlanID + ',' + row.doid + ',\'' + row.createdBy +'\')"><i class="fa fa-pencil-square"></i></a>';
                    }
                    if (row.status == "ASSIGNED") {
                        WOViewicon += //change for Merging tabs
                            '<a href="javascript:void(0)"  class="icon-delete" title="Delete WO" onclick="deleteWO(' + row.wOID + ',' + row.lastModifiedBy + ', \'' + row.status + '\', \'WO View\')">' + getIcon('delete') + '</a>' +
                        '<a href="#"  class="icon-add lsp" title="Transfer" onclick="modalTransfer(' + row.wOID + ',\'' + row.createdBy + '\',\'' + row.WoSignumID + '\', \'WO View\'),getAllStepNameForTransferDOPlan(' + row.wOID + ')"><i class="fa fa-exchange"></i> </a>' +
                        '<a href="#"  class="icon-add lsp" title="Priority" onclick="modalPriority(' + row.wOID + ')"><i class="fa fa-pinterest-p"></i> </a>' + '<a href="#" title="Edit WO"  class="icon-edit lsp" onclick="openModalEditDetails(' + row.wOID + ',\'' + row.status + '\',' + row.wOPlanID + ',' + row.doid + ',\'' + row.createdBy +'\')"><i class="fa fa-pencil-square"></i></a>';
                        WOViewicon += '<a href="#" name="WO View" class="icon-edit lsp" title="EDIT WF version" onclick="openModalEditVersion(' + row.wfid + ',' + projectID + ',' + row.wOID + ',' + row.subActivityID + ',\'wo\')">' + getIcon('edit') + '</a>';
                    }
                    WOViewicon += '<div class="dropdown" style="margin-top: 5px;"><a title="Input URLs" href="#" class="icon-view lsp" data-toggle="modal" data-target="#OpenInputFilesModal" onclick="getInputFilesWO(' + row.wOID + '); getLocalGlobalURLs(\'OpenInputFilesModal\')"><i class="fa fa-info"></i></a><ul id="inputFilesWO" class="dropdown-menu"></ul></div></div>';
                    var editClicked = true;
                    return WOViewicon;
                }
            },
            {
                "title": "Deliverable Name",
                "data": "deliverableName",
                "searchable": true
            },
            {
                "title": "Deliverable Unit",
                "data": "deliverableUnitName",
                "searchable": true
            },
            {
                "title": "DOID",
                "data": "doid",
                "searchable": true
            },
			{
                "title": "WOID",
                "data": "wOID",
                "searchable": true,
                "className": 'woidWOView'
            },
            {
                "title": "Network Element Name/ID",
                "data": "NodeNames",
                "searchable": true,
                "render": function (data, type, row, meta) {
                    var nodesView;
                    if (row.NodeNames.length < 40) {
                        nodesView = `<span class="nodes" >${row.NodeNames}<i data-toggle="tooltip" style="cursor:pointer" title="Network Element Details"
                        class="fa fa-list" onclick="modalNodeWO(${row.wOPlanID},${row.wOID})"></i></span>`;
                    }
                    else {
                        nodesView = `<span class="nodes">${row.NodeNames.substr(0, 11)}<a id="showPara${row.wOID}" style="cursor:pointer"
                        onclick="showParaWO(${row.wOID})">.....</a><p id="paraShowhide${row.wOID}" style="display:none">
                        ${row.NodeNames.substr(11, row.NodeNames.length)}</p><i data-toggle="tooltip" id="collapseParagraph${row.wOID}"
                        title="collapse" class="fa fa-chevron-up" style="display:none;cursor:pointer" onclick="collapseParaWO(${row.wOID})"></i>
                        &nbsp;&nbsp;<i data-toggle="tooltip" id="showList${row.wOID}"  title="Network Element Details" class="fa fa-list"
                        style="display:none;cursor:pointer" onclick="modalNodeWO(${row.wOPlanID},${row.wOID})"></i></span>`;
                    }
                    return nodesView;
                }
            },
            {
                "title": "Market",
                "data": "market",
                "searchable": true
            },
            {
                "title": "WO Name",
                "data": "wOName",
                "searchable": true
            },
            {
                "title": "WO AssignedTo",
                "data": "WoSignumID",
                "searchable": true,
                "className": 'signumWOView'
            },
            {
                "title": "Work Flow Version Name",
                "data": "WorkFlowName",
                "searchable": true
            },
            {
                "title": "Priority Last modified on",
                "data": "lastModifiedDate",
                "searchable": true,
                "render": function (data, type, row, meta) {
                    var priorityLastModifiedDate;
                    if (row.lastModifiedDate == null)
                        priorityLastModifiedDate = "";
                    else {
                        priorityLastModifiedDate = new Date(row.lastModifiedDate);
                        priorityLastModifiedDate = ConvertDateTime_tz(priorityLastModifiedDate);
                    }
                    return priorityLastModifiedDate;
                }
            },
            {
                "title": "Created By",
                "data": "createdBy",
                "searchable": true
            },
            {
                "title": "Priority",
                "data": "priority",
                "searchable": true
            },
            {
                "title": "SLA",
                "data": "slaHrs",
                "searchable": true
            },
            {
                "title": "Technology",
                "data": "technology",
                "searchable": true
            },
            {
                "title": "Planned Start Date",
                "searchable": true,
                "data": "StartDate",
                "render": function (data, type, row, meta) {
                    var StartDate;
                    if (row.StartDate == null)
                        StartDate = "";
                    else {
                        StartDate = new Date(row.StartDate);
                        StartDate = ConvertDateTime_tz(StartDate);
                    }
                    return StartDate;
                }
            },
            {
                "title": "Planned End Date",
                "searchable": true,
                "data": "EndDate",
                "render": function (data, type, row, meta) {
                    var EndDate;
                    if (row.EndDate == null)
                        EndDate = "";
                    else {
                        EndDate = new Date(row.EndDate);
                        EndDate = ConvertDateTime_tz(EndDate);
                    }
                    return EndDate;
                }
            },
            {
                "title": "Activity",
                "data": "activity",
                "searchable": true
            },
            {
                "title": "Subactivity",
                "data": "subActivity",
                "searchable": true
            },
            {
                "title": "StartedOn",
                "data": "actualStartDate",
                "searchable": true,
                "render": function (data, type, row, meta) {
                    var actualStartDate;
                    if (row.actualStartDate == null)
                        actualStartDate = "";
                    else {
                        actualStartDate = new Date(row.actualStartDate);
                        actualStartDate = ConvertDateTime_tz(actualStartDate);
                    }
                    return actualStartDate;
                }
            },
            {
                "title": "Status",
                "data": "status",
                "searchable": true,
                "className": 'statusWOView'
            },
            {
                "title": "WO Plan ID",
                "data": "wOPlanID",
                "searchable": true
            },
        ],
        initComplete: function () {
            $('#table_wo tfoot th').each(function (i,value) {
                var title = $('#table_wo thead th').eq($(this).index()).text();
                if (title != "Action" && title != "")
                    $(this).html('<input type="text" class="form-control" style="font-size:12px;"  placeholder="Search ' + title + '" data-index="' + i + '" value="' + oTableWOPlanWO.column(i).search() +'"/>');
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

    $('#table_wo tfoot').insertAfter($('#table_wo thead'));

    $('#table_wo tbody').on('click', 'a.first_delete', function (e) {
        e.preventDefault();
        var data = oTableWOPlanWO.row($(this).parents('tr')).data();
        deleteWorkOrderPlan(data.wOPlanID, data.lastModifiedBy);
    });


    $('#table_wo tbody').on('click', 'i.md-edit', function (e) {
        e.preventDefault();
        var data = oTableWOPlanWO.row($(this).parents('tr')).data();
        if (validateDeleteEditWorkOrderPlan(data.listOfWorkOrder)) {
            getWorkOrderPlanDetails(data.wOPlanID, data.projectID, data.signumID);
            localStorage.setItem("projectID", data.projectID);
            localStorage.setItem("woPlanID", data.wOPlanID);
            localStorage.setItem("signumID", data.signumID);
            localStorage.setItem("lastModifiedBy", data.lastModifiedBy);
            localStorage.setItem("createdBy", data.createdBy);
            listOfNode = [{
                "nodeType": data.listOfNode.nodeType,
                "nodeNames": data.listOfNode.nodeNames
            }]
            localStorage.setItem("listOfNode", data.listOfNode);
            $('.nav a[href="#menu5"]').tab('show')
        } else {
            $('#edit_success').modal('show');
            $('#employee_change_status').html('The work order plan cannot be edited. There is a work order with status different from ASSIGNED.');
        }
    });

    //Checking all boxes
    $("#ckbCheckAllWOView").click(function (e) {
        $(".checkBoxClassWOView").prop('checked', $(this).prop('checked'));
    });
    $(".checkBoxClassWOView").on('change', function (e) {
        if (!$(this).prop("checked")) {
            $("#ckbCheckAllWOView").prop("checked", false);
        }
    });
    var state = oTableWOPlanWO.state.loaded();
    if (state) {
        oTableWOPlanWO.columns().eq(0).each(function (colIdx) {
            var colSearch = state.columns[colIdx].search;
            if (colSearch.search) {
                $('input', oTableWOPlanWO.column(colIdx).footer()).val(colSearch.search);
            }
        });
        oTableWOPlanWO.draw(false);
    }
}

function modalPriority(woid) {
    $("#modalPriority").modal('show');
    $("#modal_woid").val(woid);
}

function getElementNamesObj() {
    var elementIdPrefix = "workOrderViewIdPrefix_";
    var controlNames = ['project_name', 'estimated_effort', 'wo_name', 'priority', 'start_time', 'start_date', 'wo_id', 'project_id', 'assigned_to',
        'domain-subdomain', 'technology', 'service-area-sub-service-area', 'activity-subactivity',
        'market', 'vendor', 'sitetype-node-type', 'node-site-name', 'new-node-site', 'node_master_select', 'hiddenDateTime', 'hidden_status','full_wo_name'
    ];
    var controlObj = {};

    for (var i = 0; i < controlNames.length; i++) {
        controlObj[controlNames[i]] = $('#' + elementIdPrefix + controlNames[i]);
    }

    return controlObj;
}

function getValuesFromApiResponse(data) {
    var retElementsArrVal = [];
    var n = getPosition(data.responseData[0].wOName, "_", 1);
    retElementsArrVal['project_name'] = data.responseData[0].project;
    retElementsArrVal['project_id'] = data.responseData[0].projectID;
    retElementsArrVal['estimated_effort'] = data.responseData[0].effort;
    retElementsArrVal['wo_name'] = data.responseData[0].wOName.substring(n);
    retElementsArrVal['full_wo_name'] = data.responseData[0].wOName;
    retElementsArrVal['hidden_status'] = data.responseData[0].status;
    retElementsArrVal['priority'] = data.responseData[0].priority;
    retElementsArrVal['start_time'] = data.responseData[0].startTime;
    retElementsArrVal['start_date'] = data.responseData[0].startDate;
    retElementsArrVal['wo_id'] = data.responseData[0].wOID;
    retElementsArrVal['assigned_to'] = data.responseData[0].signumID;
    retElementsArrVal['domain-subdomain'] = data.responseData[0].domain;
    retElementsArrVal['technology'] = data.responseData[0].technology;
    retElementsArrVal['service-area-sub-service-area'] = data.responseData[0].serviceArea;
    retElementsArrVal['activity-subactivity'] = data.responseData[0].activity;
    retElementsArrVal['vendor'] = data.responseData[0].vendor;
    retElementsArrVal['sitetype-node-type'] = data.responseData[0].listOfNode[0].nodeType;
    retElementsArrVal['node-site-name'] = data.responseData[0].listOfNode;
    retElementsArrVal['new-node-site'] = 'Missing';
    console.log('retElementsArrVal');
    console.log(retElementsArrVal);
    $('#woNameBefore').val(data.responseData[0].woNameAlias);
    $('#woNameAfter').text(data.responseData[0].wOName.substring(n));
    return retElementsArrVal;
}

function getPosition(string, subString, index) {
    return string.split(subString, index).join(subString).length;
}

// to append autocomplete on WO Edit Details Modal
function getAllSignumForEditWODetails() {
    $("#workOrderViewIdPrefix_assigned_to").autocomplete({
        appendTo: "#editWODetails",
        source: function (request, response) {
            $.isf.ajax({
                url: `${service_java_URL}activityMaster/getEmployeesByFilter`,
                type: "POST",
                data: {
                    term: request.term
                },
                success: function (data) {
                    $("#workOrderViewIdPrefix_assigned_to").autocomplete().addClass("ui-autocomplete-loading");
                    var result = [];
                    if (data.length == 0) {
                        pwIsf.alert({ msg: 'Please enter a valid Signum', type: 'warning' });
                        $('#workOrderViewIdPrefix_assigned_to').val("");
                        $('#workOrderViewIdPrefix_assigned_to').focus();
                        response(result);
                    }
                    else {
                        $.each(data, function (i, d) {
                            result.push({
                                "label": d.signum + "/" + d.employeeName,
                                "value": d.signum
                            });
                        })
                        response(result);
                        $("#workOrderViewIdPrefix_assigned_to").autocomplete().removeClass("ui-autocomplete-loading");
                    }
                }
            });
        },
        change: function (event, ui) {
            if (ui.item === null) {
                $(this).val('');
                $('#workOrderViewIdPrefix_assigned_to').val('');
            }
        },
        minLength: 3
    });
    $("#workOrderViewIdPrefix_assigned_to").autocomplete("widget").addClass("fixedHeight");
}

// to open the commom WO Edit Details Modal
function openModalEditDetails(workOrderId, status, woplanid, doid, createdBy) {
    hideNEAddEdit();
     getDODetails(doid, workOrderId);
     $('.nodeEditCol').hide();
     $('.nodeDetailCol').show();
     modalNodeWOPopup(woplanid, workOrderId);
     $('#workOrderViewIdPrefix_doid').val(doid);
     $('#WODetailPanel').addClass('collapse');
     $('#activityPanel').addClass('collapse');
     $('#nodePanel').addClass('collapse');
     $('#nodePanel').removeClass('in');
     $('#nodeEditPanel').hide();
     $('#WODetailPanel').removeClass('in');
     $('#activityPanel').removeClass('in');
     $('#woNameBefore').prop('disabled', true);
     $('#btn_update_project').attr("disabled", "disabled");
     $('#workOrderViewIdPrefix_start_date').attr("disabled", "disabled");
     $('#workOrderViewIdPrefix_start_time').attr("disabled", "disabled");
     $('#workOrderViewIdPrefix_priority').attr("disabled", "disabled");
     $('#workOrderViewIdPrefix_estimated_effort').attr("disabled", "disabled");
     $('#workOrderViewIdPrefix_assigned_to').attr("disabled", "disabled");
     $('#workOrderViewIdPrefix_wo_name').attr("disabled", "disabled");
    $('#wODetailHeader').find('p').remove().end();
     $('#wODetailHeader').append('<p> WO Details - ' + workOrderId + '</p>');
     $('#DODetailHeader').find('p').remove().end();
     $('#DODetailHeader').append('<p> DO Details - ' + doid + '</p>');
    var editFlag = true;
    var getEditFlag = $(this).data('edit-work-order-flag');
    if (typeof $(this).data('edit-work-order-flag') !== 'undefined' || $(this).data('edit-work-order-flag') == false) {
        editFlag = false;
    }
    pwIsf.addLayer({ text: C_PLEASE_WAIT });
    $.isf.ajax({
        url: `${service_java_URL}woExecution/getCompleteWoDetails/${workOrderId}`,
        success: function (data) {
            pwIsf.removeLayer();
            $('#all_nodes_master').hide();
            if (data.isValidationFailed == false) {
                if (data.responseData[0].externalGroup == "ERISITE") {
                    $("#woNameDiv").css("display", "none");
                    $("#woNameDivErisite").css("display", "block");
                    $("#workOrderViewIdPrefix_full_wo_name").attr("title", data.responseData[0].wOName);
                }
                else {
                    $("#woNameDiv").css("display", "block");
                    $("#woNameDivErisite").css("display", "none");
                }
                var formElements = getElementNamesObj();
                var valuesFromApiResponse = getValuesFromApiResponse(data);
                // Finaly - Set values to elements
                setValuesToElements(formElements, valuesFromApiResponse);
                setValuesToNEPartialView(data.responseData, 'edit');
                getDefaultValuesForNEAndCount('edit', data.responseData);
                //Hide Save button on view order form
                if (((createdBy == "AMERICAS1") || (createdBy == "EMEA1") || (createdBy == "APAC1")) && ((status.toLowerCase() == "closed") || (status == "DEFERRED") || (status == "REJECTED") || (status == "ONHOLD") || (status == "INPROGRESS") || (status == "PLANNED") || (status == "COMPLETED"))) {
                    $(".projectDetailsIcon").hide();
                    $("#btn_update_project").hide();
                }
                else if (((createdBy == "AMERICAS1") || (createdBy == "EMEA1") || (createdBy == "APAC1")) && ((status == "ASSIGNED") || (status == "REOPENED"))) {
                    $(".projectDetailsIcon").show();
                    $("#btn_update_project").show();
                }
                else if (((createdBy != "AMERICAS1") || (createdBy != "EMEA1") || (createdBy != "APAC1")) && ((status == "CLOSED") || (status == "DEFERRED") || (status == "REJECTED"))) {
                    $('#btn_update_nodes').attr("disabled", true);
                    $("#edit_node_details").hide();
                    $(".projectDetailsIcon").show();
                    $("#btn_update_project").show();
                }
                else {
                    $(".projectDetailsIcon").show();
                    $("#btn_update_project").show();
                    $("#edit_node_details").show();
                }
                $('#projectIdWarning').val(data.responseData[0].projectID);
                $('#domainWarning').val(data.responseData[0].domainID);
                $('#technologyWarning').val(data.responseData[0].technologyID);
            }
            else {
                pwIsf.alert({ msg: data.formErrors[0], type: 'error' });
            }
            $("#editWODetails").modal('show');
        },
        error: function (xhr, status, statusText) {
            pwIsf.removeLayer();
            $("#editWODetails").modal('hide');
            alert("Failed to Fetch Data");
        }
    });
}

// to make WO Details Updatable
function getWODetails() {
    pwIsf.addLayer({ text: "Please wait ..." });
    $('#WODetailPanel').addClass('in');
    $('#WODetailPanel').removeClass('collapse');
    var woStatus = $('#workOrderViewIdPrefix_hidden_status').val();
    if (woStatus == "ASSIGNED" || woStatus == "REOPENED") {
        $('#btn_update_project').attr("disabled", false);
        $('#woNameBefore').prop('disabled', false);
        $('#workOrderViewIdPrefix_start_date').attr("disabled", false);
        $('#workOrderViewIdPrefix_start_time').attr("disabled", false);
        $('#workOrderViewIdPrefix_estimated_effort').attr("disabled", false);
        $('#workOrderViewIdPrefix_assigned_to').attr("disabled", false);
        $('#workOrderViewIdPrefix_wo_name').attr("disabled", false);
        let selectedPriority = $('#workOrderViewIdPrefix_priority option:selected').val();
        $('#workOrderViewIdPrefix_priority')
            .find('option')
            .remove();
        $('#workOrderViewIdPrefix_start_date').datepicker({ minDate: 0 });
        $('#workOrderViewIdPrefix_priority').append('<option value="0" disabled>Select Priority</option>');
        $('#workOrderViewIdPrefix_priority').append('<option value="Live">Live</option>');
        $('#workOrderViewIdPrefix_priority').append('<option value="Normal">Normal</option>');
        $('#workOrderViewIdPrefix_priority').append('<option value="Critical">Critical</option>');
        $('#workOrderViewIdPrefix_priority').append('<option value="High">High</option>');
        $('#workOrderViewIdPrefix_priority').append('<option value="Low">Low</option>');
        $('#workOrderViewIdPrefix_priority option[value=' + selectedPriority + ']').attr('selected', 'selected');
        getAllSignumForEditWODetails();
    }
    else if (woStatus == "INPROGRESS" || woStatus == "ONHOLD" || woStatus == "DEFERRED") {
        $('#woNameBefore').prop('disabled', false);
        $('#workOrderViewIdPrefix_wo_name').attr("disabled", false);
        $('#btn_update_project').attr("disabled", false);
    }
    pwIsf.removeLayer();
}

function updateWODetails() {
    pwIsf.addLayer({ text: C_PLEASE_WAIT });
    let woid = $('#workOrderViewIdPrefix_wo_id').val();
    let woName = $('#woNameBefore').val();
    let start_date = $('#workOrderViewIdPrefix_start_date').val();
    let start = start_date.split('-');
    let day = start[2];
    let month = start[1];
    let year = start[0];
    let startDate = year + '-' + month + '-' + day;
    let start_time = $('#workOrderViewIdPrefix_start_time').val();
    let priority = $('#workOrderViewIdPrefix_priority option:selected').val();
    let signumWOEditDetails = $('#workOrderViewIdPrefix_assigned_to').val();
    if (woName == '' || priority == 0) {
        pwIsf.alert({ msg: 'Please fill all the fields to update the Work Order!', type: 'warning' });
        pwIsf.removeLayer();
    }
    else {
        let ewodetail = new FormData();
        ewodetail.wOID = woid;
        ewodetail.signumID = signumWOEditDetails;
        ewodetail.wOName = woName;
        ewodetail.priority = priority;
        ewodetail.startDate = startDate;
        ewodetail.startTime = start_time + ":00";
        ewodetail.lastModifiedBy = signumGlobal;
        $.isf.ajax({
            url: `${service_java_URL}woManagement/saveEditedWorkOrderDetails`,
            context: this,
            crossdomain: true,
            processData: true,
            contentType: C_CONTENT_TYPE_APPLICATION_JSON,
            type: 'POST',
            data: JSON.stringify(ewodetail),
            xhrFields: {
                withCredentials: false
            },
            success: function (data) {
                pwIsf.removeLayer();
                pwIsf.alert({ msg: "WOID:" + woid + " updated successfully", type: "success" });
                $("#editWODetails").modal('hide');
            },
            error: function (xhr, status) {
                pwIsf.removeLayer();
                pwIsf.alert({ msg: "WOID:" + woid + " could not be updated", type: "error" });
            },
            complete: function (xhr, status) {
                getWorkOrdersWOPlan();
            }
        });
    }
}

function getDateFormat(date) {
    var d = new Date(date),
        month = '' + (d.getMonth() + 1),
        day = '' + d.getDate(),
        year = d.getFullYear();
    if (month.length < 2)
        month = '0' + month;
    if (day.length < 2)
        day = '0' + day;
    var date = new Date();
    date.toLocaleDateString();
    return [day, month, year].join('-');
}

function formattedDate(date) {
    var d = new Date(date),
        month = '' + (d.getMonth() + 1),
        day = '' + d.getDate(),
        year = d.getFullYear();
    if (month.length < 2)
        month = '0' + month;
    if (day.length < 2)
        day = '0' + day;
    var date = new Date();
    date.toLocaleDateString();
    return [year, month, day].join('-');
}

function setValuesToElements(elementsObj, valuesArr) {
    valuesArr['start_date'] = GetConvertedDate(new Date(valuesArr['start_date']).toDateString() + " " + valuesArr['start_time']);
    valuesArr['start_time'] = GetConvertedTime(new Date(valuesArr['start_date']).toDateString() + " " + valuesArr['start_time']);
    $('#workOrderViewIdPrefix_hiddenDateTime').val(valuesArr['start_date'] + " " + valuesArr['start_time']);
    elementsObj['project_name'].val(valuesArr['project_name']);
    elementsObj['estimated_effort'].val(valuesArr['estimated_effort']);
    elementsObj['wo_name'].val(valuesArr['wo_name']);
    elementsObj['wo_id'].val(valuesArr['wo_id']);
    elementsObj['project_id'].val(valuesArr['project_id']);
    elementsObj['assigned_to'].val(valuesArr['assigned_to']);
    elementsObj['hidden_status'].val(valuesArr['hidden_status']);
    elementsObj['full_wo_name'].val(valuesArr['full_wo_name']);
    elementsObj['priority'].html('');
    elementsObj['priority'].append($('<option>', {
        value: valuesArr['priority'],
        text: valuesArr['priority']
    }));
    elementsObj['priority'].val(valuesArr['priority']);
    elementsObj['start_time'].val(valuesArr['start_time']);
    elementsObj['start_date'].val(valuesArr['start_date']);
    elementsObj['domain-subdomain'].val(valuesArr['domain-subdomain']);
    elementsObj['technology'].val(valuesArr['technology']);
    elementsObj['service-area-sub-service-area'].val(valuesArr['service-area-sub-service-area']);
    elementsObj['activity-subactivity'].val(valuesArr['activity-subactivity']);
    elementsObj['node-site-name'].html('');
    elementsObj['node_master_select'].html('');
    console.log(valuesArr['node-site-name']);
    let prePopNodeName = '';
    for (var i = 0; i < valuesArr['node-site-name'].length; i++) {
        prePopNodeName = prePopNodeName + ',' + valuesArr['node-site-name'][i].nodeNames;
    }
    prePopNodeName = prePopNodeName.replace(/(^,)|(,$)/g, "");
    if (prePopNodeName == null || prePopNodeName == "null") {
        prePopNodeName = "";
    }
    elementsObj['new-node-site'].val(prePopNodeName);
    $('#workOrderViewIdPrefix_savedNodes').val(prePopNodeName);
}

function PriorityPostNeid(woid, comment) {
    var lead_time = new Object();
    var todaydate = new Date();
    var role = JSON.parse(ActiveProfileSession).role;
    if (role == "Default User")
        role = "Network Engineer";
    lead_time.actorType = role;
    lead_time.auditPageId = woid;
    lead_time.auditgroupid = 0;
    lead_time.auditGroupCategory = "WORK_ORDER";
    lead_time.additionalInfo = "";
    lead_time.context = "WORK_ORDER" + "_" + woid;
    lead_time.content = comment;
    lead_time.created = todaydate;
    lead_time.fullname = signumGlobal;
    lead_time.importance = 0;
    lead_time.modified = todaydate;
    lead_time.notificationFlag = 0;
    lead_time.parent = 0;
    lead_time.type = "Priority Change";
    lead_time.fieldName = "";
    lead_time.fileURL = "";
    lead_time.newValue = "";
    lead_time.oldValue = "";
    lead_time.commentCategory = "WO_PRIORITY_EDIT";

    let isAuditColumnlengthExceeded = false;

    if (lead_time.content.length > 1000 || lead_time.context.length > 1000 || lead_time.additionalInfo.length > 1000) {
        isAuditColumnlengthExceeded = true;
    }

    if (!isAuditColumnlengthExceeded) {
        $.isf.ajax({
            url: `${service_java_URL}auditing/addComment`,
            type: 'POST',
            crossDomain: true,
            context: this,
            contentType: C_CONTENT_TYPE_APPLICATION_JSON,
            data: JSON.stringify(lead_time),

            success: function (returndata) {
                let data = returndata;
                if (data.isValidationFailed == true) {
                    pwIsf.alert({ msg: data.formErrors[0], type: 'warning' });
                }
                else {
                    NotifyMobileDevice(lead_time, returndata.responseData);
                }
            },
            error: function (xhr, status, statusText) {
                pwIsf.alert({ msg: 'Reason and comment cannot be stored', type: 'error' });
            }
        });
    }
    else {
        pwIsf.alert({ msg: "Max comment character length(1000) exceeded", type: "error", autoClose: 4 });
    }
}

function setPriority() {
    let comment = $("textarea#prioty_comment").val();
    if (comment == "" || comment == null || comment == undefined) {
        comment="";
    }
    let woid = $("#modal_woid").val();
    let priority = $("#modal_priority option:selected").val();
    let role = JSON.parse(ActiveProfileSession).role;
    if (role == "Default User")
        role = "Network Engineer";

    if (priority == "" || priority == undefined || priority == null) {
        pwIsf.alert({ msg: 'Priority should be selected', type: 'warning' });
    }
    else if (comment.length > 1000) {
        pwIsf.alert({ msg: 'Comment should be less than 1000 characters', type: 'error' });
    }
    else {
        pwIsf.addLayer({ text: C_PLEASE_WAIT });
        $.isf.ajax({
            url: `${service_java_URL}woManagement/editWOPriority/${woid}/${priority}/${signumGlobal}/${role}?comment=${comment}`,
            context: this,
            crossdomain: true,
            processData: true,
            contentType: C_CONTENT_TYPE_APPLICATION_JSON,
            type: 'POST',
            xhrFields: {
                withCredentials: false
            },
            success: function (data) {
                pwIsf.alert({ msg: 'Priority Updated Successfully', type: 'success' });
                $("#modalPriority").modal('hide');
            },
            error: function (xhr, status, statusText) {
               console.log(C_COMMON_ERROR_MSG);
            },
            complete: function (xhr, statusText) {
                pwIsf.removeLayer();
            }
        });
    }
}

function deleteWOPlanOrWO(el) {
    var checkedWOCount = $(".checkBoxClassWO:checked").length;
    var checkedWOPlanCount = $(".checkBoxClassWOPlan:checked").length;
    if (checkedWOCount > 0 && checkedWOPlanCount == 0) {
        multipleWODelete(el);
    }
    else if (checkedWOPlanCount > 0 && checkedWOCount == 0) {
        multipleWOPlanDelete();
    }
    else if (checkedWOPlanCount > 0 && checkedWOCount > 0) {
        pwIsf.alert({ msg: "Please select either WO Plan or WO!", type: 'warning' });
    }
    else if (checkedWOPlanCount == 0 && checkedWOCount == 0) {
        pwIsf.alert({ msg: "Please select an item to delete!", type: 'warning' });
    }
}

function multipleWOPlanDelete() {
    var wo = new Object();
    wo.lstWoPlanID = getWOPlansForMultipleDelete();
    wo.projectID = localStorage.getItem("views_project_id");
    wo.lastModifiedBy = signumGlobal;
    wo.loggedInUser = signumGlobal;
    wo.type = "WOPLAN";
    pwIsf.addLayer({ text: C_PLEASE_WAIT });
    $.isf.ajax({
        url: `${service_java_URL}projectManagement/deleteProjectComponents`,
        context: this,
        crossdomain: true,
        processData: true,
        contentType: C_CONTENT_TYPE_APPLICATION_JSON,
        type: 'POST',
        data: JSON.stringify(wo),
        xhrFields: {
            withCredentials: false
        },
        success: function (data) {
            if (data.isDeleted) {
                wo.type = 'DELETE_WOPLAN';
                var msgVar = '';
                    msgVar = 'Do you want to delete this work plan (' + wo.lstWoPlanID + ')? ';
                pwIsf.confirm({
                    title: 'Delete work plan', msg: msgVar,
                    'buttons': {
                        'Yes': {
                            'action': function () {
                                pwIsf.addLayer({ text: C_PLEASE_WAIT });
                                $.isf.ajax({
                                    url: `${service_java_URL}projectManagement/deleteProjectComponents`,
                                    context: this,
                                    crossdomain: true,
                                    processData: true,
                                    contentType: C_CONTENT_TYPE_APPLICATION_JSON,
                                    type: 'POST',
                                    data: JSON.stringify(wo),
                                    xhrFields: {
                                        withCredentials: false
                                    },
                                    success: function (data) {
                                        if (data.isDeleted) {
                                            pwIsf.alert({ msg: "Successfully Deleted.", autoClose: 3 });
                                            getWorkOrderDetails();
                                        } else {
                                            pwIsf.alert({ msg: data.msg, type: 'warning' });
                                        }
                                    },
                                    error: function (xhr, status, statusText) {
                                        console.log(C_COMMON_ERROR_MSG);
                                    },
                                    complete: function (xhr, statusText) {
                                        pwIsf.removeLayer();
                                    }
                                });
                            }
                        },
                        'No': { 'action': function () { } },
                    }
                });
            } else {
                pwIsf.alert({ msg: data.msg, type: 'warning' });
            }
        },
        error: function (xhr, status, statusText) {
            console.log(C_COMMON_ERROR_MSG);
        },
        complete: function (xhr, statusText) {
            pwIsf.removeLayer();
        }
    });
}

function refreshTableWOView() {
    $('#table_wo').empty();
    createWOView();
    $('html, body').animate({
        scrollTop: $("div.WOViewScreen").offset().top
    }, 100)
}

/*Multiple WO delete in WO View*/
function multipleWODelete(el) {
    pwIsf.addLayer({ text: "Please wait ..." });
    var selectedworkorders = [];
    var selectedWOStatus = [];
    if (el.name == "WO View") {
        selectedworkorders = getWOsForMultipleDelete();
        selectedWOStatus = getWOStatusForMultipleDelete();
        $('html, body').animate({
            scrollTop: $("div.WOViewScreen").offset().top
        }, 100)
    }
    else if (el.name == "WO Plan View") {
        selectedworkorders = getWOForMultipleDeleteWOPlanView();
        selectedWOStatus = getWOStatusForMultipleDeleteWOPlanView();
    }
    if (selectedworkorders != 0) {
        if (selectedWOStatus.includes("ASSIGNED") || selectedWOStatus.includes("DEFERRED") || selectedWOStatus.includes("REOPENED")) {
            var transferArr = { "signum": signumGlobal, "woID": selectedworkorders };
            pwIsf.confirm({
                title: 'Delete Work Orders', msg: "Do you want to delete the selected work orders?",
                'buttons': {
                    'Yes': {
                        'action': function () {
                            var request = $.isf.ajax({
                                url: `${service_java_URL}woManagement/deleteWorkOrderList`,
                                method: "POST",
                                contentType: C_CONTENT_TYPE_APPLICATION_JSON,
                                data: JSON.stringify(transferArr),
                                returnAjaxObj: true,
                                dataType: "html"
                            });
                            request.success(function (msg) {
                                pwIsf.removeLayer();
                                if (el.name == "WO View")
                                    refreshTableWOView();
                                else if (el.name == "WO Plan View")
                                    refreshTable();
                                pwIsf.alert({ msg: "WOs successfully deleted" });
                                $("#ckbCheckAllWOView").prop('checked', false);
                            });
                            request.fail(function (jqXHR, textStatus) {
                                pwIsf.removeLayer();
                                var err = JSON.parse(jqXHR.responseText).errorMessage;
                                pwIsf.alert({ msg: err, type: "error" });
                            });
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
            pwIsf.alert({ msg: "Only Assigned, Reopened, Deferred and Unallocated WOs can be deleted!", type: 'error' });
            pwIsf.removeLayer();
        }
    }
    else {
        pwIsf.alert({ msg: "Please select at least one work order" });
        pwIsf.removeLayer();
    }
}

// Download Function for WO View Table
function downloadWOFile(thisObj) {
    var d = new Date(0);
    var startDateUniversal = formatted_date(d);
    var eDate = localStorage.getItem("wo_endDate");
    var status = localStorage.getItem("wo_status");
    downloadWOViewData({ thisObj: thisObj, projectid: localStorage.getItem("views_project_id"), startDate: startDateUniversal, endDate: eDate, status: status });
}

/*---------------Get multiple WOPlans for multiple deletion in WOPlan View----------------------------*/
function getWOPlansForMultipleDelete() {
    /* declare an checkbox array */
    var chkArray = [];

    /* look for all checkboes that have a class 'chk' attached to it and check if it was checked */
    $(".checkBoxClassWOPlan:checked").each(function () {
        parseInt(chkArray.push($(this).val()));
    });

    /* we join the array separated by the comma */
    var selected;
    selected = chkArray.join(',');

    /* check if there is selected checkboxes, by default the length is 1 as it contains one single comma */
    if (selected.length > 1) {
        return chkArray;
    } else {
        return 0;
    }
}

/*---------------Get multiple WOs for multiple deletion in WO Plan View----------------------------*/
function getWOStatusForMultipleDeleteWOPlanView() {
    /* declare an checkbox array */
    var chkArray = [];

    /* look for all checkboes that have a class 'chk' attached to it and check if it was checked */
    $(".checkBoxClassWO:checked").each(function () {
        parseInt(chkArray.push($(this).attr("name")));
    });

    /* we join the array separated by the comma */
    var selected;
    selected = chkArray.join(',');

    /* check if there is selected checkboxes, by default the length is 1 as it contains one single comma */
    if (selected.length > 1) {
        return chkArray;
    } else {
        return 0;
    }
}

function getWOForMultipleDeleteWOPlanView() {
    /* declare an checkbox array */
    var chkArray = [];

    /* look for all checkboes that have a class 'chk' attached to it and check if it was checked */
    $(".checkBoxClassWO:checked").each(function () {
        parseInt(chkArray.push($(this).val()));
    });

    /* we join the array separated by the comma */
    var selected;
    selected = chkArray.join(',');

    /* check if there is selected checkboxes, by default the length is 1 as it contains one single comma */
    if (selected.length > 1) {

        return chkArray;
    } else {

        return 0;
    }
}

/*---------------Get multiple WOs for multiple deletion in WO View----------------------------*/
function getWOStatusForMultipleDelete() {
    /* declare an checkbox array */
    var chkArray = [];

    /* look for all checkboes that have a class 'chk' attached to it and check if it was checked */
    $(".checkBoxClassWOView:checked").each(function () {
        parseInt(chkArray.push($(this).attr("name")));
    });

    /* we join the array separated by the comma */
    var selected;
    selected = chkArray.join(',');

    /* check if there is selected checkboxes, by default the length is 1 as it contains one single comma */
    if (selected.length > 1) {
        return chkArray;
    } else {
        return 0;
    }
}

function getWOsForMultipleDelete() {
    /* declare an checkbox array */
    var chkArray = [];

    /* look for all checkboes that have a class 'chk' attached to it and check if it was checked */
    $(".checkBoxClassWOView:checked").each(function () {
        parseInt(chkArray.push($(this).val()));
    });

    /* we join the array separated by the comma */
    var selected;
    selected = chkArray.join(',');

    /* check if there is selected checkboxes, by default the length is 1 as it contains one single comma */
    if (selected.length > 1) {
        return chkArray;
    } else {
        return 0;
    }
}
var click = false;

/*---------------Expand/Collapse All WOs DataTable----------------------------*/
function expandCollapseWO(el) {
    if (!click) {
        $("#expandCollapseLink").removeClass("expandButton");
        $("#expandCollapseLink").addClass("collapseButton");
        $(".details-control").removeClass('expandButton');
        $(".details-control").addClass('collapseButton');
        oTableWOPlan.rows().every(function () {
            // If row has details collapsed
            if (!this.child.isShown()) {
                // Open this row
                this.child(formatData(this.data(), true)).show();
                $(this.node()).addClass('shown');
            }
        });
        click = true;
    } else {
        $("#expandCollapseLink").removeClass("collapseButton");
        $("#expandCollapseLink").addClass("expandButton");
        $(".details-control").removeClass('collapseButton');
        $(".details-control").addClass('expandButton');
        click = false;
        oTableWOPlan.rows().every(function () {
            // If row has details expanded
            if (this.child.isShown()) {
                // Collapse row details
                this.child.hide();
                $(this.node()).removeClass('shown');
            }
        });
    }
}

/*---------------Get WO Data According to Duration Clicked----------------------------*/
function getDurationWO(text, el) {
    durationText = text;
    if (text != "This Week")
        $("a.divider").removeClass("active");
    $(el).addClass("active");
    switch (durationText) {
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
        case "Last Month":
            var today = new Date();
            y = today.getFullYear(), m = today.getMonth();
            var firstDay = new Date(y, m - 1, 1);
            var lastDay = new Date(y, m, 0);
            startDate = formatted_date(firstDay);
            endDate = formatted_date(lastDay);
            break;
        case "Today":
            var today = new Date();
            var tomorrow = new Date(today);
            tomorrow.setDate(tomorrow.getDate() + 1);
            startDate = formatted_date(today);
            endDate = formatted_date(tomorrow);
            break;
        case "This Week":
            getWeekDateRangeWO();
            break;
        case "This Month":
            var today = new Date();
            y = today.getFullYear(), m = today.getMonth();
            var firstDay = new Date(y, m, 1);
            var lastDay = new Date(y, m + 1, 1); //This Month + 1day
            startDate = formatted_date(firstDay);
            endDate = formatted_date(lastDay);
            break;
        case "Next Week":
            var today = new Date();
            var day = today.getDay();
            var diff = today.getDate() - day + (day == 0 ? 1 : 8);
            var week_start_tstmp = today.setDate(diff);
            var week_start = new Date(week_start_tstmp);
            startDate = formatted_date(week_start);
            var week_end = new Date(week_start_tstmp);  // first day of week 
            week_end = new Date(week_end.setDate(week_end.getDate() + 7));
            endDate = formatted_date(week_end);
            break;
        case "Next Month":
            var today = new Date();
            y = today.getFullYear(), m = today.getMonth();
            var firstDay = new Date(y, m + 1, 1);
            var lastDay = new Date(y, m + 2, 1);
            startDate = formatted_date(firstDay);
            endDate = formatted_date(lastDay);
            break;
    }
    var d = new Date(0);
    var startDateUniversal = formatted_date(d);
    
    if (statusText == "All")
        statusText = "";
    getWorkOrderDetails(startDateUniversal, endDate, woStatus);
}

/*---------------Get WO Data According to Status Clicked----------------------------*/
function getStatusWO(el) {
    statusText = el.innerText;
    $("a.dividerStatusWOPlan").removeClass("active");
    $(el).addClass("active");
    switch (statusText) {
        case "All":
            woStatus = "";
            break;
        case "Assigned":
            woStatus = "Assigned";
            break;
        case "InProgress":
            woStatus = "InProgress";
            break;
        case "OnHold":
            woStatus = "OnHold";
            break;
        case "Deferred":
            woStatus = "Deferred";
            break;
        case "Unallocated":
            woStatus = "Unassigned";
            break;
    }
    localStorage.setItem("wo_status", woStatus);
    getWorkOrderDetails(startDate, endDate, woStatus);
}

function getDurationWOView(text, el) {
    durationTextWO = text;
    if (text != "This Week")
        $("a.dividerWOView").removeClass("active");
    $(el).addClass("active");
    switch (durationTextWO) {
        case "Last Week":
            var today = new Date();
            var day = today.getDay();
            var diff = today.getDate() - day + (day == 0 ? -13 : -6);
            var week_start_tstmp = today.setDate(diff);
            var week_start = new Date(week_start_tstmp);
            startDateWO = formatted_date(week_start);
            var weekEnd = new Date(week_start_tstmp);
            weekEnd = new Date(weekEnd.setDate(weekEnd.getDate() + 6));
            endDateWO = formatted_date(weekEnd);
            break;
        case "Last Month":
            var today1 = new Date();
            y = today1.getFullYear();
            m = today1.getMonth();
            var firstDay = new Date(y, m - 1, 1);
            var lastDay = new Date(y, m, 0);
            startDateWO = formatted_date(firstDay);
            endDateWO = formatted_date(lastDay);
            break;
        case "Today":
            var today2 = new Date();
            var tomorrow = new Date(today2);
            tomorrow.setDate(tomorrow.getDate() + 1);
            startDateWO = formatted_date(today2);
            endDateWO = formatted_date(tomorrow);
            break;
        case "This Week":
            getWeekDateRangeWO();
            break;
        case "This Month":
            var today3 = new Date();
            y = today3.getFullYear();
            m = today3.getMonth();
            var frstDay = new Date(y, m, 1);
            var lstDay = new Date(y, m + 1, 1);
            startDateWO = formatted_date(frstDay);
            endDateWO = formatted_date(lstDay);
            break;
        case "Next Week":
            var today4 = new Date();
            var day1 = today4.getDay();
            var diff1 = today4.getDate() - day1 + (day1 === 0 ? 1 : 8);
            var weekStartTstmp = today4.setDate(diff1);
            var weekStart = new Date(weekStartTstmp);
            startDateWO = formatted_date(weekStart);
            var weekEND = new Date(weekStartTstmp);
            weekEND = new Date(weekEND.setDate(weekEND.getDate() + 7));
            endDateWO = formatted_date(weekEND);
            break;
        case "Next Month":
            var today5 = new Date();
            y = today5.getFullYear();
            m = today5.getMonth();
            var firstDay1 = new Date(y, m + 1, 1);
            var lastDay1 = new Date(y, m + 2, 1);
            startDateWO = formatted_date(firstDay1);
            endDateWO = formatted_date(lastDay1);
            break;
    }
    var d = new Date(0);
    var startDateUniversal = formatted_date(d);
    if (statusTextWO == "All")
        statusTextWO = "";
    createWOView(startDateUniversal, endDateWO, woStatusWO);
    $('html, body').animate({
        scrollTop: $("div.WOViewScreen").offset().top
    }, 100)
}

/*---------------Get WO Data According to Status Clicked----------------------------*/
function getStatusWOView(el) {
    statusTextWO = el.innerText;
    $("a.dividerStatusWOView").removeClass("active");
    $(el).addClass("active");
    switch (statusTextWO) {
        case "All":
            woStatusWO = "";
            break;
        case "Assigned":
            woStatusWO = "Assigned";
            break;
        case "InProgress":
            woStatusWO = "InProgress";
            break;
        case "OnHold":
            woStatusWO = "OnHold";
            break;
        case "Deferred":
            woStatusWO = "Deferred";
            break;
        case "Unallocated":
            woStatusWO = "Unassigned";
            break;
    }
    createWOView(startDateWO, endDateWO, woStatusWO);
    $('html, body').animate({
        scrollTop: $("div.WOViewScreen").offset().top
    }, 100)
}

function tooltipValWO() {
    var x = $("#woNameBefore").val() + $("#woNameAfter").text();
    $("#woNameBefore").attr("title", x);
}

$(function () {
    $('#selectAspUserSignumTransfer').change(function (e) {
        var selected = $(e.target).val();
        if (selected != undefined && selected != null) {
            $('#submitbtnModal').attr('disabled', false);
        }
        else {
            $('#submitbtnModal').attr('disabled', true);
        }
    });
});
