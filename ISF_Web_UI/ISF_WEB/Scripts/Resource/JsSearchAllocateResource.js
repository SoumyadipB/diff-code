var jobStageID = '';
var jobRoleID = '';
var certificateID = '';
var competenceID = '';
var signum = '';
var domainID = '';
var competenceLevel = '';
var subDomainID = '';
var subDomainName = '';
var subServiceAreaID = '';
var startDate = '';
var endDate = '';
var hours = '';
var certificateData = "";
var competenceData = "";
var onsiteCount = 0;
var remoteCount = 0;
var onsiteCounter = 0;
var remoteCounter = 0;
var Domain = '';
var ServiceArea = '';
var SubDomain = '';
var JobStage = '';
var JobName = '';
var competenceLevel = '';
var resourcePositionList = '';
var competenceIDChk = '';
var certificateIDChk = '';
var resourcePositionID = '';
var rrid = '';

var currentTabRpid;
var selectedSignumText = "Selected Signum";
var storPreviousSerachResult = '';
var storePreFilledSearchResult = '';


//function pleaseWaitISF(options) {
   
//    var defaults = {
//        showSpin: true,
//        spinCss: 'font-size:24px;',
//        text: '',
//        textCss:'font-size:10px;'
//    };
//    var actual = $.extend({}, defaults, options || {});

//    var spinCss = actual.spinCss;
//    var textCss = actual.textCss;
//    var text = actual.text;
//    var showSpin = actual.showSpin;
    
//    var spinIcon = (showSpin) ? '<i class="fa fa-spinner fa-spin" style="' + spinCss + '"></i>' : '';
//    var loadingText = (text != '') ? '<span style="' + textCss + '"> ' + text + '</span>' : '';
    
//    var retStr = '<div style="display:block;text-align:center;"> ' + spinIcon + loadingText + '</div>';

//    return retStr;
//}


$(document).on('click', '.clickable', function (e) {
    var $this = $(this);
    if (!$this.find('i').hasClass('fa-angle-up')) {       
        $this.find('i').removeClass('fa-angle-up').addClass('fa-angle-down');
    } else {        
        $this.find('i').removeClass('fa-angle-down').addClass('fa-angle-up');
    }
});


function getSignum() {
    //$.isf.ajax({
    //    url: service_java_URL + "activityMaster/getEmployeeDetails",
    //    success: function (data) {

    //        var navtabcontent = '<select id="search_select_signum" class="form-control select2able"  name="search_select_signum"><option value="0">Select Signum</option>';
    //        navtabcontent = navtabcontent + '</select>';
                       

    //        $("#dropdown_manager_signum").html(navtabcontent);
    //        console.log(data);
    //        $.each(data, function (i, d) {
    //            $('#search_select_signum').append('<option value="' + d.signum + '">' + d.signum + ' - ' + d.employeeName + '</option>');
                
    //        })

    //        $("#search_select_signum").select2();
    //    },
    //    error: function (xhr, status, statusText) {
    //      //  alert("Failed to Get Signum ");
    //        console.log('An error occurred in getSignum() ::' + xhr.responseText);
    //    }
    //});
}

function getAllLineManagers() {
    $.isf.ajax({

        url: service_java_URL + "activityMaster/getAllLineManagers",
        success: function (data) {
            $.each(data, function (i, d) {
                $('#search_select_manager').append('<option value="' + data[i].managerSignum + '">' + data[i].managerSignum + '/' + data[i].managerName + '</option>');
           })
        },
        error: function (xhr, status, statusText) {
          //  alert("Fail " + xhr.responseText);
           // alert("status " + xhr.status);
            console.log('An error occurred in getAllLineManagers() ::' + xhr.responseText);
        }
    });
}

function getDomains(selectedDomain) {
    
    $.isf.ajax({
        url: service_java_URL + "activityMaster/getDomainDetails",
        success: function (data) {

            $.each(data, function (i, d) {

                if (d.domain == selectedDomain) {
                    $('#search_select_domain').append('<option selected value="' + d.domain + '">' + d.domain + '</option>');
                } else {
                    $('#search_select_domain').append('<option value="' + d.domain + '">' + d.domain + '</option>');
                }

                $('#search_select_domain').select2({width:'off'});
            })
        },
        error: function (xhr, status, statusText) {
          //  alert("Fail getDomains" + xhr.responseText);
           // alert("status " + xhr.status);
            console.log('An error occurred in getDomains() ::' + xhr.responseText);
        }
    });

}

function getServices(selectedService) {
    
    $.isf.ajax({
        url: service_java_URL + "activityMaster/getServiceAreaDetails",
        success: function (data) {

            $.each(data, function (i, d) {
                if (d.serviceArea == selectedService) {
                    $('#search_select_service').append('<option selected value="' + d.serviceArea + '">' + d.serviceArea + '</option>');
                }
                $('#search_select_service').append('<option value="' + d.serviceArea + '">' + d.serviceArea + '</option>');

            })

            $('#search_select_service').select2({ width: 'off' });

        },
        error: function (xhr, status, statusText) {
           // alert("Fail " + xhr.responseText);
           // alert("status " + xhr.status);
            console.log('An error occurred in getServices() ::' + xhr.responseText);
        }
    });

}

function getJobStage(selectedJobStage) {
    $.isf.ajax({
        url: service_java_URL + "/resourceManagement/getJobStages",
        success: function (data) {

            $.each(data, function (i, d) {
                if (d.JobStageName == selectedJobStage) {
                    $('#search_select_jobstage').append('<option selected value="' + d.JobStageName + '">' + d.JobStageName + '</option>');

                } else {
                    $('#search_select_jobstage').append('<option value="' + d.JobStageName + '">' + d.JobStageName + '</option>');
                }

            })

            $('#search_select_jobstage').select2({ width: 'off' });
        },
        error: function (xhr, status, statusText) {
          //  alert("Fail " + xhr.responseText);
         //   alert("status " + xhr.status);
            console.log('An error occurred in getJobStage() ::' + xhr.responseText);
        }
    });

}

function getJobRoles(selectedJobRole) {

    $.isf.ajax({
        url: service_java_URL + "resourceManagement/getJobRoles",
        success: function (data) {

            $.each(data, function (i, d) {
                if (d.JobRoleName == selectedJobRole) {
                    $('#search_select_jobrole').append('<option selected value="' + d.JobRoleName + '">' + d.JobRoleName + '</option>');
                } else {
                    $('#search_select_jobrole').append('<option value="' + d.JobRoleName + '">' + d.JobRoleName + '</option>');
                }

            })

            $('#search_select_jobrole').select2({width:'off'});
        },
        error: function (xhr, status, statusText) {
         //   alert("Fail " + xhr.responseText);
          //  alert("status " + xhr.status);
            console.log('An error occurred in getJobRoles() ::' + xhr.responseText);
        }
    });
}



