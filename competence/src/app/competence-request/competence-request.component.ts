import { Component, OnInit, ViewChild, Input } from '@angular/core';
import { CompetenceRequestService } from './competence-request.service'
import { MatPaginator } from '@angular/material/paginator';
import { MatSort } from '@angular/material/sort';
import { MatTableDataSource } from '@angular/material/table';
import { MatDialog, MatDialogConfig } from '@angular/material/dialog';
import { EngineerRequestComponent } from './engineer-request/engineer-request.component'
import { SelectionModel } from '@angular/cdk/collections';
// import {SelectionModel} from '@angular/cdk/collections';
import { AuthService } from 'src/app/auth/auth.service';
//import {ConnectionService} from 'ng-connection-service'
//import { AlertBoxService } from 'src/app/shared/alert-box.service';
export interface CompElement {
  signum: string;
  domain: string;
  niche: string;
  tools: string;
  totalCount: string;
  raisedCount: string;
}



const ELEMENT_DATA: CompElement[] = [];
@Component({
  selector: 'app-competence-request',
  templateUrl: './competence-request.component.html',
  styleUrls: ['./competence-request.component.css']
})
export class CompetenceRequestComponent implements OnInit {
  @Input() competencelist: string[] = [];
  displayedColumns: string[] = ['actions', 'signum', 'domain', 'niche','tool', 'pendingapproval', 'ambition'];
  dataSource = new MatTableDataSource<CompElement>(ELEMENT_DATA);
  //dataSource;
  selection = new SelectionModel<CompElement>(true, []);
  competenceList: string[] = [];
  loggedInSignum = this.authService.getAuthData().loggedInSignum;

  @ViewChild(MatPaginator, { static: true }) paginator: MatPaginator;
  @ViewChild(MatSort, { static: true }) sort: MatSort;
  //internetConn:any = 'Connection is alive';
  constructor(private compLMData: CompetenceRequestService, private dialog: MatDialog,private authService:AuthService) {
    //,private conn:ConnectionService,private alertBoxService: AlertBoxService
//  this.conn.monitor().subscribe(connectivity=>{
// connectivity ? this.alertBoxService.connectionAlert("Success",''):this.alertBoxService.connectionAlert("Error",'Internet Connection Lost');
// });

   }


  ngOnInit() {
    let getC;
    //getC=this.compLMData.getCompetenceForApproval();
    //console.log(getC)
    //this.dataSource = new MatTableDataSource<competencereq>(getC);


    //this.dataSource.paginator = this.paginator;
    //this.dataSource.sort = this.sort;
    this.getCompetenceData();

  }

  getCompetenceData() {

    let dataForCompAll = {
      "loggedInSignum": this.loggedInSignum,
    }

    this.compLMData.getCompetenceForApproval(dataForCompAll).subscribe(
      (response) => {
        let dataSourceFinal = []
        //console.log('GET Competence All', response)
        let x = new Object;
        /* istanbul ignore next */
        for (var key in response) {
          // skip loop if the property is from prototype
          if (!response.hasOwnProperty(key)) continue;
         // console.log(key);
          var obj = response[key];
          for (var prop in obj) {
            // skip loop if the property is from prototype
            if (!obj.hasOwnProperty(prop)) continue;
              x[prop] = obj[prop];
            // your code
            // console.log(prop + " = " + obj[prop]);
          }
         // console.log(x)
         /* istanbul ignore next */
          let dataObj ={
            "signum":x["requestedBySignum"],
            "domain":x["Domain Competence"],
            "tool":x["Tools Competence"],
            "niche":x["Niche Area competence"],
            "totalCount":x["raisedAmbitionCount"],
            "raisedCount":x["request_count"],
            "requestedSignum":x["signum"]
            
          }
          //console.log("JSON",dataObj)
          /* istanbul ignore next */
          if(dataObj.domain == null || dataObj.domain == undefined){
            dataObj.domain = 0;
          }
          /* istanbul ignore next */
          if(dataObj.niche == null || dataObj.niche == undefined){
            dataObj.niche = 0;
          }
          /* istanbul ignore next */
          if(dataObj.tool == null || dataObj.tool == undefined){
            dataObj.tool = 0;
          }
          /* istanbul ignore next */
          if(dataObj.totalCount == null || dataObj.totalCount == undefined){
            dataObj.totalCount = 0;
          }
          /* istanbul ignore next */
          if(dataObj.raisedCount == null || dataObj.raisedCount == undefined){
            dataObj.raisedCount = 0;
          }
          dataSourceFinal.push(dataObj);
        }


        this.dataSource = new MatTableDataSource(dataSourceFinal as CompElement[]);
        this.selection = new SelectionModel<CompElement>(true, []);
        this.dataSource.paginator = this.paginator;
      }
    )
  }
/* istanbul ignore next */
  applyFilter(filterValue: string) {
    this.dataSource.filter = filterValue.trim().toLowerCase();

    if (this.dataSource.paginator) {
      this.dataSource.paginator.firstPage();
    }
  }
  openDialog(row_obj) {
    //this.compLMData.populateForm(row);

    const dialogConfig = new MatDialogConfig();
    dialogConfig.disableClose = true;
    dialogConfig.autoFocus = true;
    dialogConfig.width = "90%";
    // dialogConfig.minWidth = "190vh";
    dialogConfig.maxWidth = "190vh";
    dialogConfig.data = row_obj;
    this.dialog.open(EngineerRequestComponent, dialogConfig);
  }

}



