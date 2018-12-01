import { TestBed, inject } from '@angular/core/testing';

import { InsuranceCompanyService } from './insurance-company.service';

describe('InsuranceCompanyService', () => {
  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [InsuranceCompanyService]
    });
  });

  it('should be created', inject([InsuranceCompanyService], (service: InsuranceCompanyService) => {
    expect(service).toBeTruthy();
  }));
});
