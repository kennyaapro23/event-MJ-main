import { Component, inject, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Router, RouterLink } from '@angular/router';
import { AuthService } from '@core/services/auth.service';

@Component({
  selector: 'app-register',
  standalone: true,
  imports: [CommonModule, FormsModule, RouterLink],
  templateUrl: './register.component.html',
  styleUrl: './register.component.css'
})
export class RegisterComponent {
  private auth = inject(AuthService);
  private router = inject(Router);

  username = '';
  password = '';
  role = 'ROLE_USER';
  submitting = signal(false);
  error = signal<string | null>(null);

  onSubmit(): void {
    if (!this.username || !this.password || this.submitting()) {
      return;
    }
    this.submitting.set(true);
    this.error.set(null);
    this.auth
      .register({ username: this.username, password: this.password, role: this.role })
      .subscribe({
        next: () => {
          this.submitting.set(false);
          this.router.navigate(['/dashboard']);
        },
        error: (err) => {
          this.submitting.set(false);
          const msg = err?.error?.message ?? 'No se pudo registrar';
          this.error.set(msg);
        }
      });
  }
}
