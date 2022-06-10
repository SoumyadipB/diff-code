var marketAreaArray = ["MELA", "MOAI", "MMEA", "MANA", "MNEA"];
let commonHeadersforAllAjaxCall = {};
(function ($) {
    $.isf = {
        ajax: function (ajaxConfig) {
            const tokenData = extendSession();
            if (tokenData) {
                var marketAreaName = tokenData.Organisation;
                ajaxConfig.headers = {
                    "MarketArea": marketAreaName,
                    "Signum": tokenData.Signum,
                    "Role": tokenData.Role,
                    "X-Auth-Token": tokenData.Token
                };
                commonHeadersforAllAjaxCall = ajaxConfig.headers;
            }
            if (ajaxConfig.returnAjaxObj) {
                return $.ajax(ajaxConfig);
            }
            else {
                $.ajax(ajaxConfig);
            }
        }
    }; 
}(jQuery));
