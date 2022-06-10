const NEConstants = {
    deleteBtnId: 'btnDeleteNeid',
    tableId: 'table_infoNE',
    selectAllId: 'selectAllNE',
    checkBoxClass: 'neidCheckbox'
}
var projectID = localStorage.getItem("views_project_id");
var uploadedNEFileName = '';
var neUploadID = 0;
var allColumnData = {
    "market": null, "networkElementType": null, "vendor": null, "networkSubElementType": null,
    "networkElementName": null, "latitude": null, "longitude": null, "domainName": null,
    "technologyName": null, "uploadedOn": null, "uploadedBy": null,
    "projectID": localStorage.getItem("views_project_id")
};
var NElementCount;


function downloadFile(thisObj) {
    downloadTemplateNetworkElementData({ thisObj: thisObj, projectid: localStorage.getItem("views_project_id") });
}

function showDeleteNetworkElement(networkElementID) {
    pwIsf.confirm({
        title: 'Delete Network Element',
        msg: 'Do you want to delete this network element?',
        buttons: {
            Yes: {
                action: function () {
                    deleteNetworkElement(networkElementID);
                },
                class: 'btn btn-success',
            },
            No: {
                action: function () {
                    return true;
                },
                class: 'btn btn-danger',
            }
        }
    })
}

function deleteNetworkElement(networkElementID) {
   $.isf.ajax({
        url: `${service_java_URL}activityMaster/deleteNetworkElementDetails/${networkElementID}/${signumGlobal}`,
        success: function (data) {
            pwIsf.alert({ msg: deleteNESuccessMsg, type: "info" })
            getNETotalCount(true);
            getNetworkElementDetails();
        },
        error: function (xhr, status, statusText) {
            pwIsf.alert({ msg: deleteNEFailMsg , type: "error" })
        }
    });
}

function refreshNETable() {
    $('#DeleteNetworkElement').modal('hide');
    $('.modal-backdrop').remove();
    getNetworkElementDetails();
}

function downloadTemplate() {
    var filePath;
    $.isf.ajax({
        url: service_java_URL_VM + "flowchartController/downloadFile/NodeTemplate",
        success: function (data) {
            filePath = data;
            window.location = `${UiRootDir}/Project/Download?file=${filePath}`;
        },
        error: function (xhr, status, statusText) {
            console.log("Fail " + xhr.responseText);
            console.log(ERRORTEXT);
        }
    });
}

function getNETotalCount(isCallingFromUI) {
    if (isCallingFromUI) {
        $('#table_infoNE tfoot th').each(function (i) {
            var columnName = $('#table_infoNE th').eq($(this).index()).text();
            columnName = columnName.replace(/\s/g, '');
            var columnRefineName = columnName.split('/');
            var colName = columnRefineName[0];
            var colNameCamel = colName.toCamelCase(colName);
            allColumnData[colNameCamel] = $(`#${$(this).index()}_id`).val();
            //localstorage.setItem("",)
        });
    }
    $.isf.ajax({
        url: `${service_java_URL}networkElement/getTotalNECount`,
        method: "POST",
        returnAjaxObj: true,
        contentType: "application/json; charset=utf-8",
        data: JSON.stringify(allColumnData),
        async: false,
        success: function (data) {
            NElementCount = data.responseData;
            if (isCallingFromUI !== true) {
                getNetworkElementDetails(NElementCount);
            }
        },
        error: function (xhr, status, statusText) {
            pwIsf.alert({ msg: ERRORTEXT, type: "error" })
            console.log(ERRORTEXT)
        }
    });
    return NElementCount;
}

