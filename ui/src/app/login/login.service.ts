import { environment } from '../../environments/environment';
import { AuthClaim } from './authclaim';
import { JWT } from './jwt';
import { LoginVM } from './loginvm';
import { Injectable } from '@angular/core';

import { HttpClient} from '@angular/common/http';
import { Observable } from 'rxjs/Observable';
import 'rxjs/Rx';
import * as Moment from 'moment';

@Injectable()
export class LoginService {

  // private options = new RequestOptions({ headers: new Headers([{ 'Content-Type': 'application/json' }, {}]) });
   resourceUrl = environment.apiBaseUrl + '/api/authenticate';

  constructor(private http: HttpClient) { }

  login(loginVM: LoginVM): Observable<boolean> {
     return this.http.post(this.resourceUrl, loginVM)
      .map(response => this.storeJwt( new JWT(response), loginVM.rememberMe))
      .catch(this.handleError);
  }

  logout() {
    this.removeJwt();
  }

  storeJwt(jwt: JWT, rememberMe: boolean): boolean {
    if (rememberMe) {
      localStorage.setItem('jwt', jwt.token);
    }else {
      sessionStorage.setItem('jwt', jwt.token);
    }
    return true;
  }

  removeJwt() {
     console.log('Logging out user');
     localStorage.removeItem('jwt');
     sessionStorage.removeItem('jwt');
  }

  isAuthenticated(): boolean {
    const authClaim = this.getAuthenticatedUser();
    if (authClaim == null) {
      return false;
    }
    const now = new Date().getTime() / 1000; // 'exp' is in seconds
    return authClaim.exp > now;
  }

  /**
   * Gets authenticated user from jwt
   */
  getAuthenticatedUser() {
    let authClaim: AuthClaim;
    const jwt = this.getJwt();
    if (jwt != null) {
      const jwtArray = jwt.split('.', 3);
       authClaim = JSON.parse(atob(jwtArray[1]));
    }
    return authClaim;
  }

  private getJwt(): string {
    let jwt = localStorage.getItem('jwt');
    if (jwt === null) {
      jwt = sessionStorage.getItem('jwt');
    }
    return jwt;
  }


  getAuthorizationHeader(): string {
    const jwt = this.getJwt();
    return 'Bearer ' + jwt;
  }

  handleError(error: any) {
   const errMsg = (error.status === 401) ? 'Invalid username or password'
     : 'An unexpected error occurred';
   console.error(errMsg); // log to console instead
   return Observable.throw(errMsg);
  }

}
