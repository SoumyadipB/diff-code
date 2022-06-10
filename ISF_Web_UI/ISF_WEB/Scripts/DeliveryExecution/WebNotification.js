let notificationStart = 0;
let IsCalledFromNotification = false;
const ADHOCACTIVITYID = '#adhocActivity';
$(document).ready(function () {
    $('ul#main-menu li').on('click', function (i) {
        $('.x').hide();
        $('.x').removeClass("displayInline");
        $('.x').removeClass("displayBlock");
        $('.x').css("display", "none !important");
        notificationStart = 0;
        $("#notificationul").animate({ scrollTop: 0 }, "fast");
        $(ADHOCACTIVITYID).hide();
    });

    $('ul li.dropdown.user.hidden-xs').not(document.getElementById("notification")).on('click', function (i) {
        $('.x').hide();
        $('.x').removeClass("displayInline");
        $('.x').removeClass("displayBlock");
        $('.x').css("display", "none !important");
        notificationStart = 0;
        $("#notificationul").animate({ scrollTop: 0 }, "fast");
        $(ADHOCACTIVITYID).hide();
    });
});
$(document).click(function (e) {
    e.stopPropagation();
    var container = $(".dropdown");
    //check if the clicked area is dropDown or not
    if (container.has(e.target).length === 0) {
        if (e.target.id != "webNotifications" && e.target.id != "notificationImg") {
            $('.x').hide();
            $('.x').removeClass("displayInline");
            $('.x').removeClass("displayBlock");
            $('.x').css("display", "none !important");
            notificationStart = 0;
            $("#notificationul").animate({ scrollTop: 0 }, "fast");
            $("#notificationul").scrollTop(0);
            $(ADHOCACTIVITYID).hide();
        }
    }
})

function notficationIconClick() {
    notificationStart = 0;
    $("#notificationul").scrollTop(0);
    IsCalledFromNotification = true;
    let element = $("#notificationImg");
    if (element != undefined && element != null && element[0].attributes[1].value === "../Content/images/notification-ring.svg") {
        $("#notificationImg").attr('src', '../Content/images/notification.svg')
        localStorage.setItem("isNotificationReceived", false);
        localStorage.setItem('notificationCounts', 0);
        set_Session_Values("NotificationData", null);
    }
    
    getNotifications(notificationStart, true,notificationEnd);
    $("#notificationPanelPointer").toggleClass("displayInline");
    $("#notificationul").toggleClass("displayBlock");
    $(ADHOCACTIVITYID).css('display', 'none');
}

function OnClickNotification(woid) {
    let currentUrl = window.location.href;
    let hostUrl = `${window.location.protocol}//${window.location.hostname}:${window.location.port}/${UiRootDir.trim()}`;

    window.IsNotficationNavigation = true;
    window.woIDFW = woid;

    if (currentUrl != undefined && currentUrl != null) {
        if (!currentUrl.includes("DeliveryExecution/WorkorderAndTask")) {
            window.location.href = hostUrl + '/DeliveryExecution/WorkorderAndTask?woID=' + woid;
        }
        else {
            var hasClass = $('#liMyWorkTab').hasClass("active");
            if (hasClass === false) {
                window.location.href = hostUrl + '/DeliveryExecution/WorkorderAndTask?woID=' + woid;
            }
            else {
                RequestWorkOrderUsingWOId(woid);
            }
        }
    }
}

function _handleNotificationItems(response, isLazyLoaded) {
    if (response !== null && response !== undefined && response.responseData !== null && response.responseData.length > 0) {
        if (isLazyLoaded === true) {
            $("#notificationul > li ").not(':first').remove();
            $("#notificationul").animate({ scrollTop: 0 }, "fast");
            $("#notificationul").scrollTop(0);
        }
        var notificationData = response.responseData;
        for (let item of notificationData) {
            if (item.woid === 0) {
                const htmlNENotification = createNEItemHtml(item); // NE upload Notification Item
                $("#notificationul").append(htmlNENotification);
            } else {
                let htmlNotificaion = createLIHtml(item); // Mobile Notification Item
                $("#notificationul").append(htmlNotificaion);
            }
        }
    }
    else {
        // NO Notification code goes here
        const htmlNoNotificaion = createNoNotification(isLazyLoaded)
        $("#notificationul").append(htmlNoNotificaion);
    }
}

function getNotifications(startIndex, isLazyLoaded,length = 10) {
    let C_GetNotificationsBySignum1="notificationController/getNotificationsBySignum";
    var url = `${service_java_URL}${C_GetNotificationsBySignum1}?start=${startIndex}&length=${length}`;
    if (isLazyLoaded === true) {
        $("#notificationul").html("");
        let headerElement = `<li id="notificationHeaderli" class="list-group-item" style="font-weight: bold; border: solid 1px; border-color: darkgray; height: 20px; background-color: #d5dde6; color: #4d4b4b; padding: 5px; padding-bottom: 20px;"> Notifications</li>`;
        $("#notificationul").append(headerElement);
	}
    $.isf.ajax({
        async: false,
        url: url,
        success: function (response) {
            _handleNotificationItems(response, isLazyLoaded);
        },
        error: function (xhr, status, statusText) {
            alert(statusText);
            console.log('An error occurred on GetNotifications: ' + xhr.error);
        },
        complete: function (xhr, statusText) {
            console.log(statusText);
        }
    });
}


