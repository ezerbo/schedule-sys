import { environment } from '../../environments/environment';
import { Schedule } from '../schedule/schedule';
import { CommonService } from '../shared/commonservice';
import { CareCompany } from './care-company';
import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs/Observable';

@Injectable()
export class CareCompanyService extends CommonService {

  private resourceUrl = environment.apiBaseUrl + '/api/care-companies';

  constructor(private http: HttpClient) {super(); }

  getAll(page: number, size: number, filters?: any): Observable<{'companies': CareCompany[], 'count': number}> {
    return this.http.get(this.resourceUrl + this.formatParams(page, size, filters), {observe: 'response'})
          .map(response =>  {
            return {'companies': CareCompany.toArray(response.body as CareCompany[]), 'count': response.headers.get(this.countHeaderName)}
            })
          .catch(this.handleError);
  }

  update(careCompany: CareCompany): Observable<{'result': CareCompany, 'message': string}> {
    return this.http.put(this.resourceUrl, careCompany)
          .map(response =>  {
              return {'result': new CareCompany(response), 'message': 'Care Company successfully saved'}
            })
  }

  deleteOne(id: number): Observable<string> {
    console.log(`Deleting care company with ID :  ${id}: `);
    return this.http.delete(this.resourceUrl + '/' + id)
          .map(response => {return 'Care Company successfully deleted'})
          .catch(this.handleError);
  }

  getOne(idOrname: string): Observable<CareCompany> {
    return this.http.get(this.resourceUrl + '/' + idOrname)
           .map(response => response as CareCompany)
           .catch(this.handleError);
  }

  getSchedules(id: number, page: number, size: number, archived: boolean): Observable<{'schedules': Schedule[], 'count': number}> {
    return this.http.get(this.resourceUrl + '/' + id + '/schedules' + this.formatRequestParams(page, size) + '&archived=' + archived,
       {observe: 'response'})
          .map(response => {
            return {'schedules': Schedule.toArray(response.body as Schedule[]), 'count': response.headers.get(this.countHeaderName)}
          }).catch(this.handleError);
  }

  getOneByValue(value: string, fieldName?: string): Observable<CareCompany> {
    return this.getOne(value);
  }

}
