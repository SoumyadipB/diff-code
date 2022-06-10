 $(document).ready(function () {
        clearInterval(idleInterval);
        idleInterval = setInterval(timerIncrement, 1000); // 1 second  	
});

var idleTime = 0;
var sessionNotification = 0;
var notificationTimeout = "";
var idleInterval = "";
localStorage.setItem("idleTime", 0);

//To extend session
function extendSession() {
    localStorage.setItem("idleTime", 0);
    clearInterval(idleInterval);
    idleInterval = setInterval(timerIncrement, 1000);
    let sessionUrl = UiRootDir + "/Data/ExtendSession";
    let tokenData;
        $.ajax({
            url: sessionUrl,
            type: 'GET',
            async: false,
            success: function (response) {
                tokenData = JSON.parse(response);
                localStorage.setItem("ExpirationTime", tokenData.ExpirationTime);
                localStorage.setItem("currentExpirationTime", new Date(new Date().getTime() + Number(localStorage.getItem("ExpirationTime") * 60000)));
            },
            failure: function (response) {
                alert(response.responseText);
            },
            error: function (response) {
                console.log(response);
            }
        });
    return tokenData;
}

function timerIncrement() {   
   
    if (!(window.location.href.includes("FlowChartExecution") || window.location.href.includes("activityMonthly") || window.location.href.includes("activityDaily") || window.location.href.includes("activityHourly") || window.location.href.includes("FlowChartWorkOrder") || window.location.href.includes("FlowChart"))) {
    idleTime = parseInt(localStorage.getItem("idleTime"));
    idleTime = idleTime + 1;
        localStorage.setItem("idleTime", idleTime);
     
        if (localStorage.getItem("currentExpirationTime") !== null && localStorage.getItem("currentExpirationTime") !== "null") {
            if ((((new Date(localStorage.getItem("currentExpirationTime")) - new Date()) / 1000) / 60) < 1) {
                let btnJson = {
                    'Continue': {
                        'action': function () {        
                            extendSession();//Continue session
                            clearInterval(countdownTimer);//Stop countdown
                            clearInterval(blink);//Stop blinking countdown
                            if (sessionNotification !== undefined && sessionNotification !== "undefined" && sessionNotification !== 0) {
                                sessionNotification.close();
                                clearTimeout(notificationTimeout);
                            }
                        },
                        'class': 'btn btn-success'
                    },
                    'Logout': {
                        'action': function () {
                            clearInterval(idleInterval);
                            if (sessionNotification !== undefined && sessionNotification !== "undefined" && sessionNotification !== 0) {
                                sessionNotification.close();
                                clearTimeout(notificationTimeout);
                            }
                            if (InitiateSignalRConnection() === true) {
                                UpdateFloatingWindowServeMethods(true, false);
                            }
                            setTimeout(function () {
                                localStorage.setItem('sessionOut', 'true');
                                window.location.href = `${UiRootDir}/Login/SignOut?returnUrl=SessionTimeout`;
                            }, 2000);
                        },
                        'class': 'btn btn-danger'
                    },

                };

                let warningIcon = '<i class="fa fa-exclamation-triangle" style="font-size:22px;color:#e6c414;"></i>';
                //Pop-up to warn about session ending
                pwIsf.confirm({
                    title: `${warningIcon} Session End Alert   <p class="pull-right" id="demo" style="color:#3CFE33;font-size:12px;margin-top:8px;font-weight:bold"></p><label class="pull-right" style="font-size:12px;margin-top:8px;">Time Left(mm:ss) : </label>`, msg: 'Your session is about to end. Do you want to continue?',
                    'buttons': btnJson
                })

                let time = 60;

                // Update the count down every 1 second
                let countdownTimer = setInterval(function () {
                    if (localStorage.getItem("currentExpirationTime") !== null && localStorage.getItem("currentExpirationTime") !== "null") {
                        if (localStorage.getItem("idleTime") == 0) {
                            clearInterval(countdownTimer);//Stop countdown
                            clearInterval(blink);//Stop blinking countdown
                            clearInterval(idleInterval);
                            if (sessionNotification != undefined && sessionNotification != "undefined" && sessionNotification != 0) {
                                sessionNotification.close();
                                clearTimeout(notificationTimeout);
                            }
                            idleInterval = setInterval(timerIncrement, 1000);
                            pwIsf.confirm.hide();
                        }
                        else {
                            let min = parseInt(time / 60);
                            let sec = (time % 60);
                            var finalTime = " " + (time > 60 || time == 60) ? (min < 10 ? `0${min}` : min) + ":" + (sec < 10 ? `0${sec}` : sec) : "00:" + (sec < 10 ? "0" + sec : sec);
                            finalTime = finalTime == null ? "" : finalTime;
                            if (time < 11) {
                                $("#demo").css("color", "#e24944");
                                document.getElementById("demo").innerHTML = "<blink>" + finalTime + "</blink>";
                            }
                            else {
                                    let elem = document.getElementById("demo");
                                    if (typeof elem !== 'undefined' && elem !== null) {
                                        elem.innerHTML = finalTime;
                                    }                                                             
                            }
                            let currentExpirationTime = new Date(localStorage.getItem("currentExpirationTime"));
                            let currentRuningTime = new Date();
                            time--;
                            if (time <= 0) {
                                document.getElementById("demo").innerHTML = "<blink>" + "00:00" + "</blink>";    
                                clearInterval(countdownTimer);
                                try {
                                    if (InitiateSignalRConnection() === true) {
                                        UpdateFloatingWindowServeMethods(true, false);
                                    }
                                }catch (err) {
                                    console.log(err);
                                    triggerForceLogout();
                                } finally {
                                    console.log('call finally block executed');
                                    //Block of code to be executed regardless of the try / catch result
                                    setTimeout(function () {
                                        localStorage.setItem('sessionOut', 'true');
                                        window.location.href = `${UiRootDir}/Login/SignOut?returnUrl=SessionTimeout`;
                                    }, 2000);
                                }
                                
                            }

                            if (currentExpirationTime.getTime() < currentRuningTime.getTime()) {
                                clearInterval(countdownTimer);
                                if (InitiateSignalRConnection() == true) {
                                    UpdateFloatingWindowServeMethods(true, false);
                                }
                                setTimeout(function () {
                                    localStorage.setItem('sessionOut', 'true');
                                    window.location.href = `${UiRootDir}/Login/SignOut?returnUrl=SessionTimeout`;
                                }, 2000);
                            }
                        }
                    }
                    else {
                        if (sessionNotification !== undefined && sessionNotification !== "undefined" && sessionNotification !== 0) {
                            sessionNotification.close();
                            clearTimeout(notificationTimeout);
                        }
                        ManualLogout();
                    }                   

                }, 1000);
                clearInterval(idleInterval);
                //blinking effect
                let blink = setInterval(function () {
                    $('blink').each(function () {
                        $(this).css('visibility') == 'visible' ? $(this).css('visibility', 'hidden') : $(this).css('visibility', 'visible');
                    });
                }, 500);

                notifyMeSession();

            }
        }
        else {
            ManualLogout();
        }
    
    }
    
}

