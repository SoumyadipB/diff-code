var IsISFDesktopRunning = 1;
var preferredTimeZone = "";

if (signumGlobal == null) {

    var requestId = getUrlParameter("requestId");

    var pgid = getUrlParameter("pgid");

    window.location.href = UiRootDir + "/login?pgid=" + pgid + "&requestId=" + requestId;

}

$(document).on('change', '#user_profile', function (event, defaultPage) {

    if (defaultPage == undefined)
        defaultPage = 28;

    var selectbox = document.getElementById("landing_page");
    var i;
    for (i = selectbox.options.length - 1; i >= 0; i--) {
        selectbox.remove(i);
    }
    let selectedUserProfile = $("#user_profile").val();
    $.isf.ajax({
        url: service_java_URL + "accessManagement/getPageAccessDetailsByAccessProfileId?accessProfileId=" + selectedUserProfile,
        success: function (data) {
            for (let i = 0; i < data.length; i++) {
                let jsonData = encodeURIComponent(data[i].SubMenuHref);
                if (data[i].CapabilityPageID == defaultPage) {
                    $("#landing_page").append('<option value="' + data[i].CapabilityPageID + '" selected="selected"' + 'data-details=' + jsonData + '>' + data[i].SubMenuTitle + '</option>');
                }
                else
                    $("#landing_page").append('<option value="' + data[i].CapabilityPageID + '" data-details=' + jsonData + '>' + data[i].SubMenuTitle + '</option>');


            }
        },
        error: function (xhr, status, statusText) {
            console.log('An error occurred');
        }

    });

});

//Gets saved Location Details of the User
function getSavedLocationForUser() {
    pwIsf.addLayer({ text: C_PLEASE_WAIT });

    $.isf.ajax({
        type: C_API_GET_REQUEST_TYPE,
        url: `${service_java_URL}accessManagement/getUserLocationBySignum`,
        success: AjaxSucceeded,
        error: function (msg) {
            pwIsf.alert({ msg: `${C_COMMON_ERROR_MSG} : while fetching user location`, type: ERRORTEXT });
        },
        complete: function (msg) {
            pwIsf.removeLayer();
        }
    });
    function AjaxSucceeded(data, textStatus) {
        let country = data.countryID;
        let city = data.cityName;
        let cityID = data.cityID;
        let address = data.userAddress;
        let currentLocationOptionID = data.currentLocationOptionID;
        let locationTypeID = data.locationTypeID;
        let marketArea = '', customerName = '', startDate = '', endDate = '', countryCode = '', contactNumber = '';

        if (data != "") {
            if (currentLocationOptionID != undefined && currentLocationOptionID != '1') {
                marketArea = data.marketArea;
                customerName = data.customerName;
                startDate = data.startDate;
                let from = startDate.split("-");
                startDate = new Date(from[0], from[1] - 1, from[2]);
                startDate = formatDateforLoc(startDate);
                endDate = data.endDate;
                let to = endDate.split("-");
                endDate = new Date(to[0], to[1] - 1, to[2])
                endDate = formatDateforLoc(endDate);
                countryCode = data.countryCode;
                contactNumber = data.contactNumber;
                $("input[name=site][value=" + currentLocationOptionID + "]").prop('checked', 'checked').trigger('change');
                $('#select_user_location_country').val(country).trigger('change');
                $('#select_user_location_city').val(city);
                $('#user_address').val(address);
                $('#user_city').val(cityID);
                if (locationTypeID != 0) {
                    $('#select_user_location_type').val(locationTypeID).trigger('change');
                }
                $('#select_market_area').val(marketArea);
                $('#customer_name').val(customerName);
                $('#locStartDate').val(startDate);
                $('#locEndDate').val(endDate);
                $('#countryCode').val(countryCode);
                $('#contactNumber').val(contactNumber);
            }
            else {
                $("input[name=site][value=" + currentLocationOptionID + "]").attr('checked', true).trigger('change');
                $('#select_user_location_country').val(country).trigger('change');
                $('#select_user_location_city').val(city);
                $('#user_address').val(address);
                $('#user_city').val(cityID);
                if (locationTypeID != 0) {
                    $('#select_user_location_type').val(locationTypeID).trigger('change');
                }

                $('#select_market_area').val(marketArea);
                $('#customer_name').val(customerName);
                $('#locStartDate').val(startDate);
                $('#locEndDate').val('');
                $('#countryCode').val(countryCode);
                $('#contactNumber').val(contactNumber);
            }
        }
        else {
            $('#locEndDate').val('');
            $('#countryCode').val('');
            $('#contactNumber').val('');
            $('#customer_name').val('');
        }

    }
}

function formatDateforLoc(date) {
    var m = ("0" + (date.getMonth() + 1)).slice(-2); // in javascript month start from 0.
    var d = ("0" + date.getDate()).slice(-2); // add leading zero 
    var y = date.getFullYear();
    return d + '/' + m + '/' + y;
}

//Opens Location Modal on Layout
function openLocationModal(signum, username) {
    $('#locationTitle').text('');
    $('#locationModal').modal('show');
    $('#locationTitle').show();
    $('#locationTitle').text(signum + " (" + username + ")");
    $("#select_user_location_country").select2({
        placeholder: "Select a Country"
    });
    $('#select_user_location_country').val('');
    $('#select_user_location_city').val('');
    $('#user_city').val('');
    $('#user_address').val('');
    getCountriesForLocation();
    //Call LocaitonTypeAPI 
    getUserLocationType();
    getSavedLocationForUser();
}

function getUserLocationType() {

    pwIsf.addLayer({ text: "Please wait..." });

    $.isf.ajax({
        type: "GET",
        async: false,
        url: service_java_URL + "accessManagement/getLocationType",

        success: function (data) {

            let locOption = ''; let locValue = '';
            $('#select_user_location_type').html('');
            for (let i = 0; i < data.length; i++) {

                if (data[i].selected) {
                    locValue = data[i].locationTypeID;

                }

                locOption += '<option value="' + data[i].locationTypeID + '">' + data[i].locationType + '</option>';
            }

            $('#select_user_location_type').append(locOption);

            $('#select_user_location_type').val(locValue);
            $('#select_user_location_type').select2().trigger('change');


        },
        error: function (msg) {
            console.log(msg);
        },
        complete: function (msg) {
            pwIsf.removeLayer();
        }
    });
}

//Gets the country list
function getCountriesForLocation() {

    $.isf.ajax({
        url: service_java_URL + "activityMaster/getCountries",
        async: false,
        success: function (data) {
            $('#select_user_location_country').html('');
            $('#select_user_location_country').append('<option value="0">Select Country</option>')
            $.each(data.responseData, function (i, d) {

                $('#locationModal #select_user_location_country').append('<option value="' + d.countryID + '">' + d.countryName + '</option>');
            })
        },
        error: function (xhr, status, statusText) {
            console.log("Fail " + xhr.responseText);
        }
    });
}

