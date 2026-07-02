import { Component, inject, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Router, RouterLink } from '@angular/router';
import { AuthService } from '@core/services/auth.service';

@Component({
  selector: 'app-login',
  standalone: true,
  imports: [CommonModule, FormsModule, RouterLink],
  templateUrl: './login.component.html',
  styleUrl: './login.component.css'
})
export class LoginComponent {
  private auth = inject(AuthService);
  private router = inject(Router);

  username = '';
  password = '';
  submitting = signal(false);
  error = signal<string | null>(null);

  onSubmit(): void {
    if (!this.username || !this.password || this.submitting()) {
      return;
    }
    this.submitting.set(true);
    this.error.set(null);
    this.auth.login({ username: this.username, password: this.password }).subscribe({
      next: () => {
        this.submitting.set(false);
        this.router.navigate(['/dashboard']);
      },
      error: (err) => {
        this.submitting.set(false);
        const msg = err?.error?.message ?? 'Usuario o contrasena invalidos';
        this.error.set(msg);
        alert('Usuario o contrasena invalidos');
      }
    });
  }
}
