$(document).ready(function () {
	$("#testBtnNewRequest").hide(); //Test Bot not needed as of now
	getMarketAreas();
	fillDomainFilter();

	$('#requestInfo').on('show.bs.modal', function () {
		$(this).find('.modal-content').css({
			width: 'auto',
			height: 'auto',
			'max-width': '50%',
			'margin': 'auto'
		});
	});
	$.protip();

	//Search Bots through text on boxes
	$('#searchBots').keyup(function () {

		var searchObj = {}; callBacksObj = {};
		var that = this;

		var makeText = $(that).val().toLowerCase();
		searchObj.textToBeSearch = makeText;
		searchObj.searchInTarget = '.inprogressTask_container > .row > .searchClass';
		callBacksObj.callBack = false;
		callBacksObj.mainContainer = '.inprogressTask_container';
		callBacksObj.callBackFunction = '';

		searchInDiv(searchObj, callBacksObj);
	});
})


//2VN Populate Market area drop down 
function getMarketAreas() {
	$.isf.ajax({

		url: service_java_URL + "projectManagement/getMarketAreas",
		success: function (data) {

			$.each(data, function (i, d) {

				$('#marketFilter').append('<option value="' + d.MarketAreaName + '">' + d.MarketAreaName + '</option>');
			})
		},
		error: function (xhr, status, statusText) {
			console.log("Fail " + xhr.responseText);

			console.log('An error occurred');
		}
	});

}

//2VN On change of Market Areas, get all bots
function onChangeMarketAreas() {
	let marketArea = $("#marketFilter").val();

	fillDomainFilter(marketArea);
}

//2VN populate activity drop down.
function getActivities(marketarea, domain) {

	$("#activityFilter").empty();
	$("#techFilter").empty();
	$("#taskFilter").empty();
	$("#activityFilter").append("<option value=''>Select activity</option>");

	pwIsf.addLayer({ text: "Please wait ..." });

	$.isf.ajax({
		url: service_java_URL + "rpaController/getRPASubactivity?domainId=" + domain + "&marketarea=" + marketarea,
		success: function (data) {
			data = data.responseData.data;
			$.each(data, function (i, d) {
				$('#activityFilter').append('<option value="' + d.SubActivityID + '">' + d.SubActivity + '</option>');
			})
		},
		error: function (xhr, status, statusText) {
			console.log("Fail " + xhr.responseText);
		},
		complete: function (xhr, statusText) {
			pwIsf.removeLayer();

		}
	});

}

function onChangeActivity() {
	let marketArea = $("#marketFilter").val();
	let domain = $("#domainFilter").val();
	let activity = $("#activityFilter").val();

	fillTechFilter(marketArea, domain, activity);
}

//2VN populate Task drop down.
function getTasks() {

	let marketArea = $("#marketFilter").val();
	let domain = $("#domainFilter").val();
	let activity = $("#activityFilter").val();
	let tech = $("#techFilter").val();

	$("#taskFilter").empty();
	$("#taskFilter").append("<option value=''>Select Task</option>");

	pwIsf.addLayer({ text: "Please wait ..." });

	$.isf.ajax({
		url: service_java_URL + "rpaController/getRPATask?domainId=" + domain + "&subActivityId=" + activity + "&market=" + marketArea + "&technologyId=" + tech,
		success: function (data) {
			data = data.responseData.data;
			$.each(data, function (i, d) {
				$('#taskFilter').append('<option value="' + d.TaskID + '">' + d.Task + '</option>');
			})
		},
		error: function (xhr, status, statusText) {
			console.log("Fail " + xhr.responseText);
		},
		complete: function (xhr, statusText) {
			pwIsf.removeLayer();
		}
	});
}




//Populate Domain dropdown
function fillDomainFilter(marketArea) {
	$("#domainFilter").empty();
	$("#activityFilter").empty();
	$("#techFilter").empty();
	$("#taskFilter").empty();
	$("#domainFilter").append("<option value=''>Select Domain</option>");
	if (marketArea === null || marketArea === undefined) {

		marketArea = '';
	}
	pwIsf.addLayer({ text: "Please wait ..." });


	$.isf.ajax({
		url: service_java_URL + "rpaController/getRPADomain?marketarea=" + marketArea,
		success: function (data) {
			pwIsf.removeLayer();

			data = data.responseData.data;
			$.each(data, function (i, d) {
				$("#domainFilter").append("<option value='" + d.DomainID + "'>" + d.Domain + "</option>")
			})
		},
		error: function (xhr, status, statusText) {
			pwIsf.removeLayer();
			console.log('An error occurred');
			console.log("Fail " + xhr.responseText);
		}
	})

}

