//Global variables
var pasteOrInput;
var neNameForIndex;
var indexForNERowColour = true;
var limitNEID = parseInt(NO_OF_WO_NEID_DEFAULT_VALUE);

//Constants
var SelectorsNE = {
    selectAllRadio: '#selectAllRadio',
    pageType: '#pageType',
    dividerStatusNE: '.dividerStatusNE'
}

var GlobalsNE = {
    lengthMenu: [10, 25]
}

// hide NE Add/Edit box
function hideNEAddEdit() {
    var neAddEditOnEditButton = document.getElementById('divNEEdit');
    if (neAddEditOnEditButton.classList.contains('showThisDiv')) {
        neAddEditOnEditButton.classList.remove('showThisDiv');
        neAddEditOnEditButton.classList.add('hideThisDiv');
    }
}

// Enable or Disable Buttons
function enableDisableValidation(thisObj, page) {
    const validTextArea = thisObj.value;
    const btnValidate = document.getElementById(`btnValidateNEAddEdit_${page}`);
    //enable Validate button
    if (validTextArea !== "" && btnValidate.classList.contains(`btnDisabledForNEAddEdit`)) {
        btnValidate.classList.remove(`btnDisabledForNEAddEdit`);
        btnValidate.classList.add(`btnEnabledForNEAddEdit`);
    }
    //disable Validate button
    else if (validTextArea === "" && btnValidate.classList.contains(`btnEnabledForNEAddEdit`)) {
        btnValidate.classList.remove(`btnEnabledForNEAddEdit`);
        btnValidate.classList.add(`btnDisabledForNEAddEdit`);
    }
}

// check input/paste events
function checkInputOrPasteForNE(eventVal, page) {
    $(`#isNEValidated_${page}`).val(false);
    updateSaveBtn(page);
    console.log(eventVal);
    if (eventVal === C_PASTE) {
        pasteOrInput = C_PASTE;
    }
    else if (eventVal === C_INPUT) {
        pasteOrInput = C_INPUT;
    }
    apiCallFourLetterSearch(pasteOrInput, page);
    $(`#networkElementValidationMsg_${page}`).hide();
}

// 4 letter search API autoSuggestAfterFourLettersForNE
function apiCallFourLetterSearch(pasteOrInputEvent, page) {

    if (pasteOrInputEvent === C_INPUT) {
        const updatedNEWoid = $(`#woDetails_${page}`).data('updateNEDetails');
        const projID = updatedNEWoid.projectID;
        const domainList = updatedNEWoid.domainID.split(',');
        const techList = updatedNEWoid.technologyID.split(',');

        $(`#textAreaForNE_${page}`).autocomplete({
            source: function (request, response) {
                var searchText = extractLast(request.term);
                if (searchText === ',' || searchText === " " || searchText.length <= 3 || searchText === "") {
                    console.log(searchText);
                    $(`#textAreaForNE_${page}`).removeClass('ui-autocomplete-loading');
                }
                else {
                    $.isf.ajax({
                        url: `${service_java_URL}networkElement/getNetworkElementNameByTerm`,
                        contentType: "application/json",
                        type: 'POST',
                        data: JSON.stringify({
                            projectID: projID,
                            technologyIdList: techList,
                            domainIdList: domainList,
                            term: searchText,
                            count: 50,
                        }),
                        success: function (data) {
                            $(`#textAreaForNE_${page}`).removeClass('ui-autocomplete-loading');
                            var result = [];
                            $.each(data.responseData, function (i, d) {
                                result.push({
                                    "value": d
                                });
                            })
                            response($.ui.autocomplete.filter(result, extractLast(request.term)));
                        }
                    });
                }
            },
            minLength: 4,
            select: function (event, ui) {
                var terms = split(this.value);
                terms.pop();
                terms.push(ui.item.value);
                terms.push("");
                this.value = terms.join(',');
                return false;
            },
            focus: function (event, ui) {
                event.preventDefault();
            }
        });
        $(`#textAreaForNE_${page}`).autocomplete("widget").addClass("fixedHeight");
    }
}

function extractLast(term) {
    return split(term).pop();
}

function split(val) {
    return val.split(/,\s*/);
}

// Remove extra comma from string
function removeExtraComma(str) {
    var lastChar = str.slice(-1);
    str = str.replace(/,+/g, ',');
    if (lastChar === ",") {
        str = str.slice(0, -1);
    }
    return str;
}

function validateNetworkElementLength(commaSeparatedStrings) {
    
    if (limitNEID === 0) {
        limitNEID = 5000;
    }
    if (commaSeparatedStrings.length > limitNEID) {
        return true;
    }
    return false;
}

