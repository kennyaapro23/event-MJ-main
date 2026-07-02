import { Component, OnInit, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { RouterLink } from '@angular/router';

import { ParticipantService } from '@core/services/participant.service';
import { EventService } from '@core/services/event.service';
import { RegistroService } from '@core/services/registro.service';
import { Participant, EventResponse, Registro } from '@core/models/models';

@Component({
  selector: 'app-registro',
  standalone: true,
  imports: [CommonModule, FormsModule, RouterLink],
  templateUrl: './registro.component.html',
  styleUrl: './registro.component.css'
})
export class RegistroComponent implements OnInit {
  private participantService = inject(ParticipantService);
  private eventService = inject(EventService);
  private registroService = inject(RegistroService);

  participants: Participant[] = [];
  events: EventResponse[] = [];
  registros: Registro[] = [];

  // form model
  participantId: number | null = null;
  eventId: number | null = null;
  observaciones = '';

  // ui state
  loading = false;
  loadingList = false;
  submitting = false;
  deletingId: number | null = null;
  error = '';
  success = '';

  ngOnInit(): void {
    this.loadRefs();
    this.loadRegistros();
  }

  loadRefs(): void {
    this.loading = true;
    this.error = '';

    this.participantService.list().subscribe({
      next: (data) => {
        this.participants = data ?? [];
      },
      error: (err) => {
        this.error = err?.error?.message ?? 'No se pudieron cargar los participantes.';
      }
    });

    this.eventService.listAll().subscribe({
      next: (data) => {
        this.events = data ?? [];
        this.loading = false;
      },
      error: (err) => {
        this.error = err?.error?.message ?? 'No se pudieron cargar los eventos.';
        this.loading = false;
      }
    });
  }

  loadRegistros(): void {
    this.loadingList = true;
    this.error = '';
    this.registroService.list().subscribe({
      next: (data) => {
        this.registros = data ?? [];
        this.loadingList = false;
      },
      error: (err) => {
        this.error = err?.error?.message ?? 'No se pudieron cargar las inscripciones.';
        this.loadingList = false;
      }
    });
  }

  inscribir(): void {
    this.error = '';
    this.success = '';

    if (this.participantId == null || this.eventId == null) {
      this.error = 'Selecciona un participante y un evento.';
      return;
    }

    this.submitting = true;
    this.registroService
      .create(this.participantId, {
        idEvento: String(this.eventId),
        observations: this.observaciones,
        timestamp: new Date().toISOString()
      })
      .subscribe({
        next: () => {
          this.submitting = false;
          this.success = 'Inscripcion registrada correctamente.';
          this.observaciones = '';
          this.loadRegistros();
        },
        error: (err) => {
          this.submitting = false;
          this.error = err?.error?.message ?? 'No se pudo registrar la inscripcion.';
        }
      });
  }

  eliminar(id: number): void {
    this.error = '';
    this.success = '';
    this.deletingId = id;
    this.registroService.remove(id).subscribe({
      next: () => {
        this.deletingId = null;
        this.success = 'Inscripcion eliminada.';
        this.loadRegistros();
      },
      error: (err) => {
        this.deletingId = null;
        this.error = err?.error?.message ?? 'No se pudo eliminar la inscripcion.';
      }
    });
  }
}
