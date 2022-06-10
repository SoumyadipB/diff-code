let callingPage = '';
let oldStepsUnsatisfied = [];
let countOldStep = 0;
let currentDateInFormatYMD = formatted_date(new Date());


//constants
const botSavingsConstants = {
    botSavingTable: '#botSavingTable',
    deletedStepTable: '#deletedStepTable',
    oldJsonArr: '',
    allOldStepsSelected: {},
    deletedOldSteps: {},

}

$(document).ready(function () {
    callingPage = $('#callingPage').val();
})

$(document).on('destroy.dt', function (e, settings) {
    if (settings.sTableId === 'botSavingTable') {
        var api = new $.fn.dataTable.Api(settings);
        api.off('order.dt');
        api.off('preDraw.dt');
        api.off('column-visibility.dt');
        api.off('search.dt');
        api.off('page.dt');
        api.off('length.dt');
        api.off('xhr.dt');
    }
});

let botDataJson = new Object;
function calculateSavings(obj) {
    let sum = 0;
    let is20WOExecuted = $(obj).data('is20WOExecuted');
    let isAll20WOExecuted = $(obj).data('isAll20WOExecuted');
    let foundInNewSteps = false;
    let foundInOldSteps = false;
    let isNewSelectHasManual = false;
    let calculateFor80 = 0;
    let calculatedFor120 = 0;
    const currSelectedNewStep = $(obj).data('selectedNewStep');
    const currSelectedOldStep = $(obj).data('selectedOldStep');
    const currSavingText = $(obj).closest('tr').find('#savingsText');
    const currEpectedSavingText = $(obj).closest('tr').find('#expectedSavingsText');
    let currentSavingYear = "";
    let botID = "";

    if (callingPage === C_BOTSAVING) {
        currentSavingYear = $(obj).closest('tr').find("#calculateSavingYear").val();
        botID = $('input[name=botsaving]:checked').data('details').botid;
    }
    if (!currSelectedNewStep.filter(function (e) {
        if (callingPage === C_BOTSAVING) {
            return e.botID === $(obj).closest('tr').find('td:eq(0)')[0].innerText;
        }
        else {
            return e.ExecType === C_EXEC_TYPE_AUTO;
        }
    }).length > 0) {
        let message = callingPage === C_BOTSAVING ?
            `Please select step with BotID ${botID} in Bot Saving Details table`
            : `Please select one Automatic step in Bot Saving Details table`;
        pwIsf.alert({ msg: message, type: C_ERROR });
    }
    else if ($(currEpectedSavingText).val() == '') {
        pwIsf.alert({ msg: C_ERR_EXPECTED_SAVINGS, type: C_ERROR });
    }
    else {
        updateSelectedOldStepWithSavings();

        // New scenario if isPreCondtionSatisfied is false
        if ($(obj).data('isPreCondtionSatisfied') === false) {
            let expectedSavings = (Number)($(currEpectedSavingText).val());
            $(currSavingText).val(0);
            $(obj).data('expectedSavings', expectedSavings);
            $(obj).data('saving', 0);
            Remark = `${C_ZERO_SAVINGS}${formatted_date(new Date())}, by ${signumGlobal}`;
            if (callingPage !== C_BOTSAVING) {
                let WFVersion = urlParams.get("version");
                if (WFVersion == 0) {
                    Remark = `${C_ZERO_SAVINGS_WITH_NEW_WF} ${signumGlobal}`;
                }
            }
              $(obj).data('Remark', Remark);
            isSavingFlag = "false";
            $(obj).data('isSavingFlag', isSavingFlag);
            if (callingPage === C_BOTSAVING) {
                $(obj).data('currentSavingYr', currentSavingYear);
            }
        }

        //Scenario1- No old Step, one new auto step with new BOT ID.
        else if (currSelectedNewStep.length != 0 && currSelectedOldStep.length == 0) {
            foundInNewSteps = currSelectedNewStep.some(function (el) {
                if (el.ExecType === C_EXEC_TYPE_AUTO) {
                    return el.ExecType;
                }
            });
            isNewSelectHasManual = currSelectedNewStep.some(function (el) {
                if (el.ExecType === C_EXEC_TYPE_MAN) {
                    return el.ExecType;
                }
            });

            if (foundInNewSteps && !isNewSelectHasManual) {
                //Expected savings is Actual Savings.
                const expectedSavings = (Number)($(currEpectedSavingText).val());
                $(currSavingText).val(expectedSavings);
                $(obj).data('expectedSavings', expectedSavings);
                $(obj).data('saving', $(currSavingText).val());
                Remark = `${C_ZERO_SAVINGS}${formatted_date(new Date())}, by ${signumGlobal}`;
              //  $(obj).data('Remark', C_SAVINGS_AS_EXPECTED_SAVINGS);
                $(obj).data('Remark', Remark);
                isSavingFlag = "true";
                $(obj).data('isSavingFlag', isSavingFlag);
                if (callingPage === C_BOTSAVING) {
                    $(obj).data('currentSavingYr', currentSavingYear);
                }
            }
            else {
                $(currSavingText).val(C_TO_BE_CALCULATED);
                isSavingFlag = "false";
                $(obj).data('expectedSavings', $(currEpectedSavingText).val());
                $(obj).data('saving', 0);
                $(obj).data('isSavingFlag', isSavingFlag);
                $(obj).data('Remark', C_SAVINGS_TO_BE_CALCULATED);
                if (callingPage === C_BOTSAVING) {
                    $(obj).data('currentSavingYr', currentSavingYear);
                }
            }
        }
        //end of scenario1

        //Scenario2- IF few old manual/automatic steps, and one new auto step with new BOTID.
        else if (currSelectedNewStep.length !== 0 && currSelectedOldStep.length !== 0 &&
            !currSelectedNewStep.filter(e => e.ExecType === C_EXEC_TYPE_MAN).length > 0 &&
            !currSelectedOldStep.filter(e => e.ExecType === C_EXEC_TYPE_AUTO).length > 0) {

            foundInNewSteps = currSelectedNewStep.some(function (el) {
                return el.ExecType === C_EXEC_TYPE_AUTO;
            });

            foundInOldSteps = currSelectedOldStep.some(function (el) {
                return el.ExecType === C_EXEC_TYPE_MAN;
            });

            //2.a 20WOs for old manual steps is executed, saving to be shown instantly in savings textbox.
            if (is20WOExecuted) {
                let expectedSaving = 0;
                let savings = 0;
                let calcsumArr = currSelectedOldStep.map(function (value) {
                    if (value.Saving == C_NA) {
                        return 0;
                    }
                    return (Number)(value.Saving)
                });

                if (!calcsumArr.includes(NaN) || !calcsumArr.includes("null")) {
                    sum = calcsumArr.reduce((a, b) => a + b, 0);
                    sum = Math.round(sum * 100) / 100;
                    $(currSavingText).val(sum);
                }

                if ($(currEpectedSavingText).val() != '') {
                    savings = sum;
                    $(currSavingText).val(savings);
                    expectedSaving = (Number)($(currEpectedSavingText).val());
                    calculateFor80 = 0.8 * expectedSaving;
                    calculatedFor120 = 1.2 * expectedSaving

                    if (savings >= calculateFor80 && savings <= calculatedFor120) {
                        pwIsf.alert({ msg: C_SAVINGS_CALC_IN_RANGE, type: C_WARNING });
                        if (isAll20WOExecuted) {
                           //  Remark = C_SAVINGS_CALC_IN_RANGE;                          
                            Remark = C_EME_CALCULATED;
                            isSavingFlag = "true";
                        }
                        else {
                            Remark = C_MIN_WO_CONDITION_FAIL;
                            isSavingFlag = "false";
                        }
                    }
                    else {
                        pwIsf.alert({ msg: C_SAVINGS_CALC_NOT_IN_RANGE, type: C_WARNING });

                        if (isAll20WOExecuted) {
                            //Remark = "Saving calculated is not in range as per user Expected Savings. Calculated Savings saved.";
                            Remark = C_EME_CALCULATED;
                           
                        }
                        else {
                            Remark = C_MIN_WO_CONDITION_FAIL;
                        }
                        isSavingFlag = "false";
                    }

                    $(obj).data('expectedSavings', expectedSaving);
                    $(obj).data('saving', savings);
                    $(obj).data('isSavingFlag', isSavingFlag);
                    $(obj).data('Remark', Remark);
                    if (callingPage === C_BOTSAVING) {
                        $(obj).data('currentSavingYr', currentSavingYear);
                    }
                }
                else {
                    pwIsf.alert({ msg: C_ERR_EXPECTED_SAVINGS, type: C_WARNING });
                }
            }

            //2.b 20WOs for old manual steps in not executed
            else {
                $(currEpectedSavingText).prop('disabled', false);
                let expectedSaving = (Number)($(currEpectedSavingText).val());
                $(currSavingText).val(expectedSaving);
                //Remark = C_NO_SAVINGS_AVAILABLE;
                Remark = `${C_ZERO_SAVINGS}${formatted_date(new Date())}, by ${signumGlobal}`;
                pwIsf.alert({
                    msg: C_NO_SAVINGS_AVAILABLE_ALERT, type: C_WARNING
                })

                $(obj).data('expectedSavings', expectedSaving);
                $(obj).data('saving', $(currSavingText).val());
                $(obj).data('Remark', Remark);
                isSavingFlag = "false";
                $(obj).data('isSavingFlag', isSavingFlag);
                if (callingPage === C_BOTSAVING) {
                    $(obj).data('currentSavingYr', currentSavingYear);
                }
            }
        }

        //3. if 1+ manual step deleted and 1+ manual and 1+ auto bot in new step added.
        else if (currSelectedNewStep.length != 0 && currSelectedOldStep.length != 0 &&
            currSelectedNewStep.some(function (el) {
            return el.ExecType === C_EXEC_TYPE_MAN;
            })) {
            
            //when auto task deleted and (auto + manual) are added
            if (currSelectedOldStep.filter(e => e.ExecType === C_EXEC_TYPE_AUTO).length > 0 && !currSelectedOldStep.filter(e => e.ExecType === C_EXEC_TYPE_MAN).length > 0) {
                let sumOfDeletedAutoStepSavings = currSelectedOldStep.map(function (value) {
                    if (value.Saving == C_NA) {
                        return 0;
                    }
                    return (Number)(value.Saving);
                });
                
                if (!sumOfDeletedAutoStepSavings.includes(NaN) || !sumOfDeletedAutoStepSavings.includes("null")) {
                    sum = sumOfDeletedAutoStepSavings.reduce((a, b) => a + b, 0);
                    sum = Math.round(sum * 100) / 100;
                }

                let savings = sum;
                let expectedSavings = (Number)($(currEpectedSavingText).val());
                calculateFor80 = 0.8 * expectedSavings;
                calculatedFor120 = 1.2 * expectedSavings;
                let rpaid = "";
                 if (savings >= calculateFor80 && savings <= calculatedFor120) {
                     pwIsf.alert({ msg: C_SAVINGS_CALC_IN_RANGE, type: C_WARNING });
                    $.each(currSelectedOldStep, function (i, d) {
                        if (d.Saving == C_NA) {
                            //DO NOTHING
                        }
                        else {
                            if (d.botID != undefined) {
                                rpaid = `${rpaid}${d.botID} `;
                            }
                            else {
                                rpaid = `${rpaid}${d.RpaID} `;
                            }
                        }
                    })
                     
                    rpaid = getUniqueData(rpaid);
                    Remark = `${C_PARTIAL_SAVINGS_TO_NEW_AUTO_STEP1} ${rpaid} ${C_PARTIAL_SAVINGS_TO_NEW_AUTO_STEP2}`;
                    isSavingFlag = "true";
                }
                else {
                     pwIsf.alert({ msg: C_SAVINGS_CALC_NOT_IN_RANGE, type: C_WARNING });
                     $.each(currSelectedOldStep, function (i, d) {
                         if (d.Saving == C_NA) {
                             //DO NOTHING
                         }
                         else {
                             if (d.botID != undefined) {
                                 rpaid = `${rpaid}${d.botID} `;
                             }
                             else {
                                 rpaid = `${rpaid}${d.RpaID} `;
                             }
                         }
                     })
                     rpaid = getUniqueData(rpaid);
                     Remark = `${C_PARTIAL_SAVINGS_TO_NEW_AUTO_STEP1} ${rpaid} ${C_PARTIAL_SAVINGS_TO_NEW_AUTO_STEP2}`;
                     isSavingFlag = "true";
                 }

                $(currSavingText).val(savings);                
                $(obj).data('expectedSavings', $(currEpectedSavingText).val());
                $(obj).data('saving', $(currSavingText).val());
                $(obj).data('isSavingFlag', isSavingFlag);
                $(obj).data('Remark', Remark);
             }
             else {
                $(currSavingText).val(C_TO_BE_CALCULATED);
                isSavingFlag = "false";
                $(obj).data('expectedSavings', $(currEpectedSavingText).val());
                $(obj).data('saving', 0);
                $(obj).data('isSavingFlag', isSavingFlag);
                $(obj).data('Remark', C_SAVINGS_TO_BE_CALCULATED);
             }
            
             if (callingPage === C_BOTSAVING) {
                $(obj).data('currentSavingYr', currentSavingYear);
             }
         }

        //4. if 1 old bot selected and 1 new bot selected
        else if (currSelectedNewStep.length != 0 && currSelectedOldStep.length != 0 &&
            !currSelectedNewStep.filter(e => e.ExecType === C_EXEC_TYPE_MAN).length > 0) {
            
            let calcSumArr1 = currSelectedOldStep.map(function (value) {
                if (value.Saving == C_NA) { return 0; }
                return (Number)(value.Saving)
            });

            if (!calcSumArr1.includes(NaN) || !calcSumArr1.includes("null")) {
                sum = calcSumArr1.reduce((a, b) => a + b, 0);
                sum = Math.round(sum * 100) / 100;
            }
            //Expected savings is Actual Savings.

            let savings = sum;
            let expectedSavings = (Number)($(currEpectedSavingText).val());
            $(currSavingText).val(savings);
            calculateFor80 = 0.8 * expectedSavings;
            calculatedFor120 = 1.2 * expectedSavings;

            if (savings >= calculateFor80 && savings <= calculatedFor120) {
                pwIsf.alert({ msg: C_SAVINGS_CALC_IN_RANGE, type: C_WARNING });
                if (is20WOExecuted) {
                    if (isAll20WOExecuted) {
                        //Remark = C_SAVINGS_CALC_IN_RANGE;
                        Remark = C_EME_CALCULATED;
                        isSavingFlag = "true";
                    }
                    else {
                        Remark = C_MIN_WO_CONDITION_FAIL;
                        isSavingFlag = "false";
                    }
                }
                else {
                   //Remark = C_NO_SAVINGS_AVAILABLE_FOR_MANUAL;
                        Remark = C_EME_CALCULATED;
                        isSavingFlag = "false";
                }
            }
            else {
                pwIsf.alert({ msg: C_SAVINGS_CALC_NOT_IN_RANGE, type: C_WARNING });
                if (is20WOExecuted) {
                    if (isAll20WOExecuted) {
                      //  Remark = "Saving calculated is not in range as per user Expected Savings. Calculated Savings saved.";
                        Remark = C_EME_CALCULATED;
                    }
                    else { Remark = "The minimum WOs were not found. Calculated with available data. Recheck parent mapping."; }
                    isSavingFlag = "false";
                }
                else {
                   // Remark = C_NO_SAVINGS_AVAILABLE_FOR_MANUAL;
                    Remark = C_EME_CALCULATED;
                    isSavingFlag = "false";
                }
            }

            $(obj).data('expectedSavings', expectedSavings);
            $(obj).data('saving', $(currSavingText).val());
            $(obj).data('Remark', Remark);
            $(obj).data('isSavingFlag', isSavingFlag)
            if (callingPage === C_BOTSAVING) {
                $(obj).data('currentSavingYr', currentSavingYear);
            }
        }

        else {
            $(currEpectedSavingText).val("");
            $(currSavingText).val("");
        }

        $('#saveBotDetailsBtn').prop('disabled', false);
        $(obj).data('isSavingsCalculated', true);
    }
}

