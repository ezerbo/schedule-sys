import { Test } from '../test/test';
export class TestSubcategory {
  id: number;
  name: string;
  test: Test = new Test();

  constructor(json?: any) {
    if (json != null) {
      this.id = json.id;
      this.name = json.name;
      this.test = new Test(json.test);
    }
  }
}
