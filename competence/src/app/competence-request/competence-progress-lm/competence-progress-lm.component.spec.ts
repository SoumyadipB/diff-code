import { async, ComponentFixture, TestBed, tick, fakeAsync } from '@angular/core/testing';


import { DebugElement, Injectable, ComponentRef, NgModule, Directive } from '@angular/core';
import { By, BrowserModule } from '@angular/platform-browser';

import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { MatOptionModule, MatSelectModule, MatCardModule, MatGridListModule, MatTableModule, MatIconModule, MatButtonToggleModule, MatInputModule, MatSlideToggleModule, MatDialogModule, MatCheckboxModule, MatPaginatorModule, MatButtonModule, MAT_DIALOG_DATA, MatDialogRef } from '@angular/material';
import { HttpClientModule, HttpInterceptor, HttpRequest, HttpHandler } from '@angular/common/http';
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

import { first } from 'rxjs/operators';
import { CompetenceProgressLMComponent } from './competence-progress-lm.component';
import { MyCompetenceService } from 'src/app/my-competence/my-cometence.service';
import { CompetenceAlertComponent } from 'src/app/shared/competence-alert/competence-alert.component';
import { EditCompetenceProgressComponent } from '../edit-competence-progress/edit-competence-progress.component';
//import { CompetenceListComponentMock } from './competence-list.mock';

@Directive({
  selector: "[color]"
})

@NgModule({
  declarations: [AlertBoxComponent, CompetenceAlertComponent],
  entryComponents: [AlertBoxComponent, CompetenceAlertComponent]
})
class TestModule {}

class MockAuthService extends AuthService{ 
   userRole = 'LM';
   loggedInSignum = 'EKUYOGE';
   loggedInEmpName = 'Yogesh';
   getAuthData(){
    return {
      userRole: 'LM',
        loggedInSignum: 'EKUYOGE',
        empName:'EKUYOGE'
      }
   }
    }  

  class MockMyCompetenceService extends MyCompetenceService{
    constructor(){
      super(null);
    }
     dataForComp = {"requestedStatus":["Approved"],
                    "loggedInSignum":"EKUYOGE",
                    "requestedBySignum":"EBMHUAK",
                    "isProgress":"true"
                  }
  getSavedCompetence(dataForComp){
    console.log('sending fake answers!');
    return Observable.of([
      {
        Active: true, BaselineName: "A", CompetenceID: null, CompetenceType: "Domain Competence",
        CompetencyUpgrade: "A to B", Competency_Service_Area: "NDS(Design & Tuning)", DomainSubDomain: "Radio Access Networks / WCDMA RAN",
        FileUploadedOn: 1569555648373, ILT_TOTAL_HRS: null, Technology: "WCDMA", Vendor: "Huawei", WBL_TOTAL_HRS: null,
        baseline: 2, changedBy: "LM", competanceUpgradeLevel: "nextLevel", competenceUpgradeID: 2, createdBy: "ebmhuak",
        createdon: 1570599980343, deliveryCompetanceID: 3957, lmSignum: "EKUYOGE", loggedInSignum: "ebmhuak",
        parentSystemID: 270, requestedBySignum: "ebmhuak",  rowversion: "AAAAAAADF+A=", slmSignum: "EDBEVS",
        status: "Approved", systemID: 731, userCompetanceID: null
      }]);
  }
}
@Injectable()
   class MockAuthInterceptor implements HttpInterceptor{
    constructor(){}
    intercept(req: HttpRequest<any>,next : HttpHandler):Observable<HttpEvent<any>>{
      const storedData = {'userRole': 'LM', 'loggedInSignum': 'EKUYOGE'};
      
    const copiedReq= req.clone(
      // {headers:req.headers.append('role','admin').append('signum','ejangua')}
      {headers:req.headers.append('role',storedData.userRole).append('signum',storedData.loggedInSignum)}
      );
  return next.handle(copiedReq);
    }
   }