function getDemandRequestDetail(rrid) {
    $("#alertErrSAR").css("display", "none");

    var result;
    $.isf.ajax({

        url: service_java_URL + "resourceManagement/getDemandRequestDetail/" + rrid,

        success: function (data) {
            result = data;

            console.log('first Data');
            console.log(data);

            if (data.length) {

                $.each(data, function (i, d) {

                    var sendParams = {};


                    sendParams.startDate = d.rpefList[0].startDate;
                    sendParams.endDate = d.rpefList[0].endDate;
                    sendParams.jobStage = d.jobStageName;
                    sendParams.domain = d.domain;
                    sendParams.serviceArea = d.serviceArea;


                    getDomains(d.domain + '/' + d.subDomain);
                    getServices(d.serviceArea + '/' + d.subServiceArea);
                    getJobStage(d.jobStageName);
                    getJobRoles(d.jobRoleName);

                    //createWorkEffortTab(d.rpefList, rrid, sendParams);
                    createWorkEffortTab(d, rrid, sendParams);
                })
            } else {
                $('#search_div').html('Data not found').css({'color':'red','textAlign':'center'});
            }
        },
        error: function (xhr, status, statusText) {
            console.log('An error occurred on getDemandRequestDetail: ' + xhr.error);
          //  alert('An error occurred on getDemandRequestDetail: ' + xhr.error);
            $("#alertErrSAR").css("display", "block");
        }
    });
}
function showHideResetSeacrhButton(selectBox,showHide) {

    var resetTag = $(selectBox).closest('td').find('.resetPreviousSearchResult');
    if (showHide == 'show') {
        $(resetTag).show();
    } else {
        $(resetTag).hide();
    }

}

function acceptSlot(thisBtn,modal) {
    var previousLblId = $(thisBtn).data('lblid');

    var sdate=$('#availability_sdate').val();
    var edate=$('#availability_edate').val();

    //$('#lbl_selected_signum_'+previousLblId).attr('data-sdate',sdate);
    $('#lbl_selected_signum_' + previousLblId).attr('data-edate', edate);
    $('#lbl_selected_signum_' + previousLblId).attr('data-save', 'yes');
    
    $('#chk_propose_partially_' + previousLblId).prop('checked', true);

    var pLabl = $('#lbl_selected_signum_' + previousLblId).closest('.tag');
    $(pLabl).removeClass('label-default').removeClass('label-warning').addClass('label-success');
}

function makeavailabilityStructure(AvailabileData,modal,params) {

    //console.log(params);
    //console.log(AvailabileData);

    var monthNameArr = [];
    var dtData = AvailabileData.details;

    var recommendedSdate=AvailabileData.recommendedStartDate;
    var recommendedEdate = AvailabileData.recommendedEndDate;

    var positionSdate = $('#lbl_selected_signum_' + params.nearLblUid).data('sdate');
    var positionEdate = $('#lbl_selected_signum_' + params.nearLblUid).data('edate');

    var availablePercentage = AvailabileData.availability;
    var percentageBarClass = 'p_bar_green_color';

    var makeEndDate = positionEdate;
    if ($('#chk_propose_partially_' + params.nearLblUid).is(':checked')) {
        makeEndDate = $('#lbl_selected_signum_' + params.nearLblUid).attr('data-edate');
    }

    for (var mk in dtData) {
        if (!monthNameArr.includes(dtData[mk].month_name)) {
            monthNameArr.push(dtData[mk].month_name);
        }
    }

    //set modal title
    $(modal)
        .find('.modal-title').css({'font-size':'15px'})
        .html('Availability for ' + params.signum + ' [ Between ' + positionSdate + ' and ' + positionEdate +' ]');

    percentageBarClass = (availablePercentage <= 70 && availablePercentage >= 40) ? 'p_bar_yellow_color' : (availablePercentage < 40) ? 'p_bar_red_color' : 'p_bar_green_color';


    var progressBarDiv = '<div class="progress ' + percentageBarClass + '">'
                        + '<div class="progress-bar" role="progressbar" aria-valuenow="10" aria-valuemin="0" aria-valuemax="100" style="width: ' + availablePercentage + '%;">'
                         + '<span>Availability ' + availablePercentage + '%</span>'
                        +'</div>'
                    +'</div>';

    var dateRangeDiv = '';

    if (params.percent >= 100) {
        //dateRangeDiv = '<label style="display: block;text-align: center;font-size: 14px;color: indianred;font-weight: bold;">Not Available <i class="fa fa-times-circle" style="font-size:18px;color:indianred;"></i></label>';
        dateRangeDiv = '';
    } else {
        dateRangeDiv = '<div class="date_range">'
                        + '<label style="padding-right:10px;">Booking Slot from </label>'
                        + '<input class="datepicker" readonly name="availability_sdate" id="availability_sdate" data-date-format="yyyy-mm-dd" value="' + positionSdate + '" style="background-color:#f1f1f1;">'
                        + '<label style="padding-right:10px;padding-left:10px;"> to </label>'
                        + '<input class="datepicker" readonly name="availability_edate" id="availability_edate" data-date-format="yyyy-mm-dd" value="' + makeEndDate + '">'
                        + '<button type="button" class="btn btn-primary" style="padding: 3px 10px;margin-top: 6px;margin-left: 12px;"  data-dismiss="modal" data-lblid="' + params.nearLblUid + '" onclick="acceptSlot(this)" >Accept This Slot</button>'
                    + '</div>';
    }

    

    var detailsDiv = '<div class="details_div">' + progressBarDiv + dateRangeDiv + '</div>';

    var lblsStr='<div class="dt_box_label"><div class="dt_label">Date</div><div class="hr_part"><div class="req_hr_label"><span class="hr_caption">Req. Hr</span></div><div class="avai_hr_label"><span class="hr_caption">Ava. Hr</span></div></div></div>';

    var monthNameStr = '';
    var finalStr = '';
    var startRangeFlag = false;

    for (var k in monthNameArr) {
        var monthName=monthNameArr[k];

        monthNameStr='<div class="month_name">'+monthName+'</div>';
        var dtBoxStr = '';
        
        for (var inK in dtData) {

            var dtRowStr = '<div class="dt_row">';
            

            if (monthNameArr[k]== dtData[inK].month_name) {

                if (!dtData[inK].isweekend) {

                    var d = new Date(dtData[inK].dt);
                    var getDateNum = d.getDate();

                    var reqHr = dtData[inK].req_hr;
                    var avaHr = dtData[inK].avail_hr;

                    var avaHrCss = '';

                    if (avaHr >= reqHr) {
                        if (startRangeFlag) {
                            avaHrCss = 'available with_in_range';
                        } else {
                            avaHrCss = 'availableOutofRange';
                        }
                        if (recommendedSdate == dtData[inK].dt) {
                            avaHrCss = 'available start_slot with_in_range';
                            startRangeFlag = true;
                        }
                        if (recommendedEdate == dtData[inK].dt) {
                            avaHrCss = 'available end_slot with_in_range';
                            startRangeFlag = false;
                        }
                    } else {
                        avaHrCss = 'notAvailable';
                    }

                    dtBoxStr += '<div class="dt_box">'
                            + '<div class="dt">' + getDateNum + '</div>'
                           + '<div class="hr_part">'
                           + '     <div class="req_hr">'

                           + '         <span class="req_hr_value">' + reqHr + '</span>'
                            + '    </div>'
                           + '     <div class="avai_hr ' + avaHrCss + '">'

                            + '        <span class="hr_value">' + avaHr + '</span>'
                            + '    </div>'
                           + ' </div>'
                       + ' </div>';
                } else {
                    dtBoxStr += '<div class="dt_box_weekend"></div>';
                }
            }

        }

        finalStr += monthNameStr + dtRowStr + lblsStr + dtBoxStr + '</div>';
            

    }

    finalStr = detailsDiv + '<div class="dt_availability_parent_div">' + finalStr + '</div>';
    $(modal)
            .find('.modal-body')
            .html(finalStr);

    //$('#availability_sdate').datepicker({
    //    format: 'yyyy-mm-dd',
    //    startDate: positionSdate,
    //    endDate: positionEdate
    //});

    $('#availability_edate').datepicker({
        format: 'yyyy-mm-dd',
        startDate: positionSdate,
        //endDate: positionEdate
        autoclose: true,
        
        
    });
    
    //console.log(monthNameArr);
}

