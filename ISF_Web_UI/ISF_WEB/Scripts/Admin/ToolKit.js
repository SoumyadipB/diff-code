var tooltypeArray=[]
$(document).ready(function () {
    getToolType();
    getToolName();
 
         
        $("form#uploadTool").submit(function(event) {
            event.preventDefault();
            var tool_name = new Object();
            tool_name.tool = $("#ToolName").val();
            tool_name.licenseType = "test"
            tool_name.active = document.frmTool.active.value; 
            tool_name.createdBy = signumGlobal;
            tool_name.lastModifiedBy = signumGlobal;

            tool_name.introduction = $("#toolIntro").val();
            if ($('#selectTool option:selected').val()==-1){
                pwIsf.alert({ msg: "Please select tool type", type: 'warning' });
                return;
            }
            tool_name.toolType = $('#selectTool option:selected').text()
            tool_name.developer = $("#toolDeveloper").val();
            tool_name.infoURL = $('#toolURL').val();
            if ($("#ToolName").val() && !$("input[name='active']:checked").val()) {
                pwIsf.alert({ msg: "Please select tool status", type: 'warning' });
                return;

            }
           
                $.isf.ajax({
                    url: service_java_URL + 'toolInventory/saveToolInventory',
                    type: 'POST',
                    crossDomain: true,
                    context: this,
                    contentType: "application/json",
                    data: JSON.stringify(tool_name),

                    success: function(returndata) {
                        // alert("Tool Added");
                        pwIsf.alert({ msg: 'Tool Added.', type: 'info' });
                        location.reload();
                    
                    },
                    error: function (xhr, status, statusText) {
                        pwIsf.alert({ msg: 'Tool Addition Failed.Tool already exists.', type: 'warning' });
                    },

                });
            
                return false;
            });
        
});

function getToolType() {
    $('#selectTool').html('');
    $.isf.ajax({
        url: service_java_URL + 'toolInventory/getToolType',
        type: 'GET',        
        contentType: "application/json",        
        success: function(data) {
            tooltypeArray = data;
            $('#selectTool').append('<option value="-1" disabled selected>--Select One--</option>');
            $.each(data, function (i, d) {
                $('#selectTool').append('<option value="' + d.ToolTypeID + '">' + d.ToolType + '</option>');
                $('#editToolType').append('<option value="' + d.ToolTypeID + '">' + d.ToolType + '</option>');
            });          
                    
        },
        error: function (xhr, status, statusText) {
            pwIsf.alert({ msg: 'Get Tool Type Failed.', type: 'warning' });
        },

    });
}



