import { ChangeDetectorRef, Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterLink } from '@angular/router';
import { FormsModule, Validators } from '@angular/forms';
import { Appointment, Cita } from '../../services/appointment';
import { AuthService } from '../../services/auth';
import { Router } from '@angular/router';
import { HistoriaClinicaService,HistoriaClinicaEntry,HistoriaClinicaRequest } from '../../services/historia-clinica';
import { FormBuilder, FormGroup,Validator,ReactiveFormsModule } from '@angular/forms';


@Component({
  selector: 'app-admin',
  standalone: true,
  imports: [CommonModule, RouterLink, FormsModule,ReactiveFormsModule],
  templateUrl: './admin.html',
  styleUrl: './admin.css'
})
export class AdminComponent implements OnInit {

  citas: Cita[] = [];
  citasFiltradas: Cita[] = [];
  loading = false; // ← cambiado a false
  error = '';
  username = '';
  fechaSeleccionada = '';
  filtroEstado = 'TODOS';
  modalHistorialAbierto=false;
  historialNumeroDocumento:string ='';
  historialPacienteNombre: string='';
  historialCitaId:number | null = null;
  busquedaDocumento:string='';
  entradasHistorial:HistoriaClinicaEntry[]=[];
  cargandoHistorial=false;
  guardandoEntrada=false;
  descargandoPdf=false;
  errorHistorial='';
  entradaForm: FormGroup;
  adminMenuOpen = false;

  // Selección múltiple
  citasSeleccionadas = new Set<number>();
  seleccionarTodos = false;

  get totalCitas() { return this.citas.length; }
  get pendientes() { return this.citas.filter(c => c.estado === 'PENDIENTE').length; }
  get completadas() { return this.citas.filter(c => c.estado === 'COMPLETADA').length; }
  get canceladas() { return this.citas.filter(c => c.estado === 'CANCELADA').length; }

  constructor(
    private appointmentService: Appointment,
    private authService: AuthService,
    private router: Router,
    private cdr: ChangeDetectorRef,
    private historiaClinicaService: HistoriaClinicaService,
    private fb: FormBuilder
  ) {
    this.entradaForm= this.fb.group({
      diagnostico:['',Validators.required],
      tratamientoRealizado:[''],
      observaciones:['']
    });
  }

  toggleAdminMenu(): void{
    this.adminMenuOpen = !this.adminMenuOpen;
  }

  closeAdminMenu(): void {
    this.adminMenuOpen= false;
  }

  
  ngOnInit() {
    this.username = this.authService.getUsername();
    this.cargarTodasLasCitas();
  }

  cargarTodasLasCitas() {
    this.loading = true;
    this.error = '';
    this.citasSeleccionadas.clear();
    this.seleccionarTodos = false;
    this.appointmentService.todasLasCitas().subscribe({
      next: (data) => {
        this.citas = data;
        this.citasFiltradas = [...data];
        this.loading = false;
        this.cdr.detectChanges();
      },
      error: () => {
        this.error = 'Error al cargar las citas. Verifica la conexión con el backend.';
        this.loading = false;
        this.cdr.detectChanges();
      }
    });
  }

  abrirHistorial(cita:Cita){
    this.historialNumeroDocumento=cita.numeroDocumento;
    this.historialPacienteNombre=cita.pacienteNombre;
    this.historialCitaId=cita.id;
    this.modalHistorialAbierto=true;
    this.entradasHistorial=[];
    this.errorHistorial='';
    this.entradaForm.reset();
    this.cargarHistorial(cita.numeroDocumento);
  }

  cerrarHistorial(){
    this.modalHistorialAbierto=false;
    this.historialNumeroDocumento ='';
    this.historialPacienteNombre = '';
    this.historialCitaId = null;
    this.entradasHistorial= [];
  }

