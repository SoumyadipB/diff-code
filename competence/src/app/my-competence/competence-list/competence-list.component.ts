import { Component, Input, Output, EventEmitter, ViewChild } from '@angular/core';
import { MyCompetenceService } from '../my-cometence.service';
import { Competence } from '../add-competence/add-competence.component';
import { MatTableDataSource } from '@angular/material/table';
import { SelectionModel } from '@angular/cdk/collections';
import { AuthService } from 'src/app/auth/auth.service';
import { THIS_EXPR } from '@angular/compiler/src/output/output_ast';
// import { CompetenceAlertComponent } from 'src/app/shared/competence-alert/competence-alert.component';
import { MatDialog, MatDialogRef, MAT_DIALOG_DATA } from '@angular/material/dialog';
import { MatPaginator } from '@angular/material';
import { AlertBoxService } from 'src/app/shared/alert-box.service';


export interface PeriodicElement {
    BaselineName: string;
    CompetenceType: string;
    CompetencyUpgrade: string;
    Competency_Service_Area: string;
    DomainSubDomain: string;
    Technology: string;
    Vendor: string;
    baseline: number;
    changedBy: string;
    competenceUpgradeID: number;
    createdBy: string;
    createdon: number;
    deliveryCompetanceID: number;
    lmSignum: string;
    loggedInSignum: string;
    parentSystemID: number;
    requestedBySignum: string;
    rowversion: string;
    slmSignum: string;
    status: string;
    id: number;
    userCompetanceID: number;
    WBL_TOTAL_HRS: any;
    ILT_TOTAL_HRS: any;
}

const ELEMENT_DATA: PeriodicElement[] = [];

@Component({
    selector: 'app-engg-competence-list',
    templateUrl: './competence-list.component.html',
    styleUrls: ['./competence-list.component.css']
})

export class CompetenceListComponent {
    displayedColumns1: string[] = ['select1', 'actions', 'competence_type', 'domain', 'technology', 'vendor', 'competence_service_area', 'baseline', 'ambition', 'status'];
    dataSource = new MatTableDataSource<PeriodicElement>(ELEMENT_DATA);
    dataSourceAll = new MatTableDataSource<PeriodicElement>(ELEMENT_DATA);
    selection1 = new SelectionModel<PeriodicElement>(true, []);
    selection2 = new SelectionModel<PeriodicElement>(true, []);
    displayedColumns2: string[] = ['select2', 'actions', 'competence_type', 'domain', 'technology', 'vendor', 'competence_service_area', 'baseline', 'ambition', 'status'];
    loggedInSignum = this.authService.getAuthData().loggedInSignum;
    filter1 : string = '';
    filter2 : string = '';
    filterText1 : string = '';
    filterText2 : string = '';

    constructor(private compData: MyCompetenceService, private authService: AuthService, public dialog: MatDialog, private alertBoxService : AlertBoxService) { }

    ngOnInit() {
        this.updateTable();
        this.updateTableAll();
    }    
    
    @ViewChild(MatPaginator, { static: true }) paginator: MatPaginator;

    applyFilter1(filterValue: string) {
        this.dataSource.filter = filterValue.trim().toLowerCase();
        this.filter1 = filterValue.trim().toLowerCase();
        this.selection1 = new SelectionModel<PeriodicElement>(true, []);
    }

    applyFilter2(filterValue: string) {
        this.dataSourceAll.filter = filterValue.trim().toLowerCase();
        this.filter2 = filterValue.trim().toLowerCase();
        this.selection2 = new SelectionModel<PeriodicElement>(true, []);
    }
    
    isAllSelected1() {
        const numSelected = this.selection1.selected.length;
        const numRows = this.dataSource.filteredData.length;
        return numSelected === numRows;
    }
 