//On change of Domain, fill Technology dropdown and get all bots
function onChangeDomain() {
	let marketArea = $("#marketFilter").val();
	let domain = $("#domainFilter").val();
	getActivities(marketArea, domain);

}

function callGlobalSearch(searchBotValue) {

	let searchObj = {}; let callBacksObj = {};
	var makeText = searchBotValue.toLowerCase();
	searchObj.textToBeSearch = makeText;
	searchObj.searchInTarget = '.inprogressTask_container > .row > .searchClass';
	callBacksObj.callBack = false;
	callBacksObj.mainContainer = '.inprogressTask_container';
	callBacksObj.callBackFunction = '';

	searchInDiv(searchObj, callBacksObj);
}

//Fill Technology dropdown
function fillTechFilter(marketArea, domain, activity) {

	$("#techFilter").empty();
	$("#taskFilter").empty();
	$("#techFilter").append("<option value=''>Select Technology</option>");

	if (domain == null)
		domain = "";
	pwIsf.addLayer({ text: "Please wait ..." });

	$.isf.ajax({
		url: service_java_URL + "rpaController/getRPATechnology?domainId=" + domain + "&market=" + marketArea + "&subActivityId=" + activity,
		success: function (data) {
			pwIsf.removeLayer();
			data = data.responseData.data;
			$.each(data, function (i, d) {
				$("#techFilter").append("<option value='" + d.TechnologyID + "'>" + d.Technology + "</option>")
			})
		},
		error: function (xhr, status, statusText) {
			pwIsf.removeLayer();
			console.log('An error occurred');
		}
	})
}

//Not using this function currently
function protipAssign(bot) {
	let rowCount = 0;
	let i = 0;
	let length = (bot.length > 12) ? 12 : bot.length;
	for (i = 0; i < length; i++) {

		if (rowCount > 4) {
			rowCount = 0;
		}
		var el = $('.downloadBotFiles_' + bot[i].botid);
		el.protipSet({
			title: `<a title="Input" href="#" class="icon-plan lsp"><i class="fa fa-download"></i></a>
				'&emsp;<a title="Output" href="#" class="icon-add lsp"><i class="fa fa-download"></i></a>
				'&emsp;<a title="Code" href="#" class="icon-view lsp"><i class="fa fa-download"></i></a>
				'&emsp;<a title="Logic" href="#" class="icon-close lsp"><i class="fa fa-download"></i></a>`
		});
		rowCount++;
	};

}

//To view all bots with the filters
function viewBots(bot) {
	let botRowHtml = '<div class="row">';
	let i = 0;
	let length = bot.length;
	for (i = 0; i < length; i++) {
		botRowHtml += '<div class="col-md-3 searchClass" style="margin-bottom: 20px;">' +
			createBotBox(bot[i]) +
			'</div>';
	};
	botRowHtml += '</div>'
	$(".inprogressTask_container").append(botRowHtml);
}

//Get data of all bots with the filters
function getAllBots() {

	pwIsf.addLayer({ text: "Please wait ..." });

	$(".inprogressTask_container").html("");

	let domain = $("#domainFilter").val();
	let tech = $("#techFilter").val();
	let marketArea = $("#marketFilter").val();
	let taskId = $("#taskFilter").val();
	let subactivityId = $("#activityFilter").val();
	let serviceUrl = "";

	if (domain == null)
		domain = "";
	if (tech == null)
		tech = "";
	if (marketArea == null)
		marketArea = "";
	if (taskId == null)
		taskId = ""
	if (subactivityId == null)
		subactivityId = "";
	if (domain === null || domain === "" || domain === undefined) {
		pwIsf.alert({ msg: "Domain/SubDomain is mandatory for Bot Search", type: 'error' });
	}
	else {
		//2VN call with market area
		serviceUrl = service_java_URL + "rpaController/getRPABOTDetails?domainid=" + encodeURIComponent(domain) + "&technologyId=" + encodeURIComponent(tech) + "&taskId=" + encodeURIComponent(taskId) + "&marketarea=" + encodeURIComponent(marketArea) + "&subactivityId=" + encodeURIComponent(subactivityId);

		if (ApiProxy == true) {
			//2VN call with market area
			serviceUrl = service_java_URL + "rpaController/getRPABOTDetails?" + encodeURIComponent("domain=" + domain + "&technology=" + tech + "&taskId=" + taskId + "&marketarea=" + marketArea + "&subactivityId=" + subactivityId);
		}



		$.isf.ajax({
			url: serviceUrl,
			context: this,
			crossdomain: true,
			processData: true,
			contentType: 'application/json',
			type: 'GET',
			xhrFields: {
				withCredentials: false
			},

			success: function (data) {

				data = data.responseData.data;
				if (data.length != 0) {
					viewBots(data);
					let searchBotValue = $("#searchBots").val();
					if (searchBotValue != "") {
						callGlobalSearch(searchBotValue);
					}

				}
				else {
					$('.notFound').remove();
					$('.inprogressTask_container').append('<div class="notFound">No data found!</div>');
				}

			},
			error: function (xhr, status, statusText) {
				pwIsf.removeLayer();
				console.log('An error occurred');
			},
			complete: function (xhr, status, statusText) {
				pwIsf.removeLayer();
			}
		});
	}
	pwIsf.removeLayer();
}

