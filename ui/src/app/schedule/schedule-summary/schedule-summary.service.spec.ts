import { TestBed, inject } from '@angular/core/testing';

import { ScheduleSummaryService } from './schedule-summary.service';

describe('ScheduleSummaryService', () => {
  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [ScheduleSummaryService]
    });
  });

  it('should be created', inject([ScheduleSummaryService], (service: ScheduleSummaryService) => {
    expect(service).toBeTruthy();
  }));
});
