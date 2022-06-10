var toolNameArray = [];
var LicensetypeArray = []
var LicenseOwnerArray = []
var AccessMethodeArray = []
var proficientEmpList = [];
const proficiencyFilterStatus = 'upgrade';
var signumResult = [];
var wfNameResult = [];

//variables for DR Access
const totalDRAccess = '#listProjectDR';
const tblDRId = '#TDeliveryRes';
let countOfTotalDR;
const wrongProjIDMsg = 'Wrong Project Id';
const profileRPM = 'Resource Planning Manager';
const emptySignumMsg = 'Please select Signum';
const failedDRAddition = 'Failed to add Delivery Responsible.';
const signumDRId = '#signumDR';
const negativeDRLimitMsg = 'Limit should not be negative';
const lblLimitDR = '#lblLimitText';
const limitValueDRId = '#limitDRFromConfig';
const limitValForDRAccess = parseInt(limitDRAccessValue);

//messages for DR Access
const limitExceedDRMsg = `Maximum Limit exceeded - Maximum ${limitValForDRAccess} DR allowed`;

// variables for parallel task config
const infoForParallelLimitID = '#infoForParallelTaskLimit';
const manualStepExeLimit = parseInt(MANUAL_STEP_EXE_DEFAULT_VALUE);
const autoStepExeLimit = parseInt(AUTOMATIC_STEP_EXE_DEFAULT_VALUE);

//messages for Parallel Task validations
const warningForManualOnly = `Manual task limit exceeded -
            Only ${manualStepExeLimit} Manual tasks are allowed`;
const warningForAutoOnly = `Automatic task limit exceeded -
            Only ${autoStepExeLimit} Automatic tasks are allowed`;
const warningForManualAndAuto = `Manual & Automatic task limit is exceeded -
            Only ${manualStepExeLimit} Manual and ${autoStepExeLimit} automatic tasks are allowed`;

function getProjectConfiguration() {
    getUserSignums();
    getUserSignumsDA();
    getUserSignumsRPM();
    getParallelTaskConfiguration();
    getDeliveryAcceptanceByProject();
    getRPMByProject();
    getExternalSources();
    getToolName();
    getLicenseOwner();
    getAccessMethod();
    getToolInfoTable();
    getAllURL();

    $("#resetUrl").on('click', function () {
        resetURL();
        globalUrlEnteringPC();
        globalUrlNameEnteringPC();
    })

    $("#addUrl").on('click', function () {
        saveLocalURL();
    })

    getAllUserSignum();
    getAllProjectWorkFlows();
    resetAllUpdateProficiencyData();
}

$(document).ready(function () {


    $("#selectAllWorkFlowIds-required").hide();
    $('#proficiencyComments').val('');
    $('#proficiencyDataTable thead').css({ display: 'none' })
    var roleID = JSON.parse(ActiveProfileSession).roleID;

    //Making Input Output URL Configuration Panel in readonly mode for DR(roleID 5)
    if (roleID == 5) {
        $(".pmView").hide();
        $("#allGlobalUrlDiv").removeAttr('style');
        $('#localUrlForm').hide();
    }
    $("#selectwfIdName").keyup(function () {
        if ($('#selectwfIdName').val().trim() == '' && $('#selectUserAllSignums').val().trim() == '') {
            $('#btnSearchProficiency').prop('disabled', true);
        }
    });
    $("#selectUserAllSignums").keyup(function () {
        if ($('#selectwfIdName').val().trim() == '' && $('#selectUserAllSignums').val().trim() == '') {
            $('#btnSearchProficiency').prop('disabled', true);
        }
    });
    // update btn click .
    $('#btnUpdateProficiency').on('click', function () {
        pwIsf.confirm({
            title: '<b>Update Proficiency!', msg: getConfirmBoxMsgWithTextArea(),
            'buttons': {
                'Yes': {
                    'action': function () {
                        // Get row data
                        updateEmpProficiency();
                        $('html, body').animate({
                            scrollTop: $("div#updateProficiency").offset().top
                        }, 1000);

                    }
                },
                'No': {
                    'action': function () {
                        $('html, body').animate({
                            scrollTop: $("div#updateProficiency").offset().top
                        }, 1000);
                    }
                },

            }
        });
    });
    // search btn click .
    $('#btnSearchProficiency').on('click', function (event) {
        searchUsers(proficiencyFilterStatus);
        resetFilterToExperience();
        event.preventDefault();
    });

})

function getConfirmBoxMsgWithTextArea() {
    let msg = `<div id="wiPopupDiv" class="row" style="display:inline-flex;"><label class="col-md-2 pull-left">Comments: </label>
                            <div class="col-md-10"><textarea class="inputFileClass commentsBoxOfUpdateProficiency" rows="1" type="text" maxlength="250" id="proficiencyComments" style="" placeholder="Add any Aditional comments to Downgrade or Upgrade" onkeyup="validateComments(this);"></textarea>
                                <span id="pmdrCommentsText" style="color:darkgray" class="pull-right">
                                    Only 250 characters are allowed (250 left)
                                </span>
                            </div></div><br/>`+ `<div class="row">Are you sure want to update proficiency?</div>`;
    return msg;
    //$('#confirmBox').addClass('confirmBoxWidth');
}


/******************* Input Output URL Access Configuration Start ************************/

// Shows all the Local URL from the API in the form of DataTable
function getAllURL() {

    $('#URLTable').empty();
    if (localStorage.getItem("views_project_id")) {
        let service_URL = service_java_URL + 'activityMaster/getAllLocalUrl?projectID=' + localStorage.getItem("views_project_id");

        $.isf.ajax({
            url: service_URL,
            type: 'GET',
            success: function (data) {
                console.log("success");
            },
            complete: function (xhr, status) {
                if (status == "success") {
                    URLDataTable(xhr.responseJSON.responseData);
                }
            },
            error: function (data) {
                console.log("error");
            }
        });

        //Making Input Output URL Configuration Panel in readonly mode for DR(roleID 5)
        let createURLTable = (getData) => {

            let roleID = JSON.parse(ActiveProfileSession).roleID;

            let thead = `<thead>
                          <tr>${roleID == 5 ?
                    `` :
                    `<th>Action</th>
                              <th>Status</th>`
                }
                              <th>URL Name</th>
                              <th>URL Link</th>                              
                            </tr>
                       </thead>`;

            let tbody = ``;
            let tfoot = ``;

            $(getData).each(function (i, d) {
                if (roleID == 5) {
                    if (d.localUrlStatus) {
                        tbody += `<tr>
                          <td>${d.localUrlName}</td>
                          <td>${d.localUrlLink}</td>
                       </tr>`;
                    }
                }
                else {
                    tbody += `<tr>                           
                        <td>
                          <a class="icon-edit" title="Edit File" onclick="editURL(this)"><i class="fa fa-edit"></i></a>
                          <input type="hidden" value="${d.localUrlId}" />
                           </td>
                          <td class="sorting">
                           <label class="switchSource">
                            <input type="checkbox" ${d.localUrlStatus == true ? 'checked' : ''} class="toggleActive" onclick="changeUrlStatus(this)">
                               <div class="sliderSource round">
                             <span class="onSource">Enabled</span><span class="offSource">Disabled</span>
                              </div>
                            </label></td>
                          <td>${d.localUrlName}</td>
                          <td>${d.localUrlLink}</td>
                       </tr>`;
                }

            });

            $("#URLTable").html(thead + '<tbody>' + tbody + '</tbody>' + '<tfoot>' + tfoot + '</tfoot>');
        };

        let URLDataTable = (getData) => {

            if ($.fn.dataTable.isDataTable("#URLTable")) {
                localURLDataTable.destroy();
            }

            createURLTable(getData);

            localURLDataTable = $("#URLTable").DataTable({
                searching: true,
                resposive: true,
                destroy: true,
            });
        }
    }
    else {
        pwIsf.alert({ msg: 'Project ID Missing', type: 'error' });
    }



}

// Triggers the Updation function of URLs when clicked on Edit
function editURL(editUrlObj) {
    $("#topDiv").addClass('updateCss');

    let urlName = $(editUrlObj).closest('tr').find('td:nth-child(3)').text();
    let url = $(editUrlObj).closest('tr').find('td:nth-child(4)').text();
    let urlId = $(editUrlObj).closest('tr').find('td:nth-child(1) input').val();

    $('#URLName').val(urlName).trigger("change");
    $('#URLlink').val(url).trigger("change");
    $('#URLId').val(urlId).trigger("change");
    $('#addUrl').text("UPDATE URL");
    $('#resetUrlDiv').css('padding-left', '20px');

}

//Get all global URLs
function getAllGlobalURLs() {

    let service_URL = service_java_URL + 'activityMaster/getAllGlobalUrl';

    $.isf.ajax({
        url: service_URL,
        type: 'GET',
        success: function (data) {
            console.log("success");

        },
        complete: function (xhr, status) {
            if (status == "success") {

                configureGlobalURLDataTable(xhr.responseJSON); // Add Datatable configuration to the table
            }
        },
        error: function (data) {
            console.log('error');
        }
    });

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

    let createGlobalURLTable = (getData) => {
        let thead = `<thead>
                        <tr>
                            <th>URL Name</th>
                            <th>URL</th>
                        </tr>
                    </thead>`;

        let tfoot = `<tfoot>
                        <tr>
                            <th>URL Name</th>
                            <th>URL</th>
                        </tr>
                    </tfoot>`;

        let tbody = ``;

        $(getData).each(function (i, d) {
            tbody += `<tr>
                            <td>${d.urlName}</td>
                            <td>${d.urlLink}</td>
                        </tr>`;
        });

        $('#globalURLTable').html(thead + '<tbody>' + tbody + '</tbody>' + tfoot);

    };

    let configureGlobalURLDataTable = (getData) => {

        if ($.fn.dataTable.isDataTable('#globalURLTable')) {
            $('#globalURLTable').DataTable().destroy();
            $('#globalURLTableArea').html('<table class="table table-bordered table-striped table-hover" id="globalURLTable"></table >');
        }

        createGlobalURLTable(getData); //Create the Table for Global URLs dynamically

        GlobalURLDataTable = $('#globalURLTable').DataTable({
            searching: true,
            responsive: true,
            retrieve: true,
            destroy: true,
            "lengthMenu": [10, 15, 20, 25],
            'info': false,
            'columnDefs': [
                {
                    'searching': true,
                    'targets': [0, 1]
                }
            ],
            initComplete: function () {

                $('#globalURLTable tfoot th').each(function (i) {
                    var title = $('#globalURLTable thead th').eq($(this).index()).text();
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
                        };
                    });
                });

            }
        });

        $('#globalURLTable tfoot').insertAfter($('#globalURLTable thead'));

    }

}

