/*! Rappid v2.3.3 - HTML5 Diagramming Framework

Copyright (c) 2015 client IO

 2018-08-03 


This Source Code Form is subject to the terms of the Rappid License
, v. 2.0. If a copy of the Rappid License was not distributed with this
file, You can obtain one at http://jointjs.com/license/rappid_v2.txt
 or from the Rappid archive as was distributed by client IO. See the LICENSE file.*/

//$(document).on('click', '.addURLButton', function () {
//    //alert('error');
//    //$('#toolByProjAnchorwp').hide();
//    //getWfInstructionData();
//});
var stepURLButtonText = 'Add URL';


var App = window.App || {};

(function (_, joint) {
    var arrayJson = new Object();

    var urlParams = new URLSearchParams(window.location.search);
    var woID = urlParams.get("woID");
    var subActID = urlParams.get("subActID");
    var projid = urlParams.get("prjID");
    var mode = urlParams.get("mode");
    var vnum = urlParams.get("version");
    var wfid = urlParams.get("wfid");
    let wfName = urlParams.get('wfname');
    let flowChartType = urlParams.get('flowchartType');
    //let experienced = (urlParams.get("experienced")) ? urlParams.get("experienced") : '0';
    let proficiencyLevel = urlParams.get("proficiencyLevel");
    let proficiencyId = urlParams.get("proficiencyId");
    let woStatus = urlParams.get("status");
    let flowChartDefID = '';
    let expertViewNeeded = false;
    let isAutoSensePopupVisible = false;
    if (woID == null) {
        woID = 0;
    }
    'use strict';

    App.MainView = joint.mvc.View.extend({

        className: 'app',

        events: {
            'focus input[type="range"]': 'removeTargetFocus',
            'mousedown': 'removeFocus',
            'touchstart': 'removeFocus'
        },

        removeTargetFocus: function (evt) {
            evt.target.blur();
        },

        removeFocus: function () {
            document.activeElement.blur();
            window.getSelection().removeAllRanges();
        },

        init: function () {
            var json2;
            var getTools = App.config.toolbar.tools;
            var tools = [];
            if (mode == "view") {
                tools = [];
                this.initializePaper(mode);
                this.initializeSelection();
                this.initializeNavigator();

                if (!(window.location.href.indexOf("FlowChartWorkOrderView") > -1)) {
                    //create flowchart
                    tools = [getTools[6], getTools[7], getTools[8], getTools[9], getTools[10]];
                    json2 = this.getWFWithWoID(woID, proficiencyId);
                    this.graph.fromJSON(JSON.parse(json2));
                    pwIsf.removeLayer();
                }
                else {
                    tools = [getTools[6], getTools[7], getTools[8], getTools[9], getTools[10]];
                    this.initializeToolsAndInspector();
                }
                this.initializeToolbar(tools);
                this.initializeKeyboardShortcuts();
                this.initializeTooltips();
                $(".inspector-container").hide();
                $(".paper-container").css("width", "100%");
                //$(".paper-container").css("left", "0");
                $(".paper-container").css("right", "0");
                //this.layoutDirectedGraph();
            }

            else if (mode == "edit") {
                tools = [];
                global_toggleFlag = true;
                this.initializePaper(mode);
                //this.initializeStencil();
                this.initializeSelection();
                this.initializeToolsAndInspector();
                this.initializeNavigator();
                tools = [getTools[0], getTools[1], getTools[2], getTools[5], getTools[6], getTools[7], getTools[8], getTools[9], getTools[10], getTools[13]];
                this.initializeToolbar(tools);
                this.initializeKeyboardShortcuts();
                this.initializeTooltips();
                $(".inspector-container")

                let wfid = urlParams.get("wfid");
                //create flowchart                
                json2 = this.getMasterWF(woID, projid, subActID, wfid);
                this.graph.fromJSON(JSON.parse(json2));
                // setTimeout(pwIsf.removeLayer(), 1000);
                window.parent.$("#iframe_" + woID).load(function () {
                    pwIsf.removeLayer();
                });
                $(".inspector-container").hide();
                $(".paper-container").css("width", "100%");
                $(".paper-container").css("left", "0");
                $(".paper-container").css("right", "0");
                //this.layoutDirectedGraph();
            }
            else if (mode == "execute") {
                this.initializePaper(mode);
                this.initializeSelection();
                tools = [getTools[4], getTools[6], getTools[7], getTools[8], getTools[9], getTools[10]];
                this.initializeToolbar(tools);
                this.initializeKeyboardShortcuts();
                this.initializeTooltips();
                this.initializeNavigator();
                $(".inspector-container").hide();

                //create flowchart
                json2 = this.getWFWithWoID(woID, proficiencyId);
            
                arrayJson = JSON.parse(json2);
                this.addAutoSenseModalPopup();

                let isWOPartial;
                let isWOFresh = window.parent.$('#viewBtn_' + woID).data('details').isWOFresh;
                //Check if WO is partial
                if (woStatus.toLowerCase() == C_WO_ASSIGNED_STATUS.toLowerCase() ||
                    woStatus.toLowerCase() == C_WO_REOPENED_STATUS.toLowerCase() ||
                    (woStatus.toLowerCase() == C_WO_ONHOLD_STATUS.toLowerCase() && isWOFresh)) {
                    isWOPartial = false;
                }
                else if (woStatus.toLowerCase() == C_WO_INPROGRESS_STATUS.toLowerCase()) {
                    isWOPartial = true;
                }

                let userProficiency = window.parent.$('#viewBtn_' + woID).data('details').proficiencyLevel;
                let isAlreadyOnAssessed = urlParams.get('isAlreadyOnAssessed') != null ? JSON.parse(urlParams.get('isAlreadyOnAssessed')) : false;

                //Conditions for Autosense pop up visibility
                let experiencedConditionForAutosensePopUp = userProficiency == 2 && (isWorkOrderAutoSenseEnabled == null || (isWorkOrderAutoSenseEnabled == false && isAlreadyOnAssessed == false));
                let assessedConditionForAutosensePopUp = userProficiency == 1 && isWorkOrderAutoSenseEnabled == null;

                if (this.validateAutoSenseEnabled() === true && proficiencyLevel == 1 ) {
                    if (experiencedConditionForAutosensePopUp || assessedConditionForAutosensePopUp) {
                        isAutoSensePopupVisible = true;
                        $('#autoSensePopupId').modal('show');
                    }                  
                }

                if (isAutoSensePopupVisible === false && this.validateAutoSenseEnabled() === true && proficiencyLevel == 1) {

                    if (isWorkOrderAutoSenseEnabled === true) {
                        addAutoSenseBedgeOnWF(C_AS_BEDGE_COLOR_ARRAY_GO[1]);
                        this.disableAssessedExperiencedSwitchOfParent();
                    } else if (isWorkOrderAutoSenseEnabled === false) {
                        addAutoSenseBedgeOnWF(C_AS_BEDGE_COLOR_ARRAY_GO[0]);
                    }
                }
                this.graph.fromJSON(arrayJson);
                var root = this.graph.getSources();
                if (root[0].attributes.type == 'ericsson.StartStep') { root = root[0] } else { root = root[1]; }
                let that = this;
                let disabledStepStatus = proficiencyLevel == 2 ? JSON.parse(window.parent.$('#woButtons_' + woID + ' .disabledStepObj').val()).status : null;
                let inprogressTaskObj = window.parent.$('#hiddenVal_' + woID)
                let disabledStarted = inprogressTaskObj.length != 0 ? (inprogressTaskObj.data('exectype') == C_EXECUTION_TYPE_MANUALDISABLED ? true : false) : false;
                let isDisabledStepCompleted = disabledStepStatus == null || disabledStepStatus == C_BOOKING_STATUS_STARTED ? false : true;
                var firstStep = proficiencyLevel == 2 && isDisabledStepCompleted ? getFirstEnabledStep(that, root) : this.graph.getNeighbors(root, { outbound: true })[0];

                if (firstStep.attributes.type == "ericsson.Decision") {
                    let link = [];
                    outboundlink = this.graph.getConnectedLinks(firstStep, { outbound: true });
                    for (let m in outboundlink) {
                        //  link  =  this.graph.toJSON().cells.filter(function(el){return el.id == outboundlink[m].get('id')});
                        //  link[0].set('showIcons',true);  
                        outboundlink[m].set('showIcons', true);
                    }
                }
                else if (proficiencyLevel == 2 && isDisabledStepCompleted && firstStep.attributes.type == C_STEP_TYPE_END) {
                    let makeCompleteIcon = { src: '\uf058', fill: 'white', tooltip: 'Mark as Completed', className: 'bottom-right', event: 'markAsCompleted', name: '' };
                    firstStep.attributes.icons = [makeCompleteIcon];
                    firstStep.attributes.showIcons = true;
                }
                else { firstStep.set('showIcons', true); }
                var currentRunningPosition = firstStep.attributes.position;
                this.paperScroller.scroll(currentRunningPosition.x, currentRunningPosition.y);
                window.parent.$("#iframe_" + woID).load(function () {
                    pwIsf.removeLayer();
                });

                $(".paper-container").css("width", "100%");
                $(".paper-container").css("left", "0");
                $(".paper-container").css("right", "0");
                //  this.layoutDirectedGraph();
                
            }
            else {
                this.initializePaper(mode);
                this.initializeNavigator();
                if ((window.location.href.indexOf("FlowChartApprovalView") > -1)) {
                    tools = [getTools[6], getTools[7], getTools[8], getTools[10], getTools[9]]

                }
                else {

                    if ((window.location.href.indexOf("FlowChartWorkOrderView") > -1)) {
                        tools = [getTools[6], getTools[7], getTools[8], getTools[9], getTools[10]];
                        $(".stencil-container").hide();
                        $(".paper-container").css("width", "100%");
                        $(".paper-container").css("left", "0");
                        $(".paper-container").css("right", "0");
                    }
                    else if ((window.location.href.indexOf("FlowChartViewEP") > -1)) {
                        tools = [getTools[6], getTools[7], getTools[10], getTools[13]];
                        $(".paper-container").css("width", "100%");
                        $(".paper-container").css("left", "0");
                        $(".paper-container").css("right", "0");
                    }
                    else if ((window.location.href.indexOf("FlowChartViewMaster") > -1)) {
                        tools = [getTools[6], getTools[7], getTools[10], getTools[13]];
                        $(".paper-container").css("width", "100%");
                        $(".paper-container").css("left", "0");
                        $(".paper-container").css("right", "0");
                    }
                    else if ((window.location.href.indexOf("FlowChartBOTEdit") > -1)) {
                        tools = [getTools[6], getTools[8], getTools[10], getTools[9], getTools[12]];
                        $(".stencil-container").show();
                        $('#toggleEditMode').hide();
                        this.initializeStencil();
                        this.initializeToolsAndInspector();
                    }
                    else {
                        tools = [getTools[6], getTools[8], getTools[10], getTools[9], getTools[12]];
                        $('#toggleEditMode').hide();
                        $(".stencil-container").show();
                        this.initializeStencil();
                        this.initializeToolsAndInspector();
                    }
                }
                this.initializeSelection();
                this.initializeToolbar(tools);
                this.initializeKeyboardShortcuts();
                this.initializeTooltips();
            }
            //this.initializePaper();
            //this.initializeStencil();
            //this.initializeSelection();
            //this.initializeToolsAndInspector();
            //this.initializeNavigator();
            //this.initializeToolbar();
            //this.initializeKeyboardShortcuts();
            //this.initializeTooltips();
        },


        // Create a graph, paper and wrap the paper in a PaperScroller.
        initializePaper: function () {

            var graph = this.graph = new joint.dia.Graph;

            graph.on('add', function (cell, collection, opt) {
                if (opt.stencil) this.createInspector(cell);
                //console.log(arguments);
            }, this);

            this.commandManager = new joint.dia.CommandManager({ graph: graph });

            var paper = this.paper = new joint.dia.Paper({
                width: 1000,
                height: 1000,
                gridSize: 10,
                drawGrid: true,
                model: graph,
                defaultLink: new joint.shapes.app.Link,
                defaultConnectionPoint: joint.shapes.app.Link.connectionPoint,
                //interactive: { linkMove: false, labelMove: false }
            });

            if ((mode == "view") || (window.location.href.indexOf("FlowChartApprovalView") > -1) || (mode == "execute") || (window.location.href.indexOf("FlowChartViewEP") > -1)) { paper.setInteractivity(false); }
            else { paper.setInteractivity({ linkMove: false, labelMove: false }); }

            paper.on('blank:mousewheel', _.partial(this.onMousewheel, null), this);
            paper.on('cell:mousewheel', this.onMousewheel, this);

            //this.snaplines = new joint.ui.Snaplines({ paper: paper });

            var paperScroller = this.paperScroller = new joint.ui.PaperScroller({
                paper: paper,
                autoResizePaper: true,
                cursor: 'grab',
                //contentOptions: { maxWidth: 3000, maxHeight: 3000 }

            });

            this.$('.paper-container').append(paperScroller.el);
            paperScroller.render().center();

            //create icons in boxes
            paper.options.elementView = function (el) {
                if ((window.location.href.indexOf("FlowChartExecution") > -1) || (window.location.href.indexOf("FlowChartWorkOrderAdd") > -1)) {
                    if (el instanceof joint.shapes.ericsson.Manual || el instanceof joint.shapes.ericsson.Automatic || el instanceof joint.shapes.erd.Relationship || el instanceof joint.shapes.ericsson.EndStep) {
                        // if (el.get('showIcons'))
                        // {
                        return iconView;
                        // }
                    }
                    else if (el instanceof joint.shapes.ericsson.Decision) {
                        // outboundLinks = this.model.getConnectedLinks(el,{outbound:true});
                        // return decisionIconView;
                        //  return iconView;
                    }
                    return joint.dia.elementView;
                }
            }

            paper.options.linkView = function (el) {
                if ((window.location.href.indexOf("FlowChartExecution") > -1)) {
                    if (el.attributes.labels.length != 0) {
                        return decisionIconView
                    }
                    return joint.dia.linkView;
                }
            }


            //mark flowchart as completed

            paper.on('markAsCompleted', function (elementView, evt) {
                markAsCompleted(elementView);
            });

            //download bot nest file 
            paper.on('downloadBotNestFileClick', function (elementView, evt) {
                evt.stopPropagation();
                // let botNestId = elementView.model.attributes.externalRefId;
                let bookingId = '';
                //(elementView.model.attributes.isfRefId != undefined)? bookingId = elementView.model.attributes.isfRefId.toString() : bookingId = elementView.model.attributes.queueID.toString();
                bookingId = elementView.model.attributes.isfRefId.toString();
                let isfBotId = rpaID.toString();
                let botDetails = new FormData();

                botDetails.bookingId = bookingId;
                botDetails.isfBotId = isfBotId;

                pwIsf.addLayer({ text: 'Please wait ...' });
                $.isf.ajax({
                    type: "POST",
                    async: false,
                    //  enctype: 'multipart/form-data',
                    processData: false,
                    contentType: 'application/json',
                    cache: false,
                    timeout: 600000,
                    url: botNestExternal_URL + "downloadReport",
                    data: JSON.stringify(botDetails),
                    success: function (data) {

                        if (data != "failure") {
                            downloadFiles(isfBotId, bookingId);
                        }
                        else {
                            pwIsf.alert({ msg: "Download File Fail!", type: "error" });
                        }
                    },
                    error: function () {
                        pwIsf.alert({ msg: "error", type: "error" });
                    },
                    complete: function () {
                        pwIsf.removeLayer();
                    }
                });
            });

            //download work instruction document
            paper.on('downloadworkinstructionclick', function (elementView, evt) {
                evt.stopPropagation();

                let wiId = "";
                if (elementView.model.attributes.workInstruction) {
                    wiId =    elementView.model.attributes.workInstruction.split('@')[3];
                }
               
                let defidForInst = $('#defID').val();//JSON.parse(elementView.model.attributes.icons[4].details).subActivityDefID;
                let stepID = elementView.model.attributes.id;

                let tempD = [];
                let docName = ""
                if (elementView.model.attributes.attrs.workInstruction) {
                    tempD = elementView.model.attributes.attrs.workInstruction.WIName.split('_');
                    docName  = tempD[1] + '_' + tempD[2];
                }

                

                let pagex = evt.pageX;
                let pagey = evt.pageY;

                 
             //   $('#stepLevelInst').css('left', event.clientX);      // <<< use pageX and pageY
             //   $('#stepLevelInst').css('top', event.clientY);
              //  $('#stepLevelInst').modal('show');
              
                let JsonStringArr = [];

                var wfDataObj = new Object();
                wfDataObj.flowChartDefID = defidForInst;
                wfDataObj.stepID = stepID;
                JsonStringArr.push(wfDataObj);             

                if (JsonStringArr) {
                    $.isf.ajax({
                        url: service_java_URL + "activityMaster/getInstructionURLList",
                        context: this,
                        crossdomain: true,
                        processData: true,
                        contentType: 'application/json',
                        type: 'POST',
                        data: JSON.stringify(JsonStringArr),
                        xhrFields: {
                            withCredentials: false
                        },
                        success: AjaxSucceeded,
                        error: AjaxFailed
                    });
                    function AjaxSucceeded(data, textStatus) {
                        data = data.data;
                      //  $("#step_instruction").html('');
                        $('#step_instructions').html('');
                        let html = ''
                        if (data.length == 0) {
                            html = `No data`

                            $("#step_instruction").append(html);
                        }

                      else {
                           // $("#step_instruction").html('');
                            $('#step_instructions').html('');
                            for (x in data) {
                                if (data[x].instructionType == 'STEP_INSTRUCTION') {
                                    if ((data[x].stepID != "" || data[x].stepID != null) && data[x].stepID == stepID) {

                                        if (data[x].reference) {
                                            html = `<li><a class="instructionP" title="${escapeHtml(data[x].urlName)} (${escapeHtml(data[x].reference)})" style="color:blue;" target="_blank" href='${data[x].urlLink}'>${escapeHtml(data[x].urlName)} (${data[x].reference}) </a></li>`                                          
                                        }
                                        else {
                                            html = `<li><a class="instructionP" style="color:blue;" target="_blank" title="${escapeHtml(data[x].urlName)}" href='${data[x].urlLink}'>${escapeHtml(data[x].urlName)} </a></li>`;                                         
                                        }
                                       
                                       $("#step_instructions").append(html);    
                                        $("#step_instruction").append(html);                                       

                                    }
                                }
                            }
                           // window.parent.$('#wf_instruction').append(htmlInput);
                           // $('.toolbar-container').append($('#stepInstList_' + stepID));
                        }                        
                        if (wiId) {
                            let url = `${service_java_URL}activityMaster/downloadWorkInstructionFile?wIID=${wiId}`;
                            //let url = `http://100.96.35.120:8080/isf-rest-server-java/activityMaster/downloadWorkInstructionFile?wIID=${wiid}`;
                            //clToText(url);
                            let fileDownloadUrl;

                            fileDownloadUrl = UiRootDir + "/data/GetFileFromApi?apiUrl=" + url;

                            if (docName && docName != 'undefined_undefined') {
                                var wifilehtml = `<li><a class="instructionP" title="Engineering WI (${docName})" style="color:blue;" target="_blank" href='${fileDownloadUrl}'>Engineering WI (${docName})</a></li>`
                            }
                            else {
                                var wifilehtml = `<li><a class="instructionP"  style="color:blue;" target="_blank" href='${fileDownloadUrl}'>Engineering WI </a></li>`
                            }

                            $("#step_instructions").append(wifilehtml);
                        }
                        $('#step_instructions').toggle();
                        $("#step_instructions").css('top', pagey-20); 
                        $("#step_instructions").css('left', pagex); 

                      //  $("#step_instruction").append(wifilehtml);
                    }
                    function AjaxFailed(xhr, status, statusText) {
                        pwIsf.alert({ msg: 'Error fetching data', type: 'warning' });
                    }
                }
            });

            //downloadLink
            paper.on('downloadbuttonclick', function (elementView, evt) {
                evt.stopPropagation();
                if ($("#AutodownloadLink")) {
                    $("#AutodownloadLink").remove();
                }
                let outputFileDownloadUrl = elementView.model.attributes.attrs.task.outputLink;

                window.open(outputFileDownloadUrl, '_blank');
            });

            //mark step as SAD.
            paper.on('makeAsSad', function (elementView, evt) {
                evt.stopPropagation();
                let eleAttr = elementView.model.attributes;
                let stepID = eleAttr.id;
                let project_ID = urlParams.get("prjID");
                let subActivityDefID = $('#defID').val();
                let wo_ID = urlParams.get("woID");
                let sadCount = eleAttr.sadCount;
                let stepName = eleAttr.name;
                let wfName = $('#wfName').val()
                let currExecutionType = '';

                (eleAttr.type == 'ericsson.Automatic') ? currExecutionType = eleAttr.execType : currExecutionType = eleAttr.executionType;

                if (sadCount == 0) {
                    sadCount = 1;
                    elementView.model.set('sadCount', sadCount);
                }
                else {
                    sadCount = 0;
                    elementView.model.set('sadCount', sadCount);
                }

                let makeStepSadObject = new Object()
                makeStepSadObject.stepID = stepID;
                makeStepSadObject.stepName = stepName;
                makeStepSadObject.projectID = project_ID;
                makeStepSadObject.subActivityDefID = subActivityDefID;
                makeStepSadObject.woID = wo_ID;
                makeStepSadObject.wFID = urlParams.get('wfid');
                makeStepSadObject.wfName = wfName;
                makeStepSadObject.sadCount = sadCount;
                makeStepSadObject.taskName = eleAttr.attrs.task.taskName;
                makeStepSadObject.taskID = eleAttr.attrs.task.taskID;
                makeStepSadObject.shape = eleAttr.type;
                makeStepSadObject.execType = currExecutionType;
                makeStepSadObject.bookingID = eleAttr.attrs.task.bookingID;
                makeStepSadObject.status = eleAttr.attrs.task.status;
                makeStepSadObject.currentCell = elementView.model.attributes;
                // makeStepSadObject.currentJson = app.graph.toJSON().cells.filter(function (el) { return el.type == 'ericsson.Manual' || el.type == 'ericsson.Automatic' || el.type == 'ericsson.Decision' || el.type == 'ericsson.StartStep' || el.type == 'ericsson.EndStep' });
                makeStepSadObject.currentJson = app.graph.toJSON().cells;
                makeStepSadObject.iconsOnNextStep = 'true';
                makeStepSadObject.flowChartType = urlParams.get('flowchartType');
                makeStepSadObject.startRule = eleAttr.startRule;
                makeStepSadObject.stopRule = eleAttr.stopRule;
                makeStepSadObject.viewMode = eleAttr.viewMode;
                makeStepSadFunction(makeStepSadObject);

                makeStepSadFunction(makeStepSadObject);

            });

            paper.on('feedbackButtonClick', function (elementView, evt) {

                let eleAttr = elementView.model.attributes;
                let stepName = eleAttr.name;
                let stepId = eleAttr.id;
                let project_ID = urlParams.get("prjID");
                let subActivityDefID = $('#defID').val();
                let wo_ID = urlParams.get("woID");
                let wfID = urlParams.get("wfid");
                let versionNo = $('#flowchartVersion').val();
                let wfname = $('#wfName').val();

                let stepFeedbackObj = new Object()
                stepFeedbackObj.stepID = stepId;
                stepFeedbackObj.projectID = project_ID;
                stepFeedbackObj.subActivityDefID = subActivityDefID;
                stepFeedbackObj.woid = wo_ID;
                stepFeedbackObj.wfID = wfID;
                stepFeedbackObj.stepName = stepName;
                stepFeedbackObj.versionNo = versionNo;
                stepFeedbackObj.wfName = wfname;

                createStepLevelFeedbackModal(stepFeedbackObj);


            })
            //AutoSenseIconsClick

            paper.on('manualAutoSenseClick', function (elementView, evt) {
                let eleAttr = elementView.model.attributes;
                let stepName = eleAttr.name;
                let stepId = eleAttr.id;
                pwIsf.addLayer({ text: 'Please wait ...' });
                $.isf.ajax({
                    type: "GET",
              
                    contentType: 'application/json',
    
                    url: service_java_URL + "autoSense/getRuleDescriptionForStep?stepID=" + stepId + '&flowChartDefID=' + $("#defID").val(),
                    success: function (data) {

                        //$('#lblNoRuleText').text('');
                        //if (data.isValidationFailed == false ) {
                            
                        //    var ruleHtml = '';

                        //    if (data.formMessageCount > 0) {
                        //        $('#lblNoRuleText').text(data.formMessages["0"]);
                        //        $('#divStartRule').css({ 'display': 'none' });
                        //        $('#divNoRule').css({ 'display': 'block' });
                        //    }
                        //    if (data.responseData != null) {
                             
                        //        $('#tblRuleDescription').html('');
                        //        $('#divStartRule').css({ 'display': 'block' });
                        //        let columns = addAllColumnHeaders(data.responseData, '#tblRuleDescription');
   
                        //        for (var i = 0; i < data.responseData.length; i++) {
                        //            var row = $('<tr/>');
                        //            for (var colIndex = 0; colIndex < columns.length; colIndex++) {
                        //                var cellValue = data.responseData[i][columns[colIndex]];
                        //                if (cellValue == null) cellValue = "";
                        //                row.append($('<td/>').html(cellValue));
                        //            }
                        //            $('#tblRuleDescription').append(row);
                        //        }
                        //        //$.each(data.responseData, function (i, item) {
                        //        //    ruleHtml = ruleHtml + '<li>' + '<b>'+item.ruleType+ '</b> '+ ': ' + item.ruleDescription + '</li> '

                        //        //});
                        //        $('#ruleList').empty();
                        //        $('#ruleList').append(ruleHtml);
                        //        $('#divNoRule').css({ 'display': 'none' });
                        //    }
                           
                          

                        //} else {

                        //    $('#divStartRule').css({ 'display': 'none' });
                        //    $('#divNoRule').css({ 'display': 'block' });
                        //    $('#lblNoRuleText').text(data.formErrors["0"]);
                        //}
                        ////$('#modalAutoSenseStepRule').css({ 'display':'block'})
                        //$('#modalAutoSenseStepRule').modal('show');
                      
                    },
                    error: function () {
                        pwIsf.alert({ msg: "error", type: "error" });
                        pwIsf.removeLayer();
                    },
                    complete: function (xhr, status) {

                        if (status == 'success') {
                            if (xhr.responseJSON.isValidationFailed) {
                                responseHandler(xhr.responseJSON);
                            }
                            else {
                                if (xhr.responseJSON.responseData != null && xhr.responseJSON.responseData != 'null') {

                                    let getData = xhr.responseJSON.responseData;
                                    configureStepRulesDataTable(getData); // Add Datatable configuration to the table
                                    $('#stepRulesDiv').show();
                                    $('#stepRulesTable > thead > tr > th:nth-child(1)').css('width', '15%');
                                    $('#stepRulesTable > thead > tr > th:nth-child(2)').css('width', '25%');
                                    $('#stepRulesTable > thead > tr > th:nth-child(3)').css('width', '60%');
                                    
                                }
                                else {
                                    $('#stepRulesDiv').hide();
                                }
                                $('#stepRuleTitle').text(truncate(stepName, 238));
                                $('#stepRuleTitle').attr('data-original-title', stepName);
                                $('#modalAutoSenseStepRule').modal('show');
                            }
                            pwIsf.removeLayer();
                        }

                    }
                });

                let createStepRulesTable = (getData) => {
                    let thead = `<thead>
                    <tr>
                        <th>Rule Type</th>
                        <th>Rule Name</th>
                        <th>Rule Description</th>
                    </tr>
                </thead>`;

                    let tbody = ``;
                    let ruleNameArr = [];
                    let ruleDescArr = [];
                    $(getData).each(function (i, d) {

                        ruleNameArr.push(d.ruleName);
                        ruleDescArr.push(d.ruleDescription);

                        tbody += `<tr>
                        <td>${d.ruleType}</td>
                        <td><span class="aqua-tooltip ruleNameTitle" data-toggle="tooltip">${truncate(d.ruleName, 410)}</span></td>
                        <td><span class="aqua-tooltip ruleDescTitle" data-toggle="tooltip">${truncate(d.ruleDescription, 1250)}</span></td>
                    </tr>`;
                    });

                    $('#stepRulesTable').html(thead + '<tbody>' + tbody + '</tbody>'/* + tfoot*/);

                    $('.ruleNameTitle').each(function (i, ruleNameTitle) {
                        $(ruleNameTitle).attr('data-original-title', ruleNameArr[i]);
                    });

                    $('.ruleDescTitle').each(function (i, ruleDescTitle) {
                        $(ruleDescTitle).attr('data-original-title', ruleDescArr[i]);
                    });

                };

                let configureStepRulesDataTable = (getData) => {

                    if ($.fn.dataTable.isDataTable('#stepRulesTable')) {
                        $('#stepRulesTable').DataTable().destroy();
                        $('#stepRulesDiv').html('<table class="table table-bordered table-striped table-hover" id="stepRulesTable"></table >');
                    }

                    createStepRulesTable(getData); //Create the Table for Rules

                    let allRulesDatatable = $('#stepRulesTable').DataTable({
                        searching: false,
                        responsive: true,
                        retrieve: true,
                        destroy: true,
                        "lengthMenu": [10, 15, 20, 25],
                        'info': false,
                        lengthChange: false,
                        "paging": false,
                        "ordering": false,
                        'columnDefs': [
                            {
                                'searching': true,
                                'targets': [0, 1]
                            }
                        ],
                        dom: 'lBfrtip',
                       
                        initComplete: function () {

                        }
                    });

                    $('#stepRulesTable tfoot').insertAfter($('#stepRulesTable thead'));

                }


            });
            //startStepFlowChart
            paper.on('startbuttonclick', function (elementView, evt) {

                //stopDisabledStepFromFlowChart();

                let StartTaskInput;
                //pwIsf.addLayer({ text: 'Please wait ...' });

                // Stop any further actions with the element view e.g. dragging
                evt.stopPropagation();
                var eleattr = elementView.model.attributes;
                let botConfigJson = null; let botType = ""; let botNestId = '';
                let stepName = eleattr.name;
                let configDetails = {};
                let fileID = null;
                let booking_Id = eleattr.attrs.task.bookingID;
                botType = eleattr.botType;
                let outputUpload = eleattr.outputUpload;
                let isInputRequiredEleAttr = eleattr.attrs.tool.isInputRequired;
                //nextRunOnServer = eleattr.attrs.tool.isRunOnServer;
                //  $(evt.currentTarget).attr('pointer-event', 'none');
                if (eleattr.attrs.task.executionType == "Automatic") {
                    rpaID = eleattr.attrs.tool.RPAID;
                    //Bot Config Work flow
                    if (eleattr.botConfigJson != undefined && eleattr.botConfigJson.length != 0) {
                        pwIsf.removeLayer();
                        botConfigJson = JSON.stringify(eleattr.botConfigJson);
                        createFormFromFormBuilderJson('botConfigBody', botConfigJson);
                        botNestId = eleattr.botNestId;
                        configDetails = JSON.stringify({ flowChartType: flowChartType, isInputRequired: isInputRequiredEleAttr, isRunOnServer: eleattr.attrs.tool.isRunOnServer, configForBot: eleattr.configForBot, bookingID: eleattr.attrs.task.bookingID, bookingStatus: eleattr.attrs.task.bookingStatus, executionType: eleattr.attrs.task.executionType, taskID: eleattr.attrs.task.taskID, taskName: "", stepName: stepName, stepID: eleattr.id, version: versionNo, projID: urlParams.get("prjID"), subActivityDefID: subActivityFlowChartDefID, subActId: urlParams.get("subActID"), woID: urlParams.get("woID"), isCallingFromFlowchart: true, botType: botType, id: eleattr.id, botConfigJson: botConfigJson, fileID: fileID, afterLoadingFlowchart: afterLoadingFlowchart, outputUpload: eleattr.outputUpload });
                        configDetailsForLocal = JSON.stringify({ flowChartType: flowChartType, isInputRequired: isInputRequiredEleAttr, isRunOnServer: eleattr.attrs.tool.isRunOnServer, configForBot: eleattr.configForBot, bookingID: eleattr.attrs.task.bookingID, bookingStatus: eleattr.attrs.task.bookingStatus, executionType: eleattr.attrs.task.executionType, taskID: eleattr.attrs.task.taskID, taskName: "", stepName: stepName, stepID: eleattr.id, version: versionNo, projID: urlParams.get("prjID"), subActivityDefID: subActivityFlowChartDefID, subActId: urlParams.get("subActID"), woID: urlParams.get("woID"), isCallingFromFlowchart: true, botType: botType, id: eleattr.id, botConfigJson: botConfigJson, fileID: fileID, afterLoadingFlowchart: afterLoadingFlowchart, outputUpload: eleattr.outputUpload });
                        configDetailsForServer = JSON.stringify({ flowChartType: flowChartType, isInputRequired: isInputRequiredEleAttr, isRunOnServer: eleattr.attrs.tool.isRunOnServer, configForBot: eleattr.configForBot, bookingID: eleattr.attrs.task.bookingID, bookingStatus: eleattr.attrs.task.bookingStatus, executionType: eleattr.attrs.task.executionType, taskID: eleattr.attrs.task.taskID, taskName: "", stepName: stepName, stepID: eleattr.id, version: versionNo, projID: urlParams.get("prjID"), subActivityDefID: subActivityFlowChartDefID, subActId: urlParams.get("subActID"), woID: urlParams.get("woID"), isCallingFromFlowchart: true, botType: botType, id: eleattr.id, botConfigJson: botConfigJson, fileID: fileID, afterLoadingFlowchart: afterLoadingFlowchart, outputUpload: eleattr.outputUpload });
                        if (botType == "BotNest") {
                            $('#fileInputDiv').show();
                            $('#fileinputOnServerPlay').attr('required', true);
                            $('#expectedFileNames').show();
                            $('#currentCelldata').data('botType', botType);
                            $('#currentCelldata').data('botNestId', botNestId);
                        }
                        else {
                            $('#fileInputDiv').hide();
                            $('#fileinputOnServerPlay').attr('required', false);
                            $('#expectedFileNames').hide();
                            $('#currentCelldata').data('botType', botType);
                            $('#saveBotConfigBtn').prop('disabled', false);
                        }
                        $("#runOnServer").data('runOnServer', eleattr.attrs.tool.isRunOnServer);
                        $('#currentCelldata').data('details', configDetails);
                        $('#currentCelldata').data('botOldJson', botConfigJson);
                        $('#saveBotConfigBtn').prop('disbaled', false);
                        $('#runOnServerAutomatic').attr('data-details', configDetailsForServer);
                        $('#runOnLocalAutomatic').attr('data-details', configDetailsForLocal);
                        $('#botConfigModal').modal('show');

                    }
                    //Server Bots - Non Bot Config Work flow
                    else if (eleattr.attrs.tool.isRunOnServer == '1' && (eleattr.botConfigJson == undefined || eleattr.botConfigJson.length == 0)) {
                        pwIsf.removeLayer();
                        $('#modalSelectStartOption').modal('show');
                        botConfigJson = ''; //JSON.stringify(eleattr.botConfigJson);
                        botNestId = eleattr.botNestId;
                        let configDetailsForLocal = JSON.stringify({ flowChartType: flowChartType, isInputRequired: isInputRequiredEleAttr, configForBot: eleattr.configForBot, bookingID: eleattr.attrs.task.bookingID, bookingStatus: eleattr.attrs.task.bookingStatus, executionType: eleattr.attrs.task.executionType, taskID: eleattr.attrs.task.taskID, taskName: "", stepName: stepName, stepID: eleattr.id, version: versionNo, projID: urlParams.get("prjID"), subActivityDefID: subActivityFlowChartDefID, subActId: urlParams.get("subActID"), woID: urlParams.get("woID"), isCallingFromFlowchart: true, botType: botType, id: eleattr.id, botConfigJson: botConfigJson, fileID: fileID, afterLoadingFlowchart: afterLoadingFlowchart, outputUpload: eleattr.outputUpload, isRunOnServer: eleattr.attrs.tool.isRunOnServer });
                        let configDetailsForServer = JSON.stringify({ flowChartType: flowChartType, isInputRequired: isInputRequiredEleAttr, configForBot: eleattr.configForBot, bookingID: eleattr.attrs.task.bookingID, bookingStatus: eleattr.attrs.task.bookingStatus, executionType: eleattr.attrs.task.executionType, taskID: eleattr.attrs.task.taskID, taskName: "", stepName: stepName, stepID: eleattr.id, version: versionNo, projID: urlParams.get("prjID"), subActivityDefID: subActivityFlowChartDefID, subActId: urlParams.get("subActID"), woID: urlParams.get("woID"), isCallingFromFlowchart: true, botType: botType, id: eleattr.id, botConfigJson: botConfigJson, fileID: fileID, afterLoadingFlowchart: afterLoadingFlowchart,isRunOnServer: eleattr.attrs.tool.isRunOnServer });
                        $('#runOnServerAutomatic').attr('data-details', configDetailsForServer);
                        $('#runOnLocalAutomatic').attr('data-details', configDetailsForLocal);
                    }

                    else {
                        //stopDisabledStepFromFlowChart();
                        pwIsf.removeLayer();
                        StartTaskInput = PrepareStartFlowChartTaskInput(flowChartType, urlParams.get("woID"), urlParams.get("prjID"), eleattr.attrs.task.taskID, subActivityFlowChartDefID, eleattr.attrs.task.executionType, eleattr.id, eleattr.attrs.task.bookingStatus, booking_Id, botConfigJson, botType, fileID, eleattr.attrs.task.executionType, versionNo, stepName, outputUpload, eleattr.attrs.tool.isRunOnServer, isInputRequiredEleAttr, afterLoadingFlowchart);
                        startFlowChartTask(StartTaskInput);
                    }
                }
                //Manual Task Execution
                else {
                    pwIsf.removeLayer();
                    rpaID = '';
                    StartTaskInput = PrepareStartFlowChartTaskInput(flowChartType, urlParams.get("woID"), urlParams.get("prjID"), eleattr.attrs.task.taskID, subActivityFlowChartDefID, eleattr.attrs.task.executionType, eleattr.id, eleattr.attrs.task.bookingStatus, booking_Id, botConfigJson, botType, fileID, eleattr.attrs.task.executionType, versionNo, stepName, outputUpload, eleattr.attrs.tool.isRunOnServer, isInputRequiredEleAttr, afterLoadingFlowchart);
                    startFlowChartTask(StartTaskInput);
                }
            });

            //StopFlowchart
            paper.on('stopbuttonclick', function (elementView, evt) {
                evt.stopPropagation();
                let eleattr = elementView.model.attributes;           
                let bookingType = ''
                let bookingID = '';
                let bookingDetails = getBookingDetailsByBookingId(urlParams.get("woID"), eleattr.attrs.task.taskID, eleattr.id);
                bookingType = bookingDetails.type;
                bookingID = bookingDetails.bookingID;
                let reason = bookingDetails.reason;
                let reasonType = 'Automatic_Forced_stop';
                if (eleattr.attrs.task.executionType == "Automatic") {
                    if (eleattr.botType.toLowerCase().includes('nest')) {
                        let bookingid = eleattr.isfRefId.toString();
                        stopBotNestTask(bookingid, eleattr.id, urlParams.get("woID"), flowChartType, true, afterLoadingFlowchart);                                               
                                   
                    }
                    else {
                    
                        if (bookingType.toLowerCase() == "queued") {                      
                            GetReasonForDenail(reasonType, $('#commentStoppedWF'));
                            $('.stopAuto #woIdWF').val(urlParams.get('woID'));
                            $('.stopAuto #taskIdWF').val(eleattr.attrs.task.taskID);
                            $('.stopAuto #stepID').val(eleattr.id);
                            $('.stopAuto #subActDefId').val(subActivityFlowChartDefID);
                            $('.stopAuto #bookIdWF').val(bookingID);
                            $('.stopAuto #bookTypeWF').val(bookingType);
                            $(".stopAuto #calledFromSkip").val('false');
                            $(".stopAuto #flowChartType").val(flowChartType);
                            $('#modalStopWF').modal('show');
                        }
                        else {
                            if (reason != "Success") {
                                pwIsf.confirm({
                                    title: 'Stop BOT Execution?', type: 'warning', msg:
                                        'Bot is already executing. It is not recommended to stop it in between. Please wait!',
                                    'buttons': {
                                        'Force Stop': {
                                            'class': 'btn btn-danger',
                                            'action': function () {

                                                //Select Reason for Denail.
                                                GetReasonForDenail(reasonType, $('#commentStoppedWF'));

                                                $('.stopAuto #woIdWF').val(urlParams.get('woID'));
                                                $('.stopAuto #taskIdWF').val(eleattr.attrs.task.taskID);
                                                $('.stopAuto #stepID').val(eleattr.id);
                                                $('.stopAuto #subActDefId').val(subActivityFlowChartDefID);
                                                $('.stopAuto #bookIdWF').val(bookingID);
                                                $('.stopAuto #bookTypeWF').val('FORCE_STOP');
                                                $(".stopAuto #calledFromSkip").val('false');
                                                $(".stopAuto #flowChartType").val(flowChartType);
                                                $('#modalStopWF').modal('show');
                                            }
                                        },
                                        'Cancel': {
                                            'action': function () {
                                                $('#modalStopWF').modal('hide');
                                            }
                                        },

                                    }
                                });
                            }

                        }
                        //  stopFlowChartTask(urlParams.get("woID"), subActivityFlowChartDefID, eleattr.id, eleattr.attrs.task.taskID, true, eleattr.attrs.task.executionType, afterLoadingFlowchart);
                    }
                }
                else {

                    let stopDetails = new Object();
                    stopDetails.wOID = urlParams.get("woID");
                    stopDetails.subActivityFlowChartDefID = subActivityFlowChartDefID;
                    stopDetails.stepID = eleattr.id;
                    stopDetails.taskID = eleattr.attrs.task.taskID;
                    stopDetails.isCallingFromFlowchart = true;
                    stopDetails.executionType = eleattr.attrs.task.executionType;
                    InvokeStopFlowChartTask(flowChartType, urlParams.get("woID"), subActivityFlowChartDefID, eleattr.id, eleattr.attrs.task.taskID, true, eleattr.attrs.task.executionType, bookingID, 'Booking', '', afterLoadingFlowchart, '', false, false, false);
                }

            });

            paper.on('playNextStep', function (elementView, evt) {
                // Stop any further actions with the element view e.g. dragging
                evt.stopPropagation();
                playNextClicked = true;
                let proficiencyLevel = urlParams.get('proficiencyLevel');
                var eleattr = elementView.model.attributes;
                let bookingDetails = getBookingDetailsByBookingId(urlParams.get("woID"), eleattr.attrs.task.taskID, eleattr.id);
                let bookingID = bookingDetails.bookingID;
                let outboundlink = app.graph.getConnectedLinks(eleattr, { outbound: true });
                //Called to get the next step regardless of proficiency
                let nextStepObj = getNextStepForAssessed(outboundlink[0], arrAllShapes);
                let isNextStepDisabled = nextStepObj.executionType == C_EXECUTION_TYPE_MANUAL && !nextStepObj.viewMode.includes(C_PROFICIENCY_EXPERIENCED);
                if (nextStepObj.type == C_STEP_TYPE_DECISION || nextStepObj.type == C_STEP_TYPE_END) {
                 
                    InvokeStopFlowChartTask(flowChartType, urlParams.get("woID"), subActivityFlowChartDefID, eleattr.id, eleattr.attrs.task.taskID, true,eleattr.attrs.task.executionType, bookingID, 'Booking', '', afterLoadingFlowchart, '', false, false, false, nextStepObj.type);
                } 
                else {
                    nextStepDetails(eleattr.id);
                    let outputUpload = nextOutputUpload;
                    let botType = eleattr.botType;
                    playNextStep(flowChartType, isInputRequiredNext, nextBotType, nextRPAID, urlParams.get("woID"), urlParams.get("prjID"), versionNo, eleattr.id, nextTaskID, subActivityFlowChartDefID, nextBookingID, targetID, nextBookingStatus, nextExecType, eleattr.attrs.task.taskID, bookingID, eleattr.attrs.task.executionType, nextStepName, outputUpload, nextRunOnServer, true);
				}
                
            });

            paper.on('clickYesOnDecisionBox', function (elementView, evt) { // Bind yes event of decision box
                evt.stopPropagation();
                let eleattr = elementView.model.attributes;
                let decisionID = elementView.model.getSourceElement().get('id');
                let nextIsRunOnServer = '', nextOutputUpload = '', nextBotType = '', nextIsInputRequired = '';
                let botType = '';
                let proficiencyLevel = urlParams.get('proficiencyLevel');
                let targetElement = elementView.model.getTargetElement();
                let isNextDisabled = false;
                if (proficiencyLevel == 2) {
                    isNextDisabled = targetElement.attributes.executionType == C_EXECUTION_TYPE_MANUAL &&
                        !targetElement.attributes.viewMode.includes(C_PROFICIENCY_EXPERIENCED);
				}
                let next_taskId, next_bookingId, next_rpaid, next_bookingStatus, next_stepName, target_id, next_executionType;
                stepDetails(urlParams.get("woID"), decisionID, subActivityFlowChartDefID, executionType, eleattr);
                if (!isNextDisabled)
                {
                    
                    next_rpaid = targetElement.attr('tool/RPAID');
                    next_taskId = targetElement.attr('task/taskID');
                    next_bookingId = targetElement.attr('task/bookingID');
                    next_bookingStatus = targetElement.attr('task/status');
                    next_stepName = targetElement.attributes.name;
                    target_id = targetElement.id;
                    next_executionType = targetElement.attr('task/executionType');

                    if (next_executionType == "Automatic") {
                        nextIsRunOnServer = targetElement.attr('tool/isRunOnServer');
                        nextIsInputRequired = targetElement.attr('tool/isInputRequired');
                        
                        nextOutputUpload = targetElement.get('outputUpload');
                        nextBotType = targetElement.attributes.botType;
                    }
                    
                    if (isWorkOrderAutoSenseEnabled === true && isWorkFlowAutoSenseEnabled === true && targetElement.attributes.type === "ericsson.Decision" && nextIsRunOnServer != "1") {
                        notifyFloatingWindowOnButtonClickNextDecision(urlParams.get("woID"), subActivityFlowChartDefID, targetElement.attributes.id);
                    }

                    if (targetElement.attributes.type == C_STEP_TYPE_END) {
                        window.parent.validateAndCompleteWorkOrder(urlParams.get("woID"));
                    }
                    else if (targetElement.attributes.type != "ericsson.EndStep" && targetElement.attributes.type != "ericsson.Decision" && nextIsRunOnServer != "1") {

                        IscalledFromDecisionButton = true;
                        playNextStep(flowChartType, nextIsInputRequired, nextBotType, next_rpaid, urlParams.get("woID"), urlParams.get("prjID"), versionNo, eleattr.id, next_taskId, subActivityFlowChartDefID, next_bookingId, target_id, next_bookingStatus, next_executionType, '', '', '', next_stepName, nextOutputUpload, nextIsRunOnServer, true, paramsObj = { stopStepRequired: false }, IscalledFromDecisionButton);
                    }
                    
                }
                else {
                    // Play disabled manual Step
                    PlayNextDisabledManualStep(flowChartType, urlParams.get("woID"), urlParams.get("prjID"), versionNo, true);
                    
				}
            });

            paper.on('viewBOTLiveStreaming', function (elementView, evt) {
                evt.stopPropagation();
                let eleattr = elementView.model.attributes;
                let botNestID = eleattr.botNestId;
                let booking_Id = eleattr.isfRefId.toString();
                let isfBotId = eleattr.configForBot;

                $('#botStream').find('b').remove(0);
                $('#botLogStatus ul').empty()

                // getVideoStreamingUrl(bot_ID, booking_Id);
                getBotLogs(booking_Id, isfBotId);

                // $('#botdetails').data('botid', botNestID);
                $('#botdetails').data('bookingid', booking_Id);
                $('#botdetails').data('isfbotid', isfBotId);

                $('#modalBotNestStreaming').modal('show');
            });

            paper.on('clickNoOnDecisionBox', function (elementView, evt) { // Bind no event of decision box
                evt.stopPropagation();
                var eleattr = elementView.model.attributes;
                stepDetails(urlParams.get("woID"), eleattr.id, subActivityFlowChartDefID, 'NO', executionType);

            });
        },

        // Create and populate stencil.
        initializeStencil: function () {
            var id = 1000;
            var stencil = this.stencil = new joint.ui.Stencil({
                paper: this.paperScroller,
                snaplines: this.snaplines,
                scaleClones: true,
                width: 240,
                groups: App.config.stencil.groups,
                dropAnimation: true,
                groupsToggleButtons: true,
                search: {
                    '*': ['type', 'attrs/text/text', 'attrs/.label/text'],
                    'org.Member': ['attrs/.rank/text', 'attrs/.name/text']
                },
                // Use default Grid Layout
                layout: true,
                // Remove tooltip definition from clone
                dragStartClone: function (cell) {
                    return cell.clone().removeAttr('root/dataTooltip');
                },
                dragEndClone: function (cell) {
                    //return cloneEg;                    
                    return callOnDragEnd(app, cell);
                }
            });

            this.$('.stencil-container').append(stencil.el);
            stencil.render().load(App.config.stencil.shapes);
        },

        highlighter: V('path', {
            'stroke': '#e9fc03',
            'stroke-width': '2px',
            'fill': 'transparent',
            'pointer-events': 'none'
        }),


        initializeKeyboardShortcuts: function () {

            this.keyboard = new joint.ui.Keyboard();
            this.keyboard.on({

                /*   'ctrl+c': function () {
                       // Copy all selected elements and their associated links.
                       this.clipboard.copyElements(this.selection.collection, this.graph);
                   },
   
                   'ctrl+v': function () {
   
                       var pastedCells = this.clipboard.pasteCells(this.graph, {
                           translate: { dx: 20, dy: 20 },
                           useLocalStorage: true
                       });
   
                       var elements = _.filter(pastedCells, function (cell) {
                           return cell.isElement();
                       });
   
                       // Make sure pasted elements get selected immediately. This makes the UX better as
                       // the user can immediately manipulate the pasted elements.
                       this.selection.collection.reset(elements);
                   },
   
                   'ctrl+x shift+delete': function () {
                       this.clipboard.cutElements(this.selection.collection, this.graph);
                   },
   
                   'delete backspace': function (evt) {
                       evt.preventDefault();
                       this.graph.removeCells(this.selection.collection.toArray());
                   },
                   */
                'ctrl+z': function () {
                    this.commandManager.undo();
                    this.selection.cancelSelection();
                },

                'ctrl+y': function () {
                    this.commandManager.redo();
                    this.selection.cancelSelection();
                },

                'ctrl+a': function () {
                    this.selection.collection.reset(this.graph.getElements());
                },

                'ctrl+plus': function (evt) {
                    evt.preventDefault();
                    this.paperScroller.zoom(0.2, { max: 5, grid: 0.2 });
                },

                'ctrl+minus': function (evt) {
                    evt.preventDefault();
                    this.paperScroller.zoom(-0.2, { min: 0.2, grid: 0.2 });
                },

                'keydown:shift': function (evt) {
                    this.paperScroller.setCursor('crosshair');
                },

                'keyup:shift': function () {
                    this.paperScroller.setCursor('grab');
                }

            }, this);
        },

        initializeSelection: function () {

            this.clipboard = new joint.ui.Clipboard();
            this.selection = new joint.ui.Selection({
                paper: this.paper,
                handles: App.config.selection.handles
            });

            // Initiate selecting when the user grabs the blank area of the paper while the Shift key is pressed.
            // Otherwise, initiate paper pan.
            this.paper.on('blank:pointerdown', function (evt, x, y) {

                if (this.keyboard.isActive('ctrl', evt)) {
                    this.selection.startSelecting(evt);
                } else {
                    this.selection.cancelSelection();
                    this.paperScroller.startPanning(evt, x, y);
                    this.paper.removeTools();
                }

                //hide WI popup if visible
                if ($('#step_instructions').is(":visible")) {
                    $('#step_instructions').toggle();
                }

            }, this);

            this.paper.on('element:pointerdown', function (elementView, evt) {

                // Select an element if CTRL/Meta key is pressed while the element is clicked.
                if (this.keyboard.isActive('ctrl meta', evt)) {
                    this.selection.collection.add(elementView.model);
                }
                //hide WI popup if visible
                if ($('#step_instructions').is(":visible")) {
                    $('#step_instructions').toggle();
                }

            }, this);

            this.selection.on('selection-box:pointerdown', function (elementView, evt) {

                // Unselect an element if the CTRL/Meta key is pressed while a selected element is clicked.
                if (this.keyboard.isActive('ctrl meta', evt)) {
                    this.selection.collection.remove(elementView.model);
                }

            }, this);
        },


        createInspector: function (cell) {

            let urlParams = new URLSearchParams(window.location.search);
            let projID = urlParams.get("projID");
            let isMultiModeExe = $('#multiModeCheck').prop('checked');
            clearTimeout(window.setTimeoutID);
            clickcount = 0;
            if ((window.location.href.indexOf("FlowChartWorkOrderView") > -1)) {
                $(".inspector-container").find('[data-field="attrs/action"]').hide()

            }
            if (window.location.href.includes('FlowChartWorkOrderAdd')) {
                $('#stepIDforCurrentStep').val(cell.attributes.id);
            }
            if (global_toggleFlag) {
                $('.handles').find('.link').show();
                // $('.handles').find('.unlink').show();
            }
            else {
                $('.handles').find('.link').hide();
                // $('.handles').find('.unlink').hide();
            }
            if (cell.attributes.type == "ericsson.Manual" && window.location.href.includes('FlowChartWorkOrderAdd'))  {
                var config = Object.assign({}, App.config.inspector[cell.get('type')]);
                config.inputs.viewMode = {
                    type: 'viewMode',
                    label: 'View Mode',
                    group: 'attributes',
                    index: 7
                    //experienced: present
                };
            }

            //return joint.ui.Inspector.create('.inspector-container', _.extend({
            //    cell: cell,
            //    renderFieldContent: function (options, path, value, inspector) {
            //        if (options.type === 'viewMode') {

            //            var val2 = false;
            //            if (typeof value === 'string' && value.indexOf('experienced') > - 1) {
            //                val2 = true;
            //            }

            //            var $input1 = $('<input/>')
            //                .addClass('input-assessed')
            //                .attr('type', 'checkbox')
            //                .attr('checked', true)
            //                .attr('disabled', true);

            //            var $input2 = $('<input/>')
            //                .addClass('input-experienced')
            //                .attr('type', 'checkbox')
            //                .attr('checked', val2)

            //            var $label = $('<div/>').text(options.label);

            //            var $label1 = $('<span/>').text('Assessed');
            //            var $label2 = $('<span/>').text('Experienced');
            //            var $wrapper = $('<div/>').append([
            //                $label1,
            //                $input1
            //            ]);

            //            if (options.experienced) {
            //                $wrapper.append([
            //                    $label2,
            //                    $input2
            //                ])
            //            }

            //            var $button = $('<div/>').append([$label, $wrapper]);

            //            $button.on('change', function () {
            //                inspector.updateCell($button, path, options);
            //            });
            //            return $button;
            //        }
            //    },
            //    getFieldValue: function (attribute, type) {
            //        if (type === 'viewMode') {
            //            var val1 = $(attribute).find('.input-assessed')[0].checked;
            //            var input2 = $(attribute).find('.input-experienced')[0];
            //            var val2 = input2 ? input2.checked : false;
            //            var val = [];
            //            if (val1) val.push('Assessed');
            //            if (val2) val.push('Experienced');
            //            return { value: val.join(',') };
            //        }

            //    }

            //}, config));


            if ((cell.attributes.type == "ericsson.Manual") || (cell.attributes.type == "ericsson.Automatic")) {

                if (tools != undefined && tasks != undefined && rpaid != undefined) {
                    cell.set('myArrayOfOptions', tasks);
                    cell.set('myArrayOfTools', tools);
                    cell.set('myArrayOfRPAID', rpaid);
                    if (arr_workinstruction != undefined) { cell.set('myArrayOfWorkInstruction', arr_workinstruction); }
                }
            }
            var inspector = joint.ui.Inspector.create('.inspector-container', _.extend({
                cell: cell,
                


                renderFieldContent: function (options, path, value) {
                   
                    if (options.type == 'my-button-set' && window.location.href.indexOf("FlowChartBOTEdit") == -1) {
                        let $ax = '';


                        $ax = $('<a style="width: 154px;margin - left: 14px;margin - right: 8px;margin - top: 10px;margin-right: 8px;margin-left: 15px;margin-top: 8px;" class="btn btn-primary btn-lg addStepURL" data-toggle="modal" title="Step Instruction URL">' + stepURLButtonText + '</a>');
                        window.setTimeoutID = setTimeout(function () {
                            AddURLbuttonNameSet(cell.id);
                            
                        }, 0)
                        return $ax;
                        
                       
                    }
                    if (cell.attributes.type == "ericsson.Manual") {
                        if (options.type === 'viewMode') {

                            var val2 = false;
                            if (typeof value === 'string' && value.toLowerCase().indexOf('experienced') > - 1) {
                                val2 = true;
                            }

                           
                            
                            var $input1 = $('<input style="width:30px;"/>')
                                .addClass('input-assessed')
                                .attr('type', 'checkbox')
                                .attr('checked', true)
                                .attr('disabled', true);

                            var $input2 = $('<input style="width:30px;" id ="input-experienced"/>')
                                .addClass('input-experienced')
                                .attr('type', 'checkbox')
                                .attr('checked', val2);

                            var $label = $('<label/>').text(options.label);
                            

                            var $label1 = $('<span/>').text('Assessed');
                            var $label2 = $('<span/>').text('Experienced');
                            var $colDiv1 = $('<div class="assesedCheck"/>').append([

                                $input1,
                                $label1
                            ]);
                            var $colDiv2 = $('<div class="experiencedCheck" style="display:none"/>').append([
                                $input2,
                                $label2

                            ]);
                            var $wrapper = $('<div class="row" style="display:flex;padding-left:10px;"/>').append([
                               // $colDiv,
                                $colDiv1,
                                $colDiv2
                            ]);

                            if (isMultiModeExe == true) {
                                 $colDiv2 = $('<div class="experiencedCheck" />').append([
                                    $input2,
                                    $label2

                                ]);
                                $wrapper.append([
                                    // $colDiv,
                                    $colDiv2
                                   
                                ]);
                            }
                            else {
                                $wrapper.append([]);
                            }

                            var $button = $('<div/>').append([$label, $wrapper]);

                            $button.on('change', function () {
                                inspector.updateCell($button, path, options);
                            });
                            return $button;
                        }
                    }
                    //   var anchorVisibleFlag = true;
                    var arr = [];
                    stepTaskValue = typeof (cell.attributes.action) != "undefined" && cell.attributes.action != null ? cell.attributes.action : '';
                    //if (options.type == 'radiobuttons') {
                    //    return $('<div>').append([$('<input>').attr('type', "radio"), $('<input>').attr('type', "radio"), $('<input>').attr('type', "radio")]);

                    //}
                    if (options.type == 'select') {
                        if (Array.isArray(options.items)) {
                            arr = options.items.map(function (op) {
                                return { id: op.value, text: op.content }
                            });
                        }
                        else {
                        }
                        var $select = '';// var allToolsLabel = false;
                        if (path == "tool") { $select = $('<select id="toolsCombo"></select>').width(188).hide(); }
                        else if (path == "cascadeInput") {
                            $select = $('<select id="cascadeInput"></select>').width(188).hide();
                            // $select.val(value);
                            $select.prop('disabled', false);
                            $($select).prop('pointer-events', 'fill'); $($select).css('opacity', '1');
                        }
                        else if (path == 'action') { $select = $('<select id="taskList"></select>').width(188).hide(); }
                        else { $select = $('<select></select>').width(188).hide(); }



                        // select2 requires the element to be in the live DOM.
                        // Therefore, postpone the select2 initialization for after we
                        // add the Inspector container to the live DOM (see below).
                        let $uploadInfo = '';

                        window.setTimeoutID = setTimeout(function () {
                          
                            

                            //if (path == "outputUpload") {
                            //    var $label = $('<label>Output Upload: <span style="color:black;font-size:10px"> (If ServerBot then uploadOutput is always "Yes") </span></label>');
                            //    $uploadInfo = $('<span style="color:red;font-size:10px> If ServerBot then uploadOutput is always "Yes" </span>')
                            //}
                            if (path == "tool") {

                                if (jQuery.inArray(value, options.items) !== -1) { anchorVisibleFlag = true }
                                else { anchorVisibleFlag = false; }
                            }
                            //if (path != "cascadeInput") {
                            $select.show().select2({ data: arr }).val(value || '').trigger('change');
                            //}

                            if (!anchorVisibleFlag) { if (clickcount < 1) { $('#allToolAnchorwp').click(); clickcount + clickcount + 1; } } else { if (clickcount < 1) { $('#toolByProjAnchorwp').click(); clickcount + clickcount + 1; } }
                            //$select.on("select2:selecting", function (e) {
                            //    if (path == "action") {
                            //        if (cell.attributes.startRule == true || cell.attributes.stopRule == true) {
                            //            let confirm = confirmDeleteRule(cell, taskValue, e, returnBoolean);
                            //            if (!confirm) {
                            //                e.preventDefault();
                            //            }
                            //        }                                    
                            //    }
                            //});
                            $select.on('change', function () {
                                inspector.updateCell($select, path, options);
                                if (path == "action") {
                                    if (cell.attributes.startRule == true || cell.attributes.stopRule == true) {
                                        let taskValueOld = stepTaskValue;
                                        let taskValueNew = $select.val();
                                        confirmDeleteRule(cell, taskValueOld, taskValueNew);
                                    }
                                    else {
                                        stepTaskValue = $select.val();
                                    }
                                }
                                if (path == "rpaid") {
                                    let rpa_id = ''; var boolIsRunOnServer = false;
                                    rpa_id = ($select.val() != null) ? $select.val().split('@')[0] : null;
                                    if (rpa_id != null) {
                                        boolIsRunOnServer = calculateRunOnServer(rpa_id);
                                    }
                                    let isRunOnServer = boolIsRunOnServer.isRunOnServer;
                                    //    (boolIsRunOnServer.isRunOnServer) ? isRunOnServer = '1' : isRunOnServer = '0';
                                    let cascadeInput = $('.app-body #cascadeInput')[1];

                                    if (isRunOnServer == '1' && rpa_id != null) {
                                        if (boolIsRunOnServer.BotType.toLowerCase() != "botnest") { $('#cascadeInput').prop('disabled', false); $(cascadeInput).prop('disabled', false); }
                                        else { $('#cascadeInput').prop('disabled', 'disabled'); $('#cascadeInput').val('No'); $('#cascadeInput').change(); $(cascadeInput).prop('disabled', true); $(cascadeInput).val('No'); $(cascadeInput).change(); }
                                    }
                                    else if ((isRunOnServer == null || isRunOnServer == undefined) && (rpa_id != null)) { $('#cascadeInput').prop('disabled', 'disabled'); $(cascadeInput).prop('disabled', true); $('#cascadeInput').val('No'); $('#cascadeInput').change(); }
                                    else { $('#cascadeInput').prop('disabled', 'disabled'); $('#cascadeInput').val('No'); $('#cascadeInput').change(); $(cascadeInput).prop('disabled', true); $(cascadeInput).val('No'); $(cascadeInput).change(); }
                                    //if (isRunOnServer == '1' && bot)
                                    //var boolIsConfigAvail = '';
                                    //if (rpa_id != null) {
                                    //    boolIsConfigAvail = getBotConfigJsonByApi(rpa_id);
                                    //}
                                    //if ((boolIsConfigAvail.botjson == undefined || boolIsConfigAvail.botjson == null || boolIsConfigAvail.botjson == '' || boolIsConfigAvail.botjson == '[]') && (rpa_id != null)) {
                                    //    $(cascadeInput).prop('disabled', false); 
                                    //}
                                    //else { $(cascadeInput).prop('disabled', true); $(cascadeInput).val('No'); $(cascadeInput).change();}
                                    openConfig(rpa_id, cell.id);


                                    // cell.isRunOnServer = '1';


                                }
                                let hideInterval = setInterval(function () {
                                    if ($('#configSettings')) {
                                        $('#configSettings').hide();
                                        clearInterval(hideInterval);

                                    }
                                }, 500);

                            });

                            $('#allToolAnchorwp').on('click', function () {
                                clickcount = clickcount + 1;
                                allToolsLabel = false;

                                getAllTools();

                                if ($('#toolsCombo.select2-hidden-accessible').val() == null && clickcount > 1) {
                                    let cellText = cell.attr('bodyText/text').split('Tool Name:')[0] + 'Tool Name:';
                                    cell.attr('bodyText/text', cellText);
                                    cell.attr('task/toolID', ''); cell.attr('task/toolName', '');
                                    cell.attributes.tool = null;
                                }
                                cell.set('myArrayOfTools', tools);
                                anchorVisibleFlag = true;
                                inspector.updateCell($select, path, options);

                            })
                            $('.addStepURL').on('click', function () {
                                

                                $('#stepIdIdentifier').val(cell.id);
                                $('#TablestepIdIdentifier').val(cell.id);
                                
                                populateTableForNewlyAddedData();
                                $('#addInstructionStep').modal('show');
                                
                            });
                           

                            $('#toolByProjAnchorwp').on('click', function () {
                                clickcount = clickcount + 1;
                                allToolsLabel = true;

                                getOnlyProjectSpecificTools();
                                if ($('#toolsCombo.select2-hidden-accessible').val() == null && clickcount > 1) {
                                    let cellText = cell.attr('bodyText/text').split('Tool Name:')[0] + 'Tool Name:';
                                    cell.attr('bodyText/text', cellText);
                                    cell.attr('task/toolID', ''); cell.attr('task/toolName', '');
                                    cell.attributes.tool = null;
                                }

                                anchorVisibleFlag = false;
                                cell.set('myArrayOfTools', tools);
                                inspector.updateCell($select, path, options);

                            });

                            var $tool_link = '';
                            if (path == "tool") {
                                var $label = $('<label>Tools</label>');
                                $tool_link = (allToolsLabel == true) ? $('<span id="allToolAnchorwpspan"><a id="allToolAnchorwp" style="color:blue;font-size:10px;cursor:pointer"><i class="fa fa-lock" style="color:black"></i>(Click to unlock all tools)</a></span>') : $('<span id="projectToolAnchorwpspan"><a  id="toolByProjAnchorwp" style="color:blue;font-size:10px;cursor:pointer;display:block;"><i class="fa fa-undo" style="color:black;"></i>(Click to get tools mapped to project)</a></span>')
                            }
                            //else if (path == "integrate") {
                            //    var $label = $('<span id="inspectorIntegrateBot"><a  id="integarteBotByProjAnchorwp" style="color:blue;font-size:12px;cursor:pointer;"><i class="fa fa-undo" style="color:black;"></i>(Click to get tools mapped to project)</a></span>');
                            //}

                            else if (path == "action" || path == "tasks") { var $label = $('<label>Tasks</label>'); }
                            //else if (type == "viewMode") { var $label = $('<label>View Mode:</label>');}
                            else if (path == "rpaid") {
                                let rpa_id = ''; let boolIsRunOnServer = '';
                                let cascadeInput = $('.app-body #cascadeInput')[1];
                                rpa_id = ($select.val() != null) ? $select.val().split('@')[0] : null;
                                // onclick='openConfig("+rpa_id+",\""+cell.id+"\",this)'
                                var $label = $('<label>RPAID</label>');
                                let temp = JSON.stringify(cell.attributes.botConfigJson);
                                // var $icon1 = ($select.val() != null) ? $("<a href='#' style=;float: right;' title='Config Settings' data-configjson='"+temp+"' ><i class='fa fa-cog' id='configSettings' style='font-size: 16px;margin-top: 5px;'></i></a>") : $('<i class="fa fa-cog" id="configSettings" style="display:none"></i>')
                                if (value != "" && value != undefined) {

                                    $(cascadeInput).prop('disabled', false);
                                }
                                if (rpa_id != null) { boolIsRunOnServer = calculateRunOnServer(rpa_id); }
                                if (boolIsRunOnServer != "" && boolIsRunOnServer.isRunOnServer == "0") { $(cascadeInput).prop('disabled', true); $(cascadeInput).val('No'); $(cascadeInput).change(); }
                                else if (boolIsRunOnServer == "") { $(cascadeInput).prop('disabled', true); $(cascadeInput).val('No'); $(cascadeInput).change(); }
                            }
                            else if (path == "responsible") { var $label = $('<label>Responsible</label>'); }

                            else if (path == "cascadeInput") { var $label = $('<label>Cascade Input</label>'); $select.val(value); }

                            else if (path == "workInstruction") {
                                var dataURL = '';

                                dataURL = ($select.val() != null) ? uploadWIFile + $select.val().split('@')[0] : uploadWIFile + $select.val();
                                var $label = $('<label>Work Instruction </label>');

                                var $icon = ($select.val() != null) ? $('<a href="#" style="float: right;" title="View WorkInstruction" onclick="openpdf(' + ' \'' + $select.val().split('@')[3] + '\')"><i class="fa fa-file" id="viewInstruction" style="font-size: 16px;margin-top: 5px;"></i></a>') : $('<i class="fa fa-file" id="viewInstruction" style="display:none"></i>')
                            }
                            else if (path == 'outputUpload') {
                                var $label = $('<label>Output Upload </label>');
                                $uploadInfo = $('<span style="color:red;font-size:10px> If ServerBot then uploadOutput is always "Yes" </span>')
                            }
                            var $description = ($select.val() != null) ? $('<label>Description: ' + $select.val().split('@')[2] + '</label>') : $('<label style="display:none"></label>');

                            if (path == 'workInstruction') {
                                $select[0].parentElement.prepend($label[0], $icon[0]);
                                $select[0].parentElement.append($description[0]);
                            }
                            else if (path == 'tool') { $select[0].parentElement.prepend($label[0], $tool_link[0]); }

                            else if (path == 'rpaid') {
                                $select[0].parentElement.prepend($label[0]);
                                //$select[0].parentElement.append($icon1[0])}
                            }

                            else { $select[0].parentElement.prepend($label[0]); }


                            if (path == "action" || path == "responsible" || path == "uploadOutput") {
                                if (global_toggleFlag) {
                                    $select.prop('disabled', false);
                                    $($select[0].parentElement).prop('pointer-events', 'fill'); $($select[0].parentElement).css('opacity', '1');

                                }
                                else {
                                    $select.prop('disabled', true);
                                    $($select[0].parentElement).prop('pointer-events', 'none'); $($select[0].parentElement).css('opacity', '0.4');

                                }
                            }
                        }, 0);
                        return $select;
                    }
                    else if (window.location.href.includes('FlowChartWorkOrderAdd')) {
                        //let $ax = '';
                        if (options.type == 'startRuleButton') {
                            let $ax = '';

                            $ax = $(`<div style="display:flex" id="startRuleActivityDiv"><label>Start Rule</label>
                                    ${ !cell.attributes.startRule ?
                                    `<span><button class="btn btn-success btn-xs aqua-tooltip" title="Click to add a start rule" data-toggle="tooltip" onclick="getAllRulesByProjectAndTask(${projID + ',\'' + cell.id + '\', \'START\''})">
                                    <i class="fa fa-plus"></i>
                                </button></span>`
                                    :
                                    `<span>
                                    <a class="aqua-tooltip" data-toggle="tooltip" title="${cell.attributes.startRuleObj.ruleName}" style="font-weight: bolder; margin-right: 5px; cursor: pointer;" onclick="getAllRulesByProjectAndTask(${projID + ',\'' + cell.id + '\', \'START\''})">${truncate(cell.attributes.startRuleObj.ruleName, 80)}</a>
                                        <button class="btn btn-danger btn-xs aqua-tooltip" title="Click to delete this rule" data-toggle="tooltip" onclick="deleteRule(${'\'' + cell.id + '\',\'START\''})"><i class="fa fa-trash"></i></button>
                                </span>`}
                                
                                
                                    </div>`);


                            return $ax;


                        }
                        else if (options.type == 'stopRuleButton') {
                            let $ax = '';

                            $ax = $(`<div style="display:flex" id="stopRuleActivityDiv"><label>Stop Rule</label>
                                    ${ !cell.attributes.stopRule ?
                                    `<span><button class="btn btn-success btn-xs aqua-tooltip" title="Click to add a stop rule" data-toggle="tooltip" onclick="getAllRulesByProjectAndTask(${projID + ',\'' + cell.id + '\', \'STOP\''})">
                                    <i class="fa fa-plus"></i>
                                </button>`
                                    :
                                    `<span>
                                    <a class="aqua-tooltip" data-toggle="tooltip" title="${cell.attributes.stopRuleObj.ruleName}" style="font-weight: bolder; margin-right: 5px; cursor: pointer;" onclick="getAllRulesByProjectAndTask(${projID + ',\'' + cell.id + '\', \'STOP\''})">${truncate(cell.attributes.stopRuleObj.ruleName, 80)}</a>
                                        <button class="btn btn-danger btn-xs aqua-tooltip" title="Click to delete this rule" data-toggle="tooltip" onclick="deleteRule(${'\'' + cell.id + '\',\'STOP\''})"><i class="fa fa-trash"></i></button>
                                </span>`}
                                
                               
                                    </div><div><label style="font-size:10px; color:red">Note: Autosense is applied on Assessed WF Only.</label></div>`);

                            //$ax = $(`<label style="font-size:10px; color:red">Note: Autosense is applied on Assessed WF Only.</label>`);
                            return $ax;
                        }
                        
                    }

                },
                

                getFieldValue: function (attribute, type) {
                    if (type === 'my-button-set') {
                        AddURLbuttonNameSet(cell.id);
                        return { value: $(attribute).data('result') };
                    }
                    

                    if (type === 'select' && attribute.dataset.attribute == 'cascadeInput') {
                        return { value: $(attribute).val() || 'No' };
                    }

                    if (type === 'select' && attribute.dataset.attribute == 'outputUpload') {
                        return { value: $(attribute).val() || 'NO' };
                    }
                    //if (type === "radiobuttons") {
                    //    console.log('hi');
                    //}
                    
                    if (type === 'viewMode') {
                        var val1 = $(attribute).find('.input-assessed')[0].checked;
                        var input2 = $(attribute).find('.input-experienced')[0];
                        var val2 = input2 ? input2.checked : false;
                        var val = '';
                        if (val1) val ='Assessed';
                        if (val2) val = 'Experienced';
                        
                        return { value: val };
                    }
                },

            }, App.config.inspector[cell.get('type')]));
        },


        initializeToolsAndInspector: function () {

            this.paper.on({

                'element:pointerup': function (elementView) {

                    var element = elementView.model;

                    if (this.selection.collection.contains(element)) return;

                    new joint.ui.FreeTransform({
                        cellView: elementView,
                        allowRotation: false,
                        preserveAspectRatio: !!element.get('preserveAspectRatio'),
                        allowOrthogonalResize: element.get('allowOrthogonalResize') !== false
                    }).render();

                    new joint.ui.Halo({
                        cellView: elementView,
                        handles: App.config.halo.handles
                    }).render();

                    this.selection.collection.reset([]);
                    this.selection.collection.add(element, { silent: true });

                    this.paper.removeTools();
                    //if in flowchartedit page then no need to call createInspector as Inspector container is hidden
                    if (mode != 'edit') {
                        this.createInspector(element);
                        
                    }
                    
                },

                'link:pointerup': function (linkView) {

                    var link = linkView.model;

                    var ns = joint.linkTools;

                    var toolsView = new joint.dia.ToolsView({
                        name: 'link-pointerdown',
                        tools: [
                            new ns.Vertices(),
                            new ns.SourceAnchor(),
                            new ns.TargetAnchor(),
                            new ns.SourceArrowhead(),
                            new ns.TargetArrowhead(),
                            new ns.Segments,
                            new ns.Boundary({ padding: 15 }),
                            //new ns.Remove({ offset: -20, distance: 40 })
                        ]
                    });
                    if (global_toggleFlag) { toolsView.options.tools.push(new ns.Remove({ offset: -20, distance: 40 })) }
                    this.selection.collection.reset([]);
                    this.selection.collection.add(link, { silent: true });

                    var paper = this.paper;
                    joint.ui.Halo.clear(paper);
                    joint.ui.FreeTransform.clear(paper);
                    paper.removeTools();

                    linkView.addTools(toolsView);

                    this.createInspector(link);
                },

                'link:mouseenter': function (linkView) {

                    // Open tool only if there is none yet
                    if (linkView.hasTools()) return;

                    var ns = joint.linkTools;
                    var toolsView = new joint.dia.ToolsView({
                        name: 'link-hover',
                        tools: [
                            new ns.Vertices({ vertexAdding: false }),
                            new ns.SourceArrowhead(),
                            new ns.TargetArrowhead()
                        ]
                    });

                    linkView.addTools(toolsView);
                },

                'link:mouseleave': function (linkView) {
                    // Remove only the hover tool, not the pointerdown tool
                    if (linkView.hasTools('link-hover')) {
                        linkView.removeTools();
                    }
                }

            }, this);

            this.graph.on('change', function (cell, opt) {

                if (!cell.isLink() || !opt.inspector) return;

                var ns = joint.linkTools;
                var toolsView = new joint.dia.ToolsView({
                    name: 'link-inspected',
                    tools: [
                        new ns.Boundary({ padding: 15 }),
                    ]
                });

                cell.findView(this.paper).addTools(toolsView);

            }, this)
        },

        initializeNavigator: function () {

            var navigator = this.navigator = new joint.ui.Navigator({
                width: 240,
                height: 115,
                paperScroller: this.paperScroller,
                zoom: false,
                paperOptions: {
                    elementView: joint.shapes.app.NavigatorElementView,
                    linkView: joint.shapes.app.NavigatorLinkView
                }
            });

            this.$('.navigator-container').append(navigator.el);
            navigator.render();
        },

        initializeToolbar: function (tools) {

            var toolbar = this.toolbar = new joint.ui.Toolbar({
                groups: App.config.toolbar.groups,
                // tools: App.config.toolbar.tools,
                tools: tools,
                references: {
                    paperScroller: this.paperScroller,
                    commandManager: this.commandManager
                }
            });

            toolbar.on({
                'svg:pointerclick': _.bind(this.openAsSVG, this),
                'png:pointerclick': _.bind(this.openAsPNG, this),
                'to-front:pointerclick': _.bind(this.selection.collection.invoke, this.selection.collection, 'toFront'),
                'to-back:pointerclick': _.bind(this.selection.collection.invoke, this.selection.collection, 'toBack'),
                'layout:pointerclick': _.bind(this.layoutDirectedGraph, this),
                'snapline:change': _.bind(this.changeSnapLines, this),
                'clear:pointerclick': _.bind(this.graph.clear, this.graph),
                'print:pointerclick': _.bind(this.paper.print, this.paper),
                'grid-size:change': _.bind(this.paper.setGridSize, this.paper),
                'update:pointerclick': _.bind(this.updateFlowChart, this),
                'exec:pointerclick': _.bind(this.executeFlowChart, this),
                'edit:pointerclick': _.bind(this.editFlowChart, this),
                'step:pointerclick': _.bind(this.addStepToFlowChart, this),
                'toggle:change': _.bind(this.toggleEditMode, this),
                'back:pointerclick': _.bind(this.executeFlowChart, this),
            });

            this.$('.toolbar-container').append(toolbar.el);
            toolbar.render();
        },

        toggleEditMode: function (value, event) {

            if (value) {

                global_toggleFlag = true;
                this.initializeToolsAndInspector();
                $(".stencil-container").show();

                $($('.selection-wrapper')[1]).find('.remove').show()
                $($('.selection-wrapper')[1]).find('.rotate').show()
                $($('.selection-wrapper')[1]).find('.resize').show()
                $('.handles').find('.remove').show();
                $('.handles').find('.link').show();
                $('.handles').find('.unlink').hide();
                $(".paper-container").css("width", "100%");
                $(".paper-container").css("left", "240px");
                $(".paper-container").css("right", "240px");

                $($('*[data-attribute="action"]')[1]).prop('disabled', false); $($('*[data-attribute="action"]')[1]).parent().css('opacity', 1);
                $($('*[data-attribute="responsible"]')[1]).prop('disabled', false); $($('*[data-attribute="responsible"]')[1]).parent().css('opacity', 1);
                $($('*[data-attribute="outputUpload"]')[1]).prop('disabled', false); $($('*[data-attribute="outputUpload"]')[1]).parent().css('opacity', 1);
                $($('*[data-attribute="tasks"]')[1]).prop('disabled', false); $($('*[data-attribute="tasks"]')[1]).parent().css('opacity', 1);

                $('#saveJSON').show();
                $('#saveWFAttributes').hide();
                $('#importUploadDiv').show();
                $('#WFName').prop('readOnly', false);
                $('#multiModeCheck').prop('disabled', false);
                //  $('#SLAHours').prop('readOnly', false);
            }
            else {
                global_toggleFlag = false;
                app.graph.fromJSON(arrOfJsons['Assessed']);
                app.layoutDirectedGraph();
                $($('.selection-wrapper')[1]).find('.remove').hide();
                $($('.selection-wrapper')[1]).find('.rotate').hide()
                $($('.selection-wrapper')[1]).find('.resize').hide()
                $('.handles').find('.remove').hide();
                $('.handles').find('.link').hide();
                $('.handles').find('.unlink').hide();
                $(".stencil-container").hide();
                $(".paper-container").css("width", "100%");
                $(".paper-container").css("left", "0");
                $(".paper-container").css("right", "0");

                $($('*[data-attribute="action"]')[1]).prop('disabled', true); $($('*[data-attribute="action"]')[1]).parent().css('opacity', 0.4);
                $($('*[data-attribute="responsible"]')[1]).prop('disabled', true); $($('*[data-attribute="responsible"]')[1]).parent().css('opacity', 0.4);
                $($('*[data-attribute="outputUpload"]')[1]).prop('disabled', true); $($('*[data-attribute="outputUpload"]')[1]).parent().css('opacity', 0.4);
                $($('*[data-attribute="tasks"]')[1]).prop('disabled', true); $($('*[data-attribute="action"]')[1]).parent().css('opacity', 0.4);

                $('#saveJSON').hide();
                $('#saveWFAttributes').show();
                //  $('.handles').find('.remove').hide();
                $('#importUploadDiv').hide();
                $('#saveWFAttributes').css('margin-top', '20px');
                $('#WFName').prop('readOnly', true)
                //$('#IsQualWFCheck').prop('disabled', true);
                //  $('#SLAHours').prop('readOnly', true)
            }

        },

        changeSnapLines: function (checked) {

            if (checked) {
                this.snaplines.startListening();
                this.stencil.options.snaplines = this.snaplines;
            } else {
                this.snaplines.stopListening();
                this.stencil.options.snaplines = null;
            }
        },

        initializeTooltips: function () {

            new joint.ui.Tooltip({
                rootTarget: document.body,
                target: '[data-tooltip]',
                direction: 'auto',
                padding: 10
            });
        },

        // backwards compatibility for older shapes
        exportStylesheet: '.scalable * { vector-effect: non-scaling-stroke }',

        openAsSVG: function () {

            var paper = this.paper;
            paper.hideTools().toSVG(function (svg) {
                new joint.ui.Lightbox({
                    image: 'data:image/svg+xml,' + encodeURIComponent(svg),
                    downloadable: true,
                    fileName: 'Rappid'
                }).open();
                paper.showTools();
            }, {
                preserveDimensions: true,
                convertImagesToDataUris: true,
                useComputedStyles: false,
                stylesheet: this.exportStylesheet
            });
        },

        openAsPNG: function () {

            var paper = this.paper;
            paper.hideTools().toPNG(function (dataURL) {
                new joint.ui.Lightbox({
                    image: dataURL,
                    downloadable: true,
                    fileName: 'Rappid'
                }).open();
                paper.showTools();
            }, {
                padding: 10,
                useComputedStyles: false,
                stylesheet: this.exportStylesheet
            });
        },

        onMousewheel: function (cellView, evt, x, y, delta) {

            if (this.keyboard.isActive('alt', evt)) {
                evt.preventDefault();
                this.paperScroller.zoom(delta * 0.2, { min: 0.2, max: 5, grid: 0.2, ox: x, oy: y });
            }
            if ($('#step_instructions').is(":visible")) {
                $('#step_instructions').toggle();
            }
        },

        layoutDirectedGraph: function () {

            joint.layout.DirectedGraph.layout(this.graph, {
                setLinkVertices: true,
                rankDir: 'TB',
                marginX: 100,
                marginY: 100
            });
            
            this.paperScroller.centerContent();
        },
        

        getWFWithWoID: function (woID, proficiencyId = '0') {
            var result = false;
            window.parent.$('#selectStepsWO' + woID).empty();
            context = "WorkOrder:" + woID;
            pwIsf.addLayer({ text: 'Please wait ...' });
            $.isf.ajax({
                type: 'GET',
                url: service_java_URL + "woExecution/getWOWorkFlow/" + woID + "/" + proficiencyId,
                async: false,
                success: function (data) {
                    data = data.responseData;
                    window.parent.$('#selectStepsWO' + woID).append('<option value="WorkOrder ' + woID + '">WorkOrder ' + woID + '</option>');

                    if ('Success' in data) {
                        if (data.Success.flowChartJSON == null) {
                            data.Success.flowChartJSON = '{ "cells": [{ "size": { "width": 200, "height": 90 }, "angle": 0, "z": 1, "position": { "x": 225, "y": 230 }, "type": "basic.Rect", "attrs": { "rect": { "rx": 2, "ry": 2, "width": 50, "stroke-dasharray": "0", "stroke-width": 2, "fill": "#dcd7d7", "stroke": "#31d0c6", "height": 30 }, "text": { "font-weight": "Bold", "font-size": 11, "font-family": "Roboto Condensed", "text": "No FlowChart Available", "stroke-width": 0, "fill": "#222138" }, ".": { "data-tooltip-position-selector": ".joint-stencil", "data-tooltip-position": "left" } } }] }'
                        }
                        result = data.Success.flowChartJSON;
                        flowChartDefID = data.Success.subActivityDefID;
                        expertViewNeeded = data.Success.expertViewNeeded;
                        isWorkFlowAutoSenseEnabled = data.Success.workFlowAutoSenseEnabled;
                        isWorkOrderAutoSenseEnabled = data.Success.workOrderAutoSenseEnabled;
                        $("#workOrderAutoSenseEnabled").val(data.Success.workOrderAutoSenseEnabled);
                        $("#workFlowAutoSenseEnabled").val(data.Success.workFlowAutoSenseEnabled);
                        $("#defID").val(data.Success.subActivityDefID);
                        $('#flowchartVersion').val(data.Success.version);
                        $('#wfName').val(data.Success.workFlowName);

                        let proficiencyLevel = proficiencyId.toString().slice(-1);

                        if (proficiencyLevel == 2) {
                            data.Success.disabledStepDetails.enableStart = data.Success.disabledStepDetails.status == null ? false : true;
                            let disabledStepDetailsStr = JSON.stringify(data.Success.disabledStepDetails);

                            window.parent.$('#woButtons_' + woID + ' .disabledStepObj').val(disabledStepDetailsStr);
                            if (data.Success.disabledStepDetails.status && data.Success.disabledStepDetails.status.toUpperCase() == C_BOOKING_STATUS_ONHOLD)
                                window.parent.$('#woButtons_' + woID).css('background-color', '#E5E500');
                            else
                                window.parent.$('#woButtons_' + woID).css('background-color', '#e4e6e9');
                        }

                      
                        $.each(data.Success.workFlowSteps, function (i, d) {
                            const elem = window.parent.$('#selectStepsWO' + woID);
                            if (elem.length > 0) {
                                window.parent.$('#selectStepsWO' + woID).append('<option value="' + data.Success.workFlowSteps[i].stepID + '">' + data.Success.workFlowSteps[i].stepName + '</option>');
                                window.parent.$('#selectStepsWO' + woID).select2();
                            }
                            
                        })

                    }
                    else {
                        console.log(data)
                        pwIsf.alert({ msg: data.errormsg, type: "warning" });
                    }
                    context = $("#selectStepsWO" + woID).val

                },
                error: function (xhr, status, statusText) {
                    console.log("error");
                
                },
                complete: function (xhr, statusText) {
                    window.pwIsf.removeLayer();
                }

            });
            return result;

        },



        updateFlowChart: function () {

            // REMOVE HIGHLIGHT FROM ALL STEPS
            unhighlightAllCells(app);

            // Validation call for workflow
            let validateWF = validateWorkflow(app);
            displayWFerrorMessage(app, validateWF.errors, { callingFrom: 'DelExecution' });
            if (!validateWF.status) {
                return true;
            }

            $('#confirm').modal({
                backdrop: 'static'
            })
            $('#confirm').modal('show');
        },

        afterLoadingFlowchart: function () {
           
        },

        executeFlowChart: function () {
            window.parent.pwIsf.addLayer({ text: 'Please wait ...' });
            if (window.parent.$("#iframe_" + woID)) {
                window.parent.$("#iframe_" + woID).attr("src", window.location.href.split("FlowChartEdit")[0] + "FlowChartExecution?mode=execute&version=" + vnum + "&woID=" + woID + "&subActID=" + subActID + '&prjID=' + projid + '&wfid=' + wfid + '&flowchartType=' + flowChartType + '&proficiencyId=' + proficiencyId + '&status=' + woStatus + '&proficiencyLevel=' + proficiencyLevel);
            }
            else {
                flowChartOpenInNewWindow('FlowChartExecution?mode=execute&version=' + vnum + '&woID=' + woID + '&subActID=' + subActID + '&prjID=' + projid + '&flowchartType=' + flowChartType + '&proficiencyId=' + proficiencyId + '&status=' + woStatus + '&proficiencyLevel=' + proficiencyLevel);
            }

        },

        editFlowChart: function () {

            window.parent.pwIsf.addLayer({ text: 'Please wait ...' });
            window.parent.$("#iframe_" + woID).attr("src", window.location.href.split("FlowChartExecution")[0] + "FlowChartEdit?mode=edit&version=" + version + "&woID=" + woID + "&subActID=" + subActID + '&prjID=' + projid + '&wfid=' + wfid + '&flowchartType=' + flowChartType + '&proficiencyId=' + proficiencyId + '&status=' + woStatus + '&proficiencyLevel=' + proficiencyLevel);

            var isSelectExist = window.parent.$("#flowchartVersion_" + woID);
            if (isSelectExist.length > 0) {
                isSelectExist.select2('destroy');
                isSelectExist.hide();
            }
        },
        addStepToFlowChart: function () {
            $('#addEditStep').modal({
                backdrop: 'static'
            })
            $('#addEditStep').modal('show');
        },
        
        getMasterWF: function (woID, projectID, subActivityID, wfid) {

            var result = false;

            pwIsf.addLayer({ text: 'Please wait ...' });
            $.isf.ajax({
                type: 'GET',
                url: service_java_URL + "flowchartController/viewFlowChartForSubActivity/" + projectID + "/" + subActivityID + "/" + woID + "/0/0/" + wfid,
                async: false,
                success: function (data) {
                    if ('Success' in data[0]) {
                        if ((data[0].Success.FlowChartJSON == null) || (data[0].Success.FlowChartJSON == '') || (data[0] == '')) {
                            if (data[0] == '') {
                                data[0] = '{ "cells": [{ "size": { "width": 200, "height": 90 }, "angle": 0, "z": 1, "position": { "x": 225, "y": 230 }, "type": "basic.Rect", "attrs": { "rect": { "rx": 2, "ry": 2, "width": 50, "stroke-dasharray": "0", "stroke-width": 2, "fill": "#dcd7d7", "stroke": "#31d0c6", "height": 30 }, "text": { "font-weight": "Bold", "font-size": 11, "font-family": "Roboto Condensed", "text": "No FlowChart Available", "stroke-width": 0, "fill": "#222138" }, ".": { "data-tooltip-position-selector": ".joint-stencil", "data-tooltip-position": "left" } } }] }'
                                result = data[0];
                            }
                            else {
                                data[0].Success.FlowChartJSON = '{ "cells": [{ "size": { "width": 200, "height": 90 }, "angle": 0, "z": 1, "position": { "x": 225, "y": 230 }, "type": "basic.Rect", "attrs": { "rect": { "rx": 2, "ry": 2, "width": 50, "stroke-dasharray": "0", "stroke-width": 2, "fill": "#dcd7d7", "stroke": "#31d0c6", "height": 30 }, "text": { "font-weight": "Bold", "font-size": 11, "font-family": "Roboto Condensed", "text": "No FlowChart Available", "stroke-width": 0, "fill": "#222138" }, ".": { "data-tooltip-position-selector": ".joint-stencil", "data-tooltip-position": "left" } } }] }'
                                result = data[0].Success.FlowChartJSON;
                            }
                        }
                        else {
                            result = data[0].Success.FlowChartJSON;
                        }
                    }
                },
                error: function (xhr, status, statusText) {
                    console.log("error");
                    
                },
                complete: function () {
                  
                }
            });
            return result;
        },

        validateAutoSenseEnabled: function () {
            if (isWorkFlowAutoSenseEnabled === true ) {
                return true;
            } else {
                return false;
            }
        },
        enableAssessedExperiencedSwitchOfParent: function () {
            let getDiv = window.parent.$('#togBtnForFlowChartViewMode_' + woID).siblings().filter('div #divTogBtnForFlowChartViewMode');
            $(getDiv).removeClass('disableAssessedBg');
            window.parent.$('#togBtnForFlowChartViewMode_' + woID).attr("disabled", false);
        },

        disableAssessedExperiencedSwitchOfParent: function () {
            
            let getDiv = window.parent.$('#togBtnForFlowChartViewMode_'+woID).siblings().filter('div #divTogBtnForFlowChartViewMode');
            $(getDiv).addClass('disableAssessedBg');
            window.parent.$('#togBtnForFlowChartViewMode_' + woID).attr("disabled", true);
        },

        addAutoSenseModalPopup: function () {
            
            let tooltipText = C_AS_POPUP_TOOL_TIP_TXT;

            let userProficiency = window.parent.$('#viewBtn_' + woID).data('details').proficiencyLevel;

            var appendthis = '<div class="modal" id="autoSensePopupId" style="width:550px;top:50%;left:50%;overflow-y: hidden; -ms-transform: translate(-50%, -50%); position:absolute;transform: translate(-50%, -50%);" role="dialog" data-backdrop="static" data-keyboard="false">';
            appendthis = appendthis + ' <div class="modal-xl" style="max-width:100%;max-height:100%;text-align:center;width:550px;">';
            appendthis = appendthis + ' <div class="modal-content"> ';
            appendthis = appendthis + ' <div class="modal-header"> ';
            appendthis = appendthis + ' <div class="pull-right flowchartPullRightArea" style="display: inline-flex;height: 22px;">';
            appendthis = appendthis + ' </div>';
            appendthis = appendthis + ' <div><h5 id="" class="modal-title text-center"><strong>This Flowchart is Auto-Sense Assistance enabled !</strong> </h5></div>';
            appendthis = appendthis + ' </div> ';
            appendthis = appendthis + '<div class="modal-body" style="margin-top: 0px;padding-top: 0px;padding-bottom: 30px;">';
            appendthis = appendthis + '<h6 style="color: grey;">Your System conditions will be sensed, and if match found with pre-defined conditions in this flowchart, only a trigger will be sent to ISF to Start/Stop the step.<br/> Nothing else is being sensed, stored or sent.</h6>';
            appendthis = appendthis + '<h6 style="color: grey;">You have the option to continue without AutoSense Assistance.</h6>';
            appendthis = appendthis + '<h6 style="margin-bottom: 0px;"><b>Do you want to continue with Auto-Sense Assistance?</b></h6>';
            if (userProficiency == 2) {
                appendthis = appendthis + '<h7 style="color: #d40b0b;"><b>(Note! Once Autosense is set to Yes, you cannot toggle back to Experienced View mode)</b></h7>';
            }

            appendthis = appendthis + '<div class="row text-center" style="padding-top:5px;padding-bottom: 5px;">';
            appendthis = appendthis + '<button style="width: 75px;" id="btnAutoSenseYes" title="Yes" class="btn btn-success" type="button" value="Yes">Yes</button>';
            appendthis = appendthis + '<button style="width: 75px;" id="btnAutoSenseNo"  title="No" type="button" class="btn btn-danger" value="No">No</button>';
            appendthis = appendthis + ' </div>';
            appendthis = appendthis + ' <ul class="pull-right"> <div id="autosenseprotip"><span  class="noteLabel" style="color: #3a87ad;"> What is auto-sense&nbsp; <i  class="fa fa-question-circle" aria-hidden="true" ></i></span></div></ul>';
            appendthis = appendthis + ' </div>';
            appendthis = appendthis + ' </div>';
            appendthis = appendthis + ' </div>';
            appendthis = appendthis + ' </div>';
            $("body").append(appendthis);
            //$('#togBtnForFlowChartViewMode_' + woID).prop("checked", (flowChartType == expertFlowChartName) ? true : false);
            $('#autosenseprotip').on('mousemove', function () {
                showTooltip(event, tooltipText);
            });
            $('#autosenseprotip').on('mouseout', function () {
                hideTooltip();
            });
            $('#btnAutoSenseYes').on('click', function () {
                //disable switch
                //app.disableAssessedExperiencedSwitchOfParent();
                saveAutoSenseDetails(true, arrayJson, flowChartDefID, parseInt(proficiencyId), woID);
            });
            $('#btnAutoSenseNo').on('click', function () {
                //enable switch in case of experienced wf
                //if (parseInt(proficiencyLevel) === 1) {
                //    app.enableAssessedExperiencedSwitchOfParent();
                //}
                saveAutoSenseDetails(false, arrayJson, flowChartDefID, parseInt(proficiencyId), woID);
            });

        },

    });


    var decisionIconView = joint.dia.LinkView.extend({

        init: function () {
            joint.dia.LinkView.prototype.init.apply(this, arguments);
            this.listenTo(this.model, 'change:icons change:showIcons', this.renderIcons);
        },

        update: function () {
            joint.dia.LinkView.prototype.update.apply(this, arguments);
            this.renderIcons();
        },

        _iconsGroup: null,

        renderIcons: function () {

            var iconSize = 15;//9
            var iconPadding = 10;

            if (this._iconsGroup) this._iconsGroup.remove();

            var model = this.model;

            var showIcons = model.get('showIcons');
            if (!showIcons) { return; }

            let labelRect = this.el.getElementsByTagName('rect')[0];
            var labelTextSize = this.el.getElementsByTagName('text')[0].getBBox();
            labelRect.setAttribute('width', (parseInt(labelTextSize.width) + iconSize + iconPadding).toString());
            var labelRectSize = labelRect.getBBox();

            var icons = model.get('icons') || [];
            var x = labelRectSize.width / 2 - 8;//labelRect.getAttribute('width');

            //  var y = labelRectSize.height - iconSize - iconPadding + 13;

            if (!jQuery.isEmptyObject(icons)) {
                var vGroup = V('g').addClass('element-icons');

                vGroup.append(icons.map(function (icon) {

                    //x -= iconSize + iconPadding;

                    var vIcon = V('text');
                    vIcon.attr({
                        'class': icon.className,
                        'data-details': icon.details,
                        'width': iconSize,
                        'height': iconSize,
                        'font-size': iconSize,
                        'fill': icon.fill,
                        'cursor': 'pointer',
                        //'xlink:href': icon.src,   
                        'font-family': 'FontAwesome',
                        'font-weight': '900',
                        'alignment-baseline': 'middle',
                        'event': icon.event,
                        'x': x,
                        //  'x': (icon.src.toUpperCase().trim() == 'NO') ? x - 20 : ((icon.src.toUpperCase().trim() == 'YES') ? x - 145 : x),
                        //  'y': y
                    });

                    $(vIcon)[0].node.textContent = icon.src;
                    //vIcon.text(function (d,a) {
                    //    return d.textContent = '\uf040';
                    //});

                    var vTitle = V('title');
                    if (icon.className == 'autosenseClass') {
                        $($(vIcon)[0].node).on('mousemove', function () {
                            showTooltip(event, icon.tooltip);
                            //console.log('mousemove');
                        });
                        $($(vIcon)[0].node).on('mouseout', function () {
                            hideTooltip();
                            //console.log('mousemove');
                        });
                    } else {
                        vTitle.node.textContent = icon.tooltip;
                    }
                    vIcon.append(vTitle);
                    return vIcon;
                }))

                this._iconsGroup = vGroup;

                vGroup.appendTo(this.el.children[2].children[0]);
            }
        },
        onIconClick: function (evt) {
            //    makehi();
        },

    });



    var iconView = joint.dia.ElementView.extend({
        events: {
            //   'click image': 'onIconClick'
        },
        init: function () {
            joint.dia.ElementView.prototype.init.apply(this, arguments);
            this.listenTo(this.model, 'change:icons change:showIcons', this.renderIcons);
        },

        update: function () {
            joint.dia.ElementView.prototype.update.apply(this, arguments);
            this.renderIcons();
        },

        _iconsGroupBL: null,
        _iconsGroupBR: null,
        _iconsGroupTR: null,
        _iconsGroupTL: null,
       // _iconsGroupBRBL: null,

        renderIcons: function () {

            var iconSize = 20;//9
            var iconPaddingX = 3;//10
            var iconPaddingY = 5;

            if (this._iconsGroupBL) this._iconsGroupBL.remove();
            if (this._iconsGroupBR) this._iconsGroupBR.remove();
            if (this._iconsGroupTR) this._iconsGroupTR.remove();
            if (this._iconsGroupTL) this._iconsGroupTL.remove();
           // if (this._iconsGroupBRBL) this._iconsGroupBRBL.remove();

            var model = this.model;
            var showIcons = model.get('showIcons');

            if (!showIcons) { return; }
            var size = model.get('size');
            var icons = model.get('icons') || [];
            let bottomLeftIcons, bottomRightIcons, topLeftIcons, topRightIcons = [];

            if (!jQuery.isEmptyObject(icons)) {
                bottomLeftIcons = icons.filter(function (el) { return el.className.includes('bottom-left') == true; });
                bottomRightIcons = icons.filter(function (el) { return el.className.includes('bottom-right') == true; });
                topLeftIcons = icons.filter(function (el) { return el.className.includes('top-left') == true; });
                topRightIcons = icons.filter(function (el) { return el.className.includes('top-right') == true; });
            }
            var x_br = size.width;

            var y_br = size.height - iconSize - iconPaddingY + 13;

            var x_bl = 2;
            var y_bl = size.height - iconSize - iconPaddingY + 13;

            var x_tl = 2;
            var y_tl = 10;

            var x_tr = size.width;
            var y_tr = 10;


            if (!jQuery.isEmptyObject(icons)) {
                var vGroupBL = V('g').addClass('element-icons-bottom-left');
                var vGroupBR = V('g').addClass('element-icons-bottom-right');
                //var vGroupBRBL = V('g').addClass('element-icons-bottom-icons');
                var vGroupTR = V('g').addClass('element-icons-top-right');
                var vGroupTL = V('g').addClass('element-icons-top-left');

                let BOTIcons = bottomRightIcons.map(function (icon) {

                    x_br -= iconSize + iconPaddingX;

                    if (icon.className.includes('info') == true) { x_br = x_br + 10; }

                    let vIcon = V('text');
                    vIcon.attr({
                        'class': icon.className,
                        // 'text-anchor': icon.textanchor,
                        'data-details': icon.details,                        
                        'width': iconSize,
                        'height': iconSize,
                        'font-size': iconSize,
                        'fill': icon.fill,
                        'cursor': 'pointer',
                        //'xlink:href': icon.src,   
                        'font-family': 'FontAwesome',
                        'alignment-baseline': 'middle',
                        'event': icon.event,
                        // 'x': (icon.src.toUpperCase().trim() == 'NO') ? x - 20 : ((icon.src.toUpperCase().trim() == 'YES') ? x - 145 : x),
                        'x': x_br,
                        'y': y_br
                    });

                    $(vIcon)[0].node.textContent = icon.src;
                    let vTitle = V('title');
                    vTitle.node.textContent = icon.tooltip;
                    vIcon.append(vTitle);
                    return vIcon;
                })
                vGroupBR.append(BOTIcons);

                let actionIcons = bottomLeftIcons.map(function (icon) {

                    // x_bl += iconSize + iconPaddingX;

                    let vIcon = V('text');
                    vIcon.attr({
                        'class': icon.className,
                        // 'text-anchor': icon.textanchor,
                        'data-details': icon.details,
                        'width': iconSize,
                        'height': iconSize,
                        'font-size': iconSize,
                        'fill': icon.fill,
                        'cursor': 'pointer',
                        //'xlink:href': icon.src,   
                        'font-family': 'FontAwesome',
                        'alignment-baseline': 'middle',
                        'event': icon.event,
                        'x': x_bl,
                        'y': y_bl
                    });
                    x_bl += iconSize + iconPaddingX;
                    $(vIcon)[0].node.textContent = icon.src;
                    let vTitle = V('title');
                    if (icon.className == 'autosenseClass') {
                        $($(vIcon)[0].node).on('mousemove', function () {
                            showTooltip(event, icon.tooltip);
                        });
                        $($(vIcon)[0].node).on('mouseout', function () {
                            hideTooltip();
                        });
                    } else {
                        vTitle.node.textContent = icon.tooltip;
                    }
                    vIcon.append(vTitle);

                    return vIcon;
                })
                vGroupBL.append(actionIcons);

                let feedbackIcons = topLeftIcons.map(function (icon) {

                    let vIcon = V('text');
                    vIcon.attr({
                        'class': icon.className,
                        // 'text-anchor': icon.textanchor,
                        'data-details': icon.details,
                        'width': iconSize,
                        'height': iconSize,
                        'font-size': iconSize,
                        'fill': icon.fill,
                        'cursor': 'pointer',
                        //'xlink:href': icon.src,   
                        'font-family': 'FontAwesome',
                        'alignment-baseline': 'middle',
                        'event': icon.event,
                        // 'x': (icon.src.toUpperCase().trim() == 'NO') ? x - 20 : ((icon.src.toUpperCase().trim() == 'YES') ? x - 145 : x),
                        'x': x_tl,
                        'y': y_tl
                    });
                    x_tl += iconSize + iconPaddingX + 2;
                    $(vIcon)[0].node.textContent = icon.src;
                    let vTitle = V('title');
                    vTitle.node.textContent = icon.tooltip;
                    vIcon.append(vTitle);
                    return vIcon;
                })
                vGroupTL.append(feedbackIcons);

                let WIIcons = topRightIcons.map(function (icon) {


                    x_tr -= iconSize + iconPaddingX;                      

                    if (icon.className.includes('work-instruction') == true) { iconSize = 17; }

                    let vIcon = V('text');
                    vIcon.attr({
                        'class': icon.className,
                        // 'text-anchor': icon.textanchor,
                        'data-details': icon.details,
                        'data-toggle': icon.toggle,
                        'width': iconSize,
                        'height': iconSize,
                        'font-size': iconSize,
                        'fill': icon.fill,
                        'cursor': 'pointer',
                        //'xlink:href': icon.src,   
                        'font-family': 'FontAwesome',
                        'alignment-baseline': 'middle',
                        'event': icon.event,
                        // 'x': (icon.src.toUpperCase().trim() == 'NO') ? x - 20 : ((icon.src.toUpperCase().trim() == 'YES') ? x - 145 : x),
                        'x': x_tr,
                        'y': y_tr
                    });

                    $(vIcon)[0].node.textContent = icon.src;
                    let vTitle = V('title');
                    vTitle.node.textContent = icon.tooltip;
                    vIcon.append(vTitle);
                    return vIcon;
                })
                vGroupTR.append(WIIcons);

                //vGroupBRBL.append(vGroupBL);
                //vGroupBRBL.append(vGroupBR);

                this._iconsGroupBL = vGroupBL;
                this._iconsGroupBR = vGroupBR;
                this._iconsGroupTL = vGroupTL;
                this._iconsGroupTR = vGroupTR;
              //  this._iconsGroupBRBL = vGroupBRBL;

                vGroupBL.appendTo(this.el);
                vGroupBR.appendTo(this.el);
                //vGroupBRBL.appendTo(this.el);
                vGroupTL.appendTo(this.el);
                vGroupTR.appendTo(this.el);

            }
        },

        onIconClick: function (evt) {

        },
    });



})(_, joint);

