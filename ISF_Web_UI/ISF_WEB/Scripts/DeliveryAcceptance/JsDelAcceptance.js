$(document).ready(function () {
    getAllProjects();
    $('#moveButtonWO').hide();
    $('#AcceptbuttonWO').hide();
    $('#RejectbuttonWO').hide();
    $('#RejectbuttonWO1').hide();
});

$(document).mouseup(function (e) {
    var container = $(".CellComment");

    // if the target of the click isn't the container nor a descendant of the container
    if (!container.is(e.target) && container.has(e.target).length === 0) {
        container.hide();
    }
});

var acceptRejectWoFlag = false;

var multipleWO = [];

var multipleSubActivityID = [];
var multiplewfID = [];

function getRatings() {
    var ServiceUrl = service_java_URL + "woDeliveryAcceptance/getAcceptanceRatings";


    $("#contentRatings tr").remove();
    $.isf.ajax({
        url: ServiceUrl,
        success: function (data) {
            var html = "";
            $.each(data, function (i, d) {

                html = html + "<input name='optionsRadios1' type='radio' value='1'>";
            })
            $("#contentRatings").append(html);

        },
        error: function (xhr, status, statusText) {
            console.log('An error occurred');
        }
    });
}

function getAllProjects() {

    var signumId = signumGlobal;
    $.isf.ajax({

        url: service_java_URL + "projectManagement/getProjectAcceptance/" + signumId,

        success: function (data) {
            if (data.responseData != null && data.isValidationFailed == false)  {
                $.each(data.responseData, function (i, d) {
                    $('#sel_project_name').append('<option value="' + d.ProjectID + '">' + d.ProjectID + '-' + d.ProjectName + '</option>');
                    $('#sel_project_name_wo').append('<option value="' + d.ProjectID + '">' + d.ProjectID + '-' + d.ProjectName + '</option>');
                });
            }
            else {
                pwIsf.alert({ msg: data.formErrors[0], type: 'error' });
            }
        },
        error: function (xhr, status, statusText) {
            console.log('An error occurred on getAllProjects: ' + xhr.error);
        }
    });
}

