isVersionChanged = true;
urlParams = new URLSearchParams(window.location.search);

joint.setTheme('material');
app = new App.MainView({ el: '#app' });
themePicker = new App.ThemePicker({ mainView: app });
themePicker.render().$el.appendTo(document.body);
$("#btn-edit").css("border", "2px solid");

let getDiv = window.parent.$('#togBtnForFlowChartViewMode_' + urlParams.get("woID")).siblings().filter('div #divTogBtnForFlowChartViewMode');

let IsAccessed = getDiv != null && getDiv != undefined && getDiv[0].innerText === "ASSESSED" ? true : false;

//let getIsExperiencedChecked = (urlParams.get('isExperiencedChecked')) ? urlParams.get('isExperiencedChecked') : '0';
//if (getIsExperiencedChecked == '1') { $("#btn-edit").hide(); } else { $("#btn-edit").show(); }

IsAccessed == true ? $("#btn-edit").show() : $("#btn-edit").hide(); 

let flowChartType = (urlParams.get('flowchartType')) ? urlParams.get('flowchartType') : '';

versionNo = urlParams.get("version");
var contentDiv = '';
var currentTaskID, currentTaskName, currentShape, executionType, urlpath;
var bookingID = '', bookingStatus = '', rpaID = '';
var subActivityFlowChartDefID = '', stepID = '';
var arrBookingId = [], max;
var arrLink = app.graph.toJSON().cells.filter(function (el) { return el.type == 'app.Link' });
var arrAllShapes = app.graph.toJSON().cells.filter(function (el) { return el.type == 'ericsson.Manual' || el.type == 'ericsson.Automatic' || el.type == 'ericsson.Decision' || el.type == 'ericsson.StartStep' || el.type == 'ericsson.EndStep' });
var arrShapes = app.graph.toJSON().cells.filter(function (el) { return el.type == 'ericsson.Manual' || el.type == 'ericsson.Automatic' });
var myInterval = null;
var max = arrShapes.reduce(function (prev, current) {

   return (prev.attrs.task.bookingID > current.attrs.task.bookingID) ? prev : current
}) //returns object

subActivityFlowChartDefID = $("#defID").val();
bookingID = max.attrs.task.bookingID;
executionType = max.attrs.task.executionType;
currentShape = max.type;
currentTaskID = max.attrs.task.taskID;
stepID = max.id;

currentTaskName = max.attrs.task.taskName;
bookingStatus = max.attrs.task.status;
if (executionType == "Automatic") {
    rpaID = max.attrs.tool.RPAID;
}
else {
    rpaID = '';
}

//Check if next step can be executed.
nextStepFlag = false;
nextStepDetails(stepID);
if ((executionType == "Automatic") && (bookingStatus == "COMPLETED")) {
    urlpath = max.attrs.task.outputLink;
    if ((urlpath != null) || (urlpath != undefined))
        urlpath = urlpath.split("=")[1];
}

//make Icons in Elements
makeIconInElements(app.graph.toJSON(), max, flowChartType);

handleWOActionButtons(urlParams.get("woID"), true);

app.paper.on('cell:pointerclick', function (cellView, evt, x, y) {
    let stepName = '';
    currentTaskID = '', currentTaskName = '', currentShape = '';
    bookingID = '', bookingStatus = '', stepID = '';
    executionType = null; urlpath = '';
    rpaID = '', activeCell = '';
    currentShape = cellView.model.attributes.type;
    if (currentShape == 'ericsson.Decision') {
        stepName = cellView.model.attr('label/text').split('Selected')[0].trim();
        filterCommentsByStep(stepName); // Call for filter comment by step name
    }
    else if (currentShape == 'ericsson.Manual' || currentShape == 'ericsson.Automatic') {
        stepName = cellView.model.attributes.name;
        filterCommentsByStep(stepName); // Call for filter comment by step name
    }
    stepID = cellView.model.attributes.id;
    activeCell = cellView.model;
    if (cellView.model.attributes.attrs.task != undefined) {
        currentTaskID = cellView.model.attributes.attrs.task.taskID;
        currentTaskName = cellView.model.attributes.attrs.task.taskName;
        bookingID = cellView.model.attributes.attrs.task.bookingID;
        bookingStatus = cellView.model.attributes.attrs.task.status;
        executionType = cellView.model.attributes.attrs.task.executionType;
//>>>>>>> .theirs
        if (executionType == "Automatic") {
            rpaID = cellView.model.attributes.attrs.tool.RPAID;
        }
        else {
            rpaID = '';
        }
        if ((executionType == "Automatic") && (bookingStatus == "COMPLETED")) {
            urlpath = cellView.model.attributes.attrs.task.outputLink;
            if ((urlpath != null) || (urlpath != undefined))
                urlpath = urlpath.split("=")[1];
        }
    }
});

$(document).ready(function () {
    $('#botConfigForm').on('submit', function (e) {
        e.preventDefault();
      
        saveBotConfig();  
    });

    $('body').tooltip({
        selector: '.aqua-tooltip',
        placement: function (tip, element) { //$this is implicit                    
            //if (getTextWidth(element.dataset.originalTitle) > 1250) {
            //    return "bottom"
            //}
            //else {
            //    return "top"
            //}
            return "top"
        }
    });
});

(function () {
    var fs = (document.location.protocol === 'file:');
    var ff = (navigator.userAgent.toLowerCase().indexOf('firefox') !== -1);
    if (fs && !ff) {
        (new joint.ui.Dialog({
            width: 300,
            type: 'alert',
            title: 'Local File',
            content: $('#message-fs').show()
        })).open();
    }
})();

