import { Component, OnInit, signal } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { MessageService } from 'primeng/api';
import { catchError, of, tap } from 'rxjs';
import { PersistenceService } from './../../services/persistence.service';
import { MainHeaderComponent } from "../../components/layout/main-header/main-header.component";
import { BlockUIModule } from 'primeng/blockui';
import { MessageModule } from 'primeng/message';
import { AvaliationCardComponent } from './components/avaliation-card/avaliation-card.component';
import { ButtonModule } from 'primeng/button';
import { Location } from '@angular/common';

export interface IAvaliation {
  uuid: string;
  name: string;
  description: string;
  stars: number;
}

@Component({
  selector: 'app-avaliation-list',
  imports: [MainHeaderComponent, BlockUIModule, ButtonModule, AvaliationCardComponent, MessageModule,],
  providers: [MessageService, PersistenceService],
  templateUrl: './avaliation-list.component.html',
  styleUrl: './avaliation-list.component.scss'
})
export class AvaliationListComponent implements OnInit {

  constructor(private route: ActivatedRoute, private location: Location, private persistenceService: PersistenceService) { }

  courseUUID!: string;
  loading = signal(false);
  avaliations = signal<Array<IAvaliation>>([]);

  ngOnInit(): void {
    this.route.params.subscribe(params => {
      this.courseUUID = params['courseUUID'];
      this.loadAvaliations();
    });
  }
  back() {
    this.location.back();
  }

  private loadAvaliations(): void {
    this.persistenceService.getRequest('/v1/course/avaliation/list/' + this.courseUUID).pipe(tap((response: any) => {
      this.avaliations.set(response.data);
    }), catchError(error => {
      this.avaliations.set([]);
      return of(error);
    })).subscribe();
  }
}
