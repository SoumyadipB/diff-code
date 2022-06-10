$(document).ready(function () {
    getMarketAreasForSharepointConfig();
    getSharePointUrl();
    getAllSharePointConfigData();
});

//Constants for CSS Selectors
const SP_Selectors = {
    selectMarket: '#selectMarket',
    siteNameInput: '#siteNameInput',
    clientId: '#clientId',
    clientSecret: '#clientSecret',
    sharePointUrl: '#sharePointUrl',
    sharePointPanelBody: '#sharePointPanelBody',
    sharePointConfigTable: '#sharePointConfigTable',
    btnCancelUpdate: '#cancelSharepointConfigUpdate',
    formDiv: '.formDiv',
    EditSharepointConfigDiv: 'EditSharepointConfigDiv',
    validateSaveUpdate: '#validateSaveUpdate',
    tableLoader: '#tableLoader'
}

//get market area for sharepoint details configuration
function getMarketAreasForSharepointConfig() {
    $.isf.ajax({
        url: `${service_java_URL}projectManagement/getMarketAreas`,
        success: function (data) {
            $(SP_Selectors.selectMarket).append(`<option></option>`);
            $.each(data, function (i, d) {
                $(SP_Selectors.selectMarket).append(`<option value="${d.MarketAreaID}">${d.MarketAreaName}</option>`);               
            });
            $(SP_Selectors.selectMarket).select2({
                placeholder: "Select Market"
            });
        },
        error: function (xhr, status, statusText) {
            console.log(xhr.responseText);
        }
    });
}

// Get sharepoint url
function getSharePointUrl() {

    $.isf.ajax({
        url: `${service_java_URL}woExecution/getSharepointUrl`,
        type: 'GET',
        success: function (data) {
            if (data.isValidationFailed) {
                responseHandler(data);
            }
            else {
                if (!data.responseData) {
                    pwIsf.addRelativeLayer($(SP_Selectors.sharePointPanelBody), {
                        text: "No SharePoint URL found in the Global URL configuration!",
                        showSpin: false
                    })
                }
                else {
                    $(SP_Selectors.sharePointUrl).text(data.responseData);
                }
            }

        },
        error: function (data) {
            //Blank
        }
    });
}

//validate API for sharepoint configuration
function validateSharepointConfig(isSaveUpdate = false) {
    let siteName = $(SP_Selectors.siteNameInput).val();
    let clientId = $(SP_Selectors.clientId).val();
    let clientSecret = $(SP_Selectors.clientSecret).val();
    let baseUrl = $(SP_Selectors.sharePointUrl).text();
    let completeURL = `${baseUrl}sites/${siteName}`;

    let sharePointClientData = new Object();    
    sharePointClientData.clientId = clientId;
    sharePointClientData.secretKey = clientSecret;
    sharePointClientData.baseURL = completeURL;

    if (isFormValidated()) {
        $.isf.ajax({
            url: `${service_java_URL}appUtil/validateSharePointAccess`,
            type: "POST",
            data: JSON.stringify(sharePointClientData),
            contentType: C_CONTENT_TYPE_APPLICATION_JSON,
            success: function (data) {
                if (!data.isValidationFailed && isSaveUpdate) {
                    saveOrUpdateSharepointConfig();
                }
                else {
                    responseHandler(data);
                }
            },
            error: function (xhr) {
                // Blank
            }
        });
    }
    else {
        console.log(`Sharepoint config validation failed`);
    }
}

// Validate form elements
function isFormValidated() {
    let isValid = true;
    let warningMessage = "";
    let market = $(SP_Selectors.selectMarket).val();
    let siteName = $(SP_Selectors.siteNameInput).val();
    let clientId = $(SP_Selectors.clientId).val();
    let clientSecret = $(SP_Selectors.clientSecret).val();

    if (isNullEmptyOrUndefined(market)) {
        warningMessage = "Please select Market!";
        isValid = false;
    } else if (isNullEmptyOrUndefined(siteName)) {
        warningMessage = "Please enter Site name!";
        isValid = false;
    } else if (siteName.includes("/")) {
        warningMessage = "Site name must not contain this character '/'";
        isValid = false;
    } else if (isNullEmptyOrUndefined(clientId)) {
        warningMessage = "Please enter Client Id!";
        isValid = false;
    } else if (isNullEmptyOrUndefined(clientSecret)){
        warningMessage = "Please enter Client Secret!";
        isValid = false;
    }

    if (!isValid) {
        pwIsf.alert({ msg: warningMessage, type: "warning" });
    }

    return isValid;
}

