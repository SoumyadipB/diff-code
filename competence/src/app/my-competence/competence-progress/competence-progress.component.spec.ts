import { async, ComponentFixture, TestBed, tick, fakeAsync } from '@angular/core/testing';


import { DebugElement } from '@angular/core';
import { By, BrowserModule } from '@angular/platform-browser';
import { MyCompetenceService } from '../my-cometence.service';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { MatOptionModule, MatSelectModule, MatCardModule, MatGridListModule, MatTableModule, MatIconModule, MatButtonToggleModule, MatInputModule, MatSlideToggleModule, MatDialogModule, MatCheckboxModule, MatPaginatorModule } from '@angular/material';
import { HttpClientModule } from '@angular/common/http';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { RouterModule } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { AuthService } from 'src/app/auth/auth.service';
import { HTTP_INTERCEPTORS } from '@angular/common/http';
import { HttpEvent, HttpEventType } from '@angular/common/http';
import {
  HttpClientTestingModule,
  HttpTestingController
} from '@angular/common/http/testing';
import { AuthInterceptor } from 'src/app/shared/auth.interceptor';
import { analyzeAndValidateNgModules } from '@angular/compiler';
import { AlertBoxComponent } from 'src/app/shared/alert-box/alert-box.component';
import { Alert } from 'selenium-webdriver';
import { BrowserDynamicTestingModule } from '@angular/platform-browser-dynamic/testing';
import { AlertBoxService } from 'src/app/shared/alert-box.service';
import { Observable } from 'rxjs/Rx';
import { CompetenceProgressComponent } from './competence-progress.component';
import { first } from 'rxjs/operators';
//import { CompetenceListComponentMock } from './competence-list.mock';

class MockAuthService extends AuthService{ 
   userRole = 'Engineer';
   loggedInSignum = 'EKOMKHA';
   loggedInEmpName = 'Komal';
   getAuthData(){
    return {
      userRole: 'user',
        loggedInSignum: 'EKOMKHA',
        empName:'EKOMKHA'
      }
   }
    }  

  class MockMyCompetenceService extends MyCompetenceService{
    constructor(){
      super(null);
    }
     dataForComp = {
      "requestedStatus": ["Initiated"],
      "loggedInSignum": "EKOMKHA",
      "requestedBySignum": "EKOMKHA"
  }
  getSavedCompetence(dataForComp){
    console.log('sending fake answers!');
    return Observable.of([
      {
        Active: true, BaselineName: "A", CompetenceID: null, CompetenceType: "Tools Competence", CompetencyUpgrade: "A to A",
        Competency_Service_Area: "Optimization- Pre Launch", DomainSubDomain: "Radio Access Networks / LTE RAN",
        FileUploadedOn: 1567750855003, ILT_TOTAL_HRS: null, Technology: "CDMA",Vendor: "Nortel", WBL_TOTAL_HRS: null,
        baseline: 2, changedBy: "engineer", competanceUpgradeLevel: "same", competenceUpgradeID: 6, createdBy: "EKOMKHA",
        createdon: 1567752919000, deliveryCompetanceID: 2043, lmSignum: "EKUYOGE", loggedInSignum: "EKOMKHA",
        parentSystemID: 0, requestedBySignum: "EKOMKHA", rowversion: "AAAAAAACGb0=", slmSignum: "EDBEVS", status: "Initiated",
        systemID: 50, userCompetanceID: null
      }]);
  }
  
  }

fdescribe('CompetenceProgressComponent', () => {
  let component: CompetenceProgressComponent;
  let fixture: ComponentFixture<CompetenceProgressComponent>;
  let de: DebugElement;
  let el: HTMLElement;
  let competenceService: MockMyCompetenceService;
  let authService:MockAuthService;
  let alertBoxService:AlertBoxService;
  let dataForComp; 
  let engineerList:any;
//   let testUsers: PeriodicElement[] = [{BaselineName: "A",CompetenceType: "Tools Competence",
//   CompetencyUpgrade: "A to A",Competency_Service_Area: "Optimization- Pre Launch",DomainSubDomain: "Radio Access Networks / LTE RAN",
//   ILT_TOTAL_HRS: null,Technology: "CDMA",Vendor: "Nortel",WBL_TOTAL_HRS: null,baseline: 2,changedBy: "engineer",
//   competenceUpgradeID: 6,createdBy: "EKOMKHA",createdon: 1567752919000,deliveryCompetanceID: 2043,
//   lmSignum: "EKUYOGE",loggedInSignum: "EKOMKHA",parentSystemID: 0,requestedBySignum: "EKOMKHA",
//   rowversion: "AAAAAAACGb0=",slmSignum: "EDBEVS",status: "Initiated",systemID: 50,userCompetanceID: null 
// }];

  beforeEach(async(() => {
    
    TestBed.configureTestingModule({
      declarations: [ CompetenceProgressComponent ],
      imports: [
        BrowserModule, FormsModule, ReactiveFormsModule, MatOptionModule, MatSelectModule,
        MatCardModule, MatGridListModule, MatTableModule, MatIconModule, HttpClientModule,
        BrowserAnimationsModule, MatInputModule, RouterModule, RouterTestingModule, MatDialogModule, 
        MatCheckboxModule, MatPaginatorModule
    ],    
    providers:[MockAuthService, MockMyCompetenceService, {provide: HTTP_INTERCEPTORS,
      useClass: AuthInterceptor,
      multi: true}, AlertBoxService]
      
    })
    //.overrideModule(CompetenceListComponent, { set: { entryComponents: [AlertBoxComponent] } })

    .compileComponents().then(() => {
        fixture = TestBed.createComponent(CompetenceProgressComponent);
        component = fixture.componentInstance;
        de = fixture.debugElement.query(By.css('table'));
        el = de.nativeElement;        
        competenceService = fixture.debugElement.injector.get(MockMyCompetenceService);
        authService = fixture.debugElement.injector.get(MockAuthService);
       
        fixture.detectChanges();
        component.loggedInSignum = 'EKOMKHA';
    })
    
    
  }));  

  fit('call applyfilter() method', async() => {
    component.applyFilter('d');
  expect(component.applyFilter).toBeTruthy();
  })
  
  fit('call getSavedCompetetence method', async() => {
   let dataForComp = {
      "requestedStatus": ["Initiated"],
      "loggedInSignum": "EKOMKHA",
      "requestedBySignum": "EKOMKHA"
  }
      component.updateTable();
      expect(component.updateTable).toBeTruthy();
  })
});
