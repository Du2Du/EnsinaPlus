import { FormsModule } from '@angular/forms';
import { CdkDragDrop, DragDropModule, moveItemInArray } from '@angular/cdk/drag-drop';
import { Component, OnDestroy, OnInit, signal } from '@angular/core';
import { ActivatedRoute, Router, RouterModule } from '@angular/router';
import { AccordionModule } from 'primeng/accordion';
import { MessageService } from 'primeng/api';
import { BlockUIModule } from 'primeng/blockui';
import { ButtonModule } from 'primeng/button';
import { DialogModule } from 'primeng/dialog';
import { DrawerModule } from 'primeng/drawer';
import { ToastModule } from 'primeng/toast';
import { catchError, of, Subscription, tap } from 'rxjs';
import { MainHeaderComponent } from "../../components/layout/main-header/main-header.component";
import { CourseDTO } from '../../dtos/course.dto';
import { ModuleDTO } from '../../dtos/module.dto';
import { UserDTO } from '../../dtos/user.dto';
import { RoleEnum } from '../../enums/roleEnum';
import { PersistenceService } from '../../services/persistence.service';
import { AuthService } from './../../services/auth.service';
import { CourseHomeModuleFormComponent } from './components/course-home-module-form/course-home-module-form.component';
import { RatingModule } from 'primeng/rating';
import { ResourceFormComponent } from './components/resource-form/resource-form.component';
import { AvaliationFormComponent } from './components/avaliation-form/avaliation-form.component';
import { ResourceEnum } from '../../enums/resourceEnum';
import { ResourceItemComponent } from './components/resource-item/resource-item.component';

export interface IResource {
  type: ResourceEnum,
  descriptionHTML: string,
  name: string,
  uuid: string,
  file?: string,
  video?: string,
}

@Component({
  selector: 'app-course-home',
  imports: [RouterModule, MainHeaderComponent,ResourceItemComponent, AvaliationFormComponent, DialogModule, ToastModule, ResourceFormComponent, BlockUIModule, DragDropModule, AccordionModule, ButtonModule, CourseHomeModuleFormComponent, DrawerModule, RatingModule, FormsModule],
  providers: [MessageService, PersistenceService, AuthService], // Remover ActivatedRoute daqui
  templateUrl: './course-home.component.html',
  styleUrl: './course-home.component.scss'
})
export class CourseHomeComponent implements OnInit, OnDestroy {

  constructor(private route: ActivatedRoute, private router: Router, private persistenceService: PersistenceService,
    private messageService: MessageService, private authService: AuthService) {
    this.subscriber = authService.getUser().subscribe(user => this.userData.set({ ...user }));
  }

  blockPage = signal(false);
  course = signal<CourseDTO>({} as CourseDTO);
  modules = signal<ModuleDTO[]>([]);
  userData = signal<UserDTO>({} as UserDTO);
  visible = signal(false);
  visibleDialog = signal(false);
  uuidModules = signal<string[]>([]);
  selectedModule = signal<ModuleDTO>({} as ModuleDTO);
  selectedResource = signal<any>({});
  selectedEntity = signal<'resource' | 'module' | undefined>(undefined);
  courseId!: string;

  private subscriber: Subscription;
  currentlyDragging: ModuleDTO | null = null;

  ngOnInit(): void {
    this.route.params.subscribe(params => {
      this.courseId = params['uuid'];
      this.loadCourseInfo();
    });
  }

  ngOnDestroy(): void {
    this.subscriber.unsubscribe();
  }

  loadCourseInfo(): void {
    this.blockPage.set(true)
    this.persistenceService.getRequest('/v1/course/' + this.courseId)
      .pipe(tap((response: any) => {
        this.blockPage.set(false);
        this.course.set(response.data);
        this.loadCourseModules();
      }),
        catchError((error: any) => {
          this.blockPage.set(false);
          this.messageService.add({ severity: 'error', summary: error.error.title, key: 'message', detail: error.error.description });
          return of(error);
        })).subscribe();
  }

