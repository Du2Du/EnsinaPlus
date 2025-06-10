import { AuthService } from './../../services/auth.service';
import { Component, OnDestroy, OnInit, signal } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { MessageService } from 'primeng/api';
import { BlockUIModule } from 'primeng/blockui';
import { ToastModule } from 'primeng/toast';
import { DragDropModule } from 'primeng/dragdrop';
import { OrderListModule } from 'primeng/orderlist';
import { AccordionModule } from 'primeng/accordion';
import { catchError, of, Subscription, tap } from 'rxjs';
import { MainHeaderComponent } from "../../components/layout/main-header/main-header.component";
import { PersistenceService } from '../../services/persistence.service';
import { CourseDTO } from '../../dtos/course.dto';
import { ModuleDTO } from '../../dtos/module.dto';
import { Store } from '@ngrx/store';
import { State } from '../../store/user.reducer';
import { UserDTO } from '../../dtos/user.dto';
import { ButtonModule } from 'primeng/button';

@Component({
  selector: 'app-course-home',
  host: {class: ''},
  imports: [MainHeaderComponent, ToastModule, BlockUIModule, DragDropModule, OrderListModule, AccordionModule, ButtonModule],
  providers: [MessageService, PersistenceService],
  templateUrl: './course-home.component.html',
  styleUrl: './course-home.component.scss'
})
export class CourseHomeComponent implements OnInit, OnDestroy {

  constructor(private route: ActivatedRoute, private persistenceService: PersistenceService, private messageService: MessageService, private authService: AuthService) {
    this.subscriber = authService.getUser().subscribe(user => this.userData.set({ ...user }));
  }
  
  blockPage = signal(false);
  course = signal<CourseDTO>({} as CourseDTO);
  modules = signal<ModuleDTO[]>({} as ModuleDTO[]);
  userData = signal<UserDTO>({} as UserDTO);
  private subscriber: Subscription;

  ngOnInit(): void {
    this.route.params.subscribe(params => {
      const courseId = params['uuid'];
      this.loadCourseInfo(courseId);
      this.loadCourseModules(courseId);
    });
  }

  ngOnDestroy(): void {
    this.subscriber.unsubscribe();
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

  loadCourseModules(courseId: string):void{
     this.persistenceService.getRequest('/v1/module/list/' + courseId)
      .pipe(tap((response: any) => {
        this.blockPage.set(false);
        this.modules.set(response.data);
        console.log(this.modules());
      }),
        catchError((error: any) => {
          this.blockPage.set(false);
          this.messageService.add({ severity: 'error', summary: error.error.title, key: 'message', detail: error.error.description });
          return of(error);
        })).subscribe();
  }

  checkOwner():boolean{
    if(this.userData().uuid === this.course().owner.uuid){
      return true;
    }
    return false;
  }
  // On Drag Start
    dragStart() {
        
    }

    drag() {
       
    }

    // On Drag End
    dragEnd() {
        
    }

    // On Drop of Item to droppable area
    drop() {
        
    }

    // Find the Index of a Person
    findIndex() {
    }

}