function createLIHtml(item) {
    let createBy = '';
    if (item.createdBy != null && item.createdBy != undefined) {
        createBy = getFirstAndLastCharOfUserName(item.createdBy)
    }
    let date = ConvertDateTime_tz(item.createdDate);
    let htmlNoNotificaion = '<li class="list-group-item dropdown user hidden-xs" style="cursor:pointer; color: black;border:1px solid darkgray; background-color:#eff2f5;padding:0px !important; height: 90px !important;" onclick="OnClickNotification(\'' + item.woid + '\');">';
    htmlNoNotificaion = htmlNoNotificaion + '<div style="height:70px; background-color:#eff2f5">';
    htmlNoNotificaion = htmlNoNotificaion + ' <div style="margin-top:5px;display:inline-block;cursor:pointer;">';
    htmlNoNotificaion = htmlNoNotificaion + '<div style="margin-left: 2px; text-align: center; padding-top: 7px; border: solid 1px; float: left; border-color: #bbb" class="dot">' + createBy + '</div>';
    htmlNoNotificaion = htmlNoNotificaion + '<div style="margin-left:10px; float: right; color: #404142" class="fa;"> ' + item.createdBy + ' Commented <br /> <span>WOID ' + item.woid + '/ SRID ' + item.referenceId + '</span></div>';
    htmlNoNotificaion = htmlNoNotificaion + '</div>';
    htmlNoNotificaion = htmlNoNotificaion + '<div style="padding-left: 5px; color: gray" class="commentsTypeStyling">' + '<span text-overflow="ellipsis" title="' + item.auditComments + '">' + getComments(item.auditComments)+ '</span>' + '</div>';
    htmlNoNotificaion = htmlNoNotificaion + '<div style="text-align: right;  color: gray">';
    htmlNoNotificaion = htmlNoNotificaion + '<span>' + item.notificationSource + ' | </span><span> ' + date + '&nbsp; </span>';
    htmlNoNotificaion = htmlNoNotificaion + '</div></div></li>';
    return htmlNoNotificaion;
}