//Gets Country w.r.t. the Country ID
function getCitiesByCountryID(countryID) {

    $("#select_user_location_city").val('');
    $("#select_user_location_city").autocomplete({

        appendTo: "#locationModal",
        source: function (request, response) {
            let cityJson = new Object();
            cityJson.term = request.term;
            cityJson.countryID = countryID;
            $.isf.ajax({
                url: service_java_URL + "accessManagement/getCityByCountryID",
                type: "POST",
                contentType: "application/json",
                data: JSON.stringify(cityJson),
                success: function (data) {

                    $("#select_user_location_city").autocomplete().addClass("ui-autocomplete-loading");

                    var result = [];
                    if (data.length == 0) {
                        pwIsf.alert({ msg: 'Please enter a valid City', type: 'warning' });
                        $('#select_user_location_city').val("");
                        $('#select_user_location_city').focus();
                        response(result);
                    }
                    else {
                        $.each(data, function (i, d) {
                            result.push({
                                "label": d.cityName,
                                "value": d.cityID

                            });
                        })

                        response(result);
                        $("#select_user_location_city").autocomplete().removeClass("ui-autocomplete-loading");
                    }

                }
            });
        },
        change: function (event, ui) {
            if (ui.item === null) {
                $(this).val('');
                $('#select_user_location_city').val('');
            }
        },
        select: function (event, ui) {
            $("#select_user_location_city").val(ui.item.value);
            $("#user_city").val(ui.item.value);
            $(this).val(ui.item.label);
            return false;
        },
        minLength: 3

    });
    $("#select_user_location_city").autocomplete("widget").addClass("fixedHeight");
}

//Sets Session Variable for Location of User
//function set_Session_Values_location(val) {
//    let values = JSON.stringify(val);
//    $.ajax({
//        contentType: C_CONTENT_TYPE_APPLICATION_JSON,
//        async: false,
//        type: C_API_POST_REQUEST_TYPE,
//        data: values,
//        url: `${rootDir}/Data/SetSessionData`,
//        success: function (data) {
//            //BLANK
//        },
//        error: function (xhr, status, statusText) {
//            console.log(`${C_COMMON_ERROR_MSG}`);
//        }
//    });
//}

//save location for User
function saveUserLocation() {
    let currentLocationOptionID = $("input[name='site']:checked").val()
    let countryID = $('#select_user_location_country option:selected').val();
    let cityID = $('#user_city').val();
    let city = $('#select_user_location_city').val();
    let locationTypeID = $('#select_user_location_type').val();
    let address = $('#user_address').val();

    let marketArea = '', customerName = '', startDate = '', endDate = '', contactNumber = '', countryCode = '';

    if (currentLocationOptionID != '1') {
        marketArea = $('#select_market_area').val();
        customerName = $('#customer_name').val();
        startDate = $('#locStartDate').val();
        endDate = $('#locEndDate').val();
        countryCode = $('#countryCode').val();
        contactNumber = $('#contactNumber').val();
    }

    if (countryID === '' || countryID === null || countryID === undefined || countryID === 0) {
        pwIsf.alert({ msg: 'Please enter Country', type: 'warning' });
    }
    else if (city === '' || city === null || city === undefined) {
        pwIsf.alert({ msg: 'Please enter City', type: 'warning' });
        $('#user_city').val('');
    }
    else if (locationTypeID === '' || locationTypeID === null || locationTypeID === undefined) {
        pwIsf.alert({ msg: 'Please select location type', type: 'warning' });
    }
    else if (currentLocationOptionID !== '1' && marketArea === '' || marketArea === null || marketArea === undefined) {
        pwIsf.alert({ msg: 'Please select Market Area', type: 'warning' });
    }
    else if (currentLocationOptionID !== '1' && startDate === '' || startDate === null || startDate === undefined) {
        pwIsf.alert({ msg: 'Please select start date', type: 'warning' });
    }
    else if (currentLocationOptionID !== '1' && endDate === '' || endDate === null || endDate === undefined) {
        pwIsf.alert({ msg: 'Please select end date', type: 'warning' });
    }
    else if (currentLocationOptionID !== '1' && customerName === '' || customerName === null || customerName === undefined) {
        pwIsf.alert({ msg: 'Please enter customer name.', type: 'warning' });
    }
    else if (contactNumber !== '' && (contactNumber.length < 10 || contactNumber.length > 15)) {
        pwIsf.alert({ msg: 'Please enter correct contact number.', type: 'warning' });
    }
    else if (countryCode !== '' && (countryCode.length > 5)) {
        pwIsf.alert({ msg: 'Maximum 5 Digits in Country Code is allowed.', type: 'warning' });
    }
    else if (countryCode !== '' && contactNumber === '') {
        pwIsf.alert({ msg: 'If Country Code is entered,Contact Number can not be blank.', type: 'warning' });
    }
    else {
        let locationJson = new Object();
        locationJson.cityID = cityID;
        locationJson.countryID = countryID;
        locationJson.userAddress = address;
        locationJson.currentLocationOptionID = currentLocationOptionID;
        locationJson.locationTypeID = locationTypeID;

        if (currentLocationOptionID != '1') {
            locationJson.marketArea = marketArea;
            locationJson.customerName = customerName;

            let from = startDate.split("/");
            startDate = new Date(from[2], from[1] - 1, from[0]);
            locationJson.startDate = formatted_date(startDate);

            let to = endDate.split('/');
            endDate = new Date(to[2], to[1] - 1, to[0]);
            locationJson.endDate = formatted_date((endDate));
            locationJson.countryCode = countryCode;
            locationJson.contactNumber = contactNumber;
        }
        saveUserLocationApiCall(locationJson);
    }
}

function calculateDateDifference(locEndDate, numOfDays) {

    let to = new Date();
    let futureDate = to;

    locEndDate = locEndDate.split('/');
    locEndDate = new Date(locEndDate[2], locEndDate[1] - 1, locEndDate[0]);

    futureDate.setDate(futureDate.getDate() + numOfDays);
    locEndDate = formatted_date(locEndDate);
    futureDate = formatted_date(futureDate);

    return locEndDate <= futureDate;
}

function OpenPreferenceView(signum, name) {

    $('#userModal').modal('show');

    $("#user_time_zone").val();
    $("#user_profile").val();
    $("#landing_page").val();
    getUserPreferences('userpref');
}

function getWeekNumber(d) {
    // Copy date so don't modify original
    d = new Date(Date.UTC(d.getFullYear(), d.getMonth(), d.getDate()));
    // Set to nearest Thursday: current date + 4 - current day number
    // Make Sunday's day number 7
    d.setUTCDate(d.getUTCDate() + 4 - (d.getUTCDay() || 7));
    // Get first day of year
    var yearStart = new Date(Date.UTC(d.getUTCFullYear(), 0, 1));
    // Calculate full weeks to nearest Thursday
    var weekNo = Math.ceil((((d - yearStart) / 86400000) + 1) / 7);
    // Return array of year and week number
    return [d.getUTCFullYear(), weekNo];
}

