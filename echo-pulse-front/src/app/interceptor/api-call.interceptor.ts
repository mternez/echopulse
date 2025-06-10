import {HttpContextToken, HttpInterceptorFn} from '@angular/common/http';
import {KeycloakService} from "keycloak-angular";
import {inject} from "@angular/core";
import {fromPromise} from "rxjs/internal/observable/innerFrom";
import {mergeMap} from "rxjs";
import {environment} from "../../environments/environment";

export const HOST_CTX_TOKEN = new HttpContextToken(() => environment.serverHost);

export const apiCallInterceptor: HttpInterceptorFn = (req, next) => {

  const keycloak = inject(KeycloakService);
  return fromPromise(keycloak.getToken()).pipe(mergeMap(token => {
    const apiCall = req.clone({
      url: `${req.context.get(HOST_CTX_TOKEN)}${req.url}`,
      headers: req.headers.set('Authorization', `Bearer ${token}`)
    });
    return next(apiCall);
  }));
};
