import { CommonComponent } from '../../shared/common';
import { TestSubcategoryComponent } from '../../test-subcategory/test-subcategory.component';
import { Test } from '../test';
import { TestService } from '../test.service';
import { Component, OnInit, ViewChild } from '@angular/core';
import { Router, ActivatedRoute } from '@angular/router';
import { MenuItem, ConfirmationService, Message, SelectItem } from 'primeng/primeng';

@Component({
  selector: 'app-test-detail',
  templateUrl: './test-detail.component.html',
  styleUrls: ['./test-detail.component.css']
})
export class TestDetailComponent extends CommonComponent implements OnInit {

  test: Test;

  @ViewChild(TestSubcategoryComponent)
  subcategoryComponent: TestSubcategoryComponent;

  constructor(
    private router: Router,
    private route: ActivatedRoute,
    private testService: TestService) { super(null); }

  ngOnInit() {
      this.route.params
      .map(params => params['testId'])
      .subscribe(testId => {
        this.testService.getOne(testId)
          .subscribe(
            result => {
              this.test = result;
          });
      });
  }

  onTabOpen(e) {
    if (this.subcategoryComponent.testSubcategories.length === 0) {
      this.subcategoryComponent.getAll();
    }
  }

  onEntityEvent(e: Message) {
    this.displayMessage(e);
  }

  onBackClick(): void {
    this.router.navigate(['../../tests', { 'id': this.test.id }], { relativeTo: this.route });
  }

}