//function handleParentSwitchForQualifiedWF(chkQulify, curStatus, woID, isExpertViewNeeded) {
//    // case1 : disable parent switch only in case of wf is autosense enable and expert user view wf popup display.
//    // case2 : disable parent switch only in case of wf is autosense enable and expert user choose yes.
//    // case2 : enable parent switch only in case of wf is autosense enable and expert user choose no.
//    // case4 : enable parent switch in case of normal wf without autosense..
//    if (chkQulify === 1 && isWorkFlowAutoSenseEnabled === true) {
//        let getDiv = window.parent.$('#togBtnForFlowChartViewMode_' + woID).siblings().filter('div #divTogBtnForFlowChartViewMode');
//        $(getDiv).addClass('disableAssessedBg');
//        window.parent.$('#togBtnForFlowChartViewMode_' + woID).attr("disabled", true);
//    }
//    if (isWorkFlowAutoSenseEnabled === true) {
//        let lastASUserAction = JSON.parse(localStorage.getItem(C_LS_KEY_ASUSERINPUT + woID));
//        if (curStatus === C_WO_ASSIGNED_STATUS) {
//            if (lastASUserAction != null || lastASUserAction != undefined) {
//                if (lastASUserAction.USERACTION === true) {
//                    let getDiv = window.parent.$('#togBtnForFlowChartViewMode_' + woID).siblings().filter('div #divTogBtnForFlowChartViewMode');
//                    $(getDiv).addClass('disableAssessedBg');
//                    window.parent.$('#togBtnForFlowChartViewMode_' + woID).attr("disabled", true);
//                } else if (lastASUserAction.USERACTION === false &&  (isExpertViewNeeded=== true)) {
//                    let getDiv = window.parent.$('#togBtnForFlowChartViewMode_' + woID).siblings().filter('div #divTogBtnForFlowChartViewMode');
//                    $(getDiv).removeClass('disableAssessedBg');
//                    window.parent.$('#togBtnForFlowChartViewMode_' + woID).attr("disabled", false);
//                }
//            }
//        }
//    }

