import { Component, signal } from '@angular/core';
import { TableModule } from 'primeng/table';
import { MainHeaderComponent } from '../../components/layout/main-header/main-header.component';
import { PersistenceService } from '../../services/persistence.service';
import { catchError, finalize, tap } from 'rxjs';
import { MessageModule } from 'primeng/message';
import { AsyncPipe, DatePipe, JsonPipe } from '@angular/common';
import { PaginatorModule } from 'primeng/paginator';
@Component({
  selector: 'app-auditoria',
  imports: [TableModule, MainHeaderComponent, MessageModule, DatePipe, PaginatorModule],
  host:{class:''},
  templateUrl: './auditoria.component.html',
  styleUrl: './auditoria.component.scss'
})
export class AuditoriaComponent {
  constructor(public persistenceService: PersistenceService) { }

  searchingLogs = signal(false);
  logs = signal <any[]> ([]);
  totalElements: number = 0;

  ngOnInit(): void {
    this.searchLogs(0);
  }

  searchLogs(page:number) {
    this.searchingLogs.set(true);
    this.persistenceService.getRequest("/v1/log/list?page="+page).pipe(tap((response: any) => {
      this.logs.set(response.data.dtos);
      this.totalElements = response.data.totalElements;
    }), catchError(error => {
      this.logs.set([]);
      return error;
    }), finalize(() => {
      this.searchingLogs.set(false);
    })).subscribe();
  }

  onPageChange($event: any){
      this.searchLogs($event.page)
  }
}