function getDeletedStepTable(data, oldversionsteps = false) {
    if (!oldversionsteps) { deletedStepData = JSON.parse(JSON.stringify(data)); }
    //  $("#deletedStepTable").append($('<tfoot><tr><th>Step Name</th><th>Task Name</th><th>Execution Type</th><th>RPAID</th><th>Execution/Saving Hours</th></tr></tfoot>'));
    oDelTable = $('#deletedStepTable').DataTable({
        searching: true,
        responsive: true,
        "pageLength": 10,
        "data": data.deletedSteps,
        "destroy": true,
        "autoWidth": false,
        colReorder: true,
        dom: 'Bfrtip',
        "bPaginate": false,
        "bFilter": false,
        buttons: [
            //    'colvis', 'excelHtml5'
        ],
        "columns": [
            { "title": "Step ID", "data": "StepID" },            
            {
                "title": "Step Name", "data": "null",
                "render": function (d, t, r) {
                    if (!r.flag) {
                        let $label = $(`<label style="text-align: left"> ${r.StepName}.</br><span style="color:red;font-size:11px">${r.msg}</span></label>`)
                        return $label.prop("outerHTML");
                    }
                    else {
                        let $label = $(`<label style="text-align: left">${r.StepName}.</br><span style="color:blue;font-size:11px">${r.msg}</span></label>`)
                        return $label.prop("outerHTML");
                    }
                }
            },
            { "title": "Def ID", "data": "defId" },
            { "title": "Task Name", "data": "task" },
            { "title": "Execution Type", "data": "ExecutionType" },
            { "title": "RPAID", "data": "RpaID" },
            {
                "title": "Execution/Saving Hours", "data": "Savings", "defaultContent": "<i>NA</i>",
                "render": function (d, t, r) {
                    if (r.ExecutionType == "Automatic" && r.Savings == null) {
                        var $button = $(`<button id='btn_${r.StepID}' class='calSavingFromExisting eds btn-default' onclick='calSavingFromExisting(this)'>Calc From Other Project <i class='fa fa-info-circle fa-lg' title='Calcuate savings for BOT from other projects if present' style='font-size: 14px;color: #0C0C0C';></i></button>`, {});
                        return $button.prop("outerHTML");
                    }
                    else {
                        if (r.Savings == null || r.Savings == "NA") { r.Savings = 0 }
                        let convert = (Number)(r.Savings);
                        return Math.round(convert * 100) / 100;
                    }
                }
            }
        ],
        "fnRowCallback": function (nRow, aData, iDisplayIndex, iDisplayIndexFull) {
            if (aData["ExecutionType"] == "Automatic") {
                $('td', nRow).css('background-color', '#f39b17');

            }
            else if (aData["ExecutionType"] == "Manual") {
                $('td', nRow).css('background-color', '#31d0c6');

            }
        },
        "columnDefs": [
            {
                "targets": [0],
                "visible": false,
                "searchable": true
            },
            { "targets": [2], "width": "55px" },
            { "targets": [4], "width": "85px" },
            { "targets": [5], "width": "85px" },
            { "targets": [6], "width": "110px" },
            { "targets": [1, 3], "className":"wrapText"}
        ],
        initComplete: function () {

        }
    });
    oDelTable.columns.adjust().draw();

}

function getNewStepTable(data) {

    var newStepData = data;

    oNewTable = $('#newStepTable').DataTable({
        searching: true,
        responsive: true,
        "pageLength": 10,
        "data": newStepData.addedSteps,
        "destroy": true,
        "autoWidth": false,
        colReorder: false,
        dom: 'Bfrtip',
        "bPaginate": false,
        "bFilter": false,
        buttons: [
            //   'colvis', 'excelHtml5'
        ],
        "columns": [
            { "title": "Step ID", "data": "StepID" },
            { "title": "Step Name", "data": "StepName"},
            { "title": "Task Name", "data": "task"},
            { "title": "Execution Type", "data": "ExecutionType", "width": "85px" },
            { "title": "RPAID", "data": "RpaID", "width": "85px" },
            {
                "title": "Execution/Saving Hours", "data": "Savings", "defaultContent": "<i>NA</i>", "width": "110px",
                "render": function (d, t, r) {
                    if (r.ExecutionType == "Automatic" && (r.Savings != null || r.Savings != "NA")) {
                        var $button = $(`<button id='btn_${r.StepID}' class='calSavingFromExisting eds btn-default' onclick='calSavingFromExisting(this)'>Calc From Other Project <i class='fa fa-info-circle fa-lg' title='Calcuate savings for BOT from other projects if present' style='font-size: 14px;color: #0C0C0C';></i></button>`, {});
                        return $button.prop("outerHTML");
                    }
                    else {
                        if (r.Savings == null || r.Savings == "NA") { r.Savings = 0 }
                        let convert = (Number)(r.Savings);
                        return Math.round(convert * 100) / 100;
                    }
                }

            },

        ],
        "fnRowCallback": function (nRow, aData, iDisplayIndex, iDisplayIndexFull) {
            if (aData["ExecutionType"] == "Automatic") {
                $('td', nRow).css('background-color', '#f39b17');
            }
            else if (aData["ExecutionType"] == "Manual") {
                $('td', nRow).css('background-color', '#31d0c6');
            }
        },
        "columnDefs": [
            {
                "targets": [0],
                "visible": false,
                "searchable": true
            },
            { "targets": [1, 2], "className": "wrapText" }
        ],
        initComplete: function () {

        }
    });
    oNewTable.columns.adjust().draw();      
}

// 3
function getBOTTable(data) {
    var BotData = [];
    // data.BotId = botID
    BotData = data;
    oBOTTable = $('#botSavingTable').DataTable({
        "autoWidth": false,
        searching: true,
        responsive: true,
        // "pageLength": 10,
        "data": BotData,
        "destroy": true,
        colReorder: true,
        dom: 'Bfrtip',
        "bPaginate": false,
        "bFilter": false,
        rowsGroup: [0, 4, 5, 6, 7, 8],
        fixedHeader: false,
        buttons: [
            // 'colvis', 'excelHtml5'
        ],
        "columnDefs": [{ "width": "2%", "targets": 0 }, { "width": "7%", "targets": 1 }, { "width": "4%", "targets": 2 },
            { "width": "9%", "targets": 3 }, { "width": "9%", "targets": 4 },{ "width": "8%", "targets": 5 },
            { "width": "5%", "targets": 6 }, { "width": "10%", "targets": 7 }, { "width": "7%", "targets": 8 }
              
        ],
        "columns": [
            //{ "title": "Project ID", "data": "projectID" },
            {
                "title": "BOT ID", "data": function (d, t, r, m) { return `newStep_${d.autoStepId}`; },
                "orderable": false,
                "render": function (d, t, r, m) {
                    return r.BotId;
                }
            },
            {
                "title": "Workflow", "data": null, "orderable": false, "className": "ruleDescStyling", "defaultContent": "",
                "render": function (d, t, r, m) {
                    return `<span><a id="deleteWF" style="color: black !important;font-size: 14px;text-decoration: none !important;" class="details-control collapseButton fa fa-trash-o" data-defid="${d.defId}" data-autoid="${d.autoStepId}" data-wfname="${d.wfName}" data-wfid="${d.wfId}" data-version="${d.version}" onclick="removeWfVersion(${m.row}, '${d.autoStepId}', '${d.version}', this)"></a> <span text-overflow="ellipsis" title="${d.wfName}">${d.wfName}</span></span>`
                }
            },
            { "title": "Version", "data": "version", "orderable": false, "defaultContent": "" },
            {
                "title": "Old Step <i class='fa fa-info-circle fa-lg' title='Deleted Steps list. Minimum 3 characters required!' style='font-size: 14px;color: #0C0C0C';></i>", "targets": 'no-sort', "orderable": false, "searchable": false, "data": null,
                "defaultContent": '',
                "render": function (d, t, r) {
                    let borderColor = d.isStepPreConditionSatisfied ? "#DCDCDC":"#FF0000";
                    var $select = $(`<span id='oldstep'><textarea id='oldStepDropDown' onselect='preventSelect(this)' onkeydown='preventClear(this, event)' class='oldStepDropDown autoCompleteTextArea' style='width: 100%; height:80px; max-height: 80px;max-width: 80px ; min-width: 80px ; border: 1px solid ${borderColor};'></textarea><input id='autoText' type='hidden' value='${d.oldStepSelectedItems}'><span class="refresh" style="cursor:pointer;" title="Clear selected old steps." onclick="clearSelectedOldSteps(this);">
                                <i class="fa fa-refresh fa-fw pull-right"></i><br>
                               </span> <i class='fa fa-info-circle fa-lg oldStepTool6619' onmouseover='oldstepToolTip(this,event)' style='font-size: 14px;color: #0C0C0C';></i><span id='oldsteptooltip' style='z-index:100000 !important; display:none'></span></span>`);
                    return $select.prop("outerHTML");
                },
            },
            {
                "title": "New Step* <i class='fa fa-info-circle fa-lg' title='New/Modified Steps added.' style='font-size: 14px;color: #0C0C0C';></i>", "targets": 'no-sort', "orderable": false, "searchable": false, "data": function (d, t, r, m) { return `newStep_${d.autoStepId}`; },
                "defaultContent": '',
                "render": function (d, t, r, m) {
                    var $select = $(`<select id='newStepDropdown_${r.BotId}' class='select2able newStepDropdown' style='width: 100%;' onchange='getNewStepList(this)' multiple></select>`, {
                        "value": r.addedSteps,
                        // "multiple":true
                    });
                    $.each(r.addedSteps, function (k, v) {
                        if (v.Savings == null || v.Savings == "NA") { v.Savings = 0 }
                        if (v.ExecutionType == "Automatic") {
                            var $option = $("<option></option>", {
                                "text": "A/" + v.StepName,
                                "value": `${v.StepID}_${v.ExecutionType}_${v.RpaID}_${(Number)(v.Savings)}`,
                                "selected": r.newStepSelectedItems.includes(v.StepID)
                            });
                        }
                        else {
                            var $option = $("<option></option>", {
                                "text": "M/" + v.StepName,
                                "value": `${v.StepID}_${v.ExecutionType}_${v.RpaID}_${(Number)(v.Savings)}`,
                                "selected": r.newStepSelectedItems.includes(v.StepID)
                            });
                        }



                        $select.append($option);
                    });
                    return $select.prop("outerHTML");
                }
            },
            {
                "title": "Expected Savings (Hrs)* <i class='fa fa-info-circle fa-lg' title='Savings expected by user for selected BOT' style='font-size: 14px;color: #0C0C0C';></i>", "targets": 'no-sort', "orderable": false, "searchable": false, "data": function (d, t, r, m) { return `expectedSavings_${d.autoStepId}`; },
                "defaultContent": '',

                "render": function (d, t, r) {
                    return `<div style="display:flex">
<input type="number" id="expectedSavingsText" class="expectedSavingsText pull-left" value="0" style="width:30px;margin-left: 0px;padding-bottom: 7px;" onchange="updateIsSavingsCalculated(this)" onkeyPress="isNumberKey(event);updateIsSavingsCalculated(this)">
<button class="btnSpin" id="descValue" onclick="descButtonValue(this)" style="margin-left: 2px;border-right-width: 1px;margin-right: 2px;"><i class="fa fa-angle-down"></i></button>
<button class="btnSpin" id="incValue" onclick="incButtonValue(this)"><i class="fa fa-angle-up"></i></button></div>`



                }
            },
            {
                "title": "Savings (hrs) <i class='fa fa-info-circle fa-lg' title='Actual savings calculated for selected BOT' style='font-size: 14px;color: #0C0C0C';></i>", "targets": 'no-sort', "orderable": false, "searchable": false, "data": function (d, t, r, m) { return `savings_${d.autoStepId}`; },
                "defaultContent": '',
                "render": function (d, t, r) {
                    var $input = $("<input id='savingsText' class='savingsText' disabled style='width:100%'></input>", {});
                    return $input.prop("outerHTML");
                },
            },
            {
                "title": "Calculate Saving Year", "targets": 'no-sort', "orderable": false, "searchable": false, "data": function (d, t, r, m) { return `calcSavingsYr_${d.autoStepId}`; },
                "defaultContent": '',
                "render": function (d, t, r) {
                    var $select = $("<select id='calculateSavingYear' class='select2able calculateSavingYear' style='width: 100%;'><option value='Yes'>YES</option><option value='No'>No</option></select>", {
                        //"value": d.addedSteps,
                        // "multiple":true
                    });
                    return $select.prop('outerHTML');
                }
            },
            {
                "title": "Calculate", "targets": 'no-sort', "orderable": false, "searchable": false, "data": function (d, t, r, m) { return `calcSavings_${d.autoStepId}`; },
                "defaultContent": '',
                "render": function (d, t, r) {
                    let $button = $("<button id='calculateSavings' class='calculateSavings eds btn-default' onclick='calculateSavings(this)' disabled>Calculate </br> Savings</button>", {});
                    return $button.prop("outerHTML");
                }
            },
        ],

        initComplete: function () {

            const table = $('#botSavingTable').DataTable();
            const column = table.column(7);
            if (callingPage !== "botSaving") {
                column.visible(!column.visible());
            }
            $('.select2able').select2({ placeholder: "Select One" });
        },
        "createdRow": function (row, data, index) {
            $('td', row).eq(1).addClass('selectwidth');
            $('td', row).eq(2).addClass('selectwidth');
        },
        drawCallback: function () {
            //BLANK
        },
        rowCallback: function (row, data) {
            $(row).find('#oldStepDropDown').data('selectedItems', data.oldStepSelectedData);
            $(row).find('#oldStepDropDown').val(data.oldStepSelectedItems);
        }
    });

    $('#botDetailModal').modal('show');
    oldStepsAutoComplete();
    oBOTTable.columns.adjust().draw();
}

