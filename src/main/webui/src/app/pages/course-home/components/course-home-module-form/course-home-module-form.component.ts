import { ModuleDTO } from './../../../../dtos/module.dto';
import { Component, input, model, OnChanges, output, signal, SimpleChanges, ViewChild } from '@angular/core';
import { FormsModule, NgForm } from '@angular/forms';
import { InputTextModule } from 'primeng/inputtext';
import { PersistenceService } from '../../../../services/persistence.service';
import { ActivatedRoute, Router, RouterModule } from '@angular/router';
import { MessageService } from 'primeng/api';
import { catchError, of, tap } from 'rxjs';
import { CardModule } from 'primeng/card';
import { ToastModule } from 'primeng/toast';
import { DividerModule } from 'primeng/divider';
import { TextareaModule } from 'primeng/textarea';
import { ButtonModule } from 'primeng/button';
import {cloneDeep} from 'lodash';

@Component({
  selector: 'app-course-home-module-form',
  providers: [MessageService, PersistenceService],
  imports: [RouterModule, FormsModule, InputTextModule, CardModule, TextareaModule, DividerModule, ToastModule, ButtonModule],
  templateUrl: './course-home-module-form.component.html',
  styleUrl: './course-home-module-form.component.scss'
})
export class CourseHomeModuleFormComponent implements OnChanges {
  constructor( private persistenceService: PersistenceService,
    private messageService: MessageService, private route: ActivatedRoute) { }

  @ViewChild('form') form!: NgForm;
  isLoading = signal(false);
  moduleDTO = signal<ModuleDTO>({} as any);
  selectedModule = input<ModuleDTO>({} as any);
  courseUuid!:string;
  reloadData = output();
  onHide = output();
  showMessage = output<any>();

  ngOnChanges(changes: SimpleChanges): void {
      if(changes['selectedModule']){
        this.moduleDTO.set(cloneDeep({...this.selectedModule()}))
      }
  }

  ngOnInit(){
    this.route.params.subscribe((params)=>{
      this.courseUuid = params["uuid"]});
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
    this.moduleDTO().courseUuid = this.courseUuid;
    this.isLoading.set(true);
    if(this.moduleDTO().uuid){
       this.persistenceService.putRequest("/v1/module/update", this.moduleDTO()).pipe(tap(this.onSaveModule.bind(this)),
      catchError(this.onSaveModuleError.bind(this))).subscribe();
      
    } else {
      this.persistenceService.postRequest("/v1/module/create", this.moduleDTO()).pipe(tap(this.onSaveModule.bind(this)),
      catchError(this.onSaveModuleError.bind(this))).subscribe();
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
}
