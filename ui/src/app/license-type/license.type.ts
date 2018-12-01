export class LicenseType {
  id: number;
  name: string;

   static toArray(jsons: any[]): LicenseType[] {
      const licenseTypes: LicenseType[] = [];
      if (jsons != null) {
          for (const json of jsons) {
              licenseTypes.push(new LicenseType(json));
          }
      }
      return licenseTypes;
   }

  constructor(json?: any) {
    if (json != null) {
      this.id = json.id;
      this.name = json.name;
    }
  }
}
