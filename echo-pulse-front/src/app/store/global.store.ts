import {patchState, signalStore, withComputed, withMethods, withState} from '@ngrx/signals';
import {MembershipSummary, ServerSummary, UserSummary} from "../model/user-summary";
import {ServerMemberDetails} from "../model/server-member-details";
import {ServerDetails} from "../model/server-details";
import {ChannelDetails} from "../model/channel-details";
import {computed, inject} from "@angular/core";
import {rxMethod} from "@ngrx/signals/rxjs-interop";
import {debounceTime, forkJoin, mergeMap, of, pipe, switchMap, tap} from "rxjs";
import {UserService} from "../service/user.service";
import {MessageService} from "primeng/api";
import {ServerService} from "../service/server.service";
import {ServerCreationResource} from "../service/model/server-creation-resource";
import {FormControl} from "@angular/forms";
import {ChannelCreationResource} from "../service/model/channel-creation-resource";
import {RxStompService} from "../service/rx-stomp.service";
import {PostMessage} from "../model/post";
import {PostService} from "../service/post.service";

type GlobalState = {
  user: UserSummary | undefined;
  userLoading: boolean;
  selectedServer: ServerSummary | undefined;
  selectedServerDetails: ServerDetails | undefined;
  selectedServerLoading: boolean;
  selectedServerMembers: ServerMemberDetails[];
  selectedServerMembersLoading: boolean;
  selectedServerChannels: ChannelDetails[];
  selectedServerChannelsLoading: boolean;
  createServerDialogVisible: boolean;
  createServerForm: FormControl | undefined;
  createServerInProgress: boolean;
  createChannelDialogVisible: boolean;
  createChannelForm: FormControl | undefined;
  createChannelInProgress: boolean;
  deleteServerInProgress: boolean;
  deleteChannelInProgress: boolean;
  joinServerDialogVisible: boolean;
  joinServerForm: FormControl | undefined;
  joinServerInProgress: boolean;
  leaveServerInProgress: boolean;
  currentChannel: ChannelDetails | undefined;
  rxStompService: RxStompService;
  latestPostsLoading: boolean;
  latestPosts: PostMessage[];
};

const initialState: GlobalState = {
  user: undefined,
  userLoading: false,
  selectedServer: undefined,
  selectedServerDetails: undefined,
  selectedServerLoading: false,
  selectedServerMembers: [],
  selectedServerMembersLoading: false,
  selectedServerChannels: [],
  selectedServerChannelsLoading: false,
  createServerDialogVisible: false,
  createServerForm: undefined,
  createServerInProgress: false,
  createChannelDialogVisible: false,
  createChannelForm: undefined,
  createChannelInProgress: false,
  deleteServerInProgress: false,
  deleteChannelInProgress: false,
  joinServerDialogVisible: false,
  joinServerForm: undefined,
  joinServerInProgress: false,
  leaveServerInProgress: false,
  currentChannel: undefined,
  rxStompService: new RxStompService(),
  latestPostsLoading: false,
  latestPosts: []
};

