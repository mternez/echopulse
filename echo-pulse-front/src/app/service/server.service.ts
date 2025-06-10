import {inject, Injectable} from "@angular/core";
import {HttpClient} from "@angular/common/http";
import {ServerDetails} from "../model/server-details";
import {ServerMemberDetails} from "../model/server-member-details";
import {ChannelDetails} from "../model/channel-details";
import {ChannelCreationResource} from "./model/channel-creation-resource";
import {ServerCreationResource} from "./model/server-creation-resource";
import {MembershipSummary} from "../model/user-summary";

@Injectable({
  providedIn: 'root'
})
export class ServerService {

  private readonly httpClient = inject(HttpClient);

  getServerDetails(id: string) {
    return this.httpClient.get<ServerDetails>(`api/servers/${id}`);
  }

  getServerMemberDetails(id: string) {
    return this.httpClient.get<ServerMemberDetails[]>(`api/servers/${id}/members`);
  }

  getChannels(id: string) {
    return this.httpClient.get<ChannelDetails[]>(`api/servers/${id}/channels`);
  }

  createServer(resource: ServerCreationResource) {
    return this.httpClient.post<ServerDetails>(`api/servers`, resource);
  }

  createChannel(serverId: string, resource: ChannelCreationResource) {
    return this.httpClient.post<ChannelDetails>(`api/servers/${serverId}/channels`, resource);
  }

  deleteServer(id: string) {
    return this.httpClient.delete(`api/servers/${id}`);
  }

  deleteChannel(serverId: string, channelId: string) {
    return this.httpClient.delete(`api/servers/${serverId}/channels/${channelId}`);
  }

  joinServer(serverId: string) {
    return this.httpClient.post<MembershipSummary>(`api/servers/${serverId}/members`, {});
  }

  leaveServer(serverId: string) {
    return this.httpClient.delete(`api/servers/${serverId}/members`, {});
  }
}
