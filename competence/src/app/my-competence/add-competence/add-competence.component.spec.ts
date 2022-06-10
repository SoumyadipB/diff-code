import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { AddCompetenceComponent } from './add-competence.component';
import { DebugElement, NgModule, Directive, Injectable } from '@angular/core';
import { BrowserModule, By } from '@angular/platform-browser';
import { FormsModule, ReactiveFormsModule, FormControlDirective, FormGroupDirective } from '@angular/forms';
import { BrowserDynamicTestingModule, platformBrowserDynamicTesting } from '@angular/platform-browser-dynamic/testing';
import { MatOptionModule, MatSelectModule, MatCardModule, MatGridListModule, MatTableDataSource, MatTableModule, MatIconModule, MatButtonToggleModule, MatInputModule, MatSlideToggleModule, MatDialogModule, MatSlideToggle, MatSelect, MAT_DIALOG_DATA, MatDialogRef, MatButtonModule, MatButton, MatDialog, MatCheckboxModule, MatPaginatorModule } from '@angular/material';
import { HttpClientModule, HTTP_INTERCEPTORS, HttpInterceptor, HttpRequest, HttpHandler, HttpEvent } from '@angular/common/http';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { RouterModule } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { MyCompetenceService } from '../my-cometence.service';
import { AlertBoxComponent } from 'src/app/shared/alert-box/alert-box.component';
import { AlertBoxService } from 'src/app/shared/alert-box.service';

import { CompetenceAlertComponent } from 'src/app/shared/competence-alert/competence-alert.component';
import { CompetenceListComponent } from '../competence-list/competence-list.component';
import { AuthService } from 'src/app/auth/auth.service';
import { Observable } from 'rxjs';
import { $ } from 'protractor';

@Directive({
  selector: "[color]"
})
@NgModule({
  declarations: [AlertBoxComponent, CompetenceAlertComponent],
  entryComponents: [AlertBoxComponent, CompetenceAlertComponent]
})
class TestModule {}

class MockAuthService extends AuthService{ 
  userRole = 'Engineer';
  loggedInSignum = 'EKOMKHA';
  loggedInEmpName = 'Komal';
  getAuthData(){
   return {
     userRole: 'User',
       loggedInSignum: 'EKOMKHA',
       empName:'EKOMKHA'
     }
  }
   }    
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

fdescribe('AddCompetenceComponent', () => {

  let component: AddCompetenceComponent;
  let competencelistcomp: CompetenceListComponent;
  let fixture: ComponentFixture<AddCompetenceComponent>;
  let competenceListFixture: ComponentFixture<CompetenceListComponent>;
  let de:DebugElement;
  let el: HTMLElement;
  let competenceService: MyCompetenceService;  
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
        
      declarations: [ AddCompetenceComponent, AlertBoxComponent, CompetenceAlertComponent, CompetenceListComponent], 
      imports: [
          BrowserModule, FormsModule, ReactiveFormsModule, MatOptionModule, MatSelectModule,
          MatCardModule, MatGridListModule, MatTableModule, MatIconModule, HttpClientModule,MatCheckboxModule,
          BrowserAnimationsModule, MatButtonToggleModule, MatInputModule, MatSlideToggleModule,MatPaginatorModule,
          RouterModule, RouterTestingModule, MatDialogModule, BrowserDynamicTestingModule, MatButtonModule
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
    .compileComponents().then(() => {
        fixture = TestBed.createComponent(AddCompetenceComponent);
        fixture.detectChanges();
        component = fixture.componentInstance;
       
        de = fixture.debugElement.query(By.css('form'));
        el = de.nativeElement;
        competenceService = fixture.debugElement.injector.get(MyCompetenceService);
        alertboxService = fixture.debugElement.injector.get(AlertBoxService);
        mockAuthService = fixture.debugElement.injector.get(MockAuthService);
        // competenceListFixture = TestBed.createComponent(CompetenceListComponent);
        // competencelistcomp = competenceListFixture.componentInstance;
    })
    
   
  }));

  beforeEach(() => {
   // fixture = TestBed.createComponent(AddCompetenceComponent);
    //component = fixture.componentInstance;    
    //fixture.detectChanges();
   
  });

  //The second test expects the property of the component “submitted” to be true when the “onSubmit” function is called.
  fit('should set submitted true', async(() => {
    component.onSubmit();
    expect(component.submitted).toBeTruthy();
  }));

  fit('should call onSubmit method', async(() => {
      fixture.detectChanges();
      //create a jasmine “spy” on the “onSubmit” function of the component. 
      spyOn(component, 'onSubmit');
      el = fixture.debugElement.query(By.css('button')).nativeElement
      el.click();    
      //we expect that the spied function is not executed, because the button should be disabled since the form is not valid.
    expect(component.onSubmit).toHaveBeenCalledTimes(0);
  }));

  fit('form should be invalid', () => {
  // component.ambitionText = "";
   fixture.detectChanges();
   component.competenceFormGroup.controls['ambition'].setValue('');
  // component.competenceFormGroup.controls['ambitionLevel'].setValue('');
   component.competenceFormGroup.controls['ambitionValue'].setValue('');
   component.competenceFormGroup.controls['technology'].setValue('');
   component.competenceFormGroup.controls['domain'].setValue('');
   component.competenceFormGroup.controls['vendor'].setValue('');
   component.competenceFormGroup.controls['competenceType'].setValue('');
   component.competenceFormGroup.controls['baseline'].setValue('');
   component.competenceFormGroup.controls['serviceArea'].setValue('');
   expect(component.competenceFormGroup.valid).toBeFalsy();
});

