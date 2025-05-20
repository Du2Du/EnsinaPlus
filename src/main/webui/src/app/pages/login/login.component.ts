import { Component } from '@angular/core';
import { DashboardHeaderComponent } from "../../components/layout/dashboard-header/dashboard-header.component";
import { LoginFormComponent } from "./components/login-form/login-form.component";

@Component({
  selector: 'app-login',
  imports: [DashboardHeaderComponent, LoginFormComponent],
  host: {
    class: 'h-full w-full flex flex-col'
  },
  templateUrl: './login.component.html',
  styleUrl: './login.component.scss'
})
export class LoginComponent {

}
