import {Component, inject, OnInit} from '@angular/core';
import {RouterOutlet} from '@angular/router';
import {UserSidebardComponent} from "./features/user/containers/user-sidebard/user-sidebard.component";
import {ServerContainerComponent} from "./features/server/containers/server-container/server-container.component";
import {GlobalStore} from "./store/global.store";
import {ToastModule} from "primeng/toast";
import {SplitterModule} from "primeng/splitter";
import {UserProfileCardComponent} from "./features/user/components/user-profile-card/user-profile-card.component";

@Component({
  selector: 'app-root',
  standalone: true,
    imports: [RouterOutlet, UserSidebardComponent, ServerContainerComponent, ToastModule, SplitterModule, UserProfileCardComponent],
  providers: [GlobalStore],
  templateUrl: './app.component.html',
  styleUrl: './app.component.css'
})
export class AppComponent implements OnInit {

  title = 'echo-pulse-front';

  readonly globalStore = inject(GlobalStore);
  selectedServer = this.globalStore.selectedServer;
  user = this.globalStore.user;

  ngOnInit() {
    this.globalStore.loadUser();
  }
}