function getAvailabilityOfUser(paramObj) {

    var signum = paramObj.signum;
    var weid = paramObj.weid;

    //signum = 'EUWYZCN';
    var params = { 'nearLblUid': paramObj.nearLblUid, 'signum': signum, 'percent': paramObj.percent };

    var availabilityPopup = $('#userAvailabilityModal');


    $(availabilityPopup) 
        .find('.modal-title')
        .html('Availability for ' + signum);

    $(availabilityPopup)
        .find('.modal-body')
        .html(pleaseWaitISF({text:'Please wait ...',textCss:'font-size:12px;'}));
  
    $(availabilityPopup).modal('show');
    var serviceUrl = service_java_URL + "resourceManagement/getBookedResourceBySignum?signum=" + signum + "&weid=" + weid;
    if (ApiProxy == true) {
        serviceUrl = service_java_URL + "resourceManagement/getBookedResourceBySignum?signum=" + signum + encodeURIComponent( "&weid=" + weid);
    }
    //return;
    $.isf.ajax({
        url: serviceUrl,
        success: function (data) {
            
            makeavailabilityStructure(data, availabilityPopup,params);
            
        },
        error: function (xhr, status, statusText) {

            console.log('An error occurred in getAvailabilityOfUser() ::' + xhr.responseText);
        }
    });


}
function getAvailabilityPercentage(signum,weid,labelUid) {

    var loader = pleaseWaitISF({spinCss:'font-size:16px;'});
    
    $('#spn_availability_percentage_' + labelUid).html(loader);
    
    //set sdate and edate to the lable again
    var rowStartDate=$('#startdate'+currentTabRpid).text();
    var rowEndDate=$('#enddate'+currentTabRpid).text();
    $('#lbl_selected_signum_' + labelUid).attr('data-sdate',rowStartDate);
    $('#lbl_selected_signum_' + labelUid).attr('data-edate', rowEndDate);
    var serviceUrl = service_java_URL + "resourceManagement/getBookedResourceBySignum?signum=" + signum + "&weid=" + weid;
    if (ApiProxy == true) {
        serviceUrl = service_java_URL + "resourceManagement/getBookedResourceBySignum?signum=" + signum + encodeURIComponent( "&weid=" + weid);
    }
    $.isf.ajax({
        url: serviceUrl,
        success: function (data) {
            
            var availableText = '';
            //manage parent label color and data
            var pLabl = $('#lbl_selected_signum_' + labelUid).closest('.tag');
            if (data.availability < 100) {
                
                $('#lbl_selected_signum_' + labelUid).attr('data-save', 'no');

                //if (data.availability <= 0) {

                //    $(pLabl).removeClass('label-default').removeClass('label-success').removeClass('label-warning').addClass('label-danger');
                //    availableText = ' Not Available (' + data.availability + '%)';

                //    } else {

                        $(pLabl).removeClass('label-default').removeClass('label-success').removeClass('label-danger').addClass('label-warning');

                        $('#chk_propose_partially_' + labelUid).show();
                        $('#chk_propose_partially_' + labelUid).prop('checked', false);
                        availableText = ' Propose Partially (' + data.availability + '%)';

                //}

                
            } else {
                $('#chk_propose_partially_' + labelUid).prop('checked', false);
                $('#chk_propose_partially_' + labelUid).hide();

                $('#lbl_selected_signum_' + labelUid).attr('data-save', 'yes');
                $(pLabl).removeClass('label-default').removeClass('label-warning').removeClass('label-danger').addClass('label-success');
                availableText = 'Ready For Propose (' + data.availability + '%)';
            }
            
            $('#spn_availability_percentage_' + labelUid).html(availableText);
            $('#spn_availability_percentage_' + labelUid).attr('data-percent', data.availability);

        },
        error: function (xhr, status, statusText) {

            console.log('An error occurred in getAvailabilityPercentage() ::' + xhr.responseText);
        }
    });


}

function setSignumToLabel(selThis) {
    
    var getPDivDetails = $(selThis).closest("#selectDiv");
    var fixInLblId=getPDivDetails.data('div-details');
    var selectedSignum = $(selThis).val();
    
    var pLabl = $('#lbl_selected_signum_' + fixInLblId).closest('.tag');
    
    if (selectedSignum == '' || selectedSignum == '-') {

        $('#lbl_selected_signum_' + fixInLblId).text(selectedSignumText);
        $('#lbl_selected_signum_' + fixInLblId).attr('data-save', 'no');
        $(pLabl).removeClass('label-success').removeClass('label-warning').addClass('label-default');
        if (selectedSignum == '') {
            showHideResetSeacrhButton(selThis, 'hide');
        }

        $('#spn_availability_percentage_' + fixInLblId).html('');
        $('#chk_propose_partially_' + fixInLblId).hide();
        

    } else {

        $('#lbl_selected_signum_' + fixInLblId).text(selectedSignum);
        showHideResetSeacrhButton(selThis, 'show');
        //$('#lbl_selected_signum_' + fixInLblId).append('<a><i class="fa fa-close"></i></i></a>');


        //GET USER AVAILABILITY
        var weId = $('#lbl_selected_signum_' + fixInLblId).data('weid');
        getAvailabilityPercentage(selectedSignum, weId,fixInLblId);

    }
    



}


