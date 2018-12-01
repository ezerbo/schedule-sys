import { Employee } from '../employee/employee';
export class PhoneNumber {
  id: number;
  phoneNumber: string;
  label: string;
  numberType: string;
  employee: Employee;

  static toArray(jsons: any[]): PhoneNumber[] {
      const numbers: PhoneNumber[] = [];
      if (jsons != null) {
          for (const json of jsons) {
              numbers.push(new PhoneNumber(json));
          }
      }
      return numbers;
   }

  constructor(json?: any) {
    if (json != null) {
      this.id = json.id;
      this.phoneNumber = json.phoneNumber;
      this.label = json.label;
      this.numberType = json.numberType;
      this.employee = (json.employee === undefined)
        ? null : new Employee(json.employee);
    }
  }
}
