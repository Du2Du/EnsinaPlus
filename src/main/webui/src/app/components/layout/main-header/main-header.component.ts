import { AfterViewInit, Component, input, model, OnChanges, OnDestroy, OnInit, output, Renderer2, signal, ViewChild } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { Router } from '@angular/router';
import { AvatarModule } from 'primeng/avatar';
import { BlockUIModule } from 'primeng/blockui';
import { ButtonModule } from 'primeng/button';
import { DividerModule } from 'primeng/divider';
import { InputTextModule } from 'primeng/inputtext';
import { PopoverModule } from 'primeng/popover';
import { catchError, debounceTime, fromEvent, of, Subscription, tap } from 'rxjs';
import { UserDTO } from '../../../dtos/user.dto';
import { SpliceNamePipe } from '../../../pipes/splice-name.pipe';
import { AuthService } from '../../../services/auth.service';
import { PersistenceService } from './../../../services/persistence.service';

@Component({
  selector: 'app-main-header',
  providers: [PersistenceService, AuthService, Router],
  imports: [AvatarModule, ButtonModule, BlockUIModule, SpliceNamePipe, DividerModule, InputTextModule, FormsModule, PopoverModule],
  host: {
    class: 'w-full bg-stone-50 gap-2 !pr-2 !pl-2 flex shadow-md items-center justify-between'
  },
  templateUrl: './main-header.component.html',
  styleUrl: './main-header.component.scss'
})
export class MainHeaderComponent implements OnInit, AfterViewInit, OnDestroy, OnChanges {

  constructor(private r2: Renderer2, public router: Router, public authService: AuthService, private persistenceService: PersistenceService) {
    this.subscriber = authService.getUser().subscribe(user => this.user.set(user));
  }

  searchCoursesEvent = output<string>();
  tabs = signal<any[]>([]);
  search = input<string>('');
  searchInput = signal<string>('');
  user = signal<UserDTO>({} as UserDTO);
  blockedPanel = signal(false);
  private subscriber: Subscription;
  private inputSubscriber!: Subscription;

  ngOnInit(): void {
    this.loadTabs();
  }

  ngOnChanges(): void {
    if (this.search() !== this.searchInput()) this.searchInput.set(this.search());
  }

  ngAfterViewInit(): void {
    if (!this.inputSubscriber)
      this.inputSubscriber = fromEvent(this.r2.selectRootElement('#inputSearch', true), 'input').pipe(debounceTime(500)).subscribe((event: any) => {
        this.searchCoursesEvent.emit(this.searchInput())
        this.searchCourses(event.target.value);
      });
  }

  ngOnDestroy(): void {
    this.subscriber.unsubscribe();
    this.inputSubscriber.unsubscribe();
  }

  private loadTabs() {
    this.persistenceService.getRequest('/v1/tab/list').pipe(tap((response: any) => {
      this.tabs.set(response.data);
    }), catchError(error => {
      this.tabs.set([]);
      return of(error);
    })).subscribe();
  }

  onLogoClick() {
    this.router.navigate(['/home']);
  }

  searchCourses(event: string) {
    if (!this.router.url.includes('/search'))
      this.router.navigate(['/search', event]);
  }

  redirectProfile() {
    this.router.navigate(['/profile']);
  }

  redirectTab(url: string) {
    this.router.navigateByUrl(url);
  }
}
