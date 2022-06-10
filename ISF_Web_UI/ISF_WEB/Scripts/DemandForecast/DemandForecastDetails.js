/*-------------Initialize Arrays for Values----------------------*/
var arrJobStage = [], arrToolList = [], arrDomSubDom = [], arrServSubServ = [];
var arrTech = [], arrJobRoles = [], arrVendors = [], arrScopes = [], arrVendorTech = [];
var arrSPType = [
    { key: "Ericsson", label: "Ericsson" },
    { key: "ARP", label: "ARP" }
];
var arrDemandType = [];
var arrSpSource = [
    { key: "ALL-GSDU", label: "ALL-GSDU" },
    { key: "GSDU-China", label: "GSDU-China" },
    { key: "GSDU-India", label: "GSDU-India" },
    { key: "GSDU-Romania", label: "GSDU-Romania" },
    { key: "MANA", label: "MANA" },
    { key: "MELA", label: "MELA" },
    { key: "MNEA", label: "MNEA" },
    { key: "MOAI", label: "MOAI" },
    { key: "MMEA", label: "MMEA" },
    { key: "DMH-MANA", label: "DMH-MANA" },
    { key: "DMH-MELA", label: "DMH-MELA" },
    { key: "DMH-MNEA", label: "DMH-MNEA" },
    { key: "DMH-MOAI", label: "DMH-MOAI" },
    { key: "DMH-MMEA", label: "DMH-MMEA" },
];
var arrFteList = [
    { key: "25", label: "25" },
    { key: "50", label: "50" },
    { key: "75", label: "75" },
    { key: "100", label: "100" }
];
var ganttDemand;
const C_SELECT_DEFAULT_VAL = '--Select--';


const searchStartIndex = 0;
const searchEndIndex = 50;
const C_VENDOR_TECH_BOX_ID = '#vendorTechSelect';

/*-------------Initiate Gantt Instance-----------------*/
$(document).ready(function () {
    ganttDemand = Gantt.getGanttInstance();
    //ganttDemand.keys.edit_save = -1;
});

/*-------------Get Gantt for Default and Get API-----------------*/
function getGantt(projectID) {
    var dt;
    dt = {
        "data": [
            {
                "projectScopeId": "",
                "resourceType": C_SELECT_DEFAULT_VAL,
                "remoteCount": 0,
                "onsiteCount": 0,
                "vendor": C_SELECT_DEFAULT_VAL,
                "jobRoleId": "",
                "jobStageId": "",
                "demandType": C_SELECT_DEFAULT_VAL,
                "demandTypeID": "",
                "positionId": null,
                "jobRoleName": C_SELECT_DEFAULT_VAL,
                "jobStageName": C_SELECT_DEFAULT_VAL,
                "projectScopeName": C_SELECT_DEFAULT_VAL,
                "workEffortId": null,
                "status": "",
                "serviceArea": C_SELECT_DEFAULT_VAL,
                "toolList": C_SELECT_DEFAULT_VAL,
                "tech": C_SELECT_DEFAULT_VAL,
                "domain_subDomain": C_SELECT_DEFAULT_VAL,
                "spSource": C_SELECT_DEFAULT_VAL,
                "hours": 0,
                "fte": 100,
                "duration": 0,
                "country": "",
                "city": "",
                "latitude": 0,
                "longitude": 0,
                "description": null,
                "timeZone": null,
                "serviceAreaID": "",
                "vendorTechModel": "",
                "domainID": "",
                "start_date": Glob_projectStartDateConfig,
                "end_date": Glob_projectEndDateConfig
            }
        ]
    };

    Glob_projectID = projectID;
    var kt;
    var objk = {};
    pwIsf.addLayer({ text: C_PLEASE_WAIT });
    var serviceUrl = `${service_java_URL}demandForecast/getDemandForecastDetails?signum=${signumGlobal}&startDate=${Glob_startDate}&pageSize=${Glob_pageSize}&projectId=${projectID}&role=${Glob_logedInUserRole}&marketArea=${Glob_marketArea}`;
    if (ApiProxy) {
        const encodedRequest = encodeURIComponent(`&startDate=${Glob_startDate}&pageSize=${Glob_pageSize}&projectId=${projectID}&role=${Glob_logedInUserRole}&marketArea=${Glob_marketArea}`);
        serviceUrl = `${service_java_URL}demandForecast/getDemandForecastDetails?signum=${signumGlobal}${encodedRequest}`;
    }

    $.isf.ajax({
        type: "GET",
        url: serviceUrl,
        success: appendPlanSources,
        error: function (msg) {
            //BLANK
        },
        complete: function (msg) {
            pwIsf.removeLayer();
        }
    });

    function appendPlanSources(data) {
        ganttDemand.attachEvent("onBeforeParse", function () { ganttDemand.clearAll(); });
        ganttDemand.templates.task_text = function (start, end, task) {
            return "";
        };
        ganttDemand.templates.tooltip_text = function (start, end, task) {
            return "";
        };
        ganttDemand.templates.grid_row_class = function (start, end, task) {
            if (Glob_logedInUserRole !== "Fulfillment Manager") {
                if (task.status.toLowerCase() === "sendtorpm") {
                    return "sendToRPMColor";
                }
                else if (task.status.toLowerCase() === "submitted") {
                    return "submittedColor";
                }
                else if (task.status.toLowerCase() === "draft") {
                    return "draftColor";
                }
                else if (task.status.toLowerCase() === "savedbyspm") {
                    return "savedBySPM";
                }
                else if (task.status.toLowerCase() === "sendtospm") {
                    return "sendToSPM";
                }
            }
        };
        ganttDemand.config.server_utc = true;
        ganttDemand.config.scale_height = 60;
        ganttDemand.config.scale_unit = "year";
        ganttDemand.config.date_scale = "%Y";
        ganttDemand.config.subscales = [
            { unit: "month", step: 1, date: "%M" },
            { unit: "week", step: 1, date: "#%W" }
        ];
        ganttDemand.config.start_date = Glob_projectStartDateConfig;
        ganttDemand.config.end_date = Glob_projectEndDateConfig;
        ganttDemand.config.duration_unit = "hour";
        ganttDemand.setWorkTime({ hours: [9, 17] }); //changing default 9 hours to 8 hours
        ganttDemand.config.work_time = true; //changes the working time of working days
        ganttDemand.config.min_column_width = 70;
        ganttDemand.config.autosize = "xy";
        ganttDemand.init("gantt_here_demand");
        if (data.length === 0) {
            $(".saveDraftDetails ").removeClass("disabledbutton");
            $(".sendNextDetails ").removeClass("disabledbutton");
            pwIsf.alert({ msg: 'No records found,please fill the details', type: 'warning', autoClose: 2 });
            ganttDemand.parse(dt);
        }
        else {
            kt = data;
            objk.data = kt;
            ganttDemand.parse(objk);
        }
    }
}

