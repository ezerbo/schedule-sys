import { Component, OnInit, EventEmitter, Output, Input } from '@angular/core';

@Component({
  selector: 'app-dialog-crud',
  templateUrl: './dialog-crud.component.html',
  styleUrls: ['./dialog-crud.component.css']
})
export class DialogCrudComponent implements OnInit {

  @Input('disabled') disabled = true;
  @Output('cancel') cancel: EventEmitter<any> = new EventEmitter();

  constructor() { }

  ngOnInit() {
  }

  onCancel() {
    this.cancel.emit();
  }
}
