export type Modality = 'PRESENTIAL' | 'VIRTUAL' | 'HYBRID';
export type EventType = 'FREE' | 'PAID';
export type EventStatus = 'ACTIVE' | 'INACTIVE';

export interface EventCreate {
  name: string;
  description?: string;
  startDate: string; // 'yyyy-MM-dd'
  endDate: string; // 'yyyy-MM-dd'
  modality: Modality;
  eventType: EventType;
  maxCapacity: number;
  organizerId: number;
  address?: string;
  price: number;
}

export interface EventResponse {
  eventId: number;
  name: string;
  description: string;
  startDate: string;
  endDate: string;
  modality: Modality;
  eventType: EventType;
  maxCapacity: number;
  organizerId: number;
  organizer?: string;
  address?: string;
  status: EventStatus;
  price: number;
}

export interface Page<T> {
  content: T[];
  totalElements: number;
  totalPages: number;
  number: number;
  size: number;
}

export interface CreateParticipant {
  firstName: string;
  lastName?: string;
  email: string;
  phone?: string;
}

export interface Participant {
  participantId: number;
  firstName: string;
  lastName: string;
  email: string;
  phone: string;
  registrationDate: string;
}

export interface RegistroCreate {
  timestamp?: string;
  observations?: string;
  idEvento?: string;
}

export interface Registro {
  idAttendance: number;
  timestamp: string;
  observations: string;
  idEvento: string;
  participantDTO: Participant;
}

export type MetodoPago = 'TARJETA' | 'YAPE' | 'PLIN' | 'EFECTIVO';
export type EstadoPago = 'PENDIENTE' | 'PAGADO' | 'FALLIDO' | 'REEMBOLSADO';

export interface Pago {
  pagoId?: number;
  registroId: number;
  participantId: number;
  eventoId: number;
  monto: number;
  metodoPago: MetodoPago;
  estado?: EstadoPago;
  fechaPago?: string;
  referencia?: string;
}

export type EstadoAsistencia = 'PRESENTE' | 'AUSENTE' | 'TARDANZA';

export interface Asistencia {
  asistenciaId?: number;
  registroId: number;
  participantId: number;
  eventoId: number;
  codigoQr?: string;
  estado?: EstadoAsistencia;
  fechaEntrada?: string;
  observaciones?: string;
}

export interface LoginRequest {
  username: string;
  password: string;
}

export interface RegisterRequest {
  username: string;
  password: string;
  role?: string;
}

export interface AuthResponse {
  token: string;
  type: string;
  username: string;
  role: string;
}
