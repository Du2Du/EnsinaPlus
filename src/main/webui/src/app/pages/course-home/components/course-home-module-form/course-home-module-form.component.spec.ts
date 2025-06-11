import { ComponentFixture, TestBed } from '@angular/core/testing';

import { CourseHomeModuleFormComponent } from './course-home-module-form.component';

describe('CourseHomeModuleFormComponent', () => {
  let component: CourseHomeModuleFormComponent;
  let fixture: ComponentFixture<CourseHomeModuleFormComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [CourseHomeModuleFormComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(CourseHomeModuleFormComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