//Change the status of URL
function changeUrlStatus(urlObj) {

    let urlName = $(urlObj).closest('tr').find('td:nth-child(3)').text();
    let url = $(urlObj).closest('tr').find('td:nth-child(4)').text();
    let urlId = $(urlObj).closest('tr').find('td:nth-child(1) input').val();
    let urlUpdateObj = new Object();

    urlUpdateObj.localUrlLink = url;
    urlUpdateObj.localUrlName = urlName;
    urlUpdateObj.localUrlId = urlId;
    urlUpdateObj.actionType = "toggle";
    urlUpdateObj.localUrlStatus = urlObj.checked;
    urlUpdateObj.projectID = localStorage.getItem("views_project_id");

    let statusChanged = updateURL(urlUpdateObj, 'changeStatus');
    if (statusChanged == false) {
        urlObj.checked = !urlObj.checked;
    }
    else {
        let status = 'enabled';
        if (urlObj.checked == false) {
            status = 'disabled';
        }
        pwIsf.alert({ msg: urlName + ' has been ' + status, type: 'success' })
    }

}

// Calls for the Updation of URL or Addition of URL into the table
function saveLocalURL() {

    $("#globalUrlNameInputPC").html('Only 50 characters are allowed (50 left)')
    $("#globalUrlInputPC").html('Only 500 characters are allowed (500 left)')
    let url = $('#URLlink').val();
    let urlName = $('#URLName').val();
    let urlId = $('#URLId').val();
    let urlUpdateObj = new Object();
    urlUpdateObj.localUrlLink = url;
    urlUpdateObj.localUrlName = urlName;
    urlUpdateObj.localUrlId = urlId;
    urlUpdateObj.projectID = localStorage.getItem("views_project_id");
    urlUpdateObj.actionType = "update";
    urlUpdateObj.localUrlStatus = "true";

    let urlAddObj = new Object();
    urlAddObj.localUrlLink = url;
    urlAddObj.localUrlName = urlName;
    urlAddObj.projectID = localStorage.getItem("views_project_id");


    if (urlId != "") {
        updateURL(urlUpdateObj, 'updateURL');
    }

    else {
        addURL(urlAddObj);
    }

}

// Adds new URL into the table
function addURL(urlAddObj) {
    //let service_java_URL = 'http://:8080/isf-rest-server-java/';
    let newURL = service_java_URL + "activityMaster/saveLocalUrl";
    pwIsf.addLayer({ text: 'Please wait ...' });
    $.isf.ajax({
        url: newURL,
        type: "POST",
        contentType: 'application/json',
        data: JSON.stringify(urlAddObj),
        complete: function (xhr, status) {
            pwIsf.removeLayer();
            if (status == "success") {
                if (xhr.responseJSON.isValidationFailed) {
                    responseHandler(xhr.responseJSON);
                }
                else {
                    pwIsf.alert({ msg: "URL added", type: "success" })
                    resetURL();
                    getAllURL();
                }

            }

        },
        error: function (data) {
            console.log("Error");
            pwIsf.removeLayer();
        }
    });
}

// Updates the existing URL into the table
function updateURL(urlUpdateObj, action) {
    let editUrl = service_java_URL + "activityMaster/updateLocalUrl";
    let statusChanged = false;
    pwIsf.addLayer({ text: 'Please wait ...' });
    $.isf.ajax({
        url: editUrl,
        type: "POST",
        async: false,
        contentType: 'application/json',
        data: JSON.stringify(urlUpdateObj),
        complete: function (xhr, status) {
            pwIsf.removeLayer();
            if (status == "success") {
                if (xhr.responseJSON.isValidationFailed) {
                    responseHandler(xhr.responseJSON);
                }
                else {
                    if (action == 'updateURL') {
                        pwIsf.alert({ msg: "URL updated", type: "success" });
                        resetURL();
                        getAllURL();
                    }
                    statusChanged = true;
                }
            }
        },
        error: function (data) {
            console.log("Error");
            pwIsf.removeLayer();
        }
    });
    return statusChanged;
}

// Reset the activities when clicked the RESET button
function resetURL() {
    $("#topDiv").removeClass('updateCss');
    $('#URLName').val("").trigger('change');
    $('#URLlink').val("").trigger('change');
    $('#URLId').val("").trigger('change');
    $('#addUrl').text('ADD URL');
    $('#resetUrlDiv').css('padding-left', '0px');

}

/******************** Input Output URL Access Configuration Ends *************************/



/***************************Parallel Task Start**************************/
function getParallelTaskConfiguration() {
    $('#panelBodyOfParallelTaskWait').html(pleaseWaitISF());
    if (projectID && signumGlobal) {
        $.isf.ajax({
            url: `${service_java_URL}activityMaster/getMaxTasksValueByProject/${projectID}/${signumGlobal}`,
            success: function (data) {
                $('#maxManualTasks').val(data.MaxManualTask);
                $('#maxAutomaticTasks').val(data.MaxAutomaticTask);
                $('#panelBodyOfParallelTaskWait').hide();
                showParallelTaskLimitText(); //show limit msg to users
                $('#panelBodyOfParallelTask').show();
            },
            error: function (xhr, status, statusText) {
                //BLANK
            }
        });
    }
    else {
        if (!projectID) {
            pwIsf.alert({ msg: 'Project ID Missing', type: 'error' });
        }
        else if (!signumGlobal) {
            pwIsf.alert({ msg: 'Signum Missing', type: 'error' });
        }
        else {
            pwIsf.alert({ msg: 'API Parameters Missing', type: 'error' });
        }
    }
}

function updateParallelTaskConfiguration() {
    $('#panelBodyOfParallelTaskWaitRow').html(pleaseWaitISF({ text: 'Please wait' })).show();
    $('#btnConfigureTasks').attr('disabled', true);
    var manualNum = $('#maxManualTasks').val();
    var automaticNum = $('#maxAutomaticTasks').val();

    $.isf.ajax({
            url: `${service_java_URL}activityMaster/updateMaxTasksValueByProject/${projectID}/${signumGlobal}/${manualNum}/${automaticNum}`,
            crossdomain: true,
            contentType: C_CONTENT_TYPE_APPLICATION_JSON,
            type: C_API_POST_REQUEST_TYPE,
            success: function (data) {
                if (data.updateFlag) {
                    getParallelTaskConfiguration();
                    $('#panelBodyOfParallelTaskWaitRow').hide();
                    $('#btnConfigureTasks').attr('disabled', false);
                } else {
                    $('#panelBodyOfParallelTaskWaitRow').html(data.msg);
                }
            }, error: function (xhr, status, statusText) {
                alert("Invalid parameters !!!");
            }
    });
    
}

// Automatic and Manual Task limit Validation check
function validationForParallelTaskConfig() {

    const enteredManual = $('#maxManualTasks').val();
    const enteredAuto = $('#maxAutomaticTasks').val();

    // limit on both manual and auto
    if (manualStepExeLimit !== 0 && autoStepExeLimit !== 0) {
        limitBothManualAndAutoTask(enteredManual, enteredAuto);
    }
    // limit on manual only
    else if (manualStepExeLimit !== 0 && autoStepExeLimit === 0) {
        if (manualStepExeLimit < enteredManual) {
            pwIsf.alert({ msg: warningForManualOnly, type: WARNINGTEXT });
        }
        else {
            updateParallelTaskConfiguration();
        }
    }
    // limit on auto only
    else if (manualStepExeLimit === 0 && autoStepExeLimit !== 0) {
         if (autoStepExeLimit < enteredAuto) {
            pwIsf.alert({ msg: warningForAutoOnly, type: WARNINGTEXT });
         }
        else {
            updateParallelTaskConfiguration();
         }
    }
    // no limit on manual and auto
    else {
        updateParallelTaskConfiguration();
    }
}

// validation on both manual and auto tasks
function limitBothManualAndAutoTask(enteredManual, enteredAuto) {
    // manual task limit violation
    if (manualStepExeLimit < enteredManual && autoStepExeLimit >= enteredAuto) {
        pwIsf.alert({ msg: warningForManualOnly, type: WARNINGTEXT });
    }
    // auto task limit violation
    else if (manualStepExeLimit >= enteredManual && autoStepExeLimit < enteredAuto) {
        pwIsf.alert({ msg: warningForAutoOnly, type: WARNINGTEXT });
    }
    // both manual and auto task limit violation
    else if (manualStepExeLimit < enteredManual && autoStepExeLimit < enteredAuto) {
        pwIsf.alert({ msg: warningForManualAndAuto, type: WARNINGTEXT });
    }
    // no limit violation
    else {
        updateParallelTaskConfiguration();
    }
}

// Info text to identify users about limit
function showParallelTaskLimitText() {
    const manualCountFromConfig = parseInt(MANUAL_STEP_EXE_DEFAULT_VALUE);
    const autoCountFromConfig = parseInt(AUTOMATIC_STEP_EXE_DEFAULT_VALUE);
    const msgForNoLimit = 'NA';

    // show info when limit is given on any task or both tasks
    if (manualCountFromConfig >= 0 && autoCountFromConfig >= 0) {
        if (manualCountFromConfig === 0 && autoCountFromConfig === 0) {
            $(infoForParallelLimitID).hide();
        }
        else {
            if (manualCountFromConfig === 0 && autoCountFromConfig > 0) {
                $('#limitManualTask').text(msgForNoLimit);
                $('#limitAutoTask').text(autoCountFromConfig);
            }
            else if (manualCountFromConfig > 0 && autoCountFromConfig === 0) {
                $('#limitManualTask').text(manualCountFromConfig);
                $('#limitAutoTask').text(msgForNoLimit);
            }
            else {
                $('#limitManualTask').text(manualCountFromConfig);
                $('#limitAutoTask').text(autoCountFromConfig);
            }
            $(infoForParallelLimitID).show();
        }
    }
    // hide info when no limit given for any or both tasks
    else {
        $(infoForParallelLimitID).hide();
    }
}
/***************************Parallel Task End**************************/

/***************************DeliveryAcceptance Start**************************/
function getUserSignumsDA() {

    pwIsf.addLayer({ text: "Please wait ..." });

    $.isf.ajax({
        url: service_java_URL + "accessManagement/getUserDetailsForDR/2",
        success: function (data) {

            pwIsf.removeLayer();
            $('#signumDA').empty();
            $('#signumDA').append('<option value="0" disabled selected>Select One</option>');
            $.each(data, function (i, d) {
                var sig = [];
                sig = d + " ";
                $('#signumDA').append('<option value="' + sig + '">' + sig + '</option>');
            });
        },
        error: function (xhr, status, statusText) {
            pwIsf.removeLayer();
            pwIsf.alert({ msg: "Signum does not exist !!!", type: "warning" });

        }

    });
}

