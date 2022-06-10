var ServiceUrl= "http://10.184.49.19:8080/NEDx/webresources/nedx/getWOFlowChartDetails";
var DependencyService = "http://10.184.49.19:8080/NEDx/webresources/nedx/getWOFlowChartDepDetails";

function getUrlVars()
{
    var vars = [], hash;
	var hashes = window.location.href.slice(window.location.href.indexOf('?') + 1).split('&');

	for (const hashItem of hashes) {
		hash = hashItem.split('=');
		vars.push(hash[0]);
		vars[hash[0]] = hash[1];
	}
    return vars;
}

var flowDataModal = {
	"class": "go.GraphLinksModel",
  "linkFromPortIdProperty": "fromPort",
  "linkToPortIdProperty": "toPort",
  "nodeDataArray": [],
  "linkDataArray": [],
}

var startLeft = 0;
var startTop = 0;
var key = -1;

var addedNodes = [];

function zoomIn()
{
	if (scale < 2.0)
	{
		scale += .2;
		makeSVG();
	}	
}

function zoomOut()
{
	if (scale > .2)
	{
		scale -= .2;
		makeSVG();
	}
}

function mopURLUpdate(projectid)
{
	if (projectid.toString() === '1262')
	{
		$('#mopLink').attr("href", "http://10.184.49.19:8081/Documents/MOP/MoP_Interface_Utilization_Analysis.pdf");
	}
	else
	{
		$('#mopLink').attr("href", "http://10.184.49.19:8081/Documents/MOP/SCFT_RPA_flow.pdf");
	}
}

function getworkflowData(myDiagram, go)
{
	var serviceCall = $.isf.ajax({
        url: ServiceUrl + "?userQueueID=" + getUrlVars()["userQueueID"].toString() + "&sid=" + getUrlVars()["sid"].toString(),
        context: this,
        crossdomain: true,
        dataType: 'json',
        contentType: 'application/json',
        type: 'GET',
        xhrFields: {
            withCredentials: false
        },
        success: function (data){
			getworkflowDependencyData(data, myDiagram, go);			
        },
        error: function (xhr, status, statusText) {
            console.log('An error occurred');
			alert('Sorry, an error occurred. Please try reloading the page.'
				+ ' \n\nIf that doesn\'t work please send us the following error message, using the contact us page. \n\n'
				+ this.url + ' ' + xhr.status + ' ' + xhr.statusText);
        }
    })
    serviceCall.done(function () {
        //Empty Block
    });
}

function getworkflowDependencyData(workFlowData, myDiagram, go)
{
	var serviceCall = $.isf.ajax({
        url: DependencyService + "?userQueueID=" + getUrlVars()["userQueueID"].toString() + "&sid=" + getUrlVars()["sid"].toString(),
        context: this,
        crossdomain: true,
        dataType: 'json',
        contentType: 'application/json',
        type: 'GET',
        xhrFields: {
            withCredentials: false
        },
        success: function (data){
			createWorkflow(data ,workFlowData, myDiagram, go);        
        },
        error: function (xhr, status, statusText) {
            console.log('An error occurred');
			alert('Sorry, an error occurred. Please try reloading the page.'
				+ ' \n\nIf that doesn\'t work please send us the following error message, using the contact us page. \n\n'
				+ this.url + ' ' + xhr.status + ' ' + xhr.statusText);
        }
    })
    serviceCall.done(function () {
        //Empty Block
    });
}

