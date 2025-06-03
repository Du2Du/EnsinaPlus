import { Injectable } from '@angular/core';
import { Router } from '@angular/router';
import { Store } from '@ngrx/store';
import { Observable, catchError, map, of, switchMap, tap } from 'rxjs';
import { UserDTO } from '../dtos/user.dto';
import { setUser } from '../store/user.actions';
import { State, userSelector } from '../store/user.reducer';
import { PersistenceService } from './persistence.service';

@Injectable({
  providedIn: 'root'
})
export class AuthService {


  constructor(private persistenceService: PersistenceService, private store: Store<State>, private router: Router) {
    this.findUser();
  }

  setUser(user: UserDTO) {
    this.store.dispatch(setUser(user));
  }

  getUser(): Observable<UserDTO> {
    return this.store.select(userSelector).pipe(
      switchMap(userDTO => {
        if (!userDTO.uuid) {
          return this.searchUser();
        }
        return of(userDTO);
      })
    );
  }

  isAuthenticated(): Observable<boolean> {
    return this.persistenceService.getRequest('/v1/auth/validate').pipe(
      map(() => true),
      catchError(() => of(false))
    )
  }

  logout() {
    this.persistenceService.getRequest('/v1/auth/logout').pipe(
      map(() => true),
      catchError(() => of(false))
    ).subscribe(() => {
      this.router.navigate(['/']);
    })
  }

  private findUser() {
    return this.isAuthenticated().pipe(
      tap(isAuthenticated => {
        if (!isAuthenticated) return;
        return this.searchUser();
      })
    ).subscribe();
  }

  searchUser(): Observable<UserDTO> {
    return this.persistenceService.getRequest('/v1/user/dto').pipe(
      tap(userDTO => {
        if (userDTO) {
          this.setUser(userDTO as UserDTO);
        }
      }),
      map(userDTO => userDTO as UserDTO),
      catchError(() => of({} as UserDTO))
    );
  }
}
