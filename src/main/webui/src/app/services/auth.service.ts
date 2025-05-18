import { PersistenceService } from './persistence.service';
import { Injectable, signal } from '@angular/core';
import { UserDTO } from '../dtos/user.dto';
import { Observable, catchError, map, of, tap } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class AuthService {

  constructor(private persistenceService: PersistenceService) { }

  private _user = signal<UserDTO | null>(null);

  set user(user: UserDTO) {
    this._user.set(user);
  }

  get user(): UserDTO | null {
    return this._user();
  }

  isAuthenticated(): Observable<boolean> {
    return this.persistenceService.getRequest('/v1/auth/validate').pipe(
      map(() => true),
      catchError(() => of(false))
    )
  }
}
