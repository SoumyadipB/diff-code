import { Component, Input, Output, EventEmitter } from '@angular/core';
import { MyCompetenceService } from '../my-cometence.service';
import { NewCompetence } from '../newcompetence';
import { NgForm, FormGroupDirective } from '@angular/forms';
import { resetFakeAsyncZone } from '@angular/core/testing';
import { FormControl, FormGroup } from '@angular/forms';
import { MatDialog, MatDialogRef, MAT_DIALOG_DATA } from '@angular/material/dialog';
import { AuthService } from 'src/app/auth/auth.service';
// import { CompetenceAlertComponent } from 'src/app/shared/competence-alert/competence-alert.component'
import { AlertBoxService } from 'src/app/shared/alert-box.service';

export interface Competence {
    competenceType: string;
    domain: string;
    technology: string;
    vendor: string;
    serviceArea: string;
    baseline: string;
    ambition: string;
}
//import {FormBuilder} from '@angular/forms';

@Component({
    selector: 'app-engg-add-competence',
    templateUrl: './add-competence.component.html',
    styleUrls: ['./add-competence.component.css']

})
export class AddCompetenceComponent {

    competencelist: string[] = [];
    technologyList: string[] = [];
    vendorList: string[] = [];
    domainList: string[] = [];
    serviceAreaList: string[] = [];
    subServiceAreaList: string[] = [];
    competenceTypeList: string[] = [];

    ambitionText: any = {};
    slideLabel: string = '';
    slideTitle: string = '';
    public responseFlag: boolean;
    submitted = false;
    isBaselineSelected = false;
    submitButtonText = "Add Competence";
    editStatus: string;
    onSubmit() { this.submitted = true; }

    displayedColumns: string[];
    data: Competence[] = [];
    dataSource: Competence[] = [];
    competenceFormGroup: FormGroup;
    
    competenceTypeValue : string;
    domainValue : string;
    technologyValue : string;
    vendorValue : string;

    loggedInSignum = this.authService.getAuthData().loggedInSignum;

    // @Input()
    // dataSourceSend : Competence[] = [];

    @Output() updateTable = new EventEmitter<any>();

    // @Output() changeCompServiceArea = new EventEmitter<any>();

    // sendCompetence(compData : string){
    //     this.addCompetence.emit(compData)
    // }



    constructor(private compData: MyCompetenceService, private authService: AuthService, public dialog: MatDialog, private alertBoxService: AlertBoxService) { }

    ngOnInit() {
        this.responseFlag = false;
        this.slideLabel = "Baseline";
        this.slideTitle = 'Ambition is same as Baseline';

        let getC;
        let getCp;

        this.compData.getBaseline().subscribe(response => {
            getC = response;
            this.competencelist = getC;
            // console.log(getC);
        }
        );

        this.compData.getCompetence().subscribe(response => {
            getCp = response;
            this.competenceTypeList = getCp;
            // console.log(getCp);
        }
        );

        this.competenceFormGroup = new FormGroup({
            competenceType: new FormControl(),
            domain: new FormControl(),
            technology: new FormControl(),
            vendor: new FormControl(),
            serviceArea: new FormControl(),
            baseline: new FormControl(),
            ambition: new FormControl(),
            ambitionValue: new FormControl(),
            ambitionLevel: new FormControl(),
            id: new FormControl(),
            submitButton: new FormControl()
        })
    }

    // openDialog(dialogResponse, dialogMessage): void {
    //     const dialogRef = this.dialog.open(CompetenceAlertComponent, {
    //         width: '40%',
    //         data: { response: dialogResponse, message: dialogMessage }
    //     });
    // }

    // dialogRef.afterClosed().subscribe(result => {
    //   console.log('The dialog was closed');
    //   this.animal = result;
    // });


