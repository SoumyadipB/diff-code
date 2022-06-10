$(document).ready(function () {
    //on radio button click of update user location modal
    $('input[type=radio][name=site]').change(function () {
        if (this.value === '1') {
            $('#onsiteInformation').hide();
        }
        else if (this.value === '2' || this.value === '3') {
            $('#onsiteInformation').show();
            getMarketAreasForLocation();

            $("#locStartDate").datepicker({
                dateFormat: "dd/mm/yy",
                maxDate: 0,
            }).datepicker("setDate", new Date());

            $("#locEndDate").datepicker({
                dateFormat: "dd/mm/yy",
                minDate: 0,
            });
        }
    });

    $('#locationModal').on('show.bs.modal', function () {
        $('.userLocation').parent().addClass('not-active');
    });
    $('#locationModal').on('hide.bs.modal', function () {
        $('.userLocation').parent().removeClass('not-active');
    });
})

function getMarketAreasForLocation() {
    pwIsf.addLayer({ text: C_PLEASE_WAIT });
    $.isf.ajax({
        async: false,
        url: `${service_java_URL}projectManagement/getMarketAreas`,
        success: function (data) {
            let option = '';
            $('#select_market_area').html('');
            option = `${option}<option value="-1" selected disabled>Select MarketArea</option>`;

            $.each(data, function (i, d) {
                option = `${option}<option value="${d.MarketAreaID}">${d.MarketAreaName}</option>`;
            });

            const value = parseInt(data.length) + 1;
            option = `${option}<option value="${value}" selected>Not Applicable</option>`;
            $('#select_market_area').append(option);
        },
        error: function (xhr, status, statusText) {
            console.log('An error occurred on getMarketAreas');
        },
        complete: function () {
            pwIsf.removeLayer();
        }
    });
}
