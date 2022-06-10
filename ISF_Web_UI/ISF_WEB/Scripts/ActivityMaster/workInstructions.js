const SelectorsWI = {
    WrkInsVendorNme: '#WrkInsVendorNme',
    WrkInsTchnlgy: '#WrkInsTchnlgy',
    WrkInsDomain: '#WrkInsDomain',
    WrkInsVendorNme2: '#WrkInsVendorNme2',
    WrkInsTchnlgy2: '#WrkInsTchnlgy2',
    WrkInsDomain2: '#WrkInsDomain2',
    updateWrkIns: '#updateWrkIns',
    tblWorkInstructions: '#tblWorkInstructions'
}

const ConstText = {
    deleteWorkInstructionUrl : 'activityMaster/deleteWorkInstruction'
}

function closeWrkInsModal() {
    $(SelectorsWI.WrkInsVendorNme).select2("val", "");
    $(SelectorsWI.WrkInsTchnlgy).select2("val", "");
    $(SelectorsWI.WrkInsDomain).select2("val", "");
    $('#textAreaWrkInsKpi').val("");
    $('#textAreaWrkInsSw').val("");
    $('#textAreaWrkIns').val("");
    $('#textAreaWrkInsNme').val("");
    $('#WrkInsAddModal').modal('hide');

}
    function closeWrkInsUpdateModal() {
        $(SelectorsWI.WrkInsVendorNme2).select2("val", "");
    $(SelectorsWI.WrkInsTchnlgy2).select2("val", "");
    $(SelectorsWI.WrkInsDomain2).select2("val", "");
    $('#textAreaWrkInsKpi2').val("");
    $('#textAreaWrkInsSw2').val("");
    $('#textAreaWrkIns2').val("");
    $('#textAreaWrkInsNme2').val("");
    $(SelectorsWI.updateWrkIns).modal('hide');

}
    $(document).ready(function () {
        $(SelectorsWI.updateWrkIns).on('show.bs.modal', function () {
            $(SelectorsWI.WrkInsDomain2).select2();
        })

        $(SelectorsWI.updateWrkIns).on('hidden.bs.modal', function () {
        $(SelectorsWI.WrkInsDomain2).select2('destroy');
    })
        $(SelectorsWI.updateWrkIns).on('show.bs.modal', function () {
        $(SelectorsWI.WrkInsTchnlgy2).select2();
    })

        $(SelectorsWI.updateWrkIns).on('hidden.bs.modal', function () {
        $(SelectorsWI.WrkInsTchnlgy2).select2('destroy');
    })
        $(SelectorsWI.updateWrkIns).on('show.bs.modal', function () {
        $(SelectorsWI.WrkInsVendorNme2).select2();
    })

        $(SelectorsWI.updateWrkIns).on('hidden.bs.modal', function () {
        $(SelectorsWI.WrkInsVendorNme2).select2('destroy');
    })

    getWorkInstructions();
    getVendorsWrkIns();
    getDomainWrkIns();
    getTechnologyWrkIns();

        $('#WrkInsAdd').on('show.bs.modal', function () {
        $(this).find('.modal-content').css({
            width: 'auto', //probably not needed
            height: 'auto', //probably not needed
            'max-width': '50%',
            'margin': 'auto'
        });
    });


});




function getVendorsWrkIns() {
    $(SelectorsWI.WrkInsVendorNme).select2({
        placeholder: "Please select a Vendor"
    });
    $(SelectorsWI.WrkInsVendorNme).empty();

    $.isf.ajax({
        type: "GET",
        url: `${service_java_URL}activityMaster/getVendorDetails`,
        success: function (data) {
            $(SelectorsWI.WrkInsVendorNme).append('<option value=""></option>');
            $.each(data, function (i, d) {
                $(SelectorsWI.WrkInsVendorNme).append(`<option value="${d.vendorID}">${d.vendor}</option>`);
            })
        },
        error: function (xhr, status, statusText) {
            //Blank
        }
    });

}

function getDomainWrkIns() {
    $(SelectorsWI.WrkInsDomain).select2({
        placeholder: "Please select a Domain"
    });
    $(SelectorsWI.WrkInsDomain).empty();

    $.isf.ajax({
        type: "GET",
        url: `${service_java_URL}activityMaster/getDomainDetails`,
        success: function (data) {
            $(SelectorsWI.WrkInsDomain).append('<option value=""></option>');
            $.each(data, function (i, d) {
                $(SelectorsWI.WrkInsDomain).append(`<option value="${d.domainID}">${d.domain}</option>`);

            })
        },
        error: function (xhr, status, statusText) {
            //Blank
        }
    });

}

