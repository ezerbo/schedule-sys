import { CareCompany } from '../company/care-company';
import { CareCompanyService } from '../company/care-company.service';
import { CommonComponent } from '../shared/common';
import { Contact } from './contact';
import { ContactValidation } from './contact-validation';
import { ContactService } from './contact.service';
import { Component, OnInit, AfterViewChecked, ViewChild, ChangeDetectorRef } from '@angular/core';
import { NgForm } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import * as _ from 'lodash';
import { MenuItem, ConfirmationService, Message, SelectItem, LazyLoadEvent } from 'primeng/primeng';

@Component({
  selector: 'app-contact',
  templateUrl: './contact.component.html',
  styleUrls: ['./contact.component.css']
})
export class ContactComponent extends CommonComponent implements OnInit, AfterViewChecked {

  @ViewChild('contactForm') contactForm: NgForm;
  careCompany: CareCompany;
  contacts: Contact[];
  selectedContact: Contact;
  contact: Contact;

  constructor(
    private careCompanyService: CareCompanyService,
    private route: ActivatedRoute,
    private router: Router,
    private contactService: ContactService,
    private confirmationService: ConfirmationService,
    private changeDetector: ChangeDetectorRef
   ) { super(new ContactValidation()); }

  ngOnInit() {
    this.route.params
      .map(params => params['companyId'])
      .subscribe(companyId => {
        this.careCompanyService.getOne(companyId)
            .subscribe(result => {
              this.careCompany = result,
              this.getAll(this.tableCurrentPage, this.tableCurrentRowCount, result.id);
            });
      });
    this.buildContextMenuItems();
    this.contact = new Contact();
  }

  ngAfterViewChecked(): void {

  }

  getAll(page: number, size: number, companyId: number) {
   this.tableDataLoading = true;
   this.contactService.getAll(page, size, companyId)
      .subscribe(response => {
        this.contacts = response.result;
        this.tableItemsCount = response.count;
        this.tableDataLoading = false;
        })
  }

   private buildContextMenuItems () {
    this.contextMenuItems = [
            { label: 'Edit'   , icon: 'fa-edit'  , command: (event) => {this.showDialog(true)}},
            { label: 'Delete' , icon: 'fa-close' , command: (event) => {this.deleteContact()} }
        ];
  }

  create() {
    this.contact.phoneNumber = this.unmaskNumber(this.contact.phoneNumber);
    this.contact.fax = this.unmaskNumber(this.contact.fax);
    this.contact.careCompany = this.careCompany;
    const result = this.editing ? this.contactService.update(this.contact)
      : this.contactService.create(this.contact);
    result.subscribe(
       response => {
         this.displayMessage({severity: 'success', summary: '', detail: response.message});
         if (!this.editing) {
            this.contacts.push(response.result);
            this.contacts = this.contacts.slice();
            this.changeDetector.markForCheck();
            // Update number of items so that the paginator displays the correct number of pages
            this.tableItemsCount++;
         }
         this.hideDialog();
       },
       error => {
        this.displayMessage({severity: 'error', summary: '', detail: error});
      }
     );
  }

  deleteContact () {
     this.confirmationService.confirm({
       message: 'Are you sure you want to delete this contact ?',
       accept: () => {
        this.contactService.deleteContact(this.selectedContact.id)
          .subscribe(
            response  => {
              this.displayMessage(
                { severity: 'success', summary: '', detail: 'Contact successfully deleted'});
                const selectedContactIndex = this.findSelectedContactIndex();
                this.contacts = this.contacts.filter((val, i) =>  i !== selectedContactIndex); // Refreshes dataTable
                this.selectedContact = undefined; // Disables 'Edit' and 'Delete' buttons
                // Update number of items so that the paginator displays the correct number of pages
                this.tableItemsCount--;
                if (this.contacts.length === 0) {
                  // Goes back to adjacent page when the last item in the list is deleted
                   this.getAll(this.tableCurrentPage - 1, this.tableCurrentRowCount, this.careCompany.id);
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
    // When editing, populate form with selected User
     this.contact = editing ? _.cloneDeep(this.selectedContact)
       : new Contact();
  }

   hideDialog() {
    this.dialogDisplayed = false;
    if (this.editing) {
      this.refreshOnEdit(this.contact, this.selectedContact) // Refresh dataTable after company update
    }
    this.contactForm.resetForm();
  }

   findSelectedContactIndex() {
     let index = -1;
     this.contacts.forEach((contact, i) => {
      if (this.selectedContact.id === contact.id) {
        index = i;
      }
     });
    return index;
  }

  goToCareCompanies() {
   this.router.navigate(['../companies', {'id': this.careCompany.id}], {relativeTo: this.route});
  }

}
