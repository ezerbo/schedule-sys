import { CommonValidation } from '../shared/validation';
export class TestValidation extends CommonValidation {

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
      }
    }

    this.formErrors = {
      'name': '',
      'hasCompletionDate': '',
      'hasExpiryDate': '',
      'canBeWaived': ''
    }
  }
}
