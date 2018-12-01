import { environment } from '../../environments/environment.prod';
import { CommonService } from '../shared/commonservice';
import { ServiceRequest } from './servicerequest';
import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs/Observable';

@Injectable()
export class ServiceRequestService extends CommonService {

  private resourceUrl = environment.apiBaseUrl + '/api/service-requests';

  constructor(private http: HttpClient) { super(); }

  update(serviceRequest: ServiceRequest): Observable<ServiceRequest> {
    return this.http.put(this.resourceUrl, serviceRequest)
      .map(response => new ServiceRequest(response))
      .catch(this.handleError);
  }

  getAll(): Observable<ServiceRequest[]> {
    return this.http.get(this.resourceUrl)
      .map(response => ServiceRequest.toArray(response as ServiceRequest[]))
      .catch(this.handleError);
  }

  getOne(id: number): Observable<ServiceRequest> {
    return this.http.get(this.resourceUrl + '/' + id)
       .map(response => new ServiceRequest(response))
       .catch(this.handleError);
  }

  deleteOne(id: number): Observable<string> {
    return this.http.delete(this.resourceUrl + '/' + id)
      .map(response => 'Service Request successfully deleted')
      .catch(this.handleError);
  }
}
