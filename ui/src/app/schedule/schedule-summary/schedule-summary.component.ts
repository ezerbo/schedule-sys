import { Schedule } from '../schedule';
import { ScheduleService } from '../schedule.service';
import { ScheduleType } from '../scheduletype';
import { ScheduleSummaryService } from './schedule-summary.service';
import { ScheduleSummary } from './schedulesummary.model';
import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';

@Component({
  selector: 'app-schedule-summary',
  templateUrl: './schedule-summary.component.html',
  styleUrls: ['./schedule-summary.component.css']
})
export class ScheduleSummaryComponent implements OnInit {

  today = new Date();
  startDate: Date = null;
  endDate: Date = null;
  scheduleSummaries: ScheduleSummary[];
  employeeScheduleSummaries: Schedule[];
  showByCompany = true;
  displayPreferenceBtnLabel = 'Show By Employee';

  constructor(
    private scheduleSummaryService: ScheduleSummaryService,
    private scheduleService: ScheduleService,
    private route: ActivatedRoute,
    private router: Router,
  ) { }

  ngOnInit() {
    this.getCompanyScheduleSummary(this.today, this.today);
  }

  getEmployeeSheduleSummary() {
    this.scheduleService.getEmployeeScheduleSummaries(this.today)
     .subscribe(response => {this.employeeScheduleSummaries = response});
  }

  getCompanyScheduleSummary(startDate: Date, endDate: Date) {
     this.scheduleSummaryService.getSchedulesSummary(startDate, endDate)
      .subscribe(
        response => {this.scheduleSummaries = response; },
        error => {console.log('Something unexpected happened')}
      );
  }

  onRowDblclick(event) {
     this.router.navigate(['schedules', { id: event.data.careCompanyId, scheduleType: ScheduleType.COMPANY}], {relativeTo: this.route});
  }

  onDisplayPreferenceChange() {
    this.showByCompany = !this.showByCompany;
    if (this.showByCompany) {
      this.displayPreferenceBtnLabel = 'Show By Employee';
    } else {
      this.getEmployeeSheduleSummary();
      this.displayPreferenceBtnLabel = 'Show By Company';
    }
  }

  onLeftBtnClick() {
    this.startDate = new Date(this.today.getTime() - 604800000);
    this.endDate = this.today;
    this.showByCompany = true;
    this.getCompanyScheduleSummary(this.startDate, this.endDate);
  }

  onRightBtnClick() {
    this.startDate = this.today;
    this.showByCompany = true;
    this.endDate = new Date(this.today.getTime() + 604800000);
    this.getCompanyScheduleSummary(this.startDate, this.endDate);
  }

  onTodayBtnClick() {
    this.startDate = null;
    this.endDate = null;
    this.getCompanyScheduleSummary(this.today, this.today);
  }

}