function getNetworkElementDetails() {
    NE.resetSelectedItemsAndButton();
    var serviceUrlForNE = `${service_java_URL}networkElement/getNetworkElementDetailsv1/${localStorage.getItem("views_project_id")}`;
    var table = $('#table_infoNE').DataTable({
        stateSave: false,
        "searching": true,
        "processing": true,
        "serverSide": true,
        "ajax": {
            "headers": commonHeadersforAllAjaxCall,
            "url": serviceUrlForNE,
            "type": "POST",
            "data": function (data) {
                data.recordsTotal = NElementCount;
            },
            "dataSrc": function (data) {
                data.recordsTotal = NElementCount;
                data.recordsFiltered = NElementCount;
                data.draw = data.responseData.draw;

                //set current page NEIDs only for the latest draw
                if (table.ajax.params().draw === parseInt(data.draw)) {
                    NE.setCurrentPageNEIDs(data.responseData.data);
                }

                return data.responseData.data;
            },
        },
        "drawCallback": function () {
            NE.setCheckboxState();
        },
        responsive: true,
        "retrieve": true,
        "destroy": true,
        colReorder: true,
        order: [[1, "desc"]],
        dom: 'Blpftrip',
                buttons: [
                    {
                        extend: 'colvis'
                    },
                    {
                        extend: 'csv',
                        text: 'Download',
                        title: 'Nodes_Detail'
                    },
                ],
                "columns": [
                    {
                        "title": "Action",
                        "targets": 'no-sort',
                        "orderable": false,
                        "searchable": false,
                        "data":null,
                        "defaultContent":"",
                        "render": function (data, type, row, meta) {
                            if (data.uploadedBy !== null && (data.uploadedBy.toLowerCase() === signumGlobal.toLowerCase())) {
                                return `<input class="neidCheckbox" type="checkbox" onchange="NE.toggleIndividualSelect(this)"
                                            data-neid="${data.networkElementID}">
                                        <button style="cursor: pointer; float: right" data-toggle="tooltip"
                                            title="Delete NE" class="fa fa-trash-o"
                                            onclick="showDeleteNetworkElement(${data.networkElementID})">
                                        </button>`;
                            }
                            return ""; //Added to remove sonar smell
                        }
                    },
                    {
                        "title": "Market",
                        "data": "market"
                    },
                    {
                        "title": "Network Element Type",
                        "data": "elementType"
                    },
                    {
                        "title": "Vendor",
                        "data": "vendorDetails[0].vendor"
                    },
                    {
                        "title": "Network Sub Element Type",
                        "data": "type"
                    },
                    {
                        "title": "Network Element Name/ID",
                        "data": "name"
                    },
                    {
                        "title": "Latitude",
                        "data": "latitude"
                    },
                    {
                        "title": "Longitude",
                        "data": "longitude"
                    },
                    {
                        "title": "Domain Name",
                        "data": "domainDetails[0].domain"
                    },
                    {
                        "title": "Technology Name",
                        "data": "technologyDetails[0].technology"
                    },
                    {
                        "title": "Uploaded On",
                        "data": "uploadedON"
                    },
                    {
                        "title": "Uploaded By",
                        "data": "uploadedBy"
                    },
                ],
                "lengthMenu": [10, 25, 50, 100],
                initComplete: function () {
                    $('#table_infoNE tfoot th').each(function (i) {
                        var title = $('#table_infoNE thead th').eq($(this).index()).text();
                        if (title !== "Action") {
                            $(this).html(`<input type="text" id="${i}_id" class="form-control columnSearch" style="font-size:12px;"  placeholder="Search ${title}" data-index="${i}" />`);
                        }
                    });

                    $('#table_infoNE_wrapper .dt-buttons').append(`<button class="btn btn-sm btn-danger" id="btnDeleteNeid" style="margin-bottom: 0px;
                        text-transform: none;" onclick="NE.deleteWarningPopUp()" disabled><i class="fa fa-trash-o" style="margin-right: 5px;"></i>Delete</button>`);

                    var api = this.api();
                    api.columns().every(function () {
                        var that = this;
                        $('input.columnSearch', this.footer()).on('keyup change', function () {
                            if (that.search() !== this.value) {
                                getNETotalCount(true);
                                that
                                  .columns($(this).parent().index() + ':visible')
                                  .search(this.value)
                                    .draw();
                                NE.resetSelectedItemsAndButton();
                            }
                        });
                    });

                    $('#table_infoNE .sorting').on('click', function () {
                        NE.resetSelectedItemsAndButton();
                    });

                    $('select[name="table_infoNE_length"]').on('change', function () {
                        NE.resetSelectedItemsAndButton();
                    });

                    $('#table_infoNE_length').css('padding-top', '5px');

                }
            });

    $('#table_infoNE tfoot').insertAfter($('#table_infoNE thead'));
    table.draw();
    $("#loaderDiv").hide();
    $("#panel_infoNE").css("display", "block");
}

