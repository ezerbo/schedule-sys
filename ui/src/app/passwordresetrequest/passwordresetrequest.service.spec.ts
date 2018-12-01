import { TestBed, inject } from '@angular/core/testing';

import { PasswordresetrequestService } from './passwordresetrequest.service';

describe('PasswordresetrequestService', () => {
  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [PasswordresetrequestService]
    });
  });

  it('should be created', inject([PasswordresetrequestService], (service: PasswordresetrequestService) => {
    expect(service).toBeTruthy();
  }));
});