function preventClear(thisObj, event) {
    if (event.which == 8 || event.which == 46) {
        event.preventDefault();
    } 
}

function preventSelect(thisObj) {
    thisObj.selectionStart = thisObj.selectionEnd;
}

function calSavingFromExisting(obj) {
    let currtable = $(obj).closest('table')[0].id;
    let currentStepID = $('#' + currtable).dataTable().fnGetData($(obj).closest('td')).StepID;
    let currentBotID = $('#' + currtable).dataTable().fnGetData($(obj).closest('td')).RpaID;
    let tableOldDefId = $(obj).closest('tr').find('td:eq(1)').text();
    let currentTableName = $($(obj).closest('table'))[0].id;

    let autoSavingFromOtherProjObject = Object();
    autoSavingFromOtherProjObject.bOTID = currentBotID;
    autoSavingFromOtherProjObject.newDefID = parseInt($('#newDefId').val());
    autoSavingFromOtherProjObject.newVersion = parseInt($('#newVersion').val());
    autoSavingFromOtherProjObject.oldDefID = currentTableName === 'newStepTable' ? parseInt($('#oldDefID').val()) : parseInt(tableOldDefId);
    if (callingPage === FLOWCHARTADD) {
        autoSavingFromOtherProjObject.oldStepId = currentStepID;
    } else if (callingPage === C_BOTSAVING) {
        autoSavingFromOtherProjObject.oldStepId = $('input[name=botsaving]:checked').data('details').BotStepID;
    }
    else if (callingPage === FLOWCHARTBOTEDIT) {
        autoSavingFromOtherProjObject.oldStepId = urlParams.get('stepId');
    }


    pwIsf.addLayer({ text: 'Please wait ...' });
    return $.isf.ajax({

        type: "POST",
        async: false,
        contentType: 'application/json',
        data: JSON.stringify(autoSavingFromOtherProjObject),
        url: service_java_URL + "flowchartController/getSavingForAutomaticStepForOthers",

        xhrFields: {
            withCredentials: false
        },
        success: function (response) {
            pwIsf.alert({ msg: response.msg, type: "info", autoClose: 2 });
            $(".newStepDropdown").val("").trigger('change');
            $('.savingsText').val('');
            $('.expectedSavingsText').val('0')

            
            let closestTd = $($(obj).closest('td'))[0];
            let getBotTableData = $('#botSavingTable').dataTable().fnGetData();
            let currentRowBtnClicked = $('#' + currentTableName).dataTable().fnGetData($(obj).closest('tr'));
            let getTableData = []; let currSelectArr = ''
            getTableData = $('#' + currentTableName).dataTable().fnGetData();

            let oldVersion = '';
            if ($('#wfVersionSelect').val() != null) {
                oldVersion = $('#wfVersionSelection').val().split('_')[0];
            }

            if (response.Savings == null) {

                for (let e in getTableData) {
                    if (currentTableName == "deletedStepTable" && getTableData[e].StepID == currentRowBtnClicked.StepID && parseInt(getTableData[e].defId) === parseInt(currentRowBtnClicked.defId)) {
                        $(closestTd).html(`<input placeholder="Enter Savings" value="0" id="autoSaving_${getTableData[e].StepID}" type="number" onfocusout="refreshStepSelect(this)" onkeydown="javascript: return event.keyCode === 8 || event.keyCode === 46 ? true : !isNaN(Number(event.key))" />`);
                        getTableData[e].Savings = 0;
                        break;
                    }
                    else if (getTableData[e].StepID == currentRowBtnClicked.StepID && currentTableName == "newStepTable") {
                        $(closestTd).html(0);
                        getTableData[e].Savings = 0;
                        break;
                    }
                }
            }
            else {

                for (let e in getTableData) {
                    if (getTableData[e].StepID == currentRowBtnClicked.StepID) {
                        let finalsaving = (Number)(response.Savings);
                        finalsaving = Math.round(finalsaving * 100) / 100;
                        $(closestTd).html(finalsaving);
                        getTableData[e].Savings = finalsaving;
                    }
                }
                if (currentTableName == 'newStepTable') {
                    currSelectArr = $('.newStepDropdown');

                    for (let i = 0; i < $(currSelectArr).length; i++) {
                        tempManualStepArr = []; addedStepArr = [];
                        let currSelect = $(currSelectArr)[i].options;

                        addedStepArr = getBotTableData[i].addedSteps;
                        tempManualStepArr = addedStepArr.filter(function (el) {
                            if (callingPage === C_BOTSAVING) {
                                if (el.RpaID != botID) { return el }
                            }
                            return el.ExecutionType === "Manual";
                        });



                        let isMatch = $(currSelect).filter(function (el) { return currSelect[el].value.split('_')[0] == currentRowBtnClicked.StepID });
                        if (isMatch.length != 0) {
                            $(currSelectArr[i]).html('');
                            if (currentRowBtnClicked.ExecutionType == "Automatic") {

                                $(currSelectArr[i]).append('<option value="' + currentRowBtnClicked.StepID + '_' + currentRowBtnClicked.ExecutionType + '_' + currentRowBtnClicked.RpaID + '_' + (Number)(currentRowBtnClicked.Savings) + '"> A/' + currentRowBtnClicked.StepName + '</option>');

                                for (let k in tempManualStepArr) {
                                    $(currSelectArr[i]).append('<option value="' + tempManualStepArr[k].StepID + '_' + tempManualStepArr[k].ExecutionType + '_' + tempManualStepArr[k].RpaID + '_' + (Number)(tempManualStepArr[k].Savings) + '"> M/' + tempManualStepArr[k].StepName + '</option>');
                                }
                                break;
                            }
                        }
                    }
                } else {
                    currSelectArr = $('.oldStepDropDown');
                    oldStepsUnsatisfied = [];
                    $(".calculateSavings").data('isPreCondtionSatisfied', true);
                    $('#savingsChekboxDiv').hide();
                    $('#savingsCheckBox').prop('checked', false);
                    $(".calculateSavings").each(function () {
                        $(this).data('selectedOldStep', []);
                    });

                    currSelectArr.each(function () {
                        $(this).data('selectedItems', []);
                        $(this).val('');
                    });
                }
            }
        },

        error: function (xhr, status, statusText) {
            console.log('An error occurred');
        },
        complete: function () {
            pwIsf.removeLayer();
        }
    });
}

function refreshStepSelect(obj) {
    let getBotTableData = $('#botSavingTable').dataTable().fnGetData();
    let currentTableName = $($(obj).closest('table'))[0].id;
    let currentRowBtnClicked = $('#' + currentTableName).dataTable().fnGetData($(obj).closest('tr'));
    let getTableData = $('#' + currentTableName).dataTable().fnGetData();
    let currSelectArr = ''; let addedStepArr = []; let tempManualStepArr = [];
    let oldVersion = '';
    if ($('#wfVersionSelect').val() != null) {
        oldVersion = $('#wfVersionSelection').val().split('_')[0];
    }

    for (let e in getTableData) {
        if (getTableData[e].StepID == currentRowBtnClicked.StepID && parseInt(getTableData[e].defId) === parseInt(currentRowBtnClicked.defId)) {
            getTableData[e].Savings = $('#autoSaving_' + currentRowBtnClicked.StepID).val();
        }
        else {
            (getTableData[e].Savings == "NA" || getTableData[e].Savings == undefined) ? getTableData[e].Savings = 0 : getTableData[e].Savings = getTableData[e].Savings;
        }
    }

    if (currentTableName == 'newStepTable') {
        currSelectArr = $('.newStepDropdown');

        for (let i = 0; i < $(currSelectArr).length; i++) {
            tempManualStepArr = []; addedStepArr = [];
            let currSelect = $(currSelectArr)[i].options;

            addedStepArr = getBotTableData[i].addedSteps;
            tempManualStepArr = addedStepArr.filter(function (el) {
                if (callingPage === C_BOTSAVING) {
                    if (el.RpaID != botID) { return el }
                }
                return el.ExecutionType === "Manual";
            });



            let isMatch = $(currSelect).filter(function (el) { return currSelect[el].value.split('_')[0] == currentRowBtnClicked.StepID });
            if (isMatch.length != 0) {
                $(currSelectArr[i]).html('');
                if (currentRowBtnClicked.ExecutionType == "Automatic") {

                    $(currSelectArr[i]).append('<option value="' + currentRowBtnClicked.StepID + '_' + currentRowBtnClicked.ExecutionType + '_' + currentRowBtnClicked.RpaID + '_' + (Number)(currentRowBtnClicked.Savings) + '"> A/' + currentRowBtnClicked.StepName + '</option>');

                    for (let k in tempManualStepArr) {
                        $(currSelectArr[i]).append('<option value="' + tempManualStepArr[k].StepID + '_' + tempManualStepArr[k].ExecutionType + '_' + tempManualStepArr[k].RpaID + '_' + (Number)(tempManualStepArr[k].Savings) + '"> M/' + tempManualStepArr[k].StepName + '</option>');
                    }
                    break;
                }
            }
        }
    } else {
        currSelectArr = $('.oldStepDropDown');
        oldStepsUnsatisfied = [];
        $(".calculateSavings").data('isPreCondtionSatisfied', true);
        $('#savingsChekboxDiv').hide();
        $('#savingsCheckBox').prop('checked', false);
        $(".calculateSavings").each(function () {
            $(this).data('selectedOldStep', []);
        });

        currSelectArr.each(function () {
            $(this).data('selectedItems', []);
            $(this).val('');
        });
    }
}

