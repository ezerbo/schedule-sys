import { TestBed, inject } from '@angular/core/testing';

import { CarecompanytypeService } from './carecompanytype.service';

describe('CarecompanytypeService', () => {
  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [CarecompanytypeService]
    });
  });

  it('should be created', inject([CarecompanytypeService], (service: CarecompanytypeService) => {
    expect(service).toBeTruthy();
  }));
});
