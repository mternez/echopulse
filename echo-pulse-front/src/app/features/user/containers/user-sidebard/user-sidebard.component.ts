import {Component, inject, OnInit} from '@angular/core';
import { UserMembershipsSidebarComponent } from "../../components/user-memberships-sidebar/user-memberships-sidebar.component";
import {UserProfileCardComponent} from "../../components/user-profile-card/user-profile-card.component";
import {ServerSummary} from "../../../../model/user-summary";
import {DialogModule} from "primeng/dialog";
import {ButtonDirective} from "primeng/button";
import {FormControl, ReactiveFormsModule, Validators} from "@angular/forms";
import {InputTextModule} from "primeng/inputtext";
import {GlobalStore} from "../../../../store/global.store";

@Component({
  selector: 'app-user-sidebard',
  standalone: true,
  imports: [
    UserMembershipsSidebarComponent,
    UserProfileCardComponent,
    DialogModule,
    ButtonDirective,
    InputTextModule,
    ReactiveFormsModule
  ],
  templateUrl: './user-sidebard.component.html',
  styleUrl: './user-sidebard.component.css'
})
export class UserSidebardComponent implements OnInit {

  readonly globalStore = inject(GlobalStore);

  user = this.globalStore.user;
  creationDialogVisible = this.globalStore.createServerDialogVisible;
  createServerInProgress = this.globalStore.createServerInProgress;
  serverNameFormControl = new FormControl<string>("", Validators.required);
  joinServerFormControl = new FormControl<string>("", Validators.required);
  joinServerDialogVisible = this.globalStore.joinServerDialogVisible;
  joinServerInProgress = this.globalStore.joinServerInProgress;

  ngOnInit() {
    this.globalStore.loadUser();
    this.globalStore.setServerCreationForm(this.serverNameFormControl);
    this.globalStore.setJoinServerForm(this.joinServerFormControl);
  }

  setServerCreationDialogStatus(event: boolean) {
    this.globalStore.setServerCreationDialogVisible(event);
  }

  selectServer(server: ServerSummary) {
    this.globalStore.selectServer(server);
  }

  onCreateServer() {
    if(this.serverNameFormControl.valid) {
      this.globalStore.createServer();
    }
  }

  setJoinServerDialogStatus(event: boolean) {
    this.globalStore.setJoinServerDialogVisible(event);
  }

  onJoinServer() {
    this.globalStore.joinServer();
  }
}
