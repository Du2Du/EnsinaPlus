import { DrawerModule } from 'primeng/drawer';
import { AuthService } from './../../services/auth.service';
import { Component, OnDestroy, OnInit, output, signal } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { MessageService } from 'primeng/api';
import { BlockUIModule } from 'primeng/blockui';
import { ToastModule } from 'primeng/toast';
import { OrderListModule } from 'primeng/orderlist';
import { AccordionModule } from 'primeng/accordion';
import { DragDropModule } from '@angular/cdk/drag-drop';
import { CdkDragDrop, moveItemInArray } from '@angular/cdk/drag-drop';
import { catchError, of, Subscription, tap } from 'rxjs';
import { MainHeaderComponent } from "../../components/layout/main-header/main-header.component";
import { PersistenceService } from '../../services/persistence.service';
import { CourseDTO } from '../../dtos/course.dto';
import { ModuleDTO } from '../../dtos/module.dto';
import { Store } from '@ngrx/store';
import { State } from '../../store/user.reducer';
import { UserDTO } from '../../dtos/user.dto';
import { ButtonModule } from 'primeng/button';
import { CourseHomeModuleFormComponent } from './components/course-home-module-form/course-home-module-form.component';

@Component({
  selector: 'app-course-home',
  host: {class: ''},
  imports: [MainHeaderComponent, ToastModule, BlockUIModule, DragDropModule, AccordionModule, ButtonModule, CourseHomeModuleFormComponent, DrawerModule],
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
  modules = signal<ModuleDTO[]>([]);
  userData = signal<UserDTO>({} as UserDTO);
  visible = signal(false);
  uuidModules = signal<string []>([]);
  selectedModule = signal<ModuleDTO>({} as ModuleDTO);
  courseId!: string; 

  private subscriber: Subscription;
  currentlyDragging: ModuleDTO | null = null;

  ngOnInit(): void {
    this.route.params.subscribe(params => {
      this.courseId = params['uuid'];
      this.loadCourseInfo();
      this.loadCourseModules();
    });
  }

  ngOnDestroy(): void {
    this.subscriber.unsubscribe();
  }

  loadCourseInfo(): void {
    this.persistenceService.getRequest('/v1/course/' + this.courseId)
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

  loadCourseModules():void{
     this.persistenceService.getRequest('/v1/module/list/' + this.courseId)
      .pipe(tap((response: any) => {
        this.blockPage.set(false);
        this.modules.set(response.data);
        this.uuidModules.set(this.modules().map((module: any)=> module.uuid))
      }),
        catchError((error: any) => {
          this.blockPage.set(false);
          this.messageService.add({ severity: 'error', summary: error.error.title, key: 'message', detail: error.error.description });
          return of(error);
        })).subscribe();
  }

  checkOwner():boolean{
    return this.userData()?.uuid === this.course().owner?.uuid;
  }

  reorderModules(event: CdkDragDrop<ModuleDTO[]>) {
    moveItemInArray(this.modules(), event.previousIndex, event.currentIndex);
    this.modules().forEach((module, index) => {
      module.positionOrder = index+1;
    });
     this.persistenceService.putRequest('/v1/module/reorder', this.modules())
      .pipe(tap((response: any) => {
        this.blockPage.set(false);
      }),
        catchError((error: any) => {
          this.blockPage.set(false);
          this.messageService.add({ severity: 'error', summary: error.error.title, key: 'message', detail: error.error.description });
          return of(error);
        })).subscribe();
  }

  updateModule(module: ModuleDTO){
    this.selectedModule.set(module);
    this.visible.set(true)
  }

  deleteModule(uuid: string){
    console.log(uuid)
    this.persistenceService.deleteRequest('/v1/module/delete/' + uuid)
      .pipe(tap((response: any) => {
        this.blockPage.set(false);
        this.loadCourseModules();
      }),
        catchError((error: any) => {
          this.blockPage.set(false);
          this.messageService.add({ severity: 'error', summary: error.error.title, key: 'message', detail: error.error.description });
          return of(error);
        })).subscribe();
  }

  onHide(){
        this.selectedModule.set({} as ModuleDTO);
  }

}