// 1
function getBOTSummaryDetails(returnedJSON, isWfAddEditPage = false) {
    if ($.fn.dataTable.isDataTable('#deletedStepTable')) {
        $('#deletedStepTable').DataTable().destroy();
        $("#deletedStepTable").empty();
    }
    if ($.fn.dataTable.isDataTable('#newStepTable')) {
        $('#newStepTable').DataTable().destroy();
        $("#newStepTable").empty();
    }
    if ($.fn.dataTable.isDataTable('#botSavingTable')) {
        $('#botSavingTable').DataTable().destroy();
        $("#botSavingTable").empty();
    }

    $('#savingsChekboxDiv').hide();
    $('#savingsCheckBox').prop('checked', false);

    $('#flowChartOldDefId').val(returnedJSON.OldDefID);

    botDataJson.newDefID = returnedJSON.FlowChartDefID;
    //getLastVersionsOfFlowChart(returnedJSON.WorkFlowName);
    botDataJson.newVersion = returnedJSON.Version;
    botDataJson.oldDefID = returnedJSON.OldDefID;
    botDataJson.oldStepId = returnedJSON.OldStepId;
    botDataJson.bOTID = returnedJSON.bOTID;

    $('#botJsonData').data('botjsondata', JSON.stringify(botDataJson));

    pwIsf.addLayer({ text: 'Please wait ...' });
    $.isf.ajax({

        type: "POST",
        //  async:false,
        contentType: 'application/json',
        data: JSON.stringify(botDataJson),
        url: service_java_URL + "flowchartController/getAndSaveBotSavingSummary",

        xhrFields: {
            withCredentials: false
        },
        success: function (response) {
            storeUniqueDeletedStepsArray = { 'deletedSteps': [] };
            storeRedundantDeletedStepsArray = { 'deletedSteps': [] };
            if (!isWfAddEditPage) {

                getBOTSummaryTables(response, isWfAddEditPage);
            }
            else {
                let isNewAutoStepAdded = response.addedSteps.filter(function (el) { return el.ExecutionType === "Automatic" });
                if (isNewAutoStepAdded.length != 0 && newStepAddArray.length != 0) {
                    getBOTSummaryTables(response, isWfAddEditPage);
                }
                else {
                    //IF reset proficiency chcekbox is checked, reset the proficiency. 
                    //Called here in case bot saving is not calculated and proficiency is adjusted. If bot saving to be calculated
                    //then same function is called at "Save button" of botSaving popup. (saveAllBotDetails method)
                    pwIsf.removeLayer();
                    if (resetProficiencyFlag) {
                        resetProficiency();
                    }
                    else {
                        setTimeout("self.close()", 3000);
                    }
                }
            }
        },

        error: function (xhr, status, statusText) {
            console.log('An error occurred');
        }
    });
}

// 2
function getBOTSummaryTables(data, isWfAddEditPage = false) {
    let newAutoSteps = []; let botTableArray = []; let newManualSteps = [];
    pwIsf.removeLayer();
    let oldDefId = $("#oldDefID").val();

    data.deletedSteps.forEach(item => item.defId = oldDefId);
    storeUniqueDeletedStepsArray.deletedSteps = JSON.parse(JSON.stringify(data.deletedSteps));

    getDeletedStepTable(storeUniqueDeletedStepsArray);
    getNewStepTable(data);    

    newAutoSteps = data.addedSteps.filter(function (el) {

        if (callingPage === C_BOTSAVING) {
            return parseInt(el.RpaID) === parseInt(botID);
        }
        else if (el.ExecutionType === "Automatic" && newStepAddArray.includes(el.StepID)) {
            return el.ExecutionType;
        }

    });
    newManualSteps = data.addedSteps.filter(function (el) {
        if (callingPage === FLOWCHARTBOTEDIT) {
            if (parseInt(el.RpaID) !== parseInt(botID) && el.ExecutionType == "Manual") { return el; }
        }
        else {
            return el.ExecutionType === "Manual";
        }
    });

    for (let i in newAutoSteps) {

        let currentWf = $('#currentWf').val();
        let currentWfId = $('#currentWfId').val();
        let selectedRow = $('input[name=botsaving]:checked').data('details');
        let currentVersion = callingPage === C_BOTSAVING ? selectedRow.versionNumber : urlParams.get("version");

        let tempArr = [];
        tempArr.push(newAutoSteps[i]);

        for (let m in newManualSteps) {
            tempArr.push(newManualSteps[m]);
        }

        botSavingsConstants.deletedOldSteps[newAutoSteps[i].StepID] = JSON.parse(JSON.stringify(data.deletedSteps));

        botTableArray.push({
            addedSteps: tempArr, deletedSteps: data.deletedSteps, BotId: newAutoSteps[i].RpaID,
            autoStepId: newAutoSteps[i].StepID, wfName: currentWf, version: currentVersion, wfId: currentWfId,
            newStepSelectedItems: [], oldStepSelectedItems: '', defId: $('#oldDefID').val(), oldStepSelectedData: [], isStepPreConditionSatisfied: true
        });
    }

    getBOTTable(botTableArray);
    //if (data.deletedSteps.length != 0) {
    //    let is20WOExecutedInArray = data.deletedSteps.filter(function (el) { if (el.ExecutionType === "Manual" && el.flag == true) { return el.flag; } });
    //    let getallmanualandtrue = data.deletedSteps.filter(function (el) { if (el.ExecutionType === "Manual" && el.isAll20WOExecuted == true) { return true; } else { return false; } });
    //    (is20WOExecutedInArray.length != 0) ? is20WOExecuted = true : is20WOExecuted = false;

    //    (getallmanualandtrue.length != 0) ? isAll20WOExecuted = true : isAll20WOExecuted = false;
    //}
}

function isSaveValidated() {
    let btnclass = $(".calculateSavings"); let flag = false;
    let OK = true;
    let indices = getBotRowIndices();

    for (const a of indices) {
        if (!$($(btnclass)[a]).prop('disabled') && $($(btnclass)[a]).data('isSavingsCalculated') === true) { flag = true; }
        else { flag = false; break; }
    }
    if ($('#savingsChekboxDiv').is(':visible') && !$('#savingsCheckBox').is(':checked')) {
        pwIsf.alert({ msg: "Please check the manual request checkbox", type: "warning" });
        OK = false;
    }
    else if (!flag) {
        pwIsf.alert({ msg: "Please calculate Savings for all BOTS", type: "error" });
        OK = false;
    }
    //else if ($('#savingsChekboxDiv').is(':visible') && !$('#savingsCheckBox').is(':checked')) {
    //    pwIsf.alert({ msg: "Please check the manual request checkbox", type: "warning" });
    //    OK = false;
    //}

    return OK;
}

function saveAllBotDetails() {

    if (isSaveValidated()) {
        botForStepID = '';
        let tableLength = $('#botSavingTable').dataTable().fnGetData().length;
        let allBotDetailsObj = new Object(); let botJsonArray = [];
        let botSavingRows = $('#botSavingTable').dataTable().fnGetNodes();
        let selectedRow = $('input[name=botsaving]:checked').data('details');
        JsonStringArr = [];
        let indices = getBotRowIndices();

        indices.forEach((i) => {
            allBotDetailsObj = {};
            allBotDetailsObj.projectID = callingPage === C_BOTSAVING ? selectedRow.ProjectID : urlParams.get("projID");
            allBotDetailsObj.subActivity = callingPage === C_BOTSAVING ? selectedRow.SubActivityID : urlParams.get("subId");
            allBotDetailsObj.bOTID = $($(botSavingRows)[i]).find('td:eq(0)')[0].innerText;
            if (callingPage === C_BOTSAVING) {
                allBotDetailsObj.bOTStepID = selectedRow.BotStepID;
            }
            else if (callingPage === FLOWCHARTBOTEDIT) {
                allBotDetailsObj.bOTStepID = urlParams.get('stepId');
            }
            else {
                allBotDetailsObj.bOTStepID = botForStepID;
            }

            allBotDetailsObj.oldDefID = JSON.parse($('#botJsonData').data('botjsondata')).oldDefID;
            allBotDetailsObj.newDefID = JSON.parse($('#botJsonData').data('botjsondata')).newDefID;
            allBotDetailsObj.createdBy = callingPage === FLOWCHARTADD ? signumGlobal : localStorage.getItem('BotCreatedBy');
            allBotDetailsObj.createdOn = callingPage === FLOWCHARTADD ? new Date().toJSON().slice(0, 10) : localStorage.getItem('BotCreatedOn');
            let currRowButtonData = $($(botSavingRows)[i]).find(`td:eq(${callingPage === C_BOTSAVING ? '8' : '7'})`).find('#calculateSavings');
            allBotDetailsObj.totalSavings = $(currRowButtonData).data('saving') === undefined ? 0 : $(currRowButtonData).data('saving');
            allBotDetailsObj.expectedSavings = $(currRowButtonData).data('expectedSavings') === undefined ? 0 : $(currRowButtonData).data('expectedSavings');
            allBotDetailsObj.lastModifiedBy = signumGlobal;
            allBotDetailsObj.lastModifiedOn = new Date().toJSON().slice(0, 10);
            //allBotDetailsObj.oldSteps = $(currRowButtonData).data('selectedOldStep') === undefined ? [] : $(currRowButtonData).data('selectedOldStep');
            allBotDetailsObj.oldSteps = $(currRowButtonData).data('selectedOldStep') === undefined ? [] : $(currRowButtonData).data('selectedOldStep');
            allBotDetailsObj.newSteps = $(currRowButtonData).data('selectedNewStep') === undefined ? [] : $(currRowButtonData).data('selectedNewStep');
            allBotDetailsObj.remark = $(currRowButtonData).data('Remark') === undefined ? '' : $(currRowButtonData).data('Remark');
            allBotDetailsObj.isSavingFlag = $(currRowButtonData).data('isSavingFlag') === undefined ? false : $(currRowButtonData).data('isSavingFlag');
            allBotDetailsObj.currentYearSavings = callingPage === C_BOTSAVING ? ($(currRowButtonData).data('currentSavingYr') === undefined ? 'Yes' : $(currRowButtonData).data('currentSavingYr')) : "Yes";
            JsonStringArr.push(allBotDetailsObj);
        })

   
        if (callingPage !== C_BOTSAVING) {
            pwIsf.addLayer({ text: 'Please wait ...' });
            $.isf.ajax({
                type: "POST",
                contentType: 'application/json',
                data: JSON.stringify(JsonStringArr),
                url: `${service_java_URL}flowchartController/saveBotSavingDetailsAndHistory`,
                success: function (data) {
                    if (callingPage === FLOWCHARTADD) {
                        pwIsf.alert({ msg: 'BOT Saving for WorkFlow Saved', type: 'info' });
                        $('#botDetailModal').modal('hide');
                        if (resetProficiencyFlag) {
                            resetProficiency();
                        }
                        else {
                            setTimeout("self.close()", 3000);
                        }
                    } else {
                        let botID = urlParams.get("newBotID");
                        $.isf.ajax({
                            url: `${service_java_URL}botStore/deployBot/${botID}/${signumGlobal}`,
                            context: this,
                            crossdomain: true,
                            processData: true,
                            contentType: 'application/json',
                            type: 'POST',
                            xhrFields: {
                                withCredentials: false
                            },
                            success: function (data) {

                                if (data.apiSuccess) {
                                    pwIsf.alert({ msg: `Request id ${botID} successfully deployed`, type: 'success' });
                                    $('#botDetailModal').modal('hide');
                                }
                                else
                                    pwIsf.alert({ msg: data.responseMsg, type: 'warning' });


                            },
                            error: function (xhr, status, statusText) {
                                pwIsf.alert({ msg: "Error in Deploying", type: 'warning' });
                            },
                            complete: function (xhr, statusText) {
                                pwIsf.removeLayer();
                                window.opener.getDeployedBotDetails();
                                setTimeout(function () { window.close() }, 3000);
                            }
                        });
                    }


                },
                complete: function () {
                    pwIsf.removeLayer();
                },
                error: function (xhr, status, statusText) {
                    pwIsf.alert({ msd: "Failed to Save Bot Details ", type: "error" });
                }
            });
        }
        else {
            $('#botDetailModal').modal('hide');
        }
    }
}

