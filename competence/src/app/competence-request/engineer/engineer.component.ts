import { Component, OnInit, Inject, Output, EventEmitter } from '@angular/core';
import { MatDialogRef, MAT_DIALOG_DATA, MatDialog, MatTableDataSource } from '@angular/material';
import { CompetenceRequestService } from '../competence-request.service';
import { FormGroup, FormControl, FormGroupDirective } from '@angular/forms';
import { Observable } from 'rxjs';
//import { startWith, map } from 'rxjs/operators';
import { AuthService } from 'src/app/auth/auth.service';
// import { CompetenceAlertComponent } from 'src/app/shared/competence-alert/competence-alert.component'
import { Router } from '@angular/router';
import { Location } from '@angular/common';
import { AlertBoxService } from 'src/app/shared/alert-box.service';
import { EngineerRequestComponent } from '../engineer-request/engineer-request.component';



@Component({
    selector: 'app-engineer',
    templateUrl: './engineer.component.html',
    styleUrls: ['./engineer.component.css']
})
export class EngineerComponent implements OnInit {
    //selected:any = '2';
    competenceType: string = '';
    updateFormGroup: FormGroup;
    isBaselineSelected = false;
    public responseFlag: boolean;
    competencelist: string[] = [];
    technologyList: string[] = [];
    vendorList: string[] = [];
    domainList: string[] = [];
    serviceAreaList: string[] = [];
    subServiceAreaList: string[] = [];
    competenceTypeList: string[] = [];
    slideLabel: string = '';
    slideTitle: string = '';
    submitted = false;
    editStatus: string;
    ambitionText: any = {};
    technology = new FormControl();
    filteredOptions: Observable<string[]>;
    loggedInSignum = this.authService.getAuthData().loggedInSignum;
    onUpdate() { this.submitted = true; }

    @Output() updateTable = new EventEmitter<any>();
    constructor(private compData: CompetenceRequestService, public dialogRef: MatDialogRef<EngineerComponent>, @Inject(MAT_DIALOG_DATA) public enggCompetence: any, private authService: AuthService, private dialog: MatDialog, private router: Router, private location: Location, private alertBoxService: AlertBoxService) { }

    // openDialog(dialogResponse, dialogMessage): void {
    //     const dialogRef = this.dialog.open(CompetenceAlertComponent, {
    //         width: '40%',
    //         data: { response: dialogResponse, message: dialogMessage }
    //     });
    // }

    onNoClick() {
        this.dialogRef.close();
         this.router.navigateByUrl("/refresh-page", { skipLocationChange: true }).then(() => {
        //     console.log(decodeURI(this.location.path()));
            this.router.navigate([decodeURI(this.location.path())]);
         });
    }



