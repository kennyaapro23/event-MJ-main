import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '@env/environment';
import { CreateParticipant, Participant } from '@core/models/models';

@Injectable({ providedIn: 'root' })
export class ParticipantService {
  private http = inject(HttpClient);
  private base = environment.apiUrl;

  list(): Observable<Participant[]> {
    return this.http.get<Participant[]>(`${this.base}/participants`);
  }

  get(id: number): Observable<Participant> {
    return this.http.get<Participant>(`${this.base}/participants/${id}`);
  }

  create(dto: CreateParticipant): Observable<Participant> {
    return this.http.post<Participant>(`${this.base}/participants`, dto);
  }

  update(id: number, dto: CreateParticipant): Observable<Participant> {
    return this.http.put<Participant>(`${this.base}/participants/${id}`, dto);
  }

  remove(id: number): Observable<void> {
    return this.http.delete<void>(`${this.base}/participants/${id}`);
  }
}
