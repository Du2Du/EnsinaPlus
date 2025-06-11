import { Location } from '@angular/common';
import { Component, input, output, signal, ViewChild } from '@angular/core';
import { FormsModule, NgForm } from '@angular/forms';
import { MessageService } from 'primeng/api';
import { ButtonModule } from 'primeng/button';
import { RatingModule } from 'primeng/rating';
import { TextareaModule } from 'primeng/textarea';
import { ToastModule } from 'primeng/toast';
import { catchError, of, tap } from 'rxjs';
import { PersistenceService } from '../../../../services/persistence.service';

@Component({
  selector: 'app-avaliation-form',
  imports: [FormsModule, ButtonModule, ToastModule, TextareaModule, RatingModule],
  providers: [PersistenceService, MessageService],
  templateUrl: './avaliation-form.component.html',
  styleUrl: './avaliation-form.component.scss'
})
export class AvaliationFormComponent {

  constructor(private location: Location, private persistenceService: PersistenceService, private messageService: MessageService) { }

  @ViewChild(NgForm) form!: NgForm;
  avaliationDTO = signal<{ stars: number; description: string }>({stars: 0} as any);
  saving = signal(false);
  courseUUID = input<string>();
  showMessage = output<any>();

  saveAvaliation() {
    this.saving.set(true);
    console.log(this.avaliationDTO());
    
    this.persistenceService.postRequest("/v1/course/avaliate", {
      courseUUID: this.courseUUID(),
      comment: this.avaliationDTO().description,
      stars: this.avaliationDTO().stars
    }).pipe(tap(this.onSaveModule.bind(this)),
      catchError(this.onSaveModuleError.bind(this))).subscribe();

  }

  private onSaveModule(response: any) {
    this.saving.set(false);
    this.showMessage.emit({ severity: 'success', summary: 'Sucesso', key: 'toastMessage', detail: 'Avaliação salva com sucesso!' });
    location.reload();
  }

  private onSaveModuleError(error: any) {
    this.saving.set(false);
    this.showMessage.emit({ severity: 'error', summary: error.error.title || 'Erro ao realizar avaliação', key: 'toastMessage', detail: error.error.description });
    return of(error);
  }

  updateStars(value: number) {
    this.avaliationDTO.update(current => ({ ...current, stars: value }));
  }
  
}
