/*---------Call: Open Tab------------*/
function openTrainingTab(evt, trainingName) {
    var i, tabcontent, tablinks;
    tabcontent = document.getElementsByClassName("tabcontent");
    for (i = 0; i < tabcontent.length; i++) {
        tabcontent[i].style.display = "none";
    }
    tablinks = document.getElementsByClassName("tablinks");
    for (i = 0; i < tablinks.length; i++) {
        tablinks[i].className = tablinks[i].className.replace(" active", "");
    }
    document.getElementById(trainingName).style.display = "block";
    evt.currentTarget.className += " active";
}

//var service_java_URL = "https://isfdevservices.internal.ericsson.com:8443/isf-rest-server-java_dev_major/";
var fl = null;
/*--------OnChange: File Selection; txt Validation------------*/
$(document).ready(function () {
    $("#btnUploadITM").prop("disabled", true);
    $("#btnUploadMaster").prop("disabled", true);
   document.getElementById("defaultOpenTab").click();
    $('#itmTrainingUpload').change(function (e) {
        fl = e.target.files[0];
        var filename = $('#itmTrainingUpload').val().split('\\').pop();
        var ext = filename.split('.').pop();
        if (ext == 'txt') {
            $("lbltxt").text = fl.name;
            pwIsf.alert({ msg: 'The file "' + filename + '" has been selected', type: 'info', autoClose: 2 });
            $("#btnUploadITM").prop("disabled", false);
        }
        else {
            pwIsf.alert({ msg: 'Wrong file type selected', type: 'error', autoClose: 3 });
            $("#btnUploadITM").prop("disabled", true);
        }
    });
    $('#masterTrainingUpload').change(function (e) {
        fl = e.target.files[0];
        var filename = $('#masterTrainingUpload').val().split('\\').pop();
        var ext = filename.split('.').pop();
        if (ext == 'txt') {
            $("lbltxt").text = fl.name;
            pwIsf.alert({ msg: 'The file "' + filename + '" has been selected', type: 'info', autoClose: 2 });
            $("#btnUploadMaster").prop("disabled", false);
        }
        else {
            pwIsf.alert({ msg: 'Wrong file type selected', type: 'error', autoClose: 3 });
            $("#btnUploadMaster").prop("disabled", true);
        }
    });

});

/*--------POST: Bulk Upload ITM Training------------*/
function uploadITMTraining() {
    var fileName = fl;
    var data = new FormData();
    data.append("file", fileName);
    pwIsf.addLayer({ text: "Please wait ..." });
    $.isf.ajax({
        type: "POST",
        enctype: "multipart/form-data",
        url: service_java_URL_VM + "competenceController/insertItmTrainingBulk/" + signumGlobal,
        data: data,
        processData: false,
        contentType: false,
        cache: false,
        timeout: 600000,
        success: function (data) {
            pwIsf.removeLayer();
            var resultStatus = responseHandler(data);
            if (resultStatus) {
                //$('#AddNetworkElement').modal('hide');
                //$('.modal-backdrop').remove();
                //getNetworkElementDetails();
            }
        },
        error: function (jqXHR, textStatus, errorThrown) {
            pwIsf.removeLayer();
            pwIsf.alert({ msg: "An error has occurred, please check your file again!", type: "error" });

        }
    });
}

/*--------POST: Bulk Upload Master Training------------*/
function uploadMasterTraining() {
    var fileName = fl;
    var data = new FormData();
    data.append("file", fileName);
    pwIsf.addLayer({ text: "Please wait ..." });
    $.isf.ajax({
        type: "POST",
        enctype: "multipart/form-data",
        url: service_java_URL_VM + "competenceController/insertMasterTrainingBulk/" + signumGlobal,
        data: data,
        processData: false,
        contentType: false,
        cache: false,
        timeout: 600000,
        success: function (data) {
            pwIsf.removeLayer();
            var resultStatus = responseHandler(data);
            if (resultStatus) {
                //$('#AddNetworkElement').modal('hide');
                //$('.modal-backdrop').remove();
                //getNetworkElementDetails();
            }
        },
        error: function (jqXHR, textStatus, errorThrown) {
            pwIsf.removeLayer();
            pwIsf.alert({ msg: "An error has occurred, please check your file again!", type: "error" });

        }
    });
}