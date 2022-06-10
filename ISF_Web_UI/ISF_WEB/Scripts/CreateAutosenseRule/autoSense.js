//(function(joint, V) {

// Notes:
// - Currently, there is no support for z-indexes on HTML Elements
// - It's not possible to export the diagram into PNG/SVG on the client-side
// - Do not use CSS background on the root HTML element when using ports (or have ports outside the shape)
//var isEdit;

var isZoomed = false;
var isFirstVisit = true;

$(document).ready(function () {
    let $ruleDataParentObj = window.parent.$('#autosenseRuleData');
    let checkEdit = $ruleDataParentObj.attr("data-checkEdit");

    pwIsf.removeLayer();

    if (checkEdit == "true") {
        pwIsf.addLayer({ text: "Please wait" });
    }
    $('select').select2();
    window.parent.$('#saveRule').html("SAVE THIS RULE");
   
    
    let checkCopy = $ruleDataParentObj.attr("data-checkCopy");
    let dropDownFill = $ruleDataParentObj.attr("data-dropdownFill");
    let mgnPlot = $ruleDataParentObj.attr("data-mgnPlot");
    let pageType = $ruleDataParentObj.attr("data-name");
    let fileName = '';
    fileName = $ruleDataParentObj.attr("data-fileName");
    ownerSignum = '';
    ownerSignum = $ruleDataParentObj.attr("data-ownerSignum");

    (ownerSignum == 'null') ? ownerSignum = '' : ownerSignum = ownerSignum;

    let isMigrate = $ruleDataParentObj.attr("data-isMigrate");

    if (isMigrate == true || isMigrate == "true") {
            $("#autoSignumMigrate").show();
            $("#ruleActiveCheckboxDiv").hide();
    }
    else {
        $("#autoSignumMigrate").hide();
        $("#ruleActiveCheckboxDiv").show();
    }
    
    if (pageType == "migratePlus" || checkCopy != "true") {

      //  $("#autoSignumMigrate").show();
      //  $("#ruleActiveCheckboxDiv").hide();
        if (ownerSignum != null || ownerSignum != undefined || ownerSignum != '' /*&& ownerSignum*/) {
            $("#workOrderViewIdPrefix_assigned_to").val(ownerSignum)
        }
       
    }

    else if (checkCopy != "true") {

      //  $("#autoSignumMigrate").show();
      //  $("#ruleActiveCheckboxDiv").hide();
        if (ownerSignum != null || ownerSignum != undefined || ownerSignum != '' /*&& ownerSignum*/) {
            $("#workOrderViewIdPrefix_assigned_to").val(ownerSignum)
        }
    }
    else {
      //  $("#autoSignumMigrate").hide();
      //  $("#ruleActiveCheckboxDiv").show();
    }   

    getAllSignumForEditWODetails();

    let $ruleSaveParentObj = window.parent.$('#saveRule');
    //For populating all data in autosense dialog in edit & copy mode
    if (checkEdit == "true") {
        $('#ruleDesc').val($ruleDataParentObj.attr('data-ruleDescription'));
        $('#ruleName').val($ruleDataParentObj.attr('data-ruleName'));

       
        if (checkCopy == "true") {
            //make copy of rule
            $ruleSaveParentObj.html("SAVE THIS RULE");
            $($ruleSaveParentObj).attr('data-mode', 'copy');
         //   $("#autoSignumMigrate").hide();

        }
        else if (checkCopy == "false") {
            $ruleSaveParentObj.html("SAVE THIS RULE");
            $($ruleSaveParentObj).attr('data-mode', 'edit');

        }

        let activeCheck = $ruleDataParentObj.attr('data-active');
        if (activeCheck == "true") {
            $("#rule_active").prop("checked", true);
        }

        //for rule migration flowchart plot
        if (mgnPlot == "true") {
            $("#ruleActiveCheckboxDiv").hide();
            $($ruleSaveParentObj).attr('data-mode', 'edit');
         //   $("#autoSignumMigrate").show();
            let tableEditButtonId = $ruleDataParentObj.attr('data-id');
            if (tableEditButtonId == "ruleEditIcon") {
                $ruleSaveParentObj.html("SAVE THIS RULE"); //2VN in case they ask in future to change the button title to Edit this rule just change here
                $($ruleSaveParentObj).attr('data-mode', 'edit');
            }
            else {
                $ruleSaveParentObj.html("SAVE THIS RULE");
            }
           
            plotGraphRuleMgn();
        }
        else {
            $("#ruleActiveCheckboxDiv").show();
            editAndCopyRule();
        }


    }
    else {
        $ruleSaveParentObj.html("SAVE THIS RULE");
        $($ruleSaveParentObj).attr('data-mode', 'save');
    }

    //cascaded dropdown API wont give error    
    fillServiceAreaFilter(checkEdit,mgnPlot,dropDownFill);
    
    
 //   window.parent.$('#saveRule').prop('disabled', true); window.parent.$('#saveRule').css("background", "white"); 
   // pwIsf.removeLayer();
    //let validateRuleJsonReturn = validateRuleJson();
});

var graph = new joint.dia.Graph;

