import { TestBed, inject } from '@angular/core/testing';

import { TestOccurrenceService } from './test-occurrence.service';

describe('TestOccurrenceService', () => {
  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [TestOccurrenceService]
    });
  });

  it('should be created', inject([TestOccurrenceService], (service: TestOccurrenceService) => {
    expect(service).toBeTruthy();
  }));
});
