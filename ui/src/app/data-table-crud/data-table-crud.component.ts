import { Component, OnInit, Input, Output, EventEmitter } from '@angular/core';

@Component({
  selector: 'data-table-crud',
  templateUrl: './data-table-crud.component.html',
  styleUrls: ['./data-table-crud.component.css']
})
export class DataTableCrudComponent implements OnInit {

  @Input('disabled') disabled = true;
  @Output('new') newRecord: EventEmitter<any> = new EventEmitter();
  @Output('edit') edit = new EventEmitter();
  @Output('delete') deleteRecord: EventEmitter<any> = new EventEmitter();
  @Output('back') back = new EventEmitter();

  constructor() { }

  ngOnInit() {
  }

  onNewRecord() {
    this.newRecord.emit();
  }

  onEditRecord() {
    this.edit.emit();
  }

  onDeleteRecord() {
    this.deleteRecord.emit();
  }

  onBackNavigation() {
    this.back.emit();
  }

}