// select popup for upload NE file [2nd call]
function uploadNewNE()
{
    var removeSelectedFile = document.getElementById('uploadNE');
    removeSelectedFile.value = null;

    $("#uploadNE").click();
}

// Validate if any user is already uploading files for NE [1st call]
function validateNEUploadStatus() {
    console.log($("#uploadNE").value);
    $.isf.ajax({
        url: `${service_java_URL}networkElement/checkStatusofNEUploadForUser?projectID=${projectID}`,
        success: function (data) {
            if (data.responseData === null) {
                uploadNewNE();
            }
            else {
                const message = data.responseData.split('.');
                pwIsf.alert({ msg: `${message[0]}. <br>${message[1]}.</br>`, type: 'error' });
            }
        },
        error: function (xhr, status, statusText) {
            pwIsf.removeLayer();
            console.log(ERRORTEXT);
            console.log(ERRORMSG);
        }
    });
}


// Added this method to handle Notification toggel
function showStatus(data) {
    $("#notificationul").toggleClass("displayBlock");
    checkNEUploadFileStatus(data);
}


// It will check the status of NE upload. On the basis of this, clicking on Notification Items loader or popup will be shown
// Inprogress, active = 1
// Validated,
// completed, active = 0
// Cancelled, active = 0
// Expired.active = 0
function checkNEUploadFileStatus(refID) {
    if (refID !== "null" && refID !== null) {
        localStorage.setItem('currentRefID', refID);
        console.log(`RefID [NE]:: ${localStorage.getItem('currentRefID')}`);

        $.isf.ajax({
            url: `${service_java_URL}/networkElement/getUploadedFileStatus?referenceId=${refID}`,
            success: function (data) {
                if (data.responseData != null) {
                    showNEUploadStatus(data.responseData);
                } else {
                    console.log(ERRORTEXT);
                    console.log(errorMessage);
                }
            },
            error: function (xhr, _status, statusText) {
                console.log(ERRORTEXT);
                console.log(ERRORMSG);
            }
        });
    }
}