    isAllSelected2() {
        const numSelected = this.selection2.selected.length;
        const numRows = this.dataSourceAll.filteredData.filter((row) => row.status == "Rejected").length;
        return numSelected === numRows;
    }
/* istanbul ignore next */
    masterToggle1() {
        this.isAllSelected1() ?
            this.selection1.clear() :
            this.dataSource.filteredData.forEach(row => this.selection1.select(row));
        // console.log(this.selection1.selected)            
    }
    /* istanbul ignore next */
    masterToggle2() {
        this.isAllSelected2() ?
            this.selection2.clear() :
            this.dataSourceAll.filteredData.forEach((row) => {
                if(row.status == "Rejected"){
                    return this.selection2.select(row);
                }
            });
            
    }

    checkboxLabel1(row?: PeriodicElement): string {
        if (!row) {
            return `${this.isAllSelected1() ? 'select' : 'deselect'} all`;
        }
        return `${this.selection1.isSelected(row) ? 'deselect' : 'select'} row ${row.id + 1}`;
    }

    checkboxLabel2(row?: PeriodicElement): string {
        if (!row) {
            return `${this.isAllSelected2() ? 'select' : 'deselect'} all`;
        }
        /*istanbul ignore next*/
        return `${this.selection2.isSelected(row) ? 'deselect' : 'select'} row ${row.id + 1}`;
    }

    @Output() addValuesForm = new EventEmitter<any>();

    resetSearch1(){
        this.dataSource.filter = '';
        this.filterText1 = '';
    }

    resetSearch2(){
        this.dataSourceAll.filter = '';
        this.filterText2 = '';
    }

    updateTableAll() {


        let dataForCompAll = {
            "requestedStatus": ["Sent To Manager", "Rejected", "Approved"],
            "loggedInSignum": this.loggedInSignum,
            "requestedBySignum": this.loggedInSignum,
            "isProgress":"false"
        }


        this.compData.getSavedCompetence(dataForCompAll).subscribe(
            (response) => {
                // console.log('GET Competence All', response)
                this.dataSourceAll = new MatTableDataSource(response as PeriodicElement[]);
                this.selection2 = new SelectionModel<PeriodicElement>(true, []);
                this.dataSourceAll.paginator = this.paginator;
                this.resetSearch2();
            }
        )
    }

    updateTable() {


        let dataForComp = {
            "requestedStatus": ["Initiated"],
            "loggedInSignum": this.loggedInSignum,
            "requestedBySignum": this.loggedInSignum,
            "isProgress":"false"
        }


        this.compData.getSavedCompetence(dataForComp).subscribe(
            (response) => {
                // console.log('GET Competence', response);
                this.dataSource = new MatTableDataSource(response as PeriodicElement[]);
                this.selection1 = new SelectionModel<PeriodicElement>(true, []);
                this.resetSearch1();
            }
        )
    }

    getCompValues(id) {
        let compValues;
        this.compData.getCompValuesFromApi(id).subscribe(
            (response) => {
                // console.log('GET Competence', response)
                this.addValuesForm.emit(response);
            }
        )

    }

    deleteSelectedCompetence(status){
        let obj = {
            "message" : "Are you sure you want to delete the selected competences?",
            "allBtns" : [
                {
                    "name": "Yes",
                    "class": "primary",
                     /* istanbul ignore next */  
                    "action": () => {                         
                        this.deleteMultipleCompetence(status);                        
                    }
                },
                {
                    "name": "No",
                    "class": "warn",
                   
                    "action":  /* istanbul ignore next */() =>   {}
                }
            ]
        }

        this.alertBoxService.alertBox(obj);
    }