/*-------------Fill Data in Arrays of Gantt Fields-----------------*/
function populateDataServerList() {

    pwIsf.addLayer({ text: C_PLEASE_WAIT });

    const getJobStatgesAPI = `${service_java_URL}resourceManagement/getJobStages`;
    const getJobRolesAPI = `${service_java_URL}resourceManagement/getJobRoles`;
    const getVendorDetailsAPI = `${service_java_URL}activityMaster/getVendorDetails`;
    const getDemandTypeAPI = `${service_java_URL}accessManagement/getDemandType`;
    const getProjectSpecificToolsAPI = `${service_java_URL}projectManagement/getProjectSpecificTools`;

    $.isf.ajax({
        url: getJobStatgesAPI,
        success: function (data) {
            populateJobStages(data);
            $.isf.ajax({
                url: getJobRolesAPI,
                success: function (data) {
                    populateJobRoles(data);
                    $.isf.ajax({
                        url: getDemandTypeAPI,
                        success: function (data) {
                            populateDemandType(data);
                            $.isf.ajax({
                                url: `${service_java_URL}demandForecast/getScopeDetails?projectId=${Glob_projectID}`,
                                success: function (data) {
                                    populateScopes(data);
                                    if (Glob_projectID) {
                                        var serviceUrl = `${getProjectSpecificToolsAPI}?isOnlyActiveRequired=1&projectID=${Glob_projectID}`;
                                        if (ApiProxy) {
                                            const encodeUriText = encodeURIComponent(`isOnlyActiveRequired=1&projectID=${Glob_projectID}`);
                                            serviceUrl = `${getProjectSpecificToolsAPI}?${encodeUriText}`;
                                        }
                                        $.isf.ajax({
                                            url: serviceUrl,
                                            success: function (data) {
                                                data = data.responseData;
                                                populateTools(data);
                                            },
                                            complete: function (data) {
                                                configureganttDemand(arrScopes, arrSPType, arrVendors, arrJobRoles, arrJobStage,
                                                    arrDomSubDom, arrServSubServ, arrTech, arrSpSource, arrToolList, arrDemandType);
                                            }
                                        });
                                    }
                                    else {
                                        pwIsf.alert({ msg: "Project ID missing", type: "error" });
                                    }

                                },
                            });
                        }

                    });
                }
            });
        }
    });

}
    function populateJobStages(data) {
        arrJobStage = [];
        $.each(data, function (i, d) {
            var objJobStage = {};
            objJobStage.key = d.JobStageID;
            objJobStage.label = d.JobStageName;
            arrJobStage.push(objJobStage);
        })
    }

    function populateDemandType(data) {
        arrDemandType = [];
        $.each(data.responseData, function (i, d) {
            var objDemandType = {};
            objDemandType.key = d.demandTypeId;
            objDemandType.label = d.demandType;
            arrDemandType.push(objDemandType);
        })
    }

    function populateJobRoles(data) {
        arrJobRoles = [];
        $.each(data, function (i, d) {
            var objJobRoles = {};
            objJobRoles.key = d.JobRoleID;
            objJobRoles.label = d.JobRoleName;
            arrJobRoles.push(objJobRoles);
        })
    }

    function populateTools(data) {
        arrToolList = [];
        $.each(data, function (i, d) {
            var objTools = {};
            objTools.key = d.ToolID;
            objTools.label = d.Tool;
            arrToolList.push(objTools);
        })
    }

    function populateScopes(data) {
        arrScopes = [];
        $.each(data, function (i, d) {
            var objScopes = {};
            objScopes.key = d.ProjectScopeID;
            objScopes.key = d.ScopeName;
            arrScopes.push(objScopes);
        })
    }



/*-------------Show JobRole Name on Gantt-----------------*/
function jobRoleTemplate(task) {
    var value = task.jobRoleId;
    var list = ganttDemand.serverList("jobRole");
    for (var i = 0; i < list.length; i++) {
        if (list[i].key == value) {
            return list[i].label;
        }
    }
    return "";
}



/*-------------Show Jobstage Name on Gantt-----------------*/
function jobStageTemplate(task) {
    var value = task.jobStageId;
    var list = ganttDemand.serverList("jobStage");
    for (var i = 0; i < list.length; i++) {
        if (list[i].key == value) {
            return list[i].label;
        }
    }
    return "";
}

/*-------------Show Domain Name on Gantt-----------------*/
function domainTemplate(task) {
    var value = task.domainID;
    var list = ganttDemand.serverList("domain");
    if (list.length) {
        for (var i = 0; i < list.length; i++) {
            if (list[i].key == value) {
                return list[i].label;
            }
        }
    } else {
        return task.domain_subDomain;
    }
    return "";
}