function createWorkflow(dependencyData, workFlowData, myDiagram, go)
{
	mopURLUpdate(getUrlVars()["projectid"].toString());
	var lastNode = '';
	var startObj = {};
	
	startObj.key = key;
	startObj.category = "Start";
	startObj.loc = startLeft.toString() + " " + startTop.toString();
	startObj.text = "Start";
	
	var startLinkObj = {};
	
	startLinkObj.from = key;
	startLinkObj.to = workFlowData[0].stepID.toString();
	startLinkObj.fromPort = "R";
	startLinkObj.toPort = "L";	
	
	flowDataModal.nodeDataArray.push(startObj);
	flowDataModal.linkDataArray.push(startLinkObj);
	startLeft = startLeft + 200;
	
	for (const obj in workFlowData)
	{	
		var innerData = {};
		
		var step = workFlowData[obj].stepID;
		var text = workFlowData[obj].displayStepNameWithDetails.split("\\n")[0];
		var booking = workFlowData[obj].displayStepNameWithDetails.split("\\n")[1];
		var taskname = workFlowData[obj].displayStepNameWithDetails.split("\\n")[2];
		var avgExecTime = workFlowData[obj].displayStepNameWithDetails.split("\\n")[3];
		var startTime = workFlowData[obj].displayStepNameWithDetails.split("\\n")[4];
		var tasktype = workFlowData[obj].type.toString();
		
		innerData.key = step;
		innerData.loc = startLeft.toString() + " " + startTop.toString();
		innerData.text = text;
		innerData.booking = booking;
		innerData.taskname = taskname;
		innerData.tasktype = tasktype;
		innerData.avgExecTime = avgExecTime;
		innerData.startTime = startTime;
		innerData.type = workFlowData[obj].result;
				
		for (const dObj in dependencyData)
		{
			if (dependencyData[dObj].dependentStepID == workFlowData[obj].stepID.toString())
			{
				var innerLinkObj = {};
		
				innerLinkObj.from = dependencyData[dObj].dependentStepID;
				innerLinkObj.to = dependencyData[dObj].stepID;
				innerLinkObj.fromPort = "R";
				innerLinkObj.toPort = "L";
				flowDataModal.linkDataArray.push(innerLinkObj);
			}
		}
		flowDataModal.nodeDataArray.push(innerData);
		
		startLeft = startLeft + 250;
		
		lastNode = workFlowData[obj].stepID;
	}

	startLeft = startLeft - 50;
	
	var endObj = {};
	
	endObj.key = -2;
	endObj.category = "End";
	endObj.loc = startLeft.toString() + " " + startTop.toString();
	endObj.text = "End";
	
	var endLinkObj = {};
	
	endLinkObj.from = lastNode;
	endLinkObj.to = -2;
	endLinkObj.fromPort = "R";
	endLinkObj.toPort = "L";
	
	flowDataModal.nodeDataArray.push(endObj);
	flowDataModal.linkDataArray.push(endLinkObj);
	
	for (const nodeObj in flowDataModal.nodeDataArray)
	{
		var parent = flowDataModal.nodeDataArray[nodeObj].key.toString();
		var count = 0;
		var child = [];
		for (const nodeLinkObj2 in flowDataModal.linkDataArray)
		{
			if (parent == flowDataModal.linkDataArray[nodeLinkObj2].from)
			{
				count++;
				child.push(flowDataModal.linkDataArray[nodeLinkObj2].to.toString());
			}
		}
		
		if (count > 1)
		{
			for (const c in child)
			{
				for (const node in flowDataModal.nodeDataArray)
				{
					if (c != 0)
					{
						var nodeLeft;
						var nodeTop;
						var nodeNewLeft;
						var nodeNewTop;

						if (child[c].toString() === flowDataModal.nodeDataArray[node].key.toString())
						{
							nodeLeft = flowDataModal.nodeDataArray[node].loc.toString().split(" ")[0].toString();
							nodeTop = flowDataModal.nodeDataArray[node].loc.toString().split(" ")[1].toString();
							
							nodeNewLeft = parseInt(nodeLeft) - 150;
							nodeNewTop = parseInt(nodeTop) + 200;
							
							flowDataModal.nodeDataArray[node].loc = nodeNewLeft.toString() + " " + nodeNewTop.toString();
						}
						else
						{
							nodeLeft = flowDataModal.nodeDataArray[node].loc.toString().split(" ")[0].toString();
							nodeTop = flowDataModal.nodeDataArray[node].loc.toString().split(" ")[1].toString();
							
							nodeNewLeft = parseInt(nodeLeft) + 100;
							nodeNewTop = parseInt(nodeTop);
							
							flowDataModal.nodeDataArray[node].loc = nodeNewLeft.toString() + " " + nodeNewTop.toString();
						}
					}
				}				
			}
		}
		
		
	}
	
	
	
	myDiagram.model = go.Model.fromJson(JSON.stringify(flowDataModal, null, 2));
	
	makeSVG();
	
	$("#myDiagramDiv").hide();
	$("#mySavedModel").hide();
	
}


