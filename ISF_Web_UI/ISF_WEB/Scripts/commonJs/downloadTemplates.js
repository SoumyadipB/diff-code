function downloadTemplateFile(jsonObj) {

    let fileName = jsonObj.fileName;
    let url = null;

    url = `${service_java_URL_VM}/rpaController/downloadTemplateFile?templateName=${fileName}`;

    let fileDownloadUrl;

    fileDownloadUrl = UiRootDir+"/data/GetFileFromApi?apiUrl=" + url;
        
    window.location.href = fileDownloadUrl;

}


function downloadTemplateFileWorkinstruction(jsonObj) {

    let wiid = jsonObj.wiid;
    
    let url = `${service_java_URL}activityMaster/downloadWorkInstructionFile?wIID=${wiid}`;
    let fileDownloadUrl;

    fileDownloadUrl = UiRootDir+"/data/GetFileFromApi?apiUrl=" + url;

    window.location.href = fileDownloadUrl;

}

function downloadDOViewData(jsonObj) {
    let projectid = jsonObj.projectid;
    let startDate = jsonObj.startDate;
    let endDate = jsonObj.endDate;
    let status = jsonObj.status;
    if (status == '' || status == undefined || status == null) {
        status = 'All';
    }
    //activityMaster/downloadWoViewFile?projectID=11371&startDate=1970-01-01&endDate=2020-02-03&status=All 
    //let endDate = jsonObj.endDate;
    var urlSearch = "projectID=" + projectid + "&startDate=" + startDate + "&endDate=" + endDate + "&status=All";
    let urlEncode = encodeURIComponent(urlSearch);
    let serviceUrl = `${service_java_URL}activityMaster/downloadDoPlanViewFile?${urlEncode}`;

    //let url = `${service_java_URL}activityMaster/downloadWoViewFile?projectID=${projectid}&startDate=${startDate}&endDate=${endDate}&status=All`;
    //let url = `http://100.96.35.120:8080/isf-rest-server-java/activityMaster/downloadWorkInstructionFile?wIID=${wiid}`;
    
    let fileDownloadUrl;

    fileDownloadUrl = UiRootDir + "/data/GetFileFromApi?apiUrl=" + serviceUrl;

    window.location.href = fileDownloadUrl;
}


function downloadWOViewData(jsonObj) {

    let projectid = jsonObj.projectid;
    let startDate = jsonObj.startDate;
    let endDate = jsonObj.endDate;
    let status = jsonObj.status;
    if (status == '' || status == undefined || status == null) {
        status = 'All';
    }
    //activityMaster/downloadWoViewFile?projectID=11371&startDate=1970-01-01&endDate=2020-02-03&status=All 
    //let endDate = jsonObj.endDate;
    var urlSearch = "projectID=" + projectid + "&startDate=" + startDate + "&endDate=" + endDate + "&status=All";
    let urlEncode = encodeURIComponent(urlSearch);
    let serviceUrl = `${service_java_URL}activityMaster/downloadWoViewFile?${urlEncode}`;

    //let url = `${service_java_URL}activityMaster/downloadWoViewFile?projectID=${projectid}&startDate=${startDate}&endDate=${endDate}&status=All`;
    //let url = `http://100.96.35.120:8080/isf-rest-server-java/activityMaster/downloadWorkInstructionFile?wIID=${wiid}`;
    //clToText(url);
    let fileDownloadUrl;

    fileDownloadUrl = UiRootDir + "/data/GetFileFromApi?apiUrl=" + serviceUrl;

    window.location.href = fileDownloadUrl;

}


function downloadTemplateNetworkElementData(jsonObj) {

    let projectid = jsonObj.projectid;

    //http://localhost:8080/isf-rest-server-java//activityMaster/downloadWorkInstructionFile?wIID=44 

    //older api -> let url = `${service_java_URL_VM}activityMaster/downloadNetworkElement/${projectid}`;

    let downloadNEDetailsUrl = `${service_java_URL_VM}networkElement/downloadNetworkElementv1/${projectid}`;

    let fileDownloadUrl;

    fileDownloadUrl = UiRootDir + "/data/GetFileFromApi?apiUrl=" + downloadNEDetailsUrl;

    window.location.href = fileDownloadUrl;

}
function downloadWFTableFile(jsonObj){
    let defid = jsonObj.flowchartdefId;

    //http://localhost:8080/isf-rest-server-java//activityMaster/downloadWorkInstructionFile?wIID=44 

    let url = `${service_java_URL}/test/generateNewExcelFromJson/${defid}`;

    let fileDownloadUrl;

    fileDownloadUrl = UiRootDir +"/data/GetFileFromApi?apiUrl=" + url;

    window.location.href = fileDownloadUrl;
}


/*--------Download: MSS Leave Template------------*/
function downloadMSSLeaveTemplate(jsonObj) {
    let fileName = jsonObj.fileName;
    let url = `${service_java_URL}rpaController/downloadTemplateFileCsv?templateName=${fileName}`;
    let fileDownloadUrl;
    fileDownloadUrl = UiRootDir + "/data/GetFileFromApi?apiUrl=" + url;
    window.location.href = fileDownloadUrl;
}

// download reference mapping file for NE
function downloadNERefMappingFile() {

    let url = `${service_java_URL_VM}/networkElement/downloadReferenceMappingFile`;

    let fileDownloadUrl;

    fileDownloadUrl = UiRootDir + "/data/GetFileFromApi?apiUrl=" + url;

    window.location.href = fileDownloadUrl;
}