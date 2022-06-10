
    $(document).ready(function () {
        //Fill selects without WS
        fillProjectType();
        fillDocumentType();

        $('#cpm').focus();
        //Fill selects with WS
        getServiceAreaDetails();
        getOpportunities();
        //getCustomers();
        getCountries();
        getMarketAreas();
        getDemandOwningCompanies();
        getOperationalManager();

        getRPMList();
        intialButtons();

        $('#project_creator').val(signumGlobal);

        setTimeout(function () {
            $(document).on("change", "#select_market_area", function () {
                fillProjectName();
                if (!isDisabled('select_country')) {
                    getCountrybyMarketAreaID();
                }
            });
        }, 1000);

        $(document).on("change", "#select_country", function () {
            if (!isDisabled('select_country')) {
                if ($("#select_country").val() == 0) {
                    $('#select_customer_name').empty();
                    $('#select_customer_name').append('<option value="0">Select One</option>');
                } else {
                    var _promise = getCustomers($("#select_country").val());
                    _promise.done(function () {
                        fillProjectName();
                    });
                }
            }

        });

        var today = new Date();
        var dd = today.getDate();
        var mm = today.getMonth() + 1; //January is 0!

        var yyyy = today.getFullYear();
        if (dd < 10) {
            dd = '0' + dd;
        }
        if (mm < 10) {
            mm = '0' + mm;
        }
        var today = yyyy + '-' + mm + '-' + dd;
        $("[id$=start_date]").attr('min', today);
        $("[id$=end_date]").attr('min', today);

        $(document).on("blur", "#start_date, #end_date", function () {
            //hideErrorMsg('start_date_invalid');
            if (this.id == "end_date") {
                hideErrorMsg('end_date_invalid');
            }
            let enteredDate = new Date($(this).val());
            let currentDate = new Date(today);
            let datePattern = /^\d{4}$/;
            if (enteredDate < currentDate) {
                $(this).val("").trigger("change");
                //$(this).focus();
                if (this.id == "start_date") {
                    showErrorMsg("start_date_invalid", "Start Date must not be less than today's date");
                }
                else {
                    showErrorMsg("end_date_invalid", "End Date must not be less than today's date");
                }
            }
            else if (!datePattern.test(enteredDate.getFullYear())) {
                $(this).val("").trigger("change");
                //$(this).focus();
                if (this.id == "start_date") {
                    showErrorMsg("start_date_invalid", "Invalid date format. Format should be of type mm/dd/yyyy");
                }
                else {
                    showErrorMsg("end_date_invalid", "Invalid date format. Format should be of type mm/dd/yyyy");
                }
            }

            
        })

    // Creating collapsible DIV
        var coll = document.getElementsByClassName("collapsible");
        var i;

        for (i = 0; i < coll.length; i++) {
            coll[i].addEventListener("click", function () {
                this.classList.toggle("active");
                var content = this.nextElementSibling;
                if (content.style.display === "block") {
                    content.style.display = "none";
                } else {
                    content.style.display = "block";
                }
            });
        }

    });

    function TextOnly(control) {
        var node = $('#' + control.id);
        node.val(node.val().replace(/[^A-Za-z]/g, ''));
        //var firstChar=node.val().substring(0,1);
        //if (firstChar != 'e' && firstChar != 'E') {
        //    node.val('');
        //}
        if (node.val().length > 7) {
            node.val(node.val().substring(0, 7));
        }
        

    }


