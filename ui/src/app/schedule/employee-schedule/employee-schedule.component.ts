import { Employee } from '../../employee/employee';
import { EmployeeService } from '../../employee/employee.service';
import { CommonComponent } from '../../shared/common';
import { Schedule } from '../schedule';
import { ScheduleType } from '../scheduletype';
import { Component, OnInit, Input } from '@angular/core';

@Component({
  selector: 'app-employee-schedule',
  templateUrl: './employee-schedule.component.html',
  styleUrls: ['./employee-schedule.component.css']
})
export class EmployeeScheduleComponent extends CommonComponent implements OnInit {

  @Input()
  employee: Employee;
  scheduleType = ScheduleType.EMPLOYEE;
  schedules: Schedule[] = [];
  selectedSchedule: Schedule;
  showScheduleDetail = false;

  constructor(
    private employeeService: EmployeeService
  ) { super(null); }

  ngOnInit() {
    this.getSchedules();
  }

   getSchedules() {
    this.employeeService.getSchedules(this.employee.id)
        .subscribe(response => {this.schedules = response}, error => { this.schedules = [] });
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
}