//}

//function handleParentSwitchForQualifiedWF(chkQulify, curStatus, woID, isExpertViewNeeded) {
//    // case1 : disable parent switch only in case of wf is autosense enable and expert user view wf popup display.
//    // case2 : disable parent switch only in case of wf is autosense enable and expert user choose yes.
//    // case3 : enable parent switch only in case of wf is autosense enable and expert user choose no.
//    // case4 : enable parent switch in case of normal wf without autosense..
//    if (chkQulify === 1 && isWorkFlowAutoSenseEnabled === true) {
//        let getDiv = window.parent.$('#togBtnForFlowChartViewMode_' + woID).siblings().filter('div #divTogBtnForFlowChartViewMode');
//        $(getDiv).addClass('disableAssessedBg');
//        window.parent.$('#togBtnForFlowChartViewMode_' + woID).attr("disabled", true);
//}
//    if (curStatus === C_WO_ASSIGNED_STATUS && isExpertViewNeeded === true) {
//        let lastASUserAction = JSON.parse(localStorage.getItem(C_LS_KEY_ASUSERINPUT + woID));
//        if (lastASUserAction != null || lastASUserAction != undefined) {
//            if (lastASUserAction.USERACTION === false) {
//                let getDiv = window.parent.$('#togBtnForFlowChartViewMode_' + woID).siblings().filter('div #divTogBtnForFlowChartViewMode');
//                $(getDiv).removeClass('disableAssessedBg');
//                window.parent.$('#togBtnForFlowChartViewMode_' + woID).attr("disabled", false);
//            } else {
//                let getDiv = window.parent.$('#togBtnForFlowChartViewMode_' + woID).siblings().filter('div #divTogBtnForFlowChartViewMode');
//                $(getDiv).addClass('disableAssessedBg');
//                window.parent.$('#togBtnForFlowChartViewMode_' + woID).attr("disabled", true);
//            }
//        }
//    }
  

