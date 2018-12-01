import { Employee } from '../employee/employee';
import { EmployeeService } from '../employee/employee.service';
import { CommonComponent } from '../shared/common';
import { Test } from '../test/test';
import { TestService } from '../test/test.service';
import { TestOccurrenceService } from './test-occurrence.service';
import { TestOccurrence } from './testoccurrence';
import { Component, OnInit, Input, ViewChild, EventEmitter, Output, ChangeDetectorRef } from '@angular/core';
import { NgForm } from '@angular/forms';
import { MenuItem, ConfirmationService, Message, SelectItem } from 'primeng/primeng';
import * as _ from 'lodash';

@Component({
  selector: 'app-test-occurrence',
  templateUrl: './test-occurrence.component.html',
  styleUrls: ['./test-occurrence.component.css']
})
export class TestOccurrenceComponent extends CommonComponent implements OnInit {

  @Input() employee: Employee;
  testOccurrences: TestOccurrence[] = [];
  testOccurrence: TestOccurrence;
  selectedTestOccurrence: TestOccurrence;
  tests: Test[] = [];
  testSubcategories: SelectItem[] = [];
  tableDataLoading: false;
  editing = false;
  displayDialog = false;
  @ViewChild('testOccurrenceForm') testOccurrenceForm: NgForm;
  dialogMsgs: Message[] = [];
  @Output() entity_events = new EventEmitter();

  constructor(
    private employeeService: EmployeeService,
    private testOccurrenceService: TestOccurrenceService,
    private testService: TestService,
    private confirmationService: ConfirmationService,
    private changeDetector: ChangeDetectorRef
  ) { super(null); }

  ngOnInit() {
    this.testOccurrence = new TestOccurrence();
  }

  getTests() {
    this.employeeService.getTests(this.employee.id)
      .subscribe(response => {
        this.testOccurrences = response;
      });
  }

   create() {
    this.testOccurrence.employee = this.employee;
    if (!this.testOccurrence.applicable) {
      this.testOccurrence.completionDate = undefined;
      this.testOccurrence.expirationDate = undefined;
    }
    this.testOccurrenceService.update(this.testOccurrence)
      .subscribe(
        response => {
            if (!this.editing) {
              this.testOccurrences.push(response);
              this.testOccurrences = this.testOccurrences.slice();
              this.changeDetector.markForCheck();
            } else {
              console.log('Response after update : ' + JSON.stringify(this.testOccurrence));
              this.refreshOnEdit(response, this.selectedTestOccurrence);
            }
            this.displayDialog = false;
            this.entity_events.emit({severity: 'success', summary: '', detail: 'Test Successfully Saved'});
        },
        error => {
            this.displayMessage({severity: 'error', summary: '', detail: error}, this.dialogMsgs);
        }
      );
  }

  deleteOne(): void {
    this.confirmationService.confirm({
      message: 'Are you sure you want to delete this test ?',
      accept: () => {
        this.testOccurrenceService.deleteOne(this.selectedTestOccurrence.id)
          .subscribe(
          response => {
            this.testOccurrences = this.testOccurrences.filter((val, i) => val.id !== this.selectedTestOccurrence.id);
            this.selectedTestOccurrence = undefined;
            this.entity_events.emit({severity: 'success', summary: '', detail: 'Test occurrence successfully deleted'})
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
        this.testOccurrence = _.cloneDeep(this.selectedTestOccurrence);
        if (this.testOccurrence.testSubcategory !== undefined) {
          this.onTestSelect(); // Fetch all sub categories
        }
      } else {
        this.testOccurrence = new TestOccurrence();
        this.testSubcategories = [];
        this.testOccurrenceForm.resetForm();
      }
  }

  searchTests(event) {
    const query = event.query;
    this.testService.search(query)
      .subscribe(response => {
        this.tests = response;
      });
  }

  onTestSelect() {
    this.testSubcategories = [];
    if (!this.testOccurrence.test.canBeWaived) {
      this.testOccurrence.applicable = true;
    }
    this.testService.getAllSubcategories(this.testOccurrence.test.id)
        .subscribe(response => {
            response.forEach((v, i) => {this.testSubcategories.push({label: v.name, value: v.name})});
        });
  }

}
