/*! Rappid v2.3.3 - HTML5 Diagramming Framework

Copyright (c) 2015 client IO

 2018-08-03 


This Source Code Form is subject to the terms of the Rappid License
, v. 2.0. If a copy of the Rappid License was not distributed with this
file, You can obtain one at http://jointjs.com/license/rappid_v2.txt
 or from the Rappid archive as was distributed by client IO. See the LICENSE file.*/


(function (joint) {

    'use strict';


    joint.dia.Element.define('ericsson.StartStep', {
        attrs: {

            circle: {

                stroke: '#31d0c6',
                fill: '#dcd7d7'

            },
            text: {

                //'text-anchor':'middle',
                //xAlignment: 'middle',
                //yAlignment: 'middle',

            }
        },
    }, {
            markup: [{
                tagName: 'circle',
                selector: 'circle',
                attributes: {
                    'r': 30
                }
            },
            {
                tagName: 'text',
                selector: 'text'
            }]
        },
    );

    joint.dia.Element.define('ericsson.EndStep', {
        attrs: {

            circle: {

                stroke: '#31d0c6',
                fill: '#dcd7d7'

            },
            text: {

                //'text-anchor':'middle',
                //xAlignment: 'middle',
                //yAlignment: 'middle',

            }
        },
    }, {
            markup: [{
                tagName: 'circle',
                selector: 'circle',
                attributes: {
                    'r': 30
                }
            },
            {
                tagName: 'text',
                selector: 'text'
            }]
        },
    );


        
    joint.shapes.standard.Ellipse.define('app.CircularModel', {
        attrs: {
            root: {
                magnet: false
            }
        },
        ports: {
            groups: {
                'in': {
                    markup: [{
                        tagName: 'circle',
                        selector: 'portBody',
                        attributes: {
                            'r': 10
                        }
                    }],
                    attrs: {
                        portBody: {
                            magnet: true,
                            fill: '#61549c',
                            strokeWidth: 0
                        },
                        portLabel: {
                            fontSize: 11,
                            fill: '#61549c',
                            fontWeight: 800
                        }
                    },
                    position: {
                        name: 'ellipse',
                        args: {
                            startAngle: 0,
                            step: 30
                        }
                    },
                    label: {
                        position: {
                            name: 'radial',
                            args: null
                        }
                    }
                },
                'out': {
                    markup: [{
                        tagName: 'circle',
                        selector: 'portBody',
                        attributes: {
                            'r': 10
                        }
                    }],
                    attrs: {
                        portBody: {
                            magnet: true,
                            fill: '#61549c',
                            strokeWidth: 0
                        },
                        portLabel: {
                            fontSize: 11,
                            fill: '#61549c',
                            fontWeight: 800
                        }
                    },
                    position: {
                        name: 'ellipse',
                        args: {
                            startAngle: 180,
                            step: 30
                        }
                    },
                    label: {
                        position: {
                            name: 'radial',
                            args: null
                        }
                    }
                }
            }
        }
    }, {
        portLabelMarkup: [{
            tagName: 'text',
            selector: 'portLabel'
        }]
    });

    joint.shapes.standard.Rectangle.define('app.RectangularModel', {
        attrs: {
            root: {
                magnet: false
            }
        },
        ports: {
            groups: {
                'in': {
                    markup: [{
                        tagName: 'circle',
                        selector: 'portBody',
                        attributes: {
                            'r': 10
                        }
                    }],
                    attrs: {
                        portBody: {
                            magnet: true,
                            fill: '#61549c',
                            strokeWidth: 0
                        },
                        portLabel: {
                            fontSize: 11,
                            fill: '#61549c',
                            fontWeight: 800
                        }
                    },
                    position: {
                        name: 'left'
                    },
                    label: {
                        position: {
                            name: 'left',
                            args: {
                                y: 0
                            }
                        }
                    }
                },
                'out': {
                    markup: [{
                        tagName: 'circle',
                        selector: 'portBody',
                        attributes: {
                            'r': 10
                        }
                    }],
                    position: {
                        name: 'right'
                    },
                    attrs: {
                        portBody: {
                            magnet: true,
                            fill: '#61549c',
                            strokeWidth: 0
                        },
                        portLabel: {
                            fontSize: 11,
                            fill: '#61549c',
                            fontWeight: 800
                        }
                    },
                    label: {
                        position: {
                            name: 'right',
                            args: {
                                y: 0
                            }
                        }
                    }
                }
            }
        }
    }, {
        portLabelMarkup: [{
            tagName: 'text',
            selector: 'portLabel'
        }]
    });

    joint.shapes.standard.Link.define('app.Link', {
        router: {
            name: 'normal'
        },
        connector: {
            name: 'rounded'
        },
        labels: [],
        attrs: {
            line: {
                stroke: '#8f8f8f',
                strokeDasharray: '0',
                strokeWidth: 2,
                fill: 'none',
                sourceMarker: {
                    type: 'path',
                    d: 'M 0 0 0 0',
                    stroke: 'none'
                },
                targetMarker: {
                    type: 'path',
                    d: 'M 0 -5 -10 0 0 5 z',
                    stroke: 'none'
                }
            }
        }
    }, {
        defaultLabel: {
            attrs: {
                rect: {
                    fill: '#ffffff',
                    stroke: '#8f8f8f',
                    strokeWidth: 1,
                    refWidth: 10,
                    refHeight: 10,
                    refX: -5,
                    refY: -5
                }
            },
            position: {
                distance: 0.5,
                offset: {
                    x: 0,
                    y: 0
                }
            }
        },

        getMarkerWidth: function (type) {
            var d = (type === 'source') ? this.attr('line/sourceMarker/d') : this.attr('line/targetMarker/d');
            return this.getDataWidth(d);
        },

        getDataWidth: _.memoize(function (d) {
            return (new g.Path(d)).bbox().width;
        })

    }, {

        connectionPoint: function (line, view, magnet, opt, type, linkView) {
            var markerWidth = linkView.model.getMarkerWidth(type);
            opt = { offset: markerWidth, stroke: true };
            // connection point for UML shapes lies on the root group containg all the shapes components
            if (view.model.get('type').indexOf('uml') === 0) opt.selector = 'root';
            return joint.connectionPoints.boundary.call(this, line, view, magnet, opt, type, linkView);
        }
    });

    /*****************************************Custom Elements**********************************************/
  
    //joint.linkTools.Button.extend('infoButton',{
    //    markup: [{
    //        tagName: 'circle',
    //        selector: 'button',
    //        attributes: {
    //            'r': 7,
    //            'fill': '#001DFF',
    //            'cursor': 'pointer'
    //        }
    //    }, {
    //        tagName: 'path',
    //        selector: 'icon',
    //        attributes: {
    //            'd': 'M -2 4 2 4 M 0 3 0 0 M -2 -1 1 -1 M -1 -4 1 -4',
    //            'fill': 'none',
    //            'stroke': '#FFFFFF',
    //            'stroke-width': 2,
    //            'pointer-events': 'none'
    //        }
    //    }],
    //    distance: 60,
    //    offset: 0,
    //    action: function (evt) {
    //        alert('View id: ' + this.id + '\n' + 'Model id: ' + this.model.id);
    //    }
    //});

    joint.dia.Element.define('ericsson.Manual', {
       
        attrs: {
            body: {
                //ref:'bodyText',
                refWidth: '100%',
                refHeight: '100%',
                strokeWidth: 2,
                stroke: '#000000',
                fill: '#FFFFFF',

            },
            header: {
                //ref:'body',
                refWidth: '100%',
                height: 30,
                strokeWidth: 2,
                stroke: '#000000',
                fill: '#FFFFFF',
                // refX:0,
                // refY:0
            },
            headerText: {
                textVerticalAnchor: 'middle',
                textAnchor: 'middle',
                refX: '50%',
                refY: 15,
                fontSize: 16,
                fill: '#333333'
            },
            bodyText: {

                textVerticalAnchor: 'middle',
                textAnchor: 'middle',
                refX: '50%',
                refY: '40%',
                refY2: 10,
                fontSize: 10,
                // fill: 'black'
            },
            footer: {
                //ref:'body',
                refWidth: '100%',
                height: 30,
                strokeWidth: 2,
                stroke: '#000000',
                fill: '#FFFFFF',
                //refX:0,
                //refY:'50%'
            },
        },
        icons: {

        },
        name: '',
        viewMode:'Assessed',
        executionType: 'Manual',
        //  tool: '@',
        avgEstdTime: '0',
        botType: '',
        sadCount: 0,
        startRule: false,
        stopRule: false,
        startRuleObj: {
            ruleId: '',
            ruleName: '',
            ruleDescription: ''
        },
        stopRuleObj: {
            ruleId: '',
            ruleName: '',
            ruleDescription: ''
        },
        isStepInstructionExist: false,
        


    }, {
        //markup: '<rect/><text/>',
        markup: [{
                tagName: 'rect',
                selector: 'body'
            }, {
                tagName: 'rect',
                selector: 'header'
            }, {
                tagName: 'text',
                selector: 'headerText'
            }, {
                tagName: 'text',
                selector: 'bodyText'
            }, {
                tagName: 'rect',
                selector: 'footer'
            }
            ],
        initialize: function () {

            joint.dia.Element.prototype.initialize.apply(this, arguments);
            this.on('change:name change:action change:tool change:responsible change:workInstruction change:viewMode', this.updateText, this); // change: avgEstdTime
            this.updateText();
        },
        toJSON: function () {
            let json = joint.dia.Element.prototype.toJSON.apply(this, arguments);
            delete json.myArrayOfOptions;
            delete json.myArrayOfRPAID;
            delete json.myArrayOfTools;
            if (json.myArrayOfWorkInstruction){ delete json.myArrayOfWorkInstruction};
            return json;
        },
        //cell change opt
       updateText: function (cell, change, opt) {
           this.attr('headerText/text', 'Manual', opt);
           this.attr('bodyText/text','', opt);
            var finalText = ''; 
            let name = this.get('name');           
            let type = this.get('executionType');
           let viewMode = this.get('viewMode');
           let viewModeText;
           if (viewMode.toLowerCase() == "assessed") { viewModeText = "Assessed"; }
           else { viewModeText = "Assessed,Experienced"; }
            var effort = '';
            if (this.attributes.attrs.hasOwnProperty('task') == false) {
                effort = this.get('avgEstdEffort');
            }
            else {
                if (this.attributes.attrs.task.hasOwnProperty('Hours') && (this.attributes.attrs.task.Hours != null || this.attributes.attrs.task.Hours != undefined)) {
                    effort = this.attributes.attrs.task.Hours;
                }
                else {
                    effort = this.get('avgEstdTime').toString();
                }
            }
            let toolname = this.get('tool');
            let tasks = this.get('action');
            let responsible = this.get('responsible');
            let workInstruction = this.get('workInstruction');
            let bookingID = '%BOOKING_ID%', bookingStatus = '%STATUS%';
            let toolVal = []; let WIValue = [];

            if (tasks != undefined && toolname != undefined) {
                let taskVal = tasks.split('@');
                toolVal = toolname.split('@');
                if (this.attributes.attrs.task != undefined) {
                    if (this.attributes.attrs.task.bookingID == '' || this.attributes.attrs.task.status == '') {
                        bookingID = '%BOOKING_ID%'; bookingStatus = '%STATUS%';
                    }
                    else {
                        bookingID = this.attributes.attrs.task.bookingID;
                        if (this.attributes.attrs.task.status == undefined) {
                            bookingStatus = '%STATUS%';
                        }
                        else {
                            bookingStatus = this.attributes.attrs.task.status;
                        }
                    }
                }
               this.attr('task', { 'avgEstdEffort': effort, 'bookingID': bookingID, 'executionType': type, 'reason': '%REASON%', 'status': bookingStatus, 'taskID': taskVal[0], 'taskName': taskVal[1], 'toolID': toolVal[0], 'toolName': toolVal[1] }, opt);
                //  this.attr('text/taskName', taskVal[1]);
                this.attr('tool', { 'RPAID': "", 'RPAName': "" }, opt);
                //this.attr('responsible',{'Responsible': responsible});
                //this.attr('headerText/text', this.attr('headerText/text') + "  (" + responsible + ")");
               
            }
            else {
                toolVal[1] = '';
            }
            if(responsible != undefined){
                this.attr('responsible',{'Responsible': responsible}, opt);
                this.attr('headerText/text', this.attr('headerText/text') + "  (" + responsible + ")", opt);
           }
            if(workInstruction != undefined){
                WIValue = workInstruction.split('@');
                this.attr('workInstruction', {'WIName':WIValue[1], 'WILink':WIValue[0], 'WIID':WIValue[3]}, opt); 
            }
            let width = this.attributes.size.width, padding = 5;
           if (window.location.href.indexOf("FlowChartWorkOrderAdd") != -1) {
               if (this.attr('bodyText/text') != undefined) {

                   if (effort != undefined || effort != null) {
                       finalText += name;
                       finalText += '\n\nTool Name: ' + toolVal[1];
                       finalText += '\n\nEffort:' + effort;
                       finalText += '\n\nView Mode:' + viewModeText;
                   }
                   else if (this.attr('bodyText/text').match(/(^|\W)Effort($|\W)/)) {
                       // text = this.attr('text/text')
                       finalText += name;
                       finalText += '\n\nTool Name: ' + toolVal[1];
                       finalText += '\n\nEffort:' + this.attr('bodyText/text').split('Effort:')[1];
                       finalText += '\n\nView Mode:' + viewModeText;
                   }

                   else {
                       finalText += name;
                       finalText += '\n\nTool Name: ' + toolVal[1];
                       finalText += '\n\nEffort:NA';
                       finalText += '\n\nView Mode:' + viewModeText;
                       finalText = finalText;
                   }
               }
               else {
                   finalText += name;
                   finalText += '\n\nTool Name: ' + toolVal[1];
                   finalText += '\n\nEffort: ' + '0.0';
                   finalText += '\n\nView Mode:' + viewModeText;

               }
           }
           else {

               let urlParams = new URLSearchParams(window.location.search);
               let proficiencyId = urlParams.get("proficiencyId");
               let isProficiencyIdExp = false;

               if (proficiencyId == null || proficiencyId == undefined) {
                   isProficiencyIdExp = false;
               }
               else {
                   let profId = proficiencyId.toString();
                   isProficiencyIdExp = profId.slice(profId.length - 1) == '2' ? true : false;
               }

               let isDisabled = false;
               if (isProficiencyIdExp) {
                   isDisabled = this.attributes.executionType == C_EXECUTION_TYPE_MANUAL && !this.attributes.viewMode.includes(C_PROFICIENCY_EXPERIENCED);
               }

               if (!isDisabled) {
                   if (this.attr('bodyText/text') != undefined) {

                       if (effort != undefined || effort != null) {
                           finalText += name;
                           finalText += '\n\nTool Name: ' + toolVal[1];
                           finalText += '\n\nEffort:' + effort;

                       }
                       else if (this.attr('bodyText/text').match(/(^|\W)Effort($|\W)/)) {
                           // text = this.attr('text/text')
                           finalText += name;
                           finalText += '\n\nTool Name: ' + toolVal[1];
                           finalText += '\n\nEffort:' + this.attr('bodyText/text').split('Effort:')[1];

                       }

                       else {
                           finalText += name;
                           finalText += '\n\nTool Name: ' + toolVal[1];
                           finalText += '\n\nEffort:NA';
                           finalText = finalText;
                       }
                   }
                   else {
                       finalText += name;
                       finalText += '\n\nTool Name: ' + toolVal[1];
                       finalText += '\n\nEffort: ' + '0.0';


                   }
               }
               else {                   
                       finalText += name;
                       finalText += '\n\nTool Name: ' + toolVal[1];
               }
           }
            // var text = joint.util.breakText(text, { width: width - 2 * padding });            
            this.attr('bodyText/text', finalText, opt);
           
        },
    });

    joint.dia.Element.define('ericsson.Automatic', {
        attrs: {
            body: {
                //ref:'bodyText',
                refWidth: '100%',
                refHeight: '100%',
                strokeWidth: 2,
                stroke: '#000000',
                fill: '#FFFFFF',

            },
            header: {

                refWidth: '100%',
                height: 30,
                strokeWidth: 2,
                stroke: '#000000',
                fill: '#FFFFFF',

            },
            headerText: {
                textVerticalAnchor: 'middle',
                textAnchor: 'middle',
                refX: '50%',
                refY: 15,
                fontSize: 16,
                fill: '#333333'
            },
            bodyText: {

                textVerticalAnchor: 'middle',
                textAnchor: 'middle',
                refX: '50%',
                refY: '50%',
                refY2: 15,
                fontSize: 10,
                fill: '#333333'
            },
            footer: {
                //ref:'body',
                refWidth: '100%',
                height: 30,
                strokeWidth: 2,
                stroke: '#000000',
                fill: '#FFFFFF',
                //refX:0,
                //refY:'50%'
            },
        },
        name: '',
        //tool: '@',
        execType: 'Automatic',
        avgEstdTime: "0",
        botType: '',
        sadCount: 0,
        outputUpload: 'NO',
        isStepInstructionExist: false,
       
    }, {

            //markup: '<g class="rotatable"><g class="scalable"><polygon class="outer"/><polygon class="inner"/></g><text/></g>',
            markup: [{
                tagName: 'rect',
                selector: 'body'
            }, {
                tagName: 'rect',
                selector: 'header'
            }, {
                tagName: 'text',
                selector: 'headerText'
            }, {
                tagName: 'text',
                selector: 'bodyText'
            }, {
                tagName: 'rect',
                selector: 'footer'
            }],

        initialize: function () {
            joint.dia.Element.prototype.initialize.apply(this, arguments);
            this.on('change:name  change:action change:tool change:rpaid change:responsible change:workInstruction change:cascadeInput', this.updateText, this); //change:avgEstdTime
            this.updateText();
        },
        toJSON: function () {
            let json = joint.dia.Element.prototype.toJSON.apply(this, arguments);
            delete json.myArrayOfOptions;
            delete json.myArrayOfRPAID;
            delete json.myArrayOfTools;
            if (json.myArrayOfWorkInstruction){ delete json.myArrayOfWorkInstruction};
            return json;
        },       
        updateText: function (cell, change, opt) {
            this.attr('headerText/text', 'Automatic', opt);
            var finalText = '';
            var name = this.get('name');           
            var type = this.get('execType');
            var bookingID = '%BOOKING_ID%', bookingStatus = '%STATUS%';
            var current_title = $(document).attr('title');
            var effort = '';
            if (this.attributes.attrs.hasOwnProperty('task') == false) {
                effort = this.get('avgEstdEffort');
            }
            else {
                if (this.attributes.attrs.task.hasOwnProperty('Hours') && (this.attributes.attrs.task.Hours != null || this.attributes.attrs.task.Hours != undefined)) {
                    effort =this.attributes.attrs.task.Hours;
                }
                else {
                    effort = this.get('avgEstdTime').toString();
                }
            }
            var action = this.get('action');
            var toolname = this.get('tool');
            var rpalist = this.get('rpaid');
            let responsible = this.get('responsible');
            let workInstruction = this.get('workInstruction');
          
            let rejectReason = "%REASON%";
            let outputLink = '';
            if (rpalist == undefined) { rpalist = "@" }
            var toolVal = []; let WIValue = [];

            if (action != undefined && toolname != undefined ){
                
                //&& toolname != undefined && rpalist != undefined && responsible !=undefined) {   //  var rpaVal = rpalist.split('@');
                var taskVal = action.split('@');            
                toolVal = toolname.split('@');

                if (this.attributes.attrs.task != undefined) {
                    if (this.attributes.attrs.task.bookingID == '' || this.attributes.attrs.task.status == '') {
                        bookingID = '%BOOKING_ID%'; bookingStatus = '%STATUS%';
                    }
                    else {
                        bookingID = this.attributes.attrs.task.bookingID;
                        if (this.attributes.attrs.task.status == undefined) {
                            bookingStatus = '%STATUS%';
                        }
                        else {
                            bookingStatus = this.attributes.attrs.task.status;
                        }
                        rejectReason = this.attributes.attrs.task.reason;
                        outputLink = this.attributes.attrs.task.outputLink;
                    }
                }
                this.attr('task', {
                    'avgEstdEffort': effort, 'bookingID': bookingID, 'executionType': type, 'reason': rejectReason, 'status': bookingStatus, 'taskID': taskVal[0], 'taskName': taskVal[1], 'toolID': toolVal[0], 'toolName': toolVal[1], 'outputLink': outputLink }, opt);
               
                this.attr('text/taskName', taskVal[1], opt);
                        
            }
            else{ toolVal[1]='' }
            
            if(rpalist != undefined){
                var rpaVal = rpalist.split('@');
                this.attr('tool', { 'RPAID': rpaVal[0], 'RPAName': rpaVal[1] }, opt);
            }

            if(responsible != undefined){
                this.attr('responsible',{'Responsible': responsible});
                this.attr('headerText/text', this.attr('headerText/text') + "  (" + responsible + ")", opt);                
            }

            if(workInstruction != undefined){
                WIValue = workInstruction.split('@');
                this.attr('workInstruction', {'WIName':WIValue[1], 'WILink':WIValue[0], 'WIID':WIValue[3]}, opt); 
            }
          
            var width = this.attributes.size.width, padding = 10;
          
            if (this.attr('bodyText/text') != undefined) {

                if (effort != undefined || effort != null) {
                    finalText += name;
                    finalText += '\n\nTool Name: ' + toolVal[1];
                    finalText += '\n\nEffort:' + effort;
                }
                else if (this.attr('bodyText/text').match(/(^|\W)Effort($|\W)/)) {
                    // text = this.attr('text/text')
                    finalText += name;
                    finalText += '\n\nTool Name: ' + toolVal[1];
                    finalText += '\n\nEffort:' + this.attr('bodyText/text').split('Effort:')[1];
                }
                else {
                    finalText += name;
                    finalText += '\n\nTool Name: ' + toolVal[1];
                    finalText += '\n\n';
                    finalText = finalText;
                }
            
            }
            else {
                finalText += name;
                finalText += '\n\nTool Name: ' + toolVal[1];
                finalText += '\n\nEffort: ' + '0.0';
            }

            // let text = joint.util.breakText(text, { width: width - 2 * padding }, { 'font-size': 12 });
            //this.attr('text/text', finalText);
            this.attr('bodyText/text', finalText, opt);

        }
        });


    joint.dia.Element.define('ericsson.Decision', {
        attrs: {
            body: {

                refPoints: '0 0 10 0 10 10 0 10',
                strokeWidth: 2,
                stroke: '#333333',
                fill: '#FFFFFF',
                refWidth: '80%',
            },
            label: {
                ref: 'body',
                textVerticalAnchor: 'middle',
                textAnchor: 'middle',
                refX: '50%',
                refY: '50%',
                fontSize: 14,
                fill: '#333333',

            }
        }
    }, {
            markup: [{
                tagName: 'polygon',
                selector: 'body'
            }, {
                tagName: 'text',
                selector: 'label'
            }]
        });


})(joint);
