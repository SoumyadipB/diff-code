(function (joint, util, V) {

    var Element = joint.dia.Element;
    var ElementView = joint.dia.ElementView;

    Element.define('html.Element', {
        size: {
            width: 250,
            height: 228
        },
        z: 2,
        fields: {
            header: 'Task',
            name: '',
            resource: '',
            state: ''
        },
        attrs: {
            root: {
                magnet: false
            },
            placeholder: {
                refWidth: '100%',
                refHeight: '100%',
                fill: '#FFFFFF',
                stroke: '#D4D4D4'
            }
        },
        ports: {
            groups: {
                custom: {
                    position: 'absolute',
                    markup: [{
                        tagName: 'rect',
                        selector: 'portBody',
                        attributes: {
                            'width': 24,
                            'height': 24,
                            'x': -12,
                            'y': -12
                        }
                    }, {
                        tagName: 'text',
                        selector: 'portLabel'
                    }],
                    attrs: {
                        portBody: {
                            magnet: 'active',
                            fill: '#4666E5'
                        },
                        portLabel: {
                            pointerEvents: 'none',
                            textAnchor: 'middle',
                            textVerticalAnchor: 'middle',
                            fontFamily: 'sans-serif',
                            fill: 'white'
                        }
                    }
                }
            }
        }
    }, {
            markup: [{
                tagName: 'rect',
                selector: 'placeholder'
            }],
            htmlMarkup: [{
                tagName: 'div',
                className: 'html-element',
                selector: 'htmlRoot',
                style: {
                    'position': 'absolute',
                    'pointer-events': 'none',
                    'user-select': 'none',
                    'box-sizing': 'border-box'
                },
                children: []
            }],
            resetFields: function () {
                this.set('fields', {});
            }
        });

    /*AppScan Data Populate */
    let appScanOptions = [{
        tagName: 'option',
        textContent: 'Select App',
        attributes: {
            'value': '-1',
            //'disabled': 'disabled'
            
        }
    }].concat(StaticJson.scanType[0].appType.map(function (app_type) {

        if (app_type.value == "customApp") {
            return {
                tagName: 'option',
                textContent: app_type.appName,
                attributes: {
                    'value': app_type.value
                },
                style: {
                    'display': 'none'
                }
            }
        } else {
            return {
                tagName: 'option',
                textContent: app_type.appName,
                attributes: {
                    'value': app_type.value
                }
            }
        }
    }));

    joint.shapes.html.Element.define('html.AppScan', {
        size: {
            width: 210,
            height: 230
        },
        typeName: 'Scanner',
        ports: {
            items: [{
                id: 'and',
                group: 'custom',
                args: {
                    x: 85,
                    y: -12
                },
                attrs: {
                    portLabel: {
                        text: '&'
                    }
                }
            }, {
                id: 'or',
                group: 'custom',
                args: {
                    x: 115,
                    y: -12
                },
                attrs: {
                    portLabel: {
                        text: '||'
                    }
                }
            }, {
                id: 'r',
                group: 'custom',
                args: {
                    x: '50%',
                    y: 241
                },
                attrs: {
                    portLabel: {
                        text: 'R'
                    }
                }
            }, {
                id: 'out',
                group: 'custom',
                args: {
                    x: 222,
                    y: '50%'
                },
                attrs: {
                    portLabel: {
                        text: 'O'
                    }
                }
            }, {
                id: 'in',
                group: 'custom',
                args: {
                    x: -12,
                    y: '50%'
                },
                attrs: {
                    portLabel: {
                        text: 'I'
                    },
                    portBody: {
                        magnet: 'passive'
                    }
                }
            }]
        },
        fields: {
            triggerOnClose: false,
            customApp: '',
            app:['-1']
        },
        htmlMarkup: [{
            tagName: 'label',
            className: 'html-element-header',
            textContent: 'App Scan',
        },
            {
                tagName: 'label',
                className: 'html-element-label',
                textContent: 'Select App',
                children: [
                    //{
                    //    tagName: 'span',
                    //    className: 'html-element-label',
                    //    textContent: '*',
                    //    style: {
                    //        color:'red'
                    //    }
                    //},
                    {
                        tagName: 'select',
                        className: 'html-element-field',
                        groupSelector: 'field',
                        attributes: {
                            'data-attribute': 'app',
                            'multiple': 'true'
                        },
                        style: {
                            'pointer-events': 'auto'
                        },
                        children: appScanOptions
                    }
                ]
            },
        //}, {
        //    tagName: 'div',
        //    className: 'html-element-note',
        //    textContent: 'Note: Empty corresponds to scan all'
        //}, {
            {
            tagName: 'label',
            className: 'html-element-label',
            textContent: 'Custom Apps',
            children: [{
                tagName: 'input',
                className: 'html-element-field',
                groupSelector: 'field',
                attributes: {
                    'data-attribute': 'customApp',
                    'placeholder': 'Custom Apps'
                },
                style: {
                    'pointer-events': 'auto'
                }
            }]
        }, {
            tagName: 'div',
            className: 'html-element-note',
            textContent: 'Comma Separated'
        }, {
            tagName: 'label',
            className: 'html-element-label',
            textContent: 'Trigger OnClose',
            children: [{
                tagName: 'input',
                className: 'html-element-field',
                groupSelector: 'field',
                attributes: {
                    'data-attribute': 'triggerOnClose',
                    'type': 'checkbox'
                },
                style: {
                    'pointer-events': 'auto'
                }
            }]
        }
        ]
    });

    /* FileScan Data Populate */

    let fsScanOptions = [{
        tagName: 'option',
        textContent: 'Select Location',
        attributes: {
            'value': '-1',
           'disabled':'disabled'
        }
    }].concat(StaticJson.scanType[1].selectLoc.map(function (loc_type) {
        if (loc_type.value == "customPath") {
            return {
                tagName: 'option',
                textContent: loc_type.locName,
                attributes: {
                    'value': loc_type.value
                },
                style: {
                    'display': 'none'
                }
            }
        } else {
            return {
                tagName: 'option',
                textContent: loc_type.locName,
                attributes: {
                    'value': loc_type.value
                }
            }
        }
      
    }));

    joint.shapes.html.Element.define('html.FileScan', {
        size: {
            width: 230,
            height: 180
        },
        typeName: 'Scanner',
        fields: {
            customPath: '',
            app:'-1'
        },
        ports: {
            items: [{
                id: 'and',
                group: 'custom',
                args: {
                    x: 95,
                    y: -12
                },
                attrs: {
                    portLabel: {
                        text: '&'
                    }
                }
            }, {
                id: 'or',
                group: 'custom',
                args: {
                    x: 125,
                    y: -12
                },
                attrs: {
                    portLabel: {
                        text: '||'
                    }
                }
            }, {
                id: 'r',
                group: 'custom',
                args: {
                    x: '50%',
                    y: 192
                },
                attrs: {
                    portLabel: {
                        text: 'R'
                    }
                }
            }, {
                id: 'out',
                group: 'custom',
                args: {
                    x: 242,
                    y: '50%'
                },
                attrs: {
                    portLabel: {
                        text: 'O'
                    }
                }
            }, {
                id: 'in',
                group: 'custom',
                args: {
                    x: -12,
                    y: '50%'
                },
                attrs: {
                    portLabel: {
                        text: 'I'
                    },
                    portBody: {
                        magnet: 'passive'
                    }
                }
            }]
        },
        htmlMarkup: [{
            tagName: 'label',
            className: 'html-element-header',
            textContent: 'FS Scan',
        }, {
            tagName: 'label',
            className: 'html-element-label',
            textContent: 'Locations',
            children: [{
                tagName: 'select',
                className: 'html-element-field',
                groupSelector: 'field',
                attributes: {
                    'data-attribute': 'app'
                },
                style: {
                    'pointer-events': 'auto'
                },
                children: fsScanOptions
            }]
        }, {
            tagName: 'div',
            className: 'html-element-note',
            textContent: 'Note: Invalid Paths are prone to crash'
        }, {
            tagName: 'label',
            className: 'html-element-label',
            //textContent: 'Custom Apps',
            children: [{
                tagName: 'input',
                className: 'html-element-field',
                groupSelector: 'field',
                attributes: {
                    'data-attribute': 'customPath',
                    'placeholder': 'Path',
                    'disabled': 'disabled'
                },
                style: {
                    'pointer-events': 'auto'
                }
            }]
        }, {
            tagName: 'div',
            className: 'html-element-note',
            //textContent: 'Comma Separated'
        }, {
            tagName: 'label',
            className: 'html-element-label',
            textContent: 'Look for subfolders',
            children: [{
                tagName: 'input',
                className: 'html-element-field',
                groupSelector: 'field',
                attributes: {
                    'data-attribute': 'lookForSubfolder',
                    'type': 'checkbox'
                },
                style: {
                    'pointer-events': 'auto'
                }
            }]
        }]
    });

    /*Email Scan */
    joint.shapes.html.Element.define('html.EmailScan', {
        size: {
            width: 210,
            height: 90
        },
        typeName: 'Scanner',
        fields: {
            'timeField': '0',
            'lookForPastEmail': false
        },
        ports: {
            items: [{
                id: 'and',
                group: 'custom',
                args: {
                    x: 85,
                    y: -12
                },
                attrs: {
                    portLabel: {
                        text: '&'
                    }
                }
            }, {
                id: 'or',
                group: 'custom',
                args: {
                    x: 115,
                    y: -12
                },
                attrs: {
                    portLabel: {
                        text: '||'
                    }
                }
            }, {
                id: 'r',
                group: 'custom',
                args: {
                    x: '50%',
                    y: 102
                },
                attrs: {
                    portLabel: {
                        text: 'R'
                    }
                }
            }, {
                id: 'out',
                group: 'custom',
                args: {
                    x: 222,
                    y: '50%'
                },
                attrs: {
                    portLabel: {
                        text: 'O'
                    }
                }
            }, {
                id: 'in',
                group: 'custom',
                args: {
                    x: -12,
                    y: '50%'
                },
                attrs: {
                    portLabel: {
                        text: 'I'
                    },
                    portBody: {
                        magnet: 'passive'
                    }
                }
            }]
        },
        htmlMarkup: [{
            tagName: 'label',
            className: 'html-element-header',
            textContent: 'Email Scan',
        }, {
            tagName: 'label',
            className: 'html-element-label',
            textContent: 'Look For Past Email',
            children: [{
                tagName: 'input',
                className: 'html-element-field',
                groupSelector: 'field',
                attributes: {
                    'data-attribute': 'lookForPastEmail',
                    'type': 'checkbox',
                },
                style: {
                    'pointer-events': 'auto'
                }
            }]
        }]
    },
        //{
        //    initialize: function () {
        //        joint.shapes.html.Element.prototype.initialize.apply(this, arguments);
        //        //    this.addNotification();
        //        this.attributes.fields.lookForPastEmail = false
        //        this.set('fields/lookForPastEmail', 'false');
        //    }
        //}
    );

    joint.shapes.html.Element.define('html.AppCondition', {

        size: {
            width: 142,
            height: 82
        },
        typeName: 'Condition',
        ports: {
            items: [{
                id: 'out',
                group: 'custom',
                args: {
                    x: 154,
                    y: '50%'
                },
                attrs: {
                    portLabel: {
                        text: 'O'
                    }
                }
            }, {
                id: 'in',
                group: 'custom',
                args: {
                    x: -12,
                    y: '50%'
                },
                attrs: {
                    portLabel: {
                        text: 'I'
                    },
                    portBody: {
                        magnet: 'passive'
                    }
                }
            }]
        },
        fields: {
            mode:false
        },
        htmlMarkup: [{
            tagName: 'label',
            className: 'html-element-header',
            textContent: 'App Condition'
        },
        {
            tagName: 'label',
            className: 'switch',
            textContent: '',
            children: [{
                tagName: 'input',
                className: 'inputCheck',
                groupSelector: 'field',
                attributes: {

                    'data-attribute': 'mode',
                    'type': 'checkbox',

                },
                style: {
                    'pointer-events': 'auto'
                }
            },
            {
                tagName: 'div',
                className: 'slider round',
                groupSelector: 'field',
                style: {
                    'pointer-events': 'auto'
                }
            }
            ]
        }
        ]
    });

    joint.shapes.html.Element.define('html.FileCondition', {
        size: {
            width: 142,
            height: 82
        },
        typeName: 'Condition',
        ports: {
            items: [{
                id: 'out',
                group: 'custom',
                args: {
                    x: 154,
                    y: '50%'
                },
                attrs: {
                    portLabel: {
                        text: 'O'
                    }
                }
            }, {
                id: 'in',
                group: 'custom',
                args: {
                    x: -12,
                    y: '50%'
                },
                attrs: {
                    portLabel: {
                        text: 'I'
                    },
                    portBody: {
                        magnet: 'passive'
                    }
                }
            }]
        },
        fields: {
            mode: false
        },
        htmlMarkup: [{
            tagName: 'label',
            className: 'html-element-header FSCon',
            textContent: 'FS Condition'
        },
        {
            tagName: 'label',
            className: 'switch',
            textContent: '',
            children: [{
                tagName: 'input',
                className: '',
                groupSelector: 'field',
                attributes: {

                    'data-attribute': 'mode',
                    'type': 'checkbox',



                },
                style: {
                    'pointer-events': 'auto'
                }
            },
            {
                tagName: 'div',
                className: 'slider round',
                groupSelector: 'field',
                style: {
                    'pointer-events': 'auto'
                }
            }
            ]
        }
        ]

    });


    joint.shapes.html.Element.define('html.EmailCondition', {
        size: {
            width: 142,
            height: 82
        },
        typeName: 'Condition',
        ports: {
            items: [{
                id: 'out',
                group: 'custom',
                args: {
                    x: 154,
                    y: '50%'
                },
                attrs: {
                    portLabel: {
                        text: 'O'
                    }
                }
            }, {
                id: 'in',
                group: 'custom',
                args: {
                    x: -12,
                    y: '50%'
                },
                attrs: {
                    portLabel: {
                        text: 'I'
                    },
                    portBody: {
                        magnet: 'passive'
                    }
                }
            }]
        },
        fields: {
            mode: false
        },
        htmlMarkup: [{
            tagName: 'label',
            className: 'html-element-header',
            textContent: 'Email Condition'
        },
        {
            tagName: 'label',
            className: 'switch',
            textContent: '',
            children: [{
                tagName: 'input',
                className: '',
                groupSelector: 'field',
                attributes: {

                    'data-attribute': 'mode',
                    'type': 'checkbox',



                },
                style: {
                    'pointer-events': 'auto'
                }
            },
            {
                tagName: 'div',
                className: 'slider round',
                groupSelector: 'field',
                style: {
                    'pointer-events': 'auto'
                }
            }
            ]
        }
        ]
    });

    joint.shapes.html.Element.define('html.ConnectorCondition', {
        size: {
            width: 149,
            height: 82
        },
        typeName: 'Condition',
        fields: {
            mode: true
        },
        ports: {
            items: [{
                id: 'in',
                group: 'custom',
                args: {
                    x: 160,
                    y: '50%'
                },
                attrs: {
                    portLabel: {
                        text: 'I'
                    }
                }
            }]
        },
        htmlMarkup: [{
            tagName: 'label',
            className: 'html-element-header',
            textContent: 'Scanner Connector'
        },
        {
            tagName: 'label',
            className: 'switch',
            textContent: '',
            children: [{
                tagName: 'input',
                className: '',
                groupSelector: 'field',
                attributes: {
                    'data-attribute': 'mode',
                    'type': 'checkbox',
                },
                style: {
                    'pointer-events': 'auto'
                }
            },
            {
                tagName: 'div',
                className: 'slider round',
                groupSelector: 'field',
                style: {
                    'pointer-events': 'auto'
                }
            }
            ]
        }
        ]
    });


    joint.shapes.html.Element.define('html.ConnectorNotification', {
        count: 1,
        size: {
            width: 250,
            height: 120
        },
        typeName: 'Notification',
        fields: {
            header: '',
            type: '',
            message: ''
        },
        ports: {
            items: [{
                id: 'in',
                group: 'custom',
                args: {
                    x: -12,
                    y: '50%'
                },
                attrs: {
                    portLabel: {
                        text: 'I'
                    },
                    portBody: {
                        magnet: 'passive'
                    }
                }
            }]
        },
        htmlMarkup: [{

            tagName: 'label',
            className: 'html-element-header',
            textContent: 'Success Action'
        },
        {
            tagName: 'div',
            className: 'html-element-group',
            children: [{
                tagName: 'label',
                className: 'html-element-label',
                children: [{
                    tagName: 'select',
                    className: 'html-element-field',
                    groupSelector: 'field',
                    attributes: {
                        'data-attribute': 'type0'
                    },
                    style: {
                        'pointer-events': 'auto'
                    },
                    children: [{

                        tagName: 'option',
                        textContent: 'Rest API',
                        attributes: {
                            'value': 'restAPI'
                        }
                    }]
                }]
            }]
        },
        {
            tagName: 'label',
            className: 'html-element-label',
            textContent: 'Action Name*',
            children: [{
                tagName: 'select',
                className: 'html-element-field',
                groupSelector: 'field',
                attributes: {
                    'data-attribute': 'actionName0'

                },
                style: {
                    'pointer-events': 'auto'
                },
                children: [{
                    tagName: 'option',
                    textContent: 'Start Task',
                    attributes: {
                        'value': 'startTask'
                    }
                }, {
                    tagName: 'option',
                    textContent: 'Stop Task',
                    attributes: {
                        'value': 'stopTask'
                    }
                }]
            }]
        }
        ]
    }, {
            notificationHeight: 120,


        },

        {
            addNotification: function () {
                var count = this.get('count');
                //+1;
                var htmlMarkup = [];
                for (var i = 0; i < count; i++) {
                    Array.prototype.push.apply(htmlMarkup, this.getNotificationMarkup(i));
                }
                this.set({
                    htmlMarkup: htmlMarkup,
                    count: count,
                    size: {
                        width: 250,
                        height: Math.max(count, 1) * this.notificationHeight - count
                    },
                    fields: {
                        'type0': 'restAPI'

                    }
                });
            }
            //initialize: function () {
            //    joint.shapes.html.Element.prototype.initialize.apply(this, arguments);
            //    this.addNotification();
            //    this.set('fields/type0', 'restAPI');
            //},
            //resetFields: function () {
            //    this.set('fields', {});
            //    this.set('count', 0);
            //    this.addNotification();
            //}
        },
    );



    joint.shapes.html.Element.define('html.AppMatch', {
        count: 1,
        size: {
            width: 210,
            height: 200
        },
        typeName: 'Match',
        fields: {
            //header: 'Log',
            //type: '',
            //message: 'Test Message'
            matchattr: '-1',
            matchType: '-1',
            operator: '-1'
        },
        ports: {
            items: [{
                id: 'in',
                group: 'custom',
                args: {
                    x: -12,
                    y: '50%'
                },
                attrs: {
                    portLabel: {
                        text: 'I'
                    },
                    portBody: {
                        magnet: 'passive'
                    }
                }
            }]
        },
        htmlMarkup: getAppMatchHTMLMarkup()
    }, {
            notificationHeight: 200,
            // getNotificationMarkup: getAppMatchHTMLMarkup(),
            getHTMLMarkup: getAppMatchHTMLMarkup,

            addNotification: function () {
                var count = this.get('count');
                //+ 1;
                var htmlMarkup = [];
                for (var i = 0; i < count; i++) {
                    Array.prototype.push.apply(htmlMarkup, getAppMatchHTMLMarkup());
                }

                this.set({
                    htmlMarkup: htmlMarkup,
                    count: count,
                    size: {
                        width: 210,
                        height: Math.max(count, 1) * this.notificationHeight - count
                    }
                });
            }
            //initialize: function () {
            //    joint.shapes.html.Element.prototype.initialize.apply(this, arguments);
            //    this.addNotification();
            //},
            //resetFields: function () {
            //    this.set('fields', {});
            //    this.set('count', 0);
            //    this.addNotification();
            //}
        }, {
            getHTMLMarkup: getAppMatchHTMLMarkup
        });

    joint.shapes.html.Element.define('html.FileMatch', {
        count: 1,
        size: {
            width: 230,
            height: 200
        },
        typeName: 'Match',
        fields: {     
            matchType:'-1'
        },
        ports: {
            items: [{
                id: 'in',
                group: 'custom',
                args: {
                    x: -12,
                    y: '50%'
                },
                attrs: {
                    portLabel: {
                        text: 'I'
                    },
                    portBody: {
                        magnet: 'passive'
                    }
                }
            }]
        },
        htmlMarkup: getFSMatchHTMLMarkup()
    }, {
            notificationHeight: 230,
            // getNotificationMarkup: getAppMatchHTMLMarkup(),
            getHTMLMarkup: getFSMatchHTMLMarkup,

            addNotification: function () {
                var count = this.get('count');
                //+ 1;
                var htmlMarkup = [];
                for (var i = 0; i < count; i++) {
                    Array.prototype.push.apply(htmlMarkup, getFSMatchHTMLMarkup());
                }

                this.set({
                    htmlMarkup: htmlMarkup,
                    count: count,
                    size: {
                        width: 250,
                        height: Math.max(count, 1) * this.notificationHeight - count
                    }
                });
            }
            //initialize: function () {
            //    joint.shapes.html.Element.prototype.initialize.apply(this, arguments);
            //    this.addNotification();
            //},
            //resetFields: function () {
            //    this.set('fields', {});
            //    this.set('count', 0);
            //    this.addNotification();
            //}
        }, {
            getHTMLMarkup: getFSMatchHTMLMarkup
        });

    joint.shapes.html.Element.define('html.EmailMatch', {
        count: 1,
        size: {
            width: 210,
            height: 200
        },
        typeName: 'Match',
        fields: {
            header: 'Log',
            type: '',
            message: 'Test Message'
        },
        ports: {
            items: [{
                id: 'in',
                group: 'custom',
                args: {
                    x: -12,
                    y: '50%'
                },
                attrs: {
                    portLabel: {
                        text: 'I'
                    },
                    portBody: {
                        magnet: 'passive'
                    }
                }
            }]
        },
        htmlMarkup: getEmailMatchHTMLMarkup()
    }, {
            notificationHeight: 200,
            // getNotificationMarkup: getAppMatchHTMLMarkup(),
            getHTMLMarkup: getEmailMatchHTMLMarkup,

            addNotification: function () {
                var count = this.get('count');
                //var count = this.get('count') + 1;
                var htmlMarkup = [];
                for (var i = 0; i < count; i++) {
                    Array.prototype.push.apply(htmlMarkup, getEmailMatchHTMLMarkup());
                }

                this.set({
                    htmlMarkup: htmlMarkup,
                    count: count,
                    size: {
                        width: 250,
                        height: Math.max(count, 1) * this.notificationHeight - count
                    }
                });
            }
            //initialize: function () {
            //    joint.shapes.html.Element.prototype.initialize.apply(this, arguments);
            //    this.addNotification();
            //},
            //resetFields: function () {
            //    this.set('fields', {});
            //    this.set('count', 0);
            //    this.addNotification();
            //}
        }, {
            getHTMLMarkup: getEmailMatchHTMLMarkup
        });

    //joint.shapes.html.Element.define('html.')

    // Custom view for JointJS HTML element that displays an HTML <div></div> above the SVG Element.
    joint.shapes.html.ElementView = ElementView.extend({

        html: null,

        presentationAttributes: ElementView.addPresentationAttributes({
            position: ['HTML_UPDATE'],
            size: ['HTML_UPDATE'],
            fields: ['HTML_FIELD_UPDATE'],
            htmlMarkup: ['RENDER', 'HTML_UPDATE', 'HTML_FIELD_UPDATE']
        }),

        // Run these upon first render
        initFlag: ElementView.prototype.initFlag.concat([
            'HTML_UPDATE',
            'HTML_FIELD_UPDATE'
        ]),

        confirmUpdate: function () {
            var flags = ElementView.prototype.confirmUpdate.apply(this, arguments);
            if (this.hasFlag(flags, 'HTML_UPDATE')) {
                this.updateHTML();
                flags = this.removeFlag(flags, 'HTML_UPDATE');
            }
            if (this.hasFlag(flags, 'HTML_FIELD_UPDATE')) {
                this.updateFields();
                flags = this.removeFlag(flags, 'HTML_FIELD_UPDATE');
            }
            return flags;
        },

        onRender: function () {
            this.removeHTMLMarkup();
            this.renderHTMLMarkup();
            return this;
        },

        renderHTMLMarkup: function () {
            var rootMarkup = util.cloneDeep(this.model.htmlMarkup);
            rootMarkup[0].children = this.model.get('htmlMarkup');
            var doc = util.parseDOMJSON(rootMarkup, V.namespace.xhtml);
            var html = doc.selectors.htmlRoot;
            var fields = doc.groupSelectors.field || [];
            var buttons = doc.groupSelectors.button || [];
            // React on all box changes. e.g. input change
            html.addEventListener('change', this.onFieldChange.bind(this), false);
            buttons.forEach(function (button) {
                button.addEventListener('click', this.onButtonClick.bind(this), false);
            }.bind(this), false);
            this.paper.htmlContainer.appendChild(doc.fragment);
            this.html = html;
            this.fields = fields;

            // select2   $(id).select2()
            //  $('select', html).select2();
            // select2 select2 - container select2 - container--classic select2 - container--above  - pointerevent:auto
        },

        removeHTMLMarkup: function () {
            var html = this.html;
            if (!html) return;
            this.paper.htmlContainer.removeChild(html);
            this.html = null;
            this.fields = null;
        },

        updateHTML: function () {
            var bbox = this.model.getBBox();
            var html = this.html;
            html.style.width = bbox.width + 'px';
            html.style.height = bbox.height + 'px';
            html.style.left = bbox.x + 'px';
            html.style.top = bbox.y + 'px';
        },

        onFieldChange: function (evt) {
            var input = evt.target;
            var attribute = input.dataset.attribute;
            if (attribute) {
                var value;
                if (input.tagName.toUpperCase() === 'INPUT' && input.type === 'checkbox') {
                    value = input.checked;
                } else {
                   // value = input.value;
                    //for multiple
                    value = $(input).val();
                }
                //this.model.prop(['fields', attribute], value, {
                //    userAttribute: attribute
                //});
                //for multiple
                this.model.prop(['fields', attribute], value, { userChange: true, rewrite: true });
            }
        },

        onButtonClick: function (evt) {
            var button = evt.target;
            var event = button.dataset.event;
            if (event) {
                this.notify(event);
            }
        },

        updateFields: function () {
            this.fields.forEach(function (field) {
                var attribute = field.dataset.attribute;
                var value = this.model.prop(['fields', attribute]) || '';
                switch (field.tagName.toUpperCase()) {
                    case 'LABEL':
                        field.textContent = value;
                        break;
                    case 'INPUT':
                    case 'SELECT':
                        if (field.type === 'checkbox') {
                            field.checked = Boolean(value);
                        } else {
                            //field.value = value;
                            //for multiple
                            $(field).val(value);
                            if (value) {
                                field.classList.remove('field-empty');
                            } else {
                                field.classList.add('field-empty');
                            }
                        }
                        break;
                    case 'DIV':
                        field.dataset[attribute] = value;
                        break;
                }
            }.bind(this));
        },

        onRemove: function () {
            this.removeTools();
            this.removeHTMLMarkup();
        }
    });

})(joint, joint.util, V);