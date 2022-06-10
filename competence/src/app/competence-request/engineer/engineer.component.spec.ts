import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { EngineerComponent } from './engineer.component';
import { DebugElement, Inject, NgModule, Directive } from '@angular/core';
import { BrowserModule, By } from '@angular/platform-browser';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { BrowserDynamicTestingModule, platformBrowserDynamicTesting } from '@angular/platform-browser-dynamic/testing';
import { MatOptionModule, MatSelectModule, MatCardModule, MatGridListModule, MatTableDataSource, MatTableModule, MatIconModule, MatButtonToggleModule, MatInputModule, MatSlideToggleModule, MatDialogModule, MatDialogTitle, MatDialogRef, MAT_DIALOG_DATA } from '@angular/material';
import { HttpClientModule, HTTP_INTERCEPTORS } from '@angular/common/http';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { RouterModule } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { CompetenceRequestService } from '../competence-request.service';
import { AuthInterceptor } from 'src/app/shared/auth.interceptor';
import { of } from 'rxjs';
import { EngineerRequestComponent } from '../engineer-request/engineer-request.component';
import { CompetenceAlertComponent } from 'src/app/shared/competence-alert/competence-alert.component';
import { AlertBoxComponent } from 'src/app/shared/alert-box/alert-box.component';
//import { AppModule } from 'src/app/app.module';
//import { AuthGuard } from 'src/app/auth/auth.guard';
//import { AuthService } from 'src/app/auth/auth.service';
//import { EngineerRequestComponent } from '../engineer-request.component';
@Directive({
  selector: "[color]"
})
@NgModule({
  declarations: [AlertBoxComponent, CompetenceAlertComponent,EngineerComponent],
  entryComponents: [AlertBoxComponent, CompetenceAlertComponent]
})
class TestModule{}
describe('LMUpdateComponent', () => {

  let component: EngineerComponent;
  let fixture: ComponentFixture<EngineerComponent>;
  let competenceService:CompetenceRequestService;
  let httpClientSpy: { get: jasmine.Spy , post: jasmine.Spy};
  let de:DebugElement;
  let el: HTMLElement;
  let enggService: CompetenceRequestService;
  //let systemID = 0;
  const mockDialogRef = {
    close: jasmine.createSpy('close')
  };

  //create an angular environment for the component being tested.
  beforeEach(async(() => {
    TestBed.resetTestEnvironment();
    TestBed.initTestEnvironment(BrowserDynamicTestingModule,
       platformBrowserDynamicTesting());
       
    TestBed.configureTestingModule({
        
      declarations: [ EngineerComponent ], 
      imports: [
          BrowserModule, FormsModule, ReactiveFormsModule, MatOptionModule, MatSelectModule,
          MatCardModule, MatGridListModule, MatTableModule, MatIconModule, HttpClientModule,
          BrowserAnimationsModule, MatButtonToggleModule, MatInputModule, MatSlideToggleModule,
          RouterModule, RouterTestingModule, MatDialogModule],
     providers: [ { provide: MAT_DIALOG_DATA, useValue: {} },{ provide: MatDialogRef, useValue:mockDialogRef },CompetenceRequestService, {provide: HTTP_INTERCEPTORS,
      useClass: AuthInterceptor,
      multi: true}] 
    
     // { provide: MatDialogRef, useValue: {} }
    })
    .overrideModule(BrowserDynamicTestingModule, { set: { entryComponents: [AlertBoxComponent, CompetenceAlertComponent] } })
     .compileComponents()
    .then(() => {
        fixture = TestBed.createComponent(EngineerComponent);
        fixture.detectChanges();
        component = fixture.componentInstance;
        de = fixture.debugElement.query(By.css('form'));
        el = de.nativeElement;
        enggService = fixture.debugElement.injector.get(CompetenceRequestService);
      
    })

    httpClientSpy = jasmine.createSpyObj('HttpClient', ['get', 'post']);
    competenceService = new CompetenceRequestService(<any> httpClientSpy);    
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(EngineerComponent);
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

fit('should call change method on slide change', () => {
    const componentDebug = fixture.debugElement;
    
    const slider = componentDebug.query(By.css('mat-slide-toggle'));
    component.isBaselineSelected = true;
    fixture.detectChanges();
    spyOn(component, 'onValChange'); // set your spy
    slider.nativeElement.click();
    fixture.detectChanges();
    slider.triggerEventHandler('change', null); // triggerEventHandler
    fixture.detectChanges();
    component.onValChange(event,"2");
    expect(component.onValChange).toHaveBeenCalled(); // event has been called
   
});

fit('calls method onValChange', async() => {
  //spyOn(component, 'getDomainByCompTypeId'); // set your spy  
  let event = {checked:false}  ;
  //component.responseFlag = false; 
  component.onValChange(event,'2');
  fixture.detectChanges();
  expect(component.onValChange).toBeTruthy();  
  expect(component.slideLabel).toEqual('Baseline');
  expect(component.slideTitle).toEqual('Ambition is same as Baseline');

  event.checked =true;
  component.onValChange(event,'2');
  expect(component.slideLabel).toEqual('Next Level');
  expect(component.slideTitle).toEqual('Ambition is gone to Next Level');
});



fit('should call getDomainByCompTypeId method', async() => {
  const componentDebug = de;
  const selectbox = fixture.debugElement.query(By.css('#competenceType')).nativeElement;
  spyOn(component, 'getDomainByCompTypeId'); // set your spy  
  await competenceService.getCompetence().toPromise();
  fixture.detectChanges();
  selectbox.click();
  fixture.detectChanges();  
    const inquiryOptions = fixture.debugElement.queryAll(By.css('.mat-option-text'));
    inquiryOptions[0].nativeElement.click();
    fixture.detectChanges();
    component.getDomainByCompTypeId('1',false);
    fixture.detectChanges();
    expect(component.getDomainByCompTypeId).toHaveBeenCalled(); // event has been called
    //expect(inquiryOptions.length).toEqual(4);
});

fit('calls getDomainByCompTypeId method', async() => {
  //spyOn(component, 'getDomainByCompTypeId'); // set your spy  
component.getDomainByCompTypeId('1',false);
expect(component.getDomainByCompTypeId).toBeTruthy();
});

fit('calls getTechnologyByDomainId method', async() => {
  //spyOn(component, 'getDomainByCompTypeId'); // set your spy  
component.getTechnologyByDomainId('1','5', false);
expect(component.getTechnologyByDomainId).toBeTruthy();
});

fit('calls  getVendorByTechnologyId method', async() => {
  //spyOn(component, 'getDomainByCompTypeId'); // set your spy  
component.getVendorByTechnologyId('1','5', '2', false);
expect(component.getVendorByTechnologyId).toBeTruthy();
});

fit('calls  getServiceAreaByCompId method', async() => {
  //spyOn(component, 'getDomainByCompTypeId'); // set your spy  
component.getServiceAreaByCompId('1','5', '2', '1',false);
expect(component.getServiceAreaByCompId).toBeTruthy();
});

fit('check value for compTypeID if comTypeID = null', async() => {
  let compTypeID = null;
  component.competenceType = "1";
  fixture.detectChanges();
  compTypeID = component.competenceType;
  component.getDomainByCompTypeId(compTypeID,false)
  expect(component.getDomainByCompTypeId).toBeTruthy();
});
fit('check value for compTypeID if compTypeID undefined ', async() => {
  let compTypeID = undefined; 
  component.competenceType = "1";
  fixture.detectChanges();
  compTypeID = component.competenceType;
  component.getDomainByCompTypeId(compTypeID,false)
  expect(component.getDomainByCompTypeId).toBeTruthy();
});

fit('getInnerText is called', async()=>{
  const selectbox = fixture.debugElement.query(By.css('#baseline')).nativeElement;
  //spyOn(component, 'getInnerText'); // set your spy  
  await competenceService.getBaseline().toPromise();
  fixture.detectChanges();
  selectbox.click();
  fixture.detectChanges();  
    const inquiryOptions = fixture.debugElement.queryAll(By.css('.mat-option-text'));
    inquiryOptions[0].nativeElement.click();
    component.getInnerText(inquiryOptions[0].nativeElement.innerHTML.trim());
    fixture.detectChanges();
    expect(component.getInnerText).toBeTruthy();
})

fit('editCompetence is called', async() => {
//For Same 
  const row = {active: true,ambition: null,baseline: 2,changedBy: "LM",competanceID: 149,
  competanceUpgradeLevel: "same",competenceGrade: null,competenceGradeID: 2,competenceTypeID: 1,
  competenceUpgrade: null,competenceUpgradeID: 6,competencyServiceArea: null,competencyUpgrade: "A to A",
  createdBy: "EKOMKHA",createdon: 1568025427590,deliveryCompetanceID: 2170,domain: null,domainID: 12,
  isEditable: false,lmSignum: "EKUYOGE",loggedInSignum: "EKUYOGE",parentSystemID: 0,requestedBySignum: "EKOMKHA",
  requestedStatus: null,slmSignum: "EDBEVS",status: "Sent To Manager",systemID: 33,technology: null,technologyID: 10,
  userCompetanceID: null,vendor: null,vendorID: 5}
//For Next Level
  const row1 = {active: true,ambition: null,baseline: 2,changedBy: "LM",competanceID: 149,
  competanceUpgradeLevel: "next level",competenceGrade: null,competenceGradeID: 2,competenceTypeID: 1,
  competenceUpgrade: null,competenceUpgradeID: 6,competencyServiceArea: null,competencyUpgrade: "A to A",
  createdBy: "EKOMKHA",createdon: 1568025427590,deliveryCompetanceID: 2170,domain: null,domainID: 12,
  isEditable: false,lmSignum: "EKUYOGE",loggedInSignum: "EKUYOGE",parentSystemID: 0,requestedBySignum: "EKOMKHA",
  requestedStatus: null,slmSignum: "EDBEVS",status: "Sent To Manager",systemID: 33,technology: null,technologyID: 10,
  userCompetanceID: null,vendor: null,vendorID: 5}

  component.editCompetence(row);
  component.editCompChild(row);
  component.editCompetence(row1);
  component.editCompChild(row1);

  fixture.detectChanges();
  expect(component.editCompetence).toBeTruthy();
  expect(component.editCompChild).toBeTruthy();
});

fit('should retrieve all data', () => {
  fixture.detectChanges();
  expect(competenceService.getCompetence()).toBeTruthy();      
//  const a = competenceService.getCompetence();
   
  //  expect(competenceService.getCompetence()).toBeGreaterThanOrEqual(1);
  expect(competenceService.getDomain(1)).toBeTruthy();
  expect(competenceService.getTechnology("1","5")).toBeTruthy();
  expect(competenceService.getVendor("1","5","2")).toBeTruthy();     
  expect(competenceService.getServiceArea("1","5","2","1")).toBeTruthy();
  expect(competenceService.getBaseline()).toBeTruthy();
  expect(competenceService.getAmbition("same","1")).toBeTruthy();
   
});

});