// get service area on page load ready function
function fillServiceAreaFilter(checkEdit,mgnPlot,dropDownFill) {
    $("#select_serviceAreaRule").empty();
    $("#select_serviceAreaRule").append("<option value=''>Select Service Area/Sub Service Area</option>");
  //  pwIsf.addLayer({ text: "Please wait" });
    $.isf.ajax({
        url: service_java_URL + "activityMaster/getServiceAreaDetails/",
      //  async: false,
        success: function (data) {
            
            $.each(data, function (i, d) {
                $("#select_serviceAreaRule").append("<option value='" + d.serviceAreaID + "'>" + d.serviceArea + "</option>")
            })
           //For editing the rule in autosense rule creation & migration while getting values in cascaded select boxes
            //dropDownFill set true for the edit case in rulemigration where rule is verified,set false for unverified.
            if (checkEdit == 'true') {
                if (mgnPlot == undefined || dropDownFill == "true" || window.parent.$('#autosenseRuleData').attr('data-isFormatUpload'))
                {
                    $("#select_serviceAreaRule").val(window.parent.$('#autosenseRuleData').attr('data-serviceAreaID'));  
                    
                    if (!$("#select_serviceAreaRule option:selected").length) {
                        $("#select_serviceAreaRule").val('');
                    }

                    $("#select_serviceAreaRule").trigger("change");
                  
                    if (!ownerSignum == "null" && ownerSignum) {
                        $("#workOrderViewIdPrefix_assigned_to").val(ownerSignum)
                    }
                }
            
            }

        },
        error: function (xhr, status, statusText) {
            pwIsf.removeLayer();
            console.log('An error occurred');
        },
        complete: function (xhr, statusText) {
           // pwIsf.removeLayer();
        }
    })
}


//Populate Domain dropdown
function fillDomainFilterRule() {
    document.getElementById('select_serviceAreaRuleDiv').getElementsByClassName('select2-selection')[0].style.border = '';
    window.parent.$('#saveRule').prop('disabled', false);
    window.parent.$('#saveRule').css("background", ""); 

    let serviceareaID = $("#select_serviceAreaRule").val();
    $("#select_domainRule").empty().append("<option value=''>Select Domain/Sub Domain</option>");;
    $("#select_activityRule").empty().append("<option value=''>Select Activity/Subactivity</option>");
    $("#select_technologyRule").empty().append("<option value=''>Select Technology</option>");
    $("#select_taskRule").empty().append("<option value=''>Select Task</option>");

   // pwIsf.addLayer({ text: "Please wait" });

    $.isf.ajax({
        url: service_java_URL + "activityMaster/getAllDomainDetailsByService?ServiceAreaID=" + serviceareaID,
      //  async: false,
        success: function (data) {

            data = data.responseData;
            $.each(data, function (i, d) {
                $("#select_domainRule").append("<option value='" + d.domainID + "'>" + d.domain + "</option>")
            })
            let checkEdit = window.parent.$('#autosenseRuleData').attr("data-checkEdit");
            let dropDownFill = window.parent.$('#autosenseRuleData').attr("data-dropdownFill");
            let mgnPlot = window.parent.$('#autosenseRuleData').attr("data-mgnPlot");
            if (checkEdit == 'true') {
                if (mgnPlot == undefined || dropDownFill == "true" || window.parent.$('#autosenseRuleData').attr('data-isFormatUpload')) {
                    $("#select_domainRule").val(window.parent.$('#autosenseRuleData').attr('data-domainID'));

                    if (!$("#select_domainRule option:selected").length) {
                        $("#select_domainRule").val('');
                    }

                    $("#select_domainRule").trigger("change");
                }
              
            }
            
         },
        error: function (xhr, status, statusText) {
            pwIsf.removeLayer();
            console.log('An error occurred');
        },
        complete: function (xhr, statusText) {
          //  pwIsf.removeLayer();

        }
    })
}



//Fill Technology dropdown on change of subactivity drop down
function fillTechFilterRule() {
    document.getElementById('select_domainRuleDiv').getElementsByClassName('select2-selection')[0].style.border = '';
    window.parent.$('#saveRule').prop('disabled', false);
    window.parent.$('#saveRule').css("background", ""); 
    $("#select_technologyRule").empty().append("<option value=''>Select Technology</option>");
    $("#select_activityRule").empty().append("<option value=''>Select Activity/Subactivity</option>");   
    $("#select_taskRule").empty().append("<option value=''>Select Task</option>");
    let serviceareaID = $("#select_serviceAreaRule").val();
    let domainID = $("#select_domainRule").val();

   // pwIsf.addLayer({ text: "Please wait" });

    $.isf.ajax({
        url: service_java_URL + "activityMaster/getTechnologyDetailsByDomain?domainId=" + domainID + "&serviceAreaID=" + serviceareaID,
       // async: false,
        success: function (data) {

            $.each(data, function (i, d) {
                $("#select_technologyRule").append("<option value='" + d.technologyID + "'>" + d.technology + "</option>")
            })
            let checkEdit = window.parent.$('#autosenseRuleData').attr("data-checkEdit");
            let dropDownFill = window.parent.$('#autosenseRuleData').attr("data-dropdownFill");
            let mgnPlot = window.parent.$('#autosenseRuleData').attr("data-mgnPlot");
            if (checkEdit == 'true') {
                if (mgnPlot == undefined || dropDownFill == "true" || window.parent.$('#autosenseRuleData').attr('data-isFormatUpload')) {
                    $("#select_technologyRule").val(window.parent.$('#autosenseRuleData').attr('data-technologyID'));

                    if (!$("#select_technologyRule option:selected").length) {
                        $("#select_technologyRule").val('');
                    }
                    $("#select_technologyRule").trigger("change");
                }

            }
           
        },
        error: function (xhr, status, statusText) {
            pwIsf.removeLayer();
            console.log('An error occurred');
        },
        complete: function (xhr, statusText) {
            
         //   pwIsf.removeLayer();
        }
    })
}


