import { Component, OnInit, Inject } from '@angular/core';
import {MatDialog, MatDialogRef, MAT_DIALOG_DATA} from '@angular/material/dialog';

export interface DialogData {
  response: string;
  message: string;
}

@Component({
  selector: 'app-competence-alert',
  templateUrl: './competence-alert.component.html',
  styleUrls: ['./competence-alert.component.css']
})


export class CompetenceAlertComponent implements OnInit {
  
  constructor(
    public dialogRef: MatDialogRef<CompetenceAlertComponent>,
    @Inject(MAT_DIALOG_DATA) public data: any) {}

  onNoClick(): void {
    this.dialogRef.close();
  }


  ngOnInit() {
  }

}
