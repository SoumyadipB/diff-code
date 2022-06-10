/*--------OnChange: File Selection; xlsx/xls Validation------------*/
var selectedFileName;
$(document).ready(function () {
    $(C_MssLeaveUpload).change(function (e) {
        selectedFileName = e.target.files[0];
        var bulkFileName = $(C_MssLeaveUpload).val().split('\\').pop();
        var ext = bulkFileName.split('.').pop();
        if (ext ==='csv') {
            pwIsf.alert({ msg: `The file ${bulkFileName} has been selected`, type: 'info', autoClose: 2 });
            $(C_BtnUploadMssLeave).prop("disabled", false);
        }
        else {
            pwIsf.alert({ msg: 'Wrong file type selected', type: 'error', autoClose: 3 });
            $(C_BtnUploadMssLeave).prop("disabled", true);
        }
    });
});

/*--------show Progress Bar for Upload MSS Leave------------*/
function uploadMssLeave(){
    pwIsf.addLayer({ text: "Uploading file please wait ...", progressId: 'mssLeaveUploadProgress', showSpin: false });
    var bulkFileName = selectedFileName;
    var data = new FormData();
    data.append("uploadedBy", signumGlobal);
    data.append("file", bulkFileName);
    const reqOpt = {
        url : `${service_java_URL_VM}activityMaster/v2/uploadFileForESSLeaveData`,
        progressBarId : 'mssLeaveUploadProgress',
       frmData :data,
        onDone : function (result) {
            pwIsf.removeLayer();
            responseHandler(result);
            $(C_MssLeaveUpload).val("");
            $(C_BtnUploadMssLeave).prop("disabled", true);
        }
    }
    sendRequest(reqOpt);
}
/*--------POST: Upload MSS Leave------------*/
function sendRequest(opt) {
    const url = opt.url;
    const progressId = opt.progressBarId;
    const sendData = opt.frmData;
    const reqPromise = $.isf.ajax({
        type: "POST",
        enctype: "multipart/form-data",
        url: url,
        returnAjaxObj: true,
        data: sendData,
        processData: false,
        contentType: false,
        cache: false,
        timeout: 600000,
        xhr: function () {
            var myXhr = $.ajaxSettings.xhr();
            if (myXhr.upload) {
                // For handling the progress of the upload
                myXhr.upload.addEventListener('progress', function (e) {
                    if (e.lengthComputable) {
                        const prog = `#${progressId}`;
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
    reqPromise.fail(function ()
    {
        pwIsf.removeLayer();
        pwIsf.alert({ msg: "An error has occurred, please check your file again!", type: "error" });
    });

    reqPromise.always(function () {
        pwIsf.removeLayer();
    });
}