//2VN populate activity drop down on change of technology drop down
function getActivitiesRule() {

    document.getElementById('select_technologyRuleDiv').getElementsByClassName('select2-selection')[0].style.border = '';
    window.parent.$('#saveRule').prop('disabled', false);
    window.parent.$('#saveRule').css("background", ""); 
    let serviceareaID = $("#select_serviceAreaRule").val();
    let domainID = $("#select_domainRule").val();
    let technologyID = $("#select_technologyRule").val();
    $("#select_activityRule").empty().append("<option value=''>Select Activity/Subactivity</option>");
    $("#select_taskRule").empty().append("<option value=''>Select Task</option>");

   // pwIsf.addLayer({ text: "Please wait" });

    $.isf.ajax({
        url: service_java_URL + "activityMaster/getActivityAndSubActivityDetails/" + domainID + "/" + serviceareaID + "/" + technologyID,
     //   async: false,
        success: function (data) {
        
            $.each(data, function (i, d) {
                $('#select_activityRule').append('<option value="' + d.subActivityID + '">' + d.subActivityName + '</option>');
            })
            let checkEdit = window.parent.$('#autosenseRuleData').attr("data-checkEdit");
            let dropDownFill = window.parent.$('#autosenseRuleData').attr("data-dropdownFill");
            let mgnPlot = window.parent.$('#autosenseRuleData').attr("data-mgnPlot");
            if (checkEdit == 'true') {
                if (mgnPlot == undefined || dropDownFill == "true" || window.parent.$('#autosenseRuleData').attr('data-isFormatUpload')) {
                    $("#select_activityRule").val(window.parent.$('#autosenseRuleData').attr('data-subactivityID'));
                    if (!$("#select_activityRule option:selected").length) {
                        $("#select_activityRule").val('');
                    }

                    $("#select_activityRule").trigger("change");
                }

            }
        },
        error: function (xhr, status, statusText) {
            pwIsf.removeLayer();
            console.log('An error occurred');
        },
        complete: function (xhr, statusText) {
         //   pwIsf.removeLayer();

        }
    });

}


//2VN populate Task drop down on change of activity and subactivity drop down.
function getTasksRule() {

    let checkEdit = window.parent.$('#autosenseRuleData').attr("data-checkEdit");
    document.getElementById('select_activityRuleDiv').getElementsByClassName('select2-selection')[0].style.border = '';
    window.parent.$('#saveRule').prop('disabled', false);
    window.parent.$('#saveRule').css("background", ""); 
    let subactivityID = $("#select_activityRule").val();

    $("#select_taskRule").empty();
    $("#select_taskRule").append("<option value=''>Select Task</option>");

  //  pwIsf.addLayer({ text: "Please wait" });

    $.isf.ajax({
        url: service_java_URL + "autoSense/getTaskDetailsBySubactivityID?subActivityID=" + subactivityID,
        //async: false,
        success: function (data) {
            data = data.responseData;
           

            if (!data || data == "") {
                pwIsf.alert({ msg: "No tasks in selected combination", type: "info" });

            }
            else {
                $.each(data, function (i, d) {
                    $('#select_taskRule').append('<option value="' + d.taskID + '">' + d.task + '</option>');
                })
            }
           
            
            let dropDownFill = window.parent.$('#autosenseRuleData').attr("data-dropdownFill");
            let mgnPlot = window.parent.$('#autosenseRuleData').attr("data-mgnPlot");
            if (checkEdit == 'true') {
                if (mgnPlot == undefined || dropDownFill == "true" || window.parent.$('#autosenseRuleData').attr('data-isFormatUpload')) {
                    $("#select_taskRule").val(window.parent.$('#autosenseRuleData').attr('data-taskID'));
                    if (!$("#select_taskRule option:selected").length) {
                        $("#select_taskRule").val('');
                    }
                }              
            }
        },
        error: function (xhr, status, statusText) {
          //  pwIsf.removeLayer();
            console.log('An error occurred');
        },
        complete: function (xhr, statusText) {
            pwIsf.removeLayer();

            if (checkEdit == 'true' && isFirstVisit) {
                isFirstVisit = false;
                window.parent.$('#saveRule').prop('disabled', true); window.parent.$('#saveRule').css("background", "white");
            }
        }
    });
}

function onTaskChangeDropDown() {
    window.parent.$('#saveRule').prop('disabled', false); window.parent.$('#saveRule').css("background", "");
    document.getElementById('select_taskRuleDiv').getElementsByClassName('select2-selection')[0].style.border = '';
}


function ruleNameTextCheck() {
    if ($("#ruleName").val()) {
        document.getElementById('ruleName').style.border = '';
       
    }
    if ($("#ruleName").val() && $("#ruleDesc").val()) {
        window.parent.$("#saveRule").prop('disabled', false);
        window.parent.$('#saveRule').css("background", ""); 
    }
    else {
      //  window.parent.$("#saveRule").prop('disabled', true);
    }
}

function signumTextCheck() {
    if ($("#workOrderViewIdPrefix_assigned_to").val()) {
        document.getElementById('workOrderViewIdPrefix_assigned_to').style.border = '';

    }
   
}


function ruleDescriptioTextCheck() {
    if ($("#ruleDesc").val()) {
        document.getElementById('ruleDesc').style.border = '';

    }
    if ($("#ruleName").val() && $("#ruleDesc").val()) {
        window.parent.$("#saveRule").prop('disabled', false);
        window.parent.$('#saveRule').css("background", ""); 
    }
    else {
      //  window.parent.$("#saveRule").prop('disabled', true);
    }
}



