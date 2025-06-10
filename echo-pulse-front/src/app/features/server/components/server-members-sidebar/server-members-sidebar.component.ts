import {Component, input} from '@angular/core';
import {ServerMemberDetails} from "../../../../model/server-member-details";
import {AvatarModule} from "primeng/avatar";
import {UpperCasePipe} from "@angular/common";
import {SkeletonModule} from "primeng/skeleton";

@Component({
  selector: 'app-server-members-sidebar',
  standalone: true,
  imports: [
    AvatarModule,
    UpperCasePipe,
    SkeletonModule
  ],
  templateUrl: './server-members-sidebar.component.html',
  styleUrl: './server-members-sidebar.component.css'
})
export class ServerMembersSidebarComponent {
  loading = input(false);
  members = input.required<ServerMemberDetails[]>();
}
