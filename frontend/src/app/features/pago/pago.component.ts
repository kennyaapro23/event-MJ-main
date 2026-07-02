import { Component, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { RouterLink } from '@angular/router';
import { PagoService } from '@core/services/pago.service';
import { Pago, MetodoPago } from '@core/models/models';

@Component({
  selector: 'app-pago',
  standalone: true,
  imports: [CommonModule, FormsModule, RouterLink],
  templateUrl: './pago.component.html',
  styleUrl: './pago.component.css'
})
export class PagoComponent {
  private pagoService = inject(PagoService);

  // ----- Create form model -----
  form: Pago = {
    registroId: 0,
    participantId: 0,
    eventoId: 0,
    monto: 0,
    metodoPago: 'TARJETA',
    referencia: ''
  };
  metodos: MetodoPago[] = ['TARJETA', 'YAPE', 'PLIN', 'EFECTIVO'];

  creating = false;
  createError: string | null = null;
  createSuccess: string | null = null;

  // Results table (created payments prepended)
  pagos: Pago[] = [];

  // ----- Query section -----
  consultaId: number | null = null;
  loading = false;
  queryError: string | null = null;
  consulta: Pago[] = [];
  consultado = false;

  registrar(): void {
    this.createError = null;
    this.createSuccess = null;
    this.creating = true;

    const dto: Pago = {
      registroId: Number(this.form.registroId),
      participantId: Number(this.form.participantId),
      eventoId: Number(this.form.eventoId),
      monto: Number(this.form.monto),
      metodoPago: this.form.metodoPago,
      referencia: this.form.referencia
    };

    this.pagoService.create(dto).subscribe({
      next: (pago) => {
        this.creating = false;
        this.createSuccess = `Pago registrado correctamente. Estado: ${pago.estado ?? 'PAGADO'}.`;
        this.pagos = [pago, ...this.pagos];
        this.resetForm();
      },
      error: (err) => {
        this.creating = false;
        this.createError = err?.error?.message ?? 'No se pudo registrar el pago.';
      }
    });
  }

  consultar(): void {
    this.queryError = null;
    if (this.consultaId == null) {
      this.queryError = 'Ingrese un ID de participante.';
      return;
    }
    this.loading = true;
    this.consultado = false;

    this.pagoService.byParticipant(Number(this.consultaId)).subscribe({
      next: (list) => {
        this.loading = false;
        this.consulta = list ?? [];
        this.consultado = true;
      },
      error: (err) => {
        this.loading = false;
        this.consulta = [];
        this.consultado = true;
        this.queryError = err?.error?.message ?? 'No se pudieron obtener los pagos.';
      }
    });
  }

  badgeClass(estado?: string): string {
    if (estado === 'PAGADO') return 'badge badge-success';
    if (estado === 'PENDIENTE') return 'badge badge-warn';
    return 'badge badge-muted';
  }

  private resetForm(): void {
    this.form = {
      registroId: 0,
      participantId: 0,
      eventoId: 0,
      monto: 0,
      metodoPago: 'TARJETA',
      referencia: ''
    };
  }
}
