import { Component } from '@angular/core';
import { Router } from '@angular/router';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrl: './app.component.css'
})
export class AppComponent {
  title = 'cosmic-news';
  constructor(private router: Router) {
    const isFront = window.location.pathname.includes('front');
    const backgroundUrl = isFront ? '/images/background.jpg' : '/api/images/background';
    document.documentElement.style.setProperty('--background-url', `url(${backgroundUrl})`);
  }
}
