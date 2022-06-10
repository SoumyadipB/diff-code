import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { EngineerRequestComponent } from './engineer-request.component';
import { DebugElement, NgModule, Directive, Injectable } from '@angular/core';
import { BrowserModule, By } from '@angular/platform-browser';
import { FormsModule, ReactiveFormsModule, FormControlDirective, FormGroupDirective } from '@angular/forms';
import { BrowserDynamicTestingModule, platformBrowserDynamicTesting } from '@angular/platform-browser-dynamic/testing';
import { MatOptionModule, MatSelectModule, MatCardModule, MatGridListModule, MatTableDataSource, MatTableModule, MatIconModule, MatButtonToggleModule, MatInputModule, MatSlideToggleModule, MatDialogModule, MatSlideToggle, MatSelect, MAT_DIALOG_DATA, MatDialogRef, MatButtonModule, MatButton, MatDialog, MatCheckboxModule, MatPaginatorModule, MatTabsModule } from '@angular/material';
import { HttpClientModule, HTTP_INTERCEPTORS, HttpInterceptor, HttpRequest, HttpHandler, HttpEvent } from '@angular/common/http';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { RouterModule } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { CompetenceRequestService } from '../competence-request.service';
import { AlertBoxComponent } from 'src/app/shared/alert-box/alert-box.component';
import { AlertBoxService } from 'src/app/shared/alert-box.service';

import { CompetenceAlertComponent } from 'src/app/shared/competence-alert/competence-alert.component';
//import { CompetenceListComponent } from '../competence-list/competence-list.component';
import { AuthService } from 'src/app/auth/auth.service';
import { Observable, of } from 'rxjs';
import { $ } from 'protractor';
import { CompetenceProgressLMComponent } from '../competence-progress-lm/competence-progress-lm.component';

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
     userRole: 'User',
       loggedInSignum: 'EKUYOGE',
       empName:'EKUYOGE'
     }
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

