package hu.bme.aut.alkfejl.ludospringboot.config

import hu.bme.aut.alkfejl.ludospringboot.messages.GameSocketHandler
import org.springframework.context.annotation.Configuration
import org.springframework.web.socket.config.annotation.EnableWebSocket
import org.springframework.web.socket.config.annotation.WebSocketConfigurer
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry

@Configuration
@EnableWebSocket
class WebSocketConfiguration : WebSocketConfigurer {
    override fun registerWebSocketHandlers(registry: WebSocketHandlerRegistry) {
        registry.addHandler(GameSocketHandler(), "/game-websocket").setAllowedOrigins("*")
    }
}
