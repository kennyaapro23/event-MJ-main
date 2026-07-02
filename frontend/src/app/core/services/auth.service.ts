import { Injectable, signal, WritableSignal, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable, tap } from 'rxjs';
import { environment } from '@env/environment';
import { AuthResponse, LoginRequest, RegisterRequest } from '@core/models/models';

@Injectable({ providedIn: 'root' })
export class AuthService {
  private http = inject(HttpClient);
  private base = environment.apiUrl;

  authed: WritableSignal<boolean> = signal(!!localStorage.getItem('token'));

  login(req: LoginRequest): Observable<AuthResponse> {
    return this.http.post<AuthResponse>(`${this.base}/auth/login`, req).pipe(
      tap((res) => this.store(res))
    );
  }

  register(req: RegisterRequest): Observable<AuthResponse> {
    return this.http.post<AuthResponse>(`${this.base}/auth/register`, req).pipe(
      tap((res) => this.store(res))
    );
  }

  private store(res: AuthResponse): void {
    if (res?.token) {
      localStorage.setItem('token', res.token);
      localStorage.setItem('username', res.username ?? '');
      localStorage.setItem('role', res.role ?? '');
      this.authed.set(true);
    }
  }

  logout(): void {
    localStorage.removeItem('token');
    localStorage.removeItem('username');
    localStorage.removeItem('role');
    this.authed.set(false);
  }

  isLoggedIn(): boolean {
    return !!localStorage.getItem('token');
  }

  username(): string | null {
    return localStorage.getItem('username');
  }
}