    onValChange($event, baselinevalue) {
        this.responseFlag = $event.checked;
        (this.responseFlag) ? this.slideLabel = 'Next Level' : this.slideLabel = 'Baseline';
        (this.responseFlag) ? this.slideTitle = 'Ambition is gone to Next Level' : this.slideTitle = 'Ambition is same as Baseline';
        // baselinevalue.levelFlag = selectedToggleValue;
        this.getAmbitionByGradeWeight(baselinevalue, $event.checked);

    }
    getDomainByCompTypeId(compTypeId, domain) {
        let getD;
        /* istanbul ignore if */
        if(compTypeId == null || compTypeId == undefined){
            compTypeId = this.competenceTypeValue;
        }
        /* istanbul ignore else */
        else{ compTypeId = compTypeId;}
        this.compData.getDomain(compTypeId).subscribe(response => {
            getD = response;
            this.domainList = getD;
            // console.log(getD);
            if (domain) {
                this.competenceFormGroup.controls["domain"].setValue(domain);
            }
        }
        );
    }

    getTechnologyByDomainId(compTypeId, domainId, technology) {
        let getT;
        /* istanbul ignore if */
        if(compTypeId == null || compTypeId == undefined){
            compTypeId = this.competenceTypeValue;
        }
        /* istanbul ignore if */
        if(domainId == null || domainId == undefined){
            domainId = this.domainValue;
        }
        this.compData.getTechnology(compTypeId, domainId).subscribe(response => {
            getT = response;
            this.technologyList = getT;
            // console.log(getT);
            /* istanbul ignore if */
            if (technology) {
                this.competenceFormGroup.controls["technology"].setValue(technology);
            }
        }
        );
    }

    getVendorByTechnologyId(compTypeId, domainId, techId, vendor) {
        let getV;
/* istanbul ignore if */
        if(compTypeId == null || compTypeId == undefined){
            compTypeId = this.competenceTypeValue;
        }
/* istanbul ignore if */
        if(domainId == null || domainId == undefined){
            domainId = this.domainValue;
        }
/* istanbul ignore if */
        if(techId == null || techId == undefined){
            techId = this.technologyValue;
        }

        this.compData.getVendor(compTypeId, domainId, techId).subscribe(response => {
            getV = response;
            this.vendorList = getV;
            // console.log(getV);
            /* istanbul ignore if */
            if (vendor) {
                this.competenceFormGroup.controls["vendor"].setValue(vendor);
            }
        }
        );
    }

    getServiceAreaByCompId(compTypeId, domainId, techId, vendorId, serviceA) {
        let getSA;
/* istanbul ignore if */
        if(compTypeId == null || compTypeId == undefined){
            compTypeId = this.competenceTypeValue;
        }
/* istanbul ignore if */
        if(domainId == null || domainId == undefined){
            domainId = this.domainValue;
        }
/* istanbul ignore if */
        if(techId == null || techId == undefined){
            techId = this.technologyValue;
        }
/* istanbul ignore if */
        if(vendorId == null || vendorId == undefined){
            vendorId = this.vendorValue;
        }

        this.compData.getServiceArea(compTypeId, domainId, techId, vendorId).subscribe(response => {
            getSA = response;
            this.serviceAreaList = getSA;
            // console.log(getSA);
            /* istanbul ignore if */
            if (serviceA) {
                this.competenceFormGroup.controls["serviceArea"].setValue(serviceA);
            }
        }
        );
    }

    getInnerText(innerText) {
        this.ambitionText.GradeName = innerText;
    }

    getAmbitionByGradeWeight(gradeW, selectedToggleValue) {
        this.isBaselineSelected = true;
        let getAmb;
        let levelFlag = '';

        (!selectedToggleValue) ? levelFlag = "same" : levelFlag = "nextLevel";

        this.compData.getAmbition(gradeW, levelFlag).subscribe(response => {
            getAmb = response;
            /* istanbul ignore if */
            if (getAmb.responseData == null || getAmb.responseData == undefined) {
                this.ambitionText = '';
            }
            /* istanbul ignore else */
            else {
                this.ambitionText = getAmb.responseData[0];
                (getAmb.responseData[0].Description == "same") ? this.responseFlag = false : this.responseFlag = true;
                (this.responseFlag) ? this.slideLabel = 'Next Level' : this.slideLabel = 'Baseline';
                (this.responseFlag) ? this.slideTitle = 'Ambition is gone to Next Level' : this.slideTitle = 'Ambition is same as Baseline';
            }
            // console.log(getAmb);
        }
        );

    }

