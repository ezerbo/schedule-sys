import { Pipe, PipeTransform } from '@angular/core';

@Pipe({
  name: 'phonenumber'
})
export class PhoneNumberPipe implements PipeTransform {

  transform(value: any, args?: any): any {
    const valueAsString = value as string;
    return '(' + valueAsString.substr(0, 3) + ') ' + valueAsString.substr(3, 3) + ' - ' + valueAsString.substr(6, 4)
  }

}
