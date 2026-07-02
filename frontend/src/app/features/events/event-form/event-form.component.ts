import { Component, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Router, RouterLink } from '@angular/router';
import { EventService } from '@core/services/event.service';
import { EventCreate } from '@core/models/models';

@Component({
  selector: 'app-event-form',
  standalone: true,
  imports: [CommonModule, FormsModule, RouterLink],
  templateUrl: './event-form.component.html',
  styleUrl: './event-form.component.css'
})
export class EventFormComponent {
  private eventService = inject(EventService);
  private router = inject(Router);

  saving = false;
  error: string | null = null;
  validationErrors: Record<string, string> | null = null;

  today = new Date().toISOString().slice(0, 10);

  model: EventCreate = {
    name: '',
    description: '',
    startDate: '',
    endDate: '',
    modality: 'PRESENTIAL',
    eventType: 'FREE',
    maxCapacity: 100,
    organizerId: 1,
    address: '',
    price: 0
  };

  submit(): void {
    if (this.saving) {
      return;
    }
    this.error = null;
    this.validationErrors = null;
    this.saving = true;

    this.eventService.create(this.model).subscribe({
      next: () => {
        this.saving = false;
        this.router.navigate(['/eventos']);
      },
      error: (err) => {
        this.saving = false;
        const ve = err?.error?.validationErrors;
        if (ve && typeof ve === 'object') {
          this.validationErrors = ve as Record<string, string>;
          this.error = err?.error?.message ?? 'Invalid input data';
        } else {
          this.error = err?.error?.message ?? 'No se pudo crear el evento.';
        }
      }
    });
  }
}
