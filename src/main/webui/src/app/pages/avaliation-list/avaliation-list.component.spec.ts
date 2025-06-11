import { ComponentFixture, TestBed } from '@angular/core/testing';

import { AvaliationListComponent } from './avaliation-list.component';

describe('AvaliationListComponent', () => {
  let component: AvaliationListComponent;
  let fixture: ComponentFixture<AvaliationListComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [AvaliationListComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(AvaliationListComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