/*-------------Show Tools Name on Gantt-----------------*/
function toolsTemplate(task) {
    returnToolArr = [];
    var value = task.toolList || [];
    var list = ganttDemand.serverList("toolList");
    if (list.length) {
        for (var i = 0; i < list.length; i++) {
            for (var j = 0; j < value.length; j++) {
                if (list[i].key == value[j]) {
                    returnToolArr.push(list[i].label);
                }
            }
        }
        return returnToolArr;
    }
    else {
        return "";
    }
}

/*-------------Show Service Area Name on Gantt-----------------*/
function serviceAreaTemplate(task) {
    var value = task.serviceAreaID;
    var list = ganttDemand.serverList("serviceArea");
    if (list.length) {
        for (var i = 0; i < list.length; i++) {
            if (list[i].key == value) {
                return list[i].label;
            }
        }
    } else {
        return task.serviceArea;
    }
    return "";
}

/*-------------Show Vendor-Tech Name on Gantt-----------------*/
function vendorTechTemplate(task) {
    if (task.vendorTechModel.length > 1) {
        arrVendorTech = [];
        $.each(task.vendorTechModel, function (i, d) {
            arrVendorTech.push(d.vendorTech);
        })
       
        return `<span onclick="showMultiVendorTechView(${task.id})">
            <input id="selectedVendorTech_${task.id}" type="hidden" value="${arrVendorTech}"/>
            <u style="color:blue;cursor:pointer;"  title="Click to View All Vendor-Tech Combinations">View Multi</u>
            </span>`;
    }
    else {
        if (task.vendorTechModel !== "") {
            arrVendorTech = task.vendorTechModel[0].vendorTech;
            return `<span>
            <input id="selectedVendorTech_${task.id}" type="hidden" value="${arrVendorTech}"/>
            ${arrVendorTech}
            </span>`;
        }
        else {
            arrVendorTech = [];
            return `<span>
            <input id="selectedVendorTech_${task.id}" type="hidden" value="${arrVendorTech}"/>
            ${arrVendorTech}
            </span>`;
        }
    }
}

/*-------------Hours Calculation on Gantt-----------------*/
function hoursTemplate(task) {
    var fteValue = task.fte;
    var duration = (task.duration) + 8;
    var hours = (fteValue / 100) * duration;
    task.hours = hours;
    return hours;

}

/*-------------Actions of Tasks(Copy Row, Edit Deliverables, Delete Row)-----------------*/
var actionsContent = function (task) {
    return ('<i title="Copy Row" class="fa_action fa gantt_button_grid gantt_grid_add fa-plus"' +
        'onclick="clone_task(' + task.id + ')"></i>' +
        '<i title="Delete Row" class="fa_action fa gantt_button_grid gantt_grid_delete fa-times"' +
        'onclick="clickGridButton(' + task.id + ', \'delete\')"></i>' +
        '<i title="Click for Deliverables" class="fa_action fa gantt_button_grid gantt_grid_delete fa-edit"' +
        'onclick="clickforScope(' + task.id + ')"></i>');
};

/*-------------Open Lightbox for Editing-----------------*/
function clickforScope(tid) {
    ganttDemand.showLightbox(tid);
    setTimeout(function () { ganttDemand.showLightbox(tid); }, 50);
}

/*-------------Delete Task-----------------*/
function clickGridButton(id, action) {
    switch (action) {
        case "delete":
            ganttDemand.confirm({
                title: ganttDemand.locale.labels.confirm_deleting_title,
                text: ganttDemand.locale.labels.confirm_deleting,
                callback: function (res) {
                    if (res) {
                        var tasks = ganttDemand.getTaskByTime();
                        if (tasks.length != 1 && ganttDemand.getTask(id).status == "") {
                            ganttDemand.deleteTask(id);
                            return true;
                        }
                        else if (ganttDemand.getTask(id).status != "") {
                            deletedTaskArray = [];
                            ganttDemand.getTask(id).status = "DELETED";
                            ganttDemand.updateTask(id);
                            deletedTaskObj = ganttDemand.getTask(id);
                            deletedTaskArray.push(deletedTaskObj);
                            deleteTask("", deletedTaskArray, id);
                            ganttDemand.render();
                            return true;
                        }

                        else {
                            pwIsf.alert({ msg: "Cannot Delete the Default Single Row!", type: "warning", autoClose: 2 });
                            return false;
                        }
                    }
                }
            });
            break;
    }
}

/*-------------Copy Row Task-----------------*/
function clone_task(id) {
    var task = ganttDemand.getTask(id);
    var clone = ganttDemand.copy(task);

    if (clone.start_date < new Date()) {
        clone.start_date = new Date(new Date().getFullYear(), new Date().getMonth(), new Date().getDate());
    }
    clone.id = +(new Date());
    clone.positionId = null;
    clone.status = "";
    ganttDemand.addTask(clone, clone.parent, clone.$index + 1)
}

/*-------------Show Scopes Name on Gantt-----------------*/
function scopesTemplate(task) {
    var value = task.projectScopeId;
    var list = ganttDemand.serverList("scope");
    if (list.length) {
        for (var i = 0; i < list.length; i++) {
            if (list[i].key == value) {
                return list[i].projectScopeName;
            }
        }
    }
    else {
        return "";
    }
    return task.projectScopeName;
}

function demandTypeTemplate(task) {
    var value = task.demandTypeID;
    if (value == null) {
        task.demandType = "";
    }
    var list = ganttDemand.serverList("demandType");
    if (list.length) {
        for (var i = 0; i < list.length; i++) {
            if (list[i].key == value) {
                return list[i].label;
            }
        }
    }
    else {
        return "";
    }
    return task.demandType;
}

