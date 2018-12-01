import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { TestOccurrenceComponent } from './test-occurrence.component';

describe('TestOccurrenceComponent', () => {
  let component: TestOccurrenceComponent;
  let fixture: ComponentFixture<TestOccurrenceComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ TestOccurrenceComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(TestOccurrenceComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should be created', () => {
    expect(component).toBeTruthy();
  });
});
