import {Component, inject, signal} from '@angular/core';
import {RouterOutlet} from '@angular/router';
import {UserSidebardComponent} from "./features/user/containers/user-sidebard/user-sidebard.component";
import {ServerContainerComponent} from "./features/server/containers/server-container/server-container.component";
import {ServerSummary} from "./model/user-summary";
import {GlobalStore} from "./store/global.store";
import {MessageService} from "primeng/api";
import {ToastModule} from "primeng/toast";
import {SplitterModule} from "primeng/splitter";

@Component({
  selector: 'app-root',
  standalone: true,
  imports: [RouterOutlet, UserSidebardComponent, ServerContainerComponent, ToastModule, SplitterModule],
  providers: [GlobalStore],
  templateUrl: './app.component.html',
  styleUrl: './app.component.css'
})
export class AppComponent {
  title = 'echo-pulse-front';
  readonly globalStore = inject(GlobalStore);
  selectedServer = this.globalStore.selectedServer;
}
