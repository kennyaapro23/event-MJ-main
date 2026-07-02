import { Component, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { RouterLink } from '@angular/router';
import { AsistenciaService } from '@core/services/asistencia.service';
import { Asistencia } from '@core/models/models';

@Component({
  selector: 'app-asistencia-registro',
  standalone: true,
  imports: [CommonModule, FormsModule, RouterLink],
  templateUrl: './asistencia-registro.component.html',
  styleUrl: './asistencia-registro.component.css'
})
export class AsistenciaRegistroComponent {
  private asistenciaService = inject(AsistenciaService);

  // --- Generar asistencia ---
  form: { registroId: number | null; participantId: number | null; eventoId: number | null; observaciones: string } = {
    registroId: null,
    participantId: null,
    eventoId: null,
    observaciones: ''
  };
  creating = false;
  createError = '';
  created: Asistencia | null = null;

  // --- Asistencias del evento ---
  eventoIdBuscar: number | null = null;
  loading = false;
  listError = '';
  searched = false;
  asistencias: Asistencia[] = [];

  generar(): void {
    this.createError = '';
    this.created = null;

    if (this.form.registroId == null || this.form.participantId == null || this.form.eventoId == null) {
      this.createError = 'Completa registroId, participantId y eventoId.';
      return;
    }

    const dto: Asistencia = {
      registroId: this.form.registroId,
      participantId: this.form.participantId,
      eventoId: this.form.eventoId,
      observaciones: this.form.observaciones?.trim() || undefined
    };

    this.creating = true;
    this.asistenciaService.create(dto).subscribe({
      next: (res) => {
        this.created = res;
        this.creating = false;
      },
      error: (err) => {
        if (err?.status === 404) {
          this.createError = 'El participante no existe';
        } else {
          this.createError = err?.error?.message || 'No se pudo generar la asistencia.';
        }
        this.creating = false;
      }
    });
  }

  buscar(): void {
    this.listError = '';
    if (this.eventoIdBuscar == null) {
      this.listError = 'Ingresa un eventoId.';
      return;
    }

    this.loading = true;
    this.searched = true;
    this.asistenciaService.byEvent(this.eventoIdBuscar).subscribe({
      next: (res) => {
        this.asistencias = res ?? [];
        this.loading = false;
      },
      error: (err) => {
        this.listError = err?.error?.message || 'No se pudieron cargar las asistencias.';
        this.asistencias = [];
        this.loading = false;
      }
    });
  }

  badgeClass(estado: Asistencia['estado']): string {
    switch (estado) {
      case 'PRESENTE':
        return 'badge badge-success';
      case 'TARDANZA':
        return 'badge badge-warn';
      case 'AUSENTE':
      default:
        return 'badge badge-muted';
    }
  }
}
