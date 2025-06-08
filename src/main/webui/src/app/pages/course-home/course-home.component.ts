import { Component, OnInit, signal } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { MessageService } from 'primeng/api';
import { BlockUIModule } from 'primeng/blockui';
import { ToastModule } from 'primeng/toast';
import { catchError, of, tap } from 'rxjs';
import { MainHeaderComponent } from "../../components/layout/main-header/main-header.component";
import { PersistenceService } from '../../services/persistence.service';
import { CourseDTO } from '../../dtos/course.dto';

@Component({
  selector: 'app-course-home',
  host: {class: ''},
  imports: [MainHeaderComponent, ToastModule, BlockUIModule],
  providers: [MessageService, PersistenceService],
  templateUrl: './course-home.component.html',
  styleUrl: './course-home.component.scss'
})
export class CourseHomeComponent implements OnInit {

  constructor(private route: ActivatedRoute, private persistenceService: PersistenceService, private messageService: MessageService) {
  }

  blockPage = signal(false);
  course = signal<CourseDTO>({} as CourseDTO);

  ngOnInit(): void {
    this.route.params.subscribe(params => {
      const courseId = params['uuid'];
      this.loadCourseInfo(courseId);
    });
  }

  loadCourseInfo(courseId: string): void {
    this.persistenceService.getRequest('/v1/course/' + courseId)
      .pipe(tap((response: any) => {
        this.blockPage.set(false);
        this.course.set(response.data);
        console.log(this.course());
      }),
        catchError((error: any) => {
          this.blockPage.set(false);
          this.messageService.add({ severity: 'error', summary: error.error.title, key: 'message', detail: error.error.description });
          return of(error);
        })).subscribe();
  }
}