  buscarHistorialPorDocumento(){
    const numeroDocumento = this.busquedaDocumento.trim();
    if (!numeroDocumento) return;

    this.historialNumeroDocumento = numeroDocumento;
    this.historialPacienteNombre= '';// se completa al cargar si hay entradas
    this.historialCitaId = null;//no viene de ninguna cita puntual
    this.modalHistorialAbierto= true;
    this.entradasHistorial = [];
    this.errorHistorial= '';
    this.entradaForm.reset();
    this.cargarHistorial(numeroDocumento);

  }

  cargarHistorial(numeroDocumento:string){
    this.cargandoHistorial=true;
    this.historiaClinicaService.buscarPorDocumento(numeroDocumento).subscribe({
    next:(entradas)=>{
      this.entradasHistorial=entradas;
      if(!this.historialPacienteNombre && entradas.length > 0 ){
        this.historialPacienteNombre = entradas[0].pacienteNombre;
      }
      this.cargandoHistorial = false;
      this.cdr.detectChanges();
    },
    error:()=>{
      this.errorHistorial='Error loading clinical history.';
      this.cargandoHistorial=false;
      this.cdr.detectChanges();
    } 
    });
  }
  agregarEntrada(){
    if(this.entradaForm.invalid || !this.historialNumeroDocumento){
      this.entradaForm.markAllAsTouched();
      return
    }
    if(!this.historialPacienteNombre){
      this.errorHistorial= 'Patient name is required to create a record.';
    }

    this.guardandoEntrada = true;


    const request: HistoriaClinicaRequest={
      numeroDocumento:this.historialNumeroDocumento,
      pacienteNombre:this.historialPacienteNombre,
      fecha:new Date().toISOString().split('T')[0],
      diagnostico:this.entradaForm.value.diagnostico,
      tratamientoRealizado:this.entradaForm.value.tratamientoRealizado||undefined,
      observaciones:this.entradaForm.value.observaciones || undefined,
      citaId:this.historialCitaId || undefined
    };

    this.historiaClinicaService.crear(request).subscribe({
     next:(nueva)=>{
      this.entradasHistorial=[nueva,...this.entradasHistorial];
      this.entradaForm.reset();
      this.guardandoEntrada=false;
      this.cdr.detectChanges();
     },
     error:()=>{
      this.errorHistorial='Error saving the clinical record.';
      this.guardandoEntrada=false;
      this.cdr.detectChanges();
     }
    });
  }
  descargarPdf(){
    if(!this.historialNumeroDocumento) return;

    this.descargandoPdf=true;
    const numeroDocumento =this.historialNumeroDocumento;

    this.historiaClinicaService.descargarPdf(numeroDocumento).subscribe({
      next:(blob)=>{
        const url = window.URL.createObjectURL(blob);
        const link =document.createElement('a');
        link.href=url;
        link.download= `historia-clinica-${numeroDocumento}.pdf`;
        link.click();
        window.URL.revokeObjectURL(url);
        this.descargandoPdf=false;
        this.cdr.detectChanges();
      },
      error:()=>{
        this.errorHistorial='Error generating the PDF';
        this.descargandoPdf=false;
        this.cdr.detectChanges();
      }
    });
  }

  cargarPorFecha() {
    if (!this.fechaSeleccionada) {
      this.cargarTodasLasCitas();
      return;
    }
    this.loading = true;
    this.error = '';
    this.appointmentService.citasPorFecha(this.fechaSeleccionada).subscribe({
      next: (data) => {
        this.citas = data;
        this.aplicarFiltros();
        this.loading = false;
        this.cdr.detectChanges();
      },
      error: () => {
        this.error = 'Error al cargar citas por fecha.';
        this.loading = false;
        this.cdr.detectChanges();
      }
    });
  }

  aplicarFiltros() {
    if (this.filtroEstado === 'TODOS') {
      this.citasFiltradas = [...this.citas];
    } else {
      this.citasFiltradas = this.citas.filter(c => c.estado === this.filtroEstado);
    }
    this.citasSeleccionadas.clear();
    this.seleccionarTodos = false;
  }

  onFiltroEstadoChange() { this.aplicarFiltros(); }
  onFechaChange() { this.cargarPorFecha(); }

