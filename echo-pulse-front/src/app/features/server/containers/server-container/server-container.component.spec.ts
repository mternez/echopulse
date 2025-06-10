import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ServerContainerComponent } from './server-container.component';

describe('ServerContainerComponent', () => {
  let component: ServerContainerComponent;
  let fixture: ComponentFixture<ServerContainerComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [ServerContainerComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(ServerContainerComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
