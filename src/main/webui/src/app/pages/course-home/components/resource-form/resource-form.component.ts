import { Component, input, output, signal, SimpleChanges, ViewChild } from '@angular/core';
import { FormsModule, NgForm } from '@angular/forms';
import { ActivatedRoute, RouterModule } from '@angular/router';
import { cloneDeep } from 'lodash';
import { MessageService } from 'primeng/api';
import { ButtonModule } from 'primeng/button';
import { CardModule } from 'primeng/card';
import { DividerModule } from 'primeng/divider';
import { FileUploadModule } from 'primeng/fileupload';
import { InputTextModule } from 'primeng/inputtext';
import { SelectButtonModule } from 'primeng/selectbutton';
import { TextareaModule } from 'primeng/textarea';
import { ToastModule } from 'primeng/toast';
import { catchError, of, tap } from 'rxjs';
import { FileUtilsService } from '../../../../services/file-utils.service';
import { PersistenceService } from '../../../../services/persistence.service';
import { ModuleDTO } from '../../../../dtos/module.dto';
import { IResource } from '../../course-home.component';
import { FileUploadService } from '../../../../services/file-upload.service';

@Component({
  selector: 'app-resource-form',
  providers: [MessageService, PersistenceService],
  imports: [RouterModule, FormsModule, FileUploadModule, SelectButtonModule, InputTextModule, CardModule, TextareaModule, DividerModule, ToastModule, ButtonModule], templateUrl: './resource-form.component.html',
  styleUrl: './resource-form.component.scss'
})
export class ResourceFormComponent {
  constructor(
    private fileService: FileUtilsService, 
    private fileUploadService: FileUploadService,
    private persistenceService: PersistenceService,
    private messageService: MessageService, 
    private route: ActivatedRoute
  ) { }

  @ViewChild('form') form!: NgForm;
  isLoading = signal(false);
  resourceDTO = signal<any>({});
  selectedResource = input<IResource & { typeObj: any }>({} as any);
  selectedModule = input<ModuleDTO>();
  file = signal<string>('');
  videoFileName = signal<string>('');
  courseUuid!: string;
  reloadData = output();
  onHide = output();
  showMessage = output<any>();
  types = [
    {
      label: 'Arquivo',
      code: 'FILE',
    }, {
      label: 'Vídeo',
      code: 'VIDEO',
    }]


  ngOnChanges(changes: SimpleChanges): void {
    if (changes['selectedResource']) {
      this.resourceDTO.set(cloneDeep({ ...this.selectedResource() }))
    }
  }

  ngOnInit() {
    if (this.selectedResource().type) {
      this.resourceDTO().tipoObj = this.types.find(t => t.code === this.resourceDTO().type);
    }
    this.route.params.subscribe((params) => {
      this.courseUuid = params["uuid"]
    });
  }

  onSubmit() {
    if (this.form.invalid) {
      return this.showMessage.emit({
        severity: 'error',
        key: 'toastMessage',
        summary: 'Formulário inválido',
        detail: this.joinFormMessages()
      });
    }
    this.resourceDTO().courseUuid = this.courseUuid;
    if (this.resourceDTO().tipoObj.code === 'FILE') {
      this.resourceDTO().video = '';
    } else if (this.resourceDTO().tipoObj.code === 'VIDEO') {
      this.file.set('');
    }
    this.resourceDTO().type = this.resourceDTO().tipoObj.code;
    this.resourceDTO().moduleUUID = this.selectedModule()?.uuid;

    if (this.file())
      this.resourceDTO().file = this.file();
    this.isLoading.set(true);
    if (this.resourceDTO().uuid) {
      this.persistenceService.putRequest('resource/update', this.resourceDTO()).subscribe({
        next: (response) => this.onSaveModule(response),
        error: (error) => this.onSaveModuleError(error)
      });
    } else {
      this.persistenceService.postRequest('resource/save', this.resourceDTO()).subscribe({
        next: (response) => this.onSaveModule(response),
        error: (error) => this.onSaveModuleError(error)
      });
    }
  }

  private onSaveModule(response: any) {
    this.isLoading.set(false);
    this.showMessage.emit({ severity: 'success', summary: 'Sucesso', key: 'toastMessage', detail: 'Módulo salvo com sucesso!' });
    this.reloadData.emit();
    this.onHide.emit();

  }

  private onSaveModuleError(error: any) {
    this.isLoading.set(false);
    this.showMessage.emit({ severity: 'error', summary: error.error.title, key: 'toastMessage', detail: error.error.description });
    return of(error);
  }
  private joinFormMessages() {
    let message = '';
    if (this.form.controls['name']?.errors?.['required'] || this.form.controls['name']?.errors?.['required'])
      message += 'Nome é obrigatório. ';
    return message;
  }

  onBasicUploadAuto(event: any) {
    const file = event.currentFiles[0];
    this.fileUploadService.uploadFile(file, 'file').subscribe({
      next: (response) => {
        this.resourceDTO().file = response.filePath;
        this.file.set(response.originalName);
      },
      error: (error) => {
        console.error('Erro no upload:', error);
      }
    });
  }

  onVideoUpload(event: any) {
    const file = event.currentFiles[0];
    this.videoFileName.set(file.name);
    this.fileUploadService.uploadFile(file, 'video').subscribe({
      next: (response) => {
        this.resourceDTO().video = response.filePath;
      },
      error: (error) => {
        console.error('Erro no upload do vídeo:', error);
      }
    });
  }

  onRemove() {
    this.file.set('');
  }

  onVideoRemove() {
    this.resourceDTO().video = '';
    this.videoFileName.set('');
  }
}
