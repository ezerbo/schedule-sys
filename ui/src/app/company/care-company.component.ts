import { InsuranceCompanyService } from '../insurance-company/insurance-company.service';
import { InsuranceCompany } from '../insurance-company/insurancecompany';
import { ScheduleType } from '../schedule/scheduletype';
import { CommonComponent } from '../shared/common';
import { FileServiceService } from '../shared/file-service.service';
import { PhoneNumberPipe } from '../shared/phonenumber.pipe';
import { CareCompany } from './care-company';
import { CareCompanyTypeService } from './care-company-type.service';
import { CareCompanyService } from './care-company.service';
import { CareCompanyCSVEntry } from './carecompanycsventry';
import { CareCompanyValidation } from './validation';
import { Component, OnInit, ViewChild, AfterViewChecked, ChangeDetectorRef } from '@angular/core';
import { NgForm } from '@angular/forms';
import { Router, ActivatedRoute, Params } from '@angular/router';
import 'rxjs/add/operator/switchMap';
import { MenuItem, ConfirmationService, Message, SelectItem, LazyLoadEvent } from 'primeng/primeng';
import * as _ from 'lodash';
import * as Papa from 'papaparse';
import { Observable } from 'rxjs/Observable';

@Component({
  selector: 'app-care-company',
  templateUrl: './care-company.component.html',
  styleUrls: ['./care-company.component.css']
})
export class CareCompanyComponent extends CommonComponent implements OnInit, AfterViewChecked {

  @ViewChild('companyForm') companyForm: NgForm;
  careCompanies: CareCompany[] = [];
  selectedCompany: CareCompany;
  careCompany: CareCompany;
  careCompanyTypes: SelectItem[] = [];

  insuranceCompanies: SelectItem[] = [];
  editAndDeleteDisabled = true;
  exportedFileName = 'care_companies.csv';

  constructor(
      public careCompanyService: CareCompanyService,
      private careCompanyTypeService: CareCompanyTypeService,
      private confirmationService: ConfirmationService,
      private insuranceCompanyService: InsuranceCompanyService,
      private router: Router,
      private route: ActivatedRoute,
      private changeDetector: ChangeDetectorRef,
      private fileService: FileServiceService,
      private phoneNumberPipe: PhoneNumberPipe
    ) {
        super(new CareCompanyValidation());
      }

  ngOnInit() {
    this.getAll(this.tableCurrentPage, this.tableCurrentRowCount);
    this.buildContextMenuItems();
    this.getAllCareCompanyTypes();
    this.getInsuranceCompanies();
    this.careCompany = new CareCompany();
    this.route.params
       .subscribe(params => {
         if (params['id'] != null) {
           this.careCompanyService.getOne(params['id'])
             .subscribe(result => {this.selectedCompany = result});
         }
       });
  }

  ngAfterViewChecked(): void {
    if (this.dialogDisplayed) {
      this.formChanged(this.companyForm);
    }
  }

  private buildContextMenuItems () {
    this.contextMenuItems = [
            { label: 'Edit'     , icon: 'fa-edit'          , command: (event) => { this.showDialog(true)}       },

            { label: 'Delete'   , icon: 'fa-close'         , command: (event) => { this.deleteCareCompany()}    },

            { label: 'Schedules', icon: 'fa-calendar'      , command: (event) => { this.navigateTo('../schedules',
            { id: this.selectedCompany.id, scheduleType: ScheduleType.COMPANY} )}},

            { label: 'Contacts' , icon: 'fa-address-book-o', command: (event) => {
               this.navigateTo('../contacts', {companyId: this.selectedCompany.id})} }
        ];
  }

  create() {
    this.careCompany.phoneNumber = this.unmaskNumber(this.careCompany.phoneNumber);
    this.careCompany.fax = this.unmaskNumber(this.careCompany.fax);
    this.careCompanyService.update(this.careCompany)
       .subscribe(response => {
         this.displayMessage({severity: 'success', summary: '', detail: response.message});
         if (!this.editing) {
            this.careCompanies.push(response.result);
            this.careCompanies = this.careCompanies.slice();
            // Update number of items so that the paginator displays the correct number of pages
            this.tableItemsCount++;
         } else {
           let careCompany: CareCompany = new CareCompany();
           this.refreshOnEdit(this.careCompany, careCompany) // Refresh dataTable after company update
           this.selectedCompany.insuranceCompany.name = this.careCompany.insuranceCompany.name === null ?
              'None' : this.careCompany.insuranceCompany.name;
           this.careCompanies = this.careCompanies.filter((val, i) =>  val.id !== this.selectedCompany.id);
           this.careCompanies.push(careCompany);
           this.careCompany = new CareCompany();
         }
         this.sortCompanies()
         this.changeDetector.markForCheck();
         this.hideDialog();
       },
       error => {
        this.displayMessage({severity: 'error', summary: '', detail: error});
      }
     );
  }

