import { environment } from '../../environments/environment';
import { LoginService } from '../login/login.service';
import { CommonService } from '../shared/commonservice';
import { TestSubcategory } from './testsubcategory';
import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs/Observable';

@Injectable()
export class TestSubcategoryService extends CommonService {

  resourceUrl = environment.apiBaseUrl + '/api/test-sub-categories';

  constructor(private http: HttpClient) { super(); }

  update(testSubcategory: TestSubcategory): Observable<TestSubcategory> {
    return this.http.put(this.resourceUrl, testSubcategory)
        .map(response => response as TestSubcategory)
        .catch(this.handleError);
  }

  deleteOne(id: number): Observable<string> {
    return this.http.delete(this.resourceUrl + '/' + id)
        .map(response => 'Test subcategory successfully deleted')
        .catch(this.handleError);
  }
}
