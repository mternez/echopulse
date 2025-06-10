import {Component, inject, signal} from '@angular/core';
import {PaginatorModule} from "primeng/paginator";
import {GlobalStore} from "../../../../store/global.store";
import {KeycloakService} from "keycloak-angular";
import {forkJoin, mergeMap, of, Subscription} from "rxjs";
import {IMessage} from "@stomp/stompjs";
import {toObservable} from "@angular/core/rxjs-interop";
import {fromPromise} from "rxjs/internal/observable/innerFrom";
import {parseJson} from "@angular/cli/src/utilities/json-file";
import {PostMessage} from "../../../../model/post";
import {DatePipe} from "@angular/common";
import {PostService} from "../../../../service/post.service";
import {InputTextModule} from "primeng/inputtext";
import {InputTextareaModule} from "primeng/inputtextarea";

@Component({
  selector: 'app-server-channel-content',
  standalone: true,
  imports: [
    PaginatorModule,
    DatePipe,
    InputTextModule,
    InputTextareaModule
  ],
  templateUrl: './server-channel-content.component.html',
  styleUrl: './server-channel-content.component.css'
})
export class ServerChannelContentComponent {

  readonly globalStore = inject(GlobalStore);
  readonly keycloakService = inject(KeycloakService);
  readonly postService = inject(PostService);
  readonly rxStompService = this.globalStore.rxStompService;

  currentChannelSubscription: Subscription | undefined = undefined;

  sendingMessage = signal<boolean>(false);
  message: string = "";
  messages = signal<PostMessage[]>([]);


  constructor() {
    toObservable(this.globalStore.latestPosts)
      .subscribe(
        (posts) => this.messages.set(posts)
      );
    toObservable(this.globalStore.currentChannel)
      .pipe(mergeMap((channel) => forkJoin(of(channel), fromPromise(this.keycloakService.getToken()))))
      .subscribe(([currentChannel, token]) => {
        if(this.currentChannelSubscription) {
          this.currentChannelSubscription.unsubscribe();
        }
        this.currentChannelSubscription = this.rxStompService().watch(
          `/channels/${currentChannel?.id}`,
          {'Authorization': `Bearer ${token}`}
        )
          .subscribe((data: IMessage) => {
            const body: PostMessage = parseJson(data.body);
            console.log(parseJson(data.body));
            this.messages.update(m  => [...m, body]);
          });
      });
  }

  onKeyDownSend(event: KeyboardEvent) {
    if(event.code === 'Enter')
      this.onSend();
  }

  onSend() {
    this.sendingMessage.set(true);
    this.keycloakService
      .getToken()
      .then( token => {
        this.rxStompService()
          .publish({ destination: `/app/channels/${this.globalStore.currentChannel()?.id}`, headers: {'Authorization': `Bearer ${token}`}, body: JSON.stringify(this.message) });
        this.sendingMessage.set(false);
        this.message = "";
      }).finally(() => this.sendingMessage.set(false));
  }
}