//Function for searching in a div 
function searchInDiv(searchObj, callBacksObj) {

	var searchIn = $(searchObj.searchInTarget);
	searchIn.each(function (index, val) {

		let title = $(val).find(".shape-text").attr("title") + $(val).find(".boxHeaderCSS").attr("data-pt-title");
		var allText = ($(val).text() + title).toLowerCase();
		var inputText = searchObj.textToBeSearch;
			$('.notFound').remove();
		var ar = inputText.split(",");
		var condition = '';
		for (var j in ar) {
			condition += "allText.indexOf('" + ar[j] + "') == -1 || ";
		}
		condition = condition.substr(0, condition.length - 3);
		if (eval(condition)) {
			searchIn.eq(index).hide();
		}
		else {
			$('.notFound').remove();
			searchIn.eq(index).css('display', '');
		}
	});

	if (callBacksObj.callBack) {
		$(callBacksObj.mainContainer).trigger(callBacksObj.callBackFunction);
	}
	if (searchIn.children(':visible').length == 0) {
		$('.notFound').remove();
		$(callBacksObj.mainContainer).append('<div class="notFound">No data found!</div>');
	}
}

//View string of fixed length
function reduceString(str) {
	if (str.length > 6)
		return str.substring(0, 7) + ".."
	else
		return str + ".."
}



//Show download files div on hover of download button
function openDownloadDiv(obj) {

	$(obj).parent().find("#boxDownloadDropdown").animate({ bottom: "0px" }, 100);
}

//to Open model for Demo UR
function openURLModel(url) {
	$('#demoURLPopup').modal('show');
	$('#videoURL').val(url);
}

//Close download files div on leaving div area
function closeFileDiv(obj) {
	$(obj).animate({ bottom: "-75px" }, 100);

}

//Format Date
function formatDate(date) {
	var d = new Date(date),
		month = '' + (d.getMonth() + 1),
		day = '' + (d.getDate()),
		year = d.getFullYear();

	if (month.length < 2) month = '0' + month;
	if (day.length < 2) day = '0' + day;

	return [year, month, day].join('-');
}

