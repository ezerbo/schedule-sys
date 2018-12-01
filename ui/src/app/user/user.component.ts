import { AuthClaim } from '../login/authclaim';
import { LoginService } from '../login/login.service';
import { UserProfile } from '../profile/userprofile';
import { UserRole } from '../profile/userrole';
import { CommonComponent } from '../shared/common';
import { UserService } from './user.service';
import { UserRoleService } from './userrole.service';
import { Validation } from './validation';
import { Component, OnInit, ViewChild, AfterViewChecked, ChangeDetectorRef } from '@angular/core';
import { NgForm, FormBuilder, FormGroup } from '@angular/forms';
import { Router } from '@angular/router';
import { MenuItem, ConfirmationService, Message, SelectItem } from 'primeng/primeng';
import * as _ from 'lodash';
import { Observable } from 'rxjs/Observable';


@Component({
  selector: 'app-user',
  templateUrl: './user.component.html',
})
export class UserComponent extends CommonComponent implements OnInit, AfterViewChecked {

  @ViewChild('userForm')
  userForm: NgForm;
  users: UserProfile[];
  selectedUser: UserProfile;
  user: UserProfile;
  userRoles: SelectItem[] = [];
  authClaim: AuthClaim;
  editing: boolean; // Indicates whether a record is being edited in the list of users

  // TODO If updated user == logged in user, then logout user
// TODO Set selected user to undefined when using paginator
// TODO monitor token validity
  constructor(
      public userService: UserService,
      private confirmationService: ConfirmationService,
      private userRoleService: UserRoleService,
      private loginService: LoginService,
      private router: Router,
      private changeDetector: ChangeDetectorRef
    ) {
        super(new Validation());
        this.authClaim = this.loginService.getAuthenticatedUser();
  }

  ngAfterViewChecked(): void {
    if (this.dialogDisplayed) {
      this.formChanged(this.userForm);
    }
  }

  ngOnInit() {
    this.getAll(this.tableCurrentPage, this.tableCurrentRowCount);
    this.getAllUserRoles();
    this.buildContextMenuItems();
    this.user = new UserProfile();
  }

  getAll(page: number, size: number) {
    this.tableDataLoading = true;
    this.userService.getAll(page, size)
          .subscribe(
            response => {
              this.users = response.users;
              this.tableItemsCount = response.count;
              this.tableDataLoading = false;
            }, error => { this.users = [] }
         );
  }

  private getAllUserRoles() {
    this.userRoleService.getAll()
        .subscribe(
          response => {
            const component = this;
            response.forEach((userRole) => {
              component.userRoles.push({label: userRole.name, value: userRole.name});
            });
          });
  }

  /**
   * Update or Create a new ScheduleSys User
   */
  create() {
    const result = this.editing ? this.userService.update(this.user)
      : this.userService.create(this.user);
    result.subscribe(
      response => {
         this.displayMessage({ severity: 'success', summary: '', detail: response.message });
         if (!this.editing) {
           this.users.push(response.result);
           this.users = this.users.slice();
           this.changeDetector.markForCheck(); // Forces the change detector to run
           this.tableItemsCount ++;
         } else {
           this.refreshOnEdit(response.result, this.selectedUser);
         }
         this.showDialog(false, false);
        // this.loginService.logout();
      },
      error => {
         this.displayMessage({ severity: 'error', summary: '', detail: error });
      }
    );
  }

  deleteUser() {
    if (this.authClaim.sub === this.selectedUser.username) {
      this.displayMessage({ severity: 'error', summary: '',
        detail: 'Cannot delete account of authenticated user'});
      return;
    }
    this.confirmationService.confirm({
       message: 'Are you sure you want to delete this user ?',
       accept: () => {
        this.userService.deleteUser(this.selectedUser.username)
          .subscribe(
            response  => {
              this.displayMessage({ severity: 'success', summary: '', detail: 'User successfully deleted'});
                this.users = this.users.filter((val, i) =>  val.id !== this.selectedUser.id); // Refreshes dataTable
                this.selectedUser = undefined; // Disables 'Edit' and 'Delete' buttons
                // Update number of items so that the paginator displays the correct number of pages
                this.tableItemsCount--;
                if (this.users.length === 0) {
                  // Goes back to first page when the last item in the list is deleted
                  this.getAll(this.tableCurrentPage - 1, this.tableCurrentRowCount);
                }
            },
            error => {
                this.displayMessage({ severity: 'error', summary: '', detail: error});
            }
          );
       }
    });
  }

  showDialog(editing: boolean, display?: boolean) {
    this.dialogDisplayed = (display == null) ? true : display;
    this.editing = editing;
    this.userForm.resetForm();
    // When editing, populate form with selected User
    this.user = editing ? _.cloneDeep(this.selectedUser) : new UserProfile();
  }

  private buildContextMenuItems () {
    this.contextMenuItems = [
            { label: 'Edit'  , icon: 'fa-edit' , command: (event) => { this.editing = true; this.showDialog(true); }},
            { label: 'Delete', icon: 'fa-close', command: (event) => {this.deleteUser()}    }
        ];
  }

  gotToHome() {
    this.router.navigate(['../']);
  }

}
