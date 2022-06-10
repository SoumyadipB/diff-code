if (signumGlobal == null) {
    var id = getUrlParameter("requestId");

    window.location.href = "/pgid=requestapproval&requestId="+id;
}

function getUrlParameter(sParam) {
    var sPageURL = decodeURIComponent(window.location.search.substring(1)),
        sURLVariables = sPageURL.split('&'),
        sParameterName,
        i;

    for (i = 0; i < sURLVariables.length; i++) {
        sParameterName = sURLVariables[i].split('=');

        if (sParameterName[0] === sParam) {
            return sParameterName[1] === undefined ? true : sParameterName[1];
        }
    }
};

function getRequestAccessProfileDetail() {
    var id = getUrlParameter("requestId");
    
    if ($.fn.dataTable.isDataTable('#dataTableManagerRelation')) {
        oTable.destroy();
        $('#dataTableManagerRelation').empty();
    }
    $.isf.ajax({
        url: service_java_URL + "accessManagement/getAccessRequestsBySignum?signum=" + signumGlobal,
        type: 'GET',
        xhrFields: {
            withCredentials: false
        },
        success: ProfileAjaxSucceeded,
        error: ProfileAjaxFailed
    });
    function ProfileAjaxSucceeded(data, textStatus) {
        console.log(data);
        $.each(data, function (i, d) {
            var remarksTextId;
            var APPROVED = "APPROVED";
            var REJECTED = "REJECTED";
            var remarksText;
            d.remarks = '<input type="text" class="form-control" placeholder="Remarks" id="remarks' + d.AccessProfileID + '"/>';
                     
            d.actApprove = '<a href="#" onclick="approveRequest(' + '\'' + d.signum + '\',' + d.accessProfileID + ',\'' + APPROVED + '\',\'' + d.accessProfileName + '\')"  >' +
                    '<span class="fa fa-check" data-toggle="tooltip" title="Approve Request" style="color:blue"></span></a>&nbsp;&nbsp;';
            d.actDeny = '<a href="#" onclick="approveRequest(' + '\'' + d.signum + '\',' + d.accessProfileID + ',\'' + REJECTED + '\',\'' + d.accessProfileName + '\')"  >' +
                '<span class="fa fa-close" data-toggle="tooltip" title="Deny Request" style="color:red"></span></a>&nbsp;&nbsp;';
            d.action = d.actApprove + d.actDeny ;
            });
        $("#dataTableManagerRelation").append($('<tfoot><tr><th></th><th></th><th>Employee Name</th><th>Employee Signum</th><th>Role</th><th>Organisation</th><th>Access Profile Name</th></tr></tfoot>'));

             oTable = $('#dataTableManagerRelation').DataTable({
                 searching: true,
                 responsive: true,
                 "pageLength": 10,
                 "data": data,
                 colReorder: true,
                 order: [1],
                 dom: 'Bfrtip',
                 buttons: [
                     'colvis','excel'
                 ],
                 "destroy": true,
                 "columns": [
                     { "title": "Action", "targets": 'no-sort', "orderable": false, "searchable": false, "data": "action" },
                     { "title": "Remarks", "targets": 'no-sort', "orderable": false, "searchable": false, "data": "remarks" },
                     { "title": "Employee Name", "data": "employeeName" },
                     { "title": "Employee Signum", "data": "signum" },
                     { "title": "Role", "data": "role" }, { "title": "Organisation", "data": "organisation" },
                     { "title": "Access Profile Name", "data": "accessProfileName" }

                 ],
                 initComplete: function () {

                     $('#dataTableManagerRelation tfoot th').each(function (i) {
                         var title = $('#dataTableManagerRelation thead th').eq($(this).index()).text();
                         console.log("title " + title);
                         if (title != "Action"&& title != "Remarks")
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
        $('#dataTableManagerRelation tfoot').insertAfter($('#dataTableManagerRelation thead'));

    }
    function ProfileAjaxFailed(result) {
      //  $("#alertErrSAR").css("display", "block");
           alert("Error :" + result.responseText);
       
    }
}

function approveRequest(signum, id, status, accessProfileName) {
    var remarksId = 'remarks' + id;
    var remarks = $('#' + remarksId).val();
    if (remarks == null || remarks == '')
        remarks = '';


    var approveProfileObj = new Object();
    approveProfileObj.signumId = signum;
    approveProfileObj.approvalStatus = status;
    approveProfileObj.approvedBy = signumGlobal;
    approveProfileObj.accessProfileId = id;
    approveProfileObj.remark = remarks;
    approveProfileObj.accessProfileName = accessProfileName;
    var JsonObj = JSON.stringify(approveProfileObj);
    if (status == "APPROVED") {
        pwIsf.confirm({
            title: 'Approve User Aceess', msg: `Do you wish to approve the user's access?`, type: 'success',
            'buttons': {
                'YES': {
                    'action': function () {
                        $.isf.ajax({
                            url: service_java_URL + "accessManagement/updateAccessRequestStatus",
                            context: this,
                            crossdomain: true,
                            processData: true,
                            contentType: 'application/json',
                            type: 'POST',
                            data: JsonObj,
                            xhrFields: {
                                withCredentials: false
                            },
                            success: function (data) {
                                responseHandler(data);
                                getRequestAccessProfileDetail();
                                switch (status) {

                                }
                            },
                            error: function (xhr, status, statusText) {
                                console.log("Fail " + xhr.responseText);
                            }
                        });
                    }
                },
                'NO': {
                    'action': function () {
                        getRequestAccessProfileDetail();
                    }
                }

            }
        });
    }
    else if (status == "REJECTED") {
        pwIsf.confirm({
            title: 'Reject User Access', msg: `Do you wish to reject the user's access?`, type: 'success',
            'buttons': {
                'YES': {
                    'action': function () {
                        $.isf.ajax({
                            url: service_java_URL + "accessManagement/updateAccessRequestStatus",
                            context: this,
                            crossdomain: true,
                            processData: true,
                            contentType: 'application/json',
                            type: 'POST',
                            data: JsonObj,
                            xhrFields: {
                                withCredentials: false
                            },
                            success: function (data) {
                                responseHandler(data);
                                getRequestAccessProfileDetail();
                                switch (status) {

                                }
                            },
                            error: function (xhr, status, statusText) {
                                console.log("Fail " + xhr.responseText);
                            }
                        });
                    }
                },
                'NO': {
                    'action': function () {
                        getRequestAccessProfileDetail();
                    }
                }

            }
        });
    }
}