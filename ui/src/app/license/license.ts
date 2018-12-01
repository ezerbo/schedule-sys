import { Employee } from '../employee/employee';
import { LicenseType } from '../license-type/license.type';
export class License {
  id: number;
  licenseNumber: string;
  expiryDate: Date;
  employee: Employee = new Employee();
  licenseType: LicenseType = new LicenseType();

  static toArray(jsons: any[]): License[] {
      const licenses: License[] = [];
      if (jsons != null) {
          for (const json of jsons) {
              licenses.push(new License(json));
          }
      }
      return licenses;
   }

  constructor(json?: any) {
    if (json != null) {
      this.id = json.id;
      this.licenseNumber = json.licenseNumber;
      this.expiryDate = new Date(json.expiryDate);
      this.employee = new Employee(json.employee);
      this.licenseType = new LicenseType(json.licenseType);
    }
  }
}
