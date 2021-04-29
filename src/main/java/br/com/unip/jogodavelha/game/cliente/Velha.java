package br.com.unip.jogodavelha.game.cliente;

import br.com.unip.jogodavelha.service.ClienteServiceImpl;
import lombok.Getter;

import javax.swing.*;
import java.io.IOException;

@Getter
public class Velha {

    private final int jogador = 1;

    public String nomeJogador = "";

    public String nomeOponente = "";

    private final ClienteJogo cliente;

    public Velha(String nomeJogador, String nomeOponente, ClienteJogo cliente) {
        this.nomeJogador = nomeJogador;
        this.nomeOponente = nomeOponente;
        this.cliente = cliente;
    }

    public int verificaMatriz() {

        return 2;
    }

    public int checkWhoWon() {
        int retorno = verificaMatriz();
        if (retorno == 1) {
            JOptionPane.showMessageDialog(null, nomeJogador + " venceu o jogo!");
            return 0;
        } else if (retorno == -1) {
            JOptionPane.showMessageDialog(null, nomeOponente + " venceu o jogo!");
            return 0;
        }
        return 1;

    }

    public void sendNumberButtonToServer(int buttonNumber) {
        try {
            cliente.sendToGameServer("jogar;" + cliente.getId() + ";" + buttonNumber + ";" + cliente.getLogin());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
