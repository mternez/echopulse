import {RoleDetails} from "./role-details";

export interface ServerDetails {
  id: string;
  name: string;
  defaultRole: RoleDetails;
  roles: RoleDetails[];
}
