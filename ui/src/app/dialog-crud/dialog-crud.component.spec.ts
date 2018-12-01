import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { DialogCrudComponent } from './dialog-crud.component';

describe('DialogCrudComponent', () => {
  let component: DialogCrudComponent;
  let fixture: ComponentFixture<DialogCrudComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ DialogCrudComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(DialogCrudComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should be created', () => {
    expect(component).toBeTruthy();
  });
});
