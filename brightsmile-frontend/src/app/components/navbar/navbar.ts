import { Component, OnInit, HostListener} from '@angular/core';
import {RouterLink, RouterLinkActive} from '@angular/router';
import { CommonModule } from '@angular/common';
import {ChatService} from '../../services/chat'
import gsap from 'gsap';


@Component({
  selector: 'app-navbar',
  standalone: true,
  imports: [RouterLink, RouterLinkActive, CommonModule ],
  templateUrl: './navbar.html',
  styleUrls: ['./navbar.css']
})
export class NavbarComponent implements OnInit {

  scrolled = false;
  menuOpen = false;
  
 @HostListener('window:scroll')
 onScroll(){
  this.scrolled = window.scrollY > 40;
 } 

 constructor (private chatService: ChatService){}

 abrirChat(): void{
  this.chatService.abrirChat();
}

 ngOnInit(){
  // Entrada del navbar con GSAP

  gsap.from('.nav-logo', {y:30, opacity: 0, duration:0.5,delay: 0.7, ease:'back.out(1.7)'});
 }

 toggleMenu(){
  this.menuOpen = !this.menuOpen;
 }

 scrollToServices(event: Event) {
  event.preventDefault();
  const el = document.getElementById('services');
  if (el) el.scrollIntoView({ behavior: 'smooth' });
}

}