  limpiarFiltros() {
    this.fechaSeleccionada = '';
    this.filtroEstado = 'TODOS';
    this.citasFiltradas = [...this.citas];
    this.citasSeleccionadas.clear();
    this.seleccionarTodos = false;
  }

  refresh() {
    this.fechaSeleccionada = '';
    this.filtroEstado = 'TODOS';
    this.error = '';
    this.cargarTodasLasCitas();
  }

  // ── Selección múltiple ──
  toggleSeleccion(id: number) {
    const numId = Number (id);
    if (this.citasSeleccionadas.has(numId)) {
      this.citasSeleccionadas.delete(numId);
    } else {
      this.citasSeleccionadas.add(numId);
    }
    this.seleccionarTodos = this.citasSeleccionadas.size === this.citasFiltradas.length;
  }

  toggleSeleccionarTodos() {
    if (this.seleccionarTodos) {
      this.citasFiltradas.forEach(c => this.citasSeleccionadas.add(c.id));
    } else {
      this.citasSeleccionadas.clear();
    }
  }

  estaSeleccionada(id: number): boolean {
    return this.citasSeleccionadas.has(Number(id));
  }

  // ── Eliminar seleccionadas (solo del frontend por ahora) ──
  eliminarSeleccionadas() {
    if (this.citasSeleccionadas.size === 0) return;
    const ids = Array.from(this.citasSeleccionadas).map (id=>Number(id));
    
    // Se verifica que todas sean CANCELADAS o COMPLETADAS
    const hayPendientes = ids.some(id=>{
      const cita = this.citas.find(c=> Number(c.id)===id);
      return cita?.estado==='PENDIENTE';
    });
    if(hayPendientes){
      this.error='No se puede eliminar las citas PENDIENTES, cancélala primero';
      return;
    }
    //Elimina una por una en el Backend
    let eliminadas = 0;
    ids.forEach(id =>{
      this.appointmentService.eliminarCita(id).subscribe({
        next:()=>{
          eliminadas++;
          if(eliminadas ===ids.length){
          this.citas = this.citas.filter(c => !ids.includes(Number(c.id)));
          this.aplicarFiltros();
          this.citasSeleccionadas.clear();
          this.seleccionarTodos = false;
          this.cdr.detectChanges();
          }
        },
        error:(err)=>{
          this.error = err.error?.message ||'Error al eliminar alguna cita';
          this.cdr.detectChanges();
        }
      });
    });
    
  }

  completarCita(id: number) {
    this.appointmentService.cambiarEstado(id, 'COMPLETADA').subscribe({
      next: (citaActualizada) => {
        this.citas = this.citas.map(c => c.id === id ? citaActualizada : c);
        this.aplicarFiltros();
        this.cdr.detectChanges();
      },
      error: () => {
        this.error = 'Error al completar la cita.';
        this.cdr.detectChanges();
      }
    });
  }

  cancelarCita(id: number) {
    this.appointmentService.cambiarEstado(id, 'CANCELADA').subscribe({
      next: (citaActualizada) => {
        this.citas = this.citas.map(c => c.id === id ? citaActualizada : c);
        this.aplicarFiltros();
        this.cdr.detectChanges();
      },
      error: () => {
        this.error = 'Error al cancelar la cita.';
        this.cdr.detectChanges();
      }
    });
  }

  logout() { this.authService.logout(); }

  getBadgeClass(estado: string): string {
    const map: Record<string, string> = {
      'PENDIENTE': 'badge-pendiente',
      'COMPLETADA': 'badge-completada',
      'CANCELADA': 'badge-cancelada'
    };
    return map[estado] || 'badge-pendiente';
  }

  formatHora(hora: string): string {
    return hora ? hora.substring(0, 5) : '';
  }

  formatFecha(fecha: string): string {
    if (!fecha) return '';
    const [y, m, d] = fecha.split('-');
    return `${d}/${m}/${y}`;
  }

}