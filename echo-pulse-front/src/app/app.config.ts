import {APP_INITIALIZER, ApplicationConfig, provideZoneChangeDetection} from '@angular/core';
import {provideRouter} from '@angular/router';
import {routes} from './app.routes';
import {HTTP_INTERCEPTORS, provideHttpClient, withInterceptors} from "@angular/common/http";
import {KeycloakBearerInterceptor, KeycloakService} from "keycloak-angular";
import {apiCallInterceptor} from "./interceptor/api-call.interceptor";
import {provideAnimationsAsync} from '@angular/platform-browser/animations/async';
import {MessageService} from "primeng/api";

function initializeKeycloak(keycloak: KeycloakService) {
  return () =>
    keycloak.init({
      config: {
        url: 'http://localhost:18080',
        realm: 'echopulse',
        clientId: 'echo-pulse-front-end'
      },
      initOptions: {
        onLoad: 'login-required',
        checkLoginIframe: false
      },
      enableBearerInterceptor: true,
      bearerPrefix: 'Bearer',
      bearerExcludedUrls: []
    });
}

export const appConfig: ApplicationConfig = {
  providers: [
    provideZoneChangeDetection({ eventCoalescing: true }),
    KeycloakService,
    {
      provide: HTTP_INTERCEPTORS,
      useClass: KeycloakBearerInterceptor,
      multi: true,
      deps: [KeycloakService]
    },
    {
      provide: APP_INITIALIZER,
      useFactory: initializeKeycloak,
      multi: true,
      deps: [KeycloakService]
    },
    provideRouter(routes),
    provideHttpClient(withInterceptors([apiCallInterceptor])),
    provideAnimationsAsync(),
    MessageService
  ]
};
