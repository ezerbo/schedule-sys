import { Employee } from '../employee/employee';
import { TestSubcategory } from '../test-subcategory/testsubcategory';
import { Test } from '../test/test';
export class TestOccurrence {
  id: number;
  completionDate: Date;
  expirationDate: Date;
  employee: Employee = new Employee();
  test: Test = new Test();
  testSubcategory: TestSubcategory = new TestSubcategory();
  applicable = true;

  static toArray(jsons: any[]): TestOccurrence[] {
      const testOccurrences: TestOccurrence[] = [];
      if (jsons != null) {
          for (const json of jsons) {
              testOccurrences.push(new TestOccurrence(json));
          }
      }
      return testOccurrences;
   }

  constructor(json?: any) {
    if (json != null) {
      this.id = json.id;
      this.completionDate = json.completionDate != null ? new Date(json.completionDate) : undefined;
      this.expirationDate = json.expirationDate != null ? new Date(json.expirationDate) : undefined;
      this.employee = new Employee(json.employee);
      this.test = new Test(json.test);
      this.testSubcategory = new TestSubcategory(json.testSubcategory);
      this.applicable = json.applicable;
    }
  }
}
