$(document).ready(function () {
    selectSource();
    selectAPI();
    $("#extRef").select2({
        placeholder: "Select External Reference"

    });
    $("#extRef1").select2({
        placeholder: "Select External Reference"
    });
    getKeyTable();

});

function showUser() {    
    document.getElementById("userBox").style.display = "";
    document.getElementById("passBox").style.display = "";
    document.getElementById("generateKeybtn").style.display = "";
    $('#username').val('');
    $('#password').val('');
   
}

function selectSource() {
    pwIsf.addLayer({ text: 'Please wait ...' });
    $("#extRef1").find('option').not(':first').remove();
    $.isf.ajax({
        url: service_java_URL + "externalInterface/getAllExternalSource",
        async: false,
        success: function (data) {
            $('#extRef').append('<option value="' + 0 + '"></option>');
            //$('#extRef1').append('<option value="' + 0 + '"></option>');
            $.each(data, function (i, d){
                $('#extRef').append('<option value="' + d + '">' + d + '</option>')
                $('#extRef1').append('<option value="' + d.SourceID + '">' + d.SourceName + '</option>')
            });
            pwIsf.removeLayer();
        },
        error: function (xhr, status, statusText) {
            console.log('');
        }
    });
}
 function split(val) {
        return val.split(/,\s*/);
    }
    function extractLast(term) {
        return split(term).pop();
}

function isExtRefValidated() {
    isValidated = true;
    let extRefName = $("#extRefName").val();
    let extRefOwner = $("#selectReferenceOwnerSignum").val();
    let extRefSponsor = $("#selectReferenceSponsorSignum").val();
    let purpose = $("#description").val();

    if (extRefName == "" || extRefName == null) {
        isValidated = false;
    }

    if (extRefOwner == "" || extRefOwner == null) {
        isValidated = false;
    }

    if (extRefSponsor == "" || extRefSponsor == null) {
        isValidated = false;
    }

    if (purpose == "" || purpose == null) {
        isValidated = false;
    }

    return isValidated;
}

//Add External reference. 
$(document).on("click", "#saveReference", function () {

    if (isExtRefValidated()) {
        let extRefName = $('#extRefName').val();
        let extRefOwner = $('#selectReferenceOwnerSignum').val();
        let extRefSponsor = $('#selectReferenceSponsorSignum').val();
        let description = $('#description').val();

        let addReferenceObj = new Object();
        addReferenceObj.type = "JWT";
        addReferenceObj.sourceName = extRefName;
        addReferenceObj.createdby = signumGlobal;
        addReferenceObj.referenceOwner = extRefOwner;
        addReferenceObj.referenceSponser = extRefSponsor;
        addReferenceObj.description = description;

        $.isf.ajax({
            url: service_java_URL + "projectManagement/addExternalSource",
            type: "POST",
            async:false,
            data: JSON.stringify(addReferenceObj),
            contentType: "application/json",
            success: function (data) {
                $('#modalAddReference').modal('hide');
                pwIsf.alert({ msg: 'Saved', type: 'info' });
                selectSource();
            },
            error: function () {
                pwIsf.alert({ msg: 'Error while saving', type: 'error' });
            }
        });
    }

});

//$("#selectReferenceOwnerSignum").on("keyup", function (event) {

//        if (event.keyCode === $.ui.keyCode.TAB &&
//            $(this).autocomplete("instance").menu.active) {
//            event.preventDefault();
//        }
//    })
//        .autocomplete({

//            //appendTo: "#modal_wo_creation",

//            source: function (request, response) {
//                $.isf.ajax({
//                    url: service_java_URL + "activityMaster/getEmployeesByFilter",
//                    type: "POST",

//                    data: {
//                        term: extractLast(request.term)
//                    },
//                    success: function (data) {
//                        $("#selectReferenceOwnerSignum").autocomplete().addClass("ui-autocomplete-loading");

//                        var result = [];
//                        $.each(data, function (i, d) {
//                            result.push({
//                                "label": d.signum + "/" + d.employeeName,
//                                "value": d.signum
//                            });
//                        });

//                        response($.ui.autocomplete.filter(
//                            result, extractLast(request.term)));
//                        //response(result);
//                        $("#selectReferenceOwnerSignum").autocomplete().removeClass("ui-autocomplete-loading");

//                    }
//                });
//            },

