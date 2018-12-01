import { CommonValidation } from '../shared/validation';
export class ContactValidation extends CommonValidation {

  constructor() {
    super();
    this.initMessages();
  }

  initMessages() {
    this.validationMessages = {
      'firstName': {
        'required': 'First Name is required',
        'minlength': 'First Name must be at least 2 characters long'
      },
      'lastName': {
        'required': 'Last Name is required',
        'minlength': 'Last Name must be at least 2 characters long'
      },
      'title': {
        'required': 'Title is required',
        'minlength': 'Title must be at least 2 characters long'
      },
      'fax': {
        'required': 'Fax is required'
      },
      'phoneNumber': {
        'required': 'Phone Number is required'
      }
    }

    this.formErrors = {
      'firstName': '',
      'lastName': '',
      'title': '',
      'fax': '',
      'phoneNumber': ''
    }
  }
}
