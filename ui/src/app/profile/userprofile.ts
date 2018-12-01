import { UserRole } from './userrole';
export class UserProfile {
  id: number;
  emailAddress: string;
  firstName: string;
  lastName: string;
  username: string;
  userRole: UserRole = new UserRole();
  activated: boolean;

  constructor(json?: any) {
    if (json != null)  {
      this.id = json.id;
      this.emailAddress = json.emailAddress;
      this.firstName = json.firstName;
      this.lastName = json.lastName;
      this.username = json.username;
      this.userRole = json.userRole;
      this.activated = json.activated;
    }
  }

}
