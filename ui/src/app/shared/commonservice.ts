import { Observable } from 'rxjs/Observable';

export abstract class CommonService {

  protected countHeaderName = 'X-Total-Count';

  getOneByValue(value: string, fieldName?: string): Observable<any> { return; }


  handleError(error: any) {
    let errorMsg = error.error ? error.error.message : '';
    if (error.status === 404) {
      errorMsg = 'Unable to find resource';
    } else if (error.status === 500) {
      const xScheduleSysError = error.headers.get('X-schedulesys-error');
      errorMsg = xScheduleSysError ? xScheduleSysError :
         (error.message ? error.message : error.toString());
    }
    console.log('An error occurred : ' + JSON.stringify(error));
    return Observable.throw(errorMsg);
  }

  formatRequestParams(page: number, size: number): string {
    return '?page=' + page + '&size=' + size;
  }

  formatParams(page: number, size: number, filters: any) {
    return filters === undefined ? this.formatRequestParams(page, size)
      : this.formatRequestParams(page, size) + filters;
  }

  formatSearchRequestParam(query: string): string {
    return '?query=' + query;
  }
}
