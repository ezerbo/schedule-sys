export class EmployeeCSVEntry {
  constructor(
    public first_name: string,
    public last_name: string,
    public date_of_hire: string,
    public last_day_of_work: string,
    public ebc: string,
    public active: string,
    public position: string,
    public employee_type: string
  ) {}
}
