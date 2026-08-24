import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable, BehaviorSubject } from 'rxjs';
import { tap } from 'rxjs/operators';
import { Router } from '@angular/router';

// Interface que define la estructura del request de login
export interface LoginRequest {
  username: string;
  password: string;
}

// Interface que define la respuesta del backend al hacer login
export interface LoginResponse {
  token: string;
}

// @Injectable permite que este servicio se inyecte en componentes
// providedIn: 'root' → singleton — existe una sola instancia en toda la app
@Injectable({
  providedIn: 'root'
})
export class AuthService {

  // URL base del backend
  private apiUrl = 'http://localhost:8080';

  // BehaviorSubject → almacena el estado actual de autenticación
  // Cualquier componente puede suscribirse y saber si el usuario está logueado
  // false = no autenticado, true = autenticado
  private isAuthenticatedSubject = new BehaviorSubject<boolean>(this.hasValidToken());

  // Observable público — los componentes se suscriben a este
  isAuthenticated$ = this.isAuthenticatedSubject.asObservable();

  constructor(private http: HttpClient, private router: Router) {}

  // Hace el POST /auth/login y guarda el token en localStorage
  login(credentials: LoginRequest): Observable<LoginResponse> {
    return this.http.post<LoginResponse>(`${this.apiUrl}/auth/login`, credentials)
      .pipe(
        // tap → ejecuta una acción sin modificar el valor del observable
        tap(response => {
          // Guarda el token en localStorage
          localStorage.setItem('token', response.token);
          // Notifica a todos los componentes que el usuario está autenticado
          this.isAuthenticatedSubject.next(true);
        })
      );
  }

  // Elimina el token y redirige al login
  logout(): void {
    localStorage.removeItem('token');
    this.isAuthenticatedSubject.next(false);
    this.router.navigate(['/login']);
  }

  // Obtiene el token del localStorage
  getToken(): string | null {
    return localStorage.getItem('token');
  }

  // Verifica si hay token válido y no expirado
  hasValidToken(): boolean {
    const token = localStorage.getItem('token');
    if (!token) return false;

    try {
      // Decodifica el payload del JWT (parte del medio)
      const payload = JSON.parse(atob(token.split('.')[1]));
      // Verifica que no haya expirado
      const ahora = Math.floor(Date.now() / 1000);
      return payload.exp && payload.exp > ahora;
    } catch {
      return false;
    }
  }

  // Extrae los roles del token JWT
  getRoles(): string[] {
    const token = this.getToken();
    if (!token) return [];

    try {
      const payload = JSON.parse(atob(token.split('.')[1]));
      return payload.roles || [];
    } catch {
      return [];
    }
  }

  // Verifica si el usuario tiene el rol de admin
  isAdmin(): boolean {
    return this.getRoles().includes('ROLE_ADMIN');
  }

  // Extrae el username del token
  getUsername(): string {
    const token = this.getToken();
    if (!token) return '';

    try {
      const payload = JSON.parse(atob(token.split('.')[1]));
      return payload.sub || '';
    } catch {
      return '';
    }
  }
}