//View Bot info
function viewBotDetailsDeployed(requestID, projectName, botLanguage) {
	pwIsf.addLayer({ text: 'Please wait ...' });

	$('#requestDetailsInfo').empty();
	$('.updateBtn').remove();

	$.isf.ajax({
		url: `${service_java_URL}rpaController/getAllRPARequestDetails/${requestID}`,
		context: this,
		crossdomain: true,
		processData: true,
		contentType: 'application/json',
		type: 'GET',
		xhrFields: {
			withCredentials: false
		},
		success: function (data) {
			let newTableHtml = '';
			if (!jQuery.isEmptyObject(data)) {
				const getData = getJsonObjectRPARequest(data);
				var stepIdNameDisplay = "";
				var wfIdNameDisplay = "";
				if (!getData.stepID) {
					stepIdNameDisplay = "none";
				}
				if (!getData.wfID) {
					wfIdNameDisplay = "none";
				}
				$('.testNewBotRequest').attr('data-details', JSON.stringify({ reqId: requestID, signum: signumGlobal }));
				$('.stopTestBtnBotRequest').attr('data-details', JSON.stringify({ reqId: requestID, signum: signumGlobal }));


				var checkBox = getchkBoxInputRequiredHtml();
                var updateBtn = `<button type="button" id="btnUpdateOwner" class="btn btn-success updateBtn" 
                                    onclick="updateInputFileStatus('${signumGlobal}',${getData.botId})" 
                                    data-dismiss="modal" style="margin-left:10px">Update</button>`;

				if (ApiProxy === false && getData.url === null) {
					newTableHtml = getHtmlTableForViewDeployedBotDetails(getData, stepIdNameDisplay, wfIdNameDisplay, requestID);
				}
				else if (ApiProxy === false) {
					newTableHtml = getHtmlTableWhenUrlNullViewDeployedBotDetails(getData, stepIdNameDisplay, wfIdNameDisplay, requestID);
				}
				else {
					newTableHtml = getDefaultViewDeployedBotDetails(getData, stepIdNameDisplay, wfIdNameDisplay, projectName);
				}
				$('#requestDetailsInfo').append(newTableHtml);
				$('#botDetailPopUp').append(checkBox);
				// populate checkbox by isInputRequired value from DB
				if (getData.isInputRequired) {
					$('.inputRequiredCheckboxOwnDev').attr('checked', getData.isInputRequired);
					if (botLanguage && botLanguage.toLowerCase() === 'macro') {
						$("input.inputRequiredCheckboxOwnDev").prop("disabled", true);
						$("input.inputRequiredCheckboxOwnDev").parent().attr({ disabled: true });
						// Remove update html button on viweDeployedBotPopup;
						updateBtn = `<button type="button" id="btnUpdateOwner" class="btn btn-success updateBtn" 
                                    onclick="updateInputFileStatus('${signumGlobal}',${getData.botId})" 
                                    data-dismiss="modal" style="margin-left:10px;display:none">Update</button>`;
					}
				} else {
					$(".infile").addClass("disabledbutton");
					$(".inputLabel").css("color", '#868598');
					$(".inputLabel").text("Bot Input Not Required");
				}
				//  validate user to edit checkbox and append update button
				checkValidUser(getData.botId, updateBtn);
			}
			else {
				newTableHtml = `No records returned !!!`;
				const newH = `<button type="button" class="btn btn-primary" data-dismiss="modal" style="margin-left:10px">Close</button>`;
				$('#requestDetailsInfo').append(newTableHtml);
				$('#requestfoot').html('').append(newH);
			}
		},
		error: function (xhr, status, statusText) {
			console.log('An error occurred');
		},
		complete: function () {
			$('#requestInfo').modal('show');
			pwIsf.removeLayer();
		}
	});

}


// toggel checkbox
function toggleCheckbox(element) {


	var inputRqdStr = 'Bot Input Required';
	var inputNotRqdStr = 'Bot Input Not Required';
	var greenColor = '#1fb65b';
	var greyColor = '#868598';

	if (!element.checked) {
		$('.infile').addClass("disabledbutton");
		$('.inputLabel').css("color", greyColor);
		$('.inputLabel').text(inputNotRqdStr);
	} else {
		$('.infile').removeClass("disabledbutton");
		$('.inputLabel').css("color", greenColor);
		$('.inputLabel').text(inputRqdStr);

		$('.inputRequiredCheckboxOwnDev').attr('checked', element.checked);
	}
}

// check if User is valid to update the input required i.e PM/DR/Bot Developer
function checkValidUser(botId, updateBtn) {
	$.isf.ajax({
		url: `${service_java_URL}rpaController/getIsUserValidForBotChanges?signum=${signumGlobal}&botID=${botId}`,
		context: this,
		crossdomain: true,
		processData: true,
		contentType: 'application/json',
		type: 'GET',
		async: false,
		xhrFields: {
			withCredentials: false
		},
		success: function (data) {
			if (data.responseData) {
				$('.modal-footer').prepend(updateBtn);
			} else {
				$('.checkbox-container').addClass('disabledbutton');
			}
		},
		error: function (xhr, status, statusText) {
			console.log('An error occurred');
		},
		complete: function (xhr, statusText) {
			pwIsf.removeLayer();
		}
	});
}

