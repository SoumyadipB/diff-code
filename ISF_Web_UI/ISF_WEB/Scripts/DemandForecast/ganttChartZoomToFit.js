function zoom_tasks(node, ganttObj) {
    
    switch (node.value) {
        case "yearAndmonthAndDays":
            //ganttObj.config.scale_height = 60;
            ganttObj.config.scale_unit = "year";
            ganttObj.config.date_scale = "%Y";
            ganttObj.config.subscales = [
                { unit: "month", step: 1, date: "%M" },
                { unit: "day", step: 1, date: "%d" }
            ];
            break;
        case "yearAndmonthAndweek":
            //ganttObj.config.scale_height = 60;
            ganttObj.config.scale_unit = "year";
            ganttObj.config.date_scale = "%Y";
            ganttObj.config.subscales = [
                { unit: "month", step: 1, date: "%M" },
                { unit: "week", step: 1, date: "#%W" }
            ];
            break;
        case "monthAndweek":
            
            ganttObj.config.scale_unit = "month";
            ganttObj.config.date_scale = "%M";
            ganttObj.config.subscales = [
                { unit: "week", step: 1, date: "#%W" }
            ];
            break;
        case "monthAndDays":

            ganttObj.config.scale_unit = "month";
            ganttObj.config.date_scale = "%M";
            ganttObj.config.subscales = [
                { unit: "day", step: 1, date: "%d" }
            ];
            break;
        case "week":
            ganttObj.config.scale_unit = "day";
            ganttObj.config.date_scale = "%d %M";

            //gantt.config.scale_height = 60;
            //gantt.config.min_column_width = 30;
            ganttObj.config.subscales = [
                { unit: "hour", step: 1, date: "%H" }
            ];
            //show_scale_options("hour");
            break;
        case "trplweek":
            ganttObj.config.min_column_width = 70;
            ganttObj.config.scale_unit = "day";
            ganttObj.config.date_scale = "%d %M";
            ganttObj.config.subscales = [];
            //ganttObj.config.scale_height = 35;
            //show_scale_options("day");
            break;
        case "month":
            //gantt.config.min_column_width = 70;
            ganttObj.config.scale_unit = "week";
            ganttObj.config.date_scale = "Week #%W";
            ganttObj.config.subscales = [
                { unit: "day", step: 1, date: "%D" }
            ];
            //show_scale_options();
            //gantt.config.scale_height = 60;
            break;
        case "year":
            //ganttObj.config.min_column_width = 70;
            ganttObj.config.scale_unit = "month";
            ganttObj.config.date_scale = "%M";
            //ganttObj.config.scale_height = 60;
            //show_scale_options();
            ganttObj.config.subscales = [
                { unit: "week", step: 1, date: "#%W" }
            ];
            break;
    }
    //set_scale_units();

    let eveOnGanttRender = ganttObj.attachEvent("onGanttRender", function () {

        setTimeout(function () { pwIsf.removeLayer(); }, 500);
        
        console.log('onGanttRender')
    });

    ganttObj.render();

    ganttObj.detachEvent(eveOnGanttRender);
       
}





