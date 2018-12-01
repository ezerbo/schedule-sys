import { TestBed, inject } from '@angular/core/testing';

import { LicenseService } from './license.service';

describe('LicenseService', () => {
  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [LicenseService]
    });
  });

  it('should be created', inject([LicenseService], (service: LicenseService) => {
    expect(service).toBeTruthy();
  }));
});
