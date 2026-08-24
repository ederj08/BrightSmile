import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators, ReactiveFormsModule } from '@angular/forms';
import { Router, RouterLink } from '@angular/router';
import { CommonModule } from '@angular/common';
import { AuthService, LoginRequest } from '../../services/auth'; // ajusta si el archivo se llama diferente
import gsap from 'gsap';

@Component({
  selector: 'app-login',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule, RouterLink],
  templateUrl: './login.html',
  styleUrls: ['./login.css']
})
export class LoginComponent implements OnInit {
  loginForm: FormGroup;
  loading = false;
  error = '';
  showPassword = false;

  constructor(
    private fb: FormBuilder,
    private authService: AuthService,
    private router: Router
  ) {
    this.loginForm = this.fb.group({
      username: ['', [Validators.required]],
      password: ['', [Validators.required, Validators.minLength(6)]]
    });
  }

  ngOnInit() {
    const tl = gsap.timeline();
    tl.from('.login-visual', { x: -60, opacity: 0, duration: 0.8, ease: 'power3.out' })
      .from('.login-card', { x: 60, opacity: 0, duration: 0.8, ease: 'power3.out' }, '-=0.6')
      .from('.form-group', { y: 20, opacity: 0, stagger: 0.12, duration: 0.5, ease: 'power2.out' }, '-=0.3')
      .from('.login-btn', { scale: 0.9, opacity: 0, duration: 0.4, ease: 'back.out(1.7)' }, '-=0.1');
  }

  togglePassword() {
    this.showPassword = !this.showPassword;
  }

  onSubmit() {
  if (this.loginForm.invalid) {
    this.loginForm.markAllAsTouched();
    return;
  }

  this.loading = true;
  this.error = '';

  const credentials: LoginRequest = this.loginForm.value;

  this.authService.login(credentials).subscribe({
    next: (_response: any) => {
      this.loading = false;
      const roles = this.authService.getRoles();
      if (roles.includes('ROLE_ADMIN')) this.router.navigate(['/admin']);
      else this.router.navigate(['/appointment']);
    },
    error: (_err: any) => {
      this.loading = false;
      this.error = 'Credenciales incorrectas. Intenta de nuevo.';
      const card = document.querySelector('.login-card');
      gsap.to(card, {
        keyframes: [
          { x: -10, duration: 0.08 },
          { x: 10, duration: 0.08 },
          { x: -8, duration: 0.08 },
          { x: 8, duration: 0.08 },
          { x: -4, duration: 0.08 },
          { x: 0, duration: 0.08 }
        ]
      });
    }
  });
}
}