// Check is value is valid
function isNullEmptyOrUndefined(value) {
    if (value === null || value === undefined || value === "") {
        return true;
    }

    return false;
}

// Get all sharepoint config data
function getAllSharePointConfigData() {

    $.isf.ajax({
        url: `${service_java_URL}woExecution/getSharepointConfigurationDetails`,
        success: function (data) {
            if (!data.isValidationFailed) {
                createDatatable(data.responseData);
            } else {
                responseHandler(data);
            }            
        },
        error: function (data) {
            createDatatable([]);
        }
    });
}

// Create Datatable
function createDatatable(data) {

    $(SP_Selectors.tableLoader).hide();
    $(SP_Selectors.sharePointConfigTable).show();

    let tbody = ``;

    $(data).each(function (i, d) {
        tbody += `<tr>
                    <td style="min-width:90px;">
                        <a class="icon-edit" title="Edit Row" onclick="editSharePointConfigData(this)"><i class="fa fa-edit"></i></a>
                    </td>
                    <td>${d.marketAreaName}</td>
                    <td>${d.sitename}</td>
                    <td>${d.clientID}</td>
                  </tr>`;
    });

    if ($.fn.dataTable.isDataTable(SP_Selectors.sharePointConfigTable)) {
        $(SP_Selectors.sharePointConfigTable).DataTable().destroy();
    }

    $(`${SP_Selectors.sharePointConfigTable} tbody`).html(tbody);

    $(SP_Selectors.sharePointConfigTable).DataTable({
        "lengthChange": false,
        'pagelength': 10
    });
}

// save sharepoint config details in DB 
function saveOrUpdateSharepointConfig() {

    let market = $(SP_Selectors.selectMarket).val();
    let siteName = $(SP_Selectors.siteNameInput).val();
    let clientId = $(SP_Selectors.clientId).val();
    let clientSecret = $(SP_Selectors.clientSecret).val();
    let baseUrl = $(SP_Selectors.sharePointUrl).text();

    let sharePointDetails = new Object();
    sharePointDetails.marketAreaID = market;
    sharePointDetails.baseURL = baseUrl;
    sharePointDetails.siteName = siteName;
    sharePointDetails.clientId = clientId;
    sharePointDetails.secretKey = clientSecret;
    sharePointDetails.createdBy = signumGlobal;

    $.isf.ajax({
        url: `${service_java_URL}appUtil/saveSharePointAccessDetail`,
        type: "POST",
        data: JSON.stringify(sharePointDetails),
        contentType: C_CONTENT_TYPE_APPLICATION_JSON,
        success: function (data) {
            if (!data.isValidationFailed) {
                responseHandler(data);
                cancelSharepointConfigUpdate();
                getAllSharePointConfigData();
            }
            else {
                responseHandler(data);
            }
        },
        error: function (xhr) {
            // Blank
        }
    });
}

// edit sharepoint config details
function editSharePointConfigData(ele) {    
    $(SP_Selectors.formDiv).addClass(SP_Selectors.EditSharepointConfigDiv);
    setFieldData(ele);
    $(SP_Selectors.btnCancelUpdate).show();
    $(SP_Selectors.validateSaveUpdate).text('Validate and Update');
  
}

// cancel update
function cancelSharepointConfigUpdate() {
    $(SP_Selectors.formDiv).removeClass(SP_Selectors.EditSharepointConfigDiv);
    resetFieldData();
    $(SP_Selectors.btnCancelUpdate).hide();
    $(SP_Selectors.validateSaveUpdate).text('Validate and Save');
}

// sets the data to fields
function setFieldData(ele) {
    let marketName = ele.parentElement.parentElement.childNodes[3].innerText;
    $(`${SP_Selectors.selectMarket} option`).filter(function () {return this.text === marketName}).prop("selected", true).trigger('change');
    $(SP_Selectors.siteNameInput).val(ele.parentElement.parentElement.childNodes[5].innerText);
    $(SP_Selectors.clientId).val(ele.parentElement.parentElement.childNodes[7].innerText);
    $(SP_Selectors.clientSecret).val('');
}

// reset the fields
function resetFieldData() {
    $(SP_Selectors.selectMarket).val(null).trigger('change');
    $(SP_Selectors.siteNameInput).val('');
    $(SP_Selectors.clientId).val('');
    $(SP_Selectors.clientSecret).val('');
}

