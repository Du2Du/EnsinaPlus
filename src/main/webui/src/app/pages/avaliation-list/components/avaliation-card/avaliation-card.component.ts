import { Component, input } from '@angular/core';
import { IAvaliation } from '../../avaliation-list.component';
import { CardModule } from 'primeng/card';
import { RatingModule } from 'primeng/rating';
import { FormsModule } from '@angular/forms';
@Component({
  selector: 'app-avaliation-card',
  imports: [CardModule, RatingModule, FormsModule],
  templateUrl: './avaliation-card.component.html',
  styleUrl: './avaliation-card.component.scss'
})
export class AvaliationCardComponent {


  avaliation = input<IAvaliation>();

}
