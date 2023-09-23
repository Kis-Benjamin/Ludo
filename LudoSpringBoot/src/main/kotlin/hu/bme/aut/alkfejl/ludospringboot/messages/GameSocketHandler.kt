package hu.bme.aut.alkfejl.ludospringboot.messages

import org.springframework.stereotype.Component
import org.springframework.web.socket.TextMessage
import org.springframework.web.socket.WebSocketSession
import org.springframework.web.socket.handler.TextWebSocketHandler
import java.io.IOException


@Component
class GameSocketHandler : TextWebSocketHandler() {
    @Throws(IOException::class)
    override fun handleTextMessage(session: WebSocketSession, message: TextMessage) {
        val payload: String = message.payload
        session.sendMessage(TextMessage("Hi $payload how may we help you?"))
    }

    @Throws(Exception::class)
    override fun afterConnectionEstablished(session: WebSocketSession) {
        session.sendMessage(TextMessage("Connected!"))
    }
}