// On change of wf version dropdown. Logic in getBOTTable()
function getOldStepList(obj, preCondition) {
    
    let currRowButton = getCalSavingsBtn(obj);
    let responseData = '';
    let selectedRow = $('input[name=botsaving]:checked').data('details');
    let version = callingPage === C_BOTSAVING ? selectedRow.versionNumber : urlParams.get("version");
    let paramWFId = callingPage === C_BOTSAVING ? selectedRow.wfid : urlParams.get("wfid");
    let rowBotId = $(obj).closest('tr').children('td:first').text();
   
    let oldStepDeleted = $(currRowButton).data('selectedOldStep') === undefined ? [] : $(currRowButton).data('selectedOldStep');

    if (oldStepDeleted.length === 0) {
        $(currRowButton).data('isPreCondtionSatisfied', true);
    }

    let currentCollapseBtn = $(obj).closest('tr').find('.collapseButton');
    let collapseBtnOld = $(obj).closest('tr').find('#oldStepDropDown'); //obj can be used instead of this. Need to check
    let currentAutoStepId = $(currentCollapseBtn).data('autoid');
    let defId = $(currentCollapseBtn).data('defid');
    let rowVersion = $(currentCollapseBtn).data('version');
    let rowWfId = $(currentCollapseBtn).data('wfid');
    let deletedOldSteps = botSavingsConstants.deletedOldSteps[currentAutoStepId];

    let step = $(obj).data('selectedItems').at(-1);
    let temp = step.stepData.split('_');
    selectedOldStep = []; selectedNewStep = [];

    if (!(parseInt(version) === parseInt(rowVersion) && parseInt(paramWFId) === parseInt(rowWfId))) {
        responseData = checkSavingsForOldVersionWFStep(temp[1], temp[0], defId, temp[4], temp[2], step.stepName.split('/')[1], temp[3], defId, currentAutoStepId);

        checkIsPreConditionSatisfied(responseData.responseJSON.isPreCondtionSatisfied);

        if (temp[1] == "Manual") {
            step.stepData = `${temp[0]}_${temp[1]}_${temp[3]}_${(Number)(Math.round(responseData.responseJSON.Savings * 100) / 100)}_${temp[4]}`;
        }

    }
    else {
        selectedOldStep.push({ 'StepID': temp[0], 'StepName': step.stepName.split('/')[1], 'ExecType': temp[1], 'botID': temp[2], 'Saving': temp[3], 'emeCalculationDefID': $("#oldDefID").val(), 'defId': $("#oldDefID").val() });

        let deletedItem = deletedStepData.deletedSteps.filter(item => parseInt(item.defId) === parseInt($("#oldDefID").val()) && item.StepID === temp[0])[0];
        if (deletedItem != undefined && deletedItem != null) {
            addUniqueStepAndDefIdInArray(deletedOldSteps, deletedItem);
        }

        if (!storeUniqueDeletedStepsArray.deletedSteps.filter(function (e) { return e.StepID === temp[0] && parseInt(e.defId) === parseInt(defId); }).length > 0 && deletedItem) {
            storeUniqueDeletedStepsArray.deletedSteps.push(deletedItem);
        }

        checkIsPreConditionSatisfied(deletedItem.isPreCondtionSatisfied, deletedItem);
        
    }

    function checkIsPreConditionSatisfied(isPreCondtionSatisfied, deletedStepItem = true) {
        if (deletedStepItem && isPreCondtionSatisfied === false) {
            $(currRowButton).data('isPreCondtionSatisfied', false);
            $('#savingsChekboxDiv').show();
            $(collapseBtnOld).css("border", "1px solid #FF0000");
            pwIsf.alert({ msg: 'No data in the pre-period. Please select a different WF and Version from the drop-down.', type: 'warning' });
            addUniqueObjInArray(oldStepsUnsatisfied, { autoId: currentAutoStepId, stepId: temp[0], defId });
        }
    }

    oDelTable.clear(); oDelTable.draw();

    getDeletedStepTable(storeUniqueDeletedStepsArray, true);

    if (deletedOldSteps.length !== 0) {
        oldStep20WO(deletedOldSteps, currRowButton);
        
    }

    if (oldStepsUnsatisfied.length === 0) {
        $('#savingsChekboxDiv').hide();
        $('#savingsCheckBox').prop('checked', false);
    }
    $.each($(`#newStepDropdown_${rowBotId}`), function (newStepIndex, d) {
        if ($(`#newStepDropdown_${rowBotId}`)[newStepIndex].selectedOptions.length > 0) {

            $.each($(`#newStepDropdown_${rowBotId}`)[newStepIndex].selectedOptions, function () {
                var temp = $(this).val().split('_');
                selectedNewStep.push({ 'StepID': temp[0], 'StepName': $(this).text().split('/')[1], 'ExecType': temp[1], 'botID': temp[2], 'Saving': temp[3] });
            });
        };
    });

    setStepDataAttr(currRowButton, 'selectedNewStep', selectedNewStep);
    setStepDataAttr(currRowButton, 'selectedOldStep', selectedOldStep);

    validateData(obj);
    updateIsSavingsCalculated(obj);
}

function setStepDataAttr(element, attrName, data) {
    if (attrName === 'selectedOldStep') {
        let currentData = $(element).data(attrName);

        if (currentData === undefined || currentData === null) {
            $(element).data(attrName, data);
        }
        else {
            $.each(data, function (i, d) {
                if (!currentData.some(item => parseInt(item.defId) === parseInt(d.defId) && item.StepID === d.StepID)) {
                    currentData.push(d);
                }
            });
            $(element).data(attrName, currentData);
        }
    } else {
        $(element).data(attrName, data);
    }    
}

function hideErrorMsg(inpEle) {
    $('#' + inpEle).text('');
    $('#' + inpEle).css('display', 'none');
}

function getLastVersionsOfFlowChart(workFlowName) {
    let selectedRow = $('input[name=botsaving]:checked').data('details');
    let WFObject = new Object();
    WFObject.projectID = callingPage === C_BOTSAVING ? selectedRow.ProjectID : urlParams.get("projID");
    WFObject.subActivity = callingPage === C_BOTSAVING ? selectedRow.SubActivityID : urlParams.get("subId");
    WFObject.versionCount = "5";
    WFObject.newVersion = callingPage === C_BOTSAVING ? selectedRow.versionNumber : urlParams.get("version");
    WFObject.wfName = workFlowName;
    WFObject.wfid = callingPage === C_BOTSAVING ? selectedRow.wfid : urlParams.get("wfid");
    let subActivityDefID = callingPage === C_BOTSAVING ? selectedRow.subDefID : parseInt($('#oldDefID').val());
    versionArray = [];
    if (WFObject.newVersion != 0) {
        $("#wfVersionSelect").val('WF:' + WFObject.wfName + '_' + WFObject.wfid + ' V:' + WFObject.newVersion);
        $('#wfVersionSelection').val(WFObject.newVersion + '_' + subActivityDefID + '_' + WFObject.wfid);
    }
    
    $("#wfVersionSelect").autocomplete({

        source: function (request, response) {
            searchTerm = request.term;
           if (searchTerm.includes(":")) {
                sTerm = searchTerm.split(":");
                let termWFArr = sTerm[1];
                let termWF = termWFArr.slice(0, -1);
                searchTerm = termWF;
                
            }
            
            WFObject.term = searchTerm;
            $.isf.ajax({
                url: `${service_java_URL}flowchartController/getWFVersionsFromSubactivityID`,
                contentType: "application/json",
                type: "POST",
                data: JSON.stringify(WFObject),
                success: function (resp) {
                    
                    var result = [];
                        versionArray = resp;
                       let subActivityDefID = callingPage === C_BOTSAVING ? selectedRow.subDefID : parseInt($('#oldDefID').val());

                    
                    if (resp.responseData.length == 0) {

                        $('#lblWarning').fadeIn().delay(2000).fadeOut();
                        $('#lblWarning').text('Please enter a valid WorkFlow name');
                        $('#wfVersionSelect').val("");
                        $('#wfVersionSelect').focus();
                        response(result);

                    }
                    else {
                        hideErrorMsg('WorkFlow-Required');

                        $("#wfVersionSelect").autocomplete().addClass("ui-autocomplete-loading");

                        $.each(resp.responseData, function (i, d) {
                            
                            result.push({
                                "label": d.WorkFlowVersion,
                                "value": d.WorkFlowVersion,
                                "wfVersionInfo": d.VersionNumber + '_' + d.SubActivityFlowChartDefID + '_' + d.wfid
                            });
                        });
                        $('#wfVersionSelection').val(WFObject.newVersion + '_' + subActivityDefID + '_' + WFObject.wfid );
                        response(result);
                        
                    }
                    

                     

                }
            });
        },
        appendTo: $('#botDetailModal'),
        minLength: 3,
        select: function (event, ui) {
            let wfVersionInformation = ui.item.wfVersionInfo.split("_");

            let labelSelected = ui.item.label;
            let workFlowNameArr = labelSelected.split(":");
            let workFlowNameArrV = workFlowNameArr[1];
            let workFlowNameWFID = workFlowNameArrV.slice(0, -2);
            let workFlowNamewithoutWFID = workFlowNameWFID.split("_");
            let workFlowName = workFlowNamewithoutWFID[0];
            let versionSelected = workFlowNameArr[2];
            let wfId = wfVersionInformation[2];
            let defId = wfVersionInformation[1];
            $('#wfVersionSelection').val(wfVersionInformation[0] + '_' + wfVersionInformation[1] + '_' + wfVersionInformation[2]);

            appendWfVersion(workFlowName, versionSelected, wfId, defId);
        }

    });
    $("#wfVersionSelect").autocomplete("widget").addClass("fixedHeight");


}

function viewVersionFlowChart() {
    let selectedRow = $('input[name=botsaving]:checked').data('details');
    let subID = callingPage === C_BOTSAVING ? selectedRow.SubActivityID : urlParams.get("subId");
    let projID = callingPage === C_BOTSAVING ? selectedRow.ProjectID : urlParams.get("projID");
    let wfid = callingPage === C_BOTSAVING ? selectedRow.wfid : urlParams.get("wfid");
    flowChartOpenInNewWindow(`${UiRootDir}/Project/FlowChartWorkOrderView\?subId=${subID}&projID=${projID}&wfid=${$('#wfVersionSelection').val().split('_')[2]}&version=${$('#wfVersionSelection').val().split('_')[0]}`, 'versionWF');
}

function getNewStepList(obj) {

    let newdropdownselect = $(obj)[0];
    let olddropdownselect = $(obj).closest('tr').find('#oldStepDropDown')[0];
    //let currRowButton = $(obj).closest('tr').find('#calculateSavings')[0];
    let currRowButton = getCalSavingsBtn(obj);

    let isPreCondtionSatisfied = $(currRowButton).data('isPreCondtionSatisfied');
    if (isPreCondtionSatisfied === undefined || isPreCondtionSatisfied === null) {
        $(currRowButton).data('isPreCondtionSatisfied', true);
    }
    let temp = [];
    selectedOldStep = []; selectedNewStep = [];

    let currentCollapseBtn = $(obj).closest('tr').find('.collapseButton');
    let currentAutoStepId = $(currentCollapseBtn).data('autoid');

    $.each($(newdropdownselect)[0].selectedOptions, function () {
        temp = $(this).val().split('_');
        selectedNewStep.push({ 'StepID': temp[0], 'StepName': $(this).text().split('/')[1], 'ExecType': temp[1], 'botID': temp[2], 'Saving': temp[3] });

    });
    arrexectype = [];

    $('.oldStepDropDown').each(function () {
        let collapseBtn = $(this).closest('tr').find('.collapseButton');
        let autoStepId = $(collapseBtn).data('autoid');
        let rowDefId = $(collapseBtn).data('defid');

        if (currentAutoStepId === autoStepId) {
            let stepData = $(this).data('selectedItems');
            $.each(stepData, function (i, d) {
                temp = d.stepData.split('_');
                selectedOldStep.push({ 'StepID': temp[0], 'StepName': d.stepName.split('/')[1], 'ExecType': temp[1], 'botID': temp[2], 'Saving': temp[3], 'emeCalculationDefID': 0, defId: rowDefId });
            });
        }
    });
        
    //$(currRowButton).data('selectedNewStep', selectedNewStep)
    //$(currRowButton).data('selectedOldStep', selectedOldStep

    setStepDataAttr(currRowButton, 'selectedNewStep', selectedNewStep);
    setStepDataAttr(currRowButton, 'selectedOldStep', selectedOldStep);

    let btnClass = ''; let isSavingButtonExist = false;
    btnClass = $('.calSavingFromExisting');
    isSavingButtonExist = $(btnClass).filter(function (el) { return $(btnClass[el]).css('display') == 'inline-block' });

    if (oldStepsUnsatisfied.length === 0) {
        $('#savingsChekboxDiv').hide();
        $('#savingsCheckBox').prop('checked', false);

    }


    checkNoOldStepButNewStepCondition(obj);

    let isPreConditionCheck = $(currRowButton).data('isPreCondtionSatisfied') === true ||
        ($(currRowButton).data('isPreCondtionSatisfied') === false && $('#savingsCheckBox').is(':checked'));

    if ($(currRowButton).data('selectedNewStep').length != 0 && isSavingButtonExist.length == 0 && isPreConditionCheck) {
        $(currRowButton).prop('disabled', false)
    }
    else {
        $(currRowButton).prop('disabled', true)
        $(obj).closest('tr').find('#expectedSavingsText').val('0')//$('#expectedSavingsText').val("");
        $(obj).closest('tr').find('#savingsText').val('')//$('#savingsText').val("");
        $('#saveBotDetailsBtn').prop('disabled', true);
    }

    updateIsSavingsCalculated(obj);

}

function isOldStepDropDownEmpty(thisObj) {
    let isEmpty = false;

    let currentCollapseBtn = $(thisObj).closest('tr').find('.collapseButton');
    let currentAutoStepId = $(currentCollapseBtn).data('autoid');

    $('.oldStepDropDown').each(function (i) {
        let collapseBtn = $(this).closest('tr').find('.collapseButton');
        let autoStepId = $(collapseBtn).data('autoid');

        if (autoStepId === currentAutoStepId) {
            if ($(this).val() === "") {
                isEmpty = true
                return false; //breaking the loop
            }
        }
    });

    return isEmpty;
}

function checkNoOldStepButNewStepCondition(obj) {

    let currRowButton = getCalSavingsBtn(obj);

    if (isOldStepDropDownEmpty(obj)) {
        if ($(currRowButton).data('selectedNewStep').length != 0) {
            $(currRowButton).data('isPreCondtionSatisfied', false);
            $('#savingsChekboxDiv').show();
        }
        //else {
        //    if ($('.newStepDropdown option:selected').length > 0) {
        //        $(currRowButton).data('isPreCondtionSatisfied', false);
        //        $('#savingsChekboxDiv').show();
        //    }
        //    else {
        //        $(currRowButton).data('isPreCondtionSatisfied', true);
        //        $('#savingsChekboxDiv').hide();
        //    }
        //    $('#savingsCheckBox').prop('checked', false);
        //}
    }
}

