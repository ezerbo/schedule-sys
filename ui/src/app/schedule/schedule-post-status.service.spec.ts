import { TestBed, inject } from '@angular/core/testing';

import { SchedulePostStatusService } from './schedule-post-status.service';

describe('SchedulePostStatusService', () => {
  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [SchedulePostStatusService]
    });
  });

  it('should be created', inject([SchedulePostStatusService], (service: SchedulePostStatusService) => {
    expect(service).toBeTruthy();
  }));
});
