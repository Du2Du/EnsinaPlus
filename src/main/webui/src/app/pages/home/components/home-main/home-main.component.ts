import { catchError, finalize, tap } from 'rxjs';
import { PersistenceService } from './../../../../services/persistence.service';
import { Component, signal } from '@angular/core';
import { MessageModule } from 'primeng/message';
import { CourseCardComponent } from "../../../../components/output/course-card/course-card.component";

@Component({
  selector: 'app-home-main',
  imports: [MessageModule, CourseCardComponent],
  host: { class: 'mt-3 block w-[50%] h-full' },
  templateUrl: './home-main.component.html',
  styleUrl: './home-main.component.scss'
})
export class HomeMainComponent {

  constructor(public persistenceService: PersistenceService) { }

  searchingCourses = signal(false);
  courses = signal([]);

  ngOnInit(): void {
    this.searchCourses();
  }

  searchCourses() {
    this.searchingCourses.set(true);
    this.persistenceService.getRequest("/v1/course/enrollment").pipe(tap((response: any) => {
      this.courses.set(response.data);
    }), catchError(error => {
      this.courses.set([]);
      return error;
    }), finalize(() => {
      this.searchingCourses.set(false);
    })).subscribe();
  }
}
