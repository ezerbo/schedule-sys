import { TestBed, inject } from '@angular/core/testing';

import { ScheduleStatusService } from './schedule-status.service';

describe('ScheduleStatusService', () => {
  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [ScheduleStatusService]
    });
  });

  it('should be created', inject([ScheduleStatusService], (service: ScheduleStatusService) => {
    expect(service).toBeTruthy();
  }));
});
