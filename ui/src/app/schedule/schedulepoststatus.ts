export class SchedulePostStatus {
  id: number;
  name: string;

  static toArray(jsons: any[]): SchedulePostStatus[] {
      const schedulePostStatuses: SchedulePostStatus[] = [];
      if (jsons != null) {
          for (const json of jsons) {
              schedulePostStatuses.push(new SchedulePostStatus(json));
          }
      }
      return schedulePostStatuses;
   }

  constructor(json?: any) {
    if (json != null) {
      this.id = json.id;
      this.name = json.name;
    }
  }
}
