import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { DataTableCrudComponent } from './data-table-crud.component';

describe('DataTableCrudComponent', () => {
  let component: DataTableCrudComponent;
  let fixture: ComponentFixture<DataTableCrudComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ DataTableCrudComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(DataTableCrudComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should be created', () => {
    expect(component).toBeTruthy();
  });
});