function createWorkEffortTab(allData, rrid,getParams) {

    var navtab = '<ul class="nav nav-tabs" id="wef-tab">';
    var navtabcontent = '<div class="tab-content">';
     
    var data = [];
        
    $.each(allData.rpefList, function (k, dd) {   // adjust blank work group tab
        if (dd.resourcePositionList.length >= 1) {
            data.push(dd);
        }
    });

    //var data = allData.rpefList;

    $.each(data, function (i, d) {

        
        var workGroupNumber = i + 1;
        onsiteCount = d.onsiteCount;
        remoteCount = d.remoteCount;

        resourcePositionList = d.resourcePositionList;

        //var workEffort = resourcePositionList.split(",");
        
       // console.log("resourcePositionList--->>"+resourcePositionList);

        resourcePositionID = d.rpefId;        
        rrid = rrid;
        navtab = navtab + '<li><a data-toggle="tab" href="#WF' + resourcePositionID + '" data-rpid="' + resourcePositionID + '"> Work Group ' + workGroupNumber + '</a></li>';
        if (i == 0) {
            navtabcontent = navtabcontent + '<div class="tab-pane fade in active" id= "WF' + resourcePositionID + '" >';
        } else {
            navtabcontent = navtabcontent + '<div class="tab-pane fade" id= "WF' + resourcePositionID + '" >';
        }

        navtabcontent = navtabcontent + '<div class="col-lg-3" style="margin:0;padding:15px;">';
        navtabcontent = navtabcontent + '<label style="border:0 !important;font-size:small;">StartDate:</label>';
        navtabcontent = navtabcontent + '<label id="startdate' + resourcePositionID + '" style="border:0 !important;font-size:small;padding:0 5px 0 20px">' + d.startDate + '</label></div>';
        navtabcontent = navtabcontent + '<div class="col-lg-3" style="margin:0;padding:15px;">';
        navtabcontent = navtabcontent + '<label style="border:0 !important;font-size:small;">EndDate:</label>';
        navtabcontent = navtabcontent + '<label id="enddate' + resourcePositionID + '" style="border:0 !important;font-size:small;padding:0 5px 0 20px">' + d.endDate + '</label></div>';
        navtabcontent = navtabcontent + '<div class="col-lg-2" style="margin:0;padding:15px;">';
        navtabcontent = navtabcontent + '<label style="border:0 !important;font-size:small;">OnsiteCount:</label>';
        navtabcontent = navtabcontent + '<label id="onsitecount' + resourcePositionID + '" style="border:0 !important;font-size:small;padding:0 5px 0 20px">' + d.onsiteCount + '</label></div>';
        navtabcontent = navtabcontent + '<div class="col-lg-2" style="margin:0;padding:15px;">';
        navtabcontent = navtabcontent + '<label style="border:0 !important;font-size:small;">RemoteCount:</label>';
        navtabcontent = navtabcontent + '<label id ="remotecount' + resourcePositionID + '" style="border:0 !important;font-size:small;padding:0 5px 0 20px">' + d.remoteCount + '</label></div>';
        navtabcontent = navtabcontent + '<div class="col-lg-2" style="margin:0;padding:15px;">';
        navtabcontent = navtabcontent + '<label style="border:0 !important;font-size:small;">TotalHours:</label>';
        navtabcontent = navtabcontent + '<label id ="totalhours' + resourcePositionID + '" style="border:0 !important;font-size:small;padding:0 5px 0 20px;">' + d.totalHours + '</label></div>';

        //--Create table where number of <tr>= onsitecount+remotecount
        var totaltablerows = d.onsiteCount + d.remoteCount;
        var onsiteRowCount = d.onsiteCount;
        var remoteRowCount = d.remoteCount;
        var locationLabel = "";
      //  var signumsList = [];
        var selectSignumDropDown = '<select name="signumSelectClass" class="signumSelectClass" onchange="setSignumToLabel(this)" data-rpid="' + resourcePositionID + '" style="width:300px"><option value="">Select Smart Signum (Auto-Suggested By System)</option></select>';
        
        var getParams = {};
        getParams.startDate = '2018-01-20';
        getParams.endDate = '2018-01-25';
        getParams.jobStage = 'Job stage 4';
        getParams.domain = 'Radio Access Networks (RAN)';
        getParams.serviceArea = 'SA-NRO-Integration';


        $.isf.ajax({
            url: service_java_URL + "resourceManagement/SearchResourcesByFilters",
            method:'POST',
            data: JSON.stringify(getParams),
            dataType: "json",
            contentType: "application/json; charset=utf-8",
            success: function (data) {
                $.each(data, function (i, d) {
                    $('.signumSelectClass').append('<option value="' + d.Signum + '">' + d.Signum +' - '+d.employeeName + '</option>');
                })

                storeThisSearch();

            },
            error: function (xhr, status, statusText) {
                //  alert("Failed to Get Signum ");
                console.log('An error occurred in getSignum() ::' + xhr.responseText);
            }
        });

        //$(".signumSelectClass").select2();

        navtabcontent = navtabcontent +'<div class="table-responsive row col-lg-12" style="overflow-x:auto;max-height:300px;">'+ '<table id="table' + resourcePositionID + '" class="table table-striped table-bordered table-hover" style="width:100%;" >' +
            '<thead><tr><th style="width:230px;">Resource Request Position Id</th>'+
            //'<th><input type="checkbox" id="checkbox2 class="styled"">&nbsp;SelectAll </th>'+
            '<th style="width:130px;">locationType</th><th style="width:300px;">Signum</th>'
            + '<th>Selected Signum(s)</th>'
            +'</thead><tbody>';


        //---------------------

        for (var rpi in d.resourcePositionList) {
            var rpi_id = d.resourcePositionList[rpi].resourcePositionId;
            var weid = d.resourcePositionList[rpi].WorkEffortID;
            var allocatedStatus=d.resourcePositionList[rpi].remote_onsite;

            navtabcontent = navtabcontent + '<tr>'
                + '<td>' + rpi_id + '</td>'
               // + '<td><div class="checkbox checkbox-success"><input type="checkbox" name="wfCheckbox' + resourcePositionID + '" class="styled""></div></td>'
                + '<td>' + d.resourcePositionList[rpi].remote_onsite + '</td>'
                + '<td style="display:flex;"><div id="selectDiv" data-div-details="' + resourcePositionID + '_' + rpi_id + '">' + selectSignumDropDown + '</div>'
                + '<a style="padding-left:5px;font-size:13px;display:none;" href="#" class="resetPreviousSearchResult" title="Reset Your Search" ><i class="fa fa-refresh"></i></a>'

                //+ '<label class="label label-default selectedLblArr' + resourcePositionID + '" id="lbl_selected_signum_' + resourcePositionID + '_' + rpi_id + '" data-save="no" data-weid="' + weid + '" data-allocatedstatus="' + allocatedStatus + '" >' + selectedSignumText + '</label>'

                + '</td><td>'
                + '<div style="display:flex;">'
                + '<label class="tag label label-default"> '
                + '<span class="selectedLblArr' + resourcePositionID + '" id="lbl_selected_signum_' + resourcePositionID + '_' + rpi_id + '" data-save="no" data-ftepercent="' + allData.ftePercent + '" data-sdate="' + d.startDate + '" data-edate="' + d.endDate + '" data-weid="' + weid + '" data-allocatedstatus="' + allocatedStatus + '">' + selectedSignumText + '</span>'
                //+ '<a><i class="fa fa-close"></i></a>'
                + '</label>'
                + '<div style="display:flex;">'
                + '<input type="checkbox" readonly id="chk_propose_partially_' + resourcePositionID + '_' + rpi_id + '" style="display:none; margin-right: 4px;" > '
                + ' <span class="spn_availability" data-percent="0" id="spn_availability_percentage_' + resourcePositionID + '_' + rpi_id + '" data-post_fixid="' + resourcePositionID + '_' + rpi_id + '"></span>'
                + '</div>'
                + '</div>'
                + '</td>'
                + '</tr>';
        }

        
        
        navtabcontent = navtabcontent + '</tbody></table></div>'
            +'<div class="row" style="padding-top:20px;">'
            + '<div class="col-md-2"><button class="btn btn-primary" id = "Allocatebtn' + resourcePositionID + '" onclick = "sendProposal()" >Send Proposal</button></div>'
            + '<div id="proposalErrors_' + resourcePositionID + '" style="display:none" class="alert alert-danger col-md-9">Error message</div>'
            //+'</div>'

        //$(".signumSelectClass").select2();

        navtabcontent = navtabcontent + '</div>';
        navtabcontent = navtabcontent + '</div>';

    });
    navtab = navtab + '</ul>';
    navtabcontent = navtabcontent + '</div>';

    $("#workEffortDiv").append(navtab).append(navtabcontent);

    //BIND EVENT ON SEARCH RESET
    $('.resetPreviousSearchResult').on('click', function (e) {
        e.preventDefault();
        var that = this;
        var nearSelbox = $(that).closest('td').find('.signumSelectClass');

        nearSelbox.html(storPreviousSerachResult);
        nearSelbox.trigger('onchange');
    });

    $(".signumSelectClass").select2({ width: 'copy' });

    //BIND EVENT ON PERCENTAGE 
    $('.spn_availability').on('click', function () {

        var that = this;
        var postFixId = $(that).data('post_fixid');
        var getNearLbl = $('#lbl_selected_signum_' + postFixId);
        var percent = $(that).attr('data-percent');

        var passedSignum = $(getNearLbl).text();
        var weId = $(getNearLbl).data('weid');

        var passParams = { 'signum': passedSignum, 'weid': weId, 'nearLblUid': postFixId, 'percent': percent };

        getAvailabilityOfUser(passParams);
    });
    

    $('#wef-tab a:first').tab('show');
    $('a[data-toggle="tab"]').on('shown.bs.tab', function (e) {
        currentTabRpid = $(e.target).data('rpid'); // get current tab

        //set sdate and edate for advance search
        var positionSdate = $('#startdate' + currentTabRpid).text();
        var positionEdate = $('#enddate' + currentTabRpid).text();

        $('#advanceSearch_sdate').val(positionSdate);
        $('#advanceSearch_sdate').datepicker({
            format: 'yyyy-mm-dd',
            startDate: positionSdate,
            endDate: positionEdate
        });
        
        $('#advanceSearch_edate').val(positionEdate);
        $('#advanceSearch_edate').datepicker({
            format: 'yyyy-mm-dd',
            startDate: positionSdate,
            endDate: positionEdate
        });
        
    });

}


