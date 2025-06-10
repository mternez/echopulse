import {RoleDetails} from "./role-details";

export interface ServerSummary {
  id: string;
  name: string;
}

export interface MembershipSummary {
  server: ServerSummary;
  roles: RoleDetails[];
}

export interface UserSummary {
  id: string;
  username: string;
  displayName: string;
  memberships: MembershipSummary[];
}