    deleteMultipleCompetence(status) {

        let allRows;
        if (status == "Initiated")
            allRows = this.selection1.selected;
        else
         /* istanbul ignore next */
            allRows = this.selection2.selected;

        let competenceData = [];
        for (let i = 0; i < allRows.length; i++) {
            let item = {
                id: allRows[i].id,
                parentSystemID: allRows[i].id,
                isEditable: false,
                status: "Deleted",
                changedBy : "engineer"
            };
            competenceData.push(item);
        }

        this.compData.addCompetence(competenceData).subscribe(
            (res: any) => {
                // console.log('Insert : ', res);
                if (res) {
                    if (status == "Initiated"){
                        this.updateTable();
                        // this.resetSearch1();
                    }
                    else{
                         /* istanbul ignore next */
                        this.updateTableAll();
                        // this.resetSearch2();
                    }

                    if (res.formErrorCount != 0) {
                         /* istanbul ignore next */
                        this.alertBoxService.responseAlert("Error", res.formErrors[0]);
                    }
                    else if (res.formMessageCount != 0) {
                        // this.alertBoxService.openDialog(res.formMessages[0]);
                        this.alertBoxService.responseAlert("Success", res.formMessages[0]);
                    }
                }
            },
            /*istanbul ignore next */
            (error) => console.log('POST Error->', error)
        );

    }

    // openDialog(dialogResponse, dialogMessage): void {
    //     const dialogRef = this.dialog.open(CompetenceAlertComponent, {
    //         width: '40%',
    //         data: { response: dialogResponse, message: dialogMessage }
    //     });
    // }

    sendCompetenceForApproval(){
        let obj = {
            "message" : "Are you sure you want to send the selected competences for approval?",
            "allBtns" : [
                {
                    "name": "Yes",
                    "class": "primary",
                    "action": /* istanbul ignore next */() => {
                          
                        this.sendForApproval();                        
                    }
                },
                {
                    "name": "No",
                    "class": "warn",
                   
                    "action":  /* istanbul ignore next */() => {}
                }
            ]
        }

        this.alertBoxService.alertBox(obj);
    }

    sendForApproval() {
        let allRows = this.selection1.selected;
        let competenceData = [];
        for (let i = 0; i < allRows.length; i++) {
            let item = {
                id: allRows[i].id,
                parentSystemID: allRows[i].id,
                isEditable: false,
                status: "Sent To Manager",
                changedBy: "engineer"
            };
            competenceData.push(item);
        }

        this.compData.addCompetence(competenceData).subscribe(
            (res: any) => {
                // console.log('Insert : ', res);
                if (res) {
                    this.updateTable();
                    // this.resetSearch1();
                    this.updateTableAll();//For Now
                    if (res.formErrorCount != 0) {
                        /* istanbul ignore next */
                        this.alertBoxService.responseAlert("Error", res.formErrors[0]);
                    }
                    else if (res.formMessageCount != 0) {
                        this.alertBoxService.responseAlert("Success", res.formMessages[0]);
                        // this.alertBoxService.openDialog(res.formMessages[0]);
                    }
                }
            },
              /*istanbul ignore next */
            (error) => console.log('POST Error->', error)
        );

    }

    deleteSingleCompetence(id, status){
        let obj = {
            "message" : "Are you sure you want to delete this competence?",
            "allBtns" : [
                {
                    "name": "Yes",
                    "class": "primary",
                    "action": () => {
                          /* istanbul ignore next */
                        this.DeleteCompetence(id, status);                        
                    }
                },
                {
                    "name": "No",
                    "class": "warn",
                    
                    "action": /* istanbul ignore next */() => {}
                }
            ]
        }

        this.alertBoxService.alertBox(obj);
    }

    DeleteCompetence(id, status) {
        let competenceData =
            [
                {
                    id: id,
                    parentSystemID: id,
                    isEditable: false,
                    status: "Deleted",
                    changedBy : "engineer"
                }
            ]

        this.compData.addCompetence(competenceData).subscribe(
            (res: any) => {
                // console.log('Insert : ', res);
                if (res) {
                    if (status == "Initiated"){
                      
                        this.updateTable();
                        // this.resetSearch1();
                    }
                    else{
                         /* istanbul ignore next */
                        this.updateTableAll();
                    }

                    if (res.formErrorCount != 0) {
                        /* istanbul ignore next */
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



}

