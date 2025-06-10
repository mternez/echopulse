import { ComponentFixture, TestBed } from '@angular/core/testing';

import { UserMembershipsSidebarComponent } from './user-memberships-sidebar.component';

describe('UserMembershipsSidebarComponent', () => {
  let component: UserMembershipsSidebarComponent;
  let fixture: ComponentFixture<UserMembershipsSidebarComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [UserMembershipsSidebarComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(UserMembershipsSidebarComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
