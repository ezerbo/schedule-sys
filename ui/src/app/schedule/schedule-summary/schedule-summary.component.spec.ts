import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { ScheduleSummaryComponent } from './schedule-summary.component';

describe('ScheduleSummaryComponent', () => {
  let component: ScheduleSummaryComponent;
  let fixture: ComponentFixture<ScheduleSummaryComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ ScheduleSummaryComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(ScheduleSummaryComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should be created', () => {
    expect(component).toBeTruthy();
  });
});