    editCompetence(row) {
        // console.log("In Add Competence Edit : ", row);
        this.competenceFormGroup.controls["competenceType"].setValue(row.competenceTypeID);
        this.competenceTypeValue = row.competenceTypeID;
        this.domainValue = row.domainID;
        this.technologyValue = row.technologyID;
        this.vendorValue = row.vendorID;
        // this.competenceFormGroup.controls["domain"].setValue(row.domainID);
        // this.competenceFormGroup.controls["technology"].setValue(row.technologyID);
        // this.competenceFormGroup.controls["vendor"].setValue(row.vendorID);
        // this.competenceFormGroup.controls["serviceArea"].setValue(row.competanceID);
        this.competenceFormGroup.controls["baseline"].setValue(row.baseline);
        this.competenceFormGroup.controls["ambition"].setValue(row.competencyUpgrade);
        this.competenceFormGroup.controls["ambitionValue"].setValue(row.competenceUpgradeID);
        this.competenceFormGroup.controls["id"].setValue(row.id);
        this.submitButtonText = "Update Competence";
        this.editStatus = row.status;
        this.isBaselineSelected = true;
        /* istanbul ignore if */
        if (row.competanceUpgradeLevel == "same") {
            this.responseFlag = false;
            this.slideLabel = 'Baseline';
            this.slideTitle = 'Ambition is same as Baseline'
        }
        /* istanbul ignore else */
        else {
            this.responseFlag = true;
            this.slideLabel = 'Next Level';
            this.slideTitle = 'Ambition is gone to Next Level'
        }
    }

    editCompChild(row) {
        this.editCompetence(row);
        this.getDomainByCompTypeId(row.competenceTypeID, row.domainID)
        this.getTechnologyByDomainId(row.competenceTypeID, row.domainID, row.technologyID);
        this.getVendorByTechnologyId(row.competenceTypeID, row.domainID, row.technologyID, row.vendorID);
        this.getServiceAreaByCompId(row.competenceTypeID, row.domainID, row.technologyID, row.vendorID, row.competanceID);
        // this.competenceFormGroup.controls["submitButton"].setValue("Update Competence");
        // this.competenceFormGroup["ambitionLevel"].setValue(row.);
    }

    // resetForm(){
    //     this.competenceFormGroup.reset();
    // }

    cancelCompetence(addCompetenceForm: FormGroupDirective) {
        addCompetenceForm.value.id = "";
        this.submitButtonText = "Add Competence";
        addCompetenceForm.resetForm();
        this.clearDependentDropdowns();
    }


    addCompetenceToTable(addCompetenceForm: FormGroupDirective) {

        this.submitted = true;
        /* istanbul ignore if */
        if (addCompetenceForm.value.id == "" || addCompetenceForm.value.id == null) {
            let obj = {
                "message": "Are you sure you want to add this competence?",
                "allBtns": [
                    {
                        "name": "Yes",
                        "class": "primary",
                        "action": () => {
                            
                            this.addCompToTable(addCompetenceForm);
                        }
                    },
                    {
                        "name": "No",
                        "class": "warn",
                        "action": () => { }
                    }
                ]
            }

            this.alertBoxService.alertBox(obj);
        }
//         /* istanbul ignore else*/
        else {
            let obj = {
                "message": "Are you sure you want to update this competence?",
                "allBtns": [
                    {
                        "name": "Yes",
                        "class": "primary",
                        "action": () => {
                             /* istanbul ignore next */
                            this.updateCompetenceToTable(addCompetenceForm);
                        }
                    },
                    {
                        "name": "No",
                        "class": "warn",
                        "action": () => { }
                    }
                ]
            }
 /* istanbul ignore next */
            this.alertBoxService.alertBox(obj);
        }

    }

    clearDependentDropdowns() {
        this.domainList = [];
        this.technologyList = [];
        this.vendorList = [];
        this.serviceAreaList = [];
    }