// add or upload NE data [3rd call]
function selectedNEFileValidation() {
    var selectedNEFileName = document.getElementById('uploadNE');
    if (selectedNEFileName !== null) {
        console.log(`Selected file: ${selectedNEFileName.files.item(0).name}`);
        uploadedNEFileName = selectedNEFileName.files.item(0).name;
        localStorage.setItem('uploadedNEFileName', uploadedNEFileName);
        var ext = uploadedNEFileName.split('.').pop();
        if (ext !== 'xlsx') {
            pwIsf.alert({ msg: "Wrong file type selected. Please select an Excel (.xlsx) file.", type: "error" });
        }
        else if (ext === 'xlsx' && uploadedNEFileName.length > 40) {
            console.log(uploadedNEFileName.length);
            pwIsf.alert({ msg: "Filename maximum characters exceeded. Please give a name within 35 characters.", type: "error" });
        }
        else {
            uploadSelectedNEFile(selectedNEFileName);
        }
    }
    else {
        console.log(selectedNEFileName);
    }
}
// add or upload NE data
function uploadSelectedNEFile(selectedNEFileName) {
    var uploadedNEInProgress = document.getElementById('uploadNEInProgress');
            var startTime = new Date();
            console.log(`Start time :: ${startTime}`);
            var file = selectedNEFileName.files.item(0);
            pwIsf.addLayer({ text: "Please wait uploading your file..." });
            var blobService = AzureStorage.Blob.createBlobServiceWithSas(`${uploadNE}cnedb`, pathUrlNE);
            var customBlockSize = file.size > 1024 * 1024 * 32 ? 1024 * 1024 * 4 : 1024 * 512;
            blobService.singleBlobPutThresholdInBytes = customBlockSize;
            blobService.createBlockBlobFromBrowserFile('temp', file.name, file, { blockSize: customBlockSize }, function (error, result, response) {
                if (error) {
                    console.log("Upload blob failed");
                } else {
                    var currentDate = formatDate(new Date());
                    var fileName = selectedNEFileName.files[0];
                    var formData = new FormData();
                    formData.append('file', fileName);
                    // Call API
                    $.isf.ajax({
                        type: "POST",
                        enctype: "multipart/form-data",
                        url: `${service_java_URL}networkElement/validateNetworkElementFile/${projectID}/${currentDate}/${signumGlobal}`,
                        data: formData,
                        processData: false,
                        contentType: false,
                        cache: false,
                        success: function (data) {
                            if (data.responseData !== null) {
                                console.log("sucesss");
                                pwIsf.removeLayer();
                                localStorage.setItem('NEUploadProgress', 'true');
                                uploadedNEInProgress.style.display = "block";
                                console.log(`${file.name} Upload successfully`);
                                console.log(`Total time taken to Upload :: ${new Date() - startTime} miliseconds`);
                            } else {
                                pwIsf.removeLayer();
                                if (data.formErrorCount === 0) {
                                    const message = data.formMessages[0].split('.');
                                    pwIsf.alert({ msg: `${message[0]}. <br>${message[1]}.</br>`, type: 'error' });
                                } else {
                                    pwIsf.alert({ msg: data.formErrors[0], type: "error" });
                                }
                            }
                        },
                        error: function (xhr, status, statusText) {
                            pwIsf.removeLayer();
                            pwIsf.alert({ msg: "An error has occurred, please check your file again!", type: "error" });
                        }
                    });
                }
            });
}

