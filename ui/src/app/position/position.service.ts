import { environment } from '../../environments/environment';
import { CommonService } from '../shared/commonservice';
import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs/Observable';
import { Position } from './position';

@Injectable()
export class PositionService extends CommonService {

  resourceUrl = environment.apiBaseUrl + '/api/positions';

  constructor(private http: HttpClient) {super(); }

  getAll(page: number, size: number): Observable<{'result': Position[], 'count': number}>;

  getAll(): Observable<Position[]>;

  getAll(page?: number, size?: number) {
    const pageable = (page != null && size != null);
    const result: Observable<any> =  pageable ?
      this.http.get(this.resourceUrl + this.formatRequestParams(page, size), {observe: 'response'}) :
        this.http.get(this.resourceUrl);
    return result.map(
        response => {
            return pageable ? {'result': response.body as Position[],
                   'count': + response.headers.get(this.countHeaderName)}
                   :  response as Position[]
        }
      ).catch(this.handleError);
  }

  update(position: Position): Observable<{'result': Position, 'message': string}> {
    return this.http.put(this.resourceUrl, position)
        .map(
          response => {
            return {'result': new Position(response), 'message': 'Position successfully saved' }
       }).catch(this.handleError);
  }

  deleteOne(id: number): Observable<String> {
    return this.http.delete(this.resourceUrl + '/' + id)
      .map(response => 'Position successfully deleted')
      .catch(this.handleError);
  }
}
