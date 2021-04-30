package br.com.unip.jogodavelha.game.cliente_chat;

import lombok.extern.slf4j.Slf4j;

import java.io.InputStream;
import java.util.Scanner;

import static br.com.unip.jogodavelha.service.ClienteServiceImpl.getMessages;
import static br.com.unip.jogodavelha.service.ClienteServiceImpl.setOpponentMessage;

@Slf4j
class RecebedorMensagemServidor implements Runnable {

    private final InputStream servidor;

    public RecebedorMensagemServidor(InputStream servidor) {
        this.servidor = servidor;
    }

    public void run() {
        try (Scanner scanner = new Scanner(this.servidor)) {
            while (scanner.hasNextLine()) {
                log.info("Nova mensagem...");
                log.info(scanner.nextLine());
                getMessages().add(scanner.nextLine());
                setOpponentMessage(true);
            }
        }
    }
}