var paper = new joint.dia.Paper({
    //el: document.getElementById('paper'),
    width: 1500,
    height: 700,
    model: graph,
    async: true,
    frozen: true,
    sorting: joint.dia.Paper.sorting.NONE,
    defaultConnector: { name: 'smooth' },
    defaultConnectionPoint: { name: 'boundary' },
    defaultAnchor: { name: 'midSide' },
    elementView: joint.shapes.html.ElementView,
    snapLinks: true,
    linkPinning: false,
    magnetThreshold: 'onleave',
    defaultLink: function () {
        return new joint.shapes.standard.Link({
            z: 1,
            attrs: {
                line: {
                    stroke: '#4666E5',
                    strokeWidth: 3
                }
            }
        });
    },
    validateConnection: function (sourceView, sourceMagnet, targetView, targetMagnet) {
        if (sourceView === targetView) return false;
        return true;
    }
});

var paperScroller = this.paperScroller = new joint.ui.PaperScroller({
    paper: paper,
    autoResizePaper: true,
    contentOptions: {
        allowNewOrigin: 'any'
    }
});

$('#paper').append(paperScroller.render().el);

var NavigatorElementView = joint.dia.ElementView.extend({

    //body: null,

    //markup: [{
    //    tagName: 'rect',
    //    selector: 'body',
    //    attributes: {
    //        'fill': '#31d0c6'
    //    }
    //}],

    //initFlag: ['RENDER', 'UPDATE', 'TRANSFORM'],

    //presentationAttributes: {
    //    size: ['UPDATE'],
    //    position: ['TRANSFORM'],
    //    angle: ['TRANSFORM']
    //},

    //confirmUpdate: function (flags) {

    //    if (this.hasFlag(flags, 'RENDER')) this.render();
    //    if (this.hasFlag(flags, 'UPDATE')) this.update();
    //    if (this.hasFlag(flags, 'TRANSFORM')) this.updateTransformation();
    //},

    //render: function () {
    //    var doc = joint.util.parseDOMJSON(this.markup);
    //    this.body = doc.selectors.body;
    //    this.el.appendChild(doc.fragment);
    //},

    //update: function () {
    //    var size = this.model.size();
    //    this.body.setAttribute('width', size.width);
    //    this.body.setAttribute('height', size.height);
    //}

    //update: function () {
    //    joint.dia.ElementView.prototype.update.apply(this, arguments);
    //    var placeholder1 = this.findBySelector('placeholder')[0];
    //    placeholder1.setAttribute('fill', 'blue');

    //}
});

var NavigatorLinkView = joint.dia.LinkView.extend({

    initialize: joint.util.noop,
    render: joint.util.noop,
    update: joint.util.noop
});

var nav = new joint.ui.Navigator({
    paperScroller: paperScroller,
    width: 200,
    height: 167,
    padding: 10,
    zoomOptions: { max: 2, min: 0.2 },
    paperOptions: {
        async: true
        //elementView: NavigatorElementView,
        //linkView: joint.dia.LinkView,
        //    cellViewNamespace: { /* no other views are accessible in the navigator */ }
    },
    useContentBBox: true

});
nav.$el.appendTo('#navigator');
nav.render();

// Container for all HTML views inside paper
var htmlContainer = document.createElement('div');
htmlContainer.style.pointerEvents = 'none';
htmlContainer.style.position = 'absolute';
htmlContainer.style.inset = '0';
paper.el.appendChild(htmlContainer);
paper.htmlContainer = htmlContainer;

paper.on('scale translate', function () {
    // Update the transformation of all JointJS HTML Elements
    var htmlContainer = this.htmlContainer;
    htmlContainer.style.transformOrigin = '0 0';
    htmlContainer.style.transform = V.matrixToTransformString(this.matrix());
});

//paper.on('blank:pointerdown', paperScroller.startPanning);

paper.on('blank:pointerdown cell:pointerdown', function () {
    document.activeElement.blur();
  
});
paper.on('blank:pointerdown', paperScroller.startPanning);
paper.on('element:add-more', function (elementView) {
    elementView.model.addNotification();
});

paper.on('link:pointerup', function (linkView) {
    this.removeTools();
    var tools = new joint.dia.ToolsView({
        tools: [
            new joint.linkTools.Boundary({ useModelGeometry: true }),
            new joint.linkTools.Remove({
                useModelGeometry: true
            }),
        ]
    });
    linkView.addTools(tools);
    
});
graph.on('remove', function (cell) {
    if (cell.isLink()) {
        window.parent.$('#saveRule').prop('disabled', false);
        window.parent.$('#saveRule').css("background", ""); /* do your logic */
}
});


