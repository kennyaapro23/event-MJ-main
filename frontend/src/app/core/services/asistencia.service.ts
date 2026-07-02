import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '@env/environment';
import { Asistencia } from '@core/models/models';

@Injectable({ providedIn: 'root' })
export class AsistenciaService {
  private http = inject(HttpClient);
  private base = environment.apiUrl;

  create(dto: Asistencia): Observable<Asistencia> {
    return this.http.post<Asistencia>(`${this.base}/asistencias`, dto);
  }

  confirmByQr(codigoQr: string): Observable<Asistencia> {
    return this.http.patch<Asistencia>(`${this.base}/asistencias/qr/${codigoQr}`, null);
  }

  byEvent(id: number): Observable<Asistencia[]> {
    return this.http.get<Asistencia[]>(`${this.base}/asistencias/evento/${id}`);
  }

  byParticipant(id: number): Observable<Asistencia[]> {
    return this.http.get<Asistencia[]>(`${this.base}/asistencias/participante/${id}`);
  }

  get(id: number): Observable<Asistencia> {
    return this.http.get<Asistencia>(`${this.base}/asistencias/${id}`);
  }
}
