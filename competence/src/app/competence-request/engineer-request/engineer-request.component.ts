import { Component, OnInit, ViewChild, Input, Inject, Output } from '@angular/core'
import { MatDialogRef, MatTableDataSource, MatSort, MatPaginator, MatDialogConfig, MatDialog, MAT_DIALOG_DATA } from '@angular/material';
import { SelectionModel } from '@angular/cdk/collections';
import { CompetenceRequestService } from '../competence-request.service';
import { AuthService } from 'src/app/auth/auth.service';
// import { CompetenceAlertComponent } from 'src/app/shared/competence-alert/competence-alert.component'
import { Router } from '@angular/router';
import { Location } from '@angular/common';
import { CompetenceProgressLMComponent } from '../competence-progress-lm/competence-progress-lm.component';
import { AlertBoxService } from 'src/app/shared/alert-box.service';
import { EngineerComponent } from '../engineer/engineer.component';


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
}



const ELEMENT_DATA: PeriodicElement[] = [];
@Component({

  selector: 'app-engineer-request',
  templateUrl: './engineer-request.component.html',
  styleUrls: ['./engineer-request.component.css']
})
export class EngineerRequestComponent implements OnInit {
  // Enggcompetence: FormGroup;
  diasabledSelectAllCheck:boolean = false;
  disabledRejectButton: boolean = true;
  disabledAcceptButton: boolean = true;
  hideButtons: boolean = false;
  deleteButton: boolean = true;
  displayedColumns: string[] = ['select', 'actions', 'competence_type', 'domain', 'technology', 'vendor', 'competence_service_area', 'baseline', 'ambition', 'status'];
  loggedInSignum = this.authService.getAuthData().loggedInSignum;
  dataSource = new MatTableDataSource<PeriodicElement>(ELEMENT_DATA);;
  @ViewChild(MatPaginator, { static: true }) paginator: MatPaginator;
  @ViewChild(MatSort, { static: true }) sort: MatSort;

  selection = new SelectionModel<PeriodicElement>(true, []);
  exportShow = false;
  //dialog: any;
  constructor(private service: CompetenceRequestService,
    public dialogRef: MatDialogRef<EngineerRequestComponent>, private dialog: MatDialog, @Inject(MAT_DIALOG_DATA) public competenceData: any, private authService: AuthService, private router: Router, private location: Location, private alertBoxService : AlertBoxService) { 

  
    }
  
  exportProgress(){
    this.child.exportProgressTable();
  }

  tabClick(tab) {    
    if (tab.index == 1) {      
      this.getCompetenceProgress();
      this.hideButtons = true;
    }
    else{
      this.updateTableAll();
      this.hideButtons = false;
    }
  }

  @ViewChild(CompetenceProgressLMComponent, { static: false }) child: CompetenceProgressLMComponent;
  getCompetenceProgress() {    
    this.child.selectedSignum = this.competenceData.requestedSignum;
    this.child.selectedName = this.competenceData.signum;
    this.child.updateTable();
  }

  isAllSelected() {
    const numSelected = this.selection.selected.length;
    // const page = this.dataSource.paginator.pageSize;
    // return numSelected === page;
    const numRows = this.dataSource.filteredData.filter((row) => row.status == "Sent To Manager").length;
    return numSelected === numRows;

  }

  masterToggle() {
    this.isAllSelected() ?
      this.selection.clear() :
      this.dataSource.filteredData.forEach((row) => {
        if(row.status == "Sent To Manager" ){
            return this.selection.select(row);
        }
    });

  }
  selectRows() {
    for (let index = 0; index < this.dataSource.paginator.pageSize; index++) {
      this.selection.select(this.dataSource.data[index]);
      //this.selectionAmount = this.selection.selected.length;
    }
  }

  // openDialog(dialogResponse, dialogMessage): void {
  //   const dialogRef = this.dialog.open(CompetenceAlertComponent, {
  //     width: '40%',
  //     data: { response: dialogResponse, message: dialogMessage }
  //   });
  // }

