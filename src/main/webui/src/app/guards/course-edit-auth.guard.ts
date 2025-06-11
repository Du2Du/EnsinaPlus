import { inject } from '@angular/core';
import { CanActivateFn, Router } from '@angular/router';
import { map } from 'rxjs';
import { AuthService } from '../services/auth.service';
import { RoleEnum } from '../enums/roleEnum';

export const courseEditAuthGuard: CanActivateFn = (route, state) => {
  const authService = inject(AuthService);
  const router = inject(Router);

  return authService.getUser().pipe(
    map(user => {
      const hasValidRole = user.role === RoleEnum.TEACHER ||
        user.role === RoleEnum.ADMIN ||
        user.role === RoleEnum.SUPER_ADMIN;
      if (!hasValidRole) {
        router.navigate(['/home']);
      }

      return hasValidRole;
    })
  );
};