/*-------------Configure Gantt Setup-----------------*/
function configureganttDemand(scopes, arrSPType, vendors, jobRoles, arrJobStage, arrDomSubDom, arrServSubServ, arrTech, arrSPSource, arrToolList, arrDemandType) {
    ganttDemand.config.show_errors = false;
    ganttDemand.config.server_utc = true;
    ganttDemand.serverList("scope", scopes);
    ganttDemand.serverList("spType", arrSPType);
    ganttDemand.serverList("jobRole", jobRoles);
    ganttDemand.serverList("jobStage", arrJobStage);
    ganttDemand.serverList("domain", arrDomSubDom);
    ganttDemand.serverList("serviceArea", arrServSubServ);    
    ganttDemand.serverList("spSource", arrSPSource);
    ganttDemand.serverList("toolList", arrToolList);
    ganttDemand.serverList("fteList", arrFteList);
    ganttDemand.serverList("demandType", arrDemandType);
    ganttDemand.serverList("vendorTechModel", arrVendorTech);
    var demandTypeEditor = { type: "select", map_to: "demandTypeID", options: ganttDemand.serverList("demandType") };
    var cityEditor = { type: "text", map_to: "city" };
    var spTypeEditor = { type: "select", map_to: "resourceType", options: ganttDemand.serverList("spType") };
    var spSourceEditor = { type: "select", map_to: "spSource", options: ganttDemand.serverList("spSource") };
    var remoteCountEditor = { type: "number", map_to: "remoteCount", min: 0, max: 100 };
    var onsiteCountEditor = { type: "number", map_to: "onsiteCount", min: 0, max: 100 };
    var jobRoleEditor = { type: "select", map_to: "jobRoleId", options: ganttDemand.serverList("jobRole") };
    var jobStageEditor = { type: "select", map_to: "jobStageId", options: ganttDemand.serverList("jobStage") };
    var startDateEditor = {
        type: "date", map_to: "start_date",
        min: new Date()
    };
    var endDateEditor = {
        type: "date", map_to: "end_date"
    };
    var fteEditor = { type: "select", map_to: "fte", options: ganttDemand.serverList("fteList") };
    var descEditor = { type: "text", map_to: "description" };
    ganttDemand.config.start_date = Glob_projectStartDateConfig;
    ganttDemand.config.end_date = Glob_projectEndDateConfig;
    ganttDemand.setWorkTime({ hours: [9, 17] }); //changing default 9 hours to 8 hours
    ganttDemand.config.columns = [
        { name: "buttons", label: "Actions", width: 25, template: actionsContent },
        { name: "projectScopeId", label: "Deliverable", align: "center", resize: true, width: 20, template: scopesTemplate },
        { name: "serviceAreaID", label: "SubServiceArea", resize: true, align: "center", width: 20, template: serviceAreaTemplate },
        { name: "domainID", label: "SubDomain", align: "center", resize: true, width: 20, template: domainTemplate },
        { name: "vendorTechModel", label: "Vendor-Tech", align: "center", resize: true, width: 20, template: vendorTechTemplate },
        { name: "toolList", label: "Tools", align: "center", resize: true, width: 20, template: toolsTemplate },
        { name: "demandTypeID", label: "Demand Type", align: "center", resize: true, width: 20, editor: demandTypeEditor, template: demandTypeTemplate},
        { name: "city", label: "Location", align: "center", resize: true, width: 20, editor: cityEditor },
        { name: "resourceType", label: "NE_Type", align: "center", resize: true, width: 20, editor: spTypeEditor },
        { name: "spSource", label: "NE_Source", align: "center", resize: true, width: 20, editor: spSourceEditor },
        { name: "remoteCount", label: "Remote", align: "center", width: 20, resize: true, editor: remoteCountEditor },
        { name: "onsiteCount", label: "Onsite/Local", align: "center", width: 20, resize: true, editor: onsiteCountEditor },
        { name: "jobRoleId", label: "JobRole", align: "center", resize: true, width: 20, editor: jobRoleEditor, template: jobRoleTemplate },
        { name: "jobStageId", label: "JobStage", align: "center", resize: true, width: 20, editor: jobStageEditor, template: jobStageTemplate },
        { name: "start_date", label: "StartDate", align: "center", width: 20, resize: true, editor: startDateEditor },
        { name: "end_date", label: "End Date", align: "center", width: 20, resize: true, editor: endDateEditor },
        { name: "fte", label: "FTE%", align: "center", width: 20, resize: true, editor: fteEditor },
        { name: "hours", label: "Hours", align: "center", width: 20, resize: true, template: hoursTemplate },
        { name: "description", label: "Description", align: "center", width: 20, resize: true, editor: descEditor }
    ];
    ganttDemand.config.work_time = true;  // removes non-working time from calculations

    //Customise the LightBox
    ganttDemand.form_blocks["my_seditor"] = {

        render: function (sns) {
            const html = renderDefaultHTMLForDemand();
            $.isf.ajax({
                url: `${service_java_URL}demandForecast/getAllScopeDetailsByProject?projectId=${Glob_projectID}`,
                success: function (data) {
                    populateScopesLB(data);
                },
            });

            if (Glob_projectID) {
                var serviceUrl = `${service_java_URL}projectManagement/getProjectSpecificTools?isOnlyActiveRequired=1&projectID=${Glob_projectID}`;
                if (ApiProxy) {
                    const encodedDetails = encodeURIComponent(`isOnlyActiveRequired=1&projectID=${Glob_projectID}`);
                    serviceUrl = `${service_java_URL}projectManagement/getProjectSpecificTools?${encodedDetails}`;
                }
                $.isf.ajax({
                    url: serviceUrl,
                    success: function (data) {
                        data = data.responseData;
                        populateTools(data);
                    }
                });
            }
            else {
                pwIsf.alert({ msg: "Project ID missing", type: "error" });
            }

            function populateTools(data) {
                arrToolList = [];
                $.each(data, function (i, d) {
                    $('#toolSel').append(`<option value="${d.ToolID}">${d.Tool}</option>`);

                });
            }

            function populateScopesLB(data) {
                $('#scopeDetails').empty();
                $('#scopeDetails').append(`<option value="">Select one</option>`);

                $.each(data, function (i, d) {
                    scopeID = d.scopeId;
                    $('#scopeDetails').append(`<option value="${d.projectScopeID}">${d.scopeName}</option>`);

                })
                $('#scopeDetails').select2({ dropdownParent: $("#my_form") });
                $('#serviceArea').select2({ dropdownParent: $("#my_form") });
                $('#domainSel').select2({ dropdownParent: $("#my_form") });
                $('#techSel').select2({ dropdownParent: $("#my_form") });
                $('#toolSel').select2({ dropdownParent: $("#my_form") });
            }

            $(document).off("change").on("change", "#scopeDetails", function (e) {
                scopeID = this.value;
                $.isf.ajax({
                    url: `${service_java_URL}activityMaster/getSubScopebyScopeID?projectScopeID=${scopeID}`,
                    async: false,
                    success: function (data) {
                        var lookup = {};
                        var result = [];
                        if (!data.isValidationFailed) {
                            for (var item, i = 0; item = data.responseData[i++];) {
                                if (!(item.ServiceArea in lookup)) {
                                    lookup[item.ServiceArea] = 1;
                                    result.push(item);
                                }
                            }
                        }
                        populateServiceAreaLB(result);
                    },
                    complete: function (xhr, statusText) {
                        pwIsf.removeLayer();
                    },
                    error: function (xhr, status, statusText) {
                        //BLANK
                    }
                });
            });

            function populateServiceAreaLB(data) {
                $('#serviceArea').empty();
                $('#serviceArea').append('<option value="">Select one</option>');
                $('#domainSel').empty();
                $('#domainSel').append('<option value="">Select one</option>');
                $('#techSel').empty();
                $('#techSel').append('<option value="">Select one</option>');
                $.each(data, function (i, d) {
                    var todayDate = new Date();
                    if (new Date(d.StartDate) > todayDate) {
                        Glob_projectStartDate = new Date(d.StartDate);
                    }
                    else {
                        Glob_projectStartDate = todayDate;
                    }
                    $('#serviceArea').append('<option value="' + d.ServiceAreaID + '">' + d.ServiceArea + '</option>');
                });
            }

            $(document).on("change", "#serviceArea", function (e) {
                servArID = this.value;
                const apiUrl = `${service_java_URL}demandForecast/getDomainSubdomain`;
                var serviceApiUrl = `${apiUrl}?projectScopeID=${scopeID}&serviceAreaID=${servArID}`;
                if (ApiProxy) {
                    const encodeUrlData = encodeURIComponent(`projectScopeID=${scopeID}&serviceAreaID=${servArID}`);
                    serviceApiUrl = `${apiUrl}?${encodeUrlData}`;
                }
                $.isf.ajax({
                    url: serviceApiUrl,
                    async: false,
                    success: function (data) {
                        populateDemandLB(data);
                    },
                    complete: function (xhr, statusText) {
                        pwIsf.removeLayer();
                    },
                    error: function (xhr, status, statusText) {
                        //BLANK
                    }
                });
            });

            function populateDemandLB(data) {
                $.each(data, function (i, d) {
                    $('#domainSel').empty();
                    $('#domainSel').append('<option value="">Select one</option>');
                    $('#techSel').empty();
                    $('#techSel').append('<option value="">Select one</option>');
                    $.each(data, function (i, d) {
                        $('#domainSel').append(`<option value="${d.domainID}">${d.Domain_SubDomain}</option>`);
                    });
                });
            }                       

            $(document).on("change", "#domainSel", function (e) {
                domainID = this.value;
                const apiURL = `${service_java_URL}demandForecast/getTechnologies`;
                var serviceURL = `${apiURL}?projectScopeID=${scopeID}&serviceAreaID=${servArID}&domainID=${domainID}`;
                if (ApiProxy) {
                    const encodedText = encodeURIComponent(`${scopeID}&serviceAreaID=${servArID}&domainID=${domainID}`);
                    serviceURL = `${apiURL}?projectScopeID=${encodedText}`;
                }
                $.isf.ajax({
                    url: serviceURL,
                    async: false,
                    success: function (data) {
                        populateTechnologyLB(data);
                    },
                    complete: function (xhr, statusText) {
                        //BLANK
                    },
                    error: function (xhr, status, statusText) {
                        //BLANK
                    }
                });
            });

            function populateTechnologyLB(data) {
                $('#techSel').empty();
                $('#techSel').append('<option value="">Select one</option>');
                $.each(data, function (i, d) {
                    $('#techSel').append(`<option value="${d.technologyID}">${d.Technology}</option>`);
                })
            }
            return html;
        },
        set_value: function (node, value, task, section) {
            setTimeout(function () { console.log('Call2'); }, 3000);
            if ($('#scopeDetails')) {
                console.log('Call');
            }

            $('#scopeDetails').select2({ dropdownParent: $("#my_form") });
            $('#serviceArea').select2({ dropdownParent: $("#my_form") });
            $('#domainSel').select2({ dropdownParent: $("#my_form") });          
            $('#toolSel').select2({ dropdownParent: $("#my_form") });
            scopeID = task.projectScopeId;
            servArID = task.serviceAreaID;
            domainID = task.domainID;
            toolList = task.toolList;
            $('#scopeDetails').val(task.projectScopeId);
            $('#scopeDetails').trigger('change');
            $('#serviceArea').val(task.serviceAreaID);
            $('#serviceArea').trigger('change');
            $('#domainSel').val(task.domainID);
            $('#domainSel').trigger('change');
            $('#toolSel').val(task.toolList);
            $('#toolSel').trigger('change');

            const arrVendorTech = [];
            if (task.vendorTechModel.length != 0) {
                $.each(task.vendorTechModel, function (i, d) {
                    arrVendorTech.push(d.vendorTech);
                })
            }

            $('#vendorTechSelect').val(arrVendorTech);

            //set the vendor - tech value from DB here
        },
        get_value: function (node, task, section) {
            var ToolArray = [];
            ScopeID = node.childNodes[6].value;
            ScopeName = $(node.childNodes[6]).find("option:selected").text();
            ServArID = node.childNodes[15].value;
            ServArName = $(node.childNodes[15]).find("option:selected").text();
            DomainID = node.childNodes[24].value;
            DomainName = $(node.childNodes[24]).find("option:selected").text();
            ToolArray = $(node.childNodes[45]).val();
            let listOfVendorTech = [];
            listOfVendorTech = split($('#vendorTechSelect').val());
            
            const arrayModelOfvendorTech = [];
            $.each(listOfVendorTech, function (i, d) {
                if (d !== "") {
                    arrayModelOfvendorTech.push(
                        {
                            "vendorTech": d
                        }
                    )
                }
            });
            VendorTechList = arrayModelOfvendorTech;
            task.projectScopeId = ScopeID;
            task.projectScopeName = ScopeName;
            task.serviceAreaID = ServArID;
            task.serviceArea = ServArName;
            task.domainID = DomainID;
            task.domain_subDomain = DomainName;            
            task.vendorTechModel = VendorTechList;
            task.start_date = Glob_projectStartDate;
            task.scopeStartDate = Glob_projectStartDate;
            task.toolList = ToolArray;
        },
        focus: function (node) {
            //BLANK
        }
    };

    ganttDemand.config.lightbox.sections = [
        { name: "template", height: 800, type: "my_seditor", map_to: "text" }
    ];

    ganttDemand.locale.labels.section_template = "";

    ganttDemand.attachEvent("onBeforeLightbox", function (tid) {
        return true;
    });

    ganttDemand.attachEvent("onLightbox", function (tid) {
        setTimeout(function () { return true; }, 5000);
        ganttDemand.keys.edit_save = -1;

    });

    ganttDemand.attachEvent("onLightboxCancel", function (id) {
        ganttDemand.keys.edit_save = 13;

        $('#serviceArea').empty().append('<option value="">Select one</option>');
        $('#techSel').empty().append('<option value="">Select one</option>');
        $('#domainSel').empty().append('<option value="">Select one</option>');
        $('#scopeDetails').val("0");
        $('#serviceArea').val("0");
        $('#techSel').val("0");
        $('#domainSel').val("0");
    })

    ganttDemand.attachEvent("onTaskDblClick", function (id, e) {
        return false;
    });
   
    ganttDemand.attachEvent("onLightboxSave", function (id, task, is_new) {
        ganttDemand.keys.edit_save = 13;

        task.projectScopeId = ScopeID;
        task.projectScopeName = ScopeName;
        task.serviceAreaID = ServArID;
        task.serviceArea = ServArName;
        task.domainID = DomainID;
        task.domain_subDomain = DomainName;        
        task.vendorTechModel = VendorTechList

        const vendorTechListLength = VendorTechList.length;

        if (ScopeID === "" || ServArID === "" || DomainID === "" || VendorTechList === "") {
            pwIsf.alert({ msg: "Please fill all fields!", type: "warning" });
            return false;
        }
        
        var isDuplicateVendorTech = false;
        var valuesSoFar = Object.create(null);
        for (var i = 0; i < vendorTechListLength; ++i) {
            var value = VendorTechList[i].vendorTech;
            if (value in valuesSoFar) {
                isDuplicateVendorTech =  true;
            }
            valuesSoFar[value] = true;
        }

            

        if (isDuplicateVendorTech) {
            pwIsf.alert({
                msg: "Duplicate Vendor-Tech values are not allowed.", type: "warning" });
            return false;
        }
        else if (vendorTechListLength > 10) {
            pwIsf.alert({ msg: "Only 10 Vendor-Tech allowed", type: "warning" });
            return false;
        }
        else {            
            ganttDemand.updateTask(id);
            return true;
        }
    })

    ganttDemand.attachEvent("onEmptyClick", function (e) {
        ganttDemand.ext.inlineEditors.save()
    });

    ganttDemand.attachEvent("onBeforeGanttRender", function () {
        $(".gantt_grid_data").on('scroll', function () {
            $(".gantt_data_area").scrollTop($(this).scrollTop());
        });
        $(".gantt_data_area").on('scroll', function () {
            $(".gantt_grid_data").scrollTop($(this).scrollTop());
        });
      

    });
    
    ganttDemand.init("gantt_here_demand");
   // ganttDemand.keys.edit_save = -1;
    var inlineEditors = ganttDemand.ext.inlineEditors;

    inlineEditors.attachEvent("onBeforeSave", function (state) {

        if (state.columnName === "fte") {
            var task = ganttDemand.getTask(state.id);
            var fteValue = state.newValue;
            var duration = (task.duration) + 8;
            var hours = (fteValue / 100) * duration;
            task.hours = hours;
            return true;
        }
        if (state.columnName === "start_date") {
            var todayDate = new Date();
            var taskScopeStartDate = new Date(ganttDemand.getTask(state.id).scopeStartDate);
            if (taskScopeStartDate > todayDate) {
                taskScopeStartDate = new Date(ganttDemand.getTask(state.id).scopeStartDate);
            }
            else {
                taskScopeStartDate = todayDate;
            }
            if (taskScopeStartDate.toDateString() === state.newValue.toDateString()) {
                return true;
            }
            else if (state.newValue < taskScopeStartDate) {
                pwIsf.alert({ msg: "Start Date Cannot be less than: " + taskScopeStartDate.toDateString() + " (Today's Date/Future Deliverable Start Date)!", type: "warning" });
                return false;
            }
            else {
                return true;
            }
        }
        if (state.columnName === 'remoteCount' || state.columnName === 'onsiteCount' || state.columnName === 'fte') {
            if (state.newValue < 0 || state.newValue > 100 || state.newValue % 1 != 0) {
                pwIsf.alert({ msg: "Count Cannot be negative/Cannot exceed 100/Cannot be a decimal", type: "warning" });
                return false;
            }
            else {
                return true;
            }
        }
    });

    var initial_start_date, initial_end_date;

    inlineEditors.attachEvent("onBeforeEditStart", function (state) {
        var date_type = state.columnName;
        if (date_type === "start_date") {
            initial_end_date = ganttDemand.getTask(state.id).end_date;
        }
        if (date_type === "end_date") {
            initial_start_date = ganttDemand.getTask(state.id).start_date;
        }
        ganttDemand.render();
    });

    inlineEditors.attachEvent("onSave", function (state) {
        var date_type = state.columnName;

        if (date_type === "start_date") {
            ganttDemand.getTask(state.id).end_date = initial_end_date;
        }
        if (date_type === "end_date") {
            ganttDemand.getTask(state.id).start_date = initial_start_date;
        }
        ganttDemand.updateTask(state.id)
        ganttDemand.render();

    });

    pwIsf.removeLayer();
}

