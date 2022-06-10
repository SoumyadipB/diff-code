//Constants
const msgColor2 = 'msgColor2';
const submitMessageId = '#submitMessage';
const startTimeId = '#startTime';
const isDownMsgId = '#isDownMsg';

let todayDate = new Date();
$(document).ready(function () {
$('#msgBox').val('');
resetControls();
    var dd = todayDate.getDate();
    var mm = todayDate.getMonth() + 1;
    var yyyy = todayDate.getFullYear();
    if (dd < 10) {
        dd = `0${dd}`;
    }

    if (mm < 10) {
        mm =`0${mm}`;
    }
    todayDate = `${yyyy}-${mm}-${dd}`;
    $(C_START_DATE_ID).val(todayDate)
    $(C_END_DATE_ID).val(todayDate)
    $(C_START_DATE_ID).datepicker({
        currentText: "Now",
        minDate: 0,
        dateFormat: 'yy-mm-dd',
        yearRange: "2020:2050"
    }).on('change', function () {
        changeStartDate();
    }).on('keyup',function () {
        changeStartDate();
    });
    $(C_END_DATE_ID).datepicker({
        currentText: "Now",
        minDate: 0,
        dateFormat: 'yy-mm-dd'
    }).on('change', function () {
        changeEndDate();
    }).on('keyup', function () {
        changeEndDate();
    });

    $(function () {
        $(function () {
            var x = $(`#${msgColor2}`).val();
            document.getElementById(msgColor2).value = x;
            $('#msgBox').css({ "color": x });
            console.log(x);
        });
        getMessageData();
    });
});

function changeEndDate() {
    const date = moment($(C_END_DATE_ID).val());
    const valid = date.isValid();
    if (valid && $(C_START_DATE_ID).val().length > 8 && $(C_END_DATE_ID).val().length > 8) {
        $("#eSpan").css({ 'display': 'none' });
        $(submitMessageId).prop('disabled', false);
    }
    else {
        $("#eSpan").css({ 'display': 'block' });
        $(submitMessageId).prop('disabled', true);
    }
}
function changeStartDate() {
    const date = moment($(C_START_DATE_ID).val());
    const valid = date.isValid();
    if (valid && $(C_START_DATE_ID).val().length > 8 && $(C_END_DATE_ID).val().length > 8) {
        $("#sSpan").css({ 'display': 'none' });
        $(submitMessageId).prop('disabled', false);
    }
    else {
        $("#sSpan").css({ 'display': 'block' });
        $(submitMessageId).prop('disabled', true);
    }
}
function changeColor(obj) {
    var y = obj.value;
    document.getElementById(msgColor2).value = y;
    $('#msgBox').css({ "color": y });
}
var messageId = "";
function updateMessage() {
    var message = new Object();
    message.startDate = `${$(C_START_DATE_ID).val()} ${$(startTimeId).val()}` ;
    message.endDate = `${$(C_END_DATE_ID).val()} ${$("#endTime").val()}`;
    message.message = $("#msgBox").val();
    var color1 = $(`#${msgColor2}`).val();
    var Color = color1.substr(1);
    message.color = Color;
    if ($('.btn-warning').val() !== 'UPDATE') {
        message.id = "";
    } else {
        message.id = messageId;
    }
    const isValid = validateAdminMsg(message);
    if (isValid===true) {
        if ($(isDownMsgId).is(':checked')) {
            message.down = 1;
        }
        else { message.down = 0; }
        if (Color.toLowerCase() === 'f32121d9' || Color.toLowerCase().substring(0, 1) === 'f') {
            pwIsf.alert({ msg: 'Please select diffrent color of message.', type: 'error' });
            return;
		}
        $.isf.ajax({
            url: `${service_java_URL}accessManagement/insertDownStatus`,
            type: 'POST',
            crossDomain: true,
            context: this,
            contentType: C_AJAX_JSON_CONTENT_TYPE,
            processData: true,
            data: JSON.stringify(message),
            success: function (returndata) {
                $(`.btn-warning`).val('SUBMIT');
                if (returndata.isValidationFailed === false) {
                    messageId = "";
                pwIsf.alert({ msg: returndata.formMessages[0], type: "success", autoClose: 4 });
                    $(C_START_DATE_ID).val(todayDate);
                    $(C_END_DATE_ID).val(todayDate);
                    $(startTimeId).val('0').trigger('change');
                    $("#endTime").val('0').trigger('change');
                    getMessageData();
            }
            else {
                pwIsf.alert({ msg: returndata.formErrors[0], type: "warning" });
            }
                resetControls();
        },
            error: function (xhr, status, statusText) {

                pwIsf.alert({ msg: 'Message Update Failed.', type: 'error' });

            },
        });
    }
}

