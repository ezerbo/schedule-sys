import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { InsuranceCompanyComponent } from './insurance-company.component';

describe('InsuranceCompanyComponent', () => {
  let component: InsuranceCompanyComponent;
  let fixture: ComponentFixture<InsuranceCompanyComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ InsuranceCompanyComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(InsuranceCompanyComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
