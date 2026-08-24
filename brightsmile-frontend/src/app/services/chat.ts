import { Injectable } from '@angular/core';
import { Client, Message } from '@stomp/stompjs';
import SockJS from 'sockjs-client';
import { BehaviorSubject, Observable } from 'rxjs';

export interface ChatMessage {
  sender: string;
  content: string;
  timestamp: number;
  typing: boolean;
}

@Injectable({
  providedIn: 'root'
})
export class ChatService {

  private apiUrl = 'http://localhost:8080';
  private stompClient: Client | null = null;
  private conectado = false; // ← bandera que faltaba, evita conexiones duplicadas

  private messagesSubject = new BehaviorSubject<ChatMessage[]>([]);
  messages$ = this.messagesSubject.asObservable();

  private connectedSubject = new BehaviorSubject<boolean>(false);
  isConnected$ = this.connectedSubject.asObservable();

  private typingSubject = new BehaviorSubject<boolean>(false);
  typing$ = this.typingSubject.asObservable();

  private chatOpenSubject = new BehaviorSubject<boolean> (false);
  chatOpen$ = this.chatOpenSubject.asObservable();

  private sessionId: string;

  constructor() {
    this.sessionId = 'session-' + Math.random().toString(36).substr(2, 9);
  }

  connect(): void {
    if (this.conectado) return; // ← evita crear un segundo cliente STOMP

    this.stompClient = new Client({
      webSocketFactory: () => new SockJS(`${this.apiUrl}/ws`),

      onConnect: () => {
        this.conectado = true;
        this.connectedSubject.next(true);

        this.stompClient?.subscribe(
          `/topic/chat/${this.sessionId}`,
          (message: Message) => {
            const chatMessage: ChatMessage = JSON.parse(message.body);

            if (chatMessage.typing) {
              this.typingSubject.next(true);
              return;
            }

            this.typingSubject.next(false);
            const current = this.messagesSubject.getValue();
            this.messagesSubject.next([...current, chatMessage]);
          }
        );
      },

      onDisconnect: () => {
        this.conectado = false;
        this.connectedSubject.next(false);
      },

      reconnectDelay: 5000
    });

    this.stompClient.activate();
  }

  sendMessage(content: string): void {
    if (!this.stompClient?.connected) return;

    const message: ChatMessage = {
      sender: this.sessionId,
      content: content,
      timestamp: Date.now(),
      typing: false
    };

    const current = this.messagesSubject.getValue();
    this.messagesSubject.next([...current, {
      ...message,
      sender: 'USER'
    }]);

    this.stompClient.publish({
      destination: '/app/chat.send',
      body: JSON.stringify(message)
    });
  }

  disconnect(): void {
    this.stompClient?.deactivate();
    this.conectado = false;
    this.connectedSubject.next(false);
  }

  clearMessages(): void {
    this.messagesSubject.next([]);
  }

  getSessionId(): string {
    return this.sessionId;
  }

  abrirChat(): void {
    this.chatOpenSubject.next(true);
  }

  confirmarChatAbierto(): void {
    this.chatOpenSubject.next(false);
  }
}