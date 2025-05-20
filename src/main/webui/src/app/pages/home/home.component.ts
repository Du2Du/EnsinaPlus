import { Component } from '@angular/core';
import { AuthService } from '../../services/auth.service';
import { AsyncPipe, JsonPipe } from '@angular/common';

@Component({
  selector: 'app-home',
  imports: [AsyncPipe, JsonPipe],
  templateUrl: './home.component.html',
  styleUrl: './home.component.scss'
})
export class HomeComponent {

  constructor(public authService: AuthService) {}

}
