import { environment } from '../../environments/environment';
import { LoginService } from '../login/login.service';
import { CareCompanyType } from './care-company-type';
import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs/Observable';

@Injectable()
export class CareCompanyTypeService {

  private resourceUrl = environment.apiBaseUrl + '/api/care-company-types'

  constructor(
    private http: HttpClient) { }

  getAll(): Observable<CareCompanyType[]> {
    return this.http.get(this.resourceUrl)
            .map(response => response as CareCompanyType[]);
  }

}
