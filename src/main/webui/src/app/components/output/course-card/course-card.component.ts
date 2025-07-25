import { AfterViewInit, Component, ElementRef, input, output, signal, ViewChild } from '@angular/core';
import { CardModule } from 'primeng/card';
import { CourseDTO } from '../../../dtos/course.dto';
import { ButtonModule } from 'primeng/button';
import { TooltipModule } from 'primeng/tooltip';
import { Router } from '@angular/router';
import { UserDTO } from '../../../dtos/user.dto';
import { RoleEnum } from '../../../enums/roleEnum';

@Component({
  selector: 'app-course-card',
  imports: [CardModule, ButtonModule, TooltipModule],
  providers: [Router],
  templateUrl: './course-card.component.html',
  styleUrl: './course-card.component.scss'
})
export class CourseCardComponent implements AfterViewInit {

  constructor(private router: Router) { }

  course = input<CourseDTO>({} as CourseDTO);
  user = input<UserDTO>({} as UserDTO);
  matriculated = input(false);
  enroll = output<CourseDTO>();


  ngAfterViewInit(): void {
  }

  accessCourse() {
    this.router.navigateByUrl('/course/resume/' + this.course().uuid)
  }

  protected RoleEnum = RoleEnum;
}
