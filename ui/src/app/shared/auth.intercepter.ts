import { LoginService } from '../login/login.service';
import {Injectable, Injector} from '@angular/core';
import {HttpEvent, HttpInterceptor, HttpHandler, HttpRequest, HttpResponse, HttpErrorResponse} from '@angular/common/http';
import { Router } from '@angular/router';
import { Observable } from 'rxjs/Observable';

@Injectable()
export class AuthInterceptor implements HttpInterceptor {

  constructor(private injector: Injector, private router: Router) {}

  intercept(req: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {
    const loginService = this.injector.get(LoginService);
    const authHeader = loginService.getAuthorizationHeader();
    const isAuthHeaderNeeded = !(req.url.includes('/account') || req.url.includes('/authenticate'));
    let authReq = null;
    if (isAuthHeaderNeeded) {
      if (loginService.isAuthenticated()) {
        authReq = req.clone({headers: req.headers.set('Authorization', authHeader), responseType: 'text'})
      } else {
        console.log('JWT expired');
        loginService.logout();
        this.router.navigate(['']);
      }
    } else {
      authReq = req.clone({responseType: 'text'});
    }

    return next.handle(authReq)
        .map((event: HttpEvent<any>) => {
          if (event instanceof HttpResponse) {
            return event.clone({ body: JSON.parse(event.body), });
          }
        })
        .catch((error: HttpErrorResponse) => {
          const parsedError = Object.assign({}, error, {error: JSON.parse(error.error)});
          return Observable.throw(new HttpErrorResponse(parsedError));
      });

  }

}
