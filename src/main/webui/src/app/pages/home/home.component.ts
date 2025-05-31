import { Component } from '@angular/core';
import { MainHeaderComponent } from "../../components/layout/main-header/main-header.component";
import { AuthService } from '../../services/auth.service';
import { HomeMainComponent } from "./components/home-main/home-main.component";

@Component({
  selector: 'app-home',
  imports: [MainHeaderComponent, HomeMainComponent],
  host: {class: 'w-full h-full flex flex-col items-center'},
  templateUrl: './home.component.html',
  styleUrl: './home.component.scss'
})
export class HomeComponent {

  constructor(public authService: AuthService) {}

}
