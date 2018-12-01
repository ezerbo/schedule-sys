import { CommonValidation } from './validation';
import { NgForm } from '@angular/forms';
import { MenuItem, ConfirmationService, Message, SelectItem, LazyLoadEvent } from 'primeng/primeng';

/**
 * Base class for all components managing data tables
 */
export class CommonComponent {

  editing: boolean;
  dialogDisplayed: boolean;
  validation: CommonValidation;
  msgs: Message[] = [];
  contextMenuItems: MenuItem[];
  tableItemsCount: number;
  tableCurrentPage = 0;
  tableCurrentRowCount = 10;
  tableDataLoading = false;
  phoneNumberMask =  ['(', /[1-9]/, /\d/, /\d/, ')', ' ', /\d/,
     /\d/, /\d/, '-', /\d/, /\d/, /\d/, /\d/];

  constructor (validation: CommonValidation) {
    this.validation = validation;
  }

  /**
   * Handles changes in entity forms
   */
  protected formChanged(entityForm: NgForm) {
    entityForm.valueChanges
          .subscribe(data => {
             for (const field in this.validation.formErrors) {
               this.validation.formErrors[field] = '';
               const control = entityForm.form.get(field);
               if (control && control.dirty && !control.valid) {
                  const messages = this.validation.validationMessages[field];
                  for (const key in control.errors) {
                      this.validation.formErrors[field] += messages[key] + ' ';
                  }
              }
            }
          });
  }

  /**
   * Presents a notification to the user and removes it after 3 seconds
   */
  protected displayMessage(msg: Message, msgs?: Message[]): void {
      if (msgs === undefined) {
          msgs = this.msgs;
      }
      msgs.pop(); // Remove previous messages
      msgs.push(msg);
      setTimeout(() => {msgs.pop()}, 3000); // Messages do not disappear automatically
  }
  /**
   *  angular2-text-mask does not provide the user's input without the mask
   *  This method removes the mask.
   */
  protected unmaskNumber(value): string {
    return (value === null) ? '' : value.replace(/\D+/g, '');
  }

 /**
 * Pushes error in errors array on when duplicates are detected in users input
 */
 onDuplicates(event: any) {
    this.validation.formErrors[event.field] = event.message;
  }

  loadDataLazy(event: LazyLoadEvent, params?: any) {
    this.tableCurrentPage = (event.first / event.rows);
    this.tableCurrentRowCount = event.rows;
    let filterParams = this.parseFilterParams(event);
    this.getAll(this.tableCurrentPage, this.tableCurrentRowCount, params, filterParams);
  }

  protected getAll(page: number, size: number, params?: any, filters?: any) {}

  parseFilterParams(event: LazyLoadEvent): string {
    let params = '';
    for (let key in event.filters) {
      params += '&' + key + '=' + event.filters[key].value;
    }
    return params;
  }

  refreshOnEdit(source: any, destination: any) {
    for (const key in source) {
      if (typeof source[key] === 'object' && !(source[key] instanceof Date)) {
        this.refreshOnEdit(source[key], destination[key])
      } else {
        destination[key] = source[key];
      }
    }
  }

   markFormPristine(form: NgForm): void {
    Object.keys(form.controls).forEach(control => {
      console.log(control)
        form.controls[control].markAsPristine();
    });
  }
}
