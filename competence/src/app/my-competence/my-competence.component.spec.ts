import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { MyCompetenceComponent } from './my-competence.component';
import { MyCompetenceService } from './my-cometence.service';
import { PeriodicElement } from '../competence-request/engineer-request/engineer-request.component';
import { CompetenceListComponent } from './competence-list/competence-list.component';
import { AddCompetenceComponent } from './add-competence/add-competence.component';
import { MatTabsModule, MatOptionModule, MatSelectModule, MatCardModule, MatGridListModule, MatTableModule, MatIconModule, MatInputModule, MatDialogModule, MatCheckboxModule, MatPaginatorModule, MatSlideToggleModule } from '@angular/material';
import { CompetenceProgressComponent } from './competence-progress/competence-progress.component';
import { BrowserModule } from '@angular/platform-browser';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { HttpClientModule, HttpErrorResponse, HTTP_INTERCEPTORS, HttpInterceptor, HttpRequest, HttpHandler, HttpEvent } from '@angular/common/http';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { RouterModule } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Observable } from 'rxjs';
import { HttpTestingController } from '@angular/common/http/testing';
import { AuthInterceptor } from '../shared/auth.interceptor';
import { Injectable } from '@angular/core';

@Injectable()
class MockAuthInterceptor implements HttpInterceptor{
 constructor(){}
 intercept(req: HttpRequest<any>,next : HttpHandler):Observable<HttpEvent<any>>{
   const storedData = {'userRole': 'User', 'loggedInSignum': 'EKOMKHA'};
   
 const copiedReq= req.clone(
   // {headers:req.headers.append('role','admin').append('signum','ejangua')}
   {headers:req.headers.append('role',storedData.userRole).append('signum',storedData.loggedInSignum)}
   );
return next.handle(copiedReq);
 }
}


