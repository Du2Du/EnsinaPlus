import { Location } from '@angular/common';
import { Component, signal, ViewChild } from '@angular/core';
import { FormsModule, NgForm } from '@angular/forms';
import { ButtonModule } from 'primeng/button';
import { CardModule } from 'primeng/card';
import { InputMaskModule } from 'primeng/inputmask';
import { InputTextModule } from 'primeng/inputtext';
import { ToastModule } from 'primeng/toast';
import { UserDTO } from '../../../../dtos/user.dto';
import { AuthService } from '../../../../services/auth.service';
import { PersistenceService } from '../../../../services/persistence.service';
import { DividerModule } from 'primeng/divider';
import { catchError, tap } from 'rxjs';
import { MessageService } from 'primeng/api';

@Component({
  selector: 'app-main-profile',
  imports: [CardModule, ToastModule, FormsModule, ButtonModule, InputTextModule, InputMaskModule, DividerModule],
  host: {class: 'w-[50%] m-auto mt-5 block'},
  templateUrl: './main-profile.component.html',
  styleUrl: './main-profile.component.scss'
})
export class MainProfileComponent {

  constructor(public authService: AuthService, private location: Location, private persistenceService: PersistenceService, private messageService: MessageService) {
    authService.getUser().subscribe(user => this.userData.set({...user}));
  }

  @ViewChild('form') form!: NgForm;
  userData = signal<UserDTO>({} as UserDTO);
  isLoading = signal(false);

  onSubmit(){
    if(this.form.invalid){
      return this.messageService.add({
        severity: 'error',
        key: 'toastMessage',
        summary: 'Formulário inválido',
        detail: this.joinFormMessages()
      });

    }
    this.isLoading.set(true);
    this.persistenceService.postRequest("/v1/user/save", this.userData()).pipe(tap(this.onSaveUser.bind(this)), catchError(this.onSaveUserError.bind(this))).subscribe();
  }

  private joinFormMessages() {
    let message = '';
    if (this.form.controls['name']?.errors?.['required'])
      message += 'Nome é obrigatório. ';
    if (this.form.controls['email']?.errors?.['required'])
      message += 'Email é obrigatório. ';
    if (this.form.controls['password']?.errors?.['required'])
      message += 'Senha é obrigatória. ';
    if (this.form.controls['password']?.errors?.['minlength'])
      message += `Senha deve ter pelo menos ${this.form.controls['password']?.errors?.['minlength']?.['requiredLength']} caracteres. `;

    return message;
  }

  onBack(){
    this.location.back();
  }

  private onSaveUser(response: any){
    this.isLoading.set(false);
    this.messageService.add({severity: 'success', summary: 'Sucesso', key: 'toastMessage', detail: 'Usuário salvo com sucesso!'});
  }

  private onSaveUserError(error: any){
    this.isLoading.set(false);
    this.messageService.add({severity: 'error', summary: 'Erro', key: 'toastMessage', detail: error.message});
    return error;
  }

}