// Handler to Click on Validate button
function validateNetworkElements(page) {
    pwIsf.addLayer({ text: C_PLEASE_WAIT });

    // Set Default filter to Group
    $("a.dividerStatusNE").removeClass("active");
    $("a#neStatusGroup").addClass("active");

    $(SelectorsNE.pageType).val(page);
    const updatedNEWoid = $(`#woDetails_${page}`).data('updateNEDetails');
    const projID = updatedNEWoid.projectID.toString();
    const domain = updatedNEWoid.domainID.toString();
    const tech = updatedNEWoid.technologyID.toString();
    var commaSeparatedNE = $(`#textAreaForNE_${page}`).val();
    commaSeparatedNE = removeExtraComma(commaSeparatedNE);
    var commaSeparatedStrings = split(commaSeparatedNE);

    if (validateNetworkElementLength(commaSeparatedStrings)) {
        pwIsf.removeLayer();
        $(`#btnValidateNEAddEdit_${page}`).attr('data-target', null);
        const warningForLength = `Maximum limit exceeded - Maximum ${limitNEID} Network Elements allowed`;
        pwIsf.alert({ msg: warningForLength, type: ERRORTEXT });
    }
    else {
        $(`#btnValidateNEAddEdit_${page}`).attr('data-target', '#networkElementDuplicateModal');
        var nodesValidateObj = {
            projectID : projID,
            technologyIDs : tech,
            domainIDs : domain,
            nodeNames : commaSeparatedNE
        };

        var JsonObj = JSON.stringify(nodesValidateObj);

        $.isf.ajax({
            type: C_API_POST_REQUEST_TYPE,
            url: `${service_java_URL}networkElement/validateCommaSeparatedNetworkElementData`,
            dataType: 'json',
            contentType: C_CONTENT_TYPE_APPLICATION_JSON,
            data: JsonObj,
            success: nodesValidation,
            error: function (xhr, status, statusText) {
                pwIsf.removeLayer();
            }
        });

        function nodesValidation(data) {
            pwIsf.removeLayer();
            var tableName = data.responseData;
            if (tableName !== null && tableName !== "") {
                $(`#tableName_${page}`).val(tableName);
                viewNetworkElementDetails(tableName, C_NE_STATUS_GROUP);
            }
            else {
                pwIsf.removeLayer();
                if (data.formErrorCount > 0) {
                    pwIsf.alert({ msg: data.formErrors[0], type: ERRORTEXT });
                }
            }
        }
    }
}

function setHeaderCheckBox() {
    let checkArray = [];
    const dataLength = $(C_ID_TABLE_NE_DETAILS).DataTable().rows({ page: 'current' }).data().length;

    if (dataLength !== 0) {
        $('#selectAllRadio').show();
        checkArray = $(".neRadio").filter(function (i) {
            return $(".neRadio")[i].checked === true;
        })

        if (checkArray.length === $(".neRadio").length) {
            $('#selectAllRadio').prop('checked', true)
        }
    } else {
        $('#selectAllRadio').hide()
    }
}

