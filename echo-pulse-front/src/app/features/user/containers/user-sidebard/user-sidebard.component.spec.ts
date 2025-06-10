import { ComponentFixture, TestBed } from '@angular/core/testing';

import { UserSidebardComponent } from './user-sidebard.component';

describe('UserSidebardComponent', () => {
  let component: UserSidebardComponent;
  let fixture: ComponentFixture<UserSidebardComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [UserSidebardComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(UserSidebardComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
