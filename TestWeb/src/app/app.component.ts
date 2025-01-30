import { Component } from '@angular/core';
import { RouterOutlet } from '@angular/router';
import { HttpClientModule } from '@angular/common/http'; // Import HttpClientModule
import { Router, NavigationEnd } from '@angular/router';

@Component({
  selector: 'app-root',
  standalone: true,
  imports: [RouterOutlet, HttpClientModule],
  templateUrl: './app.component.html',
  styleUrl: './app.component.scss'
})
export class AppComponent {
  title = 'Aquadis Test';

  constructor(private router: Router) {
    this.router.events.subscribe(event => {
      if (event instanceof NavigationEnd) {
        const isAuthPage = this.isAuthPage();
        document.body.classList.toggle('auth-page', isAuthPage);
        document.documentElement.classList.toggle('auth-page', isAuthPage);
      }
    });
  }

  isAuthPage(): boolean {
    return this.router.url === '/login' || this.router.url === '/sign-up';
  }
}