// Populate Datatables
function viewNetworkElementDetails(tablename, status) {

    NEAddEdit.resetSelectAllData();

    if ($.fn.dataTable.isDataTable(C_ID_TABLE_NE_DETAILS)) {
        $(C_ID_TABLE_NE_DETAILS).DataTable().destroy();
        $(C_ID_TABLE_NE_DETAILS).empty();
    }

    var url = `${service_java_URL}networkElement/viewNetworkElementDetails?tablename=${tablename}&status=${status}`;

    $(C_ID_TABLE_NE_DETAILS).append($(`<tfoot>
                                        <tr>
                                            <th>${status !== C_NE_STATUS_INVALID ?
                                                '<input type="checkBox" id="selectAllRadio" onclick="NEAddEdit.toggleSelectAllNE()">' :
                                                ""}</th>
                                            <th>Network Element Name/ID</th>
                                            <th>Network Element Type</th>
                                            <th>Network Sub-Element Type</th>
                                            <th>Market</th>
                                            <th>Domain/Sub-Domain</th>
                                            <th>Technology</th>
                                            <th>Vendor</th>
                                        </tr>
                                    </tfoot>`));
    var table = $(C_ID_TABLE_NE_DETAILS).DataTable({
        "processing": true, //paging
        "serverSide": true, //paging
        "searching": true,
        info: true,
        responsive: true,
        "lengthMenu": GlobalsNE.lengthMenu,
        "ajax": {
            "headers": commonHeadersforAllAjaxCall,
            "url": url,
            "type": "POST",
            "complete": function (response) {
                console.log(response.responseJSON.responseData);
            },
            "dataSrc": function (data) {
                const resData = data.responseData.data;
                if (resData !== null && resData !== undefined && resData.length > 0) {
                    if (resData[0].name !== undefined) {
                        neNameForIndex = resData[0].name;
                        $.each(resData, function (i, d) {
                            const jsonData = JSON.stringify({
                                name: d.name,
                                networkElementType: d.networkElementType,
                                networkSubElementType: d.networkSubElementType,
                                market: d.market,
                                domainSubDomain: d.domainSubDomain,
                                technology: d.technology,
                                vendor: d.vendor,
                                status: d.status,
                                networkElementId: d.networkElementId,
                                tableName: d.tableName,
                                radioSelection: d.radioSelection
                            });

                            d.radio = createRadioElement(jsonData);
                            d.rowIndex = getRowIndexByName(jsonData);
                        });
                    }
                }

                //Resetting global variable value
                indexForNERowColour = true;

                console.log(data.responseData.data);
                data.recordsTotal = data.responseData.recordsTotal;
                data.recordsFiltered = data.responseData.recordsFiltered;
                data.draw = data.responseData.draw;
                return data.responseData.data;
            },
        },
        drawCallback: function () {
            drawCallbackHandler(status);
        },
        colReorder: true,
        order: [[1, 'asc']],
        dom: 'Blrtip',
        buttons: [
            {
                extend: 'excel',
                text: 'Download',
                action: function (e, dt, node, config) {
                    downloadNEValidatedData(status, tablename);
                }
            },
        ],
        "fnRowCallback": function (row, data, iDisplayIndex, iDisplayIndexFull) {
            rowCallBackHandler(data, row);
        },
        "columns": [
            {
                "title": 'Select',
                "orderable": false,
                "data": "radio",
                "defaultContent": ""
            },
            {
                "title": "Network Element Name/ID",
                "data": "name",
                "orderable": false,
            },
            {
                "title": "Network Element Type",
                "data": "networkElementType",
                "orderable": false,
            },
            {
                "title": "Network Sub-Element Type",
                "data": "networkSubElementType",
                "orderable": false,
            },
            {
                "title": "Market",
                "data": "market",
                "orderable": false,
            },
            {
                "title": "Domain/Sub-Domain",
                "data": "domainSubDomain",
                "orderable": false,
            },
            {
                "title": "Technology",
                "data": "technology",
                "orderable": false,
            },
            {
                "title": "Vendor",
                "data": "vendor",
                "orderable": false,
            },
        ],
        initComplete: function () {
            $('#table_NE_Details tfoot th').each(function (i) {
                var title = $('#table_NE_Details thead th').eq($(this).index()).text();
                if (title !== "Select") {
                    $(this).html(`<input id="neSearch" type="text" class="form-control columnSearch" style="font-size:12px;"  placeholder="Search ${title}" data-index="${i}" />`);
                }
            });
            var api = this.api();
            api.columns().every(function () {
                var that = this;
                $('input.columnSearch', this.footer()).on('keyup change', function () {
                    if (that.search() !== this.value) {

                        NEAddEdit.resetSelection();

                        that
                            .columns($(this).parent().index() + ':visible')
                            .search(this.value)
                            .draw();
                    }
                });
            });

            let options = "";
            GlobalsNE.lengthMenu.forEach((item, i) => options = options.concat(`<option value="${item}">${item}</option>`));

            const customSelect = `<div><label>Show <select id="customLengthMenu" onchange="customLengthMenuChange(this)">
                    ${options}
                    </select> entries</label></div>`;

            $(customSelect).insertAfter('#table_NE_Details_wrapper .dt-buttons');

            $('#table_NE_Details_length').hide();
        }
    });
    $('#table_NE_Details tfoot').insertAfter($('#table_NE_Details thead'));
}

function rowCallBackHandler(data, row) {
    if (data.rowIndex === true) {
        $('td', row).css('background-color', '#FFFFFF');
    } else if (data.rowIndex === false) {
        $('td', row).css('background-color', '#DCDCDC');
    }
}

function drawCallbackHandler(status) {
    if (status !== C_NE_STATUS_INVALID) {
        NEAddEdit.setSelectAllState();
    }
}

function customLengthMenuChange() {
    NEAddEdit.resetSelection();
    const value = $('#customLengthMenu').val();
    $('select[name="table_NE_Details_length"]').val(value).trigger('change');
}

