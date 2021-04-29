package br.com.unip.jogodavelha.game.cliente;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.net.Socket;

@Getter
@Setter
@Slf4j
@NoArgsConstructor
@Component
public class ClienteJogo {

    private String host;

    private int porta;

    private Socket socketCliente;

    private PrintStream saida;

    private String id;

    private String idOponente;

    private String login;

    private Recebedor recebedor;

    @Setter
    private static ClienteJogo clienteJogo;

    public ClienteJogo(String host, int porta, String login) {
        this.host = host;
        this.porta = porta;
        this.login = login;
        setClienteJogo(this);
    }

    public void executa() throws IOException {
        this.socketCliente = new Socket(this.host, this.porta);
        log.info("Cliente conectado no servidor do jogo");
        InputStream inputStream = this.socketCliente.getInputStream();
        recebedor = new Recebedor(inputStream);
        Thread threadRecebedor = new Thread(recebedor);
        threadRecebedor.start();
    }

    public void sendToGameServer(String dado) throws IOException {
        // lê msgs do teclado e manda pro servidor
        log.info("Enviando mensagem para o servidor");
        this.saida = new PrintStream(this.socketCliente.getOutputStream());
        this.saida.println(dado);
    }

    public void fechaConexao() throws IOException {
        this.saida.close();
        this.socketCliente.close();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getIdOponente() {
        return idOponente;
    }

    public void setIdOponente(String idOponente) {
        this.idOponente = idOponente;
    }

    public static synchronized ClienteJogo getInstance() {
        if (clienteJogo == null) {
            clienteJogo = new ClienteJogo("127.0.0.1", 11111, "Login");
        }
        return clienteJogo;
    }
}
