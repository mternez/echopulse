import {Component, inject, input, OnInit, output} from '@angular/core';
import {ChannelDetails} from "../../../../model/channel-details";
import {SkeletonModule} from "primeng/skeleton";
import {ButtonDirective} from "primeng/button";
import {DialogModule} from "primeng/dialog";
import {InputTextModule} from "primeng/inputtext";
import {FormControl, ReactiveFormsModule, Validators} from "@angular/forms";
import {GlobalStore} from "../../../../store/global.store";
import {HasPermissionDirective} from "../../../../directives/has-permission.directive";
import {NgClass} from "@angular/common";

@Component({
  selector: 'app-server-channels-sidebar',
  standalone: true,
  imports: [
    SkeletonModule,
    ButtonDirective,
    DialogModule,
    InputTextModule,
    ReactiveFormsModule,
    HasPermissionDirective,
    NgClass
  ],
  templateUrl: './server-channels-sidebar.component.html',
  styleUrl: './server-channels-sidebar.component.css'
})
export class ServerChannelsSidebarComponent implements OnInit {

  readonly globalStore = inject(GlobalStore);

  loading = input(false);
  channels = input.required<ChannelDetails[]>();
  onOpenChannel = output<ChannelDetails>();

  selectedServer = this.globalStore.selectedServer;
  selectedChannel = this.globalStore.currentChannel;
  creationDialogVisible = this.globalStore.createChannelDialogVisible;
  createChannelInProgress = this.globalStore.createChannelInProgress;
  channelNameFormControl = new FormControl<string>("", Validators.required);
  deleteChannelInProgress = this.globalStore.deleteChannelInProgress;

  ngOnInit() {
    this.globalStore.setChannelCreationForm(this.channelNameFormControl);
  }

  setChannelCreationDialogStatus(event: boolean) {
    this.globalStore.setChannelCreationDialogVisible(event);
  }

  onCreateServer() {
    if(this.channelNameFormControl.valid) {
      this.globalStore.createChannel();
    }
  }

  onDeleteChannel(channel: ChannelDetails) {
    this.globalStore.deleteChannel(channel.id);
  }

  onSelectChannel(channel: ChannelDetails) {
    this.globalStore.setCurrentChannel(channel);
    this.globalStore.fetchLatestPosts();
    this.onOpenChannel.emit(channel);
  }
}
