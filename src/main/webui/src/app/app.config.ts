import { APP_INITIALIZER, ApplicationConfig, ErrorHandler, LOCALE_ID, provideZoneChangeDetection } from '@angular/core';
import { provideRouter, Router } from '@angular/router';
import * as Sentry from "@sentry/angular";

import { provideHttpClient, withInterceptors } from '@angular/common/http';
import { provideAnimationsAsync } from '@angular/platform-browser/animations/async';
import { provideStore } from '@ngrx/store';
import { providePrimeNG } from 'primeng/config';
import { EnsinaPlusTheme } from '../themes/theme';
import { routes } from './app.routes';
import { authInterceptorInterceptor } from './interceptors/auth-interceptor.interceptor';
import { userReducer } from './store/user.reducer';

export const appConfig: ApplicationConfig = {
  providers: [provideZoneChangeDetection({ eventCoalescing: true }), provideRouter(routes),
  provideAnimationsAsync(),
  provideStore({user: userReducer}),
  provideHttpClient(withInterceptors([authInterceptorInterceptor])),
  providePrimeNG({
    theme: {
      preset: EnsinaPlusTheme,
      options: {
        darkModeSelector: '.e-dark',
        prefix: 'e'
      }
    }
  }),
  {
    provide: ErrorHandler,
    useValue: Sentry.createErrorHandler(),
  },
  {
    provide: Sentry.TraceService,
    deps: [Router],
  },
  {
    provide: APP_INITIALIZER,
    useFactory: () => () => { },
    deps: [Sentry.TraceService],
    multi: true,
  },
  ]
};
