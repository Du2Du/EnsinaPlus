import { bootstrapApplication } from '@angular/platform-browser';
import { appConfig } from './app/app.config';
import { AppComponent } from './app/app.component';
import * as Sentry from "@sentry/angular";
import { environment } from './environments/environment';

Sentry.init({
  dsn: environment.sentryDns,
  sendDefaultPii: true,
  integrations: [Sentry.browserTracingIntegration()],
  tracesSampleRate: 1.0,
});

bootstrapApplication(AppComponent, appConfig)
  .catch((err) => console.error(err));
