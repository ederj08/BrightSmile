import { Routes } from '@angular/router';
import { HomeComponent } from './pages/home/home';
import { LoginComponent } from './pages/login/login';
import {authGuard} from './guards/auth.guard';
import {roleGuard} from './guards/role.guard';


export const routes: Routes = [
  //rutas públicas no requieren autenticación
  { path: '', component: HomeComponent },
  { path: 'login', component: LoginComponent },

  //Rutas protegidas - requieren token válido + rol especifico

  {
    path: 'admin',
    loadComponent: () => import ('./pages/admin/admin').then(m => m.AdminComponent),
    canActivate: [authGuard,roleGuard],
    data:{role: 'ROLE_ADMIN'}
  },
  
{
  path:'appointment',
  loadComponent: () => import ('./components/appointment/appointment').then(m =>m.AppointmentComponent),
  
},
   
{ path:'chat',
  loadComponent: () =>import('./components/chat/chat').then(m => m.Chat),
  canActivate: [authGuard]
},

//Catch-all

  { path: '**', redirectTo: '' }  // catch-all recomendado
];