const automaticStep = 'ericsson.Automatic';
const manualStep = 'ericsson.Manual';
const decisionStep = 'ericsson.Decision';
const startStep = 'ericsson.StartStep';
const endStep = 'ericsson.EndStep';
const globalResponsible = ['MA', 'GLOBAL', 'ASP'];
const showValidateAdjustBtn = true;

function getElementCountByTypeName(rappidObj, typeName) {

    let allElements = rappidObj.graph.toJSON();
    let count = 0;
    for (let i in allElements['cells']) {
        if (allElements['cells'][i]['type'] == typeName) {
            count++;
        }
    }
    return count;
}

function removeElementIfCountGreaterThan(rappidObj, cell, elements = []) {

    for (let i in elements) {
        if (getElementCountByTypeName(rappidObj, elements[i]['elName']) > elements[i]['elCount']) {

            rappidObj.graph.removeCells(cell);
        }
    }

}

function getElementType(cell) {
    return cell['attributes']['type'];
}


function callAfterAddElementOnPaper(rappidObj, cell, collection, opt) {


    if (cell.attributes.type == startStep || cell.attributes.type == endStep) {
        removeElementIfCountGreaterThan(rappidObj, cell, [{ elCount: 1, elName: startStep }, { elCount: 1, elName: endStep }]);
    }

}

function callOnMouseUpOnElement(cellView) {


    if (cellView.model.attributes.type == startStep) {
        var halo = new joint.ui.Halo({ cellView: cellView });

        halo.removeHandle('remove');
        halo.removeHandle('clone');
        halo.removeHandle('resize');
        halo.removeHandle('fork');
        halo.render();
    }

    // Remove halo tools from stop step 
    if (cellView.model.attributes.type == endStep) {
        var halo = new joint.ui.Halo({ cellView: cellView });

        //halo.removeHandle('remove');
        halo.removeHandle('clone');
        halo.removeHandle('resize');
        halo.removeHandle('fork');
        halo.removeHandle('link');
        halo.render();

    }

}


function callOnDragEnd(rappidObj, cell) { // Take action on Drag End of element
    var cloneEg = cell.clone();
    //if($('#max_id').val() == ""){$('#max_id').val('1000')}
    // var maxid = parseInt($('#max_id').val())
    // maxid = ++maxid; maxid = maxid.toString(); $('#max_id').val(maxid);
    //cloneEg.set("id", maxid);

    if (cell.attributes.type == manualStep || cell.attributes.type == automaticStep) {
        cloneEg.resize(250, 145);
        // previously value was cloneEg.resize(190, 100);before 9-sept-2020
    }
    if (cell.attributes.type == decisionStep) {
        cloneEg.resize(170, 80);
    }
    if (cell.attributes.type == startStep) {
        //rappidObj.graph.removeCells(cell);
        //return true;
    }

    return cloneEg;
}


function autoResize(rappidObj, element) {  // Make autosize of step boxes according to text

    let view = rappidObj.paper.findViewByModel(element);
    let text;

    if (element.attributes.type == automaticStep || element.attributes.type == manualStep) {
        text = view.$('text')[1];
        // Use bounding box without transformations so that our autosizing works
        // even on e.g. rotated element.
        var bbox = V(text).bbox(true);
        // Give the element some padding on the right/bottom.
        if (bbox.width < 250) {
            element.resize(250, bbox.height + 80);
        } else {
            element.resize(bbox.width + 5, bbox.height + 80);
        }
        //previous condition before 9-sept-2020
        //if (bbox.width < 200) {
        //    element.resize(200, bbox.height + 80);
        //} else {
        //    element.resize(bbox.width + 5, bbox.height + 80);
        //}
        
    }
    else if (element.attributes.type == decisionStep) {
        text = view.$('text')[0];
        // Use bounding box without transformations so that our autosizing works
        // even on e.g. rotated element.
        var bbox = V(text).bbox(true);
        // Give the element some padding on the right/bottom.
        element.resize(bbox.width + 50, bbox.height + 70);
    }
    else if (element.attributes.type == startStep || element.attributes.type == endStep) {

    }


}

function unhighlightAllCells(rappidObj) {

    let allCells = rappidObj.graph.getCells();

    for (let cel in allCells) {
        let cell = allCells[cel];
        errorUnHighlight(rappidObj, cell);

    }

}


function errorUnHighlight(rappidObj, currentCell) { // UNHIGHLIGHT SINGLE CELL
    var cellView = rappidObj.paper.findViewByModel(currentCell);
    cellView.unhighlight(null/* defaults to cellView.el */, {
        highlighter: {
            name: 'stroke',
            options: {
                //padding: 10,
                // rx: 5,
                // ry: 5,
                attrs: {
                    'stroke-width': 5,
                    stroke: '#FF0000'
                }
            }
        }
    });
}

