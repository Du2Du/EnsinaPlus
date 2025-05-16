import { Component } from '@angular/core';
import { DashboardHeaderComponent } from "../../components/layout/dashboard-header/dashboard-header.component";
import { LoginMainComponent } from "./components/login-main/login-main.component";

@Component({
  selector: 'app-login',
  imports: [DashboardHeaderComponent, LoginMainComponent],
  host: {
    class: 'h-full w-full flex flex-col'
  },
  templateUrl: './login.component.html',
  styleUrl: './login.component.scss'
})
export class LoginComponent {

}
