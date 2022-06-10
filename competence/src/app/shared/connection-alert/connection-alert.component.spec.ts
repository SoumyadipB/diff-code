import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { ConnectionAlertComponent } from './connection-alert.component';

describe('ConnectionAlertComponent', () => {
  let component: ConnectionAlertComponent;
  let fixture: ComponentFixture<ConnectionAlertComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ ConnectionAlertComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(ConnectionAlertComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
