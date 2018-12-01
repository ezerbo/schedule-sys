import { environment } from '../../environments/environment';
import { CommonService } from '../shared/commonservice';
import { ScheduleStatus } from './schedulestatus';
import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs/Observable';

@Injectable()
export class ScheduleStatusService extends CommonService {

  resourceUrl = environment.apiBaseUrl + '/api/schedule-statuses';

  constructor(private http: HttpClient) { super(); }

  getAll(): Observable<ScheduleStatus[]> {
    return this.http.get(this.resourceUrl)
        .map(response => response as ScheduleStatus[])
        .catch(this.handleError);
  }
}
