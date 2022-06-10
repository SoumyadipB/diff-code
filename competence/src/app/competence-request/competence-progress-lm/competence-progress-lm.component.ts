import { Component, OnInit, ViewChild, ElementRef, Output, EventEmitter } from '@angular/core';
import { MatTableDataSource, MatPaginator, MatSort, MatDialogConfig, MatDialog } from '@angular/material';
import { PeriodicElement } from 'src/app/my-competence/competence-list/competence-list.component';
import { MyCompetenceService } from 'src/app/my-competence/my-cometence.service';
import { AuthService } from 'src/app/auth/auth.service';
import { EditCompetenceProgressComponent } from '../edit-competence-progress/edit-competence-progress.component';

const ELEMENT_DATA: PeriodicElement[] = [];

@Component({
  selector: 'app-competence-progress-lm',
  templateUrl: './competence-progress-lm.component.html',
  styleUrls: ['./competence-progress-lm.component.css']
})
export class CompetenceProgressLMComponent implements OnInit {

  displayedColumns1: string[] = ['actions', 'competence_type', 'domain', 'technology', 'vendor', 'competence_service_area', 'ambition', 'status', 'wblHours', 'iltHours', 'ojtHours', 'delScore', 'scopeComp', 'assessmentScore', 'totalScore', 'compDevelepmentStatus'];
  dataSource = new MatTableDataSource<PeriodicElement>(ELEMENT_DATA);
  loggedInSignum = this.authService.getAuthData().loggedInSignum;
  selectedSignum = "";
  selectedName = "";
  progressUploadedDate : any;

  @Output() showExport = new EventEmitter<any>();

  constructor(private compData: MyCompetenceService, private authService: AuthService, private dialog: MatDialog) { }

  ngOnInit() {
    // this.updateTable();    
    this.dataSource.sort = this.sort;
  }


  @ViewChild(MatPaginator, {static: true}) paginator: MatPaginator;
  @ViewChild(MatSort, { static: true }) sort: MatSort;

  @ViewChild('exporter', { static: true }) table: any;

  applyFilter(filterValue: string) {
    this.dataSource.filter = filterValue.trim().toLowerCase();
    /* istanbul ignore next */
    if (this.dataSource.paginator) {
      this.dataSource.paginator.firstPage();
    }
  }

  exportProgressTable(){
    this.table.exportTable('xlsx', { fileName : 'Competence_Progress_LM_' + this.selectedSignum })
  }

  updateTable() {


    let dataForComp = {
        "requestedStatus": ["Approved"],
        "loggedInSignum": this.loggedInSignum,
        "requestedBySignum": this.selectedSignum,
        "isProgress":"true"
        
    }


    this.compData.getSavedCompetence(dataForComp).subscribe(
        (response : any) => {
          if(response.length != 0){
            // console.log('GET Competence', response);
            this.dataSource = new MatTableDataSource(response as PeriodicElement[]);
            this.dataSource.paginator = this.paginator;
            this.progressUploadedDate = response[0].FileUploadedOn;
            this.showExport.emit(true);
          }
          else{
            this.showExport.emit(false);
          }
        }
    )
}

editRecord(row){
  const dialogConfig = new MatDialogConfig();
    dialogConfig.disableClose = true;
    dialogConfig.autoFocus = true;
    dialogConfig.width = "50%";
    dialogConfig.maxHeight = "95vh";
    //dialogConfig.data = competenceType;
    row.signumName = this.selectedName;
    dialogConfig.data = row;
    
    //dialogConfig.
    this.dialog.open(EditCompetenceProgressComponent, dialogConfig).afterClosed().subscribe(response =>{
      this.updateTable();
    });
}

}
