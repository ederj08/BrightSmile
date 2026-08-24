import { Component, OnInit, OnDestroy, ViewChild, ElementRef, AfterViewChecked, ChangeDetectorRef} from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { ChatService, ChatMessage} from '../../services/chat';
import { Subscription } from 'rxjs';


@Component({
  selector: 'app-chat',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './chat.html',
  styleUrl: './chat.css'
})
export class Chat implements OnInit, OnDestroy, AfterViewChecked {
  @ViewChild('scrollContainer') scrollContainer!: ElementRef;

  mensajes: ChatMessage[] = [];
  inputTexto: string = '';
  chatAbierto: boolean = false;
  escribiendo: boolean = false;

  private sub!: Subscription;
  private subTyping!: Subscription;
  private subAbrir!: Subscription;

  constructor(private chatService: ChatService,
              private cdr: ChangeDetectorRef
  ) {}

  ngOnInit(): void {
    this.chatService.connect();

    this.sub = this.chatService.messages$.subscribe((msgs)=>{
      this.mensajes = msgs;
      this.cdr.detectChanges();
    });
    this.subTyping = this.chatService.typing$.subscribe((estaEscribiendo)=>{
      this.escribiendo=estaEscribiendo;
      this.cdr.detectChanges();
    })

    this.subAbrir = this.chatService.chatOpen$.subscribe((abrir)=>{
      if (abrir){
        this.chatAbierto = true;
        this.chatService.confirmarChatAbierto();
        this.cdr.detectChanges();
      }
    });

  }

  toggleChat(): void {
    this.chatAbierto = !this.chatAbierto;
  }

  enviar(): void {
    const texto = this.inputTexto.trim();
    if (!texto) return;

    // El servicio ya agrega el mensaje del usuario a messages$ internamente,
    // así que NO lo empujamos manualmente aquí (evitaríamos duplicados)
    this.chatService.sendMessage(texto);
    this.inputTexto = '';
  }

  onEnterPress(event:KeyboardEvent):void{
    if (!event.shiftKey){
      event.preventDefault();
      this.enviar();
    }
  }

  ngAfterViewChecked(): void {
    if (this.scrollContainer) {
      this.scrollContainer.nativeElement.scrollTop = this.scrollContainer.nativeElement.scrollHeight;
    }
  }

  ngOnDestroy(): void {
    this.sub?.unsubscribe();
    this.subTyping?.unsubscribe();
    this.chatService.disconnect();
    this.subAbrir?.unsubscribe();
  }
}