// upload or cancel the NE Data upload
function uploadOrCancelNEUpload(uploadedNENotificationData) {
    neUploadID = uploadedNENotificationData.neUploadedId;
    var uploadedFileName = uploadedNENotificationData.origionalFileName;

    var divNEDataUpload = document.getElementById('divNEDataUpload');
    var uploadNEInProgress = document.getElementById('uploadNEInProgress');

    //hide the loader and close button
    $("#neLoader").hide();
    $("#closeNEPopup").hide();

    //change the box dimension
    uploadNEInProgress.style.display = "block";
    uploadNEInProgress.style.height = "136px";
    uploadNEInProgress.style.width = "440px";

    //file name block
    divNEDataUpload.style.paddingLeft = "0px";
    divNEDataUpload.style.marginBottom = "2px";
    divNEDataUpload.style.paddingTop = "0px";
    var neUploadStatus = document.getElementById('neUploadStatus');
    var neFileName = document.getElementById('filename');
    neUploadStatus.innerHTML = `<strong>Uploaded file:</strong>`;
    neFileName.style.display = "block";
    if (uploadedFileName !== null && uploadedFileName !== "") {
        neFileName.innerText = uploadedFileName;
    }
    else {
        neFileName.innerText = 'File name not found';
    }


    /*********** valid/invalid count, buttons and note section  ********/
    var validNE = document.getElementById('validNEData');
    var invalidNE = document.getElementById('invalidNEData');
    var noteForNEUpload = document.getElementById('noteForNEUploadTime');
    var btnForNEUpload = document.getElementById('btnForNEUpload');
    validNE.style.display = "block";
    invalidNE.style.display = "block";
    noteForNEUpload.style.display = "block";
    btnForNEUpload.style.display = "block";

    //valid entries
    validNE.innerHTML = "";
    let nEHtmlForValidUpload = `<div class="col-xs-12" id="divNEUploadStatus" style="padding-left:0px;margin-bottom:1px;">
        <span class="edsFontColour"><strong>Valid entries: </strong>${uploadedNENotificationData.successCount}&nbsp&nbsp&nbsp&nbsp<span>`;
    if (uploadedNENotificationData.successCount > 0) {
        nEHtmlForValidUpload = `${nEHtmlForValidUpload}<a href="${uploadedNENotificationData.successLink}"><u class="edsHyperlink">Download to View</u></a>`;
    }
    nEHtmlForValidUpload = `${nEHtmlForValidUpload}</div>`;
    $("#validNEData").append(nEHtmlForValidUpload);

    //invalid entries
    invalidNE.innerHTML = "";
    let neHtmlForInvalidUpload = `<div class="col-xs-12" id="divNEUploadStatus" style="padding-left:0px;">
        <span class="edsFontColour"><strong>Invalid entries (Cannot be uploaded): </strong>${uploadedNENotificationData.failureCount}&nbsp&nbsp&nbsp&nbsp</span>`;
    if (uploadedNENotificationData.failureCount > 0) {
        neHtmlForInvalidUpload += `<a href="${uploadedNENotificationData.failureLink}"><u class="edsHyperlink">Download to View</u></a>`;
    }
    neHtmlForInvalidUpload = `${neHtmlForInvalidUpload}</div >`;
    $("#invalidNEData").append(neHtmlForInvalidUpload);

    //buttons for upload/cancel/close
    btnForNEUpload.innerHTML = "";
    let neHtmlForButtons = `<div class="col-xs-11" style="margin:3px 2px 2px 15px;padding-left:0px;width:75%;">`;
    if (uploadedNENotificationData.successCount > 0) {
        neHtmlForButtons = `${neHtmlForButtons}<button class="btnUploadOrCancel edsFontColour edsBlueButton" id="divNEUploadStatus"
        value="Completed"
        onclick = "uploadOrCancelHandler(this.value, ${uploadedNENotificationData.projectID})" >Upload (${uploadedNENotificationData.successCount} entries)</button >&nbsp&nbsp`;
    }
    else {
        neHtmlForButtons = `${neHtmlForButtons}<button class="btnUploadOrCancel edsFontColour edsBlueButton" id="divNEUploadStatus"
        style="cursor:not-allowed;" value="Completed">
        Upload (${uploadedNENotificationData.successCount} entries)</button >&nbsp&nbsp`;
    }
    neHtmlForButtons = `${neHtmlForButtons}<button class="btnUploadOrCancel edsRedButton edsFontColour" id="divNEUploadStatus"
    style="width:60px;" value="Cancelled" onclick="uploadOrCancelHandler(this.value, ${uploadedNENotificationData.projectID})" >Discard</button ></div>`;
    neHtmlForButtons = `${neHtmlForButtons}<div class="col-xs-1">
    <button class="edsFontColour edsBackgroundColour" id="btnCloseValidatePopup" title="Close this popup"
    style="margin-left:20px;" onclick="closeNEStatusPopup()">Close</button></div>`;
    $("#btnForNEUpload").append(neHtmlForButtons);

    //note for timeline to upload or cancel
    noteForNEUpload.innerHTML = "";
    const timeInterval = ConvertDateTime_tz(uploadedNENotificationData.expiryTime, "neUpload");
    let noteForUpload = `<div class="col-xs-12" style=padding-top:3px;">
        <p style="margin-bottom:0px;color:#E2ACAC" id="divNEUploadStatus"><b>NOTE</b>: Please take action, It will be available till
        ${timeInterval} (15 min).</p>
        <p style="color:#E2ACAC" id="divNEUploadStatus">Else action will be auto discard.</p></span>`;
    noteForUpload = `${noteForUpload}</div>`;
    $("#noteForNEUploadTime").append(noteForUpload);
}