function errorHighlight(rappidObj, currentCell) { // HIGHLIGHT SINGLE CELL
    var cellView = rappidObj.paper.findViewByModel(currentCell);

    cellView.highlight(null/* defaults to cellView.el */, {
        highlighter: {
            name: 'stroke',
            options: {
                //padding: 10,
                // rx: 5,
                // ry: 5,
                attrs: {
                    'stroke-width': 5,
                    stroke: '#FF0000'
                }
            }
        }
    });
}

function getTaskIdFromActionAttr(passCell, rappidObj) {
    let actionName = passCell.get('action');
    let returnTaskId = '';
    if (actionName) {
        let name = actionName.split('@');
        returnTaskId = name[0];
    } else {
        if (passCell.attributes.attrs.task) {
            let mkActionName = passCell.attributes.attrs.task.taskID + '@' + passCell.attributes.attrs.task.taskName;
            rappidObj.graph.getCell(passCell.id).attributes.action = mkActionName;
            returnTaskId = passCell.attributes.attrs.task.taskID;
        }
    }
    return returnTaskId;
}

function validateWorkflow(rappidObj) {
    let errorsArr = [];
    let allCells = rappidObj.graph.getCells();
    let automaticTaskArr = [];
    let manualTaskArr = [];


    if (getElementCountByTypeName(rappidObj, startStep) != 1) {
        errorsArr.push({ errorDesc: 'One Strat box should be there.', actionText: 'Add or Remove manualy', elId: 0 });
    }
    if (getElementCountByTypeName(rappidObj, endStep) != 1) {
        errorsArr.push({ errorDesc: 'One End box should be there.', actionText: 'Add or Remove manualy', elId: 0 });
    }

    //console.log('AllCells->', allCells);


    let validateTaskName = (passTaskId, stepType) => {

        if (stepType == manualStep) {
            if (automaticTaskArr.includes(passTaskId)) {
                return false;
            } else {
                if (!manualTaskArr.includes(passTaskId)) {
                    manualTaskArr.push(passTaskId);
                }
            }
        }
        else {
            if (manualTaskArr.includes(passTaskId)) {
                return false;
            } else {
                if (!automaticTaskArr.includes(passTaskId)) {
                    automaticTaskArr.push(passTaskId);
                }
            }
        }

        return true;

    };


    for (let cel in allCells) {
        let cell = allCells[cel];
        let elementType = getElementType(cell);
        let elementId = allCells[cel]['id'];
        let actText = 'Fixit';
        let responsible = '';

        if (elementType == manualStep || elementType == automaticStep) {
            responsible = cell.get('responsible'); //(allCells[cel]['']['responsible']) ? allCells[cel]['responsible']:"";
            if (!responsible) {
                responsible = '';
            }
        }

        let inboundConnectedLinks = rappidObj.graph.getConnectedLinks(allCells[cel], { inbound: true });
        let outboundConnectedLinks = rappidObj.graph.getConnectedLinks(allCells[cel], { outbound: true });


        //Start : Validate for Start Step
        if (elementType == startStep) {
            let startStepErrorFlag = false;

            if (inboundConnectedLinks.length > 0) {
                errorsArr.push({ errorDesc: 'Not required inbound connection for Start step. (It will not have any parent)', actionText: actText, elId: elementId });
                startStepErrorFlag = true;
            }

            if (outboundConnectedLinks.length != 1) {
                errorsArr.push({ errorDesc: 'Start step must have only one outbond connection. (It should connect with one)', actionText: actText, elId: elementId });
                startStepErrorFlag = true;
            }

            if (startStepErrorFlag) {
                errorHighlight(rappidObj, cell);
            }
        }
        //End : Validate for Start Step

        //Start : Validate for End Step
        if (elementType == endStep) {
            let endStepErrorFlag = false;

            if (inboundConnectedLinks.length < 1) {
                errorsArr.push({ errorDesc: 'Atleast one inbound connection required for End step. (It should have atleast one parent)', actionText: actText, elId: elementId });
                endStepErrorFlag = true;
            }

            if (outboundConnectedLinks.length != 0) {
                errorsArr.push({ errorDesc: 'End step can not have outbond connection.', actionText: actText, elId: elementId });
                endStepErrorFlag = true;
            }

            if (endStepErrorFlag) {
                errorHighlight(rappidObj, cell);
            }

        }
        //End : Validate for End Step

        //Start : Validate for Manual Step
        if (elementType == manualStep) {
            let stepName = allCells[cel]['attributes']['name'];
            let toolName = allCells[cel]['attributes']['tool'];

            let manualErrorFlag = false;
            let prefixText = ' <label class="label label-info "><i>Manual Step</i></label>';
            let taskId;

            taskId = getTaskIdFromActionAttr(cell, rappidObj);
            let taskName = cell.get('action');

            if (!taskName) {
                errorsArr.push({ errorDesc: `Task name not selected.${prefixText}`, actionText: actText, elId: elementId });
                manualErrorFlag = true;
            }

            if (!validateTaskName(taskId, manualStep)) {
                //errorsArr.push({ errorDesc: `Task name already assigned to automatic step.${prefixText}`, actionText: actText, elId: elementId });
                //manualErrorFlag = true;
            }


            if (inboundConnectedLinks.length < 1) {
                errorsArr.push({ errorDesc: `Parent connection not found.${prefixText}`, actionText: actText, elId: elementId });
                manualErrorFlag = true;
            }

            if (outboundConnectedLinks.length != 1) {
                errorsArr.push({
                    errorDesc: `It should connect with one.${prefixText}`, actionText: actText, elId: elementId
                });
                manualErrorFlag = true;
            }

            if (!stepName) {
                errorsArr.push({
                    errorDesc: `Step name not defined.${prefixText}`, actionText: actText, elId: elementId
                });
                manualErrorFlag = true;
            }
            if (!toolName) {
                errorsArr.push({
                    errorDesc: `Tool name not selected.${prefixText}`, actionText: actText, elId: elementId
                });
                manualErrorFlag = true;
            }

            if (!globalResponsible.includes(responsible.trim())) {
                errorsArr.push({
                    errorDesc: `Responsible not selected or wrong defined.${prefixText}`, actionText: actText, elId: elementId
                });
                manualErrorFlag = true;
            }

            if (manualErrorFlag) {
                errorHighlight(rappidObj, cell);
            }


        }
        //End : Validate for Manual Step

        //Start : Validate for Automatic Step
        if (elementType == automaticStep) {
            let sourceElement = ''; let targetElement = '';
            let stepName = allCells[cel]['attributes']['name'];
            let toolName = allCells[cel]['attributes']['tool'];
            let botType = allCells[cel]['attributes']['botType'];
            let rpaid = allCells[cel]['attributes']['attrs']['tool']['RPAID'];
            let automaticErrorFlag = false;
            let prefixText = ' <label class="label label-warning "><i>Automatic Step</i></label>';
            let taskId;
            let isRunOnServer = allCells[cel]['attributes']['attrs']['tool']['isRunOnServer'];
            taskId = getTaskIdFromActionAttr(cell, rappidObj);
            let taskName = cell.get('action');
            let uploadOutput = cell.get('outputUpload');
            let cascadeInput = cell.get('cascadeInput');
            let botConfigJson = cell.get('botConfigJson');

            (inboundConnectedLinks.length != 0) ? sourceElement = inboundConnectedLinks[0].getSourceElement() : sourceElement = '';
            (outboundConnectedLinks.length != 0) ? targetElement = outboundConnectedLinks[0].getTargetElement() : targetElement = '';
            if (!taskName) {
                errorsArr.push({ errorDesc: `Task name not selected.${prefixText}`, actionText: actText, elId: elementId });
                automaticErrorFlag = true;
            }

            if (!uploadOutput) {
                errorsArr.push({ errorDesc: `No value selected for Output Upload.${prefixText}`, actionText: actText, elId: elementId });
                automaticErrorFlag = true;
            }

            if (!validateTaskName(taskId, automaticStep)) {
                //errorsArr.push({ errorDesc: `Task name already assigned to manual step.${prefixText}`, actionText: actText, elId: elementId });
                //automaticErrorFlag = true;
            }

            if (inboundConnectedLinks.length < 1) {
                errorsArr.push({ errorDesc: `Parent connection not found.${prefixText}`, actionText: actText, elId: elementId });
                automaticErrorFlag = true;
            }

            if (outboundConnectedLinks.length != 1) {
                errorsArr.push({ errorDesc: `It should connect with one.${prefixText}`, actionText: actText, elId: elementId });
                automaticErrorFlag = true;
            }

            if (!stepName) {
                errorsArr.push({ errorDesc: `Step name not defined.${prefixText}`, actionText: actText, elId: elementId });
                automaticErrorFlag = true;
            }
            if (!toolName) {
                errorsArr.push({ errorDesc: `Tool name not selected.${prefixText}`, actionText: actText, elId: elementId });
                automaticErrorFlag = true;
            }
            if (botType == null) {
                errorsArr.push({ errorDesc: `BotType is null when selecting RPAId. Please select RPAID again.${prefixText}`, actionText: actText, elId: elementId });
                automaticErrorFlag = true;
            }

            if (!rpaid || rpaid == 0 || rpaid == "0" || rpaid == "") {
                errorsArr.push({
                    errorDesc: `RPAID not selected.${prefixText}`, actionText: actText, elId: elementId
                });
                automaticErrorFlag = true;
            }

            //if (responsible.trim() == "") {
            if (!globalResponsible.includes(responsible.trim())) {
                errorsArr.push({ errorDesc: `Responsible not selected or wrong defined.${prefixText}`, actionText: actText, elId: elementId });
                automaticErrorFlag = true;
            }
            if (cascadeInput != null && cascadeInput != undefined && cascadeInput != '') {
                if (cascadeInput.toLowerCase() == "yes" && isRunOnServer == "0") {
                    errorsArr.push({ errorDesc: `Cascade Input cannot be yes for non Server Bot.${prefixText}`, actionText: actText, elId: elementId });
                    automaticErrorFlag = true;
                }
            }
            //if ((cascadeInput.toLowerCase() == "yes") && (isRunOnServer == null || isRunOnServer == undefined)) {
            //    errorsArr.push({ errorDesc: `Cascade Input cannot be yes for non Server Bot.${prefixText}`, actionText: actText, elId: elementId });
            //    automaticErrorFlag = true;
            //}
            if (sourceElement != '') {
                if (cascadeInput != null && cascadeInput.toLowerCase() == "yes" && sourceElement.get('type') == startStep) {
                    errorsArr.push({ errorDesc: `First step cannot be cascaded step.${prefixText}`, actionText: actText, elId: elementId });
                }
                if (cascadeInput != null && cascadeInput.toLowerCase() == 'yes' && (sourceElement.get('type') == manualStep || sourceElement.get('type') == decisionStep)) {
                    errorsArr.push({ errorDesc: `Parent step of cascaded step cannot be Manual or Decision Step.${prefixText}`, actionText: actText, elId: elementId });
                }
                if (cascadeInput != null && cascadeInput.toLowerCase() == 'yes' && sourceElement.get('type') == automaticStep) {
                    if (sourceElement.get('outputUpload').toLowerCase() == 'no') {
                        errorsArr.push({ errorDesc: `Parent step of cascaded step should have uploadOutput as 'YES'.${prefixText}`, actionText: actText, elId: elementId });
                    }
                }
                if (cascadeInput != null && cascadeInput.toLowerCase() == 'yes') {
                    if (uploadOutput.toLowerCase() == 'no') {
                        errorsArr.push({ errorDesc: `Cascaded step should have uploadOutput as 'YES'.${prefixText}`, actionText: actText, elId: elementId });
                    }
                }
                if (cascadeInput != null && cascadeInput.toLowerCase() == 'yes' && botType.toLowerCase() == 'botnest') {
                    errorsArr.push({ errorDesc: `Cascaded step BOT cannot be of type BOTNest'.${prefixText}`, actionText: actText, elId: elementId });
                }
                if (cascadeInput != null && cascadeInput.toLowerCase() == 'yes' && botType.toLowerCase() != 'botnest' && botConfigJson != "") {
                    errorsArr.push({ errorDesc: `Cascaded step BOT cannot have BOTConfig.${prefixText}`, actionText: actText, elId: elementId });
                }
                if (cascadeInput != null && cascadeInput.toLowerCase() == 'yes') {
                    //if (targetElement.get('type') == automaticStep && targetElement.get('botType').toLowerCase() == 'botnest') {
                    //    errorsArr.push({ errorDesc: `Child step of cascadedStep cannot be of type BOTNest.${prefixText}`, actionText: actText, elId: elementId });
                    //}
                    if (sourceElement.get('type') == automaticStep && sourceElement.get('botType').toLowerCase() == 'botnest') {
                        errorsArr.push({ errorDesc: `Parent step of cascadedStep cannot be of type BOTNest.${prefixText}`, actionText: actText, elId: elementId });
                    }
                }
            }

            if (automaticErrorFlag) {
                errorHighlight(rappidObj, cell);
            }
        }
        //End : Validate for Automatic Step

        //Start : Validate for Decision Step
        if (elementType == decisionStep) {
            let decisionErrorFlag = false;
            let linkErrorFlag = false;
            let stepName = allCells[cel]['attributes']['attrs']['label']['text'];


            if (!stepName) {
                errorsArr.push({
                    errorDesc: `Step name not defined. [Decision Step]`, actionText: actText, elId: elementId
                });
                decisionErrorFlag = true;
            }
            if (inboundConnectedLinks.length < 1) {
                errorsArr.push({ errorDesc: 'Atleast one inbound connection required for Decision step. (It should have atleast one parent)', actionText: actText, elId: elementId });
                decisionErrorFlag = true;
            }

            if (outboundConnectedLinks.length < 2) {
                errorsArr.push({ errorDesc: 'Decision step required atleast two outbond connection.', actionText: actText, elId: elementId });
                decisionErrorFlag = true;
            }

            //Check link labels of Decision step 
            let linkCell = '';
            if (outboundConnectedLinks.length >= 2) {
                   
                 if(outboundConnectedLinks[0]["attributes"]["labels"].length > 0){
            

                let firstLinkLabel = outboundConnectedLinks[0]["attributes"]["labels"][0]["attrs"]["text"]["text"];
                let labelArray = [];

                for (let c in outboundConnectedLinks) {
                    labelArray.push(outboundConnectedLinks[c]["attributes"]["labels"][0]["attrs"]["text"]["text"].toLowerCase().trim());
                }
                let map = {};
                for (let i = 0; i < labelArray.length; i++) {
                    // check if object contains entry with this element as key
                    if (map[labelArray[i]]) {
                        errorsArr.push({ errorDesc: 'Outbound connections of Decision step have 2 links with same label.', actionText: actText, elId: elementId });
                        linkErrorFlag = true;
                        linkCell = outboundConnectedLinks[i];
                        // terminate the loop
                        break;
                    }
                    map[labelArray[i]] = true;
                }
                }

                else {
                    errorsArr.push({
                        errorDesc: `Label not defined. [Decision Step]`, actionText: actText, elId: elementId
                    });
                    decisionErrorFlag = true;

                 }

            }

            if (outboundConnectedLinks.length > 1) {

                let targetIdsArr = [];
                let allManualStepsHaveRule = true;
                let anyManualStepHasRule = false;
                let nonManualStepPresent = false;
                let manualStepsRuleObj = [];

                for (let c in outboundConnectedLinks) {
                    let decCell = outboundConnectedLinks[c];
                    let getDecLinkTarget = decCell.getTargetElement();

                    if (getDecLinkTarget) {
                        let targetId = getDecLinkTarget.get('id');

                        if (targetIdsArr.includes(targetId)) {
                            errorsArr.push({ errorDesc: 'Mutiple conection with same step. [Decision Step]', actionText: actText, elId: elementId });
                            decisionErrorFlag = true;
                            break;
                        } else {
                            targetIdsArr.push(targetId);}
                            if (getDecLinkTarget.attributes.type == manualStep) {
                                if (getDecLinkTarget.attributes.startRule == true || getDecLinkTarget.attributes.stopRule == true) {
                                    anyManualStepHasRule = true;
                            }
                            else {
                                allManualStepsHaveRule = false;
                            }

                            let ruleObj = new Object();
                            ruleObj.startRule = getDecLinkTarget.attributes.startRule;
                            ruleObj.stopRule = getDecLinkTarget.attributes.stopRule;
                            manualStepsRuleObj.push(ruleObj);

                        }
                        else /*if (getDecLinkTarget.attributes.type == automaticStep)*/ {
                            nonManualStepPresent = true;
                        }
                    }

                }

                if (anyManualStepHasRule) {
                    if (!allManualStepsHaveRule) {
                        errorsArr.push({ errorDesc: 'Either all manual steps linked to a decision step should have rules or none', actionText: actText, elId: elementId });
                        decisionErrorFlag = true;
                    }
                    if (nonManualStepPresent) {
                        errorsArr.push({ errorDesc: 'Autosense rules will not work if non-manual step is linked to a decision step', actionText: actText, elId: elementId });
                        decisionErrorFlag = true;
                    }

                    let areRulesUniformity = manualStepsRuleObj.every(function (d) {
                        return d.startRule == manualStepsRuleObj[0].startRule && d.stopRule == manualStepsRuleObj[0].stopRule;
                    });

                    if (!areRulesUniformity) {
                        errorsArr.push({ errorDesc: 'All manual steps linked to a decision step have uniformity in rules combination', actionText: actText, elId: elementId });
                        decisionErrorFlag = true;
                    }

                }

            }
            if (decisionErrorFlag) {
                errorHighlight(rappidObj, cell);
            }
            if (linkErrorFlag) {
                errorHighlight(rappidObj, linkCell);
                linkErrorFlag = false;
            }

        }
        //End : Validate for Decision Step

        //Start : Validate for Links
        if (elementType == 'app.Link') {
            let linkErrorFlag = false;
            let sourceElement = cell.getSourceElement();
            let targetElement = cell.getTargetElement();

            if (!sourceElement) {
                errorsArr.push({ errorDesc: 'Link source is missing.', actionText: actText, elId: elementId });
                linkErrorFlag = true;
            } else {

                if (getElementType(sourceElement) == decisionStep) {

                    if (cell['attributes']['labels'].length < 1) {
                        errorsArr.push({ errorDesc: 'Label missing for this Link.', actionText: actText, elId: elementId });
                        linkErrorFlag = true;
                    }
                }
            }

            if (!targetElement) {
                errorsArr.push({ errorDesc: 'Link target is missing.', actionText: actText, elId: elementId });
                linkErrorFlag = true;
            }

            if (linkErrorFlag) {
                errorHighlight(rappidObj, cell);
            }

        }
        //End : Validate for Links

    }


        //console.log('errorsArr->', errorsArr);


    if (errorsArr.length) {
        let validateStatus = { status: false, errors: errorsArr };
        return validateStatus;
    } else {
        let validateStatus = { status: true, errors: errorsArr };
        return validateStatus;
    }

}

