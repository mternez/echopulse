import {RoleDetails} from "./role-details";

export interface ChannelEventMessage {
  event: {
    id: { value: string }, name: string
  }
};

export interface UserJoinedEventMessage {
  event: {
    userId: { value: string },
    serverId: { value: string },
    username: string,
    displayName: string,
    roles: RoleDetails[]
  }
};

export interface UserLeftEventMessage {
  event: {
    userId: { value: string },
    serverId: { value: string }
  }
};
