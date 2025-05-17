import { Component, model, signal, ViewChild } from '@angular/core';
import { FormsModule, NgForm } from '@angular/forms';
import { Router } from '@angular/router';
import { MessageService } from 'primeng/api';
import { ButtonModule } from 'primeng/button';
import { CardModule } from 'primeng/card';
import { DividerModule } from 'primeng/divider';
import { InputTextModule } from 'primeng/inputtext';
import { PasswordModule } from 'primeng/password';
import { ToastModule } from 'primeng/toast';
import { PersistenceService } from './../../../../services/persistence.service';
import { catchError, finalize, tap } from 'rxjs/operators';
import { of } from 'rxjs';
import * as Sentry from "@sentry/angular";

@Component({
  selector: 'app-login-form',
  imports: [CardModule, FormsModule, InputTextModule, PasswordModule,
    DividerModule, ButtonModule, ToastModule],
  providers: [MessageService],
  host: {
    class: 'h-full w-[90%] md:w-[60%] xl:w-[30%] m-auto flex items-center justify-center'
  },
  templateUrl: './login-form.component.html',
  styleUrl: './login-form.component.scss'
})
export class LoginFormComponent {

  constructor(private router: Router, private messageService: MessageService, private persistenceService: PersistenceService) { }

  @ViewChild('form') form!: NgForm;
  dto = model<{ email: string, password: string }>({ email: '', password: '' });
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

    this.persistenceService.postRequest('/v1/user/login/student', this.dto())
      .pipe(
        tap((response: any) => {
          this.messageService.add({
            severity: 'success',
            key: 'toastMessage',
            summary: 'Login realizado',
            detail: 'Você foi autenticado com sucesso!'
          });
          setTimeout(() => {
            this.router.navigate(['/home']);
          }, 2000)
        }),
        catchError((data: any) => {
          this.messageService.add({
            severity: 'error',
            key: 'toastMessage',
            summary: data?.error?.title || 'Erro ao logar',
            detail: data?.error?.description || 'Não foi possível realizar o login. Verifique suas credenciais.'
          });

          return of(null);
        }),
        finalize(() => {
          this.isLoading.set(false);
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
