import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterLink } from '@angular/router';
import {NavbarComponent} from '../../components/navbar/navbar';
import gsap from 'gsap';

@Component({
  selector: 'app-home',
  standalone: true,
  imports: [CommonModule,RouterLink,NavbarComponent],
  templateUrl: './home.html',
  styleUrls: ['./home.css']
})
export class HomeComponent implements OnInit {

  services =[
    {icon: '🦷', name: 'Dental Cleaning', price: '$120',duration:'60 min'},
    {icon: '✨', name: 'Teeth Whitening', price: '$299',duration:'90 min'},
    {icon: '📋', name: 'Dental x-Ray', price: '$85',duration:'30 min'},
    {icon: '🔧', name: 'Tooth Extraction', price: '$150',duration:'45 min'},
    {icon: '💉', name: 'Dental filling', price: '$175',duration:'60 min'},
    {icon: '🏥', name: 'Root canal', price: '$850',duration:'120 min'},
    {icon: '😁', name: 'Orthodontic consultation', price: '$200',duration:'60 min'},
    {icon: '👑', name: 'Dental Crown', price: '$950',duration:'90 min'},
    
  ];

  ngOnInit(): void {
    //Hero animations
    const tl = gsap.timeline();
    tl.from('.hero-badge', { y: -20, opacity: 0, duration: 0.6, ease: 'power3.out' })
      .from('.hero-title', { y: 40, opacity: 0, duration: 0.8, ease: 'power3.out' }, '-=0.3')
      .from('.hero-subtitle', { y: 30, opacity: 0, duration: 0.6, ease: 'power2.out' }, '-=0.4')
      .from('.hero-actions', { y: 20, opacity: 0, duration: 0.5, ease: 'power2.out' }, '-=0.3')
      .from('.hero-stats', { y: 20, opacity: 0, duration: 0.5, ease: 'power2.out' }, '-=0.2')
      .from('.hero-visual', { x: 60, opacity: 0, duration: 0.9, ease: 'power3.out' }, '-=0.8');

    // Service cards stagger
    gsap.from ('.service-card',{
      y: 40,
      opacity:0,
      duration:0.5,
      stagger:0.08,
      ease:'power2.out',
      delay: 0.8
    });
  }

}