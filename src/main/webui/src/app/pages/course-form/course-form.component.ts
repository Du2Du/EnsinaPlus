import { Component, signal, ViewChild } from '@angular/core';
import { MainHeaderComponent } from "../../components/layout/main-header/main-header.component";
import { CardModule } from 'primeng/card';
import { FormsModule, NgForm } from '@angular/forms';
import { InputTextModule } from 'primeng/inputtext';
import { ButtonModule } from 'primeng/button';
import { ToastModule } from 'primeng/toast';
import { DividerModule } from 'primeng/divider';
import { TextareaModule } from 'primeng/textarea';
import { PersistenceService } from '../../services/persistence.service';
import { MessageService } from 'primeng/api';
import { Location } from '@angular/common';
import { FileUploadModule } from 'primeng/fileupload';
import { FileUtilsService } from '../../services/file-utils.service';
import { catchError, tap } from 'rxjs';
import { Router } from '@angular/router';

@Component({
  selector: 'app-course-form',
  imports: [MainHeaderComponent, FileUploadModule, CardModule, FormsModule, InputTextModule, ButtonModule, TextareaModule, DividerModule, ToastModule],
  templateUrl: './course-form.component.html',
  styleUrl: './course-form.component.scss'
})
export class CourseFormComponent {

  constructor(private fileService: FileUtilsService, private persistenceService: PersistenceService,
    private router: Router,
    private messageService: MessageService, private location: Location) { }

  @ViewChild('form') form!: NgForm;
  isLoading = signal(false);
  courseDTO = signal<{ uuid: string, name: string, description: string; mainPicture: string }>({} as any);

  onBack() {
    this.location.back();
  }

  onSubmit() {
    if (this.form.invalid) {
      return this.messageService.add({
        severity: 'error',
        key: 'toastMessage',
        summary: 'Formulário inválido',
        detail: this.joinFormMessages()
      });

    }
    this.isLoading.set(true);
    this.persistenceService.postRequest(this.courseDTO().uuid ? "/v1/course/save" : "/v1/course/create", this.courseDTO()).pipe(tap(this.onSaveCourse.bind(this)),
      catchError(this.onSaveCourseError.bind(this))).subscribe();

  }

  private onSaveCourse(response: any) {
    this.isLoading.set(false);
    this.messageService.add({ severity: 'success', summary: 'Sucesso', key: 'toastMessage', detail: 'Curso salvo com sucesso!' });
    this.router.navigate(['/course', response.data.uuid]);
  }

  private onSaveCourseError(error: any) {
    this.isLoading.set(false);
    this.messageService.add({ severity: 'error', summary: error.error.title, key: 'toastMessage', detail: error.error.description });
    return error;
  }

  onBasicUploadAuto(event: any) {
    this.fileService.toBase64(event.currentFiles[0]).subscribe((base64) => {
      this.courseDTO().mainPicture = base64;
    });
  }


  private joinFormMessages() {
    let message = '';
    if (this.form.controls['name']?.errors?.['required'])
      message += 'Nome é obrigatório. ';
    return message;
  }
}