function validateForm() {
    let errorsArr = [];
    let formAreaErrorFlag = false;
    let actText = 'Fixit';
    let WFName, editReason;
    //let ruleIframeBody = document.getElementById('iframeAutosenseRule').contentWindow.document.body;
    let wfnameDiv = $('#WFName');
    let editReasonWFDiv; 
    let slaHoursDiv = $('#SLAHours');
    let ftrDiv = $('#ftr');
    let loeDiv = $('#loe');
    let multimode = $('.multiModeCheck').is(':checked');
    let mandatoryKpiField = $('#mandatoryInputFlag').val();
    let mandatoryDiv = '#' + mandatoryKpiField;
    //if (window.location.href.indexOf("FlowChartWorkOrderAdd") != -1) {
    //    let savingstate = savingState;
    //}

    if (window.location.href.indexOf("FlowChartWorkOrderAdd") != -1) {
        WFName = $('#WFName').val();      
        editReason = $('#editReasonDD option:selected').val();
        editReasonWFDiv = $('#editReasonDD');
    }
    if (window.location.href.indexOf("FlowChartBOTEdit") != -1) {
        editReasonWFDiv = $('#editReasonDDBot');
        editReason = $('#editReasonDDBot option:selected').val();
    }
    if (window.location.href.indexOf("FlowChartWorkOrderAdd") != -1) {
        var iWfLevelArrLength = 0;
        iWfLevelArrLength = $(".iRow").length;
        if (signumGlobal.toLowerCase() == wfOwnerName.toLowerCase()) {
            if (!(iWfLevelArrLength > 0)) {
                errorsArr.push({
                    errorDesc: `Atleast one work instruction required.`, actionText: actText
                });
                formAreaErrorFlag = true;

            }
        }
    }
    if ((WFName == '' || WFName == null) && (window.location.href.indexOf("FlowChartWorkOrderAdd") != -1)) {
        //pwIsf.alert({ msg: "Please name the work flow", type: 'error' });
        errorsArr.push({
            errorDesc: `Please name the work flow`, actionText: actText
        });
        formAreaErrorFlag = true;
        wfnameDiv[0].style.cssText = "border: 1px solid red; border-radius: 5px;height:30px;";
    }
    // alert("Please name the work flow");
    if (($('#SLAHours').css('borderColor') == 'rgb(255, 0, 0)' || $('#SLAHours').val() == '') && (window.location.href.indexOf("FlowChartWorkOrderAdd") != -1)) {
        //pwIsf.alert({ msg: "Please enter valid SLA Hours (<=72 hours)", type: 'error' });
         errorsArr.push({
             errorDesc: `Please enter valid SLA Hours (<=72 hours)`, actionText: actText
         });
         formAreaErrorFlag = true;
        slaHoursDiv[0].style.cssText = "border: 1px solid red; border-radius: 5px;height:30px;";
    }

    if (($('#ftr').val() == '-1' || $('#ftr').val() == null) && (window.location.href.indexOf("FlowChartWorkOrderAdd") != -1)) {
        // pwIsf.alert({ msg: "Please select FTR", type: 'error' });
        errorsArr.push({
            errorDesc: `Please select FTR`, actionText: actText
        });
        formAreaErrorFlag = true;
        ftrDiv[0].style.cssText = "border: 1px solid red; border-radius: 5px;height:30px;width:236px;";
    }

    if (($('#loe').val() == '-1' || $('#loe').val() == null) && (window.location.href.indexOf("FlowChartWorkOrderAdd") != -1)) {
        errorsArr.push({
            errorDesc: `Please select LOE Measurement`, actionText: actText
        });
        formAreaErrorFlag = true;
        loeDiv[0].style.cssText = "border: 1px solid red; border-radius: 5px;height:30px;width:236px;";
    }

    if ((version != '0') && (editReason == '0' || editReason == "" || editReason == null) && (window.location.href.indexOf("FlowChartWorkOrderAdd") != -1)) {
        //pwIsf.alert({ msg: "Please select the reason to Edit the Workflow!", type: 'error' });
        errorsArr.push({
            errorDesc: `Please select the reason to Edit the Workflow!`, actionText: actText
        });
        formAreaErrorFlag = true;
        editReasonWFDiv[0].style.cssText = "border: 1px solid red; border-radius: 5px;height:30px; width:247px;";
    }

    if ((editReason == '0' || editReason == "" || editReason == null) && (window.location.href.indexOf("FlowChartBOTEdit") != -1)) {
        //pwIsf.alert({ msg: "Please select the reason to Edit the Workflow!", type: 'error' });
        errorsArr.push({
            errorDesc: `Please select the reason to Edit the Workflow!`, actionText: actText
        });
        formAreaErrorFlag = true;
        editReasonWFDiv[0].style.cssText = "border: 1px solid red; border-radius: 5px;height:30px; width:212px;";
    }
    if (multimode == true) {
        if ($(mandatoryDiv).val() == null || $(mandatoryDiv).val() == undefined || $(mandatoryDiv).val() == '') {
            //pwIsf.alert({ msg: "Please select the reason to Edit the Workflow!", type: 'error' });
            errorsArr.push({
                errorDesc: `Please fill the mandatory KPI Field!`, actionText: actText
            });
            formAreaErrorFlag = true;
            $((mandatoryDiv))[0].style.cssText = "border: 1px solid red; border-radius: 5px;height:30px;width:266px;";
        }
    }

    if (errorsArr.length) {
        let validateStatus = { status: false, errors: errorsArr };
        return validateStatus;
        //return false;
    } else {
        let validateStatus = { status: true, errors: errorsArr };
        return validateStatus;
        //return true;
    }
}

