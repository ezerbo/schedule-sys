import { environment } from '../../environments/environment';
import { CommonService } from '../shared/commonservice';
import { License } from './license';
import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs/Observable';

@Injectable()
export class LicenseService extends CommonService {

  resourceUrl = environment.apiBaseUrl + '/api/licenses';

  constructor(private http: HttpClient) { super(); }

  update(license: License): Observable<License> {
    return this.http.put(this.resourceUrl, license)
      .map(response => new License(response))
      .catch(this.handleError);
  }

  deleteOne(id: number): Observable<string> {
    return this.http.delete(this.resourceUrl + '/' + id)
      .map(response => 'License successfully deleted')
      .catch(this.handleError);
  }
}
