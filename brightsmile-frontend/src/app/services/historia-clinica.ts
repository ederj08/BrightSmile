
import {Injectable} from '@angular/core';
import {HttpClient, HttpHeaders} from '@angular/common/http';
import { Observable } from 'rxjs';
import {AuthService} from './auth';

export interface HistoriaClinicaEntry{
    id:number;
    numeroDocumento: string;
    pacienteNombre: string;
    fecha:string;
    diagnostico:string;
    tratamientoRealizado: string;
    observaciones:string;
    creadoPor:string;
}

export interface HistoriaClinicaRequest{
    numeroDocumento:string;
    pacienteNombre:string;
    fecha:string;
    diagnostico:string;
    tratamientoRealizado?:string;
    observaciones?:string;
    citaId?:number;
}

@Injectable ({
    providedIn:'root',
})
export class HistoriaClinicaService{
    private apiUrl = 'http://localhost:8080/api/v1/historia-clinica';

    constructor (private http: HttpClient,private authService: AuthService){}

    private getHeaders ():HttpHeaders{
        return new HttpHeaders({
            'Authorization': `Bearer ${this.authService.getToken()}`
        });
    }

    crear (request:HistoriaClinicaRequest): Observable<HistoriaClinicaEntry>{
        return this.http.post<HistoriaClinicaEntry>(this.apiUrl, request,{headers:this.getHeaders()});
    }

    buscarPorDocumento(numeroDocumento: string):Observable<HistoriaClinicaEntry[]>{
        return this.http.get<HistoriaClinicaEntry[]>(
           `${this.apiUrl}/paciente/${numeroDocumento}`,
           {headers: this.getHeaders()}  
        );
    }

    descargarPdf(numeroDocumento:string):Observable<Blob>{
        return this.http.get(`${this.apiUrl}/paciente/${numeroDocumento}/pdf`,{headers:this.getHeaders(),responseType:'blob'});
    }

}