function UserSessionLogout() {
    var d = new Date();
    var logoutTime = d.getFullYear().toString() + "-" + ((d.getMonth() + 1).toString().length == 2 ? (d.getMonth() + 1).toString() : "0" + (d.getMonth() + 1).toString()) + "-" + (d.getDate().toString().length == 2 ? d.getDate().toString() : "0" + d.getDate().toString()) + " " + (d.getHours().toString().length == 2 ? d.getHours().toString() : "0" + d.getHours().toString()) + ":" + ((parseInt(d.getMinutes())).toString().length == 2 ? (parseInt(d.getMinutes())).toString() : "0" + (parseInt(d.getMinutes())).toString()) + ":" + ((d.getSeconds()).toString().length == 2 ? (d.getSeconds()).toString() : "0" + (d.getSeconds()).toString());
    var logId = '';
    var userLoggedIn = signumGlobal;
    localStorage.setItem("userLoggedIn", "AzureAd");

    $.isf.ajax({

        url: `${service_java_URL}accessManagement/getLoginHistory/${userLoggedIn}`,
        async: false,
        success: function (data) {
            console.log("success");
            logId = data[0].logID;
            $.isf.ajax({
                type: "POST",
                url: `${service_java_URL}accessManagement/updateLoginHistory/${userLoggedIn}/${logId}/${logoutTime}`,
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

function notifyMeSession() {
    // Let's check if the browser supports notifications
    if (!("Notification" in window)) {
        alert("This browser does not support desktop notification");
    }
    // Let's check whether notification permissions have already been granted
    else if (Notification.permission === "granted") {
        // If it's okay let's create a notification
        let options = {
            body: 'Your session is about to expire, click here to go to ISF.',
            icon: '/Content/images/Ericsson/ericsson.jpg',
            requireInteraction: true
        }
        sessionNotification = new Notification("Session Expire Alert!", options);
        sessionNotification.onclick = function (event) {
            window.focus();
        }
        notificationTimeout = setTimeout(sessionNotification.close.bind(sessionNotification), 15000);
    }    
}

//trigger logout button if confirmedBox is open.
function triggerForceLogout() {
    setTimeout(function () {
        localStorage.setItem('sessionOut', 'true');
        window.location.href = `${UiRootDir}/Login/SignOut?returnUrl=SessionTimeout`;
    }, 100);
}