/*---------------Save Resource Details 2nd View Resources-----------------------*/
$(document).on('click', '.saveDraftDetails', function (event) {
    var operation = "saveasdraft";
    spmOperations(operation);
})

/*---------------Send Back to RPM 2nd View Resources-----------------------*/
$(document).on('click', '.sendBackRPM', function (event) {
    var operation = "sendbacktorpm";
    spmOperations(operation);
})

/*---------------Send to NExt Level SPM 2nd View Resources-----------------------*/
$(document).on('click', '.sendNextDetails', function (event) {
    var operation = "";
    if (Glob_logedInUserRole === "Resource Planning Manager") {
        operation = "sendtospm";
        $(".sendNextDetails ").addClass("disabledbutton");
        $(".saveDraftDetails ").addClass("disabledbutton");
    }
    else {
        operation = "sendtofm";
    }
    spmOperations(operation);
})

/*-------------On Save and Send Functions-----------------*/
function spmOperations(operation) {
    var tasksArray = ganttDemand.getTaskByTime();
    var task;   

    $.each(tasksArray, function (i, d) {
        var tid = tasksArray[i].id;
        task = ganttDemand.getTask(tid);
        ganttDemand.updateTask(tid);
        const listVendorTech = $(`#selectedVendorTech_${tid}`).val();
        const arrVendorTechnology = listVendorTech.split(",");
        var vendorTechModel = [];
        $.each(arrVendorTechnology, function (j, v) {
            const separatedVendorTechName = v.split("-");
            const vendorName = separatedVendorTechName[0];
            const techName = separatedVendorTechName[1];
            vendorTechModel.push({
                "vendor": vendorName,
                "technology": techName
            });
        })
        tasksArray[i].vendorTechModel = vendorTechModel;
    });        
    if ((task.onsiteCount + task.remoteCount) > 0) {
        var resourceDetailsObj = new Object();
        resourceDetailsObj.role = Glob_logedInUserRole;
        resourceDetailsObj.operation = operation;
        resourceDetailsObj.signum = signumGlobal;
        resourceDetailsObj.projectid = Glob_projectID;
        resourceDetailsObj.positionData = tasksArray;
        var JsonObj = JSON.stringify(resourceDetailsObj);
        pwIsf.addLayer({ text: C_PLEASE_WAIT });
            $.isf.ajax({
                type: C_API_POST_REQUEST_TYPE,
                contentType: C_CONTENT_TYPE_APPLICATION_JSON,
                url: `${service_java_URL}demandForecast/saveDemandForecastDetails`,
                data: JsonObj,
                success: resourcesSaved,
                error: function (msg) {
                    pwIsf.alert({ msg: data.msg, type: C_WARNING });
                },
                complete: function (msg) {
                    pwIsf.removeLayer();
                }
            });
        function resourcesSaved(data) {
            if (data.isValidationFailed) {
                errorHandler(data);

            }
            else {
                if (data.formWarningCount > 0) {
                   
                    var str = "";
                    for (var i = 0; i < Object.keys(data.formWarnings).length; i++) {
                        var sno = i + 1;
                        str += sno + ". "+ data.formWarnings[i]+ "<br>";
                        
                    }
                    pwIsf.alert({ msg: str, type: 'warning' });
                } else {
                    getMsgByOperation(operation);
                }
                
                getDemandForecastSummary(new Date(), 6);
                getGantt(Glob_projectID);
            }
            
        }
    }
    else {
        pwIsf.alert({ msg: "Head Count Sum Cannot be 0!", type: "warning" });
        getGantt(Glob_projectID);
    }
}