function validateAdminMsg(message) {
    let isResult = true;
    const today = Date.now();
    if (Date.parse(message.startDate) < today) {
        pwIsf.alert({ msg: 'Start date time should be greater than current date time', type: 'error' });
        isResult = false;
    }
    else if (message.message === undefined || message.message === '' || message.message === null) {
        pwIsf.alert({ msg: 'Message field can not be blank', type: 'error' });
        isResult = false;
    }
    else if (Date.parse(message.startDate) > Date.parse(message.endDate)) {
        pwIsf.alert({ msg: 'End date should be greater than Start date', type: 'error' });
        isResult = false;
    } else if (Date.parse(message.endDate) < today ) {
        pwIsf.alert({ msg: 'End date should be greater than or equal to current date', type: 'error' });
        isResult = false;
    }
    else if (message.message.length > 1000) {
        pwIsf.alert({ msg: 'Maximum 1000 character allowed', type: 'warning' });
        isResult = false;
    }
    else if ($(C_START_DATE_ID).val() === '' || $(startTimeId).val() === '' || $(startTimeId).val() === '0'
        || $(startTimeId).val() === null || $(C_END_DATE_ID).val() === '' || $("#endTime").val() === '' || $("#endTime").val() === '0' || $("#endTime").val() === null) {
        pwIsf.alert({ msg: 'Please fill mandatory fileds', type: 'warning' });
        isResult = false;
    }
    return isResult;
}
var tabledata = [];
var oldMessageID = "";
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
function getMessageData() {
    pwIsf.addLayer({ text: 'Please wait ...' });
    var oTable = $(C_TBLLOGIN_MESSAGE_ID).DataTable();
    if ($.fn.dataTable.isDataTable(C_TBLLOGIN_MESSAGE_ID)) {
        oTable.destroy();
        $(C_TBLLOGIN_MESSAGE_ID).empty();
    }
    $.isf.ajax({
        url: `${ service_java_URL }accessManagement/getMessageTable`,
        context: this,
        crossdomain: true,
        processData: true,
        contentType: C_AJAX_JSON_CONTENT_TYPE,
        type: 'GET',
        success: function (data, status, jqXhr) {
            ajaxSucceededNew(data);
        },
        error: function (xhr, status, statusText) {
            ajaxFailed(xhr, status, statusText);
        }
    });
}
function ajaxSucceededNew(data) {
    tabledata = null;
    tabledata = data.responseData;
    addActionHtml(data);
    $(`${C_TBLLOGIN_MESSAGE_ID}_body`).html('');
    $(C_TBLLOGIN_MESSAGE_ID).append($('<tfoot><tr><th></th><th>Message</th><th>Start Date</th><th>End Date</th><th>Status</th></tr></tfoot>'));
    const oTable = $(C_TBLLOGIN_MESSAGE_ID).DataTable({
        "order": [[5, "none"]],
        searching: true,
        responsive: true,
        colReorder: true,
        "pageLength": 10,
        "paging": true,
        "data": data.responseData,
        "destroy": true,
        dom: 'Bfrtip',
        buttons: [
            {
                extend: 'excel',
                text: 'Export',
                filename: function () {
                    var d = (new Date()).toLocaleDateString();
                    return `Login Message_${signumGlobal}_${d}`;
                },
            }
        ],
        "columnDefs": [
            {
                "targets": [5],
                "visible": false,
                orderData: [5],
                "searchable": false
            }
        ],
        "columns": [
            {
                "title": "Action",
                "targets": 'no-sort',
                "orderable": false,
                "searchable": false,
                "data": "actionIcon",
                "width": "120px"
            },
            {
                "title": "Message",
                "data": "message",
                "orderable": true,
                "width": "350px"
            },
            {
                "title": "Start Date",
                "data": "startDate", "width": "150px", "orderable": true,
            },

            {
                "title": "End Date",
                "data": "endDate", "width": "150px", "orderable": true,
            },
            {
                "title": 'Status',
                "orderable": true,

                "targets": 'no-sort',
                "defaultContent": "",
                "render": function (rowdata, type, row, meta) {

                    if (row.down === true) {
                        return 'Active'
                    }
                    else {
                        return 'Inactive';
                    }
                },
            },
            {
                "title": "Modify Date",
                "data": "modifiedDate"

            },
        ],
        initComplete: function () {
            addColumns();
            var api = this.api();
            addColumnSearch(api);
            $(`${C_TBLLOGIN_MESSAGE_ID} tfoot`).insertAfter($(`${C_TBLLOGIN_MESSAGE_ID} thead`));
        }
    });

    $(C_TBLLOGIN_MESSAGE_ID).on('mousemove', 'tr td:nth-child(2)', function (e) {

        const rowData = getMessageFromDiv(oTable.row(this).index());
        const tooltipDiv = document.getElementById("tooltip");
        tooltipDiv.innerHTML = escapeHtml(rowData);

        tooltipDiv.style.left = `${e.pageX - 100}px`;
        tooltipDiv.style.top = `${e.pageY + 10}px`;

        if (!$("#tooltip").is(':visible')) {
            $("#tooltip").show();
        }
    })
    $(C_TBLLOGIN_MESSAGE_ID).on('mouseleave', 'td:nth-child(2)', function (e) {
        $("#tooltip").hide();
    })
    sessionStorage.setItem("msgFlag", false);
    $('div #arHead').removeClass('arrow-up').addClass('arrow-down');
    getActiveLoginMessage();
    pwIsf.removeLayer();
}
function addActionHtml(data) {
    $.each(data.responseData, function (i, d) {
        var dt = moment().format('YYYY-MM-DD hh:mm:ss');

        if (d.endDate < dt) {
            d.actionIcon = `<div style="display:flex"><a class="icon-edit"  onclick="editMessage(this,${i})"> ${getIcon('edit')} </a> </div>`;
        } else {
            var checked = '';
            if (d.down === true) {
                checked = "checked";
                oldMessageID = d.id;
            }
            else {
                checked = "";
            }
            d.actionIcon = `<div style="display:flex"><a class="icon-edit" onclick="editMessage(this,${i})"> ${getIcon('edit')}</a>&nbsp;&nbsp;<label class="switchSource">
                                <input type="radio" name="message" ${checked} class="toggleActive" data-toggle="toggle" onclick="messageStatus(this,${d.id},${d.down})">
                                </input><div class="sliderSource round"><span class="onSource">Enabled</span>
                                <span class="offSource">Disabled</span></div>
                                </label></div>`;
        }
        if (d.message.length > 200) {
            d.message = `<div id="tdMsg${i}" class="text-wrap width-300">${escapeHtml(d.message).substring(0, 100)}.......</div>
                      <div id="tdLongMsg${i}" style="display:none;" class="text-wrap width-300">${escapeHtml(d.message)}</div>`;
        } else {
            d.message = `<div id="tdMsg${i}" class="text-wrap width-300">${escapeHtml(d.message)}</div>`;
        }
    });
}
function addColumns() {
    $(`${C_TBLLOGIN_MESSAGE_ID} tfoot th`).each(function (i) {
        var title = $(`${C_TBLLOGIN_MESSAGE_ID} thead th`).eq($(this).index()).text();
        if (title !== "Action" && title !== "") {
            $(this).html(`<input type="text" class="form-control tableInput" style="font-size:12px;"  placeholder="Search ${title}" data-index="${i}" />`);
        }
    });
}
function addColumnSearch(api) {
    api.columns().every(function () {
        var that = this;
        $('.tableInput', this.footer()).on('keyup change', function (e) {
            if (that.search() !== this.value
                && (e !== undefined && e.target !== undefined && ((e.target.type !== "checkbox")
                    && (e.target.type !== "color") && e.target.className !== 'form-control sdate'))) {
                if (e.target.parentElement.parentNode.id === `${C_TBLLOGIN_MESSAGE_ID}_filter`) {
                    oTable.search(this.value).draw();
                } else {
                    that
                        .columns($(this).parent().index() + ':visible')
                        .search(this.value)
                        .draw();
                }

            }
        });
    });
}
function ajaxFailed(xhr, status, statusText) {
    pwIsf.alert({ msg: 'Message data fetching failed.', type: 'warning' });
    pwIsf.removeLayer();
}
function messageStatus(elem, id, down) {
    const serviceUrl = `${service_java_URL}accessManagement/enableMessage`;
    var obj = new Object();
    obj.id = id;
    obj.down = down;
    let confirmMsg ='';
    if (down === true) {
        confirmMsg = 'Inactive';
	}
    else {
        confirmMsg = 'Active';
	}
    pwIsf.confirm({
        title: confirmMsg + ' Message', msg: `Do you wish to ${confirmMsg.toLowerCase()}  message?`, type: 'success',
        'buttons': {
            'Yes': {
                'action': function () {
                    pwIsf.addLayer({ text: 'Please wait ...' });
                    $.isf.ajax({
                        url: serviceUrl,
                        method: 'POST',
                        headers: commonHeadersforAllAjaxCall,
                        contentType: C_AJAX_JSON_CONTENT_TYPE,
                        data: JSON.stringify(obj),
                        success: function (data) {
                            if (data.isValidationFailed === true) {
                                pwIsf.alert({ msg: returndata.formErrors[0], type: "warning" });
                            }
                            else {
                                pwIsf.removeLayer();
                            }
                            sessionStorage.setItem("msgFlag", false);
                            getMessageData();
                            getActiveLoginMessage();
                        },
                        error: function (xhr, status, statusText) {
                            pwIsf.removeLayer();
                            console.log('An error occurred');
                        }
                    });
                }
            },
            'No': { 'action': function () { getMessageData(); } },

        }
    });
}