fdescribe('CompetenceProgressLMComponent', () => {
  let component: CompetenceProgressLMComponent;
  let fixture: ComponentFixture<CompetenceProgressLMComponent>;

  let editComponent: EditCompetenceProgressComponent;
  let editComponentFixture : ComponentFixture<EditCompetenceProgressComponent>;
  
  let de: DebugElement;
  let el: HTMLElement;
  let competenceService: MockMyCompetenceService;
  let authService:MockAuthService;
  let alertBoxService:AlertBoxService;
  let dataForComp; 
  let engineerList:any;
  const mockDialogRef = {
    close: jasmine.createSpy('close')
  };
  

  beforeEach(async(() => {
    
    TestBed.configureTestingModule({
      declarations: [ CompetenceProgressLMComponent, EditCompetenceProgressComponent, AlertBoxComponent, CompetenceAlertComponent ],
      imports: [
        BrowserModule, FormsModule, ReactiveFormsModule, MatOptionModule, MatSelectModule,
        MatCardModule, MatGridListModule, MatTableModule, MatIconModule, HttpClientModule,
        BrowserAnimationsModule, MatInputModule, RouterModule, RouterTestingModule, MatDialogModule, 
        MatCheckboxModule, MatPaginatorModule,MatButtonModule, MatDialogModule
    ],    
    providers:[AlertBoxService, MockAuthService, MockMyCompetenceService, {provide: HTTP_INTERCEPTORS,
      useClass: MockAuthInterceptor, 
      multi: true},  { provide: MAT_DIALOG_DATA, useValue: {} }, 
      { provide: MatDialogRef, useValue:mockDialogRef },]
      
    })
    .overrideModule(BrowserDynamicTestingModule, { set: { entryComponents: [AlertBoxComponent, CompetenceAlertComponent, EditCompetenceProgressComponent] } })

    .compileComponents().then(() => {
        fixture = TestBed.createComponent(CompetenceProgressLMComponent);
        component = fixture.componentInstance;
        de = fixture.debugElement.query(By.css('table'));
        el = de.nativeElement;        
        competenceService = fixture.debugElement.injector.get(MockMyCompetenceService);
        authService = fixture.debugElement.injector.get(MockAuthService);
       
        fixture.detectChanges();
        component.loggedInSignum = 'EKOMKHA';
    });    
    
  }));  

  fit('call applyfilter() method', async() => {
    component.applyFilter('d');
  expect(component.applyFilter).toBeTruthy();
  })
  
  fit('call getSavedCompetetence method', async() => {
    await authService.getAuthData();
    fixture.detectChanges();
    component.loggedInSignum='EKUYOGE';
    component.selectedSignum="EBMHUAK";
   let dataForComp = {
    "requestedStatus":["Approved"],
    "loggedInSignum":authService.loggedInSignum,
    "requestedBySignum":"EBMHUAK",
    "isProgress":"true"
 }
      component.updateTable();
      expect(component.updateTable).toBeTruthy();
  });

  fit('call editRecord method', async() => {
    let row = {Active: true, BaselineName: "A", CompetenceID: null, CompetenceType: "Domain Competence",CompetencyUpgrade: "A to B",
    Competency_Service_Area: "NDS(Design & Tuning)", DomainSubDomain: "Radio Access Networks / WCDMA RAN",
    FileUploadedOn: 1569555648373, ILT_TOTAL_HRS: null, Technology: "WCDMA", Vendor: "Huawei", WBL_TOTAL_HRS: null,
    baseline: 2, changedBy: "LM", competanceUpgradeLevel: "nextLevel",competenceUpgradeID: 2, createdBy: "ebmhuak",
    createdon: 1570599980343, deliveryCompetanceID: 3957, lmSignum: "EKUYOGE", loggedInSignum: "ebmhuak",
    parentSystemID: 270, requestedBySignum: "ebmhuak", rowversion: "AAAAAAADF+A=", slmSignum: "EDBEVS",
    status: "Approved", systemID: 731, userCompetanceID: null};

    component.editRecord(row);
    fixture.detectChanges();
    editComponentFixture = TestBed.createComponent(EditCompetenceProgressComponent);
    editComponent = editComponentFixture.componentInstance;
    let button = editComponentFixture.nativeElement.parentElement.querySelector('#editCancel');
    button.click();
    editComponentFixture.detectChanges();
    expect(component.editRecord).toBeTruthy();
  });
});