// update checkbox
function updateInputFileStatus(signum, botId) {
	pwIsf.addLayer({ text: 'Please wait ...' });

	var isInputRequired = $(".inputRequiredCheckboxOwnDev").is(":checked") ? 1 : 0;

	$.isf.ajax({
		url: `${service_java_URL}botStore/updateInputFileStatus?isInputRequired=${isInputRequired}&botId=${botId}&workFlowOwner=${signum}`,
		context: this,
		crossdomain: true,
		processData: true,
		contentType: 'application/json',
		type: 'POST',
		async: false,
		xhrFields: {
			withCredentials: false
		},
		success: function (data) {
			if (data.apiSuccess) {
				pwIsf.alert({ msg: data.responseMsg, type: 'success' });
			}
		},
		error: function (xhr, status, statusText) {
			console.log('An error occurred');
		},
		complete: function (xhr, statusText) {
			pwIsf.removeLayer();
			getAllBots();
		}
	}); // end ajax call
}
//Create structure of Tiles/Box
function createBotBox(bot) {
	let dBox = '';
	var eme;
	const botIDName = getBotIDName(bot);
	if (bot.EME === null) {
		eme = 'NA';
	}
	else {
		eme = bot.EME;
	}

	const languageBaseVersion = bot.LanguageBaseVersion === null ? 'NA' : bot.LanguageBaseVersion;
	var projectName = bot.ProjectName;
	const boxClass = 'dBox-success';
	const blankStr = '_blank';

	const botDetailRowHtml = function (title, value, nowrapTitle = false) {
		return `<div class="content-row">
            <span class="lbl" ${nowrapTitle ? 'style="white-space:nowrap;"' : ''}>${title}: </span>
            <p>${value}</p>
            </div>`
    }
	const videoUrlHtml = (bot.videoURL === null || bot.videoURL === undefined) ? `` :
		`<div class="row" style="width: 100%;margin-left: 0px;">
				<div class="col-sm-7" style="padding-right: 0px;padding-left: 10px;">
				<a class="fileLink" href="#" title="Demo Video URL" onclick="window.open("${bot.videoURL}","${blankStr}");">Video URL&nbsp;<i class="fa fa-download"></i></a>
				</div>
				</div>`;

	dBox += `<div class="dBox ${boxClass} inprogressTask_dBox">
            <div class="shape">
            <div class="shape-text" title="${bot.BOTLanguage}">
            ${reduceString(bot.BOTLanguage)}
            </div>
            </div>
            <div class="dBox-content">
            <div class="content-row">
            <p class="boxHeaderCSS protip" data-pt-scheme="aqua" data-pt-position="top" data-pt-skin="square"
				data-pt-width="400" data-pt-title="${bot.BOTID}/${bot.BOTName}">${botIDName}</p>
            </div>
			${botDetailRowHtml('EME(hour)', eme, true)}
			${botDetailRowHtml('Subactivity', bot.SubActivity)}
			${botDetailRowHtml('Deployed On', ConvertDateTime_tz(new Date(bot.DeployedOn)))}
			${botDetailRowHtml('BOT Execution Hours', bot.BOTExecutionHours)}
			${botDetailRowHtml('BOT Execution Fail Count', bot.BOTExecutionFailCount)}
			${botDetailRowHtml('Language Base Version', languageBaseVersion)}
            <div id="boxDownloadDropdown" class="downloadButtonClass" onmouseleave="closeFileDiv(this)">
            <div class="row" style="width: 100%;margin-left: 0px;">
            <div class="col-sm-3" style="padding-right: 0px;padding-left: 8px;">
            <a class="fileLink" href="#" title="Input File"
				onclick="downloadBotFiles({thisObj:this,botId:${bot.BOTID},fileName:\'input.zip\'});">
				Input&nbsp;<i class="fa fa-download"></i>
			</a>
            </div>
            <div class="col-sm-3" style="padding-right: 0px;padding-left: 8px;">
            <a class="fileLink" href="#" title="Output File"
				onclick="downloadBotFiles({thisObj:this,botId:${bot.BOTID},fileName:\'output.zip\'});">
				Output<i class="fa fa-download"></i>
			</a>
            </div>
            <div class="col-sm-3" style="padding-right: 0px;padding-left: 13px;">
            <a class="fileLink" href="#" title="Code File"
				onclick="downloadBotFiles({thisObj:this,botId:${bot.BOTID},fileName:\'code.zip\'});">
				Code&nbsp;<i class="fa fa-download"></i>
			</a>
            </div>
            <div class="col-sm-3" style="padding-right: 0px;padding-left: 10px;">
            <a class="fileLink" href="#" title="Logic File"
				onclick="downloadBotFiles({thisObj:this,botId:${bot.BOTID},fileName:\'logic.zip\'});">
				Logic&nbsp;<i class="fa fa-download"></i>
			</a>
            </div>
            </div>
			${videoUrlHtml}
            </div>
            <a id="boxDownloadFiles" title="Download BOT Files" href="#" class="icon-add lsp boxDownloadIcon"
				onmouseover="openDownloadDiv(this)"><i class="fa fa-download"></i>
			</a>
            <a title="View BOT" href="#" class="icon-plan lsp boxInfoIcon"
				onclick="viewBotDetailsDeployed(${bot.BOTID},'${projectName}','${bot.BOTLanguage}')"
				data-toggle="modal" data-target="#requestInfo" href="javascript:void()"><i class="fa fa-info"></i>
			</a>
            </div>
            </div>`;
	return dBox;
}
function getBotIDName(bot) {
	let botIDName = `${bot.BOTID}/${bot.BOTName}`;
	if (botIDName.length > 27) {
		botIDName = `${botIDName.substring(0, 28)}..`;
	}
	return botIDName;
}
function getJsonObjectRPARequest(responseData) {
	let getData = {};
	getData.spocsignum = responseData[0].SPOC;
	getData.proj = responseData[0].ProjectName;
	getData.botexecutioncount = responseData[0].BOTExecutedCount;
	getData.botexecutionhours = responseData[0].BOTExecutionHours;
	getData.description = responseData[0].Description;
	getData.tooltype = responseData[0].ToolType;
	getData.activity = responseData[0].Activity;
	getData.accessmethod = responseData[0].AccessMethod;
	getData.domain = responseData[0].Domain;
	getData.eme = responseData[0].EME;
	getData.BotExecutionCount = responseData[0].BotExecutionCount;
	getData.BotReuseCount = responseData[0].BotReuseCount;
	getData.botId = responseData[0].BOTID;
	getData.botName = responseData[0].BOTName;
	getData.tech = responseData[0].Technology;
	getData.isInputRequired = responseData[0].IsInputRequired;
	getData.SubactivityName = responseData[0].SubActivity;
	getData.TaskName = responseData[0].TaskName;
	getData.toolNames = responseData[0].ToolName;
	getData.url = responseData[0].VideoURL;
	// step ID, step name, WF ID and WF name
	getData.stepID = responseData[0].StepID;
	getData.stepName = responseData[0].StepName;
	getData.wfID = responseData[0].WFID;
	getData.wfName = responseData[0].WorkFlowName;
	getData.LanguageBaseVersionID = responseData[0].LanguageBaseVersionID;
	getData.LanguageBaseVersion = responseData[0].LanguageBaseVersion;


	return getData;
}
function getchkBoxInputRequiredHtml() {
	return `<tr>
                  <div class="col-md-6">
                                        <td class="heading"><label>Input Required :</label></td>
                                        <td colspan="3">
                                        <div style="margin-left:-15px;">
                                            <div class="col-md-1" style="margin: 0px;padding-left: 4px;">
                                                <div class="checkbox-container">
                                                    <label class="checkbox-label">
                                                        <input class="inputRequiredCheckboxOwnDev" type="checkbox" onchange="toggleCheckbox(this)">
                                                        <span class="checkbox-custom rectangular"></span>
                                                    </label>
                                                </div>
                                            </div>
                                            <div>
                                                <label class="forLabel inputLabel">Bot Input Required</label>
                                            </div>
                                        </div>
                                        </td>
                                    </div>
                                    </tr>`;
}
function getHtmlTableForViewDeployedBotDetails(getData, stepIdNameDisplay, wfIdNameDisplay, requestID) {
	return `<table class="table table-striped">
                            <thead>
                                <tr>
                                    <th colspan="2"><h5><b>Bot ID:</b> ${getData.botId}</h5></th>
                                    <th colspan="2"><h5><b>Bot Name:</b> ${getData.botName}</h5></th>

                                </tr>
                                <tr>
                                    <th colspan="4"><h5><b>Project Name:</b> ${getData.proj}</h5></th>

                                </tr>
                            </thead>
                            <tbody id="botDetailPopUp">
                                <tr>
                                    <td class="heading">Spoc(Name/Signum):</td>
                                    <td>${getData.spocsignum}</td>

                                </tr>
                               <tr>
                                    <td class="heading">Domain/Subdomain:</td>
                                    <td>${getData.domain}</td>
                                    <td class="heading">Tool Name:</td>
                                    <td>${getData.toolNames}</td>
                                </tr>
                                <tr>
                                    <td class="heading">Tool Type:</td>
                                    <td colspan="3">${getData.tooltype}</td>
                                </tr>
                                 <tr>
                                <td class="heading">Task Name:</td>
                                <td colspan="3">${getData.TaskName}</td>
                                </tr>

                                 <tr>
                               <td class="heading" style="display:${stepIdNameDisplay}">Step ID/Name:</td>
                                    <td style="display:${stepIdNameDisplay}">${getData.stepID}/${getData.stepName}</td>
                                    <td class="heading" style="display:${wfIdNameDisplay}">WF ID/Name:</td>
                                    <td style="display:${wfIdNameDisplay}">${getData.wfID}/${getData.wfName}</td>
                                </tr>

                                <tr>
                                 <td class="heading">Technology:</td>
                                <td>${getData.tech}</td>
                                <td class="heading">Activity/Subactivity:</td>
                                <td>${getData.activity} / ${getData.SubactivityName}</td>
                                </tr>
                                <tr>
                                        <td class="heading">Access Method:</td>
                                        <td colspan="3">${getData.accessmethod}</td>
                                    </tr>
                                <tr>
                                    <td class="heading">Description:</td>
                                    <td colspan="3">${getData.description}</td>                                      
                                </tr>
                                  <tr>
                                        <td class="heading">Bot Executions:</td>
                                        <td >${getData.BotExecutionCount}</td>
                                         <td class="heading">Bot Reused:</td>
                                        <td >${getData.BotReuseCount}</td>
                                  </tr>
 <tr>
                                    <td class="heading">Language Base Version</td>
                                    <td colspan="3">${getData.LanguageBaseVersion === null ? 'NA' : getData.LanguageBaseVersion}</td>
                                </tr>
                                <tr>
                                    <td class="heading">Downloadable Files:</td>
                                    <td colspan="3"><button class='infile btn btn-default' type="button" title='Input File' onclick="downloadBotFiles({thisObj:this,botId:${requestID},fileName:'input.zip'});"><i class='fa fa-download' aria-hidden='true'></i> Input</button><button class=' btn btn-default' type="button" title='Output File' onclick="downloadBotFiles({thisObj:this,botId:${requestID},fileName:'output.zip'});"><i class='fa fa-download' aria-hidden='true'></i> Output</button><button class=' btn btn-default' type="button" title='Code File' onclick="downloadBotFiles({thisObj:this,botId:${requestID},fileName:'code.zip'});"><i class='fa fa-download' aria-hidden='true'></i>Code</button><button class=' btn btn-default' type="button" title='Logic File' onclick="downloadBotFiles({thisObj:this,botId:${requestID},fileName:'logic.zip'});"><i class='fa fa-download' aria-hidden='true'></i>Logic</button></td>
                                
                                </tr>
                            </tbody>
                        </table>`;
}