function dateFromWeekNumber(year, week) {
    var d = new Date(year, 0, 1);
    var dayNum = d.getDay();
    var diff = --week * 7;

    // If 1 Jan is Friday to Sunday, go to next week
    if (!dayNum || dayNum > 4) {
        diff += 7;
    }

    // Add required number of days
    d.setDate(d.getDate() - d.getDay() + ++diff);
    return d;
}

var formatted_date = function (date) {
    var m = ("0" + (date.getMonth() + 1)).slice(-2); // in javascript month start from 0.
    var d = ("0" + date.getDate()).slice(-2); // add leading zero 
    var y = date.getFullYear();
    return y + '-' + m + '-' + d;
}

function getShiftTiming(signum, name, timeZoneVisible) {
    $('#shiftTitle').show();
    $('#currentShiftDiv').show();
    $('#savedTimingsTab').show();
    $('#labelCurrentShift').show();
    $("#singleOrMultiple").val("single");
    clearShiftModal();
    let weekNo = getWeekNumber(new Date());

    let currentYear = new Date().getFullYear();
    let startDate = dateFromWeekNumber(currentYear, weekNo[1]);
    let currentStartDate = formatted_date(startDate)
    $('#shiftSignum').val(signum);
    $('#shiftModal').modal('show');

    $('#shiftModal .nav-tabs li a[href="#shiftTimingAdd_tab_1"]').tab('show');
    if (!timeZoneVisible) {
        var UserTimeZone = localStorage.getItem("UserTimeZone");
        $('#timeZoneDD').hide();
        $('#timeLabelDD').hide();

        $('#timeZoneHome').show();
        $('#time_home').val(UserTimeZone);
    }
    else {
        $('#timeZoneDD').show();
        $('#timeZoneHome').hide();
    }
    $("#recordedShiftStartTime").val("");
    $("#recordedShiftEndTime").val("");
    $("#recordedTimeZone").val("");
    $("#recordedShiftStartDate").val("");
    $("#recordedShiftEndDate").val("");
    $('#shiftTitle').text(signum + " (" + name + ")");
    var serviceCall = $.isf.ajax({
        url: service_java_URL + "activityMaster/getShiftTimmingBySignum/" + signum + "/" + currentStartDate,
        returnAjaxObj: true,
        async: false,
        context: this,
        crossdomain: true,
        dataType: 'json',
        contentType: 'application/json',
        type: 'GET',
        xhrFields: {
            withCredentials: false
        },
        success: function (data) {
            if (typeof data != "undefined" && data.length > 0) {
                var startTimeFormatted = data[0].shiftStartTime.substring(0, 5);
                var endTimeFormatted = data[0].shiftEndTime.substring(0, 5);
                $("#recordedShiftStartTime").val(startTimeFormatted);
                $("#recordedShiftEndTime").val(endTimeFormatted);
                $("#recordedTimeZone").val(data[0].timeZone);
                $("#recordedShiftStartDate").val(data[0].startDate);
                $("#recordedShiftEndDate").val(data[0].endDate);
                if (data[0].preferredTimeZone !== null)
                    preferredTimeZone = data[0].preferredTimeZone.split(" - ")[1];
                else
                    preferredTimeZone = "";
            }
        },
        error: function (xhr, status, statusText) {
            console.log('An error occurred');


        }
    })

}

function setMintToZero(thisObj) {
    let t = thisObj.value;
    let h = t.split(':');

    thisObj.value = h[0] + ':00';
}

function getDateOfISOWeekShift(w) {

    var date = w.split('/');
    $('#weekNumberSelected').val(date[0]);
    var w1 = $('#weekNumberSelected').val();
    var y = date[1];
    var simple = new Date(y, 0, 1 + (w1 - 1) * 7);
    var simpleEnd = new Date(y, 0, 1 + (w1 - 1) * 7);
    var dow = simple.getDay();
    var ISOweekStart = simple;
    var ISOweekEnd = simpleEnd;
    if (dow <= 4) {
        ISOweekStart.setDate(simple.getDate() - simple.getDay() + 1);
        ISOweekEnd.setDate((simpleEnd.getDate() - simpleEnd.getDay() + 1) + 6);
        $('#startDateSelected').val(formatted_date(ISOweekStart));
        $('#endDateSelected').val(formatted_date(ISOweekEnd));

    }
    else {
        ISOweekStart.setDate(simple.getDate() + 8 - simple.getDay());
        ISOweekEnd.setDate((simpleEnd.getDate() + 8 - simpleEnd.getDay()) + 6);
        $('#startDateSelected').val(formatted_date(ISOweekStart));
        $('#endDateSelected').val(formatted_date(ISOweekEnd));

    }
}

function endTimeChange(thisObj) {

    const sTimeArr = $(thisObj).val().split(":");
    if (sTimeArr.length > 1) {
        let updatedEndTime = parseInt(sTimeArr[0]) + 8;
        if (updatedEndTime < 10) {
            updatedEndTime = `0${updatedEndTime}`;
        }
        if (updatedEndTime >= 24) {
            updatedEndTime = updatedEndTime - 24;
            if (updatedEndTime < 10) {
                updatedEndTime = `0${updatedEndTime}`;
            }
        }
        updatedEndTime = `${updatedEndTime}:${sTimeArr[1]}`;
        $("#shiftEndTime").val(updatedEndTime);
    }
}

function validateShift() {
    var OK = true;
    hideErrorMsg('StartLessThanEnd');

    var startTime = $("#shiftStartTime").val();
    var endTime = $("#shiftEndTime").val();
    var hours = moment.utc(moment(endTime, "HH:mm:ss").diff(moment(startTime, "HH:mm:ss"))).format("HH");
    if (hours < 08) {
        showErrorMsg('StartLessThanEnd', 'Shift should be minimum of 8 hours');
        OK = false;
    }

    hideErrorMsg('Start_Time-Required');
    if ($("#shiftStartTime").val() === "") {
        showErrorMsg('Start_Time-Required', 'Start time Required');
        OK = false;
    }
    hideErrorMsg('End_Time-Required');
    if ($("#shiftEndTime").val() === "") {
        showErrorMsg('End_Time-Required', 'Start time Required');
        OK = false;
    }
    hideErrorMsg('timeZone-Required');
    if ($("#time_zone").val() === "0") {
        showErrorMsg('timeZone-Required', 'Time Zone Required');
        OK = false;
    }
    hideErrorMsg('timeWeek-Required');
    if ($("#datepickerShift").val() === "") {
        showErrorMsg('timeWeek-Required', 'Week Required');
        OK = false;
    }
    return OK;

}