function getTechnologyWrkIns() {
    $(SelectorsWI.WrkInsTchnlgy).select2({
        placeholder: "Please select a Technology"
    });
    $(SelectorsWI.WrkInsTchnlgy).empty();

    $.isf.ajax({
        type: "GET",
        url: `${service_java_URL}activityMaster/getTechnologyDetails`,
        success: function (data) {
            $(SelectorsWI.WrkInsTchnlgy).append('<option value=""></option>');
            $.each(data, function (i, d) {
                $(SelectorsWI.WrkInsTchnlgy).append(`<option value="${d.technologyID}">${d.technology}</option>`);

            })
        },
        error: function (xhr, status, statusText) {
            //Blank
        }
    });

}


function getVendorsWrkInsForUpdate() {
    $(SelectorsWI.WrkInsVendorNme2).select2({
        placeholder: "Please select a Vendor"
    });

    $.isf.ajax({
        type: "GET",
        url: `${service_java_URL}activityMaster/getVendorDetails`,
        success: function (data) {
            $(SelectorsWI.WrkInsVendorNme2).append('<option value=""></option>');
            $.each(data, function (i, d) {
                $(SelectorsWI.WrkInsVendorNme2).append(`<option value="${d.vendorID}">${d.vendor}</option>`);

            })
        },
        error: function (xhr, status, statusText) {
            //Blank
        }
    });

}

function getDomainWrkInsForUpdate() {
    $(SelectorsWI.WrkInsDomain2).select2({
        placeholder: "Please select a Domain"
    });

    $.isf.ajax({
        type: "GET",
        url: `${service_java_URL}activityMaster/getDomainDetails`,
        success: function (data) {
            $(SelectorsWI.WrkInsDomain2).append('<option value=""></option>');
            $.each(data, function (i, d) {
                $(SelectorsWI.WrkInsDomain2).append(`<option value="${d.domainID}">${d.domain}</option>`);
            })
        },
        error: function (xhr, status, statusText) {
            //Blank
        }
    });

}

function getTechnologyWrkInsForUpdate() {
    $(SelectorsWI.WrkInsTchnlgy2).select2({
        placeholder: "Please select a Technology"
    });

    $.isf.ajax({
        type: "GET",
        url: `${service_java_URL}activityMaster/getTechnologyDetails`,
        success: function (data) {
            $(SelectorsWI.WrkInsTchnlgy2).append('<option value=""></option>');
            $.each(data, function (i, d) {
                $(SelectorsWI.WrkInsTchnlgy2).append(`<option value="${d.technologyID}">${d.technology}</option>`);

            })
        },
        error: function (xhr, status, statusText) {
            //Blank
        }
    });

}