function addDeliveryAcceptance() {

    let sig = $("#signumDA").val().split('(')[0];
    let createdBy = signumGlobal;
    let serviceData = new Object();
    serviceData.projectID = projectID;
    serviceData.signumID = sig;
    serviceData.createdBy = createdBy;
    var ulrService = service_java_URL + "projectManagement/saveDeliveryAcceptance"
    if (sig == null || sig == "") {
        pwIsf.alert({ msg: "Please select Signum", type: "warning" });
        return;
    }
    $.isf.ajax({

        url: ulrService,
        context: this,
        crossdomain: true,
        processData: true,
        contentType: 'application/json',
        type: 'POST',
        data: JSON.stringify(serviceData),
        xhrFields: {
            withCredentials: false
        },
        success: function (data) {
            if (data) {
                pwIsf.alert({ msg: "Signum already exist", type: "warning" });
            }
            else { getDeliveryAcceptanceByProject(); }
        }, error: function (xhr, status, statusText) {
            pwIsf.alert({ msg: "Invalid parameters !!!", type: "warning" });
        }

    });

}

function getDeliveryAcceptanceByProject() {
    $('#TDeliveryAcceptance tr').remove();
    pwIsf.addLayer({ text: "Please wait ..." });
    if (projectID) {
        $.isf.ajax({
            url: service_java_URL + "projectManagement/getDeliveryAcceptance/" + parseInt(projectID),
            success: function (data) {
                pwIsf.removeLayer();

                var html = "";
                $.each(data, function (i, d) {
                    var signum = d.EmployeeSignum.split("(");
                    html = "<tr>";
                    html = html + '<td><a href="#" style="color:blue;" onclick="getEmployeeDetailsBySignum(\'' + signum[0] + '\');">' + d.EmployeeSignum + '</a></td>';
                    html = html + "</td>";
                    html = html + "<td><button class='fa fa-trash-o' style='border:none' onclick='deleteDeliveryAcceptance(" + d.DeliveryAcceptanceID + ")'></button>";
                    html = html + "</tr>";

                    //append to table.
                    $("#TDeliveryAcceptance").append(html);

                    if (localStorage.getItem("IsProjectTabsAccessible") == 0 && signumGlobal.toString().toLowerCase() == d.signumID.toLowerCase()
                        && JSON.parse(ActiveProfileSession).role != "Resource Planning Manager") {
                        $("#nav_barProjectView").attr('style', '');
                        $("#home").attr('style', '');
                        localStorage.setItem("IsProjectTabsAccessible", 1);
                        activaTab('project_others');
                    }
                });


            },
            error: function (xhr, status, statusText) {
                pwIsf.removeLayer();

            }
        });

    }

    else {
        pwIsf.alert({ msg: "Project ID missing", type: "error" });
    }

}

function deleteDeliveryAcceptance(id) {
    var serviceUrl = service_java_URL + "projectManagement/disableDeliveryAcceptance?deliveryAcceptanceID=" + id + "&signumID=" + signumGlobal;
    if (ApiProxy == true) {

        serviceUrl = service_java_URL + "projectManagement/disableDeliveryAcceptance?deliveryAcceptanceID=" + encodeURIComponent(id + "&signumID=" + signumGlobal);
    }

    $.isf.ajax({
        url: serviceUrl,
        type: 'POST',
        success: function (data) {
            pwIsf.alert({ msg: "Delivery Acceptance successfully deleted!!!", type: "info" });
            getDeliveryAcceptanceByProject(projectID);
        },
        error: function (xhr, status, statusText) {
            pwIsf.alert({ msg: "Operation failed to perform", type: "error" });

        }

    });


}

/***************************DeliveryAcceptance End**************************/

/***************************RPM Start**************************/
function getUserSignumsRPM() {

    pwIsf.addLayer({ text: "Please wait ..." });

    $.isf.ajax({
        url: service_java_URL + "accessManagement/getRPMByMarket",
        success: function (data) {

            pwIsf.removeLayer();
            $('#signumRPM').empty();
            $('#signumRPM').append('<option value="0" disabled selected>Select One</option>');
            $.each(data, function (i, d) {
                var sig = [];
                sig = d + " "
                $('#signumRPM').append('<option value="' + sig + '">' + sig + '</option>');
            })
        },
        error: function (xhr, status, statusText) {
            pwIsf.removeLayer();
            pwIsf.alert({ msg: "Signum does not exist !!!", type: "warning" });

        }

    });
}

function addRPM() {

    let sig = $("#signumRPM").val().split('(')[0];
    let createdBy = signumGlobal;
    let serviceData = new Object();
    serviceData.projectID = projectID;
    serviceData.rpmSignumID = sig;
    serviceData.createdBy = createdBy;
    var ulrService = service_java_URL + "projectManagement/saveRPM"
    if (sig == null || sig == "") {
        pwIsf.alert({ msg: "Please select Signum", type: "warning" });
        return;
    }
    $.isf.ajax({

        url: ulrService,
        context: this,
        crossdomain: true,
        processData: true,
        contentType: 'application/json',
        type: 'POST',
        data: JSON.stringify(serviceData),
        xhrFields: {
            withCredentials: false
        },
        success: function (data) {
            if (data) {
                pwIsf.alert({ msg: "Signum already exist", type: "warning" });
            }
            else {
                getRPMByProject();
            }
        }, error: function (xhr, status, statusText) {
            pwIsf.alert({ msg: "Invalid parameters !!!", type: "warning" });
        }

    });

}

function getRPMByProject() {
    $('#TbodyRPM tr').remove();
    pwIsf.addLayer({ text: "Please wait ..." });
    if (projectID) {
        $.isf.ajax({
            url: service_java_URL + "projectManagement/getRPM/" + parseInt(projectID),
            success: function (data) {
                pwIsf.removeLayer();

                var html = "";
                $.each(data, function (i, d) {
                    var signum = d.EmployeeSignum.split("(");
                    html = "<tr>";
                    html = html + '<td><a href="#" style="color:blue;" onclick="getEmployeeDetailsBySignum(\'' + signum[0] + '\');">' + d.EmployeeSignum + '</a></td>';
                    html = html + "</td>";
                    html = html + "<td><button class='fa fa-trash-o' style='border:none' onclick='deleteRPM(" + d.RPMID + ")'></button>"
                    html = html + "</tr>";

                    //append to table.
                    $("#TbodyRPM").append(html);

                    if (localStorage.getItem("IsProjectTabsAccessible") == 0 && signumGlobal.toString().toLowerCase() == d.signumID.toLowerCase()
                        && JSON.parse(ActiveProfileSession).role != "Resource Planning Manager") {
                        $("#nav_barProjectView").attr('style', '');
                        $("#home").attr('style', '');
                        localStorage.setItem("IsProjectTabsAccessible", 1);
                        activaTab('project_others');
                    }
                })


            },
            error: function (xhr, status, statusText) {
                pwIsf.removeLayer();

            }
        });
    }

    else {
        pwIsf.alert({ msg: "Project ID missing", type: "error" });
    }

}

function deleteRPM(id) {
    let serviceURL = service_java_URL + "projectManagement/disableRPM?rpmID=" + id + "&signumID=" + signumGlobal;
    if (ApiProxy == true) {
        serviceURL = service_java_URL + encodeURIComponent("projectManagement/disableRPM?rpmID=" + id + "&signumID=" + signumGlobal);
    }
    $.isf.ajax({
        url: serviceURL,
        headers: commonHeadersforAllAjaxCall,
        type: 'POST',
        success: function (data) {
            pwIsf.alert({ msg: "RPM successfully deleted!!!", type: "info" });
            getRPMByProject(projectID);
        },
        error: function (xhr, status, statusText) {
            pwIsf.alert({ msg: "Operation failed to perform", type: "error" });

        }

    });


}

/***************************RPM End**************************/


/***************************DeliveryResponsible Start**************************/
function getDeliveryResponsibleByProject() {
    $('#TDeliveryRes tr').remove();
    pwIsf.addLayer({ text: C_PLEASE_WAIT });
    showDRLimitText();
    if (projectID === undefined || projectID == null || projectID === '' || isNaN(parseInt(projectID))) {
        console.log(wrongProjIDMsg);
        pwIsf.alert({ msg: wrongProjIDMsg, type: ERRORTEXT });
    }
    else {
        $.isf.ajax({
            url: `${service_java_URL}projectManagement/getDeliveryResponsibleByProject/${parseInt(projectID)}`,
            success: function (data) {
                pwIsf.removeLayer();
                var html = "";
                countOfTotalDR = data.length;
                if (countOfTotalDR !== 0) {
                    $(totalDRAccess).val(countOfTotalDR);
                }
                else {
                    $(totalDRAccess).val(0);
                }
                $.each(data, function (i, d) {
                    var signum = d.deliveryResponsible.split("(");
                    html = `$html}<tr>`;
                    html = `${html}<td><a href="#" style="color:blue;"
                    onclick="getEmployeeDetailsBySignum(${signum[0]});">${d.deliveryResponsible}</a></td>`;
                    html = `${html}</td>`;
                    html = `${html}<td><button class='fa fa-trash-o' style='border:none'
                    onclick='deleteDeliveryResponsible(${d.deliveryResponsibleID})'></button>`;
                    html = `${html}</tr>`;
                    $("#TDeliveryRes").append(html);
                    localStorage.setItem("DeliveryResponsible", d.signumID);
                    if (localStorage.getItem("IsProjectTabsAccessible") === 0 &&
                        signumGlobal.toString().toLowerCase() === d.signumID.toLowerCase() &&
                        JSON.parse(ActiveProfileSession).role !== profileRPM) {
                        $("#nav_barProjectView").attr('style', '');
                        $("#home").attr('style', '');
                        localStorage.setItem("IsProjectTabsAccessible", 1);
                        activaTab('project_others');
                    }
                });
            },
            error: function (xhr, status, statusText) {
                pwIsf.removeLayer();
            }
        });
    }
}

function getUserSignums() {
    pwIsf.addLayer({ text: "Please wait ..." });

    $.isf.ajax({
        url: service_java_URL + "accessManagement/getUserDetailsForDR/5,15",
        success: function (data) {
            pwIsf.removeLayer();
            $('#signumDR').empty();
            $('#signumDR').append('<option value="0" disabled selected>Select One</option>');
            $.each(data, function (i, d) {
                var sig = [];
                sig = d + " ";
                $('#signumDR').append('<option value="' + sig + '">' + sig + '</option>');
            });
        },
        error: function (xhr, status, statusText) {
            pwIsf.removeLayer();
            alert("Signum does not exist !!!");
        }

    });
}