/*-------------On Delete of Gantt Task-----------------*/
function deleteTask(operation, deletedTaskArray, id) {
    var resourceDetailsArr = [];
    var resourceDetailsObj = new Object();
    resourceDetailsObj.role = Glob_logedInUserRole;
    resourceDetailsObj.operation = operation;
    resourceDetailsObj.signum = signumGlobal;
    resourceDetailsObj.projectid = Glob_projectID;
    resourceDetailsObj.positionData = deletedTaskArray;
    var JsonObj = JSON.stringify(resourceDetailsObj);
    pwIsf.addLayer({ text: C_PLEASE_WAIT });
    $.isf.ajax({
        type: "POST",
        contentType: C_CONTENT_TYPE_APPLICATION_JSON,
        url: `${service_java_URL}demandForecast/saveDemandForecastDetails`,
        data: JsonObj,
        success: resourcesDeleted,
        error: function (msg) {
            //BLANK
        },
        complete: function (msg) {
            pwIsf.removeLayer();
        }
    });
    getGantt(Glob_projectID);
}

/*----------On Delete Task-----------------*/
function resourcesDeleted(data) {
    if (data.isValidationFailed) {
        errorHandler(data);
    }
    else {
        getDemandForecastSummary(new Date(), 6);
        CloseDetailsView();
        getGantt(Glob_projectID);
        pwIsf.removeLayer();
    }
}

