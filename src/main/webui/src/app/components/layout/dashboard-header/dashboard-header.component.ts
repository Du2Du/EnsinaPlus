import { Component } from '@angular/core';
import { Router } from '@angular/router';
import { ButtonModule } from 'primeng/button';

@Component({
  selector: 'app-dashboard-header',
  imports: [ButtonModule],
  host: {
    class: 'w-full bg-stone-50 !pr-2 !pl-2 flex shadow-md items-center justify-between'
  },
  templateUrl: './dashboard-header.component.html',
  styleUrl: './dashboard-header.component.scss'
})
export class DashboardHeaderComponent {

  constructor(public router: Router) { }

  onLoginClick() {
    this.router.navigate(['/login']);
  }

  onRegisterClick() {
    this.router.navigate(['/register']);
  }
  onLogoClick() {
    this.router.navigate(['/']);
  }
}
