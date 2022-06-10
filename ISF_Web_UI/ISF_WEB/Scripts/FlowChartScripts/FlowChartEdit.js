
        var urlParams = new URLSearchParams(window.location.search);
        var woID = urlParams.get("woID");
        var subActID = urlParams.get("subActID");
        var projid = urlParams.get("prjID");
        let proficiencyId = urlParams.get("proficiencyId");
        let woStatus = urlParams.get("status");
        let proficiencyLevel = urlParams.get("proficiencyLevel");
        $(document).ready(function () {            
            getTools();
            getTasks(subActID);
            getRPAId(projid);
            getWorkInstruction(0, 0);
        });

        joint.setTheme('material');
        app = new App.MainView({ el: '#app' });

        themePicker = new App.ThemePicker({ mainView: app });
        themePicker.render().$el.appendTo(document.body);

        $($('.selection-wrapper')[0]).find('.remove').hide()

        $("#btn-add-step").css("border", "2px solid");

        (function () {
            var fs = (document.location.protocol === 'file:');
            var ff = (navigator.userAgent.toLowerCase().indexOf('firefox') !== -1);
            if (fs && !ff) {
                (new joint.ui.Dialog({
                    width: 300,
                    type: 'alert',
                    title: 'Local File',
                    content: $('#message-fs').show()
                })).open();
            }
        })();

        app.paper.on('cell:pointerclick', function (cellView, evt, x, y) {            
           
            var shape = currentShape = cellView.model.attributes.type;
            if (shape == "ericsson.Manual" || shape == "ericsson.Automatic" || shape == "ericsson.StartStep" || shape == "ericsson.EndStep" || shape == "ericsson.Decision") {

                $('.inspector-container').hide();
                $(".paper-container").css("width", "100%");
                $(".paper-container").css("left", "0");
                $(".paper-container").css("right", "0");
            }
            else {
                $('.inspector-container').show();
                if (evt.sourcePoint != undefined) {
                    var clickPoint = { x: evt._dx, y: evt._dy },

                        lengthTotal = evt.sourcePoint.manhattanDistance(evt.targetPoint),
                        length = evt.sourcePoint.manhattanDistance(clickPoint),
                        position = round(length / lengthTotal, 2);

                    evt.model.label(0, { position: position });
                }
            }


        });

        app.paper.on("cell:pointerup", function (cellView, evt, x, y) {
            if (Global_newAddedCells.includes(cellView.model.attributes.id)) {
                $('.handles').find('.remove').show();
                $('.handles').find('.unlink').show();
            } else {
                $('.handles').find('.remove').hide();
                $('.handles').find('.unlink').hide();
            }
        })

