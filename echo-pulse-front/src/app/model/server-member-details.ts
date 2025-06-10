import {RoleDetails} from "./role-details";

export interface ServerMemberDetails {
  user: {id: string, name: string; displayName: string};
  roles: RoleDetails[];
}
