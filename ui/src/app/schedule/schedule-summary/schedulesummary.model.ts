export class ScheduleSummary {
  careCompanyId: number;
  careCompanyName: string;
  careCompanyType: string;
  shiftsScheduled: number;

  constructor(json?: any) {
    if (json != null) {
      this.careCompanyId = json.careCompanyId;
      this.careCompanyName = json.careCompanyName;
      this.careCompanyType = json.careCompanyType;
      this.shiftsScheduled = json.shiftsScheduled;
    }
  }
}
