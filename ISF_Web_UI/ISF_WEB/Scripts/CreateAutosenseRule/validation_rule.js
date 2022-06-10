const appScanner = 'html.AppScan';
const fileSystemScanner = 'html.FileScan';
const emailScanner = 'html.EmailScan';
const appMatch = 'html.AppMatch';
const fileSystemMatch = 'html.FileMatch';
const emailMatch = 'html.EmailMatch';
const appCondition = 'html.AppCondition';
const fileSystemCondition = 'html.FileCondition';
const emailCondition = 'html.EmailCondition';
const connectorNotification = 'html.ConnectorNotification';
const connectorCondition = 'html.ConnectorCondition';
const scannerType = 'Scanner';
const conditionType = 'Condition';
const matchType = 'Match';
const notificationType = 'Notification';
const link = 'standard.Link';
//const showValidateAdjustBtn = true;

function getElementCountByTypeNameAutosense(typeName) {

    let allElements = graph.toJSON();
        
    let count = 0;
    for (let i in allElements['cells']) {
        if (allElements['cells'][i]['typeName'] == typeName) {
            count++;
        }
    }
    return count;
}

function getElementCountByTypeAutosense(type) {

    let allElements = graph.toJSON();

    let count = 0;
    for (let i in allElements['cells']) {
        if (allElements['cells'][i]['type'] == type) {
            count++;
        }
    }
    return count;
}

//function removeElementIfCountGreaterThanAutosense(cell, elements = []) {

//    for (let i in elements) {
//        if (getElementCountByTypeNameAutosense(elements[i]['elName']) > elements[i]['elCount']) {

//           graph.removeCells(cell);
//        }
//    }

//}

function getElementTypeAutosense(cell) {
    return cell['attributes']['type'];
}

function getElementTypeNameAutosense(cell) {
    return cell['attributes']['typeName'];
}

//function callAfterAddElementOnPaperAutosense(cell, collection, opt) {


//    if (cell.attributes.typeName == notificationType) {
//        removeElementIfCountGreaterThanAutosense(cell, [{ elCount: 1, elName: notificationType }]);
//    }

//}

function unhighlightAllCellsAutosense() {

    let allCells = graph.getCells();
        

    for (let cel in allCells) {
        let cell = allCells[cel];
        errorUnHighlightAutosense(cell);

    }

}


