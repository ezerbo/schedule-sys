import { environment } from '../../environments/environment';
import { LoginService } from '../login/login.service';
import { CommonService } from '../shared/commonservice';
import { Preference } from './preference';
import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs/Observable';

@Injectable()
export class PreferenceService extends CommonService {

  resourceUrl = environment.apiBaseUrl + '/api/preferences';

  constructor(
    private http: HttpClient
  ) { super(); }

  getOne(): Observable<Preference> {
    return this.http.get(this.resourceUrl)
      .map(response => new Preference(response))
      .catch(this.handleError);
  }

  save(preference: Preference): Observable<string> {
    return this.http.put(this.resourceUrl, preference)
      .map(response => new Preference(response))
      .catch(this.handleError);
  }

}