fit('form should be valid', () => {
  // component.ambitionText = "";
  fixture.detectChanges();
  component.competenceFormGroup.controls['ambition'].setValue('A to A'); 
  component.competenceFormGroup.controls['ambitionValue'].setValue('1');
  component.competenceFormGroup.controls['technology'].setValue('10');
  component.competenceFormGroup.controls['domain'].setValue('12');
  component.competenceFormGroup.controls['vendor'].setValue('5');
  component.competenceFormGroup.controls['competenceType'].setValue('1');
  component.competenceFormGroup.controls['baseline'].setValue('2');
  component.competenceFormGroup.controls['serviceArea'].setValue('149');
  expect(component.competenceFormGroup.valid).toBeTruthy();
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
  component.competenceTypeValue = "1";
  fixture.detectChanges();
  compTypeID = component.competenceTypeValue;
  component.getDomainByCompTypeId(compTypeID,false)
  expect(component.getDomainByCompTypeId).toBeTruthy();
});
fit('check value for compTypeID if compTypeID undefined ', async() => {
  let compTypeID = undefined; 
  component.competenceTypeValue = "1";
  fixture.detectChanges();
  compTypeID = component.competenceTypeValue;
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
  const row = {active: true,ambition: null,baseline: 2,changedBy: "engineer",competanceID: 149,
  competanceUpgradeLevel: "same",competenceGrade: null,competenceGradeID: 2,competenceTypeID: 1,
  competenceUpgrade: null,competenceUpgradeID: 6,competencyServiceArea: null,competencyUpgrade: "A to A",
  createdBy: "EKOMKHA",createdon: 1568025427590,deliveryCompetanceID: 2170,domain: null,domainID: 12,
  isEditable: false,lmSignum: "EKUYOGE",loggedInSignum: "EKOMKHA",parentSystemID: 0,requestedBySignum: "EKOMKHA",
  requestedStatus: null,slmSignum: "EDBEVS",status: "Initiated",systemID: 33,technology: null,technologyID: 10,
  userCompetanceID: null,vendor: null,vendorID: 5}
//For Next Level
  const row1 = {active: true,ambition: null,baseline: 2,changedBy: "engineer",competanceID: 149,
  competanceUpgradeLevel: "next level",competenceGrade: null,competenceGradeID: 2,competenceTypeID: 1,
  competenceUpgrade: null,competenceUpgradeID: 6,competencyServiceArea: null,competencyUpgrade: "A to A",
  createdBy: "EKOMKHA",createdon: 1568025427590,deliveryCompetanceID: 2170,domain: null,domainID: 12,
  isEditable: false,lmSignum: "EKUYOGE",loggedInSignum: "EKOMKHA",parentSystemID: 0,requestedBySignum: "EKOMKHA",
  requestedStatus: null,slmSignum: "EDBEVS",status: "Initiated",systemID: 33,technology: null,technologyID: 10,
  userCompetanceID: null,vendor: null,vendorID: 5}

  component.editCompetence(row);
  component.editCompChild(row);
  component.editCompetence(row1);
  component.editCompChild(row1);

  fixture.detectChanges();
  expect(component.editCompetence).toBeTruthy();
  expect(component.editCompChild).toBeTruthy();
});

