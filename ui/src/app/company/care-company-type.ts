export class CareCompanyType {

  id: number;
  name: string;

  static toArray(jsons: any[]): CareCompanyType[] {
        const users: CareCompanyType[] = [];
        if (jsons != null) {
            for (const json of jsons) {
                users.push(new CareCompanyType(json));
            }
        }
        return users;
  }

  constructor(json?: any) {
    if (json != null) {
      this.id = json.id;
      this.name = json.name;
    }
  }
}
