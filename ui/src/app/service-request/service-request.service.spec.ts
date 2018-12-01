import { TestBed, inject } from '@angular/core/testing';

import { ServiceRequestService } from './service-request.service';

describe('ServiceRequestService', () => {
  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [ServiceRequestService]
    });
  });

  it('should be created', inject([ServiceRequestService], (service: ServiceRequestService) => {
    expect(service).toBeTruthy();
  }));
});
