var path = (document.location.pathname).split('/');
var rootDir = '';
if (path.length > 2) {
    if (path[1].indexOf("ISF") > -1) {
        rootDir = `/${path[1]}`;
    }
}


function getSessionValuesByKey(passkey = '') {
    
    let getData;
    $.ajax({
        contentType: "application/json",
        async: false,
        url: rootDir + "/Data/GetSessionDataByKey?key=" + passkey,
        success: function (data) {
            getData = data;
        },
        error: function (xhr, status, statusText) {
            console.log('An error occurred');
        }
    });

    return getData;
}


function getAllSessionValues() {
    let getData;
    $.ajax({
        contentType: "application/json",
        async: false,
        url: rootDir + "/Data/GetAllSession",
        success: function (data) {
            getData = data;
        },
        error: function (xhr, status, statusText) {
            console.log('An error occurred');
        }
    });

    return getData;
}

function getConfigurationDetail() {
    let getData;
    $.ajax({
        contentType: "application/json",
        async: false,
        url: rootDir + "/Data/GetConfiguration",
        success: function (data) {
            getData = data;
        },
        error: function (xhr, status, statusText) {
            console.log('An error occurred');
        }
    });

    return getData;
}

function set_Session_Values(passKey,val) {
    let getData;
    let values = JSON.stringify(val);
    $.ajax({
        contentType: "application/json",
        async: false,
        type: 'POST',
        data: values,
        url: rootDir + "/Data/SetSessionData?key=" + passKey,
        success: function (data) {
            //getData = data;
        },
        error: function (xhr, status, statusText) {
            console.log('An error occurred');
        }
    });

    //return getData;
}