    ngOnInit() {
        this.slideLabel = "Same";
        this.slideTitle = 'Ambition is same as Baseline';
        this.responseFlag = false;
        let getC;
        //let getT;
        //let getV;
        let getD;
        //this.systemID =this.enggCompetence.systemID; 
        let getCp;

        this.compData.getBaseline().subscribe(response => {
            getC = response;
            this.competencelist = getC;
            //console.log(getC);
        }
        );


        this.compData.getCompetence().subscribe(response => {
            getCp = response;
            this.competenceTypeList = getCp;
           // console.log(getCp);
        }
        );

        this.updateFormGroup = new FormGroup({
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

        this.getCompValues(this.enggCompetence.id);
    }



    getInnerText(innerText) {
        this.ambitionText.GradeName = innerText;
    }

    onValChange($event, baselinevalue) {
        this.responseFlag = $event.checked;
        (this.responseFlag) ? this.slideLabel = 'Next Level' : this.slideLabel = 'Baseline';
        (this.responseFlag) ? this.slideTitle = 'Ambition is gone to Next Level' : this.slideTitle = 'Ambition is same as Baseline';
        // baselinevalue.levelFlag = selectedToggleValue;
        this.getAmbitionByGradeWeight(baselinevalue, $event.checked);

    }
    getDomainByCompTypeId(compTypeId, domain) {
        let getD;
        if (compTypeId == null || compTypeId == undefined) {
            compTypeId = this.competenceType;
        }
        this.compData.getDomain(compTypeId).subscribe(response => {
            getD = response;
            this.domainList = getD;
           // console.log(getD);
            if (domain) {
                this.updateFormGroup.controls["domain"].setValue(domain);
            }
            if (this.domainList == undefined || this.domainList == null) {
                this.updateFormGroup.controls["technology"].setValue(" ");
            }
        }
        );
    }

    getTechnologyByDomainId(compTypeId, domainId, technology) {
        let getT;
        if (compTypeId == null || compTypeId == undefined) {
            compTypeId = this.competenceType;
        }
        this.compData.getTechnology(compTypeId, domainId).subscribe(response => {
            getT = response;
            this.technologyList = getT;
           // console.log(getT);
            if (technology) {
                this.updateFormGroup.controls["technology"].setValue(technology);
            }
            if (this.technologyList == undefined || this.technologyList == null) {
                this.updateFormGroup.controls["technology"].setValue(" ");
            }
        }
        );
    }

    getVendorByTechnologyId(compTypeId, domainId, techId, vendor) {
        let getV;
        if (compTypeId == null || compTypeId == undefined) {
            compTypeId = this.competenceType;
        }
        this.compData.getVendor(compTypeId, domainId, techId).subscribe(response => {
            getV = response;
            this.vendorList = getV;
           // console.log(getV);
            if (vendor) {
                this.updateFormGroup.controls["vendor"].setValue(vendor);
            }
            if (this.vendorList == undefined || this.vendorList == null) {
                this.updateFormGroup.controls["vendors"].setValue(" ");
            }
        }
        );
    }


    getAmbitionByGradeWeight(gradeW, selectedToggleValue) {
        this.isBaselineSelected = true;
        let getAmb;
        let levelFlag = '';

        (!selectedToggleValue) ? levelFlag = "same" : levelFlag = "nextLevel";

        this.compData.getAmbition(gradeW, levelFlag).subscribe(response => {
            getAmb = response;
            if (getAmb.responseData == null || getAmb.responseData == undefined) {
                this.ambitionText = '';
            }
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
    getCompValues(id) {
        let compValues;
        this.compData.getCompValuesFromApi(id).subscribe(
            (response) => {
               // console.log('GET Competence', response)
                //this.addValuesForm.emit(response);
                this.editCompChild(response);
            }
        )

    }

    editCompetence(res) {
        //console.log("In Add Competence Edit : ", res);
        this.updateFormGroup.controls["competenceType"].setValue(res.competenceTypeID);
        this.competenceType = res.competenceTypeID;
        this.updateFormGroup.controls["baseline"].setValue(res.baseline);
        this.updateFormGroup.controls["ambition"].setValue(res.competencyUpgrade);
        this.updateFormGroup.controls["ambitionValue"].setValue(res.competenceUpgradeID);
        this.updateFormGroup.controls["id"].setValue(res.id);
        this.editStatus = res.status;
        this.isBaselineSelected = true;
        if (res.competanceUpgradeLevel == "same") {
            this.responseFlag = false;
            this.slideLabel = 'Baseline';
            this.slideTitle = 'Ambition is same as Baseline'
        }
        else {
            this.responseFlag = true;
            this.slideLabel = 'Next Level';
            this.slideTitle = 'Ambition is gone to Next Level'
        }
    }
    getServiceAreaByCompId(compTypeId, domainId, techId, vendorId, serviceA) {
        let getSA;
        if (compTypeId == null || compTypeId == undefined) {
            compTypeId = this.competenceType;
        }
        this.compData.getServiceArea(compTypeId, domainId, techId, vendorId).subscribe(response => {
            getSA = response;
            this.serviceAreaList = getSA;
           // console.log(getSA);
            if (serviceA) {
                this.updateFormGroup.controls["serviceArea"].setValue(serviceA);
            }
            if (this.serviceAreaList == undefined || this.serviceAreaList == null) {
                this.updateFormGroup.controls["serviceArea"].setValue(" ");
            }
        }
        );
    }
    editCompChild(row) {
        this.editCompetence(row);

        this.getTechnologyByDomainId(row.competenceTypeID, row.domainID, row.technologyID);
        this.getVendorByTechnologyId(row.competenceTypeID, row.domainID, row.technologyID, row.vendorID);
        this.getServiceAreaByCompId(row.competenceTypeID, row.domainID, row.technologyID, row.vendorID, row.competanceID);
        this.getDomainByCompTypeId(row.competenceTypeID, row.domainID);

    }
    updateCompToTable(updateCompetenceForm: FormGroupDirective) {
        let values = updateCompetenceForm.value;
        let competenceData =
            //     [
            //         {"loggedInSignum":"eakinhm","requestedBySignum":"eakinhm","vendorID":1,"competanceID":1,"ambition":"A to B","competenceGradeID":1,"status":"sent to manager","changedBy":"engineer","createdBy":"eakinhm","technologyID":1,"domainID":1}

            // ];
            [
                {
                    loggedInSignum: this.loggedInSignum,
                    requestedBySignum: this.enggCompetence.requestedBySignum,
                    vendorID: values.vendor,
                    // competanceID : values.competenceType,
                    competanceID: values.serviceArea,
                    baseline: values.baseline,
                    ambition: values.ambitionValue,
                    competenceUpgradeID: values.ambitionValue, //ambitionValue.value//"A to B",
                    competenceGradeID: values.baseline,
                    status: this.editStatus,
                    id: values.id,
                    //status: "Initiated",
                    changedBy: "LM",
                    createdBy: this.loggedInSignum,
                    technologyID: values.technology,
                    domainID: values.domain,
                    isEditable: true
                }
            ]

        this.compData.addCompetence(competenceData).subscribe(
            (res: any) => {
                //console.log('Insert : ', res);
                if (res) {
                    // let updateTableObj = {
                    //     "status" : "Sent To Manager",
                    //     "reloadAllTables" : false
                    // }
                    this.updateTable.emit(this.editStatus);
                    this.updateFormGroup.controls["id"].setValue("");
                    //this.updateTable.emit("Initiated");
                    // this.resetForm();
                    //formDirective.resetForm();
                    updateCompetenceForm.resetForm();
                    this.isBaselineSelected = false;

                    if (res.formErrorCount != 0) {
                        this.alertBoxService.responseAlert("Error", res.formErrors[0]);
                        this.onNoClick();
                    }
                    else if (res.formMessageCount != 0) {
                        this.alertBoxService.responseAlert("Success", res.formMessages[0]);
                        this.onNoClick();
                    }
                }
            },
            (error) => console.log('POST Error->', error)
        );
    }

    updateCompetenceToTable(updateCompetenceForm: FormGroupDirective) {
        
        let obj = {
            "message": "Are you sure you want to update this competence?",
            "allBtns": [
                {
                    "name": "Yes",
                    "class": "primary",
                    "action": () => {
                        this.updateCompToTable(updateCompetenceForm);
                        //updateCompetenceForm.resetForm();
                        
                        //this.comp.updateTableAll();
                        
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

}