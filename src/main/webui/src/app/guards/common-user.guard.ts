import { inject } from '@angular/core';
import { CanActivateFn, Router } from '@angular/router';
import { map, tap } from 'rxjs';
import { UserTypeEnum } from '../enums/userTypeEnum';
import { AuthService } from '../services/auth.service';
import { RoleEnum } from '../enums/roleEnum';

export const commonUserGuard: CanActivateFn = (route, state) => {
  const authService = inject(AuthService);
  const router = inject(Router);

  return authService.getUser().pipe(
    tap(user => {  
      if (user.role !== RoleEnum.STUDENT && user.role!== RoleEnum.TEACHER) {
        router.navigate(['/audit']);
      }
    }),
    map(user => user.role === RoleEnum.STUDENT || user.role === RoleEnum.TEACHER)
  );
};
