import { CareCompany } from '../company/care-company';

export class Contact {
  id: number;
  firstName: string;
  lastName: string;
  phoneNumber: string;
  fax: string;
  title: string;
  careCompany: CareCompany = new CareCompany();

  static toArray(jsons: any[]): Contact[] {
        const contacts: Contact[] = [];
        if (jsons != null) {
            for (const json of jsons) {
                contacts.push(new Contact(json));
            }
        }
        return contacts;
  }

  constructor(json?: any) {
    if (json != null) {
      this.id = json.id;
      this.firstName = json.firstName;
      this.lastName = json.lastName;
      this.phoneNumber = json.phoneNumber;
      this.fax = json.fax;
      this.title = json.title;
      this.careCompany = json.careCompany;
    }
  }
}
