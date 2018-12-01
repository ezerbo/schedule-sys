import { CommonService } from './commonservice';
import { Directive, Input, Output, EventEmitter, HostListener } from '@angular/core';

@Directive({
  selector: '[appValueInUse]'
})
export class ValueInUseDirective {

  @Input() value;
  @Input() field_name;
  @Input() old_value;
  @Input() entity_service: CommonService;

  @Output() duplicate = new EventEmitter();

  @HostListener('blur') onBlur() {
  if (this.old_value !== this.value) {
      this.entity_service.getOneByValue(this.value, this.field_name)
        .subscribe(response => {
          this.duplicate.emit(
            {field: this.field_name, message: '\'' + this.value + '\' is already in use'}
           );
      })
    }
  }
}
