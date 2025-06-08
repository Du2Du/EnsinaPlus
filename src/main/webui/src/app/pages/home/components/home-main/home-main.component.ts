import { Component, signal } from '@angular/core';
import { MessageModule } from 'primeng/message';
import { catchError, finalize, of, tap } from 'rxjs';
import { CourseCardComponent } from "../../../../components/output/course-card/course-card.component";
import { PersistenceService } from './../../../../services/persistence.service';

@Component({
  selector: 'app-home-main',
  imports: [MessageModule, CourseCardComponent],
  host: { class: 'mt-3 block w-[80%] h-full' },
  templateUrl: './home-main.component.html',
  styleUrl: './home-main.component.scss'
})
export class HomeMainComponent {

  constructor(public persistenceService: PersistenceService) { }

  courses = signal<any[]>([]);
  searchingCourses = signal(false);
  hasMoreItems = signal(true);
  currentPage = signal(0);

  ngOnInit(): void {
    this.searchCourses();
  }

  searchCourses() {
    this.searchingCourses.set(true);
    this.persistenceService.getRequest("/v1/course/enrollment").pipe(tap((response: any) => {
      if (response.data.length > 0) {
        this.courses.update(currentItems => [...currentItems, ...response.data]);
        this.currentPage.update(page => page + 1);
      } else {
        this.hasMoreItems.set(false);
      }
    }), catchError(error => {
      this.courses.set([]);
      return of(error);
    }), finalize(() => {
      this.searchingCourses.set(false);
    })).subscribe();
  }

  loadMore(): void {
    if (!this.hasMoreItems()) return;
    this.searchCourses();
  }
}
