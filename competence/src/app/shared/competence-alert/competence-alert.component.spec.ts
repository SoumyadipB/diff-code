import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { CompetenceAlertComponent } from './competence-alert.component';

describe('CompetenceAlertComponent', () => {
  let component: CompetenceAlertComponent;
  let fixture: ComponentFixture<CompetenceAlertComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ CompetenceAlertComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(CompetenceAlertComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
