import { environment } from '../../environments/environment';
import { LoginService } from '../login/login.service';
import { UserRole } from '../profile/userrole';
import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs/Observable';

@Injectable()
export class UserRoleService {

  private resourceUrl = environment.apiBaseUrl + '/api/user-roles';

  constructor(private http: HttpClient) { }

  getAll(): Observable<UserRole[]> {
    return this.http.get(this.resourceUrl)
              .map(response => {
                  return response as UserRole[]
              }).catch(this.handleError);
  }

  handleError(error: any) {
   const errMsg = error.headers.get('X-schedulesys-error');
   return Observable.throw(errMsg);
  }

}