function validateData(obj) {

    let btnClass = '';
    let isSavingButtonExist = false;
    //let currRowButton = $(obj).closest('tr').find('#calculateSavings')[0];
    let currRowButton = getCalSavingsBtn(obj);
    btnClass = $('.calSavingFromExisting');
    isSavingButtonExist = $(btnClass).filter(function (el) { return $(btnClass[el]).css('display') == 'inline-block' });

    let isPreConditionCheck = $(currRowButton).data('isPreCondtionSatisfied') === true ||
        ($(currRowButton).data('isPreCondtionSatisfied') === false && $('#savingsCheckBox').is(':checked'));

    if ($(currRowButton).data('selectedNewStep').length != 0 && isSavingButtonExist.length == 0 && isPreConditionCheck) {
        $(currRowButton).prop('disabled', false);
    }
    else {
            $(currRowButton).prop('disabled', true)
            $(obj).closest('tr').find('#expectedSavingsText').val('0');
            $(obj).closest('tr').find('#savingsText').val('');
            $('#saveBotDetailsBtn').prop('disabled', true);
        }
}

function checkSavingsForOldVersionWFStep(ExecutionType, oldStepID, oldDefID, task, RpaID, StepName, savings, emeCalculationDefID, currentAutoStepId) {
    let stepObject = new Object()
    stepObject.oldDefID = oldDefID;
    stepObject.oldStepId = oldStepID;
    stepObject.executionType = ExecutionType;

    pwIsf.addLayer({ text: 'Please wait ...' });
    return $.isf.ajax({
        returnAjaxObj: true,
        type: "POST",
        async: false,
        contentType: 'application/json',
        data: JSON.stringify(stepObject),
        url: service_java_URL + "flowchartController/getAndSaveBotSaving",

        xhrFields: {
            withCredentials: false
        },
        success: function (response) {

            if ((savings != null) && (savings != "NA") && (savings != "0")) {
                savings = savings;
            } else { savings = response.Savings; }
            let isAll20WOExecuted = response.isAll20WOExecuted;
            selectedOldStep.push({ 'ExecType': ExecutionType, 'RpaID': RpaID, 'Saving': savings, 'StepName': StepName, 'task': task, 'flag': response.flag, 'msg': response.msg, 'StepID': oldStepID, 'emeCalculationDefID': emeCalculationDefID, 'defId': oldDefID,isAll20WOExecuted});
            //deletedOldStepsData.deletedSteps.push({ 'ExecutionType': ExecutionType, 'RpaID': RpaID, 'Savings': savings, 'StepName': StepName, 'task': task, 'flag': response.flag, 'msg': response.msg, 'StepID': oldStepID, 'emeCalculationDefID': emeCalculationDefID, 'defId': oldDefID });
            let stepObj = { 'ExecutionType': ExecutionType, 'RpaID': RpaID, 'Savings': savings, 'StepName': StepName, 'task': task, 'flag': response.flag, 'msg': response.msg, 'StepID': oldStepID, 'emeCalculationDefID': emeCalculationDefID, 'defId': oldDefID, isAll20WOExecuted};
            addUniqueStepAndDefIdInArray(botSavingsConstants.deletedOldSteps[currentAutoStepId], stepObj);

            if (!storeUniqueDeletedStepsArray.deletedSteps.filter(function (e) { return e.StepID === oldStepID && parseInt(e.defId) === parseInt(oldDefID); }).length > 0) {
                storeUniqueDeletedStepsArray.deletedSteps.push({ 'ExecutionType': ExecutionType, 'RpaID': RpaID, 'Savings': savings, 'StepName': StepName, 'task': task, 'flag': response.flag, 'msg': response.msg, 'StepID': oldStepID, 'emeCalculationDefID': emeCalculationDefID, 'defId': oldDefID, isAll20WOExecuted });
            }

            if (!response.flag) {

            }
        },

        error: function (xhr, status, statusText) {
            console.log('An error occurred');
        },
        complete: function () {
            //return deletedStepsData;
            pwIsf.removeLayer();
        }
    });


}

function deleteAllBotDetails(callingPage) {
    if (callingPage !== "botSaving") {
        //$('#wfVersionSelect').val($('#wfVersionSelect option:first').val());
        $('#wfVersionSelect').val("");
        let botJsonDataForDelDef = new Object();
        botJsonDataForDelDef.newDefID = botDataJson.newDefID
        botJsonDataForDelDef.newVersion = botDataJson.newVersion
        botJsonDataForDelDef.oldDefID = botDataJson.oldDefID
        botJsonDataForDelDef.oldStepId = botDataJson.oldStepId
        botJsonDataForDelDef.bOTID = botDataJson.bOTID

        $.isf.ajax({
            type: "POST",
            contentType: 'application/json',
            data: JSON.stringify(botJsonDataForDelDef),
            url: service_java_URL + "flowchartController/deleteDefID",
            success: function (data) {
                pwIsf.alert({ msg: "BOT not Deployed and new changes of workFlow is not saved", type: "info" })
                if (callingPage === "flowChartAdd") {
                    location.reload();
                }
            },
            complete: function () {

            },
            error: function (xhr, status, statusText) {
                pwIsf.alert({ msd: "Failed to delete workflow ", type: "error" });
            }
        });
    }
}

function onSavingCheckBoxClick(thisObj) {
    let btnClass = $('.calSavingFromExisting');
    let isSavingButtonExist = $(btnClass).filter(function (el) { return $(btnClass[el]).css('display') == 'inline-block' });
    let botSavingRows = $('#botSavingTable').dataTable().fnGetNodes();

    let autoIds = new Set(botSavingRows.map(item => $(item).find('.collapseButton').data('autoid')));
    autoIds.forEach(id => {
        let row = botSavingRows.find(item => $(item).find('.collapseButton').data('autoid') === id);
        let currRowButton = $(row).find('#calculateSavings')[0];

        if ($(currRowButton).data('isPreCondtionSatisfied') === false) {
            if ($(currRowButton).data('selectedNewStep').length !== 0 && isSavingButtonExist.length === 0 && $(thisObj).is(':checked')) {
                $(currRowButton).prop('disabled', false);
            }
            else {
                $(currRowButton).prop('disabled', true);
            }
        }
    })


}

function descButtonValue(obj) {
    let currValue = $(obj).closest('tr').find('#expectedSavingsText');
    let btnValue = $(currValue).val();
    btnValue = Number(btnValue);
    if (btnValue != 0) {
        btnValue = btnValue - 1;
    }
    $(currValue).val(btnValue);
    updateIsSavingsCalculated(obj);
}

function incButtonValue(obj) {
    let currValue = $(obj).closest('tr').find('#expectedSavingsText');
    let btnValue = $(currValue).val();
    btnValue = Number(btnValue);
    btnValue = btnValue + 1;
    $(currValue).val(btnValue);
    updateIsSavingsCalculated(obj);
}

function updateIsSavingsCalculated(obj) {
    let currRowButton = $(obj).closest('tr').find('#calculateSavings')[0];
    $(currRowButton).data('isSavingsCalculated', false);
    $('#saveBotDetailsBtn').prop('disabled', true);
}

/**
 * Validations for appending WF and version combination for BOTs
 * 1) Duplicate combination is not allowed if the combination is already present in all BOTs.
 * 2) If the duplicate combination is present in only one of the BOTs, then append it in the missing BOT
 * 3) Maximum 5 combinations are allowed per BOT
 */
function isWfVersionCombinationValidated(wf, version, currentTableData, autoIds) {
    let isValidated = true;
    let maxLimit = autoIds.size * 5; // Need to configure this figure
    let validBotsForAppending = [];

    //Check if 5 combinations are already present per BOT
    if (currentTableData.length === maxLimit) {
        pwIsf.alert({ msg: 'Maximum 5 unique combinations of WF and Version is allowed.', type: 'warning' });
        isValidated = false;
    }
    else {

        let isPresentInAllBots = true;
        autoIds.forEach((id) => {
            let comboCount = currentTableData.filter(item => item.autoStepId === id).length;
            let isPresent = currentTableData.some(item => item.autoStepId === id && item.wfName === wf && item.version === version);
            if (comboCount < 5 && !isPresent) {
                isPresentInAllBots = false;
                validBotsForAppending.push(id);
            }
        });

        if (isPresentInAllBots) {
            pwIsf.alert({ msg: 'This combination is already present in all the BOTs', type: 'warning' });
            isValidated = false;
        }
    }
    
    

    return { isValidated, validBotsForAppending };
}

function getStepsData(add, deletedIndex) {
    let allStepsData = {};
    let indices = getBotRowIndices();   

    indices.forEach((indexVal, i) => {
        let savingsBtn = $('.calculateSavings')[indexVal];
        let selectedNewStep = $(savingsBtn).data('selectedNewStep');
        let selectedOldStep = $(savingsBtn).data('selectedOldStep');

        selectedNewStep = selectedNewStep === undefined ? [] : selectedNewStep;
        selectedOldStep = selectedOldStep === undefined ? [] : selectedOldStep;
        let data = {
            selectedNewStep,
            selectedOldStep
        }

        let finalIndex = add ? indexVal + i : (indexVal > deletedIndex ? indexVal - 1 : indexVal);
        allStepsData[finalIndex] = data;
    });

    return allStepsData;
}

function getBotRowIndices() {
    let botSavingTable = botSavingsConstants.botSavingTable;
    let botSavingRows = $(botSavingTable).dataTable().fnGetNodes();
    let autoIds = new Set(botSavingRows.map(item => $(item).find('.collapseButton').data('autoid')));
    let indices = [];
    autoIds.forEach(id => {
        let rowIndex = botSavingRows.findIndex(item => $(item).find('.collapseButton').data('autoid') === id);
        indices.push(rowIndex);
    });

    return indices;
}

function restoreStepsData(allStepsData) {

    let indices = getBotRowIndices();

    indices.forEach(indexVal => {
        let savingsBtn = $('.calculateSavings')[indexVal];
        let selectedNewStep = allStepsData[indexVal].selectedNewStep;
        let selectedOldStep = allStepsData[indexVal].selectedOldStep;
        $(savingsBtn).data('selectedNewStep', selectedNewStep);
        $(savingsBtn).data('selectedOldStep', selectedOldStep);
    });
}

// Append WF and version combination for BOTs
function appendWfVersion(wf, version, wfId, defId) {
    let allStepsData = getStepsData(true);
    let botSavingTable = botSavingsConstants.botSavingTable;
    let currentTableData = getBotTableDataWithState();
    let autoIds = new Set(currentTableData.map(item => item.autoStepId));
    let wfVersionValidationObj = isWfVersionCombinationValidated(wf, version, currentTableData, autoIds);
    let selectedRow = $('input[name=botsaving]:checked').data('details');
    let currentVersion = callingPage === C_BOTSAVING ? selectedRow.versionNumber : urlParams.get("version");
    let currentWfId = callingPage === C_BOTSAVING ? selectedRow.wfid : urlParams.get("wfid");
    let deletedStepTable = botSavingsConstants.deletedStepTable;
    let deletedStepsTableData = Array.from($(deletedStepTable).DataTable().rows().data());
    let isCurrentWf = parseInt(version) === parseInt(currentVersion) && parseInt(wfId) === parseInt(currentWfId);

    if (wfVersionValidationObj.isValidated) {
        // Add the combination for all the Bots
        autoIds.forEach((id) => {
            if (wfVersionValidationObj.validBotsForAppending.includes(id)) {
                let newObj = JSON.parse(JSON.stringify(currentTableData.find(item => item.autoStepId === id))); // New row to be appended
                newObj.wfName = wf;
                newObj.version = version;
                newObj.wfId = wfId;
                newObj.isStepPreConditionSatisfied = true;
                newObj.defId = defId;
                newObj.newStepSelectedItems = [];
                newObj.oldStepSelectedItems = '';
                newObj.oldStepSelectedData = [];

                let index = currentTableData.slice().reverse().findIndex(item => item.autoStepId === id); // Reverse the array and find the index of first occurrence
                let lastIndexOfBot = currentTableData.length - 1 - index; // Find the last index
                currentTableData.splice(lastIndexOfBot + 1, 0, newObj); // Append the new row after the last index

                if (isCurrentWf) {
                    deletedStepData.deletedSteps.forEach(item => {
                        addUniqueStepAndDefIdInArray(botSavingsConstants.deletedOldSteps[id], item);
                        if (!storeUniqueDeletedStepsArray.deletedSteps.some(e => e.StepID === item.StepID && parseInt(e.defId) === parseInt(item.defId))) {
                            storeUniqueDeletedStepsArray.deletedSteps.push(item);
                        }

                        if (!deletedStepsTableData.some(e => e.StepID === item.StepID && parseInt(e.defId) === parseInt(item.defId))) {
                            deletedStepsTableData.push(item);
                        }
                    });
                }
            }
        });


        if ($.fn.dataTable.isDataTable(botSavingTable)) {
            $(botSavingTable).DataTable().destroy();
            $(botSavingTable).remove();
            $('#botSavingTableDiv').html('<table id="botSavingTable" class="table table-striped table-bordered table-hover" style="width:100%;"></table>');
        }

        getBOTTable(currentTableData);
        restoreStepsData(allStepsData);

        if (isCurrentWf) {
            if ($.fn.dataTable.isDataTable(deletedStepTable)) {
                $(deletedStepTable).DataTable().destroy();
                $(deletedStepTable).empty();
            }

            getDeletedStepTable({ deletedSteps: deletedStepsTableData }, true);
        }
    }
}

