import { Component, OnInit, signal } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { MessageService } from 'primeng/api';
import { MessageModule } from 'primeng/message';
import { PaginatorModule } from 'primeng/paginator';
import { SelectModule } from 'primeng/select';
import { TableModule } from 'primeng/table';
import { catchError, finalize, of, tap } from 'rxjs';
import { MainHeaderComponent } from "../../components/layout/main-header/main-header.component";
import { UserDTO } from '../../dtos/user.dto';
import { UserTypeEnum } from '../../enums/userTypeEnum';
import { PersistenceService } from './../../services/persistence.service';
import { BlockUIModule } from 'primeng/blockui';

@Component({
  selector: 'app-role-form',
  providers: [MessageService, PersistenceService],
  imports: [MainHeaderComponent, BlockUIModule, MessageModule, TableModule, FormsModule, SelectModule, PaginatorModule, PaginatorModule],
  templateUrl: './role-form.component.html',
  styleUrl: './role-form.component.scss'
})
export class RoleFormComponent implements OnInit {

  constructor(private persistenceService: PersistenceService, private messageService: MessageService) {

  }

  loadingUsers = signal(false);
  blockPage = signal(false);
  users = signal<Array<UserDTO>>([]);
  total = signal(0);
  page = signal(0);
  limit = signal(10);
  types = [{
    label: 'Super Administrador',
    code: UserTypeEnum.SUPER_ADMIN
  }, {
    label: 'Administrador',
    code: UserTypeEnum.ADMIN
  }, {
    label: 'Comum',
    code: UserTypeEnum.COMMON
  }];

  ngOnInit(): void {
    this.loadUsers();
  }

  private loadUsers() {
    this.loadingUsers.set(true);
    this.persistenceService.getRequest('/v1/user/list/role/').pipe(
      tap((response: any) => {
        this.users.set(response.data.map((user: UserDTO) => ({
          ...user,
          typeObj: this.types.find(t => t.code === user.type)
        })));
        this.total.set(response.total);
        console.log(this.users(), this.types);

      }),
      catchError(error => {
        this.users.set([]);
        this.total.set(0);
        this.messageService.clear();
        this.messageService.add({
          severity: 'error',
          summary: error.error?.title ?? 'Erro ao carregar os usuário',
          detail: error.error?.description ?? 'Tente novamente mais tarde'
        });
        return of(error);
      }),
      finalize(() => this.loadingUsers.set(false))
    ).subscribe();
  }

  onPageChange(event: any) {
    this.page.set(event.page);
    this.loadUsers();
  }

  changeRole(user: UserDTO & { typeObj: { code: UserTypeEnum } }) {
    user.type = user.typeObj['code'];
    this.blockPage.set(true);
    this.persistenceService.putRequest('/v1/user/save/', user).pipe(
      tap((response) => {
        this.blockPage.set(false);
        this.messageService.clear();
        this.messageService.add({
          severity: 'success',
          summary: 'Usuário salvo com sucesso'
        });
      }),
      catchError(error => {
        this.blockPage.set(false);
        this.messageService.clear();
        this.messageService.add({
          severity: 'error',
          summary: error.error?.title ?? 'Erro ao salvar o usuário',
          detail: error.error?.description ?? 'Tente novamente mais tarde'
        });
        return of(error);
      })
    ).subscribe();
  }
}
