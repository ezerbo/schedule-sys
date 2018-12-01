import { CommonComponent } from '../shared/common';
import { InsuranceCompanyService } from './insurance-company.service';
import { InsuranceCompany } from './insurancecompany';
import { Component, OnInit, ViewChild, ChangeDetectorRef } from '@angular/core';
import { NgForm } from '@angular/forms';
import { Router } from '@angular/router';
import { MenuItem, ConfirmationService, Message, SelectItem } from 'primeng/primeng';
import * as _ from 'lodash';

@Component({
  selector: 'app-insurance-company',
  templateUrl: './insurance-company.component.html',
  styleUrls: ['./insurance-company.component.css']
})
export class InsuranceCompanyComponent extends CommonComponent implements OnInit {

  insuranceCompanies: InsuranceCompany[] = [];
  insuranceCompany: InsuranceCompany;
  selectedInsuranceCompany: InsuranceCompany;
  displayDialog = false;
  editing = false;
  dialogMsgs: Message[] = [];
  @ViewChild('insuranceCompanyForm') insuranceCompanyForm: NgForm;

  constructor(
   private insuranceCompanyService: InsuranceCompanyService,
   private router: Router,
   private changeDetector: ChangeDetectorRef,
   private confirmationService: ConfirmationService
  ) { super(null); }

  ngOnInit() {
    this.insuranceCompany = new InsuranceCompany();
    this.getAll();
  }

  getAll() {
    this.insuranceCompanyService.getAll()
      .subscribe(response => {this.insuranceCompanies = response},
        error => {console.log('Something Unexpected Happenned')});
  }

  create() {
    this.insuranceCompanyService.update(this.insuranceCompany)
      .subscribe(
        response => {
          if (!this.editing) {
            this.insuranceCompanies.push(response.company);
            this.insuranceCompanies = this.insuranceCompanies.slice();
            this.changeDetector.markForCheck();
          } else {
            this.refreshOnEdit(response.company, this.selectedInsuranceCompany);
          }
          this.displayDialog = false;
          this.displayMessage({severity: 'success', summary: '', detail: response.message});
        },
        error => {
          this.displayMessage({severity: 'error', summary: '', detail: error}, this.dialogMsgs);
        }
      );
  }

  deleteInsuranceCompany() {
      this.confirmationService.confirm({
      message: 'Are you sure you want to delete this Company ?',
      accept: () => {
        this.insuranceCompanyService.deleteOne(this.selectedInsuranceCompany.id)
          .subscribe(
          response => {
            this.insuranceCompanies = this.insuranceCompanies.filter((val, i) => val.id !== this.selectedInsuranceCompany.id);
            this.selectedInsuranceCompany = undefined;
            this.displayMessage({severity: 'success', summary: '', detail: 'Company successfully deleted'});
          },
          error => {
            this.displayMessage({severity: 'error', summary: '', detail: error});
          }
          );
      }
    });
  }

  showOrHideDialog(editing: boolean) {
    this.editing = editing;
    this.displayDialog = !this.displayDialog;
    if (editing) {
      this.insuranceCompany = _.cloneDeep(this.selectedInsuranceCompany);
    } else {
      this.insuranceCompany = new InsuranceCompany();
      this.insuranceCompanyForm.resetForm();
    }
  }

  gotToHome() {
    this.router.navigate(['../']);
  }
}
