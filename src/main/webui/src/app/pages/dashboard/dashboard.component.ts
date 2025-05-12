import { Component } from '@angular/core';
import { DashboardHeaderComponent } from '../../components/layout/dashboard-header/dashboard-header.component';
import { MainContentComponent } from "./components/main-content/main-content.component";

@Component({
  selector: 'app-dashboard',
  host: { class: 'w-full bg-stone-100 flex flex-col h-full overflow-hidden' },
  imports: [DashboardHeaderComponent, MainContentComponent],
  templateUrl: './dashboard.component.html',
  styleUrl: './dashboard.component.scss'
})
export class DashboardComponent {

}