function addDeliveryResponsible() {

    var sig = $(signumDRId).val();
    var createdBy = signumGlobal;
    var service_data = "{\"deliveryResponsible\":\"" + sig + "\",\"projectID\":\"" + projectID + "\",\"createdBy\":\"" + createdBy + "\"}";
    var ulrService = service_java_URL + "projectManagement/addDeliveryResponsible";
    
    $.isf.ajax({
        url: ulrService,
        context: this,
        crossdomain: true,
        processData: true,
        contentType: 'application/json',
        type: 'POST',
        data: service_data,
        xhrFields: {
            withCredentials: false
        },
        success: function (data) {

            if (data.isValidationFailed == true) {
                pwIsf.alert({ msg: data.formErrors[0], type: 'warning' });
            }
            else {
                pwIsf.alert({ msg: data.formMessages[0], type: 'success', autoClose: 3 });
                getDeliveryResponsibleByProject();
            }

        }, error: function (xhr, status, statusText) {
            pwIsf.alert({ msg: "Failed to add Delivery Responsible.", type: 'error' });
        }

    });

}

function deleteDeliveryResponsible(id) {
    $.isf.ajax({
        url: service_java_URL + "accessManagement/deleteDeliveryResponsible/" + id + "/" + signumGlobal,
        type: 'POST',
        success: function (data) {
            pwIsf.alert({ msg: "Delivery Responsible successfully deleted!!!", type: "info" });
            getDeliveryResponsibleByProject(projectID);

        },
        error: function (xhr, status, statusText) {
            pwIsf.alert({ msg: "Operation Failed to perform!!!", type: "info" });
        }

    });


}

// check limit validation for DR Access
function checkDRLimitValidation() {
    const totalDROnProj = parseInt($(totalDRAccess).val());
    const enteredSignum = $(signumDRId).val();

    if (enteredSignum == null || enteredSignum === "") {
        alert(emptySignumMsg);
    }
    else if (limitValForDRAccess >= 0) {
        if (limitValForDRAccess === 0 ||
            totalDROnProj < limitValForDRAccess) {
            addDeliveryResponsible();
        }
        else {
            pwIsf.alert({ msg: limitExceedDRMsg, type: C_WARNING });
        }
    }
    else {
        pwIsf.alert({ msg: negativeDRLimitMsg, type: C_WARNING });
    }
}

// check limit of DR from config to show/hide text
function showDRLimitText() {
    if (limitValForDRAccess >= 0) {
        if (limitValForDRAccess === 0) {
            $(lblLimitDR).hide();
        }
        else {
            $(limitValueDRId).text(limitValForDRAccess);
            $(lblLimitDR).show();
        }
    }
    else {
        $(lblLimitDR).hide();
    }
}
/***************************DeliveryResponsible End**************************/

/*----Project Mapping Starts------*/

/*---------------Get Sources in dropdown-----------------------*/

function getExternalSources() {
    $('#select_ext_source').empty();
    $.isf.ajax({
        type: "GET",
        url: service_java_URL + "woManagement/getSourcesForMapping/", //getSourcesForMapping 
        success: appendExternalSources,
        error: function (msg) {

        },
        complete: function (msg) {
            getExternalSourceTable();
        }
    });

    function appendExternalSources(data) {
        externalSourceDetails = data;
        var html = "";
        $('#select_ext_source').append('<option value="0">Select One</option>');
        $.each(data, function (i, d) {
            $('#select_ext_source').append('<option value="' + d.sourceId + '">' + d.sourceName + '</option>');

        });
    }
}


function GetExternalProjectId() {

    $("#ext_source_reference").val('');
    $('#ext_source_reference_list').html('');
    var sourceID = $("#select_ext_source").val();
    $.isf.ajax({
        type: "GET",
        url: service_java_URL + "externalInterface/getExternalProjectID/" + sourceID,
        success: function (data) {
            $('#ext_source_reference_list').append('<option value ="" ></option>');
            if (data !== null && data !== undefined) {
                if (data.isValidationFailed === false) {
                    if (data.responseData.length > 0) {
                        $.each(data.responseData, function (i, d) {
                            $('#ext_source_reference_list').append('<option data-id="' + d.ExternalProjectID + '" value="' + d.ParentProjectName + "/" + d.ExternalProjectID + '">');

                        });
                    }
                }
                else {
                    pwIsf.alert({ msg: data.formErrors[0], type: 'error' });
                }
            }
        },
        error: function (msg) {

        }
    });

}

/*---------------Get Projects in dropdown-----------------------*/

function getExternalProjects() {
    $.isf.ajax({
        type: "GET",
        url: service_java_URL + "projectManagement/getExternalProjects/", //getSourcesForMapping 
        success: appendExternalProjects,
        error: function (msg) {

        },
        complete: function (msg) {

        }
    });

    function appendExternalProjects(data) {
        externalSourceDetails = data;
        var html = "";
        $.each(data, function (i, d) {
            $('#ext_source_reference_id').append('<option value="' + d.ProjectID + '">' + d.ProjectID + '-' + d.ProjectName + '</option>');
        });
    }
}

/*--------------Add New External Source------------------------*/

function addExternalSource() {
    if (validateExternalSource() == true) {
        pwIsf.addLayer({ text: 'Please wait ...' });
        var projectID = localStorage.getItem("views_project_id");
        var isActive = false;
        externalProjectSource = $("#select_ext_source option:selected").val();
        var createdBy = signumGlobal;
        var today = new Date();
        var createdOn = today.toISOString().substring(0, 10);
        var externalReferenceID = $('#ext_source_reference').val();
        var projectArray = externalReferenceID.split('/');
        var arrlen = projectArray.length;
        var projectName = projectArray[0];
        var ProjectID = projectArray[arrlen - 1];
        var arrayAddSourceObj = [];


        var addSourceObj = new Object();
        addSourceObj.sourceId = externalProjectSource;
        addSourceObj.externalProjectId = ProjectID;
        addSourceObj.createdby = createdBy;
        addSourceObj.isactive = isActive;
        addSourceObj.projectId = projectID;
        arrayAddSourceObj.push(addSourceObj);

        var JsonObj = JSON.stringify(arrayAddSourceObj);
        $.isf.ajax({
            type: "POST",
            url: service_java_URL + "projectManagement/addExternalApplicationReference/",
            dataType: 'json',
            processData: true,
            crossDomain: true,
            contentType: "application/json; charset=utf-8",
            data: JsonObj,
            success: function (data) {
                if (data.isValidationFailed) {
                    //errorHandler(data);
                    pwIsf.alert({ msg: data.formErrors[0], type: 'error' });
                }
                else {
                    cleanSourceFields();
                    pwIsf.alert({ msg: data.formMessages[0], type: 'success' });
                }
            },
            error: function (xhr, status, statusText) {
            },
            complete: function (xhr, statusText) {
                getExternalSourceTable();
                pwIsf.removeLayer();
            }
        });
    }
}

/*--------------Draw table on GET of DB data------------------------*/

