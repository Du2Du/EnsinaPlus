import { ApplicationConfig, ErrorHandler, provideZoneChangeDetection, APP_INITIALIZER } from '@angular/core';
import { provideRouter, Router } from '@angular/router';
import * as Sentry from "@sentry/angular";

import { routes } from './app.routes';
import { provideAnimationsAsync } from '@angular/platform-browser/animations/async';
import { providePrimeNG } from 'primeng/config';
import { EnsinaPlusTheme } from '../themes/theme';

export const appConfig: ApplicationConfig = {
  providers: [provideZoneChangeDetection({ eventCoalescing: true }), provideRouter(routes),
  provideAnimationsAsync(),
  providePrimeNG({
    theme: {
      preset: EnsinaPlusTheme,
      options: {
        darkModeSelector: '.e-dark'
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
    useFactory: () => () => {},
    deps: [Sentry.TraceService],
    multi: true,
  },
  ]
};
