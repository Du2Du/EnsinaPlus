import { ModuleDTO } from './../../../../dtos/module.dto';
import { Component, input, model, OnChanges, output, signal, SimpleChanges, ViewChild } from '@angular/core';
import { FormsModule, NgForm } from '@angular/forms';
import { InputTextModule } from 'primeng/inputtext';
import { PersistenceService } from '../../../../services/persistence.service';
import { ActivatedRoute, Router } from '@angular/router';
import { MessageService } from 'primeng/api';
import { catchError, of, tap } from 'rxjs';
import { CardModule } from 'primeng/card';
import { ToastModule } from 'primeng/toast';
import { DividerModule } from 'primeng/divider';
import { TextareaModule } from 'primeng/textarea';
import { ButtonModule } from 'primeng/button';
import { cloneDeep } from 'lodash';
import { SelectButtonModule } from 'primeng/selectbutton';
import { FileUploadModule } from 'primeng/fileupload';
import { FileUtilsService } from '../../../../services/file-utils.service';

@Component({
  selector: 'app-resource-form',
  providers: [MessageService, PersistenceService, ActivatedRoute],
  imports: [FormsModule, FileUploadModule, SelectButtonModule, InputTextModule, CardModule, TextareaModule, DividerModule, ToastModule, ButtonModule], templateUrl: './resource-form.component.html',
  styleUrl: './resource-form.component.scss'
})
export class ResourceFormComponent {
  constructor(private fileService: FileUtilsService, private persistenceService: PersistenceService,
    private messageService: MessageService, private route: ActivatedRoute) { }

  @ViewChild('form') form!: NgForm;
  isLoading = signal(false);
  resourceDTO = signal<any>({});
  selectedResource = input<any>({});
  file = signal<string>('');
  courseUuid!: string;
  reloadData = output();
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
    this.route.params.subscribe((params) => {
      this.courseUuid = params["uuid"]
    });
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
    this.resourceDTO().courseUuid = this.courseUuid;
    if (this.file())
      this.resourceDTO().file = this.file();
    this.isLoading.set(true);
    if (this.resourceDTO().uuid) {
      this.persistenceService.putRequest("/v1/resource/update", this.resourceDTO()).pipe(tap(this.onSaveModule.bind(this)),
        catchError(this.onSaveModuleError.bind(this))).subscribe();

    } else {
      this.persistenceService.postRequest("/v1/resource/create", this.resourceDTO()).pipe(tap(this.onSaveModule.bind(this)),
        catchError(this.onSaveModuleError.bind(this))).subscribe();
    }
  }

  private onSaveModule(response: any) {
    this.isLoading.set(false);
    this.messageService.add({ severity: 'success', summary: 'Sucesso', key: 'toastMessage', detail: 'Módulo salvo com sucesso!' });
    this.reloadData.emit();
  }

  private onSaveModuleError(error: any) {
    this.isLoading.set(false);
    this.messageService.add({ severity: 'error', summary: error.error.title, key: 'toastMessage', detail: error.error.description });
    return of(error);
  }
  private joinFormMessages() {
    let message = '';
    if (this.form.controls['name']?.errors?.['required'] || this.form.controls['name']?.errors?.['required'])
      message += 'Nome é obrigatório. ';
    return message;
  }

  onBasicUploadAuto(event: any) {
    this.fileService.toBase64(event.currentFiles[0]).subscribe((base64) => {
      this.file.set(base64);
    });
  }

  onRemove() {
    this.file.set('');
  }
}