fit('cancelCompetence is called', async() => {
  const cancelButton = fixture.debugElement.query(By.css('#cancelCompetenceBtn')).nativeElement;
  component.competenceFormGroup.controls['ambition'].setValue('A to A'); 
  component.competenceFormGroup.controls['ambitionValue'].setValue('1');
  component.competenceFormGroup.controls['technology'].setValue('10');
  component.competenceFormGroup.controls['domain'].setValue('12');
  component.competenceFormGroup.controls['vendor'].setValue('5');
  component.competenceFormGroup.controls['competenceType'].setValue('1');
  component.competenceFormGroup.controls['baseline'].setValue('2');
  component.competenceFormGroup.controls['serviceArea'].setValue('149');
   fixture.detectChanges();
   expect(component.competenceFormGroup.valid).toBeTruthy();
    
  //spyOn(component, 'cancelCompetence');
  cancelButton.click();
  fixture.detectChanges();    //addCompetenceForm = component.competenceFormGroup;  
  expect(component.cancelCompetence).toBeTruthy();
})

//Enter data successfully.
fit('addCompetenceToTable method is called and data is entered in table', async() => {
  const addCompetenceBtn = fixture.debugElement.query(By.css('#addCompetenceBtn')).nativeElement;
  component.competenceFormGroup.controls['ambition'].setValue('A to A'); 
  component.competenceFormGroup.controls['ambitionValue'].setValue('6');
  component.competenceFormGroup.controls['technology'].setValue('11');
  component.competenceFormGroup.controls['domain'].setValue('13');
  component.competenceFormGroup.controls['vendor'].setValue('5');
  component.competenceFormGroup.controls['competenceType'].setValue('1');
  component.competenceFormGroup.controls['baseline'].setValue('2');
  component.competenceFormGroup.controls['serviceArea'].setValue('197');
   fixture.detectChanges();
   expect(component.competenceFormGroup.valid).toBeTruthy();
    
   addCompetenceBtn.click();
   fixture.detectChanges();

   await fixture.whenStable().then(async ()=>{
     component.loggedInSignum = 'EKOMKHA';   
    const dialogYesButton = fixture.nativeElement.parentElement.querySelector('.mat-raised-button.mat-primary');
    dialogYesButton.click();
   });

   expect(component.addCompToTable).toBeTruthy();   
   
  //  let engineerList:any;
  //  fixture.detectChanges();    
  //  competenceListFixture = TestBed.createComponent(CompetenceListComponent);
  //  competencelistcomp = competenceListFixture.componentInstance;

  //  competencelistcomp.loggedInSignum = 'EKOMKHA';
  //  await mockAuthService.getAuthData();
  //  fixture.detectChanges();
  //  let dataForComp= {"requestedStatus":["Initiated"],"loggedInSignum":mockAuthService.loggedInSignum,"requestedBySignum":mockAuthService.loggedInSignum}
  //  competenceListFixture.detectChanges();
  //  engineerList = await competenceService.getSavedCompetence(dataForComp).toPromise();
  //  competenceListFixture.detectChanges();
  //  expect(engineerList.length).toBeGreaterThanOrEqual(0);
       
});
//Data already exist
fit('addCompetenceToTable method is called with duplicate entry', async() => {
  const addCompetenceBtn = fixture.debugElement.query(By.css('#addCompetenceBtn')).nativeElement;
  let okbtn = fixture.nativeElement.parentElement.querySelector('.mat-raised-button');
  okbtn.click();
  fixture.detectChanges();
  component.competenceFormGroup.controls['ambition'].setValue('A to A'); 
  component.competenceFormGroup.controls['ambitionValue'].setValue('6');
  component.competenceFormGroup.controls['technology'].setValue('11');
  component.competenceFormGroup.controls['domain'].setValue('13');
  component.competenceFormGroup.controls['vendor'].setValue('5');
  component.competenceFormGroup.controls['competenceType'].setValue('1');
  component.competenceFormGroup.controls['baseline'].setValue('2');
  component.competenceFormGroup.controls['serviceArea'].setValue('197');
   fixture.detectChanges();
   expect(component.competenceFormGroup.valid).toBeTruthy();
    
   addCompetenceBtn.click();
   fixture.detectChanges();

   await fixture.whenStable().then(async ()=>{
     component.loggedInSignum = 'EKOMKHA';   
    const dialogYesButton = fixture.nativeElement.parentElement.querySelector('.mat-raised-button.mat-primary');
    dialogYesButton.click();
   //fixture.detectChanges();
   expect(component.addCompToTable).toBeTruthy();
   });    
});

