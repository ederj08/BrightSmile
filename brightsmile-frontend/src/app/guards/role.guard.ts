
import { inject } from '@angular/core';
import { CanActivateFn, Router, ActivatedRouteSnapshot } from '@angular/router';
import { AuthService } from '../services/auth';

//CanActivateFn con parámetro  route -> permite leer datos de la ruta y se usa cuando requieren un rol especifico ademas de la autenticación

export const roleGuard: CanActivateFn = (route: ActivatedRouteSnapshot)=>{
    const authService = inject(AuthService);
    const router = inject (Router);

    // Primero verifica autenticación básica
    
    if (!authService.hasValidToken()) {
        router.navigate(['/login']);
        return false;
    }

    //Lee el rol requerido definido en app.routes.ts
    const requiredRole = route.data['role'] as string;

    //si no se especifico rol requerido, solo basta con estar autenticado
    if(!requiredRole) return true;

    //Verifica si el usuario tiene el rol requerido
    const hasRole = authService.getRoles().includes(requiredRole);

    if (hasRole){
        //Rol correcto -> permite el acceso
        return true;
    }

    //Rol incorrecto -> redirige a home (no al login, ya que está autenticado)
    router.navigate(['/']);
    return false;
};
