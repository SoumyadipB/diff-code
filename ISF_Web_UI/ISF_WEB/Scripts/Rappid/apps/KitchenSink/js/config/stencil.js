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

    App.config.stencil = {};

    App.config.stencil.groups = {
        ericsson: { index: 1, label: 'Ericsson Tools' },
        //standard: { index: 2, label: 'Standard shapes' },
        //fsa: { index: 2, label: 'State machine' },
        //pn: { index: 3, label: 'Petri nets' },
        //erd: { index: 4, label: 'Entity-relationship' },
        //uml: { index: 5, label: 'UML' },
        //org: { index: 6, label: 'ORG' }
    };

    App.config.stencil.shapes = {};


    App.config.stencil.shapes.ericsson = [
      

        /*****************************Custom Shapes******************************/
        {
            type: 'ericsson.Manual',
            //size: { width: 190, height: 90 },
            size: { width: 5, height: 4 },
            attrs: {
                root: {
                    dataTooltip: 'Manual Step',
                    dataTooltipPosition: 'right',
                    dataTooltipPositionSelector: '.joint-stencil'
                },
                body: {
                    fill: 'white',
                    stroke: '#31d0c6',
                    strokeWidth: 2,
                    strokeDasharray: '0',

                },
                header: {
                    stroke: '#31d0c6',
                    fill: '#31d0c6',
                    strokeWidth: 2,
                    strokeDasharray: '0',
                    height: 25
                },
                bodyText: {
                    text: '',
                    //fill: '#c6c7e2',
                    fill: 'black',
                    fontFamily: 'Roboto Condensed',
                    fontWeight: 'Normal',
                    fontSize: 10,
                    strokeWidth: 0,
                    //refY2: 12,
                },
                headerText: {
                    text: 'Manual',
                    fill: '#f6f6f6',
                    fontFamily: 'Roboto Condensed',
                    fontWeight: 'Normal',
                    fontSize: 15,
                    strokeWidth: 0,
                    refY: 10
                },
                footer: {
                    stroke: '#31d0c6',
                    fill: '#31d0c6',
                    strokeWidth: 2,
                    strokeDasharray: '0',
                    height: 30,
                    refY: '100%',
                    refY2: -30,
                }
            }
           
        },
              {
                  type: 'ericsson.Automatic',
                  size: { width: 5, height: 4 },
                  attrs: {
                      root: {
                          dataTooltip: 'Automatic Step',
                          dataTooltipPosition: 'left',
                          dataTooltipPositionSelector: '.joint-stencil'
                      },
                      body: {
                          fill: 'white',
                          stroke: '#f39b17',
                          strokeWidth: 2,
                          strokeDasharray: '0',

                      },
                      header: {
                          stroke: '#f39b17',
                          fill: '#f39b17',
                          strokeWidth: 2,
                          strokeDasharray: '0',
                          height: 25
                      },
                      bodyText: {
                          text: '',
                          fill: 'black',
                          fontFamily: 'Roboto Condensed',
                          fontWeight: 'Normal',
                          fontSize: 10,
                          strokeWidth: 0,
                          refY2: 0,
                      },
                      headerText: {
                          text: 'Automatic',
                          fill: '#f6f6f6',
                          fontFamily: 'Roboto Condensed',
                          fontWeight: 'Normal',
                          fontSize: 15,
                          strokeWidth: 0,
                          refY: 10
                      },
                      footer: {
                          stroke: '#f39b17',
                          fill: '#f39b17',
                          strokeWidth: 2,
                          strokeDasharray: '0',
                          height: 30,
                          refY: '100%',
                          refY2: -30,
                      }
                  }
        },
        {
            type: 'ericsson.Decision',
            attrs: {
                root: {
                    dataTooltip: 'Decision Step',
                    dataTooltipPosition: 'left',
                    dataTooltipPositionSelector: '.joint-stencil'
                },
                body: {
                    refPoints: '50,0 100,50 50,100 0,50',
                    fill: 'white',
                    stroke: '#7c7ac7',
                    strokeWidth: 2,
                    strokeDasharray: '0'
                },
                label: {
                    ref: 'body',
                    text: '',
                    fill: 'black',
                    fontFamily: 'Roboto Condensed',
                    fontWeight: 'Normal',
                    fontSize: 11,
                    strokeWidth: 0
                }
            }
        },

        {
            type: 'ericsson.StartStep',
            preserveAspectRatio: true,

            attrs: {
                root: {
                    dataTooltip: 'Start Step',
                    dataTooltipPosition: 'left',
                    dataTooltipPositionSelector: '.joint-stencil'
                },
                circle: {
                    cx: 30,
                    cy: 30,
                    fill: 'green',
                    'stroke-width': 0
                },
                text: {
                    x: 17,
                    y: 35,
                    text: 'Start',
                    fill: 'white',
                    'font-family': 'Roboto Condensed',
                    'font-weight': 'Normal',
                    'font-size': 15,
                    'stroke-width': 0
                }
            }
        },
        {
            type: 'ericsson.EndStep',
            preserveAspectRatio: true,
            attrs: {
                root: {
                    dataTooltip: 'End Step',
                    dataTooltipPosition: 'left',
                    dataTooltipPositionSelector: '.joint-stencil'
                },
                circle: {
                    cx: 30,
                    cy: 30,
                    fill: 'red',
                    'stroke-width': 0
                },
                text: {
                    x: 17,
                    y: 35,
                    text: 'Stop',
                    fill: 'white',
                    'font-family': 'Roboto Condensed',
                    'font-weight': 'Normal',
                    'font-size': 15,
                    'stroke-width': 0
                }
            }
        }
    ];

    

    App.config.stencil.shapes.erd = [

        
        //{
        //    type: 'erd.Relationship',
        //    attrs: {
        //        root: {
        //            dataTooltip: 'Relationship',
        //            dataTooltipPosition: 'left',
        //            dataTooltipPositionSelector: '.joint-stencil'
        //        },
        //        '.outer': {
        //            fill: '#61549c',
        //            stroke: 'transparent',
        //            'stroke-width': 2,
        //            'stroke-dasharray': '0'
        //        },
        //        text: {
        //            text: 'Relation',
        //            'font-size': 11,
        //            'font-family': 'Roboto Condensed',
        //            'font-weight': 'Normal',
        //            fill: '#f6f6f6',
        //            'stroke-width': 0
        //        }
        //    }
        //},
        
    ];

    

})();