fit('updateCompetenceToTable method is called with changes', async() => { 
  let okbtn = fixture.nativeElement.parentElement.querySelector('.mat-raised-button');
  okbtn.click();
  fixture.detectChanges();
let engineerList:any;     
//create Testbed for Competencelist
 competenceListFixture = TestBed.createComponent(CompetenceListComponent);
 competencelistcomp = competenceListFixture.componentInstance;
 let editButton
await competenceListFixture.whenStable().then(async ()=>{
await mockAuthService.getAuthData();
fixture.detectChanges();
let dataForComp= {"requestedStatus":["Initiated"],"loggedInSignum":mockAuthService.loggedInSignum,"requestedBySignum":mockAuthService.loggedInSignum}

competencelistcomp.loggedInSignum = 'EKOMKHA';
competenceListFixture.detectChanges();
//populate Data in table
engineerList = await competenceService.getSavedCompetence(dataForComp).toPromise();
competenceListFixture.detectChanges();

expect(engineerList.length).toBeGreaterThanOrEqual(0);

//Pick 1st row of table and click edit button to get systemID
const tableRows = competenceListFixture.nativeElement.querySelectorAll('tr');      
let row = tableRows[1];
editButton = row.cells[1].firstElementChild.firstElementChild;
editButton.click();
//detect changes after button click in form as well as table.
competenceListFixture.detectChanges();
});

//Create Form Test bed now.
fixture = TestBed.createComponent(AddCompetenceComponent);
component = fixture.componentInstance;
fixture.detectChanges();

await fixture.whenStable().then(async ()=>{
  let getRowData: any;
  //Get data against syatemID that was clicked in competence list component (edit bytton)
 getRowData = await competenceService.getCompValuesFromApi(editButton.value).toPromise();
  component.competenceFormGroup.controls['ambition'].setValue(getRowData.competencyUpgrade); 
  component.competenceFormGroup.controls['ambitionValue'].setValue(getRowData.competenceUpgradeID);
  component.competenceFormGroup.controls['technology'].setValue(getRowData.technologyID);
  component.competenceFormGroup.controls['domain'].setValue(getRowData.domainID);
  component.competenceFormGroup.controls['vendor'].setValue(getRowData.vendorID);
  component.competenceFormGroup.controls['competenceType'].setValue(getRowData.competenceTypeID);
  component.competenceFormGroup.controls['baseline'].setValue(getRowData.baseline);
  component.competenceFormGroup.controls['serviceArea'].setValue(getRowData.competanceID);
  component.competenceFormGroup.controls["systemID"].setValue(getRowData.systemID);
fixture.detectChanges();
expect(component.competenceFormGroup.valid).toBeTruthy();
//});
//check update click hence change button in form to updatebutton
component.submitButtonText = "Update Competence";
component.editStatus = getRowData.status;
component.isBaselineSelected = true;
fixture.detectChanges();

//make some change and test
const selectbox = fixture.debugElement.query(By.css('#baseline')).nativeElement; // changing baseline
  selectbox.click();
  fixture.detectChanges();  
    const inquiryOptions = fixture.debugElement.queryAll(By.css('.mat-option-text'));
    inquiryOptions[3].nativeElement.click();
    fixture.detectChanges();
});
    await fixture.whenStable().then(()=>{
    component.getAmbitionByGradeWeight('4',false);
    fixture.detectChanges();
    });
   
 const addCompetenceBtn = fixture.debugElement.nativeElement.querySelector('#addCompetenceBtn'); 
 //click on update button of form
  addCompetenceBtn.click();
  fixture.detectChanges();

  await fixture.whenStable().then(()=>{
    component.loggedInSignum = 'EKOMKHA';   
    const dialogYesButton = fixture.nativeElement.parentElement.querySelector('.mat-raised-button.mat-primary');
    dialogYesButton.click();
   //fixture.detectChanges();
   expect(component.updateCompetenceToTable).toBeTruthy();   
  });
});

