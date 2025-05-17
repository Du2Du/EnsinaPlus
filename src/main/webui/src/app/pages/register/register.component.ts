import { Component } from '@angular/core';
import { DashboardHeaderComponent } from "../../components/layout/dashboard-header/dashboard-header.component";
import { RegisterFormComponent } from "./components/register-form/register-form.component";

@Component({
  selector: 'app-register',
  imports: [DashboardHeaderComponent, RegisterFormComponent],
  host: {
    class: 'h-full w-full flex flex-col'
  },
  templateUrl: './register.component.html',
  styleUrl: './register.component.scss'
})
export class RegisterComponent {

}
