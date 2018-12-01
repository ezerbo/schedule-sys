import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { CompanyScheduleComponent } from './company-schedule.component';

describe('CompanyScheduleComponent', () => {
  let component: CompanyScheduleComponent;
  let fixture: ComponentFixture<CompanyScheduleComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ CompanyScheduleComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(CompanyScheduleComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should be created', () => {
    expect(component).toBeTruthy();
  });
});
