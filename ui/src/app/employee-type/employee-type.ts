export class EmployeeType {
  id: number;
  name: string;

  static toArray(jsons: any[]): EmployeeType[] {
      const employeeTypes: EmployeeType[] = [];
      if (jsons != null) {
          for (const json of jsons) {
              employeeTypes.push(new EmployeeType(json));
          }
      }
      return employeeTypes;
   }

  constructor(json?: any) {
    if (json != null) {
      this.id = json.id;
      this.name = json.name;
    }
  }
}
