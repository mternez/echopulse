import {DestroyRef, Directive, ElementRef, inject, input} from '@angular/core';
import {GlobalStore} from "../store/global.store";
import {takeUntilDestroyed, toObservable} from "@angular/core/rxjs-interop";

@Directive({
  selector: '[appHasPermission]',
  standalone: true
})
export class HasPermissionDirective {

  private readonly globalStore = inject(GlobalStore);
  private readonly el = inject(ElementRef);
  private readonly destroyRef = inject(DestroyRef);

  serverId = input.required<string>();
  permission = input.required<string>();

  constructor() {
    toObservable(this.globalStore.user)
      .pipe(takeUntilDestroyed(this.destroyRef))
      .subscribe(
        (user) => {
          const membership = user!.memberships.find(m => m.server.id === this.serverId());
          if(membership) {
            const roleWithRequiredPermission = membership.roles.find(r => r.permissions.indexOf(this.permission()) !== -1);
            if(!roleWithRequiredPermission) {
              this.el.nativeElement.style.visibility = 'hidden';
            }
          }
        }
      )
  }

}
