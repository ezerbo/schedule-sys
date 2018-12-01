import { environment } from '../../../environments/environment';
import { LoginService } from '../../login/login.service';
import { CommonService } from '../../shared/commonservice';
import { ScheduleSummary } from './schedulesummary.model';
import { Injectable } from '@angular/core';
import {HttpClient} from '@angular/common/http';
import { Observable } from 'rxjs/Observable';

@Injectable()
export class ScheduleSummaryService extends CommonService {

  resourceUrl = environment.apiBaseUrl + '/api/schedules/company-summary';

  constructor(private http: HttpClient) { super(); }

  getSchedulesSummary(startDate?: Date, endDate?: Date): Observable<ScheduleSummary[]> {
    startDate.setHours(0, 0, 0, 0);
    endDate.setHours(0, 0, 0, 0);
    let requestParams = '?startDate=' + startDate + '&endDate=' + endDate;
    return this.http.get(this.resourceUrl + requestParams)
      .map(response => response as ScheduleSummary[])
      .catch(this.handleError);
  }
}
