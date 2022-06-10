$(document).ready(function () {
   // $("#btnaddInstantFeedback").val("Add");
    $(document).on('submit', '#addFeedbackForm', function () {
       
        return false;
    });
    $('#typeOfInstantFeedback').on("change", function (e) {
        validateForEnablingAddButton();
        //validateForEnablingCancelButton();

        
    });
    $(btnaddInstantFeedback).prop('disabled', true);
   // $(btnCancelInstantFeedback).prop('disabled', true);
    
    //For displaying "Type"(highlighted) in selectbox
    //$("#typeOfInstantFeedback").select2({
    //    placeholder: "Type"
    //});
    getInstantFeedbacks();


    
    



    //For adding datatable through API
     function getInstantFeedbacks() {

        if ($.fn.dataTable.isDataTable('#instantFeedbackTable')) {
            oInstantFeedbackTable.destroy();
            $('#instantFeedbackTable').empty();

        }
        pwIsf.addLayer({ text: "Please wait ..." });

        $.isf.ajax({
            url: service_java_URL + "activityMaster/getInstantFeedback",
            success: function (data) {
                pwIsf.removeLayer();

                $.each(data, function (i, d) {

                    let action = "<a href='#' class='icon-edit edit-InstantFeedback' data-toggle='tooltip' title='Update Feedback' onclick='updateInstantFeedback(this)'>" +
                        "<span class='fa fa-edit'  style='color:white;'> </span></a>"
                    d.action = action;
                    //let rightIconForActiveColumn = "<a class='icon-edit editRightSign' style='background-color:#4aaf4a;border: 1px solid #468e45;padding-right: 6px;padding-left: 6px;' data-toggle='tooltip' title='Active' >" +
                    //    "<span class='fa fa-check'  style='color:white;'> </span></a>";
                    //let wrongIconForActiveColumn = "<a class='icon-edit editWrongSign' style='background-color:#ca402ae6;border: 1px solid #cc1204;' data-toggle='tooltip' title='Inactive' >" +
                    //    "<span class='fa fa-times'  style='color:white;'> </span></a>";
                    if (d.isActive == true) {
                        d.activeIcon = "Active";
                    }
                    else if (d.isActive == false) {
                       d.activeIcon = "Inactive";
                    }
                })
             

                $("#instantFeedbackTable").append($('<tfoot><tr><th></th><th>ID</th><th>Feedback Text</th><th></th><th>Type</th></tr></tfoot>'));

                oInstantFeedbackTable = $('#instantFeedbackTable').DataTable({
                    searching: true,
                    responsive: true,
                    "pageLength": 10,
                    "data": data,
                    colReorder: true,
                    order: [1],
                    dom: 'Bfrtip',
                    buttons: [
                        'colvis', 'excel'
                    ],
                    "destroy": true,
                    "columns": [
                        { "title": "Action", "targets": 'no-sort', "orderable": false, "searchable": false, "data": "action" },
                        { "title": "Feedback ID", "targets": 'no-sort', "data": "instantFeedbackID" },
                        { "title": "Feedback Text", "data": "feedbackText" },
                        { "title": "Status", "data": "activeIcon"},
                        { "title": "Type", "data": "feedbackType"}

                    ],
                    initComplete: function () {

                        $('#instantFeedbackTable tfoot th').each(function (i) {
                            var title = $('#instantFeedbackTable thead th').eq($(this).index()).text();
                            if ((title != "Action"))
                                $(this).html('<input type="text" class="form-control" style="font-size:12px;"  placeholder="Search ' + title + '" data-index="' + i + '" />');
                        });
                        var api = this.api();
                        api.columns().every(function () {
                            var that = this;
                            $('input', this.footer()).on('keyup change', function () {
                                if (that.search() !== this.value) {
                                    that
                                        .columns($(this).parent().index() + ':visible')
                                        .search(this.value)
                                        .draw();
                                }
                            });
                        });

                    }

                });

                $('#instantFeedbackTable tfoot').insertAfter($('#instantFeedbackTable thead'));
                //console.log(data);

            },
            error: function (xhr, status, statusText) {
                pwIsf.removeLayer();
                console.log("failed to fetch data");
            }

        });
    }
//});
    
    
    //For functionality on click of "Add" or "Click" button(data fetched from corresponding ADD/EDIT API)
    $('#btnaddInstantFeedback').click(function () {
        var all = validationForTypeAndForm();

        //if (all[0] == true && all[3] == true) {
            //$(btnaddInstantFeedback).prop('disabled', false);
        if (this.value == "Add") {
            if (all[0] != "") {
                    //AJAX Call
                let feedbackObj = new Object();
                feedbackObj.feedbackText = all[0];
                feedbackObj.isActive = all[1];
                feedbackObj.createdBy = signumGlobal;
               
                feedbackObj.feedbackType = $('#typeOfInstantFeedback').val();
                pwIsf.addLayer({ text: "Please wait" });
                $.isf.ajax({
                    type: "POST",
                    url: service_java_URL + "activityMaster/addInstantFeedback",
                    crossdomain: true,
                    processData: true,
                    async: false,
                    contentType: 'application/json',
                    data: JSON.stringify(feedbackObj),
                    success: function (data) {
                        if (data.isValidationFailed == true) {
                            pwIsf.alert({ msg: data.formErrors[0], type: "info", autoClose: 3 });
                        }
                        else {
                            pwIsf.alert({ msg: "Instant Feedback successfully added.", type: "info", autoClose: 3 });
                            getInstantFeedbacks();
                           // tableRowBackgroundColourChange();
                            $('#feedbackText').val("");
                            $('#typeOfInstantFeedback').val("STEP");
                            //$("#typeOfInstantFeedback").select2({
                            //    placeholder: "Type"
                            //});
                            $('#radioActive').prop('checked', true);
                            $(btnaddInstantFeedback).prop('disabled', true);
                           // $(btnCancelInstantFeedback).prop('disabled', true);
                           // $('#instantFeedbackTable tbody tr:first').addClass('colorChange');
                        }
            
                    },
                    error: function (status) {
                        pwIsf.alert({ msg: "Error while adding", type: "error" });
                    },
                    complete: function () {
                        pwIsf.removeLayer();
                        
                    }
                })

            }
            

        }
        else if (this.value == "Update") {
           // this.value = "Add";

            
            $(".addFeedbackDiv").removeClass('EditCssInstantFeedback');
            //setTimeout(function () {
            //    $('#instantFeedbackTable tr').each(function (i) {
            //        $(this).removeClass('colorChange');


            //    });
            //}, 10000);
           // tableRowBackgroundColourChange();
           
            //$('#instantFeedbackTable').dataTable({
            //    "createdRow": function (row, data, dataIndex) {
            //        if (data[1] == $("#feedbackText").val() && data[2] == $("#typeOfInstantFeedback").val() ) {
            //            $(row).addClass('colorChange');
            //        }
            //    }
            //});
           // var updateFeedbackObj = updateInstantFeedback();
            //$(updateFeedbackObj).closest('tr').addClass('colorChange');

           // $('#btnCancelInstantFeedback').hide();

            //$('div[id^="radioButtonDiv"]').removeClass('col-sm-1').addClass('col-sm-2');
            //$('div[id^="addCancelButtonDiv"]').removeClass('col-lg-2').addClass('col-sm-1');
            //var all = validationForTypeAndForm();
              if (all[0] != "") {
                    //AJAX Call
                    let feedbackObj = new Object();
                    feedbackObj.feedbackText = all[0];
                    feedbackObj.isActive = all[1];
                    feedbackObj.createdBy = signumGlobal;
                    feedbackObj.feedbackType = $('#typeOfInstantFeedback').val();
                    feedbackObj.instantFeedbackID = $(feedbackID).val();
                  pwIsf.addLayer({ text: "Please wait" });
                    $.isf.ajax({
                        type: "POST",
                        url: service_java_URL + "activityMaster/updateInstantFeedback",
                        crossdomain: true,
                        processData: true,
                        contentType: 'application/json',
                        data: JSON.stringify(feedbackObj),
                        success: function (data) {
                          
                            if (data.isValidationFailed == true) {
                                pwIsf.alert({ msg: data.formErrors[0], type: "info", autoClose:3});
                            }
                            else {
                                pwIsf.alert({ msg: "Successfully Updated", type: "info", autoClose: 3});
                                getInstantFeedbacks();
                                $('#feedbackText').val("");
                                $('#typeOfInstantFeedback').val("STEP");
                                //$("#typeOfInstantFeedback").select2({
                                //    placeholder: "Type"
                                //});
                                $('#radioActive').prop('checked', true);
                                $(btnaddInstantFeedback).prop('disabled', true);
                              //  $(btnCancelInstantFeedback).prop('disabled', true);
                                $('#btnaddInstantFeedback').val("Add");
                                
                              
                            }
                             

           // }
                           
                        },
                        error: function (status) {
                            pwIsf.alert({ msg: "Error while updating", type: "error" });
                        },
                        complete: function () {
                            pwIsf.removeLayer();
                            
                            $('#instantFeedbackTable tr').each(function (i) {
                                $(this).removeClass('colorChange');


                            });

                        }
                    })
            }
        
            
            }

        
 //   }
    });
    $('#btnCancelInstantFeedback').click(function () {
        if ($('#btnaddInstantFeedback').val("Update")) {
            $('#btnaddInstantFeedback').val("Add");
            $(".addFeedbackDiv").removeClass('EditCssInstantFeedback');

        }
        $('#instantFeedbackTable tr').each(function (i) {
            $(this).removeClass('colorChange');


        });
        $('#feedbackText').val("");
       $('#typeOfInstantFeedback').val("STEP");
        //$("#typeOfInstantFeedback").select2({
        //    placeholder: "Type"
        //});
        $('#radioActive').prop('checked', true);
        $(btnaddInstantFeedback).prop('disabled', true);
        //$(btnCancelInstantFeedback).prop('disabled', true);
       // $('#instantFeedbackRequired').text("");
        //$('#typeOfInstantFeedbackRequired').hide();
       
        
    });
});
//Function on click of edit icon to populate corresponding row data in the edit section above
function updateInstantFeedback(updateFeedbackObj) {
    $(".addFeedbackDiv").addClass('EditCssInstantFeedback');
    let feedbackText = $(updateFeedbackObj).closest('tr').find('td:nth-child(3)').text();
    let activeCheck = $(updateFeedbackObj).closest('tr').find('td:nth-child(4)').text();
    let feedbackID = $(updateFeedbackObj).closest('tr').find('td:nth-child(2)').text();
    let feedbackType = $(updateFeedbackObj).closest('tr').find('td:nth-child(5)').text();
    $('#instantFeedbackTable tr').each(function (i) {
        $(this).removeClass('colorChange');


    });
    //let activeHtml = $(updateFeedbackObj).closest('tr').find('td:nth-child(4)').html();
    //if ($(activeHtml).hasClass('editWrongSign')) {
    //    active = "false";

    //}
    //else if ($(activeHtml).hasClass('editRightSign')) {
    //    active = "true";

    //}
    let active="true";
    if (activeCheck == "Active") {
        active = "true";
    }
    else if (activeCheck=="Inactive") {
        active = "false";
    }





   // var tableRowColorChange = $(updateFeedbackObj).closest('tr');
    $(updateFeedbackObj).closest('tr').addClass('colorChange');
   // $(tableRowColorChange).addClass('colorChange');
    $('#feedbackText').val(feedbackText).trigger("change");
    $('#feedbackID').val(feedbackID).trigger("change");
    $('#typeOfInstantFeedback').val(feedbackType).trigger("change");
    


    $('#btnaddInstantFeedback').val("Update");

    if (active == "true") {
        $('#radioActive').prop('checked', true);
    }
    else if (active == "false") {
        $('#radioInactive').prop('checked', true);
    }
    $('#typeOfInstantFeedbackRequired').text("");
    $('#instantFeedbackRequired').text("");
    
    
}
//function for checking "Type" select box,"Feedback Text" is null or not and to initialise form data
function validationForTypeAndForm() {
    //var typeOK = true;
    //$('#typeOfInstantFeedbackRequired').text("");
    //if ($("#typeOfInstantFeedback").val() == "" || $("#typeOfInstantFeedback").val() == null) {
    //    $('#typeOfInstantFeedbackRequired').text("Type is required");
    //    typeOK = false;
    //}
    //var feedbackTextOK = true;
    //$('#instantFeedbackRequired').text("");
    //if ($("#feedbackText").val() == "" || $("#feedbackText").val() == null) {
    //    $('#instantFeedbackRequired').text("Feedback Text is required.");
    //    feedbackTextOK = false;
    //}
    let formData = $('form').serializeArray();
    var feedbackText = "", feedbackState = "";
    $.each(formData, function (i, field) {
        console.log(i + field);
        if (field.name == "feedbackText") {
            if (field.value == "") {
                //$('requiredText').show();
                //$('requiredText').val('Required Field').delay(5000).fadeOut();
                return false;
            }
            else { feedbackText = field.value; }
        }
        else {
            feedbackState = field.value;
        }
    });
    //var arr = [typeOK, feedbackText, feedbackState, feedbackTextOK];
    var arr = [feedbackText, feedbackState];
    return arr;

}
//function tableRowBackgroundColourChange(selectedRow) {
//    $(selectedRow).closest('tr').addClass('colorChange');

//}
//function tableRowBackgroundColourChange () {
//    $("#instantFeedbackTable tr").each(function () {
//        var col_val = $(this).find("td:eq(2)").text();
//        if (col_val == $(feedbackID).val()) {
//            $(this).addClass('colorChange');
//        }
//        //the selected class colors the row green//
//        //} else {
//        //    $(this).addClass('bad');
//        //}
//    });
//}
//function tableRowBackgroundColourChange() {
//    $('#instantFeedbackTable tr:first').addClass('colorChange');
//}
function validateForEnablingAddButton() {
    if ($("#feedbackText").val() == "" || $("#feedbackText").val() == null) {
        $(btnaddInstantFeedback).prop('disabled', true);
        
        
    }
    else {
        $(btnaddInstantFeedback).prop('disabled', false);
        
    }
}
//function validateForEnablingCancelButton() {
//    if (($("#feedbackText").val() == "" || $("#feedbackText").val() == null) && ($("#typeOfInstantFeedback").val() == "" || $("#typeOfInstantFeedback").val() == null)) {
//        $(btnCancelInstantFeedback).prop('disabled', true);
//    }
//    else {
//        $(btnCancelInstantFeedback).prop('disabled', false);

//    }
//}
  

