import { MatDialog, MatDialogRef } from '@angular/material';
import { AlertBoxComponent } from './alert-box/alert-box.component';
import { Injectable } from '@angular/core';
import { CompetenceAlertComponent } from './competence-alert/competence-alert.component';
import { ConnectionAlertComponent } from './connection-alert/connection-alert.component';

@Injectable({
    providedIn: "root"
})
export class AlertBoxService {
    buttonAction = [];
    constructor(public dialog: MatDialog) { }
    alertBox(obj: any) {
        this.openDialog(obj);
    }

    openDialog(obj): void {
        const dialogRef = this.dialog.open(AlertBoxComponent, {
            width: '40%',
            data: { "message": obj.message, "allBtns": obj.allBtns },
            panelClass: 'alertBoxCSS'
        });
        this.buttonAction = [];
        for (let i = 0; i < obj.allBtns.length; i++) {
            this.buttonAction.push(obj.allBtns[i].action);
        }

        const sub = dialogRef.componentInstance.alertAction.subscribe((number) => {
            this.buttonAction[number]();
            dialogRef.close();
        })
    }

    responseAlert(dialogResponse, dialogMessage): void {
        const dialogRef = this.dialog.open(CompetenceAlertComponent, {
            width: '40%',
            data: { response: dialogResponse, message: dialogMessage }
        });
    }
/* istanbul ignore next */
    connectionAlert(dialogResponse, dialogMessage): void {
     const dialogRef = this.dialog.open(ConnectionAlertComponent, {
            width: '40%',
            data: { response: dialogResponse, message: dialogMessage }
        });
        dialogRef.disableClose = true;
        if(dialogResponse == "Success" && dialogMessage == ""){
                this.dialog.closeAll();
        }
    }
}
