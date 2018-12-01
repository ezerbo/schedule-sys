import { UserProfile } from '../profile/userprofile';
import { Injectable } from '@angular/core';
import { Subject } from 'rxjs/Subject';

@Injectable()
export class EventBusService {

  private userProfileUpdateSource = new Subject<UserProfile>();

  userProfileUpdated$ = this.userProfileUpdateSource.asObservable();

  constructor() { }

  broadcastUserUpdate(user: UserProfile) {
    this.userProfileUpdateSource.next(user);
  }

}