function errorUnHighlightAutosense(currentCell) {// UNHIGHLIGHT SINGLE CELL

    var cellView = paper.findViewByModel(currentCell);
        
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

function errorHighlightAutosense(currentCell) { // HIGHLIGHT SINGLE CELL
    var cellView = paper.findViewByModel(currentCell);
       // document.getElementById('iframeAutosenseRule').contentWindow.paper.findViewByModel(currentCell);

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



function validateRuleJsonAutosense() {
    let errorsArr = [];
    let allCells = graph.getCells();


    if (getElementCountByTypeNameAutosense(notificationType) < 1) {

        errorsArr.push({ errorDesc: 'Minimum 1 Success Action mandatory for the rule.', actionText: 'Add or Remove manualy', elId: 0 });
    }

    if (getElementCountByTypeNameAutosense(scannerType) > 5) {
        errorsArr.push({ errorDesc: 'Maximum five Scanners allowed for the rule.', actionText: 'Add or Remove manualy', elId: 0 });
    }    

    if (getElementCountByTypeNameAutosense(scannerType) < 1) {

        errorsArr.push({ errorDesc: 'Minimum 1 Scanner mandatory for the rule.', actionText: 'Add or Remove manualy', elId: 0 });
    }

    if (getElementCountByTypeNameAutosense(conditionType) > 5) {

        errorsArr.push({ errorDesc: 'Maximum 5 AND/OR Condition allowed for the rule.', actionText: 'Add or Remove manualy', elId: 0 });
    }

    if (getElementCountByTypeNameAutosense(notificationType) > 1) {

        errorsArr.push({ errorDesc: 'Minimum 1 Success Action allowed for the rule.', actionText: 'Add or Remove manualy', elId: 0 });
    }

    if (getElementCountByTypeAutosense(connectorCondition) > 1) {
        errorsArr.push({ errorDesc: 'Maximum 1 Scanner Connector allowed for the rule.', actionText: 'Add or Remove manualy', elId: 0 });
    }   

    //Check nesting and child of scanners. 
    if (getElementCountByTypeNameAutosense(scannerType) > 1) {
        let nestingCounter = 0;
        let linkCounterScan = 0;
        let linkCount = getElementCountByTypeNameAutosense(scannerType) - 1;
        let scannerConnectorCount = getElementCountByTypeAutosense(connectorCondition);
        let scannerList = allCells.filter(function (el) { return el.attributes.typeName == scannerType });
         
        for (let i in scannerList) {
            let cell = scannerList[i];
            //let elementId = allCells[i]['id'];
            //let actText = 'Fixit';
            let childCountForScanner = 0;

            let inboundConnectedLinks = graph.getConnectedLinks(scannerList[i], { inbound: true });
            let outboundConnectedLinks = graph.getConnectedLinks(scannerList[i], { outbound: true });

            if (getElementTypeAutosense(cell) == appScanner || getElementTypeAutosense(cell) == fileSystemScanner || getElementTypeAutosense(cell) == emailScanner) {

                if (inboundConnectedLinks.length != 0) {
                    for (let i in inboundConnectedLinks) {
                        let sourceLink = inboundConnectedLinks[i];
                        let sourceEle = sourceLink.getSourceElement();

                        if (sourceEle.prop('type') === connectorCondition && sourceLink.attributes.target.port === 'r') {
                            linkCounterScan++;
                        }
                    }
                }
                if (outboundConnectedLinks.length != 0) {
                    for (let i in outboundConnectedLinks) {

                        let targetLink = outboundConnectedLinks[i];
                        let targetElement = targetLink.getTargetElement();
                        if (targetLink.attributes.source.port == 'out' && targetLink.attributes.target.port == 'in' && targetElement.attributes.typeName == scannerType) {
                            childCountForScanner++;
                            if (childCountForScanner > 1) {
                                errorsArr.push({
                                    errorDesc: `Only 1 child allowed for nesting scanners`});
                                break;
                            }
                        }
                        //let targetLink = inboundConnectedLinks[i];
                        if (targetLink.attributes.source.port == 'out' && targetLink.attributes.target.port == 'in') {
                            let targetElement = targetLink.getTargetElement();
                            if (targetElement.attributes.typeName == scannerType) {
                                nestingCounter++;
                                linkCounterScan++;
                            }
                        }
                        if (targetLink.attributes.source.port == 'r' && targetLink.attributes.target.port == 'in') {
                            let targetElement = targetLink.getTargetElement();
                            if (targetElement.attributes.typeName == conditionType) {
                                linkCounterScan++;

                            }
                        }
                    }
                }
            }
        }
        if (nestingCounter > 2) {

            errorsArr.push({
                errorDesc: `Only 2 level of nesting allowed for scanners`});

        }
        else if (scannerConnectorCount === 1 && getElementCountByTypeNameAutosense(scannerType) > linkCounterScan) {
            errorsArr.push({
                errorDesc: `Scanner must be connected to other scanner directly or via scanner connector.`
            });            
        }
        else if (scannerConnectorCount == 0 && linkCounterScan < linkCount) {
            errorsArr.push({
                errorDesc: `Scanner must be connected to other scanner directly or via scanner connector.`
            });
        }
    }
   

    for (let cel in allCells) {
        let cell = allCells[cel];
        let elementType = getElementTypeAutosense(cell);
        let elementId = allCells[cel]['id'];
        let actText = 'Fixit';

        let inboundConnectedLinks = graph.getConnectedLinks(allCells[cel], { inbound: true });

        let outboundConnectedLinks = graph.getConnectedLinks(allCells[cel], { outbound: true });


        //Start : Validate for Scanner Connector
        if (elementType == connectorCondition) {

            let sourceElement = ''; let targetElement = '';
            let scannerConnectorErrorFlag = false; let sourcePort = '';
            let inputOutputCount = 0;

            (inboundConnectedLinks.length != 0) ? sourceElement = inboundConnectedLinks[0].getSourceElement() : sourceElement = '';
            (outboundConnectedLinks.length != 0) ? targetElement = outboundConnectedLinks[0].getTargetElement() : targetElement = '';


            if (outboundConnectedLinks.length != 0 || inboundConnectedLinks.length != 0) {
                inputOutputCount = outboundConnectedLinks.length + inboundConnectedLinks.length;
                if (inputOutputCount > 3) {
                    errorsArr.push({
                        errorDesc: `Scanner Connector can have connections to max 3 Scanners`, actionText: actText, elId: elementId
                    });
                    scannerConnectorErrorFlag = true;
                }
            }
            else {
                errorsArr.push({
                    errorDesc: `Scanner Connector needs to connected`, actionText: actText, elId: elementId
                });
                scannerConnectorErrorFlag = true;

            }

            if (inboundConnectedLinks.length != 0) {
                errorsArr.push({
                            errorDesc: `Scanner Connector cannot have Inbound Links. `, actionText: actText, elId: elementId
                        });
                scannerConnectorErrorFlag = true;

              
            }

            if (outboundConnectedLinks.length != 0) {
                //let scannerCount = 0;
                for (let c in outboundConnectedLinks) {
                    targetPort = outboundConnectedLinks[c];
                    let sourceElement = outboundConnectedLinks[c].getSourceElement();
                    let targetElement = outboundConnectedLinks[c].getTargetElement();
                  
                    if (targetElement.attributes.type == fileSystemMatch || targetElement.attributes.type == emailMatch || targetElement.attributes.type == appMatch) {
                        errorsArr.push({
                            errorDesc: `Scanner Connector cannot be connected to AppMatch ,FSMatch and Email Match`, actionText: actText, elId: elementId
                        });
                        scannerConnectorErrorFlag = true; break;
                    }
                    if (targetElement.attributes.type == fileSystemCondition || targetElement.attributes.type == appCondition || targetElement.attributes.type == emailCondition) {
                        errorsArr.push({
                            errorDesc: `Scanner Connector cannot be connected to App Condition,FS Condition and Email Condition`, actionText: actText, elId: elementId
                        });
                        scannerConnectorErrorFlag = true; break;
                    }
             
                    if (targetElement.attributes.typeName == scannerType && targetPort.attributes.target.port != 'r') {
                        errorsArr.push({
                            errorDesc: `Scanner Connector can be connected only to 'R' port of Scanners `, actionText: actText, elId: elementId
                        });
                        scannerConnectorErrorFlag = true; break;
                    }
                }
            }



            if (scannerConnectorErrorFlag) {
                errorHighlightAutosense(cell);
            }


        }
        //End: Validate for Scanner Connector
      

        //Start : Validate for App Scanner
        if (elementType == appScanner) {
            let sourceElement = ''; let targetElement = '';
            let appScannerErrorFlag = false;
            let linkCounter = 0;
            let countOR, countAnd = 0;

            (inboundConnectedLinks.length != 0) ? sourceElement = inboundConnectedLinks[0].getSourceElement() : sourceElement = '';
            (outboundConnectedLinks.length != 0) ? targetElement = outboundConnectedLinks[0].getTargetElement() : targetElement = '';

        
            if (inboundConnectedLinks.length != 0) {

                for (i in inboundConnectedLinks) {

                    let sourcePort = inboundConnectedLinks[i];
                    let target = sourcePort.getTargetElement();
                    let source = sourcePort.getSourceElement();

                    if (sourcePort.attributes.target.port == 'r') {
                        if ((source.attributes.type != connectorCondition)) {
                            errorsArr.push({
                                errorDesc: `App Scanners can only be linked to Scanner connector through Port R`, actionText: actText, elId: elementId
                            });
                            appScannerErrorFlag = true;
                        }
                        if (sourcePort.attributes.target.port == 'r' && source.attributes.type == connectorCondition) {
                            for (j in inboundConnectedLinks) {
                                let sPort = inboundConnectedLinks[j];
                                let targetEle = inboundConnectedLinks[j].getTargetElement();
                                if (sPort.attributes.source.port == 'out' && sPort.attributes.target.port == 'in' && targetEle.attributes.typeName == scannerType) {

                                    errorsArr.push({
                                        errorDesc: `When R port is connected to Scanner Connector, output port of source element cannot be connected to Scanner element's input port.`, actionText: actText, elId: elementId
                                    });
                                    appScannerErrorFlag = true;
                                }
                            }
                        }
                    }
                }
            }

            if (outboundConnectedLinks.length != 0) {
                //Check if  outbound link from 'and' and 'or' ports
                 countOR = outboundConnectedLinks.filter(function (el) { return (el.attributes.source.port == 'or') });
                 countAnd = outboundConnectedLinks.filter(function (el) { return (el.attributes.source.port == 'and') })

                if (countOR.length != 0 && countAnd.length != 0) {
                        errorsArr.push({
                            errorDesc: `Both AND and OR ports cannot be used for linking at the same time`, actionText: actText, elId: elementId
                        });
                        appScannerErrorFlag = true;
                    }
                if (outboundConnectedLinks.length > 1) {
                    let matchCount = 0;
                    let conditionCount = 0;
                        for (let c in outboundConnectedLinks) {
                            let appSCell = outboundConnectedLinks[c];
                            let getAppSLinkTarget = appSCell.getTargetElement();
                            if (getAppSLinkTarget.attributes.type == appMatch) {
                                matchCount++;
                            }
                            if (getAppSLinkTarget.attributes.type == appCondition) {
                                conditionCount++;
                            }
                        }
                        if (matchCount > 5) {

                            errorsArr.push({
                                errorDesc: `Application Scanner can be connected to max 5 App Match`, actionText: actText, elId: elementId
                            });
                            appScannerErrorFlag = true;
                    }

                    if (conditionCount > 1) {

                        errorsArr.push({
                            errorDesc: `Application Scanner can be connected to max 1 App Condition`, actionText: actText, elId: elementId
                        });
                        appScannerErrorFlag = true;
                    }
                    }
                
            }

            if (outboundConnectedLinks.length == 0 && inboundConnectedLinks.length == 0) {
                errorsArr.push({
                    errorDesc: `Please create links for AppScanner to successfully save rules`, actionText: actText, elId: elementId
                });
                appScannerErrorFlag = true;

            }

            if (allCells[cel].attributes.fields.app == undefined || allCells[cel].attributes.fields.app == '') {
                errorsArr.push({
                    errorDesc: `All mandatory fields on the App Scanner must be filled properly`, actionText: actText, elId: elementId
                });
                appScannerErrorFlag = true;
            }

            let regExp = /[\\"]/;
            let customAppVal = allCells[cel].attributes.fields.customApp;
            if (customAppVal.length != 0 && regExp.test(customAppVal) == true) {

                errorsArr.push({
                   errorDesc: `Custom App value cannot have \\ (backslash) and " (double quotes)`, actionText: actText, elId: elementId
                });
                appScannerErrorFlag = true;

            }
             
            if (appScannerErrorFlag) {
                errorHighlightAutosense(cell);
            }
        }
        //End : Validate for App Scanner

        //Start : Validate for FS Scanner
        if (elementType == fileSystemScanner) {
            let sourceElement = ''; let targetElement = '';
            let fsScannerErrorFlag = false;
            let countOR, countAnd = 0;

            (inboundConnectedLinks.length != 0) ? sourceElement = inboundConnectedLinks[0].getSourceElement() : sourceElement = '';
            (outboundConnectedLinks.length != 0) ? targetElement = outboundConnectedLinks[0].getTargetElement() : targetElement = '';

            if (outboundConnectedLinks != 0) {
                let matchCount = 0;
                for (let i in outboundConnectedLinks) {
                    let targetPort = outboundConnectedLinks[i];
                    let targetElement = targetPort.getTargetElement();
                   
                    if ((targetPort.attributes.source.port == 'and' || targetPort.attributes.source.port == 'or') && targetPort.attributes.target.port == 'in') {

                        if (targetElement.attributes.type == fileSystemMatch) {

                            matchCount++;
                        }
                        
                        else if (targetElement.get('type') == fileSystemCondition) {
                            let targetOfCondition = graph.getNeighbors(targetElement, { outbound: true })[0];
                            if (targetOfCondition != undefined && targetOfCondition.attributes.type == fileSystemMatch) {
                                matchCount++;
                            }
                        }
                    }
                    
                }
                if (matchCount < 1) {
                    errorsArr.push({ errorDesc: 'Minimum 1 FS Match is mandatory for each FS Scanner.', actionText: 'Add or Remove manualy', elId: 0 });
                    fsScannerErrorFlag = true;

                }

            }
            else {

                errorsArr.push({ errorDesc: 'Please Connect the FS Scanner to another scanner or FS Match', actionText: 'Add or Remove manualy', elId: 0 });
                fsScannerErrorFlag = true;

            }

            if (inboundConnectedLinks.length != 0) {

                for (i in inboundConnectedLinks) {

                    let sourcePort = inboundConnectedLinks[i];
                    let target = sourcePort.getTargetElement();
                    let source = sourcePort.getSourceElement();

                    if (sourcePort.attributes.target.port == 'r') {
                        if ((source.attributes.type != connectorCondition)) {
                            errorsArr.push({
                                errorDesc: `File Scanners can only be linked to Scanner connector through Port R`, actionText: actText, elId: elementId
                            });
                            fsScannerErrorFlag = true;
                        }
                        if (sourcePort.attributes.target.port == 'r' && source.attributes.type == connectorCondition) {
                            for (j in inboundConnectedLinks) {
                                let sPort = inboundConnectedLinks[j];
                                let targetEle = inboundConnectedLinks[j].getTargetElement();
                                if (sPort.attributes.source.port == 'out' && sPort.attributes.target.port == 'in' && targetEle.attributes.typeName == scannerType) {

                                    errorsArr.push({
                                        errorDesc: `When R port is connected to Scanner Connector, output port of source element cannot be connected to Scanner element's input port.`, actionText: actText, elId: elementId
                                    });
                                    fsScannerErrorFlag = true;
                                }
                            }
                        }
                    }
                }
            }
            if (outboundConnectedLinks.length != 0) {     

                //Check if  outbound link from 'and' and 'or' ports
                
                    countOR = outboundConnectedLinks.filter(function (el) { return (el.attributes.source.port == 'or') });
                    countAnd = outboundConnectedLinks.filter(function (el) { return (el.attributes.source.port == 'and') })

                 if (countOR.length != 0 && countAnd.length != 0) {
                        errorsArr.push({
                            errorDesc: `Both AND and OR ports cannot be used for linking at the same time`, actionText: actText, elId: elementId
                        });
                        fsScannerErrorFlag = true;
                    }    

                if (outboundConnectedLinks.length > 1) {
                    let matchCount = 0;
                    let conditionCount = 0;
                    for (let c in outboundConnectedLinks) {
                        let appSCell = outboundConnectedLinks[c];
                        let getAppSLinkTarget = appSCell.getTargetElement();
                        if (getAppSLinkTarget.attributes.type == fileSystemMatch) {
                            matchCount++;
                        }
                        if (getAppSLinkTarget.attributes.type == fileSystemCondition) {
                            conditionCount++;
                        }
                    }
                    if (matchCount > 5) {

                        errorsArr.push({
                            errorDesc: `File Syatem Scanner can be connected to max 5 File System Match`, actionText: actText, elId: elementId
                        });
                        fsScannerErrorFlag = true;
                    }

                    if (conditionCount > 1) {

                        errorsArr.push({
                            errorDesc: `File System Scanner can be connected to max 1 File System Condition`, actionText: actText, elId: elementId
                        });
                        fsScannerErrorFlag = true;
                    }
                }
            }

            if (inboundConnectedLinks.length == 0 && outboundConnectedLinks.length == 0) {
           
                errorsArr.push({
                    errorDesc: `Please create links for File Scanner to successfully save rules`, actionText: actText, elId: elementId
                });
                fsScannerErrorFlag = true;

            }

            if (allCells[cel].attributes.fields.app == undefined || allCells[cel].attributes.fields.app == '-1' || allCells[cel].attributes.fields.app == '') {
                errorsArr.push({
                    errorDesc: `All mandatory fields on the File Scanner must be filled properly`, actionText: actText, elId: elementId
                });
                fsScannerErrorFlag = true;

            }
            if (allCells[cel].attributes.fields.app == 'Absolute Path') {
                if (allCells[cel].attributes.fields.customPath == '' || allCells[cel].attributes.fields.customPath == undefined || allCells[cel].attributes.fields.customPath == null) {

                    errorsArr.push({
                        errorDesc: `For Absolute Path option input field path cannot be empty`, actionText: actText, elId: elementId
                    });
                    fsScannerErrorFlag = true;
                }

                let regExp = /[\\"]/;
                let customPathVal = allCells[cel].attributes.fields.customPath;
                if (customPathVal.length != 0 && regExp.test(customPathVal) == true) {

                    errorsArr.push({
                        errorDesc: `Custom Path value cannot have \\ (backslash) and " (double quotes)`, actionText: actText, elId: elementId
                    });
                    fsScannerErrorFlag = true;

                }

            }


            if (fsScannerErrorFlag) {
                errorHighlightAutosense(cell);
            }


        }
        //End : Validate for FS Scanner

        //Start : Validate for Email Scanner
        if (elementType == emailScanner) {
            let sourceElement = ''; let targetElement = '';
            let emailScannerErrorFlag = false;
            let countOR, countAnd = 0;

            (inboundConnectedLinks.length != 0) ? sourceElement = inboundConnectedLinks[0].getSourceElement() : sourceElement = '';
            (outboundConnectedLinks.length != 0) ? targetElement = outboundConnectedLinks[0].getTargetElement() : targetElement = '';


            if (outboundConnectedLinks != 0) {
                let matchCount = 0;
                for (let i in outboundConnectedLinks) {
                    let targetPort = outboundConnectedLinks[i];
                    let targetElement = targetPort.getTargetElement();
                    if ((targetPort.attributes.source.port == 'and' || targetPort.attributes.source.port == 'or') && targetPort.attributes.target.port == 'in') {

                        if (targetElement.attributes.type == emailMatch) {

                            matchCount++;
                        }
                        else if (targetElement.get('type') == emailCondition) {
                            let targetOfCondition = graph.getNeighbors(targetElement, { outbound: true })[0];
                            if (targetOfCondition != undefined && targetOfCondition.attributes.type == emailMatch) {
                                matchCount++;
                            }
                        }
                }
                }
                if (matchCount < 1) {
                    errorsArr.push({ errorDesc: 'Minimum 1 Email Match is mandatory for each Email Scanner.', actionText: 'Add or Remove manualy', elId: 0 });
                    emailScannerErrorFlag = true;

                }

            }
            else {

                errorsArr.push({ errorDesc: 'Please Connect the Email Scanner to another scanner or Email Match', actionText: 'Add or Remove manualy', elId: 0 });
                emailScannerErrorFlag = true;

            }

            if (outboundConnectedLinks.length != 0 || inboundConnectedLinks.length != 0) {

                if (inboundConnectedLinks.length != 0) {

                    for (i in inboundConnectedLinks) {

                        let sourcePort = inboundConnectedLinks[i];
                        let target = sourcePort.getTargetElement();
                        let source = sourcePort.getSourceElement();

                        if (sourcePort.attributes.target.port == 'r') {
                            if ((source.attributes.type != connectorCondition)) {
                                errorsArr.push({
                                    errorDesc: `Email Scanners can only be linked to Scanner connector through Port R`, actionText: actText, elId: elementId
                                });
                                emailScannerErrorFlag = true;
                            }
                            if (sourcePort.attributes.target.port == 'r' && source.attributes.type == connectorCondition) {
                                for (j in inboundConnectedLinks) {
                                    let sPort = inboundConnectedLinks[j];
                                    let targetEle = inboundConnectedLinks[j].getTargetElement();
                                    if (sPort.attributes.source.port == 'out' && sPort.attributes.target.port == 'in' && targetEle.attributes.typeName == scannerType) {

                                        errorsArr.push({
                                            errorDesc: `When R port is connected to Scanner Connector, output port of source element cannot be connected to Scanner element's input port.`, actionText: actText, elId: elementId
                                        });
                                        emailScannerErrorFlag = true;
                                    }
                                }
                            }
                        }
                    }
                }

                if (outboundConnectedLinks.length != 0) {

                    countOR = outboundConnectedLinks.filter(function (el) { return (el.attributes.source.port == 'or') });
                    countAnd = outboundConnectedLinks.filter(function (el) { return (el.attributes.source.port == 'and') })

                    if (countOR.length != 0 && countAnd.length != 0) {
                        errorsArr.push({
                            errorDesc: `Both AND and OR ports cannot be used for linking at the same time`, actionText: actText, elId: elementId
                        });
                        emailScannerErrorFlag = true;
                    }

                    if (allCells[cel].attributes.fields.lookForPastEmail == undefined || allCells[cel].attributes.fields.timeField == undefined || allCells[cel].attributes.fields.timeField == '') {
                        errorsArr.push({
                            errorDesc: `All mandatory fields on the Email Scanner must be filled properly`, actionText: actText, elId: elementId
                        });
                        emailScannerErrorFlag = true;

                    }

                    let time = parseFloat(allCells[cel].attributes.fields.timeField);
                    if (time % 1 != 0) {

                        errorsArr.push({
                            errorDesc: `Decimal entry is not allowd for Time in Email scanner`, actionText: actText, elId: elementId
                        });
                        emailScannerErrorFlag = true;

                    }



                    if (outboundConnectedLinks.length > 1) {
                        let matchCount = 0;
                        let conditionCount = 0;
                        for (let c in outboundConnectedLinks) {
                            let appSCell = outboundConnectedLinks[c];
                            let getAppSLinkTarget = appSCell.getTargetElement();
                            if (getAppSLinkTarget.attributes.type == emailMatch) {
                                matchCount++;
                            }
                            if (getAppSLinkTarget.attributes.type == emailCondition) {
                                conditionCount++;
                            }
                        }
                        if (matchCount > 5) {

                            errorsArr.push({
                                errorDesc: `Email Scanner can be connected to max 5 Email Match`, actionText: actText, elId: elementId
                            });
                            appScannerErrorFlag = true;
                        }

                        if (conditionCount > 1) {

                            errorsArr.push({
                                errorDesc: `Email Scanner can be connected to max 1 Email Condition`, actionText: actText, elId: elementId
                            });
                            appScannerErrorFlag = true;
                        }
                    }
                }
            }

            if (outboundConnectedLinks.length == 0 && inboundConnectedLinks.length ==0){
                errorsArr.push({
                    errorDesc: `Please create links for Email Scanner to successfully save rules`, actionText: actText, elId: elementId
                });
                emailScannerErrorFlag = true;

            }
            if (emailScannerErrorFlag) {
                errorHighlightAutosense(cell);
            }
        }
        //End : Validate for Email Scanner
   

        //Start:Validate For App Match
        if (elementType == appMatch) {

            let sourceElement = ''; let targetElement = '';
            let appMatchErrorFlag = false; let sourcePort = ''; let targetPort = '';
            let appMatchFields = allCells[cel].attributes.fields;
            (inboundConnectedLinks.length != 0) ? sourceElement = inboundConnectedLinks[0].getSourceElement() : sourceElement = '';
            (outboundConnectedLinks.length != 0) ? targetElement = outboundConnectedLinks[0].getTargetElement() : targetElement = '';

            

            if (inboundConnectedLinks.length != 0) {

                if (appMatchFields.matchType != undefined && appMatchFields.matchType != '-1') {
                    if (appMatchFields.matchType !== '-1' && appMatchFields.matchType.includes('static')) {
                        if (appMatchFields.matchType == '-1' || appMatchFields.matchattr == '-1' || appMatchFields.operator == '-1' || appMatchFields.staticData == '' || appMatchFields.staticData == undefined) {
                            errorsArr.push({
                                errorDesc: `All mandatory fields in App Match must be filled properly`, actionText: actText, elId: elementId
                            });
                            appMatchErrorFlag = true;
                        }

                        let regExp = /[\\"]/;
                        let staticVal = appMatchFields.staticData;
                        if (staticVal.length != 0 && regExp.test(staticVal) == true) {

                            errorsArr.push({
                                errorDesc: `Static Value value cannot have \\ (backslash) and " (double quotes)`, actionText: actText, elId: elementId
                            });
                            appMatchErrorFlag = true;

                        }
                    }
                     if (appMatchFields.matchType !== '-1' && appMatchFields.matchType.includes('dynamic')) {
                        if ((appMatchFields.matchType == 'dynamic' || appMatchFields.matchType == 'staticanddynamic' || appMatchFields.matchType == 'dynamicandstatic') && (appMatchFields.defaultValueForDynamic == '')) {
                            errorsArr.push({
                                errorDesc: `Dynamic default value should be entered for Dynamic Matching Type selection`, actionText: actText, elId: elementId
                            });
                            appMatchErrorFlag = true;
                         }
                         if ((appMatchFields.matchType == 'dynamic' || appMatchFields.matchType == 'staticanddynamic' || appMatchFields.matchType == 'dynamicandstatic') && (appMatchFields.dynamicField == '-1')) {
                             errorsArr.push({
                                 errorDesc: `Select Dynamic values for AppMatch`, actionText: actText, elId: elementId
                             });
                             appMatchErrorFlag = true;
                         }

                         let regExp = /[\\"]/;
                         let defaultDynamicVal = appMatchFields.defaultValueForDynamic;
                         if (defaultDynamicVal.length != 0 && regExp.test(defaultDynamicVal) == true) {

                             errorsArr.push({
                                 errorDesc: `Default dyanmic value value cannot have \\ (backslash) and " (double quotes)`, actionText: actText, elId: elementId
                             });
                             appMatchErrorFlag = true;

                         }
                    }

                   
                }
               
                else {
                    errorsArr.push({
                        errorDesc: `All mandatory fields in App Match must be filled properly`, actionText: actText, elId: elementId
                    });
                    appMatchErrorFlag = true;
                }


                if (appMatchFields.matchattr === '-1' || appMatchFields.operator == '-1' || appMatchFields.matchattr === undefined || appMatchFields.operator === undefined) {

                    errorsArr.push({
                        errorDesc: `All mandatory fields in App Match must be filled properly`, actionText: actText, elId: elementId
                    });
                    appMatchErrorFlag = true;

                }
                //if ((appMatchFields.matchType == 'dynamic' || appMatchFields.matchType == 'staticanddynamic' || appMatchFields.matchType == 'dynamicandstatic') && appMatchFields.defaultValueForDynamic == '') {
                //    errorsArr.push({
                //        errorDesc: `Dynamic default value should be selected for Dynamic Matching Type selection`, actionText: actText, elId: elementId
                //    });
                //    appMatchErrorFlag = true;

                //}

                if (sourceElement != "") {
                    if (sourceElement.attributes.type == emailScanner || sourceElement.attributes.type == fileSystemScanner) {
                        errorsArr.push({
                            errorDesc: `App Match can only be connected to App Scanner or App Condition`, actionText: actText, elId: elementId
                        });
                        appMatchErrorFlag = true;
                    }


                    if (sourceElement.attributes.type == emailCondition || sourceElement.attributes.type == fileSystemCondition) {
                        errorsArr.push({
                            errorDesc: `App Match can only be connected to App Scanner or App Condition`, actionText: actText, elId: elementId
                        });
                        appMatchErrorFlag = true;
                    }
                }

                if (inboundConnectedLinks < 1) {
                    errorsArr.push({
                        errorDesc: `Parent Connection not found`, actionText: actText, elId: elementId
                    });
                    appMatchErrorFlag = true;
                }
                else {
                    for (let i in inboundConnectedLinks) {
                        sourcePort = inboundConnectedLinks[i];

                        if (inboundConnectedLinks.length > 1 && sourceElement.attributes.type == appScanner) {
                            errorsArr.push({
                                errorDesc: `AppMatch cannot have multiple Parent`, actionText: actText, elId: elementId
                            });
                            appMatchErrorFlag = true;
                            break;
                        }
                        else if (sourceElement.attributes.type == appScanner && (sourcePort.attributes.source.port == 'in' || sourcePort.attributes.source.port == 'out' || sourcePort.attributes.source.port == 'r')) {
                            errorsArr.push({
                                errorDesc: `Parent of AppMatch can come from either '&' or '||' ports of AppScan`, actionText: actText, elId: elementId
                            });
                            appMatchErrorFlag = true;
                            break;
                        }
                    }
                }
            }

            else {
                errorsArr.push({
                    errorDesc: `Match should be connected to parent App element`, actionText: actText, elId: elementId
                });
                appMatchErrorFlag = true;
            }

            if (appMatchErrorFlag) {
                errorHighlightAutosense(cell);
            }

        }
        //End:Validate for App Match

        //Start:Validate For FS Match
        if (elementType == fileSystemMatch) {

            let sourceElement = ''; let targetElement = '';
            let fsMatchErrorFlag = false;
            let fileSysMatchFields = allCells[cel].attributes.fields;
            (inboundConnectedLinks.length != 0) ? sourceElement = inboundConnectedLinks[0].getSourceElement() : sourceElement = '';
            (outboundConnectedLinks.length != 0) ? targetElement = outboundConnectedLinks[0].getTargetElement() : targetElement = '';

            

            //if (getElementCountByTypeAutosense(fileSystemMatch) > 3) {

            //    errorsArr.push({ errorDesc: 'Maximum 3 File Match are allowed per rule.', actionText: 'Add or Remove manualy', elId: 0 });
            //    fsMatchErrorFlag = true;
            //    break;
            //}
            if (inboundConnectedLinks.length != 0) {
                if (fileSysMatchFields.matchType != undefined && fileSysMatchFields.matchType != '-1') {
                    if (fileSysMatchFields.matchType !== '-1' && fileSysMatchFields.matchType.includes('static')) {
                        if (fileSysMatchFields.matchType == '-1' || fileSysMatchFields.matchattr == '-1' || fileSysMatchFields.operator == '-1' || fileSysMatchFields.staticData == '' || fileSysMatchFields.staticData == undefined) {
                            errorsArr.push({
                                errorDesc: `All mandatory fields in FileMatch must be filled properly`, actionText: actText, elId: elementId
                            });
                            fsMatchErrorFlag = true;
                        }

                        let regExp = /[\\"]/;
                        let staticVal = fileSysMatchFields.staticData;
                        if (staticVal.length != 0 && regExp.test(staticVal) == true) {

                            errorsArr.push({
                                errorDesc: `Static Value value cannot have \\ (backslash) and " (double quotes)`, actionText: actText, elId: elementId
                            });
                            fsMatchErrorFlag = true;

                        }

                    }
                    if (fileSysMatchFields.matchType !== '-1' && fileSysMatchFields.matchType.includes('dynamic')) {
                        if ((fileSysMatchFields.matchType == 'dynamic' || fileSysMatchFields.matchType == 'staticanddynamic' || fileSysMatchFields.matchType == 'dynamicandstatic') && (fileSysMatchFields.defaultValueForDynamic == '' || fileSysMatchFields.defaultValueForDynamic == undefined || fileSysMatchFields.defaultValueForDynamic == null)) {
                            errorsArr.push({
                                errorDesc: `Dynamic default value should be entered for Dynamic Matching Type selection`, actionText: actText, elId: elementId
                            });
                            fsMatchErrorFlag = true;
                         }
                         if ((fileSysMatchFields.matchType == 'dynamic' || fileSysMatchFields.matchType == 'staticanddynamic' || fileSysMatchFields.matchType == 'dynamicandstatic') && (fileSysMatchFields.dynamicField == '-1')) {
                             errorsArr.push({
                                 errorDesc: `Select Dynamic value for File Match`, actionText: actText, elId: elementId
                             });
                             fsMatchErrorFlag = true;
                        }

                        let regExp = /[\\"]/;
                        let defaultDynamicVal = fileSysMatchFields.defaultValueForDynamic;
                        if (defaultDynamicVal.length != 0 && regExp.test(defaultDynamicVal) == true) {

                            errorsArr.push({
                                errorDesc: `Default dyanmic value value cannot have \\ (backslash) and " (double quotes)`, actionText: actText, elId: elementId
                            });
                            fsMatchErrorFlag = true;

                        }


                    }
                   

                    if (fileSysMatchFields.matchattr == 'FOLDER_OPERATION') {
                        if (fileSysMatchFields.staticData.trim().toLowerCase() === 'createandmodify' || fileSysMatchFields.staticData.trim().toLowerCase() === 'modify') {

                            fsMatchErrorFlag = false;
                        }
                        else {

                            errorsArr.push({
                                errorDesc: `Only createAndModify/modify is allowed in the static field`, actionText: actText, elId: elementId
                            });
                            fsMatchErrorFlag = true;
                        }
                    }
                    if (fileSysMatchFields.matchattr == 'FILE_OPERATION') {
                        if (fileSysMatchFields.staticData.trim().toLowerCase() === 'createandmodify' || fileSysMatchFields.staticData.trim().toLowerCase() === 'modify' || fileSysMatchFields.staticData.trim().toLowerCase() === 'delete') {

                            fsMatchErrorFlag = false;
                        }
                        else {

                            errorsArr.push({
                                errorDesc: `Only createAndModify/modify/delete is allowed in the static field`, actionText: actText, elId: elementId
                            });
                            fsMatchErrorFlag = true;
                        }
                    }

                    if (fileSysMatchFields.matchattr == 'nested_level') {
                        if (parseInt(fileSysMatchFields.staticData) > 3) {

                            errorsArr.push({
                                errorDesc: `Only 3 level of subfolder search is allowed`, actionText: actText, elId: elementId
                            });
                            fsMatchErrorFlag = true;
                        }

                    }

                }
                else if (fileSysMatchFields.matchattr === '-1' || fileSysMatchFields.operator == '-1' || fileSysMatchFields.matchattr === undefined || fileSysMatchFields.operator === undefined) {

                    errorsArr.push({
                        errorDesc: `All mandatory fields in FileMatch must be filled properly`, actionText: actText, elId: elementId
                    });
                    fsMatchErrorFlag = true;

                }
                else {
                    errorsArr.push({
                        errorDesc: `All mandatory fields in FileMatch must be filled properly`, actionText: actText, elId: elementId
                    });
                    fsMatchErrorFlag = true;
                }

                //if ((fileSysMatchFields.matchType == 'dynamic' || fileSysMatchFields.matchType == 'staticanddynamic' || fileSysMatchFields.matchType == 'dynamicandstatic') && fileSysMatchFields.defaultValueForDynamic == '') {
                //    errorsArr.push({
                //        errorDesc: `Dynamic default value should be selected for Dynamic Matching Type selection`, actionText: actText, elId: elementId
                //    });
                //    fsMatchErrorFlag = true;

                //}
                if (sourceElement != '') {
                    if (sourceElement.attributes.type == emailScanner || sourceElement.attributes.type == appScanner) {
                        errorsArr.push({
                            errorDesc: `File System Match can only be connected to File System Scanner or File System Condition`, actionText: actText, elId: elementId
                        });
                        fsMatchErrorFlag = true;
                    }
                    if (sourceElement.attributes.type == appCondition || sourceElement.attributes.type == emailCondition) {
                        errorsArr.push({
                            errorDesc: `File System Match can only be connected to File System Scanner or File System Condition`, actionText: actText, elId: elementId
                        });
                        fsMatchErrorFlag = true;
                    }
                }

                if (inboundConnectedLinks < 1) {
                    errorsArr.push({
                        errorDesc: `Parent Connection not found`, actionText: actText, elId: elementId
                    });
                    fsMatchErrorFlag = true;
                }
                else {
                    for (let i in inboundConnectedLinks) {
                        sourcePort = inboundConnectedLinks[i];

                        if (inboundConnectedLinks.length > 1 && sourceElement.attributes.type == fileSystemScanner) {
                            errorsArr.push({
                                errorDesc: `FileSystemMatch cannot have multiple Parent`, actionText: actText, elId: elementId
                            });
                            fsMatchErrorFlag = true;
                            break;
                        }
                        else if (sourceElement.attributes.type == fileSystemScanner && (sourcePort.attributes.source.port == 'in' || sourcePort.attributes.source.port == 'out' || sourcePort.attributes.source.port == 'r')) {
                            errorsArr.push({
                                errorDesc: `Parent of FileSystemMatch can come from either '&' or '||' ports of FileSystemScanner`, actionText: actText, elId: elementId
                            });
                            fsMatchErrorFlag = true;
                            break;
                        }
                    }
                }
            }
            else {

                errorsArr.push({
                    errorDesc: `Match should be connected to the parent File system element`, actionText: actText, elId: elementId
                });
                fsMatchErrorFlag = true;
            }

            if (fsMatchErrorFlag) {
                errorHighlightAutosense(cell);
            }

        }
        //End:Validate for FS Match

        //Start:Validate for Email Match
        if (elementType == emailMatch) {

            let sourceElement = ''; let targetElement = '';
            let emailMatchErrorFlag = false;
            let emailMatchFields = allCells[cel].attributes.fields;
            (inboundConnectedLinks.length != 0) ? sourceElement = inboundConnectedLinks[0].getSourceElement() : sourceElement = '';
            (outboundConnectedLinks.length != 0) ? targetElement = outboundConnectedLinks[0].getTargetElement() : targetElement = '';
            
                //if (getElementCountByTypeAutosense(emailMatch) > 3) {

                //    errorsArr.push({ errorDesc: 'Maximum 3 Email Match are allowed per rule.', actionText: 'Add or Remove manualy', elId: 0 });
                //    emailMatchErrorFlag = true;
                //    break;
                //}
            if (inboundConnectedLinks.length != 0) {
                if (emailMatchFields.matchType != undefined && emailMatchFields.matchType !='-1') {
                    if (emailMatchFields.matchType !== '-1' && emailMatchFields.matchType.includes('static')) {
                        if (emailMatchFields.matchType == '-1' || emailMatchFields.matchattr == '-1' || emailMatchFields.operator == '-1' || emailMatchFields.staticData == '' || emailMatchFields.staticData == undefined) {
                            errorsArr.push({
                                errorDesc: `All mandatory fields in EmailMatch must be filled properly`, actionText: actText, elId: elementId
                            });
                            emailMatchErrorFlag = true;
                        }

                        let regExp = /[\\"]/;
                        let staticVal = emailMatchFields.staticData;
                        if (staticVal.length != 0 && regExp.test(staticVal) == true) {

                            errorsArr.push({
                                errorDesc: `Static Value value cannot have \\ (backslash) and " (double quotes)`, actionText: actText, elId: elementId
                            });
                            emailMatchErrorFlag = true;

                        }

                    }
                    if (emailMatchFields.matchType !== '-1' && emailMatchFields.matchType.includes('dynamic')) {
                        if ((emailMatchFields.matchType == 'dynamic' || emailMatchFields.matchType == 'staticanddynamic' || emailMatchFields.matchType == 'dynamicandstatic') && (emailMatchFields.defaultValueForDynamic == '' || emailMatchFields.defaultValueForDynamic == null || emailMatchFields.defaultValueForDynamic == undefined)) {
                            errorsArr.push({
                                errorDesc: `Dynamic default value should be entered for Dynamic Matching Type selection`, actionText: actText, elId: elementId
                            });
                            emailMatchErrorFlag = true;
                        }
                        if ((emailMatchFields.matchType == 'dynamic' || emailMatchFields.matchType == 'staticanddynamic' || emailMatchFields.matchType == 'dynamicandstatic') && (emailMatchFields.dynamicField == '-1')) {
                            errorsArr.push({
                                errorDesc: `Select Dynamic value for Email Match`, actionText: actText, elId: elementId
                            });
                            emailMatchErrorFlag = true;
                        }

                        let regExp = /[\\"]/;
                        let defaultDynamicVal = emailMatchFields.defaultValueForDynamic;
                        if (defaultDynamicVal.length != 0 && regExp.test(defaultDynamicVal) == true) {

                            errorsArr.push({
                                errorDesc: `Default dyanmic value value cannot have \\ (backslash) and " (double quotes)`, actionText: actText, elId: elementId
                            });
                            emailMatchErrorFlag = true;

                        }

                    }
                   
                }
                else if (emailMatchFields.matchattr === '-1' || emailMatchFields.operator == '-1' || emailMatchFields.matchattr === undefined || emailMatchFields.operator === undefined) {

                    errorsArr.push({
                        errorDesc: `All mandatory fields in EmailMatch must be filled properly`, actionText: actText, elId: elementId
                    });
                    emailMatchErrorFlag = true;

                }
                else {
                    errorsArr.push({
                        errorDesc: `All mandatory fields in EmailMatch must be filled properly`, actionText: actText, elId: elementId
                    });
                    emailMatchErrorFlag = true;
                }

                //if ((emailMatchFields.matchType == 'dynamic' || emailMatchFields.matchType == 'staticanddynamic' || emailMatchFields.matchType == 'dynamicandstatic') && emailMatchFields.defaultValueForDynamic == '') {
                //    errorsArr.push({
                //        errorDesc: `Dynamic default value should be selected for Dynamic Matching Type selection`, actionText: actText, elId: elementId
                //    });
                //    emailMatchErrorFlag = true;

                //}
                if (sourceElement != "") {
                    if (sourceElement.attributes.type == appScanner || sourceElement.attributes.type == fileSystemScanner) {
                        errorsArr.push({
                            errorDesc: `Email Match can only be connected to Email Scanner or Email Condition`, actionText: actText, elId: elementId
                        });
                        emailMatchErrorFlag = true;
                    }

                    if (sourceElement.attributes.type == appCondition || sourceElement.attributes.type == fileSystemCondition) {
                        errorsArr.push({
                            errorDesc: `Email Match can only be connected to Email Scanner or Email Condition`, actionText: actText, elId: elementId
                        });
                        emailMatchErrorFlag = true;
                    }
                }
                if (inboundConnectedLinks < 1) {
                    errorsArr.push({
                        errorDesc: `Parent Connection not found`, actionText: actText, elId: elementId
                    });
                    emailMatchErrorFlag = true;
                }
                else {
                    for (let i in inboundConnectedLinks) {
                        sourcePort = inboundConnectedLinks[i];

                        if (inboundConnectedLinks.length > 1 && sourceElement.attributes.type == emailScanner) {
                            errorsArr.push({
                                errorDesc: `EmailMatch cannot have multiple Parent`, actionText: actText, elId: elementId
                            });
                            emailMatchErrorFlag = true;
                            break;
                        }
                        else if (sourceElement.attributes.type == emailScanner && (sourcePort.attributes.source.port == 'in' || sourcePort.attributes.source.port == 'out' || sourcePort.attributes.source.port == 'r')) {
                            errorsArr.push({
                                errorDesc: `Parent of EmailMatch can come from either '&' or '||' ports of EmailScanner`, actionText: actText, elId: elementId
                            });
                            emailMatchErrorFlag = true;
                            break;
                        }
                    }
                }
            }

            else {

                errorsArr.push({
                    errorDesc: `Match should be connected to parent Email Elements`, actionText: actText, elId: elementId
                });
                emailMatchErrorFlag = true;

            }
            if (emailMatchErrorFlag) {
                errorHighlightAutosense(cell);
            }
        }
        //End:Validate for Email Match

        //Start: Validate for App Condition
        if (elementType == appCondition) {

            let sourceElement = ''; let targetElement = '';
            let appConditionErrorFlag = false;

            (inboundConnectedLinks.length != 0) ? sourceElement = inboundConnectedLinks[0].getSourceElement() : sourceElement = '';
            (outboundConnectedLinks.length != 0) ? targetElement = outboundConnectedLinks[0].getTargetElement() : targetElement = '';

            if (outboundConnectedLinks.length != 0 || inboundConnectedLinks.length !=0) {
                if (inboundConnectedLinks.length == 0) {

                    errorsArr.push({
                        errorDesc: `Please link the AppCondition to a App Elements`, actionText: actText, elId: elementId
                    });

                    appConditionErrorFlag = true;

                }
                else {
                    if (inboundConnectedLinks.length > 1) {
                        errorsArr.push({
                            errorDesc: `App Condition can only have one Parent`, actionText: actText, elId: elementId
                        });

                        appConditionErrorFlag = true;
                    }
                    let sourcePort = inboundConnectedLinks[0];

                    if (sourcePort.attributes.source.port == 'r' || sourcePort.attributes.source.port == 'in' || sourcePort.attributes.source.port == 'out') {
                        errorsArr.push({
                            errorDesc: `App Condition can only be connected to App Scanner through port 'and' or 'or'`, actionText: actText, elId: elementId
                        });

                        appConditionErrorFlag = true;
                    }

                    if ((sourcePort.attributes.source.port == 'and' || sourcePort.attributes.source.port == 'or') && sourcePort.attributes.target.port == 'out') {

                        errorsArr.push({
                            errorDesc: `App Scanner cannot be connected to App Condition's Output port`, actionText: actText, elId: elementId
                        });

                        appConditionErrorFlag = true;

                    }

                    if (sourceElement != '' && sourceElement.attributes.type != appScanner) {
                        errorsArr.push({
                            errorDesc: `App Condition can only be connected to App Scanner`, actionText: actText, elId: elementId
                        });

                        appConditionErrorFlag = true;
                    }
                }
                let connectedLinks = 0
                for (i in outboundConnectedLinks) {
                    let appSCell = outboundConnectedLinks[i];
                    let target = appSCell.getTargetElement();
                    if (target.attributes.type == appMatch) {
                        connectedLinks++;
                    }
                    if (target.attributes.type == appScanner) {
                        errorsArr.push({
                            errorDesc: `App Condition can only be connected to App Match via outbound link`, actionText: actText, elId: elementId
                        });

                        appConditionErrorFlag = true;
                        break;
                    }

                    if (appSCell.attributes.source.port == 'in' && appSCell.attributes.target.port == 'in') {
                        if (target.attributes.type == appMatch) {
                            errorsArr.push({
                                errorDesc: `App Condition can only be connected to App Match via 'out' port of Condition`, actionText: actText, elId: elementId
                            });

                            appConditionErrorFlag = true;
                            break;
                        }


                    }
                }
                
                if (connectedLinks > 5) {

                    errorsArr.push({
                        errorDesc: ` App Condition can have maximum 5 outbound links`, actionText: actText, elId: elementId
                    });

                    appConditionErrorFlag = true;

                }
                if (connectedLinks < 2) {

                    errorsArr.push({
                        errorDesc: ` App Condition must have minimum 2 outbound links`, actionText: actText, elId: elementId
                    });

                    appConditionErrorFlag = true;

                }

            }
            else {

                errorsArr.push({
                    errorDesc: `Please link the AppCondition to a App Elements`, actionText: actText, elId: elementId
                });

                appConditionErrorFlag = true;
            }

          

            if (appConditionErrorFlag) {
                errorHighlightAutosense(cell);
            }
        }
        //End: Validate for App Condition

        //Start: Validate for FS Condition
        if (elementType == fileSystemCondition) {

            let sourceElement = ''; let targetElement = '';
            let fsConditionErrorFlag = false;

            (inboundConnectedLinks.length != 0) ? sourceElement = inboundConnectedLinks[0].getSourceElement() : sourceElement = '';
            (outboundConnectedLinks.length != 0) ? targetElement = outboundConnectedLinks[0].getTargetElement() : targetElement = '';

            if (outboundConnectedLinks.length != 0 || inboundConnectedLinks.length != 0) {
                if (inboundConnectedLinks.length == 0) {

                    errorsArr.push({
                        errorDesc: `Please link the FS Condition to a File system Elements`, actionText: actText, elId: elementId
                    });

                    fsConditionErrorFlag = true;

                }
                else {
                if (inboundConnectedLinks.length > 1) {
                    errorsArr.push({
                        errorDesc: `File system Condition can only have one Parent`, actionText: actText, elId: elementId
                    });

                    fsConditionErrorFlag = true;
                }
                let sourcePort = inboundConnectedLinks[0];

                if (sourcePort.attributes.source.port == 'r' || sourcePort.attributes.source.port == 'in' || sourcePort.attributes.source.port == 'out') {
                    errorsArr.push({
                        errorDesc: `File system Condition can only be connected to File system Scanner through port 'and' or 'or'`, actionText: actText, elId: elementId
                    });

                    fsConditionErrorFlag = true;
                }

                if ((sourcePort.attributes.source.port == 'and' || sourcePort.attributes.source.port == 'or') && sourcePort.attributes.target.port == 'out') {

                    errorsArr.push({
                        errorDesc: `File system Scanner cannot be connected to File system Condition's Output port`, actionText: actText, elId: elementId
                    });

                    fsConditionErrorFlag = true;

                }

                    if (sourceElement.attributes.type != fileSystemScanner) {
                        errorsArr.push({
                            errorDesc: `File System Condition can only be connected to File System Scanner`, actionText: actText, elId: elementId
                        });

                        fsConditionErrorFlag = true;
                    }

                }

                let connectedLinks = 0
                for (i in outboundConnectedLinks) {
                    let appSCell = outboundConnectedLinks[i];
                    let target = appSCell.getTargetElement();
                    if (target.attributes.type == fileSystemMatch) {
                        connectedLinks++;
                    }

                    if (target.attributes.type == fileSystemScanner) {
                        errorsArr.push({
                            errorDesc: `File System Condition can only be connected to File System Match via outbound Link`, actionText: actText, elId: elementId
                        });

                        fsConditionErrorFlag = true;
                        break;
                    }

                    if (appSCell.attributes.source.port == 'in' && appSCell.attributes.target.port == 'in') {
                        if (target.attributes.type == fileSystemMatch) {
                            errorsArr.push({
                                errorDesc: `File System Condition can only be connected to File System Match via 'out' port of the condition`, actionText: actText, elId: elementId
                            });

                            fsConditionErrorFlag = true;
                            
                            break;
                        }


                    }
                }

                //if (connectedLinks == 0) {

                //    errorsArr.push({
                //        errorDesc: `Minimum 1 File Match is mandatory for each File Scanner. `, actionText: actText, elId: elementId
                //    });

                //    fsConditionErrorFlag = true;

                //}
                if (connectedLinks > 5) {

                    errorsArr.push({
                        errorDesc: `File System Condition can have maximum 5 outbound links`, actionText: actText, elId: elementId
                    });

                    fsConditionErrorFlag = true;

                }
                if (connectedLinks < 2) {

                    errorsArr.push({
                        errorDesc: `File System Condition must have minimum 2 outbound links`, actionText: actText, elId: elementId
                    });

                    fsConditionErrorFlag = true;

                }
            }

            else {

                errorsArr.push({
                    errorDesc: `Please link the Condition to a Parent Element`, actionText: actText, elId: elementId
                });

                fsConditionErrorFlag = true;
            }
            if (fsConditionErrorFlag) {
                errorHighlightAutosense(cell);
            }
        }
        //End: Validate for FS Condition

        //Start: Validate for Email Condition
        if (elementType == emailCondition) {

            let sourceElement = ''; let targetElement = '';
            let emailConditionErrorFlag = false;

            (inboundConnectedLinks.length != 0) ? sourceElement = inboundConnectedLinks[0].getSourceElement() : sourceElement = '';
            (outboundConnectedLinks.length != 0) ? targetElement = outboundConnectedLinks[0].getTargetElement() : targetElement = '';

            if (outboundConnectedLinks.length != 0 || inboundConnectedLinks.length != 0) {
                if (inboundConnectedLinks.length == 0) {

                    errorsArr.push({
                        errorDesc: `Please link the Email Condition to a Email Elements`, actionText: actText, elId: elementId
                    });

                    emailConditionErrorFlag = true;

                }
                else {
                    if (inboundConnectedLinks.length > 1) {
                        errorsArr.push({
                            errorDesc: `Email Condition can only have one Parent`, actionText: actText, elId: elementId
                        });

                        emailConditionErrorFlag = true;
                    }
                    let sourcePort = inboundConnectedLinks[0];

                    if (sourcePort.attributes.source.port == 'r' || sourcePort.attributes.source.port == 'in' || sourcePort.attributes.source.port == 'out') {
                        errorsArr.push({
                            errorDesc: `Email Condition can only be connected to Email Scanner through port 'and' or 'or'`, actionText: actText, elId: elementId
                        });

                        emailConditionErrorFlag = true;
                    }

                    if ((sourcePort.attributes.source.port == 'and' || sourcePort.attributes.source.port == 'or') && sourcePort.attributes.target.port == 'out') {

                        errorsArr.push({
                            errorDesc: `Email Scanner cannot be connected to Email Condition's Output port`, actionText: actText, elId: elementId
                        });

                        emailConditionErrorFlag = true;

                    }

                    if (sourceElement.attributes.type != emailScanner) {
                        errorsArr.push({
                            errorDesc: `Email Condition can only be connected to Email Scanner`, actionText: actText, elId: elementId
                        });

                        emailConditionErrorFlag = true;


                    }
                }
            }
            else {

                errorsArr.push({
                    errorDesc: `Please link the Condition to a Parent Element`, actionText: actText, elId: elementId
                });

                emailConditionErrorFlag = true;

            }
            let connectedLinks = 0
            for (i in outboundConnectedLinks) {
                let appSCell = outboundConnectedLinks[i];
                let target = appSCell.getTargetElement();
                if (target.attributes.type == emailMatch) {
                    connectedLinks++;
                }
                 if (target.attributes.type == emailScanner) {
                    errorsArr.push({
                        errorDesc: `Email Condition can only be connected to Email Match via outbound Link`, actionText: actText, elId: elementId
                    });

                    emailConditionErrorFlag = true;

                    break;

                }
                if (appSCell.attributes.source.port == 'in' && appSCell.attributes.target.port == 'in') {
                    if (target.attributes.type == emailMatch) {
                        errorsArr.push({
                            errorDesc: `Email Condition can only be connected to Email Match via 'out' port of the condition`, actionText: actText, elId: elementId
                        });

                        emailConditionErrorFlag = true;

                        break;
                    }


                }
            }
            if (connectedLinks > 5) {

                errorsArr.push({
                    errorDesc: `Email Condition can have maximum 5 outbound links`, actionText: actText, elId: elementId
                });

                emailConditionErrorFlag = true;

            }

            //if (connectedLinks == 0) {

            //    errorsArr.push({
            //        errorDesc: `Minimum 1 Email Match is mandatory for each Email Scanner. `, actionText: actText, elId: elementId
            //    });

            //    emailConditionErrorFlag = true;

            //}

            if (connectedLinks < 2) {

                errorsArr.push({
                    errorDesc: `Email Condition must have minimum 2 outbound links`, actionText: actText, elId: elementId
                });

                emailConditionErrorFlag = true;

            }
           
            
            if (emailConditionErrorFlag) {
                errorHighlightAutosense(cell);
            }
        }
        //End: Validate for Email Condition

        //Start: Validate for Success Action
        if (elementType == connectorNotification) {
            let sourceElement = ''; let targetElement = '';
            let connectorNotificationErrorFlag = false;
            let connectorNotifFields = allCells[cel].attributes.fields;
            (inboundConnectedLinks.length != 0) ? sourceElement = inboundConnectedLinks[0].getSourceElement() : sourceElement = '';
            (outboundConnectedLinks.length != 0) ? targetElement = outboundConnectedLinks[0].getTargetElement() : targetElement = '';
            if (inboundConnectedLinks.length != 0) {
                if (connectorNotifFields.type0 == '' || connectorNotifFields.type0 == undefined) {
                    errorsArr.push({
                        errorDesc: `Type field for Success Action must be filled`, actionText: actText, elId: elementId
                    });
                    connectorNotificationErrorFlag = true;

                }


                if (connectorNotifFields.actionName0 == '' || connectorNotifFields.actionName0 == undefined) {
                    errorsArr.push({
                        errorDesc: `Action Name field for Success Action must be filled`, actionText: actText, elId: elementId
                    });
                    connectorNotificationErrorFlag = true;
                }

                if (sourceElement.attributes.typeName != 'Scanner' || inboundConnectedLinks.length > 1) {
                    errorsArr.push({
                        errorDesc: `Parent of Success Action can only be one Scanner`, actionText: actText, elId: elementId
                    });
                    connectorNotificationErrorFlag = true;
                }
            }

            else {

                errorsArr.push({
                    errorDesc: `Success Action has to be connected to a Scanner`, actionText: actText, elId: elementId
                });
                connectorNotificationErrorFlag = true;

            }
            if (connectorNotificationErrorFlag) {
                errorHighlightAutosense(cell);
            }

        }
        //End: Validate for Success Action
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


function validateForm() {
    let errorsArr = [];
    let formAreaErrorFlag = false;
    let actText = 'Fixit';
    //let ruleIframeBody = document.getElementById('iframeAutosenseRule').contentWindow.document.body;
    let serviceAreaID = $('#select_serviceAreaRule').val();
    let domainID = $('#select_domainRule').val();
    let subactID = $('#select_activityRule').val();
    let techID = $('#select_technologyRule').val();
    let taskID = $('#select_taskRule').val();
    let ruleName = $('input[id="ruleName"]').val();
    let description = $('#ruleDesc').val();

    let selectServiceArea = $('#select_serviceAreaRuleDiv .select2-selection');
    let select_domainRule = $('#select_domainRuleDiv .select2-selection');
    let select_technologyRule = $('#select_technologyRuleDiv .select2-selection');
    let select_activityRuleDiv = $('#select_activityRuleDiv .select2-selection');
    let select_taskRule = $('#select_taskRuleDiv .select2-selection');
    let input_ruleName = $('#ruleName');
    let input_ruleDesc = $('#ruleDesc');    
    let input_ownerSignum = $('#workOrderViewIdPrefix_assigned_to');
    let fileName = window.parent.$('#autosenseRuleData').attr('data-fileName');
    let isPageTypeMigration = window.parent.$('#autosenseRuleData').attr('data-mgnplot');
    let pageName = window.parent.$('#autosenseRuleData').attr('data-name');
    let ownerSignum = $('#workOrderViewIdPrefix_assigned_to').val();

    selectServiceArea[0].style.border = '';
    select_domainRule[0].style.border = '';
    select_technologyRule[0].style.border = '';
    select_activityRuleDiv[0].style.border = '';
    select_taskRule[0].style.border = '';
    input_ruleName[0].style.border = '';
    input_ruleDesc[0].style.border = '';    

    if (domainID == undefined || domainID == null || domainID == "") {

        errorsArr.push({
            errorDesc: `Domain not selected from the dropdown`, actionText: actText
        });
        formAreaErrorFlag = true;
        select_domainRule[0].style.cssText = "border: 1px solid red; border-radius: 5px;";
    }

    if (serviceAreaID == undefined || serviceAreaID == null || serviceAreaID == "") {

        errorsArr.push({
            errorDesc: `Service Area not selected from the dropdown`, actionText: actText
        });
        formAreaErrorFlag = true;
        selectServiceArea[0].style.cssText = "border: 1px solid red; border-radius: 5px;";
    }

    if (subactID == undefined || subactID == null || subactID == "") {

        errorsArr.push({
            errorDesc: `Activity/Sub-Activity not selected from the dropdown`, actionText: actText
        });
        formAreaErrorFlag = true;
        select_activityRuleDiv[0].style.cssText = "border: 1px solid red; border-radius: 5px;";
    }

    if (techID == undefined || techID == null || techID == "") {

        errorsArr.push({
            errorDesc: `Technology not selected from the dropdown`, actionText: actText
        });
        formAreaErrorFlag = true;
        select_technologyRule[0].style.cssText = "border: 1px solid red; border-radius: 5px;";
    }

    if (taskID == undefined || taskID == null || taskID == "") {

        errorsArr.push({
            errorDesc: `Taskname not selected from the dropdown`, actionText: actText
        });
        formAreaErrorFlag = true;
        select_taskRule[0].style.cssText = "border: 1px solid red; border-radius: 5px;";
    }

    if (!ruleName) {

        input_ruleName[0].style.cssText = "border: 1px solid red; ";
        errorsArr.push({
            errorDesc: `Please enter the Rule Name`, actionText: actText
        });
        formAreaErrorFlag = true;

    }

    if (!description) {
        input_ruleDesc[0].style.cssText = "border: 1px solid red;";
        errorsArr.push({
            errorDesc: `Please enter the Rule Description`, actionText: actText
        });
        formAreaErrorFlag = true;
    }

    if (!ownerSignum && (isPageTypeMigration || pageName == 'migratePlus')) {
        input_ownerSignum[0].style.cssText = "border: 1px solid red;";
        errorsArr.push({
            errorDesc: `Please enter the Owner Signum`, actionText: actText
        });
        formAreaErrorFlag = true;
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

function appendErrorMainAreaToDOMAutosense() {
    let html = `<div id="wf_error_list_area" style="display:none;position: absolute; width: 52%; margin: auto 231px;top:370px;">
        <div class="errors" style="-webkit-box-shadow: 0px 9px 114px 2px rgba(0,0,0,0.75); -moz-box-shadow: 0px 9px 114px 2px rgba(0,0,0,0.75); box-shadow: 0px 9px 114px 2px rgba(0,0,0,0.75); border-radius: 5px; width: 100%; max-height: 158px; background-color: white;border: 1px solid #d48080;">
            
        </div>
    </div>`;

    //document.getElementById('iframeAutosenseRule').contentWindow.document.body.querySelector('#wf_error_list_area')

    //if ($('#wf_error_list_area')) {
    //    $('#wf_error_list_area').remove();
    //}

    if ($('#wf_error_list_area')) {
        $('#wf_error_list_area').remove();
    }

    //let iframeBody =  document.getElementById('iframeAutosenseRule').contentWindow.document.body
    //$(iframeBody).append(html);
    $('body').append(html);
}

function appendErrorTableToErrorAreaAutosense() {

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


function displayWFerrorMessageAutosense(formErrorArr,passErrorArr, options = {}) {

    let errorCount = passErrorArr.length + formErrorArr.length;

    if (errorCount) {

        appendErrorMainAreaToDOMAutosense();
        appendErrorTableToErrorAreaAutosense();

        $('#wf_error_list_area #errorCount').text(errorCount);

        let tableData = `<thead>
            <tr>
                <th>#</th>
                <th>Error</th>               

            </tr>
        </thead><tbody>`;
        let rCount = 1;
        for (let i in passErrorArr) {

            tableData += `<tr>
                    <td><strong>${rCount++}</strong></td>
                    <td>${passErrorArr[i]['errorDesc']}</td>                   
                    </tr>`;
            //<td><button class="btn btn-info btn-xs wf_error_fixit" data-cellid="${passErrorArr[i]['elId']}">${passErrorArr[i]['actionText']}</button></td>
             //<td><button class="btn btn-info btn-xs wf_error_fixit" data-cellid="${passErrorArr[i]['elId']}">${passErrorArr[i]['actionText']}</button></td>
        }
        for (let j in formErrorArr) {

            tableData += `<tr>
                    <td><strong>${rCount++}</strong></td>
                    <td>${formErrorArr[j]['errorDesc']}</td>                   
                    </tr>`;

        }

        tableData += `</tbody>`;


        $('#wf_error_table').empty().append(tableData);

        $('#wf_error_list_area').slideDown();

        $("#wf_error_list_area").draggable();

        //$('#wf_error_list_area .wf_error_fixit').off('click').on('click', function () { // BIND CLICK EVENT TO FIXIT BTN FOR FIX THIS ERROR
        //    let that = this;
        //    let cellId = $(that).attr('data-cellid');

        //    let getCell = graph.getCell(cellId);

        //    //let getElementType = getCell.get('type');
        //    //if (getElementType == manualStep || getElementType == automaticStep) {
        //    //    let newHalo = new joint.ui.Halo({
        //    //        clearAll: true,
        //    //        cellView: rapidObj.paper.findViewByModel(getCell),
        //    //        handles: App.config.halo.handles
        //    //    }).render();

        //        //if (actual.removeCloseFromHalo) {
        //        //    newHalo.removeHandle('remove');
        //        //}
        //  //  }
        //    //if (actual.activeInspector) {
        //    //    rappidObj.createInspector(getCell);
        //    //}

        //  //  document.getElementById('iframeAutosenseRule').contentWindow.paperScroller.scroll(getCell.get('position').x + 150, getCell.get('position').y + 100); //SCROLL TO THAT ELEMENT

        //    if ($(that).hasClass('btn-info')) {
        //        $(that).removeClass('btn-info').addClass('btn-default');
        //    }
        //});
    }
    //else {

    //    appendErrorMainAreaToDOMAutosense();
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


function validateAndAdjustFlowchartAutosense(rappidObj) {

  
    // REMOVE HIGHLIGHT FROM ALL STEPS
    unhighlightAllCellsAutosense(rappidObj);

    // Call for validation workflow
    let validateWF = validateRuleJsonAutosense();   
    let validateFm = validateForm();
    displayWFerrorMessageAutosense(validateFm.errors, validateWF.errors);

    //Active save button
    //if ($('#saveJSON')) {
    //    $('#saveJSON').attr({ 'disabled': false }).text('Save Work Flow');

    //}
    let validateStatus = { formValidation: validateFm, jsonValidation: validateWF };
    return validateStatus;
}