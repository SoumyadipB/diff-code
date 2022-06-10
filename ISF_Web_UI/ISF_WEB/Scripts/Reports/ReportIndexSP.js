$(document).ready(function () {
    
    getTableauReportSP();
   
    $('#system-search4').keyup(function () {
        var that = this;
        // affect all table rows on in systems table
        var tableBody = $('.table-list-search3 tbody');
        var tableRowsClass = $('.table-list-search3 tbody tr');
      

        tableRowsClass.each(function (i, val) {
           
            //Lower text for case insensitive
            var rowText = $(val).text().toLowerCase();
            var inputText = $(that).val().toLowerCase();
            if (inputText != '') {

            }
            else {
               
                console.log('ok ');
            }

            if (rowText.indexOf(inputText) == -1) {
                //hide rows
                tableRowsClass.eq(i).hide();


            }
            else {
              
                tableRowsClass.eq(i).show();

            }
        });
        //all tr elements are hidden
        if (tableRowsClass.children(':visible').length == 0) {


        }

       




    });

    



});






function getTableauReportSP() {
    $("#TableaureportbodySP").html('');


    $.isf.ajax({
        url: service_java_URL + "/reportManagement/getAllTableauReport",

        success: function (data) {

            htmlDoc = '';
            htmlBody = '';
            html = '';



            for (i = 0; i < data.length; i++) {
                TableauReportID = data[i]['TableauReportID'];
                TableauReportName = data[i]['TableauReportName'];
                Script_Tableau = data[i]['Script_Tableau'];
                TableauDescription = data[i]['TableauDescription'];
                TableauReportImage = data[i]['TableauReportImage'];
                if (TableauReportName == "Individual Delivery Performance Report – SP View") {
                    Script_Tableau += "<param name='filter' value='SPSIGNUM=" + signumGlobal + " ' /></object></div>"

                }
                
                else {
                    Script_Tableau = data[i]['Script_Tableau'];
                }
                if (TableauReportName == "Individual Delivery Performance Report – SP View") {
                    htmlBody = htmlBody + '<tr >';
                    htmlBody = htmlBody + '<td><a href="#myModal" class="view-report" data-toggle="modal" data-src="' + TableauReportImage + '" data-target="#myModal" "><span class="fa fa-info"></span>&emsp;&nbsp;&nbsp;<a href="#Tableau_Report" class="view-TableauReport" title = "View ' + TableauReportName + ' Report" onclick="TableaureportPage(this)" data-tabScript="' + Script_Tableau + '" data-tabName="' + TableauReportName + '" ><i class="fa fa-play"></i></a >&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>' + TableauReportName + '</b></td>';
                    htmlBody += '<td>' + TableauDescription + '</td>';

                    htmlBody = htmlBody + '</tr>';
                }
            }
            $("#TableaureportbodySP").append(htmlBody);
            
        }
    });
}



function TableaureportPage(el) {

    let tabScript = $(el).attr('data-tabScript');
    let tabName = $(el).attr('data-tabName');

    localStorage.removeItem('Tableau_Report_Script');
    localStorage.removeItem('Tableau_Report_Name');
    localStorage.setItem('Tableau_Report_Script', tabScript);
    localStorage.setItem('Tableau_Report_Name', tabName);

    window.location.href = UiRootDir + 'Tableau_Report';

}