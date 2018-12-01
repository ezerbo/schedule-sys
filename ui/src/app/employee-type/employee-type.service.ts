import { environment } from '../../environments/environment';
import { CommonService } from '../shared/commonservice';
import { EmployeeType } from './employee-type';
import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs/Observable';

@Injectable()
export class EmployeeTypeService extends CommonService {

  resourceUrl = environment.apiBaseUrl + '/api/employee-types';

  constructor(private http: HttpClient) { super(); }


  getAll(page: number, size: number): Observable<{'result': EmployeeType[], 'count': number}> ;

  getAll(): Observable<EmployeeType[]> ;

  getAll(page?: number, size?: number) {
    const pageable = (page != null && size != null);
    const result: Observable<any> =  pageable ?
        this.http.get(this.resourceUrl + this.formatRequestParams(page, size), {observe: 'response'}) :
        this.http.get(this.resourceUrl);
    return result.map(
        response => {
            return pageable ? {'result': response.body as EmployeeType[],
                   'count': + response.headers.get(this.countHeaderName)} :  response as EmployeeType[]
        }
      ).catch(this.handleError);
  }

}
