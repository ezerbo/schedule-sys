export class Test {
  id: number;
  name: string;
  hasCompletionDate: boolean;
  hasExpiryDate: boolean;
  canBeWaived: boolean;

   static toArray(jsons: any[]): Test[] {
      const tests: Test[] = [];
      if (jsons != null) {
          for (const json of jsons) {
              tests.push(new Test(json));
          }
      }
      return tests;
   }

  constructor(json?: any) {
    if (json != null) {
      this.id = json.id;
      this.name = json.name;
      this.hasCompletionDate = json.hasCompletionDate;
      this.hasExpiryDate = json.hasExpiryDate;
      this.canBeWaived = json.canBeWaived;
    }
  }
}
