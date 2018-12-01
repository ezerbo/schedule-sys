import { AppRoutingModule } from './app-routing.module';
import { BrowserModule } from '@angular/platform-browser';
import { NgModule, ErrorHandler } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { HttpModule } from '@angular/http';
import {HTTP_INTERCEPTORS} from '@angular/common/http';
import { HttpClientModule } from '@angular/common/http';

import { PanelModule, ButtonModule, InputTextModule, CheckboxModule,
         InputSwitchModule, MessagesModule, MenubarModule,
         ScheduleModule, DataTableModule, SharedModule, AccordionModule,
         ContextMenuModule, ConfirmDialogModule, ConfirmationService,
         GrowlModule, DialogModule, DropdownModule, StepsModule, AutoCompleteModule,
         CalendarModule, ToggleButtonModule, InputTextareaModule, FieldsetModule, SpinnerModule
} from 'primeng/primeng';

import { TextMaskModule } from 'angular2-text-mask';
import { MaterialModule, MdNativeDateModule } from '@angular/material';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';

import { AppComponent } from './app.component';
import { LoginService } from './login/login.service';
import { PasswordResetRequestComponent } from './passwordresetrequest/passwordresetrequest.component';
import { LoginComponent } from './login/login.component';
import { PageNotFoundComponent } from './pagenotfound/pagenotfound.component';
import { PasswordResetRequestService } from './passwordresetrequest/passwordresetrequest.service';
import { AccountComponent } from './account/account.component';
import { AccountService } from './account/account.service';
import { CareCompanyTypeService } from './company/care-company-type.service';
import { HomeComponent } from './home/home.component';
import { AuthGuard } from './login/auth-guard.service';
import { DashboardComponent } from './dashboard/dashboard.component';
import { ProfileComponent } from './profile/profile.component';
import { ProfileService } from './profile/profile.service';
import { GlobalErrorHandler } from './shared/global-error.handler';
import { InputRegexDirective } from './shared/input-regex.directive';
import { UserComponent } from './user/user.component';
import { UserService } from './user/user.service';
import { UserRoleService } from './user/userrole.service';
import { CareCompanyComponent } from './company/care-company.component';
import { CareCompanyService } from './company/care-company.service';
import { PhoneNumberPipe } from './shared/phonenumber.pipe';
import { ContactComponent } from './contact/contact.component';
import { ContactService } from './contact/contact.service';
import { DataTableCrudComponent } from './data-table-crud/data-table-crud.component';
import { DialogCrudComponent } from './dialog-crud/dialog-crud.component';
import { EmployeeComponent } from './employee/employee.component';
import { PositionComponent } from './position/position.component';
import { EmployeeTypeComponent } from './employee-type/employee-type.component';
import { EmployeeTypeService } from './employee-type/employee-type.service';
import { EmployeeService } from './employee/employee.service';
import { PhoneNumberService } from './phone-number/phone-number.service';
import { PositionService } from './position/position.service';
import { EmployeeDetailComponent } from './employee/employee-detail/employee-detail.component';
import { LicenseTypeService } from './license-type/license-type.service';
import { LicenseService } from './license/license.service';
import { PhoneNumberComponent } from './phone-number/phone-number.component';
import { LicenseComponent } from './license/license.component';
import { TestComponent } from './test/test.component';
import { ScheduleComponent } from './schedule/schedule.component';
import { ValueInUseDirective } from './shared/value-in-use.directive';
import { TestService } from './test/test.service';
import { TestDetailComponent } from './test/test-detail/test-detail.component';
import { TestOccurrenceComponent } from './test-occurrence/test-occurrence.component';
import { TestOccurrenceService } from './test-occurrence/test-occurrence.service';
import { TestSubcategoryComponent } from './test-subcategory/test-subcategory.component';
import { TestSubcategoryService } from './test-subcategory/test-subcategory.service';
import { CompanyScheduleComponent } from './schedule/company-schedule/company-schedule.component';
import { SchedulePostStatusService } from './schedule/schedule-post-status.service';
import { ScheduleStatusService } from './schedule/schedule-status.service';
import { ScheduleService } from './schedule/schedule.service';
import { ScheduleSummaryComponent } from './schedule/schedule-summary/schedule-summary.component';
import { ScheduleSummaryService } from './schedule/schedule-summary/schedule-summary.service';
import { AuthInterceptor } from './shared/auth.intercepter';
import { ScheduleDetailComponent } from './schedule/schedule-detail/schedule-detail.component';
import { ServiceRequestComponent } from './service-request/service-request.component';
import { ServiceRequestService } from './service-request/service-request.service';
import { RequestDetailComponent } from './service-request/request-detail/request-detail.component';
import { InsuranceCompanyComponent } from './insurance-company/insurance-company.component';
import { InsuranceCompanyService } from './insurance-company/insurance-company.service';
import { EmployeeScheduleComponent } from './schedule/employee-schedule/employee-schedule.component';
import { PreferenceComponent } from './preference/preference.component';
import { PreferenceService } from './preference/preference.service';
import { FileServiceService } from './shared/file-service.service';
import { DatePipe } from '@angular/common';


