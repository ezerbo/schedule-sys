export class Preference {
  id: number;
  scheduleArchDays: number;

  constructor(json?: any) {
    if (json != null) {
        this.id = json.id;
        this.scheduleArchDays = json.scheduleArchDays;
    }
  }
}
