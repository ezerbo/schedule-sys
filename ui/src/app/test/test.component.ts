import { CommonComponent } from '../shared/common';
import { Test } from './test';
import { TestValidation } from './test-validation';
import { TestService } from './test.service';
import { Component, OnInit, ViewChild, AfterViewChecked, ChangeDetectorRef } from '@angular/core';
import { NgForm } from '@angular/forms';
import { Router, ActivatedRoute } from '@angular/router';
import { MenuItem, ConfirmationService, Message, SelectItem } from 'primeng/primeng';
import * as _ from 'lodash';

@Component({
  selector: 'app-test',
  templateUrl: './test.component.html',
  styleUrls: ['./test.component.css']
})
export class TestComponent extends CommonComponent implements OnInit, AfterViewChecked {

  tests: Test[] = [];
  test: Test;
  selectedTest: Test;
  dialogDisplayed = false;

  @ViewChild(NgForm)
  testForm: NgForm;

  constructor(
    public testService: TestService,
    private confirmationService: ConfirmationService,
    private changeDetector: ChangeDetectorRef,
    private route: ActivatedRoute,
    private router: Router
  ) { super(new TestValidation()); }

  ngOnInit() {
    this.test = new Test();
    this.getAll(this.tableCurrentPage, this.tableCurrentRowCount);
    this.buildContextMenuItems();
  }

  ngAfterViewChecked(): void {
     if (this.dialogDisplayed) {
      this.formChanged(this.testForm);
    }
  }

  create() {
    this.testService.update(this.test)
      .subscribe(response => {
         if (!this.editing) {
              this.tests.push(response.result);
              this.tests = this.tests.slice();
              this.changeDetector.markForCheck();
              this.tableItemsCount++;
            } else {
              this.refreshOnEdit(response.result, this.selectedTest);
            }
            this.dialogDisplayed = false;
            this.displayMessage({severity: 'success', summary: '', detail: response.message});
        },
        error => {
            this.displayMessage({severity: 'error', summary: '', detail: error});
        }
      )
  }

  deleteOne() {
    this.confirmationService.confirm({
       message: 'Are you sure you want to delete this test ?',
       accept: () => {
        this.testService.deleteOne(this.selectedTest.id)
          .subscribe(
            response  => {
              this.displayMessage(
                { severity: 'success', summary: '', detail: response});
                this.tests = this.tests.filter((val, i) =>  val.id !== this.selectedTest.id); // Refreshes dataTable
                this.selectedTest = undefined; // Disables 'Edit' and 'Delete' buttons
                // Update number of items so that the paginator displays the correct number of pages
                this.tableItemsCount--;
                if (this.tests.length === 0) {
                  // Goes back to adjacent page when the last item in the list is deleted
                  this.getAll(this.tableCurrentPage - 1, this.tableCurrentRowCount);
                }
            },
            error => {
                this.displayMessage({ severity: 'error', summary: '', detail: error });
            }
          );
       }
    });
  }

  getAll(page: number, size: number) {
    this.tableDataLoading = true;
    this.testService.getAll(page, size)
        .subscribe(
            response => {
              this.tests = response.result;
              this.tableItemsCount = response.count;
              this.tableDataLoading = false;
            }, error => { this.tests = [] }
        );
  }

  private buildContextMenuItems () {
    this.contextMenuItems = [
            { label: 'View'     , icon: 'fa-eye'     , command: (event) => {this.navigateTo('../tests')}},
            { label: 'Edit'     , icon: 'fa-edit'    , command: (event) => {this.showOrHideDialog(true)}},
            { label: 'Delete'   , icon: 'fa-close'   , command: (event) => {this.deleteOne()}}
        ];
  }

   showOrHideDialog(editing: boolean) {
     this.dialogDisplayed = !this.dialogDisplayed;
     this.editing = editing;

     if (this.editing) {
        this.test = _.cloneDeep(this.selectedTest);
     } else {
        this.test = new Test();
        this.testForm.resetForm();
     }
    // When editing, populate form with selected User
  }

  gotToHome() {
    this.router.navigate(['../']);
  }

   navigateTo(destionation: string) {
    this.router.navigate([destionation, this.selectedTest.id], {relativeTo: this.route})
  }

}