fit('updateCompetenceToTable method is called without changes', async() => { 
  let okbtn = fixture.nativeElement.parentElement.querySelector('.mat-raised-button');
  okbtn.click();
  fixture.detectChanges();

  let engineerList:any;     
  //create Testbed for Competencelist
   competenceListFixture = TestBed.createComponent(CompetenceListComponent);
   competencelistcomp = competenceListFixture.componentInstance;
   let editButton
  await competenceListFixture.whenStable().then(async ()=>{
  await mockAuthService.getAuthData();
  fixture.detectChanges();
  let dataForComp= {"requestedStatus":["Initiated"],"loggedInSignum":mockAuthService.loggedInSignum,"requestedBySignum":mockAuthService.loggedInSignum}
  
  competencelistcomp.loggedInSignum = 'EKOMKHA';
  competenceListFixture.detectChanges();
  //populate Data in table
  engineerList = await competenceService.getSavedCompetence(dataForComp).toPromise();
  competenceListFixture.detectChanges();
  
  expect(engineerList.length).toBeGreaterThanOrEqual(0);
  
  //Pick 1st row of table and click edit button to get systemID
  const tableRows = competenceListFixture.nativeElement.querySelectorAll('tr');      
  let row = tableRows[1];
  editButton = row.cells[1].firstElementChild.firstElementChild;
  editButton.click();
  //detect changes after button click in form as well as table.
  competenceListFixture.detectChanges();
  });
  
  //Create Form Test bed now.
  fixture = TestBed.createComponent(AddCompetenceComponent);
  component = fixture.componentInstance;
  fixture.detectChanges();
  
  await fixture.whenStable().then(async ()=>{
    let getRowData: any;
    //Get data against syatemID that was clicked in competence list component (edit bytton)
   getRowData = await competenceService.getCompValuesFromApi(editButton.value).toPromise();
    component.competenceFormGroup.controls['ambition'].setValue(getRowData.competencyUpgrade); 
    component.competenceFormGroup.controls['ambitionValue'].setValue(getRowData.competenceUpgradeID);
    component.competenceFormGroup.controls['technology'].setValue(getRowData.technologyID);
    component.competenceFormGroup.controls['domain'].setValue(getRowData.domainID);
    component.competenceFormGroup.controls['vendor'].setValue(getRowData.vendorID);
    component.competenceFormGroup.controls['competenceType'].setValue(getRowData.competenceTypeID);
    component.competenceFormGroup.controls['baseline'].setValue(getRowData.baseline);
    component.competenceFormGroup.controls['serviceArea'].setValue(getRowData.competanceID);
    component.competenceFormGroup.controls["systemID"].setValue(getRowData.systemID);
  fixture.detectChanges();
  expect(component.competenceFormGroup.valid).toBeTruthy();
  //});
  //check update click hence change button in form to updatebutton
  component.submitButtonText = "Update Competence";
  component.editStatus = getRowData.status;
  component.isBaselineSelected = true;
  fixture.detectChanges();
  });    
     
   const addCompetenceBtn = fixture.debugElement.nativeElement.querySelector('#addCompetenceBtn'); 
   //click on update button of form
    addCompetenceBtn.click();
    fixture.detectChanges();
  
    await fixture.whenStable().then(()=>{
      component.loggedInSignum = 'EKOMKHA';   
      const dialogYesButton = fixture.nativeElement.parentElement.querySelector('.mat-raised-button.mat-primary');
      dialogYesButton.click();
     //fixture.detectChanges();
     expect(component.updateCompetenceToTable).toBeTruthy();   
    });
  });

  fit('delete newly added record', async()=>{
  let okbtn = fixture.nativeElement.parentElement.querySelector('.mat-raised-button');
  okbtn.click();
  fixture.detectChanges();
    let engineerList:any;     
    //create Testbed for Competencelist
     competenceListFixture = TestBed.createComponent(CompetenceListComponent);
     competencelistcomp = competenceListFixture.componentInstance;
     let deleteButton;
    await competenceListFixture.whenStable().then(async ()=>{
    await mockAuthService.getAuthData();
    fixture.detectChanges();
    let dataForComp= {"requestedStatus":["Initiated"],"loggedInSignum":mockAuthService.loggedInSignum,"requestedBySignum":mockAuthService.loggedInSignum}
    
    competencelistcomp.loggedInSignum = 'EKOMKHA';
    competenceListFixture.detectChanges();
    //populate Data in table
    engineerList = await competenceService.getSavedCompetence(dataForComp).toPromise();
    competenceListFixture.detectChanges();
    
    expect(engineerList.length).toBeGreaterThanOrEqual(0);
    
    //Pick 1st row of table and click edit button to get systemID
    const tableRows = competenceListFixture.nativeElement.querySelectorAll('tr');      
    let row = tableRows[1];
    deleteButton = row.cells[1].firstElementChild.lastElementChild;
    deleteButton.click();
    //detect changes after button click in form as well as table.
    competenceListFixture.detectChanges();

    await fixture.whenStable().then(()=>{
      component.loggedInSignum = 'EKOMKHA';   
      const dialogYesButton = competenceListFixture.nativeElement.parentElement.querySelector('.mat-dialog-container .mat-raised-button.mat-primary');
      dialogYesButton.click();
      fixture.detectChanges();      
    });
    });

    await fixture.whenStable().then(() => {
      let okbtn = competenceListFixture.nativeElement.parentElement.querySelector('.mat-dialog-container .mat-raised-button');
  
      okbtn.click();
      
    })
  });

  // fit('testing internet lost alert', async() => {
  //   alertboxService.connectionAlert('Error', 'Internet Connection Lost');
  //   expect(alertboxService.connectionAlert).toBeTruthy();
  // })

});
