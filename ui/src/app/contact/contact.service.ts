import { environment } from '../../environments/environment';
import { LoginService } from '../login/login.service';
import { CommonService } from '../shared/commonservice';
import { Contact } from './contact';
import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs/Observable';

@Injectable()
export class ContactService extends CommonService {

  private companyResourceUrl = environment.apiBaseUrl + '/api/care-companies';
  private contactResourceUrl = environment.apiBaseUrl + '/api/contacts';

  constructor(private http: HttpClient) { super(); }

  getAll(page: number, size: number, companyId: number): Observable<{'result': Contact[], 'count': number}> {
    return this.http.get(this.companyResourceUrl + '/' + companyId + '/contacts' + this.formatRequestParams(page, size),
        {observe: 'response'})
            .map(response => {return {'result': response.body as Contact[], 'count': response.headers.get(this.countHeaderName)}})
            .catch(this.handleError);
  }

  create(contact: Contact): Observable<{'result': Contact, 'message': string}> {
    return this.http.post(this.contactResourceUrl, contact)
          .map(response => {
            return {'result': response as Contact, 'message': 'Contact successfully created'}
          })
          .catch(this.handleError);
  }

  update(contact: Contact): Observable<{'result': Contact, 'message': string}> {
    return this.http.put(this.contactResourceUrl, contact)
          .map(response => {
            return {'result': response as Contact, 'message': 'Contact successfully updated'}
          })
          .catch(this.handleError);
  }

  deleteContact(id: number): Observable<string> {
    return this.http.delete(this.contactResourceUrl + '/' + id)
          .map(response => 'Contact successfully deleted')
          .catch(this.handleError);
  }

}