function saveShiftTiming() {
    var timeZ = "";
    var startT = "";
    var endT = "";
    var shiftISTStartTime = "";
    var shiftISTEndTime = "";
    var shiftISTEndDate = "";
    var shiftISTStartDate = "";
    if (validateShift() === true) {
        var UserTimeZone = localStorage.getItem("UserTimeZone");



        timeZ = $("#time_zone").val();
        moment.tz.setDefault(timeZ);

        startT = $("#shiftStartTime").val();

        endT = $("#shiftEndTime").val();
        var finalStartDateTime = moment($("#startDateSelected").val() + " " + $("#shiftStartTime").val()).tz("Asia/Kolkata").format('YYYY-MM-DD HH:mm');
        shiftISTStartDate = finalStartDateTime.split(" ")[0];
        shiftISTStartTime = finalStartDateTime.split(" ")[1];
        var finalEndtDateTime = moment($("#endDateSelected").val() + " " + $("#shiftEndTime").val()).tz("Asia/Kolkata").format('YYYY-MM-DD HH:mm');
        shiftISTEndDate = finalEndtDateTime.split(" ")[0];
        shiftISTEndTime = finalEndtDateTime.split(" ")[1];
        var shiftObj = new Object();
        var signumArr = [];

        signumArr.push($('#shiftSignum').val());
        shiftObj.shiftStartTime = startT + ":00";
        shiftObj.shiftEndTime = endT + ":00";
        shiftObj.shiftISTStartTime = shiftISTStartTime + ":00";
        shiftObj.shiftISTEndTime = shiftISTEndTime + ":00";
        shiftObj.shiftISTStartDate = shiftISTStartDate;
        shiftObj.shiftISTEndDate = shiftISTEndDate;
        shiftObj.timeZone = timeZ;
        if ($("#singleOrMultiple").val() === "single")
            shiftObj.signum = signumArr;
        else
            shiftObj.signum = signumArrTeamExplorer;
        shiftObj.createdBy = signumGlobal;
        shiftObj.startDate = $('#startDateSelected').val();
        shiftObj.endDate = $('#endDateSelected').val();
        shiftObj.week = $('#weekNumberSelected').val();

        $.isf.ajax({
            url: service_java_URL + "activityMaster/saveshiftTimming",
            context: this,
            crossdomain: true,
            processData: true,
            contentType: 'application/json',
            type: 'POST',
            data: JSON.stringify(shiftObj),
            xhrFields: {
                withCredentials: false
            },
            success: function (data) {
                pwIsf.alert({ msg: "Shift Time changed successfully", type: "success" });
                $('#shiftModal').modal('hide');
                clearShiftModal();
                moment.tz.setDefault();
            },
            error: function (xhr, status, statusText) {
                console.log(xhr);
            }
        });
    }

}

function showErrorMsg(inpEle, msg) {

    $('#' + inpEle).text(msg);
    $('#' + inpEle).css('display', '');
}

function hideErrorMsg(inpEle) {
    $('#' + inpEle).text('');
    $('#' + inpEle).css('display', 'none');
}

function clearUserPreferenceModal() {
    $("#user_time_zone").find('option').not(':first').remove();
    var selectbox = document.getElementById("user_profile");
    var i;
    for (i = selectbox.options.length - 1; i >= 0; i--) {
        selectbox.remove(i);
    }
    $("#landing_page").find('option').not(':first').remove();
}

function getTimeZonesForPreference(defaultTimeZone = 282) {


    $.isf.ajax({
        url: service_java_URL + "activityMaster/getTimeZones",
        context: this,
        crossdomain: true,
        dataType: 'json',
        contentType: 'application/json',
        type: 'GET',
        xhrFields: {
            withCredentials: false
        },
        success: function (data) {
            var currentDate = new Date();
            $("#user_time_zone").empty();
            $.each(data, function (i, d) {
                if (d.timeZoneID === defaultTimeZone)
                    $("#user_time_zone").append('<option value="' + d.timeZoneID + '" selected="selected' + '">' + '(UTC ' + moment(currentDate).tz(d.timeZone.trim()).format('Z') + ') ' + d.utcOffset + " - " + d.timeZone + '</option>');
                else
                    $("#user_time_zone").append('<option value="' + d.timeZoneID + '">' + '(UTC ' + moment(currentDate).tz(d.timeZone.trim()).format('Z') + ') ' + d.utcOffset + " - " + d.timeZone + '</option>');
            });

        },
        error: function (xhr, status, statusText) {
            console.log('An error occurred');


        }
    });
}

function getUrlParameter(sParam) {
    var sPageURL = decodeURIComponent(window.location.search.substring(1)),
        sURLVariables = sPageURL.split('&'),
        sParameterName,
        i;

    for (i = 0; i < sURLVariables.length; i++) {
        sParameterName = sURLVariables[i].split('=');

        if (sParameterName[0] === sParam) {
            return sParameterName[1] === undefined ? true : sParameterName[1];
        }
    }
}

var userLoggedIn = signumGlobal;

$(document).ready(function () {


    if (Notification.permission !== "denied") {
        Notification.requestPermission();
    }

    $("#user_time_zone").select2({
        placeholder: "Select a Time Zone"

    });

    $("#user_profile").select2({
        placeholder: "Select a Preferred User Profile"
    });
    $("#landing_page").select2({
        placeholder: "Select Landing Page"
    });

    if (IsAspUser === "False" || (IsAspUser === "True" && AspStatus === "APPROVED")) {
        ConfigureProfiles();

        disableFields();


        if ($("#empName")[0]) {
            var userImage = "";
            if (UserImageUriSession == null || UserImageUriSession == 'null' || UserImageUriSession == '') {
                userImage = '<i class="fa fa-user" title="' + UserNameSession + ' ( ' + (JSON.parse(ActiveProfileSession)).accessProfileName + ' )' + '"></i>';
            } else {
                userImage = '<img style="width: 30px; height:30px" src="' + UserImageUriSession + '" id="userImage1" title="' + UserNameSession + ' ( ' + (JSON.parse(ActiveProfileSession)).accessProfileName + ' )' + '" />';
            }
            $("#empName")[0].innerHTML =
                userImage

                + (JSON.parse(ActiveProfileSession)).accessProfileName

                + '<b class="caret"></b>';

        }
        var userLoggedIn = signumGlobal;




    }




});

var ISFConnectionProxy;

function globalConnection() {
    var page = '';

    page = $(location).attr('pathname');
    // Declare a proxy to reference the hub.
    ISFConnectionProxy = $.connection.iSFConnectionsHub;
    // Client fuction to send it's identity
    ISFConnectionProxy.client.ClientIdentity = function (CID) {

    };

    ISFConnectionProxy.client.checkISFDesktop = function () {
        isfDesktopStatus();
    };

    ISFConnectionProxy.client.desktopStatus = function (status) {
        localStorage.setItem("IsISFDesktopRunning", status);
    };

    // Start the connection.
    $.connection.hub.start().done(function () {
        ISFConnectionProxy.server.clientInfo(ISFConnectionProxy.connection.id, 'Web', signumGlobal, page);

    });
}

var login_main_signum = signumGlobal;

