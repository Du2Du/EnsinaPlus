import { Component } from '@angular/core';
import { ButtonModule } from 'primeng/button';
import { CardComponent } from "../../../../components/output/card/card.component";

@Component({
  selector: 'app-main-content',
  imports: [ButtonModule, CardComponent],
  host: {
    class: 'col-span-12 flex h-full overflow-y-auto'
  },
  templateUrl: './main-content.component.html',
  styleUrl: './main-content.component.scss'
})
export class MainContentComponent {

}
