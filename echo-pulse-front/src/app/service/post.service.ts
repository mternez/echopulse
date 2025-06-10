import {inject, Injectable} from "@angular/core";
import {HttpClient, HttpContext} from "@angular/common/http";
import {PostMessage} from "../model/post";
import {HOST_CTX_TOKEN} from "../interceptor/api-call.interceptor";
import {environment} from "../../environments/environment";

@Injectable({
  providedIn: 'root'
})
export class PostService {

  private readonly httpClient = inject(HttpClient);

  getLatestPosts(channelId: string) {
    return this.httpClient.get<PostMessage[]>(`api/channels/${channelId}/posts`, {
      context: new HttpContext().set(HOST_CTX_TOKEN, environment.chatHost),
    });
  }
}
