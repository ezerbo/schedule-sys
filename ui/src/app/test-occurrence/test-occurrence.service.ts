import { environment } from '../../environments/environment';
import { LoginService } from '../login/login.service';
import { CommonService } from '../shared/commonservice';
import { TestOccurrence } from '../test-occurrence/testoccurrence';
import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs/Observable';

@Injectable()
export class TestOccurrenceService extends CommonService {

  resourceUrl = environment.apiBaseUrl + '/api/test-occurrences'

  constructor(private http: HttpClient) { super(); }

  update(testOccurrence: TestOccurrence): Observable<TestOccurrence> {
    return this.http.put(this.resourceUrl, testOccurrence)
        .map(response => new TestOccurrence(response))
        .catch(this.handleError);
  }

  deleteOne(id: number): Observable<string> {
    return this.http.delete(this.resourceUrl + '/' + id)
        .map(response => 'Test Occurrence Successfully Deleted')
        .catch(this.handleError);
  }
}
