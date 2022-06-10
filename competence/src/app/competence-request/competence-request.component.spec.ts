import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { CompetenceRequestComponent } from './competence-request.component';
import { DebugElement, NgModule, Directive, Injectable } from '@angular/core';
import { BrowserModule, By } from '@angular/platform-browser';
import { FormsModule, ReactiveFormsModule, FormControlDirective, FormGroupDirective } from '@angular/forms';
import { BrowserDynamicTestingModule, platformBrowserDynamicTesting } from '@angular/platform-browser-dynamic/testing';
import { MatOptionModule, MatSelectModule, MatCardModule, MatGridListModule, MatTableDataSource, MatTableModule, MatIconModule, MatButtonToggleModule, MatInputModule, MatSlideToggleModule, MatDialogModule, MatSlideToggle, MatSelect, MAT_DIALOG_DATA, MatDialogRef, MatButtonModule, MatButton, MatDialog, MatCheckboxModule, MatPaginatorModule, MatTab, MatTabsModule } from '@angular/material';
import { HttpClientModule, HTTP_INTERCEPTORS, HttpInterceptor, HttpRequest, HttpHandler, HttpEvent } from '@angular/common/http';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { RouterModule } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { CompetenceRequestService } from './competence-request.service';
import { AlertBoxComponent } from 'src/app/shared/alert-box/alert-box.component';
import { AlertBoxService } from 'src/app/shared/alert-box.service';

import { CompetenceAlertComponent } from 'src/app/shared/competence-alert/competence-alert.component';
import { CompetenceProgressLMComponent } from './competence-progress-lm/competence-progress-lm.component';
//import { AuthService } from 'src/app/auth/auth.service';
import { Observable, of } from 'rxjs';
import { EngineerRequestComponent } from './engineer-request/engineer-request.component';
import { RefreshPageComponent } from '../refresh-page/refresh-page.component';
//import { $ } from 'protractor';

@Directive({
  selector: "[color]"
})
@NgModule({
  declarations: [AlertBoxComponent, CompetenceAlertComponent,EngineerRequestComponent,RefreshPageComponent],
  entryComponents: [AlertBoxComponent, CompetenceAlertComponent,EngineerRequestComponent]
})
class TestModule {}

// class MockAuthService extends AuthService{ 
//   userRole = 'LM';
//   loggedInSignum = 'EKUYOGE';
//   loggedInEmpName = 'Yogesh';
//   getAuthData(){
//    return {
//      userRole: 'LM',
//        loggedInSignum: 'EKUYOGE',
//        empName:'EKUYOGE'
//      }
//   }
//    }    
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

