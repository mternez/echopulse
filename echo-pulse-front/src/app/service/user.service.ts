import {inject, Injectable} from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {UserSummary} from "../model/user-summary";

@Injectable({
  providedIn: 'root'
})
export class UserService {

  private readonly httpClient = inject(HttpClient);

  getUser() {
    return this.httpClient.get<UserSummary>("api/users");
  }
}
