function getAllProjectDetails() {
    $("#combo_project").html('');
    pwIsf.addLayer({ text: "Please wait ..." });
    $("#combo_project").append("<option value='' selected disabled>Select Project</option>");
    $.isf.ajax({
        url: service_java_URL + "flowchartController/searchWFAvailabilityforScope/all/all/all/all/all/all/all/all",
        success: function (data) {
            allProjectDetails = data;
            //console.log(data);
            pwIsf.removeLayer();
            var projects = [];

            $.each(data, function (key, value) {
                if (projects.indexOf(value.projectID) == -1) {
                    $("#combo_project").append("<option value=" + value.projectID + ">" + value.projectID + "_" + value.projectName + "</option>");
                    projects.push(value.projectID);
                }
            });
        },
        complete: function () {
            $("#combo_project").select2();
        }
    });
}

function projectChange() {
    var selectedproj = $('#combo_project option:selected').val();
    $("#combo_subact").html('');
    $("#combo_subact").append("<option value='' selected disabled>Select SubActivity</option>");
    tblProjectDetails = [];
    for (var k in allProjectDetails) {
        if (allProjectDetails[k].projectID == selectedproj) {
            // console.log(k)Domain SubDomain Technology SubActivity
            $("#combo_subact").append("<option value=" + allProjectDetails[k].subActivity + ">" + allProjectDetails[k].domain + "/" + allProjectDetails[k].subDomain + "/" + allProjectDetails[k].technology + "/" + "/" + allProjectDetails[k].activity + "/" + "/" + allProjectDetails[k].subActivity + "/" + allProjectDetails[k].subActivity + "</option>");
            tblProjectDetails.push(allProjectDetails[k])
        }
    }
}

function subactChange() {
    $("#combo_subact").select2();
    var selectedproj = $('#combo_project option:selected').val();
    var selectedSubact = $('#combo_subact option:selected').val();
    tblProjectDetails = [];
    for (var i = 0; i < allProjectDetails.length; i++) {
        if ((allProjectDetails[i].projectID == selectedproj) && allProjectDetails[i].subActivityID == selectedSubact)
            tblProjectDetails.push(allProjectDetails[i])
    }
}

function populateTable() {
    if (($('#combo_project option:selected').val() == '')) {
        pwIsf.alert({ msg: "Please select a Project", type: "warning" });
    }
    else {
        pwIsf.addLayer({ text: "Please wait ..." });
        var projID = $('#combo_project option:selected').val();
        var subactName = $('#combo_subact option:selected').val();
        var workOrderInfo = '';
        $.isf.ajax({
            //url: "https://isfdevservices.internal.ericsson.com:8443/isf-rest-server-java_Laptop/woExecution/getEfficiencyDevlieryIndexForUser?projectID=" + projID + "&subActivity=" + subactName + "&flowChartDefID=0&woID=0&markAsComplete=true&signumID="+signumGlobal,
            url: "https://isfdevservices.internal.ericsson.com:8443/isf-rest-server-java_dev_major/woExecution/getEfficiencyDevlieryIndexForUser?projectID=1&subActivity=Network Audit&flowChartDefID=0&woID=0&markAsComplete=true&signumID=EADSIDD",
            //url: "https://isfdevservices.internal.ericsson.com:8443/isf-rest-server-java_dev_major/woExecution/getEfficiencyDevlieryPerformance?projectID=1&subActivity=Network Audit&flowChartDefID=0&woID=0&markAsComplete=true&signumID=EADSIDD",
            context: this,
            crossdomain: true,
            processData: true,
            contentType: 'application/json',
            type: 'GET',
            xhrFields: {
                withCredentials: false
            },
            success: function (data) {
                pwIsf.removeLayer();
                $("#tableDI tr").remove();
                workOrderInfo += '<tr><th>Name</th><th>EfficiencyIndex</th><th>FTR</th><th>SLA</th><th>DeliveryIndex</th></tr>';
                //data.AfterExecution = [{ EmployeeName: "Adil Siddiqi", EFiciencyIndex: 115.92, FTR: 100, SLA: 0, DeliveryIndex: 0 }, { EmployeeName: "Shambhavi", EFiciencyIndex: 115.92, FTR: 100, SLA: 0, DeliveryIndex: 0 }, { EmployeeName: "Meenu", EFiciencyIndex: 115.92, FTR: 100, SLA: 0, DeliveryIndex: 0 }]
                if (data.AfterExecution.length == 0) {
                    workOrderInfo += '<tr><td colspan="5" style="text-align:center;"><div class="col-md-5">No Data Available</div></td></tr>';
                    $("#tableDI").append(workOrderInfo);
                }
                else {
                    $.each(data.AfterExecution, function (i, d) {
                        workOrderInfo += '<tr><td><div class="col-md-3">' + d.EmployeeName + '</div></td>';
                        workOrderInfo += '<td><div class="col-md-3"><div id="gaugeContainerF_PER' + i + '" style="min-width: 100px; max-width: 250px; width:200px; height: 200px; margin: 0 auto"></div></div></td>';
                        workOrderInfo += '<td><div class="col-md-3"><div id="gaugeContainerG_PER' + i + '" style="min-width: 100px; max-width: 250px; width:200px; height: 200px; margin: 0 auto"></div></div></td>';
                        workOrderInfo += '<td><div class="col-md-3"><div id="gaugeContainerH_PER' + i + '" style="min-width: 100px; max-width: 250px; width:200px; height: 200px; margin: 0 auto"></div></div></td>';
                        workOrderInfo += '<td><div class="col-md-3"><div id="gaugeContainerE_PER' + i + '" style="min-width: 100px; max-width: 250px; width:200px; height: 200px; margin: 0 auto"></div></div></td></tr>';
                    });
                    $("#tableDI").append(workOrderInfo);
                    $.each(data.AfterExecution, function (i, d) {
                        createGauge('gaugeContainerF_PER' + i, 'Efficiency Index', [{ name: 'F-PER', data: [d.EFiciencyIndex] }]);
                        createGauge('gaugeContainerG_PER' + i, 'FTR', [{ name: 'G-PER', data: [d.FTR] }]);
                        createGauge('gaugeContainerH_PER' + i, 'SLA', [{ name: 'H-PER', data: [d.SLA] }]);
                        createGauge('gaugeContainerE_PER' + i, 'Delivery Index', [{ name: 'E-PER', data: [d.DeliveryIndex] }]);
                    });
                }
                
            },
            error: function (xhr, status, statusText) {
                console.log('An error occurred');
            }
        });
    }
}

