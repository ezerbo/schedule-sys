import { CommonComponent } from '../shared/common';
import { PasswordResetRequestService } from './passwordresetrequest.service';
import { Component, OnInit, ViewChild } from '@angular/core';
import { NgForm } from '@angular/forms';
import { Router } from '@angular/router';
import {Message} from 'primeng/primeng';

@Component({
  selector: 'app-passwordresetrequest',
  templateUrl: './passwordresetrequest.component.html',
  styleUrls: ['./passwordresetrequest.component.css']
})
export class PasswordResetRequestComponent extends CommonComponent implements OnInit {

  email = '';
  msgs: Message[] = [];
  @ViewChild('resetForm') resetForm: NgForm

  constructor(
    private passwordResetRequestService: PasswordResetRequestService,
    private router: Router
  ) { super(null); }

  ngOnInit() {
  }

  sendResetRequest() {
    this.msgs = [];
    this.passwordResetRequestService.sendPasswordResetRequest(this.email)
        .subscribe(
          response => {
               this.msgs.push({severity: 'success',
                   summary: '',
                   detail: 'Password reset email successfully sent. Please check your emails'});
            this.resetForm.resetForm('');
          },
          error => {
                this.msgs.push({severity: 'error',
                   summary: 'Something unexpected occurred',
                   detail: 'Unable to send reset email. No account found with email address \'' + this.email + '\''});
          });
  }

  /**
   * Handles password reset request cancellation
   */
  onCancelBtnClick() {
    this.router.navigate(['']);
  }

}
