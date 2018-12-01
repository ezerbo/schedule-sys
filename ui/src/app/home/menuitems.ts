/**
 * ToolBar
 */
import {Injectable} from '@angular/core';
import {MenuItem} from 'primeng/primeng';

export class MenuBar {


  constructor(public isAdmin: boolean) {}

  getMenuItems(): MenuItem[] {
    return [
      {
        label: 'Home', icon: 'fa-home', routerLink: './'
      },
      {
        label: 'Companies', icon: 'fa-hospital-o', routerLink: 'companies'
      },
      {
        label: 'Employees', icon: 'fa-user-md', routerLink: 'employees'
      },
      {
        label: 'Tests', icon: 'fa-book', routerLink: 'tests'
      },
      {
        label: 'Positions', icon: 'fa-medkit', routerLink: 'positions'
      },
      {
        label: 'Service Requests', icon: 'fa-ambulance', routerLink: 'service-requests'
      },
      {
        label: 'Insurance Companies', icon: 'fa-info', routerLink: 'insurance-companies'
      },
      {
        label: 'Users', icon: 'fa-users', routerLink: 'users', disabled: !this.isAdmin
      },
      {
        label: 'Preferences', icon: 'fa-wrench', routerLink: 'preferences'
      }
    ];
  }
}