fdescribe('MyCompetenceComponent', () => {
  let component: MyCompetenceComponent;
  let fixture: ComponentFixture<MyCompetenceComponent>;
  let httpClientSpy: { get: jasmine.Spy , post: jasmine.Spy};
  let myCompetenceService: MyCompetenceService; 
  let competenceListFixture: ComponentFixture<CompetenceListComponent>;
  let competencelistcomp: CompetenceListComponent;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ MyCompetenceComponent, CompetenceListComponent, AddCompetenceComponent, CompetenceProgressComponent ],
      imports: [ BrowserModule, FormsModule, ReactiveFormsModule, MatOptionModule, MatSelectModule,
        MatCardModule, MatGridListModule, MatTableModule, MatIconModule, HttpClientModule,
        BrowserAnimationsModule, MatInputModule, MatTabsModule, MatSlideToggleModule,
        RouterModule, RouterTestingModule, MatDialogModule, MatCheckboxModule, MatPaginatorModule],
      providers: [MyCompetenceService, {provide: HTTP_INTERCEPTORS,
        useClass: MockAuthInterceptor, multi: true}]
    })
    .compileComponents();
    httpClientSpy = jasmine.createSpyObj('HttpClient', ['get', 'post']);
    myCompetenceService = new MyCompetenceService(<any> httpClientSpy);    
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(MyCompetenceComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  fit('should return expected Competence (HttpClient called once)', () => {

  const expectedCompetence: any[] = [{CompetenceType: "Domain Competence(CDP)", CompetenceTypeID: 1}];

    httpClientSpy.get.and.returnValue(of(expectedCompetence));
    
    myCompetenceService.getCompetence().subscribe(
      response => expect(response).toEqual(expectedCompetence, 'expected competence'),
      fail
    );
    expect(httpClientSpy.get.calls.count()).toBe(1, 'one call');
  });

  fit('should return expected baselines (HttpClient called once)', () => { 

  const expectedBaseline: any[] = [{CompetenceGRADEID: 1, GradeName: "T", GradeWeitage: 1}];

    httpClientSpy.get.and.returnValue(of(expectedBaseline));
   
    myCompetenceService.getBaseline().subscribe(
      response => expect(response).toEqual(expectedBaseline, 'expected baselines'),
      fail
    );
    expect(httpClientSpy.get.calls.count()).toBe(1, 'one call');
  });

  fit('should return expected Domain (HttpClient called once)', () => { 

    const expectedDomain: any[] = [{Domain: "IP & Core / Mobile Switching",subdomainID: 2}, {Domain: "Transport / IP",
    subdomainID: 7},{Domain: "Radio Access Networks / GSM RAN", subdomainID: 12}];
  
      httpClientSpy.get.and.returnValue(of(expectedDomain));        
   
      myCompetenceService.getDomain('1').subscribe(
        response => expect(response).toEqual(expectedDomain, 'expected domain'),
        fail
      );
      expect(httpClientSpy.get.calls.count()).toBe(1, 'one call');
    });

  fit('should return expected Technology (HttpClient called once)', () => {
    const exptectedTech: any[] = [{Technology: "Wireline Switching", TechnologyID: 6},
          {Technology: "WCDMA", TechnologyID: 12}];
  
    httpClientSpy.get.and.returnValue(of(exptectedTech));
    
    myCompetenceService.getTechnology("1","7").subscribe(
        response => expect(response).toEqual(exptectedTech, 'expected Technology'),
        fail
      );
    expect(httpClientSpy.get.calls.count()).toBe(1, 'one call');
    });

  fit('should return expected Vendor (HttpClient called once)', () => {
      const exptectedVendor: any[] = [{VendorID: 6, Vendor: "Juniper"},
      {VendorID: 8, Vendor: "Extreme"},{VendorID: 9, Vendor: "ZTE"}];
    
      httpClientSpy.get.and.returnValue(of(exptectedVendor));
      
      myCompetenceService.getVendor("1","25","12").subscribe(
          response => expect(response).toEqual(exptectedVendor, 'expected vendor'),
          fail
        );
      expect(httpClientSpy.get.calls.count()).toBe(1, 'one call');
  });

  fit('should return expected ServiceArea (HttpClient called once)', () => {
    const exptectedSerArea: any[] = [{Competency_Service_Area: "Assertion", CompetenceID: 147},
    {Competency_Service_Area: "Segregation", CompetenceID: 150}];
  
    httpClientSpy.get.and.returnValue(of(exptectedSerArea));
    
    myCompetenceService.getServiceArea("1","25","12","8").subscribe(
        response => expect(response).toEqual(exptectedSerArea, 'expected servicearea'),
        fail
      );
    expect(httpClientSpy.get.calls.count()).toBe(1, 'one call');
});

fit('should return expected Ambition (HttpClient called once)', () => {
  const exptectedAmbition: any[] = [{CompetenceUpgradeID: 6, CompetencyUpgrade: "A to A", Description: "same"}];

  httpClientSpy.get.and.returnValue(of(exptectedAmbition));
  
  myCompetenceService.getAmbition("same","2").subscribe(
      response => expect(response).toEqual(exptectedAmbition, 'expected ambition'),
      fail
    );
  expect(httpClientSpy.get.calls.count()).toBe(1, 'one call');
});

fit('should add data in competence', () => {
  const expectedResult = {formErrorCount: 0,formErrors: {}, formMessageCount: 1, formMessages: {0: "Request has been initiated successfully!"},
  formWarningCount: 0, formWarnings: {}, isValidationFailed: false, responseData: null}
  const compData = [{"loggedInSignum":"EKOMKHA","requestedBySignum":"EKOMKHA","vendorID":7,
  "competanceID":151,"competenceUpgradeID":6,"competenceGradeID":2,"status":"Initiated",
  "changedBy":"engineer","createdBy":"EKOMKHA","technologyID":7,"domainID":20,"isEditable":true}]; 

  httpClientSpy.post.and.returnValue(of(expectedResult));

  myCompetenceService.addCompetence(compData).subscribe(
    response => expect(response).toEqual(expectedResult),
    fail
  );
  expect(httpClientSpy.post.calls.count()).toBe(1, 'one call')

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

  myCompetenceService.getCompValuesFromApi('33').subscribe(
    response => expect(response).toEqual(expectedResult),
    fail
  );
  expect(httpClientSpy.get.calls.count()).toBe(1, 'one call')

});

fit('isUpdateTable method is called with reloadAllTables false', async()=>{
  let statusObj = {'status': 'Initiated', 'reloadAllTables': false};
  component.isUpateTable(statusObj); 
  expect(component.isUpateTable).toBeTruthy();
});

fit('isUpdateTable method is called with reloadAllTables true', async()=>{
  let statusObj = {'status': 'Initiated', 'reloadAllTables': true};
  component.isUpateTable(statusObj); 
  expect(component.isUpateTable).toBeTruthy();
   statusObj = {'status': 'Send To Manager', 'reloadAllTables': true};
   component.isUpateTable(statusObj); 
   expect(component.isUpateTable).toBeTruthy();
});

fit('editCompChild is called', async() => {

  const row = {active: true,ambition: null,baseline: 2,changedBy: "engineer",competanceID: 149,
  competanceUpgradeLevel: "same",competenceGrade: null,competenceGradeID: 2,competenceTypeID: 1,
  competenceUpgrade: null,competenceUpgradeID: 6,competencyServiceArea: null,competencyUpgrade: "A to A",
  createdBy: "EKOMKHA",createdon: 1568025427590,deliveryCompetanceID: 2170,domain: null,domainID: 12,
  isEditable: false,lmSignum: "EKUYOGE",loggedInSignum: "EKOMKHA",parentSystemID: 0,requestedBySignum: "EKOMKHA",
  requestedStatus: null,slmSignum: "EDBEVS",status: "Initiated",systemID: 33,technology: null,technologyID: 10,
  userCompetanceID: null,vendor: null,vendorID: 5}

  component.editCompParent(row);
  expect(component.editCompParent).toBeTruthy();
});

fit('tabClick method is called', async() => {
  const tab = { index:1 };
  component.tabClick(tab);
  expect(component.tabClick).toBeTruthy();

})
  
});