function getBotTableDataWithState(remove = false, deletedIndex = null) {
    let botSavingTable = botSavingsConstants.botSavingTable;
    let currentTableData = Array.from($(botSavingTable).DataTable().rows().data());
    let rowIndices = getBotRowIndices();

    $('.newStepDropdown').each(function (i) {
        if ($(this).val() !== null) {
            $.each(Array.from(this.selectedOptions), function (j, item) {
                let stepId = item.value.split('_')[0];
                //let indexVal = remove && rowIndices.includes(i) ? i + 1 : i;
                let indexVal = remove && i === deletedIndex ? i + 1 : i;
                currentTableData[indexVal].newStepSelectedItems.push(stepId);
            })
        }
    });

    $('.oldStepDropDown').each(function (i) {
        if ($(this).val() !== null && $(this).val() !== '') {
            let color = $(this).css('border-color');
            let boolVar = color === 'rgb(255, 0, 0)' ? false : true;
            currentTableData[i].isStepPreConditionSatisfied = boolVar;
            currentTableData[i].oldStepSelectedItems = $(this).val();
            currentTableData[i].oldStepSelectedData = $(this).data('selectedItems');
        }
    });

    return currentTableData;
}

// Remove WF and version row from table
function removeWfVersion(index, autoStepId, version, thisObj) {
    let botSavingTable = botSavingsConstants.botSavingTable;
    let deletedIndex = $(thisObj).closest('tr').index();
    let currentTableData = getBotTableDataWithState(true, deletedIndex);
    let allStepsData = getStepsData(false, deletedIndex);
    let autoIds = Array.from(new Set(currentTableData.map(item => item.autoStepId)));

    currentTableData.splice(index, 1);

    if (!currentTableData.some(item => item.autoStepId === autoStepId)) {
        pwIsf.alert({ msg: 'Cannot delete! Atlease one combination of WF and Version is mandatory for a Bot', type: 'warning' });
        return;
    }

    let stepDataArr = $(thisObj).closest('tr').find('#oldStepDropDown').data('selectedItems');
    if (stepDataArr !== undefined && stepDataArr !== null) {
        stepDataArr.forEach(item => delete botSavingsConstants['allOldStepsSelected'][autoStepId][item.stepData.split('_')[0]]);
    }

    if ($.fn.dataTable.isDataTable(botSavingTable)) {
        $(botSavingTable).DataTable().destroy();
        $(botSavingTable).remove();
        $('#botSavingTableDiv').html('<table id="botSavingTable" class="table table-striped table-bordered table-hover" style="width:100%;"></table>');
    }    

    getBOTTable(currentTableData);

    let selectedRow = $('input[name=botsaving]:checked').data('details');
    let currentVersion = callingPage === C_BOTSAVING ? selectedRow.versionNumber : urlParams.get("version");
    let currentWfId = callingPage === C_BOTSAVING ? selectedRow.wfid : urlParams.get("wfid");
    let selectedWfId = $(thisObj).data('wfid');
    let versionDefIdWfId = $('#wfVersionSelection').val().split('_');
    let wfVersionDropdownWfId = versionDefIdWfId[2];
    let wfVersionDropdownVersion = versionDefIdWfId[0];

    let isCurrentVersionWfId = (parseInt(wfVersionDropdownVersion) === parseInt(version)) && (parseInt(wfVersionDropdownWfId) === parseInt(selectedWfId));
    let isCurrentVersion = (parseInt(currentVersion) === parseInt(version)) && (parseInt(currentWfId) === parseInt(selectedWfId));
    let isWfVersionPresent = currentTableData.some(item => parseInt(item.version) === parseInt(version) && parseInt(item.wfId) === parseInt(selectedWfId));

    removeDeletedSteps(autoStepId, isCurrentVersion);

    if (isCurrentVersionWfId && !isWfVersionPresent) {
        $('#wfVersionSelection').val('');
        $('#wfVersionSelect').val('');
    }

    //To update old step allStepData and restore data attribute
    autoIds.forEach((id, i) => {
        let allStepsSelectedInOldStepDropdown = [];
        $('.oldStepDropDown').each(function () {
            let stepData = $(this).data('selectedItems');
            let collapseBtn = $(this).closest('tr').find('.collapseButton');
            let currentAutoStepId = $(collapseBtn).data('autoid');
            let rowDefId = $(collapseBtn).data('defid');

            if (currentAutoStepId === id && stepData.length !== 0) {
                $.each(stepData, function (j, d) {
                    let stepId = d.stepData.split('_')[0];
                    addUniqueObjInArray(allStepsSelectedInOldStepDropdown, { stepId, defId: rowDefId });
                })
            }
        });

        let rowIndex = currentTableData.findIndex(item => item.autoStepId === id);
        let updatedData = allStepsData[rowIndex].selectedOldStep.filter(d => allStepsSelectedInOldStepDropdown.some(k => k.stepId === d.StepID && parseInt(k.defId) === parseInt(d.defId)));
        allStepsData[rowIndex].selectedOldStep = updatedData;
    });

    restoreStepsData(allStepsData);
    mutipleWFPreCondition();
    //validateData(thisObj);


}