function appendErrorMainAreaToDOM() {
    let html = `<div id="wf_error_list_area" style="display:none;position: absolute; width: 52%; margin: auto 231px;top:167px;z-index:4;left:67px;">
        <div class="errors" style="-webkit-box-shadow: 0px 9px 114px 2px rgba(0,0,0,0.75); -moz-box-shadow: 0px 9px 114px 2px rgba(0,0,0,0.75); box-shadow: 0px 9px 114px 2px rgba(0,0,0,0.75); border-radius: 5px; width: 100%; max-height: 158px; background-color: white;border: 1px solid #d48080;">
            
        </div>
    </div>`;

    if ($('#wf_error_list_area')) {
        $('#wf_error_list_area').remove();
    }

    $('body').append(html);
}

function appendErrorTableToErrorArea() {

    let html = `<div class="panel panel-danger">
                <div class="panel-heading" style="font-weight:bold;cursor: all-scroll;"><span class="badge" id="errorCount" style="background-color:#e00808;">0</span> Error(s) found 
                <button class="btn pull-right btn-danger btn-xs closeErrors" style="cursor:auto;"><i class="fa fa-times"></i></button>
                </div>
                <div class="panel-body" style="padding:0px 5px;overflow-y: auto;max-height: 113px;">
                    <table id="wf_error_table" class="table table-striped table-sm"></table>
                </div>
            </div>`;

    $('#wf_error_list_area .errors').append(html);

    $('#wf_error_list_area .errors .closeErrors').on('click', function () {
        $('#wf_error_list_area').remove();
    });


}



