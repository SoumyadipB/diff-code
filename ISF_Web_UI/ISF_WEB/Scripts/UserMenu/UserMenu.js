function getUserMenuFromSession() {
    let getData;
    $.isf.ajax({
        type: "POST",
        async: false,
        url: UiRootDir + "/Login/GetUserMenuSessionDetails?UiRootDir=" + UiRootDir,
        success: function (response) {
            getData = response;
        },
        error: function () {
            console.log("Error in loading menu");
            window.location.reload();
        }
    });

    return getData;
}