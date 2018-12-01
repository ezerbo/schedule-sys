import { CommonComponent } from '../shared/common';
import { Preference } from './preference';
import { PreferenceService } from './preference.service';
import { Component, OnInit } from '@angular/core';
import { Message } from 'primeng/primeng';

@Component({
  selector: 'app-preference',
  templateUrl: './preference.component.html',
  styleUrls: ['./preference.component.css']
})
export class PreferenceComponent extends CommonComponent implements OnInit {

  preference: Preference;

  constructor(private preferenceService: PreferenceService) { super(null); }

  ngOnInit() {
    this.preferenceService.getOne()
      .subscribe(response => this.preference = response);
  }

  save() {
    this.preferenceService.save(this.preference)
      .subscribe(response => {this.displayMessage({severity: 'success', detail: '', summary: 'Schedule preferences saved successfully'})}
        , error => {this.displayMessage({severity: 'error', detail: '', summary: 'Unable to save preferences'})});
  }

}
