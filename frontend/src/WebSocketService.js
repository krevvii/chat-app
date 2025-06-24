import { Client } from '@stomp/stompjs';
import SockJS from 'sockjs-client';

const socketUrl = 'http://localhost:8080/ws'; // URL WebSocket сервера

class WebSocketService {
    constructor() {
        this.client = new Client({
            brokerURL: socketUrl,
            connectHeaders: {},
            debug: function (str) {
                console.log(str);
            },
            onConnect: () => {
                this.client.subscribe('/topic/messages', (messageOutput) => {
                    console.log("Получено сообщение:", messageOutput.body);
                });
            },
            onStompError: (frame) => {
                console.error('STOMP ошибка:', frame);
            },
        });
    }

    connect() {
        this.client.activate();
    }

    sendMessage(message) {
        this.client.publish({ destination: '/app/send', body: message });
    }
}

export default new WebSocketService();
