import { Routes } from '@angular/router';
import { authGuard } from '@core/guards/auth.guard';

export const routes: Routes = [
  { path: '', redirectTo: 'dashboard', pathMatch: 'full' },
  {
    path: 'login',
    loadComponent: () =>
      import('./features/auth/login/login.component').then((m) => m.LoginComponent)
  },
  {
    path: 'register',
    loadComponent: () =>
      import('./features/auth/register/register.component').then((m) => m.RegisterComponent)
  },
  {
    path: 'dashboard',
    canActivate: [authGuard],
    loadComponent: () =>
      import('./features/dashboard/dashboard.component').then((m) => m.DashboardComponent)
  },
  {
    path: 'eventos',
    canActivate: [authGuard],
    loadComponent: () =>
      import('./features/events/event-list/event-list.component').then((m) => m.EventListComponent)
  },
  {
    path: 'eventos/nuevo',
    canActivate: [authGuard],
    loadComponent: () =>
      import('./features/events/event-form/event-form.component').then((m) => m.EventFormComponent)
  },
  {
    path: 'participantes',
    canActivate: [authGuard],
    loadComponent: () =>
      import('./features/participants/participant-list/participant-list.component').then(
        (m) => m.ParticipantListComponent
      )
  },
  {
    path: 'inscripciones',
    canActivate: [authGuard],
    loadComponent: () =>
      import('./features/registro/registro.component').then((m) => m.RegistroComponent)
  },
  {
    path: 'pagos',
    canActivate: [authGuard],
    loadComponent: () =>
      import('./features/pago/pago.component').then((m) => m.PagoComponent)
  },
  {
    path: 'asistencia',
    canActivate: [authGuard],
    loadComponent: () =>
      import('./features/asistencia/asistencia-registro/asistencia-registro.component').then(
        (m) => m.AsistenciaRegistroComponent
      )
  },
  {
    path: 'checkin',
    canActivate: [authGuard],
    loadComponent: () =>
      import('./features/asistencia/checkin/checkin.component').then((m) => m.CheckinComponent)
  },
  { path: '**', redirectTo: 'dashboard' }
];
