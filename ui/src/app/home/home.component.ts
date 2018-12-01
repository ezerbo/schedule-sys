import { AuthClaim } from '../login/authclaim';
import { LoginService } from '../login/login.service';
import { UserProfile } from '../profile/userprofile';
import { EventBusService } from '../shared/event-bus.service';
import { UserService } from '../user/user.service';
import { MenuBar } from './menuitems';
import { Component, OnInit } from '@angular/core';
import { Router, ActivatedRoute } from '@angular/router';
import { MenuItem } from 'primeng/primeng';
import * as _ from 'lodash';


@Component({
  selector: 'app-home',
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.css'],
  providers: [EventBusService]
})
export class HomeComponent implements OnInit {

  items: MenuItem[];
  profile: AuthClaim;
  user: UserProfile

  constructor(
    private loginService: LoginService,
    private userService: UserService,
    private eventBusService: EventBusService,
    private router: Router,
    private route: ActivatedRoute) { }

  ngOnInit() {
    this.profile = this.loginService.getAuthenticatedUser();
    this.getUser();
    this.items = new MenuBar(this.profile.auth === 'ROLE_ADMIN')
        .getMenuItems();
    this.eventBusService.userProfileUpdated$.subscribe(userProfile => this.user = _.cloneDeep(userProfile));
  }

  logout(): void {
     this.loginService.logout();
     this.navigateToLogin();
  }

  private navigateToLogin(): void {
    this.router.navigate(['']);
  }

  getUser() {
    this.userService.getUserByEmailAddress(this.profile.email)
      .subscribe(response => this.user = response);
  }

}
