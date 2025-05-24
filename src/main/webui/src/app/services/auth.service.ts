import { Injectable } from '@angular/core';
import { Store } from '@ngrx/store';
import { Observable, catchError, map, of, tap } from 'rxjs';
import { UserDTO } from '../dtos/user.dto';
import { setUser } from '../store/user.actions';
import { State, userSelector } from '../store/user.reducer';
import { PersistenceService } from './persistence.service';
import { Router } from '@angular/router';

@Injectable({
  providedIn: 'root'
})
export class AuthService {


  constructor(private persistenceService: PersistenceService, private store: Store<State>, private router: Router) {    
    this.isAuthenticated().pipe(
      tap(isAuthenticated => {
        if (!isAuthenticated) return;
        this.getUser().subscribe(user => !user.uuid && this.persistenceService.getRequest('/v1/user/dto').pipe(
          map(user => user),
          catchError(() => of({} as UserDTO))
        ).subscribe((user) => {
          if (user) {
            this.setUser(user as UserDTO);
          }
        }))
      })
    ).subscribe()
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

  logout(){
    this.persistenceService.getRequest('/v1/auth/logout').pipe(
      map(() => true),
      catchError(() => of(false))
    ).subscribe(() => {
      this.router.navigate(['/']);
    })
  }

}