function disableFields() {
    if (document.getElementById("main_employee_signum")) {
        document.getElementById("main_employee_signum").disabled = true;
        document.getElementById("main_employee_email").disabled = true;
        document.getElementById("main_employee_team").disabled = true;
        document.getElementById("main_employee_jobRole").disabled = true;
        document.getElementById("main_employee_careerStartDate").disabled = true;
        document.getElementById("main_employee_serviceArea").disabled = true;
        document.getElementById("main_employee_officeBuilding").disabled = true;

        document.getElementById("main_employee_employeeName").disabled = true;
        document.getElementById("main_employee_hrLocation").disabled = true;
        document.getElementById("main_employee_personnelNumber").disabled = true;

        document.getElementById("main_employee_jobRoleFamily").disabled = true;
        document.getElementById("main_employee_dateOfJoining").disabled = true;
        document.getElementById("main_employee_city").disabled = true;
        document.getElementById("main_employee_floor").disabled = true;
        document.getElementById("main_employee_id").disabled = true;
        document.getElementById("main_employee_costCenter").disabled = true;
        document.getElementById("main_employee_grade").disabled = true;
        document.getElementById("main_employee_functionalArea").disabled = true;

        document.getElementById("main_employee_seatNumber").disabled = true;
        document.getElementById("main_employee_reportingManager").disabled = true;
    }
}

function saveUserPreference() {

    var time_zone_id = $('#user_time_zone option:selected').val();
    var user_profile_id = $('#user_profile option:selected').val();
    var landing_page_id = $('#landing_page option:selected').val();
    var signum = signumGlobal;

    var time_zone = $('#user_time_zone option:selected').text();
    var user_profile = $('#user_profile option:selected').text();
    var landing_page_name = $('#landing_page option:selected').text();
    let thisObj = $('#landing_page option:selected');
    let landing_page_href = $(thisObj).data('details');
    let landing_page = landing_page_name + ',' + decodeURIComponent(landing_page_href);


    if ((time_zone !== 'Select Time Zone' && time_zone_id !== 0)) {
        var userPref = new Object();
        userPref.defaultZoneId = time_zone_id;
        userPref.defaultZoneValue = time_zone;
        userPref.defaultProfileId = user_profile_id;
        userPref.userSignum = signum;
        userPref.createdBy = signumGlobal;
        userPref.defaultProfileValue = user_profile;
        userPref.lastModifiedBy = signumGlobal;
        userPref.defaultPageId = landing_page_id;
        userPref.defaultPageName = landing_page;

        $.isf.ajax({
            url: service_java_URL + "accessManagement/addUserPreferences",
            context: this,
            crossdomain: true,
            processData: true,
            contentType: 'application/json',
            type: 'POST',
            data: JSON.stringify(userPref),
            xhrFields: {
                withCredentials: false
            },
            success: function (data) {
                $('#userModal').modal('hide');

                localStorage.setItem("UserTimeZone", time_zone.split(" - ")[1].trim());

                if (data.isApiSuccess) {
                    pwIsf.alert({ msg: data.msg, type: "success" });
                    $('#userModal').modal('hide');

                    localStorage.setItem("UserTimeZone", time_zone.split(" - ")[1].trim());
                    var refID = localStorage.getItem('currentRefID');
                    var uploadedFileName = localStorage.getItem('uploadedNEFileName');
                    if (refID !== null && refID !== undefined && uploadedFileName !== null && uploadedFileName !== undefined) {
                        checkNEUploadFileStatus(refID);
                    }
                }
                else {
                    pwIsf.alert({ msg: data.msg, type: 'warning' });
                    $('#userModal').modal('hide');
                }
            },
            error: function (xhr, status, statusText) {
                console.log(xhr);
            }
        });
    }
    else {
        pwIsf.alert({ msg: 'Please fill all the fields', type: 'warning' });
    }
}

function getEmployeeDetailsBySignumView() {

    $('#scopeModal1').modal('show');

    $.isf.ajax({
        url: service_java_URL + "activityMaster/getEmployeeBySignum/" + login_main_signum,
        success: function (data) {

            $("#main_employee_signum").val(data.signum);
            $("#main_employee_email").val(data.employeeEmailId);
            $("#main_employee_id").val(data.employeeId);
            $("#main_employee_team").val(data.team);
            $("#main_employee_jobRole").val(data.jobRole);
            $("#main_employee_careerStartDate").val(data.careerStartDate);
            $("#main_employee_serviceArea").val(data.serviceArea);
            $("#main_employee_officeBuilding").val(data.officeBuilding);

            $("#main_employee_employeeName").val(data.employeeName);
            $("#main_employee_personnelNumber").val(data.personnelNumber);

            $("#main_employee_jobRoleFamily").val(data.jobRoleFamily);
            $("#main_employee_dateOfJoining").val(data.dateOfJoining);
            $("#main_employee_city").val(data.city);
            $("#main_employee_floor").val(data.floor);
            $("#main_employee_costCenter").val(data.costCenter);
            $("#main_employee_functionalArea").val(data.functionalArea);

            $("#main_employee_seatNumber").val(data.seatNumber);
            $("#main_employee_grade").val(data.grade);
            $("#main_employee_reportingManager").val(data.managerSignum);

        },
        error: function (xhr, status, statusText) {
            console.log('An error occurred : ' + xhr.responseText);
        }
    });


}

function getEmployeeDetailsBySignum(signum) {

    $('#scopeModal1').modal('show');

    $.isf.ajax({
        url: service_java_URL + "activityMaster/getEmployeeBySignum/" + signum,
        success: function (data) {

            $("#main_employee_signum").val(data.signum);
            $("#main_employee_email").val(data.employeeEmailId);
            $("#main_employee_id").val(data.employeeId);
            $("#main_employee_team").val(data.team);
            $("#main_employee_jobRole").val(data.jobRole);
            $("#main_employee_careerStartDate").val(data.careerStartDate);
            $("#main_employee_serviceArea").val(data.serviceArea);
            $("#main_employee_officeBuilding").val(data.officeBuilding);

            $("#main_employee_employeeName").val(data.employeeName);
            $("#main_employee_personnelNumber").val(data.personnelNumber);

            $("#main_employee_jobRoleFamily").val(data.jobRoleFamily);
            $("#main_employee_dateOfJoining").val(data.dateOfJoining);
            $("#main_employee_city").val(data.city);
            $("#main_employee_floor").val(data.floor);
            $("#main_employee_costCenter").val(data.costCenter);
            $("#main_employee_functionalArea").val(data.functionalArea);

            $("#main_employee_seatNumber").val(data.seatNumber);
            $("#main_employee_grade").val(data.grade);
            $("#main_employee_reportingManager").val(data.managerSignum);

        },
        error: function (xhr, status, statusText) {
            console.log('An error occurred : ' + xhr.responseText);
        }
    });


}

function CreateMenu() {

    HideAllMenus();
    var isLineMansger = JSON.parse(UserDetailsSession)[0].isLineManager
    if (isLineMansger === 'YES') {
        $("#menu_teamexplorer").show();
    }
    var pageList = JSON.parse(PageAccessSession);

    pageList.forEach(AccessiblePage);

}

