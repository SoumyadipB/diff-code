function NotifyFloatingWindowForAutoSenseRules(data, AutoSenseDetails) {
    var AutoScannerInputData = {};

    for (var i in autoSesnseDetails) {
        if (autoSesnseDetails[i].woID == data.woId) {
            if (AutoSenseDetails[i].NextSteps[0].isStepAutoSenseEnabled === true && AutoSenseDetails[i].NextSteps[0].NextExecutionType === "Manual") {
                AutoScannerInputData.WoId = data.WoId;
                AutoScannerInputData.FlowChartDefID = AutoSenseDetails[i].FlowChartDefID;
                AutoScannerInputData.stepID = AutoSenseDetails[i].NextSteps[0].NextStepID;
                AutoScannerInputData.source = "UI";
                workOrderData.signumID = signumGlobal; //Manual
                nextExecutionType = AutoSenseDetails[i].NextSteps[0].NextExecutionType;
            }
        }
    }

    if (AutoScannerInputData.length > 0) {
        try {
            connection.invoke('AutoSenseEnabledWorkOrderDetails', AutoScannerInputData);
        }
        catch (err) {

            console.log("error invoking AutoSenseEnabledWorkOrderDetails " + err);
        }
        //serverpushhubproxy.server.AutoSenseEnabledWorkOrderDetails(AutoScannerInputData).done(function () {
        //    console.log('Successfully called ');
        //}).fail(function (error) {
        //    console.log('Invocation of AutoSenseEnabledWorkOrderDetails. Error: ' + error);
        //});
    }

}