//}

//get the first enabled step
function getFirstEnabledStep(appObj, root) {
    var firstStep = appObj.graph.getNeighbors(root, { outbound: true })[0];
    if (firstStep.attributes.type == C_STEP_TYPE_MANUAL && !firstStep.attributes.viewMode.includes(C_PROFICIENCY_EXPERIENCED)) {
        firstStep = getFirstEnabledStep(appObj, firstStep);
    }
    return firstStep;
}

let stepTaskValue = '';

//Gets rules based on Project ID and Task ID
function getAllRulesByProjectAndTask(projectID, cellID, ruleType) {

    $('#ruleTypeTitle').text('Select any one ' + ruleType.toLowerCase() + ' rule for this step');

    let cellAttr = app.graph.getCell(cellID).attributes;
    let taskIDTaskName = cellAttr.action;
    let stepName = cellAttr.name;
    let ruleId = ruleType == 'START' ? cellAttr.startRuleObj.ruleId : cellAttr.stopRuleObj.ruleId;
    let ruleName = ruleType == 'START' ? cellAttr.startRuleObj.ruleName : cellAttr.stopRuleObj.ruleName;
    let ruleDescription = ruleType == 'START' ? cellAttr.startRuleObj.ruleDescription : cellAttr.stopRuleObj.ruleDescription;    

    if (taskIDTaskName != undefined && taskIDTaskName != "undefined" && taskIDTaskName != null && taskIDTaskName != "null" && stepName != "") {
        let taskID = taskIDTaskName.split('@')[0];
        $('#taskIDforRules').val(taskID);
        $('#projectIDforRules').val(projectID);
        $('#cellIDforRules').val(cellID);
        $('#ruleType').val(ruleType);

        if ($('#addedRuleDiv').is(':hidden')) {
            //If rule already exists for this step
            if (ruleId != '') {
                $('#ruleId').val(ruleId);
                $('#ruleName').html(truncate(ruleName, 160));
                $('#ruleName').attr('data-original-title', ruleName);
                $('#ruleDescription').html(truncate(ruleDescription, 355));
                $('#ruleDescription').attr('data-original-title', ruleDescription);
                $('#addedRuleDiv').show();
            }
            else {
                $('#ruleId').val('');
                $('#ruleName').html('');
                $('#ruleDescription').html('');
                $('#ruleName').removeAttr('data-original-title');
                $('#ruleDescription').removeAttr('data-original-title');
                $('#addedRuleDiv').hide();
            }
        }

        let service_URL = service_java_URL + 'autoSense/getRulesForTaskIDProjectID?projectId=' + projectID + '&taskId=' + taskID + '&ruleType=' + ruleType + 'TASK';
        //let service_URL = service_java_URL + 'autoSense/getRulesForTaskIDProjectID?projectId=11703&taskId=3736&ruleType=STOPTASK';
        pwIsf.addLayer({ text: 'Please wait ...' });

        $.isf.ajax({
            url: service_URL,
            type: 'GET',
            success: function (data) {
            },
            complete: function (xhr, status) {
                if (status == 'success') {
                    if (xhr.responseJSON.isValidationFailed) {
                        responseHandler(xhr.responseJSON);
                    }
                    else {
                        let selectAddedRule = false;
                        if (xhr.responseJSON.responseData != null && xhr.responseJSON.responseData != 'null') {
                            $('#allRulesTableDiv').show();
                            $('#noDataHeader').hide();
                            let getData = xhr.responseJSON.responseData;
                            let ruleId = $('#ruleId').val();                            
                            if (ruleId != '' && getData.map(rule => rule.ruleId).indexOf(Number(ruleId)) != -1) {
                                let ruleObj = getData.find(rule => rule.ruleId == Number(ruleId));
                                getData = getData.filter(rule => rule.ruleId != Number(ruleId));
                                getData.unshift(ruleObj);
                                selectAddedRule = true;

                            }
                            configureAllRulesDataTable(getData); // Add Datatable configuration to the table
                            if (selectAddedRule) {
                                $('input[name=allRules]')[0].checked = true;
                                enableAddRuleButton();
                            }
                            if (ruleId == '') {
                                $('#ruleLinkDiv').css('margin-bottom', '15px');
                            }
                            else {
                                $('#ruleLinkDiv').css('margin-bottom', '0px')
                            }
                        }
                        else {
                            $('#allRulesTableDiv').hide();                            
                            $('#noDataHeader').show();
                        }
                        $('#filterByTask').show();
                        $('#filterByTaskAndProject').hide();
                        if (!selectAddedRule) {
                            $('#addRuleButton').removeClass('btn-success').addClass('btn-disabled')
                            $('#addRuleButton').attr('disabled', true);
                        }
                        $('#rulesTaskProject').modal('show');                        
                    }
                    pwIsf.removeLayer();
                }
            },
            error: function (data) {
                pwIsf.removeLayer();
            }
        });



        let createAllRulesTable = (getData) => {
            let thead = `<thead>
                        <tr>
                            <th>Action</th>
                            <th>Rule Name</th>
                            <th>Rule Description</th>
                            <th>Creator Name/Signum</th>
                        </tr>
                    </thead>`;

            let tfoot = `<tfoot>
                        <tr>
                            <th>Action</th>
                            <th>Rule Name</th>
                            <th>Rule Description</th>
                            <th>Creator Name/Signum</th>
                        </tr>
                    </tfoot>`;

            let tbody = ``;

            $(getData).each(function (i, d) {
                tbody += `<tr>
                            <td>
                                <div class="radio" style="display: table-cell;">
                                    <input type="radio" name="allRules" value="${d.ruleId}" onclick="enableAddRuleButton()">
                                </div>
                                <input class="ruleJson" type="hidden" value='${d.ruleJson}' />
                            </td>
                            <td>${d.ruleName}</td>
                            <td>${d.ruleDescription}</td>
                            <td>${d.nameAndCreatedBy}</td>
                        </tr>`;
            });

            $('#allRulesTable').html(thead + '<tbody>' + tbody + '</tbody>' + tfoot);

        };

        let configureAllRulesDataTable = (getData) => {

            if ($.fn.dataTable.isDataTable('#allRulesTable')) {
                $('#allRulesTable').DataTable().destroy();
                $('#allRulesTableDiv').html('<table class="table table-bordered table-striped table-hover" id="allRulesTable"></table >');
            }

            createAllRulesTable(getData); //Create the Table for Rules

            let allRulesDatatable = $('#allRulesTable').DataTable({
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
                dom: 'lBfrtip',
                buttons: [
                    {
                        extend: 'excelHtml5',
                        filename: 'AutoSenseRules_For_ProjectID_' + projectID + '_And_TaskID_' + taskID,
                        title: 'AutoSenseRules_For_ProjectID_' + projectID + '_And_TaskID_' + taskID
                    }
                ],
                initComplete: function () {

                    $('#allRulesTable tfoot th').each(function (i) {
                        var title = $('#allRulesTable thead th').eq($(this).index()).text();
                        if (title != 'Action') {
                            $(this).html('<input type="text" class="form-control" style="font-size:12px;"  placeholder="Search ' + title + '" data-index="' + i + '" />');
                        }
                        else {
                            $(this).html('');
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
                            };
                        });
                    });

                }
            });

            $('#allRulesTable tfoot').insertAfter($('#allRulesTable thead'));

        }
    }
    else {
        pwIsf.alert({ msg: "Please provide task id and step name!", type: "error" });
    }
}