  sortCompanies() {
    this.careCompanies.sort((a, b) => (a.name > b.name) ? 1 : -1);
  }

  deleteCareCompany () {
     this.confirmationService.confirm({
       message: 'Are you sure you want to delete this company ?',
       accept: () => {
        this.careCompanyService.deleteOne(this.selectedCompany.id)
          .subscribe(
            response  => {
              this.displayMessage(
                { severity: 'success', summary: '', detail: 'User successfully deleted'});
                this.careCompanies = this.careCompanies.filter((val, i) =>  val.id !== this.selectedCompany.id); // Refreshes dataTable
                this.editAndDeleteDisabled = true; // Disables 'Edit' and 'Delete' buttons
                // Update number of items so that the paginator displays the correct number of pages
                this.tableItemsCount--;
                if (this.careCompanies.length === 0) {
                  // Goes back to adjacent page when the last item in the list is deleted
                  this.getAll(this.tableCurrentPage - 1, this.tableCurrentRowCount);
                }
            },
            error => {
                this.displayMessage({ severity: 'error', summary: '', detail: error });
            }
          );
       }
    });
  }

  showDialog(editing: boolean) {
     this.dialogDisplayed = true;
     this.editing = editing;
    if (editing) {
      this.careCompany = _.cloneDeep(this.selectedCompany);
    } else {
      this.careCompany = new CareCompany();
    }
  }

   hideDialog() {
    this.dialogDisplayed = false;
    this.companyForm.resetForm();
  }

  private getAllCareCompanyTypes() {
    this.careCompanyTypeService.getAll()
        .subscribe(
          response => {
            response.forEach((careCompanyType, i) => {
              this.careCompanyTypes.push({label: careCompanyType.name, value: careCompanyType.name});
            });
          });
  }

  getAll(page: number, size: number, params?: any, filters?: any) {
     this.tableDataLoading = true;
     this.careCompanyService.getAll(page, size, filters)
          .subscribe(response => {
              this.careCompanies = response.companies;
              this.tableItemsCount = response.count;
              this.tableDataLoading = false;
          }, error => {this.careCompanies = [] /*Prevents spinner from indefinitely spinning*/});
  }

  getInsuranceCompanies() {
    return this.insuranceCompanyService.getAll()
      .subscribe(response => {
        this.insuranceCompanies.push({label: 'None', value: 'None'});
        response.forEach(insuranceCompany => {
          this.insuranceCompanies.push({label: insuranceCompany.name, value: insuranceCompany.name});
        });
      });
  }

  exportCSV() {
    let careCompaniesCSVEntries = this.parseCareCompanies();
    let csvData = Papa.unparse(careCompaniesCSVEntries);
    this.fileService.saveFile(this.exportedFileName, csvData);
  }

  parseCareCompanies(): CareCompanyCSVEntry[] {
    let careCompaniesCSVEntries: CareCompanyCSVEntry[] = [];
    this.careCompanies.forEach(careCompany => {
      let phoneNumber: string = this.phoneNumberPipe.transform(careCompany.phoneNumber);
      let fax: string = this.phoneNumberPipe.transform(careCompany.fax);
      let companyType = careCompany.careCompanyType.name;
      let insuranceCompany = careCompany.insuranceCompany.name;
      let careCompanyCSVEntry: CareCompanyCSVEntry = new CareCompanyCSVEntry(
        careCompany.name, careCompany.address, phoneNumber, fax, companyType, insuranceCompany);
      careCompaniesCSVEntries.push(careCompanyCSVEntry);
    });
    return careCompaniesCSVEntries;
  }

  onRowSelect() {
    this.editAndDeleteDisabled = false;
  }

  navigateTo(destionation: string, navigationExtras: any) {
    this.router.navigate([destionation,  navigationExtras] , {relativeTo: this.route})
  }

  gotToHome() {
    this.router.navigate(['../']);
  }
}
