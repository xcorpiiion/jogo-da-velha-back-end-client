package br.com.unip.jogodavelha.game.cliente;

import br.com.unip.jogodavelha.game.cliente_chat.ClienteChat;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Slf4j
@Getter
@Component
public class ServidorJogo {

    private ClienteChat clienteChat;

    public void conectaComServidorChat(String ipServidor, String playerName) {
        Thread threadChat = new Thread(() -> {
            try {
                clienteChat = new ClienteChat();
                clienteChat.setHost(ipServidor);
                clienteChat.setPorta(12345);
                clienteChat.executa();
            } catch (IOException e) {
                log.info(e.getMessage(), e.getCause());
            }

        });
        threadChat.setName("Chat do jogador: " + playerName);
        threadChat.start();
    }

}
