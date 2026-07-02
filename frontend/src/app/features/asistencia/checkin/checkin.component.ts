import { Component, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { RouterLink } from '@angular/router';
import { AsistenciaService } from '@core/services/asistencia.service';
import { Asistencia } from '@core/models/models';

@Component({
  selector: 'app-checkin',
  standalone: true,
  imports: [CommonModule, FormsModule, RouterLink],
  templateUrl: './checkin.component.html',
  styleUrl: './checkin.component.css'
})
export class CheckinComponent {
  private asistenciaService = inject(AsistenciaService);

  codigoQr = '';
  submitting = false;
  error: string | null = null;
  result: Asistencia | null = null;

  confirmar(): void {
    const codigo = this.codigoQr.trim();
    if (!codigo || this.submitting) {
      return;
    }

    this.submitting = true;
    this.error = null;
    this.result = null;

    this.asistenciaService.confirmByQr(codigo).subscribe({
      next: (asistencia) => {
        this.result = asistencia;
        this.submitting = false;
      },
      error: (err) => {
        if (err?.status === 404) {
          this.error = 'Codigo QR no valido';
        } else {
          this.error = err?.error?.message ?? 'No se pudo confirmar la asistencia';
        }
        this.submitting = false;
      }
    });
  }

  reset(): void {
    this.codigoQr = '';
    this.error = null;
    this.result = null;
  }
}
