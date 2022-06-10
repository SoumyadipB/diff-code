function changePermission(obj) {

    document.getElementById('answer_' + obj.name).value = obj.value;

    access = obj.value;

    var array = obj.name.split("_");

    capabilityid = array[0];

    rolesid = array[1];

    rolCapd = array[2];

    if (rolCapd === 'undefined') {
        rolCapd = 0;
    }

    updateRoleCapability();

}

function roleBasedRadioButton(capId, rolId, rolCap, permissionValue) {

    radioButtonName = capId + '_' + rolId + '_' + rolCap + '_' + permissionValue;

    var x = "";

    var txtPermission = '<label><input type="text" value="' + permissionValue + '" id="answer_' + radioButtonName + '" readonly></label>';

    var txtPermission1 = '<label><input type="text" value="Non Accessible" id="answer_' + radioButtonName + '" readonly></label>';

 

    if (permissionValue == "Write" || permissionValue == "write" || permissionValue == "WRITE") {

        x += ' <label class="btn btn-danger btn-xs" ><input type="radio" name="' + radioButtonName + '" value="Write" checked = "checked" onchange="changePermission(this);" ></label><label class="btn btn-success btn-xs" > <input type="radio" name="' + radioButtonName + '" value="Read" onchange="changePermission(this);"></label> <label class="btn btn-default btn-xs"><input type="radio" name="' + radioButtonName + '" value="Non Accessible" onchange="changePermission(this);"></label>' + txtPermission;

    }

    else if (permissionValue == "read" || permissionValue == "Read" || permissionValue == "READ") {

        x += '<label class="btn btn-danger btn-xs"><input type="radio" name="' + radioButtonName + '" value="Write" onchange="changePermission(this);" ></label><label class="btn btn-success btn-xs"><input type="radio" name="' + radioButtonName + '" value="Read" checked = "checked" onchange="changePermission(this);"></label><label class="btn btn-default btn-xs"><input type="radio" name="' + radioButtonName + '" value="Non Accessible" onchange="changePermission(this);"></label>' + txtPermission;

    }

    else if (permissionValue == "None Accessible" || permissionValue == "non accessible" || permissionValue == "none") {

        x += '<label class="btn btn-danger btn-xs"><input type="radio" name="' + radioButtonName + '" value="Write" onchange="changePermission(this);" ></label><label class="btn btn-success btn-xs"><input type="radio" name="' + radioButtonName + '" value="Read" onchange="changePermission(this);"></label><label class="btn btn-default btn-xs"><input type="radio" name="' + radioButtonName + '" value="Non Accessible" checked = "checked" onchange="changePermission(this);"></label>' + txtPermission;

    }

    else {

        x += '<label class="btn btn-danger btn-xs"><input type="radio" name="' + radioButtonName + '" value="Write"  onchange="changePermission(this);" ></label><label class="btn btn-success btn-xs"><input type="radio" name="' + radioButtonName + '" value="Read"  onchange="changePermission(this);"></label><label class="btn btn-default btn-xs"><input type="radio" name="' + radioButtonName + '" value="Non Accessible" checked = "checked" onchange="changePermission(this);"></label>' + txtPermission1;

    }

    return x;

}

$(document).ready(function () {

    d = {};

    f = {};

 

 

    $.when($.ajax(service_java_URL + "accessManagement/getRoleList", { headers: commonHeadersforAllAjaxCall }), $.ajax(service_java_URL + "accessManagement/getCapabilities", { headers: commonHeadersforAllAjaxCall }), $.ajax(service_java_URL + "accessManagement/getAllRoleCapabilities", { headers : commonHeadersforAllAjaxCall })).done

        (

        function (roleListFromApi, capabilityListFromAPI, permissionRoleCapability) {

            console.log(permissionRoleCapability[0]);

            for (pmcnt in permissionRoleCapability[0]) {

                permission = permissionRoleCapability[0][pmcnt]['permission'];

                roleCapabilityId = roleId = permissionRoleCapability[0][pmcnt]['roleCapabilityId'];

                roleId = permissionRoleCapability[0][pmcnt]['roleId'];

                capabilityPageId = permissionRoleCapability[0][pmcnt]['capabilityPageId'];

 

 

                roleId_capabilityPageId = capabilityPageId + '_' + roleId;

                d[roleId_capabilityPageId] = permission;

                f[roleId_capabilityPageId] = roleCapabilityId;

 

 

            }

            console.log(d);

            console.log(f);

            var tr = '';

            var th = '<th>Capabilities/Roles</th>';

            for (rolCnt in roleListFromApi[0]) {

                var rolName = roleListFromApi[0][rolCnt]['role'];

                th += '<th style ="background-color:  #bfbfbf";>' + rolName + '</th>';

            }

            th = '<tr >' + th + '</tr>';

            var table = th;

            for (capCnt in capabilityListFromAPI[0]) {

                var groupTitle = capabilityListFromAPI[0][capCnt]['groupTitle'];
                var capName = capabilityListFromAPI[0][capCnt]['subMenuTitle'];

                var capId = capabilityListFromAPI[0][capCnt]['capabilityPageId'];
                if (capName!=null) {
                    tr = '<td style ="background-color:  #bfbfbf";><b>' + groupTitle + ' <i class="fa fa-arrow-circle-right" aria-hidden="true"></i></b> ' + capName + '</td>';
                }
                else {
                    tr = '<td style ="background-color:  #bfbfbf";><b>' + groupTitle + '</b></td>';
                }

                for (rolCnt in roleListFromApi[0]) {

                    var rolName = roleListFromApi[0][rolCnt]['role'];

                    var rolId = roleListFromApi[0][rolCnt]['accessRoleID'];

                    var name = capId + '_' + rolId;

                    var roleCapId = f[name];

                    var permissionValue = d[name];

                    tr += '<td>' + roleBasedRadioButton(capId, rolId, roleCapId, permissionValue) + '</td>';

                }

                table += '<tr>' + tr + '</tr>';

                tr = '';

            }

            $("#areaRoleCap").html('<table>' + table + '</table>');

        });

});

function updateRoleCapability() {

    //changePermission();

    var rol_cap_access = new Object();

    rol_cap_access.roleId = rolesid;

    rol_cap_access.capabilityPageId = capabilityid;

    rol_cap_access.permission = access;

    rol_cap_access.roleCapabilityId = rolCapd;

    console.log(rol_cap_access);

    //rol_cap_access.roleCapabilityId= 
    pwIsf.addLayer({ text: "Please wait ..." });
    $.isf.ajax({

        url: service_java_URL + 'accessManagement/addRoleCapability',

        type: 'POST',

        crossDomain: true,

        context: this,

        contentType: "application/json",

        data: JSON.stringify(rol_cap_access),

        success: function (returndata) {
            pwIsf.removeLayer();
            pwIsf.alert({ msg: 'Roles and Capabilities Updated.', type: 'success' });

            //location.reload();

        },

        error: function (xhr, status, statusText) {
            pwIsf.removeLayer();
            pwIsf.alert({ msg: 'Role and Capabilities Update Failed.', type: 'error' });

        },

    });

 

 

 

 

 

 

    return false;

 

 

}

 

 

