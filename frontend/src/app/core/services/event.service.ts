import { Injectable, inject } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable, map } from 'rxjs';
import { environment } from '@env/environment';
import { EventCreate, EventResponse, EventStatus, Page } from '@core/models/models';

@Injectable({ providedIn: 'root' })
export class EventService {
  private http = inject(HttpClient);
  private base = environment.apiUrl;

  list(page = 0, size = 20): Observable<Page<EventResponse>> {
    return this.http.get<Page<EventResponse>>(`${this.base}/events?page=${page}&size=${size}`);
  }

  listAll(): Observable<EventResponse[]> {
    return this.list(0, 200).pipe(map((pg) => pg.content));
  }

  get(id: number): Observable<EventResponse> {
    return this.http.get<EventResponse>(`${this.base}/events/${id}`);
  }

  create(dto: EventCreate): Observable<EventResponse> {
    return this.http.post<EventResponse>(`${this.base}/events`, dto);
  }

  remove(id: number, organizerId: number): Observable<void> {
    const headers = new HttpHeaders({ 'X-Organizer-Id': String(organizerId) });
    return this.http.delete<void>(`${this.base}/events/${id}`, { headers });
  }

  changeStatus(id: number, status: EventStatus, organizerId: number): Observable<EventResponse> {
    const headers = new HttpHeaders({ 'X-Organizer-Id': String(organizerId) });
    return this.http.patch<EventResponse>(
      `${this.base}/events/${id}/status?status=${status}`,
      null,
      { headers }
    );
  }
}
