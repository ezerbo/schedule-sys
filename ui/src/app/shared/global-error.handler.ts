import { LoginService } from '../login/login.service';
import { Injectable, ErrorHandler, Injector } from '@angular/core';
import { Router } from '@angular/router';
import { Observable } from 'rxjs/Observable';

@Injectable()
export class GlobalErrorHandler implements ErrorHandler {

  constructor(
    private injector: Injector) { }

  handleError(error: any) {
    const loginService = this.injector.get(LoginService);
    const router = this.injector.get(Router);
    if (!loginService.isAuthenticated()) {
      // 401 and jwt expired then redirect to login
      console.log('jwt expired, redirecting to login page');
      router.navigate(['']);
    }
  }
}