function displayWFerrorMessage(rappidObj, passErrorArr, passErrorFmArr, options = {}) {

    let defaultVal = {
        callingFrom: 'createFLChart',
        removeCloseFromHalo: false,
        activeInspector: true

    };

    let actual = $.extend({}, defaultVal, options || {});

    if (actual.callingFrom == 'DelExecution') {
        actual.removeCloseFromHalo = true;
        actual.activeInspector = false;

    }

    let errorCount = passErrorArr.length;
    let errorCountFm = passErrorFmArr.length;
    if (errorCount || errorCountFm) {


        appendErrorMainAreaToDOM();
        appendErrorTableToErrorArea();

        $('#wf_error_list_area #errorCount').text(errorCount + errorCountFm);

        let tableData = `<thead>
            <tr>
                <th>#</th>
                <th>Error</th>
                <th>Action</th>

            </tr>
        </thead><tbody>`;
        let rCount = 1;
        for (let i in passErrorArr) {

            tableData += `<tr>
                    <td><strong>${rCount++}</strong></td>
                    <td>${passErrorArr[i]['errorDesc']}</td>
                    <td><button class="btn btn-info btn-xs wf_error_fixit" data-cellid="${passErrorArr[i]['elId']}">${passErrorArr[i]['actionText']}</button></td>
                    </tr>`;
            //<td><button class="btn btn-info btn-xs wf_error_fixit" data-cellid="${passErrorArr[i]['elId']}">${passErrorArr[i]['actionText']}</button></td>

        }
        for (let j in passErrorFmArr) {

            tableData += `<tr>
                    <td><strong>${rCount++}</strong></td>
                    <td>${passErrorFmArr[j]['errorDesc']}</td>
                    <td></td>
                    </tr>`;
            //<td><button class="btn btn-info btn-xs wf_error_fixit" data-cellid="${passErrorArr[i]['elId']}">${passErrorArr[i]['actionText']}</button></td>

        }
        tableData += `</tbody>`;


        $('#wf_error_table').empty().append(tableData);

        $('#wf_error_list_area').slideDown();

        $("#wf_error_list_area").draggable();

        $('#wf_error_list_area .wf_error_fixit').off('click').on('click', function () { // BIND CLICK EVENT TO FIXIT BTN FOR FIX THIS ERROR
            let that = this;
            let cellId = $(that).attr('data-cellid');

            let getCell = rappidObj.graph.getCell(cellId);

            let getElementType = getCell.get('type');
            if (getElementType == manualStep || getElementType == automaticStep) {
                let newHalo = new joint.ui.Halo({
                    clearAll: true,
                    cellView: rappidObj.paper.findViewByModel(getCell),
                    handles: App.config.halo.handles
                }).render();

                if (actual.removeCloseFromHalo) {
                    newHalo.removeHandle('remove');
                }
            }
            if (actual.activeInspector) {
                rappidObj.createInspector(getCell);
            }

            rappidObj.paperScroller.scroll(getCell.get('position').x + 150, getCell.get('position').y + 100); //SCROLL TO THAT ELEMENT

            if ($(that).hasClass('btn-info')) {
                $(that).removeClass('btn-info').addClass('btn-default');
            }


        });


    }
    //} else {

    //    appendErrorMainAreaToDOM();
    //    let successHtml = `<div class="alert alert-success" role="alert"><strong>Well Done !!</strong> Ready to save.

    //                        <button class="btn pull-right btn-danger btn-xs closeErrors" style="cursor:auto;"><i class="fa fa-times"></i></button>
    //                        </div>`;

    //    $('#wf_error_list_area .errors').append(successHtml);
    //    $('#wf_error_list_area').slideDown();

    //    $('#wf_error_list_area .errors .closeErrors').on('click', function () {
    //        $('#wf_error_list_area').remove();
    //    });

    //}
}