// Create Radio element
function createRadioElement(data) {
    var radioHtml = '';

    data = JSON.parse(data);

    if (data != null) {
        if (data["status"] === C_NE_STATUS_INVALID) {
            return radioHtml;
        }

        const jsonData = JSON.stringify({
            networkElementId: data['networkElementId'],
            name: data['name'],
            tableName: data['tableName'],
            radioSelection: data['radioSelection'],
            status: data['status']
        });

        const radioValue = data["radioSelection"] ? 'checked' : 'unchecked';

        radioHtml = `<div style='display:flex;'><label style='padding-left: 0px;'><input class="neRadio" type='radio'name="${data['name']}"`;
        radioHtml += 'data-details=\'' + jsonData + '\'';// used concatetion due to space issue in json string #2000113
        radioHtml += `id=neRow_${data['networkElementId']} value=${data['networkElementId']}
            onclick='event.preventDefault();handleRadioForNE(this, false);' ${radioValue} ></label></div>`;
    }
    return radioHtml;
}

// Handler to Click on Radio button
function handleRadioForNE(thisObj, selectMultiple, callback = null) {
    const jsonData = $(thisObj).data('details');
    const obj = {
        selectMultiple: selectMultiple
    };
    if (!selectMultiple) {

        obj.name = jsonData.name;
        obj.tableName = jsonData.tableName;
        obj.networkElementId = jsonData.networkElementId;
        obj.status = jsonData.status;
    }
    else {
        const page = $(SelectorsNE.pageType).val();
        const tableName = $(`#tableName_${page}`).val();
        const status = $(`${SelectorsNE.dividerStatusNE}.active`).text().trim();
        const listData = NEAddEdit.getNeidAndNameListString();

        obj.name = listData.nameListString;
        obj.tableName = tableName;
        obj.listOfNetworkElementId = listData.neidListString;
        obj.status = status;
        obj.radioSelectionForSelectAll = $(SelectorsNE.selectAllRadio).prop('checked') ? 1 : 0;
    }

    const JsonObj = JSON.stringify(obj);
    $.isf.ajax({
        type: "POST",
        url: `${service_java_URL}networkElement/updateRadioSelectionForNE`,
        data: JsonObj,
        dataType: 'json',
        contentType: C_CONTENT_TYPE_APPLICATION_JSON,
        success: function (data) {
            if (data.isValidationFailed) {
                responseHandler(data);
            } else {
                if (selectMultiple) {
                    callback();
                }
                else {
                    const el = $(`#neRow_${jsonData.networkElementId}`);
                    const isChecked = el.prop('checked');
                    el.prop('checked', !isChecked);
                }
            }
       },
        error: function (xhr, st, statusText) {
            pwIsf.removeLayer();
        }
    });

}

// Handler to Click on Status
function handleStatusFilterForNE(el) {
    var neStatus;
    const status = el.innerText.trim();
    $("a.dividerStatusNE").removeClass("active");
    $(el).addClass("active");
    const page = $(SelectorsNE.pageType).val();
    const tableName = $(`#tableName_${page}`).val();
    switch (status) {
        case "Group":
            neStatus = "Group";
            break;
        case "Valid":
            neStatus = "Valid";
            break;
        case "InValid":
            neStatus = "InValid";
            break;
    }
    viewNetworkElementDetails(tableName, neStatus)
}

// Handler to Click on Close button
function handleCloseBtnNetworkElementPopUp() {
    $('.dividerStatusNE').removeClass('active');
    $('#neStatusGroup').addClass('active');
    $('#networkElementDuplicateModal').modal('hide');
}

// Handler to Click on Done button
function handleDoneBtnNetworkElementPopUp() {
    const page = $(SelectorsNE.pageType).val();
    const tableName = $(`#tableName_${page}`).val();
    pwIsf.addLayer({ text: C_PLEASE_WAIT });

    $.isf.ajax({
        type: "GET",
        url: `${service_java_URL}networkElement/checkNetworkElementType?tablename=${tableName}`,
        dataType: 'json',
        contentType: C_CONTENT_TYPE_APPLICATION_JSON,
        success: function (data) {
            if (data.formErrorCount > 0) {
                pwIsf.alert({ msg: data.formErrors[0], type: "error" });
            } else {
                if (data.responseData !== null) {
                    populateCountValue(data.responseData, page);
                    $('.dividerStatusNE').removeClass('active');
                    $('#neStatusGroup').addClass('active');
                    $('#networkElementDuplicateModal').modal('hide');
                    $(`#isNEValidated_${page}`).val(true);
                    console.log('Network Element is Validated');

                    if (page === 'neMandatoryX') {
                        $(`#networkElementValidationMsg_${page}`).css('color', 'green');
                        $(`#networkElementValidationMsg_${page}`).text('Network Element validated');
                        $(`#networkElementValidationMsg_${page}`).show();
                    }
                    if (page === 'create') {
                        $(`#networkElementValidationMsg_${page}`).hide();
                    }
                    updateSaveBtn(page);
                } else {
                    pwIsf.alert({ msg: "Please select at least one", type: "warning" });
                }
            }
            pwIsf.removeLayer();
        },
        error: function (xhr, status, statusText) {
            pwIsf.removeLayer();
        }
    });
}

