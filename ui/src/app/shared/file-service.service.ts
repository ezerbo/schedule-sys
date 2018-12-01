import { Injectable } from '@angular/core';
import { saveAs } from 'file-saver/FileSaver';

@Injectable()
export class FileServiceService {

  constructor() { }

  saveFile(filename: string, content: any) {
    const blob = new Blob([content], { type: 'text/csv' });
    saveAs(blob, filename);
  }

}
