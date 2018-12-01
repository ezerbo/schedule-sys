/**
 * Validation messages for user form
 */
import { CommonValidation } from '../shared/validation';
export class Validation extends CommonValidation {

  constructor() { super(); this.initMessages() }

  private initMessages(): void {
      this.validationMessages = {
        'username': {
           'required': 'Username is required',
           'minlength': 'Username must be at least 6 characters long',
           'regex': 'Invalid username'
         },

        'email': {
           'required': 'Email address is required',
           'emailAddressInUse': 'Email address entered is already in use',
           'regex': 'Invalid email address'
         },

        'firstname': {
           'required': 'First Name is required',
           'minlength': 'First Name must be at leat 2 characters long'
         },

        'lastname': {
           'required': 'Last name is required',
           'minlength': 'Last Name must be at least 2 characters long'
        },

        'role': {
           'required': 'Role is required'
        }
       }

    // No errors when the form hasn't been touched
    this.formErrors = {
      'username': '',
      'email': '',
      'firstname': '',
      'lastname': '',
      'role': ''
    }
  }
}
