import { Component, OnInit, ViewChild } from '@angular/core';
import { MatTableDataSource, MatPaginator } from '@angular/material';
import { PeriodicElement } from '../competence-list/competence-list.component';
import { MyCompetenceService } from '../my-cometence.service';
import { AuthService } from 'src/app/auth/auth.service';

const ELEMENT_DATA: PeriodicElement[] = [];

@Component({
  selector: 'app-competence-progress',
  templateUrl: './competence-progress.component.html',
  styleUrls: ['./competence-progress.component.css']
})
export class CompetenceProgressComponent implements OnInit {

  displayedColumns1: string[] = ['competence_type', 'domain', 'technology', 'vendor', 'competence_service_area', 'ambition', 'status', 'wblHours', 'iltHours', 'ojtHours', 'delScore', 'scopeComp', 'assessmentScore', 'totalScore', 'compDevelepmentStatus'];
  dataSource = new MatTableDataSource<PeriodicElement>(ELEMENT_DATA);
  loggedInSignum = this.authService.getAuthData().loggedInSignum;
  progressUploadedDate : any;

  constructor(private compData: MyCompetenceService, private authService: AuthService) { }

  @ViewChild(MatPaginator, { static: true }) paginator: MatPaginator;
  
  ngOnInit() {
    // this.updateTable();
  }

  applyFilter(filterValue: string) {
    this.dataSource.filter = filterValue.trim().toLowerCase();
  }

  updateTable() {


    let dataForComp = {
        "requestedStatus": ["Approved"],
        "loggedInSignum": this.loggedInSignum,
        "requestedBySignum": this.loggedInSignum,
        "isProgress":"true"
    }


    this.compData.getSavedCompetence(dataForComp).subscribe(
        (response) => {
            // console.log('GET Competence', response);
            this.dataSource = new MatTableDataSource(response as PeriodicElement[]);
            this.dataSource.paginator = this.paginator;
            this.progressUploadedDate = response[0].FileUploadedOn;
        }
    )
}

}
