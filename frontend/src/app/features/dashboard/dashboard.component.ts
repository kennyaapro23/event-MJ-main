import { Component, inject, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterLink } from '@angular/router';
import { forkJoin } from 'rxjs';

import { AuthService } from '@core/services/auth.service';
import { EventService } from '@core/services/event.service';
import { ParticipantService } from '@core/services/participant.service';
import { RegistroService } from '@core/services/registro.service';

interface DashboardCard {
  emoji: string;
  title: string;
  description: string;
  link: string;
}

interface DashboardStats {
  events: number;
  participants: number;
  registros: number;
}

@Component({
  selector: 'app-dashboard',
  standalone: true,
  imports: [CommonModule, RouterLink],
  templateUrl: './dashboard.component.html',
  styleUrl: './dashboard.component.css'
})
export class DashboardComponent {
  auth = inject(AuthService);
  private eventService = inject(EventService);
  private participantService = inject(ParticipantService);
  private registroService = inject(RegistroService);

  loading = signal(false);
  error = signal<string | null>(null);
  stats = signal<DashboardStats | null>(null);

  readonly cards: DashboardCard[] = [
    { emoji: '📅', title: 'Eventos', description: 'Crea y administra tus eventos.', link: '/eventos' },
    { emoji: '👥', title: 'Participantes', description: 'Registra a las personas.', link: '/participantes' },
    { emoji: '📝', title: 'Inscripciones', description: 'Inscribe participantes a eventos.', link: '/inscripciones' },
    { emoji: '💳', title: 'Pagos', description: 'Gestiona los pagos de inscripciones.', link: '/pagos' },
    { emoji: '✅', title: 'Registrar asistencia', description: 'Genera la asistencia y su QR.', link: '/asistencia' },
    { emoji: '📷', title: 'Check-in QR', description: 'Confirma la entrada por código QR.', link: '/checkin' }
  ];

  constructor() {
    this.loadStats();
  }

  loadStats(): void {
    this.loading.set(true);
    this.error.set(null);
    forkJoin({
      events: this.eventService.listAll(),
      participants: this.participantService.list(),
      registros: this.registroService.list()
    }).subscribe({
      next: (res) => {
        this.stats.set({
          events: res.events.length,
          participants: res.participants.length,
          registros: res.registros.length
        });
        this.loading.set(false);
      },
      error: (err) => {
        this.error.set(err?.error?.message ?? 'No se pudieron cargar las estadísticas.');
        this.loading.set(false);
      }
    });
  }
}