function formatDate(date) {
    var d = new Date(date),
        month = ` ${d.getMonth() + 1}`,
        day = '' + d.getDate(),
        year = d.getFullYear(),
        hour = d.getHours(),
        min = d.getMinutes(),
        sec = d.getSeconds();
    if (month.length < 2) {
        month = '0' + month;
    }
    if (day.length < 2) {
        day = '0' + day;
    }
    return `${[year, month, day].join('-')} ${[hour, min, sec].join(':')}`;
}

function closeNEStatusPopup() {
    localStorage.setItem('currentRefID', null);
    console.log(`currentRefID : ${localStorage.getItem('currentRefID')}`);

    $("#uploadNE").val('');

    var uploadNEInProgress = document.getElementById('uploadNEInProgress');
    uploadNEInProgress.style.height = "38px";
    uploadNEInProgress.style.width = "487px";
    $("#filename").css('display', 'none');
    $("#neLoader").show();
    var divNEDataUpload = document.getElementById('divNEDataUpload');
    divNEDataUpload.style.paddingLeft = "14px";
    divNEDataUpload.style.paddingTop = "0px";
    divNEDataUpload.style.height = "18px";
    var neUploadStatus = document.getElementById('neUploadStatus');
    neUploadStatus.innerHTML = "";
    neUploadStatus.innerHTML=`<strong>Uploading NE is in-progress (Under bell icon you may check the latest update)</strong>`;
    $("#closeNEPopup").show();
    $("#validNEData").css('display', 'none');
    $("#invalidNEData").css('display', 'none');
    $("#noteForNEUploadTime").css('display', 'none');
    $("#btnForNEUpload").css('display', 'none');
    $("#uploadNEInProgress").hide();
}

function showNEUploadStatus(data) {

    if (data.currentStatus === "InProgress") {
        $("#uploadNEInProgress").css('display', 'block');
        localStorage.setItem('currentRefID', `${data.neUploadedId}`);

    } else if (data.currentStatus === "Validated") {
        console.log("validated pop up");
        if (data != null) {
            uploadOrCancelNEUpload(data);
        }
        else {
            console.log("No NE Data found");
        }
    } else if (data.currentStatus === "Failed") {
        $("#uploadNEInProgress").css('display', 'none');
    }
}

function uploadOrCancelHandler(clickStatus, pID) {
    localStorage.setItem('currentRefID', null);

    console.log(`currentRefID : ${localStorage.getItem('currentRefID')}`);

    closeNEStatusPopup();
    pwIsf.addLayer({ text: "Processing your request, please wait..." });
    $.isf.ajax({
        type: "POST",
        enctype: "multipart/form-data",
        url: `${service_java_URL}/networkElement/saveDeleteNeUploadFinalData?signum=${signumGlobal}&projectID=${pID}&status=${clickStatus}&neuploadId=${neUploadID}`,
        success: function (data) {
            if (data.responseData !== null) {
                pwIsf.removeLayer();
                pwIsf.alert({ msg: data.responseData, type: "success" });
                // refresh the datatable only when active tab is Netork Element Tab
                if (window.location.href.includes("Project/ViewProject") && $("li#liMenu3").hasClass("active")) {
                    NE.resetSelectedItemsAndButton();
                    $("#table_infoNE").DataTable().ajax.reload();
                }
            } else {
                pwIsf.removeLayer();
                pwIsf.alert({ msg: data.formErrors[0], type: "error" });
            }
        },
        error: function (xhr, status, statusText) {
            console.log(ERRORTEXT);
            console.log(ERRORMSG);
            pwIsf.removeLayer();
            pwIsf.alert({ msg: "An error has occurred, please check your file again!", type: "error" });
        }
    });
}

