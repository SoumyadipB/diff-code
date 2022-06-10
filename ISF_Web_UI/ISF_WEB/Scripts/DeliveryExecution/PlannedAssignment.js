
    $(document).ready(function () {
        var oTable;
        $('#start_date').datepicker("setDate", new Date());
        $('#end_date').datepicker("setDate", new Date());
        //START- fetch NODE DETAILS
        $(function () {
            $("[id$=start_date]").datepicker({
                startDate: $("[id$=end_date]").val(),
                autoClose: true,
                viewStart: 0,
                weekStart: 1
            });
        });
        $(function () {
            $("[id$=end_date]").datepicker({
                startDate: $("[id$=start_date]").val(),
                autoClose: true,
                viewStart: 0,
                weekStart: 1
            });
        });
        $(function () {
            $("[id$=start_date]").datepicker({ minDate: 0 });
            $("[id$=start_date_mt]").datepicker({ minDate: 0 });
        });

        $('#edit_node_details').on('click', function () {

            var projectID = $('#workOrderViewIdPrefix_project_id').val();
            var type = $('#workOrderViewIdPrefix_sitetype-node-type').select2("val");
            if (projectID == undefined || projectID == null || projectID == '' || isNaN(parseInt(projectID))) {
                pwIsf.alert({ msg: "Project ID missing or invalid", type: "error" });
            }
            else {
                $.isf.ajax({
                    url: service_java_URL + "woManagement/getNodeType/" + parseInt(projectID),
                    async: false,
                    success: function (data) {
                        $('#workOrderViewIdPrefix_sitetype-node-type').empty();
                        $.each(data, function (i, d) {
                            if (type != d)
                                $('#workOrderViewIdPrefix_sitetype-node-type').append('<option value="' + d + '">' + d + '</option>');
                        })                       
                    },
                    error: function (xhr, status, statusText) {
                        console.log('An error occurred on getting Node type: ' + xhr.error);
                    }
                });

                getNodeNamesForEditWO();
            }
        });


        //END- fetch NODE DETAILS

        var signum = signumGlobal;
        var startDate = $('#start_date').val();
        var endDate = $('#end_date').val();
        var table;
        if (startDate == '') startDate = 'all'
        if (endDate == '') endDate = 'all'
        //Search work order plan details
        getAllPlannedWorkOrderDetails(signum, 'all', 'all');

    });

    function getNodeNamesForEditWO() {
        var projectID = $('#workOrderViewIdPrefix_project_id').val();
        var type = $('#workOrderViewIdPrefix_sitetype-node-type').select2('data')[0].text;
        var eleType = $('#workOrderViewIdPrefix_sitetype-node-type').select2("val")
        $.isf.ajax({

            url: service_java_URL + "woManagement/getNodeNames/" + projectID + "/" + type + "/" +eleType,
            success: function (data) {

                $('#workOrderViewIdPrefix_node_master_select').html('');
                $.each(data, function (j, d) {
                    for (var i = 0; i < d.lstNodeName.length; i++) {
                        $('#workOrderViewIdPrefix_node_master_select').append($('<option>', {
                            value: d.lstNodeName[i],
                            text: d.lstNodeName[i]
                        }));
                    }
                })

                var savedNodes = [];

                $("#workOrderViewIdPrefix_node-site-name option").each(function () {
                    savedNodes.push($(this).val());
                });


                $('#workOrderViewIdPrefix_node_master_select').select2();
                $("#workOrderViewIdPrefix_node_master_select").select2("val", savedNodes); // craete tags


                $('#all_nodes_master').show();

                //Show Save button on view order form
                $('#btn_update_nodes').attr("disabled", false);

            },
            error: function (xhr, status, statusText) {
                alert("Failed to Edit Node");
            }
        });
    }