    addCompToTable(addCompetenceForm: FormGroupDirective) {
        let values = addCompetenceForm.value;
        let competenceData =
            //     [
            //         {"loggedInSignum":"ebmhuak","requestedBySignum":"ebmhuak","vendorID":1,"competanceID":1,"ambition":"A to B","competenceGradeID":1,"status":"sent to manager","changedBy":"engineer","createdBy":"ebmhuak","technologyID":1,"domainID":1}

            // ];
            [
                {
                    loggedInSignum: this.loggedInSignum,
                    requestedBySignum: this.loggedInSignum,
                    vendorID: values.vendor,
                    // competanceID : values.competenceType,
                    competanceID: values.serviceArea,
                    // baseline : values.baseline,
                    // ambition : values.ambitionValue,
                    competenceUpgradeID: values.ambitionValue, //ambitionValue.value//"A to B",
                    competenceGradeID: values.baseline,
                    status: "Initiated",
                    changedBy: "engineer",
                    createdBy: this.loggedInSignum,
                    technologyID: values.technology,
                    domainID: values.domain,
                    isEditable: true
                }
            ]

        this.compData.addCompetence(competenceData).subscribe(
            (res: any) => {
                // console.log('Insert : ', res);
                if (res) {
                    let updateTableObj = {
                        "status": "Initiated",
                        "reloadAllTables": false
                    }
                    this.updateTable.emit(updateTableObj);
                    // this.resetForm();
                    addCompetenceForm.resetForm();
                    this.clearDependentDropdowns();
                    this.isBaselineSelected = false;
                    if (res.formErrorCount != 0) {
                        this.alertBoxService.responseAlert("Error", res.formErrors[0]);
                    }
                    else if (res.formMessageCount != 0) {
                        this.alertBoxService.responseAlert("Success", res.formMessages[0]);
                    }
                }

            },
            /*istanbul ignore next */
            (error) => console.log('POST Error->', error)
        );
    }

    updateCompetenceToTable(addCompetenceForm: FormGroupDirective) {
        let values = addCompetenceForm.value;
        let statusValue = this.editStatus == "Rejected" ? "Initiated" : this.editStatus;
        let competenceData =
            //     [
            //         {"loggedInSignum":"ebmhuak","requestedBySignum":"ebmhuak","vendorID":1,"competanceID":1,"ambition":"A to B","competenceGradeID":1,"status":"sent to manager","changedBy":"engineer","createdBy":"ebmhuak","technologyID":1,"domainID":1}

            // ];
            [
                {
                    loggedInSignum: this.loggedInSignum,
                    requestedBySignum: this.loggedInSignum,
                    vendorID: values.vendor,
                    // competanceID : values.competenceType,
                    competanceID: values.serviceArea,
                    // baseline : values.baseline,
                    // ambition : values.ambitionValue,
                    competenceUpgradeID: values.ambitionValue, //ambitionValue.value//"A to B",
                    competenceGradeID: values.baseline,
                    status: statusValue,
                    changedBy: "engineer",
                    createdBy: this.loggedInSignum,
                    technologyID: values.technology,
                    domainID: values.domain,
                    id: values.id,
                    parentSystemID: values.id,
                    isEditable: true
                }
            ]

        this.compData.addCompetence(competenceData).subscribe(
            (res: any) => {
                // console.log('Insert : ', res);
                if (res) {
                    let updateTableObj = {
                        "status": statusValue,
                        "reloadAllTables": this.editStatus == "Rejected" ? true : false
                    }
                    this.updateTable.emit(updateTableObj);
                    this.competenceFormGroup.controls["id"].setValue("");
                    this.submitButtonText = "Add Competence";
                    // this.resetForm();
                    addCompetenceForm.resetForm();
                    this.clearDependentDropdowns();
                    this.isBaselineSelected = false;
                    if (res.formErrorCount != 0) {
                        this.alertBoxService.responseAlert("Error", res.formErrors[0]);
                    }
                    else if (res.formMessageCount != 0) {
                        this.alertBoxService.responseAlert("Success", res.formMessages[0]);
                    }
                }
            },
            /* istanbul ignore next */
            (error) => console.log('POST Error->', error)
        );


    }


}