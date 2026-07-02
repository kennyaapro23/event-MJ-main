import { Component, OnInit, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Router, RouterLink } from '@angular/router';
import { EventService } from '@core/services/event.service';
import { EventResponse, EventStatus } from '@core/models/models';

@Component({
  selector: 'app-event-list',
  standalone: true,
  imports: [CommonModule, RouterLink],
  templateUrl: './event-list.component.html',
  styleUrl: './event-list.component.css'
})
export class EventListComponent implements OnInit {
  private eventService = inject(EventService);
  private router = inject(Router);

  events: EventResponse[] = [];
  loading = false;
  error = '';

  ngOnInit(): void {
    this.load();
  }

  load(): void {
    this.loading = true;
    this.error = '';
    this.eventService.listAll().subscribe({
      next: (data) => {
        this.events = data;
        this.loading = false;
      },
      error: (err) => {
        this.error = err?.error?.message ?? 'No se pudieron cargar los eventos.';
        this.loading = false;
      }
    });
  }

  toggleStatus(e: EventResponse): void {
    const next: EventStatus = e.status === 'ACTIVE' ? 'INACTIVE' : 'ACTIVE';
    this.error = '';
    this.eventService.changeStatus(e.eventId, next, e.organizerId).subscribe({
      next: () => this.load(),
      error: (err) => {
        this.error = err?.error?.message ?? 'No se pudo cambiar el estado del evento.';
      }
    });
  }

  remove(e: EventResponse): void {
    if (!confirm(`¿Eliminar el evento "${e.name}"?`)) {
      return;
    }
    this.error = '';
    this.eventService.remove(e.eventId, e.organizerId).subscribe({
      next: () => this.load(),
      error: (err) => {
        this.error = err?.error?.message ?? 'No se pudo eliminar el evento.';
      }
    });
  }
}
