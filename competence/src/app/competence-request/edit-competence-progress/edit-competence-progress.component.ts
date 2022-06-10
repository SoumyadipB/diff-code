import { Component, OnInit, Inject } from '@angular/core';
import { MatDialogRef, MAT_DIALOG_DATA } from '@angular/material';
import { FormGroup, FormControl, FormGroupDirective, Validators } from '@angular/forms';
import { AlertBoxService } from 'src/app/shared/alert-box.service';
import { CompetenceRequestService } from '../competence-request.service';
import { AuthService } from 'src/app/auth/auth.service';

@Component({
  selector: 'app-edit-competence-progress',
  templateUrl: './edit-competence-progress.component.html',
  styleUrls: ['./edit-competence-progress.component.css']
})
export class EditCompetenceProgressComponent implements OnInit {

  constructor(public dialogRef: MatDialogRef<EditCompetenceProgressComponent>,
    @Inject(MAT_DIALOG_DATA) public editData: any, private alertBoxService: AlertBoxService,
    private compData: CompetenceRequestService, private authService: AuthService) { }

  ngOnInit() {
    this.updateFormGroup = new FormGroup({
      compType: new FormControl(),
      domainSubDomain: new FormControl(),
      technology: new FormControl(),
      vendor: new FormControl(),
      compServiceArea: new FormControl(),
      ambition: new FormControl(),
      status: new FormControl(),
      id: new FormControl(),
      ojtHours: new FormControl('', [Validators.required, Validators.min(0), Validators.max(10000000)]),
      delScore: new FormControl('', [Validators.required, Validators.min(0), Validators.max(10)]),
      scopeComp: new FormControl('', [Validators.required, Validators.min(0), Validators.max(10)]),
      assessmentScore: new FormControl('', [Validators.required, Validators.min(0), Validators.max(10)]),
      requestedBySignum: new FormControl(),
      totalScore: new FormControl(),
      compDevelopmentStatus: new FormControl()
    })
  }

  updateFormGroup: FormGroup;
  loggedInSignum = this.authService.getAuthData().loggedInSignum;
  // delScoreValidationFailed = false;
  // scopeCompValidationFailed = false;
  // assessmentScoreValidationFailed = false;

  close() {
    this.dialogRef.close();
  }

  getErrorMessage(controlName) {
    let error = '';
    if (this.updateFormGroup.controls[controlName].hasError('required')) {
      error = 'You must enter a value';
    }
    else if (this.updateFormGroup.controls[controlName].hasError('max') || this.updateFormGroup.controls[controlName].hasError('min')) {
      if (controlName == 'ojtHours') {
        error = 'Value should be between 0 to 10000000';
      }
      else {
        error = 'Value should be between 0 to 10';
      }
    }

    return error;
  }

  editCompProgressValues(editCompetenceProgress: FormGroupDirective) {
    let formValues = editCompetenceProgress.value;
    let dataObj = new Object();
    dataObj["id"] = formValues.id;
    dataObj["ojtHours"] = formValues.ojtHours;
    dataObj["deliveryScore"] = formValues.delScore;
    dataObj["scopeComplexity"] = formValues.scopeComp;
    dataObj["assessmentScore"] = formValues.assessmentScore;
    dataObj["loggedInSignum"] = this.loggedInSignum;
    dataObj["lmSignum"] = this.loggedInSignum;
    dataObj["requestedBySignum"] = formValues.requestedBySignum;

    this.compData.updateCompetenceAssesmentData(dataObj).subscribe(
      (res: any) => {
        if (res) {
          if (res.formMessageCount != 0) {
            this.dialogRef.close();
            this.alertBoxService.responseAlert("Success", res.formMessages[0]);
          }
          else if (res.formErrorCount != 0) {
            this.alertBoxService.responseAlert("Error", res.formErrors[0]);
          }
        }
      })
  }

  isValidated(editCompetenceProgress: FormGroupDirective) {
    let formValues = editCompetenceProgress.value;
    let OK = true;

    if (formValues.delScore < 0 || formValues.delScore > 10) {
      // this.delScoreValidationFailed = true;
      OK = false;
    }
    if (formValues.scopeComp < 0 || formValues.scopeComp > 10) {
      // this.scopeCompValidationFailed = true;
      OK = false;
    }
    if (formValues.assessmentScore < 0 || formValues.assessmentScore > 10) {
      // this.assessmentScoreValidationFailed = true;
      OK = false;
    }

    return OK;
  }

  editCompProgress(editCompetenceProgress: FormGroupDirective) {

    if (this.isValidated(editCompetenceProgress)) {
      let obj = {
        "message": "Are you sure you want to update this competence?",
        "allBtns": [
          {
            "name": "Yes",
            "class": "primary",
            "action": () => {
              this.editCompProgressValues(editCompetenceProgress);
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

  validateInput(obj, controlName) {

    let inputValue = obj.updateFormGroup.controls[controlName].value;
    if (inputValue == ">") {
      this.updateFormGroup.controls[controlName].setValue('');
    }

    // if (inputValue < 0) {
    //   this.updateFormGroup.controls[controlName].setValue('0');
    // }
    // if (inputValue > 10) {
    //   this.updateFormGroup.controls[controlName].setValue('10');
    // }

  }

  removeLeadingZero(obj, controlName) {
    let inputValue = obj.updateFormGroup.controls[controlName].value;

    if (inputValue < '0' || isNaN(inputValue)) {
      inputValue = '0';
      this.updateFormGroup.controls[controlName].setValue(inputValue);
      return;
    }

    if (inputValue > '0') {
      inputValue = parseFloat(inputValue);
      if (inputValue.toString().indexOf('.') != -1) {
        inputValue = parseFloat(parseFloat(inputValue).toFixed(2));
      }
      this.updateFormGroup.controls[controlName].setValue(inputValue);
    }
  }

}
