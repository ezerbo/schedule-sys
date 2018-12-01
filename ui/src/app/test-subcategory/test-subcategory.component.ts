import { CommonComponent } from '../shared/common';
import { Test } from '../test/test';
import { TestService } from '../test/test.service';
import { TestSubcategoryService } from './test-subcategory.service';
import { TestSubcategory } from './testsubcategory';
import { Component, OnInit, Input, ViewChild, EventEmitter, Output, ChangeDetectorRef } from '@angular/core';
import { NgForm } from '@angular/forms';
import { HttpErrorResponse } from '@angular/common/http';
import { MenuItem, ConfirmationService, Message, SelectItem } from 'primeng/primeng';
import * as _ from 'lodash';

@Component({
  selector: 'app-test-subcategory',
  templateUrl: './test-subcategory.component.html',
  styleUrls: ['./test-subcategory.component.css']
})
export class TestSubcategoryComponent extends CommonComponent implements OnInit {

  @Input() test: Test;
  testSubcategories: TestSubcategory[] = [];
  testSubcategory: TestSubcategory;
  selectedSubcategory: TestSubcategory;
  tableDataLoading: false;
  editing = false;
  displayDialog = false;
  @ViewChild('subcategoryForm') subcategoryForm: NgForm;
  dialogMsgs: Message[] = [];
  @Output() entity_events = new EventEmitter();

  constructor(
    private testService: TestService,
    private testSubcategoryService: TestSubcategoryService,
    private confirmationService: ConfirmationService,
    private changeDetector: ChangeDetectorRef
  ) { super(null); }

  ngOnInit() {
    this.testSubcategory = new TestSubcategory();
  }

  create() {
    this.testSubcategory.test = this.test;
    this.testSubcategoryService.update(this.testSubcategory)
      .subscribe(
        response => {
            if (!this.editing) {
              this.testSubcategories.push(response);
              this.testSubcategories = this.testSubcategories.slice();
              this.changeDetector.markForCheck();
            } else {
              this.refreshOnEdit(this.testSubcategory, this.selectedSubcategory);
            }
            this.displayDialog = false;
            this.entity_events.emit({severity: 'success', summary: '', detail: 'Subcategory successfully saved'});
        },
        error => {
            this.displayMessage({severity: 'error', summary: '', detail: error}, this.dialogMsgs);
        }
      );
  }

  deleteOne(): void {
    this.confirmationService.confirm({
      message: 'Are you sure you want to delete this subcategory ?',
      accept: () => {
        this.testSubcategoryService.deleteOne(this.selectedSubcategory.id)
          .subscribe(
          response => {
            this.testSubcategories = this.testSubcategories.filter((val, i) => val.id !== this.selectedSubcategory.id);
            this.selectedSubcategory = undefined;
            this.entity_events.emit({severity: 'success', summary: '', detail: response })
          },
          error => {
           this.entity_events.emit({severity: 'error', summary: '', detail: error})
          }
          );
      }
    });
  }

  getAll() {
    this.testService.getAllSubcategories(this.test.id)
      .subscribe(response => {
        this.testSubcategories = response;
      });
  }

  showOrHideDialog(editing: boolean) {
      this.editing = editing;
      this.displayDialog = !this.displayDialog;
      if (editing) {
        this.testSubcategory = _.cloneDeep(this.selectedSubcategory);
      } else {
        this.testSubcategory = new TestSubcategory();
        this.subcategoryForm.resetForm();
      }
  }

}
