$(document).ready(function () {
    displayNoteForMacroBots();
});
function getDeployedBots() {
    if ($.fn.dataTable.isDataTable('#tbAudit')) {
        oTable.destroy();
        $("#tbAudit").empty();
    }

    pwIsf.addLayer({ text: 'Please wait ...' });
    $.isf.ajax({
        url: `${service_java_URL}botStore/getBOTsForExplore`,
        context: this,
        crossdomain: true,
        processData: true,
        contentType: 'application/json',
        type: 'GET',
        xhrFields: {
            withCredentials: false
        },

        success: function (data) {
            pwIsf.removeLayer();
            const botRequestData = data;
            $("#tbAudit").append($('<tfoot><tr><th></th><th></th><th></th><th>BOT ID</th><th>BOT Name</th><th>Creatted On</th><th>Current Execution Time(Weekly)</th><th>Current Execution Time</th></tr></tfoot>'));
            oTable = $('#tbAudit').DataTable({
                searching: true,
                responsive: true,
                "pageLength": 10,
                "data": data,
                "destroy": true,
                colReorder: true,
                dom: 'Bfrtip',
                buttons: [
                    'colvis', 'excelHtml5'
                ],
                "columns": [
                    {
                        "title": "Deployed Status", "targets": 'no-sort', "orderable": false, "searchable": false, "data": null,
                        "defaultContent": "",
                        "render": function (data, type, row, meta) {
                            const isActive = data.isActive;
                            if (isActive === 1 || isActive === '1') {
                                return '<label class="switchSource"><input type="checkbox" checked class="toggleActive" onclick="toggleSourceStatus(' + data.botid + ')" id="togBtnSource_' + data.botid + '" /><div class="sliderSource round"><span class="onSource">Enabled</span><span class="offSource">Disabled</span></div></label>';
                            }
                            else {
                                return '<label class="switchSource"><input type="checkbox" class="toggleActive" onclick="toggleSourceStatus(' + data.botid + ')" id="togBtnSource_' + data.botid + '"/><div class="sliderSource round"><span class="onSource">Enabled</span><span class="offSource">Disabled</span></div></label>';
                            }
                           
                        }
                    },
                    { "title": "Server Status", "targets": 'no-sort', "orderable": false, "searchable": false, "data": null,
                    "defaultContent": "",
                        "render": function (data, type, row, meta) {
                            const isRunOnServer = data.isRunOnServer;
                            if (isRunOnServer === 1 || isRunOnServer==='1') {
                                return '<label class="switchSource"><input type="checkbox" checked class="toggleActive" onclick="toggleServerStatus(' + data.botid + ')" id="togBtnServer_' + data.botid + '"/><div class="sliderSource round"><span class="onSource">Enabled</span><span class="offSource">Disabled</span></div></label>';
                            }
                            else {
                                return '<label class="switchSource"><input type="checkbox" class="toggleActive" onclick="toggleServerStatus(' + data.botid + ')" id="togBtnServer_' + data.botid + '"/><div class="sliderSource round"><span class="onSource">Enabled</span><span class="offSource">Disabled</span></div></label>';
                            }
                        }
                    },
                    {
                        "title": "Audit Status", "targets": 'no-sort', "orderable": false, "searchable": false, "data": null,
                        "defaultContent": "",
                        "render": function (data, type, row, meta) {
                            const isAuditPass = data.isAuditPass;
                            if (isAuditPass == -1) {
                                return 'Pending';
                            }
                            else {
                                if (isAuditPass == 1) {
                                    return 'Pass';
                                }
                                else {
                                    return 'Fail';
                                }
                            }
                        }
                    },
                    { "title": "BOT ID", "data": "botid" },
                    { "title": "BOT Name", "data": "botname" },
                    {
                        "title": "Created On",
                        "render": function (data, row, type, meta) {
                            let d = new Date(type.createdOn);
                            return $.format.date(d, "yyyy-MM-dd");
                        }
                        },
                    { "title": "Current Execution Time(Weekly)", "data": "currentExecutioncountWeekly" },
                    { "title": "Current Execution Time", "data": "currentAvgExecutiontime" },


                ],
                initComplete: function () {

                    $('#tbAudit tfoot th').each(function (i) {
                        var title = $('#tbAudit thead th').eq($(this).index()).text();
                        if ((title !== "Deployed Status") && (title !== "Server Status"))
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
            oTable
                .search('')
                .draw();
            $('#tbReqBody tfoot').insertAfter($('#tbReqBody thead'));
        },
        error: function (xhr, status, statusText) {
            console.log(`An error occurred status:${xhr.status}`);
        }
    });


}
function toggleSourceStatus(requestID) {
    const checkbox = document.querySelector('input[id="togBtnSource_' + requestID + '"]');
    if (checkbox.checked) {
        $.isf.ajax({
            url: `${service_java_URL}botStore/changeDeployedBotStatus/${requestID}/${signumGlobal}/1`,
            type: 'POST',
            success: function (data) {
                enableDisable(data, ENABLEDTEXT);
            },
            error: function (xhr, status, statusText) {
                pwIsf.alert({ msg: 'Error in Enabling/Disabling', type: 'warning' });
            },
            complete: function () {
                pwIsf.removeLayer();
            }
        });

    } else {
        $.isf.ajax({
            url: `${service_java_URL}botStore/changeDeployedBotStatus/${requestID}/${signumGlobal}/0`,
            type: 'POST',
            success: function (data) {
                enableDisable(data, DISABLEDTEXT);
            },
            error: function (xhr, status, statusText) {
                pwIsf.alert({ msg: 'Error in Enabling/Disabling', type: WARNINGTEXT });
            },
            complete: function () {
                pwIsf.removeLayer();
            }
        });
    }

}
function toggleServerStatus(requestID) {
    const checkbox = document.querySelector('input[id="togBtnServer_' + requestID + '"]');
    if (checkbox.checked) {
        $.isf.ajax({
            url: `${service_java_URL}botStore/changeRunOnServerStatus/${requestID}/${signumGlobal}/1`,
            type: 'POST',
            success: function (data) {
                enableDisable(data, ENABLEDTEXT);
            },
            error: function (xhr, status, statusText) {
                pwIsf.alert({ msg: "Error in Enabling/Disabling", type: WARNINGTEXT });
            },
            complete: function () {
                pwIsf.removeLayer();
            }
        });

    } else {
        $.isf.ajax({
            url: `${service_java_URL}botStore/changeRunOnServerStatus/${requestID}/${signumGlobal}/0`,
            type: 'POST',
            success: function (data) {
                enableDisable(data, DISABLEDTEXT);
            },
            error: function (xhr, status, statusText) {
                pwIsf.alert({ msg: "Error in Enabling/Disabling", type: WARNINGTEXT });
            },
            complete: function () {
                pwIsf.removeLayer();
            }
        });
    }

}
function enableDisable(data,botStatus) {
    if (data.apiSuccess) {
        pwIsf.alert({ msg: `${data.responseMsg}(${botStatus})`, type: C_SUCCESS });
    }
    else {
        pwIsf.alert({ msg: data.responseMsg, type: WARNINGTEXT });
	}    
}