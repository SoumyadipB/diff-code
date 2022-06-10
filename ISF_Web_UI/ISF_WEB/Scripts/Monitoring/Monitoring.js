$(document).ready(function () {
	setInterval(function () {
		reloadReports();
    }, 900000)
});

function openInNewTab(url) {
    var win = window.open(url, '_blank');
    win.focus();
}

function reloadReports() {
	reloadSQL();
	reloadAPI();    
}

function reloadSQL() {
	let html1 = "<div id='sqlReport'><script type='text/javascript' src='https://tbsrvp.internal.ericsson.com/javascripts/api/viz_v1.js'><" + "/script>" + "<div class='tableauPlaceholder' style='width: 1604px; height: 845px;'><object class='tableauViz' width='1604' height='845' style='display:none;'><param name='host_url' value='https%3A%2F%2Ftbsrvp.internal.ericsson.com%2F' /> <param name='embed_code_version' value='3' /> <param name='site_root' value='&#47;t&#47;ISFAnalytics' /><param name='name' value='SQLMonitoringReport&#47;StatusDashboard' /><param name='tabs' value='no' /><param name='toolbar' value='yes' /><param name='showAppBanner' value='false' /><param name='filter' value='iframeSizedToWindow=true' /><param name='refresh' value='yes' /></object></div></div>"
	$("#sqlReport").remove();
	$("#Tableau_ReportView").append(html1);
}

function reloadAPI() {
    let html2 = "<div id='apiMonitoring'><" + "script type='text/javascript' src='https://tbsrvp.internal.ericsson.com/javascripts/api/viz_v1.js'></script><div class='tableauPlaceholder' style='width: 1280px; height: 514px;'><object class='tableauViz' width='1280' height='514' style='display:none;'><param name='host_url' value='https%3A%2F%2Ftbsrvp.internal.ericsson.com%2F' /> <param name='embed_code_version' value='3' /> <param name='site_root' value='&#47;t&#47;ISFAnalytics' /><param name='name' value='APIMonitoring&#47;APIMonitoringReport' /><param name='tabs' value='no' /><param name='toolbar' value='yes' /><param name='showAppBanner' value='false' /><param name='filter' value='iframeSizedToWindow=true' /></object></div></div>"
	$("#apiMonitoring").remove();
	$("#Tableau_ReportView2").append(html2);
}

function reloadLogs() {
    let html3 = "<div id='logsReport'><" + "script type='text/javascript' src='https://tbsrvp.internal.ericsson.com/javascripts/api/viz_v1.js'></script><div class='tableauPlaceholder' style='width: 1280px; height: 514px;'><object class='tableauViz' width='1280' height='514' style='display:none;'><param name='host_url' value='https%3A%2F%2Ftbsrvp.internal.ericsson.com%2F' /> <param name='embed_code_version' value='3' /> <param name='site_root' value='&#47;t&#47;ISFAnalytics' /><param name='name' value='IISLogs&#47;IISLogs' /><param name='tabs' value='no' /><param name='toolbar' value='yes' /><param name='showAppBanner' value='false' /><param name='filter' value='iframeSizedToWindow=true' /></object></div></div>"
    $("#logsReport").remove();
    $("#Tableau_ReportView3").append(html3);
}

function reloadLogsTrend() {
    let html4 = "<div id='logsReportTrend'><" + "script type='text/javascript' src='https://tbsrvp.internal.ericsson.com/javascripts/api/viz_v1.js'></script><div class='tableauPlaceholder' style='width: 1366px; height: 795px;'><object class='tableauViz' width='1366' height='795' style='display:none;'><param name='host_url' value='https%3A%2F%2Ftbsrvp.internal.ericsson.com%2F' /> <param name='embed_code_version' value='3' /> <param name='site_root' value='&#47;t&#47;ISFAnalytics' /><param name='name' value='IISLogs-TrendChart&#47;IISTrendChart' /><param name='tabs' value='no' /><param name='toolbar' value='yes' /><param name='showAppBanner' value='false' /><param name='filter' value='iframeSizedToWindow=true' /></object></div></div>"
    $("#logsReportTrend").remove();
    $("#Tableau_ReportView4").append(html4);
}