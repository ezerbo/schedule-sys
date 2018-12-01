import { TestBed, inject } from '@angular/core/testing';

import { TestSubcategoryService } from './test-subcategory.service';

describe('TestSubcategoryService', () => {
  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [TestSubcategoryService]
    });
  });

  it('should be created', inject([TestSubcategoryService], (service: TestSubcategoryService) => {
    expect(service).toBeTruthy();
  }));
});