function getWorkInstructions() {
    if ($.fn.dataTable.isDataTable(SelectorsWI.tblWorkInstructions)) {
        $(SelectorsWI.tblWorkInstructions).DataTable().destroy();
        $(SelectorsWI.tblWorkInstructions).empty();
    }

    pwIsf.addLayer({ text: 'Please wait ...' });
    $.isf.ajax({
        url: `${service_java_URL}activityMaster/getWorkInstruction`,
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

            $(SelectorsWI.tblWorkInstructions).append($(`<tfoot><tr><th></th><th></th><th>Work Instruction Name</th>
                            <th>SW Release</th><th>KPI Name</th><th>Revision No.</th><th>Flowchart Owner</th><th>Description</th>
                            <th>Domain</th><th>Vendor</th><th>Technology</th><th>Status</th><th>Created By</th><th>Created On</th>
                            <th>Modified By</th><th>Modified On</th><th>Hyperlink</th></tr></tfoot>`));
            $(SelectorsWI.tblWorkInstructions).DataTable({
                searching: true,
                responsive: true,
                "pageLength": 20,
                "data": data,
                "destroy": true,
                colReorder: true,
                dom: 'Bfrtip',
                buttons: [
                    'colvis', 'excelHtml5'
                ],
                "columns": [
                    {
                        "title": "Action", "targets": 'no-sort', "orderable": false, "searchable": false, "data": null,
                        "defaultContent": "",
                        "render": function (data, type, row, meta)
                        {

                            const jsonData = JSON.stringify({
                                wIID: data.wIID, workInstructionName: data.workInstructionName, domainID: data.domainID,
                                vendorID: data.vendorID, technologyID: data.technologyID, sWrelease: data.sWrelease,
                                kPIName: data.kPIName, flowchartOwner: data.flowchartOwner, description: data.description,
                                revNumber: data.revNumber, active: data.active, createdBy: data.createdBy, createdON: data.createdON,
                                modifiedBy: data.modifiedBy, modifiedON: data.modifiedON, domain: data.domain, vendor: data.vendor,
                                technology: data.technology, hyperlink: data.hyperlink,

                            });
                            if (data.active === true) {
                                return `<div style="display:flex"><a class="icon-edit lsp" href="#updateWrkIns" 
                                data-details= \'${jsonData}\' title="Edit Work Instruction" onclick="updateWrkIns(this)" 
                                data-toggle="modal" ><i class="fa fa-edit"></i></a></div>`;
                            }

                            return "";
                        }
                    },
                    {
                        "title": "Active/Inactive", "targets": 'no-sort', "orderable": false, "searchable": false, "data": null,
                        "defaultContent": "",
                        "render": function (data, type, row, meta) {

                            if (data.active === true) {
                                return `<label class="switchSource"><input type="checkbox" checked class="toggleActive" 
                            onclick="toggleSourceStatusWrkIns(${data.wIID})" id="togBtnSource_${data.wIID}" />
                            <div class="sliderSource round"><span class="onSource">Enabled</span><span class="offSource">Disabled</span>
                            </div></label>`;
                            }
                            else {
                                return `<label class="switchSource"><input type="checkbox" class="toggleActive" 
                            onclick="toggleSourceStatusWrkIns(${data.wIID})" id="togBtnSource_${data.wIID}"/>
                            <div class="sliderSource round"><span class="onSource">Enabled</span><span class="offSource">Disabled</span>
                            </div></label>`;

                            }

                        }

                    },
                    {
                        "title": "Work Instruction Name", "data": null, "searchable": true,
                        "render": function (data, type, row, meta) {
                            console.log(data);
                            return data.workInstructionName;
                        }
                    },
                    {
                        "title": "SW Release", "data": null, "searchable": true,
                        "render": function (data, type, row, meta) {
                            return data.sWrelease;
                        }
                    },
                    {
                        "title": "KPI Name", "data": null,
                        "render": function (data, type, row, meta) {
                            return data.kPIName;
                        }
                    },
                    {
                        "title": "Revision No.", "data": null, "searchable": true,
                        "render": function (data, type, row, meta) {
                            console.log(data);
                            return data.revNumber;
                        }
                    },
                    {
                        "title": "Flowchart Owner", "data": null, "searchable": true,
                        "render": function (data, type, row, meta) {
                            return data.flowchartOwner;
                        }
                    },
                    {
                        "title": "Description", "data": null, "searchable": true,
                        "render": function (data, type, row, meta) {
                            console.log(data);
                            return data.description;
                        }
                    },
                    {
                        "title": "Domain", "data": null, "searchable": true,
                        "render": function (data, type, row, meta) {
                            console.log(data);
                            return data.domain;
                        }
                    },
                    {
                        "title": "Vendor", "data": null, "searchable": true,
                        "render": function (data, type, row, meta) {
                            return data.vendor;
                        }
                    },
                    {
                        "title": "Technology", "data": null, "searchable": true,
                        "render": function (data, type, row, meta) {
                            return data.technology;
                        }
                    },
                    {
                        "title": "Status", "data": null,
                        "render": function (data, type, row, meta) {
                            return activeOrInactive(data);
                        }
                    },
                    {
                        "title": "Created By", "data": null, "searchable": true,
                        "render": function (data, type, row, meta) {
                            return data.createdBy;
                        }
                    },
                    {
                        "title": "Created On", "data": null, "searchable": true,
                        "render": function (data, type, row, meta) {
                            return data.createdON;
                        }
                    },
                    {
                        "title": "Modified By", "data": null, "searchable": true,
                        "render": function (data, type, row, meta) {
                            return data.modifiedBy;
                        }
                    },
                    {
                        "title": "Modified On", "data": null, "searchable": true,
                        "render": function (data, type, row, meta) {
                            return data.modifiedON;
                        }
                    },
                    {
                        "title": "Hyperlink", "data": null, "searchable": true,
                        "render": function (data, type, row, meta) {
                            return data.hyperlink;
                        }
                    },

                ],
                initComplete: function () {

                    $(`${SelectorsWI.tblWorkInstructions} tfoot th`).each(function (i) {
                        var title = $(`${SelectorsWI.tblWorkInstructions} thead th`).eq($(this).index()).text();
                        if (title !== "Action") {
                            $(this).html(`<input type="text" class="form-control" style="font-size:12px;" placeholder="Search ${title}" data-index="${i}" />`);
                        }
                    });
                    var api = this.api();
                    api.columns().every(function () {
                        var that = this;
                        $('input', this.footer()).on('keyup change', function () {
                            const thisObj = this;
                            tableColumnSearch(that, thisObj);                            
                        });
                    });

                }
            });
            $(`${SelectorsWI.tblWorkInstructions} tfoot`).insertAfter($(`${SelectorsWI.tblWorkInstructions} thead`));
        },
        error: function (xhr, status, statusText) {
            console.log('An error occurred');
        }
    });


}

