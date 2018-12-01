export class ServiceRequestCSVEntry {
  constructor(
    public name: string,
    public phone_number: string,
    public email_address: string,
    public start_date: string,
    public end_date: string,
    public start_time: string,
    public end_time: string,
  ) {}
}
