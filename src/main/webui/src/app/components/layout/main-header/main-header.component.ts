import { PersistenceService } from './../../../services/persistence.service';
import { Component, OnDestroy, OnInit, signal } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { Router } from '@angular/router';
import { AvatarModule } from 'primeng/avatar';
import { InputTextModule } from 'primeng/inputtext';
import { PopoverModule } from 'primeng/popover';
import { AuthService } from '../../../services/auth.service';
import { UserDTO } from '../../../dtos/user.dto';
import { SpliceNamePipe } from '../../../pipes/splice-name.pipe';
import { DividerModule } from 'primeng/divider';
import { ButtonModule } from 'primeng/button';
import { BlockUIModule } from 'primeng/blockui';
import { catchError, Subscriber, Subscription, tap } from 'rxjs';

@Component({
  selector: 'app-main-header',
  imports: [AvatarModule, ButtonModule, BlockUIModule, SpliceNamePipe, DividerModule, InputTextModule, FormsModule, PopoverModule],
  host: {
    class: 'w-full bg-stone-50 gap-2 !pr-2 !pl-2 flex shadow-md items-center justify-between'
  },
  templateUrl: './main-header.component.html',
  styleUrl: './main-header.component.scss'
})
export class MainHeaderComponent implements OnInit, OnDestroy {

  constructor(public router: Router, public authService: AuthService, private persistenceService: PersistenceService) {
    this.subscriber = authService.getUser().subscribe(user => this.user.set(user));
  }

  tabs = signal<any[]>([]);
  search = signal<string>('');
  user = signal<UserDTO>({} as UserDTO);
  blockedPanel = signal(false);
  private subscriber: Subscription;

  ngOnInit(): void {
    this.loadTabs();
  }

  ngOnDestroy(): void {
    this.subscriber.unsubscribe();
  }

  private loadTabs() {
    this.persistenceService.getRequest('/v1/tabs').pipe(tap((response: any) => {
      this.tabs.set(response.data);
    }), catchError(error => {
      this.tabs.set([]);
      return error;
    })).subscribe();
  }

  onLogoClick() {
    this.router.navigate(['/']);
  }

  searchCourses(event: string) {
    this.router.navigate(['/search', event]);
  }

  redirectProfile() {
    this.router.navigate(['/profile']);
  }

  redirectTab(url: string) {
    this.router.navigateByUrl(url);
  }
}
