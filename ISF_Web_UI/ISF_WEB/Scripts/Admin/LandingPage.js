function getRequestDetails() {
    if ($.fn.dataTable.isDataTable('#tbBotBody')) {
        oTable.destroy();
        $("#tbReqBody").empty();
    }
    $.ajax({
        url: service_java_URL + "botStore/",
        context: this,
        crossdomain: true,
        processData: true,
        contentType: 'application/json',
        type: 'GET',
        xhrFields: {
            withCredentials: false
        },

        success: function (data) {
            $("#tbReqBody").append($('<tfoot><tr><th></th><th>Request Id</th><th>Request Name</th><th>SPOC</th><th>Periodicity</th><th>Status</th></tr></tfoot>'));
            oTable = $('#tbReqBody').DataTable({
                searching: true,
                responsive: true,
                "pageLength": 10,
                "data": data,
                "destroy": true,
                colReorder: true,
                dom: 'Bfrtip',
                buttons: [
                    'excelHtml5'
                ],
                "columns": [
                    {
                        "title": "Action", "targets": 'no-sort', "orderable": false, "searchable": false, "data": null,
                        "defaultContent": "",
                        "render": function (data, type, row, meta) {
                            return "<div style='display: flex'>< a class='icon-view' title = 'View' data - toggle='modal' data - target='#requestInfo' ><i class='fa fa-eye'></i></a > <a class='icon - edit lsp' title='Assign' data-toggle='modal' data-target='#assignDev'>< i class='fa fa-location-arrow' ></i ></a ><a class='icon-view lsp' title='Audit' href='#'><i class='fa fa-search'></i></a><a class='icon-delete lsp' title='Cancel' href='#'><i class='fa fa-remove'></i></a><a class='icon-edit lsp' title='Edit' href='#'><i class='fa fa-edit'></i></a><a class='icon-edit lsp' title='Deploy' href='#'><i class='fa fa-laptop'></i></a><a class='icon-view lsp' title='View Workflow' href='#'><i class='fa fa-code-fork'></i></a></div >";
                        }
                    },
                    { "title": "Request Id", "data": "" }, { "title": "Request Name", "data": "" },
                    { "title": "SPOC", "data": "" },
                    { "title": "Periodicity", "data": "" },
                    { "title": "Status", "data": "" },

                ],
                initComplete: function () {

                    $('#tbReqBody tfoot th').each(function (i) {
                        var title = $('#tbReqBody thead th').eq($(this).index()).text();
                        if (title != "Request Details")
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
            $('#tbReqBody tfoot').insertAfter($('#tbReqBody thead'));
        },
        error: function (xhr, status, statusText) {
            //console.log("Fail " + xhr.responseText);
            //console.log("status " + xhr.status);
            console.log('An error occurred');
        }
    });


}
function getDeployedBots() {
    if ($.fn.dataTable.isDataTable('#tbDepBody')) {
        oTable.destroy();
        $("#tbDepBody").empty();
    }
    $.ajax({
        url: service_java_URL + "botStore/",
        context: this,
        crossdomain: true,
        processData: true,
        contentType: 'application/json',
        type: 'GET',
        xhrFields: {
            withCredentials: false
        },

        success: function (data) {
            $("#tbDepBody").append($('<tfoot><tr><th></th><th>Request Id</th><th>Request Name</th><th>SPOC</th><th>Periodicity</th><th>Status</th></tr></tfoot>'));
            oTable = $('#tbDepBody').DataTable({
                searching: true,
                responsive: true,
                "pageLength": 10,
                "data": data,
                "destroy": true,
                colReorder: true,
                dom: 'Bfrtip',
                buttons: [
                    'excelHtml5'
                ],
                "columns": [
                    {
                        "title": "Action", "targets": 'no-sort', "orderable": false, "searchable": false, "data": null,
                        "defaultContent": "",
                        "render": function (data, type, row, meta) {
                            return " <div class='btn - group recurrence' data-toggle='buttons' id='recGrp'>< label class='btn btn-primary btn-sm ' ><input type='radio' name='options' value='enable' autocomplete='off'>Enable</label><label class='btn btn-primary btn-sm'><input type='radio' name='options' value='disable' autocomplete='off'>Disable</label></div>";
                        }
                    },
                    { "title": "Request Id", "data": "" }, { "title": "Request Name", "data": "" },
                    { "title": "SPOC", "data": "" },
                    { "title": "Periodicity", "data": "" },
                    { "title": "Status", "data": "" },

                ],
                initComplete: function () {

                    $('#tbDepBody tfoot th').each(function (i) {
                        var title = $('#tbDepBody thead th').eq($(this).index()).text();
                        if (title != "Deployed BOT Details")
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
            $('#tbDepBody tfoot').insertAfter($('#tbDepBody thead'));
        },
        error: function (xhr, status, statusText) {
            //console.log("Fail " + xhr.responseText);
            //console.log("status " + xhr.status);
            console.log('An error occurred');
        }
    });


}