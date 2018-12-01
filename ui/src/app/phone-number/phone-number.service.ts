import { environment } from '../../environments/environment';
import { LoginService } from '../login/login.service';
import { CommonService } from '../shared/commonservice';
import { PhoneNumber } from './phone-number';
import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs/Observable';

@Injectable()
export class PhoneNumberService extends CommonService {

  resourceUrl = environment.apiBaseUrl + '/api/phone-numbers';

  constructor(private http: HttpClient) {super(); }

  update(phoneNumber: PhoneNumber): Observable<PhoneNumber> {
      return this.http.put(this.resourceUrl, phoneNumber)
          .map(response => response as PhoneNumber)
          .catch(this.handleError);
  }

  deleteOne(id: number) {
     return this.http.delete(this.resourceUrl + '/' + id)
          .map(response => {return 'Phone Number successfully deleted'})
          .catch(this.handleError);
  }
}
