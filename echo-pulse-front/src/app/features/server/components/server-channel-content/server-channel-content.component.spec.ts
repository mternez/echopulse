import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ServerChannelContentComponent } from './server-channel-content.component';

describe('ServerChannelContentComponent', () => {
  let component: ServerChannelContentComponent;
  let fixture: ComponentFixture<ServerChannelContentComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [ServerChannelContentComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(ServerChannelContentComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
