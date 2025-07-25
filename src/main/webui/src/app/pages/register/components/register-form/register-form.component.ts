import { Component, model, signal, ViewChild } from '@angular/core';
import { FormsModule, NgForm } from '@angular/forms';
import { MessageService } from 'primeng/api';
import { ButtonModule } from 'primeng/button';
import { CardModule } from 'primeng/card';
import { DividerModule } from 'primeng/divider';
import { InputTextModule } from 'primeng/inputtext';
import { PasswordModule } from 'primeng/password';
import { ToastModule } from 'primeng/toast';
import { PersistenceService } from '../../../../services/persistence.service';
import { Router } from '@angular/router';
import { catchError, finalize, of, tap } from 'rxjs';

@Component({
  selector: 'app-register-form',
  imports: [CardModule, FormsModule, InputTextModule, PasswordModule,
    DividerModule, ButtonModule, ToastModule],
  providers: [MessageService],
  host: {
    class: 'h-full w-[90%] md:w-[60%] xl:w-[30%] m-auto flex items-center justify-center'
  },
  templateUrl: './register-form.component.html',
  styleUrl: './register-form.component.scss'
})
export class RegisterFormComponent {
  constructor(private router: Router, private messageService: MessageService, private persistenceService: PersistenceService) { }

  @ViewChild('form') form!: NgForm;
  dto = model<{ name: string, email: string, password: string }>({ name: '', email: '', password: '' });
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

  this.persistenceService.postRequest('/v1/user/create/', this.dto())
      .pipe(
        tap((response: any) => {
          this.messageService.add({
            severity: 'success',
            key: 'toastMessage',
            summary: 'Cadastro realizado',
            detail: 'Você foi cadastrado com sucesso!'
          });
          setTimeout(() => {
            this.router.navigate(['/login']);
          }, 2000)
        }),
        catchError((data: any) => {
          this.messageService.add({
            severity: 'error',
            key: 'toastMessage',
            summary: data?.error?.title || 'Erro ao cadastrar',
            detail: data?.error?.description || 'Não foi possível realizar o cadastro. Tente novamente mais tarde!'
          });

          return of(data);
        }),
        finalize(() => {
          this.isLoading.set(false);
        })
      )
      .subscribe()
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
}
