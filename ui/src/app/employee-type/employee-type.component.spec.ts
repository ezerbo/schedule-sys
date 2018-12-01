import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { EmployeeTypeComponent } from './employee-type.component';

describe('EmployeeTypeComponent', () => {
  let component: EmployeeTypeComponent;
  let fixture: ComponentFixture<EmployeeTypeComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ EmployeeTypeComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(EmployeeTypeComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should be created', () => {
    expect(component).toBeTruthy();
  });
});