function AccessiblePage(item, index) {
    var pageObj = item;
    $("#" + pageObj.capabilityPageName).show();
    $("#" + pageObj.capabilityPageGroup).show();
}

function HideAllMenus() {

    var menuIdArr = JSON.parse(MenuIdListSession);

    for (var i in menuIdArr) {
        if (menuIdArr[i]) {
            $('#' + menuIdArr[i]).hide();
        }
    }


}

function ManualLogout() {
    if ($("#lnkLogout").attr("disabled") !== "disabled") {
        $("#lnkLogout").attr("disabled", "disabled");

        sessionStorage.clear();
        try {
            if (InitiateSignalRConnection() === true) {
                UpdateFloatingWindowServeMethods(true, false);
            }
        } catch (err) {
            console.log(err);
            triggerForceLogout();
        } finally {
            setTimeout(function () {
                localStorage.setItem('sessionOut', 'true');
                window.location.href = UiRootDir + '/Login/SignOut';
            }, 2000);
        }

    }
}

function UserLogout() {
    var d = new Date();

    var logoutTime = d.getFullYear().toString() + "-" + ((d.getMonth() + 1).toString().length === 2 ? (d.getMonth() + 1).toString() : "0" + (d.getMonth() + 1).toString()) + "-" + (d.getDate().toString().length === 2 ? d.getDate().toString() : "0" + d.getDate().toString()) + " " + (d.getHours().toString().length === 2 ? d.getHours().toString() : "0" + d.getHours().toString()) + ":" + ((parseInt(d.getMinutes())).toString().length === 2 ? (parseInt(d.getMinutes())).toString() : "0" + (parseInt(d.getMinutes())).toString()) + ":" + ((d.getSeconds()).toString().length === 2 ? (d.getSeconds()).toString() : "0" + (d.getSeconds()).toString());
    var logId = '';

    var userLoggedIn = signumGlobal;

    $.isf.ajax({

        url: service_java_URL + "accessManagement/getLoginHistory/" + userLoggedIn,
        async: false,
        success: function (data) {
            console.log("success");
            logId = data[0].logID;
            $.isf.ajax({
                type: "POST",
                url: service_java_URL + "accessManagement/updateLoginHistory/" + userLoggedIn + "/" + logId + "/" + logoutTime,
                success: function () {
                    console.log("success");
                    localStorage.removeItem("userLoggedIn");
                    localStorage.removeItem("navigationList");
                    localStorage = null;

                },
                error: function (xhr, status, statusText) {

                    console.log('An error occurred');
                }
            });
        },
        error: function (xhr, status, statusText) {

            console.log('An error occurred');
        }
    })



}

function ActivePage(pageID) {
    localStorage.setItem("ActivePage", pageID);
}

function showProfileModal() {
    $('#SwitchProfileModal').modal('show');
}

function showRequestProfileModal() {
    $('#SwitchProfileModal').modal('hide');
    $('#RequestProfileModal').modal('show');
}

function getUserPreferences(callFrom) {

    return $.isf.ajax({
        url: service_java_URL + "accessManagement/getUserPreferences/" + signumGlobal,
        type: "GET",
        crossDomain: true,
        async: false,
        success: function (data) {
            if (callFrom === "switchprofile") {
                let accessProfileName = JSON.parse(ActiveProfileSession).accessProfileName;
                if (data.length !== 0) {
                    if (data.defaultProfileValue !== accessProfileName && data.defaultProfileValue != null && data.defaultZoneValue!=null) {

                        localStorage.setItem("UserTimeZone", data.defaultZoneValue.split(" - ")[1].trim());
                    }
                }
            }
            else if (callFrom === "userpref") {
                if (data.defaultProfileId !== null && data.defaultZoneId !== null && data.defaultPageId !== null) {

                    let favouriteProfile = data.defaultProfileId;
                    let selectedtimezone = data.defaultZoneId;
                    let selectedLandingPageId = data.defaultPageId;
                    let selectedLandingPageName = data.defaultPageName;

                    getTimeZonesForPreference(selectedtimezone);
                    getActiveUserProfileList();
                    $("#user_profile").val(favouriteProfile);
                    $("#user_profile").trigger('change', [selectedLandingPageId]);

                }
                else {
                    getTimeZonesForPreference();
                    getActiveUserProfileList();
                    $('#user_time_zone').val('282');
                    $('#user_time_zone').trigger('change');
                    var selectbox = document.getElementById("user_profile");
                    $("#user_profile").val(selectbox[0].value);
                    $("#user_profile").trigger('change');

                }
            }
        },
        error: function (xhr, status, statusText) {

            console.log('An error occurred');
        }
    });
}

function getActiveUserProfileList() {

    clearUserPreferenceModal();
    var activeProfileObj = JSON.parse(ActiveProfileSession);
    if ($('#lblActiveProfile')[0]) {
        $('#lblActiveProfile')[0].innerText = activeProfileObj.accessProfileName;
    }
    var profileList = JSON.parse(ProfileListSession);
    var accessProfileName = JSON.parse(ActiveProfileSession).accessProfileName;
    var profileOptionListHTML = '';
    for (var i = 0; i < profileList.length; i++) {

        $("#user_profile").append('<option value="' + profileList[i].accessProfileID + '">' + profileList[i].accessProfileName + '</option>');

    }



}

function ConfigureProfiles() {

    var activeProfileObj = JSON.parse(ActiveProfileSession);
    if ($('#lblActiveProfile')[0]) {
        $('#lblActiveProfile')[0].innerText = activeProfileObj.accessProfileName;
    }
    var profileList = JSON.parse(ProfileListSession);
    var accessProfileName = JSON.parse(ActiveProfileSession).accessProfileName;
    var profileOptionListHTML = '';
    for (var i = 0; i < profileList.length; i++) {
        if (profileList[i].accessProfileName == accessProfileName) {
            $('.highlight').bind('click', false);
            profileOptionListHTML += '<li><a class="removeHighlight highlight"  id="' + profileList[i].accessProfileID + '">' + profileList[i].accessProfileName + '</a></li>';

        }
        else
            profileOptionListHTML += '<li><a class="removeHighlight" href="#" onclick="switchProfile(this)" id="' + profileList[i].accessProfileID + '">' + profileList[i].accessProfileName + '</a></li>';
    }
    $("#switchP").append(profileOptionListHTML);
    $('.highlight').bind('click', false);

}

function switchProfile(obj) {
    pwIsf.addLayer({ text: 'Please wait ...' });
    var activateProfileName = obj.text;

    var profileList = JSON.parse(ProfileListSession);

    var organizationalModal = JSON.parse(UserDetailsSession)[0].lstOrganizationModel;


    for (var i = 0; i < profileList.length; i++) {
        if (profileList[i].accessProfileName == activateProfileName) {

            var profile = JSON.stringify(profileList[i]);
            var pageAccess = JSON.stringify(profileList[i].lstCapabilityModel);
            let org = profileList[i].organisation;
            let userRole = profileList[i].role;


            set_Session_Values('Organisation', org);
            set_Session_Values('UserRole', userRole);
            set_Session_Values('ActiveProfile', profile);
            set_Session_Values('PageAccess', pageAccess);
            getUserPreferences('switchprofile');
            SetProfileHistory(profileList[i]);
        }
    }


    window.location.href = UiRootDir + "/Home";


}