// Populate count value
function populateCountValue(data, page) {
    const neNames = data.neTextName;
    const countValue = data.count;
    const networkElementType = data.networkElementType;
    
    $(`#textAreaForNE_${page}`).val(neNames);
    if (networkElementType !== null && (networkElementType === C_NE_TYPE_NETWORK || networkElementType === C_NE_TYPE_CLUSTER)) {
        $(`#commaSeparatedFreeTextArea_${page}`).css('display', 'block');
        $(`#commaSeparatedLabelDynamic_${page}`).text(networkElementType);
        $(`#countBoxDynamicLabel_${page}`).text(`Count of NETWORK ELEMENTS`);
        $(`#countLabelDynamic_${page}`).text(` worked under : ${networkElementType}`);
        $(`#neCountBoxForNeType_${page}`).css('display', 'block');
        $(`#neCountBoxForNeType_${page}`).css('margin-top','12px');
        $(`#textAreaForNECountForNeType_${page}`).val(countValue);
       //$(`#freeTextAreaForNE_${page}`).val(null);
        if ($(`#freeTextAreaForNE_${page}`).val() == "") {
            $(`#textAreaForNECount_${page}`).val(0);
        }
    } else {
        $(`#commaSeparatedFreeTextArea_${page}`).hide();
        $(`#commaSeparatedLabelDynamic_${page}`).text('');
        $(`#countBoxDynamicLabel_${page}`).text(`Count of NETWORK ELEMENTS: `);
        $(`#countLabelDynamic_${page}`).text('');
        $(`#textAreaForNECount_${page}`).val(countValue);
        $(`#neCountBoxForNeType_${page}`).css('display', 'none');
        $(`#textAreaForNECountForNeType__${page}`).val(0);
        $(`#freeTextAreaForNE_${page}`).val(null);
        
    }
    $(`#networkElementType_${page}`).val(networkElementType)
    enableDisableCount(networkElementType, page);
}

// Update Count Value on input value in free text area.
function updateCountOnInput(value, page) {
    if (value.trim().length > 0) {
        $(`#textAreaForNECount_${page}`).prop('disabled', true);
        const items = value.split(',');
        const countValue = items[items.length - 1] === "" ? items.length - 1 : items.length;
        $(`#textAreaForNECount_${page}`).val(countValue);
    } else {
        $(`#textAreaForNECount_${page}`).val(0);
        $(`#textAreaForNECount_${page}`).prop('disabled', false);
    }
}

// Enable/Disable count input box
function enableDisableCount(networkElementType, page) {
    if (networkElementType !== null) {
        if (networkElementType === C_NE_TYPE_NETWORK || networkElementType === C_NE_TYPE_CLUSTER) {
            if ($(`#freeTextAreaForNE_${page}`).val() === "") {
                $(`#textAreaForNECount_${page}`).prop('disabled', false);
            } else {
                $(`#textAreaForNECount_${page}`).prop('disabled', true);
            }
        } else {
            $(`#textAreaForNECount_${page}`).prop('disabled', true);
        }
    }
}

//Reset Network Element div to default properties
function resetNetworkElementFields(page) {
    $(`#textAreaForNE_${page}`).val('');
    $(`#textAreaForNECount_${page}`).val('');
    $(`#freeTextAreaForNE_${page}`).val('');
    $(`#commaSeparatedFreeTextArea_${page}`).hide();
    $(`#btnValidateNEAddEdit_${page}`).removeClass('btnEnabledForNEAddEdit');
    $(`#btnValidateNEAddEdit_${page}`).addClass('btnDisabledForNEAddEdit');
    $(`#isNEValidated_${page}`).val(false);
    $(`#isNEFreeTextOrCountChanged_${page}`).val(false);
    $(`#btnEditSaveNEAddEdit_${page}`).removeClass('btnEnabledForNEAddEdit');
    $(`#btnEditSaveNEAddEdit_${page}`).addClass('btnDisabledForNEAddEdit');
    $(`#countBoxDynamicLabel_${page}`).text(`Count of NETWORK ELEMENTS: `);
    $(`#countLabelDynamic_${page}`).text(``);
    $(`#textAreaForNECount_${page}`).prop('disabled', true);
    $(`#networkElementValidationMsg_${page}`).hide();
    $(`#neCountBoxForNeType_${page}`).css('display', 'none');
    $(`#textAreaForNECountForNeType__${page}`).val(0);

}

