import { environment } from '../../environments/environment';
import { CommonService } from '../shared/commonservice';
import { LicenseType } from './license.type';
import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs/Observable';

@Injectable()
export class LicenseTypeService extends CommonService {

  resourceUrl = environment.apiBaseUrl + '/api/license-types'
  constructor(
    private http: HttpClient) { super(); }

  getAll(): Observable<LicenseType[]> {
    return this.http.get(this.resourceUrl)
        .map(response => response as LicenseType[])
        .catch(this.handleError);
  }
}