function activeOrInactive(data) {
    return data.active ? 'Active' : 'Inactive';
}

function tableColumnSearch(that, thisObj) {
    if (that.search() !== thisObj.value) {
        that
            .columns($(thisObj).parent().index() + ':visible')
            .search(thisObj.value)
            .draw();
    }
}

function deleteWrkIns(wIID) {
    pwIsf.confirm({
        title: 'Delete Work Instruction', msg: "Are you sure you want to delete this Work Instruction?",
        'buttons': {
            'Yes': {
                'action': function () {
                    pwIsf.addLayer({ text: 'Please wait ...' });
                    $.isf.ajax({
                        url: `${service_java_URL}${ConstText.deleteWorkInstructionUrl}/${wIID}/${signumGlobal}`,
                        context: this,
                        crossdomain: true,
                        processData: true,
                        contentType: 'application/json',
                        type: 'GET',
                        xhrFields: {
                            withCredentials: false
                        },
                        success: function (data) {

                            if (data.apiSuccess) {
                                pwIsf.alert({ msg: "successfully Deleted.", type: "success" });
                            }
                            location.reload();
                        },
                        error: function (xhr, status, statusText) {
                            pwIsf.alert({ msg: "Error in Deleting", type: 'warning' });
                        },
                        complete: function (xhr, statusText) {
                            pwIsf.removeLayer();
                        }
                    });
                }
            },
            'No': {
                'action': function () {
                    //Blank        
                }
            },

        }
    });

}

function toggleSourceStatusWrkIns(wIID) {   
    var checkbox = document.querySelector(`input[id="togBtnSource_${wIID}"]`);
    if (checkbox.checked) {
        $.isf.ajax({

            url: `${service_java_URL}${ConstText.deleteWorkInstructionUrl}/${wIID}/${signumGlobal}/true`,
            type: 'GET',
            success: function (data) {
                pwIsf.alert({ msg: 'Successfully Enabled.', type: 'success' });
                getWorkInstructions();
            },
            error: function (xhr, status, statusText) {
                pwIsf.alert({ msg: 'Error in Enabling/Disabling', type: 'warning' });
            },
            complete: function (xhr, statusText) {
                pwIsf.removeLayer();
            }
        });       
    } else {
        $.isf.ajax({
            url: `${service_java_URL}${ConstText.deleteWorkInstructionUrl}/${wIID}/${signumGlobal}/false`,
            type: 'GET',
            success: function (data) {      
                pwIsf.alert({ msg: 'Successfully Disabled.', type: 'success' });
                getWorkInstructions();
            },
            error: function (xhr, status, statusText) {
                pwIsf.alert({ msg: 'Error in Enabling', type: 'warning' });
            },
            complete: function (xhr, statusText) {
                pwIsf.removeLayer();
            }
        });
    }
}

