import { Component, OnDestroy, OnInit, signal } from '@angular/core';
import { MessageModule } from 'primeng/message';
import { catchError, finalize, of, Subscription, tap } from 'rxjs';
import { CourseCardComponent } from "../../../../components/output/course-card/course-card.component";
import { UserDTO } from '../../../../dtos/user.dto';
import { RoleEnum } from '../../../../enums/roleEnum';
import { AuthService } from './../../../../services/auth.service';
import { PersistenceService } from './../../../../services/persistence.service';

@Component({
  selector: 'app-home-main',
  imports: [MessageModule, CourseCardComponent],
  host: { class: 'mt-3 block w-[80%] h-full' },
  templateUrl: './home-main.component.html',
  styleUrl: './home-main.component.scss'
})
export class HomeMainComponent implements OnInit, OnDestroy {

  constructor(public persistenceService: PersistenceService, private authService: AuthService) {
    this.subscriber = this.authService.getUser().pipe(tap(user => {
      this.userDTO.set(user);
      if (user.role !== RoleEnum.TEACHER) {
        this.searchCourses();
      } else {
        this.searchCreatedCourses();
      }

    })).subscribe();
  }

  courses = signal<any[]>([]);
  searchingCourses = signal(false);
  userDTO = signal<UserDTO>({} as UserDTO);
  private subscriber!: Subscription;

  ngOnInit(): void {
  }

  ngOnDestroy(): void {
    this.subscriber.unsubscribe();
  }

  searchCourses() {
    this.searchingCourses.set(true);
    this.persistenceService.getRequest("/v1/course/enrollment").pipe(tap((response: any) => {
      if (response.data.length > 0) {
        this.courses.set(response.data);
      }
    }), catchError(error => {
      this.courses.set([]);
      return of(error);
    }), finalize(() => {
      this.searchingCourses.set(false);
    })).subscribe();
  }

  searchCreatedCourses() {
    this.searchingCourses.set(true);
    this.persistenceService.getRequest("/v1/course/list/created").pipe(tap((response: any) => {
      if (response.data.length > 0) {
        this.courses.set(response.data);
      }
    }), catchError(error => {
      this.courses.set([]);
      return of(error);
    }), finalize(() => {
      this.searchingCourses.set(false);
    })).subscribe();
  }

  protected RoleEnum = RoleEnum;
}