  loadCourseModules(): void {
    this.blockPage.set(true)
    this.persistenceService.getRequest('/v1/module/list/' + this.courseId)
      .pipe(tap((response: any) => {
        this.blockPage.set(false);
        this.modules.set(response.data);
        this.uuidModules.set(this.modules().map((module: any) => module.uuid))
      }),
        catchError((error: any) => {
          this.blockPage.set(false);
          this.messageService.add({ severity: 'error', summary: error.error.title, key: 'message', detail: error.error.description });
          return of(error);
        })).subscribe();
  }

  reorderModules(event: CdkDragDrop<ModuleDTO[]>) {
    this.blockPage.set(true)
    moveItemInArray(this.modules(), event.previousIndex, event.currentIndex);
    this.modules().forEach((module, index) => {
      module.positionOrder = index + 1;
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

  updateModule(module: ModuleDTO) {
    this.selectedModule.set(module);
    this.visible.set(true)
  }

  deleteModule(uuid: string) {
    this.blockPage.set(true);
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

  deleteResource(uuid: string) {
    this.blockPage.set(true);
    this.persistenceService.deleteRequest('/v1/resource/delete/' + uuid)
      .pipe(tap((response: any) => {
        this.blockPage.set(false);
        this.loadCourseModules();
      }),
        catchError((error: any) => {
          this.blockPage.set(false);
          this.messageService.add({ severity: 'error', summary: error.error.title ?? 'Erro ao deletar recurso', key: 'message', detail: error.error.description });
          return of(error);
        })).subscribe();
  }

  onHide() {
    this.visible.set(false);
    this.selectedEntity.set(undefined)
    this.selectedModule.set({} as ModuleDTO);
    this.selectedResource.set({});
  }

  desmatricular() {
    this.blockPage.set(true);
    this.persistenceService.deleteRequest('/v1/course/unenroll/' + this.courseId).pipe(
      tap(response => {
        this.messageService.clear()
        this.messageService.add({ severity: 'success', summary: 'Sua matricula no curso foi cancelada!', key: 'message', });
        setTimeout(() => {
          this.router.navigate(['/home']);
        }, 2000)
      }), catchError(error => {
        this.blockPage.set(false);
        this.messageService.add({ severity: 'error', summary: error.error?.title || 'Erro ao desmatricular-se', key: 'message', detail: error.error?.description });
        return of(error);
      })
    ).subscribe();
  }
  excludeCourse() {
    this.blockPage.set(true);
    this.persistenceService.deleteRequest('/v1/course/delete/' + this.courseId).pipe(
      tap(response => {
        this.messageService.clear()
        this.messageService.add({ severity: 'success', summary: 'Curso deletado com sucesso!', key: 'message', });
        this.router.navigate(['/home']);
      }), catchError(error => {
        this.blockPage.set(false);
        this.messageService.add({ severity: 'error', summary: error.error?.title || 'Erro ao deletar curso', key: 'message', detail: error.error?.description });
        return of(error);
      })
    ).subscribe();
  }

  generateFile() {
    this.blockPage.set(true);
    this.persistenceService.getRequest(`/v1/course/generate/${this.courseId}/certification`, { responseType: 'blob' }).pipe(
      tap((response: any) => {
        const blobUrl = URL.createObjectURL(response);
        window.open(blobUrl, '_blank');
        setTimeout(() => {
          URL.revokeObjectURL(blobUrl);
        }, 1000);
        this.blockPage.set(false);

      }), catchError(error => {
        console.log(error);

        this.blockPage.set(false);
        this.messageService.add({ severity: 'error', summary: error.error?.title || 'Erro ao gerar certificado', key: 'message', detail: error.error?.description });
        return of(error);
      })
    ).subscribe()
  }

  goToAvaliations() {
    this.router.navigate(['course', 'avaliation', this.courseId])
  }

  avaliate() {
    this.visibleDialog.set(true);
  }

  showMessage(data: any) {
    this.messageService.clear()
    this.messageService.add(data);
  }

  editResource(resource: IResource) {
    this.selectedEntity.set('resource')
    this.selectedResource.set(resource)
    this.visible.set(true)
  }

  editCourse(){
    this.router.navigate(['course', 'form', this.courseId])
  }

  protected RoleEnum = RoleEnum;
}