paper.on('link:connect', function (linkView, event, elementView, port) {
    window.parent.$('#saveRule').prop('disabled', false);
    window.parent.$('#saveRule').css("background", ""); 
    let selectedFieldValue = '' 

    let sourceElement = linkView.model.getSourceCell();
    let targetElement = linkView.model.getTargetCell();

    if (sourceElement.attributes.type == 'html.EmailScan') {
        selectedFieldValue = sourceElement.get('fields').lookForPastEmail;
    }
    else if (sourceElement.attributes.type == 'html.AppScan') {
        selectedFieldValue = [];
        (sourceElement.get('fields').app != -1) ? selectedFieldValue = sourceElement.get('fields').app : selectedFieldValue = [];
        let customApp = sourceElement.get('fields').customApp;
        if (customApp != '' && customApp != undefined) {
            selectedFieldValue.push('customApp');
        }
    }
    else if (sourceElement.attributes.type === "html.FileScan") {//For FileScan
        //selectedFieldValue = sourceElement.get('fields').app;
        selectedFieldValue = [];
        (sourceElement.get('fields').app != -1) ? selectedFieldValue.push(sourceElement.get('fields').app) : selectedFieldValue = [];
        let customPath = sourceElement.get('fields').customPath;
        if (customPath != '' && customPath != undefined) {
            selectedFieldValue.push('customPath');
        }
    }

    //if (targetElement.attributes.type == 'html.EmailMatch') {

    //    emailMatchPopulateAttrs(targetElement, selectedFieldValue, sourceElement);
    //}

    if (selectedFieldValue.length != 0 && targetElement.get('typeName') != 'Condition') {
       
        if (targetElement.attributes.type == 'html.AppMatch') {
            let triggerOnCloseVal = sourceElement.attributes.fields.triggerOnClose;
            appMatchPopulateAttrs(targetElement, selectedFieldValue, sourceElement, triggerOnCloseVal);
        }
        if (targetElement.attributes.type == 'html.FileMatch') {

            fileMatchPopulateAttrs(targetElement, selectedFieldValue, sourceElement);
        }
        if (targetElement.attributes.type == 'html.EmailMatch') {

            emailMatchPopulateAttrs(targetElement, selectedFieldValue, sourceElement);
        }
    }

    else {
        if (sourceElement.get('typeName') == 'Scanner' && targetElement.get('typeName') == 'Condition') {
            let targetOfCondition = graph.getNeighbors(targetElement, { outbound: true })[0];

            //selectedFieldValue = sourceElement.attributes.fields.app;
            //if (sourceElement.attributes.type == 'html.AppCondition') {
            //    selectedFieldValue = [];
            //    (sourceOfCondition.get('fields').app != -1) ? selectedFieldValue = sourceOfCondition.get('fields').app : selectedFieldValue = [];
            //    let customApp = sourceOfCondition.get('fields').customApp;
            //    if (customApp.length != 0) {
            //        selectedFieldValue.push('customApp');
            //    }
            //}
            //else if (sourceElement.attributes.type == 'html.EmailCondition') {
            //    selectedFieldValue = sourceOfCondition.attributes.fields.lookForPastEmail;
            //}

            if (targetOfCondition != undefined && selectedFieldValue != undefined) {

                if (selectedFieldValue != undefined && targetElement.attributes.type == 'html.AppCondition' && targetOfCondition.attributes.type == 'html.AppMatch') {
                    let triggerOnCloseVal = sourceElement.attributes.fields.triggerOnClose;
                    appMatchPopulateAttrs(targetOfCondition, selectedFieldValue, sourceElement, triggerOnCloseVal);
                }
                if (selectedFieldValue != undefined && targetElement.attributes.type == 'html.FileCondition' && targetOfCondition.attributes.type == 'html.FileMatch') {
                    fileMatchPopulateAttrs(targetOfCondition, selectedFieldValue, sourceElement);
                }
                if (selectedFieldValue != undefined && targetElement.attributes.type == 'html.EmailCondition' && targetOfCondition.attributes.type == 'html.EmailMatch') {
                    emailMatchPopulateAttrs(targetOfCondition, selectedFieldValue, sourceElement);
                }
            }
        }
        else if (sourceElement.get('typeName') == 'Condition' && targetElement.get('typeName') == 'Match') {
            let sourceOfCondition = graph.getNeighbors(sourceElement, { inbound: true })[0];

            if (sourceOfCondition != undefined) {
                //selectedFieldValue = sourceOfCondition.get('fields').app;

                if (sourceElement.attributes.type == 'html.AppCondition') {
                    selectedFieldValue = [];
                    (sourceOfCondition.get('fields').app != -1) ? selectedFieldValue = sourceOfCondition.get('fields').app : selectedFieldValue = [];
                    let customApp = sourceOfCondition.get('fields').customApp;
                    if (customApp != '' && customApp != undefined) {
                        selectedFieldValue.push('customApp');
                    }
                }
                else if (sourceElement.attributes.type == 'html.EmailCondition') {
                    selectedFieldValue = sourceOfCondition.attributes.fields.lookForPastEmail;
                }
                else if (sourceElement.attributes.type === "html.FileCondition"){//For FileScan
                    selectedFieldValue = [];
                    (sourceOfCondition.get('fields').app != -1) ? selectedFieldValue.push(sourceOfCondition.get('fields').app) : selectedFieldValue = [];
                    let customPath = sourceOfCondition.get('fields').customPath;
                    if (customPath != '' && customPath != undefined) {
                        selectedFieldValue.push('customPath');
                    }
                }
              //  (sourceElement.attributes.type == 'html.EmailCondition') ? selectedFieldValue = sourceOfCondition.attributes.fields.lookForPastEmail : selectedFieldValue = selectedFieldValue;

                if (selectedFieldValue != undefined && sourceElement.attributes.type == 'html.AppCondition' && targetElement.attributes.type == 'html.AppMatch') {
                    let triggerOnCloseVal = sourceOfCondition.attributes.fields.triggerOnClose;
                    appMatchPopulateAttrs(targetElement, selectedFieldValue, sourceOfCondition, triggerOnCloseVal);
                }
                if (selectedFieldValue != undefined && sourceElement.attributes.type == 'html.FileCondition' && targetElement.attributes.type == 'html.FileMatch') {
                    fileMatchPopulateAttrs(targetElement, selectedFieldValue, sourceOfCondition);
                }
                if (selectedFieldValue != undefined && sourceElement.attributes.type == 'html.EmailCondition' && targetElement.attributes.type == 'html.EmailMatch') {
                    emailMatchPopulateAttrs(targetElement, selectedFieldValue, sourceOfCondition);
                }
            }

        }
      
    }

    if (selectedFieldValue.length == 0 && targetElement.attributes.type == 'html.AppMatch') {      
            let triggerOnCloseVal = sourceElement.attributes.fields.triggerOnClose;
            appMatchPopulateAttrs(targetElement, selectedFieldValue, sourceElement, triggerOnCloseVal);
        
    }
   
    // elementView.model.prop('fields/type0', 'logs');
});

