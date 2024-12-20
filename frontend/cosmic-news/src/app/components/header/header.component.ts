import { Component } from '@angular/core';
import { Router } from '@angular/router';

@Component({
  selector: 'app-header',
  templateUrl: './header.component.html',
  styleUrls: ['./header.component.css']
})
export class HeaderComponent {

  logoUrl: string;

  constructor(private router: Router) {
      const isFront = window.location.pathname.includes('front');
      this.logoUrl = isFront ? '/images/logo.png' :'/api/images/logo';
    }

  }