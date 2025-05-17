import { bootstrapApplication } from '@angular/platform-browser';
import * as Sentry from "@sentry/angular";
import { AppComponent } from './app/app.component';
import { appConfig } from './app/app.config';

Sentry.init({
  dsn: 'https://68c99e72e412fb211dadc7283d4b3231@o4508059939962880.ingest.us.sentry.io/4508059941142528',
  sendDefaultPii: true,
  integrations: [Sentry.browserTracingIntegration()],
  tracesSampleRate: 1.0,
  tracePropagationTargets: ["localhost"],
});

bootstrapApplication(AppComponent, appConfig)
  .catch((err) => console.error(err));