// save button
function updateSaveBtn(page) {
    if (JSON.parse($(`#isNEValidated_${page}`).val()) === true) {
        $(`#btnEditSaveNEAddEdit_${page}`).removeClass('btnDisabledForNEAddEdit');
        $(`#btnEditSaveNEAddEdit_${page}`).addClass('btnEnabledForNEAddEdit');
    }
    else {
        $(`#btnEditSaveNEAddEdit_${page}`).removeClass('btnEnabledForNEAddEdit');
        $(`#btnEditSaveNEAddEdit_${page}`).addClass('btnDisabledForNEAddEdit');
    }
}

// update WO NE on Save Btn
function updateWorkOrderNE(page) {

    pwIsf.addLayer({ text: C_PLEASE_WAIT });

    const updatedNEWoid = $(`#woDetails_${page}`).data('updateNEDetails');
    let updatedNEdata = {}; 
    let selectedNodes = $(`#textAreaForNE_edit`).val().split(',');
    if (($(`#neNameCount_edit`).data('defaultNeDetails').neNames) === '') {
        updatedNEdata.doid = updatedNEWoid.doid;
        updatedNEdata.count = $(`#textAreaForNECount_${page}`).val();
        updatedNEdata.neTextName = $(`#freeTextAreaForNE_${page}`).val();
        updatedNEdata.signum = signumGlobal;
        updatedNEdata.tablename = $(`#tableName_${page}`).val();

        updateNeUsingDoIdForEdit(updatedNEdata, updatedNEWoid.woid, selectedNodes,page);
    }
    else if (($(`#neNameCount_edit`).data('defaultNeDetails').neNames) !== '') {
        updatedNEdata.wOID = updatedNEWoid.woid;
        updatedNEdata.count = $(`#textAreaForNECount_${page}`).val();
        updatedNEdata.neTextName = $(`#freeTextAreaForNE_${page}`).val();
        updatedNEdata.signum = signumGlobal;
        updatedNEdata.tablename = $(`#tableName_${page}`).val();

        updateNeUsingWoIdForEdit(updatedNEdata, updatedNEWoid.woid, selectedNodes,page);
    }

}

function updateNeUsingDoIdForEdit(updatedNEdata, workOID, selectedNodes,page) {
    $.isf.ajax({
        url: `${service_java_URL}woExecution/updateDeliverableOrderNodes`,
        method: "POST",
        contentType: "application/json; charset=utf-8",
        data: JSON.stringify(updatedNEdata),
        dataType: "html",
        success: function () {
            commonSuccessBlock(page);
        },
        error: function () {
            commonErrorBlock(page);
        },
        complete: function () {
            commonCompleteBlock(page);
        }
    });
}

// update NE using WOID
function updateNeUsingWoIdForEdit(updatedNEdata, workOID, selectedNodes,page) {
    $.isf.ajax({
        url: `${service_java_URL}woExecution/updateWorkOrderNodes`,
        method: "POST",
        contentType: C_CONTENT_TYPE_APPLICATION_JSON,
        data: JSON.stringify(updatedNEdata),
        dataType: "html",
        success: function () {
            commonSuccessBlock(page);
        },
        error: function () {
            commonErrorBlock(page);
        },
        complete: function () {
            commonCompleteBlock(page);
        }
    });
}

function commonSuccessBlock(page) {
    pwIsf.removeLayer();
    $('#myModalMadEwo').modal('hide');
    $('#editWODetails').modal('hide');
    resetNetworkElementFields(page);
    pwIsf.alert({ msg: 'Network Elements updated', type: 'success' });
}
function commonErrorBlock(page) {
    pwIsf.removeLayer();
    pwIsf.alert({ msg: "(updateNodes) Request failed" });
}
function commonCompleteBlock(page) {
    if (page === 'edit') {
        location.reload();
    }
}


function setValuesToNEPartialView(data, page) {
    const updateNEObj = new Object();
    if (page === 'edit' || page === 'neMandatory') {
        updateNEObj.projectID = data[0].projectID;
        updateNEObj.woid = data[0].wOID;
        updateNEObj.domainID = data[0].domainID.toString();
        updateNEObj.technologyID = data[0].technologyID.toString();
        updateNEObj.doid = data[0].doID;
    }
    else if (page === 'create') {
        updateNEObj.projectID = $('#projID_DO').val();
        updateNEObj.domainID = $('#domainSp').val();
        updateNEObj.technologyID = $('#techSp').val();
    }
    $(`#woDetails_${page}`).data('updateNEDetails', updateNEObj);
}