graph.on('change:fields', function (element,fields,opt) {
    //enable save button 
    window.parent.$('#saveRule').prop('disabled', false);
    window.parent.$('#saveRule').css("background", ""); 

    let selectedFieldValue; let path = opt.propertyPathArray[1];
    
    if (element.attributes.type === "html.EmailScan") {
         selectedFieldValue = element.attributes.fields.lookForPastEmail;
    }
    else if (element.attributes.type === "html.AppScan") {
        selectedFieldValue = [];
        (element.attributes.fields.app != -1)?selectedFieldValue = element.attributes.fields.app: selectedFieldValue = [];
        let customApp = element.attributes.fields.customApp;
        if (customApp != '') {
            selectedFieldValue.push('customApp');
        }
    }
    else if (element.attributes.type === "html.FileScan") {//FOR FileSCAN
         //selectedFieldValue = element.attributes.fields.app;
        selectedFieldValue = [];
        (element.attributes.fields.app != -1) ? selectedFieldValue.push(element.attributes.fields.app) : selectedFieldValue = [];
        let customPath = element.attributes.fields.customPath;
        if (customPath != '' && customPath != undefined) {
            selectedFieldValue.push('customPath');
        }

    }
    if (element.attributes.type === "html.FileScan" && selectedFieldValue != undefined) {
        disableEnableInputPathForFSScan(element, opt, selectedFieldValue[0]);
        //element.attributes.htmlMarkup[3].children[0].attributes;
    }
    
    let neighbors = graph.getNeighbors(element, { outbound: true });
    let scannerConditionInNeighbors = '';
    scannerConditionInNeighbors = neighbors.filter(function (el) { return (el.attributes.type !== 'html.ConnectorCondition' && el.attributes.typeName === 'Condition') })

    if (neighbors.length != 0 && scannerConditionInNeighbors.length != 0 && scannerConditionInNeighbors[0].get('typeName') == "Condition") {
        let targetsOfAppCondition = graph.getNeighbors(scannerConditionInNeighbors[0], { outbound: true });
        neighbors = targetsOfAppCondition;
    }

    neighbors.forEach(function (neighbor, e) {
        let targetElement = neighbor.attributes.type;

        if (targetElement === 'html.AppMatch') {
            
            if (path == 'app' || path === 'customApp') {
                appMatchPopulateAttrs(neighbor, selectedFieldValue, element, fields.triggerOnClose);
            } 
            else if (path == 'triggerOnClose') { // If user clicks on Trigger on close.
                appMatchPopulateAttrs(neighbor, selectedFieldValue, element, fields.triggerOnClose);
            }         
        }
        else if (targetElement === 'html.FileMatch' && selectedFieldValue != undefined) {

            fileMatchPopulateAttrs(neighbor, selectedFieldValue, element);
        }
        else if (targetElement === 'html.EmailMatch' && selectedFieldValue != undefined) {
            //let trigger = element.
            emailMatchPopulateAttrs(neighbor, selectedFieldValue, element);
          
        }
        
    });

    let type = element.attributes.type;
    if (type == 'html.AppMatch') {
        //const prevFields = element.previous('fields');
        //const newFields = element.get('fields');
        if (path === 'matchattr') {
            selectedFieldValue = element.attributes.fields.matchattr;
            if (selectedFieldValue != '-1') {
                appMatchOperatorGenerator(element, opt, fields, selectedFieldValue);
            }
        }
        if (path == 'matchType') {
            changeHTMLMarkupForMatchingType(element, opt, fields);
        }
    }

    else if (type == 'html.FileMatch') {
        //const prevFields = element.previous('fields');
        //const newFields = element.get('fields');
        if (path === 'matchattr') {
            selectedFieldValue = element.attributes.fields.matchattr;
            if (selectedFieldValue != '-1') {
                fileMatchOperatorGenerator(element, opt, fields, selectedFieldValue);
            }
        }
        if (path == 'matchType') {
            changeHTMLMarkupForMatchingType(element, opt, fields);
        }
    }

    else if (type == 'html.EmailMatch') {
        //const prevFields = element.previous('fields');
        //const newFields = element.get('fields');
        

        if (path === 'matchattr') {
            selectedFieldValue = element.attributes.fields.matchattr;
            if (selectedFieldValue != '-1') {
                emailMatchOperatorGenerator(element, opt, fields, selectedFieldValue);
            }
        }
        if (path == 'matchType') {

            changeHTMLMarkupForMatchingType(element, opt, fields);
        }
    }
    if (type == "html.EmailScan") {

        if (path === 'lookForPastEmail') {

            createHTMLForEmailScanner(element, opt, selectedFieldValue);
        }
      
    }
    if (type == 'html.AppCondition' || type == 'html.ConnectorCondition' || type == 'html.EmailCondition' || type == 'html.FileCondition' ) {

        if (path === 'mode') {
            //var switchStatus = false;
            $(".inputCheck").on('change', function () {
                if ($(this).is(':checked')) {
                   let switchStatus = $(this).is(':checked');
                    //alert(switchStatus);// To verify
                    element.attributes.fields.mode = switchStatus;
                }
                else {
                    let switchStatus = $(this).is(':checked');
                    element.attributes.fields.mode = switchStatus;
                    //alert(switchStatus);// To verify
                }
            });
        }
    }

});


