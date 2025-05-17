import { Component, model, ViewChild } from '@angular/core';
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

@Component({
  selector: 'app-login-form',
  imports: [CardModule, FormsModule, InputTextModule, PasswordModule,
    DividerModule, ButtonModule, ToastModule],
  providers: [MessageService],
  host: {
    class: 'h-full w-full flex items-center justify-center'
  },
  templateUrl: './login-form.component.html',
  styleUrl: './login-form.component.scss'
})
export class LoginFormComponent {

  constructor(private router: Router, private messageService: MessageService, private persistenceService: PersistenceService) { }

  @ViewChild('form') form!: NgForm;
  dto = model<{ email: string, password: string }>({ email: '', password: '' });

  onBack() {
    this.router.navigate(['/']);
  }

  onSubmit() {
    if (!this.form.valid)
      return this.messageService.add({
        severity: 'error',
        key: 'toastMessage',
        summary: 'Erro',
        detail: 'Formulário inválido'
      });
    this.persistenceService.postRequest('/v1/user/login/student', this.dto()).subscribe((data: any) => console.log(data))
  }
}
