import { environment } from '../../environments/environment.prod';
import { CommonService } from '../shared/commonservice';
import { InsuranceCompany } from './insurancecompany';
import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs/Observable';

@Injectable()
export class InsuranceCompanyService extends CommonService {

  private resourceUrl = environment.apiBaseUrl + '/api/insurance-companies';

  constructor(
    private http: HttpClient
  ) { super(); }

  update(insuranceCompany: InsuranceCompany): Observable<{'company': InsuranceCompany, 'message': string}> {
    return this.http.put(this.resourceUrl, insuranceCompany)
      .map(response => {return {'company': new InsuranceCompany(response), 'message': 'Insurance Company Successfully Saved'}})
      .catch(this.handleError);
  }

  getAll(): Observable<InsuranceCompany[]> {
    return this.http.get(this.resourceUrl)
      .map(response => response as InsuranceCompany[])
      .catch(this.handleError);
  }

  getOne(id: number): Observable<InsuranceCompany> {
    return this.http.get(this.resourceUrl + '/' + id)
      .map(response => new InsuranceCompany(response))
      .catch(this.handleError);
  }

  deleteOne(id: number): Observable<string> {
    return this.http.delete(this.resourceUrl + '/' + id)
      .map(response => 'Insurance Company Successfully Deleted')
      .catch(this.handleError);
  }

}
