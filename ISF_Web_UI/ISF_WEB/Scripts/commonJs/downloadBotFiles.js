
function downloadBotFiles(jsonObj) {
    let rpaID = jsonObj.botId;
    let fileName = jsonObj.fileName;
    let u = "fileName=" + fileName + "&rpaID=" + rpaID;
    let fileDownloadUrl;

    if (rpaID && fileName) {
        pwIsf.addLayer({ text: "Please wait ..." });
        $.isf.ajax({
            url: service_java_URL + "externalInterface/DownloadFileFromContainer?" + u,
            success: function (data) {
                data = data.responseData;
                fileDownloadUrl = data;
                window.open(fileDownloadUrl, '_blank');
            },
            error: function (xhr, status, statusText) {
                // Blank
            },
            complete: function (xhr, statusText) {
                pwIsf.removeLayer();
            }
        });
    }
    else {
        pwIsf.alert({ msg: "Bot ID or File name missing", type: "error" });
    }
}
