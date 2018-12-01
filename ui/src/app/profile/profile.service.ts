import { environment } from '../../environments/environment';
import { LoginService } from '../login/login.service';
import { CommonService } from '../shared/commonservice';
import { UserProfile } from './userprofile';
import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';

import { Observable } from 'rxjs/Observable';

@Injectable()
export class ProfileService extends CommonService {

  resourceBaseUrl = environment.apiBaseUrl + '/api/users/';
  // TODO Redirect back to login when token expires
  constructor(
    private http: HttpClient,
    private loginService: LoginService
  ) { super(); }

  getUserProfile(): Observable<UserProfile> {
    const authClaim = this.loginService.getAuthenticatedUser();
    return this.http.get(
        this.resourceBaseUrl + authClaim.sub,
      ).map(response => response as UserProfile)
  }

  update(userProfile: UserProfile): Observable<string> {
    return this.http.put(this.resourceBaseUrl, userProfile)
        .map(response => 'Profile successfully updated')
        .catch(error => this.handleError(error));
  }

  getUserByUsername(username: string): Observable<UserProfile> {
    return this.http.get(this.resourceBaseUrl + username)
            .map(response => new UserProfile(response))
            .catch(this.handleError);
  }

  getUserByEmailAddress(emailAddress: string): Observable<UserProfile> {
    return this.http.get(this.resourceBaseUrl + emailAddress + '?by=email')
            .map(response => new UserProfile(response))
            .catch(this.handleError);
  }

  getOneByValue(value: string, fieldName?: string): Observable<UserProfile> {
    return (fieldName === 'username' ) ? this.getUserByUsername(value) : this.getUserByEmailAddress(value);
  }

}
