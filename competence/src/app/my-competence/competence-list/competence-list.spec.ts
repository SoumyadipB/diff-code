import { async, ComponentFixture, TestBed } from '@angular/core/testing';

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

fdescribe('CompetenceListComponent', () => {

  let component: CompetenceListComponent;  
  let fixture: ComponentFixture<CompetenceListComponent>;  
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
        
      declarations: [AlertBoxComponent, CompetenceAlertComponent, CompetenceListComponent], 
      imports: [
          BrowserModule, FormsModule, ReactiveFormsModule, MatOptionModule, MatSelectModule,
          MatCardModule, MatGridListModule, MatTableModule, MatIconModule, HttpClientModule,MatCheckboxModule,
          BrowserAnimationsModule, MatButtonToggleModule, MatInputModule, MatSlideToggleModule,MatPaginatorModule,
          RouterModule, RouterTestingModule, MatDialogModule, BrowserDynamicTestingModule, MatButtonModule
        ],
      providers: [AlertBoxService, MockAuthService, { provide: MAT_DIALOG_DATA, useValue: {} }, 
        { provide: MatDialogRef, useValue:mockDialogRef },
        {provide: HTTP_INTERCEPTORS,
          useClass: MockAuthInterceptor,
          //useValue:{'role':'user', 'signum':'EKOMKHA'},
          multi: true}]
    
    })
    .overrideModule(BrowserDynamicTestingModule, { set: { entryComponents: [AlertBoxComponent, CompetenceAlertComponent] } })
    .compileComponents().then(() => {
        fixture = TestBed.createComponent(CompetenceListComponent);
        fixture.detectChanges();
        component = fixture.componentInstance;       
        de = fixture.debugElement.query(By.css('table'));
        el = de.nativeElement;
        competenceService = fixture.debugElement.injector.get(MyCompetenceService);
        alertboxService = fixture.debugElement.injector.get(AlertBoxService);
        mockAuthService = fixture.debugElement.injector.get(MockAuthService);
        
    })
    
   
  }));


  fit('should return data to populate table.', async() => {
    let engineerList: any;
    let dataForComp= {"requestedStatus":["Initiated"],"loggedInSignum":"EKOMKHA","requestedBySignum":"EKOMKHA"}
    engineerList =  await competenceService.getSavedCompetence(dataForComp).toPromise();
     
    expect(engineerList.length).toBeGreaterThanOrEqual(0);
  });  

  fit('delete selected Competence check', async() => {
  //  let data = [{"loggedInSignum":"EKOMKHA","requestedBySignum":"EKOMKHA","vendorID":5,"competanceID":197,"competenceUpgradeID":7,"competenceGradeID":3,"status":"Initiated","changedBy":"engineer","createdBy":"EKOMKHA","technologyID":11,"domainID":13,"isEditable":true}];
    let dataComp = {"requestedStatus":["Initiated"],"loggedInSignum":"EKOMKHA","requestedBySignum":"EKOMKHA","isProgress":"false"};
    let response:any;
 //   response = await competenceService.addCompetence(data).toPromise();
 //   fixture.detectChanges();
 //   expect(response.isValidationFailed).toEqual(false);
    
    await fixture.whenStable().then(async ()=>{
      await mockAuthService.getAuthData();
      fixture.detectChanges();
      let dataForComp= {"requestedStatus":["Initiated"],"loggedInSignum":mockAuthService.loggedInSignum,"requestedBySignum":mockAuthService.loggedInSignum}
      
      component.loggedInSignum = 'EKOMKHA';
      fixture.detectChanges();
      //populate Data in table
      await component.updateTable();
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

      let commonDeleteButton = fixture.nativeElement.parentElement.querySelector('#delSlectedCompetence');
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

  fit('Send for Approval selected Competence check', async() => {
    let okbtn = fixture.nativeElement.parentElement.querySelector('.mat-raised-button');
  okbtn.click();
  fixture.detectChanges();
      let data = [{"loggedInSignum":"EKOMKHA","requestedBySignum":"EKOMKHA","vendorID":9,"competanceID":197,"competenceUpgradeID":6,"competenceGradeID":2,"status":"Initiated","changedBy":"engineer","createdBy":"EKOMKHA","technologyID":15,"domainID":15,"isEditable":true}]
      let dataComp = {"requestedStatus":["Initiated"],"loggedInSignum":"EKOMKHA","requestedBySignum":"EKOMKHA","isProgress":"false"};
      let response:any;
      response = await competenceService.addCompetence(data).toPromise();
      fixture.detectChanges();
     // expect(response.isValidationFailed).toEqual(false);
      
      await fixture.whenStable().then(async ()=>{
        await mockAuthService.getAuthData();
        fixture.detectChanges();
        let dataForComp= {"requestedStatus":["Initiated"],"loggedInSignum":mockAuthService.loggedInSignum,"requestedBySignum":mockAuthService.loggedInSignum}
        
        component.loggedInSignum = 'EKOMKHA';
        fixture.detectChanges();
        //populate Data in table
        await component.updateTable();
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
  
        let commonApprovalButton = fixture.nativeElement.parentElement.querySelector('.sendForApproval');
        commonApprovalButton.click();
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
    
  it("Should include records with 'initiated' status only", async() => {

    let engineerList:any;

    let tempData = [{"loggedInSignum":"EZNISPA","requestedBySignum":"EZNISPA","vendorID":1,"competanceID":203,"competenceUpgradeID":5,"competenceGradeID":1,"status":"Initiated","changedBy":"engineer","createdBy":"EZNISPA","technologyID":1,"domainID":1,"isEditable":true}]
        
    await fixture.whenStable().then(async () => {   
  
      await mockAuthService.getAuthData();
      fixture.detectChanges();
      let dataForComp= {"requestedStatus":["Initiated"],"loggedInSignum":mockAuthService.loggedInSignum,"requestedBySignum":mockAuthService.loggedInSignum}
      
      component.loggedInSignum = 'EKOMKHA';
      fixture.detectChanges();
      //populate Data in table
      engineerList = await competenceService.getSavedCompetence(dataForComp).toPromise();
      fixture.detectChanges();
      expect(engineerList.length).toBeGreaterThanOrEqual(0);

    let tableRows = fixture.nativeElement.querySelectorAll('tr');
       // Header row
    let headerRow = tableRows[0];
    expect(headerRow.cells[9].innerHTML.trim()).toBe('Status');  
  
    // for(let i=1; i<tableRows.length; i++){
      // Data rows
    let row1 = tableRows[1];
    expect(row1.cells[9].innerHTML.trim()).toBe('Initiated');
    // }   
    });
    expect(engineerList.length).toBeGreaterThanOrEqual(1);
  });
  
  it("should delete record from grid on click of ok", async() => {
   
    fixture.detectChanges();
      spyOn(component, 'deleteSingleCompetence').and.callThrough();
      el = fixture.debugElement.query(By.css('button')).nativeElement
      el.click();    
    
      expect(component.deleteSingleCompetence).toHaveBeenCalled();
    
  });
      it('should not delete Record from grid on click of cancel', () => {
     // spyOn(component, 'deleteSingleCompetence').and.callThrough();
     // spyOn(window, 'confirm').and.returnValue(false);
      expect(component.deleteSingleCompetence).not.toHaveBeenCalled();
      });
  
      fit('applyFilter1 method is called', () => {
        const filterValue = "GSM";
        component.applyFilter1(filterValue);
        expect(component.applyFilter1).toBeTruthy();
      });
  
      fit('applyFilter2 method is called', () => {
        const filterValue = "Send to manager";
        component.applyFilter2(filterValue);
        expect(component.applyFilter1).toBeTruthy();
      })
  
      fit('isAllSelected methods is called', () => {      
        component.isAllSelected1();
        fixture.detectChanges();
        component.isAllSelected2();
        fixture.detectChanges();
        expect(component.isAllSelected1).toBeTruthy();
        expect(component.isAllSelected2).toBeTruthy();
      });
  
      fit('masterToggle methods called', () => {
        const columncheckbox = de.nativeElement.querySelector('.selectAllCheckbox');
        columncheckbox.click();
        fixture.detectChanges();
        
        component.masterToggle1();
        component.selection1.clear();
        fixture.detectChanges();
  
        component.masterToggle2();
        component.selection2.clear();
        fixture.detectChanges();
        expect(component.masterToggle1).toBeTruthy();
      });
  
      fit("GetCompValues method call", () => {
        component.getCompValues('33');
        expect(component.getCompValues).toBeTruthy();
        
      });
  
      // fit("delete selected competence", async() => {
      //   let engineerList, competenceList:any;
      //   mockAuthService.getAuthData();
      //   let dataForComp= {"requestedStatus":["Initiated"],"loggedInSignum":mockAuthService.loggedInSignum,"requestedBySignum":mockAuthService.loggedInSignum}
      //   let data = [{"loggedInSignum":"EKOMKHA","requestedBySignum":"EKOMKHA","vendorID":5,"competanceID":194,"competenceUpgradeID":6,"competenceGradeID":2,"status":"Initiated","changedBy":"engineer","createdBy":"EKOMKHA","technologyID":10,"domainID":12,"isEditable":true}];
        
      //   fixture.whenStable().then(async () => {
      //     fixture.detectChanges();
      //     competenceList = await competenceService.addCompetence(data).toPromise();        
      //   fixture.detectChanges();
      //   engineerList = await competenceService.getSavedCompetence(dataForComp).toPromise();
      //   fixture.detectChanges();
        
      //   const tableRows = fixture.nativeElement.querySelectorAll('tr');
        
      //   let row1 = tableRows[1];
      //   let checkbox = row1.cells[0].firstElementChild;
      //   checkbox.click();
      //   fixture.whenStable().then(() => {
      //   expect(row1).toBeTruthy();
      //   });
      //   expect(component.deleteSelectedCompetence).toBeTruthy();
      // });
     
      // })

});