function createNEItemHtml(item) {
    const date = ConvertDateTime_tz(item.createdDate);
    let htmlNoNotificaion = '';
    if (item.active) {
        if (item.auditComments === "Completed" || item.auditComments === "Cancelled" ||
            item.auditComments === "Failed" || item.auditComments === "Expired") {
            htmlNoNotificaion = `<li class="list-group-item dropdown user hidden-xs"
            style="cursor:not-allowed; color: black;border:1px solid darkgray; background-color:#eff2f5;padding:0px !important; height: 64px !important;">`;
        }
        else {
            htmlNoNotificaion = `<li class="list-group-item dropdown user hidden-xs"
            style="cursor:pointer; color: black;border:1px solid darkgray; background-color:#eff2f5;padding:0px !important; height: 64px !important;"
            onclick="showStatus(${item.referenceId});">`;
        }
    }
    else if (!item.active) {
        htmlNoNotificaion = `<li class="list-group-item dropdown user hidden-xs" style="cursor:not-allowed; color: black;border:1px solid darkgray;
            background-color:#eff2f5;padding:0px !important; height: 64px !important;"`;
    }

    let headerForNEComments = "";
    switch (item.auditComments) {
        case "InProgress":
            headerForNEComments = "NE Upload is In-progress";
            break;
        case "Validated":
            headerForNEComments = "NE file validated. Your action required";
            break;
        case "Completed":
            headerForNEComments = "NE Uploaded Successfully.";
            break;
        case "Cancelled":
            headerForNEComments = "NE Upload Request Cancelled.";
            break;
        case "Expired":
            headerForNEComments = "NE Upload Request Timed-out";
            break;
        case "Failed":
            headerForNEComments = "Upload failed. Please try again.";
            break;
        default: headerForNEComments = "No status found";

    }

    htmlNoNotificaion = `${htmlNoNotificaion}<div style="height:70px; background-color:#eff2f5">`;
    htmlNoNotificaion = `${htmlNoNotificaion}<div style="margin-top:5px;margin-bottom:-5px;display:inline-block;">`;
    htmlNoNotificaion = `${htmlNoNotificaion}<div style="margin-left:5px; float: right; color: #404142" class="fa;">${headerForNEComments}</span></div>`;
    htmlNoNotificaion = `${htmlNoNotificaion}</div>`;
    htmlNoNotificaion = `${htmlNoNotificaion}<div style="padding-left: 10px;padding-bottom:7px;margin-left:-5px; color: gray" class="commentsTypeStyling">`;
    htmlNoNotificaion = `${htmlNoNotificaion}<span text-overflow="ellipsis" title="${item.auditComments}">Status : ${item.auditComments}</span>`;
    htmlNoNotificaion = `${htmlNoNotificaion}</div >`;
    htmlNoNotificaion = `${htmlNoNotificaion}<div style="text-align: right;  color: gray">`;
    htmlNoNotificaion = `${htmlNoNotificaion}<span>${item.notificationSource}|</span><span>${date}&nbsp;</span>`;
    htmlNoNotificaion = `${htmlNoNotificaion}</div></div></li>`;
    return htmlNoNotificaion;
}
function createNoNotification(isLazyLoaded) {
    if (isLazyLoaded === true) {
        $("#notificationul > li ").not(':first').remove();
        let htmlNoNotificaion = '<li class="list-group-item dropdown user hidden-xs" style="color: black;border:1px solid darkgray; padding:0px !important; height: 110px !important;">';
        htmlNoNotificaion = htmlNoNotificaion + '<div style="height:110px;background-color:#eff2f5;border-bottom: solid;border-color: darkgray;">';
        htmlNoNotificaion = htmlNoNotificaion + '<div style="margin-top:5px;display:inline-block; ">';
        htmlNoNotificaion = htmlNoNotificaion + '</div>';
        htmlNoNotificaion = htmlNoNotificaion + '<div style="color: gray;text-align:center;"><img id="imgNoNotification" style="background-color:darkgrey;" src="../Content/images/notification.svg"></div>';
        htmlNoNotificaion = htmlNoNotificaion + '<div style="text-align: center;  color: gray">';
        htmlNoNotificaion = htmlNoNotificaion + '<span>No Notification </span>';
        htmlNoNotificaion = htmlNoNotificaion + '</div></li>';
        return htmlNoNotificaion;
       
	}
}
function getComments(comments) {
    if (comments.length > 51) {
        comments = comments.substring(0, 50)+'...';
	}
    return comments;
}
function getFirstAndLastCharOfUserName(UserName) {
    UserName = UserName.split("(")[1];
    let nameArr = UserName.split(" ");
    let result;

    if (nameArr.length > 0) {
        result = nameArr[0].charAt(0);
    }
    if (nameArr.length > 1 && nameArr[nameArr.length - 1].charAt(0) != ".") {
        result += nameArr[nameArr.length - 1].charAt(0);
    }
    if (result != undefined && result != null) {
        result = result.toUpperCase();
    }
    return result;
}
function scrollNotification() {
    if (IsCalledFromNotification) {
        $("#notificationul").scrollTop(0);
    }
    let noNotificationLength = $(document.querySelector('#notificationul > li:last-child')).find('img').length;
    if ($("#notificationul").scrollTop() + $("#notificationul").innerHeight() >= $("#notificationul")[0].scrollHeight - 20 && noNotificationLength == 0) {
        notificationStart = notificationStart + notificationEnd;
        getNotifications(notificationStart,false ,notificationEnd);
    }
    IsCalledFromNotification = false;
}


function formatNotificationDate(dateTime) {
    if (dateTime != null && dateTime != undefined && dateTime.length > 0) {
        let date = dateTime.split(" ")[0];
        if (date.indexOf("-") != -1) {
            let time = formatAMPM(new Date(dateTime));
            let dateArr = date.split("-");
            let day = getDayUsingDate(dateArr[0], dateArr[1], dateArr[2]);
            if (day == "Today" || day == "Yesterday") {
                return `${day} at ${time}`;
            }
            else {
                return `${date} at ${time}`;
            }
        }

    }
    return "";
}

function getDayUsingDate(year, month, day) {
    let today = new Date();
    today.setHours(0);
    today.setMinutes(0);
    today.setSeconds(0);
    today.setMilliseconds(0);
    let compDate = new Date(year, month - 1, day); // month - 1 because January == 0
    let diff = today.getTime() - compDate.getTime(); // get the difference between today(at 00:00:00) and the date
    if (compDate.getTime() == today.getTime()) {
        return "Today";
    } else if (diff <= (24 * 60 * 60 * 1000)) {
        return "Yesterday";
    } else {
        return compDate.toDateString(); // or format it what ever way you want
    }
}

function formatAMPM(date) {
    var hours = date.getHours();
    var minutes = date.getMinutes();
    var ampm = hours >= 12 ? 'AM' : 'PM';
    hours = hours % 12;
    hours = hours ? hours : 12; // the hour '0' should be '12'
    minutes = minutes < 10 ? '0' + minutes : minutes;
    return `${hours}:${minutes}  ${ampm} `;
}