paper.on('element:pointerup', function (elementView) {
    this.removeTools();
    var tools = new joint.dia.ToolsView({
        tools: [
            new joint.elementTools.Boundary({ useModelGeometry: true }),
            new joint.elementTools.Remove({
                useModelGeometry: true,
                y: '0%',
                x: '100%',
                offset: { x: 10, y: -10 }
            }),
        ]
    });
    elementView.addTools(tools);
});

paper.on('blank:pointerdown', function () {
    paper.removeTools();
});

//paper.el.addEventListener('blank:mousewheel')


paper.el.addEventListener('drop', function (evt, element) {
   
    //enable save button 
    //window.parent.$('#downloadJsonIcon').css({ "pointer-events":"auto" , "color": ""}); 
    window.parent.$('#saveRule').prop('disabled', false);
    window.parent.$('#saveRule').css("background", ""); 
    var elementJSON = {
        type: evt.dataTransfer.getData('type'),
        position: paper.clientToLocalPoint(evt.clientX - 125, evt.clientY - 50).toJSON()
    };
    if (!isZoomed) {
        paperScroller.zoom(-0.2, { min: 0.2 });
        isZoomed = true;
    }
    graph.addCell(elementJSON);
    let scannerConnectors = graph.getCells().filter(function (el) { return el.attributes.type == 'html.ConnectorCondition' });
    scannerConnectors.forEach(function (scannerConnector, e) {
        scannerConnector.attributes.attrs.placeholder.fill = '#BDE7F3';
    });

    let scannerNotification = graph.getCells().filter(function (el) { return el.attributes.type == 'html.ConnectorNotification' });
    scannerNotification.forEach(function (scannerNotification, e) {
        scannerNotification.attributes.fields.type0 = 'restAPI';
    });

    //let condition = graph.getCells().filter(function (el) { return el.attributes.typeName == 'Condition' });
    //condition.forEach(function (condition, e) {
    //    condition.attributes.fields.mode = true;
    //});

    //let appScanner = graph.getCells().filter(function (el) { return el.attributes.type == 'html.AppScan' });
    //appScanner.forEach(function (appScanner, e) {
    //        if (appScanner.attributes.fields.app == undefined || appScanner.attributes.fields.app == '') {
    //            appScanner.attributes.fields.app = '-1';
    //        }
    //});

    //let fsScanner = graph.getCells().filter(function (el) { return el.attributes.type == 'html.FileScan' });
    //fsScanner.forEach(function (fsScanner, e) {
    //    if (fsScanner.attributes.fields.app == undefined || fsScanner.attributes.fields.app == '') {
    //        fsScanner.attributes.fields.app = '-1';
    //    }
    //});
   
   
}, false);  

// necessary for the drop event to trigger
paper.el.addEventListener('dragover', function (evt) {
    evt.preventDefault();
}, false);
paper.unfreeze();

// Toolbar
//var zoomLevel = 1;

document.getElementById('zoom-in').addEventListener('click', function () {
    //zoomLevel = Math.min(3, zoomLevel + 0.2);
    //var size = paper.getComputedSize();
    //paper.translate(0,0);
    //paper.scale(zoomLevel, zoomLevel, size.width / 2, size.height / 2);
    paperScroller.zoom(0.2, { max: 3 });
});

document.getElementById('zoom-out').addEventListener('click', function () {
    //zoomLevel = Math.max(0.2, zoomLevel - 0.2);
    //var size = paper.getComputedSize();
    //paper.translate(0,0);
    //paper.scale(zoomLevel, zoomLevel, size.width / 2, size.height / 2);
    paperScroller.zoom(-0.2, { min: 0.2 });
});

//document.getElementById('reset').addEventListener('click', function () {
//    graph.getElements().forEach(function (element) {
//        element.resetFields();
//    });
//});

document.getElementById('zoomToFit').addEventListener('click', function () {    
    paperScroller.zoomToFit({
        minScale: 0.2,
        maxScale: 2
    });
});



