import { Component, inject, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { RouterLink } from '@angular/router';
import { ParticipantService } from '@core/services/participant.service';
import { CreateParticipant, Participant } from '@core/models/models';

@Component({
  selector: 'app-participant-list',
  standalone: true,
  imports: [CommonModule, FormsModule, RouterLink],
  templateUrl: './participant-list.component.html',
  styleUrl: './participant-list.component.css'
})
export class ParticipantListComponent implements OnInit {
  private participantService = inject(ParticipantService);

  participants: Participant[] = [];
  loading = false;
  error = '';

  form: CreateParticipant = { firstName: '', lastName: '', email: '', phone: '' };
  creating = false;
  createError = '';
  successMsg = '';

  ngOnInit(): void {
    this.load();
  }

  load(): void {
    this.loading = true;
    this.error = '';
    this.participantService.list().subscribe({
      next: (data) => {
        this.participants = data;
        this.loading = false;
      },
      error: (err) => {
        this.error = err?.error?.message || 'No se pudo cargar la lista de participantes';
        this.loading = false;
      }
    });
  }

  create(): void {
    this.createError = '';
    this.successMsg = '';

    if (!this.form.firstName?.trim() || !this.form.email?.trim()) {
      this.createError = 'Nombre y email son obligatorios';
      return;
    }

    const dto: CreateParticipant = {
      firstName: this.form.firstName.trim(),
      lastName: this.form.lastName?.trim() || undefined,
      email: this.form.email.trim(),
      phone: this.form.phone?.trim() || undefined
    };

    this.creating = true;
    this.participantService.create(dto).subscribe({
      next: () => {
        this.creating = false;
        this.successMsg = 'Participante agregado correctamente';
        this.form = { firstName: '', lastName: '', email: '', phone: '' };
        this.load();
      },
      error: (err) => {
        this.creating = false;
        this.createError = err?.error?.message || 'No se pudo crear (email duplicado?)';
      }
    });
  }

  remove(p: Participant): void {
    if (!confirm(`¿Eliminar al participante "${p.firstName} ${p.lastName}"?`)) {
      return;
    }
    this.error = '';
    this.participantService.remove(p.participantId).subscribe({
      next: () => this.load(),
      error: (err) => {
        this.error = err?.error?.message || 'No se pudo eliminar el participante';
      }
    });
  }
}
