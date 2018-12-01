import { CommonComponent } from '../shared/common';
import { PositionService } from './position.service';
import { Component, OnInit, ViewChild, ChangeDetectorRef } from '@angular/core';
import { Router } from '@angular/router';
import { Position } from './position';
import { NgForm } from '@angular/forms';
import { MenuItem, ConfirmationService, Message, SelectItem } from 'primeng/primeng';
import * as _ from 'lodash';

@Component({
  selector: 'app-position',
  templateUrl: './position.component.html',
  styleUrls: ['./position.component.css']
})
export class PositionComponent extends CommonComponent implements OnInit {

  positions: Position[];
  position: Position;
  selectedPosition: Position;
  dialogMsgs: Message[] = [];
  editing = false;
  displayDialog = false;
  @ViewChild('positionForm') positionForm: NgForm;

  constructor(
    private router: Router,
    private positionService: PositionService,
    private confirmationService: ConfirmationService,
    private changeDetector: ChangeDetectorRef
  ) { super(null); }

  ngOnInit() {
    this.positionService.getAll()
      .subscribe(response => this.positions = response);
    this.position = new Position();
  }

  create() {
    this.positionService.update(this.position)
      .subscribe(
        response => {
          if (!this.editing) {
            this.positions.push(response.result);
            this.positions = this.positions.slice();
            this.changeDetector.markForCheck();
          } else {
            this.refreshOnEdit(response.result, this.selectedPosition);
          }
          this.displayDialog = false;
          this.displayMessage({severity: 'success', summary: '', detail: response.message});
        },
        error => {
          this.displayMessage({severity: 'error', summary: '', detail: error}, this.dialogMsgs);
        }
      );
  }

  deleteOne() {
     this.confirmationService.confirm({
      message: 'Are you sure you want to delete this license ?',
      accept: () => {
        this.positionService.deleteOne(this.selectedPosition.id)
          .subscribe(
          response => {
            this.displayMessage({ severity: 'success', summary: '', detail: 'Position successfully deleted'});
            this.positions = this.positions.filter((val, i) => val.id !== this.selectedPosition.id);
            this.selectedPosition = undefined;
          },
          error => {
            this.displayMessage({ severity: 'error', summary: '', detail: error });
          }
          );
      }
    });
  }

  showOrHideDialog(editing: boolean) {
    this.editing = editing;
    this.displayDialog = !this.displayDialog;
    if (editing) {
      this.position = _.cloneDeep(this.selectedPosition);
    } else {
      this.position = new Position();
      this.positionForm.resetForm();
    }
  }

  gotToHome() {
    this.router.navigate(['../']);
  }

}
