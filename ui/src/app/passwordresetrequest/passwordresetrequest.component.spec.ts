import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { PasswordresetrequestComponent } from './passwordresetrequest.component';

describe('PasswordresetrequestComponent', () => {
  let component: PasswordresetrequestComponent;
  let fixture: ComponentFixture<PasswordresetrequestComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ PasswordresetrequestComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(PasswordresetrequestComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should be created', () => {
    expect(component).toBeTruthy();
  });
});
