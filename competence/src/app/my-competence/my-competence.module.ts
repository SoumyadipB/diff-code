import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';

import { MatModule } from '../mat-module';

import { MyCompetenceComponent } from './my-competence.component';
import {CompetenceListComponent} from '../my-competence/competence-list/competence-list.component';
import {AddCompetenceComponent} from '../my-competence/add-competence/add-competence.component';

import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { FlexLayoutModule } from "@angular/flex-layout";
import {MatIconModule} from '@angular/material/icon';
import { MatTableExporterModule } from 'mat-table-exporter'

import { MatSlideToggleModule } from '@angular/material';
import { CompetenceProgressComponent } from './competence-progress/competence-progress.component';

@NgModule({
    declarations:[
        MyCompetenceComponent,
        CompetenceListComponent,
        AddCompetenceComponent,
        CompetenceProgressComponent,        
    ],
    
    imports:[
        CommonModule,
        MatModule,
        FormsModule,
        FlexLayoutModule,
        MatIconModule,
        
        ReactiveFormsModule,
        MatSlideToggleModule,
        MatTableExporterModule

    ]  
})

export class MyCompetenceModule{}