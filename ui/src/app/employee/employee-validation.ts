import { CommonValidation } from '../shared/validation';
export class EmployeeValidation extends CommonValidation {

  constructor() {
    super();
    this.initMessages();
  }

  initMessages() {
    this.validationMessages = {
      'name': {
        'required': 'Name is required',
        'minlength': 'Name must be at least 2 characters long',
        'name-in-use': 'Name provided is already in use'
      },
      'address': {
        'required': 'Address is required'
      },
      'phoneNumber': {
        'required': 'Phone Number is required'
      },
      'fax': {
        'required': 'Fax is required'
      },
      'careCompanyType': {
        'required': 'Compay Type is required'
      }
    }

    this.formErrors = {
      'name': '',
      'address': '',
      'phoneNumber': '',
      'fax': '',
      'careCompanyType': ''
    }
  }
}
