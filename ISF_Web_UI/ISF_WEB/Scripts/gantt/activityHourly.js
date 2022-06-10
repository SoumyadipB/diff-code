
gantt.config.columns = [
    {
        name: "buttons",
        label: "Action",
        width: '50',
        align: "center",
        template: function (item) {
            var woid = item.text.split("_")[0].trim();
            //To show O/p url only on wo level
            if (item.itemType === "subactivity") {
                return '<a data-toggle="modal" data-target="#outputURLModal" onclick="viewOutputURLs(' + woid + ')">O/p Url</a>';
            }
            else {
                return '';
            }
        }
    },
            { name: "text", label: "Work Orders", tree: true, width: 200, template: woTemplate, resize: true },
            {
                name: "nodes", label: "NEID", align: "center",
                template: function (item) {
                    return '<span class="val"><p title="' + item.nodes + '">' + item.nodes + '</p></span>';
                }, resize: true
            },
            { name: "start_date", label: "Start Date", align: "center", width: 100, resize: true },
            { name: "duration", label: "Duration (Hrs)", align: "center", width: 100, resize: true },
            {
                name: "assignedBy", label: "Assigned By", align: "center", width: 100,
                template: function (item) {
                    return item.createdby;
                }, resize: true
            },
            {
                name: "assigned", label: "Assigned to", align: "center", width: 100,
                template: function (item) {
                    return item.resource;
                }, resize: true
            }
        ];



        gantt.config.work_time = true;
        gantt.setWorkTime({ hours: [8, 17] });//global working hours. 8:00-12:00, 13:00-17:00

        gantt.config.scale_unit = "day";
        gantt.config.date_scale = "%l, %F %d";
        gantt.config.min_column_width = 20;
        gantt.config.duration_unit = "hour";
        gantt.config.scale_height = 20 * 3;

        gantt.config.drag_move = false;
        gantt.config.drag_progress = false;
        gantt.config.drag_resize = false;
        gantt.config.details_on_dblclick = false;

        gantt.templates.task_cell_class = function (task, date) {
            var css = [];

            if (date.getHours() === 7) {
                css.push("day_start");
            }
            if (date.getHours() === 16) {
                css.push("day_end");
            }
            if (!gantt.isWorkTime(date, 'day')) {
                css.push("week_end");
            } else if (!gantt.isWorkTime(date, 'hour')) {
                css.push("no_work_hour");
            }

            return css.join(" ");
        };

