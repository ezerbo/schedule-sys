import { environment } from '../../environments/environment';
import { CommonService } from '../shared/commonservice';
import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable } from 'rxjs/Observable';

@Injectable()
export class PasswordResetRequestService extends CommonService {

  private resourceUrl = environment.apiBaseUrl + '/api/account/reset_password/init';

  constructor(private http: HttpClient) { super(); }

  sendPasswordResetRequest(email: string): Observable<string> {
    return this.http.post(this.resourceUrl, email);
  }
}