function validateAndAdjustFlowchart(rappidObj) {

    let callAutoAlignmentFlag = true;

    if ($('#autoAlignCheckBox').attr('checked') == 'checked') {
        callAutoAlignmentFlag = true;
    } else {
        callAutoAlignmentFlag = false;
    }


    // REMOVE HIGHLIGHT FROM ALL STEPS
    unhighlightAllCells(rappidObj);

    //Call auto resize all elements
    for (let i in rappidObj.graph.attributes.cells.models) {
        let cell = rappidObj.graph.attributes.cells.models[i];
        autoResize(rappidObj, cell);
    }


    // Call auto alignment
    if (callAutoAlignmentFlag) {
        rappidObj.layoutDirectedGraph();
    }

    // Call for validation workflow
    let validateWF = validateWorkflow(rappidObj);
    let validateFm = validateForm();

    //console.log('validateWF->', validateWF);
    //if (Object.keys(arrOfJsons).length == 1) {
    //    if (!validateWF.status) { global_validationOfJsonsArray.NoviceValidation = false; }
    //    else { global_validationOfJsonsArray.NoviceValidation = true; }

    //}
    //else if (Object.keys(arrOfJsons).length > 1) {

    //    if ($("#assessed").is(':checked')) {
    //        if (!validateWF.status) {
    //            global_validationOfJsonsArray.NoviceValidation = false;
    //        } else {
    //            global_validationOfJsonsArray.NoviceValidation = true;
    //        }
    //    }

    //    if ($("#expert").is(':checked')) {
    //        if (!validateWF.status) {
    //            global_validationOfJsonsArray.ExpertValidation = false;
    //        } else {
    //            global_validationOfJsonsArray.ExpertValidation = true;
    //        }
    //    }
    if (validateFm.status == false) {
        if (window.location.href.indexOf("FlowChartWorkOrderAdd") != -1) {
            $('#mainDivToOverlay').addClass('overlayWfDetailDiv');
            $('#accordionOne').removeClass('panel-collapse collapse');
            $('#accordionOne').addClass('panel-collapse in');
            $('#accordionOne')[0].style = 'height:auto;';
        }
    }
    else {
        if (window.location.href.indexOf("FlowChartWorkOrderAdd") != -1) {
            $('#mainDivToOverlay').removeClass('overlayWfDetailDiv');
            $('#accordionOne').removeClass('panel-collapse in');
            $('#accordionOne').addClass('panel-collapse collapse');
            $('#accordionOne')[0].style = 'height:0px;';
        }
    }
    //}
    displayWFerrorMessage(rappidObj, validateWF.errors, validateFm.errors);

    //Active save button
    //if ($('#saveJSON')) {
    //    $('#saveJSON').attr({ 'disabled': false }).text('Save Work Flow');

    //}
    let validateStatus = { formValidation: validateFm, WfValidation: validateWF };
    return validateStatus;


}