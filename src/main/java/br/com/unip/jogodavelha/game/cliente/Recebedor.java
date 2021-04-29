package br.com.unip.jogodavelha.game.cliente;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.io.InputStream;
import java.util.Scanner;

import static br.com.unip.jogodavelha.service.ClienteServiceImpl.*;
import static java.lang.Long.parseLong;

@Getter
@Slf4j
public class Recebedor implements Runnable {

    private final InputStream servidor;

    private Velha velha;

    public Recebedor(InputStream servidor) {
        this.servidor = servidor;
    }

    @Override
    public void run() {
        Scanner scanner = new Scanner(servidor);
        while (scanner.hasNextLine()) {
            String entrada = scanner.nextLine();
            this.trataEntrada(entrada);
            log.info(entrada);
        }
    }

    public void trataEntrada(String entrada) {
        String[] array = entrada.split(";");
        switch (array[0]) {
            case "logar":
                //Id, loginOponente, idOponente
                logar(array[1], array[2], array[3]);
                break;
            case "jogar":
                jogar(entrada);
                break;
            default:
                break;
        }
    }

    public void logar(String id, String loginOponente, String idOponente) {
        ClienteJogo clienteJogo = ClienteJogo.getInstance();
        clienteJogo.setId(id);
        clienteJogo.setIdOponente(idOponente);
        log.info("ClienteLongin: {}", loginOponente);
        log.info("MeuloginRecebedor: {}", clienteJogo.getLogin());
        this.velha = new Velha(clienteJogo.getLogin(), loginOponente, clienteJogo);
        setMyId(parseLong(id));
        setOpponentId(parseLong(idOponente));
        setMyNickname(this.velha.getNomeJogador());
        setOpponentNickname(this.velha.getNomeOponente());
        setIsAllowed(true);
    }

    public void jogar(String entrada) {
        String[] array = entrada.split(";");
        setButtonNumberToBlock(array[1]);
        String currentNicknameTurn = !array[2].equals(this.velha.getNomeJogador()) ? this.velha.getNomeJogador() : this.velha.getNomeOponente();
        setCurrentNicknameTurn(currentNicknameTurn);
        int retorno = this.velha.verificaMatriz();
        this.verificaQuemVenceu(retorno, this.velha.getNomeJogador(), this.velha.getNomeOponente());
    }

    private void verificaQuemVenceu(int retorno, String nomeJogador, String nomeOponente) {
        if (retorno == 1) {
            log.info("Jogador {} venceu", nomeJogador);
        } else if (retorno == -1) {
            log.info("Jogador {} venceu", nomeOponente);
        }
    }
}