//IIFE to expose only the desired functions and abstract the data
const NE = (function (constants) {

    //Data on which operations are performed
    const NEData = {
        checkboxDataForAllPages: {},
        currentPageNEIDList: [],
        selectionLimitNE: 500,
    }

    //Reset the selected items to 0 and disable delete button
    function resetSelectedItemsAndButton() {
        NEData.checkboxDataForAllPages = {};
        NEData.currentPageNEIDList = [];
        const deleteBtn = document.getElementById(constants.deleteBtnId);
        if (deleteBtn) {
            deleteBtn.disabled = true;
        }
    }

    //Logic for individual checkbox selection
    function toggleIndividualSelect(thisObj) {

        const currentPageNumber = $(`#${constants.tableId}`).DataTable().page.info().page;
        const neid = $(thisObj).data('neid');
        const isChecked = $(thisObj).prop('checked');
        let neidList;

        if (Reflect.has(NEData.checkboxDataForAllPages, currentPageNumber)) {
            neidList = NEData.checkboxDataForAllPages[currentPageNumber].neidList;
            
        }
        else {
            NEData.checkboxDataForAllPages[currentPageNumber] = {};
            NEData.checkboxDataForAllPages[currentPageNumber].selectAll = false;
            neidList = NEData.checkboxDataForAllPages[currentPageNumber].neidList = [];
        }

        updateNeidList(neid, isChecked, neidList);

        if (neidList.length === NEData.currentPageNEIDList.length) {
            NEData.checkboxDataForAllPages[currentPageNumber].selectAll = true;
            document.getElementById(constants.selectAllId).checked = true;
        }

        updateDeleteBtn();
        if (isChecked) {
            isNESelectionLimitExceeded();
        }
    }

    //Add or remove from the list passed
    function updateNeidList(neid, isChecked, neidList) {
        if (isChecked) {
            neidList.push(neid);
        }
        else {
            const index = neidList.indexOf(neid);
            if (index > -1) {
                neidList.splice(index, 1);
            }
        }
    }

    //Logic for selection of all checkboxes
    function toggleSelectAll(thisObj) {
        const isChecked = $(thisObj).prop('checked');
        const currentPageNumber = $(`#${constants.tableId}`).DataTable().page.info().page;
        const neidList = isChecked ? [...NEData.currentPageNEIDList] : [];

        if (!Reflect.has(NEData.checkboxDataForAllPages, currentPageNumber)) {
            NEData.checkboxDataForAllPages[currentPageNumber] = {};
        }

        setSelectAllAndNeid();
        updateDeleteBtn();
        if (isChecked) {
            isNESelectionLimitExceeded();
        }

        function setSelectAllAndNeid() {
            NEData.checkboxDataForAllPages[currentPageNumber].selectAll = isChecked;
            NEData.checkboxDataForAllPages[currentPageNumber].neidList = neidList;

            const checkBoxClasses = document.getElementsByClassName(constants.checkBoxClass);
            Array.prototype.forEach.call(checkBoxClasses, el => el.checked = isChecked);
        }
    }

    //Add the deletable items of the current page to the list
    function setCurrentPageNEIDs(data) {
        NEData.currentPageNEIDList = [];
        data.map((item) => {
            if (item.uploadedBy !== null && (item.uploadedBy.toLowerCase() === signumGlobal.toLowerCase())) {
                NEData.currentPageNEIDList.push(item.networkElementID);
            }
        });
    }

    //Restore the checkbox state(if any) of all items of current page
    function setCheckboxState() {
        const currentPageNumber = $(`#${constants.tableId}`).DataTable().page.info().page;
        const selectAllCheckBox = document.getElementById(constants.selectAllId);

        if (Reflect.has(NEData.checkboxDataForAllPages, currentPageNumber)) {
            const isChecked = NEData.checkboxDataForAllPages[currentPageNumber].selectAll;
            const checkBoxList = document.getElementsByClassName(constants.checkBoxClass);

            selectAllCheckBox.checked = isChecked;
            selectAllCheckBox.disabled = NEData.currentPageNEIDList.length === 0 ? true : false;

            for (const checkBoxElement of checkBoxList) {
                const checkBoxNeid = Number(checkBoxElement.dataset.neid);
                if (NEData.checkboxDataForAllPages[currentPageNumber].neidList.includes(checkBoxNeid)) {
                    $(checkBoxElement).prop('checked', true);
                }
            }
        }
        else if (selectAllCheckBox){
            selectAllCheckBox.checked = false;
            selectAllCheckBox.disabled = NEData.currentPageNEIDList.length === 0 ? true : false;
        }

    }

    //Get the total selected items
    function getTotalSelectedItems() {
        let totalSelectedItems = [];
        const pages = Reflect.ownKeys(NEData.checkboxDataForAllPages);
        for (const page of pages) {
            totalSelectedItems = [...totalSelectedItems, ...NEData.checkboxDataForAllPages[page].neidList];
        }

        return totalSelectedItems;
    }

    //Show warning for deletion
    function deleteWarningPopUp() {

        if (!isNESelectionLimitExceeded()) {
            const totalSelectedItems = getTotalSelectedItems();
            const totalCount = totalSelectedItems.length;

            pwIsf.confirm({
                title: "Delete Network Elements",
                msg: `Do you want to delete the selected ${totalCount} Network Element(s)?`,
                buttons: {
                    Yes: {
                        action: function () {
                            deleteSelectedItems(totalSelectedItems);
                        },
                        class: 'btn btn-success',
                    },
                    No: {
                        action: function () {
                            return true;
                        },
                        class: 'btn btn-danger',
                    }
                }
            });
        }

    }

    //Delete all the selected items
    function deleteSelectedItems(totalSelectedItems) {

        const postData = {
            signumID: signumGlobal,
            networkElementID: totalSelectedItems.toString()
        };

        pwIsf.addLayer({ text: C_LOADDER_MSG });

        $.isf.ajax({
            url: `${service_java_URL}activityMaster/deleteNEList`,
            type: 'POST',
            contentType: "application/json",
            data: JSON.stringify(postData),
            success: function (data) {
                pwIsf.removeLayer();
                if (!data.isValidationFailed) {
                    getNETotalCount(true);
                    getNetworkElementDetails();
                }
                responseHandler(data);
            },
            error: function () {
                pwIsf.removeLayer();
            }
        });

    }

    //Enable disable delete button based on the count of selected items
    function updateDeleteBtn() {
        const totalSelectedItems = getTotalSelectedItems();
        if (totalSelectedItems.length > 0) {
            document.getElementById(constants.deleteBtnId).disabled = false;
        }
        else {
            document.getElementById(constants.deleteBtnId).disabled = true;
        }
    }

    //Check if the selcted items exceed the limit
    function isNESelectionLimitExceeded() {

        const totalSelectedItemsCount = getTotalSelectedItems().length;
        let isLimitExceeded = false;
        if (totalSelectedItemsCount > NEData.selectionLimitNE) {
            isLimitExceeded = true;
            pwIsf.alert({
                msg: `You have already reached Max limit (${NEData.selectionLimitNE}) of rows allowed for deletion in one go.
                        Limit is exceeded by ${totalSelectedItemsCount - NEData.selectionLimitNE} row(s).`,
                type: 'warning'
            });
        }

        return isLimitExceeded;

    }

    //Exposing only the desired functions
    return {
        resetSelectedItemsAndButton,
        toggleIndividualSelect,
        toggleSelectAll,
        setCurrentPageNEIDs,
        setCheckboxState,
        deleteWarningPopUp
    }

})(NEConstants);



String.prototype.toCamelCase = function (str) {
    return str
        .replace(/\s(.)/g, function ($1) { return $1.toUpperCase(); })
        .replace(/\s/g, '')
        .replace(/^(.)/, function ($1) { return $1.toLowerCase(); });
}