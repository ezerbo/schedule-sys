import { environment } from '../../environments/environment';
import { LoginService } from '../login/login.service';
import { CommonService } from '../shared/commonservice';
import { TestSubcategoryComponent } from '../test-subcategory/test-subcategory.component';
import { TestSubcategory } from '../test-subcategory/testsubcategory';
import { Test } from './test';
import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs/Observable';

@Injectable()
export class TestService extends CommonService {

  resourceUrl = environment.apiBaseUrl + '/api/tests';

  constructor(private http: HttpClient) { super(); }

  update(test: Test): Observable<{'message': string, 'result': Test}> {
    return this.http.put(this.resourceUrl, test)
      .map(response =>  {return {'message': 'Test successully saved', 'result': response as Test}})
      .catch(this.handleError);
  }

  getAll(page: number, size: number): Observable<{'result': Test[], 'count': number}>  {
    return this.http.get(this.resourceUrl + this.formatRequestParams(page, size), {observe: 'response'})
      .map(response => {
         return {'result': response.body as Test[], 'count': + response.headers.get(this.countHeaderName) }
      }).catch(this.handleError)
  }

  deleteOne(id: number): Observable<string> {
    return this.http.delete(this.resourceUrl + '/' + id)
       .map(response => 'Test successfully deleted')
       .catch(this.handleError);
  }

  getOne(id: string): Observable<Test> {
    return this.http.get(this.resourceUrl + '/' + id)
      .map(response => response as Test)
      .catch(this.handleError);
  }

  search(query: string): Observable<Test[]> {
   return this.http.get(this.resourceUrl + '/search' + this.formatSearchRequestParam(query))
      .map(response => response as Test[])
      .catch(this.handleError);
  }

  getAllSubcategories(id: number): Observable<TestSubcategory[]> {
    return this.http.get(this.resourceUrl + '/' + id + '/subcategories')
           .map(response => response as TestSubcategory[])
           .catch(this.handleError);
  }

  getOneByValue(value: string, fieldName?: string): Observable<Test> {
    return this.getOne(value);
  }

}
