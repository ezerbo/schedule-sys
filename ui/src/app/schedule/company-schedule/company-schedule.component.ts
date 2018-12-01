import { CareCompany } from '../../company/care-company';
import { CareCompanyService } from '../../company/care-company.service';
import { Employee } from '../../employee/employee';
import { EmployeeService } from '../../employee/employee.service';
import { CommonComponent } from '../../shared/common';
import { FileServiceService } from '../../shared/file-service.service';
import { Schedule } from '../schedule';
import { SchedulePostStatusService } from '../schedule-post-status.service';
import { ScheduleStatusService } from '../schedule-status.service';
import { ScheduleService } from '../schedule.service';
import { ScheduleCSVEntry } from '../schedulecsventry';
import { SchedulePostStatus } from '../schedulepoststatus';
import { ScheduleStatus } from '../schedulestatus';
import { ScheduleType } from '../scheduletype';
import { DatePipe } from '@angular/common';
import { Component, OnInit, Input, AfterViewInit, ViewChild, ChangeDetectorRef } from '@angular/core';
import { NgForm } from '@angular/forms';
import { MenuItem, ConfirmationService, Message, SelectItem, LazyLoadEvent } from 'primeng/primeng';
import * as _ from 'lodash';
import * as Papa from 'papaparse';

@Component({
  selector: 'app-company-schedule',
  templateUrl: './company-schedule.component.html',
  styleUrls: ['./company-schedule.component.css']
})
export class CompanyScheduleComponent extends CommonComponent implements OnInit {

  schedules: Schedule[] = [];
  selectedSchedule: Schedule;
  schedule: Schedule;
  editing = false;
  displayDialog = false;
  showScheduleDetail = false
  showArchived = false;
  employees: Employee[] = [];
  scheduleStatuses: SelectItem[] = [];
  schedulePostStatuses: SelectItem[] = [];
  dialogMsgs: Message[] = [];
  defaultDate = new Date();
  scheduleType = ScheduleType.COMPANY;
  exportedFileName = 'schedules.csv';
  archived = false;

  @ViewChild(NgForm)
  scheduleForm: NgForm;

  @Input()
  careCompany: CareCompany;

  constructor(
    private changeDetector: ChangeDetectorRef,
    private careCompanyService: CareCompanyService,
    private confirmationService: ConfirmationService,
    private scheduleService: ScheduleService,
    private employeeService: EmployeeService,
    private scheduleStatusService: ScheduleStatusService,
    private schedulePostStatusService: SchedulePostStatusService,
    private fileService: FileServiceService,
    private datePipe: DatePipe
  ) { super(null); }

  ngOnInit() {
    this.getAll(this.tableCurrentPage, this.tableCurrentRowCount);
    this.schedule = new Schedule();
    this.getScheduleStatuses();
    this.getSchedulePostStatuses();
    this.defaultDate.setMinutes(0); // Reset minutes of current date so that shift times are 30 minutes apart
  }

  getAll(page: number, size: number, params?: any, filters?: any) {
     this.careCompanyService.getSchedules(this.careCompany.id, page, size, this.archived) // TODO Replace by an archived field
        .subscribe(response => {
          this.schedules = response.schedules;
          this.sortSchedules();
          this.tableItemsCount = response.count
        }, error => {this.schedules = []} /* Prevent the spinner from indefinitely spinning*/ );
  }

  getScheduleStatuses() {
    this.scheduleStatusService.getAll()
      .subscribe(response => {
        response.forEach((status, i) => {
          this.scheduleStatuses.push({label: status.name, value: status.name});
        });
      });
  }

  getSchedulePostStatuses() {
    this.schedulePostStatusService.getAll()
      .subscribe(response => {
        response.forEach((status, i) => {
          this.schedulePostStatuses.push({label: status.name, value: status.name});
        });
      });
  }

  create() {
  // TODO Add support for schedules that span on multiple days
    if (!this.validateShiftDates()) {
      this.displayMessage({severity: 'error', summary: 'Invalid shift times',
         detail: 'Start Time must be before End Time'}, this.dialogMsgs);
      return;
    }
    if (typeof this.schedule.employee === 'string') {
      this.displayMessage({severity: 'error', summary: 'No employee selected',
         detail: 'Please select an employee'}, this.dialogMsgs);
      return;
    }
    let startTime = new Date(this.schedule.scheduleDate.getTime());
    let endTime = new Date(this.schedule.scheduleDate.getTime());
    startTime.setHours(this.schedule.shiftStartTime.getHours(), this.schedule.shiftStartTime.getMinutes(), 0, 0);
    endTime.setHours(this.schedule.shiftEndTime.getHours(), this.schedule.shiftEndTime.getMinutes(), 0, 0);
    this.schedule.shiftStartTime = startTime;
    this.schedule.shiftEndTime = endTime;
    this.schedule.careCompany = this.careCompany;
    this.scheduleService.update(this.schedule)
      .subscribe(
        response => {
          if (!this.editing) {
            this.schedules.push(response);
            this.schedules = this.schedules.slice();
            // Update number of items so that the paginator displays the correct number of pages
            this.tableItemsCount++;
         } else {
            let schedule: Schedule = new Schedule();
            this.refreshOnEdit(response, schedule);
            this.schedules = this.schedules.filter((val, i) => val.id !== this.selectedSchedule.id);
            this.schedules.push(schedule);
            this.schedule = new Schedule();
         }
         this.sortSchedules();
         this.changeDetector.markForCheck();
         this.displayDialog = false;
         this.displayMessage({severity: 'success', summary: '', detail: 'Schedule successfully saved'});
        },
        error => {
           this.displayMessage({severity: 'error', summary: '', detail: error}, this.dialogMsgs);
        }
      );
  }

