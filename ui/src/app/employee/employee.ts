import { EmployeeType } from '../employee-type/employee-type';
import { Position } from '../position/position';
export class Employee {
  id: number;
  firstName: string;
  lastName: string;
  dateOfHire: Date;
  lastDayOfWork: Date;
  ebc: boolean;
  insvc: boolean;
  comment: string;
  employeeType: EmployeeType = new EmployeeType();
  position: Position = new Position();

  static toArray(jsons: any[]): Employee[] {
      const employees: Employee[] = [];
      if (jsons != null) {
          for (const json of jsons) {
              employees.push(new Employee(json));
          }
      }
      return employees;
   }

  constructor(json?: any) {
    if (json != null) {
      this.id = json.id;
      this.firstName = json.firstName;
      this.lastName = json.lastName;
      this.dateOfHire = json.dateOfHire !== null ? new Date(json.dateOfHire) : null;
      this.lastDayOfWork = json.lastDayOfWork !== null ? new Date(json.lastDayOfWork) : null;
      this.ebc = json.ebc;
      this.insvc = json.insvc;
      this.comment = json.comment;
      this.employeeType = json.employeeType;
      this.position = json.position;
    }
  }
}
