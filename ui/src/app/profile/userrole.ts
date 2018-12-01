export class UserRole {
  id: number;
  name: string;

  static toArray(jsons: any[]): UserRole[] {
        const users: UserRole[] = [];
        if (jsons != null) {
            for (const json of jsons) {
                users.push(new UserRole(json));
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
