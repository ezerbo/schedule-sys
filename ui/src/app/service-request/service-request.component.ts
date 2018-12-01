import { CommonComponent } from '../shared/common';
import { FileServiceService } from '../shared/file-service.service';
import { PhoneNumberPipe } from '../shared/phonenumber.pipe';
import { ServiceRequestService } from './service-request.service';
import { ServiceRequest } from './servicerequest';
import { ServiceRequestCSVEntry } from './servicerequestcsventry';
import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { DatePipe } from '@angular/common';
import * as Papa from 'papaparse';

@Component({
  selector: 'app-service-request',
  templateUrl: './service-request.component.html',
  styleUrls: ['./service-request.component.css']
})
export class ServiceRequestComponent extends CommonComponent implements OnInit {

  serviceRequests: ServiceRequest[] = [];
  selectedServiceRequest: ServiceRequest;
  showRequestDetail = false;
  exportedFileName = 'service_requests.csv';

  constructor(
    private serviceRequestService: ServiceRequestService,
    private router: Router,
    private fileService: FileServiceService,
    private datePipe: DatePipe,
    private phoneNumberPipe: PhoneNumberPipe
  ) { super(null); }

  ngOnInit() {
    this.getAll();
  }

  getAll() {
    this.serviceRequestService.getAll()
      .subscribe(response => {this.serviceRequests = response});
  }

  onRowDblClick() {
    this.changeDisplayPreference();
  }

  onBackBtnClick() {
    this.changeDisplayPreference();
  }

  private changeDisplayPreference() {
    this.showRequestDetail = !this.showRequestDetail;
  }

  exportCSV() {
    let serviceRequestCSVEntries = this.parseServiceRequests();
    let csvData = Papa.unparse(serviceRequestCSVEntries);
    this.fileService.saveFile(this.exportedFileName, csvData);
  }

  parseServiceRequests(): ServiceRequestCSVEntry[] {
    let serviceRequestCSVEntries: ServiceRequestCSVEntry[] = [];
    this.serviceRequests.forEach(serviceRequest => {
      let startDate = this.datePipe.transform(serviceRequest.startDate, 'shortDate');
      let endDate = this.datePipe.transform(serviceRequest.endDate, 'shortDate');
      let startTime = this.datePipe.transform(serviceRequest.startTime, 'shortTime');
      let endTime = this.datePipe.transform(serviceRequest.endTime, 'shortTime');
      let phoneNumber = this.phoneNumberPipe.transform(serviceRequest.phoneNumber);
      let employeeCSVEntry: ServiceRequestCSVEntry = new ServiceRequestCSVEntry(
        serviceRequest.name, phoneNumber, serviceRequest.emailAddress, startDate, endDate,
        startTime, endTime);
      serviceRequestCSVEntries.push(employeeCSVEntry);
    });
    return serviceRequestCSVEntries;
  }

  gotToHome() {
    this.router.navigate(['../']);
  }

}
