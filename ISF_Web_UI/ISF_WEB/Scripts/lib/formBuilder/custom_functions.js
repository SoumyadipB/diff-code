// CUSTOM FUNCTIONS FOR FORM BUILDER

let frmBuilderCustom_isJSON = (data) => {  // CHECK PASSED VAR IS JSON OR NOT
    var isJson = false;

    try {
        //This works with JSON string and JSON object, not sure about others.
        //var json = $.parseJSON(data);
        isJson = (typeof (data) === 'object');
    }
    catch (ex) { console.error('data is not JSON'); }

    return isJson;
};


function configureFormBuilderFromOldJson(divId, OldJsonStr) {
    let formData = `${OldJsonStr}`;

    let options = {
        formData: formData,
        dataType: 'json'
    };

    let formBuilder = configureFormBuilder(divId, options);

    return formBuilder;
}


function configureFormBuilder(divId, opt = {}) { // IT WILL RETURN FORM BUILDER OBJECT
    let fbEditor = document.getElementById(divId);
    let options = {
        //controlPosition: 'left',
        disabledActionButtons: ['data', 'save', 'clear'],

        disabledAttrs: ['placeholder', 'className', 'access', 'subtype', 'description','name'],

        disableFields: ['autocomplete',
            'file',
            'paragraph', 'header', 'hidden', 'textarea', 'checkbox-group', 'radio-group', 'button'],
        editOnAdd: true,
        controlOrder: [
            'text',
            'number',
            'select',
            'date'
        ]

    };

    let mainOptions = $.extend({}, options, opt);

    let formBuilder = $(fbEditor).formBuilder(mainOptions);
    return formBuilder;

}

function getJsonFromFormBuilder(formBuilderObj,returnType='jsonStr') {
    //console.log(formBuilderObj.actions.getData('json'));
    let returnVar='';
    if (returnType === 'jsonStr') {
        returnVar = formBuilderObj.actions.getData('json');
    }
    if (returnType === 'json') {
        returnVar = JSON.parse(formBuilderObj.actions.getData('json'));
    }

    return returnVar;
}

function createFormFromFormBuilderJson(divId,formJsonStr) {

    let container = document.getElementById(divId);
    let formData = `${formJsonStr}`;
   
    let formRenderOpts = {
                
        formData,
        dataType: 'json'
    };

    $(container).formRender(formRenderOpts);

}


function common_getActualJsonFromFormBuilderJson(formBuilderJson=[]) {
        //returnType = auto , customString
    if (!frmBuilderCustom_isJSON(formBuilderJson)) {
        console.error('Pass json to common_getActualJsonFromFormBuilderJson.');
        return;
    }

    let frmBuilderJson = formBuilderJson;
    let returnJson = {};
    
    for (let i in frmBuilderJson) {

        if (frmBuilderJson[i]['type'] !== 'select') {

            let makeVal = (typeof frmBuilderJson[i]['value'] === 'undefined') ? "" : frmBuilderJson[i]['value'];
            returnJson[frmBuilderJson[i]['label']] = makeVal;

        } else {

            let selectedVals = [];

            for (let vals in frmBuilderJson[i]['values']) {
                if (frmBuilderJson[i]['values'][vals]['selected']) {
                    let selVal = (typeof frmBuilderJson[i]['values'][vals]['value'] === 'undefined') ? "" : frmBuilderJson[i]['values'][vals]['value'];
                    selectedVals.push(selVal);
                    
                    //break;
                }
            }
            returnJson[frmBuilderJson[i]['label']] = selectedVals;
            
        }

        //returnJson.put(returnJson);

    }

    return returnJson;


}


function frmBuilderCustome_getFormDataArray(passFrmId) {

    return $(`#${passFrmId}`).serializeArray();

}


function updateFormBuilderJsonWithExistingFormData(oldFormBuilderJsonObj = [], formId) {
    

    if (!frmBuilderCustom_isJSON(oldFormBuilderJsonObj)) {
        console.error('Pass json object to updateFormBuilderJsonWithExistingFormData.');
        return;
    }

    let oldFormBuilderJson = oldFormBuilderJsonObj;
    let formDataObj = frmBuilderCustome_getFormDataArray(formId);

    //console.log(formDataObj);

    

    for (let i in oldFormBuilderJson) {

        //Make all unselect 
        if (oldFormBuilderJson[i]['type'] === 'select') {
            for (let v in oldFormBuilderJson[i]['values']) {
                oldFormBuilderJson[i]['values'][v]['selected'] = false;
            }
        }


        for (let j in formDataObj) {

            if (formDataObj[j]['name'] === oldFormBuilderJson[i]['name'] || formDataObj[j]['name'] === oldFormBuilderJson[i]['name']+'[]') {

                if (oldFormBuilderJson[i]['type'] !== 'select') {
                    oldFormBuilderJson[i]['value'] = formDataObj[j]['value'];
                    break;
                } else {

                    for (let vals in oldFormBuilderJson[i]['values']) {
                        if (oldFormBuilderJson[i]['values'][vals]['value'] === formDataObj[j]['value']) {
                            oldFormBuilderJson[i]['values'][vals]['selected'] = true;
                            break;
                        } 
                    }
                   
                }


            }

            
        }

    }

    //console.log(oldFormBuilderJson);
    return oldFormBuilderJson; // updated json


}




