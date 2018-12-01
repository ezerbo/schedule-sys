export class ScheduleCSVEntry {

  constructor(
    public filled_by: string,
    public emp_first_name: string,
    public emp_last_name: string,
    public schedule_date: string,
    public job: string,
    public status: string,
    public post_status: string,
    public start_time: string,
    public end_time: string,
    public tsr: string,
    public hours_worked: number,
    public overtime: number,
    public billed: string,
    public paid: string,
    public comment: string
  ) {}
}