//Get all rules based on task id
function getAllRulesByTask(taskID, ruleType) {

    let service_URL = service_java_URL + 'autoSense/getRulesForTaskID?taskId=' + taskID + '&ruleType=' + ruleType + 'TASK';
    pwIsf.addLayer({ text: 'Please wait ...' });

    $.isf.ajax({
        url: service_URL,
        type: 'GET',
        success: function (data) {
        },
        complete: function (xhr, status) {
            if (status == 'success') {
                if (xhr.responseJSON.isValidationFailed) {
                    responseHandler(xhr.responseJSON);
                }
                else {
                    let selectAddedRule = false;
                    if (xhr.responseJSON.responseData != null && xhr.responseJSON.responseData != 'null') {
                        $('#allRulesTableDiv').show();
                        $('#noDataHeader').hide();
                        let getData = xhr.responseJSON.responseData;
                        let ruleId = $('#ruleId').val();                        
                        if (ruleId != '' && getData.map(rule => rule.ruleId).indexOf(Number(ruleId)) != -1) {
                            let ruleObj = getData.find(rule => rule.ruleId == Number(ruleId));
                            getData = getData.filter(rule => rule.ruleId != Number(ruleId));
                            getData.unshift(ruleObj);
                            selectAddedRule = true;
                        }
                        configureAllRulesDataTable(getData); // Add Datatable configuration to the table
                        if (selectAddedRule) {
                            $('input[name=allRules]')[0].checked = true;
                            enableAddRuleButton();
                        }
                        if (ruleId == '') {
                            $('#ruleLinkDiv').css('margin-bottom', '15px');
                        }
                        else {
                            $('#ruleLinkDiv').css('margin-bottom', '0px')
                        }
                    }
                    else {
                        $('#allRulesTableDiv').hide();                        
                        $('#noDataHeader').show();
                    }
                    $('#filterByTaskAndProject').show();
                    $('#filterByTask').hide();
                    if (!selectAddedRule) {
                        $('#addRuleButton').removeClass('btn-success').addClass('btn-disabled')
                        $('#addRuleButton').attr('disabled', true);
                    }
                    $('#rulesTaskProject').modal('show');                    
                }
                pwIsf.removeLayer();
            }
        },
        error: function (data) {
            pwIsf.removeLayer();
        }
    });



    let createAllRulesTable = (getData) => {
        let thead = `<thead>
                    <tr>
                        <th>Action</th>
                        <th>Rule Name</th>
                        <th>Rule Description</th>
                        <th>Creator Name/Signum</th>
                    </tr>
                </thead>`;

        let tfoot = `<tfoot>
                    <tr>
                        <th>Action</th>
                        <th>Rule Name</th>
                        <th>Rule Description</th>
                        <th>Creator Name/Signum</th>
                    </tr>
                </tfoot>`;

        let tbody = ``;

        $(getData).each(function (i, d) {
            tbody += `<tr>
                        <td>
                            <div class="radio" style="display: table-cell;">
                                <input type="radio" name="allRules" value="${d.ruleId}" onclick="enableAddRuleButton()">
                            </div>
                            <input class="ruleJson" type="hidden" value='${d.ruleJson}' />
                        </td>
                        <td>${d.ruleName}</td>
                        <td>${d.ruleDescription}</td>
                        <td>${d.nameAndCreatedBy}</td>
                    </tr>`;
        });

        $('#allRulesTable').html(thead + '<tbody>' + tbody + '</tbody>' + tfoot);

    };

    let configureAllRulesDataTable = (getData) => {

        if ($.fn.dataTable.isDataTable('#allRulesTable')) {
            $('#allRulesTable').DataTable().destroy();
            $('#allRulesTableDiv').html('<table class="table table-bordered table-striped table-hover" id="allRulesTable"></table >');
        }

        createAllRulesTable(getData); //Create the Table for Rules

        let allRulesDatatable = $('#allRulesTable').DataTable({
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
            dom: 'lBfrtip',
            buttons: [
                {
                    extend: 'excelHtml5',
                    filename: 'AutoSenseRules_For_TaskID_' + taskID,
                    title: 'AutoSenseRules_For_TaskID_' + taskID
                }
            ],
            initComplete: function () {

                $('#allRulesTable tfoot th').each(function (i) {
                    var title = $('#allRulesTable thead th').eq($(this).index()).text();
                    if (title != 'Action') {
                        $(this).html('<input type="text" class="form-control" style="font-size:12px;"  placeholder="Search ' + title + '" data-index="' + i + '" />');
                    }
                    else {
                        $(this).html('');
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
                        };
                    });
                });

            }
        });

        $('#allRulesTable tfoot').insertAfter($('#allRulesTable thead'));

    }

}