function getToolName() {
    if ($.fn.dataTable.isDataTable('#Tool_Name_Search')) {
        oTable.destroy();
        $('#Tool_Name_Search').empty();
    }

    $.isf.ajax({
        url: service_java_URL + "toolInventory/getToolInventoryDetails?flag=true",
        crossDomain: true,
        success: function (data) {
            pwIsf.removeLayer();
            $('#div_table').show();
            $('#div_table_message').hide();
            var jsonData='';            
            $("#Tool_Name_Search").append($('<tfoot><tr><th></th><th></th><th>Tool Name</th><th>Tool Type</th><th>Developer</th><th>Status</th><th>Tool URL</th><th>Introduction</th><th>Last Modified</th></tr></tfoot>'));
            oTable = $('#Tool_Name_Search').DataTable({
                searching: true,
                responsive: true,
               
                "pageLength": 10,
                colReorder: true,
                dom: 'Bfrtip',
                order: [1],
                buttons: [
                    'colvis', 'excelHtml5'
                ],
                "data": data,
                "destroy": true,
                "columns": [
                    {
                        "title": "Action",
                        "targets": 'no-sort',
                        "orderable": false,
                        "searchable": false,
                        "data": null,
                        "render": function(data, type, row, meta){
                            if(data.active){
                                jsonData = JSON.stringify({
                                    toolID:data.toolID , tool: data.tool, active: data.active, intro: data.introduction, tooltype: data.toolType, developer: data.developer, infoURL: data.infoURL

                                });
                                return '<div style="display:flex"><a href="#edittoolname" class="icon-edit" title="Edit Tool Kit " data-details= \'' + jsonData + '\' data-toggle="modal"  onclick="updateToolMethod(this)">' + getIcon('edit') + '</a>';
                            }
                            else{
                                return '<div style="display:none"><a href="#edittoolname" class="icon-edit" title="Edit Tool Kit " data-details= \'' + jsonData + '\' data-toggle="modal"  onclick="updateToolMethod(this)">' + getIcon('edit') + '</a>';
                            }
                        }
                    },
                    {
                        "title": "Active/Inactive", "targets": 'no-sort', "orderable": false, "searchable": false, "data": null,
                        "render": function (data, type, row, meta) {
                         if (data.active) {
                             return '<label class="switchSource"><input type="checkbox" checked class="toggleActive" onclick="deleteToolKit(' + data.toolID + ')" id="togBtnSource_' + data.toolID + '" /><div class="sliderSource round"><span class="onSource">Enabled</span><span class="offSource">Disabled</span></div></label>';
                        }
                        else {
                             return '<label class="switchSource"><input type="checkbox" class="toggleActive" onclick="deleteToolKit(' + data.toolID + ')" id="togBtnSource_' + data.toolID + '"/><div class="sliderSource round"><span class="onSource">Enabled</span><span class="offSource">Disabled</span></div></label>';

                        }
                        }
                    },
                    {
                        "title": "Tool Name",
                        "data": "tool"
                    },
                    {
                         "title": "Tool Type",
                         "data": "toolType"
                    },
                    {
                          "title": "Developer",
                          "data": "developer"
                    },
                    {
                        "title": "Status", "targets": 'no-sort', "orderable": false, "data": null,
                        "render": function (data, type, row, meta) {
                            if (data.active) {
                                return "Enabled";
                            }
                            else {
                                return "Disabled";
                            }

                        }
                    },
                    {
                         "title": "Tool URL",
                         "data": "infoURL"
                    },
                    {
                          "title": "Introduction",
                          "data": "introduction"
                    },
                    {
                        "title": "Last Modified",
                        "data": "lastModifiedDate"
                    },

                ],
                initComplete: function () {

                    $('#Tool_Name_Search tfoot th').each(function (i) {
                        var title = $('#Tool_Name_Search thead th').eq($(this).index()).text();
                        if (title != "Action" && title !="Active/Inactive")
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


            $('#Tool_Name_Search tfoot').insertAfter($('#Tool_Name_Search thead'));

            $('#Tool_Name_Search tbody').on('click', 'a.icon-view', function () {
                var data = oTable.row($(this).parents('tr')).data();
                localStorage.setItem("views_project_id", data.projectID);

                window.location.href = "../Admin/ToolKit";

            });


        },
        error: function (xhr, status, statusText) {
            pwIsf.removeLayer();
            $('#div_table').hide();
            $('#div_table_message').show();
            console.log('An error occurred on' + xhr.error);
        }
    })


}


function deleteToolKit(toolID) {
    var checkbox = document.querySelector('input[id="togBtnSource_' + toolID + '"]');
    if (checkbox.checked) {
        pwIsf.confirm({
            title: 'Enable Tool',msg:'Do you wish to enable the tool?', type: 'success',
            'buttons': {
                'YES': {
                    'action': function () {
                        $.isf.ajax({
                            url: service_java_URL + "toolInventory/deleteToolInventory/" + toolID + "/" + signumGlobal + "/true",
                            context: this,
                            crossdomain: true,
                            processData: true,
                            contentType: 'application/json',
                            type: 'GET',

                            xhrFields: {
                                withCredentials: false
                            },
                            crossDomain: true,
                            success: function (data) {
                                pwIsf.alert({ msg: "Successfully Enabled.", autoClose: 3 });
                                //location.reload();
                                getToolName();
                            },
                            error: function (xhr, status, statusText) {
                                pwIsf.alert({ msg: 'Error in Enabling', type: 'warning' });
                            },
                            complete: function (xhr, statusText) {
                                // pwIsf.removeLayer();
                            }
                        });    
                    }
                },
                'NO': {
                    'action': function () {
                        getToolName();
                    }
                }

            }
        });
                  
    }  
    else {
        pwIsf.confirm({
            title: 'Disable Tool', msg: 'Do you wish to disable the tool?', type: 'success',
            'buttons': {
                'YES': {
                    'action': function () {
                        $.isf.ajax({
                            url: service_java_URL + "toolInventory/deleteToolInventory/" + toolID + "/" + signumGlobal + "/false",
                            context: this,
                            crossdomain: true,
                            processData: true,
                            contentType: 'application/json',
                            type: 'GET',

                            xhrFields: {
                                withCredentials: false
                            },
                            crossDomain: true,
                            success: function (data) {
                                pwIsf.alert({ msg: "Successfully Disabled.", autoClose: 3 });
                                getToolName()
                            },
                            error: function (xhr, status, statusText) {
                                pwIsf.alert({ msg: 'Error in Disabling', type: 'warning' });
                            },
                            complete: function (xhr, statusText) {
                            }
                        });                
                    }
                },
                'NO': {
                    'action': function () {
                        getToolName();
                    }
                }

            }
        });
      
    }
 }



function updateToolMethod(thisObj) {
    let jsonData = $(thisObj).data('details');
    let isActive = false;
    let status = $(thisObj).closest('tr').find('input[type="checkbox"]').val();
    if(status == "on"){isActive = true}
    var html = `
               <div class="col-lg-9">
                    Tool Name<a class=" text-danger">*</a>: <input class="form-control " id="ToolName2" name="tool" type="text" value="${jsonData.tool}"  />
                    <input type="hidden" id="license_Type" name="licenseType" value="test1" />
                    <input type="hidden" id="lastmodifiedby" name="lastmodifiedBy" value="${jsonData.signumGlobal}" />
                    <input type="hidden" id="toolID1" name="roolID" value="${jsonData.toolID}" />
                    <input type="hidden" id="activeTool" name="toolStatus" value="${isActive}" />
                    </div>
                      
                   <div class="col-lg-9">
                     Tool Type<a class="text-danger">*</a>: <select id="editToolType" class="select2able" placeholder="ToolType" style="width:100%" required></select>
                    </div>
                   
                     <div class="col-lg-9">
                     Introduction<a class="text-danger">*</a>: <input class="form-control" name="intro" type="text" id="editToolIntro" value="${jsonData.intro}" />
                    </div>

                      <div class="col-lg-9">
                      Developer<a class="text-danger">*</a>: <input class="form-control" name="developer" type="text" id="editDeveloper" value="${jsonData.developer}" />
                    </div>

                    <div class="col-lg-9">
                     Tool URL: <input class="form-control" name="toolURL" type="text" id="editURL" value="${jsonData.infoURL}" />
                    </div>

                        <div class="col-lg-4">
                            <button id="btnUpdateToolKitName" type="submit" onclick="updatetoolfinal()" pull-right class="btn btn-primary">Update Tool</button>
                        
                        </div>
                    `;
                        
              $('#updatetoolmodal').html('').append(html);
                            
              $.each(tooltypeArray, function (i, d) {                              
                    $('#editToolType').append('<option value="' + d.ToolTypeID + '">' + d.ToolType + '</option>');
              });          
                $('#editToolType option').filter(function() { 
                  return ($(this).text() == jsonData.tooltype); //To select
              }).prop('selected', true);
                
                $('#editToolType').select2();

}

function updatetoolfinal() {
        var tool_name1 = new Object();
        
        if($("#ToolName2").val() == "" || $("#editToolIntro").val() == "" || $("#editDeveloper").val() == ""){
            pwIsf.alert({ msg: 'Please fill all mandate fields', type: 'warning' });
            return;
        }
        tool_name1.tool = $("#ToolName2").val();
        tool_name1.licenseType = "test"
        tool_name1.active  = $("#activeTool").val();
        tool_name1.createdBy = signumGlobal;
        tool_name1.lastModifiedBy = signumGlobal;
        tool_name1.toolID = $("#toolID1").val();
        tool_name1.introduction = $("#editToolIntro").val();
       
        tool_name1.toolType = $('#editToolType option:selected').text()
        tool_name1.developer = $("#editDeveloper").val();
        tool_name1.infoURL = $('#editURL').val();
      
        $.isf.ajax({
            url: service_java_URL + 'toolInventory/updateToolInventory' ,
            type: 'POST',
            crossDomain: true,
            context: this,
            contentType: "application/json",
            data: JSON.stringify(tool_name1),
            
            success: function (returndata) {
                pwIsf.alert({ msg: 'Tool Updated', type: 'info' });
                location.reload();
                
            },
            error: function(xhr, status, statusText) {
                pwIsf.alert({ msg: 'Tool Update Failed.Please check all fields are filled and tool name is not same as previous. If still getting an error, kindly contact technical team.', type: 'error' });
            },

        });

    return false;
}