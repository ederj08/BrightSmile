 import {inject} from '@angular/core';
 import {CanActivateFn, Router} from '@angular/router';
 import {AuthService} from '../services/auth';

 export const authGuard: CanActivateFn = () =>{
    //inject()->forma de inyectar dependencias en guards funcionales
    const authService = inject (AuthService);
    const router = inject (Router);

    //Verifica si hay token válido y no expirado en localStorage
    if (authService.hasValidToken()){
        //token válido-> permite el acceso a la ruta
        return true;
    }
    //Sin token-> redirige al login y bloquea el acceso
    router.navigate(['/login']);
    return false;
 }