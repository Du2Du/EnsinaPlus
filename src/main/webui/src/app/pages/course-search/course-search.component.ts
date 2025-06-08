import { Component, OnInit, signal } from '@angular/core';
import { MainHeaderComponent } from "../../components/layout/main-header/main-header.component";
import { PersistenceService } from '../../services/persistence.service';
import { MessageModule } from 'primeng/message';
import { CourseCardComponent } from "../../components/output/course-card/course-card.component";
import { PaginatorModule } from 'primeng/paginator';
import { BlockUIModule } from 'primeng/blockui';
import { CourseDTO } from '../../dtos/course.dto';
import { MessageService } from 'primeng/api';
import { ToastModule } from 'primeng/toast';

@Component({
  selector: 'app-course-search',
  providers: [MessageService],
  imports: [MainHeaderComponent, ToastModule, MessageModule, BlockUIModule, PaginatorModule, CourseCardComponent],
  host: { class: 'w-full h-full flex flex-col items-center' },
  templateUrl: './course-search.component.html',
  styleUrl: './course-search.component.scss'
})
export class CourseSearchComponent implements OnInit {

  constructor(private persistenceService: PersistenceService, private messageService: MessageService) { }

  search = signal('');
  blockPage = signal(false);
  courses = signal<any[]>([]);
  page = 0;
  limit = 15;
  totalRecords = 0;

  ngOnInit(): void {
    this.search.set(location.pathname.split('search/').pop() ?? '');
    this.loadCourses();
  }


  private loadCourses() {
    this.blockPage.set(true);
    this.persistenceService.getRequest(`/v1/course/search?search=${this.search()}&page=${this.page}&limit=${this.limit}`)
      .subscribe((response: any) => {
        this.blockPage.set(false);
        this.courses.set(response.data ?? []);
        this.totalRecords = response.total ?? 0;
      });
  }

  searchCoursesEvent(search: string) {
    this.search.set(search);
    this.loadCourses();
  }

  onPageChange(event: any) {
    this.page = event.page;
  }

  enroll(course: CourseDTO) {
    this.blockPage.set(true);
    this.persistenceService.postRequest('/v1/course/enroll', {
      courseUuid: course.uuid
    }).subscribe((response: any) => {
      this.blockPage.set(false);
      course.matriculado = true;
      this.messageService.add({
        key: 'message',
        severity: 'success',
        summary: 'Matricula efetuada com sucesso!',
      })
    })
    this.loadCourses();
  }
}
