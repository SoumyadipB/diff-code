

function ConvertTimeZoneInPreferance(object, colsObj) {

    var UserTimeZone = localStorage.getItem("UserTimeZone");
    if (UserTimeZone == null || UserTimeZone == undefined) {
        UserTimeZone = 'Asia/Kolkata';
    }
    moment.tz.setDefault("Asia/Kolkata");
    for (var x in object) {
        if (object.hasOwnProperty(x)) {
            if (typeof object[x] == 'object') {
                ConvertTimeZoneInPreferance(object[x], colsObj);
            }
            $.each(colsObj, function (indx, col) {
                if (x == col) {
                    if (object[x] != null) {
                        var inputDate = '';
                        if (typeof object[x] == "number") {
                            
                            inputDate = moment(object[x]).tz('Asia/Kolkata');
                            
                        }
                        else {

                            inputDate = moment(object[x].toString()).tz('Asia/Kolkata'); 
                            
                           
                        }
                       
                        //var newVal = moment.tz(inputDate, UserTimeZone).format('YYYY-MM-DD HH:mm') + " " + moment.tz(inputDate, UserTimeZone).zoneAbbr();
                        var newVal = moment.tz(inputDate, UserTimeZone).format('YYYY-MM-DD HH:mm');
                        object[x] = newVal;
                    }
                }
            });
        }
    } 
       
}


function ConvertTimeZones(object, colsObj) {

    var UserTimeZone = localStorage.getItem("UserTimeZone");  

    moment.tz.setDefault("Asia/Kolkata");
    for (var x in object) {
        if (object.hasOwnProperty(x)) {
            if (typeof object[x] == 'object') {
                ConvertTimeZones(object[x], colsObj);
            }
            $.each(colsObj, function (indx, col) {
                if (x == col) {
                    if (object[x] != null)
                    {
                        var inputDate = '';
                        if (typeof object[x] == "number") {
                            inputDate = new Date(object[x]);
                        }
                        else { 
                            inputDate = new Date(object[x].toString());
                        }                       

                        //var newVal = moment.tz(inputDate, UserTimeZone).format('YYYY-MM-DD HH:mm') + " " + moment.tz(inputDate, UserTimeZone).zoneAbbr();
                        var newVal = moment.tz(inputDate, UserTimeZone).format('YYYY-MM-DD HH:mm');
                        object[x] = newVal;
                    }                    
                }
            });
        }
    }    
}




function ConvertTimeZonesDT(object, colsObj) {

    var UserTimeZone = localStorage.getItem("UserTimeZone");

    moment.tz.setDefault("Asia/Kolkata");
    for (var x in object) {
        if (object.hasOwnProperty(x)) {
            if (typeof object[x] == 'object') {
                ConvertTimeZonesDT(object[x], colsObj);
            }
            $.each(colsObj, function (indx, col) {
                if (x == col) {
                    if (object[x] != null) {
                        var inputDate = '';
                        if (typeof object[x] == "number") {
                            inputDate = new Date(object[x]);
                        }
                        else {
                            inputDate = new Date(object[x].toString());
                        }

                        var newVal = moment.tz(inputDate, UserTimeZone).format('YYYY-MM-DD HH:mm')/* + " " + moment.tz(inputDate, UserTimeZone).zoneAbbr()*/;
                        object[x] = newVal;
                    }
                }
            });
        }
    }
}

function GetConvertedDate(dt) {
    var UserTimeZone = localStorage.getItem("UserTimeZone");
   // moment.tz.setDefault("Asia/Calcutta");
    return newVal = moment.tz(new Date(dt.toString()), UserTimeZone).format('YYYY-MM-DD');
}

function GetConvertedTime(dt) {
    var UserTimeZone = localStorage.getItem("UserTimeZone");
    return newVal = moment.tz(new Date(dt.toString()), UserTimeZone).format('HH:mm');
}

function ConvertDateTime_tz(dt, module = null)
{
    var newVal, UserTimeZone;
    if (module === "neUpload") {
        getUserPreferenceTimeZone(signumGlobal);
        UserTimeZone = localStorage.getItem("UserTimeZone");
        //NE Upload format
        newVal = `${moment.tz(new Date(dt.toString()), UserTimeZone).format('HH:mm A DD MMM YY')} ${moment.tz(new Date(dt.toString()), UserTimeZone).zoneAbbr()}`;
    }
    else {
        UserTimeZone = localStorage.getItem("UserTimeZone");
        newVal = `${moment.tz(new Date(dt.toString()), UserTimeZone).format('YYYY-MM-DD HH:mm:ss')} ${moment.tz(new Date(dt.toString()), UserTimeZone).zoneAbbr()}`;
    }
    return newVal;
}

