export class ScheduleStatus {
  id: number;
  name: string;

  static toArray(jsons: any[]): ScheduleStatus[] {
      const scheduleStatuses: ScheduleStatus[] = [];
      if (jsons != null) {
          for (const json of jsons) {
              scheduleStatuses.push(new ScheduleStatus(json));
          }
      }
      return scheduleStatuses;
   }

  constructor(json?: any) {
    if (json != null) {
      this.id = json.id;
      this.name = json.name;
    }
  }
}
