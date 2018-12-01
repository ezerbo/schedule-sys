import { EmployeeType } from '../employee-type/employee-type';
import { EmployeeTypeService } from '../employee-type/employee-type.service';
import { InsuranceCompanyService } from '../insurance-company/insurance-company.service';
import { InsuranceCompany } from '../insurance-company/insurancecompany';
import { PositionService } from '../position/position.service';
import { CommonComponent } from '../shared/common';
import { Employee } from './employee';
import { EmployeeValidation } from './employee-validation';
import { EmployeeService } from './employee.service';
import { PhoneNumber } from '../phone-number/phone-number';
import { PhoneNumberService } from '../phone-number/phone-number.service';
import { PhoneNumberType } from '../phone-number/phone-number.type';
import { FileServiceService } from '../shared/file-service.service';
import { ScheduleType } from '../schedule/scheduletype';
import { EmployeeCSVEntry } from './employecsventry';
import { DatePipe } from '@angular/common';
import { Component, OnInit, ViewChild, AfterViewChecked, DoCheck, ChangeDetectorRef } from '@angular/core';
import { NgForm } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { MenuItem, ConfirmationService, Message, SelectItem } from 'primeng/primeng';
import * as _ from 'lodash';
import * as Papa from 'papaparse';
import * as moment from 'moment';
import { Observable } from 'rxjs/Observable';

@Component({
  selector: 'app-employee',
  templateUrl: './employee.component.html',
  styleUrls: ['./employee.component.css']
})
export class EmployeeComponent extends CommonComponent implements OnInit, AfterViewChecked {

  @ViewChild('employeeForm') employeeForm: NgForm;
  employees: Employee[] = [];
  selectedEmployee: Employee;
  employee: Employee;
  positions: SelectItem[] = [];
  employeeTypes: SelectItem[] = [];
  detailView = false;
  exportedFileName = 'employees.csv';

  constructor(
    private employeeService: EmployeeService,
    private employeeTypeService: EmployeeTypeService,
    private positionService: PositionService,
    private confirmationService: ConfirmationService,
    private phoneNumberService: PhoneNumberService,
    private route: ActivatedRoute,
    private router: Router,
    private fileService: FileServiceService,
    private datePipe: DatePipe,
    private changeDetector: ChangeDetectorRef
  ) { super(new EmployeeValidation()); }

  ngOnInit() {
    this.employee = new Employee();
    this.getEmployeeTypes();
    this.getPositions();
    this.buildContextMenuItems();
    this.getAll(this.tableCurrentPage, this.tableCurrentRowCount);
  }

  ngAfterViewChecked(): void {
    if (this.dialogDisplayed) {
      this.formChanged(this.employeeForm);
    }
  }

   create(): void {
    const result = this.editing ? this.employeeService.update(this.employee)
                          : this.employeeService.create(this.employee);
    result.subscribe(
      response => {
        this.displayMessage({severity: 'success', summary: '', detail: response.message});
         if (!this.editing) {
            this.employees.push(response.result);
            this.employees = this.employees.slice();
            this.changeDetector.markForCheck();
            // Update number of items so that the paginator displays the correct number of pages
            this.tableItemsCount++;
         } else {
           let employee: Employee = new Employee();
           this.refreshOnEdit(this.employee, employee);
           this.employees = this.employees.filter((val, i) =>  val.id !== this.selectedEmployee.id);
           this.employees.push(employee);
           this.employee = new Employee();
         }
        // TODO Create/Update phone numbers
        this.sortEmployees();
        this.hideDialog();
      },
      error => {
       this.displayMessage({severity: 'error', summary: '', detail: error});
      },
    );
  }

  sortEmployees() {
    this.employees.sort((a, b) => (a.lastName > b.lastName) ? 1 : -1);
  }

  deleteOne() {
     this.confirmationService.confirm({
       message: 'Are you sure you want to delete this employee ?',
       accept: () => {
        this.employeeService.deleteOne(this.selectedEmployee.id)
          .subscribe(
            response  => {
              this.displayMessage(
                { severity: 'success', summary: '', detail: 'User successfully deleted'});
                this.employees = this.employees.filter((val, i) =>  val.id !== this.selectedEmployee.id); // Refreshes dataTable
                this.selectedEmployee = undefined; // Disables 'Edit' and 'Delete' buttons
                // Update number of items so that the paginator displays the correct number of pages
                this.tableItemsCount--;
                if (this.employees.length === 0) {
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

   private buildContextMenuItems () {
    this.contextMenuItems = [
            { label: 'View'     , icon: 'fa-eye'     , command: (event) => {this.navigateTo('../employees')}},
            { label: 'Edit'     , icon: 'fa-edit'    , command: (event) => {this.showDialog(true)}},
            { label: 'Delete'   , icon: 'fa-close'   , command: (event) => {this.deleteOne()}},
             { label: 'Schedules', icon: 'fa-calendar'      , command: (event) => { this.navigateTo('../schedules',
            { id: this.selectedEmployee.id, scheduleType: ScheduleType.EMPLOYEE} )}}
        ];
  }

  showDialog(editing: boolean) {
     this.dialogDisplayed = true;
     this.editing = editing;
    // When editing, populate form with selected User
     this.employee = editing ? _.cloneDeep(this.selectedEmployee) : new Employee();
  }

  hideDialog() {
    this.dialogDisplayed = false;
    this.employeeForm.resetForm();
  }

  getAll(page: number, size: number, params?: any, filters?: any) {
    this.tableDataLoading = true;
    this.employeeService.getAll(page, size, filters)
        .subscribe(
            response => {
              this.employees = response.result;
              this.tableItemsCount = response.count;
              this.tableDataLoading = false;
            }, error => { this.employees = [] }
        );
  }

  getEmployeeTypes() {
    this.employeeTypeService.getAll()
        .subscribe(
          response => {
            response.forEach((employeeType) => {
              this.employeeTypes.push({label: employeeType.name, value: employeeType.name});
            })
          })
  }

  getPositions() {
    this.positionService.getAll()
         .subscribe(
            response => {
              response.forEach((position) => {
                this.positions.push({label: position.name, value: position.name});
              })
            }
         );
  }

  exportCSV() {
    let employeeCSVEntries = this.parseEmployees();
    let csvData = Papa.unparse(employeeCSVEntries);
    this.fileService.saveFile(this.exportedFileName, csvData);
  }

  parseEmployees(): EmployeeCSVEntry[] {
    let careCompaniesCSVEntries: EmployeeCSVEntry[] = [];
    this.employees.forEach(employee => {
      let dateOfHire = this.datePipe.transform(employee.dateOfHire, 'shortDate');
      let lastDayOfWork = this.datePipe.transform(employee.lastDayOfWork, 'shortDate');
      let ebc = employee.ebc ? 'yes' : 'no';
      let active = employee.insvc ? 'yes' : 'no';
      let employeeCSVEntry: EmployeeCSVEntry = new EmployeeCSVEntry(
        employee.firstName, employee.lastName, dateOfHire, lastDayOfWork, ebc, active,
        employee.position.name, employee.employeeType.name);
      careCompaniesCSVEntries.push(employeeCSVEntry);
    });
    return careCompaniesCSVEntries;
  }

  gotToHome() {
    this.router.navigate(['../']);
  }

  navigateTo(destionation: string, navigationExtras?: any) {
    if (navigationExtras === undefined) {
      this.router.navigate([destionation, this.selectedEmployee.id] , {relativeTo: this.route});
    } else {
      this.router.navigate([destionation,  navigationExtras] , {relativeTo: this.route});
    }
  }


}
