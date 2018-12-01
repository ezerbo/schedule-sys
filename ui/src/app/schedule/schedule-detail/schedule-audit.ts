import { UserProfile } from '../../profile/userprofile';
export class ScheduleAudit {
  id: number;
  scheduleSysUser: UserProfile;
  updateDate: Date;

  constructor(json?: any) {
    if (json != null) {
      this.id = json.id;
      this.scheduleSysUser = new UserProfile(json.scheduleSysUser);
      this.updateDate = new Date(json.updateDate);
    }
  }
}
