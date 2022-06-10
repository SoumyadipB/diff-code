$(document).ready(function () {

    LoadDeliveryUnitData();
});
let prevDescription = '';
function LoadDeliveryUnitData() {
    if ($.fn.dataTable.isDataTable('#Delivery_Unit_Search')) {
        oTable.destroy();
        $('#Delivery_Unit_Search').empty();
    }
    pwIsf.addLayer({ text: 'Please wait ...' });
    $.isf.ajax({

        url: `${service_java_URL}projectManagement/getAllDeliverableUnit`,
        crossDomain: true,
        success: function (responseData) {
            pwIsf.removeLayer();
            $('#div_table').show();
            $('#div_table_message').hide();
            getDataTableActionIconsHtml(responseData);
            $("#Delivery_Unit_Search").append($('<tfoot><tr><th></th><th>Deliverable Unit Name</th><th>Delivery Unit Desc</th><th>Method</th><th>Operator Count</th><th>Last Modified By</th><th>Last Modified On</th></tfoot>'));
            oTable = $('#Delivery_Unit_Search').DataTable({
                searching: true,
                responsive: true,

                "pageLength": 10,
                colReorder: true,
                dom: 'Bfrtip',
                order: [1],
                buttons: [
                    'colvis', 'excelHtml5'
                ],
                "data": responseData,
                "destroy": true,
                "columns": [
                    {
                        "title": "Action",
                        "targets": 'no-sort',
                        "orderable": false,
                        "searchable": false,
                        "data": "actionIcon",
                    },
                    {
                        "title": "Unit Name",
                        "data": "DeliverableUnitName",
                    },
                    {
                        "title": "Deliverable Unit Desc",
                        "data": "Description",
                        "defaultContent": "-",
                        "render": function (data) {
                            return (data);
                        }
                    },
                    {
                        "title": "Method",
                        "data": null,
                        "render": function (data) {
                            return methodRowText(data);
                        }
                    },
                    {
                        "title": "Operator Count",
                        "data": null,
                        "render": function (data) {
                            return operatorCountRowText(data);
                        }
                    },
                    {
                        "title": "Last Modified By",
                        "data": "LastModifiedBy",
                        "defaultContent": "-"
                    },
                    {
                        "title": "Last Modified On",
                        "data": "LastModifiedOn",
                        "render": function (data) {
                            if (data) {
                                var date = new Date(data);
                                var month = date.getMonth() + 1;
                                return (month.length > 1 ? month : "0" + month) + "/" + date.getDate() + "/" + date.getFullYear();
                            }
                            
                        },
                        "defaultContent": "-"
                    },
                ],
                initComplete: function () {

                    $('#Delivery_Unit_Search tfoot th').each(function (i) {
                        var title = $('#Delivery_Unit_Search thead th').eq($(this).index()).text();
                        if (title !== "Action")
                            $(this).html(`<input type="text" class="form-control" style="font-size:12px;"  placeholder="Search  ${title}" data-index="${i}" />`);
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


            $('#Delivery_Unit_Search tfoot').insertAfter($('#Delivery_Unit_Search thead'));

            $('#Delivery_Unit_Search tbody').on('click', 'a.icon-view', function () {
                var data = oTable.row($(this).parents('tr')).data();
                localStorage.setItem("views_project_id", data.projectID);

                window.location.href = "../ActivityMaster/DeliverUnit";

            });


        },

    })

}




function deleteDeliveryUnit(UnitID) {

    pwIsf.confirm({
        title: 'Delete Delivery Unit', msg: 'Are you sure to delete this Unit ? ',
        'buttons': {
            'Yes': {
                'action': function () {
                    pwIsf.addLayer({ text: 'Please wait ...' });
                    var unit1 = new Object();


                    unit1.deliverableUnitID = UnitID;
                    unit1.lastModifiedBy = signumGlobal;




                    $.isf.ajax({
                        url: service_java_URL+ 'projectManagement/deleteEditProjDelUnit',
                        type: 'POST',
                        crossDomain: true,
                        context: this,
                        processData: true,
                        contentType: "application/json",
                        data: JSON.stringify(unit1),
                        success: function (data) {

                            pwIsf.alert({ msg: "Successfully Deleted.", autoClose: 3 });
                            location.reload();

                        },
                        error: function (xhr, status, statusText) {
                            pwIsf.alert({ msg: 'Unit delete Failed', type: 'error' });
                        },
                        complete: function (xhr, statusText) {
                            pwIsf.removeLayer();
                        }
                    });
                    
                }
            },
            'No': { 'action': function () { } },

        }
    });
}

function updateUnitMethod(thisObj) {
    const jsonData = $(thisObj).data('details');
    const isMethodChecked = (jsonData.IsMethod === true) ? true : false;
    const isOprCountChecked = (jsonData.IsOperatorCount === true) ? true : false;
    prevDescription = jsonData.Description;
    const html = `<div class="row">
                        <div class="col-lg-4">
                            Unit Name:<a class="text-danger">*</a> <input class="form-control " id="unitName2" name="unit" type="text" value="${jsonData.DeliverableUnitName}"  />
                            <label id="tbEditUnit-Required" style="color: red; font-size: 10px; text-align: center; padding-left:2px;"></label>
                             <input type="hidden" id="unitID"  value="${jsonData.DeliverableUnitID}" />
                                <input type="hidden" id="createdBy"  value="${jsonData.CreatedBy}" />
                        </div>
                        <div class="col-lg-5">
                            Unit Description:<a class="text-danger">*</a>
                                <textarea class="form-control ruleDesc" rows="1"  id="Desc" name="unit"  style="height: 36px;padding-top:3px;"  value="\'${jsonData.Description}\'" maxlength="2500" ></textarea>

                            <label id="tbEditDesc-Required" style="color: red; font-size: 10px; text-align: center; padding-left:2px;"></label>
                        </div>
                        <div class="col-lg-3">
                           <center> <button  id="btnUpdateToolKitName" type="submit" onclick="updateUnitFinal('${jsonData.DeliverableUnitName}',${isMethodChecked},${isOprCountChecked})" pull-right class="btn btn-primary btn-lg" style="margin-top: 13px;">Update Unit</button></center>
                        </div>
</div>
<div class="row">
	<div class="col-lg-4"> 
					Method: <div class="form-inline"><input type="checkbox" class="form-check-input" id="chkEditMethod" />
                         </div> 
				</div>
				<div class="col-lg-5">
					Operator Count:<div class="form-inline">
	<input type="checkbox" class="form-check-input" id="chkEditOperatorCount" />
</div>
</div>
                        
                    `;
  
    $('#updateDeliveryUnit').html('').append(html);
    $('#chkEditMethod').prop('checked', isMethodChecked);
    $('#chkEditOperatorCount').prop('checked', isOprCountChecked);
    $("#Desc").val(jsonData.Description);
    $("#Desc").val($("#Desc").val());

}

function updateUnitFinal(prevName,prevMethod,prevOpCount) {

    var OK = true;
    var flag = false;
    const isMethodChecked = $("#chkEditMethod").prop("checked");
    const isOperatorCountChecked = $("#chkEditOperatorCount").prop("checked");
    $('#tbEditUnit-Required').text("");
    if ($("#unitName2").val() === null || $("#unitName2").val().trim() === "") {
        $('#tbEditUnit-Required').text("Unit name is required");

        OK = false;
    }
    else if ($("#Desc").val() === null || $("#Desc").val().trim() === "") {
        $('#tbEditDesc-Required').text("Description  is required");

        OK = false;
    }
    else if ($("#Desc").val().length > 2500 ) {
        $('#tbEditDesc-Required').text("Description can not be greater than 2500 characters.");

        OK = false;
    }
    else if ($("#Desc").val() === prevDescription && $("#unitName2").val() === prevName
        && isMethodChecked === prevMethod && isOperatorCountChecked === prevOpCount) {

        pwIsf.alert({ msg: "No changes in Unit", type: WARNINGTEXT });
        OK = false;
    }
    if (OK) {
        var updateUnitJsonBody = new Object();
        if (prevName === $("#unitName2").val()) {
            flag = true;
		}
        updateUnitJsonBody.deliverableUnitName = $("#unitName2").val();
        updateUnitJsonBody.description = $("#Desc").val();
        updateUnitJsonBody.deliverableUnitID= $("#unitID").val();
        updateUnitJsonBody.lastModifiedBy = signumGlobal;
        updateUnitJsonBody.createdBy = $("#createdBy").val();
        updateUnitJsonBody.method = $('#chkEditMethod').prop('checked');
        updateUnitJsonBody.operatorCount = $('#chkEditOperatorCount').prop('checked');
        updateUnitJsonBody.flag = flag;
        $.isf.ajax({
            url: `${service_java_URL}projectManagement/EditDeliverableUnit`,
            type: 'POST',
            crossDomain: true,
            context: this,
            contentType: "application/json",
            data: JSON.stringify(updateUnitJsonBody),

            success: function (returndata) {
                if (returndata === "Unit Name Already Exists") {
                    pwIsf.alert({ msg: 'Unit already exists', type: 'warning' });
                }
                else {
                    pwIsf.alert({ msg: 'Unit Updated', type: 'success', autoClose:3 });
                    location.reload();
                }  
            },
            error: function (xhr, status, statusText) {
                pwIsf.alert({ msg: 'Unit Update Failed', type: 'error' });
            },



        });
    }
}

function ClearData() {
    $('#tbAddNewUnit-Required').text("");
    $('#tbAddNewUnitDesc-Required').text("");
    $('#unitName').val("");
    $('#unitDesc').val("");
    $('#chkMethod').prop('checked', false);
    $('#chkOperatorCount').prop('checked',false);

}

function validateAddUnit() {
    var OK = true;
    $('#tbAddNewUnit-Required').text("");
    $('#tbAddNewUnitDesc-Required').text("");
    if ($("#unitName").val() === null || $("#unitName").val().trim() === "") {
        $('#tbAddNewUnit-Required').text("Unit Name  is required");
        OK = false;
    }
    else if ($("#unitDesc").val() === null || $("#unitDesc").val() === 0 || $("#unitDesc").val().trim() === "") {
        $('#tbAddNewUnitDesc-Required').text("Unit Desc is required");
        OK = false;
    } else if ($("#unitDesc").val().length > 2500) {
        $('#tbAddNewUnitDesc-Required').text("Unit description can not be greater than 2500 charecters.");
        OK = false;
    }
    if (OK) {
        addNewUnit();
        ClearData();
    }      
}



function addNewUnit() {
    let unit = new Object();
    unit.deliverableUnitName = $("#unitName").val();
    unit.description = $('#unitDesc').val();
    unit.lastModifiedBy = signumGlobal;
    unit.createdBy = signumGlobal;
    unit.method = $('#chkMethod').prop('checked');
    unit.operatorCount = $('#chkOperatorCount').prop('checked');
    $.isf.ajax({
        url: `${service_java_URL}projectManagement/saveDeliverableUnit`,
        context: this,
        crossdomain: true,
        processData: true,
        contentType: 'application/json',
        type: 'POST',
        data: JSON.stringify(unit),
        xhrFields: {
            withCredentials: false
        },
        success: AjaxSucceeded,
        error: AjaxFailed
    });
    function AjaxSucceeded(data, textStatus) {
        if (!data) {
            pwIsf.alert({ msg: 'Unit Saved', type: 'success', autoClose:3 });
            location.reload();
        }
        else {
            pwIsf.alert({ msg: 'Unit Addition Failed. Unit already exists', type: 'warning' });
		} 
    }
    function AjaxFailed(xhr, status, statusText) {
        pwIsf.alert({ msg: 'Unit Addition Failed', type: 'warning' });
    }
}



//2VN Popup opens after add reason click 
function addReasonPopUp(thisObj) {
    console.log(thisObj);
    var jsonData = $(thisObj).data('details');
    //jsonData.Description = jsonData.Description;
    var html = `
                        <div class="col-lg-12">
                           <h3>${jsonData.DeliverableUnitName}</h3>
                             <input type="hidden" id="unitID"  value="${jsonData.DeliverableUnitID}" />
                                <input type="hidden" id="createdBy"   value="${jsonData.CreatedBy}" />
                                   
                                <input type = "hidden" id="reasonID" style="width:100px; height:20px; background-color: #d3d3d3" class="form-control" disabled  value="" />
                        </div>
                        <div class="col-lg-12">
                            <input type="text" id="reasonText"  class="form-control" placeholder="Add Reason (Max 100 characters without quotes)" maxlength="100" required/>
                                <br>
                            <button id="reasonBtn"   type="submit" onclick="saveReason(this)" data-details= \'` + JSON.stringify(jsonData) + `\' data-toggle="modal"  pull-right class="btn btn-primary btn-lg">Add</button>
                            <button id="cancel"   onclick="clearReason()" class="btn btn-warning">Reset</button>
                        </div>                      
                        
                    `;


    var table = $("<table>", { id: "reasonTable", "class": "table table-striped table-bordered table-hove" });
    table.append('<tr><td>Status</td><td>Reason ID</td><td>Reason</td><td>Actions</td></tr>');



    var unit = new Object();
    unit.deliverableUnitId = jsonData.DeliverableUnitID

    console.log(unit)
    pwIsf.addLayer({ text: 'Please wait ...' });

    $.isf.ajax({
        url: service_java_URL + "woExecution/getAllSuccessReasons?deliverableUnitId=" + unit.deliverableUnitId,
        context: this,
        crossdomain: true,
        processData: true,
        contentType: 'application/json',
        type: 'GET',

        success: AjaxSucceeded,
        error: AjaxFailed
    });
    function AjaxSucceeded(data, textStatus) {
    
        pwIsf.removeLayer();
        for (var x = 0; x < data.length; x++) {
            if (data[x]["encodedSuccessReason"] == "Others" || data[x]["encodedSuccessReason"] == "Not Applicable") {

                table.append('<tr><td>Enabled</td><td>' + data[x]["successReasonId"] + '</td><td>' + data[x]["encodedSuccessReason"] + '</td><td><a class="btn">  </a> </td></tr>');

            }
            else {
                if (data[x]["active"] === true) {
                    var checked = "checked";
                }
                else {
                    checked = "";
                }
                table.append('<tr><td><label class="switchSource"><input type="checkbox" '
                    + checked + ' class="toggleActive" data-toggle="toggle" onclick="reasonStatus(this,'
                    + data[x]["successReasonId"]
                    + ')"> </input><div class="sliderSource round"><span class="onSource">Enabled</span><span class="offSource">Disabled</span></div></label> </td><td>'
                    + data[x]["successReasonId"] + '</td><td>' + data[x]["encodedSuccessReason"] +
                    '</td><td><a class="btn" onclick="editReason(this,' +
                    data[x]["successReasonId"] + ')"> ' +
                    getIcon('edit') + ' </a> </td></tr>');               
            }

                
            }

            $('#updateReason').html('').append(html);
            $('#updateReason').append(table);



     

    }
    function AjaxFailed(xhr, status, statusText) {
        pwIsf.alert({ msg: 'Unit Reasons Fetching Failed.', type: 'warning' });
        pwIsf.removeLayer();
    }



}


//save reason
function saveReason(thisObj) {
    //add reason
    var reason = new Object();
    if ($("#reasonText").val().includes('"')) {
        pwIsf.alert({ msg: 'Double Quotes are not allowed. Please remove them and try saving again.', type: 'warning' });
    }


    else if ($("#reasonID").val() == "" && $("#reasonText").val() ) {

        
        reason.deliverableUnitId = $("#unitID").val();
        reason.encodedSuccessReason = escapeHtml($('#reasonText').val());
        reason.successReason = $('#reasonText').val();
        reason.active =
            console.log(reason)

        pwIsf.addLayer({ text: 'Please wait ...' });

        $.isf.ajax({
            url: service_java_URL + "woExecution/addDeliverableSuccessReason",
            context: this,
            crossdomain: true,
            processData: true,
            contentType: 'application/json',
            type: 'POST',
            data: JSON.stringify(reason),
            xhrFields: {
                withCredentials: false
            },
            success: AjaxSucceeded,
            error: AjaxFailed
        });
        function AjaxSucceeded(xhr, data, textStatus) {
            pwIsf.removeLayer();
            addReasonPopUp(thisObj);

            if (xhr.isValidationFailed === true) {
                pwIsf.alert({ msg: 'Reason already exist', type: 'warning' });
            }
            else {
                pwIsf.alert({ msg: 'Reason added successfully', type: 'success' });
            }


        }
        function AjaxFailed(xhr, status, statusText) {
            pwIsf.removeLayer();
            var err = JSON.parse(xhr.responseText);
            pwIsf.alert({ msg: err.errorMessage, type: 'error' });
        }
    }
    //update reason
    else if ($("#reasonID").val() && $("#reasonText").val() ) {

        reason.deliverableUnitId = $("#unitID").val();
        reason.encodedSuccessReason = escapeHtml($('#reasonText').val());
        reason.successReason = $('#reasonText').val();
        reason.successReasonId = $("#reasonID").val();

        pwIsf.addLayer({ text: 'Please wait ...' });

        $.isf.ajax({
            url: service_java_URL + "woExecution/addDeliverableSuccessReason",
            context: this,
            crossdomain: true,
            processData: true,
            contentType: 'application/json',
            type: 'POST',
            data: JSON.stringify(reason),
            xhrFields: {
                withCredentials: false
            },
            success: AjaxSucceeded,
            error: AjaxFailed
        });
        function AjaxSucceeded(xhr, data, textStatus) {
            pwIsf.removeLayer();
            addReasonPopUp(thisObj);

            if (xhr.isValidationFailed === true) {
                pwIsf.alert({ msg: 'Reason already exist', type: 'warning' });
            }
            else {
                pwIsf.alert({ msg: 'Reason updated successfully', type: 'success' });
            }

        }
        function AjaxFailed(xhr, status, statusText) {
            pwIsf.removeLayer();
            pwIsf.alert({ msg: 'Reason update failed', type: 'warning' });
        }
    }

    else if ( ! $("#reasonText").val()) {
        pwIsf.removeLayer();
        pwIsf.alert({ msg: 'Please fill required fields', type: 'warning' });
    }

    else {
        pwIsf.removeLayer();
    }



}

//edit reason
function editReason(elem, reasonID) {
    var child = elem.parentElement.parentElement.parentElement.childNodes
    var reasonText = unescapeHtml(elem.parentElement.previousSibling.innerHTML);
    for (i = 0; i < child.length; i++) {
        child[i].style["background-color"] = "";
    }
    document.getElementById("reasonBtn").innerHTML = "Update";

    elem.parentElement.parentElement.style["background-color"] = "#F4D5F5";
    $("#reasonID").val(reasonID)
    $("#reasonText").val(reasonText)

}

function clearReason() {
    document.getElementById("reasonBtn").innerHTML = "Add";
    var childs = document.getElementById("reasonTable").firstChild.childNodes;
    for (i = 0; i < childs.length; i++) {
        childs[i].style["background-color"] = "";
    }

    $("#reasonID").val("");
    $("#reasonText").val("");
}


function reasonStatus(elem, reasonID) {

    console.log(elem.checked)
    var flag = "";
    flag = elem.checked;
    var reason = new Object();
    reason.successReasonId = reasonID
    pwIsf.addLayer({ text: 'Please wait ...' });
    $.isf.ajax({
        url: service_java_URL + "woExecution/saveReasonStatus?flag="+ flag,
        context: this,
        crossdomain: true,
        processData: true,
        contentType: 'application/json',
        type: 'POST',
        data: JSON.stringify(reason),
        xhrFields: {
            withCredentials: false
        },
        success: AjaxSucceeded,
        error: AjaxFailed
    });
    function AjaxSucceeded(data, textStatus) {

        pwIsf.removeLayer();
        pwIsf.alert({ msg: 'Reason status changed successfully', type: 'success' });

    }
    function AjaxFailed(xhr, status, statusText) {
        pwIsf.removeLayer();
        pwIsf.alert({ msg: 'Reason status update failed', type: 'warning' });
    }
         

}


function Check(elem) {

    var reg = /<(.|\n)*?>/g;
    var reg2 = /<[a-zA-Z]/g;
    if (reg.test(elem.val()) === true || reg2.test(elem.val()) === true) {
        pwIsf.alert({ msg: 'HTML Tags not allowed. invalid use of "<,>"', type: 'warning' });
        return 0;
    }
    else {
        return 1;
    }
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

function unescapeHtml(safe) {
    return safe.replace(/&amp;/g, '&')
        .replace(/&lt;/g, '<')
        .replace(/&gt;/g, '>')
        .replace(/&quot;/g, '"')
        .replace(/&#039;/g, "'");
}
function methodRowText(data) {
    if (data.IsMethod && data.IsMethod === true) {
        return "YES";
    } else {
        return "NO";
    }
}
function operatorCountRowText(data) {
    if (data.IsOperatorCount && data.IsOperatorCount === true) {
        return "YES";
    } else {
        return "NO";
    }
}
function getDataTableActionIconsHtml(responseData) {
    $.each(responseData, function (i, d) {
        const jsonData = JSON.stringify({
            DeliverableUnitName: d.DeliverableUnitName, Description: (d.Description), LastModifiedBy: d.LastModifiedBy,
            LastModifiedOn: d.LastModifiedOn, CreatedBy: d.CreatedBy, DeliverableUnitID: d.DeliverableUnitID,
            active: d.Active, IsMethod:d.IsMethod,IsOperatorCount:d.IsOperatorCount
        });
        
        d.actionIcon = `<div style="display:flex"><a href="#editUnit" class="icon-edit" title="Edit Delivery Unit " data-details=\'${jsonData}\'  data-toggle="modal"  onclick="updateUnitMethod(this)">${getIcon('edit')}</a>

           <a class="icon-delete lsp" title="Delete Unit" onclick="deleteDeliveryUnit(${d.DeliverableUnitID})">${getIcon('delete')}</a>
        <div style="display:flex; margin-left: 5px;"><a href="#addReason" class="icon-add" title="Success Reason Add/Edit" data-details=\'${jsonData}\' data-toggle="modal"  onclick="addReasonPopUp(this)">${getIcon('add')}</a></div>`;
    });
}
