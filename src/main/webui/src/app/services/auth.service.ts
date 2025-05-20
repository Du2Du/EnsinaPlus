import { Injectable } from '@angular/core';
import { Store } from '@ngrx/store';
import { Observable, catchError, map, of } from 'rxjs';
import { UserDTO } from '../dtos/user.dto';
import { setUser } from '../store/user.actions';
import { State, userSelector } from '../store/user.reducer';
import { PersistenceService } from './persistence.service';

@Injectable({
  providedIn: 'root'
})
export class AuthService {

  private requestAlredySent = false;

  constructor(private persistenceService: PersistenceService, private store: Store<State>) {
    this.getUser().subscribe(user => !user.uuid && !this.requestAlredySent && this.persistenceService.getRequest('/v1/user/dto').pipe(
      map(user => user),
      catchError(() => of({} as UserDTO))
    ).subscribe((user) => {
      if (user) {
        this.requestAlredySent = true;
        this.setUser(user as UserDTO);
      }
    }))
  }

  setUser(user: UserDTO) {
    this.store.dispatch(setUser(user));
  }

  getUser(): Observable<UserDTO> {
    return this.store.select(userSelector);
  }

  isAuthenticated(): Observable<boolean> {
    return this.persistenceService.getRequest('/v1/auth/validate').pipe(
      map(() => true),
      catchError(() => of(false))
    )
  }

}
