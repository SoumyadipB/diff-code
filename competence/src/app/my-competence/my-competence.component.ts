import { Component, ViewChild } from '@angular/core';
import { Competence } from './add-competence/add-competence.component';
import { CompetenceListComponent } from './competence-list/competence-list.component';
import { AddCompetenceComponent } from './add-competence/add-competence.component';
import { CompetenceProgressComponent } from './competence-progress/competence-progress.component';

@Component({
  selector: 'app-my-competence',
  templateUrl: './my-competence.component.html',
  styleUrls: ['./my-competence.component.css']
})
export class MyCompetenceComponent {

  @ViewChild(CompetenceListComponent, { static: false }) child2: CompetenceListComponent;
  isUpateTable(statusObj) {
    if (statusObj.status == "Initiated" && statusObj.reloadAllTables == false){
      this.child2.updateTable();
    }
    else if(statusObj.status == "Initiated" && statusObj.reloadAllTables == true){
      this.child2.updateTable();
      this.child2.updateTableAll();
    }
    else{
      this.child2.updateTableAll();
    }
      
  }

  @ViewChild(AddCompetenceComponent, { static: false }) child1: AddCompetenceComponent;
  editCompParent(row) {
    this.child1.editCompChild(row);
  }

  tabClick(tab) {
    if(tab.index == 1){
      this.getCompetenceProgress();
    }
  }

  @ViewChild(CompetenceProgressComponent, { static: false }) child3: CompetenceProgressComponent;
  getCompetenceProgress(){
    this.child3.updateTable();
  }


}