function addWrkInsForm() {
    setvalueforID();
    pwIsf.addLayer({ text: 'Submitting Instructions please wait ...', progressId: 'uploadProgress', showSpin: false })

    let restData = {};
    restData = imgDataForAjax.getFormDataJson($('#Wrk_Ins_Add'));
    const opt = {};
    opt.setImgFldsJosn = [{ sendAs: 'workinstructionfile', frmElmntAs: 'workInstructionfile' }];
    opt.formId = 'Wrk_Ins_Add';

    const frmData = imgDataForAjax.getData(opt);
    frmData.append('workInstructionModel', JSON.stringify(restData['workInsModal']));
    const reqOpt = {};
    reqOpt.url = `${service_java_URL_VM}activityMaster/saveWorkInstruction`;
    reqOpt.progressBarId = 'uploadProgress';
    reqOpt.frmData = frmData;
    reqOpt.onDone = function (data) {
        pwIsf.alert({ msg: 'Successfully Added', type: 'success' });
        pwIsf.removeLayer({});
    };

    imgDataForAjax.sendRequest(reqOpt);

}

function setvalueforID() {
    const hiddenDomainval = $(SelectorsWI.WrkInsDomain).val();
    const hiddenVendorval = $(SelectorsWI.WrkInsVendorNme).val();
    const hiddenTechnologyval = $(SelectorsWI.WrkInsTchnlgy).val();
    const hiddencreatedby = signumGlobal;
    const hiddenmodifiedby = signumGlobal;
    const hiddenflowchartowner = signumGlobal;
    const hiddenactive = true;
    const hiddenversionrev = 1;

    $('#hiddenDomain').val(hiddenDomainval);
    $('#hiddenVendor').val(hiddenVendorval);
    $('#hiddenTechnology').val(hiddenTechnologyval);
    $('#hiddenflowchartowner').val(hiddenflowchartowner);
    $('#hiddenCreatedBy').val(hiddencreatedby);
    $('#hiddenLastModifiedBy').val(hiddenmodifiedby);
    $('#hiddenactive').val(hiddenactive);
    $('#hiddenrevision').val(hiddenversionrev);

}

function setvalueforIDUpdate() {
    const hiddenDomainval2 = $(SelectorsWI.WrkInsDomain2).val();
    const hiddenVendorval2 = $(SelectorsWI.WrkInsVendorNme2).val();
    const hiddenTechnologyval2 = $(SelectorsWI.WrkInsTchnlgy2).val();
    const hiddencreatedby2 = signumGlobal;
    const hiddenmodifiedby2 = signumGlobal;
    const hiddenflowchartowner2 = signumGlobal;

    $('#hiddenDomain2').val(hiddenDomainval2);
    $('#hiddenVendor2').val(hiddenVendorval2);
    $('#hiddenTechnology2').val(hiddenTechnologyval2);
    $('#hiddenflowchartowner2').val(hiddenflowchartowner2);
    $('#hiddenCreatedBy2').val(hiddencreatedby2);
    $('#hiddenLastModifiedBy2').val(hiddenmodifiedby2);

}