//Save rule in Temp Table
function saveRuleInTempTable() {

    let saveStepRuleObj = new Object();
    let rowObj = $('#allRulesTable input[name=allRules]:checked');
    let cellID = $('#cellIDforRules').val();
    let cellAttributes = app.graph.getCell(cellID).attributes;
    let stepName = cellAttributes.name;
    let taskID = $('#taskIDforRules').val();
    //let taskName = cellAttributes.action.split('@')[1];
    let urlParams = new URLSearchParams(window.location.search);
    let projID = urlParams.get("projID");
    let subID = urlParams.get("subId");
    let wfOwner = urlParams.get("wfownername").split('[')[0];
    let loggedInSignum = signumGlobal;
    let ruleName = rowObj.closest('tr').find('td:nth-child(2)').text();
    let ruleDescription = rowObj.closest('tr').find('td:nth-child(3)').text();
    let ruleType = $('#ruleType').val();
    let ruleID = rowObj.val();
    

    saveStepRuleObj.ruleId = rowObj.val();
    //saveStepRuleObj.stepIDStepNameTaskId = cellID + '_' + stepName + '_' + taskID;
    saveStepRuleObj.stepID = cellID;
    saveStepRuleObj.taskId = taskID;
    saveStepRuleObj.projectIDSubactivityIDLoggedInSignum = projID + '_' + subID + '_' + loggedInSignum;
    saveStepRuleObj.taskActionName = ruleType;
    saveStepRuleObj.parseRuleJson = rowObj.closest('tr').find('td:nth-child(1) input.ruleJson').val();
    saveStepRuleObj.createdBy = signumGlobal;
    saveStepRuleObj.modifiedBy = signumGlobal;
    saveStepRuleObj.experiencedFlow = $('#multiModeCheck').prop('checked');

    let stepIDStepNameTaskId = cellID + '_' + stepName + '_' + taskID;

    let service_URL = service_java_URL + "autoSense/saveStepRuleInAutoSenseTmpTable";
    pwIsf.addLayer({ text: 'Please wait ...' });

    $.isf.ajax({
        url: service_URL,
        type: 'POST',
        crossDomain: true,
        context: this,
        contentType: 'application/json',
        data: JSON.stringify(saveStepRuleObj),
        success: function (data) {
        },
        complete: function (xhr, status) {
            if (status == "success") {
                if (xhr.responseJSON.isValidationFailed) {
                    responseHandler(xhr.responseJSON);
                }
                else {
                    
                    renderRuleHtml(projID, cellID, ruleType, ruleID, ruleName, ruleDescription);
                    isRulePresent(app);
                    //let stepIDStepNameTaskId_Obj = new Object();
                    //stepIDStepNameTaskId_Obj.cellIDruleType = cellID + '_' + ruleType;
                    //stepIDStepNameTaskId_Obj.stepIDStepNameTaskId = stepIDStepNameTaskId;
                    //isQualified == false ? stepIDStepNameTaskId_Container.Novice.push(stepIDStepNameTaskId_Obj) : stepIDStepNameTaskId_Container.Qualified.push(stepIDStepNameTaskId_Obj);

                    responseHandler(xhr.responseJSON);
                    $('#rulesTaskProject').modal('hide')
                }
                pwIsf.removeLayer();
            }
        },
        error: function (data) {
            pwIsf.removeLayer();
        }
    })

}

