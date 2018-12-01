import { TestBed, inject } from '@angular/core/testing';

import { LicenseTypeService } from './license-type.service';

describe('LicenseTypeService', () => {
  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [LicenseTypeService]
    });
  });

  it('should be created', inject([LicenseTypeService], (service: LicenseTypeService) => {
    expect(service).toBeTruthy();
  }));
});