// get default ne name and count 
function getDefaultValuesForNEAndCount(page, data) {
    const updateNENameCount = new Object();
    let neName = [];
    let neNameCount;
    $.each(data[0].listOfNode, function (i, d) {
        if (data[0].listOfNode !== '' && data[0].listOfNode !== undefined) {
            neName.push(d.nodeNames);
        }
    });
    if (neName === '') {
        neNameCount = 0;
    }
    else {
        neNameCount = (neName).length;
    }
    
    if (page === 'edit' || page === 'neMandatory') {
       updateNENameCount.neNames = neName.toString();
       updateNENameCount.neCount = neNameCount;
       updateNENameCount.neType = data[0].listOfNode[0].nodeType;
       updateNENameCount.csv = data[0].neCsv;
       updateNENameCount.count = data[0].count;
    }
    $(`#neNameCount_${page}`).data('defaultNeDetails', updateNENameCount);
}

// set default ne name and count
function setDefaultValuesForNEAndCount(page) {
    const updateNENameCount = $(`#neNameCount_${page}`).data('defaultNeDetails');
    if (updateNENameCount.neNames !== '') {
        $(`#textAreaForNE_${page}`).val(updateNENameCount.neNames);        
        if (updateNENameCount.neType === C_NE_TYPE_NETWORK || updateNENameCount.neType === C_NE_TYPE_CLUSTER) {
            $(`#commaSeparatedFreeTextArea_${page}`).css('display', 'block');
            $(`#commaSeparatedLabelDynamic_${page}`).text(updateNENameCount.neType);
            $(`#countBoxDynamicLabel_${page}`).text(`Count of NETWORK ELEMENTS`);
            $(`#countLabelDynamic_${page}`).text(` worked under : ${updateNENameCount.neType}`);
            $(`#freeTextAreaForNE_${page}`).val(updateNENameCount.csv);
            $(`#neCountBoxForNeType_${page}`).css('display', 'block');
            $(`#neCountBoxForNeType_${page}`).css('margin-top', '12px');
            if (updateNENameCount.count !== null) {
                if (updateNENameCount.csv === null) {
                    enableDisableCount(updateNENameCount.neType, page);
                }
                $(`#textAreaForNECount_${page}`).val(updateNENameCount.count);
            }
            else {
                $(`#textAreaForNECount_${page}`).val(0);
                enableDisableCount(updateNENameCount.neType, page);
            }
            $(`#textAreaForNECountForNeType_${page}`).val(updateNENameCount.neCount);
        }
        else {
            $(`#commaSeparatedFreeTextArea_${page}`).hide();
            $(`#commaSeparatedLabelDynamic_${page}`).text('');
            $(`#countBoxDynamicLabel_${page}`).text(`Count of NETWORK ELEMENTS: `);
            $(`#textAreaForNECount_${page}`).val(updateNENameCount.neCount);
            $(`#neCountBoxForNeType_${page}`).css('display', 'none');
            $(`#textAreaForNECountForNeType_${page}`).val(0);
        }
    }
}

// download validated data as per filters
function downloadNEValidatedData(filter, tableName) {
    var paramSearch = `status=${filter}&tablename=${tableName}`;
    let woHistoryEncode = encodeURIComponent(paramSearch);
    let serviceUrl = `${service_java_URL}networkElement/downloadNeData?${woHistoryEncode}`;
    let fileDownloadUrl =`${UiRootDir}/data/GetFileFromApi?apiUrl=${serviceUrl}`;
    window.location.href = fileDownloadUrl;
}

// get row index for alternate colour code
function getRowIndexByName(data) {
    let index = 0;
    data = JSON.parse(data);
    if (data !== null) {
        if ((data.name) == neNameForIndex) {
            index = indexForNERowColour;
        }
        else {
            neNameForIndex = data.name;
            indexForNERowColour = !indexForNERowColour;
            index = indexForNERowColour;
        }
    }
    return index;
}

//check change in NE free text or count
function changeinNeFreeTextOrCount(page) {
    $(`#isNEFreeTextOrCountChanged_${page}`).val(true);
    updateSaveBtnForFreeTextOrCountChange(page);
}