fdescribe('LMEnggCompetenceComponent', () => {

  let component: EngineerRequestComponent;
  //let competencelistcomp: CompetenceListComponent;
  let fixture: ComponentFixture<EngineerRequestComponent>;
  //let competenceListFixture: ComponentFixture<CompetenceListComponent>;
  let httpClientSpy: { get: jasmine.Spy , post: jasmine.Spy};
  let de:DebugElement;
  let el: HTMLElement;
  let competenceService: CompetenceRequestService;  
  let alertboxService : AlertBoxService;
  let mockAuthService : MockAuthService;
  let mockInterceptor : MockAuthInterceptor;
  const mockDialogRef = {
    close: jasmine.createSpy('close')
  };
  

  //create an angular environment for the component being tested.
  beforeEach(async(() => {
    TestBed.resetTestEnvironment();
    TestBed.initTestEnvironment(BrowserDynamicTestingModule,
       platformBrowserDynamicTesting());
    TestBed.configureTestingModule({
        
      declarations: [ EngineerRequestComponent, AlertBoxComponent, CompetenceAlertComponent, CompetenceProgressLMComponent], 
      imports: [
          BrowserModule, FormsModule, ReactiveFormsModule, MatOptionModule, MatSelectModule,
          MatCardModule, MatGridListModule, MatTableModule, MatIconModule, HttpClientModule,MatCheckboxModule,
          BrowserAnimationsModule, MatButtonToggleModule, MatInputModule, MatSlideToggleModule,MatPaginatorModule,
          RouterModule, RouterTestingModule, MatDialogModule, BrowserDynamicTestingModule, MatButtonModule,MatTabsModule,MatCheckboxModule
        ],
      providers: [AlertBoxService, MockAuthService,
        { provide: MAT_DIALOG_DATA, useValue: {} }, 
        { provide: MatDialogRef, useValue:mockDialogRef },
        {provide: HTTP_INTERCEPTORS,
          useClass: MockAuthInterceptor,
          //useValue:{'role':'user', 'signum':'EKOMKHA'},
          multi: true}]
    
    })
    .overrideModule(BrowserDynamicTestingModule, { set: { entryComponents: [AlertBoxComponent, CompetenceAlertComponent] } })
    .compileComponents()
    // .then(() => {
    //     fixture = TestBed.createComponent(EngineerRequestComponent);
    //     fixture.detectChanges();
    //     component = fixture.componentInstance;
       
    //     de = fixture.debugElement.query(By.css('form'));
    //     el = de.nativeElement;
    //     competenceService = fixture.debugElement.injector.get(CompetenceRequestService);
    //     alertboxService = fixture.debugElement.injector.get(AlertBoxService);
    //     mockAuthService = fixture.debugElement.injector.get(MockAuthService);
    //     // competenceListFixture = TestBed.createComponent(CompetenceListComponent);
    //     // competencelistcomp = competenceListFixture.componentInstance;
    // })
    
    httpClientSpy = jasmine.createSpyObj('HttpClient', ['get', 'post']);
    competenceService = new CompetenceRequestService(<any> httpClientSpy);    
  }));

  beforeEach(() => {
   // fixture = TestBed.createComponent(AddCompetenceComponent);
    //component = fixture.componentInstance;    
    //fixture.detectChanges();
   
  });

  //The second test expects the property of the component “submitted” to be true when the “onSubmit” function is called.
  it('should create', () => {
    expect(component).toBeTruthy();
  });

  fit('tabClick method is called', async() => {
    const tab = { index:1 };
    component.tabClick(tab);
    expect(component.tabClick).toBeTruthy();
  
  })

  fit('getEnggViewRow should return data', () => {
    const loggedInSignum = "EKUYOGE"
  const dataForCompAll = {
    "requestedStatus": ["Sent To Manager","Approved","Rejected"],
    "loggedInSignum": loggedInSignum,
    "requestedBySignum": "EBMHUAK" ,
    "isProgress":"false"
    }
  const expectedResult = {};
    //let a = "AVC";s
  httpClientSpy.post.and.returnValue(of(expectedResult));

  competenceService.getSavedCompetence(dataForCompAll).toPromise().then(
      response => expect(response).toEqual(expectedResult),
       fail
  );

  expect(httpClientSpy.post.calls.count()).toBe(1, 'one call')

});
fit('delete selected Competence check', async() => {
  //  let data = [{"loggedInSignum":"EKOMKHA","requestedBySignum":"EKOMKHA","vendorID":5,"competanceID":197,"competenceUpgradeID":7,"competenceGradeID":3,"status":"Initiated","changedBy":"engineer","createdBy":"EKOMKHA","technologyID":11,"domainID":13,"isEditable":true}];
    //let dataComp = {"requestedStatus":["Initiated"],"loggedInSignum":"EKOMKHA","requestedBySignum":"EKOMKHA","isProgress":"false"};
    let response:any;
 //   response = await competenceService.addCompetence(data).toPromise();
 //   fixture.detectChanges();
 //   expect(response.isValidationFailed).toEqual(false);
    
    await fixture.whenStable().then(async ()=>{
      await mockAuthService.getAuthData();
      fixture.detectChanges();
      let dataForComp= {
        "requestedStatus": ["Sent To Manager","Approved","Rejected"],
        "loggedInSignum": "EKUYOGE",
        "requestedBySignum": "EBMHUAK",
        "isProgress":"false"
        // "loggedInSignum": "EBMHUAK",
        // "requestedBySignum": "EBMHUAK"
      }
      
      //component.loggedInSignum = 'EKOMKHA';
      fixture.detectChanges();
      //populate Data in table
      //await component.updateTable();
      response = await competenceService.getSavedCompetence(dataForComp).toPromise();
      fixture.detectChanges();
      
      expect(response.length).toBeGreaterThanOrEqual(0);
      
      //Pick 1st row of table and click edit button to get systemID
      const tableRows = fixture.nativeElement.querySelectorAll('tr');      
      let row = tableRows[1];
      let checkbox =  row.cells[0].querySelector('mat-checkbox label');
      checkbox.click();
      //detect changes after checkbox click in form as well as table.
      fixture.detectChanges();

      let commonDeleteButton = fixture.nativeElement.parentElement.querySelector('#deleteBtn');
      commonDeleteButton.click();
      fixture.detectChanges();
    });
    await fixture.whenStable().then(async() => {
      let dialogYesButton = fixture.nativeElement.parentElement.querySelector('.mat-dialog-actions .mat-raised-button');
      dialogYesButton.click();
      fixture.detectChanges();
    })
     
    //response = await competenceService.getSavedCompetence(dataComp).toPromise();
    //fixture.detectChanges();
   
  })
 

  fit('isAllSelected methods is called', () => {      
    component.isAllSelected();
    fixture.detectChanges();
    //component.isAllSelected2();
    //fixture.detectChanges();
    expect(component.isAllSelected).toBeTruthy();
    //expect(component.isAllSelected2).toBeTruthy();
  });

  fit('masterToggle methods called', () => {
    const columncheckbox = de.nativeElement.querySelector('.selectAllCheckbox');
    columncheckbox.click();
    fixture.detectChanges();
    
    component.masterToggle();
    //component.selectRows.clear();
    fixture.detectChanges();

    //component.masterToggle2();
    //component.selection2.clear();
    //fixture.detectChanges();
    expect(component.masterToggle).toBeTruthy();
  });
  fit('approve selected Competence check', async() => {
    //  let data = [{"loggedInSignum":"EKOMKHA","requestedBySignum":"EKOMKHA","vendorID":5,"competanceID":197,"competenceUpgradeID":7,"competenceGradeID":3,"status":"Initiated","changedBy":"engineer","createdBy":"EKOMKHA","technologyID":11,"domainID":13,"isEditable":true}];
      //let dataComp = {"requestedStatus":["Initiated"],"loggedInSignum":"EKOMKHA","requestedBySignum":"EKOMKHA","isProgress":"false"};
      let response:any;
   //   response = await competenceService.addCompetence(data).toPromise();
   //   fixture.detectChanges();
   //   expect(response.isValidationFailed).toEqual(false);
      
      await fixture.whenStable().then(async ()=>{
        await mockAuthService.getAuthData();
        fixture.detectChanges();
        let dataForComp= {
          "requestedStatus": ["Sent To Manager"],
          "loggedInSignum": "EKUYOGE",
          "requestedBySignum": "EBMHUAK",
          "isProgress":"false"
          // "loggedInSignum": "EBMHUAK",
          // "requestedBySignum": "EBMHUAK"
        }
        
        //component.loggedInSignum = 'EKOMKHA';
        fixture.detectChanges();
        //populate Data in table
        //await component.updateTable();
        response = await competenceService.getSavedCompetence(dataForComp).toPromise();
        fixture.detectChanges();
        
        expect(response.length).toBeGreaterThanOrEqual(0);
        
        //Pick 1st row of table and click edit button to get systemID
        const tableRows = fixture.nativeElement.querySelectorAll('tr');      
        let row = tableRows[1];
        let checkbox =  row.cells[0].querySelector('mat-checkbox label');
        checkbox.click();
        //detect changes after checkbox click in form as well as table.
        fixture.detectChanges();
  
        let commonAcceptButton = fixture.nativeElement.parentElement.querySelector('#approveBtn');
        commonAcceptButton.click();
        fixture.detectChanges();
      });
      await fixture.whenStable().then(async() => {
        let dialogYesButton = fixture.nativeElement.parentElement.querySelector('.mat-dialog-actions .mat-raised-button');
        dialogYesButton.click();
        fixture.detectChanges();
      })
       
      //response = await competenceService.getSavedCompetence(dataComp).toPromise();
      //fixture.detectChanges();
     
    })
    fit('reject selected Competence check', async() => {
      //  let data = [{"loggedInSignum":"EKOMKHA","requestedBySignum":"EKOMKHA","vendorID":5,"competanceID":197,"competenceUpgradeID":7,"competenceGradeID":3,"status":"Initiated","changedBy":"engineer","createdBy":"EKOMKHA","technologyID":11,"domainID":13,"isEditable":true}];
        //let dataComp = {"requestedStatus":["Initiated"],"loggedInSignum":"EKOMKHA","requestedBySignum":"EKOMKHA","isProgress":"false"};
        let response:any;
     //   response = await competenceService.addCompetence(data).toPromise();
     //   fixture.detectChanges();
     //   expect(response.isValidationFailed).toEqual(false);
        
        await fixture.whenStable().then(async ()=>{
          await mockAuthService.getAuthData();
          fixture.detectChanges();
          let dataForComp= {
            "requestedStatus": ["Sent To Manager"],
            "loggedInSignum": "EKUYOGE",
            "requestedBySignum": "EBMHUAK",
            "isProgress":"false"
            // "loggedInSignum": "EBMHUAK",
            // "requestedBySignum": "EBMHUAK"
          }
          
          //component.loggedInSignum = 'EKOMKHA';
          fixture.detectChanges();
          //populate Data in table
          //await component.updateTable();
          response = await competenceService.getSavedCompetence(dataForComp).toPromise();
          fixture.detectChanges();
          
          expect(response.length).toBeGreaterThanOrEqual(0);
          
          //Pick 1st row of table and click edit button to get systemID
          const tableRows = fixture.nativeElement.querySelectorAll('tr');      
          let row = tableRows[1];
          let checkbox =  row.cells[0].querySelector('mat-checkbox label');
          checkbox.click();
          //detect changes after checkbox click in form as well as table.
          fixture.detectChanges();
    
          let commonRejectButton = fixture.nativeElement.parentElement.querySelector('#rejectBtn');
          commonRejectButton.click();
          fixture.detectChanges();
        });
        await fixture.whenStable().then(async() => {
          let dialogYesButton = fixture.nativeElement.parentElement.querySelector('.mat-dialog-actions .mat-raised-button');
          dialogYesButton.click();
          fixture.detectChanges();
        })
         
        //response = await competenceService.getSavedCompetence(dataComp).toPromise();
        //fixture.detectChanges();
       
      })

});