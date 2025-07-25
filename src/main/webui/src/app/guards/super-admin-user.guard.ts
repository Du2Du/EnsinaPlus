import { inject } from '@angular/core';
import { CanActivateFn, Router } from '@angular/router';
import { map, tap } from 'rxjs';
import { RoleEnum } from '../enums/roleEnum';
import { AuthService } from '../services/auth.service';

export const superAdminUserGuard: CanActivateFn = (route, state) => {
  const authService = inject(AuthService);
  const router = inject(Router);

  return authService.getUser().pipe(
    tap(user => {
      if (user.role !== RoleEnum.SUPER_ADMIN) {
        router.navigate([user.role === RoleEnum.ADMIN ? '/search/' : '/home']);
      }
    }),
    map(user => user.role === RoleEnum.SUPER_ADMIN)
  );
};
