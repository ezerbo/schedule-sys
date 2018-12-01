import { environment } from '../../environments/environment';
import { LoginService } from '../login/login.service';
import { CommonService } from '../shared/commonservice';
import { Schedule } from './schedule';
import { ScheduleAudit } from './schedule-detail/schedule-audit';
import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs/Observable';

@Injectable()
export class ScheduleService extends CommonService {

  private resourceUrl = environment.apiBaseUrl + '/api/schedules'
  private employeeScheduleSummariesUrl = environment.apiBaseUrl + '/api/schedules/employee-summary';

  constructor(private http: HttpClient) { super(); }

  deleteOne(id: number): Observable<string> {
    return this.http.delete(this.resourceUrl + '/' + id)
        .map(response => 'Schedule Successfully Deleted')
        .catch(this.handleError);
  }

  update(schedule: Schedule): Observable<Schedule> {
    return this.http.put(this.resourceUrl, schedule)
      .map(response => new Schedule(response))
      .catch(this.handleError);
  }

  getScheduleAudit(id: number): Observable<ScheduleAudit[]> {
    return this.http.get(this.resourceUrl + '/' + id + '/audit')
      .map(response => response as ScheduleAudit[])
      .catch(this.handleError);
  }

  getEmployeeScheduleSummaries(scheduleDate: Date): Observable<Schedule[]> {
    return this.http.get(this.employeeScheduleSummariesUrl + '?scheduleDate=' + scheduleDate)
      .map(response => response as Schedule[])
      .catch(this.handleError);
  }

  getAll(scheduleDate: Date): Observable<Schedule[]> {
    return null;
  }
}