function viewOutputURLs(woid) {

    $.isf.ajax({
        type: "GET",
        url: service_java_URL + "woManagement/getWOOutputFile?woid=" + woid,
        success: makeOutputUrlTable,
        error: function (msg) {
            //Empty block
        }
    });

    function makeOutputUrlTable(data) {
        $('#table_outputURLs_tbody').html('');
        $("#table_outPutURLs").append($('<tfoot><tr><th>WOID</th><th>OutputName</th><th>OutputUrl</th><th>CreatedBy</th></tr></tfoot>'));
        $('#table_outPutURLs').DataTable({
            "searching": true,
            "paging": true,
            "ordering": false,
            "colReorder": true,
            "data": data.responseData,
            "destroy": true,
            "columns": [
                {
                    "title": "WOID",
                    "data": "woid"
                },
                {
                    "title": "Output Name",
                    "data": "outputName"
                },
                {
                    "title": "Created By",
                    "data": "createdBy"
                },
                {
                    "title": "Output Url",
                    "data": null,
                    "render": function (data, type, row, meta) {
                        return '<a title=' + data.outputUrl + ' href=' + data.outputUrl + ' target="_blank">' + data.outputUrl + '</a>';
                    }
                } 
            ],
            initComplete: function () {
                $('#table_outPutURLs tfoot th').each(function (i) {
                    var title = $('#table_outPutURLs thead th').eq($(this).index()).text();
                    if (title !== "Actions" && title != "") {
                        $(this).html('<input type="text" class="form-control" style="font-size:12px;"  placeholder="Search ' + title + '" data-index="' + i + '" />');
                    }
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

        $('#table_outPutURLs tfoot').insertAfter($('#table_outPutURLs thead'));
    }
}


        gantt.templates.task_text = function (start, end, task) {

            if (task.itemType === "project") {
                return "<div class='gantt_task_content' style='width:100%;' onclick='showModel(" + task.id + ")'>Work Order Count: " + task.wocount + "</div>";
            }
            else if (task.itemType === "subactivity") {
                var n = task.id.lastIndexOf('_');
                var ID = task.id.substring(n + 1);
                 // getting proficiency level of work flow.
                var profLevel = getProficiencyLevel(task.status, task.proficiencyName);
                 // open the work flow based on the proficiency Assessed/Experienced 1,2.
                return "<div style='width:100%;' data-toggle='tooltip' title='" + task.text + ' / ' + task.nodes
                    + "' onclick='flowChartOpenInNewWindow(" + " \"" + UiRootDir + "\/DeliveryExecution\/FlowChart\?mode\=view&woID="
                    + ID + "\&proficiencyId=" + task.proficiencyId + "\&proficiencyLevel=" + profLevel + "\" " + ")'>"
                    + task.text + ' / ' + task.nodes + "</div>";
            }
            else {
                return task.text;
            }
        };

        gantt.templates.task_class = function (start, end, task) {
            if (task.itemType === "project") {
                return "project";
            }

            switch (task.status.toLowerCase()) {
                case "onhold":
                    return "onhold";
                case "assigned":
                    return "assigned";
                case "inprogress":
                    return "inprogress";
                case "open":
                    return "open";
                case "closed":
                    return "closed";
                case "deferred":
                    return "deferred";
                case "planned":
                    return "planned";
                case "rejected":
                    return "rejected";
            }


        };

        gantt.templates.grid_file = function (item) {
            return "<div class='gantt_tree_icon'></div>";
        };

        gantt.templates.grid_folder = function (item) {
            if (item.itemType === "activity") {
                return "<div class='gantt_tree_icon'></div>";
            }
            else {
                return "<div class='gantt_tree_icon gantt_folder_open'></div>";
            }
        };

        function woTemplate(task) {
            if (task.resType === "partial") {
                return "<div class='gantt_tree_content' style='margin-left: -20px;'>"
                    + "<span class='glyphicon glyphicon-random' aria-hidden='true' style='color: #888888;'></span>"
                    + task.text + " </div>";
            }
            else if (task.resType === "RPA") {
                return "<div class='gantt_tree_content' style='margin-left: -20px;'>"
                    + "<span class='glyphicon glyphicon-flash' aria-hidden='true' style='color: #888888;font-size: 16px;top: 2px;'></span>"
                    + task.text + "</div>";
            }
            else if (task.itemType === "activity") {
                return "<div class='gantt_tree_content' style='margin-left: -22px;'>"
                    + "<span class='glyphicon glyphicon-tasks' aria-hidden='true' style='color: #888888;'></span>&nbsp&nbsp"
                    + task.text + " </div>";
            }
            else {
                return "<div class='gantt_tree_content'>" + task.text + "</div>";
            }
        };

        var weekScaleTemplate = function (date) {
            var dateToStr = gantt.date.date_to_str("%d %M");
            var weekNum = gantt.date.date_to_str("(week %W)");
            var endDate = gantt.date.add(gantt.date.add(date, 1, "week"), -1, "day");
            return dateToStr(date) + " - " + dateToStr(endDate) + " " + weekNum(date);
        };

        gantt.config.subscales = [
            { unit: "week", step: 1, template: weekScaleTemplate },
            { unit: "hour", step: 1, date: "%G" }

        ];


        function showAll() {
            window.parent.$("#frame").prop('scrolling', 'yes');
            gantt.ignore_time = null;
            gantt.render();
            window.parent.$("#frame").prop('scrolling', 'no');
        }
        function hideWeekEnds() {
            window.parent.$("#frame").prop('scrolling', 'yes');
            gantt.ignore_time = function (date) {
                return !gantt.isWorkTime(date, "day");
            };
            gantt.render();
            window.parent.$("#frame").prop('scrolling', 'no');
        }
        function hideNotWorkingTime() {
            window.parent.$("#frame").prop('scrolling', 'yes');
            gantt.ignore_time = function (date) {
                return !gantt.isWorkTime(date);
            };
            gantt.render();
            window.parent.$("#frame").prop('scrolling', 'no');
        }
        gantt.config.sort = true;
        gantt.config.columns[0].sort = false;
        gantt.config.columns[3].sort = false;
        gantt.config.columns[4].sort = false;
        gantt.config.static_background = true;
        gantt.init("gantt_hereHourly");
        gantt.config.branch_loading = true;
        getHourlyActivityData(gantt);

