package br.com.unip.jogodavelha.game.cliente_chat;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.PrintStream;
import java.net.Socket;
import java.util.Scanner;

import static java.lang.System.in;

@Slf4j
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Component
public class ClienteChat {

    private String host;

    private Integer porta;

    public void executa() throws IOException {
        try (Socket cliente = new Socket(this.host, this.porta);
             Scanner teclado = new Scanner(in);
             PrintStream saida = new PrintStream(cliente.getOutputStream())) {
            log.info("Cliente conectado no servidor do chat...");

            RecebedorMensagemServidor recebedorMensagem = new RecebedorMensagemServidor(cliente.getInputStream());
            new Thread(recebedorMensagem).start();

            while (teclado.hasNextLine()) {
                log.info("Digite a sua mensagem...");
                saida.println(teclado.nextLine());
            }
        }
    }
}
