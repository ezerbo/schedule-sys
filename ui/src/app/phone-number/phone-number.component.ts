import { Employee } from '../employee/employee';
import { EmployeeService } from '../employee/employee.service';
import { CommonComponent } from '../shared/common';
import { PhoneNumber } from './phone-number';
import { PhoneNumberLabel } from './phone-number.label';
import { PhoneNumberService } from './phone-number.service';
import { PhoneNumberType } from './phone-number.type';
import { Component, OnInit, ViewChild, ChangeDetectorRef, Input, Output, EventEmitter } from '@angular/core';
import { NgForm } from '@angular/forms';
import { MenuItem, ConfirmationService, Message, SelectItem } from 'primeng/primeng';
import * as _ from 'lodash';

@Component({
  selector: 'app-phone-number',
  templateUrl: './phone-number.component.html',
  styleUrls: ['./phone-number.component.css']
})
export class PhoneNumberComponent extends CommonComponent implements OnInit {

  @Input() employee: Employee;
  phoneNumbers: PhoneNumber[] = [];
  phoneNumber: PhoneNumber;
  selectedPhoneNumber: PhoneNumber;
  tableDataLoading: false;
  phoneNumbersPerEmployee = 3;
  editing = false;
  displayDialog = false;
  @ViewChild('phoneNumberForm') phoneNumberForm: NgForm;
  dialogMsgs: Message[] = [];
  @Output() entity_events = new EventEmitter();

 phoneNumberTypes: SelectItem[] = [
     {label: PhoneNumberType.PRIMARY, value: PhoneNumberType.PRIMARY},
     {label: PhoneNumberType.SECONDARY, value: PhoneNumberType.SECONDARY},
     {label: PhoneNumberType.OTHER, value: PhoneNumberType.OTHER}
   ];

  phoneNumberLabels: SelectItem[] = [
    {label: PhoneNumberLabel.HOME, value: PhoneNumberLabel.HOME},
    {label: PhoneNumberLabel.MOBILE, value: PhoneNumberLabel.MOBILE}
  ];

  constructor(
    private employeeService: EmployeeService,
    private phoneNumberService: PhoneNumberService,
    private confirmationService: ConfirmationService,
    private changeDetector: ChangeDetectorRef
  ) { super(null); }

  ngOnInit() {
    this.phoneNumber = new PhoneNumber();
  }

  getPhoneNumbers() {
    this.employeeService.getPhoneNumbers(this.employee.id)
      .subscribe(response => {
        this.phoneNumbers = response;
      });
  }

  create() {
    this.phoneNumber.phoneNumber = this.unmaskNumber(this.phoneNumber.phoneNumber);
    this.phoneNumber.employee = this.employee;
    this.phoneNumberService.update(this.phoneNumber)
      .subscribe(
        response => {
            if (!this.editing) {
              this.phoneNumbers.push(response);
              this.phoneNumbers = this.phoneNumbers.slice();
              this.changeDetector.markForCheck();
            } else {
              this.refreshOnEdit(this.phoneNumber, this.selectedPhoneNumber);
            }
            this.displayDialog = false;
            this.entity_events.emit({severity: 'success', summary: '', detail: 'Phone number successfully saved'});
        },
        error => {
            this.displayMessage({severity: 'error', summary: '', detail: error}, this.dialogMsgs);
        }
      );
  }

  deleteOne(): void {
    this.confirmationService.confirm({
      message: 'Are you sure you want to delete this phone number ?',
      accept: () => {
        this.phoneNumberService.deleteOne(this.selectedPhoneNumber.id)
          .subscribe(
          response => {
            this.phoneNumbers = this.phoneNumbers.filter((val, i) => val.id !== this.selectedPhoneNumber.id);
            this.selectedPhoneNumber = undefined;
            this.entity_events.emit({severity: 'success', summary: '', detail: 'Phone number successfully deleted'})
          },
          error => {
           this.entity_events.emit({severity: 'error', summary: '', detail: error})
          }
          );
      }
    });
  }

  showOrHideDialog(editing: boolean) {
      this.editing = editing;
      this.displayDialog = !this.displayDialog;
      if (editing) {
        this.phoneNumber = _.cloneDeep(this.selectedPhoneNumber);
      } else {
        this.phoneNumber = new PhoneNumber();
        this.phoneNumberForm.resetForm();
      }
  }

}
