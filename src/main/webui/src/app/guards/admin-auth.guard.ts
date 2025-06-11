import { inject } from '@angular/core';
import { CanActivateFn, Router } from '@angular/router';
import { map, tap } from 'rxjs';
import { AuthService } from '../services/auth.service';
import { RoleEnum } from '../enums/roleEnum';

export const adminAuthGuard: CanActivateFn = (route, state) => {
  const authService = inject(AuthService);
  const router = inject(Router);

  return authService.getUser().pipe(
    tap(user => {
      if (user.role !== RoleEnum.ADMIN && user.role !== RoleEnum.SUPER_ADMIN) {
        router.navigate(['/home']);
      }
    }),
    map(user => user.role === RoleEnum.ADMIN || user.role === RoleEnum.SUPER_ADMIN)
  );
};
