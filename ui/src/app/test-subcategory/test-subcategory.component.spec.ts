import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { TestSubcategoryComponent } from './test-subcategory.component';

describe('TestSubcategoryComponent', () => {
  let component: TestSubcategoryComponent;
  let fixture: ComponentFixture<TestSubcategoryComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ TestSubcategoryComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(TestSubcategoryComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should be created', () => {
    expect(component).toBeTruthy();
  });
});