function storeThisSearch() {
   storPreviousSerachResult = $('.signumSelectClass').html();
}

function clearSelected() {
    document.getElementById("search_select_domain").selectedIndex = "0";
    document.getElementById("search_select_service").selectedIndex = "0";
    document.getElementById("search_select_jobstage").selectedIndex = "0";
    document.getElementById("search_select_jobrole").selectedIndex = "0";
   
    certificateID = '';
    competenceID = '';
    competenceLevel = '';

    if (document.getElementById("search_select_manager") != null && document.getElementById("search_select_manager").selectedIndex != "0")
    {
        document.getElementById("search_select_manager").selectedIndex = "0";
    }

    
    if (document.getElementById("search_select_signum") != null && document.getElementById("search_select_signum").selectedIndex != "0")
    {
        document.getElementById("search_select_signum").selectedIndex = "0";
    }
    
}

function searchWorkEffortTableGrid(resourcePositionID, rrid) {

    resourcePositionID = currentTabRpid;
    rrid = getUrlParameter("RRID");
       

    $('#we_search_result').html(pleaseWaitISF());

    $("#alertErrSAR").css("display", "none");

    var signumv = "#signum" + resourcePositionID;
    var hoursv = "#totalhours" + resourcePositionID;
    var startdatev = "#startdate" + resourcePositionID;
    var enddatev = "#enddate" + resourcePositionID;

    //var startdate = $(startdatev).text();
    //var enddate = $(enddatev).text();

    var startdate = $('#advanceSearch_sdate').val();
    var enddate = $('#advanceSearch_edate').val();

    var hour = $(hoursv).text();
    var signum = $(signumv).text();


    if (document.getElementById("search_select_domain").selectedIndex != "0") {
        Domain = document.getElementById("search_select_domain").value.split("/")[0];
        domainID = '';
        subDomainID = '';
        SubDomain = document.getElementById("search_select_domain").value.split("/")[1];
    } else {
        Domain = document.getElementById("domainlbl").innerHTML.split("/")[0];
        SubDomain = document.getElementById("subdomainlbl").innerHTML;
        domainID = '';
        subDomainID = '';
    }

    if (document.getElementById("search_select_service").selectedIndex != "0") {

        subServiceAreaID = '';
        ServiceArea = document.getElementById("search_select_service").value;
    }
    else {
        subServiceAreaID = '';
        ServiceArea = document.getElementById("servicelbl").innerHTML;
    }
    
   
    if (document.getElementById("search_select_jobstage").selectedIndex != "0") {

        jobStageID = '';
        JobStage = document.getElementById("search_select_jobstage").value;
    } else {
        jobStageID = '';
        JobStage = document.getElementById("jobstagelbl").innerHTML;
    }

    if (document.getElementById("search_select_jobrole").selectedIndex != "0") {
        jobRoleID = '';
        JobName = document.getElementById("search_select_jobrole").value;
    } else {
        jobRoleID = '';
        JobName = document.getElementById("jobrolelbl").innerHTML;
    }
    
    
    var managerSignum = '';

    
    if (document.getElementById("domain_checkbox").checked == false) {
        domainID = '';
        Domain = '';
        subDomainID = '';
        SubDomain = '';
    }
    if (document.getElementById("service_checkbox").checked == false) {
        subServiceAreaID = '';
        ServiceArea = '';
    }
    
    if (document.getElementById("jobstage_checkbox").checked == false) {
        jobStageID = '';
        JobStage = '';
    }
    if (document.getElementById("jobrole_checkbox").checked == false) {
        jobRoleID = '';
        JobName = '';
    }

    if (document.getElementById("search_select_manager") != null  && document.getElementById("search_select_manager").selectedIndex != "0") {
        managerSignum = document.getElementById("search_select_manager").value;

        jobStageID = '';
        jobRoleID = '';
        certificateID = '';
        competenceID = '';
        competenceLevel = '';
        signum = '';
        subDomainID = '';
        subServiceAreaID = '';
        domainID = '';
        Domain = '';
        ServiceArea = '';
        SubDomain = '';
        JobStage = '';
        JobName = '';

    }
    else if (document.getElementById("search_select_signum") != null && document.getElementById("search_select_signum").selectedIndex != "0") {
        signum = document.getElementById("search_select_signum").value;

        managerSignum = '';
        jobStageID = '';
        jobRoleID = '';
        certificateID = '';
        competenceID = '';
        competenceLevel = '';
        subDomainID = '';
        subServiceAreaID = '';
        domainID = '';
        Domain = '';
        ServiceArea = '';
        SubDomain = '';
        JobStage = '';
        JobName = '';
      
    }
   
    var n = competenceIDChk.lastIndexOf(",");
    var competenceIDChkBox = competenceIDChk.substring(0, n);

    var competenceIDChkList = competenceIDChkBox.split(",");

    competenceID = '';
    competenceLevel = '';

    for (var i = 0; i < competenceIDChkList.length; i++) {
        var competanceChk = competenceIDChkList[i];
        if ($(competanceChk).prop('checked')) {
            var dt = competanceChk.split("_");
            competenceID = competenceID + dt[3] + ",";
            competenceLevel = competenceLevel + "\"" + dt[2] + "\"" + ",";

        }
    }

    var m = certificateIDChk.lastIndexOf(",");
    var certificateIDChkBox = certificateIDChk.substring(0, m);

    var certificateIDChkList = certificateIDChkBox.split(",");
    certificateID = "";
    for (var i = 0; i < certificateIDChkList.length; i++) {
        var certificateChk = certificateIDChkList[i];
        if ($(certificateChk).prop('checked')) {
            var m = certificateChk.lastIndexOf("_");
            var cid = certificateChk.substring(m, certificateChk.length)
            certificateID = certificateID + cid + ",";
        }
    }

    certificateID = "";

    competenceID = competenceID.substring(0, competenceID.lastIndexOf(","));
    competenceLevel = competenceLevel.substring(0, competenceLevel.lastIndexOf(","));
    certificateID = certificateID.substring(0, certificateID.lastIndexOf(","));

    var competenceIDStr = "[" + competenceID + "]";
    var competenceLevelStr = "[" + competenceLevel + "]";
    var certificateIDStr = "[" + certificateID + "]";
    
    certificateIDStr = "";

    var service_data = "{\"resourceRequestID\":\"" + rrid + "\"," +
        "\"jobStageID\":\"" + jobStageID +
        "\",\"jobRoleID\":\"" + jobRoleID +
        "\",\"certificateID\":\"" + certificateIDStr +
        "\",\"competenceString\":" + competenceIDStr + 
        ",\"competenceLevel\":" + competenceLevelStr + "," + 
        "\"signum\":\"" + signum +
       "\",\"subDomainID\":\"" + subDomainID + "\"," +
        "\"subServiceAreaID\":\"" + subServiceAreaID +
        "\",\"startDate\":\"" + startdate+
        "\",\"endDate\":\"" + enddate+
        "\",\"hours\":\"" + hour +
        "\",\"managerSignum\":\"" + managerSignum +
        "\",\"domainID\":\"" + domainID +
        "\",\"domain\":\"" + Domain +
        "\",\"serviceArea\":\"" + ServiceArea +
        "\",\"subDomain\":\"" + SubDomain +
       "\",\"jobStage\":\"" + JobStage +
        "\",\"jobName\":\"" + JobName +
        "\"}";
      
    $.isf.ajax({
        
        url: service_java_URL + "/resourceManagement/SearchResourcesByFilter",
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

       //var table = '#table' + resourcePositionID;
       // if ($.fn.dataTable.isDataTable(table)) {
       //     $(table).destroy();
       //     $(table).empty();

       // }
        //var tableRowDiv = '#tableRowDiv' + resourcePositionID;
        //$(tableRowDiv).remove();
        
        $('#we_search_result').html('');

        createWorkEffortTableGrid(data, resourcePositionID, rrid);
                
 
    }
    function AjaxFailed(result) {
        $("#alertErrSAR").css("display", "block");
    //   alert("Error :" + result.responseText);
        var tableRowDiv = '#tableRowDiv' + resourcePositionID;
        $(tableRowDiv).remove();

    }

}

function createWorkEffortTableGrid(data, resourcePositionID, rrid) {

    var tabID = 'search_table' + resourcePositionID ;
    
    var tablediv = '<div class="row" id="tableRowDiv' + resourcePositionID + '">';
    var tablediv = tablediv + '<div class="col-lg-12">';
    tablediv = tablediv + '<div class="table-responsive">';
    tablediv = tablediv + '<table id = "' + tabID + '" class="table table-striped table-bordered table-hover" style="width:100%;" >';
    tablediv = tablediv + '<thead><tr><th >Action</th><th>Signum</th><th>Name</th><th>Availability Against Demand %</th><th>ManagerSignum</th><th>ManagerName</th></tr></thead>';
    tablediv = tablediv + '<tfoot><tr><th ></th><th>Signum</th><th>Name</th><th>Availability Against Demand %</th><th>ManagerSignum</th><th>ManagerName</th></tr></tfoot>';

    tablediv = tablediv + '<tbody>';

    for (var i = 0; i < data.length; i++) {
        tablediv = tablediv + '<tr class="odd gradeX">';
        tablediv = tablediv + '<td><input type="checkbox" value="' + data[i].Signum + '"   id = "resourcechk_' + resourcePositionID + '_' + i + '" onchange="changeSelectionOfSignum(this)" data-availability="' + data[i].Availability + '" data-name="' + data[i].employeeName + '">';
        //tablediv = tablediv + '<select style="visibility:hidden" class = "myClass" id = "site_' + resourcePositionID + '_' + i + '" onchange="selectSite(this,' + resourcePositionID + ',' + i + ')"><option value="0">Select one</option><option value="onsite">Onsite</option><option value="remote">Remote</option></select></td>';
        tablediv = tablediv + '<td><a href="javascript:void(0)" style="color:blue;" onclick="getEmployeeDetailsBySignum(\'' + data[i].Signum + '\');">' + data[i].Signum + '</a></td>';
        tablediv = tablediv + '<td>' + data[i].employeeName + '</td>';
        tablediv = tablediv + '<td>' + data[i].Availability + '</td>';
        tablediv = tablediv + '<td><a href="javascript:void(0)" style="color:blue;" onclick="getEmployeeDetailsBySignum(\'' + data[i].managerSignum + '\');">' + data[i].managerSignum + '</a></td>';

        tablediv = tablediv + '<td>' + data[i].managerName + '</td>';
        //tablediv = tablediv + '<td>' + data[i].JobStage + '</td>';
        
        tablediv = tablediv + '</tr>';
    }
    tablediv = tablediv + '</tbody>';
    tablediv = tablediv + '<tfoot><tr>';
   // tablediv = tablediv + '<td colspan="7"><button class="btn btn-primary" id = "Allocatebtn' + resourcePositionID + '" onclick = "updateAllocatedResources(' + resourcePositionID + ')" >Allocate</button></td>';
    tablediv = tablediv + '</tr><tr><td colspan="7">';
    tablediv = tablediv + '<div id="allocatesuccessSAR" style="display:none" class="alert alert-danger">';
   
    tablediv = tablediv + '<strong>Success!</strong>Resources has been allocated successfully</div>';
    tablediv = tablediv + '<div id="allocatefailureSAR" style="display:none" class="alert alert-danger"><strong>Failure!</strong>Resources could not be allocated</div>';
    tablediv = tablediv + '</td></tr></tfoot> ';
    tablediv = tablediv + '</table>';
    tablediv = tablediv + '</div>';
    tablediv = tablediv + '</div>';
    tablediv = tablediv + '</div>';

    //var t = '#WF' + resourcePositionID;

    var t = '#we_search_result';
    $(t).append(tablediv);

    var Dtable = $('#' + tabID).DataTable({
        destroy: true,
        responsive: true,
        order: [1],
        columnDefs: [{ orderable: false, targets: [0] }],
        dom: 'Bfrtip',
        buttons: [
         'colvis', 'excel'
        ],
        initComplete: function () {

                //CREATE TEXT BOXES FOR INDIVIDUAL COLUMN SEARCH
            $('#' + tabID + ' tfoot th').each(function () {
                    var title = $(this).text();
                    if (title != "") {
                        $(this).html('<input type="text" />');
                    }
                });

                var api = this.api();

                // Apply the search
                api.columns().every(function () {
                    var that = this;

                    $('input', this.footer()).on('keyup change', function () {
                        if (that.search() !== this.value) {
                            that
                              .columns($(this).parent().index() + ':visible')
                              .search(this.value)
                              .draw();
                        }
                    });
                });

                //APPLY X SCROLL
                $('#' + tabID).wrap('<div class="tabWrap" style="overflow:auto;width:100%;"></div>');
            }
    });

    


}

function sendProposal() {

    var d = [];
    var uniqueSignumArr = [];
    var duplicateFound = false;
    var proposalErrorDivId = 'proposalErrors_' + currentTabRpid;
    var previousBtn = '<button onclick="window.history.back()">Go to Previous Page</button>';

    $('.selectedLblArr' + currentTabRpid).each(function () {
        var getLbl = $(this).closest('.tag.label');
        $(getLbl).css('border-right', '0px solid red');
    });


    $('.selectedLblArr' + currentTabRpid).each(function () {

        if ($(this).attr('data-save') == 'yes') {
        
            var weid = $(this).data('weid');
            var signum = $(this).text();
            var allocatedStatus = $(this).data('allocatedstatus');
            var loggedInUser = signumGlobal;
            var sDate = $(this).attr('data-sdate');
            var eDate = $(this).attr('data-edate');
            var ftePercent=$(this).data('ftepercent');

            
            var posInArr = $.inArray(signum, uniqueSignumArr);
            if (posInArr < 0) {
                var obj = { "weid": weid, "signum": signum, "allocatedStatus": allocatedStatus, "loggedInUser": loggedInUser, "startDate": sDate, "endDate": eDate, "positionStatus": "Proposed", "ftePercentage": ftePercent };
                d.push(obj);
                uniqueSignumArr.push(signum);
            } else {
                duplicateFound = true;
                var getLbl = $(this).closest('.tag.label');
                $(getLbl).css('border-right', '12px solid red');
            }
            
        }
    });
    
    if (d.length <= 0) {
        $('#' + proposalErrorDivId).removeClass('alert-success').addClass('alert-danger');
        $('#' + proposalErrorDivId).html('Please select signum(s). ' + previousBtn).show();
        return;
    }

    if (duplicateFound) {
        $('#' + proposalErrorDivId).removeClass('alert-success').addClass('alert-danger');
        $('#' + proposalErrorDivId).html('Some duplicate signums are found. (Check in Red)').show();
    } else {

        //console.log(d);
        //return;

        $.isf.ajax({
            url: service_java_URL + "activityMaster/updateProposedResources",
            method: 'POST',
            data: JSON.stringify(d),
            dataType: "json",
            contentType: "application/json; charset=utf-8",
            success: function (data) {

                $('#' + proposalErrorDivId).removeClass('alert-danger').addClass('alert-success');
                $('#' + proposalErrorDivId).html('Proposal sent successfully ' + previousBtn).show();

                $('.selectedLblArr' + currentTabRpid).each(function () {
                    if ($(this).attr('data-save') == 'yes') {
                        $(this).closest('tr').css({ 'pointer-events': 'none', 'backgroundColor': '#ccc' });
                        $(this).attr('data-save', 'no');
                    }
                });

            },
            error: function (xhr, status, statusText) {
                //  alert("Failed to Get Signum ");
                console.log('An error occurred in sendProposal() ::' + xhr.responseText);
            }
        });
    }



}


function updateAllocatedResources(resourcePositionID) {
    /*
    Now we need
    #selectDiv'+resourcePositionID+'-'+rowNo+' .signumSelectClass for selectedRowData
    
    */
    $("#allocatesuccessSAR").css("display", "none");

    $("#allocatefailureSAR").css("display", "none");

    var table = '#table' + resourcePositionID;
    var result = $("tr:has(:checked)");
    $(table).find('input[type="checkbox"]:checked');

    var selectedRowData = '';
    $(table).find('tr').each(function () {
        var row = $(this);
        if (row.find('input[type="checkbox"]').is(':checked')) {//$(this).closest('tr').find('td:nth-child(1)').text();  $( "#myselect option:selected" ).text();
            selectedRowData = selectedRowData + '{' + $(this).find('.signumSelectClass :selected').html() + $(this).children().slice(1).map(function () {
                return $(this).text().trim()
            }).get() + '},';

        }
    });

   
    var selectedRecords = selectedRowData.split("},{");

    var service_data = "[";

    for (var i = 0; i < selectedRecords.length; i++) {
        var record = selectedRecords[i];        
        var allocatedStatus = '';
        var signum = '';
        if(i==0)
            signum = (record.split(",")[0]).split("{")[1];
        else
            signum = (record.split(",")[0]);
        allocatedStatus = (record.split(",")[1]);

        

      //  if (i == (selectedRecords.length - 1)) {
            for (var k = 0; k < resourcePositionList.length; k++) {
                var rec = resourcePositionList[k];
                var remote_onsite = resourcePositionList[k].remote_onsite;                
                if (allocatedStatus.toLowerCase() == remote_onsite.toLowerCase()) {
                    workEffortId = resourcePositionList[k].WorkEffortID;
                    resourcePositionList.splice(k, 1);
                    service_data = service_data + "{\"weid\":\"" + workEffortId + "\",\"signum\":\"" + signum + "\",\"allocatedStatus\":\"" + allocatedStatus + "\",\"loggedInUser\":\"" + signumGlobal + "\"}";
                }                 
            }
  //      }
//        else {
//            for (var l = 0; l < resourcePositionList.length; l++) {
//                var rec = resourcePositionList[l];
////                console.log("rec>>" + rec);
//                var remote_onsite = resourcePositionList[l].remote_onsite;              
//                if (allocatedStatus.toLowerCase() == remote_onsite.toLowerCase()) {
//                    workEffortId = resourcePositionList[l].WorkEffortID;
//                    resourcePositionList.splice(l, 1);
//                    service_data = service_data + "{\"weid\":\"" + workEffortId + "\",\"signum\":\"" + signum + "\",\"allocatedStatus\":\"" + allocatedStatus + "\",\"loggedInUser\":\"" + localStorage.getItem("userLoggedIn") + "\"},";   
//                }                
//            }            
//        }
       // console.log("Request updateAllocatedResources -->>" + service_data);
    }
    service_data = service_data + "]";


        
    $.isf.ajax({
        url: service_java_URL + "activityMaster/updateAllocatedResources",
       context: this,
        crossdomain: true,
        processData: true,
        contentType: 'application/json',
        type: 'POST',
        data: service_data,
        xhrFields: {
            withCredentials: false
        },
        success: AllocateAjaxSucceeded,
        error: AjaxFailed
    });
    function AllocateAjaxSucceeded(data, textStatus) {
      // alert("Successfully Executed : Allocation Status" + data);
        $("#allocatesuccessSAR").css("display", "block");
        searchWorkEffortTableGrid(resourcePositionID, rrid);
    }
    function AjaxFailed(result) {
       // alert("Error :" + result.responseText);
        $("#allocatefailureSAR").css("display", "block");
    }
}

function createCompetenceLevel(data) {
  
 if (data.competenceName!=null){
        var tablediv = '';
        tablediv = tablediv + '<tr>';
        tablediv = tablediv + '<th><input  type="checkbox" checked id="competencelevel_checkbox_' + data.competenceLevel +'_' + data.competenceId + '">';
        tablediv = tablediv + '<label id = "competencelevel_' + data.competenceId + '"  for="filter">' + data.competenceName + '</label></th>';
        $("#tBodyCompetence").append(tablediv);
        competenceIDChk = competenceIDChk + "#competencelevel_checkbox_" + data.competenceLevel + '_' + data.competenceId + ",";
    }
   
    
}

function createCertificateLevel(data) {
    
    if (data.CertificateName != null) {
        var tablediv = '';
        tablediv = tablediv + '<tr>';
        tablediv = tablediv + '<th><input  type="checkbox" checked id="certificatelevel_checkbox_' + data.CertificateID + '" >';
        tablediv = tablediv + '<label id = "competencelevel_' + data.CertificateID + '" for="filter">' + data.CertificateName + '</label></th>';
        $("#tBodyCertificate").append(tablediv);
        certificateIDChk = certificateIDChk + "#certificatelevel_checkbox_" + data.CertificateID +",";
    }
}

function setCertificateID(CertificateIDChk, id) {
    var certificatechk = CertificateIDChk.id;

    if (document.getElementById(certificatechk).checked == true) {
        certificateID = certificateID + id + ",";
       
    }
    else {
        certificateID = certificateID.replace(id, ''); 
       
    } 
}

function setCompetenceIDLevel(competenceIDChk, id , level) {
    var competencechk = competenceIDChk.id;
   
    if (document.getElementById(competencechk).checked == true) {
        competenceID = competenceID  + id + ",";
        competenceLevel = competenceLevel + "\"" + level + "\"" + ",";
        
     }
    else {
        competenceID = competenceID.replace(id, ''); 
    }
   
}

function changeSelectionOfSignum(chkbox, resourcePositionID, i) {


    var checkedOrNot=$(chkbox).prop('checked');
    var signumToBeAdd = $(chkbox).val();
    var nameWithSignum=$(chkbox).data('name');
    var availabilityOfSignum = $(chkbox).data('availability');

    if (checkedOrNot) {
        
        //FIRST REMOVE CHECKED SIGNUM - BEFORE INDERT IT INTO SELECT BOX
        $('.signumSelectClass[data-rpid=' + currentTabRpid + '] option[value="' + signumToBeAdd + '"]').each(function () {
            $(this).remove();
        });

        //NOW ADD CHECKED SIGNUM INTO SELECT BOX
        $('.signumSelectClass[data-rpid=' + currentTabRpid + ']').each(function () {

            var selValue = $(this).val();

            if (selValue == '' || selValue == '-') {

                showHideResetSeacrhButton(this, 'show');

                if (selValue == '-') {
                    $(this).append('<option value="' + signumToBeAdd + '">' + signumToBeAdd + ' - ' + nameWithSignum + '</option>');
                } else {
                    $(this).html('<option value="-">Assign Signum From Your Search</option><option value="' + signumToBeAdd + '">' + signumToBeAdd + ' - ' + nameWithSignum + '</option>');
                }

            }
        });
        
        storePreFilledSearchResult = storPreviousSerachResult;
        storeThisSearch();
      

    } else {
        storPreviousSerachResult = storePreFilledSearchResult;
        $('.signumSelectClass[data-rpid=' + currentTabRpid + '] option[value="' + signumToBeAdd + '"]').each(function () {

            $(this).remove();
           
        });
    }
    
    

    
}

function selectSite(chkbox, resourcePositionID, i) {

    var resourcechk = '#resourcechk_' + resourcePositionID + '_' + i;

    var site = 'site_' + resourcePositionID + '_' + i;

    
        if (document.getElementById(site).value == 'onsite') {
            if (onsiteCounter >= onsiteCount) {
                alert("Max onsite resource allocation is " + onsiteCount);

                $(resourcechk).prop('checked', false);
                document.getElementById(site).selectedIndex = "0";
                document.getElementById(site).style.visibility = 'hidden';

            } else {

                document.getElementById(site).style.visibility = 'visible';
                onsiteCounter = onsiteCounter + 1;
            }
        } else if (document.getElementById(site).value == 'remote') {
            if (remoteCounter >= remoteCount) {
                alert("Max remote resource allocation is " + remoteCount);

                $(resourcechk).prop('checked', false);
                document.getElementById(site).selectedIndex = "0";
                document.getElementById(site).style.visibility = 'hidden';

            } else {

                document.getElementById(site).style.visibility = 'visible';
                remoteCounter = remoteCounter + 1;
            }
        }

}

function showDropdown(dropdownname) {
    
    var navtabcontent = '';
    $("#dropdown_manager_signum").css('display', 'block');

    if (dropdownname == 'manager') {
        $("#dropdown_manager_signum").html('');
        navtabcontent = navtabcontent + '<select id="search_select_manager" class="form-control select2able"  name="search_select_manager"><option value="0">Select Manager</option>';
        navtabcontent = navtabcontent + '</select>';

        getAllLineManagers();
        $("#dropdown_manager_signum").append(navtabcontent);
        $('#domain_checkbox').prop('checked', false); 
        $('#service_checkbox').prop('checked', false); 
        $('#jobstage_checkbox').prop('checked', false); 
        $('#jobrole_checkbox').prop('checked', false); 

        var n = competenceIDChk.lastIndexOf(",");
        var competenceIDChkBox = competenceIDChk.substring(0, n);

        var competenceIDChkList = competenceIDChkBox.split(",");

        for (var i = 0; i < competenceIDChkList.length; i++) {
            var competanceChk = competenceIDChkList[i];
            $(competanceChk).prop('checked', false);

        }

        var m = certificateIDChk.lastIndexOf(",");
        var certificateIDChkBox = certificateIDChk.substring(0, m);

        var certificateIDChkList = certificateIDChkBox.split(",");

        for (var i = 0; i < certificateIDChkList.length; i++) {
            var certificateChk = certificateIDChkList[i];
            $(certificateChk).prop('checked', false);
        }

        $("#search_select_manager").select2();
    } else if (dropdownname == 'signum') {
       
        $("#dropdown_manager_signum").html(pleaseWaitISF({ text: 'Loading Signums ...' ,spinCss:'font-size:12px;'}));
        //navtabcontent = navtabcontent + '<select id="search_select_signum" class="form-control select2able"  name="search_select_signum"><option value="0">Select Signum</option>';
        //navtabcontent = navtabcontent + '</select>';

        getSignum();

        //$("#dropdown_manager_signum").append(navtabcontent);
        $('#domain_checkbox').prop('checked', false);
        $('#service_checkbox').prop('checked', false);
        $('#jobstage_checkbox').prop('checked', false);
        $('#jobrole_checkbox').prop('checked', false); 

        var n = competenceIDChk.lastIndexOf(",");
        var competenceIDChkBox = competenceIDChk.substring(0, n);

        var competenceIDChkList = competenceIDChkBox.split(",");

        for (var i = 0; i < competenceIDChkList.length; i++) {
            var competanceChk = competenceIDChkList[i];
            $(competanceChk).prop('checked', false);
        }

        var m = certificateIDChk.lastIndexOf(",");
        var certificateIDChkBox = certificateIDChk.substring(0, m);

        var certificateIDChkList = certificateIDChkBox.split(",");

        for (var i = 0; i < certificateIDChkList.length; i++) {
            var certificateChk = certificateIDChkList[i];
            $(certificateChk).prop('checked', false);
        }
        //$("#search_select_signum").select2();
  } else if (dropdownname == 'rrid') {
        $("#dropdown_manager_signum").html('');
        $("#dropdown_manager_signum").css('display', 'none');
        $("#domain_checkbox").prop('checked', true);
        $('#service_checkbox').prop('checked', true);
        $('#jobstage_checkbox').prop('checked', true);
        $('#jobrole_checkbox').prop('checked', true);

        var n = competenceIDChk.lastIndexOf(",");
        var competenceIDChkBox = competenceIDChk.substring(0, n);

        var competenceIDChkList = competenceIDChkBox.split(",");

        for (var i = 0; i < competenceIDChkList.length; i++) {
            var competanceChk = competenceIDChkList[i];
            $(competanceChk).prop('checked', true);
        }

        var m = certificateIDChk.lastIndexOf(",");
        var certificateIDChkBox = certificateIDChk.substring(0, m);

        var certificateIDChkList = certificateIDChkBox.split(",");

        for (var i = 0; i < certificateIDChkList.length; i++) {
            var certificateChk = certificateIDChkList[i];
            $(certificateChk).prop('checked', true);
        }
       
        
    }

}
