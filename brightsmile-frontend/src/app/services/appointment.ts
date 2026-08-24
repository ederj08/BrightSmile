import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable } from 'rxjs';
import { AuthService } from './auth';

export interface Servicio {
  id: number;
  nombre: string;
  descripcion: string;
  duracionMinutos: number;
  activo: boolean;
}

export interface HorarioDisponible {
  id: number;
  diaSemana: number;
  horaInicio: string;
  horaFin: string;
  disponible: boolean;
}

export interface CitaRequest {
  pacienteNombre: string;
  pacienteEmail: string;
  pacienteTelefono: string;
  tipoDocumento: string;
  numeroDocumento: string;
  fecha: string;
  hora: string;
  servicioId: number;
  notas?: string;
}

export interface Cita {
  id: number;
  pacienteNombre: string;
  pacienteEmail: string;
  pacienteTelefono: string;
  tipoDocumento: string;
  numeroDocumento: string;
  servicio:{ nombre:string };
  fecha: string;
  hora: string;
  estado: string;
  notas: string;
}

@Injectable({
  providedIn: 'root',
})
export class Appointment {

  private apiUrl = 'http://localhost:8080/api/v1';

  constructor(private http: HttpClient, private authService: AuthService) {}

  private getHeaders(): HttpHeaders {
    return new HttpHeaders({
      'Authorization': `Bearer ${this.authService.getToken()}`
    });
  }

  // GET servicios activos - público
  getServicios(): Observable<Servicio[]> {
    return this.http.get<Servicio[]>(`${this.apiUrl}/servicios/activos`);
  }

  // GET horarios disponibles de un día - público
  getHorariosDisponibles(diaSemana: number): Observable<HorarioDisponible[]> {
    return this.http.get<HorarioDisponible[]>(
      `${this.apiUrl}/horarios/disponibles/${diaSemana}`
    );
  }

  // GET verificar disponibilidad - público
  verificarDisponibilidad(fecha: string, hora: string): Observable<boolean> {
    return this.http.get<boolean>(
      `${this.apiUrl}/citas/disponibilidad?fecha=${fecha}&hora=${hora}`
    );
  }

  // POST crear cita - público
  crearCita(cita: CitaRequest): Observable<Cita> {
    return this.http.post<Cita>(`${this.apiUrl}/citas`, cita);
  }

  // GET todas las citas - requiere token
  todasLasCitas(): Observable<Cita[]> {
    return this.http.get<Cita[]>(`${this.apiUrl}/citas`, {
      headers: this.getHeaders()
    });
  }

  // GET citas por fecha - requiere token
  citasPorFecha(fecha: string): Observable<Cita[]> {
    return this.http.get<Cita[]>(
      `${this.apiUrl}/citas/fecha?fecha=${fecha}`,
      { headers: this.getHeaders() }
    );
  }

  // GET citas por número de documento - requiere token
  citasPorDocumento(numeroDocumento: string): Observable<Cita[]> {
    return this.http.get<Cita[]>(
      `${this.apiUrl}/citas/paciente/${numeroDocumento}`,
      { headers: this.getHeaders() }
    );
  }

  // PUT cambiar estado - requiere token
  cambiarEstado(id: number, estado: string): Observable<Cita> {
    return this.http.put<Cita>(
      `${this.apiUrl}/citas/${id}/estado?estado=${estado}`,
      {},
      { headers: this.getHeaders() }
    );
  }

  // PUT cancelar cita por paciente - público, verifica con número de documento
  cancelarCitaPaciente(id: number, numeroDocumento: string): Observable<Cita> {
    return this.http.put<Cita>(
      `${this.apiUrl}/citas/${id}/cancelar?numeroDocumento=${numeroDocumento}`,
      {}
    );
  }

  // PUT completar citas pasadas - requiere token admin
  completarCitasPasadas(): Observable<number> {
    return this.http.put<number>(
      `${this.apiUrl}/citas/completar-pasadas`,
      {},
      { headers: this.getHeaders() }
    );
  }
  
  //DELETE eliminar cita - requiere token admin
  eliminarCita(id:number): Observable <void>{
    return this.http.delete<void>(
      `${this.apiUrl}/citas/${id}`,
      {headers: this.getHeaders()}
    );
  }

  //GET buscar citas propias por documento - publico, sin token
  buscarCitasPaciente(numeroDocumento: string):Observable <Cita[]>{
    return this.http.get<Cita[]>(
      `${this.apiUrl}/citas/buscar?numeroDocumento=${numeroDocumento}`
    );
  }

}