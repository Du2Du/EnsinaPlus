import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ResourceItemComponent } from './resource-item.component';

describe('ResourceItemComponent', () => {
  let component: ResourceItemComponent;
  let fixture: ComponentFixture<ResourceItemComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [ResourceItemComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(ResourceItemComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
