import WebSocketService from './WebSocketService.js';

WebSocketService.connect();

const chatDiv = document.getElementById('chat');

function appendMessage(msg) {
    const div = document.createElement('div');
    div.textContent = msg;
    chatDiv.appendChild(div);
}

function sendMessage() {
    const input = document.getElementById('messageInput');
    const message = input.value;
    if (message) {
        WebSocketService.sendMessage(message);
        appendMessage("Вы: " + message);
        input.value = '';
    }
}

window.sendMessage = sendMessage;

// Подписка уже настроена в WebSocketService
WebSocketService.client.onConnect = () => {
    WebSocketService.client.subscribe('/topic/messages', (messageOutput) => {
        appendMessage("Сервер: " + messageOutput.body);
    });
};
