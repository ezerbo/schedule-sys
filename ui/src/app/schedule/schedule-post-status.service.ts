import { environment } from '../../environments/environment';
import { CommonService } from '../shared/commonservice';
import { SchedulePostStatus } from './schedulepoststatus';
import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs/Observable';

@Injectable()
export class SchedulePostStatusService extends CommonService {

  resourceUrl = environment.apiBaseUrl + '/api/schedule-post-statuses';

  constructor(private http: HttpClient) { super(); }

  getAll(): Observable<SchedulePostStatus[]> {
    return this.http.get(this.resourceUrl)
        .map(response => response as SchedulePostStatus[])
        .catch(this.handleError);
  }

}
