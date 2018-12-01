import { LoginService } from './login.service';
import { LoginVM } from './loginvm';
import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { Message } from 'primeng/primeng';


/**
 * Manages Users authentication
 */
@Component({
  selector: 'app-login',
  templateUrl: './login.component.html'
})
export class LoginComponent implements OnInit {

  loginVM: LoginVM = new LoginVM();
  msgs: Message[] = [];

  constructor(
    private loginService: LoginService,
    private router: Router) {}

  ngOnInit(): void {
    if (this.loginService.isAuthenticated()) {
      // User is authenticated, redirecting to home
      this.navigateToHome();
    }
  }

   /**
   * Authenticate a user
   */
   login() {
      this.loginService.login(this.loginVM)
        .subscribe(res => {
              this.navigateToHome();
              console.log('Successfully logged in');
            }
            , error => {
                this.msgs = [];
                this.msgs.push({
                   severity: 'warn',
                   summary: 'Something unexpected occurred',
                   detail: 'Username or password invalid'});
            });
  }

  /**
   * Handles rememberMe button events
   */
  onRememberMeChange() {
    this.loginVM.rememberMe = !this.loginVM.rememberMe;
    console.log('Updated rememberMe : ' + this.loginVM.rememberMe);
  }

  private navigateToHome(): void {
    this.router.navigate(['home']);
  }

  // TODO Add method to reset all control in login form

}
