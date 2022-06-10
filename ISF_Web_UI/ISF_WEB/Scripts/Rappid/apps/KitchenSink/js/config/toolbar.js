/*! Rappid v2.3.3 - HTML5 Diagramming Framework

Copyright (c) 2015 client IO

 2018-08-03 


This Source Code Form is subject to the terms of the Rappid License
, v. 2.0. If a copy of the Rappid License was not distributed with this
file, You can obtain one at http://jointjs.com/license/rappid_v2.txt
 or from the Rappid archive as was distributed by client IO. See the LICENSE file.*/


var App = App || {};
App.config = App.config || {};

(function () {

    'use strict';

    App.config.toolbar = {
        groups: {
            'undo-redo': { index: 1 },
            'export': { index: 1 },
            'fullscreen': { index: 2 },
            'layout': { index: 2 },
            'zoom': { index: 2 },  
            'update': { index: 3 },
            'workflow': { index: 3, align: "right" },
            'order': { index: 4 },
                     
        },
        tools: [
            {
                type: 'undo',
                name: 'undo',
                group: 'undo-redo',
                attrs: {
                    button: {
                        'data-tooltip': 'Undo',
                        'data-tooltip-position': 'top',
                        'data-tooltip-position-selector': '.toolbar-container'
                    }
                }
            },
            {
                type: 'redo',
                name: 'redo',
                group: 'undo-redo',
                attrs: {
                    button: {
                        'data-tooltip': 'Redo',
                        'data-tooltip-position': 'top',
                        'data-tooltip-position-selector': '.toolbar-container'
                    }
                }
            },
            {
                type: 'button',
                name: 'update',
                group: 'update',
                text: 'Update',
                attrs: {
                    button: {
                        id: 'btn-update',
                        'data-tooltip': 'Update json',
                        'data-tooltip-position': 'top',
                        'data-tooltip-position-selector': '.toolbar-container'
                    }
                }
            },
            {
                     type: 'button',
                     name: 'exec',
                     group: 'workflow',
                     text: 'Execute WorkFlow',
                     attrs: {
                         button: {
                             id: 'btn-exec',
                             'data-tooltip': 'Execute WorkFlow',
                             'data-tooltip-position': 'top',
                             'data-tooltip-position-selector': '.toolbar-container'
                         }
                     }
             },
             {
                          type: 'button',
                          name: 'edit',
                          group: 'workflow',
                          text: 'Edit WorkFlow',
                          attrs: {
                              button: {
                                  id: 'btn-edit',
                                  'data-tooltip': 'Edit WorkFlow',
                                  'data-tooltip-position': 'top',
                                  'data-tooltip-position-selector': '.toolbar-container'
                              }
                          }
             },
            //{
            //    type: 'button',
            //    name: 'clear',
            //    group: 'clear',
            //    attrs: {
            //        button: {
            //            id: 'btn-clear',
            //            'data-tooltip': 'Clear Paper',
            //            'data-tooltip-position': 'top',
            //            'data-tooltip-position-selector': '.toolbar-container'
            //        }
            //    }
            //},
            //{
            //    type: 'button',
            //    name: 'svg',
            //    group: 'export',
            //    text: 'Export SVG',
            //    attrs: {
            //        button: {
            //            id: 'btn-svg',
            //            'data-tooltip': 'Open as SVG in a pop-up',
            //            'data-tooltip-position': 'top',
            //            'data-tooltip-position-selector': '.toolbar-container'
            //        }
            //    }
            //},
            {
                type: 'button',
                name: 'step',
                group: 'workflow',
                text: 'Add Step',
                attrs: {
                    button: {
                        id: 'btn-add-step',
                        'data-tooltip': 'Add Step',
                        'data-tooltip-position': 'top',
                        'data-tooltip-position-selector': '.toolbar-container'

                    }
                }
            },
            {
                type: 'button',
                name: 'png',
                group: 'export',
                text: 'Export PNG',
                attrs: {
                    button: {
                        id: 'btn-png',
                        'data-tooltip': 'Open as PNG in a pop-up',
                        'data-tooltip-position': 'top',
                        'data-tooltip-position-selector': '.toolbar-container'
                    }
                }
            },
            //{
            //    type: 'button',
            //    name: 'print',
            //    group: 'print',
            //    attrs: {
            //        button: {
            //            id: 'btn-print',
            //            'data-tooltip': 'Open a Print Dialog',
            //            'data-tooltip-position': 'top',
            //            'data-tooltip-position-selector': '.toolbar-container'
            //        }
            //    }
            //},
            //{
            //    type: 'button',
            //    name: 'to-front',
            //    group: 'order',
            //    text: 'Send To Front',
            //    attrs: {
            //        button: {
            //            id: 'btn-to-front',
            //            'data-tooltip': 'Bring Object to Front',
            //            'data-tooltip-position': 'top',
            //            'data-tooltip-position-selector': '.toolbar-container'
            //        }
            //    }
            //},
            //{
            //    type: 'button',
            //    name: 'to-back',
            //    group: 'order',
            //    text: 'Send To Back',
            //    attrs: {
            //        button: {
            //            id: 'btn-to-back',
            //            'data-tooltip': 'Send Object to Back',
            //            'data-tooltip-position': 'top',
            //            'data-tooltip-position-selector': '.toolbar-container'
            //        }
            //    }
            //},
            {
                type: 'button',
                group: 'layout',
                name: 'layout',
                attrs: {
                    button: {
                        id: 'btn-layout',
                        'data-tooltip': 'Auto-layout Graph',
                        'data-tooltip-position': 'top',
                        'data-tooltip-position-selector': '.toolbar-container'
                    }
                }
            },
            {
                type: 'zoom-to-fit',
                name: 'zoom-to-fit',
                group: 'zoom',
                attrs: {
                    button: {
                        'data-tooltip': 'Zoom To Fit',
                        'data-tooltip-position': 'top',
                        'data-tooltip-position-selector': '.toolbar-container'
                    }
                }
            },
            {
                type: 'zoom-out',
                name: 'zoom-out',
                group: 'zoom',
                attrs: {
                    button: {
                        'data-tooltip': 'Zoom Out',
                        'data-tooltip-position': 'top',
                        'data-tooltip-position-selector': '.toolbar-container'
                    }
                }
            },
            //{
            //    type: 'label',
            //    name: 'zoom-slider-label',
            //    group: 'zoom',
            //    text: 'Zoom:'
            //},
            //{
            //    type: 'zoom-slider',
            //    name: 'zoom-slider',
            //    group: 'zoom'
            //},
            {
                type: 'zoom-in',
                name: 'zoom-in',
                group: 'zoom',
                attrs: {
                    button: {
                        'data-tooltip': 'Zoom In',
                        'data-tooltip-position': 'top',
                        'data-tooltip-position-selector': '.toolbar-container'
                    }
                }
            },
            //{
            //    type: 'separator',
            //    group: 'grid'
            //},
            //{
            //    type: 'label',
            //    name: 'grid-size-label',
            //    group: 'grid',
            //    text: 'Grid size:',
            //    attrs: {
            //        label: {
            //            'data-tooltip': 'Change Grid Size',
            //            'data-tooltip-position': 'top',
            //            'data-tooltip-position-selector': '.toolbar-container'
            //        }
            //    }
            //},
            //{
            //    type: 'range',
            //    name: 'grid-size',
            //    group: 'grid',
            //    text: 'Grid size:',
            //    min: 1,
            //    max: 50,
            //    step: 1,
            //    value: 10
            //},
            //{
            //    type: 'separator',
            //    group: 'snapline'
            //},
            //{
            //    type: 'checkbox',
            //    name: 'snapline',
            //    group: 'snapline',
            //    label: 'Snaplines:',
            //    value: true,
            //    attrs: {
            //        input: {
            //            id: 'snapline-switch'
            //        },
            //        label: {
            //            'data-tooltip': 'Enable/Disable Snaplines',
            //            'data-tooltip-position': 'top',
            //            'data-tooltip-position-selector': '.toolbar-container'
            //        }
            //    }
            //},
            {
                type: 'fullscreen',
                name: 'fullscreen',
                group: 'fullscreen',
                attrs: {
                    button: {
                        'data-tooltip': 'Toggle Fullscreen Mode',
                        'data-tooltip-position': 'top',
                        'data-tooltip-position-selector': '.toolbar-container'
                    }
                }
            },
            {
                   type: 'toggle',
                   id:'toggleEditMode',
                   label: 'EditMode',
                   name: 'toggle',
                   group:'workflow',
                   value: true,
                   attrs: {
                       'fontsize':'10px',
                   }
               },
            {
                  type: 'button',
                  name: 'back',
                  group: 'workflow',
                  text: 'Back',
                  attrs: {
                  button: {
                            id: 'btn-back',
                            'data-tooltip': 'Update json',
                            'data-tooltip-position': 'top',
                            'data-tooltip-position-selector': '.toolbar-container'
                    }
                }
            },
        ]
    };
})();
