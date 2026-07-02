import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '@env/environment';
import { Registro, RegistroCreate } from '@core/models/models';

@Injectable({ providedIn: 'root' })
export class RegistroService {
  private http = inject(HttpClient);
  private base = environment.apiUrl;

  list(): Observable<Registro[]> {
    return this.http.get<Registro[]>(`${this.base}/registros`);
  }

  get(id: number): Observable<Registro> {
    return this.http.get<Registro>(`${this.base}/registros/${id}`);
  }

  create(participantId: number, dto: RegistroCreate): Observable<Registro> {
    return this.http.post<Registro>(`${this.base}/registros/participant/${participantId}`, dto);
  }

  remove(id: number): Observable<void> {
    return this.http.delete<void>(`${this.base}/registros/${id}`);
  }
}