function getRolesRP() {
    $('#cbxRolesRP').empty();
    var userType = getSessionValuesByKey('UserType');
    $.isf.ajax({
        url: service_java_URL + "accessManagement/getRoleList?userType=" + userType,
        success: function (data) {
            var html = "";
            $('#cbxRolesRP').append('<option value="0">----Select Role----</option>');
            $.each(data, function (i, d) {
                $('#cbxRolesRP').append('<option value="' + d.accessRoleID + '">' + d.role + '</option>');
            })

            $('#cbxRolesRP').select2('val', '0');
            getAccessProfileByRoleRP();
        },
        error: function (xhr, status, statusText) {

            console.log('An error occurred');
        }
    });
}

function getAccessProfileByRoleRP() {

    var role = $("#cbxRolesRP").val();
    pwIsf.addLayer({ text: "Please wait ..." });
    $('#cbxAccessProfileRP').find('option').remove();
    if (role !== 0 && role !== "0") {
        $.isf.ajax({
            url: service_java_URL + "accessManagement/getAccessProfilesByRole/" + role,
            success: function (data) {
                $('#cbxAccessProfileRP').empty();
                $('#cbxAccessProfileRP').append('<option value="0">----Select Access Profiles----</option>');
                $.each(data, function (i, d) {

                    $('#cbxAccessProfileRP').append('<option value="' + d.accessProfileID + '">' + d.accessProfileName + '</option>');
                })


                pwIsf.removeLayer();
            },
            error: function (xhr, status, statusText) {
                alert("Fail Get Access Profiles " + xhr.responseText);
                alert("status " + xhr.status);
                console.log('An error occurred');
                pwIsf.removeLayer();
            }
        });
    }
    else {
        pwIsf.removeLayer();

        $('#cbxAccessProfileRP').append('<option value="0">----Select Access Profiles----</option>');
        $('#cbxAccessProfileRP').select2('val', '0');
    }

}