//Delivery Acceptance Search Project
function SearchProject() {

    pwIsf.addLayer({ text: 'Please wait ...' });
    var project = document.getElementById("sel_project_name").value.split('-');
    var projectName = project;

    var ServiceUrl = service_java_URL + "woDeliveryAcceptance/getclosedWODetails/" + projectName + "/all/" + signumGlobal;

    var table = $('#tblProjects').DataTable({
        //"searching": true,
        //processing: true,
        responsive: true,
        destroy: true,
        colReorder: true,
        dom: 'Bfrtip',
        buttons: [{
            extend: 'colvis',
            postfixButtons: ['colvisRestore'],
            columns: ':gt(0)'
        },
            'excel'],
    });

    //$('#tblProjects').dataTable({
    //    "searching": true
    //}); 

    $('#tblProjects tfoot th').each(function (i) {
        var title = $('#tblProjects thead th').eq($(this).index()).text();
        if (title != "Actions" && title != "")
            $(this).html('<input type="text" class="form-control" style="font-size:12px;"  placeholder="Search ' + title + '" data-index="' + i + '" />');
    });

    // Apply the search
    //table.columns().every(function () {
    //    var that = this;

    //    $('input', this.footer()).on('keyup change', function () {
    //        if (that.search() !== this.value) {
    //            that
    //                .columns($(this).parent().index() + ':visible')
    //                .search(this.value)
    //                .draw();
    //        }
    //    });
    //});

    //table.on('column-reorder', function (e, settings, details) {
    //    //Apply the search
    //    table.columns().every(function () {
    //        var that = this;

    //        $('input', this.footer()).on('keyup change', function () {
    //            if (that.search() !== this.value) {
    //                that
    //                    .columns($(this).parent().index() + ':visible')
    //                    .search(this.value)
    //                    .draw();
    //            }
    //        });
    //    });
    //});
    $("#ckbCheckAllDA").click(function (e) {
        // e.preventDefault();
        $(".checkBoxClassDA").prop('checked', $(this).prop('checked'));
    });

    $(".checkBoxClassWO").on('change', function (e) {
        // e.preventDefault();
        if (!$(this).prop("checked")) {
            $("#ckbCheckAllWO").prop("checked", false);
        }
    });
    // table.clear();
    //if ($.fn.dataTable.isDataTable('#tblProjects')) {
    //    table.destroy();
    //    $("#tblProjects").html('');
    //}
    $.isf.ajax({
        url: ServiceUrl,

        success: function (data) {
            //pwIsf.removeLayer();
            ConvertTimeZones(data, getclosedWODetails_tzColumns);
            $('#moveButtonWO').show();
            $('#AcceptbuttonWO').show();
            $('#RejectbuttonWO').show();
            $('.checkBoxClassDA').prop('checked', false);

            table.clear();

            $.each(data, function (i, d) {
                
                var wName = (d.wOName.replace(/\"/g, '&quot;')).replace(/'/g, "\\'");
                // getting proficiency level of work flow.
                let profLevel = getProfLevel(d.proficiencyName);
                table.row.add(['<span><label><input id="approveWO" type="checkbox" name="approveWO" class="checkBoxClassDA" value="' + d.wOID + '-\'' + wName + '\'\"' + '" data-subactivityid="' + d.activityDetails[0].subActivityID + '" data-projectid="' + d.projectID + '" data-wfid="' + d.wfID + '" data-woid="' + d.wOID + '" data-signum="' + d.signum + '"/></label></span>', '<div style="display:flex;">&thinsp;&thinsp;&thinsp;' + '<a title="View Work Flowchart" href="#" onclick="flowChartOpenInNewWindow(' + ' \'DeliveryExecution\/FlowChart\?mode\=view&woID=' + d.wOID + '\&proficiencyId=' + d.proficiencyID + '\&proficiencyLevel=' + profLevel+ '\' ' + ')"><i style="cursor: pointer;" class="fa fa-sitemap"></i></a>&nbsp;&nbsp;&nbsp;' +
                    '<span onclick="openViewWorkOrder()"><i title="View Work Order" style="cursor: pointer;" data-toggle="modal" class="fa fa-file-o viewWorkOrder" data-workorder-id="' + d.wOID + '" data-edit-work-order-flag="false" data-doid="' + d.doid + '" data-woplan-id="' + d.woplanid + '" ></i></span>&thinsp;&thinsp;&thinsp;' +
                    '|&thinsp;&thinsp;&thinsp;<i title="Accept" style="cursor: pointer;" data-toggle="modal" data-target="#modal_accept" class="fa fa-check-square" onclick="sendShowDataAccept(' + d.wOID + ',\'' + wName + '\')"></i>&thinsp;&thinsp;&thinsp;' +
                    '<i title="Reject" style="cursor: pointer;" data-toggle="modal" data-target="#modal_reject" class="fa fa-reply-all" onclick="sendShowDataReject(' + d.wOID + ',\'' + wName + '\'' + ',' + d.activityDetails[0].subActivityID + ',' + d.projectID + ',' + d.wfID + ',\'' + d.signum + '\'' + ')" ></i>&thinsp;&thinsp;&thinsp;' +
                    '<i title="Move To" style="cursor: pointer;" data-toggle="modal" data-target="#modal_moveTo" class="fa fa-exchange" onclick="sendShowDataMoveTo(' + d.wOID + ',\'' + wName + '\')"></i>&thinsp;&thinsp;&thinsp;' +

                    '<i title="Show Output_Links" style=" z-index: 2; cursor: pointer;" class="fa fa-shield CellWithComment" onclick="ShowOutputLinks(' + d.wOID + ',this)"> </i>' + '<span class="CellComment"><ul style="list-style-type:none;padding-left:3px;margin-bottom: 0px;" id = "showOutput_' + d.wOID + '" >'
                    + '</ul></span>' +


                    '</div>', d.doid, d.scopeName, d.deliverableUnitName, d.wOName, d.activityDetails[0].subActivityName, d.wOID, d.workFlowName, d.nodeName, d.plannedOn, d.closedOn, d.createdBy, d.closedBy, d.comment])


            });
            table.draw();


            bindClickEventOnAction(); //Bind for view work order

        },
        error: function (xhr, status, statusText) {
            table.clear();
            table.draw();

        },
        complete: function (xhr, statusText) {
            pwIsf.removeLayer();
        }
    });
}
// get proficiency level
function getProfLevel(name) {
    let level = '1';
    if (name.toLowerCase() == "assessed") {
        level = '1';
    } else {
        level = '2';

    }
    return level;
}
//Accepted WOs Search Project
function SearchProjectAcceptedWO() {

    pwIsf.addLayer({ text: 'Please wait ...' });
    var project = document.getElementById("sel_project_name_wo").value.split('-');
    var projectName = project;

    var ServiceUrl = service_java_URL + "woDeliveryAcceptance/getacceptedWODetails/" + projectName + "/all/" + signumGlobal;

    var table = $('#tblProjectsAcceptedWO').DataTable({
        destroy: true,
        colReorder: true,
        dom: 'Bfrtip',
        buttons: [{
            extend: 'colvis',
            postfixButtons: ['colvisRestore'],
            columns: ':gt(0)'
        },
            'excel'],
    });

    $('#tblProjectsAcceptedWO tfoot th').each(function (i) {
        var title = $('#tblProjectsAcceptedWO thead th').eq($(this).index()).text();
        if (title != "Actions" && title != "")
            $(this).html('<input type="text" class="form-control" style="font-size:12px;"  placeholder="Search ' + title + '" data-index="' + i + '" />');
    });


    // Apply the search
    //table.columns().every(function () {
    //    var that = this;

    //    $('input', this.footer()).on('keyup change', function () {
    //        if (that.search() !== this.value) {
    //            that
    //                .columns($(this).parent().index() + ':visible')
    //                .search(this.value)
    //                .draw();
    //        }
    //    });
    //});

    //table.on('column-reorder', function (e, settings, details) {
    //    //Apply the search
    //    table.columns().every(function () {
    //        var that = this;

    //        $('input', this.footer()).on('keyup change', function () {
    //            if (that.search() !== this.value) {
    //                that
    //                    .columns($(this).parent().index() + ':visible')
    //                    .search(this.value)
    //                    .draw();
    //            }
    //        });
    //    });
    //});
    $("#ckbCheckAllDAWO").click(function (e) {
        // e.preventDefault();
        $(".checkBoxClassDAWO").prop('checked', $(this).prop('checked'));
    });

    $(".checkBoxClassWO").on('change', function (e) {
        // e.preventDefault();
        if (!$(this).prop("checked")) {
            $("#ckbCheckAllWO").prop("checked", false);
        }
    });

    $.isf.ajax({
        url: ServiceUrl,

        success: function (data) {

            ConvertTimeZones(data, getacceptedWODetails_tzColumns);

            $('#RejectbuttonWO1').show();
            $('.checkBoxClassDAWO').prop('checked', false);

            table.clear();
            $.each(data, function (i, d) {
                var wName = (d.wOName.replace(/\"/g, '&quot;')).replace(/'/g, "\\'");
                
                // getting proficiency level of work flow.
                let profLevel = getProfLevel(d.proficiencyName);
                table.row.add(['<span><label><input id="approveWO1" type="checkbox" name="approveWO1" class="checkBoxClassDAWO" value="' + d.wOID + '-\'' + wName + '\'\"' + '" data-subactivityid="' + d.activityDetails[0].subActivityID + '" data-projectid="' + d.projectID + '" data-woid="' + d.wOID + '" data-wfid="' + d.wfID + '" data-signum="' + d.signum + '"></label></span>', '<div style="display:flex;">&thinsp;&thinsp;&thinsp;' + '<a title="View Work Flowchart" href="#" onclick="flowChartOpenInNewWindow(' + ' \'DeliveryExecution\/FlowChart\?mode\=view&woID=' + d.wOID + '\&proficiencyId=' + d.proficiencyID + '\&proficiencyLevel=' + profLevel  +'\' ' + ')"><i style="cursor: pointer;" class="fa fa-sitemap"></i></a>&nbsp;&nbsp;&nbsp;' +
                    '<span onclick="openViewWorkOrder()"><i title="View Work Order" style="cursor: pointer;" data-toggle="modal" class="fa fa-file-o viewWorkOrder" data-workorder-id="' + d.wOID + '" data-edit-work-order-flag="false" data-doid="'+d.doid+'" data-woplan-id="'+d.woplanid+'"></i></span>&thinsp;&thinsp;&thinsp;' +
                    '|&thinsp;&thinsp;&thinsp;' +
                    '<i title="Reject" style="cursor: pointer;" data-toggle="modal" data-target="#modal_reject" class="fa fa-reply-all" onclick="sendShowDataReject(' + d.wOID + ',\'' + wName + '\'' + ',' + d.activityDetails[0].subActivityID + ',' + d.projectID + ',' + d.wfID + ',\'' + d.signum + '\'' + ', true)" ></i>&thinsp;&thinsp;&thinsp;' +
                    '<i title="Move To" style="cursor: pointer; display:none;" data-toggle="modal" data-target="#modal_moveTo" class="fa fa-exchange" onclick="sendShowDataMoveTo(' + d.wOID + ',\'' + wName + '\')"></i>' + '<i title="Show Output_Links" style=" z-index: 2; cursor: pointer;" class="fa fa-shield CellWithComment" onclick="ShowOutputLinks(' + d.wOID + ',this)"> </i>' + '<span class="CellComment"><ul style="list-style-type:none;padding-left:3px;margin-bottom: 0px;" id = "showOutput_' + d.wOID + '" >'
                    + '</ul></span>' +


                    '</div>', d.doid, d.scopeName, d.deliverableUnitName, d.wOName, d.activityDetails[0].subActivityName, d.wOID, d.workFlowName, d.nodeName, d.plannedOn, d.closedOn, d.createdBy, d.closedBy, d.acceptedOrRejectedBy, d.acceptedOrRejectedDate, d.comment])

            })
            table.draw();
            bindClickEventOnAction(); //Bind for view work order
        },
        error: function (xhr, status, statusText) {
            table.clear();
            table.draw();

        },
        complete: function (xhr, statusText) {
            pwIsf.removeLayer();
        }
    });
}

function sendShowDataAccept(wOID, wOName) {

    $("#woID").val(wOID);
    $("#woIDandName").val(wOID + " -" + wOName);
}

function sendShowDataMoveTo(wOID, wOName) {
    $("#woID_MoveTo").val(wOID);
    $("#woID_MoveToName").val(wOID + " -" + wOName);
    $(".select2able2").select2({
        dropdownParent: $("#modal_moveTo")
    });

    let tab = $("#nav_MainProjectView li.active a").text();
    let projectID = "";

    if (tab == "Delivery Acceptance") {
        projectID = $("#sel_project_name").val();
    }
    else if (tab = "Accepted Work Orders"){
        projectID = $("#sel_project_name_wo").val();
    }

    $.isf.ajax({
        url: service_java_URL + "accessManagement/getUserDetailsForDR/2/" + projectID,
        success: function (data) {

            $('#acceptOrRejectBy_MoveTo').html('');
            $('#acceptOrRejectBy_MoveTo').append('<option value="" ></option>');
            $.each(data, function (i, d) {
                var sig = [];
                sig = d + " "
                $('#acceptOrRejectBy_MoveTo').append('<option value="' + sig + '" >' + sig + '</option>');
            })
        },
        error: function (xhr, status, statusText) {
            alert("Signum does not exist !!!");

        }

    });


}

function ShowOutputLinks(wOID, obj) {



    $.isf.ajax({
        url: service_java_URL + "woManagement/getWOOutputFile?woid=" + wOID,
        success: function (data) {
            let outputID = '#showOutput_' + wOID;
            $(outputID).empty();

            if (data.responseData.length == 0 || data.isValidationFailed == true) {
                $(outputID).append('<li><a>No Output URL</a></li>');
            }
            else {
                let html = '';
                $.each(data.responseData, function (i, d) {

                    html += '<li><a title="' + d.outputUrl + '" href="' + d.outputUrl + '" target="_blank">' + d.outputName + '</a></li>';


                })
                $(outputID).append(html);
            }
            $(obj).next().show();
        },
        error: function (xhr, status, statusText) {
            alert("Error in Output Links!!!");

        }

    });


}

function sendShowDataRejectOfAcceptedWO(wOID, wOName, subActivityID) {

    $("#woID_Reject1").val(wOID);
    $("#woID_RejectandName1").val(wOID + " -" + wOName);
    $("#subActivityID").val(subActivityID);
    acceptRejectWoFlag = false;
    var reasonType = 'DeliveryFailure';
    var ServiceUrl = service_java_URL + "woExecution/getWOFailureReasons/" + reasonType;
    $('#reasonReject1').find('option').remove();
    $('#reasonReject1').append('<option value="">Select any one</option>');
    $.isf.ajax({
        url: ServiceUrl,
        success: function (data) {
            if (data.isValidationFailed == false) {
                $.each(data.responseData, function (i, d) {
                    $('#reasonReject1').append('<option value="' + d.failureReason + '">' + d.failureReason + '</option>');
                })
            }
            else {

                pwIsf.alert({ msg: data.formErrors[0], type: 'error' });

            }

        },
        error: function (xhr, status, statusText) {
            console.log('An error occurred');
        }
    });
}

function sendShowDataReject(wOID, wOName, subActivityID, projectID, wfID, signum, isAcceptedWO = "false") {
    acceptRejectWoFlag = false;
    var woIDandWOName = wOID + " -" + wOName;
    $("#isAcceptedWO").val(isAcceptedWO);
    $("#woID_Reject").val(wOID);
    $("#woID_RejectandName").val(woIDandWOName);
    $("#subActivityID").val(subActivityID);
    $("#wfid").val(wfID);
    $("#projid").val(projectID);
    $("#signum").val(signum);
    var reasonType = 'DeliveryFailure';
    var ServiceUrl = service_java_URL + "woExecution/getWOFailureReasons/" + reasonType;
    $('#reasonReject').find('option').remove();
    $('#reasonReject').append('<option value="">Select any one</option>');
    $.isf.ajax({
        url: ServiceUrl,
        success: function (data) {

            if (data.isValidationFailed == false) {
                $.each(data.responseData, function (i, d) {
                    $('#reasonReject').append('<option value="' + d.failureReason + '">' + d.failureReason + '</option>');
                })
            }
            else {
                pwIsf.alert({ msg: data.formErrors[0], type: 'error' });
            }
        },
        error: function (xhr, status, statusText) {
            console.log('An error occurred');
        }
    });

}
//DA
function acceptWO() {

    var commlength = $("#comments").val();
    if (commlength.length > 1000) {
        pwIsf.alert({ msg: "Maximum 1000 characters are allowed.", type: 'error' });
    }
    else {
        var woID = document.getElementById("woID").value;
        var rating = document.getElementById("ratingAccept").value;
        var comments = document.getElementById("comments").value;
        acceptWO_save(woID, rating, comments);
        $('#searchAcceptRejectTable').trigger("click");
        $("#comments").val('');
    }
}
//DA
function acceptWO_save(woID, rating, comments) {
    $('#modal_accept').modal('hide');
    var signum = signumGlobal;

    var service_data = "{\"lstWoID\":\[" + woID + "\],\"comment\":\"" + comments + "\",\"lastModifiedBy\":\"" + signum + "\",\"acceptedOrRejectedBy\":\"" + signum + "\",\"rating\":\"" + rating + "\"}";
    if (acceptRejectWoFlag == true) {

        var woIDList = [];
        $.each(multipleWO, function (i, d) {

            woIDList.push(d);
        }
        );
        service_data = "{\"lstWoID\":\[" + woIDList + "\],\"comment\":\"" + comments + "\",\"lastModifiedBy\":\"" + signum + "\",\"acceptedOrRejectedBy\":\"" + signum + "\",\"rating\":\"" + rating + "\"}";
    }
    pwIsf.addLayer({ text: "Please wait ..." });
    $.isf.ajax({
        url: service_java_URL + "woDeliveryAcceptance/acceptWorkOrder/",
        context: this,
        crossdomain: true,
        processData: true,
        contentType: 'application/json',
        type: 'POST',
        data: service_data,
        xhrFields: {
            withCredentials: false
        },
        success: AjaxSucceeded,
        error: AjaxFailed,
        complete: AjaxCompleted
    });

    function AjaxCompleted() {
        pwIsf.removeLayer();
        multipleWO = [];
    }

    function AjaxSucceeded(data, textStatus) {
        pwIsf.alert({msg: "Successfully accepted", type: "success" });
        SearchProject();
        //$('#messageOK').show();
        //$('#contentForm').hide();
        //document.getElementById('contentForm').style.display = 'none';
        //document.getElementById('AcceptButton').style.display = 'none';
        //document.getElementById('cancelModalAceeptWO').style.display = 'none';
        //document.getElementById('closeAccept').style.display = 'none';
    }
    function AjaxFailed(result) {
        pwIsf.removeLayer();
        pwIsf.alert({ msg: "An error occurred", type: "error" });
        //alert("Error.");
    }


}

function selectAll() {
    for (i = 0; i < document.f1.elements.length; i++) {
        if (document.f1.elements[i].type == "checkbox") {

            if (document.f1.elements[i].checked == 1) {
                document.f1.elements[i].checked = 0
            }
            else {
                document.f1.elements[i].checked = 1
            }
        }
    }
}

function reject() {
    var signum = signumGlobal;
    let isAcceptedWO = $('#isAcceptedWO').val();

    var woIDReject = document.getElementById("woID_Reject").value;
    var acceptRejectBy = document.getElementById("acceptRejectBy").value;
    var commentsReject = document.getElementById("commentsReject").value;

    var subActivityID = document.getElementById("subActivityID").value;
    var wfID = document.getElementById("wfid").value;
    var projid = document.getElementById("projid").value;
    var signumID = document.getElementById("signum").value;
    

    var lastModifiedBy = signum;
    var reasonReject = document.getElementById("reasonReject").value;
    var ratingReject = 0;
    //if (isAcceptedWO == "true") { $('#searchAcceptRejectTableAcceptedWO').trigger("click"); }
    //else { $('#searchAcceptRejectTable').trigger("click"); }
    if (reasonReject.length == 0) {
        pwIsf.alert({ msg: "Please select a reason", type: "info" });
        //alert("Please select a reason");
    }
    else if (commentsReject.length > 1000) {
        pwIsf.alert({ msg: 'Comment should be less than 1000 characters', type: 'error' });
    }
    else {
        saveReject(woIDReject, acceptRejectBy, commentsReject, lastModifiedBy, reasonReject, ratingReject, isAcceptedWO, subActivityID, wfID, projid, signumID);
    }
}

function rejectAcceptedWO() {
    var signum = signumGlobal;

    var woIDReject = document.getElementById("woID_Reject1").value;
    var acceptRejectBy = document.getElementById("acceptRejectBy1").value;
    var commentsReject = document.getElementById("commentsReject1").value;
    var lastModifiedBy = signum;
    var reasonReject = document.getElementById("reasonReject1").value;
    var ratingReject = 0;

    var subActivityID = document.getElementById("subactivityid1").value;
    var wfID = document.getElementById("wfid1").value;
    var projid = document.getElementById("projid1").value;
    var signumID = document.getElementById("signum1").value;

    $('#searchAcceptRejectTableWO').trigger("click");
    if (reasonReject.length == 0) {
        alert("Please select a reason");
    }
    else if (commentsReject.length > 1000) {
        pwIsf.alert({ msg: 'Comment should be less than 1000 characters', type: 'error' });
    }
    else {
        saveReject(woIDReject, acceptRejectBy, commentsReject, lastModifiedBy, reasonReject, ratingReject, subActivityID, wfID, projid, signumID);
    }
}

function saveReject(woIDReject, acceptRejectBy, commentsReject, lastModifiedBy, reasonReject, ratingReject, isAcceptedWO, subActivityID, wfID, projid, signumID) {
    $("#modal_reject").modal('hide');
    pwIsf.addLayer({ text: "Please wait ..." });
    var service_data = "{\"lstWoID\":\[" + woIDReject + "\],\"comment\":\"" + commentsReject + "\",\"lastModifiedBy\":\"" + lastModifiedBy + "\", \"reason\":\"" + reasonReject + "\",\"acceptedOrRejectedBy\":\"" + lastModifiedBy + "\",\"rating\":\"" + ratingReject + "\"}";
    if (acceptRejectWoFlag == true) {

        var woIDList = [];
        $.each(multipleWO, function (i, d) {
            woIDList.push(d);
        }
        );
        service_data = "{\"lstWoID\":\[" + woIDList + "\],\"comment\":\"" + commentsReject + "\",\"lastModifiedBy\":\"" + lastModifiedBy + "\", \"reason\":\"" + reasonReject + "\",\"acceptedOrRejectedBy\":\"" + lastModifiedBy + "\",\"rating\":\"" + ratingReject + "\"}";
    }

    //var subActivityidList = '{' + '"subactivityID" :'  + subActivityID + ',' + '"wfID" :'  + wfID + '}';
   
    //wfDetailForWORejectWithUserProficiency = {
    //    '"signumID"': signumID,
    //    '"subActivityWfIDModel"' : '[' + subActivityidList + ']',
    //    '"projectID"' : projid,
    //    '"proficiencyMeasurement"' : "reverse",
    //    '"createdBy"': signumGlobal,
    //    '"woID"': woIDReject
    //};

    let wfDetailForWORejectWithUserProficiency = new Object();
    let subActivityidListArr = [];
    let subActivityidListObj = new Object();

    subActivityidListObj.signumID = signumID;
    subActivityidListObj.subactivityID = subActivityID;
    subActivityidListObj.wfID = wfID;
    subActivityidListObj.woID = woIDReject;

    subActivityidListArr.push(subActivityidListObj);

    wfDetailForWORejectWithUserProficiency.subActivityWfIDModel = subActivityidListArr;
    wfDetailForWORejectWithUserProficiency.projectID = projid;
    wfDetailForWORejectWithUserProficiency.proficiencyMeasurement = "reverse";
    wfDetailForWORejectWithUserProficiency.createdBy = signumGlobal;

    if (acceptRejectWoFlag == true) {

        var subActivityIDList = [];        
        $.each(multipleSubActivityID, function (i, d) {
            subActivityIDList.push(d);

        });      
  
       // wfDetailForWORejectWithUserProficiency = "{\"signumID\":\"" + signumGlobal + ",\"subActivityWfIDModel\":\[" + subActivityIDList + "\],\"projectID\":\"" + projid + "\",\"proficiencyMeasurement\":\"" + "reverse" + "\", \"createdBy\":\"" + createdBy + "\"}";

        // wfDetailForWORejectWithUserProficiency = {
        //     '"signumID"': signumID,
        //     '"subActivityWfIDModel"': '[' + subActivityIDList + ']',
        //     '"projectID"': projid,
        //     '"proficiencyMeasurement"': "reverse",
        //     '"createdBy"': signumGlobal,
        //     '"woID"': woIDReject
        //};

        wfDetailForWORejectWithUserProficiency.subActivityWfIDModel = subActivityIDList;
        wfDetailForWORejectWithUserProficiency.projectID = projid;
        wfDetailForWORejectWithUserProficiency.proficiencyMeasurement = "reverse";
        wfDetailForWORejectWithUserProficiency.createdBy = signumGlobal;
    }
    
    $.isf.ajax({
        url: service_java_URL + "woDeliveryAcceptance/rejectWorkOrder/",
        context: this,
        //async:false,
        crossdomain: true,
        processData: true,
        contentType: 'application/json',
        type: 'POST',
        data: service_data,
        xhrFields: {
            withCredentials: false
        },
        success: AjaxSucceeded,
        error: AjaxFailed,
        complete: AjaxCompleted
    });
    function AjaxCompleted() {
        //pwIsf.removeLayer();
       
        //pwIsf.alert({ msg: "Successfully rejected", type: "success" });
        multipleWO = [];
        multipleSubActivityID = [];
        multiplewfID = [];
    }
    function AjaxSucceeded(data, textStatus) {

        var convertedJson = JSON.parse(JSON.stringify(data))
        if (convertedJson.isValidationFailed == false) {

            pwIsf.alert({ msg: convertedJson.formMessages[0], type: 'success' });

        }
        else {
            pwIsf.alert({ msg: convertedJson.formErrors[0], type: 'error' });
            pwIsf.removeLayer();
            return;
        }

        //save user proficiency after rejecttion of WO
        saveUserProficiencyOnWOReject(wfDetailForWORejectWithUserProficiency);

        if (isAcceptedWO == "true")
            SearchProjectAcceptedWO();
        else
            SearchProject();
    }
    function AjaxFailed(result) {
        pwIsf.removeLayer();
        pwIsf.alert({ msg: "An error occurred", type: "error" });
    }

    //save user proficiency after rejecttion of WO
    function saveUserProficiencyOnWOReject(wfDetailForWORejectWithUserProficiency) {
        $.isf.ajax({
            type: "POST",
            contentType: "application/json; charset=utf-8",
            url: service_java_URL + "/woExecution/saveWFUserProficiency",
            data: JSON.stringify(wfDetailForWORejectWithUserProficiency),
            success: function (data) {
                //data = JSON.parse(data);
            },
            error: function (xhr, status, statusText) {
                //var err = JSON.parse(xhr.responseText);
                //pwIsf.alert({ msg: "An error occurred", type: 'error' });
            }
        });
    }
}

function moveTo() {
    var idMoveTo = document.getElementById("woID_MoveTo").value;
    var comment_MoveTo = document.getElementById("comment_MoveTo").value;
    var acceptOrRejectBy_MoveTo = document.getElementById("acceptOrRejectBy_MoveTo").value;
    var lastModifiedBy = signumGlobal;

    if (acceptOrRejectBy_MoveTo == '') {
        $('#lbl_delAcceptance_Required_moveTo').show();
        return;
    } else {
        $('#lbl_delAcceptance_Required_moveTo').hide();
    }

    saveMoveTo(idMoveTo, comment_MoveTo, acceptOrRejectBy_MoveTo, lastModifiedBy);

}

function saveMoveTo(idMoveTo, comment_MoveTo, acceptOrRejectBy_MoveTo, lastModifiedBy) {

    acceptOrRejectBy_MoveTo = acceptOrRejectBy_MoveTo.split('(');
    var service_data = "{\"lstWoID\":\[" + idMoveTo + "\],\"comment\":\"" + comment_MoveTo + "\",\"acceptedOrRejectedBy\":\"" + acceptOrRejectBy_MoveTo[0] + "\",\"lastModifiedBy\":\"" + lastModifiedBy + "\"}";

    $.isf.ajax({
        url: service_java_URL + "woDeliveryAcceptance/sendWorkOrderForAcceptance",
        context: this,
        crossdomain: true,
        processData: true,
        contentType: 'application/json',
        type: 'POST',
        data: service_data,
        xhrFields: {
            withCredentials: false
        },
        success: AjaxSucceeded,
        error: AjaxFailed
    });
    function AjaxSucceeded(data, textStatus) {
        $('#messageOK_moveTo').show();
        document.getElementById('modalMoveTo').style.display = 'none';
        document.getElementById('buttonCancelModal').style.display = 'none';
        document.getElementById('MoveButtonSEND').style.display = 'none';
    }
    function AjaxFailed(result) {
        alert("Error.");

    }
}

function cleanAll_Acceptance() {
    $('#contentForm').show();
    $('#ratingAccept').val("");
    $('#comments').val("");
    $('#messageOK').hide();
    $('#contentForm').show();
    $('#AcceptButton').show();
    $('#cancelModalAceeptWO').show();
    $('#closeAccept').show();
    SearchProject();
}

function cleanAll_Reject(isAcceptedWO) {
    $('#contentFormReject').show();
    $('#acceptRejectBy').val("");
    $('#reasonReject').val("");
    $('#commentsReject').val("");
    $('#messageOK_Reject').hide();
    $('#RejectButton').show();
    $('#cancelModalReject').show();
    $('#ModalCloseReject').show();
    if (isAcceptedWO == "true") { SearchProjectAcceptedWO() }
    else { SearchProject(); }
}

function cleanAll_AcceptedWOReject() {
    $('#contentFormReject').show();
    $('#acceptRejectBy').val("");
    $('#reasonReject').val("");
    $('#commentsReject').val("");
    $('#messageOK_Reject').hide();
    $('#RejectButton').show();
    $('#cancelModalReject').show();
    $('#ModalCloseReject').show();
    SearchProjectAcceptedWO();
}

function cleanAll_MoveTo() {
    $('#contentMoveTo').show();
    $('#rating_MoveTo').val("");
    $('#deliveryStatus_MoveTo').val("");
    $('#reason_MoveTo').val("");
    $('#acceptOrRejectBy_MoveTo').val("");
    $('#newAcceptance').val("");
    $('#comment_MoveTo').val("");
    $('#messageOK_moveTo').hide();
    $('#contentMoveTo').show();
    $('#MoveButtonSEND').show();
    $('#buttonCancelModal').show();
    $('#modalMoveTo').show();
    SearchProject();
}

//open in popup
function flowChartOpenInNewWindow(url) {

    var urlLocation = window.location.href;

    var count = (urlLocation.match(/Project/g) || []).length;
    if (count) {
        var firstPart = urlLocation.split('Project');
        url = firstPart[0] + url;
    }


    var width = window.innerWidth * 0.95;
    // define the height in
    var height = width * window.innerHeight / window.innerWidth;
    // Ratio the hight to the width as the user screen ratio
    window.open(url + '&commentCategory=WO_DA_WO_LEVEL', 'newwindow', 'width=' + width + ', height=' + height + ', top=' + ((window.innerHeight - height) / 2) + ', left=' + ((window.innerWidth - width) / 2));
}

function openViewWorkOrder() {

    $('#myModalMadEwo').modal({
        backdrop: 'static'
    })
    $('#myModalMadEwo').modal('show');
    $('.nodeDetailsIcon').hide();
    $('#edit_node_details').remove();
    $('#btn_update_nodes').hide();
    $('#btn_update_project').hide();
    $('.projectDetailsIcon').remove();
    
};

function multipleSelectCheckbox() {
    var projID;
    var signum;
    $.each($("input[name='approveWO']:checked"), function () {
        var str = $(this).val();
        var subactivityID = $(this).data('subactivityid');
        var wfid = $(this).data('wfid');
        projID = $(this).data('projectid');
        signum = $(this).data('signum');
        let woid = $(this).data('woid');

        //var subActModel = '{' + '"subactivityID" :' + subactivityID + ',' + '"wfID" :' + wfid + '}';

        let subActModel = new Object();
        subActModel.subactivityID = subactivityID;
        subActModel.wfID = wfid;
        subActModel.signumID = signum;
        subActModel.woID = woid;

        multipleWO.push(str.substr(0, str.indexOf("-")));
        multipleSubActivityID.push(subActModel);
        multiplewfID.push(wfid);
    });

    $("#woID_MoveTo").val(multipleWO);
    $("#subActivityID").val(multipleSubActivityID);
    $("#wfid").val(multiplewfID);

    $("#projid").val(projID);

    $("#signum").val(signum);

}

function multipleSelectCheckboxAcceptedWO() {
    var projID;
    var signum;
    $.each($("input[name='approveWO1']:checked"), function () {
        var str = $(this).val();
        var subactivityID = $(this).data('subactivityid');
        var wfid = $(this).data('wfid');
        projID = $(this).data('projectid');
        signum = $(this).data('signum');
        let woid = $(this).data('woid');

        //var subActModel = '{' + "subactivityID :" + subactivityID + "," + "wfID :" + wfid + '}';

        let subActModel = new Object();
        subActModel.subactivityID = subactivityID;
        subActModel.wfID = wfid;
        subActModel.signumID = signum;
        subActModel.woID = woid;

        multipleWO.push(str.substr(0, str.indexOf("-")));
        multipleSubActivityID.push(subActModel);
        multiplewfID.push(wfid);

    });

    $("#woID_MoveTo").val(multipleWO);
    $("#subActivityID").val(multipleSubActivityID);
    $("#wfid").val(multiplewfID);
    
    $("#projid").val(projID);
   
    $("#signum").val(signum);
    
}

function acceptmultipleWO() {
    multipleWO = [];
    $('#contentForm').show();
    multipleSelectCheckbox();
    $("#woIDandName").hide();
    $("#woIdlabel").hide();
    if (multipleWO.length > 0) {

        $('#modal_accept').modal('show');
        GetAcceptRejectTask();
        $('.checkBoxClassDA').prop('checked', false);
    }
    else {
        $('#modal_accept').modal('hide');

        pwIsf.alert({ msg: 'Please Select one or more Work Order(s) to accept or reject.', type: 'warning' });


    }

}

function moveMultipleWO() {
    multipleWO = [];
    $('#contentForm').show();
    multipleSelectCheckbox();
    $("#woIDandName").hide();
    $("#woIdlabel").hide();
    if (multipleWO.length > 0) {

        $('#modal_moveTo').modal('show');
        getMoveToTask();
    }
    else {
        $('#modal_moveTo').modal('hide');

        pwIsf.alert({ msg: 'Please Select one or more Work Order(s) to move work order', type: 'warning' });


    }

}

function rejectmultipleWO() {
    multipleWO = [];
    multipleSelectCheckbox();
    $("#woIdlabelreject").hide();
    $("#woID_RejectandName").hide();
    if (multipleWO.length > 0) {

        $('#modal_reject').modal('show');
        GetAcceptRejectTask();
        $('.checkBoxClassDA').prop('checked', false);
    }
    else {
        $("#modal_reject").modal('hide');

        pwIsf.alert({ msg: 'Please Select a Work Order To accept or reject.', type: 'warning' });


    }

}

function rejectmultipleAcceptedWO() {
    multipleWO = [];
    multipleSelectCheckboxAcceptedWO();
    $("#woIdlabelreject").hide();
    $("#woID_RejectandName").hide();
    $('#isAcceptedWO').val(true);
    if (multipleWO.length > 0) {

        $('#modal_reject').modal('show');

        GetAcceptRejectTaskAcceptedWO();
        $('.checkBoxClassDAWO').prop('checked', false);
    }
    else {
        $("#modal_reject").modal('hide');

        pwIsf.alert({ msg: 'Please Select a Work Order To accept or reject.', type: 'warning' });


    }

}

function GetAcceptRejectTask() {

    var woIdName = '';
    $.each($("input[name='approveWO']:checked"), function () {
        woIdName += $(this).val();
    }
    );
    $('#woIDandName').val(woIdName);
    $('#woID_RejectandName').val(woIdName);
    acceptRejectWoFlag = true;
    var reasonType = 'DeliveryFailure';
    var ServiceUrl = service_java_URL + "woExecution/getWOFailureReasons/" + reasonType;
    $('#reasonReject').find('option').remove();
    $('#reasonReject').append('<option value="">Select any one</option>');
    $.isf.ajax({
        url: ServiceUrl,
        success: function (data) {
            if (data.isValidationFailed == false) {
                $.each(data.responseData, function (i, d) {
                    $('#reasonReject').append('<option value="' + d.failureReason + '">' + d.failureReason + '</option>');
                })
            }
            else {
                pwIsf.alert({ msg: data.formErrors[0], type: 'error' });
            }
        },
        error: function (xhr, status, statusText) {
            console.log('An error occurred');
        }
    });
}

function GetAcceptRejectTaskAcceptedWO() {

    var woIdName = '';
    $.each($("input[name='approveWO1']:checked"), function () {
        woIdName += $(this).val();
    }
    );
    $('#woID_RejectandName').val(woIdName);
    acceptRejectWoFlag = true;
    var reasonType = 'DeliveryFailure';
    var ServiceUrl = service_java_URL + "woExecution/getWOFailureReasons/" + reasonType;
    $('#reasonReject').find('option').remove();
    $('#reasonReject').append('<option value="">Select any one</option>');
    $.isf.ajax({
        url: ServiceUrl,
        success: function (data) {

            if (data.isValidationFailed == false) {
                $.each(data.responseData, function (i, d) {
                    $('#reasonReject').append('<option value="' + d.failureReason + '">' + d.failureReason + '</option>');
                })
            }
            else {
                pwIsf.alert({ msg: data.formErrors[0], type: 'error' });
            }
        },
        error: function (xhr, status, statusText) {
            console.log('An error occurred');
        }
    });
}

function getMoveToTask() {

    var woIdName = '';
    $.each($("input[name='approveWO']:checked"), function () {
        woIdName += $(this).val();
    }
    );

    $(".select2able2").select2({
        dropdownParent: $("#modal_moveTo")
    });

    let tab = $("#nav_MainProjectView li.active a").text();
    let projectID = "";

    if (tab == "Delivery Acceptance") {
        projectID = $("#sel_project_name").val();
    }
    else if (tab = "Accepted Work Orders") {
        projectID = $("#sel_project_name_wo").val();
    }

    $.isf.ajax({
        url: service_java_URL + "accessManagement/getUserDetailsForDR/2/" + projectID,
        success: function (data) {

            $('#acceptOrRejectBy_MoveTo').html('');
            $('#acceptOrRejectBy_MoveTo').append('<option value="" ></option>');
            $.each(data, function (i, d) {
                var sig = [];
                sig = d + " "
                $('#acceptOrRejectBy_MoveTo').append('<option value="' + sig + '" >' + sig + '</option>');
            })
        },
        error: function (xhr, status, statusText) {
            alert("Signum does not exist !!!");

        }

    });
}

function acceptModalClose() {

    //$('#searchAcceptRejectTable').trigger("click");
    $('#modal_accept').hide();
    $('.checkBoxClassDA').prop('checked', false);
    $("#comments").val('');
}

function rejectModalClose() {

   // $('#searchAcceptRejectTable').trigger("click");
    $('#modal_reject').hide();
    $('.checkBoxClassDA').prop('checked', false);
}

function rejectModalClose1() {

    $('#searchAcceptRejectTableWO').trigger("click");
    $('#modal_reject_accept_WO').hide();
    $('.checkBoxClassDAWO').prop('checked', false);
}