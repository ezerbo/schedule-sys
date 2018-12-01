import { TestBed, inject } from '@angular/core/testing';

import { UserroleService } from './userrole.service';

describe('UserroleService', () => {
  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [UserroleService]
    });
  });

  it('should be created', inject([UserroleService], (service: UserroleService) => {
    expect(service).toBeTruthy();
  }));
});
