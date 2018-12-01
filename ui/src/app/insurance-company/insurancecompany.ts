export class InsuranceCompany {
  id: number;
  name: string;

  static toArray(jsons: any[]): InsuranceCompany[] {
      const insuranceCompanies: InsuranceCompany[] = [];
      if (jsons != null) {
          for (const json of jsons) {
              insuranceCompanies.push(new InsuranceCompany(json));
          }
      }
      return insuranceCompanies;
  }

  constructor(json?: any) {
    if (json != null) {
      this.id = json.id;
      this.name = json.name;
    } else {
       this.name = 'None';
    }
  }
}