   deleteSchedule () {
     this.confirmationService.confirm({
       message: 'Are you sure you want to delete this schedule ?',
       accept: () => {
        this.scheduleService.deleteOne(this.selectedSchedule.id)
          .subscribe(
            response  => {
              this.displayMessage({ severity: 'success', summary: '', detail: response});
                this.schedules = this.schedules.filter((val, i) => val.id !== this.selectedSchedule.id); // Refreshes dataTable
                this.selectedSchedule = undefined; // Disables 'Edit' and 'Delete' buttons
                // Update number of items so that the paginator displays the correct number of pages
                this.tableItemsCount--;
                if (this.schedules.length === 0) {
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

  sortSchedules () {
    this.schedules.sort((a, b) => (a.scheduleDate > b.scheduleDate) ? 1 : -1);
  }

  showOrHideDialog(editing: boolean) {
      this.editing = editing;
      this.displayDialog = !this.displayDialog;
      if (editing) {
        this.schedule = _.cloneDeep(this.selectedSchedule);
        this.schedule.shiftStartTime = new Date (this.selectedSchedule.shiftStartTime);
        this.schedule.shiftEndTime = new Date(this.selectedSchedule.shiftEndTime);
        // Auto completion operates on a single field. Combining 'firstName' and 'lastName' for better display
        this.schedule.employee.firstName += ' ' + this.schedule.employee.lastName;
      } else {
        this.schedule = new Schedule();
        this.scheduleForm.resetForm();
      }
  }

  onRowDblClick() {
    this.changeDisplayPreference();
  }

  onBackBtnClick() {
    this.changeDisplayPreference();
  }

  private changeDisplayPreference() {
    this.showScheduleDetail = !this.showScheduleDetail;
  }

  onShowArchived() {
    this.showArchived = true;
    this.archived = true;
    this.getAll(this.tableCurrentPage, this.tableCurrentRowCount);
  }

  onHideArchived() {
    this.showArchived = false;
    this.archived = false;
    this.getAll(this.tableCurrentPage, this.tableCurrentRowCount);
  }

  exportCSV() {
    let scheduleCSVEntries = this.parseSchedules();
    let csvData = Papa.unparse(scheduleCSVEntries);
    this.fileService.saveFile(this.exportedFileName, csvData);
  }

  parseSchedules(): ScheduleCSVEntry[] {
    let scheduleCSVEntries: ScheduleCSVEntry[] = [];
    this.schedules.forEach(schedule => {
      let scheduleDate = this.datePipe.transform(schedule.scheduleDate, 'shortDate');
      let startTime = this.datePipe.transform(schedule.shiftStartTime, 'shortTime');
      let endTime = this.datePipe.transform(schedule.shiftEndTime, 'shortTime');
      let tsr = schedule.timeSheetReceived ? 'yes' : 'no';
      let billed = schedule.billed ? 'yes' : 'no';
      let paid = schedule.paid ? 'yes' : 'no';
      let csvEntry: ScheduleCSVEntry = new ScheduleCSVEntry(
        schedule.scheduleSysUser.username, schedule.employee.firstName, schedule.employee.lastName,
        scheduleDate, schedule.employee.position.name, schedule.scheduleStatus.name, schedule.schedulePostStatus.name,
        startTime, endTime, tsr, schedule.hoursWorked, schedule.overtime, billed, paid, schedule.comment
      );
      scheduleCSVEntries.push(csvEntry);
    });
    return scheduleCSVEntries;
  }

  searchEmployees(event) {
    const query = event.query;
    this.employeeService.search(query)
      .subscribe(response => {
        response.forEach((employee , i) => {
          employee.firstName += ' ' + employee.lastName; // Combine first and last names for easy display
        });
        this.employees = response;
        // TODO Revise this ? Use a ViewModel wrapping 'Employee'
      });
  }

  validateShiftDates(): boolean {
    if (this.schedule.shiftEndTime.getTime() <= this.schedule.shiftStartTime.getTime()) {
      return false;
    }
    return true;
  }

}
