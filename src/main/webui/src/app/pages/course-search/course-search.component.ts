import { Component, OnDestroy, OnInit, signal } from '@angular/core';
import { MainHeaderComponent } from "../../components/layout/main-header/main-header.component";
import { PersistenceService } from '../../services/persistence.service';
import { MessageModule } from 'primeng/message';
import { CourseCardComponent } from "../../components/output/course-card/course-card.component";
import { PaginatorModule } from 'primeng/paginator';
import { BlockUIModule } from 'primeng/blockui';
import { CourseDTO } from '../../dtos/course.dto';
import { MessageService } from 'primeng/api';
import { ToastModule } from 'primeng/toast';
import { catchError, of, Subscription, tap } from 'rxjs';
import { ActivatedRoute } from '@angular/router';
import { UserDTO } from '../../dtos/user.dto';
import { AuthService } from '../../services/auth.service';

@Component({
  selector: 'app-course-search',
  providers: [MessageService, PersistenceService],
  imports: [MainHeaderComponent, ToastModule, MessageModule, BlockUIModule, PaginatorModule, CourseCardComponent],
  host: { class: 'w-full h-full flex flex-col items-center' },
  templateUrl: './course-search.component.html',
  styleUrl: './course-search.component.scss'
})
export class CourseSearchComponent implements OnInit, OnDestroy {

  constructor(private authService: AuthService, private route: ActivatedRoute, private persistenceService: PersistenceService, private messageService: MessageService) {
    this.subscriber = authService.getUser().subscribe(user => this.userData.set({ ...user }));
  }

  search = signal('');
  blockPage = signal(false);
  courses = signal<any[]>([]);
  page = 0;
  limit = 15; userData = signal<UserDTO>({} as UserDTO);
  totalRecords = 0;
  private subscriber: Subscription;

  ngOnInit(): void {
    this.route.params.subscribe(params => {
      this.search.set(params?.['search'] ?? '');
      this.loadCourses();
    });
  }

  ngOnDestroy(): void {
    this.subscriber.unsubscribe();
  }

  private loadCourses() {
    this.blockPage.set(true);
    this.persistenceService.getRequest(`/v1/course/search?search=${this.search()}&page=${this.page}&limit=${this.limit}`)
      .pipe(tap((response: any) => {
        this.blockPage.set(false);
        this.courses.set(response.data ?? []);
        this.totalRecords = response.total ?? 0;
      }),
        catchError((error: any) => {
          this.blockPage.set(false);
          this.courses.set([]);
          this.totalRecords = 0
          this.messageService.add({ severity: 'error', summary: error.error.title, key: 'message', detail: error.error.description });
          return of(error);
        })).subscribe();
  }

  searchCoursesEvent(search: string) {
    this.search.set(search);
    this.loadCourses();
  }

  onPageChange(event: any) {
    this.page = event.page;
    this.loadCourses();
  }

  enroll(course: CourseDTO) {
    this.blockPage.set(true);
    this.persistenceService.postRequest('/v1/course/enroll', {
      courseUuid: course.uuid
    })
      .pipe(tap((response: any) => {
        this.blockPage.set(false);
        this.courses().find(c => c.uuid === course.uuid)!.matriculado = true;
        this.messageService.add({
          key: 'message',
          severity: 'success',
          summary: 'Matricula efetuada com sucesso!',
        })
      }),
        catchError((error: any) => {
          this.blockPage.set(false);
          this.messageService.add({ severity: 'error', summary: error.error.title, key: 'message', detail: error.error.description });
          return of(error);
        })).subscribe();
    this.loadCourses();
  }
}
