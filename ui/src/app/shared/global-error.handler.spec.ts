import { TestBed, inject } from '@angular/core/testing';

import { GlobalerrorhandlerService } from './globalerrorhandler.service';

describe('GlobalerrorhandlerService', () => {
  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [GlobalerrorhandlerService]
    });
  });

  it('should be created', inject([GlobalerrorhandlerService], (service: GlobalerrorhandlerService) => {
    expect(service).toBeTruthy();
  }));
});