function oldStepsAutoComplete() {
    let selectedRow = $('input[name=botsaving]:checked').data('details');
    let subID = callingPage === C_BOTSAVING ? selectedRow.SubActivityID : urlParams.get("subId");
    let projID = callingPage === C_BOTSAVING ? selectedRow.ProjectID : urlParams.get("projID");

    function split(val) {
        return val.split(/,\s*/);
    }
    function extractLast(term) {
        return split(term).pop();
    }

    $('.oldStepDropDown').each(function () {
        $(this).val('');
        $(this).data('selectedItems', []);
        let oldDropDown = $(this).closest('tr').find('#oldStepDropDown');
        let collapseBtn = $(this).closest('tr').find('.collapseButton');
        let version = $(collapseBtn).data('version');
        let currentVersion = callingPage === C_BOTSAVING ? selectedRow.versionNumber : urlParams.get("version");
        let wfId = $(collapseBtn).data('wfid');
        let wfName = $(collapseBtn).data('wfname');
        let defID = $(collapseBtn).data('defid');
        let autoStepId = $(collapseBtn).data('autoid');
        botSavingsConstants['allOldStepsSelected'][autoStepId] = {};

        let postData = {
            projectID: projID,
            subActivity: subID,
            newVersion: version,
            wfName: wfName,
            wfid: wfId
        };

        if (parseInt(version) === parseInt(currentVersion) && parseInt(defID) === parseInt($("#oldDefID").val())) {
            postData.newDefID = $('#newDefId').val();
            postData.oldDefID = $('#flowChartOldDefId').val();
            postData.newVersion = callingPage === C_BOTSAVING ? currentVersion:$('#newVersion').val();
        } else {
            postData.newDefID = $('#newDefId').val();
            postData.oldDefID = defID;
		}

        $(this).autocomplete({
            source: function (request, response) {
                let searchTermOldStep = extractLast(request.term);
                if (searchTermOldStep === ',' || searchTermOldStep === " " || searchTermOldStep.length <= 2 || searchTermOldStep === "") {
                    $("#oldStepDropDown").removeClass('ui-autocomplete-loading');
                }
                else if (searchTermOldStep.length >= 3) {
                    if (version != 0 || version != null || version != undefined) {
                        $.isf.ajax({
                            url: `${service_java_URL}flowchartController/getOldStepBotSavingDetails?term=${searchTermOldStep}`,
                            type: "POST",
                            data: JSON.stringify(postData),
                            contentType: 'application/json',
                            success: function (data) {
                                $("#oldStepDropDown").autocomplete().addClass("ui-autocomplete-loading");
                                var result = [];
                                if (data.responseData.length > 0) {
                                    $.each(data.responseData, function (i, d) {
                                        if (d.Savings == null || d.Savings == "NA") {
                                            d.Savings = 0;
                                        }
                                        if (d.ExecutionType == "Automatic") {
                                            result.push({
                                                "label": "A/" + d.StepName,
                                                "value": "A/" + d.StepName,
                                                "stepData": d.StepID + '_' + d.ExecutionType + '_' + d.RpaID + '_' + (Number)(d.Savings) + '_' + d.Task
                                            });
                                        }
                                        else {
                                            result.push({
                                                "label": "M/" + d.StepName,
                                                "value": "M/" + d.StepName,
                                                "stepData": d.StepID + '_' + d.ExecutionType + '_' + d.RpaID + '_' + (Number)(d.Savings) + '_' + d.Task
                                            });
                                        }

                                    })
                                    response(result);
                                }
                                else {
                                    response([{ label: 'No matches found.', value: -1 }]);
                                }



                                $("#oldStepDropDown").autocomplete().removeClass("ui-autocomplete-loading");

                            }
                        });
                    }
                }
            },
            minLength: 3,
            appendTo: $('#botDetailModal'),
            focus: function () {
                return false;
            },
            select: function (event, ui) {
                var terms;
                

                if (ui.item.value == -1) {
                    //Clear the AutoComplete TextBox.
                    clearInvalidUserInput($(this));

                } else {
                    terms = split(this.value);
                    let selectedItems = $(this).data('selectedItems');
                    //if (terms.length > 10) {
                    if (selectedItems.length == 10) {
                        pwIsf.alert({ msg: "Maximum limit reached!", type: "warning" });
                        let old = $(oldDropDown).val();
                        let oldsplit = old.split(",");
                        let oldslice = oldsplit.slice(0, -1);
                        oldslice.push(" ");
                        $(oldDropDown).val(oldslice.join(", "));

                    }
                    // remove the current input
                    else {
                        terms.pop();
                        // add the selected item
                        terms.push(ui.item.value);
                        // add placeholder to get the comma-and-space at the end
                        terms.push("");
                        //return unique work flow id.
                        // terms = uniqueValues(terms, true);
                        this.value = terms.join(", ");



                        
                        
                        let allStepId = selectedItems.map(x => x.stepData.split("_")[0]);                        

                        let temp = ui.item.stepData.split('_');
                        let isStepIDexist = allStepId.find(x => x.toString() === temp[0]);
                        if (isStepIDexist) {
                            pwIsf.alert({ msg: "Already selected", type: "warning" });
                            let old = $(oldDropDown).val();
                            let oldsplit = old.split(",");
                            let oldslice = oldsplit.slice(0, -2);
                            oldslice.push(" ");
                            $(oldDropDown).val(oldslice.join(", "));

                            //terms = uniqueValues(allStepId, true);

                        }
                        else {
                            selectedItems.push({ stepData: ui.item.stepData, stepName: ui.item.value });
                            let preCondition = { stepID: temp[0], wfId}
                            getOldStepList(this, preCondition);
                        }
                        
                        
                        botSavingsConstants['allOldStepsSelected'][autoStepId][temp[0]] = ui.item.stepData;
                        
                        
                    }
                }

                
                return false;

            },

        });
        $(this).autocomplete("widget").addClass("fixedHeight");
    });    

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

function uniqueValues(list, isOldStep) {
    var result = [];
    if (isOldStep) {
        $.each(list, function (i, e) {
            if ($.inArray(e.trim(), result) == -1)
                result.push(e.trim());
            else {
                if (e.trim() != '') {
                    pwIsf.alert({ msg: "Already selected", type: "warning" });
                }
            }

        });
    } else {
        $.each(list, function (i, e) {
            if ($.inArray(e.trim(), result) == -1)
                result.push(e.trim());
            else {
                if (e.trim() != '') {
                    $("#oldStepRequired").text(e + ' already selected.');
                    $("#oldStepRequired").show().delay(2000).fadeOut();
                }
            }

        });
    }

    return result;
}

/**
 * Two Bots Restriction Validation
 * 1) Maximum 2 auto steps are allowed at a time in WF+, EWF+ and Bot Approval/Deployement page
 * 2) If existing automantic step's bot is changed to deployed bot, then include its considered for Bot savings.
 * Total such considerations(including new auto steps) must not be more than 2.
 * 3) Return true if all validations pass
 */
function isTwoBotsValidated(oldJsonArr, newJsonArr, deployedBot = null) {
    let OK = true;
    let oldAutoArr = oldJsonArr.filter(item => item.type === C_STEP_TYPE_AUTOMATIC); //Initial flowchart json automatic cells array
    let newAutoArr = newJsonArr.filter(item => item.type === C_STEP_TYPE_AUTOMATIC); //Final flowchart json automatic cells array
    let newBotsCount = 0; //Total Bots considered for Bot Savings

    //Comparing each autostep in new array with old array
    for (const newAuto of newAutoArr) {
        let newRpaId = parseInt(newAuto.rpaid.split('@')[0]);

        let isAutoStepNew = !oldAutoArr.some(item => item.id === newAuto.id); //Checking if this step already exists

        //Checking if the bot has changed to deployed bot
        let isRpaIdChangedToDeployedBot = oldAutoArr.some(item => {
            let oldRpaId = parseInt(item.rpaid.split('@')[0]);

            return newRpaId !== oldRpaId && newRpaId === parseInt(deployedBot);
        });

        /*Conditions required for Bot Savings calculation
         * 1) If auto step is new. (WF+/EWF+/Bot Approval)
         * 2) If existing step's bot is changed to deployed bot (Bot Approval)
         */
        if (isAutoStepNew || (callingPage === FLOWCHARTADD ? false : (!isAutoStepNew && isRpaIdChangedToDeployedBot))) {
            newBotsCount++;
        }
    }

    if (newBotsCount > 2) {
        pwIsf.alert({ msg: 'There is a limit of two BOTs to be integrated simultaneously. However more than two BOTs can be integrated one after the other', type: 'warning' });
        OK = false;
    }

    return OK;
    
}

/**
 * Update Deleted Steps table
 */
function removeDeletedSteps(autoStepId, isCurrentVersion = false) {

    let selectedRow = $('input[name=botsaving]:checked').data('details');
    let version = callingPage === C_BOTSAVING ? selectedRow.versionNumber : urlParams.get("version");
    let currentVersion = callingPage === C_BOTSAVING ? selectedRow.versionNumber : urlParams.get("version");
    let currentWfId = callingPage === C_BOTSAVING ? selectedRow.wfid : urlParams.get("wfid");
    let deletedStepTable = botSavingsConstants.deletedStepTable;
    let deletedStepsTableData = Array.from($(deletedStepTable).DataTable().rows().data());
    let collapseBtnOld = $(this).closest('tr').find('#oldStepDropDown');
    let paramWFId = callingPage === C_BOTSAVING ? selectedRow.wfid : urlParams.get("wfid");
    let oldDefId = $('#oldDefID').val();
    let updatedData = false;
    let allSteps = getAllOldStepsSelected(version, oldDefId);
    let allStepsSelectedInOldStepDropdown = allSteps[0];
    let allStepsSelectedInOldStepDropdownBotWise = allSteps[1];

    if (isCurrentVersion) {
        let currentVersionCount = Array.from($('.collapseButton')).filter(item => parseInt($(item).data('version')) === parseInt(currentVersion) && parseInt($(item).data('wfid')) === parseInt(currentWfId)).length;

        if (currentVersionCount === 0) {
            updatedData = deletedStepsTableData.filter(item => !deletedStepData.deletedSteps.some(i => parseInt(i.defId) === parseInt(item.defId) && i.StepID === item.StepID));            
        }
    } else {
        updatedData = deletedStepsTableData.filter(item => allStepsSelectedInOldStepDropdown.some(d => d.stepId === item.StepID && parseInt(d.defId) === parseInt(item.defId)));
    }

    if (updatedData !== false) {        

        if ($.fn.dataTable.isDataTable(deletedStepTable)) {
            $(deletedStepTable).DataTable().destroy();
            $(deletedStepTable).empty();
        }

        getDeletedStepTable({ deletedSteps: updatedData }, true);

        let newDeletedData = storeUniqueDeletedStepsArray.deletedSteps.filter(i => updatedData.some(d => parseInt(d.defId) === parseInt(i.defId) && d.StepID === i.StepID));
        storeUniqueDeletedStepsArray.deletedSteps = newDeletedData;
    }

    oldStepsUnsatisfied = oldStepsUnsatisfied.filter(item => allStepsSelectedInOldStepDropdownBotWise.some(d => d.autoId === item.autoId && d.stepId === item.stepId && parseInt(d.defId) === parseInt(item.defId) && d.checkUnsatisfied));
    botSavingsConstants.deletedOldSteps[autoStepId] = botSavingsConstants.deletedOldSteps[autoStepId].filter(item => allStepsSelectedInOldStepDropdownBotWise.some(d => d.autoId === autoStepId && d.stepId === item.StepID && parseInt(d.defId) === parseInt(item.defId)));

    if (oldStepsUnsatisfied.length === 0) {
        $('#savingsChekboxDiv').hide();
        $('#savingsCheckBox').prop('checked', false);
        $(collapseBtnOld).css("border", "1px solid #DCDCDC");
    }
    

}

function clearSelectedOldSteps(thisObj) {
    let selectedRow = $('input[name=botsaving]:checked').data('details');
    let deletedStepTable = botSavingsConstants.deletedStepTable;
    let deletedStepsTableData = Array.from($(deletedStepTable).DataTable().rows().data());
    let oldDropDown = $(thisObj).closest('tr').find('#oldStepDropDown');
    let collapseBtn = $(thisObj).closest('tr').find('.collapseButton');
    let currentAutoStepId = $(collapseBtn).data('autoid');
    let currentRowDefId = $(collapseBtn).data('defid');
    let currRowButton = getCalSavingsBtn(thisObj);
    let version = callingPage === C_BOTSAVING ? selectedRow.versionNumber : urlParams.get("version");
    let paramWFId = callingPage === C_BOTSAVING ? selectedRow.wfid : urlParams.get("wfid");
    let oldDefId = $('#oldDefID').val();

    $(oldDropDown).val('');
    $(oldDropDown).data('selectedItems', []);

    let allSteps = getAllOldStepsSelected(version, oldDefId, currentAutoStepId, true);
    let allStepsSelectedInOldStepDropdown = allSteps[0];
    let allStepsSelectedInOldStepDropdownRow = allSteps[1];
    let allStepsSelectedInOldStepDropdownBotWise = allSteps[2];

    let selectedOldStepArr = $(currRowButton).data('selectedOldStep');
    if (selectedOldStepArr !== undefined) {
        let newArr = selectedOldStepArr.filter(item => allStepsSelectedInOldStepDropdownRow.some(d => d.stepId === item.StepID && parseInt(d.defId) === parseInt(item.defId)));
        $(currRowButton).data('selectedOldStep', newArr);
    }

    let updatedData = deletedStepsTableData.filter(item => allStepsSelectedInOldStepDropdown.some(d => d.stepId === item.StepID && parseInt(d.defId) === parseInt(item.defId)));
    if ($.fn.dataTable.isDataTable(deletedStepTable)) {
        $(deletedStepTable).DataTable().destroy();
        $(deletedStepTable).empty();
    }
    getDeletedStepTable({ deletedSteps: updatedData }, true);

    let newDeletedData = storeUniqueDeletedStepsArray.deletedSteps.filter(i => updatedData.some(d => parseInt(d.defId) === parseInt(i.defId) && d.StepID === i.StepID));
    storeUniqueDeletedStepsArray.deletedSteps = newDeletedData;

    oldStepsUnsatisfied = oldStepsUnsatisfied.filter(item => allStepsSelectedInOldStepDropdownBotWise.some(d => d.autoId === item.autoId && d.stepId === item.stepId && parseInt(d.defId) === parseInt(item.defId) && d.checkUnsatisfied));
    botSavingsConstants.deletedOldSteps[currentAutoStepId] = botSavingsConstants.deletedOldSteps[currentAutoStepId].filter(item => allStepsSelectedInOldStepDropdownBotWise.some(d => d.autoId === currentAutoStepId && d.stepId === item.StepID && parseInt(d.defId) === parseInt(item.defId)));
    $(oldDropDown).css("border", "1px solid #DCDCDC");

    if (oldStepsUnsatisfied.length === 0) {
        $('#savingsChekboxDiv').hide();
        $('#savingsCheckBox').prop('checked', false);
        
    }

    checkNoOldStepButNewStepCondition(thisObj);

}

function getCalSavingsBtn(thisObj, autoId = false) {
    if (!autoId) {
        autoId = $(thisObj).closest('tr').find('.collapseButton').data('autoid');
    }
    let botSavingTable = botSavingsConstants.botSavingTable;
    let botSavingTableData = Array.from($(botSavingTable).DataTable().rows().data());
    let firstIndex = botSavingTableData.findIndex(item => item.autoStepId === autoId);

    let row = document.querySelector("#botSavingTable tbody").rows[firstIndex];

    return $(row).find('#calculateSavings')[0];
}

function getAllOldStepsSelected(version, defId, currentAutoStepId = false, getRowOldStepSelected = false) {

    let allStepsSelectedInOldStepDropdown = [];
    let allStepsSelectedInOldStepDropdownRow = [];
    let allStepsSelectedInOldStepDropdownBotWise = [];
    $('.oldStepDropDown').each(function () {
        let stepData = $(this).data('selectedItems');
        let collapseBtnRow = $(this).closest('tr').find('.collapseButton');
        let rowVersion = $(collapseBtnRow).data('version');
        let rowDefId = $(collapseBtnRow).data('defid');
        let autoStepId = $(collapseBtnRow).data('autoid');

        if (parseInt(version) === parseInt(rowVersion) && parseInt(defId) === parseInt(rowDefId)) {
            $.each(deletedStepData.deletedSteps, function (i, d) {
                addUniqueObjInArray(allStepsSelectedInOldStepDropdown, { stepId: d.StepID, defId: d.defId });
                addUniqueObjInArray(allStepsSelectedInOldStepDropdownBotWise, { autoId: autoStepId, stepId: d.StepID, defId: d.defId ,checkUnsatisfied: false });
            });
        }

        if (stepData.length !== 0) {
            $.each(stepData, function (i, d) {
                let stepId = d.stepData.split('_')[0];
                addUniqueObjInArray(allStepsSelectedInOldStepDropdown, { stepId, defId: rowDefId });
                addUniqueObjInArray(allStepsSelectedInOldStepDropdownBotWise, { autoId: autoStepId, stepId, defId: rowDefId, checkUnsatisfied: true});

                if (currentAutoStepId && currentAutoStepId === autoStepId) {
                    addUniqueObjInArray(allStepsSelectedInOldStepDropdownRow, { stepId, defId: rowDefId });
                }
            })
        }
    });

    return getRowOldStepSelected ? [allStepsSelectedInOldStepDropdown, allStepsSelectedInOldStepDropdownRow, allStepsSelectedInOldStepDropdownBotWise] :
        [allStepsSelectedInOldStepDropdown, allStepsSelectedInOldStepDropdownBotWise];
}

function addUniqueObjInArray(arr, obj) {
    let isObjPresent = arr.some(item => {
        let objKeys = Object.keys(obj);
        let equality = true;
        objKeys.forEach(key => equality = equality && item[key] === obj[key]);
        return equality;
    });
    if (!isObjPresent) {
        arr.push(obj);
    }
}

function updateSelectedOldStepWithSavings() {
    let indices = getBotRowIndices();
    let deletedStepTable = botSavingsConstants.deletedStepTable;
    let deletedStepsTableData = Array.from($(deletedStepTable).DataTable().rows().data());

    indices.forEach((indexVal, i) => {
        let savingsBtn = $('.calculateSavings')[indexVal];
        let selectedOldStep = [...$(savingsBtn).data('selectedOldStep')];

        selectedOldStep.forEach(item => {
            let matchingStep = deletedStepsTableData.find(d => d.StepID === item.StepID && parseInt(d.defId) === parseInt(item.defId));
            if (matchingStep !== undefined && matchingStep !== null) {
                item.Saving = matchingStep.Savings;
            }
        })

    });
}

// FORMAT DATE
function formatted_date (date) {
    var m = ("0" + (date.getMonth() + 1)).slice(-2); // in javascript month start from 0.
    var d = ("0" + date.getDate()).slice(-2); // add leading zero 
    var y = date.getFullYear();
    return y + '-' + m + '-' + d;
}


function addUniqueStepAndDefIdInArray(arr, step) {
    if (!arr.some(e => e.StepID === step.StepID && parseInt(e.defId) === parseInt(step.defId))) {
        arr.push(step);
    }
}

function oldstepToolTip(thisObj,e){
    oldID = $(thisObj).closest('tr').find('#oldStepDropDown').val();
    oldIDName = oldID.split(",");
    if (oldID.length > 0) {
        thisObj.title = oldIDName;
    }
    else {
        thisObj.title = '';
    }
}

function oldStep20WO(deletedStepsArray, currRowButton) {
    let is20WOExecutedInArray = deletedStepsArray.filter(function (el) { if (el.ExecutionType === "Manual" && el.flag == true) { return el.flag; } });
    let isAll20WOExecuted = true;
    for (let i in deletedStepsArray) {
        if (deletedStepsArray[i].ExecutionType === "Manual" && deletedStepsArray[i].isAll20WOExecuted !== true) {
            isAll20WOExecuted = false;
            break;
        }
    }
    let is20WOExecuted = is20WOExecutedInArray.length !== 0;
    $(currRowButton).data('isAll20WOExecuted', isAll20WOExecuted);
    $(currRowButton).data('is20WOExecuted', is20WOExecuted);
}

function getUniqueData(rpaid) {

    let arrRpaid = rpaid.split(' ');

    let uniq = [...new Set(arrRpaid)];
    uniq = uniq.toString();
    uniq = uniq.replace(/,\s*$/, "");
    return uniq;
}

function mutipleWFPreCondition() {
    var arr1 = getBotTableDataWithState();
    let botSavingRows = $('#botSavingTable').dataTable().fnGetNodes();
    let autoIds = new Set(botSavingRows.map(item => $(item).find('.collapseButton').data('autoid')));
    autoIds.forEach(id => {
        let btnObj = getCalSavingsBtn(null, id);
        arr1.forEach(obj => {
            if (obj.autoStepId === id) {
                if (obj.isStepPreConditionSatisfied === false) {
                    $(btnObj).data("isPreCondtionSatisfied", false);
                    return false;
                }
                $(btnObj).data("isPreCondtionSatisfied", true);
            }
        });
        validateData(btnObj);
    });

}