import { ComponentFixture, TestBed } from '@angular/core/testing';

import { AvaliationCardComponent } from './avaliation-card.component';

describe('AvaliationCardComponent', () => {
  let component: AvaliationCardComponent;
  let fixture: ComponentFixture<AvaliationCardComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [AvaliationCardComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(AvaliationCardComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