  changeCheck(event) {
    if (this.selection.selected.length != 0) {
      this.disabledRejectButton = false;
      this.disabledAcceptButton = false;
      this.deleteButton = false;
    }
    else {
      this.disabledRejectButton = true;
      this.disabledAcceptButton = true;
      this.deleteButton = true;
    }
    // if(this.disabledAcceptButton == false && this.disabledRejectButton == false){
    //     this.disabledAcceptButton = !event.checked;
    //     this.disabledRejectButton = !event.checked;
    // }
  }
  /** The label for the checkbox on the passed row */
  checkboxLabel(row?: PeriodicElement): string {
    if (!row) {
      return `${this.isAllSelected() ? 'select' : 'deselect'} all`;
    }
    return `${this.selection.isSelected(row) ? 'deselect' : 'select'} row ${row.id + 1}`;

  }

  deleteRequest(){
    let obj = {
      "message" : "Are you sure you want to delete the selected competences?",
      "allBtns" : [
          {
              "name": "Yes",
              "class": "primary",
              "action": () => {
                  this.deleteAll();                        
              }
          },
          {
              "name": "No",
              "class": "warn",
              "action": () => {}
          }
      ]
  }

  this.alertBoxService.alertBox(obj);
  }

  exportShowButton(isShow){
    this.exportShow = isShow;
  }

  deleteAll() {

    let allRows = this.selection.selected;
    let competenceData = [];
    for (let i = 0; i < allRows.length; i++) {
      let item = {
        id: allRows[i].id,
        parentSystemID: allRows[i].id,
        isEditable: false,
        status: "Deleted",
        changedBy:"LM"
      };
      competenceData.push(item);
    }

    this.service.addCompetence(competenceData).subscribe(
      (res: any) => {
        //console.log('Insert : ', res);
        if (res) {
          //this.updateTable();
          this.updateTableAll();//For Now

          if (res.formErrorCount != 0) {
            this.alertBoxService.responseAlert("Error", res.formErrors[0]);
          }
          else if (res.formMessageCount != 0) {
            this.alertBoxService.responseAlert("Success", res.formMessages[0]);
          }
        }
      },
      (error) => console.log('POST Error->', error)
    );


  }
  editRecord(row_obj) {


    const dialogConfig = new MatDialogConfig();
    dialogConfig.disableClose = true;
    dialogConfig.autoFocus = true;
    dialogConfig.width = "30%";
    //dialogConfig.data = competenceType;
    dialogConfig.data = row_obj;
    
    //dialogConfig.
    this.dialog.open(EngineerComponent, dialogConfig).afterClosed().subscribe(response =>{
      this.updateTableAll();
    });
  }

  ngOnInit() {
    this.updateTableAll();

  }

  updateTableAll() {

    this.diasabledSelectAllCheck = false;

    let dataForCompAll = {
      "requestedStatus": ["Sent To Manager","Approved","Rejected"],
      "loggedInSignum": this.loggedInSignum,
      "requestedBySignum": this.competenceData.requestedSignum,
      "isProgress":"false"
      // "loggedInSignum": "EBMHUAK",
      // "requestedBySignum": "EBMHUAK"
    }


    this.service.getSavedCompetence(dataForCompAll).subscribe(
      (response : any) => {
        if(response.length!=0){
          this.exportShow = true;
        }
        else{
          this.exportShow = false;
        }
        //console.log('GET Competence All', response)
        this.dataSource = new MatTableDataSource(response as PeriodicElement[]);
        this.dataSource.paginator = this.paginator;
        this.selection = new SelectionModel<PeriodicElement>(true, []);
        this.disabledRejectButton = true;
        this.disabledAcceptButton = true;
        this.deleteButton = true;
        for(let i=0;i<response.length; i++){
          if(response[i].status == "Sent To Manager"){
            this.diasabledSelectAllCheck = true;
          }
        }
      }
    )
  }
  onClose() {

    this.dialogRef.close();

    //this.comp.getCompetenceData();
    this.router.navigateByUrl("/refresh-page", { skipLocationChange: true }).then(() => {
      //console.log(decodeURI(this.location.path()));
      this.router.navigate([decodeURI(this.location.path())]);
    });
  }
  applyFilter(filterValue: string) {
    this.dataSource.filter = filterValue.trim().toLowerCase();
    this.selection = new SelectionModel<PeriodicElement>(true, []);

    if (this.dataSource.paginator) {
      this.dataSource.paginator.firstPage();
    }
  }

