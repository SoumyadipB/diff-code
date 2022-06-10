
        var urlParams = new URLSearchParams(window.location.search);
        var woID = urlParams.get("woID");
        $(document).ready(function () {
            getWODetailsForWF(woID);

            //Close of session timeout or logout
            setInterval(function () {
                var isUserLoggedin = signumGlobal;
                if (isUserLoggedin == null) {
                    window.close();
                }
            }, 2000);
        });

        joint.setTheme('material');
        app = new App.MainView({ el: '#app' });
        themePicker = new App.ThemePicker({ mainView: app });
        themePicker.render().$el.appendTo(document.body);
        window.addEventListener('load', function () {
        
        });
        resizeCell(app.paper.model);
        $("#btn-exec").hide();
        $("#btn-edit").hide();
		
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
		
      

