import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { EditCompetenceProgressComponent } from './edit-competence-progress.component';

describe('EditCompetenceProgressComponent', () => {
  let component: EditCompetenceProgressComponent;
  let fixture: ComponentFixture<EditCompetenceProgressComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ EditCompetenceProgressComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(EditCompetenceProgressComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
