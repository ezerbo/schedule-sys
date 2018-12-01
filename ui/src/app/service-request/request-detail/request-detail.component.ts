import { ServiceRequest } from '../servicerequest';
import { Component, OnInit, EventEmitter, Output, Input } from '@angular/core';

@Component({
  selector: 'app-request-detail',
  templateUrl: './request-detail.component.html',
  styleUrls: ['./request-detail.component.css']
})
export class RequestDetailComponent implements OnInit {

  @Input()
  serviceRequest: ServiceRequest;

  @Output()
  back = new EventEmitter();

  constructor() { }

  ngOnInit() {
  }

  onBackBtnClick() {
    this.back.emit();
  }
}