  rejectRequest(){
    let obj = {
      "message" : "Are you sure you want to reject the selected competences?",
      "allBtns" : [
          {
              "name": "Yes",
              "class": "primary",
              "action": () => {
                  this.rejectReq();                        
              }
          },
          {
              "name": "No",
              "class": "warn",
              "action": () => {}
          }
      ]
  }

  this.alertBoxService.alertBox(obj);
  }

  rejectReq() {
    let allRows = this.selection.selected;
    let competenceData = [];
    for (let i = 0; i < allRows.length; i++) {
      let item = {
        id: allRows[i].id,
        parentSystemID: allRows[i].id,
        isEditable: false,
        status: "Rejected",
        changedBy:"LM"
      };
      competenceData.push(item);
    }

    this.service.addCompetence(competenceData).subscribe(
      (res: any) => {
       // console.log('Insert : ', res);
        if (res) {
          //this.updateTable();
          this.updateTableAll();//For Now

          if (res.formErrorCount != 0) {
            this.alertBoxService.responseAlert("Error", res.formErrors[0]);
          }
          else if (res.formMessageCount != 0) {
            this.alertBoxService.responseAlert("Success", res.formMessages[0]);
          }
        }
      },
      (error) => console.log('POST Error->', error)
    );
  }

  acceptRequest(){
    let obj = {
      "message" : "Are you sure you want to approve the selected competences?",
      "allBtns" : [
          {
              "name": "Yes",
              "class": "primary",
              "action": () => {
                  this.acceptReq();                        
              }
          },
          {
              "name": "No",
              "class": "warn",
              "action": () => {}
          }
      ]
  }

  this.alertBoxService.alertBox(obj);
  }

  acceptReq() {
    let allRows = this.selection.selected;
    let competenceData = [];
    for (let i = 0; i < allRows.length; i++) {
      let item = {
        id: allRows[i].id,
        parentSystemID: allRows[i].id,
        isEditable: false,
        status: "Approved",
        changedBy:"LM"
      };
      competenceData.push(item);
    }

    this.service.addCompetence(competenceData).subscribe(
      (res: any) => {
        //console.log('Insert : ', res);
        if (res) {
          //this.updateTable();
          this.updateTableAll();

          if (res.formErrorCount != 0) {
            let errorMessage = res.errorsDetail["Error Description"];
            if(res.errorsDetail["Error Code"] == 101){
              errorMessage = `One of the requests has been modified by engineer. Now all requests are updated, please try again.`
            }
            this.alertBoxService.responseAlert("Error", errorMessage);
          }
          else if (res.formMessageCount != 0) {
            this.alertBoxService.responseAlert("Success", res.formMessages[0]);
          }
        }
      },
      (error) => console.log('POST Error->', error)
    );
  }

  deleteSingleCompetence(id){
    let obj = {
        "message" : "Are you sure you want to delete this competence?",
        "allBtns" : [
            {
                "name": "Yes",
                "class": "primary",
                "action": () => {
                    this.DeleteCompetence(id);                        
                }
            },
            {
                "name": "No",
                "class": "warn",
                "action": () => {}
            }
        ]
    }

    this.alertBoxService.alertBox(obj);
}

  DeleteCompetence(id) {
    let competenceData =
      [
        {
          id: id,
          parentSystemID: id,
          isEditable: false,
          status: "Deleted",
          changedBy:"LM"
        }
      ]

    this.service.addCompetence(competenceData).subscribe(
      (res) => {
        //console.log('Insert : ', res);
        if (res) {
          //this.updateTable();
          this.updateTableAll();//For Now
        }
      },
      (error) => console.log('POST Error->', error)
    );

  }
}