function getDefaultViewDeployedBotDetails(getData, stepIdNameDisplay, wfIdNameDisplay,proj) {
	return `<table class="table table-striped">
                            <thead>
                                <tr>
                                    <th colspan="2"><h5><b>Bot ID:</b> ${getData.botId}</h5></th>
                                    <th colspan="2"><h5><b>Bot Name:</b> ${getData.botName}</h5></th>

                                </tr>
                                <tr>
                                    <th colspan="4"><h5><b>Project Name:</b> ${getData.proj}</h5></th>

                                </tr>
                            </thead>
                            <tbody id="botDetailPopUp">
                                <tr>
                                    <td class="heading">Spoc(Name/Signum):</td>
                                    <td>${getData.spocsignum}</td>

                                </tr>
                               <tr>
                                    <td class="heading">Domain/Subdomain:</td>
                                    <td>${getData.domain}</td>
                                    <td class="heading">Tool Name:</td>
                                    <td>${getData.toolNames}</td>
                                </tr>
                                <tr>
                                    <td class="heading">Tool Type:</td>
                                    <td colspan="3">${getData.tooltype}</td>
                                </tr>
                                 <tr>
                                <td class="heading">Task Name:</td>
                                <td colspan="3">${getData.TaskName}</td>
                                </tr>


                                 <tr>
                                 <td class="heading" style="display:${stepIdNameDisplay}">Step ID/Name:</td>
                                    <td style="display:${stepIdNameDisplay}">${getData.stepID}/${getData.stepName}</td>
                                    <td class="heading" style="display:${wfIdNameDisplay}">WF ID/Name:</td>
                                    <td style="display:${wfIdNameDisplay}">${getData.wfID}/${getData.wfName}</td>
                                </tr>

                                <tr>
                                 <td class="heading">Technology:</td>
                                <td>${getData.tech}</td>
                                <td class="heading">Activity/Subactivity:</td>
                                <td>${getData.activity} / ${getData.SubactivityName}</td>
                                </tr>
                                <tr>
                                    <td class="heading">Access Method:</td>
                                        <td colspan="3">${getData.accessmethod}</td>
                                    </tr>
                                <tr>
                                    <td class="heading">Description:</td>
                                    <td colspan="3">${getData.description}</td>
                                </tr>
                            </tbody>
                        </table>`;
}
function getHtmlTableWhenUrlNullViewDeployedBotDetails(getData, stepIdNameDisplay, wfIdNameDisplay, requestID) {
	return `<table class="table table-striped">
                            <thead>
                                <tr>
                                    <th colspan="2"><h5><b>Bot ID:</b> ${getData.botId}</h5></th>
                                    <th colspan="2"><h5><b>Bot Name:</b> ${getData.botName}</h5></th>

                                </tr>
                                <tr>
                                    <th colspan="4"><h5><b>Project Name:</b> ${getData.proj}</h5></th>

                                </tr>
                            </thead>
                            <tbody id="botDetailPopUp">
                                <tr>
                                    <td class="heading">Spoc(Name/Signum):</td>
                                    <td>${getData.spocsignum}</td>

                                </tr>
                               <tr>
                                    <td class="heading">Domain/Subdomain:</td>
                                    <td>${getData.domain}</td>
                                    <td class="heading">Tool Name:</td>
                                    <td>${getData.toolNames}</td>
                                </tr>
                                <tr>
                                    <td class="heading">Tool Type:</td>
                                    <td colspan="3">${getData.tooltype}</td>
                                </tr>
                                 <tr>
                                <td class="heading">Task Name:</td>
                                <td colspan="3">${getData.TaskName}</td>
                                </tr>
                                

                                 <tr>
                               <td class="heading" style="display:${stepIdNameDisplay}">Step ID/Name:</td>
                                    <td style="display:${stepIdNameDisplay}">${getData.stepID}/${getData.stepName}</td>
                                    <td class="heading" style="display:${wfIdNameDisplay}">WF ID/Name:</td>
                                    <td style="display:${wfIdNameDisplay}">${getData.wfID}/${getData.wfName}</td>
                                </tr>

                                <tr>
                                 <td class="heading">Technology:</td>
                                <td>${getData.tech}</td>
                                <td class="heading">Activity/Subactivity:</td>
                                <td>${getData.activity} / ${getData.SubactivityName}</td>
                                </tr>
                                <tr>
                                    <td class="heading">Access Method:</td>
                                        <td colspan="3">${getData.accessmethod}</td>
                                      
                                    </tr>
                                <tr>
                                    <td class="heading">Description:</td>
                                    <td colspan="3">${getData.description}</td>
                                      
                                </tr>
                                  <tr>
                                        <td class="heading">Bot Executions:</td>
                                        <td >${getData.BotExecutionCount}</td>
                                         <td class="heading">Bot Reused:</td>
                                        <td >${getData.BotReuseCount}</td>
                                  </tr>

                                <tr>
                                    <td class="heading">Language Base Version:</td>
                                    <td>${getData.LanguageBaseVersion === null ? 'NA' : getData.LanguageBaseVersion}</td>
                                </tr>
                                

                                <tr>
                                    <td class="heading">Downloadable Files:</td>
                                    <td colspan="3"><button class='infile btn btn-default' type="button" title='Input File' onclick="downloadBotFiles({thisObj:this,botId:${requestID},fileName:'input.zip'});"><i class='fa fa-download' aria-hidden='true'></i> Input</button><button class=' btn btn-default' type="button" title='Output File' onclick="downloadBotFiles({thisObj:this,botId:${requestID},fileName:'output.zip'});"><i class='fa fa-download' aria-hidden='true'></i> Output</button><button class=' btn btn-default' type="button" title='Code File' onclick="downloadBotFiles({thisObj:this,botId:${requestID},fileName:'code.zip'});"><i class='fa fa-download' aria-hidden='true'></i>Code</button><button class=' btn btn-default' type="button" title='Logic File' onclick="downloadBotFiles({thisObj:this,botId:${requestID},fileName:'logic.zip'});"><i class='fa fa-download' aria-hidden='true'></i>Logic</button><button class=' btn btn-default' type="button" title='Logic File' onclick="window.open(\'${getData.url}\','_blank');"><i class='fa fa-download' aria-hidden='true'></i>Video URL</button></td>
                                
                                </tr>
                            </tbody>
                        </table>`;
}