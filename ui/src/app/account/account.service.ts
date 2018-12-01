import { environment } from '../../environments/environment';
import { KeyAndPasswordVM } from './keyandpasswordvm';
import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs/Observable';

@Injectable()
export class AccountService {

  pwdResetUrl = environment.apiBaseUrl + '/api/account/reset_password/finish';
  acntActivationUrl = environment.apiBaseUrl + '/api/account/activate';

  constructor(private http: HttpClient) { }

  resetPassword(keyAndPassword: KeyAndPasswordVM): Observable<string> {
    return this.http.post(this.pwdResetUrl, keyAndPassword)
            .map(response => 'Password successfully reset');
  }

  activateAccount(keyAndPassword: KeyAndPasswordVM): Observable<string> {
     return this.http.post(this.acntActivationUrl, keyAndPassword)
            .map(response => 'Account successfully activated');
  }

}
