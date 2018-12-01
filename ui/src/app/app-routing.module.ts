
import { AccountComponent } from './account/account.component';
import { CareCompanyComponent } from './company/care-company.component';
import { ContactComponent } from './contact/contact.component';
import { DashboardComponent } from './dashboard/dashboard.component';
import { EmployeeDetailComponent } from './employee/employee-detail/employee-detail.component';
import { EmployeeComponent } from './employee/employee.component';
import { HomeComponent } from './home/home.component';
import { InsuranceCompanyComponent } from './insurance-company/insurance-company.component';
import { AuthGuard } from './login/auth-guard.service';
import { LoginComponent } from './login/login.component';
import { PageNotFoundComponent } from './pagenotfound/pagenotfound.component';
import { PasswordResetRequestComponent } from './passwordresetrequest/passwordresetrequest.component';
import { PositionComponent } from './position/position.component';
import { PreferenceComponent } from './preference/preference.component';
import { ProfileComponent } from './profile/profile.component';
import { ScheduleComponent } from './schedule/schedule.component';
import { ServiceRequestComponent } from './service-request/service-request.component';
import { TestDetailComponent } from './test/test-detail/test-detail.component';
import { TestComponent } from './test/test.component';
import { UserComponent } from './user/user.component';
import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';


const appRoutes: Routes = [
  { path: 'home', component: HomeComponent, canActivate: [AuthGuard],
    children: [
      { path: '', component: DashboardComponent },
      { path: 'profile', component: ProfileComponent },
      { path: 'users', component: UserComponent },
      { path: 'companies', component: CareCompanyComponent },
      { path: 'employees', component: EmployeeComponent },
      { path: 'employees/:employeeId', component: EmployeeDetailComponent },
      { path: 'contacts', component: ContactComponent },
      { path: 'tests', component: TestComponent },
      { path: 'positions', component: PositionComponent },
      { path: 'tests/:testId', component: TestDetailComponent },
      { path: 'schedules', component: ScheduleComponent },
      { path: 'service-requests', component: ServiceRequestComponent },
      { path: 'insurance-companies', component: InsuranceCompanyComponent },
      { path: 'preferences', component: PreferenceComponent }
    ]
  },
  { path: 'pwd-reset-request', component: PasswordResetRequestComponent },
  { path: 'account', component: AccountComponent },
  { path: '', component: LoginComponent },
  { path: '**', component: PageNotFoundComponent }
];

@NgModule({
  imports: [
    RouterModule.forRoot(appRoutes)
  ],
  exports: [
    RouterModule
  ]
})
export class AppRoutingModule {

}