//            search: function () {
//                // custom minLength
//                var term = extractLast(this.value);
//                if (term.length < 3) {
//                    return false;
//                }
//            },
//            focus: function () {
//                // prevent value inserted on focus
//                return false;
//            },
//            select: function (event, ui) {
//                var terms = split(this.value);
//                // remove the current input
//                terms.pop();
//                // add the selected item
//                terms.push(ui.item.value);
//                // add placeholder to get the comma-and-space at the end
//                terms.push("");
//                this.value = terms.join(", ");
//                return false;
//            },
//            change: function (event, ui) {
//                if (!ui.item) {
//                    $(this).val("");                   
//                    $('#emptyAPI-message').show();
//                } else {
//                    $('#emptyAPI-message').hide();

//                }
//            },


//            minLength: 3

//        });

//$("#selectReferenceOwnerSignum").autocomplete("widget").addClass("fixedHeight");

function selectAPI() {
   

    pwIsf.addLayer({ text: 'Please wait ...' });
    $.isf.ajax({
        type: "GET",
        async:false,
        url: service_java_URL + "accessManagement/getSubscribedApiList?externalSourceName=DESKTOP",
        success: function (data) {
          
            $('#selectMultipleAPI').append('<option value="' + 0 + '"></option>');
            $.each(data, function (i, d) {
               // let intVal = parseInt(d.apiId);
                $('#selectMultipleAPI').append('<option value=' + d.apiId + '>' + d.apiName + '</option>')
            });
            pwIsf.removeLayer();
        },
        error: function (xhr, status, statusText) {
            console.log('');
        }
    });
}

function isKeyValidated() {
    let isValidated = true;
    let extRef = $("#extRef1").val();
    if (extRef == "" || extRef == null) {
        isValidated = false;
    }

    let apiList = $("#selectMultipleAPI").val();
    if (apiList == "" || apiList == null) {
        isValidated = false;
    }

    let expiryDate = $("#expiryDate").val();
    if (expiryDate == "" || expiryDate == null) {
        isValidated = false;
    }

    return isValidated;
}

function generateKey() {

    if (isKeyValidated()) {
        let apiSelectedTemp = [];
        pwIsf.addLayer({ text: 'Please wait ...' });
        let extRefId = $('#extRef1 option:selected').val();
        let expiryYear = $('#expiryDate option:selected').val();
        apiSelectedTemp = $('#selectMultipleAPI').val();
        let apiSelected = []
        for (let i = 0; i < apiSelectedTemp.length; i++) {
            apiSelected.push(parseInt(apiSelectedTemp[i]));
        }

        var key = new Object();
        key.externalRefID = parseInt(extRefId);
        key.expirationInYear = parseInt(expiryYear);
        key.requestedAPI = apiSelected;
        key.ownerSignum = signumGlobal.toLowerCase();


        $.isf.ajax({
            url: service_java_URL + "token/generateAPIToken",
            context: this,
            async: false,
            crossdomain: true,
            processData: true,
            contentType: 'application/json',
            type: 'POST',
            data: JSON.stringify(key),
            xhrFields: {
                withCredentials: false
            },
            success: AjaxSucceeded,
            error: AjaxFailed
        });

        function AjaxSucceeded(data, textStatus) {
            pwIsf.removeLayer();
            let dataMsg = '';
            for (let a = 0; a < data.formMessageCount; a++) {
                dataMsg += data.formMessages[a];
            }
            pwIsf.alert({ msg: dataMsg, type: "info" });
            getKeyTable();
            $('#extRef1').val(null).trigger('change.select2');
            $('#selectMultipleAPI').val(null).trigger('change.select2');
            $("#expiryDate").val(1).trigger('change.select2');
            $("#topDiv").removeClass('updateBlockCss');
        }
        function AjaxFailed(xhr, status, statusText) {
            pwIsf.removeLayer();
            pwIsf.alert({ msg: "Failed to Add", type: "error" });

        }
    }
    //else {
    //    pwIsf.alert({
    //        msg:"All fields are mandatory"
    //    })
    //}
}

var formatted_date = function (date) {
    var m = ("0" + (date.getMonth() + 1)).slice(-2); // in javascript month start from 0.
    var d = ("0" + date.getDate()).slice(-2); // add leading zero 
    var y = date.getFullYear();
    return y + '-' + m + '-' + d;
}

