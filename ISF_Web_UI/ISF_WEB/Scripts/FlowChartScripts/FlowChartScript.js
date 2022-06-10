            
            var urlParams = new URLSearchParams(window.location.search);
            
            var woID = urlParams.get("woID");
            
            
            $(document).ready(function () {
                if (!(window.parent.location.href.indexOf("ProgAssignments") > -1))
                    getWODetailsForWF(woID);

                setInterval(function () {
                    var isUserLoggedin = signumGlobal;
                    if (isUserLoggedin == null) {
                        window.close();
                    }
                }, 2000);
            });
            $("#expertise").hide();
            joint.setTheme('material');
            app = new App.MainView({ el: '#app' });
            themePicker = new App.ThemePicker({ mainView: app });
            themePicker.render().$el.appendTo(document.body);

            var href = $("#btn_exec").attr("href");
            var href1 = href + "&woID=" + woID;
            $("#btn_exec").prop("href", href1);

            $("#btn-exec").css("border", "2px solid");
            $("#btn-edit").css("border", "2px solid");

            app.paper.on('cell:pointerclick', function (cellView, evt, x, y) {
                let stepName = '';
                currentShape = cellView.model.attributes.type;
                if (currentShape == 'ericsson.Decision') {
                    stepName = cellView.model.attr('label/text').split('Selected')[0].trim();
                    filterCommentsByStep(stepName); // Call for filter comment by step name
                }
                else if (currentShape == 'ericsson.Manual' || currentShape == 'ericsson.Automatic') {
                    stepName = cellView.model.attributes.name;
                    filterCommentsByStep(stepName); // Call for filter comment by step name
                }
            });

            app.layoutDirectedGraph();

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
            $(document).ready(function () {

                $('input#searchInComments').keyup(function () {
                    var that = this;
                    var textToBeSearched = $(that).val().toLowerCase();                 

                    
                    getCommentsSection(uniquePageID, textToBeSearched);
                    
                    
                });

                $('input[type=search]').on('search', function () {
                    var that = this;
                    var textToBeSearched = $(that).val().toLowerCase();
                    
                    getCommentsSection(uniquePageID, textToBeSearched);
                });

                $('#refreshAudit').on('click', function () {
                    that = this;
                    getStepsWO(subActivityFlowChartDefID, woID, experiencedMode = '21');
                    getCommentsSection(uniquePageID);
                    $('button#flagStateButton').removeClass('btn-danger').addClass('btn-info-outline');
                    $('button#mailStateButton').removeClass('btn-success').addClass('btn-info-outline');
                    flagButtonBoolean = 0;
                    mailButtonBoolean = 0;
                });
                $('button#showHideFlaggedData').on('click', function () {
                    that = this;
                    const flaggedCommentsArray = commentsArrayGlobal.filter(element => element.importance == 1);
                    if ($(that).hasClass('btn-info')) {
                        $(that).removeClass('btn-info').addClass('btn-danger');
                        getCommentsGeneric(uniquePageID, flaggedCommentsArray);
                    } else if ($(that).hasClass('btn-danger')) {
                        $(that).removeClass('btn-danger').addClass('btn-info');
                        getCommentsGeneric(uniquePageID, commentsArrayGlobal);
                    }
                });

                $('button#showHideAuditData').on('click', function () {
                    that = this;
                    const auditCommentsArray = commentsArrayGlobal.filter(element => element.type == "USER_COMMENT");
                    if ($(that).hasClass('btn-info')) {
                        $(that).removeClass('btn-info').addClass('btn-info-outline');
                        getCommentsGeneric(uniquePageID, auditCommentsArray);
                    } else if ($(that).hasClass('btn-info-outline')) {
                        getCommentsGeneric(uniquePageID, commentsArrayGlobal);
                        $(that).removeClass('btn-info-outline').addClass('btn-info');
                    }
                    if ($(that).find('i').hasClass('fa-eye')) {
                        $(that).find('i').removeClass('fa-eye').addClass('fa-eye-slash');
                    } else if ($(that).find('i').hasClass('fa-eye-slash')) {
                        $(that).find('i').removeClass('fa-eye-slash').addClass('fa-eye');
                    }
                });
                $('button#flagStateButton').on('click', function () {
                    that = this;
                    if ($(that).hasClass('btn-info-outline')) {
                        $(that).removeClass('btn-info-outline').addClass('btn-danger');
                        $('button#normalComment').removeClass('btn-success').addClass('btn-info-outline');
                        flagButtonBoolean = 1;
                    } else if ($(that).hasClass('btn-danger')) {
                        $(that).removeClass('btn-danger').addClass('btn-info-outline');
                        $('button#normalComment').removeClass('btn-info-outline').addClass('btn-success');
                        flagButtonBoolean = 0;
                    }
                });

                $('button#mailStateButton').on('click', function () {
                    that = this;
                    if ($(that).hasClass('btn-info-outline')) {
                        $(that).removeClass('btn-info-outline').addClass('btn-success');
                        mailButtonBoolean = 1;
                    } else if ($(that).hasClass('btn-success')) {
                        $(that).removeClass('btn-success').addClass('btn-info-outline');
                        mailButtonBoolean = 0;
                    }
                });
                $('button#normalComment').on('click', function () {
                    $('button#flagStateButton').removeClass('btn-danger').addClass('btn-info-outline');
                    $('button#normalComment').removeClass('btn-info-outline').addClass('btn-success');
                    flagButtonBoolean = 1;
                });
            });

//nahar 
            //$(function () {
            //    $.connection.hub.stop();
            //    if (startServerPush) {

            //        $.connection.hub.logging = true;
            //        var serverpushhubproxy = $.connection.serverPush;

            //        //Start of updateMyWorkAuditDetailUserComment
            //        serverpushhubproxy.client.updateMyWorkAuditDetailUserComment = function (data) {
            //            console.log(data);

            //            getCommentsSection(uniquePageID, textToBeSearched);
            //        };
                           
            //        //End of updateMyWorkAuditDetailUserComment
            //        $.connection.hub.start().done(function () {
            //            console.log('Now connected, connection ID=' + $.connection.hub.id);

            //            serverpushhubproxy.server.makeConnection(signumGlobal, $.connection.hub.id);
            //        })
            //            .fail(function () { console.log('Could not Connect!'); });
            //    }
            //    $(window).unload(function () {
            //        $.connection.hub.stop().done(function () {
            //            console.log('Now Disconnected , connection ID=' + $.connection.hub.id);
            //            serverpushhubproxy.server.removeConnection(signumGlobal, $.connection.hub.id);
            //        });
            //    });

                //        serverpushhubproxy.server.makeConnection(signumGlobal, $.connection.hub.id);
                //    })
                //        .fail(function () { console.log('Could not Connect!'); });
                //}

                ////2VN added for jquery 3.0
                //$(window).on("unload", function (e) {
                //    $.connection.hub.stop().done(function () {
                //        console.log('Now Disconnected , connection ID=' + $.connection.hub.id);
                //        serverpushhubproxy.server.removeConnection(signumGlobal, $.connection.hub.id);
                //    });
                //});

                //2VN commented
                //$(window).unload(function () {
                //    $.connection.hub.stop().done(function () {
                //        console.log('Now Disconnected , connection ID=' + $.connection.hub.id);
                //        serverpushhubproxy.server.removeConnection(signumGlobal, $.connection.hub.id);
                //    });
                //});

           // });

