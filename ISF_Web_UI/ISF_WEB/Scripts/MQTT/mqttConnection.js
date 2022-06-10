
//shared worker implemented for multiple tabs and windows 
const worker = new SharedWorker(UiRootDir + '/Scripts/MQTT/worker.js');
worker.port.postMessage([getTopicMQTT("Notifications", signumGlobal), UiRootDir, environment, getTopicMQTT("NEUPLOAD", signumGlobal)]);

var mqttNotifications = 0;

// generic topic name
function getTopicMQTT(module, signum) {
    if (module !== null && signum !== null && environment !== null) {
        return `ISF/${module}/${environment}/${signum.toLowerCase()}`;
    }
    else {
        return false;
    }
}


worker.port.onmessage = function (e) {
    //e.data is the received data from broker
    if (e!==null && e!==undefined && e!=='' && e.data !==null && e.data!== undefined && e.data !== '') {
        console.log(e.data);
        var sessionData = JSON.parse(ActiveProfileSession);
        if (sessionData !== null && sessionData !== undefined && (sessionData.roleID === 5 || sessionData.roleID === 7 ||
            sessionData.roleID === 14 || sessionData.roleID === 15 || sessionData.roleID === 1)) {
            makeBellIconTilted();
            if (JSON.parse(e.data).source === "NE") {
                localStorage.setItem('currentRefID', `${JSON.parse(e.data).neUploadedId}`);
                var refID = localStorage.getItem('currentRefID');
                console.log(`RefID [MQTT]:: ${refID}`);

                if (JSON.parse(e.data).currentStatus && JSON.parse(e.data).currentStatus === "Failed") {
                    $("#uploadNEInProgress").css('display', 'none');
                    pwIsf.alert({ msg: "Your request has been failed, Please Try again.", type: "error" });
                } else {
                    checkNEUploadFileStatus(JSON.parse(e.data).neUploadedId);
                }
            }
            checkMqttNotification(JSON.parse(e.data));
        }
    }
    else {
        console.log('No data available');
    }
}

function makeBellIconTilted()
{
    // make bell Icon tilted.
    let element = $("#notificationImg");
    if (element != undefined && element != null && element.length > 0) {
        $("#notificationImg").attr('src', '../Content/images/notification-ring.svg');
        localStorage.setItem("isNotificationReceived", true);
    }
}

function checkMqttNotification(notificationData) {
    if (notificationData.source === "NE") {
        if (notificationData.currentStatus === "Validated") {
            mqttNotification(notificationData,notificationData.currentStatus);
        }
    }
    else {
        mqttNotification(notificationData);
    }
}

function mqttNotification(notificationData, status = null) {
    var msg = "";
    if (status === "Validated") {
            msg = "NE file validated.Your action required";
            if (!("Notification" in window)) {
                alert("This browser does not support desktop notification");
            }
            else if (Notification.permission === "granted") {
                // If it's okay let's create a notification
                const options = {
                    body: msg,
                    icon: '/Content/images/Ericsson/ericsson.jpg',
                    requireInteraction: true
                }
                mqttNotifications = new Notification("Notification Alert!", options);
                mqttNotifications.onclick = function (event) {
                    window.focus();
                }
        }
    }
    else {
        var name = lowercase(notificationData.name);
        if (name.length > 19) {
            name = name.substring(0, 19) + "..";
        }
        var comment = notificationData.comment.toLowerCase();
        if (comment.length > 24) {
            comment = comment.substring(0, 24) + "..";
        }
        msg = `${notificationData.signum} (${name}) - \n`;
        msg = `${msg}WO ${notificationData.woid} \n`;
        msg = `${msg}${comment} \n`;
        msg = `${msg}Click to view Work Order`;
        // Let's check if the browser supports notifications
        if (!("Notification" in window)) {
            alert("This browser does not support desktop notification");
        }
        // Let's check whether notification permissions have already been granted
        else if (Notification.permission === "granted") {
            // If it's okay let's create a notification
            let options = {
                body: msg,
                icon: '/Content/images/Ericsson/ericsson.jpg',
                requireInteraction: true
            }
            mqttNotifications = new Notification("Notification Alert!", options);
            mqttNotifications.onclick = function (event) {
                window.focus();
                //redirect to DE
                openDeliveryExecutionTab(notificationData.woid);
            }
        }
    }
    setTimeout(mqttNotifications.close.bind(mqttNotifications), 15000);
}

function openDeliveryExecutionTab(woId) {
    let currentUrl = window.location.href;
    let hostUrl = `${window.location.protocol}//${window.location.hostname}:${window.location.port}/${UiRootDir.trim()}`;

    window.IsNotficationNavigation = true;
    window.woIDFW = woId;

    if (currentUrl != undefined && currentUrl != null) {
        if (!currentUrl.includes("DeliveryExecution/WorkorderAndTask")) {
            window.location.href = hostUrl + '/DeliveryExecution/WorkorderAndTask?woID=' + woId;
        }
        else {
            var hasClass = $('#liMyWorkTab').hasClass("active");
            if (hasClass === false) {
                window.location.href = hostUrl + '/DeliveryExecution/WorkorderAndTask?woID=' + woId;
            }
            else {
                RequestWorkOrderUsingWOId(woId);
            }
        }
    }
}

function lowercase(name) {
    var splittedName = name.split(' ');
    var newName = [];
    for (var x = 0; x < splittedName.length; x++) {
        newName.push(splittedName[x].charAt(0) + splittedName[x].slice(1).toLowerCase());   
    }
    return newName.join(' ');
}
