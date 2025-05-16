import { Component, model, ViewChild } from '@angular/core';
import { CardModule } from 'primeng/card';
import { FormsModule, NgForm } from '@angular/forms';
import { InputTextModule } from 'primeng/inputtext';
import { PasswordModule } from 'primeng/password';
import { DividerModule } from 'primeng/divider';
import { ButtonModule } from 'primeng/button';
import { Router } from '@angular/router';
import { MessageService } from 'primeng/api';

@Component({
  selector: 'app-login-main',
  imports: [CardModule, FormsModule, InputTextModule, PasswordModule,
    DividerModule, ButtonModule],
  providers: [MessageService],
  host: {
    class: 'h-full w-full flex items-center justify-center'
  },
  templateUrl: './login-main.component.html',
  styleUrl: './login-main.component.scss'
})
export class LoginMainComponent {

  constructor(private router: Router, private messageService: MessageService) { }

  @ViewChild('form') form!: NgForm;
  dto = model<{ email: string, password: string }>({ email: '', password: '' });

  onBack() {
    this.router.navigate(['/']);
  }

  onSubmit() {
    if (!this.form.valid)
      this.messageService.add({
        severity: 'error',
        key: 'toastMessage',
        summary: 'Erro',
        detail: 'Formulário inválido'
      });
  }
}
