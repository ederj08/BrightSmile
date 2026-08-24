import { Component, OnInit, ChangeDetectorRef } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ReactiveFormsModule, FormBuilder, FormGroup, Validators } from '@angular/forms';
import { RouterLink } from '@angular/router';
import { Appointment, Servicio, HorarioDisponible, CitaRequest,Cita } from '../../services/appointment';
import gsap from 'gsap';


@Component({
  selector: 'app-appointment',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule, RouterLink],
  templateUrl: './appointment.html',
  styleUrl: './appointment.css'
})
export class AppointmentComponent implements OnInit {

  form: FormGroup;
  servicios: Servicio[] = [];
  horarios: HorarioDisponible[] = [];
  loading = false;
  loadingHorarios = false;
  exito = false;
  error = '';
  modo:'book'|'cancel'='book';
  buscarForm:FormGroup;
  citasEncontradas: Cita[] = [];
  buscando = false;
  errorBusqueda='';
  cancelandoId:number | null = null;

  constructor(
    private fb: FormBuilder,
    private appointmentService: Appointment,
    private cdr: ChangeDetectorRef
  ) {
    this.form = this.fb.group({
      pacienteNombre: ['', [Validators.required, Validators.minLength(3)]],
      pacienteEmail: ['', [Validators.required, Validators.email]],
      pacienteTelefono: ['', [Validators.required, Validators.pattern(/^\d{7,15}$/)]],
      tipoDocumento:['',Validators.required],
      numeroDocumento:['',Validators.required],
      servicioId: ['', Validators.required],
      fecha: ['', Validators.required],
      hora: ['', Validators.required],
      notas: ['']
    });
    this.buscarForm=this.fb.group({
      numeroDocumento:['',Validators.required]
    });
  }

  ngOnInit() {
    this.cargarServicios();
    gsap.from('.appointment-card', { y: 40, opacity: 0, duration: 0.7, ease: 'power3.out' });
    gsap.from('.form-section', { y: 20, opacity: 0, stagger: 0.1, duration: 0.5, delay: 0.3, ease: 'power2.out' });
  }

  cargarServicios() {
    this.appointmentService.getServicios().subscribe({
      next: (data) => this.servicios = data,
      error: () => this.error = 'Error al cargar los servicios.'
    });
  }

  onFechaChange() {
    const fecha = this.form.get('fecha')?.value;
    if (!fecha) return;

    this.form.patchValue({ hora: '' });
    this.error = '';
    this.horarios = [];

    // getDay(): 0=Dom, 1=Lun, 2=Mar, 3=Mie, 4=Jue, 5=Vie, 6=Sab
    // Usamos fecha+'T12:00:00' para evitar problemas de zona horaria
    const date = new Date(fecha + 'T12:00:00');
    const diaSemanaJS = date.getDay();

    // Domingo cerrado
    if (diaSemanaJS === 0) {
      this.error = 'The clinic is closed on Sundays. Please select another date.';
      return;
    }

    // JavaScript: 1=Lun, 2=Mar, 3=Mie, 4=Jue, 5=Vie, 6=Sab
    // Backend data.sql: 1=Lun, 2=Mar, 3=Mie, 4=Jue, 5=Vie, 6=Sab
    // ¡Coinciden exactamente! No necesita conversión
    const diaSemanaBackend = diaSemanaJS;

    console.log('Fecha:', fecha, '| Día JS:', diaSemanaJS, '| Enviando al backend:', diaSemanaBackend);

    this.loadingHorarios = true;
    this.appointmentService.getHorariosDisponibles(diaSemanaBackend).subscribe({
      next: (data) => {
        console.log('Horarios recibidos:', data);
        this.horarios = data.filter(h => h.disponible);
        this.loadingHorarios = false;

        this.cdr.detectChanges();
      },
      error: () => {
        this.error = 'Error al cargar horarios.';
        this.loadingHorarios = false;

        this.cdr.detectChanges();
      }
    });
  }

  seleccionarHora(hora: string) {
    this.form.patchValue({ hora });
  }

  get horaSeleccionada() {
    return this.form.get('hora')?.value;
  }

  getFechaMinima(): string {
    const hoy = new Date();
    hoy.setDate(hoy.getDate() + 1);
    return hoy.toISOString().split('T')[0];
  }

  onSubmit() {
    if (this.form.invalid) {
      this.form.markAllAsTouched();
      return;
    }

    this.loading = true;
    this.error = '';

    const values = this.form.value;
    const request: CitaRequest = {
      pacienteNombre: values.pacienteNombre,
      pacienteEmail: values.pacienteEmail,
      pacienteTelefono: values.pacienteTelefono,
      tipoDocumento: values.tipoDocumento,
      numeroDocumento: values.numeroDocumento,
      servicioId: Number(values.servicioId),
      fecha: values.fecha,
      hora: values.hora + ':00',
      notas: values.notas || ''
    };

    this.appointmentService.crearCita(request).subscribe({
      next: () => {
        this.loading = false;
        this.exito = true;
        this.form.reset();
        this.horarios = [];
        gsap.from('.success-card', { scale: 0.8, opacity: 0, duration: 0.5, ease: 'back.out(1.7)' });
      },
      error: (err) => {
        this.loading = false;
        this.error = err.error?.message || 'Error al agendar la cita. Intenta de nuevo.';
      }
    });
  }

  nuevaCita() {
    this.exito = false;
    this.error = '';
  }

  fieldError(field: string): boolean {
    const control = this.form.get(field);
    return !!(control?.invalid && control?.touched);
  }

  

cambiarModo(nuevoModo: 'book' | 'cancel') {
  this.modo = nuevoModo;
  this.citasEncontradas = [];
  this.errorBusqueda = '';
  this.buscarForm.reset();
}

buscarCitas() {
  if (this.buscarForm.invalid) {
    this.buscarForm.markAllAsTouched();
    return;
  }

  this.buscando = true;
  this.errorBusqueda = '';
  this.citasEncontradas = [];

  const numeroDocumento = this.buscarForm.get('numeroDocumento')?.value;

  this.appointmentService.buscarCitasPaciente(numeroDocumento).subscribe({
    next: (citas) => {
      this.buscando = false;
      // Solo mostramos citas que aún se pueden cancelar
      this.citasEncontradas = citas.filter(c => c.estado === 'PENDIENTE');
      if (this.citasEncontradas.length === 0) {
        this.errorBusqueda = 'No pending appointments found for this document number.';
      }
      this.cdr.detectChanges(); //<-- fuerza el re-render
    },
    error: () => {
      this.buscando = false;
      this.errorBusqueda = 'Error searching for appointments. Please try again.';
      this.cdr.detectChanges();//<-- fuerza el re-render
    }
  });
}

cancelarCita(cita: Cita) {
  const numeroDocumento = this.buscarForm.get('numeroDocumento')?.value;
  this.cancelandoId = cita.id;
  this.cdr.detectChanges();//<-- para que se vea el "Cacenling.." de inmediato.

  this.appointmentService.cancelarCitaPaciente(cita.id, numeroDocumento).subscribe({
    next: () => {
      this.cancelandoId = null;
      this.citasEncontradas = this.citasEncontradas.filter(c => c.id !== cita.id);
      this.cdr.detectChanges();//<--- fuerza el re-render.
    },
    error: () => {
      this.cancelandoId = null;
      this.errorBusqueda = 'Could not cancel the appointment. Please try again.';
      this.cdr.detectChanges();//<-- fuerza el re-render.
    }
  });
}
}