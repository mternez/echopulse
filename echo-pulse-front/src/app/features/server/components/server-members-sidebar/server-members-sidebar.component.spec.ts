import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ServerMembersSidebarComponent } from './server-members-sidebar.component';

describe('ServerMembersSidebarComponent', () => {
  let component: ServerMembersSidebarComponent;
  let fixture: ComponentFixture<ServerMembersSidebarComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [ServerMembersSidebarComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(ServerMembersSidebarComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