function updateWrkIns(thisObj) {

    $(SelectorsWI.updateWrkIns).on('show.bs.modal', function () {
        $(this).find('.modal-content').css({
            width: 'auto', //probably not needed
            height: 'auto', //probably not needed
            'max-width': '50%',
            'margin': 'auto'
        });
    });

    getVendorsWrkInsForUpdate();
    getDomainWrkInsForUpdate();
    getTechnologyWrkInsForUpdate();

    const jsonData = $(thisObj).data('details');
    console.log(jsonData);

    var html = `
                         <form id="Wrk_Ins_Update" enctype="multipart/form-data" method="POST" >
                                    <div class="col-md-12" id="WrkInsFileDiv2">
                                        <b> Attach File (PDF only): </b>
                                    <input class="btn btn-default" type="file" id="workInstructionfile2" name="workInstructionfile2" accept=".pdf">
                                    </div>
                                    <div class="col-md-12" id="WrkInsNmediv2">
                                       <b> Work Instruction Name: </b>
                                       <input type="text" class="form-control" id="textAreaWrkInsNme2" name="workInsModal2{}workInstructionName"
                                        rows="1" style="border:outset 2px lightblue; "value="${jsonData.workInstructionName}"/>
                                        </div>
                                <br />
                                    <div class="col-md-12" id="WrkInsdescDiv2">
                                        <label for="textAreaWrkIns"><b>Description :</b></label>
                                        <input type="text" class="form-control" id="textAreaWrkIns2" name="workInsModal2{}description"
                                        rows="1" style="border:outset 2px lightblue;" value="${jsonData.description}"/>
                                    </div>
                                <br />
                                    <div class="col-lg-12">
                                        <div class="panel panel-default">
                                            <div class="panel-heading">
                                                Applicable For
                                            </div>
                                            <div class="panel-body">
                                                <div id="WrkInsDomaindiv2" class="col-md-12">
                                                  <b>Domain:</b><br>
                                                    <select id="WrkInsDomain2" class="select2able">
                                                     <option value="${jsonData.domainID}">${jsonData.domain}</option>
                                                  </select>
                                                </div>
                                                <br />
                                                <div id="WrkInsTchnlgydiv2" class="col-md-12">
                                                   <b> Technology:</b>
                                                    <select id="WrkInsTchnlgy2" class="select2able">
                                                    <option value="${jsonData.technologyID}">${jsonData.technology}</option>
                                                    </select>
                                                </div>
                                                <br />
                                                <div id="WrkInsVendorNmediv2" class="col-md-12">
                                                  <b>Vendor:</b><br>
                                                    <select id="WrkInsVendorNme2" class="select2able select2-offscreen">
                                                      <option value="${jsonData.vendorID}">${jsonData.vendor}</option>
                                                  </select>
                                                </div>
                                                <br />
                                                <div class="col-md-12" id="WrkInsSwDiv2">
                                                    SW Release:
                                                    <input type="text" class="form-control" id="textAreaWrkInsSw2" name="workInsModal2{}sWrelease"
                                                    rows="1" style="border:outset 2px lightblue;" value="${jsonData.sWrelease}"/>
                                                </div>
                                                <br />
                                                <div id="WrkInsKpiNmediv2" class="col-md-12">
                                                    KPI Name:
                                                    <input type="text" class="form-control" id="textAreaWrkInsKpi2" name="workInsModal2{}kPIName"
                                                    rows="1" style="border:outset 2px lightblue;" value="${jsonData.kPIName}"/>
                                                </div>
                                                <input type="hidden" id="hiddenWiid2" name="workInsModal2{}wIID" value="${jsonData.wIID}" />
                                                <input type="hidden" id="hiddenDomain2" name="workInsModal2{}domainID" />
                                                <input type="hidden" id="hiddenVendor2" name="workInsModal2{}vendorID" />
                                                <input type="hidden" id="hiddenTechnology2" name="workInsModal2{}technologyID" />
                                                <input type="hidden" id="hiddenCreatedBy2" name="workInsModal2{}createdBy"  />
                                                <input type="hidden" id="hiddenLastModifiedBy2" name="workInsModal2{}modifiedBy" />
                                                <input type="hidden" id="hiddenflowchartowner2" name="workInsModal2{}flowchartOwner"  />
                                                <input type="hidden" id="hiddenrevision2" name="workInsModal2{}revNumber" value="${jsonData.revNumber}" />
                                                <input type="hidden" id="hiddenactive2" name="workInsModal2{}active" value="${jsonData.active}" />

                                            </div>
                                        </div>
                                    </div>
                                    <div class="row">
                                        <div class="col-md-2 pull-right">
                                            <button id="closeModalUpdate" type="button" class="btn btn-danger" onclick="closeWrkInsUpdateModal();">CLOSE</button>
                                        </div>
                                        <div class="col-md-2 pull-right">
                                            <button type="button" id="updateWorkInsButton" onclick="validationForUpdate();" class="btn btn-success">UPDATE</button>
                                        </div>

                                    </div>
</form>
                    `;

    $('#updateWrkInsBody').html('').append(html);

}

function updateWrkInsForm() {
    setvalueforIDUpdate();
    pwIsf.addLayer({ text: 'Submitting Instructions please wait ...', progressId: 'uploadProgress', showSpin: false })

    let restData = {};
    restData = imgDataForAjax.getFormDataJson($('#Wrk_Ins_Update'));
    const opt = {};
    opt.setImgFldsJosn = [{ sendAs: 'workinstructionfile', frmElmntAs: 'workInstructionfile2' }];
    opt.formId = 'Wrk_Ins_Update';

    const frmData = imgDataForAjax.getData(opt);
    frmData.append('workInstructionModel', JSON.stringify(restData['workInsModal2']));
    const reqOpt = {};
    reqOpt.url = `${service_java_URL_VM}activityMaster/editWorkInstruction`;
    reqOpt.progressBarId = 'uploadProgress';
    reqOpt.frmData = frmData;
    reqOpt.onDone = function (data) {
        pwIsf.alert({ msg: 'Successfully Updated', type: 'success' });
        pwIsf.removeLayer({});
    };

    imgDataForAjax.sendRequest(reqOpt);

}


