import { environment } from '../../environments/environment';
import { LoginService } from '../login/login.service';
import { UserProfile } from '../profile/userprofile';
import { CommonService } from '../shared/commonservice';
import { Injectable, OnInit } from '@angular/core';
import {HttpClient} from '@angular/common/http';
import { Observable } from 'rxjs/Observable';

@Injectable()
export class UserService extends CommonService {

  private resourceUrl = environment.apiBaseUrl + '/api/users';

  constructor(private http: HttpClient) { super(); }


  create(user: UserProfile): Observable<{'result': UserProfile, 'message': string}> {
    return this.http.post(this.resourceUrl, user)
              .map(response =>  {
                return {'result': new UserProfile(response), 'message': 'User successfully created'}
              })
              .catch(this.handleError);
  }

  update(user: UserProfile): Observable<{'result': UserProfile, 'message': string}> {
    return this.http.put(this.resourceUrl, user)
              .map(response =>  {
                return {'result': new UserProfile(response), 'message': 'User successfully updated'}
              })
              .catch(this.handleError);
  }

  getAll(page: number, size: number): Observable<{'users': UserProfile[], 'count': number}> {
    return this.http.get(this.resourceUrl + this.formatRequestParams(page, size), {observe: 'response'})
              .map(response => {
                  return {'users': response.body as UserProfile[], 'count': response.headers.get(this.countHeaderName)}
              }).catch(this.handleError);
  }

  deleteUser(username: string): Observable<string> {
    return this.http.delete(this.resourceUrl + '/' + username)
            .map(response => 'User successfully deleted')
            .catch(this.handleError);
  }

  getUserByUsername(username: string): Observable<UserProfile> {
    return this.http.get(this.resourceUrl + '/' + username)
            .map(response => new UserProfile(response))
            .catch(this.handleError);
  }

  getUserByEmailAddress(emailAddress: string): Observable<UserProfile> {
    return this.http.get(this.resourceUrl + '/' + emailAddress + '?by=email')
            .map(response => new UserProfile(response))
            .catch(this.handleError);
  }

  getOneByValue(value: string, fieldName?: string): Observable<UserProfile> {
    return (fieldName === 'username' ) ? this.getUserByUsername(value) : this.getUserByEmailAddress(value);
  }

}