fdescribe('LMViewRequestCompetence', () => {

  let component: CompetenceRequestComponent;
  let competencelistcomp: CompetenceProgressLMComponent;
  let fixture: ComponentFixture<CompetenceRequestComponent>;
  let competenceListFixture: ComponentFixture<CompetenceProgressLMComponent>;
  let httpClientSpy: { get: jasmine.Spy , post: jasmine.Spy};
  let de:DebugElement;
  let el: HTMLElement;
  let competenceService: CompetenceRequestService;  
//   let alertboxService : AlertBoxService;
//   let mockAuthService : MockAuthService;
//   let mockInterceptor : MockAuthInterceptor;
  const mockDialogRef = {
    close: jasmine.createSpy('close')
  };
  

  //create an angular environment for the component being tested.
  beforeEach(async(() => {
    TestBed.resetTestEnvironment();
    TestBed.initTestEnvironment(BrowserDynamicTestingModule,
       platformBrowserDynamicTesting());
    TestBed.configureTestingModule({
        
      declarations: [ CompetenceRequestComponent, AlertBoxComponent, CompetenceAlertComponent, CompetenceProgressLMComponent,EngineerRequestComponent,RefreshPageComponent], 
      imports: [
          BrowserModule, FormsModule, ReactiveFormsModule, MatOptionModule, MatSelectModule,
          MatCardModule, MatGridListModule, MatTableModule, MatIconModule, HttpClientModule,MatCheckboxModule,
          BrowserAnimationsModule, MatButtonToggleModule, MatInputModule, MatSlideToggleModule,MatPaginatorModule,
          RouterModule, RouterTestingModule, MatDialogModule, BrowserDynamicTestingModule, MatButtonModule,MatTabsModule
        ],
      providers: [AlertBoxService,
        { provide: MAT_DIALOG_DATA, useValue: {} }, 
        { provide: MatDialogRef, useValue:mockDialogRef },
        {provide: HTTP_INTERCEPTORS,
          useClass: MockAuthInterceptor,
          //useValue:{'role':'user', 'signum':'EKOMKHA'},
          multi: true}]
    
    })
    .overrideModule(BrowserDynamicTestingModule, { set: { entryComponents: [AlertBoxComponent, CompetenceAlertComponent,EngineerRequestComponent] } })
    .compileComponents()
    // .then(() => {
    //     fixture = TestBed.createComponent(CompetenceRequestComponent);
    //     fixture.detectChanges();
    //     component = fixture.componentInstance;
       
    //     de = fixture.debugElement.query(By.css('form'));
    //     el = de.nativeElement;
    //     competenceService = fixture.debugElement.injector.get(CompetenceRequestService);
    //     // alertboxService = fixture.debugElement.injector.get(AlertBoxService);
    //     // mockAuthService = fixture.debugElement.injector.get(MockAuthService);
    //     // competenceListFixture = TestBed.createComponent(CompetenceListComponent);
    //     // competencelistcomp = competenceListFixture.componentInstance;
    // })
    httpClientSpy = jasmine.createSpyObj('HttpClient', ['get', 'post']);
    competenceService = new CompetenceRequestService(<any> httpClientSpy);    
   
  }));

  beforeEach(() => {
   fixture = TestBed.createComponent(CompetenceRequestComponent);
    component = fixture.componentInstance;    
    fixture.detectChanges();
   
  });

  //The second test expects the property of the component “submitted” to be true when the “onSubmit” function is called.
  it('should create', () => {
    expect(component).toBeTruthy();
  });

  fit('should return expected Competence (HttpClient called once)', () => {

    const expectedCompetence: any[] = [{CompetenceType: "Domain Competence(CDP)", CompetenceTypeID: 1}];
  
      httpClientSpy.get.and.returnValue(of(expectedCompetence));
      
      competenceService.getCompetence().subscribe(
        response => expect(response).toEqual(expectedCompetence, 'expected competence'),
        fail
      );
      expect(httpClientSpy.get.calls.count()).toBe(1, 'one call');
    });
  
    fit('should return expected baselines (HttpClient called once)', () => { 
  
    const expectedBaseline: any[] = [{CompetenceGRADEID: 1, GradeName: "T", GradeWeitage: 1}];
  
      httpClientSpy.get.and.returnValue(of(expectedBaseline));
     
      competenceService.getBaseline().subscribe(
        response => expect(response).toEqual(expectedBaseline, 'expected baselines'),
        fail
      );
      expect(httpClientSpy.get.calls.count()).toBe(1, 'one call');
    });
  
    fit('should return expected Domain (HttpClient called once)', () => { 
  
      const expectedDomain: any[] = [{Domain: "IP & Core / Mobile Switching",subdomainID: 2}, {Domain: "Transport / IP",
      subdomainID: 7},{Domain: "Radio Access Networks / GSM RAN", subdomainID: 12}];
    
        httpClientSpy.get.and.returnValue(of(expectedDomain));        
     
        competenceService.getDomain('1').subscribe(
          response => expect(response).toEqual(expectedDomain, 'expected domain'),
          fail
        );
        expect(httpClientSpy.get.calls.count()).toBe(1, 'one call');
      });
  
    fit('should return expected Technology (HttpClient called once)', () => {
      const exptectedTech: any[] = [{Technology: "Wireline Switching", TechnologyID: 6},
            {Technology: "WCDMA", TechnologyID: 12}];
    
      httpClientSpy.get.and.returnValue(of(exptectedTech));
      
      competenceService.getTechnology("1","7").subscribe(
          response => expect(response).toEqual(exptectedTech, 'expected Technology'),
          fail
        );
      expect(httpClientSpy.get.calls.count()).toBe(1, 'one call');
      });
  
    fit('should return expected Vendor (HttpClient called once)', () => {
        const exptectedVendor: any[] = [{VendorID: 6, Vendor: "Juniper"},
        {VendorID: 8, Vendor: "Extreme"},{VendorID: 9, Vendor: "ZTE"}];
      
        httpClientSpy.get.and.returnValue(of(exptectedVendor));
        
        competenceService.getVendor("1","25","12").subscribe(
            response => expect(response).toEqual(exptectedVendor, 'expected vendor'),
            fail
          );
        expect(httpClientSpy.get.calls.count()).toBe(1, 'one call');
    });
  
    fit('should return expected ServiceArea (HttpClient called once)', () => {
      const exptectedSerArea: any[] = [{Competency_Service_Area: "Assertion", CompetenceID: 147},
      {Competency_Service_Area: "Segregation", CompetenceID: 150}];
    
      httpClientSpy.get.and.returnValue(of(exptectedSerArea));
      
      competenceService.getServiceArea("1","25","12","8").subscribe(
          response => expect(response).toEqual(exptectedSerArea, 'expected servicearea'),
          fail
        );
      expect(httpClientSpy.get.calls.count()).toBe(1, 'one call');
  });
  
  fit('should return expected Ambition (HttpClient called once)', () => {
    const exptectedAmbition: any[] = [{CompetenceUpgradeID: 6, CompetencyUpgrade: "A to A", Description: "same"}];
  
    httpClientSpy.get.and.returnValue(of(exptectedAmbition));
    
    competenceService.getAmbition("same","2").subscribe(
        response => expect(response).toEqual(exptectedAmbition, 'expected ambition'),
        fail
      );
    expect(httpClientSpy.get.calls.count()).toBe(1, 'one call');
  });
  
  fit('getUserCompetenceRow should return data', () => {
    const expectedResult = {ambition: null, baseline: 2, changedBy: "engineer", competanceID: 149,
      competanceUpgradeLevel: "same", competenceGrade: null, competenceGradeID: 2, competenceTypeID: 1,
      competenceUpgrade: null, competenceUpgradeID: 6, competencyServiceArea: null, competencyUpgrade: "A to A", createdBy: "EKOMKHA", createdon: 1568025427590, deliveryCompetanceID: 2170,
      domain: null, domainID: 12, isEditable: false, lmSignum: "EKUYOGE", loggedInSignum: "EKOMKHA",
      parentSystemID: 0, requestedBySignum: "EKOMKHA", requestedStatus: null, slmSignum: "EDBEVS",
      status: "Initiated", systemID: 33, technology: null, technologyID: 10, userCompetanceID: null,
      vendor: null, vendorID: 5};
      let a = "AVC";
    httpClientSpy.get.and.returnValue(of(expectedResult));
  
    competenceService.getCompValuesFromApi('33').subscribe(
      response => expect(response).toEqual(expectedResult),
      fail
    );
    expect(httpClientSpy.get.calls.count()).toBe(1, 'one call')
  
  });
  

  fit('getLMViewRow should return data', () => {
      const loggedInSignum = "EKUYOGE"
    const dataForCompAll = {
        "loggedInSignum": loggedInSignum,
      }
    const expectedResult = {};
      //let a = "AVC";s
    httpClientSpy.post.and.returnValue(of(expectedResult));
  
    competenceService.getCompetenceForApproval(dataForCompAll).toPromise().then(
        response => expect(response).toEqual(expectedResult),
         fail
    );

    expect(httpClientSpy.post.calls.count()).toBe(1, 'one call')
  
  });

  fit('getLMViewRow should return null/undefined data', () => {
    const loggedInSignum = "ESUOBAN"
  const dataForCompAll = {
      "loggedInSignum": loggedInSignum,
    }
  const expectedResult = {signum:"Abhinav Kumar(ebmhuak)",domain:1,niche:1,tool:0,pendingapproval:1,ambition:2};
    //let a = "AVC";s
  httpClientSpy.post.and.returnValue(of(expectedResult));

  competenceService.getCompetenceForApproval(dataForCompAll).toPromise().then(
      response => expect(response).toEqual(expectedResult),
       fail
  );

  expect(httpClientSpy.post.calls.count()).toBe(1, 'one call')

});
fit('applyFilter method is called', () => {
    const filterValue = "1";
    component.applyFilter(filterValue);
    expect(component.applyFilter).toBeTruthy();
  });
fit('should open the dialog for the user',async ()=>{
    //let dialogFixture: ComponentFixture<EngineerRequestComponent>;
    const row_obj = {signum:"Abhinav Kumar(ebmhuak)",domain:1,niche:1,tool:0,pendingapproval:1,ambition:2};
    component.openDialog(row_obj);
    //fixture.detectChanges();
    //dialogFixture.detectChanges();
    const dialogYesButton = fixture.nativeElement.parentElement.querySelector('#closeBtn');
    dialogYesButton.click();
    expect(component.openDialog).toBeTruthy();
    
});
});
