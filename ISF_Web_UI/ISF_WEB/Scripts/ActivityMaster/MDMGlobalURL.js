$(document).ready(function () {

    getAllGlobalURLs();

    $("#resetGlobalURL").on('click',function () {
        resetGlobalURL();
        globalUrlEntering();
        globalUrlNameEntering();
    })

    $("#submitGlobalURL").on('click', function () {
        saveGlobalURL();

    })

});

//Get all global URLs
function getAllGlobalURLs() {

    $('#globalURLTable').empty();
    const service_URL = service_java_URL + 'activityMaster/getAllGlobalUrl';

    $.isf.ajax({
        url: service_URL,
        type: 'GET',
        success: function (data) {
            console.log("success");

        },
        complete: function (xhr, status) {
            if (status === "success") {
                 
                configureGlobalURLDataTable(xhr.responseJSON); // Add Datatable configuration to the table
            }
        },
        error: function (data) {
            console.log('error');
        }
    });



    const createGlobalURLTable = (getData) => {
        const thead = `<thead>
                        <tr>
                            <th>Actions</th>
                            <th>URL Name</th>
                            <th>URL</th>
                        </tr>
                    </thead>`;

        const tfoot = `<tfoot>
                        <tr>
                            <th></th>
                            <th>URL Name</th>
                            <th>URL</th>
                        </tr>
                    </tfoot>`;


        let tbody = ``;

        $(getData).each(function (i, d) {
            tbody += `<tr>
                            <td style="min-width:90px;">
                                <a class="icon-edit" title="Edit File" onclick="editGlobalURL(this)"><i class="fa fa-edit"></i></a>
                                <a class="icon-delete" title="Delete File" onclick="deleteGlobalURL(this)"><i class="fa fa-lg fa-trash-o"></i></a>
                                <input type="hidden" value="${d.globalUrlId}" />
                            </td>
                            <td>${d.urlName}</td>
                            <td>${d.urlLink}</td>
                        </tr>`;
        });

        $('#globalURLTable').html(thead + '<tbody>' + tbody + '</tbody>' + tfoot);

    };

    const configureGlobalURLDataTable = (getData) => {

        if ($.fn.dataTable.isDataTable('#globalURLTable')) {
            $('#globalURLTable').DataTable().destroy();
        }

        createGlobalURLTable(getData); //Create the Table for Global URLs dynamically

        $('#globalURLTable').DataTable({
            searching: true,
            responsive: true,
            retrieve: true,
            destroy: true,
            'pagelength': 10,
            'info': false,
            'columnDefs': [
                {
                    'searching': false,
                    'width': '10%',
                    'targets': [0]
                },
                {
                    'searching': true,
                    'targets': [1, 2]
                }
            ],
            dom: 'Bfrtip',
            initComplete: function () {

                $('#globalURLTable tfoot th').each(function (i) {
                    var title = $('#globalURLTable thead th').eq($(this).index()).text();
                    if (title !== "Actions") {
                        $(this).html('<input type="text" class="form-control" style="font-size:12px;"  placeholder="Search ' + title + '" data-index="' + i + '" />');
                    }
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

        $('#globalURLTable tfoot').insertAfter($('#globalURLTable thead'));

    }

}

//Edit Global URL. It populates the row data in the top div
function editGlobalURL(globalURLobj) {

    $("#topDiv").addClass('updateBlockCss', 100);
    const url = $(globalURLobj).closest('tr').find('td:nth-child(3)').text();
    const urlName = $(globalURLobj).closest('tr').find('td:nth-child(2)').text();
    const urlId = $(globalURLobj).closest('tr').find('td:nth-child(1) input').val();
    $('#globalURLName').val(urlName).trigger('change');
    $('#globalURL').val(url).trigger('change');
    $('#globalURLId').val(urlId).trigger('change');
    $('#submitGlobalURL').text('Update URL');

}

//Common function for Add and Update URL
function saveGlobalURL() {


    $("#globalUrlNameInput").html('Only 50 characters are allowed (50 left)');
    $("#globalUrlInput").html('Only 500 characters are allowed (500 left)');
    const url = $('#globalURL').val();
    const urlName = $('#globalURLName').val();
    const urlId = $('#globalURLId').val();
    const urlUpdateObj = new Object();
    const urlAddObj = new Object();    

    //If action is to update the URL. urlId is populated with some value when edit action is clicked
    if (urlId != "") {
        urlUpdateObj.urlLink = url;
        urlUpdateObj.urlName = urlName;
        urlUpdateObj.globalUrlId = urlId;
        urlUpdateObj.actionType = 'update';

        updateGlobalURL(urlUpdateObj);
    }
    //Else add new URL
    else {
        urlAddObj.urlLink = url;
        urlAddObj.urlName = urlName;     
        addGlobalURL(urlAddObj);
    } 

}

//Add new global URL
function addGlobalURL(urlAddObj) {

    const service_URL = service_java_URL + "activityMaster/saveGlobalUrl";

    $.isf.ajax({
        url: service_URL,
        type: 'POST',
        crossDomain: true,
        context: this,
        contentType: 'application/json',
        data: JSON.stringify(urlAddObj),
        success: function (data) {
            console.log('Success in test');
        },
        complete: function (xhr, status) {
            if (status === "success") {
                if (xhr.responseJSON.isValidationFailed) {
                    responseHandler(xhr.responseJSON);
                }
                else {
                    pwIsf.alert({ msg: "Successfully added!", type: "success" });
                    resetGlobalURL();
                    getAllGlobalURLs();
                }
            }            
        },
        error: function (data) {
            console.log('error in test');
        }
    })

}

//Update the selected global URL
function updateGlobalURL(urlUpdateObj) {

    const service_URL = service_java_URL + "activityMaster/updateGlobalUrl";

    $.isf.ajax({
        url: service_URL,
        type: 'POST',
        crossDomain: true,
        context: this,
        contentType: 'application/json',
        data: JSON.stringify(urlUpdateObj),
        success: function (data) {
            console.log('Success in test');
        },
        complete: function (xhr, status) {           
            if (status === "success") {
                if (xhr.responseJSON.isValidationFailed) {
                    responseHandler(xhr.responseJSON);
                }
                else {
                    pwIsf.alert({ msg: "Successfully updated!", type: "success" });
                    resetGlobalURL();
                    getAllGlobalURLs();
                }
            }
        },
        error: function (data) {
            console.log('error in test');
        }
    })

}

//Delete the selected global URL
function deleteGlobalURL(globalURLobj) {

    const btnJson = {
        'Yes': {
            'action': function () {

                pwIsf.addLayer({ text: 'Please wait ...' });

                const urlId = $(globalURLobj).closest('tr').find('td:nth-child(1) input').val();
                const url = $(globalURLobj).closest('tr').find('td:nth-child(3)').text();
                const urlName = $(globalURLobj).closest('tr').find('td:nth-child(2)').text();
                const service_URL = service_java_URL + "activityMaster/updateGlobalUrl";
                const urlDeleteObj = new Object();

                urlDeleteObj.urlLink = url;
                urlDeleteObj.urlName = urlName;
                urlDeleteObj.globalUrlId = urlId;
                urlDeleteObj.actionType = 'delete';

                $.isf.ajax({
                    url: service_URL,
                    type: 'POST',
                    crossDomain: true,
                    context: this,
                    contentType: 'application/json',
                    data: JSON.stringify(urlDeleteObj),
                    success: function (data) {
                        //Empty Block
                    },
                    complete: function (xhr, status) {
                        pwIsf.removeLayer();
                        if (status === "success") {
                            getAllGlobalURLs();
                        }
                    },
                    error: function (data) {
                        console.log('error in test');
                    }
                })
            },
            'class': 'btn btn-success'
        },
        'No': {
            'action': function () {
                //Empty Block
            },
            'class': 'btn btn-danger'
        },

    };

    pwIsf.confirm({
        title: 'Confirmation',
        msg: 'Do you want to delete this Global URL?',
        'buttons': btnJson
    })    
    
}

//Reset global URL fields
function resetGlobalURL() {

    $("#topDiv").removeClass('updateBlockCss');
    $('#globalURLName').val("").trigger('change');
    $('#globalURL').val("").trigger('change');
    $('#globalURLId').val("").trigger('change');
    $('#submitGlobalURL').text('Add Global URL');

}
function globalUrlEntering() {
    const characterCount = $(globalURL).val().length,
        current = $('#globalUrlInput'),
        left = 500 - characterCount;
    current.text("Only 500 characters are allowed (" + left + " left)");
    if (left < 0) {
        current.css('color', 'red')
    }
    else {
        current.css('color', '')
    }
}
function globalUrlNameEntering() {
    const characterCount = $(globalURLName).val().length,
        current = $('#globalUrlNameInput'),
        left = 50 - characterCount;
    current.text("Only 50 characters are allowed (" + left + " left)");
    if (left < 0) {
        current.css('color', 'red')
    }
    else {
        current.css('color', '')
    }

}