function getMsgByOperation(operation) {
    if (operation === "saveasdraft") {
        pwIsf.alert({ msg: 'Draft Saved Successfully', type: 'success' });
    }
    else if (operation === "sendtospm") {
        pwIsf.alert({ msg: 'Details Successfully Sent to SPM', type: 'success' });
    }
    else if (operation === "sendbacktorpm") {
        pwIsf.alert({ msg: 'Details Successfully Saved', type: 'success' });
    }
    else if (operation === "sendtofm") {
        CloseDetailsView();
    }
}


function renderDefaultHTMLForDemand() {
    let rendHtml = '';
    rendHtml = `${rendHtml}<div id="my_form" style="padding-left:25px;"><br/>
                <label><b>Deliverables</b></label>
                <div style="width: 20px;  margin-left: -8px; margin-top: -25px; font-size: 17px; color:red;">*</div>
                <select name="description" class="select2able select2-offscreen" id="scopeDetails">
                <option value="0">Select one</option>
                </select><br/><br/>               
                <label><b>ServiceArea/SubServiceArea</b></label>
                <div style="width: 20px;  margin-left: -8px; margin-top: -25px; font-size: 17px; color:red;">*</div>
                <select name="description" class="select2able select2-offscreen" id="serviceArea">
                <option value="0">Select one</option>
                </select><br/><br/>
                <label><b>Domain/SubDomain</b></label>
                <div style="width: 20px;  margin-left: -8px; margin-top: -25px; font-size: 17px; color:red;">*</div>
                <select name="description" class="select2able select2-offscreen" id="domainSel">
                <option value="0">Select one</option>
                </select>
                <br/><br/>
                <label><b>Vendor-Tech</b></label>&nbsp;
                <i class="fa fa-info-circle" style="font-size:13px;cursor:pointer;"
                title="Please Enter 3 Characters of Vendor/Technology
to get the List of Combinations.
This can be repeated to select multiple values.">
                </i>
                <div style="width: 20px;  margin-left: -8px; margin-top: -25px; font-size: 17px; color:red;">*</div>
                <input type="text" style="height:32px;width:90%;border:1px solid #aaa;border-radius:4px;"
                oninput="setStartAndEnd()" id="vendorTechSelect">
                <input type="hidden" id="rrid"/>
                <br/><br/>
                <div><label><b>Tools</b></label></div>
                <select name="description" class="select2able select2-offscreen" id="toolSel" multiple></select>
                </div>`;
    return rendHtml;
}

