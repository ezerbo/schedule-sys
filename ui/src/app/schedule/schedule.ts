import { CareCompany } from '../company/care-company';
import { Employee } from '../employee/employee';
import { UserProfile } from '../profile/userprofile';
import { SchedulePostStatus } from './schedulepoststatus';
import { ScheduleStatus } from './schedulestatus';
export class Schedule {
  id: number;
  employee: Employee = new Employee();
  careCompany: CareCompany = new CareCompany();
  scheduleStatus: ScheduleStatus = new ScheduleStatus();
  schedulePostStatus: SchedulePostStatus = new SchedulePostStatus();
  scheduleSysUser: UserProfile = new UserProfile();
  shiftStartTime: Date;
  shiftEndTime: Date;
  scheduleDate: Date;
  hoursWorked: number;
  overtime: number;
  timeSheetReceived: boolean;
  comment: string;
  paid: boolean;
  billed: boolean;


  static toArray(jsons: any[]): Schedule[] {
      const schedules: Schedule[] = [];
      if (jsons != null) {
          for (const json of jsons) {
              schedules.push(new Schedule(json));
          }
      }
      return schedules;
  }

  constructor(json?: any) {
    if (json != null) {
      this.id = json.id;
      this.employee = new Employee(json.employee);
      this.careCompany = new CareCompany(json.careCompany);
      this.scheduleStatus = new ScheduleStatus(json.scheduleStatus);
      this.schedulePostStatus = new SchedulePostStatus(json.schedulePostStatus);
      this.shiftStartTime = new Date(json.shiftStartTime);
      this.shiftEndTime = new Date(json.shiftEndTime);
      this.scheduleDate = new Date(json.scheduleDate);
      this.hoursWorked = json.hoursWorked;
      this.overtime = json.overtime;
      this.timeSheetReceived = json.timeSheetReceived;
      this.comment = json.comment;
      this.paid = json.paid;
      this.billed = json.billed;
      this.scheduleSysUser = new UserProfile(json.scheduleSysUser);
    }
  }

}