//Enable add rule button
function enableAddRuleButton() {
    $('#addRuleButton').removeClass('btn-disabled').addClass('btn-success')
    $('#addRuleButton').attr('disabled', false);
}

//Edit rule in Temp Table
function editRuleInTempTable() {


    let rowObj = $('#allRulesTable input[name=allRules]:checked');
    let ruleID = rowObj.val();    
    let cellID = $('#cellIDforRules').val();
    let cellAttributes = app.graph.getCell(cellID).attributes;
    let stepName = cellAttributes.name;
    let taskID = $('#taskIDforRules').val();
    //let taskName = cellAttributes.action.split('@')[1];
    let urlParams = new URLSearchParams(window.location.search);
    let projID = urlParams.get("projID");
    let subID = urlParams.get("subId");
    let wfOwner = urlParams.get("wfownername").split('[')[0];
    let loggedInSignum = signumGlobal;
    let ruleName = rowObj.closest('tr').find('td:nth-child(2)').text();
    let ruleDescription = rowObj.closest('tr').find('td:nth-child(3)').text();
    let ruleType = $('#ruleType').val();
    //let isQualified = $("#assessed").is(':checked') ? false : true;
    //let stepIDStepNameTaskId_Obj = isQualified == false ?
    //    stepIDStepNameTaskId_Container.Novice.find(function (combo) { return combo.cellIDruleType == cellID + '_' + ruleType }) :
    //    stepIDStepNameTaskId_Container.Qualified.find(function (combo) { return combo.cellIDruleType == cellID + '_' + ruleType });


    let editStepRuleArray = [];
    let existingRule = new Object();
    let editStepRuleObj = new Object();

    //existingRule.stepIDStepNameTaskId = stepIDStepNameTaskId_Obj.stepIDStepNameTaskId;
    existingRule.stepID = cellID;
    existingRule.taskId = taskID;
    existingRule.projectIDSubactivityIDLoggedInSignum = projID + '_' + subID + '_' + loggedInSignum;
    existingRule.taskActionName = ruleType;
    existingRule.existing = true;
    existingRule.experiencedFlow = $('#multiModeCheck').prop('checked');

    editStepRuleArray.push(existingRule);

    editStepRuleObj.ruleId = rowObj.val();
    //editStepRuleObj.stepIDStepNameTaskId = cellID + '_' + stepName + '_' + taskID;
    editStepRuleObj.stepID = cellID;
    editStepRuleObj.taskId = taskID;
    editStepRuleObj.projectIDSubactivityIDLoggedInSignum = projID + '_' + subID + '_' + loggedInSignum;
    editStepRuleObj.taskActionName = ruleType;
    editStepRuleObj.parseRuleJson = rowObj.closest('tr').find('td:nth-child(1) input.ruleJson').val();
    editStepRuleObj.createdBy = signumGlobal;
    editStepRuleObj.modifiedBy = signumGlobal;
    editStepRuleObj.experiencedFlow = $('#multiModeCheck').prop('checked');

    editStepRuleArray.push(editStepRuleObj);

    let stepIDStepNameTaskId = cellID + '_' + stepName + '_' + taskID;

    let service_URL = service_java_URL + "autoSense/editStepRuleInAutoSenseTmpTable";
    pwIsf.addLayer({ text: 'Please wait ...' });

    $.isf.ajax({
        url: service_URL,
        type: 'POST',
        crossDomain: true,
        context: this,
        contentType: 'application/json',
        data: JSON.stringify(editStepRuleArray),
        success: function (data) {
        },
        complete: function (xhr, status) {
            if (status == "success") {
                   if (xhr.responseJSON.isValidationFailed) {
                    responseHandler(xhr.responseJSON);
                }
                   else {
                      
                    renderRuleHtml(projID, cellID, ruleType, ruleID, ruleName, ruleDescription);
                       isRulePresent(app);
                    //if (isQualified == false) {
                    //    let removeIndex = stepIDStepNameTaskId_Container.Novice.map(function (combo) { return combo.cellIDruleType }).indexOf(cellID + '_' + ruleType);
                    //    stepIDStepNameTaskId_Container.Novice.splice(removeIndex, 1);
                    //}
                    //else {
                    //    let removeIndex = stepIDStepNameTaskId_Container.Qualified.map(function (combo) { return combo.cellIDruleType }).indexOf(cellID + '_' + ruleType);
                    //    stepIDStepNameTaskId_Container.Qualified.splice(removeIndex, 1);
                    //}

                    //let stepIDStepNameTaskId_Obj = new Object();
                    //stepIDStepNameTaskId_Obj.cellIDruleType = cellID + '_' + ruleType;
                    //stepIDStepNameTaskId_Obj.stepIDStepNameTaskId = stepIDStepNameTaskId;
                    //isQualified == false ? stepIDStepNameTaskId_Container.Novice.push(stepIDStepNameTaskId_Obj) : stepIDStepNameTaskId_Container.Qualified.push(stepIDStepNameTaskId_Obj);

                    responseHandler(xhr.responseJSON);
                    $('#rulesTaskProject').modal('hide')
                }
                pwIsf.removeLayer();
            }
        },
        error: function (data) {
            pwIsf.removeLayer();
        }
    })   

}

