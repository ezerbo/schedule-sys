import { Schedule } from '../schedule';
import { ScheduleService } from '../schedule.service';
import { ScheduleType } from '../scheduletype';
import { ScheduleAudit } from './schedule-audit';
import { Component, OnInit, Input, EventEmitter, Output } from '@angular/core';

@Component({
  selector: 'app-schedule-detail',
  templateUrl: './schedule-detail.component.html',
  styleUrls: ['./schedule-detail.component.css']
})
export class ScheduleDetailComponent implements OnInit {

  @Input()
  schedule: Schedule;

  @Input()
  scheduleType: string;

  @Output()
  back = new EventEmitter();

  scheduleAudits: ScheduleAudit[] = [];
  showAudit = false;
  auditBtnLabel = 'Show Change History';

  companySchedule = ScheduleType.COMPANY;
  employeeSchedule = ScheduleType.EMPLOYEE;

  constructor(
    private scheduleService: ScheduleService
  ) { }

  ngOnInit() {

  }

  getScheduleAudit() {
     this.scheduleService.getScheduleAudit(this.schedule.id)
      .subscribe(
        response => {this.scheduleAudits = response},
        error => {console.log('Something unexpected happened')}
      );
  }

  onBackBtnClick() {
    this.showAudit = false;
    this.back.emit();
  }

  showOrHideAudit() {
    this.showAudit = !this.showAudit;
    if (this.showAudit) {
      this.auditBtnLabel = 'Hide Change History'
      this.getScheduleAudit();
    } else {
      this.auditBtnLabel = 'Show Change History';
    }
  }

}