//update save button only for NE free text and count change
function updateSaveBtnForFreeTextOrCountChange(page) {
    if (JSON.parse($(`#isNEFreeTextOrCountChanged_${page}`).val()) === true) {
        $(`#btnEditSaveNEAddEdit_${page}`).removeClass('btnDisabledForNEAddEdit');
        $(`#btnEditSaveNEAddEdit_${page}`).addClass('btnEnabledForNEAddEdit');
    }
    else {
        $(`#btnEditSaveNEAddEdit_${page}`).removeClass('btnEnabledForNEAddEdit');
        $(`#btnEditSaveNEAddEdit_${page}`).addClass('btnDisabledForNEAddEdit');
    }
}

var NEAddEdit = (function () {

    let NESelectAll = {};

    //Restore the select all state(if any) current page
    function setSelectAllState() {
        const currentPageNumber = getCurrentPageNumber(C_ID_TABLE_NE_DETAILS);
        const selectAllRadioBtn = document.getElementById('selectAllRadio');

        if (Reflect.has(NESelectAll, currentPageNumber)) {
            const isChecked = NESelectAll[currentPageNumber].selectAll;
            selectAllRadioBtn.checked = isChecked;
        }
        else {
            selectAllRadioBtn.checked = false;
            NESelectAll[currentPageNumber] = {};
            NESelectAll[currentPageNumber].selectAll = false;
        }

        NESelectAll[currentPageNumber].isSelectAllValid = setSelectAllValidity();
        setHeaderCheckBox();

    }

    // Get current page number
    function getCurrentPageNumber(tableIdSelector) {
        return $(tableIdSelector).DataTable().page.info().page;
    }


    // Toggle select all radio button
    function toggleSelectAllNE() {
        const status = $(`${SelectorsNE.dividerStatusNE}.active`).text().trim().toLowerCase();
        const currentPageNumber = getCurrentPageNumber(C_ID_TABLE_NE_DETAILS);

        if (status === GroupStatus.toLowerCase() && !NESelectAll[currentPageNumber].isSelectAllValid) {
            pwIsf.alert({ msg: RefineSearchText, type: WARNINGTEXT });
            const el = $(SelectorsNE.selectAllRadio);
            const isChecked = el.prop('checked');
            el.prop('checked', !isChecked);
        } else {
            //call multiselect update api for status 'Valid' and 'Group'
            handleRadioForNE(null, true, toggleSelectAll);
        }

        // Update selectAll radio button
        function toggleSelectAll() {
            const el = $(SelectorsNE.selectAllRadio);
            const isChecked = el.prop('checked');

            $('.neRadio').prop('checked', isChecked);
            NESelectAll[currentPageNumber].selectAll = isChecked;
        }
    }

    //Check if select all is valid
    function setSelectAllValidity() {
        const currentPageData = $(C_ID_TABLE_NE_DETAILS).DataTable().rows({ page: 'current' }).data();
        const currentPageGroupsArr = Array.from(currentPageData, d => d.name);
        const currentPageGroupsSet = new Set(currentPageGroupsArr);
        let isValid = false;

        if (currentPageGroupsArr.length === currentPageGroupsSet.size) {
            isValid = true;
        }

        return isValid;

    }

    //Get the list of NEID and Network Element Names as comma separated string
    function getNeidAndNameListString() {
        const currentPageData = $(C_ID_TABLE_NE_DETAILS).DataTable().rows({ page: 'current' }).data();
        const nameList = Array.from(currentPageData, d => d.name);
        const neidList = Array.from(currentPageData, d => d.networkElementId);

        return {
            neidListString: neidList.toString(),
            nameListString: nameList.toString()
        }
    }

    //Just for testing
    function getGlobalData() {
        return NESelectAll;
    }

    //Reset data
    function resetSelectAllData() {
        NESelectAll = {};
    }

    // Reset selection and update DB
    function resetSelection() {

        const page = $(SelectorsNE.pageType).val();

        // Post Object to be confirmed from API developer
        const resetObj = {
            tableName: $(`#tableName_${page}`).val(),
            status: $(`${SelectorsNE.dividerStatusNE}.active`).text().trim()
        }

        // Sample API name. To be confirmed from API dev
        $.isf.ajax({
            url: `${service_java_URL}networkElement/resetSelectedRecordsForNE`,
            contentType: "application/json",
            type: 'POST',
            async: false,
            data: JSON.stringify(resetObj),
            success: function (data) {
                if (data.isValidationFailed) {
                    responseHandler(data);
                }
                else {

                    resetSelectAllData();
                    setSelectAllState();

                    $('.neRadio').prop('checked', false);
                    $(SelectorsNE.selectAllRadio).prop('checked', false);

                }
            }
        });

    }

    //Exposing only the desired functions
    return {
        setSelectAllState,
        toggleSelectAllNE,
        getNeidAndNameListString,
        getGlobalData,
        resetSelection,
        resetSelectAllData
    }

})();

