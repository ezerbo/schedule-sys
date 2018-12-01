export class ServiceRequest {
  id: number;
  name: string;
  phoneNumber: string
  emailAddress: string
  ccc: boolean;
  lis: boolean;
  bad: boolean;
  aw: boolean;
  lh: boolean;
  mp: boolean;
  laundry: boolean;
  se: boolean;
  mr: boolean;
  lt: boolean;
  ccg: boolean;
  ae: boolean;
  cpp: boolean;
  prlc: boolean;
  startDate: Date;
  endDate: Date;
  startTime: Date;
  endTime: Date;
  requestDate: Date;
  comment: string;

   static toArray(jsons: any[]): ServiceRequest[] {
      const serviceRequests: ServiceRequest[] = [];
      if (jsons != null) {
          for (const json of jsons) {
              serviceRequests.push(new ServiceRequest(json));
          }
      }
      return serviceRequests;
  }

  constructor(json?: any) {
    if (json != null) {
      this.id = json.id;
      this.name = json.name;
      this.phoneNumber = json.phoneNumber;
      this.emailAddress = json.emailAddress;
      this.ccc = json.ccc;
      this.lis = json.lis;
      this.bad = json.bad;
      this.aw = json.aw;
      this.lh = json.lh;
      this.mp = json.mp;
      this.laundry = json.laundry;
      this.se = json.se;
      this.mr = json.mr;
      this.lt = json.lt;
      this.ccg = json.ccg;
      this.ae = json.ae;
      this.cpp = json.cpp;
      this.prlc = json.prlc;
      this.startDate = new Date(json.startDate);
      this.endDate = new Date(json.endDate);
      this.startTime = new Date(json.startTime);
      this.endTime = new Date(json.endTime);
      this.requestDate = new Date (json.requestDate);
      this.comment = json.comment;
    }
  }
}