function GetConvertedTime_tz(dt) {
    var UserTimeZone = localStorage.getItem("UserTimeZone");
    moment.tz.setDefault(UserTimeZone);
    return newVal = moment(dt.toString()).tz("Asia/Calcutta").format('HH:mm:ss');
}
function GetConvertedDate_tz(dt) {
    var UserTimeZone = localStorage.getItem("UserTimeZone");
    moment.tz.setDefault(UserTimeZone);
    return newVal = moment(dt.toString()).tz("Asia/Calcutta").format('YYYY-MM-DD');
}
function GetConvertedDate_WOPlan(dt) {
    var UserTimeZone = localStorage.getItem("UserTimeZone");
    return newVal = moment.tz(new Date(dt.toString()), UserTimeZone).format('YYYY-MM-DD') + " " + moment.tz(new Date(dt.toString()), UserTimeZone).zoneAbbr();
}
function GetConvertedTime_IST(dt) {
    var UserTimeZone = localStorage.getItem("UserTimeZone");
    moment.tz.setDefault(UserTimeZone);
    var TimeZone = "Asia/Calcutta";
    var newVal = moment.tz(new Date(dt), TimeZone);
    //var Val2 = newVal.clone().tz("Asia/Kolkata").format('HH:mm:ss');
    newVal = newVal.utc().format('HH:mm:ss');
    console.log(newVal);
    return newVal;
}  
function GetConvertedTime_WOPlan(dt) {
    var UserTimeZone = localStorage.getItem("UserTimeZone");
    return newVal = moment.tz(new Date(dt.toString()), UserTimeZone).format('HH:mm:ss');
}

function GetConvertedTimeWO(dt) {
    var timeZone = "";
    var UserTimeZone = localStorage.getItem("UserTimeZone");
    if (UserTimeZone == null || UserTimeZone == "" || UserTimeZone == undefined) {
        timeZone = "Asia/Calcutta";
    }
    else {
        timeZone = UserTimeZone;
    }
    moment.tz.setDefault("Asia/Calcutta");
    return newVal = moment(dt.toString()).tz(timeZone).format('HH:mm:ss');
}

function GetConvertedDateWO(dt) {
    var UserTimeZone = localStorage.getItem("UserTimeZone");
    var timeZone = "";
    if (UserTimeZone == null || UserTimeZone == "" || UserTimeZone == undefined) {
        timeZone = "Asia/Calcutta";
    }
    else {
        timeZone = UserTimeZone;
    }
    moment.tz.setDefault("Asia/Calcutta");
    return newVal = moment(dt.toString()).tz(timeZone).format('YYYY-MM-DD');
}

function GetConvertedDateTime(dt) {
    
    var UserTimeZone = 'Asia/Kolkata';
    var getUserTimeZone = localStorage.getItem("UserTimeZone");
    moment.tz.setDefault(getUserTimeZone);

    if (dt != null && dt != undefined) {

        var inputDate = moment(dt.toString()).tz(getUserTimeZone);
        if (inputDate != null && inputDate != undefined) {
            return newVal = moment.tz(new Date(inputDate.toString()), UserTimeZone).format('YYYY-MM-DD HH:mm');
        }
    }
        return newVal = moment.tz(new Date(), UserTimeZone).format('YYYY-MM-DD HH:mm');
     

}

function GetConvertedDateTimeIST(dt) {

    var UserTimeZone = 'Asia/Kolkata';
    var getUserTimeZone = localStorage.getItem("UserTimeZone");
    moment.tz.setDefault(UserTimeZone);

    if (dt != null && dt != undefined) {

        var inputDate = moment(dt.toString()).tz(UserTimeZone);
        if (inputDate != null && inputDate != undefined) {
            return newVal = moment.tz(new Date(dt.toString()), UserTimeZone).format('YYYY-MM-DD HH:mm');
        }
    }
    return newVal = moment.tz(new Date(), UserTimeZone).format('YYYY-MM-DD HH:mm');


}

function getUserPreferenceTimeZone(signum) {
    $.isf.ajax({
        url: `${service_java_URL}accessManagement/getUserPreferences/${signum}`,
        type: "GET",
        crossDomain: true,
        async: false,
        success: function (data) {
            if (data.length !== 0) {
                if (data.defaultZoneValue !== null) {
                    localStorage.setItem("UserTimeZone", data.defaultZoneValue.split(" - ")[1].trim());
                }
            }
        },
        error: function (xhr, status, statusText) {
            console.log('An error occurred');
        }
    });
}