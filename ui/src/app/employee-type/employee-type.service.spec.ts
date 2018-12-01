import { TestBed, inject } from '@angular/core/testing';

import { EmployeeTypeService } from './employee-type.service';

describe('EmployeeTypeService', () => {
  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [EmployeeTypeService]
    });
  });

  it('should be created', inject([EmployeeTypeService], (service: EmployeeTypeService) => {
    expect(service).toBeTruthy();
  }));
});