function downloadObjectAsJson() {
    var ruleID;
    var ruleName;
    let $ruleDataParentObj = window.parent.$('#autosenseRuleData');
    let isMigrate = $ruleDataParentObj.attr("data-isMigrate");
    if ($('#ruleID').val() != null && $('#ruleID').val() != undefined && $('#ruleID').val() != "") {
        ruleID = $("#ruleID").val();

    }
    else {

        ruleID = window.parent.$('#autosenseRuleData').attr('data-ruleID');
        //ruleName = window.parent.$('#autosenseRuleData').attr('data-ruleName');
    }
    ruleName = $("#ruleName").val();
    if (isMigrate == "true" || isMigrate == true) {

        pwIsf.addLayer({ text: "Please wait" });
        $.isf.ajax({
            type: "GET",
            async: false,
            url: service_java_URL + "autoSense/getRuleJsonByMigrationID?ruleMigrationID=" + ruleID,
            crossdomain: true,
            processData: true,
            contentType: 'application/json',
            success: function (data) {


                if (data.isValidationFailed == true) {
                    pwIsf.alert({ msg: "Error in downloading file.", type: "error", autoClose: 2 });
                }
                else if (data.isValidationFailed == false) {
                    let responseJson = data.responseData.ruleJsonForValidation;
                    var dataStrRule = "data:text/json;charset=utf-8," + encodeURIComponent(responseJson);
                    var ruleJsonElementdl = document.getElementById("downloadAnchorElem");
                    ruleJsonElementdl.setAttribute("href", dataStrRule);
                    ruleJsonElementdl.setAttribute("download", "RuleJson_" + ruleName + ".txt");
                    ruleJsonElementdl.click();
                }

            },
            error: function (status) {
                pwIsf.alert({ msg: "Error in API.", type: "error", autoClose: 2 });
            },
            complete: function () {
                pwIsf.removeLayer();

            }
        })


    }
    else {
        
        pwIsf.addLayer({ text: "Please wait" });
        $.isf.ajax({
            type: "GET",
            async: false,
            url: service_java_URL + "autoSense/getRuleJsonByID?ruleID=" + ruleID,
            crossdomain: true,
            processData: true,
            contentType: 'application/json',
            success: function (data) {


                if (data.isValidationFailed == true) {
                    pwIsf.alert({ msg: "Error in downloading file.", type: "error", autoClose: 2 });
                }
                else if (data.isValidationFailed == false) {
                    let responseJson = data.responseData[0].jsonManualValidation;
                    var dataStrRule = "data:text/json;charset=utf-8," + encodeURIComponent(responseJson);
                    var ruleJsonElementdl = document.getElementById("downloadAnchorElem");
                    ruleJsonElementdl.setAttribute("href", dataStrRule);
                    ruleJsonElementdl.setAttribute("download", "RuleJson_" + ruleName + ".txt");
                    ruleJsonElementdl.click();
                }

            },
            error: function (status) {
                pwIsf.alert({ msg: "Error in API.", type: "error", autoClose: 2 });
            },
            complete: function () {
                pwIsf.removeLayer();

            }
        })
    }
    //let exportObj = graph.toJSON();
    //var dataStr = "data:text/json;charset=utf-8," + encodeURIComponent(JSON.stringify(exportObj));
    //var dlAnchorElem = document.getElementById('downloadAnchorElem');
    //dlAnchorElem.setAttribute("href", dataStr);
    //dlAnchorElem.setAttribute("download", "Rule.txt");
    //dlAnchorElem.click();
}

//To get Json of the Rule while plotting the graph
function editAndCopyRule() {
    let ruleID = window.parent.$('#autosenseRuleData').attr('data-ruleID');
    
    $.isf.ajax({
        type: "GET",
        //async: false,
        url: service_java_URL + "autoSense/getRuleJsonByID?ruleID=" + ruleID,
        crossdomain: true,
        processData: true,
        contentType: 'application/json',
        success: function (data) {
            let ruleJson = data.responseData[0].ruleJson;
            graph.fromJSON(JSON.parse(ruleJson));
            isZoomed = true;
            paperScroller.zoom(-0.2, { min: 0.2 });
            if (data.isValidationFailed == true) {
                pwIsf.alert({ msg: "Error in API", type: "error" });
            }

        },
        error: function (status) {
            pwIsf.alert({ msg: "Error in API", type: "error" });
        },
        complete: function () {

        }
    });
}
//api call in rule migration for plotting graph
function plotGraphRuleMgn() {
   
    let ruleID = window.parent.$('#autosenseRuleData').attr('data-ruleID');
    if (ruleID) {
        $.isf.ajax({
            type: "GET",
            //async: false,
            url: service_java_URL + "autoSense/getRuleJsonByMigrationID?ruleMigrationID=" + ruleID,
            crossdomain: true,
            processData: true,
            contentType: 'application/json',
            success: function (data) {
                let ruleJson = data.responseData.newRuleJson;
                graph.fromJSON(JSON.parse(ruleJson));
                paperScroller.zoom(-0.2, { min: 0.2 });
                isZoomed = true;
                if (data.isValidationFailed == true) {
                    pwIsf.alert({ msg: "Error in API", type: "error" });
                }

            },
            error: function (status) {
                pwIsf.removeLayer();
                pwIsf.alert({ msg: "Error in API", type: "error" });
            },

            complete: function () {

            }
        });
    }

}
//})(joint, V);
function getAllSignumForEditWODetails() {
    $("#workOrderViewIdPrefix_assigned_to").autocomplete({
       
        
        source: function (request, response) {
            $.isf.ajax({
                url: service_java_URL + "activityMaster/getEmployeesByFilter",
                type: "POST",
              //  async:"false",
                data: {
                    term: request.term
                },
                success: function (data) {
                    $("#workOrderViewIdPrefix_assigned_to").autocomplete().addClass("ui-autocomplete-loading");
                    window.parent.$('#saveRule').prop('disabled', false);
                    window.parent.$('#saveRule').css("background", ""); 
                    var result = [];
                    if (data.length == 0) {
                        pwIsf.alert({ msg: 'Please enter a valid Signum', type: 'warning' });
                        $('#workOrderViewIdPrefix_assigned_to').val("");
                        $('#workOrderViewIdPrefix_assigned_to').focus();
                        window.parent.$('#saveRule').prop('disabled', true); window.parent.$('#saveRule').css("background", "white"); 
                        response(result);
                    }
                    else {
                        $.each(data, function (i, d) {
                            result.push({
                                "label": d.signum + "/" + d.employeeName,
                                "value": d.signum
                            });
                        })
                        response(result);
                        $("#workOrderViewIdPrefix_assigned_to").autocomplete().removeClass("ui-autocomplete-loading");
                    }

                }
            });
        },
        change: function (event, ui) {
            if (ui.item === null) {
                $(this).val('');
                $('#workOrderViewIdPrefix_assigned_to').val('');
            }
        },
        minLength: 3

    });
    $("#workOrderViewIdPrefix_assigned_to").autocomplete("widget").addClass("fixedHeight");
}