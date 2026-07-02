import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '@env/environment';
import { EstadoPago, Pago } from '@core/models/models';

@Injectable({ providedIn: 'root' })
export class PagoService {
  private http = inject(HttpClient);
  private base = environment.apiUrl;

  create(dto: Pago): Observable<Pago> {
    return this.http.post<Pago>(`${this.base}/pagos`, dto);
  }

  byParticipant(id: number): Observable<Pago[]> {
    return this.http.get<Pago[]>(`${this.base}/pagos/participante/${id}`);
  }

  byEvent(id: number): Observable<Pago[]> {
    return this.http.get<Pago[]>(`${this.base}/pagos/evento/${id}`);
  }

  get(id: number): Observable<Pago> {
    return this.http.get<Pago>(`${this.base}/pagos/${id}`);
  }

  updateEstado(id: number, estado: EstadoPago): Observable<Pago> {
    return this.http.patch<Pago>(`${this.base}/pagos/${id}/estado?estado=${estado}`, null);
  }
}
