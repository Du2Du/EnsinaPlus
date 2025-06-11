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

@Component({
  selector: 'app-resource-form',
  providers: [MessageService, PersistenceService],
  imports: [RouterModule, FormsModule, FileUploadModule, SelectButtonModule, InputTextModule, CardModule, TextareaModule, DividerModule, ToastModule, ButtonModule], templateUrl: './resource-form.component.html',
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
    this.showMessage.emit({ severity: 'success', summary: 'Sucesso', key: 'toastMessage', detail: 'Módulo salvo com sucesso!' });
    this.reloadData.emit();
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
    this.fileService.toBase64(event.currentFiles[0]).subscribe((base64) => {
      this.file.set(base64);
    });
  }

  onRemove() {
    this.file.set('');
  }
}
