import { CanActivateFn } from '@angular/router';
import { inject } from '@angular/core';
import { AuthService } from '../services/auth.service';
import { Router } from '@angular/router';
import { map, tap } from 'rxjs';

export const redirectAuthenticationGuard: CanActivateFn = (route, state) => {
  const authService = inject(AuthService);
  const router = inject(Router);
  
   return authService.isAuthenticated().pipe(
     tap(isAuthenticated => {
       if (isAuthenticated) {
         router.navigate(['/home']);
       }
     }),
     map(isAuthenticated => !isAuthenticated)
   );
};