function requestAccessProfile() {


    var service_data = "{\"accessProfileID\":\"" + $('#cbxAccessProfileRP').val() + "\",\"signum\":\"" + signumGlobal + "\"}";

    console.log("Request requestAccessProfile -->>" + service_data);
    pwIsf.addLayer({ text: "Please wait ..." });
    $.isf.ajax({
        url: service_java_URL + "accessManagement/requestAccessProfile",
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
        responseHandler(data);
        pwIsf.removeLayer();


    }
    function AjaxFailed(result) {
        pwIsf.removeLayer();


    }
}

//--RPA & Analysis integration code
function openURL(url, type) {
    if (type === 'automation') {
        url = url + userLoggedIn;


        window.open(url, url, 'top=0, left=0,fullscreen=yes,height=' + screen.height + ',width=' + screen.width);

    }
}

imgDataForAjax = {};

imgDataForAjax.createHiddenFlds = function (jsonNameIdVal, idPreFix) {  //create hidden flds from json 
    let html = '';
    for (let fk in jsonNameIdVal) {
        let nm = jsonNameIdVal[fk]['name'];
        let inputId = (jQuery.trim(jsonNameIdVal[fk]['id']) != '') ? `id="${idPreFix}_${jsonNameIdVal[fk]['id']}"` : '';
        let val = (jQuery.trim(jsonNameIdVal[fk]['value']) != '') ? `value="${jsonNameIdVal[fk]['value']}"` : '';

        html += `<input type="hidden" name="${nm}" ${inputId} ${val} />\n`;
    }

    return html;
};

imgDataForAjax.getFormDataJson = function (frm) { // GET ALL FORM DATA IN JSON FORMAT USING {} AND []
    //frm jquery object
    let s = frm.serializeArray();
    let g = {};

    for (let i in s) {
        if ((s[i]['name'].match(/{}/g) || []).length) {
            let splitArr = s[i]['name'].split('{}');
            let add = {};
            add[splitArr['1']] = s[i]['value'];
            if (typeof g[splitArr[0]] === 'undefined') {
                g[splitArr[0]] = add;
            } else {

                g[splitArr[0]][splitArr['1']] = s[i]['value'];
            }

        } else if ((s[i]['name'].match(/\[\]/g) || []).length) {
            let splitArr = s[i]['name'].split('[]');
            if (typeof g[splitArr[0]] === 'undefined') {
                g[splitArr[0]] = [];
            }
            let add = {};
            add[splitArr['1']] = s[i]['value'];
            g[splitArr[0]].push(add);
        } else {
            g[s[i]['name']] = s[i]['value'];
        }
    }

    return g;

};

imgDataForAjax.getData = function (options) {
    let imgflds = options.setImgFldsJosn;
    let formId = options.formId;
    let formObj = $('#' + formId);
    let form = document.getElementById(formId);
    let data = new FormData(form);

    for (let i in imgflds) {
        for (let f in formObj[0]) {
            if (formObj[0][f]) {
                if (formObj[0][f].name == imgflds[i]['frmElmntAs']) {

                    data.append(imgflds[i]['sendAs'], formObj[0][f].files[0]);
                    break;
                }
            }
        }
    }
    return data;
};

imgDataForAjax.sendRequest = function (opt) {

    let url = opt.url;
    let progressId = opt.progressBarId;
    let sendData = new FormData();
    sendData = opt.frmData;

    let reqPromise = $.isf.ajax({
        type: "POST",
        enctype: "multipart/form-data",
        url: url,
        returnAjaxObj: true,
        data: sendData,
        processData: false,
        contentType: false,
        cache: false,
        timeout: 600000,
        // Custom XMLHttpRequest
        xhr: function () {
            var myXhr = $.ajaxSettings.xhr();
            if (myXhr.upload) {
                // For handling the progress of the upload
                myXhr.upload.addEventListener('progress', function (e) {
                    if (e.lengthComputable) {
                        let prog = '#' + progressId;
                        if ($(prog)) {
                            $(prog).attr({
                                value: e.loaded,
                                max: e.total,
                            });
                        }
                    }
                }, false);
            }
            return myXhr;
        }
    });

    reqPromise.done(opt.onDone);
    reqPromise.fail(function () { alert("Image upload error -" + url); });
    reqPromise.always(function () { pwIsf.removeLayer(); });

};

//-- Feedback modal 
var isfFeedModelOpenCount = 0;

function feedbackForm() {
    $('[data-toggle="tooltip"]').tooltip();
    $('#smileys input').on('click', function () {
        $('#result').html($(this).val());
    });
    var html = ` <div class="modal fade" id="feedback" role="dialog">
        <div class="modal-dialog">
            <div class="modal-content">
                <div class="modal-header">
                    <button type="button" class="close" data-dismiss="modal">&times;</button>
                    <h4 class="modal-title"><b>Feedback:   </b>` + $(document).find("title").text() + `</h4>
                </div>
                <div class="modal-body"  style="overflow-y:auto;">
<form name="formfeedback" enctype="multipart/form-data" method="POST" id="smileys">

 <div class="well well-sm">
                                                          <span style="margin-left:10px" class="required"> For instant help regarding any issue on ISF please click on<a href="https://teams.microsoft.com/l/channel/19%3a23235d9c44e245c5aec35232723c23bb%40thread.tacv2/Live%2520Chat?groupId=967cdde2-88fe-462f-8150-4f223c1c453e&tenantId=92e84ceb-fbfd-47ab-be52-080c6b87953f" target="_blank" class="btn btn-link btn-md" role="button" style="height: 25.979166px; width: 86.97916599999999px; padding-left: 0px;">Support</a></span>
                                                        </div> <hr>
<input type="hidden" id="submittedBy" name="userFeedbackModel{}submittedBy" value="${signumGlobal}" />
<input type="hidden" id="title" name="userFeedbackModel{}title" value="${$(document).find("title").text()}" />
<input type="hidden" id="url" name="userFeedbackModel{}url" value="${$(location).attr('href')}" />

<div class="row" style="height:80px; padding-top:1px;">
<div class="col-md-12"> 

	<div class="rating">
<div style="float:left;padding-top: 18px;"><b>Are you satisfied with this page?</b></div>
   
     
	<input data-toggle="tooltip" title="No, It needs improvement."  type="radio" name="userFeedbackModel{}rating" value= false class="sad" id="smileyrating">
	<input data-toggle="tooltip" title="Yes, Its good!" type="radio" name="userFeedbackModel{}rating" value= true class="happy"  id="smileyrating">

    </div>
	</div>


                        </div> <hr>



<div class="col-lg-12">

                            
                      <label for="feedBacktext"><b>Please provide your valuable feedback:-</b></label>
    <textarea class="form-control" id="textArea" name="userFeedbackModel{}textArea" rows="5" style="border:outset 2px lightblue;padding-top: 4px;"> </textarea>
 <h6> Note: Above feedback will be used for future improvement of the tool and is not subjected to requirement tracking.Please add your ideas and track them in the IDEABOX. </h6>
                        </div>


       <hr>                 
<div class="row">
                                        <div class="col-md-12">
                                            <label style="margin-left:10px" class="required">Please attach sample for reference(If any)  :</label>
                                            <input class="btn btn-default" type="file" id="userfeedbackfile" name="userfeedbackfile" >
                                            <label id="feedback_name_note-Required" style="color: gray; font-size: 10px; text-align: left;padding-left:10px"><strong style="font-weight:900">Note: </strong>doc, excel, txt, pdf and zip formats are allowed.</label>
                                        </div> <hr>
                          	
<div class="col-lg-12" style="color:blue height: 50px;  padding-top: 1px;">
URL: <span id="pageURL" style="color:grey"></span>
</div><hr>
 <div class="col-lg-2">
                            <button id="btngiveFeedBack" type="submit"  pull-right class="btn btn-primary">Submit Feedback</button>
                        
                        </div>
</form>
                        

                </div>

            </div>
        </div>
    </div>  `;

    $('body').append(html);
    $("#pageTitle").text($(document).find("title").text());
    $("#pageURL").text($(location).attr('href'));
    $('#feedback').modal('show');
    validateFeedbackFile();
    if (isfFeedModelOpenCount++ == 0) {
        makeRTE();
    }
}

function makeRTE() {
    $('#smileys').on('submit', function (e) {
        e.preventDefault();
        submitFeedback();
    });
}

function submitFeedback() { // Submit Feedback
        pwIsf.addLayer({ text: 'Submitting Feedback please wait ...', progressId: 'uploadProgress', showSpin: false })
        let restData = {};
        restData = imgDataForAjax.getFormDataJson($('#smileys'));
        let opt = {};
        opt.setImgFldsJosn = [{ sendAs: 'userfeedbackfile', frmElmntAs: 'userfeedbackfile' }];
        opt.formId = 'smileys';
        let form = document.getElementById(opt.formId);
        let frmData = new FormData(form);
        frmData = imgDataForAjax.getData(opt);
        frmData.append('userFeedbackModel', JSON.stringify(restData['userFeedbackModel']));
        let reqOpt = {};
        reqOpt.url = service_java_URL_VM + "toolInventory/saveUserFeedback";
        reqOpt.progressBarId = 'uploadProgress';
        reqOpt.frmData = frmData;
        reqOpt.onDone = function (data) {

            pwIsf.alert({ msg: 'Thank You for your FeedBack', type: 'success' });
            pwIsf.removeLayer({});
            $('#feedback').modal('hide');
        };

        imgDataForAjax.sendRequest(reqOpt);
    pwIsf.removeLayer({});
}
//save user preffrred location
function saveUserLocationApiCall(locationJson) {
    pwIsf.addLayer({ text: C_PLEASE_WAIT });
    $.isf.ajax({
        url: `${service_java_URL}accessManagement/saveUserLocation`,
        context: this,
        crossdomain: true,
        processData: true,
        contentType: C_CONTENT_TYPE_APPLICATION_JSON,
        type: C_API_POST_REQUEST_TYPE,
        data: JSON.stringify(locationJson),
        xhrFields: {
            withCredentials: false
        },
        success: AjaxSucceeded,
        error: AjaxFailed
    });

    function AjaxSucceeded(data, textStatus) {
        if (!data.isValidationFailed) {
            pwIsf.removeLayer();
            pwIsf.alert({ msg: data.formMessages[0], type: C_SUCCESS, autoClose: 1 });
            $('#locationModal').modal('hide');
        }
        else {
            pwIsf.removeLayer();
            pwIsf.alert({ msg: data.formErrors[0], type: ERRORTEXT, autoClose: 1 });
        }
    }
    function AjaxFailed(result) {
        pwIsf.removeLayer();
        pwIsf.alert({ msg: "Failed to save the location", type: ERRORTEXT });
    }
}
function validateFeedbackFile() {
    $('input[type="file"]').on("change", function (evt) {
        const files = evt.target.files;
        if (files.length !== 0) {
            const fileName = evt.target.files[0].name;
            const isZipFolder = zipFolderType(fileName);
            if (isZipFolder) {
                handleFiles(evt, files[0]);
            }
            else {
                const fileExt = getExtension(fileName);
                if (fileExt !== C_PDF_EXT && fileExt !== C_EXCEL_EXT && fileExt !== C_EXCELX_EXT && fileExt !== C_DOC_EXT && fileExt !== C_DOCX_EXT && fileExt !== C_TXT_EXT) {
                    pwIsf.alert({ msg: C_WRONG_TYPE, autoClose: 3 });
                    evt.target.value = "";
                }
            }
        }
    });
}