@NgModule({
  declarations: [
    AppComponent,
    PasswordResetRequestComponent,
    LoginComponent,
    PageNotFoundComponent,
    AccountComponent,
    HomeComponent,
    DashboardComponent,
    ProfileComponent,
    UserComponent,
    InputRegexDirective,
    PhoneNumberPipe,
    CareCompanyComponent,
    ContactComponent,
    DataTableCrudComponent,
    DialogCrudComponent,
    EmployeeComponent,
    PositionComponent,
    EmployeeTypeComponent,
    EmployeeDetailComponent,
    PhoneNumberComponent,
    LicenseComponent,
    TestComponent,
    ScheduleComponent,
    ValueInUseDirective,
    TestDetailComponent,
    TestSubcategoryComponent,
    TestOccurrenceComponent,
    CompanyScheduleComponent,
    ScheduleSummaryComponent,
    ScheduleDetailComponent,
    ServiceRequestComponent,
    RequestDetailComponent,
    InsuranceCompanyComponent,
    EmployeeScheduleComponent,
    PreferenceComponent
  ],
  imports: [
    BrowserModule,
    FormsModule,
    HttpModule,
    HttpClientModule,
    PanelModule,
    InputSwitchModule,
    ButtonModule,
    InputTextModule,
    MaterialModule,
    MessagesModule,
    MenubarModule,
    ScheduleModule,
    DataTableModule,
    SharedModule,
    ContextMenuModule,
    ConfirmDialogModule,
    GrowlModule,
    DialogModule,
    DropdownModule,
    TextMaskModule,
    StepsModule,
    CalendarModule,
    ToggleButtonModule,
    InputTextareaModule,
    FieldsetModule,
    AccordionModule,
    BrowserAnimationsModule,
    AutoCompleteModule,
    SpinnerModule,
    CheckboxModule,
    AppRoutingModule
  ],
  providers: [
    LoginService,
    AccountService,
    AuthGuard,
    ProfileService,
    UserService,
    ConfirmationService,
    UserRoleService,
    CareCompanyService,
    CareCompanyTypeService,
    ContactService,
    EmployeeService,
    EmployeeTypeService,
    PositionService,
    PhoneNumberService,
    LicenseTypeService,
    LicenseService,
    TestService,
    PasswordResetRequestService,
    TestSubcategoryService,
    TestOccurrenceService,
    ScheduleService,
    ScheduleStatusService,
    SchedulePostStatusService,
    ScheduleSummaryService,
    ServiceRequestService,
    InsuranceCompanyService,
    PreferenceService,
    FileServiceService,
    DatePipe,
    PhoneNumberPipe,
    {
      provide: HTTP_INTERCEPTORS,
      useClass: AuthInterceptor,
      multi: true
    }],
  bootstrap: [AppComponent]
})
export class AppModule { }