//For Table Creation.
function getKeyTable() {
    pwIsf.addLayer({ text: 'Please wait ...' });
   
    var ServiceUrl = service_java_URL + "accessManagement/getUserTokenList";

    var table = $('#tblExternalReferences').DataTable({
        destroy: true,
        colReorder: true,
        dom: 'Bfrtip',
        buttons: [{
            extend: 'colvis',
            postfixButtons: ['colvisRestore'],
            columns: ':gt(0)'
        },
            'excel'],
        columnDefs: [
            { "width": "6%", "targets": 0 },
            { "width": "10%", "targets": 1 },
            { "width": "15%", "targets": 2 },
            { "width": "25%", "targets": 3 },
            { "width": "7%", "targets": 4 },
            { "width": "7%", "targets": 5 },
            { "width": "15%", "targets": 6 },
            { "width": "15%", "targets": 7 }
        ]
    });

    $('#tblExternalReferences tfoot th').each(function (i) {
        var title = $('#tblExternalReferences thead th').eq($(this).index()).text();
        if (title != "Actions" && title != "")
            $(this).html('<input type="text" class="form-control" style="font-size:12px;"  placeholder="Search ' + title + '" data-index="' + i + '" />');
    });

    // Apply the search
    table.columns().every(function () {
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

    table.on('column-reorder', function (e, settings, details) {
        //Apply the search
        table.columns().every(function () {
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
    });
    $("#ckbCheckAllDA").click(function (e) {
        // e.preventDefault();
        $(".checkBoxClassDA").prop('checked', $(this).prop('checked'));
    });

    $(".checkBoxClassWO").on('change', function (e) {
        // e.preventDefault();
        if (!$(this).prop("checked")) {
            $("#ckbCheckAllWO").prop("checked", false);
        }
    });
    var a = new Object();
    a.signum = signumGlobal;

    // table.clear();
    $.isf.ajax({
        url: ServiceUrl,
        type: 'POST',
        crossDomain: true,
        context: this,
        async:false,
        contentType: "application/json",
        data: JSON.stringify(a),
        success: function (data) {           

            //$('#moveButtonWO').show();
            //$('#AcceptbuttonWO').show();
            //$('#RejectbuttonWO').show();
            //$('.checkBoxClassDA').prop('checked', false);

            table.clear();

            $.each(data, function (i, d) {
                //var wName = (d.wOName.replace(/\"/g, '&quot;')).replace(/'/g, "\\'");
                table.row.add(['<div style="display:flex;">&thinsp;&thinsp;&thinsp;' + '<a title="Regenerate Key" href="#" onclick="regenerateKey(this)"><i style="cursor: pointer;color: green;" class="fa fa-paper-plane"></i></a>&nbsp;&nbsp;&nbsp;' +                    
                    '&thinsp;&thinsp;&thinsp;<a title="Disable Key" href="#" onclick="disableKey(this)"><i style="cursor: pointer;color: red;" class="fa fa-ban"></i></a>&thinsp;&thinsp;&thinsp;' +
                    '</div>', d.SourceName, d.API, d.Token, formatted_date(new Date(d.CreatedOn)), formatted_date(new Date(d.ExpirationDate)), '<a target="_blank" style="cursor:pointer" href="' + d.SwaggerLink + '">' + d.SwaggerLink + '</a>', d.APIDocLink]);
            })
            table.draw();


            //bindClickEventOnAction(); //Bind for view work order

        },
        error: function (xhr, status, statusText) {
            table.clear();
            table.draw();

        },
        complete: function (xhr, statusText) {
            pwIsf.removeLayer();
        }
    });
}

function flashTopDiv() {
    $("#topDiv").addClass('updateBlockCss', 100);
    //$("#topDiv").removeClass('updateBlockCss', 1000);
    //setTimeout(function () {        
    //    $("#topDiv").animate({ 'background-color': '' }, 'slow');
    //}, 1000);
}

function regenerateKey(obj) {

    let extRefName = $(obj).closest('tr').find('td:nth-child(2)').text();
    let APIs = $(obj).closest('tr').find('td:nth-child(3)').text().split(', ');
    let reqYear = new Date($(obj).closest('tr').find('td:nth-child(5)').text()).getFullYear();
    let expYear = new Date($(obj).closest('tr').find('td:nth-child(6)').text()).getFullYear();
    let duration = expYear - reqYear;

    let btnJson = {
        'Yes': {
            'action': function () {
                pwIsf.addLayer({ text: 'Please wait ...' });
                $.isf.ajax({
                    url: service_java_URL + "accessManagement/disableActiveToken?externalSourceName=" + extRefName,
                    type: 'POST',
                    crossDomain: true,
                    context: this,
                    async:false,
                    contentType: "application/json",
                    success: function (data) {
                        pwIsf.removeLayer();
                        let APIsSelected = [];
                        let extRefSelected = '';
                        $("#extRef1 option").filter(function () {
                            if (this.text == extRefName) {
                                extRefSelected = this.value;
                                return true;
                            }
                        })

                        $("#extRef1").val(extRefSelected).trigger('change');
                        
                        for (let i = 0; i < APIs.length; i++) {
                            $("#selectMultipleAPI option").filter(function () {
                                if (this.text == APIs[i]) {
                                    APIsSelected.push(this.value);
                                    return true;
                                }
                                //return this.text == APIs[i];
                                
                            })
                        }
                        $("#selectMultipleAPI").val(APIsSelected).trigger('change');

                        $("#expiryDate").val(duration).trigger('change');

                        $("#extRef1").focus();
                    },
                    error: function (xhr, status, statusText) {
                        pwIsf.removeLayer();
                    },
                    complete: function (xhr, statusText) {
                        getKeyTable();
                        pwIsf.removeLayer();
                        //$("#extRef1").focus();                       

                        

                        flashTopDiv();
                        
                        
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
        title:'Confirmation',
        msg: 'Your key will be disabled. Are you sure you want to regenerate this key?',
        'buttons': btnJson
    })
}

function disableKey(obj) {

    let extRefName = $(obj).closest('tr').find('td:nth-child(2)').text();

    let btnJson = {
        'Yes': {
            'action': function () {
                pwIsf.addLayer({ text: 'Please wait ...' });
                $.isf.ajax({
                    url: service_java_URL + "accessManagement/disableActiveToken?externalSourceName=" + extRefName,
                    type: 'POST',
                    async:false,
                    crossDomain: true,
                    context: this,
                    contentType: "application/json",
                    success: function (data) {
                        pwIsf.removeLayer();
                    },
                    error: function (xhr, status, statusText) {
                        pwIsf.removeLayer();
                    },
                    complete: function (xhr, statusText) {
                        getKeyTable();
                        pwIsf.removeLayer();
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
        title: 'Confirmation',
        msg: 'Are you sure you want to disable this key?',
        'buttons': btnJson
    })
}

//function generateKey() {
//    pwIsf.addLayer({ text: 'Please wait ...' });
//    $('#keyBox').val('');
//    var extRefId = $('#extRef option:selected').val();
//    var extRefName = $('#extRef option:selected').text();
//    var username = $('#username').val();
//    var pass = $('#password').val();


//    var key = new Object();
//    key.userName = username;
//    key.externalSourceName = extRefId;
//    key.password = pass;

//    $.isf.ajax({
//        url: service_java_URL + "token/logon",
//        context: this,
//        crossdomain: true,
//        processData: true,
//        contentType: 'application/json',
//        type: 'POST',
//        data: JSON.stringify(key),
//        xhrFields: {
//            withCredentials: false
//        },
//        success: AjaxSucceeded,
//        error: AjaxFailed
//    });

//    function AjaxSucceeded(data, textStatus) {
//        pwIsf.removeLayer();
//        document.getElementById("generateDiv").style.display = "";
//        selectSourceAPI();
//        document.getElementById("generatedKey").style.display = "";
//        $('#keyBox').val(data);
//        document.getElementById("successMsg").style.display = "";
//        document.getElementById("nalreadyMsg").style.display = "none";
//        document.getElementById("failureMsg").style.display = "none";
//    }
//    function AjaxFailed(xhr, status, statusText) {
//        pwIsf.removeLayer();
//        document.getElementById("failureMsg").style.display = "";
//        document.getElementById("nalreadyMsg").style.display = "none";
//        document.getElementById("successMsg").style.display = "none";

//    }

//}

function selectSourceAPI() {
    var extRefId = $('#extRef option:selected').val();
    var html = ``;

    $.isf.ajax({
        url: service_java_URL + "externalInterface/getAllowedApiListForExternalSource?externalSourceName=" + extRefId,
        async:false,
        success: function (data) {
            html += `External Reference API's Configured:`;
            html += `<ul class="list-group list-group-flush">`;
            $.each(data, function (i, d) {
                html += `<li class="list-group-item">` + d + `</li>`;
            });
            html += `</ul>`;
            $('#externalAPIList').html('').append(html);
        },
        error: function (xhr, status, statusText) {
            console.log('');
        }
    });
}

function extRefOwnerList() {
    $("#selectReferenceOwnerSignum").select2({
        ajax: {
            url: service_java_URL + "activityMaster/getEmployeesByFilter",
            type: 'POST',
            async:false,
            headers: commonHeadersforAllAjaxCall,
            dataType: 'json',
            delay: 250,
            data: function (params) {
                return {
                    term: params.term, // search term
                    page: params.page
                };
            },
            processResults: function (data, params) {
                // parse the results into the format expected by Select2
                // since we are using custom formatting functions we do not need to
                // alter the remote JSON data, except to indicate that infinite
                // scrolling can be used
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
        placeholder: 'Search signum',
        escapeMarkup: function (markup) { return markup; }, // let our custom formatter work
        minimumInputLength: 3,
        templateResult: formatRepo,
        templateSelection: formatRepoSelection
        //dropdownParent: $("#modalAddReference")
    });

}

function extRefSponsorList() {
    $("#selectReferenceSponsorSignum").select2({
        ajax: {
            url: service_java_URL + "activityMaster/getEmployeesByFilter",
            type: 'POST',
            async:false,
            headers: commonHeadersforAllAjaxCall,
            dataType: 'json',
            delay: 250,
            data: function (params) {
                return {
                    term: params.term, // search term
                    page: params.page
                };
            },
            processResults: function (data, params) {
                // parse the results into the format expected by Select2
                // since we are using custom formatting functions we do not need to
                // alter the remote JSON data, except to indicate that infinite
                // scrolling can be used
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
        placeholder: 'Search signum',
        escapeMarkup: function (markup) { return markup; }, // let our custom formatter work
        minimumInputLength: 3,
        templateResult: formatRepo,
        templateSelection: formatRepoSelection
        //dropdownParent: $("#modalAddReference")
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

function getTokenOnPageLoad() {
    pwIsf.addLayer({ text: 'Please wait ...' });
    var extRefId = $('#extRef option:selected').val();
    $.isf.ajax({
        url: service_java_URL + "externalInterface/getActiveToken?externalSourceName=" + extRefId,
        async: false,
        success: function (data) {
            if (data == null || data =="") {
                showUser();
                pwIsf.removeLayer();
                document.getElementById("generatedKey").style.display = "none";
                document.getElementById("generateDiv").style.display = "none";
                document.getElementById("alreadyMsg").style.display = "none";
                document.getElementById("nalreadyMsg").style.display = "";
                document.getElementById("successMsg").style.display = "none";
                document.getElementById("failureMsg").style.display = "none";
            }
            else {
                pwIsf.removeLayer();
                $('#keyBox').val('');
                document.getElementById("generateDiv").style.display = "";
                document.getElementById("generatedKey").style.display = "";
                selectSourceAPI();
                $('#keyBox').val(data);
                document.getElementById("alreadyMsg").style.display = "";
                document.getElementById("nalreadyMsg").style.display = "none";
                document.getElementById("successMsg").style.display = "none";
                document.getElementById("failureMsg").style.display = "none";
                document.getElementById("userBox").style.display = "none";
                document.getElementById("passBox").style.display = "none";
                document.getElementById("generateKeybtn").style.display = "none";
                $('#username').val('');
                $('#password').val('');
            }
        },
        error: function (xhr, status, statusText) {
            console.log('');
        }
    });
}

//{
//    "externalRefID": 1,
//        "expirationInYear" : 2,
//            "requestedAPI" : [1, 2, 3],
//                "ownerSignum" : "ekuyoge"
//} 

function addER() {
    $('#modalAddReference').modal('show');
    $("#extRefName").val("");
    $("#description").val("");
    $('#selectReferenceOwnerSignum').val(null).trigger('change.select2');
    $('#selectReferenceSponsorSignum').val(null).trigger('change.select2');
    extRefOwnerList();
    extRefSponsorList();
}