//Delete Rule on change of Task
function confirmDeleteRule(cell, taskValueOld, taskValueNew) {

    var returnValue = ''

    let btnJson = {
        'Yes': {
            'action': function () {
                //let isStartRuleDeleted = false;
                //let isStopRuleDeleted = false;
                let startRule = cell.attributes.startRule;
                let stopRule = cell.attributes.stopRule;
                let taskIdOld = taskValueOld.split('@')[0];
                stepTaskValue = taskValueNew

                if (startRule == true && stopRule == true) {
                    deleteRuleInTempTable(cell.id, '', taskIdOld, true);
                }
                else if (startRule == true) {
                    //isStartRuleDeleted = deleteRuleInTempTable(cell.id, 'START', true);
                    deleteRuleInTempTable(cell.id, 'START', taskIdOld);
                }
                else {
                    //isStopRuleDeleted = deleteRuleInTempTable(cell.id, 'STOP', true);
                    deleteRuleInTempTable(cell.id, 'STOP', taskIdOld);
                }

                //if (startRule && stopRule) {
                //    if (isStartRuleDeleted && isStopRuleDeleted) {
                //        pwIsf.alert({ msg: 'All rules successfully deleted', type: 'success', autoClose: 3 });
                //    }
                //    else {
                //        pwIsf.alert({ msg: 'An error occured while deleted the rules', type: 'error' });
                //    }
                //}
                //else if (startRule) {
                //    if (isStartRuleDeleted) {
                //        pwIsf.alert({ msg: 'All rules successfully deleted', type: 'success', autoClose: 3 });
                //    }
                //    else {
                //        pwIsf.alert({ msg: 'An error occured while deleted the rules', type: 'error' });
                //    }
                //}
                //else if (stopRule){
                //    if (isStopRuleDeleted) {
                //        pwIsf.alert({ msg: 'All rules successfully deleted', type: 'success', autoClose: 3 });
                //    }
                //    else {
                //        pwIsf.alert({ msg: 'An error occured while deleted the rules', type: 'error' });
                //    }
                //}               

            },
            'class': 'btn btn-success'
        },
        'No': {
            'action': function () {
                if (taskValueOld != '') {
                    $('.app-body #taskList').val(taskValueOld).trigger('change');
                }
                else {
                    $('.app-body #taskList').val('').trigger('change');
                }
            },
            'class': 'btn btn-danger'
        }
    }
    let gg = pwIsf.confirm({
        title: 'Delete Rules',
        msg: window.location.href.includes('FlowChartWorkOrderAdd') ?
            'All the rules attached will be deleted. Are you sure you want to change taskID and delete the existing rules?' :
            'Autosense Rules are attached with this step for future reference. Are you sure you want to change the taskID and delete the autosense reference.',
        'buttons': btnJson
    });

    //while (true) {
        

    //    if (returnValue != '' && gg == false) {
    //        break
    //    }
    //}
    return returnValue;
}

//Delete Rule
function deleteRule(cellID, ruleType) {
  
    let btnJson = {
        'Yes': {
            'action': function () {
                deleteRuleInTempTable(cellID, ruleType);
            },
            'class': 'btn btn-success'
        },
        'No': {
            'action': function () {

            },
            'class': 'btn btn-danger'
        }
    }
    pwIsf.confirm({
        title: 'Delete Rule',
        msg: 'Are you sure you want to delete this rule?',
        'buttons': btnJson
    })
}

//Add or Edit Rule
function addOrUpdateRuleInTempTable() {
   
    if (!$('#addedRuleDiv').is(':hidden')) {
        let btnJson = {
            'Yes': {
                'action': function () {
                    editRuleInTempTable();
                },
                'class': 'btn btn-success'
            },
            'No': {
                'action': function () {

                },
                'class': 'btn btn-danger'
            }
        }

        let rowObj = $('#allRulesTable input[name=allRules]:checked');
        let ruleID = rowObj.val();

        if (ruleID != $('#ruleId').val()) {
            pwIsf.confirm({
                title: 'Edit Rule',
                msg: 'Are you sure? Previous rule will be updated with this new rule',
                'buttons': btnJson
            })
        }
        else {
            pwIsf.alert({ msg: 'This rule is already added. Please select a different rule', type: 'warning' });
        }       

    }
    else {
        saveRuleInTempTable();
    }
}

function isRulePresent(app) {
    let isMultiModeEnabled = $('#multiModeCheck').prop('checked');
    let ruleCells = app.graph.getCells().filter(function (cell) { return (cell.attributes.startRule == true || cell.attributes.stopRule == true) });

    if (ruleCells.length == 0) {
        

            $('#IsAutoSenseDisabled').prop({ 'disabled': true });
       
    }
    else {
        $('#IsAutoSenseDisabled').prop({ 'disabled': false });
    }



}

//Delete rule from temp table
function deleteRuleInTempTable(cellID, ruleType, taskIdOld = '', calledForBothRules = false) {
    
    //var returnValue = false;    
    let deleteStepRuleArr = [];
    let rowObj = $('#allRulesTable input[name=allRules]:checked');
    let cellAttributes = app.graph.getCell(cellID).attributes;
    let taskID = cellAttributes.action.split('@')[0];
    let stepName = cellAttributes.name;
    //let taskName = cellAttributes.action.split('@')[1];
    let urlParams = new URLSearchParams(window.location.search);
    let projID = urlParams.get("projID");
    let subID = urlParams.get("subId");
    let wfOwner = urlParams.get("wfownername").split('[')[0];
    let loggedInSignum = signumGlobal;
    let ruleName = rowObj.closest('tr').find('td:nth-child(2)').text();
    let ruleDescription = rowObj.closest('tr').find('td:nth-child(3)').text();
    //let isQualified = $("#assessed").is(':checked') ? false : true;
    
    
    if (!calledForBothRules) { //Delete logic only one rule

        let deleteStepRuleObj = new Object();

        //let stepIDStepNameTaskId_Obj = isQualified == false ?
        //    stepIDStepNameTaskId_Container.Novice.find(function (combo) { return combo.cellIDruleType == cellID + '_' + ruleType }) :
        //    stepIDStepNameTaskId_Container.Qualified.find(function (combo) { return combo.cellIDruleType == cellID + '_' + ruleType });

        //deleteStepRuleObj.stepIDStepNameTaskId = stepIDStepNameTaskId_Obj.stepIDStepNameTaskId;
        deleteStepRuleObj.stepID = cellID;
        deleteStepRuleObj.taskId = taskIdOld == '' ? taskID : taskIdOld;
        deleteStepRuleObj.projectIDSubactivityIDLoggedInSignum = projID + '_' + subID + '_' + loggedInSignum;
        deleteStepRuleObj.taskActionName = ruleType;
        deleteStepRuleObj.experiencedFlow = $('#multiModeCheck').prop('checked');
        deleteStepRuleArr.push(deleteStepRuleObj);
    }
    else { //Delete logic for both Rules

        let deleteStepRuleStartObj = new Object();
        let deleteStepRuleStopObj = new Object();

        //let stepIDStepNameTaskId_StartObj = isQualified == false ?  
        //    stepIDStepNameTaskId_Container.Novice.find(function (combo) { return combo.cellIDruleType == cellID + '_START' }) :
        //    stepIDStepNameTaskId_Container.Qualified.find(function (combo) { return combo.cellIDruleType == cellID + '_START' });

        //let stepIDStepNameTaskId_StopObj = isQualified == false ?
        //    stepIDStepNameTaskId_Container.Novice.find(function (combo) { return combo.cellIDruleType == cellID + '_STOP' }) :
        //    stepIDStepNameTaskId_Container.Qualified.find(function (combo) { return combo.cellIDruleType == cellID + '_STOP' });

        //deleteStepRuleStartObj.stepIDStepNameTaskId = stepIDStepNameTaskId_StartObj.stepIDStepNameTaskId;
        deleteStepRuleStartObj.stepID = cellID;
        deleteStepRuleStartObj.taskId = taskIdOld;
        deleteStepRuleStartObj.projectIDSubactivityIDLoggedInSignum = projID + '_' + subID + '_' + loggedInSignum;
        deleteStepRuleStartObj.taskActionName = 'START';
        deleteStepRuleStartObj.experiencedFlow = $('#multiModeCheck').prop('checked');
        deleteStepRuleArr.push(deleteStepRuleStartObj);

        //deleteStepRuleStopObj.stepIDStepNameTaskId = stepIDStepNameTaskId_StopObj.stepIDStepNameTaskId;
        deleteStepRuleStopObj.stepID = cellID;
        deleteStepRuleStopObj.taskId = taskIdOld;
        deleteStepRuleStopObj.projectIDSubactivityIDLoggedInSignum = projID + '_' + subID + '_' + loggedInSignum;
        deleteStepRuleStopObj.taskActionName = 'STOP';
        deleteStepRuleStopObj.experiencedFlow = $('#multiModeCheck').prop('checked');
        deleteStepRuleArr.push(deleteStepRuleStopObj);

    }

    let service_URL = service_java_URL + "autoSense/deleteStepRuleFromAutoSenseTmpTable";    

    $.isf.ajax({
        url: service_URL,
        type: 'POST',
        crossDomain: true,
        context: this,
        contentType: 'application/json',
        data: JSON.stringify(deleteStepRuleArr),
        beforeSend: function () {
            pwIsf.addLayer({ text: 'Please wait ...' });
        },
        success: function (data) {
        },
        complete: function (xhr, status) {
            if (status == "success") {
                if (xhr.responseJSON.isValidationFailed) {
                    //if (calledForBothRules) {
                    //    returnValue = false;
                    //}
                    //else {
                    //    responseHandler(xhr.responseJSON);
                    //}
                    
                    responseHandler(xhr.responseJSON);
                }
                else {
                    if (taskIdOld == '') { //Delete logic only one rule
                        renderNoRuleHtml(projID, cellID, ruleType);
                    }
                    else { //Delete logic for both Rules
                        renderNoRuleHtml(projID, cellID, 'START');
                        renderNoRuleHtml(projID, cellID, 'STOP');
                    }
                    isRulePresent(app);
                    //if (isQualified == false) {
                    //    if (!calledForBothRules) { //Delete logic only one rule
                    //        let removeIndex = stepIDStepNameTaskId_Container.Novice.map(function (combo) { return combo.cellIDruleType }).indexOf(cellID + '_' + ruleType);
                    //        stepIDStepNameTaskId_Container.Novice.splice(removeIndex, 1);
                    //    }
                    //    else { //Delete logic for both Rules
                    //        let removeIndexStart = stepIDStepNameTaskId_Container.Novice.map(function (combo) { return combo.cellIDruleType }).indexOf(cellID + '_START');
                    //        stepIDStepNameTaskId_Container.Novice.splice(removeIndexStart, 1);

                    //        let removeIndexStop = stepIDStepNameTaskId_Container.Novice.map(function (combo) { return combo.cellIDruleType }).indexOf(cellID + '_STOP');
                    //        stepIDStepNameTaskId_Container.Novice.splice(removeIndexStop, 1);
                    //    }
                    //}
                    //else {
                    //    if (!calledForBothRules) { //Delete logic only one rule
                    //        let removeIndex = stepIDStepNameTaskId_Container.Qualified.map(function (combo) { return combo.cellIDruleType }).indexOf(cellID + '_' + ruleType);
                    //        stepIDStepNameTaskId_Container.Qualified.splice(removeIndex, 1);
                    //    }
                    //    else { //Delete logic for both Rules
                    //        let removeIndexStart = stepIDStepNameTaskId_Container.Qualified.map(function (combo) { return combo.cellIDruleType }).indexOf(cellID + '_START');
                    //        stepIDStepNameTaskId_Container.Novice.splice(removeIndexStart, 1);

                    //        let removeIndexStop = stepIDStepNameTaskId_Container.Qualified.map(function (combo) { return combo.cellIDruleType }).indexOf(cellID + '_STOP');
                    //        stepIDStepNameTaskId_Container.Novice.splice(removeIndexStop, 1);
                    //    }
                    //}

                    $('#addedRuleDiv').hide();
                    $('#ruleId').val('');
                    $('#ruleLinkDiv').css('margin-bottom', '15px');
                    //if (calledForBothRules) {
                    //    returnValue = true;
                    //}
                    //else {
                    //    responseHandler(xhr.responseJSON);
                    //}
                    responseHandler(xhr.responseJSON);
                }
                pwIsf.removeLayer();
            }
        },
        error: function (data) {
            //if (calledForBothRules) {
            //    pwIsf.removeLayer();
            //    returnValue = false;
            //}
            //else {
            //    pwIsf.removeLayer();
            //}
            pwIsf.removeLayer();
        }
    });
    //if (calledForBothRules) {
    //    return returnValue;
    //}
}

//Truncate string with minimum width
function truncate(string, length) {
    let stringMod = string;
    let textWidth = getTextWidth(stringMod);

    if (textWidth > length) {
        while (getTextWidth(stringMod) > length) {
            stringMod = stringMod.substring(0, stringMod.length - 1);
        }
        return stringMod + '...';
    }
    else {
        return string;
    }

}

//Get the width of given string
function getTextWidth(string) {

    let text = document.createElement("span");

    document.body.appendChild(text);

    text.style.height = 'auto';
    text.style.width = 'auto';
    text.style.position = 'absolute';
    text.style.whiteSpace = 'no-wrap';
    text.style.visiblity = 'hidden';
    text.innerHTML = string;

    let width = Math.ceil(text.clientWidth);

    document.body.removeChild(text);

    return width;

}

//Create html for new rule in inspector
function renderNoRuleHtml(projID, cellID, ruleType) {
    let html = '';
    let ruleObj = new Object();

    ruleObj.ruleId = '';
    ruleObj.ruleName = '';
    ruleObj.ruleDescription = '';

    if (ruleType == 'START') {

        html += `<label>Start Rule</label>
                            <span><button class="btn btn-success btn-xs aqua-tooltip" title="Click to add a start rule" data-toggle="tooltip" onclick="getAllRulesByProjectAndTask(${projID + ',\'' + cellID + '\',\'' + ruleType + '\''})">
                                <i class="fa fa-plus"></i>
                            </button></span>`

        $('.app-body #startRuleActivityDiv').html('').html(html);

        app.graph.getCell(cellID).set('startRule', false);
        app.graph.getCell(cellID).set('startRuleObj', ruleObj);
    }
    else {

        html += `<label>Stop Rule</label>
                            <span><button class="btn btn-success btn-xs aqua-tooltip" title="Click to add a stop rule" data-toggle="tooltip" onclick="getAllRulesByProjectAndTask(${projID + ',\'' + cellID + '\',\'' + ruleType + '\''})">
                                <i class="fa fa-plus"></i>
                            </button></span>`

        $('.app-body #stopRuleActivityDiv').html('').html(html);

        app.graph.getCell(cellID).set('stopRule', false);
        app.graph.getCell(cellID).set('stopRuleObj', ruleObj);
    }
}

//Create html for old(already added) rule in inspector
function renderRuleHtml(projID, cellID, ruleType, ruleID, ruleName, ruleDescription) {

    let html = '';
    let ruleObj = new Object();

    ruleObj.ruleId = ruleID;
    ruleObj.ruleName = ruleName;
    ruleObj.ruleDescription = ruleDescription;

    if (ruleType == 'START') {

        html += `<label>Start Rule</label>
                            <span>
                                <a class="aqua-tooltip" data-toggle="tooltip" title="${ruleName}" style="font-weight: bolder; margin-right: 5px; cursor: pointer;" onclick="getAllRulesByProjectAndTask(${projID + ',\'' + cellID + '\', \'' + ruleType + '\''})">${truncate(ruleName, 80)}</a>
                                    <button class="btn btn-danger btn-xs aqua-tooltip" title="Click to delete this rule" data-toggle="tooltip" onclick="deleteRule(${'\'' + cellID + '\',\'' + ruleType + '\''})"><i class="fa fa-trash"></i></button>
                            </span>`;

        $('.app-body #startRuleActivityDiv').html('').html(html);

        app.graph.getCell(cellID).set('startRule', true);
        app.graph.getCell(cellID).set('startRuleObj', ruleObj);

    }
    else {

        html += `<label>Stop Rule</label>
                            <span>
                                <a class="aqua-tooltip" data-toggle="tooltip" title="${ruleName}" style="font-weight: bolder; margin-right: 5px; cursor: pointer;" onclick="getAllRulesByProjectAndTask(${projID + ',\'' + cellID + '\', \'' + ruleType + '\''})">${truncate(ruleName, 80)}</a>
                                    <button class="btn btn-danger btn-xs aqua-tooltip" title="Click to delete this rule" data-toggle="tooltip" onclick="deleteRule(${'\'' + cellID + '\',\'' + ruleType + '\''})"><i class="fa fa-trash"></i></button>
                            </span>`;

        $('.app-body #stopRuleActivityDiv').html('').html(html);

        app.graph.getCell(cellID).set('stopRule', true);
        app.graph.getCell(cellID).set('stopRuleObj', ruleObj);

    }

}

//Delete existing rules from Temp Table
function deleteExistingRulesFromTempTable(version, subActivityDefIDArr) {

    let urlParams = new URLSearchParams(window.location.search);
    let projID = urlParams.get("projID");
    let subID = urlParams.get("subId");
    let wfOwner = urlParams.get("wfownername").split('[')[0];
    let loggedInSignum = signumGlobal;
    let projectIDSubactivityIDLoggedInSignum = projID + '_' + subID + '_' + loggedInSignum;

    let service_URL = service_java_URL + "autoSense/deleteDataAutoSenseTempTable?projectIDSubactivityIDLoggedInSignum=" + projectIDSubactivityIDLoggedInSignum;
    //let service_URL = service_java_URL + "autoSense/editStepRuleInAutoSenseTmpTable";

    $.isf.ajax({
        url: service_URL,
        type: 'POST',
        crossDomain: true,
        context: this,
        async: false,
        contentType: 'application/json',
        success: function (data) {
            console.log('Success in test');
        },
        complete: function (xhr, status) {
            if (status == "success") {
                if (version != '0') {
                    addWfDataToTempTable(subActivityDefIDArr);
                }
                //if (xhr.responseJSON.isValidationFailed) {
                //    //responseHandler(xhr.responseJSON);
                //}
                //else {
                //    if (version != '0') {
                //        for (let i in subActivityDefIDArr) {
                //            addWfDataToTempTable(subActivityDefIDArr[i]);
                //        }
                //    }                  
                //}
            }
            pwIsf.removeLayer();
        },
        error: function (data) {
            console.log('error in test');
        }
    })
}

//Add rules in temp table from master table based on def ids
function addWfDataToTempTable(subActivityDefIDArr) {

    let defIdString = '';
    if (subActivityDefIDArr.length > 1) {
        defIdString += subActivityDefIDArr[0] + ',' + subActivityDefIDArr[1];
    }
    else {
        defIdString += subActivityDefIDArr[0];
    }

    let service_URL = service_java_URL + "autoSense/saveWfDataAutoSenseTempTable?subActivityFlowchartDefId=" + defIdString;

    $.isf.ajax({
        url: service_URL,
        type: 'POST',
        crossDomain: true,
        context: this,
        contentType: 'application/json',
        success: function (data) {
            console.log('Success in test');
        },
        complete: function (xhr, status) {
            if (status == "success") {
                if (xhr.responseJSON.isValidationFailed) {
                    //responseHandler(xhr.responseJSON);
                }
                else {

                }
            }
        },
        error: function (data) {
            console.log('error in test');
        }
    })
}

// Adds a header row to the table and returns the set of columns.
// Need to do union of keys from all records as some records may not contain
// all records.
function addAllColumnHeaders(myList, selector) {
    var columnSet = [];
    var headerTr = $('<tr/>');

    for (var i = 0; i < myList.length; i++) {
        var rowHash = myList[i];
        for (var key in rowHash) {
            if ($.inArray(key, columnSet) == -1) {
                columnSet.push(key);
                headerTr.append($('<th/>').html(key.toUpperCase()));
            }
        }
    }
    $(selector).append(headerTr);

    return columnSet;
}
