import {Component, DestroyRef, inject, OnInit, signal} from '@angular/core';
import {
  ServerChannelsSidebarComponent
} from "../../components/server-channels-sidebar/server-channels-sidebar.component";
import {ServerMembersSidebarComponent} from "../../components/server-members-sidebar/server-members-sidebar.component";
import {GlobalStore} from "../../../../store/global.store";
import {takeUntilDestroyed, toObservable} from "@angular/core/rxjs-interop";
import {ButtonDirective} from "primeng/button";
import {SkeletonModule} from "primeng/skeleton";
import {CdkCopyToClipboard} from "@angular/cdk/clipboard";
import {RxStompService} from "../../../../service/rx-stomp.service";
import {KeycloakService} from "keycloak-angular";
import {FormsModule} from "@angular/forms";
import {Subscription} from "rxjs";
import {IMessage} from "@stomp/stompjs";
import {parseJson} from "@angular/cli/src/utilities/json-file";
import {ChannelEventMessage, UserJoinedEventMessage, UserLeftEventMessage} from "../../../../model/event-message";
import {ChannelDetails} from "../../../../model/channel-details";
import {ServerMemberDetails} from "../../../../model/server-member-details";
import {ServerChannelContentComponent} from "../../components/server-channel-content/server-channel-content.component";
import {fromPromise} from "rxjs/internal/observable/innerFrom";
import {HasPermissionDirective} from "../../../../directives/has-permission.directive";

@Component({
  selector: 'app-server-container',
  standalone: true,
  imports: [
    ServerMembersSidebarComponent,
    ServerChannelsSidebarComponent,
    ButtonDirective,
    SkeletonModule,
    CdkCopyToClipboard,
    FormsModule,
    ServerChannelContentComponent,
    HasPermissionDirective,
  ],
  templateUrl: './server-container.component.html',
  styleUrl: './server-container.component.css'
})
export class ServerContainerComponent implements OnInit {

  readonly globalStore = inject(GlobalStore);
  readonly destroyRef = inject(DestroyRef);
  readonly keycloakService = inject(KeycloakService);

  selectedServer = this.globalStore.selectedServer;
  selectedServerDetails = this.globalStore.selectedServerDetails;
  selectedServerDetailsLoading = this.globalStore.selectedServerLoading;
  selectedServerMembers = this.globalStore.selectedServerMembers;
  selectedServerMembersLoading = this.globalStore.selectedServerMembersLoading;
  selectedServerChannels = this.globalStore.selectedServerChannels;
  selectedServerChannelsLoading = this.globalStore.selectedServerChannelsLoading;
  currentChannel = this.globalStore.currentChannel;

  constructor() {
    toObservable(this.selectedServer).pipe(takeUntilDestroyed(this.destroyRef)).subscribe(
      (selection) => {
        this.globalStore.loadSelectedServerDetails();
        this.globalStore.loadSelectedServerMembers();
        this.globalStore.loadSelectedServerChannels();
      }
    );
  }

  ngOnInit() {
    fromPromise(this.keycloakService.getToken())
    .subscribe(
      (token) => {
        if(this.globalStore.rxStompService().active) {
          this.globalStore.rxStompService().deactivate({force: true});
        }
        const rxStompService = new RxStompService();
        rxStompService.configure({
          // Which server?
          brokerURL: `ws://127.0.0.1:8181/ws?token=${token}`,
          connectHeaders: {
            'Authorization': `Bearer ${token}`
          },
          disconnectHeaders: {
            'Authorization': `Bearer ${token}`
          },
          // How often to heartbeat?
          // Interval in milliseconds, set to 0 to disable
          heartbeatIncoming: 0, // Typical value 0 - disabled
          heartbeatOutgoing: 20000, // Typical value 20000 - every 20 seconds

          // Wait in milliseconds before attempting auto reconnect
          // Set to 0 to disable
          // Typical value 500 (500 milli seconds)
          reconnectDelay: 200,

          // Will log diagnostics on console
          // It can be quite verbose, not recommended in production
          // Skip this key to stop logging to console
          debug: (msg: string): void => {
            console.log(new Date(), msg);
          }
        });
        rxStompService.activate();
        rxStompService.watch(`/servers/${this.selectedServer()?.id}/channels/created`, {'Authorization': `Bearer ${token}`}).subscribe(
          (message: IMessage) => {
            const body: ChannelEventMessage = parseJson(message.body);
            const newChannel: ChannelDetails = {
              id: body.event.id.value,
              name: body.event.name
            };
            const currentChannels = this.globalStore.selectedServerChannels();
            const existingChannelIndex = currentChannels.findIndex(c => c.id === newChannel.id);
            if(existingChannelIndex === -1) {
              this.globalStore.updateSelectedServerChannels([...currentChannels, newChannel]);
            }
          }
        );
        rxStompService.watch(`/servers/${this.selectedServer()?.id}/channels/deleted`, {'Authorization': `Bearer ${token}`}).subscribe(
          (message: IMessage) => {
            const body: ChannelEventMessage = parseJson(message.body);
            const currentChannels = this.globalStore.selectedServerChannels();
            const deletedChannelIndex = currentChannels.findIndex(c => c.id === body.event.id.value);
            if(deletedChannelIndex !== -1) {
              currentChannels.splice(deletedChannelIndex,1);
              this.globalStore.updateSelectedServerChannels([...currentChannels]);
            }
          }
        );
        rxStompService.watch(`/servers/${this.selectedServer()?.id}/user-joined`, {'Authorization': `Bearer ${token}`}).subscribe(
          (message: IMessage) => {
            const body: UserJoinedEventMessage = parseJson(message.body);
            const newMember: ServerMemberDetails = {
              user: {
                id: body.event.userId.value,
                name: body.event.username,
                displayName: body.event.displayName
              },
              roles: body.event.roles
            };
            const currentMembers = this.globalStore.selectedServerMembers();
            const existingMemberIndex = currentMembers.findIndex(m => m.user.id === body.event.userId.value);
            if(existingMemberIndex === -1) {
              this.globalStore.updateSelectedServerMembers([...currentMembers, newMember]);
            }
          }
        );
        rxStompService.watch(`/servers/${this.selectedServer()?.id}/user-left`, {'Authorization': `Bearer ${token}`}).subscribe(
          (message: IMessage) => {
            const body: UserLeftEventMessage = parseJson(message.body);
            const currentMembers = this.globalStore.selectedServerMembers();
            const deletedMemberIndex = currentMembers.findIndex(m => m.user.id === body.event.userId.value);
            if(deletedMemberIndex !== -1) {
              currentMembers.splice(deletedMemberIndex,1);
              this.globalStore.updateSelectedServerMembers([...currentMembers]);
            }
          }
        );
        this.globalStore.updateRxStompService(rxStompService);
      }
    );
  }

  onDeleteServer() {
    this.globalStore.deleteSelectedServer();
  }

  onLeaveServer() {
    this.globalStore.leaveServer();
  }
}
