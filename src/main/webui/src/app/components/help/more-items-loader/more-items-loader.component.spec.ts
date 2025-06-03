import { ComponentFixture, TestBed } from '@angular/core/testing';

import { MoreItemsLoaderComponent } from './more-items-loader.component';

describe('MoreItemsLoaderComponent', () => {
  let component: MoreItemsLoaderComponent;
  let fixture: ComponentFixture<MoreItemsLoaderComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [MoreItemsLoaderComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(MoreItemsLoaderComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
