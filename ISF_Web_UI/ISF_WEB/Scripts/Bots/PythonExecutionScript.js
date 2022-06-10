$(document).ready(function () {
    getPythonBaseVersion();
}
);

function getPythonBaseVersion() {

    $.isf.ajax({
        async: false,
        type: "GET",
        url: service_java_URL + "botStore/getLanguageBaseVersion",
        dataType: "json",
        // crossdomain: true,
        success: function (data) {
            if (!data.isValidationFailed) {
                var versions = data.responseData;
                $("#selectVersion").empty();
                $("#selectVersion").append("<option value=-1>Select Version</option>");
                $('#selectVersion').index = -1;
                $.each(versions, function (i, d) {
                    if (d["languageBaseVersion"] === PythonDefaultBaseVersion) {
                        $('#selectVersion').append('<option selected value="' + d["languageBaseVersionID"] + '">' + d["languageBaseVersion"] + '</option>');
                    }
                    else {
                        $('#selectVersion').append('<option value="' + d["languageBaseVersionID"] + '">' + d["languageBaseVersion"] + '</option>');
                    }
                })
            }
        },
        error: function () {
            pwIsf.removeLayer();
            console.log("Error in Python version API calling");
        },
        complete: function () {
            pwIsf.removeLayer();
        }
    });

    $('#selectVersion').select2({
        closeOnSelect: true
    });

    $("#selectVersion").on('change', function () {
        //validateTransferButton();
    })
}