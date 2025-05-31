import { Component } from '@angular/core';
import { MainHeaderComponent } from "../../components/layout/main-header/main-header.component";
import { MainProfileComponent } from "./components/main-profile/main-profile.component";

@Component({
  selector: 'app-profile',
  imports: [MainHeaderComponent, MainProfileComponent],
  templateUrl: './profile.component.html',
  styleUrl: './profile.component.scss'
})
export class ProfileComponent {


}
