import {Component, computed, input, output} from '@angular/core';
import {AvatarModule} from "primeng/avatar";
import {UpperCasePipe} from "@angular/common";
import {MembershipSummary, ServerSummary} from "../../../../model/user-summary";
import {SkeletonModule} from "primeng/skeleton";
import {ScrollPanelModule} from "primeng/scrollpanel";

@Component({
  selector: 'app-user-memberships-sidebar',
  standalone: true,
  imports: [
    AvatarModule,
    UpperCasePipe,
    SkeletonModule,
    ScrollPanelModule
  ],
  templateUrl: './user-memberships-sidebar.component.html',
  styleUrl: './user-memberships-sidebar.component.css'
})
export class UserMembershipsSidebarComponent {

  memberships = input.required<MembershipSummary[]>();
  servers = computed(() => this.memberships()?.map(m => m.server).sort((a,b) => a.id.localeCompare(b.id)));
  onClick = output<ServerSummary>();
  loading = input(false);
}