function getExternalSourceTable() {
    var ProjectID = localStorage.getItem("views_project_id");

    if ($.fn.dataTable.isDataTable('#externalSourceBody')) {
        oTableSource.destroy();
        $("#externalSourceBody").empty();
    }

    var request = $.isf.ajax({
        url: service_java_URL + "projectManagement/getExternalApplicationReferencesByProjectId?projectId=" + ProjectID,
        returnAjaxObj: true,
        success: function (data) {
        },
    });

    request.done(function (data) {
        $.each(data, function (i, d) {
            var isActive = d.isactive;
            if (!isActive)
                d.actionIcon = '<label class="switchSource"><input type="checkbox" class="toggleActive' + d.referenceId + '" onclick="toggleSourceStatus(' + d.referenceId + ')" id="togBtnSource' + d.referenceId + '"><div class="sliderSource round"><span class="onSource">Enabled</span><span class="offSource">Disabled</span></div></label>';
            else
                d.actionIcon = '<label class="switchSource"><input type="checkbox" checked class="toggleActive' + d.referenceId + '" onclick="toggleSourceStatus(' + d.referenceId + ')" id="togBtnSource' + d.referenceId + '"><div class="sliderSource round"><span class="offSource">Disabled</span><span class="onSource">Enabled</span></div></label>';

        });
        $("#externalSourceBody").append($('<tfoot><tr><th></th><th>Source</th><th>Reference ID</th><th>Added By</th><th>Added On</th></tr></tfoot>'));
        oTableSource = $("#externalSourceBody").DataTable({
            searching: true,
            colReorder: true,
            responsive: true,
            "pageLength": 10,
            "data": data,
            "destroy": true,
            order: [1],
            dom: 'Bfrtip',
            buttons: [
                'colvis', 'excel'
            ],
            "columns": [{ "title": "Action", "targets": 'no-sort', "orderable": false, "searchable": false, "data": "actionIcon" },
            { "title": "Source", "data": "sourcename" }, { "title": "Reference ID (External Project ID)", "data": "externalProjectId" },
            { "title": "Added By", "data": "createdby" }, { "title": "Added On", "data": "createdOn" }],
            initComplete: function () {

                $('#externalSourceBody tfoot th').each(function (i) {
                    var title = $('#externalSourceBody thead th').eq($(this).index()).text();
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

        $('#externalSourceBody tfoot').insertAfter($('#externalSourceBody thead'));


    });

    request.fail(function (jqXHR, textStatus) {
    });

}

/*--------------Validate Required fields while adding Source------------------------*/

function validateExternalSource() {
    var OK = true;
    $("#select_ext_source-Required").text("");
    if ($("#select_ext_source").val() == 0) {
        $('#select_ext_source-Required').text("Source is required");
        OK = false;
    }

    if ($('#ext_source_reference').val().indexOf('/') != -1) {
        OK = true;
    }
    else {
        if ($('#ext_source_reference').val().indexOf('/') === -1) {
            $('#ext_source_reference_id-Required').text("Please use / between Project Name and Project ID");
            OK = false;
        }
    }


    return OK;
}

/*---------------Reset fields after adding External Source-----------------------*/

function cleanSourceFields() {
    $("#select_ext_source").val("");
    $("#ext_source_reference_id").val("");
    $("#ext_source_reference").val("");
}

/*---------------Enable Disable Source-----------------------*/

function toggleSourceStatus(referenceID) {
    var checkbox = document.querySelector('input[id="togBtnSource' + referenceID + '"]');

    $("#externalSourceBody").off("change").on("change", checkbox, function (e) {

        if (checkbox.checked) {
            var serviceUrl = service_java_URL + "projectManagement/updateStatusOfExternalReference?isActive=true&referenceId=" + referenceID;
            if (ApiProxy == true) {
                serviceUrl = service_java_URL + encodeURIComponent("projectManagement/updateStatusOfExternalReference?isActive=true&referenceId=" + referenceID);
            }
            $.isf.ajax({

                url: serviceUrl,
                type: 'GET',
                success: activesInactive(),
                error: function (xhr, status, statusText) {
                },
                complete: function (xhr, statusText) {
                    pwIsf.removeLayer();
                }


            });
            function activesInactive() {
                pwIsf.alert({ msg: 'Source Enabled successfully!', type: 'success' });
            }

        } else {
            var serviceUrl = service_java_URL + "projectManagement/updateStatusOfExternalReference?isActive=false&referenceId=" + referenceID;
            if (ApiProxy == true) {
                serviceUrl = service_java_URL + encodeURIComponent("projectManagement/updateStatusOfExternalReference?isActive=false&referenceId=" + referenceID);
            }
            $.isf.ajax({
                url: serviceUrl,
                type: 'GET',
                success: activesInactive(),
                error: function (msg) {

                },
                complete: function (msg) {

                }
            });
            function activesInactive() {
                pwIsf.alert({ msg: 'Source Disabled successfully!', type: 'success' });
            }

        }
    });

}

/*----Project Mapping Ends------*/

/*--------------------- Add New Tool ----------------------*/
function addToolInfo() {
    if (validateToolInfoFields() == true) {
        pwIsf.addLayer({ text: 'Please wait ...' });

        let projectID = localStorage.getItem("views_project_id");
        let toolName = $("#select_tool_name option:selected").text();
        let toolID = $("#select_tool_name option:selected").val();
        let licenseOwner = $("#select_license_owner option:selected").val();
        let accessMethod = $("#select_access_method option:selected").val();
        let toolVersion = $('#release_version').val();
        let createdBy = signumGlobal;
        let today = new Date();
        let createdDate = today.toISOString().substring(0, 10)

        let addSourceObj = new Object();
        addSourceObj.projectID = projectID;
        addSourceObj.toolName = toolName;
        addSourceObj.createdBy = createdBy;
        addSourceObj.toolID = toolID;
        addSourceObj.licenseOwnerID = licenseOwner;
        addSourceObj.accessMethodID = accessMethod;
        addSourceObj.toolVersion = toolVersion;
        addSourceObj.createdDate = createdDate;



        var JsonObj = JSON.stringify(addSourceObj);

        $.isf.ajax({
            type: "POST",
            url: service_java_URL + "projectManagement/saveProjectSpecificTools",
            dataType: 'json',
            processData: true,
            crossDomain: true,
            contentType: "application/json; charset=utf-8",
            data: JsonObj,
            success: function (data) {
                if (data) {
                    pwIsf.alert({ msg: "Tool added successfully", type: 'success', autoClose: 3 });
                    getToolInfoTable();

                }
                else {
                    pwIsf.alert({ msg: "Tool already exist", type: 'warning', autoClose: 3 });

                }
            },
            error: function (xhr, status, statusText) {
            },
            complete: function (xhr, statusText) {

                pwIsf.removeLayer();
            }
        });
    }
}

/*--------------Draw table on GET of DB data------------------------*/
function getToolInfoTable() {

    if ($.fn.dataTable.isDataTable('#toolInfoBody')) {
        oTableSource.destroy();
        $("#toolInfoBody").empty();
    }
    let projectID = localStorage.getItem('views_project_id')
    if (projectID) {
        $.isf.ajax({
            url: service_java_URL + "projectManagement/getProjectSpecificTools?projectID=" + projectID,
            success: function (data) {
                data = data.responseData;
                $.each(data, function (i, d) {

                    var isActive = d.Active;
                    if (isActive) {
                        d.actionIcon = '<label class="switchSource"><input type="checkbox" checked class="toggleActive" onclick="toggleToolStatus(' + d.ToolID + ', ' + d.ProjectToolID + ')" id="togBtnSource_' + d.ToolID + '" /><div class="sliderSource round"><span class="onSource">Enabled</span><span class="offSource">Disabled</span></div></label>';
                    }
                    else {
                        d.actionIcon = '<label class="switchSource"><input type="checkbox" class="toggleActive"onclick="toggleToolStatus(' + d.ToolID + ', ' + d.ProjectToolID + ')" id="togBtnSource_' + d.ToolID + '"/><div class="sliderSource round"><span class="onSource">Enabled</span><span class="offSource">Disabled</span></div></label>';

                    }

                });
                $("#toolInfoBody").append($('<tfoot><tr><th></th><th></th><th>Tool Name</th><th>License Owner</th><th>Access Method</th><th>Release/Version</th></tr></tfoot>'));
                oTableSource = $("#toolInfoBody").DataTable({
                    searching: true,
                    colReorder: true,
                    responsive: true,
                    "pageLength": 10,
                    "data": data,
                    "destroy": true,
                    order: [1],
                    dom: 'Bfrtip',
                    buttons: [
                        'colvis', 'excel'
                    ],
                    "columns": [{
                        "title": "Action", "targets": 'no-sort', "orderable": false, "searchable": false, "data": null,
                        "render": function (data, type, row, meta) {
                            jsonData = JSON.stringify({
                                toolID: data.ToolID, tool: data.Tool, active: data.Active, licenseType: data.ToolLicenseTypeID, accessMethod: data.AccessMethodID, licenseOwner: data.ToolLicenseOwnerID, release_version: data.ToolVersion, projectToolID: data.ProjectToolID
                            });
                            if (data.Active) {

                                return '<div style="display:flex"><a href="#edittoolname" class="icon-edit" title="Edit Tool Kit " data-details= \'' + jsonData + '\' data-toggle="modal"  onclick="updateToolMethod(this)">' + getIcon('edit') + '</a>';
                            }
                            else {
                                return '<div style="display:none"><a href="#edittoolname" class="icon-edit" title="Edit Tool Kit " data-details= \'' + jsonData + '\' data-toggle="modal"  onclick="updateToolMethod(this)">' + getIcon('edit') + '</a>';
                            }
                        }
                    },
                    { "title": "Status", "targets": 'no-sort', "orderable": false, "searchable": false, "data": "actionIcon" },
                    { "title": "Tool Name", "data": "Tool" },
                    { "title": "License Owner", "data": "LicenseOwner" }, { "title": "Access Method", "data": "AccessMethod" },
                    { "title": "Release/Version", "data": "ToolVersion" }],
                    initComplete: function () {

                        $('#toolInfoBody tfoot th').each(function (i) {
                            var title = $('#toolInfoBody thead th').eq($(this).index()).text();
                            if (title != "Action" && title != "Status")
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

                $('#toolInfoBody tfoot').insertAfter($('#toolInfoBody thead'));

            },
            error: function (jqXHR, textStatus) {
            }

        });
    }

    else {

        pwIsf.alert({ msg: "Project ID missing", type: "error" });
    }
}

/*---------------Validate Tool Info Fields-----------------*/

function validateToolInfoFields() {
    var OK = true;

    $("#select_tool_name-Required").text("");
    if ($("#select_tool_name").val() == null) {
        $('#select_tool_name-Required').text("Tool Name is required");
        OK = false;
    }


    $("#license_owner_id-Required").text("");
    if ($("#select_license_owner").val() == null) {
        $('#license_owner_id-Required').text("License Owner is required");
        OK = false;
    }
    $("#access_method_id-Required").text("");
    if ($("#select_access_method").val() == null) {
        $('#access_method_id-Required').text("Access Method is required");
        OK = false;
    }



    return OK;
}

/*---------------Update Tool Info  Modal-----------------------------*/

function updateToolMethod(thisObj) {
    let jsonData = $(thisObj).data('details');
    let isActive = false;
    let status = $(thisObj).closest('tr').find('input[type="checkbox"]').val();
    if (status == "on") { isActive = true }
    var html = `
               <div class="col-lg-9" style="padding-top:10px">
                   <label class="" id="editToolName" name="toolname" style="width:100%" value="${jsonData.tool}">Tool Name: ${jsonData.tool}</label>
                    <input type="hidden" id="license_Type" name="licenseType" value="test1" />
                    <input type="hidden" id="lastmodifiedby" name="lastmodifiedBy" value="${jsonData.signumGlobal}" />
                    <input type="hidden" id="toolID1" name="roolID" value="${jsonData.toolID}" />
                    <input type="hidden" id="activeTool" name="toolStatus" value="${isActive}" />
                    <input type="hidden" id="projectToolId" name="toolStatus" value="${jsonData.projectToolID}" />
                        
                    </div>
 
                     <div class="col-lg-9">
                     License Owner<a class="text-danger">*</a>: <select id="editLicenseOwner" class="select2able select2-offscreen" required>
                    </select>
                    </div>

                      <div class="col-lg-9">
                      Access Method<a class="text-danger">*</a>: <select id="editAccessMethod" class="select2able select2-offscreen" required></select>
                    </div>

                    <div class="col-lg-8">
                     Release/Version: <input class="form-control" name="toolversion" type="text" id="editVersion" value="${jsonData.release_version}" />
                    </div>

                    <div class="col-lg-8">
                       <button id="btnUpdateToolKitName" type="submit" onclick="updatetoolfinal()" pull-right class="btn btn-primary">Update Tool</button>
                        
                    </div>`;

    $('#updatetoolmodal').html('').append(html);

    $.each(LicenseOwnerArray, function (i, d) {
        $('#editLicenseOwner').append('<option value="' + d.ToolLicenseOwnerID + '">' + d.LicenseOwner + '</option>');
    });

    var wantToFind = jsonData.licenseOwner;
    console.log(wantToFind);
    var dd = document.getElementById('editLicenseOwner');
    console.log(dd);
    var it;
    for (it = 0; it < dd.options.length; it++) {
        if (dd.options[it].value == wantToFind) {
            break;
        }
    }
    var txt = dd.options[it].text;
    console.log(txt);
    var $newOption = $("<option selected='selected'></option>").val(jsonData.licenseOwner).text(txt);

    $("#editLicenseOwner").append($newOption).trigger('change');



    $.each(AccessMethodeArray, function (i, d) {
        $('#editAccessMethod').append('<option value="' + d.AccessMethodID + '">' + d.AccessMethod + '</option>');
    });

    var wantToFind = jsonData.accessMethod;
    console.log(wantToFind);
    var dd = document.getElementById('editAccessMethod');
    var it;
    for (it = 0; it < dd.options.length; it++) {
        if (dd.options[it].value == wantToFind) {
            break;
        }
    }
    var txt = dd.options[it].text;
    console.log(txt);
    var $newOption = $("<option selected='selected'></option>").val(jsonData.accessMethod).text(txt);

    $("#editAccessMethod").append($newOption).trigger('change');
    //$('#edittoolname').modal('show');

}

function updatetoolfinal() {

    var tool_obj = new Object();

    if ($("#editToolName").text().split(':')[1] == "" || $("#editLicenseOwner").val() == "" || $("#editAccessMethod").val() == "") {
        pwIsf.alert({ msg: 'Please fill all mandate fields', type: 'warning' });
        return;
    }
    tool_obj.licenseTypeID = "";
    tool_obj.active = $("#activeTool").val();
    tool_obj.createdBy = signumGlobal;
    tool_obj.lastModifiedBy = signumGlobal;
    tool_obj.toolID = $('#toolID1').val();
    tool_obj.licenseOwnerID = $("#editLicenseOwner option:selected").val();
    tool_obj.projectID = localStorage.getItem('views_project_id');
    tool_obj.licenseTypeID = null;
    tool_obj.accessMethodID = $('#editAccessMethod option:selected').val();
    tool_obj.toolVersion = $('#editVersion').val();
    tool_obj.projectToolID = $('#projectToolId').val();
    console.log(tool_obj);
    $.isf.ajax({
        url: service_java_URL + 'projectManagement/updateProjectSpecificTools',
        type: 'POST',
        crossDomain: true,
        context: this,
        contentType: "application/json",
        data: JSON.stringify(tool_obj),

        success: function (returndata) {
            pwIsf.alert({ msg: 'Tool Updated', type: 'info' });
            $('#edittoolname').modal('hide');
            getToolInfoTable();

        },
        error: function (xhr, status, statusText) {
            pwIsf.alert({ msg: 'Tool Update Failed.Please check all fields are filled. If still getting an error, kindly contact technical team.', type: 'error' });
        },

    });

    return false;
}

/*---------------Enable Disable Tools-----------------------*/

function toggleToolStatus(toolId, projectToolID) {

    var checkbox = document.querySelector('input[id="togBtnSource_' + toolId + '"]');
    let statusObj = new Object();
    if (checkbox.checked) { statusObj.active = true; } else { statusObj.active = false; }

    statusObj.projectID = localStorage.getItem('views_project_id');
    statusObj.lastModifiedBy = signumGlobal;
    statusObj.projectToolID = projectToolID
    $.isf.ajax({
        url: service_java_URL + "projectManagement/disableEnableTools",
        type: 'POST',
        crossDomain: true,
        context: this,
        contentType: "application/json",
        data: JSON.stringify(statusObj),
        success: function (data) {
            pwIsf.alert({ msg: "Successfully Enabled/Disabled.", autoClose: 3 });
            getToolInfoTable();
        },
        error: function (xhr, status, statusText) {
            pwIsf.alert({ msg: 'Error in Enabling/Disabling', type: 'warning' });
        },
        complete: function (xhr, statusText) {
        }
    });
}

/*---------------Get Tool Name Dropdown -------------------*/

function getToolName() {

    $('#select_tool_name').html('');
    $.isf.ajax({
        url: service_java_URL + 'projectManagement/getTools',
        type: 'GET',
        contentType: "application/json",
        success: function (data) {
            toolNameArray = data;
            $('#select_tool_name').append('<option value="-1" disabled selected>--Select One--</option>');
            $.each(data, function (i, d) {
                $('#select_tool_name').append('<option value="' + d.ToolID + '">' + d.Tool + '</option>');
            });

        },
        error: function (xhr, status, statusText) {
            pwIsf.alert({ msg: 'Get Tool Name Failed.', type: 'warning' });
        },

    });
}




function getLicenseOwner() {

    $('#select_license_owner').html('');
    $.isf.ajax({
        url: service_java_URL + 'projectManagement/getToolLicenseOwner',
        type: 'GET',
        contentType: "application/json",
        success: function (data) {
            LicenseOwnerArray = data;
            $('#select_license_owner').append('<option value="-1" disabled selected>--Select One--</option>');
            $.each(data, function (i, d) {
                if (d.Defination == null) { $('#select_license_owner').append('<option value="' + d.ToolLicenseOwnerID + '">' + d.LicenseOwner + '</option>'); }
                else { $('#select_license_owner').append('<option value="' + d.ToolLicenseOwnerID + '" title="' + d.Defination + '">' + d.LicenseOwner + '</option>'); }
                // $('#editToolType').append('<option value="' + d.ToolTypeID + '">' + d.ToolType + '</option>');
            });

        },
        error: function (xhr, status, statusText) {
            pwIsf.alert({ msg: 'Get License Owner Failed.', type: 'warning' });
        },

    });
}

/******************** Get Access Method ***********************/

function getAccessMethod() {

    $('#select_access_method').html('');
    $.isf.ajax({
        url: service_java_URL + 'projectManagement/getAccessMethod',
        type: 'GET',
        contentType: "application/json",
        success: function (data) {
            AccessMethodeArray = data;
            $('#select_access_method').append('<option value="-1" disabled selected>--Select One--</option>');
            $.each(data, function (i, d) {
                if (d.Defination == null) {
                    $('#select_access_method').append('<option value="' + d.AccessMethodID + '">' + d.AccessMethod + '</option>');
                }
                else { $('#select_access_method').append('<option value="' + d.AccessMethodID + '" title="' + d.Defination + '">' + d.AccessMethod + '</option>'); }
            });

        },
        error: function (xhr, status, statusText) {
            pwIsf.alert({ msg: 'Get Access Method Failed.', type: 'warning' });
        }

    });
}


function globalUrlEnteringPC() {
    let characterCount = $(URLlink).val().length,
        current = $('#globalUrlInputPC'),
        left = 500 - characterCount;
    current.text("Only 500 characters are allowed (" + left + " left)");
    if (left < 0) {
        current.css('color', 'red')
    }
    else {
        current.css('color', '')
    }
}
function globalUrlNameEnteringPC() {
    let characterCount = $(URLName).val().length,
        current = $('#globalUrlNameInputPC'),
        left = 50 - characterCount;
    current.text("Only 50 characters are allowed (" + left + " left)");
    if (left < 0) {
        current.css('color', 'red')
    }
    else {
        current.css('color', '')
    }
}

function getAllUserSignum() {
    $("#selectallSignumdiv").show();
    $("#selectUserAllSignum-required").hide();
    $("#select_assign_to_user-Required").text("")


    function split(val) {
        return val.split(/,\s*/);
    }
    function extractLast(term) {
        return split(term).pop();
    }

    $('#selectUserAllSignums').val('');

    $("#selectUserAllSignums").autocomplete({
        source: function (request, response) {
            $.isf.ajax({
                url: service_java_URL + "activityMaster/getEmployeesByFilter",
                type: "POST",
                data: {
                    term: extractLast(request.term)
                },
                success: function (data) {
                    $("#selectUserAllSignum-required").hide();
                    $("#selectUserAllSignums").autocomplete().addClass("ui-autocomplete-loading");
                    var result = [];
                    if (data.length == 0) {
                        $("#selectUserAllSignum-required").text('No matches found.');
                        response([{ label: 'No matches found.', val: -1 }]);
                        $("#selectUserAllSignum-required").show().delay(2000).fadeOut();
                    } else {
                        $.each(data, function (i, d) {
                            result.push({
                                "label": d.signum + "/" + d.employeeName,
                                "value": d.signum
                            });
                            //signumResult.push(d.signum.toLowerCase());

                        })
                        response(result);

                    }

                    $("#selectUserAllSignums").autocomplete().removeClass("ui-autocomplete-loading");

                }
            });
        },
        minLength: 3,
        change: function (event, ui) {
            $("#selectUserAllSignum-required").hide();

        },
        focus: function () {
            return false;
        },
        select: function (event, ui) {
            if (ui.item.val == -1) {
                //Clear the AutoComplete TextBox.
                clearInvalidUserInput($(this));

            } else {
                var terms = split(this.value);
                // remove the current input
                terms.pop();
                // add the selected item
                terms.push(ui.item.value);
                signumResult.push(ui.item.value);
                // add placeholder to get the comma-and-space at the end
                terms.push("");
                //return unique work flow id.
                terms = uniqueValues(terms, true);
                this.value = terms.join(", ");
            }
            $('#btnSearchProficiency').prop('disabled', false);
            return false;
        }

    });
    $("#selectUserAllSignums").autocomplete("widget").addClass("fixedHeight");

}
function clearInvalidUserInput(that) {
    if (that.val().indexOf(',') > -1) {
        let strArr = that.val().trim().split(',');
        strArr.pop();
        if (strArr.length > 0) {
            strArr.push(' ');
            that.val(strArr.join(', '));
        } else {
            that.val("");
        }

    } else {
        that.val("");
    }
}
function uniqueValues(list, isSignum) {
    var result = [];
    if (isSignum) {
        $.each(list, function (i, e) {
            if ($.inArray(e.trim(), result) == -1)
                result.push(e.trim());
            else {
                if (e.trim() != '') {
                    $("#selectUserAllSignum-required").text(e + ' already selected.');
                    $("#selectUserAllSignum-required").show().delay(2000).fadeOut();
                }
            }

        });
    } else {
        $.each(list, function (i, e) {
            if ($.inArray(e.trim(), result) == -1)
                result.push(e.trim());
            else {
                if (e.trim() != '') {
                    $("#selectAllWorkFlowIds-required").text(e + ' already selected.');
                    $("#selectAllWorkFlowIds-required").show().delay(2000).fadeOut();
                }
            }

        });
    }

    return result;
}
var termsOfWfId = [];
function getAllProjectWorkFlows() {
    $("#selectAllWorkFlowIds-required").hide();


    function split(val) {
        return val.split(/,\s*/);
    }
    function extractLast(term) {
        return split(term).pop();
    }


    $('#selectwfIdName').val('');

    $("#selectwfIdName").autocomplete({
        source: function (request, response) {
            $.isf.ajax({
                url: service_java_URL + "projectManagement/getWorkFlowsByProjectID",
                type: "POST",
                headers: commonHeadersforAllAjaxCall,
                data: {
                    projectID: parseInt(projectID),
                    term: extractLast(request.term)
                },
                success: function (data) {
                    $("#selectAllWorkFlowIds-required").hide();
                    $("#selectwfIdName").autocomplete().addClass("ui-autocomplete-loading");
                    var result = [];
                    wfNameResult = [];
                    if (data.responseData.length == 0) {
                        response([{ label: 'No matches found.', val: -1 }]);
                        $("#selectAllWorkFlowIds-required").text('No matches found.');
                        $("#selectAllWorkFlowIds-required").show().delay(2000).fadeOut();
                    } else {
                        $.each(data.responseData, function (i, d) {
                            result.push({
                                "label": d.workFlowName,
                                "value": d.workFlowName
                            });
                            //wfNameResult.push(d.workFlowId);

                        });
                        response(result);
                    }
                    $("#selectwfIdName").autocomplete().removeClass("ui-autocomplete-loading");

                }
            });
        },
        minLength: 3,
        change: function (event, ui) {
            $("#selectAllWorkFlowIds-required").hide();

        },
        focus: function () {
            return false;
        },
        select: function (event, ui) {
            if (ui.item.val == -1) {
                //Clear the AutoComplete TextBox.
                clearInvalidUserInput($(this));

            } else {
                var terms = split(this.value);
                // remove the current input
                terms.pop();
                // add the selected item
                terms.push(ui.item.value);
                wfNameResult.push(ui.item.value);
                // add placeholder to get the comma-and-space at the end
                terms.push("");
                //return unique work flow id.
                terms = uniqueValues(terms, false);
                this.value = terms.join(", ");
            }
            $('#btnSearchProficiency').prop('disabled', false);
            return false;
        }

    });
    $("#selectwfIdName").autocomplete("widget").addClass("fixedHeight");

}

function searchUsers(callFromFilter) {
    proficientEmpList = [];
    $('#btnUpdateProficiency').prop("disabled", true);
    let userMsg = validateUserInputs();
    if (userMsg.trim() != '') {
        pwIsf.alert({ msg: userMsg, type: 'error' });

    } else {
        $('#updateProficiency').show();
        $("#selectAllWorkFlowIds-required").hide();
        $("#selectUserAllSignum-required").hide();
        let signums = $('#selectUserAllSignums').val();
        signums = signums.trim();
        signums = signums.replace(/,\s*$/, "");
        var lastChar = signums.slice(-1);
        if (lastChar == ',') {
            signums = signums.substring(0, signums.length - 1);
        }
        let wfids = $('#selectwfIdName').val().trim();
        wfids = wfids.replace(/,\s*$/, "");
        lastChar = wfids.slice(-1);
        if (lastChar == ',') {
            wfids = wfids.substring(0, wfids.length - 1);
        }
        wfids = getwfId(wfids.trim());
        signums = getSignumWithoutSpace(signums);
        if (signums.trim() == '' && wfids.trim() == '') {
            pwIsf.alert({ msg: 'Please provide valid wfid/wfname/signum/name .', type: 'error' });

        } else {
            if (callFromFilter) {
                getEFWorkflowForSignumWFID(signums, wfids, callFromFilter);
            } else {
                getEFWorkflowForSignumWFID(signums, wfids, proficiencyFilterStatus);
            }
            $('#proficiencyFilter').show();
        }

    }
}
function validateUserInputs() {
    let msgUserProficiency = '';
    if ($('#selectUserAllSignums').val() === "" && $('#selectwfIdName').val() === "") {
        msgUserProficiency = 'Please select at least either wfId_name or user signum\Name';
    }
    return msgUserProficiency;
}

function getSignumWithoutSpace(signumStr) {
    let signumArray = signumStr.split(',');
    let refineUserSignums = [];
    signumArray.forEach(ele => {
        if (ele.trim() != '')
            refineUserSignums.push(ele.trim());
    });
    return refineUserSignums.join(',');
}

function getwfId(wfidWithName) {

    let refineWfid = [];
    let allwfidWfName = wfidWithName.split(',');
    allwfidWfName.forEach(ele => {
        refineWfid.push(removeNonNumericData(ele.trim())[0]);
    });

    let nonBlankArr = refineWfid.filter(el => el.trim() != "");
    return nonBlankArr.join(',');
}
function removeNonNumericData(item) {
    let wfid = [];
    wfid = item.split('_');
    if (!isNumericWfId(wfid[0].trim()) && wfid.length > 0) {
        wfid[0] = "";
    }
    return wfid;
}
function isNumericWfId(wfid) {
    let intRegex = /^\d+$/;
    let result = false;
    if (intRegex.test(wfid)) {
        result = true;
    }
    return result;
}
function getUserInoutArrays(signums, wfIds) {
    var userInputArray = [];
    let userSignum = signums.split(',');
    let userWfid = wfIds.split(',');
    if (userSignum.length > userWfid.length) {
        $.each(userSignum, function (i, d) {

            let userdata = {
                signum: userSignum[i] ? userSignum[i].trim() : '',
                workFlowId: userWfid[i] ? getwfId(userWfid[i].trim())[0] : ''
            }
            userInputArray.push(userdata);
        });
    } else {

        $.each(userWfid, function (i, d) {
            let userdata = {
                signum: userSignum[i] ? userSignum[i].trim() : '',
                workFlowId: userWfid[i] ? getwfId(userWfid[i].trim())[0] : ''
            }
            userInputArray.push(userdata);
        });
    }

    return userInputArray;
}

function getEFWorkflowForSignumWFID(signums, wfIds, proficiencyStatus) {
    proficientEmpList = [];
    $('#MsjNodata').text("");
    var today = new Date();
    var dd = String(today.getDate()).padStart(2, '0');
    var mm = String(today.getMonth() + 1).padStart(2, '0'); //January is 0!
    var yyyy = today.getFullYear();

    today = mm + '-' + dd + '-' + yyyy;
    let excelName = 'ProficiencyReport (' + ProjectID + ')__' + today;
    let updatewfids = wfIds.trim();
    if ($.fn.dataTable.isDataTable('#proficiencyDataTable')) {
        oTable.destroy();
        $('#proficiencyDataTable').empty();
    }
    let serviceUrl = service_java_URL + "projectManagement/getEFWorkflowForSignumWFID?projectID=" + projectID + "&listOfSignum=" + decodeURI(signums.trim()) + "&listOfWfid=" + decodeURI(updatewfids) + "&proficiencyStatus=" + proficiencyStatus;
    pwIsf.addLayer({ text: "Please wait ..." });
    $('#proficiencyDataTable').html("");
    $("#proficiencyDataTable").append($('<tfoot><tr><th>Action</th><th>WFID/WorkFlow Name</th><th>Signum</th><th>Proficiency Level</th><th>Triggered By</th><th>Comments</th><th>Modified By</th><th>Modified On</th></tr></tfoot>'));
    oTable = $('#proficiencyDataTable').on('page.dt', function () { $("#chk_selectAll_action").prop('checked', false); }).DataTable({
        "processing": true, //paging
        "serverSide": true, //paging
        searching: true,
        responsive: true,
        "pageLength": 10, //length of records
        "destroy": true,
        order: [[4, "desc"]],
        dom: 'Bfrtip',
        buttons: [
            'colvis', { extend: 'excelHtml5', title: excelName },
            {
                text: 'Clear Selection',
                action: function (e, dt, node, config) {
                    clearSelection();
                }
            },
            {
                text: 'Refresh',
                action: function () {
                    let filterValue = getActiveFilterValue();
                    searchUsers(filterValue);
                }
            }
        ],
        "ajax": {
            "headers": commonHeadersforAllAjaxCall,
            "url": serviceUrl,
            "type": "POST",
            "dataSrc": "data",
            "complete": function (response) {
                handleVisibilityOfHeaderCheckbox(response);
                pwIsf.removeLayer();
            }
        },
        "drawCallback": function (settings) {
            $("#chk_selectAll_action").prop('checked', $('.chk_proficiency').prop('checked'));
            proficientEmpList = [];
            $('#btnUpdateProficiency').prop("disabled", true);


        },
        "columns": [
            {
                "title": "Actions",
                "targets": 'no-sort',
                "orderable": false,
                "searching": false,
                "data": null,
                "render": function (response, type, row, count) {
                    count++
                    let actionHtml = '<div style="display:flex;" > <input type="checkbox" style="margin-top: 0px;" id="chk_UpdateEmpProficiency_' + count + '" class="chk_proficiency" >';
                    if (response.proficiencyLevel.toLowerCase() === 'assessed')
                        actionHtml = actionHtml + '&nbsp;&nbsp;<a  id="assessedIcon' + count + '" title = "Upgrade Proficiency" href="#" > ' + '<i class="fa fa-reply fa-lg revertColor fa-fw"></i>' + '</a>'
                    else
                        actionHtml = actionHtml + '&nbsp;&nbsp;<a  id="expIcon' + count + '" title = "Downgrade Proficiency" href="#" > ' + '<i class="fa fa-arrow-down fa-lg downGradeColor fa-fw"></i>' + '</a>';


                    actionHtml = actionHtml + '</div > ';
                    return actionHtml;
                }
            },
            {
                "title": "WFID/WorkFlow Name", "data": "workFlowName",
                "render": function (data, type, row) {
                    return row.workFlowId + '/' + row.workFlowName;
                }
            },
            { "title": "Signum", "data": "signum", "searchable": true },
            { "title": "Proficiency Level", "data": "proficiencyLevel", "searchable": true },
            { "title": "Triggered By", "data": "triggeredBy", "searchable": true },
            {
                "title": "Comments", "data": "comments", "searchable": true, "className": "ellipsisText", "render": function (data, type, row) {

                    let cmt = '';
                    if (row.comments != null) {
                        let rd = escapeHtml(row.comments);
                        cmt = '<span text-overflow="ellipsis" title="' + rd + '">' + rd + '</span>';
                    }
                    else {
                        cmt = 'NA';
                    }
                    return cmt;
                }
            },
            { "title": "Modified By", "data": "modifiedBy", "searchable": true },
            { "title": "Modified On", "data": "lastModifiedOn", "defaultContent": "NA", "searchable": true },

        ],
        initComplete: function () {
            addFieldsToFooter();  // To append Search and Select all in footer
            var api = this.api();
            api.columns().every(function () {
                var that = this;
                $('input', this.footer()).on('keyup change', function (e) {
                    if (that.search() !== this.value && (e != undefined && e.target != undefined && e.target.type != "checkbox" && e.target.id != 'selectUserAllSignums' && e.target.id != 'selectwfIdName')) {

                        if (e.target.parentElement.parentNode.id === 'proficiencyDataTable_filter') {
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
    });
    $('#proficiencyDataTable tfoot').insertAfter($('#proficiencyDataTable thead'));
    pwIsf.removeLayer();
    //fired on user click row checkbox.
    $('#proficiencyDataTable tbody').on('click', 'input[type="checkbox"]', function (e) {
        var $row = $(this).closest('tr');
        // Get row data
        var data = oTable.row($row).data();
        if (this.checked) {
            $row.addClass('selected');
            AddToArrEmpList(data);
            $('#btnUpdateProficiency').prop("disabled", false);
        } else {
            $row.removeClass('selected');
            removeEmpDetails(data);
            if (proficientEmpList.length == 0) {
                $('#btnUpdateProficiency').prop("disabled", true);
            }
        }

        // Prevent click event from propagating to parent
        e.stopPropagation();
    });
    // fired when user directly click on proficiency icon.
    $('#proficiencyDataTable tbody').on('click', 'a > i', function (e) {
        var $row = $(this).closest('tr');

        pwIsf.confirm({
            title: '<b>Update Proficiency!', msg: getConfirmBoxMsgWithTextArea(),
            'buttons': {
                'Yes': {
                    'action': function () {
                        proficientEmpList = [];
                        // Get row data
                        var data = oTable.row($row).data();
                        $row.addClass('selected');
                        AddToArrEmpList(data);
                        updateEmpProficiency();
                        $('html, body').animate({
                            scrollTop: $("div#updateProficiency").offset().top
                        }, 1000);

                    }
                },
                'No': {
                    'action': function () {
                        $('html, body').animate({
                            scrollTop: $("div#updateProficiency").offset().top
                        }, 1000);
                    }
                },

            }
        });
        e.preventDefault();
        // Prevent click event from propagating to parent
        e.stopPropagation();
    });

}

function addFieldsToFooter() {
    $('#proficiencyDataTable tfoot th').each(function (i) {
        var title = $('#proficiencyDataTable thead th').eq($(this).index()).text();
        if (title != "Actions")
            $(this).html('<input type="text" class="form-control" style="font-size:12px;"  placeholder="Search ' + title + '" data-index="' + i + '" />');
        else if (title == "Actions")
            $(this).html('<div id="allChkDiv" style="display:flex; justify-content: right; margin-left:-7px;"><input style="margin-right:5px;margin-top:0px" id="chk_selectAll_action" type="checkbox" onclick="selectAllCheckBoxes(this)">All</div>');
    });
}
function handleVisibilityOfHeaderCheckbox(response) {

    if (response != undefined && response.responseJSON != undefined && response.responseJSON.data != undefined) {
        if (response.responseJSON.data.length == 0)
            $('#allChkDiv').hide();
        else
            $('#allChkDiv').show();

    }

}
function clearSelection() {

    var table = $('#proficiencyDataTable').DataTable();
    table.rows().nodes().to$().find('input[type="checkbox"]').each(function () {
        $(this).prop('checked', false);
        var $row = $(this).closest('tr');
        var data = oTable.row($row).data();
        $row.removeClass('selected');
        removeEmpDetails(data);
    });
    $("#chk_selectAll_action").prop('checked', false);
    $('#btnUpdateProficiency').prop("disabled", true);
}
// Select/Deselect all checkboxes on selecting/Deselecting "Select All" checkbox 
function selectAllCheckBoxes(e) {
    $("#proficiencyDataTable .chk_proficiency").prop('checked', $("#chk_selectAll_action").prop('checked'));
    if ($("#chk_selectAll_action").prop('checked') == true) {
        $('#btnUpdateProficiency').prop("disabled", false);
        $('#proficiencyDataTable tbody tr').each(function (i, d) {
            if (!$(this).hasClass('selected')) {
                $(this).addClass("selected");
            }
        });
    }
    else {
        $('#btnUpdateProficiency').prop("disabled", true);
        $('#proficiencyDataTable tbody tr').each(function (i, d) {
            if ($(this).hasClass('selected')) {
                $(this).removeClass("selected");
            }
        });
    }
    let table = $('#proficiencyDataTable').DataTable();
    let info = table.page.info();
    let allEmployeesDetails = table.rows().data();
    if (allEmployeesDetails != undefined && allEmployeesDetails.length > 0 && allEmployeesDetails.length >= info.end) {

        for (var i = 0; i < parseInt(info.end); i++) {
            if ($("#chk_selectAll_action").prop('checked') == true) {
                AddToArrEmpList(allEmployeesDetails[i]);
            }
            else {
                removeEmpDetails(allEmployeesDetails[i]);
            }
        }
    }
};


// add detail on list of array on click of checkbox.
function AddToArrEmpList(el) {

    if (el != undefined) {
        var employeeObj = getEmpData(el);
        if (proficientEmpList != undefined && proficientEmpList.length == 0) {
            proficientEmpList.push(employeeObj);
        }
        else if (employeeObj != undefined && proficientEmpList.length > 0) {
            let alreadyExistsObj = proficientEmpList.filter(temp => temp.signum.trim() === el.signum.trim().substring(0, 7) && temp.workFlowId === el.workFlowId && temp.proficiencyLevel.trim().toLowerCase() === el.proficiencyLevel.trim().toLowerCase() && temp.triggeredBy.trim().toLowerCase() == el.triggeredBy.trim().toLowerCase());
            if (alreadyExistsObj != undefined && alreadyExistsObj.length == 0) {
                proficientEmpList.push(employeeObj);
            }
        }
    }
}

function getEmpData(el) {
    let comments = $('#proficiencyComments').val();
    var employeeObj = {
        'signum': el.signum.substring(0, 7),
        'workFlowId': el.workFlowId,
        'triggeredBy': 'UI',
        'modifiedBy': signumGlobal,
        'subActivityId': el.subActivityId,
        'projectId': el.projectId,
        'previousWfUserProficenctID': el.wfUserProficenctID,
        'comments': comments,
        'proficiencyLevel': el.proficiencyLevel,
        'subActivityFlowchartDefID': el.subActivityFlowchartDefID,
        'workFlowName': el.workFlowName
    };
    return employeeObj;
}
// remove employee details from "renewalList" object on deselecting on checkboxes
function removeEmpDetails(el) {
    if (el != undefined) {

        if (proficientEmpList != undefined && proficientEmpList.length > 0) {
            let index = proficientEmpList.findIndex(temp => temp.signum == el.signum.trim().substring(0, 7) && temp.workFlowId == el.workFlowId && temp.proficiencyLevel.trim().toLowerCase() === el.proficiencyLevel.trim().toLowerCase() && temp.triggeredBy.trim().toLowerCase() == el.triggeredBy.trim().toLowerCase());
            if (index != -1) {
                proficientEmpList.splice(index, 1);

            }
        }
    }
}

// when user click upgrade/downgrade icon or update button click.
function updateEmpProficiency() {
    if (proficientEmpList.length == 0) {
        pwIsf.alert({ msg: 'No record selected!', type: 'error' });
    } else {
        proficientEmpList.forEach(function (i) {
            i.comments = $('#proficiencyComments').val();
        });

        var JsonObj = JSON.stringify(proficientEmpList);
        pwIsf.addLayer({ text: "Please wait ..." });
        $.isf.ajax({
            url: service_java_URL + "projectManagement/updateProficiency?projectID=" + projectID,
            type: "POST",
            contentType: 'application/json',
            data: JsonObj,
            success: function (data) {
                pwIsf.removeLayer();
                responseHandled(data);
                let filterValue = getActiveFilterValue();
                searchUsers(filterValue);
                proficientEmpList = [];
                $('#proficiencyComments').val('');
            },
            error: function (xhr, status, statusText) {
                pwIsf.removeLayer();
                pwIsf.alert({ msg: 'error ocure at update proficiency', type: 'error' });

            }
        });
    }
}
// handle api response.
function responseHandled(data) {
    if (data.formErrorCount > 0) {
        var str = "Error(s):";
        for (var i = 0; i < Object.keys(data.formErrors).length; i++) {
            str += "<li>" + data.formErrors[i].replace(/\n/g, '<br/>') + "</li>";
        }
        pwIsf.alert({ msg: str, type: 'error' });
        return false;
    }
    else if (data.formMessageCount > 0) {
        var str = "";
        for (var i = 0; i < Object.keys(data.formMessages).length; i++) {
            if (str != data.formMessages[i].replace(/\n/g, '<br/>')) {
                str += data.formMessages[i].replace(/\n/g, '<br/>');
            }
        }
        pwIsf.alert({ msg: str, type: 'success', autoClose: 2 });
        return true;
    }
    else if (data.formWarningCount > 0) {
        var str = "";
        for (var i = 0; i < Object.keys(data.formWarnings).length; i++) {
            str += data.formWarnings[i].replace(/\n/g, '<br/>');
        }
        pwIsf.alert({ msg: str, type: 'warning' });
        return true;
    }

}
// comments 
function validateComments(commentTextArea) {

    let characterCount = $(commentTextArea).val().length,
        current = $('#pmdrCommentsText'),
        left = 250 - characterCount;
    current.text("Only 250 characters are allowed (" + left + " left)");
}
function callOnProficiencyFilter(filteredProficiency) {
    $("a.divider").removeClass("active");;
    $('#proficiencyFilter').find('a.divider').each(function (e) {
        let htmlData = $(this).attr('data-proficiencylevel');
        if (htmlData.toLowerCase() == $(filteredProficiency).closest('a.divider').attr('data-proficiencylevel').toLowerCase()) {
            $(this).addClass('active');
        }
    });
    let proficiencyFilterValue = $(filteredProficiency).closest('a.divider').attr('data-proficiencylevel').toLowerCase();
    searchUsers(proficiencyFilterValue);
    return false;
}
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


function resetAllUpdateProficiencyData() {
    $('#updateProficiency').hide();
    $('#proficiencyFilter').hide();
    $('#divCommentsUpdate').hide();
    $('#proficiencyDataTable').html('');
    $('#selectwfIdName').val('');
    $('#selectUserAllSignums').val('');
    $('#btnUpdateProficiency').prop("disabled", true);
    $('#btnSearchProficiency').prop("disabled", true);
    proficientEmpList = [];
    $("a.divider").removeClass("active");
    resetFilterToExperience();
}
function resetFilterToExperience() {
    $("a.divider").removeClass("active");
    $('#proficiencyFilter').find('a.divider').each(function (e) {
        let htmlDataproficiencylevel = $(this).attr('data-proficiencylevel');
        if (htmlDataproficiencylevel.toLowerCase() == proficiencyFilterStatus) {
            $(this).addClass('active');
        }
    });
}
function getActiveFilterValue() {
    let filterValue = '';
    $('#proficiencyFilter').find('a.divider').each(function (e) {

        if ($(this).hasClass('active')) {
            filterValue = $(this).attr('data-proficiencylevel');
        }
    });
    return filterValue.toLowerCase();
}


