import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ServerChannelsSidebarComponent } from './server-channels-sidebar.component';

describe('ServerChannelsSidebarComponent', () => {
  let component: ServerChannelsSidebarComponent;
  let fixture: ComponentFixture<ServerChannelsSidebarComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [ServerChannelsSidebarComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(ServerChannelsSidebarComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