// call three letter search API for Vendor-Tech when data input
function setStartAndEnd() {

    threeLetterVendorTechSearchAPI(searchStartIndex, searchEndIndex, true);
}

// 4 letter search API autoSuggestAfterFourLettersForNE
function threeLetterVendorTechSearchAPI(searchStart,searchEnd,isLazyLoaded) {
    $(C_VENDOR_TECH_BOX_ID).autocomplete({
        source: function (request, response) {
            var searchText = extractLast(request.term);
            if (searchText === ',' || searchText === " " || searchText.length <= 2 || searchText === "") {
                console.log(searchText);
                $(C_VENDOR_TECH_BOX_ID).removeClass('ui-autocomplete-loading');
            }
            else if (searchText.length >= 3) {
                $.isf.ajax({
                    url: `${service_java_URL}demandForecast/getVendorTechCombination?start=${searchStart}&length=${searchEnd}&term=${searchText}`,
                    contentType: C_CONTENT_TYPE_APPLICATION_JSON,
                    type: C_API_GET_REQUEST_TYPE,
                    success: function (data) {
                        $(C_VENDOR_TECH_BOX_ID).removeClass('ui-autocomplete-loading');
                        var result = [];
                        $.each(data.responseData, function (i, d) {
                            result[i] = `${d.vendorTech}`;
                        })
                        response($.each(data.responseData, function (i,d) {
                            result[i] =  {
                                label: d.vendorTech,
                                value: d.vendorTechID
                            }
                        }));
                        response($.ui.autocomplete.filter(result, extractLast(request.term)));
                    }
                });
            }
        },
        minLength: 3,
        close: true,
        select: function (event, ui) {
            var terms = split(this.value);
            terms.pop();
            terms.push(ui.item.label);
            terms.push("");
            this.value = terms.join(', ');
            return false;
        },
        focus: function (event, ui) {
            event.preventDefault();
        }
    });
    $(C_VENDOR_TECH_BOX_ID).autocomplete("widget").addClass("fixedHeight");

}

//extract the last searched item
function extractLast(term) {
    return split(term).pop();
}

function split(val) {
    return val.split(/,\s*/);
}

function showMultiVendorTechView(taskId) {
    let multiVendorTechTablehtml = '';
    $("#tblMultipleVenderTech").empty();
    const multiVendorTechVal = split($(`#selectedVendorTech_${taskId}`).val());
    if (multiVendorTechVal !== null) {
        $.each(multiVendorTechVal, function (i, d) {
            multiVendorTechTablehtml = `${multiVendorTechTablehtml}<tr><td>${d}</td></tr>`;
        })
    }
    else {
        multiVendorTechTablehtml = `${multiVendorTechTablehtml}<tr><td>Error</td></tr>`
    }
   $('#tblMultipleVenderTech').append(multiVendorTechTablehtml);
   $('#multiVendorTechView').modal('show');
}

//close vendor tech combinations modal
function closeVendorTechModal() {
    $('#multiVendorTechView').modal('hide');
}