function validationForAdd() {
    const namev = $("#textAreaWrkInsNme").val();
    const descriptionv = $("#textAreaWrkIns").val()
    const domainv = $(SelectorsWI.WrkInsDomain).val();
    const technologyv = $(SelectorsWI.WrkInsTchnlgy).val();
    const vendorv = $(SelectorsWI.WrkInsVendorNme).val();
    const swv = $("#textAreaWrkInsSw").val();
    const KPIv = $("#textAreaWrkInsKpi").val();
    const filev = $('#workInstructionfile').val();
    const workInstPDF = $('#workInstructionfile').val().toLowerCase();
    const fileExt = getExtension(workInstPDF);
    if (namev === "") {
        alert(" Work Instruction Name is mandatory.");
        $("#WrkInsNmediv").css("color", "red");
    }
    else if (descriptionv === "") {
        alert("Description is mandatory.");
        $("#WrkInsdescDiv").css("color", "red");

    }
    else if (domainv === "") {
        alert("Domain is mandatory.");
        $("#WrkInsDomaindiv").css("color", "red");

    }
    else if (technologyv === "") {
        alert("Technology is mandatory.");
        $("#WrkInsTchnlgydiv").css("color", "red");

    }
    else if (vendorv === "") {
        alert("Vendor is mandatory.");
        $("#WrkInsVendorNmediv").css("color", "red");

    }
    else if (swv === "") {
        alert("SW Release mandatory.");
        $("#WrkInsSwDiv").css("color", "red");

    }
    else if (KPIv === "") {
        alert("KPI Name is mandatory.");
        $("#WrkInsKpiNmediv").css("color", "red");

    }
    else if (filev === "") {
        alert("File Upload is mandatory.");
        $("#WrkInsFileDiv").css("color", "red");
    }
    else if (fileExt !== C_PDF_EXT)
    {
        alert(C_WRONG_TYPE);
        $("#WrkInsFileDiv").css("color", "red");
    }
    else {
        addWrkInsForm();
    }

}


function validationForUpdate() {

    const namev2 = $("#textAreaWrkInsNme2").val();
    const descriptionv2 = $("#textAreaWrkIns2").val()
    const domainv2 = $(SelectorsWI.WrkInsDomain2).val();
    const technologyv2 = $(SelectorsWI.WrkInsTchnlgy2).val();
    const vendorv2 = $(SelectorsWI.WrkInsVendorNme2).val();
    const swv2 = $("#textAreaWrkInsSw2").val();
    const KPIv2 = $("#textAreaWrkInsKpi2").val();

    const filev2 = $('#workInstructionfile2').val();

    if (namev2 === "") {
        alert(" Work Instruction Name is mandatory.");
        $("#WrkInsNmediv2").css("color", "red");
    }
    else if (descriptionv2 === "") {
        alert("Description is mandatory.");
        $("#WrkInsdescDiv2").css("color", "red");

    }
    else if (domainv2 === "") {
        alert("Domain is mandatory.");
        $("#WrkInsDomaindiv2").css("color", "red");

    }
    else if (technologyv2 === "") {
        alert("Technology is mandatory.");
        $("#WrkInsTchnlgydiv2").css("color", "red");

    }
    else if (vendorv2 === "") {
        alert("Vendor is mandatory.");
        $("#WrkInsVendorNmediv2").css("color", "red");

    }
    else if (swv2 === "") {
        alert("SW Release mandatory.");
        $("#WrkInsSwDiv2").css("color", "red");

    }
    else if (KPIv2 === "") {
        alert("KPI Name is mandatory.");
        $("#WrkInsKpiNmediv2").css("color", "red");

    }
    else if (filev2 === "") {
        alert("File Upload is mandatory.");
        $("#WrkInsFileDiv2").css("color", "red");

    }
    else {
        updateWrkInsForm();
    }

}




