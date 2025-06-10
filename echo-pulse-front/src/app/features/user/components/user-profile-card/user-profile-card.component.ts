import {Component, computed, input} from '@angular/core';
import {AvatarModule} from "primeng/avatar";
import {UpperCasePipe} from "@angular/common";
import {ButtonDirective} from "primeng/button";
import {SkeletonModule} from "primeng/skeleton";

@Component({
  selector: 'app-user-profile-card',
  standalone: true,
  templateUrl: './user-profile-card.component.html',
  imports: [
    AvatarModule,
    UpperCasePipe,
    ButtonDirective,
    SkeletonModule
  ],
  styleUrl: './user-profile-card.component.css'
})
export class UserProfileCardComponent {

  id = input.required<string>();
  username = input.required<string>();
  displayName = input.required<string>();
  avatarLabel = computed(() => this.displayName() ? this.displayName().substring(0, 2) : 'N/A');
  loading = input(false);
}