export const GlobalStore = signalStore(
  withState(initialState),
  withComputed(({ selectedServer, user }) => ({
    selectedServerId: computed(() => selectedServer()?.id),
    userMemberships: computed(() => user()?.memberships.map(m => m.server).sort((a,b) => a.id.localeCompare(b.id)))
  })),
  withMethods((
    store,
    userService = inject(UserService),
    serverService = inject(ServerService),
    messageService = inject(MessageService),
    postService = inject(PostService)
  ) => ({
    updateRxStompService: (rxStompService: RxStompService) => {
      patchState(store, {rxStompService});
    },
    selectServer: (selection: ServerSummary) => {
      patchState(store, {selectedServer: {...selection}});
    },
    setServerCreationForm: (form: FormControl) => {
      patchState(store, {createServerForm: form});
    },
    setServerCreationDialogVisible: (visible: boolean) => {
      if(!visible) {
        store.createServerForm()?.reset();
      }
      patchState(store, {createServerDialogVisible: visible});
    },
    setChannelCreationForm: (form: FormControl) => {
      patchState(store, {createChannelForm: form});
    },
    setChannelCreationDialogVisible: (visible: boolean) => {
      if(!visible) {
        store.createChannelForm()?.reset();
      }
      patchState(store, {createChannelDialogVisible: visible});
    },
    setJoinServerForm: (form: FormControl) => {
      patchState(store, {joinServerForm: form});
    },
    setJoinServerDialogVisible: (visible: boolean) => {
      if(!visible) {
        store.joinServerForm()?.reset();
      }
      patchState(store, {joinServerDialogVisible: visible});
    },
    setCurrentChannel: (currentChannel: ChannelDetails) => {
      patchState(store, {currentChannel});
    },
    updateSelectedServerChannels: (channels: ChannelDetails[]) => {
      patchState(store, {selectedServerChannels: channels});
    },
    updateSelectedServerMembers: (members: ServerMemberDetails[]) => {
      patchState(store, {selectedServerMembers: members});
    },
    loadUser: rxMethod<void>(
      pipe(
        debounceTime(300),
        tap(() => patchState(store, { userLoading: true })),
        switchMap(() => {
          return userService.getUser().pipe(
            tap({
              next: (user: UserSummary) => patchState(store, { user, userLoading: false }),
              error: () => {
                patchState(store, { userLoading: false });
                messageService.add({severity: 'failure', summary: 'Failed to load user', detail: 'Couldn\'t retrieve user.'})
              },
            })
          );
        })
      )
    ),
    loadSelectedServerDetails: rxMethod<void>(
      pipe(
        debounceTime(300),
        tap(() => patchState(store, { selectedServerLoading: true })),
        switchMap(() => {
          const serverId = store.selectedServerId();
          if(serverId) {
            return serverService.getServerDetails(serverId).pipe(
              tap({
                next: (serverDetails: ServerDetails) => patchState(store, { selectedServerDetails: serverDetails, selectedServerLoading: false }),
                error: () => {
                  patchState(store, { selectedServerLoading: false });
                  messageService.add({severity: 'failure', summary: 'Failed to load server', detail: 'Couldn\'t retrieve server details.'})
                },
              })
            );
          }
          return of(undefined);
        })
      )
    ),
    loadSelectedServerMembers: rxMethod<void>(
      pipe(
        debounceTime(300),
        tap(() => patchState(store, { selectedServerMembersLoading: true })),
        switchMap(() => {
          const serverId = store.selectedServerId();
          if(serverId) {
            return serverService.getServerMemberDetails(serverId).pipe(
              tap({
                next: (members: ServerMemberDetails[]) => patchState(store, { selectedServerMembers: members, selectedServerMembersLoading: false }),
                error: () => {
                  patchState(store, { selectedServerMembersLoading: false });
                  messageService.add({severity: 'failure', summary: 'Failed to load server members', detail: 'Couldn\'t retrieve server members.'})
                },
              })
            );
          }
          return of(undefined);
        })
      )
    ),
    loadSelectedServerChannels: rxMethod<void>(
      pipe(
        debounceTime(300),
        tap(() => patchState(store, { selectedServerChannelsLoading: true })),
        switchMap(() => {
          const serverId = store.selectedServerId();
          if(serverId) {
            return serverService.getChannels(serverId).pipe(
              tap({
                next: (members: ChannelDetails[]) => patchState(store, { selectedServerChannels: members, selectedServerChannelsLoading: false }),
                error: () => {
                  patchState(store, { selectedServerChannelsLoading: false });
                  messageService.add({severity: 'failure', summary: 'Failed to load server channels', detail: 'Couldn\'t retrieve server channels.'})
                },
              })
            );
          }
          return of(undefined);
        })
      )
    ),
    createServer: rxMethod<void>(
      pipe(
        debounceTime(300),
        tap(() => patchState(store, { createServerInProgress: true })),
        switchMap(() => {
          const resource: ServerCreationResource = {name: store.createServerForm()?.value!};
          return serverService
            .createServer(resource)
            .pipe(mergeMap(createdServer => forkJoin(of(createdServer), userService.getUser())))
            .pipe(
            tap({
              next: ([createdServer, user]) => {
                const serverSummary: ServerSummary = {id: createdServer.id, name: createdServer.name};
                patchState(store, { user, createServerDialogVisible: false, createServerInProgress: false });
                store.createServerForm()?.reset();
              },
              error: () => {
                patchState(store, { createServerInProgress: false });
                messageService.add({severity: 'failure', summary: 'Failed to create server', detail: 'Couldn\'t create server.'})
              }
            })
          );
        })
      )
    ),
    createChannel: rxMethod<void>(
      pipe(
        debounceTime(300),
        tap(() => patchState(store, { createChannelInProgress: true })),
        switchMap(() => {
          const resource: ChannelCreationResource = {name: store.createChannelForm()?.value!};
          const serverId = store.selectedServerId();
          return serverService.createChannel(serverId!, resource).pipe(
            tap({
              next: (createdChannel: ChannelDetails) => {
                const channelDetails: ChannelDetails = {id: createdChannel.id, name: createdChannel.name};
                const existingChannelIndex = store.selectedServerChannels().findIndex(c => c.id === channelDetails.id);
                if(existingChannelIndex === -1) {
                  const updatedChannels = [...store.selectedServerChannels(), channelDetails];
                  patchState(store, { selectedServerChannels: updatedChannels });
                }
                patchState(store, { createChannelDialogVisible: false, createChannelInProgress: false });
                store.createChannelForm()?.reset();
              },
              error: () => {
                patchState(store, { createChannelInProgress: false });
                messageService.add({severity: 'failure', summary: 'Failed to create channel', detail: 'Couldn\'t create channel.'})
              }
            })
          );
        })
      )
    ),
    deleteSelectedServer: rxMethod<void>(
      pipe(
        debounceTime(300),
        tap(() => patchState(store, { deleteServerInProgress: true })),
        switchMap(() => {
          const serverId = store.selectedServerId();
          return serverService.deleteServer(serverId!).pipe(
            tap({
              next: () => {
                const updatedMemberships: MembershipSummary[] = store.user()?.memberships.filter(m => m.server.id != serverId)!;
                const userClone = {
                  id: store.user()?.id!,
                  username: store.user()?.username!,
                  displayName: store.user()?.displayName!,
                  memberships: [...updatedMemberships]
                };
                patchState(store, {
                  user: userClone,
                  selectedServer: undefined,
                  selectedServerDetails: undefined,
                  selectedServerLoading: false,
                  selectedServerMembers: [],
                  selectedServerMembersLoading: false,
                  selectedServerChannels: [],
                  selectedServerChannelsLoading: false,
                  createServerDialogVisible: false,
                  createServerForm: undefined,
                  createServerInProgress: false,
                  createChannelDialogVisible: false,
                  createChannelForm: undefined,
                  createChannelInProgress: false,
                  deleteServerInProgress: false,
                  deleteChannelInProgress: false
                });
              },
              error: () => {
                patchState(store, { deleteServerInProgress: false });
                messageService.add({severity: 'failure', summary: 'Failed to delete server', detail: 'Couldn\'t delete server.'})
              }
            })
          );
        })
      )
    ),
    deleteChannel: rxMethod<string>(
      pipe(
        debounceTime(300),
        tap(() => patchState(store, { deleteChannelInProgress: true })),
        switchMap((channelId: string) => {
          const serverId = store.selectedServerId();
          return serverService.deleteChannel(serverId!, channelId).pipe(
            tap({
              next: () => {
                const channels = store.selectedServerChannels().filter(c => c.id != channelId);
                patchState(store, { selectedServerChannels: [...channels], deleteChannelInProgress: false });
              },
              error: () => {
                patchState(store, { deleteChannelInProgress: false });
                messageService.add({severity: 'failure', summary: 'Failed to delete channel', detail: 'Couldn\'t delete channel.'})
              }
            })
          );
        })
      )
    ),
    joinServer: rxMethod<void>(
      pipe(
        debounceTime(300),
        tap(() => patchState(store, { joinServerInProgress: true })),
        switchMap(() => {
          const serverId = store.joinServerForm()?.value!;
          return serverService.joinServer(serverId)
            .pipe(mergeMap(() => forkJoin(userService.getUser())))
            .pipe(
            tap({
              next: ([user]) => {
                patchState(store, { user: user, joinServerDialogVisible: false, joinServerInProgress: false });
                store.joinServerForm()?.reset();
              },
              error: () => {
                patchState(store, { joinServerInProgress: false });
                messageService.add({severity: 'failure', summary: 'Failed to join server', detail: 'Couldn\'t join server.'})
              }
            })
          );
        })
      )
    ),
    leaveServer: rxMethod<void>(
      pipe(
        debounceTime(300),
        tap(() => patchState(store, { leaveServerInProgress: true })),
        switchMap(() => {
          const serverId = store.selectedServerId()!;
          return serverService.leaveServer(serverId).pipe(
            tap({
              next: () => {
                const updatedMemberships: MembershipSummary[] = store.user()?.memberships.filter(m => m.server.id != serverId)!;
                const userClone = {
                  id: store.user()?.id!,
                  username: store.user()?.username!,
                  displayName: store.user()?.displayName!,
                  memberships: [...updatedMemberships]
                };
                patchState(store, {
                  user: userClone,
                  selectedServer: undefined,
                  selectedServerDetails: undefined,
                  selectedServerLoading: false,
                  selectedServerMembers: [],
                  selectedServerMembersLoading: false,
                  selectedServerChannels: [],
                  selectedServerChannelsLoading: false,
                  createServerDialogVisible: false,
                  createServerForm: undefined,
                  createServerInProgress: false,
                  createChannelDialogVisible: false,
                  createChannelForm: undefined,
                  createChannelInProgress: false,
                  deleteServerInProgress: false,
                  deleteChannelInProgress: false
                });
              },
              error: () => {
                patchState(store, { leaveServerInProgress: false });
                messageService.add({severity: 'failure', summary: 'Failed to leave server', detail: 'Couldn\'t leave server.'})
              }
            })
          );
        })
      )
    ),
    fetchLatestPosts: rxMethod<void>(
      pipe(
        debounceTime(300),
        tap(() => patchState(store, { latestPostsLoading: true })),
        switchMap(() => {
          const channelId = store.currentChannel()?.id;
          if(channelId) {
            return postService.getLatestPosts(channelId).pipe(
              tap({
                next: (latestPosts: PostMessage[]) => patchState(store, { latestPosts, latestPostsLoading: false }),
                error: () => {
                  patchState(store, { latestPostsLoading: false });
                  messageService.add({severity: 'failure', summary: 'Failed to load latest posts', detail: 'Couldn\'t retrieve latest posts.'})
                },
              })
            );
          }
          return of(undefined);
        })
      )
    )
  }))
);