function editMessage(elem, row) {

    const msg = getMessageFromDiv(row);
    $(C_START_DATE_ID).val(tabledata[row].startDate.split(' ')[0]);
    $(C_END_DATE_ID).val(tabledata[row].endDate.split(' ')[0]);
    $(startTimeId).val(tabledata[row].startDate.split(' ')[1].substring(0, 8)).trigger('change');
    $('#endTime').val(tabledata[row].endDate.split(' ')[1].substring(0, 8)).trigger('change');
    $("#msgBox").val(msg);
    $('#msgBox').css({ "color": `#${tabledata[row].color}` });
    $(`#${msgColor2}`).val(`#${tabledata[row].color}`);
    $(isDownMsgId).prop("checked", tabledata[row].down)
    messageId = tabledata[row].id;
    $(`.btn-warning`).val('UPDATE');
}
function getMessageFromDiv(row) {
    let msg = $(`#tdMsg${row}`).text();
    if ($(`#tdLongMsg${row}`).length) {
        msg = $(`#tdLongMsg${row}`).text();

    }
    return msg;
}
function resetControls() {
    $("#msgBox").val('');
    $(`#${msgColor2}`).val('#000000');
    $('#msgBox').css({ "color": '#000000' });
    $(isDownMsgId).prop("checked", false);
}
