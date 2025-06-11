import { Component, model, signal, ViewChild } from '@angular/core';
import { FormsModule, NgForm } from '@angular/forms';
import { Router } from '@angular/router';
import { MessageService } from 'primeng/api';
import { ButtonModule } from 'primeng/button';
import { CardModule } from 'primeng/card';
import { DividerModule } from 'primeng/divider';
import { InputTextModule } from 'primeng/inputtext';
import { PasswordModule } from 'primeng/password';
import { RadioButtonModule } from 'primeng/radiobutton';
import { ToastModule } from 'primeng/toast';
import { of } from 'rxjs';
import { catchError, finalize, tap } from 'rxjs/operators';
import { RoleEnum } from '../../../../enums/roleEnum';
import { PersistenceService } from './../../../../services/persistence.service';
import { AuthService } from '../../../../services/auth.service';

@Component({
  selector: 'app-login-form',
  imports: [CardModule, FormsModule, InputTextModule, PasswordModule, RadioButtonModule,
    DividerModule, ButtonModule, ToastModule],
  providers: [MessageService],
  host: {
    class: 'h-full w-[90%] md:w-[60%] xl:w-[30%] m-auto flex items-center justify-center'
  },
  templateUrl: './login-form.component.html',
  styleUrl: './login-form.component.scss'
})
export class LoginFormComponent {

  constructor(private router: Router, private messageService: MessageService, private persistenceService: PersistenceService, private authService: AuthService) { }

  @ViewChild('form') form!: NgForm;
  dto = model<{ role: RoleEnum, email: string, password: string }>({ role: RoleEnum.STUDENT, email: '', password: '' });
  isLoading = signal(false);

  onBack() {
    this.router.navigate(['/']);
  }

  onSubmit() {
    if (!this.form.valid)
      return this.messageService.add({
        severity: 'error',
        key: 'toastMessage',
        summary: 'Formulário inválido',
        detail: this.joinFormMessages()
      });

    this.isLoading.set(true);

    this.persistenceService.postRequest(`/v1/user/login/${this.dto().role.toLowerCase()}`, this.dto())
      .pipe(
        tap((response: any) => {
          localStorage.setItem('ensina-plus-token', response.data);
          this.messageService.add({
            severity: 'success',
            key: 'toastMessage',
            summary: 'Login realizado',
            detail: 'Você foi autenticado com sucesso!'
          });
          this.router.navigate(['/home']);
        }),
        catchError((data: any) => {
          this.isLoading.set(false);
          this.messageService.add({
            severity: 'error',
            key: 'toastMessage',
            summary: data?.error?.title || 'Erro ao logar',
            detail: data?.error?.description || 'Não foi possível realizar o login. Tente novamente mais tarde!'
          });

          return of(data);
        })
      )
      .subscribe()
  }

  private joinFormMessages() {
    let message = '';
    if (this.form.controls['email']?.errors?.['required'])
      message += 'Email é obrigatório. ';
    if (this.form.controls['password']?.errors?.['required'])
      message += 'Senha é obrigatória. ';
    if (this.form.controls['password']?.errors?.['minlength'])
      message += `Senha deve ter pelo menos ${this.form.controls['password']?.errors?.['minlength']?.['requiredLength']} caracteres. `;

    return message;
  }
}
